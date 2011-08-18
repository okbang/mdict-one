<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>postMortemReport.jsp</TITLE></HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
		if (Integer.parseInt(archiveStatus) == 4) {
			isArchive = true;
		}
	}
%>
<BODY onload="loadPrjMenu()" class="BD">
<% 
	if (request.getParameter("noStage") != null) { 
%> 
    <P class="TITLE"><%=languageChoose.getMessage("fi.jsp.postMortemReport.Post_mortem_report")%></P> 
    <P> 
    <P class="ERROR"><%=languageChoose.getMessage("fi.jsp.postMortemReport.Nomilestonesdefinedorcompleted")%></P> 
<%
	}
    else {

	int right = Security.securiPage("Project reports", request, response);
	boolean style = false;
	
	PmReportHeaderInfo pmHeaderInfo = (PmReportHeaderInfo)session.getAttribute("PMHeaderInfo");
	ProjectInfo projectInfo = (ProjectInfo)session.getAttribute("WOGeneralInfo");
	EffortInfo effortInfo = (EffortInfo)session.getAttribute("EffortInfo");

	ConformityNCInfo[] confNCArr = (ConformityNCInfo[])session.getAttribute("ConfNCArr");
	TypeDefectInfo[] typeDefectArr = (TypeDefectInfo[])session.getAttribute("TypeDefectArr");

	String[] strArr = (String[])session.getAttribute("strArr");
	
	Vector dar = (Vector)session.getAttribute("dar");
	Vector risk = (Vector)session.getAttribute("risk");
	Vector tool = (Vector)session.getAttribute("tool");
	Vector norm = (Vector)session.getAttribute("norm");
	Vector stage = (Vector)session.getAttribute("stage");
	Vector module = (Vector)session.getAttribute("module");
	Vector process = (Vector)session.getAttribute("ProcessEffortPM");
	Vector deliverable = (Vector)session.getAttribute("DeliverableList");
	Vector projectHistory = (Vector)session.getAttribute("ProjectHistory");
	Vector furtherWork = (Vector)session.getAttribute("FurtherWork");
	Vector defectPrevention = (Vector)session.getAttribute("defectPrevention");
	Vector effortDistribution = (Vector)session.getAttribute("EffortDistributionByType");
	Vector skillSet = (Vector)session.getAttribute("ProjectSkillSet");
	Vector skillSet2 = (Vector)session.getAttribute("ProjectSkillSet2");
	Vector WOPerformanceMetrics = (Vector)session.getAttribute("WOPerformanceMetrics");
	Vector WOStandardMetrics = (Vector)session.getAttribute("WOStandardMetrics");
	Vector WOCustomeMetrics = (Vector)session.getAttribute("WOCustomeMetrics");
	
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
		reviewer = pmHeaderInfo.reviewers;
	}
	else if (right != 3) {
		reviewer = "N/A";
	}
%>
<BODY onload="loadPrjMenu()" class="BD">
<FORM method="post" name="frm">
<TABLE width="95%">
	<TR>
		<TD><P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.PostMortemReport")%></P></TD>
		<TD align="right" valign="bottom"><A href="postMortemReportExport.jsp" target="about:blank"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ExportPostmortemReport")%></A></TD>
	</TR>
</TABLE>
<TABLE class="Table" width="95%" cellspacing="1">
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.postMortemReport.Reviewers")%>*</TD>
            <TD class="CellBGRnews"><%if(right == 3){%><TEXTAREA rows="4" cols="50" name="txtReviewer"><%=reviewer%></TEXTAREA><%}else{%><%=ConvertString.toHtml(reviewer)%><%}%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.postMortemReport.Approvers")%>*</TD>
            <TD class="CellBGRnews"><%if(right == 3){%><TEXTAREA rows="4" cols="50" name="txtApprover"><%=approver%></TEXTAREA><%}else{%><%=ConvertString.toHtml(approver)%><%}%></TD>
        </TR>
    </TBODY>
</TABLE>
<p><%if(right == 3 && !isArchive){%><INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.postMortemReport.Update")%>" class="BUTTON" onclick="updateHeader();"><%}%></p>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.postMortemReport.GeneralInformation")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectName")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getProjectName()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectCode")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getProjectCode()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ContractType")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getContractType()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.Customer")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getCustomer()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">2nd <%=languageChoose.getMessage("fi.jsp.postMortemReport.Customer")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getSecondCustomer()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectLevel")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getProjectLevel()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectRank")%></TD>
            <TD class="CellBGRnews">
            	<%
	            	String rank;
	            	if (projectInfo.getProjectRank() == null)
	            		rank = "N/A"; 
	            	else if ("?".equals(projectInfo.getProjectRank()))
	            		rank = "Not Rank";
	            	else
	            	 	rank = projectInfo.getProjectRank();
            	%>
            	<%=rank%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.Group")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getGroupName()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.Division")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getDivisionName()%></TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectType")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getProjectType()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectManager")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getLeaderName()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectCategory")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getLifecycle()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.BusinessDomain")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getBusinessDomain()%></TD>
        </TR>
		<TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ApplicationType")%></TD>
            <TD class="CellBGRnews"><%=projectInfo.getApplicationType()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.postMortemReport.ScopeAndObjective")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(projectInfo.getScopeAndObjective())%></TD>
        </TR>
    </TBODY>
</TABLE>

<p></p>

<TABLE class="Table" width="95%" cellspacing="1">
<CAPTION class="TableCaption"><A name="history"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectHistory")%></A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">            
        	<TD width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Date")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Event")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Comment")%> </TD>
        </TR>
	<%
        for (int i = 0; i < projectHistory.size(); i++) {
        	style = !style;
 			className = (style) ? "CellBGRnews" : "CellBGR3";
		 	ProjectHisInfo info = (ProjectHisInfo)projectHistory.elementAt(i);
	%>
        <TR class="<%=className%>">            
            <TD align="center"><%=i+1%></TD>
        <%
        	if (!info.link.equalsIgnoreCase(info.NOLINK)) {
        %>
			<TD NOWRAP><A href="<%=info.link %>"><%=CommonTools.dateFormat(info.eventDate)%></A></TD>
        <%
        	}
        	else {
        %>
			<TD NOWRAP><%=CommonTools.dateFormat(info.eventDate)%></TD>
        <%
        	}
        %>
			<TD><%=info.events%></TD>
			<TD><%=info.comments%></TD>
        </TR>    
	<%
        }
	%>
    </TBODY>
</TABLE>
<br>
<%if (right == 3 && !isArchive){%>
<P><INPUT type="button" class="BUTTON" name="btnAddProjectHis" value="<%=languageChoose.getMessage("fi.jsp.postMortemReport.AddNew")%>" onclick="addProjectHis();"></P>
<%}%>
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
    <CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Deliverables")%> </CAPTION>
    <TR class="ColumnLabel">
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Deliverable")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.DeliveryLocation")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.CommittedDateOfDelivery")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ActualDateOfDelivery")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Status")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ScheduleDeviaton")%> </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Note")%> </TD>
	</TR>
	<%
		for (int i = 0; i < deliverable.size(); i++) {
	 		style = !style;
			className = (style) ? "CellBGRnews" : "CellBGR3";
			ModuleInfo info = (ModuleInfo)deliverable.elementAt(i);
	%>
	<TR class="<%=className%>">
		<TD><%=info.name%></TD>
		<TD><%=((info.deliveryLocation == null) ? "N/A" : info.deliveryLocation)%></TD>
		<TD><%=(info.rePlannedReleaseDate== null) ? CommonTools.dateFormat(info.plannedReleaseDate) : CommonTools.dateFormat(info.rePlannedReleaseDate)%></TD>
		<TD><%=((info.actualReleaseDate == null) ? "N/A" : CommonTools.dateFormat(new java.util.Date(info.actualReleaseDate.getTime())))%></TD>
		<TD>
	<%
			switch (info.status) {
				case 1:%> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Pending")%> <%break;
				case 2:%> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Accepted")%> <%break;
				case 3:%> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Rejected")%> <%break;
				case 4:%> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Cancelled")%> <%break;
				case 6:%> <%=languageChoose.getMessage("fi.jsp.postMortemReport.InProgress")%> <%break;
				case 5:%> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Others")%> <%break;
			}
	%>
		</TD>
		<TD><%=CommonTools.formatDouble(info.deviation)%></TD>
		<TD><%=ConvertString.toHtml(info.note)%></TD>
	</TR>
	<%
		}
	%>
</TBODY>
</TABLE>
<p></p>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"><A name="StandardMetrics"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.StandardObjectives")%> </A> </CAPTION>
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
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ProjectSchedule")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">           
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Stage")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.IsOnTime")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.PlannedDuration")%> </TD>                       
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ActualDuration")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.DurationDeviation")%> </TD>
        </TR>
        <%
        	for (int i = 0; i < stage.size(); i++) {
        		StageInfo stageInfo = (StageInfo)stage.get(i);
        		style = !style;
 				className = (style) ? "CellBGRnews" : "CellBGR3";
				if (stageInfo.isOntime.equalsIgnoreCase("Yes")) {
					stageInfo.isOntime = Color.setColor(Color.GOODMETRIC,languageChoose.getMessage(stageInfo.isOntime));
				}
				else if (stageInfo.isOntime.equalsIgnoreCase("No")) {
  				     stageInfo.isOntime = Color.setColor(Color.BADMETRIC,languageChoose.getMessage(stageInfo.isOntime));
				}
        %>
        <TR class="<%=className%>">            
            <TD><%=ConvertString.toHtml(stageInfo.stage)%></TD>
            <TD><%=stageInfo.isOntime%></TD>
            <TD><%=stageInfo.pDuration%></TD>     
            <TD><%=stageInfo.aDuration%></TD>
            <TD><%=stageInfo.duDeviation%></TD>
        </TR>
        <%
        	}
        %> 
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ProductSchedule")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ProductName")%> </TD>            
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.PlannedReleaseDate")%></TD>  
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.ActualReleaseDate")%></TD>      
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.ScheduleDeviation")%>(%)</TD>             
        </TR>
        <%
        	for (int i=0; i < module.size(); i++) {
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
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.EffortDistributionByProcesses")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">            
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Process")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Plannedpd")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Planned")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Actualpd")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Actual")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Deviation")%> </TD>
        </TR>
	<%
		double totalP = CommonTools.parseDouble(strArr[0]);
        double totalA = CommonTools.parseDouble(strArr[1]);
        double totalP2 = 0;
        double totalA2 = 0;
        for (int i = 0; i < process.size(); i++) {
        	ProcessEffortPMInfo processInfo = (ProcessEffortPMInfo)process.get(i);
        	style = !style;
 			className = (style) ? "CellBGRnews" : "CellBGR3";
  			if ((totalP > 0) && (!Double.isNaN(processInfo.plan1))) {
  				processInfo.plan2 = processInfo.plan1*100d / totalP;
  				totalP2 += processInfo.plan2;
			}
  			if ((totalA > 0) && (!Double.isNaN(processInfo.actual1))) {
  				processInfo.actual2 = processInfo.actual1*100d / totalA;
  				totalA2 += processInfo.actual2;
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
	<%
		}
  		String ttd = null;
		if (totalP > 0 && totalA >= 0) {
  			ttd = CommonTools.formatDouble((totalA - totalP) * 100d / totalP);
  		}
  		else {
  			ttd="N/A";
  		}
	%>    
        <TR class="TableLeft">
            <TD><B><%=languageChoose.getMessage("fi.jsp.postMortemReport.Total")%></B></TD>
            <TD><B><%=strArr[0]%></B></TD>
            <TD><B>100</B></TD>
            <TD><B><%=strArr[1]%></B></TD>
            <TD><B>100</B></TD>
            <TD><B><%=ttd%></B></TD>
        </TR> 
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.EffortDistributionByType")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">            
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Type")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Effortpd")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Effort")%> </TD>            
        </TR>
	<%
    	EffortDistributionByTypeInfo effortDistributionByType = (EffortDistributionByTypeInfo)effortDistribution.get(effortDistribution.size() - 1);
        float total = -1;
        totalP = -1;
        boolean flag = false;
        if (ConvertString.isNumber(effortDistributionByType.effortPD)) {
        	total=Float.parseFloat(effortDistributionByType.effortPD);
		}
        float tempFloat = 0;
        for (int i = 0; i < effortDistribution.size() - 1; i++) {
			effortDistributionByType = (EffortDistributionByTypeInfo)effortDistribution.get(i);
			tempFloat = 0;
			style = !style;
			className = (style) ? "CellBGRnews" : "CellBGR3";
			if (total > 0) {
				if (ConvertString.isNumber(effortDistributionByType.effortPD)) {
  					tempFloat = 100 * Float.parseFloat(effortDistributionByType.effortPD) / total;
  					effortDistributionByType.effortPC = CommonTools.formatDouble(tempFloat);
				}
				totalP += tempFloat;
				flag = true;
			}
	%>
        <TR class=<%=className%>>           
            <TD><%=ConvertString.toHtml(languageChoose.getMessage(effortDistributionByType.type))%></TD>
            <TD><%=effortDistributionByType.effortPD%></TD>
            <TD><%=effortDistributionByType.effortPC%></TD>             
        </TR>        
	<%
        	effortDistributionByType = (EffortDistributionByTypeInfo)effortDistribution.get(i+1);
		}
		if (flag) {
			effortDistributionByType.effortPC = CommonTools.formatDouble(totalP);
		}
		style = !style;
		className = (style) ? "CellBGRnews" : "CellBGR3";
	%>
        <TR class="TableLeft">
            <TD><B><%=ConvertString.toHtml(effortDistributionByType.type)%></B></TD>
            <TD><B><%=effortDistributionByType.effortPD%></B></TD>
            <TD><B>100</B></TD>
        </TR>           
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.WeightedDefectDistributionByQC")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.DetectionOn")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.InjectionOnRequirement")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.InjectionOnDesign")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.InjectionOnCoding")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.InjectionOnOther")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.WeightedDefects")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.oftotal")%> </TD>
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
            <TD><%=languageChoose.getMessage(totalWeightedD.detection)%></TD>
            <TD><%=totalWeightedD.injRequirement%></TD>
            <TD><%=totalWeightedD.injDesign%></TD>
            <TD><%=totalWeightedD.injCoding%></TD>
            <TD><%=totalWeightedD.injOther%></TD>
            <TD><%=totalWeightedD.weightedDefect%></TD>
            <TD> 100 </TD>
    </TR>
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.DefectDistributionBySeverity")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Severity")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.NumberOfDefects")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.oftotal")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.NumberOfDefectsByReview")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.oftotalByReview")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.NumberOfDefectsByTest")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.oftotalByTest")%> </TD>
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
            <TD><B> 100 </B></TD>
            <TD><B><%=totalSeverityD.nDreview%></B></TD>
            <TD><B> 100 </B></TD>
            <TD><B><%=totalSeverityD.nDtest%></B></TD>
            <TD><B> 100 </B></TD>
        </TR>
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.DefectDistributionByClassifica")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ClassificationType")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Fatal")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Serious")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Medium")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Cosmetic")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.WeightedDefect")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.oftotal")%> </TD>
        </TR>
        <%
        	int size = typeDefectArr.length - 1;
        	int iTemp = 0;
        	total = typeDefectArr[size].weighted;
        	String strPC = "";
        	allTotal = 0;
        	for (int i = 0; i < size; i++) {
				if (typeDefectArr[i].type != null) {  	
					iTemp = i;
	        		style = !style;
	 				className = (style) ? "CellBGRnews" : "CellBGR3";
		        	strPC = "";
		        	if (total != 0) {
		        		tempFloat = typeDefectArr[iTemp].weighted * 100 / total;
		        		strPC = CommonTools.formatDouble(tempFloat);
		        		allTotal += tempFloat;
		        	}
			        %>
			        <TR class="<%=className%>">
			            <TD><%=languageChoose.getMessage(typeDefectArr[iTemp].type)%></TD>
			            <TD><%=typeDefectArr[iTemp].fatal%></TD>
			            <TD><%=typeDefectArr[iTemp].serious%></TD>
			            <TD><%=typeDefectArr[iTemp].medium%></TD>
			            <TD><%=typeDefectArr[iTemp].cosmetic%></TD>
			            <TD><%=typeDefectArr[iTemp].weighted%></TD>
			            <TD><%=strPC%></TD>
			        </TR>
			        <%
        		}
        	}
        	strPC = CommonTools.formatDouble(allTotal);
        	style = !style;
 			className = (style) ? "CellBGRnews" : "CellBGR3";
        %>
		<TR class="TableLeft">
            <TD><B><%=typeDefectArr[size].type%></B></TD>
            <TD><B><%=typeDefectArr[size].fatal%></B></TD>
            <TD><B><%=typeDefectArr[size].serious%></B></TD>
            <TD><B><%=typeDefectArr[size].medium%></B></TD>
            <TD><B><%=typeDefectArr[size].cosmetic%></B></TD>
            <TD><B><%=typeDefectArr[size].weighted%></B></TD>
            <TD><B>100</B></TD>       
        </TR>
    </TBODY>
</TABLE>
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ConformityWithProcess")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.FoundIn")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.NumberOfNCs")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.NumberOfObs")%> </TD>
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
        		}else
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
</FORM>

<FORM method="POST" name="frmDPTask">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><A name="defectprevention"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.DefectPreventionGoals")%> </A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD rowspan="2" width="24" align="center">#</TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Item")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Unit")%> </TD>
            <TD rowspan="1" colspan="3" align="center"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Planned")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Actual")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Deviation")%></TD>
            <TD rowspan="2" width="42%">Cause</TD>
        </TR>
        <TR class="ColumnLabel">
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%>&nbsp;&nbsp;</TD>
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>&nbsp;&nbsp;</TD>
	    	<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>&nbsp;&nbsp;</TD>
        </TR>
        <%
        	boolean bl = true;
        	String rowStyle = "";
        	
        	for (int i = 0; i < defectPrevention.size(); i++) {
				rowStyle = bl ? "CellBGRnews" : "CellBGR3";
  				bl = !bl;
  				
        		DPTaskInfo dpTaskInfo = (DPTaskInfo)defectPrevention.get(i);
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A href="dpTaskUpdate.jsp?vtID=<%=i%>"><%=dpTaskInfo.item%></A></TD>
            <TD><%=dpTaskInfo.unit%></TD>
            <TD><%=CommonTools.formatDouble(dpTaskInfo.usl)%></TD>
            <TD><%=CommonTools.formatDouble(dpTaskInfo.planValue)%></TD>
            <TD><%=CommonTools.formatDouble(dpTaskInfo.lsl)%></TD>
            <TD><%=CommonTools.formatDouble(dpTaskInfo.actualValue)%></TD>
		<%  
				String s = CommonTools.formatDouble(dpTaskInfo.deviationValue);
				if (dpTaskInfo.deviationValue > 20) {
		%>
			<TD><%=Color.setColor(Color.BADMETRIC, s)%></TD>
		<%
				}
				else if (dpTaskInfo.deviationValue<0) {
		%>
			<TD><%=Color.setColor(Color.GOODMETRIC, s)%></TD>
		<%
				}
				else {
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
<br>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><A name="#darplan">DAR</A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="3%" align="center">#</TD>
            <TD width="23%">Item</TD>
            <TD width="5%">Doer</TD>
            <TD NOWRAP>Target date</TD>  
            <TD NOWRAP>Actual date</TD>
            <TD NOWRAP>On-time</TD>                                
            <TD width="42%">Cause</TD>
        </TR>
        <%
        	bl = true;
        	rowStyle = "";        	
        	for (int i = 0; i < dar.size(); i++) {
       			rowStyle = (bl) ? "CellBGRnews" : "CellBGR3";
  				bl = !bl;
  				DARPlanInfo darPlanInfo = (DARPlanInfo)dar.get(i);
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A href="darPlanUpdate.jsp?vtID=<%=i%>"><%=darPlanInfo.darItem%></A></TD>
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
            <TD rowspan="1" colspan="3" align="center"> <%=languageChoose.getMessage("fi.jsp.norm.Norm")%> </TD>
            <TD rowspan="1" colspan="3" align="center"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.TargetedValue")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ActualValue")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.DeviationFromNorm")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.DeviationFromTarget")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Cause")%> </TD>
        </TR>

	    <TR class="ColumnLabel">
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LCL")%>&nbsp;&nbsp;</TD>
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>&nbsp;&nbsp;</TD>
	    	<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.UCL")%>&nbsp;&nbsp;</TD>
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
	            <TD><%=normInfo.lcl%></TD>
	            <TD><%=CommonTools.formatDouble(normInfo.average)%></TD>
	            <TD><%=normInfo.ucl%></TD>	 
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
		final int HIGHGOOD=1;
		String customMetricDeviationFromTarget = "";
		double customMetricDeviation = 0;
		for (int i = 0; i < WOCustomeMetrics.size(); i++) {
 			className = (style) ? "CellBGRnews" : "CellBGR3";
 			WOCustomeMetricInfo info = (WOCustomeMetricInfo)WOCustomeMetrics.elementAt(i);
 			if((CommonTools.formatDouble(info.actualValue) != "N/A" && CommonTools.formatDouble(info.LCL) != "N/A" && CommonTools.formatDouble(info.UCL) != "N/A" && (info.actualValue < info.LCL || info.actualValue > info.UCL)) || (CommonTools.formatDouble(info.actualValue) != "N/A" && CommonTools.formatDouble(info.plannedValue) != "N/A" && info.actualValue != info.plannedValue)){
			customMetricDeviation = 0;
			
			if(!CommonTools.formatDouble(info.plannedValue).equals("N/A")){
				customMetricDeviation = (info.actualValue - info.plannedValue) * 100 / info.plannedValue;
			}
			//if(!CommonTools.formatDouble(info.UCL).equals("N/A") && !CommonTools.formatDouble(info.LCL).equals("N/A")){
			//	if(customMetricDeviation > 0){
			//		customMetricDeviationFromTarget = Color.colorByNorm(CommonTools.formatDouble(customMetricDeviation), info.actualValue, info.LCL, info.UCL, Color.GOODMETRIC);
			//	}else{
			//		customMetricDeviationFromTarget = Color.colorByNorm(CommonTools.formatDouble(customMetricDeviation), info.actualValue, info.LCL, info.UCL, Color.BADMETRIC);
			//	}
			//}
			
				l++;
				style = !style;
				className = (style) ? "CellBGRnews" : "CellBGR3";
			%>
				<TR class="<%=className%>">
					<TD align="center">14.<%=l%></TD>
					<TD><%=ConvertString.toHtml(info.name)%></TD>
					<TD><%=ConvertString.toHtml(info.unit)%></TD>
					<TD></TD>
					<TD></TD>
					<TD></TD>
					<TD></TD>
					<TD><%=CommonTools.formatDouble(info.plannedValue)%></TD>
					<TD></TD>
					<TD><%=CommonTools.formatDouble(info.actualValue)%></TD>
					<TD></TD>
					<TD><%=CommonTools.formatDouble(customMetricDeviation)%></TD>
					<TD><%=ConvertString.toHtml(info.causal)%></TD>
				</TR>
			<%
				}
			}
	%>
</TABLE>
<P>
<%if(right==3&&(norm.size()>0)&&j>0&&!isArchive){%>
<INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.postMortemReport.Update") %>" class="BUTTON" onclick="jumpURL('pmCauseUpdate.jsp');">
<INPUT type="button" value=" <%=languageChoose.getMessage("fi.jsp.postMortemReport.CopyToLessonPractice")%> " class="BUTTONWIDTH" onclick="doIt(<%=Constants.EXPORT_PM_CAUSE%>);" <%=((disabled)?"disabled":"")%>>
<%}%>
</P>
<!-- End Causal Analysis Table -->
<TABLE class="Table" width="95%" cellspacing="1">
    <COL span="1" width="24">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.RisksInProject")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
        	<TD rowspan="2">#</TD>
            <TD rowspan="2" width="20%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ActualRiskScenario")%> </TD>
            <TD rowspan="2" width="20%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ActualActions")%> </TD>
            <TD colspan="3" align="center" width="35%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ActualImpact")%> </TD>
            <TD rowspan="2" width="10%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Unplanned")%> </TD>
            <TD rowspan="2" width="10%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.AssessmentDate")%> </TD>
        </TR>
        <TR class="ColumnLabel">
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Impactto")%> </TD>
            <TD width="5%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Unit")%> </TD>
            <TD width="10%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ActualImpact")%> </TD>
        </TR>
	<%
        int rowNum = 0;
        if (risk != null) {
       		for (int i = 0; i < risk.size(); i++) {
	        	RiskInfo riskInfo = (RiskInfo)risk.elementAt(i);
	        	int rowspan = 0;
	        	String htmlRows = "";
	        	String actImp = "";
	        	if (riskInfo.status == 2) {
					style = !style;
	 				className = (style) ? "CellBGRnews" : "CellBGR3";
		        	for (j = 0; j < 3; j++) {
		        		if (!riskInfo.imp[j].equals("")) {
		        			rowspan++;
			        		if (rowspan == 1) {
			        			actImp = "<TD>" + languageChoose.getMessage(riskInfo.imp[j]) + "</TD><TD>" + riskInfo.unt[j] + "</TD><TD>" + riskInfo.est[j] + "</TD>";
							}
			        		else {
			        			htmlRows += "<TR class=\"" + className + "\"><TD>" + riskInfo.imp[j] + "</TD><TD>" + riskInfo.unt[j] + "</TD><TD>" + riskInfo.est[j] + "</TD></TR>";
							}
		        		}       	
		        	}
		        	String strRowSpan = "";
		        	if (rowspan != 1) {
		        		strRowSpan = "rowspan =\"" + rowspan + "\"";
					}
	%>
        <TR class="<%=className%>">
        	<TD <%=strRowSpan%>><%=++rowNum%></TD>
            <TD <%=strRowSpan%>><%=riskInfo.actualRiskScenario != null ? riskInfo.actualRiskScenario : "N/A"%></TD>
            <TD <%=strRowSpan%>><%=riskInfo.actualAction != null ? riskInfo.actualAction : "N/A" %></TD>
			<%=actImp%>
            <TD <%=strRowSpan%> ><%=riskInfo.unplanned == 1 ? languageChoose.getMessage("fi.jsp.postMortemReport.Yes") : languageChoose.getMessage("fi.jsp.postMortemReport.No")%></TD>
            <TD <%=strRowSpan%>><%=CommonTools.dateFormat(riskInfo.assessmentDate)%></TD>
        </TR>
        <%
        			if (rowspan!=1){
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
<p></p>
<TABLE class="Table" width="95%" cellspacing="1">
<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ToolsUsed")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">            
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Item")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Purpose")%></TD>            
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Description")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.postMortemReport.Status")%></TD>
            <TD width="11%"><%=languageChoose.getMessage("fi.jsp.postMortemReport.DueDate")%></TD>                        
        </TR>
    <%
		String pur,des,dueD;
		ToolInfo toolInfo = null;
		for (int i = 0; i < tool.size(); i++) {
        	style = !style;
 			className = (style) ? "CellBGRnews" : "CellBGR3";
  			pur = "N/A";
  			des = "N/A";
  			
        	toolInfo = (ToolInfo)tool.get(i);
        	
        	if (toolInfo.purpose != null) {
        		pur = toolInfo.purpose;        		
        		if (pur.length() > 50) {
        			pur = pur.substring(0, 50) + "...";
        		}
        	}
        	if (toolInfo.description != null) {
        		des = toolInfo.description;
        		if (des.length() > 50) {
        			des = des.substring(0, 50) + "...";
        		}
        	}
        	
        	if (toolInfo.dueD == null) {
        		dueD="N/A";
        	}
        	else {
        		dueD = CommonTools.dateFormat(toolInfo.dueD);
        	}
	%>
        <TR class="<%=className%>">            
            <TD><%=ConvertString.toHtml(toolInfo.name)%></TD>
            <TD><%=ConvertString.toHtml(pur)%></TD>           
            <TD><%=ConvertString.toHtml(des)%></TD>
            <TD><%=ConvertString.toHtml(toolInfo.status)%></TD>
            <TD><%=dueD%></TD>
        </TR>
	<%
		}
	%>        
    </TBODY>
</TABLE>
<p></p>

<TABLE cellspacing="1" class="Table" width="95%" id="tableTeam">
<CAPTION class="TableCaption"><A name="teamvaluation"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.TeamValuation")%> </A></CAPTION>
<TBODY>
<TR>
<TD class="ColumnLabel" width = "24" align = "center">#</TD>
<TD class="ColumnLabel" width = "150"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Name")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Account")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Role")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Process")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.SkillTechnology")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Point")%> </TD>
<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Comment")%> </TD>
</TR>
<%
int countRow = 1;
boolean isMove = false;
long tempAssignmentId = 0;
int ii = 0;
for (int i = 0; i < skillSet.size(); i++) {
	className = "";
 	if (i%2==0) className="CellBGRnews";
 	else className = "CellBGR3";
 	
 	SkillInfo info = (SkillInfo) skillSet.elementAt(i);
 	
 	int k;
 	for (k = 0; k < skillSet2.size(); k++) {
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
<TD rowspan=<%=countRow%>><A href="Fms1Servlet?reqType=<%=Constants.PROJECT_SKILL_DETAIL%>&assignmentID=<%=info.assignmentId%>&fullName=<%=info.fullName%>"><%=info.fullName%></A></TD>
<TD rowspan=<%=countRow%>><%=((info.account == null)?"N/A" : ConvertString.toHtml(info.account))%></TD>
<TD><%=((info.projectRole == null)?"N/A" : languageChoose.getMessage(info.projectRole))%></TD>
<TD><%=((info.process == null)? "N/A" : info.process)%></TD>
<TD><%=((info.process == null)? "N/A" : info.skill)%></TD>
<TD><%=((Integer.toString(info.point) == null)? "N/A" : Integer.toString(info.point))%></TD>
<TD><%=((info.skillComment == null)? "N/A": info.skillComment)%></TD>
<%
		}
		else {
%>
<TD><%=((info.projectRole == null)? "N/A" : info.projectRole)%></TD>
<TD><%=((info.process == null)? "N/A" : info.process)%></TD>
<TD><%=((info.skill == null)? "N/A" : info.skill)%></TD>
<TD><%=((Integer.toString(info.point) == null)? "N/A" : Integer.toString(info.point))%></TD>
<TD><%=((info.skillComment == null)? "N/A" : info.skillComment)%></TD>
<%
		}
%>
</TR>
<%	 
	}
	else {
		ii = ii + 1;
%>
<TR class="<%=className%>">
<TD width="24" align="center"><%=ii%></TD>
<TD><A href="Fms1Servlet?reqType=<%=Constants.PROJECT_SKILL_DETAIL%>&assignmentID=<%=info.assignmentId%>&fullName=<%=info.fullName%>"><%=ConvertString.toHtml(info.fullName)%></A></TD>
<TD><%=((info.account == null)? "N/A" : ConvertString.toHtml(info.account))%></TD>
<TD><%=((info.projectRole == null)? "N/A" : languageChoose.getMessage(info.projectRole))%></TD>
<TD><%=((info.process == null)? "N/A" : info.process)%></TD>
<TD><%=((info.skill == null)? "N/A" : info.skill)%></TD>
<TD><%=((Integer.toString(info.point) == null)? "N/A": Integer.toString(info.point))%></TD>
<TD><%=((info.skillComment == null)?"N/A" : info.skillComment)%></TD>
</TR>
<%
	}
}
%>
</TBODY>
</TABLE>
<BR>

<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION class="TableCaption"><A name="furtherwork"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.FurtherWorkList")%> </A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Item")%> </TD>
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ResultToBeDone")%> </TD>                       
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.TimeToDoDay")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Responsibility")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Note")%> </TD>            
        </TR>
        <%    
        	String result;        	
        	String note;
        	String res;
        	String time;
       		
        	for (int i = 0; i < furtherWork.size(); i++) {
        		FurtherWorkInfo wfInfor = (FurtherWorkInfo)furtherWork.get(i);
        		
        		style = !style;
 				className = (style) ? "CellBGRnews" : "CellBGR3";
  				
  				time = "N/A";
  				result = wfInfor.result;
  				note = wfInfor.note;
  				res = wfInfor.responsibility;
  				
  				if (result.length() > 50) {
  					result = result.substring(0, 50) + "...";
				}
  				if (note.length() > 50) {
  					note = note.substring(0, 50) + "...";
				}
  				if (res.length() > 50) {
  					res = res.substring(0, 50) + "...";
				}
        		if (wfInfor.time != -1) {
        			time = CommonTools.formatDouble(wfInfor.time);
        		}
        %>
        <TR class=<%=className%>>
            <TD align="center"><A href="furtherWorkDetails.jsp?vtID=<%=i%>"><%=i+1%></A></TD>
            <TD><%=ConvertString.toHtml(wfInfor.name)%></TD>
            <TD><%=ConvertString.toHtml(result)%></TD>
            <TD><%=time%></TD>            
            <TD><%=ConvertString.toHtml(res)%></TD>
            <TD><%=(note.equals("N/A")) ? "" : ConvertString.toHtml(note)%></TD>
        </TR>
        <%
        	}
        %>        
    </TBODY>
</TABLE>
<%if(right == 3 && !isArchive){%>
<P><INPUT type="button" class="BUTTON" name="btnAddFurtherWork" value="<%=languageChoose.getMessage("fi.jsp.postMortemReport.AddNew")%>" onclick="addFurtherWork();"></P>
<%}%>
    <%}%> 
</FORM>
<SCRIPT language="javascript">
	function addFurtherWork(){
		frm.action="furtherWorkAdd.jsp";
		frm.submit();
	}	
	function addDPTask(){
		frm.action="dpTaskAdd.jsp";
		frm.submit();
	}
	function addProjectHis(){
		frm.action="prjHisAdd.jsp";
		frm.submit();
	}
	function updateHeader(){	
	  	if (mandatoryFld(frm.txtReviewer,"<%= languageChoose.getMessage("fi.jsp.postMortemReport.Reviewers")%>"))
	  		if (maxLength(frm.txtReviewer,"<%=languageChoose.getMessage("fi.jsp.postMortemReport.Reviewers")%>",200))
	  			if (mandatoryFld(frm.txtApprover,"<%= languageChoose.getMessage("fi.jsp.postMortemReport.Approvers")%>"))
		  			if (maxLength(frm.txtApprover,"<%= languageChoose.getMessage("fi.jsp.postMortemReport.Approvers")%>",200)){
			  			frm.action="Fms1Servlet?reqType=<%=Constants.UPDATE_PM_HEADER%>";
			  			frm.submit();
			  		}
	} 	
</SCRIPT>
</BODY>
</HTML>