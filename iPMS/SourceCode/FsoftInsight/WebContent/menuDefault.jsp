<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>menuDefault.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>

<BODY  bgcolor='#C3D4E4'>
<script type='text/javascript' src='jscript/menuParams.js'></script>
<SCRIPT language="JavaScript">
//var NoOffFirstLineMenus=6;			// Number of first level items
var NoOffFirstLineMenus=5;
Menu1=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Userprofiles")%>",URL+"<%=Constants.GET_USER_PROFILES_LIST%>","",0,20,mnuWdth);
Menu2=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Roles")%>",URL+"<%=Constants.GET_RIGHT_GROUP_LIST%>","",0);
Menu3=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Workunits")%>",URL+"<%=Constants.GET_WORK_UNIT_LIST %>","",0);
Menu4=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Parameters")%>","","",6);
	//Moved to norm Menu4_1=new Array("Dist. rates",URL+"<%=Constants.DISTR_RATE_INIT%>","",0,20,mnuWdth);
	Menu4_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Estimmethods")%>",URL+"<%=Constants.METHOD_LIST%>","",0,20,mnuWdth);
	Menu4_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Conversionrates")%>",URL+"<%=Constants.CONVERSION_LIST_INIT%>","",0);
	Menu4_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Contracttype")%>",URL+"<%=Constants.CONTRACT_TYPE%>","",0);
	Menu4_4=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Businessdomain")%>",URL+"<%=Constants.BUSINESS_DOMAIN%>","",0);
	Menu4_5=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Applicationtype")%>",URL+"<%=Constants.APP_TYPE%>","",0);
	Menu4_6=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Processtailoring")%>",URL+"<%=Constants.PROCESS_TAILORING%>","",0);
Menu5=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Orgpoint")%>","","",3);
	Menu5_1=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Fsoftgroup")%>",URL+"<%=Constants.UPDATE_GROUP_POINT%>"+"&workUnitID="+"<%=String.valueOf(Parameters.FSOFT_WU)%>","",0,20,200);
	Menu5_2=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Operationgroup")%>",URL+"<%=Constants.GET_ORG_POINT_LIST%>","",0);
	Menu5_3=new Array("<%= languageChoose.getMessage("fi.jsp.menuDefault.Businessassurancegroup")%>",URL+"<%=Constants.GET_ORG_POINTBA_LIST%>","",0);

//Menu6=new Array("<%= "Project archive"%>","","",2);
//	Menu6_1=new Array("<%= "Archive and restore"%>",URL+"<%=Constants.PROJECT_ARCHIVE_LIST %>","",0,20,200);
//	Menu6_2=new Array("<%= "Archive history"%>",URL+"<%=Constants.PROJECT_ARCHIVE_HISTORY %>","",0);
//Menu6=new Array("<%= "Project archive"%>",URL+"<%=Constants.PROJECT_ARCHIVE_LIST %>","",0);

</SCRIPT>
<script type='text/javascript' src='jscript/menu_com.js'></script>
<noscript><%= languageChoose.getMessage("fi.jsp.menuDefault.Yourbrowserdoesnotsupportscript")%></noscript>
<script type='text/javascript' >Go();</script>
</BODY>
</HTML>
