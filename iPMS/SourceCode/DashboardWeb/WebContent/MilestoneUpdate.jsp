<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    MilestoneUpdateBean beanMilestoneUpdate
            = (MilestoneUpdateBean)request.getAttribute("beanMilestoneUpdate");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/util.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<TITLE>Update Milestone</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/StyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT language="javascript">
function doSave() {
    if (!checkValues()) {
        return;
    }
    var form = document.frmMilestoneUpdate;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "SaveUpdateMilestone";
    form.action = "DashboardServlet";
    form.submit();
}

function doBack() {
    var form = document.frmMilestoneUpdate;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ListMilestone";
    form.action = "DashboardServlet";
    form.submit();
}

function checkValues() {
    var form = document.forms[0];

    //MILESTONE NAME
    var value = trimSpaces(form.txtName.value);
    if (value == "") {
        alert("Please enter Milestone name!");
        form.txtName.focus();
        return false;
    }
    //BASE FINISH DATE
    value = trimSpaces(form.txtBaseFinishDate.value);
    if (value == "") {
        alert("You must enter Base Finish Date");
        form.txtBaseFinishDate.focus();
        return false;
    }
    if (!isDate(value)) {
        alert("Base Finish Date format is invalid!");
        form.txtBaseFinishDate.focus();
        return false;
    }
    if (CompareDate(value, form.hidPrStartDate.value) == "-1") {
        alert("Milestone Base Finish Date must be greater than Project Start Date!");
        form.txtBaseFinishDate.focus();
        return false;
    }
    if (isDate(form.hidPrBaseFinishDate.value)) {
        if (CompareDate(value, form.hidPrBaseFinishDate.value) == "1") {
            alert("Milestone Base Finish Date must less than Project Base Finish Date!");
            form.txtBaseFinishDate.focus();
            return false;
        }
    }
    //PLAN FINISH DATE
    value = trimSpaces(form.txtPlanFinishDate.value);
    if (value != "") {
        if (!isDate(value)) {
            alert("Plan Finish Date format is invalid!");
            form.txtPlanFinishDate.focus();
            return false;
        }
        if (CompareDate(value, form.hidPrStartDate.value) == "-1") {
            alert("Milestone plan finish date must be greater than Project start date!");
            form.txtPlanFinishDate.focus();
            return false;
        }
        if (isDate(form.hidPrPlanFinishDate.value)) {
            if (CompareDate(value, form.hidPrPlanFinishDate.value) == "1") {
                alert("Milestone plan finish date must be less than Project plan finish date");
                form.txtPlanFinishDate.focus();
                return false;
            }
        }
    }
    //ACTUAL FINISH DATA
    value = trimSpaces(form.txtActualFinishDate.value);
    if (value != "") {
        if (!isDate(value)) {
            alert("Actual Finish Date format is invalid!");
            form.txtActualFinishDate.focus();
            return false;
        }
        if (CompareDate(value, form.hidPrStartDate.value) == "-1") {
            alert("Milestone actual finish date must be greater than Project start date!");
            form.txtActualFinishDate.focus();
            return false;
        }
        if (isDate(form.hidPrActualFinishDate.value)) {
            if (CompareDate(value, form.hidPrActualFinishDate.value) == "1") {
                alert("Milestone actual finish date must be less than Project actual finish date");
                form.txtPlanFinishDate.focus();
                return false;
            }
        }
        form.cmbComplete.value = 1;
    }
    else {
        if(form.cmbComplete.value == 1) {
            alert("If milestone is completed, actual finish date must be filled");
            form.txtActualFinishDate.focus();
            return false;
        }
    }
    //DESCRIPTION
    if (form.txtDescription.value.length > 400) {
        alert("Description field must be less than 400 characters!");
        form.txtDescription.focus();
        return false;
    }
    return true;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF"><%
    String strTitle = "Update Milestone";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmMilestoneUpdate">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="MilestoneID" value="<%=request.getParameter("hidMilestoneID")%>">
<INPUT type="hidden" name="hidProjectID" value="<%=request.getParameter("hidProjectID")%>">
<INPUT type="hidden" name="hidProjectCode" value="<%=request.getParameter("hidProjectCode")%>">
<INPUT type="hidden" name="hidProjectName" value="<%=request.getParameter("hidProjectName")%>">
<INPUT type="hidden" name="hidPrStartDate" value="<%= request.getParameter("hidPrStartDate")%>">
<INPUT type="hidden" name="hidPrBaseFinishDate" value="<%=request.getParameter("hidPrBaseFinishDate")%>">
<INPUT type="hidden" name="hidPrPlanFinishDate" value="<%=request.getParameter("hidPrPlanFinishDate")%>">
<INPUT type="hidden" name="hidPrActualFinishDate" value="<%=request.getParameter("hidPrActualFinishDate")%>">
<CENTER>
<TABLE border="0" width="90%" align="center">
    <TR>
        <TD colspan="6"><B>Project infomation</B></TD>
    </TR>
    <TR>
        <TD colspan="6">&nbsp;</TD>
    </TR>
    <TR>
        <TD width="30%" align="left"><B><FONT face="Verdana" size="1" color="#000042">Code</FONT></B>&nbsp;&nbsp;&nbsp;<%=request.getParameter("hidProjectCode")%></TD>
        <TD width="30%" align="left"><B><FONT face="Verdana" size="1" color="#000042">Name</FONT></B>&nbsp;&nbsp;&nbsp;<%=request.getParameter("hidProjectName")%></TD>
        <TD width="30%" align="left"><B><FONT face="Verdana" size="1" color="#000042">Start date</FONT></B>&nbsp;&nbsp;&nbsp;<%=request.getParameter("hidPrStartDate")%></TD>
    </TR>
    <TR>
        <TD width="30%" align="left"><B><FONT face="Verdana" size="1" color="#000042">Planned finish date</FONT></B>&nbsp;&nbsp;&nbsp;<%=request.getParameter("hidPrBaseFinishDate")%></TD>
        <TD width="30%" align="left"><B><FONT face="Verdana" size="1" color="#000042">Replanned finish date</FONT></B>&nbsp;&nbsp;&nbsp;<%=request.getParameter("hidPrPlanFinishDate")%></TD>
        <TD width="30%" align="left"><B><FONT face="Verdana" size="1" color="#000042">Actual finish date</FONT></B>&nbsp;&nbsp;&nbsp;<%=request.getParameter("hidPrActualFinishDate")%></TD>
    </TR>
</TABLE>
<HR color="#000000" noshade size="1" width="90%" align="center">
<BR>
<TABLE border="0" width="75%">
    <TR>
        <TD><B><FONT face="Verdana" size="1" color="#000042">Major Milestones</FONT><FONT color="red">*</FONT></B></TD>
        <TD><INPUT type="text" name="txtName" value="<%=beanMilestoneUpdate.getName()%>" maxlength="29"></TD>
        <TD colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
    </TR>
    <TR>
        <TD><B><FONT face="Verdana" size="1" color="#000042">Complete</FONT></B><FONT color="red">*</FONT></TD>
        <TD><SELECT name="cmbComplete"><%
    if (beanMilestoneUpdate.getComplete() == 0) {
%>
            <OPTION value="0" selected>No</OPTION>
            <OPTION value="1">Yes</OPTION><%
    }
    else {
%>
            <OPTION value="0">No</OPTION>
            <OPTION value="1" selected>Yes</OPTION><%
    }
%>
        </SELECT></TD>
        <TD colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
    </TR>
    <TR>
        <TD><B><FONT face="Verdana" size="1" color="#000042">Planned Finish</FONT><FONT color="red">*</FONT></B></TD>
        <TD>
            <INPUT type="text" value="<%=beanMilestoneUpdate.getBaseFinishDate()%>" name="txtBaseFinishDate">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].txtBaseFinishDate, "dd/mm/yy",null,1,-1,-1,true)'>
            (dd/mm/yy)
        </TD>
        <TD colspan="3"></TD>
    </TR>
    <TR>
        <TD><B><FONT face="Verdana" size="1" color="#000042">Replanned Finish</FONT></B></TD>
        <TD>
            <INPUT type="text" value="<%=beanMilestoneUpdate.getPlanFinishDate()%>" name="txtPlanFinishDate">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].txtPlanFinishDate, "dd/mm/yy",null,1,-1,-1,true)'>
            (dd/mm/yy)
        </TD>
        <TD colspan="3"></TD>
    </TR>
    <TR>
        <TD><B><FONT face="Verdana" size="1" color="#000042">Actual Finish</FONT></B></TD>
        <TD>
            <INPUT type="text" value="<%=beanMilestoneUpdate.getActualFinishDate()%>" name="txtActualFinishDate">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].txtActualFinishDate, "dd/mm/yy",null,1,-1,-1,true)'>
            (dd/mm/yy)
        </TD>
        <TD colspan="3"></TD>
    </TR>
    <TR>
        <TD><B><FONT face="Verdana" size="1" color="#000042">Description</FONT></B></TD>
        <TD colspan="3"><TEXTAREA cols="30" rows="3" name="txtDescription"><%=beanMilestoneUpdate.getDescription()%></TEXTAREA></TD>
    </TR>
</TABLE>
</CENTER>
<P align="center"><INPUT type="button" name="DoSaveUpdateMileStone" onclick="javascript:doSave()" value="Save">&nbsp;&nbsp;&nbsp;&nbsp;
<INPUT type="button" name="DoBackFromViewMileStone" onclick="javascript:doBack()" value="Back"></P>
</FORM>
</BODY>
</HTML>