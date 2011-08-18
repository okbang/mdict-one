<%@ page contentType="application/vnd.ms-excel;charset=UTF-8" %><%@
    page language = "java"
    import = "java.util.*, javax.servlet.*,
              fpt.ncms.constant.NCMS,
              fpt.ncms.bean.*,
              fpt.ncms.util.StringUtil.*" %><%@
    page buffer = "1024kb"%><%@
    page isThreadSafe = "false" errorPage = "error.jsp" %><%
    NCListBean beanNCList = (NCListBean)session.getAttribute("beanNCList");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
<FORM method="POST" action="NcmsServlet" name="frmExportNC">
<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
    <TR class="header">
        <TD align="center" valign="middle" width="3%">#</TD>
        <TD align="center" valign="middle" width="8%">Code</TD>
        <TD align="center" valign="middle" width="8%">Project Code</TD>
        <TD align="center" valign="middle" width="8%">Group Name</TD>
        <TD align="center" valign="middle" width="8%">Assignee</TD>
        <TD align="center" valign="middle" width="8%">NC Level</TD>
        <TD align="center" valign="middle" width="8%">NC Type</TD>
        <TD align="center" valign="middle" width="12%">Detected By</TD>
        <TD align="center" valign="middle" width="8%">Creator</TD>
        <TD align="center" valign="middle" width="8%">Creation Date</TD>
        <TD align="center" valign="middle" width="12%">Process</TD>
        <TD align="center" valign="middle" width="8%">ISO Clause</TD>
        <TD align="center" valign="middle" width="20%">Description</TD>
        <TD align="center" valign="middle" width="15%">Cause</TD>
        <TD align="center" valign="middle" width="12%">Type Of Cause</TD>
        <TD align="center" valign="middle" width="15%">Impact</TD>
        <TD align="center" valign="middle" width="15%">CP Action</TD>
        <TD align="center" valign="middle" width="8%">Type Of Action</TD>
        <TD align="center" valign="middle" width="8%">Deadline</TD>
        <TD align="center" valign="middle" width="8%">Closure Date</TD>
        <TD align="center" valign="middle" width="8%">Repeat</TD>
        <TD align="center" valign="middle" width="8%">Status</TD>
        <TD align="center" valign="middle" width="30%">Note</TD>
        <TD align="center" valign="middle" width="10%">KPA</TD>
        <TD align="center" valign="middle" width="10%">Reviewer</TD>
    </TR><%
    for (int i = 0; i < beanNCList.getNCList().getNumberOfRows(); i++) {
%>
    <TR>
        <TD align="left" valign="middle" width="3%"><%=i + 1%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 1)%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 2)%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 3)%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 4)%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 5)%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 6)%></TD>
        <TD align="left" valign="middle" width="12%"><%=beanNCList.getNCList().getCell(i, 7)%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 8)%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 9)%></TD>
        <TD align="left" valign="middle" width="12%"><%=beanNCList.getNCList().getCell(i, 10)%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 11)%></TD>
        <TD align="left" valign="middle" width="20%"><%=beanNCList.getNCList().getCell(i, 12)%></TD>
        <TD align="left" valign="middle" width="15%"><%=beanNCList.getNCList().getCell(i, 13)%></TD>
        <TD align="left" valign="middle" width="12%"><%=beanNCList.getNCList().getCell(i, 14)%></TD>
        <TD align="left" valign="middle" width="15%"><%=beanNCList.getNCList().getCell(i, 15)%></TD>
        <TD align="left" valign="middle" width="15%"><%=beanNCList.getNCList().getCell(i, 16)%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 17)%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 18)%></TD>
        <TD align="left" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 19)%></TD>
        <TD align="left" valign="middle" width="8%"><%
        if (NCMS.NC_REPEAT.equals(beanNCList.getNCList().getCell(i, 20))) {
            out.write("Yes");
        }
        else if (NCMS.NC_NONREPEAT.equals(beanNCList.getNCList().getCell(i, 20))) {
            out.write("No");
        }
        %></TD>
        <TD align="center" valign="middle" width="8%"><%=beanNCList.getNCList().getCell(i, 0)%></TD>
        <TD align="center" valign="middle" width="30%"><%=beanNCList.getNCList().getCell(i, 21)%></TD>
        <TD align="center" valign="middle" width="10%"><%=beanNCList.getNCList().getCell(i, 22)%></TD>
        <TD align="center" valign="middle" width="10%"><%=beanNCList.getNCList().getCell(i, 23)%></TD>
    </TR><%
    }
%>
</TABLE>
</FORM>
</BODY>
</HTML>