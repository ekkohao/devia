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

import com.jerehao.devia.beans.exception.BeanCreateException;
import com.jerehao.devia.beans.exception.MultipleBeanException;
import com.jerehao.devia.beans.support.Bean;
import com.jerehao.devia.beans.build.BeanBuilder;
import com.jerehao.devia.beans.exception.NoSuchBeanException;
import com.jerehao.devia.beans.support.inject.Qualifiee;
import com.jerehao.devia.common.annotation.NotNull;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-11 16:36 jerehao
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    private BeanBuilder beanBuilder;

    private Set<Bean<?>> beans = new LinkedHashSet<>();

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

    @SuppressWarnings({"all"})
    @Override
    public <T> Bean<T> getBean(String beanName) throws MultipleBeanException, NoSuchBeanException {
        Bean<T> find = null;
        for (Bean<?> bean : beans) {
            if (StringUtils.equals(bean.getBeanName(), beanName)) {
                if(find == null) {
                    find = (Bean<T>) bean;
                }
                else {
                    String msg;
                    msg = "There is multiple bean exists, so we can't decide which one you wangt get.";
                    msg += "\n\t\t\t1. " + find.getBeanClass().getTypeName();
                    msg += "\n\t\t\t2. " + bean.getBeanClass().getTypeName();
                    throw new MultipleBeanException(msg);
                }
            }
        }
        if(find == null)
            throw new NoSuchBeanException("Cannot find bean with name [" + beanName + "]");
        return find;
    }


    @Override
    public <T> Bean<T> getBean(Class<T> beanClass) throws MultipleBeanException, NoSuchBeanException {
        return getBean(beanClass, null);
    }

    @Override
    public <T> Bean<T> getBean(Class<T> beanClass, Set<Qualifiee> qualifiees) throws MultipleBeanException, NoSuchBeanException {
        Iterator<Type> itr = type2BeansMap.keySet().iterator();
        Bean<T> find = null;
        while (itr.hasNext()) {
            find = getBean(type2BeansMap.get(itr.next()), qualifiees);
        }

        if(find == null)
            throw new NoSuchBeanException("Cannot find bean with class [" + beanClass.getName() + "]");
        return find;
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

    @SuppressWarnings("all")
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
        if(beans.add(bean)) {
            for (Type type : bean.getTypes())
                putType2BeansMap(type, bean);
        }
        else
            throw new BeanCreateException("There is already bean with name [" + bean.getBeanName() + "]");

    }

    private void putType2BeansMap(Type type, Bean<?> bean) {
        if(type2BeansMap.containsKey(type))
            type2BeansMap.get(type).add(bean);
        else  {
            List<Bean<?>> list = new LinkedList<>();
            list.add(bean);
            type2BeansMap.put(type,list);
        }
    }
}
