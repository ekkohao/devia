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

package com.jerehao.devia.config;

import com.jerehao.devia.common.annotation.NotNull;
import com.jerehao.devia.common.annotation.Nullable;
import com.jerehao.devia.config.annotation.ApplicationConfig;
import com.jerehao.devia.config.annotation.AutoScanPackage;
import com.jerehao.devia.config.annotation.WebResource;
import com.jerehao.devia.config.annotation.WebResources;
import com.jerehao.devia.core.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-16 14:08 jerehao
 */
public final class ApplicationConfigReader {

    public static Configuration reader(@NotNull Class<?> configClass) {

        if(configClass == null || !configClass.isAnnotationPresent(ApplicationConfig.class))
            throw new RuntimeException(StringUtils.build(
                    "Class [{0}] is not a config class, maybe it's null or there isn't annotation [{1}]",
                    configClass,
                    ApplicationConfig.class.getName()));

        String scanPackage = readAutoScanPackage(configClass);
        List<WebResource> resources = readWebResources(configClass);

        return new Configuration(scanPackage, resources);

    }

    private static String readAutoScanPackage(@NotNull Class<?> configClass) {
        if(!configClass.isAnnotationPresent(AutoScanPackage.class))
            return Configuration.DEFAULT_AUTO_SCAN_PACKAGE;

        return configClass.getAnnotation(AutoScanPackage.class).value();
    }

    private static List<WebResource> readWebResources(@NotNull Class<?> configClass) {
        List<WebResource> resources = new LinkedList<>();
        if(!configClass.isAnnotationPresent(WebResources.class))
            return resources;
        resources.addAll(Arrays.asList(configClass.getAnnotation(WebResources.class).value()));
        return resources;
    }

    private ApplicationConfigReader() {}
}
