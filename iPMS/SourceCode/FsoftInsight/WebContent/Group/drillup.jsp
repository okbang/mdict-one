<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.Vector,java.util.Calendar,com.fms1.tools.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<LINK href="../stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="../jscript/javaFns.js"></SCRIPT>

<TITLE>Drill up</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY>
<INPUT type="BUTTON" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.drillup.Drillup")%> " onclick="dome()">
</BODY>
<SCRIPT language="JavaScript">
function dome(){
window.open('../Fms1Servlet?reqType=<%=Constants.TASK_DRILLUP+"&"+request.getQueryString()%>','main');
window.close();
}
</SCRIPT>
</HTML>
