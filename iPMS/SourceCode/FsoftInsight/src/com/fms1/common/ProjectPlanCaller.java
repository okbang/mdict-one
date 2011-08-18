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

import java.lang.reflect.Array;
import java.sql.Date;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fms1.tools.*;
import com.fms1.html.WUCombo;
import com.fms1.infoclass.*;
import com.fms1.web.Constants;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.Http;
import com.fms1.web.StringConstants;
import java.util.StringTokenizer;
import java.util.Hashtable;


/**
 * Project plan pages
 *
 */

public final class ProjectPlanCaller {

	// ------------------------------Project Description------------------------------
	public static final void doLoadPLProjectOverview(final HttpServletRequest request, final HttpServletResponse response) {
		doLoadPLProjectOverview(request, response, "");
	}

	public static final void doLoadPLProjectOverview(final HttpServletRequest request, final HttpServletResponse response, String index) {
		try {
			final HttpSession session = request.getSession();
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			final ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			session.setAttribute("PLGeneralInfo", projectInfo);
			final EffortTypeInfo effortTypeInfo = Effort.getEffortType(prjID);
			session.setAttribute("PLEffortInfo", effortTypeInfo);
			final Vector constraintList = Project.getConstraintList(prjID);
			session.setAttribute("PLConstraintList", constraintList);
			final Vector assumptionList = Project.getAssumptionList(prjID);
			session.setAttribute("PLAssumptionList", assumptionList);
			final Vector riskList = Risk.getRiskList(prjID);
			session.setAttribute("riskList", riskList);
			final Vector referenceList = Project.getPLReferenceList(prjID);
			session.setAttribute("PLReferenceList", referenceList);
			final ProjectSizeInfo sizeInfo = new ProjectSizeInfo(prjID);
			session.setAttribute("TotalEstimatedSize", CommonTools.formatDouble(sizeInfo.totalPlannedSize));
			double targetDuration = CommonTools.dateDiff(projectInfo.getPlanStartDate(), projectInfo.getPlannedFinishDate());
			final String duration = CommonTools.formatDouble(targetDuration);
			session.setAttribute("duration", duration);
			Fms1Servlet.callPage("plOverview.jsp" + index, request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ------------------------------End Project Description------------------------------

	// ------------------------------PL Constraint------------------------------
	public static final void doAddPLConstraint(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final int type = Integer.parseInt(request.getParameter("plType"));
			String description = request.getParameter("plDescription");
			if (description == null) {
				description = "";
			}

			// landd add start
			String note = request.getParameter("plNote");
			if (note == null) {
				note = "";
			}
			// landd add end

			final ConstraintInfo constraintInfo = new ConstraintInfo();
			constraintInfo.prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			constraintInfo.description = description;
			constraintInfo.note = note;		// landd add
			constraintInfo.isConstraint = 1;
			constraintInfo.type = type;
			Project.addCon_Ass(constraintInfo);
			addChangeAuto(prjID, Constants.ACTION_ADD, Constants.PL_OVERVIEW, "Constraint>" + description, null, null);
			if (Integer.parseInt((String)session.getAttribute("caller"))==Constants.PROJECT_PLAN_CALLER)
				doLoadPLProjectOverview(request, response, "");
			else
				WorkOrderCaller.doLoadGeneralInfo(request, response, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doExportPP(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));

//			String ProjectOverview = request.getParameter("ProjectOverview");
//			String ProjectDevelopmentApproach = request.getParameter("ProjectDevelopmentApproach");
//			String Estimate = request.getParameter("Estimate");
//			String ProjectOrganization = request.getParameter("ProjectOrganization");
//			String Communication_N_Reporting = request.getParameter("Communication_N_Reporting");
//			String SecurityAspects = request.getParameter("SecurityAspects");

			String ProjectOverview = "true";
			String ProjectDevelopmentApproach = "true";
			String Estimate = "true";
			String ProjectOrganization = "true";
			String Communication_N_Reporting = "true";
			String SecurityAspects = "true";
			
			session.setAttribute("ProjectOverview", ProjectOverview);
			session.setAttribute("ProjectDevelopmentApproach", ProjectDevelopmentApproach);
			session.setAttribute("Estimate", Estimate);
			session.setAttribute("ProjectOrganization", ProjectOrganization);
			session.setAttribute("Communication_N_Reporting", Communication_N_Reporting);
			session.setAttribute("SecurityAspects", SecurityAspects);

			if(ProjectOverview.equals("true")){
				Vector performanceVector = Project.getPerformanceMetrics(prjID);
				session.setAttribute("WOPerformanceVector", performanceVector);
				Vector stdMetrics = Project.getStandardMetricList(prjID);
				session.setAttribute("WOStandardMetricMatrix", stdMetrics);
				
				// anhtv08-start
				// get milestoneID
				Vector stageList = Schedule.getStageList(prjID);
				StageInfo stageInfo= null;
				if(stageList!=null)
				{
					for (int i = stageList.size() - 1; i >= 0; i--) {
						stageInfo = (StageInfo) stageList.elementAt(i);
						if ((stageInfo.aEndD != null)
							&& (stageInfo.actualBeginDate != null)
							&& (stageInfo.plannedEndDate != null)
							&& (stageInfo.plannedBeginDate != null)) {
		
							break;
						}
					}
				}
				if(stageInfo!=null)
				{
					Vector cmList = Project.getCustomerMetric(prjID);
					if(cmList==null)
					{
						cmList = new Vector();
					}
					session.setAttribute("WOCustomeMetricList", cmList);
					
				}
				
				//end
				Vector dpVt = Defect.getDPTask(prjID);
				if(dpVt==null)
				{
					dpVt= new Vector();
					
				}
				session.setAttribute("defectPrevention", dpVt);
				Vector dependencyList = Project.getPLDependencyList(prjID);
				session.setAttribute("PLDependencyList", dependencyList);

				final Vector riskList = Risk.getRiskList(prjID);
				session.setAttribute("riskList", riskList);

			}

			if(ProjectDevelopmentApproach.equals("true")){
				// 2.1 Project Process
				Vector tailoringList = Assets.getDeviationList(prjID, "");
				session.setAttribute("tailoringList", tailoringList);

				ReqChangesInfo info = Requirement.getChangesRequirement(prjID);
				if (info == null) info = new ReqChangesInfo();
				session.setAttribute("ReqChangesInfo",info);

				final Vector integrList = ProductIntegration.getIntegrationStratList(prjID);
				session.setAttribute("ProIntegrList", integrList);

				final Vector vtStrategyOfMeeting = QualityObjective.getStrategyOfMeetingList(prjID);
				session.setAttribute("StratOfMeetingList", vtStrategyOfMeeting);

				final Vector vtReviewStrategy = QualityObjective.getReviewStrategyList(prjID);
				session.setAttribute("ReviewStrategyList", vtReviewStrategy);

				final Vector vtMeasProg = MeasurementsProg.getMeasurementsProgList(prjID);
				session.setAttribute("MeasurementsProgList", vtMeasProg);

				final Vector vtTestStrategy = TestStrategy.getTestStrategyList(prjID);
				session.setAttribute("TestStrategyList", vtTestStrategy);
				

				Vector dependencyList = Project.getPLDependencyList(prjID);
				session.setAttribute("PLDependencyList", dependencyList);


				// 2.4.1. Estimates of Defects to be detected
				final DefectByProcessInfo[] defectProcess = Defect.getWeigthedDefectByOrigin(prjID);
				session.setAttribute("defectProcess", defectProcess);				
				
				final Vector moduleList = WorkProduct.getDefectModuleListSize(prjID);
				session.setAttribute("defectModuleList", moduleList);
			
				final Hashtable Process_WorkProduct =  WorkProduct.getProcessWorkProductList();
				session.setAttribute("Process_WP",Process_WorkProduct);
			}
			if(Estimate.equals("true")){
				
				final Vector moduleList = WorkProduct.getModuleListSize(prjID);
				session.setAttribute("moduleList", moduleList);				
				
				final Vector vProjectScheduleList = ProjectSchedule.getProjectScheduleList(prjID);
				session.setAttribute("ProjectScheduleList", vProjectScheduleList);
				
				final Vector vtToolList = Project.getToolList(prjID);
				session.setAttribute("toolVector", vtToolList);	
				
				Vector deliverableList = WorkProduct.getDeliverableList(prjID);
				session.setAttribute("deliverableList", deliverableList);
				
				final Vector stageVt = Schedule.getStageList(prjID);
				session.setAttribute("stageVector", stageVt);
				final Vector iterationList = Project.getPLIterationList(prjID);
				session.setAttribute("PLIterationList", iterationList);
				final Vector milestoneList = Project.getMilestoneListByProject(prjID);
				session.setAttribute("plMilestoneList", milestoneList);

				final Vector vtTrain = Project.getTrainingList(prjID);
				session.setAttribute("trainingVector", vtTrain);

			}
			if(ProjectOrganization.equals("true")){
				final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
				final String orgStructure = Project.getPLOrjStructure(prjPlanID);
				session.setAttribute("plOrgStructure", orgStructure);

				Vector teamList = Assignments.getWOTeamList(prjID, null, null);
				if (teamList == null) {
					teamList = new Vector();
				}
				session.setAttribute("WOTeamList", teamList);

				Vector subTeamList = SubTeams.getWOSubTeamList(prjID);
				if (subTeamList == null) {
					subTeamList = new Vector();
				}
				session.setAttribute("WOSubTeamList", subTeamList);

				final Vector interfaceList = Project.getPLInterfaceList(prjPlanID, 0);
				session.setAttribute("plInterfaceList", interfaceList);

				Vector tailoringList = Assets.getDeviationList(prjID, "");
				session.setAttribute("tailoringList", tailoringList);
				
				Vector subcontractVector = Project.getPLSubcontractList(prjID);
				session.setAttribute("subcontractVector", subcontractVector);
				
			}
			if(Communication_N_Reporting.equals("true")){
				final Vector vComList = ComReport.getCommunicationReportingList(prjID);
				session.setAttribute("ComReportList", vComList);				
			}
			
			final Vector referenceList = Project.getPLReferenceList(prjID);
			session.setAttribute("PLReferenceList", referenceList);
			
			Fms1Servlet.callPage("PP.jsp",request,response,Fms1Servlet.CONTENT_TYPE_WORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdatePLConstraint(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String)session.getAttribute("projectID"));
			final long constraintID = Long.parseLong(request.getParameter("plConstraint_ID"));
			Vector constraintList = (Vector)session.getAttribute("PLConstraintList");
			ConstraintInfo oldConstraintInfo = null;
			for(int i = 0; i < constraintList.size(); i++){
				oldConstraintInfo = (ConstraintInfo) constraintList.elementAt(i);
				if (oldConstraintInfo.constraintID==constraintID) {
					break;
				}
			}
			final int type = Integer.parseInt(request.getParameter("plType"));
			String description = request.getParameter("plDescription");
			if (description == null) {
				description = "";
			}

			// landd add start
			String note = request.getParameter("plNote");
			if (note == null) {
				note = "";
			}
			// landd add end

			final ConstraintInfo constraintInfo = new ConstraintInfo();
			constraintInfo.description = description;
			constraintInfo.note = note;		// landd add
			constraintInfo.isConstraint = 1;
			constraintInfo.type = type;
			constraintInfo.constraintID = constraintID;
			Project.updateCon_Ass(constraintInfo);

			// Automatically change
			String oldValue = oldConstraintInfo.description;
			String newValue = constraintInfo.description;
			if (!oldValue.equalsIgnoreCase(newValue)) {
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_OVERVIEW,
					"Constraint>Description",
					newValue,
					oldValue);
			}
			// landd add note start
			oldValue = oldConstraintInfo.note;
			newValue = constraintInfo.note;
			if (!oldValue.equalsIgnoreCase(newValue)) {
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_OVERVIEW,
					"Constraint>Note",
					newValue,
					oldValue);
			}
			// landd add note end

			oldValue = oldConstraintInfo.GetNameOfType();
			newValue = constraintInfo.GetNameOfType();
			if (!oldValue.equalsIgnoreCase(newValue)) {
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_OVERVIEW,
					"Constraint>Type",
					newValue,
					oldValue);
			}
			if (Integer.parseInt((String)session.getAttribute("caller"))==Constants.PROJECT_PLAN_CALLER) {
				doLoadPLProjectOverview(request, response, "");
			} else {
				WorkOrderCaller.doLoadGeneralInfo(request, response, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeletePLConstraint(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String)session.getAttribute("projectID"));
			final long constraintID = Long.parseLong(request.getParameter("plConstraint_ID"));
			Project.deleteCon_Ass(constraintID);

			// Automatically change
			Vector constraintList = (Vector)session.getAttribute("PLConstraintList");
			ConstraintInfo oldConstraintInfo = null;
			for(int i = 0; i < constraintList.size(); i++) {
				oldConstraintInfo = (ConstraintInfo)constraintList.elementAt(i);
				if (oldConstraintInfo.constraintID == constraintID) {
					break;
				}
			}
			addChangeAuto(
				prjID,
				Constants.ACTION_DELETE,
				Constants.PL_OVERVIEW,
				"Constraint>" + oldConstraintInfo.description,
				null,
				null);
			if (Integer.parseInt((String)session.getAttribute("caller"))==Constants.PROJECT_PLAN_CALLER) {
				doLoadPLProjectOverview(request, response, "");
			} else {
				WorkOrderCaller.doLoadGeneralInfo(request, response, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ------------------------------End PL Constraint------------------------------

	// ------------------------------PL Assumption------------------------------
	public static final void doAddPLAssumption(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String)session.getAttribute("projectID"));
			final int type = Integer.parseInt(request.getParameter("plType"));
			String description = request.getParameter("plDescription");
			if (description == null) {
				description = "";
			}

			// landd add start
			String note = request.getParameter("plNote");
			if (note == null) {
				note = "";
			}
			// landd add end

			final ConstraintInfo constraintInfo = new ConstraintInfo();
			constraintInfo.prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			constraintInfo.description = description;
			constraintInfo.note = note;		// landd add
			constraintInfo.isConstraint = 0;
			constraintInfo.type = type;
			Project.addCon_Ass(constraintInfo);
			addChangeAuto(prjID, Constants.ACTION_ADD, Constants.PL_OVERVIEW, "Assumption>" + description, null, null);
			if (Integer.parseInt((String)session.getAttribute("caller"))==Constants.PROJECT_PLAN_CALLER) {
				doLoadPLProjectOverview(request, response, "");
			} else {
				WorkOrderCaller.doLoadGeneralInfo(request, response, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLAssumption(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String)session.getAttribute("projectID"));
			final long constraintID = Long.parseLong(request.getParameter("plAssumption_ID"));
			final int type = Integer.parseInt(request.getParameter("plType"));
			String description = request.getParameter("plDescription");
			if (description == null) {
				description = "";
			}

			// landd add start
			String note = request.getParameter("plNote");
			if (note == null) {
				note = "";
			}
			// landd add end

			final ConstraintInfo constraintInfo = new ConstraintInfo();
			constraintInfo.description = description;
			constraintInfo.note = note;		// landd add
			constraintInfo.isConstraint = 0;
			constraintInfo.type = type;
			constraintInfo.constraintID = constraintID;
			Project.updateCon_Ass(constraintInfo);

			// Automatically change
			Vector constraintList = (Vector)session.getAttribute("PLAssumptionList");
			ConstraintInfo oldConstraintInfo = null;
			for(int i = 0; i < constraintList.size(); i++) {
				oldConstraintInfo = (ConstraintInfo)constraintList.elementAt(i);
				if (oldConstraintInfo.constraintID == constraintID) {
					break;
				}
			}
			String oldValue = oldConstraintInfo.description;
			String newValue = constraintInfo.description;
			if (!oldValue.equalsIgnoreCase(newValue)) {
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_OVERVIEW,
					"Assumption>Description",
					newValue,
					oldValue);
			}

			// landd add note start
			oldValue = oldConstraintInfo.note;
			if (oldValue==null) oldValue = "";

			newValue = constraintInfo.note;
			if (!oldValue.equalsIgnoreCase(newValue)) {
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_OVERVIEW,
					"Assumption>Note",
					newValue,
					oldValue);
			}
			// landd add note end

			oldValue = oldConstraintInfo.GetNameOfType();
			newValue = constraintInfo.GetNameOfType();
			if (!oldValue.equalsIgnoreCase(newValue)) {
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_OVERVIEW,
					"Assumption>Type",
					newValue,
					oldValue);
			}

			if (Integer.parseInt((String)session.getAttribute("caller"))==Constants.PROJECT_PLAN_CALLER) {
				doLoadPLProjectOverview(request, response, "");
			} else {
				WorkOrderCaller.doLoadGeneralInfo(request, response, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeletePLAssumption(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String)session.getAttribute("projectID"));
			final long assumptionID = Long.parseLong(request.getParameter("plAssumption_ID"));
			Project.deleteCon_Ass(assumptionID);

			// Automatically change
			Vector constraintList = (Vector)session.getAttribute("PLAssumptionList");
			ConstraintInfo oldConstraintInfo = null;
			for(int i = 0; i < constraintList.size(); i++) {
				oldConstraintInfo = (ConstraintInfo)constraintList.elementAt(i);
				if (oldConstraintInfo.constraintID == assumptionID) {
					break;
				}
			}
			addChangeAuto(
				prjID,
				Constants.ACTION_DELETE,
				Constants.PL_OVERVIEW,
				"Assumption>" + oldConstraintInfo.description,
				null,
				null);

			if (Integer.parseInt((String)session.getAttribute("caller"))==Constants.PROJECT_PLAN_CALLER) {
				doLoadPLProjectOverview(request, response, "");
			} else {
				WorkOrderCaller.doLoadGeneralInfo(request, response, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ------------------------------End PL Assumption------------------------------

	// ------------------------------PL Reference------------------------------
	public static final void doPreparePLReferenceUpdate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long refID = Long.parseLong(request.getParameter("plReference_refID"));
			final ReferenceInfo info = Project.getPLReference(refID);
			session.setAttribute("plReferenceInfo", info);
			Fms1Servlet.callPage("plReferenceUpdate.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPreparePLReferenceAdd(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plReferenceAdd.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAddPLReference(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			final String issuedDate = request.getParameter("plReference_issuedDate");
			String source = request.getParameter("plReference_source");
			if (source == null)
				source = "";
			String note = request.getParameter("plReference_note");
			if (note == null)
				note = "";
			String document = request.getParameter("plReference_document");
			if (document == null)
				document = "";
			final ReferenceInfo refInfo = new ReferenceInfo();
			if ((!issuedDate.equals("")) && (issuedDate != null)) {
				final java.util.Date iDate = CommonTools.parseDate(issuedDate);
				final java.sql.Date iSqlDate = new java.sql.Date(iDate.getTime());
				refInfo.issueDate = iSqlDate;
			}
			refInfo.projectPlanID = prjPlanID;
			refInfo.source = source;
			refInfo.note = note;
			refInfo.document = document;
			Project.addPLReference(refInfo);

			addChangeAuto(
				prjID,
				Constants.ACTION_ADD,
				Constants.PL_OVERVIEW,
				"Reference>" + refInfo.document,
				null,
				null);
			doLoadPLProjectOverview(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLReference(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			final long refID = Long.parseLong(request.getParameter("plReference_refID"));
			final String issuedDate = request.getParameter("plReference_issuedDate");
			String source = request.getParameter("plReference_source");
			if (source == null)
				source = "";
			String note = request.getParameter("plReference_note");
			if (note == null)
				note = "";
			String document = request.getParameter("plReference_document");
			if (document == null)
				document = "";
			final ReferenceInfo refInfo = new ReferenceInfo();
			if ((!issuedDate.equals("")) && (issuedDate != null)) {
				final java.util.Date iDate = CommonTools.parseDate(issuedDate);
				final java.sql.Date iSqlDate = new java.sql.Date(iDate.getTime());
				refInfo.issueDate = iSqlDate;
			}
			refInfo.referenceID = refID;
			refInfo.projectPlanID = prjPlanID;
			refInfo.source = source;
			refInfo.note = note;
			refInfo.document = document;
			Project.updatePLReference(refInfo);

			//automatically change
			ReferenceInfo oldRefInfo = (ReferenceInfo) session.getAttribute("plReferenceInfo");
			String oldValue = oldRefInfo.document;
			String newValue = refInfo.document;
			if (!oldValue.equalsIgnoreCase(newValue))
				addChangeAuto(prjID, Constants.ACTION_UPDATE, Constants.PL_OVERVIEW, "Document", newValue, oldValue);

			oldValue = CommonTools.dateFormat(new java.util.Date(oldRefInfo.issueDate.getTime()));
			newValue = CommonTools.dateFormat(new java.util.Date(refInfo.issueDate.getTime()));
			if (!oldValue.equalsIgnoreCase(newValue))
				addChangeAuto(prjID, Constants.ACTION_UPDATE, Constants.PL_OVERVIEW, "Issued date", newValue, oldValue);

			doLoadPLProjectOverview(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeletePLReference(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));

			final long refID = Long.parseLong(request.getParameter("plReference_refID"));
			Project.deletePLReference(refID);

			ReferenceInfo oldRefInfo = (ReferenceInfo) session.getAttribute("plReferenceInfo");
			addChangeAuto(
				prjID,
				Constants.ACTION_DELETE,
				Constants.PL_OVERVIEW,
				"Assumption>" + oldRefInfo.document,
				null,
				null);
			doLoadPLProjectOverview(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ------------------------------End of PL Reference------------------------------

	public static final void doLoadPLDevilerable_Dependencies(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		doLoadPLDevilerable_Dependencies(request, response, "");
	}
	public static final void doLoadPLDevilerable_Dependencies(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String index) {
		try {

			final HttpSession session = request.getSession();
			session.setAttribute("caller", String.valueOf(Constants.PROJECT_PLAN_CALLER));
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector deliverableList = WorkProduct.getDeliverableList(prjID);
			Vector dependencyList = Project.getPLDependencyList(prjID);
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			session.setAttribute("deliverableList", deliverableList);
			session.setAttribute("PLDependencyList", dependencyList);
			// Added by HaiMM - Start
			final Vector stageVt = Schedule.getStageList(prjID);
			session.setAttribute("stageVector", stageVt);
			// Added by HaiMM - End
			Fms1Servlet.callPage("plDeliverables_Dependencies.jsp" + index,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ------------------------------PL Dependency------------------------------
	public static final void doViewPLDependency(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long dependencyID = Long.parseLong(request.getParameter("plDependency_depID"));
			final DependencyInfo info = Project.getPLDependency(dependencyID);
			session.setAttribute("plDependencyInfo", info);
			Fms1Servlet.callPage("plDependencyView.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAddPLDependency(final HttpServletRequest request, final HttpServletResponse response) {
		addPLDependency(request, response);
		doLoadPLDevilerable_Dependencies(request, response, "");
	}

	public static final void addPLDependency(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			String item = request.getParameter("plDependency_item");
			if (item == null)
				item = "";
			final String plannedDate = request.getParameter("plDependency_plannedDate");
			final String actualDate = request.getParameter("plDependency_actualDate");
			String note = request.getParameter("plDependency_note");
			if (note == null)
				note = "";
			final DependencyInfo depInfo = new DependencyInfo();
			if ((!plannedDate.equals("")) && (plannedDate != null)) {
				final java.util.Date pDate = CommonTools.parseDate(plannedDate);
				final java.sql.Date pSqlDate = new java.sql.Date(pDate.getTime());
				depInfo.plannedDeliveryDate = pSqlDate;
			}
			if ((!actualDate.equals("")) && (actualDate != null)) {
				final java.util.Date aDate = CommonTools.parseDate(actualDate);
				final java.sql.Date aSqlDate = new java.sql.Date(aDate.getTime());
				depInfo.actualDeliveryDate = aSqlDate;
			}
			depInfo.projectPlanID = prjPlanID;
			depInfo.item = item;
			depInfo.note = note;
			Project.addPLDependency(depInfo);

			addChangeAuto(
				prjID,
				Constants.ACTION_ADD,
				Constants.PL_DEVDEP,
				"Critical dependences>" + depInfo.item,
				null,
				null);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLDependency(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		updatePLDependency(request, response);
		doLoadPLDevilerable_Dependencies(request, response, "");

	}

	public static final void updatePLDependency(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			final long dependencyID = Long.parseLong(request.getParameter("plDependency_depID"));
			String item = request.getParameter("plDependency_item");
			if (item == null)
				item = "";
			final String plannedDate = request.getParameter("plDependency_plannedDate");
			final String actualDate = request.getParameter("plDependency_actualDate");
			String note = request.getParameter("plDependency_note");
			if (note == null)
				note = "";
			final DependencyInfo depInfo = new DependencyInfo();
			if ((!plannedDate.equals("")) && (plannedDate != null)) {
				final java.util.Date pDate = CommonTools.parseDate(plannedDate);
				final java.sql.Date pSqlDate = new java.sql.Date(pDate.getTime());
				depInfo.plannedDeliveryDate = pSqlDate;
			}
			if ((!actualDate.equals("")) && (actualDate != null)) {
				final java.util.Date aDate = CommonTools.parseDate(actualDate);
				final java.sql.Date aSqlDate = new java.sql.Date(aDate.getTime());
				depInfo.actualDeliveryDate = aSqlDate;
			}
			depInfo.dependencyID = dependencyID;
			depInfo.projectPlanID = prjPlanID;
			depInfo.item = item;
			depInfo.note = note;
			Project.updatePLDependency(depInfo);

			DependencyInfo oldDepInfo = (DependencyInfo) session.getAttribute("plDependencyInfo");
			String oldValue;
			String newValue;
			oldValue = oldDepInfo.item;
			newValue = depInfo.item;
			if (!oldValue.equalsIgnoreCase(newValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_DEVDEP,
					"Critial dependency>Item",
					newValue,
					oldValue);

			oldValue = CommonTools.dateFormat(new java.util.Date(oldDepInfo.plannedDeliveryDate.getTime()));
			newValue = CommonTools.dateFormat(new java.util.Date(depInfo.plannedDeliveryDate.getTime()));
			if (!oldValue.equalsIgnoreCase(newValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_DEVDEP,
					"Critial dependency>Planned delivery date",
					newValue,
					oldValue);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void deletePLDependency(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long dependencyID = Long.parseLong(request.getParameter("plDependency_depID"));
			Project.deletePLDependency(dependencyID);

			DependencyInfo dependencyInfo = (DependencyInfo) session.getAttribute("plDependencyInfo");
			addChangeAuto(
				prjID,
				Constants.ACTION_DELETE,
				Constants.PL_DEVDEP,
				"Critial dependencies>" + dependencyInfo.item,
				null,
				null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static final void doDeletePLDependency(
		final HttpServletRequest request,
		final HttpServletResponse response) {

		deletePLDependency(request, response);
		doLoadPLDevilerable_Dependencies(request, response, "");

	}
	// ------------------------------End of PL Dependency------------------------------

	public static final void doLoadPLLifecycle(final HttpServletRequest request, final HttpServletResponse response) {
		doLoadPLLifecycle(request, response, "");
	}

	public static final void doLoadPLLifecycle(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String index) {
		try {
			final HttpSession session = request.getSession();
			// session.setAttribute("caller", String.valueOf(Constants.PROJECT_PLAN_CALLER));
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			final Vector iterationList = Project.getPLIterationList(prjID);
			final Vector milestoneList = Project.getMilestoneListByProject(prjID);
			// Get date info from prjdate info, 2 requests are redundant
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			final ProjectInfo prjInfo = Project.getProjectInfo(prjID);
			//final Vector stageVt = Schedule.getStageList(prjID); //landd comment
			session.setAttribute("ProjectInfo", prjInfo);
			session.setAttribute("prjDateInfo", prjDateInfo);
			//session.setAttribute("stageVector", stageVt);	// landd comment
			session.setAttribute("PLIterationList", iterationList);
			session.setAttribute("plMilestoneList", milestoneList);
			Vector tailoringList = Assets.getDeviationList(prjID, "");
			session.setAttribute("tailoringList", tailoringList);

			// Following function call is not necessary and expensive call
			// because Project.getPerformanceMetrics will calculate all other
			// metrics => solution: use project information to get durration
			// final Vector performanceVector = Project.getPerformanceMetrics(prjID);
			// session.setAttribute("WOPerformanceVector", performanceVector);
			MetricInfo durationMetric = prjInfo.getDurationMetric();
			session.setAttribute("durationMetric", durationMetric);

			request.setAttribute("schedFileName",Project.getSchedFileName(prjID));
			
			// HaiMM: CR - Add Requirement section
			final ReqChangesInfo reqInfo = Requirement.getChangesRequirement(prjID);
			session.setAttribute("ReqChangesInfo", reqInfo);

			final Vector info = ProductIntegration.getIntegrationStratList(prjID);
			session.setAttribute("ProIntegrList", info);

			
			
			int caller=Integer.parseInt(session.getAttribute("caller").toString());
			if (caller == Constants.SCHEDULE_CALLER) {
				Fms1Servlet.callPage("scheStage.jsp" + index,request,response);
			} else {
				Fms1Servlet.callPage("plLifecycle.jsp" + index,request,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------------------------PL Iteration------------------------------
	public static final void doPreparePLIterationUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long iterationID = Long.parseLong(request.getParameter("plIteration_ID"));
			final IterationInfo info = Project.getPLIteration(iterationID);
			final Vector milestoneList = Project.getMilestoneListByProject(prjID);
			session.setAttribute("plIterationInfo", info);
			session.setAttribute("plMilestoneList", milestoneList);
			Fms1Servlet.callPage("plIterationUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPreparePLIterationAdd(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final Vector milestoneList = Project.getMilestoneListByProject(prjID);
			session.setAttribute("plMilestoneList", milestoneList);
			Fms1Servlet.callPage("plIterationAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAddPLIteration(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));

			final long stage = Long.parseLong(request.getParameter("plIteration_milestoneID"));
			String description = request.getParameter("plIteration_description");
			if (description == null)
				description = "";
			String milestone = request.getParameter("plIteration_milestone");
			if (milestone == null)
				milestone = "";
			final String plannedDate = request.getParameter("txtBEndD");
			final String replannedDate = request.getParameter("txtPEndD");
			final String actualDate = request.getParameter("txtAEndD");

			final IterationInfo iterationInfo = new IterationInfo();
			iterationInfo.milestoneID = stage;
			iterationInfo.description = description;
			iterationInfo.milestone = milestone;

			if ((!plannedDate.equals("")) && (plannedDate != null)) {
				final java.util.Date pDate = CommonTools.parseDate(plannedDate);
				final java.sql.Date pSqlDate = new java.sql.Date(pDate.getTime());
				iterationInfo.planEndDate = pSqlDate;
			}
			if ((!replannedDate.equals("")) && (replannedDate != null)) {
				final java.util.Date pDate = CommonTools.parseDate(replannedDate);
				final java.sql.Date pSqlDate = new java.sql.Date(pDate.getTime());
				iterationInfo.replanEndDate = pSqlDate;
			}
			if ((!actualDate.equals("")) && (actualDate != null)) {
				final java.util.Date pDate = CommonTools.parseDate(actualDate);
				final java.sql.Date pSqlDate = new java.sql.Date(pDate.getTime());
				iterationInfo.actualEndDate = pSqlDate;
			}

			Project.addPLIteration(iterationInfo);

			Vector milestoneList = (Vector) session.getAttribute("plMilestoneList");
			String name = "";
			for (int j = 0; j < milestoneList.size(); j++) {
				MilestoneInfo milestoneInfo = (MilestoneInfo)milestoneList.elementAt(j);
				long milestoneID = milestoneInfo.getMilestoneId();
				if (iterationInfo.milestoneID == milestoneID) {
					name = milestoneInfo.getName();
					break;
				}
			}
			addChangeAuto(prjID, Constants.ACTION_ADD, Constants.PL_LIFECYCLE, "Iteration List>" + name, null, null);

			doLoadPLLifecycle(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLIteration(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long iterationID = Long.parseLong(request.getParameter("plIteration_ID"));
			String description = request.getParameter("plIteration_description");
			if (description == null)
				description = "";
			String milestone = request.getParameter("plIteration_milestone");
			if (milestone == null)
				milestone = "";
			final String plannedDate = request.getParameter("txtBEndD");
			final String replannedDate = request.getParameter("txtPEndD");
			final String actualDate = request.getParameter("txtAEndD");

			final IterationInfo iterationInfo = new IterationInfo();

			iterationInfo.iterationID = iterationID;
			iterationInfo.description = description;
			iterationInfo.milestone = milestone;

			if ((!plannedDate.equals("")) && (plannedDate != null)) {
				final java.util.Date pDate = CommonTools.parseDate(plannedDate);
				final java.sql.Date pSqlDate = new java.sql.Date(pDate.getTime());
				iterationInfo.planEndDate = pSqlDate;
			}
			if ((!replannedDate.equals("")) && (replannedDate != null)) {
				final java.util.Date pDate = CommonTools.parseDate(replannedDate);
				final java.sql.Date pSqlDate = new java.sql.Date(pDate.getTime());
				iterationInfo.replanEndDate = pSqlDate;
			}
			if ((!actualDate.equals("")) && (actualDate != null)) {
				final java.util.Date pDate = CommonTools.parseDate(actualDate);
				final java.sql.Date pSqlDate = new java.sql.Date(pDate.getTime());
				iterationInfo.actualEndDate = pSqlDate;
			}

			Project.updatePLIteration(iterationInfo);

			IterationInfo oldIterationInfo = (IterationInfo) session.getAttribute("plIterationInfo");
			String oldValue, newValue;

			oldValue = oldIterationInfo.description;
			newValue = iterationInfo.description;
			if (!oldValue.equalsIgnoreCase(newValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_LIFECYCLE,
					"Iteration List>Description",
					newValue,
					oldValue);

			oldValue = oldIterationInfo.milestone;
			newValue = iterationInfo.milestone;
			if (!oldValue.equalsIgnoreCase(newValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_LIFECYCLE,
					"Iteration List>milestone",
					newValue,
					oldValue);

			doLoadPLLifecycle(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeletePLIteration(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));

			final long iterationID = Long.parseLong(request.getParameter("plIteration_ID"));
			Project.deletePLIteration(iterationID);

			IterationInfo iterationInfo = (IterationInfo) session.getAttribute("plIterationInfo");
			Vector milestoneList = (Vector) session.getAttribute("plMilestoneList");
			String name = "";
			for (int j = 0; j < milestoneList.size(); j++) {
				MilestoneInfo milestoneInfo = (MilestoneInfo)milestoneList.elementAt(j);
				long milestoneID = milestoneInfo.getMilestoneId();
				if (iterationInfo.milestoneID == milestoneID) {
					name = milestoneInfo.getName();
					break;
				}
			}
			addChangeAuto(prjID, Constants.ACTION_DELETE, Constants.PL_LIFECYCLE, "Iteration List>" + name, null, null);

			doLoadPLLifecycle(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ------------------------------End of PL Iteration------------------------------

	public static final void doLoadPLStructure(final HttpServletRequest request, final HttpServletResponse response) {
		doLoadPLStructure(request, response, "");
	}

	public static final void doLoadPLStructure(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String index) {
		try {
			final HttpSession session = request.getSession();
			session.setAttribute("caller", String.valueOf(Constants.PROJECT_PLAN_CALLER));
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			final String orgStructure = Project.getPLOrjStructure(prjPlanID);
			final Vector interfaceList = Project.getPLInterfaceList(prjPlanID,0);
			final Vector subcontractList = Project.getPLSubcontractList(prjID);
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			session.setAttribute("plInterfaceList", interfaceList);
			session.setAttribute("subcontractVector", subcontractList);
			session.setAttribute("plOrgStructure", orgStructure);
			Fms1Servlet.callPage("plStructure.jsp" + index,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPreparePLOrgStructureUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plOrgStructureUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLOrgStructure(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			String orgStructure = request.getParameter("plOrgStructure_txt");
			if (orgStructure == null)
				orgStructure = "";
			Project.updatePLOrjStructure(prjPlanID, orgStructure);

			String oldValue = (String) session.getAttribute("plOrgStructure");
			String newValue = orgStructure;
			if (!newValue.equalsIgnoreCase(oldValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_ORGANIZATION,
					"Project Organization",
					newValue,
					oldValue);
			doLoadPLStructure(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------------------------PL Interface------------------------------
	public static final void doViewPLInterface(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long interfaceID = Long.parseLong(request.getParameter("plInterface_ID"));
			final InterfaceInfo info = Project.getPLInterface(interfaceID);
			session.setAttribute("plInterfaceInfo", info);
			final Vector devList = Assignments.getDevListByProject(prjID);
			session.setAttribute("plDevList", devList);
			Fms1Servlet.callPage("plInterfaceView.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	// Sercurity aspects start
	public static final void doViewPLDefAcrList(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));			
			final Vector info = DefAcr.getDefAcrList(prjID);
			session.setAttribute("DefAcrList", info);
			Fms1Servlet.callPage("plDefAcrList.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doPrepareAddPLDefAcr(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plDefAcrAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doPrepareUpdatePLDefAcr(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector vDefAcrList = (Vector) session.getAttribute("DefAcrList");
			String listUpdate = (String) request.getParameter("listUpdate");						
			
			Vector vUpdate = new Vector();
			final StringTokenizer strSerUpdateIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			Vector vUpdateListId =new Vector();
			int i = 0;
			while (strSerUpdateIDList.hasMoreElements()) {
				vUpdateListId.addElement(strSerUpdateIDList.nextToken());				
			}

			int tSize = vDefAcrList.size();
			for(i = 0; i < tSize; i++){				
				DefAcrInfo info = (DefAcrInfo) vDefAcrList.elementAt(i);
				if ( vUpdateListId.contains(info.defID+"")) {
					vUpdate.addElement(info);
				}
			}
			session.setAttribute("DefAcrBatchUpdateList",vUpdate);
			Fms1Servlet.callPage("plDefAcrUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doAddPLDefAcr(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));			
			Vector errDataVector = new Vector();
			DefAcrInfo defInfo = null;			
			int iRet = 0;			

			String[] acronym = request.getParameterValues("def_acr");
			String[] definition = request.getParameterValues("def_def");
			String[] note = request.getParameterValues("def_note");						

			int size = acronym.length;
			for (int i = 0; i < size; i++) {
				if (acronym[i] == null || "".equals(acronym[i].trim())) continue;			
				defInfo = new DefAcrInfo();				
							
				if (acronym[i] == null) acronym[i] = "";
				defInfo.acronym = ConvertString.toStandardizeString(acronym[i].trim());
				
				if (definition[i] == null) definition[i] = "";
				defInfo.definition = ConvertString.toStandardizeString(definition[i].trim());
								
				if (note[i] == null) note[i] = "";
				defInfo.note = ConvertString.toStandardizeString(note[i].trim());
			
				if (iRet != 1) iRet = DefAcr.doAddDefAcr(defInfo, prjID);

				// If error then add to vector
				if (iRet != 0) {					
					errDataVector.addElement(defInfo);
				} else {
					addChangeAuto(  prjID,
									Constants.ACTION_ADD,
									Constants.DEFINITIONS_ACRONYMS_ADD,
									"Acronyms Item",
									defInfo.acronym,
									null);
				}
			}			

			if ( iRet == 0) {				
				Vector defList = DefAcr.getDefAcrList(prjID);
				if (defList == null) {
					defList = new Vector();
				}
				session.setAttribute("DefAcrList", defList);				
				Fms1Servlet.callPage("plDefAcrList.jsp",request,response);
			} else {
				request.setAttribute("ErrDefAcrList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("plDefAcrAdd.jsp",request,response);			
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doUpdatePLDefAcr(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			int size = 0;			
			Vector vNewDef = new Vector();
			Vector vOldDef = (Vector) session.getAttribute("DefAcrBatchUpdateList");
			if (vOldDef != null)	session.removeAttribute("DefAcrBatchUpdateList");
			Vector vErrDef = new Vector();

			DefAcrInfo newDefInfo = null;
			DefAcrInfo oldDefInfo = null;
	
			String[] defID = request.getParameterValues("defID");
			String[] acronym = request.getParameterValues("def_acr");
			String[] definition = request.getParameterValues("def_def");
			String[] note = request.getParameterValues("def_note");		

			if (defID != null) size = defID.length;
			for (int i = 0; i < size; i++) {
				newDefInfo = new DefAcrInfo();
				oldDefInfo = (DefAcrInfo) vOldDef.elementAt(i);
		
				if (acronym[i] == null) acronym[i] = "";
				else acronym[i] = ConvertString.toStandardizeString(acronym[i]);

				if (definition[i] == null) definition[i] = "";
				else definition[i] = ConvertString.toStandardizeString(definition[i]);
		
				if (note[i] == null) note[i] = "";
				else note[i] = ConvertString.toStandardizeString(note[i]);
		
				if (oldDefInfo.acronym == null) oldDefInfo.acronym = "";
				if (oldDefInfo.definition == null) oldDefInfo.definition = "";
				if (oldDefInfo.note == null) oldDefInfo.note = "";					
		
				newDefInfo.defID 			= 	Long.parseLong(defID[i]);
				newDefInfo.acronym 			= 	acronym[i];
				newDefInfo.definition		= 	definition[i];
				newDefInfo.note	 			= 	note[i];								
								

				if (   	 acronym[i].compareToIgnoreCase(oldDefInfo.acronym) != 0						
						|| definition[i].compareToIgnoreCase(oldDefInfo.definition) != 0
						|| note[i].compareToIgnoreCase(oldDefInfo.note) != 0						
				) 
				{
					addChangeAuto(prjID,
								Constants.ACTION_UPDATE,
								Constants.DEFINITIONS_ACRONYMS_UPDATE,
								"Acronym",
								acronym[i],
								oldDefInfo.acronym);
					vNewDef.addElement(newDefInfo);
				}				
				vErrDef.addElement(newDefInfo);				
			}

			if (DefAcr.doUpdateDefAcr(vNewDef,prjID)==0) {				
				Vector defList = DefAcr.getDefAcrList(prjID);
				if (defList == null) {
					defList = new Vector();
				}
				session.setAttribute("DefAcrList", defList);				
				Fms1Servlet.callPage("plDefAcrList.jsp",request,response);
			} else {
				request.setAttribute("ErrDefAcrList",vErrDef);
				request.setAttribute("ErrType","1");
				Fms1Servlet.callPage("plDefAcrUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doDeleteDefAcr(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int defID = 0;			
			int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			String listUpdate = (String) request.getParameter("listUpdate");
			
			final StringTokenizer strDefDeleteIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			while (strDefDeleteIDList.hasMoreElements()) {
				defID = Integer.parseInt(strDefDeleteIDList.nextToken());				
				if (DefAcr.doDeleteDefAcr(defID)) {
					WorkOrderCaller.addChangeAuto(
						prjID,
						Constants.ACTION_DELETE,
						Constants.DEFINITIONS_ACRONYMS_DELETE,
						"defID "+defID,
						null,
						null);
				}
			}
			
			Vector defList = DefAcr.getDefAcrList(prjID);
			if (defList == null) {
				defList = new Vector();
			}
			session.setAttribute("DefAcrList", defList);				
			Fms1Servlet.callPage("plDefAcrList.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//	Sercurity aspects end


	// Communication & reporting start
	public static final void doViewPLComReportList(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final Vector info = ComReport.getCommunicationReportingList(prjID);
			session.setAttribute("ComReportList", info);
			Fms1Servlet.callPage("plComReportList.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareAddPLComReport(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plComReportAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareUpdatePLComReport(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector comReportList = (Vector) session.getAttribute("ComReportList");
			String listUpdate = (String) request.getParameter("listUpdate");

			Vector vUpdate = new Vector();
			final StringTokenizer strComUpdateIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			Vector vUpdateListId =new Vector();
			int i = 0;
			while (strComUpdateIDList.hasMoreElements()) {
				vUpdateListId.addElement(strComUpdateIDList.nextToken());
			}

			int tSize = comReportList.size();
			for(i = 0; i < tSize; i++){
				ComReportInfo info = (ComReportInfo) comReportList.elementAt(i);
				if ( vUpdateListId.contains(info.comID+"")) {
					vUpdate.addElement(info);
				}
			}
			session.setAttribute("ComReportBatchUpdateList",vUpdate);
			Fms1Servlet.callPage("plComReportUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAddPLComReport(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector errDataVector = new Vector();
			ComReportInfo comInfo = null;
			int iRet = 0;

			String[] parent_type = request.getParameterValues("parent_type");
			String[] com_type = request.getParameterValues("com_type");
			String[] com_method_tool = request.getParameterValues("com_method_tool");
			String[] com_when = request.getParameterValues("com_when");
			String[] com_information = request.getParameterValues("com_information");
			String[] com_resp = request.getParameterValues("com_resp");

			int size = com_type.length;
			for (int i = 0; i < size; i++) {
				if (com_type[i] == null || "".equals(com_type[i].trim())) continue;
				comInfo = new ComReportInfo();

				comInfo.comParentType = Integer.parseInt(parent_type[i].trim());

				if (com_type[i] == null) com_type[i] = "";
				comInfo.comType = ConvertString.toStandardizeString(com_type[i].trim());

				if (com_method_tool[i] == null) com_method_tool[i] = "";
				comInfo.comMethodTool = ConvertString.toStandardizeString(com_method_tool[i].trim());

				if (com_when[i] == null) com_when[i] = "";
				comInfo.comWhen = ConvertString.toStandardizeString(com_when[i].trim());

				if (com_information[i] == null) com_information[i] = "";
				comInfo.comInfo = ConvertString.toStandardizeString(com_information[i].trim());

				if (com_resp[i] == null) com_resp[i] = "";
				comInfo.comResp = ConvertString.toStandardizeString(com_resp[i].trim());

				if (iRet != 1) iRet = ComReport.doAddComReport(comInfo, prjID);

				// If error then add to vector
				if (iRet != 0) {
					errDataVector.addElement(comInfo);
				} else {
					addChangeAuto(  prjID,
									Constants.ACTION_ADD,
									Constants.COMREPORT_ADD,
									"Communication Reporting Type",
									comInfo.comType,
									null);
				}
			}

			if ( iRet == 0) {
				Vector comList = ComReport.getCommunicationReportingList(prjID);
				if (comList == null) {
					comList = new Vector();
				}
				session.setAttribute("ComReportList", comList);
				Fms1Servlet.callPage("plComReportList.jsp",request,response);
			} else {
				request.setAttribute("ErrComReportList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("plComReportAdd.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLComReport(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			int size = 0;
			Vector vNewCom = new Vector();
			Vector vOldCom = (Vector) session.getAttribute("ComReportBatchUpdateList");
			if (vOldCom != null)	session.removeAttribute("ComReportBatchUpdateList");
			Vector vErrCom = new Vector();

			ComReportInfo newComInfo = null;
			ComReportInfo oldComInfo = null;

			String[] comID = request.getParameterValues("comID");
			String[] parent_type = request.getParameterValues("parent_type");
			String[] com_type = request.getParameterValues("com_type");
			String[] com_method_tool = request.getParameterValues("com_method_tool");
			String[] com_when = request.getParameterValues("com_when");
			String[] com_information = request.getParameterValues("com_information");
			String[] com_resp = request.getParameterValues("com_resp");

			if (comID != null) size = comID.length;
			for (int i = 0; i < size; i++) {
				newComInfo = new ComReportInfo();
				oldComInfo = (ComReportInfo) vOldCom.elementAt(i);

				if (com_type[i] == null) com_type[i] = "";
				else com_type[i] = ConvertString.toStandardizeString(com_type[i]);

				if (com_method_tool[i] == null) com_method_tool[i] = "";
				else com_method_tool[i] = ConvertString.toStandardizeString(com_method_tool[i]);

				if (com_when[i] == null) com_when[i] = "";
				else com_when[i] = ConvertString.toStandardizeString(com_when[i]);

				if (com_information[i] == null) com_information[i] = "";
				else com_information[i] = ConvertString.toStandardizeString(com_information[i]);

				if (com_resp[i] == null) com_resp[i] = "";
				else com_resp[i] = ConvertString.toStandardizeString(com_resp[i]);

				if (oldComInfo.comType == null) oldComInfo.comType = "";
				if (oldComInfo.comMethodTool == null) oldComInfo.comMethodTool = "";
				if (oldComInfo.comWhen == null) oldComInfo.comWhen = "";
				if (oldComInfo.comInfo == null) oldComInfo.comInfo = "";
				if (oldComInfo.comResp == null) oldComInfo.comResp = "";

				newComInfo.comID 			= 	Long.parseLong(comID[i]);
				newComInfo.comParentType	= 	Integer.parseInt(parent_type[i]);
				newComInfo.comType 			= 	com_type[i];
				newComInfo.comMethodTool 	= 	com_method_tool[i];
				newComInfo.comWhen 			= 	com_when[i].trim();
				newComInfo.comInfo 			= 	com_information[i];
				newComInfo.comResp 			= 	com_resp[i];

				if (	newComInfo.comParentType != oldComInfo.comParentType
						|| com_type[i].compareToIgnoreCase(oldComInfo.comType) != 0
						|| com_method_tool[i].compareToIgnoreCase(oldComInfo.comMethodTool) != 0
						|| com_when[i].compareToIgnoreCase(oldComInfo.comWhen) != 0
						|| com_information[i].compareToIgnoreCase(oldComInfo.comInfo) != 0
						|| com_resp[i].compareToIgnoreCase(oldComInfo.comResp) != 0
				)
				{
					addChangeAuto(prjID,
								Constants.ACTION_UPDATE,
								Constants.PL_PROJECT_SCHEDULE_UPDATE,
								"Communication Reporting Type",
								com_type[i],
								oldComInfo.comType);
					vNewCom.addElement(newComInfo);
				}
				vErrCom.addElement(newComInfo);
			}

			if (ComReport.doUpdateComReport(vNewCom,prjID)==0) {
				Vector comList = ComReport.getCommunicationReportingList(prjID);
				if (comList == null) {
					comList = new Vector();
				}
				session.setAttribute("ComReportList", comList);
				Fms1Servlet.callPage("plComReportList.jsp",request,response);
			} else {
				request.setAttribute("ErrComReportList",vErrCom);
				request.setAttribute("ErrType","1");
				Fms1Servlet.callPage("plComReportUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeleteComReport(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int comID = 0;
			int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			String listUpdate = (String) request.getParameter("listUpdate");

			final StringTokenizer strComDeleteIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			while (strComDeleteIDList.hasMoreElements()) {
				comID = Integer.parseInt(strComDeleteIDList.nextToken());
				if (ComReport.doDeleteProjectSchedule(comID)) {
					WorkOrderCaller.addChangeAuto(
						prjID,
						Constants.ACTION_DELETE,
						Constants.COMREPORT_DELETE,
						"comID "+comID,
						null,
						null);
				}
			}

			Vector comList = ComReport.getCommunicationReportingList(prjID);
			if (comList == null) {
				comList = new Vector();
			}
			session.setAttribute("ComReportList", comList);
			Fms1Servlet.callPage("plComReportList.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	Communication & reporting end


	// project schedule start
	public static final void doViewPLProjSchedList(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final Vector info = ProjectSchedule.getProjectScheduleList(prjID);
			session.setAttribute("ProjectScheduleList", info);
			
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);           

			Fms1Servlet.callPage("plProjSchedList.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareAddPLProjSched(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plProjSchedAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareUpdatePLProjSched(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector projSchedList = (Vector) session.getAttribute("ProjectScheduleList");
			String listUpdate = (String) request.getParameter("listUpdate");

			Vector vUpdate = new Vector();
			final StringTokenizer strAssUpdateIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			Vector vUpdateListId =new Vector();
			int i = 0;
			while (strAssUpdateIDList.hasMoreElements()) {
				vUpdateListId.addElement(strAssUpdateIDList.nextToken());
			}

			int tSize = projSchedList.size();
			for(i = 0; i < tSize; i++){
				ProjSchedInfo info = (ProjSchedInfo) projSchedList.elementAt(i);
				if ( vUpdateListId.contains(info.schedID+"")) {
					vUpdate.addElement(info);
				}
			}
			session.setAttribute("SchedBatchUpdateList",vUpdate);
			Fms1Servlet.callPage("plProjSchedUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static final void doAddPLProjSched(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector errDataVector = new Vector();
			ProjSchedInfo schedInfo = null;
			int iRet = 0;

			String[] sched_type = request.getParameterValues("sched_type");
			String[] sched_activity = request.getParameterValues("sched_activity");
			String[] sched_startDate = request.getParameterValues("sched_startDate");
			String[] sched_responsible = request.getParameterValues("sched_responsible");
			String[] sched_note = request.getParameterValues("sched_note");


			int size = sched_activity.length;
			for (int i = 0; i < size; i++) {
				if (sched_activity[i] == null || "".equals(sched_activity[i].trim())) continue;
				schedInfo = new ProjSchedInfo();

				schedInfo.schedType = Integer.parseInt(sched_type[i].trim());

				if (sched_activity[i] == null) sched_activity[i] = "";
				schedInfo.schedActivity = ConvertString.toStandardizeString(sched_activity[i].trim());

				schedInfo.schedStartDate = CommonTools.parseSQLDate(sched_startDate[i].trim());

				if (sched_responsible[i] == null) sched_responsible[i] = "";
				schedInfo.schedResponsible = ConvertString.toStandardizeString(sched_responsible[i].trim());

				if (sched_note[i] == null) sched_note[i] = "";
				schedInfo.schedNote = ConvertString.toStandardizeString(sched_note[i].trim());

				if (iRet != 1) iRet = ProjectSchedule.doAddProjectSchedule(schedInfo, prjID);

				// If error then add to vector
				if (iRet != 0) {
					errDataVector.addElement(schedInfo);
				} else {
					addChangeAuto(  prjID,
									Constants.ACTION_ADD,
									Constants.PL_PROJECT_SCHEDULE_ADD,
									"Project Schedule Activity",
									schedInfo.schedActivity,
									null);
				}
			}

			if ( iRet == 0) {
				Vector schedList = ProjectSchedule.getProjectScheduleList(prjID);
				if (schedList == null) {
					schedList = new Vector();
				}
				session.setAttribute("ProjectScheduleList", schedList);
				Fms1Servlet.callPage("plProjSchedList.jsp",request,response);
			} else {
				request.setAttribute("ErrProjectScheduleList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("plProjSchedAdd.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLProjSched(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			int size = 0;
			Vector vNewSched = new Vector();
			Vector vOldSched = (Vector) session.getAttribute("SchedBatchUpdateList");
			if (vOldSched != null)	session.removeAttribute("SchedBatchUpdateList");
			Vector vErrSched = new Vector();

			ProjSchedInfo newSchedInfo = null;
			ProjSchedInfo oldSchedInfo = null;

			String[] schedID = request.getParameterValues("schedID");
			String[] sched_type = request.getParameterValues("sched_type");
			String[] sched_activity = request.getParameterValues("sched_activity");
			String[] sched_startDate = request.getParameterValues("sched_startDate");
			String[] sched_responsible = request.getParameterValues("sched_responsible");
			String[] sched_note = request.getParameterValues("sched_note");

			if (schedID != null) size = schedID.length;
			for (int i = 0; i < size; i++) {
				newSchedInfo = new ProjSchedInfo();
				oldSchedInfo = (ProjSchedInfo) vOldSched.elementAt(i);

				if (sched_activity[i] == null) sched_activity[i] = "";
				else sched_activity[i] = ConvertString.toStandardizeString(sched_activity[i]);

				if (sched_responsible[i] == null) sched_responsible[i] = "";
				else sched_responsible[i] = ConvertString.toStandardizeString(sched_responsible[i]);

				if (sched_note[i] == null) sched_note[i] = "";
				else sched_note[i] = ConvertString.toStandardizeString(sched_note[i]);

				if (oldSchedInfo.schedActivity == null) oldSchedInfo.schedActivity = "";
				if (oldSchedInfo.schedResponsible == null) oldSchedInfo.schedResponsible = "";
				if (oldSchedInfo.schedNote == null) oldSchedInfo.schedNote = "";

				newSchedInfo.schedID 			= 	Long.parseLong(schedID[i]);
				newSchedInfo.schedType 			= 	Integer.parseInt(sched_type[i]);
				newSchedInfo.schedActivity	 	= 	sched_activity[i];
				newSchedInfo.schedStartDate 	= 	CommonTools.parseSQLDate(sched_startDate[i].trim());
				newSchedInfo.schedResponsible 	= 	sched_responsible[i];
				newSchedInfo.schedNote 			= 	sched_note[i];

				if (   newSchedInfo.schedType != oldSchedInfo.schedType
						|| sched_activity[i].compareToIgnoreCase(oldSchedInfo.schedActivity) != 0
						|| newSchedInfo.schedStartDate != oldSchedInfo.schedStartDate
						|| sched_responsible[i].compareToIgnoreCase(oldSchedInfo.schedResponsible) != 0
						|| sched_note[i].compareToIgnoreCase(oldSchedInfo.schedNote) != 0
				)
				{
					addChangeAuto(prjID,
								Constants.ACTION_UPDATE,
								Constants.PL_PROJECT_SCHEDULE_UPDATE,
								"Project Schedule Activity",
								sched_activity[i],
								oldSchedInfo.schedActivity);
					vNewSched.addElement(newSchedInfo);
				}
				vErrSched.addElement(newSchedInfo);
			}

			if (ProjectSchedule.doUpdateProjectSchedule(vNewSched,prjID)==0) {
				Vector schedList = ProjectSchedule.getProjectScheduleList(prjID);
				if (schedList == null) {
					schedList = new Vector();
				}
				session.setAttribute("ProjectScheduleList", schedList);
				Fms1Servlet.callPage("plProjSchedList.jsp",request,response);
			} else {
				request.setAttribute("ErrMeasurementsProgList",vErrSched);
				request.setAttribute("ErrType","1");
				Fms1Servlet.callPage("plMeasureProgUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeleteSched(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int schedID = 0;
			int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			String listUpdate = (String) request.getParameter("listUpdate");

			final StringTokenizer strSchedDeleteIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			while (strSchedDeleteIDList.hasMoreElements()) {
				schedID = Integer.parseInt(strSchedDeleteIDList.nextToken());
				if (ProjectSchedule.doDeleteProjectSchedule(schedID)) {
					WorkOrderCaller.addChangeAuto(
						prjID,
						Constants.ACTION_DELETE,
						Constants.PL_PROJECT_SCHEDULE_DELETE,
						"schedID "+schedID,
						null,
						null);
				}
			}

			Vector schedList = ProjectSchedule.getProjectScheduleList(prjID);
			if (schedList == null) {
				schedList = new Vector();
			}
			session.setAttribute("ProjectScheduleList", schedList);
			Fms1Servlet.callPage("plProjSchedList.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	//	project schedule end

	// mesurements program start
	public static final void doPrepareAddPLMeasurementsProg(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plMeasureProgAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareUpdatePLMeasurementsProg(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plMeasureProgUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAddPLMeasurementsProg(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector errDataVector = new Vector();
			MeasureProgInfo measInfo = null;
			int iRet = 0;
			boolean isExist = false;

			String[] aMes_data_colect = request.getParameterValues("mes_data_colect");
			String[] aMes_purpose = request.getParameterValues("mes_purpose");
			String[] aMes_responsible = request.getParameterValues("mes_responsible");
			String[] aMes_when = request.getParameterValues("mes_when");


			int size = aMes_data_colect.length;
			for (int i = 0; i < size; i++) {
				if (aMes_data_colect[i] == null || "".equals(aMes_data_colect[i].trim())) continue;
				measInfo = new MeasureProgInfo();

				measInfo.mes_data_colect = ConvertString.toStandardizeString(aMes_data_colect[i].trim());

				if (aMes_purpose[i] == null) aMes_purpose[i] = "";
				measInfo.mes_purpose = ConvertString.toStandardizeString(aMes_purpose[i].trim());

				if (aMes_responsible[i] == null) aMes_responsible[i] = "";
				measInfo.mes_responsible = ConvertString.toStandardizeString(aMes_responsible[i].trim());


				if (aMes_when[i] == null) aMes_when[i] = "";
				measInfo.mes_when = ConvertString.toStandardizeString(aMes_when[i].trim());

				if (iRet != 1) iRet = MeasurementsProg.doAddMeasurementsProgram(measInfo, prjID);

				// If error then add to vector
				if (iRet != 0) {
					if (iRet == 2) isExist = true;
					errDataVector.addElement(measInfo);
				} else {
					addChangeAuto(  prjID,
									Constants.ACTION_ADD,
									Constants.PL_MEASUREMENTS_PROGRAM_ADD,
									"Measurements Program",
									measInfo.mes_data_colect,
									null);
				}
			}

			if (isExist) iRet = 2;

			if ( iRet == 0 && !isExist) {
				Vector measList = MeasurementsProg.getMeasurementsProgList(prjID);
				if (measList == null) {
					measList = new Vector();
				}
				session.setAttribute("MeasurementsProgList", measList);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
			} else {
				request.setAttribute("ErrMeasurementsProgList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("plMeasureProgAdd.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLMeasurementsProg(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			int size = 0;
			Vector vNewMeas = new Vector();
			Vector vOldMeas = (Vector) session.getAttribute("MeasurementsProgList");
			Vector vErrMeas = new Vector();

			MeasureProgInfo newMeasInfo = null;
			MeasureProgInfo oldMeasInfo = null;

			String[] aMes_id 			= request.getParameterValues("mes_id");
			String[] aMes_data_colect 	= request.getParameterValues("mes_data_colect");
			String[] aMes_purpose 		= request.getParameterValues("mes_purpose");
			String[] aMes_responsible 	= request.getParameterValues("mes_responsible");
			String[] aMes_when 			= request.getParameterValues("mes_when");

			if (aMes_id != null) size = aMes_id.length;
			for (int i = 0; i < size; i++) {
				newMeasInfo = new MeasureProgInfo();
				oldMeasInfo = (MeasureProgInfo) vOldMeas.elementAt(i);

				if (aMes_data_colect[i] == null) aMes_data_colect[i] = "";
				else aMes_data_colect[i] = ConvertString.toStandardizeString(aMes_data_colect[i]);

				if (aMes_purpose[i] == null) aMes_purpose[i] = "";
				else aMes_purpose[i] = ConvertString.toStandardizeString(aMes_purpose[i]);

				if (aMes_responsible[i] == null) aMes_responsible[i] = "";
				else aMes_responsible[i] = ConvertString.toStandardizeString(aMes_responsible[i]);

				if (aMes_when[i] == null) aMes_when[i] = "";
				else aMes_when[i] = ConvertString.toStandardizeString(aMes_when[i]);

				if (oldMeasInfo.mes_data_colect == null) oldMeasInfo.mes_data_colect = "";
				if (oldMeasInfo.mes_purpose == null) oldMeasInfo.mes_purpose = "";
				if (oldMeasInfo.mes_responsible == null) oldMeasInfo.mes_responsible = "";
				if (oldMeasInfo.mes_when == null) oldMeasInfo.mes_when = "";

				newMeasInfo.mes_id 				= 	Long.parseLong(aMes_id[i]);
				newMeasInfo.mes_data_colect 	= 	aMes_data_colect[i];
				newMeasInfo.mes_purpose 		= 	aMes_purpose[i];
				newMeasInfo.mes_responsible 	= 	aMes_responsible[i];
				newMeasInfo.mes_when 			= 	aMes_when[i];

				if (   aMes_data_colect[i].compareToIgnoreCase(oldMeasInfo.mes_data_colect) != 0
						|| aMes_purpose[i].compareToIgnoreCase(oldMeasInfo.mes_purpose) != 0
						|| aMes_responsible[i].compareToIgnoreCase(oldMeasInfo.mes_responsible) != 0
						|| aMes_when[i].compareToIgnoreCase(oldMeasInfo.mes_when) != 0
				)
				{
					addChangeAuto(prjID,
								Constants.ACTION_UPDATE,
								Constants.PL_MEASUREMENTS_PROGRAM_UPDATE,
								"Measurements Program",
								aMes_data_colect[i],
								oldMeasInfo.mes_data_colect);
					vNewMeas.addElement(newMeasInfo);
				}
				vErrMeas.addElement(newMeasInfo);
			}

			if (MeasurementsProg.doUpdateMeasurementsProgram(vNewMeas,prjID)==0) {
				Vector measList = MeasurementsProg.getMeasurementsProgList(prjID);
				if (measList == null) {
					measList = new Vector();
				}
				session.setAttribute("MeasurementsProgList", measList);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
			} else {
				request.setAttribute("ErrMeasurementsProgList",vErrMeas);
				request.setAttribute("ErrType","1");
				Fms1Servlet.callPage("plMeasureProgUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeletePLMeasurementsProg(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector vErrMeas = new Vector();
			int size = 0;

			long delMeasID = Long.parseLong(request.getParameter("delMeasID"));

			MeasureProgInfo newMeasInfo = null;

			String[] aMes_id 			= request.getParameterValues("mes_id");
			String[] aMes_data_colect 	= request.getParameterValues("mes_data_colect");
			String[] aMes_purpose 		= request.getParameterValues("mes_purpose");
			String[] aMes_responsible 	= request.getParameterValues("mes_responsible");
			String[] aMes_when 			= request.getParameterValues("mes_when");

			if (aMes_id != null) size = aMes_id.length;
			for (int i = 0; i < size; i++) {
				newMeasInfo = new MeasureProgInfo();

				if (aMes_data_colect[i] == null) aMes_data_colect[i] = "";
				else aMes_data_colect[i] = ConvertString.toStandardizeString(aMes_data_colect[i]);

				if (aMes_purpose[i] == null) aMes_purpose[i] = "";
				else aMes_purpose[i] = ConvertString.toStandardizeString(aMes_purpose[i]);

				if (aMes_responsible[i] == null) aMes_responsible[i] = "";
				else aMes_responsible[i] = ConvertString.toStandardizeString(aMes_responsible[i]);

				if (aMes_when[i] == null) aMes_when[i] = "";
				else aMes_when[i] = ConvertString.toStandardizeString(aMes_when[i]);

				newMeasInfo.mes_id 				= 	Long.parseLong(aMes_id[i]);
				newMeasInfo.mes_data_colect 	= 	aMes_data_colect[i];
				newMeasInfo.mes_purpose 		= 	aMes_purpose[i];
				newMeasInfo.mes_responsible 	= 	aMes_responsible[i];
				newMeasInfo.mes_when 			= 	aMes_when[i];

				vErrMeas.addElement(newMeasInfo);

			}

			if (MeasurementsProg.doDeleteMeasurementsProgram(delMeasID)) {
				addChangeAuto(	prjID,
								Constants.ACTION_DELETE,
								Constants.PL_MEASUREMENTS_PROGRAM_DELETE,
								"Measurements Program",
								null,
								null);
				Vector measList = MeasurementsProg.getMeasurementsProgList(prjID);
				if (measList == null) {
					measList = new Vector();
				}
				session.setAttribute("MeasurementsProgList", measList);
			} else {
				request.setAttribute("ErrMeasurementsProgList",vErrMeas);
				request.setAttribute("ErrType","1");
			}
			Fms1Servlet.callPage("plMeasureProgUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// mesurements program end

	// Product integration strategy start
	public static final void doViewPLIntegrationStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final Vector info = ProductIntegration.getIntegrationStratList(prjID);
			session.setAttribute("ProIntegrList", info);
			Fms1Servlet.callPage("plIntegr_StratList.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareAddPLIntegrationStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plIntegr_StratAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareUpdatePLIntegrationStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plIntegr_StratUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAddPLIntegrationStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector errDataVector = new Vector();
			IntegrStratInfo integrInfo = null;
			int iRet = 0;
			boolean isExist = false;

			String[] aIntegrComID = request.getParameterValues("integr_comid");
			String[] aIntegrDesc = request.getParameterValues("integr_desc");
			String[] aIntegrWithCom = request.getParameterValues("integr_withcom");
			String[] aIntegrOrder = request.getParameterValues("integr_order");
			String[] aIntegrReady = request.getParameterValues("integr_readyneed");


			int size = aIntegrComID.length;
			for (int i = 0; i < size; i++) {
				if (aIntegrComID[i] == null || "".equals(aIntegrComID[i].trim())) continue;
				integrInfo = new IntegrStratInfo();

				integrInfo.integrCompID = ConvertString.toStandardizeString(aIntegrComID[i].trim());

				// creator
				if (aIntegrDesc[i] == null) aIntegrDesc[i] = "";
				integrInfo.integrDesc = ConvertString.toStandardizeString(aIntegrDesc[i].trim());

				// Reviewer
				if (aIntegrWithCom[i] == null) aIntegrWithCom[i] = "";
				integrInfo.integrWithComp = ConvertString.toStandardizeString(aIntegrWithCom[i].trim());

				// Approver
				if (aIntegrOrder[i] == null) aIntegrOrder[i] = "";
				integrInfo.integrOrder = ConvertString.toStandardizeString(aIntegrOrder[i].trim());

				// Log
				if (aIntegrReady[i] == null) aIntegrReady[i] = "";
				integrInfo.integrReadyNeed = ConvertString.toStandardizeString(aIntegrReady[i].trim());

				// If not system error then call add sub team
				if (iRet != 1) iRet = ProductIntegration.doAddProductIntegration(integrInfo, prjID);

				// If error then add to vector
				if (iRet != 0) {
					if (iRet == 2) isExist = true;
					errDataVector.addElement(integrInfo);
				} else {
					addChangeAuto(  prjID,
									Constants.ACTION_ADD,
									Constants.PL_INTEGRATION_STRATEGY_ADD,
									"Product Integration",
									integrInfo.integrCompID,
									null);
				}
			}

			if (isExist) iRet = 2;

			if ( iRet == 0 && !isExist) {
				Vector integrList = ProductIntegration.getIntegrationStratList(prjID);
				if (integrList == null) {
					integrList = new Vector();
				}
				session.setAttribute("ProIntegrList", integrList);
				Fms1Servlet.callPage("plLifecycle.jsp",request,response);
				//Fms1Servlet.callPage("plIntegr_StratList.jsp",request,response);
			} else {
				request.setAttribute("ErrProIntegrList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("plIntegr_StratAdd.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLIntegrationStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			int size = 0;
			Vector vNewIntegr = new Vector();
			Vector vOldIntegr = (Vector) session.getAttribute("ProIntegrList");
			Vector vErrIntegr = new Vector();

			IntegrStratInfo newIntegrInfo = null;
			IntegrStratInfo oldIntegrInfo = null;

			String[] aIntegrID 		= request.getParameterValues("integrID");
			String[] aIntegrComID 	= request.getParameterValues("integr_comid");
			String[] aIntegrDesc 	= request.getParameterValues("integr_desc");
			String[] aIntegrWithCom = request.getParameterValues("integr_withcom");
			String[] aIntegrOrder 	= request.getParameterValues("integr_order");
			String[] aIntegrReady 	= request.getParameterValues("integr_readyneed");

			if (aIntegrComID != null) size = aIntegrComID.length;
			for (int i = 0; i < size; i++) {
				newIntegrInfo = new IntegrStratInfo();
				oldIntegrInfo = (IntegrStratInfo) vOldIntegr.elementAt(i);

				if (aIntegrComID[i] == null) aIntegrComID[i] = "";
				else aIntegrComID[i] = ConvertString.toStandardizeString(aIntegrComID[i]);

				if (aIntegrDesc[i] == null) aIntegrDesc[i] = "";
				else aIntegrDesc[i] = ConvertString.toStandardizeString(aIntegrDesc[i]);

				if (aIntegrWithCom[i] == null) aIntegrWithCom[i] = "";
				else aIntegrWithCom[i] = ConvertString.toStandardizeString(aIntegrWithCom[i]);

				if (aIntegrOrder[i] == null) aIntegrOrder[i] = "";
				else aIntegrOrder[i] = ConvertString.toStandardizeString(aIntegrOrder[i]);

				if (aIntegrReady[i] == null) aIntegrReady[i] = "";
				else aIntegrReady[i] = ConvertString.toStandardizeString(aIntegrReady[i]);

				if (oldIntegrInfo.integrCompID == null) oldIntegrInfo.integrCompID = "";
				if (oldIntegrInfo.integrDesc == null) oldIntegrInfo.integrDesc = "";
				if (oldIntegrInfo.integrWithComp == null) oldIntegrInfo.integrWithComp = "";
				if (oldIntegrInfo.integrOrder == null) oldIntegrInfo.integrOrder = "";
				if (oldIntegrInfo.integrReadyNeed == null) oldIntegrInfo.integrReadyNeed = "";

				newIntegrInfo.integrCompID 		= 	aIntegrComID[i];
				newIntegrInfo.integrDesc 		= 	aIntegrDesc[i];
				newIntegrInfo.integrWithComp 	= 	aIntegrWithCom[i];
				newIntegrInfo.integrOrder 		= 	aIntegrOrder[i];
				newIntegrInfo.integrReadyNeed 	= 	aIntegrReady[i];
				newIntegrInfo.integrID = Long.parseLong(aIntegrID[i]);

				if (   aIntegrComID[i].compareToIgnoreCase(oldIntegrInfo.integrCompID) != 0
						|| aIntegrDesc[i].compareToIgnoreCase(oldIntegrInfo.integrDesc) != 0
						|| aIntegrWithCom[i].compareToIgnoreCase(oldIntegrInfo.integrWithComp) != 0
						|| aIntegrOrder[i].compareToIgnoreCase(oldIntegrInfo.integrOrder) != 0
						|| aIntegrReady[i].compareToIgnoreCase(oldIntegrInfo.integrReadyNeed) != 0
				)
				{
					addChangeAuto(prjID,
								Constants.ACTION_UPDATE,
								Constants.PL_INTEGRATION_STRATEGY_UPDATE,
								"Product integration",
								aIntegrComID[i],
								oldIntegrInfo.integrCompID);
					vNewIntegr.addElement(newIntegrInfo);
				}
				vErrIntegr.addElement(newIntegrInfo);
			}

			if (ProductIntegration.doUpdateProductIntegration(vNewIntegr,prjID)==0) {
				Vector integrList = ProductIntegration.getIntegrationStratList(prjID);
				if (integrList == null) {
					integrList = new Vector();
				}
				session.setAttribute("ProIntegrList", integrList);
				Fms1Servlet.callPage("plLifecycle.jsp",request,response);
				//Fms1Servlet.callPage("plIntegr_StratList.jsp",request,response);
			} else {
				request.setAttribute("ErrProIntegrList",vErrIntegr);
				request.setAttribute("ErrType","1");
				Fms1Servlet.callPage("plIntegr_StratUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeletePLIntegrationStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector vErrIntegr = new Vector();
			int size = 0;

			long integrID = Long.parseLong(request.getParameter("DelIntegrID"));

			IntegrStratInfo newIntegrInfo = null;

			String[] aIntegrID 		= request.getParameterValues("integrID");
			String[] aIntegrComID 	= request.getParameterValues("integr_comid");
			String[] aIntegrDesc 	= request.getParameterValues("integr_desc");
			String[] aIntegrWithCom = request.getParameterValues("integr_withcom");
			String[] aIntegrOrder 	= request.getParameterValues("integr_order");
			String[] aIntegrReady 	= request.getParameterValues("integr_readyneed");

			if (aIntegrComID != null) size = aIntegrComID.length;
			for (int i = 0; i < size; i++) {
				newIntegrInfo = new IntegrStratInfo();

				if (aIntegrComID[i] == null) aIntegrComID[i] = "";
				else aIntegrComID[i] = ConvertString.toStandardizeString(aIntegrComID[i]);

				if (aIntegrDesc[i] == null) aIntegrDesc[i] = "";
				else aIntegrDesc[i] = ConvertString.toStandardizeString(aIntegrDesc[i]);

				if (aIntegrWithCom[i] == null) aIntegrWithCom[i] = "";
				else aIntegrWithCom[i] = ConvertString.toStandardizeString(aIntegrWithCom[i]);

				if (aIntegrOrder[i] == null) aIntegrOrder[i] = "";
				else aIntegrOrder[i] = ConvertString.toStandardizeString(aIntegrOrder[i]);

				if (aIntegrReady[i] == null) aIntegrReady[i] = "";
				else aIntegrReady[i] = ConvertString.toStandardizeString(aIntegrReady[i]);


				vErrIntegr.addElement(newIntegrInfo);

			}

			if (ProductIntegration.doDeletePLProIntegration(integrID)) {
				addChangeAuto(	prjID,
								Constants.ACTION_DELETE,
								Constants.PL_INTEGRATION_STRATEGY_DELETE,
								"Product integration",
								null,
								null);
				Vector integrList = ProductIntegration.getIntegrationStratList(prjID);
				if (integrList == null) {
					integrList = new Vector();
				}
				session.setAttribute("ProIntegrList", integrList);
			} else {
				request.setAttribute("ErrProIntegrList",vErrIntegr);
				request.setAttribute("ErrType","1");
			}
			Fms1Servlet.callPage("plIntegr_StratUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	landd add Product integration strategy end

	// Interface start
	public static final void doPLFsoftInterfacePrepareAdd(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plFsoftInterfaceAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLCustomerInterfacePrepareAdd(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plCustomerInterfaceAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLOtherProjectInterfacePrepareAdd(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plOtherProjectInterfaceAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLFsoftInterfacePrepareUpdate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plFsoftInterfaceUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLCustomerInterfacePrepareUpdate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plCustomerInterfaceUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLOtherProjectInterfacePrepareUpdate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plOtherProjectInterfaceUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLFsoftInterfaceAdd(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector errDataVector = new Vector();
			InterfaceInfo info = null;
			int iRet = 0;			
			boolean userFilterErr = false;

			String[] aIFunction = request.getParameterValues("interface_function");
			String[] aIContactP = request.getParameterValues("interface_contactPerson");
			String[] aIContactA = request.getParameterValues("interface_contactAddress");
			String[] aIContactAcc = request.getParameterValues("strAccountName");
			String[] aIResp = request.getParameterValues("interface_resp");


			int size = aIFunction.length;
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			for (int i = 0; i < size; i++) {
				if (aIFunction[i] == null || "".equals(aIFunction[i].trim())) continue;
				info = new InterfaceInfo();

				info.function = ConvertString.toStandardizeString(aIFunction[i].trim());


				if (aIContactP[i] == null) aIContactP[i] = "";
				info.contactPerson = ConvertString.toStandardizeString(aIContactP[i].trim());


				if (aIContactA[i] == null) aIContactA[i] = "";
				info.contact = ConvertString.toStandardizeString(aIContactA[i].trim());
				
				if (aIContactAcc[i] == null) aIContactAcc[i] = "";
				info.contactAccount = ConvertString.toStandardizeString(aIContactAcc[i].trim());

				if (aIResp[i] == null) aIResp[i] = "";
				info.responsibility = ConvertString.toStandardizeString(aIResp[i].trim());

				info.department = "";
				info.dependency = "";
				info.otherProjName = "";

				info.type = 1; // Fsoft interface
				
				UserInfo userInfo = UserProfileCaller.checkUserFilter(request, aIContactAcc[i], null);
				if (userInfo != null){					
					 if (iRet != 1) iRet = Interface.doAddInterfaces(info, prjPlanID);

					 // If error then add to vector
					 if (iRet != 0) {					
						 errDataVector.addElement(info);
					 } else {
						 addChangeAuto(  prjID,
										 Constants.ACTION_ADD,
										 Constants.PL_INTERFACE_FSOFT_ADD,
										 "Interface Type",
										 info.type+"",
										 null);
					 }

				} else {
					userFilterErr = true;
					errDataVector.addElement(info);	
				}				
			}

			if ( iRet == 0 && !userFilterErr) {
				Vector interfaceList = Project.getPLInterfaceList(prjPlanID,0);
				if (interfaceList == null) {
					interfaceList = new Vector();
				}
				session.setAttribute("plInterfaceList", interfaceList);
				Fms1Servlet.callPage("plStructure.jsp",request,response);
			} else {
				request.setAttribute("ErrFsoftInterfaceList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("plFsoftInterfaceAdd.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLCustomerInterfaceAdd(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector errDataVector = new Vector();
			InterfaceInfo info = null;
			int iRet = 0;
			boolean userFilterErr = false;

			String[] aIDepart = request.getParameterValues("interface_department");
			String[] aIContactP = request.getParameterValues("interface_contactPerson");
			String[] aIContactA = request.getParameterValues("interface_contactAddress");
			String[] aIContactAcc = request.getParameterValues("strAccountName");
			String[] aIResp = request.getParameterValues("interface_resp");


			int size = aIDepart.length;
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			for (int i = 0; i < size; i++) {
				if (aIDepart[i] == null || "".equals(aIDepart[i].trim())) continue;
				info = new InterfaceInfo();

				info.department = ConvertString.toStandardizeString(aIDepart[i].trim());


				if (aIContactP[i] == null) aIContactP[i] = "";
				info.contactPerson = ConvertString.toStandardizeString(aIContactP[i].trim());


				if (aIContactA[i] == null) aIContactA[i] = "";
				info.contact = ConvertString.toStandardizeString(aIContactA[i].trim());

				if (aIContactAcc[i] == null) aIContactAcc[i] = "";
				info.contactAccount = ConvertString.toStandardizeString(aIContactAcc[i].trim());

				if (aIResp[i] == null) aIResp[i] = "";
				info.responsibility = ConvertString.toStandardizeString(aIResp[i].trim());

				info.function = "";
				info.dependency = "";
				info.otherProjName = "";

				info.type = 2; // Customer interface

				UserInfo userInfo = UserProfileCaller.checkUserFilter(request, aIContactAcc[i], null);
				if (userInfo != null){					
					if (iRet != 1) iRet = Interface.doAddInterfaces(info, prjPlanID);

					// If error then add to vector
					if (iRet != 0) {					
						errDataVector.addElement(info);
					} else {
						addChangeAuto(  prjID,
										Constants.ACTION_ADD,
										Constants.PL_INTERFACE_CUSTOMER_ADD,
										"Interface Type",
										info.type+"",
										null);
					}
				} else {
					userFilterErr = true;
					errDataVector.addElement(info);	
				}
			}

			if ( iRet == 0 && !userFilterErr) {
				Vector interfaceList = Project.getPLInterfaceList(prjPlanID,0);
				if (interfaceList == null) {
					interfaceList = new Vector();
				}
				session.setAttribute("plInterfaceList", interfaceList);
				Fms1Servlet.callPage("plStructure.jsp",request,response);
			} else {
				request.setAttribute("ErrCustomerInterfaceList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("plCustomerInterfaceAdd.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLOtherProjectInterfaceAdd(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);

			Vector errDataVector = new Vector();
			InterfaceInfo info = null;
			int iRet = 0;
			boolean userFilterErr = false;

			String[] aIProjectName = request.getParameterValues("interface_project");
			String[] aIContactP = request.getParameterValues("interface_contactPerson");
			String[] aIContactA = request.getParameterValues("interface_contactAddress");
			String[] aIContactAcc = request.getParameterValues("strAccountName");
			String[] aIDepend = request.getParameterValues("interface_depend");


			int size = aIProjectName.length;

			for (int i = 0; i < size; i++) {
				if (aIProjectName[i] == null || "".equals(aIProjectName[i].trim())) continue;
				info = new InterfaceInfo();

				info.otherProjName = ConvertString.toStandardizeString(aIProjectName[i].trim());

				if (aIContactP[i] == null) aIContactP[i] = "";
				info.contactPerson = ConvertString.toStandardizeString(aIContactP[i].trim());


				if (aIContactA[i] == null) aIContactA[i] = "";
				info.contact = ConvertString.toStandardizeString(aIContactA[i].trim());
				
				if (aIContactAcc[i] == null) aIContactAcc[i] = "";
				info.contactAccount = ConvertString.toStandardizeString(aIContactAcc[i].trim());

				if (aIDepend[i] == null) aIDepend[i] = "";
				info.dependency = ConvertString.toStandardizeString(aIDepend[i].trim());

				info.responsibility = "";
				info.function = "";

				info.type = 3; // Other project

				UserInfo userInfo = UserProfileCaller.checkUserFilter(request, aIContactAcc[i], null);
				if (userInfo != null){	
					if (iRet != 1) iRet = Interface.doAddInterfaces(info, prjPlanID);

					if (iRet != 0) {					
						errDataVector.addElement(info);
					} else {
						addChangeAuto(  prjID,
										Constants.ACTION_ADD,
										Constants.PL_INTERFACE_OTHER_PROJECT_ADD,
										"Interface Type",
										info.type+"",
										null);
					}
				} else {
					userFilterErr = true;
					errDataVector.addElement(info);	
				}
			}

			if ( iRet == 0 && !userFilterErr) {
				Vector interfaceList = Project.getPLInterfaceList(prjPlanID,0);
				if (interfaceList == null) {
					interfaceList = new Vector();
				}
				session.setAttribute("plInterfaceList", interfaceList);
				Fms1Servlet.callPage("plStructure.jsp",request,response);
			} else {
				request.setAttribute("ErrOtherProjectInterfaceList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("plOtherProjectInterfaceAdd.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLFsoftInterfaceUpdate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			boolean userFilterErr = false;
			int userErrIdx = 0;
			UserInfo userInfo = null;

			int size = 0;
			Vector vNewInterface = new Vector();
			Vector vErrInterface = new Vector();
			Vector vOldInterface = (Vector) session.getAttribute("FsoftInterfaceList");

			InterfaceInfo newInterfaceInfo = null;
			InterfaceInfo oldInterfaceInfo = null;

			String[] aIID = request.getParameterValues("interfaceID");
			String[] aIFunction = request.getParameterValues("interface_function");
			String[] aIContactP = request.getParameterValues("interface_contactPerson");
			String[] aIContactA = request.getParameterValues("interface_contactAddress");
			String[] aIContactAcc = request.getParameterValues("strAccountName");
			String[] aIResp = request.getParameterValues("interface_resp");

			if (aIID != null) size = aIID.length;
			for (int i = 0; i < size; i++) {
				if ("0".equals(aIID[i])) continue;
				newInterfaceInfo = new InterfaceInfo();
				oldInterfaceInfo = (InterfaceInfo) vOldInterface.elementAt(i);

				if (aIFunction[i] == null) aIFunction[i] = "";
				else aIFunction[i] = ConvertString.toStandardizeString(aIFunction[i]);

				if (aIContactP[i] == null) aIContactP[i] = "";
				else aIContactP[i] = ConvertString.toStandardizeString(aIContactP[i]);

				if (aIContactA[i] == null) aIContactA[i] = "";
				else aIContactA[i] = ConvertString.toStandardizeString(aIContactA[i]);
				
				if (aIContactAcc[i] == null) aIContactAcc[i] = "";
				aIContactAcc[i] = ConvertString.toStandardizeString(aIContactAcc[i].trim());

				if (aIResp[i] == null) aIResp[i] = "";
				else aIResp[i] = ConvertString.toStandardizeString(aIResp[i]);

				if (oldInterfaceInfo.function == null) oldInterfaceInfo.function = "";
				if (oldInterfaceInfo.contactPerson == null) oldInterfaceInfo.contactPerson = "";
				if (oldInterfaceInfo.contact == null) oldInterfaceInfo.contact = "";
				if (oldInterfaceInfo.contactAccount == null) oldInterfaceInfo.contactAccount = "";
				if (oldInterfaceInfo.responsibility == null) oldInterfaceInfo.responsibility = "";

				newInterfaceInfo.interfaceID = Long.parseLong(aIID[i]);
				newInterfaceInfo.function = aIFunction[i];
				newInterfaceInfo.contactPerson = 	aIContactP[i];
				newInterfaceInfo.contact = aIContactA[i];
				newInterfaceInfo.contactAccount = aIContactAcc[i];
				newInterfaceInfo.responsibility = aIResp[i];

				newInterfaceInfo.otherProjName = "";
				newInterfaceInfo.department = "";
				newInterfaceInfo.dependency = "";

				if (!userFilterErr) {
					userInfo = UserProfileCaller.checkUserFilter(request, aIContactAcc[i], null);
					if (userInfo != null){
						if (   aIFunction[i].compareToIgnoreCase(oldInterfaceInfo.function) != 0
								|| aIContactP[i].compareToIgnoreCase(oldInterfaceInfo.contactPerson) != 0
								|| aIContactA[i].compareToIgnoreCase(oldInterfaceInfo.contact) != 0
								|| aIContactAcc[i].compareToIgnoreCase(oldInterfaceInfo.contactAccount) != 0
								|| aIResp[i].compareToIgnoreCase(oldInterfaceInfo.responsibility) != 0
						)
						{
							addChangeAuto(prjID,
										Constants.ACTION_UPDATE,
										Constants.PL_INTERFACE_FSOFT_UPDATE,
										"Interface function",
										aIFunction[i],
										oldInterfaceInfo.function);
	
							vNewInterface.addElement(newInterfaceInfo);
						}
					} else {
						userErrIdx = i;
						userFilterErr = true;						
					}
				}
				vErrInterface.addElement(newInterfaceInfo);
			}

			if (!userFilterErr && Interface.doUpdateInterface(vNewInterface,prjID)==0) {
				Vector interfaceList = Project.getPLInterfaceList(prjPlanID,0);
				if (interfaceList == null) {
					interfaceList = new Vector();
				}
				session.setAttribute("plInterfaceList", interfaceList);
				Fms1Servlet.callPage("plStructure.jsp",request,response);
			} else {
				request.setAttribute("ErrFsoftInterfaceList",vErrInterface);
				if (!userFilterErr) request.setAttribute("ErrType","1");
				else request.setAttribute("UserErrIdx",""+userErrIdx);
				Fms1Servlet.callPage("plFsoftInterfaceUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLCustomerInterfaceUpdate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			boolean userFilterErr = false;
			int userErrIdx = 0;
			UserInfo userInfo = null;
			
			int size = 0;			
			Vector vNewInterface = new Vector();
			Vector vErrInterface = new Vector();
			Vector vOldInterface = (Vector) session.getAttribute("CustomerInterfaceList");

			InterfaceInfo newInterfaceInfo = null;
			InterfaceInfo oldInterfaceInfo = null;

			String[] aIID = request.getParameterValues("interfaceID");
			String[] aIDepart = request.getParameterValues("interface_department");
			String[] aIContactP = request.getParameterValues("interface_contactPerson");
			String[] aIContactA = request.getParameterValues("interface_contactAddress");
			String[] aIContactAcc = request.getParameterValues("strAccountName");
			String[] aIResp = request.getParameterValues("interface_resp");

			if (aIID != null) size = aIID.length;
			for (int i = 0; i < size; i++) {
				if ("0".equals(aIID[i])) continue;
				newInterfaceInfo = new InterfaceInfo();
				oldInterfaceInfo = (InterfaceInfo) vOldInterface.elementAt(i);

				if (aIDepart[i] == null) aIDepart[i] = "";
				else aIDepart[i] = ConvertString.toStandardizeString(aIDepart[i]);

				if (aIContactP[i] == null) aIContactP[i] = "";
				else aIContactP[i] = ConvertString.toStandardizeString(aIContactP[i]);

				if (aIContactA[i] == null) aIContactA[i] = "";
				else aIContactA[i] = ConvertString.toStandardizeString(aIContactA[i]);
				
				if (aIContactAcc[i] == null) aIContactAcc[i] = "";
				aIContactAcc[i] = ConvertString.toStandardizeString(aIContactAcc[i].trim());

				if (aIResp[i] == null) aIResp[i] = "";
				else aIResp[i] = ConvertString.toStandardizeString(aIResp[i]);

				if (oldInterfaceInfo.department == null) oldInterfaceInfo.department = "";
				if (oldInterfaceInfo.contactPerson == null) oldInterfaceInfo.contactPerson = "";
				if (oldInterfaceInfo.contact == null) oldInterfaceInfo.contact = "";
				if (oldInterfaceInfo.contactAccount == null) oldInterfaceInfo.contactAccount = "";
				if (oldInterfaceInfo.responsibility == null) oldInterfaceInfo.responsibility = "";

				newInterfaceInfo.interfaceID = Long.parseLong(aIID[i]);
				newInterfaceInfo.department = aIDepart[i];
				newInterfaceInfo.contactPerson = 	aIContactP[i];
				newInterfaceInfo.contact = aIContactA[i];
				newInterfaceInfo.contactAccount = aIContactAcc[i];
				newInterfaceInfo.responsibility = aIResp[i];

				newInterfaceInfo.otherProjName = "";
				newInterfaceInfo.function = "";
				newInterfaceInfo.dependency = "";
				
				if (!userFilterErr) {
					userInfo = UserProfileCaller.checkUserFilter(request, aIContactAcc[i], null);
					if (userInfo != null){
						if (   aIDepart[i].compareToIgnoreCase(oldInterfaceInfo.department) != 0
							|| aIContactP[i].compareToIgnoreCase(oldInterfaceInfo.contactPerson) != 0
							|| aIContactA[i].compareToIgnoreCase(oldInterfaceInfo.contact) != 0
							|| aIContactAcc[i].compareToIgnoreCase(oldInterfaceInfo.contactAccount) != 0
							|| aIResp[i].compareToIgnoreCase(oldInterfaceInfo.responsibility) != 0
						)
						{
							addChangeAuto(prjID,
										Constants.ACTION_UPDATE,
										Constants.PL_INTERFACE_CUSTOMER_UPDATE,
										"Interface department",
										aIDepart[i],
										oldInterfaceInfo.department);
		
							vNewInterface.addElement(newInterfaceInfo);
						}
					} else {
						userErrIdx = i;
						userFilterErr = true;						
					}
				}
				vErrInterface.addElement(newInterfaceInfo);
			}

			if (!userFilterErr && Interface.doUpdateInterface(vNewInterface,prjID)==0) {
				Vector interfaceList = Project.getPLInterfaceList(prjPlanID,0);
				if (interfaceList == null) {
					interfaceList = new Vector();
				}
				session.setAttribute("plInterfaceList", interfaceList);
				Fms1Servlet.callPage("plStructure.jsp",request,response);
			} else {
				request.setAttribute("ErrCustomerInterfaceList",vErrInterface);
				if (!userFilterErr) request.setAttribute("ErrType","1");
				else request.setAttribute("UserErrIdx",""+userErrIdx);
				Fms1Servlet.callPage("plCustomerInterfaceUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static final void doPLOtherProjectInterfaceUpdate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			boolean userFilterErr = false;
			int userErrIdx = 0;
			UserInfo userInfo = null;

			int size = 0;
			Vector vNewInterface = new Vector();
			Vector vErrInterface = new Vector();
			Vector vOldInterface = (Vector) session.getAttribute("OtherProjectInterfaceList");

			InterfaceInfo newInterfaceInfo = null;
			InterfaceInfo oldInterfaceInfo = null;

			String[] aIID 			= request.getParameterValues("interfaceID");
			String[] aIProjectName 	= request.getParameterValues("interface_project");
			String[] aIContactP 	= request.getParameterValues("interface_contactPerson");
			String[] aIContactA 	= request.getParameterValues("interface_contactAddress");
			String[] aIContactAcc = request.getParameterValues("strAccountName");
			String[] aIDepend 		= request.getParameterValues("interface_depend");

			if (aIID != null) size = aIID.length;
			for (int i = 0; i < size; i++) {
				if ("0".equals(aIID[i])) continue;
				newInterfaceInfo = new InterfaceInfo();
				oldInterfaceInfo = (InterfaceInfo) vOldInterface.elementAt(i);

				if (aIProjectName[i] == null) aIProjectName[i] = "";
				else aIProjectName[i] = ConvertString.toStandardizeString(aIProjectName[i]);

				if (aIContactP[i] == null) aIContactP[i] = "";
				else aIContactP[i] = ConvertString.toStandardizeString(aIContactP[i]);

				if (aIContactA[i] == null) aIContactA[i] = "";
				else aIContactA[i] = ConvertString.toStandardizeString(aIContactA[i]);
				
				if (aIContactAcc[i] == null) aIContactAcc[i] = "";
				aIContactAcc[i] = ConvertString.toStandardizeString(aIContactAcc[i].trim());

				if (aIDepend[i] == null) aIDepend[i] = "";
				else aIDepend[i] = ConvertString.toStandardizeString(aIDepend[i]);

				if (oldInterfaceInfo.otherProjName == null) oldInterfaceInfo.otherProjName = "";
				if (oldInterfaceInfo.contactPerson == null) oldInterfaceInfo.contactPerson = "";
				if (oldInterfaceInfo.contact == null) oldInterfaceInfo.contact = "";
				if (oldInterfaceInfo.contactAccount == null) oldInterfaceInfo.contactAccount = "";
				if (oldInterfaceInfo.dependency == null) oldInterfaceInfo.dependency = "";

				newInterfaceInfo.interfaceID 	= Long.parseLong(aIID[i]);
				newInterfaceInfo.otherProjName 	= aIProjectName[i];
				newInterfaceInfo.contactPerson 	= aIContactP[i];
				newInterfaceInfo.contact 		= aIContactA[i];
				newInterfaceInfo.contactAccount = aIContactAcc[i];
				newInterfaceInfo.dependency 	= aIDepend[i];

				newInterfaceInfo.department = "";
				newInterfaceInfo.function = "";
				newInterfaceInfo.responsibility = "";
				
				if (!userFilterErr) {
					userInfo = UserProfileCaller.checkUserFilter(request, aIContactAcc[i], null);
					if (userInfo != null){
						if (    aIProjectName[i].compareToIgnoreCase(oldInterfaceInfo.otherProjName) != 0
												|| aIContactP[i].compareToIgnoreCase(oldInterfaceInfo.contactPerson) != 0
												|| aIContactA[i].compareToIgnoreCase(oldInterfaceInfo.contact) != 0
												|| aIContactAcc[i].compareToIgnoreCase(oldInterfaceInfo.contactAccount) != 0
												|| aIDepend[i].compareToIgnoreCase(oldInterfaceInfo.dependency) != 0
						)
						{
							addChangeAuto(prjID,
										Constants.ACTION_UPDATE,
										Constants.PL_INTERFACE_OTHER_PROJECT_UPDATE,
										"Interface other project",
										aIProjectName[i],
										oldInterfaceInfo.otherProjName);
	
							vNewInterface.addElement(newInterfaceInfo);
						}
					} else {
						userErrIdx = i;
						userFilterErr = true;						
					}
				}
				vErrInterface.addElement(newInterfaceInfo);				
			}

			if (!userFilterErr && Interface.doUpdateInterface(vNewInterface,prjID)==0) {
				Vector interfaceList = Project.getPLInterfaceList(prjPlanID,0);
				if (interfaceList == null) {
					interfaceList = new Vector();
				}
				session.setAttribute("plInterfaceList", interfaceList);
				Fms1Servlet.callPage("plStructure.jsp",request,response);
			} else {
				request.setAttribute("ErrOtherProjectInterfaceList",vErrInterface);
				if (!userFilterErr) request.setAttribute("ErrType","1");
				else request.setAttribute("UserErrIdx",""+userErrIdx);
				Fms1Servlet.callPage("plOtherProjectInterfaceUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLFsoftInterfaceDelete(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);

			int size = 0;
			long delInterfaceID = 0;

			Vector vErrInterface = new Vector();

			InterfaceInfo newInterfaceInfo = null;

			String strDelInterfaceID = request.getParameter("delInterfaceID");
			if (strDelInterfaceID != null) delInterfaceID = Long.parseLong(strDelInterfaceID);

			String[] aIID = request.getParameterValues("interfaceID");
			String[] aIFunction = request.getParameterValues("interface_function");
			String[] aIContactP = request.getParameterValues("interface_contactPerson");
			String[] aIContactA = request.getParameterValues("interface_contactAddress");
			String[] aIContactAcc = request.getParameterValues("strAccountName");
			String[] aIResp = request.getParameterValues("interface_resp");

			if (aIID != null) size = aIID.length;
			for (int i = 0; i < size; i++) {
				if ("".equals(aIFunction[i])) continue;
				newInterfaceInfo = new InterfaceInfo();

				if (aIFunction[i] == null) aIFunction[i] = "";
				else aIFunction[i] = ConvertString.toStandardizeString(aIFunction[i]);

				if (aIContactP[i] == null) aIContactP[i] = "";
				else aIContactP[i] = ConvertString.toStandardizeString(aIContactP[i]);

				if (aIContactA[i] == null) aIContactA[i] = "";
				else aIContactA[i] = ConvertString.toStandardizeString(aIContactA[i]);
				
				if (aIContactAcc[i] == null) aIContactAcc[i] = "";
				aIContactAcc[i] = ConvertString.toStandardizeString(aIContactAcc[i].trim());

				if (aIResp[i] == null) aIResp[i] = "";
				else aIResp[i] = ConvertString.toStandardizeString(aIResp[i]);

				newInterfaceInfo.interfaceID = Long.parseLong(aIID[i]);
				newInterfaceInfo.function = aIFunction[i];
				newInterfaceInfo.contactPerson = 	aIContactP[i];
				newInterfaceInfo.contact = aIContactA[i];
				newInterfaceInfo.contactAccount = aIContactAcc[i];
				newInterfaceInfo.responsibility = aIResp[i];

				newInterfaceInfo.otherProjName = "";
				newInterfaceInfo.department = "";
				newInterfaceInfo.dependency = "";

				vErrInterface.addElement(newInterfaceInfo);
			}

			if (Interface.doDeletePLInterface(delInterfaceID)) {
				addChangeAuto(	prjID,
								Constants.ACTION_DELETE,
								Constants.PL_INTERFACE_FSOFT_DELETE,
								"Interface",
								null,
								null);
				Vector interfaceList = Project.getPLInterfaceList(prjPlanID,1);
				if (interfaceList == null) {
					interfaceList = new Vector();
				}
				session.setAttribute("FsoftInterfaceList", interfaceList);
			} else {
				request.setAttribute("ErrFsoftInterfaceList",vErrInterface);
				request.setAttribute("ErrType","1");
			}
			Fms1Servlet.callPage("plFsoftInterfaceUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLCustomerInterfaceDelete(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);

			int size = 0;
			long delInterfaceID = 0;
			Vector vErrInterface = new Vector();

			InterfaceInfo newInterfaceInfo = null;

			String strDelInterfaceID = request.getParameter("delInterfaceID");
			if (strDelInterfaceID != null) delInterfaceID = Long.parseLong(strDelInterfaceID);
			String[] aIID = request.getParameterValues("interfaceID");
			String[] aIDepart = request.getParameterValues("interface_department");
			String[] aIContactP = request.getParameterValues("interface_contactPerson");
			String[] aIContactA = request.getParameterValues("interface_contactAddress");
			String[] aIContactAcc = request.getParameterValues("strAccountName");
			String[] aIResp = request.getParameterValues("interface_resp");

			if (aIID != null) size = aIID.length;
			for (int i = 0; i < size; i++) {
				if ("".equals(aIDepart[i])) continue;
				newInterfaceInfo = new InterfaceInfo();

				if (aIDepart[i] == null) aIDepart[i] = "";
				else aIDepart[i] = ConvertString.toStandardizeString(aIDepart[i]);

				if (aIContactP[i] == null) aIContactP[i] = "";
				else aIContactP[i] = ConvertString.toStandardizeString(aIContactP[i]);

				if (aIContactA[i] == null) aIContactA[i] = "";
				else aIContactA[i] = ConvertString.toStandardizeString(aIContactA[i]);

				if (aIContactAcc[i] == null) aIContactAcc[i] = "";
				aIContactAcc[i] = ConvertString.toStandardizeString(aIContactAcc[i].trim());
				
				if (aIResp[i] == null) aIResp[i] = "";
				else aIResp[i] = ConvertString.toStandardizeString(aIResp[i]);

				newInterfaceInfo.interfaceID = Long.parseLong(aIID[i]);
				newInterfaceInfo.department = aIDepart[i];
				newInterfaceInfo.contactPerson = 	aIContactP[i];
				newInterfaceInfo.contact = aIContactA[i];
				newInterfaceInfo.contactAccount = aIContactAcc[i];
				newInterfaceInfo.responsibility = aIResp[i];

				newInterfaceInfo.otherProjName = "";
				newInterfaceInfo.function = "";
				newInterfaceInfo.dependency = "";

				vErrInterface.addElement(newInterfaceInfo);
			}

			if (Interface.doDeletePLInterface(delInterfaceID)) {
				addChangeAuto(	prjID,
								Constants.ACTION_DELETE,
								Constants.PL_INTERFACE_CUSTOMER_DELETE,
								"Interface",
								null,
								null);
				Vector interfaceList = Project.getPLInterfaceList(prjPlanID,2);
				if (interfaceList == null) {
					interfaceList = new Vector();
				}
				session.setAttribute("CustomerInterfaceList", interfaceList);
			} else {
				request.setAttribute("ErrCustomerInterfaceList",vErrInterface);
				request.setAttribute("ErrType","1");
			}
			Fms1Servlet.callPage("plCustomerInterfaceUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPLOtherProjectInterfaceDelete(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long prjPlanID = Project.getPrjPlanIDFromPrjID(prjID);

			int size = 0;
			long delInterfaceID = 0;
			Vector vErrInterface = new Vector();

			InterfaceInfo newInterfaceInfo = null;

			String strDelInterfaceID = request.getParameter("delInterfaceID");
			if (strDelInterfaceID != null) delInterfaceID = Long.parseLong(strDelInterfaceID);
			String[] aIID 			= request.getParameterValues("interfaceID");
			String[] aIProjectName 	= request.getParameterValues("interface_project");
			String[] aIContactP 	= request.getParameterValues("interface_contactPerson");
			String[] aIContactA 	= request.getParameterValues("interface_contactAddress");
			String[] aIContactAcc = request.getParameterValues("strAccountName");
			String[] aIDepend 		= request.getParameterValues("interface_depend");

			if (aIID != null) size = aIID.length;
			for (int i = 0; i < size; i++) {
				if ("".equals(aIContactP[i])) continue;
				newInterfaceInfo = new InterfaceInfo();

				if (aIProjectName[i] == null) aIProjectName[i] = "";
				else aIProjectName[i] = ConvertString.toStandardizeString(aIProjectName[i]);

				if (aIContactP[i] == null) aIContactP[i] = "";
				else aIContactP[i] = ConvertString.toStandardizeString(aIContactP[i]);

				if (aIContactA[i] == null) aIContactA[i] = "";
				else aIContactA[i] = ConvertString.toStandardizeString(aIContactA[i]);

				if (aIContactAcc[i] == null) aIContactAcc[i] = "";
				aIContactAcc[i] = ConvertString.toStandardizeString(aIContactAcc[i].trim());
				
				if (aIDepend[i] == null) aIDepend[i] = "";
				else aIDepend[i] = ConvertString.toStandardizeString(aIDepend[i]);

				newInterfaceInfo.interfaceID 	= Long.parseLong(aIID[i]);
				newInterfaceInfo.otherProjName 	= aIProjectName[i];
				newInterfaceInfo.contactPerson 	= aIContactP[i];
				newInterfaceInfo.contact 		= aIContactA[i];
				newInterfaceInfo.contactAccount = aIContactAcc[i];
				newInterfaceInfo.dependency 	= aIDepend[i];

				newInterfaceInfo.department = "";
				newInterfaceInfo.function = "";
				newInterfaceInfo.responsibility = "";

				vErrInterface.addElement(newInterfaceInfo);

			}

			if (Interface.doDeletePLInterface(delInterfaceID)) {
				addChangeAuto(	prjID,
								Constants.ACTION_DELETE,
								Constants.PL_INTERFACE_OTHER_PROJECT_DELETE,
								"Interface",
								null,
								null);
				Vector interfaceList = Project.getPLInterfaceList(prjPlanID,3);
				if (interfaceList == null) {
					interfaceList = new Vector();
				}
				session.setAttribute("OtherProjectInterfaceList", interfaceList);
			} else {
				request.setAttribute("ErrOtherProjectInterfaceList",vErrInterface);
				request.setAttribute("ErrType","1");
			}
			Fms1Servlet.callPage("plOtherProjectInterfaceUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	// Interface end


	// landd add Requirement changes start
	public static final void doViewPLReqChanges(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final ReqChangesInfo reqInfo = Requirement.getChangesRequirement(prjID);
			session.setAttribute("ReqChangesInfo", reqInfo);
			Fms1Servlet.callPage("plReqChangesMng.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareAddPLReqChanges(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plReqChangesAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPrepareUpdatePLReqChanges(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plReqChangesUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAddPLReqChanges(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			ReqChangesInfo reqInfo = null;
			int iRet = 0;

			String aReqDesc = ""; // request.getParameter("req_desc");
			String aReqCreator = request.getParameter("req_creator");
			String aReqReviewer = request.getParameter("req_reviewer");
			String aReqApprover = request.getParameter("req_approver");
			String aReqLogLocation = request.getParameter("req_logged_location");

			reqInfo = new ReqChangesInfo();

			reqInfo.reqChangesDesc = ConvertString.toStandardizeString(aReqDesc.trim());

			// creator
			if (aReqCreator == null) aReqCreator = "";
			reqInfo.reqCreator = ConvertString.toStandardizeString(aReqCreator.trim());

			// Reviewer
			if (aReqReviewer == null) aReqReviewer = "";
			reqInfo.reqReviewer = ConvertString.toStandardizeString(aReqReviewer.trim());

			// Approver
			if (aReqApprover == null) aReqApprover = "";
			reqInfo.reqApprover = ConvertString.toStandardizeString(aReqApprover.trim());

			// Log
			if (aReqLogLocation == null) aReqLogLocation = "";
			reqInfo.reqLogLocation = ConvertString.toStandardizeString(aReqLogLocation.trim());

			// If not system error then call add sub team
			iRet = Requirement.doAddReqChanges(reqInfo, prjID);

			// If error then add to vector
			if (iRet == 0) {
				addChangeAuto(  prjID,
								Constants.ACTION_ADD,
								Constants.PL_REQ_CHANGES_MNG_ADD,
								"Requirement Changes",
								reqInfo.reqChangesDesc,
								null);

				ReqChangesInfo info = Requirement.getChangesRequirement(prjID);
				if (info == null) {
					info = new ReqChangesInfo();
				}
				session.setAttribute("ReqChangesInfo", info);
				Fms1Servlet.callPage("plLifecycle.jsp",request,response);
				// Fms1Servlet.callPage("plReqChangesMng.jsp",request,response);
			} else {
				request.setAttribute("ErrReqChangesInfo", reqInfo);
				Fms1Servlet.callPage("plReqChangesAdd.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLReqChanges(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			ReqChangesInfo oldReqInfo = (ReqChangesInfo) session.getAttribute("ReqChangesInfo");
			ReqChangesInfo newReqInfo = null;

			String aReqDesc = null; // request.getParameter("req_desc");
			String aReqCreator = request.getParameter("req_creator");
			String aReqReviewer = request.getParameter("req_reviewer");
			String aReqApprover = request.getParameter("req_approver");
			String aReqLogLocation = request.getParameter("req_logged_location");
			String aReqID = request.getParameter("reqID");


			newReqInfo = new ReqChangesInfo();

			if (aReqDesc == null) aReqDesc = "";
			else aReqDesc = ConvertString.toStandardizeString(aReqDesc);

			if (aReqCreator == null) aReqCreator = "";
			else aReqCreator = ConvertString.toStandardizeString(aReqCreator);

			if (aReqReviewer == null) aReqReviewer = "";
			else aReqReviewer = ConvertString.toStandardizeString(aReqReviewer);

			if (aReqApprover == null) aReqApprover = "";
			else aReqApprover = ConvertString.toStandardizeString(aReqApprover);

			if (aReqLogLocation == null) aReqLogLocation = "";
			else aReqLogLocation = ConvertString.toStandardizeString(aReqLogLocation);

			if (oldReqInfo.reqChangesDesc == null) oldReqInfo.reqChangesDesc = "";
			if (oldReqInfo.reqCreator == null) oldReqInfo.reqCreator = "";
			if (oldReqInfo.reqReviewer == null) oldReqInfo.reqReviewer = "";
			if (oldReqInfo.reqApprover == null) oldReqInfo.reqApprover = "";
			if (oldReqInfo.reqLogLocation == null) oldReqInfo.reqLogLocation = "";


			newReqInfo.reqChangesDesc = aReqDesc;
			newReqInfo.reqCreator = 	aReqCreator;
			newReqInfo.reqReviewer = aReqReviewer;
			newReqInfo.reqApprover = aReqApprover;
			newReqInfo.reqLogLocation = aReqLogLocation;
			newReqInfo.reqChangesID = Long.parseLong(aReqID);

			if (   aReqDesc.compareToIgnoreCase(oldReqInfo.reqChangesDesc) != 0
					|| aReqCreator.compareToIgnoreCase(oldReqInfo.reqCreator) != 0
					|| aReqReviewer.compareToIgnoreCase(oldReqInfo.reqReviewer) != 0
					|| aReqApprover.compareToIgnoreCase(oldReqInfo.reqApprover) != 0
					|| aReqLogLocation.compareToIgnoreCase(oldReqInfo.reqLogLocation) != 0
			)
			{
				addChangeAuto(prjID,
							Constants.ACTION_UPDATE,
							Constants.PL_REQ_CHANGES_MNG_UPDATE,
							"Requirement Changes",
							aReqDesc,
							oldReqInfo.reqChangesDesc);

			}

			if (Requirement.doUpdateReqChanges(newReqInfo,prjID)==0) {
				ReqChangesInfo info = Requirement.getChangesRequirement(prjID);
				if (info == null) {
					info = new ReqChangesInfo();
				}
				session.setAttribute("ReqChangesInfo", info);
				Fms1Servlet.callPage("plLifecycle.jsp",request,response);
				// Fms1Servlet.callPage("plReqChangesMng.jsp",request,response);
			} else {
				request.setAttribute("ErrReqChangesInfo",newReqInfo);
				Fms1Servlet.callPage("plReqChangesUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeletePLReqChanges(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			long reqID = 0;

			ReqChangesInfo newReqInfo = null;

			String aReqDesc = request.getParameter("req_desc");
			String aReqCreator = request.getParameter("req_creator");
			String aReqReviewer = request.getParameter("req_reviewer");
			String aReqApprover = request.getParameter("req_approver");
			String aReqLogLocation = request.getParameter("req_logged_location");
			String aReqID = request.getParameter("reqID");

			newReqInfo = new ReqChangesInfo();
			if (aReqDesc == null) aReqDesc = "";
			else aReqDesc = ConvertString.toStandardizeString(aReqDesc);

			if (aReqCreator == null) aReqCreator = "";
			else aReqCreator = ConvertString.toStandardizeString(aReqCreator);

			if (aReqReviewer == null) aReqReviewer = "";
			else aReqReviewer = ConvertString.toStandardizeString(aReqReviewer);

			if (aReqApprover == null) aReqApprover = "";
			else aReqApprover = ConvertString.toStandardizeString(aReqApprover);

			if (aReqLogLocation == null) aReqLogLocation = "";
			else aReqLogLocation = ConvertString.toStandardizeString(aReqLogLocation);

			newReqInfo.reqChangesDesc = aReqDesc;
			newReqInfo.reqCreator = 	aReqCreator;
			newReqInfo.reqReviewer = aReqReviewer;
			newReqInfo.reqApprover = aReqApprover;
			newReqInfo.reqLogLocation = aReqLogLocation;
			newReqInfo.reqChangesID = Long.parseLong(aReqID);

			reqID = Long.parseLong(aReqID);
			if (Requirement.doDeletePLReqChanges(reqID)) {
				addChangeAuto(	prjID,
								Constants.ACTION_DELETE,
								Constants.PL_REQ_CHANGES_MNG_DELETE,
								"Requirement Changes",
								null,
								null);

				session.removeAttribute("ReqChangesInfo");
				Fms1Servlet.callPage("plLifecycle.jsp",request,response);
				// Fms1Servlet.callPage("plReqChangesMng.jsp",request,response);
			} else {
				request.setAttribute("ErrReqChangesInfo",newReqInfo);
				Fms1Servlet.callPage("plReqChangesUpdate.jsp",request,response);
			}


		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	landd add Requirement changes end
	public static final void doPreparePLInterfaceUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final Vector devList = Assignments.getDevListByProject(prjID);
			session.setAttribute("plDevList", devList);
			Fms1Servlet.callPage("plInterfaceUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doPreparePLInterfaceAdd(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final Vector devList = Assignments.getDevListByProject(prjID);
			session.setAttribute("plDevList", devList);
			Fms1Servlet.callPage("plInterfaceAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAddPLInterface(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			String name = request.getParameter("plInterface_name");
			String position = request.getParameter("plInterface_position");
			String responsibility = request.getParameter("plInterface_responsibility");
			String communication = request.getParameter("plInterface_communication");
			String contact = request.getParameter("plInterface_contact");
			final InterfaceInfo interfaceInfo = new InterfaceInfo();
			interfaceInfo.projectPlanID =Project.getPrjPlanIDFromPrjID(prjID);
			interfaceInfo.name = (name==null)?"":name;
			interfaceInfo.position = (position==null)?"":position;
			interfaceInfo.roleID =Long.parseLong(request.getParameter("plInterface_role"));
			interfaceInfo.responsibility = (responsibility==null)?"":responsibility;
			//if team is not yet set-up then assigned id can be null
			interfaceInfo.assignedID = CommonTools.parseDouble(request.getParameter("plInterface_assignedID"));
			interfaceInfo.communication =  (communication==null)?"":communication;
			interfaceInfo.contact = (contact==null)?"":contact;
			Project.addPLInterface(interfaceInfo);
			addChangeAuto(prjID, Constants.ACTION_ADD, Constants.PL_ORGANIZATION, "Interface>" + name, null, null);
			doLoadPLStructure(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLInterface(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long interfaceID = Long.parseLong(request.getParameter("plInterface_ID"));
			String name = request.getParameter("plInterface_name");
			String position = request.getParameter("plInterface_position");
			String responsibility = request.getParameter("plInterface_responsibility");
			String communication = request.getParameter("plInterface_communication");
			String contact = request.getParameter("plInterface_contact");
			final InterfaceInfo interfaceInfo = new InterfaceInfo();
			interfaceInfo.interfaceID = interfaceID;
			interfaceInfo.projectPlanID =Project.getPrjPlanIDFromPrjID(prjID);
			interfaceInfo.name = (name==null)?"":name;
			interfaceInfo.position = (position==null)?"":position;
			interfaceInfo.roleID =Long.parseLong(request.getParameter("plInterface_role"));
			interfaceInfo.responsibility = (responsibility==null)?"":responsibility;
			//if team is not yet set-up then assigned id can be null
			interfaceInfo.assignedID = CommonTools.parseDouble(request.getParameter("plInterface_assignedID"));
			interfaceInfo.communication =  (communication==null)?"":communication;
			interfaceInfo.contact = (contact==null)?"":contact;
			Project.updatePLInterface(interfaceInfo);
			InterfaceInfo oldInterfaceInfo = (InterfaceInfo) session.getAttribute("plInterfaceInfo");
			String newValue, oldValue;
			newValue = interfaceInfo.name;
			oldValue = oldInterfaceInfo.name;
			if (!newValue.equalsIgnoreCase(oldValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_ORGANIZATION,
					"Interface>Name",
					newValue,
					oldValue);
			newValue = interfaceInfo.position;
			oldValue = oldInterfaceInfo.position;
			if (!newValue.equalsIgnoreCase(oldValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_ORGANIZATION,
					"Interface>Position",
					newValue,
					oldValue);
			newValue = interfaceInfo.responsibility;
			oldValue = oldInterfaceInfo.responsibility;
			if (!newValue.equalsIgnoreCase(oldValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_ORGANIZATION,
					"Interface>Responsibility",
					newValue,
					oldValue);
			newValue = interfaceInfo.communication;
			oldValue = oldInterfaceInfo.communication;
			if (!newValue.equalsIgnoreCase(oldValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_ORGANIZATION,
					"Interface>Communication",
					newValue,
					oldValue);
			newValue = interfaceInfo.contact;
			oldValue = oldInterfaceInfo.contact;
			if (!newValue.equalsIgnoreCase(oldValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_ORGANIZATION,
					"Interface>Tel, Fax, E-mail",
					newValue,
					oldValue);
			newValue = interfaceInfo.getNameOfRole();
			oldValue = oldInterfaceInfo.getNameOfRole();
			if (!newValue.equalsIgnoreCase(oldValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_ORGANIZATION,
					"Interface>Role",
					newValue,
					oldValue);
			Vector devList = (Vector) session.getAttribute("plDevList");
			for (int k = 0; k < devList.size(); k++) {
				PlanInterfacesInfo planInterfacesInfo = (PlanInterfacesInfo)devList.elementAt(k);
				long devID = planInterfacesInfo.getDeveloperId();
				if (devID == interfaceInfo.assignedID)
					newValue = planInterfacesInfo.getDeveloperName();
				if (devID == oldInterfaceInfo.assignedID)
					oldValue = planInterfacesInfo.getDeveloperName();
			}
			if (!newValue.equalsIgnoreCase(oldValue))
				addChangeAuto(
					prjID,
					Constants.ACTION_UPDATE,
					Constants.PL_ORGANIZATION,
					"Interface>Assigned to",
					newValue,
					oldValue);
			doLoadPLStructure(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeletePLInterface(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long interfaceID = Long.parseLong(request.getParameter("plInterface_ID"));
			Project.deletePLInterface(interfaceID);
			InterfaceInfo oldInterfaceInfo = (InterfaceInfo) session.getAttribute("plInterfaceInfo");
			addChangeAuto(
				prjID,
				Constants.ACTION_DELETE,
				Constants.PL_ORGANIZATION,
				"Interface>" + oldInterfaceInfo.name,
				null,
				null);
			doLoadPLStructure(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ------------------------------End of PL Interface------------------------------

	// ------------------------------PL Subcontract ------------------------------ moved to schedulecaller

	// ------------------------------Signature------------------------------
	public static final void doLoadPLSignatureList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		doLoadPLSignatureList(request, response, "");
	}

	public static final void doLoadPLSignatureList(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String index) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector sigList = Project.getApprovalList(prjID, 3);
			if (sigList == null) {
				sigList = new Vector();
			}
			request.setAttribute("PLSigList", sigList);
			Vector changeSigList = Project.getApprovalList(prjID, 4);
			if (changeSigList == null) {
				changeSigList = new Vector();
			}
			request.setAttribute("PLChangeSigList", changeSigList);
			Fms1Servlet.callPage("plSignature.jsp" + index,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdatePLSignature(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long lDeveloperId = Long.parseLong(request.getParameter("woSig_devID"));
			final int iSigApp = Integer.parseInt(request.getParameter("woSig_app"));
			final SignatureInfo signatureInfo = new SignatureInfo();
			signatureInfo.setProjectId(prjID);
			signatureInfo.setDeveloperId(lDeveloperId);
			signatureInfo.setApprovalStatus(iSigApp);
			Project.updateApproval(signatureInfo, 3);
			doLoadPLSignatureList(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAddPLApproval(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long lProjectID = Long.parseLong((String) session.getAttribute("projectID"));
			String strAccountName = request.getParameter("strAccountName");
			String strType = request.getParameter("rdAccountName");
			strAccountName = ConvertString.toStandardizeString(strAccountName);
			UserInfo userInfo = UserHelper.doCheckUserAssigned(3, lProjectID, strAccountName, strType);
			if (userInfo == null || !Project.addApproval(3, userInfo.developerID, lProjectID)){
				request.setAttribute(StringConstants.FILLTER_USER_ERROR, "3");
			}
			doLoadPLSignatureList(request, response, "?Index=" + strAccountName);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doResetPLApproval(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Project.resetApproval(3, prjID);
			doLoadPLSignatureList(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeletePLApproval(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String[] idArray = request.getParameterValues("delWOApprovalID");
			if (idArray != null)
				for (int i = 0; i < idArray.length; i++) {
					final long appID = Long.parseLong(idArray[i]);
					Project.deleteApproval(appID);
				}
			doLoadPLSignatureList(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdateChangeSignature(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final long lDeveloperId = Long.parseLong(request.getParameter("woChangeSig_devID"));
			final int iSigApp = Integer.parseInt(request.getParameter("woChangeSig_app"));
			final SignatureInfo signatureInfo = new SignatureInfo();
			signatureInfo.setProjectId(prjID);
			signatureInfo.setDeveloperId(lDeveloperId);
			signatureInfo.setApprovalStatus(iSigApp);
			Project.updateApproval(signatureInfo, 4);
			doLoadPLSignatureList(request, response, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doAddChangeApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long lProjectID = Long.parseLong((String) session.getAttribute("projectID"));
			String strAccountName = request.getParameter("strAccountName");
			String strType = request.getParameter("rdAccountName");
			strAccountName = ConvertString.toStandardizeString(strAccountName);
			UserInfo userInfo = UserHelper.doCheckUserAssigned(4, lProjectID, strAccountName, strType);
			if (userInfo == null || !Project.addApproval(4, userInfo.developerID, lProjectID)){
				request.setAttribute(StringConstants.FILLTER_USER_ERROR, "4");
			}
			doLoadPLSignatureList(request, response, "?Index=" + strAccountName);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeleteChangeApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final String[] idArray = request.getParameterValues("delWOApprovalID2");
			if (idArray != null)
				for (int i = 0; i < idArray.length; i++) {
					final long appID = Long.parseLong(idArray[i]);
					Project.deleteApproval(appID);
				}
			doLoadPLSignatureList(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doResetChangeApproval(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Project.resetApproval(4, prjID);
			doLoadPLSignatureList(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ------------------------------End of signature------------------------------

	public static void addChangeAuto(long prjID, //all table ids are long (application standart)
	int ActionType, int Location, String name, Object newValue, Object oldValue) {
		try {
			String item = "";
			String changes = "";
			String oldVl = (String) oldValue;
			String newVl = (String) newValue;
			//check if every one is approved
			Vector sigList = Project.getApprovalList(prjID, 3);
			SignatureInfo signatureInfo = new SignatureInfo();
			if (sigList.size() == 0) {
				return;
			}
			for (int i = 0; i < sigList.size(); i++) {
				signatureInfo = (SignatureInfo)sigList.elementAt(i);
				if (signatureInfo.getApprovalStatus() != 1) {
					return;
				}
			}
			//////////////////////////////////////////
			if (oldVl == null)
				oldVl = "";
			if (newVl == null)
				newVl = "";
			final String reason = "";
			final String version = "";
			final String note = "";
			final WOChangeInfo info = new WOChangeInfo();
			switch (Location) {
				case Constants.PL_OVERVIEW :
					item = "PL>Overview>";
					break;
				case Constants.PL_DEVDEP :
					item = "PL>Dev.&Dep.>";
					break;
				case Constants.PL_LIFECYCLE :
					item = "PL>Life Cycle>";
					break;
				case Constants.PL_ORGANIZATION :
					item = "PL>Organization>";
					break;
				case Constants.PL_TRAINING :
					item = "PL>Training>";
					break;
				case Constants.PL_FINANCE :
					item = "PL>Finance>";
					break;

			}
			item += name;
			item = ConvertString.trunc(item,50);
			switch (ActionType) {
				case Constants.ACTION_ADD :
					changes = "Added new";
					break;
				case Constants.ACTION_DELETE :
					changes = "Deleted";
					break;
				case Constants.ACTION_UPDATE :

					changes = "Previous value \"" + oldVl + "\" changed to \"" + newVl + "\"";
					if (changes.length() > 200) {
						oldVl = ConvertString.trunc(oldVl,75);
						newVl = ConvertString.trunc(newVl,75);
						changes = "Previous value \"" + oldVl + "\" changed to \"" + newVl + "\"";
					}
					break;
			}
			info.projectID = prjID;
			info.item = item;
			info.changes = changes;
			info.reason = reason;
			info.version = version;
			info.note = note;
			Project.addPLChange(info);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all trainings for one project.
	 */
	public static final void doLoadTrainingList(
		final HttpServletRequest request,
		final HttpServletResponse response) {
			final HttpSession session = request.getSession();
			session.setAttribute("caller", String.valueOf(Constants.PROJECT_PLAN_CALLER));
			ProjectObj.doGetTrainingList(request, response);
			Fms1Servlet.callPage("training.jsp",request,response);
	}

	public static final void doUploadSched(	HttpServletRequest request,HttpServletResponse response) {
		UploadInfo upInfo=Http.doUpload(request,(int)Math.pow(2,20)*4);//4MB is the max file size
		HttpSession session = request.getSession();
		long prjID = Long.parseLong((String) session.getAttribute("projectID"));
		Project.saveScheduleFile(upInfo,prjID);
		request.setAttribute("upload",upInfo.getErrorMessage());
		ProjectPlanCaller.doLoadPLLifecycle(request, response);
	}

	public static final void doGetSchedFile(HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		long prjID = Long.parseLong((String) session.getAttribute("projectID"));
		UploadInfo upInfo=Project.getScheduleFile(prjID);
		Http.httpSendFile(response,upInfo.file);
	}	
}