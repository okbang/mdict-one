<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plOverview.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%LanguageChoose languageChoose =
	(LanguageChoose) session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu();javascript:ExportOption();" class="BD">
<%int right = Security.securiPage("Project plan", request, response);

EffortTypeInfo effortTypeInfo =
	(EffortTypeInfo) session.getAttribute("PLEffortInfo");
ProjectInfo projectInfo = (ProjectInfo) session.getAttribute("PLGeneralInfo");
Vector constraintList = (Vector) session.getAttribute("PLConstraintList");
Vector assumptionList = (Vector) session.getAttribute("PLAssumptionList");
Vector riskList = (Vector) session.getAttribute("riskList");
Vector refList = (Vector) session.getAttribute("PLReferenceList");

String totalEstimatedSize = (String) session.getAttribute("TotalEstimatedSize");
String duration = (String) session.getAttribute("duration");

session.setAttribute("change_source_page", "1"); //for risks

String className;

int i;

// HuyNH2 add code for project archive
String archiveStatus = (String) session.getAttribute("archiveStatus");
boolean isArchive = false;
if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
	if (Integer.parseInt(archiveStatus) == 4) {
		isArchive = true;
	}
}
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plOverview.ProjectplanInformation")%></P>
<TABLE cellspacing="1" class="HDR" width="95%">
	<TBODY>
		<TR>
			<TD width="10%"><%=languageChoose.getMessage("fi.jsp.plOverview.Project")%></TD>
			<TD><%=projectInfo.getProjectCode()%></TD>
			<TD align="right" valign="bottom"><A href="Fms1Servlet?reqType=<%=Constants.PL_EXPORT%>" target="about:blank"> <%=languageChoose.getMessage("fi.jsp.plOverview.ExportProjectPlan")%> </A></TD>
		</TR>
	</TBODY>
</TABLE>
<br>

<FORM name="frm_Export">
<input type="hidden" name="ProjectOverview" value="true"> 
<input type="hidden" name="ProjectDevelopmentApproach" value="true"> 
<input type="hidden" name="Estimate" value="true">
<input type="hidden" name="ProjectOrganization" value="true"> 
<input type="hidden" name="Communication_N_Reporting" value="true"> 
<input type="hidden" name="SecurityAspects" value="true"> 

<TABLE cellspacing="1" class="HDR" width="95%" id="ExportOptionTable">
	<TBODY>
		<TR>
			<TD><INPUT type="checkbox" name="projectOverview" checked="checked"/ onclick="onOK()">1. <%=languageChoose.getMessage("fi.jsp.plOverview.ProjectOverview")%></TD>
			<TD><INPUT type="checkbox" name="projectDevelopmentApproach" checked="checked"/ onclick="onOK()" >2. <%=languageChoose.getMessage("fi.jsp.plOverview.ProjectDevelopmentApproach")%></TD>
			<TD><INPUT type="checkbox" name="estimate" checked="checked"/ onclick="onOK()">3. <%=languageChoose.getMessage("fi.jsp.plOverview.Estimate")%></TD>
		</TR>
		<TR>
			<TD><INPUT type="checkbox" name="projectOrganization" checked="checked"/ onclick="onOK()">4. <%=languageChoose.getMessage("fi.jsp.plOverview.ProjectOrganization")%></TD>
			<TD><INPUT type="checkbox" name="communication_N_Reporting" checked="checked"/ onclick="onOK()">5. <%=languageChoose.getMessage("fi.jsp.plOverview.Communication_N_Reporting")%></TD>
			<TD><INPUT type="checkbox" name="securityAspects" checked="checked"/ onclick="onOK()">6. <%=languageChoose.getMessage("fi.jsp.plOverview.SecurityAspects")%><BR>
			<BR>
			</TD>
		</TR>
		<TR>
			<TD></TD>
			<TD></TD>
			<TD align="right" valign="bottom"><A href="Fms1Servlet?reqType=<%=Constants.PL_EXPORT%> target="about:blank"> EXPORT </A></TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<br>

<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.plOverview.ProjectDescription")%></CAPTION>
	<TBODY>
		<TR>
			<TD class="ColumnLabel" width="200"><%=languageChoose.getMessage("fi.jsp.plOverview.ProjectName")%></TD>
			<TD class="CellBGR3"><%=projectInfo.getProjectName()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.ProjectCode")%></TD>
			<TD class="CellBGR3"><%=projectInfo.getProjectCode()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.ContractType")%>
			</TD>
			<TD class="CellBGR3"><%=projectInfo.getContractType()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.Customer")%></TD>
			<TD class="CellBGR3"><%=projectInfo.getCustomer()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel">2nd <%=languageChoose.getMessage("fi.jsp.plOverview.Customer")%></TD>
			<TD class="CellBGR3"><%=projectInfo.getSecondCustomer()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.ProjectLevel")%></TD>
			<TD class="CellBGR3"><%=projectInfo.getProjectLevel()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.ProjectRank")%></TD>
			<TD class="CellBGR3"><%String rank;
if (projectInfo.getProjectRank() == null)
	rank = "N/A";
else if ("?".equals(projectInfo.getProjectRank()))
	rank = "Not Rank";
else
	rank = projectInfo.getProjectRank();%> <%=rank%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.Group")%></TD>
			<TD class="CellBGR3"><%=projectInfo.getGroupName()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.Division")%></TD>
			<TD class="CellBGR3"><%=projectInfo.getDivisionName()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.ProjectType")%></TD>
			<TD class="CellBGR3"><%=projectInfo.getProjectType()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.ProjectManager")%></TD>
			<TD class="CellBGR3"><%=projectInfo.getLeaderName()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.ProjectCategory")%></TD>
			<TD class="CellBGR3"><%=projectInfo.getLifecycle()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.BusinessDomain")%>
			</TD>
			<TD class="CellBGR3"><%=projectInfo.getBusinessDomain()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.ApplicationType")%>
			</TD>
			<TD class="CellBGR3"><%=projectInfo.getApplicationType()%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.ScopeAndObjective")%></TD>
			<TD class="CellBGR3"><%=ConvertString.toHtml(projectInfo.getScopeAndObjective())%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.CommittedBillableEffort")%></TD>
			<TD class="CellBGR3"><%=CommonTools.formatDouble(projectInfo.getPlannedBillableEffort())%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.CommittedCalendarEffort")%></TD>
			<TD class="CellBGR3"><%=CommonTools.formatDouble(projectInfo.getPlannedCalendarEffort())%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.CommittedEffortUsage")%></TD>
			<TD class="CellBGR3"><%=CommonTools.formatDouble(projectInfo.getPlannedEffort())%></TD>
		</TR>
		<!--
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.Inwhich")%><br>
			&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.plOverview.Development")%></TD>
			<TD class="CellBGR3"><%=CommonTools.formatDouble(effortTypeInfo.getDevelopmentEffort())%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.plOverview.Management")%></TD>
			<TD class="CellBGR3"><%=CommonTools.formatDouble(effortTypeInfo.getManagementEffort())%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.plOverview.Quality")%></TD>
			<TD class="CellBGR3"><%=CommonTools.formatDouble(effortTypeInfo.getQualityEffort())%></TD>
		</TR>
		-->
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.ActualStartDate")%></TD>
			<TD class="CellBGR3"><%=CommonTools.dateUpdate(projectInfo.getStartDate())%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.PlannedDurationDay")%></TD>
			<TD class="CellBGR3"><%=duration%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plOverview.EstimatedSizeUCP")%></TD>
			<TD class="CellBGR3"><%=totalEstimatedSize%></TD>
		</TR>
	</TBODY>
</TABLE>
<br>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"><A name="constraints"><%=languageChoose.getMessage("fi.jsp.plOverview.Constraints")%></A></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="24" align="center"> # </TD>
            <TD width = "50%"> <%=languageChoose.getMessage("fi.jsp.plOverview.Description")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.plOverview.Note")%> </TD>
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plOverview.Type")%> </TD>	
		</TR>
		<%for (i = 0; i < constraintList.size(); i++) {
	className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
	ConstraintInfo constraintInfo =
		(ConstraintInfo) constraintList.elementAt(i);%>
		<TR class="<%=className%>">
			<TD width="24" align="center"><%=i + 1%></td>
			<TD><%if (right == 3 && !isArchive) {%><a
				href="plConstraintUpdate.jsp?plConstraint_ID=<%=constraintInfo.constraintID%>"><%}%>
			<%=(constraintInfo.description == null
	|| constraintInfo.description.trim().equals(""))
	? "N/A"
	: ConvertString.toHtml(constraintInfo.description)%> <%if (right == 3 && !isArchive) {%></a><%}%></TD>
			<TD><%=(constraintInfo.note == null||constraintInfo.note.trim().equals(""))?"N/A": ConvertString.toHtml(constraintInfo.note)%></TD>
			<TD><%=languageChoose.getMessage(constraintInfo.GetNameOfType())%></TD>
		</TR>
		<%}%>
</TABLE>
<br>
<%if (right == 3 && !isArchive) {%>
<input type="submit" name="add"
	value="<%=languageChoose.getMessage("fi.jsp.plOverview.AddNew")%>"
	class="BUTTON" onclick="window.location='plConstraintAdd.jsp'">
<BR>
<BR>
<%}%>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"><A name="Assumptions"><%=languageChoose.getMessage("fi.jsp.plOverview.Assumptions")%></A></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="24" align="center"> # </TD>
            <TD width="50%"> <%=languageChoose.getMessage("fi.jsp.plOverview.Description")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.plOverview.Note")%> </TD>
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plOverview.Type")%> </TD>	
		</TR>
		<%for (i = 0; i < assumptionList.size(); i++) {
	className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
	ConstraintInfo assumptionInfo =
		(ConstraintInfo) assumptionList.elementAt(i);%>
		<TR class="<%=className%>">
			<TD width="24" align="center"><%=i + 1%></TD>
			<TD><%if (right == 3 && !isArchive) {%><a
				href="plAssumptionUpdate.jsp?plAssumption_ID=<%=assumptionInfo.constraintID%>"><%}%>
			<%=(assumptionInfo.description == null
	|| assumptionInfo.description.trim().equals(""))
	? "N/A"
	: ConvertString.toHtml(assumptionInfo.description)%> <%if (right == 3 && !isArchive) {%></a><%}%></TD>
			<TD><%=(assumptionInfo.note == null||assumptionInfo.note.trim().equals(""))?"N/A": ConvertString.toHtml(assumptionInfo.note)%></TD>
			<TD><%=languageChoose.getMessage(assumptionInfo.GetNameOfType())%></TD>
		</TR>
		<%}%>
</TABLE>
<br>
<%if (right == 3 && !isArchive) {%>
<input type="submit" name="add"
	value="<%=languageChoose.getMessage("fi.jsp.plOverview.AddNew")%>"
	class="BUTTON" onclick="window.location='plAssumptionAdd.jsp'">
<br>
<br>
<%}%>
<br>
<TABLE class="Table" cellspacing="1" width="95%">
	<CAPTION class="TableCaption"><A name="risks"><%=languageChoose.getMessage("fi.jsp.plOverview.Risks")%></A></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="5%" align="center">#</TD>
			<TD width="35%"><%=languageChoose.getMessage("fi.jsp.plOverview.Description")%></TD>
			<TD width="35%"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Source")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.riskAddnew.Probability")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.paRiskExport.Exposure")%></TD>
			<TD width="25%"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Trigger")%></TD>
		</TR>
	<%if (riskList != null) {
		for (i = 0; i < riskList.size(); i++) {
		RiskInfo risk = (RiskInfo) riskList.elementAt(i);
		className = "";
		String trimmedCondition = risk.condition;
		if (trimmedCondition != null) {
			if (trimmedCondition.length() > 50) {
				trimmedCondition = trimmedCondition.substring(0, 50) + "...";
			}
		} else {
			trimmedCondition = "N/A";
		}

		className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";%>
		<TR class="<%=className%>">
			<TD align="center"><%=i + 1%></TD>
			<TD><A
				href="Fms1Servlet?reqType=<%=Constants.RISK_UPDATE_PREP%>&riskNo=<%=i%>&riskID=<%=risk.riskID%>"><%=trimmedCondition%></A></TD>
			<TD><%=risk.sourceName%></TD>
			<TD><%=CommonTools.formatDouble(risk.probability)%></TD>
			<TD><%=CommonTools.formatDouble(risk.exposure)%></TD>
			<TD><%=ConvertString.toHtml(risk.triggerName)%></TD>
		</TR>
		
		<%}
	}%>
	</TBODY>
</TABLE>
<BR>
<%if (right == 3 && !isArchive) {
%>

<form name="frm_plRiskAddPrep" action="Fms1Servlet" method="get">
<input type="hidden" name="reqType" value="<%=Constants.RISK_IDENTIFY%>">
<input type="hidden" name="source" value="1"> 
<input type="submit" name="add" value="<%=languageChoose.getMessage("fi.jsp.plOverview.AddNew")%>" class="BUTTON">
</form>

<BR>
<BR>

<%}
%>

<BR>

<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"><A name="#References"><%=languageChoose.getMessage("fi.jsp.plOverview.References")%></A></CAPTION>
	<TR class="ColumnLabel">
		<TD width="24" align="center">#</TD>
		<TD><%=languageChoose.getMessage("fi.jsp.plOverview.Document")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.plOverview.IssuedDate")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.plOverview.Source")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.plOverview.Note")%></TD>
	</TR>
	<%for (i = 0; i < refList.size(); i++) {
	className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
	ReferenceInfo refInfo = (ReferenceInfo) refList.elementAt(i);%>
	<TR class="<%=className%>">
		<TD width="24" align="center"><%=i + 1%></td>
		<TD><%if (right == 3 && !isArchive) {%><a
			href="Fms1Servlet?reqType=<%=Constants.PL_REFERENCE_MNG%>&plReference_refID=<%=refInfo.referenceID%>"><%}%><%=refInfo.document%><%if (right == 3 && !isArchive) {%></a><%}%></td>
		<TD><%=((refInfo.issueDate == null)
	? "N/A"
	: CommonTools.dateFormat(new java.util.Date(refInfo.issueDate.getTime())))%></td>
		<TD><%=((refInfo.source == null) ? "N/A" : refInfo.source)%></td>
		<TD><%=((refInfo.note == null) ? "N/A" : refInfo.note)%></td>
	</TR>
	<%}%>
</TABLE>

<BR>

<%if (right == 3 && !isArchive) {
%>
<form name="frm_plReferenceAddPrep" action="Fms1Servlet" method="get"><input
	type="hidden" name="reqType"
	value="<%=Constants.PL_REFERENCE_ADD_PREPARE%>"> <input type="submit"
	name="add"
	value="<%=languageChoose.getMessage("fi.jsp.plOverview.AddNew")%>"
	class="BUTTON"></form>
<%}
%>

<SCRIPT language="javascript">
var isHide = true;
	function ExportOption(){
		isHide = !isHide;
	 	var ExportOptionTable = document.getElementById("ExportOptionTable");
  		if (isHide) {
    		ExportOptionTable.style.display="";
   		}else{
    		ExportOptionTable.style.display="none";
		}
    }
</SCRIPT>

<SCRIPT language="javascript">
/**
function doExport(){
	var url = "Fms1Servlet?reqType=<%=Constants.PL_EXPORT%>&ProjectOverview=" + document.frm_Export.ProjectOverview.checked + 
    			 "&ProjectDevelopmentApproach=" + document.frm_Export.ProjectDevelopmentApproach.checked + 
    			 "&Estimate=" + document.frm_Export.Estimate.checked + 
    			 "&ProjectOrganization=" + document.frm_Export.ProjectOrganization.checked + 
    			 "&Communication_N_Reporting=" + document.frm_Export.Communication_N_Reporting.checked + 
    			 "&SecurityAspects=" + document.frm_Export.SecurityAspects.checked;
     
  	url = encodeURI(url);
  	//window.location.href = url;
  	window.open("Fms1Servlet?reqType=<%=Constants.PL_EXPORT%>&ProjectOverview=" + ,'');
  return;
}

function onOK() {
	
	frm_Export.ProjectOverview.value = document.frm_Export.projectOverview.checked;
	frm_Export.ProjectDevelopmentApproach.value = document.frm_Export.projectDevelopmentApproach.checked;
	frm_Export.Estimate.value = document.frm_Export.estimate.checked;
	frm_Export.ProjectOrganization.value = document.frm_Export.projectOrganization.checked;
	frm_Export.Communication_N_Reporting.value = document.frm_Export.communication_N_Reporting.checked;
	frm_Export.SecurityAspects.value = document.frm_Export.securityAspects.checked;
	
}
**/
</SCRIPT>

</BODY>
</HTML>
