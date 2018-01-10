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

import com.jerehao.devia.core.document.DefaultDocumentLoader;
import com.jerehao.devia.core.document.DocumentLoader;
import com.jerehao.devia.core.resource.FileSystemResource;
import com.jerehao.devia.core.resource.Resource;
import com.jerehao.devia.logging.Logger;
import com.jerehao.devia.servlet.helper.StaticResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-03 20:36 jerehao
 */
public class DeviaContextConfigReader {

    public static final String STATIC_RESOURCES_TAG_NAME = "static-resources";

    public static final String RESOURCE_TAG_NAME = "resource";

    public static final String RESOURCE_TAG_LOCATION_ATTRIBUTE_NAME = "location";

    public static final String RESOURCE_TAG_MAPPING_ATTRIBUTE_NAME ="mapping";

    public static final String COMPONENT_SCAN_TAG_NAME = "component-scan";

    public static final String COMPONENT_SCAN_BASE_PACKAGE_ATTRIBUTE_NAME = "base-package";

    private static final Logger LOGGER = Logger.getLogger(DeviaContextConfigReader.class);

    private final Resource contextConfig;

    private DocumentLoader documentLoader;

    private Element root;

    public DeviaContextConfigReader(Resource contextConfig) {
           this(contextConfig, new DefaultDocumentLoader());
    }

    public DeviaContextConfigReader(Resource contextConfig, DocumentLoader documentLoader) {
        this.contextConfig = contextConfig;
        this.documentLoader = documentLoader;
        initDocumentRoot();
    }

    public DocumentLoader getDocumentLoader() {
        return documentLoader;
    }

    public void setDocumentLoader(DocumentLoader documentLoader) {
        this.documentLoader = documentLoader;
    }

    private void initDocumentRoot() {
        try {
            root = documentLoader.loadDocument(contextConfig).getDocumentElement();
            root.normalize();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            LOGGER.error("Get context config file document root element error.");
            e.printStackTrace();
        }
    }

    public void loadStaticResources() {
        LOGGER.info("Loading static resources config ...");
        //Element staticResources
        NodeList staticResourcesList = root.getElementsByTagName(STATIC_RESOURCES_TAG_NAME);
        for(int i = 0 , len = staticResourcesList.getLength(); i < len ; ++i) {
            Element staticResources = (Element) staticResourcesList.item(i);
            NodeList resources = staticResources.getElementsByTagName(RESOURCE_TAG_NAME);
            for (int j = 0, l = resources.getLength(); j < l; ++j) {
                Element resource = ((Element) resources.item(j));
                String location = resource.getAttribute(RESOURCE_TAG_LOCATION_ATTRIBUTE_NAME);
                String mapping = resource.getAttribute(RESOURCE_TAG_MAPPING_ATTRIBUTE_NAME);
                LOGGER.info("loadStaticResources [location] = " + location + " [mapping] = " + mapping);
                StaticResource.addMapping(mapping, location);
            }
        }

    }

    public String getComponentScanPath() {
        NodeList packages = root.getElementsByTagName(COMPONENT_SCAN_TAG_NAME);
        if(packages != null && packages.getLength() > 0)
           return  ((Element) packages.item(0)).getAttribute(COMPONENT_SCAN_BASE_PACKAGE_ATTRIBUTE_NAME);
        return "*";
    }
    ///TODO read xml custom define (see paper)
    ///TODO bean list
    ///TODO init bean
}
