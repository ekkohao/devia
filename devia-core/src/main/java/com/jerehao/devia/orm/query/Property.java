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

import com.jerehao.devia.orm.model.support.Column;
import com.jerehao.devia.orm.model.support.Model;

import java.sql.SQLException;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-29 14:34 jerehao
 */
public class Property {

    private final String name;

    private final Model model;

    private final boolean changeable;

    public Property(String name) {
        this.name = name;
        this.model = null;
        changeable = true;
    }

    Property(String name, Model model) {
        this.name = name;
        this.model = model;
        changeable = true;
    }

    public static Property forName(String name) {
        return new Property(name);
    }

    public String getName() {
        return name;
    }

    public Model getModel() {
        return model;
    }

    public Column getColumn() throws SQLException {
        return model.getColumn(name);
    }

    @Override
    public int hashCode() {
        String s = (this.model == null) ? name : model.getTable().getName() + "#" + name;
        return s.hashCode();
    }
}
