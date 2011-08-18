<%@page import="com.fms1.tools.*"%>
<%@page import="com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.Vector" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>

<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<TITLE>ProjectList.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	Vector vtInProgressProjects = (Vector)session.getAttribute("inProgressProjectList");
	String strMetricID = (String)session.getAttribute("metricID");
	MetricDescInfo metricDescInfo = (MetricDescInfo)session.getAttribute("metricDescInfo");
	String strWorkUnitName = (String)session.getAttribute("workUnitName");
	String strParentWorkUnitID = (String)session.getAttribute("parentWorkUnitID");
	ReportMonth actualMonth = (ReportMonth)session.getAttribute("reportMonth");
	int month = actualMonth.getMonth();
	int year = actualMonth.getYear();
%>
<BODY class="BD" onLoad="loadGrpMenu();">
<TABLE width="95%">
	<TBODY>
		<TR>
			<TD><P class="TITLE"><%=strWorkUnitName%>&nbsp;<%=languageChoose.paramText(new String[]{"fi.jsp.ProjectList.~PARAM1_METRICNAME~__as__of__~PARAM2_MONTH~__~PARAM3_YEAR~", languageChoose.getMessage(metricDescInfo.metricName), CommonTools.getMonth(month), String.valueOf(year)})%> </P></TD>
			<TD align="right"></TD>
		</TR>
	</TBODY>
</TABLE>
<BR>

<FORM name="frm" method="POST">
<TABLE width="95%">
	<TBODY>
		<TR>
			<TD width="50%">
				<TABLE width="100%">
				<TR>
					<TD>
					<FIELDSET>
						<LEGEND class="LargeText"><%=languageChoose.getMessage("fi.jsp.ProjectList.Legend")%></LEGEND>
						<FONT color="gray" size="1"><LI><%=languageChoose.getMessage("fi.jsp.ProjectList.Clickonworkunittodrilldown")%></LI></FONT>
					</FIELDSET>
					</TD>
				</TR>
				</TABLE>
			</TD>
			<TD align="center">
				<TABLE cellspacing="4" cellpadding="4">
					<TBODY>
						<TR>
							<TD class="NormalText"><%=languageChoose.getMessage("fi.jsp.ProjectList.Choosemonth")%><SELECT name="month" class="COMBO">
									<%
										for (int i = 1; i <= 12; i++) {
									%>
									<OPTION value="<%=i%>"<%=(i == month ? " selected" : "")%>	><%=CommonTools.getMonth(i)%></OPTION>
									<%
										}
									%>
								</SELECT>
							</TD>
							<TD class="NormalText"><%=languageChoose.getMessage("fi.jsp.ProjectList.Year")%><SELECT name="year" class="COMBO">
									<%
										int nStartYear = 2000;
										java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat("yyyy");
										int nEndYear = Integer.parseInt(yearFormat.format(new java.util.Date()));
										for (int i = nEndYear; i >= nStartYear; i--) {
									%>
									<OPTION value="<%=i%>"<%=(i == actualMonth.getYear() ? " selected" : "")%>><%=i%></OPTION>
									<%
										}
									%>
								</SELECT>
							</TD>
						</TR>
						<TR>
							<TD colspan="2" align="center">
							<INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.ProjectList.Refresh")%>" class="BUTTON" onclick="onReportMonthChange();">
							</TD>
						</TR>
					</TBODY>
				</TABLE>
			</TD>
		</TR>
	</TBODY>
</TABLE>
<BR>
<TABLE class="Table" width="95%" cellspacing="1">
<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage(metricDescInfo.metricName)%> (<%=metricDescInfo.unit%>):</CAPTION>
	<TBODY>
		<COLGROUP>
			<COL width="20%">
			<COL width="15%" align="left">
			<COL width="35%" align="left">
			<COL width="15%" align="left">
			<COL width="15%" align="left">
		<TR class="ColumnLabel">
			<TD><%=languageChoose.getMessage("fi.jsp.ProjectList.Projectname")%></TD>
			<TD align="center"><%=languageChoose.getMessage("fi.jsp.ProjectList.Leader")%></TD>
			<TD align="center"><%=languageChoose.getMessage("fi.jsp.ProjectList.Customer")%></TD>
			<TD align="center"><%=languageChoose.getMessage("fi.jsp.ProjectList.Category")%></TD>
			<TD align="center"><%=languageChoose.getMessage("fi.jsp.ProjectList.Type")%></TD>
		</TR>
		<%
			int nRows = vtInProgressProjects.size();
			for (int i = 0; i < nRows; i++) {
				ProjectInfo projectInfo = (ProjectInfo)vtInProgressProjects.elementAt(i);
		%>
		<TR class="CellBGRnews" onmouseover="setPointer(this, '#66CCFF')" onmouseout="setPointer(this, '#F7F7F7')" onclick="onChooseWorkUnit(<%=projectInfo.getWorkUnitId()%>)" style="cursor:hand">
			<TD><%=projectInfo.getProjectName()%></TD>
			<TD><%=projectInfo.getLeader()%></TD>
			<TD><%=projectInfo.getCustomer()%></TD>
			<TD><%=languageChoose.getMessage(projectInfo.getLifecycle())%></TD>
			<TD><%=languageChoose.getMessage(projectInfo.getProjectType())%></TD>
		</TR>
		<%
			}
		%>
	</TBODY>
</TABLE>
<BR>
</FORM>
<P align="center">
<INPUT type="button" class="BUTTON" name="btnSummary" value="<%=languageChoose.getMessage("fi.jsp.ProjectList.Summary")%>" onclick="onSummary()">
<%
	if (strParentWorkUnitID != null) {
%>
&nbsp;<INPUT type="button" class="BUTTON" name="btnDrillUp" value="<%=languageChoose.getMessage("fi.jsp.ProjectList.DrillUp")%>" onclick="onDrillUp()">
<%}%>
</P>
<SCRIPT language="javascript">
function onReportMonthChange() {
	frm.action = "Fms1Servlet?reqType=<%=Constants.INDEX_DRILL%>&metricID=<%=strMetricID%>";
	frm.submit();
}

function onChooseWorkUnit(workUnitID) {
	frm.action = "Fms1Servlet?reqType=<%=Constants.INDEX_DRILL%>&metricID=<%=strMetricID%>&workUnitID=" + workUnitID;
	frm.submit();
}

function onSummary() {
	frm.action="Fms1Servlet?reqType=" + "<%=Constants.PRODUCT_INDEXES%>";
	frm.submit();
}
<%
	if (strParentWorkUnitID != null) {
%>
function onDrillUp() {
	frm.action="Fms1Servlet?reqType=<%=Constants.INDEX_DRILL%>&metricID=<%=strMetricID%>&workUnitID=<%=strParentWorkUnitID%>";
	frm.submit();
}
<%
	}
%>
</SCRIPT>
<BR>
</BODY>
</HTML>
