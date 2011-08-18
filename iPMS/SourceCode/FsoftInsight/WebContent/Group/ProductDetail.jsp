<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.ArrayList" contentType="text/html;charset=UTF-8"%>
<%@taglib uri="/WEB-INF/cewolf-1.1.tld" prefix="cewolf" %>
<%@page import="java.util.*"%>
<%@page import="de.laures.cewolf.*"%>
<%@page import="de.laures.cewolf.tooltips.*"%>
<%@page import="de.laures.cewolf.links.*"%>
<%@page import="org.jfree.data.gantt.*"%>
<%@page import="org.jfree.chart.*"%>
<%@page import="org.jfree.chart.plot.CategoryPlot"%>
<%@page import="java.awt.*" %>
<%@ page import="de.laures.cewolf.taglib.CewolfChartFactory" %>
<%@ page import="org.jfree.chart.event.ChartProgressListener" %>
<%@ page import="org.jfree.chart.event.ChartProgressEvent" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<TITLE>ProductDetail.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	Vector alIndexDrill = (Vector)session.getAttribute("indexDrill");
	String strMetricID = (String)session.getAttribute("metricID");
	MetricDescInfo metricDescInfo = (MetricDescInfo)session.getAttribute("metricDescInfo");
	String strWorkUnitName = (String)session.getAttribute("workUnitName");
	String strParentWorkUnitID = (String)session.getAttribute("parentWorkUnitID");
	int nWorkUnitType = Integer.parseInt((String)session.getAttribute("workUnitType"));
	String strMenuType = (nWorkUnitType==Constants.RIGHT_GROUP)?"loadGrpMenu":"loadOrgMenu";
	ReportMonth actualMonth = (ReportMonth)session.getAttribute("reportMonth");
	int month = actualMonth.getMonth();
	int year = actualMonth.getYear();
	int nRows = alIndexDrill.size();
%>
<BODY class="BD" onLoad="<%=strMenuType%>()">
<TABLE width="95%">
	<TBODY>
		<TR>
			<TD><P class="TITLE"><%= languageChoose.paramText(new String[]{"fi.jsp.ProductDetail.~PARAM1_STRING~~PARAM2_STRING~asof~PARAM3_MONTH~~PARAM4_YEAR~", strWorkUnitName, languageChoose.getMessage(metricDescInfo.metricName), CommonTools.getMonth(month), String.valueOf(year)})%></P></TD>
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
						<LEGEND class="LargeText"> <%=languageChoose.getMessage("fi.jsp.ProductDetail.Legend")%> </LEGEND>
						<FONT color="red"  size="1"><LI> <%=languageChoose.getMessage("fi.jsp.ProductDetail.Redmetricshavemorethan20ofbadd")%> </LI></FONT>
						<FONT color="blue" size="1"><LI> <%=languageChoose.getMessage("fi.jsp.ProductDetail.Bluemetricshavemorethan20ofgoo")%> </LI></FONT>
						<FONT color="gray" size="1"><LI> <%=languageChoose.getMessage("fi.jsp.ProductDetail.Clickonworkunittodrilldown")%> </LI></FONT>
					</FIELDSET>
					</TD>
				</TR>
				</TABLE>
			</TD>
			<TD align="center">
				<TABLE cellspacing="4" cellpadding="4">
					<TBODY>
						<TR>
							<TD class="NormalText"> <%=languageChoose.getMessage("fi.jsp.ProductDetail.Choosemonth")%> &nbsp; <SELECT name="month" class="COMBO">
									<%for (int i = 1; i <= 12; i++) {
									%><OPTION value="<%=i%>"<%=(i == month ? " selected" : "")%>	><%=CommonTools.getMonth(i)%></OPTION>
									<%}%>
								</SELECT>
							</TD>
							<TD class="NormalText"> <%=languageChoose.getMessage("fi.jsp.ProductDetail.Year")%> &nbsp; <SELECT name="year" class="COMBO">
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
							<INPUT type="button" value=" <%=languageChoose.getMessage("fi.jsp.ProductDetail.Refresh")%> " class="BUTTON" onclick="onReportMonthChange();">
							</TD>
						</TR>
					</TBODY>
				</TABLE>
			</TD>
		</TR>
	</TBODY>
</TABLE>
<BR>
<P>
<%	
				//create Data
				String name = languageChoose.getMessage("fi.jsp.ProductDetail.Planned");
				String name2 = languageChoose.getMessage("fi.jsp.ProductDetail.Actual");
				String name3 = languageChoose.getMessage("fi.jsp.ProductDetail.Last");
			  	ArrayList nameSeries = new ArrayList();
				nameSeries.add(name);
			  	nameSeries.add(name2);
			  	nameSeries.add(name3);
				HashMap valueSeries = new HashMap();
				ArrayList listValuesSeries = new ArrayList();
				ArrayList listValuesSeries2 = new ArrayList();
				ArrayList listValuesSeries3 = new ArrayList();
				valueSeries.put(name, listValuesSeries);
			  	valueSeries.put(name2, listValuesSeries2);
			  	valueSeries.put(name3, listValuesSeries3);
				ArrayList categorySeries = new ArrayList();
				String value;
				String value2;
				String value3;
				String nameCategory;
				for (int i = 0; i < nRows; i++) {
					IndexDrillInfo indDrillInf = (IndexDrillInfo)alIndexDrill.get(i);
					value = Double.isNaN(indDrillInf.getPlannedValue()) ? "0" : String.valueOf(indDrillInf.getPlannedValue());
					value2 = Double.isNaN(indDrillInf.getActualValue()) ? "0" : String.valueOf(indDrillInf.getActualValue());
					value3 = Double.isNaN(indDrillInf.getLastValue()) ? "0" : String.valueOf(indDrillInf.getLastValue());
					if (indDrillInf.getWorkUnitName() != null){
						nameCategory =(indDrillInf.getWorkUnitName().length() > 8) ? indDrillInf.getWorkUnitName().substring(0, 7) + "..." : indDrillInf.getWorkUnitName();
					}
					else{
						nameCategory = "";
					}
					listValuesSeries.add(value);
					listValuesSeries2.add(value2);
					listValuesSeries3.add(value3);
					categorySeries.add(nameCategory);
				}
			%>
				<jsp:useBean id="CategoryData" class="com.fms1.chart.LinesDataSet" scope="page"  />
				<jsp:useBean id="LineLabelled" class="com.fms1.chart.BarChartLabels" scope="page"  />
				<cewolf:chart id="XYChart" title='<%=(languageChoose.getMessage(metricDescInfo.metricName)+"("+ metricDescInfo.unit+")")%>' type="verticalbar" xaxislabel="" yaxislabel="" showlegend = "true" >
				    <cewolf:colorpaint color="#AAAAFFEE"/>
				    <cewolf:data>
				        <cewolf:producer id="CategoryData">
				        	<cewolf:param 
				                name="<%=StringConstants.paramNameSeries%>" 
				                value="<%= nameSeries %>"/>
				            <cewolf:param 
				                name= "<%=StringConstants.paramValueSeries%>"
				                value="<%= valueSeries %>"/>
				            <cewolf:param 
				                name= "<%=StringConstants.paramCategorySeries%>"
				                value="<%= categorySeries %>"/>
				        </cewolf:producer>
				    </cewolf:data>
				    <cewolf:chartpostprocessor id="LineLabelled"/>
				</cewolf:chart>
				<cewolf:img chartid="XYChart" renderer="/cewolf" width="800" height="400">
				  <cewolf:map linkgeneratorid="xyLinkGenerator" useJFreeChartTooltipGenerator="true">
				  	
				  </cewolf:map>
				</cewolf:img>

</P>
<TABLE class="Table" width="95%" cellspacing="1">
<CAPTION align="left" class="TableCaption"><%= languageChoose.paramText(new String[] { "fi.jsp.ProductDetail.~PARAM1_METRICNAME~~PARAM2_UNIT~", languageChoose.getMessage(metricDescInfo.metricName),metricDescInfo.unit})%></CAPTION>
	<TBODY>
		<COLGROUP>
			<COL width="26%">
			<COL width="14%" align="right">
			<COL width="14%" align="right">
			<COL width="14%" align="right">
			<COL width="16%" align="right">
			<COL width="16%" align="right">
		<TR class="ColumnLabel">
			<TD><%=(nWorkUnitType == 0 ? languageChoose.getMessage("fi.jsp.ProductDetail.Group") : languageChoose.getMessage("fi.jsp.ProductDetail.Project"))%>  <%=languageChoose.getMessage("fi.jsp.ProductDetail.name")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.ProductDetail.Planned")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.ProductDetail.Actual")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.ProductDetail.Last")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.ProductDetail.Achievement")%> </TD>
			<TD align="center"> <%=languageChoose.getMessage("fi.jsp.ProductDetail.Improvement")%> </TD>
		</TR>
		<% 
			boolean isNotInProgressMetric = (Integer.parseInt(strMetricID) == MetricDescInfo.IN_PROGRESS_PROJECTS ? false : true);
			for (int i = 0; i < nRows; i++) {
				IndexDrillInfo indDrillInf = (IndexDrillInfo)alIndexDrill.get(i);
				String strPlannedValue = CommonTools.formatNumber(indDrillInf.getPlannedValue(), isNotInProgressMetric);
				String strActualValue = CommonTools.formatNumber(indDrillInf.getActualValue(), isNotInProgressMetric);
				String strLastValue = CommonTools.formatNumber(indDrillInf.getLastValue(), isNotInProgressMetric);
				String strRowClass = (i < nRows - 1 ? "class=\"CellBGRnews\" onmouseover=\"setPointer(this, '#66CCFF')\" onmouseout=\"setPointer(this, '#F7F7F7')\" onclick=\"onChooseWorkUnit(" + String.valueOf(indDrillInf.getWorkUnitID()) + ")\" style=\"cursor:hand\"" : "class=\"CellBGR3\"");
				String strWorkUnitname = "N/A";
				if (indDrillInf.getWorkUnitName() != null){
					strWorkUnitname = indDrillInf.getWorkUnitName();
				}
		%>
		<TR <%=strRowClass%>>
			<TD><%=strWorkUnitname%></TD>
			<TD><%=strPlannedValue%></TD>
			<TD><%=strActualValue%></TD>
			<TD><%=strLastValue%></TD>
			<TD><%=com.fms1.tools.Color.colorByNorm(CommonTools.formatNumber(indDrillInf.getAchievementValue(), true), indDrillInf.getAchievementValue(), 80, 120, 1)%></TD>
			<TD><%=com.fms1.tools.Color.colorByNorm(CommonTools.formatNumber(indDrillInf.getImprovementValue(), true), indDrillInf.getImprovementValue(), 0, 20, 1)%></TD>
		</TR>
		<%}%>
	</TBODY>
</TABLE>
<BR>
</FORM>
<P align="center">
<INPUT type="button" class="BUTTON" name="btnSummary" value=" <%=languageChoose.getMessage("fi.jsp.ProductDetail.Summary")%> " onclick="onSummary()">
<%if ((nWorkUnitType == Constants.RIGHT_GROUP) && (strParentWorkUnitID != null)) {%>
&nbsp;<INPUT type="button" class="BUTTON" name="btnDrillUp" value=" <%=languageChoose.getMessage("fi.jsp.ProductDetail.DrillUp")%> " onclick="onDrillUp()">
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
	frm.action = "Fms1Servlet?reqType=<%=Constants.PRODUCT_INDEXES%>";
	frm.submit();
}
<%if ((nWorkUnitType == Constants.RIGHT_GROUP) && (strParentWorkUnitID != null)) {%>
function onDrillUp() {
	frm.action = "Fms1Servlet?reqType=<%=Constants.INDEX_DRILL%>&metricID=<%=strMetricID%>&workUnitID=<%=strParentWorkUnitID%>";
	frm.submit();
}
<%}%>
</SCRIPT>
<BR>
</BODY>
</HTML>
