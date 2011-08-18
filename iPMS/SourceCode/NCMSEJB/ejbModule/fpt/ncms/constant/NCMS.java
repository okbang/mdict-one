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
 
 /*
 * @(#)NCMS.java 15-Mar-03
 */


package fpt.ncms.constant;

/**
 * Class NCMS
 * Constants used in program
 * @version 1.0 15-Mar-03
 * @author
 */
public final class NCMS {
    /** flag for login mode */
    public static final int ADMIN_NCMS = 2;
    public static final int ADMIN_CALL = 3;
    public static final int USER_NCMS = 0;
    public static final int USER_CALL = 1;
    public static final String NCMS_SYSTEM="NCMS";
    public static final String CALL_SYSTEM="CALL";
    
    
    /** flag for debug mode */
    public final static boolean DEBUG = true;
    /** for gen SQL*/
    public static final int NUMERIC_FIELD = 1;
    public static final int STRING_FIELD = 2;
    public static final int DATE_FIELD = 3;
    
    /** values for combo type*/
    public static final int CBO_ALL_VALUE = 1;
    public static final int CBO_NONE_VALUE = 2;
    public static final int CBO_BOTH_ALL_AND_NONE_VALUE = 3;
    public static final int CBO_OTHER_VALUE = 4;
    public static final int CBO_BOTH_ALL_AND_GENERAL_VALUE = 5;
    public static final int CBO_EMPTY_VALUE = 6;

    public static final int COMBO_CREATOR = 0;
    public static final int COMBO_ASSIGNEE = 1;
    
    // Other combo items
    public static final String CBO_ITEM_ALL_STRING = "(All)";
    public static final String CBO_ITEM_NONE_STRING = "(None)";
    public static final String CBO_ITEM_GENERAL_STRING = "General";
    public static final String CBO_ITEM_ALL_VALUE = "-2";
    public static final String CBO_ITEM_NONE_VALUE = "-1";
    public static final String CBO_ITEM_GENERAL_VALUE = "0";

    public static final int GROUP_VALUE = 0;
    public static final int NONGROUP_VALUE = 1;

//    public static final String STR_ALL_VALUE = "-2";
//    public static final String STR_ALL_STRING = "(All)";
//    public static final String STR_NONE_VALUE = "-1";
//    public static final String STR_NONE_STRING = "(None)";
//    public static final String STR_GENERAL_VALUE = "0";
//    public static final String STR_GENERAL_STRING = "General";
    /** General value for project & group combo*/
    public static final String GENERAL_VALUE = "General";

    /** flag for combo mode (combo has blank space or not) */
    public final static boolean NO_BLANK = false;
    public final static boolean HAS_BLANK = true;

    /** Listing mode */
    public final static String SYSTEM_MODE = "SystemMode";
    public final static String PERSONAL_MODE = "PersonalMode";

    /** User's role */
    public final static String ROLE_CREATOR = "Creator";
    public final static String ROLE_ASSIGNEE = "Assignee";
    public final static String ROLE_REVIEWER = "Reviewer";
    public final static String ROLE_PQA = "Admin";

    /** View constants for each user class */
    public final static String CREATOR_VIEW = "CreatorView";
    public final static String ASSIGNEE_VIEW = "AssigneeView";
    public final static String REVIEWER_VIEW = "ReviewerView";
    public final static String PQA_VIEW = "PQAView";
    public final static String ADMIN_VIEW = "AdminView";

    /** Flag constants for field enable for each user class and action */

    /** Add new NC */
    public final static String DETAIL_CREATOR_ADD
            = "011110111111101000011110";
    /** Modify creator's own NC */
    public final static String DETAIL_CREATOR_UPDATE
            = "011110111111101000011110";
    /** Analyse NC */
    public final static String DETAIL_REVIEWER_ANALYSED
            = "000000111100000000000000";
    /** Correct NC as assignee */
    public final static String DETAIL_ASSIGNEE_CORRECT
            = "111111111110111111111110";
    /** Review NC */
    public final static String DETAIL_PQA_REVIEW
            = "001000111000000000000000";
    /** View NC only */
    public final static String DETAIL_VIEW_ONLY
            = "111111111111111111111111";
    public final static String DETAIL_REVIEWER_ONLY
            = "111111111110111111111111";

    /** Constants for relation between NC status and user's role */
    /*
    public final static int CREATOR_ADD = 0;
    public final static int REVIEWER_ADD = 1;
    public final static int REVIEWER_ANALYSED = 2;
    public final static int CREATOR_UPDATE = 3;
    public final static int ASSIGNEE_CORRECT = 4;
    public final static int PQA_REVIEW = 5;
    public final static int VIEW_ONLY = 6;
    public final static int PQA_REVIEW_ASSIGNED = 7;
    public final static int ALL_STATUS = 8;
    */
    /** Constants for NC status */
    /*
    public final static String NC_OPENED = "5";
    public final static String NC_CANCELLED = "6";
    public final static String NC_ASSIGNED = "7";
    public final static String NC_PENDING = "8";
    public final static String NC_CLOSED = "9";
    */
    public final static int NC_STATUS_OPENED = 5;
    public final static int NC_STATUS_CANCELLED = 6;
    public final static int NC_STATUS_ASSIGNED = 7;
    public final static int NC_STATUS_PENDING = 8;
    public final static int NC_STATUS_CLOSED = 9;
    // Apply when add new
    public final static int NC_STATUS_NEW = -1;
    // Apply when view list
    public final static int NC_STATUS_ALL = -2;
    
    /** Constants for NC types */
    public final static int NCTYPE_NC = 21;
    public final static int NCTYPE_OB = 22;
    public final static int NCTYPE_CC = 23;
    public final static int NCTYPE_CL = 24;
	public final static int NCTYPE_PB = 26;
    //public final static int NCTYPE_DP = 27;

    public final static String NC_NAME_ALL = "All status";
    public final static String GROUP_ALL = "All groups";

    /** Constants for NC repeated */
    public final static String NC_REPEAT = "1";
    public final static String NC_NONREPEAT = "0";

    /** Constant for NC level */
    public final static String FSOFT_LEVEL = "18";
    public final static String GROUP_LEVEL = "19";
    public final static String PROJECT_LEVEL = "20";

    /** Constants for user's action */
    public final static String LOGIN_ACTION = "NCMSLogin";
    public final static String HOMEPAGE_ACTION = "NCMSHomepage";
//    public final static String GET_RSA_ACTION = "GetRsaAction";

    public final static String CALL_LOG_ADD = "CallLogAdd";
    public final static String CALL_LOG_SAVE_NEW = "CallLogSave";
    public final static String CALL_LOG_UPDATE = "CallLogUpdate";
    public final static String CALL_LOG_SAVE_UPDATE = "CallLogSaveUpdate";
    public final static String CALL_LOG_LIST = "CallLogList";
    public final static String CALL_LOG_DELETE = "CallLogDelete";


    public final static String NC_ADD = "NCAdd";
    public final static String NC_UPDATE = "NCUpdate";
    public final static String NC_LIST = "NCList";
    public final static String NC_EXPORT = "NCExport";
    public final static String NC_REPORT = "NCReport";
    public final static String NC_REPORT_PIVOT = "NCReportPivot";

    public final static String NC_EXPORT_CC = "NCExportCC";
    public final static String NC_EXPORT_NC = "NCExportNC";

    public final static String NC_SAVE_NEW = "NCSave";
    public final static String NC_SAVE_UPDATE = "NCSaveUpdate";
    public final static String NC_HISTORY = "NCHistory";

    public final static String VIEW_LIST = "ViewList";
    public final static String VIEW_ADD = "ViewAdd";
    public final static String VIEW_UPDATE = "ViewUpdate";
    public final static String VIEW_DELETE = "ViewDelete";

    public final static String VIEW_SAVE_NEW = "ViewSave";
    public final static String VIEW_SAVE_UPDATE = "ViewSaveUpdate";

    public final static String CONSTANT_ADD = "ConstantAdd";
    public final static String CONSTANT_LIST = "ConstantList";
    public final static String CONSTANT_UPDATE = "ConstantUpdate";
    public final static String CONSTANT_DELETE = "ConstantDelete";

    public final static String CONSTANT_SAVE_NEW = "ConstantSave";
    public final static String CONSTANT_SAVE_UPDATE = "ConstantSaveUpdate";

    public final static String NC_TYPE_ALL = "-1";
    public final static String NC_TYPE_STRING_ALL = "All types";
    public final static String NC_TOTAL_FSOFT = "FSOFT";

    /** Constants for JSP names */
    public final static String JSP_CALL_LOG_LIST = "CallList.jsp";
    public final static String JSP_CALL_LOG_LIST_PERSONAL = "CallListPersonal.jsp";
    public final static String JSP_CALL_LOG_DETAIL = "CallDetail.jsp";
    public final static String JSP_CALL_LOG_UPDATE = "CallUpdate.jsp";
    public final static String JSP_CALL_LOG_EXPORT = "CallExport.jsp";
    public final static String JSP_CALL_LOG_REPORT = "CallReport.jsp";
    public final static String JSP_CALL_LOG_REPORT_PIVOT = "CallReportPivot.jsp";
    public final static String JSP_CALL_LOG_HISTORY = "CallHistory.jsp";

    public final static String JSP_ERROR = "error.jsp";
    public final static String JSP_LOGIN = "Login.jsp";
	public final static String JSP_THE_CHARACTER_AT_THE_END_OF_STRING_IS_INVALID = "The character at the end of string is invalid";
	public final static String JSP_PLEASE_ENTER_USERNAME = "Please enter username !";
	public final static String JSP_PLEASE_ENTER_PASSWORD = "Please enter password !";
	public final static String JSP_HOMEPAGE = "index.jsp";
    public final static String JSP_NC_LIST = "NCList.jsp";
    public final static String JSP_NC_LIST_PERSONAL = "NCListPersonal.jsp";
    public final static String JSP_NC_DETAIL = "NCDetail.jsp";
    public final static String JSP_NC_UPDATE = "NCUpdate.jsp";
    public final static String JSP_NC_EXPORT = "NCExport.jsp";
    public final static String JSP_NC_REPORT = "NCReport.jsp";
    public final static String JSP_NC_REPORT_PIVOT = "NCReportPivot.jsp";
    public final static String JSP_VIEW_LIST = "ViewList.jsp";
    public final static String JSP_VIEW_DETAIL = "ViewDetail.jsp";
    public final static String JSP_VIEW_UPDATE = "ViewUpdate.jsp";
    public final static String JSP_CONSTANT_LIST = "ConstantList.jsp";
    public final static String JSP_CONSTANT_DETAIL = "ConstantDetail.jsp";
    public final static String JSP_CONSTANT_UPDATE = "ConstantUpdate.jsp";
    public final static String JSP_EXPORT_CC = "cc.jsp";
    public final static String JSP_EXPORT_NC = "nc.jsp";
    public final static String JSP_NC_HISTORY = "NCHistory.jsp";

    /** Constant for date format used in NCMS */
    public static final String DATE_FORMAT = "dd-MMM-yy";
    public static final String DATE_FORMAT_EXT = "dd-MMM-yy HH:mm";
    public static final String DATE_FORMAT_FULL = "dd-MMM-yyyy HH:mm:ss";
    public static final String SQL_DATE_FORMAT = "DD-Mon-YY";
    public static final String SQL_DATE_FORMAT_EXT = "DD-Mon-YY HH24:MI";
    public static final String SQL_DATE_FORMAT_FULL = "DD-Mon-YYYY HH24:MI:SS";

    /** Constant for number of NC on one screen */
    public static final int NUM_PER_PAGE = 25;

    /** Constant for number of constants on one screen */
    public static final int NUM_CONST_PER_PAGE = 10;

    /** Message */
    public final static String MSG_INVALID_USER
            = "You have no permission to access this system.";
    public final static String MSG_INVALID_LOCATION
            = "You have no permission to access this modul.";
    public final static String MSG_INVALID_SESSION = "Invalid session.";
    public final static String MSG_TIME_OUT
            = "Session time-out, please login to system again.";
    public final static String MSG_CONSTANT_EXISTS
            = "Some of added constants already exists.";
    public final static String MSG_CONSTANT_LINKED
            = "Constant is linked to some NC.";
    public final static String MSG_VALUE_EXISTS
            = "Some of updated values already exists.";
    public final static String MSG_VIEW_TITLE_EXISTS
            = "View title already exists.";
    public final static String MSG_NC_TITLE_EXISTS
            = "Code already exists.";

    public final static String MSG_YES = "Yes";
    public final static String MSG_NO = "No";
    
	public static final int PROJECT_STATUS_ALL = -1;
	public static final int PROJECT_STATUS_ONGOING = 0;
	public static final int PROJECT_STATUS_CLOSED = 1;
	public static final int PROJECT_STATUS_CANCELLED = 2;
	public static final int PROJECT_STATUS_TENTATIVE = 3;
	
	public static final String PROJECT_STATUS_ALL_STR 		= "All status";
	public static final String PROJECT_STATUS_ONGOING_STR 	= "On-going";
	public static final String PROJECT_STATUS_CLOSED_STR 	= "Closed";
	public static final String PROJECT_STATUS_CANCELLED_STR = "Cancelled";
	public static final String PROJECT_STATUS_TENTATIVE_STR = "Tentative";
}