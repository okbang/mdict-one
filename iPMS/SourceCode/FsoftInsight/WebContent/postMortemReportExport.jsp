<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%><%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>postMortemReportExport.jsp</TITLE>
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
.ColumnLabel1 {
	background-color: #003366;
	color: white;
	font-weight: bold;
	vertical-align: middle;
	text-align: left;
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
.ColumnLabel{
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
	response.addHeader("Content-Disposition", "attachment;filename=postMortemReport.xls");
%>
<%
	int right = 2;
	
	boolean style = false;

	PmReportHeaderInfo pmHeaderInfo =(PmReportHeaderInfo)session.getAttribute("PMHeaderInfo");
	ProjectInfo projectInfo = (ProjectInfo)session.getAttribute("WOGeneralInfo");
	EffortInfo effortInfo = (EffortInfo)session.getAttribute("EffortInfo");
	ConformityNCInfo[] confNCArr = (ConformityNCInfo[])session.getAttribute("ConfNCArr");
	TypeDefectInfo[] typeDefectArr = (TypeDefectInfo[])session.getAttribute("TypeDefectArr");
	
	String[] strArr = (String[])session.getAttribute("strArr");
	
	Vector dar = (Vector)session.getAttribute("dar");	
	Vector risk = (Vector)session.getAttribute("risk");
	Vector tool = (Vector)session.getAttribute("tool");
	Vector norm = (Vector) session.getAttribute("norm");
	Vector stage = (Vector)session.getAttribute("stage");
	Vector module = (Vector)session.getAttribute("module");	
	Vector process = (Vector)session.getAttribute("ProcessEffortPM");
	Vector deliverable = (Vector) session.getAttribute("DeliverableList");
	Vector projectHistory = (Vector) session.getAttribute("ProjectHistory");
	Vector furtherWork = (Vector)session.getAttribute("FurtherWork");
	Vector defectPrevention = (Vector)session.getAttribute("defectPrevention");
	Vector effortDistribution = (Vector)session.getAttribute("EffortDistributionByType");
	Vector skillSet = (Vector) session.getAttribute("ProjectSkillSet");
	Vector skillSet2 = (Vector) session.getAttribute("ProjectSkillSet2");
	Vector WOPerformanceMetrics = (Vector)session.getAttribute("WOPerformanceMetrics");
	Vector WOStandardMetrics = (Vector) session.getAttribute("WOStandardMetrics");
	Vector WOCustomeMetrics = (Vector) session.getAttribute("WOCustomeMetrics");
	
	Vector vtWDefect = (Vector)session.getAttribute("WeightedDefect");
	Vector vtSDefect = (Vector)session.getAttribute("SeverityDefect");
	
	String className = "";
	String approver = "";
	String reviewer = "";
	
	if (pmHeaderInfo.approvers != null) {
		approver = pmHeaderInfo.approvers;
	}
	else if (right != 3) {
		approver = "N/A";
	}
	if (pmHeaderInfo.reviewers != null) {
		reviewer=pmHeaderInfo.reviewers;
	}
	else if (right != 3) {
		reviewer = "N/A";
	}
%>

<TABLE width="95%">
	<TR>
		<TD><P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Postmortemreport")%> </P></TD>
		<TD align="right" valign="bottom"></TD>
	</TR>
</TABLE>
<TABLE class="Table" width="95%" cellspacing="1">
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Reviewers")%>* </TD>
            <TD class="CellBGRnews"><%if(right == 3){%><TEXTAREA rows="4" cols="50" name="txtReviewer"><%=reviewer%></TEXTAREA><%}else{%><%=ConvertString.toHtml(reviewer)%><%}%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Approvers")%>* </TD>
            <TD class="CellBGRnews"><%if(right == 3){%><TEXTAREA rows="4" cols="50" name="txtApprover"><%=approver%></TEXTAREA><%}else{%><%=ConvertString.toHtml(approver)%><%}%></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.postMortemReport.GeneralInformation")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectName")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getProjectName()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel1" width="160"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectCode")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getProjectCode()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ContractType")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getContractType()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.Customer")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getCustomer()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel1">2nd <%=languageChoose.getMessage("fi.jsp.postMortemReport.Customer")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getSecondCustomer()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectLevel")%></TD>
            <TD class="CellBGR3"><%=projectInfo.getProjectLevel()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectRank")%></TD>
            <TD class="CellBGRnews">
            	<%
	            	String rank;
	            	if (projectInfo.getProjectRank() == null) {
	            		rank = "N/A";
	            	}
	            	else if ("?".equals(projectInfo.getProjectRank())) {
	            		rank = "Not Rank";
	            	}
	            	else {
	            	 	rank = ConvertString.toHtml(projectInfo.getProjectRank());
	            	}
            	%>
            	<%=rank%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.Group")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getGroupName()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.Division")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getDivisionName()%></TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectType")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getProjectType()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectManager")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getLeaderName()%></TD>
        </TR>
	    <TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectCategory")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getLifecycle()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.BusinessDomain")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getBusinessDomain()%></TD>
        </TR>
		<TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ApplicationType")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getApplicationType()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel1"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ScopeAndObjective")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(projectInfo.getScopeAndObjective())%></TD>
        </TR>
	</TBODY>
</TABLE>

<p></p>

<TABLE class="Table" width="95%" cellspacing="1">
<CAPTION class="TableCaption"><A name="history"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Projecthistory")%> </A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
        	<TD width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Date")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Event")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Comment")%> </TD>
        </TR>
        <%
        for(int i=0;i<projectHistory.size();i++)
        {
        	style=!style;
 			className=(style)?"CellBGRnews":"CellBGR3";
		 	ProjectHisInfo info = (ProjectHisInfo) projectHistory.elementAt(i);
        %>

        <TR class="<%=className%>">
            <TD align="center"><%=i+1%></TD>
			<TD NOWRAP><%=CommonTools.dateFormat(info.eventDate)%></TD>
			<TD><%=info.events%></TD>
			<TD><%=info.comments%></TD>
        </TR>

        <%
        }
        %>
    </TBODY>
</TABLE>
<p></p>

<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.postMortemReport.Performance")%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Metric")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Unit")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Planned")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Replanned")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Actual")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Deviation")%></TD>
        </TR>
	<%
		MetricInfo metricInfo;
		
		String planVal;
		String rePlanVal;
		String actVal;
		String rowSpan;
		for(int i = 0; i < 8; i++) {
			if ((i != 5) && (i != 6) && (i != 7)) { // HaiMM modify: Remove 3 metrics Development, Management, Quality
				metricInfo = (MetricInfo)WOPerformanceMetrics.elementAt(i);
				style = !style;
			 	className = (style) ? "CellBGRnews" : "CellBGR3";
			 	if (metricInfo.unit.equalsIgnoreCase("dd-mmm-yy")) {
			 		//metric is a date
			 		planVal = CommonTools.dateFormat(metricInfo.plannedValue);
			 		rePlanVal = CommonTools.dateFormat(metricInfo.rePlannedValue);
			 		actVal = CommonTools.dateFormat(metricInfo.actualValue);
				}
			 	else {
			 		//metric is a number
			 		planVal = CommonTools.formatDouble(metricInfo.plannedValue);
			 		rePlanVal = CommonTools.formatDouble(metricInfo.rePlannedValue);
			 		actVal = CommonTools.formatDouble(metricInfo.actualValue);
				}
				if (i == 0 || i == 3 || i > 4) {
					rePlanVal = "";
				}
	 	%>
	 		<TR class="<%=className%>">
	 	<%
	 			if (metricInfo.name.equals("Maximum team size")) {
		%>
				<TD><A href="Fms1Servlet?reqType=<%=Constants.TEAM_SIZE_PROGRESS%>&back=<%=Constants.GET_POST_MORTEM%>"><%=languageChoose.getMessage(metricInfo.name)%></A></TD>
		<%
				}
				else {
		%>
				<TD><%=languageChoose.getMessage(metricInfo.name)%></TD>
		<%
				}
		%>
				<TD><%=metricInfo.unit%></TD>
				<TD><%=planVal%></TD>
				<TD><%=rePlanVal%></TD>
				<TD><%=actVal%></TD>
				<TD><%=Color.colorByNorm(CommonTools.formatDouble(metricInfo.deviation),metricInfo.deviation, metricInfo.lcl, metricInfo.ucl, metricInfo.colorType)%></TD>
			</TR>
		<%
				// huynh2 add some line code.
				// add some code line for billable effort, calendar effort
				if (i==3) {
					metricInfo = (MetricInfo)WOPerformanceMetrics.elementAt(8);
	 				planVal = CommonTools.formatDouble(metricInfo.plannedValue);
	 				rePlanVal = CommonTools.formatDouble(metricInfo.rePlannedValue);
	 				actVal = CommonTools.formatDouble(metricInfo.actualValue);
		%>
			<TR class="<%=className%>">
				<TD><%=languageChoose.getMessage(metricInfo.name)%></TD>
				<TD><%=metricInfo.unit%></TD>
				<TD><%=planVal%></TD>
				<TD><%=rePlanVal%></TD>
				<TD><%=actVal%></TD>
				<TD><%=Color.colorByNorm(CommonTools.formatDouble(metricInfo.deviation),metricInfo.deviation, metricInfo.lcl, metricInfo.ucl, metricInfo.colorType)%></TD>
			</TR>
		<%
			// Calendar effort
			metricInfo = (MetricInfo)WOPerformanceMetrics.elementAt(9);
	 		planVal = CommonTools.formatDouble(metricInfo.plannedValue);
	 		rePlanVal = CommonTools.formatDouble(metricInfo.rePlannedValue);
	 		actVal = CommonTools.formatDouble(metricInfo.actualValue);		
		%>
			<TR class="<%=className%>">
				<TD><%=languageChoose.getMessage(metricInfo.name)%></TD>
				<TD><%=metricInfo.unit%></TD>
				<TD><%=planVal%></TD>
				<TD><%=rePlanVal%></TD>
				<TD><%=actVal%></TD>
				<TD><%=Color.colorByNorm(CommonTools.formatDouble(metricInfo.deviation),metricInfo.deviation, metricInfo.lcl, metricInfo.ucl, metricInfo.colorType)%></TD>
			</TR>
		<%
				}
			}
		}
		className = (style) ? "CellBGRnews" : "CellBGR3";
		style = !style;
		ProjectSizeInfo projectSizeInfo = (ProjectSizeInfo)session.getAttribute("ProjectSizeInfo");
	%>
		<TR class="<%=className%>">
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProductSize")%></TD>
            <TD>UCP</TD>
            <TD><%=CommonTools.formatDouble(projectSizeInfo.totalEstimatedSize)%></TD>
            <TD><%=CommonTools.formatDouble(projectSizeInfo.totalReestimatedSize)%></TD>
            <TD><%=CommonTools.formatDouble(projectSizeInfo.totalActualSize)%></TD>
            <TD><%=CommonTools.formatDouble(projectSizeInfo.totalSizeDeviation)%></TD>
        </TR>
	<%
		className = (style) ? "CellBGRnews" : "CellBGR3";
		style = !style;
		//get the planned effort values
		metricInfo = (MetricInfo)WOPerformanceMetrics.elementAt(4);
		double estimated = Double.NaN;
		double reEstimated = Double.NaN;
		double reEstimatedSize;
		double reEstimatedEffort;
		double actual = Double.NaN;
		double dbldeviation = Double.NaN;
		if (metricInfo.plannedValue != 0) {
			estimated = (double) projectSizeInfo.totalEstimatedSize / metricInfo.plannedValue;
    	}
		if ((!Double.isNaN(projectSizeInfo.totalReestimatedSize)) || (!Double.isNaN(metricInfo.rePlannedValue))) {
    		reEstimatedSize = (Double.isNaN(projectSizeInfo.totalReestimatedSize)
    		? (double)projectSizeInfo.totalEstimatedSize
    		: (double)projectSizeInfo.totalReestimatedSize);

			reEstimatedEffort = (Double.isNaN(metricInfo.rePlannedValue)
							? (double)metricInfo.plannedValue
							:(double)metricInfo.rePlannedValue);
			reEstimated = reEstimatedSize / reEstimatedEffort;
		}
		if (metricInfo.actualValue != 0) {
			actual = (double)projectSizeInfo.totalActualSize / metricInfo.actualValue;
		}
		dbldeviation = CommonTools.metricDeviation(estimated, reEstimated, actual);
	%>
        <TR class="<%=className%>">
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Productivity")%></TD>
            <TD>UCP/pd</TD>
            <TD><%=CommonTools.formatDouble(estimated)%></TD>
            <TD><%=CommonTools.formatDouble(reEstimated)%></TD>
            <TD><%=CommonTools.formatDouble(actual)%></TD>
            <TD><%=CommonTools.formatDouble(dbldeviation)%></TD>
        </TR>
	<%
		className = (style) ? "CellBGRnews" : "CellBGR3";
		style = !style;
		CostTotalInfo totalInfo = (CostTotalInfo)session.getAttribute("CostTotalInfo");	
		double fnAmount = Double.parseDouble(session.getAttribute("FinanceAmountTotal").toString());
		estimated = Double.NaN;
		reEstimated = Double.NaN;
		actual = Double.NaN;
		dbldeviation = Double.NaN;
		if (metricInfo.plannedValue != 0) {
			estimated = totalInfo.cost / metricInfo.plannedValue;
		}
		if (metricInfo.rePlannedValue != 0) {
			reEstimated = totalInfo.cost/metricInfo.rePlannedValue;
		}
		if (metricInfo.actualValue != 0) {
			actual = fnAmount/metricInfo.actualValue;
		}
		dbldeviation = CommonTools.metricDeviation(estimated, reEstimated, actual);
	%>
        <TR class="<%=className%>">
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Efficiency")%></TD>
            <TD>USD/pd</TD>
            <TD><%=CommonTools.formatDouble(estimated)%></TD>
            <TD><%=CommonTools.formatDouble(reEstimated)%></TD>
            <TD><%=CommonTools.formatDouble(actual)%></TD>
            <TD><%=CommonTools.formatDouble(dbldeviation)%></TD>
        </TR>
</TBODY>
</TABLE>
<p></P>
<TABLE cellspacing="1" class="Table" width="95%">
    <CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Deliverables")%> </CAPTION>
    <TR class="ColumnLabel">
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Deliverable")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.DeliveryLocation")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Committeddateofdelivery")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Actualdateofdelivery")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Status")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Scheduledeviaton")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Note")%> </TD>
	</TR>
<%
for(int i = 0; i < deliverable.size(); i++){
	
 	style=!style;
 	className=(style)?"CellBGRnews":"CellBGR3";
 	ModuleInfo info = (ModuleInfo) deliverable.elementAt(i);
%>
	<TR class="<%=className%>">
		<TD><%=info.name%></TD>
		<TD><%=((info.deliveryLocation == null)? "N/A" : info.deliveryLocation)%></TD>
		<TD><%=(info.rePlannedReleaseDate== null)? CommonTools.dateFormat(info.plannedReleaseDate) : CommonTools.dateFormat(info.rePlannedReleaseDate)%></TD>
		<TD><%=((info.actualReleaseDate == null)? "N/A" : CommonTools.dateFormat(new java.util.Date(info.actualReleaseDate.getTime())))%></TD>
		<TD>
<%
switch(info.status){
	case 1:%> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Pending")%> <%break;
	case 2:%> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Accepted")%> <%break;
	case 3:%> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Rejected")%> <%break;
	case 4:%> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Cancelled")%> <%break;
	case 6:%> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Inprogress")%> <%break;
	case 5:%> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Others")%> <%break;
}%>
		</TD>
		<TD><%=CommonTools.formatDouble(info.deviation)%></TD>
		<TD><%=ConvertString.toHtml(info.note)%></TD>
	</TR>
<%}%>
</TBODY>
</TABLE>
<p></p>
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
for(int i = 0; i < WOStandardMetrics.size() - 1; i++){
NormInfo normInfo =(NormInfo)WOStandardMetrics.elementAt(i);
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
if (i ==4)
    if (effortInfo != null){
		normInfo.actualValue = effortInfo.perCorrectionEffort;
    }
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
		for (int i = 0; i < WOCustomeMetrics.size(); i++) {
			style = !style;
 			className = (style) ? "CellBGRnews" : "CellBGR3";
 			WOCustomeMetricInfo info = (WOCustomeMetricInfo)WOCustomeMetrics.elementAt(i); 	
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

<p></p>
<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Projectschedule")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Stage")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Isontime")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Plannedduration")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Actualduration")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Durationdeviation")%> </TD>
        </TR>
        <%
        	for (int i=0; i < stage.size(); i++) {
        		StageInfo stageInfo = (StageInfo)stage.get(i);
        		style = !style;
 				className = (style) ? "CellBGRnews" : "CellBGR3";
				if (stageInfo.isOntime.equalsIgnoreCase("Yes")) {
					stageInfo.isOntime = Color.setColor(Color.GOODMETRIC,stageInfo.isOntime);
				}
				else if (stageInfo.isOntime.equalsIgnoreCase("No")) {
  				     stageInfo.isOntime = Color.setColor(Color.BADMETRIC,stageInfo.isOntime);
  				}
        %>
        <TR class="<%=className%>">
            <TD><%=ConvertString.toHtml(stageInfo.stage)%></TD>
            <TD><%=stageInfo.isOntime%></TD>
            <TD><%=stageInfo.pDuration%></TD>
            <TD><%=stageInfo.aDuration%></TD>
            <TD><%=stageInfo.duDeviation%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Productschedule")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Productname")%> </TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Plannedreleasedate")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Actualreleasedate")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Devitation")%>(%)</TD>
        </TR>
        <%
        	for (int i = 0; i < module.size(); i++) {
        		ModuleInfo modun = (ModuleInfo)module.get(i);
        		style = !style;
 				className = (style) ? "CellBGRnews" : "CellBGR3";
        %>
        <TR class=<%=className%>>
            <TD><%=ConvertString.toHtml(modun.name)%></TD>
            <TD><%=CommonTools.dateFormat(modun.thePlanReleaseDate)%></TD>
            <TD><%=CommonTools.dateFormat(modun.actualReleaseDate)%></TD>
            <TD><%=CommonTools.formatDouble(modun.deviation)%></TD>
        </TR>
        <%
        	}
        %>
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Effortdistributionbyprocesses")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Process")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Plannedpd")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Plannedpercent")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Actualpd")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Actualpercent")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Deviation")%> </TD>
        </TR>
        <%         	
        	double totalP=CommonTools.parseDouble(strArr[0]);
        	double totalA=CommonTools.parseDouble(strArr[1]);
        	double totalP2=0;
        	double totalA2=0;
        	for(int i=0;i<process.size();i++)
        	{
        		ProcessEffortPMInfo processInfo = (ProcessEffortPMInfo)process.get(i);
        		style=!style;
 				className=(style)?"CellBGRnews":"CellBGR3";
  				if((totalP > 0)&&(!Double.isNaN(processInfo.plan1))){
  					processInfo.plan2=processInfo.plan1*100d/totalP;
  					totalP2+=processInfo.plan2;

  				}
  				if((totalA > 0)&&(!Double.isNaN(processInfo.actual1))){
  					processInfo.actual2=processInfo.actual1*100d/totalA;
  					totalA2+=processInfo.actual2;
  				}
        %>
        <TR class=<%=className%>>
            <TD><%=ConvertString.toHtml(processInfo.process)%></TD>
            <TD><%=CommonTools.formatDouble(processInfo.plan1)%></TD>
            <TD><%=CommonTools.formatDouble(processInfo.plan2)%></TD>
            <TD><%=CommonTools.formatDouble(processInfo.actual1)%></TD>
            <TD><%=CommonTools.formatDouble(processInfo.actual2)%></TD>
            <TD><%=CommonTools.formatDouble(processInfo.deviation)%></TD>
        </TR>
        <%}
  		String ttd=null;
		if (totalP>0 && totalA>=0)
  			ttd=CommonTools.formatDouble((totalA - totalP)*100d/totalP);
  		else
  			ttd="N/A";
       %>
        <TR class="TableLeft">
            <TD><B> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Total")%> </B></TD>
            <TD><B><%=strArr[0]%></B></TD>
            <TD><B> 100 </B></TD>
            <TD><B><%=strArr[1]%></B></TD>
            <TD><B> 100 </B></TD>
            <TD><B><%=ttd%></B></TD>
        </TR>
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Effortdistributionbytype")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Type")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Effortpd")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Effort")%> </TD>

        </TR>
        <%         	
        	EffortDistributionByTypeInfo effortDistributionByType=(EffortDistributionByTypeInfo)effortDistribution.get(effortDistribution.size()-1);
        	float total=-1;
        	totalP=-1;
        	boolean flag=false;
        	if(ConvertString.isNumber(effortDistributionByType.effortPD)){
        		total=Float.parseFloat(effortDistributionByType.effortPD);
        	}
        	float tempFloat=0;
        	for(int i=0;i<effortDistribution.size()-1;i++)
        	{
        		effortDistributionByType=(EffortDistributionByTypeInfo)effortDistribution.get(i);
        		tempFloat=0;
        		style=!style;
 				className=(style)?"CellBGRnews":"CellBGR3";
  				if(total>0){
  					if(ConvertString.isNumber(effortDistributionByType.effortPD)){
  						tempFloat=100*Float.parseFloat(effortDistributionByType.effortPD)/total;
  						effortDistributionByType.effortPC=CommonTools.formatDouble(tempFloat);
  					}
  					totalP+=tempFloat;
  					flag=true;
  				}
  				
        %>
        <TR class=<%=className%>>
            <TD><%=ConvertString.toHtml(languageChoose.getMessage(effortDistributionByType.type))%></TD>
            <TD><%=effortDistributionByType.effortPD%></TD>
            <TD><%=effortDistributionByType.effortPC%></TD>
        </TR>

        <%
        	effortDistributionByType=(EffortDistributionByTypeInfo)effortDistribution.get(i+1);
        	}
        	if(flag){
        		effortDistributionByType.effortPC=CommonTools.formatDouble(totalP);
        	}
        	style=!style;
 			className=(style)?"CellBGRnews":"CellBGR3";
        %>

        <TR class="TableLeft">
            <TD><B><%=ConvertString.toHtml(effortDistributionByType.type)%></B></TD>
            <TD><B><%=effortDistributionByType.effortPD%></B></TD>
            <TD><B> 100.0 </B></TD>
        </TR>

    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.WeighteddefectdistributionbyQC")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Detectionon")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Injectiononrequirement")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Injectionondesign")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Injectiononcoding")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Injectiononother")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Weighteddefects")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.oftotal")%> </TD>
        </TR>
<%
	WeightedDefectInfo totalWeightedD=(WeightedDefectInfo)vtWDefect.get(vtWDefect.size()-1);
	total=totalWeightedD.weightedDefect;

	String totalStr="";
	float allTotal=0;
	for(int i=0;i<vtWDefect.size()-1;i++){
		style=!style;
 		className=(style)?"CellBGRnews":"CellBGR3";
		WeightedDefectInfo info=(WeightedDefectInfo)vtWDefect.get(i);
		if(total!=0){
			tempFloat =info.weightedDefect*100/total;
			totalStr=CommonTools.formatDouble(tempFloat);
			allTotal+=tempFloat;
		}
		
%>
        <TR class=<%=className%>>
            <TD><%=languageChoose.getMessage(info.detection)%></TD>
            <TD><%=info.injRequirement%></TD>
            <TD><%=info.injDesign%></TD>
            <TD><%=info.injCoding%></TD>
            <TD><%=info.injOther%></TD>
            <TD><%=info.weightedDefect%></TD>
            <TD><%=totalStr%></TD>
        </TR>
<%
	}
	totalStr=CommonTools.formatDouble(allTotal);
	style=!style;
 	className=(style)?"CellBGRnews":"CellBGR3";
%>
	<TR class="TableLeft">
    	<TD><B><%=languageChoose.getMessage(totalWeightedD.detection)%></B></TD>
        <TD><B><%=totalWeightedD.injRequirement%></B></TD>
        <TD><B><%=totalWeightedD.injDesign%></B></TD>
        <TD><B><%=totalWeightedD.injCoding%></B></TD>
        <TD><B><%=totalWeightedD.injOther%></B></TD>
        <TD><B><%=totalWeightedD.weightedDefect%></B></TD>
        <TD><B>100.0</B></TD>
    </TR>
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Defectdistributionbyseverity")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Severity")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Numberofdefects")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.oftotal")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Numberofdefectsbyreview")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.oftotalbyreview")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Numberofdefectsbytest")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.oftotalbytest")%> </TD>
        </TR>
        <%
        	SeverityDefectInfo totalSeverityD=(SeverityDefectInfo)vtSDefect.get(vtSDefect.size()-1);
			total=totalSeverityD.nDreview+totalSeverityD.nDtest+totalSeverityD.nDothers;
		
			totalStr="";
			allTotal=0;
			float allTotalr=0;
			float allTotalt=0;
			String pcRstr="";
			String pcTstr="";
			
			for(int i=0;i<vtSDefect.size()-1;i++){
				style=!style;
 				className=(style)?"CellBGRnews":"CellBGR3";
				SeverityDefectInfo info=(SeverityDefectInfo)vtSDefect.get(i);
			
				if(total!=0){
					tempFloat=(info.nDreview+info.nDtest)*(float)100.0/total;
					totalStr=CommonTools.formatDouble(tempFloat);						
					allTotal+=tempFloat;
				}
				if(totalSeverityD.nDreview!=0){
					tempFloat=info.nDreview*(float)100.0/totalSeverityD.nDreview;
					pcRstr=CommonTools.formatDouble(tempFloat);
					allTotalr+=tempFloat;
				}
				if(totalSeverityD.nDtest!=0){
					tempFloat=info.nDtest*(float)100.0/totalSeverityD.nDtest;
					pcTstr=CommonTools.formatDouble(tempFloat);
					allTotalt+=tempFloat;
				}
        %>
        <TR class=<%=className%>>
            <TD><%=languageChoose.getMessage(info.severity)%></TD>
            <TD><%=info.nDreview+info.nDtest+info.nDothers%></TD>
            <TD><%=totalStr%></TD>
            <TD><%=info.nDreview%></TD>
            <TD><%=pcRstr%></TD>
            <TD><%=info.nDtest%></TD>
            <TD><%=pcTstr%></TD>
        </TR>
        <%
        	}
        	style=!style;
 			className=(style)?"CellBGRnews":"CellBGR3";
  			totalStr=CommonTools.formatDouble(allTotal);
  			pcRstr=CommonTools.formatDouble(allTotalr);
  			pcTstr=CommonTools.formatDouble(allTotalt);
        %>
        <TR class="TableLeft">
            <TD><B><%=languageChoose.getMessage(totalSeverityD.severity)%></B></TD>
            <TD><B><%=totalSeverityD.nDreview+totalSeverityD.nDtest+totalSeverityD.nDothers%></B></TD>
            <TD><B> 100.0 </B></TD>
            <TD><B><%=totalSeverityD.nDreview%></B></TD>
            <TD><B> 100.0 </B></TD>
            <TD><B><%=totalSeverityD.nDtest%></B></TD>
            <TD><B>100.0 </B></TD>
        </TR>
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Defectdistributionbyclassifica")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Classificationtype")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Fatal")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Serious")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Medium")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Cosmetic")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.WeightedDefect")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.oftotal")%> </TD>
        </TR>
        <%
        	int size=typeDefectArr.length-1;
        	total=typeDefectArr[size].weighted;
        	String strPC="";
        	allTotal=0;
        	for(int i=0; i< size; i++)
        	{
        		style=!style;
 				className=(style)?"CellBGRnews":"CellBGR3";
        	strPC="";
        	if(total!=0){
        		tempFloat=typeDefectArr[i].weighted*100/total;
        		strPC=CommonTools.formatDouble(tempFloat);
        		allTotal+=tempFloat;
        	}
        %>
        <TR class="<%=className%>">
            <TD><%=languageChoose.getMessage(typeDefectArr[i].type)%></TD>
            <TD><%=typeDefectArr[i].fatal%></TD>
            <TD><%=typeDefectArr[i].serious%></TD>
            <TD><%=typeDefectArr[i].medium%></TD>
            <TD><%=typeDefectArr[i].cosmetic%></TD>
            <TD><%=typeDefectArr[i].weighted%></TD>
            <TD><%=strPC%></TD>
        </TR>
        <%
        	}
        	strPC=CommonTools.formatDouble(allTotal);
        	style=!style;
 			className=(style)?"CellBGRnews":"CellBGR3";
        %>
		<TR class="TableLeft">
            <TD><B><%=languageChoose.getMessage(typeDefectArr[size].type)%></B></TD>
            <TD><B><%=typeDefectArr[size].fatal%></B></TD>
            <TD><B><%=typeDefectArr[size].serious%></B></TD>
            <TD><B><%=typeDefectArr[size].medium%></B></TD>
            <TD><B><%=typeDefectArr[size].cosmetic%></B></TD>
            <TD><B><%=typeDefectArr[size].weighted%></B></TD>
            <TD><B> 100.0</B></TD>

        </TR>
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Conformitywithprocess")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Foundin")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.NumberofNCs")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.NumberofObs")%> </TD>
        </TR>
        <%        	
        	for(int i=0; i<confNCArr.length; i++)
        	{
        		style=!style;
 				className=(style)?"CellBGRnews":"CellBGR3";       	
  				if (i!=confNCArr.length-1)
  				{
        %>
			        <TR class="<%=className%>">
			            <TD><%=ConvertString.toHtml(languageChoose.getMessage(confNCArr[i].type))%></TD>
			            <TD><%=confNCArr[i].numNC%></TD>
			            <TD><%=confNCArr[i].numOb%></TD>
			        </TR>
        <%
        		}
        		else
        		{
        %>
			        <TR class="TableLeft">
			            <TD><B><%=ConvertString.toHtml(languageChoose.getMessage(confNCArr[i].type))%></B></TD>
			            <TD><B><%=confNCArr[i].numNC%></B></TD>
			            <TD><B><%=confNCArr[i].numOb%></B></TD>
			        </TR>
        <%
        		}
        	}
        %>
    </TBODY>
</TABLE>
<P></P>

<FORM method="POST" name="frmDPTask">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><A name="defectprevention"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Defectpreventiongoals")%> </A></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="24" align="center">#</TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Item")%> </TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Unit")%> </TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Planned")%> </TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Actual")%> </TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Deviation")%> </TD>
            <TD class="ColumnLabel">Cause</TD>
        </TR>
        <%
        	boolean bl = true;
        	String rowStyle = "";
        	
        	for(int i=0;i<defectPrevention.size();i++)
        	{
        		if(bl)
        			rowStyle="CellBGRnews";
  				else
  					rowStyle = "CellBGR3";
  				
  				bl=!bl;
  				
        		DPTaskInfo dpTaskInfo = (DPTaskInfo)defectPrevention.get(i);
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><%=dpTaskInfo.item%></A></TD>
            <TD><%=dpTaskInfo.unit%></TD>
            <TD><%=CommonTools.formatDouble(dpTaskInfo.planValue)%></TD>
            <TD><%=CommonTools.formatDouble(dpTaskInfo.actualValue)%></TD>
           <%  String s = CommonTools.formatDouble(dpTaskInfo.deviationValue);          	   	
           	   if (dpTaskInfo.deviationValue > 20) {%>
            	    <TD> <%=Color.setColor(Color.BADMETRIC, s)%></TD>
	           <%} else if (dpTaskInfo.deviationValue<0) {%>
	            	<TD><%=Color.setColor(Color.GOODMETRIC, s)%></TD>
	           <%} else {%>
	           		<TD><%=Color.setColor(Color.NOCOLOR, s)%></TD>
	           <%}            	
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
        	for(int i=0;i<dar.size();i++){
       			rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				DARPlanInfo darPlanInfo = (DARPlanInfo) dar.get(i);      		       		
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
        <%}%>
    </TBODY>
</TABLE>

<br>

<!-- Begin Causal Analysis Table -->
<%
    int j = 0;
	flag = false;
	String deviation;
	String deviationFromTarget;
	boolean disabled = true;
%>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"><A name="causal"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.CausalAnalysis")%> </A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD rowspan="2" width="24" align="center">#</TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Name")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Unit")%> </TD>
            <TD rowspan="1" colspan="3" align="center"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.TargetedValue")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ActualValue")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.DeviationFromNorm")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.DeviationFromTarget")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Cause")%> </TD>
        </TR>

	    <TR class="ColumnLabel">
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%>&nbsp;&nbsp;</TD>
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>&nbsp;&nbsp;</TD>
	    	<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>&nbsp;&nbsp;</TD>
        </TR>
        
	<%
		for(int i = 0; i < norm.size(); i++) {
			deviationFromTarget = "";
			NormInfo normInfo = (NormInfo)norm.get(i);
			// Add by HaiMM - Start: Remove Project Management & Quality Cost metrics
			if ((normInfo.metricID == MetricDescInfo.PROJECT_MANAGEMENT
                    || normInfo.metricID == MetricDescInfo.QUALITY_COST)) {
                    continue;
             }
			
			// Add by HaiMM - End
			
			//Do not show Acceptance_Rate and Requirement_Completeness if their actual value is 100%
			if ((normInfo.metricID == MetricDescInfo.ACCEPTANCE_RATE
                    || normInfo.metricID == MetricDescInfo.REQUIREMENT_COMPLETENESS)
                    && (normInfo.actualValue == 100)) {
                    continue;
             }
	        
	        if (normInfo.cause != null && normInfo.cause.trim().length() != 0) {
	        	disabled = false;
			}
			//only display metrics out of norms (only out of norm are formated by Color)
	        deviation = Color.colorByNorm(CommonTools.formatDouble(normInfo.normDeviation),normInfo.actualValue, normInfo.lcl, normInfo.ucl, normInfo.colorType);
			if(!CommonTools.formatDouble(normInfo.lsl).equals("N/A") && !CommonTools.formatDouble(normInfo.usl).equals("N/A")){
				deviationFromTarget = Color.colorByNorm(CommonTools.formatDouble(normInfo.planDeviation), normInfo.actualValue, normInfo.usl, normInfo.lsl, normInfo.colorType);
			}
			if (!"N/A".equals(deviation)) {
				j = j+1;
			}
			if (!deviation.startsWith("<")) {
				continue;
			}
			style = !style;
	 		className = (style) ? "CellBGRnews" : "CellBGR3";
	%>
	        <TR class=<%=className%>>
	            <TD align="center"><%=normInfo.normID%></TD>
	            <TD><%=languageChoose.getMessage(normInfo.normName)%></TD>
	            <TD><%=normInfo.normUnit%></TD>
	            <TD><%=normInfo.usl%></TD>
	            <TD><%=CommonTools.formatDouble(normInfo.plannedValue)%></TD>
	            <TD><%=normInfo.lsl%></TD>	           
	            <TD><%=CommonTools.formatDouble(normInfo.actualValue)%></TD>            
	            <TD><%=deviation%></TD>
   	            <TD><%=deviationFromTarget%></TD>
	            <TD><%=(normInfo.cause==null)?"":normInfo.cause%></TD>
	        </TR>
	<%
		}
	%>
<%-- allow display custom metric follow Buz rule --%>	
	<% 
 		int l=0;
		for (int i = 0; i < WOCustomeMetrics.size(); i++) {
 			className = (style) ? "CellBGRnews" : "CellBGR3";
 			WOCustomeMetricInfo info = (WOCustomeMetricInfo)WOCustomeMetrics.elementAt(i);
 			if((CommonTools.formatDouble(info.actualValue) != "N/A" && CommonTools.formatDouble(info.LCL) != "N/A" && CommonTools.formatDouble(info.UCL) != "N/A" && (info.actualValue < info.LCL || info.actualValue > info.UCL)) || (CommonTools.formatDouble(info.actualValue) != "N/A" && CommonTools.formatDouble(info.plannedValue) != "N/A" && info.actualValue != info.plannedValue)){
				l++;
				style = !style;
				className = (style) ? "CellBGRnews" : "CellBGR3";
			%>
				<TR class="<%=className%>">
					<TD align="center">14.<%=l%></TD>
					<TD><%=ConvertString.toHtml(info.name)%></TD>
					<TD><%=ConvertString.toHtml(info.unit)%></TD>
					<TD></TD>
					<TD><%=CommonTools.formatDouble(info.plannedValue)%></TD>
					<TD></TD>
					<TD><%=CommonTools.formatDouble(info.actualValue)%></TD>
					<TD><%=info.deviation%></TD>
					<TD></TD>
					<TD><%=ConvertString.toHtml(info.causal)%></TD>
				</TR>
			<%
				}
			}	
	%>
</TABLE>
<!-- End Causal Analysis Table -->
<BR>
<TABLE class="Table" width="95%" cellspacing="1">
    <COL span="1" width="24">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Risksinproject")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD rowspan="2" width="20%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Actualriskscenario")%> </TD>
            <TD rowspan="2" width="20%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Actualactions")%> </TD>
            <TD colspan="3" align="center" width="35%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Actualimpact")%> </TD>
            <TD rowspan="2" width="10%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Unplanned")%> </TD>
            <TD rowspan="2" width="10%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Assessmentdate")%> </TD>
        </TR>
        <TR class="ColumnLabel">
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Impactto")%> </TD>
            <TD width="5%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Unit")%> </TD>
            <TD width="10%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Estimatedimpact")%> </TD>
        </TR>
        <%
        String strRowSpan;
        if (risk != null)

       	for (int i = 0; i < risk.size(); i++) {
        	RiskInfo riskInfo = (RiskInfo)risk.elementAt(i);
        	int rowspan=0;
        	String htmlRows="";
        	String actImp ="";
        	if(riskInfo.status==2){
        		style=!style;
 				className=(style)?"CellBGRnews":"CellBGR3";
	        	for (j =0;j<3;j++){
	        		if (!riskInfo.imp[j].equals("")){
	        			rowspan++;
		        		if (rowspan ==1)
		        			actImp="<TD>"+languageChoose.getMessage(riskInfo.imp[j])+"</TD><TD>"+riskInfo.unt[j]+"</TD><TD>"+riskInfo.est[j]+"</TD>";
		        		else
		        			htmlRows+="<TR class=\""+className+"\"><TD>"+riskInfo.imp[j]+"</TD><TD>"+riskInfo.unt[j]+"</TD><TD>"+riskInfo.est[j]+"</TD></TR>";
		        		}
		        	}
	        	strRowSpan="";
	        	if (rowspan!=1)
	        		strRowSpan = "rowspan =\""+rowspan+"\"";
        %><TR class="<%=className%>">
            <TD <%=strRowSpan%>><%=riskInfo.actualRiskScenario != null ? riskInfo.actualRiskScenario : "N/A"%></TD>
            <TD <%=strRowSpan%>><%=riskInfo.actualAction != null ? riskInfo.actualAction : "N/A" %></TD>
           <%=actImp%>
            <TD <%=strRowSpan%> ><%=riskInfo.unplanned == 1 ? languageChoose.getMessage("fi.jsp.postMortemReportExport.Yes") : languageChoose.getMessage("fi.jsp.postMortemReportExport.No")%></TD>
            <TD <%=strRowSpan%>><%=CommonTools.dateFormat(riskInfo.assessmentDate)%></TD>
        </TR>
        <%
        		if (rowspan!=1){%>
        		<%=htmlRows%>
        		<%}


       		}
        }
        %>
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Toolsused")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Item")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Purpose") %></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Description")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Status")%></TD>
            <TD width="11%"><%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Duedate")%></TD>


        </TR>
        <%
		String pur,des,dueD;
		ToolInfo toolInfo=null;
        for(int i=0;i<tool.size();i++)
        {
        	style=!style;
 			className=(style)?"CellBGRnews":"CellBGR3";
  			pur="N/A";
  			des="N/A";
  			
  				
        	
        	toolInfo=(ToolInfo)tool.get(i);
        	     	
        	if(toolInfo.purpose!=null){
        		pur=toolInfo.purpose;        		
        		if(pur.length()>50) pur=pur.substring(0,50)+"...";
        	}
        	if(toolInfo.description!=null){
        		des=toolInfo.description;
        		if(des.length()>50) des=des.substring(0,50)+"...";
        	}        	
        	
        	if(toolInfo.dueD==null){
        		dueD="N/A";
        	}
        	else{
        		dueD=CommonTools.dateFormat(toolInfo.dueD);
        	}
        %>	
        <TR class="<%=className%>">
            <TD><%=ConvertString.toHtml(toolInfo.name)%></TD>
            <TD><%=ConvertString.toHtml(pur)%></TD>
            <TD><%=ConvertString.toHtml(des)%></TD>
            <TD><%=ConvertString.toHtml(languageChoose.getMessage(toolInfo.status))%></TD>
            <TD><%=dueD%></TD>

        </TR>
        <%
        	}
        %>
    </TBODY>
</TABLE>
<p></p>

<TABLE cellspacing="1" class="Table" width="95%" id="tableTeam">
<CAPTION class="TableCaption"><A name="teamevaluation"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Teamevaluation")%> </A></CAPTION>
<TR>
<TD class="ColumnLabel" width = "24" align = "center">#</TD>
<TD class="ColumnLabel" width = "150"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Name")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Account")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Role")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Process")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.SkillTechnology")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Point")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Comment")%> </TD>
</TR>
<TBODY>
<%
int countRow = 1;
boolean isMove = false;
long tempAssignmentId = 0;
int ii = 0;
for(int i = 0; i < skillSet.size(); i++){
	
	className = "";
 	if (i%2==0) className="CellBGRnews";
 	else className = "CellBGR3";
 	
 	SkillInfo info = (SkillInfo) skillSet.elementAt(i);
 	
 	int k;
 	for(k = 0; k < skillSet2.size(); k++){
	 	SkillInfo info2 = (SkillInfo) skillSet2.elementAt(k);
	 	if (info2.assignmentId == info.assignmentId) {
	 		countRow = info2.point;
	 		break;
	 	}
	 }

 	if (tempAssignmentId != info.assignmentId)
 	{
		isMove = true;
		tempAssignmentId = info.assignmentId;
	}
	else
	{
		isMove = false;
	}
	
	if (countRow > 1) {
%>
<TR class="<%=className%>">
<%
	 	if (isMove) {
			ii = ii + 1;
%>
<TD width="24" align="center" rowspan=<%=countRow%>><%=ii%></TD>
<TD rowspan=<%=countRow%>><%=info.fullName%></TD>
<TD rowspan=<%=countRow%>><%=((info.account == null)?"N/A" : ConvertString.toHtml(info.account))%></TD>
<TD><%=((info.projectRole == null)? "N/A" : languageChoose.getMessage(info.projectRole))%></TD>
<TD><%=((info.process == null)? "N/A" : languageChoose.getMessage(info.process))%></TD>
<TD><%=((info.skill == null)? "N/A" : info.skill)%></TD>
<TD><%=((Integer.toString(info.point) == null)? "N/A" : Integer.toString(info.point))%></TD>
<TD><%=((info.skillComment == null)? "N/A" : info.skillComment)%></TD>
<%
		}
		else
		{
%>
<TD><%=((info.projectRole == null)? "N/A" : languageChoose.getMessage(info.projectRole))%></TD>
<TD><%=((info.process == null)? "N/A" : languageChoose.getMessage(info.process))%></TD>
<TD><%=((info.skill == null)? "N/A" : info.skill)%></TD>
<TD><%=((Integer.toString(info.point) == null)? "N/A" : Integer.toString(info.point))%></TD>
<TD><%=((info.skillComment == null)? "N/A" : info.skillComment)%></TD>
<%
		}
%>
</TR>
<%	 }
	 else
	 {
		ii = ii + 1;
%>
<TR class="<%=className%>">
<TD  width="24" align="center"><%=ii%></TD>
<TD><%=info.fullName%></TD>
<TD><%=((info.account == null)?"N/A" : ConvertString.toHtml(info.account))%></TD>
<TD><%=((info.projectRole == null)? "N/A" : languageChoose.getMessage(info.projectRole))%></TD>
<TD><%=((info.process == null)? "N/A" : languageChoose.getMessage(info.process))%></TD>
<TD><%=((info.skill == null)? "N/A" : info.skill)%></TD>
<TD><%=((Integer.toString(info.point) == null)? "N/A" : Integer.toString(info.point))%></TD>
<TD><%=((info.skillComment == null)? "N/A" : info.skillComment)%></TD>
</TR>
<%
	}
}
%>
</TBODY>
</TABLE>
<BR>

<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION class="TableCaption"><A name="furtherwork"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Furtherworklist")%> </A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Item")%> </TD>
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Resulttobedone")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Timetododay")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Responsibility")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReportExport.Note")%> </TD>
        </TR>
        <%
        	String result;        	
        	String note;
        	String res;
        	String time;
       		
        	for(int i=0;i<furtherWork.size();i++)
        	{
        		FurtherWorkInfo wfInfor=(FurtherWorkInfo)furtherWork.get(i);
        		
        		style=!style;
 				className=(style)?"CellBGRnews":"CellBGR3";
  				
  				time="N/A";
  				result=wfInfor.result;  							
  				note=wfInfor.note;
  				res=wfInfor.responsibility;
  				
  				if(result.length()>50) result=result.substring(0,50)+"...";
  				if(note.length()>50) note=note.substring(0,50)+"...";
  				if(res.length()>50) res=res.substring(0,50)+"...";
        		        		
        		if(wfInfor.time!=-1){
        			time=CommonTools.formatDouble(wfInfor.time);
        		}        		
        %>
        <TR class=<%=className%>>
            <TD align="center"><%=i+1%></TD>
            <TD><%=ConvertString.toHtml(wfInfor.name)%></TD>
            <TD><%=ConvertString.toHtml(result)%></TD>
            <TD><%=time%></TD>
            <TD><%=ConvertString.toHtml(res)%></TD>
            <TD><%=(note.equals("N/A"))?"":ConvertString.toHtml(note)%></TD>
        </TR>
        <%
        	}
        %>
    </TBODY>
</TABLE>
</BODY>
</HTML>
