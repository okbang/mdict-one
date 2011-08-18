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
 * @Title:        StaffManagementServlet.java
 * @Description:  Locate and forward the requests from the control pages.
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  October 23, 2002
 * @Modified date:
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import fpt.dashboard.bean.UserInfoBean;
import fpt.dashboard.bean.StaffManagement.StaffAddBean;
import fpt.dashboard.bean.StaffManagement.StaffListBean;
import fpt.dashboard.bean.StaffManagement.StaffUpdateBean;
import fpt.dashboard.bo.ProjectManagememt.DeveloperBO;
import fpt.dashboard.constant.DB;
import fpt.dashboard.ProjectManagementTran.ejb.Constants;
import fpt.dashboard.servlet.core.BaseServlet;
//import fpt.dashboard.util.CommonUtil.Rsa;

public class StaffManagementServlet extends BaseServlet {
	private static Logger logger =
		Logger.getLogger(StaffManagementServlet.class.getName());
	/**
	 * StaffManagementServlet constructor.
	 */
	public StaffManagementServlet() {
	}
	/**
	 * Process client request by action.
	 * @author  Nguyen Thai Son
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	public void performTask(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Throwable {
		try {
			String strAction = request.getParameter("hidActionDetail");

			if (DB.VIEW_STAFF_LIST.equals(strAction)) {
				getStaffList(request, response);
				callPage(response, DB.JSP_STAFF_LIST, request);
			}
			//Staff List form
			else if (DB.STAFF_ADD.equals(strAction)) {
				createStaffAddForm(request, response, "");
				callPage(response, DB.JSP_STAFF_ADD, request);
			} else if (DB.STAFF_DELETE.equals(strAction)) {
				deleteStaff(request, response);
				getStaffList(request, response);
				callPage(response, DB.JSP_STAFF_LIST, request);
			} else if (DB.STAFF_UPDATE.equals(strAction)) {
				createStaffUpdateForm(request, response);
				callPage(response, DB.JSP_STAFF_UPDATE, request);
			} else if (DB.STAFF_SAVE_NEW.equals(strAction)) {
				saveStaff(request, response);
				callPage(response, DB.JSP_STAFF_ADD, request);
			} else if (DB.STAFF_SAVE_UPDATE.equals(strAction)) {
                updateStaff(request, response);
//				getStaffList(request, response);
//				callPage(response, DB.JSP_STAFF_LIST, request);
			} else {
				//sendErrorRedirect(request, response, DB.JSP_ERROR, new Exception("Invalid action: strAction = " + strAction));
				getLoginForm(request, response);
				callPage(response, DB.JSP_LOGIN, request);
			}
			return;
		} catch (Exception exception) {
			logger.debug("Exception in StaffManagementServlet.performTask().");
			logger.error("StaffManagementServlet - performTask(): ", exception);
			try {
				sendErrorRedirect(request, response, DB.JSP_ERROR, exception);
			} catch (Exception e) {
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
	private void sendErrorRedirect(
		HttpServletRequest request,
		HttpServletResponse response,
		String errorPageURL,
		Throwable e)
		throws ServletException, IOException {
		request.setAttribute("javax.servlet.jsp.jspException", e);
		getServletConfig()
			.getServletContext()
			.getRequestDispatcher(errorPageURL)
			.forward(request, response);
	}

	/**
	 * Get a form to login
	 * @author  Nguyen Thai Son
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	private void getLoginForm(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		if (session.getAttribute("beanUserInfo") != null) {
			session.invalidate();
			setNoCaching(response);
		}
        session = request.getSession(true);
        
//        Rsa rsa = new Rsa(160);
//        session.setAttribute("rsa", rsa);
		UserInfoBean beanUserInfo = new UserInfoBean();
		session.setAttribute("beanUserInfo", beanUserInfo);
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
	private UserInfoBean checkSessionVariables(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, Exception {
		UserInfoBean beanUserInfo = new UserInfoBean();

		try {
			HttpSession session = request.getSession();
			beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

			if (beanUserInfo == null) {
				logger.debug(
					"StaffManagementServlet.checkSessionVariables(): Invalid session.");

				getLoginForm(request, response);
				//display result page
				callPage(response, DB.JSP_LOGIN, request);
			}
		} catch (Exception exception) {
			logger.error("Exception in checkSessionVariables().", exception);
			sendErrorRedirect(request, response, DB.JSP_ERROR, exception);
		}

		return beanUserInfo;
	}

	/**
	 * Get a list of FSOFTers
	 * @author  Nguyen Thai Son
	 * @version November 13, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	private void getStaffList(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();

		String strRole = beanUserInfo.getSRole(); //formatted as "XXXXXXXXXX"

		//Group name
		String strGroup = "";
		if (request.getParameter("cboGroup") != null) {
			strGroup = request.getParameter("cboGroup");
			session.setAttribute("cboGroup", strGroup);
		} else if (request.getParameter("hidSelectedGroup") != null)
			strGroup = request.getParameter("hidSelectedGroup");
		else if (session.getAttribute("cboGroup") != null)
			strGroup = (String) session.getAttribute("cboGroup");
		else {
			strGroup = beanUserInfo.getGroupName();
			//set default view his group only.
			if (strRole.charAt(4) == '1') //PQA
				strGroup = Constants.GROUP_ALL; //PQA can view all groups
		}

		//Staff status
        String strStatus = Integer.toString(DB.DEVELOPER_SATUS_STAFF);
        if (request.getParameter("cboStaffStatus") != null) {
            strStatus = request.getParameter("cboStaffStatus");
            session.setAttribute("cboStaffStatus", strStatus);
        } else
            strStatus = (String) session.getAttribute("cboStaffStatus");

        // 01-Aug-2004: Sort the listing
        String strSortBy = "Account";   // Default sorting is Account column
        if (request.getParameter("hidSortBy") != null) {
            strSortBy = request.getParameter("hidSortBy");
            session.setAttribute("hidSortBy", strSortBy);
        }
        else if (session.getAttribute("hidSortBy") != null) {
            strSortBy = (String) session.getAttribute("hidSortBy");
        }

        String strDirection = "1";  // Default ascending (1)
        if (request.getParameter("hidDirection") != null) {
            strDirection = request.getParameter("hidDirection");
            session.setAttribute("hidDirection", strDirection);
        }
        else if (session.getAttribute("hidDirection") != null) {
            strDirection = (String) session.getAttribute("hidDirection");
        }

        String strAccount = "";
        if (request.getParameter("txtAccount") != null) {
            strAccount = request.getParameter("txtAccount");
            session.setAttribute("txtAccount", strAccount);
        }
        else if (session.getAttribute("txtAccount") != null) {
            strAccount = (String) session.getAttribute("txtAccount");
        }

        String strName = "";
        if (request.getParameter("txtName") != null) {
            strName = request.getParameter("txtName");
            session.setAttribute("txtName", strName);
        }
        else if (session.getAttribute("txtName") != null) {
            strName = (String) session.getAttribute("txtName");
        }

        String strPageNumber = "0";
        if (request.getParameter("lstPage") != null) {
            strPageNumber = request.getParameter("lstPage");
            session.setAttribute("lstPage", strPageNumber);
        }
        else if (session.getAttribute("lstPage") != null) {
            strPageNumber = (String) session.getAttribute("lstPage");
        }

		StaffListBean beanStaffList = new StaffListBean();
        beanStaffList.setSelectedGroup(strGroup);
        beanStaffList.setStatus(strStatus);
        beanStaffList.setSortBy(strSortBy);
        beanStaffList.setDirection(strDirection);
        beanStaffList.setAccount(strAccount);
        beanStaffList.setName(strName);
        if ((strPageNumber != null) && (! "".equals(strPageNumber))) {
            beanStaffList.setPageNumber(Integer.parseInt(strPageNumber));
        }
        
		DeveloperBO boStaff = new DeveloperBO();
		beanStaffList = boStaff.getStaffList(beanStaffList);

		///////////////////////////////////////////////
		request.setAttribute("beanStaffList", beanStaffList);
	}

	/**
	 * Create a form to add a new staff
	 * @author  Nguyen Thai Son
	 * @version November 13, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	private void createStaffAddForm(HttpServletRequest request,
		                            HttpServletResponse response,String strMsg)
                            		throws Exception {
		checkSessionVariables(request, response);
        UserInfoBean beanUserInfo =
            (UserInfoBean) request.getSession().getAttribute("beanUserInfo");

		StaffAddBean beanStaffAdd = new StaffAddBean();
		DeveloperBO boStaff = new DeveloperBO();
		beanStaffAdd = boStaff.createStaffAddForm();
		beanStaffAdd.setMessage(strMsg);
        beanStaffAdd.setStartDate(beanUserInfo.getDateLogin());
		request.setAttribute("beanStaffAdd", beanStaffAdd);
	}

	/**
	 * Delete staff
	 * @author  Nguyen Thai Son
	 * @version November 13, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	private void deleteStaff(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		checkSessionVariables(request, response);

		String[] arrID = request.getParameterValues("dev_id");
		DeveloperBO boStaff = new DeveloperBO();
		boStaff.deleteStaff(arrID);
	}

	/**
	 * Create a form to update a staff
	 * @author  Nguyen Thai Son
	 * @version November 13, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	private void createStaffUpdateForm(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		checkSessionVariables(request, response);

		String strID = request.getParameter("hidStaffID");
		StaffUpdateBean beanStaffUpdate = new StaffUpdateBean();
		DeveloperBO boStaff = new DeveloperBO();
		beanStaffUpdate = boStaff.getStaffInfo(strID);
		request.setAttribute("beanStaffUpdate", beanStaffUpdate);
	}

	/**
	 * Save a staff
	 * @author  Nguyen Thai Son
	 * @version November 13, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	private int saveStaff(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		int nResult = 0;
		checkSessionVariables(request, response);
        UserInfoBean beanUserInfo =
            (UserInfoBean) request.getSession().getAttribute("beanUserInfo");

        DeveloperBO boStaff = new DeveloperBO();
		StaffAddBean beanStaffAdd = boStaff.createStaffAddForm();
		beanStaffAdd.setName(request.getParameter("name"));   //(CommonUtil.decodeParameter(request, "name", "UTF-8"));
		beanStaffAdd.setDesignation(request.getParameter("design"));  //(CommonUtil.decodeParameter(request, "design", "UTF-8"));
		beanStaffAdd.setRole(request.getParameter("role"));
		beanStaffAdd.setAccount(request.getParameter("acc")); //(CommonUtil.decodeParameter(request, "acc", "UTF-8"));
        beanStaffAdd.setPassword(request.getParameter("pass")); //(CommonUtil.decodeParameter(request, "pass", "UTF-8"));
		beanStaffAdd.setGroup(request.getParameter("group"));
		beanStaffAdd.setStatus(request.getParameter("status"));
        beanStaffAdd.setEmail(request.getParameter("email"));   //(CommonUtil.decodeParameter(request, "email", "UTF-8"));
        beanStaffAdd.setStartDate(request.getParameter("startDate"));

		nResult = boStaff.saveStaff(beanStaffAdd);
        // Account existed, keep infomations and send error message
        if (nResult == -1) {
            beanStaffAdd.setMessage("Account already existed");
        }
        // Successful, clear fields
        else {
            beanStaffAdd.setMessage("");
            
            beanStaffAdd.setName("");
            beanStaffAdd.setDesignation("");
            beanStaffAdd.setRole("");
            beanStaffAdd.setAccount("");
            beanStaffAdd.setPassword("");
            beanStaffAdd.setGroup("");
            beanStaffAdd.setStatus("");
            beanStaffAdd.setEmail("");
            beanStaffAdd.setStartDate(beanUserInfo.getDateLogin());
        }
        request.setAttribute("beanStaffAdd", beanStaffAdd);
		return nResult;
	}

	/**
	 * Update a staff
	 * @author  Nguyen Thai Son
	 * @version November 13, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	private void updateStaff(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
        int nResult = -1;
		checkSessionVariables(request, response);

        DeveloperBO boStaff = new DeveloperBO();
		//StaffUpdateBean beanStaffUpdate = new StaffUpdateBean();
        StaffUpdateBean beanStaffUpdate = boStaff.getStaffInfo(request.getParameter("hidStaffID"));
        
		beanStaffUpdate.setDevID(request.getParameter("hidStaffID"));
		beanStaffUpdate.setName(request.getParameter("name"));    //(CommonUtil.decodeParameter(request, "name", "UTF-8"));
		beanStaffUpdate.setDesignation(request.getParameter("design"));   //(CommonUtil.decodeParameter(request, "design", "UTF-8"));
		beanStaffUpdate.setRole(request.getParameter("role"));
		if (request.getParameter("acc") != null)
			beanStaffUpdate.setAccount(request.getParameter("acc").toUpperCase());   //(CommonUtil.decodeParameter(request, "acc", "UTF-8").toUpperCase());
		else
			beanStaffUpdate.setAccount(request.getParameter("acc"));
		//beanStaffUpdate.setPassword(request.getParameter("pass"));    //(CommonUtil.decodeParameter(request, "pass", "UTF-8"));
		beanStaffUpdate.setGroup(request.getParameter("group"));
		beanStaffUpdate.setStatus(request.getParameter("status"));
        beanStaffUpdate.setEmail(request.getParameter("email"));    //(CommonUtil.decodeParameter(request, "email", "UTF-8"));
        beanStaffUpdate.setStartDate(request.getParameter("startDate"));
        beanStaffUpdate.setQuitDate(request.getParameter("quitDate"));
        
		nResult = boStaff.updateStaff(beanStaffUpdate);
        // Update failed -> return update page and display error message
        if (nResult == -1) {
            request.setAttribute("beanStaffUpdate", beanStaffUpdate);
            request.setAttribute(DB.ATT_ERROR_MESSAGE, "Update failed, may be account already existed");
            callPage(response, DB.JSP_STAFF_UPDATE, request);
        }
        // Successful -> return project listing page
        else {
            getStaffList(request, response);
            callPage(response, DB.JSP_STAFF_LIST, request);
        }
	}
}