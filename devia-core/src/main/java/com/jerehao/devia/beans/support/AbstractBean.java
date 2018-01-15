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


import com.jerehao.devia.beans.annotation.Named;
import com.jerehao.devia.beans.build.BeanBuilder;
import com.jerehao.devia.beans.exception.BeanCreateException;
import com.jerehao.devia.beans.exception.NoBeanNameException;
import com.jerehao.devia.beans.support.inject.ConstructorInjectPoint;
import com.jerehao.devia.beans.support.inject.FieldInjectPoint;
import com.jerehao.devia.beans.support.inject.MethodInjectPoint;
import com.jerehao.devia.beans.support.inject.Qualifiee;
import com.jerehao.devia.common.annotation.Nullable;
import com.jerehao.devia.core.util.AnnotationUtils;
import com.jerehao.devia.core.util.Assert;
import com.jerehao.devia.core.util.ClassUtils;
import com.jerehao.devia.core.util.ReflectionUtils;
import com.jerehao.devia.logging.Logger;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-09 16:37 jerehao
 */
public abstract class AbstractBean<T> extends BeanDefinition<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractBean.class);

    private T singletonInstance = null;


    protected AbstractBean(Class<T> clazz, BeanBuilder beanBuilder) throws BeanCreateException {
        Assert.notNull(clazz);
        setBeanBuilder(beanBuilder);
        initBean(clazz);
    }

    @Override
    public T getBeanInstance() {
        if(singletonInstance == null)
            try {
                singletonInstance = this.getBeanBuilder().createBeanInstance(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

        return singletonInstance;
    }

    @Override
    public boolean hasConstructorInjectPoint() {
        return this.getConstructorInjectPoint() != null;
    }

    //包含Named
    @Override
    public boolean satisfiedQualifiees(@Nullable Set<Qualifiee> qualifiees) {
        if(qualifiees == null)
            return true;

        for(Qualifiee qualifiee : qualifiees) {
            if(Objects.equals(AnnotationUtils.NAMED_CLASS, qualifiee.getAnnotation().annotationType())) {
                Named checkingNamed = (Named) qualifiee.getAnnotation();
                String checkingName = StringUtils.isEmpty(checkingNamed.value()) ? qualifiee.getName() : checkingNamed.value();
                if(!StringUtils.equals(checkingName, getBeanName()))
                    return false;
            }
            else if (Objects.equals(AnnotationUtils.JSR330.NAMED_CLASS, qualifiee.getAnnotation().annotationType())) {
                javax.inject.Named checkingNamed = (javax.inject.Named) qualifiee.getAnnotation();
                String checkingName = StringUtils.isEmpty(checkingNamed.value()) ? qualifiee.getName() : checkingNamed.value();
                if(!StringUtils.equals(checkingName, getBeanName()))
                    return false;
            }
            else if(!containsQualifiee(qualifiee.getAnnotation().annotationType())){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.getBeanName().hashCode();
    }

    private void initBean(Class<T> clazz) throws BeanCreateException {
        //clazz must be set first, because all init*() method after use it
        setBeanClass(clazz);
        setProxyClass(clazz); ///TODO AOP Transaction

        setBeanName(determineBeanName(clazz));
        setScope(determineBeanScope(clazz));
        addTypes(ClassUtils.getAllTypes(clazz));

        //不包含Named
        initQualifiees();

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

    private void initQualifiees() {
        for(Annotation annotation : getBeanClass().getAnnotations()) {
            if(ClassUtils.equalsAny(annotation.annotationType(), AnnotationUtils.NAMED_CLASS, AnnotationUtils.JSR330.NAMED_CLASS))
                continue;
            if (AnnotationUtils.isQualifierAnnotation(annotation.annotationType()))
                addQualifiee(annotation.annotationType(), new Qualifiee(annotation, getBeanName()));

        }
    }

    private void initFieldInjects() {
        final Set<Field> fields = ReflectionUtils.getAllFields(getBeanClass());
        for(final Field field : fields) {
            if(field == null
                    || !(field.isAnnotationPresent(AnnotationUtils.INJECT_ClASS) || field.isAnnotationPresent(AnnotationUtils.JSR330.INJECT_ClASS)))
                continue;

            FieldInjectPoint injectPoint = new FieldInjectPoint(field);
            addFieldInjectPoint(injectPoint);
        }
    }

    private void initMethodInjects() {
        final Set<Method> methods = ReflectionUtils.getAllMethods(getBeanClass());
        for(final Method method : methods) {
            if(method == null
                    || !(method.isAnnotationPresent(AnnotationUtils.INJECT_ClASS) || method.isAnnotationPresent(AnnotationUtils.JSR330.INJECT_ClASS)))
                continue;

            MethodInjectPoint injectPoint = new MethodInjectPoint(method);
            addMethodInjectPoint(injectPoint);

        }
    }

    private void initConstructorInjects() throws BeanCreateException {
        Constructor<T>[] constructors = (Constructor<T>[]) getBeanClass().getConstructors();
        boolean findInject = false;
        for (Constructor<T> constructor : constructors) {
            if(constructor == null
                    || !(constructor.isAnnotationPresent(AnnotationUtils.INJECT_ClASS) || constructor.isAnnotationPresent(AnnotationUtils.JSR330.INJECT_ClASS)))
                continue;
            if(!findInject) {
                setConstructorInjectPoint(new ConstructorInjectPoint<T>(constructor));
                findInject = true;
            }
            else
                throw new BeanCreateException("find multiply inject constructor for class [" + getBeanClass().getTypeName() + "]");
        }
        if(!findInject)
            setConstructorInjectPoint(null);
    }

    private String determineBeanName(final Class<?> clazz) throws BeanCreateException {
        String beanName = null;
        if(clazz.isAnnotationPresent(AnnotationUtils.NAMED_CLASS))
            beanName = clazz.getAnnotation(AnnotationUtils.NAMED_CLASS).value();
        if(clazz.isAnnotationPresent(AnnotationUtils.JSR330.NAMED_CLASS)) {
            String name = clazz.getAnnotation(AnnotationUtils.JSR330.NAMED_CLASS).value();
            if(beanName == null)
                beanName = name;
            else if(!StringUtils.equals(beanName, name))
                throw new BeanCreateException(
                        "There are two or more @Named{"
                                + beanName
                                + ", "
                                + name
                                + "} defined, We cannot decide which one to use.");
        }
        if(StringUtils.isEmpty(beanName)) {
            beanName = clazz.getSimpleName();
            if(!StringUtils.isEmpty(beanName))
                beanName = beanName.substring(0,1).toLowerCase() + beanName.substring(1);
        }
        if(StringUtils.isEmpty(beanName))
            throw new NoBeanNameException();
        return beanName;
    }

    private BeanScope determineBeanScope(Class<T> clazz) throws BeanCreateException {
        BeanScope scope = null;
        if(clazz.isAnnotationPresent(AnnotationUtils.SCOPE_CLASS))
            scope = clazz.getAnnotation(AnnotationUtils.SCOPE_CLASS).value();
        if(clazz.isAnnotationPresent(AnnotationUtils.JSR330.SINGLETON_CLASS)) {
            if(scope == null || scope == BeanScope.SINGLETON)
                return BeanScope.SINGLETON;
            else
                throw new BeanCreateException(
                        "Find two or more scope annotation present in class [" + clazz.getName() + "], please devide one.");
        }
        return scope == null ? BeanScope.SINGLETON : scope;
    }
}
