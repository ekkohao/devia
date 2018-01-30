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
import com.jerehao.devia.core.common.annotation.Nullable;
import com.jerehao.devia.core.util.StringUtils;
import com.jerehao.devia.orm.model.support.Column;
import com.jerehao.devia.orm.model.support.GenerationPolicy;
import com.jerehao.devia.orm.model.support.Model;
import com.jerehao.devia.orm.model.support.Table;
import com.jerehao.devia.orm.query.*;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-23 10:37 jerehao
 */
public class MySQLStatementGenerator implements SQLStatementGenerator {


    private final DatabaseEngine databaseEngine;

    public MySQLStatementGenerator(MySQLDatabaseEngine databaseEngine) {
        this.databaseEngine = databaseEngine;
    }

    @Override
    public PreparedStatement getInsert(Connection connection, Model model, JSONObject entity) throws SQLException {
        setUUIDIfNecessary(model, entity);

        List<Column> columns = getContainedColumns(entity, model);;
        String preparedSQL = getInsertPreparedSQL(model.getTable().getName(), columns);

        PreparedStatement preparedStatement = connection.prepareStatement(preparedSQL);
        int index = 0;

        for(Column column : columns) {
            String key = column.getName();

            databaseEngine.getModelValueConverter().toSave(column, preparedStatement, ++index, entity.get(key));
        }

        return preparedStatement;
    }

    private void setUUIDIfNecessary(Model model, JSONObject entity) {
        for (Column column : model.getPrimaryKeys()) {
            if(column.getGenerationPolicy() == GenerationPolicy.UUID) {
                UUID uuid = UUID.randomUUID();
                entity.put(column.getName(), uuid.toString().replace("-",""));
            }
        }
    }

    private String getInsertPreparedSQL(String tableName, List<Column> columns) {
        StringBuilder sb = new StringBuilder();
        StringBuilder args = new StringBuilder();

        sb.append("INSERT INTO ").append(backQuote(tableName));
        sb.append("(");

        Iterator<Column> itr = columns.iterator();

        if(itr.hasNext()) {
            Column column = itr.next();
            sb.append(backQuote(column.getName()));
            args.append("?");
        }

        while (itr.hasNext()) {
            Column column = itr.next();
            sb.append(",").append(backQuote(column.getName()));
            args.append(",?");
        }

        sb.append(") VALUES(").append(args).append(");");
        return sb.toString();
    }

    @Override
    public PreparedStatement getUpdate(Connection connection, Update update) throws SQLException {
        Map<Column, Object> args = new LinkedHashMap<>();
        String sql = getUpdateSQL(update, args);
        PreparedStatement ps = connection.prepareStatement(sql);
        int index = 0;

        for(Column column : args.keySet()) {
            databaseEngine.getModelValueConverter().toSave(column, ps, ++index, args.get(column));
        }

        return ps;
    }

    private String getUpdateSQL(Update update, Map<Column, Object> args) throws SQLException {
        UpdateGetter ug = new UpdateGetter(update);
        Set<Assignment.Assign> assigns = ug.getAssigns();
        List<Condition.Term> terms = ug.getTerms();
        List<Condition.RelationOperator> operators = ug.getOperators();
        Set<String> tableNames = new LinkedHashSet<>();

        if(assigns.isEmpty())
            throw new SQLException("UPDATE empty");

        Model model = assigns.iterator().next().getLeft().getModel();

        String assignSQL = paresAssgins(assigns, args, tableNames);
        String termSQL = parseTerms(terms, operators, args, tableNames);

        StringBuilder sb = new StringBuilder("UPDATE ").append(backQuote(model.getTable().getName()));

        sb.append(" SET ").append(assignSQL);
        sb.append(" WHERE ").append(termSQL);
        sb.append(";");

        return sb.toString();
    }

    private String paresAssgins(Set<Assignment.Assign> assigns, Map<Column, Object> args, Set<String> tableNames) throws SQLException {
        if(assigns.isEmpty())
            throw new SQLException("UPDATE empty");

        StringBuilder sb = new StringBuilder();

        for(Assignment.Assign assign : assigns) {
            sb.append(parseAssign(assign, args, tableNames)).append(",");
        }

        return sb.toString();
    }

    private String parseAssign(Assignment.Assign assign, Map<Column, Object> args, Set<String> tableNames) throws SQLException {
        StringBuilder sb = new StringBuilder();
        Property left = assign.getLeft();
        Object right = assign.getRight();

        sb.append(getPropertySql(left, tableNames)).append("=");

        if(right instanceof Property) {
            sb.append(getPropertySql((Property) right, tableNames));
        }
        else {
            sb.append("?");
            args.put(left.getColumn(), right);
        }

        return sb.toString();
    }

    private String parseTerms(List<Condition.Term> terms, List<Condition.RelationOperator> operators,
                              Map<Column, Object> args, Set<String> tableNames) throws SQLException {
        if(terms.isEmpty())
            return "";

        if(terms.size() != operators.size() + 1)
            throw new SQLException("Condition operator and condition term not matched.");

        if(operators.isEmpty())
            return parseTerm(terms.get(0), args, tableNames);

        List<String> mainStack = new LinkedList<>();
        List<Integer> sequenceStack = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        Iterator<Condition.Term> termIterator = terms.iterator();
        Iterator<Condition.RelationOperator> operatorIterator = operators.iterator();

        mainStack.add(0,parseTerm(termIterator.next(), args, tableNames));

        while (!mainStack.isEmpty() && operatorIterator.hasNext()) {

            Condition.RelationOperator currentRO = operatorIterator.next();

            if(!sequenceStack.isEmpty() && sequenceStack.get(0) < currentRO.getSequence()) {

                int flag = sequenceStack.get(0);
                StringBuilder current = new StringBuilder();

                current.append(" ").append(mainStack.remove(0)).append(" ");

                while (!sequenceStack.isEmpty() && sequenceStack.get(0) == flag) {
                    sequenceStack.remove(0);
                    current.insert(0, mainStack.remove(0));
                    current.insert(0, " " + mainStack.remove(0) + " ");
                }

                mainStack.add(0, " (" + current.toString() + ") ");

            }
            else {
                mainStack.add(0, currentRO.getOperator());
                mainStack.add(0, parseTerm(termIterator.next(), args, tableNames));
                sequenceStack.add(0, currentRO.getSequence());
            }
        }

        while (!mainStack.isEmpty()) {
            int flag = sequenceStack.get(0);
            StringBuilder current = new StringBuilder();

            current.append(" ").append(mainStack.remove(0)).append(" ");

            while (!sequenceStack.isEmpty() && sequenceStack.get(0) == flag) {
                sequenceStack.remove(0);
                current.insert(0, mainStack.remove(0));
                current.insert(0, " " + mainStack.remove(0) + " ");
            }

            if(mainStack.isEmpty()) {
                sb.append(current.toString());
                break;
            }
            else
                mainStack.add(0, " (" + current.toString() + ") ");
        }

        return sb.toString().trim();
    }

    private String parseTerm(Condition.Term term, Map<Column, Object> args, Set<String> tableNames) throws SQLException {
        StringBuilder sb = new StringBuilder();
        Property left = term.getLeft();
        Object right = term.getRight();
        sb.append(getPropertySql(left, tableNames)).append(term.getOperator().getSymbol());
        if(right instanceof Property){
            sb.append(getPropertySql((Property) right, tableNames));
        }
        else {
            sb.append('?');
            args.put(left.getColumn(), right);
        }

        return sb.toString();
    }

    private String getPropertySql(Property property, Set<String> tableNames) {
        StringBuilder sb = new StringBuilder();
        String tableName = property.getModel().getTable().getName();

        if(!tableNames.contains(tableName))
            tableNames.add(tableName);

        sb.append(backQuote(tableName)).append('.').append(backQuote(property.getName()));

        return sb.toString();
    }

//    public PreparedStatement getSelect(Connection connection, Criteria criteria) {
//
//    }
//
//    public PreparedStatement getDelete(Connection connection, Model model, Object entity) {
//
//    }

    @Override
    public PreparedStatement getDropTable(Connection connection, Model model) throws SQLException {
        String tableName = model.getTable().getName();
        String preparedSQL = StringUtils.build("DROP TABLE IF EXISTS `{0}`;", tableName);
        return connection.prepareStatement(preparedSQL);
    }

    @Override
    public PreparedStatement getCreateTable(Connection connection, Model model) throws SQLException {
        Table table = model.getTable();
        List<Column> columns = model.getColumns();
        StringBuilder sb = new StringBuilder(createTablePrefix(table));
        StringBuilder uniques = new StringBuilder();

        if(columns.isEmpty()) {
            throw new RuntimeException("Table " + model.getTable().getName() + " must have at least one column");
        }

        sb.append(" (");
        for(Column column : columns) {
            sb.append(createColumn(column)).append(",");

            if(column.isUnique()) {
                uniques.append("UNIQUE (").append(backQuote(column.getName())).append("),");
            }
        }

        List<Column> pkColumns = model.getPrimaryKeys();

        if(pkColumns != null && !pkColumns.isEmpty()){
            sb.append("PRIMARY KEY (");

            pkColumns.forEach((pk) -> sb.append(pk.getName()).append(","));

            sb.deleteCharAt(sb.length() - 1);
            sb.append("),");
        }
        sb.append(uniques).deleteCharAt(sb.length() - 1);
        sb.append(") ").append(createTableSuffix(table));

        return connection.prepareStatement(sb.toString());
    }

    private String createTablePrefix(Table table) {
        return "CREATE TABLE `" + table.getName() + "`";
    }

    private String createTableSuffix(Table table) {
        StringBuilder sb = new StringBuilder();
        sb.append("ENGINE=").append(table.getEngine());
        sb.append(" DEFAULT CHARSET=").append(table.getDefaultCharSet());

        if(table.getAutoIncrement() > 1)
            sb.append(" AUTO_INCREMENT=").append(table.getAutoIncrement());
        sb.append(";");
        return sb.toString();
    }

    private String createColumn(Column column) throws SQLException {
        StringBuilder sb = new StringBuilder();

        sb.append(backQuote(column.getName())).append(" ").append(getColumnSQLType(column));
        if(column.isNotnull())
            sb.append(" NOT NULL");

        if(column.isAutoIncrement())
            sb.append(" AUTO_INCREMENT");

        if(!StringUtils.isEmptyOrNull(column.getDefaultValue())) {
            sb.append(" DEFAULT ");
            if(column.getType().isNumeric())
                sb.append(column.getDefaultValue());
            else
                sb.append(singleQuote(column.getDefaultValue()));
        }

        return sb.toString();
    }

    private String getColumnSQLType(Column column) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append(databaseEngine.mappingDataType(column.getType()));

        if(column.getType().isLengthAvailable() && column.getLength() > 0)
            sb.append("(").append(column.getLength()).append(")");

        return sb.toString();

    }

    private String singleQuote(@NotNull String s) {
        StringBuilder sb = new StringBuilder("'");

        for(int i = 0, len = s.length(); i < len; ++i) {
            char c = s.charAt(i);
            switch (c) {
                case '\'': sb.append("''"); break;
                default: sb.append(c);
            }
        }

        sb.append("'");

        return sb.toString();

    }

    private String backQuote(@NotNull String s) {
        return "`" + s + "`";
    }

    private List<Column> getContainedColumns(@Nullable JSONObject jsonObject, @NotNull Model model) throws SQLException {
        List<Column> columns = new LinkedList<>();

        if (jsonObject == null || jsonObject.length() < 1)
            return columns;

        if(model == null)
            throw new RuntimeException("The 2nd argument is cannot be null.");

        for(String key : jsonObject.keySet()) {
            columns.add(model.getColumn(key));
        }

        return columns;
    }
}
