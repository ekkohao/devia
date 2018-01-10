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

package com.jerehao.devia.beans;

import com.jerehao.devia.logging.Logger;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-09 16:28 jerehao
 */

public class DeviaBeanFactory implements BeanFactory {

    private static final Logger LOGGER = Logger.getLogger(DeviaBeanFactory.class);

    private Set<Bean<?>> beans = new HashSet<>();

    private Set<Bean<?>> builtInBeans = new HashSet<>();



    @Override
    public Object getBean(String beanName) {
        for (Bean<?> bean : beans) {
            if (StringUtils.equals(bean.getName(), beanName))
                return bean.getInstance();
        }
        for (Bean<?> bean : builtInBeans) {
            if (StringUtils.equals(bean.getName(), beanName))
                return bean.getInstance();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(String beanName, Class<T> beanClass) {
        for (Bean<?> bean : beans) {
            if (StringUtils.equals(bean.getName(), beanName) && beanClass.isInstance(bean.getInstance()))
                return (T) bean.getInstance();
        }
        for (Bean<?> bean : builtInBeans) {
            if (StringUtils.equals(bean.getName(), beanName) && beanClass.isInstance(bean.getInstance()))
                return (T) bean.getInstance();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> beanClass) {
        for (Bean<?> bean : beans) {
            if (beanClass.isInstance(bean.getInstance()))
                return (T) bean.getInstance();
        }
        for (Bean<?> bean : builtInBeans) {
            if (beanClass.isInstance(bean.getInstance()))
                return (T) bean.getInstance();
        }
        return null;
    }
}
