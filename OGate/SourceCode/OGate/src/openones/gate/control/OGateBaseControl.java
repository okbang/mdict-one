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
package openones.gate.control;

import java.util.logging.Logger;

import javax.servlet.ServletConfig;

import openones.corewa.control.BaseControl;
import openones.gate.Cons;

/**
 * @author ThachLN
 *
 */
public class OGateBaseControl extends BaseControl {
    /** . */
    public static final String SK_MAINSCREEN = "MainScreen";
    public static final String K_FORM = "form";
    /**
     * Parameter name of Menu Item. Refer Jsp page leftmenu.jsp
     */
    final static String K_MENUID = "menuId";
    final static String K_TABID = "tabId";
    final static String K_TABKEY = "tabKey";
    final static String K_TABMODULE = "tabModule";
    final static String K_TABMODULEID = "tabModuleId";
    final static String K_TABNAME = "tabName";
    
    
    public final Logger LOG = Logger.getLogger(this.getClass().getName());
    
    /**
     * 
     */
    public OGateBaseControl() {
        super();
    }
    /**
     * @param config
     */
    public OGateBaseControl(ServletConfig config) {
        super(config);
    }
    
    /**
     * Put the data of form bean into the request with key K_FORM ("form"). 
     * @param formBean
     */
    public void keepForm(Object formBean) {
        outForm.putRequest(K_FORM, formBean);
    }
    
    /**
     * Set screen identifier into the request/session with key "MainScreen".
     * 
     * @param screenId
     */
    public void setMainScreen(String screenId) {
        outForm.putRequest(SK_MAINSCREEN, screenId);
    }

    public void setMainScreen(Cons.Screens screenId) {
        outForm.putRequest(SK_MAINSCREEN, screenId);
    }
}
