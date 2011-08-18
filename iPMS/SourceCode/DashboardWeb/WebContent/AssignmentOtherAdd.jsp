<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    AssignmentAddBean beanAssignmentAdd
            = (AssignmentAddBean)request.getAttribute("beanAssignmentAdd");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<TITLE>Add Other Assignment</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<LINK href="styles/pcal.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
function doSave() {
    var form = document.frmOtherAssignmentAdd;
    if (doCheck()) {
        form.hidAction.value = "PM";
        form.hidActionDetail.value = "SaveNewOtherAssignment";
        form.action = "DashboardServlet";
        form.submit();
    }
}

function doBack() {
    var form = document.frmOtherAssignmentAdd;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ListOtherAssignment";
    form.action = "DashboardServlet";
    form.submit();
}

function doCheck() {
    var form = document.forms[0];
    if (!isDate((form.From_date.value))) {
        alert("Invalid date format");
        form.From_date.focus();
        return (false);
    }
    if (!isDate((form.To_date.value))) {
        alert("Invalid date format");
        form.To_date.focus();
        return (false);
    }
    if (CompareDate((form.From_date.value), (form.To_date.value)) == 1) {
        alert("From date must less than or equals To Date");
        form.From_date.focus();
        return (false);
    }
    if (!isPositiveInteger(form.Usage.value)) {
        alert("Invalid number");
        form.Usage.focus();
        return false;
    }
    else {
       if (parseInt(form.Usage.value) > 100) {
            alert("This number must be between 0 and 100");
            form.Usage.focus();
            return false;
       }
    }
    if (form.Description.value.length > 30) {
        alert("Length of description must less than 30");
        form.Description.focus();
        return (false);
    }
    return (true);
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<%
    String strTitle = "Add Other Assignment";
%>
<%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet"
    name="frmOtherAssignmentAdd"><INPUT type="hidden" name="hidAction"
    value=""> <INPUT type="hidden" name="hidActionDetail" value="">
<CENTER>
<TABLE width="70%" border="0" cellpadding="1" cellspacing="1">
    <TR>
        <TD colspan="2"><%
    if (!"".equals(beanAssignmentAdd.getMessage())) {
        out.write("<B><FONT color=red>" + beanAssignmentAdd.getMessage() + "</FONT></B>");
    }
    beanAssignmentAdd.setMessage("");
%>
        </TD>
    </TR>
    <TR>
        <TD width="30%" height="23">
        <P align="left"><FONT face="Verdana" size="1" color="#000042">Developer</FONT><FONT
            color="red">*</FONT>
        </TD>
        <TD width="70%" height="23"><SELECT size="1" name="Developer_ID"
            class="medcombo">
            <%
    for (int i = 0; i < beanAssignmentAdd.getDevList().getNumberOfRows(); i++) {
%>
            <OPTION value="<%=beanAssignmentAdd.getDevList().getCell(i, 0)%>"><%=beanAssignmentAdd.getDevList().getCell(i, 1)%><%
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD width="30%" height="23">
        <P align="left"><FONT face="Verdana" size="1" color="#000042">From&nbsp;</FONT><FONT
            color="red">*</FONT>
        </TD>
        <TD width="70%" height="23">
        <P align="left">
            <INPUT type="text" name="From_date" size="15" maxlength="8">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].From_date, "dd/mm/yy",null,1,-1,-1,true)'>
            (dd/mm/yy)
        </P>
        </TD>
    </TR>
    <TR>
        <TD width="30%" height="23"><FONT face="Verdana" size="1"
            color="#000042">To&nbsp;</FONT><FONT color="red">*</FONT></TD>
        <TD width="70%" height="23">
        <P align="left">
            <INPUT type="text" name="To_date" size="15" maxlength="8">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].To_date, "dd/mm/yy",null,1,-1,-1,true)'>
            (dd/mm/yy)
        </P>
        </TD>
    </TR>
    <TR>
        <TD width="30%" height="23"><FONT face="Verdana" size="1"
            color="#000042">Type</FONT><FONT color="red">*</FONT></TD>
        <TD width="70%" height="23">
        <P align="left"><SELECT size="1" name="Type" class="medcombo">
            <OPTION value="1">Onsite</OPTION>
            <OPTION value="2">Allocated</OPTION>
            <OPTION value="3">Tentative</OPTION>
            <OPTION value="4">Training</OPTION>
            <OPTION value="5">Off</OPTION>
        </SELECT>
        </TD>
    </TR>
    <TR>
        <TD width="30%" height="23"><FONT face="Verdana" size="1"
            color="#000042">Usage</FONT><FONT color="red">*</FONT></TD>
        <TD width="70%" height="23">
        <P align="left"><INPUT type="text" name="Usage" size="3" maxlength="3"
            value="100">(%)
        </TD>
    </TR>
    <TR>
        <TD width="30%" height="18"><FONT face="Verdana" size="1"
            color="#000042">Description</FONT></TD>
        <TD width="70%" height="18">
        <P><TEXTAREA rows="3" name="Description" cols="50"></TEXTAREA></P>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" width="40%">
    <TR>
        <TD width="50%" align="center"><INPUT type="button"
            name="DoSaveNewOtherAssignment" onclick="javascript:doSave()"
            value="Save"> <INPUT type="button"
            name="DoBackFromNewOtherAssignment" onclick="javascript:doBack()"
            value="Back"></TD>
    </TR>
</TABLE>
</CENTER>
</FORM>
</BODY>
</HTML>
