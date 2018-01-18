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

package com.jerehao.devia.bean.support;

import com.jerehao.devia.bean.BeanFactory;
import com.jerehao.devia.bean.support.inject.ConstructorInjectPoint;
import com.jerehao.devia.bean.support.inject.FieldInjectPoint;
import com.jerehao.devia.bean.support.inject.MethodInjectPoint;
import com.jerehao.devia.bean.support.inject.Qualifiee;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-03 10:00 jerehao
 */
public abstract class BeanDefinition<T> implements Bean<T> {

    private String beanName;

    private Class<T> beanClass;

    private Class<T> proxyClass;

    private BeanScope scope;

    private BeanFactory beanFactory;

    private final Set<Type> types = new LinkedHashSet<>();

    private final Map<Class<? extends Annotation>, Qualifiee> qualifieeMap = new HashMap<>();



    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public Class<T> getBeanClass() {
        return beanClass;
    }

    @Override
    public Class<T> getProxyClass() {
        return proxyClass;
    }

    @Override
    public BeanScope getScope() {
        return scope;
    }

    @Override
    public Set<Type> getTypes() {
        return types;
    }

    @Override
    public Collection<Qualifiee> getQualifiees() {
        return qualifieeMap.values();
    }

    @Override
    public boolean containsQualifiee(Class<? extends Annotation> clazz) {
        return this.qualifieeMap.containsKey(clazz);
    }

    @Override
    public Qualifiee getQualifiee(Class<? extends Annotation> clazz) {
        if(containsQualifiee(clazz))
            return this.qualifieeMap.get(clazz);
        else
            return null;
    }

    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    //setter

    protected void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    protected void setBeanClass(Class<T> clazz) {
        this.beanClass = clazz;
    }

    protected void setProxyClass(Class<T> proxyClass) {
        this.proxyClass = proxyClass;
    }

    protected void setScope(BeanScope scope) {
        this.scope = scope;
    }

    protected void addTypes(Set<Type> types) {
        this.types.addAll(types);
    }

    protected void addQualifiee(Class<? extends Annotation> clazz,Qualifiee qualifiee) {
        this.qualifieeMap.put(clazz, qualifiee);
    }

    protected void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


}
