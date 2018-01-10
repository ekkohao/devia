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

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-03 13:18 jerehao
 */

public class ResourceUtilsTest {

    @Test
    public void testIsURL(){
        Assert.assertFalse(ResourceUtils.isURL("aa:bb"));
        Assert.assertTrue(ResourceUtils.isURL("http://aa"));
        Assert.assertTrue(ResourceUtils.isURL("classpath:a"));
        Assert.assertTrue(ResourceUtils.isURL("file:a"));
        Assert.assertFalse(ResourceUtils.isURL("/a.jsp"));
    }

    @Test
    public void testGetURL(){
        try {
            System.out.println(ResourceUtils.getURL("http://www.jerehao.com").getPath());
            System.out.println(ResourceUtils.getURL("https://www.jerehao.com").getPath());
            System.out.println(ResourceUtils.getURL("ftp://www.jerehao.com/a.gif").getPath());
            System.out.println(ResourceUtils.getURL("classpath:log4j2.xml").getPath());
            System.out.println(ResourceUtils.getURL("E:/a/f.gif").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
