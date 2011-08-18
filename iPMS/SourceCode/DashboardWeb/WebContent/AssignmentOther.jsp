<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    AssignmentOtherBean beanAssignmentOther
            = (AssignmentOtherBean)request.getAttribute("beanAssignmentOther");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<TITLE>Other Assignment</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<LINK href="styles/pcal.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
function doAdd() {
    var form = document.frmOtherAssignment;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "AddOtherAssignment";
    form.action = "DashboardServlet";
    form.submit();
}

function doDelete() {
    var form = document.frmOtherAssignment;
    if (checkValid()) {
        if (confirm("Do you want to delete selected records, continue?")) {
            form.hidAction.value = "PM";
            form.hidActionDetail.value = "DeleteOtherAssignment";
            form.action = "DashboardServlet";
            form.submit();
        }
    }
}

function doFilter() {
    var form = document.frmOtherAssignment;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ListOtherAssignment";
    form.action = "DashboardServlet";
    form.submit();
}

function checkValid() {
    var flag = true;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == "dev_id" && e.type == "checkbox") {
            if (e.checked == 1) {
                flag = false;
            }
        }
    }
    if (flag) {
        alert("Please select assignment to do this action!");
        return false;
    }
    return true;
}

function callcheckdate() {
    str = document.forms[0].from.value;
    if (!isDate(str)) {
        alert("Invalid date format!");
        document.forms[0].from.focus();
        return (false);
    }
    return true;
}

function callcheckdate2() {
    str = document.forms[0].to.value;
    if (!isDate(str)) {
        alert("Invalid date format!");
        document.forms[0].to.focus();
        return (false);
    }
    return true;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF"><%
    String strTitle = "Other Assignment";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmOtherAssignment">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<CENTER>
<TABLE border="0" width="80%">
    <TR>
        <TD width="100%" align="right">&nbsp; From&nbsp;
            <INPUT type="text" name="from" size="8" value="<%= beanAssignmentOther.getFrom()%>" maxlength="8" onchange="return callcheckdate()">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].from, "dd/mm/yy",null,1,-1,-1,true)'>
            &nbsp;&nbsp;To&nbsp;
            <INPUT type="text" name="to" size="8" value="<%= beanAssignmentOther.getTo()%>" maxlength="8" onchange="return callcheckdate2()">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].to, "dd/mm/yy",null,1,-1,-1,true)'><%
    String[] arrGroup = beanAssignmentOther.getGroup();
    String strGroup = beanAssignmentOther.getSelectGroup();
    String strDisabled = "disabled";
    if (beanUserInfo.getRole().equals("2")) {
        strDisabled = "";
    }
%>
        &nbsp;&nbsp;In&nbsp;<SELECT name="cboGroup" <%=strDisabled%>>
            <OPTION value="All">All groups</OPTION><%
    for (int i = 0; i < arrGroup.length; i++) {
        if (strGroup.equals(arrGroup[i])) {
%>
            <OPTION value="<%=arrGroup[i]%>" selected><%=arrGroup[i]%></OPTION><%
        }
        else {
%>
            <OPTION value="<%=arrGroup[i]%>"><%=arrGroup[i]%></OPTION><%
        }
    }
%>
        </SELECT> &nbsp;&nbsp;<INPUT type="button" name="DoOtherAssignFilter" onclick="javascript:doFilter()" value="View">
        <INPUT type="hidden" name="hidSelectedGroup" value="<%=strGroup%>"><!-- don't delete --></TD>
    </TR>
</TABLE>
<TABLE border="0" width="80%" cellspacing="1" cellpadding="0">
    <TR class="table_header">
        <TD width="10%" height="25">D</TD>
        <TD width="25%" height="25">Developer</TD>
        <TD width="12%" height="25">Start date</TD>
        <TD width="12%" height="25">End date</TD>
        <TD width="12%" height="25">Type</TD>
        <TD width="10%" height="25">Usage (%)</TD>
        <TD width="19%" height="25">Description</TD>
    </TR><%
    for (int i = 0; i < beanAssignmentOther.getAssignList().getNumberOfRows(); i++) {
%>
    <TR class="row<%=i % 2 + 1%>">
        <TD width="10%" align="center"><INPUT type="checkbox" name="dev_id" value="<%=beanAssignmentOther.getAssignList().getCell(i, 0)%>"></TD>
        <TD width="25%">&nbsp;<%=beanAssignmentOther.getAssignList().getCell(i, 1)%></TD>
        <TD width="12%" align="center"><%
        String tmpStr = "";
        if (beanAssignmentOther.getAssignList().getCell(i, 2) != null) {
            tmpStr = beanAssignmentOther.getAssignList().getCell(i, 2);
        }
        else {
            tmpStr = "";
        }
        out.write(tmpStr);
%>
        </TD>
        <TD width="12%" align="center"><%
        if (beanAssignmentOther.getAssignList().getCell(i, 3) != null) {
            tmpStr = beanAssignmentOther.getAssignList().getCell(i, 3);
        }
        else {
            tmpStr="";
        }
        out.write(tmpStr);
%>
        </TD>
        <TD width="12%" align="center"><%=beanAssignmentOther.getAssignList().getCell(i, 4)%></TD>
        <TD width="10%" align="center"><%=beanAssignmentOther.getAssignList().getCell(i, 5)%></TD>
        <TD width="19%" align="center"><%=beanAssignmentOther.getAssignList().getCell(i, 6)%></TD>
    </TR><%
    }
%>
</TABLE>
<BR>
<TABLE border="0" width="30%">
    <TR>
        <TD width="25%" align="center"><INPUT type="button" name="DoAddOtherAssign" onclick="javascript:doAdd()" value="Add"<%=(("1".equals(beanUserInfo.getSRole().substring(4, 5)) || "1".equals(beanUserInfo.getSRole().substring(2, 3)))? "" : " disabled")%>>
        <INPUT type="button" name="DoDeleteOtherAssign" onclick="javascript:doDelete()" value="Delete"<%=(("1".equals(beanUserInfo.getSRole().substring(4, 5))|| "1".equals(beanUserInfo.getSRole().substring(2, 3))) ? "" : " disabled")%>></TD>
    </TR>
</TABLE>
</CENTER>
</FORM>
</BODY>
</HTML>