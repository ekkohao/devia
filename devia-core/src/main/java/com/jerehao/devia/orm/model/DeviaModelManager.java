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

package com.jerehao.devia.orm.model;


import com.jerehao.devia.orm.model.support.Model;

import java.sql.SQLException;
import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-23 15:38 jerehao
 */
public class DeviaModelManager implements ModelManager {

    //<name, table>
    private Map<Class<?>, Model> models = new HashMap<>();

    private ModelBuilder modelBuilder;

    private static DeviaModelManager instance;

    private DeviaModelManager() {
        modelBuilder = new DeviaModelBuilder(this);
    }

    public static DeviaModelManager getModelManager() {
        if(instance == null)
            instance = new DeviaModelManager();
        return instance;
    }

    @Override
    public void addModel(Class<?> clazz, Model model) {
        models.put(clazz, model);
    }

    @Override
    public Model getModel(Class<?> clazz) throws SQLException {
        if(models.containsKey(clazz))
            return models.get(clazz);

        throw new SQLException("Model [" + clazz.getName() + "] cannot be found.");
    }

    @Override
    public List<Model> getModels() {
        List<Model> list = new LinkedList<>();
        list.addAll(models.values());

        return list;
    }

    @Override
    public ModelBuilder getModelBuilder() {
        return modelBuilder;
    }
}
