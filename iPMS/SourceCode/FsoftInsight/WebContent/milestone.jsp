<%@ page contentType="text/html;charset=UTF-8" import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*" errorPage="error.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD> 
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>milestone.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
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
	int right=Security.securiPage("Project reports",request,response);  
	
	if (request.getParameter("noStage") != null) {
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.milestone.Milestonereport")%> </P>
<P>
<P class="ERROR"> <%=languageChoose.getMessage("fi.jsp.milestone.Nomilestonesdefinedorcompleted")%> </P>
<%	
	} else {
		String className;
		StageInfo stageInfo = (StageInfo)session.getAttribute("stageInfo");
		ProjectInfo projectInfo = (ProjectInfo)session.getAttribute("projectInfo");
		Vector milestoneEffort = (Vector)session.getAttribute("milestoneEffort");
		Vector milestoneEffort2 = (Vector)session.getAttribute("milestoneEffort2");
		Vector milestoneQA = (Vector)session.getAttribute("milestoneQA");
		Vector milestoneQO = (Vector)session.getAttribute("milestoneQO");
		Vector milestoneSchedule = (Vector)session.getAttribute("milestoneSchedule");
		Vector milestoneRisk =  (Vector)session.getAttribute("milestoneRisk");
		Vector milestoneIssue = (Vector)session.getAttribute("milestoneIssue");
		Vector metricInfo = (Vector)session.getAttribute("metricInfo");
		Vector stageList = (Vector)session.getAttribute("stageList");
		Vector defectPrevention = (Vector)session.getAttribute("defectPrevention");
		Vector dar = (Vector)session.getAttribute("dar");
		Vector cusMetric = (Vector)session.getAttribute("cusMetric");
		Vector cusMetricList = (Vector)session.getAttribute("cusMetricList");
		Vector dpVt=(Vector)session.getAttribute("defectPrevention");
		String comments = stageInfo.comments;
		
		if ((comments == null) || (comments.trim().equals(""))) {
			comments = (right!=3) ? "N/A" : "";
		}
		
		boolean style=false;
	
		ProjectPointInfo projectPoint = (ProjectPointInfo) session.getAttribute("projectPoint");
		String acceptanceRate = (String) session.getAttribute("acceptanceRate");
		String rightGroupID = (String) session.getAttribute("rightGroupID");
		
		Vector allClosedTasks = (Vector)session.getAttribute("allClosedTasks");
		Vector allOpenTasks = (Vector)session.getAttribute("allOpenTasks");
		
		int allTask = 0;
		int allCloseTask = 0;
		
		if (allClosedTasks != null) {
			allTask = allClosedTasks.size();
			allCloseTask = allClosedTasks.size();
		}
		
		if (allOpenTasks != null) {
			allTask = allTask + allOpenTasks.size();
		}
%>

<TABLE width="95%">
	<TR>
		<TD><P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.milestone.Milestonereport1")%> </P></TD>
		<TD align="right" valign="bottom"><A href="Fms1Servlet?reqType=<%=Constants.MILESTONE_GET_PAGE%>&export=1" target="about:blank"> <%=languageChoose.getMessage("fi.jsp.milestone.ExportMilestoneReport")%> </A></TD>
	</TR>
</TABLE>
<TABLE width="95%" class="HDR">
	<TBODY>
		<TR>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.milestone.Customer")%> </TD>
			<TD><%=CommonTools.formatString(projectInfo.getCustomer())%></TD>
		</TR>
		<TR>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.milestone.Planstagestartdate")%> </TD>
			<TD><%=CommonTools.dateFormat(stageInfo.plannedBeginDate)%></TD>
		</TR>
		<TR>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.milestone.Planstageenddate")%> </TD>
			<TD><%=CommonTools.dateFormat(stageInfo.plannedEndDate)%></TD>
		</TR>
		
		<TR>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.milestone.Actualstartdate")%> </TD>
			<TD><%=CommonTools.dateFormat(stageInfo.actualBeginDate)%></TD>
		</TR>
		<TR>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.milestone.Actualenddate")%> </TD>
			<TD><%=CommonTools.dateFormat(stageInfo.aEndD)%></TD>
		</TR>
		<TR>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.milestone.Milestone")%> </TD>
			<TD>
			<FORM name="frm_milestoneHdr" action="Fms1Servlet" method="get">
				<INPUT type="hidden" name="reqType"	value="<%=Constants.MILESTONE_GET_PAGE%>">
				<SELECT name="stageID" class="COMBO" onchange="frm_milestoneHdr.submit();">
				<%
		            //most recent stage first
		            for (int i = stageList.size()-1; i >= 0; i--) {
		            	StageInfo stageInfoTemp = (StageInfo) stageList.elementAt(i);
		            	if ((stageInfoTemp.aEndD != null) && (stageInfoTemp.actualBeginDate != null)) {
		        %>
                <OPTION value="<%=stageInfoTemp.milestoneID%>"<%=((stageInfoTemp.milestoneID==stageInfo.milestoneID)? " selected":"")%>><%=stageInfoTemp.stage%></OPTION>
				<% 		}
		            }
		        %>
				</SELECT>
			</FORM>
			</TD>
		</TR>
	</TBODY>
</TABLE>

<br>

<FORM name="frmComment" action="Fms1Servlet?reqType=<%=Constants.UPDATE_MR_COMMENTS%>"	method="POST">
	<INPUT type="hidden" name="milestoneID"	value="<%=stageInfo.milestoneID%>">
	<P class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestone.Comments")%> </P>
	<% 
		if (right == 3) {
	%>
 	<TEXTAREA rows="6" cols="70" name="txtComments"><%=comments%></TEXTAREA><P> 
	<% 		
			if (!isArchive) { 
	%> 
	<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.milestone.Update")%>" onclick="javascript:on_Submit1();" class="BUTTON"></P> 
	<% 		} 

 		} else { 
 	%> 
		<%=ConvertString.toHtml(comments)%> 
	<% 	
		} 
	%>
</FORM>

<br>

<P> <%=languageChoose.getMessage("fi.jsp.milestone.NotesifanyvaluebelowisNAornote")%> </P>

<FORM name="frmPoint" action="Fms1Servlet?reqType=<%=Constants.UPDATE_PROJECT_POINT%>" method="POST">
<INPUT type="hidden" name="milestoneID"	value="<%=stageInfo.milestoneID%>">
<INPUT type="hidden" name="reportDate" value="<%=CommonTools.dateFormat(stageInfo.aEndD)%>">
<TABLE class="Table" cellspacing="1" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestone.Projectpoint")%> </CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="200"><%=languageChoose.getMessage("fi.jsp.milestone.MetricName")%></TD>
			<TD width="100"><%=languageChoose.getMessage("fi.jsp.milestone.Value")%></TD>
			<TD width="100"><%=languageChoose.getMessage("fi.jsp.milestone.Point")%></TD>
		</TR>
		<TR class="ColumnLabel">
			<TD width="200" colspan="3"> <%=languageChoose.getMessage("fi.jsp.milestone.StandardMetric")%> </TD>
		</TR>
		<TR class="CellBGRNews">
			<TD colspan="3"><B><%=languageChoose.getMessage("fi.jsp.milestone.Quality")%></B></TD>
		</TR>
		<%
		ReportMetricInfo reportMetricInfo;
		NormInfo normInfo;
		reportMetricInfo = (ReportMetricInfo)metricInfo.elementAt(17);
		%>
		<% normInfo = (NormInfo)milestoneQO.elementAt(0); %>
		<INPUT type="hidden" name="CS" value="<%=CommonTools.formatDouble(normInfo.actualValue)%>">
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.CustomerSatisfactionPoint")%></TD>
			<TD><%=CommonTools.formatDouble(normInfo.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.CusSatisfaction)%></TD>
		</TR>
		<% normInfo = (NormInfo)milestoneQO.elementAt(1); %>
		<INPUT type="hidden" name="LK" value="<%=CommonTools.formatDouble(normInfo.actualValue)%>">
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.LeakageWdefectUCP")%></TD>
			<TD><%=CommonTools.formatDouble(normInfo.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.Leakage)%></TD>
		</TR>
		<% normInfo =(NormInfo)milestoneQO.elementAt(2); %>
		<INPUT type="hidden" name="NC" value="<%=CommonTools.formatDouble(normInfo.actualValue)%>">
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.ProcessCompliance")%></TD>
			<TD><%=CommonTools.formatDouble(normInfo.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.ProjectNC)%></TD>
		</TR>
		
		<TR class="CellBGRNews">
			<TD colspan="3"><B><%=languageChoose.getMessage("fi.jsp.milestone.Cost")%></B></TD>
		</TR>
		<%
		double dbPS = 0;
		dbPS = ((reportMetricInfo.spent != Double.NaN) ? reportMetricInfo.spent:reportMetricInfo.estimated);
		%>
		<INPUT type="hidden" name="PS" value="<%=CommonTools.formatDouble(dbPS)%>">
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.ProjectSizeUCP")%></TD>
			<TD><%=CommonTools.formatDouble(dbPS)%></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.ProjectSize)%></TD>
		</TR>
		<% normInfo =(NormInfo)milestoneQO.elementAt(3); %>
		<INPUT type="hidden" name="EE" value="<%=CommonTools.formatDouble(normInfo.actualValue)%>">
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.EffortEfficiency")%></TD>
			<TD><%=CommonTools.formatDouble(normInfo.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.EffortEfficiency)%></TD>
		</TR>
		<% normInfo = (NormInfo)milestoneQO.elementAt(4); %>
		<INPUT type="hidden" name="CC" value="<%=CommonTools.formatDouble(normInfo.actualValue)%>">
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.CorrectionCost")%></TD>
			<TD><%=CommonTools.formatDouble(normInfo.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.CorrectionCost)%></TD>
		</TR>
		
		<TR class="CellBGRNews">
			<TD colspan="3"><B><%=languageChoose.getMessage("fi.jsp.milestone.Delivery")%></B></TD>
		</TR>
		<% normInfo = (NormInfo)milestoneQO.elementAt(5); %>
		<INPUT type="hidden" name="TL" value="<%=CommonTools.formatDouble(normInfo.actualValue)%>">
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.Timeliness")%></TD>
			<TD><%=CommonTools.formatDouble(normInfo.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.Timeliness)%></TD>
		</TR>
		<%
			normInfo = (NormInfo)milestoneQO.elementAt(6);
			double dbRC = 0;
			dbRC = ((normInfo.actualValue == Double.NaN) ? 0 : normInfo.actualValue);
		%>
		<INPUT type="hidden" name="RC" value="<%=CommonTools.formatDouble(dbRC)%>">
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.RequirementCompleteness")%></TD>
			<TD><%=CommonTools.formatDouble(dbRC)%></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.ReqCompleteness)%></TD>
		</TR>
				
				
				
		<TR class="CellBGRNews">
			<TD colspan="3"></TD>
		</TR>
		
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.WorkOrder")%></TD>
			<TD><INPUT size="9" type="text" maxlength="9" name="WO" value="<%=CommonTools.formatDouble(projectPoint.WOPoint)%>"></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.WOPoint)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.AcceptanceNote")%></TD>
			<TD><INPUT size="9" type="text" maxlength="9" name="AN" value="<%=CommonTools.formatDouble(projectPoint.ANPoint)%>"></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.ANPoint)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.ProjectPlan")%></TD>
			<TD><INPUT size="9" type="text" maxlength="9" name="PP" value="<%=CommonTools.formatDouble(projectPoint.PPPoint)%>"></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.PPPoint)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.ProjectReports")%></TD>
			<TD><INPUT size="9" type="text" maxlength="9" name="PR" value="<%=CommonTools.formatDouble(projectPoint.PRPoint)%>"></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.PRPoint)%></TD>
		</TR>
		<% normInfo = (NormInfo)milestoneQO.elementAt(7);
			double dbOverdueNCsObs = 0;
			dbOverdueNCsObs = ((normInfo.actualValue == Double.NaN) ? 0 : normInfo.actualValue);
			%>
		<INPUT type="hidden" name="OverdueNCsObsCount" value="<%=CommonTools.formatDouble(dbOverdueNCsObs)%>">
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.OverdueNCsObs")%></TD>
			<TD><%=CommonTools.formatDouble(dbOverdueNCsObs)%></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.OverdueNCsObsPoint)%></TD>
		</TR>
		<TR class="ColumnLabel">
			<TD colspan="3"><%=languageChoose.getMessage("fi.jsp.milestone.CustomizedMetrics")%></TD>
		</TR>
		<%
		if(cusMetric.size()==0){
		%>
			<TR class="CellBGRNews"><INPUT name="cmValue"  size="9" maxlength="9" value="N/A" type="hidden">
			</TR>
		<%
		}else if(cusMetric.size()==1){
		CusMetricInfo cusMetricInfo = new CusMetricInfo();
		cusMetricInfo = (CusMetricInfo)cusMetric.elementAt(0);
		%>
			<TR class="CellBGRNews">
				<TD><%=cusMetricInfo.name%></TD>
				<TD><%=CommonTools.formatDouble(cusMetricInfo.actualValue)%></TD>
				<TD><INPUT name="cmValue"  size="9" maxlength="9" onblur="check(cmValue)" value="<%=CommonTools.formatDouble(cusMetricInfo.point)%>"></TD>
			</TR>
		<%
		}else{		
		for(int i=0;i< cusMetric.size(); i++){
			CusMetricInfo cusMetricInfo = new CusMetricInfo();
			cusMetricInfo = (CusMetricInfo)cusMetric.elementAt(i);
		%>
		<TR class="CellBGRNews">
			<TD><%=cusMetricInfo.name%></TD>
			<TD><%=CommonTools.formatDouble(cusMetricInfo.actualValue)%></TD>
			<TD><INPUT name="cmValue"  size="9" maxlength="9" onblur="check(cmValue[<%=i%>])" value="<%=CommonTools.formatDouble(cusMetricInfo.point)%>"></TD>
		</TR>
		<%}
		}
		%>
		
		<TR class="ColumnLabel">
			<TD colspan="3"><B> <%=languageChoose.getMessage("fi.jsp.milestone.BonusPenalty")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.PrestigePoint")%></TD>
			<TD><INPUT size="9" type="text" maxlength="9" name="Prestige" value="<%=CommonTools.formatDouble(projectPoint.PrestigePoint)%>"></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.PrestigePoint)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.PenaltyPoint")%></TD>
			<TD><INPUT size="9" type="text" maxlength="9" name="CusPoint" value="<%=CommonTools.formatDouble(projectPoint.CusPoint)%>"></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.CusPoint)%></TD>
		</TR>
		<TR class="TableLeft">
			<TD><%=languageChoose.getMessage("fi.jsp.milestone.Projectevaluation")%></TD>
			<TD><%=languageChoose.getMessage(projectPoint.ProjectEval)%></TD>
			<TD><%=CommonTools.formatDouble(projectPoint.ProjectPoint)%></TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>

<br>

<%
 	if ((
		(rightGroupID.equalsIgnoreCase("PQA")) ||
		(rightGroupID.equalsIgnoreCase("ADMIN")) ||
		(rightGroupID.equalsIgnoreCase("SQA")) ||
		(rightGroupID.equalsIgnoreCase("QAG"))) &&
		(projectInfo.getArchiveStatus() != 4)) {
%>
	<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.milestone.Calculate")%>" onclick="javascript:on_Calculate();" class="BUTTON"">
<%	
	}
%>

<br><br>

<TABLE class="Table" width="95%" cellspacing="1">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestone.Metrics")%> </CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD> <%=languageChoose.getMessage("fi.jsp.milestone.Item")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.milestone.Unit")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.milestone.TotalPlanned")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.milestone.SpentCompleted")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.milestone.Remain")%> </TD>
		</TR>
	<%
	    String estimated=null;
	    String spent=null;
	    String remaining=null;
	    
		for (int i = 0; i < metricInfo.size(); i++)
			{
				if ((i != 9) && (i != 10) && (i != 11)) // Modify by HaiMM: Remove 3 metrics follow CR: Development, Quality, Management
				{
					reportMetricInfo = (ReportMetricInfo)metricInfo.elementAt(i);
					
					if (i == 1) {
			%>
				<TR class="CellBGRNews">
					<TD><%=languageChoose.getMessage("fi.jsp.milestone.Taskcompleteness")%></TD>
					<TD>%</TD>
					<TD>100</TD>
				<%
						if (allTask > 0) {
				%>
		            <TD><%=CommonTools.formatDouble((allCloseTask * 100)/allTask)%></TD>
		            <TD><%=CommonTools.formatDouble(100 - (allCloseTask * 100)/allTask)%></TD>
				<%
						} else {
				%>
		            <TD>N/A</TD>
		            <TD>N/A</TD>
				<%			
						}
					}
				%>
		        </TR>
			<%
					if (!reportMetricInfo.unit.equalsIgnoreCase("DD-MMM-YY")) {
						estimated=CommonTools.formatDouble(reportMetricInfo.estimated);
						spent=CommonTools.formatDouble(reportMetricInfo.spent);
						remaining=CommonTools.formatDouble(reportMetricInfo.remain);
					} else {
						estimated=CommonTools.dateFormat(reportMetricInfo.estimated);
						spent=CommonTools.dateFormat(reportMetricInfo.spent);
						remaining=CommonTools.dateFormat(reportMetricInfo.remain);
					}
				
					spent=Color.setColor(reportMetricInfo.color,spent);
			%>
				<TR class="CellBGRNews">
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

<br>

<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestone.Effort")%> </CAPTION>
	<TR class="ColumnLabel">
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Process")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Plannedpd")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Spentduringstagepd")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Spentuptomilestonepd")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Remainpd")%></TD>
	</TR>
	<%
		if (milestoneEffort != null) {
	        
	        float totalPlannedValue = 0;
	        float totalActual = 0;
	        float totalActual2 = 0;
	        double remain = 0;//interim variable
	        float totalRemain = 0;
	        
	        for (int i=0; i < milestoneEffort.size(); i++) { 
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
	    		} else if (!Double.isNaN(processInfo.estimated)) {
	    			plannedValue = CommonTools.formatDouble(processInfo.estimated);
	    			remain = processInfo.estimated - processInfo2.actual;
	    			remainValue = CommonTools.formatDouble(remain);
	    			totalPlannedValue += processInfo.estimated;
	    			totalRemain += remain;
	    		} else {
	    			plannedValue="N/A";
	    		    remainValue="N/A";
	    		}	
	%>		
	<TR class="CellBGRnews">
		<TD><%=languageChoose.getMessage(processInfo.process)%></TD>
		<TD><%=plannedValue%></TD>
		<TD><%=CommonTools.formatDouble(processInfo.actual)%></TD>
		<TD><%=CommonTools.formatDouble(processInfo2.actual)%></TD>
		<TD><%=remainValue%></TD>
	</TR>
		<%	
			}
		%>	
	<TR class="TableLeft">
		<TD><B><%=languageChoose.getMessage("fi.jsp.milestone.Total")%></B></TD>
		<TD><B><%=CommonTools.formatDouble(totalPlannedValue)%></B></TD>
		<TD><B><%=CommonTools.formatDouble(totalActual)%></B></TD>
		<TD><B><%=CommonTools.formatDouble(totalActual2)%></B></TD>
		<TD><B><%=CommonTools.formatDouble(totalRemain)%></B></TD>
	</TR>
	<%
		}
	%>
</TABLE>

<br><br>

<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestone.Schedule")%> </CAPTION>
	<TR class="ColumnLabel">
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Product")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Plannedreleasedate")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Actualreleasedate")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Deviation")%></TD>
	</TR>
	<%
		if (milestoneSchedule != null) {
	        for (int i = 0; i < milestoneSchedule.size(); i++) {
	     	   	style = !style;
	      	  	className = style ? "CellBGRnews" : "CellBGR3";
	        	ProcessScheduleInfo processScheduleInfo = (ProcessScheduleInfo)milestoneSchedule.elementAt(i);        
	%>
	<TR class="<%=className%>">
		<TD><%=ConvertString.toHtml(processScheduleInfo.getProduct())%></TD>
		<TD><%=CommonTools.dateFormat(processScheduleInfo.getPlannedReleaseDate())%></TD>
		<TD><%=CommonTools.dateFormat(processScheduleInfo.getActualReleaseDate())%></TD>
		<TD><%=CommonTools.formatDouble(processScheduleInfo.getDeviation())%></TD>
	</TR>
	<%
			}
		}
	%>
</TABLE>

<br><br>

<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestone.Qualityactivities")%> </CAPTION>
	<TR class="ColumnLabel">
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Activity")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Weighteddefectsplannedtobefoun")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Weighteddefectsdetectedprerele")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Plannedeffortpd")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Actualeffortpd")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Completeness")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.milestone.Timeliness1")%></TD>
	</TR>
	<%        
		double total1 = 0;
	    double total2 = 0;
	    double total3 = 0;
	    double total4 = 0;
	
		if (milestoneQA != null) {
	        for (int i = 0; i < milestoneQA.size(); i++) {			        	
	        	style = !style;
	      	 	className = style ? "CellBGRnews" : "CellBGR3";
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
	%>
	<TR class="<%=className%>">
		<TD><%=qualityActivitiesInfo.getActivity()%></TD>
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
	<TR class="TableLeft">
		<TD><B><%=languageChoose.getMessage("fi.jsp.milestone.Total")%></B></TD>
		<TD><B><%=CommonTools.formatDouble(total1)%></B></TD>
		<TD><B><%=CommonTools.formatDouble(total2)%></B></TD>
		<TD><B><%=CommonTools.formatDouble(total3)%></B></TD>
		<TD><B><%=CommonTools.formatDouble(total4)%></B></TD>
		<TD></TD>
		<TD></TD>
	</TR>
</TABLE>

<br>

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

<br>	
<!--anhtv08-start -->

<FORM name="mileStoneSpecObjective" action="Fms1Servlet?reqType=<%=Constants.MILESTONE_OBJECTIVE_UPDATE%>" method="POST">
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.SpecificObjectives")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD align = "center" rowspan="2"># </TD>		
            <TD rowspan="2" align="left" width="25%"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Name")%> </TD>
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Unit")%> </TD>
            <TD rowspan="1" colspan="3" align="center"><%=languageChoose.getMessage("fi.jsp.postMortemReport.TargetedValue")%></TD>    	
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.ActualValue")%>* </TD>
            <!--anhtv08-start
            	
            -->
            
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.postMortemReport.Deviation")%> </TD>            
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Basic")%> </TD>
			</TR>
            <TR class="ColumnLabel">
            <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>&nbsp;&nbsp;</TD>
            <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.postMortemReport.Average")%>&nbsp;&nbsp;</TD>
    		<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%>&nbsp;&nbsp;</TD>
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
			<%
				if(Double.isNaN(info.actualValue))
				{
				%>
				<TD><INPUT size="9" type="text" maxlength="9" name="actualValue"  value="" onblur="checkActualValue(this)"></TD>
				<%
				}else 
				{%>
				<TD><INPUT size="9" type="text" maxlength="9" name="actualValue" value="<%=CommonTools.formatDouble(info.actualValue)%>" onblur="checkActualValue(this)"></TD>					
				<%}
				
				%>

			<!--
			<TD><%=CommonTools.formatDouble(info.actualValue)%></TD>
			-->
			<TD><%=info.deviation%></TD>
			<TD><%=ConvertString.toHtml(info.note)%></TD>
		</TR>
	<%
		}
	%>
</TABLE>
<INPUT type="button" name="btnUpdate1" value="<%=languageChoose.getMessage("fi.jsp.milestone.Update")%>" onclick="on_Submit2()" class="BUTTON">
</FORM>
<br><br>

<FORM method="POST" name="frmDPTask" action ="Fms1Servlet?reqType=<%=Constants.MILESTONE_DEFECT_UPDATE%>">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><A name="defectprevention"> <%=languageChoose.getMessage("fi.jsp.milestone.DefectPreventionGoals")%> </A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD align = "center" rowspan="2"># </TD>		
            <TD rowspan="2" align="left" width="25%"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Name1")%> </TD>
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Unit1")%> </TD>
            <TD rowspan="1" colspan="3" align="center"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Target")%></TD>    	
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.ActualValue1")%>* </TD>
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Deviation1")%> </TD>            
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Basic")%> </TD>
		</TR>
        <TR class="ColumnLabel">
            <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%>&nbsp;&nbsp;</TD>
            <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>&nbsp;&nbsp;</TD>
    		<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>&nbsp;&nbsp;</TD>

		
        </TR>
        <%
        	boolean bl = true;
        	String rowStyle = "";
        	double totalPlanValue = 0;
        	if(dpVt!=null){
        		for(int tmp=0;tmp<dpVt.size();tmp++){
       			rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				DPTaskInfo dpTaskInfo = (DPTaskInfo) dpVt.get(tmp);
        		if(dpTaskInfo.planValue > 0){
					totalPlanValue = totalPlanValue + dpTaskInfo.planValue;
        		}
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=tmp+1%></TD>
            <TD><%=dpTaskInfo.item%></TD>
            <TD><%=dpTaskInfo.unit%></TD>            
			<TD><%=CommonTools.formatDouble(dpTaskInfo.usl)%></TD>			 
            <TD><%=CommonTools.formatDouble(dpTaskInfo.planValue)%></TD>
		     <TD><%=CommonTools.formatDouble(dpTaskInfo.lsl)%></TD>
            <TD>
            	<%
				if(Double.isNaN(dpTaskInfo.actualValue))
				{
				%>
				<INPUT size="9" type="text" maxlength="9" name="actualValue"  value="" onblur="checkActualValue(this)">
				<%
				}else 
				{%>
				<INPUT size="9" type="text" maxlength="9" name="actualValue" value="<%=CommonTools.formatDouble(dpTaskInfo.actualValue)%>" onblur="checkActualValue(this)">					
				<%}
				
				%>
			<TD><%=CommonTools.formatDouble(dpTaskInfo.deviationValue)%></TD>
		    <TD><%=ConvertString.toHtml(dpTaskInfo.dpCause)%></TD>
        </TR>
        <%}%>
        <%}%>
        
		<!--
        <TR class="TableLeft">
            <TD></TD>
            <TD><B> <%=languageChoose.getMessage("fi.jsp.qualityObjective.Total")%> </B></TD>
            <TD></TD>
            <TD><B><%=CommonTools.formatDouble(totalPlanValue)%></B></TD>
        </TR>
		-->
    </TBODY>
</TABLE>
<%if (right == 3 && !isArchive){%>
<P><INPUT type="button" class="BUTTON" name="btnUpdateDPTask" value="<%=languageChoose.getMessage("fi.jsp.milestone.Update") %>" onclick="updateDPTask();"></P>
<%}%>
<br>

<TABLE class="Table" width="95%" cellspacing="1">
	<COL span="1" width="24">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestone.Risksinproject")%> </CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD rowspan="2" width="20%"><%=languageChoose.getMessage("fi.jsp.milestone.Actualriskscenario")%></TD>
			<TD rowspan="2" width="20%"><%=languageChoose.getMessage("fi.jsp.milestone.Actualactions")%></TD>
			<TD colspan="3" align="center" width="35%"><%=languageChoose.getMessage("fi.jsp.milestone.Actualimpact")%></TD>
			<TD rowspan="2" width="10%"><%=languageChoose.getMessage("fi.jsp.milestone.Unplanned")%></TD>
			<TD rowspan="2" width="10%"><%=languageChoose.getMessage("fi.jsp.milestone.Assessmentdate")%></TD>
		</TR>
		<TR class="ColumnLabel">
			<TD width="20%"><%=languageChoose.getMessage("fi.jsp.milestone.Impactto")%></TD>
			<TD width="5%"><%=languageChoose.getMessage("fi.jsp.milestone.Unit")%></TD>
			<TD width="10%"><%=languageChoose.getMessage("fi.jsp.milestone.Actualimpact")%></TD>
		</TR>
	<%
		if (milestoneRisk != null) {
	       	for (int i = 0; i < milestoneRisk.size(); i++) {
	        	RiskInfo risk = (RiskInfo)milestoneRisk.elementAt(i);
	        	int rowspan = 0;
	        	String htmlRows = "";
	        	String actImp = "";
	        	if (risk.status == 2) {
	        		style = !style;
	      	  		className =  style ? "CellBGRnews" : "CellBGR3";
		        	for (int j = 0; j < 3; j++) {
		        		if (!risk.imp[j].equals("")) {
		        			rowspan++;
			        		if (rowspan == 1)
			        			actImp = "<TD>" + risk.imp[j] + "</TD><TD>" + risk.unt[j] + "</TD><TD>" + risk.est[j] + "</TD>";
			        		else
			        			htmlRows += "<TR class=\"" + className + "\"><TD>" + risk.imp[j] + "</TD><TD>" + risk.unt[j] + "</TD><TD>" + risk.est[j] + "</TD></TR>";
		        		}	        	
		        	}
		        	String strRowSpan = "";
		        	if (rowspan != 1) {
		        		strRowSpan = "rowspan =\"" + rowspan + "\"";
	        		}
	%>
		<TR class="<%=className%>">
			<TD <%=strRowSpan%>><%=risk.actualRiskScenario != null ? risk.actualRiskScenario : "N/A"%></TD>
			<TD <%=strRowSpan%>><%=risk.actualAction != null ? risk.actualAction : "N/A"%></TD>
			<%=actImp%>
			<TD <%=strRowSpan%>><%=risk.unplanned == 1 ? languageChoose.getMessage("fi.jsp.milestone.Yes") : languageChoose.getMessage("fi.jsp.milestone.No")%></TD>
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

<br><br>

<TABLE cellspacing="1" class="Table" width="95%">
	<COL span="1" width="24">
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.milestone.Issues")%> </CAPTION>
	<TR class="ColumnLabel">
		<TD width="276"><%=languageChoose.getMessage("fi.jsp.milestone.Description")%></TD>
		<TD width="71"><%=languageChoose.getMessage("fi.jsp.milestone.Status")%></TD>
		<TD width="71"><%=languageChoose.getMessage("fi.jsp.milestone.Priority")%></TD>
		<TD width="71"><%=languageChoose.getMessage("fi.jsp.milestone.Startdate")%></TD>
		<TD width="71"><%=languageChoose.getMessage("fi.jsp.milestone.Duedate")%></TD>
	</TR>
	<%        
		if (milestoneIssue != null) {
	        for (int i = 0; i < milestoneIssue.size(); i++) {
		        IssueInfo issueInfo = (IssueInfo)milestoneIssue.elementAt(i);
		        style = !style;
		      	className =  style ? "CellBGRnews" : "CellBGR3";
	%>
	<TR class="<%=className%>">
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

<br>

<%@ include file="requirementDetailBatchPlan.jsp" %>

<script language = "javascript">	
  		
	var Dpcount= <%= dpVt.size()%>;
	var check=<%=cusMetricList.size()%>;
	void function on_Submit1()
	{  		 	
		if(maxLength(frmComment.txtComments,"Comments",4000)==false){
  	 		frmComment.txtComments.focus();
  		 	return;
  	 	}   			 	
  	 	document.frmComment.submit();
	} 
	void function on_Submit2()
	{  		 	
		if(check<=0)
		{
			alert("No item to update");
			return;
		}
		document.mileStoneSpecObjective.submit();
	} 
	
	void function checkActualValue(txtValue){

		if (isNaN(txtValue.value)){
  			alert("Actual value must be a number");
  			txtValue.focus();
  			return;
  		}
	}
	void function check(txtValue){
	var checkValue = txtValue.value;
		if (isNaN(checkValue) || (checkValue == "")) {
  			alert("<%=languageChoose.getMessage("fi.jsp.milestone.Thevaluemustbeanumber")%>");
  			txtValue.focus();
  			return;
  		}
		if ((checkValue < -3) || (checkValue > 3)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.milestone.ThepointofRecordCusMetricPoint")%>");
  			txtValue.focus();
  			return;
  		}
	}
	
	function updateDPTask()
	{
		if(Dpcount<=0)
		{
			alert("No item to update");
			return;
		}
		frmDPTask.submit();
	}
	void function on_Calculate()
	{
		if(frmPoint.cmValue.length >1){
	  		var total = 0;
	  		for(var i=0;i< frmPoint.cmValue.length; i++){ 
	  			total = total + eval(frmPoint.cmValue[i].value);
	  		}
	  		if(total > 5 || total < -5) {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.milestone.TheTotalpointofAllCusMetricPoint")%>");
		  		frmPoint.cmValue[0].focus();
		  		return;
	  		}
	  	}
		if (isNaN(frmPoint.WO.value) || (frmPoint.WO.value == "")) {
  			alert("<%=languageChoose.getMessage("fi.jsp.milestone.Thevaluemustbeanumber")%>");
  			frmPoint.WO.focus();
  			return;
  		}
		
  	 	if ((frmPoint.WO.value < -2) || (frmPoint.WO.value > 2)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.milestone.ThepointofRecordWorkOrder")%>");
  			frmPoint.WO.focus();
  			return;
  		}
		
		if (isNaN(frmPoint.AN.value)) {
  			alert("<%=languageChoose.getMessage("fi.jsp.milestone.Thevaluemustbeanumber")%>");
  			frmPoint.AN.focus();
  			return;
  		}

		if (frmPoint.AN.value == "") {
			frmPoint.AN.value = "0";
  		}

  	 	if ((frmPoint.AN.value < -2) || (frmPoint.AN.value > 2)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.milestone.ThepointofRecordAcceptanceNote")%>");
  			frmPoint.AN.focus();
  			return;
  		}

		if (isNaN(frmPoint.PP.value) || (frmPoint.PP.value == "")) {
  			alert("<%=languageChoose.getMessage("fi.jsp.milestone.Thevaluemustbeanumber")%>");
  			frmPoint.PP.focus();
  			return;
  		}

  	 	if ((frmPoint.PP.value < -1) || (frmPoint.PP.value > 1)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.milestone.ThepointofRecordProjectPlan")%>");
  			frmPoint.PP.focus();
  			return;
  		}	

		if (isNaN(frmPoint.PR.value) || (frmPoint.PR.value == "")) {
  			alert("<%=languageChoose.getMessage("fi.jsp.milestone.Thevaluemustbeanumber")%>");
  			frmPoint.PR.focus();
  			return;
  		}

  	 	if ((frmPoint.PR.value < -10) || (frmPoint.PR.value > 10)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.milestone.ThepointofRecordProjectReports")%>");
  			frmPoint.PR.focus();
  			return;
  		}	

		if (isNaN(frmPoint.Prestige.value)) {
  			alert("<%=languageChoose.getMessage("fi.jsp.milestone.Thevaluemustbeanumber")%>");
  			frmPoint.Prestige.focus();
  			return;
  		}

		if (frmPoint.Prestige.value == "") {
			frmPoint.Prestige.value = "0";
  		}

  	 	if ((frmPoint.Prestige.value < -10) || (frmPoint.Prestige.value > 10)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.milestone.ThepointofRecordPrestige")%>");
  			frmPoint.Prestige.focus();
  			return;
  		}
		
		if (isNaN(frmPoint.CusPoint.value)) {
  			alert("<%=languageChoose.getMessage("fi.jsp.milestone.Thevaluemustbeanumber")%>");
  			frmPoint.CusPoint.focus();
  			return;
  		}

		if (frmPoint.CusPoint.value == "") {
			frmPoint.CusPoint.value = "0";
  		}

  	 	if ((frmPoint.CusPoint.value < -10) || (frmPoint.CusPoint.value > 10)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.milestone.ThepointofRecordCustomer")%>");
  			frmPoint.CusPoint.focus();
  			return;
  		}	

		document.frmPoint.submit();
	}
</script>

<%
	}
%>

</BODY>
</HTML>