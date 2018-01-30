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

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-24 18:39 jerehao
 */
public class Query implements SQLSentence {

    private Projection projection = new Projection(this);

    private Condition condition = new Condition(this) {
        @Override
        public Query next() {
            return (Query) getSqlSentence();
        }
    };

    private OrderBy orderBy = new OrderBy(this);

    public Query() {

    }

    public Projection projection() {
        return projection;
    }

    public Condition condition() {
        return condition;
    }

    public OrderBy orderBy() {
        return orderBy;
    }

}
