/**
 * Licensed to Open-Ones Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Open-Ones Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package openones.corewa.res;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import rocky.common.CommonUtil;
import rocky.common.Constant;
import rocky.common.PropertiesManager;

/**
 * @author ThachLN
 *
 */
public class DefaultRes {
    final public static Logger LOG = Logger.getLogger("DefaultRes");
    final static String DEF_LANG = "en";
    
    private String lang;
    
    /** Default resourse "/ApplicationResources" */
    private String baseName = "ApplicationResources";
    
    private Properties props = null;
    
    /**
     * Create instance of Resource with order left to right of language codes.
     * @param langs array of language codes
     */
    public DefaultRes(String[] langs) {
        PropertiesManager propsManager = null;
        String resourceFile;
        for (String lang: langs) {
            try {
                if (CommonUtil.isNNandNB(lang)) {
                    resourceFile = "/" + baseName + "_" + lang + ".properties";
                    
                } else {
                    resourceFile = "/" + baseName + ".properties";
                }
                propsManager = new PropertiesManager(resourceFile);
                props = propsManager.getProperties();
                if (props != null) {
                    break;    
                }
            } catch (Exception ex) {
                LOG.warn("Could not load resource /" + baseName + "_" + lang + ".properties");
            }
        }
    }
    /**
     * 
     * @param lang en | vn
     * Default value
     */
    public DefaultRes(String langCode) {
        if (CommonUtil.isNNandNB(lang)) {
            this.lang = langCode; 
        } else {
            this.lang = DEF_LANG;
        }
        
        // Load file properties
        loadResource(baseName);
        
    }
    
    public DefaultRes(String langCode, String baseName) {
        if (CommonUtil.isNNandNB(lang)) {
            this.lang = langCode; 
        } else {
            this.lang = DEF_LANG;
        }
        
        // Load file properties
        loadResource(baseName);
        
    }
    
    private void loadResource(String baseName) {
        try {
            props = PropertiesManager.newInstanceFromProps("/" + baseName + "_" + lang + ".properties");
        } catch (IOException ex) {
            LOG.error("Load resource file " + "/" + baseName + "_" + lang + ".properties", ex);
        }
    }
    
    public String get(String key) {
        return props.getProperty(key, Constant.BLANK);
    }
    
    public Set<Object> getKey() {
        return props.keySet();
    }
}
