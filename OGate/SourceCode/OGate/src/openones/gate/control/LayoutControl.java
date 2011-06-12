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
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import openones.corewa.BaseOutForm;
import openones.gae.users.OUser;
import openones.gate.Cons;
import openones.gate.biz.ModuleBiz;
import openones.gate.biz.SessionBiz;
import openones.gate.store.dto.ModuleDTO;

/**
 * @author Thach Le
 */
public class LayoutControl extends OGateBaseControl {
    private static int nmLogonUser = 0;

    public LayoutControl() {
        super();
    }

    public LayoutControl(ServletConfig config) {
        super(config);
    }

    @Override
    public BaseOutForm procInit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        outForm.putRequest(SK_MAINSCREEN, "intro");
        OUser logonUser = SessionBiz.getLogonUser();
        ModuleBiz moduleBiz = new ModuleBiz(logonUser );
        // Get list of tab and it's content
        List<ModuleDTO> moduleTasList = moduleBiz.getModules(SessionBiz.getLangCd());
        
        LOG.info("Number of tabs:" + moduleTasList.size());
        outForm.putSession("moduleTabs", moduleTasList);
        
        return outForm;
    }

    public static int updateNmLogonUser(HttpSession session, int delta) {
        // Update number of logon user
        Object nmLogonUserObj = session.getAttribute(Cons.SK_NMLOGON_USER);

        if (nmLogonUserObj != null) {
            nmLogonUser = (Integer) nmLogonUserObj;
        }

        nmLogonUser += delta;
        if (nmLogonUser < 0) {
            nmLogonUser = 0;
        }
        session.setAttribute(Cons.SK_NMLOGON_USER, nmLogonUser);

        return nmLogonUser;
    }
}
