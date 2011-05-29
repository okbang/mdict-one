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

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import openones.corewa.BaseOutForm;
import openones.gate.Cons;

/**
 * Controller of part Header.
 * 
 * @author Thach Le
 */
public class MenuControl extends LayoutControl {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    /** */
    public enum MenuItem {
        tabSetting, accSetting, langSetting
    }
    /**
     * Parameter name of Menu Item. Refer Jsp page leftmenu.jsp
     */
    final static String K_MENUID = "menuId";

    private MenuControl() {
    }

    public MenuControl(ServletConfig config) {
        super(config);
    }

    public BaseOutForm procItem(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp)
            throws ServletException, IOException {
        LOG.finest("procItem.START");
        setMainScreen("EditModuleIntro");

        return outForm;
    }

    public BaseOutForm procCommonItem(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp)
            throws ServletException, IOException {
        LOG.finest("procCommonItem.START;selected menu=" + reqMap.get(K_MENUID));

        MenuItem selectedMenu = MenuItem.valueOf((String) reqMap.get(K_MENUID));

        if (selectedMenu == MenuItem.tabSetting) {
            setMainScreen(Cons.Screens.TabSetting.toString());
        } else if (selectedMenu == MenuItem.accSetting) {
            setMainScreen(Cons.Screens.AccSetting.toString());
        } else if (selectedMenu == MenuItem.langSetting) {
            // 
            //outForm.putRequest(K_FORM, value);
            setMainScreen(Cons.Screens.LangSetting.toString());
        } else {
            LOG.warning("No procesing for menu item: " + selectedMenu);
        }

        return outForm;
    }
}
