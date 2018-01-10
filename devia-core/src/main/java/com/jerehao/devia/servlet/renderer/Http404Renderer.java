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

package com.jerehao.devia.servlet.renderer;

import com.jerehao.devia.logging.Logger;
import com.jerehao.devia.servlet.DeviaServletContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-05 16:10 jerehao
 */
public class Http404Renderer implements Renderer {

    private final static Logger LOGGER = Logger.getLogger(Http404Renderer.class);

    @Override
    public void render(final DeviaServletContext context) {
        final HttpServletResponse response = context.getResponse();
        try {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
            LOGGER.error("404 render error.");
        }
    }
}
