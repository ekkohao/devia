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

import com.jerehao.devia.core.util.StringUtils;

import java.sql.SQLException;
import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-24 20:16 jerehao
 */
public class Model {

    private final Table table;

    private final Map<String, Column> columnMap  = new HashMap<>();

    private final List<Column> columns = new LinkedList<>();

    public Model(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void addColumn(Column column) {
        if(column.isPrimaryKey())
            columns.add(0, column);
        else
            columns.add(column);

        this.columnMap.put(column.getName(), column);
    }

    public Column getColumn(String columnName) throws SQLException {
        if(containsColumn(columnName))
            return columnMap.get(columnName);

        throw new SQLException("Cannot found column [" + columnName +"] in table [" + table.getName() + "]");
    }

    public boolean containsColumn(String columnName) {
        return columnMap.containsKey(columnName);
    }

    public Column removeColumn(String columnName) {
        if(StringUtils.isEmptyOrNull(columnName))
            throw new NullPointerException("Remove name cannot be null or empty.");

        Column column = null;

        if(columnMap.containsKey(columnName)) {
            column = columnMap.remove(columnName);
            columns.remove(column);
        }

        return column;
    }

    public boolean hasPrimaryKey() {
        List<Column> pkColumns = new LinkedList<>();

        for(Column column : columns)
            if(column.isPrimaryKey())
                return true;

        return false;
    }

    public List<Column> getPrimaryKeys() {
        List<Column> pkColumns = new LinkedList<>();

        for(Column column : columns) {
            if (column.isPrimaryKey())
                pkColumns.add(column);
            else
                break;
        }

        return pkColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        return Objects.equals(table, model.table) && columnMap.equals(model.columnMap) && columns.equals(model.columns);
    }

    @Override
    public int hashCode() {
        StringBuilder sb = new StringBuilder();

        columns.forEach(column -> sb.append(column.getName()));
        sb.append("#").append(table.getName());

        return sb.toString().hashCode();
    }
}
