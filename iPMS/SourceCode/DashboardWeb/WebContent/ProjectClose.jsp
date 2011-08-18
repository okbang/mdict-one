<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*, fpt.dashboard.constant.DATA" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    ProjectCloseBean beanProjectClose
            = (ProjectCloseBean)request.getAttribute("beanProjectClose");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/util.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<TITLE>Close/Cancel Project</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/StyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT language="javascript">
function doSave() {
    var form = document.frmProjectClose;
    if (checkData()) {
        form.hidAction.value = "PM";
        form.hidActionDetail.value = "SaveCloseProject";
        form.action = "DashboardServlet";
        form.submit();
    }
}

function doBack() {
    var form = document.frmProjectClose;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewProjectDetail";
    form.action = "DashboardServlet";
    form.submit();
}

function checkData() {
    var ErrorMsg = "E R R O R (s) :  \n\n";
    var ErrorFound = false;
    var form = document.forms[0];
    var _actualFinishDate = trim(form.actualFinish.value);
    var _status = form.status.value;
    if (_status == 0) {
        alert("Select close value please!");
        form.status.focus();
        return false;
    }
    if (_actualFinishDate.length > 0) {
        if (!(isDate(_actualFinishDate))) {
            ErrorMsg = ErrorMsg + "- Invalid Actual Finish Date value.   \n";
            alert(ErrorMsg);
            form.actualFinish.focus();
            return false;
        }
    }
    else {
        alert("Invalid Actual Finish Date!");
        form.actualFinish.focus();
        return false;
    }
    return true;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="center"><!-- Page Header Title Information --> <%!
    String strTitle = "Close/Cancel Project";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmProjectClose">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidProjectID" value="<%=beanProjectClose.getProjectID()%>">
<TABLE width="60%" border="0" cellpadding="3" cellspacing="3">
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" width="40%" height="20"><B>Status</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD width="60%" height="20"><SELECT name="status" size="1" style="width: 100">
            <OPTION value="<%=DATA.PROJECT_VALUE_STATUS_CLOSED%>" selected><%=DATA.PROJECT_STATUS_CLOSED%></OPTION>
            <OPTION value="<%=DATA.PROJECT_VALUE_STATUS_CANCELLED%>"><%=DATA.PROJECT_STATUS_CANCELLED%></OPTION>
        </SELECT></TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" width="40%" height="20"><B>Actual finish date</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD width="60%" height="20">
            <INPUT name="actualFinish" size="10" value="" maxlength="8">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].actualFinish, "dd/mm/yy",null,1,-1,-1,true)'>
            &nbsp;(dd/mm/yy)
        </TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" width="40%" height="20"><B>Description</B></TD>
        <TD width="60%" height="20"><TEXTAREA rows="8" cols="60" name="desc"><%=beanProjectClose.getDescription()%></TEXTAREA></TD>
    </TR>
</TABLE>
<P><INPUT type="button" name="DoSaveCloseProject" onclick="javascript:doSave()" value="Save">
<INPUT type="button" name="DoBackFromCloseProject" onclick="javascript:doBack()" value="Back"></P>
</FORM>
<BR>
<BR>
</DIV>
</BODY>
</HTML>