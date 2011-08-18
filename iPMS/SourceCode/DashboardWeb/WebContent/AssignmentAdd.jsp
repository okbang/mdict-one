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
<TITLE>Assign Developer</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
var button_clicked = '';
var order = [];
var sep = ' ';
var max_move = 10;

function clickAdd() {
    var form = document.forms[0];
    var curCateID = split(form.curCateID.value, sep);
    var developer = form.lstDeveloper.options;
    var selected_developer = form.lstSelected.options;

    addOptions(developer, selected_developer);
    var selected_str = "";
    for (var i = 0; i < selected_developer.length; i++) {
        selected_str += selected_developer[i].value + sep;
    }
    form.curCateID.value = selected_str;
}

function clickRemove() {
    var form = document.forms[0];
    var curCateID = split(form.curCateID.value, sep);
    var developer = form.lstDeveloper.options;
    var selected_developer = form.lstSelected.options;
    removeOptions(selected_developer, developer);
    var selected_str = '';
    for (var i = 0; i < selected_developer.length; i++) {
        selected_str += selected_developer[i].value + sep;
    }
    form.curCateID.value = selected_str;
}

function split(string, sep) {
    var items = string.split(sep);
    var result = [];
    for (var i = 0; i < items.length; i++) {
        if (items[i]) {
            result[result.length] = items[i];
        }
    }
    return result;
}

function addOptions(src, dst) {
    var selected_value = [];
    var selected_text = [];
    for (var i = 0; i < src.length; i++) {
        if (src[i].selected) {
            var exists = 0;
            for (var j = 0; j < dst.length; j++) {
                if (dst[j].value == src[i].value) {
                    exists = 1;
                    break;
                }
            }
            if (!exists) {
                selected_value[selected_value.length] = src[i].value;
                selected_text[selected_text.length] = src[i].text;
            }
        }
    }
    for (var i = 0; i < dst.length; i++) {
        selected_value[selected_value.length] = dst[i].value;
        selected_text[selected_text.length] = dst[i].text;
    }
    while (dst.length >= 1) {
        dst[0] = null;
    }
    for (var j = 0; j < selected_value.length; j++) {
        if (selected_value[j] == "") {
            continue;
        }
        dst[dst.length] = new Option(selected_text[j], selected_value[j]);
    }
}

function removeOptions(src) {
    for (var i = 0; i < src.length; i++) {
        if (src[i].selected) {
            src[i--] = null;
        }
    }
}

function doSave() {
    var form = document.frmAssignmentAdd;
    if (nSubmit()) {
        form.hidAction.value = "PM";
        form.hidActionDetail.value = "SaveNewAssignment";
        form.action = "DashboardServlet";
        form.submit();
    }
}

function nSubmit() {
    var form = document.forms[0];
    if (form.lstSelected.length < 1) {
        alert("You must select at least one developer!");
        return false;
    }
    form.hidSelID.value = "";
    for (var i = 0; i < form.lstSelected.length; i++) {
        form.hidSelID.value = form.hidSelID.value + form.lstSelected[i].value + ",";
    }
    return true;
}

function doBack() {
    var form = document.frmAssignmentAdd;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ListAssignment";
    form.action = "DashboardServlet";
    form.submit();
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF"><%
    String strTitle = "Assign Developer";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmAssignmentAdd">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="curCateID" value="">
<INPUT type="hidden" name="hidSelID" value="">
<INPUT type="hidden" name="hidProjectID" value="<%=request.getParameter("hidProjectID")%>">
<INPUT type="hidden" name="hidProjectStart" value="<%=request.getParameter("hidProjectStart")%>">
<INPUT type="hidden" name="hidProjectEnd" value="<%=request.getParameter("hidProjectEnd")%>">
<CENTER>
<TABLE border="0" width="70%">
    <TR>
        <TD width="100%" colspan="3" align="center"><B><FONT face="Verdana" size="1">Please select developers then click (--&gt;) to assign.<BR>
        To remove please click(&lt;--)</FONT></B></TD>
    </TR>
    <TR>
        <TD width="40%"><B><FONT face="Verdana" size="1"> Group</FONT></B></TD>
        <TD width="20%" align="center">&nbsp;</TD>
        <TD width="40%">&nbsp;</TD>
    </TR>
    <TR>
        <TD width="40%"><SELECT name="lstGroup" title="Double click to quick select" style="width: 120" onchange="selectGroup();"><%
    for (int i = 0; i < beanAssignmentAdd.getGroupList().getNumberOfRows(); i++) {
        String strGroup = beanAssignmentAdd.getGroupList().getCell(i, 0);
%>
            <OPTION value="<%=strGroup%>"<%=
            (strGroup.equals(beanAssignmentAdd.getSelectedGroup()) ? " selected" : "")%>><%=strGroup%></OPTION><%
    }
%>
        </SELECT></TD>
        <TD width="20%" align="center">&nbsp;</TD>
        <TD width="40%">&nbsp;</TD>
    </TR>
    <TR>
        <TD width="40%"><B><FONT face="Verdana" size="1"> Developers list</FONT></B></TD>
        <TD width="20%" align="center">&nbsp;</TD>
        <TD width="40%"><B><FONT face="Verdana" size="1"> Selected developers</FONT></B></TD>
    </TR>
    <TR>
        <TD width="40%"><SELECT name="lstDeveloper" title="Double click to quick remove" multiple size="15" style="width: 100%" ondblclick="clickAdd()"></SELECT></TD>
        <TD width="20%" align="center"><INPUT type="button" name="Add" value=" --&gt; " onclick="clickAdd()"><BR>
        <INPUT type="button" name="Delete" value=" &lt;-- " onclick="clickRemove()"></TD>
        <TD width="40%"><SELECT name="lstSelected" multiple size="15" style="width: 100%" ondblclick="clickRemove()">
        </SELECT></TD>
    </TR>
</TABLE>
<TABLE border="0" width="40%">
    <TR>
        <TD width="50%" align="center"><INPUT type="button" name="DoSaveNewAssignment" onclick="javascript:doSave()" value="Add">
        <INPUT type="button" name="DoBackFromNewAssignment" onclick="javascript:doBack()" value="Back"></TD>
    </TR>
</TABLE>
</CENTER>
</FORM>
<SCRIPT language="javascript">
var a = new Array();
<%
    for (int i = 0; i < beanAssignmentAdd.getDevList().getNumberOfRows(); i++) {
        out.write("\na[" + i + "]=new Array(3);");
        out.write("a[" + i + "][0]=" + beanAssignmentAdd.getDevList().getCell(i, 0) + ";");
        out.write("a[" + i + "][1]='" + beanAssignmentAdd.getDevList().getCell(i, 1) + "';");
        out.write("a[" + i + "][2]='" + beanAssignmentAdd.getDevList().getCell(i, 2) + "';");
    }
%>

function appendOption(ctrlSelect, strValue, strText, strLabel, bSelected) {
    var optNew = document.createElement('option');
    optNew.value = strValue;
    optNew.text = strText;
    optNew.label = strLabel;
    if (bSelected == true) {
        optNew.selected = true;
    }

    try {
        ctrlSelect.add(optNew, null); // standards compliant; doesn't work in IE
    }
    catch(ex) {
        ctrlSelect.add(optNew); // IE only
    }
}
function selectGroup() {
    var myForm = document.forms[0];
    while (myForm.lstDeveloper.options.length > 0) {
        myForm.lstDeveloper.options[0] = null;
    }

    if ((myForm.lstGroup.value != "<%=fpt.dashboard.constant.DATA.GROUP_ALL%>") &&
        (myForm.lstGroup.value.length > 0))
    {
        for (var i = 0; i < a.length; i++) {
            if (a[i][2] == myForm.lstGroup.value) {
                appendOption(myForm.lstDeveloper, a[i][0], a[i][1], a[i][2], false);
            }
        }
    }
    else {
        for (var i = 0; i < a.length; i++) {
            appendOption(myForm.lstDeveloper, a[i][0], a[i][1], a[i][2], false);
        }
    }
}

selectGroup();
</SCRIPT>
</BODY>
</HTML>