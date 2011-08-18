<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.ArrayList, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>

<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<TITLE>product.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	ArrayList alProdInd = (ArrayList)session.getAttribute("productIndexes");
	String strWorkUnitName = (String)session.getAttribute("workUnitName");
	ReportMonth actualMonth = (ReportMonth)session.getAttribute("reportMonth");
	ReportMonth lastMonth = new ReportMonth(actualMonth);
	lastMonth.moveToPreviousMonth();
	ReportMonth nextMonth = new ReportMonth(actualMonth);
	nextMonth.moveToNextMonth();
	ReportMonth next2Month = new ReportMonth(nextMonth);
	next2Month.moveToNextMonth();
	double dSCI = Double.parseDouble((String)session.getAttribute("SCI"));
	int nWorkUnitType = Integer.parseInt((String)session.getAttribute("workUnitType"));
	String strMenuType = (nWorkUnitType==Constants.RIGHT_GROUP)?"loadGrpMenu":"loadOrgMenu";
%>
<BODY class="BD" onLoad="<%=strMenuType%>()">
<TABLE width="95%">
	<TBODY>
		<TR>
			<TD><P class="TITLE"><%= languageChoose.paramText(new String[]{"fi.jsp.Product.~PARAM1_NAME~Productindexesasof~PARAM2_MONTH~__~PARAM3_YEAR~",strWorkUnitName, CommonTools.getMonth(actualMonth.getMonth()), String.valueOf(actualMonth.getYear())})%></P></TD>
			<TD align="right"></TD>
		</TR>
	</TBODY>
</TABLE>
<BR>
<TABLE width="20%" align="center" cellspacing="1" class="table">
	<TBODY>
		<TR>
			<TD class="WELCOME"> SCI: <%=CommonTools.formatDouble(dSCI)%></TD>
		</TR>
	</TBODY>
</TABLE>
<BR>
<FORM name="frm" action="Fms1Servlet" method="POST">
<TABLE width="95%">
	<TBODY>
		<TR>
			<TD width="50%">
				<TABLE width="100%">
				<TR>
					<TD>
					<FIELDSET>
						<LEGEND class="LargeText"> <%=languageChoose.getMessage("fi.jsp.Product.Legend")%> </LEGEND>
						<FONT color="red"  size="1"><LI> <%=languageChoose.getMessage("fi.jsp.Product.Redmetricshavemorethan20ofbadd")%> </LI></FONT>
						<FONT color="blue" size="1"><LI> <%=languageChoose.getMessage("fi.jsp.Product.Bluemetricshavemorethan20ofgoo")%> </LI></FONT>
						<FONT color="gray" size="1"><LI> <%=languageChoose.getMessage("fi.jsp.Product.Clickoneachmetrictodrilldown")%> </LI></FONT>
					</FIELDSET>
					</TD>
				</TR>
				</TABLE>
			</TD>
			<TD align="center">
				<TABLE cellspacing="4" cellpadding="4">
					<TBODY>
						<TR>
							<TD class="NormalText"> <%=languageChoose.getMessage("fi.jsp.Product.Choosemonth")%> &nbsp; <SELECT name="month" class="COMBO">
									<%for (int i = 1; i <= 12; i++) {
									%><OPTION value="<%=i%>"<%=(i == actualMonth.getMonth() ? " selected" : "")%>><%=CommonTools.getMonth(i)%></OPTION>
									<%}%>
								</SELECT>
							</TD>
							<TD class="NormalText"> <%=languageChoose.getMessage("fi.jsp.Product.Year")%>&nbsp; <SELECT name="year" class="COMBO">
									<%
										int nStartYear = 2000;
										java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat("yyyy");
										int nEndYear = Integer.parseInt(yearFormat.format(new java.util.Date()));
										for (int i = nEndYear; i >= nStartYear; i--) {
									%><OPTION value="<%=i%>"<%=(i == actualMonth.getYear() ? " selected" : "")%>><%=i%></OPTION>
									<%}%>
								</SELECT>
							</TD>
						</TR>
						<TR>	
							<TD colspan="2" align="center">
							<INPUT type="button" value=" <%=languageChoose.getMessage("fi.jsp.Product.Refresh")%> " class="BUTTON" onclick="onReportMonthChange();">
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
	<TBODY>
		<COLGROUP>
			<COL width="25%">
			<COL width="15%" align="center">
			<COL width="10%" align="right">
			<COL width="10%" align="right">
			<COL width="10%" align="right">
			<COL width="10%" align="right">
			<COL width="10%" align="right">
			<COL width="15%" align="right">
		<TR class="ColumnLabel">
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.Product.Name")%> </TD>
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.Product.Unit")%> </TD>
			<TD colspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.Product.Actualvalues")%> </TD>
			<TD colspan="3" align="center"> <%=languageChoose.getMessage("fi.jsp.Product.Plannedvalues")%> </TD>
			<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.Product.Achievement")%> </TD>
		</TR>
		<TR class="ColumnLabel">
			<TD align="center"><%=CommonTools.getMonth(lastMonth.getMonth())%>-<%=String.valueOf(lastMonth.getYear())%></TD>
			<TD align="center"><%=CommonTools.getMonth(actualMonth.getMonth())%>-<%=String.valueOf(actualMonth.getYear())%></TD>
			<TD align="center"><%=CommonTools.getMonth(actualMonth.getMonth())%>-<%=String.valueOf(actualMonth.getYear())%></TD>
			<TD align="center"><%=CommonTools.getMonth(nextMonth.getMonth())%>-<%=String.valueOf(nextMonth.getYear())%></TD>
			<TD align="center"><%=CommonTools.getMonth(next2Month.getMonth())%>-<%=String.valueOf(next2Month.getYear())%></TD>
		</TR>
		<%
			int nRows = alProdInd.size();
			ProductIndexInfo prodIndInf = (ProductIndexInfo)alProdInd.get(0);
			for (int i = 0; i < nRows; i++) {
				boolean isDouble = (i == 0) ? false : true;
				prodIndInf = (ProductIndexInfo)alProdInd.get(i);
		%>
		<TR class="CellBGRnews" onmouseover="setPointer(this, '#66CCFF')" onmouseout="setPointer(this, '#F7F7F7')" onclick="onDetailIndexDrill(<%=i%>)" style="cursor:hand">
			<TD><%=languageChoose.getMessage(prodIndInf.getMetricName())%></TD>
			<TD><%=prodIndInf.getMetricUnit()%></TD>
			<TD><%=CommonTools.formatNumber(prodIndInf.getLastValue(), isDouble)%></TD>
			<TD><%=CommonTools.formatNumber(prodIndInf.getActualValue(), isDouble)%></TD>
			<TD><%=CommonTools.formatNumber(prodIndInf.getActualPlannedValue(), isDouble)%></TD>
			<TD><%=CommonTools.formatNumber(prodIndInf.getNextPlannedValue(), isDouble)%></TD>
			<TD><%=CommonTools.formatNumber(prodIndInf.getNext2monthsPlannedValue(), isDouble)%></TD>
			<TD><%=Color.colorByNorm(CommonTools.formatNumber(prodIndInf.getAchievementValue(), isDouble), prodIndInf.getAchievementValue(), 80, 120, 1)%></TD>
		</TR>
		<%}%>
	</TBODY>
</TABLE>
</FORM>
<SCRIPT language="javascript">
function onReportMonthChange() {
	frm.action = "Fms1Servlet?reqType=<%=Constants.PRODUCT_INDEXES%>";
	frm.submit();
}

function onDetailIndexDrill(i) {
	var strMetricID;
	var subSelect;
	switch (i) {
		case 0:
			strMetricID = "<%=String.valueOf(MetricDescInfo.IN_PROGRESS_PROJECTS)%>";
			subSelect = "In progress projects" ;
			break;
		case 1:
			strMetricID = "<%=String.valueOf(MetricDescInfo.PRODUCTION_SIZE)%>";
			subSelect = "Production size";
			break;
		case 2:
			strMetricID = "<%=String.valueOf(MetricDescInfo.PRODUCTIVITY_LOC)%>";
			subSelect = "LOC Productivity";
			break;
		case 3:
			strMetricID = "<%=String.valueOf(MetricDescInfo.REQUIREMENT_STABILITY)%>";
			subSelect = "Requirement stability";
			break;
		case 4:
			strMetricID = "<%=String.valueOf(MetricDescInfo.ACCEPTANCE_RATE)%>";
			subSelect = "Acceptance rate";
			break;
		case 5:
			strMetricID = "<%=String.valueOf(MetricDescInfo.REQUIREMENT_COMPLETENESS)%>";
			subSelect = "Req. Completeness";
			break;
		case 6:
			strMetricID = "<%=String.valueOf(MetricDescInfo.TIMELINESS)%>";
			subSelect = "Timeliness";
			break;
		case 7:
			strMetricID = "<%=String.valueOf(MetricDescInfo.DELIVERY_SCHEDULE_DEVIATION)%>";
			subSelect = "Delivery sched. deviation";
			break;
		case 8:
			strMetricID = "<%=String.valueOf(MetricDescInfo.DEFECT_RATE)%>";
			subSelect = "Defect rate";
			break;
		case 9:
			strMetricID = "<%=String.valueOf(MetricDescInfo.LEAKAGE)%>";
			subSelect = "Leakage";
			break;
		case 10:
			strMetricID = "<%=String.valueOf(MetricDescInfo.DEFECT_RATE_LOC)%>";
			subSelect = "Defect Rate LOC";
			break;
		case 11:
			strMetricID = "<%=String.valueOf(MetricDescInfo.CUSTOMER_SATISFACTION)%>";
			subSelect = "Customer satisfaction";
			break;
		case 12:
			strMetricID = "<%=String.valueOf(MetricDescInfo.EFFORT_EFFICIENCY)%>";
			subSelect = "Effort efficiency";
			break;
		case 13:
			strMetricID = "<%=String.valueOf(MetricDescInfo.NONCONFORMING_PRODUCT_RATE)%>";
			subSelect = "Nonconf. product rate";
			break;
	}
	frm.action = "Fms1Servlet?reqType=<%=Constants.INDEX_DRILL%>&metricID=" + strMetricID;
	parent.frames['menu'].selectedSub = subSelect;
	frm.submit();
}
</SCRIPT>
<BR>
</BODY>
</HTML>
