<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>menuOrg.jsp</TITLE>
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
var NoOffFirstLineMenus=10;			// Number of first level items
//The labels of menu are used in the loadMenu functions of the jsp, change them carefully
Menu1=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Home")%>",URL+"<%=Constants.WORKUNIT_HOME+"&workUnitID="+(String)session.getAttribute("workUnitID")%>","",0,20,mnuWdth);
Menu2=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Norms")%>","","",2,20,mnuWdth);
	Menu2_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Software")%>",URL+"<%=Constants.LOADNORMS+"&type="+MetricDescInfo.GR_SOFTWARE%>","",0,20,180);
	Menu2_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Process")%>",URL+"<%=Constants.LOADNORMS+"&type="+MetricDescInfo.GR_PROCESS%>","",0);
Menu3=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Plans")%>","","",6);
	Menu3_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Tasks")%>",URL+"<%=Constants.PLAN%>","",0,20,180);
	Menu3_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Staff")%>",URL+"<%=Constants.LOADPLANNING%>&section=Staff","",0);
	Menu3_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Finance")%>",URL+"<%=Constants.LOADPLANNING%>&section=Finance","",0);
	Menu3_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Infrastructure")%>",URL+"<%=Constants.LOADPLANNING%>&section=Infrastructure","",0);
	Menu3_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Product")%>",URL+"<%=Constants.LOADPLANNING%>&section=Product","",0);
	Menu3_6=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Process")%>",URL+"<%=Constants.LOADPLANNING%>&section=Process","",0);
Menu4=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Product")%>","","",16);//if change needed , please synchronize with menuGroup.jsp and Product.jsp
	Menu4_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Summary")%>",URL+"<%=Constants.PRODUCT_INDEXES%>","",0,20,180);
	Menu4_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Inprogressprojects")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.IN_PROGRESS_PROJECTS)%>","",0);
	Menu4_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Productionsize")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.PRODUCTION_SIZE)%>","",0);
	Menu4_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.ProductivityLOC")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.PRODUCTIVITY_LOC)%>","",0);
	Menu4_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Requirementstability")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.REQUIREMENT_STABILITY)%>","",0);
	Menu4_6=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Acceptancerate")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.ACCEPTANCE_RATE)%>","",0);
	Menu4_7=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Reqcompleteness")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.REQUIREMENT_COMPLETENESS)%>","",0);
	Menu4_8=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Timeliness")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.TIMELINESS)%>","",0);
	Menu4_9=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Deliveryscheddeviation")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.DELIVERY_SCHEDULE_DEVIATION)%>","",0);
	Menu4_10=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Defectrate")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.DEFECT_RATE)%>","",0);
	Menu4_11=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Leakage")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.LEAKAGE)%>","",0);
	Menu4_12=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.DefectrateLOC")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.DEFECT_RATE_LOC)%>","",0);
	Menu4_13=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Customersatisfaction")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.CUSTOMER_SATISFACTION)%>","",0);
	Menu4_14=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Effortefficiency")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.EFFORT_EFFICIENCY)%>","",0);
	Menu4_15=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Nonconfproductrate")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.NONCONFORMING_PRODUCT_RATE)%>","",0);
	Menu4_16=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.DeliveryList")%>",URL+"<%=Constants.INDEX_DRILL%>"+"&metricID="+"<%=String.valueOf(MetricDescInfo.DELIVERY_LIST)%>","",0);
Menu5=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Tasks")%>",URL+"<%=Constants.ORGMONITORING%>","",0);
Menu6=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Issues")%>",URL+"<%=Constants.ISSUE%>","",0);
Menu7=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.MngmtDecisions")%>",URL+"<%=Constants.DECISION%>","",0);
Menu8=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.ProcessAssets")%>","","",5);
	Menu8_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.ProjectDescription")%>",URL+"<%=Constants.PROASS_PROJECT_DESC%>&cboGroup=132","",0,20,180);//
	Menu8_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.TailoringDeviation")%>",URL+"<%=Constants.PROASS_TAILORING_DEVIATION%>&cboGroup=132","",0);
	Menu8_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.RisksEncountered")%>",URL+"<%=Constants.PROASS_RISK%>&cboGroup=132","",0);
	Menu8_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.PracticesLessons")%>",URL+"<%=Constants.PROASS_PRACTICE_LESSON%>&cboGroup=132","",0);
	Menu8_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Issues")%>",URL+"<%=Constants.PROASS_ISSUE%>&cboGroup=132","",0);
Menu9=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.PCBreports")%>",URL+"<%=Constants.PCB_LOADSEARCHPAGE%>","",1);
	Menu9_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.SearchCreate")%>",URL+"<%=Constants.PCB_LOADSEARCHPAGE%>","",0,20,mnuWdth);
	Menu9_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Galinformation")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbInformation.jsp","",0);
	Menu9_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Metrics")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbMetrics.jsp","",0);
	Menu9_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Comments")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbComments.jsp","",0);
	Menu9_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Conclusions")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbConclusion.jsp","",0);
Menu10=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Customer")%>",URL+"<%=Constants.LOAD_CUSTOMER_PAGE%>","",1);
	Menu10_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuOrg.Customer")%>",URL+"<%=Constants.LOAD_CUSTOMER_PAGE%>","",0,20,mnuWdth);
</SCRIPT>
<script type='text/javascript' src='jscript/menu_com.js'></script>
<noscript><%= languageChoose.getMessage("fi.jsp.menuOrg.Yourbrowserdoesnotsupportscript")%></noscript>
<script type='text/javascript' >Go();</script>

</BODY>
</HTML>
