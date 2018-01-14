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
import com.jerehao.devia.beans.support.inject.ConstructorInjectPoint;
import com.jerehao.devia.beans.support.inject.FieldInjectPoint;
import com.jerehao.devia.beans.support.inject.MethodInjectPoint;
import com.jerehao.devia.beans.support.inject.Qualifiee;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-12 11:04 jerehao
 */
public interface Bean<T> {

    T getBeanInstance();

    String getBeanName();

    Class<T> getBeanClass();

    Class<T> getProxyClass();

    BeanScope getScope();

    Set<Type> getTypes();

    Collection<Qualifiee> getQualifiees();

    boolean containsQualifiee(Class<? extends Annotation> clazz);

    Qualifiee getQualifiee(Class<? extends Annotation> clazz);

    Set<FieldInjectPoint> getFieldInjectPoints();

    Set<MethodInjectPoint> getMethodInjectPoints();

    ConstructorInjectPoint<T> getConstructorInjectPoint();

    boolean hasConstructorInjectPoint();

    boolean satisfiedQualifiees(Set<Qualifiee> qualifiees);

    BeanBuilder getBeanBuilder();
}
