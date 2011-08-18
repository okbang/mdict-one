<%@page import="com.fms1.tools.*"%> 
<%@ page import="java.util.*,com.fms1.web.*,com.fms1.infoclass.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="javascript" src="jscript/BigInt.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>redirect</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="doRedirect()">
<FORM method="POST" action="" name="frmLogin">
<%
LinkInfo link=(LinkInfo)session.getAttribute("link");

switch(link.type){
case Constants.GET_DMS:%>
	<INPUT type="hidden" name="hidAction" value="ViewDefectListing">
<%break;case Constants.GET_TIMESHEET:%>
	<INPUT type="hidden" name="hidAction" value="AA">
	<INPUT type="hidden" name="hidActionDetail" value="TimesheetLogin">
	<INPUT  type="hidden" name="Location" value="0">
<%break;case Constants.GET_DASHBOARD:%>
	<INPUT type="hidden" name="hidAction" value="DashboardHomepage">
<%break;case Constants.GET_NCMS:%>
	<INPUT type="hidden" name="hidAction" value="NCMSLogin">
	<INPUT type="hidden" name="cboMode" value="0">
<%break;case Constants.GET_CALLLOG:%>
	<INPUT type="hidden" name="hidAction" value="CallLogLogin">
	<INPUT type="hidden" name="cboMode" value="1">
<%break;case Constants.GET_ITSPROJECT:%>
<input type="hidden" name="pid" value="<%=(String) session.getAttribute("workUnitID")%>">
<%case Constants.GET_ITS:%>
	<input type="hidden" name="login" value="<%=link.name%>">
	<input type="hidden" name="password" value="<%=link.password%>">
	<input type="hidden" name="its" value="3">
<%break;}%>
<INPUT type="hidden" name="txtAccount" value="<%=link.name%>">
<INPUT type="hidden" name="txtPassword" value="<%=link.password%>">
<INPUT type="hidden" name="key" value="<%=link.id%>">
</FORM>
<p>
<SCRIPT language="javascript">
	function doRedirect(){
		frmLogin.action="<%=link.link%>";
		frmLogin.submit();
	}

</SCRIPT>
</BODY>
</HTML>
