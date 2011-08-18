<%@page import="com.fms1.tools.*"%> 
<%@ page import="java.util.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">

<TITLE>error.jsp</TITLE>

<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	int iWorkUnitType = 0;
	if (session.getAttribute("workUnitType") != null){
		iWorkUnitType = Integer.parseInt((String)session.getAttribute("workUnitType"));
	}
	String strMenu = "";
	switch(iWorkUnitType){
		case 0: strMenu = "loadOrgMenu()"; break;
		case 1: strMenu = "loadGrpMenu()"; break;
		case 2: strMenu = "loadPrjMenu()"; break;
		case 3: strMenu = "loadAdminMenu()";break;
	}
%>
<BODY class="BD" onload="<%=strMenu%>">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.error.Error")%> </p>
<P class="ERROR"><%= languageChoose.getMessage(request.getParameter("error"))%></P>
<%
   	Enumeration e = request.getAttributeNames();
   	while(e.hasMoreElements()) {
      	String name = (String)e.nextElement();
      	Object attribute = request.getAttribute(name);
      	if(attribute instanceof Throwable) {
         	Throwable error = (Throwable)attribute;
%> <SPAN lang="EN-GB"><% error.printStackTrace(new java.io.PrintWriter(out));%></SPAN> <%
	   	}
	} 
%>
<p>
<INPUT type="button" name="Back" value=" <%=languageChoose.getMessage("fi.jsp.error.Back")%> "  onclick="window.history.back();" class="BUTTON"> </p>
</BODY>
</HTML>
