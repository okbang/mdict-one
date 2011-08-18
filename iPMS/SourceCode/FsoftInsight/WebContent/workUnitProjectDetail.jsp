<%@page import="com.fms1.tools.*"%>
<%@page import="com.fms1.infoclass.*,com.fms1.infoclass.group.* ,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<TITLE>workUnitProjectDetail.jsp</TITLE>
	<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
	<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
	<SCRIPT language="javascript"><%@ include file="javaFns.jsp"%></SCRIPT>
	<TITLE>workUnitProjectDetail.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload = "loadAdminMenu()" class = "BD">
<BR>
<FORM name="frm" method="POST">
<%
	WorkUnitInfo wuInfo = (WorkUnitInfo)request.getAttribute("workUnitInfor");

	ProjectInfo projectInfo = (ProjectInfo)request.getAttribute("WUprojectInfo");
%>
<DIV align = "left">
	<TABLE cellspacing="1" class = "Table" width="95%">
	<CAPTION align="left" class = "TableCaption" ><%= languageChoose.getMessage("fi.jsp.workUnitDetail.WorkUnitProjectDetail")%></CAPTION>
		<TBODY>
			<INPUT size="20" type="hidden" name="hideProjectID" value="<%=projectInfo.getProjectId()%>">
			<INPUT size="20" type="hidden" name="hideWorkUnitID" value="<%=wuInfo.workUnitID%>">
			<TR>
				<TD class = "ColumnLabel" width="260"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.Group")%></TD>
				<TD class = "CellBGR3"><%=ConvertString.toHtml(projectInfo.getGroupName())%></TD>
			</TR>
			<TR>
				<TD class = "ColumnLabel" width="260"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.ProjectCode")%></TD>
				<TD class = "CellBGR3"><%=ConvertString.toHtml(projectInfo.getProjectCode())%></TD>
			</TR>
			<TR>
				<TD class = "ColumnLabel" width="260"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.ProjectName")%></TD>
				<TD class = "CellBGR3"><%=ConvertString.toHtml(projectInfo.getProjectName())%></TD>
			</TR>
			<TR>
				<TD class = "ColumnLabel" width="260"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.ProjectCustomer")%></TD>
				<TD class = "CellBGR3"><%=ConvertString.toHtml(projectInfo.getCustomer())%></TD>
			</TR>
			<TR>
				<TD class = "ColumnLabel" width="260"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.ProjectCategory")%></TD>
				<TD class = "CellBGR3"><%=ConvertString.toHtml(projectInfo.getLifecycle())%></TD>
			</TR>
			<TR>
				<TD class = "ColumnLabel" width="260"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.ProjectType")%></TD>
				<TD class = "CellBGR3"><%=languageChoose.getMessage(projectInfo.getProjectType())%></TD>
			</TR>
			<TR>
				<TD class = "ColumnLabel" width="260"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.ProjectManager")%></TD>
				<TD class = "CellBGR3"><%=ConvertString.toHtml(projectInfo.getLeader())%></TD>
			</TR>
			<TR>
				<TD class = "ColumnLabel" width="260"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.PlanedStartDate")%></TD>
				<TD class = "CellBGR3"><%=CommonTools.dateFormat(projectInfo.getPlanStartDate())%></TD>
			</TR>
			<TR>
				<TD class = "ColumnLabel" width="260"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.PlanedEndDate")%></TD>
				<TD class = "CellBGR3"><%=CommonTools.dateFormat(projectInfo.getPlanFinishDate())%></TD>
			</TR>
			<TR>
				<TD class = "ColumnLabel" width="260"><%=languageChoose.getMessage("fi.jsp.workUnitDetail.ProjectScopeObject")%></TD>
				<TD class = "CellBGR3"><%=ConvertString.toHtml(projectInfo.getScopeAndObjective())%></TD>
			</TR>
			<TR>
				<TD class = "ColumnLabel" width="260"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectStatus")%></TD>
				<TD class = "CellBGR3"><%=ConvertString.toHtml(projectInfo.getStatusName())%></TD>
			</TR>		
		</TBODY>
	</TABLE>
</DIV>
<BR>
<p align="left">
<%if (!wuInfo.protect){%>
<INPUT type="button" name="Delete" value="<%=languageChoose.getMessage("fi.jsp.workUnitDetail.Delete")%>"  onclick="doDelete()" class="BUTTON"> 
<%}%>
<INPUT type="button" name="Back" value="<%=languageChoose.getMessage("fi.jsp.workUnitDetail.Back")%>"  onclick="doBack()" class="BUTTON"></p>
</FORM>
<SCRIPT language="javascript">
	function doBack() {
		doIt(<%=Constants.GET_WORK_UNIT_LIST%>);
	}
<%
	if (wuInfo.getCheckDelete()) {
%>
		function doDelete(){
			if(window.confirm("<%=languageChoose.getMessage("fi.jsp.workUnitDetail.AreYouSuretoDeleteThisWorkunit")%>")){
				frm.action = "Fms1Servlet?reqType=<%=Constants.DELETE_WORK_UNIT_PROJECT%>";
				frm.submit();
			}
			else {
				return;
			}
		}
<%
	}
	else {
%>
		function doDelete(){
			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitDetail.YouMustRemoveProjectAssignmentBeforeDelete")%>");
			return;
		}
<%
	}
%>
</SCRIPT>
</BODY>
</HTML>