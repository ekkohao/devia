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

import org.apache.commons.lang3.StringUtils;

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
    public static boolean Match(String patten, String path) {
        return doMatch(patten, path, true);
    }

    //TODO test 部分匹配
    public static boolean Match(String patten, String path, boolean fullMatch) {
        return doMatch(patten, path, fullMatch);
    }


    private static boolean doMatch(String patten, String path, boolean fullMatch) {
        String[] pattenArr = StringUtils.split(patten, PATH_SEPARATOR);
        String[] pathArr = StringUtils.split(path, PATH_SEPARATOR);
        int pattenHead = 0, pathHead = 0;
        int pattenTail = pattenArr.length - 1, pathTail = pathArr.length - 1;

        if(!StringUtils.contains(patten, ZERO_OR_MORE_DIRECTORIES_WILDCARD) && pathTail != pattenTail)
            return false;

        //比较模式串第一个**之前的模式，如有不匹配则返回false
        while(pathHead <= pathTail && pattenHead <= pattenTail) {
            if(StringUtils.equals(pattenArr[pattenHead], ZERO_OR_MORE_DIRECTORIES_WILDCARD))
                break;
            if(!StringMatch(pattenArr[pattenHead++], pathArr[pathHead++]))
                return false;
        }

        if(fullMatch) {
            //比较模式串最后一个**之后的模式，如有不匹配则返回false
            while (pathHead <= pathTail && pattenHead <= pattenTail) {
                if (StringUtils.equals(pattenArr[pattenTail], ZERO_OR_MORE_DIRECTORIES_WILDCARD))
                    break;
                if (!StringMatch(pattenArr[pattenTail--], pathArr[pathTail--]))
                    return false;
            }
        }
        else {
            boolean partMatch = false;
            while (pathHead <= pathTail && pattenHead <= pattenTail) {
                if (StringUtils.equals(pattenArr[pattenTail], ZERO_OR_MORE_DIRECTORIES_WILDCARD)) {
                    partMatch = true;
                    break;
                }
                if (StringMatch(pattenArr[pattenTail], pathArr[pathTail--]))
                    --pattenTail;
            }
            if(!partMatch)
                return false;
        }

        //只剩模式串包含**的匹配
        if(pattenHead >= pattenTail)
            return true;

        //寻找模式串中不是**的路径
        List<Integer> specialIndex = new LinkedList<>();
        for(int i = pattenHead; i <= pattenTail; ++i) {
            if(!StringUtils.equals(pattenArr[i], ZERO_OR_MORE_DIRECTORIES_WILDCARD))
                specialIndex.add(i);
        }

        int checkFlag = 0;

        while(pathHead <= pathTail && checkFlag < specialIndex.size()) {
            if(StringMatch(pattenArr[specialIndex.get(checkFlag)], pathArr[pathHead]))
                ++checkFlag;
            ++pathHead;
        }
        return checkFlag == specialIndex.size();
    }

    private static boolean StringMatch(String patten, String str) {
        int pattenHead = 0, strHead = 0;
        int pattenTail = patten.length() - 1, strTail = str.length() - 1;

        //比较模式串第一个通配符之前的字符，不匹配返回false
        while(pattenHead <= pattenTail && strHead <= strTail) {
            if(StringUtils.equals(patten.substring(pattenHead, pattenHead + 1), ZERO_OR_MORE_CHARACTERS_WILDCARD))
                break;
            if(!charMatch(patten.charAt(pattenHead++), str.charAt(strHead++)))
                return false;
        }

        //比较模式串最后一个通配符之后的字符，不匹配返回false
        while(pattenHead <= pattenTail && strHead <= strTail) {
            if(StringUtils.equals(patten.substring(pattenTail, pattenTail + 1), ZERO_OR_MORE_CHARACTERS_WILDCARD))
                break;
            if(!charMatch(patten.charAt(pattenTail--),str.charAt(strTail--)))
                return false;
        }

        if(pattenHead >= pattenTail)
            return true;

        List<Integer> specialIndex = new LinkedList<>();
        for(int i = pattenHead; i <= pattenTail; ++i) {
            if(!StringUtils.equals(patten.substring(i, i + 1), ZERO_OR_MORE_CHARACTERS_WILDCARD))
                specialIndex.add(i);
        }

        int checkFlag = 0;

        while(strHead <= strTail && checkFlag < specialIndex.size()) {
            if(charMatch(patten.charAt(specialIndex.get(checkFlag)), str.charAt(strHead)))
                ++checkFlag;
            ++strHead;
        }
        return checkFlag == specialIndex.size();

    }

    private static boolean charMatch(char patten, char c) {
        return patten == ONE_CHARACTER_WILDCARD.charAt(0) || patten == c;
    }

}
