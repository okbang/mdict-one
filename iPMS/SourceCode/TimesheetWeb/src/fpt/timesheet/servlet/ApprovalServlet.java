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
 * @Title:        ApprovalServlet.java
 * @Description:  Locate and forward the requests from the control pages.
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  November 18, 2002
 * @Modified date:
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import fpt.timesheet.InputTran.ejb.TimeSheetInfo;
import fpt.timesheet.bean.ExemptionInfoBean;
import fpt.timesheet.bean.ExemptionListBean;
import fpt.timesheet.bean.UserInfoBean;
import fpt.timesheet.bean.Approval.ChangePasswordBean;
import fpt.timesheet.bean.Approval.GLListBean;
import fpt.timesheet.bean.Approval.GLUpdateBean;
import fpt.timesheet.bean.Approval.PLListBean;
import fpt.timesheet.bean.Approval.PLUpdateBean;
import fpt.timesheet.bean.Approval.QAListBean;
import fpt.timesheet.bean.Approval.QAUpdateBean;
import fpt.timesheet.bean.Approval.TSAddBean;
import fpt.timesheet.bean.Approval.TSListBean;
import fpt.timesheet.bean.Approval.TSUpdateBean;
import fpt.timesheet.bean.Mapping.MappingDetailBean;
import fpt.timesheet.bean.Mapping.MappingListBean;
import fpt.timesheet.bean.Mapping.MappingSaveBean;
import fpt.timesheet.bean.Report.InquiryReportBean;
import fpt.timesheet.bo.Approval.PLApprovalBO;
import fpt.timesheet.bo.Approval.QAApprovalBO;
import fpt.timesheet.bo.Approval.TimesheetBO;
import fpt.timesheet.bo.Exemption.ExemptionBO;
import fpt.timesheet.bo.Login.LoginBO;
import fpt.timesheet.bo.Mapping.MappingBO;
import fpt.timesheet.constant.DATA;
import fpt.timesheet.constant.DefinitionList;
import fpt.timesheet.constant.TIMESHEET;
import fpt.timesheet.framework.util.CommonUtil.CommonUtil;
import fpt.timesheet.framework.util.SqlUtil.SqlUtil;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.servlet.core.BaseServlet;

// Added by HaiMM
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;

public class ApprovalServlet extends BaseServlet {
	private static Logger logger = Logger.getLogger(ApprovalServlet.class.getName());

	/**
	 * ApprovalServlet constructor.
	 */
	public ApprovalServlet() {
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
			String strAction = request.getParameter("hidActionDetail");
			if (TIMESHEET.DEBUG) {
				logger.info("hidActionDetail=" + strAction);
			}

			//Login
			if (TIMESHEET.LOGIN_ACTION.equals(strAction)) {
				int nResult = getGenericList(request, response);
				switch (nResult) {
					case 0:
						callPage(response, TIMESHEET.JSP_TIMESHEET_LIST, request);
						break;
					case 1:
						callPage(response, TIMESHEET.JSP_PL_APPROVE_LIST, request);
						break;
					case 2:
						callPage(response, TIMESHEET.JSP_QA_APPROVE_LIST, request);
						break;
					case 3:
						callPage(response, TIMESHEET.JSP_GL_APPROVE_LIST, request);
						break;
					case 4:
						callPage(response, TIMESHEET.JSP_MAPPING_LIST, request);
						break;
					case 5:
						callPage(response, TIMESHEET.JSP_REPORT_INQUIRY, request);
						break;
					case 6:
						callPage(response, TIMESHEET.JSP_EXEMPTION_LIST, request);
						break;
					default:
						// Check session and return Login Form
						if (checkSessionVariables(request,response) != null) {
							callPage(response, TIMESHEET.JSP_LOGIN, request);
						}
				}
				return;
			}

			//Check session variable first
			if (checkSessionVariables(request,response) == null) {
				return;
			}

			//HanhTN added 15/10/2006
			//Exemption List form
			else if (TIMESHEET.EXEMPTION_LIST.equals(strAction)) {
				getExemptionListServlet(request, response);
				callPage(response, TIMESHEET.JSP_EXEMPTION_LIST, request);
			}
			else if (TIMESHEET.EXEMPTION_DELETE.equals(strAction)) {
				deleteExemptionServlet(request, response);
				getExemptionListServlet(request, response);
				callPage(response, TIMESHEET.JSP_EXEMPTION_LIST, request);
			}
			else if (TIMESHEET.EXEMPTION_ADD.equals(strAction)) {
				addExemptionFormServlet(request, response);
				callPage(response, TIMESHEET.JSP_EXEMPTION_ADD, request);
			}
			else if (TIMESHEET.EXEMPTION_SAVE_ADD.equals(strAction)) {
				saveAddExemptionFormServlet(request, response);
				addExemptionFormServlet(request, response);
				callPage(response, TIMESHEET.JSP_EXEMPTION_ADD, request);
			}
			else if (TIMESHEET.EXEMPTION_UPDATE.equals(strAction)) {
				updateExemptionFormServlet(request, response);
				callPage(response, TIMESHEET.JSP_EXEMPTION_UPDATE, request);
			}
			else if (TIMESHEET.EXEMPTION_SAVE_UPDATE.equals(strAction)) {
				String strMessage = saveUpdateExemptionFormServlet(request, response);
				//If having an error message --> Display Message
				if (! strMessage.equals("")) {
					saveUpdateExemptionFormServlet(request, response);
					updateExemptionFormServlet(request, response);
					callPage(response, TIMESHEET.JSP_EXEMPTION_UPDATE, request);
				}
				//Successfully --> Don't Display Message
				else if (strMessage.equals("")) {
					saveUpdateExemptionFormServlet(request, response);
					getExemptionListServlet(request, response);
					callPage(response, TIMESHEET.JSP_EXEMPTION_LIST, request);
				}
			}

			//Timesheet List form
			else if (TIMESHEET.TS_LIST.equals(strAction)) {
				getTimesheetList(request, response);
				callPage(response, TIMESHEET.JSP_TIMESHEET_LIST, request);
			}
			else if (TIMESHEET.TS_ADD.equals(strAction)) {
				createTimesheetAddForm(request, response);
				callPage(response, TIMESHEET.JSP_TIMESHEET_ADD, request);
			}
			else if (TIMESHEET.TS_DELETE.equals(strAction)) {
				deleteTimesheet(request, response);
				getTimesheetList(request, response);
				callPage(response, TIMESHEET.JSP_TIMESHEET_LIST, request);
			}
			else if (TIMESHEET.TS_UPDATE.equals(strAction)) {
				createTimesheetUpdateForm(request, response);
				callPage(response, TIMESHEET.JSP_TIMESHEET_UPDATE, request);
			}

			//Timesheet Import form
			else if (TIMESHEET.TS_IMPORT.equals(strAction)) {
				//Reset information
				InquiryReportBean beanInquiryReport = new InquiryReportBean();
				request.setAttribute("beanInquiryReport", beanInquiryReport);
				callPage(response, TIMESHEET.JSP_TIMESHEET_IMPORT, request);
			}

			//Timesheet Add form
			else if (TIMESHEET.TS_SAVE_NEW.equals(strAction)) {
				addTimesheet(request, response);
				getTimesheetList(request, response);
				callPage(response, TIMESHEET.JSP_TIMESHEET_LIST, request);
			}

			//Timesheet Update form
			else if (TIMESHEET.TS_SAVE_UPDATE.equals(strAction)) {
				updateTimesheet(request, response);
				getTimesheetList(request, response);
				callPage(response, TIMESHEET.JSP_TIMESHEET_LIST, request);
			}

			//PL Approve List form
			else if (TIMESHEET.PL_LIST.equals(strAction)) {
				getPLList(request, response);
				callPage(response, TIMESHEET.JSP_PL_APPROVE_LIST, request);
			}
			else if (TIMESHEET.PL_APPROVE.equals(strAction) || TIMESHEET.PL_REJECT.equals(strAction)) {
				approveByPL(request, response, strAction);
				getPLList(request, response);
				callPage(response, TIMESHEET.JSP_PL_APPROVE_LIST, request);
			}
			else if (TIMESHEET.PL_UPDATE.equals(strAction)) {
				createPLUpdateForm(request, response);
				callPage(response, TIMESHEET.JSP_PL_APPROVE_UPDATE, request);
			}

			//PL Update form
			else if (TIMESHEET.PL_UPDATE_AND_APPROVE.equals(strAction)) {
				updateAndApproveByPL(request, response);
				getPLList(request, response);
				callPage(response, TIMESHEET.JSP_PL_APPROVE_LIST, request);
			}

			//////////////////////////////////////////////////////
			//QA Approve List form
			else if (TIMESHEET.QA_LIST.equals(strAction)) {
				getQAList(request, response);
				callPage(response, TIMESHEET.JSP_QA_APPROVE_LIST, request);
			}
			else if (TIMESHEET.QA_APPROVE.equals(strAction) || TIMESHEET.QA_REJECT.equals(strAction)) {
				approveByQA(request, response, strAction);
				getQAList(request, response);
				callPage(response, TIMESHEET.JSP_QA_APPROVE_LIST, request);
			}

			else if (TIMESHEET.QA_UPDATE.equals(strAction)) {
				createQAUpdateForm(request, response);
				callPage(response, TIMESHEET.JSP_QA_APPROVE_UPDATE, request);
			}

			//QA Update form
			else if (TIMESHEET.QA_UPDATE_AND_APPROVE.equals(strAction) || TIMESHEET.QA_UPDATE_ONLY.equals(strAction)) {
				updateAndApproveByQA(request, response, strAction);

				if (DATA.VIEW_REPORT_INQUIRY.equals(request.getParameter("hidTypeOfView"))) {
					ReportServlet servletReport = new ReportServlet();
					servletReport.getInquiryReport(request, response, this, false);
					callPage(response, TIMESHEET.JSP_REPORT_INQUIRY, request);
				}
				else {
					getQAList(request, response);
					callPage(response, TIMESHEET.JSP_QA_APPROVE_LIST, request);
				}
			}

			//////////////////////////////////////////////////////////////////////////////////////
			//GL Approve List form
			else if (TIMESHEET.GL_LIST.equals(strAction)) {
				getGLList(request, response);
				callPage(response, TIMESHEET.JSP_GL_APPROVE_LIST, request);
			}

			else if (TIMESHEET.GL_APPROVE.equals(strAction) || TIMESHEET.GL_REJECT.equals(strAction)) {
				approveByGL(request, response, strAction);
				getGLList(request, response);
				callPage(response, TIMESHEET.JSP_GL_APPROVE_LIST, request);
			}
			else if (TIMESHEET.GL_UPDATE.equals(strAction)) {
				createGLUpdateForm(request, response);
				callPage(response, TIMESHEET.JSP_GL_APPROVE_UPDATE, request);
			}

			//GL Update form
			else if (TIMESHEET.GL_UPDATE_AND_APPROVE.equals(strAction)) {
				updateAndApproveByGL(request, response);
				getGLList(request, response);
				callPage(response, TIMESHEET.JSP_GL_APPROVE_LIST, request);
			}

			//Change Password form
			else if (TIMESHEET.PASSWORD_CHANGE.equals(strAction)) {
				createChangePasswordForm(request, response);
				callPage(response, TIMESHEET.JSP_CHANGE_PASSWORD, request);
			}
			else if (TIMESHEET.PASSWORD_SAVE_NEW.equals(strAction)) {
				saveNewPassword(request, response);
				callPage(response, TIMESHEET.JSP_CHANGE_PASSWORD, request);
			}

			else if (TIMESHEET.MAPPING_LIST.equals(strAction)) {
				getMappingList(request, response);
				callPage(response, TIMESHEET.JSP_MAPPING_LIST, request);
			}
			else if (TIMESHEET.MAPPING_ADD.equals(strAction)) {
				createMappingDetailForm(request, response);
				callPage(response, TIMESHEET.JSP_MAPPING_DETAIL, request);
			}
			else if (TIMESHEET.MAPPING_UPDATE.equals(strAction)) {
				createMappingDetailForm(request, response);
				callPage(response, TIMESHEET.JSP_MAPPING_DETAIL, request);
			}
			else if (TIMESHEET.MAPPING_SAVE.equals(strAction)) {
				saveDetailForm(request, response);
				callPage(response, TIMESHEET.JSP_MAPPING_DETAIL, request);
			}
			else if (TIMESHEET.MAPPING_DELETE.equals(strAction)) {
				deleteMapping(request, response);
				callPage(response, TIMESHEET.JSP_MAPPING_LIST, request);
			}
			else if (TIMESHEET.QA_SAVE_JS.equals(strAction)) {
				saveJavaScript(request, response);
				callPage(response, TIMESHEET.JSP_MAPPING_LIST, request);
			}
			else {
				logger.debug("Invalid action: strAction = " + strAction);
				//sendErrorRedirect(request, response, TIMESHEET.JSP_ERROR, new Exception("Invalid action: strAction = " + strAction));
				getLoginForm(request, response);
				callPage(response, TIMESHEET.JSP_LOGIN, request);
			}
		}
		catch (Exception exception) {
			logger.debug("Exception in ApprovalServlet.performTask().");
			logger.error("ApprovalServlet - performTask(): ", exception);
			try {
				sendErrorRedirect(request, response, TIMESHEET.JSP_ERROR, exception);
			}
			catch (Exception e) {
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
	private void sendErrorRedirect(HttpServletRequest request, HttpServletResponse response, String errorPageURL, Throwable e) throws ServletException, IOException {
		request.setAttribute("javax.servlet.jsp.jspException", e);
		getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
	}

	/**
	 * Method getLoginForm
	 * Get a form to login
	 * @author  Nguyen Thai Son
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getLoginForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			setNoCaching(response);
			logger.debug("session.invalidate() called.");
		}
		session = request.getSession(true);

		UserInfoBean beanUserInfo = new UserInfoBean();
//		  Rsa rsa = new Rsa(160);
//		  session.setAttribute("rsa", rsa);
		session.setAttribute("beanUserInfo", beanUserInfo);
	}

	/**
	 * Method checkSessionVariables
	 * Reset session variables
	 * @author  Nguyen Thai Son.
	 * @version 24 October, 2002.
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws Exception
	 */
	private UserInfoBean checkSessionVariables(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, Exception {
        
		UserInfoBean beanUserInfo = null;
		try {
			HttpSession session = request.getSession();
			beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");
			String strLoginName = "";
			if (beanUserInfo != null) {
				strLoginName = beanUserInfo.getLoginName();
			}

//			if (beanUserInfo == null || strLoginName == null || strLoginName.length() <= 0) {
//				if (TIMESHEET.DEBUG) {
//					logger.debug("ApprovalServlet.checkSessionVariables(): Invalid session.");
//				}
//				getLoginForm(request,response);
//				// New "beanUserInfo" attribute of session is created after getLoginForm()
//				session = request.getSession();
//				beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");
//				beanUserInfo.setMessage(TIMESHEET.MSG_INVALID_SESSION);
//
//				callPage(response, TIMESHEET.JSP_LOGIN, request);
//				return null;
//			}
			if (beanUserInfo == null) {
				if (TIMESHEET.DEBUG) {
					logger.debug("ApprovalServlet.checkSessionVariables(): Invalid session.");
				}
				getLoginForm(request,response);
				// New "beanUserInfo" attribute of session is created after getLoginForm()
				session = request.getSession();
				beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");
				beanUserInfo.setMessage(TIMESHEET.MSG_INVALID_SESSION);

				callPage(response, TIMESHEET.JSP_LOGIN, request);
				return null;
			}
			if (strLoginName == null || strLoginName.length() <= 0) {
				if (TIMESHEET.DEBUG) {
					logger.debug("ApprovalServlet.checkSessionVariables(): You have no permission to login the system.");
				}
				getLoginForm(request,response);
				// New "beanUserInfo" attribute of session is created after getLoginForm()
				session = request.getSession();
				beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");
				beanUserInfo.setMessage(TIMESHEET.MSG_INVALID_USERPASS);

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
	 * Method getGenericList
	 * Get a list of timesheet
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private int getGenericList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int nResult = -1;
		UserInfoBean beanUserInfo;
		HttpSession session = request.getSession();
		//special code if called from the insight
//		  Rsa rsa;
//		  String key = request.getParameter("key");
//		  if (key != null) {
//			  rsa = Rsa.getRsaFromPool(key);
//			  session.setAttribute("rsa", rsa);
//		  }
//		  else {
//			  rsa = (Rsa) session.getAttribute("rsa");
//		  }

		String strUsername = request.getParameter("txtAccount");
		String strPassword = request.getParameter("txtPassword");
//		  }

		String strLocation = request.getParameter("Location");

		LoginBO boLogin = new LoginBO();
		beanUserInfo = boLogin.checkLogin(strUsername, strPassword, strLocation);

		session.setAttribute("beanUserInfo", beanUserInfo);

		String strTypeOfView = beanUserInfo.getTypeOfView();
		if (DATA.VIEW_TS_LIST.equals(strTypeOfView)) {
			getTimesheetList(request, response);
			nResult = 0;
		}
		else if (DATA.VIEW_PL_LIST.equals(strTypeOfView)) {
			getPLList(request, response);
			nResult = 1;
		}
		else if (DATA.VIEW_QA_LIST.equals(strTypeOfView)) {
			getQAList(request, response);
			nResult = 2;
		}
		else if (DATA.VIEW_GL_LIST.equals(strTypeOfView)) {
			getGLList(request, response);
			nResult = 3;
		}
		else if (DATA.VIEW_MAPPING_LIST.equals(strTypeOfView)) {
			getMappingList(request, response);
			nResult = 4;
		}
		else if (DATA.VIEW_REPORT_INQUIRY.equals(strTypeOfView)) {
			ReportServlet servletReport = new ReportServlet();
			servletReport.getInquiryReport(request, response, this, false);
			nResult = 5;
		}
		//HanhTN added 15/10/2006 --> add for list of exemption
		else if (DATA.VIEW_EXEMPTION.equals(strTypeOfView)) {
			getExemptionListServlet(request, response);
			nResult = 6;
		}
		return nResult;
	}

	/**
	 * Method getExemptionListServlet
	 * Get a list of an exemption
	 * @author Truong Ngoc Hanh
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getExemptionListServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");
		ExemptionInfoBean beanExemptionInfo = new ExemptionInfoBean();
		ExemptionListBean beanExemptionList = new ExemptionListBean();

		String strType = "0";
		String strGroupName = beanExemptionInfo.getGroupName();
		String strAccount = "";
		String strName = "";
		String strSearchFromDate = null;
		String strSearchToDate = null;
		String strMessage = null;

		//GROUP NAME
		if (request.getParameter("cboGroupNameList") != null) {
			strGroupName = request.getParameter("cboGroupNameList");
			session.setAttribute("cboGroupNameList", strGroupName);
		}
		else if (session.getAttribute("cboGroupNameList") != null) {
			strGroupName = (String) session.getAttribute("cboGroupNameList");
		}
		else {
			strGroupName = "All";
		}

		//ACCOUNT
		if (request.getParameter("txtDevAccount") != null) {
			strAccount = request.getParameter("txtDevAccount");
			session.setAttribute("txtDevAccount", strAccount);
		}
		else if (session.getAttribute("txtDevAccount") != null) {
			strAccount = (String) session.getAttribute("txtDevAccount");
		}

		//NAME
		if (request.getParameter("txtDevName") != null) {
			strName = request.getParameter("txtDevName");
			session.setAttribute("txtDevName", strName);
		}
		else if (session.getAttribute("txtDevName") != null) {
			strName = (String) session.getAttribute("txtDevName");
		}

		//EXEMPTION TYPE
		int intType = 0;
		if (request.getParameter("cboTypeList") != null) {
			strType = request.getParameter("cboTypeList");
			intType = Integer.parseInt(strType);
			session.setAttribute("cboTypeList", strType);
		}
		else if (session.getAttribute("cboTypeList") != null) {
			intType = Integer.parseInt((String) session.getAttribute("cboTypeList"));
		}

		//FROM DATE
		if (request.getParameter("txtFromDateList") != null) {
			strSearchFromDate = request.getParameter("txtFromDateList");
			session.setAttribute("txtFromDateList", strSearchFromDate);
		}
		else if (session.getAttribute("txtFromDateList") != null) {
			strSearchFromDate = (String) session.getAttribute("txtFromDateList");
		}
		
		//TO DATE
		if (request.getParameter("txtToDateList") != null) {
			strSearchToDate = request.getParameter("txtToDateList");
			session.setAttribute("txtToDateList", strSearchToDate);
		}
		else if (session.getAttribute("txtToDateList") != null) {
			strSearchToDate = (String) session.getAttribute("txtToDateList");
		}

		//PAGING
		int intCurrentPage = 0;
		if (request.getParameter("hidCurrentPage") != null) {
			intCurrentPage = Integer.parseInt(request.getParameter("hidCurrentPage"));
		}

		beanExemptionList.setCurrentPage(intCurrentPage);
		beanExemptionList.setGroupList();

		beanExemptionInfo.setGroupName(strGroupName);
		beanExemptionInfo.setDevAccount(strAccount);
		beanExemptionInfo.setDevName(strName);
		beanExemptionInfo.setType(intType);
		beanExemptionInfo.setSearchFromDate(strSearchFromDate);
		beanExemptionInfo.setSearchToDate(strSearchToDate);

		ExemptionBO boExemption = new ExemptionBO();
		strMessage = boExemption.getExistedAccountBO(beanExemptionInfo);

		beanExemptionList = boExemption.getExemptionListBO(beanUserInfo, beanExemptionList, beanExemptionInfo);

		request.setAttribute("beanExemptionInfo", beanExemptionInfo);
		request.setAttribute("beanExemptionList", beanExemptionList);
		request.setAttribute("ERROR_MESSAGE", strMessage);
	}

	/**
	 * Method addExemptionFormServlet
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	private void addExemptionFormServlet(HttpServletRequest request, 
										 HttpServletResponse response) throws Exception {

		//STEP 1 - check session to validate user information
		checkSessionVariables(request, response);

		//HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) request.getSession().getAttribute("beanUserInfo");

		ExemptionListBean beanExemptionList = new ExemptionListBean();
		beanExemptionList.setGroupList();
		beanExemptionList.setDevList();

		ExemptionInfoBean beanExemptionInfo = new ExemptionInfoBean();
		if ( request.getAttribute("beanExemptionInfo") != null ) {
			beanExemptionInfo = (ExemptionInfoBean) request.getAttribute("beanExemptionInfo");
		}
		else {
			beanExemptionInfo = new ExemptionInfoBean();
			beanExemptionInfo.setGroupName(beanUserInfo.getGroupName());
		}
		request.setAttribute("beanExemptionInfo", beanExemptionInfo);
		request.setAttribute("beanExemptionList", beanExemptionList);
	}

	/**
	 * Method updateExemptionFormServlet
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void updateExemptionFormServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String strExemptionId = request.getParameter("hidExemptionId");

		ExemptionInfoBean beanExemptionInfo = new ExemptionInfoBean();
		ExemptionListBean beanExemptionList = new ExemptionListBean();

		ExemptionBO boExemption = new ExemptionBO();
		beanExemptionInfo = boExemption.getExemptionByIdBO(strExemptionId);

		beanExemptionList.setGroupList();
		beanExemptionList.setDevList();
		beanExemptionInfo.setExemptionId(Integer.parseInt(strExemptionId));

		request.setAttribute("beanExemptionInfo", beanExemptionInfo);
		request.setAttribute("beanExemptionList", beanExemptionList);
	}

	/**
	 * Method saveExemptionFormServlet
	 * @param request
	 * @param response
	 * @return strMessage
	 * @throws Exception
	 */
	private String saveAddExemptionFormServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strMessage = null;

		String strSunday = request.getParameter("chkSunday");
		String strMonday = request.getParameter("chkMonday");
		String strTuesday = request.getParameter("chkTuesday");
		String strWednesday = request.getParameter("chkWednesday");
		String strThursday = request.getParameter("chkThursday");
		String strFriday = request.getParameter("chkFriday");
		String strSaturday = request.getParameter("chkSaturday");
		String strDevId = request.getParameter("cboDev");

		if (strDevId == null) {
			//Set strDevId = "1" in order to check for saving exemption's record if have not Developer  
			strDevId = "1";
		}
		if (strSunday == null) {
			strSunday = "";
		}
		if (strMonday == null) {
			strMonday = "";
		}
		if (strTuesday == null) {
			strTuesday = "";
		}
		if (strWednesday == null) {
			strWednesday = "";
		}
		if (strThursday == null) {
			strThursday = "";
		}
		if (strFriday == null) {
			strFriday = "";
		}
		if (strSaturday == null) {
			strSaturday = "";
		}

		ExemptionInfoBean beanExemptionInfo;
		if (request.getAttribute("beanExemptionInfo") != null) {
			beanExemptionInfo = (ExemptionInfoBean) request.getAttribute("beanExemptionInfo");
		}
		else {
			beanExemptionInfo = new ExemptionInfoBean();
		}
		beanExemptionInfo.setGroupName(request.getParameter("cboGroupName"));
		beanExemptionInfo.setDeveloperId(Integer.parseInt(strDevId));
		beanExemptionInfo.setType(Integer.parseInt(request.getParameter("radType")));

		beanExemptionInfo.setSunday(strSunday);
		beanExemptionInfo.setMonday(strMonday);
		beanExemptionInfo.setTuesday(strTuesday);
		beanExemptionInfo.setWednesday(strWednesday);
		beanExemptionInfo.setThursday(strThursday);
		beanExemptionInfo.setFriday(strFriday);
		beanExemptionInfo.setSaturday(strSaturday);

		beanExemptionInfo.setFromDate(request.getParameter("txtFromDate"));
		beanExemptionInfo.setToDate(request.getParameter("txtToDate"));
		beanExemptionInfo.setReason(request.getParameter("txtReason"));
		beanExemptionInfo.setNote(request.getParameter("txtNote"));

		ExemptionBO boExemption = new ExemptionBO();
		strMessage = boExemption.addExemptionFormBO(beanExemptionInfo);
		// Added by HaiMM ==========
		if (strMessage == "") {
			boExemption.addDummyExemptionFormBO(beanExemptionInfo);
		}
		// End =====================
		request.setAttribute("beanExemptionInfo", beanExemptionInfo);
		request.setAttribute("ERROR_MESSAGE", strMessage);

		return strMessage;
	}

	/**
	 * Method saveUpdateExemptionFormServlet
	 * @param request
	 * @param response
	 * @return strMessage
	 * @throws Exception
	 */
	private String saveUpdateExemptionFormServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strMessage = null;

		String strSunday = request.getParameter("chkSunday");
		String strMonday = request.getParameter("chkMonday");
		String strTuesday = request.getParameter("chkTuesday");
		String strWednesday = request.getParameter("chkWednesday");
		String strThursday = request.getParameter("chkThursday");
		String strFriday = request.getParameter("chkFriday");
		String strSaturday = request.getParameter("chkSaturday");

		if (strSunday == null) {
			strSunday = "";
		}
		if (strMonday == null) {
			strMonday = "";
		}
		if (strTuesday == null) {
			strTuesday = "";
		}
		if (strWednesday == null) {
			strWednesday = "";
		}
		if (strThursday == null) {
			strThursday = "";
		}
		if (strFriday == null) {
			strFriday = "";
		}
		if (strSaturday == null) {
			strSaturday = "";
		}

		ExemptionInfoBean beanExemptionInfo;
		if (request.getAttribute("beanExemptionInfo") != null) {
			beanExemptionInfo = (ExemptionInfoBean) request.getAttribute("beanExemptionInfo");
		}
		else {
			beanExemptionInfo = new ExemptionInfoBean();
		}
		String strExemptionId = request.getParameter("hidExemptionId");
		String strDevId = request.getParameter("hidDevId");

		beanExemptionInfo.setExemptionId(Integer.parseInt(strExemptionId));
		beanExemptionInfo.setDeveloperId(Integer.parseInt(strDevId));
		beanExemptionInfo.setType(Integer.parseInt(request.getParameter("radType")));

		beanExemptionInfo.setSunday(strSunday);
		beanExemptionInfo.setMonday(strMonday);
		beanExemptionInfo.setTuesday(strTuesday);
		beanExemptionInfo.setWednesday(strWednesday);
		beanExemptionInfo.setThursday(strThursday);
		beanExemptionInfo.setFriday(strFriday);
		beanExemptionInfo.setSaturday(strSaturday);

		beanExemptionInfo.setFromDate(request.getParameter("txtFromDate"));
		beanExemptionInfo.setToDate(request.getParameter("txtToDate"));
		beanExemptionInfo.setReason(request.getParameter("txtReason"));
		beanExemptionInfo.setNote(request.getParameter("txtNote"));

		ExemptionBO boExemption = new ExemptionBO();
		strMessage = boExemption.updateExemptionFormBO(beanExemptionInfo);

		request.setAttribute("beanExemptionInfo", beanExemptionInfo);
		request.setAttribute("ERROR_MESSAGE", strMessage);

		return strMessage;
	}

	/**
	 * Method deleteExemptionServlet
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void deleteExemptionServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String [] strExemptionId = request.getParameterValues("chkExemptionId");
		ExemptionBO boExemption = new ExemptionBO();
		boExemption.deleteExemptionBO(strExemptionId);
	}

	/**
	 * Method getTimesheetList
	 * Get timesheet list
	 * @author  Nguyen Thai Son
	 * @version November 19, 2002
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getTimesheetList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//STEP 1 - Validate session variables
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		TimesheetBO boTS = new TimesheetBO();
		TSListBean beanTSList = new TSListBean();

		//STEP 2 - Get data from client
		//PROJECT ID
		int intProjectID = 0;
		String strProjectID = request.getParameter("SearchProjectID");
		if (strProjectID != null) {
			intProjectID = Integer.parseInt(strProjectID);
			session.setAttribute("SearchProjectID", strProjectID);
		}
		else if (session.getAttribute("SearchProjectID") != null) {
			intProjectID = Integer.parseInt((String) session.getAttribute("SearchProjectID"));
		}

		//TIMESHEET STATUS
		int intStatus = 1;
		String strStatus = request.getParameter("SearchStatus");
		if (strStatus != null) {
			intStatus = Integer.parseInt(strStatus);
			session.setAttribute("SearchStatus", strStatus);
		}
		else if (session.getAttribute("SearchStatus") != null) {
			intStatus = Integer.parseInt((String) session.getAttribute("SearchStatus"));
		}

		//SORT BY
		int intSortBy = 1;
		String strSortBy = request.getParameter("SearchSortBy");
		if (strSortBy != null) {
			intSortBy = Integer.parseInt(strSortBy);
			session.setAttribute("SearchSortBy", strSortBy);
		}
		else if (session.getAttribute("SearchSortBy") != null) {
			intSortBy = Integer.parseInt((String) session.getAttribute("SearchSortBy"));
		}

		//FROM DATE
		String strFromDate = request.getParameter("FromDate");
		if (strFromDate != null) {
			session.setAttribute("FromDate", strFromDate);
		}
		else if (session.getAttribute("FromDate") != null) {
			strFromDate = (String) session.getAttribute("FromDate");
		}

		//TO DATE
		String strToDate = request.getParameter("ToDate");
		if (strToDate != null) {
			session.setAttribute("ToDate", strToDate);
		}
		else if (session.getAttribute("ToDate") != null) {
			strToDate = (String) session.getAttribute("ToDate");
		}

		//PAGING
		int intPage = 0;
		if (request.getParameter("hidCurrentPage") != null) {
			intPage = Integer.parseInt(request.getParameter("hidCurrentPage"));
		}

		beanTSList.setSearchProjectID(intProjectID);
		beanTSList.setSearchStatus(intStatus);
		beanTSList.setSearchSortBy(intSortBy);
		beanTSList.setSearchFromDate(strFromDate);
		beanTSList.setSearchToDate(strToDate);
		beanTSList.setCurrentPage(intPage);

		//STEP 3 - Using BO to get a list of timesheet
		beanTSList = boTS.getTimesheetList(beanUserInfo, beanTSList);

		//STEP 4 - Put result into client
		request.setAttribute("beanTSList", beanTSList);
	}

	/**
	 * Method getPLList
	 * Get timesheet list of members so that PL can approve it
	 * @author  Nguyen Thai Son
	 * @version November 19, 2002
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getPLList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//STEP 1 - Validate session
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		PLListBean beanPLList = new PLListBean();
		beanPLList.setProjectList(beanUserInfo.getRole(), beanUserInfo.getUserId());

		//STEP 2 - Get filter parameters from client
		//PROJECT ID
		String strProjectID = request.getParameter("PLProject");
		if (strProjectID != null) {
			session.setAttribute("PLProject", strProjectID);
		}
		else if (session.getAttribute("PLProject") != null) {
			strProjectID = (String) session.getAttribute("PLProject");
		}
		else {
			strProjectID = concatIDs(beanPLList.getProjectList(), 0);
		}

		//TIMESHEET STATUS
		String strStatus = request.getParameter("Status");
		if (strStatus != null) {
			beanPLList.setStatus(CommonUtil.StrToInt(strStatus));
			session.setAttribute("Status", strStatus);
		}
		else if (session.getAttribute("Status") != null) {
			beanPLList.setStatus(CommonUtil.StrToInt((String) session.getAttribute("Status")));
		}
		else {
			beanPLList.setStatus(DefinitionList.TS_UNAPPROVED);
		}

		//SORT BY
		String strSortBy = request.getParameter("Sortby");
		if (strSortBy != null) {
			beanPLList.setSortby(CommonUtil.StrToInt(strSortBy));
			session.setAttribute("Sortby", strSortBy);
		}
		else if (session.getAttribute("Sortby") != null) {
			beanPLList.setSortby(CommonUtil.StrToInt((String) session.getAttribute("Sortby")));
		}
		else {
			beanPLList.setSortby(1);
		}

		//FROM DATE
		String strFromDate = request.getParameter("apvFromDate");
		if (strFromDate != null) {
			session.setAttribute("plFromDate", strFromDate);
		}
		else if (session.getAttribute("plFromDate") != null) {
			strFromDate = (String) session.getAttribute("plFromDate");
		}

		//TO DATE
		String strToDate = request.getParameter("apvToDate");
		if (strToDate != null) {
			session.setAttribute("plToDate", strToDate);
		}
		else if (session.getAttribute("plToDate") != null) {
			strToDate = (String) session.getAttribute("plToDate");
		}

		//ACCOUNT
		String strAccount = request.getParameter("apvAccount");
		if (strAccount != null) {
			strAccount = strAccount.trim();
			session.setAttribute("plAccount", strAccount);
		}
		else if (session.getAttribute("plAccount") != null) {
			strAccount = (String) session.getAttribute("plAccount");
		}
		else {
			strAccount = "";
		}

		//Getting new searching
		String strFromSearch = request.getParameter("hidFromSearch");
		if (strFromSearch != null) {
			session.setAttribute("hidFromSearch", strFromSearch);
		}
		else if (session.getAttribute("hidFromSearch") != null) {
			strFromSearch = (String) session.getAttribute("hidFromSearch");
		}

		//The current page
		String strCurrentPage = "0";
		if (request.getParameter("hidPLCurrentPage") != null) {
			strCurrentPage = request.getParameter("hidPLCurrentPage");
			session.setAttribute("hidPLCurrentPage", strCurrentPage);
		}
		else if (session.getAttribute("hidPLCurrentPage") != null) {
			strCurrentPage = (String) session.getAttribute("hidPLCurrentPage");
		}

		beanPLList.setProject(strProjectID);
		beanPLList.setFromDate(strFromDate);
		beanPLList.setToDate(strToDate);
		beanPLList.setAccount(strAccount.toUpperCase());
		beanPLList.setCurrentPage(CommonUtil.StrToInt(strCurrentPage));

		PLApprovalBO boPL = new PLApprovalBO();
		beanPLList = boPL.getPLList(beanUserInfo, beanPLList);

		request.setAttribute("beanPLList", beanPLList);
	}

	/**
	 * Method getQAList
	 * Get timesheet list of members in projects so that QA can approve it
	 * @author  Nguyen Thai Son
	 * @version November 19, 2002
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getQAList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//STEP 1 - Validate session
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		QAListBean beanQAList = new QAListBean();
		beanQAList.setProjectList(beanUserInfo.getRole(), beanUserInfo.getUserId());

		//STEP 2 - Get filter parameters from client
		//PROJECT ID
		String strProjectID = request.getParameter("Project");
		if (strProjectID != null) {
			session.setAttribute("qaProject", strProjectID);
		}
		else if (session.getAttribute("qaProject") != null) {
			strProjectID = (String) session.getAttribute("qaProject");
		}
		else {
			strProjectID = concatIDs(beanQAList.getProjectList(), 0);
		}

		//TIMESHEET STATUS
		String strStatus = request.getParameter("Status");
		if (strStatus != null) {
			beanQAList.setStatus(CommonUtil.StrToInt(strStatus));
			session.setAttribute("qaStatus", strStatus);
		}
		else if (session.getAttribute("qaStatus") != null) {
			beanQAList.setStatus(CommonUtil.StrToInt((String) session.getAttribute("qaStatus")));
		}
		else {// Unapproved by QA
			beanQAList.setStatus(DefinitionList.LD_APPROVE_STATUS);
		}

		//SORT BY
		String strSortBy = request.getParameter("Sortby");
		if (strSortBy != null) {
			beanQAList.setSortby(CommonUtil.StrToInt(strSortBy));
			session.setAttribute("qaSortby", strSortBy);
		}
		else if (session.getAttribute("qaSortby") != null) {
			beanQAList.setSortby(CommonUtil.StrToInt((String) session.getAttribute("qaSortby")));
		}
		else {
			beanQAList.setSortby(1);
		}

		//FROM DATE
		String strFromDate = request.getParameter("apvFromDate");
		if (strFromDate != null) {
			session.setAttribute("qaFromDate", strFromDate);
		}
		else if (session.getAttribute("qaFromDate") != null) {
			strFromDate = (String) session.getAttribute("qaFromDate");
		}

		//TO DATE
		String strToDate = request.getParameter("apvToDate");
		if (strToDate != null) {
			session.setAttribute("qaToDate", strToDate);
		}
		else if (session.getAttribute("qaToDate") != null) {
			strToDate = (String) session.getAttribute("qaToDate");
		}

		//ACCOUNT
		String strAccount = request.getParameter("apvAccount");
		if (strAccount != null) {
			strAccount = strAccount.trim();
			session.setAttribute("qaAccount", strAccount);
		}
		else if (session.getAttribute("qaAccount") != null) {
			strAccount = (String) session.getAttribute("qaAccount");
		}
		else {
			strAccount = "";
		}

		//PAGING
		String strCurrentPage = "0";
		if (request.getParameter("hidCurrentPage") != null) {
			strCurrentPage = request.getParameter("hidCurrentPage");
			session.setAttribute("hidCurrentPage", strCurrentPage);
		}
		else if (session.getAttribute("hidCurrentPage") != null) {
			strCurrentPage = (String) session.getAttribute("hidCurrentPage");
		}

		beanQAList.setProject(strProjectID);
		beanQAList.setFromDate(strFromDate);
		beanQAList.setToDate(strToDate);
		beanQAList.setAccount(strAccount.toUpperCase());
		beanQAList.setCurrentPage(CommonUtil.StrToInt(strCurrentPage));

		QAApprovalBO boQA = new QAApprovalBO();
		beanQAList = boQA.getQAList(beanUserInfo, beanQAList);

		request.setAttribute("beanQAList", beanQAList);
	}

	/**
	 * Method getGLList
	 * @author  Nguyen Thai Son
	 * @version November 19, 2002
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getGLList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//STEP 1 - Validate session
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		GLListBean beanGLList = new GLListBean();
		beanGLList.setProjectList(beanUserInfo.getRole(), beanUserInfo.getUserId());

		//STEP 2 - Get filter parameters from client
		//PROJECT ID
		String strProjectID = request.getParameter("GLProject");
		if (strProjectID != null) {
			session.setAttribute("GLProject", strProjectID);
		}
		else if (session.getAttribute("GLProject") != null) {
			strProjectID = (String) session.getAttribute("GLProject");
		}
		else {
			strProjectID = concatIDs(beanGLList.getProjectList(), 0);
		}

		//TIMESHEET STATUS
		String strStatus = request.getParameter("Status");
		if (strStatus != null) {
			beanGLList.setStatus(CommonUtil.StrToInt(strStatus));
			session.setAttribute("glStatus", strStatus);
		}
		else if (session.getAttribute("glStatus") != null) {
			beanGLList.setStatus(CommonUtil.StrToInt((String) session.getAttribute("glStatus")));
		}
		else {
			beanGLList.setStatus(DefinitionList.TS_UNAPPROVED);
		}

		//SORT BY
		String strSortBy = request.getParameter("Sortby");
		if (strSortBy != null) {
			beanGLList.setSortby(CommonUtil.StrToInt(strSortBy));
			session.setAttribute("glSortby", strSortBy);
		}
		else if (session.getAttribute("glSortby") != null) {
			beanGLList.setSortby(CommonUtil.StrToInt((String) session.getAttribute("glSortby")));
		}
		else {
			beanGLList.setSortby(1);
		}

		//FROM DATE
		String strFromDate = request.getParameter("apvFromDate");
		if (strFromDate != null) {
			session.setAttribute("glFromDate", strFromDate);
		}
		else if (session.getAttribute("glFromDate") != null) {
			strFromDate = (String) session.getAttribute("glFromDate");
		}

		//TO DATE
		String strToDate = request.getParameter("apvToDate");
		if (strToDate != null) {
			session.setAttribute("glToDate", strToDate);
		}
		else if (session.getAttribute("glToDate") != null) {
			strToDate = (String) session.getAttribute("glToDate");
		}

		//ACCOUNT
		String strAccount = request.getParameter("apvAccount");
		if (strAccount != null) {
			strAccount = strAccount.trim();
			session.setAttribute("plAccount", strAccount);
		}
		else if (session.getAttribute("plAccount") != null) {
			strAccount = (String) session.getAttribute("plAccount");
		}
		else {
			strAccount = "";
		}

		//LISTING NAME
		String strRole = beanUserInfo.getRole();
		if (strRole.substring(2, 3).equals("1")) {
			if (strRole.substring(1, 2).equals("1")) {
				beanGLList.setListingName("GLPLOtherListing"); // GL + PL
			}
			else {
				beanGLList.setListingName("GLOtherListing");  // GL
			}
		}

		//GET NEW SEARCH
		String strFromSearch = request.getParameter("hidFromSearch");
		if (strFromSearch != null) {
			session.setAttribute("hidFromSearch", strFromSearch);
		}
		else if (session.getAttribute("hidFromSearch") != null) {
			strFromSearch = (String) session.getAttribute("hidFromSearch");
		}

		//PAGING
		String strCurrentPage = "0";
		if (request.getParameter("hidGLCurrentPage") != null) {
			strCurrentPage = request.getParameter("hidGLCurrentPage");
			session.setAttribute("hidGLCurrentPage", strCurrentPage);
		}
		else if (session.getAttribute("hidGLCurrentPage") != null) {
			strCurrentPage = (String) session.getAttribute("hidGLCurrentPage");
		}

		beanGLList.setProject(strProjectID);
		beanGLList.setFromDate(strFromDate);
		beanGLList.setToDate(strToDate);
		beanGLList.setAccount(strAccount.toUpperCase());
		beanGLList.setCurrentPage(CommonUtil.StrToInt(strCurrentPage));

		//Mapping filter parameters between PLListBean and GLListBean because of two BO objects are quite the same.
		PLListBean beanPLList = new PLListBean();
		beanPLList.setProjectList(beanGLList.getProjectList());
		beanPLList.setProject(beanGLList.getProject());
		beanPLList.setStatus(beanGLList.getStatus());
		beanPLList.setSortby(beanGLList.getSortby());
		beanPLList.setFromDate(beanGLList.getFromDate());
		beanPLList.setToDate(beanGLList.getToDate());
		beanPLList.setAccount(beanGLList.getAccount());
		beanPLList.setCurrentPage(beanGLList.getCurrentPage());
		beanPLList.setListingName(beanGLList.getListingName());

		PLApprovalBO boPL = new PLApprovalBO();
		beanPLList = boPL.getPLList(beanUserInfo, beanPLList);

		beanGLList.setTimesheetList(beanPLList.getTimesheetList());
		beanGLList.setTotalTimesheet(beanPLList.getTotalTimesheet());
		beanGLList.setTotalPage(beanPLList.getTotalPage());
		//added by MinhPT 03Oct13
		//for adjust nCurrentPage
		beanGLList.setCurrentPage(beanPLList.getCurrentPage());

		request.setAttribute("beanGLList", beanGLList);
	}

	/**
	 * Method createTimesheetAddForm
	 * @author  Nguyen Thai Son
	 * @version November 19, 2002
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void createTimesheetAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		TSAddBean beanTSAdd = new TSAddBean();
		MappingDetailBean beanMappingDetail;
		MappingBO boMapping = new MappingBO();
		beanMappingDetail = boMapping.getMappingForUse();
		request.setAttribute("beanTSAdd", beanTSAdd);
		request.setAttribute("beanMappingDetail", beanMappingDetail);
	}

	/**
	 * Method createTimesheetUpdateForm
	 * Create a new form to update timesheet
	 * @author  Nguyen Thai Son
	 * @version November 19, 2002
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void createTimesheetUpdateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		TSListBean beanTSList = new TSListBean();
		TSUpdateBean beanTSUpdate = new TSUpdateBean();
		TimesheetBO boTS = new TimesheetBO();

		//Selected ID
		String strSelectedID = request.getParameter("arrSelectedID");
		if (strSelectedID != null) {
			beanTSList.setSelectedTS(strSelectedID);
		}
		beanTSList = boTS.getTimesheetList(beanUserInfo, beanTSList);
		beanTSUpdate.setTimesheetList(beanTSList.getTimesheetList());

		request.setAttribute("beanTSUpdate", beanTSUpdate);

		MappingDetailBean beanMappingDetail;
		MappingBO boMapping = new MappingBO();
		beanMappingDetail = boMapping.getMappingForUse();
		request.setAttribute("beanMappingDetail", beanMappingDetail);
	}

	/**
	 * Method addTimesheet
	 * Add new timesheet into database
	 * @author  Nguyen Thai Son
	 * @version November 19, 2002
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void addTimesheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		TSAddBean beanTSAdd = new TSAddBean();

		String[] arrDate = request.getParameterValues("Date");
		int[] arrProject = StringArr2IntArr(request.getParameterValues("Project"));
		int[] arrProcess = StringArr2IntArr(request.getParameterValues("Process"));
		int[] arrTypeOfWork = StringArr2IntArr(request.getParameterValues("TypeOfWork"));
		int[] arrProduct = StringArr2IntArr(request.getParameterValues("Product"));
		String[] arrDuration = request.getParameterValues("Duration");
		String[] arrDescription = request.getParameterValues("Description");

		try {
			if (arrDate != null) {
				ArrayList arrListing = new ArrayList();
				for (int i=0; i<arrDate.length; i++) {
					arrDate[i].trim();
					arrDescription[i].trim();
					/**
					 * Modified by PhuongNT, Monday, September 29, 2003
					 * Add asynchronous index incrementation for arrProduct to avoid
					 * problem with null value for getParameterValue
					 */
					//HanhTN change --> set info to TimesheetInfo 
					if (arrDate[i].length() > 0 && 
						arrDuration[i].length() > 0 &&
						arrDescription[i].length() > 0 &&
						arrProject[i] != 0 && 
						arrProcess[i] != 0 && 
						arrTypeOfWork[i] != 0) 
					{
						TimeSheetInfo timeSheetInfo = new TimeSheetInfo();
						timeSheetInfo.setDate(arrDate[i]);
						timeSheetInfo.setProject(arrProject[i]);
						timeSheetInfo.setProcess(arrProcess[i]);
						timeSheetInfo.setTypeofWork(arrTypeOfWork[i]);
						timeSheetInfo.setWorkProduct(arrProduct[i]);
						timeSheetInfo.setDuration(Float.parseFloat(arrDuration[i]));
						timeSheetInfo.setDescription(CommonUtil.stringConvert(arrDescription[i]));
						arrListing.add(timeSheetInfo);
					}
				}//end for
				beanTSAdd.setTimesheetList(arrListing);

				TimesheetBO boTimeSheet = new TimesheetBO();
				boTimeSheet.addTimesheet(beanTSAdd, beanUserInfo);
				
				// Added by HaiMM ================
				boTimeSheet.addTimesheetDummy(beanUserInfo, TIMESHEET.INSERT);
				boTimeSheet.updateTimesheetDummy(beanUserInfo);
				// End ========================

			}//end if
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method updateTimesheet
	 * Update timesheet into database
	 * @author  Nguyen Thai Son
	 * @version November 19, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateTimesheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Added by HaiMM ================
		 HttpSession session = request.getSession();
		 UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");
	
		 // End ==========================
		
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		TSUpdateBean beanTSUpdate = new TSUpdateBean();

		int[] arrTimesheetID = StringArr2IntArr(request.getParameterValues("TimesheetID"));
		String[] arrDate = request.getParameterValues("Date");
		int[] arrProject = StringArr2IntArr(request.getParameterValues("Project"));
		int[] arrProcess = StringArr2IntArr(request.getParameterValues("Process"));
		int[] arrTypeOfWork = StringArr2IntArr(request.getParameterValues("TypeOfWork"));
		int[] arrProduct = StringArr2IntArr(request.getParameterValues("Product"));
		String[] arrDuration = request.getParameterValues("Duration");
		String[] arrDescription = request.getParameterValues("Description");

		if (arrDate != null) {
			ArrayList arrListing = new ArrayList();
			for (int i=0; i<arrDate.length; i++) {
				arrDate[i].trim();
				arrDescription[i].trim();
				//HanhTN change --> set info to TimesheetInfo
				if (arrDate[i].length() > 0 && 
					arrDuration[i].length() > 0 &&
					arrDescription[i].length() > 0 &&
					arrProject[i] != 0 && 
					arrProcess[i] != 0 && 
					arrTypeOfWork[i] != 0 && 
					arrProduct[i] != 0) 
				{
					TimeSheetInfo timeSheetInfo = new TimeSheetInfo();
					timeSheetInfo.setTimeSheetID(arrTimesheetID[i]);
					timeSheetInfo.setDate(arrDate[i]);
					timeSheetInfo.setProject(arrProject[i]);
					timeSheetInfo.setProcess(arrProcess[i]);
					timeSheetInfo.setTypeofWork(arrTypeOfWork[i]);
					timeSheetInfo.setWorkProduct(arrProduct[i]);
					timeSheetInfo.setDuration(Float.parseFloat(arrDuration[i]));
					timeSheetInfo.setDescription(CommonUtil.stringConvert(arrDescription[i]));
					arrListing.add(timeSheetInfo);
				}
			}
			beanTSUpdate.setTimesheetList(arrListing);

			TimesheetBO boTS = new TimesheetBO();
			boTS.updateTimesheet(beanTSUpdate);
			
			// Added by HaiMM ==========================
			Collection colUpdateTSList = beanTSUpdate.getTimesheetList();
			Collection colTSMigrationList = boTS.getTimesheetMigrateList();
			
			// Get exits or non-exits TimesheetID
			Collection colExitsList = new ArrayList();
			Collection colNonExitsList = new ArrayList();
			
			colExitsList = getExitsList(colUpdateTSList, colTSMigrationList);
			colNonExitsList = getNonExitsList(colUpdateTSList, colTSMigrationList);
			
			//	Update to Timsheet Migration for exits records
			if (colExitsList != null && !colExitsList.isEmpty()) {
				boTS.updateTimesheetMigrate(colExitsList, TIMESHEET.UPDATE);
			}
			
			// Add to TimeSheet Migration table for new records
			if (colNonExitsList != null && !colNonExitsList.isEmpty()) {
				boTS.addTimesheetByUpdateStatus(colNonExitsList, TIMESHEET.UPDATE);
			}
			
			// End ======================================
		}//end if
	}

	// Added by HaiMM ===================================
	 public Collection getExitsList(Collection colUpdateTSList, Collection colTSMigrationList)throws Exception {
		 Collection colExitsList = new ArrayList();
		 try {
			 if (colUpdateTSList != null && !colUpdateTSList.isEmpty()) {
				 Iterator it = colUpdateTSList.iterator();
				 while (it.hasNext()) {
					 TimeSheetInfo timeSheetUpdate = (TimeSheetInfo) it.next();
					 if (colTSMigrationList != null && !colTSMigrationList.isEmpty()) {
						 Iterator it_tmp = colTSMigrationList.iterator();
						 while (it_tmp.hasNext()) {
							 TimeSheetInfo timeSheetMigrate = (TimeSheetInfo) it_tmp.next();
							 if (timeSheetUpdate.getTimeSheetID() == timeSheetMigrate.getTimeSheetID()) {
								 colExitsList.add(timeSheetUpdate);
								 break;
							 }
						 }
					 }
				 }
			 }

		 } catch (Exception ex) {
			 logger.debug(
				 "Exception occurs in TimesheetBO.getExitsList(): "
					 + ex.toString());
			 logger.error(ex);
		 }
		 if (colExitsList != null && !colExitsList.isEmpty()) {
			 return colExitsList;
		 }
		 return colExitsList;
	 }
	
	 public Collection getNonExitsList(Collection colUpdateTSList, Collection colTSMigrationList)throws Exception {
		 Collection colNonExitsList = new ArrayList();
		 colNonExitsList.addAll(colUpdateTSList);
		 try {
			 if (colUpdateTSList != null && !colUpdateTSList.isEmpty()) {
				 Iterator it = colUpdateTSList.iterator();
				 while (it.hasNext()) {
					 TimeSheetInfo timeSheetUpdate = (TimeSheetInfo) it.next();
					 if (colTSMigrationList != null && !colTSMigrationList.isEmpty()) {
						 Iterator it_tmp = colTSMigrationList.iterator();
						 while (it_tmp.hasNext()) {
							 TimeSheetInfo timeSheetMigrate = (TimeSheetInfo) it_tmp.next();
							 if (timeSheetUpdate.getTimeSheetID() == timeSheetMigrate.getTimeSheetID()) {
								 colNonExitsList.remove(timeSheetUpdate);
								 break;
							 }
						 }
					 }
				 }
			 }

		 } catch (Exception ex) {
			 logger.debug(
				 "Exception occurs in TimesheetBO.getNonExitsList(): "
					 + ex.toString());
			 logger.error(ex);
		 }
		 if (colNonExitsList != null && !colNonExitsList.isEmpty()) {
			 return colNonExitsList;
		 }
		 return new ArrayList();
	 }
	
	// End ==========================================

	/**
	 * Delete timesheet
	 * @author  Nguyen Thai Son
	 * @version November 19, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteTimesheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		String strSelectedIDs = request.getParameter("arrSelectedID");
		if (strSelectedIDs != null && strSelectedIDs.length() > 0) {
			TSUpdateBean beanTSUpdate = new TSUpdateBean();
			TimesheetBO boTS = new TimesheetBO();
		
			
			
			// Added by HaiMM =================================================
			String[] arrTimesheetID = StringUtils.split(strSelectedIDs, ",");
			int[] arrIntTimesheetID = StringArr2IntArr(arrTimesheetID);
			int[] listNewTSId = new int[arrIntTimesheetID.length];
			for (int k=0;k<listNewTSId.length;k++)
			listNewTSId[k] = boTS.getNewTSid(arrTimesheetID[k]);
			
			boTS.deleteTimesheet(strSelectedIDs);
			if (arrTimesheetID != null) {
				ArrayList arrListing = new ArrayList();
				for (int i=0; i<arrTimesheetID.length; i++) {
					arrTimesheetID[i].trim();
						TimeSheetInfo timeSheetInfo = new TimeSheetInfo();
						timeSheetInfo.setTimeSheetID(arrIntTimesheetID[i]);
						arrListing.add(timeSheetInfo);
				}
				beanTSUpdate.setTimesheetList(arrListing);

				Collection colDeleteTSList = beanTSUpdate.getTimesheetList();
				Collection colTSMigrationList = boTS.getTimesheetMigrateList();
			
				// Get exits or non-exits TimesheetID
				Collection colExitsList = new ArrayList();
				Collection colNonExitsList = new ArrayList();
				
				Collection colExitsList1 = new ArrayList();//Old
				Collection colNonExitsList1 = new ArrayList();
				
				Collection colExitsList2 = new ArrayList();//New
				Collection colNonExitsList2 = new ArrayList();
			
				colExitsList = getExitsList(colDeleteTSList, colTSMigrationList);
				colNonExitsList = getNonExitsList(colDeleteTSList, colTSMigrationList);
				
				
				if (colExitsList != null && !colExitsList.isEmpty()) {
					Iterator it = colExitsList.iterator();
					while (it.hasNext()) {
						int flag = 0 ; 
						TimeSheetInfo timeSheetInfo = (TimeSheetInfo) it.next();
						for (int k=0;k<arrIntTimesheetID.length;k++) {
							if (arrIntTimesheetID[k]==timeSheetInfo.getTimeSheetID()) {
								if (listNewTSId[k]>0){
									timeSheetInfo.setTimeSheetID(listNewTSId[k]);
									colExitsList2.add(timeSheetInfo); 
								}
								else {
									colExitsList1.add(timeSheetInfo);
								}
								break;
							}
						}
					}
				}
				
				if (colNonExitsList != null && !colNonExitsList.isEmpty()) {
					Iterator it = colNonExitsList.iterator();
					while (it.hasNext()) {
						int flag = 0 ; 
						TimeSheetInfo timeSheetInfo = (TimeSheetInfo) it.next();
						for (int k=0;k<arrIntTimesheetID.length;k++) {
							if (arrIntTimesheetID[k]==timeSheetInfo.getTimeSheetID()) {
								if (listNewTSId[k]>0){
									timeSheetInfo.setTimeSheetID(listNewTSId[k]);
									colNonExitsList2.add(timeSheetInfo); 
								}
								else {
									colNonExitsList1.add(timeSheetInfo);
								}
								break;
							}
						}
					}
				}
				
				
				
				//	Update to Timsheet Migration for exits records
				if (colExitsList1 != null && !colExitsList1.isEmpty()) {
					boTS.updateTimesheetMigrate(colExitsList1, TIMESHEET.DELETE);
				}

				// Add to TimeSheet Migration table for new records
				if (colNonExitsList1 != null && !colNonExitsList1.isEmpty()) {
					boTS.addTimesheetByUpdateStatus(colNonExitsList1,TIMESHEET.DELETE);
				}
				
				
				if (colExitsList2 != null && !colExitsList2.isEmpty()) {
					boTS.updateTimesheetMigrate(colExitsList2, TIMESHEET.DELETE1);
				}

				// Add to TimeSheet Migration table for new records
				if (colNonExitsList2 != null && !colNonExitsList2.isEmpty()) {
					boTS.addTimesheetByUpdateStatus(colNonExitsList2,TIMESHEET.DELETE1);
				}
			//	End ==============================================================
			}//end if
		} 
	}

	/**
	 * Method approveByPL
	 * PL approves timesheet in his project.
	 * @author  Nguyen Thai Son
	 * @version November 20, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void approveByPL(HttpServletRequest request, HttpServletResponse response, String strAction) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		PLListBean beanPLList = new PLListBean();
		// For Approve, Reject or Update action
		beanPLList.setArrIdList(request.getParameterValues("check"));
		if (request.getParameterValues("check") != null) {
			String[] arrCheck = request.getParameterValues("ischeck");
			String[] tmpComment = new String[beanPLList.getArrIdList().length];
			String[] rComment = request.getParameterValues("comment");  //CommonUtil.decodeParameterValues(request, "comment", "UTF-8");

			for (int i = 0; i < arrCheck.length; i++) {
				if (!arrCheck[i].equals("-1")) {
					tmpComment[Integer.parseInt(arrCheck[i])] = rComment[i];
				}
			}
			beanPLList.setArrComment(tmpComment);
		}

		if (beanPLList.getArrIdList() != null && beanPLList.getArrIdList().length > 0) {
			beanPLList.setUpdateAction(strAction);
			PLApprovalBO boPL = new PLApprovalBO();
			boPL.approveByPL(beanUserInfo, beanPLList);
		}
	}

	/**
	 * Method createPLUpdateForm
	 * Create a new form so that PL can update his member's timesheet
	 * @author  Nguyen Thai Son
	 * @version November 20, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void createPLUpdateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);

		String[] arrID = request.getParameterValues("check");
		PLApprovalBO boPL = new PLApprovalBO();
		PLUpdateBean beanPLUpdate;
		beanPLUpdate = boPL.getPLUpdateInfo(arrID, DATA.VIEW_PL_LIST);

		request.setAttribute("beanPLUpdate", beanPLUpdate);

		MappingDetailBean beanMappingDetail;
		MappingBO boMapping = new MappingBO();
		beanMappingDetail = boMapping.getMappingForUse();
		request.setAttribute("beanMappingDetail", beanMappingDetail);
	}

	/**
	 * Method updateAndApproveByPL
	 * Approve and update timesheet by PL
	 * @author  Nguyen Thai Son
	 * @version November 20, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateAndApproveByPL(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		PLUpdateBean beanPLUpdate = new PLUpdateBean();
		String[] sIdList = request.getParameterValues("uId");
		String[] sProcessList = request.getParameterValues("Process");
		String[] sTypeList = request.getParameterValues("Type");
		String[] sProductList = request.getParameterValues("Product");
		String[] sDurationList = request.getParameterValues("Duration");
		String[] sDecriptionList = request.getParameterValues("Description");   //CommonUtil.decodeParameterValues(request, "Description", "UTF-8");
		String[] sDateList = request.getParameterValues("Date");
		int max = sIdList.length - 1;
		StringMatrix mtxList = new StringMatrix(max, 7);

		for (int i = 0; i < max; i++) {
			mtxList.setCell(i, 0, sIdList[i]);
			mtxList.setCell(i, 1, sProcessList[i]);
			mtxList.setCell(i, 2, sTypeList[i]);
			mtxList.setCell(i, 3, sProductList[i]);
			mtxList.setCell(i, 4, sDurationList[i]);
			mtxList.setCell(i, 5, sDecriptionList[i]);
			mtxList.setCell(i, 6, sDateList[i]);
		}
		beanPLUpdate.setUpdateList(mtxList);
		beanPLUpdate.setAction(DATA.PL_APPROVE);

		PLApprovalBO boPL = new PLApprovalBO();
		boPL.approveAndUpdateByPL(beanUserInfo, beanPLUpdate);
	}

	/**
	 * Method approveByQA
	 * QA approves timesheet in all projects.
	 * @author  Nguyen Thai Son
	 * @version November 20, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void approveByQA(HttpServletRequest request, HttpServletResponse response, String strAction) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		QAListBean beanQAList = new QAListBean();

		// For Approve, Reject or Update action
		beanQAList.setArrIdList(request.getParameterValues("check"));
		if (request.getParameterValues("check") != null) {
			String[] arrCheck = request.getParameterValues("ischeck");
			String[] arrSelectedComment = new String[beanQAList.getArrIdList().length];
			String[] arrComment = request.getParameterValues("comment");
			String[] arrSelectedProcess = new String[beanQAList.getArrIdList().length];
			String[] arrProcess = request.getParameterValues("hidProcess");
			String[] arrSelectedKPA = new String[beanQAList.getArrIdList().length];
			String[] arrKPA = request.getParameterValues("hidKPA");

			int nCheckIndex = -1;
			for (int i = 0; i < arrCheck.length; i++) {
				nCheckIndex = Integer.parseInt(arrCheck[i]);
				if (nCheckIndex != -1) {
					arrSelectedComment[nCheckIndex] = arrComment[i];
					arrSelectedProcess[nCheckIndex] = arrProcess[i];
					arrSelectedKPA[nCheckIndex] = arrKPA[i];
				}
			}
			beanQAList.setArrComment(arrSelectedComment);

			// Replace empty KPA by defalt KPA that get from Process-KPA mapping
			int nProcessID = 0;
			for (int i = 0; i < arrSelectedKPA.length; i++) {
				if (arrSelectedKPA[i].length() == 0) {
					nProcessID = Integer.parseInt(arrSelectedProcess[i]);
					arrSelectedKPA[i] = Integer.toString(SqlUtil.mapProcessKPA(nProcessID));
				}
			}
			beanQAList.setArrKPA_ID(arrSelectedKPA);
			// End: Replace empty KPA by defalt KPA
		}
		if (beanQAList.getArrIdList() != null && beanQAList.getArrIdList().length > 0) {
			beanQAList.setUpdateAction(strAction);
			QAApprovalBO boQA = new QAApprovalBO();
			boQA.approveByQA(beanUserInfo, beanQAList);
		}
	}

	/**
	 * Method createQAUpdateForm
	 * Create a new form so that QA can update timesheet
	 * @author  Nguyen Thai Son
	 * @version November 20, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void createQAUpdateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo =
				(UserInfoBean) session.getAttribute("beanUserInfo");

		String[] arrID = null;
		if (request.getParameterValues("check") != null) {
			arrID = request.getParameterValues("check");
		}
		else if (request.getParameter("TimesheetID") != null) {
			arrID = new String[1];
			arrID[0] = request.getParameter("TimesheetID");
		}

		//Update user information
		beanUserInfo.setTypeOfView(request.getParameter("hidTypeOfView"));
		request.getSession().setAttribute("beanUserInfo", beanUserInfo);

		QAApprovalBO boQA = new QAApprovalBO();
		QAUpdateBean beanQAUpdate;
		beanQAUpdate = boQA.getQAUpdateInfo(arrID);

		request.setAttribute("beanQAUpdate", beanQAUpdate);

		MappingDetailBean beanMappingDetail;
		MappingBO boMapping = new MappingBO();
		beanMappingDetail = boMapping.getMappingForUse();
		request.setAttribute("beanMappingDetail", beanMappingDetail);
	}

	/**
	 * Method updateAndApproveByQA
	 * Update and approve timesheet by QA
	 * @author  Nguyen Thai Son
	 * @version November 21, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateAndApproveByQA(HttpServletRequest request, HttpServletResponse response, String strAction) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		QAUpdateBean beanQAUpdate = new QAUpdateBean();
		String[] sIdList = request.getParameterValues("uId");
		String[] sProcessList = request.getParameterValues("Process");
		String[] sTypeList = request.getParameterValues("Type");
		String[] sProductList = request.getParameterValues("Product");
		String[] sKpaList = request.getParameterValues("Kpa");
		String[] sDateList = request.getParameterValues("Date");

		int max = sIdList.length - 1;
		StringMatrix mtxList = new StringMatrix(max, 6);
		for (int i = 0; i < max; i++) {
			mtxList.setCell(i, 0, sIdList[i]);
			mtxList.setCell(i, 1, sProcessList[i]);
			mtxList.setCell(i, 2, sTypeList[i]);
			mtxList.setCell(i, 3, sProductList[i]);
			mtxList.setCell(i, 4, sKpaList[i]);
			mtxList.setCell(i, 5, sDateList[i]);
		}
		beanQAUpdate.setUpdateList(mtxList);
		beanQAUpdate.setAction(strAction);

		QAApprovalBO boQA = new QAApprovalBO();
		boQA.updateAndApproveByQA(beanUserInfo, beanQAUpdate);
	}

	/**
	 * Method approveByGL
	 * GL approves timesheet in his project.
	 * @author  Nguyen Thai Son
	 * @version November 22, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void approveByGL(HttpServletRequest request, HttpServletResponse response, String strAction) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		GLListBean beanGLList = new GLListBean();
		// For Approve, Reject or Update action
		beanGLList.setArrIdList(request.getParameterValues("check"));
		if (request.getParameterValues("check") != null) {
			String[] arrCheck = request.getParameterValues("isRcheck");
			String[] tmpComment = new String[beanGLList.getArrIdList().length];
			String[] rComment = request.getParameterValues("comment");  //CommonUtil.decodeParameterValues(request, "comment", "UTF-8");

			for (int i = 0; i < arrCheck.length; i++) {
				if (!arrCheck[i].equals("-1")) {
					tmpComment[Integer.parseInt(arrCheck[i])] = rComment[i];
				}
			}
			beanGLList.setArrComment(tmpComment);
		}

		if (beanGLList.getArrIdList() != null && beanGLList.getArrIdList().length > 0) {
			beanGLList.setUpdateAction(strAction);

			//Mapping GLListBean with PLListBean
			PLListBean beanPLList = new PLListBean();
			beanPLList.setArrIdList(beanGLList.getArrIdList());
			beanPLList.setArrComment(beanGLList.getArrComment());
			beanPLList.setUpdateAction(beanGLList.getUpdateAction());

			PLApprovalBO boPL = new PLApprovalBO();
			boPL.approveByPL(beanUserInfo, beanPLList);
		}
	}

	/**
	 * Method createGLUpdateForm
	 * Create a new form so that GL can update his member's timesheet
	 * @author  Nguyen Thai Son
	 * @version November 22, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void createGLUpdateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);

		String[] arrID = request.getParameterValues("check");
		PLApprovalBO boPL = new PLApprovalBO();
		PLUpdateBean beanPLUpdate;
		beanPLUpdate = boPL.getPLUpdateInfo(arrID, DATA.VIEW_GL_LIST);

		GLUpdateBean beanGLUpdate = new GLUpdateBean();
		beanGLUpdate.setTimesheetList(beanPLUpdate.getTimesheetList());

		request.setAttribute("beanGLUpdate", beanGLUpdate);

		MappingDetailBean beanMappingDetail;
		MappingBO boMapping = new MappingBO();
		beanMappingDetail = boMapping.getMappingForUse();
		request.setAttribute("beanMappingDetail", beanMappingDetail);
	}

	/**
	 * Method updateAndApproveByGL
	 * Approve and update timesheet by GL
	 * @author  Nguyen Thai Son
	 * @version November 22, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateAndApproveByGL(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		GLUpdateBean beanGLUpdate = new GLUpdateBean();
		String[] sIdList = request.getParameterValues("uId");
		String[] sProcessList = request.getParameterValues("Process");
		String[] sTypeList = request.getParameterValues("Type");
		String[] sProductList = request.getParameterValues("Product");
		String[] sDurationList = request.getParameterValues("Duration");
		String[] sDecriptionList = request.getParameterValues("Description");   //CommonUtil.decodeParameterValues(request, "Description", "UTF-8");
		String[] sDateList = request.getParameterValues("Date");

		int max = sIdList.length - 1;
		StringMatrix mtxList = new StringMatrix(max, 7);
		for (int i = 0; i < max; i++) {
			mtxList.setCell(i, 0, sIdList[i]);
			mtxList.setCell(i, 1, sProcessList[i]);
			mtxList.setCell(i, 2, sTypeList[i]);
			mtxList.setCell(i, 3, sProductList[i]);
			mtxList.setCell(i, 4, sDurationList[i]);
			mtxList.setCell(i, 5, sDecriptionList[i]);
			mtxList.setCell(i, 6, sDateList[i]);
		}

		beanGLUpdate.setUpdateList(mtxList);
		beanGLUpdate.setAction(DATA.PL_APPROVE);

		//Mapping PLUpdateBean to GLUpdateBean
		PLUpdateBean beanPLUpdate = new PLUpdateBean();
		beanPLUpdate.setUpdateList(beanGLUpdate.getUpdateList());
		beanPLUpdate.setAction(beanGLUpdate.getAction());

		PLApprovalBO boPL = new PLApprovalBO();
		boPL.approveAndUpdateByPL(beanUserInfo, beanPLUpdate);
	}

	/**
	 * Method createChangePasswordForm
	 * Create a new form to change password
	 * @author  Nguyen Thai Son
	 * @version November 23, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void createChangePasswordForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);

		ChangePasswordBean beanChangePassword = new ChangePasswordBean();
		request.setAttribute("beanChangePassword", beanChangePassword);
	}

	/**
	 * Method saveNewPassword
	 * Save new password
	 * @author  Nguyen Thai Son
	 * @version November 23, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void saveNewPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		ChangePasswordBean beanChangePassword = new ChangePasswordBean();
		beanChangePassword.setOldPassword(request.getParameter("OldPassword")); //(CommonUtil.decodeParameter(request, "OldPassword", "UTF-8"));
		beanChangePassword.setNewPassword(request.getParameter("NewPassword")); //(CommonUtil.decodeParameter(request, "NewPassword", "UTF-8"));

		TimesheetBO boTS = new TimesheetBO();
		beanChangePassword = boTS.saveNewPassword(beanUserInfo, beanChangePassword);

		request.setAttribute("beanChangePassword", beanChangePassword);
	}

	/**
	 * Method getMappingList
	 * @author  Luong Hong Thai
	 * @version November 23, 2002
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Exception    If an exception occurred.
	 */
	private void getMappingList(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		MappingListBean beanMappingList = new MappingListBean();

		String strCurrentProcessID = request.getParameter("cboProcess");
		if (strCurrentProcessID != null) {
			session.setAttribute("cboProcess", strCurrentProcessID);
		}
		else if (session.getAttribute("cboProcess") != null) {
			strCurrentProcessID = (String) session.getAttribute("cboProcess");
		}
		else {
			strCurrentProcessID = DATA.PROCESS_NOTHING;
		}
		beanMappingList.setCurrentProcessID(strCurrentProcessID);
		MappingBO boMapping = new MappingBO();
		beanMappingList = boMapping.getMappingList(beanMappingList);
		session.setAttribute("beanMappingList", beanMappingList);
	}

	/**
	 * Method createMappingDetailForm.
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void createMappingDetailForm(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		MappingDetailBean beanMappingDetail = new MappingDetailBean();

		//CURRENT PROCESS ID
		String strCurrentProcessID = request.getParameter("cboProcess");
		if (strCurrentProcessID != null) {
			session.setAttribute("cboProcess", strCurrentProcessID);
		}
		else if (session.getAttribute("cboProcess") != null) {
			strCurrentProcessID = (String) session.getAttribute("cboProcess");
		}
		else {
			strCurrentProcessID = DATA.PROCESS_NOTHING;
		}

		//CURRENT PROCESS NAME
		String strCurrentProcessName = request.getParameter("hidCurrentProcessName");
		if (strCurrentProcessName != null) {
			session.setAttribute("hidCurrentProcessName", strCurrentProcessName);
		}
		else if (session.getAttribute("hidCurrentProcessName") != null) {
			strCurrentProcessName = (String) session.getAttribute("hidCurrentProcessName");
		}
		beanMappingDetail.setCurrentProcessName(strCurrentProcessName);

		//CURRENT MAPPING STATE
		String strCurrentMappingState = request.getParameter("hidMappingState");
		if (strCurrentMappingState != null) {
			session.setAttribute("hidMappingState", strCurrentMappingState);
		}
		else if (session.getAttribute("hidMappingState") != null) {
			strCurrentMappingState = (String) session.getAttribute("hidMappingState");
		}

		beanMappingDetail.setCurrentProcessID(strCurrentProcessID);
		beanMappingDetail.setCurrentState(strCurrentMappingState);

		MappingBO boMapping = new MappingBO();

		if ("Adding".equals(strCurrentMappingState)) {
			beanMappingDetail = boMapping.getMappingAddForm(beanMappingDetail);
		}
		else {
			beanMappingDetail = boMapping.getMappingUpdateForm(beanMappingDetail);
		}
		session.setAttribute("beanMappingDatail", beanMappingDetail);
	}

	/**
	 * Method saveDetailForm.
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void saveDetailForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		String strCurrentMappingState = request.getParameter("hidMappingState");
		if (strCurrentMappingState != null) {
			session.setAttribute("hidMappingState", strCurrentMappingState);
		}
		else if (session.getAttribute("hidMappingState") != null) {
			strCurrentMappingState = (String) session.getAttribute("hidMappingState");
		}

		MappingSaveBean beanMappingSave = new MappingSaveBean();
		//Getting current process
		String strCurrentProcessID = request.getParameter("cboProcess");
		if (strCurrentProcessID != null) {
			session.setAttribute("cboProcess", strCurrentProcessID);
		}
		else if (session.getAttribute("cboProcess") != null) {
			strCurrentProcessID = (String) session.getAttribute("cboProcess");
		}
		else {
			strCurrentProcessID = DATA.PROCESS_NOTHING;
		}
		beanMappingSave.setCurrentProcessID(strCurrentProcessID);

		//Getting CurrentWorkProduct list
		ArrayList alCurrentWorkProductIDList = new ArrayList();

		String strCurrentWorkProductIDList = request.getParameter("hidCurrentWorkProductIDList");
		if (strCurrentWorkProductIDList != null) {
			session.setAttribute("hidCurrentWorkProductIDList", strCurrentWorkProductIDList);
		}
		else if (session.getAttribute("hidCurrentWorkProductIDList") != null) {
			strCurrentWorkProductIDList = (String) session.getAttribute("hidCurrentWorkProductIDList");
		}
		else {
			strCurrentWorkProductIDList = "";
		}

		StringTokenizer stkCurrentWorkProductIDList = new StringTokenizer(strCurrentWorkProductIDList, ",");
		while (stkCurrentWorkProductIDList.hasMoreTokens()) {
			alCurrentWorkProductIDList.add(stkCurrentWorkProductIDList.nextToken().trim());
		}

		beanMappingSave.setCurrentWorkProductListID(alCurrentWorkProductIDList);

		MappingBO boMapping = new MappingBO();

		int nSavingResult = boMapping.saveDetailForm(beanMappingSave, strCurrentMappingState);

		MappingDetailBean beanMappingDetail = new MappingDetailBean();
		beanMappingDetail.setSavingResult(nSavingResult);
		beanMappingDetail.setCurrentProcessID(strCurrentProcessID);

		String strCurrentProcessName = request.getParameter("hidCurrentProcessName");
		if (strCurrentProcessName != null) {
			session.setAttribute("hidCurrentProcessName", strCurrentProcessName);
		}
		else if (session.getAttribute("hidCurrentProcessName") != null) {
			strCurrentProcessName = (String) session.getAttribute("hidCurrentProcessName");
		}
		beanMappingDetail.setCurrentProcessName(strCurrentProcessName);
		beanMappingDetail = boMapping.getMappingUpdateForm(beanMappingDetail);

		session.setAttribute("beanMappingDatail", beanMappingDetail);
	}

	/**
	 * Method deleteMapping.
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void deleteMapping(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		//Getting processID
		String strCurrentProcessID = request.getParameter("cboProcess");
		if (strCurrentProcessID != null) {
			session.setAttribute("cboProcess", strCurrentProcessID);
		}
		else if (session.getAttribute("cboProcess") != null) {
			strCurrentProcessID = (String) session.getAttribute("cboProcess");
		}
		else {
			strCurrentProcessID = DATA.PROCESS_NOTHING;
		}

		//Getting WorkProductID list
		ArrayList alCurrentWorkProductIDList = new ArrayList();

		String strCurrentWorkProductIDList = request.getParameter("hidCurrentWorkProductIDList");
		if (strCurrentWorkProductIDList != null) {
			session.setAttribute("hidCurrentWorkProductIDList", strCurrentWorkProductIDList);
		}
		else if (session.getAttribute("hidCurrentWorkProductIDList") != null) {
			strCurrentWorkProductIDList = (String) session.getAttribute("hidCurrentWorkProductIDList");
		}
		else {
			strCurrentWorkProductIDList = "";
		}

		StringTokenizer stkCurrentWorkProductIDList = new StringTokenizer(strCurrentWorkProductIDList, ",");
		while (stkCurrentWorkProductIDList.hasMoreTokens()) {
			alCurrentWorkProductIDList.add(stkCurrentWorkProductIDList.nextToken().trim());
		}

		MappingBO boMapping = new MappingBO();
		boMapping.deleteMapping(strCurrentProcessID, alCurrentWorkProductIDList);

		MappingListBean beanMappingList = new MappingListBean();

		beanMappingList.setCurrentProcessID(strCurrentProcessID);
		beanMappingList = boMapping.getMappingList(beanMappingList);
		session.setAttribute("beanMappingList", beanMappingList);
	}

	/**
	 * Method StringArr2IntArr.
	 * @param arrString
	 * @return int[]
	 */
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] StringArr2IntArr(String[] arrString) {
		if (arrString != null) {
			int size = arrString.length;
			int[] arrInt = new int[size];
			for (int nIndex = 0x00; nIndex < size; nIndex++) {
				try {
					arrInt[nIndex] = Integer.parseInt(arrString[nIndex]);
				}
				catch (Exception e) {
					arrInt[nIndex] = 0x00;
				}
			}
			return arrInt;
		}
		return null;
	}

	/**
	 * Method concatIDs.
	 * @param smList
	 * @param nPos
	 * @return String
	 */
	private static String concatIDs(StringMatrix smList, int nPos) {
        
		String strResult = "";
		StringBuffer strResultBuff = new StringBuffer();
		for (int i = 0; i < smList.getNumberOfRows(); i++) {
			strResultBuff.append(smList.getCell(i, nPos) + ",");
		}
		strResult = strResultBuff.toString();
		if (strResult.length() > 0)
			strResult = strResult.substring(0, strResult.length() - 1);
		return strResult;
	}

	/**
	 * Method saveJavaScript
	 * Process client request for save JavaScript by QA.
	 * @param   request javax.servlet.HttpServletRequest: the request object.
	 * @param   response javax.servlet.HttpServletResponse: the response object.
	 * @exception   Throwable    If an exception occurred.
	 */
	public void saveJavaScript(HttpServletRequest request, HttpServletResponse response) throws Exception {
		MappingBO boMapping = new MappingBO();
		MappingDetailBean beanMappingDetail = new MappingDetailBean();
		beanMappingDetail = boMapping.getMappingForSaveJavaScript(beanMappingDetail);
		QAApprovalBO boQA = new QAApprovalBO();
		String path = this.getServletContext().getRealPath("/scripts/").toString();
		boQA.saveJavaScript(beanMappingDetail, path);
	}
}