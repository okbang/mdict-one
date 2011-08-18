<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>
<%@page import="com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>SQADefectOrigin.jsp</TITLE>
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
	response.addHeader("Content-Disposition", "attachment;filename=SQADefectOrigin.xls");
%>
<BODY>
<%
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
%>
<P class="Title"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.SQAReportDefectOrigin")%> <P>

<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
	<COL width="28%" align="left">
	<COL width="9%" align="center">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<COL width="9%" align="right">
	<TR class="header">
		<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.Project")%> </TD>
		<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.Group")%> </TD>
		<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.SizeUCP")%> </TD>
		<TD colspan="4" align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.DefectDistributionWDef")%> </TD>
		<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.TotalWDef")%> </TD>
		<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.DPEffortpd")%> </TD>
	</TR>
	<TR class="header">
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.Requirement")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.Design")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.Coding")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.Other")%> </TD>
	</TR>
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
	<TR>
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
	<TR align="right" class="footer">
		<TD align="left"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.Total")%> </TD>
		<TD></TD>
		<TD><%=CommonTools.formatNumber(dTotalSize, true)%></TD>
		<TD><%=nTotalReq%></TD>
		<TD><%=nTotalDesign%></TD>
		<TD><%=nTotalCoding%></TD>
		<TD><%=nTotalOther%></TD>
		<TD><%=nTotalWeighted%></TD>
		<TD><%=CommonTools.formatNumber(dTotalEffort, true)%></TD>
	</TR>
</TABLE>
<BR>
<TABLE border="1" cellspacing="1" cellpadding="0" class="Table" width="50%">
	<COL width="40%" align="left">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<TR class="header">
		<TD> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.Defectorigin")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.WeightedDefect")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.Aggregative")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.Percentage")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.SQADefectOriginExport.AggregativePercentage")%> </TD>
	</TR>
<%
	for (int i = 0; i < defOrgParetoList.size(); i++) {
		DefectOriginParetoInfo defOrgParetoInfo = (DefectOriginParetoInfo)defOrgParetoList.get(i);
%>
	<TR>
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
</BODY>
</HTML>
