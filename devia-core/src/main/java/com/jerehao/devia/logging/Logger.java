/*
 * Copyright (c) 2017, jerehao.com
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

package com.jerehao.devia.logging;

import org.apache.logging.log4j.LogManager;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2017-12-22 11:23 jerehao
 */
public final class Logger {

    private org.apache.logging.log4j.Logger logger;

    private Logger(final String className) {
        logger = LogManager.getLogger(className);
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz.getName());
    }

    public static Logger getLogger(final String className) {
        return new Logger(className);
    }

    public void error(final String msg) {
        logger.error(msg);
    }

    public void error(final String msg, Object... args) {
        logger.error(msg, args);
    }

    public void error(final String msg, Throwable t) {
        logger.error(msg, t);
    }

    public void warn(final String msg) {
        logger.warn(msg);
    }

    public void warn(final String msg, Object... args) {
        logger.warn(msg, args);
    }

    public void warn(final String msg, Throwable t) {
        logger.warn(msg, t);
    }

    public void info(final String msg) {
        logger.info(msg);
    }

    public void info(final String msg, Object... args) {
        logger.info(msg, args);
    }

    public void info(final String msg, Throwable t) {
        logger.info(msg, t);
    }

    public void debug(final String msg) {
        logger.debug(msg);
    }

    public void debug(final String msg, Object... args) {
        logger.debug(msg, args);
    }

    public void debug(final String msg, Throwable t) {
        logger.debug(msg, t);
    }

    public void trace(final String msg) {
        logger.trace(msg);
    }

    public void trace(final String msg, Object... args) {
        logger.trace(msg, args);
    }

    public void trace(final String msg, Throwable t) {
        logger.trace(msg, t);
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }
}
