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
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.error.Error")%> </p>
<P class="ERROR"><%= request.getParameter("error")%></P>
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
