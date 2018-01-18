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
import com.jerehao.devia.bean.exception.BeanCreateException;
import com.jerehao.devia.bean.exception.MultipleBeanException;
import com.jerehao.devia.bean.exception.NoBeanNameException;
import com.jerehao.devia.bean.exception.NoSuchBeanException;
import com.jerehao.devia.bean.support.inject.Qualifiee;
import com.jerehao.devia.core.util.AnnotationUtils;
import com.jerehao.devia.core.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-18 10:53 jerehao
 */
public class MethodCryBean<T> extends AbstractBean<T> {

    private Method cryMethod;

    private Class<?> declareClass;

    public MethodCryBean(Method cryMethod, Class declareClass ,BeanFactory beanFactory) throws BeanCreateException {
        this.cryMethod = cryMethod;
        this.declareClass = declareClass;

        setBeanClass((Class<T>) cryMethod.getReturnType());
        setBeanFactory(beanFactory);

        initBean();
    }

    @Override
    public T create() throws BeanCreateException, NoSuchBeanException, MultipleBeanException {
        T instance;
        Object configObj = getBeanFactory().get(declareClass);

        try {
            instance = (T) cryMethod.invoke(configObj, (Object[]) null);
            return  instance;
        } catch (Exception e) {
            throw new BeanCreateException(e.getMessage());
        }
    }


    private void initBean() throws BeanCreateException {
        setBeanName(determineBeanName());
        setScope(determineBeanScope());
        addTypes(ClassUtils.getAllTypes(getBeanClass()));

        initProxyClass();
        initQualifiees();
    }

    private void initProxyClass() {
        setProxyClass(getBeanClass());
    }

    private void initQualifiees() {
        for(Annotation annotation : cryMethod.getAnnotations()) {
            if(ClassUtils.equalsAny(annotation.annotationType(), AnnotationUtils.NAMED_CLASS, AnnotationUtils.JSR330.NAMED_CLASS))
                continue;
            if (AnnotationUtils.isQualifierAnnotation(annotation.annotationType()))
                addQualifiee(annotation.annotationType(), new Qualifiee(annotation, getBeanName()));

        }
    }


    @SuppressWarnings("all")
    private String determineBeanName() throws BeanCreateException {
        String beanName = null;
        if(cryMethod.isAnnotationPresent(AnnotationUtils.NAMED_CLASS))
            beanName = cryMethod.getAnnotation(AnnotationUtils.NAMED_CLASS).value();
        if(cryMethod.isAnnotationPresent(AnnotationUtils.JSR330.NAMED_CLASS)) {
            String name = cryMethod.getAnnotation(AnnotationUtils.JSR330.NAMED_CLASS).value();
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

        if(StringUtils.isEmpty(beanName))
            beanName = cryMethod.getName();

        if(StringUtils.isEmpty(beanName))
            throw new NoBeanNameException();

        return beanName;
    }

    private BeanScope determineBeanScope() throws BeanCreateException {
        BeanScope scope = null;
        if(cryMethod.isAnnotationPresent(AnnotationUtils.SCOPE_CLASS))
            scope = cryMethod.getAnnotation(AnnotationUtils.SCOPE_CLASS).value();
        if(cryMethod.isAnnotationPresent(AnnotationUtils.JSR330.SINGLETON_CLASS)) {
            if(scope == null || scope == BeanScope.SINGLETON)
                return BeanScope.SINGLETON;
            else
                throw new BeanCreateException(
                        "Find two or more scope annotation present in method [" + cryMethod.getName() + "], please decide one.");
        }
        return scope == null ? BeanScope.SINGLETON : scope;
    }

}
