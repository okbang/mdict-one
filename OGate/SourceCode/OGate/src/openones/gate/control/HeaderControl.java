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
import javax.servlet.http.HttpSession;

import openones.corewa.BaseOutForm;
import openones.corewa.ReqUtil;
import openones.corewa.res.DefaultRes;
import openones.gate.Cons;
import openones.gate.header.form.HeaderInForm;
import openones.gate.header.form.HeaderOutForm;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Controller of part Header.
 * @author Thach Le
 */
public class HeaderControl extends LayoutControl {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());
    //private ServletContext context = null;
    
    private HeaderControl() {
    }
    
    public HeaderControl(ServletConfig config) {
        super(config);
    }
    
    @Override
    public BaseOutForm procInit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("procInit.START");
        HeaderOutForm headerOutForm = new HeaderOutForm();
        
        //context = req.getSession().getServletContext();
        outForm.putRequest("outForm", headerOutForm);
        
        return outForm;
    }
    
    public BaseOutForm googleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        BaseOutForm outForm = new BaseOutForm();
        UserService userService = UserServiceFactory.getUserService();

        //resp.setCharacterEncoding("utf8");
        outForm.setNextScreen(userService.createLoginURL(req.getRequestURI()));
        //outForm.setDispatched(true);
        //resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));

        // Assumption: user click on Login mean will try to login successfully
        updateNmLogonUser(req.getSession(), +1);

        return outForm;
    }
    public void googleLogout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = UserServiceFactory.getUserService();

        resp.sendRedirect(userService.createLogoutURL(req.getRequestURI()));

        HttpSession session = req.getSession();
        if (session != null) {
            session.removeAttribute(Cons.SK_USER);
            updateNmLogonUser(session, -1);
        }
    }
    
    public BaseOutForm changeLanguage(HttpServletRequest req, Map<String, Object> reqMap, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("changeLanguage.START");
        HeaderOutForm headerOutForm = new HeaderOutForm();
        
        for (Object key: reqMap.keySet()) {
            LOG.info(key + "=" + reqMap.get(key));
        }
        HeaderInForm headerInForm = (HeaderInForm) ReqUtil.getData(reqMap, HeaderInForm.class);
        String selectedLang = headerInForm.getLang();
        
        // Keep the selected language
        headerOutForm.setSelectedLang(selectedLang);
        
        LOG.info("Selected language:" + selectedLang);
        outForm.putRequest("outForm", headerOutForm);
        
        // Keep language name into the session.
        outForm.putSession(Cons.SK_LANG, selectedLang);
        
        LOG.info("Reload the resource of language code " + headerOutForm.getLangCd(selectedLang));
        
        String langCd = headerOutForm.getLangCd(selectedLang);
        LOG.info("Loading resource for language " + langCd + ";" + selectedLang);
        reloadResource(langCd);
        
        return outForm;
    }

    
    public BaseOutForm changeLanguage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.finest("changeLanguage.START");
        HeaderOutForm headerOutForm = new HeaderOutForm();
        
        String selectedLang = req.getParameter("lang");
        
        headerOutForm.setSelectedLang(selectedLang);
        
        LOG.info("Selected language:" + selectedLang);
        outForm.putRequest("outForm", headerOutForm);
        
        LOG.info("Reload the resource of language code " + headerOutForm.getLangCd(selectedLang));
        reloadResource(headerOutForm.getLangCd(selectedLang));
        
        return outForm;
    }   

    /* 
     * Explain the description for this method here
     * @see openones.corewa.control.BaseControl#reloadResource(java.lang.String)
     */
    @Override
    public void reloadResource(String langCd) {
        LOG.info("reloadResource:" + langCd);
        // Load resource
        DefaultRes resource = new DefaultRes(langCd);
        if (resource != null) {
            LOG.info("Number of resource:" + resource.getKey().size());
            for (Object key : resource.getKey()) {
                LOG.info(key.toString() + "=" + resource.get(key.toString()));
                config.getServletContext().setAttribute(key.toString(), resource.get(key.toString()));
            }
        }
    }
}
