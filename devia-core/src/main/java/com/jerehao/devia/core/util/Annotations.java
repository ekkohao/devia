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

import com.jerehao.devia.beans.annotation.jsr330.*;

import java.lang.annotation.*;
import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-12 14:12 jerehao
 */
public class Annotations {

    public static final Class<Inject> INJECT_ClASS = Inject.class;

    public static final Class<Named> NAMED_CLASS = Named.class;

    public static final Class<Qualifier> QUALIFIER_CLASS = Qualifier.class;

    public static final Class<Scope> SCOPE_CLASS = Scope.class;

    public static final Class<Singleton> SINGLETON_CLASS = Singleton.class;

    private static final Set<Class<? extends Annotation>> alreadyKnownQualifierAnnotation;

    static {
        alreadyKnownQualifierAnnotation = new HashSet<>();
        alreadyKnownQualifierAnnotation.add(NAMED_CLASS);
    }

    //元注解@Qualifier定义的注解
    public static boolean isQualifierAnnotation(Class<? extends Annotation> annotationType) {
        if(alreadyKnownQualifierAnnotation.contains(annotationType))
            return true;
        else if (getMetaAnnotations(annotationType).contains(QUALIFIER_CLASS)) {
            alreadyKnownQualifierAnnotation.add(annotationType);
            return true;
        }
        return false;

    }

    //获取普通类或注解类的元注解
    public static Set<Class<? extends Annotation>> getMetaAnnotations(Class<?> clazz) {
        final Set<Class<? extends Annotation>> alreadyParses = new HashSet<>();
        final List<Class<? extends Annotation>> needParses = new LinkedList<>();
        final Set<Class<? extends Annotation>> metaAnnotations = new HashSet<>();

        //exclude some meta annotation that we don't want to get
        alreadyParses.add(Target.class);
        alreadyParses.add(Documented.class);
        alreadyParses.add(Inherited.class);
        alreadyParses.add(Retention.class);

        //如果是注解
        //判断此注解定义时使用的注解
        if(clazz.isAnnotation()) {
            Annotation[] annotations = ((Class<? extends Annotation>) clazz).getAnnotations();
            for (Annotation annotation : annotations)
                needParses.add(annotation.annotationType());
        }
        else {
            //如果是普通类
            //获取普通类上的所有注解，获取所有注解的定义注解
            for(Annotation annotation : clazz.getAnnotations()) {
                for (Annotation annotation1 : annotation.annotationType().getAnnotations())
                    needParses.add(annotation1.annotationType());
            }
        }

        Class<? extends Annotation> annotationType;

        while (!needParses.isEmpty()) {
            Class<? extends Annotation> current = needParses.remove(0);
            if(!alreadyParses.contains(current) && isMetaAnnotation(current)) {
                alreadyParses.add(current);
                metaAnnotations.add(current);
                for(Annotation annotation : current.getDeclaredAnnotations()) {
                    if(!alreadyParses.contains(annotation.annotationType()))
                        needParses.add(annotation.annotationType());
                }
            }
        }

        return metaAnnotations;
    }

    //which @target value is only ElemType.ANNOTATION_TYPE is recognize a meta annotation
    private static boolean isMetaAnnotation(Class<? extends Annotation> clazz) {

        if(!clazz.isAnnotationPresent(Target.class))
            return false;

        Target targetAnnotation = clazz.getAnnotation(Target.class);
        ElementType[] elementTypes = targetAnnotation.value();
        return elementTypes.length == 1 && elementTypes[0] == ElementType.ANNOTATION_TYPE;
    }

    private Annotations(){}
}
