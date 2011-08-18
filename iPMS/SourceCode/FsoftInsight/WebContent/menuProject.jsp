<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<%
	String wuId = (String)session.getAttribute("projectID");
	String strDisable=request.getParameter("mnuDisable");
	boolean mnuDisable="1".equals(strDisable);
	String bkGrnd=(mnuDisable)?"#C0C0C0":"";
	String main=request.getParameter("main");
	String sub=request.getParameter("sub");
	
	// Add by HaiMM: Hide or Disable CI_Register menu
	ProjectDateInfo prjDateInfo = Project.getProjectDate(Integer.parseInt(wuId));
	
	Date fixed = new Date("03-Apr-2009");
	boolean compareDate = false;
	if (prjDateInfo.actualStartDate != null) compareDate = prjDateInfo.actualStartDate.before(fixed);
	
	int noOffFirstLineMenus = 15;
	if (compareDate) noOffFirstLineMenus = 16;
	// Add by HaiMM --- End
%>
<TITLE>menuProject.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY  bgcolor='#C3D4E4' style="margin: 0px" >
<script type='text/javascript'>
//function Go(){return}
</script>

<script type='text/javascript' src='jscript/menuParams.js'></script>
<SCRIPT language="JavaScript">
//after updateing the WO>metrics, the left menu is refreshed, we must keep the selection
	selectedSub=<%=sub%>;
	selectedMain=<%=main%>;
	var NoOffFirstLineMenus=<%=noOffFirstLineMenus%>;	// Number of first level items
// Menu tree
//	MenuX=new Array(Text to show, Link, background image (optional), number of sub elements, height, width);
//	For rollover images set "Text to show" to:  "rollover:Image1.jpg:Image2.jpg"
//The labels of menu are used in the loadMenu functions of the jsp, change them carefully
Menu1=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Home")%>",URL+"<%=Constants.WORKUNIT_HOME+"&workUnitID="+(String)session.getAttribute("workUnitID")%>","",0,20,mnuWdth);
Menu2=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Workorder")%>","","",7);
	Menu2_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Information")%>",URL+"<%=Constants.WO_GENERAL_INFO_GET_LIST%>","",0,20,mnuWdth);	
	Menu2_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.StageDeliv")%>",URL+"<%=Constants.WO_DELIVERABLE_GET_LIST%>","",0);
	Menu2_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Metrics")%>",URL+"<%=Constants.WO_PERFORMANCE_GET_LIST%>","",0);
	Menu2_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Team")%>",URL+"<%=Constants.WO_TEAM_GET_LIST %>","",0);
	Menu2_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Changes")%>",URL+"<%=Constants.WO_CHANGE_GET_LIST%>&change_source_page=0","",0);
	Menu2_6=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Acceptance")%>",URL+"<%=Constants.WO_ACCEPTANCE_GET_LIST%>","",0);
	Menu2_7=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Signatures")%>",URL+"<%=Constants.WO_SIG_GET_LIST%>","",0);
Menu3=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Projectplan")%>","","",15);
	Menu3_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Information")%>",URL+"<%=Constants.PL_OVERVIEW_GET_PAGE %>","",0,20,mnuWdth);
	Menu3_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.DelivDepend")%>",URL+"<%=Constants.PL_DELIVERIES_DEPENDENCIES_GET_PAGE %>","",0);
	Menu3_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Lifecycle")%>",URL+"<%=Constants.PL_LIFECYCLE_GET_PAGE%>","",0);
	Menu3_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Quality")%>",URL+"<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>","",0);
	Menu3_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Estimate")%>",URL+"<%=Constants.MODULE_LIST%>","",0);
	Menu3_6=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.EstEffort")%>",URL+"<%=Constants.EST_EFFORT_VIEW%>","",0);
	Menu3_7=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.ProjectSchedule")%>",URL+"<%=Constants.PL_PROJECT_SCHEDULE_GET_LIST%>","",0);
	Menu3_8=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Infrastructure")%>",URL+"<%=Constants.GET_TOOL_LIST%>","",0);
	Menu3_9=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Training")%>",URL+"<%=Constants.GET_TRAINING_LIST%>","",0);
	Menu3_10=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Finance")%>",URL+"<%=Constants.FINANCE_VIEW%>","",0);
	Menu3_11=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.OrgMenu")%>",URL+"<%=Constants.ORGANIZATION_VIEW%>","",0);	
	Menu3_12=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Organization")%>",URL+"<%=Constants.PL_STRUCTURE_GET_PAGE%>","",0);
	Menu3_13=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.ComReport")%>",URL+"<%=Constants.COMREPORT_VIEW%>","",0);
	Menu3_14=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Changes")%>",URL+"<%=Constants.WO_CHANGE_GET_LIST%>&change_source_page=1","",0);
	Menu3_15=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Signatures")%>",URL+"<%=Constants.PL_SIG_GET_LIST%>","",0);
<%if (mnuDisable){%>Menu4=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Requirements")%>","javascript:mnuDisabled()","<%=bkGrnd%>",0);
<%}else{%>
Menu4=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Requirements")%>",URL+"<%=Constants.REQUIREMENT_LIST_INIT%>","",3);
	Menu4_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Information")%>",URL+"<%=Constants.REQUIREMENT_GRAPH%>","",0,20,mnuWdth+30);
	Menu4_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.StageprocessRCR")%>",URL+"<%=Constants.REQUIREMENT_PLAN%>","",0);
	Menu4_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Statustraceability")%>",URL+"<%=Constants.REQUIREMENT_LIST_INIT%>","",0);
<%}

if (mnuDisable){%>Menu5=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Schedule")%>","javascript:mnuDisabled()","<%=bkGrnd%>",0);
<%}else{%>Menu5=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Schedule")%>","","",8);
<%}%>
	Menu5_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Information")%>",URL+"<%=Constants.SCHE_MAJOR_INFOMATION_GET_LIST%>","",0,20,mnuWdth);
	Menu5_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.StageIteration")%>",URL+"<%=Constants.SCHE_STAGE_GET_LIST%>","",0);
	Menu5_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.ReviewTest")%>",URL+"<%=Constants.SCHE_REVIEW_TEST_GET_LIST%>","",0);
	Menu5_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Otherquality")%>",URL+"<%=Constants.SCHE_OTHER_QUALITY_GET_LIST%>","",0);
	Menu5_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Training")%>",URL+"<%=Constants.SCHE_TRAINING_PLAN_GET_LIST%>","",0);
	Menu5_6=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Dependencies")%>",URL+"<%=Constants.SCHE_CRITICAL_DEPENDENCIES_GET_LIST%>","",0);
	Menu5_7=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Subcontracts")%>",URL+"<%=Constants.SCHE_SUBCONTRACT_GET_LIST%>","",0);
	Menu5_8=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Finance")%>",URL+"<%=Constants.SCHE_FINANCIAL_PLAN_GET_LIST%>","",0);
<%if (mnuDisable){%>Menu6=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Effort")%>","javascript:mnuDisabled()","<%=bkGrnd%>",0);
<%}else{%>Menu6=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Effort")%>","","",5);
<%}%>
	Menu6_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Information")%>",URL+"<%=Constants.EFF_INFORMATION_GET_LIST%>","",0,20,mnuWdth);
	Menu6_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Stageprocess")%>",URL+"<%=Constants.EFF_STAGE_GET_LIST%>","",0);
	Menu6_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Review")%>",URL+"<%=Constants.EFF_REVIEW_GET_LIST%>","",0);
	Menu6_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Otherqualityactiv.")%>",URL+"<%=Constants.EFF_QUALITY_ACTIVITY_GET_LIST%>","",0);
	Menu6_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Weekly")%>",URL+"<%=Constants.EFF_WEEKLY_GET_LIST%>","",0);
<%if (mnuDisable){%>	
Menu7=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Products")%>","javascript:mnuDisabled()","<%=bkGrnd%>",2);
    Menu7_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.ProductsAndSize")%>","","",0,20,mnuWdth);
    Menu7_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.ProductsSizeLOC")%>","","",0);
Menu8=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Defects")%>","javascript:mnuDisabled()","<%=bkGrnd%>",0);
Menu9=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Risks")%>","javascript:mnuDisabled()","<%=bkGrnd%>",0);
<%}else{%>
Menu7=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Products")%>",URL+"<%=Constants.MODULE_LIST%>","",2);
    Menu7_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.ProductsAndSize")%>",URL+"<%=Constants.MODULE_LIST%>","",0,20,mnuWdth);
    Menu7_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.ProductsSizeLOC")%>",URL+"<%=Constants.PRODUCT_LOC_LIST%>","",0);
Menu8=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Defects")%>",URL+"<%=Constants.DEFECT_VIEW%>","",6);
	Menu8_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Information")%>",URL+"<%=Constants.DEFECT_VIEW%>","",0,20,mnuWdth);
	Menu8_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Proddefectplan")%>",URL+"<%=Constants.DETAIL_DEFECT_PLAN%>","",0);
	Menu8_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Stagedefectplan")%>",URL+"<%=Constants.PLAN_DRE_BY_QC_STAGE%>","",0);
	Menu8_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Progress")%>",URL+"<%=Constants.DEFECT_PROGRESS%>","",0);
	Menu8_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.DPLog")%>",URL+"<%=Constants.DEFECT_LOG%>","",0);
	Menu8_6=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Commondefects")%>",URL+"<%=Constants.COMMON_DEFECT%>","",0);
<%}%>
Menu9=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Risks")%>",URL+"<%=Constants.RISK_LIST_INIT%>","",0);
Menu10=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Issues")%>",URL+"<%=Constants.ISSUE%>","",0);
Menu11=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Lessons")%>",URL+"<%=Constants.GET_PRACTICE_LIST%>","",0);
Menu12=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Tailorings")%>",URL+"<%=Constants.TAILORING_GET_LIST%>","",0);
Menu13=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Parameters")%>","","",3);
	Menu13_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Distrates")%>",URL+"<%=Constants.DISTR_RATE_INIT%>","",0,20,mnuWdth);
	Menu13_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Estimmethods")%>",URL+"<%=Constants.METHOD_LIST%>","",0);
	Menu13_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Conversionrates")%>",URL+"<%=Constants.CONVERSION_LIST_INIT%>","",0);
<%if (mnuDisable){%>
Menu14=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Summarymetrics")%>","javascript:mnuDisabled()","<%=bkGrnd%>",0);
Menu15=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Reports")%>","javascript:mnuDisabled()","<%=bkGrnd%>",0);
<%}else{%>
Menu14=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Summarymetrics")%>",URL+"<%=Constants.NORM_GET_LIST%>","",0);
Menu15=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Reports")%>","","",3);
<%}%>
	Menu15_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Weekly")%>",URL+"<%=Constants.WEEKLY_REPORT_VIEW_INIT%>","",0,20,mnuWdth);
	Menu15_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Milestone")%>",URL+"<%=Constants.MILESTONE_GET_PAGE%>","",0);
	Menu15_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.Postmortem")%>",URL+"<%=Constants.GET_POST_MORTEM%>","",0);
<%if (compareDate){%>
Menu16=new Array("<%= languageChoose.getMessage("fi.jsp.menuProject.CIRegister")%>",URL+"<%=Constants.GET_CI_REGISTER%>","",0);
<%}%>
</SCRIPT>
<script type='text/javascript' src='jscript/menu_com.js'></script>
<noscript> <%=languageChoose.getMessage("fi.jsp.menuProject.Yourbrowserdoesnotsupportscrip")%> </noscript>
<script type='text/javascript' >Go();</script>
<FORM name="frm" method="post">

</FORM>
</BODY>
<SCRIPT language="JavaScript">
function mnuDisabled(){
	alert("<%= languageChoose.getMessage("fi.jsp.menuProject.Inordertousethisfeature")%>");
}
</SCRIPT>
</HTML>
