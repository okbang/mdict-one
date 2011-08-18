<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<title>New Page 2</title>
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>

<frameset rows="*,64">
  <frame name="WOMain" src="Fms1Servlet?reqType=<%=Constants.WO_GENERAL_INFO_GET_LIST%>">
  <frame name="WOFooter" scrolling="no" noresize src="WOFooter.jsp">
  <noframes>
  <BODY onload="loadPrjMenu()">

  <p> <%=languageChoose.getMessage("fi.jsp.WOMainFrame.Thispageusesframesbutyourbrows")%> </p>

  </body>
  </noframes>
</frameset>

</html>
