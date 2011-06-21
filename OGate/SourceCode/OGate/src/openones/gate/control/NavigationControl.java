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
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import openones.corewa.BaseOutForm;

/**
 * @author Thach Le
 */
public class NavigationControl extends OGateBaseControl {
    @Override
    public BaseOutForm procInit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        outForm = super.procInit(req, resp);

        return outForm;
    }

    private final Logger LOG = Logger.getLogger(this.getClass().getName());
    // private static int nmLogonUser = 0;

    public BaseOutForm selectTab(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String screenId = req.getParameter("screenId");
        String eventId = req.getParameter("eventId");
        String tabId = req.getParameter(K_TABID);

        LOG.info("selectTab.START:screenId=" + screenId + ";eventId=" + eventId + ";tabId=" + tabId);

        setMainScreen(tabId);
        outForm.putRequest(K_TABID, tabId);
        return outForm;
    }
    
//    public BaseOutForm gotoService(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
//            IOException {
//        setMainScreen("service");
//        
//        return outForm;
//    }
//
//    public BaseOutForm gotoIntro(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
//            IOException {
//
//        setMainScreen("intro");
//
//        return outForm;
//    }
//
//    public BaseOutForm gotoMember(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
//            IOException {
//
//        setMainScreen("member");
//        
//        return outForm;
//    }
//
//    public BaseOutForm gotoProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
//            IOException {
//        
//        setMainScreen("product");
//
//        return outForm;
//    }
}
