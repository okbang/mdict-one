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
 
 package com.fms1.common.group;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fms1.tools.CommonTools;
import com.fms1.tools.ConvertString;
import com.fms1.tools.ReportMonth;
import com.fms1.common.*;
import com.fms1.web.Constants;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.Parameters;
import com.fms1.web.StringConstants;
import com.fms1.html.WUCombo;
import com.fms1.infoclass.FilterInfo;
import com.fms1.infoclass.ProjectInfo;
import com.fms1.infoclass.TaskInfo;
import com.fms1.infoclass.UserInfo;
/**
 * Page logic of Group monitoring and tasks
 * @author manu
 * @date jan, 2004
 */
public class MonitoringCaller {
	public static final void taskCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			String fromDateStr = request.getParameter("fromDate");
			String toDateStr = request.getParameter("toDate");
			FilterInfo inf=new FilterInfo();
			inf.wuID=workUnitID;
			inf.projectID = 0; //all
			inf.groupID = 0; //all
			inf.type = -1; //SQA- all
			inf.assignment= 0;
			
			//if this is called from the menu , set the default filter
			if (toDateStr == null) {
				
				FilterInfo oldInf=null;
				Object obj=session.getAttribute("tasksfilter");
				if (obj!=null){
					oldInf=(FilterInfo)obj;
				}
				//Page has been called before
				if (oldInf!=null && oldInf.wuID==workUnitID){
					//do nothing, keep the previous params
					inf=oldInf;
				}
				else if(workUnitID == Parameters.PQA_WU){
					//default period =next week
					inf.fromDate=CommonTools.getTodayMidnight();
					inf.toDate=new java.sql.Date(inf.fromDate.getTime()+7*24*3600*1000);
					inf.completion = -1; //open tasks
				}
				else if(workUnitID == Parameters.SQA_WU){
					//default period =current month
					ReportMonth rp = new ReportMonth();
					inf.fromDate = rp.getFirstDayOfMonth();
					inf.toDate = rp.getLastDayOfMonth();
					inf.completion = -1; //open tasks
				}
				else{//operation groups & org
					inf.toDate = new java.sql.Date(new java.util.Date().getTime());
					inf.completion =(int)TaskInfo.STATUS_PENDING; //open tasks
				}
			}
			else {
				//	if this is called from the filter
				inf.completion = Integer.parseInt(request.getParameter("completion"));
				inf.fromDate = CommonTools.parseSQLDate(fromDateStr);
				inf.toDate = CommonTools.parseSQLDate(toDateStr);
				if (inf.toDate!=null)
					CommonTools.setJustBeforeMidnight(inf.toDate);
				String strProj = request.getParameter("project");
				inf.projectID = (strProj == null) ? 0 : Long.parseLong(strProj);
				String strGroupID = request.getParameter("group");
				if (strGroupID != null) //called from group level
					inf.groupID = Long.parseLong(strGroupID);
				String strType = request.getParameter("type");
				if (strType != null) //called SQA
					inf.type = Integer.parseInt(strType);
				inf.assignment= Integer.parseInt(request.getParameter("cboAss"));
			}
			/** Note, tasks are collected for all running project whatever the project/group selected,
			 * the tasks filter is in the jsp.
			 *  if the user filters the search, there is no need to re query DB (see jsp)
			 */
			Vector projects = Project.getChildProjectsByWU((workUnitID == Parameters.SQA_WU||workUnitID == Parameters.PQA_WU) ? Parameters.FSOFT_WU : workUnitID, inf.fromDate, inf.toDate, Project.INPROGRESS_PROJECTS);
			
			Vector projectsFilter;
			if (inf.projectID >0){
				projectsFilter=new Vector();
				projectsFilter.add(Project.getProjectInfo(inf.projectID));
			}
			else if(inf.groupID>0)
				projectsFilter=Project.getChildProjectsByWU(inf.groupID, inf.fromDate, inf.toDate, Project.ALLPROJECTS);
			else if(workUnitID == Parameters.SQA_WU||workUnitID == Parameters.PQA_WU) 
				projectsFilter=null; //=all projects
			else
				projectsFilter=projects;
				
			Vector groups = WorkUnit.getChildrenGroups(Parameters.FSOFT_WU);
			Vector tasks = Tasks.getTasks(inf.fromDate, inf.toDate,groups, projectsFilter, workUnitID);
			Vector users = UserHelper.getUsersByGroup((workUnitID == Parameters.PQA_WU) ? "PQA" : "SQA");
			UserInfo userinfo;
			TaskInfo taskInfo;
			boolean flag=false;
			for (int i=0; i < tasks.size();i++){
				taskInfo=(TaskInfo) tasks.elementAt(i);
				if (taskInfo.assignedTo > 0){
					for (int j=0; j<users.size();j++){
						userinfo=(UserInfo) users.elementAt(j);
						if (taskInfo.assignedTo==userinfo.developerID){
							flag=true;
							break;
						}
					}
				
					if (!flag){
						userinfo= new UserInfo();
						userinfo.developerID=taskInfo.assignedTo;
						userinfo.account=taskInfo.assignedToStr;
						users.add(userinfo);
					}
								
				}
				flag=false;
   			}
			CommonTools.sortVector(users,"account");
			session.setAttribute("groupstasks", groups);
			session.setAttribute("tasks", tasks);
			session.setAttribute("projectstasks", projects);
			session.setAttribute("userstasks",users);
			session.setAttribute("tasksfilter", inf);
			Fms1Servlet.callPage("Group/tasksSupport.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static final void reportTaskCaller(HttpServletRequest request, HttpServletResponse response) {
		final HttpSession session = request.getSession();
		long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
		java.sql.Date fromDate=(java.sql.Date)request.getAttribute("fromDate");
		java.sql.Date toDate=(java.sql.Date)request.getAttribute("toDate");
		if (toDate!=null)
			CommonTools.setJustBeforeMidnight(toDate);
		Vector tasks = Tasks.getTasks(fromDate, toDate,null, null, workUnitID);
		request.setAttribute("tasks", tasks);
	}
	public static final void changeDateCbo(HttpServletRequest request, HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			//filter parameter
			ReportMonth rp;
			rp = new ReportMonth(Integer.parseInt(request.getParameter("cboMonth")), Integer.parseInt(request.getParameter("cboYear")));
			Vector projects =
				Project.getChildProjectsByWU(workUnitID, rp.getFirstDayOfMonth(), rp.getLastDayOfMonth(), Project.INPROGRESS_PROJECTS);
			session.setAttribute("projectstasks", projects);
			String query = request.getQueryString();
			Fms1Servlet.callPage("Group/monitoring.jsp?" + query, request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void drillCaller( HttpServletRequest request,  HttpServletResponse response) {
		final HttpSession session = request.getSession();
		int vtID = Integer.parseInt(request.getParameter("vtID"));
		Vector tasks = (Vector) session.getAttribute("tasks");
		TaskInfo theTask = (TaskInfo) tasks.elementAt(vtID);
		long currentWU=Long.parseLong((String)session.getAttribute("workUnitID"));
		if (currentWU!=theTask.wuID)
			WorkUnitCaller.setWorkUnitHome(request, theTask.wuID);
		Fms1Servlet.callPage(theTask.getLink(), request, response);
	}
	public static final void drillUpCaller( HttpServletRequest request,  HttpServletResponse response) {
		int wuID = Integer.parseInt(request.getParameter("wuID"));
		WorkUnitCaller.setWorkUnitHome(request, wuID);
		taskCaller(request, response);
	}
	/**
	 * Only for SQA/PQA
	 */
	public static final void taskAddPrepCaller( HttpServletRequest request,  HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			java.sql.Date today = new java.sql.Date(new Date().getTime());
			java.sql.Date oneMonthAgo = new java.sql.Date(today.getTime() - 30 * 24 * 3600000);
			Vector projects = Project.getChildProjectsByWU(Parameters.FSOFT_WU, oneMonthAgo, today, Project.INPROGRESS_PROJECTS);
			session.setAttribute("projectstasks", projects);
			Fms1Servlet.callPage("Group/tasksSupportAdd.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void taskUpdatePrepCaller( HttpServletRequest request,  HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			int vtID = Integer.parseInt(request.getParameter("vtID"));
			Vector tasks = (Vector) session.getAttribute("tasks");
			TaskInfo theTask = (TaskInfo) tasks.elementAt(vtID);
			long time = theTask.planDate.getTime();
			long onemonth = 30 * 24 * 3600000;
			java.sql.Date oneMonthAfter = new java.sql.Date(time + onemonth);
			java.sql.Date oneMonthAgo = new java.sql.Date(time - onemonth);
			Vector projects = Project.getChildProjectsByWU(Parameters.FSOFT_WU, oneMonthAgo, oneMonthAfter, Project.INPROGRESS_PROJECTS);
			session.setAttribute("projectstasks", projects);
			Fms1Servlet.callPage("Group/tasksSupportUpdate.jsp?vtID=" + vtID, request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void taskAddCaller( HttpServletRequest request,  HttpServletResponse response) {
		final HttpSession session = request.getSession();
		long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
		String strCurrentGroup = (workUnitID == Parameters.PQA_WU)? "PQA":"SQA";
		TaskInfo inf = new TaskInfo();
		inf.wuID = Long.parseLong(request.getParameter("wuID"));
		inf.desc = request.getParameter("desc");
		inf.assignedToStr = request.getParameter("strAccountName");
		inf.assignedToStr = ConvertString.toStandardizeString(inf.assignedToStr);
		inf.effort = CommonTools.parseDouble(request.getParameter("effort"));
		inf.planDate = CommonTools.parseSQLDate(request.getParameter("planDate"));
		inf.actualDate = CommonTools.parseSQLDate(request.getParameter("actualDate"));
		inf.status = Integer.parseInt(request.getParameter("status"));
		inf.typeID = Integer.parseInt(request.getParameter("type"));
		inf.note= request.getParameter("note");
		//Check user by account
		UserInfo  userInfo =  UserProfileCaller.checkUserFilter(request, inf.assignedToStr, strCurrentGroup);
		if (userInfo != null){
			inf.assignedTo = userInfo.developerID;
			inf.assignedToStr = userInfo.account;
		}
		if (userInfo != null && Tasks.addTask(inf)){
			String ref = request.getParameter(WUCombo.meUpdate);
			if (ref != null && ref.length() > 0) //called from PQA, "OK,Add issue bton"
				IssueCaller.issueAddPrepCaller(request, response);
			else
				taskCaller(request, response);
		}
		else{
			request.setAttribute("usersTasksAdd",inf);
			Fms1Servlet.callPage("Group/tasksSupportAdd.jsp", request, response);
		}
	}
	public static final void taskUpdateCaller( HttpServletRequest request,  HttpServletResponse response) {
		final HttpSession session = request.getSession();
		long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
		String strCurrentGroup = (workUnitID == Parameters.PQA_WU)? "PQA":"SQA";
		int vtID = Integer.parseInt(request.getParameter("vtID"));
		Vector tasks = (Vector) session.getAttribute("tasks");
		TaskInfo inf = (TaskInfo) tasks.elementAt(vtID);
		inf.wuID = Long.parseLong(request.getParameter("wuID"));
		inf.desc = request.getParameter("desc");
		inf.effort = CommonTools.parseDouble(request.getParameter("effort"));
		inf.planDate = CommonTools.parseSQLDate(request.getParameter("planDate"));
		inf.actualDate = CommonTools.parseSQLDate(request.getParameter("actualDate"));
		inf.status = Integer.parseInt(request.getParameter("status"));
		inf.typeID = Integer.parseInt(request.getParameter("type"));
		inf.note= request.getParameter("note");
		inf.assignedToStr = request.getParameter("strAccountName");
		inf.assignedToStr = ConvertString.toStandardizeString(inf.assignedToStr);
		//Check "Assigned to"
		UserInfo  userInfo =  UserProfileCaller.checkUserFilter(request, inf.assignedToStr, strCurrentGroup);
		if (userInfo != null){
			inf.assignedTo = userInfo.developerID;
			inf.assignedToStr = userInfo.account;
		}
		if (userInfo != null && Tasks.updateTask(inf)){
			taskCaller(request, response);
		}
		else{
			request.setAttribute("usersTasksUpdate",inf);
			Fms1Servlet.callPage("Group/tasksSupportUpdate.jsp", request, response);
		}
	}
	public static final void taskDeleteCaller( HttpServletRequest request,  HttpServletResponse response) {
		final HttpSession session = request.getSession();
		int vtID = Integer.parseInt(request.getParameter("vtID"));
		Vector tasks = (Vector) session.getAttribute("tasks");
		TaskInfo inf = (TaskInfo) tasks.elementAt(vtID);
		Tasks.deleteTask(inf.taskID);
		taskCaller(request, response);
	}
	//	/getDecisionList
	public static final void getDecisionList( HttpServletRequest request,  HttpServletResponse response, int[] types) {
		final HttpSession session = request.getSession();
		try {
			String fromDateStr = request.getParameter("fromDate");
			String toDateStr = request.getParameter("toDate");
			ReportMonth rp;
			//called 1st time
			java.sql.Date fromDate;
			java.sql.Date toDate;
			String firstCall = "";
			if (toDateStr == null && fromDateStr == null) {
				rp = new ReportMonth();
				fromDate = rp.getFirstDayOfMonth();
				toDate = rp.getLastDayOfMonth();
				firstCall = "?fromDate=" + CommonTools.dateFormat(fromDate) + "&toDate=" + CommonTools.dateFormat(toDate);
			}
			else {
				fromDate = CommonTools.parseSQLDate(fromDateStr);
				toDate = CommonTools.parseSQLDate(toDateStr);
			}
			/** Note, tasks are collected for all running project whatever the project/group selected,
			 * the tasks filter is in the jsp.
			 *  if the user filters the search, there is no need to re query DB (see jsp)
			 */
			//Vector processes=ProcessInfo.getProcessList();
			Vector tasks = Tasks.getManualTaskList(fromDate, toDate, types);
			Vector users = UserHelper.getAllUsers();
			session.setAttribute("tasks", tasks);
			session.setAttribute("userstasks", users);
			Fms1Servlet.callPage("Group/decision.jsp" + firstCall, request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void decisionAddCaller( HttpServletRequest request,  HttpServletResponse response, String type) {
		TaskInfo inf = new TaskInfo();
		inf.code = request.getParameter("code");
		inf.desc = request.getParameter("desc");
		inf.assignedTo = Long.parseLong(request.getParameter("assignedTo"));
		inf.planDate = CommonTools.parseSQLDate(request.getParameter("planDate"));
		inf.rePlanDate = CommonTools.parseSQLDate(request.getParameter("rePlanDate"));
		inf.actualDate = CommonTools.parseSQLDate(request.getParameter("actualDate"));
		inf.note = request.getParameter("note");
		
		inf.processID =CommonTools.parseInt(request.getParameter("process"));
		inf.feasible = request.getParameter("feasible") != null;
		inf.typeID = TaskInfo.getTypeID(type);
		Tasks.addTask(inf);
		getDecisionList(request, response, (type == TaskInfo.DECISION) ? TaskInfo.typesDecision : TaskInfo.typesPlan);
	}
	public static final void decisionUpdateCaller(HttpServletRequest request, HttpServletResponse response) {
		final HttpSession session = request.getSession();
		int vtID = Integer.parseInt(request.getParameter("vtID"));
		Vector tasks = (Vector) session.getAttribute("tasks");
		TaskInfo inf = (TaskInfo) tasks.elementAt(vtID);
		inf.code = request.getParameter("code");
		inf.desc = request.getParameter("desc");
		inf.assignedTo = Long.parseLong(request.getParameter("assignedTo"));
		inf.planDate = CommonTools.parseSQLDate(request.getParameter("planDate"));
		inf.rePlanDate = CommonTools.parseSQLDate(request.getParameter("rePlanDate"));
		inf.actualDate = CommonTools.parseSQLDate(request.getParameter("actualDate"));
		inf.feasible = request.getParameter("feasible") != null;
		inf.note = request.getParameter("note");
		inf.processID = Integer.parseInt(request.getParameter("process"));
		Tasks.updateTask(inf);
		getDecisionList(request, response, (inf.type == TaskInfo.DECISION) ? TaskInfo.typesDecision : TaskInfo.typesPlan);
	}
	public static final void decisionDeleteCaller(HttpServletRequest request, HttpServletResponse response) {
		final HttpSession session = request.getSession();
		int reqType = Integer.parseInt(request.getParameter("reqType"));
		int vtID = Integer.parseInt(request.getParameter("vtID"));
		Vector tasks = (Vector) session.getAttribute("tasks");
		TaskInfo inf = (TaskInfo) tasks.elementAt(vtID);
		Tasks.deleteTask(inf.taskID);
		getDecisionList(request, response, (reqType == Constants.DECISION_DELETE) ? TaskInfo.typesDecision : TaskInfo.typesPlan);
	}
}
