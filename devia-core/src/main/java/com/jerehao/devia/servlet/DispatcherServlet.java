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

package com.jerehao.devia.servlet;

import com.jerehao.devia.application.ApplicationManager;
import com.jerehao.devia.core.resource.FileSystemResource;
import com.jerehao.devia.core.resource.Resource;
import com.jerehao.devia.logging.Logger;
import com.jerehao.devia.servlet.handler.RequestDispatcherHandler;
import com.jerehao.devia.servlet.handler.StaticResourceHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2017-12-22 11:07 jerehao
 */
public final class DispatcherServlet extends HttpServlet {

    private static final String DEFAULT_SERVLET_CONFIG_FILENAME_SUFFIX = "-servlet.xml";

    public static final String XML_NAMESPACE_MVC_URI = "http://www.jerehao.com/mvc/";

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DispatcherServlet.class);

    /**
     * set context config location
     * if this isn't set, the location is {@code /WEB-INF/$servlet-name$-servlet.xml}
     */
    private String contextConfigLocation;

    /**
     * base path to create beans
     * [..]多级包路径 = [.**.]
     * [*]零个或多个字符
     */
    private String scanPaths;

    /**
     * 每次浏览器请求会执行service方法
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HandlerExecutionChain chain = new HandlerExecutionChain();
        final DeviaServletContext servletContext = new DeviaServletContext(req, resp , getServletContext());

        LOGGER.info("TomcatServletContext : " + getServletContext());

        chain.execute(servletContext);

        servletContext.doRender();
    }

    /**
     * web初始化会执行一次
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initializing servlet '" + getServletName() + "'");
        }

        initEnvironment();

        initServletContextConfig();

        HandlerExecutionChain.addHandler(new StaticResourceHandler());
        HandlerExecutionChain.addHandler(new RequestDispatcherHandler());

        initServlet();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialize servlet '" + getServletName() + "' completed!");
        }
    }


    /**
     * 初始化servlet参数设置
     */
    private void initEnvironment() {
        contextConfigLocation = getServletConfig().getInitParameter("contextConfigLocation");
        if(contextConfigLocation == null)
            contextConfigLocation = getServletName() + DEFAULT_SERVLET_CONFIG_FILENAME_SUFFIX;
        LOGGER.info("To use context config location: " + contextConfigLocation);
    }

    /**
     * 加载静态资源
     */
    private void initServletContextConfig() {
        try {
            Resource resource = new FileSystemResource(
                    this.getServletContext().getResource(this.contextConfigLocation).getFile());
            DeviaContextConfigReader contextConfigReader = new DeviaContextConfigReader(resource);
            contextConfigReader.loadStaticResources();

            scanPaths = contextConfigReader.getComponentScanPath();
            LOGGER.trace("Set scan path: " + scanPaths);
        } catch (MalformedURLException e) {
            LOGGER.error(
                    "Context config file '" + this.contextConfigLocation + "' cannot be found.",e);
        }
        //before this to set scanPaths
        afterServletContextConfig();
    }

    private void afterServletContextConfig() {

        ApplicationManager.setScanPaths(scanPaths);
        ApplicationManager.start();

    }

    /**
     * nothing to do
     * 留给子类继承
     */
    private void initServlet() {

    }

}
