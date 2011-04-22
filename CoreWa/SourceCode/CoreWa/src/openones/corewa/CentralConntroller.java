/*
 * CentralConntroller.java 0.1 June 30, 2010
 * 
 * Copyright (c) 2010, LunarCal4U
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package openones.corewa;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import openones.corewa.config.ConfigUtil;
import openones.corewa.config.CoreWa;
import openones.corewa.config.Event;
import openones.corewa.config.Screen;
import openones.corewa.control.BaseControl;
import rocky.common.CommonUtil;

@SuppressWarnings("serial")
public class CentralConntroller extends HttpServlet {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());
    private final static String DEF_EVENTID = "init";
    private final static String DEF_PROCID = "procInit";
    private final static String DEF_ERRORPAGE = "/WEB-INF/pages/error.jsp";
    private CoreWa conf;
    private BaseOutForm resultForm;

    static Map<String, Object> cacheControl = new HashMap<String, Object>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String confFile = config.getInitParameter("conf-file");
        String realPath = config.getServletContext().getRealPath("WEB-INF/" + confFile);
        LOG.log(Level.INFO, "real path of confFile=" + confFile);
        try {
            conf = ConfigUtil.parse(new FileInputStream(realPath));
        } catch (FileNotFoundException fnfex) {
            LOG.log(Level.CONFIG, "init", fnfex);
            throw new ServletException(fnfex);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Screen screen;
        Event event;
        Object controlClass = null;
        String procId = null;
        String nextScrId = null;
        Method method;
        
        try {
            String screenId = req.getParameter("screenId");
            String eventId = req.getParameter("eventId");
            String servletPath = req.getServletPath().substring(1); // servlet path without forward character /

            LOG.log(Level.INFO, "servlet path=" + servletPath);
            LOG.log(Level.INFO, "home screen id=" + conf.getHomeScreenId());
//            if ((conf.getLayout() != null) && (conf.getLayout().getPart(servletPath) != null) && (screenId == null)) {
//                Part homePart = conf.getLayout().getPart(servletPath);
//                screenId = homePart.getScreenId();
//                LOG.log(Level.INFO, "Layout mode; screenId=" + screenId);
//            }
            
            if ((screenId == null) || (eventId == null)) {
                eventId = DEF_EVENTID;
            }

            LOG.log(Level.INFO, "screenId=" + screenId + ";eventId=" + eventId);
            screen = conf.getScreen(screenId);

            if (screen == null) { // Using home screen from the configuration
                screen = conf.getScreens().get(conf.getHomeScreenId());
            }

            event = screen.getEvent(eventId);

            if (event != null) {
                procId = event.getProcId();
                nextScrId = event.getNextScrId();
            }

            if (procId == null) { // procId is not declared, it is default
                procId = DEF_PROCID;
            }

            if (nextScrId == null) { // the next screen id is not declared, it is the same.
                nextScrId = screen.getInputPage();
            }

            LOG.log(Level.INFO, "procId=" + procId + ";nextScreenId=" + nextScrId);
            
            String ctrlClassName = screen.getCtrlClass();
            
            if (CommonUtil.isNNandNB(ctrlClassName)) {
                if (cacheControl.containsKey(ctrlClassName)) { // The control is already created
                    controlClass = cacheControl.get(ctrlClassName);
                } else { // Create new the instance of the control. The put it into the cache
                    controlClass = Class.forName(ctrlClassName).newInstance();
                    cacheControl.put(ctrlClassName, controlClass);
                }
            }

            // Assumption if tag <event> without attribute "redirect", tag "control" is not null
            if ((event != null) && (!event.isRedirect())) {
               // ScreenForm screenForm = (ScreenForm) BaseControl.getData(request, ScreenForm.class);
                Map<String, Object> mapReq = BaseControl.getMapData(req);
                
                LOG.log(Level.FINE, "Invoke method '" + procId + "' of class '" + screen.getCtrlClass() + "'");
                
                try {
                    method = controlClass.getClass().getMethod(procId, HttpServletRequest.class, Map.class,
                            HttpServletResponse.class);
                    resultForm = (BaseOutForm) method.invoke(controlClass, req, mapReq, resp);
                } catch (Exception ex) {
                    LOG.warning("Could not invoke method xxx(ActionRequest, Map, ActionRespone)");
                    // @deprecated
                    method = controlClass.getClass().getMethod(procId, HttpServletRequest.class, HttpServletResponse.class);
                    method.invoke(controlClass, req, resp);
                }
            } else if (controlClass!= null) { // Initial screen
                method = controlClass.getClass().getMethod(DEF_PROCID, HttpServletRequest.class, HttpServletResponse.class);
                resultForm = (BaseOutForm) method.invoke(controlClass, req, resp);
            }
            
            if (resultForm != null) {
                partResultFormIntoWeb(resultForm, req, req.getSession());
            }

            RequestDispatcher dispatcher = null;
            if ((conf.getLayout() != null) && CommonUtil.isNNandNB(conf.getLayout().getId())) {
                LOG.info("Layout page:" + conf.getScreen(conf.getHomeScreenId()).getInputPage());
                dispatcher = req.getRequestDispatcher(conf.getScreen(conf.getHomeScreenId()).getInputPage());
                dispatcher.forward(req, resp);
            } else {
                dispatcher = req.getRequestDispatcher(nextScrId);
                
                if ((event != null) && (event.getDispType() == Event.DispType.FORWARD)) {
                    LOG.info("Forward to '" + nextScrId);
                    // Forward to next screen
                    dispatcher.forward(req, resp);
                } else {
                    LOG.info("Include '" + nextScrId);
                    // Include the next screen
                    dispatcher.include(req, resp);
                }
            }
        } catch (Throwable th) {
            LOG.log(Level.FINEST, "doPost", th);
            th.printStackTrace();
            req.getRequestDispatcher(DEF_ERRORPAGE).include(req, resp);
        }
    }
    
    /**
     * 
     * @param request
     * @param session
     */
    private void partResultFormIntoWeb(BaseOutForm resultForm, HttpServletRequest request, HttpSession session) {
        //
        if (resultForm != null) {
            // Scan object in session map to put into the session
            Map<String, Object> sessionMap = resultForm.getSessionMap();
            
            if (sessionMap.keySet() != null) {
                for (String key : sessionMap.keySet()) {
                    LOG.log(Level.FINEST, "Set session attribute: key = " + key);
                    session.setAttribute(key, sessionMap.get(key));
                    LOG.log(Level.FINEST, "Set session attribute: sessionMap.get(key) = " + sessionMap.get(key));
                }
            }

            // Scan object in reqest map to put into the request
            Map<String, Object> requestMap = resultForm.getRequestMap();
            if (requestMap.keySet() != null) {
                for (String key : requestMap.keySet()) {
                    LOG.log(Level.FINEST,"Set request attribute: key = " + key);
                    request.setAttribute(key, requestMap.get(key));
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

}
