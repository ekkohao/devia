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

package com.jerehao.devia.model;

import org.json.JSONObject;

import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-23 15:38 jerehao
 */
public class DeviaModelFactory implements ModelFactory {

    private ModelBuilder modelBuilder;

    private static DeviaModelFactory instance;

    private DeviaModelFactory() {
        modelBuilder = new DeviaModelBuilder(this);
    }

    public static DeviaModelFactory getModelFactory() {
        if(instance == null)
            instance = new DeviaModelFactory();
        return instance;
    }

    //<name, table>
    private Map<Class<?>, ModelDefinition> models = new HashMap<>();


    public void addModel(Class<?> clazz, ModelDefinition model) {
        models.put(clazz, model);
    }

    public ModelDefinition getModel(Class<?> clazz) {
        return models.getOrDefault(clazz, null);
    }

    public List<ModelDefinition> getModels() {
        List<ModelDefinition> list = new LinkedList<>();
        list.addAll(models.values());
        return list;
    }

    public ModelBuilder getModelBuilder() {
        return modelBuilder;
    }
}
