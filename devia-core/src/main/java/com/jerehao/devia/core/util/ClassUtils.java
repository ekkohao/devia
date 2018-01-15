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

import com.jerehao.devia.logging.Logger;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-03 11:21 jerehao
 */
public class ClassUtils {

    private static final Logger LOGGER = Logger.getLogger(ClassUtils.class);

    private ClassUtils(){}

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;

        classLoader = Thread.currentThread().getContextClassLoader();

        if(classLoader == null) {
            classLoader = ClassUtils.class.getClassLoader();

            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }

        return classLoader;
    }

    public static boolean isAbstract(final Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    public static boolean isInterface(final Class<?> clazz) {
        return Modifier.isInterface(clazz.getModifiers());
    }

    public static boolean isConcrete(final Class<?> clazz) {
        return !isAbstract(clazz) && !isInterface(clazz) && !clazz.isAnnotation();
    }

    public static Set<Type> getAllTypes(final Class<?> clazz) {
        Set<Type> types = new LinkedHashSet<>();
        Type current = clazz;
        Class<?> currentClass;

        while (current != null && !current.equals(Object.class)) {
            Type[] interfaces = null;
            types.add(current);

            if(current instanceof Class<?>)
                currentClass = (Class<?>) current;
            else if(current instanceof ParameterizedType)
                currentClass = (Class<?>) ((ParameterizedType) current).getRawType();
            else {
                LOGGER.warn("Cannot resolver type [" + current.getTypeName() + "]");
                break;
            }

            interfaces = currentClass.getGenericInterfaces();
            current = currentClass.getGenericSuperclass();

            if(interfaces == null || interfaces.length < 1)
                continue;
            for(Type type : interfaces) {
                types.add(type);

                //find interface all extends
                List<Type> supers = new LinkedList<>();
                supers.add(type);
                while (!supers.isEmpty()) {
                    Type currentInterface = supers.remove(0);

                    Type[] superInterfaces;
                    if(currentInterface instanceof Class<?>) {
                        superInterfaces = ((Class<?>) currentInterface).getGenericInterfaces();
                    }
                    else if(currentInterface instanceof ParameterizedType) {
                        superInterfaces = ((Class<?>) ((ParameterizedType) currentInterface).getRawType()).getGenericInterfaces();
                    }
                    else {
                        LOGGER.warn("Cannot resolver type [" + currentInterface.getTypeName() + "]");
                        continue;
                    }
                    Collections.addAll(types, superInterfaces);
                    Collections.addAll(supers,superInterfaces);
                }

            }

        }

        return types;
    }

    public static boolean equalsAny(Class<?> clazz, Class<?>... classes) {
        if(classes == null || classes.length < 1)
            return false;
        for(Class<?> clz : classes) {
            if(Objects.equals(clz, clazz))
                return true;
        }

        return false;
    }

}
