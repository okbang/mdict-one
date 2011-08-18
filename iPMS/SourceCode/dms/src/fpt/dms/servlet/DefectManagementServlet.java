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
 
 package fpt.dms.servlet;

/**
 * @Title:        DefectManagementServlet.java
 * @Description:  Locate and forward the requests from the control pages.
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  October 23, 2002
 * @Modified by:
 *                - First : Nguyen Thai Son, March 09, 2002
 *                - Second: FullName, Date
 */
//imported from standard JAVA
import fpt.dms.bean.ComboBoxExt;
import fpt.dms.bean.DefectManagement.*;
import fpt.dms.bean.UserInfoBean;
import fpt.dms.bean.login.LoginBean;
import fpt.dms.bo.DefectManagement.*;
import fpt.dms.bo.combobox.*;
import fpt.dms.constant.DMS;
import fpt.dms.framework.core.BaseServlet;
import fpt.dms.framework.util.CommonUtil.CommonUtil;
import fpt.dms.framework.util.CommonUtil.Rsa;
import fpt.dms.framework.util.DateUtil.DateUtil;
import fpt.dms.framework.util.StringUtil.StringMatrix;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DefectManagementServlet extends BaseServlet
{
    private static Logger logger = Logger.getLogger(DefectManagementServlet.class.getName());

    /**
     * DefectManagementServlet constructor.
     */
    public DefectManagementServlet()
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
            String strAction = request.getParameter("hidActionDetail");
            if (DMS.DEBUG) {
                logger.info(strAction);
            }
            
            ////////////////////////////////Query Listing form//////////////////////////////////
            if (DMS.DEFECT_SEARCH.equals(strAction))
            {
                String strFormType = request.getParameter("hidTypeOfView");

                if (DMS.VIEW_ALL_DEFECTS.equals(strFormType) ||
                    DMS.VIEW_ALL_OPEN_DEFECTS.equals(strFormType) ||
                    DMS.VIEW_ALL_LEAKAGE_DEFECTS.equals(strFormType))
                {
                    getAllDefectsForm(request);
                    callPage(response, DMS.JSP_ALL_DEFECT_LISTING, request);
                }
                else
                {
                    getUserDefectsForm(request);
                    callPage(response, DMS.JSP_DEFECT_USER_LISTING, request);
                }
            }
            else if (DMS.QUERY_ADD.equals(strAction))
            {
                //checkSessionVariables(request, response);
                callPage(response, DMS.JSP_QUERY_ADD, request);
            }
            else if (DMS.QUERY_DELETE.equals(strAction))
            {
                deleteQuery(request);
                getQueryListingForm(request);
                callPage(response, DMS.JSP_QUERY_LISTING, request);
            }
            else if (DMS.QUERY_USER_LISTING.equals(strAction))
            {
                getUserDefectsForm(request);
                callPage(response, DMS.JSP_DEFECT_USER_LISTING, request);
            }

            ////////////////////////////////Query Add New form//////////////////////////////////
            else if (DMS.QUERY_SAVENEW.equals(strAction))
            {
                saveNewQuery(request);
                getQueryListingForm(request);
                callPage(response, DMS.JSP_QUERY_LISTING, request);
            }
            else if (DMS.QUERY_LISTING.equals(strAction))
            {
                getQueryListingForm(request);
                callPage(response, DMS.JSP_QUERY_LISTING, request);
            }

            ////////////////////////////////All Defects form//////////////////////////////////
            else if (DMS.DEFECT_ADD.equals(strAction))
            {
                // Remove cache of Defect attachment
                UploadBO.clearTempAttach(request.getSession());
                createAddDefectForm(request);
                request.getSession().removeAttribute("hidSaveNewCounter");
                callPage(response, DMS.JSP_DEFECT_ADD, request);
            }
            else if (DMS.DEFECT_UPDATE.equals(strAction))
            {
                // Remove cache of Defect attachment
                UploadBO.clearTempAttach(request.getSession());
                createUpdateDefectForm(request);
                callPage(response, DMS.JSP_DEFECT_UPDATE, request);
            }
            else if (DMS.DEFECT_BATCH_UPDATE.equals(strAction))
            {
                createBatchUpdateDefectForm(request);
                callPage(response, DMS.JSP_DEFECT_BATCH_UPDATE, request);
            }
            else if (DMS.DEFECT_DELETE.equals(strAction))
            {
                deleteDefect(request, response);
                String strFormType = request.getParameter("hidTypeOfView");

                if (DMS.VIEW_ALL_DEFECTS.equals(strFormType) ||
                    DMS.VIEW_ALL_OPEN_DEFECTS.equals(strFormType) ||
                    DMS.VIEW_ALL_LEAKAGE_DEFECTS.equals(strFormType))
                {
                    getAllDefectsForm(request);
                    callPage(response, DMS.JSP_ALL_DEFECT_LISTING, request);
                }
                else
                {
                    getUserDefectsForm(request);
                    callPage(response, DMS.JSP_DEFECT_USER_LISTING, request);
                }
            }
            else if (DMS.DEFECT_EXPORT.equals(strAction))
            {
                exportDefect(request);
                callPage(response, DMS.JSP_DEFECT_EXPORT, request);
            }
            else if (DMS.DEFECT_MOVE.equals(strAction))
            {
                moveDefect(request);
                getAllDefectsForm(request);
                callPage(response, DMS.JSP_ALL_DEFECT_LISTING, request);
            }

            ////////////////////////////////Add New Defect form//////////////////////////////////
            else if (DMS.DEFECT_SAVE_NEW.equals(strAction))
            {
                saveNewDefect(request);
                createAddDefectForm(request);
                callPage(response, DMS.JSP_DEFECT_ADD, request);
            }

            ////////////////////////////////Update Defect form//////////////////////////////////
            else if (DMS.DEFECT_SAVE_UPDATE.equals(strAction))
            {
                saveUpdateDefect(request);

                String strFormType = request.getParameter("hidTypeOfView");

                if (DMS.VIEW_ALL_DEFECTS.equals(strFormType) ||
                    DMS.VIEW_ALL_OPEN_DEFECTS.equals(strFormType) ||
                    DMS.VIEW_ALL_LEAKAGE_DEFECTS.equals(strFormType))
                {
                    getAllDefectsForm(request);
                    callPage(response, DMS.JSP_ALL_DEFECT_LISTING, request);
                }
                else
                {
                    getUserDefectsForm(request);
                    callPage(response, DMS.JSP_DEFECT_USER_LISTING, request);
                }

            }
            else if (DMS.DEFECT_HISTORY.equals(strAction))
            {
                getDefectHistory(request, response);
                callPage(response, DMS.JSP_DEFECT_HISTORY, request);
            }

            ////////////////////////////////Batch Update Defect form//////////////////////////////////
            else if (DMS.DEFECT_SAVE_BATCH_UPDATE.equals(strAction))
            {
                saveBatchUpdateDefect(request);
                getAllDefectsForm(request);
                callPage(response, DMS.JSP_ALL_DEFECT_LISTING, request);
            }

            else if (DMS.REPORT_WEEKLY.equals(strAction))
            {
                getReportWeekly(request);
                callPage(response, DMS.JSP_REPORT_WEEKLY, request);
            }

            else if (DMS.DEFECT_IMPORT.equals(strAction)) {
                getImportForm(request);
                callPage(response, DMS.JSP_DEFECT_IMPORT, request);
            }

            else if (DMS.DEFECT_ATTACH_NEW_FORM.equals(strAction)) {
                getAttachAddForm(request);
                callPage(response, DMS.JSP_DEFECT_ATTACH, request);
            }
            else if (DMS.DEFECT_ATTACH_UPDATE_FORM.equals(strAction)) {
                getAttachUpdateForm(request);
                callPage(response, DMS.JSP_DEFECT_ATTACH, request);
            }
            else if (DMS.DEFECT_ATTACH_NEW_CANCEL.equals(strAction)) {
                cancelAttachAdd(request);
                callPage(response, DMS.JSP_DEFECT_ADD, request);
            }
            else if (DMS.DEFECT_ATTACH_UPDATE_CANCEL.equals(strAction)) {
                cancelAttachUpdate(request);
                callPage(response, DMS.JSP_DEFECT_UPDATE, request);
            }
            else if (DMS.DEFECT_ATTACH_NEW_DONE.equals(strAction)) {
                attachAddDone(request);
                callPage(response, DMS.JSP_DEFECT_ADD, request);
            }
            else if (DMS.DEFECT_ATTACH_UPDATE_DONE.equals(strAction)) {
                attachUpdateDone(request);
                callPage(response, DMS.JSP_DEFECT_UPDATE, request);
            }
            else if (DMS.DEFECT_ATTACH_FILE_VIEW.equals(strAction)) {
                showAttachFile(request, response);
            }
            else if (DMS.DEFECT_ATTACH_FILE_REMOVE_NEW.equals(strAction)) {
                removeAttachAddFile(request);
                callPage(response, DMS.JSP_DEFECT_ADD, request);
            }
            else if (DMS.DEFECT_ATTACH_FILE_REMOVE_UPDATE.equals(strAction)) {
                removeAttachUpdateFile(request);
                callPage(response, DMS.JSP_DEFECT_UPDATE, request);
            }
            else if (DMS.DEFECT_ATTACH_FILE_REMOVE_UPLOAD.equals(strAction)) {
                removeAttachUploadFile(request);
                callPage(response, DMS.JSP_DEFECT_ATTACH, request);
            }
            else if (DMS.DEFECT_ATTACH_DB_VIEW.equals(strAction)) {
                showAttachDb(request, response);
            }
            else if (DMS.DEFECT_ATTACH_DB_REMOVE_UPDATE.equals(strAction)) {
                removeAttachUpdateDb(request);
                callPage(response, DMS.JSP_DEFECT_UPDATE, request);
            }
            else if (DMS.DEFECT_ATTACH_DB_REMOVE_UPLOAD.equals(strAction)) {
                removeAttachUploadDb(request);
                callPage(response, DMS.JSP_DEFECT_ATTACH, request);
            }

            else
            {
                //sendErrorRedirect(request, response, DMS.JSP_ERROR, new Exception("Invalid action: strActionDetail = " + strAction));
                getLoginForm(request);
                callPage(response, DMS.JSP_LOGIN, request);
            }
        }
        catch (Exception exception)
        {
            logger.error("Exception in DefectManagementServlet.performTask().", exception);
            sendErrorRedirect(request, response, DMS.JSP_ERROR, exception);
        }
    }

    /*
     * Redirect exception to an error page.
     * @author  Nguyen Thai Son.
     * @version  03 April, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   errorPageURL String: the URL to the error page.
     * @param   e Throwable: a throwable storing exception.
     * @exception   IOException    If an input or output exception occurred.
     * @exception   ServletException    If a servlet exception occurred.
    private void sendErrorRedirect(HttpServletRequest request,  HttpServletResponse response, String errorPageURL, Throwable e)  throws ServletException, IOException
    {
        request.setAttribute("javax.servlet.jsp.jspException", e);
        getServletConfig().getServletContext().getRequestDispatcher(errorPageURL).forward(request, response);
    }

    /*
     * Reset session variables
     * @author  Nguyen Thai Son.
     * @version 24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   IOException    If an input or output exception occurred.
     * @exception   ServletException    If a servlet exception occurred.
     */
    /* (Already called from DMSServlet)
    private boolean checkSessionVariables(HttpServletRequest request,
                                          HttpServletResponse response)
                                          throws ServletException, IOException
    {
        boolean bolResult = true;
        try
        {
            HttpSession session = request.getSession();
            UserInfoBean beanUserInfo =
                    (UserInfoBean) session.getAttribute("beanUserInfo");
            String strLoginName = "";
            if (beanUserInfo != null) {
                strLoginName = beanUserInfo.getAccount();
            }
            
            if (beanUserInfo == null || strLoginName == null ||
                strLoginName.length() <= 0)
            {
                logger.debug("DefectManagementServlet.checkSessionVariables(): Invalid session.");
                getLoginForm(request);
                //display result page
                callPage(response, DMS.JSP_LOGIN, request);

                bolResult = false;
            }
        }
        catch (Exception exception)
        {
            logger.error("Exception in checkSessionVariables().", exception);
            sendErrorRedirect(request, response, DMS.JSP_ERROR, exception);
            bolResult = false;
        }
        
        return bolResult;
    }
    */

    /**
     * Get login form.
     * @author  Nguyen Thai Son.
     * @version  24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void getLoginForm(HttpServletRequest request) throws Exception {
        // Remove cache of Defect attachment
        UploadBO.clearTempAttach(request.getSession());
        
        HttpSession session = request.getSession();
        if (session.getAttribute("beanUserInfo") != null) {
            session.invalidate();
            session = request.getSession();
        }
//        Rsa rsa = new Rsa(160);
//        session.setAttribute("rsa", rsa);
        //go to BO to get data
        LoginBean beanLogin = new LoginBean();
        request.setAttribute("beanLogin", beanLogin);
    }

    /**
     * Get query listing form.
     * @author  Nguyen Thai Son.
     * @version  24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void getQueryListingForm(HttpServletRequest request) throws Exception
    {
        UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
        if (beanUserInfo == null)
        {
            getLoginForm(request);
            return;
        }

        //go to BO to get data
        QueryBO query = new QueryBO();
        StringMatrix smQuery = query.getQueryList(beanUserInfo.getProjectID(), beanUserInfo.getAccount());

        //put object (smQuery) into BEAN
        QueryListingBean beanQueryListing = new QueryListingBean();
        beanQueryListing.setQueryList(smQuery);

        request.setAttribute("beanQueryListing", beanQueryListing);
    }

    /**
     * Save a new query
     * @author  Nguyen Thai Son.
     * @version  24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void saveNewQuery(HttpServletRequest request) throws Exception
    {
        QueryAddBean beanQuery = new QueryAddBean();
        beanQuery.setFieldName(request.getParameterValues("FieldName"));
        beanQuery.setNotOpe(request.getParameterValues("Not"));
        beanQuery.setLogical(request.getParameterValues("Logical"));
        beanQuery.setValue(request.getParameterValues("Values"));
//        beanQuery.setValue(
//                CommonUtil.decodeParameterValues(request, "Values", "UTF-8"));
        beanQuery.setGroup(request.getParameterValues("Group"));
        beanQuery.setCriteria(request.getParameterValues("Criteria"));
        beanQuery.setName(request.getParameter("Name"));
//        beanQuery.setName(
//                CommonUtil.decodeParameter(request, "Name", "UTF-8"));
        beanQuery.setScope(request.getParameter("Scope"));

        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");

        QueryBO boQuery = new QueryBO();
        boQuery.addQuery(beanQuery, beanUserInfo);
    }

    /**
     * Delete the selected query.
     * @author  Nguyen Thai Son.
     * @version  24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void deleteQuery(HttpServletRequest request) throws Exception
    {
        QueryListingBean beanQueryList = new QueryListingBean();

        String[] arrPublic = request.getParameterValues("selectIndex1");
        String[] arrPrivate = request.getParameterValues("selectIndex2");
        if(arrPublic != null)
        {
            if(arrPrivate != null)
            {
                int numPub =arrPublic.length;
                String[] result = new String[numPub+numPub];
                for(int i=0;i<arrPublic.length;i++) result[i]=arrPublic[i];
                for(int i=0;i<arrPrivate.length;i++)    result[numPub+i]=arrPrivate[i];

                beanQueryList.setSelectedQueries(result);
            }
            else    beanQueryList.setSelectedQueries(arrPublic);
        }
        else
        {
            if(arrPrivate!=null)
                beanQueryList.setSelectedQueries(arrPrivate);
        }

        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");

        QueryBO boQuery = new QueryBO();
        boQuery.deleteQuery(beanQueryList, beanUserInfo);
    }


    /**
     * Get all defect listing form.
     * @author  Nguyen Thai Son.
     * @version  24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void getAllDefectsForm(HttpServletRequest request) throws Exception
    {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
        if (beanUserInfo == null)
        {
            getLoginForm(request);
            return;
        }

        DefectListingBean beanDefectList = new DefectListingBean();

        String strTemp = "";

        //Type of defect list
        strTemp = request.getParameter("hidTypeOfView");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("hidTypeOfView") == null) ? "" : session.getAttribute("hidTypeOfView"));
        else    session.setAttribute("hidTypeOfView", strTemp);
        beanUserInfo.setTypeOfView(strTemp);
        beanDefectList = getRequestInfo(request);

        /////////////////////////////////////////////////////////////////////////////////////////////
 
        DefectBO boDefect = new DefectBO();
        beanDefectList = boDefect.getDefectList(beanDefectList, beanUserInfo, false);
        ///////////////////////////////////////////////////////////////////////////
        
        ComboBoxExt cboComboCode;
        cboComboCode = new ComboAssignTo(beanUserInfo.getProjectID(), ComboBoxExt.COMBO_ALL_NONE);
        beanDefectList.setComboAssignTo(cboComboCode.getListing());

		cboComboCode = new ComboDefectOwner(beanUserInfo.getProjectID(), ComboBoxExt.COMBO_ALL_NONE);
		beanDefectList.setComboDefectOwner(cboComboCode.getListing());
		
        cboComboCode = new ComboProcess(ComboBoxExt.COMBO_ALL);
        beanDefectList.setComboDefectOrigin(cboComboCode.getListing());

        cboComboCode = new ComboModuleCode(beanUserInfo.getProjectID(), ComboBoxExt.COMBO_ALL_NONE);
        beanDefectList.setComboModuleCode(cboComboCode.getListing());

        cboComboCode = new ComboPriority(ComboBoxExt.COMBO_ALL_NONE);
        beanDefectList.setComboPriority(cboComboCode.getListing());

        cboComboCode = new ComboQCActivity(ComboBoxExt.COMBO_ALL);
        beanDefectList.setComboQCActivity(cboComboCode.getListing());

        cboComboCode = new ComboSeverity(ComboBoxExt.COMBO_ALL);
        beanDefectList.setComboSeverity(cboComboCode.getListing());

        cboComboCode = new ComboProjectStage(ComboBoxExt.COMBO_ALL_NONE);
        beanDefectList.setComboStageDetected(cboComboCode.getListing());

        cboComboCode = new ComboProjectStage(ComboBoxExt.COMBO_ALL_NONE);
        beanDefectList.setComboStageInjected(cboComboCode.getListing());

        cboComboCode = new ComboTypeDefect(ComboBoxExt.COMBO_ALL);
        beanDefectList.setComboType(cboComboCode.getListing());

        cboComboCode = new ComboActivityType(ComboBoxExt.COMBO_ALL);
        beanDefectList.setComboTypeofActivity(cboComboCode.getListing());

        cboComboCode = new ComboWorkProduct(ComboBoxExt.COMBO_ALL);
        beanDefectList.setComboWorkProduct(cboComboCode.getListing());

        cboComboCode = new ComboStatus(ComboBoxExt.COMBO_ALL);
        beanDefectList.setComboDefectStatus(cboComboCode.getListing());

        cboComboCode= new ComboRef(beanUserInfo.getProjectID(), ComboBoxExt.COMBO_ALL_NONE);
        beanDefectList.setComboRef(cboComboCode.getListing());

        //cboComboCode = new ComboProject(false);
        //cboComboCode = new ComboProject(beanUserInfo.getAccount(),beanUserInfo.getRole());

		ComboProject cboAllProject = new ComboProject(beanUserInfo.getAccount(),beanUserInfo.getRole());
        beanDefectList.setProjectList(cboAllProject.getListAllProject());
        
        ///////////////////////////////////////////////////////////////////////////

        request.setAttribute("beanDefectList", beanDefectList);
    }

    /**
     * Get request data
     * @author  Nguyen Thai Son.
     * @version  04 November, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private DefectListingBean getRequestInfo(HttpServletRequest request) throws Exception
    {
        HttpSession session = request.getSession();

        DefectListingBean beanDefectList = new DefectListingBean();
        String strTemp = "";

        /**
         * Modified by: Tu Ngoc Trung
         * Date: 2003-10-27
         * Description: Change parameter names which received from client requests
         *              to avoid same parameter names of Listing, Add New, and Update functions*/
        //Assigned to whom
        strTemp = request.getParameter("ListingAssignto");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingAssignto") == null) ? "" : session.getAttribute("ListingAssignto"));
        else    session.setAttribute("ListingAssignto", strTemp);
        beanDefectList.setAssignTo(strTemp);
		
		// defect owner
		strTemp = request.getParameter("ListingDefectOwner");
		if (strTemp == null) {
			strTemp = (String)((session.getAttribute("ListingDefectOwner") == null) ? "" : session.getAttribute("ListingDefectOwner"));
		} 
        else {
        session.setAttribute("ListingDefectOwner", strTemp);
        }
		beanDefectList.setDefectOwner(strTemp);
		
        strTemp = request.getParameter("ListingCreatedBy");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingCreatedBy") == null) ? "" : session.getAttribute("ListingCreatedBy"));
        else    session.setAttribute("ListingCreatedBy", strTemp);
        beanDefectList.setCreatedBy(strTemp);

        //Defect status
        strTemp = request.getParameter("ListingStatus");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingStatus") == null) ? ComboBoxExt.STR_ALL_VALUE : session.getAttribute("ListingStatus"));
        else    session.setAttribute("ListingStatus", strTemp);
        beanDefectList.setStatus(strTemp);

        //Work product
        strTemp = request.getParameter("ListingWorkProduct");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingWorkProduct") == null) ? ComboBoxExt.STR_ALL_VALUE : session.getAttribute("ListingWorkProduct"));
        else    session.setAttribute("ListingWorkProduct", strTemp);
        beanDefectList.setWorkProduct(strTemp);

        //Module
        strTemp = request.getParameter("ListingModuleCode");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingModuleCode") == null) ? ComboBoxExt.STR_ALL_VALUE : session.getAttribute("ListingModuleCode"));
        else    session.setAttribute("ListingModuleCode", strTemp);
        beanDefectList.setModuleCode(strTemp);

        //Severity
        strTemp = request.getParameter("ListingSeverity");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingSeverity") == null) ? ComboBoxExt.STR_ALL_VALUE : session.getAttribute("ListingSeverity"));
        else    session.setAttribute("ListingSeverity", strTemp);
        beanDefectList.setSeverity(strTemp);

        //Priority
        strTemp = request.getParameter("ListingPriority");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingPriority") == null) ? ComboBoxExt.STR_ALL_VALUE : session.getAttribute("ListingPriority"));
        else    session.setAttribute("ListingPriority", strTemp);
        beanDefectList.setPriority(strTemp);

        //Defected stage
        strTemp = request.getParameter("ListingStageDetected");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingStageDetected") == null) ? ComboBoxExt.STR_ALL_VALUE : session.getAttribute("ListingStageDetected"));
        else    session.setAttribute("ListingStageDetected", strTemp);
        beanDefectList.setStageDefected(strTemp);

        //QC Activity
        strTemp = request.getParameter("ListingQCActivity");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingQCActivity") == null) ? ComboBoxExt.STR_ALL_VALUE : session.getAttribute("ListingQCActivity"));
        else    session.setAttribute("ListingQCActivity", strTemp);
        beanDefectList.setQCActivity(strTemp);

        //Injected stage
        strTemp = request.getParameter("ListingStageInjected");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingStageInjected") == null) ? ComboBoxExt.STR_ALL_VALUE : session.getAttribute("ListingStageInjected"));
        else    session.setAttribute("ListingStageInjected", strTemp);
        beanDefectList.setStageInjected(strTemp);

        //Defect origin
        strTemp = request.getParameter("ListingDefectOrigin");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingDefectOrigin") == null) ? ComboBoxExt.STR_ALL_VALUE : session.getAttribute("ListingDefectOrigin"));
        else    session.setAttribute("ListingDefectOrigin", strTemp);
        beanDefectList.setDefectOrigin(strTemp);

        //Defect type
        strTemp = request.getParameter("ListingType");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("ListingType") == null) ? ComboBoxExt.STR_ALL_VALUE : session.getAttribute("ListingType"));
        else    session.setAttribute("ListingType", strTemp);
        beanDefectList.setType(strTemp);

        /**End*/

        //From which date
        strTemp = request.getParameter("txtFromDate");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("txtFromDate") == null) ? "" : session.getAttribute("txtFromDate"));
        else    session.setAttribute("txtFromDate", strTemp);
        beanDefectList.setFromDate(strTemp);

        //To which date
        strTemp = request.getParameter("txtToDate");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("txtToDate") == null) ? "" : session.getAttribute("txtToDate"));
        else    session.setAttribute("txtToDate", strTemp);
        beanDefectList.setToDate(strTemp);

        //Page index
        strTemp = request.getParameter("numPage");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("numPage") == null) ? "0" : session.getAttribute("numPage"));
        else    session.setAttribute("numPage", strTemp);
        beanDefectList.setNumpage(strTemp);

        //Reference
        strTemp = request.getParameter("Reference");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("Reference") == null) ? ComboBoxExt.STR_ALL_VALUE : session.getAttribute("Reference"));
        else    session.setAttribute("Reference", strTemp);
        beanDefectList.setRef(strTemp);

        //Sort by
        strTemp = request.getParameter("SortBy");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("SortBy") == null) ? "" : session.getAttribute("SortBy"));
        else    session.setAttribute("SortBy", strTemp);
        beanDefectList.setSortBy(strTemp);

        //Direction
        strTemp = request.getParameter("Direction");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("Direction") == null) ? "0" : session.getAttribute("Direction"));
        else    session.setAttribute("Direction", strTemp);
        beanDefectList.setDirection(CommonUtil.StrToInt(strTemp));

        //Fixed from
        strTemp = request.getParameter("txtFixedFrom");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("txtFixedFrom") == null) ? "" : session.getAttribute("txtFixedFrom"));
        else    session.setAttribute("txtFixedFrom", strTemp);
        beanDefectList.setFixedFrom(strTemp);

        //Fixed to
        strTemp = request.getParameter("txtFixedTo");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("txtFixedTo") == null) ? "" : session.getAttribute("txtFixedTo"));
        else    session.setAttribute("txtFixedTo", strTemp);
        beanDefectList.setFixedTo(strTemp);

        //DefectID
        strTemp = request.getParameter("txtDefectID");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("txtDefectID") == null) ? "" : session.getAttribute("txtDefectID"));
        else    session.setAttribute("txtDefectID", strTemp);
        beanDefectList.setDefectID(strTemp);

        //Title
        strTemp = request.getParameter("txtLstTitle");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("txtLstTitle") == null) ? "" : session.getAttribute("txtLstTitle"));
        else    session.setAttribute("txtLstTitle", strTemp);
        beanDefectList.setTitle(strTemp);

		//Test Case
		strTemp = request.getParameter("txtLstTestCase");
		if (strTemp == null)
			strTemp = (String)((session.getAttribute("txtLstTestCase") == null) ? "" : session.getAttribute("txtLstTestCase"));
		else    session.setAttribute("txtLstTestCase", strTemp);
		beanDefectList.setTestCase(strTemp);

        return beanDefectList;
    }

    /**
     * Create add new defect form.
     * @author  Nguyen Thai Son.
     * @version  26 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void createAddDefectForm(HttpServletRequest request) throws Exception
    {
        UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
        if (beanUserInfo == null)
        {
            getLoginForm(request);
            return;
        }
        
        DefectAddBean beanDefectAdd = new DefectAddBean();

        ComboBoxExt cboComboCode;
        cboComboCode = new ComboAssignTo(beanUserInfo.getProjectID(), ComboBoxExt.COMBO_EMPTY);
        beanDefectAdd.setComboAssignTo(cboComboCode.getListing());

        cboComboCode = new ComboProcess(ComboBoxExt.COMBO_NORMAL);
        beanDefectAdd.setComboDefectOrigin(cboComboCode.getListing());

        cboComboCode = new ComboModuleCode(beanUserInfo.getProjectID(), ComboBoxExt.COMBO_EMPTY);
        beanDefectAdd.setComboModuleCode(cboComboCode.getListing());

        cboComboCode = new ComboPriority(ComboBoxExt.COMBO_EMPTY);
        beanDefectAdd.setComboPriority(cboComboCode.getListing());

        cboComboCode = new ComboQCActivity(ComboBoxExt.COMBO_NORMAL);
        beanDefectAdd.setComboQCActivity(cboComboCode.getListing());

        cboComboCode = new ComboSeverity(ComboBoxExt.COMBO_NORMAL);
        beanDefectAdd.setComboSeverity(cboComboCode.getListing());

        cboComboCode = new ComboProjectStage(ComboBoxExt.COMBO_EMPTY);
        beanDefectAdd.setComboStageDetected(cboComboCode.getListing());

        cboComboCode = new ComboProjectStage(ComboBoxExt.COMBO_EMPTY);
        beanDefectAdd.setComboStageInjected(cboComboCode.getListing());

        cboComboCode = new ComboTypeDefect(ComboBoxExt.COMBO_EMPTY);
        beanDefectAdd.setComboType(cboComboCode.getListing());

        cboComboCode = new ComboActivityType(ComboBoxExt.COMBO_NORMAL);
        beanDefectAdd.setComboTypeofActivity(cboComboCode.getListing());

        cboComboCode = new ComboWorkProduct(ComboBoxExt.COMBO_NORMAL);
        beanDefectAdd.setComboWorkProduct(cboComboCode.getListing());

        // Variables initializing for new AddNew view..
        beanDefectAdd.setDefectID("0");
        beanDefectAdd.setStatus(0x01);
        beanDefectAdd.setCreateUser(beanUserInfo.getAccount());
        beanDefectAdd.setCreateDate(DateUtil.getCurrentDate());
        beanDefectAdd.setDueDate(DateUtil.getCurrentDate(1));
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        request.setAttribute("beanDefectAdd", beanDefectAdd);
    }

    /**
     * Create update defect form.
     * @author  Nguyen Thai Son.
     * @version  26 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void createUpdateDefectForm(HttpServletRequest request) throws Exception
    {
        //STEP 1 - get user information from session
        UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
        if (beanUserInfo == null)
        {
            getLoginForm(request);
            return;
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //STEP 2 - get request information from client
        DefectUpdateBean beanDefectUpdate = new DefectUpdateBean();
        String  strCurrentID = "0";
        if (request.getParameter("hidUpdateDefect") != null)
            strCurrentID = request.getParameter("hidUpdateDefect");
        else if (request.getParameter("hidDefectID") != null)
            strCurrentID = request.getParameter("hidDefectID");
        beanDefectUpdate.setDefectID(strCurrentID);

        String strTemp = (request.getParameter("numPage") == null) ? "0" : request.getParameter("numPage");
        beanDefectUpdate.setNumpage(strTemp);
        
        strTemp = (request.getParameter("ProjectID") == null ?
                            "0" :
                            request.getParameter("ProjectID"));
        beanDefectUpdate.setProjectId(strTemp);
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //STEP 3 - Preparing the next/previous defect
        DefectBO boDefect = new DefectBO();

        DefectListingBean beanDefectList = new DefectListingBean();
        beanDefectList = getRequestInfo(request);
        beanDefectList = boDefect.getDefectList(beanDefectList, beanUserInfo, false);
        StringMatrix smDefectList = beanDefectList.getDefectList();
        
        String strDirect = request.getParameter("hidDirect");
        if (DMS.DEFECT_NEXT.equals(strDirect) || DMS.DEFECT_PREV.equals(strDirect))
        {
            int nCurrentRow = 0;
            if (request.getParameter("hidCurrentRow") != null)
                nCurrentRow = Integer.parseInt(request.getParameter("hidCurrentRow"));

            String strNextID = strCurrentID;
            if (DMS.DEFECT_NEXT.equals(strDirect))
            {
                strNextID = smDefectList.getCell(nCurrentRow +1, 0);
                if (beanDefectUpdate.getIsNext())
                {
                    beanDefectUpdate.setSelectedRow(nCurrentRow +1);
                    beanDefectUpdate.setDefectID(strNextID);
                }
            }
            else if (DMS.DEFECT_PREV.equals(strDirect))
            {
                strNextID = smDefectList.getCell(nCurrentRow -1, 0);
                if (beanDefectUpdate.getIsPrev())
                {
                    beanDefectUpdate.setSelectedRow(nCurrentRow - 1);
                    beanDefectUpdate.setDefectID(strNextID);
                }
            }

        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //STEP 4 - Check displaying "Next" and "Prev" buttons
        strCurrentID = beanDefectUpdate.getDefectID();
        for(int i=0; i < smDefectList.getNumberOfRows(); i++)
        {
            if(strCurrentID.equals(smDefectList.getCell(i,0)))
            {
                beanDefectUpdate.setSelectedRow(i);
                beanDefectUpdate.setIsPrev((i == 0) ? false : true);
                beanDefectUpdate.setIsNext((i == smDefectList.getNumberOfRows() -1) ? false : true);

                break;
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //STEP 5 - Using BO to display the selected defect
        beanDefectUpdate = boDefect.getDefectInfo(beanDefectUpdate, beanUserInfo);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //STEP 6 - Get all combo boxes
        ComboBoxExt cboComboCode;
        cboComboCode = new ComboAssignTo(beanUserInfo.getProjectID(), ComboBoxExt.COMBO_EMPTY);
        beanDefectUpdate.setComboAssignTo(cboComboCode.getListing());

        cboComboCode = new ComboProcess(ComboBoxExt.COMBO_NORMAL);
        beanDefectUpdate.setComboDefectOrigin(cboComboCode.getListing());

        cboComboCode = new ComboModuleCode(beanUserInfo.getProjectID(), ComboBoxExt.COMBO_EMPTY);
        beanDefectUpdate.setComboModuleCode(cboComboCode.getListing());

        cboComboCode = new ComboPriority(ComboBoxExt.COMBO_EMPTY);
        beanDefectUpdate.setComboPriority(cboComboCode.getListing());

        cboComboCode = new ComboQCActivity(ComboBoxExt.COMBO_NORMAL);
        beanDefectUpdate.setComboQCActivity(cboComboCode.getListing());

        cboComboCode = new ComboSeverity(ComboBoxExt.COMBO_NORMAL);
        beanDefectUpdate.setComboSeverity(cboComboCode.getListing());

        cboComboCode = new ComboProjectStage(ComboBoxExt.COMBO_EMPTY);
        beanDefectUpdate.setComboStageDetected(cboComboCode.getListing());

        cboComboCode = new ComboProjectStage(ComboBoxExt.COMBO_EMPTY);
        beanDefectUpdate.setComboStageInjected(cboComboCode.getListing());

        cboComboCode = new ComboTypeDefect(ComboBoxExt.COMBO_EMPTY);
        beanDefectUpdate.setComboType(cboComboCode.getListing());

        cboComboCode = new ComboActivityType(ComboBoxExt.COMBO_NORMAL);
        beanDefectUpdate.setComboTypeofActivity(cboComboCode.getListing());

        cboComboCode = new ComboWorkProduct(ComboBoxExt.COMBO_NORMAL);
        beanDefectUpdate.setComboWorkProduct(cboComboCode.getListing());

        cboComboCode = new ComboStatus(ComboBoxExt.COMBO_NORMAL);
        beanDefectUpdate.setComboStatus(cboComboCode.getListing());

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //STEP 7 - Put object to bean for preparing display
        request.setAttribute("beanDefectUpdate", beanDefectUpdate);
        
        // Step 8 - Get attachment references
        UploadBO boUpload = new UploadBO();
        boUpload.getAttachData(request, beanDefectUpdate, this);
    }

    /**
     * Delete the selected defect(s).
     * @author  Nguyen Thai Son.
     * @version  26 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @param   response javax.servlet.HttpServletResponse: the response object
     * @exception   Exception    If an exception occurred.
     */
    private void deleteDefect(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //checkSessionVariables(request, response);
        /* Modified by Tu Ngoc Trung, 2003-Dec-16
         * Description: Delete one defect at once
        DefectListingBean beanDefectList = new DefectListingBean();
        beanDefectList.setArrDeleteDefect(request.getParameterValues("selected"));

        DefectBO boDefect = new DefectBO();
        boDefect.deleteDefect(beanDefectList);
        */
        
        String strDefectID = request.getParameter("hidDefectID");
        DefectBO boDefect = new DefectBO();
        boDefect.deleteDefect(Integer.parseInt(strDefectID));
    }

    /**
     * Create batch update defect form.
     * @author  Nguyen Thai Son.
     * @version  26 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void createBatchUpdateDefectForm(HttpServletRequest request) throws Exception
    {
        UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
        if (beanUserInfo == null)
        {
            getLoginForm(request);
            return;
        }

        //STEP 1 - Get the selected defects from client
        DefectListingBean beanDefectList = new DefectListingBean();
        beanDefectList.setArrDeleteDefect(request.getParameterValues("selected"));

        String[] arrDefectIDList = beanDefectList.getArrDeleteDefect();
        String strDefectIDList = "";
        for (int nDefectID = 0; nDefectID<arrDefectIDList.length-1; nDefectID++)
        {
            strDefectIDList += arrDefectIDList[nDefectID] + ",";
        }
        if (arrDefectIDList.length>0)
            strDefectIDList += arrDefectIDList[arrDefectIDList.length-1];
        /////////////////////////////////////////////////////////////////////

        DefectBatchUpdateBean beanDefectBatchUpdate = new DefectBatchUpdateBean();
        DefectBO boDefect = new DefectBO();
        StringMatrix smDefects = boDefect.getBatchUpdateDefect(strDefectIDList);
        beanDefectBatchUpdate.setBatchUpdateList(smDefects);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ComboBoxExt cboComboCode;
        cboComboCode = new ComboStatus(ComboBoxExt.COMBO_NORMAL);
        beanDefectBatchUpdate.setComboStatus(cboComboCode.getListing());

        cboComboCode = new ComboSeverity(ComboBoxExt.COMBO_NORMAL);
        beanDefectBatchUpdate.setComboSeverity(cboComboCode.getListing());

        cboComboCode = new ComboPriority(ComboBoxExt.COMBO_EMPTY);
        beanDefectBatchUpdate.setComboPriority(cboComboCode.getListing());

        cboComboCode = new ComboAssignTo(beanUserInfo.getProjectID(), ComboBoxExt.COMBO_EMPTY);
        beanDefectBatchUpdate.setComboAssignTo(cboComboCode.getListing());
		
		cboComboCode = new ComboDefectOwner(beanUserInfo.getProjectID(), ComboBoxExt.COMBO_EMPTY);
		beanDefectBatchUpdate.setComboDefectOwner(cboComboCode.getListing());
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        request.setAttribute("beanDefectBatchUpdate", beanDefectBatchUpdate);
    }

    /**
     * Get data posted from add new defect form
     * @param request
     * @return
     * @throws Exception
     */
    private DefectAddBean getAddDefectData(HttpServletRequest request,
                            DefectAddBean beanDefectAdd) throws Exception {
        try {
            beanDefectAdd.setDefectID(request.getParameter("DefectID"));
            beanDefectAdd.setStatus(Integer.parseInt(request.getParameter("hidStatus")));
            beanDefectAdd.setCreateUser(request.getParameter("CreateUser"));
            beanDefectAdd.setCreateDate(request.getParameter("CreateDate"));
            beanDefectAdd.setWorkProduct(Integer.parseInt(request.getParameter("cboWorkProduct")));
            beanDefectAdd.setTitle(request.getParameter("Title").trim());
			beanDefectAdd.setTestCase(request.getParameter("txtTestCase").trim());
//            beanDefectAdd.setTitle(
//                CommonUtil.decodeParameter(request, "Title", "UTF-8").trim());
            beanDefectAdd.setSeverity(Integer.parseInt(request.getParameter("Severity")));
            beanDefectAdd.setModuleCode(Integer.parseInt(
                    request.getParameter("ModuleCode") != null ?
                    request.getParameter("ModuleCode") :
                    "0"));
            beanDefectAdd.setTypeofActivity(Integer.parseInt(
                    request.getParameter("TypeofActivity") != null ?
                    request.getParameter("TypeofActivity") :
                    "0"));
//            beanDefectAdd.setModuleCode(Integer.parseInt(request.getParameter("ModuleCode")));
//            beanDefectAdd.setTypeofActivity(Integer.parseInt(request.getParameter("TypeofActivity")));
            beanDefectAdd.setPriority(Integer.parseInt(request.getParameter("Priority")));
            beanDefectAdd.setType(Integer.parseInt(request.getParameter("Type")));
            beanDefectAdd.setDescription(request.getParameter("Description").trim());
//            beanDefectAdd.setDescription(
//                CommonUtil.decodeParameter(request, "Description", "UTF-8").trim());
            //added by MinhPT
            //for set Defect Status when select AssignTo
            String strAssignTo = request.getParameter("AssignTo");
            if ((strAssignTo != null) && (strAssignTo.length() > 0)) {
                beanDefectAdd.setStatus(2);
            }
            beanDefectAdd.setAssignTo(strAssignTo);
            beanDefectAdd.setDueDate(request.getParameter("DueDate"));
            beanDefectAdd.setStageDetected(Integer.parseInt(request.getParameter("StageDetected")));
            beanDefectAdd.setQCActivity(Integer.parseInt(request.getParameter("QCActivity")));
            beanDefectAdd.setStageInjected(Integer.parseInt(request.getParameter("StageInjected")));
            beanDefectAdd.setDefectOrigin(Integer.parseInt(request.getParameter("DefectOrigin")));
            beanDefectAdd.setCorrectiveAction(request.getParameter("CorrectiveAction").trim());
//            beanDefectAdd.setCorrectiveAction(
//                CommonUtil.decodeParameter(request, "CorrectiveAction", "UTF-8").trim());
			beanDefectAdd.setCauseAnalysis(request.getParameter("txtCauseAnalysis"));
            beanDefectAdd.setStrImage(request.getParameter("Image"));
            beanDefectAdd.setStrProject_Origin(request.getParameter("ProjectOrigin"));
//            beanDefectAdd.setStrProject_Origin(
//                CommonUtil.decodeParameter(request, "ProjectOrigin", "UTF-8").trim());
            beanDefectAdd.setReference(request.getParameter("Ref").trim());
//            beanDefectAdd.setReference(
//                CommonUtil.decodeParameter(request, "Ref", "UTF-8").trim());
			beanDefectAdd.setDefectOwner(request.getParameter("DefectOwner"));
			//System.out.println(request.getParameter("DefectOwner"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return beanDefectAdd;
    }
    
    /**
     * @param beanDefectAdd
     * Get text fields and transform to new texts that not have HTML tag like & " > <
     */
    private synchronized void correctHtmlTag(DefectAddBean beanDefectAdd) {
        if (beanDefectAdd != null) {
            beanDefectAdd.setTitle(
                CommonUtil.correctHTMLError(beanDefectAdd.getTitle()));
            beanDefectAdd.setDescription(
                CommonUtil.correctHTMLError(beanDefectAdd.getDescription()));
            beanDefectAdd.setCorrectiveAction(
                CommonUtil.correctHTMLError(beanDefectAdd.getCorrectiveAction()));
			beanDefectAdd.setCauseAnalysis(
				CommonUtil.correctHTMLError(beanDefectAdd.getCauseAnalysis()));
            beanDefectAdd.setStrProject_Origin(
                CommonUtil.correctHTMLError(beanDefectAdd.getStrProject_Origin()));
            beanDefectAdd.setReference(
                CommonUtil.correctHTMLError(beanDefectAdd.getReference()));
        }
    }
    
    /**
     * Save a new defect
     * @author  Nguyen Thai Son.
     * @version  24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void saveNewDefect(HttpServletRequest request) throws Exception
    {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
        int sesSaveCounter = Integer.parseInt((String) session.getAttribute("hidSaveNewCounter"));
        int reqSaveCounter = Integer.parseInt(request.getParameter("hidSaveNewCounter"));
        if (beanUserInfo == null) {
            getLoginForm(request);
            return;
        }
        else if (sesSaveCounter != reqSaveCounter) {    // User refresh button checking
            if (DMS.DEBUG) {
                logger.debug("req.hidSaveNewCounter=" + request.getParameter("hidSaveNewCounter"));
                logger.debug("ses.hidSaveNewCounter=" + session.getAttribute("hidSaveNewCounter"));
            }
            return;
        }
        DefectAddBean beanDefectAdd = new DefectAddBean();
        beanDefectAdd = getAddDefectData(request, beanDefectAdd);
        /////////////////////////////////////////////////////////////////////
        DefectBO boDefect = new DefectBO();
        boDefect.addDefect(beanDefectAdd, beanUserInfo);

        DefectAttachBean beanDefectAttach = (DefectAttachBean)
                session.getAttribute("beanDefectAttach");
        if (beanDefectAttach != null) {
            if ((beanDefectAttach.getTempAttachList() != null) &&
                (beanDefectAttach.getTempAttachList().size() > 0))
            {
                // Save new attach to database
                UploadBO boUpload = new UploadBO();
                boUpload.saveAttachUpdate(request, beanDefectAdd, beanDefectAttach);
                // Remove temporary attach files
                beanDefectAttach.discardTempAttach();
            }
        }
    }

    /**
     * Get data posted from update defect form
     * @param request
     * @return
     * @throws Exception
     */
    private DefectUpdateBean getUpdateDefectData(HttpServletRequest request,
                                                 DefectUpdateBean beanDefectUpdate)
                                                 throws Exception {
        try {
            beanDefectUpdate.setDefectID(request.getParameter("hidDefectID"));
            beanDefectUpdate.setStatus(Integer.parseInt(request.getParameter("cboStatus")));
            beanDefectUpdate.setCreateUser(request.getParameter("hidCreateUser"));
            beanDefectUpdate.setCreateDate(request.getParameter("txtCreateDate"));
            beanDefectUpdate.setWorkProduct(Integer.parseInt(request.getParameter("cboWorkProduct")));
            beanDefectUpdate.setTitle(request.getParameter("txtTitle").trim());
			beanDefectUpdate.setTestCase(request.getParameter("txtTestCase").trim());
//            beanDefectUpdate.setTitle(
//                CommonUtil.decodeParameter(request, "txtTitle", "UTF-8").trim());
            beanDefectUpdate.setSeverity(Integer.parseInt(request.getParameter("cboSeverity")));
            beanDefectUpdate.setModuleCode(Integer.parseInt(
                    request.getParameter("cboModuleCode") != null ?
                    request.getParameter("cboModuleCode") :
                    "0"));
            beanDefectUpdate.setTypeofActivity(Integer.parseInt(
                    request.getParameter("cboTypeofActivity") != null ?
                    request.getParameter("cboTypeofActivity") :
                    "0"));
            beanDefectUpdate.setPriority(Integer.parseInt(request.getParameter("cboPriority")));
            beanDefectUpdate.setType(Integer.parseInt(request.getParameter("cboType")));
            beanDefectUpdate.setDescription(request.getParameter("hidDescription").trim());
//            beanDefectUpdate.setDescription(
//                CommonUtil.decodeParameter(request, "hidDescription", "UTF-8").trim());
            beanDefectUpdate.setAssignTo(request.getParameter("cboAssignTo"));
            beanDefectUpdate.setDueDate(request.getParameter("txtDueDate"));
            beanDefectUpdate.setStageDetected(Integer.parseInt(request.getParameter("cboStageDetected")));
            beanDefectUpdate.setQCActivity(Integer.parseInt(request.getParameter("cboQCActivity")));
            beanDefectUpdate.setStageInjected(Integer.parseInt(request.getParameter("cboStageInjected")));
            beanDefectUpdate.setDefectOrigin(Integer.parseInt(request.getParameter("cboDefectOrigin")));
            beanDefectUpdate.setCorrectiveAction(request.getParameter("hidCorrectiveAction").trim());
//            beanDefectUpdate.setCorrectiveAction(
//                CommonUtil.decodeParameter(request, "hidCorrectiveAction", "UTF-8").trim());
			beanDefectUpdate.setCauseAnalysis(request.getParameter("hidCauseAnalysis").trim());
            beanDefectUpdate.setStrImage(request.getParameter("Image"));
            beanDefectUpdate.setStrProject_Origin(request.getParameter("txtProjectOrigin").trim());
//            beanDefectUpdate.setStrProject_Origin(
//                CommonUtil.decodeParameter(request, "txtProjectOrigin", "UTF-8").trim());
            beanDefectUpdate.setReference(request.getParameter("txtRef").trim());
//            beanDefectUpdate.setReference(
//                CommonUtil.decodeParameter(request, "txtRef", "UTF-8").trim());
            beanDefectUpdate.setDir(request.getParameter("hidDirect"));
            beanDefectUpdate.setFixedDate(request.getParameter("txtFixedDate"));
            beanDefectUpdate.setDefectOwner(request.getParameter("cboDefectOwner"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return beanDefectUpdate;
    }
    /**
     * Save a current defect
     * @author  Nguyen Thai Son.
     * @version  28 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void saveUpdateDefect(HttpServletRequest request) throws Exception {
        UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
        if (beanUserInfo == null)
        {
            getLoginForm(request);
            return;
        }
        DefectUpdateBean beanDefectUpdate = new DefectUpdateBean();
        getUpdateDefectData(request, beanDefectUpdate);
        
//        String strRemove = request.getParameter("chkRemove");
//        String strOldFile = request.getParameter("attachedPicture");
//        if ((strRemove != null) && ("on".equals(strRemove)) &&
//            (strOldFile != null))
//        {
//            CommonUtil.deleteFile(this.getServletContext().getRealPath("/upload/") +
//                              strOldFile);
//            // File is not replaces by new file => remove this image link
//            if (strOldFile.equals(beanDefectUpdate.getStrImage())) {
//                beanDefectUpdate.setStrImage("");
//            }
//        }
        /////////////////////////////////////////////////////////////////////
        DefectBO boDefect = new DefectBO();
        boDefect.updateDefect(beanDefectUpdate, beanUserInfo);
        
        DefectAttachBean beanDefectAttach = (DefectAttachBean)
                request.getSession().getAttribute("beanDefectAttach");
        if (beanDefectAttach != null) {
            if ((beanDefectAttach.getTempAttachList() != null) &&
                (beanDefectAttach.getTempAttachList().size() > 0))
            {
                // Save new attach to database
                UploadBO boUpload = new UploadBO();
                boUpload.saveAttachUpdate(request, beanDefectUpdate, beanDefectAttach);
                // Remove temporary attach files
                beanDefectAttach.discardTempAttach();
            }
        }
    }

    /**
     * Save a batch of defects.
     * @author  Nguyen Thai Son.
     * @version  28 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void saveBatchUpdateDefect(HttpServletRequest request) throws Exception
    {
        UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
        if (beanUserInfo == null) {
            getLoginForm(request);
            return;
        }
        DefectBatchUpdateBean beanDefectBatch = new DefectBatchUpdateBean();

        int nNumberOfRows = Integer.parseInt(request.getParameter("hidNumberOfRows"));
        StringMatrix smxBatchUpdateList = new StringMatrix(nNumberOfRows, 8);

        String[] arrDefectID = request.getParameterValues("hidDefectID");
        String[] arrSeverity = request.getParameterValues("cboSeverity");
        String[] arrPriority = request.getParameterValues("cboPriority");
        String[] arrDefectOwner = request.getParameterValues("cboDefectOwner");
        String[] arrAssignTo = request.getParameterValues("cboAssignTo");      
        String[] arrDueDate = request.getParameterValues("txtDueDate");
        String[] arrFixedDate = request.getParameterValues("txtFixedDate");
		String[] arrStatus = request.getParameterValues("newstatus");
        
        for (int i=0; i<nNumberOfRows; i++) 
        {
            smxBatchUpdateList.setCell(i, 0, arrDefectID[i]);
            smxBatchUpdateList.setCell(i, 1, arrSeverity[i]);
            smxBatchUpdateList.setCell(i, 2, arrPriority[i]);
            smxBatchUpdateList.setCell(i, 3, arrAssignTo[i]);
            smxBatchUpdateList.setCell(i, 4, arrDueDate[i]);
            smxBatchUpdateList.setCell(i, 5, arrFixedDate[i]);
			smxBatchUpdateList.setCell(i, 6, arrDefectOwner[i]);
            smxBatchUpdateList.setCell(i, 7, arrStatus[i]);
        }
        
        beanDefectBatch.setBatchUpdateList(smxBatchUpdateList);
        /////////////////////////////////////////////////////////////////////
        DefectBO boDefect = new DefectBO();
        boDefect.batchUpdateDefect(beanDefectBatch, beanUserInfo);
    }

    /**
     * View defect history.
     * @author  Nguyen Thai Son.
     * @version  28 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @param   response javax.servlet.HttpServletResponse: the response object
     * @exception   Exception    If an exception occurred.
     */
    private void getDefectHistory(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //STEP 1 - Check if session variables existed or not
        //checkSessionVariables(request, response);

        //STEP 2 - Get client properties
        int nDefectID = 0;
        if (request.getParameter("hidDefectID") != null)
            nDefectID = Integer.parseInt(request.getParameter("hidDefectID"));

        //STEP 3 - Using BO to query defect history
        DefectHistoryBean beanDefectHistory = new DefectHistoryBean();
        beanDefectHistory.setDefectID(nDefectID);

        DefectBO boDefect = new DefectBO();
        String strHistory = boDefect.getDefectHistory(nDefectID);
        beanDefectHistory.setHistory(strHistory);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //STEP 4 - Put object into BEAN for displaying
        request.setAttribute("beanDefectHistory", beanDefectHistory);
    }

    /**
     * Export all defects.
     * @author  Nguyen Thai Son.
     * @version  28 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void exportDefect(HttpServletRequest request) throws Exception
    {
        UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
        if (beanUserInfo == null)
        {
            getLoginForm(request);
            return;
        }

        //DefectListingBean beanDefectList = new DefectListingBean();
        DefectListingBean beanDefectList = getRequestInfo(request);
        beanDefectList.setExportAll(("true".equals(request.getParameter("hidExportAll"))) ? true : false);
//Remove this solution
//        beanDefectList.setExportMode(request.getParameter("chkExportMode"));

        DefectBO boDefect = new DefectBO();
        beanDefectList = boDefect.getDefectList(beanDefectList, beanUserInfo, true);

        ///////////////////////////////////////////////////////////////////////////
        request.setAttribute("beanDefectList", beanDefectList);
    }

    /**
     * Get weekly report for a project
     * @author  Nguyen Thai Son.
     * @version  28 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void getReportWeekly(HttpServletRequest request) throws Exception
    {
        UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null) {
			getLoginForm(request);
			return;
		}

        int nProjectID = beanUserInfo.getProjectID();
        //System.out.println("MinhPT"+nProjectID);

        ReportWeeklyBean beanReport = new ReportWeeklyBean();
        beanReport.setProjectID(nProjectID);

        //StringMatrix smResult = new StringMatrix();

		if (request.getParameter("cboTypeReport") != null) {
			String strReport = request.getParameter("cboTypeReport");
			String strFromDate = request.getParameter("txtFromDate");
			String strToDate = request.getParameter("txtToDate");

			int nSubReport = 0;
			if (request.getParameter("hidActualDetail") != null)
				nSubReport =
					Integer.parseInt(request.getParameter("hidActualDetail"));
			beanReport.setSubReportType(nSubReport);

			//STEP 2 - Switch report
			int nReport = Integer.parseInt(strReport);
			switch (nReport) {
				case 0 : //General Report
					{
						ReportGeneralBO boGeneral = new ReportGeneralBO();
						beanReport =
							boGeneral.getGeneralReport(
								nProjectID,
								strFromDate,
								strToDate,
								nReport);
						break;
					}
				case 1 : //Defect Tracking
					{
						ReportTrackingBO boTracking = new ReportTrackingBO();
						beanReport =
							boTracking.getTrackingReport(
								nProjectID,
								strFromDate,
								strToDate,
								nReport);
						break;
					}
				case 2 : //Defect Leakage
					{
						ReportLeakageBO boLeakage = new ReportLeakageBO();
						beanReport =
							boLeakage.getLeakageReport(
								nProjectID,
								strFromDate,
								strToDate,
								nReport);
						break;
					}
				case 3 : //Defect Type
					{
						ReportDefectTypeBO boDefectType =
							new ReportDefectTypeBO();
						beanReport =
							boDefectType.getDefectTypeReport(
								nProjectID,
								strFromDate,
								strToDate,
								nReport);
						break;
					}
				case 4 : //Defect Distribution
					{
						ReportDistributionBO boDistribution =
							new ReportDistributionBO();
						beanReport =
							boDistribution.getDistributionReport(
								nProjectID,
								strFromDate,
								strToDate,
								nReport);
						break;
					}
				case 5 : //Defect Summary
					{
						ReportSummaryBO boSummary = new ReportSummaryBO();
						beanReport =
							boSummary.getSummaryReport(
								nProjectID,
								strFromDate,
								strToDate,
								nReport,
								nSubReport);
						break;
					}
				case 6 : // Defect by TestCase Id
					{
						ReportDefectByTestCaseIdBO boDefectByTestCaseId =
							new ReportDefectByTestCaseIdBO();
						beanReport =
							boDefectByTestCaseId.getDefectTestCaseIdBOReport(
								nProjectID,
								strFromDate,
								strToDate,
								nReport);
						break;
					}
				default :
					{
						ReportGeneralBO boGeneral = new ReportGeneralBO();
						beanReport =
							boGeneral.getGeneralReport(
								nProjectID,
								strFromDate,
								strToDate,
								nReport);
					}
			} //end switch
		}
        else
        {
            ReportGeneralBO boGeneral = new ReportGeneralBO();
            String strBeginDate = DateUtil.getDateBeginOfWeek();
            String strCurrentDate = DateUtil.getCurrentDate();
            beanReport = boGeneral.getGeneralReport(nProjectID, strBeginDate, strCurrentDate, 0);
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////
        request.setAttribute("beanReport", beanReport);
    }

    /**
     * Get defects defined by a user query
     * @author  Nguyen Thai Son.
     * @version  05 November, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void getUserDefectsForm(HttpServletRequest request) throws Exception
    {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
        if (beanUserInfo == null)
        {
            getLoginForm(request);
            return;
        }

        DefectListingBean beanDefectUserList = new DefectListingBean();
        //Type of view
        String strTemp = request.getParameter("hidTypeOfView");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("hidTypeOfView") == null) ? "" : session.getAttribute("hidTypeOfView"));
        else    session.setAttribute("hidTypeOfView", strTemp);
        beanUserInfo.setTypeOfView(strTemp);

        //logger.debug("strTemp = " + strTemp);

        //Page index
        strTemp = request.getParameter("numPage");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("numPage") == null) ? "0" : session.getAttribute("numPage"));
        else    session.setAttribute("numPage", strTemp);
        beanDefectUserList.setNumpage(strTemp);

        //Sort by
        strTemp = request.getParameter("SortBy");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("SortBy") == null) ? "" : session.getAttribute("SortBy"));
        else    session.setAttribute("SortBy", strTemp);
        beanDefectUserList.setSortBy(strTemp);

        //Direction
        strTemp = request.getParameter("Direction");
        if (strTemp == null)
            strTemp = (String)((session.getAttribute("Direction") == null) ? "0" : session.getAttribute("Direction"));
        else    session.setAttribute("Direction", strTemp);
        beanDefectUserList.setDirection(CommonUtil.StrToInt(strTemp));

        DefectBO boDefect = new DefectBO();
        beanDefectUserList = boDefect.getDefectList(beanDefectUserList, beanUserInfo, false);

        /////////////////////////////////////////////////////////////////
        request.setAttribute("beanDefectUserList", beanDefectUserList);
    }

    /**
     * Move defect(s) to another project
     * @author  Nguyen Thai Son.
     * @version  05 November, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private void moveDefect(HttpServletRequest request) throws Exception
    {
        DefectListingBean beanDefectList = new DefectListingBean();

        beanDefectList.setArrDeleteDefect(request.getParameterValues("selected"));
        beanDefectList.setNewProjectID(request.getParameter("cboProject"));

        DefectBO boDefect = new DefectBO();
        boDefect.moveDefect(beanDefectList);
    }

    /*
     * Method importDefect.
     * @param request
     * @throws Exception
     */
    /*
    private void importDefect(HttpServletRequest request) throws Exception {
        try {
//            HttpSession session = request.getSession();
//            UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");

            DefectBO boDefect = new DefectBO();
            String strExcelFile = request.getParameter("hidExcelFile");
            String strRealPath = request.getRealPath("\\upload");
            String strUserID = request.getParameter("hidUser");
            String strProjectID = request.getParameter("hidProject");
            System.out.println(strUserID);

            boDefect.importFromExcel(strUserID, strProjectID, strRealPath + "\\" + strExcelFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    /**
     * Get import form.
     * @since 14 December, 2003.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    public void getImportForm(HttpServletRequest request) throws Exception
    {
        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
        if (beanUserInfo == null)
        {
            getLoginForm(request);
            return;
        }
        //Reset information
        DefectListingBean beanDefectList = new DefectListingBean();
        request.setAttribute("beanDefectList", beanDefectList);
        
        int nProjectID = beanUserInfo.getProjectID();
        ComboAssignTo comboCreatedBy;
        comboCreatedBy = new ComboAssignTo(nProjectID, ComboAssignTo.COMBO_NORMAL);
        session.setAttribute("comboCreatedBy", comboCreatedBy);
        
        String strCreatedBy = request.getParameter("cboCreatedBy");
        if (strCreatedBy != null) {
            session.setAttribute("diCreatedBy", strCreatedBy);
        }
        
        /*
        ComboProject beanComboProject = new ComboProject(beanUserInfo.getAccount(),
                                                         beanUserInfo.getRole());
        session.setAttribute("beanComboProject", beanComboProject);
        */
    }
    
    /**
     * Request file attach form from Update defect
     * @param request
     * @throws Exception
     */
    private void getAttachAddForm(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        // Remember draft defect data
        createAddDefectForm(request);
        DefectAddBean beanDefectAdd =
                (DefectAddBean) request.getAttribute("beanDefectAdd");
        getAddDefectData(request, beanDefectAdd);
        correctHtmlTag(beanDefectAdd);
        session.setAttribute("beanDefectAdd", beanDefectAdd);
        //~ Remember draft defect data

        if (session.getAttribute("beanDefectAttach") == null) {
            String strTempDir =
                this.getServletContext().getRealPath(DMS.DIRECTORY_UPLOAD_TEMP);
            DefectAttachBean beanDefectAttach = new DefectAttachBean(strTempDir);
            beanDefectAttach.setModeAdd();
            session.setAttribute("beanDefectAttach", beanDefectAttach);
        }
    }
    /**
     * Request file attach form from Update defect
     * @param request
     * @throws Exception
     */
    private void getAttachUpdateForm(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        // Remember draft defect data
        createUpdateDefectForm(request);
        DefectUpdateBean beanDefectUpdate =
                (DefectUpdateBean) request.getAttribute("beanDefectUpdate");
        getUpdateDefectData(request, beanDefectUpdate);
        correctHtmlTag(beanDefectUpdate);
        session.setAttribute("beanDefectUpdate", beanDefectUpdate);
        //~ Remember draft defect data

        if (session.getAttribute("beanDefectAttach") == null) {
            String strTempDir =
                this.getServletContext().getRealPath(DMS.DIRECTORY_UPLOAD_TEMP);
            DefectAttachBean beanDefectAttach = new DefectAttachBean(strTempDir);
            beanDefectAttach.setModeUpdate();
            session.setAttribute("beanDefectAttach", beanDefectAttach);
        }
    }
    
    /**
     * Cancel attach and return update form
     * @param request
     * @throws Exception
     */
    private void cancelAttachAdd(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        DefectAddBean beanDefectAdd =
                (DefectAddBean) session.getAttribute("beanDefectAdd");
        request.setAttribute("beanDefectAdd", beanDefectAdd);
    }
    
    /**
     * Cancel attach and return update form
     * @param request
     * @throws Exception
     */
    private void cancelAttachUpdate(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        DefectUpdateBean beanDefectUpdate =
                (DefectUpdateBean) session.getAttribute("beanDefectUpdate");
        request.setAttribute("beanDefectUpdate", beanDefectUpdate);
    }
    
    /**
     * Attach files successful and return update form
     * @param request
     * @throws Exception
     */
    private void attachAddDone(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        DefectAddBean beanDefectAdd =
                (DefectAddBean) session.getAttribute("beanDefectAdd");
        request.setAttribute("beanDefectAdd", beanDefectAdd);
    }
    
    /**
     * Attach files successful and return update form
     * @param request
     * @throws Exception
     */
    private void attachUpdateDone(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        DefectUpdateBean beanDefectUpdate =
                (DefectUpdateBean) session.getAttribute("beanDefectUpdate");
        request.setAttribute("beanDefectUpdate", beanDefectUpdate);
    }
    
    /**
     * Show download dialog for temporary attach file
     * @param request
     * @param response
     * @throws Exception
     */
    private void showAttachFile(HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        UploadBO.showAttachFile(request, response);
    }
    
    /**
     * Remove temporary attach file
     * @param request
     * @throws Exception
     */
    private void removeAttachAddFile(HttpServletRequest request)
                                throws Exception {
        // Remember draft defect data
        createAddDefectForm(request);
        DefectAddBean beanDefectAdd =
                (DefectAddBean) request.getAttribute("beanDefectAdd");
        getAddDefectData(request, beanDefectAdd);
        correctHtmlTag(beanDefectAdd);
        //~ Remove file
        UploadBO.removeAttachFile(request);
    }

    /**
     * Remove temporary attach file (from update screen)
     * @param request
     * @throws Exception
     */
    private void removeAttachUpdateFile(HttpServletRequest request)
                                throws Exception {
        // Remember draft defect data
        createUpdateDefectForm(request);
        DefectUpdateBean beanDefectUpdate =
                (DefectUpdateBean) request.getAttribute("beanDefectUpdate");
        getUpdateDefectData(request, beanDefectUpdate);
        correctHtmlTag(beanDefectUpdate);
        //~ Remove file
        UploadBO.removeAttachFile(request);
    }
    
    /**
     * Remove temporary attach file (from upload screen)
     * @param request
     * @throws Exception
     */
    private void removeAttachUploadFile(HttpServletRequest request)
                                throws Exception {
        // Remove file
        UploadBO.removeAttachFile(request);
    }
    
    /**
     * Show download dialog for permanent attach file (Blob)
     * @param request
     * @param response
     * @throws Exception
     */
    private void showAttachDb(HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        UploadBO boUpload = new UploadBO();
        boUpload.showAttachDb(request, response);
    }

    /**
     * Show download dialog for permanent attach file (Blob) (from update screen)
     * @param request
     * @param response
     * @throws Exception
     */
    private void removeAttachUpdateDb(HttpServletRequest request) throws Exception {
        // Remember draft defect data
        createUpdateDefectForm(request);
        DefectUpdateBean beanDefectUpdate =
                (DefectUpdateBean) request.getAttribute("beanDefectUpdate");
        getUpdateDefectData(request, beanDefectUpdate);
        correctHtmlTag(beanDefectUpdate);
        // Remove file from Db
        UploadBO.removeAttachData(request);
    }

    /**
     * Show download dialog for permanent attach file (Blob) (from upload screen)
     * @param request
     * @param response
     * @throws Exception
     */
    private void removeAttachUploadDb(HttpServletRequest request) throws Exception {
        // Remove file from Db
        UploadBO.removeAttachData(request);
    }
}