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

import com.jerehao.devia.core.common.annotation.NotNull;
import com.jerehao.devia.logging.Logger;
import com.jerehao.devia.orm.model.DeviaModelManager;
import com.jerehao.devia.orm.model.ModelManager;
import com.jerehao.devia.orm.model.support.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-25 13:57 jerehao
 */
public class MySQLDatabaseEngine implements DatabaseEngine {

    private static final Logger LOGGER = Logger.getLogger(MySQLDatabaseEngine.class);

    private static MySQLDatabaseEngine instance;

    private SQLStatementGenerator statementGenerator = new MySQLStatementGenerator(this);

    private JDBCStatementActuator statementActuator = new JDBCStatementActuator(this);

    private ModelValueConverter modelValueConverter = new ModelValueConverterImpl();

    private ModelManager modelManager = DeviaModelManager.getModelManager();

    private ConnectionManager connectionManager = ConnectionManager.getConnectionManager();

    private TransactionManager transactionManager = TransactionManager.getTransactionManager();

    private MySQLDatabaseEngine() {}

    public static MySQLDatabaseEngine getDatabaseEngine() {
        if(instance == null)
            instance = new MySQLDatabaseEngine();

        return instance;
    }

    @Override
    public void rebuildDatabase() {
        try {
            Connection connection = connectionManager.getDataSource().getConnection();
            connection.setAutoCommit(true);

            for (Model model : modelManager.getModels()) {
                PreparedStatement ps;

                ps = statementGenerator.getDropTable(connection, model);
                ps.execute();
                ps.close();

                ps = statementGenerator.getCreateTable(connection, model);
                ps.execute();
                ps.close();
            }

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String mappingDataType(@NotNull DataType dataType) throws SQLException {
        switch (dataType) {
            case TINY_INT: return "TINYINT";
            case SMALL_INT: return "SMALLINT";
            case INT: return "INT";
            case BIG_INT: return "BIGINT";
            case FLOAT: return "FLOAT";
            case DOUBLE: return "DOUBLE";
            case DECIMAL: return "DECIMAL";
            case CHAR: return "CHAR";
            case NCHAR: return "NCHAR";
            case VARCHAR: return "VARCHAR";
            case NVARCHAR: return "NVARCHAR";
            case TEXT: return "TEXT";
            case NTEXT: return "NTEXT";
            case DATETIME: return "DATETIME";
            case TIMESTAMP: return "TIMESTAMP";
            case BLOB: return "BLOB";
            case CLOB: return "TEXT";
            case BOOLEAN: return "BOOLEAN";
            default: throw new SQLException("Cannot determine [" + dataType + "] to SqlDataType, maybe not define.");
        }
    }

    @Override
    public SQLStatementGenerator getStatementGenerator() {
        return statementGenerator;
    }

    @Override
    public JDBCStatementActuator getStatementActuator() {
        return statementActuator;
    }

    @Override
    public ModelValueConverter getModelValueConverter() {
        return modelValueConverter;
    }

    @Override
    public ModelManager getModelManager() {
        return modelManager;
    }

    @Override
    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    @Override
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }
}
