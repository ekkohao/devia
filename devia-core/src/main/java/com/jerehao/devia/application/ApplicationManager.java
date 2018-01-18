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
import com.jerehao.devia.bean.build.BeanBuilder;
import com.jerehao.devia.config.*;
import com.jerehao.devia.bean.DeviaBeanFactory;
import com.jerehao.devia.common.annotation.NotNull;
import com.jerehao.devia.config.annotation.WebResource;
import com.jerehao.devia.core.util.StringUtils;
import com.jerehao.devia.logging.Logger;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-11 14:50 jerehao
 */

public final class ApplicationManager {

    private static final Logger LOGGER = Logger.getLogger(ApplicationManager.class);

    private static boolean running = false;

    private static BeanFactory beanFactory;

    private static ResourceMappingStorer resourceMappingStorer;


    public static BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public static ResourceMappingStorer getResourceMappingStorer() {
        return resourceMappingStorer;
    }

    public static void start(@NotNull Class<?> configClass) {
        if(running) {
            LOGGER.error("Application already started.");
            return;
        }


        LOGGER.info(StringUtils.build("To use config class [{0}]", configClass.getName()));

        Configuration configuration = ApplicationConfigReader.reader(configClass);
        initApplication(configuration);

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

    private static void initApplication(Configuration configuration) {
        initBeanFactory(configuration.getAutoScanPackage());
        initWebResources(configuration.getWebResources());
    }

    private static void initBeanFactory(String scanPaths) {
        beanFactory = DeviaBeanFactory.getBeanFactory();
        final BeanBuilder beanBuilder = beanFactory.getBeanBuilder();
        Set<Class<?>> classes = ComponentScan.getPathClasses(scanPaths, Configuration.autoScanAnnotationTypes);
        beanBuilder.createBeans(classes);
    }

    private static void initWebResources(List<WebResource> webResources) {
        resourceMappingStorer = WebResourceMapping.getResourceMappingStore(webResources);
    }

    private ApplicationManager() {}
}
