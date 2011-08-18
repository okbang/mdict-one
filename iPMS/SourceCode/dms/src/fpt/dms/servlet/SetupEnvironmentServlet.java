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

import fpt.dms.bean.UserInfoBean;
import fpt.dms.bean.SetupEnvironment.*;
import fpt.dms.bean.login.LoginBean;
import fpt.dms.bo.SetupEnvironment.*;
import fpt.dms.constant.DMS;
import fpt.dms.framework.core.BaseServlet;
import fpt.dms.framework.util.StringUtil.StringMatrix;
import fpt.dms.framework.util.StringUtil.StringVector;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * @Title:        SetupEnvironmentServlet.java
 * @Description:  Locate and forward the requests from the control pages.
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  November 01, 2002
 * @Modified by:
 */

public class SetupEnvironmentServlet	extends BaseServlet
{
	private static Logger logger = Logger.getLogger(SetupEnvironmentServlet.class.getName());

	/**
	 * ProjectEnvironmentServlet constructor.
	 */
	public SetupEnvironmentServlet() 
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
                logger.info(strAction );
            }
            
			//Setup Environment form
			if (DMS.SETUP_ENVIRONMENT.equals(strAction))
			{
				callPage(response, DMS.JSP_SETUP_ENVIRONMENT, request);
			}
			else if (DMS.DEFECT_TYPE_LIST.equals(strAction))
			{
				getDefectTypeList(request, response);
				callPage(response, DMS.JSP_DEFECT_TYPE_LIST, request);
			}
			else if (DMS.GROUP_LIST.equals(strAction))
			{
				getGroupList(request, response);
				callPage(response, DMS.JSP_GROUP_LIST, request);
			}
			else if (DMS.KPA_LIST.equals(strAction))
			{
				getKPAList(request, response);
				callPage(response, DMS.JSP_KPA_LIST, request);
			}
			else if (DMS.PRIORITY_LIST.equals(strAction))
			{
				getPriorityList(request, response);
				callPage(response, DMS.JSP_PRIORITY_LIST, request);
			}
			else if (DMS.PROCESS_LIST.equals(strAction))
			{
				getProcessList(request, response);
				callPage(response, DMS.JSP_PROCESS_LIST, request);
			}
			else if (DMS.PROJECT_STAGE_LIST.equals(strAction))
			{
				getProjectStageList(request, response);
				callPage(response, DMS.JSP_PROJECT_STAGE_LIST, request);
			}
			else if (DMS.QC_ACTIVITY_LIST.equals(strAction))
			{
				getQCActivityList(request, response);
				callPage(response, DMS.JSP_QC_ACTIVITY_LIST, request);
			}
			else if (DMS.SEVERITY_LIST.equals(strAction))
			{
				getSeverityList(request, response);
				callPage(response, DMS.JSP_SEVERITY_LIST, request);
			}
			else if (DMS.STATUS_LIST.equals(strAction))
			{
				getStatusList(request, response);
				callPage(response, DMS.JSP_STATUS_LIST, request);
			}
			else if (DMS.TYPE_OF_WORK_LIST.equals(strAction))
			{
				getTypeOfWorkList(request, response);
				callPage(response, DMS.JSP_TYPE_OF_WORK_LIST, request);
			}
			else if (DMS.WORK_PRODUCT_LIST.equals(strAction))
			{
				getWorkProductList(request, response);
				callPage(response, DMS.JSP_WORK_PRODUCT_LIST, request);
			}
			
			///////////Defect Type form/////////////////////////////////////////////////////////////////////////////
			else if (DMS.DEFECT_TYPE_ADD.equals(strAction))
			{
				addDefectType(request, response);
				callPage(response, DMS.JSP_DEFECT_TYPE_LIST, request);
			}
			else if (DMS.DEFECT_TYPE_DELETE.equals(strAction))
			{
				deleteDefectType(request, response);
				callPage(response, DMS.JSP_DEFECT_TYPE_LIST, request);
			}
			else if (DMS.DEFECT_TYPE_UPDATE.equals(strAction))
			{
				createDefectTypeUpdate(request, response);
				callPage(response, DMS.JSP_DEFECT_TYPE_UPDATE, request);
			}
			else if (DMS.DEFECT_TYPE_SAVE_UPDATE.equals(strAction))
			{
				updateDefectType(request, response);
				callPage(response, DMS.JSP_DEFECT_TYPE_LIST, request);
			}
			
			///////////Group form/////////////////////////////////////////////////////////////////////////////
			else if (DMS.GROUP_ADD.equals(strAction))
			{
				addGroup(request, response);
				callPage(response, DMS.JSP_GROUP_LIST, request);
			}
			else if (DMS.GROUP_DELETE.equals(strAction))
			{
				deleteGroup(request, response);
				callPage(response, DMS.JSP_GROUP_LIST, request);
			}
			else if (DMS.GROUP_UPDATE.equals(strAction))
			{
				createGroupUpdate(request, response);
				callPage(response, DMS.JSP_GROUP_UPDATE, request);
			}
			else if (DMS.GROUP_SAVE_UPDATE.equals(strAction))
			{
				updateGroup(request, response);
				callPage(response, DMS.JSP_GROUP_LIST, request);
			}
			
			///////////KPA form/////////////////////////////////////////////////////////////////////////////
			else if (DMS.KPA_ADD.equals(strAction))
			{
				addKPA(request, response);
				callPage(response, DMS.JSP_KPA_LIST, request);
			}
			else if (DMS.KPA_DELETE.equals(strAction))
			{
				deleteKPA(request, response);
				callPage(response, DMS.JSP_KPA_LIST, request);
			}
			else if (DMS.KPA_UPDATE.equals(strAction))
			{
				createKPAUpdate(request, response);
				callPage(response, DMS.JSP_KPA_UPDATE, request);
			}
			else if (DMS.KPA_SAVE_UPDATE.equals(strAction))
			{
				updateKPA(request, response);
				callPage(response, DMS.JSP_KPA_LIST, request);
			}
			
			///////////Priority form/////////////////////////////////////////////////////////////////////////////
			else if (DMS.PRIORITY_ADD.equals(strAction))
			{
				addPriority(request, response);
				callPage(response, DMS.JSP_PRIORITY_LIST, request);
			}
			else if (DMS.PRIORITY_DELETE.equals(strAction))
			{
				deletePriority(request, response);
				callPage(response, DMS.JSP_PRIORITY_LIST, request);
			}
			else if (DMS.PRIORITY_UPDATE.equals(strAction))
			{
				createPriorityUpdate(request, response);
				callPage(response, DMS.JSP_PRIORITY_UPDATE, request);
			}
			else if (DMS.PRIORITY_SAVE_UPDATE.equals(strAction))
			{
				updatePriority(request, response);
				callPage(response, DMS.JSP_PRIORITY_LIST, request);
			}
			
			///////////Process form/////////////////////////////////////////////////////////////////////////////
			else if (DMS.PROCESS_ADD.equals(strAction))
			{
				addProcess(request, response);
				callPage(response, DMS.JSP_PROCESS_LIST, request);
			}
			else if (DMS.PROCESS_DELETE.equals(strAction))
			{
				deleteProcess(request, response);
				callPage(response, DMS.JSP_PROCESS_LIST, request);
			}
			else if (DMS.PROCESS_UPDATE.equals(strAction))
			{
				createProcessUpdate(request, response);
				callPage(response, DMS.JSP_PROCESS_UPDATE, request);
			}
			else if (DMS.PROCESS_SAVE_UPDATE.equals(strAction))
			{
				updateProcess(request, response);
				callPage(response, DMS.JSP_PROCESS_LIST, request);
			}
			
			///////////ProjectStage form/////////////////////////////////////////////////////////////////////////////
			else if (DMS.PROJECT_STAGE_ADD.equals(strAction))
			{
				addProjectStage(request, response);
				callPage(response, DMS.JSP_PROJECT_STAGE_LIST, request);
			}
			else if (DMS.PROJECT_STAGE_DELETE.equals(strAction))
			{
				deleteProjectStage(request, response);
				callPage(response, DMS.JSP_PROJECT_STAGE_LIST, request);
			}
			else if (DMS.PROJECT_STAGE_UPDATE.equals(strAction))
			{
				createProjectStageUpdate(request, response);
				callPage(response, DMS.JSP_PROJECT_STAGE_UPDATE, request);
			}
			else if (DMS.PROJECT_STAGE_SAVE_UPDATE.equals(strAction))
			{
				updateProjectStage(request, response);
				callPage(response, DMS.JSP_PROJECT_STAGE_LIST, request);
			}
			
			///////////QCActivity form/////////////////////////////////////////////////////////////////////////////
			else if (DMS.QC_ACTIVITY_ADD.equals(strAction))
			{
				addQCActivity(request, response);
				callPage(response, DMS.JSP_QC_ACTIVITY_LIST, request);
			}
			else if (DMS.QC_ACTIVITY_DELETE.equals(strAction))
			{
				deleteQCActivity(request, response);
				callPage(response, DMS.JSP_QC_ACTIVITY_LIST, request);
			}
			else if (DMS.QC_ACTIVITY_UPDATE.equals(strAction))
			{
				createQCActivityUpdate(request, response);
				callPage(response, DMS.JSP_QC_ACTIVITY_UPDATE, request);
			}
			else if (DMS.QC_ACTIVITY_SAVE_UPDATE.equals(strAction))
			{
				updateQCActivity(request, response);
				callPage(response, DMS.JSP_QC_ACTIVITY_LIST, request);
			}
			
			///////////Severity form/////////////////////////////////////////////////////////////////////////////
			else if (DMS.SEVERITY_ADD.equals(strAction))
			{
				addSeverity(request, response);
				callPage(response, DMS.JSP_SEVERITY_LIST, request);
			}
			else if (DMS.SEVERITY_DELETE.equals(strAction))
			{
				deleteSeverity(request, response);
				callPage(response, DMS.JSP_SEVERITY_LIST, request);
			}
			else if (DMS.SEVERITY_UPDATE.equals(strAction))
			{
				createSeverityUpdate(request, response);
				callPage(response, DMS.JSP_SEVERITY_UPDATE, request);
			}
			else if (DMS.SEVERITY_SAVE_UPDATE.equals(strAction))
			{
				updateSeverity(request, response);
				callPage(response, DMS.JSP_SEVERITY_LIST, request);
			}
			
			///////////Status form/////////////////////////////////////////////////////////////////////////////
			else if (DMS.STATUS_ADD.equals(strAction))
			{
				addStatus(request, response);
				callPage(response, DMS.JSP_STATUS_LIST, request);
			}
			else if (DMS.STATUS_DELETE.equals(strAction))
			{
				deleteStatus(request, response);
				callPage(response, DMS.JSP_STATUS_LIST, request);
			}
			else if (DMS.STATUS_UPDATE.equals(strAction))
			{
				createStatusUpdate(request, response);
				callPage(response, DMS.JSP_STATUS_UPDATE, request);
			}
			else if (DMS.STATUS_SAVE_UPDATE.equals(strAction))
			{
				updateStatus(request, response);
				callPage(response, DMS.JSP_STATUS_LIST, request);
			}
			
			///////////TypeOfWork form/////////////////////////////////////////////////////////////////////////////
			else if (DMS.TYPE_OF_WORK_ADD.equals(strAction))
			{
				addTypeOfWork(request, response);
				callPage(response, DMS.JSP_TYPE_OF_WORK_LIST, request);
			}
			else if (DMS.TYPE_OF_WORK_DELETE.equals(strAction))
			{
				deleteTypeOfWork(request, response);
				callPage(response, DMS.JSP_TYPE_OF_WORK_LIST, request);
			}
			else if (DMS.TYPE_OF_WORK_UPDATE.equals(strAction))
			{
				createTypeOfWorkUpdate(request, response);
				callPage(response, DMS.JSP_TYPE_OF_WORK_UPDATE, request);
			}
			else if (DMS.TYPE_OF_WORK_SAVE_UPDATE.equals(strAction))
			{
				updateTypeOfWork(request, response);
				callPage(response, DMS.JSP_TYPE_OF_WORK_LIST, request);
			}
			
			///////////WorkProduct form/////////////////////////////////////////////////////////////////////////////
			else if (DMS.WORK_PRODUCT_ADD.equals(strAction))
			{
				addWorkProduct(request, response);
				callPage(response, DMS.JSP_WORK_PRODUCT_LIST, request);
			}
			else if (DMS.WORK_PRODUCT_DELETE.equals(strAction))
			{
				deleteWorkProduct(request, response);
				callPage(response, DMS.JSP_WORK_PRODUCT_LIST, request);
			}
			else if (DMS.WORK_PRODUCT_UPDATE.equals(strAction))
			{
				createWorkProductUpdate(request, response);
				callPage(response, DMS.JSP_WORK_PRODUCT_UPDATE, request);
			}
			else if (DMS.WORK_PRODUCT_SAVE_UPDATE.equals(strAction))
			{
				updateWorkProduct(request, response);
				callPage(response, DMS.JSP_WORK_PRODUCT_LIST, request);
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
			logger.error("Exception in SetupEnvironmentServlet.performTask().", exception);
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
	private void sendErrorRedirect(HttpServletRequest request,	HttpServletResponse response, String errorPageURL, Throwable e)  throws ServletException, IOException 
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
     * @return boolean Valid session or not
     */
    /* (Already called from DMSServlet)
    private boolean checkSessionVariables (HttpServletRequest request,
                                           HttpServletResponse response)
                                           throws ServletException,
                                                  IOException {
        boolean bResult = true;

        try {
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
                if (DMS.DEBUG) {
                    logger.debug("SetupEnvironmentServlet.checkSessionVariables(): Invalid session.");
                }
                getLoginForm(request);
                callPage(response, DMS.JSP_LOGIN, request);

                bResult = false;
            }
        }
        catch (Exception exception) {
            logger.error("Exception in checkSessionVariables().", exception);
            sendErrorRedirect(request, response, DMS.JSP_ERROR, exception);
            bResult = false;
        }

        return bResult;
    }
    */
    
	/**
	 * Get login form.
	 * @author  Nguyen Thai Son.
	 * @version  24 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getLoginForm(HttpServletRequest request) throws Exception
	{
		//go to BO to get data
//		ComboProject projectList = new ComboProject(true);
//		StringMatrix smProjectList = projectList.getListing();

		//put object (smProjectList) into BEAN
		LoginBean beanLogin = new LoginBean();
//		beanLogin.setProjectList(smProjectList);
		request.setAttribute("beanLogin", beanLogin);
	}
	
	/**
	 * Get DefectType form.
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getDefectTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		DefectTypeListBean beanDefectTypeList = new DefectTypeListBean();
		DefectTypeBO boDefectType = new DefectTypeBO();
		beanDefectTypeList.setDefectTypeList(boDefectType.getDefectTypeList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanDefectTypeList", beanDefectTypeList);
	}
	
	/**
	 * Add a DefectType
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addDefectType(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svNew = new StringVector(2);
		svNew.setCell(0, request.getParameter("txtID"));
		svNew.setCell(1, request.getParameter("txtName"));
		
		DefectTypeListBean beanDefectTypeList = new DefectTypeListBean();
		DefectTypeBO boDefectType = new DefectTypeBO();
		boDefectType.addDefectType(svNew);
		beanDefectTypeList.setDefectTypeList(boDefectType.getDefectTypeList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanDefectTypeList", beanDefectTypeList);
	}
	
	/**
	 * Delete a DefectType
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteDefectType(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		DefectTypeListBean beanDefectTypeList = new DefectTypeListBean();
		DefectTypeBO boDefectType = new DefectTypeBO();
		boDefectType.deleteDefectType(arrSelectedRows);
		beanDefectTypeList.setDefectTypeList(boDefectType.getDefectTypeList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanDefectTypeList", beanDefectTypeList);
	}
	
	/**
	 * Create DefectType update form
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createDefectTypeUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("hidName"));
		smUpdate.addRow(svUpdate);
		
		DefectTypeUpdateBean beanDefectTypeUpdate = new DefectTypeUpdateBean();
		beanDefectTypeUpdate.setDefectTypeList(smUpdate);
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanDefectTypeUpdate", beanDefectTypeUpdate);
	}
	
	/**
	 * Update a DefectType
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateDefectType(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("txtName"));
		
		DefectTypeListBean beanDefectTypeList = new DefectTypeListBean();
		DefectTypeBO boDefectType = new DefectTypeBO();
		boDefectType.updateDefectType(svUpdate);
		beanDefectTypeList.setDefectTypeList(boDefectType.getDefectTypeList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanDefectTypeList", beanDefectTypeList);
	}
	
	/**
	 * Get Group form.
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getGroupList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get Group list
		GroupListBean beanGroupList = new GroupListBean();
		GroupBO boGroup = new GroupBO();
		beanGroupList.setGroupList(boGroup.getGroupList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanGroupList", beanGroupList);
	}
	
	/**
	 * Add a Group
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String strName = request.getParameter("txtName");
		
		GroupListBean beanGroupList = new GroupListBean();
		GroupBO boGroup = new GroupBO();
		boGroup.addGroup(strName);
		beanGroupList.setGroupList(boGroup.getGroupList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanGroupList", beanGroupList);
	}
	
	/**
	 * Delete a Group
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		GroupListBean beanGroupList = new GroupListBean();
		GroupBO boGroup = new GroupBO();
		boGroup.deleteGroup(arrSelectedRows);
		beanGroupList.setGroupList(boGroup.getGroupList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanGroupList", beanGroupList);
	}
	
	/**
	 * Create Group update form
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createGroupUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(1);
		svUpdate.setCell(0, request.getParameter("hidName"));
		smUpdate.addRow(svUpdate);
		
		GroupUpdateBean beanGroupUpdate = new GroupUpdateBean();
		beanGroupUpdate.setGroupList(smUpdate);
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanGroupUpdate", beanGroupUpdate);
	}
	
	/**
	 * Update a Group
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidName"));
		svUpdate.setCell(1, request.getParameter("txtName"));
		
		GroupListBean beanGroupList = new GroupListBean();
		GroupBO boGroup = new GroupBO();
		boGroup.updateGroup(svUpdate);
		beanGroupList.setGroupList(boGroup.getGroupList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanGroupList", beanGroupList);
	}
	
	/**
	 * Get KPA form.
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getKPAList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		KPAListBean beanKPAList = new KPAListBean();
		KPABO boKPA = new KPABO();
		beanKPAList.setKPAList(boKPA.getKPAList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanKPAList", beanKPAList);
	}
	
	/**
	 * Add a KPA
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addKPA(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svNew = new StringVector(2);
		svNew.setCell(0, request.getParameter("txtID"));
		svNew.setCell(1, request.getParameter("txtName"));
		
		KPAListBean beanKPAList = new KPAListBean();
		KPABO boKPA = new KPABO();
		boKPA.addKPA(svNew);
		beanKPAList.setKPAList(boKPA.getKPAList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanKPAList", beanKPAList);
	}
	
	/**
	 * Delete a KPA
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteKPA(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		KPAListBean beanKPAList = new KPAListBean();
		KPABO boKPA = new KPABO();
		boKPA.deleteKPA(arrSelectedRows);
		beanKPAList.setKPAList(boKPA.getKPAList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanKPAList", beanKPAList);
	}
	
	/**
	 * Create KPA update form
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createKPAUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("hidName"));
		smUpdate.addRow(svUpdate);
		
		KPAUpdateBean beanKPAUpdate = new KPAUpdateBean();
		beanKPAUpdate.setKPAList(smUpdate);
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanKPAUpdate", beanKPAUpdate);
	}
	
	/**
	 * Update a KPA
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateKPA(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get KPA list
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("txtName"));
		
		KPAListBean beanKPAList = new KPAListBean();
		KPABO boKPA = new KPABO();
		boKPA.updateKPA(svUpdate);
		beanKPAList.setKPAList(boKPA.getKPAList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanKPAList", beanKPAList);
	}
	
	/**
	 * Get Priority form.
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getPriorityList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		PriorityListBean beanPriorityList = new PriorityListBean();
		PriorityBO boPriority = new PriorityBO();
		beanPriorityList.setPriorityList(boPriority.getPriorityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanPriorityList", beanPriorityList);
	}
	
	/**
	 * Add a Priority
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addPriority(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svNew = new StringVector(2);
		svNew.setCell(0, request.getParameter("txtID"));
		svNew.setCell(1, request.getParameter("txtName"));
		
		PriorityListBean beanPriorityList = new PriorityListBean();
		PriorityBO boPriority = new PriorityBO();
		boPriority.addPriority(svNew);
		beanPriorityList.setPriorityList(boPriority.getPriorityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanPriorityList", beanPriorityList);
	}
	
	/**
	 * Delete a Priority
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deletePriority(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		PriorityListBean beanPriorityList = new PriorityListBean();
		PriorityBO boPriority = new PriorityBO();
		boPriority.deletePriority(arrSelectedRows);
		beanPriorityList.setPriorityList(boPriority.getPriorityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanPriorityList", beanPriorityList);
	}
	
	/**
	 * Create Priority update form
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createPriorityUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("hidName"));
		smUpdate.addRow(svUpdate);
		
		PriorityUpdateBean beanPriorityUpdate = new PriorityUpdateBean();
		beanPriorityUpdate.setPriorityList(smUpdate);
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanPriorityUpdate", beanPriorityUpdate);
	}
	
	/**
	 * Update a Priority
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updatePriority(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get Priority list
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("txtName"));
		
		PriorityListBean beanPriorityList = new PriorityListBean();
		PriorityBO boPriority = new PriorityBO();
		boPriority.updatePriority(svUpdate);
		beanPriorityList.setPriorityList(boPriority.getPriorityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanPriorityList", beanPriorityList);
	}
	
	/**
	 * Get Process form.
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getProcessList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		ProcessListBean beanProcessList = new ProcessListBean();
		ProcessBO boProcess = new ProcessBO();
		beanProcessList.setProcessList(boProcess.getProcessList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanProcessList", beanProcessList);
	}
	
	/**
	 * Add a Process
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addProcess(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svNew = new StringVector(2);
		svNew.setCell(0, request.getParameter("txtID"));
		svNew.setCell(1, request.getParameter("txtName"));
		
		ProcessListBean beanProcessList = new ProcessListBean();
		ProcessBO boProcess = new ProcessBO();
		boProcess.addProcess(svNew);
		beanProcessList.setProcessList(boProcess.getProcessList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanProcessList", beanProcessList);
	}
	
	/**
	 * Delete a Process
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteProcess(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		ProcessListBean beanProcessList = new ProcessListBean();
		ProcessBO boProcess = new ProcessBO();
		boProcess.deleteProcess(arrSelectedRows);
		beanProcessList.setProcessList(boProcess.getProcessList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanProcessList", beanProcessList);
	}
	
	/**
	 * Create Process update form
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createProcessUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("hidName"));
		smUpdate.addRow(svUpdate);
		
		ProcessUpdateBean beanProcessUpdate = new ProcessUpdateBean();
		beanProcessUpdate.setProcessList(smUpdate);
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanProcessUpdate", beanProcessUpdate);
	}
	
	/**
	 * Update a Process
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateProcess(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get Process list
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("txtName"));
		
		ProcessListBean beanProcessList = new ProcessListBean();
		ProcessBO boProcess = new ProcessBO();
		boProcess.updateProcess(svUpdate);
		beanProcessList.setProcessList(boProcess.getProcessList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanProcessList", beanProcessList);
	}
	
	/**
	 * Get ProjectStage form.
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getProjectStageList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		ProjectStageListBean beanProjectStageList = new ProjectStageListBean();
		ProjectStageBO boProjectStage = new ProjectStageBO();
		beanProjectStageList.setProjectStageList(boProjectStage.getProjectStageList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanProjectStageList", beanProjectStageList);
	}
	
	/**
	 * Add a ProjectStage
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addProjectStage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svNew = new StringVector(2);
		svNew.setCell(0, request.getParameter("txtID"));
		svNew.setCell(1, request.getParameter("txtName"));
		
		ProjectStageListBean beanProjectStageList = new ProjectStageListBean();
		ProjectStageBO boProjectStage = new ProjectStageBO();
		boProjectStage.addProjectStage(svNew);
		beanProjectStageList.setProjectStageList(boProjectStage.getProjectStageList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanProjectStageList", beanProjectStageList);
	}
	
	/**
	 * Delete a ProjectStage
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteProjectStage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		ProjectStageListBean beanProjectStageList = new ProjectStageListBean();
		ProjectStageBO boProjectStage = new ProjectStageBO();
		boProjectStage.deleteProjectStage(arrSelectedRows);
		beanProjectStageList.setProjectStageList(boProjectStage.getProjectStageList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanProjectStageList", beanProjectStageList);
	}
	
	/**
	 * Create ProjectStage update form
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createProjectStageUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("hidName"));
		smUpdate.addRow(svUpdate);
		
		ProjectStageUpdateBean beanProjectStageUpdate = new ProjectStageUpdateBean();
		beanProjectStageUpdate.setProjectStageList(smUpdate);
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanProjectStageUpdate", beanProjectStageUpdate);
	}
	
	/**
	 * Update a ProjectStage
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateProjectStage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get ProjectStage list
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("txtName"));
		
		ProjectStageListBean beanProjectStageList = new ProjectStageListBean();
		ProjectStageBO boProjectStage = new ProjectStageBO();
		boProjectStage.updateProjectStage(svUpdate);
		beanProjectStageList.setProjectStageList(boProjectStage.getProjectStageList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanProjectStageList", beanProjectStageList);
	}
	
	/**
	 * Get QCActivity form.
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getQCActivityList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		QCActivityListBean beanQCActivityList = new QCActivityListBean();
		QCActivityBO boQCActivity = new QCActivityBO();
		beanQCActivityList.setQCActivityList(boQCActivity.getQCActivityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanQCActivityList", beanQCActivityList);
	}
	
	/**
	 * Add a QCActivity
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addQCActivity(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svNew = new StringVector(2);
		svNew.setCell(0, request.getParameter("txtID"));
		svNew.setCell(1, request.getParameter("txtName"));
		
		QCActivityListBean beanQCActivityList = new QCActivityListBean();
		QCActivityBO boQCActivity = new QCActivityBO();
		boQCActivity.addQCActivity(svNew);
		beanQCActivityList.setQCActivityList(boQCActivity.getQCActivityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanQCActivityList", beanQCActivityList);
	}
	
	/**
	 * Delete a QCActivity
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteQCActivity(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		QCActivityListBean beanQCActivityList = new QCActivityListBean();
		QCActivityBO boQCActivity = new QCActivityBO();
		boQCActivity.deleteQCActivity(arrSelectedRows);
		beanQCActivityList.setQCActivityList(boQCActivity.getQCActivityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanQCActivityList", beanQCActivityList);
	}
	
	/**
	 * Create QCActivity update form
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createQCActivityUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("hidName"));
		smUpdate.addRow(svUpdate);
		
		QCActivityUpdateBean beanQCActivityUpdate = new QCActivityUpdateBean();
		beanQCActivityUpdate.setQCActivityList(smUpdate);
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanQCActivityUpdate", beanQCActivityUpdate);
	}
	
	/**
	 * Update a QCActivity
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateQCActivity(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get QCActivity list
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("txtName"));
		
		QCActivityListBean beanQCActivityList = new QCActivityListBean();
		QCActivityBO boQCActivity = new QCActivityBO();
		boQCActivity.updateQCActivity(svUpdate);
		beanQCActivityList.setQCActivityList(boQCActivity.getQCActivityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanQCActivityList", beanQCActivityList);
	}
	
	/**
	 * Get Severity form.
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getSeverityList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		SeverityListBean beanSeverityList = new SeverityListBean();
		SeverityBO boSeverity = new SeverityBO();
		beanSeverityList.setSeverityList(boSeverity.getSeverityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanSeverityList", beanSeverityList);
	}
	
	/**
	 * Add a Severity
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addSeverity(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svNew = new StringVector(2);
		svNew.setCell(0, request.getParameter("txtID"));
		svNew.setCell(1, request.getParameter("txtName"));
		
		SeverityListBean beanSeverityList = new SeverityListBean();
		SeverityBO boSeverity = new SeverityBO();
		boSeverity.addSeverity(svNew);
		beanSeverityList.setSeverityList(boSeverity.getSeverityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanSeverityList", beanSeverityList);
	}
	
	/**
	 * Delete a Severity
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteSeverity(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		SeverityListBean beanSeverityList = new SeverityListBean();
		SeverityBO boSeverity = new SeverityBO();
		boSeverity.deleteSeverity(arrSelectedRows);
		beanSeverityList.setSeverityList(boSeverity.getSeverityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanSeverityList", beanSeverityList);
	}
	
	/**
	 * Create Severity update form
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createSeverityUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("hidName"));
		smUpdate.addRow(svUpdate);
		
		SeverityUpdateBean beanSeverityUpdate = new SeverityUpdateBean();
		beanSeverityUpdate.setSeverityList(smUpdate);
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanSeverityUpdate", beanSeverityUpdate);
	}
	
	/**
	 * Update a Severity
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateSeverity(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get Severity list
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("txtName"));
		
		SeverityListBean beanSeverityList = new SeverityListBean();
		SeverityBO boSeverity = new SeverityBO();
		boSeverity.updateSeverity(svUpdate);
		beanSeverityList.setSeverityList(boSeverity.getSeverityList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanSeverityList", beanSeverityList);
	}
	
	/**
	 * Get Status form.
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getStatusList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StatusListBean beanStatusList = new StatusListBean();
		StatusBO boStatus = new StatusBO();
		beanStatusList.setStatusList(boStatus.getStatusList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanStatusList", beanStatusList);
	}
	
	/**
	 * Add a Status
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addStatus(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svNew = new StringVector(2);
		svNew.setCell(0, request.getParameter("txtID"));
		svNew.setCell(1, request.getParameter("txtName"));
		
		StatusListBean beanStatusList = new StatusListBean();
		StatusBO boStatus = new StatusBO();
		boStatus.addStatus(svNew);
		beanStatusList.setStatusList(boStatus.getStatusList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanStatusList", beanStatusList);
	}
	
	/**
	 * Delete a Status
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteStatus(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		StatusListBean beanStatusList = new StatusListBean();
		StatusBO boStatus = new StatusBO();
		boStatus.deleteStatus(arrSelectedRows);
		beanStatusList.setStatusList(boStatus.getStatusList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanStatusList", beanStatusList);
	}
	
	/**
	 * Create Status update form
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createStatusUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("hidName"));
		smUpdate.addRow(svUpdate);
		
		StatusUpdateBean beanStatusUpdate = new StatusUpdateBean();
		beanStatusUpdate.setStatusList(smUpdate);
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanStatusUpdate", beanStatusUpdate);
	}
	
	/**
	 * Update a Status
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateStatus(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get Status list
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("txtName"));
		
		StatusListBean beanStatusList = new StatusListBean();
		StatusBO boStatus = new StatusBO();
		boStatus.updateStatus(svUpdate);
		beanStatusList.setStatusList(boStatus.getStatusList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanStatusList", beanStatusList);
	}
	
	/**
	 * Get TypeOfWork form.
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getTypeOfWorkList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		TypeOfWorkListBean beanTypeOfWorkList = new TypeOfWorkListBean();
		TypeOfWorkBO boTypeOfWork = new TypeOfWorkBO();
		beanTypeOfWorkList.setTypeOfWorkList(boTypeOfWork.getTypeOfWorkList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanTypeOfWorkList", beanTypeOfWorkList);
	}
	
	/**
	 * Add a TypeOfWork
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addTypeOfWork(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svNew = new StringVector(2);
		svNew.setCell(0, request.getParameter("txtID"));
		svNew.setCell(1, request.getParameter("txtName"));
		
		TypeOfWorkListBean beanTypeOfWorkList = new TypeOfWorkListBean();
		TypeOfWorkBO boTypeOfWork = new TypeOfWorkBO();
		boTypeOfWork.addTypeOfWork(svNew);
		beanTypeOfWorkList.setTypeOfWorkList(boTypeOfWork.getTypeOfWorkList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanTypeOfWorkList", beanTypeOfWorkList);
	}
	
	/**
	 * Delete a TypeOfWork
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteTypeOfWork(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		TypeOfWorkListBean beanTypeOfWorkList = new TypeOfWorkListBean();
		TypeOfWorkBO boTypeOfWork = new TypeOfWorkBO();
		boTypeOfWork.deleteTypeOfWork(arrSelectedRows);
		beanTypeOfWorkList.setTypeOfWorkList(boTypeOfWork.getTypeOfWorkList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanTypeOfWorkList", beanTypeOfWorkList);
	}
	
	/**
	 * Create TypeOfWork update form
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createTypeOfWorkUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("hidName"));
		smUpdate.addRow(svUpdate);
		
		TypeOfWorkUpdateBean beanTypeOfWorkUpdate = new TypeOfWorkUpdateBean();
		beanTypeOfWorkUpdate.setTypeOfWorkList(smUpdate);
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanTypeOfWorkUpdate", beanTypeOfWorkUpdate);
	}
	
	/**
	 * Update a TypeOfWork
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateTypeOfWork(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get TypeOfWork list
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("txtName"));
		
		TypeOfWorkListBean beanTypeOfWorkList = new TypeOfWorkListBean();
		TypeOfWorkBO boTypeOfWork = new TypeOfWorkBO();
		boTypeOfWork.updateTypeOfWork(svUpdate);
		beanTypeOfWorkList.setTypeOfWorkList(boTypeOfWork.getTypeOfWorkList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanTypeOfWorkList", beanTypeOfWorkList);
	}
	
	/**
	 * Get WorkProduct form.
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getWorkProductList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		WorkProductListBean beanWorkProductList = new WorkProductListBean();
		WorkProductBO boWorkProduct = new WorkProductBO();
		beanWorkProductList.setWorkProductList(boWorkProduct.getWorkProductList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanWorkProductList", beanWorkProductList);
	}
	
	/**
	 * Add a WorkProduct
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addWorkProduct(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		StringVector svNew = new StringVector(2);
		svNew.setCell(0, request.getParameter("txtID"));
		svNew.setCell(1, request.getParameter("txtName"));
		
		WorkProductListBean beanWorkProductList = new WorkProductListBean();
		WorkProductBO boWorkProduct = new WorkProductBO();
		boWorkProduct.addWorkProduct(svNew);
		beanWorkProductList.setWorkProductList(boWorkProduct.getWorkProductList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanWorkProductList", beanWorkProductList);
	}
	
	/**
	 * Delete a WorkProduct
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteWorkProduct(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get defect type list
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		WorkProductListBean beanWorkProductList = new WorkProductListBean();
		WorkProductBO boWorkProduct = new WorkProductBO();
		boWorkProduct.deleteWorkProduct(arrSelectedRows);
		beanWorkProductList.setWorkProductList(boWorkProduct.getWorkProductList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanWorkProductList", beanWorkProductList);
	}
	
	/**
	 * Create WorkProduct update form
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createWorkProductUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("hidName"));
		smUpdate.addRow(svUpdate);
		
		WorkProductUpdateBean beanWorkProductUpdate = new WorkProductUpdateBean();
		beanWorkProductUpdate.setWorkProductList(smUpdate);
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanWorkProductUpdate", beanWorkProductUpdate);
	}
	
	/**
	 * Update a WorkProduct
	 * @author  Nguyen Thai Son.
	 * @version  02 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateWorkProduct(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);
		
		//STEP 2 - Using BO to get WorkProduct list
		StringVector svUpdate = new StringVector(2);
		svUpdate.setCell(0, request.getParameter("hidID"));
		svUpdate.setCell(1, request.getParameter("txtName"));
		
		WorkProductListBean beanWorkProductList = new WorkProductListBean();
		WorkProductBO boWorkProduct = new WorkProductBO();
		boWorkProduct.updateWorkProduct(svUpdate);
		beanWorkProductList.setWorkProductList(boWorkProduct.getWorkProductList());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanWorkProductList", beanWorkProductList);
	}
}