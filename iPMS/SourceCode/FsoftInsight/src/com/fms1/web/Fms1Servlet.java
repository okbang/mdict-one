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
 
 package com.fms1.web;

import javax.servlet.http.*;
import com.fms1.common.*;
import com.fms1.common.group.*;
import com.fms1.tools.*;
import com.fms1.infoclass.*;

/**
 * Centralises all web requests before passing them to caller objects
 * Access rights and logging are called from here
 */
public class Fms1Servlet extends HttpServlet implements Constants {
	public void init() {}
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		processRequestType(request, response);
	}
	public void destroy() {
		super.destroy();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		processRequestType(request, response);
	}
	public void processRequestType(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String reqTypeStr = request.getParameter("reqType");
			final HttpSession session = request.getSession();
			
			Logger.logRequest(request);
			if (reqTypeStr != null) {
				final int requestType = CommonTools.parseInt(reqTypeStr);
				String logged = (String) session.getAttribute("logged");
				if ((requestType != LOGIN)
					//&& (requestType != GET_RSA)
					&& (requestType != XML_PROJECT_GENERATION)
					&& ((logged == null) || (logged.equals("no")))) { //user has logged out
					callPage("login.jsp?error=Please re-login",request,response);
					System.err.println("Bad session");
					System.err.println(requestType);
					return;
				}
				int i=0;
				String s = (String) session.getAttribute("caller");
				if (s != null)
					i = Integer.parseInt(s);
				final String strworkUnitID = (String) session.getAttribute("projectID");
				long workUnitID = 0;
				if (strworkUnitID != null)
					workUnitID = Long.parseLong(strworkUnitID);
				if (Security.check(requestType,request,response))
				switch (requestType) {
					case LOGIN :
						session.setMaxInactiveInterval(10000);
						UserProfileCaller.doLoginProcess(request, response);
						break;
					case LOGOUT :
						//because we are not sure that all variables are unbound as they should be
						session.setAttribute("logged", "no");
						session.invalidate();
						callPage("login.jsp",request, response);
						break;		
					case SEARCH_CUSTOMER :
						RequirementCaller.doSearchCustomer(request, response);
						break;			
					case WEEKLY_REPORT_VIEW_INIT :
						ReportCaller.weeklyReportCallerInit(request, response);
						break;
					case DELIVERY_LIST_SEARCH:
						OrganizationCaller.getListDelivery(request, response);
						break;
					case WEEKLY_REPORT_VIEW :
						ReportCaller.weeklyReportCaller(request, response);
						break;
					case UPDATE_WR_COMMENTS :
						ReportCaller.doUpdateWRComments(request, response);
						break;
					case DEFECT_VIEW :
						DefectCaller.defectViewCaller(request, response);
						break;
					case UPDATE_PRODUCT_DEFECT_TRACKING:
						DefectCaller.updateProductDefectTracking(request, response);
						break;	
					case PLAN_DRE_BY_QC_STAGE :
						DefectCaller.getPlanDreByQcStage(request, response, "");
						break;
					case PLAN_DRE_BY_QC_STAGE_SAVE :
						DefectCaller.updatePlanDreByQcStage(request, response);
						break;
					case PLAN_DRE_PLAN_DEFECT :
						DefectCaller.getPlanDrePlanDefect(request, response, "");
						break;
					case PLAN_DRE_REPLAN_SAVE :
						DefectCaller.updateDreReplan(request, response);
						break;
					case PLAN_DRE_DEFECT_RATE :
						DefectCaller.getPlanDreDefectRate(request, response, "");
						break;
					case PLAN_DRE_DEFECT_RATE_SAVE :
						DefectCaller.updateDreDefectRate(request, response);
						break;
					case PLAN_DRE_LEAKAGE :
						DefectCaller.getPlanDreLeakage(request, response, "");
						break;
					case REQUIREMENT_GRAPH :
						RequirementCaller.requirementGraphCaller(request, response);
						break;
					case REQUIREMENT_LIST_INIT :
						RequirementCaller.requirementListInitCaller(request, response);
						break;
					case REQUIREMENT_LIST :
						RequirementCaller.requirementListCaller(request, response);
						break;
					case REQUIREMENT_ADDNEW_PREP :
						RequirementCaller.requirementAddnewPrepCaller(request, response);
						break;
					case REQUIREMENT_ADDNEW :
						RequirementCaller.requirementAddnewCaller(request, response);
						break;
					case REQUIREMENT_IMPORT :
						RequirementCaller.doImportRequirement(request, response);
						break;
					case LOAD_CUSTOMER_PAGE :
						RequirementCaller.doLoadCustomerPage(request, response);
						break;
					case CUSTOMER_ADD_NEW :
						RequirementCaller.addNewCustomer(request, response);
						break;
					case CUSTOMER_UPDATE :
						RequirementCaller.updateCustomer(request, response);
						break;
					case DO_CUSTOMER_UPDATE :
						RequirementCaller.doUpdateCustomer(request, response);
						break;
					case CUSTOMER_IMPORT :
						RequirementCaller.doImportCustomer(request, response);
						break;
					case REQUIREMENT_UPDATE_PREP :
						RequirementCaller.requirementUpdatePrepCaller(request, response);
						break;
					case REQUIREMENT_UPDATE :
						RequirementCaller.requirementUpdateCaller(request, response);
						break;
					case REQUIREMENT_DELETE :
						RequirementCaller.requirementDeleteCaller(request, response);
						break;
					case REQUIREMENT_BATCH_EDIT :
						RequirementCaller.requirementBatchUpdatePrepCaller(request, response);
						break;
					case REQUIREMENT_BATCH_UPDATE :
						RequirementCaller.requirementBatchUpdate(request, response);
						break;
					case REQUIREMENT_DETAIL:
						RequirementCaller.requirementDetail(request, response);
						break;
					case REQUIREMENT_STATUS :
						RequirementCaller.requirementStatus(request, response);
						break;
					case REQUIREMENT_UPDATE_STATUS :						
						RequirementCaller.requirementUpdateStatus(request, response);
						break;
					case REQUIREMENT_GROUP_BY :
						RequirementCaller.requirementByGroup(request, response);
						break;
					case REQUIREMENT_PLAN:
						RequirementCaller.getRequirementPlan(request, response);
						break;
					case REQUIREMENT_REPLAN_RCR_BATCH:
						RequirementCaller.getBatchRePlan(request, response);
						break;
					case REQUIREMENT_PLAN_RCR_BATCH:
						RequirementCaller.getBatchPlan(request, response);
						break;
					case REQUIREMENT_PLAN_RCR_BATCH_SAVE:
						RequirementCaller.updateBatchPlan(request, response);
						break;
					case REQUIREMENT_UPDATE_PROCESS:
						RequirementCaller.updateProcessPlan(request, response);
						break;
					case MODULE_LIST :
						ModuleCaller.moduleListCaller(request, response);
						break;
					case MODULE_DETAIL:
						 ModuleCaller.moduleDetailCaller(request, response);
						 break;
					case MODULE_ADDNEW_PREP :
						ModuleCaller.moduleAddnewPrepCaller(request, response);
						break;
					case MODULE_ADDNEW :
						ModuleCaller.moduleAddnewCaller(request, response);
						break;
					case MODULE_IMPORT :
						ModuleCaller.moduleImport(request, response);
						break;
					case MODULE_UPDATE_PREP :
						ModuleCaller.moduleUpdatePrepCaller(request, response);
						break;
					case MODULE_UPDATE :
						ModuleCaller.moduleUpdateCaller(request, response);
						break;
					case MODULE_DELETE :
						ModuleCaller.moduleDeleteCaller(request, response);
						break;
					case BATCH_MODULE_PREPARE_ADD :
						ModuleCaller.doPrepareBatchModuleAdd(request, response);
						break;
					case BATCH_MODULE_PREPARE_UPDATE :
						ModuleCaller.doPrepareBatchModuleUpdate(request, response);
						break;
					case BATCH_MODULE_ADD :
						ModuleCaller.doBatchModuleAdd(request, response);
						break;
					case BATCH_MODULE_UPDATE :
						ModuleCaller.doBatchModuleUpdate(request, response);
						break;
					case BATCH_MODULE_DELETE :
						ModuleCaller.doBatchModuleDelete(request, response);
						break;
						
					case PRODUCT_LOC_LIST :
						ModuleCaller.productLocListingCaller(request, response);
						break;
					case PRODUCT_LOC_DETAIL :
						ModuleCaller.productLocDetailCaller(request, response);
						break;
					case PRODUCT_LOC_UPDATE :
						ModuleCaller.productLocUpdateCaller(request, response);
						break;
					case PRODUCT_LOC_UPDATE_SAVE :
						ModuleCaller.productLocSaveUpdateCaller(request, response);
						break;
					case PRODUCT_LOC_ADD :
						ModuleCaller.productLocAddCaller(request, response);
						break;
					case PRODUCT_LOC_ADD_SAVE :
						ModuleCaller.productLocSaveNewCaller(request, response);
						break;
					case COMPL_RATE_UPDATE :
						ParamCaller.complRateUpdateCaller(request, response);
						break;
					case DISTR_RATE_INIT :
						ParamCaller.distributionRateInitCaller(request, response);
						break;
					case CONVERSION_LIST_INIT :
						ParamCaller.conversionListInitCaller(request, response);
						break;
					case CONVERSION_LIST :
						ParamCaller.conversionListCaller(request, response);
						break;
					case CONVERSION_UPDATE :
						ParamCaller.conversionUpdateCaller(request, response, this);
						break;
					case CONVERSION_ADDNEW :
						ParamCaller.conversionAddnewCaller(request, response, this);
						break;
					case CONVERSION_DELETE :
						ParamCaller.conversionDeleteCaller(request, response, this);
						break;
					case CONVERSION_LIST_NAME :
						ParamCaller.conversionListByNameCaller(request, response);
						break;
					case METHOD_LIST :
						ParamCaller.methodListCaller(request, response);
						break;
					case METHOD_ADDNEW :
						ParamCaller.methodAddnewCaller(request, response);
						break;
					case METHOD_UPDATE :
						ParamCaller.methodUpdateCaller(request, response);
						break;
					case METHOD_REMOVE :
						ParamCaller.methodDeleteCaller(request, response);
						break;
					case CONTRACT_TYPE :
						ParamCaller.contractTypeCaller(request, response);
						break;
					case CONTRACT_TYPE_ADD :
						ParamCaller.contractTypeAddnewCaller(request, response);
						break;
					case CONTRACT_TYPE_UPDATE :
						ParamCaller.contractTypeUpdateCaller(request, response);
						break;
					case CONTRACT_TYPE_REMOVE :
						ParamCaller.contractTypeDeleteCaller(request, response);
						break;
					case BUSINESS_DOMAIN :
						ParamCaller.bizDomainCaller(request, response);
						break;
					case BIZDOMAIN_UPDATE :
						ParamCaller.bizDomainUpdateCaller(request, response);
						break;
					case BIZDOMAIN_REMOVE :
						ParamCaller.bizDomainDeleteCaller(request, response);
						break;
					case BIZDOMAIN_ADD :
						ParamCaller.bizDomainAddnewCaller(request, response);
						break;
					case APP_TYPE :
						ParamCaller.appTypeCaller(request, response);
						break;
					case APPTYPE_UPDATE :
						ParamCaller.appTypeUpdateCaller(request, response);
						break;
					case APPTYPE_REMOVE :
						ParamCaller.appTypeDeleteCaller(request, response);
						break;
					case APPTYPE_ADD :
						ParamCaller.appTypeAddnewCaller(request, response);
						break;
					case PROCESS_TAILORING:
						ParamCaller.proTailoringCaller(request, response);
						break;
					case PROCESS_TAILORING_SEARCH:
						ParamCaller.proTailoringSearchCaller(request, response);
						 break;
					case TAILORING_REF:
						ParamCaller.tailRefCaller(request, response);
						 break;
					case PROCESS_TAILORING_ADD:
						ParamCaller.proTailoringAddnewCaller(request, response);
						break;
					case PROCESS_TAILORING_UPDATE:
						ParamCaller.proTailoringUpdateCaller(request, response);
						break;
					case PROCESS_TAILORING_REMOVE:
						ParamCaller.proTailoringDeleteCaller(request, response);
						break;	
					case RISK_LIST_INIT :
						RiskCaller.riskListInitCaller(request, response);
						break;
					case RISK_LIST_OTHER :
						RiskCaller.listOtherRiskCaller(request, response);
						break;
					case RISK_LIST_OTHER_CALL_BACK :
						RiskCaller.listOtherRiskCallBack(request, response);
						break;
					case RISK_NEXT :
						RiskCaller.riskNextCaller(request, response);
						break;
					case RISK_PREV :
						RiskCaller.riskPrevCaller(request, response);
						break;
					case RISK_ADDNEW_PREP :
						RiskCaller.riskAddnewPrepCaller(request, response);
						break;
					case RISK_MIGRATE :
						RiskCaller.riskMigrate(request, response);
						break;
					case RISK_ADDNEW :
						RiskCaller.riskAddnewCaller(request, response);
						break;
					case RISK_ADD :
						RiskCaller.riskAddCaller(request, response);
						break;
					case RISK_ADD_PREP :
						RiskCaller.riskAddPrepCaller(request, response);
						break;
					case RISK_UPDATE_PREP :
						RiskCaller.riskUpdatePrepCaller(request, response);
						break;
					case RISK_MENUORG :
						RiskCaller.riskUpdatePrepCaller1(request, response);
						break;
					case RISK_DATABASE_UPDATE_PREP :
						RiskCaller.riskDatabaseUpdatePrepCaller(request, response);
						break;
					case RISK_DATABASE_REMOVE :
						RiskCaller.riskDatabaseRemoveCaller(request, response);
						break;					
					case RISK_DATABASE_UPDATE :
						RiskCaller.riskDatabaseUpdateCaller(request, response);
						break;
					case RISK_DATABASE_ADD_PREPARE :
						RiskCaller.riskDatabasePrepAddCaller(request, response);
						break;
					case RISK_DATABASE_ADD :
						RiskCaller.riskDatabaseAddCaller(request, response);
						break;
					case RISK_UPDATE :
						RiskCaller.riskUpdateCaller(request, response);
						break;
					case RISK_SOURCE_UPDATE_PREP :
						RiskCaller.riskSourcePrepUpdateCaller(request, response);
						break;
					case RISK_SOURCE_UPDATE :
						RiskCaller.riskSourceUpdateCaller(request, response);
						break;
					case RISK_DELETE :
						RiskCaller.riskDeleteCaller(request, response);
						break;
					case RISK_MITIGATION_DELETE :
						RiskCaller.riskMitigationDeleteCaller(request, response);
						break;
					case RISK_CONTINGENCY_DELETE :
						RiskCaller.riskContingencyDeleteCaller(request, response);
						break;
					case RISK_IMPORT:
						RiskCaller.doImportRisk(request, response);
						break;
					case RISK_BASELINE:
						ProcessAssetsCaller.doBaselineRisks(request, response);
						break;
					case RISK_COMMON:
						RiskCaller.doLoadCommonRisks(request, response);
						break;
					case RISK_IDENTIFY:
						RiskCaller.riskIdentifyCaller(request, response);
						break;
					case ISSUE :
					//ITS :	Security.getLink(GET_ITSPROJECT, request, response);
						IssueCaller.issueListCaller(request, response);
						break;
					case ISSUE_ADDNEW :
						IssueCaller.issueAddnewCaller(request, response);
						break;
					case ISSUE_ADDPREP :
						IssueCaller.issueAddPrepCaller(request, response);
						break;
					case ISSUE_UPDATE :
						IssueCaller.issueUpdateCaller(request, response);
						break;
					case ISSUE_DELETE :
						IssueCaller.issueDeleteCaller(request, response);
						break;
					case HOME_SQA:
						SupportGroupCaller.SQAhomeCaller(request, response);
						break;
					case HOME_PQA:
						SupportGroupCaller.PQAhomeCaller(request, response);
						break;
					case WORKUNIT_HOME :
						WorkUnitCaller.workUnitHomeCaller(request, response);
						break;
					case WORKUNIT_HOME2 :
						String strWUID = request.getParameter("workUnitID");
						workUnitID = Long.parseLong(strWUID);
						WorkUnitInfo wuInfo = WorkUnit.getWorkUnitInfo(workUnitID);
						WorkUnit.workUnitHome(request, response, wuInfo);
						break;
					case HEADER_GROUP:
						WorkUnitCaller.doGetGroupHome(request, response);
						break;
					case HEADER_PRJ :
						WorkUnitCaller.doGetProjectHome(request, response);
						break;
					/*HuyNH2 add some line code for project archive*/
					case PROJECT_ARCHIVE_LIST:
						WorkUnitCaller.doGetProjectArchiveHome(request,response);
						break;
					case PROJECT_ARCHIVE_RESTORE:
						WorkUnitCaller.doRestore(request,response);
						break;
					case PROJECT_ARCHIVE_ARCHIVE:
						WorkUnitCaller.doArchive(request,response);
						break;
					case PROJECT_ARCHIVE_HISTORY:
						WorkUnitCaller.doGetProjectArchiveHistoryHome(request,response);
						break;    
					case PROJECT_ARCHIVE_HISTORY_DETAIL:    
						WorkUnitCaller.doGetProjectArchiveHistoryDetailHome(request,response);
						break;
					/*End add code*/    
					case HEADER_ADM :
						request.setAttribute("workUnitID", "-1");
						WorkUnitCaller.workUnitHomeCaller(request, response);
						break;                                                                     
						//ROLES
					case GET_RIGHT_GROUP_LIST :
						RightGroupCaller.doGetRightGroupList(request, response);
						break;
					case GET_RIGHT_GROUP :
						RightGroupCaller.doGetRightForPage(request, response);
						break;
					case GET_PAGE_LIST :
						RightGroupCaller.doPrepAdd(request, response);
						break;
					case ADDNEW_RIGHT_GROUP :
						RightGroupCaller.doAddnewRightGroup(request, response);
						break;
					case UPDATE_RIGHT_GROUP :
						RightGroupCaller.doUpdateRightGroup(request, response);
						break;
					case DELETE_RIGHT_GROUP :
						RightGroupCaller.doDeleteRightGroup(request, response);
						break;
						//WORKUNITS	
					case GET_WORK_UNIT_LIST :
						WorkUnitCaller.doGetWorkUnitListAction(request, response);
						break;
					case GET_WORK_UNIT :
						WorkUnitCaller.doGetWorkUnitAction(request, response);
						break;
					case DELETE_WORK_UNIT_ORGANIZATION :
						WorkUnitCaller.doDeleteWorkUnitOrgAction(request, response);					
						break;
					case DELETE_WORK_UNIT_GROUP :
						WorkUnitCaller.doDeleteWorkUnitGroupAction(request, response);
						break;
					case DELETE_WORK_UNIT_PROJECT :
						WorkUnitCaller.doDeleteWorkUnitProjectAction(request, response);											
						break;
					case ADDNEW_WORK_UNIT_ORGANIZATION :
						WorkUnitCaller.doAddnewWorkUnitOrgAction(request, response);
						break;
					case ADDNEW_WORK_UNIT_GROUP :
						WorkUnitCaller.doAddnewWorkUnitGroupAction(request, response);
						break;
					case ADDNEW_WORK_UNIT_PROJECT :
						WorkUnitCaller.doAddnewWorkUnitProjectAction(request, response);						
						break;
					case UPDATE_WORK_UNIT_GROUP :
						WorkUnitCaller.doUpdateWorkUnitGroupAction(request, response);					
						break;
					case UPDATE_WORK_UNIT_ORGANIZATION :
						WorkUnitCaller.doUpdateWorkUnitOrgAction(request, response);
						break;						
						//PRACTICES
					case GET_PRACTICE_LIST :
						PracticeCaller.doGetPracticeList(request, response);
						request.getSession().setAttribute("pracCurPage", "0");
						break;
					case ADDNEW_PRACTICE :
						PracticeCaller.doAddPractice(request, response);
						break;
					case UPDATE_PRACTICE :
						PracticeCaller.doUppdatePractice(request, response);
						break;
					case DELETE_PRACTICE :
						PracticeCaller.doDeletePractice(request, response);
						break;
					case GET_PRACTICE :
						PracticeCaller.doGetPractice(request, response);
						break;
						//Training-Project plan
					case GET_TRAINING_LIST :
						ProjectPlanCaller.doLoadTrainingList(request, response);
						request.getSession().setAttribute("trainCurPage", "0");
						break;
					case ADDNEW_TRAINING :
						ProjectObj.doAddTraining(request, response);
						if (i == TRAINING_CALLER) {
							ProjectPlanCaller.doLoadTrainingList(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doGetScheTrainingPlan(request, response);
						}
						break;
					case UPDATE_TRAINING :
						ProjectObj.doUppdateTraining(request, response);
						if (i == TRAINING_CALLER) {
							ProjectPlanCaller.doLoadTrainingList(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doGetScheTrainingPlan(request, response);
						}
						break;
					case DELETE_TRAINING :
						ProjectObj.doDeleteTraining(request, response);
						if (i == TRAINING_CALLER) {
							ProjectPlanCaller.doLoadTrainingList(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doGetScheTrainingPlan(request, response);
						}
						break;
						//Tool and Infrastructure				
					case GET_TOOL_LIST :
						ToolCaller.doGetToolList(request, response);						
						break;
					case PREPARE_ADD_TOOL :
					ToolCaller.doPrepareAddTool(request, response);
						break;
					case GET_TOOL :
						ToolCaller.doGetTool(request, response);
						break;
					case ADDNEW_TOOL :
						ToolCaller.doAddTool(request, response);
						break;
					case UPDATE_TOOL :
						ToolCaller.doUppdateTool(request, response);
						break;
					case DELETE_TOOL :
						ToolCaller.doDeleteTool(request, response);
						break;
						//Financial_Plan									
					case GET_FINAN_LIST :
						FinanCaller.doGetFinanList(request, response);
						break;
					case GET_FINAN :
						FinanCaller.doGetFinan(request, response);
						break;
					case ADDNEW_FINAN :
						if (i == FINAN_CALLER) {
							FinanCaller.doAddFinan(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doAddFinan(request, response);
						}
						break;
					case UPDATE_FINAN :
						if (i == FINAN_CALLER) {
							FinanCaller.doUppdateFinan(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doUppdateFinan(request, response);
						}
						break;
					case DELETE_FINAN :
						if (i == FINAN_CALLER) {
							FinanCaller.doDeleteFinan(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doDeleteFinan(request, response);
						}
						break;
						//Cost
					case GET_COST :
						FinanCaller.doGetCost(request, response);
						break;
					case ADDNEW_COST :
						FinanCaller.doAddCost(request, response);
						break;
					case UPDATE_COST :
						FinanCaller.doUppdateCost(request, response);
						break;
					case DELETE_COST :
						FinanCaller.doDeleteCost(request, response);
						break;
						//Quality objective
					case GET_QUALITY_OBJECTIVE_LIST :
						QualityObjectiveCaller.doGetQualityObjectiveList(request, response);
						break;
						
					// STRATEGY FOR MEETING QUALITY OBJECTIVES START			
					case PL_STRATEGY_FOR_MEETING_PREPARE_ADD :
						QualityObjectiveCaller.doPrepareAddStratForMeeting(request, response);
						break;
					case PL_STRATEGY_FOR_MEETING_PREPARE_UPDATE :
						QualityObjectiveCaller.doPrepareUpdateStratForMeeting(request, response);
						break;
					case PL_STRATEGY_FOR_MEETING_ADD :
						QualityObjectiveCaller.doAddStratForMeeting(request, response);
						break;
					case PL_STRATEGY_FOR_MEETING_UPDATE :
						QualityObjectiveCaller.doUpdateStratForMeeting(request, response);
						break;
					case PL_STRATEGY_FOR_MEETING_DELETE :
						QualityObjectiveCaller.doDeleteStratForMeeting(request, response);
						break;
					// STRATEGY FOR MEETING QUALITY OBJECTIVES END
					
					// REVIEW STRATEGY START
					case PL_REVIEW_STRATEGY_PREPARE_ADD :						
						QualityObjectiveCaller.doPrepareAddReviewStrat(request, response);
						break;
					case PL_REVIEW_STRATEGY_PREPARE_UPDATE :
						QualityObjectiveCaller.doPrepareUpdateReviewStrat(request, response);
						break;
					case PL_REVIEW_STRATEGY_ADD :
						QualityObjectiveCaller.doAddReviewStrat(request, response);
						break;
					case PL_REVIEW_STRATEGY_UPDATE :
						QualityObjectiveCaller.doUpdateReviewStrat(request, response);
						break;
					case PL_REVIEW_STRATEGY_DELETE :
						QualityObjectiveCaller.doDeleteReviewStrat(request, response);
						break;
					// REVIEW STRATEGY END
					
					// TEST STRATEGY START					
					case PL_TEST_STRATEGY_PREPARE_ADD :						
						QualityObjectiveCaller.doPrepareAddTestStrat(request, response);
						break;
					case PL_TEST_STRATEGY_PREPARE_UPDATE :
						QualityObjectiveCaller.doPrepareUpdateTestStrat(request, response);
						break;
					case PL_TEST_STRATEGY_ADD :						
						QualityObjectiveCaller.doAddTestStrat(request, response);
						break;
					case PL_TEST_STRATEGY_UPDATE :
						QualityObjectiveCaller.doUpdateTestStrat(request, response);
						break;
					case PL_TEST_STRATEGY_DELETE :
						QualityObjectiveCaller.doDeleteTestStrat(request, response);
						break;
					// TEST STRATEGY END
					
					case PL_ADD_PRODUCT_FROM_QUALITY : 
						QualityObjectiveCaller.doAddProductsPrepare(request, response);
						break;
					case UPDATE_REVIEW_TEST :
						if (i == QUALITY_OBJECTIVE_CALLER) {
							QualityObjectiveCaller.doUpdateModule(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doUpdateModule(request, response);
						}
						break;
					case UPDATE_QLT_OBJECTIVE :
						QualityObjectiveCaller.doUpdateObjective(request, response);
						break;
					case ADDNEW_OTHER_ACTIVITY :
						if (i == QUALITY_OBJECTIVE_CALLER) {
							QualityObjectiveCaller.doAddOtherAct(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doAddOtherAct(request, response);
						}
						break;
					case UPDATE_OTHER_ACTIVITY :
						if (i == QUALITY_OBJECTIVE_CALLER) {
							QualityObjectiveCaller.doUpdateOtherAct(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doUpdateOtherAct(request, response);
						}
						break;
					case DELETE_OTHER_ACTIVITY :
						if (i == QUALITY_OBJECTIVE_CALLER) {
							QualityObjectiveCaller.doDeleteOtherAct(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doDeleteOtherAct(request, response);
						}
						break;
						// Schedule
					case SCHE_MAJOR_INFOMATION_GET_LIST :
						ScheduleCaller.doGetScheInformation(request, response);
						break;
					case SCHE_SUBCONTRACT_GET_LIST :
						ScheduleCaller.doGetScheSubcontract(request, response);
						break;
					case SCHE_STAGE_GET_LIST :
						ScheduleCaller.doGetScheStage(request, response);
						break;
					case SCHE_REVIEW_TEST_GET_LIST :
						ScheduleCaller.doGetScheReviewAndTest(request, response);
						break;
					case SCHE_REVIEW_TEST_UPDATE :
						//after module update
						ScheduleCaller.doGetRevTestUpdate(request, response, ModuleCaller.moduleUpdate(request));
						break;
					case SCHE_REVIEW_TEST_UPDATE2 :
						//after module add new
						ScheduleCaller.doGetRevTestUpdate(request, response, ModuleCaller.moduleAddnew(request));
						break;
					
					case SCHE_REVIEW_TEST_UPDATE3 :
						//after module update						
						ScheduleCaller.doGetRevTestUpdate2(request, response, ModuleCaller.moduleBatchUpdate(request,null));
						break;
					case SCHE_REVIEW_TEST_UPDATE4 :
						//after module add new
						ScheduleCaller.doGetRevTestUpdate2(request, response, ModuleCaller.moduleBatchAddnew(request,null));
						break;
						
					case SCHE_REVIEW_TEST_VIEW :
						//after module add new
						ScheduleCaller.doGetRevTestView(request, response);
						break;
					case SCHE_OTHER_QUALITY_GET_LIST :
						ScheduleCaller.doGetScheOtherQuality(request, response);
						break;
					case SCHE_TRAINING_PLAN_GET_LIST :
						ScheduleCaller.doGetScheTrainingPlan(request, response);
						break;
					case SCHE_CRITICAL_DEPENDENCIES_GET_LIST :
						ScheduleCaller.doGetScheCriticalDependencies(request, response);
						break;
					case SCHE_FINANCIAL_PLAN_GET_LIST :
						ScheduleCaller.doGetScheFinancialPlan(request, response);
						break;
					case SCHE_SIZE_INPUT:
						ScheduleCaller.doGetScheSizeInput(request, response);
						break;
					case UPDATE_SCHEDULE_HEADER :
						ScheduleCaller.doUpdateScheduleHeader(request, response);
						break;
					case GET_SUBCONTRACT_LIST :
						ScheduleCaller.doAddOtherAct(request, response);
						break;
					case ADDNEW_SUBCONTRACT :
						ScheduleCaller.doAddSubcontract(request, response);
						break;
					case UPDATE_SUBCONTRACT :
						ScheduleCaller.doUpdateSubcontract(request, response);
						break;
					case DELETE_SUBCONTRACT :
						ScheduleCaller.doDeleteSubcontract(request, response);
						break;
					case ADDNEW_STAGE :
						ScheduleCaller.doAddStage(request, response);
						break;
					case UPDATE_STAGE :
						ScheduleCaller.doUpdateStage(request, response);
						break;
					case DELETE_STAGE :
						ScheduleCaller.doDeleteStage(request, response);
						break;
					case ADDNEW_OTHER_ACTIVITY_SCH :
						ScheduleCaller.doAddOtherAct(request, response);
						break;
					case UPDATE_OTHER_ACTIVITY_SCH :
						ScheduleCaller.doUpdateOtherAct(request, response);
						break;
					case DELETE_OTHER_ACTIVITY_SCH :
						ScheduleCaller.doDeleteOtherAct(request, response);
						break;
						//Effort
					case UPDATE_REVIEW_EFFORT :
						EffortCaller.doUpdateReviewEffort(request, response);
						break;
					case UPDATE_QLT_ACTIVITY_EFFORT :
						EffortCaller.doUpdateQltActivityEffort(request, response);
						break;
					case UPDATE_WEEKLY_EFFORT :
						EffortCaller.doUpdateWeeklyEffort(request, response);
						break;
					case EFF_INFORMATION_GET_LIST :
						EffortCaller.doGetEffInformation(request, response);
						break;
					case EFF_STAGE_GET_LIST :
						EffortCaller.doGetEffStageProcess(request, response);
						break;
					case EFF_REVIEW_GET_LIST :
						EffortCaller.doGetEffReview(request, response);
						break;
					case EFF_QUALITY_ACTIVITY_GET_LIST :
						EffortCaller.doGetEffQualityActivity(request, response);
						break;
					case EFF_WEEKLY_GET_LIST :
						EffortCaller.doGetEffWeekly(request, response);
						break;
					case EFF_GET_BATCH_PLAN :
						EffortCaller.getBatchPlan(request, response);
						break;
					case EFF_UPDATE_BATCH_PLAN :
						EffortCaller.updateBatchPlan(request, response);
						break;
					case BATCH_UPDATE_PROCESS_EFFORT :
						  EffortCaller.doBatchUppdateProcessEffort(request, response);
						  break;
					case UPDATE_STAGE_EFFORT :
						  EffortCaller.doUppdateStageEffort(request, response);
						  break;
					case GET_POST_MORTEM :
						ReportCaller.doGetPostMortemReport(request, response);
						break;
					case UPDATE_PM_HEADER :
						ReportCaller.doUpdatePmHeader(request, response);
						break;
					case UPDATE_PM_CAUSE :
						ReportCaller.doUpdatePmCause(request, response);
						break;
					case EXPORT_PM_CAUSE :
						ReportCaller.doExportCausal(request, response);
						break;
						//Further work				
					case ADDNEW_FURTHER_WORK :
						ReportCaller.doAddFurtherWork(request, response);
						break;
					case UPDATE_FURTHER_WORK :
						ReportCaller.doUpdateFurtherWork(request, response);
						break;
					case DELETE_FURTHER_WORK :
						ReportCaller.doDeleteFurtherWork(request, response);
						break;
					case APPLY_PPM_FEATURE:
						AssetsCaller.doApplyPPM(request, response);																						
					case TAILORING_GET_LIST :
						AssetsCaller.doLoadTailoringPage(request, response);
						break;
					case TAILORING_ADD :
						AssetsCaller.doAddTailoring(request, response);
						break;
					case TAILORING_UPDATE :
						AssetsCaller.doUpdateTailoring(request, response);
						break;
					case DEVIATION_ADD :
						AssetsCaller.doAddDeviation(request, response);
						break;
						
					case TAILORING_ADD_PREPARE :
						AssetsCaller.doPrepareTailoringAdd(request, response);
						break;
					case DEVIATION_ADD_PREPARE :
						AssetsCaller.doPrepareDeviationAdd(request, response);
						break;
					case TAILORING_UPDATE_PRE :
						AssetsCaller.doPrepareTailoringUpdate(request, response);
						break;
					case TAILORING_DELETE :
						AssetsCaller.doDeleteTailoring(request, response);
						break;
//					case TAILORING_MNG :
//						AssetsCaller.doPrepareTailoringUpdate(request, response);
//						break;
					case DEVIATION_UPDATE_PRE :
						AssetsCaller.doPrepareDeviationUpdate(request, response);
						break;
					case DEVIATION_UPDATE :
						AssetsCaller.doUpdateDeviation(request, response);
						break;
					case DEVIATION_DELETE :
						AssetsCaller.doDeleteDeviation(request, response);
						break;
					case TAILORING_NEXT :
						AssetsCaller.doNextTailoring(request, response);
						break;
					case TAILORING_PREV :
						AssetsCaller.doPrevTailoring(request, response);
						break;
					case WO_DELIVERABLE_GET_LIST :
						WorkOrderCaller.doLoadDeliverableList(request, response);
						break;
					case WO_DELIVERABLE_IMPORT :
						WorkOrderCaller.doImportDeliverable(request, response);
						break;
						
					case WO_DELIVERABLE_BATCH_PRE_ADD :
						WorkOrderCaller.doPrepareDeliverableBatchAdd(request, response);						
						break;
					case WO_DELIVERABLE_BATCH_PRE_UPDATE :
						WorkOrderCaller.doPrepareDeliverableBatchUpdate(request, response);
						break;
					case WO_DELIVERABLE_BATCH_ADD :
						WorkOrderCaller.doBatchUpdateDeliverable(request, response);
						break;
					case WO_DELIVERABLE_BATCH_UPDATE :
						WorkOrderCaller.doBatchUpdateDeliverable(request, response);
						break;
					case WO_DELIVERABLE_BATCH_DELETE :
						WorkOrderCaller.doBatchDeleteDeliverable(request, response);
						break;
						
					case WO_DELIVERABLE_ADD_PREP :
						WorkOrderCaller.doPrepareDeliverableAdd(request, response);
						break;	
					case WO_DELIVERABLE_ADD :
						WorkOrderCaller.doUpdateDeliverable(request, response);
						break;
					case WO_DELIVERABLE_UPDATE_PREP :
						WorkOrderCaller.doPrepareDeliverableUpdate(request, response);
						break;
					case WO_DELIVERABLE_UPDATE :
						WorkOrderCaller.doUpdateDeliverable(request, response);
						break;
					case WO_DELIVERABLE_DELETE :
						WorkOrderCaller.doDeleteDeliverable(request, response);
						break;
					case WO_STANDARD_METRIC_UPDATE :
						WorkOrderCaller.doUpdateStandardMetricList(request, response);
						break;
					case WO_STANDARD_METRIC :					
						callPage("woStandardMetrics.jsp",request,response);
						break;
					case WO_PERFORMANCE_GET_LIST :
						WorkOrderCaller.doLoadWOPerformanceList(request, response);
						break;
					case WO_PERFORMANCE_UPDATE :
						WorkOrderCaller.doUpdatePerformanceList(request, response);
						break;
					//anhtv08- start	
					case WO_CUS_METRIC_AUTO_UPDATE :
						WorkOrderCaller.doAutoUpdateCustomerMetric(request,response,"");
						break;
					case WO_DEF_METRIC_AUTO_UPDATE :
						WorkOrderCaller.doAutoUpdateDefect(request,response);	
						break;
					// end	
						
					case WO_GENERAL_INFO_GET_LIST :
						WorkOrderCaller.doLoadGeneralInfo(request, response);
						break;
					case WO_GENERAL_INFO_UPDATE :
						WorkOrderCaller.doGeneralInfoUpdate(request, response);
						break;
					case WO_GENERAL_INFO : 
						WorkOrderCaller.doLoadGeneralInfoUpdate(request, response);                       
						break;
					case WO_CLOSE_CANCEL_PROJECT :
						WorkOrderCaller.doLoadGeneralInfoProjectCloseCancel(request, response);
						break;
					case WO_REOPEN_PROJECT :
						WorkOrderCaller.doLoadGeneralInfoProjectReOpen(request, response);
						break;
					case WO_CUS_METRIC_ADD :
						WorkOrderCaller.doAddWOCustomeMetric(request, response);
						break;
					case WO_CUS_METRIC_UPDATE :
						WorkOrderCaller.doUpdateWOCustomeMetric(request, response);
						break;
					case WO_CUS_METRIC_DELETE :
						WorkOrderCaller.doDeleteWOCustomeMetric(request, response);
						break;
					case WO_TEAM_GET_LIST :
						WorkOrderCaller.doLoadWOTeamList(request, response);
						break;
					// landd add sub team start
					case WO_SUB_TEAM_ADD_PREPARE :
						WorkOrderCaller.doPrepareSubTeamAdd(request, response);
						break;
					case WO_SUB_TEAM_UPDATE_PREPARE :
						WorkOrderCaller.doPrepareSubTeamUpdate(request, response);
						break;
					case WO_SUB_TEAM_ADD :
						WorkOrderCaller.doSubTeamAdd(request, response);
						break;
					case WO_SUB_TEAM_UPDATE :
						WorkOrderCaller.doSubTeamUpdate(request, response);
						break;
					case WO_SUB_TEAM_DELETE :
						WorkOrderCaller.doSubTeamDelete(request, response);
						break;
					// landd add sub team end
					case WO_TEAM_ADD_PREPARE :
						WorkOrderCaller.doPrepareTeamAdd(request, response);
						break;						
					case WO_TEAM_ADD :
						WorkOrderCaller.doAddAssignment(request, response);
						break;
					case WO_TEAM_IMPORT :
						WorkOrderCaller.doImportTeam(request, response);
						break;
					case WO_TEAM_UPDATE :
						WorkOrderCaller.doUpdateAssignment(request, response);
						break;
					case WO_TEAM_BATCH_UPDATE_PREPARE :
						WorkOrderCaller.doPrepareBatchUpdateAssignment(request, response);
						break;
					case WO_TEAM_BATCH_UPDATE :
						WorkOrderCaller.doBatchUpdateAssignment(request, response);
						break;
					case WO_TEAM_BATCH_DELETE :
						WorkOrderCaller.doBatchDeleteAssignment(request, response);
						break;
					case WO_TEAM_DELETE :
						WorkOrderCaller.doDeleteAssignment(request, response);
						break;
					case WO_TEAM_MNG :
						WorkOrderCaller.doPrepareTeamUpdate(request, response);
						break;
					case WO_CHANGE_GET_LIST :
						WorkOrderCaller.doLoadWOChangeList(request, response);
						break;
					case WO_CHANGE_ADD_PREPARE :
						WorkOrderCaller.doPrepareChangeAdd(request, response);
						break;
					case WO_CHANGE_ADD :
						WorkOrderCaller.doAddChange(request, response);
						break;
					case WO_CHANGE_UPDATE :
						WorkOrderCaller.doUpdateChange(request, response);
						break;
					case WO_CHANGE_DELETE :
						WorkOrderCaller.doDeleteChange(request, response);
						break;
					case WO_CHANGE_MNG :
						WorkOrderCaller.doPrepareChangeUpdate(request, response);
						break;
					case WO_ACCEPTANCE_GET_LIST :
						WorkOrderCaller.doLoadWOAcceptanceList(request, response);
						break;
					case WO_UPDATE_TEAM_EVALUATION :
						WorkOrderCaller.doUpdateTeamEvaluation(request, response);
						break;
					case WO_TEAM_EVALUATION_DO_UPDATE :
						WorkOrderCaller.TeamEvaluationSave(request, response);
						break;
					case WO_ACCEPTANCE_UPDATE :
						WorkOrderCaller.doUpdateWOAcceptanceList(request, response);
						break;
					case WO_SIG_GET_LIST :
						WorkOrderCaller.doLoadWOSignatureList(request, response);
						break;
					case WO_SIG_APPROVE :
						WorkOrderCaller.doUpdateWOSignature(request, response);
						break;
					case WO_SIG_DEL :
						WorkOrderCaller.doDeleteWOApproval(request, response);
						break;
					case WO_SIG_ADD :
						WorkOrderCaller.doAddWOApproval(request, response);
						break;
					case WO_SIG_RESET :
						WorkOrderCaller.doResetWOApproval(request, response);
						break;
					case WO_CHANGE_SIG_APPROVE :
						WorkOrderCaller.doUpdateChangeSignature(request, response);
						break;
					case WO_CHANGE_SIG_DEL :
						WorkOrderCaller.doDeleteChangeApproval(request, response);
						break;
					case WO_CHANGE_SIG_ADD :
						WorkOrderCaller.doAddChangeApproval(request, response);
						break;
					case WO_CHANGE_SIG_RESET :
						WorkOrderCaller.doResetChangeApproval(request, response);
						break;
					case WO_INTERNAL_SIG_APPROVE :
						WorkOrderCaller.doUpdateInternalSignature(request, response);
						break;
					case WO_INTERNAL_SIG_DEL :
						WorkOrderCaller.doDeleteInternalApproval(request, response);
						break;
					case WO_INTERNAL_SIG_ADD :
						WorkOrderCaller.doAddInternalApproval(request, response);
						break;
					case WO_INTERNAL_SIG_RESET :
						WorkOrderCaller.doResetInternalApproval(request, response);
						break;
					case WO_EXPORT :
						WorkOrderCaller.doExportWO(request, response, true);
						break;
					case WO_AC_EXPORT :
						WorkOrderCaller.doExportWO(request, response, false);
						break;
					case PL_OVERVIEW_GET_PAGE :						
						ProjectPlanCaller.doLoadPLProjectOverview(request, response);
						break;
					case PL_EXPORT :
						ProjectPlanCaller.doExportPP(request, response);
						break;
					case ISSUE_IMPORT :
						IssueCaller.doImportIssue(request, response);
						break;	
					case PL_CONSTRAINT_ADD :
						ProjectPlanCaller.doAddPLConstraint(request, response);
						break;
					case PL_CONSTRAINT_UPDATE :
						ProjectPlanCaller.doUpdatePLConstraint(request, response);
						break;
					case PL_CONSTRAINT_DELETE :
						ProjectPlanCaller.doDeletePLConstraint(request, response);
						break;
					case PL_ASSUMPTION_ADD :
						ProjectPlanCaller.doAddPLAssumption(request, response);
						break;
					case PL_ASSUMPTION_UPDATE :
						ProjectPlanCaller.doUpdatePLAssumption(request, response);
						break;
					case PL_ASSUMPTION_DELETE :
						ProjectPlanCaller.doDeletePLAssumption(request, response);
						break;
					case PL_REFERENCE_ADD_PREPARE :
						ProjectPlanCaller.doPreparePLReferenceAdd(request, response);
						break;
					case PL_REFERENCE_ADD :
						ProjectPlanCaller.doAddPLReference(request, response);
						break;
					case PL_REFERENCE_UPDATE :
						ProjectPlanCaller.doUpdatePLReference(request, response);
						break;
					case PL_REFERENCE_DELETE :
						ProjectPlanCaller.doDeletePLReference(request, response);
						break;
					case PL_REFERENCE_MNG :
						ProjectPlanCaller.doPreparePLReferenceUpdate(request, response);
						break;
					case PL_DELIVERIES_DEPENDENCIES_GET_PAGE :
						ProjectPlanCaller.doLoadPLDevilerable_Dependencies(request, response);
						break;
					case PL_DEPENDENCY_DELETE :
						if (i == PROJECT_PLAN_CALLER) {
							ProjectPlanCaller.doDeletePLDependency(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doDeletePLDependency(request, response);
						}
						break;
					case PL_DEPENDENCY_ADD :
						if (i == PROJECT_PLAN_CALLER) {
							ProjectPlanCaller.doAddPLDependency(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doAddPLDependency(request, response);
						}
						break;
					case PL_DEPENDENCY_UPDATE :
						if (i == PROJECT_PLAN_CALLER) {
							ProjectPlanCaller.doUpdatePLDependency(request, response);
						}
						else if (i == SCHEDULE_CALLER) {
							ScheduleCaller.doUpdatePLDependency(request, response);
						}
						break;
					case PL_DEPENDENCY_VIEW :
						ProjectPlanCaller.doViewPLDependency(request, response);
						break;
					case PL_LIFECYCLE_GET_PAGE :
						session.setAttribute("caller", String.valueOf(Constants.PROJECT_PLAN_CALLER));
						ProjectPlanCaller.doLoadPLLifecycle(request, response);
						break;
					case PL_ITERATION_ADD :
						ProjectPlanCaller.doAddPLIteration(request, response);
						break;
					case PL_ITERATION_ADD_PREPARE :
						ProjectPlanCaller.doPreparePLIterationAdd(request, response);
						break;
					case PL_ITERATION_DELETE :
						ProjectPlanCaller.doDeletePLIteration(request, response);
						break;
					case PL_ITERATION_UPDATE :
						ProjectPlanCaller.doUpdatePLIteration(request, response);
						break;
					case PL_ITERATION_UPDATE_PREPARE :
						ProjectPlanCaller.doPreparePLIterationUpdate(request, response);
						break;
					case PL_STRUCTURE_GET_PAGE :
						ProjectPlanCaller.doLoadPLStructure(request, response);
						break;
					case PL_ORG_STRUCTURE_UPDATE_PREPARE :
						ProjectPlanCaller.doPreparePLOrgStructureUpdate(request, response);
						break;
					case PL_ORG_STRUCTURE_UPDATE :
						ProjectPlanCaller.doUpdatePLOrgStructure(request, response);
						break;
					case PL_INTERFACE_DELETE :
						ProjectPlanCaller.doDeletePLInterface(request, response);
						break;
					case PL_INTERFACE_ADD_PREPARE :
						ProjectPlanCaller.doPreparePLInterfaceAdd(request, response);
						break;
					case PL_INTERFACE_ADD :
						ProjectPlanCaller.doAddPLInterface(request, response);
						break;
					case PL_INTERFACE_UPDATE_PREPARE :
						ProjectPlanCaller.doPreparePLInterfaceUpdate(request, response);
						break;
					case PL_INTERFACE_UPDATE :
						ProjectPlanCaller.doUpdatePLInterface(request, response);
						break;
					case PL_INTERFACE_VIEW :
						ProjectPlanCaller.doViewPLInterface(request, response);
						break;
						
					case PL_INTERFACE_FSOFT_ADD_PREPARE :
						ProjectPlanCaller.doPLFsoftInterfacePrepareAdd(request, response);
						break;
					case PL_INTERFACE_CUSTOMER_ADD_PREPARE :
						ProjectPlanCaller.doPLCustomerInterfacePrepareAdd(request, response);
						break;
					case PL_INTERFACE_OTHER_PROJECT_ADD_PREPARE :
						ProjectPlanCaller.doPLOtherProjectInterfacePrepareAdd(request, response);
						break;
					case PL_INTERFACE_FSOFT_UPDATE_PREPARE :
						ProjectPlanCaller.doPLFsoftInterfacePrepareUpdate(request, response);
						break;
					case PL_INTERFACE_CUSTOMER_UPDATE_PREPARE :
						ProjectPlanCaller.doPLCustomerInterfacePrepareUpdate(request, response);
						break;
					case PL_INTERFACE_OTHER_PROJECT_UPDATE_PREPARE :
						ProjectPlanCaller.doPLOtherProjectInterfacePrepareUpdate(request, response);
						break;
	
					case PL_INTERFACE_FSOFT_ADD :
						ProjectPlanCaller.doPLFsoftInterfaceAdd(request, response);
						break;
					case PL_INTERFACE_CUSTOMER_ADD :
						ProjectPlanCaller.doPLCustomerInterfaceAdd(request, response);
						break;
					case PL_INTERFACE_OTHER_PROJECT_ADD :
						ProjectPlanCaller.doPLOtherProjectInterfaceAdd(request, response);
						break;
					case PL_INTERFACE_FSOFT_UPDATE :
						ProjectPlanCaller.doPLFsoftInterfaceUpdate(request, response);
						break;
					case PL_INTERFACE_CUSTOMER_UPDATE :
						ProjectPlanCaller.doPLCustomerInterfaceUpdate(request, response);
						break;
					case PL_INTERFACE_OTHER_PROJECT_UPDATE :
						ProjectPlanCaller.doPLOtherProjectInterfaceUpdate(request, response);
						break;
					case PL_INTERFACE_FSOFT_DELETE :
						ProjectPlanCaller.doPLFsoftInterfaceDelete(request, response);
						break;
					case PL_INTERFACE_CUSTOMER_DELETE :
						ProjectPlanCaller.doPLCustomerInterfaceDelete(request, response);
						break;
					case PL_INTERFACE_OTHER_PROJECT_DELETE :
						ProjectPlanCaller.doPLOtherProjectInterfaceDelete(request, response);
						break;
					// CHANGE REQUIREMENT MANAGEMENT START
					case PL_REQ_CHANGES_MNG_GET_LIST :
						ProjectPlanCaller.doViewPLReqChanges(request, response);
						break;
					case PL_REQ_CHANGES_MNG_PREPARE_ADD :
						ProjectPlanCaller.doPrepareAddPLReqChanges(request, response);
						break;
						
					case PL_REQ_CHANGES_MNG_PREPARE_UPDATE :
						ProjectPlanCaller.doPrepareUpdatePLReqChanges(request, response);
						break;
					case PL_REQ_CHANGES_MNG_ADD :
						ProjectPlanCaller.doAddPLReqChanges(request, response);
						break;
					case PL_REQ_CHANGES_MNG_UPDATE :
						ProjectPlanCaller.doUpdatePLReqChanges(request, response);
						break;
					case PL_REQ_CHANGES_MNG_DELETE :
						ProjectPlanCaller.doDeletePLReqChanges(request, response);
						break;
					// CHANGE REQUIREMENT MANAGEMENT END
					
					// INTEGRATION STRATEGY START
					case PL_INTEGRATION_STRATEGY_GET_LIST :
						ProjectPlanCaller.doViewPLIntegrationStrat(request, response);
						break;
					case PL_INTEGRATION_STRATEGY_PREPARE_ADD :						
						ProjectPlanCaller.doPrepareAddPLIntegrationStrat(request, response);
						break;
					case PL_INTEGRATION_STRATEGY_PREPARE_UPDATE :
						ProjectPlanCaller.doPrepareUpdatePLIntegrationStrat(request, response);
						break;
					case PL_INTEGRATION_STRATEGY_ADD :
						ProjectPlanCaller.doAddPLIntegrationStrat(request, response);
						break;
					case PL_INTEGRATION_STRATEGY_UPDATE :
						ProjectPlanCaller.doUpdatePLIntegrationStrat(request, response);
						break;
					case PL_INTEGRATION_STRATEGY_DELETE :
						ProjectPlanCaller.doDeletePLIntegrationStrat(request, response);
						break;
					// INTEGRATION STRATEGY END
					
					// MEASUREMENTS PROGRAM START
					case PL_MEASUREMENTS_PROGRAM_PREPARE_ADD :
						ProjectPlanCaller.doPrepareAddPLMeasurementsProg(request, response);
						break;
					case PL_MEASUREMENTS_PROGRAM_PREPARE_UPDATE :
						ProjectPlanCaller.doPrepareUpdatePLMeasurementsProg(request, response);
						break;					
					case PL_MEASUREMENTS_PROGRAM_ADD :
						ProjectPlanCaller.doAddPLMeasurementsProg(request, response);
						break;					
					case PL_MEASUREMENTS_PROGRAM_UPDATE :
						ProjectPlanCaller.doUpdatePLMeasurementsProg(request, response);
						break;
					case PL_MEASUREMENTS_PROGRAM_DELETE :
						ProjectPlanCaller.doDeletePLMeasurementsProg(request, response);
						break;
					// MEASUREMENTS PROGRAM END
					
					// PROJECT SCHEDULE START					
					case PL_PROJECT_SCHEDULE_GET_LIST :
						ProjectPlanCaller.doViewPLProjSchedList(request, response);
						break;
					case PL_PROJECT_SCHEDULE_PREPARE_ADD :
						ProjectPlanCaller.doPrepareAddPLProjSched(request, response);
						break;
					case PL_PROJECT_SCHEDULE_PREPARE_UPDATE :
						ProjectPlanCaller.doPrepareUpdatePLProjSched(request, response);
						break;					
					case PL_PROJECT_SCHEDULE_ADD :
						ProjectPlanCaller.doAddPLProjSched(request, response);
						break;					
					case PL_PROJECT_SCHEDULE_UPDATE :
						ProjectPlanCaller.doUpdatePLProjSched(request, response);
						break;
					case PL_PROJECT_SCHEDULE_DELETE :
						ProjectPlanCaller.doDeleteSched(request, response);
						break;
					// PROJECT SCHEDULE END
					
					// COMMUNICATION & REPORTING START
					case COMREPORT_VIEW :						
						ProjectPlanCaller.doViewPLComReportList(request, response);
						break;
					case COMREPORT_PRE_ADD :
						ProjectPlanCaller.doPrepareAddPLComReport(request, response);
						break;
					case COMREPORT_PRE_UPDATE :
						ProjectPlanCaller.doPrepareUpdatePLComReport(request, response);
						break;					
					case COMREPORT_ADD :
						ProjectPlanCaller.doAddPLComReport(request, response);
						break;					
					case COMREPORT_UPDATE :
						ProjectPlanCaller.doUpdatePLComReport(request, response);
						break;
					case COMREPORT_DELETE :
						ProjectPlanCaller.doDeleteComReport(request, response);
						break;
					// COMMUNICATION & REPORTING END
					
					// DEFINITIONS AND ACRONYMS
					case DEFINITIONS_ACRONYMS_VIEW :						
						ProjectPlanCaller.doViewPLDefAcrList(request, response);
						break;
					case DEFINITIONS_ACRONYMS_PRE_ADD :
						ProjectPlanCaller.doPrepareAddPLDefAcr(request, response);
						break;
					case DEFINITIONS_ACRONYMS_PRE_UPDATE :
						ProjectPlanCaller.doPrepareUpdatePLDefAcr(request, response);
						break;					
					case DEFINITIONS_ACRONYMS_ADD :
						ProjectPlanCaller.doAddPLDefAcr(request, response);
						break;					
					case DEFINITIONS_ACRONYMS_UPDATE :
						ProjectPlanCaller.doUpdatePLDefAcr(request, response);
						break;
					case DEFINITIONS_ACRONYMS_DELETE :
						ProjectPlanCaller.doDeleteDefAcr(request, response);
						break;
					// COMMUNICATION & REPORTING END
					
					case PL_SIG_GET_LIST :
						ProjectPlanCaller.doLoadPLSignatureList(request, response);
						break;
					case PL_SIG_APPROVE :
						ProjectPlanCaller.doUpdatePLSignature(request, response);
						break;
					case PL_SIG_DEL :
						ProjectPlanCaller.doDeletePLApproval(request, response);
						break;
					case PL_SIG_ADD :
						ProjectPlanCaller.doAddPLApproval(request, response);
						break;
					case PL_SIG_RESET :
						ProjectPlanCaller.doResetPLApproval(request, response);
						break;
					case PL_CHANGE_SIG_APPROVE :
						ProjectPlanCaller.doUpdateChangeSignature(request, response);
						break;
					case PL_CHANGE_SIG_DEL :
						ProjectPlanCaller.doDeleteChangeApproval(request, response);
						break;
					case PL_CHANGE_SIG_ADD :
						ProjectPlanCaller.doAddChangeApproval(request, response);
						break;
					case PL_CHANGE_SIG_RESET :
						ProjectPlanCaller.doResetChangeApproval(request, response);
						break;
					case PL_UPLOAD_SCHED:
						ProjectPlanCaller.doUploadSched(request, response);
						break;
					case PL_GET_SCHED_FILE:
						ProjectPlanCaller.doGetSchedFile(request, response);
						break;
					case NORM_GET_LIST :
						NormCaller.doGetNormTable(request, response);
						break;
					case MILESTONE_GET_PAGE :
						if ("1".equals(request.getParameter("export")))
							callPage("milestoneReportExport.jsp",request,response);
						else
							ReportCaller.doGetMilestone(request, response);
						break;
					//anhtv08-start
					case MILESTONE_OBJECTIVE_UPDATE:
							ReportCaller.updateSpecificObjective(request,response,"");
							break;
					//end	
					case UPDATE_CI_REGISTER :
						CIRegister.SetCIRegister(workUnitID, request);
					case GET_CI_REGISTER :
						session.setAttribute("CIRegister", CIRegister.GetCIRegister(workUnitID));
						callPage("CIRegister.jsp",request,response);
						break;
					case VIEW_CI_REGISTER :						
						callPage("CIRegisterUpdate.jsp",request,response);
						break;
					case GET_USER_PROFILES_LIST :
						UserProfileCaller.doShowAllPeople(request, response);
						break;
					case SEARCH_USER_PROFILES :
						UserProfileCaller.doSearchPeople(request, response);
						break;					
					case GET_USER_PROFILE :
					case VIEW_USER_PROFILE : 
						UserProfileCaller.doUserDetail(request, response);
						break;			
					case USER_CHANGE_PASSWORD : 
						UserProfileCaller.doUserChangePassword(request, response);
						break;						
					case DELETE_USER_PROFILES:
						UserProfileCaller.doDeleteUserProfile(request, response);
						break;
					case UPDATE_USER_PROFILE :
						UserProfileCaller.doUpdateUserProfile(request, response);
						break;
					case ADD_USER_PROFILE :
						UserProfileCaller.doAddNewUserProfile(request, response);
						break;
					case USER_PROFILE_DETAIL :						
						callPage("UserProfileDetail.jsp",request,response);
						break;	
					case UPDATE_DEFECTS :
						DefectCaller.defectUpdateCaller(request, response);
						break;
					case DETAIL_DEFECT_PLAN :
						DefectCaller.getDetailDefectPlan(request, response);
						break;
					case DETAIL_DEFECT_REPLAN :						
						DefectCaller.getDetailDefectReplan(request, response);
						break;					
					case DETAIL_DEFECT_RATE :						
						DefectCaller.getDetailDefectRate(request, response);
						break;						
					case ADD_WP_DEFECT_PLAN :
						DefectCaller.updateDetailDefectPlan(request, response);
						break;
					case UPDATE_WP_DEFECT_REPLAN :
						DefectCaller.updateDetailDefectReplan(request, response);
						break;						
					case UPDATE_DEFECT_RATE_REPLAN :
						DefectCaller.updateDetailDefectRateReplan(request, response);
						break;							
					case DELETE_DEFECT_PLAN :
						DefectCaller.deleteDetailDefectPlan(request, response);
						break;
					case DEFECT_PROGRESS:
						DefectCaller.defectProgressCaller(request, response);
						break;
					case DEFECT_REV_PRODUCT_PRE_ADD :
						DefectCaller.doPrepareAddDefectRevProduct(request,response);
						break;
					case DEFECT_REV_PRODUCT_PRE_UPDATE :
						DefectCaller.doPrepareUpdateDefectRevProduct(request,response);
						break;
					case DEFECT_REV_PRODUCT_ADD :
						DefectCaller.doAddDefectRevProduct(request,response);
						break;
					case DEFECT_REV_PRODUCT_UPDATE :
						DefectCaller.doUpdateDefectRevProduct(request,response);
						break;
					case DEFECT_REV_PRODUCT_DELETE :
						DefectCaller.doDeleteDefectRevProduct(request,response);
						break;
					case DEFECT_TEST_PRODUCT_PRE_ADD :
						DefectCaller.doPrepareAddDefectTestProduct(request,response);
						break;
					case DEFECT_TEST_PRODUCT_PRE_UPDATE :
						DefectCaller.doPrepareUpdateDefectTestProduct(request,response);
						break;
					case DEFECT_TEST_PRODUCT_ADD :
						DefectCaller.doAddDefectTestProduct(request,response);
						break;
					case DEFECT_TEST_PRODUCT_UPDATE :
						DefectCaller.doUpdateDefectTestProduct(request,response);
						break;
					case DEFECT_TEST_PRODUCT_DELETE :  
						DefectCaller.doDeleteDefectTestProduct(request,response);
						break;
					case UPDATE_DASHBOARD :
						ReportCaller.submitWeeklyReport(request, response);
						break;
					case UPDATE_MR_COMMENTS :
						ReportCaller.doUpdateMRComments(request, response);
						break;
					case UPDATE_PROJECT_POINT :
						ReportCaller.doUpdateProjectPoint(request, response);
						break;
					case GET_ORG_POINT_LIST :
						ReportCaller.doGetGroupPoint2(request, response);
						break;
					case GET_ORG_POINTBA_LIST :
						ReportCaller.doGetGroupPoint3(request, response);
						break;
//					case GET_ORG_POINT_LIST :
//						callPage("orgPointUpdate.jsp",request,response);
//						break;	
					case UPDATE_GROUP_POINT :
						ReportCaller.doGetGroupPoint(request, response);
						break;
					// Note Update
					case UPDATE_GROUP_POINT2 :
						ReportCaller.doGetGroupPoint2(request, response);
						break;
					// HanhTN add UPDATE_GROUP_POINT3 case	
					case UPDATE_GROUP_POINT3 :
						ReportCaller.doGetGroupPoint3(request, response);
						break;
					case UPDATE_GROUP_METRIC :
						ReportCaller.doUpdateGroupPoint(request, response);
						break;
					// Note Update	
					case UPDATE_GROUP_METRIC2 :
						ReportCaller.doUpdateGroupPoint2(request, response);
						break;
//					HanhTN add UPDATE_GROUP_METRIC3 case	
					case UPDATE_GROUP_METRIC3 :
						ReportCaller.doUpdateGroupPointBA(request, response);
						break;	
					case VIEW_GROUP_METRIC :
						ReportCaller.doViewGroupPoint(request, response);
						break;
					case VIEW_PROJECT_POINT :
						ReportCaller.doViewProjectPoint(request, response);
						break;
					case PROJECT_SEARCH :
						ReportCaller.doViewProjectPoint(request, response);
						break;
					case ADDNEW_DPTASK :					    
						ReportCaller.doAddDPTask(request, response, i);
						break;
					case DELETE_DPTASK :
						ReportCaller.doDeleteDPTask(request, response, i);
						break;
					case UPDATE_DPTASK :
						ReportCaller.doUpdateDPTask(request, response, i);
						
						break;						
					case ADDNEW_DARPLAN :					    
						ReportCaller.doAddDarPlan(request, response, i);
						break;	
					case UPDATE_DARPLAN :
						ReportCaller.doUpdateDarPlan(request, response, i);
						break;			
					case DELETE_DARPLAN :					
						ReportCaller.doDeleteDarPlan(request, response, i);
						break;									    					    					    
					case DEFECT_LOG:
						DefectCaller.defectDPLogListing(request, response);
						break;
					case DEFECT_LOG_ADDNEW:
						DefectCaller.doAddnewDPLog(request, response);
						break;
					case DEFECT_LOG_UPDATE:
						DefectCaller.doUpdateDPLog(request, response);
						break;
					case DEFECT_LOG_DELETE:
						DefectCaller.doDeleteDPLog(request, response);
						break;
					case DEFECT_LOG_DRILL :
						DefectCaller.dpDrill(request, response);
						break;
					case COMMON_DEFECT:
						DefectCaller.commDefListing(request, response);
						break;
					case COMMON_DEFECT_ADDNEW:
						DefectCaller.doAddnewCommDef(request, response);
						break;
					case COMMON_DEFECT_UPDATE:
						DefectCaller.doUpdateCommDef(request, response);
						break;
					case COMMON_DEFECT_DELETE:
						DefectCaller.doDeleteCommDef(request, response);
						break;
					case DP_DATABASE:
						DefectCaller.defectDPDatabase(request, response);
						break;
					case PROJECT_SKILL_DETAIL :
						ReportCaller.doGetSkillDetail(request, response);
						break;
					case UPDATE_SKILL :
						ReportCaller.doUpdateSkill(request, response);
						break;
					case DELETE_SKILL :
						ReportCaller.doDeleteSkill(request, response);
						break;
					case TO_ADD_SKILL :
						ReportCaller.doAddSkill(request, response);
						break;
					case ADD_SKILL :
						ReportCaller.doAddSkill2(request, response);
						break;
					case REPORTS_UPDATE_BATCH_TEAM_EVALUATION:
						ReportCaller.doBatchUpdatePostMortem(request, response);
						break;
					case DO_UPDATE_TEAM_EVALUATION_POST_MORTEM:
						ReportCaller.doUpdateTeam(request, response);
						break;
					case DO_DELETE_IN_TEAM_BATCH_POST_MORTEM:
						ReportCaller.doDeleteTeam(request, response);
						break;
					case REFRESH_SKILL_LIST :
						ReportCaller.doRefreshSkillList(request, response);
						break;
					case UPDATE_HIS:
						ReportCaller.doUpdateHis(request, response);
						break;
					case SAVE_HIS:
						ReportCaller.doSaveHis(request, response);
						break;
					case DELETE_HIS:
						ReportCaller.doDeleteHis(request, response);
						break;
					case ADDNEW_HIS:
						ReportCaller.doAddnewHis(request, response);
						break;
		//************Group Handlers***************
					case GROUP_INFO:
						WorkUnitCaller.doGetGroupInfo(request, response);
						break;
					case GROUP_INFO_UPDATE:
						WorkUnitCaller.doUpdateGroupInfo(request, response);
						break;
					case GROUP_CALENDAR_EFFORT:
						HumanResourceCaller.doGetCalendarEffortList(request, response);
						break;
					case GROUP_RESOURCE_ALLOCATION:
						HumanResourceCaller.doGetHumanResourceList(request, response);
						break;
					case GROUP_RESOURCE_ALLOCATION_EXPORT:
						HumanResourceCaller.doHumanResourceAllocationExport(request, response, this);
						break;
					case PCB_LOADSEARCHPAGE :
						PCBCaller.pcbLoadSearchPage(request, response);
						break;
					case PCB_CREATE :
						PCBCaller.pcbCreate(request, response);
						break;
					case PCB_VIEW :
						PCBCaller.pcbView(request, response);
						break;
					case PCB_SELECT_PROJECT :
						PCBCaller.pcbSelectProject(request, response);
						break;
					case PCB_SAVEINFO :
						PCBCaller.pcbSaveGalInfo(request, response);
						break;
					case PCB_SAVEDETAILS :
						PCBCaller.pcbSaveDetails(request, response);
						break;
					case PCB_SAVECOMMENTS :
						PCBCaller.pcbSaveComments(request, response);
						break;
					case PCB_SAVECONCLUSIONS :
						PCBCaller.pcbSaveConclusions(request, response);
						break;
					case PCB_DELETE :
						PCBCaller.pcbDelete(request, response);
						break;
					case PRODUCT_INDEXES :
						ProductIndexesCaller.doGetProductIndexes(request, response);
						break;
					case INDEX_DRILL :
						ProductIndexesCaller.doGetIndexDrill(request, response);
						break;
					case LOADNORMS :
						NormCaller.getPlannedNorms(request, response, "");
						break;
					case SAVENORMS :
						NormCaller.saveNormTable(request, response);
						break;
					case GET_SQA_DEFECT_ORIGIN:
						SupportGroupCaller.doGetSQADefectOriginReport(request, response);
						break;
					case SQA_DEFECT_TYPE:
						SupportGroupCaller.doGetSQADefectTypeReport(request, response);
						break;
					case LOADPLANNING :
						PlanCaller.loadPlanning(request, response, "");
						break;
					case SAVEPLANNING :
						PlanCaller.savePlanning(request, response);
						break;
					case GROUPMONITORING :
					case ORGMONITORING :
					case TASK_LIST://support
						MonitoringCaller.taskCaller(request, response);
						break;
					case PLAN:
						MonitoringCaller.getDecisionList(request, response,TaskInfo.typesPlan);
						break;
					case DECISION:
						MonitoringCaller.getDecisionList(request, response,TaskInfo.typesDecision);
						break;
					case PLAN_ADD:
						MonitoringCaller.decisionAddCaller(request, response,TaskInfo.PLAN);
						break;
					case DECISION_ADD:
						MonitoringCaller.decisionAddCaller(request, response,TaskInfo.DECISION);
						break;
					case PLAN_UPDATE:						
					case DECISION_UPDATE:
						MonitoringCaller.decisionUpdateCaller(request, response);
						break;
					
					case PLAN_DELETE:		
					case DECISION_DELETE:						 		
						MonitoringCaller.decisionDeleteCaller(request, response);												
						break;
					case GROUPMONITORINGDRILL :
						MonitoringCaller.drillCaller(request, response);
						break;
					case CBOMONITORING :
						MonitoringCaller.changeDateCbo(request, response);
						break;
					case TASK_ADDPREP:
						MonitoringCaller.taskAddPrepCaller(request, response);
						break;
					case TASK_ADD:
						MonitoringCaller.taskAddCaller(request, response);
						break;
					case TASK_UPDATEPREP:
						MonitoringCaller.taskUpdatePrepCaller(request,response);
						break;
					case TASK_UPDATE:
						MonitoringCaller.taskUpdateCaller(request,response);
						break;
					case TASK_DELETE:
						MonitoringCaller.taskDeleteCaller(request,response);
						break;
					case TASK_DRILLUP:
						MonitoringCaller.drillUpCaller(request,response);
						break;
					case GET_DMS :
					case GET_TIMESHEET :
					case GET_NCMS :
					case GET_ITS :
					case GET_DASHBOARD :
					case GET_CALLLOG:
						Security.getLink(requestType, request, response);
						break;
//					case GET_RSA :
//						Rsa.getAuthenticate(request, response);
//						break;
					case GET_PAGE:
						String page=request.getParameter("page");
						callPage(page,request,response);
						break;
					case PROASS_PROJECT_DESC:
						ProcessAssetsCaller.doLoadProjectDesc(request,response);
						break;
					case PROASS_PROJECT_DETAIL:
						ProcessAssetsCaller.doLoadProjectDetails(request,response);
						break;
					case PROASS_TAILORING_DEVIATION:
						ProcessAssetsCaller.doLoadTailorings(request,response);
						break;
					case PROASS_RISK:
						ProcessAssetsCaller.doLoadRisksEncountered(request,response);
						break;
					case PROASS_PRACTICE_LESSON:
						ProcessAssetsCaller.doLoadPracticeLesson(request,response);
						break;
					case PROASS_ISSUE:
						ProcessAssetsCaller.doLoadIssue(request,response);
						break;
					case SET_METRIC:
						NormCaller.setMetric(request,response);
						break;
					case PQA_REPORT:
						SupportGroupCaller.getSupportReport(request,response);
						break;
					case TEAM_SIZE_PROGRESS:
						TeamSizeProgressCaller.TeamSizeCaller(request,response);
						break;
					case XML_PROJECT_GENERATION:
						WorkUnitCaller.doGenerateXMLProjectList(request, response);
						break;
					case FILLTER_USER:
						UserProfileCaller.doFilterUser(request, response);
						break;
					case FILLTER_CUSTOMER:
						UserProfileCaller.doFilterCustomer(request, response);
						break;
					case FILLTER_USER_ASSIGNED_PROJECT:
						UserProfileCaller.doFilterUserAssigned(request, response);
						break;
					case EST_DEFECT_VIEW :
						DefectCaller.estDefectViewCaller(request, response);
						break;
						
					case EST_DEFECT_ADD :
						DefectCaller.estDefectAddCaller(request, response);
						break;
					
					case EST_DEFECT_DELETE :
						DefectCaller.doDeleteComReport(request, response);
						break;
						
					case EST_DEFECT_PRE_UPDATE :
						DefectCaller.doPrepareUpdateEstDef(request, response);
						break;
						
					case EST_DEFECT_UPDATE :
						DefectCaller.estDefUpdateCaller(request, response);
						break;
					
					case EST_EFFORT_VIEW :
						RequirementCaller.getEstEffort(request, response);
						break;

					case EST_EFFORT_UPDATE:
						RequirementCaller.updateEstEffort(request, response);
						break;
					case EST_EFFORT_DELETE:
						RequirementCaller.deleteEstEffort(request, response);
						break;

					case EST_EFFORT_ADD_PRE:
						RequirementCaller.estEffortAddPreCaller(request, response);
						break;

					case EST_EFFORT_ADD:
						RequirementCaller.estEffortAddCaller(request, response);
						break;

					case FINANCE_VIEW :
						FinanCaller.getFinance(request, response);
						break;
						
					case FINANCE_DELETE:
						FinanCaller.deleteFinancePlan(request, response);
						break;
					
					case FINANCE_UPDATE:
						FinanCaller.updateFinancePlan(request, response);
						break;
											
					case FINANCE_ADD_PRE:
						FinanCaller.financeAddPreCaller(request, response);
						break;

					case FINANCE_ADD:
						FinanCaller.financePlanAdd(request, response);
						break;

					case ORGANIZATION_VIEW :
						OrganizationCaller.getOrganization(request, response);
						break;
					
					case ORGANIZATION_UPDATE:
						OrganizationCaller.updateOrgPlan(request, response);
						break;
					case MILESTONE_DEFECT_UPDATE:
						ReportCaller.updateDefect(request,response,"");
						break;	
					/*	
					case MIGRATE_DATA:
						WorkOrderCaller.doMigrateCustomerMetricsTable(request,response);
						break;
					*/						
					//Don't forget to specify security settiong in Security.check function
					default :
						callPage("error.jsp?error=Stop playing, your IP is logged !",request,response);
						break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final String CONTENT_TYPE_NORMAL="text/html;charset=UTF-8";
	public static final String CONTENT_TYPE_WORD="application/msword;charset=UTF-8";
	public static final String CONTENT_TYPE_EXCEL="application/vnd.ms-excel;charset=UTF-8";
	public static final String CONTENT_TYPE_BINARY="application/octet-stream";
	public static void callPage(String strJSPPage,HttpServletRequest request,HttpServletResponse response){
		callPage(strJSPPage,request, response,CONTENT_TYPE_NORMAL);
	}
	public static void callPage(String strJSPPage,HttpServletRequest request,HttpServletResponse response,String contentType){
		try{
			response.addHeader("Pragma", "No-cache");
			response.addHeader("Cache-control", "no-cache");
			response.addDateHeader("Expires", 1);
			response.setContentType(contentType);
			request.getRequestDispatcher(strJSPPage).forward(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}