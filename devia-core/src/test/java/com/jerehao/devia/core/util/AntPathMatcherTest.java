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

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-05 20:34 jerehao
 */
public class AntPathMatcherTest {

    @Test
    public void TestMatch(){
        Assert.assertTrue(AntPathMather.Match("/css/**","/css/main.css"));
        Assert.assertTrue(AntPathMather.Match("/a/**/c","/a/b/c"));
        Assert.assertTrue(AntPathMather.Match("/a/**/s/?/?/c?er","/a/b/a/s/d/f/cder"));
    }

    @Test
    public void TestGetPartialMatched() {
        Assert.assertEquals(AntPathMather.getPartialMatched("/a/b","/a/b/c"), "/a/b");
        Assert.assertEquals(AntPathMather.getPartialMatched("/a/**/b","/a/b"), "/a/b");
        Assert.assertEquals(AntPathMather.getPartialMatched("/a/b/**","/a/b/c/d"), "/a/b/c/d");
        Assert.assertEquals(AntPathMather.getPartialMatched("/a/b/**/d","/a/b/c/d/e/e/d/e"), "/a/b/c/d/e/e/d");
        Assert.assertEquals(AntPathMather.getPartialMatched("/a/**/b/**/c","/a/b/c/b/c/a/b/c/a"), "/a/b/c/b/c/a/b/c");
        Assert.assertEquals(AntPathMather.getPartialMatched("/**/b","/a/b/c"), "/a/b");
        Assert.assertEquals(AntPathMather.getPartialMatched("/a/b*/c","/a/b/c"), "/a/b/c");
        Assert.assertEquals(AntPathMather.getPartialMatched("/a/*","/a/b/c"), "/a/b");
        Assert.assertEquals(AntPathMather.getPartialMatched("/a/**/*","/a/b/c"), "/a/b/c");

    }
}
