<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@
    page language="java" import="java.util.*, javax.servlet.*,java.util.Date,java.text.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.StaffManagement.*,
            fpt.dashboard.util.StringUtil.*,fpt.dashboard.util.DateUtil.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    StaffUpdateBean beanStaffUpdate
            = (StaffUpdateBean)request.getAttribute("beanStaffUpdate");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT language="javascript" src="scripts/popcalendar.js"></SCRIPT>
<TITLE>Update Staff</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
</HEAD>
<%
    String strTitle = "Update Staff";
%>
<%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmStaffUpdate">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidStaffID" value="<%=beanStaffUpdate.getDevID()%>">
<INPUT type="hidden" name="hidAccount" value="<%=beanStaffUpdate.getAccount()%>">
<CENTER>
<TABLE border="0" width="80%" cellpadding="5">
    <COLGROUP>
        <COL width="15%">
        <COL width="35%">
        <COL width="15%">
        <COL width="35%">
    <TR><%
    // Display error message
    if (request.getAttribute(fpt.dashboard.constant.DB.ATT_ERROR_MESSAGE) != null) {%>
        <FONT color="red"><B><%=request.getAttribute(fpt.dashboard.constant.DB.ATT_ERROR_MESSAGE)%></B></FONT><%
        request.removeAttribute(fpt.dashboard.constant.DB.ATT_ERROR_MESSAGE);
    }%>
    </TR>
    <TR>
        <TD align="right">Name<FONT color="red">*</FONT></TD>
        <TD><INPUT type="text" name="name" size="29" maxlength="29" value="<%=beanStaffUpdate.getName()%>"></TD>
        <TD align="right">Group<FONT color="red">*</FONT></TD>
        <TD>
<%
    String[] arrGroup = beanStaffUpdate.getArrGroup();
    String strGroup=beanStaffUpdate.getGroup();
%>
        <SELECT name="group" style="width: 140">
<%
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
        </SELECT></TD>
    </TR>
    <TR>
        <TD align="right">Account<FONT color="red">*</FONT></TD>
        <TD><INPUT type="text" name="acc" size="29" maxlength="29" value="<%=beanStaffUpdate.getAccount()%>"></TD>
        <TD align="right">Role<FONT color="red">*</FONT></TD>
        <TD>
<%
    String[] selected = new String[9];
    String role = beanStaffUpdate.getRole();

    selected[0] = "Developer".equals(role) ? "selected" : "";
    selected[1] = "Project Leader".equals(role) ? "selected" : "";
    selected[2] = "Group Leader".equals(role) ? "selected" : "";
    selected[4] = "PQA".equals(role) ? "selected" : "";
    selected[5] = "Manager".equals(role) ? "selected" : "";
    selected[6] = "External of project level".equals(role) ? "selected" : "";
    selected[7] = "External of group level".equals(role) ? "selected" : "";
    selected[8] = "Communicator".equals(role) ? "selected" : "";
%>
        <SELECT name="role" style="width: 140">
            <OPTION value="1000000000" <%=selected[0]%>>Developer</OPTION>
            <OPTION value="1100000000" <%=selected[1]%>>Project Leader</OPTION>
            <OPTION value="1110000000" <%=selected[2]%>>Group Leader</OPTION>
            <OPTION value="1000100000" <%=selected[4]%>>PQA/SQA</OPTION>
            <OPTION value="1111110000" <%=selected[5]%>>Manager</OPTION>
            <OPTION value="0000001000" <%=selected[6]%>>External of project level</OPTION>
            <OPTION value="0000001100" <%=selected[7]%>>External of group level</OPTION>
            <OPTION value="0000000010" <%=selected[8]%>>Communicator</OPTION>
        </SELECT></TD>
    </TR>
    <TR>
        <TD align="right">Status<FONT color="red">*</FONT></TD>
        <TD align="left" valign="middle"><SELECT name="status" onchange="changeStatus()" size="1" style="width: 140"><%
    for (int nRow = 0x00; nRow < beanStaffUpdate.getStatusList().getNumberOfRows(); nRow++) {
        String strText = beanStaffUpdate.getStatusList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanStaffUpdate.getStatus() != null) {
            out.write(beanStaffUpdate.getStatus().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">"
                + beanStaffUpdate.getStatusList().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD align="right">Email<font color="red">*</font></TD>
        <TD><INPUT type="text" name="email" size="29" maxlength="50" value="<%=beanStaffUpdate.getEmail() == null ? "" : beanStaffUpdate.getEmail()%>"></TD>
        <TD align="right">Position<FONT color="red">*</FONT></TD>
        <TD><INPUT type="text" name="design" size="29" maxlength="29" value="<%=beanStaffUpdate.getDesignation() == null ? "" : beanStaffUpdate.getDesignation()%>"></TD>
    </TR>
    <TR>
        <TD align="right">Start Date<font color="red">*</font></TD>
        <TD>
            <INPUT type="text" name="startDate" size="29" maxlength="8" value="<%=(beanStaffUpdate.getStartDate() == null) ? "" : beanStaffUpdate.getStartDate()%>">
            <IMG   src="images/cal.gif" style="CURSOR:hand"
                   onclick='showCalendar(document.forms[0].startDate, document.forms[0].startDate, "dd/mm/yy",null,1,-1,-1,true)'>&nbsp;(dd/mm/yy)
        </TD>
        <TD align="right">Quit Date</TD>
        <TD><INPUT type="text" name="quitDate" size="29" maxlength="8" value="<%=(beanStaffUpdate.getQuitDate() == null) ? "" : beanStaffUpdate.getQuitDate()%>">
            <IMG src="images/cal.gif" style="CURSOR:hand"
                   onclick='if (!document.forms[0].quitDate.disabled) showCalendar(document.forms[0].quitDate, document.forms[0].quitDate, "dd/mm/yy",null,1,-1,-1,true)'>&nbsp;(dd/mm/yy)
        </TD>
    </TR>
    <TR>
        <TD width="100%" colspan="5">
        <CENTER><BR>
        <INPUT type="button" name="DoSaveUpdateForm" onclick="javascript:doSave()" value="Save" <%
    if (!"1".equals(beanUserInfo.getSRole().substring(4, 5))) {
        out.print("disabled");
    }
%>>
        <INPUT type="button" name="DoBackFormUpdateDev" onclick="javascript:doBack();" value="Back"></CENTER>
        </TD>
    </TR>
</TABLE>
</CENTER>
</FORM>
<SCRIPT language="javascript">
var form = document.frmStaffUpdate;

function doSave() {
    if (formValidate()) {
        form.hidAction.value = "SM";
        form.hidActionDetail.value = "SaveUpdateStaff";
        form.action = "DashboardServlet";
        form.submit();
    }
}

function doBack() {
    form.hidAction.value = "SM";
    form.hidActionDetail.value = "ViewStaffList";
    form.action = "DashboardServlet";
    form.submit();
}

function changeStatus() {
    // Change to quit => allow to enter quit date
    if (form.status.value == 4) {
        form.quitDate.disabled = false;
        if (trimSpaces(form.quitDate.value) == "") {
            form.quitDate.value = "<%=beanUserInfo.getDateLogin()%>";
        }
    }
    // Disable input quit date if this is not a quit user
    else {
        form.quitDate.disabled = true;
    }
}
function formValidate() {
    if (trimSpaces(form.name.value) == "") {
        alert("Please, fill up name!");
        form.name.focus();
        return false;
    }
    if (trimSpaces(form.acc.value) == "") {
        alert("Please, fill up account!");
        form.acc.focus();
        return false;
    }
    if (trimSpaces(form.email.value) == "") {
        alert("Please, fill up email!");
        form.email.focus();
        return false;
    }
    if (trimSpaces(form.design.value) == "") {
        alert("Please, fill up postion!");
        form.design.focus();
        return false;
    }
    if (!emailCheck(form.email.value)) {
        form.email.focus();
        return false;
    }
    if (trimSpaces(form.startDate.value) == "") {
        alert("Please, fill up start date!");
        form.startDate.focus();
        return false;
    }
    if (!(isDate(form.startDate.value))) {
        alert("Invalid start date (dd/mm/yy) value.");
        form.startDate.focus();
        return false;
    }
    
    if (form.status.value == 4) {//Quit
        if (trimSpaces(form.quitDate.value) == "") {
            alert("Please, fill up quit date!");
            form.quitDate.focus();
            return false;
        }
        if (!(isDate(form.quitDate.value))) {
            alert("Invalid quit date (dd/mm/yy) value.");
            form.quitDate.focus();
            return false;
        }
        if (CompareDate(form.startDate.value, form.quitDate.value) == 1) {
            alert("Quit date cannot be prior to start date.");
            form.quitDate.focus();
            return false;
        }
    }
    return true;
}

// Init from: disable quit date text box if not a quit user
changeStatus();
</SCRIPT>
</BODY>
</HTML>