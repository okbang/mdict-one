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
import openones.gate.store.ModuleStore;
import openones.gate.store.dto.ModuleDTO;

/**
 * Controller of part Menu.
 * @author Thach Le
 */
public class MenuControl extends OGateBaseControl {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    /** */
    public enum MenuItem {
        tabSetting, accSetting, langSetting
    }

    private MenuControl() {
    }

    public MenuControl(ServletConfig config) {
        super(config);
    }

    /**
     * Processing select tab from menu of the Admin.
     * @param req
     * @param reqMap
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public BaseOutForm procItem(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp)
            throws ServletException, IOException {
        LOG.finest("procItem.START");
        // MenuId = selected tab module id
        String menuId = (String) reqMap.get(K_MENUID);
        String tabKey = (String) reqMap.get(K_TABKEY);

        LOG.finest("Selected menuId=" + menuId + ";tabKey=" + tabKey);
        setMainScreen("EditModuleIntro");

        outForm.putRequest(K_MENUID, menuId);
        ModuleDTO tabModule = ModuleStore.getTabModuleByKey(Long.valueOf(tabKey));
        outForm.putRequest(K_TABMODULE, tabModule);

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
    
    public BaseOutForm changeLayoutItem(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp)
            throws ServletException, IOException {
        LOG.finest("procItem.START");
        outForm = new BaseOutForm();
        // MenuId = selected tab module id
        String menuId = (String) reqMap.get(K_MENUID);
        String moduleId = (String) reqMap.get(K_MODULEID);

        LOG.finest("Selected menuId=" + menuId + ";moduleId=" + moduleId);
        setMainScreen("ChangeLayoutContent");

        outForm.putRequest(K_MENUID, menuId);
        //List<Text> moduleContents = ModuleStore.getModuleContent(Cons.ModuleType.Layout.toString(), moduleId);
        //outForm.putRequest(K_TABMODULE, tabModule);

        return outForm;
    }
}
