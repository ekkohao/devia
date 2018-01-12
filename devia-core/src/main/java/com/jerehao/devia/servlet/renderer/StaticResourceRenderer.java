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

import com.jerehao.devia.core.resource.FileSystemResource;
import com.jerehao.devia.core.resource.Resource;
import com.jerehao.devia.core.util.MimeType;
import com.jerehao.devia.logging.Logger;
import com.jerehao.devia.servlet.DeviaServletContext;
import com.jerehao.devia.servlet.exception.NotSupportedHttpRequestMethodException;
import com.jerehao.devia.servlet.helper.HttpCacheControl;
import com.jerehao.devia.servlet.helper.HttpMethod;
import com.jerehao.devia.servlet.helper.RequestAttributeKeys;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Base64;


/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-05 15:58 jerehao
 */
public class StaticResourceRenderer extends ResponseGenerator implements Renderer {

    private static final Logger LOGGER = Logger.getLogger(StaticResourceRenderer.class);

    private static final String REQUEST_HEAD_KEY_IF_MODIFIED_SINCE = "If-Modified-Since";

    private static final String REQUEST_HEAD_KEY_IF_NONE_MATCH = "If-None-Match";

    private static final String REQUEST_HEAD_KEY_RANGE = "Range";

    private static final String RESPONSE_HEAD_KEY_LAST_MODIFIED = "Last-Modified";

    private static final String RESPONSE_HEAD_KEY_ACCEPT_RANGES = "Accept-Ranges";

    private static final String RESPONSE_HEAD_KEY_CONTENT_RANGE = "Content-Range";

    private static final String RESPONSE_HEAD_KEY_CACHE_CONTROL = "Cache-Control";

    private static  final String RESPONSE_HEAD_KEY_ETAG = "ETag";

    private static final int BUFFER_SIZE = 4096;

    private HttpCacheControl httpCacheControl;

    private ServletContext tomcatServletContext;

    private HttpServletRequest request;

    private HttpServletResponse response;

    public StaticResourceRenderer() {
        this(7 * 24 * 3600);
    }

    public StaticResourceRenderer(int maxAge) {
        super(HttpMethod.GET, HttpMethod.HEAD);
        this.httpCacheControl = new HttpCacheControl();
        this.httpCacheControl.setMaxAge(maxAge);
    }

    public void setHttpCacheControl(HttpCacheControl httpCacheControl) {
        this.httpCacheControl = httpCacheControl;
    }

    public HttpCacheControl getHttpCacheControl() {
        return httpCacheControl;
    }

    @Override
    public void render(DeviaServletContext context) {
        this.tomcatServletContext = context.getTomcatServletContext();
        this.request = context.getRequest();
        this.response = context.getResponse();

        LOGGER.info("Static render start");

        Resource resource = getResource();

        if (resource == null) {
            doRenderByCode(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (HttpMethod.OPTIONS.match(this.request.getMethod())) {
            this.response.setHeader("Allow", getAllowHeader());
            return;
        }


        try {
            checkRequestMethod();
        } catch (NotSupportedHttpRequestMethodException e) {
            LOGGER.error("Not supported http request method.", e);
            doRenderByCode(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        String mimeType = determineMimeType();
        try {
            if (checkNotModified(resource)) {
                LOGGER.trace("Resource not modified.");
                return;
            }

            ///缓存控制 DONE
            applyCacheControl();

            if (HttpMethod.HEAD.match(request.getMethod())) {

                setResponseHeader(resource, mimeType);
                return;
            }

            LOGGER.info("File location uri = " + resource.getURI());


            if(this.request.getAttribute(REQUEST_HEAD_KEY_RANGE) == null) {
                setResponseHeader(resource, mimeType);
                writeContent(resource);
            }
            else { //分段下载

                this.response.setHeader(RESPONSE_HEAD_KEY_ACCEPT_RANGES, "bytes");
                this.response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                if(!StringUtils.isEmpty(mimeType))
                    this.response.setContentType(mimeType);

                writeRangeContent(resource);

            }

        } catch (IOException e) {
            LOGGER.error("Resource open error.", e);
            doRenderByCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            //return;
        }
    }

    private void applyCacheControl() {
        String cacheHead;
        if(this.httpCacheControl != null &&
                !StringUtils.isEmpty(cacheHead = httpCacheControl.getResponseHeader())) {
            this.response.setHeader(RESPONSE_HEAD_KEY_CACHE_CONTROL, cacheHead);
        }
    }

    private void writeContent(Resource resource) throws IOException {
        OutputStream op = this.response.getOutputStream();
        InputStream in = resource.getInputStream();
        int readCount = -1;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((readCount = in.read(buffer)) != -1) {
            op.write(buffer, 0, readCount);
        }
        op.flush();
    }

    private void writeRangeContent(Resource resource) throws IOException {
        String range = this.request.getHeader(REQUEST_HEAD_KEY_RANGE);
        String startStr = range.substring(range.indexOf('=') + 1, range.lastIndexOf('-'));
        String endStr = range.substring(range.lastIndexOf('-') + 1);
        int start = StringUtils.isNumeric(startStr.trim()) ? Integer.valueOf(startStr) : 0;
        int end = StringUtils.isNumeric(endStr.trim()) ? Integer.valueOf(endStr) : Integer.MAX_VALUE;

        OutputStream op = this.response.getOutputStream();
        InputStream in = resource.getInputStream();
        int readCount = -1;
        int readSum = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int toReadLen = BUFFER_SIZE;
        if (end - start + 1 < BUFFER_SIZE)
            toReadLen = end - start + 1;

        while ((readCount = in.read(buffer, start ,toReadLen)) != -1 && start <= end) {
            op.write(buffer, 0, readCount);
            readSum += readCount;
            start += toReadLen;
            if (end - start + 1 < BUFFER_SIZE)
                toReadLen = end - start + 1;
        }

        this.response.setContentLength(readSum);
        this.response.setHeader(RESPONSE_HEAD_KEY_CONTENT_RANGE,"bytes " + start +"-" + (start + readSum - 1) + "/" + resource.getFile().length());

        op.flush();
    }


    private void setResponseHeader(Resource resource, String mimeType) throws IOException {
        long length = resource.getFile().length();
        if (length > Integer.MAX_VALUE)
            response.setContentLengthLong(length);
        else
            response.setContentLength(Integer.parseInt(String.valueOf(length)));

        if (!StringUtils.isEmpty(mimeType))
            response.setContentType(mimeType);
        response.setHeader(RESPONSE_HEAD_KEY_ACCEPT_RANGES, "bytes");
    }

    private String determineMimeType() {
        String uri = this.request.getRequestURI();
        if (uri.lastIndexOf('.') < 0)
            return null;
        String suffix = uri.substring(uri.lastIndexOf('.'));
        return new MimeType().getMimeType(suffix);
    }

    /// TODO 待重构
    private boolean checkNotModified(Resource resource) throws IOException {
        long lastModifiedTime = resource.getFile().lastModified();
        long recordedModifiedTime = this.request.getDateHeader(REQUEST_HEAD_KEY_IF_MODIFIED_SINCE);
        String lastETag = getETagByLastModifiedTime(lastModifiedTime);
        String recordETag = this.request.getHeader(REQUEST_HEAD_KEY_IF_NONE_MATCH);

        if(!StringUtils.isEmpty(recordETag) && !StringUtils.isEmpty(lastETag)) {
            if (StringUtils.equals(lastETag, recordETag.replaceFirst("^/W", ""))) {
                doRenderByCode(HttpServletResponse.SC_NOT_MODIFIED);
                return true;
            }
            else {
                this.response.setDateHeader(RESPONSE_HEAD_KEY_LAST_MODIFIED, lastModifiedTime);
                this.response.setHeader(RESPONSE_HEAD_KEY_ETAG, lastETag);
                return false;
            }
        }

        LOGGER.info("lastModifiedTime = " + lastModifiedTime + ", recordedModifiedTime = " + recordedModifiedTime);

        if (lastModifiedTime > 0 && lastModifiedTime / 1000 == recordedModifiedTime / 1000) {
            doRenderByCode(HttpServletResponse.SC_NOT_MODIFIED);
            return true;
        } else {
            this.response.setDateHeader(RESPONSE_HEAD_KEY_LAST_MODIFIED, lastModifiedTime);
            if(!StringUtils.isEmpty(lastETag))
                this.response.setHeader(RESPONSE_HEAD_KEY_ETAG, lastETag);
            return false;
        }
    }

    private String getETagByLastModifiedTime(long lastModifiedTime) {
        String encode = Base64.getEncoder().encodeToString(String.valueOf(lastModifiedTime).getBytes());
        return lastModifiedTime > 0 ? "\"" + encode + "\"" : null;
    }


    private void checkRequestMethod() throws NotSupportedHttpRequestMethodException {
        HttpMethod method = HttpMethod.valueOf(StringUtils.upperCase(this.request.getMethod()));
        if (this.supportedMethods != null && !this.supportedMethods.contains(method))
            throw new NotSupportedHttpRequestMethodException("Not supported http request method : " + method.name());
    }

    private void doRenderByCode(int code) {
        try {
            this.response.sendError(code);
        } catch (IOException e) {
            LOGGER.error("render " + code + " error", e);
        }
    }

    private Resource getResource() {

        final String locationURI = (String) this.request.getAttribute(RequestAttributeKeys.STATIC_LOCATION_URI);

        Resource resource;

        try {
            resource = new FileSystemResource(tomcatServletContext.getResource(locationURI).getPath());
        } catch (MalformedURLException e) {
            resource = null;
        }
        return resource;
    }
}
