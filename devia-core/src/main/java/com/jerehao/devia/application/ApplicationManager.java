/*
 * Copyright (c) 2018, jerehao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jerehao.devia.application;

import com.jerehao.devia.bean.BeanFactory;
import com.jerehao.devia.bean.annotation.BeanAnnotationProcessor;
import com.jerehao.devia.application.config.*;
import com.jerehao.devia.bean.DeviaBeanFactory;
import com.jerehao.devia.core.common.annotation.NotNull;
import com.jerehao.devia.application.annotation.WebResource;
import com.jerehao.devia.core.util.StringUtils;
import com.jerehao.devia.logging.Logger;
import com.jerehao.devia.application.config.ResourceMappingStorer;
import com.jerehao.devia.application.config.WebResourceMapping;
import com.jerehao.devia.orm.jdbc.DatabaseEngine;
import com.jerehao.devia.orm.jdbc.MySQLDatabaseEngine;
import com.jerehao.devia.orm.model.DeviaModelManager;
import com.jerehao.devia.orm.model.ModelManager;
import com.jerehao.devia.orm.model.annotation.ModelAnnotationProcessor;

import javax.servlet.ServletContext;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-11 14:50 jerehao
 */

public final class ApplicationManager {

    private static final Logger LOGGER = Logger.getLogger(ApplicationManager.class);

    private static boolean running = false;

    private static BeanFactory beanFactory;

    private static ResourceMappingStorer resourceMappingStorer;

    private static ServletContext servletContext;

    private static DatabaseEngine databaseEngine;

    // function

    public static BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public static ResourceMappingStorer getResourceMappingStorer() {
        return resourceMappingStorer;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static void start(@NotNull Class<?> configClass, ServletContext servletContext) {
        if(running) {
            LOGGER.error("Application already started.");
            return;
        }

        ApplicationManager.servletContext = servletContext;

        //after read properties
        //to solve this 想办法不调用servletContext获取上下文路径
        beanFactory = DeviaBeanFactory.getBeanFactory();
        databaseEngine = MySQLDatabaseEngine.getDatabaseEngine();

        LOGGER.info(StringUtils.build("To use config class [{0}]", configClass.getName()));

        Configuration configuration = ApplicationConfigReader.reader(configClass);

        //init anything after config reader
        initConfiguration(configuration);
        afterInitConfiguration();

        beanFactory.get("aaa");

        running = true;
    }



    public static void stop() {
        if(!running())
            return;
        running = false;
    }

    public static boolean running() {
        return running;
    }

    private static void initConfiguration(Configuration configuration) {
        initBeanFactoryAndAnnotationProcessor(configuration.getAutoScanPackage(), configuration.getConfigClass());
        initWebResources(configuration.getWebResources());
    }

    private static void initBeanFactoryAndAnnotationProcessor(String[] scanPaths, Class<?> configClass) {

        List<AnnotationProcessor> annotationProcessors = new LinkedList<>();

        annotationProcessors.add(new BeanAnnotationProcessor(beanFactory.getBeanBuilder()));
        annotationProcessors.add(new ModelAnnotationProcessor(databaseEngine.getModelManager().getModelBuilder()));

        ComponentScan.doScanAndProcess(scanPaths, annotationProcessors);

        beanFactory.getBeanBuilder().createBean(configClass);
    }

    private static void initWebResources(List<WebResource> webResources) {
        resourceMappingStorer = WebResourceMapping.getResourceMappingStore(webResources);
    }

    private static void afterInitConfiguration() {
        initDatabase();
    }

    private static void initDatabase() {
        String mode = ApplicationProperties.getProperty(ApplicationProperties.Keys.DATABASE_MODE);

        if(StringUtils.equals(mode, "rebuild"))
            databaseEngine.rebuildDatabase();

    }

    private ApplicationManager() {}
}
