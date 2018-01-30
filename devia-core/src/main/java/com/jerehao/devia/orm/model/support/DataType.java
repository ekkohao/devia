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

package com.jerehao.devia.orm.model.support;

import java.math.BigDecimal;
import java.sql.*;


/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-20 15:47 jerehao
 */
public enum DataType {
    TINY_INT(0x00000001, Short.class),
    SMALL_INT(0x00000002, Short.class),
    INT(0x00000004, Integer.class),
    BIG_INT(0x00000008, Long.class),
    FLOAT(0x00000010, Float.class),
    DOUBLE(0x00000020, Double.class),
    DECIMAL(0x00000040, BigDecimal.class),
    CHAR(0x00000080, String.class),
    NCHAR(0x00000100, String.class),
    VARCHAR(0x00000200, String.class),
    NVARCHAR(0x00000400, String.class),
    DATETIME(0x00000800, Date.class),
    TIMESTAMP(0x00001000, Timestamp.class),
    TEXT(0x00002000, String.class),
    NTEXT(0x00004000, String.class),
    BLOB(0x00008000, Blob.class),
    CLOB(0x00010000, Clob.class),
    BOOLEAN(0x00020000, Boolean.class);

    public static final int ALL_INTEGER = TINY_INT.code | SMALL_INT.code | INT.code | BIG_INT.code;

    public static final int ALL_DECIMAL = FLOAT.code | DOUBLE.code | DECIMAL.code;

    public static final int NUMBER = ALL_DECIMAL | ALL_INTEGER;

    public static final int LENGTH_AVAILABLE = NUMBER | CHAR.code | NCHAR.code | VARCHAR.code | NVARCHAR.code
            | DATETIME.code | TIMESTAMP.code;

    private int code;

    private Class<?> javaClass;

    DataType(int code, Class<?> javaClass) {
        this.code = code;
        this.javaClass = javaClass;
    }

    public boolean isInteger() {
        return (code & ALL_INTEGER) != 0;
    }

    public boolean isNumeric() {
        return (code & NUMBER) != 0;
    }

    public boolean isLengthAvailable() {
        return (code & LENGTH_AVAILABLE) != 0;
    }

    public Class<?> getJavaClass() {
        return javaClass;
    }

}
