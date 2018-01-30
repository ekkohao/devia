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

import com.jerehao.devia.logging.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-30 15:55 jerehao
 */
public class ConnectionManager {

    private static final Logger LOGGER = Logger.getLogger(ConnectionManager.class);

    private final DataSource dataSource;

    private static ConnectionManager instance;

    private ThreadLocal<Connection> connection = new ThreadLocal<>();

    private ConnectionManager() {
        this.dataSource = DruidDataSourceInner.getDataSource();
    }

    public static ConnectionManager getConnectionManager() {
        if(instance == null)
            instance = new ConnectionManager();

        return instance;
    }

    public Connection getConnection() {
        if(connection.get() == null) {
            Connection con = null;
            try {
                con = dataSource.getConnection();
                con.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            connection.set(con);
            return con;
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

    public DataSource getDataSource() {
        return dataSource;
    }
}
