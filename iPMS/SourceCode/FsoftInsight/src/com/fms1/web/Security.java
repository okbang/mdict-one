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
import java.util.Vector;
import javax.servlet.http.*;
import com.fms1.common.*;
import com.fms1.infoclass.*;
import com.fms1.tools.LanguageChoose;
/**
 * Manages security information for pages.
 * @author Manu
 * @date Mar 25, 2003
 */
public class Security implements Constants {
	public static final int none = RightForPage.RIGHT_NONE;
	public static final int view = RightForPage.RIGHT_VIEW;
	public static final int mana = RightForPage.RIGHT_MANAGE;
	public static final int securiPage(
		final String pageName,
		final HttpServletRequest request,
		final HttpServletResponse response) {
			int retVal = RightForPage.RIGHT_NONE;
			try {
				retVal = getRights(pageName, request);				
				if (retVal == RightForPage.RIGHT_NONE) {
					HttpSession session = request.getSession();
					//no view right
					Language lang = (Language) session.getAttribute("lang");
					Fms1Servlet.callPage("error.jsp?error=" + lang.userProfile_msgError,request,response);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				return retVal;
			}
		}
	/**
	 * for current user
	 */
	public static final int getRights(final String pageName, final HttpServletRequest request) {
		int retVal = RightForPage.RIGHT_NONE;
		try {
			HttpSession session = request.getSession();
			Vector rightForPage = (Vector) session.getAttribute("rightForPage");
			retVal = securiCheck(pageName, rightForPage);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return retVal;
		}
	}
	/**
	 * for non logged users
	 */
	public static final int getUserRights(final String pageName, long userID,long workUnit) {
		int retVal = RightForPage.RIGHT_NONE;
		try {
			Vector [] userRights=Roles.getUserRoles(userID);
			String rightGroup = WorkUnitCaller.getRightGroupID(userRights,workUnit);
			Vector rightForPage = RightForPage.getRightForPage(rightGroup);
			retVal = securiCheck(pageName, rightForPage);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return retVal;
		}
	}
	public static final int securiCheck(final String pageName, Vector rightForPage) {
		try {
			if ((rightForPage == null) || (pageName == null))
				return RightForPage.RIGHT_NONE;
			else
				for (int i = 0; i <rightForPage.size() ; i++) {
					final RightForPageInfor rpi = (RightForPageInfor) rightForPage.elementAt(i);
					if (pageName.trim().toLowerCase().equals(rpi.pageName2.trim().toLowerCase())){
						return rpi.privilege;
					}
				}
			return RightForPage.RIGHT_NONE;
		}
		catch (Exception e) {
			e.printStackTrace();
			return RightForPage.RIGHT_NONE;
		}
	}
	public static final void getLink(int linkID, final HttpServletRequest request, HttpServletResponse response) {
		try {
			//String rsalink = "";
			String loginlink = "";
			switch (linkID) {
				case Constants.GET_DMS :
					//rsalink = Parameters.localhost + "/" + Parameters.dmsURL + "?hidAction=GetRsaAction";
					loginlink = "../" + Parameters.dmsURL;
					break;
				case Constants.GET_DASHBOARD :
					//rsalink = Parameters.localhost + "/" + Parameters.dashboardURL + "?hidAction=GetRsaAction";
					loginlink = "../" + Parameters.dashboardURL;
					break;
				case Constants.GET_CALLLOG :				
					//rsalink = Parameters.localhost + "/" + Parameters.callLogURL + "?hidAction=GetRsaAction";
					loginlink = "../" + Parameters.callLogURL;
					break;
				case Constants.GET_NCMS :
					//rsalink = Parameters.localhost + "/" + Parameters.ncmsURL + "?hidAction=GetRsaAction";
					loginlink = "../" + Parameters.ncmsURL;
					break;
				case Constants.GET_TIMESHEET :
					//rsalink = Parameters.localhost + "/" + Parameters.timesheetURL + "?hidAction=GetRsaAction";
					loginlink =  "../" + Parameters.timesheetURL;
					break;
				case Constants.GET_ITSPROJECT:
				case Constants.GET_ITS:
					//rsalink = Parameters.itsURL + "?its=1";
					loginlink = Parameters.itsURL;
					break;
			}
			HttpSession session = request.getSession();
			LinkInfo link = new LinkInfo();
			link.type=linkID;
			link.link = loginlink;
			UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
			if (userLoginInfo != null){
				link.name = userLoginInfo.account;
				link.password = userLoginInfo.Password;
			}
			else{
				link.name = "";
				link.password = "";
			}
			session.setAttribute("link", link);
			Fms1Servlet.callPage("redirect.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean check(int requestType, HttpServletRequest request, HttpServletResponse response)
		throws java.io.IOException {
		final HttpSession session = request.getSession();
		String page = null;
		int level = -1;
		String strLevel=(String) session.getAttribute("workUnitType");
		if (strLevel!=null)
			level=Integer.parseInt(strLevel);
		int right = -1;
		
		switch (requestType) {
			case WEEKLY_REPORT_VIEW_INIT :
			case WEEKLY_REPORT_VIEW :
			case GET_POST_MORTEM :
			case MILESTONE_GET_PAGE :
			case NORM_GET_LIST :
			case PROJECT_SKILL_DETAIL :
			case REFRESH_SKILL_LIST :
			case MILESTON_UPDATE_SPECIFIED_OBJECTIVE:
				page = "Project reports";
				right = view;
				break;
			case UPDATE_WR_COMMENTS :
			case ADDNEW_FURTHER_WORK :
			case UPDATE_FURTHER_WORK :
			case DELETE_FURTHER_WORK :
			case UPDATE_PM_HEADER :
			case UPDATE_PM_CAUSE :
			case UPDATE_MR_COMMENTS :
			case MILESTONE_OBJECTIVE_UPDATE:
			case UPDATE_PROJECT_POINT :
			case UPDATE_DASHBOARD :
			
			case ADDNEW_DPTASK :
			case DELETE_DPTASK :
			case UPDATE_DPTASK :
			
			case ADDNEW_DARPLAN :
			case UPDATE_DARPLAN :
			case DELETE_DARPLAN :
			
			case UPDATE_SKILL :
			case DELETE_SKILL :
			case TO_ADD_SKILL :
			case ADD_SKILL :
			case UPDATE_HIS:
			case SAVE_HIS:
			case DELETE_HIS:
			case ADDNEW_HIS:
			case MILESTONE_DEFECT_UPDATE:
				page = "Project reports";
				right = mana;
				break;
			case DEFECT_PROGRESS:
			case DEFECT_VIEW :			
			case DETAIL_DEFECT_PLAN :
			case DETAIL_DEFECT_REPLAN :
			case DETAIL_DEFECT_RATE :
			case DEFECT_LOG:
			case COMMON_DEFECT:
			case DEFECT_LOG_DRILL:
            case PLAN_DRE_PLAN_DEFECT : // Defect Removal Efficiency
            case PLAN_DRE_LEAKAGE :
				page = "Defects";
				right = view;
				break;
			case ADD_WP_DEFECT_PLAN :
			case UPDATE_WP_DEFECT_REPLAN:
			case UPDATE_DEFECT_RATE_REPLAN:
			case UPDATE_PRODUCT_DEFECT_TRACKING:
			case DELETE_DEFECT_PLAN :
			case UPDATE_DEFECTS :			
			case DEFECT_LOG_ADDNEW :
			case DEFECT_LOG_UPDATE :
			case DEFECT_LOG_DELETE :
			case COMMON_DEFECT_ADDNEW :
			case COMMON_DEFECT_UPDATE :
			case COMMON_DEFECT_DELETE :
            case PLAN_DRE_BY_QC_STAGE : // Defect Removal Efficiency
            case PLAN_DRE_BY_QC_STAGE_SAVE:
            case PLAN_DRE_REPLAN :
            case PLAN_DRE_REPLAN_SAVE:
            case PLAN_DRE_DEFECT_RATE :
            case PLAN_DRE_DEFECT_RATE_SAVE :
			case DEFECT_REV_PRODUCT_PRE_ADD :
			case DEFECT_REV_PRODUCT_PRE_UPDATE :
			case DEFECT_REV_PRODUCT_ADD :
			case DEFECT_REV_PRODUCT_UPDATE :
			case DEFECT_REV_PRODUCT_DELETE :
				
			case DEFECT_TEST_PRODUCT_PRE_ADD :
			case DEFECT_TEST_PRODUCT_PRE_UPDATE :
			case DEFECT_TEST_PRODUCT_ADD :
			case DEFECT_TEST_PRODUCT_UPDATE :
			case DEFECT_TEST_PRODUCT_DELETE :  
			case LOAD_CUSTOMER_PAGE :
				page = "Defects";
				right = mana;
				break;
            case REQUIREMENT_UPDATE_PROCESS:
			case REQUIREMENT_PLAN_RCR_BATCH_SAVE:
			case REQUIREMENT_ADDNEW_PREP :
			case REQUIREMENT_ADDNEW :
			case REQUIREMENT_IMPORT :
			case REQUIREMENT_UPDATE_PREP :
			case REQUIREMENT_UPDATE :
			case REQUIREMENT_DELETE :
			case REQUIREMENT_BATCH_EDIT :
			case REQUIREMENT_BATCH_UPDATE :
			case REQUIREMENT_UPDATE_STATUS :
			case REQUIREMENT_DETAIL:
			case REQUIREMENT_STATUS:
				page = "Requirements";
				right = mana;
				break;
			case REQUIREMENT_PLAN:
			case REQUIREMENT_PLAN_RCR_BATCH:
			case REQUIREMENT_REPLAN_RCR_BATCH:
			case REQUIREMENT_LIST_INIT :
			case REQUIREMENT_LIST :
			case REQUIREMENT_NEXT :
			case REQUIREMENT_PREV :
			case REQUIREMENT_GROUP_BY :
            case REQUIREMENT_GRAPH:
				page = "Requirements";
				right = view;
				break;
			case MODULE_LIST :
			case MODULE_DETAIL:
            case PRODUCT_LOC_LIST:
            case PRODUCT_LOC_DETAIL:
				page = "Size";
				right = view;
				break;
			case SCHE_SIZE_INPUT:
			case MODULE_ADDNEW_PREP :
			case MODULE_ADDNEW :
			case MODULE_IMPORT :
			case MODULE_UPDATE_PREP :
			case MODULE_UPDATE :
			case MODULE_DELETE :
			case BATCH_MODULE_PREPARE_ADD :
			case BATCH_MODULE_PREPARE_UPDATE :
			case BATCH_MODULE_ADD :
			case BATCH_MODULE_UPDATE :
			case BATCH_MODULE_DELETE :
			
            case PRODUCT_LOC_UPDATE:
            case PRODUCT_LOC_UPDATE_SAVE:
            case PRODUCT_LOC_ADD:
            case PRODUCT_LOC_ADD_SAVE:
				page = "Size";
				right = mana;
				break;
			case COMPL_RATE_UPDATE :
				page = "Project parameters";
				right = mana;
				break;
			case DISTR_RATE_INIT :
				page = "Project parameters";
				right = view;
				break;
			case CONVERSION_LIST_INIT :
			case CONVERSION_LIST :
			case CONVERSION_LIST_NAME :
				page = (level == WorkUnitInfo.TYPE_ADMIN) ? "Parameters" : "Project parameters";
				right = view;
				break;
			case CONVERSION_UPDATE :
			case CONVERSION_ADDNEW :
			case CONVERSION_DELETE :
				page = (level == WorkUnitInfo.TYPE_ADMIN) ? "Parameters" : "Project parameters";
				right = mana;
				break;
			case METHOD_LIST :
				page = "Parameters";
				right = view;
				break;
			case METHOD_ADDNEW :
			case METHOD_UPDATE :
			case METHOD_REMOVE :
				page = "Parameters";
				right = mana;
				break;
            case CONTRACT_TYPE :
                page = "Parameters";
                right = view;
                break;
            case CONTRACT_TYPE_ADD : 
            case CONTRACT_TYPE_UPDATE :
            case CONTRACT_TYPE_REMOVE :                
                page = "Parameters";
                right = mana;
                break;
			case BUSINESS_DOMAIN :
				page = "Parameters";
				right = view;
				break;
			case BIZDOMAIN_UPDATE :
			case BIZDOMAIN_REMOVE :
			case BIZDOMAIN_ADD :
				page = "Parameters";
				right = mana;
				break;
			case APP_TYPE:
				page = "Parameters";
				right = view;
				break;
			case APPTYPE_UPDATE :
			case APPTYPE_REMOVE :
			case APPTYPE_ADD :
				page = "Parameters";
				right = mana;
				break;
			case TAILORING_REF:
				page = "Parameters";
				right = view;
				break;
			case PROCESS_TAILORING:
				page = "Parameters";
				right = view;
				break;
			case PROCESS_TAILORING_SEARCH:
				page = "Parameters";
				right = view;
				break;
			case PROCESS_TAILORING_UPDATE:
			case PROCESS_TAILORING_REMOVE:
			case PROCESS_TAILORING_ADD:
				page = "Parameters";
				right = mana;
				break;
			case RISK_LIST_INIT :
			case RISK_LIST_OTHER :
			case RISK_LIST_OTHER_CALL_BACK :
            case RISK_UPDATE_PREP :
            case RISK_DATABASE_UPDATE_PREP :
			case RISK_DATABASE_ADD_PREPARE :
			case RISK_DATABASE_REMOVE :
			case RISK_DATABASE_ADD :
			case RISK_NEXT :
			case RISK_PREV :
			case RISK_COMMON :
			case RISK_IDENTIFY :
				page = "Risks";
				right = view;
				break;
			case RISK_ADDNEW_PREP :
			case RISK_ADD_PREP :
			case RISK_ADDNEW :
			case RISK_MIGRATE :
			case RISK_ADD :
			case RISK_UPDATE :
			case RISK_SOURCE_UPDATE_PREP :
			case RISK_SOURCE_UPDATE :
			case RISK_DATABASE_UPDATE :
			case RISK_IMPORT :
			case RISK_DELETE :
			case RISK_MITIGATION_DELETE :
			case RISK_CONTINGENCY_DELETE :
				page = "Risks";
				right = mana;
				break;
			case ISSUE :
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Organization issues";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Group issues";
				else
					page = "Project issues";
				right = view;
				break;
			case ISSUE_ADDNEW :
			case ISSUE_ADDPREP :
			case ISSUE_UPDATE :
			case ISSUE_DELETE :
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Organization issues";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Group issues";
				else
					page = "Project issues";
				right = mana;
				break;

			case VIEW_PROJECT_POINT :
			//case MIGRATE_DATA:
				page = "Project home";
				right = view;
				break;
			case HOME_SQA:
			case HOME_PQA:
			case WORKUNIT_HOME2 :
			case GROUP_INFO:
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Organization home";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Group home";
				right = view;
				break;
			case GROUP_INFO_UPDATE:
				page = "Group home";
				right = mana;
				break;
				//ROLES
			case GET_RIGHT_GROUP_LIST :
			case GET_RIGHT_GROUP :
				page = "Roles";
				right = view;
				break;
			case GET_PAGE_LIST : //prepare add a RG
			case ADDNEW_RIGHT_GROUP :
			case UPDATE_RIGHT_GROUP :
			case DELETE_RIGHT_GROUP :
				page = "Roles";
				right = mana;
				break;
				//WORKUNITS
			case GET_WORK_UNIT_LIST :
			case GET_WORK_UNIT :
				page = "Work Unit";
				right = view;
				break;
			case ADDNEW_WORK_UNIT_ORGANIZATION :
			case ADDNEW_WORK_UNIT_GROUP :
			case ADDNEW_WORK_UNIT_PROJECT :
			case ADDNEW_WORK_UNIT_PROJECT_RELOAD:
			case UPDATE_WORK_UNIT_ORGANIZATION :
			case UPDATE_WORK_UNIT_GROUP :
			case DELETE_WORK_UNIT_ORGANIZATION :
			case DELETE_WORK_UNIT_GROUP :
			case DELETE_WORK_UNIT_PROJECT :
			case IMPORT :
				page = "Work Unit";
				right = mana;
				break;
				//PRACTICES
			case GET_PRACTICE :
			case GET_PRACTICE_LIST :
				page = "Practice and Lessons";
				right = view;
				break;
			case ADDNEW_PRACTICE :
			case UPDATE_PRACTICE :
			case DELETE_PRACTICE :
			case EXPORT_PM_CAUSE :
				page = "Practice and Lessons";
				right = mana;
				break;
				//Training-Project plan
			case ADDNEW_TRAINING :
			case UPDATE_TRAINING :
			case DELETE_TRAINING :
			case ADDNEW_FINAN :
			case UPDATE_FINAN :
			case DELETE_FINAN :
			case ADDNEW_OTHER_ACTIVITY :
			case UPDATE_OTHER_ACTIVITY :
			case DELETE_OTHER_ACTIVITY :
			case UPDATE_REVIEW_TEST :
			case PL_DEPENDENCY_DELETE :
			case PL_DEPENDENCY_ADD :
			case PL_DEPENDENCY_UPDATE :
				String s = (String) session.getAttribute("caller");
				if (s != null) {
					page = (Integer.parseInt(s) == SCHEDULE_CALLER) ? "Schedule" : "Project plan";
					right = mana;
				}
				break;
				// Schedule
			case SCHE_MAJOR_INFOMATION_GET_LIST :
			case SCHE_SUBCONTRACT_GET_LIST :
			case SCHE_STAGE_GET_LIST :
			case SCHE_REVIEW_TEST_GET_LIST :
			case SCHE_REVIEW_TEST_VIEW :
			case SCHE_OTHER_QUALITY_GET_LIST :
			case SCHE_TRAINING_PLAN_GET_LIST :
			case SCHE_CRITICAL_DEPENDENCIES_GET_LIST :
			case SCHE_FINANCIAL_PLAN_GET_LIST :
				page = "Schedule";
				right = view;
				break;
			case SCHE_REVIEW_TEST_UPDATE : //after module add/update
			case SCHE_REVIEW_TEST_UPDATE2 :
			case SCHE_REVIEW_TEST_UPDATE3 : // after module batch add/update
			case SCHE_REVIEW_TEST_UPDATE4 :
			case UPDATE_SCHEDULE_HEADER :
			case ADDNEW_SUBCONTRACT :
			case UPDATE_SUBCONTRACT :
			case DELETE_SUBCONTRACT :
			case ADDNEW_STAGE :
			case UPDATE_STAGE :
			case DELETE_STAGE :
				page = "Schedule";
				right = mana;
				break;
				//Effort
			case UPDATE_STAGE_EFFORT :
			case UPDATE_REVIEW_EFFORT :
			case UPDATE_QLT_ACTIVITY_EFFORT :
			case UPDATE_WEEKLY_EFFORT :
            case EFF_UPDATE_BATCH_PLAN:
				page = "Effort";
				right = mana;
				break;
			case EFF_INFORMATION_GET_LIST :
			case EFF_STAGE_GET_LIST :
			case EFF_REVIEW_GET_LIST :
			case EFF_QUALITY_ACTIVITY_GET_LIST :
			case EFF_WEEKLY_GET_LIST :
            case EFF_GET_BATCH_PLAN:
            case BATCH_UPDATE_PROCESS_EFFORT:
				page = "Effort";
				right = view;
				break;
				//Further work				
			case TAILORING_GET_LIST :			
			case TAILORING_NEXT :
			case TAILORING_PREV :
				page = "Tailoring Deviation";
				right = view;
				break;
			case APPLY_PPM_FEATURE:	
			case TAILORING_ADD :
			case DEVIATION_ADD :
			case TAILORING_ADD_PREPARE :
			case DEVIATION_ADD_PREPARE :
			case DEVIATION_UPDATE_PRE:
			case DEVIATION_UPDATE:
			case DEVIATION_DELETE:
			case TAILORING_UPDATE :
			case TAILORING_UPDATE_PRE :
			case TAILORING_DELETE :
			case TAILORING_MNG :
				page = "Tailoring Deviation";
				right = mana;
				break;
			case WO_DELIVERABLE_GET_LIST :
			case WO_PERFORMANCE_GET_LIST :
			case WO_GENERAL_INFO_GET_LIST :
			case WO_TEAM_GET_LIST :
			case WO_CHANGE_GET_LIST :
			case WO_ACCEPTANCE_GET_LIST :
			case WO_SIG_GET_LIST :
			case WO_SIG_APPROVE :
			case WO_CHANGE_SIG_APPROVE :
			case WO_INTERNAL_SIG_APPROVE :
			case WO_EXPORT :
			case WO_AC_EXPORT :
			case TEAM_SIZE_PROGRESS:
				page = "Work Order";
				right = view;
				break;
				
			case WO_DELIVERABLE_BATCH_PRE_ADD :
			case WO_DELIVERABLE_BATCH_PRE_UPDATE :
			case WO_DELIVERABLE_BATCH_ADD :
			case WO_DELIVERABLE_BATCH_UPDATE :
			case WO_DELIVERABLE_BATCH_DELETE :
			case WO_DELIVERABLE_ADD_PREP :
			case WO_DELIVERABLE_IMPORT :
			case WO_DELIVERABLE_ADD :
			case WO_DELIVERABLE_UPDATE_PREP :
			case WO_DELIVERABLE_UPDATE :
			case WO_DELIVERABLE_DELETE :
			case WO_STANDARD_METRIC_UPDATE :
			case WO_STANDARD_METRIC :
			case WO_PERFORMANCE_UPDATE :
			case WO_GENERAL_INFO_UPDATE :
			case WO_GENERAL_INFO :
			case WO_REOPEN_PROJECT:
			case WO_CLOSE_CANCEL_PROJECT:
			case WO_CUS_METRIC_ADD :
			case WO_CUS_METRIC_UPDATE :
			case WO_CUS_METRIC_DELETE :			
			case WO_SUB_TEAM_ADD_PREPARE :
			case WO_SUB_TEAM_UPDATE_PREPARE :
			case WO_TEAM_BATCH_UPDATE_PREPARE :
			case WO_TEAM_BATCH_UPDATE :
			case WO_SUB_TEAM_DELETE :
			case WO_SUB_TEAM_ADD :
			case WO_SUB_TEAM_UPDATE :
			case WO_TEAM_ADD_PREPARE :
			case WO_TEAM_ADD :
			case WO_TEAM_IMPORT :
			case WO_TEAM_UPDATE :
			case WO_TEAM_DELETE :
			case WO_TEAM_BATCH_DELETE :
			case WO_TEAM_MNG :
			case WO_CHANGE_ADD_PREPARE :
			case WO_CHANGE_ADD :
			case WO_CHANGE_UPDATE :
			case WO_CHANGE_DELETE :
			case WO_CHANGE_MNG :
			case WO_ACCEPTANCE_UPDATE :
			case WO_SIG_DEL :
			case WO_SIG_ADD :
			case WO_SIG_RESET :
			case WO_CHANGE_SIG_DEL :
			case WO_CHANGE_SIG_ADD :
			case WO_CHANGE_SIG_RESET :
			case WO_INTERNAL_SIG_DEL :
			case WO_INTERNAL_SIG_ADD :
			case WO_INTERNAL_SIG_RESET :
			case WO_CUS_METRIC_AUTO_UPDATE:
			case WO_DEF_METRIC_AUTO_UPDATE:
				page = "Work Order";
				right = mana;
				break;
			case PL_EXPORT :
			case ISSUE_IMPORT :
			case PL_OVERVIEW_GET_PAGE :
			case PL_DELIVERIES_DEPENDENCIES_GET_PAGE :
			case PL_DEPENDENCY_VIEW :
			case PL_LIFECYCLE_GET_PAGE :
			case PL_STRUCTURE_GET_PAGE :
			
			// INTERFACE
			case PL_INTERFACE_VIEW :
			case PL_INTERFACE_FSOFT_ADD_PREPARE :
			case PL_INTERFACE_CUSTOMER_ADD_PREPARE :
			case PL_INTERFACE_OTHER_PROJECT_ADD_PREPARE :
			
			case PL_INTERFACE_FSOFT_UPDATE_PREPARE :
			case PL_INTERFACE_CUSTOMER_UPDATE_PREPARE :
			case PL_INTERFACE_OTHER_PROJECT_UPDATE_PREPARE :
	
			case PL_INTERFACE_FSOFT_ADD :
			case PL_INTERFACE_CUSTOMER_ADD :
			case PL_INTERFACE_OTHER_PROJECT_ADD :
	
			case PL_INTERFACE_FSOFT_UPDATE :
			case PL_INTERFACE_CUSTOMER_UPDATE :
			case PL_INTERFACE_OTHER_PROJECT_UPDATE :
	
			case PL_INTERFACE_FSOFT_DELETE :
			case PL_INTERFACE_CUSTOMER_DELETE :
			case PL_INTERFACE_OTHER_PROJECT_DELETE :
			
			case PL_SUBCONTRACT_VIEW :
			
			// REQUIREMENT CHANGES
			case PL_REQ_CHANGES_MNG_GET_LIST :
			case PL_REQ_CHANGES_MNG_PREPARE_ADD :
			case PL_REQ_CHANGES_MNG_PREPARE_UPDATE :
			case PL_REQ_CHANGES_MNG_ADD :
			case PL_REQ_CHANGES_MNG_UPDATE :
			case PL_REQ_CHANGES_MNG_DELETE :			
			
			// PRODUCT INTEGRATION STRATEGY
			case PL_INTEGRATION_STRATEGY_GET_LIST :
			case PL_INTEGRATION_STRATEGY_PREPARE_ADD :
			case PL_INTEGRATION_STRATEGY_PREPARE_UPDATE :
			case PL_INTEGRATION_STRATEGY_ADD :
			case PL_INTEGRATION_STRATEGY_UPDATE :
			case PL_INTEGRATION_STRATEGY_DELETE :
			
			// STRATEGY FOR MEETING QUALITY OBJECTIVES	
			case PL_STRATEGY_FOR_MEETING_PREPARE_ADD :
			case PL_STRATEGY_FOR_MEETING_PREPARE_UPDATE :
			case PL_STRATEGY_FOR_MEETING_ADD :
			case PL_STRATEGY_FOR_MEETING_UPDATE :
			case PL_STRATEGY_FOR_MEETING_DELETE :
			
			// REVIEW STRATEGY 	
			case PL_REVIEW_STRATEGY_PREPARE_ADD :
			case PL_REVIEW_STRATEGY_PREPARE_UPDATE :
			case PL_REVIEW_STRATEGY_ADD :
			case PL_REVIEW_STRATEGY_UPDATE :
			case PL_REVIEW_STRATEGY_DELETE :
			case PL_ADD_PRODUCT_FROM_QUALITY :
			
			// TEST STRATEGY
			case PL_TEST_STRATEGY_PREPARE_ADD :
			case PL_TEST_STRATEGY_PREPARE_UPDATE :
			case PL_TEST_STRATEGY_ADD :
			case PL_TEST_STRATEGY_UPDATE :
			case PL_TEST_STRATEGY_DELETE :
			
			// MEASUREMENTS PROGRAM
			case PL_MEASUREMENTS_PROGRAM_GET_LIST :
			case PL_MEASUREMENTS_PROGRAM_PREPARE_ADD :
			case PL_MEASUREMENTS_PROGRAM_PREPARE_UPDATE :
			case PL_MEASUREMENTS_PROGRAM_ADD :
			case PL_MEASUREMENTS_PROGRAM_UPDATE :
			case PL_MEASUREMENTS_PROGRAM_DELETE :
			
			//	PROJECT SCHEDULE
			case PL_PROJECT_SCHEDULE_GET_LIST :
			case PL_PROJECT_SCHEDULE_PREPARE_ADD :
			case PL_PROJECT_SCHEDULE_PREPARE_UPDATE :
			case PL_PROJECT_SCHEDULE_ADD :
			case PL_PROJECT_SCHEDULE_UPDATE :
			case PL_PROJECT_SCHEDULE_DELETE :
			
			// ESTIMATE DEFECT 
			case EST_DEFECT_VIEW :
			case EST_DEFECT_PRE_ADD :
			case EST_DEFECT_PRE_UPDATE :
			case EST_DEFECT_ADD :
			case EST_DEFECT_UPDATE :
			case EST_DEFECT_DELETE :
			
			// ESTIMATE EFFORT
			case EST_EFFORT_VIEW :
			case EST_EFFORT_UPDATE :
			case EST_EFFORT_DELETE :
			case EST_EFFORT_ADD_PRE :
			case EST_EFFORT_ADD :
			
			// FINANCE
			case FINANCE_VIEW :
			case FINANCE_UPDATE :
			case FINANCE_DELETE :
			case FINANCE_ADD_PRE :
			case FINANCE_ADD :
			
			// ORGANIZATION
			case ORGANIZATION_VIEW :
			case ORGANIZATION_UPDATE :
			case ORGANIZATION_DELETE :
			case ORGANIZATION_ADD_PRE :
			case ORGANIZATION_ADD :
			
			// COMMUNICATION & REPORTING
			case COMREPORT_VIEW :
			case COMREPORT_PRE_ADD :
			case COMREPORT_PRE_UPDATE :
			case COMREPORT_ADD :
			case COMREPORT_UPDATE :
			case COMREPORT_DELETE :
			
			//	SECURITY ASPECTS
			case DEFINITIONS_ACRONYMS_VIEW :
			case DEFINITIONS_ACRONYMS_PRE_ADD :
			case DEFINITIONS_ACRONYMS_PRE_UPDATE :
			case DEFINITIONS_ACRONYMS_ADD :
			case DEFINITIONS_ACRONYMS_UPDATE :
			case DEFINITIONS_ACRONYMS_DELETE :
			
			case PL_SIG_GET_LIST :			
			case PL_SIG_APPROVE :
			case PL_CHANGE_SIG_APPROVE :
			case GET_TRAINING_LIST :
			case GET_FINAN : // Please remove this and use the vector containing the finance list
			case GET_TOOL_LIST :
			case GET_TOOL : // Please remove this and use the vector
			case GET_COST :
			case GET_FINAN_LIST :
			case GET_QUALITY_OBJECTIVE_LIST :
			case PL_GET_SCHED_FILE:
				page = "Project plan";
				right = view;
				break;
			case PL_CONSTRAINT_ADD :
			case PL_CONSTRAINT_UPDATE :
			case PL_CONSTRAINT_DELETE :
			case PL_ASSUMPTION_ADD :
			case PL_ASSUMPTION_UPDATE :
			case PL_ASSUMPTION_DELETE :
			case PL_REFERENCE_ADD_PREPARE :
			case PL_REFERENCE_ADD :
			case PL_REFERENCE_UPDATE :
			case PL_REFERENCE_DELETE :
			case PL_REFERENCE_MNG :
			case PL_ITERATION_ADD :
			case PL_ITERATION_ADD_PREPARE :
			case PL_ITERATION_DELETE :
			case PL_ITERATION_UPDATE :
			case PL_ITERATION_UPDATE_PREPARE :
			case PL_ORG_STRUCTURE_UPDATE_PREPARE :
			case PL_ORG_STRUCTURE_UPDATE :
			case PL_INTERFACE_DELETE :
			case PL_INTERFACE_ADD_PREPARE :
			case PL_INTERFACE_ADD :
			case PL_INTERFACE_UPDATE_PREPARE :
			case PL_INTERFACE_UPDATE :
			case PL_SUBCONTRACT_DELETE :
			case PL_SUBCONTRACT_ADD_PREPARE :
			case PL_SUBCONTRACT_ADD :
			case PL_SUBCONTRACT_UPDATE_PREPARE :
			case PL_SUBCONTRACT_UPDATE :
			case PL_SIG_DEL :
			case PL_SIG_ADD :
			case PL_SIG_RESET :
			case PL_CHANGE_SIG_DEL :
			case PL_CHANGE_SIG_ADD :
			case PL_CHANGE_SIG_RESET :
			case ADDNEW_TOOL :
			case UPDATE_TOOL :
			case DELETE_TOOL :
			case PREPARE_ADD_TOOL :
			case ADDNEW_COST :
			case UPDATE_COST :
			case DELETE_COST :
			case UPDATE_QLT_OBJECTIVE :
			case PL_UPLOAD_SCHED :
				page = "Project plan";
				right = mana;
				break;
            case VIEW_CI_REGISTER :
			case GET_CI_REGISTER :
				page = "CI Tracking";
				right = view;
				break;

			case UPDATE_CI_REGISTER :
				page = "CI Tracking";
				right = mana;
				break;
			case GET_USER_PROFILES_LIST :
			case SEARCH_USER_PROFILES :
			case GET_USER_PROFILE :
				page = "User Profiles";
				right = view;
				break;
			case DELETE_USER_PROFILES :
			case UPDATE_USER_PROFILE :
			case ADD_USER_PROFILE :
				page = "User Profiles";
				right = mana;
				break;
			case UPDATE_GROUP_POINT :			
			case GET_ORG_FSOFT_LIST :
			case GET_ORG_POINT_LIST :
			case GET_ORG_POINTBA_LIST :
			case VIEW_GROUP_METRIC :
				page = "Org Point";
				right = view;
				break;
			case UPDATE_GROUP_POINT2 :
			case UPDATE_GROUP_POINT3 :
			case UPDATE_GROUP_METRIC :
			case UPDATE_GROUP_METRIC2 :
			case UPDATE_GROUP_METRIC3 :
				page = "Org Point";
				right = mana;			
				break;
				/************Group Handlers***************/
            /*HuyNH2 add some line code for project archive*/
            case PROJECT_ARCHIVE_LIST:     
            case PROJECT_ARCHIVE_HISTORY:
            case PROJECT_ARCHIVE_HISTORY_DETAIL:
            case PROJECT_ARCHIVE_SUSSCESS_RESULT:
                page = "Project Archive";
                right = view;       
                break;                         
            case PROJECT_ARCHIVE_RESTORE:
            case PROJECT_ARCHIVE_ARCHIVE:
                page = "Project Archive";
                right = mana;
                break;
            /*end of add code*/
            /*Hieunv1 add some lines of code for Group/Humance Resource - Start*/
            case GROUP_CALENDAR_EFFORT:
            case GROUP_RESOURCE_ALLOCATION:
            case GROUP_RESOURCE_ALLOCATION_EXPORT:
            	page = "Human Resource";
            	right = view;
            	break;
            /*Hieunv1 add some lines of code for Group/Humance Resource - End*/
			case PCB_LOADSEARCHPAGE :
			case PCB_VIEW :
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Organization PCB";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Group PCB";
				right = view;
				break;
			case PCB_CREATE :
			case PCB_SELECT_PROJECT :
			case PCB_SAVEINFO :
			case PCB_SAVEDETAILS :
			case PCB_SAVECOMMENTS :
			case PCB_SAVECONCLUSIONS :
			case PCB_DELETE :
			case CUSTOMER_IMPORT :
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Project parameters";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Project parameters";
				right = mana;
				break;
			case PRODUCT_INDEXES :
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Organization product";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Group product";
				right = view;
				break;
			//case LOADNORMSSQA:
			case LOADNORMS :
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Organization norms";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Group norms";
				right = view;
				break;
			case SAVENORMSSQA:
			case SAVENORMS :
			case SET_METRIC :
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Organization norms";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Group norms";
				right = mana;
				break;
            case GET_SQA_DEFECT_ORIGIN:
            case SQA_DEFECT_TYPE:
            case PQA_REPORT:
			case DP_DATABASE:
                page = "Group reports";
                right = view;
                break;
			case LOADPLANNING :
			case PLAN:
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Organization plan";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Group plan";
				right = view;
				break;
			case SAVEPLANNING :
			case PLAN_ADD:
			case PLAN_UPDATE:
			case PLAN_DELETE:
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Organization plan";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Group plan";
				right = mana;
				break;
			case TASK_LIST:
			case GROUPMONITORING :
			
				page = "Group monitoring";
				right = view;
				break;
			case ORGMONITORING :
			case DECISION:

				page = "Organization monitoring";
				right = view;
				break;
			case PROASS_PROJECT_DESC:
			case PROASS_PROJECT_DETAIL:
			case PROASS_TAILORING_DEVIATION:
			case PROASS_RISK:
			case PROASS_PRACTICE_LESSON:
			case PROASS_ISSUE:
				page = "Process Assets";
				right = view;
				break;
			case RISK_BASELINE:
				page = "Process Assets";
				right = mana;
				break;
			case CBOMONITORING :
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Organization monitoring";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Group monitoring";
				right = view;
				break;
			case TASK_DELETE:
			case TASK_UPDATEPREP:
			case TASK_UPDATE:
			case TASK_ADD:
			case TASK_ADDPREP:
			case DECISION_ADD:
			case DECISION_UPDATE:
			case DECISION_DELETE:
				if (level == WorkUnitInfo.TYPE_ORGANIZATION)
					page = "Organization monitoring";
				else if (level == WorkUnitInfo.TYPE_GROUP)
					page = "Group monitoring";
				right = mana;
				break;
				//not managed
			case SPECIAL_REPORT:
			case TASK_DRILLUP:
			case GET_PAGE:
			case HEADER_PRJ :
			case HEADER_GROUP:
			case PROJECT_SEARCH :
			case HEADER_ADM :
			case GET_DMS :
			case GET_TIMESHEET :
			case GET_NCMS :
			case GET_CALLLOG :
			case GET_DASHBOARD :
			case GET_ITS :
			case INDEX_DRILL :
			case GROUPMONITORINGDRILL :
			case LOGIN :
			case LOGOUT :
			case WORKUNIT_HOME :
			case VIEW_USER_PROFILE:
			case USER_CHANGE_PASSWORD:
			case USER_PROFILE_DETAIL:
			case XML_PROJECT_GENERATION:
			case FILLTER_USER:
			case FILLTER_CUSTOMER :
			case FILLTER_USER_ASSIGNED_PROJECT:
				return true;
			case WO_TEAM_EVALUATION_DO_UPDATE:
				page = "Project parameters";
				right = mana;
				break;
			case WO_UPDATE_TEAM_EVALUATION:
				page = "Project parameters";
				right = mana;
				break;
			case DO_UPDATE_TEAM_EVALUATION_POST_MORTEM:
				page = "Project parameters";
				right = mana;
				break;
			case DO_DELETE_IN_TEAM_BATCH_POST_MORTEM:
				page = "Project parameters";
				right = mana;
				break;
			case REPORTS_UPDATE_BATCH_TEAM_EVALUATION:
				page = "Project parameters";
				right = mana;
				break;
			case CUSTOMER_UPDATE:
				page = "Project parameters";
				right = mana;
				break;
			case CUSTOMER_ADD_NEW:
				page = "Project parameters";
				right = mana;
				break;
			case DO_CUSTOMER_UPDATE:
				page = "Project parameters";
				right = mana;
				break;
			case DELIVERY_LIST_SEARCH :
				page = "Project home";
				right = view;
				break;
			case SEARCH_CUSTOMER:
				page = "Project home";
				right = view;
				break;
			case RISK_MENUORG:
				page = "Project parameters";
				right = mana;
				break;
			
		}
		LanguageChoose languageChoose = new LanguageChoose();
		if (right == -1) {
			Fms1Servlet.callPage("error.jsp?error=" + languageChoose.getMessage("fi.jsp.error.YouDoNotHavePermissionToDoThisAction"),request,response);
			return false;
		}
		else {
			int permission = getRights(page, request);
			if (permission < right) {
				Fms1Servlet.callPage("error.jsp?error=" + languageChoose.getMessage("fi.jsp.error.YouDoNotHavePermissionToDoThisAction"),request,response);
				return false;
			}
			return true;
		}
	}
}
