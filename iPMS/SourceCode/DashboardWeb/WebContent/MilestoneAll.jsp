<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    MilestoneAllBean beanMilestoneAll
            = (MilestoneAllBean)request.getAttribute("beanMilestoneAll");
    String strTitle = "";
    if ("1".equals(beanMilestoneAll.getStatus())) {
        strTitle = "Completed Milestone";
    }
    else {
        if ("0".equals(beanMilestoneAll.getStatus())) {
            strTitle = "Incompleted Milestone";
        }
        else {
            strTitle = "Milestone";
        }
    }
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/util.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<TITLE><%=strTitle%></TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<LINK href="styles/pcal.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
function doFilter() {
    if (!isValidForm()) {
        return;
    }
    var form = document.frmMilestoneAll;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ListAllMilestone";
    form.action = "DashboardServlet";
    form.submit();
}

function doViewProjectDetail(id) {
    var form = document.frmMilestoneAll;
    form.hidProjectID.value = id;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewProjectDetail";
    form.action = "DashboardServlet";
    form.submit();
}

function isValidForm () {
    var form = document.frmMilestoneAll;
    var objDateFrom = form.txtDateFrom;
    var objDateTo = form.txtDateTo;
    
    if (objDateFrom.value.length > 0) {
        if (!isDate(objDateFrom.value)) {
            alert("Date format is not correct!");
            objDateFrom.focus();
            return false;
        }
    }
    if (objDateTo.value.length > 0) {
        if (!isDate(objDateTo.value)) {
            alert("Date format is not correct!");
            objDateTo.focus();
            return false;
        }
    }
    if (CompareDate(objDateFrom.value, objDateTo.value) > 0) {
        alert("To date must greater than from date");
        objDateFrom.focus();
        return false;
    }
    return true;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF"><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmMilestoneAll">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidProjectID" value="">
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0">
    <TR>
        <TD valign="middle" width="8%" height="25" align="right">
        <FONT color="blue">(Date&nbsp;format:&nbsp;dd/mm/yy)</FONT>&nbsp;&nbsp;&nbsp;&nbsp;From&nbsp;
        <INPUT type="text" name="txtDateFrom" size="8" value="<%=beanMilestoneAll.getDateFrom()%>" maxlength="8">
        <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].txtDateFrom, document.forms[0].txtDateFrom, "dd/mm/yy",null,1,-1,-1,true)'>
        &nbsp;&nbsp;To&nbsp;
        <INPUT type="text" name="txtDateTo" size="8" value="<%=beanMilestoneAll.getDateTo()%>" maxlength="8">
        <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].txtDateTo, document.forms[0].txtDateTo, "dd/mm/yy",null,1,-1,-1,true)'>
        &nbsp;&nbsp;Milestone&nbsp; <SELECT name="cboStatus"><%
    if ("1".equals(beanMilestoneAll.getStatus())) {
%>
            <OPTION value="1" selected>Completed</OPTION><%
    }
    else {
%>
            <OPTION value="1">Completed</OPTION><%
    }
    if ("0".equals(beanMilestoneAll.getStatus())) {
%>
            <OPTION value="0" selected>Incompleted</OPTION><%
    }
    else {
%>
            <OPTION value="0">Incompleted</OPTION><%
    }
    if ("-1".equals(beanMilestoneAll.getStatus())) {
%>
            <OPTION value="-1" selected>All</OPTION><%
    }
    else {
%>
            <OPTION value="-1">All</OPTION><%
    }
%>
        </SELECT>&nbsp; &nbsp;
        <INPUT type="button" name="DoViewCompleteMilestone" value="View" onclick="javascript:doFilter()">
        </TD>
    </TR>
    <TR>
        <TD>
        <TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
            <TR height="20">
                <TD width="10%" bgcolor="#006699" height="25" align="center"><B><FONT face="Verdana" color="#ffffff">Project</FONT></B></TD>
                <TD width="25%" bgcolor="#006699" height="25" align="center"><B><FONT face="Verdana" color="#ffffff">Milestone</FONT></B></TD>
                <TD width="10%" bgcolor="#006699" height="25" align="center"><B><FONT face="Verdana" color="#ffffff">Planned Finish Date</FONT></B></TD>
                <TD width="10%" bgcolor="#006699" height="25" align="center"><B><FONT face="Verdana" color="#ffffff">Replanned Finish Date</FONT></B></TD>
                <TD width="5%" bgcolor="#006699" height="25" align="center"><B><FONT face="Verdana" color="#ffffff">Complete</FONT></B></TD>
                <TD width="35%" bgcolor="#006699" height="25" align="center"><B><FONT face="Verdana" color="#ffffff">Description</FONT></B></TD>
            </TR><%
    int nNumberOfRows = beanMilestoneAll.getMilestoneList().getNumberOfRows();
    if (nNumberOfRows > 0) {
        for(int i = 0; i < nNumberOfRows; i++) {
%>
            <TR class="Row<%=(i + 1) % 2 +1%>">
                <!-- PROJECT CODE  -->
                <TD><FONT face="Verdana" size="1"><%=beanMilestoneAll.getMilestoneList().getCell(i, 0)%></FONT></TD>
                <!-- MILESTONE NAME  -->
                <TD><A href="javascript:doViewProjectDetail('<%=beanMilestoneAll.getMilestoneList().getCell(i, 6)%>')"> <FONT face="Verdana" size="1"><%=beanMilestoneAll.getMilestoneList().getCell(i, 1)%></FONT></A></TD>
                <!-- Base Finish Date  -->
                <TD align="center"><FONT face="Verdana" size="1"><%=beanMilestoneAll.getMilestoneList().getCell(i, 2)%></FONT></TD>
                <!-- Plan Finish Date  -->
                <TD align="center"><FONT face="Verdana" size="1"><%=beanMilestoneAll.getMilestoneList().getCell(i, 3)%></FONT></TD>
                <!-- Description  -->
                <TD align="center"><FONT face="Verdana" size="1"><%="1".equals(beanMilestoneAll.getMilestoneList().getCell(i, 4)) ? "Yes" : "No"%></FONT></TD>
                <!-- Description  -->
                <TD><FONT face="Verdana" size="1"><%=beanMilestoneAll.getMilestoneList().getCell(i, 5)%></FONT></TD>
            </TR><%
        } //end for
    }  //end if
    else {
%>
            <TR>
                <TD colspan="14">No milestone found</TD>
            </TR><%
    } //end else
%>
        </TABLE>
        </TD>
    </TR>
</TABLE>
</FORM>
</BODY>
</HTML>