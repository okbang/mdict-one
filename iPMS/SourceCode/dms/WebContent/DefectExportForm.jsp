<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
	<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%>
	<%@ page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.DefectManagement.*, fpt.dms.framework.util.StringUtil.*" %>
    <%@ page buffer = "1024kb" %>
    <%@ page isThreadSafe="false" errorPage="error.jsp" %>
    <% DefectListingBean beanDefectList = (DefectListingBean)request.getAttribute("beanDefectList"); %>
<HTML>
<HEAD>
<TITLE>DefectExportForm.jsp</TITLE>
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
<FORM method="POST" action="DMSServlet" name="frmExportDefect">
<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
    <COLGROUP>
        <COL align="center" valign="middle" width="3%">
        <COL align="center" valign="middle" width="3%">
        <COL align="left" valign="middle" width="35%">
        <COL align="left" valign="middle" width="35%">
        <COL align="left" valign="middle" width="10%">
        <COL align="left" valign="middle" width="13%">
        <COL align="left" valign="middle" width="10%">
        <COL align="left" valign="middle" width="6%">
        <COL align="left" valign="middle" width="8%">
        <COL align="right" valign="middle" width="8%">
        <COL align="right" valign="middle" width="8%">
        <COL align="right" valign="middle" width="8%">
        <COL align="left" valign="middle" width="20%">
        <COL align="left" valign="middle" width="10%">
        <COL align="left" valign="middle" width="8%">
        <COL align="left" valign="middle" width="6%">
        <COL align="left" valign="middle" width="10%">
        <COL align="left" valign="middle" width="6%">
        <COL align="left" valign="middle" width="8%">
        <COL align="left" valign="middle" width="8%">
        <COL align="left" valign="middle" width="10%">
        <COL align="left" valign="middle" width="10%">
        <COL align="left" valign="middle" width="10%">
        <COL align="left" valign="middle" width="20%">
        <COL align="left" valign="middle" width="8%">
        <COL align="right" valign="middle" width="25%">
        <COL align="left" valign="middle" width="25%">
    <TR class="header">
        <TD align="center">#</TD>
        <TD align="center">DefectID</TD>
        <TD align="center">Title</TD>
        <TD align="center">Test Case ID</TD>
        <TD align="center">Severity</TD>
        <TD align="center">Priority</TD>
        <TD align="center">Status</TD>
        <TD align="center">Defect Owner</TD>
        <TD align="center">Assign To</TD>
        <TD align="center">Created By</TD>
        <TD align="center">Create Date</TD>
        <TD align="center">Fixed Date</TD>
        <TD align="center">Due Date</TD>
        <TD align="center">Description</TD>
        <TD align="center">Project Origin</TD>
        <TD align="center">Reference</TD>
        <TD align="center">QC Activity</TD>
        <TD align="center">Defect Origin</TD>
        <TD align="center">Activity Type</TD>
        <TD align="center">Stage Injected</TD>
        <TD align="center">Stage Detected</TD>
        <TD align="center">Work Product</TD>
        <TD align="center">Module</TD>
        <TD align="center">Defect Type</TD>
        <TD align="center">Corrective Action</TD>
        <TD align="center">Closed Date</TD>
        <TD align="center">Cause Analysis</TD>        
    </TR>
    <%
    	for(int i = 0; i < beanDefectList.getDefectList().getNumberOfRows(); i++) {
	%>
    <TR class="Row<%=(i + 1) % 2 + 1%>">
        <TD><%=i + 1%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 0)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 1)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 24)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 2)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 3)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 4)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 25)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 5)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 7)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 9)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 8)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 6)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 10)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 11)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 12)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 13)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 14)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 15)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 16)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 17)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 18)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 19)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 20)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 21)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 22)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 23)%></TD>        
    </TR><%
    }
%>
</TABLE>
</FORM>
</BODY>
</HTML>