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

package com.jerehao.devia.beans.support.inject;

import com.jerehao.devia.core.util.AnnotationUtils;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-12 10:02 jerehao
 */
public class ParameterInjectPoint {

    private Type type;

    private Set<Annotation> qualifiers = new HashSet<>();

    public ParameterInjectPoint(Type type, Annotation[] annotations) {
        this.type = type;
        initInjectPoint(annotations);
    }

    private void initInjectPoint(Annotation[] annotations) {
        initQualifiers(annotations);
    }

    private void initQualifiers(Annotation[] annotations) {
        for(Annotation annotation : annotations) {
            if(AnnotationUtils.getMetaAnnotations(annotation.annotationType()).contains(Qualifier.class))
                qualifiers.add(annotation);
        }
    }


    public Type getType() {
        return this.type;
    }

    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }
}
