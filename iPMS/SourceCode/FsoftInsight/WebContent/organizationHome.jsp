<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>orgHome.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onload="loadBlankMenu()">
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.organizationHome.OrganizationHome")%></p>
<%
        Vector orgList = (Vector)session.getAttribute("orgList");
        if ((orgList != null)&&(orgList.size()!=0)) {
        %>
<TABLE cellspacing="1" width="60%" class="Table">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.organizationHome.Pleaseselectanorganization")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.organizationHome.OrganizationName")%></TD>
        </TR>
        <%
        	if (orgList.size() > 1) {
	        	for (int i = 0; i < orgList.size(); i++) {
	         		RolesInfo ru = (RolesInfo)orgList.elementAt(i);
	         		String classStyle = "";
	         		if (i%2 == 0)
	         			classStyle = "CellBGRnews";
	         		else
	         			classStyle = "CellBGR3";
		%>
        <TR class="<%=classStyle%>">
            <TD><A href="Fms1Servlet?reqType=<%=Constants.WORKUNIT_HOME%>&workUnitID=<%=ru.workUnitID%>"><%=ru.workunitName%></A></TD>
        </TR>
        <%
        		}
			}
			else {
	       		RolesInfo ru = (RolesInfo)orgList.elementAt(0);
	       		Fms1Servlet.callPage("Fms1Servlet?reqType=" + Constants.WORKUNIT_HOME + "&workUnitID=" + ru.workUnitID,request,response);
			}
		%>
        <TR>
            <TD colspan="2" class="TableLeft">&nbsp;</TD>
        </TR>
    </TBODY>
</TABLE>
<%			
		}
		else {
		%>
<P class="ERROR"><%=languageChoose.getMessage("fi.jsp.organizationHome.Youhavenorightinthispage") %></P>
<%
		}
		%>

</BODY>
</HTML>
