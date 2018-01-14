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

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-14 17:45 jerehao
 */
public class Qualifiee {

    private Annotation annotation;

    //name为字段名或参数名，或类名首字母小写
    private String name;

    public Qualifiee(Annotation annotation, String name) {
        this.annotation = annotation;
        this.name = StringUtils.isEmpty(name) ? "" : name;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return annotation.hashCode() + name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Qualifiee))
            return false;
        return StringUtils.equals(name, ((Qualifiee) obj).getName())
                && Objects.equals(annotation, ((Qualifiee) obj).getAnnotation());
    }

    @Override
    public String toString() {
        return "Annotation name [" + name + "] " +annotation.toString();
    }
}
