<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
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
<TITLE>weeklyReport.jsp</TITLE> 
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.reportDate.focus()">
<%
	int right=Security.securiPage("Project reports",request,response);
	
	ProjectInfo projectInfo = (ProjectInfo)session.getAttribute("projectInfo");
	RequirementInfo reqHdrInfo = (RequirementInfo)session.getAttribute("reqHdrInfo");
	Date firstMonday = (Date)session.getAttribute("firstMonday");
	Date lastMonday = (Date)session.getAttribute("lastMonday");
	Date reportDate = (Date)session.getAttribute("reportDate");
	Calendar cal = new GregorianCalendar();
	cal.setTime(lastMonday);
	Vector defectWeeklyInfoList = (Vector)session.getAttribute("defectWeeklyInfo");
	Vector requirementList = (Vector)session.getAttribute("requirementList");
	Vector metricList = (Vector)session.getAttribute("metricInfo");
	Vector riskList = (Vector)session.getAttribute("riskList");
	Vector issueList = (Vector)session.getAttribute("issueList");
	String comments = (String)session.getAttribute("wrComments");
	Vector lastWeekTasks = (Vector)session.getAttribute("lastWeekTasks");
	Vector nextWeekTasks = (Vector)session.getAttribute("nextWeekTasks");
	if ((comments==null)||(comments.trim().equals(""))) {
		comments=(right!=3)?"N/A":"";
	}
	boolean style=false;
	String className = null;
%>
<TABLE width="80%">
	<TR>
		<TD><P class="TITLE"><%=languageChoose.getMessage("fi.jsp.weeklyReport.Weeklyreport")%></P></TD>
		<TD align="right" valign="bottom"><A href="weeklyReportExport.jsp" target="about:blank"><%=languageChoose.getMessage("fi.jsp.weeklyReport.ExportWeeklyReport")%></A></TD>
	</TR>
</TABLE>
<FORM name="frm" action="Fms1Servlet" method="POST">
<TABLE class="HDR">
    <TBODY>
        <TR>
            <TD width="15%"></TD>
            <TD style="text-align: right" width="20%"></TD>
            <TD width="5%"></TD>
           	<TD width="25%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Projectplannedstartdate")%> </TD>
            <TD style="text-align: right" width="10%"><%=CommonTools.dateFormat(projectInfo.getPlanStartDate())%></TD>
            <TD width="5%"></TD>
        </TR>
        <TR>
        	<TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Projectlifecycle")%> </TD>
	        <TD style="text-align: right"><%=projectInfo.getLifecycle()%></TD>
            <TD></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Projectactualstartdate")%> </TD>
            <TD style="text-align: right"><%=CommonTools.dateFormat(projectInfo.getStartDate())%></TD>
        </TR>
        <TR>
        	<TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Customer")%> </TD>
            <TD style="text-align: right"><%=projectInfo.getCustomer()%></TD>
            <TD></TD>
             <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Projectplannedenddate")%> </TD>
            <TD style="text-align: right"><%=CommonTools.dateFormat(projectInfo.getBaseFinishDate())%></TD>

        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Reportdate")%> </TD>
            <TD style="text-align: right"><SELECT class="COMBO" name="reportDate" onchange="onReportDateChange()">
            	<%
            	String sDate ;
            	while (true) {
                	if (firstMonday.compareTo(cal.getTime()) > 0)
                		break;
            		sDate = CommonTools.dateFormat(cal.getTime());
            	%><OPTION value="<%=sDate%>" <%=(cal.getTime().equals(reportDate))?"selected":""%>><%=sDate%></OPTION>
                <%	
                	cal.add(Calendar.DAY_OF_WEEK, -7);
                }
                %>
            </SELECT></TD>
            <TD></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Projectreplannedenddate")%> </TD>
            <TD style="text-align: right"><%=CommonTools.dateFormat(projectInfo.getPlanFinishDate())%></TD>
        </TR>
        <TR>
            <TD></TD>
            <TD></TD>
            <TD></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Projectactualenddate")%> </TD>
            <TD style="text-align: right"><%=CommonTools.dateFormat(projectInfo.getActualFinishDate())%></TD>
        </TR>
    </TBODY>
</TABLE>
<%if(right == 3 && !isArchive){%>
<INPUT type="button" name="btnUpdateDashboard" value="<%=languageChoose.getMessage("fi.jsp.weeklyReport.Submitweeklyreport")%>" class="BUTTONWIDTH" onclick="updateDashboard();">
<%}%>
<%=("1".equals(request.getParameter("dash")))? "<P class='ERROR'>" + languageChoose.getMessage("fi.jsp.weeklyReport.Weeklyreporthasbeensubmittedsuccessfully") + "</P>" : ""%>
<BR>
<INPUT type="hidden" name="wrDate" value="<%=CommonTools.dateFormat(reportDate)%>">
<P class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Comments")%> </P>
<%if(right == 3){%>
	<TEXTAREA rows="6" cols="70" name="txtComments"><%=comments%></TEXTAREA>
<%}else{%>
	<%=ConvertString.toHtml(comments)%><%}%>
<P><%if(right == 3 && !isArchive){%>
	<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.weeklyReport.Update")%>" class="BUTTON" onclick="updateComments();">
<%}%></P>

<TABLE class="Table" width="248" cellspacing="1">
    <COL span="1" width="160">
    <COL span="1" width="88">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Requirementcompleteness")%> </CAPTION>
    <TBODY  class="CellBGRnews">
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Total")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Size")%> </TD>
        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Committed")%> </TD>
            <TD><%=reqHdrInfo.sumSizeCommitted%></TD>
        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Designed")%> </TD>
            <TD><%=reqHdrInfo.sumSizeDesigned%></TD>
        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Coded")%> </TD>
            <TD><%=reqHdrInfo.sumSizeCoded%></TD>
        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Tested")%> </TD>
            <TD><%=reqHdrInfo.sumSizeTested%></TD>
        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Deployed")%> </TD>
            <TD><%=reqHdrInfo.sumSizeDeployed%></TD>
        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Accepted")%> </TD>
            <TD><%=reqHdrInfo.sumSizeAccepted%></TD>
        </TR>
    </TBODY>
</TABLE>
<INPUT type="hidden" name="reqType" value="<%=Constants.WEEKLY_REPORT_VIEW%>">
<BR>
<BR>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Metrics")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Item")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Unit")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.TotalPlanned")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.SpentCompleted")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Remain")%> </TD>
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
        <TR class="CellBGRNews">
        <TD><%=languageChoose.getMessage(reportMetricInfo.name)%></TD>
        <TD><%=reportMetricInfo.unit%></TD>
        <TD><%=estimated%></TD>
        <TD><%=spent%></TD>
        <TD><%=remain%></TD>
        </TR>
	<%}%>
    </TBODY>
</TABLE>
<BR>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Changesofrequirements")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD colspan="2"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Requirement")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Size1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Completenessrate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Effortimpact")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Scheduleimpact")%> </TD>
        </TR>
        <%
        if (requirementList != null)
        for (int i = 0; i < requirementList.size(); i++) {
        	RequirementInfo reqInfo = (RequirementInfo)requirementList.elementAt(i);
			style=!style;
      	  	className=(style)?"CellBGRnews":"CellBGR3";
        %>
        <TR class="<%=className%>">
            <TD colspan="2"><%=reqInfo.name%></TD>
            <TD style="text-align: left"><%=reqInfo.size%></TD>
            <TD style="text-align: left"><%=reqInfo.completenessRate >= 0 ? CommonTools.formatDouble(reqInfo.completenessRate) : "N/A"%></TD>
            <TD style="text-align: left"><%=reqInfo.effort >= 0 ? String.valueOf(reqInfo.effort) : "N/A"%></TD>
            <TD style="text-align: left"><%=reqInfo.elapsedDay >= 0 ? String.valueOf(reqInfo.elapsedDay) : "N/A"%></TD>
        </TR>
        <%}%>
	</TBODY>
</TABLE>
</FORM>
<BR>

<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Defects")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD colspan="2"></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Fatal")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Serious")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Medium")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Cosmetic")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Total1")%> </TD>
        </TR>
        <%
			if (defectWeeklyInfoList != null) {
				DefectWeeklyInfo openingDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(0);
				DefectWeeklyInfo totalDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(1);
				DefectWeeklyInfo weightedOpeningDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(2);
				DefectWeeklyInfo weightedTotalDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(3);
        %>
        <TR class="CellBGRnews">
            <TD colspan="2"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Opening")%> </TD>
            <TD><%=openingDefectWeeklyInfo.getFatal()%></TD>
            <TD><%=openingDefectWeeklyInfo.getSerious()%></TD>
            <TD><%=openingDefectWeeklyInfo.getMedium()%></TD>
            <TD><%=openingDefectWeeklyInfo.getCosmetic()%></TD>
            <TD><%=openingDefectWeeklyInfo.getTotal()%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD colspan="2"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Total2")%> </TD>
            <TD><%=totalDefectWeeklyInfo.getFatal()%></TD>
            <TD><%=totalDefectWeeklyInfo.getSerious()%></TD>
            <TD><%=totalDefectWeeklyInfo.getMedium()%></TD>
            <TD><%=totalDefectWeeklyInfo.getCosmetic()%></TD>
            <TD><%=totalDefectWeeklyInfo.getTotal()%></TD>
        </TR>
        <TR class="CellBGRnews">
            <TD colspan="2"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Weightedopening")%> </TD>
            <TD><%=weightedOpeningDefectWeeklyInfo.getFatal()%></TD>
            <TD><%=weightedOpeningDefectWeeklyInfo.getSerious()%></TD>
            <TD><%=weightedOpeningDefectWeeklyInfo.getMedium()%></TD>
            <TD><%=weightedOpeningDefectWeeklyInfo.getCosmetic()%></TD>
            <TD><%=weightedOpeningDefectWeeklyInfo.getTotal()%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD colspan="2"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Weightedtotal")%> </TD>
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
<TABLE class="Table" width="95%" cellspacing="1">
    <COL span="1" width="24">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Risksinproject")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD rowspan="2" width="20%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Actualriskscenario")%> </TD>
            <TD rowspan="2" width="20%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Actualactions")%> </TD>
            <TD colspan="3" align="center" width="35%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Actualimpact")%> </TD>
            <TD rowspan="2" width="10%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Unplanned")%> </TD>
            <TD rowspan="2" width="10%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Assessmentdate")%> </TD>
        </TR>
        <TR class="ColumnLabel">
            <TD  width="20%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Impactto")%> </TD>
            <TD  width="5%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Unit")%> </TD>
            <TD  width="10%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Actualimpact")%> </TD>
        </TR>
        <%
        if (riskList != null)
       	for (int i = 0; i < riskList.size(); i++) {
        	RiskInfo risk = (RiskInfo)riskList.elementAt(i);
        	int rowspan=0;
        	String htmlRows="";
        	String actImp ="";
        	if(risk.status==2){
        		style=!style;
      	  		className=(style)?"CellBGRnews":"CellBGR3";
	        	for (int j =0;j<3;j++){
	        		if (!risk.imp[j].equals("")){
	        			rowspan++;
		        		if (rowspan ==1)
		        			actImp="<TD>"+languageChoose.getMessage(risk.imp[j])+"</TD><TD>"+risk.unt[j]+"</TD><TD>"+risk.est[j]+"</TD>";
		        		else
		        			htmlRows+="<TR class=\""+className+"\"><TD>"+languageChoose.getMessage(risk.imp[j])+"</TD><TD>"+risk.unt[j]+"</TD><TD>"+risk.est[j]+"</TD></TR>";
	        		}
	        	}
	        	String strRowSpan="";
	        	if (rowspan!=1)
	        		strRowSpan = "rowspan =\""+rowspan+"\"";
        %><TR class="<%=className%>">
            <TD <%=strRowSpan%>><%=risk.actualRiskScenario != null ? risk.actualRiskScenario : "N/A"%></TD>
            <TD <%=strRowSpan%>><%=risk.actualAction != null ? risk.actualAction : "N/A" %></TD>
           <%=actImp%>
            <TD <%=strRowSpan%> ><%=risk.unplanned == 1 ? languageChoose.getMessage("fi.jsp.weeklyReport.Yes") : languageChoose.getMessage("fi.jsp.weeklyReport.No")%></TD>
            <TD <%=strRowSpan%>><%=CommonTools.dateFormat(risk.assessmentDate)%></TD>
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
<BR>
<TABLE class="Table" width="95%" cellspacing="1">
    <COL span="1" width="24">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Issues")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="276"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Description")%> </TD>
            <TD width="71"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Status")%> </TD>
            <TD width="71"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Priority")%> </TD>
            <TD width="71"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Startdate")%> </TD>
            <TD width="71"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Duedate")%>  </TD>
        </TR>
        <%
        if (issueList != null) 
        for (int i = 0; i < issueList.size(); i++) {
        	IssueInfo issue = (IssueInfo)issueList.elementAt(i);
			style=!style;
      	  	className=(style)?"CellBGRnews":"CellBGR3";
        %>
        <TR class="<%=className%>">
            <TD><%=issue.description%></TD>
            <TD><%=languageChoose.getMessage(issue.getStatusName())%></TD>
            <TD><%=languageChoose.getMessage(issue.getPriorityName()) %></TD>
            <TD><%=CommonTools.dateFormat(issue.startDate)%></TD>
            <TD><%=CommonTools.dateFormat(issue.dueDate)%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<P></P>
<TABLE class="Table" width="95%" cellspacing="1">
    <COL span="1" width="24">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Tasksachievedlastweek")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Type")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Description1")%> </TD>
            <TD width="15%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Plannedenddate")%> </TD>
            <TD width="15%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Actualenddate")%> </TD>
        </TR>
        <%
        for (int i = 0; i < lastWeekTasks.size(); i++) {
        	TaskInfo taskInfo = (TaskInfo)lastWeekTasks.elementAt(i);
			style=!style;
      	  	className=(style)?"CellBGRnews":"CellBGR3";
        %>
        <TR class="<%=className%>">
            <TD><%=languageChoose.getMessage(taskInfo.type)%></TD>
            <TD><%=languageChoose.getMessage(taskInfo.desc)%></TD>
            <TD><%=CommonTools.dateFormat(taskInfo.planDate)%></TD>
            <TD><%=CommonTools.dateFormat(taskInfo.actualDate)%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<BR>

<TABLE class="Table" width="95%" cellspacing="1">
    <COL span="1" width="24">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Pendingtasksfornextweek")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="15%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Type1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Description2")%> </TD>
            <TD width="15%"> <%=languageChoose.getMessage("fi.jsp.weeklyReport.Plannedenddate1")%> </TD>
        </TR>
        <%
        for (int i = 0; i < nextWeekTasks.size(); i++) {
        	TaskInfo taskInfo = (TaskInfo)nextWeekTasks.elementAt(i);
			style=!style;
      	  	className=(style)?"CellBGRnews":"CellBGR3";
        %>
        <TR class="<%=className%>">
            <TD><A href="<%=taskInfo.getLink()%>"><%=languageChoose.getMessage(taskInfo.type)%></A></TD>
            <TD><%=taskInfo.desc%></TD>
            <TD NOWRAP><%=CommonTools.dateFormat(taskInfo.planDate)%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<P></P>
<SCRIPT language="javascript">
function onReportDateChange() {
	frm.submit();
}
 
function updateComments(){	
	if (maxLength(frm.txtComments,"Comments",4000)){
	  	frm.action="Fms1Servlet?reqType=<%=Constants.UPDATE_WR_COMMENTS%>";
	  	frm.submit();
	}
} 
function updateDashboard(){
	frm.action="Fms1Servlet?reqType=<%=Constants.UPDATE_DASHBOARD%>";
	frm.submit();
}	

</SCRIPT>
</BODY>
</HTML>
