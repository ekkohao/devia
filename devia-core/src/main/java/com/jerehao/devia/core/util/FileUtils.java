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

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-10 15:31 jerehao
 */
public final class FileUtils {

    public static Set<File> listAllFile(String path) {
        return listAllFile(path, null);
    }

    public static Set<File> listAllFile(File file) {
        return listAllFile(file, null);
    }

    public static Set<File> listAllFile(String path , FileFilter filter) {
        File file = ResourceUtils.getFile(path);
        return listAllFile(file, filter);
    }
    public static Set<File> listAllFile(File file , FileFilter filter) {
        Set<File> files = new LinkedHashSet<>();

        List<File> dirs = new LinkedList<>();
        File[] currentDirFiles;

        dirs.add(file);

        while (!dirs.isEmpty()) {
            File currentDir = dirs.remove(0);
            if(currentDir == null || !currentDir.exists())
                continue;
            currentDirFiles = currentDir.listFiles();

            //System.out.println(Arrays.toString(currentDirFiles));

            if (currentDirFiles != null) {
                for(File f : currentDirFiles) {
                    if(f.isDirectory())
                        dirs.add(f);
                    if(filter == null || filter.accept(f))
                        files.add(f);
                }
            }
        }
        return files;
    }
    private FileUtils() {}
}
