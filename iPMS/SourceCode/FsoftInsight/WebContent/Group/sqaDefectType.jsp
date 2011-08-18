<%@page  contentType="text/html;charset=UTF-8" import="com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<SCRIPT language="JavaScript">
        <%@ include file="../javaFns.jsp"%>
</SCRIPT>
<TITLE>sqaDefectType.jsp</TITLE>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
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
	
	String strOnload = nDefectTypeInfoNum > 15 ? "onload=\"makeScrollableTable('tabDefType',true,'400');loadSQAMenu()\"" : "onload=\"loadSQAMenu()\"";
%>
<BODY <%=strOnload%> class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.SQAReportDefectType")%> </P>
<FORM name="frm" method="POST">
 <%=languageChoose.getMessage("fi.jsp.sqaDefectType.From")%>  <INPUT type="text" name="startDate" maxleng="9" size="9" value="<%=strStartDate%>">
 <%=languageChoose.getMessage("fi.jsp.sqaDefectType.To")%>  <INPUT type="text" name="endDate" maxleng="9" size="9" value="<%=strEndDate%>">

 <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Group")%>  <SELECT name="groupID" class="Combo">
<OPTION value="0"<%=(nWorkUnitID == 0 ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.All")%> </OPTION>
<%
	for (int i = 0; i < nNumGroup; i++) {
		GroupInfo groupInfo = (GroupInfo)vtGroupList.elementAt(i);
%>
<OPTION value="<%=groupInfo.wuID%>"<%=(groupInfo.wuID == nWorkUnitID ? " selected" : "")%>><%=groupInfo.name%></OPTION>
<%
	}
%>
</SELECT>

<INPUT type="button" value=" <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Refresh")%> " class="Button" onClick="onRefresh()">
<B><A href='Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=Group/sqaDefectTypeExport.jsp' target='DefectType'> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.ExporttoExcel")%> </A></B>
<INPUT type="hidden" name="projectID">
</FORM> 

<BR>

<TABLE cellspacing="1" class="Table" id="tabDefType" width="1000">
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
		<TR class="ColumnLabel">
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Project")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Group1")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.UCP")%> </TD>
			<TD colspan="5" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Functionality")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Interface")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.CodingStandard")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Document")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Designissue")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Othertype")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.TotalWD")%> </TD>
		</TR>
		<TR class="ColumnLabel">
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.ReqMisunderst.")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.FeatureMissing")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Codinglogic")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Businesslogic")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.OtherFunct.")%> </TD>
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
		<TR class="CellBGRnews" onmouseover="setPointer(this, '#66CCFF')" onmouseout="setPointer(this, '#F7F7F7')" onclick="onChooseProject(<%=defectTypeInfo.nProjectId%>)">
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
		<TR class="TableLeft" align="right">
			<TD align="left"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Total")%> </TD>
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
<TABLE cellspacing="1" class="Table" width="98%">
	<COL width="13%" align="left">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<TR class="ColumnLabel">
		<TD> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Defecttype")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.WeightedDefect")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Aggregative")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Percentage")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.AggregativePercentage")%> </TD>
	</TR>
<%
	for (int i = 0; i < defTypeParetoList.size(); i++) {
		DefectTypeParetoInfo defTypeParetoInfo = (DefectTypeParetoInfo)defTypeParetoList.get(i);
%>
	<TR class="CellBGRnews">
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
<TABLE cellspacing="1" class="Table" width="98%">
	<COL width="14%" align="left">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<COL width="15%" align="right">
	<TR class="ColumnLabel">
		<TD> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Defectbyfunctionality")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.WeightedDefect1")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Aggregative1")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.Percentage1")%> </TD>
		<TD align="center"> <%=languageChoose.getMessage("fi.jsp.sqaDefectType.AggregativePercentage1")%> </TD>
	</TR>
<%
	for (int i = 0; i < defFunctionalityParetoList.size(); i++) {
		DefectTypeParetoInfo defTypeParetoInfo = (DefectTypeParetoInfo)defFunctionalityParetoList.get(i);
%>
	<TR class="CellBGRnews">
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
<SCRIPT language="javascript">
function onRefresh() {
	if (!mandatoryDateFld(frm.startDate, "<%= languageChoose.getMessage("fi.jsp.sqaDefectType.Startdate")%>") || !mandatoryDateFld(frm.endDate, "<%= languageChoose.getMessage("fi.jsp.sqaDefectType.Enddate")%>")) {
		return;
	}
	if (compareDate(frm.startDate.value, frm.endDate.value) == -1) {
		alert("<%= languageChoose.getMessage("fi.jsp.sqaDefectType.Startdatemustbebeforeenddate")%>");
		frm.startDate.focus();
		return;
	}
	frm.action = "Fms1Servlet?reqType=<%=Constants.SQA_DEFECT_TYPE%>";
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
