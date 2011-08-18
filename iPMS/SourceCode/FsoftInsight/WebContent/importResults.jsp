<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>

<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>

<BODY onload="loadPrjMenu()" class="BD" >
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.importResults.Importresults")%> </p>
<P><%=request.getParameter("importResult").toString()%></P>

<p><INPUT type="button" name="Back" value=" <%=languageChoose.getMessage("fi.jsp.importResults.Back")%> "  onclick="doIt(<%=Constants.GET_WORK_UNIT_LIST%>)" class="BUTTON"> </p>

</BODY>
</HTML>
