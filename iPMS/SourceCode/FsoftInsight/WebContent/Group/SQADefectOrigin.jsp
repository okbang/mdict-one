<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>SQADefectOrigin.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<SCRIPT language="JavaScript">
        <%@ include file="../javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	Vector vtDefectOriginInfoList = (Vector)session.getAttribute("defectOriginList");
	int nDefectOriginInfoNum = vtDefectOriginInfoList.size();
	String strStartDate = (String)session.getAttribute("startDate");
	String strEndDate = (String)session.getAttribute("endDate");
	int nWorkUnitID = Integer.parseInt((String)session.getAttribute("groupID"));
	Vector vtGroupList = (Vector)session.getAttribute("grList");
	int nNumGroup = vtGroupList.size();
	ArrayList defOrgParetoList = (ArrayList)session.getAttribute("defectOriginParetoList");
	double dTotalSize = 0;
	int nTotalReq = 0;
	int nTotalDesign = 0;
	int nTotalCoding = 0;
	int nTotalOther = 0;
	int nTotalWeighted = 0;
	double dTotalEffort = 0;
	String strOnload = nDefectOriginInfoNum > 0 ? "onload=\"makeScrollableTable('tabDefOrigin',true,'200');loadSQAMenu()\"" : "onload=\"loadSQAMenu()\"";
%>
<BODY <%=strOnload%> class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.SQAReportDefectOrigin")%> </P>
<FORM name="frm" method="POST">
 <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.From")%>  <INPUT type="text" name="startDate" maxleng="9" size="9" value="<%=strStartDate%>">
 <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.To")%>  <INPUT type="text" name="endDate" maxleng="9" size="9" value="<%=strEndDate%>">
 <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Group")%>  <SELECT name="groupID" class="Combo">
<OPTION value="0"<%=(nWorkUnitID == 0 ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.All")%> </OPTION>
<%
	for (int i = 0; i < nNumGroup; i++) {
		GroupInfo groupInfo = (GroupInfo)vtGroupList.elementAt(i);
%>
<OPTION value="<%=groupInfo.wuID%>"<%=(groupInfo.wuID == nWorkUnitID ? " selected" : "")%>><%=groupInfo.name%></OPTION>
<%
	}
%>
</SELECT>
<INPUT type="button" value=" <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Refresh")%> " class="Button" onClick="onRefresh()">
<B><A href='Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=Group/SQADefectOriginExport.jsp' target='DefectOrigin'> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.ExporttoExcel")%> </A></B>
<INPUT type="hidden" name="projectID">
</FORM> 
<TABLE cellspacing="1" class="Table" width="95%" id="tabDefOrigin">
	<COL width="28%" align="left">
	<COL width="9%" align="center">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<THEAD>
		<TR class="ColumnLabel">
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Project")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Group1")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.SizeUCP")%> </TD>
			<TD colspan="4" align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.DefectDistributionWDef")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.TotalWDef")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.DPEffortpd")%> </TD>
		</TR>
		<TR class="ColumnLabel">
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Req")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Design")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Coding")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Other")%> </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	for (int i = 0; i < nDefectOriginInfoNum; i++) {
		DefectOriginInfo defOriginInfo = (DefectOriginInfo)vtDefectOriginInfoList.elementAt(i);
		if (!Double.isNaN(defOriginInfo.getProjectSize())) {
			dTotalSize += defOriginInfo.getProjectSize();
		}
		nTotalReq += defOriginInfo.getRequirementDefect();
		nTotalDesign += defOriginInfo.getDesignDefect();
		nTotalCoding += defOriginInfo.getCodingDefect();
		nTotalOther += defOriginInfo.getOtherDefect();
		nTotalWeighted += defOriginInfo.getTotalWeightedDefect();
		dTotalEffort += defOriginInfo.getDPEffort();
%>
		<TR class="CellBGRnews" onmouseover="setPointer(this, '#66CCFF')" onmouseout="setPointer(this, '#F7F7F7')" onclick="onChooseProject(<%=defOriginInfo.getProjectId()%>)">
			<TD><%=defOriginInfo.getCode()%></TD>
			<TD><%=defOriginInfo.getGroupName()%></TD>
			<TD><%=CommonTools.formatNumber(defOriginInfo.getProjectSize(), true)%></TD>
			<TD><%=defOriginInfo.getRequirementDefect()%></TD>
			<TD><%=defOriginInfo.getDesignDefect()%></TD>
			<TD><%=defOriginInfo.getCodingDefect()%></TD>
			<TD><%=defOriginInfo.getOtherDefect()%></TD>
			<TD><%=defOriginInfo.getTotalWeightedDefect()%></TD>
			<TD><%=CommonTools.formatNumber(defOriginInfo.getDPEffort(), true)%></TD>
		</TR>
<%
	}
%>
	</TBODY>
	<TFOOT>
		<TR class="TableLeft" align="right">
			<TD align="left"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Total")%> </TD>
			<TD></TD>
			<TD><%=CommonTools.formatNumber(dTotalSize, true)%></TD>
			<TD><%=nTotalReq%></TD>
			<TD><%=nTotalDesign%></TD>
			<TD><%=nTotalCoding%></TD>
			<TD><%=nTotalOther%></TD>
			<TD><%=nTotalWeighted%></TD>
			<TD><%=CommonTools.formatNumber(dTotalEffort, true)%></TD>
		</TR>
	</TFOOT>
</TABLE>
<BR>
<TABLE cellspacing="1" class="Table" width="50%">
	<COL width="40%" align="left">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<TR class="ColumnLabel">
		<TD> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Defectorigin")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.WeightedDefect")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Aggregative")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.Percentage")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOrigin.AggregativePercentage")%> </TD>
	</TR>
<%
	for (int i = 0; i < defOrgParetoList.size(); i++) {
		DefectOriginParetoInfo defOrgParetoInfo = (DefectOriginParetoInfo)defOrgParetoList.get(i);
%>
	<TR class="CellBGRnews">
		<TD><%=languageChoose.getMessage(defOrgParetoInfo.strOrigin)%></TD>
		<TD><%=defOrgParetoInfo.nWeighted%></TD>
		<TD><%=defOrgParetoInfo.nAggregative%></TD>
		<TD><%=CommonTools.formatNumber(100 * defOrgParetoInfo.dPercentage, true)%></TD>
		<TD><%=CommonTools.formatNumber(100 * defOrgParetoInfo.dAggregativePercentage, true)%></TD>
	</TR>
<%
	}
%>	
</TABLE>
<SCRIPT language="javascript">
function onRefresh() {
	if (!mandatoryDateFld(frm.startDate, "<%= languageChoose.getMessage("fi.jsp.SQADefectOrigin.Startdate")%>") || !mandatoryDateFld(frm.endDate, "<%= languageChoose.getMessage("fi.jsp.SQADefectOrigin.Enddate")%>")) {
		return;
	}
	if (compareDate(frm.startDate.value, frm.endDate.value) == -1) {
 		alert("<%= languageChoose.getMessage("fi.jsp.SQADefectOrigin.StartDateMustBeBeforeEndDate")%>");  			
		frm.startDate.focus();
		return;
	}
	frm.action = "Fms1Servlet?reqType=<%=Constants.GET_SQA_DEFECT_ORIGIN%>";
	frm.submit();
}

function onChooseProject(nProjectID) {
	frm.projectID.value = nProjectID;
	frm.action = "Fms1Servlet?reqType=<%=Constants.DEFECT_VIEW%>";
	frm.submit();
}
</SCRIPT>
</BODY>
</HTML>
