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

package com.jerehao.devia.servlet.helper;

import com.jerehao.devia.core.util.AntPathMather;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-05 16:31 jerehao
 *
 * @deprecated
 */
@Deprecated
public final class StaticResource {

    private static final String PATH_SEPARATOR = "/";

    /**
     * mapping -> location
     */
    private static final Map<String, String> staticResources = new HashMap<>();

    private StaticResource() {
    }

    public static void addMapping(String mapping, String location) {
        staticResources.put(mapping, location);
    }

    public static boolean isStatic(String uri) {
        Set<String> mappings = staticResources.keySet();
        for (String mapping : mappings) {
            //路径匹配
            if (AntPathMather.Match(mapping, uri))
                return true;
        }
        return false;
    }

    public static String getLocationURI(String uri) {
        Set<String> mappings = staticResources.keySet();
        String suffix = uri.substring(uri.lastIndexOf(PATH_SEPARATOR) + 1);
        String mappingURI = "";
        for (String mapping : mappings) {
            if (AntPathMather.Match(mapping, uri) && mapping.length() > mappingURI.length())
                mappingURI = mapping;
        }

        return StringUtils.isEmpty(mappingURI) ? null : staticResources.get(mappingURI) + suffix;
    }

}
