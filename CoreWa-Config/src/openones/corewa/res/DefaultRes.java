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
 * @author Thach Le
 *
 */
public class DefaultRes {
    final public static Logger LOG = Logger.getLogger("DefaultRes");
    final static public String DEF_LANG = "vn";
    final static public String BASE_NAME = "ApplicationResources";
    
    private String lang = DEF_LANG;
    
    /** Default resource "/ApplicationResources" */
    //private String baseName = BASE_NAME;
    
    private Properties props = null;
    
    /**
     * Create instance of Resource with order left to right of language codes.
     * @param langs array of language codes
     */
    public static DefaultRes getInstance(String[] langs) {
        PropertiesManager propsManager = null;
        String resourceFile;
        Properties props = null;
        for (String langCd: langs) {
            try {
                if (CommonUtil.isNNandNB(langCd)) {
                    resourceFile = "/" + BASE_NAME + "_" + langCd + ".properties";
                    
                } else {
                    resourceFile = "/" + BASE_NAME + ".properties";
                }
                propsManager = new PropertiesManager(resourceFile);
                props = propsManager.getProperties();
                
                if (props != null) {
                    return new DefaultRes(props, langCd);
                }
            } catch (Exception ex) {
                LOG.warn("Could not load resource /" + BASE_NAME + "_" + langCd + ".properties");
            }
        }
        return null;
    }
    /**
     * 
     * @param lang en | vn
     * Default value
     */
//    public DefaultRes(String langCode) {
//        if (CommonUtil.isNNandNB(langCode)) {
//            this.lang = langCode; 
//        } else {
//            this.lang = DEF_LANG;
//        }
//        
//        // Load file properties
//        loadResource(baseName, langCode);
//        
//    }
    
//    public DefaultRes(String langCd, String baseName) {
//        if (CommonUtil.isNNandNB(lang)) {
//            this.lang = langCd; 
//        } else {
//            this.lang = DEF_LANG;
//        }
//        
//        // Load file properties
//        loadResource(baseName, langCd);
//        
//    }
    
    /**
     * @param props
     */
    private DefaultRes(Properties props) {
        this.props = props;
    }

    private DefaultRes(Properties props, String langCd) {
        this.props = props;
        this.lang = langCd;
    }
    
    public static DefaultRes getInstance(String langCd) {
        // Load file properties
        Properties props = loadResource(BASE_NAME, langCd);

        if (props != null) {
            return new DefaultRes(props, langCd);
        } else {
            return null;
        }
    }
    
    private static Properties loadResource(String baseName, String langCd) {
        Properties props = null;
        try {
            props = PropertiesManager.newInstanceFromProps("/" + baseName + "_" + langCd + ".properties");
        } catch (IOException ex) {
            LOG.error("Load resource file " + "/" + baseName + "_" + langCd + ".properties", ex);
        }
        return props;
    }
    
    public String get(String key) {
        return props.getProperty(key, Constant.BLANK);
    }
    
    public Set<Object> getKey() {
        return props.keySet();
    }
    
    public String getLang() {
        return lang;
    }
}
