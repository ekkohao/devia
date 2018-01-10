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

package com.jerehao.devia.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-08 17:20 jerehao
 */
public class MimeType {
    private Map<String, String> mimeTypeMappings = new HashMap<>();

    public MimeType(){
        init();
    }

    private void init() {
        addMimeTypeMapping(".","application/x-");
        addMimeTypeMapping(".*", "application/octet-stream");
        addMimeTypeMapping(".css","text/css");
        addMimeTypeMapping(".js","application/javascript");
        addMimeTypeMapping(".json","application/json");
        addMimeTypeMapping(".exe","application/x-msdownload");
        addMimeTypeMapping(".zip","application/zip");
        addMimeTypeMapping(".pdf","application/pdf");
        addMimeTypeMapping(".dtd","application/xml-dtd");
        addMimeTypeMapping(".xml","application/xml");
        addMimeTypeMapping(".xhtml","application/xhtml+xml");
        addMimeTypeMapping(".gzip","application/gzip");
        addMimeTypeMapping(".xls","application/x-xls");
        addMimeTypeMapping(".htm","text/html");
        addMimeTypeMapping(".html","text/html");
        addMimeTypeMapping(".txt","text/plain");
        addMimeTypeMapping(".tif","image/tiff");
        addMimeTypeMapping(".gif","image/gif");
        addMimeTypeMapping(".ico","image/x-icon");
        addMimeTypeMapping(".jpe","image/jpeg");
        addMimeTypeMapping(".jpg","image/jpeg");
        addMimeTypeMapping(".jpeg","image/jpeg");
        addMimeTypeMapping(".png","image/png");
        addMimeTypeMapping(".tif","image/tiff");
        addMimeTypeMapping(".tiff","image/tiff");
    }

    public void addMimeTypeMapping(String extend, String mimeType) {
        mimeTypeMappings.put(extend, mimeType);
    }

    public String getMimeType(String extend) {
        return mimeTypeMappings.get(extend);
    }
}
