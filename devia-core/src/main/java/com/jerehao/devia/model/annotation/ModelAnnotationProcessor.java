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

package com.jerehao.devia.model.annotation;

import com.jerehao.devia.config.AnnotationProcessor;
import com.jerehao.devia.model.ModelBuilder;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-22 10:24 jerehao
 */
public class ModelAnnotationProcessor implements AnnotationProcessor {

    private final List<Class<? extends Annotation>> targetAnnotations;

    private ModelBuilder modelBuilder;

    public ModelAnnotationProcessor(ModelBuilder modelBuilder) {
        this.modelBuilder = modelBuilder;

        targetAnnotations = new LinkedList<>();
        targetAnnotations.add(Model.class);
    }

    @Override
    public List<Class<? extends Annotation>> getTargetAnnotations() {
        return targetAnnotations;
    }

    @Override
    public void process(Class<?> clazz) {
        modelBuilder.createModel(clazz);
    }
}
