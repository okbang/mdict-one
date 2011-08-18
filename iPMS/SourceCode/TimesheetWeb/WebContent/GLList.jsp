<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, fpt.timesheet.bean.*,
            fpt.timesheet.bean.Approval.*,
            fpt.timesheet.framework.util.CommonUtil.*,
            fpt.timesheet.framework.util.StringUtil.StringMatrix,
            java.util.Collection, java.util.Iterator" 
%>
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    GLListBean beanGLList = (GLListBean)request.getAttribute("beanGLList");
%>
<HTML>
<HEAD>
<TITLE>Approve for Internal Projects</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT src='scripts/popcalendar.js'></SCRIPT>
</HEAD>
<BODY bgcolor="#336699" leftmargin="0" topmargin="0" onkeypress='javascript:setKeypress_Search(event.which)'>
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H1><IMG align="top" src="image/tit_ApproveForOtherTypePro.gif"></H1>

<FORM method="post" action="TimesheetServlet" name="frmGLList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="hidGLCurrentPage" value='<%=beanGLList.getCurrentPage()%>'>
<INPUT type="hidden" name="hidGLTotalPage" value='<%=beanGLList.getTotalPage()%>'>
<INPUT type="hidden" name="hidFromSearch" value="0">

<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName() %></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR>
<%
    // Common variable
    int i;
    int maxrows;
    int tmp ;
    String gItemValue = "";
    String gItemDisplay = "";
%>
<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" align="center">
    <TR>
        <!-- PROJECT -->
        <TD width="12%"><STRONG><FONT color="#ffffff" class="label1">Project</FONT></STRONG></TD>
        <TD width="27%"><SELECT size="1" name="GLProject" class="SmallCombo" tabindex="1">
            <!-- Fill Data -->
<%
    // get selected value
    String tmpProject = beanGLList.getProject();
    StringMatrix mtxProject = beanGLList.getProjectList();
    maxrows = mtxProject.getNumberOfRows();
    i = 0;
    while (i < maxrows) {
        gItemValue = mtxProject.getCell(i, 0);
        gItemDisplay = mtxProject.getCell(i, 1);
        if (!(gItemValue.equalsIgnoreCase(tmpProject))) {
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
        <!-- FROM DATE -->
        <TD width="12%"><STRONG><FONT color="#ffffff" class="label1">From Date</FONT></STRONG></TD>
        <TD width="26%">
            <INPUT type="text" name="apvFromDate" value="<%=beanGLList.getFromDate()%>" size="20" class="smallTextbox" maxlength="8">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(apvFromDate, apvFromDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <!-- SORT BY -->
        <TD width="10%"><STRONG><FONT color="red" class="label1">Sort by</FONT></STRONG></TD>
        <TD width="20%"><SELECT name="Sortby" size="1" class="VerySmallCombo">
            <!-- Fill Data -->
<%
    // get selected value
    tmp = beanGLList.getSortby();
    StringMatrix mtxSortby = beanGLList.getSortbyList();
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
        <TD width="27%"><SELECT name="Status" size="1" class="SmallCombo">
            <!-- Fill Data -->
<%
    // get selected value
    tmp = beanGLList.getStatus();
    StringMatrix mtxStatus = beanGLList.getStatusList();
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
            <INPUT type="text" name="apvToDate" value="<%=beanGLList.getToDate()%>" size="20" class="smallTextbox" maxlength="8">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(apvToDate, apvToDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <TD width="10%"><STRONG><FONT color="#ffffff" class="label1">Account</FONT></STRONG></TD>
        <TD width="20%">
        	<INPUT type="text" size="20" class="SmallTextbox" name="apvAccount" value="<%=beanGLList.getAccount()%>" maxlength="30">
        </TD>
    </TR>
    <TR>
        <TD width="12%" align="left"></TD>
        <TD width="27%"></TD>
        <TD width="12%" align="left"></TD>
        <TD width="26%"></TD>
        <TD width="10%"></TD>
        <TD width="20%"><INPUT type="button" name="Search" class="Button" onclick='javascript:doSearch()' value="  Search "></TD>
    </TR>
</TABLE>
<FONT color="#ffffff" class="label1">&nbsp;Date format:&nbsp;</FONT><FONT class="labelDate" color="yellow">&nbsp;(mm/dd/yy)</FONT>
<HR noshade size="1">
<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" bgcolor="#336699">
    <TR>
<%
    StringMatrix mtxTimesheet = beanGLList.getTimesheetList();
    int nMax = 50; 
    int nTotalPage = beanGLList.getTotalPage();
    maxrows = mtxTimesheet.getNumberOfRows();
    int nCurrentPage = beanGLList.getCurrentPage();
    if(beanGLList.getTotalTimesheet()>0){
        
        if(maxrows>0){
%>
        <TD height="10" valign="bottom">    
        <FONT color="#ffffff" class="label1">Result:&nbsp;</FONT>
        <FONT color="yellow" size="-1"><%=nCurrentPage * nMax + 1%> - <%=nCurrentPage * nMax + maxrows%></FONT>
        <FONT color="#ffffff" class="label1"> of </FONT>
        <FONT color="yellow" size="-1"><%=beanGLList.getTotalTimesheet()%></FONT>
        <FONT color="#ffffff" class="label1"> records in </FONT>
        <FONT color="yellow" size="-1"><%=nTotalPage%></FONT>
        <FONT color="#ffffff" class="label1"> page(s) </FONT>
        </TD>
<%
        } 
        else{
%>
        <TD height="10" valign="bottom">  
        <FONT color="#ffffff" class="label1">Result:&nbsp;</FONT>
        <FONT color="yellow" size="-1">0 - 0</FONT>
        <FONT color="#ffffff" class="label1"> of </FONT>
        <FONT color="yellow" size="-1"><%=beanGLList.getTotalTimesheet()%></FONT>
        <FONT color="#ffffff" class="label1"> records in </FONT>
        <FONT color="yellow" size="-1"><%=nTotalPage%></FONT>
        <FONT color="#ffffff" class="label1"> page(s) </FONT>
        </TD>
<%
        }
        if(beanGLList.getTotalTimesheet()>nMax) {
        %>
        <TD align="right" height="10" valign="top">     
        <%
            if(nCurrentPage>0){
         %>
         <A class="HeaderMenu" href="javascript:doViewTimesheet('Prev')">Prev</A>&nbsp;&nbsp;&nbsp;
         <%         
            }
            if(nCurrentPage+1 < nTotalPage){
         %>
         <A class="HeaderMenu" href="javascript:doViewTimesheet('Next')">Next</A>&nbsp;&nbsp;&nbsp;
         <%         
            }
        %>
        
        <INPUT type="text" onkeypress="javascript:numberAllowed()" size="4" name="txtGLPage" maxlength="10" value='<%=nCurrentPage + 1 %>' class="flatTextbox">
        <INPUT type="button" name="GoPage" class="Button" onclick='javascript:doGoPage()' value="Go">
        
        </TD>
        <%
        }
    }
    else
    {
    %>
    <TD height="10" valign="bottom">    
    <B> <FONT color="yellow" size="-1">Total 0 records </FONT></B>
    </TD>
   
    <%
    }
%>
</TR>
</TABLE>
<BR>
<TABLE border="0" cellpadding="1" cellspacing="0" bgcolor="#336699" width="100%">
    <TR>
        <TD width="3%" class="TableHeader1"><INPUT type="checkbox" name="allbox" value="CheckAll" onclick="JavaScript:checkAll();"></TD>
        <TD width="10%" class="TableHeader1">Project</TD>
        <TD width="10%" class="TableHeader1">Account</TD>
        <TD width="8%" class="TableHeader1">Date</TD>
        <TD width="10%" class="TableHeader1">Process</TD>
        <TD width="7%" class="TableHeader1">Work</TD>
        <TD width="10%" class="TableHeader1">Product</TD>
        <TD width="5%" class="TableHeader1">Time</TD>
        <TD width="10%" class="TableHeader1">Description</TD>
        <TD width="10%" class="TableHeader1">Approver</TD>
        <TD width="10%" class="TableHeader1">Status</TD>
        <TD width="10%" class="TableHeader1">Comment</TD>
    </TR>
<%
    i = 0;
    while (i < maxrows)  {
        String sId = mtxTimesheet.getCell(i, 0);
        String sProject = mtxTimesheet.getCell(i, 1);
        String sAccount = mtxTimesheet.getCell(i, 2);
        String sDate = mtxTimesheet.getCell(i, 3);
        String sDescription = mtxTimesheet.getCell(i, 4);
        String sDuration = mtxTimesheet.getCell(i, 5);
        String sProcessId = mtxTimesheet.getCell(i, 6);
        String sTypeId = mtxTimesheet.getCell(i, 7);
        String sProductId = mtxTimesheet.getCell(i, 8);
        String sProcess = beanGLList.mapToName("Process", sProcessId);
        String sType = beanGLList.mapToName("Type", sTypeId);
        String sProduct = beanGLList.mapToName("Product", sProductId);
        String sLeader = mtxTimesheet.getCell(i, 9);
        String sStatusId = mtxTimesheet.getCell(i, 10);
        String comment = mtxTimesheet.getCell(i, 11);
        String sStatus = beanGLList.mapToName("Status", sStatusId);
        String strClass = ((i%2) == 1) ? "Row2" : "Row1";
%>
    <TR>
        <TD width="3%" class="<%=strClass%>" align="center"><INPUT type="checkbox" name="check" value="<%=sId%>">
        <INPUT type="hidden" name="isRcheck" value="-1"></TD>
        <TD width="10%" class="<%=strClass%>"><%=sProject%></TD>
        <TD width="10%" class="<%=strClass%>"><%=sAccount%></TD>
        <TD width="8%" class="<%=strClass%>" align="center"><%=sDate%></TD>
        <TD width="17%" class="<%=strClass%>"><%=sProcess%></TD>
        <TD width="8%" class="<%=strClass%>"><%=sType%></TD>
        <TD width="17%" class="<%=strClass%>"><%=sProduct%></TD>
        <TD width="5%" class="<%=strClass%>"><%=sDuration%></TD>
        <TD width="15%" class="<%=strClass%>"><%= CommonUtil.correctHTMLError(sDescription)%></TD>
        <TD width="13%" class="<%=strClass%>">&nbsp;<%=sLeader%></TD>
        <TD width="10%" class="<%=strClass%>"><%=sStatus%></TD>
        <TD width="10%" class="<%=strClass%>"><INPUT type="text" name="comment" size="10" maxlength="100" value="<%=comment%>" class="SmallTextbox"></TD>
    </TR>
    <TR>
        <TD height="1"></TD>
    </TR><%
        i++;
    }
%>
</TABLE>
<P align="center"><INPUT type="button" class="Button" name="Approve" onclick='javascript:doApprove()' value="Approve">
<INPUT type="button" class="Button" name="Update" onclick='javascript:doUpdate()' value="Update">
<INPUT type="button" class="Button" name="Reject" onclick='javascript:doReject()' value="Reject"></P>
</DIV>
</FORM>

<SCRIPT language="javascript">
function doApprove() {
    if (hasCheck()) {
        var form = document.forms[0];
        clearInvalidDate(form);
        form.hidAction.value = "AA";
        form.hidActionDetail.value = "ApproveGL";
        form.action = "TimesheetServlet";
        form.submit();
    }
    else {
        alert("Please select a timesheet.");
    }
}

function doUpdate() {
    var form = document.forms[0];
    var nCount = hasCheck1();
    clearInvalidDate(form);
    if ((0 < nCount) && (nCount < 26)) {
        form.hidAction.value = "AA";
        form.hidActionDetail.value = "UpdateGL";
        form.action = "TimesheetServlet";
        form.submit();
    }
    else if (nCount > 25) {
        bOK = window.confirm("You have selected more than 25 records. Click OK for continuing, or Cancel for re-setting");
        if (!bOK) {
            return false;
        }
        else {
            form.hidAction.value = "AA";
            form.hidActionDetail.value = "UpdateGL";
            form.action = "TimesheetServlet";
            form.submit();
         }
    }
    else alert("Please select a timesheet.");
}

function doReject() {
    var form = document.forms[0];
    clearInvalidDate(form);
    if (hasCheck()) {
        bOK = window.confirm("Do you want to reject timesheet?");
        if (!bOK) {
            return false;
        }
        form.hidAction.value = "AA";
        form.hidActionDetail.value = "RejectGL";
        form.action = "TimesheetServlet";
        form.submit();
    }
    else {
        alert("Please select a timesheet.");
    }
}

function doSearch() {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
	//form.hidFromSearch.value = "1";
    //modified by MinhPT 03Oct13 for check nCurrentPage > nTotalPage ?
    if (form.txtGLPage != null)
        form.hidGLCurrentPage.value = form.txtGLPage.value - 1;
    else 
        form.hidGLCurrentPage.value = "0";
        form.hidAction.value = "AA";
    form.hidActionDetail.value = "ListGL";
    form.action = "TimesheetServlet";
    form.submit();
}

function hasCheck() {
    var nCount = 0;
    var cInt = 0;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == 'check' && e.type == "checkbox") {
            if (e.checked == 1) {
                if (document.forms[0].isRcheck.length > 0) {
                    document.forms[0].isRcheck[cInt].value = nCount;
                }
                else {
                    document.forms[0].isRcheck.value = nCount;
                }
                nCount++;
            }
            cInt++;
        }
    }
    if (nCount>0) {
        return true;
    }
    return false;
}

function hasCheck1() {
    var nCount = 0;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == 'check' && e.type == "checkbox") {
            if (e.checked == 1) {
                nCount++;
                if (nCount > 25) {
                    e.checked=0;
                }
            }
        }
    }
    return nCount;
}

function checkAll() {
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == 'check' && !e.disabled) {
            e.checked = document.forms[0].allbox.checked;
        }
    }
}

function doGoPage(){
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    if(isNonNegativeInteger(form.txtGLPage.value - 1)) {
        if ((parseInt(form.txtGLPage.value)) > parseInt(form.hidGLTotalPage.value)) {
            alert("Invalid page.");
            return false;
        }
        else {
            form.hidGLCurrentPage.value = form.txtGLPage.value - 1;
            form.hidAction.value = "AA";
            form.hidActionDetail.value = "ListGL";
            form.action = "TimesheetServlet";
            form.submit();
        }
    }
    else {
        alert("Invalid number");
        form.txtGLPage.focus();
        form.txtGLPage.select();
        return false;
    }

}

function Next() {
    var num;
    num = parseInt(document.forms[0].hidGLCurrentPage.value);
    if (num < (document.forms[0].hidGLTotalPage.value - 1)) {
        num++;
        document.forms[0].hidGLCurrentPage.value = num;
        return true;
    }
    return false;
}

function Prev() {
    var num;
    num = parseInt(document.forms[0].hidGLCurrentPage.value);
    if (num > 0) {
        num--;
        document.forms[0].hidGLCurrentPage.value = num;
        if (num < 1) {
            num = 1;
        }
        return true;
    }
    return false;
}

function doViewTimesheet(to) {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    if (to == "Next") {
        if (Next()) {
            form.hidAction.value = "AA";
            form.hidActionDetail.value = "ListGL";
            form.action = "TimesheetServlet";
            form.submit();
        }
    }
    else if (to=="Prev") {
        if (Prev()) {
            form.hidAction.value = "AA";
            form.hidActionDetail.value = "ListGL";
            form.action = "TimesheetServlet";
            form.submit();
        }
    }
}

function isValidForm() {
    var count;
    var form = document.forms[0];

    if (form.apvFromDate.value.length > 0 ) {
		if (isValidate(form.apvFromDate.value)==false) {
			form.apvFromDate.focus();
			return false;
		}
    }
    if (form.apvToDate.value.length > 0 ) {
		if (isValidate(form.apvToDate.value)==false) {
    		form.apvToDate.focus();
    		return false;
    	}
    }
    if ((form.apvFromDate.value.length > 0) && (form.apvToDate.value.length > 0)) {
        if (compareDate(form.apvFromDate , form.apvToDate) > 0) {
            alert("From Date must lower or equal To Date");
            form.apvFromDate.focus();
            return false
        }
    }
    return true;
}

function clearInvalidDate(form) {
    if (!isDate(form.apvFromDate.value)) {
        form.apvFromDate.value = "";
    }
    if (!isDate(form.apvToDate.value)) {
        form.apvToDate.value = "";
    }
    if (compareDate(form.apvFromDate, form.apvToDate) > 0) {
        form.apvFromDate.value = "";
    }
}
</SCRIPT>
</BODY>
</HTML>