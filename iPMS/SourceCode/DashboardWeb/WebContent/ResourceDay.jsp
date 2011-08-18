<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ResourceManagement.*,
            fpt.dashboard.util.StringUtil.*, fpt.dashboard.util.DateUtil.*,
            java.util.Date, java.text.*"%><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ResourceDayBean beanResourceDay
            = (ResourceDayBean)request.getAttribute("beanResourceDay");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Resource Allocation</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
function doViewDetailAssignment(id) {
    var form = document.frmResourceDay;
    form.hidAssignmentID.value = id;
    form.hidAction.value = "RM";
    form.hidActionDetail.value = "ViewAssignmentDetail";
    form.action = "DashboardServlet";
    form.submit();
}

function doSumNextMonth() {
    var form = document.frmResourceDay;
    if (NextMonth()) {
        form.hidAction.value = "RM";
        form.hidActionDetail.value = "ViewDailyResource";
        form.action = "DashboardServlet";
        form.submit();
    }
}

function doSumPrevMonth() {
    var form = document.frmResourceDay;
    if (PrevMonth()) {
        form.hidAction.value = "RM";
        form.hidActionDetail.value = "ViewDailyResource";
        form.action = "DashboardServlet";
        form.submit();
    }
}

function doFilterResource() {
    var form = document.frmResourceDay;
    if (checkMonth()) {
        if(checkYear()) {
            form.hidAction.value = "RM";
            form.hidActionDetail.value = form.cboType.value;
            form.action = "DashboardServlet";
            form.submit();
        }
    }
}

function NextMonth() {
    var num;
    num = document.forms[0].dMonth.value;
    if (num < 12) {
        num++;
    }
    else {
        num = 1;
        document.forms[0].Year.value++;
    }
    document.forms[0].dMonth.value = num;
    return true;
}

function PrevMonth() {
    var num;
    num = document.forms[0].dMonth.value;
    if (num > 1) {
        num--;
    }
    else {
        num = 12;
        document.forms[0].Year.value--;
    }
    document.forms[0].dMonth.value = num;
    return true;
}

function checkMonth() {
    var str = document.forms[0].dMonth.value;
    if (!isPositiveInteger(str)) {
        alert("Invalid number!");
        return false;
    }
    else {
        if ((str < 1) || (str > 12)) {
            alert("Month must be between 1 and 12!");
            return false;
        }
    }
    return true;
}

function checkYear() {
    var str = document.forms[0].Year.value;
    if (!isPositiveInteger(str)) {
        alert("Invalid number!");
        return false;
    }
    else {
        if ((str < 1990) || (str > 2100)) {
            alert("Year to far!");
            return false;
        }
    }
    return true;
}
</SCRIPT>
</HEAD><%
    int month = Integer.parseInt(beanResourceDay.getMonth()) - 1;
    int year = Integer.parseInt(beanResourceDay.getYear()) - 1900;
    int numDay = DateUtil.getNumDate(month + 1, year + 1900);
%>
<BODY bgcolor="#FFFFFF"><%
    String strTitle = "Resource Allocation";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmResourceDay">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAssignmentID" value="">
<TABLE border="0" width="100%" cellpadding="0" cellspacing="0" bgcolor="#e3e3ea">
    <TR>
        <TD align="left" height="10" valign="bottom" width="10%"></TD>
        <TD align="right" height="10" valign="top" width="90%"><A href="javascript:doSumPrevMonth()">Prev month</A> | <A href="javascript:doSumNextMonth()">Next month</A>&nbsp;&nbsp;&nbsp;
        <INPUT type="text" name="dMonth" size="2" maxlength="2" value="<%=month + 1%>" onchange="return checkMonth()"> /
        <INPUT type="text" name="Year" size="4" maxlength="4" value="<%=year + 1900%>" onchange="return checkYear()"><%
    String[] arrGroup = beanResourceDay.getArrGroup();
    String strGroup=beanResourceDay.getCondi();
%>
        &nbsp;Group&nbsp;<SELECT name="cboGroup" <%
    if (beanUserInfo.getRole().equals("4")) {
        out.print("disabled");
    }
%>>
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
        </SELECT>
        &nbsp;Type&nbsp;
        <SELECT name="cboType">
        <OPTION value="ViewWeeklyResource">View weekly report</OPTION>
        <OPTION value="ViewDailyResource" selected>View daily report</OPTION>
        <OPTION value="ViewProjectResource">View project report</OPTION>
        </SELECT>
        &nbsp;<INPUT type="button" name="DoResDayFilter" onclick="javascript:doFilterResource()" value="View"></TD>
    </TR>
</TABLE>
<TABLE border="0" width="100%" cellspacing="3" cellpadding="0">
    <TR>
        <TD width="8%" align="right">Onsite</TD>
        <TD width="8%" bgcolor="#FF00FF">&nbsp;</TD>
        <TD width="8%" align="right">Allocated</TD>
        <TD width="8%" bgcolor="#FFFF00">&nbsp;</TD>
        <TD width="8%" align="right">Tentative</TD>
        <TD width="8%" bgcolor="#800080">&nbsp;</TD>
        <TD width="8%" align="right">Training</TD>
        <TD width="8%" bgcolor="#008080">&nbsp;</TD>
        <TD width="9%" align="right">Off</TD>
        <TD width="9%" bgcolor="#0000FF">&nbsp;</TD>
        <TD width="9%" align="right">Overallocated</TD>
        <TD width="9%" bgcolor="#FF0000">&nbsp;</TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
    <TR class="table_header">
        <TD width="20%" height="45" rowspan="2">Developers</TD>
        <TD width="80%" height="24"><%=month + 1%>/<%=year + 1900%></TD>
    </TR>
    <TR>
        <TD width="80%" height="20">
        <TABLE border="0" cellspacing="1" cellpadding="0" width="100%"><%
    int pR = 600 / numDay;
%>
            <TR><%
    for (int i = 1; i < numDay + 1; i++) {
        if ((i % 2) == 0) {
%>
                <TD width="<%=pR%>" class="pcell2"><%=i%></TD><%
        }
        else {
%>
                <TD width="<%=pR%>" class="pcell1"><%=i%></TD><%
        }
    }
%>
            </TR>
        </TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="0" width="100%"><%
    int i = 0;
    for (i = 0; i < beanResourceDay.getBusyDevID().size(); i++) {
%>
    <TR class="row<%=(i + 1) % 2 + 1%>">
        <TD width="20%">&nbsp;<A href="javascript:doViewDetailAssignment('<%=beanResourceDay.getBusyDevID().elementAt(i)%>')"><%=(String)beanResourceDay.getBusyDevName().elementAt(i)%></A></TD>
        <TD width="80%">
        <TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
            <TR><%
        String[] tooltip = (String[])beanResourceDay.getTooltip().elementAt(i);
        out.print("<SCRIPT>");
        out.print("var id" + beanResourceDay.getBusyDevID().elementAt(i)
                + "= new Array(" + tooltip.length + ");");
        for (int ki = 0; ki < tooltip.length; ki++) {
            out.print("id" + beanResourceDay.getBusyDevID().elementAt(i)
                    + "[" + ki + "]= \"" + tooltip[ki].replace('\n', ' ') + "\";");
        }
%>
function Showtip<%=beanResourceDay.getBusyDevID().elementAt(i)%>(num) {
    var tmp="";
    for (var j = 0; j < num.length; j++) {
        var k;
        k = parseInt(num.charAt(j), 10) - 1;
        tmp += id<%=beanResourceDay.getBusyDevID().elementAt(i)%>[k] + "<BR>";
    }
    //drc(tmp,"");
    overlib(tmp, WIDTH, 350, FGCOLOR, '#FFCF00', BGCOLOR, '#000080');
}<%
        out.print("</SCRIPT>");
        String fname = "";
        int[] fillTable = (int[])beanResourceDay.getBusyTable().elementAt(i);
        for (int ci = 0; ci < fillTable.length / 2; ci++) {
            switch(fillTable[ci]) {
                case -1:fname = "#FF0000";
                    break;
                case 0: fname = "#B569C6";
                    break;
                case 1: fname = "#FF00FF";
                    break;
                case 2: fname = "#FFFF00";
                    break;
                case 3: fname = "#800080";
                    break;
                case 4: fname = "#008080";
                    break;
                case 5: fname = "#0000FF";
                    break;
            }
            if (fillTable[ci] != 6) {
%>
                <TD width="<%=pR%>" align="left" bgcolor="<%=fname%>" onmouseover=Showtip<%=beanResourceDay.getBusyDevID().elementAt(i)%>("<%=fillTable[fillTable.length / 2 + ci]%>") onmouseout="nd();">&nbsp;</TD><%
            }
            else {
%>
                <TD width="<%=pR%>" align="center">&nbsp;</TD><%
            }
        }
%>
            </TR>
        </TABLE>
        </TD>
    </TR><%
    }
    for (int kc = 0; kc < beanResourceDay.getFreeDevID().size(); kc++) {
%>
    <TR class="row<%=((i + 1) % 2 + 1)%>">
        <TD width="20%">&nbsp;<A href="javascript:doViewDetailAssignment('<%=beanResourceDay.getFreeDevID().elementAt(kc)%>')"><%=(String)beanResourceDay.getFreeDevName().elementAt(kc)%></A></TD>
        <TD width="80%">
        <TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
            <TR><%
        for (int mi = 0; mi < numDay; mi++) {
%>
                <TD width="3%" align="center">&nbsp;</TD><%
        }
%>
            </TR>
        </TABLE><%
        i++;
%>
        </TD>
    </TR><%
    }
%>
</TABLE>
<INPUT type="hidden" name="from" value="<%="01/" + (month + 1) + "/" + (year + 1900)%>">
<INPUT type="hidden" name="to" value="<%=numDay + "/" + (month + 1) + "/" + (year + 1900)%>">
</FORM>
<DIV id="overDiv" style="position: absolute; visibility: hide"></DIV>
<SCRIPT language="JavaScript" src="scripts/overlib_mini.js"></SCRIPT>
</BODY>
</HTML>