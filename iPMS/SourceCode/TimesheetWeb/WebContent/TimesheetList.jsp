<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, fpt.timesheet.bean.*,
            fpt.timesheet.bean.Approval.*,
            fpt.timesheet.framework.util.StringUtil.*,
            fpt.timesheet.InputTran.ejb.TimeSheetInfo,
            fpt.timesheet.bo.ComboBox.ProjectComboBO,
            java.util.Collection,
            java.util.Iterator" 
%>
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    TSListBean beanTSList = (TSListBean)request.getAttribute("beanTSList");
%>
<HTML>
<HEAD>
<TITLE>Timesheet List</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT src='scripts/popcalendar.js'></SCRIPT>
</HEAD>
<BODY bgcolor="#336699" leftmargin="0" topmargin="0" style="FONT-FAMILY: tahoma, sans-serif; FONT-SIZE: 11px" onkeypress='javascript:setKeypress_Search(event.which)'>
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H1><IMG align="top" src="image/tit_ReportTimesheet.gif"></H1>
<FORM method="post" action="TimesheetServlet" name="frmTimesheetList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="arrSelectedID" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="hidCurrentPage" value='<%=beanTSList.getCurrentPage()%>'>
<INPUT type="hidden" name="hidTotalPage" value='<%=beanTSList.getTotalPage()%>'>
<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName()%></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR>
<!-- COMBO Initialization.. -->
<%
    ProjectComboBO comboProject = new ProjectComboBO();
    StringMatrix smtProjectList = comboProject.getProjectComboList(beanUserInfo.getRole(), beanUserInfo.getUserId(), 0x00, -1);
    smtProjectList.setCell(0, 0, "0");
%>
<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" align="center">
    <TR>
        <!-- PROJECT CODE -->
        <TD width="12%"><STRONG><FONT color="#ffffff" class="label1">Project</FONT></STRONG></TD>
        <TD width="27%"><SELECT name="SearchProjectID" class="SmallCombo" value="<%=beanTSList.getSearchProjectID()%>">
<%
    for (int nRow = 0x00; nRow < smtProjectList.getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(smtProjectList.getCell(nRow, 0));
        String strText = smtProjectList.getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(beanTSList.getSearchProjectID() == nValue ? " selected " : " ");
        out.write("value='" + nValue + "'>" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <!-- FROM DATE  -->
        <TD width="12%"><STRONG><FONT color="#ffffff" class="label1">From Date</FONT></STRONG></TD>
        <TD width="26%">
        <INPUT type="text" name="FromDate" size="20" maxlength="8" value="<%=beanTSList.getSearchFromDate()%>" class="SmallTextbox">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(FromDate, FromDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <!-- SORT BY  -->
        <TD width="10%"><STRONG><FONT color="ffffff" class="label1">Sort By</FONT></STRONG></TD>
        <TD width="20%"><SELECT size="1" name="SearchSortBy" class="VerySmallCombo" value="<%=beanTSList.getSearchSortBy()%>">
            <OPTION value="1" <%=beanTSList.getSearchSortBy() == 1 ? "selected" : ""%>>Date</OPTION>
            <OPTION value="2" <%=beanTSList.getSearchSortBy() == 2 ? "selected" : ""%>>Project</OPTION>
            <OPTION value="3" <%=beanTSList.getSearchSortBy() == 3 ? "selected" : ""%>>Process</OPTION>
            <OPTION value="4" <%=beanTSList.getSearchSortBy() == 4 ? "selected" : ""%>>Work</OPTION>
            <OPTION value="5" <%=beanTSList.getSearchSortBy() == 5 ? "selected" : ""%>>Product</OPTION>
            <OPTION value="6" <%=beanTSList.getSearchSortBy() == 6 ? "selected" : ""%>>Status</OPTION>
        </SELECT></TD>
    </TR>
    <TR>
        <!-- STATUS  -->
        <TD width="12%" align="left"><STRONG><FONT color="#ffffff" class="label1">Status</FONT></STRONG></TD>
        <TD width="27%"><SELECT size="1" name="SearchStatus" class="SmallCombo" value="<%=beanTSList.getSearchStatus()%>">
            <OPTION value="0" <%=beanTSList.getSearchStatus() == 0 ? "selected" : ""%>>All</OPTION>
            <OPTION value="1" <%=beanTSList.getSearchStatus() == 1 ? "selected" : ""%>>Unapproved</OPTION>
            <OPTION value="2" <%=beanTSList.getSearchStatus() == 2 ? "selected" : ""%>>Approved</OPTION>
            <OPTION value="3" <%=beanTSList.getSearchStatus() == 3 ? "selected" : ""%>>Rejected</OPTION>
        </SELECT></TD>
        <!-- TO DATE  -->
        <TD width="12%" align="left"><STRONG><FONT color="#ffffff" class="label1">To Date</FONT></STRONG></TD>
        <TD width="26%" colspan="2">
            <INPUT type="text" name="ToDate" size="20" value="<%=beanTSList.getSearchToDate()%>" maxlength="8" class="SmallTextbox">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(ToDate, ToDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <!-- SEARCH button  -->
        <TD width="20%"><INPUT type="button" name="Search" onclick='javascript:doSearch()' value="  Search " class="Button"></TD>
    </TR>
</TABLE>
<FONT color="#ffffff" class="label1">&nbsp;Date format:&nbsp;</FONT><FONT class="labelDate" color="yellow">&nbsp;(mm/dd/yy)</FONT>
<HR noshade size="1">
<%
    if ((beanTSList.getRejectedTimesheets() > 0x00)
            && (beanTSList.getSearchStatus() != 0)  // STATUS is ALL
            && (beanTSList.getSearchStatus() != 3)) {   // STATUS is REJECTED
%>
<FONT class="label2" color="#cc0001"><STRONG> You have <FONT color="yellow" size="-1"> <%=beanTSList.getRejectedTimesheets()%> </FONT>rejected timesheet(s). </STRONG></FONT>&nbsp; <A class="HeaderMenu" href="javascript:doViewRejected()">View all rejected.</A> 
<%
    }
    int nRecords = beanTSList.getTimesheetList().size();
%>
<BR><BR>
<%
    Collection collResult = (Collection)beanTSList.getTimesheetList();
    Object[] arrResult = collResult.toArray();
    int nSize = arrResult.length;
%>
<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" bgcolor="#336699">
    <TR>
<%
    if (beanTSList.getTotalTimesheet() > 0) {
%>
        <TD height="10" valign="bottom">
<%
        int nItem = 50;
        int nPage = beanTSList.getCurrentPage();
        if (nSize > 0) {
%>
        <FONT color="#ffffff" class="label1">Result&nbsp;</FONT><FONT color="yellow" size="-1"><%=nPage * nItem + 1%> - <%=nPage * nItem + nSize%></FONT> <FONT color="#ffffff" class="label1"> of </FONT><FONT color="yellow" size="-1"><%=beanTSList.getTotalTimesheet()%></FONT> <FONT color="#ffffff" class="label1"> records in </FONT><FONT color="yellow" size="-1"><%=beanTSList.getTotalPage()%></FONT> <FONT color="#ffffff" class="label1"> page(s)</FONT>
<%
        }
        else {
%>
        <B> <FONT color="#ffffff" class="label1">Result&nbsp;</FONT> <FONT color="#ffffff" class="label1">0 - 0 </FONT> <FONT color="#ffffff" class="label1"> of </FONT> <FONT color="yellow" size="-1">0</FONT> <FONT color="#ffffff" class="label1"> records in </FONT> <FONT color="yellow" size="-1">0</FONT><FONT color="#ffffff" class="label1"> page(s)</FONT></B><%
        }
%>
        </TD>
        <TD align="right" height="10" valign="top">
<%
        if (beanTSList.getTotalTimesheet() > 50) {
            if (beanTSList.getCurrentPage() > 0) {
%>
        <A class="HeaderMenu" href="javascript:doViewTimesheet('Prev')">Prev</A> &nbsp;&nbsp; 
<%
            }
            if (beanTSList.getCurrentPage() + 1 < beanTSList.getTotalPage()) {
%>
        <A class="HeaderMenu" href="javascript:doViewTimesheet('Next')">Next</A>&nbsp;&nbsp;&nbsp; 
<%
            }
%>
        <INPUT type="text" onkeypress="javascript:numberAllowed()" size="4" name="txtPage" maxlength="5" value='<%=beanTSList.getCurrentPage() + 1%>' class="flatTextbox">
        <INPUT type="button" name="GoPage" class="Button" onclick='javascript:doGoPage()' value="Go"></TD>
<%
        }
    }
    else {
%>
        <TD width="20%" height="10" valign="bottom"><FONT color="#ffffff" class="label1">Total:&nbsp;</FONT><FONT color="yellow" size="-1">0</FONT></TD>
<%
    }
%>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" bgcolor="#336699">
<%-- MAIN TABLE --%>
<%
    Iterator itResult = beanTSList.getTimesheetList().iterator();
%>
    <COLGROUP>
        <COL width="2%">
        <COL width="8%">
        <COL width="10%">
        <COL width="15%">
        <COL width="10%">
        <COL width="12%">
        <COL width="4%">
        <COL width="43%">
        <COL width="7%">
    <TR>
        <TD class="TableHeader1"><INPUT type="checkbox" name="allbox" value="Check All" onclick="JavaScript:checkAll();"></TD>
        <TD class="TableHeader1">Date</TD>
        <TD class="TableHeader1">Project</TD>
        <TD class="TableHeader1">Process</TD>
        <TD class="TableHeader1">Work</TD>
        <TD class="TableHeader1">Product</TD>
        <TD class="TableHeader1">Time</TD>
        <TD class="TableHeader1">Description</TD>
        <TD class="TableHeader1">Status</TD>
    </TR>
    <TR>
        <TD height="1"></TD>
    </TR><%
    if (itResult != null) {
        int nIterator = 0;
        TimeSheetInfo tsRow;
        while (itResult.hasNext()) {
            tsRow = (TimeSheetInfo)itResult.next();
            nIterator++;
            String strClass = ((nIterator % 2) == 1) ? "Row1" : "Row2";
%>
    <TR>
        <TD class="<%=strClass%>"><INPUT type="checkbox" <%=((tsRow.getStatus() == 1) || (tsRow.getStatus() == 5)) ? "" : "disabled"%> name="checkBox" value='<%=tsRow.getTimeSheetID()%>' onclick="JavaScript:checkCounter();"></TD>
        <TD class="<%=strClass%>" align="center"><FONT color='<%=(tsRow.getStatus() == 5) ? "#FF0000" : "" %>'><%=tsRow.getDate()%></FONT></TD>
        <TD class="<%=strClass%>"><FONT color='<%=(tsRow.getStatus() == 5) ? "#FF0000" : "" %>'><%=tsRow.getProjectName()%></FONT></TD>
        <TD class="<%=strClass%>"><FONT color='<%=(tsRow.getStatus() == 5) ? "#FF0000" : "" %>'><%=tsRow.getProcessName()%></FONT></TD>
        <TD class="<%=strClass%>"><FONT color='<%=(tsRow.getStatus() == 5) ? "#FF0000" : "" %>'><%=tsRow.getTypeofWorkName()%></FONT></TD>
        <TD class="<%=strClass%>"><FONT color='<%=(tsRow.getStatus() == 5) ? "#FF0000" : "" %>'><%=tsRow.getWorkProductName()%></FONT></TD>
        <TD class="<%=strClass%>" align="center"><FONT color='<%=(tsRow.getStatus() == 5) ? "#FF0000" : "" %>'><%=tsRow.getDuration()%></FONT></TD>
        <TD class="<%=strClass%>"><%=fpt.timesheet.framework.util.CommonUtil.CommonUtil.correctHTMLError(tsRow.getDescription())%> <FONT color='<%=(tsRow.getStatus() == 5) ? "#FF0000" : "" %>'>
<%
            if (tsRow.getStatus() == 5) {
%>
        <BR>
        Comment: 
<%
  	      if (tsRow.getComment() != null) {
%>
<%=fpt.timesheet.framework.util.CommonUtil.CommonUtil.correctHTMLError(tsRow.getComment())%>
<%
  		      }
%>
<%
  	       }
%> </FONT></TD>
        <TD class="<%=strClass%>"><FONT color='<%=(tsRow.getStatus() == 5) ? "#FF0000" : "" %>'><%=tsRow.getStatusName()%></FONT></TD>
    </TR>
<%
        }//end while
    }
%>
</TABLE>
<P align="center"><INPUT type="button" name="Addnew" onclick='javascript:doAdd()' value="Addnew" class="Button">
<INPUT type="button" name="Update" onclick='javascript:doUpdate()' value="Update" class="Button">
<INPUT type="button" name="Delete" onclick='javascript:doDelete()' value="Delete" class="Button"></P>
</DIV>
</FORM>

<SCRIPT language="javascript">
function doSearch() {
    var form = document.forms[0];

    if (!isValidForm()) {
        return;
    }
    
    //modified by MinhPT 03Oct13 for check nCurrentPage > nTotalPage ?
//    if (form.txtPage != null)
//        form.hidCurrentPage.value = form.txtPage.value - 1;
//    else 

	form.hidCurrentPage.value = "0";
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "ListTimesheet";
    form.action = "TimesheetServlet";
    form.submit();
}

function doViewRejected() {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    form.SearchStatus.value = "3";
    if (form.txtPage != null)
        form.hidCurrentPage.value = form.txtPage.value - 1;
    else 
        form.hidCurrentPage.value = "0";
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "ListTimesheet";
    form.action = "TimesheetServlet";
    form.submit();
}

function doViewTimesheet(to) {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    if (to == "Next") {
        if (Next()) {
            form.hidAction.value = "AA";
            form.hidActionDetail.value = "ListTimesheet";
            form.action = "TimesheetServlet";
            form.submit();
        }
    }
    else if(to == "Prev") {
        if (Prev()) {
            form.hidAction.value = "AA";
            form.hidActionDetail.value = "ListTimesheet";
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
            form.hidAction.value = "AA";
            form.hidActionDetail.value = "ListTimesheet";
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

function doAdd() {
    var form = document.forms[0];
    clearInvalidDate(form);
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "AddTimesheet";
    form.action = "TimesheetServlet";
    form.submit();
}

function doDelete() {
    var form = document.forms[0];
    clearInvalidDate(form);
    if (form.arrSelectedID.value == "") {
        alert("Please select some timesheets first!");
        return;
    }
    bOK = window.confirm("Do you want to delete timesheet?");
    if (!bOK) {
        return false;
    }
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "DeleteTimesheet";
    form.action = "TimesheetServlet";
    form.submit();
}

function doUpdate() {
    var form = document.forms[0];
    clearInvalidDate(form);
    if (form.arrSelectedID.value == "") {
        alert("Please select some timesheet.");
        return;
    }
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "UpdateTimesheet";
    form.action = "TimesheetServlet";
    form.submit();
}

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
            alert("From date must be lower than or equal to To date");
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

function Next() {
    var num;
    num = parseInt(document.forms[0].hidCurrentPage.value);
    if (num < (document.forms[0].hidTotalPage.value - 1)) {
        num++;
        document.forms[0].hidCurrentPage.value = num;
        return true;
    }
    return false;
}

function Prev() {
    var num;
    num = parseInt(document.forms[0].hidCurrentPage.value);
    if (num > 0) {
        num--;
        document.forms[0].hidCurrentPage.value = num;
        if (num < 1) {
            num = 1;
        }
        return true;
    }
    return false;
}

function checkAll() {
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == 'checkBox' && !e.disabled) {
            e.checked = document.forms[0].allbox.checked;
        }
    }
    checkCounter();
}

function checkCounter() {
    var nCount;
    var strValue;
    nCount = 0;
    strValue = "";
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == 'checkBox' && e.type == "checkbox") {
            if (e.checked == 1) {
                nCount++;
                strValue += e.value + ',';
            }
        }
    }
    if (nCount > 0) {
        document.forms[0].arrSelectedID.value = strValue.substring(0, strValue.length - 1);
    }
    else {
        document.forms[0].arrSelectedID.value = "";
    }
}
</SCRIPT>
</BODY>
</HTML>