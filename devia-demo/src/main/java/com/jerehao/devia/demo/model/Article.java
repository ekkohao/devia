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
import com.jerehao.devia.orm.query.Property;
import org.json.JSONObject;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-20 15:32 jerehao
 */

@Model
public class Article{

    @PrimaryKey
    @Column(type = DataType.INT, autoIncrement = true)
    public static final Property ID = Property.forName("ID");

    @Column(type = DataType.VARCHAR, length = 60, notnull = true)
    public static final Property TITLE = Property.forName("title");

    @Column(type = DataType.TEXT)
    public static final Property PASSAGE = Property.forName("passage");

    @Column(type = DataType.INT)
    public static final Property AUTHOR_ID = Property.forName("author_ID");
}
