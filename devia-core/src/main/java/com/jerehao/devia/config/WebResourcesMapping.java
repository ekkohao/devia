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
import com.jerehao.devia.common.annotation.Nullable;
import com.jerehao.devia.config.annotation.WebResource;
import com.jerehao.devia.core.util.AntPathMather;
import com.jerehao.devia.core.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-16 18:29 jerehao
 */
public class WebResourcesMapping {

    private final List<WebResource> webResources = new LinkedList<>();

    private static final WebResourcesMapping instance;

    static {
        instance = new WebResourcesMapping();
    }

    public static String decideLocalURI(String clientURI) {
        MatchResult mr = null;

        for( WebResource webResource : instance.webResources) {
            MatchResult matchResult = matching(webResource, clientURI);
            if(matchResult != null && matchResult.isMatched())
                if(mr == null || mr.getMatchedURI().length() < matchResult.getMatchedURI().length())
                    mr = matchResult;
        }

        return (mr == null) ? null : getLocalURI(mr, clientURI);
    }

    private static MatchResult matching(@NotNull WebResource webResource, @NotNull String clientURI) {
        String uris = webResource.value();
        String matchedURI = "";
        clientURI = uniformFormat(clientURI);

        for(String uri : uris.split(",")) {
            uri = uniformFormat(uri);
            if(uri.startsWith("-") && AntPathMather.Match(uri.substring(1), clientURI ,false))
                return null;
            if(uri.startsWith("/") && AntPathMather.Match(uri, clientURI, false) && uri.length() > matchedURI.length())
                matchedURI = uri;
        }

        return new MatchResult(matchedURI, webResource.locationTo());
    }

    private static String getLocalURI(MatchResult mr, String clientURI) {
        String matchedURI = mr.getMatchedURI();
        String locationTo = uniformFormat(mr.getLocalURI());
        clientURI = uniformFormat(clientURI);

        if(!matchedURI.contains("**"))
            return locationTo + clientURI.substring(matchedURI.length());
        //if(matchedURI.endsWith("**"))
        //    return locationTo + "/" +getFileName(clientURI);
        int lastSlash = clientURI.length();
        while (lastSlash > -1) {
            String current = clientURI.substring(0, lastSlash);
            if(AntPathMather.Match(matchedURI, current))
                return locationTo + clientURI.substring(lastSlash);
            else
                lastSlash = current.lastIndexOf("/");
        }
        //should never happened
        return locationTo + "/" + getFileName(clientURI);
    }

    private static String uniformFormat(String s) {
        if(s == null)
            return "";
        s = s.trim();

        if(s.endsWith("/"))
            s = s.substring(0, s.length() - 1);

        if(s.startsWith("-") && !s.startsWith("-/"))
            return  "-/" + s.substring(1);
        if(!s.startsWith("/"))
            return "/" + s;
        return s;
    }

    private static String getFileName(@NotNull String s) {
        if(s.contains("/"))
            return s.substring(s.lastIndexOf("/") + 1);
        return s;
    }

    private static class MatchResult {

        private final String matchedURI;

        private final String localURI;

        public MatchResult(String matchedURI, String localURI) {
            this.matchedURI = matchedURI;
            this.localURI = localURI;
        }

        public String getMatchedURI() {
            return matchedURI;
        }

        public String getLocalURI() {
            return localURI;
        }

        public boolean isMatched() {
            return !StringUtils.isEmptyOrNull(matchedURI);
        }
    }

}
