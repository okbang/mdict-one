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
package openones.gate.control.setting;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import openones.corewa.BaseOutForm;
import openones.corewa.ReqUtil;
import openones.gate.biz.setting.TabBiz;
import openones.gate.control.OGateBaseControl;
import openones.gate.form.setting.TabSettingForm;
import openones.gate.form.setting.TabSettingOutForm;
import rocky.common.CommonUtil;

/**
 * @author Thach Le
 *
 */
public class TabControl extends OGateBaseControl {

    @Override
    public BaseOutForm procInit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("procInit.START");
        TabSettingOutForm tabOutForm = TabBiz.getTabs(getLangCd(req));

        LOG.finest("Number of tabs:" + ((tabOutForm.getTabFormMap() != null) ? tabOutForm.getTabFormMap().size():0));

        outForm.putRequest("tabSettingForm", tabOutForm);

        return outForm;
    }

    /**
     * .
     * @param req
     * @param reqMap
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public BaseOutForm save(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("save.START");
        TabSettingForm form = (TabSettingForm) ReqUtil.getData(reqMap, TabSettingForm.class);
        String[] allTabs = req.getParameterValues("selectedTab");
        LOG.finest("getSelectedTab=" + form.getSelectedTab() +
                   ";getParameterValues(selectedTab)=" + CommonUtil.arrayToString(allTabs, ","));

        LOG.finest("Tab keys=" + form.getTabKeys());

        form.setTabForms(allTabs);
        if (TabBiz.save(form, getLangCd(req))) {
        } else {
            keepForm(form);
        }
//        outForm.removeFromSession(K_MODULETABS);
//        outForm.removeFromSession(K_MINTAB_ORDERNO);

        //setMainScreen(Cons.Screens.TabSetting.toString());
        LOG.finest("save.END");
        return outForm;
    }

}
