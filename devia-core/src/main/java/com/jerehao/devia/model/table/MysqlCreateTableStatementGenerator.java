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

package com.jerehao.devia.model.table;

import com.jerehao.devia.core.util.StringUtils;
import com.jerehao.devia.model.ModelDefinition;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-23 10:37 jerehao
 */
public class MysqlCreateTableStatementGenerator implements CreateTableStatementGenerator {

    public MysqlCreateTableStatementGenerator() {
    }

    @Override
    public String getSQLString(ModelDefinition model) {
        TableDefinition table = model.getTable();
        List<ColumnDefinition> columns = model.getColumns();

        StringBuilder s;
        s = new StringBuilder(getTablePrefix(table));

        if(columns.isEmpty()) {
            s.append(" ");
        }
        else {
            s.append(" (");
            String pk = "";
            List<String> uniques = new LinkedList<>();
            for(ColumnDefinition column : columns) {
                s.append(getColumnString(column)).append(",");

                if(column.isPrimaryKey()) {
                    String pkName = "`" + column.getName() + "`";
                    pk = StringUtils.isAnyEmptyOrNull(pk) ? pkName : pk + "," + pkName;
                }
                else if(column.isUnique()) {
                    uniques.add("`" + column.getName() + "`");
                }
            }
            if(!uniques.isEmpty()) {
                for(String unique : uniques)
                    s.append("UNIQUE (").append(unique).append("),");
            }
            if(StringUtils.isEmptyOrNull(pk))
                s.deleteCharAt(s.length());
            else {
                s.append("PRIMARY KEY (").append(pk).append(")");
            }

            s.append(") ");
        }

        s.append(getTableSuffix(table));
        return s.toString();
    }

    private String getTablePrefix(TableDefinition table) {
        return "CREATE TABLE `" + table.getName() + "`";
    }

    private String getTableSuffix(TableDefinition table) {
        String s = "ENGINE=" + table.getEngine() + " DEFAULT CHARSET=" + table.getDefaultCharSet();

        if(table.getAutoIncrement() > 1)
            s = s + " AUTO_INCREMENT=" + table.getAutoIncrement();
        s += ";";
        return s;
    }

    private String getColumnString(ColumnDefinition column) {
        String s;

        s = StringUtils.build("`{0}` {1}", column.getName(), column.getType().toSQLString(column.getLength()));

        if(column.isNotnull())
            s += " NOT NULL";

        if(column.isAutoIncrement())
            s += " AUTO_INCREMENT";

        if(!StringUtils.isEmptyOrNull(column.getDefaultValue())) {
            s += " DEFAULT ";
            if(column.getType().isNumeric())
                s += column.getDefaultValue();
            else
                s = s + "'" + column.getDefaultValue() + "'";
        }

        return s;
    }
}
