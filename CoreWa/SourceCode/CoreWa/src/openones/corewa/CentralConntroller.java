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

import openones.corewa.config.ConfigUtil;
import openones.corewa.config.CoreWa;
import openones.corewa.config.Event;
import openones.corewa.config.Screen;
import rocky.common.CommonUtil;

@SuppressWarnings("serial")
public class CentralConntroller extends HttpServlet {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());
    private final static String DEF_EVENTID = "init";
    private final static String DEF_PROCID = "procInit";
    private final static String DEF_ERRORPAGE = "/WEB-INF/pages/error.jsp";
    private CoreWa conf;
    
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
        Object controlClass;
        String procId = null;
        String nextScrId = null;
        String screenId = req.getParameter("screenId");
        String eventId = req.getParameter("eventId");
        
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

        LOG.log(Level.FINE, "procId=" + procId + ";nextScreenId=" + nextScrId);

        if ((event != null) && (!event.isRedirect())) {
            try {
                Method method;
                String ctrlClassName = screen.getCtrlClass();
                if (cacheControl.containsKey(ctrlClassName)) { // The control is already created
                    controlClass = cacheControl.get(ctrlClassName);
                } else { // Create new the instance of the control. The put it into the cache
                    controlClass = Class.forName(ctrlClassName).newInstance();
                    cacheControl.put(ctrlClassName, controlClass);
                }

                LOG.log(Level.FINE, "Invoke method '" + procId + "' of class '" + screen.getCtrlClass() + "'");
                method = controlClass.getClass().getMethod(procId, HttpServletRequest.class, HttpServletResponse.class);
                method.invoke(controlClass, req, resp);

            } catch (Throwable th) {
                LOG.log(Level.FINEST, "doPost", th);
                req.getRequestDispatcher(DEF_ERRORPAGE).forward(req, resp);
            }
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher(nextScrId);

        if ((event != null) && (event.getDispType() == Event.DispType.FORWARD)) {
            // Forward to next screen
            req.getRequestDispatcher(nextScrId).forward(req, resp);
        } else {
            // Include the next screen
            req.getRequestDispatcher(nextScrId).include(req, resp);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
 
}
