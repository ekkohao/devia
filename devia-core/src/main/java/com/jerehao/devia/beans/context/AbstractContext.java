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

package com.jerehao.devia.beans.context;

import com.jerehao.devia.beans.exception.BeanCreateException;
import com.jerehao.devia.beans.exception.MultipleBeanException;
import com.jerehao.devia.beans.exception.NoSuchBeanException;
import com.jerehao.devia.beans.support.Bean;
import com.jerehao.devia.beans.support.BeanScope;
import com.jerehao.devia.beans.support.inject.Qualifiee;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-15 18:51 jerehao
 */
public abstract class AbstractContext implements Context {

    private final BeanScope scope;

    private final Map<Bean<?>, Object> instances = new HashMap<>();

    protected AbstractContext(BeanScope scope) {
        this.scope = scope;
    }

    @Override
    public BeanScope getScope() {
        return this.scope;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Bean<T> bean) throws MultipleBeanException, NoSuchBeanException, BeanCreateException {

        if(instances.containsKey(bean)) {
            return (T) instances.get(bean);
        }

        T instance = bean.create();
        if(instances.putIfAbsent(bean, instance) != null) {
            return (T) instances.get(bean);
        }
        return instance;
    }
}
