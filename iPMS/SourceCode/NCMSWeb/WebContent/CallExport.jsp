<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%><%@
    page language = "java"
    import = "java.util.*, javax.servlet.*,
              fpt.ncms.constant.NCMS,
              fpt.ncms.bean.*,
              fpt.ncms.util.StringUtil.*" %><%@
    page buffer = "1028kb" autoFlush = "false"%><%@
    page isThreadSafe = "false" errorPage = "error.jsp" %><%
    NCListBean beanNCList = (NCListBean)session.getAttribute("beanNCList");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>Export</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<STYLE type="text/css">
.header
{
    FONT-WEIGHT: bold;
    BACKGROUND-COLOR: #C0C0C0;
}
</STYLE>
</HEAD>
<BODY>
<FORM method="POST" action="NcmsServlet" name="frmExportCall">
<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
    <COLGROUP>
        <COL width="3%">
        <COL width="3%">
        <COL width="8%">
        <COL width="8%">
        <COL width="8%">
        <COL width="8%">
        <COL width="8%">
        <COL width="8%">
        <COL width="12%">
        <COL width="20%">
        <COL width="12%">
        <COL width="8%">
        <COL width="12%">
        <COL width="8%">
        <COL width="12%">
        <COL width="12%">
        <COL width="20%">
        <COL width="12%">
        <COL width="20%">
        <COL width="12%">
        <COL width="8%">
    <TR class="header">
        <TD valign="middle">#</TD>
        <TD valign="middle">ID</TD>
        <TD valign="middle">Project Code</TD>
        <TD valign="middle">Group Name</TD>
        <TD valign="middle">Assignee</TD>
        <TD valign="middle">Priority</TD>
        <TD valign="middle">Request</TD>
        <TD valign="middle">Request Detail</TD>
        <TD valign="middle">Request To</TD>
        <TD valign="middle">Creator</TD>
        <TD align="center" valign="middle">Log Date</TD>
        <TD valign="middle">Review Date</TD>
        <TD align="center" valign="middle">Deadline</TD>
        <TD align="center" valign="middle">Closed Date</TD>
        <TD valign="middle">Solution</TD>
        <TD valign="middle">Type Of Solution</TD>
        <TD valign="middle">Result</TD>
        <TD valign="middle">Process</TD>
        <TD valign="middle">Status</TD>
    </TR><%
    for (int i = 0; i < beanNCList.getNCList().getNumberOfRows(); i++) {
%>
    <TR>
        <TD valign="middle"><%=i + 1%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 1)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 2)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 3)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 4)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 5)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 6)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 7)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 8)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 9)%></TD>
        <TD align="center" valign="middle"><%=beanNCList.getNCList().getCell(i, 10)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 11)%></TD>
        <TD align="center" valign="middle"><%=beanNCList.getNCList().getCell(i, 12)%></TD>
        <TD align="center" valign="middle"><%=beanNCList.getNCList().getCell(i, 13)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 14)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 15)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 16)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 17)%></TD>
        <TD valign="middle"><%=beanNCList.getNCList().getCell(i, 0)%></TD>
    </TR><%
    }
%>
</TABLE>
</FORM>
</BODY>
</HTML>