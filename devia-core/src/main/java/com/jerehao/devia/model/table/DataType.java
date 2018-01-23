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

package com.jerehao.devia.model.table;

import com.jerehao.devia.core.util.StringUtils;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-20 15:47 jerehao
 */
public enum DataType {
    TINY_INT(0, "TINYINT"),
    SMALL_INT(1, "SMALLINT"),
    INT(2, "INT"),
    BIG_INT(3, "BIGINT"),
    FLOAT(10, "FLOAT"),
    DOUBLE(11, "DOUBLE"),
    DECIMAL(12, "DECIMAL"),
    CHAR(100, "CHAR"),
    NCHAR(101, "NCHAR"),
    VARCHAR(102, "VARCHAR"),
    NVARCHAR(103, "NVARCHAR"),
    DATETIME(300, "DATETIME"),
    TIMESTAMP(301, "TIMESTAMP"),
    TEXT(500, "TEXT"),
    NTEXT(501, "NTEXT"),
    BLOB(502, "BLOB"),
    CLOB(503, "TEXT");

    private int code;

    private String name;

    DataType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public boolean isInteger() {
        return code < 10;
    }

    public boolean isNumeric() {
        return code < 100;
    }

    public String toSQLString(int length) {
        if(length < 0 || code >= 500)
            return name;
        return StringUtils.build("{0}({1})", name, length);
    }
}
