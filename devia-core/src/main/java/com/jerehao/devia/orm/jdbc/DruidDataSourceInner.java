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


import com.alibaba.druid.pool.DruidDataSource;
import com.jerehao.devia.application.ApplicationManager;
import com.jerehao.devia.application.ApplicationProperties;
import com.jerehao.devia.application.ApplicationProperties.Keys;

import javax.sql.DataSource;

import static java.sql.Connection.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-30 15:55 jerehao
 */
public class DruidDataSourceInner {
    private static DruidDataSource dataSource = null;

    public static DataSource getDataSource() {
        if(dataSource == null)
            init();

        return dataSource;
    }

    private static void init() {
        if(dataSource != null)
            return;

        String driver = ApplicationProperties.getProperty(Keys.JDBC_DRIVER);
        String user = ApplicationProperties.getProperty(Keys.JDBC_USERNAME);
        String pass = ApplicationProperties.getProperty(Keys.JDBC_PASSWORD);
        String url = ApplicationProperties.getProperty(Keys.JDBC_URL);
        String isolationMode = ApplicationProperties.getPropertyOrDefault(Keys.JDBC_TRANSACTION_ISOLATION,"");

        dataSource = new DruidDataSource();

        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setPassword(pass);
        dataSource.setUrl(url);

        switch (isolationMode) {
            case "READ_COMMITTED ":
                dataSource.setDefaultTransactionIsolation(TRANSACTION_READ_COMMITTED);
                break;
            case "REPEATABLE_READ ":
                dataSource.setDefaultTransactionIsolation(TRANSACTION_REPEATABLE_READ);
                break;
            case "SERIALIZABLE ":
                dataSource.setDefaultTransactionIsolation(TRANSACTION_SERIALIZABLE);
                break;
            default:
                dataSource.setDefaultTransactionIsolation(TRANSACTION_READ_UNCOMMITTED);
        }

        //TODO 需要写到applicationProperties
        dataSource.setInitialSize(1);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(20);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
//        dataSource.setValidationQuery("SELECT 'x'");
//        dataSource.setTestWhileIdle(true);
        dataSource.setPoolPreparedStatements(false);

    }
}
