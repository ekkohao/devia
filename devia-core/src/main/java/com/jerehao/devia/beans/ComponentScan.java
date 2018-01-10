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

package com.jerehao.devia.beans;

import com.jerehao.devia.core.util.ClassUtils;
import com.jerehao.devia.core.util.NumericUtils;
import com.jerehao.devia.core.util.ResourceUtils;
import com.jerehao.devia.logging.Logger;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.jerehao.devia.core.util.AntPathMather.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-09 20:01 jerehao
 */
public class ComponentScan {

    private static final Logger LOGGER = Logger.getLogger(ComponentScan.class);

    private static final String REGEX_ZERO_OR_MORE_CHILD_PACKAGE = "\\.\\.";

    private static final String REGEX_PACKAGE_SEPARATOR = "\\.";

    private static final String CLASS_FILE_SUFFIX = ".class";

    private String scanPaths;

    private Set<Class<?>> classes;

    private boolean scanned;

    public ComponentScan(String scanPaths) {
        this.scanPaths = scanPaths;
        classes = new HashSet<>();
        scanned = false;
    }

    public Set<Class<?>> getClasses() {
        if(!scanned) {
            scanAllPath();
            scanned = true;
        }

        return this.classes;
    }

    private void scanAllPath() {
        Set<URL> resources = new LinkedHashSet<>();
        if(scanPaths == null || scanPaths.isEmpty())
            resources.addAll(doScanPathAndGetResources("*"));
        for (String path : StringUtils.split(scanPaths,','))
            resources.addAll(doScanPathAndGetResources(path));
    }

    private Set<URL> doScanPathAndGetResources(String path) {
        Set<URL> resources = new LinkedHashSet<>();

        path = formatToDirectoryPath(path);
        final String baseDir = getBaseDirectory(path);
        final String subPatten = path.substring(baseDir.length());
        final Set<URL> baseResources = getDirectoryResources(baseDir);

        LOGGER.info("Scan base dir: " + baseDir);
        LOGGER.info("Scan patten: " + subPatten);

        for(URL url : baseResources) {
            LOGGER.info("Find base resource: " + url.getProtocol() + " | " +url.getPath());
            if(ResourceUtils.isJarURL(url))
                resources.addAll(getJarMatchedResources(url, subPatten));
        }

        //遍历根目录下所以资源筛选出符合条件的
        //分析文件格式
        //maybe借助javassis找到具有指定注解的类

        return resources;
    }

    private Set<URL> getJarMatchedResources(URL url, String subPatten) {
        Set<URL> resources = new LinkedHashSet<>();



        return resources;
    }

    private String formatPathToUse(String path) {
        if(StringUtils.isEmpty(path))
            return "";
        path = StringUtils.replaceAll(path, REGEX_ZERO_OR_MORE_CHILD_PACKAGE,
                PATH_SEPARATOR + ZERO_OR_MORE_DIRECTORIES_WILDCARD + PATH_SEPARATOR);
        path = StringUtils.replaceAll(path, REGEX_PACKAGE_SEPARATOR, PATH_SEPARATOR);

        String suffix = PATH_SEPARATOR + ZERO_OR_MORE_CHARACTERS_WILDCARD + CLASS_FILE_SUFFIX;
        if(path.endsWith(ZERO_OR_MORE_DIRECTORIES_WILDCARD))
            path = path + suffix;
        else if(path.endsWith(ZERO_OR_MORE_CHARACTERS_WILDCARD) || path.endsWith(ONE_CHARACTER_WILDCARD))
            path = path + CLASS_FILE_SUFFIX;
        else if(path.endsWith(PATH_SEPARATOR))
            path = path + ZERO_OR_MORE_DIRECTORIES_WILDCARD + suffix;
        else
            path = path + PATH_SEPARATOR + ZERO_OR_MORE_DIRECTORIES_WILDCARD + suffix;

        return path;
    }

    private String getBaseDirectory(final String path) {
        if(StringUtils.isEmpty(path))
            return "";

        int zeroOrMoreDirStartIndex = path.indexOf(ZERO_OR_MORE_DIRECTORIES_WILDCARD);
        int zeroOrMoreCharStartIndex = path.indexOf(ZERO_OR_MORE_CHARACTERS_WILDCARD);
        int oneCharStartIndex = path.indexOf(ONE_CHARACTER_WILDCARD);

        int firstPattenCharIndex = NumericUtils.getMinPositive(zeroOrMoreCharStartIndex,
                zeroOrMoreDirStartIndex, oneCharStartIndex);
        String base;
        if(firstPattenCharIndex < 0)
            base = path;
        else
            base = path.substring(0,
                path.substring(0, firstPattenCharIndex).lastIndexOf(PATH_SEPARATOR));
        return StringUtils.isEmpty(base) ? "" : base;
    }

    private Set<URL> getDirectoryResources(String dirPath) {

        final Set<URL> ret = new LinkedHashSet<>();

        if(dirPath.startsWith("/"))
            dirPath = dirPath.substring(1);

        try {
            Enumeration<URL> resources = ClassUtils.getDefaultClassLoader().getResources(dirPath);
            while (resources.hasMoreElements()) {
                ret.add(resources.nextElement());
            }
        } catch (IOException e) {
            LOGGER.error("Read Directory [" + dirPath + "] resources error.", e);
        }
        return ret;
    }
}
