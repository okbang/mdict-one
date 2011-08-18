<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>
<%@page import="com.fms1.common.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,java.util.*"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>paRiskExport.jsp</TITLE>
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
%>
<BODY>

<P class="Title"><%=languageChoose.paramText(new String[]{"fi.jsp.ProjectPointPageExport.~PARAM1_NAME~projectpoints",(String)session.getAttribute("workUnitName")})%><P>
<%

	WorkUnitInfo wuInfo = (WorkUnitInfo) session.getAttribute("workUnit");
	String CurrentMonth = (String) session.getAttribute("CurrentMonth");
	String PreviousMonth1 = (String) session.getAttribute("PreviousMonth1");

	Vector vtProject1 = (Vector) session.getAttribute("vtProject1");
	Vector vtProject2 = (Vector) session.getAttribute("vtProject2");

	Vector vtProjectPoint1 = (Vector) session.getAttribute("vtProjectPoint1");
	Vector vtProjectPoint2 = (Vector) session.getAttribute("vtProjectPoint2");
	
	Vector vtGroupRole = (Vector) session.getAttribute("groupList");
	Vector vtOrgRole = (Vector) session.getAttribute("orgList");

	String strProjectType = (String) session.getAttribute("cboProjectType");
	String strProjectCategory = (String) session.getAttribute("cboProjectCategory");
	String strProjectStatus = (String) session.getAttribute("cboProjectStatus");
	
	if (strProjectType == null)
		strProjectType = "-1";

	if (strProjectCategory == null)
		strProjectCategory = "-1";

	if (strProjectStatus == null)
		strProjectStatus = "-1";

%> 

<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
<CAPTION align="left" class="TableCaption"></CAPTION>
    <TBODY>
        <TR class="header">
            <TD width = "24" align = "center"># </TD>
			<TD width="290"> <%=languageChoose.getMessage("fi.jsp.ProjectPointPageExport.Project")%> </TD>
			<TD width="100"><%=PreviousMonth1%></TD>
			<TD width="100"><%=CurrentMonth%></TD>
			<TD width="100"> <%=languageChoose.getMessage("fi.jsp.ProjectPointPageExport.Evolution")%> </TD>
		</TR>
<%
	int index = 1;
	ProjectPointInfo ppointInfo1 = null, ppointInfo2 = null;
	ProjectInfo projectInfo1 = null, projectInfo2 = null;
	String projectCode1 = null, projectCode2 = null;
	
	for (int i = 0; i < vtProject2.size(); i ++)
	{
		projectInfo2 = (ProjectInfo) vtProject2.elementAt(i);
		projectCode2 = projectInfo2.getProjectCode();
		ppointInfo2 = (ProjectPointInfo) vtProjectPoint2.elementAt(i);
		boolean getAProject = false;
		
		for (int j = 0; j < vtProject1.size(); j ++)
		{
			projectInfo1 = (ProjectInfo) vtProject1.elementAt(j);
			projectCode1 = projectInfo1.getProjectCode();
			
			if (projectCode2.equalsIgnoreCase(projectCode1))
			{
				getAProject = true;
				ppointInfo1 = (ProjectPointInfo) vtProjectPoint1.elementAt(j);
%>
		<TR>
			<TD class="CellBGR3"><%=index%></TD>
			<TD class="CellBGR3"><%=projectCode2%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(ppointInfo2.ProjectPoint)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(ppointInfo1.ProjectPoint)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(ppointInfo1.ProjectPoint - ppointInfo2.ProjectPoint)%></TD>
		</TR>
<%
				index = index + 1;
				break;
			}
		}
		
		if (!getAProject)
		{
			index = index + 1;
%>
		<TR>
			<TD class="CellBGR3"><%=index%></TD>
			<TD class="CellBGR3"><%=projectCode2%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(ppointInfo2.ProjectPoint)%></TD>
			<TD class="CellBGRNews">N/A</TD>
			<TD class="CellBGRNews">N/A</TD>
		</TR>
<%		}
	}

	index = index - 1;
	for (int i = 0; i < vtProject1.size(); i ++)
	{
		projectInfo1 = (ProjectInfo) vtProject1.elementAt(i);
		projectCode1 = projectInfo1.getProjectCode();
		ppointInfo1 = (ProjectPointInfo) vtProjectPoint1.elementAt(i);
		boolean doNotGetIt = false;
		
		for (int j = 0; j < vtProject2.size(); j ++)
		{
			projectInfo2 = (ProjectInfo) vtProject2.elementAt(j);
			projectCode2 = projectInfo2.getProjectCode();
			
			if (projectCode1.equalsIgnoreCase(projectCode2))
			{
				doNotGetIt = true;
				break;
			}
		}
		if (!doNotGetIt)
		{
			index = index + 1;
%>
		<TR>
			<TD class="CellBGR3"><%=index%></TD>
			<TD class="CellBGR3"><%=projectCode1%></TD>
			<TD class="CellBGRNews">N/A</TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(ppointInfo1.ProjectPoint)%></TD>
			<TD class="CellBGRNews">N/A</TD>
		</TR>
<%		}
	}
%>
	</TBODY>
</TABLE>

</BODY>
</HTML>
