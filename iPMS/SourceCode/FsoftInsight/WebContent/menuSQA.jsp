<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>menuSQA.jsp</TITLE>
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
long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
int []types=null;
%>
<script type='text/javascript' src='jscript/menuParams.js'></script>
<SCRIPT language="JavaScript">
//after drill down on index, the left menu is refreshed, we must keep the selection
selectedSub=<%=sub%>;
selectedMain=<%=main%>;
<%if (workUnitID==Parameters.SQA_WU) {%>
    
	var NoOffFirstLineMenus=10;			// Number of first level items
	//The labels of menu are used in the loadMenu functions of the jsp, change them carefully
	Menu1=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Home")%>",URL+"<%=Constants.HOME_SQA%>","",0,20,mnuWdth);
	Menu2=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Information")%>",URL+"<%=Constants.GROUP_INFO%>","",0);
	Menu3=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Norms")%>",URL+"<%=Constants.LOADNORMS+"&type="+MetricDescInfo.GR_SET_SQA_HOME_PAGE%>","",0);
	Menu4=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Defects")%>","","",2);//if change needed , please synchronize with menuOrg.jsp and Product.jsp
		Menu4_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Type")%>",URL+"<%=Constants.SQA_DEFECT_TYPE%>","",0,20,180);
		Menu4_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Origin")%>",URL+"<%=Constants.GET_SQA_DEFECT_ORIGIN%>","",0);
	Menu5=new Array("DPC","","",2);
		Menu5_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.DPLog")%>",URL+"<%=Constants.DEFECT_LOG%>","",0,20,180);
		Menu5_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.DPDatabase")%>",URL+"<%=Constants.DP_DATABASE%>","",0);
	Menu6=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Tasks")%>",URL+"<%=Constants.TASK_LIST%>","",0);
	Menu7=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Issues")%>",URL+"<%=Constants.ISSUE%>","",0);
	Menu8=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Report")%>",URL+"<%=Constants.PQA_REPORT%>","",0);
	Menu9=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.HumanResource")%>", "", "",2);
		Menu9_1=new Array("<%=languageChoose.getMessage("fi.jsp.menuGroup.CalendarEffort")%>",URL+"<%=Constants.GROUP_CALENDAR_EFFORT%>","",0,20, 180);
		Menu9_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.ResourceAllocation")%>",URL+"<%=Constants.GROUP_RESOURCE_ALLOCATION%>","",0);
	Menu10=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.PCBreport")%>",URL+"<%=Constants.PCB_LOADSEARCHPAGE%>","",1);
		Menu10_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.SearchCreate")%>",URL+"<%=Constants.PCB_LOADSEARCHPAGE%>","",0,20,mnuWdth);
		Menu10_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Galinformation")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbInformation.jsp","",0);
		Menu10_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Metrics")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbMetrics.jsp","",0);
		Menu10_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Comments")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbComments.jsp","",0);
		Menu10_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Conclusions")%>",URL+"<%=Constants.GET_PAGE%>&page=Group/pcbConclusion.jsp","",0);
	MenuPCB="Menu10";
<%}else if (workUnitID==Parameters.PQA_WU){%>
	var NoOffFirstLineMenus=6;// Number of first level items
	//The labels of menu are used in the loadMenu functions of the jsp, change them carefully
	Menu1=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Home")%>",URL+"<%=Constants.HOME_PQA%>","",0,20,mnuWdth);
	Menu2=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Information")%>",URL+"<%=Constants.GROUP_INFO%>","",0);
	Menu3=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Tasks")%>",URL+"<%=Constants.TASK_LIST%>","",0);
	Menu4=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Issues")%>",URL+"<%=Constants.ISSUE%>","",0);
	Menu5=new Array("<%= languageChoose.getMessage("fi.jsp.menuSQA.Report")%>",URL+"<%=Constants.PQA_REPORT%>","",0);
	Menu6=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.HumanResource")%>", "", "",2);
		Menu6_1=new Array("<%=languageChoose.getMessage("fi.jsp.menuGroup.CalendarEffort")%>",URL+"<%=Constants.GROUP_CALENDAR_EFFORT%>","",0,20, 180);
		Menu6_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuGroup.ResourceAllocation")%>",URL+"<%=Constants.GROUP_RESOURCE_ALLOCATION%>","",0);
<%}%>
</SCRIPT>
<script type='text/javascript' src='jscript/menu_com.js'></script>
<noscript><%= languageChoose.getMessage("fi.jsp.menuSQA.Yourbrowserdoesnotsupportscript")%></noscript>
<script type='text/javascript' >Go();</script>

</BODY>
</HTML>
