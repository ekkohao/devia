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

package com.jerehao.devia.config;

import com.jerehao.devia.bean.exception.UnSupportedURLProtocolException;
import com.jerehao.devia.core.util.*;
import com.jerehao.devia.logging.Logger;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;
import org.apache.commons.lang3.StringUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

    private static final char SCAN_PATH_SEPARATOR_CHAR = ',';

    private final List<Class<?>[]> needProcessAnnotations = new LinkedList<>();

    private final List<AnnotationProcessor> annotationProcessors;

    private final String[] scanPaths;

    //private Set<Class<?>> classes;

    private boolean scanned;

    private ComponentScan(String[] scanPaths, List<AnnotationProcessor> annotationProcessors) {
        this.scanPaths = scanPaths;
        this.annotationProcessors = annotationProcessors;

        for(AnnotationProcessor annotationProcessor : annotationProcessors) {
            Class<?> classes[] = new Class[annotationProcessor.getTargetAnnotations().size()];
            needProcessAnnotations.add(annotationProcessor.getTargetAnnotations().toArray(classes));
        }

        scanned = false;
    }

    public static void doScanAndProcess(String[] scanPaths, List<AnnotationProcessor> annotationProcessors) {
        ComponentScan sc = new ComponentScan(scanPaths, annotationProcessors);
        sc.scan();
    }

    public void scan() {
        if (!scanned) {
            scanAllPath();
            scanned = true;
        }
    }

    private void scanAllPath() {
        Set<URL> resources = new LinkedHashSet<>();

        if (scanPaths == null || scanPaths.length < 1)
            resources.addAll(doScanPathAndGetResources(Configuration.DEFAULT_AUTO_SCAN_PACKAGE));
        else {
            for (String path : scanPaths)
                resources.addAll(doScanPathAndGetResources(path));

            scanResourceAndProcess(resources);
        }
    }

    private void scanResourceAndProcess(Set<URL> resources) {
        for (URL url : resources) {
            ClassFile classFile;

            try {
                classFile = new ClassFile(new DataInputStream(url.openStream()));
            } catch (IOException e) {
                LOGGER.error("Can not open file [{0}]", url.getFile());
                continue;
            }

            AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) classFile.getAttribute(AnnotationsAttribute.visibleTag);

            if (annotationsAttribute == null || annotationsAttribute.numAnnotations() < 1)
                continue;

            Annotation[] annotations = annotationsAttribute.getAnnotations();

            for( int i = 0, len = annotationProcessors.size(); i < len; ++i) {
                if (anyAnnotationMatched(annotations, needProcessAnnotations.get(i))) {
                    try {
                        annotationProcessors.get(i).process(ClassUtils.getDefaultClassLoader().loadClass(classFile.getName()));
                    } catch (ClassNotFoundException e) {
                        LOGGER.error("Cannot load class [{0}]", classFile.getName());
                    }
                }
            }
        }
    }

    private boolean anyAnnotationMatched(Annotation[] annotations, Class<?>[] classes) {
        for(Annotation annotation : annotations) {
            for(Class<?> clazz : classes) {
                if(StringUtils.equals(annotation.getTypeName(), clazz.getName()))
                    return true;
            }
        }

        return false;
    }

    private Set<URL> doScanPathAndGetResources(String path) {
        Set<URL> resources = new LinkedHashSet<>();

        path = formatPathToUse(path);
        final String baseDir = getBaseDirectory(path);
        final String subPatten = path.substring(baseDir.length());
        final Set<URL> baseResources = getDirectoryResources(baseDir);

        LOGGER.info("Scan base dir: " + baseDir);
        LOGGER.info("Scan patten: " + subPatten);

        for (URL url : baseResources) {
            LOGGER.info("Find base resource: " + url.getProtocol() + " | " + url.getPath());
            resources.addAll(getMatchedResources(url, subPatten));
        }

        return resources;
    }

    private Set<URL> getMatchedResources(URL url, String subPatten) {
        Set<URL> resources = new LinkedHashSet<>();
        if (ResourceUtils.isJarURL(url))
            resources.addAll(getJarFileMatchedResources(url, subPatten));
        else if (ResourceUtils.isVFSURL(url))
            resources.addAll(getVFSSystemMatchedResources(url, subPatten));
        else if (ResourceUtils.isFileSystemURL(url))
            resources.addAll(getFileSystemMatchedResources(url, subPatten));
        else
            throw new UnSupportedURLProtocolException(
                    "URL protocol [" + url.getProtocol() + "] not supported, please contact your service provider");
        return resources;
    }

    private Set<URL> getJarFileMatchedResources(URL url, String subPatten) {
        Set<URL> resources = new LinkedHashSet<>();

        JarFile jarFile = null;
        boolean closeJarFileFlag = false;
        String basePrefix = "";
        String patten;

        String uriPath = url.getPath();
        int jarSepIndex = uriPath.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
        if (jarSepIndex > -1)
            basePrefix = uriPath.substring(jarSepIndex + ResourceUtils.JAR_URL_SEPARATOR.length());
        if (!StringUtils.isEmpty(basePrefix) && !basePrefix.endsWith(PATH_SEPARATOR))
            basePrefix += PATH_SEPARATOR;
        if (subPatten.startsWith(PATH_SEPARATOR))
            subPatten = subPatten.substring(PATH_SEPARATOR.length());
        patten = basePrefix + subPatten;
        try {
            URLConnection urlCon = url.openConnection();
            if (urlCon instanceof JarURLConnection) {
                jarFile = ((JarURLConnection) urlCon).getJarFile();
            }
        } catch (IOException e) {
            String msg = "Open url connection " + url.getPath() + "error.";
            LOGGER.error(msg, e);
        }

        //jar connect failed
        if (jarFile == null) {
            try {
                jarFile = getJarFileFromURL(url);
                closeJarFileFlag = true;
            } catch (IOException e) {
                LOGGER.error("Cannot read jar fileFilter on path [" + url.getPath() + "]", e);
                return resources;
            }
        }

        if (jarFile == null) {
            LOGGER.error("Cannot read jar fileFilter on path [" + url.getPath() + "]");
            return resources;
        }

        final Enumeration<JarEntry> entries = jarFile.entries();
        final int basePrefixLen = basePrefix.length();
        while (entries.hasMoreElements()) {
            String entryPath = entries.nextElement().getName();
            if (AntPathMather.Match(patten, entryPath)) {
                try {
                    resources.add(new URL(url, entryPath.substring(basePrefixLen)));
                } catch (MalformedURLException e) {
                    LOGGER.error("New URL ERROR", e);
                }
                LOGGER.info("Find jar fileFilter entry [" + entryPath + "]");
            }
        }

        if (closeJarFileFlag) {
            try {
                jarFile.close();
            } catch (IOException e) {
                LOGGER.error("Close jar fileFilter [" + jarFile.getName() + "] error", e);
            }
        }

        return resources;
    }

    private Set<URL> getVFSSystemMatchedResources(URL url, String subPatten) {
        throw new UnSupportedURLProtocolException("VFS not supported");
    }

    private Set<URL> getFileSystemMatchedResources(URL url, String subPatten) {

        Set<URL> resources = new LinkedHashSet<>();
        File root = ResourceUtils.getFile(url);

        String _patten = root.getAbsolutePath().replace(File.separator, PATH_SEPARATOR);
        if(!subPatten.startsWith(PATH_SEPARATOR))
            subPatten = PATH_SEPARATOR + subPatten;
        if(_patten.endsWith(PATH_SEPARATOR))
            _patten = _patten.substring(0, 0 - PATH_SEPARATOR.length()) + subPatten;
        else
            _patten += subPatten;
        final String patten = _patten;

        Set<File> files  = FileUtils.listAllFile(url.getPath(), file ->
                !file.isDirectory() && AntPathMather.Match(patten, file.getAbsolutePath().replace(File.separator, PATH_SEPARATOR)));

        for(File file : files)
            try {
                resources.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                LOGGER.error("Parse fileFilter ["+ file.getPath() +"] to url error.", e);
            }

        return resources;
    }

    private JarFile getJarFileFromURL(URL url) throws IOException {
        try {
            return getJarFileFromPath(url.toURI().getSchemeSpecificPart());
        } catch (URISyntaxException e) {
            if (url.getPath().startsWith(ResourceUtils.FILE_URL_PREFIX))
                return getJarFileFromPath(url.getPath().substring(ResourceUtils.FILE_URL_PREFIX.length()));
            else
                return getJarFileFromPath(url.getPath());
        }
    }

    /**
     * path must be has no protocol prefix like "fileFilter:"
     *
     * @param path
     * @return
     * @throws IOException
     */
    private JarFile getJarFileFromPath(String path) throws IOException {
        if (StringUtils.isEmpty(path))
            return null;
        int sep = path.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
        if (sep == -1)
            return new JarFile(path);
        else
            return new JarFile(path.substring(0, sep));
    }

    private String formatPathToUse(String path) {
        if (StringUtils.isEmpty(path))
            return "";
        path = StringUtils.replaceAll(path, REGEX_ZERO_OR_MORE_CHILD_PACKAGE,
                PATH_SEPARATOR + ZERO_OR_MORE_DIRECTORIES_WILDCARD + PATH_SEPARATOR);
        path = StringUtils.replaceAll(path, REGEX_PACKAGE_SEPARATOR, PATH_SEPARATOR);

        String suffix = PATH_SEPARATOR + ZERO_OR_MORE_CHARACTERS_WILDCARD + CLASS_FILE_SUFFIX;
        if (path.endsWith(ZERO_OR_MORE_DIRECTORIES_WILDCARD))
            path = path + suffix;
        else if (path.endsWith(ZERO_OR_MORE_CHARACTERS_WILDCARD) || path.endsWith(ONE_CHARACTER_WILDCARD))
            path = path + CLASS_FILE_SUFFIX;
        else if (path.endsWith(PATH_SEPARATOR))
            path = path + ZERO_OR_MORE_DIRECTORIES_WILDCARD + suffix;
        else if(!path.endsWith(CLASS_FILE_SUFFIX))
            path = path + PATH_SEPARATOR + ZERO_OR_MORE_DIRECTORIES_WILDCARD + suffix;

        return path;
    }

    private String getBaseDirectory(final String path) {
        if (StringUtils.isEmpty(path))
            return "";

        int zeroOrMoreDirStartIndex = path.indexOf(ZERO_OR_MORE_DIRECTORIES_WILDCARD);
        int zeroOrMoreCharStartIndex = path.indexOf(ZERO_OR_MORE_CHARACTERS_WILDCARD);
        int oneCharStartIndex = path.indexOf(ONE_CHARACTER_WILDCARD);

        int firstPattenCharIndex = NumericUtils.getMinPositive(zeroOrMoreCharStartIndex,
                zeroOrMoreDirStartIndex, oneCharStartIndex);
        String base;
        if (firstPattenCharIndex < 0)
            base = path;
        else
            base = path.substring(0,
                    path.substring(0, firstPattenCharIndex).lastIndexOf(PATH_SEPARATOR));
        return StringUtils.isEmpty(base) ? "" : base;
    }

    private Set<URL> getDirectoryResources(String dirPath) {

        final Set<URL> ret = new LinkedHashSet<>();

        if (dirPath.startsWith("/"))
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
