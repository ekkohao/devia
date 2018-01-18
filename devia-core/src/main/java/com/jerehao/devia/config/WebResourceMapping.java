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

package com.jerehao.devia.config;

import com.jerehao.devia.common.annotation.NotNull;
import com.jerehao.devia.config.annotation.WebResource;
import com.jerehao.devia.core.util.AntPathMather;
import com.jerehao.devia.core.util.StringUtils;
import com.jerehao.devia.logging.Logger;

import java.util.*;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-16 18:29 jerehao
 */
public class WebResourceMapping implements ResourceMappingStorer {

    private static final Logger LOGGER = Logger.getLogger(WebResourceMapping.class);

    private final List<WebResource> webResources = new LinkedList<>();

    // <except uri, files>
    private final List<MappingEntity> excepts = new LinkedList<>();

    // <mapping uri, files>
    private final List<MappingEntity> mappings = new LinkedList<>();


    private WebResourceMapping(List<WebResource> webResources) {
        initResourceMapping(webResources);
    }

    public static WebResourceMapping getResourceMappingStore(List<WebResource> webResources ) {
        return new WebResourceMapping(webResources);
    }

    public String getLocalURI(String requestURI) {
        for(MappingEntity me : excepts) {
            if(!StringUtils.isEmptyOrNull(me.getLocalURI(requestURI)))
                return "";
        }

        for(MappingEntity me : mappings) {
            String localURI = me .getLocalURI(requestURI);
            if(!StringUtils.isEmptyOrNull(localURI))
                return localURI;
        }

        return "";
    }

    private void initResourceMapping(List<WebResource> webResources) {
        if(webResources == null || webResources.isEmpty())
            return;
        for(WebResource webResource : webResources) {
            resolveMapping(webResource);
        }
    }

    private void resolveMapping(WebResource webResource) {
        if(webResource == null || StringUtils.isEmptyOrNull(webResource.value()))
            return;

        MappingEntity mappingEntity;

        try {
            mappingEntity = new MappingEntity(webResource);
        } catch (ResourceMappingException e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }

        if(mappingEntity.isExcept())
            addMapping(excepts, mappingEntity);
        else
            addMapping(mappings, mappingEntity);

    }

    private void addMapping(List<MappingEntity> list, MappingEntity mappingEntity) {
        int i = 0;
        int listLen = list.size();
        int len = mappingEntity.getUriPattern().length();
        for (; i < listLen; ++i) {
            if(len > list.get(i).getUriPattern().length())
                break;
        }
        list.add(i, mappingEntity);
    }

}


class MappingEntity{

    private static final Logger LOGGER = Logger.getLogger(MappingEntity.class);

    private String uriPattern;

    private String[] fileFilters;

    private String locationTo;

    private boolean isExcept = false;

    public MappingEntity(@NotNull WebResource webResource) throws ResourceMappingException {
        locationTo = formatSlashIfNecessary(webResource.locationTo());
        initURIPattern(webResource.value());
        initSuffixes(webResource.fileFilter());
    }

    public String getLocalURI(@NotNull final String requestURI) {

        String uri = formatSlashIfNecessary(requestURI.trim());
        int lastSlash = uri.lastIndexOf('/');
        String fileName;
        String path;

        //lastSlash must be > 0 as formatSlashIfNecessary
        fileName = uri.substring(lastSlash + 1);
        path = uri.substring(0, lastSlash);

        if(StringUtils.isEmptyOrNull(path))
            path = "/";

        boolean filterMatched = false;

        for (String fileFilter : fileFilters) {
            if (AntPathMather.Match(fileFilter, fileName)) {

                filterMatched = true;
                break;
            }
        }

        if(!filterMatched)
            return "";

        String matched =  AntPathMather.getPartialMatched(uriPattern, path);
        return (StringUtils.isEmptyOrNull(matched)) ? "" : StringUtils.replace(uri, matched, locationTo);
    }

    public boolean isExcept() {
        return isExcept;
    }

    public String getUriPattern() {
        return uriPattern;
    }

    private void initURIPattern(@NotNull String uri) throws ResourceMappingException {
        uri = uri.trim();

        if(!isURI(uri))
            throw new ResourceMappingException(StringUtils.build("Cannot resolve [{0}] as a uri pattern.", uri));

        if(uri.startsWith("-")) {
            isExcept = true;
            uri = uri.substring("-".length());
        }

        uriPattern = formatSlashIfNecessary(uri);
    }

    private void initSuffixes(String[] fileFilters) {
        if(fileFilters == null || fileFilters.length < 1) {
            this.fileFilters = new String[]{"*"};
            return;
        }

        List<String> filterList = new LinkedList<>();
        for(String fileFilter : fileFilters) {
            if(fileFilter.startsWith("/"))
                fileFilter = fileFilter.substring(1);

            if(fileFilter.contains("/")) {
                LOGGER.error(StringUtils.build("Cannot resolve [{0}] as a fileFilter because there is a '/' in", fileFilter));
                continue;
            }

            if(fileFilter.startsWith("."))
                fileFilter = "*" + fileFilter;
            filterList.add(fileFilter);
        }
        this.fileFilters = new String[filterList.size()];
        this.fileFilters = filterList.toArray(this.fileFilters);
    }

    private String formatSlashIfNecessary(@NotNull String s) {
        if(!s.startsWith("/"))
            s = "/" + s;

        if(s.endsWith("/"))
            s = s.substring(0, s.length() - 1);

        return s;
    }

    private boolean isURI(String str) {
        if(StringUtils.isEmptyOrNull(str))
            return false;

        char[] allowed = new char[]{'/', '\\', '-', '_', '=', '^', '!', '.', '?', '*'};

        for(char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && !inArray(c, allowed))
                return false;
        }

        return true;
    }

    private boolean inArray(char c, char[] chars) {
        if(chars != null && chars.length > 0) {
            for (char ch : chars)
                if (c == ch)
                    return true;
        }
        return false;
    }

}
