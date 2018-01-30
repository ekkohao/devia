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

import com.jerehao.devia.core.common.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-05 19:16 jerehao
 */
public final class AntPathMather {

    public static final String PATH_SEPARATOR = "/";

    public static final String ZERO_OR_MORE_DIRECTORIES_WILDCARD = "**";

    public static final String ZERO_OR_MORE_CHARACTERS_WILDCARD = "*";

    public static final String ONE_CHARACTER_WILDCARD = "?";

    private AntPathMather(){}

    //要保证模式串初始模式一致，比如都以"/"开始
    public static boolean Match(String pattern, String path) {
        return Match(pattern, path, true);
    }

    //TODO test 部分匹配
    public static boolean Match(String pattern, String path, boolean fullMatch) {
        if(StringUtils.isEmptyOrNull(pattern))
            return true;
        if(StringUtils.isEmptyOrNull(path))
            return false;

        String matchedPart = getPartialMatched(pattern, path);
        return fullMatch ? StringUtils.equals(path, matchedPart) : !StringUtils.isEmptyOrNull(matchedPart);
    }

    /**
     * <pre>
     *     getPartialMatched("/a/b/** /f/", "/a/b/c/d/e/f/g/")    "/a/b/c/d/e/f/"
     *     getPartialMatched("/a/b/**", "/a/b/c/d/e/f/g/")    "/a/b/c/d/e/f/g/"
     *     getPartialMatched("/a/b", "/a/b/c/d/e/f/g/")    "/a/b"
     * </pre>
     *
     * @param pattern
     * @param path
     * @return
     */
    public static String getPartialMatched(@Nullable String pattern,@Nullable String path) {
        if(StringUtils.isEmptyOrNull(pattern))
            return path;
        if(StringUtils.isEmptyOrNull(path))
            return "";

        String[] patternArr = StringUtils.split(pattern, PATH_SEPARATOR);
        String[] pathArr = StringUtils.split(path, PATH_SEPARATOR);
        int patternHead = 0, pathHead = 0;
        int patternTail = patternArr.length - 1, pathTail = pathArr.length - 1;

        //比较模式串第一个**之前的模式
        while(pathHead <= pathTail && patternHead <= patternTail) {
            if(StringUtils.equals(patternArr[patternHead], ZERO_OR_MORE_DIRECTORIES_WILDCARD))
                break;
            if(!StringMatch(patternArr[patternHead++], pathArr[pathHead++]))
                return "";
        }

        if(patternHead > patternTail)
            return StringUtils.join(pathArr, PATH_SEPARATOR, 0, pathHead - 1);

        int longestMatchedIndex = pathArr.length - 1;
        if(!StringUtils.equals(patternArr[patternTail], ZERO_OR_MORE_DIRECTORIES_WILDCARD)) {
            //比较最后一个**之后的模式
            int pathTailTmp = pathTail; //record flag
            int patternTailTmp = patternTail;
            boolean partMatched = false;

            while (pathHead <= pathTail && patternHead <= patternTail) {

                if (StringUtils.equals(patternArr[patternTail], ZERO_OR_MORE_DIRECTORIES_WILDCARD)) {
                    partMatched = true;
                    break;
                }

                if (StringMatch(patternArr[patternTail], pathArr[pathTail])) {
                    --pathTail;
                    --patternTail;
                    if (StringUtils.equals(patternArr[patternTail], ZERO_OR_MORE_DIRECTORIES_WILDCARD)) {
                        partMatched = true;
                        break;
                    }
                }
                else {
                    patternTail = patternTailTmp;
                    pathTail = --pathTailTmp;
                }
            }

            //part matched
            if(partMatched) {
                longestMatchedIndex = pathTailTmp;
            }
            else
                return "";
        }

        //只剩模式串包含**的匹配
        if(patternHead >= patternTail)
            return StringUtils.join(pathArr, PATH_SEPARATOR, 0, longestMatchedIndex);

        //寻找模式串中不是**的路径
        List<Integer> specialIndex = new LinkedList<>();
        for(int i = patternHead; i <= patternTail; ++i) {
            if(!StringUtils.equals(patternArr[i], ZERO_OR_MORE_DIRECTORIES_WILDCARD))
                specialIndex.add(i);
        }

        int checkFlag = 0;

        while(pathHead <= pathTail && checkFlag < specialIndex.size()) {
            if(StringMatch(patternArr[specialIndex.get(checkFlag)], pathArr[pathHead]))
                ++checkFlag;
            ++pathHead;
        }
        return checkFlag == specialIndex.size() ? StringUtils.join(pathArr, PATH_SEPARATOR, 0, longestMatchedIndex) : "";
    }


//    private static boolean doMatch(String pattern, String path, boolean fullMatch) {
//        String[] patternArr = StringUtils.split(pattern, PATH_SEPARATOR);
//        String[] pathArr = StringUtils.split(path, PATH_SEPARATOR);
//        int patternHead = 0, pathHead = 0;
//        int patternTail = patternArr.length - 1, pathTail = pathArr.length - 1;
//
//        if(!StringUtils.contains(pattern, ZERO_OR_MORE_DIRECTORIES_WILDCARD) && pathTail != patternTail)
//            return false;
//
//        //比较模式串第一个**之前的模式，如有不匹配则返回false
//        while(pathHead <= pathTail && patternHead <= patternTail) {
//            if(StringUtils.equals(patternArr[patternHead], ZERO_OR_MORE_DIRECTORIES_WILDCARD))
//                break;
//            if(!StringMatch(patternArr[patternHead++], pathArr[pathHead++]))
//                return false;
//        }
//
//        if(fullMatch) {
//            //比较模式串最后一个**之后的模式，如有不匹配则返回false
//            while (pathHead <= pathTail && patternHead <= patternTail) {
//                if (StringUtils.equals(patternArr[patternTail], ZERO_OR_MORE_DIRECTORIES_WILDCARD))
//                    break;
//                if (!StringMatch(patternArr[patternTail--], pathArr[pathTail--]))
//                    return false;
//            }
//        }
//        else {
//            boolean partMatch = false;
//            while (pathHead <= pathTail && patternHead <= patternTail) {
//                if (StringUtils.equals(patternArr[patternTail], ZERO_OR_MORE_DIRECTORIES_WILDCARD)) {
//                    partMatch = true;
//                    break;
//                }
//                if (StringMatch(patternArr[patternTail], pathArr[pathTail--]))
//                    --patternTail;
//            }
//            if(!partMatch)
//                return false;
//        }
//
//        //只剩模式串包含**的匹配
//        if(patternHead >= patternTail)
//            return true;
//
//        //寻找模式串中不是**的路径
//        List<Integer> specialIndex = new LinkedList<>();
//        for(int i = patternHead; i <= patternTail; ++i) {
//            if(!StringUtils.equals(patternArr[i], ZERO_OR_MORE_DIRECTORIES_WILDCARD))
//                specialIndex.add(i);
//        }
//
//        int checkFlag = 0;
//
//        while(pathHead <= pathTail && checkFlag < specialIndex.size()) {
//            if(StringMatch(patternArr[specialIndex.get(checkFlag)], pathArr[pathHead]))
//                ++checkFlag;
//            ++pathHead;
//        }
//        return checkFlag == specialIndex.size();
//    }

    private static boolean StringMatch(String pattern, String str) {
        int patternHead = 0, strHead = 0;
        int patternTail = pattern.length() - 1, strTail = str.length() - 1;

        //比较模式串第一个通配符之前的字符，不匹配返回false
        while(patternHead <= patternTail && strHead <= strTail) {
            if(StringUtils.equals(pattern.substring(patternHead, patternHead + 1), ZERO_OR_MORE_CHARACTERS_WILDCARD))
                break;
            if(!charMatch(pattern.charAt(patternHead++), str.charAt(strHead++)))
                return false;
        }

        //比较模式串最后一个通配符之后的字符，不匹配返回false
        while(patternHead <= patternTail && strHead <= strTail) {
            if(StringUtils.equals(pattern.substring(patternTail, patternTail + 1), ZERO_OR_MORE_CHARACTERS_WILDCARD))
                break;
            if(!charMatch(pattern.charAt(patternTail--),str.charAt(strTail--)))
                return false;
        }

        if(patternHead >= patternTail)
            return true;

        List<Integer> specialIndex = new LinkedList<>();
        for(int i = patternHead; i <= patternTail; ++i) {
            if(!StringUtils.equals(pattern.substring(i, i + 1), ZERO_OR_MORE_CHARACTERS_WILDCARD))
                specialIndex.add(i);
        }

        int checkFlag = 0;

        while(strHead <= strTail && checkFlag < specialIndex.size()) {
            if(charMatch(pattern.charAt(specialIndex.get(checkFlag)), str.charAt(strHead)))
                ++checkFlag;
            ++strHead;
        }
        return checkFlag == specialIndex.size();

    }

    private static boolean charMatch(char pattern, char c) {
        return pattern == ONE_CHARACTER_WILDCARD.charAt(0) || pattern == c;
    }

}
