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

import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-16 14:48 jerehao
 */
public final class StringUtils {

    public static boolean isEmptyOrNull(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean equals(String s, String s2) {
        return Objects.equals(s, s2);
    }

    public static String build(String s, Object... args) {
        return MessageFormat.format(s, args);
    }

    public static String trim(String s) {
        if(s == null)
            return null;
        return s.trim();
    }

    private StringUtils() {}
}
