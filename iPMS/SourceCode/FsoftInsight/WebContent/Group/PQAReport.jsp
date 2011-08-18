<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.Vector,java.util.Calendar,com.fms1.tools.*, java.sql.Date  ,com.fms1.html.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%  boolean report = (request.getParameter("report") != null); 
	if (report) {%>
<style TYPE="text/css">
<!--
<%@ include file="../stylesheet/fms.css" %>
-->
</style>
<%} else {%>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<%}%>

<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="JavaScript">
        <%@ include file="../javaFns.jsp"%>
</SCRIPT>
<%int right = Security.securiPage("Group reports", request, response);
Date fromDate = (Date) request.getAttribute("fromDate");
Date toDate = (Date) request.getAttribute("toDate");
String fromDateStr = CommonTools.dateUpdate(fromDate);
String toDateStr = CommonTools.dateUpdate(toDate);
Vector metrics = (Vector) request.getAttribute("metrics");
Vector issueList1 = (Vector) session.getAttribute("issueList");
Vector issueList = new Vector();
IssueInfo issueInfo;
for (int i = 0; i < issueList1.size(); i++) {
	issueInfo = (IssueInfo) issueList1.elementAt(i);
	if (issueInfo.statusID == 0
		&& (issueInfo.dueDate.compareTo(fromDate) >= 0)
		&& (issueInfo.dueDate.compareTo(toDate) <= 0)) {
		issueList.add(issueInfo);
	}
}
Vector tasks = (Vector) request.getAttribute("tasks");
long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
    String wu= (workUnitID == Parameters.SQA_WU)?"SQA":"PQA";
    
%>
<%
	String name = wu.toLowerCase();
	response.setContentType(
	(report)
		? Fms1Servlet.CONTENT_TYPE_EXCEL
		: Fms1Servlet.CONTENT_TYPE_NORMAL);
	if (report)	{
		response.addHeader("Content-Disposition", "attachment;filename="+name+"Report.xls");
	}	
%>
<TITLE>Report</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%if (!report){%>
<BODY class="BD" onload="loadSQAMenu('Report');">
<%}else{%>
<BODY class="BD">
<%}%>
<P class="TITLE"><%=languageChoose.paramText(new String[]{"fi.jsp.PQAReport.~PARAM1_WORKUNIT~report",wu})%>
</P>
<%if (!report) {%>
<P align=right><A href="javascript:report()"> <%=languageChoose.getMessage("fi.jsp.PQAReport.Export")%>
</A></P>
<%}%>
<br>
<FORM action="Fms1Servlet?reqType=<%=Constants.PQA_REPORT%>" name="frm" method="post">
<%if (!report){%>
<INPUT type="hidden" name="report" value="1" disabled>
<TABLE  border=0 cellspacing=0 cellpadding=0 style='border-collapse:collapse;'>
	<TBODY>
		<TR class="NormalText">
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Fromdate")%>&nbsp;</TD>
			<TD NOWRAP><INPUT size="9" type="text" maxlength="9" name="fromDate"
				value="<%=fromDateStr%>"> (DD-MMM-YY)
			</TD>
			<TD><INPUT type="button" class="BUTTON"
				value=" <%=languageChoose.getMessage("fi.jsp.PQAReport.Go")%> "
				onclick="dofilter()"></TD>
		</TR>
		<TR class="NormalText">
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Todate")%></TD>
			<TD><INPUT size="9" type="text" maxlength="9" name="toDate"
				value="<%=toDateStr%>"> (DD-MMM-YY)
			</TD>

		</TR>
	</TBODY>
</TABLE>
</FORM>
<%}%>
<p><br>
</P>
<TABLE class="Table" width="95%" cellspacing="1">
	<CAPTION align="left"
		class="<%=(report) ? "ColumnLabel" : "TableCaption"%>"><%= languageChoose.paramText(new String[]{"fi.jsp.PQAReport.~PARAM1_WORKUNIT~metricsfrom~PARAM2_DATE~to~PARAM3_DATE~", wu, fromDateStr, toDateStr})%></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.ID")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Name")%></TD>
			<TD align="center"><%=languageChoose.getMessage("fi.jsp.PQAReport.Unit")%>
			</TD>
			<TD align="center"><%=languageChoose.getMessage("fi.jsp.PQAReport.Norm")%>
			</TD>
			<TD align="center"><%=languageChoose.getMessage("fi.jsp.PQAReport.Actual")%>
			</TD>
			<TD align="center"><%=languageChoose.getMessage("fi.jsp.PQAReport.Deviation")%>
			</TD>
		</TR>
		<%MetricInfo inf;
for (int i = 0; i < metrics.size(); i++) {
	inf = (MetricInfo) metrics.elementAt(i);%>
		<TR class="CellBGRnews">
			<TD><%=inf.id%></TD>
			<TD><%=languageChoose.getMessage(inf.name)%></TD>
			<TD><%=inf.unit%></TD>
			<TD><%=CommonTools.formatDouble(inf.plannedValue)%></TD>
			<TD><%=CommonTools.formatDouble(inf.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(inf.deviation)%></TD>
		</TR>
		<%}%>
</TABLE>
<p><br>
</P>
<TABLE class="Table" width="95%" cellspacing="1" id="tableIssue">
	<CAPTION align="left"
		class="<%=(report) ? "ColumnLabel" : "TableCaption"%>"><%=languageChoose.getMessage("fi.jsp.PQAReport.Openissues")%>
	</CAPTION>
	<TR class="ColumnLabel">
		<%if (workUnitID == Parameters.PQA_WU) {%>
		<TD width="18%"><%=languageChoose.getMessage(WUCombo.label)%></TD>
		<%}%>
		<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Description")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Status")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Priority")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Owner")%></TD>
		<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.DueDate")%></TD>

	</TR>
	<%String className;
for (int i = 0; i < issueList.size(); i++) {
	issueInfo = (IssueInfo) issueList.elementAt(i);
	className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";%>
	<TR class="<%=className%>">
		<%if (workUnitID == Parameters.PQA_WU) {%>
		<TD><%=issueInfo.wuName%></TD>
		<%}%>
		<TD><%=issueInfo.description%></TD>
		<TD><%=languageChoose.getMessage(issueInfo.getStatusName())%></TD>
		<TD><%=languageChoose.getMessage(issueInfo.getPriorityName())%></TD>
		<TD><%=issueInfo.owner%></TD>
		<TD NOWRAP><%=CommonTools.dateFormat(issueInfo.dueDate)%></TD>
	</TR>
	<%}%>
</TABLE>
<p><br>
</P>
<TABLE class="Table" width="95%" cellspacing="1">
	<CAPTION align="left"
		class="<%=(report) ? "ColumnLabel" : "TableCaption"%>"><%=languageChoose.getMessage("fi.jsp.PQAReport.Tasks")%>
	</CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Group")%> <br>
			<%=languageChoose.getMessage("fi.jsp.PQAReport.Project")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Type")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Description")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Assignedto")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Actualeffort")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Planneddate")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Actualdate")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Status")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.PQAReport.Note")%></TD>
		</TR>
		<%TaskInfo taskInfo;
String style;
java.util.Date now = new java.util.Date();
java.util.Date pldate;
String color;
for (int i = 0; i < tasks.size(); i++) {
	taskInfo = (TaskInfo) tasks.elementAt(i);
	style = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
	pldate =
		(taskInfo.rePlanDate != null) ? taskInfo.rePlanDate : taskInfo.planDate;
	if (pldate.compareTo(now) < 0
		&& (taskInfo.status == TaskInfo.STATUS_PENDING
			|| taskInfo.status == TaskInfo.STATUS_NOT_PASSED))
		color = "style='color: red'";
	else
		color = "";%>
		<TR class="<%=style%>">
			<TD><%=((taskInfo.prjCode == null) ? taskInfo.grpName : taskInfo.prjCode)%></TD>
			<TD><%=languageChoose.getMessage(taskInfo.type)%></TD>
			<TD><%=(taskInfo.desc == null)? "N/A": taskInfo.desc%></TD>
			<TD><%=((taskInfo.assignedToStr == null)? "N/A": taskInfo.assignedToStr)%></TD>
			<TD><%=CommonTools.formatDouble(taskInfo.effort)%></TD>
			<TD NOWRAP <%=color%>><%=CommonTools.dateFormat(taskInfo.planDate)%></TD>
			<TD NOWRAP <%=color%>><%=CommonTools.dateFormat(taskInfo.actualDate)%></TD>
			<TD><%=languageChoose.getMessage(TaskInfo.statusSQA[taskInfo.status])%></TD>
			<TD><%=taskInfo.note == null ? "" : taskInfo.note%></TD>
		</TR>
		<%}%>
		</TBODY >
	</TABLE >
		<P><br>
		</P>
</BODY>
<SCRIPT language="JavaScript">
//objs to hide when submenu is displayed
var objToHide=new Array(frm.fromDate,frm.toDate);
	function dofilter(){
		if (mandatoryDateFld(frm.fromDate,"<%=languageChoose.getMessage("fi.jsp.PQAReport.Fromdate")%>"))
		if (mandatoryDateFld(frm.toDate,"<%=languageChoose.getMessage("fi.jsp.PQAReport.todate")%>"))
		if (compareDate(frm.fromDate.value,frm.toDate.value)>=0){
			if (frm.fromDate.value !='<%=fromDateStr%>'
				||frm.toDate.value !='<%=toDateStr%>'){
				frm.report.disabled=true;
				frm.target="";
				frm.submit();
			}
		}
		else{
			alert("<%=languageChoose.getMessage(
	"fi.jsp.PQAReport.FromdatemustbebeforeorequaltoTodate")%>");
			frm.fromDate.focus();
		}
	}
	function report(){
		if (mandatoryDateFld(frm.fromDate,"<%=languageChoose.getMessage("fi.jsp.PQAReport.Fromdate")%>"))
		if (mandatoryDateFld(frm.toDate,"<%=languageChoose.getMessage("fi.jsp.PQAReport.todate")%>"))
		if (compareDate(frm.fromDate.value,frm.toDate.value)>=0){
				frm.report.disabled=false;
				frm.target="about:blank";
				frm.submit();
		}
		else{
			alert("<%=languageChoose.getMessage(
				"fi.jsp.PQAReport.FromdatemustbebeforeorequaltoTodate")%>");
			frm.fromDate.focus();
		}
	}
</SCRIPT></HTML>
