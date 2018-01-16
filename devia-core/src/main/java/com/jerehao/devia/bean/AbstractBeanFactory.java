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

package com.jerehao.devia.bean;

import com.jerehao.devia.bean.exception.BeanCreateException;
import com.jerehao.devia.bean.exception.MultipleBeanException;
import com.jerehao.devia.bean.support.Bean;
import com.jerehao.devia.bean.build.BeanBuilder;
import com.jerehao.devia.bean.exception.NoSuchBeanException;
import com.jerehao.devia.bean.support.inject.Qualifiee;
import com.jerehao.devia.common.annotation.NotNull;
import com.sun.istack.internal.Nullable;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-11 16:36 jerehao
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    private BeanBuilder beanBuilder;

    private Map<String, Bean<?>> name2BeanMap = new HashMap<>();

    private Map<Type, List<Bean<?>>> type2BeansMap = new HashMap<>();

    public AbstractBeanFactory() {
    }

    @Override
    public BeanBuilder getBeanBuilder() {
        return this.beanBuilder;
    }

    protected void setBeanBuilder(BeanBuilder beanBuilder) {
        this.beanBuilder = beanBuilder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Bean<T> getBean(String beanName) throws NoSuchBeanException {
        if(name2BeanMap.containsKey(beanName))
            return (Bean<T>) name2BeanMap.get(beanName);
        throw new NoSuchBeanException("Cannot find bean with name [" + beanName + "]");
    }

    @Override
    public <T> Bean<T> getBean(Type type) throws MultipleBeanException, NoSuchBeanException {
        return getBean(type, null);
    }

    @Override
    public <T> Bean<T> getBean(Type type,@Nullable Set<Qualifiee> qualifiees) throws MultipleBeanException, NoSuchBeanException {
        Bean<T> find = null;
        if(type2BeansMap.containsKey(type)) {
            find = getBean(type2BeansMap.get(type), qualifiees);
        }
        if(find == null)
            throw new NoSuchBeanException("Cannot find bean with class [" + type.getTypeName() + "]");
        return find;
    }

    @SuppressWarnings("unchecked")
    private <T> Bean<T> getBean(@NotNull List<Bean<?>> beanList, @Nullable Set<Qualifiee> qualifiees) throws MultipleBeanException {
        Bean<T> find = null;
        for(Bean<?> bean : beanList) {
            if (bean.satisfiedQualifiees(qualifiees)) {
                if (find == null)
                    find = (Bean<T>) bean;
                else {
                    String msg;
                    msg = "There is multiple bean exists, so we can't decide which one you wangt get.";
                    msg += "\n\t\t\t1. " + find.getBeanClass().getTypeName();
                    msg += "\n\t\t\t2. " + bean.getBeanClass().getTypeName();
                    throw new MultipleBeanException(msg);
                }
            }
        }
        return find;
    }
    @Override
    public <T> void addBean(Bean<T> bean) throws BeanCreateException {
        if(bean == null)
            return;

        if(name2BeanMap.putIfAbsent(bean.getBeanName(), bean) != null)
            throw new BeanCreateException("There is already bean with name [" + bean.getBeanName() + "]");

        for (Type type : bean.getTypes()) {
            if(!type2BeansMap.containsKey(type))
                type2BeansMap.putIfAbsent(type, new LinkedList<>());

            type2BeansMap.get(type).add(bean);
        }
    }

}
