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
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import openones.gate.biz.SessionBiz;
import openones.gate.store.ModuleStore;
import rocky.common.Constant;

import com.google.appengine.api.datastore.Text;

/**
 * @author ThachLN
 */
public class MainBody extends HttpServlet {
    final static Logger LOG = Logger.getLogger("MainBody");
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String screenId = req.getParameter("screenId");
        String eventId = req.getParameter("eventId");
        String tabId = req.getParameter("tabId");

        // Click on left menu
        // if ("selectItem".equals(eventId)) {
        // String menuId = req.getParameter("menuId");
        // processMenu(screenId, menuId, req, resp);
        // } else if ("tabId".equals(eventId)) {
        // processTab(screenId, tabId, req, resp);
        // }

        resp.setContentType("text/html");

        String moduleId = tabId;
        String langCd = (String) req.getSession().getAttribute("langCd");
        if (langCd == null) {
            // Get default language code
            langCd = SessionBiz.getLangCd();
        }
        
        LOG.info("screenId=" + screenId + ";eventId=" + eventId + ";tabId=" + tabId + ";langCd=" + langCd);
        Text tabContent = ModuleStore.getLastModuleContent(moduleId, langCd);
        String content = (tabContent != null) ? tabContent.getValue() : Constant.BLANK;
        LOG.info("content=" + content);
        PrintWriter out = resp.getWriter();

        out.print(content);
    }

}
