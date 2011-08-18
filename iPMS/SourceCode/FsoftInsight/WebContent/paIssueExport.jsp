<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %><%@page import="com.fms1.common.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>paTailoringExport.jsp</TITLE>
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
	response.addHeader("Content-Disposition", "attachment;filename=paIssue.xls");
%>
<BODY>

<P class="Title"> <%=languageChoose.getMessage("fi.jsp.paIssueExport.ProcessAssetsIssues")%> <P>

<%
	Vector vtIssueList = (Vector) session.getAttribute("vtIssueList");
	Vector vtProcess = (Vector)session.getAttribute("vtProcess");
	
	int j = 0;
%>

<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
    <TBODY>
        <TR class="header">
            <TD width = "24" align = "center"># </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.ProjectCode")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Description")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Category")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Priority")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Type")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Processrelated")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Owner")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.StartDate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.DueDate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.CommentSolution")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.ClosedDate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Reference")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Note")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Year")%> </TD>
        </TR>
	<%
	j = vtIssueList.size();
	for(int i = 0 ;i < j; i++)
	{
	 	IssueInfo info = (IssueInfo) vtIssueList.elementAt(i);
	%>
	
	<TR>
	<TD width = "24" align = "center"><%=i+1%></TD>
	<TD><%=info.projectCode%></TD>
	<TD><%=((info.description == null)?"N/A":ConvertString.toHtmlForExcel(info.description))%></TD>
	<TD></TD>
	<TD><%=info.getPriorityName()%></TD>
	<TD>
	<%
	switch(info.typeID)
	{
		case 0:%> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Organization")%> <%break; 
		case 1:%> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Customer")%> <%break;
		case 2:%> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Resource")%> <%break; 
		case 3:%> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Operation")%> <%break; 
		case 4:%> <%=languageChoose.getMessage("fi.jsp.paIssueExport.Others")%> <%break; 
	}
	%>
	</TD>
	<TD>
   	<%
   		String processName = "Other";
   		for(int k=0;k<vtProcess.size();k++)
   		{
   			ProcessInfo psInfo=(ProcessInfo)vtProcess.get(k);
   			if (psInfo.processId == info.processId)
   			{
				processName = psInfo.name;
       			break;
   			}
   		}
   	%>
	<%=processName%>
	</TD>
	<TD><%=info.owner%></TD>
	<TD><%=CommonTools.dateFormat(info.startDate)%></TD>
	<TD><%=CommonTools.dateFormat(info.dueDate)%></TD>
	<TD><%=((info.comment == null)?"N/A":ConvertString.toHtmlForExcel(info.comment))%></TD>
	<TD><%=CommonTools.dateFormat(info.closeDate)%></TD>
	<TD><%=((info.reference == null)?"N/A":ConvertString.toHtmlForExcel(info.reference))%></TD>
	<TD></TD>
	<%
		String start_date = CommonTools.dateFormat(info.start_date);
	%>
	<TD><%="20" + start_date.substring(7,9)%></TD>

	</TR>
	
	<%
	}
	%>

</TABLE>
</BODY>
</HTML>
