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
 * @Title:        ProjectEnvironmentServlet.java
 * @Description:  Locate and forward the requests from the control pages.
 * @Copyright:    Copyright (c) 2002
 * @Company:      FPT - FSOFT
 * @Author:       Nguyen Thai Son
 * @Create date:  October 23, 2002
 * @Modified by:
 *                - First : Nguyen Thai Son, March 09, 2002
 *                - Second: FullName, Date
 */

import fpt.dms.bean.ComboBoxExt;
import fpt.dms.bean.ProjectEnvironment.*;
import fpt.dms.bean.UserInfoBean;
import fpt.dms.bean.login.LoginBean;
import fpt.dms.bo.ProjectEnvironment.AssignBO;
import fpt.dms.bo.ProjectEnvironment.DefectPlanBO;
import fpt.dms.bo.ProjectEnvironment.ModuleBO;
import fpt.dms.bo.ProjectEnvironment.WorkProductBO;
import fpt.dms.bo.combobox.*;
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

public class ProjectEnvironmentServlet extends BaseServlet 
{
	private static Logger logger = Logger.getLogger(ProjectEnvironmentServlet.class.getName());

	/**
	 * ProjectEnvironmentServlet constructor.
	 */
	public ProjectEnvironmentServlet() 
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
            
			////////////////////////////////Assign User Listing form//////////////////////////////////
			if (DMS.ASSIGN_LIST.equals(strAction))
			{
				//getAssignUserList(request);
				//callPage(response, DMS.JSP_ASSIGN_USER_LISTING, request);
                callPage(response, DMS.JSP_REMOVETOFI, request);		
			}
			else if (DMS.ASSIGN_ADD.equals(strAction))
			{
				/*
                addAssignedUser(request);
				callPage(response, DMS.JSP_ASSIGN_USER_LISTING, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);		
			}
			else if (DMS.ASSIGN_DELETE.equals(strAction))
			{	
				/*
                deleteAssignedUser(request);
				callPage(response, DMS.JSP_ASSIGN_USER_LISTING, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);		
			}
			else if (DMS.ASSIGN_UPDATE.equals(strAction))
			{	
				/*
                createUpdateAssignForm(request);
				callPage(response, DMS.JSP_ASSIGN_USER_UPDATE, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);		
			}
			
			////////////////////////////////Assign User Update form//////////////////////////////////
			else if (DMS.ASSIGN_SAVE_UPDATE.equals(strAction))
			{
				/*
                updateAssignedUser(request);
				callPage(response, DMS.JSP_ASSIGN_USER_LISTING, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);		
			}
			
			////////////////////////////////Work Product Listing form//////////////////////////////////
			else if (DMS.WP_LIST.equals(strAction))
			{
				/*
                getWorkProductList(request);
				callPage(response, DMS.JSP_WP_LISTING, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			else if (DMS.WP_ADD.equals(strAction))
			{
				/*
                addWorkProduct(request);
				callPage(response, DMS.JSP_WP_LISTING, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			else if (DMS.WP_UPDATE.equals(strAction))
			{
				/*
                createUpdateWorkProductForm(request, response);
				callPage(response, DMS.JSP_WP_UPDATE, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			else if (DMS.WP_DELETE.equals(strAction))
			{
				/*
                deleteWorkProduct(request);
				callPage(response, DMS.JSP_WP_LISTING, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			
			////////////////////////////////Work Product Update form//////////////////////////////////
			else if (DMS.WP_SAVE_UPDATE.equals(strAction))
			{
				/*
                String strResult = updateWorkProduct(request);
				if ("".equals(strResult))
					callPage(response, DMS.JSP_WP_LISTING, request);
				else if (strResult != null)
					callPage(response, DMS.JSP_WP_UPDATE, request);
				else
				{
					getLoginForm(request);
					callPage(response, DMS.JSP_LOGIN, request);
				}
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			
			////////////////////////////////Module Listing form//////////////////////////////////
			else if (DMS.MODULE_LIST.equals(strAction))
			{
				/*
                getModuleList(request);
				callPage(response, DMS.JSP_MODULE_LISTING, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			else if (DMS.MODULE_ADD.equals(strAction))
			{
				/*
                addModule(request);
				callPage(response, DMS.JSP_MODULE_LISTING, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			else if (DMS.MODULE_UPDATE.equals(strAction))
			{
				/*
                createUpdateModuleForm(request);
				callPage(response, DMS.JSP_MODULE_UPDATE, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			else if (DMS.MODULE_DELETE.equals(strAction))
			{
				/*
                deleteModule(request);
				callPage(response, DMS.JSP_MODULE_LISTING, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			
			////////////////////////////////Module Update form//////////////////////////////////
			else if (DMS.MODULE_SAVE_UPDATE.equals(strAction))
			{
				/*
                String strResult = updateModule(request);
				if ("".equals(strResult))
					callPage(response, DMS.JSP_MODULE_LISTING, request);
				else if (strResult != null)
					callPage(response, DMS.JSP_MODULE_UPDATE, request);
				else
				{
					getLoginForm(request);
					callPage(response, DMS.JSP_LOGIN, request);
				}
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			
			////////////////////////////////Manage Planned Defect form//////////////////////////////////
			else if (DMS.DEFECT_PLAN_LIST.equals(strAction))
			{
				/*
                getPlannedDefect(request);
				callPage(response, DMS.JSP_MANAGE_PLANNED_DEFECT, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			else if (DMS.DEFECT_PLAN_ADD.equals(strAction))
			{
				/*
                addPlannedDefect(request);
				getPlannedDefect(request);
				callPage(response, DMS.JSP_MANAGE_PLANNED_DEFECT, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
			}
			else if (DMS.DEFECT_PLAN_DELETE.equals(strAction))
			{
				/*
                deletePlannedDefect(request);
				getPlannedDefect(request);
				callPage(response, DMS.JSP_MANAGE_PLANNED_DEFECT, request);
                */
                callPage(response, DMS.JSP_REMOVETOFI, request);
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
			logger.error("Exception in ProjectEnvironmentServlet.performTask().", exception);
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
				logger.debug("ProjectEnvironmentServlet.checkSessionVariables(): Invalid session.");
			
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
	 * Get a list of user assignment in project
	 * @author  Nguyen Thai Son.
	 * @version  24 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getAssignUserList(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		//STEP 2 - Using BO to get assigned user list
		AssignListBean beanAssignList = new AssignListBean();
		AssignBO boAssign = new AssignBO();
		
		beanAssignList = boAssign.getAssignList(beanUserInfo.getProjectID());
		
		ComboUser combo = new ComboUser(false);
		beanAssignList.setListing(combo.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		HttpSession session = request.getSession();
		if (session.getAttribute("beanAssignList") != null)	session.removeAttribute("beanAssignList");
		session.setAttribute("beanAssignList", beanAssignList);
	}
	
	/**
	 * Add an assigned user
	 * @author  Nguyen Thai Son.
	 * @version  29 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addAssignedUser(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		String[] strPositionSelected = request.getParameterValues("cboPosition");
		String strPosition ="";
    	for (int i= 0; i< strPositionSelected.length; i++)
        	strPosition += strPositionSelected[i];
        int nDevID = 0;
        if (request.getParameter("cboAssignTo") != null)
        	nDevID = Integer.parseInt(request.getParameter("cboAssignTo"));
        String strStatus = request.getParameter("cboStatus");
		
		//STEP 2 - Using BO to get assigned user list
		AssignListBean beanAssignList = new AssignListBean();
		AssignBO boAssign = new AssignBO();
		
		int nResult = boAssign.addAssignedUser(beanUserInfo.getProjectID(), nDevID, strPosition, strStatus);
		beanAssignList = boAssign.getAssignList(beanUserInfo.getProjectID());
		
		beanAssignList.setMessage(nResult);
		
		ComboUser combo = new ComboUser(false);
		beanAssignList.setListing(combo.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		HttpSession session = request.getSession();
		if (session.getAttribute("beanAssignList") != null)	session.removeAttribute("beanAssignList");
		session.setAttribute("beanAssignList", beanAssignList);
	}
	
	/**
	 * Delete assigned user(s)
	 * @author  Nguyen Thai Son.
	 * @version  29 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteAssignedUser(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		
		//STEP 2 - Using BO to get assigned user list
		AssignListBean beanAssignList = new AssignListBean();
		AssignBO boAssign = new AssignBO();
		
		int nResult = boAssign.deleteAssignedUser(beanUserInfo.getProjectID(), arrSelectedRows);
		beanAssignList = boAssign.getAssignList(beanUserInfo.getProjectID());
		
		beanAssignList.setMessage(nResult);
		
		ComboUser combo = new ComboUser(false);
		beanAssignList.setListing(combo.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		HttpSession session = request.getSession();
		if (session.getAttribute("beanAssignList") != null)	session.removeAttribute("beanAssignList");
		session.setAttribute("beanAssignList", beanAssignList);
	}
	
	/**
	 * Create a form to update an assigned user
	 * @author  Nguyen Thai Son.
	 * @version  30 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createUpdateAssignForm(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		AssignListBean beanAssignList = (AssignListBean)request.getSession().getAttribute("beanAssignList");
		if (beanAssignList == null)
		{
			getAssignUserList(request);
			return;
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		String strRow = request.getParameter("UpdateUser");
	    int nRow = Integer.parseInt(strRow);
	    
	    AssignUpdateBean beanAssignUpdate = new AssignUpdateBean();
    	
		StringMatrix smAssigned = new StringMatrix();
	    StringVector svAssigned = new StringVector(4);
		String strID = beanAssignList.getAssignUserList().getCell(nRow, 0);
		String strName = beanAssignList.getAssignUserList().getCell(nRow, 1);
		String strPosition = beanAssignList.getAssignUserList().getCell(nRow, 2);
		String strStatus = beanAssignList.getAssignUserList().getCell(nRow, 3);

		svAssigned.setCell(0, strID);
		svAssigned.setCell(1, strName);
		svAssigned.setCell(2, strPosition);
		svAssigned.setCell(3, strStatus);
		smAssigned.addRow(svAssigned);
		beanAssignUpdate.setAssignUserList(smAssigned);
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		request.setAttribute("beanAssignUpdate", beanAssignUpdate);
	}
	
	/**
	 * Update an assigned user
	 * @author  Nguyen Thai Son.
	 * @version  30 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void updateAssignedUser(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		int nDevID = 0;
		if (request.getParameter("hidID") != null)
			nDevID = Integer.parseInt(request.getParameter("hidID"));
		String[] strPositionSelected = request.getParameterValues("cboPosition");
		String strPosition ="";
    	for (int i= 0; i< strPositionSelected.length; i++)
        	strPosition += strPositionSelected[i];
		String strStatus = request.getParameter("cboStatus");

		//STEP 2 - Using BO to get assigned user list
		AssignListBean beanAssignList = new AssignListBean();
		AssignBO boAssign = new AssignBO();
		
		int nResult = boAssign.updateAssignedUser(beanUserInfo.getProjectID(), nDevID, strPosition, strStatus);
		beanAssignList = boAssign.getAssignList(beanUserInfo.getProjectID());
		beanAssignList.setMessage(nResult);
		
		ComboUser combo = new ComboUser(false);
		beanAssignList.setListing(combo.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		HttpSession session = request.getSession();
		if (session.getAttribute("beanAssignList") != null)	session.removeAttribute("beanAssignList");
		session.setAttribute("beanAssignList", beanAssignList);
	}
	
	
	/**
	 * Get a list of work product in project
	 * @author  Nguyen Thai Son.
	 * @version  30 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getWorkProductList(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		//STEP 2 - Using BO to get work product list
		WorkProductListBean beanWorkProductList = new WorkProductListBean();
		WorkProductBO boWP = new WorkProductBO();
		
		beanWorkProductList = boWP.getWorkProductList(beanUserInfo.getProjectID());
		
		//WorkProduct
		ComboWorkProduct comboWP = new ComboWorkProduct(ComboBoxExt.COMBO_NORMAL);
		beanWorkProductList.setComboWorkProduct(comboWP.getListing());
		//Unit
		ComboSizeUnit comboUnit = new ComboSizeUnit();
		beanWorkProductList.setComboUnit(comboUnit.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		request.setAttribute("beanWorkProductList", beanWorkProductList);
	}
	
	/**
	 * Add a work product to project
	 * @author  Nguyen Thai Son.
	 * @version  31 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addWorkProduct(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		String strWPID = request.getParameter("cboWorkProduct");
		String strSize = request.getParameter("txtSize");
		String strUnit = request.getParameter("cboUnit");
		String strPlannedDefect = request.getParameter("txtPlannedDefect");
		String strReplannedDefect = request.getParameter("txtReplannedDefect");
		
		//STEP 2 - Using BO to add new a work product to DB
		WorkProductListBean beanWorkProductList = new WorkProductListBean();
		WorkProductBO boWP = new WorkProductBO();
		boWP.addWorkProduct(beanUserInfo.getProjectID(), strWPID, strSize, strUnit, strPlannedDefect, strReplannedDefect);
		
		beanWorkProductList = boWP.getWorkProductList(beanUserInfo.getProjectID());
		
		//WorkProduct
		ComboWorkProduct comboWP = new ComboWorkProduct(ComboBoxExt.COMBO_NORMAL);
		beanWorkProductList.setComboWorkProduct(comboWP.getListing());
		//Unit
		ComboSizeUnit comboUnit = new ComboSizeUnit();
		beanWorkProductList.setComboUnit(comboUnit.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		request.setAttribute("beanWorkProductList", beanWorkProductList);
	}
	
	/**
	 * Create update work product form
	 * @author  Nguyen Thai Son.
	 * @version  31 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @param   response javax.servlet.HttpServletResponse: the response object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createUpdateWorkProductForm(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//STEP 1 - Check session variables
		//checkSessionVariables(request, response);

		//STEP 2 - Get update parameters from client and put into update page
		String strID = request.getParameter("hidWPID");
  		String strName = request.getParameter("hidWPName");
  		String strSize = request.getParameter("hidWPSize");
  		String strUnit = request.getParameter("hidWPUnit");
  		String strPlannedDefect = request.getParameter("hidWPPlanned");
  		String strReplannedDefect = request.getParameter("hidWPReplanned");
  		
  		WorkProductUpdateBean beanWorkProductUpdate = new WorkProductUpdateBean();
  		
  		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(6);
  		svUpdate.setCell(0, strID);
    	svUpdate.setCell(1, strName);
		svUpdate.setCell(2, strSize);
	    svUpdate.setCell(3, strUnit);
    	svUpdate.setCell(4, strPlannedDefect);
    	svUpdate.setCell(5, strReplannedDefect);
		smUpdate.addRow(svUpdate);

		beanWorkProductUpdate.setWorkProductSizeList(smUpdate);
		
		//Unit
		ComboSizeUnit comboUnit = new ComboSizeUnit();
		beanWorkProductUpdate.setComboUnit(comboUnit.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		request.setAttribute("beanWorkProductUpdate", beanWorkProductUpdate);
	}
	
	/**
	 * Delete a work product out of project
	 * @author  Nguyen Thai Son.
	 * @version  31 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteWorkProduct(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		//STEP 2 - Using BO to add new a work product to DB
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		WorkProductListBean beanWorkProductList = new WorkProductListBean();
		WorkProductBO boWP = new WorkProductBO();
		String strResult = boWP.deleteWorkProduct(beanUserInfo.getProjectID(), arrSelectedRows);
		
		beanWorkProductList = boWP.getWorkProductList(beanUserInfo.getProjectID());
		beanWorkProductList.setClientMessage(strResult);
		
		//WorkProduct
		ComboWorkProduct comboWP = new ComboWorkProduct(ComboBoxExt.COMBO_NORMAL);
		beanWorkProductList.setComboWorkProduct(comboWP.getListing());
		//Unit
		ComboSizeUnit comboUnit = new ComboSizeUnit();
		beanWorkProductList.setComboUnit(comboUnit.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		request.setAttribute("beanWorkProductList", beanWorkProductList);
	}
	
	/**
	 * Update a work product
	 * @author  Nguyen Thai Son.
	 * @version  31 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private String updateWorkProduct(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return null;
		}
		
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(6);
  
		svUpdate.setCell(0, request.getParameter("hidWP_ID"));
		svUpdate.setCell(1, request.getParameter("hidWPName"));
		svUpdate.setCell(2, request.getParameter("txtSize"));
		svUpdate.setCell(3, request.getParameter("cboUnit"));
		svUpdate.setCell(4, request.getParameter("txtPlannedDefect"));
		svUpdate.setCell(5, request.getParameter("txtReplannedDefect"));
		smUpdate.addRow(svUpdate);
		
		//STEP 2 - Using BO to update work product to DB
		WorkProductListBean beanWorkProductList = new WorkProductListBean();
		WorkProductBO boWP = new WorkProductBO();
		String strResult = boWP.updateWorkProduct(beanUserInfo.getProjectID(), smUpdate);
		
		if ("".equals(strResult))
		{
			beanWorkProductList = boWP.getWorkProductList(beanUserInfo.getProjectID());
		
			//WorkProduct
			ComboWorkProduct comboWP = new ComboWorkProduct(ComboBoxExt.COMBO_NORMAL);
			beanWorkProductList.setComboWorkProduct(comboWP.getListing());
			//Unit
			ComboSizeUnit comboUnit = new ComboSizeUnit();
			beanWorkProductList.setComboUnit(comboUnit.getListing());
		
			//STEP 3 - Put result into BEAN preparing to display
			request.setAttribute("beanWorkProductList", beanWorkProductList);
		}
		else
		{
			WorkProductUpdateBean beanWorkProductUpdate = new WorkProductUpdateBean();
			beanWorkProductUpdate.setWorkProductSizeList(smUpdate);
		
			//Unit
			ComboSizeUnit comboUnit = new ComboSizeUnit();
			beanWorkProductUpdate.setComboUnit(comboUnit.getListing());
		
			//STEP 3 - Put result into BEAN preparing to display
			request.setAttribute("beanWorkProductUpdate", beanWorkProductUpdate);
			request.setAttribute(DMS.MSG_CLIENT, strResult);
		}
		
		return strResult;
	}
	
	/**
	 * Get a list of modules in project
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getModuleList(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		//STEP 2 - Using BO to get module list
		ModuleListBean beanModuleList = new ModuleListBean();
		ModuleBO boModule = new ModuleBO();
		
		beanModuleList = boModule.getModuleList(beanUserInfo.getProjectID());
		
		//ComboWorkProduct combo = new ComboWorkProduct(false);
		//beanModuleList.setWorkProductList(combo.getListing());
		ComboWPSize combo = new ComboWPSize(true, beanUserInfo.getProjectID());
		beanModuleList.setWorkProductList(combo.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		request.setAttribute("beanModuleList", beanModuleList);
	}
	
	/**
	 * Add a module to project
	 * @author  Nguyen Thai Son.
	 * @version  31 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addModule(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		StringVector svModule = new StringVector(7);
		svModule.setCell(0, "0");
		svModule.setCell(1, request.getParameter("txtName"));
		String strWPID = request.getParameter("cboWP");
		svModule.setCell(2, strWPID);
		svModule.setCell(3, request.getParameter("txtPlannedDefect"));
		svModule.setCell(4, request.getParameter("txtReplannedDefect"));
		
		String strPlannedDefect = request.getParameter("hidPlannedDefect_" + strWPID);
		svModule.setCell(5, (strPlannedDefect == null || "".equals(strPlannedDefect)) ? "0" : strPlannedDefect);
		
		String strReplannedDefect = request.getParameter("hidReplannedDefect_" + strWPID);
		svModule.setCell(6, (strReplannedDefect == null || "".equals(strReplannedDefect)) ? "0" : strReplannedDefect);
		
		
		//STEP 2 - Using BO to add new a module to DB
		ModuleListBean beanModuleList = new ModuleListBean();
		ModuleBO boModule = new ModuleBO();
		String strResult = boModule.addModule(beanUserInfo.getProjectID(), svModule);
		
		beanModuleList = boModule.getModuleList(beanUserInfo.getProjectID());
		beanModuleList.setClientMessage(strResult);
		
		//Work product combo box
		ComboWPSize combo = new ComboWPSize(true, beanUserInfo.getProjectID());
		beanModuleList.setWorkProductList(combo.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		request.setAttribute("beanModuleList", beanModuleList);
	}
	
	/**
	 * Create update module form
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void createUpdateModuleForm(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		
		//STEP 2 - Get update parameters from client and put into update page
		String strID = request.getParameter("hidMID");
  		String strName = request.getParameter("hidMName");
  		String strPlannedDefect = request.getParameter("hidMPlanned");
  		String strReplannedDefect = request.getParameter("hidMReplanned");
  		String strWPID = request.getParameter("hidMWPID");
  		String strWPName = request.getParameter("hidMWPName");
  		
  		
  		ModuleUpdateBean beanModuleUpdate = new ModuleUpdateBean();
  		
  		StringMatrix smUpdate = new StringMatrix();
  		StringVector svUpdate = new StringVector(6);
  		svUpdate.setCell(0, strID);
    	svUpdate.setCell(1, strName);
		svUpdate.setCell(2, strWPName);
	    svUpdate.setCell(3, strPlannedDefect);
    	svUpdate.setCell(4, strReplannedDefect);
    	svUpdate.setCell(5, strWPID);
		smUpdate.addRow(svUpdate);

		beanModuleUpdate.setSetupModuleList(smUpdate);
		
		ComboWPSize combo = new ComboWPSize(false, beanUserInfo.getProjectID());
		beanModuleUpdate.setWorkProductList(combo.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		request.setAttribute("beanModuleUpdate", beanModuleUpdate);
	}
	
	/**
	 * Delete a module out of project
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deleteModule(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		//STEP 2 - Using BO to add new a work product to DB
		String[] arrSelectedRows = request.getParameterValues("checkBox");
		ModuleListBean beanModuleList = new ModuleListBean();
		ModuleBO boModule = new ModuleBO();
		String strResult = boModule.deleteModule(beanUserInfo.getProjectID(), arrSelectedRows);
		
		beanModuleList = boModule.getModuleList(beanUserInfo.getProjectID());
		beanModuleList.setClientMessage(strResult);
		
		//WorkProduct
		ComboWPSize combo = new ComboWPSize(true, beanUserInfo.getProjectID());
		beanModuleList.setWorkProductList(combo.getListing());
		
		//STEP 3 - Put result into BEAN preparing to display
		request.setAttribute("beanModuleList", beanModuleList);
	}
	
	/**
	 * Update a module
	 * @author  Nguyen Thai Son.
	 * @version  31 October, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private String updateModule(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return null;
		}
		
		StringMatrix smUpdate = new StringMatrix();
		StringVector svUpdate = new StringVector(7);
  
		svUpdate.setCell(0, request.getParameter("hidModuleID"));		//Module ID
		svUpdate.setCell(1, request.getParameter("txtName"));		//Module Name
		String strWPID = request.getParameter("cboWP");
		svUpdate.setCell(2, strWPID);
		svUpdate.setCell(3, request.getParameter("txtPlannedDefect"));		//
		svUpdate.setCell(4, request.getParameter("txtReplannedDefect"));		//
		
		String strPlannedDefect = request.getParameter("hidPlannedDefect_" + strWPID);
		svUpdate.setCell(5, (strPlannedDefect == null || "".equals(strPlannedDefect)) ? "0" : strPlannedDefect);
		
		String strReplannedDefect = request.getParameter("hidReplannedDefect_" + strWPID);
		svUpdate.setCell(6, (strReplannedDefect == null || "".equals(strReplannedDefect)) ? "0" : strReplannedDefect);
		smUpdate.addRow(svUpdate);
		
		
		//STEP 2 - Using BO to update module to DB
		ModuleListBean beanModuleList = new ModuleListBean();
		ModuleBO boModule = new ModuleBO();
		String strResult = boModule.updateModule(beanUserInfo.getProjectID(), smUpdate);

		if ("".equals(strResult))
		{
			beanModuleList = boModule.getModuleList(beanUserInfo.getProjectID());
			beanModuleList.setClientMessage(strResult);

			//WorkProduct
			ComboWPSize combo = new ComboWPSize(true, beanUserInfo.getProjectID());
			beanModuleList.setWorkProductList(combo.getListing());
		
			//STEP 3 - Put result into BEAN preparing to display
			request.setAttribute("beanModuleList", beanModuleList);	
		}
		else 
		{
			ModuleUpdateBean beanModuleUpdate = new ModuleUpdateBean();
  		
  			StringMatrix smBackUpdate = new StringMatrix();
  			StringVector svBackUpdate = new StringVector(6);
  			svBackUpdate.setCell(0, smUpdate.getCell(0, 0));
    		svBackUpdate.setCell(1, smUpdate.getCell(0, 1));
			svBackUpdate.setCell(2, "Cu dau qua!");
	    	svBackUpdate.setCell(3, smUpdate.getCell(0, 3));
    		svBackUpdate.setCell(4, smUpdate.getCell(0, 4));
    		svBackUpdate.setCell(5, smUpdate.getCell(0, 2));
			smBackUpdate.addRow(svBackUpdate);

			beanModuleUpdate.setSetupModuleList(smBackUpdate);
		
			ComboWPSize combo = new ComboWPSize(true, beanUserInfo.getProjectID());
			beanModuleUpdate.setWorkProductList(combo.getListing());
			
			request.setAttribute("beanModuleUpdate", beanModuleUpdate);
			request.setAttribute(DMS.MSG_CLIENT, strResult);
		}
		return strResult;
	}
	
	/**
	 * Get planned defect list
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void getPlannedDefect(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		//STEP 2 - Using BO to get planned defect list
		StringMatrix smResult = new StringMatrix();
		DefectPlanBO boDefectPlan = new DefectPlanBO();
		smResult = boDefectPlan.getPlannedDefectList(beanUserInfo.getProjectID());
		
		DefectPlanBean beanDefectPlan = new DefectPlanBean();
		beanDefectPlan.setPlannedDefectList(smResult);
		
		//Combo boxes for adding a new defect plan
		ComboBoxExt cboComboCode;
		cboComboCode = new ComboModuleCode(beanUserInfo.getProjectID(), ComboBoxExt.COMBO_EMPTY);
		beanDefectPlan.setComboModuleCode(cboComboCode.getListing());
		cboComboCode = new ComboWPSize(true, beanUserInfo.getProjectID());
		beanDefectPlan.setComboWorkProduct(cboComboCode.getListing());
		
		//STEP 3 - Put result into BEAN
		request.setAttribute("beanDefectPlan", beanDefectPlan);
	}
	
	/**
	 * Add a planned defect
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void addPlannedDefect(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		StringVector vecPlanned = new StringVector(24);

		vecPlanned.setCell(0, "");
		vecPlanned.setCell(1, "");
		String strWPID = request.getParameter("cboWorkProduct");
		vecPlanned.setCell(2, strWPID);
		String strModuleID = request.getParameter("cboModule");
		vecPlanned.setCell(3, strModuleID);
		
		//QC_Activity
		vecPlanned.setCell(4, request.getParameter("hidDocumentReview"));			//QA_ID = 20
		String strDR = request.getParameter("txtDocumentR");						//value
		vecPlanned.setCell(5, (strDR == null || "".equals(strDR)) ? "0" : strDR);

		vecPlanned.setCell(6, request.getParameter("hidPrototypeReview"));
		String strPR = request.getParameter("txtPrototypeR");
		vecPlanned.setCell(7, (strPR == null || "".equals(strPR)) ? "0" : strPR);
		
		vecPlanned.setCell(8, request.getParameter("hidCodeReview"));
		String strCR = request.getParameter("txtCodeR");
		vecPlanned.setCell(9, (strCR == null || "".equals(strCR)) ? "0" : strCR);
		
		vecPlanned.setCell(10, request.getParameter("hidUnitTest"));
		String strUT = request.getParameter("txtUnitT");
		vecPlanned.setCell(11, (strUT == null || "".equals(strUT)) ? "0" : strUT);
		
		vecPlanned.setCell(12, request.getParameter("hidIntegrationTest"));
		String strIT = request.getParameter("txtIntegrationT");
		vecPlanned.setCell(13, (strIT == null || "".equals(strIT)) ? "0" : strIT);
		
		vecPlanned.setCell(14, request.getParameter("hidSystemTest"));
		String strST = request.getParameter("txtSystemT");
		vecPlanned.setCell(15, (strST == null || "".equals(strST)) ? "0" : strST);
		
		vecPlanned.setCell(16, request.getParameter("hidAcceptanceTest"));
		String strAT = request.getParameter("txtAcceptanceT");
		vecPlanned.setCell(17, (strAT == null || "".equals(strAT)) ? "0" : strAT);
		
		vecPlanned.setCell(18, DMS.OTHER_QA);
		String strOs = request.getParameter("txtOthers");
		vecPlanned.setCell(19, (strOs == null || "".equals(strOs)) ? "0" : strOs);
		
		//For module
		String strPlannedDefect = request.getParameter("hidPlannedDefect_" + strModuleID);
		vecPlanned.setCell(20, (strPlannedDefect == null || "".equals(strPlannedDefect)) ? "0" : strPlannedDefect);
		
		String strReplannedDefect = request.getParameter("hidReplannedDefect_" + strModuleID);
		vecPlanned.setCell(21, (strReplannedDefect == null || "".equals(strReplannedDefect)) ? "0" : strReplannedDefect);
		
		//For work product
		String strWPPlannedDefect = request.getParameter("hidWPPlannedDefect_" + strWPID);
		vecPlanned.setCell(22, (strWPPlannedDefect == null || "".equals(strWPPlannedDefect)) ? "0" : strWPPlannedDefect);
		
		String strWPReplannedDefect = request.getParameter("hidWPReplannedDefect_" + strWPID);
		vecPlanned.setCell(23, (strWPReplannedDefect == null || "".equals(strWPReplannedDefect)) ? "0" : strWPReplannedDefect);
		
		DefectPlanBO boDefectPlan = new DefectPlanBO();
		String strResult = boDefectPlan.addPlannedDefect(beanUserInfo.getProjectID(), vecPlanned);
		
		request.setAttribute(DMS.MSG_CLIENT, strResult);
	}
	
	/**
	 * Delete a planned defect
	 * @author  Nguyen Thai Son.
	 * @version  01 November, 2002.
	 * @param   request javax.servlet.HttpServletRequest: the request object
	 * @exception   Exception    If an exception occurred.
	 */
	private void deletePlannedDefect(HttpServletRequest request) throws Exception
	{
		//STEP 1 - Check session variables
		UserInfoBean beanUserInfo = (UserInfoBean)request.getSession().getAttribute("beanUserInfo");
		if (beanUserInfo == null)	
		{
			getLoginForm(request);
			return;
		}
		
		String strSelectedID = request.getParameter("hidSelectedItem");
        if (DMS.DEBUG)
            logger.debug("strSelectedID = " + strSelectedID);
		
		DefectPlanBO boDefectPlan = new DefectPlanBO();
		boDefectPlan.deletePlannedDefect(strSelectedID);	
	}
}