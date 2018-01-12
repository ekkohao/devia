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

import com.jerehao.devia.beans.build.BeanBuilder;
import com.jerehao.devia.beans.support.inject.FieldInjectPoint;
import com.jerehao.devia.beans.support.inject.MethodInjectPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-03 10:00 jerehao
 */
public abstract class BeanDefinition<T> implements Bean<T> {

    private String beanName;

    private Class<T> clazz;

    private BeanScope scope;

    private BeanBuilder beanBuilder;

    private final Set<Type> types = new LinkedHashSet<>();

    private final Set<Annotation> qualifiers = new LinkedHashSet<>();

    private final Set<FieldInjectPoint> fieldInjectPoints = new LinkedHashSet<>();

    private final Set<MethodInjectPoint> methodInjectPoints = new LinkedHashSet<>();

    private Set<Method> needInjectConstructor;

    public String getBeanName() {
        return beanName;
    }

    public Class<T> getClazz() {

        return clazz;
    }

    public BeanScope getScope() {
        return scope;
    }

    public Set<Type> getTypes() {
        return types;
    }

    public Set<? extends Annotation> getQualifiers() {
        return qualifiers;
    }

    @Override
    public Set<FieldInjectPoint> getFieldInjectPoints() {
        return fieldInjectPoints;
    }

    @Override
    public Set<MethodInjectPoint> getMethodInjectPoints() {
        return methodInjectPoints;
    }

    @Override
    public BeanBuilder getBeanBuilder() {
        return beanBuilder;
    }

    //setter

    protected void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    protected void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected void setScope(BeanScope scope) {
        this.scope = scope;
    }

    protected void addTypes(Set<Type> types) {
        this.types.addAll(types);
    }

    protected void addQualifier(Annotation qualifier) {
        this.qualifiers.add(qualifier);
    }

    protected void addFieldInjectPoint(FieldInjectPoint fieldInjectPoint) {
        this.fieldInjectPoints.add(fieldInjectPoint);
    }

    protected void addMethodInjectPoint(MethodInjectPoint methodInjectPoint) {
        this.methodInjectPoints.add(methodInjectPoint);
    }

    protected void setBeanBuilder(BeanBuilder beanBuilder) {
        this.beanBuilder = beanBuilder;
    }
}
