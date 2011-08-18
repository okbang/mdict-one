<%@page import="java.util.Vector,com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.tools.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<TITLE>TailoringExisted.jsp</TITLE>
</HEAD>
<BODY onLoad="loadAdminMenu()">
<P class="ERROR"> <%=languageChoose.getMessage("fi.jsp.TailoringExisted.Theselectedprocesstailoringisd")%> </P>
<%
Vector TailoringListExisted = (Vector)session.getAttribute("tailoringexisted");
int num=TailoringListExisted.size();
%>
<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.PROCESS_TAILORING%>">

<%
for (int i =0; i < num; i++) {
TailoringInfo Info = (TailoringInfo)TailoringListExisted.elementAt(i);
%>
<TD class="ColumnLabel"><%=Info.projectCode%></TD>
<BR>
<%}%>
<P> <%=languageChoose.getMessage("fi.jsp.TailoringExisted.Pleaseremovealltailoringsrelat")%> </P>
<INPUT type="button" name="Back" value=" <%=languageChoose.getMessage("fi.jsp.TailoringExisted.Back")%> " class="BUTTON" onclick="onBack();">
</BODY>
</FORM>
<SCRIPT>
 function onBack()
 {
  frm.submit();
 }
 </SCRIPT>
 </HTML>
