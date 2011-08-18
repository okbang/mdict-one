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
	response.addHeader("Content-Disposition", "attachment;filename=paPractice.xls");
%>
<BODY>

<P class="Title"> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.ProcessAssetsPracticeLesson")%> <P>

<%
	Vector vtPracticeList = (Vector) session.getAttribute("vtPracticeList");
	
	int j = 0;
%>

<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
    <TBODY>
        <TR class="header">
            <TD width = "24" align = "center"># </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.ProjectCode")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.ScenarioProblem")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.PracticeLessonSuggestion")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.Process")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.Type")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.Year")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.ProjectManager")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.Group")%> </TD>
        </TR>
	<%
	j = vtPracticeList.size();
	for(int i = 0 ;i < j; i++)
	{
	 	PracticeInfo info = (PracticeInfo) vtPracticeList.elementAt(i);
	%>
	
	<TR>
	<TD width = "24" align = "center"><%=i+1%></TD>
	<TD><%=info.projectCode%></TD>
	<TD><%=((info.scenario == null)?"N/A":ConvertString.toHtmlForExcel(info.scenario))%></TD>
	<TD><%=((info.practice  == null)?"N/A":ConvertString.toHtmlForExcel(info.practice))%></TD>
	<TD><%=info.category%></TD>
	<TD>
	<%
	switch(info.type)
	{
		case 0:%> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.Lesson")%> <%break; 
		case 1:%> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.Practice")%> <%break;
		case 2:%> <%=languageChoose.getMessage("fi.jsp.paPracticeExport.Suggestion")%> <%break; 
	}
	%>
	</TD>
	<%
		String start_date = CommonTools.dateFormat(info.start_date);
	%>
	<TD><%="20" + start_date.substring(7,9)%></TD>
	<TD><%=((info.projectManager  == null)?"N/A":ConvertString.toHtmlForExcel(info.projectManager))%></TD>
	<TD><%=((info.groupName  == null)?"N/A":ConvertString.toHtmlForExcel(info.groupName))%></TD>
	</TR>
	
	<%
	}
	%>

</TABLE>
</BODY>
</HTML>
