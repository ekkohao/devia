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

package com.jerehao.devia.beans.support;

import javax.inject.Scope;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-12 11:09 jerehao
 */
public enum  BeanScope {
    SINGLETON(Singleton.class);

    Class<? extends Annotation> clazz;

    BeanScope(Class<? extends Annotation> singletonClass) {
        this.clazz = singletonClass;
    }

    public Class<? extends Annotation> getClazz() {
        return clazz;
    }
}
