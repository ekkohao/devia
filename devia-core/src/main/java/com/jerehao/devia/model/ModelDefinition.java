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

import com.jerehao.devia.model.table.ColumnDefinition;
import com.jerehao.devia.model.table.TableDefinition;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-23 15:46 jerehao
 */
public class ModelDefinition {

    private TableDefinition table;

    //<name, column>
    private Map<String, ColumnDefinition> columns = new HashMap<>();

    private ColumnDefinition autoIncrementPKColumn = null;

    public ModelDefinition() {
    }

    public TableDefinition getTable() {
        return table;
    }

    public void setTable(TableDefinition table) {
        this.table = table;
    }

    public List<ColumnDefinition> getColumns() {
        List<ColumnDefinition> list = new LinkedList<>();

        list.addAll(columns.values());

        return list;
    }

    public void addColumn(ColumnDefinition column) {

        if(column.getType().isInteger() && column.isPrimaryKey())
            autoIncrementPKColumn = column;
        this.columns.put(column.getName(), column);
    }

    public ColumnDefinition getColumn(String columnName) {
        return columns.getOrDefault(columnName, null);
    }

    public ColumnDefinition getAutoIncrementPK() {
        return autoIncrementPKColumn;
    }

    @Override
    public int hashCode() {
        return table.getName().hashCode();
    }
}
