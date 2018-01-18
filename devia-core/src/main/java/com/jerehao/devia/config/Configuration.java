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

import com.jerehao.devia.bean.annotation.Named;
import com.jerehao.devia.config.annotation.WebResource;
import com.jerehao.devia.core.util.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-16 15:24 jerehao
 */
public class Configuration {

    public static final String DEFAULT_AUTO_SCAN_PACKAGE = "**";

    public static final Class<?> autoScanAnnotationTypes[];

    static {
        autoScanAnnotationTypes = new Class<?>[] {
                AnnotationUtils.NAMED_CLASS,
                AnnotationUtils.JSR330.NAMED_CLASS
        };
    }

    private final String[] autoScanPackages;

    private final List<WebResource> resources;

    private final Class<?> configClass;

    public Configuration(String[] autoScanPackages, List<WebResource> resources, Class<?> configClass) {
        this.autoScanPackages = autoScanPackages;
        this.resources = resources;
        this.configClass = configClass;
    }

    public String[] getAutoScanPackage() {
        return autoScanPackages;
    }

    public List<WebResource> getWebResources() {
        return resources;
    }

    public Class<?> getConfigClass() {
        return configClass;
    }
}
