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

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-12 17:09 jerehao
 */
public final class ReflectionUtils {

    //get all field except final field
    public static Set<Field> getAllFields(final Class<?> clazz) {
        Set<Field> fields = new HashSet<>();
        Class<?> current = clazz;

        while (current != null && !current.equals(Object.class)) {
            System.out.println(current.getName());
            Field[] declaredFields = current.getDeclaredFields();
            for(Field f : declaredFields) {
                if(!Modifier.isFinal(f.getModifiers()) && !fieldCollectionContains(fields, f))
                    fields.add(f);
            }

            current = current.getSuperclass();
        }

        return fields;
    }

    //get class all methods except abstract method
    public static Set<Method> getAllMethods(final Class<?> clazz) {
        final Set<Method> methods = new HashSet<>();

        Class<?> current = clazz;

        while (current != null && !current.equals(Object.class)) {
            for(Method method : current.getDeclaredMethods()) {
                if(!Modifier.isAbstract(method.getModifiers()) && !methodCollectionContains(methods, method))
                    methods.add(method);
            }

            current = current.getSuperclass();
        }

        return methods;
    }

    public static boolean fieldEquals(Field field, Field field2) {
        return StringUtils.equals(field2.getName(), field.getName())
                && StringUtils.equals(field2.getGenericType().getTypeName(), field.getGenericType().getTypeName());

    }

    public static boolean methodEquals(Method method, Method method2) {

        if(!StringUtils.equals(method.getName(), method2.getName()))
            return false;

        if(!StringUtils.equals(method.getReturnType().getTypeName(), method2.getReturnType().getTypeName()))
            return false;

        if(method.getModifiers() != method2.getModifiers())
            return false;

        Class<?>[] params = method.getParameterTypes();
        Class<?>[] params2 = method2.getParameterTypes();

        if(params.length != params2.length)
            return false;

        for(int i = 0; i < params.length; ++i)
            if(!StringUtils.equals(params[i].getTypeName(), params2[i].getTypeName()))
                return false;

        return true;
    }


    private static boolean fieldCollectionContains(Collection<Field> collection, Field field) {
        for(Field f : collection) {
            if(fieldEquals(f, field))
                return true;
        }

        return false;
    }

    private static boolean methodCollectionContains(Collection<Method> collection, Method method) {
        for(Method m : collection)
            if(methodEquals(m, method))
                return true;

        return false;
    }

    private ReflectionUtils() {}
}
