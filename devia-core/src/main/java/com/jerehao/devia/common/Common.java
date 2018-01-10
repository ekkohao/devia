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

package com.jerehao.devia.common;

import com.jerehao.devia.logging.Logger;
import com.jerehao.devia.servlet.listener.ServletListener;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-02 18:04 jerehao
 */
public final class Common {

    private static final Logger LOGGER = Logger.getLogger(Common.class);

    private Common(){}

    public static File getWebAppFile(final String path) {
        final ServletContext servletContext = ServletListener.getServletContext();
        File file;
        try {
            final URL resource = servletContext.getResource(path);
            if(null != resource) {
                file = FileUtils.toFile(resource);
                if (null == file) {
                    final File tempDir = (File) servletContext.getAttribute(ServletContext.TEMPDIR);
                    file = new File(tempDir.getPath() + path);
                    FileUtils.copyURLToFile(resource, file);
                    file.deleteOnExit();
                }
                return file;
            }
        } catch (IOException e) {
            LOGGER.error("cannot read file with path='" + path + "'", e);
        }
        return null;
    }


}
