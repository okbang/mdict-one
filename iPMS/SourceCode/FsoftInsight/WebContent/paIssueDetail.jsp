<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.tools.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<TITLE>paIssueDetail.jsp</TITLE>
<SCRIPT language="JavaScript">
	function doOnload()
	{
		window.open("header.jsp","header");
		loadOrgMenu();
	}
</SCRIPT>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad="doOnload();" class="BD">

<%
	Vector vtIssueList = (Vector)session.getAttribute("vtIssueList");
	Vector vtProcess = (Vector)session.getAttribute("vtProcess");
	
	int vtIssueId = Integer.parseInt(request.getParameter("vtIssueId"));
	
	IssueInfo issue = (IssueInfo)vtIssueList.elementAt(vtIssueId);
	
	String strWuID = (String) session.getAttribute("wuID");
	String strIssueType = (String) session.getAttribute("strIssueType");
	
	String fromDate = (String) session.getAttribute("fromDate");
	String toDate = (String) session.getAttribute("toDate");
	
	long lWuID = 132; // FSOFT by default
	if (strWuID != null)
		lWuID = Long.parseLong(strWuID);
	
	if (strIssueType == null)
		strIssueType = "-1";
	
	ReportMonth rm = new ReportMonth();
	if (fromDate == null)
		fromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2,4);
	
	if (toDate == null)
		toDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2,4);
		
%>

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.paIssueDetail.ProcessAssetsIssues")%> </P>
<FORM name="frm" action="Fms1Servlet" method="post">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.paIssueDetail.Issuedetails")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width ="30%"><%=languageChoose.getMessage("fi.jsp.paIssueDetail.Description")%></TD>
            <TD class="CellBGR3"><%=issue.description%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paIssueDetail.Status")%></TD>
            <TD class="CellBGR3"><%=languageChoose.getMessage(issue.getStatusName())%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paIssueDetail.Priority")%></TD>
            <TD class="CellBGR3"><%=languageChoose.getMessage(issue.getPriorityName())%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paIssueDetail.Type")%></TD>
            <TD class="CellBGR3"><%=languageChoose.getMessage(issue.getTypeName())%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.paIssueDetail.Processrelated")%> </TD>
            <TD class="CellBGR3">
           	<%
           		String processName = languageChoose.getMessage("fi.jsp.paIssueDetail.Other");
           		for(int i=0;i<vtProcess.size();i++)
           		{
           			ProcessInfo psInfo=(ProcessInfo)vtProcess.get(i);
           			if (psInfo.processId == issue.processId)
           			{
						processName = psInfo.name;
	           			break;
           			}
           		}
           	%>
			<%=processName%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paIssueDetail.Owner")%></TD>
            <TD class="CellBGR3"><%=issue.owner%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paIssueDetail.Creator") %></TD>
            <TD class="CellBGR3"><%=issue.creator%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paIssueDetail.CreatedDate")%></TD>
            <TD class="CellBGR3"><%=CommonTools.dateFormat(issue.startDate)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paIssueDetail.DueDate")%></TD>
            <TD class="CellBGR3"><%=CommonTools.dateFormat(issue.dueDate)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paIssueDetail.CommentSolution") %></TD>
            <TD class="CellBGR3"><%=((issue.comment==null)?"N/A":issue.comment)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paIssueDetail.ClosedDate")%></TD>
            <TD class="CellBGR3"><%=CommonTools.dateFormat(issue.closeDate)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paIssueDetail.Reference") %></TD>
            <TD class="CellBGR3"><%=((issue.reference==null)?"N/A":issue.reference)%></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="hidden" name="reqType">
<INPUT type = "hidden" name = "cboGroup" value="<%=strWuID%>">
<INPUT type = "hidden" name = "cboIssueType" value="<%=strIssueType%>">
<INPUT type = "hidden" name = "fromDate" value="<%=fromDate%>">
<INPUT type = "hidden" name = "toDate" value="<%=toDate%>">
<INPUT type = "button" name = "btnBack" value="<%=languageChoose.getMessage("fi.jsp.paIssueDetail.Back")%>" onclick="onBack()" class="BUTTON">
</FORM>

<SCRIPT>
function onBack() {
	frm.reqType.value = "<%=Constants.PROASS_ISSUE%>";
	frm.submit();
}
</SCRIPT>

</BODY>
</HTML>
