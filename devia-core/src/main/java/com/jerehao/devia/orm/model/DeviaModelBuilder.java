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

package com.jerehao.devia.orm.model;

import com.jerehao.devia.core.util.ReflectionUtils;
import com.jerehao.devia.core.util.StringUtils;
import com.jerehao.devia.orm.jdbc.NameGenerator;
import com.jerehao.devia.orm.model.annotation.PrimaryKey;
import com.jerehao.devia.orm.model.support.Column;
import com.jerehao.devia.orm.model.support.Model;
import com.jerehao.devia.orm.model.support.Table;
import com.jerehao.devia.orm.query.Property;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-23 15:04 jerehao
 */
public class DeviaModelBuilder implements ModelBuilder {

    private ModelManager modelManager;
    private Class<?> clazz;

    DeviaModelBuilder(ModelManager modelManager) {
        this.modelManager = modelManager;
    }

    public void createModel(Class<?> clazz) {

        Table table = new Table(NameGenerator.getTableName(clazz));
        Model model = new Model(table);

        Field[] fields = clazz.getFields();

        for(Field field : fields) {
            int modifiers = field.getModifiers();
            int check = Modifier.STATIC | Modifier.FINAL;

            if((check & modifiers) == check
                    && Property.class.isAssignableFrom(field.getType())
                    && field.isAnnotationPresent(com.jerehao.devia.orm.model.annotation.Column.class)) {
                try {
                    Property property = (Property) field.get(null);
                    Field propertyField = property.getClass().getDeclaredField("model");
                    propertyField.setAccessible(true);
                    propertyField.set(property, model);
                    propertyField.setAccessible(false);
                    propertyField = property.getClass().getDeclaredField("changeable");
                    propertyField.setAccessible(true);
                    propertyField.set(property,false);
                    propertyField.setAccessible(false);

                    model.addColumn(createColumn(field, property));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }

        modelManager.addModel(clazz, model);
    }

    private Column createColumn(Field field, Property property) {

        com.jerehao.devia.orm.model.annotation.Column annotation = field.getAnnotation(com.jerehao.devia.orm.model.annotation.Column.class);
        String columnName = NameGenerator.getColumnName(field);

        Column column = new Column(columnName, annotation.type(), property);

        if(annotation.length() > 0)
            column.setLength(annotation.length());

        if(!StringUtils.isEmptyOrNull(annotation.defaultValue()))
            column.setDefaultValue(annotation.defaultValue());

        if(annotation.notnull())
            column.setNotnull(true);

        if(annotation.unique())
            column.setUnique(true);

        if(annotation.autoIncrement())
            column.setAutoIncrement(true);

        if(field.isAnnotationPresent(PrimaryKey.class)) {
            column.setPrimaryKey(true);
            column.setGenerationPolicy(field.getAnnotation(PrimaryKey.class).value());
        }

        column.afterSetter();

        return column;
    }

}
