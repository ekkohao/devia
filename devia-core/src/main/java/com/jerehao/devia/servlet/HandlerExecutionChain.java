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

import com.jerehao.devia.servlet.handler.Handler;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-05 14:25 jerehao
 */
public class HandlerExecutionChain {
    private static final List<Handler> chain = new LinkedList<>();

    private java.util.Iterator<Handler> chainIterator;

    private DeviaServletContext servletContext;


    public HandlerExecutionChain() {
    }

    public static void addHandler(Handler handler) {
        chain.add(handler);
    }

    public static void addHandlerBefore(Handler handler, Handler before) {
        int i = chain.indexOf(before);
        if(i < 0)
            chain.add(handler);
        else
            chain.add(i, handler);
    }

    public static void addHandlerAfter(Handler handler, Handler after) {
        int i = chain.indexOf(after);
        if(i < 0)
            chain.add(handler);
        else
            chain.add(i + 1, handler);
    }

    public void execute(DeviaServletContext context) {
        servletContext = context;
        chainIterator = chain.iterator();
        next();
    }

    public void next() {
        if(chainIterator.hasNext()) {
            chainIterator.next().handle(servletContext, this);
        }
    }

}
