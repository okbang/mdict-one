<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java"
    import=
    "java.util.*,
    java.util.Date,
    java.text.*,
    javax.servlet.*,
    fpt.dashboard.constant.DB,
    fpt.dashboard.bean.*,
    fpt.dashboard.bean.ResourceManagement.*,
    fpt.dashboard.util.StringUtil.*,
    fpt.dashboard.util.DateUtil.*,
    fpt.dashboard.util.CommonUtil.InfoList,
    fpt.dashboard.ProjectManagementTran.ejb.ProjectDashboardInfo,
    fpt.dashboard.ProjectManagementTran.ejb.AssignmentInfo"%><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ResourceProjectBean beanResourceProject =
        (ResourceProjectBean) request.getAttribute("beanResourceProject");
    String strTitle = "Resource Allocation";
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Resource Allocation</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
function doFilterResource() {
    var form = document.forms[0];
    if ((rangeDate()) && (checkMonth(document.forms[0].Month)) &&
        (checkMonth(document.forms[0].bMonth)) &&
        (checkYear(document.forms[0].Year)) && (checkYear(document.forms[0].bYear)))
    {
        form.hidAction.value = "RM";
        form.hidActionDetail.value = form.cboType.value;
        form.action = "DashboardServlet";
        form.submit();
    }
}

function doProjectDetail(id) {
    var form = document.frmResourceWeek;
    form.hidProjectID.value = id;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewProjectDetail";
    form.action = "DashboardServlet";
    form.submit();
}

var today = new Date();
var currentYear = today.getFullYear();
function checkMonth(ctlMonth) {
	var bResult = false;
	var month = ctlMonth.value;
    if (!isPositiveInteger(month)) {
        alert("Enter number please!");
    }
    else if ((month < 1) || (month > 12)) {
        alert("Month must be between 1 and 12!");
    }
    else {
    	bResult = true;
    }
    if (! bResult) {
    	ctlMonth.focus();
    }
    return bResult;
}

function checkYear(ctlYear) {
	var bResult = false;
	var year = ctlYear.value;
    if (!isPositiveInteger(year)) {
        alert("Enter number please!");
    }
    else if ((year < 2000) || (year > (currentYear + 10))) {
        alert("Year too far!");
    }
    else {
    	bResult = true;
    }
    if (! bResult) {
    	ctlYear.focus();
    }
    return bResult;
}

function rangeDate() {
	var bResult = false;
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
        bResult = false;
    }
    else if (sYear == eYear) {
        //Check month
        if (sMonth > eMonth) {
            alert(message);
            bResult = false;
        }
        else if (eMonth - sMonth >= MAX) {
            alert(message);
            bResult = false;
        }
        else {
	        //document.forms[0].range.value = eMonth - sMonth;
	        bResult = true;
        }
    }
    else {  //sYear < eYear
        //Check month
        if (12 - sMonth + eMonth > MAX - 1) {
            alert(message);
            bResult = false;
        }
        else {
            //document.forms[0].range.value = 12 - sMonth + eMonth;
            bResult = true;
        }
    }
    
    if (! bResult) {
    	document.forms[0].Month.focus();
    }
    return bResult;
}
</SCRIPT>
</HEAD><%
    int monthFrom = beanResourceProject.getFromMonth();
    int yearFrom = beanResourceProject.getFromYear();
    int monthTo = beanResourceProject.getToMonth();
    int yearTo = beanResourceProject.getToYear();
    String strCurrentGroup = beanResourceProject.getGroupName();
    String strGroup;
    // Group combo box
    ArrayList arrGroups = beanResourceProject.getGroupsList();
    ArrayList arrMonths = beanResourceProject.getMonthsList();
    Calendar calFrom = beanResourceProject.getFromCalendar();
    Calendar calTo = beanResourceProject.getToCalendar();
    Calendar calMonth;
    int nGroupDevelopers = beanResourceProject.getGroupDevelopers();
    int nMaxDevelopers = beanResourceProject.getMaxDevelopers();
    int nFirstWeekDays = beanResourceProject.getFirstWeekDays();
    int nLastWeekDays = beanResourceProject.getLastWeekDays();
    long nDays = CalendarUtil.getDaysBetween(calFrom, calTo);
    int nWeeks = beanResourceProject.getWeeksList().size();
    // For display progress bars
    int[] arrWidth = new int[nWeeks];

    InfoList reportList = beanResourceProject.getReportList();
    InfoList listProject;
    InfoList listWeek;
    ArrayList arrAssignments = beanResourceProject.getAssignmentsList();

    ProjectDashboardInfo projectInfo;
    AssignmentInfo assignmentInfo;
    int[] arr_GroupSum = beanResourceProject.getGroupSum();
    int[] arr_GroupAllocated = beanResourceProject.getGroupAllocated();
    int[] arr_GroupTentative = beanResourceProject.getGroupTentative();
    String strSum;
    int iPr, iWk, iAs;
    CalendarUtil calUtil = new CalendarUtil();

    // All groups report:
    // Only displays allocation graph cell if its height is higher than lower bound
    int nLowerBound = -1;
    final int MIN_DISPLAY = 5;
    final int FIRST_BOUND = 20;
    int iMin = 0;
    if (nGroupDevelopers > FIRST_BOUND) {
        // Calculate minimum assigned week
        for (iWk = 1; iWk < arr_GroupAllocated.length; iWk++) {
            if (arr_GroupAllocated[iWk] < arr_GroupAllocated[iMin]) {
                iMin = iWk;
            }
        }
        nLowerBound = arr_GroupAllocated[iMin] - 1;
    }
    if (nLowerBound >= nGroupDevelopers) {
        nLowerBound = nGroupDevelopers - 1;
    }
    if (nLowerBound >= MIN_DISPLAY) {
        nLowerBound -= MIN_DISPLAY;
    }

    int nCellHeight;
    int nMaximum = (nGroupDevelopers > nMaxDevelopers) ? nGroupDevelopers : nMaxDevelopers;
    int nHeight = nMaximum - nLowerBound;
    if (nHeight <= 25) {
        nCellHeight = 14;
    }
    else if (nHeight <= 50) {
        nCellHeight = 7;
    }
    else if (nHeight <= 100) {
        nCellHeight = 3;
    }
    else {
        nCellHeight = 2;
    }
    String strHeight = Integer.toString(nCellHeight) + "px";
    float tfloat = 600;
    float pR = tfloat / nDays;

    // For display progress bars's size correctly, set number of days of week
    // to 6 if it's less than 6.
    if (nFirstWeekDays < 6) {
        arrWidth[0] = Math.round(6 * pR);
    }
    else {
        arrWidth[0] = Math.round(nFirstWeekDays * pR);
    }
    if (nLastWeekDays < 6) {
        arrWidth[arrWidth.length - 1] = Math.round(6 * pR);
    }
    else {
        arrWidth[arrWidth.length - 1] = Math.round(nLastWeekDays * pR);
    }
    for (iWk = 1; iWk < arrWidth.length - 1; iWk++) {
        arrWidth[iWk] = Math.round(7 * pR);
    }

    out.write("\n<SCRIPT language='javascript'>\n");
    out.write("var arrMap=new Array();\n");
    for (iAs = 0; iAs < arrAssignments.size(); iAs++) {
        out.write("arrMap[" + iAs + "]='" +
                ((AssignmentInfo) arrAssignments.get(iAs)).getTooltip() + "';\n");
    }

    out.write("var a=new Array();\n");
    for (iPr = 0; iPr < reportList.size(); iPr++) {
        listProject = (InfoList) reportList.get(iPr);
        out.write("a[" + iPr + "]=new Array();\n");
        //projectInfo = (ProjectDashboardInfo) listProject.getInfo();
        for (iWk = 0; iWk < listProject.size(); iWk++) {
            listWeek = (InfoList) listProject.get(iWk);
            out.write("a[" + iPr + "][" + iWk + "]=new Array();\n");
            for (iAs = 0; iAs < listWeek.size(); iAs++) {
                out.write("a[" + iPr + "][" + iWk + "][" + iAs + "]=new Array(1);\n");
                out.write("a[" + iPr + "][" + iWk + "][" + iAs + "][0]=" +
                            ((Integer) listWeek.get(iAs)).intValue() + ";\n");

                //assignmentInfo = (AssignmentInfo) listWeek.get(iAs);
                //out.write("arrInfo[" + iPr + "][" + iWk + "][" + iAs + "] = new Array(1);\n");
                //out.write("arrInfo[" + iPr + "][" + iWk + "][" + iAs + "][0] = '" + assignmentInfo.getTooltip() + "';\n");

                //out.write("arrInfo[" + iPr + "][" + iWk + "][" + iAs + "][1] = " + assignmentInfo.getType() + ";\n");
            }
        }
    }
    out.write("</SCRIPT>\n");

/*    String strFilePath = request.getRealPath("/scripts/") + "assignments.js";
    java.io.File scriptFile = new java.io.File(strFilePath);
    scriptFile.createNewFile();
    java.io.FileWriter fileWriter = new java.io.FileWriter(scriptFile);
    //fileWriter.write("\n<SCRIPT language='javascript'>\n");
    fileWriter.write("var arrInfo = new Array();\n");
    for (iPr = 0; iPr < reportList.size(); iPr++) {
        listProject = (InfoList) reportList.get(iPr);
        fileWriter.write("arrInfo[" + iPr + "] = new Array();\n");
        //projectInfo = (ProjectDashboardInfo) listProject.getInfo();
        for (iWk = 0; iWk < listProject.size(); iWk++) {
            listWeek = (InfoList) listProject.get(iWk);
            fileWriter.write("arrInfo[" + iPr + "][" + iWk + "] = new Array();\n");
            for (iAs = 0; iAs < listWeek.size(); iAs++) {
                assignmentInfo = (AssignmentInfo) listWeek.get(iAs);
                fileWriter.write("arrInfo[" + iPr + "][" + iWk + "][" + iAs + "] = new Array(1);\n");
                fileWriter.write("arrInfo[" + iPr + "][" + iWk + "][" + iAs + "][0] = \"" + assignmentInfo.getTooltip() + "\";\n");
            }
        }
    }
    //fileWriter.write("</SCRIPT>\n");
    fileWriter.close();*/
%>
<BODY bgcolor="#FFFFFF">
<%@    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmResourceWeek">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidProjectID" value="">
<TABLE border="0" width="100%" cellpadding="2" cellspacing="0" bgcolor="#e3e3ea">
    <TR>
        <TD align="left" height="10" valign="bottom" width="10%"></TD>
        <TD align="right" height="10" valign="top" width="90%">
        From&nbsp;<INPUT type="text" name="Month" size="2" maxlength="2" value="<%=beanResourceProject.getFromMonth()%>" onchange="//return checkMonth(this.value)"> /
        <INPUT type="text" name="Year" size="4" maxlength="4" value="<%=beanResourceProject.getFromYear()%>" onchange="//return checkYear(this.value)">
        &nbsp;To&nbsp;<INPUT type="text" name="bMonth" size="2" maxlength="2" value="<%=beanResourceProject.getToMonth()%>" onchange="//return checkMonth(this.value)"> /
        <INPUT type="text" name="bYear" size="4" maxlength="4" value="<%=beanResourceProject.getToYear()%>" onchange="//return checkYear(this.value)">
        &nbsp;Group&nbsp;<SELECT name="cboGroup" <%
    if (beanUserInfo.getRole().equals("4")) {
        out.print("disabled");
    }
%>>
<%
    for (int i = 0; i < arrGroups.size(); i++) {
        strGroup = (String) arrGroups.get(i);
        if (strGroup.equals(strCurrentGroup)) {
%>
            <OPTION value="<%=strGroup%>" selected><%=strGroup%></OPTION><%
        }
        else {
%>
            <OPTION value="<%=strGroup%>"><%=strGroup%></OPTION><%
        }
    }
%>
        </SELECT>
        &nbsp;Type&nbsp;<SELECT name="cboType">
        <OPTION value="ViewWeeklyResource">View weekly report</OPTION>
        <OPTION value="ViewDailyResource">View daily report</OPTION>
        <OPTION value="ViewProjectResource" selected>View project report</OPTION>
        </SELECT>
        &nbsp;<INPUT type="button" name="DoResFilterView" onclick="javascript:doFilterResource()" value="View"></TD>
    </TR>
</TABLE>
<TABLE border="0" width="100%" cellspacing="3" cellpadding="0">
    <TR>
    </TR>
</TABLE>
<TABLE border="0" width="100%" cellspacing="1" cellpadding="0">
    <TR>
        <TD width="15%" height="45" rowspan="2" align="center" bgcolor="#006699"><FONT face="Verdana" size="1" color="#FFFFFF"><B>Project Allocations</B></FONT></TD>
        <TD width="85%" height="24" align="center" valign="top">
        <TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
            <TR><%
    for (int i = 0; i < arrMonths.size(); i++) {
        calMonth = (Calendar) arrMonths.get(i);
%>
                <TD height="24" align="center" width="<%=arrMonths.size() / 7%>%">
                <TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
                    <TR>
                        <TD height="24" align="center" colspan="4" bgcolor="#006699"><FONT face="Verdana" size="1" color="#FFFFFF"> <B><%=calMonth.get(Calendar.MONTH) + 1%>/<%=calMonth.get(Calendar.YEAR)%></B> </FONT></TD>
                    </TR>
                </TABLE>
                </TD>
<%
    }
%>
            </TR>
        </TABLE>
        <TABLE align="left" border="0" cellspacing="1" cellpadding="0" width="100%">
            <TR>
<%
    for (int i = 0; i < nWeeks; i++) {
        if ((i % 2) == 0) {
%>
                <TD class="pcell1" width="<%=arrWidth[i]%>"><%=i + 1%></TD><%
        }
        else {
%>
                <TD class="pcell2" width="<%=arrWidth[i]%>"><%=i + 1%></TD><%
        }
    }
%>
            </TR>
        </TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
    <TR class="summary">
        <TD width="15%">&nbsp;<%=beanResourceProject.getGroupName()%></TD>
        <TD width="85%">
        <TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
            <TR align="center" class="summary">
<%
        for (iWk = 0; iWk < arr_GroupSum.length; iWk++) {
            strSum = (arr_GroupSum[iWk] > 0 ? Integer.toString(arr_GroupSum[iWk]) : "");
%>
                <TD <%=((arr_GroupSum[iWk] > 0) ? "class=\"pcell\"" : "")%> width="<%=arrWidth[iWk]%>"><%=strSum%></TD><%
        }
%>
            </TR>
        </TABLE>
        </TD>
    </TR>
<%
    for (iPr = 0; iPr < reportList.size(); iPr++) {
        listProject = (InfoList) reportList.get(iPr);
        projectInfo = (ProjectDashboardInfo) listProject.getInfo();
%>
    <TR class="row<%=(iPr + 1) % 2 + 1%>">
        <TD width="15%">&nbsp;<A href="javascript:doProjectDetail('<%=projectInfo.getProjectID()%>')" title="<%=projectInfo.getName()%>"><%=(projectInfo.getCode().length() > 15 ? (projectInfo.getCode().substring(0, 12) + "...") : projectInfo.getCode())%></A></TD>
        <TD width="85%">
        <TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
            <TR align="center"><%
        for (iWk = 0; iWk < listProject.size(); iWk++) {
            listWeek = (InfoList) listProject.get(iWk);
            strSum = "&nbsp;";
            if (listWeek.size() > 0) {
                strSum = Integer.toString(((Integer) listWeek.getInfo()).intValue());
                //strSum = Integer.toString(listWeek.size());
            }
%>
                <TD <%=((listWeek.size() > 0) ? "class=\"pcell\"" : "")%> width="<%=arrWidth[iWk]%>" onmouseover="return showTooltip(<%=iPr%>,<%=iWk%>,-1)" onmouseout="nd()"><%=strSum%></TD><%
        }
%>
            </TR>
        </TABLE>
        </TD>
    </TR>
<%
    }
%>
</TABLE>
<BR>
<FONT size="5">Allocation Graph</FONT>
<TABLE border="0" width="100%" cellspacing="3" cellpadding="0">
    <TR>
        <TD width="8%" align="right">Allocated</TD>
        <TD width="8%" bgcolor="#FFFF00">&nbsp;</TD>
        <TD width="8%" align="right">Tentative</TD>
        <TD width="8%" bgcolor="#800080">&nbsp;</TD>
        <TD width="8%" align="right">&nbsp;</TD>
        <TD width="8%">&nbsp;</TD>
        <TD width="8%" align="right">&nbsp;</TD>
        <TD width="8%">&nbsp;</TD>
        <TD width="9%" align="right">&nbsp;</TD>
        <TD width="9%">&nbsp;</TD>
        <TD width="9%" align="right">&nbsp;</TD>
        <TD width="9%">&nbsp;</TD>
    </TR>
</TABLE>
<TABLE border="0" width="100%" cellspacing="1" cellpadding="0">
    <TR>
        <TD width="15%" height="45" rowspan="2" align="center" bgcolor="#006699"><FONT face="Verdana" size="1" color="#FFFFFF"><B>Group Allocations</B></FONT></TD>
        <TD width="85%" height="24" align="center" valign="top">
        <TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
            <TR><%
    for (int i = 0; i < arrMonths.size(); i++) {
        calMonth = (Calendar) arrMonths.get(i);
%>
                <TD height="24" align="center" width="<%=arrMonths.size() / 7%>%">
                <TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
                    <TR>
                        <TD height="24" align="center" colspan="4" bgcolor="#006699"><FONT face="Verdana" size="1" color="#FFFFFF"> <B><%=calMonth.get(Calendar.MONTH) + 1%>/<%=calMonth.get(Calendar.YEAR)%></B> </FONT></TD>
                    </TR>
                </TABLE>
                </TD>
<%
    }
%>
            </TR>
        </TABLE><%
%>
        <TABLE align="left" border="0" cellspacing="1" cellpadding="0" width="100%">
            <TR>
<%
    for (int i = 0; i < nWeeks; i++) {
        if ((i % 2) == 0) {
%>
                <TD class="pcell1" width="<%=arrWidth[i]%>"><%=i + 1%></TD><%
        }
        else {
%>
                <TD class="pcell2" width="<%=arrWidth[i]%>"><%=i + 1%></TD><%
        }
    }
%>
            </TR>
        </TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
    <TR class="summary">
        <TD width="15%">
        <TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
<%
        for (int iDev = nMaximum; iDev > nGroupDevelopers; iDev--) {
%>
            <TR><TD height="<%=strHeight%>"></TD><TD></TD></TR>
<%
        }
%>
            <TR><TD height="<%=strHeight%>"></TD><TD rowspan="<%=nGroupDevelopers - nLowerBound - 1%>" valign="top" align="right"><B>Total staffs: <%=nGroupDevelopers%></B></TD></TR>
<%
        for (int iDev = nGroupDevelopers - 2; iDev > nLowerBound; iDev--) {
%>
            <TR><TD height="<%=strHeight%>"></TD></TR>
<%
        }
%>
        </TABLE>
        </TD>
        <TD width="85%">
        <TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
<%
        int iDev;
        String strBgcolor;
        final String COLOR_ALLOCATED = "#FFFF00";
        final String COLOR_TENTATIVE = "#800080";
        String strTooltip = " onmouseover=\"";
        String strHide = " onmouseout=\"nd()\"";
        String strShowType;
        for (iDev = nMaximum - 1; iDev > nLowerBound; iDev--) {
            // Create a bound line for group developers
            if (iDev == (nGroupDevelopers - 1)) {
%>
            <TR>
<%
                for (iWk = 0; iWk < arr_GroupSum.length; iWk++) {
%>
                <TD height="1px" bgcolor="red" onmouseover="showTotal(<%=nGroupDevelopers%>)" onmouseout="nd()">
<%
                }
%>
            </TR>
<%
            }
%>
            <TR>
<%
            String strEmptyColor = ((iDev % 2) == 0) ? "#b1b9cd" : "#c0c7d6";
            for (iWk = 0; iWk < arr_GroupSum.length; iWk++) {
                if (iDev >= arr_GroupSum[iWk]) {
                    strBgcolor = " bgcolor=\"" + strEmptyColor + "\"";
                    strShowType = "";
                }
                else if (iDev >= arr_GroupAllocated[iWk]) {
                    strBgcolor = " bgcolor=\"" + COLOR_TENTATIVE + "\"";
                    strShowType = strTooltip + "showType(" +
                                  DB.ASSIGNMENT_TYPE_TENTATIVE + "," + arr_GroupTentative[iWk] + ")\"" +
                                  strHide;
                }
                else {
                    strBgcolor = " bgcolor=\"" + COLOR_ALLOCATED + "\"";
                    strShowType = strTooltip + "showType(" +
                                  DB.ASSIGNMENT_TYPE_ALLOCATED + "," + arr_GroupAllocated[iWk] + ")\"" +
                                  strHide;
                }
%>
                <TD<%=strBgcolor%><%=strShowType%> width="<%=arrWidth[iWk]%>" height="<%=strHeight%>"></TD><%
            }
%>
            </TR>
<%
        }
%>
        </TABLE>
        </TD>
    </TR>
</TABLE>
</FORM>
<DIV id="overDiv" style="position: absolute; visibility: hide; FONT-SIZE: xx-small; FONT-FAMILY: Verdana"></DIV>
<SCRIPT src="scripts/overlib_mini.js"></SCRIPT>
<%//out.print("<SCRIPT src=\"" + strFilePath + "\"></SCRIPT>");%>
<SCRIPT language="javascript">
// Show an assignment tooltip or project assignment tooltip of a week
// Parameter:
//      iAs < 0: show project tooltip
//      iAs >= 0: show an assignment tooltip
function showTooltip(iPr, iWk, iAs) {
    var tmp = "";
    if (iAs >= 0) {
        tmp = arrMap[ a[iPr][iWk][iAs][0] ];
    }
    else {
        for (var i = 0; i < a[iPr][iWk].length; i++) {
            tmp += arrMap[ a[iPr][iWk][i][0] ] + "<BR>";
        }
    }
    if (tmp.length > 0) {
        //drc(tmp, "");
        return overlib(tmp, WIDTH, 350, FGCOLOR, '#FFCF00', BGCOLOR, '#000080');
    }
}

function showType(type, total) {
    var tmp = "";
    if (type == <%=DB.ASSIGNMENT_TYPE_ALLOCATED%>) {
        tmp += "Allocated: " + total;
    }
    else if (type == <%=DB.ASSIGNMENT_TYPE_TENTATIVE%>) {
        tmp += "Tentative: " + total;
    }
    if (tmp.length > 0) {
        //drc(tmp, "");
        return overlib(tmp, WIDTH, 150, FGCOLOR, '#FFCF00', BGCOLOR, '#000080');
    }
}

function showTotal(total) {
    var tmp = "";
    if (total > 0) {
        tmp += "Total staffs: " + total;
    }
    if (tmp.length > 0) {
        //drc(tmp, "");
        return overlib(tmp, WIDTH, 150, FGCOLOR, '#FFCF00', BGCOLOR, '#000080');
    }
}
</SCRIPT>
</BODY>
</HTML>