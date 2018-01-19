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

package com.jerehao.devia.repository.jdbc;

import com.jerehao.devia.application.ApplicationProperties;
import com.jerehao.devia.logging.Logger;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-18 20:49 jerehao
 */
public final class SimpleDataSource implements DataSource {

    private static final Logger LOGGER = Logger.getLogger(SimpleDataSource.class);

    private String user;

    private String pass;

    private String url;

    private int transactionIsolation;

    private static SimpleDataSource instance = null;

    private SimpleDataSource() {
        init();
    }

    public static SimpleDataSource getInstance() {
        if(instance == null)
            instance = new SimpleDataSource();
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(user, pass);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {

        final Connection connection = DriverManager.getConnection(url, username, password);
        connection.setTransactionIsolation(transactionIsolation);
        return connection;

    }


    @SuppressWarnings("unckecked")
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        throw new SQLException("DataSource of type [" + getClass().getName() +
                "] cannot be unwrapped as [" + iface.getName() + "]");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException("getLogWriter");
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException("setLogWriter");
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException("setLoginTimeout");
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return LOGGER;
    }

    private void init() {
        try {
            //register driver
            Class.forName(ApplicationProperties.getProperty(ApplicationProperties.Keys.JDBC_DRIVER));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("jdbc.driver cannot load.");
        }

        user = ApplicationProperties.getProperty(ApplicationProperties.Keys.JDBC_USERNAME);
        pass = ApplicationProperties.getProperty(ApplicationProperties.Keys.JDBC_PASSWORD);
        url = ApplicationProperties.getProperty(ApplicationProperties.Keys.JDBC_URL);

        String isolation = ApplicationProperties.getProperty(ApplicationProperties.Keys.JDBC_TRANSACTION_ISOLATION);

        switch (isolation) {
            case "READ_COMMITTED": transactionIsolation = Connection.TRANSACTION_READ_COMMITTED; break;
            case "READ_UNCOMMITTED": transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED; break;
            case "READ_REPEATABLE": transactionIsolation = Connection.TRANSACTION_REPEATABLE_READ; break;
            case "READ_SERIALIZABLE": transactionIsolation = Connection.TRANSACTION_SERIALIZABLE; break;
            default: transactionIsolation = Connection.TRANSACTION_NONE;
        }
    }
}
