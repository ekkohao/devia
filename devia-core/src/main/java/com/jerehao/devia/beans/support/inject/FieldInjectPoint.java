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

import com.jerehao.devia.core.util.Annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-12 10:02 jerehao
 */
public class FieldInjectPoint {

    private Type type;

    private Field field;

    private Set<Qualifiee> qualifiees = new LinkedHashSet<>();

    public FieldInjectPoint(Field field) {
        this.field = field;
        this.type = field.getGenericType();
        initInjectPoint();
    }

    private void initInjectPoint() {
        initQualifiers();
    }

    private void initQualifiers() {
        for(Annotation annotation : field.getAnnotations()) {
            if(Annotations.isQualifierAnnotation(annotation.annotationType()))
                qualifiees.add(new Qualifiee(annotation, this.field.getName()));
        }
    }

    public Field getField() {
        return field;
    }

    public Type getType() {
        return this.type;
    }

    public Set<Qualifiee> getQualifiees() {
        return qualifiees;
    }
}
