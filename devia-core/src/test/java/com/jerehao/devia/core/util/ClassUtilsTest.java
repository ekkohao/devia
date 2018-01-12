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

package com.jerehao.devia.core.util;

import com.jerehao.devia.beans.BeanFactory;
import com.jerehao.devia.beans.DeviaBeanFactory;
import com.jerehao.devia.testclass.BBB;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Types;
import java.util.Arrays;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-11 17:26 jerehao
 */
@Singleton
@Named
@BBB
public class ClassUtilsTest {

    Class<ClassUtilsTest> field;

    @Test
    public void testGetType() {

        Set<Type> types = ClassUtils.getAllTypes(Target.class);
        for (Type type : types)
            System.out.println(type.getTypeName());

    }

    @Test
    public void testGetMeta() {
        Set<Class<? extends Annotation>> classes;

        classes = AnnotationUtils.getMetaAnnotations(ClassUtilsTest.class);

        for (Class<? extends Annotation> clazz : classes)
            System.out.println(clazz.getName());
    }

    @Test
    public void testGetAllFields() {
        for(Method method : ReflectionUtils.getAllMethods(DeviaBeanFactory.class))
            System.out.println(method.toGenericString() + method.isAnnotationPresent(Inject.class));
    }
}