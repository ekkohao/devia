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

package com.jerehao.devia.bean.support.inject;

import com.jerehao.devia.core.util.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-12 10:02 jerehao
 */
public class ParameterInjectPoint {

    private Parameter parameter;

    private Type type;

    private Set<Qualifiee> qualifiees = new LinkedHashSet<>();

//    public ParameterInjectPoint(Type type, Annotation[] annotations) {
//        this.type = type;
//        initInjectPoint(annotations);
//    }

    public ParameterInjectPoint(Parameter parameter) {
        this.parameter = parameter;
        this.type = parameter.getParameterizedType();
        initInjectPoint(parameter.getDeclaredAnnotations());
    }

    private void initInjectPoint(Annotation[] annotations) {
        initQualifiers(annotations);
    }

    private void initQualifiers(Annotation[] annotations) {
        for(Annotation annotation : annotations) {
            if(AnnotationUtils.isQualifierAnnotation(annotation.annotationType()))
                qualifiees.add(new Qualifiee(annotation, this.parameter.getName()));
        }
    }


    public Type getType() {
        return this.type;
    }

    public Set<Qualifiee> getQualifiees() {
        return qualifiees;
    }

    public Parameter getParameter() {
        return parameter;
    }
}
