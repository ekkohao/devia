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

package com.jerehao.devia.application.config;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-17 20:14 jerehao
 */
public class ResourceMappingException extends Exception {

    public ResourceMappingException(String message) {
        super(message);
    }

    public ResourceMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceMappingException(Throwable cause) {
        super(cause);
    }

    public ResourceMappingException() {
        super();
    }
}
