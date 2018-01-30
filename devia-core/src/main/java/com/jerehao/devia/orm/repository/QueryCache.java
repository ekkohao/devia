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

package com.jerehao.devia.orm.repository;

import org.json.JSONObject;

import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-19 14:19 jerehao
 */
public class QueryCache {
    // TODO 重做缓存控制
    //    <id, value>
    private final Map<Number,JSONObject> caches = new HashMap<>();

    public void save(Number id, JSONObject jsonObject) {
        caches.put(id, jsonObject);
    }

    public void remove(Number id) {
        caches.remove(id);
    }

    public JSONObject find(Number id) {
        if(caches.containsKey(id))
            return caches.get(id);
        return null;
    }

    public JSONObject find(String key, Object value) {
        for (Number number : caches.keySet()) {
            JSONObject jsonObject = caches.get(number);
            if (Objects.equals(jsonObject.get(key), value))
                return jsonObject;
        }

        return null;
    }

}
