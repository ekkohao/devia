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

package com.jerehao.devia.bean;

import com.jerehao.devia.beans.BeanFactory;
import com.jerehao.devia.beans.DeviaBeanFactory;
import com.jerehao.devia.beans.build.BeanBuilder;
import com.jerehao.devia.beans.exception.BeanCreateException;
import com.jerehao.devia.beans.exception.BeanException;
import com.jerehao.devia.beans.exception.MultipleBeanException;
import com.jerehao.devia.beans.exception.NoSuchBeanException;
import com.jerehao.devia.beans.support.Bean;
import com.jerehao.devia.beans.support.inject.FieldInjectPoint;
import com.jerehao.devia.beans.support.inject.MethodInjectPoint;
import com.jerehao.devia.testclass.Child;
import com.jerehao.devia.testclass.Mother;
import com.jerehao.devia.testclass.Mother2;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-12 19:00 jerehao
 */
public class BeanTest {
    @Test
    public void testBean() {
        BeanFactory beanFactory = DeviaBeanFactory.getBeanFactory();
        BeanBuilder beanBuilder = beanFactory.getBeanBuilder();

        beanBuilder.createBean(Mother.class);
        beanBuilder.createBean(Mother2.class);
        beanBuilder.createBean(Child.class);

        try {
            Mother mother = beanFactory.get("mother2");
            Child child = beanFactory.get(Child.class);
            System.out.println("name : " + child.getMother().getName());
        } catch (BeanException e) {
            System.out.println(e.getMessage());
        }
    }
}
