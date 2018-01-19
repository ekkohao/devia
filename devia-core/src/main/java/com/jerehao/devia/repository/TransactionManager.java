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

import com.jerehao.devia.repository.jdbc.SimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-19 16:15 jerehao
 */
public class TransactionManager {

    private ThreadLocal<Transaction> transaction = new ThreadLocal<>();

    private ThreadLocal<Connection> connection = new ThreadLocal<>();

    private DataSource dataSource;

    private static TransactionManager instance = null;

    private TransactionManager() {
        dataSource = SimpleDataSource.getInstance();
    }

    public static TransactionManager getInstance() {
        if(instance == null)
            instance = new TransactionManager();
        return instance;
    }

    public void startTransaction() {
        if(!hasTransaction())
            transaction.set(new Transaction(dataSource));
    }

    public Transaction getTransaction() {
        if(!hasTransaction())
            transaction.set(new Transaction(dataSource));
        return transaction.get();
    }

    public void closeTransaction() {
        if(hasTransaction()) {
            transaction.get().close();
            transaction.set(null);
        }
    }

    public boolean hasTransaction() {
        return transaction.get() != null;
    }

    public Connection getConnection() {
        if(connection.get() == null) {
            try {
                connection.set(SimpleDataSource.getInstance().getConnection());
            } catch (SQLException e) {
                String msg = "Get JDBC connection error. " + e.getMessage();
                throw new RuntimeException(msg, e);
            }
        }
        return connection.get();
    }
    public void closeConnection() {
        if(connection.get() == null)
            return;
        try {
            connection.get().close();
            connection.set(null);
        } catch (SQLException e) {
            String msg = "Close JDBC connection error. " + e.getMessage();
            throw new RuntimeException(msg, e);
        }
    }
}
