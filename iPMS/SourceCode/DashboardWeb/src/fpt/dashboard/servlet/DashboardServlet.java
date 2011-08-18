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
 
 package fpt.dashboard.servlet;

/**
 * @Title:        DashboardServlet.java
 * @Description:  Locate and forward the requests from the control pages.
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  October 23, 2002
 * @Modified date:
 */

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import fpt.dashboard.bean.UserInfoBean;
import fpt.dashboard.bo.Login.LoginBO;
import fpt.dashboard.constant.DB;
import fpt.dashboard.servlet.core.BaseServlet;
//import fpt.dashboard.util.CommonUtil.Rsa;


public class DashboardServlet extends BaseServlet
{
	private static Logger logger = Logger.getLogger(DashboardServlet.class.getName());
	/**
	 * DashboardServlet constructor.
	 */
	public DashboardServlet()
	{
	}
	/**
	 * Process client request by action.
	 * @author  Nguyen Thai Son
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	public void performTask(HttpServletRequest request, HttpServletResponse response) throws Throwable
	{
		try
		{
			String strAction = request.getParameter("hidAction");
			
			//Login form
			if (DB.LOGIN_ACTION.equals(strAction))
			{
				getLoginForm(request, response);
				callPage(response, DB.JSP_LOGIN, request);
				return;
			}
			else if (DB.DASHBOARD_HOMEPAGE_ACTION.equals(strAction))
			{
				int nResult = getDashboardHomepage(request, response);
				if (nResult == -1)	//login failed
					callPage(response, DB.JSP_LOGIN, request);
				else callPage(response, DB.JSP_HOMEPAGE, request);
			}
			else if (DB.PROJECT_MANAGEMENT_ACTION.equals(strAction))
			{
				//Go to ProjectManagementServlet
				callPage(response, DB.PROJECT_MANAGEMENT_SERVLET, request);
				return;
			}
			else if (DB.STAFF_MANAGEMENT_ACTION.equals(strAction))
			{
				//go to StaffManagementServlet
				callPage(response, DB.STAFF_MANAGEMENT_SERVLET, request);
				return;
			}
			else if (DB.RESOURCE_MANAGEMENT_ACTION.equals(strAction))
			{
				//go to ResourceManagementServlet
				callPage(response, DB.RESOURCE_MANAGEMENT_SERVLET, request);
				return;
			}
            //get the Rsa public key
//            else if(DB.GET_RSA_ACTION.equals(strAction)){
//                logger.info("DB.GET_RSA_ACTION");
//                Rsa.getAuthenticate(request,response);
//                return;
//            }
			else
			{
				//sendErrorRedirect(request, response, DB.JSP_ERROR, new Exception("Invalid action: strAction = " + strAction));
				getLoginForm(request, response);
				callPage(response, DB.JSP_LOGIN, request);
			}
			return;
		}
		catch (Exception exception)
		{
			logger.debug("Exception in DashboardServlet.performTask().");
			logger.error("DashboardServlet - performTask(): ", exception);
			try
			{
				sendErrorRedirect(request, response, DB.JSP_ERROR, exception);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
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
	private void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL, Throwable e)
		throws ServletException, IOException
	{
		request.setAttribute("javax.servlet.jsp.jspException", e);
		getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
	}
	
	/**
	 * Get a form to login
	 * @author  Nguyen Thai Son
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	public void getLoginForm(HttpServletRequest request, HttpServletResponse response) throws Throwable
	{
		HttpSession session = request.getSession(false);
		if (session != null)
		{
			session.invalidate();
			setNoCaching(response);
            if (DB.DEBUG) {
                logger.info("getLoginForm(): session invalidated");
            }
		}
        session = request.getSession(true);
        
//        Rsa rsa = new Rsa(160);
//        session.setAttribute("rsa", rsa);
		UserInfoBean beanUserInfo = new UserInfoBean();
		session.setAttribute("beanUserInfo", beanUserInfo);
	}
	
	/**
	 * Get Dashboard homepage
	 * @author  Nguyen Thai Son
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	public int getDashboardHomepage(HttpServletRequest request, HttpServletResponse response) throws Throwable
	{
        HttpSession session = request.getSession();
        //special code if called from the insight
//        Rsa rsa;
//        String key = request.getParameter("key");
//        if (key != null) {
//            rsa = Rsa.getRsaFromPool(key);
//            session.setAttribute("rsa", rsa);
//        }
//        else {
//            rsa = (Rsa) session.getAttribute("rsa");
//        }

        String strAccount = request.getParameter("txtAccount");
        String strPassword = request.getParameter("txtPassword");
    	if (strAccount == null || strPassword == null)	
    	{
    		getLoginForm(request, response);
    		return -1;
    	}
    	
    	UserInfoBean beanUserInfo = new UserInfoBean();
    	LoginBO boLogin = new LoginBO();
    	
    	beanUserInfo = boLogin.checkLogin(strAccount.toUpperCase(), strPassword);
    	
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        beanUserInfo.setDateLogin(formatter.format(new java.util.Date()));
    	session.setAttribute("beanUserInfo", beanUserInfo);
  		if (!"".equals(beanUserInfo.getMessage()))		return -1;  	
		
    	return 1;
	}
}