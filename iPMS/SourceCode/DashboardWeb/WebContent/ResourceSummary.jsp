<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ResourceManagement.*,
            fpt.dashboard.util.StringUtil.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ResourceSummaryBean beanResourceSummary
            = (ResourceSummaryBean)request.getAttribute("beanResourceSummary");
    String strGroupType = (String) session.getAttribute("cboGroupType");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Resource Summary</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
function doSumNextMonth() {
    var form = document.frmResourceSummary;
    if (NextMonth()) {
        form.hidAction.value = "RM";
        form.hidActionDetail.value = "ViewResource";
        form.action = "DashboardServlet";
        form.submit();
    }
}

function doSumPrevMonth() {
    var form = document.frmResourceSummary;
    if (PrevMonth()) {
        form.hidAction.value = "RM";
        form.hidActionDetail.value = "ViewResource";
        form.action = "DashboardServlet";
        form.submit();
    }
}

function doSumToMonth() {
    var form = document.frmResourceSummary;
    if (checkMonth()){
        if (checkYear()) {
            form.hidAction.value = "RM";
            form.hidActionDetail.value = "ViewResource";
            form.action = "DashboardServlet";
            form.submit();
        }
    }
}

function doViewWeeklyResource(group) {
    var form = document.frmResourceSummary;
    form.hidGroup.value = group;
    form.hidAction.value = "RM";
    form.hidActionDetail.value = "ViewWeeklyResource";
    form.action = "DashboardServlet";
    form.submit();
}

function NextMonth() {
    var num;
    num = document.forms[0].month.value;
    if (num < 12) {
        num++;
    }
    else {
        num = 1;
        document.forms[0].year.value++;
    }
    document.forms[0].month.value = num;
    return true;
}

function PrevMonth() {
    var num;
    num = document.forms[0].month.value;
    if (num > 1) {
        num--;
    }
    else {
        num = 12; document.forms[0].year.value--;
    }
    document.forms[0].month.value = num;
    return true;
}

function checkMonth() {
    var str = document.forms[0].month.value;
    if (!isPositiveInteger(str)) {
        alert("Invalid number.");
        return false;
    }
    else {
        if ((str < 1) || (str > 12)) {
            alert("Month must be between 1 and 12.");
            return false;
        }
    }
    return true;
}

function checkYear() {
    var str = document.forms[0].year.value;
    if (!isPositiveInteger(str)) {
        alert("Invalid number.");
        return false;
    }
    else {
        if ((str < 1990) || (str > 2100)) {
        alert("Year is too far.");
        return false;
        }
    }
    return true;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF"><%
    String strTitle = "Resource Summary";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmResourceSummary">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidGroup" value="">
<CENTER>
<TABLE border="0" cellspacing="1" width="80%">
    <TR>
        <TD align="center" colspan="11">Group Type&nbsp;
        <SELECT name="cboGroupType">
        <OPTION value="-1">All</OPTION>
        <OPTION value="1"<%="1".equals(strGroupType) ? " selected" : ""%>>Operation</OPTION>
        <OPTION value="0"<%="0".equals(strGroupType) ? " selected" : ""%>>BA</OPTION>
        </SELECT>&nbsp;&nbsp;
        <A href="javascript:doSumPrevMonth()">Prev month</A> | <A href="javascript:doSumNextMonth()">Next month</A>
        <INPUT type="text" name="month" size="2" maxlength="2" value="<%=beanResourceSummary.getNowMonth()%>" onchange="return checkMonth()"> /
        <INPUT type="text" name="year" size="4" maxlength="4" value="<%=beanResourceSummary.getNowYear()%>" onchange="return checkYear()"> &nbsp;
        <INPUT type="button" name="DoSumGoToMonth" onclick="javascript:doSumToMonth()" value="View"></TD>
    </TR>
    <TR class="table_header" align="center">
        <TD>Group</TD>
        <TD><B>OnSite</B></TD>
        <TD><B>In Project</B></TD>
        <TD><B>Other <BR>Assignment</B></TD>
        <TD><B>Tentative</B></TD>
        <TD><B>Training</B></TD>
        <TD><B>Off</B></TD>
        <TD><B>Available</B></TD>
        <TD><B>Permanent</B></TD>
        <TD><B>Temporary</B></TD>
        <TD><B>Total</B></TD>
    </TR><%
    for (int i = 0; i < beanResourceSummary.getSummaryList().getNumberOfRows(); i++) {
%>
    <TR class="row<%=i % 2 + 1%>">
        <TD><%
        if (!beanUserInfo.getRole().equals("3")) {
%>
        <A href="javascript:doViewWeeklyResource('<%=beanResourceSummary.getSummaryList().getCell(i, 0)%>')"> <%=beanResourceSummary.getSummaryList().getCell(i, 0)%></A> <%
        }
        else {
            String strGroup=beanResourceSummary.getSummaryList().getCell(i, 0);
            if(beanUserInfo.getGroupName().equals(strGroup)) {
%>
        <A href="javascript:doViewWeeklyResource('<%=beanResourceSummary.getSummaryList().getCell(i, 0)%>')"> <B><%=beanResourceSummary.getSummaryList().getCell(i, 0)%></B></A> <%
            }
            else {
%>
        <A href="javascript:alert('You have no permission to access this page')"> <%=beanResourceSummary.getSummaryList().getCell(i, 0)%></A> <%
            }
        }
%>
        </TD>
        <TD><%=beanResourceSummary.getSummaryList().getCell(i, 1)%></TD>
        <TD><%=beanResourceSummary.getSummaryList().getCell(i, 2)%></TD>
        <TD><%=beanResourceSummary.getSummaryList().getCell(i, 6)%></TD>
        <TD><%=beanResourceSummary.getSummaryList().getCell(i, 3)%></TD>
        <TD><%=beanResourceSummary.getSummaryList().getCell(i, 4)%></TD>
        <TD><%=beanResourceSummary.getSummaryList().getCell(i, 5)%></TD>
        <TD><%=beanResourceSummary.getSummaryList().getCell(i, 7)%></TD>
        <TD><%=beanResourceSummary.getSummaryList().getCell(i, 9)%></TD>
        <TD><%=beanResourceSummary.getSummaryList().getCell(i, 10)%></TD>
        <TD><%=beanResourceSummary.getSummaryList().getCell(i, 8)%></TD>
    </TR><%
    }
%>
</TABLE>
</CENTER>
</FORM>
</BODY>
</HTML>