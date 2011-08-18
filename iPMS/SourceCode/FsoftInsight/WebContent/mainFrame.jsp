<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>FSOFT Insight</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
 
<FRAMESET rows="92,*" frameborder="NO" border="0">
    <FRAME
        src="Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=header.jsp" name="header" noresize scrolling="no" >
    <FRAMESET cols="138,*" border="0" frameborder="NO">
        <FRAME src="menuBlank.html" name="menu" noresize scrolling="no">
        <%
        String strPage=request.getParameter("main");
        if (strPage.equals("groupHome.jsp")){%>
        <FRAME name="main" src="Fms1Servlet?reqType=<%=Constants.HEADER_GROUP%>" noresize scrolling="yes">
        <%}else{%>
        <FRAME name="main" src="Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=<%=strPage%>" noresize scrolling="yes">
        <%}%>
        
    </FRAMESET>

</FRAMESET>
</HTML>
