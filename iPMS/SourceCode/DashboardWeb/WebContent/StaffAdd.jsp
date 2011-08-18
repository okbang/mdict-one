<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, java.text.SimpleDateFormat,
        javax.servlet.*, fpt.dashboard.bean.*, fpt.dashboard.bean.StaffManagement.*,
        fpt.dashboard.util.StringUtil.*,fpt.dashboard.util.DateUtil.*" %><%@
        page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    StaffAddBean beanStaffAdd = (StaffAddBean)request.getAttribute("beanStaffAdd");
%>
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<TITLE>Add Staff</TITLE>
<SCRIPT language="javascript" src="scripts/sha1.js"></SCRIPT>
<SCRIPT language="javascript" src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT language="javascript" src="scripts/popcalendar.js"></SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF"><%
    String strTitle = "Add Staff";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmStaffAdd">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<CENTER>
<TABLE border="0" width="80%" cellpadding="5">
    <COLGROUP>
        <COL width="15%">
        <COL width="35%">
        <COL width="15%">
        <COL width="35%">
    <TR>
        <TD colspan="5"><%
    if (!"".equals(beanStaffAdd.getMessage())) {
        out.write("<B><FONT color=red>" + beanStaffAdd.getMessage() + "</FONT></B>");
            beanStaffAdd.setMessage("");
    }
%>
        </TD>
    </TR>
    <TR>
        <TD align="right">Name<FONT color="red">*</FONT></TD>
        <TD><INPUT type="text" name="name" size="29" maxlength="29" value="<%=beanStaffAdd.getName()%>"></TD>
        <TD align="right">Group<FONT color="red">*</FONT></TD>
        <TD><SELECT name="group" style="width: 140"><%
    String[] arrGroup = beanStaffAdd.getArrGroup();
    for(int i = 0; i < arrGroup.length; i++) {
%>
            <OPTION value="<%=arrGroup[i]%>"<%=beanStaffAdd.getGroup().equals(arrGroup[i]) ? " selected" : ""%>><%=arrGroup[i]%></OPTION><%
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD align="right">Account<FONT color="red">*</FONT></TD>
        <TD><INPUT type="text" name="acc" size="29" maxlength="29" value="<%=beanStaffAdd.getAccount()%>"></TD>
        <TD align="right">Role<FONT color="red">*</FONT></TD>
        <TD><SELECT name="role" style="width: 140">
            <OPTION value="1000000000"<%=beanStaffAdd.getRole().equals("1000000000") ? " selected" : ""%>>Developer</OPTION>
            <OPTION value="1100000000"<%=beanStaffAdd.getRole().equals("1100000000") ? " selected" : ""%>>Project Leader</OPTION>
            <OPTION value="1110000000"<%=beanStaffAdd.getRole().equals("1110000000") ? " selected" : ""%>>Group Leader</OPTION>
            <OPTION value="1000100000"<%=beanStaffAdd.getRole().equals("1000100000") ? " selected" : ""%>>PQA/SQA</OPTION>
            <OPTION value="1111110000"<%=beanStaffAdd.getRole().equals("1111110000") ? " selected" : ""%>>Manager</OPTION>
            <OPTION value="0000001000"<%=beanStaffAdd.getRole().equals("0000001000") ? " selected" : ""%>>External of project level</OPTION>
            <OPTION value="0000001100"<%=beanStaffAdd.getRole().equals("0000001100") ? " selected" : ""%>>External of group level</OPTION>
            <OPTION value="0000000010"<%=beanStaffAdd.getRole().equals("0000000010") ? " selected" : ""%>>Communicator</OPTION>
        </SELECT></TD>
    </TR>
    <TR>
        <TD align="right">Password<FONT color="red">*</FONT></TD>
        <TD><INPUT type="password" name="pass" size="29" maxlength="29"></TD>
        <TD align="right">Status<FONT color="red">*</FONT></TD>
        <TD valign="middle"><SELECT name="status" size="1" style="width: 140"><%
    for (int nRow=0x00; nRow < beanStaffAdd.getStatusList().getNumberOfRows(); nRow++) {
        String strText = beanStaffAdd.getStatusList().getCell(nRow, 0);
        out.write("<OPTION ");
        out.write("value=\"" + strText + "\"" + (strText.equals(beanStaffAdd.getStatus()) ? " selected" : "") +
        			">" + beanStaffAdd.getStatusList().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD align="right">Email<font color="red">*</font></TD>
        <TD><INPUT type="text" name="email" size="29" maxlength="50" value="<%=beanStaffAdd.getEmail()%>"></TD>
        <TD align="right">Position<FONT color="red">*</FONT></TD>
        <TD><INPUT type="text" name="design" size="29" maxlength="29" value="<%=beanStaffAdd.getDesignation()%>"></TD>
    </TR>
    <TR>
        <TD align="right">Start Date<font color="red">*</font></TD>
        <TD><INPUT type="text" name="startDate" size="29" maxlength="8" value="<%=(beanStaffAdd.getStartDate() == null) ? "" : beanStaffAdd.getStartDate()%>">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].startDate, document.forms[0].startDate, "dd/mm/yy",null,1,-1,-1,true)'>
            &nbsp;(dd/mm/yy)
        </TD>
        <TD align="right"></TD>
        <TD></TD>
    </TR>
    <TR>
        <TD width="100%" colspan="5">
        <CENTER><BR>
        <INPUT type="button" name="DoSaveNewDeveloper" onclick="doSave()" value="Save">
        <INPUT type="button" name="DoBackFormAddDev" onclick="doBack()" value="Back">
        </CENTER>
        </TD>
    </TR>
</TABLE>
</CENTER>
</FORM>
<SCRIPT language="javascript">
    frmStaffAdd.name.focus();

function doSave() {
    if (formValidate()) {
        frmStaffAdd.pass.value= hex_sha1(frmStaffAdd.pass.value);
        frmStaffAdd.hidAction.value = "SM";
        frmStaffAdd.hidActionDetail.value = "SaveNewStaff";
        frmStaffAdd.action = "DashboardServlet";
        frmStaffAdd.submit();
    }
}

function doBack() {
    frmStaffAdd.hidAction.value = "SM";
    frmStaffAdd.hidActionDetail.value = "ViewStaffList";
    frmStaffAdd.action = "DashboardServlet";
    frmStaffAdd.submit();
}

function formValidate() {
    if (trimSpaces(frmStaffAdd.name.value) == "") {
        alert("Please, fill up name!");
        frmStaffAdd.name.focus();
        return false;
    }
    if (trimSpaces(frmStaffAdd.acc.value) == "") {
        alert("Please, fill up account!");
        frmStaffAdd.acc.focus();
        return false;
    }
    if (trimSpaces(frmStaffAdd.pass.value) == "") {
        alert("Please, fill up password!");
        frmStaffAdd.pass.focus();
        return false;
    }
    if (trimSpaces(frmStaffAdd.email.value) == "") {
        alert("Please, fill up email!");
        frmStaffAdd.email.focus();
        return false;
    }
    if (trimSpaces(frmStaffAdd.design.value) == "") {
        alert("Please, fill up postion!");
        frmStaffAdd.design.focus();
        return false;
    }
    if (!emailCheck(frmStaffAdd.email.value)) {
        frmStaffAdd.email.focus();
        return false;
    }
    if (trimSpaces(frmStaffAdd.startDate.value) == "") {
        alert("Please, fill up start date!");
        frmStaffAdd.startDate.focus();
        return false;
    }
    if (!(isDate(frmStaffAdd.startDate.value))) {
        alert("Invalid start date (dd/mm/yy) value.");
        frmStaffAdd.startDate.focus();
        return false;
    }
    return true;
}

function doHome() {
    frmStaffAdd.hidAction.value = "DashboardHomepage";
    frmStaffAdd.action = "DashboardServlet";
    frmStaffAdd.submit();
}
</SCRIPT>
</BODY>
</HTML>