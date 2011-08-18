<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ResourceManagement.*,
            fpt.dashboard.util.StringUtil.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    AssignmentDetailBean beanAssignmentDetail
            = (AssignmentDetailBean)request.getAttribute("beanAssignmentDetail");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Assignment Detail</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY bgcolor="#FFFFFF"><%
    String strTitle = "Assignment Detail";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmAssignmentDetail">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<CENTER><%
    if (beanAssignmentDetail.getSMAssign().getNumberOfRows() > 0) {
%>
<TABLE border="0" cellspacing="1" width="70%">
    <COL width="45%">
    <COL width="20%">
    <COL width="20%">
    <COL width="15%">
    <TR>
        <TD colspan="3"><FONT face="Verdana" size="1">Developer : <%=beanAssignmentDetail.getDeveloperName()%></FONT></TD>
    </TR>
    <TR class="table_header" align="center">
        <TD><B><FONT face="Verdana" size="1" color="white">Project Name</FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white">Start Date</FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white">End Date</FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white">Usage(%)</FONT></B></TD>
    </TR><%
        for (int i = 0; i < beanAssignmentDetail.getSMAssign().getNumberOfRows(); i++) {
%>
    <TR class="row<%=(i + 1) % 2 + 1%>">
        <TD><%=beanAssignmentDetail.getSMAssign().getCell(i, 0)%></TD>
        <TD align="center"><%=beanAssignmentDetail.getSMAssign().getCell(i, 1)%></TD>
        <TD align="center"><%=beanAssignmentDetail.getSMAssign().getCell(i, 2)%></TD>
        <TD align="center"><%=beanAssignmentDetail.getSMAssign().getCell(i, 3)%></TD>
    </TR><%
        }
%>
</TABLE><%
    }
    else {
%>
<FONT face="Verdana" size="1">No assignment found</FONT><%
    }
%>
<BR>
<INPUT type="button" name="DoBackFromListAssignment" onclick="javascript:history.go(-1)" value="Back">
</CENTER>
</FORM>
</BODY>
</HTML>