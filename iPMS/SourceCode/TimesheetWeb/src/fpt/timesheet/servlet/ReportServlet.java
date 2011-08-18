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
 * @Title:        ReportServlet.java
 * @Description:  Locate and forward the requests from the control pages.
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  November 18, 2002
 * @Modified date:
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import fpt.timesheet.bean.UserInfoBean;
import fpt.timesheet.bean.Report.InquiryReportBean;
import fpt.timesheet.bean.Report.SummaryReportBean;
import fpt.timesheet.bean.Report.WeeklyReportBean;
import fpt.timesheet.bo.Report.ReportBO;
import fpt.timesheet.constant.DefinitionList;
import fpt.timesheet.constant.TIMESHEET;
import fpt.timesheet.constant.Timesheet;
import fpt.timesheet.framework.util.CommonUtil.CommonUtil;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.servlet.core.BaseServlet;
//import fpt.timesheet.util.Rsa;

import java.util.Date;

public class ReportServlet extends BaseServlet {
    private static Logger logger = Logger.getLogger(ReportServlet.class.getName());

    /**
     * ReportServlet constructor.
     */
    public ReportServlet() {
    }

	/**
     * Method performTask
     * Process client request by action.
     * @author  Nguyen Thai Son
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Throwable    If an exception occurred.
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        try {
            //Check session variable first
            if (checkSessionVariables(request,response) == null) {
                return;
            }
            String strAction = request.getParameter("hidActionDetail");

            if (TIMESHEET.DEBUG) {
                logger.info("hidActionDetail=" + strAction);
            }
            if (TIMESHEET.REPORT_INQUIRY.equals(strAction)) {
                getInquiryReport(request, response, this, false);
                callPage(response, TIMESHEET.JSP_REPORT_INQUIRY, request);
            }
            else if (TIMESHEET.REPORT_SUMMARY.equals(strAction)) {
                getSummaryReport(request, response);
				//logger.debug("getSummaryReport is called");
				callPage(response, TIMESHEET.JSP_REPORT_SUMMARY, request);
            }
			else if (TIMESHEET.REPORT_LACKTS_GROUP.equals(strAction)) {
				getTrackingReportServlet(request, response);
				//getTrackingReportGroupServlet(request, response);
			}
            else if (TIMESHEET.REPORT_INQUIRY_EXPORT.equals(strAction)) {
                getInquiryReport(request, response, this, true);
				//logger.debug("getInquiryReport is called");
            }
            else {
                logger.debug("Invalid action: strAction = " + strAction);
                getLoginForm(request, response);
                callPage(response, TIMESHEET.JSP_LOGIN, request);
            }
        }
        catch (Exception exception) {
            logger.debug("Exception in ReportServlet.performTask().");
            logger.error("ReportServlet - performTask(): ", exception);
            try {
                sendErrorRedirect(request, response, TIMESHEET.JSP_ERROR, exception);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method sendErrorRedirect
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
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void getLoginForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            setNoCaching(response);
            if (TIMESHEET.DEBUG) {
                logger.debug("session.invalidate() called.");
            }
        }
        session = request.getSession(true);

        UserInfoBean beanUserInfo = new UserInfoBean();
//        Rsa rsa = new Rsa(160);
//        session.setAttribute("rsa", rsa);
        session.setAttribute("beanUserInfo", beanUserInfo);
    }

    /**
     * Method checkSessionVariables
     * Reset session variables
     * @author  Nguyen Thai Son.
     * @version 24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   IOException    If an input or output exception occurred.
     * @exception   ServletException    If a servlet exception occurred.
     */
    private UserInfoBean checkSessionVariables(HttpServletRequest request, HttpServletResponse response) throws ServletException, Exception {
        UserInfoBean beanUserInfo = null;

        try {
            HttpSession session = request.getSession();
            beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");
            String strLoginName = "";
            if (beanUserInfo != null) {
                strLoginName = beanUserInfo.getLoginName();
            }
            
            if (beanUserInfo == null || strLoginName == null || strLoginName.length() <= 0) {
                if (TIMESHEET.DEBUG) {
                    logger.debug("ReportServlet.checkSessionVariables(): Invalid session.");
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
     * Method getInquiryReport
     * Get an inquiry report
     * @author  Nguyen Thai Son
     * @version	November 22, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @param   bForExport boolean: Get report for export or no.
     * @exception   Exception    If an exception occurred.
     */
    public void getInquiryReport(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet, boolean bIsForExport) throws Exception {
        //STEP 1 - Check session and initialize combo boxes
        //UserInfoBean beanUserInfo = checkSessionVariables(request, response);
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

        InquiryReportBean beanInquiryReport = new InquiryReportBean();
        beanInquiryReport.setGroupList();
        beanInquiryReport.setProjectList(beanUserInfo.getRole(), beanUserInfo.getUserId(), DefinitionList.INQUIRY_REPORT_TYPE);

        //STEP 2 - Get filter parameters from client//////////////////////////////////////
        String strProjectID = beanInquiryReport.getProjectList().getCell(0, 0);
        String strStatus = "1";
        //String strGroup = "All";
        String strGroup = beanUserInfo.getGroupName();
        String strCurrentPage = "0";
        String strFromDate = null;
        String strToDate = null;
        String strAccount = "";
        String strApprover = "";
        String strSortby = "1";

        if ("false".equals(session.getAttribute("irIsFirstTime"))) {
            //Project ID
            if (request.getParameter("ircboProject") != null) {
                strProjectID = request.getParameter("ircboProject");
                session.setAttribute("irProject", strProjectID);
            }
            else if (session.getAttribute("irProject") != null) {
				strProjectID = (String) session.getAttribute("irProject");
            }

            //Timesheet status
            if (request.getParameter("ircboStatus") != null) {
                strStatus = request.getParameter("ircboStatus");
                session.setAttribute("irStatus", strStatus);
            }
            else if (session.getAttribute("irStatus") != null) {
				strStatus = (String) session.getAttribute("irStatus");
            }

            //User group
            if (request.getParameter("ircboGroup") != null) {
                strGroup = request.getParameter("ircboGroup");
                session.setAttribute("irGroup", strGroup);
            }
            else if (request.getParameter("hidGroup") != null) {
                strGroup = request.getParameter("hidGroup");
                session.setAttribute("irGroup", strGroup);
            }
            else if (session.getAttribute("irGroup") != null) {
				strGroup = (String) session.getAttribute("irGroup");
            }
            else {
				strGroup = beanUserInfo.getGroupName();	//set default view his group only.
            }

            //From date
            strFromDate = request.getParameter("FromDate");
            if (strFromDate != null) {
				session.setAttribute("irFromDate", strFromDate);
            }
            else if (session.getAttribute("irFromDate") != null) {
				strFromDate = (String) session.getAttribute("irFromDate");
            }

            //To date
            strToDate = request.getParameter("ToDate");
            if (strToDate != null) {
				session.setAttribute("irToDate", strToDate);
            }
            else if (session.getAttribute("irToDate") != null) {
				strToDate = (String) session.getAttribute("irToDate");
            }

            //Account
            strAccount = request.getParameter("Account");
            if (strAccount != null) {
				session.setAttribute("irAccount", strAccount);
            }
            else if (session.getAttribute("irAccount") != null) {
				strAccount = (String) session.getAttribute("irAccount");
            }
            else {
				strAccount = "";
            }                

            //Approver
            strApprover = request.getParameter("Approver");
            if (strApprover != null) {
				session.setAttribute("irApprover", strApprover);
            }
            else if (session.getAttribute("irApprover") != null) {
				strApprover = (String) session.getAttribute("irApprover");
            }
            else {
				strApprover = "";
            }

            //Sort by
            strSortby = request.getParameter("Sortby");
            if (strSortby != null) {
				session.setAttribute("irSortby", strSortby);
            }                
            else if (session.getAttribute("irSortby") != null) {
				strSortby = (String) session.getAttribute("irSortby");
            }

            //The current page
            if (request.getParameter("hidCurrentPage") != null) {
                strCurrentPage = request.getParameter("hidCurrentPage");
                session.setAttribute("hidCurrentPage", strCurrentPage);
            }
            else if (session.getAttribute("hidCurrentPage") != null) {
				strCurrentPage = (String) session.getAttribute("hidCurrentPage");
            }
        }
        session.setAttribute("irIsFirstTime", "false");

		beanInquiryReport.setGroup(strGroup);
        beanInquiryReport.setProject(CommonUtil.StrToInt(strProjectID));
        beanInquiryReport.setProjectID(strProjectID);
        beanInquiryReport.setStatus(CommonUtil.StrToInt(strStatus));
        beanInquiryReport.setFromDate(strFromDate);
        beanInquiryReport.setToDate(strToDate);
        beanInquiryReport.setAccount(strAccount);
        beanInquiryReport.setApprover(strApprover);
        beanInquiryReport.setSortby(CommonUtil.StrToInt(strSortby));
        beanInquiryReport.setCurrentPage(CommonUtil.StrToInt(strCurrentPage));

        //STEP 3 - Get all timesheet from database by filter parameters
        ReportBO boReport = new ReportBO();
		beanInquiryReport = boReport.getInquiryReport(beanInquiryReport, bIsForExport, response);

        request.setAttribute("beanInquiryReport", beanInquiryReport);
    }

    /**
     * Method getSummaryReport
     * Get a summary report
     * @author  Nguyen Thai Son
     * @version	November 22, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
	private void getSummaryReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//STEP 1 - Check session and initialize combo boxes
		//UserInfoBean beanUserInfo = checkSessionVariables(request, response);
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		//STEP 2 - Get filter parameters from client
		//String strGroup = "All";    // All groups
		String strGroup = beanUserInfo.getGroupName();
		String strProjectID = "0";
		String strStatus = "0";		//Unapproved
		String strProjectType = "10";	//All types
		String strReportType = "0";	//summary report
		String strFromDate = null;
		String strToDate = null;
		String strAccount = "";
		String strProjectStatus = Integer.toString(Timesheet.PROJECT_STATUS_ONGOING);

		SummaryReportBean beanSummaryReport = new SummaryReportBean();

		if (session.getAttribute("srProjectStatusList") == null) {
			beanSummaryReport.setProjectStatusList(beanUserInfo.getRole(), beanUserInfo.getUserId());
			session.setAttribute("srProjectStatusList", beanSummaryReport.getProjectStatusList());
		}
		else {
			beanSummaryReport.setProjectStatusList((StringMatrix) session.getAttribute("srProjectStatusList"));
		}

		if ("false".equals(session.getAttribute("srIsFirstTime"))) {
			//GROUP
			if (request.getParameter("srptGroup") != null) {
				strGroup = request.getParameter("srptGroup");
				session.setAttribute("srGroup", strGroup);
			}
			else if (session.getAttribute("srGroup") != null) {
				strGroup = (String) session.getAttribute("srGroup");
			}
			else {
				//strGroup = "All";
				strGroup = beanUserInfo.getGroupName();
			}

			//PROJECT STATUS
			if (request.getParameter("srptProjectStatus") != null) {
				strProjectStatus = request.getParameter("srptProjectStatus");
				session.setAttribute("srProjectStatus", strProjectStatus);
			}
			else if (session.getAttribute("srProjectStatus") != null) {
				strProjectStatus = (String) session.getAttribute("srProjectStatus");
			}
			else {
				strProjectStatus = Integer.toString(Timesheet.PROJECT_STATUS_ONGOING);
			}

			//PROJECT
			if (request.getParameter("srcboProject") != null) {
				strProjectID = request.getParameter("srcboProject");
				session.setAttribute("srProject", strProjectID);
			}
			else if (session.getAttribute("srProject") != null) {
			  strProjectID = (String) session.getAttribute("srProject");
			}

			//FROM DATE
			strFromDate = request.getParameter("FromDate");
			if (strFromDate != null) {
			  session.setAttribute("srFromDate", strFromDate);
			}
			else if (session.getAttribute("srFromDate") != null) {
			  strFromDate = (String) session.getAttribute("srFromDate");
			}

			//TO DATE
			strToDate = request.getParameter("ToDate");
			if (strToDate != null) {
			  session.setAttribute("srToDate", strToDate);
			}
			else if (session.getAttribute("srToDate") != null) {
			  strToDate = (String) session.getAttribute("srToDate");
			}

			//ACCOUNT
			strAccount = request.getParameter("srptAccount");
			if (strAccount != null) {
				session.setAttribute("srAccount", strAccount);
			}
			else if (session.getAttribute("srAccount") != null) {
				strAccount = (String) session.getAttribute("srAccount");
			}
			else {
				strAccount = "";
			}
			strAccount = strAccount.toUpperCase();

			//REPORT TYPE
			if (request.getParameter("ReportType") != null) {
				strReportType = request.getParameter("ReportType");
				session.setAttribute("srReportType", strReportType);
			}
			else if (session.getAttribute("srReportType") != null) {
				strReportType = (String) session.getAttribute("srReportType");
			}
			else {
				strReportType = "0";
			}

			//PROJECT TYPE
			if (request.getParameter("srptProjectType") != null && request.getParameter("srptProjectType") != "") {
				strProjectType = request.getParameter("srptProjectType");
				session.setAttribute("srProjectType", strProjectType);
			}
			else if (session.getAttribute("srProjectType") != null) {
			  strProjectType = (String) session.getAttribute("srProjectType");
			}
			else {
			  strProjectType = "10";
			}

			//TIMESHEET STATUS
			if (request.getParameter("Status") != null) {
				strStatus = request.getParameter("Status");
				session.setAttribute("srStatus", strStatus);
			}
			else if (session.getAttribute("srStatus") != null) {
				strStatus = (String) session.getAttribute("srStatus");
			}
		}
		//Set Info to Bean
		//==========================================================
		beanSummaryReport.setProjectList(beanUserInfo.getRole(), beanUserInfo.getUserId(),
									     DefinitionList.INQUIRY_REPORT_TYPE,
									     CommonUtil.StrToInt(strProjectStatus));
		beanSummaryReport.generateGroupList();

		String arrProjectIDs = "";
		if ("0".equals(strProjectID)) {
			if (request.getParameter("hidProjectsList") != null) {
				arrProjectIDs = request.getParameter("hidProjectsList");
				session.setAttribute("hidProjectList", arrProjectIDs);
			}
			else if (session.getAttribute("hidProjectList") != null) {
				arrProjectIDs = (String) session.getAttribute("hidProjectList");
			}
			else {
				arrProjectIDs = beanSummaryReport.getProjectList().getCell(0, 0);
			}
		}
		// Get all projects
		beanSummaryReport.setGroup(strGroup);
		beanSummaryReport.setProjectList(beanUserInfo.getRole(),
										 beanUserInfo.getUserId(),
										 DefinitionList.INQUIRY_REPORT_TYPE);
		beanSummaryReport.setProject(CommonUtil.StrToInt(strProjectID));
		beanSummaryReport.setProjectType(strProjectType);
		beanSummaryReport.setProjectStatus(CommonUtil.StrToInt(strProjectStatus));		                         
		beanSummaryReport.setStatus(CommonUtil.StrToInt(strStatus));
		beanSummaryReport.setArrayOfProjectIDs(arrProjectIDs);
		beanSummaryReport.setFromDate(strFromDate);
		beanSummaryReport.setToDate(strToDate);
		beanSummaryReport.setReportType(CommonUtil.StrToInt(strReportType));
		beanSummaryReport.setReportList(new StringMatrix());
		beanSummaryReport.setAccount(strAccount);

		//Get Info for Searching
		//==========================================================
		if ("false".equals(session.getAttribute("srIsFirstTime"))) {
			// Get project distribution reports
			ReportBO boReport = new ReportBO();
			if ((beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PROJECT_PROCESS) ||
				(beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PROJECT_PRODUCT) ||
				(beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PROJECT_KPA) ||
				(beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PROJECT_TOW)) 
			{
				 beanSummaryReport = boReport.getProjectDistribution(beanSummaryReport);
			}
			// Get summary effort of projects
			else if (beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PROJECT_SUMMARY) {
				beanSummaryReport = boReport.getProjectSummary(beanSummaryReport);
			}
			// Get general Summary report
			else if (beanSummaryReport.getReportType() == 0) {
				beanSummaryReport = boReport.getSummaryReport(beanSummaryReport);
			}
			// Get distribution report by TypeOfWork or Account and Date report
			else {
				beanSummaryReport = boReport.getEffortSummary(beanSummaryReport);
			}
		}
		session.setAttribute("srIsFirstTime", "false");
		request.setAttribute("beanSummaryReport", beanSummaryReport);
	}

	/**
	 * Method getUnapprovedPMServlet
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getUnreviewedPMServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");
		WeeklyReportBean beanWeeklyReport = new WeeklyReportBean();

		String strGroup = beanUserInfo.getGroupName(); //Group
		String strProjectID = "0";
		String strStatus = "0";			//Unapproved
		String strReportType = "0";		//Lack of timesheet report		
		String strFromDate = null;
		String strToDate = null;
		String strLogDate = null;
		String strLogTime = null;
		String strProjectStatus = Integer.toString(Timesheet.PROJECT_STATUS_ALL);

		if (session.getAttribute("srProjectStatusList") == null) {
			beanWeeklyReport.setProjectStatusList(beanUserInfo.getRole(), beanUserInfo.getUserId());
			session.setAttribute("srProjectStatusList", beanWeeklyReport.getProjectStatusList());
		}
		else {
			beanWeeklyReport.setProjectStatusList((StringMatrix) session.getAttribute("srProjectStatusList"));
		}

		if ("false".equals(session.getAttribute("wrIsFirstTime"))) {
			//GROUP
			if (request.getParameter("cboGroup") != null) {
				strGroup = request.getParameter("cboGroup");
				session.setAttribute("srGroup", strGroup);
			}
			else if (session.getAttribute("srGroup") != null) {
				strGroup = (String) session.getAttribute("srGroup");
			}
			else {
				//strGroup = "All";
				strGroup = beanUserInfo.getGroupName();
			}

			//PROJECT
			if (request.getParameter("cboProject") != null) {
				strProjectID = request.getParameter("cboProject");
				session.setAttribute("srProject", strProjectID);
			}
			else if (session.getAttribute("srProject") != null) {
				strProjectID = (String) session.getAttribute("srProject");
			}

			//PROJECT STATUS
			if (request.getParameter("cboProjectStatus") != null) {
				strProjectStatus = request.getParameter("cboProjectStatus");
				session.setAttribute("srProjectStatus", strProjectStatus);
			}
			else if (session.getAttribute("srProjectStatus") != null) {
				strProjectStatus = (String) session.getAttribute("srProjectStatus");
			}

			//FROM DATE
			strFromDate = request.getParameter("txtFromDateUnreview");
			if (strFromDate != null) {
				session.setAttribute("txtFromDateUnreview", strFromDate);
			}
			else if (session.getAttribute("txtFromDateUnreview") != null) {
				strFromDate = (String) session.getAttribute("txtFromDateUnreview");
			}

			//TO DATE
			strToDate = request.getParameter("txtToDateUnreview");
			if (strToDate != null) {
				session.setAttribute("txtToDateUnreview", strToDate);
			}
			else if (session.getAttribute("txtToDateUnreview") != null) {
				strToDate = (String) session.getAttribute("txtToDateUnreview");
			}

			//LOG DATE
			strLogDate = request.getParameter("txtLogDateUnreview");
			if (strLogDate != null) {
				session.setAttribute("srLogDate", strLogDate);
			}
			else if (session.getAttribute("srLogDate") != null) {
				strLogDate = (String) session.getAttribute("srLogDate");
			}

			//LOG TIME
			strLogTime = request.getParameter("txtLogTimeUnreview");
			if (strLogTime != null) {
				session.setAttribute("srLogTime", strLogTime);
			}
			else if (session.getAttribute("srLogTime") != null) {
				strLogTime = (String) session.getAttribute("srLogTime");
			}

			//REPORT TYPE
			if (request.getParameter("cboReportType") != null) {
				strReportType = request.getParameter("cboReportType");
				session.setAttribute("srReportType", strReportType);
			}
			else if (session.getAttribute("srReportType") != null) {
				strReportType = (String) session.getAttribute("srReportType");
			}
			else {
				strReportType = "0";
			}
		}
		beanWeeklyReport.setGroupList();
		beanWeeklyReport.setGroup(strGroup);
		beanWeeklyReport.setProject(Integer.parseInt(strProjectID));
		beanWeeklyReport.setProjectList(beanUserInfo.getRole(), beanUserInfo.getUserId(), Timesheet.PROJECT_STATUS_ONGOING_TENTATIVE_CLOSED);
		beanWeeklyReport.setProjectStatus(Integer.parseInt(strProjectStatus));
		beanWeeklyReport.setReportType(CommonUtil.StrToInt(strReportType));
		beanWeeklyReport.setSearchFromDate(strFromDate);
		beanWeeklyReport.setSearchToDate(strToDate);
		beanWeeklyReport.setLogDate(strLogDate);
		beanWeeklyReport.setLogTime(strLogTime);

		String arrProjectIDs = "";
		if ("0".equals(strProjectID)) {
			if (request.getParameter("hidProjectsList") != null) {
				arrProjectIDs = request.getParameter("hidProjectsList");
				session.setAttribute("hidProjectList", arrProjectIDs);
			}
			else if (session.getAttribute("hidProjectList") != null) {
				arrProjectIDs = (String) session.getAttribute("hidProjectList");
			}
			else {
				arrProjectIDs = beanWeeklyReport.getProjectList().getCell(0, 0);
			}
		}
		else {
			arrProjectIDs = strProjectID + ",0";
		}
		beanWeeklyReport.setArrayOfProjectIDs(arrProjectIDs);

		session.setAttribute("wrIsFirstTime", "false");

		ReportBO boReport = new ReportBO();
		beanWeeklyReport = boReport.getUnreviewedPMBO(beanWeeklyReport);
		request.setAttribute("beanWeeklyReport", beanWeeklyReport);
	}

	/**
	 * Method getUnapprovedQAServlet
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getUnreviewedQAServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		WeeklyReportBean beanWeeklyReport = new WeeklyReportBean();

		String strGroup = beanUserInfo.getGroupName(); //Group
		String strPQAName = "0";
		String strProjectID = "0";
		String strStatus = "0";			//Unapproved
		String strReportType = "0";		//Lack of timesheet report		
		String strFromDate = null;
		String strToDate = null;
		String strLogDate = null;
		String strLogTime = null;
		String strProjectStatus = Integer.toString(Timesheet.PROJECT_STATUS_ALL);

		if (session.getAttribute("srProjectStatusList") == null) {
			beanWeeklyReport.setProjectStatusList(beanUserInfo.getRole(), beanUserInfo.getUserId());
			session.setAttribute("srProjectStatusList", beanWeeklyReport.getProjectStatusList());
		}
		else {
			beanWeeklyReport.setProjectStatusList((StringMatrix) session.getAttribute("srProjectStatusList"));
		}

		if ("false".equals(session.getAttribute("wrIsFirstTime"))) {
			//GROUP
			if (request.getParameter("cboGroup") != null) {
				strGroup = request.getParameter("cboGroup");
				session.setAttribute("srGroup", strGroup);
			}
			else if (session.getAttribute("srGroup") != null) {
				strGroup = (String) session.getAttribute("srGroup");
			}
			else {
				//strGroup = "All";
				strGroup = beanUserInfo.getGroupName();
			}

			//PQA NAME
		  	if (request.getParameter("cboPQAName") != null) {
				strPQAName = request.getParameter("cboPQAName");
			  	session.setAttribute("srPQAName", strPQAName);
		  	}
		  	else if (session.getAttribute("srPQAName") != null) {
				strPQAName = (String) session.getAttribute("srPQAName");
		  	}
		  	else {
				strPQAName = "All";
		  	}

			//PROJECT
			if (request.getParameter("cboProject") != null) {
				strProjectID = request.getParameter("cboProject");
				session.setAttribute("srProject", strProjectID);
			}
			else if (session.getAttribute("srProject") != null) {
				strProjectID = (String) session.getAttribute("srProject");
			}

			//PROJECT STATUS
			if (request.getParameter("cboProjectStatus") != null) {
				strProjectStatus = request.getParameter("cboProjectStatus");
				session.setAttribute("srProjectStatus", strProjectStatus);
			}
			else if (session.getAttribute("srProjectStatus") != null) {
				strProjectStatus = (String) session.getAttribute("srProjectStatus");
			}

			//FROM DATE
			strFromDate = request.getParameter("txtFromDateUnreview");
			if (strFromDate != null) {
				session.setAttribute("txtFromDateUnreview", strFromDate);
			}
			else if (session.getAttribute("txtFromDateUnreview") != null) {
				strFromDate = (String) session.getAttribute("txtFromDateUnreview");
			}

			//TO DATE
			strToDate = request.getParameter("txtToDateUnreview");
			if (strToDate != null) {
				session.setAttribute("txtToDateUnreview", strToDate);
			}
			else if (session.getAttribute("txtToDateUnreview") != null) {
				strToDate = (String) session.getAttribute("txtToDateUnreview");
			}

			//LOG DATE
			strLogDate = request.getParameter("txtLogDateUnreview");
			if (strLogDate != null) {
				session.setAttribute("srLogDate", strLogDate);
			}
			else if (session.getAttribute("srLogDate") != null) {
				strLogDate = (String) session.getAttribute("srLogDate");
			}

			//LOG TIME
			strLogTime = request.getParameter("txtLogTimeUnreview");			
			if (strLogTime != null) {
				session.setAttribute("srLogTime", strLogTime);
			}
			else if (session.getAttribute("srLogTime") != null) {
				strLogTime = (String) session.getAttribute("srLogTime");
			}

			//REPORT TYPE
			if (request.getParameter("cboReportType") != null) {
				strReportType = request.getParameter("cboReportType");
				session.setAttribute("srReportType", strReportType);
			}
			else if (session.getAttribute("srReportType") != null) {
				strReportType = (String) session.getAttribute("srReportType");
			}
			else {
				strReportType = "0";
			}
		}

		beanWeeklyReport.setGroupList();
		beanWeeklyReport.setGroup(strGroup);
		beanWeeklyReport.setPQAName(strPQAName);
		beanWeeklyReport.setPQANameList();
		beanWeeklyReport.setProject(Integer.parseInt(strProjectID));
		beanWeeklyReport.setProjectList(beanUserInfo.getRole(), beanUserInfo.getUserId(), Timesheet.PROJECT_STATUS_ONGOING_TENTATIVE_CLOSED);
		beanWeeklyReport.setProjectStatus(Integer.parseInt(strProjectStatus));
		beanWeeklyReport.setReportType(CommonUtil.StrToInt(strReportType));
		beanWeeklyReport.setSearchFromDate(strFromDate);
		beanWeeklyReport.setSearchToDate(strToDate);
		beanWeeklyReport.setLogDate(strLogDate);
		beanWeeklyReport.setLogTime(strLogTime);

		String arrProjectIDs = "";
		if ("0".equals(strProjectID)) {
			if (request.getParameter("hidProjectsList") != null) {
				arrProjectIDs = request.getParameter("hidProjectsList");
				session.setAttribute("hidProjectList", arrProjectIDs);
			}
			else if (session.getAttribute("hidProjectList") != null) {
				arrProjectIDs = (String) session.getAttribute("hidProjectList");
			}
			else {
				arrProjectIDs = beanWeeklyReport.getProjectList().getCell(0, 0);
			}
		}
		else {
			arrProjectIDs = strProjectID + ",0";
		}
		beanWeeklyReport.setArrayOfProjectIDs(arrProjectIDs);

		session.setAttribute("wrIsFirstTime", "false");

		//Get all timesheet from database by filter parameters
		ReportBO boReport = new ReportBO();
		beanWeeklyReport = boReport.getUnreviewedQABO(beanWeeklyReport);
		request.setAttribute("beanWeeklyReport", beanWeeklyReport);
	}

	/**	 
	 * Method getTrackingReportProjectServlet
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getTrackingReportProjectServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

		WeeklyReportBean beanWeeklyReport = new WeeklyReportBean();
    
		String strGroup = beanUserInfo.getGroupName(); //Group
		String strProjectID = "0";
		String strStatus = "0";			//Unapproved
		String strReportType = "0";		//Lack of timesheet report		
		String strFromDate = null;
		String strToDate = null;
		String strLogDate = null;
		String strLogTime = null;
		String strProjectStatus = Integer.toString(Timesheet.PROJECT_STATUS_ALL);

		if (session.getAttribute("srProjectStatusList") == null) {
			beanWeeklyReport.setProjectStatusList(beanUserInfo.getRole(), beanUserInfo.getUserId());
			session.setAttribute("srProjectStatusList", beanWeeklyReport.getProjectStatusList());
		}
		else {
			beanWeeklyReport.setProjectStatusList((StringMatrix) session.getAttribute("srProjectStatusList"));
		}

		if ("false".equals(session.getAttribute("wrIsFirstTime"))) {
			//GROUP
			if (request.getParameter("cboGroup") != null) {
				strGroup = request.getParameter("cboGroup");				
				session.setAttribute("srGroup", strGroup);
			}
			else if (session.getAttribute("srGroup") != null) {
				strGroup = (String) session.getAttribute("srGroup");
			}
			else {
				//strGroup = "All";
				strGroup = beanUserInfo.getGroupName();
			}

			//PROJECT
			if (request.getParameter("cboProject") != null) {
				strProjectID = request.getParameter("cboProject");
				session.setAttribute("srProject", strProjectID);
			}
			else if (session.getAttribute("srProject") != null) {
				strProjectID = (String) session.getAttribute("srProject");
			}

			//PROJECT STATUS
			if (request.getParameter("cboProjectStatus") != null) {
				strProjectStatus = request.getParameter("cboProjectStatus");
				session.setAttribute("srProjectStatus", strProjectStatus);
			}
			else if (session.getAttribute("srProjectStatus") != null) {
				strProjectStatus = (String) session.getAttribute("srProjectStatus");
			}

			//FROM DATE
			strFromDate = request.getParameter("txtFromDateTracking");
			if (strFromDate != null) {
				session.setAttribute("txtFromDateTracking", strFromDate);
			}
			else if (session.getAttribute("txtFromDateTracking") != null) {
				strFromDate = (String) session.getAttribute("txtFromDateTracking");
			}

			//TO DATE
			strToDate = request.getParameter("txtToDateTracking");
			if (strToDate != null) {
				session.setAttribute("txtToDateTracking", strToDate);
			}
			else if (session.getAttribute("txtToDateTracking") != null) {
				strToDate = (String) session.getAttribute("txtToDateTracking");
			}

			//LOG DATE
			strLogDate = request.getParameter("txtLogDateTracking");
			if (strLogDate != null) {
				session.setAttribute("srLogDate", strLogDate);
			}
			else if (session.getAttribute("srLogDate") != null) {
				strLogDate = (String) session.getAttribute("srLogDate");
			}

			//LOG TIME
			strLogTime = request.getParameter("txtLogTimeTracking");			
			if (strLogTime != null) {
				session.setAttribute("srLogTime", strLogTime);
			}
			else if (session.getAttribute("srLogTime") != null) {
				strLogTime = (String) session.getAttribute("srLogTime");
			}

			//REPORT TYPE
			if (request.getParameter("cboReportType") != null) {
				strReportType = request.getParameter("cboReportType");
				session.setAttribute("srReportType", strReportType);
			}
			else if (session.getAttribute("srReportType") != null) {
				strReportType = (String) session.getAttribute("srReportType");
			}
			else {
				strReportType = "0";
			}
		}
		beanWeeklyReport.setGroupList();
		beanWeeklyReport.setGroup(strGroup);
		beanWeeklyReport.setProject(Integer.parseInt(strProjectID));
		beanWeeklyReport.setProjectList(beanUserInfo.getRole(), beanUserInfo.getUserId(), Timesheet.PROJECT_STATUS_ONGOING_TENTATIVE_CLOSED);		
		beanWeeklyReport.setProjectStatus(Integer.parseInt(strProjectStatus));
		beanWeeklyReport.setReportType(CommonUtil.StrToInt(strReportType));		
		beanWeeklyReport.setSearchFromDate(strFromDate);
		beanWeeklyReport.setSearchToDate(strToDate);
		beanWeeklyReport.setLogDate(strLogDate);
		beanWeeklyReport.setLogTime(strLogTime);

		session.setAttribute("wrIsFirstTime", "false");

		//Get all timesheet from database by filter parameters
		ReportBO boReport = new ReportBO();
		beanWeeklyReport = boReport.getTrackingReportProjectBO(beanWeeklyReport);
		
		request.setAttribute("beanWeeklyReport", beanWeeklyReport);
	}

	/**
		 * Method getTrackingReportServlet
		 * @Author HaiMM
		 * @param request
		 * @param response
		 * @throws Exception
		 */
	private void getTrackingReportServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strReportType = "0";
		int intReportType = 0;
		
		if (request.getParameter("cboReportType") != null) {
			strReportType = request.getParameter("cboReportType");
		}
	
		intReportType = CommonUtil.StrToInt(strReportType);
	
		if (intReportType == Timesheet.REPORTTYPE_LACKTS_GROUP) {
			getTrackingReportGroupServlet(request, response);
			callPage(response, TIMESHEET.JSP_REPORT_LACKTS_GROUP, request);
		}
			//REPORTTYPE_LACKTS_PROJECT
		else if (intReportType == Timesheet.REPORTTYPE_LACKTS_PROJECT) {
			getTrackingReportProjectServlet(request, response);
			callPage(response, TIMESHEET.JSP_REPORT_LACKTS_PROJECT, request);
		}
			//REPORTTYPE_UNAPPROVED_PM
		else if (intReportType == Timesheet.REPORTTYPE_UNAPPROVED_PM) {
			getUnreviewedPMServlet(request, response);
			callPage(response, TIMESHEET.JSP_REPORT_UNAPPROVED_PM, request);
		}
			//REPORTTYPE_UNAPPROVED_QA
		else if (intReportType == Timesheet.REPORTTYPE_UNAPPROVED_QA) {
			getUnreviewedQAServlet(request, response);
			callPage(response, TIMESHEET.JSP_REPORT_UNAPPROVED_QA, request);
		}
	}
    
    /**
     * Method getTrackingReportGroupServlet
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getTrackingReportGroupServlet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //UserInfoBean beanUserInfo = checkSessionVariables(request, response);
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean) session.getAttribute("beanUserInfo");

        WeeklyReportBean beanWeeklyReport = new WeeklyReportBean();

		String strGroup = beanUserInfo.getGroupName(); //Group
		String strReportType = "0";
		String strFromDate = null;
		String strToDate = null;
		String strLogDate = null;
		String strLogTime = null;

		if (session.getAttribute("srProjectStatusList") == null) {
			beanWeeklyReport.setProjectStatusList(beanUserInfo.getRole(), beanUserInfo.getUserId());
			session.setAttribute("srProjectStatusList", beanWeeklyReport.getProjectStatusList());
		}
		else {
			beanWeeklyReport.setProjectStatusList((StringMatrix) session.getAttribute("srProjectStatusList"));
		}

        if ("false".equals(session.getAttribute("wrIsFirstTime"))) {
			//GROUP
			if (request.getParameter("cboGroup") != null) {
				strGroup = request.getParameter("cboGroup");
				session.setAttribute("srGroup", strGroup);
			}
			else if (session.getAttribute("srGroup") != null) {
				strGroup = (String) session.getAttribute("srGroup");
			}
			else {
				//strGroup = "All";
				strGroup = beanUserInfo.getGroupName();
			}

			//FROM DATE
			strFromDate = request.getParameter("txtFromDateTracking");
			if (strFromDate != null) {
				session.setAttribute("txtFromDateTracking", strFromDate);
			}
			else if (session.getAttribute("txtFromDateTracking") != null) {
				strFromDate = (String) session.getAttribute("txtFromDateTracking");
			}

			//TO DATE
			strToDate = request.getParameter("txtToDateTracking");
			if (strToDate != null) {
				session.setAttribute("txtToDateTracking", strToDate);
			}
			else if (session.getAttribute("txtToDateTracking") != null) {
				strToDate = (String) session.getAttribute("txtToDateTracking");
			}

			//LOG DATE
			strLogDate = request.getParameter("txtLogDateTracking");
			if (strLogDate != null) {
				session.setAttribute("srLogDate", strLogDate);
			}
			else if (session.getAttribute("srLogDate") != null) {
				strLogDate = (String) session.getAttribute("srLogDate");
			}

			//LOG TIME
			strLogTime = request.getParameter("txtLogTimeTracking");
			
			if (strLogTime != null) {
				session.setAttribute("srLogTime", strLogTime);
			}
			else if (session.getAttribute("srLogTime") != null) {
				strLogTime = (String) session.getAttribute("srLogTime");
			}

			//REPORT TYPE
			if (request.getParameter("cboReportType") != null) {
				strReportType = request.getParameter("cboReportType");
				session.setAttribute("srReportType", strReportType);
			}
			else if (session.getAttribute("srReportType") != null) {
				strReportType = (String) session.getAttribute("srReportType");
			}
			else {
				strReportType = "0";
			}
		}
		beanWeeklyReport.setGroupList();
        beanWeeklyReport.setGroup(strGroup);
		beanWeeklyReport.setReportType(CommonUtil.StrToInt(strReportType));
		beanWeeklyReport.setSearchFromDate(strFromDate);
		beanWeeklyReport.setSearchToDate(strToDate);
		beanWeeklyReport.setLogDate(strLogDate);
		beanWeeklyReport.setLogTime(strLogTime);

        session.setAttribute("wrIsFirstTime", "false");

         //Get all timesheet from database by filter parameters
        ReportBO boReport = new ReportBO();
		beanWeeklyReport = boReport.getTrackingReportGroupBO(beanWeeklyReport);

		request.setAttribute("beanWeeklyReport", beanWeeklyReport);
		
   }
}