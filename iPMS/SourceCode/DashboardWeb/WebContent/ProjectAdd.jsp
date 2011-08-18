<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ProjectAddBean beanProjectAdd
            = (ProjectAddBean)request.getAttribute("beanProjectAdd");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/util.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<TITLE>Add Project</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/StyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT language="javascript">
function doSave() {
    var form = document.frmProjectAdd;
    if (!validateAddProjectForm(form)) {
        return;
    }
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "SaveNewProject";
    form.action = "DashboardServlet";
    form.submit();
}

function doBack() {
    var form = document.frmProjectAdd;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewProjectList";
    form.action = "DashboardServlet";
    form.submit();
}

function validateAddProjectForm(form) {
    var ErrorMsg = "E R R O R (s) :  \n\n";
    var ErrorFound = false;
    // check code
    if (emptyField(form.code)) {
        ErrorMsg = ErrorMsg + "- Project Code field is empty.   \n";
        alert(ErrorMsg);
        form.code.focus();
        return false;
    }
    // check if name field is empty
    if (emptyField(form.name)) {
        ErrorMsg = ErrorMsg + "- Project Name field is empty.   \n";
        alert(ErrorMsg);
        form.name.focus();
        return false;
    }

    var _type = trim(form.type[form.type.selectedIndex].value);
    var _startDate = trim(form.startDate.value);
    var _perComplete = trim(form.perComplete.value);

    var _baseFinishDate = trim(form.baseFinishDate.value);
    var _planFinishDate = trim(form.planFinishDate.value);
    //var _actualFinishDate = trim(form.actualFinishDate.value);

    var _baseEffort = trim(form.baseEffort.value);
    var _planEffort = trim(form.planEffort.value);
    var _actualEffort = trim(form.actualEffort.value);

    if (_type != 3) {
        if (!(isDate(_startDate))) {
            ErrorMsg = ErrorMsg + "- Invalid Start Date (dd/mm/yy) value.   \n";
            alert(ErrorMsg);
            form.startDate.focus();
            return false;
        }
        if (!isPer(_perComplete)) {
            ErrorMsg = ErrorMsg + "- Invalid Per Complete value.   \n";
            alert(ErrorMsg);
            form.perComplete.focus();
            return false;
        }
        if (_planFinishDate.length > 0) {
            if (CompareDate(_startDate, _planFinishDate) != -1) {
                ErrorMsg = ErrorMsg + "- The Re-planned Finish Date cannot be prior to the Start Date.   \n";
                alert(ErrorMsg);
                form.planFinishDate.focus();
                return false;
            }
            if (!(isDate(_planFinishDate))) {
                ErrorMsg = ErrorMsg + "- Invalid Re-planned Finish Date value.   \n";
                alert(ErrorMsg);
                form.planFinishDate.focus();
                return false;
            }
        }
        if (CompareDate(_startDate, _baseFinishDate) != -1) {
            ErrorMsg = ErrorMsg + "- The Base Finish Date cannot be prior to the Start Date.   \n";
            alert(ErrorMsg);
            form.baseFinishDate.focus();
            return false;
        }
        if (!(isDate(_baseFinishDate))) {
            ErrorMsg = ErrorMsg + "- Invalid Base Finish Date value.   \n";
            alert(ErrorMsg);
            form.baseFinishDate.focus();
            return false;
        }
        if (_actualEffort.length > 0) {
            if (!(isFloat(_actualEffort))) {
                ErrorMsg = ErrorMsg + "- Invalid Actual Effort value.   \n";
                alert(ErrorMsg);
                form.actualEffort.focus();
                return false;
            }
        }
        if (_planEffort.length > 0) {
            if (!(isFloat(_planEffort))) {
                ErrorMsg = ErrorMsg + "- Invalid Re-planned Effort value.   \n";
                alert(ErrorMsg);
                form.planEffort.focus();
                return false;
            }
        }
        if (!(isFloat(_baseEffort))) {
            ErrorMsg = ErrorMsg + "- Invalid Base Effort value.   \n";
            alert(ErrorMsg);
            form.baseEffort.focus();
            return false;
        }
    }
    if (ErrorFound) {
        alert(ErrorMsg);
        return false;
    }
    return true;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="center"><!-- Page Header Title Information --><%!
    String strTitle = "Add Project";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmProjectAdd">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
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
        <TD class="main_cell_bkgrnd" height="20"><INPUT name="code" size="27" value="<%=beanProjectAdd.getCode()%>" maxlength="19"></TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Name</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><INPUT name="name" size="27" value="<%=beanProjectAdd.getName()%>" maxlength="29"></TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Customer</B>&nbsp;</TD>
        <TD class="main_cell_bkgrnd" height="20"><INPUT name="customer" size="27" value="<%=(beanProjectAdd.getCustomer() == null) ? "" : (beanProjectAdd.getCustomer())%>"></TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Category</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><SELECT name="cate" size="1" style="width: 152" onchange="javascript:('DoFilterProject', document.forms[0]);"><%
    for (int nRow = 0x00; nRow < beanProjectAdd.getCateList().getNumberOfRows(); nRow++) {
        String strText = beanProjectAdd.getCateList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectAdd.getCate()!= null) {
            out.write(beanProjectAdd.getCate().equals(strText)?" selected ":" ");
        }
        out.write("value=\"" + strText + "\">" + beanProjectAdd.getCateList().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Type</B>&nbsp;<FONT color="red">*</FONT></TD><%
    // Common variable
    int maxrows;
    int i;
%>
        <TD height="20"><SELECT name="type" size="1" style="width: 152">
            <!-- Fill Data --><%
    String tValue = "";
    String tDisplay = "";
    StringMatrix mtxType = beanProjectAdd.getTypeList();
    maxrows = mtxType.getNumberOfRows();
    i = 0;
    tValue = mtxType.getCell(i, 0);
    tDisplay = mtxType.getCell(i, 1);
%>
            <OPTION selected value="<%=tValue%>"><%=tDisplay%></OPTION><%
    i++;
    while (i < maxrows) {
        tValue = mtxType.getCell(i, 0);
        tDisplay = mtxType.getCell(i, 1);
%>
            <OPTION value="<%=tValue%>"><%=tDisplay%></OPTION><%
        i++;
    }
%>
        </SELECT></TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Group</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><SELECT name="group" size="1" style="width: 152">
            <!-- Fill Data --><%
    if ("2".equals(beanUserInfo.getRole())) {
        String gItem = "";
        StringMatrix mtxGroup = beanProjectAdd.getGroupList();
        maxrows = mtxGroup.getNumberOfRows();
        i = 0;
        while (i < maxrows) {
            gItem = mtxGroup.getCell(i, 0);
%>
            <OPTION value="<%=gItem%>"><%=gItem%></OPTION><%
            i++;
        }
    }
    else if (beanUserInfo.getGroupName() != null) {
%>
            <OPTION value="<%=beanUserInfo.getGroupName()%>"><%=beanUserInfo.getGroupName()%></OPTION><%
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Leader</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><SELECT name="leader" size="1" style="width: 152">
            <!-- Fill Data --><%
    String lItem = "";
    StringMatrix mtxLeader = beanProjectAdd.getLeaderList();
    maxrows = mtxLeader.getNumberOfRows();
    i = 0;
    while (i < maxrows) {
        lItem = mtxLeader.getCell(i, 0);
%>
            <OPTION value="<%=lItem%>"><%=lItem%></OPTION><%
        i++;
    }
%>
        </SELECT></TD>
        <TD height="20"><B>Start date</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20">
            <INPUT name="startDate" size="27" value="<%=beanProjectAdd.getStartDate()%>">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].startDate, document.forms[0].startDate, "dd/mm/yy",null,1,-1,-1,true)'>
            &nbsp;(dd/mm/yy)
        </TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Per complete</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><INPUT name="perComplete" size="27" value="<%=beanProjectAdd.getPerComplete()%>" maxlength="2"></TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Planned Effort</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><INPUT name="baseEffort" size="27" value="<%=beanProjectAdd.getBaseEffort()%>" maxlength="8">&nbsp;(pd)</TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Planned&nbsp;Finish&nbsp;Date</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20">
            <INPUT name="baseFinishDate" size="27" value="<%=beanProjectAdd.getBaseFinishDate()%>">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].baseFinishDate, "dd/mm/yy",null,1,-1,-1,true)'>
            &nbsp;(dd/mm/yy)
        </TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Replanned Effort</B></TD>
        <TD height="20"><INPUT name="planEffort" size="27" value="<%=beanProjectAdd.getPlanEffort()%>" maxlength="8">&nbsp;(pd)</TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B>Replanned&nbsp;Finish&nbsp;Date</B></TD>
        <TD height="20">
            <INPUT name="planFinishDate" size="27" value="<%=beanProjectAdd.getPlanFinishDate()%>">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, document.forms[0].planFinishDate, "dd/mm/yy",null,1,-1,-1,true)'>
            &nbsp;(dd/mm/yy)
        </TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B> Actual Effort</B></TD>
        <TD height="20"><INPUT name="actualEffort" size="27" value="<%=beanProjectAdd.getActualEffort()%>" maxlength="8">&nbsp;(pd)</TD>
    </TR>
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"><B> Status</B>&nbsp;<FONT color="red">*</FONT></TD>
        <TD height="20"><SELECT name="status" size="1" style="width: 152"><%
    for (int nRow = 0x00; nRow < beanProjectAdd.getStatusList().getNumberOfRows(); nRow++) {
        //int nValue = Integer.parseInt(ProjectDashBoardView.getGroupList().getCell(nRow, 0));
        String strText = beanProjectAdd.getStatusList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectAdd.getStatus() != null) {
            out.write(beanProjectAdd.getStatus().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">" + beanProjectAdd.getStatusList().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD class="required_left_cell_bkgrnd" align="left" height="20"></TD>
        <TD height="20"></TD>
    </TR>
</TABLE>
<P><INPUT type="button" name="DoSaveProject" onclick="javascript:doSave();" value="Save">
<INPUT type="button" name="DoBackFromAddProject" onclick="javascript:doBack()" value="Back"></P>
</FORM>
<BR><BR>
</DIV>
<SCRIPT language="javascript">
    document.forms[0].code.focus();
</SCRIPT>
</BODY>
</HTML>