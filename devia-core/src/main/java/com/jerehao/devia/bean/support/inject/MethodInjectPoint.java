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

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-12 10:10 jerehao
 */
public class MethodInjectPoint {

    private ParameterInjectPoint[] parameterInjectPoints;

    private final Method method;

    public MethodInjectPoint(Method method) {
        this.method = method;
        initParamFieldInjectPoints();
    }

    private void initParamFieldInjectPoints() {
        Parameter[] parameters = method.getParameters();
        //Type[] paramTypes =  method.getGenericParameterTypes();
        //Annotation[][] annotations = method.getParameterAnnotations();
        int len = parameters.length;
        parameterInjectPoints = new ParameterInjectPoint[len];
        for(int i = 0 ; i < len; ++ i) {
            parameterInjectPoints[i] = new ParameterInjectPoint(parameters[i]);
        }
    }

    public Method getMethod() {
        return method;
    }

    public ParameterInjectPoint[] getParameterInjectPoints() {
        return parameterInjectPoints;
    }
}
