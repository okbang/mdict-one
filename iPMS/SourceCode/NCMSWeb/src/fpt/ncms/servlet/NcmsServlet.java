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
 
 /*
 * @(#)NcmsServlet.java 15-Mar-03
 */

package fpt.ncms.servlet;

import fpt.ncms.bean.ConstantAddBean;
import fpt.ncms.bean.ConstantListBean;
import fpt.ncms.bean.NCAddBean;
import fpt.ncms.bean.NCListBean;
import fpt.ncms.bean.NCListPersonalBean;
import fpt.ncms.bean.NCReportBean;
import fpt.ncms.bean.NCReportPivotBean;
import fpt.ncms.bean.UserInfoBean;
import fpt.ncms.bean.ViewAddBean;
import fpt.ncms.bean.ViewListBean;

import fpt.ncms.bo.Common.CommonBO;
import fpt.ncms.bo.Common.ConstantBO;
import fpt.ncms.bo.User.NCBO;
import fpt.ncms.bo.User.ViewBO;

import fpt.ncms.constant.NCMS;

import fpt.ncms.model.NCModel;
import fpt.ncms.servlet.core.BaseServlet;

import fpt.ncms.util.StringUtil.StringMatrix;
import fpt.ncms.util.StringUtil.StringVector;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Class NcmsServlet
 * Servlet for controlling NCMS
 * @version 1.0 15-Mar-03
 * @author
 */
public final class NcmsServlet extends BaseServlet {
    private static final Logger logger =
            Logger.getLogger(NcmsServlet.class.getName());
    /**
     * NcmsServlet
     * NCMS constructor
     */
    public NcmsServlet() {
    }
    
    /**
     * performTask
     * Process client request by action.
     * @param   request - the request object
     * @param   response - the response object
     * @throws  Throwable
     */
    public final void performTask(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String strAction = request.getParameter("hidAction");
            if (NCMS.DEBUG) {
                logger.info(strAction);
            }

            if (NCMS.HOMEPAGE_ACTION.equals(strAction)) {
                getLoginForm(request, response);
            }
            else if (NCMS.LOGIN_ACTION.equals(strAction)) {
                getLocation(request, response);
            }
            //submit to NC
            else if (NCMS.NC_ADD.equals(strAction)) {
                createNCAddForm(request, response);
            }
            else if (NCMS.NC_SAVE_NEW.equals(strAction)) {
                saveNewNC(request, response);
            }
            else if (NCMS.NC_SAVE_UPDATE.equals(strAction)) {
                saveUpdateNC(request, response);
            }
            else if (NCMS.NC_UPDATE.equals(strAction)) {
                createNCUpdateForm(request, response);
            }
            else if (NCMS.NC_LIST.equals(strAction)) {
                getNCList(request, response);
            }
            else if (NCMS.NC_EXPORT.equals(strAction)) {
                exportNC(request, response);
            }
            else if (NCMS.NC_REPORT.equals(strAction)) {
                getNCReport(request, response);
            }
            else if (NCMS.NC_REPORT_PIVOT.equals(strAction)) {
                getNCReportPivot(request, response);
            }
            else if (NCMS.VIEW_LIST.equals(strAction)) {
                getViewList(request, response);
            }
            else if (NCMS.VIEW_ADD.equals(strAction)) {
                createViewAddForm(request, response);
            }
            else if (NCMS.VIEW_UPDATE.equals(strAction)) {
                createViewUpdateForm(request, response);
            }
            else if (NCMS.VIEW_SAVE_NEW.equals(strAction)) {
                saveNewView(request, response);
                getViewList(request, response);
            }
            else if (NCMS.VIEW_DELETE.equals(strAction)) {
                deleteView(request, response);
                getViewList(request, response);
            }
            else if (NCMS.VIEW_SAVE_UPDATE.equals(strAction)) {
                saveUpdateView(request, response);
                getViewList(request, response);
            }
            else if (NCMS.CONSTANT_LIST.equals(strAction)) {
                getConstantList(request, response);
            }
            else if (NCMS.CONSTANT_ADD.equals(strAction)) {
                createConstantAddForm(request, response);
            }
            else if (NCMS.CONSTANT_UPDATE.equals(strAction)) {
                createConstantUpdateForm(request, response);
            }
            else if (NCMS.CONSTANT_SAVE_NEW.equals(strAction)) {
                saveNewConstant(request, response);
                getConstantList(request, response);
            }
            else if (NCMS.CONSTANT_DELETE.equals(strAction)) {
                deleteConstant(request, response);
                getConstantList(request, response);
            }
            else if (NCMS.CONSTANT_SAVE_UPDATE.equals(strAction)) {
                saveUpdateConstant(request, response);
                getConstantList(request, response);
            }
            //for test Report CC & Report NC 
            else if (NCMS.NC_EXPORT_CC.equals(strAction)) {
                createReportCC(request, response);
            }
            else if (NCMS.NC_EXPORT_NC.equals(strAction)) {
                createReportNC(request, response);
            }
            else if (NCMS.NC_HISTORY.equals(strAction)) {
                createNCHistoryForm(request, response);
            }            
            //added ny MinhPT
            //22-Dec-03
            //for Call Log System
            else if (NCMS.CALL_LOG_LIST.equals(strAction)){
                getCallList(request, response);                
            }
            else if (NCMS.CALL_LOG_ADD.equals(strAction)){
                createCallLogAddForm(request, response);
            }
            else if (NCMS.CALL_LOG_SAVE_NEW.equals(strAction)){
                saveNewCallLog(request, response);
            }
            else if (NCMS.CALL_LOG_UPDATE.equals(strAction)){
                createCallLogUpdateForm(request, response);
            }
            else if (NCMS.CALL_LOG_SAVE_UPDATE.equals(strAction)){
                saveUpdateCallLog(request, response);
            }
            else if (NCMS.CALL_LOG_DELETE.equals(strAction)){
                deleteCallLog(request, response);
            }
//            //get the Rsa public key
//            else if(NCMS.GET_RSA_ACTION.equals(strAction)){
//                logger.info("NCMS.GET_RSA_ACTION");
//                Rsa.getAuthenticate(request,response);
//                return;
//            }
            //Login Form
            else {
                getLoginForm(request, response);
            }
        }
        catch (Exception exception) {
            logger.debug("Exception in NcmsServlet.performTask().");
            logger.error("NcmsServlet - performTask(): ", exception);
            try {
                sendErrorRedirect(request, response, NCMS.JSP_ERROR, exception);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * checkSessionVariable
     * Get a form to login
     * @param   beanUserInfo - bean stored session
     * @return  true - if session is valid
     * @return  false - if session is invalid
     */
    private boolean checkSessionVariable(UserInfoBean beanUserInfo) {
        return ((beanUserInfo != null) &&
                (beanUserInfo.getLoginName() != null) &&
                (beanUserInfo.getLoginName().length() > 0));
    }

    /**
     * sendErrorRedirect
     * Redirect exception to an error page.
     * @param   request - the request object.
     * @param   errorPageURL - the URL to the error page.
     * @param   e - a throwable storing exception.
     * @throws  ServletException
     * @throws  IOException
     */
    private void sendErrorRedirect(HttpServletRequest request,
            HttpServletResponse response, String errorPageURL, Throwable e)
            throws ServletException, IOException {
        try {
            request.setAttribute("javax.servlet.jsp.jspException", e);
            getServletConfig().getServletContext()
                    .getRequestDispatcher(errorPageURL).forward(request, response);
        }
        catch (java.lang.IllegalStateException ise) {
            ise.printStackTrace();
        }
    }

    /**
     *  
     * Get a form to login
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  SQLException
     */
    private void getLoginForm(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            logger.info("session.invalidate() called.");
        }
        session = request.getSession(true);
        
//        Rsa rsa = new Rsa(160);
//        session.setAttribute("rsa", rsa);
        UserInfoBean beanUserInfo = new UserInfoBean();
        session.setAttribute("beanUserInfo", beanUserInfo);
        session.setAttribute("cboProject","(All)");
        callPage(response, NCMS.JSP_LOGIN, request);
    }

    /**
     * getLocation
     * Provide authorization and forward user to the right mode
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void getLocation(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        // If session is null and not Insight request
        if ((session == null) && (request.getParameter("key") == null)) {
            getLoginForm(request, response);
            return;
        }
        session = request.getSession();
        
//        //special code if called from the insight
//        Rsa rsa;
//        String key = request.getParameter("key");
//        if (key != null) {
//            rsa = Rsa.getRsaFromPool(key);
//            session.setAttribute("rsa", rsa);
//        }
//        else {
//            rsa = (Rsa) session.getAttribute("rsa");
//        }

        String strUsername = request.getParameter("txtAccount");
        String strPassword = request.getParameter("txtPassword");
        
        String strLocation = request.getParameter("cboMode");
        CommonBO boLogin = new CommonBO();
        UserInfoBean beanUserInfo = boLogin.checkLogin(strUsername,
                strPassword, strLocation);
        // Set login mode (NCMS, CallLog)
        beanUserInfo.setLocation(Integer.parseInt(strLocation));
        
        session.setAttribute("beanUserInfo", beanUserInfo);
        String strTypeOfView = beanUserInfo.getTypeOfView();
        if (!"".equals(strTypeOfView)){
            if ("0".equals(strLocation)){
                getNCList(request, response);
            }
            else {
                getCallList(request, response);      
            }
        }
        // Return login form because invalid session
        else {					
            //getLoginForm(request, response);
            callPage(response, NCMS.JSP_LOGIN, request);
        }
    }

    /**
     * getNCList
     * Get NC list
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void getNCList(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");
        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            String strListingMode = request.getParameter("hidMode"); 
            if (strListingMode != null) {
                beanUserInfo.setListingMode(strListingMode);
            }
            // Clear informations
            beanUserInfo.setCurrentNC(null);

            if (NCMS.SYSTEM_MODE.equals(beanUserInfo.getListingMode())) {
                getNCListSystem(request, response, beanUserInfo);
                callPage(response, NCMS.JSP_NC_LIST, request);
            }
            else if (NCMS.PERSONAL_MODE.equals(beanUserInfo.getListingMode())) {
                getNCListPersonal(request, response, beanUserInfo);
                callPage(response, NCMS.JSP_NC_LIST_PERSONAL, request);
            }
        }
    }
    /**
     * getNCListSystem
     * Get list of NC in system mode
     * @param   request - the request object.
     * @param   response - the response object.
     * @param   beanUserInfo - session variable contains user's information
     * @throws  Exception
     */
    private void getNCListSystem(HttpServletRequest request,
            HttpServletResponse response, UserInfoBean beanUserInfo)
            throws Exception {
		
		String strSeletedProject = null;
		try {
			strSeletedProject = request.getParameter("selectedProjectHidden");
		}
		catch(Exception e){
		}
		if (strSeletedProject!=null) {
			request.setAttribute("cboProject",strSeletedProject);
			request.getSession().setAttribute("cboProject",strSeletedProject);
		}
		else strSeletedProject="";
        HttpSession session = request.getSession();
        NCListBean beanNCList = (NCListBean) session.getAttribute("beanNCList");
        NCModel modelNC;
        int nOldStatus = NCMS.NC_STATUS_ALL;
        
        if (beanNCList == null) {
            beanNCList = new NCListBean();
            modelNC = new NCModel();
            beanNCList.setNCModel(modelNC);
            session.setAttribute("beanNCList", beanNCList);
        }
        else {
            modelNC = beanNCList.getNCModel();
        }
        
        beanNCList.setCondition(getSearchCondition(request , response,0));
        NCBO boNC = new NCBO();
        String strOrderBy = request.getParameter("hidOrderBy");
        String strDirection = request.getParameter("hidDirection");
        String strNumPage = request.getParameter("PageNumber");
        if (strOrderBy != null) {
            beanNCList.setOrderBy(strOrderBy);
        }
        if (strDirection != null) {
            beanNCList.setDirection(Integer.parseInt(strDirection));
        }
        if (strNumPage != null) {
            beanNCList.setNumPage(Integer.parseInt(strNumPage));
        }

        //added by MinhPT
        //10-Dec-03
        //For set Condition Search
        CommonBO boCommon = new CommonBO();
        ConstantBO boConstant = new ConstantBO();
        boConstant.setUsage(beanUserInfo.getLocation());
        nOldStatus = modelNC.getStatus();
        modelNC.setStatus(NCMS.NC_STATUS_ALL);
        
        beanNCList.setComboStatus(boConstant.getStatusList(
                NCMS.CBO_ALL_VALUE, beanUserInfo, modelNC));
        modelNC.setStatus(nOldStatus);
        
        beanNCList.setComboCreator(boCommon.getUserList(beanUserInfo,
                NCMS.CBO_ALL_VALUE, NCMS.COMBO_CREATOR));
        beanNCList.setComboReviewer(boCommon.getUserList(beanUserInfo,
                NCMS.CBO_BOTH_ALL_AND_NONE_VALUE, NCMS.COMBO_ASSIGNEE));
        beanNCList.setComboAssignee(boCommon.getUserList(beanUserInfo,
                NCMS.CBO_BOTH_ALL_AND_NONE_VALUE, NCMS.COMBO_ASSIGNEE));

        beanNCList.setComboProject(boCommon.getProjectList(NCMS.CBO_ALL_VALUE,
                    NCMS.NONGROUP_VALUE, beanUserInfo.getLocation()));
//		added by LAMNT3
		beanNCList.setComboProjectStatus(boCommon.getProjectStatusList());                    
//		
        beanNCList.setComboTypeOfNC(boConstant.getTypeOfNCList(NCMS.CBO_ALL_VALUE));
        beanNCList.setComboLevel(boConstant.getLevelList(NCMS.CBO_ALL_VALUE));
        beanNCList.setComboGroup(boCommon.getGroupList(NCMS.CBO_BOTH_ALL_AND_GENERAL_VALUE));
        beanNCList.setComboDetectedBy(boConstant.getDetectedByList(
                    NCMS.CBO_ALL_VALUE));
        beanNCList.setComboTypeOfAction(boConstant.getTypeOfActionList(
                    NCMS.CBO_ALL_VALUE));
        beanNCList.setComboProcess(boConstant.getProcessList(
                    NCMS.CBO_ALL_VALUE));
        beanNCList.setComboISOClause(boConstant.getISOClauseList(
                    NCMS.CBO_ALL_VALUE));
        beanNCList.setComboKPA(boConstant.getKPAList(NCMS.CBO_ALL_VALUE));
        beanNCList.setComboTypeOfCause(boConstant.getTypeOfCauseList(
                    NCMS.CBO_ALL_VALUE));
        
        modelNC.setProjectID(strSeletedProject);
        
		int priStatusCode = beanNCList.getNCModel().getPrjStatus();
		String priStatusName="All status";
		for (int i=0;i<beanNCList.getComboProjectStatus().getNumberOfRows();i++) {
		   int temp  = Integer.parseInt(beanNCList.getComboProjectStatus().getCell(i,0));
		   if (priStatusCode==temp) priStatusName = beanNCList.getComboProjectStatus().getCell(i,1).toUpperCase(); 
		}
       	
        if (!priStatusName.equalsIgnoreCase("All status")) {
		   if (priStatusName.equalsIgnoreCase("ON-GOING")) { 
		   		priStatusName = "ON_GOING";
				String newQuery = beanNCList.getCondition() + " and NC.projectid in (select b.code from project b where b.project_status_code = '"+priStatusName+"' union select 'General' from dual )  ";
				beanNCList.setCondition(newQuery); 
		   } 
		   else { 
				String newQuery = beanNCList.getCondition() + " and NC.projectid in (select b.code from project b where b.project_status_code = '"+priStatusName+"')  ";
			 	beanNCList.setCondition(newQuery);
		   }
		   
		}
                    
        beanNCList = boNC.getNCList(beanUserInfo, beanNCList);
        beanNCList.setNCModel(modelNC);
    }

    /**
     * getNCListPersonal
     * Get list of NC applied with personal view
     * @param   request - the request object.
     * @param   response - the response object.
     * @param   beanUserInfo - session variable contains user's information
     * @throws  Exception
     */
    private void getNCListPersonal(HttpServletRequest request,
            HttpServletResponse response, UserInfoBean beanUserInfo)
            throws Exception {
        HttpSession session = request.getSession();
        NCListPersonalBean beanNCListPersonal =
            (NCListPersonalBean) session.getAttribute("beanNCListPersonal");
        NCModel modelNC;
        int nOldStatus = NCMS.NC_STATUS_ALL;
        if (beanNCListPersonal == null) {
            beanNCListPersonal = new NCListPersonalBean();
            modelNC = new NCModel();
            beanNCListPersonal.setNCModel(modelNC);
            session.setAttribute("beanNCListPersonal", beanNCListPersonal);
        }
        else {
            modelNC = beanNCListPersonal.getNCModel();
        }
        beanNCListPersonal.setCondition(getSearchCondition(request , response,1));

        ViewBO boView = new ViewBO();
        StringMatrix smList = boView.getViewList(beanUserInfo);

        if (smList == null) {
            // there's no defined view, the system view will be default
            smList = new StringMatrix(1, 4);
            smList.setCell(0, 0, "0");
            smList.setCell(0, 1, "System View");

            /* fields Status, Description, NCType, CreationDate,
               NCID are mandatory */
            smList.setCell(0, 2, "Status,Description,NCType,CreationDate,NCID");
            smList.setCell(0, 3, "Status");
            beanNCListPersonal.setViewID(smList.getCell(0, 0));
            beanNCListPersonal.setFields(smList.getCell(0, 2));
            beanNCListPersonal.setOrderBy(smList.getCell(0, 3));
        }
        else {
            String strViewID = request.getParameter("cboView");

            if (strViewID != null) {
                beanNCListPersonal.setViewID(strViewID);

                int nRow = smList.indexOf(strViewID, 0);
                beanNCListPersonal.setFields(
                        "Status,Description,NCType,CreationDate," +
                        smList.getCell(nRow, 2) + ",NCID");
                beanNCListPersonal.setOrderBy(smList.getCell(nRow, 3));
            }
            else {
                // The first view will be active
                beanNCListPersonal.setViewID(smList.getCell(0, 0));
                beanNCListPersonal.setFields(
                        "Status,Description,NCType,CreationDate," +
                        smList.getCell(0, 2) + ",NCID");
                beanNCListPersonal.setOrderBy(smList.getCell(0, 3));
            }
        }

        String strOrderBy = request.getParameter("hidPerOrderBy");
        if (strOrderBy != null) {
            beanNCListPersonal.setOrderBy(strOrderBy);
        }

        beanNCListPersonal.setComboView(smList);

        String strNumPage = request.getParameter("PageNumber");
        if (strNumPage != null) {
            beanNCListPersonal.setNumPage(Integer.parseInt(strNumPage));
        }

        String strDirection = request.getParameter("hidPerDirection");
        if (strDirection != null) {
            beanNCListPersonal.setDirection(Integer.parseInt(strDirection));
        }

        //added by MinhPT
        //10-Dec-03
        //For set Condition Search
        CommonBO boCommon = new CommonBO();
        ConstantBO boConstant = new ConstantBO();
        boConstant.setUsage(beanUserInfo.getLocation());
        nOldStatus = modelNC.getStatus();
        modelNC.setStatus(NCMS.NC_STATUS_ALL);
        beanNCListPersonal.setComboStatus(boConstant.getStatusList(
                NCMS.CBO_ALL_VALUE, beanUserInfo, modelNC));
        modelNC.setStatus(nOldStatus);
        
        beanNCListPersonal.setComboCreator(boCommon.getUserList(beanUserInfo,
                NCMS.CBO_ALL_VALUE, NCMS.COMBO_CREATOR));
        // Temporary remove
        //beanNCListPersonal.setCurrentCreator(beanUserInfo.getLoginName());
        beanNCListPersonal.setComboReviewer(boCommon.getUserList(beanUserInfo,
                    NCMS.CBO_ALL_VALUE, NCMS.COMBO_ASSIGNEE));
        beanNCListPersonal.setComboAssignee(boCommon.getUserList(beanUserInfo,
                    NCMS.CBO_BOTH_ALL_AND_NONE_VALUE, NCMS.COMBO_ASSIGNEE));
        
        beanNCListPersonal.setComboTypeOfAction(boConstant.getTypeOfActionList(
                    NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboLevel(boConstant.getLevelList(NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboProcess(boConstant.getProcessList(
                    NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboGroup(boCommon.getGroupList(NCMS.CBO_BOTH_ALL_AND_GENERAL_VALUE));
        beanNCListPersonal.setComboDetectedBy(boConstant.getDetectedByList(
                    NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboProject(boCommon.getProjectList(
                    NCMS.CBO_ALL_VALUE, NCMS.NONGROUP_VALUE, beanUserInfo.getLocation()));
        beanNCListPersonal.setComboTypeOfNC(boConstant.getTypeOfNCList(
                    NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboISOClause(boConstant.getISOClauseList(
                    NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboKPA(boConstant.getKPAList(NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboTypeOfCause(boConstant.getTypeOfCauseList(
                    NCMS.CBO_ALL_VALUE));
        NCBO boNC = new NCBO();
        beanNCListPersonal = boNC.getNCListPersonal(beanUserInfo,
                beanNCListPersonal);
        session.setAttribute("beanNCListPersonal", beanNCListPersonal);
    }

    /**
     * createNCAddForm
     * Create form for adding new NC
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void createNCAddForm(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
            if (beanNCAdd == null) {
                beanNCAdd = new NCAddBean();
                session.setAttribute("beanNCAdd", beanNCAdd);
            }
            NCModel modelNC = new NCModel();
            modelNC.setCreator(beanUserInfo.getLoginName());
            modelNC.setStatus(NCMS.NC_STATUS_NEW);
            modelNC.setNCType(NCMS.NCTYPE_NC);
            beanNCAdd.setNCModel(modelNC);
            
            CommonBO boCommon = new CommonBO();
            ConstantBO boConstant = new ConstantBO();
            boConstant.setUsage(beanUserInfo.getLocation());
            beanNCAdd.setComboStatus(boConstant.getStatusList(
                    NCMS.CBO_OTHER_VALUE, beanUserInfo, modelNC));
            beanNCAdd.setComboAssignee(boCommon.getUserList(beanUserInfo,
                    NCMS.CBO_EMPTY_VALUE, NCMS.COMBO_ASSIGNEE));
            
            beanNCAdd.setComboTypeOfAction(boConstant.getTypeOfActionList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboLevel(boConstant.getLevelList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboProcess(boConstant.getProcessList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboGroup(boCommon.getGroupList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboDetectedBy(boConstant.getDetectedByList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboProject(boCommon.getProjectList(
                    NCMS.CBO_OTHER_VALUE, NCMS.GROUP_VALUE, beanUserInfo.getLocation()));
            beanNCAdd.setComboTypeOfNC(boConstant.getTypeOfNCList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboISOClause(boConstant.getISOClauseList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboKPA(boConstant.getKPAList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboTypeOfCause(boConstant.getTypeOfCauseList(
                    NCMS.CBO_OTHER_VALUE));

            // Mark new record as not saved, to avoid refresh web page action
            // Refer function saveNewNC()
            session.setAttribute("saved", NCMS.MSG_NO);
            callPage(response, NCMS.JSP_NC_DETAIL, request);
        }
    }

    /**
     * createNCUpdateForm
     * Create form for updating NC
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void createNCUpdateForm(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
            NCModel modelNC = null;
            if (beanNCAdd == null) {
                beanNCAdd = new NCAddBean();
                modelNC = new NCModel();
                beanNCAdd.setNCModel(modelNC);
                session.setAttribute("beanNCAdd", beanNCAdd);
            }
            
            CommonBO boCommon = new CommonBO();
            ConstantBO boConstant = new ConstantBO();
            boConstant.setUsage(beanUserInfo.getLocation());
            NCBO boNC = new NCBO();
            String strNCID = request.getParameter("hidID");
            session.setAttribute("hidID", strNCID);
            // Get NCModel
            beanNCAdd = boNC.getNCDetail(strNCID, beanNCAdd,
                                         beanUserInfo.getLocation());
            modelNC = beanNCAdd.getNCModel();
            beanUserInfo.setCurrentNC(modelNC);

            beanNCAdd.setComboStatus(boConstant.getStatusList(
                    NCMS.CBO_OTHER_VALUE, beanUserInfo, modelNC));
            beanNCAdd.setComboAssignee(boCommon.getUserList(beanUserInfo,
                    NCMS.CBO_EMPTY_VALUE, NCMS.COMBO_ASSIGNEE));

            beanNCAdd.setComboTypeOfAction(boConstant.getTypeOfActionList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboLevel(boConstant.getLevelList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboProcess(boConstant.getProcessList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboGroup(boCommon.getGroupList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboDetectedBy(boConstant.getDetectedByList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboProject(boCommon.getProjectList(
                    NCMS.CBO_OTHER_VALUE, NCMS.GROUP_VALUE, beanUserInfo.getLocation()));
            beanNCAdd.setComboTypeOfNC(boConstant.getTypeOfNCList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboISOClause(boConstant.getISOClauseList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboKPA(boConstant.getKPAList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboTypeOfCause(boConstant.getTypeOfCauseList(
                    NCMS.CBO_OTHER_VALUE));

            session.setAttribute("beanNCAdd", beanNCAdd);
            callPage(response, NCMS.JSP_NC_UPDATE, request);
        }
    }
    /**
     * saveNewNC
     * Save new NC
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void saveNewNC(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        // Detect this record is already saved, avoid user refresh page
        else if (NCMS.MSG_YES.equals(session.getAttribute("saved"))) {
            getNCList(request, response);
        }
        else {
            //NCAddBean beanNCAdd = new NCAddBean();
            NCModel modelNC = new NCModel();
            
            modelNC.setNCLevel(request.getParameter("optLevel"));
            modelNC.setProjectID(request.getParameter("optProject"));
            modelNC.setNCType(request.getParameter("optTypeOfNC"));
            modelNC.setDetectedBy(request.getParameter("optDetectedBy"));
            modelNC.setCode(request.getParameter("txtTitle").trim());
            modelNC.setDescription(request.getParameter("txtDescription").trim());
            modelNC.setCreator(request.getParameter("txtCreator"));
            modelNC.setCreateDate(request.getParameter("txtCreationDate"));
            modelNC.setStatus(request.getParameter("optStatus"));
            modelNC.setTypeOfCause(request.getParameter("optTypeOfCause"));
            modelNC.setCause(request.getParameter("txtCause").trim());
            modelNC.setProcess(request.getParameter("optProcesses"));
            modelNC.setImpact(request.getParameter("txtImpact").trim());
            modelNC.setTypeOfAction(request.getParameter("optTypeOfAction"));
            modelNC.setCPAction(request.getParameter("txtCPAction").trim());
            modelNC.setAssignee(request.getParameter("optAssignee"));
            modelNC.setDeadLine(request.getParameter("txtDeadLine"));
            modelNC.setRepeat(request.getParameter("optRepeat"));
			//modelNC.setEffectOfChange(request.getParameter("txtEffectOfChange").trim());
            modelNC.setNote(request.getParameter("txtNote").trim());
            modelNC.setReviewDate(request.getParameter("txtReviewDate"));
            modelNC.setClosureDate(request.getParameter("txtClosureDate"));

            if ((NCMS.NC_STATUS_OPENED != modelNC.getStatus()) &&
                (modelNC.getStatus() > 0))
            {
                modelNC.setReviewer(beanUserInfo.getLoginName());
            }
            else
            {
                modelNC.setReviewer(request.getParameter("txtReviewer"));
            }

            modelNC.setKPA(request.getParameter("optKPA"));
            modelNC.setISOClause(request.getParameter("optISOClause"));
            modelNC.setGroupName(request.getParameter("optGroup"));
            
            NCBO boNC = new NCBO();
            int nRetVal = boNC.addNC(modelNC, beanUserInfo.getLocation());

            if (nRetVal <= 0) {
                //beanUserInfo.setMessage(NCMS.MSG_NC_TITLE_EXISTS);
                NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
                beanNCAdd.setDBError(true);
                beanNCAdd.setNCModel(modelNC);
                createNCAddForm(request, response);
                beanNCAdd.setDBError(false);
                
                // Mark new record as not saved, to avoid refresh web page action
                session.setAttribute("saved", NCMS.MSG_NO);
            }
            else {
                // Marks action save, avoid duplicated save when user
                // refresh page (press F5 button) after record just saved
                session.setAttribute("saved", NCMS.MSG_YES);
                getNCList(request, response);
            }
        }
    }

    /**
     * saveUpdateNC
     * Update NC
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void saveUpdateNC(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            session.setAttribute("beanUserInfo", beanUserInfo);
            NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
            NCModel modelNC = beanNCAdd.getNCModel();
            
            modelNC.setNCID(request.getParameter("hidID"));
            modelNC.setNCLevel(request.getParameter("optLevel"));
            modelNC.setProjectID(request.getParameter("optProject"));
            modelNC.setNCType(request.getParameter("optTypeOfNC"));
            modelNC.setDetectedBy(request.getParameter("optDetectedBy"));
            modelNC.setCode(request.getParameter("txtTitle").trim());
            modelNC.setDescription(request.getParameter("txtDescription").trim());
            modelNC.setCreator(request.getParameter("txtCreator"));
            modelNC.setCreateDate(request.getParameter("txtCreationDate"));
            modelNC.setStatus(request.getParameter("optStatus"));
            modelNC.setTypeOfCause(request.getParameter("optTypeOfCause"));
            modelNC.setCause(request.getParameter("txtCause").trim());
            modelNC.setProcess(request.getParameter("optProcesses"));
            modelNC.setImpact(request.getParameter("txtImpact").trim());
            modelNC.setTypeOfAction(request.getParameter("optTypeOfAction"));
            modelNC.setCPAction(request.getParameter("txtCPAction").trim());
            modelNC.setAssignee(request.getParameter("optAssignee"));
            modelNC.setDeadLine(request.getParameter("txtDeadLine"));
            modelNC.setRepeat(request.getParameter("optRepeat"));
			modelNC.setEffectOfChange(request.getParameter("txtEffectOfChange").trim());
            modelNC.setNote(request.getParameter("txtNote").trim());
            modelNC.setClosureDate(request.getParameter("txtClosureDate"));
            modelNC.setReviewer(request.getParameter("txtReviewer"));
            modelNC.setReviewDate(request.getParameter("txtReviewDate"));
            modelNC.setKPA(request.getParameter("optKPA"));
            modelNC.setISOClause(request.getParameter("optISOClause"));
            modelNC.setGroupName(request.getParameter("optGroup"));

            NCBO boNC = new NCBO();
            int nRetVal = boNC.updateNC(modelNC, beanUserInfo);
            if (nRetVal <= 0) {
                //beanUserInfo.setMessage(NCMS.MSG_NC_TITLE_EXISTS);
                beanNCAdd.setDBError(true);
                createNCUpdateForm(request, response);
                beanNCAdd.setDBError(false);
            }
            else {
                getNCList(request, response);
            }
        }
    }

    /**
     * exportNC
     * Prepare data for export NC list to Excel
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void exportNC(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            //NCListBean beanNCList = new NCListBean();
            NCListBean beanNCList = (NCListBean) session.getAttribute("beanNCList");
            //String strListingMode = request.getParameter("hidMode");
            // Exported in personal view
            //if (NCMS.PERSONAL_MODE.equals(beanUserInfo.getListingMode())) {
                //beanNCList = (NCListPersonalBean) session.getAttribute("beanNCListPersonal");
            //}
            
            String strLastFields = beanNCList.getFields();
            // NCMS
            if (beanUserInfo.getLocation() == 0) {
                beanNCList.setFields("Status,Code,ProjectID,GroupName,Assignee," +
                        "NCLevel,NCType,DetectedBy,Creator,CreationDate,Process," +
                        "ISOClause,Description,Cause,TypeOfCause,Impact," +
                        "CPAction,TypeOfAction,DeadLine,ClosureDate,Repeat,EffectOfChange,Note," +
                        "KPA,Reviewer");
            }
            else {//if (beanUserInfo.getLocation() == 1) {
                beanNCList.setFields("Status,NCID,ProjectID,GroupName," +
                        "Assignee,Repeat,Description," +
                        "Cause,TypeOfCause,Creator,CreationDate," +
                        "ReviewDate,DeadLine,ClosureDate,CPAction," +
                        "TypeOfAction,Impact,Process");
            }
            NCBO boNC = new NCBO();
            beanNCList = boNC.getNCListExport(beanUserInfo, beanNCList);
            if (beanUserInfo.getLocation() == 0) {
                callPage(response, NCMS.JSP_NC_EXPORT, request);
            }
            else {//if (beanUserInfo.getLocation() == 1) {
                callPage(response, NCMS.JSP_CALL_LOG_EXPORT, request);
            }
            
            // Restore last setting
            beanNCList.setFields(strLastFields);
        }
    }

    /**
     * getNCReport
     * Get report
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void getNCReport(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            NCReportBean beanNCReport =
                    (NCReportBean) session.getAttribute("beanNCReport");
            if (beanNCReport == null) {
                beanNCReport = new NCReportBean();
            }
            else {
                String strTypeOfCause = request.getParameter("optTypeOfCause");
                if ((strTypeOfCause != null) && (!"".equals(strTypeOfCause))) {
                    beanNCReport.setTypeOfCause(Integer.parseInt(strTypeOfCause));
                }
    
                String strGroupBy = request.getParameter("optReportBy");
                if (strGroupBy != null) {
                    beanNCReport.setGroupBy(strGroupBy);
                }
    
                String strFromDate = request.getParameter("txtFromDate");
                if (strFromDate != null) {
                    beanNCReport.setFromDate(strFromDate);
                }
    
                String strToDate = request.getParameter("txtToDate");
                if (strToDate != null) {
                    beanNCReport.setToDate(strToDate);
                }
                String strCheckNC = request.getParameter("chkNC");
                String strCheckOB = request.getParameter("chkOB");
                String strCheckCC = request.getParameter("chkCC");
				String strCheckPB = request.getParameter("chkPB");
                // Switch from another page
                if ((strCheckNC != null) || (strCheckOB != null) ||
                    (strCheckCC != null)||(strCheckPB != null))
                {
                    beanNCReport.setCheckedNC(false);
                    beanNCReport.setCheckedOB(false);
                    beanNCReport.setCheckedCC(false);
					beanNCReport.setCheckedPB(false);
                    if ("on".equals(strCheckNC)) {
                        beanNCReport.setCheckedNC(true);
                    }
                    if ("on".equals(strCheckOB)) {
                        beanNCReport.setCheckedOB(true);
                    }
                    if ("on".equals(strCheckCC)) {
                        beanNCReport.setCheckedCC(true);
                    }
					if ("on".equals(strCheckPB)) {
						beanNCReport.setCheckedPB(true);
					}
                }
            }

            ConstantBO boConstant = new ConstantBO();
            boConstant.setUsage(beanUserInfo.getLocation());
            beanNCReport.setComboGroupBy(ConstantBO.getGroupByList());
            beanNCReport.setComboTypeOfCause(boConstant.getTypeOfCauseList(
                    NCMS.CBO_ALL_VALUE));

            NCBO boNC = new NCBO();
            beanNCReport = boNC.getNCReport(beanNCReport, beanUserInfo);
            session.setAttribute("beanNCReport", beanNCReport);
            if (beanUserInfo.getLocation() == 0) {
                callPage(response, NCMS.JSP_NC_REPORT, request);
            }
            else {//if (beanUserInfo.getLocation() == 1) {
                callPage(response, NCMS.JSP_CALL_LOG_REPORT, request);
            }
        }
    }

    /**
     * getNCReportPivot
     * Get report
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void getNCReportPivot(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            NCReportPivotBean beanNCReportPivot =
                    (NCReportPivotBean) session.getAttribute("beanNCReportPivot");
            if (beanNCReportPivot == null) {
                beanNCReportPivot = new NCReportPivotBean();
                // CallLog
                if (beanUserInfo.getLocation() >= 1) {
                    beanNCReportPivot.setReportType("Repeat");
                }
            }
            else {
                String strTypeOfCause = request.getParameter("optTypeOfCauseP");
                if ((strTypeOfCause != null) && (!"".equals(strTypeOfCause))) {
                    beanNCReportPivot.setTypeOfCause(Integer.parseInt(strTypeOfCause));
                }
    
                String strGroupBy = request.getParameter("optReportByP");
                if (strGroupBy != null) {
                    beanNCReportPivot.setGroupBy(strGroupBy);
                }
                String strReportType = request.getParameter("optReportTypeP");
                if (strReportType != null) {
                    beanNCReportPivot.setReportType(strReportType);
                }
                String strFromDate = request.getParameter("txtFromDateP");
                if (strFromDate != null) {
                    beanNCReportPivot.setFromDate(strFromDate);
                }
                String strToDate = request.getParameter("txtToDateP");
                if (strToDate != null) {
                    beanNCReportPivot.setToDate(strToDate);
                }
                String strCheckNC = request.getParameter("chkPivotNC");
                String strCheckOB = request.getParameter("chkPivotOB");
                String strCheckCC = request.getParameter("chkPivotCC");
				String strCheckPB = request.getParameter("chkPivotPB");
                // Switch from another page
                if ((strCheckNC != null) || (strCheckOB != null) ||
                    (strCheckCC != null)||(strCheckPB != null))
                {
                    beanNCReportPivot.setCheckedNC(false);
                    beanNCReportPivot.setCheckedOB(false);
                    beanNCReportPivot.setCheckedCC(false);
					beanNCReportPivot.setCheckedPB(false);
                    if ("on".equals(strCheckNC)) {
                        beanNCReportPivot.setCheckedNC(true);
                    }
                    if ("on".equals(strCheckOB)) {
                        beanNCReportPivot.setCheckedOB(true);
                    }
                    if ("on".equals(strCheckCC)) {
                        beanNCReportPivot.setCheckedCC(true);
                    }
					if ("on".equals(strCheckPB)) {
						beanNCReportPivot.setCheckedPB(true);
					}
                }
            }
            
            ConstantBO boConstant = new ConstantBO();
            // Not include Request type(support group) in constant list of
            // distribution report
            //boConstant.setExcludeConstants("'TypeOfCause'");
            boConstant.setUsage(beanUserInfo.getLocation());

            beanNCReportPivot.setComboGroupBy(ConstantBO.getGroupByList());
            beanNCReportPivot.setComboReportType(boConstant.getConstantTypeList(
                    false, true));
            beanNCReportPivot.setNumReportField(boConstant.getNumReportField(
                    beanNCReportPivot.getReportType()));
            beanNCReportPivot.setComboTypeOfCause(boConstant.getTypeOfCauseList(
                    NCMS.CBO_ALL_VALUE));

            NCBO boNC = new NCBO();
            beanNCReportPivot = boNC.getNCReportPivot(beanNCReportPivot, beanUserInfo);
            session.setAttribute("beanNCReportPivot", beanNCReportPivot);
            
            if (beanUserInfo.getLocation() == 0) {
                callPage(response, NCMS.JSP_NC_REPORT_PIVOT, request);
            }
            else {//if (beanUserInfo.getLocation() == 1) {
                callPage(response, NCMS.JSP_CALL_LOG_REPORT_PIVOT, request);
            }
        }
    }

    /**
     * getViewList
     * Get list of views
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void getViewList(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            ViewListBean beanViewList = new ViewListBean();
            ViewBO boView = new ViewBO();
            StringMatrix smList = boView.getViewList(beanUserInfo);
            beanViewList.setViewList(smList);
            session.setAttribute("beanViewList", beanViewList);
            callPage(response, NCMS.JSP_VIEW_LIST, request);
        }
    }

    /**
     * createViewAddForm
     * Create form for adding new view
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void createViewAddForm(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            ViewBO boView = new ViewBO();
            ViewAddBean beanViewAdd = new ViewAddBean();
            beanViewAdd.setComboOrderBy(ViewBO.getComboOrderBy());
            session.setAttribute("beanViewAdd", beanViewAdd);
            callPage(response, NCMS.JSP_VIEW_DETAIL, request);
        }
    }

    /**
     * createViewUpdateForm
     * Create form for updating view
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void createViewUpdateForm(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            ViewBO boView = new ViewBO();
            String strViewID = request.getParameter("hidID");
            ViewAddBean beanViewAdd = boView.getViewByID(strViewID);
            beanViewAdd.setComboOrderBy(ViewBO.getComboOrderBy());
            session.setAttribute("beanViewAdd", beanViewAdd);
            callPage(response, NCMS.JSP_VIEW_UPDATE, request);
        }
    }

    /**
     * saveNewView
     * Add new view to database
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void saveNewView(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            ViewAddBean beanViewAdd = new ViewAddBean();
            beanViewAdd.setTitle(request.getParameter("txtTitle").trim());

            String strField = request.getParameter("hidFields");

            if (strField != null) {
                beanViewAdd.setField(strField.trim());
            }

            beanViewAdd.setOrderBy(request.getParameter("cboOrderBy"));

            ViewBO boView = new ViewBO();
            boolean isSuccess = boView.addView(beanUserInfo, beanViewAdd);

            if (!isSuccess) {
                beanUserInfo.setMessage(NCMS.MSG_VIEW_TITLE_EXISTS);
            }
        }
    }

    /**
     * saveUpdateView
     * Update view information
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void saveUpdateView(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            ViewAddBean beanViewAdd = (ViewAddBean)session.getAttribute(
                    "beanViewAdd");
            String strTitle = request.getParameter("txtTitle").trim();
            String strField = request.getParameter("hidFields");
            String strOrderBy = request.getParameter("cboOrderBy");

            if (!(beanViewAdd.getTitle().equals(strTitle) &&
                    beanViewAdd.getField().equals(strField) &&
                    beanViewAdd.getOrderBy().equals(strOrderBy))) {
                // Something has changed, view must be updated
                beanViewAdd.setTitle(strTitle);

                if (strField != null) {
                    beanViewAdd.setField(strField.trim());
                }

                beanViewAdd.setOrderBy(strOrderBy);

                ViewBO boView = new ViewBO();
                boolean isSuccess = boView.updateView(beanUserInfo, beanViewAdd);

                if (!isSuccess) {
                    beanUserInfo.setMessage(NCMS.MSG_VIEW_TITLE_EXISTS);
                }
            }
        }
    }

    /**
     * deleteView
     * Remove view from database
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void deleteView(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            String strViewID = request.getParameter("hidID");

            if (strViewID != null) {
                ViewBO boView = new ViewBO();
                boView.deleteView(strViewID);
            }
        }
    }

    /**
     * getConstantlist
     * Get list of NC constants
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void getConstantList(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            ConstantListBean beanConstantList =
                    (ConstantListBean) session.getAttribute("beanConstantList");
            if (beanConstantList == null) {
                beanConstantList = new ConstantListBean();
                session.setAttribute("beanConstantList", beanConstantList);
            }
            ConstantBO boConstant = new ConstantBO();
            boConstant.setUsage(beanUserInfo.getLocation());
            String strSortBy = request.getParameter("cboSortBy");

            if (strSortBy != null) {
                beanConstantList.setSortBy(strSortBy);
            }

            String strNumPage = request.getParameter("PageNumber");

            if (strNumPage != null) {
                beanConstantList.setNumPage(Integer.parseInt(strNumPage));
            }
            
            beanConstantList = boConstant.getConstantList(beanConstantList, false);
            session.setAttribute("beanConstantList", beanConstantList);
            callPage(response, NCMS.JSP_CONSTANT_LIST, request);
        }
    }

    /**
     * createConstantAddForm
     * Create form for adding new constant
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void createConstantAddForm(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            ConstantAddBean beanConstantAdd = new ConstantAddBean();
            ConstantBO boConstant = new ConstantBO();
            boConstant.setUsage(beanUserInfo.getLocation());
            beanConstantAdd.setComboConstantType(boConstant.getConstantTypeList(
                    false, false));
            session.setAttribute("beanConstantAdd", beanConstantAdd);
            callPage(response, NCMS.JSP_CONSTANT_DETAIL, request);
        }
    }

    /**
     * saveNewConstant
     * Save new constant
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void saveNewConstant(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");
        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            ConstantAddBean beanConstantAdd = new ConstantAddBean();
            String[] arrDescription = request.getParameterValues(
                    "txtDescription");
            String[] arrConstantType = request.getParameterValues(
                    "txtConstantType");
            StringMatrix smList = new StringMatrix();
            StringVector svRow = new StringVector(2);

            if (arrDescription != null) {
                for (int i = 0; i < arrDescription.length; i++) {
                    if (arrDescription[i].trim().length() > 0) {
                        svRow.setCell(0, arrDescription[i].trim());
                        svRow.setCell(1, arrConstantType[i]);
                        smList.addRow(svRow);
                    }
                }
            }
            beanConstantAdd.setConstantList(smList);
            ConstantBO boConstant = new ConstantBO();
            boConstant.setUsage(beanUserInfo.getLocation());
            boolean isSuccess = boConstant.addConstant(beanConstantAdd);
            if (!isSuccess) {
                beanUserInfo.setMessage(NCMS.MSG_CONSTANT_EXISTS);
            }
        }
    }

    /**
     * deleteConstant
     * Delete constant
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void deleteConstant(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            ConstantAddBean beanConstantAdd = new ConstantAddBean();
            String[] arrConstantID = request.getParameterValues("checkbox");
            StringMatrix smList = new StringMatrix();
            StringVector svRow = new StringVector(1);

            if (arrConstantID != null) {
                for (int i = 0; i < arrConstantID.length; i++) {
                    if (arrConstantID[i].length() > 0) {
                        svRow.setCell(0, arrConstantID[i]);
                        smList.addRow(svRow);
                    }
                }
            }

            beanConstantAdd.setConstantList(smList);
            ConstantBO boConstant = new ConstantBO();
            boConstant.setUsage(beanUserInfo.getLocation());
            boolean isSuccess = boConstant.deleteConstant(beanConstantAdd);

            if (!isSuccess) {
                beanUserInfo.setMessage(NCMS.MSG_CONSTANT_LINKED);
            }
        }
    }

    /**
     * saveUpdateConstant
     * Update constant
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void saveUpdateConstant(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            ConstantAddBean beanConstantAdd = new ConstantAddBean();
            String[] arrConstantID = request.getParameterValues("hidCode");
            String[] arrDescription = request.getParameterValues(
                    "txtDescription");
            StringMatrix smList = new StringMatrix();
            StringVector svRow = new StringVector(2);

            if (arrConstantID != null) {
                for (int i = 0; i < arrConstantID.length; i++) {
                    if (arrConstantID[i].length() > 0) {
                        svRow.setCell(0, arrConstantID[i]);
                        svRow.setCell(1, arrDescription[i].trim());
                        smList.addRow(svRow);
                    }
                }
            }
            beanConstantAdd.setConstantList(smList);
            ConstantBO boConstant = new ConstantBO();
            boConstant.setUsage(beanUserInfo.getLocation());
            boolean isSuccess = boConstant.updateConstant(beanConstantAdd);

            if (!isSuccess) {
                beanUserInfo.setMessage(NCMS.MSG_VALUE_EXISTS);
            }
        }
    }

    /**
     * createConstantUpdateForm
     * Create form for updating constant
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void createConstantUpdateForm(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            ConstantAddBean beanConstantAdd = new ConstantAddBean();
            String[] arrConstantID = request.getParameterValues("checkbox");
            String strConstantID = "";

            if (arrConstantID != null) {
                for (int i = 0; i < arrConstantID.length; i++) {
                    if (arrConstantID[i].length() > 0) {
                        strConstantID += ("," + arrConstantID[i]);
                    }
                }
            }

            strConstantID = "(" + strConstantID.substring(1) + ")";

            ConstantBO boConstant = new ConstantBO();
            boConstant.setUsage(beanUserInfo.getLocation());
            beanConstantAdd.setConstantList(boConstant.getConstantByID(
                    strConstantID));
            beanConstantAdd.setComboConstantType(boConstant.getConstantTypeList(
                    false, false));
            session.setAttribute("beanConstantAdd", beanConstantAdd);
            callPage(response, NCMS.JSP_CONSTANT_UPDATE, request);
        }
    }
    
    /**
     * getSearchCondition
     * @author MinhPT
     * @param   request - the request object.
     * @param   response - the response object.
     * @param   nViewType 0: System view, 1: Personal view
     * @throws  Exception
     */
    private String getSearchCondition(HttpServletRequest request,
            HttpServletResponse response, int nViewType) throws Exception {
        String strCondition = "";
        HttpSession session = request.getSession();
        
        UserInfoBean beanUserInfo =
                (UserInfoBean) session.getAttribute("beanUserInfo");
        NCListBean beanNCList =
                (NCListBean) session.getAttribute("beanNCList");
        NCListPersonalBean beanNCListPersonal =
                (NCListPersonalBean) session.getAttribute("beanNCListPersonal");
        NCModel modelNC;
        
        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            if (nViewType ==0){
                modelNC = beanNCList.getNCModel();
                beanNCList.setCurrentFromDate(getParam(request, "txtFromDate"));
                beanNCList.setCurrentToDate(getParam(request, "txtToDate"));
            }
            else {
                modelNC = beanNCListPersonal.getNCModel();
                beanNCListPersonal.setCurrentFromDate(getParam(request, "txtFromDate"));
                beanNCListPersonal.setCurrentToDate(getParam(request, "txtToDate"));
            }
            
            modelNC.setCreator(getParam(request,"cboCreator"));
            modelNC.setAssignee(getParam(request,"cboAssignee"));
            modelNC.setReviewer(getParam(request,"cboReviewer"));
            modelNC.setNCLevel(getParam(request,"cboLevel"));
            modelNC.setNCType(getParam(request,"cboTypeOfNC"));
            modelNC.setGroupName(getParam(request,"cboGroup"));
            modelNC.setProjectID(getParam(request,"cboProject"));
            modelNC.setProcess(getParam(request,"cboProcess"));
            modelNC.setDetectedBy(getParam(request,"cboDetectedBy"));
            modelNC.setStatus(getParam(request,"cboStatus"));
            modelNC.setKPA(getParam(request,"cboKPA"));
            modelNC.setISOClause(getParam(request,"cboISOClause"));
            modelNC.setTypeOfCause(getParam(request,"cboTypeOfCause"));
            modelNC.setTypeOfAction(getParam(request,"cboTypeOfAction"));
            modelNC.setRepeat(getParam(request,"cboRepeat"));
            modelNC.setPrjStatus(getParam(request,"cboPrjStatus"));
            //modelNC.setPriority(getParam(request,"cboPriority"));
            
            strCondition = createCondition(modelNC);
        }
        return strCondition;
    }
    /**
     * getParameter from request
     * if request param null, it get parameter from session
     * @author MinhPT
     * @param   request - the request object.
     * @throws  Exception
     */
    private String getParam(HttpServletRequest request, String strParam)throws Exception{
        String strReturn = null;
        HttpSession session = request.getSession();
        if (request.getParameter(strParam) != null) {
            strReturn = request.getParameter(strParam);
            session.setAttribute(strParam, strReturn);
        }
        else if (session.getAttribute(strParam) != null) {
            strReturn = (String)session.getAttribute(strParam);
        }
        return strReturn;
    }
    /**
     * getParameter from request
     * if request param null, it get parameter from session
     * @author MinhPT
     * @param   request - the request object.
     * @throws  Exception
     */
    private String createCondition(NCModel modelNC){
        StringBuffer strBufferSql = new StringBuffer();
        strBufferSql.append(addSqlCondition("Creator", modelNC.getCreator()));
        strBufferSql.append(addSqlCondition("Assignee", modelNC.getAssignee()));
        strBufferSql.append(addSqlCondition("Reviewer", modelNC.getReviewer()));
        strBufferSql.append(addSqlCondition("NCLevel", modelNC.getNCLevel()));
        strBufferSql.append(addSqlCondition("NCType", modelNC.getNCType()));
        strBufferSql.append(addSqlCondition("GroupName", modelNC.getGroupName()));
        strBufferSql.append(addSqlCondition("ProjectID", modelNC.getProjectID()));
        strBufferSql.append(addSqlCondition("Process", modelNC.getProcess()));
        strBufferSql.append(addSqlCondition("DetectedBy", modelNC.getDetectedBy()));
        strBufferSql.append(addSqlCondition("KPA", modelNC.getKPA()));
        strBufferSql.append(addSqlCondition("ISOClause", modelNC.getISOClause()));
        strBufferSql.append(addSqlCondition("TypeOfCause", modelNC.getTypeOfCause()));
        strBufferSql.append(addSqlCondition("TypeOfAction", modelNC.getTypeOfAction()));
        strBufferSql.append(addSqlCondition("Status", modelNC.getStatus()));
        strBufferSql.append(addSqlCondition("Repeat", modelNC.getRepeat()));
        //strBufferSql.append(addSqlCondition("Priority", modelNC.getPriority()));
        //date */
        return strBufferSql.toString();
    }
    
    /**
     * Add SQL condition for String field
     * @author MinhPT
     * @param   strFieldName  - the name of field
     * @param   strFieldValue - the name of field
     * 15-Dec-03
     */
    private String addSqlCondition(String strFieldName, String strValue) {
        StringBuffer strBuffer = new StringBuffer();
        String strAll = NCMS.CBO_ITEM_ALL_STRING;
        String strNone = NCMS.CBO_ITEM_NONE_STRING;

        if (strValue != null) {
            if (strNone.equals(strValue)) {
                strBuffer.append(" AND ").append(strFieldName).append(" IS NULL ");
            }
            else if ( !(strAll.equals(strValue) || "".equals(strValue))) {
                strBuffer.append(" AND ").append(strFieldName).append("='").append(strValue).append("'");
            }
        }

        return strBuffer.toString();
    }
    
    /**
     * Add SQL condition for int field
     * @author MinhPT
     * @param   strFieldName  - the name of field
     * @param   strFieldValue - the name of field
     * 15-Dec-03
     */
    private String addSqlCondition(String strFieldName, int nValue) {
        StringBuffer strBuffer = new StringBuffer();
        int nAll = Integer.parseInt(NCMS.CBO_ITEM_ALL_VALUE);
        int nNone = Integer.parseInt(NCMS.CBO_ITEM_NONE_VALUE);

        if (nValue > 0) {
            if (nNone == nValue) {
                strBuffer.append(" AND ").append(strFieldName).append(" IS NULL ");
            }
            else if (nAll != nValue) {
                strBuffer.append(" AND ")
                         .append(strFieldName).append("=").append(nValue);
            }
        }
        return strBuffer.toString();
    }
    /**
     * createReportCC
     * Create reort for Customer Complaint
     * @author MinhPT
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void createReportCC(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        callPage(response, NCMS.JSP_EXPORT_CC, request);
    }
    /**
     * createReportNC
     * Create report for Nonconfornmity Complaint
     * @author MinhPT
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void createReportNC(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        callPage(response, NCMS.JSP_EXPORT_NC, request);
    }

    /**
     * createNCHistoryForm
     * Get history
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void createNCHistoryForm(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            //session.setAttribute("beanUserInfo", beanUserInfo);
            NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
            if (beanUserInfo.getLocation() == 0) {
                callPage(response, NCMS.JSP_NC_HISTORY, request);
            }
            else {
                callPage(response, NCMS.JSP_CALL_LOG_HISTORY, request);
            }
        }
    }

    /**
     * getCallList
     * Get Call Log list
     * @author MinhPT
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void getCallList(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");
        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            String strListingMode = request.getParameter("hidMode");

            if (strListingMode != null) {
                beanUserInfo.setListingMode(strListingMode);
            }
            // Clear informations
            beanUserInfo.setCurrentNC(null);
            
            if (NCMS.SYSTEM_MODE.equals(beanUserInfo.getListingMode())) {
                getCallListSystem(request, response, beanUserInfo);
                callPage(response, NCMS.JSP_CALL_LOG_LIST, request);
            }
            else if (NCMS.PERSONAL_MODE.equals(beanUserInfo.getListingMode())) {
                getCallListPersonal(request, response, beanUserInfo);
                callPage(response, NCMS.JSP_CALL_LOG_LIST_PERSONAL, request);
            }
        }
    }

    /**
     * getCallListSystem
     * Get NC list
     * @author MinhPT
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void getCallListSystem(HttpServletRequest request,
            HttpServletResponse response, UserInfoBean beanUserInfo) throws Exception {
        HttpSession session = request.getSession();
        NCListBean beanNCList = (NCListBean) session.getAttribute("beanNCList");
        NCModel modelNC = new NCModel();
        int nOldStatus = NCMS.NC_STATUS_ALL;
        if (beanNCList == null) {
            beanNCList = new NCListBean();
            beanNCList.setNCModel(modelNC);
            session.setAttribute("beanNCList", beanNCList);
        }
        else {
            modelNC = beanNCList.getNCModel();
        }
        
        beanNCList.setCondition(getSearchCondition(request , response,0));
        
        NCBO boNC = new NCBO();
        String strOrderBy = request.getParameter("hidOrderBy");
        String strDirection = request.getParameter("hidDirection");
        String strNumPage = request.getParameter("PageNumber");
        if (strOrderBy != null) {
            beanNCList.setOrderBy(strOrderBy);
        }
        if (strDirection != null) {
            beanNCList.setDirection(Integer.parseInt(strDirection));
        }
        if (strNumPage != null) {
            beanNCList.setNumPage(Integer.parseInt(strNumPage));
        }

        //added by MinhPT
        //10-Dec-03
        //For set Condition Search
        CommonBO boCommon = new CommonBO();
        ConstantBO boConstant = new ConstantBO();
        boConstant.setUsage(beanUserInfo.getLocation());
        nOldStatus = modelNC.getStatus();
        modelNC.setStatus(NCMS.NC_STATUS_ALL);
        beanNCList.setComboStatus(boConstant.getStatusList(
                NCMS.CBO_ALL_VALUE, beanUserInfo, modelNC));
        modelNC.setStatus(nOldStatus);
        
        beanNCList.setComboCreator(boCommon.getUserList(beanUserInfo,
                NCMS.CBO_ALL_VALUE, NCMS.COMBO_CREATOR));
        beanNCList.setComboReviewer(boCommon.getUserList(beanUserInfo,
                NCMS.CBO_BOTH_ALL_AND_NONE_VALUE, NCMS.COMBO_ASSIGNEE));
        beanNCList.setComboAssignee(boCommon.getUserList(beanUserInfo,
                NCMS.CBO_BOTH_ALL_AND_NONE_VALUE, NCMS.COMBO_ASSIGNEE));
        
        beanNCList.setComboProject(boCommon.getProjectList(
                NCMS.CBO_BOTH_ALL_AND_GENERAL_VALUE, NCMS.NONGROUP_VALUE, beanUserInfo.getLocation()));
        beanNCList.setComboTypeOfNC(boConstant.getTypeOfNCList(
                NCMS.CBO_ALL_VALUE));
        beanNCList.setComboLevel(boConstant.getLevelList(NCMS.CBO_ALL_VALUE));
        beanNCList.setComboGroup(boCommon.getGroupList(NCMS.CBO_ALL_VALUE));
        beanNCList.setComboPriority(boConstant.getPriorityList(
                NCMS.CBO_ALL_VALUE));
        beanNCList.setComboTypeOfAction(boConstant.getTypeOfActionList(
                NCMS.CBO_ALL_VALUE));
        beanNCList.setComboProcess(boConstant.getProcessList(
                NCMS.CBO_ALL_VALUE));
        beanNCList.setComboISOClause(boConstant.getISOClauseList(
                NCMS.CBO_ALL_VALUE));
        beanNCList.setComboKPA(boConstant.getKPAList(NCMS.CBO_ALL_VALUE));
        beanNCList.setComboTypeOfCause(boConstant.getTypeOfCauseList(
                NCMS.CBO_ALL_VALUE));
        beanNCList = boNC.getNCList(beanUserInfo, beanNCList);
    }

    /**
     * getCallListPersonal
     * Get list of Call Log applied with personal view
     * @author MinhPT
     * @param   request - the request object.
     * @param   response - the response object.
     * @param   beanUserInfo - session variable contains user's information
     * @throws  Exception
     */
    private void getCallListPersonal(HttpServletRequest request,
            HttpServletResponse response, UserInfoBean beanUserInfo)
            throws Exception {
        HttpSession session = request.getSession();
        NCListPersonalBean beanNCListPersonal =
            (NCListPersonalBean) session.getAttribute("beanNCListPersonal");
        NCModel modelNC = null;
        int nOldStatus = NCMS.NC_STATUS_ALL;
        if (beanNCListPersonal == null) {
            beanNCListPersonal = new NCListPersonalBean();
            modelNC = new NCModel();
            beanNCListPersonal.setNCModel(modelNC);
            session.setAttribute("beanNCListPersonal", beanNCListPersonal);
        }
        else {
            modelNC = beanNCListPersonal.getNCModel();
        }
        beanNCListPersonal.setCondition(getSearchCondition(request , response,1));

        ViewBO boView = new ViewBO();
        StringMatrix smList = boView.getViewList(beanUserInfo);

        if (smList == null) {
            // there's no defined view, the system view will be default
            smList = new StringMatrix(1, 4);
            smList.setCell(0, 0, "0");
            smList.setCell(0, 1, "System View");

            /* fields Status, Description, NCType, CreationDate,
               NCID are mandatory */
            smList.setCell(0, 2, "Status,Description,NCType,CreationDate,NCID");
            smList.setCell(0, 3, "Status");
            beanNCListPersonal.setViewID(smList.getCell(0, 0));
            beanNCListPersonal.setFields(smList.getCell(0, 2));
            beanNCListPersonal.setOrderBy(smList.getCell(0, 3));
        }
        else {
            String strViewID = request.getParameter("cboView");

            if (strViewID != null) {
                beanNCListPersonal.setViewID(strViewID);

                int nRow = smList.indexOf(strViewID, 0);
                beanNCListPersonal.setFields(
                        "Status,Description,NCType,CreationDate," +
                        smList.getCell(nRow, 2) + ",NCID");
                beanNCListPersonal.setOrderBy(smList.getCell(nRow, 3));
            }
            else {
                // The first view will be active
                beanNCListPersonal.setViewID(smList.getCell(0, 0));
                beanNCListPersonal.setFields(
                        "Status,Description,NCType,CreationDate," +
                        smList.getCell(0, 2) + ",NCID");
                beanNCListPersonal.setOrderBy(smList.getCell(0, 3));
            }
        }

        beanNCListPersonal.setComboView(smList);

        String strNumPage = request.getParameter("PageNumber");
        String strDirection = request.getParameter("hidDirection");

        if (strNumPage != null) {
            beanNCListPersonal.setNumPage(Integer.parseInt(strNumPage));
        }
        if (strDirection != null) {
            beanNCListPersonal.setDirection(Integer.parseInt(strDirection));
        }

        //added by MinhPT
        //10-Dec-03
        //For set Condition Search
        CommonBO boCommon = new CommonBO();
        ConstantBO boConstant = new ConstantBO();
        boConstant.setUsage(beanUserInfo.getLocation());
        nOldStatus = modelNC.getStatus();
        modelNC.setStatus(NCMS.NC_STATUS_ALL);
        beanNCListPersonal.setComboStatus(boConstant.getStatusList(
                NCMS.CBO_ALL_VALUE, beanUserInfo, modelNC));
        // Restore original status
        modelNC.setStatus(nOldStatus);
        
        beanNCListPersonal.setComboCreator(boCommon.getUserList(beanUserInfo,
                    NCMS.CBO_OTHER_VALUE, NCMS.COMBO_CREATOR));
        // Temporary remove
        //beanNCListPersonal.setCurrentCreator(beanUserInfo.getLoginName());
        beanNCListPersonal.setComboReviewer(boCommon.getUserList(beanUserInfo,
                    NCMS.CBO_ALL_VALUE, NCMS.COMBO_ASSIGNEE));
        beanNCListPersonal.setComboAssignee(boCommon.getUserList(beanUserInfo,
                    NCMS.CBO_BOTH_ALL_AND_NONE_VALUE, NCMS.COMBO_ASSIGNEE));
        
        beanNCListPersonal.setComboTypeOfAction(boConstant.getTypeOfActionList(
                    NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboLevel(boConstant.getLevelList(NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboProcess(boConstant.getProcessList(
                    NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboGroup(boCommon.getGroupList(NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboDetectedBy(boConstant.getDetectedByList(
                    NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboProject(boCommon.getProjectList(
                    NCMS.CBO_BOTH_ALL_AND_GENERAL_VALUE, NCMS.NONGROUP_VALUE, beanUserInfo.getLocation()));
        beanNCListPersonal.setComboTypeOfNC(boConstant.getTypeOfNCList(
                    NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboISOClause(boConstant.getISOClauseList(
                    NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboKPA(boConstant.getKPAList(NCMS.CBO_ALL_VALUE));
        beanNCListPersonal.setComboTypeOfCause(boConstant.getTypeOfCauseList(
                    NCMS.CBO_ALL_VALUE));
        NCBO boNC = new NCBO();
        
        beanNCListPersonal = boNC.getNCListPersonal(beanUserInfo,
                beanNCListPersonal);
    }

    /**
     * createAddCallLog
     * Create Add Page Call Log
     * @author MinhPT
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void createCallLogAddForm(HttpServletRequest request,
                                      HttpServletResponse response)
                                      throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo =
            (UserInfoBean) session.getAttribute("beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
            if (beanNCAdd == null) {
                beanNCAdd = new NCAddBean();
                session.setAttribute("beanNCAdd", beanNCAdd);
            }
            NCModel modelNC = new NCModel();
            beanNCAdd.setNCModel(modelNC);
            
            CommonBO boCommon = new CommonBO();
            ConstantBO boConstant = new ConstantBO();
            boConstant.setUsage(beanUserInfo.getLocation());
            modelNC.setStatus(NCMS.NC_STATUS_NEW);
            
            beanNCAdd.setComboStatus(boConstant.getStatusList(
                NCMS.CBO_OTHER_VALUE, beanUserInfo, modelNC));
            
            beanNCAdd.setComboAssignee(boCommon.getUserList(beanUserInfo,
                    NCMS.CBO_EMPTY_VALUE, NCMS.COMBO_ASSIGNEE));
            beanNCAdd.setComboTypeOfAction(boConstant.getTypeOfActionList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboLevel(boConstant.getLevelList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboProcess(boConstant.getProcessList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboGroup(boCommon.getGroupList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboDetectedBy(boConstant.getDetectedByList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboProject(boCommon.getProjectList(
                    NCMS.CBO_OTHER_VALUE, NCMS.NONGROUP_VALUE, beanUserInfo.getLocation()));
            beanNCAdd.setComboTypeOfNC(boConstant.getTypeOfNCList(NCMS.CBO_OTHER_VALUE));
//            beanNCAdd.setComboISOClause(boConstant.getISOClauseList(
//                    NCMS.CBO_OTHER_VALUE));
//            beanNCAdd.setComboKPA(boConstant.getKPAList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboTypeOfCause(boConstant.getTypeOfCauseList(
                    NCMS.CBO_OTHER_VALUE));

            beanNCAdd.setComboPriority(boConstant.getPriorityList(
                    NCMS.CBO_OTHER_VALUE));

            // Set default values
            modelNC.setCreator(beanUserInfo.getLoginName());
            
            // Mark new record as not saved, to avoid refresh web page action
            // Refer function saveNewCallLog()
            session.setAttribute("saved", NCMS.MSG_NO);
            callPage(response, NCMS.JSP_CALL_LOG_DETAIL, request);
        }
    }
    /**
     * createCallLogUpdateForm
     * Create form for updating Call Log 
     * @author MinhPT
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void createCallLogUpdateForm(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
            NCModel modelNC = null;
            if (beanNCAdd == null) {
                beanNCAdd = new NCAddBean();
                modelNC = new NCModel();
                beanNCAdd.setNCModel(modelNC);
                session.setAttribute("beanNCAdd", beanNCAdd);
            }
            
            CommonBO boCommon = new CommonBO();
            ConstantBO boConstant = new ConstantBO();
            boConstant.setUsage(beanUserInfo.getLocation());
            NCBO boNC = new NCBO();
            String strNCID = request.getParameter("hidID");
            session.setAttribute("hidID", strNCID);
            // Get NCModel
            beanNCAdd = boNC.getNCDetail(strNCID, beanNCAdd,
                                         beanUserInfo.getLocation());
            modelNC = beanNCAdd.getNCModel();
            beanUserInfo.setCurrentNC(modelNC);
            
            beanNCAdd.setComboStatus(boConstant.getStatusList(
                NCMS.CBO_OTHER_VALUE, beanUserInfo, modelNC));
            beanNCAdd.setComboAssignee(boCommon.getUserList(beanUserInfo,
                    NCMS.CBO_EMPTY_VALUE, NCMS.COMBO_ASSIGNEE));
            
            beanNCAdd.setComboTypeOfAction(boConstant.getTypeOfActionList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboLevel(boConstant.getLevelList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboProcess(boConstant.getProcessList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboGroup(boCommon.getGroupList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboDetectedBy(boConstant.getDetectedByList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboProject(boCommon.getProjectList(
                    NCMS.CBO_OTHER_VALUE, NCMS.NONGROUP_VALUE, beanUserInfo.getLocation()));
            beanNCAdd.setComboTypeOfNC(boConstant.getTypeOfNCList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboISOClause(boConstant.getISOClauseList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboKPA(boConstant.getKPAList(NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboTypeOfCause(boConstant.getTypeOfCauseList(
                    NCMS.CBO_OTHER_VALUE));
            beanNCAdd.setComboPriority(boConstant.getPriorityList(
                    NCMS.CBO_OTHER_VALUE));

            session.setAttribute("beanNCAdd", beanNCAdd);
            callPage(response, NCMS.JSP_CALL_LOG_UPDATE, request);
        }
    }

    /**
     * saveNewCallLog
     * Create form for updating Call Log 
     * @author MinhPT
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void saveNewCallLog(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");
        NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        // Detect this record is already saved, avoid user refresh page
        else if (NCMS.MSG_YES.equals(session.getAttribute("saved"))) {
            getCallList(request, response);
        }
        else {
            //NCAddBean beanNCAdd = new NCAddBean();
            NCModel modelNC = new NCModel();
            
            //modelNC.setNCLevel(request.getParameter("optLevel"));
            // Currently CallLog only has Project level
            modelNC.setNCLevel(beanNCAdd.getComboLevel().getCell(0, 0));
            
            modelNC.setGroupName(request.getParameter("optGroup"));
            modelNC.setProjectID(request.getParameter("optProject"));
            //modelNC.setNCType(request.getParameter("optTypeOfNC"));
            // Currently CallLog only has one type "CallLog"
            modelNC.setNCType(beanNCAdd.getComboTypeOfNC().getCell(0, 0));
            
            modelNC.setDetectedBy(request.getParameter("optDetectedBy"));
            //modelNC.setCode(request.getParameter("txtTitle").trim());
            // Request title
            modelNC.setDescription(request.getParameter("txtDescription").trim());
            modelNC.setCreator(request.getParameter("txtCreator"));
            // Log Date, time
            modelNC.setCreateDate(request.getParameter("txtCreateDate"));
            modelNC.setCreateTime(request.getParameter("txtCreateTime"));
            
            modelNC.setStatus(request.getParameter("optStatus"));
            // Request type
            modelNC.setTypeOfCause(request.getParameter("optTypeOfCause"));
            // Solution
            modelNC.setCause(request.getParameter("txtCause").trim());
            modelNC.setProcess(request.getParameter("optProcess"));
            // Result
            modelNC.setImpact(request.getParameter("txtImpact").trim());
            // Type of Solution
            modelNC.setTypeOfAction(request.getParameter("optTypeOfAction"));
            // Request detail
            modelNC.setCPAction(request.getParameter("txtCPAction").trim());
            modelNC.setAssignee(request.getParameter("optAssignee"));
            modelNC.setDeadLine(request.getParameter("txtDeadLine"));
            modelNC.setDeadLineTime(request.getParameter("txtDeadLineTime"));
            modelNC.setRepeat(request.getParameter("optRepeat"));
            //modelNC.setNote(request.getParameter("txtNote").trim());
            modelNC.setReviewDate(request.getParameter("txtReviewDate"));
            modelNC.setReviewTime(request.getParameter("txtReviewTime"));
            modelNC.setClosureDate(request.getParameter("txtClosureDate"));
            modelNC.setClosureTime(request.getParameter("txtClosureTime"));
            /*
            modelNC.setPriority(request.getParameter("optPriority"));
            if ((NCMS.NC_STATUS_OPENED != modelNC.getStatus()) &&
                (modelNC.getStatus() > 0))
            {
                modelNC.setReviewer(beanUserInfo.getLoginName());
            }
            else
            {
                modelNC.setReviewer(request.getParameter("txtReviewer"));
            }
*/
            //logger.debug();
            NCBO boNC = new NCBO();
            int nRetVal = boNC.addNC(modelNC, beanUserInfo.getLocation());

            if (nRetVal <= 0) {
                beanNCAdd.setDBError(true);
                beanNCAdd.setNCModel(modelNC);
                createCallLogAddForm(request, response);
                beanNCAdd.setDBError(false);
                // Mark this record as not saved
                session.setAttribute("saved", NCMS.MSG_NO);
            }
            else {
                getCallList(request, response);
                String strNotify = request.getParameter("hidNotify");
                if ("1".equals(strNotify)) {
                    CommonBO boCommon = new CommonBO();
                    // Notify mail messages to users
                    boCommon.notifyCallChanged(beanUserInfo,
                            request.getParameter("hidAction"),
                            modelNC, modelNC);
                }
                
                // Marks action save, avoid duplicated save when user
                // refresh page (press F5 button) after record just saved
                session.setAttribute("saved", NCMS.MSG_YES);
            }
        }
    }

    /**
     * saveUpdateCallLog
     * update Call Log 
     * @author MinhPT
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void saveUpdateCallLog(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            session.setAttribute("beanUserInfo", beanUserInfo);
            NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
            NCModel modelNC = beanNCAdd.getNCModel();
            NCModel modelOld = (NCModel) beanNCAdd.getNCModel().clone();
            
            modelNC.setNCID(request.getParameter("hidID"));

            //modelNC.setNCLevel(request.getParameter("optLevel"));
            // Currently CallLog only has Project level
            modelNC.setNCLevel(beanNCAdd.getComboLevel().getCell(0, 0));
            
            modelNC.setGroupName(request.getParameter("optGroup"));
            modelNC.setProjectID(request.getParameter("optProject"));
            //modelNC.setNCType(request.getParameter("optTypeOfNC"));
            // Currently CallLog only has one type "CallLog"
            modelNC.setNCType(beanNCAdd.getComboTypeOfNC().getCell(0, 0));
            
            modelNC.setDetectedBy(request.getParameter("optDetectedBy"));
            //modelNC.setCode(request.getParameter("txtTitle").trim());
            // Request title
            modelNC.setDescription(request.getParameter("txtDescription").trim());
            modelNC.setCreator(request.getParameter("txtCreator"));
            // Log Date, time
            modelNC.setCreateDate(request.getParameter("txtCreateDate"));
            modelNC.setCreateTime(request.getParameter("txtCreateTime"));
            
            modelNC.setStatus(request.getParameter("optStatus"));
            // Request type
            modelNC.setTypeOfCause(request.getParameter("optTypeOfCause"));
            // Solution
            modelNC.setCause(request.getParameter("txtCause").trim());
            modelNC.setProcess(request.getParameter("optProcess"));
            // Result
            modelNC.setImpact(request.getParameter("txtImpact").trim());
            // Type of Solution
            modelNC.setTypeOfAction(request.getParameter("optTypeOfAction"));
            // Request detail
            modelNC.setCPAction(request.getParameter("txtCPAction").trim());
            modelNC.setAssignee(request.getParameter("optAssignee"));
            modelNC.setDeadLine(request.getParameter("txtDeadLine"));
            modelNC.setDeadLineTime(request.getParameter("txtDeadLineTime"));
            modelNC.setRepeat(request.getParameter("optRepeat"));
            modelNC.setClosureDate(request.getParameter("txtClosureDate"));
            modelNC.setClosureTime(request.getParameter("txtClosureTime"));
            modelNC.setReviewer(request.getParameter("txtReviewer"));
            modelNC.setReviewDate(request.getParameter("txtReviewDate"));
            modelNC.setReviewTime(request.getParameter("txtReviewTime"));
            //modelNC.setPriority(request.getParameter("optPriority"));

            NCBO boNC = new NCBO();
            int nRetVal = boNC.updateNC(modelNC, beanUserInfo);
            if (nRetVal <= 0) {
                beanNCAdd.setDBError(true);
                createCallLogUpdateForm(request, response);
                beanNCAdd.setDBError(false);
                logger.info("Failed to update");
            }
            else {
                getCallList(request, response);
                
                String strNotify = request.getParameter("hidNotify");
                if ("1".equals(strNotify)) {
                    CommonBO boCommon = new CommonBO();
                    // Notify mail messages to users
                    boCommon.notifyCallChanged(beanUserInfo,
                            request.getParameter("hidAction"),
                            modelOld, modelNC);
                }
            }
        }
    }

    /**
     * deleleCallLog
     * delete Call Log 
     * @author MinhPT
     * @param   request - the request object.
     * @param   response - the response object.
     * @throws  Exception
     */
    private void deleteCallLog(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute(
                "beanUserInfo");

        if (!checkSessionVariable(beanUserInfo)) {
            getLoginForm(request, response);
        }
        else {
            session.setAttribute("beanUserInfo", beanUserInfo);
            NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
            NCModel modelNC = beanNCAdd.getNCModel();
            
            modelNC.setNCID(request.getParameter("hidID"));
            //modelNC.setNCLevel(request.getParameter("optLevel"));
            // Currently CallLog only has Project level
            modelNC.setNCLevel(beanNCAdd.getComboLevel().getCell(0, 0));
            
            NCBO boNC = new NCBO();
            int nRetVal = boNC.deleteNC(modelNC, beanUserInfo.getLocation());
            getCallList(request, response);

            CommonBO boCommon = new CommonBO();
            // Notify mail messages to users
            boCommon.notifyCallChanged(beanUserInfo,
                    request.getParameter("hidAction"),
                    beanNCAdd.getNCModel(), beanNCAdd.getNCModel());
        }
    }
}
