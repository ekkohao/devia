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

package com.jerehao.devia.orm.repository;

import com.jerehao.devia.application.ApplicationManager;
import com.jerehao.devia.orm.jdbc.JDBCStatementActuator;
import com.jerehao.devia.orm.model.ModelManager;
import com.jerehao.devia.orm.jdbc.DatabaseEngine;
import com.jerehao.devia.orm.jdbc.MySQLDatabaseEngine;
import com.jerehao.devia.orm.model.support.Model;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-19 14:16 jerehao
 */
public class AbstractRepository implements Repository {

    private final Model model;

    private final Class<?> clazz;

    //private final DatabaseEngine databaseEngine;

    private final JDBCStatementActuator statementActuator;

    protected AbstractRepository(Class<?> clazz) {
        this.clazz = clazz;

        DatabaseEngine databaseEngine = MySQLDatabaseEngine.getDatabaseEngine();

        statementActuator = databaseEngine.getStatementActuator();
        ModelManager modelManager = databaseEngine.getModelManager();

        try {
            model = modelManager.getModel(clazz);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void save(JSONObject object) {
        try {
            statementActuator.executeInsert(model, object);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //resolveObject(object, keys, values);
    }
}
