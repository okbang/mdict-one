<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>
<%@ page language="java" import="javax.servlet.*,
            fpt.timesheet.bean.Report.InquiryReportBean,
            fpt.timesheet.framework.util.StringUtil.StringMatrix"
%>
<%@ page buffer = "256kb" %>
<%@ page isThreadSafe="false" errorPage="error.jsp" %>
<%
    InquiryReportBean beanInquiryReport = (InquiryReportBean)request.getAttribute("beanInquiryReport");
    StringMatrix mtxTimesheet = beanInquiryReport.getTimesheetList();
    int maxrows = mtxTimesheet.getNumberOfRows();
%>
<HTML>
<HEAD>
<TITLE>Defect Export</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<STYLE type="text/css">
.header
{
    FONT-WEIGHT: bold;
    BACKGROUND-COLOR: #C0C0C0;
}
</STYLE>
</HEAD>
<BODY bgcolor="#FFFFFF">
<FORM method="POST" action="TimesheetServlet" name="frmExportInquiryReport">
<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
    <COLGROUP>
        <COL align="center" valign="center" width="10%">
        <COL align="center" valign="center" width="8%">
        <COL align="center" valign="center" width="8%">
        <COL align="center" valign="center" width="8%">
        <COL align="center" valign="center" width="30%">
        <COL align="center" valign="center" width="5%">
        <COL align="center" valign="center" width="12%">
        <COL align="center" valign="center" width="12%">
        <COL align="center" valign="center" width="12%">
        <COL align="center" valign="center" width="5%">
        <COL align="center" valign="center" width="10%">
        <COL align="center" valign="center" width="15%">
        <COL align="center" valign="center" width="10%">
        <COL align="center" valign="center" width="10%">
    <TR class="header">
        <TD align="center">Project</TD>
        <TD align="center">Group</TD>
        <TD align="center">Account</TD>
        <TD align="center">Date</TD>
        <TD align="center">Description</TD>
        <TD align="center">Time</TD>
        <TD align="center">Process</TD>
        <TD align="center">Work</TD>
        <TD align="center">Product</TD>
        <TD align="center">KPA</TD>
        <TD align="center">Approver</TD>
        <TD align="center">Time Stamp</TD>
		<TD align="center">PL approved time</TD>
        <TD align="center">QA approved time</TD>        
    </TR><%
    for(int i = 0; i < maxrows; i++) {
%>
    <TR>
        <TD><%=mtxTimesheet.getCell(i, 0)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 13)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 1)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 2)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 3)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 4)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 5)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 6)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 7)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 8)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 9)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 14)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 15)%></TD>
        <TD><%=mtxTimesheet.getCell(i, 16)%></TD>
    </TR><%
    }
%>
</TABLE>
</FORM>
</BODY>
</HTML>