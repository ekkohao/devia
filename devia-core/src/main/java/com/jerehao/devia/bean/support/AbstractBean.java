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

package com.jerehao.devia.bean.support;


import com.jerehao.devia.bean.annotation.Named;
import com.jerehao.devia.bean.support.inject.*;
import com.jerehao.devia.core.common.annotation.Nullable;
import com.jerehao.devia.core.util.AnnotationUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-09 16:37 jerehao
 */
public abstract class AbstractBean<T> extends BeanDefinition<T> {

    @Override
    public boolean satisfiedQualifiees(@Nullable Set<Qualifiee> qualifiees) {
        if(qualifiees == null)
            return true;

        for(Qualifiee qualifiee : qualifiees) {
            if(Objects.equals(AnnotationUtils.NAMED_CLASS, qualifiee.getAnnotation().annotationType())) {
                Named checkingNamed = (Named) qualifiee.getAnnotation();
                String checkingName = StringUtils.isEmpty(checkingNamed.value()) ? qualifiee.getName() : checkingNamed.value();
                if(!StringUtils.equals(checkingName, getBeanName()))
                    return false;
            }
            else if (Objects.equals(AnnotationUtils.JSR330.NAMED_CLASS, qualifiee.getAnnotation().annotationType())) {
                javax.inject.Named checkingNamed = (javax.inject.Named) qualifiee.getAnnotation();
                String checkingName = StringUtils.isEmpty(checkingNamed.value()) ? qualifiee.getName() : checkingNamed.value();
                if(!StringUtils.equals(checkingName, getBeanName()))
                    return false;
            }
            else if(!containsQualifiee(qualifiee.getAnnotation().annotationType())){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.getBeanName().hashCode();
    }




}
