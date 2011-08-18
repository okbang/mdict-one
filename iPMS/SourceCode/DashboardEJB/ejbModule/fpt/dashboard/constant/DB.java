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
 
 package fpt.dashboard.constant;
public class DB
{
	public final static boolean DEBUG = true;

	//Located servlets////////////////////////////////////////////////////
	public final static String PROJECT_MANAGEMENT_SERVLET = "ProjectManagementServlet";
	public final static String STAFF_MANAGEMENT_SERVLET = "StaffManagementServlet";
	public final static String RESOURCE_MANAGEMENT_SERVLET = "ResourceManagementServlet";

	//Action Names////////////////////////////////////////////////////
	public final static String PROJECT_MANAGEMENT_ACTION = "PM";
	public final static String STAFF_MANAGEMENT_ACTION = "SM";
	public final static String RESOURCE_MANAGEMENT_ACTION = "RM";

	public final static String LOGIN_ACTION = "DashboardLogin";
	public final static String DASHBOARD_HOMEPAGE_ACTION = "DashboardHomepage";
    public final static String GET_RSA_ACTION = "GetRsaAction";


	//Sub-action Names////////////////////////////////////////////////////
	//Homepage form
	public final static String VIEW_PROJECT_LIST = "ViewProjectList";
	public final static String VIEW_DASHBOARD = "ViewDashboard";
	public final static String VIEW_STAFF_LIST = "ViewStaffList";
	public final static String VIEW_RESOURCE = "ViewResource";
	public final static String VIEW_OTHER_ASSIGNMENT = "ListOtherAssignment";
	public final static String VIEW_ALL_MILESTONE = "ListAllMilestone";
	
	//Project List form
	public final static String PROJECT_ADD = "AddProject";
	public final static String PROJECT_DELETE = "DeleteProject";
	public final static String PROJECT_DETAIL = "ViewProjectDetail";
	public final static String PROJECT_SAVE_NEW = "SaveNewProject";
	public final static String PROJECT_SAVE_UPDATE = "SaveUpdateProject";
	
	//Project Detail form
	public final static String UPDATE_MAJOR_INFO = "UpdateMajorInfo";
	public final static String MILESTONE_LIST = "ListMilestone";
	public final static String ASSIGNMENT_LIST = "ListAssignment";
	public final static String PROJECT_CLOSE = "CloseProject";
    public final static String PROJECT_REOPEN = "ReopenProject";
	
	//Milestone List form
	public final static String MILESTONE_ADD = "AddMilestone";
	public final static String MILESTONE_UPDATE = "UpdateMilestone";
	public final static String MILESTONE_DELETE = "DeleteMilestone";
	public final static String MILESTONE_SAVE_NEW = "SaveNewMilestone";
	public final static String MILESTONE_SAVE_UPDATE = "SaveUpdateMilestone";

	//Assignment List form
	public final static String ASSIGNMENT_SAVE_UPDATE = "SaveAssignment";
	public final static String ASSIGNMENT_ADD = "AddAssignment";
	public final static String ASSIGNMENT_DELETE = "DeleteAssignment";
	public final static String ASSIGNMENT_SAVE_NEW = "SaveNewAssignment";
	
	//Project Close form
	public final static String PROJECT_SAVE_CLOSE = "SaveCloseProject";
	
	//Resource Summary form
	public final static String RESOURCE_WEEKLY = "ViewWeeklyResource";
    public final static String RESOURCE_DAILY = "ViewDailyResource";
    public final static String RESOURCE_PROJECT = "ViewProjectResource";

	//Weekly Resource form
	public final static String VIEW_ASSIGNMENT_DETAIL = "ViewAssignmentDetail";
	
	//Staff List form
	public final static String STAFF_ADD = "AddStaff";
	public final static String STAFF_DELETE = "DeleteStaff";
	public final static String STAFF_UPDATE = "UpdateStaff";
	public final static String STAFF_SAVE_NEW = "SaveNewStaff";
	public final static String STAFF_SAVE_UPDATE = "SaveUpdateStaff";
	
	//Other Assignment form
	public final static String OTHER_ASSIGNMENT_ADD = "AddOtherAssignment";
	public final static String OTHER_ASSIGNMENT_DELETE = "DeleteOtherAssignment";
	public final static String OTHER_ASSIGNMENT_SAVE_NEW = "SaveNewOtherAssignment";
	
	
	//JSP FILE NAMES////////////////////////////////////////////////////
	public final static String JSP_ERROR = "error.jsp";
	public final static String JSP_LOGIN = "Login.jsp";
	public final static String JSP_HOMEPAGE = "Dashboard.jsp";
	
	public final static String JSP_PROJECT_LIST = "ProjectList.jsp";
	public final static String JSP_PROJECT_ADD = "ProjectAdd.jsp";
	public final static String JSP_PROJECT_DETAIL = "ProjectDetail.jsp";
	public final static String JSP_PROJECT_DASHBOARD = "ProjectDashboard.jsp";
	
	public final static String JSP_PROJECT_UPDATE = "ProjectUpdate.jsp";
	public final static String JSP_PROJECT_CLOSE = "ProjectClose.jsp";
	
	public final static String JSP_MILESTONE_LIST = "MilestoneList.jsp";
	public final static String JSP_MILESTONE_ADD = "MilestoneAdd.jsp";
	public final static String JSP_MILESTONE_UPDATE = "MilestoneUpdate.jsp";
	
	public final static String JSP_ASSIGNMENT_LIST = "AssignmentList.jsp";
	public final static String JSP_ASSIGNMENT_ADD = "AssignmentAdd.jsp";
	public final static String JSP_ASSIGNMENT_DETAIL = "AssignmentDetail.jsp";
	
	public final static String JSP_RESOURCE_WEEK = "ResourceWeek.jsp";
    public final static String JSP_RESOURCE_DAY = "ResourceDay.jsp";
    public final static String JSP_RESOURCE_PROJECT = "ResourceProject.jsp";
	public final static String JSP_RESOURCE_SUMMARY = "ResourceSummary.jsp";
	
	public final static String JSP_STAFF_LIST = "StaffList.jsp";
	public final static String JSP_STAFF_ADD = "StaffAdd.jsp";
	public final static String JSP_STAFF_UPDATE = "StaffUpdate.jsp";
	
	public final static String JSP_ASSIGNMENT_OTHER = "AssignmentOther.jsp";
	public final static String JSP_MILESTONE_ALL = "MilestoneAll.jsp";
	public final static String JSP_ASSIGNMENT_OTHER_ADD = "AssignmentOtherAdd.jsp";

	//Client Messages////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////////
	public static final String DATE_FORMAT = "dd/MM/yy";

	//////////////////////////////////////////////////////////////////////////////
    public final static int ASSIGNMENT_TYPE_ONSITE = 1;
    public final static int ASSIGNMENT_TYPE_ALLOCATED = 2;
    public final static int ASSIGNMENT_TYPE_TENTATIVE = 3;
    public final static int ASSIGNMENT_TYPE_TRAINNING = 4;
    public final static int ASSIGNMENT_TYPE_OFF = 5;
    
    public final static int DEVELOPER_SATUS_STAFF = 1;
    public final static int DEVELOPER_SATUS_COLLABORATOR = 2;
    public final static int DEVELOPER_SATUS_EXTERNAL = 3;
    public final static int DEVELOPER_SATUS_OFF = 4;
    
    public final static String DEVELOPER_SATUS_NAME_STAFF = "Permanent";
    public final static String DEVELOPER_SATUS_NAME_COLLABORATOR = "Temporary";
    public final static String DEVELOPER_SATUS_NAME_EXTERNAL = "External viewer";
    public final static String DEVELOPER_SATUS_NAME_OFF = "Quit";
    public final static String DEVELOPER_SATUS_NAME_NA = "N/A";
    
    public final static int WORKUNIT_TYPE_GROUP = 1;
    public final static int WORKUNIT_FSOFT = 132;

    public final static int PAGE_SIZE_STAFF = 50;
    
    // Request attribute for error message
    public final static String ATT_ERROR_MESSAGE = "errorMessage";
}