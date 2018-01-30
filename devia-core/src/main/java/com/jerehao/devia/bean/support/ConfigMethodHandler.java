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

import com.jerehao.devia.bean.annotation.Scope;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-18 13:18 jerehao
 */
public class ConfigMethodHandler implements MethodHandler {

    //<MethodName, MethodReturnObject>
    private Map<String, Object> returnCached = new HashMap<>();

    private MethodFilter methodFilter = new MethodFilter() {
        @Override
        public boolean isHandled(Method m) {
            return m.isAnnotationPresent(com.jerehao.devia.application.annotation.Bean.class);
        }
    };

    private static final ConfigMethodHandler INSTANCE = new ConfigMethodHandler();

    private ConfigMethodHandler() {}

    public static ConfigMethodHandler getMethodHandler() {
        return INSTANCE;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        String methodName = thisMethod.getDeclaringClass().getName() + "#" + thisMethod.getName();

        if(thisMethod.isAnnotationPresent(com.jerehao.devia.application.annotation.Bean.class)
                && !isScopePrototype(thisMethod)) {
            if(returnCached.containsKey(methodName))
                return returnCached.get(methodName);
            Object ret = proceed.invoke(self, args);
                if(returnCached.putIfAbsent(methodName, ret) != null)
                    return returnCached.get(methodName);
                return ret;
        }

        return proceed.invoke(self, args);
    }

    public MethodFilter getMethodFilter() {
        return methodFilter;
    }

    private boolean isScopePrototype(Method method) {
        return (method.isAnnotationPresent(Scope.class)
                && method.getAnnotation(Scope.class).value() == BeanScope.PROTOTYPE);


    }
}
