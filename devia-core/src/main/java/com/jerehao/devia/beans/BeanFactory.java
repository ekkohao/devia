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
import com.jerehao.devia.beans.exception.NoSuchBeanException;
import com.jerehao.devia.beans.support.Bean;
import com.jerehao.devia.beans.support.DeviaBean;
import com.jerehao.devia.beans.build.BeanBuilder;
import com.jerehao.devia.beans.support.inject.Qualifiee;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-09 16:11 jerehao
 */
public interface BeanFactory {


    <T> void addBean(Bean<T> bean) throws BeanCreateException;

    <T> Bean<T> getBean(String beanName) throws NoSuchBeanException;

    <T> Bean<T> getBean(Type type) throws MultipleBeanException, NoSuchBeanException;

    <T> Bean<T> getBean(Type type, Set<Qualifiee> qualifiees) throws MultipleBeanException, NoSuchBeanException;

    <T> T get(String beanName);

    <T> T get(Type type);

    <T> T get(Type type, Set<Qualifiee> qualifiees);

    BeanBuilder getBeanBuilder();

}
