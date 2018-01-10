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


import com.jerehao.devia.servlet.helper.StaticResource;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-03 11:10 jerehao
 */
public class ResourceUtils {

    public static final String FOLDER_SEPARATOR = "/";

    public static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    public static final String JAR_URL_SEPARATOR = "!/";

    /** Pseudo URL prefix for loading from the class path: "classpath:" */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /** URL prefix for loading from the file system: "file:" */
    public static final String FILE_URL_PREFIX = "file:";

    /** URL protocol for a file in the file system: "file" */
    public static final String FILE_URL_PROTOCOL = "file";

    public static final String JAR_URL_PROTOCOL = "jar";

    public static final String WSJAR_URL_PROTOCOL = "wsjar";

    public static final String ZIP_URL_PROTOCOL = "zip";

    public static final String CODE_SOURCE_URL_PROTOCOL = "code-source";


    private ResourceUtils(){}

    /**
     * recognize the location is a url address if protocol contains in {@code http,https,file,ftp,classpath}
     * @param resourceLocation the resource location
     * @return {@code true} if {@code resourceLocation} start with the special protocol
     */
    public static boolean isURL(final String resourceLocation) {
        if(null == resourceLocation)
            return false;
        if(resourceLocation.startsWith(CLASSPATH_URL_PREFIX))
            return true;
        try {
            final URL url = new URL(resourceLocation);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    public static File getFile(URL url) throws IOException {
        if(url == null)
            return null;
        if(!FILE_URL_PROTOCOL.equals(url.getProtocol()))
            throw new IOException("resource url '" + url + "' isn't a file url, it cannot be resolved a file");
        try {
            return new File(toURI(url).getSchemeSpecificPart());
        } catch (URISyntaxException e) {
            return new File(url.getFile());
        }
    }

    /**
     * 获取指定文件路径的URL
     * @param resourceLocation
     * @return
     * @throws FileNotFoundException
     */
    public static URL getURL(String resourceLocation) throws FileNotFoundException {
        if(null == resourceLocation)
            return null;
        if(resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
            URL url = (classLoader != null) ? classLoader.getResource(path) : ClassLoader.getSystemResource(path);
            if(url == null)
                throw new FileNotFoundException("classpath resource '" + path + "' cannot be found maybe it doesn't exists.");
            else
                return url;
        }
        try {
            return new URL(resourceLocation);
        } catch (MalformedURLException e) {
            //if resourceLocation is a file path
            try {
                return new File(resourceLocation).toURI().toURL();
            } catch (MalformedURLException e2) {
                throw new FileNotFoundException("Resource '" + resourceLocation + "' is neither a URL nor a file path.");
            }
        }
    }

    public static URI toURI(URL resourceURL) throws URISyntaxException {
        if(resourceURL == null)
            return null;
        return new URI(StringUtils.replace(resourceURL.toString()," ", "%20"));
    }

    public static String getFilename(String path) {
        if(path == null)
            return null;
        int lastFoldSeparator = path.lastIndexOf(FOLDER_SEPARATOR);
        return (lastFoldSeparator < 0) ? path : path.substring(lastFoldSeparator + 1);
    }

    public static String formatPathToUnix(String path) {
        return StringUtils.replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);
    }

    public static boolean isJarURL(URL url) {
        final String protocol = url.getProtocol();
        final String path = url.getPath();
        return (StringUtils.contains(path, JAR_URL_SEPARATOR)
                && StringUtils.equalsAny(protocol, JAR_URL_PROTOCOL,
                                                    WSJAR_URL_PROTOCOL,
                                                    ZIP_URL_PROTOCOL,
                                                    CODE_SOURCE_URL_PROTOCOL));
    }


}
