<%@page import="com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.*" contentType="application/vnd.ms-excel"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>SQADefectType.jsp</TITLE>
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
	response.addHeader("Content-Disposition", "attachment;filename=sqaDefectType.xls");
%>
<BODY>

<%
	Vector vtDefectTypeInfoList = (Vector)session.getAttribute("defectTypeList");
	int nDefectTypeInfoNum = vtDefectTypeInfoList.size();
	
	String strStartDate = (String)session.getAttribute("startDate");
	String strEndDate = (String)session.getAttribute("endDate");
	
	int nWorkUnitID = Integer.parseInt((String)session.getAttribute("groupID"));
	Vector vtGroupList = (Vector)session.getAttribute("grList");
	int nNumGroup = vtGroupList.size();
	
	ArrayList defTypeParetoList = (ArrayList)session.getAttribute("defectTypeParetoList");

	ArrayList defFunctionalityParetoList = (ArrayList)session.getAttribute("defectFunctionalityParetoList");
	
	double dTotalSize = 0;
	
	int nTotalReq = 0;
	int nTotalFeature = 0;
	int nTotalCoding = 0;
	int nTotalBusiness = 0;
	int nTotalOtherFunc = 0;
	int nTotalInterface = 0;
	int nTotalCodingStandard = 0;
	int nTotalDocument = 0;
	int nTotalDesign = 0;
	int nTotalOther = 0;
	int nTotalWeighted = 0;
	
%>
<P class="Title"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.SQAReportDefectType")%> <P>
<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
	<COL width="12%" align="left">
	<COL align="center">
	<COL align="right">
	<COL align="right">
	<COL align="right">
	<COL align="right">
	<COL align="right">
	<COL align="right">
	<COL align="right">
	<COL align="right">
	<COL align="right">
	<COL align="right">
	<COL align="right">
	<COL align="right">
	<THEAD>
		<TR class="header">
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Project")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Group")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.UCP")%> </TD>
			<TD colspan="5" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Functionality")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Interface")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.CodingStandard")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Document")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Designissue")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Othertype")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.TotalWD")%> </TD>
		</TR>
		<TR class="header">
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.ReqMisunderst.")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.FeatureMissing")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Codinglogic")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Businesslogic")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.OtherFunct.")%> </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	for (int i = 0; i < nDefectTypeInfoNum; i++) {
		DefectTypeInfo defectTypeInfo = (DefectTypeInfo)vtDefectTypeInfoList.elementAt(i);
		if (!Double.isNaN(defectTypeInfo.dProjectSize)) {
			dTotalSize += defectTypeInfo.dProjectSize;
		}

		nTotalReq += defectTypeInfo.nRequirementDefect;
		nTotalFeature += defectTypeInfo.nFeatureDefect;
		nTotalCoding += defectTypeInfo.nCodingLogicDefect;
		nTotalBusiness += defectTypeInfo.nBusinessDefect;
		nTotalOtherFunc += defectTypeInfo.nOtherFuncDefect;
		nTotalInterface += defectTypeInfo.nInterfaceDefect;
		nTotalCodingStandard += defectTypeInfo.nCodingStandardDefect;
		nTotalDocument += defectTypeInfo.nDocumentDefect;
		nTotalDesign += defectTypeInfo.nDesignDefect;
		nTotalOther += defectTypeInfo.nOtherDefect;
		
		nTotalWeighted += defectTypeInfo.getTotalWeightedDefect();
%>
		<TR>
			<TD><%=defectTypeInfo.strCode%></TD>
			<TD><%=defectTypeInfo.strGroupName%></TD>
			
			<TD><%=CommonTools.formatNumber(defectTypeInfo.dProjectSize, true)%></TD>
			
			<TD><%=defectTypeInfo.nRequirementDefect%></TD>
			<TD><%=defectTypeInfo.nFeatureDefect%></TD>
			<TD><%=defectTypeInfo.nCodingLogicDefect%></TD>
			<TD><%=defectTypeInfo.nBusinessDefect%></TD>
			<TD><%=defectTypeInfo.nOtherFuncDefect%></TD>
			<TD><%=defectTypeInfo.nInterfaceDefect%></TD>
			<TD><%=defectTypeInfo.nCodingStandardDefect%></TD>
			<TD><%=defectTypeInfo.nDocumentDefect%></TD>
			<TD><%=defectTypeInfo.nDesignDefect%></TD>
			<TD><%=defectTypeInfo.nOtherDefect%></TD>
			
			<TD><%=defectTypeInfo.getTotalWeightedDefect()%></TD>
		</TR>
<%
	}
%>
	</TBODY>
	<TFOOT>
		<TR  align="right" class="footer">
			<TD align="left"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Total")%> </TD>
			<TD></TD>
			<TD><%=CommonTools.formatNumber(dTotalSize, true)%></TD>
			<TD><%=nTotalReq%></TD>
			<TD><%=nTotalFeature%></TD>
			<TD><%=nTotalCoding%></TD>
			<TD><%=nTotalBusiness%></TD>
			<TD><%=nTotalOtherFunc%></TD>
			<TD><%=nTotalInterface%></TD>
			<TD><%=nTotalCodingStandard%></TD>
			<TD><%=nTotalDocument%></TD>
			<TD><%=nTotalDesign%></TD>
			<TD><%=nTotalOther%></TD>
			<TD><%=nTotalWeighted%></TD>
		</TR>
	</TFOOT>
</TABLE>
<BR>
<TABLE border="1" cellspacing="1" cellpadding="0" width="98%">
	<COL width="13%" align="left">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<TR class="header">
		<TD> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Defecttype")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.WeightedDefect")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Aggregative")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Percentage")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.AggregativePercentage")%> </TD>
	</TR>
<%
	for (int i = 0; i < defTypeParetoList.size(); i++) {
		DefectTypeParetoInfo defTypeParetoInfo = (DefectTypeParetoInfo)defTypeParetoList.get(i);
%>
	<TR>
		<TD><%=languageChoose.getMessage(defTypeParetoInfo.strType)%></TD>
		<TD><%=defTypeParetoInfo.nWeighted%></TD>
		<TD><%=defTypeParetoInfo.nAggregative%></TD>
		<TD><%=CommonTools.formatNumber(100 * defTypeParetoInfo.dPercentage, true)%></TD>
		<TD><%=CommonTools.formatNumber(100 * defTypeParetoInfo.dAggregativePercentage, true)%></TD>
	</TR>
<%
	}
%>	
</TABLE>
<BR>
<TABLE border="1" cellspacing="1" cellpadding="0" class="Table" width="98%">
	<COL width="14%" align="left">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<TR class="header">
		<TD> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Defectbyfunctionality")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.WeightedDefect1")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Aggregative1")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.Percentage1")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectTypeExport.AggregativePercentage1")%> </TD>
	</TR>
<%
	for (int i = 0; i < defFunctionalityParetoList.size(); i++) {
		DefectTypeParetoInfo defTypeParetoInfo = (DefectTypeParetoInfo)defFunctionalityParetoList.get(i);
%>
	<TR>
		<TD><%=languageChoose.getMessage(defTypeParetoInfo.strType)%></TD>
		<TD><%=defTypeParetoInfo.nWeighted%></TD>
		<TD><%=defTypeParetoInfo.nAggregative%></TD>
		<TD><%=CommonTools.formatNumber(100 * defTypeParetoInfo.dPercentage, true)%></TD>
		<TD><%=CommonTools.formatNumber(100 * defTypeParetoInfo.dAggregativePercentage, true)%></TD>
	</TR>
<%
	}
%>	
</TABLE>
</BODY>
</HTML>
