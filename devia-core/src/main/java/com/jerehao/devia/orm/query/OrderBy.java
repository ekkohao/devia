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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-26 14:34 jerehao
 */
public class OrderBy extends SQLClause {

    private final Set<Property> properties = new LinkedHashSet<>();

    public OrderBy(Query query) {
        super(query);
    }

    public OrderBy add(Property property) {
        this.properties.add(property);

        return this;
    }

    public OrderBy add(OrderBy orderBy) {
        this.properties.addAll(orderBy.properties);

        return this;
    }

    Set<Property> getProperties() {
        return properties;
    }

    @Override
    public Query next() {
        return (Query) getSqlSentence();
    }
}
