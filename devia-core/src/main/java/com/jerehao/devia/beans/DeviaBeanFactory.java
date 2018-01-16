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

package com.jerehao.devia.beans;

import com.jerehao.devia.beans.build.DeviaBeanBuilder;
import com.jerehao.devia.beans.context.Context;
import com.jerehao.devia.beans.context.PrototypeContext;
import com.jerehao.devia.beans.context.SingletonContext;
import com.jerehao.devia.beans.exception.BeanCreateException;
import com.jerehao.devia.beans.exception.BeanException;
import com.jerehao.devia.beans.exception.MultipleBeanException;
import com.jerehao.devia.beans.exception.NoSuchBeanException;
import com.jerehao.devia.beans.support.Bean;
import com.jerehao.devia.beans.support.BeanScope;
import com.jerehao.devia.beans.support.inject.Qualifiee;
import com.jerehao.devia.logging.Logger;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-09 16:28 jerehao
 */

public class DeviaBeanFactory extends AbstractBeanFactory {

    private static final Logger LOGGER = Logger.getLogger(DeviaBeanFactory.class);

    private static final BeanFactory instance;

    private Context singletonContext;

    private Context prototypeContext;

    static {
        instance = new DeviaBeanFactory();
    }

    public static BeanFactory getBeanFactory() {
        return instance;
    }

    private DeviaBeanFactory() {
        super.setBeanBuilder(new DeviaBeanBuilder(this));
        singletonContext = new SingletonContext();
        prototypeContext = new PrototypeContext();
    }

    @Override
    public <T> T get(String beanName) {
        try {
            return get(getBean(beanName));
        } catch (BeanException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public <T> T get(Type type)  {
        try {
            return get(getBean(type));
        } catch (BeanException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public <T> T get(Type type, Set<Qualifiee> qualifiees) {
        try {
            return get(getBean(type, qualifiees));
        } catch (BeanException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private <T> T get(Bean<T> bean) throws BeanCreateException, NoSuchBeanException, MultipleBeanException {
        Context context = getContext(bean);
            return context.get(bean);
    }

    private Context getContext(Bean<?> bean) {
        if(bean.getScope() == BeanScope.SINGLETON)
            return this.singletonContext;
        else if(bean.getScope() == BeanScope.PROTOTYPE)
            return this.prototypeContext;
        throw new RuntimeException("Not support scope [" + bean.getScope().name() + "] yet.");
    }
}
