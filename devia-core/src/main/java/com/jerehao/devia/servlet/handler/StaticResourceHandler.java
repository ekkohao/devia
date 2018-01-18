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

package com.jerehao.devia.servlet.handler;

import com.jerehao.devia.application.ApplicationManager;
import com.jerehao.devia.config.ResourceMappingStorer;
import com.jerehao.devia.config.WebResourceMapping;
import com.jerehao.devia.core.util.StringUtils;
import com.jerehao.devia.logging.Logger;
import com.jerehao.devia.servlet.HandlerExecutionChain;
import com.jerehao.devia.servlet.DeviaServletContext;
import com.jerehao.devia.servlet.helper.RequestAttributeKeys;
import com.jerehao.devia.servlet.renderer.StaticResourceRenderer;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-02 20:46 jerehao
 */
public class StaticResourceHandler implements Handler {

    private static final Logger LOGGER = Logger.getLogger(StaticResourceHandler.class);

    /**
     * Default Servlet name used by Tomcat, Jetty, JBoss, and GlassFish.
     */
//    private static final String COMMON_DEFAULT_SERVLET_NAME = "default";

    //private RequestDispatcher requestDispatcher;

    public StaticResourceHandler() {
//        requestDispatcher = servletContext.getNamedDispatcher(COMMON_DEFAULT_SERVLET_NAME);
//        if(requestDispatcher == null)
//            throw new IllegalStateException("This isn't a default servlet named 'default'," +
//                    " maybe your Servlet is not used by Tomcat, Jetty, JBoss, and GlassFish");
    }

    @Override
    public void handle(DeviaServletContext servletContext, HandlerExecutionChain chain) {
        LOGGER.info("StaticResource Handler " + servletContext.getRequest().getRequestURI());

        ResourceMappingStorer mappingStorer = ApplicationManager.getResourceMappingStorer();
        String localURI = mappingStorer.getLocalURI(servletContext.getRequest().getRequestURI());

        servletContext.getRequest().setAttribute(RequestAttributeKeys.IS_STATIC_RESOURCE, StringUtils.isEmptyOrNull(localURI));
        System.out.println("localURI: " + localURI);

        if(!StringUtils.isEmptyOrNull(localURI)) {
            servletContext.getRequest().setAttribute(RequestAttributeKeys.STATIC_LOCATION_URI, localURI);
            servletContext.setRenderer(new StaticResourceRenderer());
            return;
        }
        chain.next();
    }
}
