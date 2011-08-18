<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ProjectUpdateBean beanProjectUpdate
            = (ProjectUpdateBean)request.getAttribute("beanProjectUpdate");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/util.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<TITLE>Update Major Information</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/StyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT language="javascript">
function doSave() {
    var form = document.frmProjectUpdate;
    if (!validateUpdateProjectForm(form)) {
        return;
    }
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "SaveUpdateProject";
    form.action = "DashboardServlet";
    form.submit();
}

function doBack() {
    var form = document.frmProjectUpdate;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewProjectDetail";
    form.action = "DashboardServlet";
    form.submit();
}

function isValidDateValues() {
    var form = document.frmProjectUpdate;
    form.startDate.value = trim(form.startDate.value);
    form.baseFinishDate.value = trim(form.baseFinishDate.value);
    form.planFinishDate.value = trim(form.planFinishDate.value);
    var _startDate = form.startDate.value;
    var _baseFinishDate = form.baseFinishDate.value;
    var _planFinishDate = form.planFinishDate.value;
    // External project => some fields are mandatory
    var isExternal = false;
    if (form.type.value == 0) {
        isExternal = true;
    }

    if (isExternal || (_startDate.length > 0)) {
        if (!(isDate(_startDate))) {
            alert("Invalid Start Date (dd/mm/yy) value.");
            form.startDate.focus();
            return false;
        }
        if (CompareToToday(_startDate) == 1){
        	alert("Start Date must be before or equal to today");
        	form.startDate.focus();
        	return false;
        }
    }
    if (_planFinishDate.length > 0) {
        if (!(isDate(_planFinishDate))) {
            alert("Invalid Re-planned Finish Date value.");
            form.planFinishDate.focus();
            return false;
        }
        if (CompareDate(_startDate,_planFinishDate) != -1) {
            alert("Re-planned Finish Date cannot be prior to the Start Date.");
            form.planFinishDate.focus();
            return false;
        }
    }
    if (isExternal || (_baseFinishDate.length > 0)) {
        if (!(isDate(_baseFinishDate))) {
            alert("Invalid Base Finish Date value.");
            form.baseFinishDate.focus();
            return false;
        }
    }
    if ((_startDate.length > 0) && (_baseFinishDate.length > 0)) {
        if (CompareDate(_startDate,_baseFinishDate) != -1) {
            alert("The Base Finish Date cannot be prior to the Start Date.");
            form.baseFinishDate.focus();
            return false;
        }
    }

    return true;
}

function isValidNumberValues() {
    var form = document.frmProjectUpdate;
    form.perComplete.value = trim(form.perComplete.value);
    form.baseEffort.value = trim(form.baseEffort.value);
    form.planEffort.value = trim(form.planEffort.value);
    form.actualEffort.value = trim(form.actualEffort.value);
    var _perComplete = form.perComplete.value;
    var _baseEffort = form.baseEffort.value;
    var _planEffort = form.planEffort.value;
    var _actualEffort = form.actualEffort.value;
    // External project => some fields are mandatory
    var isExternal = false;
    if (form.type.value == 0) {
        isExternal = true;
    }

    if (isExternal || (_perComplete.length > 0)) {
        if (!isPer(_perComplete)) {
            alert("Invalid Per Complete value.");
            form.perComplete.focus();
            return false;
        }
    }
    if (isExternal || (_baseEffort.length > 0)) {
        if (!(isFloat(_baseEffort))) {
            alert("Invalid Base Effort value.");
            form.baseEffort.focus();
            return false;
        }
    }
    if (_actualEffort.length > 0) {
        if (!(isFloat(_actualEffort))) {
            alert("Invalid Actual Effort value.");
            form.actualEffort.focus();
            return false;
        }
    }
    if (_planEffort.length > 0) {
        if (!(isFloat(_planEffort))) {
            alert("Invalid Re-planned Effort value.");
            form.planEffort.focus();
            return false;
        }
    }

    return true;
}

function validateUpdateProjectForm(form) {
    // check code
    if (emptyField(form.code)) {
        alert("Project Code field is empty.");
        form.code.focus();
        return false;
    }
    // check if name field is empty
    if (emptyField(form.name)) {
        alert("Project Name field is empty.");
        form.name.focus();
        return false;
    }
    if (emptyField(form.startDate)){
    	alert("Start date field is empty");
    	form.startDate.focus();
    	return false;
    }
	if (emptyField(form.perComplete)){
		alert("Per complete field is empty.");
		form.perComplete.focus();
		return false;
	}
	if (emptyField(form.baseEffort)){
		alert("Planned Effort field is empty.");
		form.baseEffort.focus();
		return false;
	}
	if (emptyField(form.baseFinishDate)){
		alert("Planned Finish Date  field is empty.");
		form.baseFinishDate.focus();
		return false;
	}

    if (!isValidDateValues()) {
        return false;
    }
    if (!isValidNumberValues()) {
        return false;
    }
    return true;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="center"><%!
    String strTitle = "Update Major Information";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmProjectUpdate">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="id" value="<%=beanProjectUpdate.getId()%>">
<TABLE border="0" cellpadding="3" cellspacing="3" align="center" width="95%">
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
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Code</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD class="main_cell_bkgrnd" height="20"><INPUT name="code" size="27" value="<%=beanProjectUpdate.getCode()%>" maxlength="19"></TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Name</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><INPUT name="name" size="27" value="<%=beanProjectUpdate.getName()%>" maxlength="29"></TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Customer</B>&nbsp;</TD>
        <TD class="main_cell_bkgrnd" height="20"><INPUT name="customer" size="27" maxlength="50" value="<%=beanProjectUpdate.getCustomer()%>"></TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Category</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><SELECT name="cate" size="1" style="width: 152"><%
    for (int nRow = 0x00; nRow < beanProjectUpdate.getCateList().getNumberOfRows(); nRow++) {
        String strText = beanProjectUpdate.getCateList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectUpdate.getCate() != null) {
            out.write(beanProjectUpdate.getCate().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">"
                + beanProjectUpdate.getCateList().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Type</B>&nbsp;<FONT color="red">*</FONT></TD><%
    // Common variable
    String tmp = "";
    int maxrows;
    int i;
%>
        <TD height="20"><SELECT name="type" size="1" style="width: 152"><%
    for (int nRow=0x00; nRow < beanProjectUpdate.getTypeList().getNumberOfRows(); nRow++) {
        String strText = beanProjectUpdate.getTypeList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectUpdate.getType() != null) {
            out.write(beanProjectUpdate.getType().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">"
                + beanProjectUpdate.getTypeList().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Group</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><SELECT name="group" size="1" style="width: 152"><%
    for (int nRow = 0x00; nRow < beanProjectUpdate.getGroupList().getNumberOfRows(); nRow++) {
        String strText = beanProjectUpdate.getGroupList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectUpdate.getGroup() != null) {
            out.write(beanProjectUpdate.getGroup().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Leader</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><SELECT name="leader" size="1" style="width: 152"><%
    for (int nRow = 0x00; nRow < beanProjectUpdate.getLeaderList().getNumberOfRows(); nRow++) {
        String strText = beanProjectUpdate.getLeaderList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectUpdate.getLeader() != null) {
            out.write(beanProjectUpdate.getLeader().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD height="20"><B>Start date</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20">
            <INPUT name="startDate" size="27" value="<%=beanProjectUpdate.getStartDate()%>">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].startDate, document.forms[0].startDate, "dd/mm/yy",null,1,-1,-1,true)'>
            &nbsp;(dd/mm/yy)
        </TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Per complete</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><INPUT name="perComplete" size="27" value="<%=beanProjectUpdate.getPerComplete()%>" maxlength="3"></TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Planned Effort</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><INPUT name="baseEffort" size="27" value="<%=beanProjectUpdate.getBaseEffort()%>" maxlength="8">&nbsp;(pd)</TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Planned&nbsp;Finish&nbsp;Date</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20">
            <INPUT name="baseFinishDate" size="27" value="<%=beanProjectUpdate.getBaseFinishDate()%>">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].baseFinishDate, "dd/mm/yy",null,1,-1,-1,true)'>
            &nbsp;(dd/mm/yy)
        </TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Replanned Effort</B></TD>
        <TD height="20"><INPUT name="planEffort" size="27" value="<%=beanProjectUpdate.getPlanEffort()%>" maxlength="8">&nbsp;(pd)</TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Replanned&nbsp;Finish&nbsp;Date</B></TD>
        <TD height="20">
            <INPUT name="planFinishDate" size="27" value="<%=beanProjectUpdate.getPlanFinishDate()%>">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].planFinishDate, "dd/mm/yy",null,1,-1,-1,true)'>
            &nbsp;(dd/mm/yy)
        </TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Actual Effort</B></TD>
        <TD height="20"><INPUT name="actualEffort" size="27" value="<%=beanProjectUpdate.getActualEffort()%>" maxlength="8">&nbsp;(pd)</TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Status</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><SELECT name="status" size="1" style="width: 152"><%
    for (int nRow = 0x00; nRow < beanProjectUpdate.getStatusList().getNumberOfRows(); nRow++) {
        String strText = beanProjectUpdate.getStatusList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectUpdate.getStatus() != null) {
            out.write(beanProjectUpdate.getStatus().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">"
                + beanProjectUpdate.getStatusList().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"></TD>
        <TD height="20"></TD>
    </TR>
</TABLE>
<P><INPUT type="button" name="DoSaveMajorInfo" onclick="javascript:doSave();" value="Save">
<INPUT type="button" name="DoBackFromMajor" onclick="javascript:doBack()" value="Back"></P>
</FORM>
<BR>
<BR>
</DIV>
</BODY>
</HTML>