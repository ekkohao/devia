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


import com.jerehao.devia.orm.model.annotation.Column;
import com.jerehao.devia.orm.model.annotation.Model;
import com.jerehao.devia.orm.model.annotation.PrimaryKey;
import com.jerehao.devia.orm.model.support.DataType;
import com.jerehao.devia.orm.model.support.GenerationPolicy;
import com.jerehao.devia.orm.query.Property;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-20 15:32 jerehao
 */

@Model
public class Author {

    @PrimaryKey(GenerationPolicy.UUID)
    @Column(type = DataType.INT, autoIncrement = true)
    public static final Property ID = Property.forName("ID");

    @Column(type = DataType.VARCHAR, length = 36, notnull = true, unique = true)
    public static final Property NAME = Property.forName("name");

    @Column(type = DataType.TINY_INT)
    public static final Property SEX = Property.forName("sex");

    @Column(type = DataType.TINY_INT, defaultValue = "0")
    public static final Property AGE = Property.forName("age");
    
}
