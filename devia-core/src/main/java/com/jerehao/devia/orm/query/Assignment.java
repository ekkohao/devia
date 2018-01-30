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

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-26 14:34 jerehao
 */
public class Assignment extends SQLClause {

    private final Set<Assign> assigns = new LinkedHashSet<>();

    public Assignment(Update update) {
        super(update);
    }

    Assignment set(Property property, Object object) {
        assigns.add(new Assign(property, object));

        return this;
    }

    Set<Assign> getAssigns() {
        return assigns;
    }

    @Override
    public Update next() {
        return (Update) getSqlSentence();
    }


    public static class Assign {
        Property left;

        Object right;

        public Assign(Property left, Object right) {
            this.left = left;
            this.right = right;
        }

        public Property getLeft() {
            return left;
        }

        public Object getRight() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Assign assign = (Assign) o;

            return (left != null ? left.equals(assign.left) : assign.left == null)
                    && (right != null ? right.equals(assign.right) : assign.right == null);
        }

        @Override
        public int hashCode() {
            int result = left != null ? left.hashCode() : 0;
            result = 31 * result + (right != null ? right.hashCode() : 0);
            return result;
        }
    }
}
