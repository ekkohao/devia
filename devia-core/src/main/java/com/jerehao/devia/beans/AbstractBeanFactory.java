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

import com.jerehao.devia.beans.support.Bean;
import com.jerehao.devia.beans.support.DeviaBean;
import com.jerehao.devia.beans.build.BeanBuilder;
import com.jerehao.devia.beans.exception.NoSuchBeanException;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-11 16:36 jerehao
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    protected BeanBuilder beanBuilder;

    private Set<Bean<?>> beans = new LinkedHashSet<>();

    public AbstractBeanFactory() {
    }

    @Override
    public BeanBuilder getBeanBuilder() {
        return this.beanBuilder;
    }

    protected void setBeanBuilder(BeanBuilder beanBuilder) {
        this.beanBuilder = beanBuilder;
    }

    @Override
    public <T> Bean<T> getBean(String beanName) {
        for (Bean<?> bean : beans) {
            if (StringUtils.equals(bean.getBeanName(), beanName))
                return (Bean<T>) bean;
        }
        throw new NoSuchBeanException("Cannot find bean with name [" + beanName + "]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Bean<T> getBean(Class<T> beanClass) {
        for (Bean<?> bean : beans) {
            for(Type type : bean.getTypes())
                if (StringUtils.equals(beanClass.getTypeName(), type.getTypeName()))
                    return (Bean<T>) bean;
        }

        throw new NoSuchBeanException("Cannot find bean with class [" + beanClass.getName() + "]");
    }

    @Override
    public <T> Bean<T> getBean(Type type) {
        for (Bean<?> bean : beans) {
            for(Type t : bean.getTypes())
                if (StringUtils.equals(t.getTypeName(), type.getTypeName()))
                    return (Bean<T>) bean;
        }

        throw new NoSuchBeanException("Cannot find bean with class [" + type.getTypeName() + "]");
    }

    @Override
    public <T> void addBean(Bean<T> bean) {
        if(bean != null)
            beans.add(bean);
    }
}
