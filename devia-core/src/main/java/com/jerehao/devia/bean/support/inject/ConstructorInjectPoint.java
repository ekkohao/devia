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

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-12 10:10 jerehao
 */
public class ConstructorInjectPoint<T> {

    private ParameterInjectPoint[] parameterInjectPoints;

    private final Constructor<T> constructor;

    public ConstructorInjectPoint(Constructor<T> constructor) {
        this.constructor = constructor;
        initParamFieldInjectPoints();
    }

    private void initParamFieldInjectPoints() {
        //Type[] paramTypes =  constructor.getGenericParameterTypes();
        //Annotation[][] annotations = constructor.getParameterAnnotations();
        Parameter[] parameters = constructor.getParameters();
        int len = parameters.length;
        parameterInjectPoints = new ParameterInjectPoint[len];
        for(int i = 0 ; i < len; ++ i) {
            parameterInjectPoints[i] = new ParameterInjectPoint(parameters[i]);
        }
    }

    public Constructor<T> getConstructor() {
        return constructor;
    }

    public ParameterInjectPoint[] getParameterInjectPoints() {
        return parameterInjectPoints;
    }
}
