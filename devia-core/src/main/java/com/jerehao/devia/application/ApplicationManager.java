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

import com.jerehao.devia.beans.BeanFactory;
import com.jerehao.devia.beans.build.BeanBuilder;
import com.jerehao.devia.beans.build.ComponentScan;
import com.jerehao.devia.beans.DeviaBeanFactory;

import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-11 14:50 jerehao
 */
public final class ApplicationManager {
    private static String scanPaths;

    private static boolean isRunning;

    private static BeanFactory beanFactory;

    static {
        scanPaths = ComponentScan.DEFAULT_SCAN_PACKAGE;
        isRunning = false;
    }

    public static void setScanPaths(String _scanPaths) {
        scanPaths = _scanPaths;
    }

    public static void start() {
        isRunning = true;

        initBeanFactory();
    }

    public static void stop() {
        if(!running())
            return;
        isRunning = false;
    }

    public static boolean running() {
        return isRunning;
    }

    public static BeanFactory getBeanFactory() {
        return beanFactory;
    }

    private ApplicationManager() {}

    private static void initBeanFactory() {
        beanFactory = DeviaBeanFactory.getInstance();
        final BeanBuilder beanBuilder = beanFactory.getBeanBuilder();

        Set<Class<?>> classes = ComponentScan.getPathClasses(scanPaths);
        beanBuilder.createBeans(classes);
    }
}
