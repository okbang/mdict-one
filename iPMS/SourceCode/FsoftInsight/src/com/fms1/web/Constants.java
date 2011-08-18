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
/**
 * Only put here the request constants to be used in the servlet as integer
 * note , some constants are hardcoded in js files
 */
public interface Constants {
	public static final int RIGHT_PROJECT = 2;
	public static final int RIGHT_GROUP = 1;
	public static final int RIGHT_ORGANIZATION = 0;
	public static final int RIGHT_ADMIN = 3;
	public static final int ISSUE = 0;
	// HanhTN
	public static final int GET_ORG_POINT_LIST = 2880;
	public static final int GET_ORG_FSOFT_LIST = 2881;
	public static final int GET_ORG_POINTBA_LIST = 2883;
	// HanhTN add UPDATE_GROUP_POINT3 constant
	public static final int UPDATE_GROUP_POINT3 = 1368;
	public static final int UPDATE_GROUP_METRIC3 = 2368;
	// End
	// HuyNH2 add Project archived
	public static final int PROJECT_ARCHIVE_LIST = 115;
	public static final int PROJECT_ARCHIVE_RESTORE = 116;
	public static final int PROJECT_ARCHIVE_ARCHIVE = 117;
	public static final int PROJECT_ARCHIVE_HISTORY = 118;
	public static final int PROJECT_ARCHIVE_HISTORY_DETAIL = 119;
	public static final int PROJECT_ARCHIVE_SUSSCESS_RESULT = 114;
	public static final int PROJECT_ARCHIVE_MAX_NUMBER_ARCHIVE = 20;
	// End 
	public static final int ISSUE_ADDNEW = 10;
	public static final int ISSUE_UPDATE = 20;
	public static final int ISSUE_ADDPREP = 30;
	public static final int ISSUE_DELETE = 40;
	public static final int HEADER_PRJ = 50;
	public static final int HEADER_ADM = 60;
	public static final int HEADER_GROUP = 61;
	public static final int GET_RIGHT_GROUP = 70;
	public static final int GET_RIGHT_GROUP_LIST = 80;
	public static final int ADDNEW_RIGHT_GROUP = 90;
	public static final int UPDATE_RIGHT_GROUP = 100;
	public static final int DELETE_RIGHT_GROUP = 110;
	public static final int GET_PAGE_LIST = 120;
	public static final int TAILORING_GET_LIST = 130;
	public static final int APPLY_PPM_FEATURE = 131;
	public static final int TAILORING_ADD = 140;
	public static final int TAILORING_UPDATE_PRE = 150;
	public static final int DEVIATION_ADD = 160;
	public static final int DEVIATION_UPDATE_PRE = 170;
	public static final int DEVIATION_UPDATE = 180;
	public static final int DEVIATION_DELETE = 190;
	public static final int TAILORING_UPDATE = 200;
	public static final int TAILORING_DELETE = 210;
	public static final int TAILORING_MNG = 220;
	public static final int WO_DELIVERABLE_GET_LIST = 230;
	public static final int WO_DELIVERABLE_BATCH_PRE_ADD = 231;
	public static final int WO_DELIVERABLE_BATCH_PRE_UPDATE = 232;
	public static final int WO_DELIVERABLE_BATCH_ADD = 233;
	public static final int WO_DELIVERABLE_BATCH_UPDATE = 234;
	public static final int WO_DELIVERABLE_BATCH_DELETE = 235;
	
	public static final int WO_DELIVERABLE_ADD = 240;
	public static final int WO_DELIVERABLE_UPDATE = 250;
	public static final int WO_DELIVERABLE_DELETE = 260;
	public static final int WO_DELIVERABLE_ADD_PREP = 270;
	public static final int WO_DELIVERABLE_UPDATE_PREP = 280;
	public static final int WO_DELIVERABLE_IMPORT = 281;
	public static final int WO_CUS_METRIC_ADD = 290;
	public static final int WO_CUS_METRIC_UPDATE = 300;
	public static final int WO_CUS_METRIC_DELETE = 310;
	public static final int WO_CUS_METRIC_AUTO_UPDATE=301;
	public static final int WO_DEF_METRIC_AUTO_UPDATE=302;
	public static final int WO_STANDARD_METRIC_UPDATE = 320;
	public static final int WO_STANDARD_METRIC = 321;
	public static final int WO_PERFORMANCE_GET_LIST = 330;
	public static final int WO_PERFORMANCE_UPDATE = 340;
	public static final int WO_GENERAL_INFO_GET_LIST = 350;
	public static final int WO_GENERAL_INFO_UPDATE = 360;
	public static final int WO_GENERAL_INFO = 361;
	public static final int WO_TEAM_GET_LIST = 370;
	public static final int WO_TEAM_ADD = 380;
	public static final int WO_TEAM_IMPORT = 381;
	public static final int WO_TEAM_UPDATE = 390;
	public static final int WO_TEAM_DELETE = 400;	
	public static final int WO_TEAM_BATCH_DELETE = 401;
	public static final int WO_TEAM_MNG = 410;
	public static final int WO_TEAM_ADD_PREPARE = 420;	
	
	// landd add start	
	public static final int WO_SUB_TEAM_ADD_PREPARE = 421;
	public static final int WO_SUB_TEAM_UPDATE_PREPARE = 422;
	public static final int WO_SUB_TEAM_DELETE = 423;
	public static final int WO_SUB_TEAM_ADD = 424;
	public static final int WO_SUB_TEAM_UPDATE = 425;
	public static final int WO_TEAM_BATCH_UPDATE = 426;
	public static final int WO_TEAM_BATCH_UPDATE_PREPARE = 427;
	// landd add end
	
	public static final int WO_CHANGE_GET_LIST = 430;
	public static final int WO_CHANGE_ADD = 440;
	public static final int WO_CHANGE_UPDATE = 450;
	public static final int WO_CHANGE_DELETE = 460;
	public static final int WO_CHANGE_MNG = 470;
	public static final int WO_CHANGE_ADD_PREPARE = 480;
	public static final int WO_SIG_GET_LIST = 490;
	public static final int WO_SIG_APPROVE = 500;
	public static final int WO_SIG_DEL = 510;
	public static final int WO_SIG_ADD = 520;
	public static final int WO_SIG_RESET = 530;
	public static final int WO_CHANGE_SIG_APPROVE = 540;
	public static final int WO_CHANGE_SIG_DEL = 550;
	public static final int WO_CHANGE_SIG_ADD = 560;
	public static final int WO_CHANGE_SIG_RESET = 570;
	public static final int WO_INTERNAL_SIG_APPROVE = 580;
	public static final int WO_INTERNAL_SIG_DEL = 590;
	public static final int WO_INTERNAL_SIG_ADD = 600;
	public static final int WO_INTERNAL_SIG_RESET = 610;
	public static final int WO_ACCEPTANCE_GET_LIST = 620;
	public static final int WO_ACCEPTANCE_UPDATE = 630;
	public static final int WO_EXPORT = 640;
	public static final int WO_REOPEN_PROJECT = 642;
	public static final int WO_CLOSE_CANCEL_PROJECT = 644;
	public static final int WO_AC_EXPORT = 650;
	public static final int PL_EXPORT = 660;
	public static final int ISSUE_IMPORT = 661;
	public static final int TAILORING_NEXT = 670;
	public static final int TAILORING_PREV = 680;
	public static final int PL_OVERVIEW_GET_PAGE = 690;
	public static final int PL_CONSTRAINT_ADD = 700;
	public static final int PL_CONSTRAINT_UPDATE = 710;
	public static final int PL_CONSTRAINT_DELETE = 720;
	public static final int PL_ASSUMPTION_ADD = 730;
	public static final int PL_ASSUMPTION_UPDATE = 740;
	public static final int PL_ASSUMPTION_DELETE = 750;
	public static final int PL_REFERENCE_ADD = 760;
	public static final int PL_REFERENCE_UPDATE = 770;
	public static final int PL_REFERENCE_DELETE = 780;
	public static final int PL_REFERENCE_MNG = 790;
	public static final int PL_REFERENCE_ADD_PREPARE = 800;
	public static final int PL_DELIVERIES_DEPENDENCIES_GET_PAGE = 810;
	public static final int PL_DEPENDENCY_ADD = 820;
	public static final int PL_DEPENDENCY_UPDATE = 830;
	public static final int PL_DEPENDENCY_DELETE = 840;
	public static final int PL_DEPENDENCY_VIEW = 850;
	public static final int PL_LIFECYCLE_GET_PAGE = 860;
	public static final int PL_ITERATION_ADD = 870;
	public static final int PL_ITERATION_UPDATE = 880;
	public static final int PL_ITERATION_DELETE = 890;
	public static final int PL_ITERATION_UPDATE_PREPARE = 900;
	public static final int PL_ITERATION_ADD_PREPARE = 910;
	public static final int TAILORING_ADD_PREPARE = 920;
	public static final int DEVIATION_ADD_PREPARE = 930;
	public static final int PL_STRUCTURE_GET_PAGE = 940;
	public static final int PL_ORG_STRUCTURE_UPDATE = 950;
	public static final int PL_ORG_STRUCTURE_UPDATE_PREPARE = 960;
	public static final int PL_INTERFACE_ADD = 970;
	public static final int PL_INTERFACE_UPDATE = 980;
	public static final int PL_INTERFACE_DELETE = 990;
	public static final int PL_INTERFACE_UPDATE_PREPARE = 1000;
	public static final int PL_INTERFACE_ADD_PREPARE = 1010;
	
	public static final int PL_INTERFACE_FSOFT_ADD_PREPARE = 1011;
	public static final int PL_INTERFACE_CUSTOMER_ADD_PREPARE = 1012;
	public static final int PL_INTERFACE_OTHER_PROJECT_ADD_PREPARE = 1013;
	
	public static final int PL_INTERFACE_FSOFT_UPDATE_PREPARE = 1014;
	public static final int PL_INTERFACE_CUSTOMER_UPDATE_PREPARE = 1015;
	public static final int PL_INTERFACE_OTHER_PROJECT_UPDATE_PREPARE = 1016;
	
	public static final int PL_INTERFACE_FSOFT_ADD = 1017;
	public static final int PL_INTERFACE_CUSTOMER_ADD = 1018;
	public static final int PL_INTERFACE_OTHER_PROJECT_ADD = 1019;
	
	public static final int PL_INTERFACE_FSOFT_UPDATE = 1020;
	public static final int PL_INTERFACE_CUSTOMER_UPDATE = 1021;
	public static final int PL_INTERFACE_OTHER_PROJECT_UPDATE = 1022;
	
	public static final int PL_INTERFACE_FSOFT_DELETE = 1023;
	public static final int PL_INTERFACE_CUSTOMER_DELETE = 1024;
	public static final int PL_INTERFACE_OTHER_PROJECT_DELETE = 1025;
	
	public static final int PL_INTERFACE_VIEW = 1029;
	public static final int PL_SUBCONTRACT_ADD = 1030;
	public static final int PL_SUBCONTRACT_UPDATE = 1040;
	public static final int PL_SUBCONTRACT_DELETE = 1050;
	public static final int PL_SUBCONTRACT_UPDATE_PREPARE = 1060;
	public static final int PL_SUBCONTRACT_ADD_PREPARE = 1070;
	public static final int PL_SUBCONTRACT_VIEW = 1080;
	public static final int PL_SIG_GET_LIST = 1090;	 
	
	// REQUIREMENT CHANGES
	public static final int PL_REQ_CHANGES_MNG_GET_LIST = 1091;
	public static final int PL_REQ_CHANGES_MNG_PREPARE_ADD = 1092;
	public static final int PL_REQ_CHANGES_MNG_PREPARE_UPDATE = 1093;
	public static final int PL_REQ_CHANGES_MNG_ADD = 1094;
	public static final int PL_REQ_CHANGES_MNG_UPDATE = 1095;
	public static final int PL_REQ_CHANGES_MNG_DELETE = 1096;
	
	// PRODUCT INTEGRATION_STRATEGY
	public static final int PL_INTEGRATION_STRATEGY_GET_LIST = 1101;
	public static final int PL_INTEGRATION_STRATEGY_PREPARE_ADD = 1102;
	public static final int PL_INTEGRATION_STRATEGY_PREPARE_UPDATE = 1103;
	public static final int PL_INTEGRATION_STRATEGY_ADD = 1104;
	public static final int PL_INTEGRATION_STRATEGY_UPDATE = 1105;
	public static final int PL_INTEGRATION_STRATEGY_DELETE = 1106;
	
	// STRATEGY FOR MEETING QUALITY OBJECTIVES	
	public static final int PL_STRATEGY_FOR_MEETING_PREPARE_ADD = 1108;
	public static final int PL_STRATEGY_FOR_MEETING_PREPARE_UPDATE = 1109;
	public static final int PL_STRATEGY_FOR_MEETING_ADD = 1110;
	public static final int PL_STRATEGY_FOR_MEETING_UPDATE = 1111;
	public static final int PL_STRATEGY_FOR_MEETING_DELETE = 1112;
	
	// MEASUREMENTS PROGRAM
	public static final int PL_MEASUREMENTS_PROGRAM_GET_LIST = 1113;
	public static final int PL_MEASUREMENTS_PROGRAM_PREPARE_ADD = 1114;
	public static final int PL_MEASUREMENTS_PROGRAM_PREPARE_UPDATE = 1115;
	public static final int PL_MEASUREMENTS_PROGRAM_ADD = 1116;
	public static final int PL_MEASUREMENTS_PROGRAM_UPDATE = 1117;
	public static final int PL_MEASUREMENTS_PROGRAM_DELETE = 1118;
	
	//	PROJECT SCHEDULE
	public static final int PL_PROJECT_SCHEDULE_GET_LIST = 1119;
	public static final int PL_PROJECT_SCHEDULE_PREPARE_ADD = 1120;
	public static final int PL_PROJECT_SCHEDULE_PREPARE_UPDATE = 1121;
	public static final int PL_PROJECT_SCHEDULE_ADD = 1122;
	public static final int PL_PROJECT_SCHEDULE_UPDATE = 1123;
	public static final int PL_PROJECT_SCHEDULE_DELETE = 1124;
	
	public static final int PL_SIG_APPROVE = 1127;
	public static final int PL_SIG_DEL = 1128;
	public static final int PL_SIG_ADD = 1129;
	public static final int PL_SIG_RESET = 1130;
	public static final int PL_CHANGE_SIG_APPROVE = 1140;
	public static final int PL_CHANGE_SIG_DEL = 1150;
	public static final int PL_CHANGE_SIG_ADD = 1160;
	public static final int PL_CHANGE_SIG_RESET = 1170;
	public static final int PL_UPLOAD_SCHED = 1171;
	public static final int PL_GET_SCHED_FILE = 1172;
	public static final int NORM_GET_LIST = 1180;
	public static final int MILESTONE_GET_PAGE = 1190;
	public static final int MILESTONE_CALLER = 1200;
	public static final int MILESTONE_OBJECTIVE_UPDATE=1222;
	public static final int MILESTON_UPDATE_SPECIFIED_OBJECTIVE =1191;
	public static final int WORKUNIT_HOME = 1210;
	
	// REVIEW STRATEGY 	
	public static final int PL_REVIEW_STRATEGY_PREPARE_ADD = 1211;
	public static final int PL_REVIEW_STRATEGY_PREPARE_UPDATE = 1212;
	public static final int PL_REVIEW_STRATEGY_ADD = 1213;
	public static final int PL_REVIEW_STRATEGY_UPDATE = 1214;
	public static final int PL_REVIEW_STRATEGY_DELETE = 1215;
	public static final int PL_ADD_PRODUCT_FROM_QUALITY = 1216;
	
	//	TEST STRATEGY	
	public static final int PL_TEST_STRATEGY_PREPARE_ADD = 1217;
	public static final int PL_TEST_STRATEGY_PREPARE_UPDATE = 1218;
	public static final int PL_TEST_STRATEGY_ADD = 1219;
	public static final int PL_TEST_STRATEGY_UPDATE = 1220;
	public static final int PL_TEST_STRATEGY_DELETE = 1221;
	
	// Hieunv1 add WorkUnit- Start
	public static final int GET_WORK_UNIT = 1229;
	public static final int GET_WORK_UNIT_LIST = 1230;
	public static final int ADDNEW_WORK_UNIT_ORGANIZATION = 1240;
	public static final int ADDNEW_WORK_UNIT_GROUP = 1242;
	public static final int ADDNEW_WORK_UNIT_PROJECT = 1244;
	public static final int ADDNEW_WORK_UNIT_PROJECT_RELOAD = 1246;
	public static final int UPDATE_WORK_UNIT_ORGANIZATION = 1250;
	public static final int UPDATE_WORK_UNIT_GROUP = 1252;
	public static final int DELETE_WORK_UNIT_ORGANIZATION = 1260;
	public static final int DELETE_WORK_UNIT_GROUP = 1262;
	public static final int DELETE_WORK_UNIT_PROJECT = 1264;
	// Hieunv1 add WorkUnit- End
	public static final int RISK_LIST_INIT = 1310;
	public static final int RISK_LIST_OTHER = 1311;
	public static final int RISK_LIST_OTHER_CALL_BACK = 1312;
	public static final int RISK_UPDATE_PREP = 1320;
	public static final int RISK_DATABASE_UPDATE_PREP = 1321;
	public static final int RISK_UPDATE = 1330;
	public static final int RISK_SOURCE_UPDATE_PREP = 1331;
	public static final int RISK_SOURCE_UPDATE = 1332;
	public static final int RISK_DATABASE_UPDATE = 1333;
	public static final int RISK_DATABASE_ADD_PREPARE = 1334;
	public static final int RISK_DATABASE_ADD = 1335;
	public static final int RISK_DATABASE_REMOVE = 1336;
	public static final int RISK_ADDNEW = 1340;
	public static final int RISK_ADD = 1341;
	public static final int RISK_MIGRATE = 1342;
	public static final int RISK_ADD_PREP = 1351;
	public static final int RISK_ADDNEW_PREP = 1350;
	public static final int RISK_NEXT = 1360;
	public static final int RISK_PREV = 1370;
	public static final int RISK_DELETE = 1380;
	public static final int RISK_MITIGATION_DELETE = 1382;
	public static final int RISK_CONTINGENCY_DELETE = 1383; // Added by HaiMM
	public static final int RISK_IMPORT = 1381;
	public static final int METHOD_ADDNEW = 1390;
	public static final int METHOD_UPDATE = 1400;
	public static final int METHOD_LIST = 1410;
	public static final int METHOD_REMOVE = 1420;
	public static final int CONVERSION_LIST_INIT = 1430;
	public static final int CONVERSION_LIST = 1440;
	public static final int CONVERSION_UPDATE = 1450;
	public static final int CONVERSION_ADDNEW = 1460;
	public static final int CONVERSION_DELETE = 1470;
	public static final int CONVERSION_LIST_NAME = 1480;
	public static final int MODULE_LIST = 1500;
	public static final int MODULE_DETAIL = 1505;
	public static final int MODULE_UPDATE_PREP = 1510;
	public static final int MODULE_UPDATE = 1520;
	public static final int MODULE_ADDNEW_PREP = 1530;
	public static final int MODULE_ADDNEW = 1540;
	public static final int MODULE_IMPORT = 1541;
	public static final int MODULE_DELETE = 1550;
	
	// landd start batch update/delete/add module
	public static final int BATCH_MODULE_PREPARE_ADD = 1551;
	public static final int BATCH_MODULE_PREPARE_UPDATE = 1552;
	public static final int BATCH_MODULE_ADD = 1553;
	public static final int BATCH_MODULE_UPDATE = 1554;
	public static final int BATCH_MODULE_DELETE = 1555;
	// landd end
	
	public static final int DISTR_RATE_INIT = 1560;
	public static final int COMPL_RATE_UPDATE = 1570;
	public static final int REQUIREMENT_LIST_INIT = 1580;
	public static final int REQUIREMENT_GRAPH = 1581;
	public static final int REQUIREMENT_LIST = 1590;
	public static final int REQUIREMENT_PLAN = 1591;
	public static final int REQUIREMENT_PLAN_RCR_BATCH = 1592;
	public static final int REQUIREMENT_PLAN_RCR_BATCH_SAVE = 1593;
	public static final int REQUIREMENT_REPLAN_RCR_BATCH = 1594;
	public static final int REQUIREMENT_UPDATE_PREP = 1600;
	public static final int REQUIREMENT_UPDATE = 1610;
	public static final int REQUIREMENT_UPDATE_PROCESS = 1611;
	public static final int REQUIREMENT_ADDNEW_PREP = 1620;
	public static final int REQUIREMENT_ADDNEW = 1630;
	public static final int REQUIREMENT_IMPORT = 1631;
	public static final int LOAD_CUSTOMER_PAGE = 1632;
	public static final int CUSTOMER_IMPORT = 1633;
	public static final int REQUIREMENT_NEXT = 1640;
	public static final int REQUIREMENT_PREV = 1650;
	public static final int REQUIREMENT_DELETE = 1660;
	public static final int DEFECT_VIEW = 1670;
	public static final int UPDATE_PRODUCT_DEFECT_TRACKING = 1671;
	public static final int WEEKLY_REPORT_VIEW = 1680;
	public static final int WEEKLY_REPORT_VIEW_INIT = 1690;
	public static final int GET_PRACTICE_LIST = 1700;
	public static final int ADDNEW_PRACTICE = 1710;
	public static final int UPDATE_PRACTICE = 1720;
	public static final int DELETE_PRACTICE = 1730;
	public static final int GET_PRACTICE = 1740;
	public static final int GET_TRAINING_LIST = 1750;
	public static final int ADDNEW_TRAINING = 1760;
	public static final int UPDATE_TRAINING = 1770;
	public static final int DELETE_TRAINING = 1780;
	public static final int GET_TOOL_LIST = 1790;
	public static final int PREPARE_ADD_TOOL = 1791;
	public static final int ADDNEW_TOOL = 1800;
	public static final int UPDATE_TOOL = 1810;
	public static final int DELETE_TOOL = 1820;
	public static final int GET_TOOL = 1830;
	public static final int GET_FINAN_LIST = 1840;
	public static final int ADDNEW_FINAN = 1850;
	public static final int UPDATE_FINAN = 1860;
	public static final int DELETE_FINAN = 1870;
	public static final int GET_FINAN = 1880;
	public static final int ADDNEW_COST = 1890;
	public static final int UPDATE_COST = 1900;
	public static final int DELETE_COST = 1910;
	public static final int GET_COST = 1920;
	public static final int GET_QUALITY_OBJECTIVE_LIST = 1930;
	public static final int UPDATE_REVIEW_TEST = 1940;
	public static final int UPDATE_QLT_OBJECTIVE = 1950;
	public static final int ADDNEW_OTHER_ACTIVITY = 1960;
	public static final int UPDATE_OTHER_ACTIVITY = 1970;
	public static final int DELETE_OTHER_ACTIVITY = 1980;
	public static final int UPDATE_SCHEDULE_HEADER = 1990;
	public static final int GET_SUBCONTRACT_LIST = 2000;
	public static final int ADDNEW_SUBCONTRACT = 2010;
	public static final int UPDATE_SUBCONTRACT = 2020;
	public static final int DELETE_SUBCONTRACT = 2030;
	public static final int ADDNEW_STAGE = 2040;
	public static final int UPDATE_STAGE = 2050;
	public static final int DELETE_STAGE = 2060;
	public static final int ADDNEW_OTHER_ACTIVITY_SCH = 2070;
	public static final int UPDATE_OTHER_ACTIVITY_SCH = 2080;
	public static final int DELETE_OTHER_ACTIVITY_SCH = 2090;
	public static final int SCHEDULE_CALLER = 2100;
	public static final int QUALITY_OBJECTIVE_CALLER = 2110;
	public static final int TRAINING_CALLER = 2120;
	public static final int FINAN_CALLER = 2130;
	public static final int PROJECT_PLAN_CALLER = 2140;
	public static final int GET_CI_REGISTER = 2150;
	public static final int VIEW_CI_REGISTER = 2151;
	public static final int UPDATE_CI_REGISTER = 2160;
	public static final int UPDATE_STAGE_EFFORT = 2170;
	public static final int UPDATE_REVIEW_EFFORT = 2180;
	public static final int UPDATE_QLT_ACTIVITY_EFFORT = 2190;
	public static final int UPDATE_WEEKLY_EFFORT = 2200;
	public static final int GET_POST_MORTEM = 2210;
	public static final int ADDNEW_FURTHER_WORK = 2220;
	public static final int UPDATE_FURTHER_WORK = 2230;
	public static final int DELETE_FURTHER_WORK = 2240;
	public static final int UPDATE_PM_HEADER = 2250;
	public static final int UPDATE_PM_CAUSE = 2260;
	public static final int EXPORT_PM_CAUSE = 2270;
	public static final int IMPORT = 2280;
	public static final int LOGOUT = 2290;
	public static final int LOGIN = 2300;
	public static final int SEARCH_USER_PROFILES = 2310;
	public static final int GET_USER_PROFILES_LIST = 2320;
	public static final int UPDATE_USER_PROFILE = 2330;
	public static final int DELETE_USER_PROFILES = 2340;
	public static final int GET_USER_PROFILE = 2350;
	public static final int VIEW_USER_PROFILE = 2351;
	public static final int USER_CHANGE_PASSWORD = 2352;
	public static final int ADD_USER_PROFILE = 2360;
	public static final int USER_PROFILE_DETAIL = 2361;
	public static final int ACTION_ADD = 2370;
	public static final int ACTION_DELETE = 2380;
	public static final int ACTION_UPDATE = 2390;
	public static final int WO_INFORMATION = 2400;
	public static final int WO_DELIVERABLE = 2410;
	public static final int SCHEDULE_REVIEW_AND_TEST = 2420;
	public static final int MODULE_MODULE = 2430;
	public static final int WO_METRICS_PERFORMANCE = 2440;
	public static final int WO_METRICS_STANDARD_METRIC = 2450;
	public static final int WO_METRICS_CUSTOM_METRIC = 2460;
	public static final int WO_TEAM = 2470;
	public static final int WO_ACCEPTANCE = 2480;
	public static final int PL_OVERVIEW = 2490;
	public static final int PL_DEVDEP = 2500;
	public static final int PL_LIFECYCLE = 2510;
	public static final int PL_ORGANIZATION = 2520;
	public static final int PL_TRAINING = 2530;
	public static final int PL_FINANCE = 2540;
	public static final int PL_TOOL = 2550;
	public static final int SCHE_MAJOR_INFOMATION_GET_LIST = 2560;
	public static final int SCHE_SUBCONTRACT_GET_LIST = 2570;
	public static final int SCHE_STAGE_GET_LIST = 2580;
	public static final int SCHE_REVIEW_TEST_GET_LIST = 2590;
	public static final int SCHE_REVIEW_TEST_UPDATE = 2600;
	public static final int SCHE_REVIEW_TEST_UPDATE2 = 2610;
	public static final int SCHE_REVIEW_TEST_UPDATE3 = 2611;
	public static final int SCHE_REVIEW_TEST_UPDATE4 = 2612;
	public static final int SCHE_REVIEW_TEST_VIEW = 2620;
	public static final int SCHE_OTHER_QUALITY_GET_LIST = 2630;
	public static final int SCHE_TRAINING_PLAN_GET_LIST = 2640;
	public static final int SCHE_CRITICAL_DEPENDENCIES_GET_LIST = 2650;
	public static final int SCHE_FINANCIAL_PLAN_GET_LIST = 2660;
	public static final int SCHE_SIZE_INPUT = 2670;
	public static final int EFF_INFORMATION_GET_LIST = 2680;
	public static final int EFF_STAGE_GET_LIST = 2690;
	public static final int EFF_REVIEW_GET_LIST = 2710;
	public static final int EFF_QUALITY_ACTIVITY_GET_LIST = 2720;
	public static final int EFF_WEEKLY_GET_LIST = 2730;
	public static final int EFF_GET_BATCH_PLAN = 2731;
	public static final int EFF_UPDATE_BATCH_PLAN = 2732;
	public static final int BATCH_UPDATE_PROCESS_EFFORT = 2733;
	public static final int UPDATE_DEFECTS = 2740;
	public static final int DETAIL_DEFECT_PLAN = 2750;
	public static final int DETAIL_DEFECT_REPLAN = 2751;
	public static final int DETAIL_DEFECT_RATE = 2752;
	public static final int ADD_WP_DEFECT_PLAN = 2760;
	public static final int UPDATE_WP_DEFECT_REPLAN = 2761;
	public static final int UPDATE_DEFECT_RATE_REPLAN = 2762;
	public static final int DELETE_DEFECT_PLAN = 2770;
	public static final int UPDATE_WR_COMMENTS = 2780;
	public static final int UPDATE_DASHBOARD = 2790;
	public static final int UPDATE_MR_COMMENTS = 2800;
	public static final int WO_CALLER = 2810;
	public static final int REQUIREMENT_UPDATE_STATUS = 2820;
	public static final int REQUIREMENT_BATCH_EDIT = 2840;
	public static final int REQUIREMENT_BATCH_UPDATE = 2850;
	public static final int REQUIREMENT_GROUP_BY = 2860;
	public static final int REQUIREMENT_DETAIL = 2862;
	public static final int REQUIREMENT_STATUS = 2864;
	public static final int UPDATE_PROJECT_POINT = 2870;
	public static final int UPDATE_GROUP_POINT = 2890;
	public static final int UPDATE_GROUP_POINT2 = 2900;
	public static final int UPDATE_GROUP_METRIC = 2910;
	public static final int UPDATE_GROUP_METRIC2 = 2920;
	public static final int VIEW_GROUP_METRIC = 2930;
	public static final int VIEW_PROJECT_POINT = 2940;
	public static final int PROJECT_SEARCH = 2950;
	public static final int ADDNEW_DPTASK = 2960;
	public static final int UPDATE_DPTASK = 2970;
	public static final int DELETE_DPTASK = 2980;
	public static final int PROJECT_SKILL_DETAIL = 3000;
	public static final int UPDATE_SKILL = 3010;
	public static final int TO_ADD_SKILL = 3020;
	public static final int DELETE_SKILL = 3030;
	public static final int ADD_SKILL = 3040;
	public static final int REFRESH_SKILL_LIST = 3050;
	public static final int WORKUNIT_HOME2 = 3060;
	public static final int UPDATE_HIS = 3070;
	public static final int DELETE_HIS = 3080;
	public static final int ADDNEW_HIS = 3090;
	public static final int SAVE_HIS = 3100;
	public static final int SQA_DEFECT_TYPE = 3110;
	public static final int DEFECT_PROGRESS = 3120;
	public static final int RISK_COMMON = 3130;
	public static final int RISK_IDENTIFY = 3140; // Added by HaiMM
	public static final int GET_SQA_DEFECT_ORIGIN = 3150;
	/*************Group constants***************/
	// PCB 
	public static final int PCB_CREATE = 3180;
	public static final int PCB_SELECT_PROJECT = 3190;
	public static final int PCB_VIEW = 3200;
	public static final int PCB_SAVEINFO = 3210;
	public static final int PCB_SAVEDETAILS = 3220;
	public static final int PCB_SAVECOMMENTS = 3230;
	public static final int PCB_SAVECONCLUSIONS = 3240;
	public static final int PCB_LOADSEARCHPAGE = 3250;
	public static final int PCB_DELETE = 3260;
	// Index 
	public static final int PRODUCT_INDEXES = 3280;
	public static final int INDEX_DRILL = 3290;
	// Norms 
	public static final int LOADNORMS = 3310;
	public static final int SAVENORMS = 3320;
	public static final int LOADNORMSSQA = 3330;
	public static final int SAVENORMSSQA = 3340;
	// Monitoring & Tasks 
	public static final int GROUPMONITORING = 3360;
	public static final int ORGMONITORING = 3370;
	public static final int GROUPMONITORINGDRILL = 3380;
	public static final int CBOMONITORING = 3390;
	public static final int TASK_ADDPREP = 3400;
	public static final int TASK_ADD = 3410;
	public static final int TASK_UPDATEPREP = 3420;
	public static final int TASK_UPDATE = 3430;
	public static final int TASK_DELETE = 3440;
	public static final int TASK_LIST = 3450;
	public static final int TASK_DRILLUP = 3460;
	public static final int DECISION = 3470;
	public static final int DECISION_ADD = 3480;
	public static final int DECISION_UPDATE = 3490;
	public static final int DECISION_DELETE = 3500;
	public static final int PLAN = 3510;
	public static final int PLAN_ADD = 3520;
	public static final int PLAN_UPDATE = 3530;
	public static final int PLAN_DELETE = 3540;
	// Plan 
	public static final int LOADPLANNING = 3560;
	public static final int SAVEPLANNING = 3570;
	// Tools
	public static final int GET_RSA = 3590;
	public static final int GET_DMS = 3600;
	public static final int GET_DASHBOARD = 3610;
	public static final int GET_NCMS = 3620;
	public static final int GET_CALLLOG = 3621;
	public static final int GET_TIMESHEET = 3630;
	public static final int GET_ITS = 3631;
	public static final int GET_ITSPROJECT = 3632;
	// Misc 
	public static final int HOME_SQA = 3650;
	public static final int GET_PAGE = 3660;
	public static final int HOME_PQA = 3670;
	public static final int SET_METRIC = 3680;
	public static final int PQA_REPORT = 3690;
	//Process Assest 
	public static final int PROASS_PROJECT_DESC = 3710;
	public static final int PROASS_TAILORING_DEVIATION = 3720;
	public static final int PROASS_RISK = 3730;
	public static final int PROASS_PRACTICE_LESSON = 3740;
	public static final int PROASS_ISSUE = 3750;
	public static final int RISK_BASELINE = 3760;
	// Contract Type
	public static final int CONTRACT_TYPE = 3761;
	public static final int CONTRACT_TYPE_ADD = 3762;
	public static final int CONTRACT_TYPE_UPDATE = 3763;
	public static final int CONTRACT_TYPE_REMOVE = 3764;
	// Business Domain
	public static final int BUSINESS_DOMAIN = 3770;
	public static final int BIZDOMAIN_UPDATE = 3780;
	public static final int BIZDOMAIN_REMOVE = 3790;
	public static final int BIZDOMAIN_ADD = 3800;
	// Application Type
	public static final int APPTYPE_UPDATE = 3820;
	public static final int APPTYPE_REMOVE = 3830;
	public static final int APPTYPE_ADD = 3840;
	public static final int APP_TYPE = 3850;
	public static final int DEFECT_LOG = 3870;
	public static final int DEFECT_LOG_ADDNEW = 3880;
	public static final int DEFECT_LOG_UPDATE = 3890;
	public static final int DEFECT_LOG_DELETE = 3900;
	public static final int DEFECT_LOG_DRILL = 3901;
	public static final int COMMON_DEFECT = 3920;
	public static final int COMMON_DEFECT_ADDNEW = 3930;
	public static final int COMMON_DEFECT_UPDATE = 3940;
	public static final int COMMON_DEFECT_DELETE = 3950;
	public static final int DP_DATABASE = 3970;
	public static final int DP_PCB = 3980;
	//DP
	public static final int PROASS_PROJECT_DETAIL = 4010;
	//HTML 
	public static final int HTML_WU_COMBO = 4040;
	//Tailoring 
	public static final int TAILORING_REF = 4061;
	public static final int PROCESS_TAILORING = 4060;
	public static final int PROCESS_TAILORING_ADD = 4070;
	public static final int PROCESS_TAILORING_UPDATE = 4080;
	public static final int PROCESS_TAILORING_REMOVE = 4090;
	public static final int PROCESS_TAILORING_SEARCH = 4100;
	public static final int TEST = 4101;
	public static final int GROUP_INFO = 4110;
	public static final int GROUP_INFO_UPDATE = 4120;
	public static final int GROUP_CALENDAR_EFFORT = 4122;
	public static final int GROUP_CALENDAR_EFFORT_MAX_NUMBER = 20; 
	public static final int GROUP_RESOURCE_ALLOCATION = 4124;
	public static final int GROUP_RESOURCE_ALLOCATION_EXPORT = 4126;
	public static final int SIZE_OF_FILE = 1500 * 1024;
	public static final int HR_USER_BY_GROUP = 0;
	public static final int HR_USER_BY_PROJECT = 1;
	public static final int HR_USER_BY_ALL = 2;
	public static final int HR_USER_BY_OTHER_GROUP = 2;
	// Team Size Progress
	public static final int TEAM_SIZE_PROGRESS = 4121;
	// Plan Defect Removal Efficiency
	public static final int PLAN_DRE_BY_QC_STAGE = 4200;
	public static final int PLAN_DRE_BY_QC_STAGE_SAVE = 4201;
	public static final int PLAN_DRE_PLAN_DEFECT = 4210;
	public static final int PLAN_DRE_REPLAN = 4220;
	public static final int PLAN_DRE_REPLAN_SAVE = 4221;
	public static final int PLAN_DRE_DEFECT_RATE = 4230;
	public static final int PLAN_DRE_DEFECT_RATE_SAVE = 4231;
	public static final int PLAN_DRE_LEAKAGE = 4240;
	public static final int ADDNEW_DARPLAN = 5100;
	public static final int UPDATE_DARPLAN = 5101;
	public static final int DELETE_DARPLAN = 5102;
	public static final int MILESTONE_DEFECT_UPDATE= 5103;
	public static final int PRODUCT_LOC_LIST = 5120;
	public static final int PRODUCT_LOC_DETAIL = 5130;
	public static final int PRODUCT_LOC_UPDATE = 5140;
	public static final int PRODUCT_LOC_UPDATE_SAVE = 5141;
	public static final int PRODUCT_LOC_ADD = 5150;
	public static final int PRODUCT_LOC_ADD_SAVE = 5151;
	public static final int SPECIAL_REPORT = 9999;
	public static final int XML_PROJECT_GENERATION = 6000;
	public static final int FILLTER_USER = 7000;
	public static final int FILLTER_CUSTOMER = 7001;
	public static final int FILLTER_USER_ASSIGNED_PROJECT = 7100;
	// Constant for Param to generate chart
	//public static final String paramNameSeries = "NameSeries";
	//public static final String paramValueSeries = "ValueSeries";
	//public static final String paramCategorySeries = "CategorySeries";
	//public static final String SHOW_SHAPE = "SHOW_SHAPE";
	
	// HaiMM add new Constant for Project Plan template -- START
	
	// Estimate Defect (Section 2.4.1)
	public static final int EST_DEFECT_VIEW = 8001;
	public static final int UPDATE_EST_DEFECT = 8002;
	
	 
	// Estimate Effort (Section 3.2)
	public static final int EST_EFFORT_VIEW = 8003;
	public static final int EST_EFFORT_UPDATE = 8004;	
	public static final int EST_EFFORT_DELETE = 8005;
	public static final int EST_EFFORT_ADD_PRE = 8006;
	public static final int EST_EFFORT_ADD = 8007;
	
	// Finance
	public static final int FINANCE_VIEW = 8008;
	public static final int FINANCE_UPDATE = 8009;	
	public static final int FINANCE_DELETE = 8010;
	public static final int FINANCE_ADD_PRE = 8011;
	public static final int FINANCE_ADD = 8012;	
	
	// Organization
	public static final int ORGANIZATION_VIEW = 8013;
	public static final int ORGANIZATION_UPDATE = 8014;
	public static final int ORGANIZATION_DELETE = 8015;
	public static final int ORGANIZATION_ADD_PRE = 8016;
	public static final int ORGANIZATION_ADD = 8017;
	
	// Estimate Defect (Section 2.4.1) - Updated
	public static final int EST_DEFECT_PRE_ADD = 8018;
	public static final int EST_DEFECT_PRE_UPDATE = 8019;
	public static final int EST_DEFECT_ADD = 8020;
	public static final int EST_DEFECT_UPDATE = 8021;
	public static final int EST_DEFECT_DELETE = 8022;
	
	// HaiMM add new Constant for Project Plan template -- END
	
	//	Communication & Reporting
	public static final int COMREPORT_VIEW = 8030;
	public static final int COMREPORT_PRE_ADD = 8040;
	public static final int COMREPORT_PRE_UPDATE = 8050;
	public static final int COMREPORT_ADD = 8060;
	public static final int COMREPORT_UPDATE = 8070;
	public static final int COMREPORT_DELETE = 8080;
	 
	//	SECURITY ASPECTS
	public static final int DEFINITIONS_ACRONYMS_VIEW = 8090;
	public static final int DEFINITIONS_ACRONYMS_PRE_ADD = 8100;
	public static final int DEFINITIONS_ACRONYMS_PRE_UPDATE = 8110;
	public static final int DEFINITIONS_ACRONYMS_ADD = 8120;
	public static final int DEFINITIONS_ACRONYMS_UPDATE = 8130;
	public static final int DEFINITIONS_ACRONYMS_DELETE = 8140;
		
	public static final int DEFECT_REV_PRODUCT_PRE_ADD = 8150;
	public static final int DEFECT_REV_PRODUCT_PRE_UPDATE = 8160;
	public static final int DEFECT_REV_PRODUCT_ADD = 8170;
	public static final int DEFECT_REV_PRODUCT_UPDATE = 8180;
	public static final int DEFECT_REV_PRODUCT_DELETE = 8190;
	
	public static final int DEFECT_TEST_PRODUCT_PRE_ADD = 8200;
	public static final int DEFECT_TEST_PRODUCT_PRE_UPDATE = 8210;
	public static final int DEFECT_TEST_PRODUCT_ADD = 8220;
	public static final int DEFECT_TEST_PRODUCT_UPDATE = 8230;
	public static final int DEFECT_TEST_PRODUCT_DELETE = 8240;
	
	// TEAM EVALUATION
	
	public static final int WO_UPDATE_TEAM_EVALUATION = 9000;
	public static final int WO_TEAM_EVALUATION_DO_UPDATE = 9100;	
	
	// Report Batch Update Team Evaluation -  Post Mortem
	
	public static final int REPORTS_UPDATE_BATCH_TEAM_EVALUATION = 10000;
	public static final int DO_UPDATE_TEAM_EVALUATION_POST_MORTEM = 10010;
	public static final int DO_DELETE_IN_TEAM_BATCH_POST_MORTEM = 10020;
	
	// Customer  
	
	public static final int CUSTOMER_ADD_NEW = 11000 ; 
	public static final int CUSTOMER_UPDATE = 11010 ;
	public static final int DO_CUSTOMER_UPDATE = 11020;
	public static final int SEARCH_CUSTOMER = 11030; 
	
	// Delivery List
	
	public static final int DELIVERY_LIST_SEARCH = 12000 ;
	
	public static final int RISK_MENUORG = 13000 ;
	// added by anhtv08- start 
	// Migrate data
	//public static final int MIGRATE_DATA = 123456 ;
	
	// end   
}