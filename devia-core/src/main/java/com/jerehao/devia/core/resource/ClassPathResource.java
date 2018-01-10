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

package com.jerehao.devia.core.resource;

import com.jerehao.devia.core.util.ClassUtils;
import com.jerehao.devia.core.util.ResourceUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-03 10:45 jerehao
 */
public class ClassPathResource extends AbstractResource {

    private final String path;

    private ClassLoader classLoader;

    private Class<?> clazz;

    public ClassPathResource(String path) {
        this(path,null, null);
    }

    public ClassPathResource(String path, Class<?> clazz) {
        this(path,null, clazz);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this(path,classLoader, null);
    }

    public ClassPathResource(String _path, ClassLoader classLoader, Class<?> clazz) {
        _path = StringUtils.trim(_path);
        this.path = _path.startsWith("/") ? _path.substring(1) : _path;
        this.classLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
        this.clazz = clazz;
    }

    public final ClassLoader getClassLoader() {
        return (this.clazz == null) ? this.classLoader : this.clazz.getClassLoader();
    }

    @Override
    public URL getURL() throws IOException {
        URL url = null;
        ClassLoader cl = getClassLoader();
        url = (cl != null) ? cl.getResource(path) : ClassLoader.getSystemResource(path);
        if(url == null)
            throw new FileNotFoundException(getDescription() + " cannot be founded.");
        return url;
    }

    @Override
    public File getFile() throws IOException {
        return ResourceUtils.getFile(this.getURL());
    }


    @Override
    public String getFilename() {
        return ResourceUtils.getFilename(this.path);
    }

    @Override
    public String getDescription() {
        return "classpath resource '" + this.path + "'";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = null;
        ClassLoader cl = getClassLoader();
        inputStream = (cl != null) ? cl.getResourceAsStream(path) : ClassLoader.getSystemResourceAsStream(path);
        if(inputStream == null)
            throw new FileNotFoundException(getDescription() + " cannot be founded.");
        return inputStream;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassPathResource that = (ClassPathResource) o;
        return path != null ? path.equals(that.path) : that.path == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }
}
