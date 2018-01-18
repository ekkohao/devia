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

import com.jerehao.devia.bean.annotation.Scope;
import com.jerehao.devia.bean.support.BeanScope;
import com.jerehao.devia.config.annotation.ApplicationConfig;
import com.jerehao.devia.config.annotation.AutoScanPackage;
import com.jerehao.devia.config.annotation.Bean;
import com.jerehao.devia.config.annotation.WebResource;
import com.jerehao.devia.servlet.HandlerExecutionChain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-16 14:14 jerehao
 */
@ApplicationConfig
@AutoScanPackage("com")
@WebResource(value = "/css", locationTo = "/resources/css", fileFilter = "*.css")
@WebResource(value = "/js", locationTo = "/resources/js" , fileFilter = "*.js")
public class AppConfig {

    @Bean
    @Scope(BeanScope.PROTOTYPE)
    public HandlerExecutionChain gets() {
        return new HandlerExecutionChain();
    }
}
