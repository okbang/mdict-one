<%@page import="com.fms1.infoclass.*, com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,
		java.util.*,java.io.*,java.text.*" errorPage="error.jsp" contentType="application/vnd.ms-excel;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>milestone.jsp</TITLE>
<STYLE type="text/css">
BODY {
    font-family: verdana;
	background-color: #ccffcc;
}
.Title {
	color: navy;
	font-weight: bold;
	font-size: 16pt;
	margin-left: 0px;
	margin-top: 20px
}
.TableCaption {
	font-family: verdana, geneva, arial, helvetica, sans-serif;
	font-size: 13;
	text-align: left;
	font-weight: bold;
}
.ColumnLabel {
	background-color: #003366;
	color: white;
	font-weight: bold;
	vertical-align: middle;
	text-align: center;
	border-style: solid;
}
.TableFooter {
	background-color: #666699;
	color: white;
	vertical-align: middle;
	text-align: center;
}
.Table {
	border-style: solid;
	border-width: thin;
	text-align: left;
}
.Cell {
	border-width: thin;
	border-style: solid;
	vertical-align: top;
}
.CellBGRnews{
	border-width: thin;
	border-style: solid;
	vertical-align: top;
}
.CellBGR3{
	border-width: thin;
	border-style: solid;
	vertical-align: top;
}
</STYLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY>
<%
	response.addHeader("Content-Disposition", "attachment;filename=milestoneReport.xls");
	
	if (request.getParameter("noStage") != null) {
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Milestonereport")%> </P>
<BR>
<P class="ERROR"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Nomilestonesdefinedorcompleted")%> </P>
<%
	} else {
		StageInfo stageInfo = (StageInfo) session.getAttribute("stageInfo");
		ProjectInfo projectInfo = (ProjectInfo) session.getAttribute("projectInfo");
		Vector milestoneEffort = (Vector) session.getAttribute("milestoneEffort");
		Vector milestoneEffort2 = (Vector) session.getAttribute("milestoneEffort2");
		Vector milestoneQA = (Vector)session.getAttribute("milestoneQA");
		Vector milestoneQO = (Vector) session.getAttribute("milestoneQO");
		Vector cusMetricList = (Vector)session.getAttribute("cusMetricList");
		Vector milestoneSchedule = (Vector)session.getAttribute("milestoneSchedule");
		Vector milestoneRisk =  (Vector)session.getAttribute("milestoneRisk");
		Vector milestoneIssue = (Vector) session.getAttribute("milestoneIssue");
		Vector metricInfo = (Vector)session.getAttribute("metricInfo");
		Vector defectPrevention = (Vector)session.getAttribute("defectPrevention");
		Vector dar = (Vector)session.getAttribute("dar");
		
		String comments = stageInfo.comments;
		
		// Add by HaiMM
		boolean style=false;
		String className;
%>

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Milestonereport")%> </P>

<TABLE>
	<COL class="TableCaption">
	<COL align="left">
	<TBODY>
		<TR>
			<TD><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Project")%></TD>
			<TD><%=(String)session.getAttribute("workUnitName")%></TD>
		</TR>
		<TR>
			<TD><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Customer")%></TD>
			<TD><%=projectInfo.getCustomer()%></TD>
		</TR>
		<TR>
			<TD><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Planstagestartdate")%></TD>
			<TD><%=CommonTools.dateFormat(stageInfo.plannedBeginDate)%></TD>
		</TR>
		<TR>
			<TD><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Planstageenddate")%></TD>
			<TD><%=CommonTools.dateFormat(stageInfo.plannedEndDate)%></TD>
		</TR>
		<TR>
			<TD><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Actualstartdate")%></TD>
			<TD><%=CommonTools.dateFormat(stageInfo.actualBeginDate)%></TD>
		</TR>
		<TR>
			<TD><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Actualenddate")%></TD>
			<TD><%=CommonTools.dateFormat(stageInfo.aEndD)%></TD>
		</TR>
		<TR>
			<TD><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Milestone")%></TD>
			<TD><%=stageInfo.stage%></TD>
		</TR>
	</TBODY>
</TABLE>

<BR>

<TABLE class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Comments")%> </CAPTION>
	<TR>
		<TD colspan="2" rowspan="3"><%=ConvertString.toHtml(comments)%></TD>
	</TR>
</TABLE>

<BR>

<TABLE class="Table">
	<COL span="2">
	<COL span="3" align="right">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Metrics")%> </CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Item")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Unit")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.TotalPlanned")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.SpentCompleted")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Remain")%> </TD>
		</TR>
	<%
    	String estimated = null;
    	String spent = null;
		String remaining = null;
		ReportMetricInfo reportMetricInfo;
		NormInfo normInfo;
    
	for (int i = 0; i < metricInfo.size(); i++) 
		{
			if ((i != 9) && (i != 10) && (i != 11)) // Modify by HaiMM: Remove 3 metrics follow CR: Development, Quality, Management
			{
				reportMetricInfo = (ReportMetricInfo)metricInfo.elementAt(i);
			
			if (!reportMetricInfo.unit.equalsIgnoreCase("DD-MMM-YY")) {
				estimated=CommonTools.formatDouble(reportMetricInfo.estimated);
				spent=CommonTools.formatDouble(reportMetricInfo.spent);
				remaining=CommonTools.formatDouble(reportMetricInfo.remain);
			} else {
				estimated=CommonTools.dateFormat(reportMetricInfo.estimated);
				spent=CommonTools.dateFormat(reportMetricInfo.spent);
				remaining=CommonTools.dateFormat(reportMetricInfo.remain);
			}
			
			remaining=Color.setColor(reportMetricInfo.color,remaining);
	%>
		<TR class="Cell">
			<TD><%=languageChoose.getMessage(reportMetricInfo.name)%></TD>
			<TD><%=reportMetricInfo.unit%></TD>
			<TD><%=estimated%></TD>
			<TD><%=spent%></TD>
			<TD><%=remaining%></TD>
		</TR>
	<%
		}
	}
	%>
	</TBODY>
</TABLE>

<BR>

<TABLE class="Table">
	<COL span="4" align="right">
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Effort")%> </CAPTION>
	<TR class="ColumnLabel">
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Process")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Plannedpd")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Spentduringstagepd")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Spentuptomilestonepd")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Remainpd")%> </TD>
	</TR>
	<%
    	if (milestoneEffort != null) {
	        float totalPlannedValue = 0;
	        float totalActual = 0;
	        float totalActual2 = 0;
	        double remain = 0; // interim variable
	        float totalRemain = 0;
	        for(int i = 0; i < milestoneEffort.size(); i++) {
	        	ProcessEffortInfo processInfo = (ProcessEffortInfo)milestoneEffort.elementAt(i);
	        	ProcessEffortInfo processInfo2 = (ProcessEffortInfo)milestoneEffort2.elementAt(i);	        	
	        	
	        	String plannedValue = null;
	        	String remainValue = null;
	        	
	        	if (!Double.isNaN(processInfo.actual)) {
	    			totalActual += processInfo.actual;
	    		}
	    		if (!Double.isNaN(processInfo2.actual)) {
	    			totalActual2 += processInfo2.actual;
	    		}
	    		if (!Double.isNaN(processInfo.reEstimated)) {
	    			plannedValue = CommonTools.formatDouble(processInfo.reEstimated);
	    			remain = processInfo.reEstimated - processInfo2.actual;
	    			remainValue = CommonTools.formatDouble(remain);
	    			totalPlannedValue += processInfo.reEstimated;
	    			totalRemain += remain;	
	    		} else if(!Double.isNaN(processInfo.estimated)) {
	    			plannedValue = CommonTools.formatDouble(processInfo.estimated);
	    			remain = processInfo.estimated -processInfo2.actual;
	    			remainValue = CommonTools.formatDouble(remain);
	    			totalPlannedValue += processInfo.estimated;
	    			totalRemain += remain;
	    		} else {
	    		    plannedValue = "N/A";
	    		    remainValue = "N/A";
	    		}
	%>
	<TR class="Cell">
		<TD align="left"><%=languageChoose.getMessage(processInfo.process)%></TD>
		<TD><%=plannedValue%></TD>
		<TD><%=CommonTools.formatDouble(processInfo.actual)%></TD>
		<TD><%=CommonTools.formatDouble(processInfo2.actual)%></TD>
		<TD><%=remainValue%></TD>
	</TR>
		<%
			}
		%>
	<TR class="TableFooter">
		<TD><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Total")%></TD>
		<TD><%=CommonTools.formatDouble(totalPlannedValue)%></TD>
		<TD><%=CommonTools.formatDouble(totalActual)%></TD>
		<TD><%=CommonTools.formatDouble(totalActual2)%></TD>
		<TD><%=CommonTools.formatDouble(totalRemain)%></TD>
	</TR>
	<%
		}
	%>
</TABLE>

<BR>

<TABLE class="Table">
	<COL span="3" align="right">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Schedule")%> </CAPTION>
	<TR class="ColumnLabel">
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Product")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Plannedreleasedate")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Actualreleasedate")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Deviation")%> </TD>
	</TR>
	<%
    	if (milestoneSchedule != null) {
	        for (int i = 0; i < milestoneSchedule.size(); i++) {
	        	ProcessScheduleInfo processScheduleInfo = (ProcessScheduleInfo)milestoneSchedule.elementAt(i);
	%>
	<TR class="Cell">
		<TD align="left"><%=ConvertString.toHtml(processScheduleInfo.getProduct())%></TD>
		<TD><%=CommonTools.dateFormat(processScheduleInfo.getPlannedReleaseDate())%></TD>
		<TD><%=CommonTools.dateFormat(processScheduleInfo.getActualReleaseDate())%></TD>
		<TD><%=CommonTools.formatDouble(processScheduleInfo.getDeviation())%></TD>
	</TR>
	<%
			}
		}
	%>
</TABLE>

<BR>

<TABLE class="Table">
	<COL span="6" align="right">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Qualityactivities")%> </CAPTION>
	<TR class="ColumnLabel">
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Activity")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Weighteddefectsplannedtobefoun")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Weighteddefectsdetected")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Plannedeffortpd")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Actualeffortpd")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Completeness")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Timeliness")%> </TD>
	</TR>
	<%
		double total1 = 0;
    	double total2 = 0;
	    double total3 = 0;
    	double total4 = 0;
        double total5 = 0;
        double total6 = 0;

        if (milestoneQA != null) {
	        for(int i = 0; i < milestoneQA.size(); i++) {
	        	QualityActivitiesInfo qualityActivitiesInfo = (QualityActivitiesInfo)milestoneQA.elementAt(i);
	        	
				if (!Double.isNaN(qualityActivitiesInfo.getWeightedDefectsPlannedToBeFound())) {
		    		total1 = total1 + qualityActivitiesInfo.getWeightedDefectsPlannedToBeFound();
				}
				
		    	if (!Double.isNaN(qualityActivitiesInfo.getWeightedDefectsDetected())) {
		    		total2 = total2 + qualityActivitiesInfo.getWeightedDefectsDetected();
				}
				
		    	if (!Double.isNaN(qualityActivitiesInfo.getPlannedEffort())) {
		    		total3 = total3 + qualityActivitiesInfo.getPlannedEffort();
				}
				
		    	if (!Double.isNaN(qualityActivitiesInfo.getActualEffort())) {
		    		total4 = total4 + qualityActivitiesInfo.getActualEffort();
	    		}

		    	if (!Double.isNaN(qualityActivitiesInfo.getActualEffort())) {
		    		total5 = total5 + qualityActivitiesInfo.getCompleteness();
	    		}

		    	if (!Double.isNaN(qualityActivitiesInfo.getActualEffort())) {
		    		total6 = total6 + qualityActivitiesInfo.getTimeliness();
	    		}
	%>
	<TR class="Cell">
		<TD align="left"><%=qualityActivitiesInfo.getActivity()%></TD>
		<TD><%=CommonTools.formatDouble(qualityActivitiesInfo.getWeightedDefectsPlannedToBeFound())%></TD>
		<TD><%=CommonTools.formatDouble(qualityActivitiesInfo.getWeightedDefectsDetected())%></TD>
		<TD><%=CommonTools.formatDouble(qualityActivitiesInfo.getPlannedEffort())%></TD>
		<TD><%=CommonTools.formatDouble(qualityActivitiesInfo.getActualEffort())%></TD>
		<TD><%=CommonTools.formatDouble(qualityActivitiesInfo.getCompleteness())%></TD>
		<TD><%=CommonTools.formatDouble(qualityActivitiesInfo.getTimeliness())%></TD>
	</TR>
	<%
			}
		}
	%>
	<TR class="TableFooter">
		<TD><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Total")%></TD>
		<TD><%=CommonTools.formatDouble(total1)%></TD>
		<TD><%=CommonTools.formatDouble(total2)%></TD>
		<TD><%=CommonTools.formatDouble(total3)%></TD>
		<TD><%=CommonTools.formatDouble(total4)%></TD>
		<TD><%=CommonTools.formatDouble(total5)%></TD>
		<TD><%=CommonTools.formatDouble(total6)%></TD>
	</TR>
</TABLE>

<BR>

<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"><A name="StandardMetrics"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.StandardMetric")%> </A> </CAPTION>
    <TBODY>

        <TR class="ColumnLabel">
	        <TD rowspan="2" align="left" width="25%"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Name")%> </TD>
	        <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Unit")%> </TD>
        	<TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Norm")%> </TD>		
	        <TD rowspan="1" colspan="3" align="center"><%=languageChoose.getMessage("fi.jsp.postMortemReport.TargetedValue")%>* </TD>    	
	        <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.ActualValue")%>* </TD>
	        <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Deviation")%> </TD>            
	    	<TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Basic")%> </TD>
		</TR>
	    <TR class="ColumnLabel">
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%>&nbsp;&nbsp;</TD>
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>&nbsp;&nbsp;</TD>
	    	<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>&nbsp;&nbsp;</TD>
        </TR>
            
<%
for(int i = 0; i < milestoneQO.size() - 1; i++){
normInfo =(NormInfo)milestoneQO.elementAt(i);
className=(i%2==0)?"CellBGRnews":"CellBGR3";
if (i ==0) {
%>
         <TR class="ColumnLabel">
	         <TD colspan="9"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Quality")%></TD>
         </TR>
<%}
if (i ==3) {
%>
         <TR class="ColumnLabel">
	         <TD colspan="9"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.COST")%></TD>
         </TR>
<%}
if (i ==5) {
%>
         <TR class="ColumnLabel">
	         <TD colspan="9"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.DELIVERY")%></TD>
         </TR>
<%}      
%>
	<tr class="<%=className%>">
		<td><%=languageChoose.getMessage(normInfo.normName)%></td>
		<td><%=normInfo.normUnit%></td>
		<td><%=CommonTools.formatDouble(normInfo.average)%></td>
		<td><%=CommonTools.formatDouble(normInfo.usl)%></td>
		<td><%=CommonTools.formatDouble(normInfo.plannedValue)%></td>
		<td><%=CommonTools.formatDouble(normInfo.lsl)%></td>
		<td><%=Color.colorByNorm(CommonTools.formatDouble(normInfo.actualValue),normInfo.actualValue, normInfo.lcl, normInfo.ucl, normInfo.colorType)%></td>
		<td><%=CommonTools.formatDouble(CommonTools.metricDeviation(normInfo.plannedValue, normInfo.plannedValue, normInfo.actualValue))%></td>
		<td><%=ConvertString.toHtml(normInfo.note)%></td>			
	</tr>
	
<%}%>
</TABLE>
<p></p>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.SpecificObjectives")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD align = "center" rowspan="2"># </TD>		
            <TD rowspan="2" align="left" width="25%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Name")%> </TD>
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Unit")%> </TD>
            <TD rowspan="1" colspan="3" align="center"><%=languageChoose.getMessage("fi.jsp.postMortemReport.TargetedValue")%></TD>    	
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ActualValue")%>* </TD>
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Deviation")%> </TD>            
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Basic")%> </TD>
			</TR>
            <TR class="ColumnLabel">
            <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%>&nbsp;&nbsp;</TD>
            <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.postMortemReport.Average")%>&nbsp;&nbsp;</TD>
    		<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>&nbsp;&nbsp;</TD>
        </TR>
	<%
		for (int i = 0; i < cusMetricList.size(); i++) {
			style = !style;
 			className = (style) ? "CellBGRnews" : "CellBGR3";
 			WOCustomeMetricInfo info = (WOCustomeMetricInfo)cusMetricList.elementAt(i); 	
	%>
		<TR class="<%=className%>">
			<TD align = "center"><%=i+1%></TD>		
			<TD><%=ConvertString.toHtml(info.name)%></TD>
			<TD><%=ConvertString.toHtml(info.unit)%></TD>
			<TD><%=CommonTools.formatDouble(info.LCL)%></TD>
			<TD><%=CommonTools.formatDouble(info.plannedValue)%></TD>
			<TD><%=CommonTools.formatDouble(info.UCL)%></TD>
			<TD><%=CommonTools.formatDouble(info.actualValue)%></TD>
			<TD><%=info.deviation%></TD>
			<TD><%=ConvertString.toHtml(info.note)%></TD>
		</TR>
	<%
		}
	%>
</TABLE>

<BR>

<FORM method="post" name="frmDPTask">

<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><name="defectprevention"><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.DefectPreventionGoals")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="24" align="center">#</TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Item")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Unit")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Planned")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Actual")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Deviation")%></TD>
            <TD class="ColumnLabel">Cause</TD>
        </TR>
        <%
        	boolean bl = true;
        	String rowStyle = "";
        	
        	for(int i = 0; i < defectPrevention.size(); i++)
        	{
        		rowStyle = bl ? "CellBGRnews" : "CellBGR3";
  				
  				bl = !bl;
  				
        		DPTaskInfo dpTaskInfo = (DPTaskInfo)defectPrevention.get(i);
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A><%=dpTaskInfo.item%></A></TD>
            <TD><%=dpTaskInfo.unit%></TD>
            <TD><%=CommonTools.formatDouble(dpTaskInfo.planValue)%></TD>
            <TD><%=CommonTools.formatDouble(dpTaskInfo.actualValue)%></TD>
            <%
				String s = CommonTools.formatDouble(dpTaskInfo.deviationValue);
				if (dpTaskInfo.deviationValue > 20) {
			%>
           	<TD><%=Color.setColor(Color.BADMETRIC, s)%></TD>
	        <%
	        	} else if (dpTaskInfo.deviationValue<0) {
	        %>
	        <TD><%=Color.setColor(Color.GOODMETRIC, s)%></TD>
	        <%
	        	} else {
	        %>
	        <TD><%=Color.setColor(Color.NOCOLOR, s)%></TD>
	        <%
	        	}
           	%>
            <TD><%=dpTaskInfo.dpCause%></TD>
        </TR>
        <%
        	}
        %>
    </TBODY>
</TABLE>

<BR>

<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><A name="darplan">DAR Plan</A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD>Item</TD>
            <TD>Doer</TD>
            <TD>Target date</TD>
            <TD>Actual date</TD>
            <TD>On-time</TD>
            <TD>Cause</TD>
        </TR>
        <%
        	bl = true;
        	rowStyle = "";
        	for (int i = 0; i < dar.size(); i++) {
       			rowStyle = bl ? "CellBGRnews" : "CellBGR3";
  				bl = !bl;
  				DARPlanInfo darPlanInfo = (DARPlanInfo)dar.get(i);
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><%=darPlanInfo.darItem%></TD>
            <TD><%=darPlanInfo.doer%></TD>
            <TD><%=CommonTools.dateFormat(darPlanInfo.planDate)%></TD>
            <TD><%=CommonTools.dateFormat(darPlanInfo.actualDate)%></TD>
            <TD><%=darPlanInfo.onTime%></TD>
            <TD><%=darPlanInfo.darCause%></TD>
        </TR>
        <%
        	}
        %>
    </TBODY>
</TABLE>

<BR>

<TABLE class="Table">
	<COL span="4">
	<COL span="3" align="right">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Risksinproject")%> </CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Actualriskscenario")%> </TD>
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Actualactions")%> </TD>
			<TD colspan="3"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Actualimpact")%> </TD>
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Unplanned")%> </TD>
			<TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Assessmentdate")%> </TD>
		</TR>
		<TR class="ColumnLabel">
			<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Impactto")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Unit3")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Estimatedimpact")%> </TD>
		</TR>
	<%
    	if (milestoneRisk != null) {
	      	for (int i = 0; i < milestoneRisk.size(); i++) {
	        	RiskInfo risk = (RiskInfo)milestoneRisk.elementAt(i);
	        	int rowspan = 0;
	        	String htmlRows = "";
	        	String actImp = "";
	        	if (risk.status == 2) {
		        	for (int j = 0; j < 3; j++) {
		        		if (!"".equals(risk.imp[j])) {
		        			rowspan++;
			        		if (rowspan == 1) {
			        			actImp = "<TD>" + risk.imp[j] + "</TD><TD>" + risk.unt[j] + "</TD><TD>" + risk.est[j] + "</TD>";
			        		} else {
			        			htmlRows += "<TR class=\"Cell\"><TD>" + risk.imp[j] + "</TD><TD>" + risk.unt[j] + "</TD><TD>" + risk.est[j] + "</TD></TR>";
			        		}
		        		}
		        	}
		        	String strRowSpan = "";
		        	if (rowspan != 1) {
		        		strRowSpan = "rowspan =\"" + rowspan + "\"";
		        	}
	%>
		<TR class="Cell">
			<TD <%=strRowSpan%>><%=risk.actualRiskScenario != null ? risk.actualRiskScenario : "N/A"%></TD>
			<TD <%=strRowSpan%>><%=risk.actualAction != null ? risk.actualAction : "N/A" %></TD>
			<%=actImp%>
			<TD <%=strRowSpan%>><%=risk.unplanned == 1 ? languageChoose.getMessage("fi.jsp.milestoneReportExport.Yes") : languageChoose.getMessage("fi.jsp.milestoneReportExport.No")%></TD>
			<TD <%=strRowSpan%>><%=CommonTools.dateFormat(risk.assessmentDate)%></TD>
		</TR>
		<%			
				if (rowspan != 1) {
		%>
			<%=htmlRows%>
	<%
					}
       			}
        	}
        }
    %>
	</TBODY>
</TABLE>

<BR>

<TABLE class="Table">
	<COL span="2">
	<COL span="3" align="right">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Issues")%> </CAPTION>
	<TR class="ColumnLabel">
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Description")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Status")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Priority")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Startdate")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.milestoneReportExport.Duedate")%> </TD>
	</TR>
	<%
    	if (milestoneIssue != null) {
	        for (int i = 0; i < milestoneIssue.size(); i++) {
		        IssueInfo issueInfo = (IssueInfo)milestoneIssue.elementAt(i);
	%>
	<TR class="Cell">
		<TD><%=((issueInfo.description == null) ? "N/A" : issueInfo.description)%></TD>
		<TD><%=languageChoose.getMessage(issueInfo.getStatusName())%></TD>
		<TD><%=languageChoose.getMessage(issueInfo.getPriorityName())%></TD>
		<TD><%=((issueInfo.startDate == null) ? "N/A" : CommonTools.dateFormat(issueInfo.startDate))%></TD>
		<TD><%=((issueInfo.dueDate == null) ? "N/A" : CommonTools.dateFormat(issueInfo.dueDate))%></TD>
	</TR>
	<%
			}
		}
	%>
</TABLE>

<BR>

<%@ include file="requirementDetailBatchPlan.jsp" %>

<%
	}
%>

</BODY>
</HTML>