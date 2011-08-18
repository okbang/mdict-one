<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, java.text.SimpleDateFormat,
            javax.servlet.*, fpt.dashboard.bean.*,
            fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*,
            fpt.dashboard.combo.ConstantRow" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo =
            (UserInfoBean)session.getAttribute("beanUserInfo");
    AssignmentListBean beanAssignmentList =
            (AssignmentListBean)request.getAttribute("beanAssignmentList");
    ArrayList arlResponse = beanAssignmentList.getResponseList();
    ConstantRow row;
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<TITLE>Assignment List</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<LINK href="styles/pcal.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
function doSave() {
    var form = document.frmAssignmentList;
    if (checkSave()) {
        form.hidAction.value = "PM";
        form.hidActionDetail.value = "SaveAssignment";
        form.action = "DashboardServlet";
        form.submit();
    }
}

function doAdd() {
    var form = document.frmAssignmentList;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "AddAssignment";
    form.action = "DashboardServlet";
    form.submit();
}

function doDelete() {
    var form = document.frmAssignmentList;
    if (checkValid()) {
        if (confirm("Do you want to delete selected records, continue?")) {
            form.hidAction.value = "PM";
            form.hidActionDetail.value = "DeleteAssignment";
            form.action = "DashboardServlet";
            form.submit();
        }
    }
}

function doBack() {
    var form = document.frmAssignmentList;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewProjectDetail";
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

function checkSave() {
    var flag = true;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == "End" && e.type == "text") {
            if (!validateRequiredTextField(e, "Invalid End Date.")) {
                e.focus();
                return false;
            }
        }
        if (e.name == "Begin" && e.type == "text") {
            if (!validateRequiredTextField(e, "Invalid Begin Date.")) {
                e.focus();
                return false;
            }
        }
    }
    return true;
}

function checkNumber(source) {
    if (!isPositiveInteger(source.value)) {
        alert("Invalid number.");
        source.focus();
        return false;
    }
    else {
        if (parseInt(source.value)>100) {
            alert("This number must be between 0 and 100");
            source.focus();
            return false;
        }
    }
}

function callcheckdate(i) {
    str = document.forms[0].Begin[i].value;
    if (!isDate(str)) {
        alert("Invalid date format!");
        document.forms[0].Begin[i].focus();
        return (false);
    }
    if (CompareDate(str, "<%=beanAssignmentList.getProjectStart()%>") == -1) {
        alert("The assignment date must be following <%=beanAssignmentList.getProjectStart()%>");
        return false;
    }
    if (CompareDate(str,document.forms[0].End[i].value) == 1) {
        alert("The begin assignment date must before " + document.forms[0].End[i].value);
        return false;
    }
    return(true);
}

function callcheckdate2(i) {
    str = document.forms[0].End[i].value;
    if (!isDate(str)) {
        alert("Invalid date format!");
        document.forms[0].End[i].focus();
        return (false);
    }
    if (CompareDate(str, "<%=beanAssignmentList.getProjectFinish()%>") == 1) {
        alert("The assignment date must be before <%=beanAssignmentList.getProjectFinish()%>");
        return false;
    }
    if (CompareDate(str,document.forms[0].Begin[i].value) == -1) {
        alert("The end assignment date must be after " + document.forms[0].Begin[i].value);
        return false;
    }
    return(true);
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF"><%
    String strTitle = "Assignment List";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmAssignmentList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidProjectID" value="<%=beanAssignmentList.getProjectID()%>">
<INPUT type="hidden" name="hidProjectStart" value="<%=beanAssignmentList.getProjectStart()%>">
<INPUT type="hidden" name="hidProjectEnd" value="<%=beanAssignmentList.getProjectFinish()%>">
<CENTER>
<TABLE border="0" width="80%" cellspacing="1" cellpadding="0">
    <TR>
        <TD width="100%" colspan="2"><FONT face="Verdana" size="1"> <B>&nbsp;Project name:</B>&nbsp; <%=beanAssignmentList.getProjectName()%></FONT></TD>
    </TR>
    <TR>
        <TD width="25%"><FONT face="Verdana" size="1"> <B>&nbsp;Project code:</B>&nbsp; <%=beanAssignmentList.getProjectCode()%></FONT></TD>

        <TD width="25%" align="right"><FONT face="Verdana" size="1"> <B>&nbsp;Leader:</B> <%=beanAssignmentList.getStrLeader()%></FONT></TD>
    </TR>
</TABLE>
<TABLE border="0" width="80%" cellspacing="1" cellpadding="0">
    <TR class="table_header">
        <TD width="10%" height="25">ID</TD>
        <TD width="25%" height="25">Developer</TD>
        <TD width="15%" height="25">Start date<BR>(dd/mm/yy)</TD>
        <TD width="15%" height="25">End date<BR>(dd/mm/yy)</TD>
        <TD width="15%" height="25">Type</TD>
        <TD width="10%" height="25">Usage (%)</TD>
        <TD width="10%" height="25">Responsibility</TD>
    </TR><%
    for (int i = 0; i < beanAssignmentList.getAssignList().getNumberOfRows(); i++) {
%>
    <TR class="row<%=i % 2 + 1%>">
        <TD width="10%" align="center"><INPUT type="checkbox" name="dev_id" value="<%=beanAssignmentList.getAssignList().getCell(i, 0)%>"></TD>
        <TD width="25%">&nbsp;<%=beanAssignmentList.getAssignList().getCell(i, 1)%></TD>
        <TD width="15%" align="center"><%
        String tmpStr = "";
        if (beanAssignmentList.getAssignList().getCell(i, 2) != null) {
            tmpStr = beanAssignmentList.getAssignList().getCell(i, 2);
        }
        else {
            tmpStr = "";
        }
%>
            <INPUT type="text" name="Begin" size="8" maxlength="10" value="<%=tmpStr%>" onchange="return callcheckdate(<%=i%>)">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].Begin[<%=i%>], document.forms[0].Begin[<%=i%>], "dd/mm/yy",null,1,-1,-1,true)'>
        </TD>
        <TD width="15%" align="center"><%
        if (beanAssignmentList.getAssignList().getCell(i, 3) != null) {
            tmpStr = beanAssignmentList.getAssignList().getCell(i, 3);
        }
        else {
            tmpStr = "";
        }
%>
            <INPUT type="text" name="End" size="8" maxlength="10" value="<%=tmpStr%>" onchange="return callcheckdate2(<%=i%>)">
            <IMG src="images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].End[<%=i%>], document.forms[0].End[<%=i%>], "dd/mm/yy",null,1,-1,-1,true)'>
        </TD>
        <TD width="15%" align="center"><%
        String tmpStr2 = "";
        Integer intTmp = new Integer(beanAssignmentList.getAssignList().getCell(i, 4));
        if (intTmp.intValue() == 3) {
            tmpStr = "selected";
            tmpStr2 = "";
        }
        else if (intTmp.intValue() == 2) {
            tmpStr = "";
            tmpStr2 = "selected";
        }
%>
        <SELECT name="type">
            <OPTION value="3" <%=tmpStr%>>Tentative</OPTION>
            <OPTION value="2" <%=tmpStr2%>>Allocated</OPTION>
        </SELECT></TD>
        <TD width="10%" align="center"><%
        tmpStr = beanAssignmentList.getAssignList().getCell(i, 5);
%>
        <INPUT type="text" name="Usage" size="3" maxlength="3" value="<%=tmpStr%>" onchange="return checkNumber(this)"></TD><%
//        String[] arrSelect = new String[8];
//        for (int j = 0; j < 8; j++) {
//            arrSelect[j] = "";
//        }
        int intS = Integer.parseInt(beanAssignmentList.getAssignList().getCell(i, 6));
//        arrSelect[intS] = "selected";
%>
        <TD width="10%" align="left"><SELECT size="1" name="cboPosition"><%
        String strSelected = "";
        for (int iRes = 0; iRes < arlResponse.size(); iRes++) {
            row = (ConstantRow) arlResponse.get(iRes);
            strSelected = "";
            if (row.getID() == intS) {
                strSelected = " selected";
            }
%>
            <OPTION value="<%=row.getID()%>"<%=strSelected%>><%=row.getTitle()%></OPTION><%
        }
%>
        </SELECT></TD>
    </TR><%
    }
%>
</TABLE>
<!-- Form always has array of input dates -->
<INPUT type="hidden" name="Begin" value="<%=beanAssignmentList.getAssignList().getCell(0, 2)%>">
<INPUT type="hidden" name="End" value="<%=beanAssignmentList.getAssignList().getCell(0, 3)%>">
<BR>
<TABLE border="0" width="30%">
    <TR>
        <TD width="30%" align="center"><INPUT type="button" name="DoSaveUpdateAssignment" onclick="javascript:doSave()" value="Save">
        <INPUT type="button" name="DoAddAssignment" onclick="javascript:doAdd()" value="Add">
        <INPUT type="button" name="DoDeleteAssignment" onclick="javascript:doDelete()" value="Delete">
        <INPUT type="button" name="DoBackFromListAssignment" onclick="javascript:doBack()" value="Back"></TD>
    </TR>
</TABLE>
</CENTER>
</FORM>
<BR>
<BR>
<BR>
<BR>
<BR>
</BODY>
</HTML>