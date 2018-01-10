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

package com.jerehao.devia.servlet;

import com.jerehao.devia.servlet.renderer.Http404Renderer;
import com.jerehao.devia.servlet.renderer.Renderer;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-05 14:36 jerehao
 */
public class DeviaServletContext {
    private HttpServletRequest request;

    private HttpServletResponse response;

    private Renderer renderer;



    private ServletContext tomcatServletContext;

    public DeviaServletContext(HttpServletRequest request, HttpServletResponse response, ServletContext tomcatServletContext) {
        this.request = request;
        this.response = response;
        this.tomcatServletContext = tomcatServletContext;
    }
    public HttpServletResponse getResponse() {
        return response;
    }

    public HttpServletRequest getRequest() {

        return request;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public ServletContext getTomcatServletContext() {
        return tomcatServletContext;
    }

    public void doRender() {
        if(response.isCommitted())
            return;
        if(renderer == null)
            renderer = new Http404Renderer();
        renderer.render(this);
    }

}
