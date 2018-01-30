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

package com.jerehao.devia.orm.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-19 20:38 jerehao
 */
public class Transaction {

    private final DataSource dataSource;

    private Connection connection;

    public Transaction(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                String msg = "Get JDBC connection error. " + e.getMessage();
                throw new RuntimeException(msg, e);
            }
        }
        return connection;
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            String msg = "Transaction rollback error. " + e.getMessage();
            throw new RuntimeException(msg, e);
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            String msg = "Transaction commit error. " + e.getMessage();
            throw new RuntimeException(msg, e);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            String msg = "Transaction connection close error. " + e.getMessage();
            throw new RuntimeException(msg, e);
        }
    }
}
