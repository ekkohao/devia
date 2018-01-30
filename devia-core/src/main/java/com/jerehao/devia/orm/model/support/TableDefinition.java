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

package com.jerehao.devia.orm.model.support;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-23 10:25 jerehao
 */
public class TableDefinition {

    private String name;

    private String engine = "InnoDB";

    private String defaultCharSet = "utf8";

    private int autoIncrement = 1;


    public TableDefinition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getDefaultCharSet() {
        return defaultCharSet;
    }

    public void setDefaultCharSet(String defaultCharSet) {
        this.defaultCharSet = defaultCharSet;
    }

    public int getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(int autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
