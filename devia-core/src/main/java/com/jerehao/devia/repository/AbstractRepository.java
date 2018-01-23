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

package com.jerehao.devia.repository;

import com.jerehao.devia.application.ApplicationManager;
import com.jerehao.devia.core.util.StringUtils;
import com.jerehao.devia.model.ModelDefinition;
import com.jerehao.devia.model.table.ColumnDefinition;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-19 14:16 jerehao
 */
public abstract class AbstractRepository extends PlatformRepository implements Repository {

    private final ModelDefinition model;

    private final String tableName;

    protected AbstractRepository(Class<? extends JSONObject> clazz) {
        super(TransactionManager.getTransactionManage());

        model = ApplicationManager.getModelFactory().getModel(clazz);
        tableName = model.getTable().getName();
    }

    @Override
    public void add(final JSONObject jsonObject) {
        //INSERT into values
        StringBuilder keysBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();
        String[] values = new String[jsonObject.length()];
        StringBuilder sqlBuilder = new StringBuilder();
        int index = 0;

        sqlBuilder.append("INSERT INTO ").append('`').append(tableName).append('`');

        for (Iterator<String> itr = jsonObject.keys(); itr.hasNext(); ++index) {
            String key = itr.next();
            String value = jsonObject.getString(key);

            keysBuilder.append(",`").append(key).append("`");
            valuesBuilder.append(",?");

            ColumnDefinition column = model.getColumn(key);

            if(column == null)
                throw new RuntimeException("Cannot find database column [" + key + "] for table [" + tableName + "].");

            if(column.getType().isNumeric())
                values[index] = value;
            else
                values[index] = "'" + value + "'";

        }

        keysBuilder.deleteCharAt(0);
        valuesBuilder.deleteCharAt(0);
        sqlBuilder.append("(").append(keysBuilder.toString()).append(")");
        sqlBuilder.append(" VALUES(").append(valuesBuilder.toString()).append(");");

        int id = executeInsert(sqlBuilder.toString(), (Object[]) values);

        ColumnDefinition pk = model.getAutoIncrementPK();
        if(pk != null)
            jsonObject.put(pk.getName(), id);
    }

    //TODO 查询器
    @Override
    public JSONObject find(Number id) {
        return null;
    }

    @Override
    public Collection<JSONObject> findAll() {
        return null;
    }

    @Override
    public void update(Number id, JSONObject jsonObject) {
        if(model.getAutoIncrementPK() == null)
            return;
        ColumnDefinition pk = model.getAutoIncrementPK();

        StringBuilder attrBuilder = new StringBuilder();
        String[] values = new String[jsonObject.length() + 1];
        StringBuilder sqlBuilder = new StringBuilder();
        int index = 0;


        sqlBuilder.append("UPDATE ").append("`").append(tableName).append("` SET ");
        for (Iterator<String> iterator = jsonObject.keys(); iterator.hasNext(); ++index) {
            String key = iterator.next();
            ColumnDefinition column = model.getColumn(key);

            attrBuilder.append(",`").append(key).append("`=?");

            if(column.getType().isNumeric())
                values[index] = jsonObject.getString(key);
            else
                values[index] = "'" + jsonObject.getString(key) + "'";
        }

        attrBuilder.deleteCharAt(0);

        values[index] = String.valueOf(id);
        sqlBuilder.append(attrBuilder.toString());
        sqlBuilder.append(" WHERE `").append(pk.getName()).append("`=?;");

        executeUpdate(sqlBuilder.toString(), (Object[]) values);
    }

    @Override
    public void delete(Number id) {
        if(model.getAutoIncrementPK() == null)
            return;
        ColumnDefinition pk = model.getAutoIncrementPK();

        String sql = StringUtils.build("DELETE FROM `{0}` WHERE `{1}`=?;", tableName, pk.getName());
    }

    protected String getTableName() {
        return tableName;
    }

}
