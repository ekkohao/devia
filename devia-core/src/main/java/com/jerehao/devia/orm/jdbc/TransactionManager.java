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

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-30 17:43 jerehao
 */
public class TransactionManager {

    private final DataSource dataSource;

    private static TransactionManager instance = null;

    private ThreadLocal<Transaction> transaction = new ThreadLocal<>();

    private TransactionManager() {
        this.dataSource = DruidDataSourceInner.getDataSource();
    }

    public static TransactionManager getTransactionManager() {
        if(instance == null)
            instance = new TransactionManager();

        return instance;
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

    public DataSource getDataSource() {
        return dataSource;
    }
}
