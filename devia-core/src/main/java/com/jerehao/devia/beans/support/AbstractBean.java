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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;

import com.jerehao.devia.beans.build.BeanBuilder;
import com.jerehao.devia.beans.exception.NoBeanNameException;
import com.jerehao.devia.beans.support.inject.FieldInjectPoint;
import com.jerehao.devia.beans.support.inject.MethodInjectPoint;
import com.jerehao.devia.core.util.AnnotationUtils;
import com.jerehao.devia.core.util.Assert;
import com.jerehao.devia.core.util.ClassUtils;
import com.jerehao.devia.core.util.ReflectionUtils;
import com.jerehao.devia.logging.Logger;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-09 16:37 jerehao
 */
public abstract class AbstractBean<T> extends BeanDefinition<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractBean.class);

    private static final Class<Inject> INJECT_ANNOTATION_CLASS = Inject.class;

    private static final Class<Named> NAMED_ANNOTATION_CLASS = Named.class;

    private T singletonInstance = null;


    protected AbstractBean(Class<T> clazz, BeanBuilder beanBuilder) throws NoBeanNameException {
        Assert.notNull(clazz);
        setBeanBuilder(beanBuilder);
        initBean(clazz);
    }

    @Override
    public T getBeanInstance() {
        if(singletonInstance == null)
            try {
                singletonInstance = this.getBeanBuilder().getBeanReference(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

        return singletonInstance;
    }

    @Override
    public int hashCode() {
        return this.getBeanName().hashCode();
    }

    private void initBean(Class<T> clazz) throws NoBeanNameException {
        //clazz must be set first, because all init*() method after use it
        setClazz(clazz);

        setScope(BeanScope.SINGLETON);

        setBeanName(determineBeanName(clazz));
        addTypes(ClassUtils.getAllTypes(clazz));

        initQualifiers();

        initFieldInjects();
        initMethodInjects();
        initConstructorInjects();
        //TODO 初始化各个注入点（field，method，构造方法）

        initBean();
    }

    /**
     * 空实现，留给子类继承
     */
    protected void initBean() {
    };

    private void initQualifiers() {
        for(Annotation annotation : getClazz().getAnnotations()) {
            if (AnnotationUtils.getMetaAnnotations(annotation.annotationType()).contains(Qualifier.class)) {
                addQualifier(annotation);
            }
        }
    }

    private void initFieldInjects() {
        final Set<Field> fields = ReflectionUtils.getAllFields(getClazz());
        for(final Field field : fields) {
            if(field == null || !field.isAnnotationPresent(INJECT_ANNOTATION_CLASS))
                continue;

            FieldInjectPoint injectPoint = new FieldInjectPoint(field);
            field.setAccessible(true);
            addFieldInjectPoint(injectPoint);
        }
    }

    private void initMethodInjects() {
        final Set<Method> methods = ReflectionUtils.getAllMethods(getClazz());
        for(final Method method : methods) {
            if(method == null || !method.isAnnotationPresent(INJECT_ANNOTATION_CLASS))
                continue;

            MethodInjectPoint injectPoint = new MethodInjectPoint(method);
            addMethodInjectPoint(injectPoint);

        }
    }

    private void initConstructorInjects() {

    }

    private String determineFieldRestrictName(Field field) {
        if(field.isAnnotationPresent(NAMED_ANNOTATION_CLASS)) {
            Named named = field.getAnnotation(NAMED_ANNOTATION_CLASS);
            return named.value();
        }
        return "";
    }

    private String determineBeanName(final Class<?> clazz) throws NoBeanNameException {
        String beanName = "";
        if(clazz.isAnnotationPresent(Named.class))
            beanName = clazz.getAnnotation(Named.class).value();
        if(StringUtils.isEmpty(beanName)) {
            beanName = clazz.getSimpleName();
            if(!StringUtils.isEmpty(beanName))
                beanName = beanName.substring(0,1).toLowerCase() + beanName.substring(1);
        }
        if(StringUtils.isEmpty(beanName))
            throw new NoBeanNameException();
        return beanName;
    }
}
