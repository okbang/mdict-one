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
 * @Title:        ProjectManagementServlet.java
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
import fpt.dashboard.bean.ProjectManagement.AssignmentAddBean;
import fpt.dashboard.bean.ProjectManagement.AssignmentListBean;
import fpt.dashboard.bean.ProjectManagement.AssignmentOtherBean;
import fpt.dashboard.bean.ProjectManagement.MilestoneAddBean;
import fpt.dashboard.bean.ProjectManagement.MilestoneAllBean;
import fpt.dashboard.bean.ProjectManagement.MilestoneListBean;
import fpt.dashboard.bean.ProjectManagement.MilestoneUpdateBean;
import fpt.dashboard.bean.ProjectManagement.ProjectAddBean;
import fpt.dashboard.bean.ProjectManagement.ProjectCloseBean;
import fpt.dashboard.bean.ProjectManagement.ProjectDashboardBean;
import fpt.dashboard.bean.ProjectManagement.ProjectDetailBean;
import fpt.dashboard.bean.ProjectManagement.ProjectListBean;
import fpt.dashboard.bean.ProjectManagement.ProjectUpdateBean;
import fpt.dashboard.bo.ComboBox.ComboBO;
import fpt.dashboard.bo.ProjectManagememt.AssignmentBO;
import fpt.dashboard.bo.ProjectManagememt.DeveloperBO;
import fpt.dashboard.bo.ProjectManagememt.MilestoneBO;
import fpt.dashboard.bo.ProjectManagememt.ProjectBO;
import fpt.dashboard.bo.ProjectManagememt.ProjectDashboardBO;
import fpt.dashboard.constant.DATA;
import fpt.dashboard.constant.DB;
import fpt.dashboard.ProjectManagementTran.ejb.Constants;
import fpt.dashboard.servlet.core.BaseServlet;
//import fpt.dashboard.util.CommonUtil.CommonUtil;
//import fpt.dashboard.util.CommonUtil.Rsa;
import fpt.dashboard.util.StringUtil.StringMatrix;

public class ProjectManagementServlet extends BaseServlet {
    private static Logger logger =
        Logger.getLogger(ProjectManagementServlet.class.getName());
    /**
     * ProjectManagementServlet constructor.
     */
    public ProjectManagementServlet() {
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

            //Dashboard Homepage form
            if (DB.VIEW_PROJECT_LIST.equals(strAction)) {
                getProjectList(request, response);
                callPage(response, DB.JSP_PROJECT_LIST, request);
            } else if (DB.VIEW_DASHBOARD.equals(strAction)) {
                getProjectDashboard(request, response);
                callPage(response, DB.JSP_PROJECT_DASHBOARD, request);
            } else if (DB.VIEW_OTHER_ASSIGNMENT.equals(strAction)) {
                getOtherAssignmentList(request, response);
                callPage(response, DB.JSP_ASSIGNMENT_OTHER, request);
            } else if (DB.VIEW_ALL_MILESTONE.equals(strAction)) {
                getAllMilestoneList(request, response);
                callPage(response, DB.JSP_MILESTONE_ALL, request);
            }

            //Project List form
            else if (DB.PROJECT_ADD.equals(strAction)) {
                createProjectAddForm(request, response);
                callPage(response, DB.JSP_PROJECT_ADD, request);
            } else if (DB.PROJECT_DETAIL.equals(strAction)) {
                getProjectDetail(request, response);
                callPage(response, DB.JSP_PROJECT_DETAIL, request);
            }

            //Project Add form
            else if (DB.PROJECT_SAVE_NEW.equals(strAction)) {
                saveNewProject(request, response);
                createProjectAddForm(request, response);
                callPage(response, DB.JSP_PROJECT_ADD, request);
            }

            //Project Update form
            else if (DB.PROJECT_SAVE_UPDATE.equals(strAction)) {
                saveUpdateProject(request, response);
//                getProjectDetail(request, response);
//                callPage(response, DB.JSP_PROJECT_DETAIL, request);
            }

            //Project Detail form
            else if (DB.UPDATE_MAJOR_INFO.equals(strAction)) {
                createProjectUpdateForm(request, response);
                callPage(response, DB.JSP_PROJECT_UPDATE, request);
            } 
            else if (DB.MILESTONE_LIST.equals(strAction)) {
                getMilestoneList(request, response);
                callPage(response, DB.JSP_MILESTONE_LIST, request);
            } 
            else if (DB.ASSIGNMENT_LIST.equals(strAction)) {
                getAssignmentList(request, response);
                callPage(response, DB.JSP_ASSIGNMENT_LIST, request);
            } 
            else if (DB.PROJECT_CLOSE.equals(strAction)) {
                createProjectCloseForm(request, response);
                callPage(response, DB.JSP_PROJECT_CLOSE, request);
            }
            else if (DB.PROJECT_REOPEN.equals(strAction)) {
                reopenProject(request, response);
                getProjectDetail(request, response);
                callPage(response, DB.JSP_PROJECT_DETAIL, request);
            } else if (DB.PROJECT_DELETE.equals(strAction)) {
                deleteProject(request, response);
                getProjectList(request, response);
                callPage(response, DB.JSP_PROJECT_LIST, request);
            }

            //Milestone List form
            else if (DB.MILESTONE_ADD.equals(strAction)) {
                createAddMilestoneForm(request, response);
                callPage(response, DB.JSP_MILESTONE_ADD, request);
            } else if (DB.MILESTONE_DELETE.equals(strAction)) {
                deleteMilestone(request, response);
                getMilestoneList(request, response);
                callPage(response, DB.JSP_MILESTONE_LIST, request);
            } else if (DB.MILESTONE_UPDATE.equals(strAction)) {
                createUpdateMilestoneForm(request, response);
                callPage(response, DB.JSP_MILESTONE_UPDATE, request);
            }

            //Milestone Add form
            else if (DB.MILESTONE_SAVE_NEW.equals(strAction)) {
                saveNewMilestone(request, response);
                createAddMilestoneForm(request, response);
                callPage(response, DB.JSP_MILESTONE_ADD, request);
            }
            //Milestone Update form
            else if (DB.MILESTONE_SAVE_UPDATE.equals(strAction)) {
                saveUpdateMilestone(request, response);
                getMilestoneList(request, response);
                callPage(response, DB.JSP_MILESTONE_LIST, request);
            }

            //Assignment List form
            else if (DB.ASSIGNMENT_SAVE_UPDATE.equals(strAction)) {
                saveUpdateAssignment(request, response);
                getAssignmentList(request, response);
                callPage(response, DB.JSP_ASSIGNMENT_LIST, request);
            } else if (DB.ASSIGNMENT_ADD.equals(strAction)) {
                createAssignmentAddForm(request, response);
                callPage(response, DB.JSP_ASSIGNMENT_ADD, request);
            } else if (DB.ASSIGNMENT_DELETE.equals(strAction)) {
                deleteAssignment(request, response);
                getAssignmentList(request, response);
                callPage(response, DB.JSP_ASSIGNMENT_LIST, request);
            }

            //Assignment Add form
            else if (DB.ASSIGNMENT_SAVE_NEW.equals(strAction)) {
                saveNewAssignment(request, response);
                createAssignmentAddForm(request, response);
                callPage(response, DB.JSP_ASSIGNMENT_ADD, request);
            }

            //Project Close form
            else if (DB.PROJECT_SAVE_CLOSE.equals(strAction)) {
                closeProject(request, response);
                getProjectDetail(request, response);
                callPage(response, DB.JSP_PROJECT_DETAIL, request);
            }

            //Other Assignment List form
            else if (DB.OTHER_ASSIGNMENT_ADD.equals(strAction)) {
                String strMessage = "";
                createOtherAssignmentAddForm(request, response, strMessage);
                callPage(response, DB.JSP_ASSIGNMENT_OTHER_ADD, request);
            } else if (DB.OTHER_ASSIGNMENT_DELETE.equals(strAction)) {
                deleteOtherAssignment(request, response);
                getOtherAssignmentList(request, response);
                callPage(response, DB.JSP_ASSIGNMENT_OTHER, request);
            } else if (DB.OTHER_ASSIGNMENT_SAVE_NEW.equals(strAction)) {
                String strMessage = "";
                if (saveOtherAssignment(request, response) == 0) {
                    createOtherAssignmentAddForm(request, response, strMessage);
                } else {
                    strMessage = "Cannot assign people who's off for this period!";
                    createOtherAssignmentAddForm(request, response, strMessage);
                }
                callPage(response, DB.JSP_ASSIGNMENT_OTHER_ADD, request);
            } else {
                //              sendErrorRedirect(request, response, DB.JSP_ERROR, new Exception("Invalid action: strAction = " + strAction));
                getLoginForm(request, response);
                callPage(response, DB.JSP_LOGIN, request);
            }
            return;
        } catch (Exception exception) {
            logger.debug(
                "Exception in ProjectManagementServlet.performTask().");
            logger.error(
                "ProjectManagementServlet - performTask(): ",
                exception);
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
        if (session != null) {
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
                    "ProjectManagementServlet.checkSessionVariables(): Invalid session.");
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
     * Get a list of project
     * @author  Nguyen Thai Son
     * @version November 09, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void getProjectList(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        UserInfoBean beanUserInfo = checkSessionVariables(request, response);
        beanUserInfo.setTypeOfView(request.getParameter("hidActionDetail"));

        //STEP 2 - Get data from client to filter
        HttpSession session = request.getSession();
        //"ViewProjectList"
        session.setAttribute("beanUserInfo", beanUserInfo);

        //user group
        String strGroup = Constants.GROUP_ALL;
        if (request.getParameter("cboGroup") != null) {
            strGroup = request.getParameter("cboGroup");
            session.setAttribute("cboGroup", strGroup);
        } else if (request.getParameter("hidGroup") != null)
            strGroup = request.getParameter("hidGroup");
        else if (session.getAttribute("cboGroup") != null)
            strGroup = (String) session.getAttribute("cboGroup");
        else {
            strGroup = beanUserInfo.getGroupName();
            //set default view his group only.
            String strRole = beanUserInfo.getSRole();
            //formatted as "XXXXXXXXXX"
            if (strRole.charAt(4) == '1') //PQA
                strGroup = Constants.GROUP_ALL; //PQA can view all groups
        }

        //Project type (internal, external,...)
        String strType = DATA.PROJECT_EXTERNAL;
        if (request.getParameter("cboType") != null) {
            strType = request.getParameter("cboType");
            session.setAttribute("cboType", strType);
        } else if (session.getAttribute("cboType") != null)
            strType = (String) session.getAttribute("cboType");

        //Project status (on-going, closed,...)
        String strStatus = DATA.PROJECT_VALUE_STATUS_ONGOING;
        if (request.getParameter("cboStatus") != null) {
            strStatus = request.getParameter("cboStatus");
            session.setAttribute("cboStatus", strStatus);
        } else if (session.getAttribute("cboStatus") != null)
            strStatus = (String) session.getAttribute("cboStatus");

        //Project category (development, maintenance,...)
        String strCategory = DATA.PROJECT_VALUE_CATEGORY_DEVELOPMENT;
        if (request.getParameter("cboCategory") != null) {
            strCategory = request.getParameter("cboCategory");
            session.setAttribute("cboCategory", strCategory);
        } else if (session.getAttribute("cboCategory") != null)
            strCategory = (String) session.getAttribute("cboCategory");

        //STEP 3 - Using BO to get data from Dashboard database
        ProjectBO boProject = new ProjectBO();
        ProjectListBean beanProjectList = new ProjectListBean();
        beanProjectList =
            boProject.getProjectList(
                strGroup,
                strType,
                strStatus,
                strCategory,
                beanUserInfo);

        /////////////////////////////////////////////////////////////
        request.setAttribute("beanProjectList", beanProjectList);
    }

    /**
     * Create an adding form to add a project
     * @author  Nguyen Thai Son
     * @version November 09, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void createProjectAddForm(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        checkSessionVariables(request, response);

        ProjectBO boProject = new ProjectBO();
        ProjectAddBean beanProjectAdd = new ProjectAddBean();
        if (request.getAttribute("beanProjectAdd") != null) {
            beanProjectAdd = (ProjectAddBean) request.getAttribute("beanProjectAdd");
        }
        else {
            beanProjectAdd = boProject.createProjectAddForm();
        }

        ///////////////////////////////////////////////////////////////////////////////
        request.setAttribute("beanProjectAdd", beanProjectAdd);
    }

    /**
     * Delete project
     * @author  Nguyen Thai Son
     * @version November 09, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void deleteProject(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        UserInfoBean beanUserInfo = checkSessionVariables(request, response);

        String[] arrID = request.getParameterValues("hidProjectID");
        ProjectBO boProject = new ProjectBO();
        boProject.deleteProject(arrID);
    }

    /**
     * View project detail
     * @author  Nguyen Thai Son
     * @version November 09, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void getProjectDetail(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        UserInfoBean beanUserInfo = checkSessionVariables(request, response);

        String strProjectID = request.getParameter("hidProjectID");
        if (strProjectID != null)
            request.getSession().setAttribute("hidProjectID", strProjectID);
        else
            strProjectID =
                (String) request.getSession().getAttribute("hidProjectID");

        ProjectBO boProject = new ProjectBO();

        ProjectDetailBean beanProjectDetail = new ProjectDetailBean();
        beanProjectDetail = boProject.getProjectDetail(strProjectID);

        request.setAttribute("beanProjectDetail", beanProjectDetail);
    }

    /**
     * Save a project into database
     * @author  Nguyen Thai Son
     * @version November 09, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void saveNewProject(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        checkSessionVariables(request, response);

        //STEP 2 - Get data from client
        ProjectAddBean beanProjectAdd;
        if (request.getAttribute("beanProjectAdd") != null) {
            beanProjectAdd = (ProjectAddBean) request.getAttribute("beanProjectAdd");
        }
        else {
            ProjectBO boProject = new ProjectBO();
            beanProjectAdd = boProject.createProjectAddForm();
        }

        beanProjectAdd.setCode(request.getParameter("code"));   //(CommonUtil.decodeParameter(request, "code", "UTF-8"));
        beanProjectAdd.setName(request.getParameter("name"));   //(CommonUtil.decodeParameter(request, "name", "UTF-8"));
        beanProjectAdd.setLeader(request.getParameter("leader"));   //(CommonUtil.decodeParameter(request, "leader", "UTF-8"));

        beanProjectAdd.setStartDate(request.getParameter("startDate"));
        beanProjectAdd.setPerComplete(request.getParameter("perComplete"));

        beanProjectAdd.setDescription(request.getParameter("description")); //(CommonUtil.decodeParameter(request, "description", "UTF-8"));

        beanProjectAdd.setPlanFinishDate(
            request.getParameter("planFinishDate"));
        beanProjectAdd.setBaseFinishDate(
            request.getParameter("baseFinishDate"));
        beanProjectAdd.setActualFinishDate(
            request.getParameter("actualFinishDate"));

        beanProjectAdd.setPlanEffort(request.getParameter("planEffort"));
        beanProjectAdd.setBaseEffort(request.getParameter("baseEffort"));
        beanProjectAdd.setActualEffort(request.getParameter("actualEffort"));

        beanProjectAdd.setType(request.getParameter("type"));
        beanProjectAdd.setGroup(request.getParameter("group"));
        beanProjectAdd.setStatus(request.getParameter("status"));
        beanProjectAdd.setCustomer(request.getParameter("customer"));   //(CommonUtil.decodeParameter(request, "customer", "UTF-8"));
        beanProjectAdd.setCate(request.getParameter("cate"));

        ProjectBO boProject = new ProjectBO();
        try {
            int nResult = boProject.saveProject(beanProjectAdd);
            // Add failed
            if (nResult == -1) {
                request.setAttribute("beanProjectAdd", beanProjectAdd);
                request.setAttribute(DB.ATT_ERROR_MESSAGE,"Create project failed, may be duplicated project code");
            }
            // Successful => clear infor
            else {
                request.setAttribute("beanProjectAdd", null);
            }
        }
        catch (Exception e) {
            request.setAttribute("beanProjectAdd", beanProjectAdd);
            request.setAttribute(DB.ATT_ERROR_MESSAGE,"Create project failed, exception:" + e.getMessage());
        }
    }

    /**
     * Get project dashboard
     * @author  Nguyen Thai Son
     * @version November 09, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void getProjectDashboard(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        UserInfoBean beanUserInfo = checkSessionVariables(request, response);
        beanUserInfo.setTypeOfView(request.getParameter("hidActionDetail"));
        //"ViewDashboard"
        request.getSession().setAttribute("beanUserInfo", beanUserInfo);

        ProjectDashboardBean beanProjectDashboard = new ProjectDashboardBean();

        HttpSession session = request.getSession();
        String strGroup = Constants.GROUP_ALL;
        String strStatus = DATA.PROJECT_VALUE_STATUS_ONGOING; //On-going
        String strCategory = "-1";  // All
        int nOrderBy = 0; //Order by status
        String strType = DATA.PROJECT_EXTERNAL;

        //user group
        if (request.getParameter("cboGroup") != null) {
            strGroup = request.getParameter("cboGroup");
            session.setAttribute("cboGroup", strGroup);
        } else if (request.getParameter("hidGroup") != null)
            strGroup = request.getParameter("hidGroup");
        else if (session.getAttribute("cboGroup") != null)
            strGroup = (String) session.getAttribute("cboGroup");
        else {
            strGroup = beanUserInfo.getGroupName();
            //set default view his group only.
            String strRole = beanUserInfo.getSRole();
            //formatted as "XXXXXXXXXX"
            if (strRole.charAt(4) == '1') //PQA
                strGroup = Constants.GROUP_ALL; //PQA can view all groups
        }

        //ThaiLH
        //Project type (internal, external,...)
        if (request.getParameter("cboType") != null) {
            strType = request.getParameter("cboType");
            session.setAttribute("cboType", strType);
        } else if (session.getAttribute("cboType") != null)
            strType = (String) session.getAttribute("cboType");

        //project status
        if (request.getParameter("cboStatus") != null) {
            strStatus = request.getParameter("cboStatus");
            session.setAttribute("cboStatus", strStatus);
        } else if (session.getAttribute("cboStatus") != null)
            strStatus = (String) session.getAttribute("cboStatus");
        //added by MinhPT
        //09-Dec-03
        //category
        if (request.getParameter("cboCategory") != null) {
            strCategory = request.getParameter("cboCategory");
            session.setAttribute("cboCategory", strCategory);
        } 
        else if (session.getAttribute("cboCategory") != null){
            strCategory = (String) session.getAttribute("cboCategory");
        }
        
        //sort by
        try {
            if (request.getParameter("cboOrder") != null) {
                nOrderBy = Integer.parseInt(request.getParameter("cboOrder"));
                session.setAttribute("cboOrder", Integer.toString(nOrderBy));
            } else if (session.getAttribute("cboOrder") != null)
                nOrderBy =
                    Integer.parseInt((String) session.getAttribute("cboOrder"));
        } catch (NumberFormatException ex) {
            nOrderBy = 1;
            logger.debug(
                "NumberFormatException occurs in ProjectManagementServlet.getProjectDashboard(): "
                    + ex.toString());
        }

        ProjectDashboardBO boProjectDB = new ProjectDashboardBO();
        // External at project level AND project leaders can show only his assigned projects
        if (DATA.ROLE_EXTERNAL_PL.equals(beanUserInfo.getSRole()) ||
            DATA.ROLE_PROJECTLEADER.equals(beanUserInfo.getSRole()))
        {
            beanProjectDashboard =
                boProjectDB.getProjectDashboard(
                    strGroup,
                    strType,
                    strStatus,
                    strCategory,
                    beanUserInfo.getDeveloperId(),
                    nOrderBy);
        }
        else {
            beanProjectDashboard =
                boProjectDB.getProjectDashboard(
                    strGroup,
                    strType,
                    strStatus,
                    strCategory,
                    null,
                    nOrderBy);
        }
        beanProjectDashboard.setCategory(strCategory);
        /////////////////////////////////////////////////////////////
        request.setAttribute("beanProjectDashboard", beanProjectDashboard);
    }

    /**
     * Create an updating form to update a project
     * @author  Nguyen Thai Son
     * @version November 11, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void createProjectUpdateForm(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        checkSessionVariables(request, response);

        String strProjectID = request.getParameter("hidProjectID");

        ProjectBO boProject = new ProjectBO();
        ProjectUpdateBean beanProjectUpdate = new ProjectUpdateBean();
        beanProjectUpdate = boProject.createProjectUpdateForm(strProjectID);

        ///////////////////////////////////////////////////////////////////////////////
        request.setAttribute("beanProjectUpdate", beanProjectUpdate);
    }

    /**
     * Save a project into database
     * @author  Nguyen Thai Son
     * @version November 11, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void saveUpdateProject(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        checkSessionVariables(request, response);

        //STEP 2 - Get data from client
        String strProjectID = request.getParameter("id");
        ProjectBO boProject = new ProjectBO();
        ProjectUpdateBean beanProjectUpdate = boProject.createProjectUpdateForm(strProjectID);

        beanProjectUpdate.setId(request.getParameter("id"));
        beanProjectUpdate.setCode(request.getParameter("code"));    //(CommonUtil.decodeParameter(request, "code", "UTF-8"));
        beanProjectUpdate.setName(request.getParameter("name"));    //(CommonUtil.decodeParameter(request, "name", "UTF-8"));
        beanProjectUpdate.setLeader(request.getParameter("leader"));    //(CommonUtil.decodeParameter(request, "leader", "UTF-8"));

        beanProjectUpdate.setStartDate(request.getParameter("startDate"));
        beanProjectUpdate.setPerComplete(request.getParameter("perComplete"));

        beanProjectUpdate.setDescription(request.getParameter("description"));  //(CommonUtil.decodeParameter(request, "description", "UTF-8"));

        beanProjectUpdate.setPlanFinishDate(
            request.getParameter("planFinishDate"));
        beanProjectUpdate.setBaseFinishDate(
            request.getParameter("baseFinishDate"));
        beanProjectUpdate.setActualFinishDate(
            request.getParameter("actualFinishDate"));

        beanProjectUpdate.setPlanEffort(request.getParameter("planEffort"));
        beanProjectUpdate.setBaseEffort(request.getParameter("baseEffort"));
        beanProjectUpdate.setActualEffort(request.getParameter("actualEffort"));

        beanProjectUpdate.setType(request.getParameter("type"));
        beanProjectUpdate.setGroup(request.getParameter("group"));
        beanProjectUpdate.setStatus(request.getParameter("status"));
        beanProjectUpdate.setCate(request.getParameter("cate"));
        beanProjectUpdate.setCustomer(request.getParameter("customer"));    //(CommonUtil.decodeParameter(request, "customer", "UTF-8"));

//        ProjectBO boProject = new ProjectBO();
        try {
            int nResult = boProject.updateProject(beanProjectUpdate);
            // Add failed, stay at current update page
            if (nResult == -1) {
                request.setAttribute(DB.ATT_ERROR_MESSAGE,
                        "Update project failed(may be duplicated project code)");
                request.setAttribute("beanProjectUpdate", beanProjectUpdate);
                callPage(response, DB.JSP_PROJECT_UPDATE, request);
            }
            // Success, return detail page
            else {
                getProjectDetail(request, response);
                callPage(response, DB.JSP_PROJECT_DETAIL, request);
            }
        }
        catch (Exception e) {
            request.setAttribute(DB.ATT_ERROR_MESSAGE,"Update project failed, exception:" + e.getMessage());
            request.setAttribute("beanProjectUpdate", beanProjectUpdate);
            callPage(response, DB.JSP_PROJECT_UPDATE, request);
        }
    }

    /**
     * Get a list of project's milestones
     * @author  Nguyen Thai Son
     * @version November 11, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void getMilestoneList(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        checkSessionVariables(request, response);

        //STEP 2 - get project ID from client
        String strProjectID = request.getParameter("hidProjectID");
        if (strProjectID != null)
            request.getSession().setAttribute("hidProjectID", strProjectID);
        else
            strProjectID =
                (String) request.getSession().getAttribute("hidProjectID");
        int nProjectID = 0;
        try {
            nProjectID = Integer.parseInt(strProjectID);
        } catch (NumberFormatException ex) {
            nProjectID = 0;
            logger.debug(
                "NumberFormatException occurs in ProjectManagementServlet.getMilestoneList(): "
                    + ex.getMessage());
        }

        //STEP 3 - Using BO to get milestone list and project information
        MilestoneBO boMilestone = new MilestoneBO();
        MilestoneListBean beanMilestoneList = new MilestoneListBean();
        beanMilestoneList = boMilestone.getMilestoneList(nProjectID);

        //STEP 4 - Put result into client
        request.setAttribute("beanMilestoneList", beanMilestoneList);
    }

    /**
     * Create a new form to add milestone
     * @author  Nguyen Thai Son
     * @version November 11, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void createAddMilestoneForm(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        checkSessionVariables(request, response);

        MilestoneAddBean beanMilestoneAdd = new MilestoneAddBean();
        beanMilestoneAdd.setName("");
        beanMilestoneAdd.setComplete(0);
        beanMilestoneAdd.setBaseFinishDate("");
        beanMilestoneAdd.setPlanFinishDate("");
        beanMilestoneAdd.setActualFinishDate("");
        beanMilestoneAdd.setDescription("");

        request.setAttribute("beanMilestoneAdd", beanMilestoneAdd);
    }

    /**
     * Delete the selected milestone(s)
     * @author  Nguyen Thai Son
     * @version November 12, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void deleteMilestone(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        checkSessionVariables(request, response);

        String[] arrID = request.getParameterValues("selected");
        MilestoneBO boMilestone = new MilestoneBO();
        boMilestone.deleteMilestone(arrID);
    }

    /**
     * Create a new form to update milestone
     * @author  Nguyen Thai Son
     * @version November 11, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void createUpdateMilestoneForm(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        checkSessionVariables(request, response);

        String strMilestoneID = request.getParameter("hidMilestoneID");

        MilestoneUpdateBean beanMilestoneUpdate = new MilestoneUpdateBean();
        MilestoneBO boMilestone = new MilestoneBO();

        beanMilestoneUpdate = boMilestone.getMilestoneInfo(strMilestoneID);

        //Project information
        beanMilestoneUpdate.setProjectID(request.getParameter("hidProjectID"));
        beanMilestoneUpdate.setProjectCode(
            request.getParameter("hidProjectCode"));
        beanMilestoneUpdate.setProjectName(
            request.getParameter("hidProjectName"));
        beanMilestoneUpdate.setProjectStartDate(
            request.getParameter("hidPrStartDate"));
        beanMilestoneUpdate.setProjectBaseFinishDate(
            request.getParameter("hidPrBaseFinishDate"));
        beanMilestoneUpdate.setProjectPlanFinishDate(
            request.getParameter("hidPrPlanFinishDate"));
        beanMilestoneUpdate.setProjectActualFinishDate(
            request.getParameter("hidPrActualFinishDate"));

        //////////////////////////////////////////////////////////////////////////////////////////////////////
        request.setAttribute("beanMilestoneUpdate", beanMilestoneUpdate);
    }

    /**
     * Save a new milestone into database
     * @author  Nguyen Thai Son
     * @version November 11, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void saveNewMilestone(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        checkSessionVariables(request, response);

        MilestoneAddBean beanMilestoneAdd = new MilestoneAddBean();
        beanMilestoneAdd.setName(request.getParameter("txtName"));  //(CommonUtil.decodeParameter(request, "txtName", "UTF-8"));
        beanMilestoneAdd.setComplete(
            Integer.parseInt(request.getParameter("cmbComplete")));
        beanMilestoneAdd.setBaseFinishDate(
            request.getParameter("txtBaseFinishDate"));
        beanMilestoneAdd.setPlanFinishDate(
            request.getParameter("txtPlanFinishDate"));
        beanMilestoneAdd.setActualFinishDate(
            request.getParameter("txtActualFinishDate"));
        beanMilestoneAdd.setDescription(request.getParameter("txtDescription"));    //(CommonUtil.decodeParameter(request, "txtDescription", "UTF-8"));

        //get Project Info
        beanMilestoneAdd.setProjectID(request.getParameter("hidProjectID"));
        beanMilestoneAdd.setProjectCode(request.getParameter("hidProjectCode"));    //(CommonUtil.decodeParameter(request, "hidProjectCode", "UTF-8"));
        beanMilestoneAdd.setProjectName(request.getParameter("hidProjectName"));    //(CommonUtil.decodeParameter(request, "hidProjectName", "UTF-8"));
        beanMilestoneAdd.setProjectStartDate(
            request.getParameter("hidPrStartDate"));
        beanMilestoneAdd.setProjectBaseFinishDate(
            request.getParameter("hidPrBaseFinishDate"));
        beanMilestoneAdd.setProjectPlanFinishDate(
            request.getParameter("hidPrPlanFinishDate"));
        beanMilestoneAdd.setProjectActualFinishDate(
            request.getParameter("hidPrActualFinishDate"));

        MilestoneBO boMilestone = new MilestoneBO();
        boMilestone.addMilestone(beanMilestoneAdd);
    }

    /**
     * Save the current milestone into database
     * @author  Nguyen Thai Son
     * @version November 12, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void saveUpdateMilestone(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        checkSessionVariables(request, response);

        MilestoneUpdateBean beanMilestoneUpdate = new MilestoneUpdateBean();
        beanMilestoneUpdate.setID(request.getParameter("MilestoneID"));
        beanMilestoneUpdate.setName(request.getParameter("txtName"));   //(CommonUtil.decodeParameter(request, "txtName", "UTF-8"));
        beanMilestoneUpdate.setComplete(
            Integer.parseInt(request.getParameter("cmbComplete")));
        beanMilestoneUpdate.setBaseFinishDate(
            request.getParameter("txtBaseFinishDate"));
        beanMilestoneUpdate.setPlanFinishDate(
            request.getParameter("txtPlanFinishDate"));
        beanMilestoneUpdate.setActualFinishDate(
            request.getParameter("txtActualFinishDate"));
        beanMilestoneUpdate.setDescription(request.getParameter("txtDescription")); //(CommonUtil.decodeParameter(request, "txtDescription", "UTF-8"));

        //get Project Info
        beanMilestoneUpdate.setProjectID(request.getParameter("hidProjectID"));
        beanMilestoneUpdate.setProjectCode(request.getParameter("hidProjectCode")); //(CommonUtil.decodeParameter(request, "hidProjectCode", "UTF-8"));
        beanMilestoneUpdate.setProjectName(request.getParameter("hidProjectName")); //(CommonUtil.decodeParameter(request, "hidProjectName", "UTF-8"));
        beanMilestoneUpdate.setProjectStartDate(
            request.getParameter("hidPrStartDate"));
        beanMilestoneUpdate.setProjectBaseFinishDate(
            request.getParameter("hidPrBaseFinishDate"));
        beanMilestoneUpdate.setProjectPlanFinishDate(
            request.getParameter("hidPrPlanFinishDate"));
        beanMilestoneUpdate.setProjectActualFinishDate(
            request.getParameter("hidPrActualFinishDate"));

        MilestoneBO boMilestone = new MilestoneBO();
        boMilestone.updateMilestone(beanMilestoneUpdate);
    }

    /**
     * Get a list of assigned users in project
     * @author  Nguyen Thai Son
     * @version November 12, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void getAssignmentList(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        checkSessionVariables(request, response);

        //STEP 2 - get project ID from client
        String strProjectID = request.getParameter("hidProjectID");
        if (strProjectID != null)
            request.getSession().setAttribute("hidProjectID", strProjectID);
        else
            strProjectID =
                (String) request.getSession().getAttribute("hidProjectID");
        int nProjectID = 0;
        try {
            nProjectID = Integer.parseInt(strProjectID);
        } catch (NumberFormatException ex) {
            nProjectID = 0;
            logger.debug(
                "NumberFormatException occurs in ProjectManagementServlet.getAssignmentList(): "
                    + ex.getMessage());
        }

        //STEP 3 - Using BO to get milestone list and project information
        AssignmentBO boAssignment = new AssignmentBO();
        AssignmentListBean beanAssignmentList = new AssignmentListBean();
        beanAssignmentList = boAssignment.getAssignmentList(nProjectID);
        ComboBO boCombo = new ComboBO();
        beanAssignmentList.setResponseList(boCombo.getResponseList());
        
        //STEP 4 - Put result into client
        request.setAttribute("beanAssignmentList", beanAssignmentList);
    }

    /**
     * Save all assigned users in project
     * @author  Nguyen Thai Son
     * @version November 12, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void saveUpdateAssignment(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        checkSessionVariables(request, response);

        String[] arrBeginDate = request.getParameterValues("Begin");
        String[] arrEndDate = request.getParameterValues("End");
        String[] arrType = request.getParameterValues("type");
        String[] arrUsage = request.getParameterValues("Usage");
        String[] arrPosition = request.getParameterValues("cboPosition");

        AssignmentListBean beanAssignmentList = new AssignmentListBean();
        beanAssignmentList.setProjectID(request.getParameter("hidProjectID"));
        beanAssignmentList.setArrBegin(arrBeginDate);
        beanAssignmentList.setArrEnd(arrEndDate);
        beanAssignmentList.setArrType(arrType);
        beanAssignmentList.setArrUsage(arrUsage);
        beanAssignmentList.setArrRes(arrPosition);

        AssignmentBO boAssignment = new AssignmentBO();
        boAssignment.updateAssignment(beanAssignmentList);
    }

    /**
     * Create a new form to add user
     * @author  Nguyen Thai Son
     * @version November 12, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void createAssignmentAddForm(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //Check session to validate user information
        checkSessionVariables(request, response);
        HttpSession session = request.getSession(); 
        //ThaiLH
        //getOtherAssignmentList(request, response);

        AssignmentAddBean beanAssignmentAdd = new AssignmentAddBean();
        DeveloperBO boDeveloper = new DeveloperBO();
        beanAssignmentAdd = boDeveloper.getAssignmentList();
        if (request.getParameter("lstGroup") != null) {
            beanAssignmentAdd.setSelectedGroup(request.getParameter("lstGroup"));
            session.setAttribute("lstGroup", request.getParameter("lstGroup"));
        }
        else if (session.getAttribute("lstGroup") != null) {
            beanAssignmentAdd.setSelectedGroup((String) session.getAttribute("lstGroup"));
        }
        ComboBO boCombo = new ComboBO();
        beanAssignmentAdd.setResponseList(boCombo.getResponseList());
        
        request.setAttribute("beanAssignmentAdd", beanAssignmentAdd);
    }

    /**
     * Delete assigned user(s)
     * @author  Nguyen Thai Son
     * @version November 12, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void deleteAssignment(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //Check session to validate user information
        checkSessionVariables(request, response);

        String[] arrID = request.getParameterValues("dev_id");
        AssignmentBO boAssignment = new AssignmentBO();
        boAssignment.deleteAssignment(arrID);
    }

    /**
     * Save an assignment
     * @author  Nguyen Thai Son
     * @version November 12, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void saveNewAssignment(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //Check session to validate user information
        checkSessionVariables(request, response);

        AssignmentAddBean beanAssignmentAdd = new AssignmentAddBean();

        String strDevIDs = request.getParameter("hidSelID");

        beanAssignmentAdd.setSelectDevID(strDevIDs);
        beanAssignmentAdd.setProjectID(request.getParameter("hidProjectID"));
        beanAssignmentAdd.setProjectStart(
            request.getParameter("hidProjectStart"));
        beanAssignmentAdd.setProjectEnd(request.getParameter("hidProjectEnd"));

        AssignmentBO boAssignment = new AssignmentBO();
        boAssignment.saveAssignment(beanAssignmentAdd);
        if (request.getParameter("lstGroup") != null) {
            request.getSession().setAttribute("lstGroup", request.getParameter("lstGroup"));
        }
    }

    /**
     * Create a form to close project
     * @author  Nguyen Thai Son
     * @version November 13, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void createProjectCloseForm(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        checkSessionVariables(request, response);

        //STEP 2 - get project ID from client
        String strProjectID = request.getParameter("hidProjectID");
        if (strProjectID != null)
            request.getSession().setAttribute("hidProjectID", strProjectID);
        else
            strProjectID =
                (String) request.getSession().getAttribute("hidProjectID");

        ProjectCloseBean beanProjectClose = new ProjectCloseBean();

        beanProjectClose.setProjectID(strProjectID);
        beanProjectClose.setStatus(request.getParameter("hidProjectStatus"));
        beanProjectClose.setDescription(request.getParameter("hidDescription"));    //(CommonUtil.decodeParameter(request, "hidDescription", "UTF-8"));
        StringMatrix statusList = new StringMatrix(2, 2);
        statusList.setCell(0, 0, "0");
        statusList.setCell(0, 1, DATA.PROJECT_STATUS_ONGOING);
        statusList.setCell(1, 0, "1");
        statusList.setCell(1, 1, DATA.PROJECT_STATUS_CLOSED);
        beanProjectClose.setStatusList(statusList);

        ///////////////////////////////////////////////////////////////////////////////
        request.setAttribute("beanProjectClose", beanProjectClose);
    }

    /**
     * Close project
     * @author  Nguyen Thai Son
     * @version November 13, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void closeProject(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //Check session to validate user information
        checkSessionVariables(request, response);

        ProjectCloseBean beanProjectClose = new ProjectCloseBean();
        beanProjectClose.setProjectID(request.getParameter("hidProjectID"));
        beanProjectClose.setActualFinishDate(
            request.getParameter("actualFinish"));
        beanProjectClose.setDescription(request.getParameter("desc"));  //(CommonUtil.decodeParameter(request, "desc", "UTF-8"));
        beanProjectClose.setStatus(request.getParameter("status"));

        ProjectBO boProject = new ProjectBO();
        boProject.closeProject(beanProjectClose);
    }

    /**
     * Get a list of other assignment
     * @author  Nguyen Thai Son
     * @version November 14, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void getOtherAssignmentList(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //STEP 1 - check session to validate user information
        UserInfoBean beanUserInfo = checkSessionVariables(request, response);

        AssignmentOtherBean beanAssignmentOther = new AssignmentOtherBean();

        HttpSession session = request.getSession();
        //From Date
        String strFrom = request.getParameter("from");
        if (strFrom != null)
            session.setAttribute("from", strFrom);
        else
            strFrom = (String) session.getAttribute("from");

        //To Date
        String strTo = request.getParameter("to");
        if (strTo != null)
            session.setAttribute("to", strTo);
        else
            strTo = (String) session.getAttribute("to");

        //Group name
        String strGroup = "All";
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
            String strRole = beanUserInfo.getSRole();
            //formatted as "XXXXXXXXXX"
            if (strRole.charAt(4) == '1') //PQA
                strGroup = "All"; //PQA can view all groups
        }

        beanAssignmentOther.setFrom(strFrom);
        beanAssignmentOther.setTo(strTo);
        beanAssignmentOther.setSelectGroup(strGroup);

        if (beanUserInfo.getRole().equals("3")) {
            beanAssignmentOther.setSelectGroup(beanUserInfo.getGroupName());
        }

        AssignmentBO boAssignment = new AssignmentBO();
        beanAssignmentOther =
            boAssignment.getOtherAssignment(beanAssignmentOther);

        request.setAttribute("beanAssignmentOther", beanAssignmentOther);
    }

    /**
     * Get milestones in all projects
     * @author  Nguyen Thai Son
     * @version November 14, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void getAllMilestoneList(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //Check session to validate user information
        UserInfoBean beanUserInfo = checkSessionVariables(request, response);
        beanUserInfo.setTypeOfView(request.getParameter("hidActionDetail"));
        MilestoneAllBean beanMilestoneAll = new MilestoneAllBean();

        beanMilestoneAll.setDateFrom(request.getParameter("txtDateFrom"));
        beanMilestoneAll.setDateTo(request.getParameter("txtDateTo"));
        beanMilestoneAll.setStatus(request.getParameter("cboStatus"));

        MilestoneBO boMilestone = new MilestoneBO();
        beanMilestoneAll =
            boMilestone.getAllMilestone(beanMilestoneAll, beanUserInfo);

        request.setAttribute("beanMilestoneAll", beanMilestoneAll);
    }

    /**
     * Create a new form
     * @author  Nguyen Thai Son
     * @version November 14, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void createOtherAssignmentAddForm(
        HttpServletRequest request,
        HttpServletResponse response,
        String strMessage)
        throws Exception {
        //Check session to validate user information
        checkSessionVariables(request, response);

        AssignmentAddBean beanAssignmentAdd = new AssignmentAddBean();
        DeveloperBO boDeveloper = new DeveloperBO();
        beanAssignmentAdd = boDeveloper.getAssignmentList();
        beanAssignmentAdd.setMessage(strMessage);

        request.setAttribute("beanAssignmentAdd", beanAssignmentAdd);
    }

    /**
     * Save other assignment
     * @author  Nguyen Thai Son
     * @version November 14, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private int saveOtherAssignment(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        int nResult = 0;
        //Check session to validate user information
        checkSessionVariables(request, response);

        AssignmentAddBean beanAssignmentAdd = new AssignmentAddBean();
        beanAssignmentAdd.setSelectDevID(request.getParameter("Developer_ID"));
        beanAssignmentAdd.setFrom(request.getParameter("From_date"));
        beanAssignmentAdd.setTo(request.getParameter("To_date"));
        beanAssignmentAdd.setDesc(request.getParameter("Description")); //(CommonUtil.decodeParameter(request, "Description", "UTF-8"));
        beanAssignmentAdd.setType(request.getParameter("Type"));
        beanAssignmentAdd.setUsage(request.getParameter("Usage"));

        AssignmentBO boAssignment = new AssignmentBO();
        //ThaiLH
        if (boAssignment
            .checkAssign(
                beanAssignmentAdd.getSelectDevID(),
                beanAssignmentAdd.getFrom(),
                beanAssignmentAdd.getTo())
            == 0) {
            boAssignment.saveOtherAssignment(beanAssignmentAdd);
        } else {
            nResult = -1;
        }
        return nResult;
    }

    /**
     * Delete other assignment
     * @author  Nguyen Thai Son
     * @version November 14, 2002
     * @param   request javax.servlet.HttpServletRequest: the request object.
     * @param   response javax.servlet.HttpServletResponse: the response object.
     * @exception   Exception    If an exception occurred.
     */
    private void deleteOtherAssignment(
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {
        //Check session to validate user information
        checkSessionVariables(request, response);

        String[] arrID = request.getParameterValues("dev_id");
        AssignmentBO boAssignment = new AssignmentBO();
        boAssignment.deleteOtherAssignment(arrID);
    }
    
    private void reopenProject(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        checkSessionVariables(request, response);
        String strProjectID = request.getParameter("hidProjectID");
        if (strProjectID != null) {
            request.getSession().setAttribute("hidProjectID", strProjectID);
        }
        else {
            strProjectID = (String) request.getSession().getAttribute("hidProjectID");
        }

        ProjectBO boProject = new ProjectBO();
        boProject.reopenProject(strProjectID);
    }
}