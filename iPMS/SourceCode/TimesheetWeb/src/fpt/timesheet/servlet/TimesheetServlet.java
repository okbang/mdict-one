/*
 * Copyright (c) 2009, FPT Software JSC
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *	- Neither the name of the FPT Software JSC nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 package fpt.timesheet.servlet;

/**
 * @Title:        TimesheetServlet.java
 * @Description:  Locate and forward the requests from the control pages.
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  November 18, 2002
 * @Modified date:
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import fpt.timesheet.bean.UserInfoBean;
import fpt.timesheet.bean.Report.InquiryReportBean;
import fpt.timesheet.bo.Approval.TimesheetBO;
import fpt.timesheet.constant.TIMESHEET;
import fpt.timesheet.servlet.core.BaseServlet;
//import fpt.timesheet.util.Rsa;


public class TimesheetServlet extends BaseServlet {
    private static Logger logger = Logger.getLogger(TimesheetServlet.class);

    /**
     * TimesheetServlet constructor.
     */
    public TimesheetServlet() {
    }
    
    public void init() {
        // Init log4j, the config file is "/Web content/lob4j.properties"
        org.apache.log4j.PropertyConfigurator.configure(getServletContext().getRealPath("/log4j.properties"));
		InquiryReportBean.setRealPath(this.getServletContext().getRealPath(TIMESHEET.DIRECTORY_DOWNLOAD));
    }
    /**
     * Process client request by action.
     * @author  Nguyen Thai Son
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Throwable    If an exception occurred.
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        try {
            String strType = request.getContentType();
            if ((strType != null) && (strType.startsWith("multipart/form-data"))) {
                uploadTimesheet(request, response);
            }
            else {
                String strAction = request.getParameter("hidAction");

                if (TIMESHEET.HOMEPAGE_ACTION.equals(strAction)) {
                	//go to Home page
                    getLoginForm(request, response);
                    callPage(response, TIMESHEET.JSP_LOGIN, request);
                }
                else if (TIMESHEET.APPROVAL_ACTION.equals(strAction)) {
                    //go to ApprovalServlet
                    callPage(response, TIMESHEET.APPROVAL_SERVLET, request);
                    return;
                }
                else if (TIMESHEET.REPORT_ACTION.equals(strAction)) {
                    //go to ReportServlet
                    callPage(response, TIMESHEET.REPORT_SERVLET, request);
                    return;
                }
//                //get the Rsa public key
//                else if(TIMESHEET.GET_RSA_ACTION.equals(strAction)){
//                    logger.info("TIMESHEET.GET_RSA_ACTION");
//                    Rsa.getAuthenticate(request,response);
//                    return;
//                }
                else {
                    //sendErrorRedirect(request, response, TIMESHEET.JSP_ERROR, new Exception("Invalid action: strAction = " + strAction));
                    getLoginForm(request, response);
                    callPage(response, TIMESHEET.JSP_LOGIN, request);
                }
            }
        }
        catch (Exception exception) {
            logger.debug("Exception in TimesheetServlet.performTask().");
            logger.error("TimesheetServlet - performTask(): ", exception);
            try {
                sendErrorRedirect(request, response, TIMESHEET.JSP_ERROR, exception);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * uploadTimesheet
     * Import timesheet by QA.
     * @author  Tu Ngoc Trung
     * @version November 14, 2003
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception, Throwable
     */
    private void uploadTimesheet(HttpServletRequest request, HttpServletResponse response) throws Exception, Throwable {
        //STEP 1 - Check session
        UserInfoBean beanUserInfo = checkSessionVariables(request, response);
        if (beanUserInfo == null) {
            return;
        }
        //STEP 2 - Upload timesheet
        TimesheetBO tsBO = new TimesheetBO();
        tsBO.uploadTimesheet(request, response, this);
        
        callPage(response, TIMESHEET.JSP_TIMESHEET_IMPORT, request);
    }

    /**
     * Reset session variables
     * @author  Nguyen Thai Son.
     * @version 24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   IOException    If an input or output exception occurred.
     * @exception   ServletException    If a servlet exception occurred.
     */
    private UserInfoBean checkSessionVariables(HttpServletRequest request, HttpServletResponse response) throws ServletException, Exception, Throwable {
        UserInfoBean beanUserInfo = null;

        try {
            HttpSession session = request.getSession();
            beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");
            String strLoginName = "";
            if (beanUserInfo != null) {
                strLoginName = beanUserInfo.getLoginName();
            }
            
            if (beanUserInfo == null || strLoginName == null ||
                strLoginName.length() <= 0)
            {
                if (TIMESHEET.DEBUG) {
                    logger.debug("TimesheetServlet.checkSessionVariables(): Invalid session.");
                }

                getLoginForm(request, response);
                callPage(response, TIMESHEET.JSP_LOGIN, request);
                return null;
            }
        }
        catch (Exception exception) {
            logger.error("Exception in checkSessionVariables().", exception);
            sendErrorRedirect(request, response, TIMESHEET.JSP_ERROR, exception);
            return null;
        }

        return beanUserInfo;
    }

    /**
     * Redirect exception to an error page.
     * @author  Nguyen Thai Son.
     * @version  10 April, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   errorPageURL String: the URL to the error page.
     * @param   e Throwable: a throwable storing exception.
     * @exception   IOException    If an input or output exception occurred.
     * @exception   ServletException    If a servlet exception occurred.
     */
    private void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL, Throwable e) throws ServletException, IOException {
        request.setAttribute("javax.servlet.jsp.jspException", e);
        //		response.sendError(response.SC_BAD_REQUEST, "Bad request.");
        getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
    }

    /**
     * Get a form to login
     * @author  Nguyen Thai Son
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Throwable    If an exception occurred.
     */
    public void getLoginForm(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            setNoCaching(response);
            if (TIMESHEET.DEBUG) {
                logger.info("session.invalidate() called.");
            }
        }
        session = request.getSession(true);
//
//        Rsa rsa = new Rsa(160);
//        session.setAttribute("rsa", rsa);
        UserInfoBean beanUserInfo = new UserInfoBean();
        session.setAttribute("beanUserInfo", beanUserInfo);
    }
}