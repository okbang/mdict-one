<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ResourceManagement.*,
            fpt.dashboard.util.StringUtil.*, fpt.dashboard.util.DateUtil.*,
            java.util.Date, java.text.*"%><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ResourceWeekBean beanResourceWeek
            = (ResourceWeekBean)request.getAttribute("beanResourceWeek");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Resource Allocation</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
function doViewDetailAssignment(id) {
    var form = document.frmResourceWeek;
    form.hidAssignmentID.value = id;
    form.hidAction.value = "RM";
    form.hidActionDetail.value = "ViewAssignmentDetail";
    form.action = "DashboardServlet";
    form.submit();
}

function doFilterResource() {
    var form = document.frmResourceWeek;
    if (rangeDate()) {
        form.hidAction.value = "RM";
        form.hidActionDetail.value = form.cboType.value;
        form.action = "DashboardServlet";
        form.submit();
    }
}

function checkMonth() {
    var str = document.forms[0].Month.value;
    var strY = document.forms[0].Year.value;
    if (!isPositiveInteger(str)) {
        alert("Enter number please!");
        return false;
    }
    else {
        str = parseInt(str, 10);
        strY = parseInt(strY,10);
        if ((str < 1) || (str > 12)) {
            alert("Month must be between 1 and 12!");
            return false;
        }
        else {
            if ((str + 5) > 12) {
                strY = strY + 1;
                str = str + 5 - 12;
            }
            else {
                str = str + 5;
            }
        }
    }
}

function checkYear() {
    var str = document.forms[0].Year.value;
    if (!isPositiveInteger(str)) {
        alert("Enter number please!");
        return false;
    }
    else {
        if ((str < 1990) || (str > 2100)) {
            alert("Year to far!");
            return false;
        }
    }
}

function checkbMonth() {
    var str = document.forms[0].bMonth.value;
    var strY = document.forms[0].bYear.value;
    if (!isPositiveInteger(str)) {
        alert("Enter number please!");
        return false;
    }
    else {
        str = parseInt(str, 10);
        strY = parseInt(strY, 10);
        if ((str < 1) || (str > 12)) {
            alert("Month must be between 1 and 12!");
            return false;
        }
        else {
            if ((str + 5) > 12) {
                strY = strY + 1;
                str = str + 5 - 12;
            }
            else {
                str = str + 5;
            }
        }
    }
}

function checkbYear() {
    var str = document.forms[0].bYear.value;
    if (!isPositiveInteger(str)) {
        alert("Enter number please!");
        return false;
    }
    else {
        if ((str < 1990) || (str > 2100)) {
            alert("Year to far!");
            return false;
        }
    }
}

function rangeDate() {
    var sMonth, eMonth, sYear, eYear;
    sMonth = document.forms[0].Month.value;
    eMonth = document.forms[0].bMonth.value;
    sYear = document.forms[0].Year.value;
    eYear = document.forms[0].bYear.value;
    sMonth = parseInt(sMonth, 10);
    eMonth = parseInt(eMonth, 10);
    sYear = parseInt(sYear, 10);
    eYear = parseInt(eYear, 10);

    var MAX = 6;
    var message = "Only view data in 6 months";

    if ((sYear > eYear) || (eYear - sYear > 1)) {
        alert(message);
        return false;
    }
    else if (sYear == eYear) {
        //Check month
        if (sMonth > eMonth) {
            alert(message);
            return false;
        }
        else if (eMonth - sMonth >= MAX) {
            alert(message);
            return false;
        }
        document.forms[0].range.value = eMonth - sMonth;
        return true;
    }
    else {  //sYear < eYear
        //Check month
        if (12 - sMonth + eMonth > MAX - 1) {
            alert(message);
            return false;
        }
        else {
            document.forms[0].range.value = 12 - sMonth + eMonth;
            return true;
        }
    }
}
</SCRIPT>
</HEAD><%
    String strFrom = "";
    String strTo = "";
    int month = 0;
    int year = 0;
    int day;
    int numDay = 0;
    int range = 0;
    String strGroup = "";
    boolean bflag = false;
    Calendar calendar = Calendar.getInstance();

    range = Integer.parseInt(beanResourceWeek.getRang());
    Date now = new Date();
    month = Integer.parseInt(beanResourceWeek.getMonth()) - 1;
    year = Integer.parseInt(beanResourceWeek.getYear()) - 1900;
    calendar.set(year, month, 1);
    int endM = 0, endY = year;
    if ((month + 1 + range) > 12) {
        endY = year + 1;
        endM = month + range - 12;
    }
    else {
        endM = month + range;
    }
    numDay = DateUtil.getNumDate(endM + 1, endY + 1900);
    strGroup = "All";
%>
<BODY bgcolor="#FFFFFF"><%
    String strTitle = "Resource Allocation";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmResourceWeek">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="range">
<INPUT type="hidden" name="hidAssignmentID" value="">
<INPUT type="hidden" name="dMonth" value="<%=month + 1%>">
<TABLE border="0" width="100%" cellpadding="2" cellspacing="0" bgcolor="#e3e3ea">
    <TR>
        <TD align="left" height="10" valign="bottom" width="10%"></TD>
        <TD align="right" height="10" valign="top" width="90%">From&nbsp;<INPUT type="text" name="Month" size="2" maxlength="2" value="<%=month + 1%>" onchange="return checkMonth()"> /
        <INPUT type="text" name="Year" size="4" maxlength="4" value="<%=year + 1900%>" onchange="return checkYear()">&nbsp; To&nbsp;
        <INPUT type="text" name="bMonth" size="2" maxlength="2" value="<%=endM + 1%>" onchange="return checkbMonth()"> /
        <INPUT type="text" name="bYear" size="4" maxlength="4" value="<%=endY + 1900%>" onchange="return checkbYear()"><%
    String[] arrGroup = beanResourceWeek.getArrGroup();
    strGroup = beanResourceWeek.getCondi();
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
        &nbsp;Type&nbsp;<SELECT name="cboType">
        <OPTION value="ViewWeeklyResource" selected>View weekly report</OPTION>
        <OPTION value="ViewDailyResource">View daily report</OPTION>
        <OPTION value="ViewProjectResource">View project report</OPTION>
        </SELECT>
        &nbsp;<INPUT type="button" name="DoResFilterView" onclick="javascript:doFilterResource()" value="View"></TD>
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
<TABLE border="0" width="100%" cellspacing="1" cellpadding="0">
    <TR>
        <TD width="20%" height="45" rowspan="2" align="center" bgcolor="#006699"><FONT face="Verdana" size="1" color="#FFFFFF"><B>Developers</B></FONT></TD>
        <TD width="80%" height="24" align="center" valign="top">
        <TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
            <TR><%
    for (int j = 0; j < range + 1; j++) {
%>
                <TD height="24" align="center" width="<%=100 / 7%>%">
                <TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
                    <TR>
                        <TD height="24" align="center" colspan="4" bgcolor="#006699"><FONT face="Verdana" size="1" color="#FFFFFF"> <B><%=calendar.get(Calendar.MONTH) + 1%>/<%=calendar.get(Calendar.YEAR) + 1900%></B> </FONT></TD>
                    </TR>
                </TABLE>
                </TD>
                <%
        if (calendar.get(Calendar.MONTH) == 11) {
              calendar.roll(Calendar.YEAR, true);
        }
        calendar.roll(Calendar.MONTH, true);
    }
%>
            </TR>
        </TABLE><%
    int tmp = 0;;
    int j = 0;
    int tmonth = month;
    for (int i = 0; i < range + 1; i++) {
        tmp = tmp + DateUtil.getNumDate(tmonth + i, year + j);
        if (tmonth + i > 11) {
            tmonth = 0;
            j = 1;
        }
    }
    calendar.set(year + 1900, month, 1);
    int sT = calendar.get(Calendar.DAY_OF_WEEK);
    int eT = (tmp - (8 - sT)) % 7;
    int numWeek = (tmp - (eT + 8 - sT)) / 7 + 2;
    if (eT == 0) {
        eT = 7;
    }
    float tfloat = 600;
    float pR = tfloat / tmp;
    int sWidth = Math.round((8 - sT) * pR);
    // Increase first column width for display
    if (sT > 5) {
        // Increase number of days also
        tmp += 3;
        pR = tfloat / tmp;
        sWidth = Math.round((8 - sT + 3) * pR);
    }
    // Last column
    if (eT < 2) {
        // Increase number of days also
        tmp += 2;
        pR = tfloat / tmp;
        eT += 2;
    }
%>
        <TABLE align="left" border="0" cellspacing="1" cellpadding="0" width="100%">
            <TR>
                <TD class="pcell1" width="<%=sWidth%>">1</TD><%
    for (int i = 2; i < numWeek; i++) {
        if ((i % 2) == 0) {
%>
                <TD class="pcell2" width="<%=Math.round(7 * pR)%>"><%=i%></TD><%
        }
        else {
%>
                <TD class="pcell1" width="<%=Math.round(7 * pR)%>"><%=i%></TD><%
        }
    }
    if ((numWeek % 2) == 0) {
%>
                <TD class="pcell2" width="<%=Math.round(eT * pR)%>"></TD><%
    }
    else {
%>
                <TD class="pcell1" width="<%=Math.round(eT * pR)%>"></TD><%
    }
%>
            </TR>
        </TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="0" width="100%"><%
    int i = 0;
    for (i = 0; i < beanResourceWeek.getBusyDevID().size(); i++) {
%>
    <TR class="row<%=(i + 1) % 2 + 1%>">
        <TD width="20%">&nbsp;<A href="javascript:doViewDetailAssignment('<%=beanResourceWeek.getBusyDevID().elementAt(i)%>')"><%=(String)beanResourceWeek.getBusyDevName().elementAt(i)%></A></TD>
        <TD width="80%">
        <TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
            <TR><%
        String[] tooltip =(String[])beanResourceWeek.getTooltip().elementAt(i);
        out.print("<SCRIPT>");
        out.print("\nvar id" + beanResourceWeek.getBusyDevID().elementAt(i)
                + "= new Array(" + tooltip.length + ");");
        for (int ki = 0; ki < tooltip.length; ki++) {
            out.print("\nid" + beanResourceWeek.getBusyDevID().elementAt(i)
                    + "[" + ki + "] = \"" + tooltip[ki].replace('\n', ' ') + "\";");
        }
%>
function Showtip<%=beanResourceWeek.getBusyDevID().elementAt(i)%>(num) {
    //var temp= Math.round(num/10);
    var tmp = "";
    for (var j = 0; j < num.length; j++) {
        var k;
        k = parseInt(num.charAt(j),10)-1;
        tmp += id<%=beanResourceWeek.getBusyDevID().elementAt(i)%>[k] + "<BR>";
    }
    //drc(tmp, "");
    overlib(tmp, WIDTH, 350, FGCOLOR, '#FFCF00', BGCOLOR, '#000080');
}<%
        out.print("</SCRIPT>");
        String fname = "";
        int[] fillTable = (int[])beanResourceWeek.getBusyTable().elementAt(i);
        if (fillTable[0] != 6) {
            switch(fillTable[0]) {
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
%>
                <TD width="<%=sWidth%>" align="left" bgcolor="<%=fname%>" onmouseover=Showtip<%=beanResourceWeek.getBusyDevID().elementAt(i)%>("<%=fillTable[fillTable.length / 2]%>") onmouseout="nd();">&nbsp;</TD><%
        }
        else {
%>
                <TD width="<%=sWidth%>" align="center">&nbsp;</TD><%
        }
        for (int ci = 1; ci < fillTable.length / 2 - 1; ci++) {
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
                <TD width="<%=Math.round(7 * pR)%>" align="left" bgcolor="<%=fname%>" onmouseover=Showtip<%=beanResourceWeek.getBusyDevID().elementAt(i)%>("<%=fillTable[fillTable.length / 2 + ci]%>") onmouseout="nd();">&nbsp;</TD><%
            }
            else {
%>
                <TD width="<%=Math.round(7 * pR)%>" align="center">&nbsp;</TD><%
            }
        }
        switch (fillTable[numWeek - 1]) {
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
        if (fillTable[numWeek - 1] != 6) {
%>
                <TD width="<%=Math.round((eT) * pR)%>" align="center" bgcolor="<%=fname%>" onmouseover=Showtip<%=beanResourceWeek.getBusyDevID().elementAt(i)%>("<%=fillTable[fillTable.length / 2 + numWeek - 1]%>") onmouseout="nd();">&nbsp;</TD><%
        }
        else {
%>
                <TD width="<%=Math.round((eT) * pR)%>">&nbsp;</TD><%
        }
%>

            </TR>
        </TABLE>
        </TD>
    </TR><%
    }
    for (int kc = 0; kc < beanResourceWeek.getFreeDevID().size(); kc++) {
%>
    <TR class="row<%=(i + 1) % 2 + 1%>">
        <TD width="20%">&nbsp;<A href="javascript:doViewDetailAssignment('<%=beanResourceWeek.getFreeDevID().elementAt(kc)%>')"><%=(String)beanResourceWeek.getFreeDevName().elementAt(kc)%></A></TD>
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
<INPUT type="hidden" name="to" value="<%=numDay + "/" + (endM + 1) + "/" + (endY + 1900)%>"></FORM>
<DIV id="overDiv" style="position: absolute; visibility: hide; FONT-SIZE: xx-small; FONT-FAMILY: Verdana"></DIV>
<SCRIPT src="scripts/overlib_mini.js"></SCRIPT>
</BODY>
</HTML>