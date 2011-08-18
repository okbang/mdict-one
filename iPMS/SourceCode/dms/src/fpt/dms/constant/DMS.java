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
 
 package fpt.dms.constant;

public class DMS
{
    public final static boolean DEBUG = true;
    //Located servlets////////////////////////////////////////////////////
    public final static String PROJECT_ENVIRONMENT_SERVLET = "ProjectEnvironmentServlet";
    public final static String DEFECT_MANAGEMENT_SERVLET = "DefectManagementServlet";
    public final static String SETUP_ENVIRONMENT_SERVLET = "SetupEnvironmentServlet";

    //Action Names////////////////////////////////////////////////////
    public final static String PROJECT_ENVIRONMENT_ACTION = "PE";
    public final static String DEFECT_MANAGEMENT_ACTION = "DM";
    public final static String SETUP_ENVIRONMENT_ACTION = "SE";

    public final static String HOMEPAGE_ACTION = "DMSHomePage";
    public final static String VIEW_DEFECT_LISTING_ACTION = "ViewDefectListing";
    public final static String SELECT_PROJECT_ACTION = "SelectProjectAction";
    public final static String GET_RSA_ACTION = "GetRsaAction";
    //Sub-action Names////////////////////////////////////////////////////
    //ViewDefectListing form
    public final static String VIEW_ALL_DEFECTS = "ViewAllDefects";
    public final static String VIEW_ALL_OPEN_DEFECTS = "ViewAllOpenDefects";
    public final static String VIEW_ALL_LEAKAGE_DEFECTS = "ViewAllLeakageDefects";

    public final static String QUERY_ADD = "AddQuery";
    public final static String QUERY_DELETE = "DeleteQuery";
    public final static String QUERY_SAVENEW = "SaveNewQuery";
    public final static String QUERY_LISTING = "QueryListing";
    public final static String QUERY_USER_LISTING = "UserQueryListing";

    //All Defect List form
    public final static String DEFECT_SEARCH = "SearchDefect";
    public final static String DEFECT_ADD = "AddDefect";
    public final static String DEFECT_DELETE = "DeleteDefect";
    public final static String DEFECT_UPDATE = "UpdateDefect";
    public final static String DEFECT_BATCH_UPDATE = "BatchUpdateDefect";
    public final static String DEFECT_EXPORT = "ExportDefect";
    public final static String DEFECT_MOVE = "MoveDefect";
    public final static String DEFECT_IMPORT = "ImportDefect";
    public final static String DEFECT_IMPORT_SUBMIT = "SubmitImportDefect";

    //AddNew Defect form
    public final static String DEFECT_SAVE_NEW = "SaveNewDefect";
    //Update Defect form
    public final static String DEFECT_SAVE_UPDATE = "SaveUpdateDefect";
    public final static String DEFECT_NEXT = "Next";
    public final static String DEFECT_PREV = "Prev";
    public final static String DEFECT_HISTORY = "DefectHistory";
    //Batch Update Defect form
    public final static String DEFECT_SAVE_BATCH_UPDATE = "SaveBatchUpdateDefect";
    
    // Get attach form (for Add new defect and Update defect) 
    public final static String DEFECT_ATTACH_NEW_FORM = "AttachNewForm";
    public final static String DEFECT_ATTACH_UPDATE_FORM = "AttachUpdateForm";
    // Attach file
    public final static String DEFECT_ATTACH_NEW = "AttachNew";
    public final static String DEFECT_ATTACH_UPDATE = "AttachUpdate";
    // Cancel attach file
    public final static String DEFECT_ATTACH_NEW_CANCEL = "AttachNewCancel";
    public final static String DEFECT_ATTACH_UPDATE_CANCEL = "AttachUpdateCancel";
    // Attach done
    public final static String DEFECT_ATTACH_NEW_DONE = "AttachNewDone";
    public final static String DEFECT_ATTACH_UPDATE_DONE = "AttachUpdateDone";
    // Access to temporary attach file
    public final static String DEFECT_ATTACH_FILE_VIEW = "AttachFileView";
    // Remove attach file
    public final static String DEFECT_ATTACH_FILE_REMOVE_NEW = "AttachFileRemoveAdd";
    public final static String DEFECT_ATTACH_FILE_REMOVE_UPDATE = "AttachFileRemoveUpdate";
    public final static String DEFECT_ATTACH_FILE_REMOVE_UPLOAD = "AttachFileRemoveUpload";
    // Access to attach (stored in database)
    public final static String DEFECT_ATTACH_DB_VIEW = "AttachDbView";
    // Remove attach file (stored in database)
    //public final static String DEFECT_ATTACH_DB_REMOVE_NEW = "AttachDbRemoveAdd";
    public final static String DEFECT_ATTACH_DB_REMOVE_UPDATE = "AttachDbRemoveUpdate";
    public final static String DEFECT_ATTACH_DB_REMOVE_UPLOAD = "AttachDbRemoveUpload";

    //Menu items
    public final static String REPORT_WEEKLY = "ReportWeekly";
    public final static String ASSIGN_LIST = "AssignList";
    public final static String WP_LIST = "WorkProductList";
    public final static String MODULE_LIST = "ModuleList";
    public final static String DEFECT_PLAN_LIST = "ManagePlannedDefect";

    //Assign User List form
    public final static String ASSIGN_ADD = "AddAssignedUser";
    public final static String ASSIGN_UPDATE = "UpdateAssignedUser";
    public final static String ASSIGN_DELETE = "DeleteAssignedUser";

    public final static String ASSIGN_SAVE_UPDATE = "SaveUpdateAssignedUser";

    //Work Product List form
    public final static String WP_ADD = "AddWorkProduct";
    public final static String WP_UPDATE = "UpdateWorkProduct";
    public final static String WP_DELETE = "DeleteWorkProduct";

    public final static String WP_SAVE_UPDATE = "SaveUpdateWorkProduct";

    //Module List form
    public final static String MODULE_ADD = "AddModule";
    public final static String MODULE_UPDATE = "UpdateModule";
    public final static String MODULE_DELETE = "DeleteModule";

    public final static String MODULE_SAVE_UPDATE = "SaveUpdateModule";

    //Manage Planned Defect form
    public final static String DEFECT_PLAN_ADD = "AddDefectPlan";
    public final static String DEFECT_PLAN_DELETE = "DeleteDefectPlan";

    //Setup Environment form
    public final static String SETUP_ENVIRONMENT = "SetupEnvironment";

    public final static String DEFECT_TYPE_LIST = "DefectTypeList";
    public final static String GROUP_LIST = "GroupList";
    public final static String KPA_LIST = "KPAList";
    public final static String PRIORITY_LIST = "PriorityList";
    public final static String PROCESS_LIST = "ProcessList";
    public final static String PROJECT_STAGE_LIST = "ProjectStageList";
    public final static String QC_ACTIVITY_LIST = "QCActivityList";
    public final static String SEVERITY_LIST = "SeverityList";
    public final static String STATUS_LIST = "StatusList";
    public final static String TYPE_OF_WORK_LIST = "TypeOfWorkList";
    public final static String WORK_PRODUCT_LIST = "WorkProductList";

    //Defect Type form
    public final static String DEFECT_TYPE_ADD = "AddDefectType";
    public final static String DEFECT_TYPE_DELETE = "DeleteDefectType";
    public final static String DEFECT_TYPE_UPDATE = "UpdateDefectType";
    public final static String DEFECT_TYPE_SAVE_UPDATE = "SaveUpdateDefectType";

    //Group form
    public final static String GROUP_ADD = "AddGroup";
    public final static String GROUP_DELETE = "DeleteGroup";
    public final static String GROUP_UPDATE = "UpdateGroup";
    public final static String GROUP_SAVE_UPDATE = "SaveUpdateGroup";

    //KPA form
    public final static String KPA_ADD = "AddKPA";
    public final static String KPA_DELETE = "DeleteKPA";
    public final static String KPA_UPDATE = "UpdateKPA";
    public final static String KPA_SAVE_UPDATE = "SaveUpdateKPA";

    //Priority form
    public final static String PRIORITY_ADD = "AddPriority";
    public final static String PRIORITY_DELETE = "DeletePriority";
    public final static String PRIORITY_UPDATE = "UpdatePriority";
    public final static String PRIORITY_SAVE_UPDATE = "SaveUpdatePriority";

    //Process form
    public final static String PROCESS_ADD = "AddProcess";
    public final static String PROCESS_DELETE = "DeleteProcess";
    public final static String PROCESS_UPDATE = "UpdateProcess";
    public final static String PROCESS_SAVE_UPDATE = "SaveUpdateProcess";

    //ProjectStage form
    public final static String PROJECT_STAGE_ADD = "AddProjectStage";
    public final static String PROJECT_STAGE_DELETE = "DeleteProjectStage";
    public final static String PROJECT_STAGE_UPDATE = "UpdateProjectStage";
    public final static String PROJECT_STAGE_SAVE_UPDATE = "SaveUpdateProjectStage";

    //QC_Activity form
    public final static String QC_ACTIVITY_ADD = "AddQCActivity";
    public final static String QC_ACTIVITY_DELETE = "DeleteQCActivity";
    public final static String QC_ACTIVITY_UPDATE = "UpdateQCActivity";
    public final static String QC_ACTIVITY_SAVE_UPDATE = "SaveUpdateQCActivity";

    //Severity form
    public final static String SEVERITY_ADD = "AddSeverity";
    public final static String SEVERITY_DELETE = "DeleteSeverity";
    public final static String SEVERITY_UPDATE = "UpdateSeverity";
    public final static String SEVERITY_SAVE_UPDATE = "SaveUpdateSeverity";

    //Status form
    public final static String STATUS_ADD = "AddStatus";
    public final static String STATUS_DELETE = "DeleteStatus";
    public final static String STATUS_UPDATE = "UpdateStatus";
    public final static String STATUS_SAVE_UPDATE = "SaveUpdateStatus";

    //TypeOfWork form
    public final static String TYPE_OF_WORK_ADD = "AddTypeOfWork";
    public final static String TYPE_OF_WORK_DELETE = "DeleteTypeOfWork";
    public final static String TYPE_OF_WORK_UPDATE = "UpdateTypeOfWork";
    public final static String TYPE_OF_WORK_SAVE_UPDATE = "SaveUpdateTypeOfWork";

    //WorkProduct form
    public final static String WORK_PRODUCT_ADD = "AddWorkProduct";
    public final static String WORK_PRODUCT_DELETE = "DeleteWorkProduct";
    public final static String WORK_PRODUCT_UPDATE = "UpdateWorkProduct";
    public final static String WORK_PRODUCT_SAVE_UPDATE = "SaveUpdateWorkProduct";

    //JSP FILE NAMES////////////////////////////////////////////////////
    public final static String JSP_ERROR = "error.jsp";
    public final static String JSP_LOGIN = "LoginForm.jsp";
    public final static String JSP_NOPERMISSION = "noPermission.jsp";
    public final static String JSP_REMOVETOFI = "removeToFI.jsp";
    

    public final static String JSP_QUERY_LISTING = "QueryListingForm.jsp";
    public final static String JSP_QUERY_ADD = "QueryAddForm.jsp";

    public final static String JSP_ALL_DEFECT_LISTING = "DefectListingForm.jsp";
    public final static String JSP_DEFECT_ADD = "DefectAddForm.jsp";
    public final static String JSP_DEFECT_UPDATE = "DefectUpdateForm.jsp";
    public final static String JSP_DEFECT_BATCH_UPDATE = "DefectBatchUpdateForm.jsp";
    public final static String JSP_DEFECT_HISTORY = "DefectHistoryForm.jsp";
    public final static String JSP_DEFECT_EXPORT = "DefectExportForm.jsp";
    public final static String JSP_DEFECT_USER_LISTING = "DefectUserListingForm.jsp";
    public final static String JSP_DEFECT_IMPORT = "DefectImportForm.jsp";
    public final static String JSP_DEFECT_ATTACH = "DefectAttachForm.jsp";
    public final static String JSP_DEFECT_ATTACH_RESULT = "DefectAttachResultForm.jsp";

    public final static String JSP_REPORT_WEEKLY = "ReportWeeklyForm.jsp";

    public final static String JSP_ASSIGN_USER_LISTING = "AssignUserListingForm.jsp";
    public final static String JSP_ASSIGN_USER_UPDATE = "AssignUserUpdateForm.jsp";

    public final static String JSP_WP_LISTING = "WorkProductListingForm.jsp";
    public final static String JSP_WP_UPDATE = "WorkProductUpdateForm.jsp";

    public final static String JSP_MODULE_LISTING = "ModuleListingForm.jsp";
    public final static String JSP_MODULE_UPDATE = "ModuleUpdateForm.jsp";

    public final static String JSP_MANAGE_PLANNED_DEFECT = "ManagePlannedDefectForm.jsp";

    public final static String JSP_SETUP_ENVIRONMENT = "SetupEnvironmentForm.jsp";

    //Setup Environment
    public final static String JSP_DEFECT_TYPE_LIST = "SetupEnvironment/DefectTypeList.jsp";
    public final static String JSP_DEFECT_TYPE_UPDATE = "SetupEnvironment/DefectTypeUpdate.jsp";

    public final static String JSP_GROUP_LIST = "SetupEnvironment/GroupList.jsp";
    public final static String JSP_GROUP_UPDATE = "SetupEnvironment/GroupUpdate.jsp";

    public final static String JSP_KPA_LIST = "SetupEnvironment/KPAList.jsp";
    public final static String JSP_KPA_UPDATE = "SetupEnvironment/KPAUpdate.jsp";

    public final static String JSP_PRIORITY_LIST = "SetupEnvironment/PriorityList.jsp";
    public final static String JSP_PRIORITY_UPDATE = "SetupEnvironment/PriorityUpdate.jsp";

    public final static String JSP_PROCESS_LIST = "SetupEnvironment/ProcessList.jsp";
    public final static String JSP_PROCESS_UPDATE = "SetupEnvironment/ProcessUpdate.jsp";

    public final static String JSP_PROJECT_STAGE_LIST = "SetupEnvironment/ProjectStageList.jsp";
    public final static String JSP_PROJECT_STAGE_UPDATE = "SetupEnvironment/ProjectStageUpdate.jsp";

    public final static String JSP_QC_ACTIVITY_LIST = "SetupEnvironment/QCActivityList.jsp";
    public final static String JSP_QC_ACTIVITY_UPDATE = "SetupEnvironment/QCActivityUpdate.jsp";

    public final static String JSP_SEVERITY_LIST = "SetupEnvironment/SeverityList.jsp";
    public final static String JSP_SEVERITY_UPDATE = "SetupEnvironment/SeverityUpdate.jsp";

    public final static String JSP_STATUS_LIST = "SetupEnvironment/StatusList.jsp";
    public final static String JSP_STATUS_UPDATE = "SetupEnvironment/StatusUpdate.jsp";

    public final static String JSP_TYPE_OF_WORK_LIST = "SetupEnvironment/TypeOfWorkList.jsp";
    public final static String JSP_TYPE_OF_WORK_UPDATE = "SetupEnvironment/TypeOfWorkUpdate.jsp";

    public final static String JSP_WORK_PRODUCT_LIST = "SetupEnvironment/WorkProductList.jsp";
    public final static String JSP_WORK_PRODUCT_UPDATE = "SetupEnvironment/WorkProductUpdate.jsp";

    public final static String DIRECTORY_UPLOAD = "\\upload";
    public final static String DIRECTORY_UPLOAD_TEMP = "upload/temp/";
    //Client Messages////////////////////////////////////////////////////

    //Module List form
    public final static String MSG_INVALID_MODULE_PLANNED = "Invalid planned defect of module";
    public final static String MSG_INVALID_MODULE_REPLANNED = "Invalid re-planned defect of module";

    public final static String MSG_INVALID_WP_PLANNED = "Invalid planned defect of work product";
    public final static String MSG_INVALID_WP_REPLANNED = "Invalid re-planned defect of work product";

    public final static String MSG_INVALID_SESSION = "Invalid session";
    public final static String MSG_NO_SYSTEM_PERMISSION = "You have no permission to access the system.";
    public final static String MSG_NO_PROJECT_PERMISSION = "You have no permission to access this project.";

    public final static String MSG_NO_PLANNED_DEFECT_FOR_MODULE = "You have not planned defect of module";
    public final static String MSG_NO_PLANNED_DEFECT_FOR_WP = "You have not planned defect of work product";

    public final static String MSG_INVALID_PLANNED_DEFECT_BY_MODULE = "Number of planned defects by QC activities must be equal or lower than by module";
    public final static String MSG_INVALID_PLANNED_DEFECT_BY_WP = "Number of planned defects by QC activities must be equal or lower than by work product";

    public final static String MSG_PLANNED_DEFECT_IS_UPDATED = "Update planned defect successfully";
    public final static String MSG_REPLANNED_DEFECT_IS_UPDATED = "Update re-planned defect successfully";
    public final static String MSG_OTHER_QC_ACTIVITY_IS_UPDATED = "Update \"others\" QC_Activity successfully";

    public final static String MSG_UNSPECIFIED_ERROR = "Unspecified error";
    public static final String MSG_CLIENT = "ClientMessageResult";

    public static final String MSG_ATTACH_MESSAGE = "UploadMessage";
    public static final String MSG_ATTACH_SPACE_OVER = "<FONT color=red>" +
            "Attach size exceeded" + "</FONT>";
    public static final String MSG_ATTACH_FAILED = "<FONT color=red>" +
            "Attach failed due to error" + "</FONT>";
    public static final String MSG_ATTACH_SUCCESSFUL =
            "Attach file successfully";
    public static final String UPLOAD_NEW_FILES = "UploadNewFiles";
    public static final String UPLOAD_NEW_SIZE = "UploadNewSize";
    
    public final static String SESSION_LISTENER = "myListener";
    //////////////////////////////////////////////////////////////////////////////
    public static final String DATE_FORMAT = "MM/dd/yy";
    public static final String SQL_DATE_FORMAT = "mm/dd/yy";    
    //////////////////////////////////////////////////////////////////////////////
    public static final String ALL_MODULES = "ALL_MODULES";
    public static final String OTHER_QA = "10, 11, 12, 13, 20, 21, 23"; //reference to QC_Activity table
    public static final String ACTIVE = "Active";
    public static final String INACTIVE = "Inactive";

    /** Constants for import */
    public static final String IMPORT_SHEET = "Import";
    public static final String INPUT_SHEET = "Defect log";
    //public static final int MAX_ROW_IMPORT = 20;
	public static final int MAX_ROW_IMPORT = 200;
    public static final int MAX_COL_IMPORT = 21; //Fix to 21
    public static final int INPUT_START_ROW = 91; // row get data
    
    public static final int MAX_ATTACH_SIZE = 1024 * 1024;  // Max attachment size per defect: 1M
    public static final int MAX_IMPORT_SIZE = 1204 * 1024 * 2;// Max import size : 2M
    public static final int MAX_UPLOAD_FILES = 4;               // Max attachment files: 4 files
    public static final int MAX_ATTACH_FILE_SIZE = 1024 * 256;  // Max size of each attachment file: 256K
    public static final int SESSION_TIMEOUT_NORMAL = 30 * 60;   // 30 mins
    public static final int SESSION_TIMEOUT_SHORT = 3 * 60;     // 3 mins
    
    //added by MinhPT
    //20-Nov-03
    /** Constants for project status */
    public static final String PROJECT_STATUS_ONGOING = "On-going";
    public static final String PROJECT_STATUS_CLOSED = "Closed";
    public static final String PROJECT_STATUS_CANCELLED = "Cancelled";
    public static final String PROJECT_STATUS_TENTATIVE = "Tentative";
    public static final String PROJECT_STATUS_ALL = "All status";

    public static final String PROJECT_VALUE_STATUS_ONGOING = "0";
    public static final String PROJECT_VALUE_STATUS_CLOSED = "1";
    public static final String PROJECT_VALUE_STATUS_CANCELLED = "2";
    public static final String PROJECT_VALUE_STATUS_TENTATIVE = "3";
    public static final String PROJECT_VALUE_STATUS_ALL = "-1";
    
    /** this string is used to indicate */
    public final static String LIST_ALL_PROJECT = "___";
    public static final String ERROR  = "1";
    public static final String ASSIGNED = "2";
    public static final String PENDING = "3";
    public static final String TESTED  = "4";
    public static final String ACCEPTED  = "5";
    public static final String CANCELLED  = "6";

	//added by HaiMM
	//Mar-17-2008
	/** Constant for pilot project */
	public static final String JSP_PILOT_PROJECT = "PilotProject.jsp";
	public static final String PILOT_PROJECT = "PilotProject.txt";
}