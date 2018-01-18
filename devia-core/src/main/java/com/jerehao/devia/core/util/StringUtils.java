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

import com.jerehao.devia.common.annotation.Nullable;

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

    public static boolean isAnyEmptyOrNull(String str, String... strings) {
        if(isEmptyOrNull(str))
            return true;
        for (String s : strings) {
            if(isEmptyOrNull(s))
                return true;
        }
        return false;
    }

    public static boolean contains(String s, CharSequence contains) {
        if(s == null)
            return false;
        return s.contains(contains);
    }

    public static boolean equals(String str, String str2) {
        return Objects.equals(str, str2);
    }

    public static boolean equalsAny(String str, String... strings) {
        if(strings == null || strings.length < 1)
            return str == null;

        for(String s : strings)
            if(equals(str, s))
                return true;

        return false;
    }

    public static String build(String s, Object... args) {
        return MessageFormat.format(s, args);
    }

    public static String trim(String s) {
        if(s == null)
            return null;
        return s.trim();
    }

    public static String[] split(String str, String separator) {
        if(str == null)
            return null;
        return str.split(separator);
    }

    public static String join(@Nullable String[] strings, String separator) {
        if(strings == null)
            return null;

        return join(strings, separator, 0, strings.length);
    }

    public static String join(@Nullable String[] strings, String separator, int start) {
        if(strings == null)
            return null;

        return join(strings, separator, start, strings.length);
    }

    /**
     * <pre>
     *     join(new String[]{"a","b","c"}, "-", 0, 2)   "a-b-c"
     *     join(new String[]{"a","b","c"}, "-", 0, 1)   "a-b"
     *     join(new String[]{"a","b","c"}, "-", 0, -1)   "a-b"
     * </pre>
     *
     * @param strings
     * @param separator
     * @param start
     * @param end
     * @return
     */
    public static String join(@Nullable String[] strings, String separator , int start , int end) {
        if(strings == null)
            return null;

        StringBuilder join = new StringBuilder();

        for(int i = start ; i < end ; ++i)
            join.append(strings[i]).append(separator);
        join.append(strings[end]);

        return join.toString();
    }

    public static String replace(String str, String target, String replacement) {
        if(StringUtils.isEmptyOrNull(str))
            return str;

        return str.replace(target,replacement);
    }

    private StringUtils() {}


}
