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

package com.jerehao.devia.demo.repository;

import com.jerehao.devia.bean.annotation.Named;
import com.jerehao.devia.demo.model.Author;
import com.jerehao.devia.orm.repository.AbstractRepository;
import org.json.JSONObject;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-30 18:20 jerehao
 */
@Named("aaa")
public class AuthorRepository extends AbstractRepository {

    public AuthorRepository() {
        super(Author.class);
        JSONObject author = new JSONObject();
        author.put(Author.NAME.getName(), "aaa");
        author.put(Author.SEX.getName(), 1);
        author.put(Author.AGE.getName(), 11);

        save(author);
    }

}
