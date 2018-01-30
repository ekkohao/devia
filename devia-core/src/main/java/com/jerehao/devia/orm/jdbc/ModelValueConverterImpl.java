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



import com.jerehao.devia.orm.model.support.Column;

import javax.sql.rowset.serial.SerialClob;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Enumeration;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-25 9:46 jerehao
 */
public class ModelValueConverterImpl implements ModelValueConverter {
    @Override
    public void toSave(Column column, PreparedStatement ps, int index, Object value) throws SQLException {
        if(value == null) {
            ps.setObject(index, null);
        }

        if(value instanceof Enum) {
            if(column.getType().isInteger())
                ps.setObject(index, ((Enum) value).ordinal());
            else
                ps.setObject(index, ((Enum) value).name());
            return;
        }


        switch (column.getType()) {
            case TINY_INT:ps.setByte(index, Byte.valueOf(String.valueOf(value))); break;
            case SMALL_INT: ps.setShort(index, Short.valueOf(String.valueOf(value))); break;
            case INT: ps.setInt(index, Integer.valueOf(String.valueOf(value))); break;
            case BIG_INT: ps.setLong(index, Long.valueOf(String.valueOf(value))); break;
            case FLOAT: ps.setFloat(index, Float.valueOf(String.valueOf(value))); break;
            case DOUBLE: ps.setDouble(index, Double.valueOf(String.valueOf(value))); break;
            case DECIMAL: ps.setBigDecimal(index, (BigDecimal) value); break;
            case DATETIME: ps.setDate(index, (java.sql.Date) value); break;
            case TIMESTAMP: ps.setTimestamp(index, (Timestamp) value); break;
            case BLOB: ps.setBlob(index, (Blob) value); break;
            case BOOLEAN: ps.setBoolean(index, Boolean.valueOf(String.valueOf(value))); break;

            default: ps.setObject(index, value);
        }

    }

    @Override
    public Object toGet(Column column, ResultSet rs) throws SQLException {
        String name = column.getName();
        String value = rs.getString(name);

        if(value == null)
            return null;

        switch (column.getType()) {
            case TINY_INT:
            case SMALL_INT: return rs.getShort(name);
            case INT: return rs.getInt(name);
            case BIG_INT: return rs.getLong(name);
            case FLOAT: return rs.getFloat(name);
            case DOUBLE: return rs.getDouble(name);
            case DECIMAL: return rs.getBigDecimal(name);
            case DATETIME: return rs.getDate(name);
            case TIMESTAMP: return rs.getTimestamp(name);
            case BLOB: return rs.getBlob(name);
            case CLOB: return new SerialClob(value.toCharArray());
            case BOOLEAN: return rs.getBoolean(name);
            default: return value;
        }
    }

}
