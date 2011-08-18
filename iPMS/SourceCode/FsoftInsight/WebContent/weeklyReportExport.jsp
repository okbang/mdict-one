<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%><%@page import="com.fms1.infoclass.*,java.util.*,java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>weeklyReport.jsp</TITLE>
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
</STYLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	response.addHeader("Content-Disposition", "attachment;filename=weeklyReport.xls");
%>
<BODY>
<%
	ProjectInfo projectInfo= (ProjectInfo) session.getAttribute("projectInfo");
	RequirementInfo reqHdrInfo = (RequirementInfo)session.getAttribute("reqHdrInfo");
	Date reportDate = (Date)session.getAttribute("reportDate");
	Vector requirementList = (Vector)session.getAttribute("requirementList");
	Vector defectWeeklyInfoList = (Vector)session.getAttribute("defectWeeklyInfo");
	Vector metricList = (Vector)session.getAttribute("metricInfo");
	Vector riskList = (Vector)session.getAttribute("riskList");
	Vector issueList = (Vector)session.getAttribute("issueList");
	String comments = (String)session.getAttribute("wrComments");
	Vector lastWeekTasks = (Vector)session.getAttribute("lastWeekTasks");
	Vector nextWeekTasks = (Vector)session.getAttribute("nextWeekTasks");
%>
<TABLE>
	<TR>
		<TD><P class="Title"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Weeklyreport")%> </P></TD>
	</TR>
</TABLE>
<BR>
<TABLE>
	<COLGROUP>
		<COL width="40%" class="TableCaption">
		<COL width="15%" align="left">
		<COL width="15%">
		<COL width="20%" class="TableCaption">
		<COL width="15%" align="right">
    <TBODY>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Project")%></TD>
            <TD><%=(String)session.getAttribute("workUnitName")%></TD>
            <TD></TD>
           	<TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Plannedstartdate")%></TD>
            <TD><%=CommonTools.dateFormat(projectInfo.getPlanStartDate())%></TD>
        </TR>
        <TR>
        	<TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Projectlifecycle")%></TD>
	        <TD><%=projectInfo.getLifecycle()%></TD>
            <TD></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Actualstartdate")%></TD>
            <TD><%=CommonTools.dateFormat(projectInfo.getStartDate())%></TD>
        </TR>
        <TR>
        	<TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Customer")%></TD>
            <TD><%=projectInfo.getCustomer()%></TD>
            <TD></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Plannedenddate")%></TD>
            <TD><%=CommonTools.dateFormat(projectInfo.getBaseFinishDate())%></TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Reportdate")%></TD>
            <TD><%=CommonTools.dateFormat(reportDate)%></TD>
            <TD></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Replannedenddate")%></TD>
            <TD><%=CommonTools.dateFormat(projectInfo.getPlanFinishDate())%></TD>
        </TR>
        <TR>
            <TD></TD>
            <TD></TD>
            <TD></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Actualenddate")%></TD>
            <TD><%=CommonTools.dateFormat(projectInfo.getActualFinishDate())%></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<TABLE class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Comments")%> </CAPTION>
	<TR>
		<TD colspan="2" rowspan="3"><%=ConvertString.toHtml(comments)%></TD>
	</TR>
</TABLE>
<BR>
<TABLE class="Table">
	<COLGROUP>
		<COL width="20%">
		<COL width="15%" align="right">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Requirementcompleteness")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Total")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Size")%></TD>
        </TR>
        <TR class="Cell">
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Committed")%></TD>
            <TD><%=reqHdrInfo.sumSizeCommitted%></TD>
        </TR>
        <TR class="Cell">
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Designed")%></TD>
            <TD><%=reqHdrInfo.sumSizeDesigned%></TD>
        </TR>
        <TR class="Cell">
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Coded")%></TD>
            <TD><%=reqHdrInfo.sumSizeCoded%></TD>
        </TR class="Cell">
        <TR class="Cell">
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Tested")%></TD>
            <TD><%=reqHdrInfo.sumSizeTested%></TD>
        </TR>
        <TR class="Cell">
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Deployed")%></TD>
            <TD><%=reqHdrInfo.sumSizeDeployed%></TD>
        </TR>
        <TR class="Cell">
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Accepted")%></TD>
            <TD><%=reqHdrInfo.sumSizeAccepted%></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<TABLE class="Table">
	<COLGROUP>
		<COL width="20%">
		<COL width="15%">
		<COL width="15%" align="right">
		<COL width="20%" align="right">
		<COL width="15%" align="right">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Metrics")%> </CAPTION>
    <TBODY>
    <TR class="ColumnLabel">
    	<TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Item")%> </TD>
    	<TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Unit")%> </TD>
    	<TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.TotalPlanned")%> </TD>
    	<TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.SpentCompleted")%> </TD>
    	<TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Remain")%> </TD>
	</TR>
<%
    ReportMetricInfo reportMetricInfo=null;
    String estimated=null;
    String spent=null;
    String remain=null;
	for (int i = 0; i < metricList.size(); i++) {
		reportMetricInfo=(ReportMetricInfo)metricList.elementAt(i);
		if (!reportMetricInfo.unit.equalsIgnoreCase("DD-MMM-YY")){
			estimated=CommonTools.formatDouble(reportMetricInfo.estimated);
			spent=CommonTools.formatDouble(reportMetricInfo.spent);
			remain=CommonTools.formatDouble(reportMetricInfo.remain);
		}
		else{
			estimated=CommonTools.dateFormat(reportMetricInfo.estimated);
			spent=CommonTools.dateFormat(reportMetricInfo.spent);
			remain=CommonTools.dateFormat(reportMetricInfo.remain);
		}
		spent=Color.setColor(reportMetricInfo.color,spent);
%>
	<TR class="Cell">
        <TD><%=languageChoose.getMessage(reportMetricInfo.name)%></TD>
        <TD><%=reportMetricInfo.unit%></TD>
        <TD><%=estimated%></TD>
        <TD><%=spent%></TD>
        <TD><%=remain%></TD>
	</TR>
<%
	}
%>
    </TBODY>
</TABLE>
<BR>
<TABLE class="Table">
	<COLGROUP>
		<COL width="20%">
		<COL width="15%" align="right">
		<COL width="15%" align="right">
		<COL width="20%" align="right">
		<COL width="15%" align="right">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Changesofrequirements")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Requirement")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Size1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Completenessrate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Effortimpact")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Scheduleimpact")%> </TD>
        </TR>
<%
	if (requirementList != null)
        for (int i = 0; i < requirementList.size(); i++) {
        	RequirementInfo reqInfo = (RequirementInfo)requirementList.elementAt(i);
%>
        <TR class="Cell">
            <TD><%=reqInfo.name%></TD>
            <TD><%=reqInfo.size%></TD>
            <TD><%=reqInfo.completenessRate >= 0 ? CommonTools.formatDouble(reqInfo.completenessRate) : "N/A"%></TD>
            <TD><%=reqInfo.effort >= 0 ? String.valueOf(reqInfo.effort) : "N/A"%></TD>
            <TD><%=reqInfo.elapsedDay >= 0 ? String.valueOf(reqInfo.elapsedDay) : "N/A"%></TD>
        </TR>
<%
		}
%>
	</TBODY>
</TABLE>
<BR>
<TABLE class="Table">
	<COLGROUP>
		<COL width="20%">
		<COL width="15%" align="right">
		<COL width="15%" align="right">
		<COL width="20%" align="right">
		<COL width="15%" align="right">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Defects")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Fatal")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Serious")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Medium")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Cosmetic")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Total1")%> </TD>
        </TR>
        <%
			if (defectWeeklyInfoList != null) {
				DefectWeeklyInfo openingDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(0);
				DefectWeeklyInfo totalDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(1);
				DefectWeeklyInfo weightedOpeningDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(2);
				DefectWeeklyInfo weightedTotalDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(3);
        %>
        <TR class="Cell">
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Opening")%></TD>
            <TD><%=openingDefectWeeklyInfo.getFatal()%></TD>
            <TD><%=openingDefectWeeklyInfo.getSerious()%></TD>
            <TD><%=openingDefectWeeklyInfo.getMedium()%></TD>
            <TD><%=openingDefectWeeklyInfo.getCosmetic()%></TD>
            <TD><%=openingDefectWeeklyInfo.getTotal()%></TD>
        </TR>
        <TR class="Cell">
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Total2")%></TD>
            <TD><%=totalDefectWeeklyInfo.getFatal()%></TD>
            <TD><%=totalDefectWeeklyInfo.getSerious()%></TD>
            <TD><%=totalDefectWeeklyInfo.getMedium()%></TD>
            <TD><%=totalDefectWeeklyInfo.getCosmetic()%></TD>
            <TD><%=totalDefectWeeklyInfo.getTotal()%></TD>
        </TR>
        <TR class="Cell">
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Weightedopening")%></TD>
            <TD><%=weightedOpeningDefectWeeklyInfo.getFatal()%></TD>
            <TD><%=weightedOpeningDefectWeeklyInfo.getSerious()%></TD>
            <TD><%=weightedOpeningDefectWeeklyInfo.getMedium()%></TD>
            <TD><%=weightedOpeningDefectWeeklyInfo.getCosmetic()%></TD>
            <TD><%=weightedOpeningDefectWeeklyInfo.getTotal()%></TD>
        </TR>
        <TR class="Cell">
            <TD><%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Weightedtotal")%></TD>
            <TD><%=weightedTotalDefectWeeklyInfo.getFatal()%></TD>
            <TD><%=weightedTotalDefectWeeklyInfo.getSerious()%></TD>
            <TD><%=weightedTotalDefectWeeklyInfo.getMedium()%></TD>
            <TD><%=weightedTotalDefectWeeklyInfo.getCosmetic()%></TD>
            <TD><%=weightedTotalDefectWeeklyInfo.getTotal()%></TD>
        </TR>
		<%
			}
		%>
    </TBODY>
</TABLE>
<BR>
<TABLE class="Table">
	<COLGROUP>
		<COL width="20%">
		<COL width="15%">
		<COL width="15%">
		<COL width="20%">
		<COL width="15%" align="right">
		<COL width="15%" align="right">
		<COL width="15%" align="right">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Risksinproject")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Actualriskscenario")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Actualactions")%> </TD>
            <TD colspan="3"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Actualimpact")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Unplanned")%> </TD>
            <TD rowspan="2"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Assessmentdate")%> </TD>
        </TR>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Impactto")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Unit1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Estimatedimpact")%> </TD>
        </TR>
<%
	if (riskList != null)
       	for (int i = 0; i < riskList.size(); i++) {
        	RiskInfo risk = (RiskInfo)riskList.elementAt(i);
        	int rowspan=0;
        	String htmlRows="";
        	String actImp ="";
        	if(risk.status==2){
	        	for (int j =0;j<3;j++){
	        		if (!risk.imp[j].equals("")){
	        			rowspan++;
		        		if (rowspan ==1)
		        			actImp="<TD>"+languageChoose.getMessage(risk.imp[j])+"</TD><TD>"+risk.unt[j]+"</TD><TD>"+risk.est[j]+"</TD>";
		        		else
		        			htmlRows+="<TR class=\"Cell\"><TD>"+languageChoose.getMessage(risk.imp[j])+"</TD><TD>"+risk.unt[j]+"</TD><TD>"+risk.est[j]+"</TD></TR>";
	        		}
	        	}
	        	String strRowSpan="";
	        	if (rowspan!=1)
	        		strRowSpan = "rowspan =\""+rowspan+"\"";
%>
		<TR class="Cell">
            <TD <%=strRowSpan%>><%=risk.actualRiskScenario != null ? risk.actualRiskScenario : "N/A"%></TD>
            <TD <%=strRowSpan%>><%=risk.actualAction != null ? risk.actualAction : "N/A" %></TD>
            <%=actImp%>
            <TD <%=strRowSpan%>><%=risk.unplanned == 1 ? languageChoose.getMessage("fi.jsp.weeklyReportExport.Yes") : languageChoose.getMessage("fi.jsp.weeklyReportExport.No")%></TD>
            <TD <%=strRowSpan%>><%=CommonTools.dateFormat(risk.assessmentDate)%></TD>
        </TR>
<%
        		if (rowspan!=1) {
%>
        <%=htmlRows%>
<%
				}
       		}
        }
%>
    </TBODY>
</TABLE>
<BR>
<TABLE class="Table">
	<COLGROUP>
		<COL width="20%">
		<COL width="15%" align="right">
		<COL width="15%" align="right">
		<COL width="20%" align="right">
		<COL width="15%" align="right">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Issues")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Description")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Status")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Priority")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Startdate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Duedate")%>  </TD>
        </TR>
<%
	if (issueList != null)
        for (int i = 0; i < issueList.size(); i++) {
        	IssueInfo issue = (IssueInfo)issueList.elementAt(i);
        %>
        <TR class="Cell">
            <TD><%=issue.description%></TD>
            <TD><%=languageChoose.getMessage(issue.getStatusName())%></TD>
            <TD><%=languageChoose.getMessage(issue.getPriorityName()) %></TD>
            <TD><%=CommonTools.dateFormat(issue.startDate)%></TD>
            <TD><%=CommonTools.dateFormat(issue.dueDate)%></TD>
        </TR>
<%
		}
%>
    </TBODY>
</TABLE>
<BR>
<TABLE class="Table">
	<COLGROUP>
		<COL width="20%">
		<COL width="15%">
		<COL width="15%" align="right">
		<COL width="20%" align="right">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Tasksachievedlastweek")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Type")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Description1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Plannedenddate1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Actualenddate1")%> </TD>
        </TR>
<%
	for (int i = 0; i < lastWeekTasks.size(); i++) {
		TaskInfo taskInfo = (TaskInfo)lastWeekTasks.elementAt(i);
%>
        <TR class="Cell">
            <TD><%=languageChoose.getMessage(taskInfo.type)%></TD>
            <TD><%=taskInfo.desc%></TD>
            <TD><%=CommonTools.dateFormat(taskInfo.planDate)%></TD>
            <TD><%=CommonTools.dateFormat(taskInfo.actualDate)%></TD>
        </TR>
<%
	}
%>
    </TBODY>
</TABLE>
<BR>
<TABLE class="Table">
	<COLGROUP>
		<COL width="20%">
		<COL width="15%">
		<COL width="15%" align="right">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Pendingtasksfornextweek")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Type1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Description2")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReportExport.Plannedenddate2")%> </TD>
        </TR>
<%
	for (int i = 0; i < nextWeekTasks.size(); i++) {
    	TaskInfo taskInfo = (TaskInfo)nextWeekTasks.elementAt(i);
%>
        <TR class="Cell">
            <TD><%=languageChoose.getMessage(taskInfo.type)%></TD>
            <TD><%=taskInfo.desc%></TD>
            <TD><%=CommonTools.dateFormat(taskInfo.planDate)%></TD>
        </TR>
<%
	}
%>
    </TBODY>
</TABLE>
<BR>
</BODY>
</HTML>