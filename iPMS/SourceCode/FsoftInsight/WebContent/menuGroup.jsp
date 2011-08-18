<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>menuGroup.jsp</TITLE>
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
<%
String main=request.getParameter("main");
String sub=request.getParameter("sub");
%>
<script type='text/javascript' src='jscript/menuParams.js'></script>
<SCRIPT language="JavaScript">
//after drill down on index, the left menu is refreshed, we must keep the selection
selectedSub=<%=sub%>;
selectedMain=<%=main%>;
var NoOffFirstLineMenus=9;			// Number of first level items
//The labels of menu are used in the loadMenu functions of the jsp, change them carefully
Menu1=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Home")%>",URL+"<%=Constants.WORKUNIT_HOME+"&workUnitID="+(String)session.getAttribute("workUnitID")%>","",0,20,mnuWdth);
Menu2=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Information")%>",URL+"<%=Constants.GROUP_INFO%>","",0);
Menu3=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Norms")%>","","",0);
Menu4=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Plans")%>","","",0);
Menu5=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.HumanResource")%>", "", "",2);
	Menu5_1=new Array("<%=languageChoose.getMessage("fi.jsp.menuGroup.CalendarEffort")%>",URL+"<%=Constants.GROUP_CALENDAR_EFFORT%>","",0,20, 180);
	Menu5_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.ResourceAllocation")%>",URL+"<%=Constants.GROUP_RESOURCE_ALLOCATION%>","",0);
Menu6=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Product")%>","","",15);//if change needed , please synchronize with menuOrg.jsp and Product.jsp
	Menu6_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Summary")%>",URL+"<%=Constants.PRODUCT_INDEXES%>","",0,20,180);
	Menu6_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Inprogressprojects")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.IN_PROGRESS_PROJECTS)%>","",0);
	Menu6_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Productionsize")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.PRODUCTION_SIZE)%>","",0);
	Menu6_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.ProductivityLOC")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.PRODUCTIVITY_LOC)%>","",0);
	Menu6_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Requirementstability")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.REQUIREMENT_STABILITY)%>","",0);
	Menu6_6=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Acceptancerate")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.ACCEPTANCE_RATE)%>","",0);
	Menu6_7=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Reqcompleteness")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.REQUIREMENT_COMPLETENESS)%>","",0);
	Menu6_8=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Timeliness")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.TIMELINESS)%>","",0);
	Menu6_9=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Deliveryscheddeviation")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.DELIVERY_SCHEDULE_DEVIATION)%>","",0);
	Menu6_10=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Defectrate")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.DEFECT_RATE)%>","",0);
	Menu6_11=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Leakage")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.LEAKAGE)%>","",0);
	Menu6_12=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.DefectrateLOC")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.DEFECT_RATE_LOC)%>","",0);
	Menu6_13=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Customersatisfaction")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.CUSTOMER_SATISFACTION)%>","",0);
	Menu6_14=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Effortefficiency")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.EFFORT_EFFICIENCY)%>","",0);
	Menu6_15=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Nonconfproductrate")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.NONCONFORMING_PRODUCT_RATE)%>","",0);
Menu7=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Tasks")%>",URL+"<%=Constants.GROUPMONITORING%>","",0);
Menu8=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Issues")%>",URL+"<%=Constants.ISSUE%>","",0);
Menu9=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.PCBreport")%>",URL+"<%=Constants.PCB_LOADSEARCHPAGE%>","",1);
	Menu9_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.SearchCreate")%>",URL+"<%=Constants.PCB_LOADSEARCHPAGE%>","",0,20,mnuWdth);
	Menu9_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Galinformation")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbInformation.jsp","",0);
	Menu9_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Metrics")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbMetrics.jsp","",0); 
	Menu9_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Comments")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbComments.jsp","",0);
	Menu9_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.Conclusions")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbConclusion.jsp","",0);
	Menu9_6=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.SavePCB")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbSave.jsp","",0);
</SCRIPT>
<script type='text/javascript' src='jscript/menu_com.js'></script>
<noscript><%= languageChoose.getMessage("fi.jsp.menuGroup.Yourbrowserdoesnotsupportscript")%></noscript>
<script type='text/javascript' >Go();</script>
</BODY>
</HTML>