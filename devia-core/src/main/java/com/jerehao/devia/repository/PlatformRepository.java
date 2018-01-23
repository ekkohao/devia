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

import org.json.JSONObject;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-19 23:28 jerehao
 */
public class PlatformRepository {

    private final TransactionManager transactionManager;

    protected PlatformRepository(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * should not to use
     *
     * @param prepare
     * @param params
     * @return <code>true</code> if the first result is a <code>ResultSet</code>
     *         object;
     *         <code>false</code> if the first result is an update
     *         count or there is no result
     */
    protected boolean executeSql(String prepare, Object... params) {
        Connection connection = getConnection();
        boolean ret;
        try {
            if(params == null || params.length < 1) {
                Statement statement = connection.createStatement();
                ret = statement.execute(prepare);
            }
            else {
                PreparedStatement preparedStatement = connection.prepareStatement(prepare);
                for(int i = 0; i < params.length; ++i) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
                ret = preparedStatement.execute();
            }
        }
        catch (SQLException e) {
            throw new RepositoryException();
        }

        return ret;
    }

    //select
    protected List<JSONObject> executeQuery(String prepare, Object... params) {
        Connection connection = getConnection();
        ResultSet resultSet;
        List<JSONObject> results = new LinkedList<>();
        try {
            if(params == null || params.length < 1) {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(prepare);
            }
            else {
                PreparedStatement preparedStatement = connection.prepareStatement(prepare);
                for(int i = 0; i < params.length; ++i) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
                resultSet = preparedStatement.executeQuery();
            }

            while (resultSet.next()) {
                JSONObject jsonObject = resultSetToJSONObjet(resultSet);
                results.add(jsonObject);
            }
        }
        catch (SQLException e) {
            throw new RepositoryException();
        }

        return results;
    }

    //insert update delete
    protected int executeUpdate(String prepare, Object... params) {
        Connection connection = getConnection();
        int columnAffected = -1;
        try {
            if(params == null || params.length < 1) {
                Statement statement = connection.createStatement();
                columnAffected = statement.executeUpdate(prepare);
            }
            else {
                PreparedStatement preparedStatement = connection.prepareStatement(prepare);
                for(int i = 0; i < params.length; ++i) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
                columnAffected = preparedStatement.executeUpdate();
            }

        }
        catch (SQLException e) {
            throw new RepositoryException();
        }

        return columnAffected;
    }

    protected int executeInsert(String prepare, Object... params) {
        Connection connection = getConnection();
        Statement statement;
        try {
            if(params == null || params.length < 1) {
                statement = connection.createStatement();
                statement.execute(prepare);
            }
            else {
                PreparedStatement preparedStatement = connection.prepareStatement(prepare);
                for(int i = 0; i < params.length; ++i) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
                preparedStatement.executeUpdate();
                statement = preparedStatement;
            }

            ResultSet rs = statement.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1 ;
        }
        catch (SQLException e) {
            throw new RepositoryException();
        }
    }

    private Connection getConnection() {
        return transactionManager.hasTransaction() ?
                transactionManager.getTransaction().getConnection() : transactionManager.getConnection();
    }

    //将resultSet当前指针的
    private JSONObject resultSetToJSONObjet(ResultSet resultSet) {
        JSONObject jsonObject = new JSONObject();
        ///TODO 将resultSet查询结果转为JSONObject

        return jsonObject;
    }
}
