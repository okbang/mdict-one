<%@page import="com.fms1.common.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,java.util.*"%>
<%@page contentType="application/vnd.ms-excel;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<HTML>
<HEAD>
<TITLE>paProjectDescExport.jsp</TITLE>
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
	response.addHeader("Content-Disposition", "attachment;filename=paProjectDec.xls");
%>

<BODY>

<P class="Title"> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.ProcessAssetsProjectDescriptio")%> <P>

<%	
	Vector vtProjectList = (Vector) session.getAttribute("projectInfoList");
	Vector vtProcessAssetsList = (Vector) session.getAttribute("processAssetsList");
	
	int j = 0;
	char cr = 13;
	char lf = 10;
%>

<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
	<TBODY>
		<TR class="header">
			<TD width="24" align="center">#</TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.Project")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.ProjectName")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.Description")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.Team")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.Customer")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.Group")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.ProjectCategory")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.ApplicationType")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.BusinessDomain")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.ContractType")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.OperatingSystem")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.DBMS")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.ProgrammingLanguage")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.Toolused")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.Hardware")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.Note")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.paProjectDescExport.Year")%> </TD>
		</TR>
<%
	if (vtProjectList != null) {
		for (int i = 0; i < vtProjectList.size(); i ++)
		{
			ProjectInfo projectInfo = (ProjectInfo)vtProjectList.elementAt(i);
			ProcessAssetsInfo processAssetsInfo = (ProcessAssetsInfo)vtProcessAssetsList.elementAt(i);
%>
		<TR>
			<TD><%=i + 1%></TD>
			<TD><%=projectInfo.getProjectCode()%></TD>
			<TD><%=projectInfo.getProjectName()%></TD>
            <TD>
        <%
        	StringTokenizer strToken = new StringTokenizer( ((projectInfo.getScopeAndObjective() == null) ? "" : projectInfo.getScopeAndObjective()), "" + cr + lf);
            String noteString = "";
            while (strToken.hasMoreElements()) {
            	noteString += strToken.nextToken();
            	noteString += "<br style='mso-data-placement:same-cell;'/>";
            }
      	%>
            <%=(noteString.equals("")) ? "N/A" : noteString%>
            </TD>
			<TD><%=processAssetsInfo.getTeam()%></TD>
            <TD><%=projectInfo.getCustomer()%></TD>
            <TD><%=projectInfo.getGroupName()%></TD>
			<TD><%=projectInfo.getLifecycle()%></TD>
			<TD><%=projectInfo.getApplicationType()%></TD>
			<TD><%=projectInfo.getBusinessDomain()%></TD>
			<TD><%=projectInfo.getContractType()%></TD>
			<TD><%=processAssetsInfo.getOsList()%></TD>
			<TD><%=processAssetsInfo.getDbmsList()%></TD>
			<TD><%=processAssetsInfo.getLanguageList()%></TD>
			<TD><%=processAssetsInfo.getSwToolList()%></TD>
			<TD><%=processAssetsInfo.getHardwareList()%></TD>
			<TD></TD>
			<TD><%=processAssetsInfo.getYear()%></TD>
		</TR>
<%
		}
	}
%>
	</TBODY>
</TABLE>

</BODY>
</HTML>