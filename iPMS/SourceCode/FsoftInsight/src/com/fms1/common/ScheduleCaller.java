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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fms1.infoclass.*;
import com.fms1.web.Constants;
import com.fms1.web.Fms1Servlet;
import com.fms1.tools.*;
/**
 * Shedule menu sub-pages
 *
 */
public final class ScheduleCaller{
	/**
	 * Get all data for schedule.
	 */
	public static final void doGetScheInformation(final HttpServletRequest request, final HttpServletResponse response)
	{
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			// get Stage list
			final Vector stageVt = Schedule.getStageList(prjID);
			// get test&review activities
			final Vector test_review_Vt = WorkProduct.getModuleListSchedule(prjID, WorkProduct.ORDER_BY_PRELEASE);
				/*** get Header***/
			final ScheduleHeaderInfo headerInfo = Schedule.getSchHeader(prjID);
			//Caculate average(stage deviation)for Header
			float sum = 0;
			int count = 0;
			for (int i = 0; i < stageVt.size(); i++) {
				final StageInfo info = (StageInfo) stageVt.get(i);
				if (!info.deviation.equalsIgnoreCase("N/A")) {
					sum += Float.parseFloat(info.deviation);
					count++;
				}
			}
			if (count != 0) {
				headerInfo.stageSchDev = sum / count;
			}
			//Caculate average(test&review deviation) for Header
			sum = 0;
			count = 0;
			ModuleInfo info;
			for (int i = 0; i < test_review_Vt.size(); i++) {
				info = (ModuleInfo) test_review_Vt.get(i);
				if (!Double.isNaN(info.deviation) && info.isDel) {
					sum += (float)info.deviation;
					count++;
				}
			}
			if (count != 0) {
				headerInfo.delSchDev = sum / count;
			}
			session.setAttribute("scheduleHeaderInfo", headerInfo);
			Fms1Servlet.callPage("scheInformation.jsp",request,response);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static final void doGetScheSubcontract(final HttpServletRequest request, final HttpServletResponse response)
	{
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			session.setAttribute("caller", String.valueOf(Constants.SCHEDULE_CALLER));
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			// get Subcontract list
			final Vector subcontractVt = Project.getPLSubcontractList(prjID);
			session.setAttribute("subcontractVector", subcontractVt);
			Fms1Servlet.callPage("scheSubcontract.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static final void doGetScheStage(final HttpServletRequest request, final HttpServletResponse response)
	{
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			session.setAttribute("caller", String.valueOf(Constants.SCHEDULE_CALLER));
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			final ProjectInfo prjInfo = Project.getProjectInfo(prjID);

            // Following function call is not necessary and expensive call
            // because Project.getPerformanceMetrics will calculate all other
            // metrics => use project information to get durration
			//final Vector performanceVector = Project.getPerformanceMetrics(prjID);
			//session.setAttribute("WOPerformanceVector", performanceVector);
            MetricInfo durationMetric = prjInfo.getDurationMetric();
            session.setAttribute("durationMetric", durationMetric);

			final Vector stageVt = Schedule.getStageList(prjID);
			session.setAttribute("stageVector", stageVt);

			final Vector iterationList = Project.getPLIterationList(prjID);
			session.setAttribute("PLIterationList", iterationList);

			final Vector milestoneList = Project.getMilestoneListByProject(prjID);
			session.setAttribute("plMilestoneList", milestoneList);

			Fms1Servlet.callPage("scheStage.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static final void doGetRevTestView(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector moduleList = (Vector)session.getAttribute("moduleList");
			WPSizeInfo moduleInfo = (WPSizeInfo)moduleList.elementAt(Integer.parseInt(request.getParameter("vtID")));
			getRevTest(request,moduleInfo.moduleID,0);
			Fms1Servlet.callPage("moduleDetails.jsp?vtID=0",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doGetRevTestUpdate(final HttpServletRequest request, final HttpServletResponse response,long moduleID) {
		try {
			String fromPage = request.getParameter("fromPage");
			if (fromPage != null) {
				request.setAttribute("fromPage",fromPage);
				getRevTest(request,moduleID,1);
			} else {
				getRevTest(request,moduleID,0);
			}
			
			Fms1Servlet.callPage("moduleUpdate.jsp?vtID=0",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final void getRevTest(final HttpServletRequest request,long moduleID, int iFrom) {
		try {
			final HttpSession session = request.getSession();
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			if (iFrom == 0)	session.setAttribute("caller", String.valueOf(Constants.SCHEDULE_CALLER));
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			final Vector vtDeveloper = UserHelper.getAllUsers();
			session.setAttribute("devVector", vtDeveloper);
			Vector test_review_Vt;
			if (moduleID>0){
			// get test&review activities for only one module
				test_review_Vt = WorkProduct.getModuleSchedule(prjID, moduleID);
			}
			else{
				final String orderBy = request.getParameter("orderBy");
				String strOrderBy = WorkProduct.ORDER_BY_ARELEASE;
				if (orderBy != null) {
					switch (Integer.parseInt(orderBy)) {
						case 1: strOrderBy=WorkProduct.ORDER_BY_DELIVERABLE;break;
						case 2: strOrderBy=WorkProduct.ORDER_BY_PRELEASE;break;
						case 3: strOrderBy=WorkProduct.ORDER_BY_ARELEASE;break;
					}
				}
				// get test&review activities
				test_review_Vt = WorkProduct.getModuleListSchedule(prjID, strOrderBy);
				session.setAttribute("sortBy", orderBy);
			}
			session.setAttribute("moduleVector", test_review_Vt);
			Vector userList = Assignments.getAllUserAssignment(prjID, "", "", 0);
			session.setAttribute("userList", userList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doGetRevTestUpdate2(final HttpServletRequest request, final HttpServletResponse response,Vector vModule) {
		try {
			getRevTest2(request,vModule);
			Fms1Servlet.callPage("moduleUpdate.jsp?vtID=0",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final void getRevTest2(final HttpServletRequest request,Vector vModule) {
		try {
			final HttpSession session = request.getSession();
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			session.setAttribute("caller", String.valueOf(Constants.SCHEDULE_CALLER));
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			final Vector vtDeveloper = UserHelper.getAllUsers();
			session.setAttribute("devVector", vtDeveloper);
			Vector test_review_Vt = new Vector();
			Vector allTest_review_Vt = new Vector();
			
			int mSize = 0;
			boolean updateOK = true;
			WPSizeInfo moduleInfo = new WPSizeInfo();
			if (vModule != null) mSize = vModule.size();
			
			for (int i = 0; i< mSize; i++) {
				moduleInfo = (WPSizeInfo) vModule.elementAt(i);
				updateOK = moduleInfo.updateOK;
				if (updateOK){
				// get test&review activities for only one module
					test_review_Vt = WorkProduct.getModuleSchedule(prjID, moduleInfo.moduleID);
				}
				else{
					final String orderBy = request.getParameter("orderBy");
					String strOrderBy = WorkProduct.ORDER_BY_ARELEASE;
					if (orderBy != null) {
						switch (Integer.parseInt(orderBy)) {
							case 1: strOrderBy=WorkProduct.ORDER_BY_DELIVERABLE;break;
							case 2: strOrderBy=WorkProduct.ORDER_BY_PRELEASE;break;
							case 3: strOrderBy=WorkProduct.ORDER_BY_ARELEASE;break;
						}
					}
					// get test&review activities
					test_review_Vt = WorkProduct.getModuleListSchedule(prjID, strOrderBy);
					session.setAttribute("sortBy", orderBy);
				}
				allTest_review_Vt.addAll(test_review_Vt);
			}
			session.setAttribute("moduleVector", allTest_review_Vt);
			Vector userList = Assignments.getAllUserAssignment(prjID, "", "", 0);
			session.setAttribute("userList", userList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doGetScheReviewAndTest(final HttpServletRequest request, final HttpServletResponse response)
	{
		try {
			getRevTest(request,0,0);
			Fms1Servlet.callPage("scheReviewAndTestAct.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static final void doGetScheOtherQuality(final HttpServletRequest request, final HttpServletResponse response)
	{
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			session.setAttribute("caller", String.valueOf(Constants.SCHEDULE_CALLER));
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			final Vector vtDeveloper = UserHelper.getAllUsers();
			session.setAttribute("devVector", vtDeveloper);

			// Other Quality Activities
			final Vector vtOtherAct = QualityObjective.getOtherActivityList(prjID);
			session.setAttribute("otherActVector", vtOtherAct);
			Vector userList = Assignments.getAllUserAssignment(prjID, "", "", 0);
			session.setAttribute("userList", userList);

			Fms1Servlet.callPage("scheOtherQualityAct.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static final void doGetScheTrainingPlan(final HttpServletRequest request, final HttpServletResponse response)
	{
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			session.setAttribute("caller", String.valueOf(Constants.SCHEDULE_CALLER));
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			final Vector vtTrain = Project.getTrainingList(prjID);
			session.setAttribute("trainingVector", vtTrain);
			Fms1Servlet.callPage("scheTrainingPlan.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static final void doGetScheCriticalDependencies(final HttpServletRequest request, final HttpServletResponse response)
	{
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			session.setAttribute("caller", String.valueOf(Constants.SCHEDULE_CALLER));
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			final Vector dependencyList = Project.getPLDependencyList(prjID);
			session.setAttribute("PLDependencyList", dependencyList);
			Fms1Servlet.callPage("scheCriticalDependencies.jsp",request,response);
			}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static final void doGetScheFinancialPlan(final HttpServletRequest request, final HttpServletResponse response)
	{
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			session.setAttribute("caller", String.valueOf(Constants.SCHEDULE_CALLER));
			final Vector vtFinan = Project.getFinanList(prjID);
			session.setAttribute("finanVector", vtFinan);
			Fms1Servlet.callPage("scheFinancialPlan.jsp",request,response);
			}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 *  Actual start and actual end date of project Update
	 */
	public static final void doUpdateScheduleHeader(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			final String aStartD = request.getParameter("txtAStartD").trim();
			final String aEndD = request.getParameter("txtAEndD").trim();
			final ScheduleHeaderInfo oldInfo =
				(ScheduleHeaderInfo) session.getAttribute("scheduleHeaderInfo");							
			final ScheduleHeaderInfo info = new ScheduleHeaderInfo();
			info.prjID = prjID;
			
			if (!aStartD.equalsIgnoreCase("")) {
				info.aStartD = CommonTools.parseSQLDate(aStartD);
			}
			if (!aEndD.equalsIgnoreCase("")) {
				info.aEndD = CommonTools.parseSQLDate(aEndD);
			}
			if (WorkProduct.updatePrjDate(info)) {
				doGetScheInformation(request, response);
				return;
			}
			else {						
				oldInfo.aStartD = info.aStartD;
				oldInfo.aEndD = info.aEndD; 
				session.setAttribute("scheduleHeaderInfo",oldInfo);
				
				Fms1Servlet.callPage("scheInformation.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Add new subcontract to SUBCONTRACT
	 */
	public static final void doAddSubcontract(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final SubcontractInfo info = new SubcontractInfo();
			final String prjIDstr = (String) session.getAttribute("projectID");
			info.projectPlanID = Long.parseLong(prjIDstr);
			info.sName = request.getParameter("sName").trim();
			info.contactP = request.getParameter("sContactP").trim();
			info.refToContract = request.getParameter("refToContract").trim();
			info.note = request.getParameter("txtNote").trim();
			info.deliverable = request.getParameter("txtDeliverable").trim();
			final String aDeliveryD = request.getParameter("txtADeliveryD").trim();
			final String pDeliveryD = request.getParameter("txtPDeliveryD").trim();
			info.plannedDeliveryDate =CommonTools.parseSQLDate(pDeliveryD);
			if (!aDeliveryD.equals("")) {
				info.actualDeliveryDate= CommonTools.parseSQLDate(aDeliveryD);
			}
			int caller = Integer.parseInt((String)session.getAttribute("caller"));
			Project.addPLSubcontract(info);
			ProjectPlanCaller.addChangeAuto(info.projectPlanID, Constants.ACTION_ADD, Constants.PL_ORGANIZATION, "Subcontract>" + info.job, null, null);
			if (caller==Constants.SCHEDULE_CALLER)
				doGetScheSubcontract(request, response);
			else
				ProjectPlanCaller.doLoadPLStructure(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Subcontract update
	 */
	public static final void doUpdateSubcontract(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			
			final HttpSession session = request.getSession();
			int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			int caller = Integer.parseInt((String)session.getAttribute("caller"));
			final SubcontractInfo subcontractInfo = new SubcontractInfo();
			final String substractIDstr = request.getParameter("txtSubcontractID").trim();
			subcontractInfo.subcontractID = Long.parseLong(substractIDstr);
			subcontractInfo.sName = request.getParameter("sName").trim();
			subcontractInfo.contactP = request.getParameter("sContactP").trim();
			subcontractInfo.refToContract = request.getParameter("refToContract").trim();
			subcontractInfo.note = request.getParameter("txtNote").trim();
			subcontractInfo.deliverable = request.getParameter("txtDeliverable").trim();
			final String aDeliveryD = request.getParameter("txtADeliveryD").trim();
			final String pDeliveryD = request.getParameter("txtPDeliveryD").trim();
			subcontractInfo.plannedDeliveryDate=CommonTools.parseSQLDate(pDeliveryD);
			if (!aDeliveryD.equalsIgnoreCase("")) {
				subcontractInfo.actualDeliveryDate= CommonTools.parseSQLDate(aDeliveryD);
			}
			Project.updatePLSubcontract(subcontractInfo);
			Vector vt=(Vector)session.getAttribute("subcontractVector");
			int vtID=Integer.parseInt(request.getParameter("vtID"));
			SubcontractInfo oldSubcontractInfo = (SubcontractInfo)vt.get(vtID);
			String newValue, oldValue;
			newValue = subcontractInfo.job;
			oldValue = oldSubcontractInfo.job;
			if (!newValue.equalsIgnoreCase(oldValue))
				ProjectPlanCaller.addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_ORGANIZATION,
					"Subcontract>job",
					newValue,
					oldValue);

			newValue = subcontractInfo.deliverable;
			oldValue = oldSubcontractInfo.deliverable;
			if (!newValue.equalsIgnoreCase(oldValue))
				ProjectPlanCaller.addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_ORGANIZATION,
					"Subcontract>deliverable",
					newValue,
					oldValue);

			newValue = CommonTools.dateFormat(new java.util.Date(subcontractInfo.plannedDeliveryDate.getTime()));
			oldValue = CommonTools.dateFormat(new java.util.Date(oldSubcontractInfo.plannedDeliveryDate.getTime()));
			if (!newValue.equalsIgnoreCase(oldValue))
				ProjectPlanCaller.addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_ORGANIZATION,
					"Subcontract>Planned delivery date",
					newValue,
					oldValue);
			if (caller==Constants.SCHEDULE_CALLER)
				doGetScheSubcontract(request, response);
			else
				ProjectPlanCaller.doLoadPLStructure(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Delete subcontract
	 */
	public static final void doDeleteSubcontract(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			int caller = Integer.parseInt((String)session.getAttribute("caller"));
			Vector vt=(Vector)session.getAttribute("subcontractVector");
			int vtID=Integer.parseInt(request.getParameter("vtID"));
			SubcontractInfo oldSubcontractInfo = (SubcontractInfo)vt.get(vtID);
			Project.deleteSubcontract(oldSubcontractInfo.subcontractID);
			ProjectPlanCaller.addChangeAuto(
					prjID,
					Constants.ACTION_DELETE,
					Constants.PL_ORGANIZATION,
					"Subcontract>" + oldSubcontractInfo.job,
					null,
					null);
			if (caller==Constants.SCHEDULE_CALLER)
				doGetScheSubcontract(request, response);
			else
				ProjectPlanCaller.doLoadPLStructure(request, response);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Add new stage
	 */
	public static final void doAddStage(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final StageInfo info = new StageInfo();
			info.prjID = Long.parseLong((String) session.getAttribute("projectID"));
			info.stage = request.getParameter("txtStage").trim();
			info.bEndD =CommonTools.parseSQLDate(request.getParameter("txtBEndD").trim());
			info.pEndD = CommonTools.parseSQLDate(request.getParameter("txtPEndD").trim());
			info.aEndD = CommonTools.parseSQLDate(request.getParameter("txtAEndD").trim());
			info.description = request.getParameter("txtDescription").trim();
			info.StandardStage = Integer.parseInt(request.getParameter("cmbStandardStage"));
			info.milestone = request.getParameter("txtMilestone").trim();
			WorkProduct.addStage(info);
			ProjectPlanCaller.addChangeAuto(info.prjID,Constants.ACTION_ADD,Constants.PL_LIFECYCLE,"Stage list>"+info.stage,null,null);
		
			/**
			 * Closing stage ->backups planned RCRs & effort
			 */
			/*
			if ( info.aEndD!=null){
				ProjectInfo pinf=new ProjectInfo(info.prjID);
				Vector stageList = Schedule.getStageList(pinf);
				for (int i=0;i<stageList.size();i++)
					if (info.stage.equals(((StageInfo)stageList.elementAt(i)).stage)){
						Requirement.baselineRCRandEffort(pinf,stageList,i);
						break;
					}
			}
			*/
			if ("1".equals( request.getParameter("source"))) 
				//ProjectPlanCaller.doLoadPLLifecycle(request, response);	// landd comment
				WorkOrderCaller.doLoadDeliverableList(request, response);
			else 
				doGetScheStage(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Stage update
	 */
	public static final void doUpdateStage(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			final StageInfo info = new StageInfo();
			info.milestoneID = Long.parseLong(request.getParameter("txtMilestoneID").trim());
			info.stage = request.getParameter("txtStage").trim();
			info.bEndD = CommonTools.parseSQLDate(request.getParameter("txtBEndD").trim());
			info.pEndD = CommonTools.parseSQLDate(request.getParameter("txtPEndD").trim());
			info.aEndD = CommonTools.parseSQLDate(request.getParameter("txtAEndD").trim());
			info.description = request.getParameter("txtDescription").trim();
			info.milestone = request.getParameter("txtMilestone").trim();
			info.StandardStage = Integer.parseInt(request.getParameter("cmbStandardStage"));
			WorkProduct.updateStage(info);
			/**
			 * update changes of PPlan
			 */
			String source = request.getParameter("source");
			//GET OLD INFO
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));

			//Get stage vector
			Vector stageList = (Vector)session.getAttribute("stageVector");

			String vtIDstr = request.getParameter("vtIDstr");

			//Get index of stage info
			int vtID=Integer.parseInt(vtIDstr);
			StageInfo oldInfo=(StageInfo)stageList.get(vtID);
			String oldValue,newValue;

			oldValue = oldInfo.stage;
			newValue = info.stage;
			if (!oldValue.equalsIgnoreCase(newValue))
				ProjectPlanCaller.addChangeAuto(prjID,Constants.ACTION_UPDATE,Constants.PL_LIFECYCLE,"Stage list>stage",newValue,oldValue);

			oldValue = CommonTools.dateFormat(oldInfo.bEndD);
			newValue = CommonTools.dateFormat(info.bEndD);
			if (!oldValue.equalsIgnoreCase(newValue))
				ProjectPlanCaller.addChangeAuto(prjID,Constants.ACTION_UPDATE,Constants.PL_LIFECYCLE,"Stage list>planned end date",newValue,oldValue);

			oldValue = oldInfo.description;
			newValue = info.description;
			if (!oldValue.equalsIgnoreCase(newValue))
				ProjectPlanCaller.addChangeAuto(prjID,Constants.ACTION_UPDATE,Constants.PL_LIFECYCLE,"Stage list>description",newValue,oldValue);

			oldValue = oldInfo.milestone;
			newValue = info.milestone;
			if (!oldValue.equalsIgnoreCase(newValue))
				ProjectPlanCaller.addChangeAuto(prjID,Constants.ACTION_UPDATE,Constants.PL_LIFECYCLE,"Stage list>milestone",newValue,oldValue);
			
			/**
			 * Closing stage ->backups planned RCRs
			 */
			/*
			if (oldInfo.aEndD==null && info.aEndD!=null){
				ProjectInfo pinf=new ProjectInfo(prjID);
				Requirement.baselineRCRandEffort(pinf,stageList,vtID);
			}
			*/
			
			if ("1".equals(source)) 
				//ProjectPlanCaller.doLoadPLLifecycle(request, response); // landd comment
				WorkOrderCaller.doLoadDeliverableList(request, response);				
			else
				doGetScheStage(request, response);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Delete stage
	 */
	public static final void doDeleteStage(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			final String milestoneIDstr = request.getParameter("txtMilestoneID").trim();
			final long milestoneID = Long.parseLong(milestoneIDstr);
			if (WorkProduct.deleteStage(milestoneID)) {
				//Get project ID
				final String prjIDstr = (String) session.getAttribute("projectID");
				final long prjID = Long.parseLong(prjIDstr);
				//Get stage vector
				Vector vt = (Vector)session.getAttribute("stageVector");
				String vtIDstr=request.getParameter("vtID");
				//Get index of stage info
				int vtID=Integer.parseInt(vtIDstr);
				StageInfo info=(StageInfo)vt.get(vtID);
				ProjectPlanCaller.addChangeAuto(prjID,Constants.ACTION_DELETE,Constants.PL_LIFECYCLE,"Stage list>"+info.stage,null,null);
				String source = request.getParameter("source");
				if ("1".equals(source)) 
					//ProjectPlanCaller.doLoadPLLifecycle(request, response);   // landd comment
					WorkOrderCaller.doLoadDeliverableList(request, response);	// landd add
				else 
					doGetScheStage(request, response);
			}
			else {
				Fms1Servlet.callPage("error.jsp?error=Could not delete stage",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateModule(final HttpServletRequest request, final HttpServletResponse response) {
			WorkProductCaller.doUpdateModule(request);
			doGetScheReviewAndTest(request, response);
	}
	public static final void doGetScheSizeInput(final HttpServletRequest request, final HttpServletResponse response){
		try {
			HttpSession session =request.getSession();
			long modid= Long.parseLong(request.getParameter("modID"));
			long projID= Long.parseLong((String)session.getAttribute("projectID"));
			Vector moduleList=WorkProduct.getModuleSize(projID,modid);
			ModuleCaller.modulePrep(session);
			session.setAttribute("moduleList", moduleList);
			Fms1Servlet.callPage("moduleNSizeUpdate.jsp?vtID=0&fromsched=1",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	/********************************OtherActivity********************************************/

	public static final void doAddOtherAct(final HttpServletRequest request, final HttpServletResponse response) {
			WorkProductCaller.doAddOtherAct(request);
			doGetScheOtherQuality(request, response);
	}
	public static final void doUpdateOtherAct(final HttpServletRequest request, final HttpServletResponse response) {
			WorkProductCaller.doUpdateOtherAct(request, response);
			doGetScheOtherQuality(request, response);
	}
	public static final void doDeleteOtherAct(final HttpServletRequest request, final HttpServletResponse response) {
			WorkProductCaller.doDeleteOtherAct(request, response);
			doGetScheOtherQuality(request, response);
	}

	/***************************************Financial plan*****************************************/
	public static final void doAddFinan(final HttpServletRequest request, final HttpServletResponse response) {
			FinanCaller.addFinan(request, response);
			doGetScheFinancialPlan(request, response);
	}
	public static final void doUppdateFinan(final HttpServletRequest request, final HttpServletResponse response) {
			FinanCaller.updateFinan(request, response);
			doGetScheFinancialPlan(request, response);
	}
	public static final void doDeleteFinan(final HttpServletRequest request, final HttpServletResponse response) {
			FinanCaller.deleteFinan(request, response);
			doGetScheFinancialPlan(request, response);
	}
	/**************************************Dependencies********************************************/
	public static final void doAddPLDependency(final HttpServletRequest request, final HttpServletResponse response){
			ProjectPlanCaller.addPLDependency(request, response);
			doGetScheCriticalDependencies(request, response);
	}
	public static final void doUpdatePLDependency(final HttpServletRequest request, final HttpServletResponse response){
			ProjectPlanCaller.updatePLDependency(request, response);
			doGetScheCriticalDependencies(request, response);
	}
	public static final void doDeletePLDependency(final HttpServletRequest request, final HttpServletResponse response){
			ProjectPlanCaller.deletePLDependency(request, response);
			doGetScheCriticalDependencies(request, response);
	}
}