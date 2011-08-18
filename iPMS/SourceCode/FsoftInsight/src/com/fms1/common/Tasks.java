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
import com.fms1.infoclass.*;

import java.sql.*;
import java.util.Vector;
import com.fms1.infoclass.group.*;
import com.fms1.tools.CommonTools;
import com.fms1.tools.ConvertString;
import com.fms1.tools.Db;
import com.fms1.tools.Table;
import com.fms1.web.Parameters;
import com.fms1.web.ServerHelper;
/**
 * All tasks project, group, organization
 * @author manu
 */
public class Tasks {
	public static final Vector getTasks(java.sql.Date startDate, java.sql.Date endDate,Vector groups, Vector projList, long wuID) {
		Vector taskList;
		int[] types=null;
		if (wuID == Parameters.SQA_WU ||wuID == Parameters.PQA_WU){
			types = (wuID == Parameters.SQA_WU)?TaskInfo.typesSQA:TaskInfo.typesPQA;
			taskList = getSupportAutoTasks(startDate, endDate, projList, wuID);
		}
		else if(wuID == Parameters.FSOFT_WU)//whole organization
			taskList =getOrgTasks(startDate, endDate,projList, groups, wuID) ;
		else//operation groups
			taskList =getGroupTasks(startDate, endDate,projList) ;
		//after getting the automatic tasks, we get the manual ones	
		if (types!=null)
			taskList.addAll(getManualTaskList(startDate, endDate, types));
		sortVector(taskList);
		return taskList;
	}
	public static final Vector getOrgTasks(java.sql.Date startDate, java.sql.Date endDate, Vector projList, Vector groups, long wuID) {
		//org task is the same as group tasks exept that it tracks group issues instead of project issues
		Vector taskList = getGrpTasks(startDate, endDate, projList, wuID);
		IssueInfo issueInf;
		ProjectInfo prjTemp;
		for (int i = 0; i < groups.size(); i++) {
			GroupInfo groupInfo = (GroupInfo) groups.elementAt(i);
			Vector issues = Issues.getIssueListByWorkUnit(groupInfo.wuID);
			for (int j = 0; j < issues.size(); j++) {
				issueInf = (IssueInfo) issues.elementAt(j);
				if (taskCondition(issueInf.dueDate, null, startDate, endDate)) {
					prjTemp = new ProjectInfo();
					prjTemp.setParent(groupInfo.wuID);
					prjTemp.setProjectCode(null);
					prjTemp.setWorkUnitId(groupInfo.wuID);
					prjTemp.setGroupName(groupInfo.name);
					taskList.add(
						new TaskInfo(
							prjTemp,
							TaskInfo.ISSUE,
							issueInf.description,
							issueInf.dueDate,
							null,
							issueInf.closeDate,
							issueInf.comment));
				}
			}
			//rename the group following the workunit system
			for (int m = 0; m < projList.size(); m++) {
				prjTemp = (ProjectInfo) projList.elementAt(m);
				if (prjTemp.getParent() == groupInfo.wuID) {
					prjTemp.setGroupName(groupInfo.name);
					break;
				}
			}
		}
		sortVector(taskList);
		return taskList;
	}
	public static final Vector getGroupTasks(java.sql.Date startDate, java.sql.Date endDate, Vector projList) {
		Vector taskList = getGrpTasks(startDate, endDate, projList, 0);
		sortVector(taskList);
		return taskList;
	}
	/**
	 * Collects tasks from projects and other systems
	 */
	private static final Vector getSupportAutoTasks(java.sql.Date startDate, java.sql.Date endDate, Vector projList, long wuID) {
		Date pd, rpd, ad;
		Vector  vectorEffort;
		TaskInfo taskInfo;
		String param;
		ProjectInfo prj = null;
		OtherActInfo otherActInfo;
		QltActivityEffortInfo effInfo;
		ModuleInfo moduleInfo;
		Vector taskList = new Vector();
		Vector users = null;
		
		Vector roles = null;
		try{
		int []SQA_QC_Types={QCActivityInfo.BASELINE_AUDIT,QCActivityInfo.FINAL_INSPECTION,QCActivityInfo.DOCUMENT_REVIEW};
		int []PQA_QC_Types={QCActivityInfo.INTERNAL_AUDIT,QCActivityInfo.QUALITY_GATE_INSPECTION};
		Vector otherQAList = QualityObjective.getOtherActivityList(startDate,endDate,(wuID == Parameters.SQA_WU?SQA_QC_Types:PQA_QC_Types));

		
		long[]prjIds=null;
		
		//no project filter specified get the prjects from the QC activities
		boolean projFilter=(projList!=null);
		if (!projFilter){
			prjIds=(long[])CommonTools.vectorToArrayDistinct(otherQAList,"prjID");
			projList=Project.getProjectInfos(prjIds);
		}
		int nproj = projList.size();
		//OTHER QUALITY****************************
		for (int m = 0; m < nproj; m++) {
			prj = (ProjectInfo) projList.elementAt(m);
			vectorEffort = Effort.getQltActivityEffortList(prj);
			roles = Assignments.getProjectRole(prj.getProjectId(), (wuID == Parameters.SQA_WU) ? ResponsibilityInfo.ROLE_SQA : ResponsibilityInfo.ROLE_PQA);
			for (int i = 0; i < otherQAList.size(); i++) {
				otherActInfo = (OtherActInfo) otherQAList.elementAt(i);
				if (otherActInfo.prjID == prj.getProjectId()) {
					pd = otherActInfo.pEndD;
					rpd = null;
					ad = otherActInfo.aEndD;
					param = null;
					if (wuID == Parameters.SQA_WU)
						switch (otherActInfo.qcActivity) {
							case QCActivityInfo.BASELINE_AUDIT :
								param = TaskInfo.BASELINE;
								break;
							case QCActivityInfo.FINAL_INSPECTION :
								param = TaskInfo.FINAL_INSPECTION;
								break;
							case QCActivityInfo.DOCUMENT_REVIEW :
								param = TaskInfo.REVIEW2;
								break;
						}
					else
						switch (otherActInfo.qcActivity) {
							case QCActivityInfo.QUALITY_GATE_INSPECTION :
								if (otherActInfo.standartStage != StageInfo.STAGE_INITIATION
									&& otherActInfo.standartStage != StageInfo.STAGE_TERMINATION)
									param = TaskInfo.QUALITY_GATE_INSPECTION;
								break;
							case QCActivityInfo.INTERNAL_AUDIT :
								param = TaskInfo.INTERNAL_AUDIT;
								break;
						}
					if ( taskCondition(pd, rpd, startDate, endDate)) {
						taskInfo = new TaskInfo(prj, param, otherActInfo.activity, pd, rpd, ad, otherActInfo.note);
						//we kkep the assignee for internal audit
						if (param == TaskInfo.INTERNAL_AUDIT) {
							if (users == null)
								users = UserHelper.getAllUsers();
							taskInfo.assignedToStr = otherActInfo.conductorName;
							taskInfo.assignedTo = otherActInfo.conductor;
						}
						else if (roles.size() > 0) {
							AssignmentInfo assInf = Assignments.getProjectRoleAtDate(roles, pd);
							taskInfo.assignedToStr = assInf.account;
							taskInfo.assignedTo = assInf.devID;
						}
						taskInfo.status = otherActInfo.status;
						taskInfo.metricVal = otherActInfo.metric; //used to pass the TC coverage
						for (int j = 0; j < vectorEffort.size(); j++) {
							effInfo = (QltActivityEffortInfo) vectorEffort.elementAt(j);
							if (effInfo.type == QltActivityEffortInfo.TYPE_OTHER_QUALITY
								&& effInfo.activityID == otherActInfo.otherActID
								&& effInfo.actual >= 0) {
								taskInfo.effort = effInfo.actual;
								break;
							}
						}
						taskList.add(taskInfo);
					}
				}
			}
		}
	//SQA specific******************
		if (wuID == Parameters.SQA_WU) {
				/**
				 * reviews : 
				 * 1-get the products review dates
				 * 2-Then assign the review to the SQA of the project at the date of the review
				 * 
				 */
				//Convert vertor of projects into an array of project IDs
				long[] projects = null;
				if (projFilter){
					projects=new long[nproj];
					for (int n = 0; n < nproj; n++) {
						prj = (ProjectInfo) projList.elementAt(n);
						projects[n] = prj.getProjectId();
					}
				}
				//get the products (previously called modules)
				otherQAList = WorkProduct.getModuleListSQA(projects, startDate, endDate);
				if (!projFilter){
					Vector temp=new Vector();
					boolean found;
					for (int i=0;i<otherQAList.size();i++){
						found=false;
						moduleInfo = (ModuleInfo) otherQAList.elementAt(i);
						for (int j=0;j<prjIds.length;j++){
							if (prjIds[j]==(long)moduleInfo.projectID){
								found=true;
								break;
							}
						}
						if (!found){
							temp.add(moduleInfo);
						}
					}
					int [] tempArr=(int [])CommonTools.vectorToArrayDistinct(temp,"projectID");
					long [] tempArr2= new long[tempArr.length];
					for(int f=0;f<tempArr.length;f++)
						tempArr2[f]=(long)tempArr[f];
					projList.addAll(Project.getProjectInfos(tempArr2));
				}
				//this table will contains all the SQA assignments for all projects
				Table rolesTable = new Table();
				//For each product review, create a task
				for (int j = 0; j < otherQAList.size(); j++) {
					moduleInfo = (ModuleInfo) otherQAList.elementAt(j);
					pd = moduleInfo.plannedReviewDate;
					ad = moduleInfo.actualReviewDate;
					if (!moduleInfo.isNormal) {
						for (int n = 0; n < projList.size(); n++) {
							prj = (ProjectInfo) projList.elementAt(n);
							if (prj.getProjectId() == moduleInfo.projectID)
								break;
						}
						taskInfo = new TaskInfo(prj, TaskInfo.REVIEW, moduleInfo.name, pd, null, ad, moduleInfo.note);
						taskInfo.status = (int) ((ad == null) ? TaskInfo.STATUS_PENDING : TaskInfo.STATUS_PASS);
						taskInfo.effort = moduleInfo.actualEffort;
						roles = (Vector) rolesTable.get(taskInfo.prjID);
						if (roles == null) {
							//we don't have the SQA assignments for this project, let's get it
							roles = Assignments.getProjectRole(taskInfo.prjID, ResponsibilityInfo.ROLE_SQA);
							rolesTable.add(taskInfo.prjID, roles);
						}
						if (roles.size() > 0) {
							//now we have the SQA assignments, get the SQA at the time of the review
							AssignmentInfo assInf = Assignments.getProjectRoleAtDate(roles, pd);
							//and set it as the assigned perdon for the task
							taskInfo.assignedToStr = assInf.account;
							taskInfo.assignedTo = assInf.devID;
						}
						taskList.add(taskInfo);
					}
				}
				//SQA DP LOG (GROUP & Project level)
				final Vector projectDPlog = Defect.getDPLog(startDate,endDate);
				final Vector groupDPlog = Defect.getDPLog(wuID,startDate,endDate);
				DPLogInfo dpinfo ;
				Vector vtTemp;
				for (int k = 0; k < 2; k++) {
					vtTemp=k==0?projectDPlog:groupDPlog;
				for (int j = 0; j < vtTemp.size(); j++) {
						dpinfo = (DPLogInfo) vtTemp.elementAt(j);
						taskInfo = new TaskInfo((k==0)?TaskInfo.DP:TaskInfo.DP_SQA, dpinfo.dpaction,dpinfo.targetDate, dpinfo.closedDate);
						taskInfo.grpName=dpinfo.wuName; 
						taskInfo.wuID=dpinfo.workunitID;
						taskInfo.parentwuID=dpinfo.parentWorkunitID;
						if (k==0)
							taskInfo.prjID=dpinfo.projectID;
						taskInfo.note=dpinfo.dpNote;
						taskInfo.status = (int) ((dpinfo.closedDate == null) ? TaskInfo.STATUS_PENDING : TaskInfo.STATUS_PASS);
						taskInfo.assignedTo=dpinfo.devID;
						taskInfo.assignedToStr=dpinfo.devAccount;
						taskList.add(taskInfo);
				}
			}
		}
		//PQA specific :CALL LOG+ NCMS : ***********************************
		if (wuID == Parameters.PQA_WU) {
			taskList.addAll(Ncms.getNCTasks(startDate, endDate));
			taskList.addAll(Ncms.getPQACallLogs(startDate, endDate));
		}
		}
				catch(Exception e){
			e.printStackTrace();
		}
		return taskList;
	}
	private static final Vector getGrpTasks(java.sql.Date startDate, java.sql.Date endDate, Vector projList, long wuID) {
		//we consider that all task must be completed before project completion
		ProjectInfo prj;
		Vector taskList = new Vector();
		StageInfo stageInfo;
		ModuleInfo moduleInfo;
		DependencyInfo dependencyInfo;
		SubcontractInfo subcontractInfo;
		FinancialInfo financialInfo;
		IssueInfo issueInf;
		Date pd, rpd, ad;
		int i;
		Vector vector;
		for (int m = 0; m < projList.size(); m++) {
			prj = (ProjectInfo) projList.elementAt(m);
			//milestones
			vector = Schedule.getStageList(prj.getProjectId());
			for (i = 0; i < vector.size(); i++) {
				stageInfo = (StageInfo) vector.elementAt(i);
				pd = stageInfo.bEndD;
				rpd = stageInfo.pEndD;
				ad = stageInfo.aEndD;
				if (taskCondition(pd, rpd, startDate, endDate))
					taskList.add(new TaskInfo(prj, TaskInfo.MILESTONE, stageInfo.stage, pd, rpd, ad, stageInfo.comments));
			}
			//review and test activ+deliv
			vector = WorkProduct.getModuleListSimple(prj.getProjectId(), WorkProduct.ORDER_BY_PRELEASE);
			for (i = 0; i < vector.size(); i++) {
				moduleInfo = (ModuleInfo) vector.elementAt(i);
				pd = moduleInfo.plannedReleaseDate;
				rpd = moduleInfo.rePlannedReleaseDate;
				ad = moduleInfo.actualReleaseDate;
				if ((moduleInfo.isDel) && moduleInfo.status != ModuleInfo.STATUS_CANCELLED && taskCondition(pd, rpd, startDate, endDate))
					taskList.add(new TaskInfo(prj, TaskInfo.DELIVERY, moduleInfo.name, pd, rpd, ad, moduleInfo.note));
			}
			//dependencies
			vector = Project.getPLDependencyList(prj.getProjectId());
			for (i = 0; i < vector.size(); i++) {
				dependencyInfo = (DependencyInfo) vector.elementAt(i);
				pd = dependencyInfo.plannedDeliveryDate;
				rpd = null;
				ad = dependencyInfo.actualDeliveryDate;
				if (taskCondition(pd, rpd, startDate, endDate))
					taskList.add(new TaskInfo(prj, TaskInfo.DEPENDENCY, dependencyInfo.item, pd, rpd, ad, dependencyInfo.note));
			}
			//subcontract
			vector = Project.getPLSubcontractList(prj.getProjectId());
			for (i = 0; i < vector.size(); i++) {
				subcontractInfo = (SubcontractInfo) vector.elementAt(i);
				pd = subcontractInfo.plannedDeliveryDate;
				rpd = null;
				ad = subcontractInfo.actualDeliveryDate;
				if (taskCondition(pd, rpd, startDate, endDate))
					taskList.add(new TaskInfo(prj, TaskInfo.SUBCONTRACT, subcontractInfo.job, pd, rpd, ad, subcontractInfo.note));
			}
			//finance
			vector = Project.getFinanList(prj.getProjectId());
			for (i = 0; i < vector.size(); i++) {
				financialInfo = (FinancialInfo) vector.elementAt(i);
				pd = financialInfo.dueD;
				rpd = null;
				ad = financialInfo.actualD;
				if (taskCondition(pd, rpd, startDate, endDate))
					taskList.add(new TaskInfo(prj, TaskInfo.FINANCE, financialInfo.item, pd, rpd, ad, null));
			}
			//issues
			if (wuID != Parameters.FSOFT_WU) { //for group we get project issues
				vector = Issues.getIssueListByWorkUnit(prj.getWorkUnitId());
				for (i = 0; i < vector.size(); i++) {
					issueInf = (IssueInfo) vector.elementAt(i);
					pd = issueInf.dueDate;
					rpd = null;
					ad = issueInf.closeDate;
					if (taskCondition(pd, rpd, startDate, endDate))
						taskList.add(new TaskInfo(prj, TaskInfo.ISSUE, issueInf.description, pd, rpd, ad, issueInf.comment));
				}
			}
		}
	

		return taskList;
	}
	//used in the function above
	private static boolean taskCondition(Date pd, Date rpd, Date startDate, Date endDate) {
		Date date = (rpd == null) ? pd : rpd;
		return (date != null && date.compareTo(endDate) <= 0 && (startDate == null || date.compareTo(startDate) >= 0));
	}
	/******************For projects************************/
	public static final Vector getOpenProjectTasks(final long prjID, java.sql.Date endDate) {
		return Tasks.getProjectScheduleTasks(prjID, null, endDate, 0);
	}
	public static final Vector getClosedProjectTasks(final long prjID, java.sql.Date startDate, java.sql.Date endDate) {
		return Tasks.getProjectScheduleTasks(prjID, startDate, endDate, 1);
	}
	private static final Vector getProjectScheduleTasks(final long prjID, java.sql.Date startDate, java.sql.Date endDate, int reportType) {
		Vector taskList = new Vector();
		//milestones
		//final Vector stageVt = Schedule.getStageList(prjID);
		//review and test activ+deliv
		final Vector test_review_Vt = WorkProduct.getModuleListSimple(prjID, WorkProduct.ORDER_BY_PRELEASE);
		//other qual
		final Vector vtOtherAct = QualityObjective.getOtherActivityList(prjID);
		//training
		final Vector vtTrain = Project.getTrainingList(prjID);
		//dependencies
		final Vector dependencyList = Project.getPLDependencyList(prjID);
		//subcontract
		final Vector subcontractVt = Project.getPLSubcontractList(prjID);
		//finance
		final Vector vtFinan = Project.getFinanList(prjID);
		//issues
		final Vector issueList = Issues.getIssuesByDate(prjID, endDate);
		//dplog
		ProjectInfo prjInfo = Project.getProjectInfo(prjID);
		final Vector dpList = Defect.getDPLog(prjInfo.getWorkUnitId());

		ModuleInfo moduleInfo;
		OtherActInfo otherActInfo;
		TrainingInfo trainingInfo;
		DependencyInfo dependencyInfo;
		SubcontractInfo subcontractInfo;
		FinancialInfo financialInfo;
		IssueInfo issueInf;
		DPLogInfo dpInf;
		Date pd, ad;
		int i;
		String type;
		for (i = 0; i < test_review_Vt.size(); i++) {
			moduleInfo = (ModuleInfo) test_review_Vt.elementAt(i);
			pd = moduleInfo.thePlanReleaseDate;
			ad = moduleInfo.actualReleaseDate;
			if (moduleInfo.status != ModuleInfo.STATUS_CANCELLED && taskCondition(reportType, pd, ad, startDate, endDate))
				taskList.add(new TaskInfo((moduleInfo.isDel) ? TaskInfo.DELIVERY : TaskInfo.RELEASE, moduleInfo.name, pd, ad));
			pd = moduleInfo.plannedTestEndDate;
			ad = moduleInfo.actualTestEndDate;
			if (moduleInfo.status != ModuleInfo.STATUS_CANCELLED && taskCondition(reportType, pd, ad, startDate, endDate))
				taskList.add(new TaskInfo(TaskInfo.TEST, moduleInfo.name, pd, ad));
			pd = moduleInfo.plannedReviewDate;
			ad = moduleInfo.actualReviewDate;
			if (moduleInfo.status != ModuleInfo.STATUS_CANCELLED && taskCondition(reportType, pd, ad, startDate, endDate))
				taskList.add(new TaskInfo(TaskInfo.REVIEW, moduleInfo.name, pd, ad));
		}
		for (i = 0; i < vtOtherAct.size(); i++) {
			otherActInfo = (OtherActInfo) vtOtherAct.elementAt(i);
			pd = otherActInfo.pEndD;
			ad = otherActInfo.aEndD;
			type = (otherActInfo.qcActivity == QCActivityInfo.QUALITY_GATE_INSPECTION) ? TaskInfo.MILESTONE : TaskInfo.OTHERQUALITY;
			if (otherActInfo.status != TaskInfo.STATUS_CANCELLED && taskCondition(reportType, pd, ad, startDate, endDate))
				taskList.add(new TaskInfo(type, otherActInfo.activity, pd, ad));
		}
		for (i = 0; i < vtTrain.size(); i++) {
			trainingInfo = (TrainingInfo) vtTrain.elementAt(i);
			pd = trainingInfo.endD;
			ad = trainingInfo.actualEndD;
			if (taskCondition(reportType, pd, ad, startDate, endDate))
				taskList.add(new TaskInfo(TaskInfo.TRAINING, trainingInfo.topic, pd, ad));
		}
		for (i = 0; i < dependencyList.size(); i++) {
			dependencyInfo = (DependencyInfo) dependencyList.elementAt(i);
			pd = dependencyInfo.plannedDeliveryDate;
			ad = dependencyInfo.actualDeliveryDate;
			if (taskCondition(reportType, pd, ad, startDate, endDate))
				taskList.add(new TaskInfo(TaskInfo.DEPENDENCY, dependencyInfo.item, pd, ad));
		}
		for (i = 0; i < subcontractVt.size(); i++) {
			subcontractInfo = (SubcontractInfo) subcontractVt.elementAt(i);
			pd = subcontractInfo.plannedDeliveryDate;
			ad = subcontractInfo.actualDeliveryDate;
			if (taskCondition(reportType, pd, ad, startDate, endDate))
				taskList.add(new TaskInfo(TaskInfo.SUBCONTRACT, subcontractInfo.deliverable, pd, ad));
		}
		for (i = 0; i < vtFinan.size(); i++) {
			financialInfo = (FinancialInfo) vtFinan.elementAt(i);
			pd = financialInfo.dueD;
			ad = financialInfo.actualD;
			if (taskCondition(reportType, pd, ad, startDate, endDate))
				taskList.add(new TaskInfo(TaskInfo.FINANCE, financialInfo.item, pd, ad));
		}
		for (i = 0; i < issueList.size(); i++) {
			issueInf = (IssueInfo) issueList.elementAt(i);
			pd = issueInf.dueDate;
			ad = issueInf.closeDate;
			if (taskCondition(reportType, pd, ad, startDate, endDate))
				taskList.add(new TaskInfo(TaskInfo.ISSUE, issueInf.description, pd, ad));
		}
		for (i = 0; i < dpList.size(); i++) {
			dpInf = (DPLogInfo) dpList.elementAt(i);
			pd = dpInf.targetDate;
			ad = dpInf.closedDate;
			if (taskCondition(reportType, pd, ad, startDate, endDate))
				taskList.add(new TaskInfo(TaskInfo.DP, dpInf.dpaction, pd, ad));
		}
		sortVector(taskList);
		return taskList;
	}
	//used in the function above
	private static boolean taskCondition(int reportType, Date pd, Date ad, Date startDate, Date endDate) {
		try {
			return (
				((reportType == 0) && pd != null && pd.compareTo(endDate) <= 0 && (ad == null || ad.after(endDate)))
					|| ((reportType == 1) && ad != null && ad.compareTo(endDate) <= 0 && ad.compareTo(startDate) >= 0));
		}
		catch (Exception e) {
			return false;
		}
	}
	public final static void sortVector(Vector sortMe) {
		// sort by date
		boolean isFound = true;
		int size = sortMe.size();
		TaskInfo task, taskPrev, taskTmp;
		long prevDate, date;
		while (isFound) {
			prevDate = 0;
			date = 0;
			isFound = false;
			for (int i = 1; i < size; i++) {
				taskPrev = (TaskInfo) sortMe.elementAt(i - 1);
				task = (TaskInfo) sortMe.elementAt(i);
				prevDate = (taskPrev.rePlanDate == null) ? taskPrev.planDate.getTime() : taskPrev.rePlanDate.getTime();
				date = (task.rePlanDate == null) ? task.planDate.getTime() : task.rePlanDate.getTime();
				if (date < prevDate) {
					taskTmp = task;
					sortMe.setElementAt(taskPrev, i);
					sortMe.setElementAt(taskTmp, i - 1);
					isFound = true;
				}
			}
		}
	}

	/**
	 * returns vector of TaskInfo
	 */
	public static final Vector getManualTaskList(Date startDate, Date endDate, int[] types) {
		return  getManualTaskList( startDate,  endDate, types, 0);
	}
	public static final Vector getManualTaskListClosed(Date startDate, Date endDate, int[] types) {
		return  getManualTaskList( startDate,  endDate, types, 1);
	}
	private static final Vector getManualTaskList(Date startDate, Date endDate, int[] types,int status) {
		Connection conn = null;
		String sql = null;
		PreparedStatement stmt = null;
		final Vector vector = new Vector();
		ResultSet rs = null;
		try {
			String dateContraint= status==0 ?" AND NVL(REPLAN_DATE,PLAN_DATE)":" AND ACTUAL_DATE";
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT TASKS.* , WORKUNITNAME,ACCOUNT, PARENTWORKUNITID,WORKUNIT.TYPE WUTYPE,PROJECT.PROJECT_ID"
					+" FROM TASKS,WORKUNIT,DEVELOPER,PROJECT"
					+ " WHERE WORKUNIT.WORKUNITID=TASKS.WORKUNITID AND DEVELOPER.DEVELOPER_ID=TASKS.ASSIGNEDTO "
					+ ((startDate != null) ? dateContraint+" >= ? " : "")
					+ ((endDate != null) ? dateContraint+" <= ? " : "")
					+ " AND TASKS.TYPE IN("
					+ ConvertString.arrayToString(types, ",")
					+ ") AND project.project_id(+)=workunit.tableid"
					+" ORDER BY TASKS.PLAN_DATE";
			stmt = conn.prepareStatement(sql);
			int i = 1;
			if (startDate != null)
				stmt.setDate(i++, startDate);
			if (endDate != null)
				stmt.setDate(i, endDate);
			rs = stmt.executeQuery();
			TaskInfo inf;
			while (rs.next()) {
				inf = new TaskInfo();
				inf.manualTask = true;
				inf.taskID = rs.getLong("TASKID");
				inf.prjCode = rs.getString("WORKUNITNAME");
				inf.wuID = rs.getLong("WORKUNITID");
				inf.desc = rs.getString("DESCRIPTION");
				inf.planDate = rs.getDate("PLAN_DATE");
				inf.rePlanDate = rs.getDate("REPLAN_DATE");
				inf.actualDate = rs.getDate("ACTUAL_DATE");
				inf.assignedTo = rs.getLong("ASSIGNEDTO");
				inf.assignedToStr = rs.getString("ACCOUNT");
				inf.effort = Db.getDouble(rs, "EFFORT");
				inf.status = rs.getInt("STATUS");
				inf.note = rs.getString("NOTE");
				inf.typeID = rs.getInt("TYPE");
				inf.type = TaskInfo.types[inf.typeID];
				inf.processID = rs.getInt("PROCESS");
				inf.feasible = (rs.getInt("FEASIBLE") == 1);
				inf.code = rs.getString("CODE");
				inf.parentwuID = rs.getLong("PARENTWORKUNITID");
				if ( rs.getInt("WUTYPE")==WorkUnitInfo.TYPE_PROJECT)
					inf.prjID = rs.getLong("PROJECT_ID");
				vector.add(inf);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, rs);
			return vector;
		}
	}
	public static boolean updateTask(TaskInfo inf) {
		Connection conn = null;
		String sql = null;
		PreparedStatement stmt = null;
		try {
			sql =
				"UPDATE TASKS SET"
					+ " WORKUNITID=?"
					+ ", DESCRIPTION=?"
					+ ", ASSIGNEDTO=?"
					+ ", EFFORT=?"
					+ ", PLAN_DATE=?"
					+ ", ACTUAL_DATE=?"
					+ ", STATUS=?"
					+ ", TYPE=?"
					+ ", NOTE=?"
					+ ", PROCESS=?"
					+ ", REPLAN_DATE=?"
					+ ", FEASIBLE=?"
					+ ", CODE=?"
					+ " WHERE TASKID = ?";
			conn = ServerHelper.instance().getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, inf.wuID);
			stmt.setString(2, inf.desc);
			stmt.setLong(3, inf.assignedTo);
			Db.setDouble(stmt, 4, inf.effort);
			stmt.setDate(5, inf.planDate);
			stmt.setDate(6, inf.actualDate);
			Db.setDouble(stmt, 7, inf.status);
			stmt.setInt(8, inf.typeID);
			stmt.setString(9, inf.note);
			stmt.setInt(10, inf.processID);
			stmt.setDate(11, inf.rePlanDate);
			stmt.setInt(12, inf.feasible ? 1 : 0);
			stmt.setString(13, inf.code);
			stmt.setLong(14, inf.taskID);
			return (stmt.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, null);
		}
	}
	public static boolean addTask(TaskInfo inf) {
		Connection conn = null;
		String sql = null;
		PreparedStatement stmt = null;
		try {
			sql =
				"INSERT INTO TASKS (TASKID, WORKUNITID, DESCRIPTION, ASSIGNEDTO, EFFORT, PLAN_DATE,"
					+ " ACTUAL_DATE, STATUS, TYPE,NOTE,PROCESS,REPLAN_DATE,FEASIBLE,CODE) "
					+ " VALUES (NVL((SELECT MAX(TASKID)+1 FROM TASKS),1),?,?,?,?,?,?,?,?,?,?,?,?,?)";
			conn = ServerHelper.instance().getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, inf.wuID);
			stmt.setString(2, inf.desc);
			stmt.setLong(3, inf.assignedTo);
			Db.setDouble(stmt, 4, inf.effort);
			stmt.setDate(5, inf.planDate);
			stmt.setDate(6, inf.actualDate);
			Db.setDouble(stmt, 7, inf.status);
			stmt.setInt(8, inf.typeID);
			stmt.setString(9, inf.note);
			stmt.setInt(10, inf.processID);
			stmt.setDate(11, inf.rePlanDate);
			stmt.setInt(12, inf.feasible ? 1 : 0);
			stmt.setString(13, inf.code);
			return (stmt.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, null);
		}
	}
	public static boolean deleteTask(final long tasksID) {

			return Db.delete(tasksID,"TASKID","TASKS");
	}
	public static double getOverdueTasks(Date fromDate, Date toDate, int[] taskTypes) {
		Vector tasks = Tasks.getManualTaskList(fromDate, toDate, taskTypes);
		double retVal = Double.NaN;
		double size = tasks.size();
		int overdue = 0;
		TaskInfo task;
		long today = new java.util.Date().getTime();
		Date planned;
		for (int i = 0; i < size; i++) {
			task = (TaskInfo) tasks.elementAt(i);
			planned = (task.rePlanDate == null) ? task.planDate : task.rePlanDate;
			if ((task.actualDate == null && planned.getTime() < today)
				|| (task.actualDate != null && planned.getTime() < task.actualDate.getTime()))
				overdue++;
		}
		if (size != 0)
			retVal = overdue * 100d / size;
		return retVal;
	}
	public static double getPQATimeliness(Date fromDate, Date toDate) {
		OtherActInfo otherActInfo;
		double size = 0;
		double sum = 0;
		double retVal = Double.NaN;
		int[] typeArray={QCActivityInfo.QUALITY_GATE_INSPECTION,QCActivityInfo.INTERNAL_AUDIT};
		//OTHER QUALITY
		Vector activities = QualityObjective.getOtherActivityList(fromDate,toDate,typeArray);
		
		for (int i = 0; i < activities.size(); i++) {
			otherActInfo = (OtherActInfo) activities.elementAt(i);
			if (otherActInfo.status != TaskInfo.STATUS_CANCELLED
			&&otherActInfo.standartStage != StageInfo.STAGE_INITIATION
			&& otherActInfo.standartStage != StageInfo.STAGE_TERMINATION){
				size++;
				if (otherActInfo.aEndD!=null && otherActInfo.aEndD.compareTo(otherActInfo.pEndD) <= 0)
					sum++;
			}
		}
		//Manual tasks
		int[] typeArray2={TaskInfo.getTypeID(TaskInfo.INTERNAL_AUDIT)};
		Vector tasks=getManualTaskList(fromDate, toDate,typeArray2);
		size += tasks.size();
		TaskInfo tinf;
		for (int i = 0; i < tasks.size(); i++) {
			tinf = (TaskInfo) tasks.elementAt(i);
			if (tinf.actualDate!=null && tinf.actualDate.compareTo(tinf.planDate) <= 0)
				sum++;
		}
		if (size != 0)
			retVal = sum * 100d / size;
		return retVal;
	}
	public static double getFeasibility(Date fromDate, Date toDate) {
		double size = 0;
		double sum = 0;
		double retVal = Double.NaN;
		Vector tasks = Tasks.getManualTaskList(fromDate, toDate, TaskInfo.typesDecision);
		size = tasks.size();
		int total = 0;
		TaskInfo task;
		for (int i = 0; i < size; i++) {
			task = (TaskInfo) tasks.elementAt(i);
			if (task.processID == ProcessInfo.MANAGEMENT_REVIEW) {
				total++;
				if (task.feasible)
					sum++;
			}
		}
		if (total != 0)
			retVal = sum * 100d / total;
		return retVal;
	}
}
