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
 
 package com.fms1.common;
import java.util.Vector;
import javax.servlet.http.*;

import java.io.PrintWriter;
import java.sql.Date;
import com.fms1.common.group.SupportGroupCaller;
import com.fms1.html.DevCbo;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.GroupInfo;
import com.fms1.tools.*;
import com.fms1.web.Constants;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.Language;
import com.fms1.web.Parameters;
import com.fms1.web.StringConstants;
/**
 * Work unit pages.
 * @author NgaHT
 */
public final class WorkUnitCaller implements Constants, StringConstants {
	public static String getRightGroupID(HttpServletRequest request, long workUnitID) {
		HttpSession session = request.getSession();
		Vector orgList = (Vector) session.getAttribute("orgList");
		Vector groupList = (Vector) session.getAttribute("groupList");
		Vector projectList = (Vector) session.getAttribute("projectList");
		Vector [] rightList ={projectList,groupList,orgList};
		return getRightGroupID(rightList,workUnitID);
	}
	/**
	 * input vector
	 * [0] proj rights
	 * [1] group rights
	 * [2] org rights
	 */
	public static String getRightGroupID(Vector [] rightList, long workUnitID) {
		int level = 0;
		String rightGroupID = null;
		WorkUnitInfo wuInfo = WorkUnit.getWorkUnitInfo(workUnitID);
		Vector orgList = rightList[2];
		Vector groupList = rightList[1];
		Vector projectList = rightList[0];
		if (workUnitID == -1) { //admin virtual Work unit
			level = 3;
		}
		else {
			level = wuInfo.type;
		}
		switch (level) {
			case 0 :
				for (int i = 0; i < orgList.size(); i++) {
					RolesInfo roleInfo = (RolesInfo) orgList.elementAt(i);
					if (workUnitID == roleInfo.workUnitID) {
						rightGroupID = roleInfo.rightGroupID;
						break;
					}
				}
				break;
			case 1 :
				for (int i = 0; i < groupList.size(); i++) {
					RolesInfo roleInfo = (RolesInfo) groupList.elementAt(i);
					if (workUnitID == roleInfo.workUnitID) {
						rightGroupID = roleInfo.rightGroupID;
						break;
					}
				}
				break;
			case 2 :
				for (int i = 0; i < projectList.size(); i++) {
					RolesInfo roleInfo = (RolesInfo) projectList.elementAt(i);
					if (workUnitID == roleInfo.workUnitID) {
						rightGroupID = roleInfo.rightGroupID;
						break;
					}
				}
				break;
			case 3 :
				//admin pages we choose the rightgroup with the highest permission on admin home
				int maxRight = 0;
				RolesInfo roleInfo = null;
				for (int i = 0;((i < orgList.size()) && (maxRight < 3)); i++) {
					roleInfo = (RolesInfo) orgList.elementAt(i);
					final Vector pageRightList = RightForPage.getRightForPage(roleInfo.rightGroupID);
					for (int j = 0;((j < pageRightList.size()) && (maxRight < 3)); j++) {
						final RightForPageInfor rpi = (RightForPageInfor) pageRightList.elementAt(j);
						if (rpi.pageName2.trim().equals("Admin home")) {
							if (rpi.privilege > maxRight) {
								maxRight = rpi.privilege;
							}
							break;
						}
					}
				}
				for (int i = 0;((i < groupList.size()) && (maxRight < 3)); i++) {
					roleInfo = (RolesInfo) groupList.elementAt(i);
					final Vector pageRightList = RightForPage.getRightForPage(roleInfo.rightGroupID);
					for (int j = 0;((j < pageRightList.size()) && (maxRight < 3)); j++) {
						final RightForPageInfor rpi = (RightForPageInfor) pageRightList.elementAt(j);
						if (rpi.pageName2.trim().equals("Admin home")) {
							if (rpi.privilege > maxRight) {
								maxRight = rpi.privilege;
							}
							break;
						}
					}
				}
				for (int i = 0;((i < projectList.size()) && (maxRight < 3)); i++) {
					roleInfo = (RolesInfo) projectList.elementAt(i);
					final Vector pageRightList = RightForPage.getRightForPage(roleInfo.rightGroupID);
					for (int j = 0;((j < pageRightList.size()) && (maxRight < 3)); j++) {
						final RightForPageInfor rpi = (RightForPageInfor) pageRightList.elementAt(j);
						if (rpi.pageName2.trim().equals("Admin home")) {
							if (rpi.privilege > maxRight) {
								maxRight = rpi.privilege;
							}
							break;
						}
					}
				}
				if (roleInfo != null) {
					//we save using a different variable in order to avoid processing this again
					rightGroupID = roleInfo.rightGroupID;
				}
				break;
		}
		return rightGroupID;
	}
	/**
	 * Set home for work unit
	 * @param request incomming request
	 */
	public static WorkUnitInfo setWorkUnitHome(final HttpServletRequest request, long workUnitID) {
		final HttpSession session = request.getSession();
		WorkUnitInfo wuInfo = null;
		try {
			wuInfo = WorkUnit.getWorkUnitInfo(workUnitID);
			if (workUnitID == -1) { //admin virtual Work unit
				wuInfo.type = WorkUnitInfo.TYPE_ADMIN;
				wuInfo.workUnitName = "Administration";
			}
			else if (wuInfo.type == WorkUnitInfo.TYPE_PROJECT)
				session.setAttribute("projectID", Long.toString(wuInfo.tableID));
			else if (wuInfo.type == WorkUnitInfo.TYPE_GROUP)
				session.setAttribute("groupID", Long.toString(wuInfo.tableID));
			String ex = request.getParameter("ex");			
			String rightGroupID = getRightGroupID(request, workUnitID);
			if ("true".equalsIgnoreCase(ex)) rightGroupID = "External user";
			session.setAttribute("workUnitID", Long.toString(workUnitID));
			session.setAttribute("rightGroupID", rightGroupID.trim());
			session.setAttribute("defaultHome", String.valueOf(wuInfo.type));
			session.setAttribute("rightForPage", RightForPage.getRightForPage(rightGroupID));
			session.setAttribute("workUnitName", wuInfo.workUnitName);
			session.setAttribute("workUnitType", String.valueOf(wuInfo.type));
			return wuInfo;
		}
		catch (Exception e) {
			e.printStackTrace();
			return wuInfo;
		}
	}
	/**Workunit management
	* gets work unit list and assigns all session attribute relating
	**/
	public static void workUnitHomeCaller(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			//called from the page
			HttpSession session = request.getSession();
			String strWUID = request.getParameter("workUnitID");			
			if (strWUID == null) { //called from the program
				strWUID = (String) request.getAttribute("workUnitID");
				if (strWUID == null){
					strWUID = (String) session.getAttribute("workUnitID");
				}
			}
			final long workUnitID = Long.parseLong(strWUID);
			WorkUnitInfo wuInfo = setWorkUnitHome(request,workUnitID);
			if (workUnitID==Parameters.SQA_WU)
				SupportGroupCaller.SQAhomeCaller(request,response);
			else if (workUnitID==Parameters.PQA_WU)
				SupportGroupCaller.PQAhomeCaller(request,response);
			else
				WorkUnit.workUnitHome(request, response, wuInfo);
            }
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all workunits
	 */
	public static void doGetWorkUnitListAction(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();			
			Vector vt = null;
			Vector searchResult = null;
			if (request.getParameter("searchType")!=null){
				session.setAttribute("searchType", request.getParameter("searchType"));
			}
			if (request.getParameter("searchName")!=null){
				session.setAttribute("searchName", request.getParameter("searchName"));
			}

			final String searchName = (String)session.getAttribute("searchName");
			final int searchType;
			if (session.getAttribute("searchType") != null){
				searchType = Integer.parseInt((String)session.getAttribute("searchType"));
			}
			else{
				searchType = -1;
			}
			if (searchName == null || searchName.trim().equals("")) {
				if (searchType == -1) {
					searchResult = WorkUnit.getAllWU();
				}
				else {
					searchResult = WorkUnit.getWUListByType(searchType);
				}
			}
			else {
				searchResult = WorkUnit.getWUList(searchType, searchName);
			}
			session.setAttribute("workUnitVector", searchResult);
			session.setAttribute("WUgrpVector", WorkUnit.getWUByType(RIGHT_GROUP));
			session.setAttribute("WUorgVector", WorkUnit.getWUByType(RIGHT_ORGANIZATION));
			session.setAttribute("WUdevList", UserHelper.getAllUsers());
			Fms1Servlet.callPage("workUnit.jsp?iPage=1", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Get workunit
	 */
	public static void doGetWorkUnitAction(final HttpServletRequest request, final HttpServletResponse response) {
		try {			
			final HttpSession session = request.getSession();
			final long workUnitID = Long.parseLong(request.getParameter("workUnitID"));
			final WorkUnitInfo wuInfo = WorkUnit.getWorkUnitInfo(workUnitID);

			switch (wuInfo.type){
				case 0 :
					// check all groups of this organization 
					Vector groupList =  WorkUnit.getChildrenWU(wuInfo.workUnitID);
					wuInfo.setCheckDelete(groupList.size()==0);
					session.setAttribute("workUnitInfor", wuInfo); // store workUnit information
					Fms1Servlet.callPage("workUnitOrgDetail.jsp", request, response);
					break;

				case 1 :
					Vector projectList =  WorkUnit.getChildrenWU(wuInfo.workUnitID);
					if (projectList.size() > 0 || !WorkUnit.getUserBelongGroupAssignments(wuInfo.workUnitName)){
						wuInfo.setCheckDelete(false);
					}
					else{
						wuInfo.setCheckDelete(true);
					}
					session.setAttribute("workUnitInfor", wuInfo);
					GroupInfo vtGroupInfo = WorkUnit.getGroupInfo(wuInfo.tableID);
					vtGroupInfo.setParentName(WorkUnit.getWorkUnitInfo(wuInfo.parentWorkUnitID).workUnitName);
					session.setAttribute("WUgroupInfo",vtGroupInfo);
					Fms1Servlet.callPage("workUnitGroupDetail.jsp", request, response);
					break;

				case 2 :
					request.setAttribute("workUnitInfor", wuInfo);
					final ProjectInfo projectInfo = Project.getProjectInfoForWU(wuInfo.tableID);
					wuInfo.setCheckDelete(Project.checkDeleteProject(projectInfo.getProjectId()));
					request.setAttribute("WUprojectInfo", projectInfo);
					Fms1Servlet.callPage("workUnitProjectDetail.jsp", request, response);
					break;
				default:
					break;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ADD, Update, Delete WorkUnit (Organization, Group, Project) which are create by Hieunv1  - Start	

	/**
	 * Add new a Workunit which is Organization
	 * @param request
	 * @param response
	 */
	public static void doAddnewWorkUnitOrgAction(final HttpServletRequest request, final HttpServletResponse response){
		try {
			OrganizationInfo orgInfo = new OrganizationInfo();
			orgInfo.orgName = ConvertString.toStandardName(request.getParameter("txtOrgName").trim());
			if (WorkUnit.addWorkUnitOrgAction(orgInfo)) {
				final HttpSession session = request.getSession();
				UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
				Vector [] roles=Roles.getUserRoles(userLoginInfo.developerID);
				Vector orgRoles = roles[2];
				session.setAttribute("orgList", orgRoles);
				doGetWorkUnitListAction(request,response);				
			}
			else {
				request.setAttribute("OrganizationInfo",orgInfo);
				request.setAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE, "Create Organization failed");
				Fms1Servlet.callPage("workUnitAddOrg.jsp", request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Update a Organization which is a WorkUnit type 
	 * @param request
	 * @param response
	 */
	public static void doUpdateWorkUnitOrgAction(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final WorkUnitInfo wuInfo = new WorkUnitInfo();
			wuInfo.workUnitID = Long.parseLong(request.getParameter("hideWorkUnitID"));
			wuInfo.tableID = WorkUnit.getWorkUnitInfo(wuInfo.workUnitID).tableID;
			wuInfo.workUnitName = ConvertString.toStandardName(request.getParameter("txtworkUnitName").trim());			
			wuInfo.type = 0;
			wuInfo.parentWorkUnitID = 0;

			if (WorkUnit.updateWorkUnitOrg(wuInfo)) {
				doGetWorkUnitListAction(request, response);
			}
			else {
				final HttpSession session = request.getSession();
				session.setAttribute("workUnitInfor", wuInfo);
				request.setAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE, "Update Org failed");
				Fms1Servlet.callPage("workUnitOrgUpdate.jsp", request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Delete a Organization
	 * @param request
	 * @param response
	 */
	public static void doDeleteWorkUnitOrgAction(final HttpServletRequest request, final HttpServletResponse response){
		try {
			String orgName = ConvertString.toSql(request.getParameter("txtworkUnitName").trim(), ConvertString.adText);
			long lWorkUnitID = Long.parseLong(request.getParameter("hideWorkUnitID"));
			WorkUnit.deleteWorkUnitOrg(lWorkUnitID, orgName);
			doGetWorkUnitListAction(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();			
		}
	}
	/**
	 * Add new a Group which is a WorkUnit type
	 * @param request
	 * @param response
	 */
	public static void doAddnewWorkUnitGroupAction(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final WorkUnitInfo wuInfo = new WorkUnitInfo();
			wuInfo.workUnitName = ConvertString.toStandardName(request.getParameter("txtworkUnitName").trim());
			wuInfo.type = 1;
			wuInfo.parentWorkUnitID = Long.parseLong(request.getParameter("cboOrg"));
			final GroupInfo groupInfo = new GroupInfo();
			groupInfo.name = wuInfo.workUnitName;
			groupInfo.isOperation =  request.getParameter("chkOperationGroup") != null;
			if (WorkUnit.addWorkUnitGroup(wuInfo, groupInfo)){
				doGetWorkUnitListAction(request, response);
			}
			else {
				final HttpSession session = request.getSession();
				request.setAttribute("WorkUnitGroupInfo", groupInfo);
				request.setAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE, "Update Org failed");
				Fms1Servlet.callPage("workUnitAddGroup.jsp", request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Update a Group which is a WorkUnit type
	 * @param request
	 * @param response
	 */
	public static void doUpdateWorkUnitGroupAction(final HttpServletRequest request, final HttpServletResponse response){
		try {
			final WorkUnitInfo wuInfo = new WorkUnitInfo();
			wuInfo.workUnitID = Long.parseLong(request.getParameter("hideWorkUnitID"));
			wuInfo.workUnitName = ConvertString.toStandardName(request.getParameter("txtWorkUnitName").trim());
			wuInfo.type = 1;
			wuInfo.parentWorkUnitID = Long.parseLong(request.getParameter("cboOrg"));
			String preGroupName = WorkUnit.getWorkUnitInfo(wuInfo.workUnitID).workUnitName; //which is groupName before updating
			
			final GroupInfo groupInfo = new GroupInfo();
			groupInfo.groupID = WorkUnit.getWorkUnitInfo(wuInfo.workUnitID).tableID;
			groupInfo.name = wuInfo.workUnitName;
			groupInfo.isOperation = request.getParameter("chkOperationGroup") != null;

			if( WorkUnit.updateWorkUnitGroup(wuInfo, groupInfo, preGroupName)) {
				doGetWorkUnitListAction(request, response);
			}
			else {
				HttpSession session = request.getSession();
				session.setAttribute("workUnitInfor", wuInfo);
				session.setAttribute("WUgroupInfo", groupInfo);
				request.setAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE, "Update Org failed");
				Fms1Servlet.callPage("workUnitGroupUpdate.jsp", request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Delete a group which is type of WorkUnit
	 * @param request
	 * @param response
	 */
	public static void doDeleteWorkUnitGroupAction(final HttpServletRequest request, final HttpServletResponse response){
		try {
			final long workUnitId = Long.parseLong(request.getParameter("hideWorkUnitID"));
			final String workUnitName = ConvertString.toStandardName(request.getParameter("txtWorkUnitName")).trim();
			// final long groupId = WorkUnit.getTableIdByWorkUnitId(workUnitId);
			final long groupId = WorkUnit.getWorkUnitInfo(workUnitId).tableID;
			if (WorkUnit.doDeleteWorkUnitGroupAction(workUnitId, groupId, workUnitName)){
				doGetWorkUnitListAction(request, response);
			}
			else{
				Fms1Servlet.callPage("workUnitGroupDetail.jsp", request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();			
		}
	}
	/**
	 * add new a Project which is a type of WorkUnit
	 * @param request
	 * @param response
	 */
	public static void doAddnewWorkUnitProjectAction(final HttpServletRequest request, final HttpServletResponse response){
		try {
			final WorkUnitInfo wuInfo = new WorkUnitInfo();
			final ProjectInfo projectInfo = new ProjectInfo();
			projectInfo.setProjectName(ConvertString.toSql(request.getParameter("txtProjectName"), ConvertString.adText));
			projectInfo.setProjectCode(ConvertString.toSql(request.getParameter("txtProjectCode").trim(), ConvertString.adText));
			projectInfo.setGroupName(request.getParameter("cboGroup"));
			projectInfo.setCustomer(ConvertString.toSql(request.getParameter("txtCustomer"), ConvertString.adText));
			projectInfo.setSecondCustomer(ConvertString.toSql(request.getParameter("txt2NDCustomer"), ConvertString.adText));
			projectInfo.setLifecycle(request.getParameter("cboLifeCycle"));
			projectInfo.setProjectType(request.getParameter("cboType"));
			projectInfo.setProjectRank(request.getParameter("cboRank"));
			projectInfo.setPlanStartDate(CommonTools.parseSQLDate(request.getParameter("txtPlanStartDate")));
			projectInfo.setBaseFinishDate(CommonTools.parseSQLDate(request.getParameter("txtPlanEndDate")));
			projectInfo.setScopeAndObjective(request.getParameter("txtScopeObjective"));
			projectInfo.setStatus(Integer.parseInt(request.getParameter("cboStatus")));
			//check leader
			projectInfo.setLeader(ConvertString.toStandardizeString(request.getParameter("strAccountName")));
			UserInfo userInfo =  UserProfileCaller.checkUserFilter(request, projectInfo.getLeader(), null);
			if (userInfo != null){
				projectInfo.setLeader(userInfo.account);
				wuInfo.workUnitName = projectInfo.getProjectCode();
				wuInfo.type = 2;
				wuInfo.parentWorkUnitID = WorkUnit.getWorkUnitInfo(request.getParameter("cboGroup").toString()).workUnitID;

				final AssignmentInfo assInfo = new AssignmentInfo();
				assInfo.devID = userInfo.developerID;
				assInfo.devName = projectInfo.getLeader();
				assInfo.projectCode = projectInfo.getProjectCode();
				assInfo.type = 2; // Offshore
				assInfo.beginDate = projectInfo.getPlanStartDate();
				assInfo.endDate = projectInfo.getBaseFinishDate();
				assInfo.workingTime = 100;
				assInfo.responsibilityID = 3; // This is Project Manager

				if (WorkUnit.addWorkUnitProjectAction(wuInfo, projectInfo, assInfo)) {
					final HttpSession session = request.getSession();
					UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
					Vector [] roles = Roles.getUserRoles(userLoginInfo.developerID);
					Vector projectRoles = roles[0];
					session.setAttribute("projectList", projectRoles);
					doGetWorkUnitListAction(request,response);
				}
				else {
					HttpSession session = request.getSession();
					request.setAttribute("WorkUnitProjectInfo",projectInfo);
					request.setAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE, "Create Project failed");
					Fms1Servlet.callPage("workUnitProjectAdd.jsp", request, response);
				}
			}
			else{
				request.setAttribute("WorkUnitProjectInfo",projectInfo);
				Fms1Servlet.callPage("workUnitProjectAdd.jsp", request, response);
			}

		}
		catch (Exception e) {
			e.printStackTrace();			
		}
	}
	/**
	 * To delete a Project which is a type of WorkUnit
	 * @param request
	 * @param response
	 */
	public static void doDeleteWorkUnitProjectAction(final HttpServletRequest request, final HttpServletResponse response){
		try {
			final long workUnitId = Long.parseLong(request.getParameter("hideWorkUnitID"));
			final long projectId = Long.parseLong(request.getParameter("hideProjectID"));
			if (WorkUnit.doDeleteWorkUnitProjectAction(workUnitId, projectId)){
				doGetWorkUnitListAction(request, response);
			}
			else {
				Fms1Servlet.callPage("workUnitProjectDetail.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ADD, Update, Delete WorkUnit (Organization, Group, Project) which are create by Hieunv1  - End	
	/**
	 * project selection page
	 */
	public static void doGetProjectHome(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector vProject = null;			
			
			UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
			String userNameLogged = userLoginInfo.account;
			Vector exPjListID = Project.getExternalProjectsIDList(userNameLogged);
			if (exPjListID == null) exPjListID = new Vector();
			
			Vector projectList = (Vector)session.getAttribute("projectList");
			if (exPjListID.size() > 0)
				vProject = Project.getProjectsByWUsAndEx(projectList, exPjListID);
			else 
				vProject = Project.getProjectsByWUs(projectList);
						
			session.setAttribute("ProjectFilterInfo", vProject);
            session.setAttribute("RankList", Project.getRankList());
            Fms1Servlet.callPage("projectHome.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

    /*HuyNH2 add code for project archive*/
    public static void doGetProjectArchiveHome(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HttpSession session = request.getSession();
            Vector projectList = (Vector)session.getAttribute("projectList");
            session.setAttribute("ProjectArchiveFilterInfo", Project.getProjectsArchiveByWUs(projectList));
            session.setAttribute("RankList", Project.getRankList());
            session.setAttribute("CustomerArchiveList",Project.getCustomerListForArchive(projectList));                    
            Fms1Servlet.callPage("projectArchiveHome.jsp",request,response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void doGetProjectArchiveHistoryHome(final HttpServletRequest request, final HttpServletResponse response){
        try {
            final HttpSession session = request.getSession();
            Vector projectList = (Vector)session.getAttribute("projectList");
            session.setAttribute("ProjectArchiveHistoryFilterInfo", Project.getProjectsArchiveHistoryByWUs(projectList));
            session.setAttribute("RankList", Project.getRankList());             
            session.setAttribute("CustomerArchiveList",Project.getCustomerListForArchive(projectList));        
            Fms1Servlet.callPage("projectArchiveHistoryHome.jsp",request,response);            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public static void doArchive(final HttpServletRequest request, final HttpServletResponse response){
        final HttpSession session = request.getSession();        
        String [] arrPrj = request.getParameterValues("selectProject");
        String [] arrStatus = new String[arrPrj.length];
        long devID = Long.parseLong((String)request.getSession().getAttribute("devID"));
        Vector result = new Vector();
        for(int i = 0; i < arrPrj.length; i++){
            if(Project.archive(Long.parseLong(arrPrj[i]),devID)){
                arrStatus[i]="Successful";
            }else{
                arrStatus[i]="Fail";
            }
        }                            
        result.addElement(arrPrj);
        result.addElement(arrStatus);
        Vector vt = Project.getProjectArchiveResult(result);
        session.setAttribute("projectArchiveResult",vt);        
        Fms1Servlet.callPage("projectArchiveResult.jsp",request,response);
    }
    public static void doRestore(final HttpServletRequest request, final HttpServletResponse response){
        final HttpSession session = request.getSession();
        String [] arrPrj = request.getParameterValues("selectProject");
        String [] arrStatus = new String[arrPrj.length];
        long devID = Long.parseLong((String)request.getSession().getAttribute("devID"));        
        Vector result = new Vector();
        for(int i = 0; i < arrPrj.length; i++){
            if(Project.restore(Long.parseLong(arrPrj[i]),devID)){
                arrStatus[i]="Successful";
            }else{
                arrStatus[i]="Fail";
            }            
        }
        result.addElement(arrPrj);
        result.addElement(arrStatus);
        Vector vt = Project.getProjectArchiveResult(result);
        session.setAttribute("projectArchiveResult",vt);
        Fms1Servlet.callPage("projectArchiveResult.jsp",request,response);
    }
    /**
     * @param request
     * @param response
     */
    public static void doGetProjectArchiveHistoryDetailHome(HttpServletRequest request, HttpServletResponse response) {
        String project_id = request.getParameter("workUnitID");
        final HttpSession session = request.getSession();
        Vector result = new Vector();
        result = Project.getArchiveHistoryDetail(project_id);
        session.setAttribute("archiveHistoryDetail",result);
        Fms1Servlet.callPage("projectArchiveHistoryDetail.jsp",request,response);
    }
    /*End*/
	/**
	 * group selection page
	 */
	public static void doGetGroupHome(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector groupList = (Vector) session.getAttribute("groupList");
			RolesInfo ru;
			if ((groupList != null)&&(groupList.size()!=0)) {
				if (groupList.size() > 1) {
					//Go to group selection page
	         		long [] groupIDs=(long [])CommonTools.vectorToArray(groupList,"tableID");
					request.setAttribute("groups",WorkUnit.getGroups(groupIDs));
					Fms1Servlet.callPage("groupHome.jsp",request,response);
				}	
				else{
					//Only one group, go directly to the home page
	         		ru = (RolesInfo)groupList.elementAt(0);
	         		request.setAttribute("workUnitID",Long.toString(ru.workUnitID));
		       		workUnitHomeCaller(request,response);
				}
			}
			else{
				Fms1Servlet.callPage("error.jsp&error=Sorry you are not allowed to access group section",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void doGetGroupInfo(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			long group=Long.parseLong((String)session.getAttribute("groupID"));
			GroupInfo inf = WorkUnit.getGroupInfo(group);
			session.setAttribute("groupInfo",inf);
			Fms1Servlet.callPage("Group/groupInfo.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void doUpdateGroupInfo(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			GroupInfo inf = new GroupInfo();
			// inf.leaderID=DevCbo.parse(request);
			inf.leader = ConvertString.toStandardName(request.getParameter("strAccountName"));
			inf.desc = request.getParameter("desc");
			// inf.groupID = Long.parseLong((String)session.getAttribute("groupID"));
			GroupInfo groupInfo = (GroupInfo)session.getAttribute("groupInfo");
			inf.groupID = groupInfo.groupID;
			inf.name = groupInfo.name;
			inf.isOperation = groupInfo.isOperation;
			UserInfo userInfo = UserProfileCaller.checkUserFilter(request, inf.leader, groupInfo.name);
			if (userInfo != null){
				inf.leaderID = userInfo.developerID;
				WorkUnit.updateGroup(inf);
				doGetGroupInfo(request,response);
			}
			else{
				session.setAttribute("groupInfo",inf);
				Fms1Servlet.callPage("Group/groupInfoUpdate.jsp", request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void doGenerateXMLProjectList(final HttpServletRequest request, final HttpServletResponse response){
		try{
			String strGroupName = request.getParameter("Group") == null ? "": request.getParameter("Group");
			String strUserName = request.getParameter("UserName") == null ? "": request.getParameter("UserName");
			String strPassword = request.getParameter("Password") == null ? "": request.getParameter("Password");
			UserInfo userInfo = UserHelper.getUserInfo(strUserName, strPassword);
			if ((userInfo != null) && userInfo.group.toUpperCase().equals(strGroupName.toUpperCase())){
				String strXMLResult = "";
				XMLWriter xmlWriter = new XMLWriter();
				strXMLResult = xmlWriter.generateXML(Project.doGetProjectInfoToGenerateXML(strGroupName));
				// set attributes for result page
				response.addHeader("Pragma", "No-cache");
				response.addHeader("Cache-control", "no-cache");
				response.addDateHeader("Expires", 1);
				response.setContentType("text/xml;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print(strXMLResult);
			}
			else{
				final HttpSession session = request.getSession();
				// set Multi-language before call error.jsp which ignore Login.jsp(Multi-language of system is set in Login.jsp)
				LanguageChoose languageChoose = new LanguageChoose(LanguageChoose.ENGLISH);
				session.setAttribute("LanguageChoose",languageChoose);
				Fms1Servlet.callPage("error.jsp?error=Action not allowed !", request, response);
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
}