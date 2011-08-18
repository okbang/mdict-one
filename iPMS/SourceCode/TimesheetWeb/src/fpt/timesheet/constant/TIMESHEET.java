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
 
 package fpt.timesheet.constant;

public class TIMESHEET {
	
	public final static boolean DEBUG = true;

	public final static String DIRECTORY_DOWNLOAD = "download/";
	public final static int MAX_TIMESHEET_RECORDS = 20000;
	public final static int SIZE_OF_FILE = 1500 * 1024;

	//Located servlets
	public final static String APPROVAL_SERVLET 		= "ApprovalServlet";
	public final static String REPORT_SERVLET 			= "ReportServlet";

	//Action Names
	public final static String APPROVAL_ACTION 			= "AA";
	public final static String REPORT_ACTION 			= "RA";

	public final static String HOMEPAGE_ACTION 			= "TimesheetHomepage";
	public final static String LOGIN_ACTION 			= "TimesheetLogin";
//	  public final static String GET_RSA_ACTION 			= "GetRsaAction";

	//Timesheet List form
	public final static String TS_LIST 					= "ListTimesheet";
	public final static String TS_ADD 					= "AddTimesheet";
	public final static String TS_DELETE 				= "DeleteTimesheet";
	public final static String TS_UPDATE 				= "UpdateTimesheet";
	public final static String TS_IMPORT 				= "ImportTimesheet";

	public final static String TS_SAVE_NEW 				= "SaveNewTimesheet";
	public final static String TS_SAVE_UPDATE 			= "SaveUpdateTimesheet";

	//ApprovePL List form
	public final static String PL_LIST 					= "ListPL";
	public final static String PL_APPROVE 				= "ApprovePL";
	public final static String PL_UPDATE 				= "UpdatePL";
	public final static String PL_REJECT 				= "RejectPL";
	public final static String PL_UPDATE_AND_APPROVE 	= "SaveUpdateAndApprovePL";

	//ApproveQA List form
	public final static String QA_LIST 					= "ListQA";
	public final static String QA_APPROVE 				= "ApproveQA";
	public final static String QA_UPDATE 				= "UpdateQA";
	public final static String QA_REJECT 				= "RejectQA";
	public final static String QA_UPDATE_ONLY 			= "SaveUpdateQA";
	public final static String QA_UPDATE_AND_APPROVE 	= "SaveUpdateAndApproveQA";

	//ApproveGL List form
	public final static String GL_LIST 					= "ListGL";
	public final static String GL_APPROVE 				= "ApproveGL";
	public final static String GL_UPDATE 				= "UpdateGL";
	public final static String GL_REJECT 				= "RejectGL";
	public final static String GL_UPDATE_AND_APPROVE 	= "SaveUpdateAndApproveGL";

	//Report form
	public final static String REPORT_INQUIRY 			= "InquiryReport";
	public final static String REPORT_SUMMARY 			= "SummaryReport";
	public final static String REPORT_WEEKLY 			= "WeeklyReport";
	public final static String REPORT_INQUIRY_EXPORT 	= "InquiryExport";

	//HanhTN added -- 08/08/2006		
	//Tracking report
	public final static String REPORT_LACKTS_GROUP 		= "LackTSGroup";
	public final static String REPORT_LACKTS_PROJECT 	= "LackTSProject";
	public final static String REPORT_UNAPPROVED_PM 	= "UnapprovedPM";
	public final static String REPORT_UNAPPROVED_QA 	= "UnapprovedQA";

	//Password
	public final static String PASSWORD_CHANGE 			= "ChangePassword";
	public final static String PASSWORD_SAVE_NEW 		= "SaveNewPassword";

	//Mapping form
	public final static String MAPPING_LIST 			= "MappingList";
	public final static String MAPPING_ADD 				= "MappingAdd";
	public final static String MAPPING_UPDATE			= "MappingUpdate";
	public final static String MAPPING_SAVE 			= "MappingSave";
	public final static String MAPPING_DELETE 			= "MappingDelete";
    
	//EXEMPTION INFO
	public final static String EXEMPTION_LIST			= "ExemptionList";
	public final static String EXEMPTION_LIST_BACK		= "ExemptionListBack";
	public final static String EXEMPTION_ADD			= "ExemptionAdd";
	public final static String EXEMPTION_UPDATE			= "ExemptionUpdate";
	public final static String EXEMPTION_DELETE			= "ExemptionDelete";
	public final static String EXEMPTION_SAVE_ADD		= "ExemptionSaveAdd";
	public final static String EXEMPTION_SAVE_UPDATE	= "ExemptionSaveUpdate";

	//JSP FILE NAMES
	public final static String JSP_ERROR 				= "error.jsp";
	public final static String JSP_LOGIN 				= "Login.jsp";

	public final static String JSP_PL_APPROVE_LIST 		= "PLList.jsp";
	public final static String JSP_QA_APPROVE_LIST 		= "QAList.jsp";
	public final static String JSP_GL_APPROVE_LIST 		= "GLList.jsp";

	public final static String JSP_EXEMPTION_LIST 		= "ExemptionList.jsp";
	public final static String JSP_EXEMPTION_ADD 		= "ExemptionAdd.jsp";
	public final static String JSP_EXEMPTION_UPDATE 	= "ExemptionUpdate.jsp";
	
	public final static String JSP_TIMESHEET_LIST 		= "TimesheetList.jsp";
	public final static String JSP_TIMESHEET_ADD 		= "TimesheetAdd.jsp";
	public final static String JSP_TIMESHEET_UPDATE 	= "TimesheetUpdate.jsp";
	public final static String JSP_TIMESHEET_IMPORT 	= "TimesheetImport.jsp";

	public final static String JSP_PL_APPROVE_UPDATE 	= "PLUpdate.jsp";
	public final static String JSP_QA_APPROVE_UPDATE 	= "QAUpdate.jsp";
	public final static String JSP_GL_APPROVE_UPDATE 	= "GLUpdate.jsp";

	public final static String JSP_REPORT_INQUIRY 		= "ReportInquiry.jsp";
	public final static String JSP_REPORT_SUMMARY 		= "ReportSummary.jsp";
	public final static String JSP_REPORT_WEEKLY 		= "ReportWeekly.jsp";

	//HanhTN added -- 08/08/2006
	//Tracking report
	public final static String JSP_REPORT_LACKTS_GROUP 	 = "TrackingTSGroup.jsp";
	public final static String JSP_REPORT_LACKTS_PROJECT = "TrackingTSProject.jsp";
	public final static String JSP_REPORT_UNAPPROVED_PM  = "UnreviewedTSPM.jsp";
	public final static String JSP_REPORT_UNAPPROVED_QA  = "UnreviewedTSQA.jsp";
	
	public final static String JSP_REPORT_INQUIRY_EXPORT = "ReportInquiryExport.jsp";
	public final static String JSP_CHANGE_PASSWORD 		 = "ChangePassword.jsp";
	public final static String JSP_MAPPING_LIST 		 = "MappingList.jsp";
	public final static String JSP_MAPPING_DETAIL 		 = "MappingDetail.jsp";

	//Client Messages
	public final static String MSG_INVALID_USER 		= "You have no permission to login Timesheet.";
	public final static String MSG_INVALID_LOCATION 	= "Invalid location.";
	public final static String MSG_INVALID_SESSION 		= "Invalid session.";
	public final static String MSG_INVALID_USERPASS 	= "You have no permission to login the system.";
    
	public static final String DATE_FORMAT 				= "MM/dd/yy";
	public static final String QA_SAVE_JS 				= "SaveJavaScript";
	public static final String PR_WP_JS_NAME 			= "pr_wp_map.js";

	//Import Timesheet
	public final static String IMPORT_SHEET 			= "Import";
	public final static int MAX_ROW_IMPORT 				= 50;
	public final static int MAX_COL_IMPORT 				= 8;
	public final static int TS_START_YEAR 				= 2002;
	
	// Migration
	public final static String INSERT 					= "INSERT";
	public final static String UPDATE 					= "UPDATE";
	public final static String DELETE 					= "DELETE";
	public final static String DELETE1 					= "DELETE1";
}