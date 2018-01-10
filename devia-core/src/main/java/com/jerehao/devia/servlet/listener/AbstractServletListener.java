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

package com.jerehao.devia.servlet.listener;

import com.jerehao.devia.logging.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-03 19:38 jerehao
 */
public abstract class AbstractServletListener implements ServletRequestListener, ServletContextListener, HttpSessionListener {


    private static final Logger LOGGER = Logger.getLogger(AbstractServletListener.class);

    private static ServletContext servletContext;

    public static ServletContext getServletContext() {
        if (null == servletContext) {
            throw new IllegalStateException("The servlet context hasn't initialized!");
        }

        return servletContext;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContext = servletContextEvent.getServletContext();
        LOGGER.info("contextInitialized " + servletContextEvent.getServletContext().toString());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOGGER.info("contextDestroyed");
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        LOGGER.info("requestInitialized");
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        LOGGER.info("requestDestroyed");
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        LOGGER.info("sessionCreated");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        LOGGER.info("sessionDestroyed");
    }
}
