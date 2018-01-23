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

package com.jerehao.devia.demo.model;

import com.jerehao.devia.model.annotation.Column;
import com.jerehao.devia.model.annotation.Model;
import com.jerehao.devia.model.annotation.PrimaryKey;
import com.jerehao.devia.model.table.DataType;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-20 15:32 jerehao
 */

@Model
public class Author {

    @PrimaryKey
    @Column(type = DataType.INT, autoIncrement = true)
    public static final String ID = "ID";

    @Column(type = DataType.VARCHAR, length = 36, notnull = true, unique = true)
    public static final String NAME = "name";

    @Column(type = DataType.TINY_INT)
    public static final String SEX = "SEX";

    @Column(type = DataType.TINY_INT, defaultValue = "0")
    public static final String ROLE = "role";
    
}
