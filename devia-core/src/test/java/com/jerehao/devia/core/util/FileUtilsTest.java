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

import com.jerehao.devia.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-10 15:47 jerehao
 */
public class FileUtilsTest {

    private static final Logger LOGGER = Logger.getLogger(FileUtilsTest.class);

    @Test
    public void testListAllFiles() {
        String path = "D:/__ProgramFilesX86/Apache-Tomcat-8.0.46/webapps/ROOT/";
        for (File file : FileUtils.listAllFile(path)) {
            //Assert.assertEquals(file.getPath(), "");
            //LOGGER.info("File - [" + file.getPath() +"]");
        }
    }
}
