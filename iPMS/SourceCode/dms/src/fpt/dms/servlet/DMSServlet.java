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
 * @Title:        DispatcherServlet.java
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

import fpt.dms.bean.UserInfoBean;
import fpt.dms.bean.DefectManagement.QueryListingBean;
import fpt.dms.bean.login.LoginBean;
import fpt.dms.bo.DefectManagement.DefectBO;
import fpt.dms.bo.DefectManagement.QueryBO;
import fpt.dms.bo.DefectManagement.UploadBO;
import fpt.dms.bo.combobox.ComboProject;
import fpt.dms.bo.login.VerifyLogin;
import fpt.dms.constant.DMS;
import fpt.dms.framework.core.BaseServlet;
import fpt.dms.framework.util.CommonUtil.CommonUtil;
import fpt.dms.framework.util.CommonUtil.InputFileDeclared;
import fpt.dms.framework.util.CommonUtil.Rsa;
import fpt.dms.framework.util.StringUtil.StringMatrix;

public class DMSServlet extends BaseServlet {

    private static Logger logger = Logger.getLogger(DMSServlet.class.getName());
    /**
     * DMSServlet constructor.
     */
    public DMSServlet() {
    }
    /**
     * Process client request by action.
     * @author  Nguyen Thai Son
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Throwable    If an exception occurred.
     */
    public void performTask(HttpServletRequest request,
        					HttpServletResponse response) throws Throwable {
        try {
            // Reset session timeout to normal mode
            // Temporary disabled from 19-Jan-07 due to DMS performance issue
            /*
            if (request.getSession().getMaxInactiveInterval() < DMS.SESSION_TIMEOUT_NORMAL) {
                //logger.debug("set normal session timeout");
                request.getSession().setMaxInactiveInterval(DMS.SESSION_TIMEOUT_NORMAL);
            }*/
            
            String strType = request.getContentType();
            // Upload form
            if ((strType != null) && (strType.startsWith("multipart/form-data"))) {
                upload(request, response);
                return;
            }
            else {
                String strAction = request.getParameter("hidAction");
                
                logger.info("hidActon = "+ strAction);
                
                //String strActionDetail = request.getParameter("hidActionDetail");
                //Login form (logoff action)
                if (DMS.HOMEPAGE_ACTION.equals(strAction)) {
                    getLoginForm(request);
                    //display result page
                    callPage(response, DMS.JSP_LOGIN, request);
                    return;
                }
                //View Defect Listing form (login action)
                else if (DMS.VIEW_DEFECT_LISTING_ACTION.equals(strAction)) {
                    int nResult = getViewDefectListing(request);
                    //display result page
                    if (nResult == 1) {
                        callPage(response, DMS.JSP_QUERY_LISTING, request);
                    }
                    // Login failed
                    else {
                        // Check session and return Login form
                        if (checkSessionVariables(request, response)) {
                            callPage(response, DMS.JSP_LOGIN, request);
                        }
                    }
                    return;
                }
                //get the Rsa public key
                else if(DMS.GET_RSA_ACTION.equals(strAction)){
                    if (DMS.DEBUG)
                        logger.info("DMS.GET_RSA_ACTION");
                    Rsa.getAuthenticate(request,response);
                    return;
                }

                //Check session
                if (!checkSessionVariables(request, response)) {
                    return;
                }
                else if (DMS.DEFECT_MANAGEMENT_ACTION.equals(strAction)) {
                    doChange(request);
					
					// Modified by HaiMM (Mar-17-2008)
					String strRealPath = getServletContext().getRealPath("");
					String strProjectFile = strRealPath + java.io.File.separator + DMS.PILOT_PROJECT;
					
					InputFileDeclared file = new InputFileDeclared(strProjectFile);
					
					String project1 = file.getWord();
					String project2 = file.getWord();
					String project3 = file.getWord();
					String project4 = file.getWord();
					String project5 = file.getWord();
					String project6 = file.getWord();
					
					HttpSession session = request.getSession();
					String strProjectId = (String)session.getAttribute("nProjectID");

					if (strProjectId.equalsIgnoreCase(project1)
						|| strProjectId.equalsIgnoreCase(project2)
						|| strProjectId.equalsIgnoreCase(project3)
						|| strProjectId.equalsIgnoreCase(project4)
						|| strProjectId.equalsIgnoreCase(project5)
						|| strProjectId.equalsIgnoreCase(project6)) {						
						//Go to inform jsp page
						callPage(response, DMS.JSP_PILOT_PROJECT, request);
					}
					else {
						//Go to DefectManagementServlet
						callPage(response, DMS.DEFECT_MANAGEMENT_SERVLET, request);
					}
                    return;
                }
                else if (DMS.PROJECT_ENVIRONMENT_ACTION.equals(strAction)) {
                    doChange(request);
                    //go to ProjectManagementServlet
                    if(getUserInfo(request).isProjectLeader()){                    
                    	callPage(response, DMS.PROJECT_ENVIRONMENT_SERVLET, request);
                    }
                    else {
                    	callPage(response,DMS.JSP_NOPERMISSION,request);
                    }
                    return;
                }
                else if (DMS.SETUP_ENVIRONMENT_ACTION.equals(strAction)) {
                    doChange(request);
                    //go to SetupEnvironmentServlet
                    if(getUserInfo(request).isSetupEnvironment()){                    
                    	callPage(response, DMS.SETUP_ENVIRONMENT_SERVLET, request);
                    }
                    else {
						callPage(response,DMS.JSP_NOPERMISSION,request);
                    }
                    return;
                }
                else {
                    //sendErrorRedirect(request, response, DMS.JSP_ERROR, new Exception("Invalid action: strAction = " + strAction));
                    getLoginForm(request);
                    callPage(response, DMS.JSP_LOGIN, request);
                    return;
                }
            }
        }
        catch (Exception exception) {
            logger.error("DMSServlet - performTask(): ", exception);
            try {
                sendErrorRedirect(request, response, DMS.JSP_ERROR, exception);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * upload
     * Import defect by user, upload attach files
     * @author  Tu Ngoc Trung
     * @version December 10, 2003
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception, Throwable
     */
    private void upload(HttpServletRequest request,
                        HttpServletResponse response) throws Exception, Throwable {
        //STEP 1 - Check session
        if (!checkSessionVariables(request, response)) {
            return;
        }
        //STEP 2 - Upload file
        DefectBO defectBO = new DefectBO();
        int nPosted = defectBO.upload(request, response, this);
        // Post form is import page
        if (nPosted == 0) {
            callPage(response, DMS.JSP_DEFECT_IMPORT, request);
        }
        // Post form is upload attach file from Add new or Update
        else if ((nPosted == 1) || (nPosted == 2)) {
            callPage(response, DMS.JSP_DEFECT_ATTACH_RESULT, request);
        }
        else if (nPosted == 3){
			DefectManagementServlet defectManager = new DefectManagementServlet();
			defectManager.getImportForm(request);
			callPage(response, DMS.JSP_DEFECT_IMPORT, request);
        }
        else {
        	callPage(response, DMS.JSP_ERROR, request);
        }
    }
    
    /**
     * Reset session variables
     * @author  Nguyen Thai Son.
     * @version 24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   IOException    If an input or output exception occurred.
     * @exception   ServletException    If a servlet exception occurred.
     * @return boolean Valid session or not
     */
    private boolean checkSessionVariables(HttpServletRequest request,
                                          HttpServletResponse response) throws ServletException, IOException {
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
                    logger.debug("DMSServlet.checkSessionVariables(): Invalid session.");
                }
                getLoginForm(request);
                LoginBean beanLogin = (LoginBean) request.getAttribute("beanLogin");
                // If no action => fist time enter DMS, it's not Invalid session
                if (request.getParameter("hidAction") != null) {
                    beanLogin.setMessage(DMS.MSG_INVALID_SESSION);
                }
                callPage(response, DMS.JSP_LOGIN, request);

                bResult = false;
            }
        }
        catch (Exception exception) {
            //logger.error("Exception in checkSessionVariables().", exception);
            sendErrorRedirect(request, response, DMS.JSP_ERROR, exception);
            bResult = false;
        }

        return bResult;
    }

    /**
     * Get login form.
     * @author  Nguyen Thai Son.
     * @version  24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    //Modified by    : Pham Tuan Minh
    //Modified date  : November 20, 2002
    //for remove Project List Combo
    private void getLoginForm(HttpServletRequest request) throws Exception {
        // Remove cache of Defect attachment
        UploadBO.clearTempAttach(request.getSession());

        HttpSession session = request.getSession();
        if (session.getAttribute("beanUserInfo") != null) {
            session.invalidate();
            session = request.getSession();
        }
        Rsa rsa = new Rsa(160);
        session.setAttribute("rsa", rsa);
        //go to BO to get data
        LoginBean beanLogin = new LoginBean();
        request.setAttribute("beanLogin", beanLogin);

        // Set session timeout shorter to clear waste time
        // Temporary disabled from 19-Jan-07 due to DMS performance issue
        /*
        if (request.getSession().getMaxInactiveInterval() > DMS.SESSION_TIMEOUT_SHORT) {
            request.getSession().setMaxInactiveInterval(DMS.SESSION_TIMEOUT_SHORT);
        }
        else {
            logger.warn("Last session MaxInactiveInterval the same with" +
                " SESSION_TIMEOUT_SHORT !!!");
        }*/
    }

    //Modified by    : Pham Tuan Minh
   //Modified date  : November 20, 2002
   //for remove Project List Combo
    /**
     * Get a list of queries from BO.
     * @author  Nguyen Thai Son.
     * @version  24 October, 2002.
     * @param   request javax.servlet.HttpServletRequest: the request object
     * @exception   Exception    If an exception occurred.
     */
    private int getViewDefectListing(HttpServletRequest request) throws Exception {
        int nResult = -1;
        boolean bSuccessful = true;

        HttpSession session = request.getSession();
        //special code if called from the insight
        String strUsername = request.getParameter("txtAccount");
        String strPassword = request.getParameter("txtPassword");
        
        int nProjectID = 1;

        //go to BO to get data
        VerifyLogin login = new VerifyLogin();

        //create the UserInfoBean
        UserInfoBean beanUserInfo = new UserInfoBean();
        beanUserInfo.setPassword(strPassword);
        beanUserInfo.setAccount(strUsername);
        session.setAttribute("beanUserInfo", beanUserInfo);

        //create the project list
        doChange(request);

        ComboProject cboProject = (ComboProject)session.getAttribute("beanComboProject");
        //get the project list
        StringMatrix smProject = cboProject.getListing();
        if (smProject.getNumberOfRows() > 0) {
            //get the first project_id
            nProjectID = CommonUtil.StrToInt(smProject.getCell(0,0));

            //get info about the user in the project
            StringMatrix smLogin = login.getUserInfo(strUsername, strPassword, nProjectID);
            if (smLogin.getNumberOfRows() > 0) {
                beanUserInfo.setProjectID(CommonUtil.StrToInt(smLogin.getCell(0,7).toString()));
                beanUserInfo.setPassword(strPassword);
                beanUserInfo.setUserName(smLogin.getCell(0, 0));
                beanUserInfo.setGroupName(smLogin.getCell(0, 1));
                beanUserInfo.setAccount(smLogin.getCell(0, 2));
                beanUserInfo.setRole(smLogin.getCell(0, 3));
                beanUserInfo.setDateLogin(smLogin.getCell(0, 4));
                beanUserInfo.setPosition(smLogin.getCell(0, 5));
                beanUserInfo.setProjectCode(smLogin.getCell(0, 6));
                beanUserInfo.setDeveloperId(smLogin.getCell(0, 8));
                session.setAttribute("beanUserInfo", beanUserInfo);

                //go to BO to get data
                QueryBO query = new QueryBO();
                StringMatrix smQuery = query.getQueryList(nProjectID, strUsername);
    
                //put object (smQuery) into BEAN
                QueryListingBean beanQueryListing = new QueryListingBean();
                beanQueryListing.setQueryList(smQuery);
    
                if (nProjectID > 0) {
                    nResult = 1;
                }
                else {
                    bSuccessful = false;
                }
                //Login successfully to setup environment (for all projects)
                request.setAttribute("beanQueryListing", beanQueryListing);
            }
            else {
                bSuccessful = false;
            }
        }
        else { //Login Failed
            bSuccessful = false;
            nResult = 2;
        }

        if (!bSuccessful) {
            //go to BO to get data
            LoginBean beanLogin = new LoginBean();
            beanLogin.setAccount(strUsername);
            beanLogin.setMessage(DMS.MSG_NO_SYSTEM_PERMISSION);
            request.setAttribute("beanLogin", beanLogin);
            session.removeAttribute("beanComboProject");
        }

        return nResult;
    }
    /**
     * get session of UserInfoBean
     * @author Nguyen Huu Huy - TMG
     * @since September 08, 2006
     * @param request
     * @return
     */
    private UserInfoBean getUserInfo(HttpServletRequest request){
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
		return beanUserInfo;
    }
   /**
    * For process the change ProjectList combo and the Project Status combo
    * @author Pham Tuan Minh
    * @since December 5, 2002
    */
    private void doChange(HttpServletRequest request) {

        HttpSession session = request.getSession();
        UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
        String strAccount = beanUserInfo.getAccount();
        String strPassword = beanUserInfo.getPassword();

        String strNewStatus = "0";
        int nProjectID = -1;

        if (session.getAttribute("beanComboProject") == null) {
            // first login
            ComboProject cboProject = new ComboProject();
            cboProject.setStatusList(strAccount, strPassword);
            strNewStatus = cboProject.getStatusList().getCell(0, 0);
            beanUserInfo.setCurrentStatus(strNewStatus);
            cboProject.setProjectList(strAccount, strPassword, strNewStatus);
            session.setAttribute("beanComboProject", cboProject);
            strNewStatus = beanUserInfo.getCurrentStatus();
        }
        ComboProject cboProject = (ComboProject)session.getAttribute("beanComboProject");

        if (request.getParameter("cboProjectStatus") != null) {
            // request posted with status
            strNewStatus = request.getParameter("cboProjectStatus");
            session.setAttribute("cboProjectStatus", strNewStatus);
        }
        else if (session.getAttribute("cboProjectStatus") != null) {
            // if not, get previous status
            strNewStatus = (String)session.getAttribute("cboProjectStatus");
        }
        //if the status change, create the ProjectList
        if (!beanUserInfo.getCurrentStatus().equals(strNewStatus)) {
            cboProject.setProjectList(strAccount, strPassword, strNewStatus);
            nProjectID = Integer.parseInt(cboProject.getListing().getCell(0, 0));
        }
        //if status not change
        else {
            //get the project_id
            if (request.getParameter("cboProjectList") != null) {
                nProjectID = Integer.parseInt(request.getParameter("cboProjectList"));
            } 
            else if (session.getAttribute("nProjectID") != null) {
                nProjectID = Integer.parseInt((String)session.getAttribute("nProjectID"));
            }
        }
        //store the status
        beanUserInfo.setCurrentStatus(strNewStatus);
        session.setAttribute("nProjectID",CommonUtil.IntToStr(nProjectID));
		//set Info about the User in the project 
        if (nProjectID != -1) {
            if (beanUserInfo != null) {
                VerifyLogin login = new VerifyLogin();
                StringMatrix smUserInfo;
                beanUserInfo.setProjectID(nProjectID);
                smUserInfo = login.getUserInfo(strAccount,strPassword,nProjectID);
                if(smUserInfo.getNumberOfRows() > 0){
                    beanUserInfo.setUserName(smUserInfo.getCell(0, 0));
                    beanUserInfo.setGroupName(smUserInfo.getCell(0, 1));
                    beanUserInfo.setRole(smUserInfo.getCell(0, 3));
                    beanUserInfo.setDateLogin(smUserInfo.getCell(0, 4));
                    beanUserInfo.setPosition(smUserInfo.getCell(0, 5));
                    beanUserInfo.setProjectCode(smUserInfo.getCell(0, 6));				
                }
            }
        }
        session.setAttribute("beanUserInfo", beanUserInfo);
        session.setAttribute("beanComboProject", cboProject);
    }
}