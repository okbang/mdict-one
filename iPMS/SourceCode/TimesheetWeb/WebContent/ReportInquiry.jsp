<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, 
			fpt.timesheet.bean.*,
			fpt.timesheet.constant.*,
            fpt.timesheet.bean.Report.InquiryReportBean,
            fpt.timesheet.framework.util.CommonUtil.*,
            fpt.timesheet.framework.util.StringUtil.StringMatrix,
            java.util.Collection, java.util.Iterator" 
%>
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    InquiryReportBean beanInquiryReport = (InquiryReportBean)request.getAttribute("beanInquiryReport");
%>
<HTML>
<HEAD>
<TITLE>Inquiry Report</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT language="JavaScript" src="scripts/popcalendar.js" type="text/javascript"></SCRIPT>
</HEAD>
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H1><IMG align="top" src="image/tit_ViewAllTimesheet.gif"></H1>

<BODY bgcolor="#336699" leftmargin="0" topmargin="0">
<FORM method="post" action="TimesheetServlet" name="frmReportInquiry">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="hidTypeOfView" value="InquiryReport">
<INPUT type="hidden" name="TimesheetID">
<INPUT type="hidden" name="hidCurrentPage" value='<%=beanInquiryReport.getCurrentPage()%>'>
<INPUT type="hidden" name="hidTotalPage" value='<%=beanInquiryReport.getTotalPage()%>'>
<INPUT type="hidden" name="hidTotalTS" value='<%=beanInquiryReport.getTotalTimesheet()%>'>

&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName() %></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR>
<%
    // Common variable
    int i = 0;
    int maxrows = 0;
    int tmp = 0;
    String tmpStr = "";
    String gItemValue = "";
    String gItemDisplay = "";
    String strDisabled = "";
%>
<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" align="center">
    <TR>
        <!-- PROJECT -->
        <TD width="12%"><STRONG><FONT class="label1" color="#ffffff">Project</FONT></STRONG></TD>
        <TD width="27%"><SELECT size="1" name="ircboProject" class="SmallCombo" tabindex="1">
            <!-- Fill Data -->
<%
    // get selected value
    tmp = beanInquiryReport.getProject();
    StringMatrix mtxProject = beanInquiryReport.getProjectList();
    maxrows = mtxProject.getNumberOfRows();
    i = 0;
    while (i < maxrows) {
        gItemValue = mtxProject.getCell(i, 0);
        gItemDisplay = mtxProject.getCell(i, 1);
        if (!(gItemValue.equalsIgnoreCase(tmp + ""))) {
%>
            <OPTION value="<%=gItemValue%>"><%=gItemDisplay%></OPTION>
<%
        }
        else {
%>
            <OPTION selected value="<%=gItemValue%>"><%=gItemDisplay%></OPTION>
<%
        }
        i++;
    }
%>
        </SELECT></TD>
        <!-- FROM DATE  -->
        <TD width="12%"><STRONG><FONT color="#ffffff" class="label1">From Date</FONT></STRONG></TD>
        <TD width="26%">
            <INPUT type="text" name="FromDate" value="<%=beanInquiryReport.getFromDate()%>" size="20" class="SmallTextbox" maxlength="8">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, FromDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <!-- SORT BY  -->
        <TD width="10%"><STRONG><FONT color="#ffffff" class="label1">Sort by</FONT></STRONG></TD>
        <TD width="20%"><SELECT name="Sortby" size="1" class="VerySmallCombo">
            <!-- Fill Data -->
<%
    // get selected value
    tmp = beanInquiryReport.getSortby();
    StringMatrix mtxSortby = beanInquiryReport.getSortbyList();
    maxrows = mtxSortby.getNumberOfRows();
    i = 0;
    while (i < maxrows) {
        gItemValue = mtxSortby.getCell(i, 0);
        gItemDisplay = mtxSortby.getCell(i, 1);
        if (!(gItemValue.equalsIgnoreCase(tmp + ""))) {
%>
            <OPTION value="<%=gItemValue%>"><%=gItemDisplay%></OPTION>
<%
        }
        else {
%>
            <OPTION selected value="<%=gItemValue%>"><%=gItemDisplay%></OPTION>
<%
        }
        i++;
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <!-- STATUS -->
        <TD width="12%" align="left"><STRONG><FONT color="#ffffff" class="label1">Status</FONT></STRONG></TD>
        <TD width="27%"><SELECT name="ircboStatus" size="1" class="SmallCombo" onchange="javascript:cboStatusChaged()">
            <!-- Fill Data -->
<%
    // get selected value
    tmp = beanInquiryReport.getStatus();
    if ((tmp == 1) || (tmp == 5)) { //Unapproved, Rejected
        strDisabled = "disabled";
    }
    StringMatrix mtxStatus = beanInquiryReport.getStatusList();
    maxrows = mtxStatus.getNumberOfRows();
    i = 0;
    while (i < maxrows) {
        gItemValue = mtxStatus.getCell(i, 0);
        gItemDisplay = mtxStatus.getCell(i, 1);
        if (!(gItemValue.equalsIgnoreCase(tmp + ""))) {
%>
            <OPTION value="<%=gItemValue%>"><%=gItemDisplay%></OPTION>
<%
        }
        else {
%>
            <OPTION selected value="<%=gItemValue%>"><%=gItemDisplay%></OPTION>
<%
        }
        i++;
    }
%>
        </SELECT></TD>
        <!-- TO DATE -->
        <TD width="12%" align="left"><STRONG><FONT color="#ffffff" class="label1">To Date</FONT></STRONG></TD>
        <TD width="26%">
            <INPUT type="text" size="20" class="SmallTextbox" name="ToDate" value="<%=beanInquiryReport.getToDate()%>" maxlength="8">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, ToDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <!-- APPROVER -->
        <TD width="10%"><STRONG><FONT color="#ffffff" class="label1">Approver</FONT></STRONG></TD>
        <TD width="16%"><INPUT type="text" size="10" class="SmallTextbox" name="Approver" value="<%=beanInquiryReport.getApprover()%>" onkeypress="toUpperCase()" <%=strDisabled%>></TD>
    </TR>
    <TR>
        <!-- GROUP -->
<%
    strDisabled = "";
    if (beanUserInfo.getStatus().equals("3")) {
        strDisabled = "disabled"; //External
    }
    // get selected value
    tmpStr = beanInquiryReport.getGroup();
%>
        <TD width="12%" align="left"><STRONG><FONT color="#ffffff" class="label1">Group</FONT></STRONG></TD>
        <TD width="25%"><SELECT name="ircboGroup" size="1" class="SmallCombo" <%= strDisabled%>>
            <!-- Fill Data --><%
    StringMatrix mtxGroup = beanInquiryReport.getGroupList();
    maxrows = mtxGroup.getNumberOfRows();
    i = 0;
    while (i < maxrows) {
        gItemValue = mtxGroup.getCell(i, 0);
        gItemDisplay = mtxGroup.getCell(i, 1);
        if (!(gItemValue.equalsIgnoreCase(tmpStr))) {
%>
            <OPTION value="<%=gItemValue%>"><%=gItemDisplay%></OPTION>
<%
        }
        else {
%>
            <OPTION selected value="<%=gItemValue%>"><%=gItemDisplay%></OPTION>
<%
        }
        i++;
    }
%>
        </SELECT> <!-- incase disabling Group combo, it will make request.getParameter() return a NULL value -->
        <INPUT type="hidden" name="hidGroup" value="<%= tmpStr%>"><!-- don't delete --></TD>
        <!-- Account -->
        <TD width="12%" align="left"><STRONG><FONT color="#ffffff" class="label1">Account</FONT></STRONG></TD>
        <TD width="26%"><INPUT type="text" size="20" class="SmallTextbox" name="Account" value="<%=beanInquiryReport.getAccount()%>" onkeypress="toUpperCase()"></TD>
        <TD width="10%"></TD>
        <TD width="20%"><INPUT type="button" name="Search" class="Button" onclick='javascript:doSearch()' value="  Search  "></TD>
    </TR>
</TABLE>
<FONT color="#ffffff" class="label1">&nbsp;Date format:&nbsp;</FONT><FONT class="labelDate" color="yellow">&nbsp;(mm/dd/yy)</FONT>
<HR noshade size="1"><%
    StringMatrix mtxTimesheet = null;
    maxrows = 0;
    if (beanInquiryReport.getTimesheetList() != null) {
        mtxTimesheet = beanInquiryReport.getTimesheetList();
        maxrows = mtxTimesheet.getNumberOfRows();
    }
%>
<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" bgcolor="#336699">
    <TR>
<%
    if (beanInquiryReport.getTotalTimesheet() > 0) {
    	//System.out.println("@HanhTN -- beanInquiryReport.getTotalTimesheet() == " + beanInquiryReport.getTotalTimesheet());
    
%>
        <TD height="10" valign="bottom"><%
        int MAX = 50;
        int nPage = beanInquiryReport.getCurrentPage();
        if (maxrows > 0) {
%>
        <FONT color="#ffffff" class="label1">Result&nbsp;</FONT>
        <FONT color="yellow" size="-1"><%=nPage * MAX + 1%> - <%=nPage * MAX + maxrows%></FONT>
        <FONT color="#ffffff" class="label1"> of </FONT>
        <FONT color="yellow" size="-1"><%=beanInquiryReport.getTotalTimesheet()%></FONT>
        <FONT color="#ffffff" class="label1"> records in </FONT>
        <FONT color="yellow" size="-1"><%=beanInquiryReport.getTotalPage()%></FONT>
        <FONT color="#ffffff" class="label1"> page(s)</FONT><%
        }
        else {
%>
        <B> <FONT color="#ffffff" class="label1">Result&nbsp;</FONT> <FONT color="#ffffff" class="label1">0 - 0 </FONT> <FONT color="yellow" size="-1"> <FONT color="#ffffff" class="label1"> of </FONT> <FONT color="yellow" size="-1">0</FONT> <FONT color="#ffffff" class="label1"> records in </FONT> <FONT color="yellow" size="-1">0</FONT><FONT color="#ffffff" class="label1"> page(s)</FONT></B><%
        }
%>
        </TD>
        <TD align="right" height="10" valign="top"><%
        if (beanInquiryReport.getTotalTimesheet() > 50) {
            if (beanInquiryReport.getCurrentPage() > 0) {
%>
        <A class="HeaderMenu" href="javascript:doViewTimesheet('Prev')">Prev</A>&nbsp;&nbsp;&nbsp;<%
            }
            if (beanInquiryReport.getCurrentPage() + 1 < beanInquiryReport.getTotalPage()) {
%>
        <A class="HeaderMenu" href="javascript:doViewTimesheet('Next')">Next</A>&nbsp;&nbsp;&nbsp;<%
            }
%>
        <INPUT type="text" onkeypress="javascript:numberAllowed()" size="4" name="txtPage" maxlength="10" value='<%=beanInquiryReport.getCurrentPage() +1%>' class="flatTextbox">
        <INPUT type="button" name="GoPage" class="Button" onclick='javascript:doGoPage()' value="Go"></TD><%
        }
    }
    else {
%>
        <TD width="20%" height="10" valign="bottom"><FONT color="#ffffff" class="label1">Total:&nbsp;</FONT><FONT color="yellow" size="-1">0</FONT></TD><%
    }
%>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="3" cellspacing="0" bgcolor="#336699" width="100%">
    <COLGROUP>
        <COL width="10%">
        <COL width="10%">
        <COL width="8%">
        <COL width="30%">
        <COL width="3%">
        <COL width="12%">
        <COL width="12%">
        <COL width="12%">
        <COL width="3%">
        <COL width="10%">
        <COL width="12%">
    <TR>
        <TD class="TableHeader1">Project</TD>
        <TD class="TableHeader1">Account</TD>
        <TD class="TableHeader1">Date</TD>
        <TD class="TableHeader1">Description</TD>
        <TD class="TableHeader1">Time</TD>
        <TD class="TableHeader1">Process</TD>
        <TD class="TableHeader1">Work</TD>
        <TD class="TableHeader1">Product</TD>
        <TD class="TableHeader1">KPA</TD>
        <TD class="TableHeader1">Approver</TD>
        <TD class="TableHeader1">Log&nbsp;Time</TD>
    </TR><%
    if (mtxTimesheet != null) {
        i = 0;
        while (i < maxrows ) {
            String sProject = mtxTimesheet.getCell(i, 0);
            String sAccount = mtxTimesheet.getCell(i, 1);
            String sDate = mtxTimesheet.getCell(i, 2);
            String sDescription = mtxTimesheet.getCell(i, 3);
            String sDuration = mtxTimesheet.getCell(i, 4);
            String sProcess = mtxTimesheet.getCell(i, 5);
            String sType = mtxTimesheet.getCell(i, 6);
            String sProduct = mtxTimesheet.getCell(i, 7);
            String sKpa = mtxTimesheet.getCell(i, 8);
            String sApprover = mtxTimesheet.getCell(i, 9);
            String strClass = ((i % 2) == 1) ? "Row2" : "Row1";
            String sTimesheetID = mtxTimesheet.getCell(i, 10);
            String sProjectType = mtxTimesheet.getCell(i, 11);
            String sProjectStatus = mtxTimesheet.getCell(i, 12);
            String sTimeStamp = mtxTimesheet.getCell(i, 14);
%>
    <TR><%
            if ("0".equals(sProjectStatus) || "3".equals(sProjectStatus)) {
%>
        <TD class="<%=strClass%>"><A class="grayLink" href="javascript:doUpdate('<%=sTimesheetID%>')"><%=sProject%></A></TD><%
            }
            else {
%>
        <TD class="<%=strClass%>" height="25"><%=sProject%></TD><%
            }
%>
        <TD class="<%=strClass%>"><%=sAccount%></TD>
        <TD class="<%=strClass%>" align="center"><%=sDate%></TD>
        <TD class="<%=strClass%>"><%=CommonUtil.correctHTMLError(sDescription)%></TD>
        <TD class="<%=strClass%>" align="center"><%=sDuration%></TD>
        <TD class="<%=strClass%>"><%=sProcess%></TD>
        <TD class="<%=strClass%>"><%=sType%></TD>
        <TD class="<%=strClass%>"><%=sProduct%></TD>
        <TD class="<%=strClass%>">&nbsp;<%=sKpa%></TD>
        <TD class="<%=strClass%>">&nbsp;<%=sApprover%></TD>
        <TD class="<%=strClass%>"><%=sTimeStamp%></TD>
    </TR>
<%
            i++;
        }
    }
%>
</TABLE>
<P align="center">
    <%if (beanInquiryReport.getTotalTimesheet() > 0) {
    %>
    <INPUT type="button" name="Export" onclick='javascript:doExport()' value="Export List" class="Button">
    <%}
    %>
</P>
</FORM>
<b>
<SCRIPT language="javascript">
function doSearch() {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    //modified by MinhPT 03Oct13 for check nCurrentPage > nTotalPage ?
    if (form.txtPage != null)
        form.hidCurrentPage.value = form.txtPage.value - 1;
    else 
        form.hidCurrentPage.value = "0";
    form.hidAction.value = "RA";
    form.hidActionDetail.value = "InquiryReport";
    form.action = "TimesheetServlet";
    form.submit();
}

function doUpdate(id) {
    var form = document.forms[0];
    var strRole = "";
    strRole = form.Role.value;
    clearInvalidDate(form);
    if (strRole.charAt(4) == '1') {
        form.TimesheetID.value = id;
        form.hidAction.value = "AA";
        form.hidActionDetail.value = "UpdateQA";
        form.action = "TimesheetServlet";
        form.submit();
     }
     else {
        alert('Unauthorized access. You are not QA');
    }
}

function doViewTimesheet(to) {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    if (to == "Next") {
        if (Next()) {
            form.hidAction.value = "RA";
            form.hidActionDetail.value = "InquiryReport";
            form.action = "TimesheetServlet";
            form.submit();
        }
    }
    else if (to == "Prev") {
        if (Prev()) {
            form.hidAction.value = "RA";
            form.hidActionDetail.value = "InquiryReport";
            form.action = "TimesheetServlet";
            form.submit();
        }
    }
}

function doGoPage() {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    if (isNonNegativeInteger(form.txtPage.value - 1)) {
        if ((parseInt(form.txtPage.value)) > parseInt(form.hidTotalPage.value)) {
            alert("Invalid page.");
            return false;
        }
        else {
            form.hidCurrentPage.value = form.txtPage.value - 1;
            form.hidAction.value = "RA";
            form.hidActionDetail.value = "InquiryReport";
            form.action = "TimesheetServlet";
            form.submit();
        }
    }
    else {
        alert("Invalid number");
        form.txtPage.focus();
        form.txtPage.select();
        return false;
    }
}

function Next() {
    var num;
    num = parseInt(document.forms[0].hidCurrentPage.value);
    if (num<(document.forms[0].hidTotalPage.value - 1)) {
        num++;
        document.forms[0].hidCurrentPage.value = num;
        return true;
    }
    return false;
}

function Prev() {
    var num;
    num = parseInt(document.forms[0].hidCurrentPage.value);
    if (num>0) {
        num--;
        document.forms[0].hidCurrentPage.value = num;
        if (num < 1) {
            num = 1;
        }
        return true;
    }
    return false;
}

<%
long intTotalTS = beanInquiryReport.getTotalTimesheet();
if (intTotalTS <= TIMESHEET.MAX_TIMESHEET_RECORDS) {
%>
function doExport() {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    form.onsubmit = window.open('about:blank','popup','width=780,height=550,top=0,left=0,menubar=yes');
    form.target = "popup";
    //form.hidExportAll.value = "true";
    form.hidAction.value = "RA";
    form.hidActionDetail.value = "InquiryExport";
    form.action = "TimesheetServlet";
    form.submit();
    form.onsubmit = "";
    form.target = "";
//    form.Export.disabled = true;
}
<%
} //end if
else {
%>
function doExport() {
	var form = document.forms[0];
	alert("Total Timesheets for exporting can not exceed <%=TIMESHEET.MAX_TIMESHEET_RECORDS%> records. Please correct again !");
}
<%
} //end else
%>
function isValidForm() {
    var count;
    var form = document.forms[0];

    if (form.FromDate.value.length > 0 ) {
    	if (isValidate(form.FromDate.value)==false) {
			form.FromDate.focus();
			return false;
		}
    }
    if (form.ToDate.value.length > 0 ) {
	    if (isValidate(form.ToDate.value)==false) {
	    	form.ToDate.focus();
	    	return false;
	    }
    }
    if ((form.FromDate.value.length > 0) && (form.ToDate.value.length > 0)) {
        if (compareDate(form.FromDate , form.ToDate) > 0) {
            alert("From Date must lower or equal To Date");
            form.FromDate.focus();
            return false
        }
    }
    return true;
}

function clearInvalidDate(form) {
    if (!isDate(form.FromDate.value)) {
        form.FromDate.value = "";
    }
    if (!isDate(form.ToDate.value)) {
        form.ToDate.value = "";
    }
    if (compareDate(form.FromDate, form.ToDate) > 0) {
        form.FromDate.value = "";
    }
}

function cboStatusChaged() {
    var cbo = document.forms[0].ircboStatus;
    if (cbo.value == 1 || cbo.value == 5) {   //Unapproved, Rejected
        document.forms[0].Approver.value = "";
        document.forms[0].Approver.disabled = true;
    }
    else {
        document.forms[0].Approver.disabled = false;
    }
}
</SCRIPT>
</b>
</BODY>
</HTML>