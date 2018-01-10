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

import com.jerehao.devia.core.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-03 17:56 jerehao
 */
public class FileSystemResource extends AbstractResource implements WritableResource {

    private final File file;

    private final String path;

    public FileSystemResource(File file) {
        if(file == null)
            throw new NullPointerException("File cannot be null");
        this.file = file;
        this.path = ResourceUtils.formatPathToUnix(file.getPath());
    }

    public FileSystemResource(String path) {
        if(path == null)
            throw new NullPointerException("Path cannot be null");
        this.file = new File(path);
        this.path = ResourceUtils.formatPathToUnix(path);
    }

    @Override
    public File getFile() {
        return file;
    }

    public final String getPath() {
        return path;
    }

    @Override
    public boolean isExist() {
        return this.file.exists();
    }

    @Override
    public boolean isReadable() {
        return (this.file.canRead() && !this.file.isDirectory());
    }

    @Override
    public boolean isWritable() {
        return (this.file.canWrite() && !this.file.isDirectory());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(this.file.toPath());
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return Files.newOutputStream(this.file.toPath());
    }

    @Override
    public URL getURL() throws IOException {
        return this.file.toURI().toURL();
    }

    @Override
    public URI getURI() throws IOException {
        return this.file.toURI();
    }

    @Override
    public String getFilename() {
        return this.file.getName();
    }

    @Override
    public String getDescription() {
        return "file '" + this.file.getAbsolutePath() + "'";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this || (obj instanceof FileSystemResource && this.path.equals(((FileSystemResource) obj).path)));
    }

    /**
     * This implementation returns the hash code of the underlying File reference.
     */
    @Override
    public int hashCode() {
        return this.path.hashCode();
    }
}
