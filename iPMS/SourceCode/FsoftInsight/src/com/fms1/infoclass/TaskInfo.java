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
 
 package com.fms1.infoclass;
import com.fms1.web.Constants;
import java.sql.Date ;

import com.fms1.common.Ncms;
/**
 * @author manu
 * Tasks for weekly report
 */
public class TaskInfo {
	
	public static final String MILESTONE = "Milestone";
	public static final String TEST = "Test";
	public static final String REVIEW = "Review";
	public static final String RELEASE = "Release";
	public static final String DELIVERY = "Delivery";
	public static final String OTHERQUALITY = "Other quality activity";
	public static final String TRAINING = "Training";
	public static final String DEPENDENCY = "Dependency";
	public static final String SUBCONTRACT = "Subcontract";
	public static final String FINANCE = "Finance";
	public static final String ISSUE = "Issue";
	//SQA types
	public static final String BASELINE = QCActivityInfo.getActivity(QCActivityInfo.BASELINE_AUDIT).name;//from project
	public static final String DP = "Defect prevention";//from project
	public static final String DP_SQA = "D.P. group level";//DP log of SQA group
	public static final String FINAL_INSPECTION = QCActivityInfo.getActivity(QCActivityInfo.FINAL_INSPECTION).name;//from project
	public static final String OTHERS_SQA = "Others";//Manual tasks
	public static final String REVIEW2 = "Review";
	//PQA
	public static final String INTERNAL_AUDIT = QCActivityInfo.getActivity(QCActivityInfo.INTERNAL_AUDIT).name;
	public static final String QUALITY_GATE_INSPECTION = QCActivityInfo.getActivity(QCActivityInfo.QUALITY_GATE_INSPECTION).name;
	//public static final String INTERNAL_AUDIT_MANUAL="Internal audit";
	public static final String QUALITY_GATE_INSPECTION_MANUAL = "Quality gate";
	public static final String REPORTING = "Reporting";
	public static final String CONTROL = "Control";
	public static final String DATABASING = "Databasing";
	public static final String TRAININGPQA = "Training";
	public static final String DOCUMENT_CONTROL = "Document control";
	public static final String MANAGEMENT_REVIEW = "Management review";
	public static final String PLANNING = "Planning";
	public static final String PROJECT_RELATED = "Project related";
	public static final String OTHERS = "Others";
	//public static final String CALL_LOG ="Call log";
	public static final String DECISION = "Decision";
	public static final String PLAN = "Plan";
	//for Manual tasks
	public static final String [] types = {BASELINE,DP,DP_SQA,FINAL_INSPECTION,OTHERS_SQA,INTERNAL_AUDIT,QUALITY_GATE_INSPECTION,REPORTING,CONTROL,DATABASING,TRAININGPQA,DOCUMENT_CONTROL,MANAGEMENT_REVIEW,PLANNING,PROJECT_RELATED,OTHERS,DECISION,PLAN,REVIEW,MILESTONE,DELIVERY,DEPENDENCY,SUBCONTRACT,FINANCE,ISSUE};//db id=order
	public static final int [] allTypesSQA = {0,1,2,3,18,4};//Used for filter combo in SQA tasks
	public static final int [] allTypesPQA = {5,6,7,8,9,10,11,12,13,14,15};//Used for filter combo in PQA tasks
	public static final int [] allTypesOrg = {19,20,21,22,23,24};
	//	Used for manual tasks, matches value aboves with values in task table DB
	public static final int [] typesSQA = {4};
	public static final int [] typesPQA = {5,6,7,8,9,10,11,12,13,14,15};
	public static final int [] typesDecision = {16};
	
	public static final int [] typesPlan = {17};
	//Links
	private static final String root = "Fms1Servlet?reqType=";
	public static final String MILESTONELINK = root + Constants.SCHE_STAGE_GET_LIST;
	public static final String TESTLINK = root + Constants.SCHE_REVIEW_TEST_GET_LIST;
	public static final String REVIEWLINK = root + Constants.SCHE_REVIEW_TEST_GET_LIST;
	public static final String RELEASELINK = root + Constants.SCHE_REVIEW_TEST_GET_LIST;
	public static final String DELIVERYLINK = root + Constants.WO_DELIVERABLE_GET_LIST;
	public static final String OTHERQUALITYLINK = root + Constants.SCHE_OTHER_QUALITY_GET_LIST;
	public static final String TRAININGLINK = root + Constants.SCHE_TRAINING_PLAN_GET_LIST;
	public static final String DEPENDENCYLINK = root + Constants.SCHE_CRITICAL_DEPENDENCIES_GET_LIST;
	public static final String SUBCONTRACTLINK = root + Constants.SCHE_SUBCONTRACT_GET_LIST;
	public static final String FINANCELINK = root + Constants.SCHE_FINANCIAL_PLAN_GET_LIST;
	public static final String ISSUELINK = root + Constants.ISSUE;
	public static final String DPLINK = root + Constants.DEFECT_LOG;
	public static final String CONTROLLINK_NCMS = root + Constants.GET_NCMS;
	public static final String CONTROLLINK_CALLLOG = root + Constants.GET_CALLLOG;
	//status of SQA tasks
	public static final double STATUS_NONE = 0;
	public static final double STATUS_PASS = 1;
	public static final double STATUS_PENDING = 2;
	public static final double STATUS_NOT_PASSED = 3;
	public static final double STATUS_CANCELLED = 4;
	public static final String [] statusSQA = {"N/A", "Passed", "Pending","Not passed","Cancelled"};
	// Change status SQA by Risk management
	public static final String [] statusSQA_Updated = {"", "Passed", "Pending","In-progress", "Cancelled", "Open"};
	public static final String [] statusSQA_UpdatedAll = {"N/A", "Passed", "Pending","Not passed", "Cancelled", "Open", "In-progress"};
	
	public static final String [] statusOrg = {"N/A","Closed","Open"};
	
	
	public String type;
	public String desc;
	public Date planDate;
	public Date actualDate;
	//for groups
	public Date rePlanDate;
	public long prjID;
	public long wuID;//used for drill down the project
	public long parentwuID;//used for org
	public String prjCode;
	public String note;
	public String grpName;
	public long taskID;
	public int processID;
	public boolean manualTask = false;
	//for SQA tasks
	
	public long assignedTo;
	public String assignedToStr;
	public double effort = Double.NaN;
	public int status = 0;
	public int typeID;
	public double metricVal = Double.NaN;//used to pass the value of TC coverage
	//for decisions:
	public boolean feasible = false;
	public String code;
	
	
	/**
	 * returns a copy of the object
	 * 
	 */
	public TaskInfo copy(){
		TaskInfo ret = new TaskInfo();
		ret.type = type;
		ret.desc = desc;
		if (planDate != null)
		ret.planDate = new Date(planDate.getTime());
		if (actualDate != null)
		ret.actualDate = new Date(actualDate.getTime());
		if (rePlanDate != null)
		ret.rePlanDate = new Date(rePlanDate.getTime());
		ret.prjID = prjID;
		ret.wuID = wuID;//used=for=drill=down=the=project
		ret.parentwuID = parentwuID;//used=for=org
		ret.prjCode = prjCode;
		ret.note = note;
		ret.grpName = grpName;
		ret.taskID = taskID;
		ret.processID = processID;
		ret.manualTask = manualTask;
		ret.assignedTo = assignedTo;
		ret.assignedToStr = assignedToStr;
		ret.effort = effort;
		ret.status = status;
		ret.typeID = typeID;
		ret.metricVal = metricVal;
		ret.feasible = feasible;
		ret.code = code;
		return ret;
	}
	public TaskInfo(){
		this.wuID = 0;
		this.desc = "";
		this.assignedToStr = "";
		this.effort = Double.NaN;
		this.planDate = null;
		this.actualDate = null;
		this.status = 0;
		this.typeID = 0;
		this.note = "";
	}
	public TaskInfo(String aType,String aDesc,Date  pd,Date ad){
		type = aType;
		desc = aDesc;
		planDate = pd;
		actualDate = ad;
		typeID = getTypeID(aType);
	}
	
	//for groups
	public TaskInfo(ProjectInfo prj, String aType,String aDesc,Date  pd,Date  rpd,Date ad,String anote){
		parentwuID = prj.getParent();
		prjID = prj.getProjectId();
		prjCode = prj.getProjectCode();
		wuID = prj.getWorkUnitId();
		grpName = prj.getGroupName();
		type = aType;
		desc = aDesc;
		planDate = pd;
		rePlanDate = rpd;
		actualDate = ad;
		note = anote;
		typeID = getTypeID(aType);
		status = actualDate == null ?(int)STATUS_PENDING:(int)STATUS_PASS;
	}
	public String getLink(){
			if (type.equals(MILESTONE))
				return MILESTONELINK;
			if (type.equals(TEST))
				return TESTLINK;
			if (type.equals(REVIEW))
				return REVIEWLINK;
			if (type.equals(RELEASE))
				return RELEASELINK;
			if (type.equals(DELIVERY))
				return DELIVERYLINK;
			if (type.equals(OTHERQUALITY)||type.equals(FINAL_INSPECTION)||type.equals(BASELINE)||type.equals(INTERNAL_AUDIT)||type.equals(QUALITY_GATE_INSPECTION)||type.equals(REVIEW2))
				return OTHERQUALITYLINK;
			if (type.equals(TRAINING))
				return TRAININGLINK;
			if (type.equals(OTHERQUALITY))
				return OTHERQUALITYLINK;
			if (type.equals(TRAINING))
				return TRAININGLINK;
			if (type.equals(DEPENDENCY))
				return DEPENDENCYLINK;
			if (type.equals(SUBCONTRACT))
				return SUBCONTRACTLINK;
			if (type.equals(FINANCE))
				return FINANCELINK;
			if (type.equals(ISSUE))
				return ISSUELINK;
			if (type.equals(DP)||type.equals(DP_SQA))
				return DPLINK;
			if (type.equals(CONTROL)){
				return (desc.startsWith(Ncms.CALL_LOG_PREFIX)?CONTROLLINK_CALLLOG:CONTROLLINK_NCMS);
			}
				
			return "";
	}
	public static int getTypeID(String constant){
		for (int i = 0; i < types.length; i++)
			if (types[i] == constant)
				return i;
		return -1;
	
	
	}
}
