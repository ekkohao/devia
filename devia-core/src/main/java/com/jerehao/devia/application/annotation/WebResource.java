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

package com.jerehao.devia.application.annotation;

import java.lang.annotation.*;

/**
 * if locate to "/resource/css"
 * "/css/**" means "/css/a/b/c/fileFilter.txt" -> "/resource/css/fileFilter.txt"
 * "/css" means "/css/a/b/c/fileFilter.txt" -> "/resource/css/a/b/c/fileFilter.txt"
 * "/css/**\/b means "/css/a/b/c/fileFilter.txt" -> "/resource/css/c/fileFilter.txt"
 *
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-16 14:10 jerehao
 */
@Repeatable(WebResources.class)
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebResource {

    /**
     * value is the uri pattern need to mapping without fileFilter
     * use {@code "-"} at the patten beginnings, represent that this pattern are exclude
     * use {@code ","} to separator different pattern
     * eg.
     *      value = "/css/**, -/css/font/"
     *
     * uri pattern only contains fold path
     *
     * @return folder uri pattern
     */
    String value();

    /**
     * mapping location
     * @return mapping location
     */
    String locationTo() default "";

    /**
     * fileFilter to mapping
     * eg.
     *      "/*.css, /*.js, /*.png"
     *
     * also you can use {@code "-"} to exclude some fileFilter
     * eg.
     *      "/*, -/*.txt"
     *
     * @return fileFilter name pattern
     */
    String[] fileFilter() default "*";

}
