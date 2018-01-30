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

package com.jerehao.devia.orm.query;

import com.jerehao.devia.orm.model.support.Model;

import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-26 14:34 jerehao
 */
public class Projection extends SQLClause {

    private final Set<Property> properties = new LinkedHashSet<>();

    public Projection(Query query) {
        super(query);
    }

    public Projection add(Property... properties) {
        if(properties != null)
            Collections.addAll(this.properties, properties);
        return this;
    }

    public Projection add(Projection projection) {
        this.properties.addAll(projection.properties);

        return this;
    }

//    public <T> Projection addAll(Class<T> clazz) {
//        Model model = null;
//        try {
//            model = ApplicationManager.getModelManager().getModel(clazz);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        properties.add(new Property("*", model));
//
//        return this;
//    }

    Set<Property> getProperties() {
        return properties;
    }

    @Override
    public Query next() {
        return (Query) getSqlSentence();
    }
}
