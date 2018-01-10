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

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-10 9:28 jerehao
 */
public final class NumericUtils {

    /**
     * if all numbers are negative, return -1
     * @param numbers
     * @return
     */
    public static int getMinPositive(int... numbers) {
        int ret = -1;
        for (int number : numbers) {
            if(number < 0)
                continue;
            if(ret < 0 || number < ret)
                ret = number;
        }
        return ret;
    }

    private NumericUtils() {}
}
