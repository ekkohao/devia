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

package com.jerehao.devia.servlet.helper;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-08 20:37 jerehao
 */
public class HttpCacheControl {

    private long maxAge = -1;

    private boolean noCache = false;

    private boolean noStore = false;

    private boolean isPublic = false;

    private boolean isPrivate = false;

    private boolean noTransform = false;

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }

    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    public void setNoStore(boolean noStore) {
        this.noStore = noStore;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setNoTransform(boolean noTransform) {
        this.noTransform = noTransform;
    }

    public String getResponseHeader(){
        List<String> headers = new LinkedList<>();

        if(this.maxAge != -1)
            headers.add("max-age=" + this.maxAge);
        if(this.noCache)
            headers.add("no-cache");
        if(this.noStore)
            headers.add("no-store");
        if(this.noTransform)
            headers.add("no-transform");
        if(this.isPrivate)
            headers.add("private");
        if(this.isPublic)
            headers.add("public");

        return headers.isEmpty() ? null : StringUtils.join(headers,",");
    }
}
