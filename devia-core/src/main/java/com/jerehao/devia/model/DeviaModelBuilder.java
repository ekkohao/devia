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

import com.jerehao.devia.core.util.ClassUtils;
import com.jerehao.devia.core.util.StringUtils;
import com.jerehao.devia.model.annotation.Column;
import com.jerehao.devia.model.annotation.Model;
import com.jerehao.devia.model.annotation.PrimaryKey;
import com.jerehao.devia.model.table.ColumnDefinition;
import com.jerehao.devia.model.table.CreateTableStatementGenerator;
import com.jerehao.devia.model.table.MysqlCreateTableStatementGenerator;
import com.jerehao.devia.model.table.TableDefinition;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-23 15:04 jerehao
 */
public class DeviaModelBuilder implements ModelBuilder {

    ModelFactory modelFactory;

    DeviaModelBuilder(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    public void createModel(Class<?> clazz) {
        ModelDefinition model = new ModelDefinition();
        TableDefinition table = new TableDefinition(determineModelName(clazz));

        model.setTable(table);

        Field[] fields = clazz.getFields();
        for(Field field : fields) {
            if(!Modifier.isFinal(field.getModifiers())
                    || !Modifier.isStatic(field.getModifiers())
                    || !Objects.equals(field.getType(), String.class)
                    || !field.isAnnotationPresent(Column.class))
                continue;

            model.addColumn(createColumn(field));
        }

        modelFactory.addModel((Class<?>) clazz, model);
    }

    private ColumnDefinition createColumn(Field field) {

        Column column = field.getAnnotation(Column.class);
        String columnName;
        try {
            columnName = (String) field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Read field [" + field.getName() + "] value error.");
        }

        ColumnDefinition columnDefinition = new ColumnDefinition(columnName, column.type());

        if(column.length() > 0)
            columnDefinition.setLength(column.length());

        if(!StringUtils.isEmptyOrNull(column.defaultValue()))
            columnDefinition.setDefaultValue(column.defaultValue());

        if(column.notnull())
            columnDefinition.setNotnull(true);

        if(column.unique())
            columnDefinition.setUnique(true);

        if(column.autoIncrement())
            columnDefinition.setAutoIncrement(true);

        if(field.isAnnotationPresent(PrimaryKey.class))
            columnDefinition.setPrimaryKey(true);

        return columnDefinition;
    }

    private String determineModelName(Class<?> clazz) {
        String name = null;
        if(clazz.isAnnotationPresent(Model.class)) {
            name = clazz.getAnnotation(Model.class).value();
        }
        if(StringUtils.isEmptyOrNull(name))
            name = clazz.getSimpleName().substring(0,1).toLowerCase() + clazz.getSimpleName().substring(1);

        return name;
    }
}
