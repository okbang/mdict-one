<%@page import="com.fms1.tools.*"%> 
<%@ page contentType="application/vnd.ms-excel" %><%@page import="com.fms1.common.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,java.util.*"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>defectCommExport.jsp</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<STYLE type="text/css">
.header {
    FONT-WEIGHT: bold;
    BACKGROUND-COLOR: #C0C0C0;
}
.footer {
    FONT-WEIGHT: bold;
}
.Title {
	font-weight: bold;
	font-size: 14pt;
	margin-left: 0px;
	margin-top: 20px
}

</STYLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	response.addHeader("Content-Disposition", "attachment;filename=CommonDefects.xls");
%>
<BODY>

<%
	Vector cdVt=(Vector)session.getAttribute("vtCommDefect");
	String projectCode=(String)session.getAttribute("projCode");
	String groupName = (String) session.getAttribute("groupName");
%>
<P class="Title"><%= languageChoose.getMessage("fi.jsp.CommonDefects.Project") + " : " + projectCode + " - " + languageChoose.getMessage(groupName)%><P>

<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
    <CAPTION align="left"><B> <%=languageChoose.getMessage("fi.jsp.CommonDefects.Commondefects")%> </B></CAPTION>
    <TBODY>
        <TR class="header">
            <TD width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.CommonDefects.Commdef.Code")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.CommonDefects.Commondefect")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.CommonDefects.Defecttype")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.CommonDefects.RootCause")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.CommonDefects.CauseCategory")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.CommonDefects.DPCode")%> </TD>
        </TR>
        <%
        	for(int i=0;i<cdVt.size();i++){
  				CommDefInfo info = (CommDefInfo) cdVt.get(i);
        %>
        <TR>
            <TD align="center"><%=i+1%></TD>
            <TD><%=info.commonDefCode%></TD>
            <TD><%=info.commdef%></TD>
            <TD>
            <%
            	switch (info.defecttype) {
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.01FunctionalityOther")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.02UserInterface")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.03Performance")%> <%break;
            	case 4: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.04Designissue")%> <%break;
            	case 5: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.05Codingstandard")%> <%break;
            	case 6: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.06Document")%> <%break;
            	case 7: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.07DataDatabaseintegrity")%> <%break;
            	case 8: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.08SecurityAccessControl")%> <%break;
            	case 9: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.09Portability")%> <%break;
            	case 10: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.10Other")%> <%break;
            	case 11: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.11Tools")%> <%break;
            	case 12: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.011Reqmisunderstanding")%> <%break;
            	case 13: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.012Featuremissing")%> <%break;
            	case 14: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.013Codinglogic")%> <%break;
            	case 15: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.014Businesslogic")%> <%break;
            	}
            %>
            </TD>
            <TD><%=(info.rootcause == null)?  "N/A": info.rootcause%></TD>
            <TD>
            <%
            	switch (info.causecate) {
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.Training")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.Communication")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.Oversight")%> <%break;
            	case 4: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.Understanding")%> <%break;
            	case 5: %> <%=languageChoose.getMessage("fi.jsp.CommonDefects.Other")%> <%break;
            	}
            %>
            </TD>
            <TD><%=(info.dpcode == null)?"N/A": info.dpcode%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
</BODY>
</HTML>
