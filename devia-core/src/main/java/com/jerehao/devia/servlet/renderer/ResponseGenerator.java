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

package com.jerehao.devia.servlet.renderer;

import com.jerehao.devia.servlet.helper.HttpMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-08 15:24 jerehao
 */
public abstract class ResponseGenerator {

    protected Set<HttpMethod> supportedMethods;

    protected String allowHeader;

    ResponseGenerator() {
        this(true);
    }

    ResponseGenerator(boolean restrictSupportedMethod) {
        this(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.POST);
    }

    ResponseGenerator(HttpMethod... methods) {
        setSupportedMethods(methods);
    }

    public void setSupportedMethods(HttpMethod... methods) {
        if(methods.length < 1) {
            supportedMethods = null;
            return;
        }

        supportedMethods = new LinkedHashSet<>(methods.length);
        supportedMethods.addAll(Arrays.asList(methods));

        if(!supportedMethods.contains(HttpMethod.OPTIONS))
            supportedMethods.add(HttpMethod.OPTIONS);

        initAllowHeader();
    }

    private void initAllowHeader() {
        Set<String> allowedMethods;
        if(supportedMethods == null) {
            allowedMethods = new LinkedHashSet<>(HttpMethod.values().length - 1);
            for (HttpMethod method : HttpMethod.values()) {
                if (!method.equals(HttpMethod.TRACE))
                    allowedMethods.add(method.name());
            }
        }
        else {
            allowedMethods = new LinkedHashSet<>(supportedMethods.size());
            supportedMethods.forEach((key)-> allowedMethods.add(key.name()));
        }
        allowHeader = StringUtils.join(allowedMethods,',');
    }

    public String getAllowHeader() {
        return this.allowHeader;
    }
}
