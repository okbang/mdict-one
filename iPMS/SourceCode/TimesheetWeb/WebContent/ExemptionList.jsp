<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, fpt.timesheet.bean.*,
    fpt.timesheet.bean.Approval.*,
    fpt.timesheet.framework.util.StringUtil.*,
    fpt.timesheet.InputTran.ejb.TimeSheetInfo,
    fpt.timesheet.Exemption.ejb.ExemptionBean,
    fpt.timesheet.bean.ExemptionInfoBean,
    fpt.timesheet.bean.ExemptionListBean,
    fpt.timesheet.bo.ComboBox.ProjectComboBO,
    java.util.Collection,
    java.util.Iterator" 
%>
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ExemptionInfoBean beanExemptionInfo = (ExemptionInfoBean)request.getAttribute("beanExemptionInfo");
    ExemptionListBean beanExemptionList = (ExemptionListBean)request.getAttribute("beanExemptionList");
    StringMatrix smtGroupList = beanExemptionList.getGroupList();
%>
<HTML>
<HEAD>
<TITLE>Exemption List</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT src='scripts/popcalendar.js'></SCRIPT>
</HEAD>

<BODY bgcolor="#336699" leftmargin="0" topmargin="0" style="FONT-FAMILY: tahoma, sans-serif; FONT-SIZE: 11px" onkeypress='javascript:setKeypress_Search(event.which)'>
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H3><FONT color="yellow">List Timesheet Exemption</FONT></H3>

<FORM method="post" action="TimesheetServlet" name="frmExemptionList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="arrSelectedID" value="">
<INPUT type="hidden" name="hidExemptionId" value="">
<INPUT type="hidden" name="hidCurrentPage" value='<%=beanExemptionList.getCurrentPage()%>'>
<INPUT type="hidden" name="hidTotalPage" value='<%=beanExemptionList.getTotalPage()%>'>
<INPUT type="hidden" name="hidUserGroupName" value="<%=beanUserInfo.getGroupName()%>">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">

<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName()%></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR>
<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" align="center">
    <TR>
        <!-- GROUP LIST -->
        <TD width="10%"><STRONG><FONT color="#ffffff" class="label1">Group</FONT></STRONG></TD>
        <TD width="21%">
           	<SELECT name="cboGroupNameList" class="SmallCombo">
        	<%for (int nRow = 0; nRow < smtGroupList.getNumberOfRows(); nRow++) {
        		String strText = smtGroupList.getCell(nRow, 0);
        		out.write("<OPTION value='" + strText + "'" + (strText.equals(beanExemptionInfo.getGroupName()) ? "selected" : "") + ">" + strText + "</OPTION>");
    		}%>
        	</SELECT>
        </TD>

        <!-- FROM DATE  -->
        <TD width="11%"><STRONG><FONT color="#ffffff" class="label1">From Date</FONT></STRONG></TD>
        <TD width="17%">
        <INPUT type="text" name="txtFromDateList" size="20" maxlength="8" value="<%=beanExemptionInfo.getSearchFromDate()%>" class="SmallTextbox">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(txtFromDateList, txtFromDateList, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>

        <!-- ACCOUNT  -->
        <TD width="10%">
        <STRONG><FONT color="#ffffff" class="label1">Account</FONT></STRONG></TD>
        <TD width="18%">
        	<input type="text" name="txtDevAccount" value="<%=beanExemptionInfo.getDevAccount()%>" size="20" class="BigTextbox">
        </TD>
    </TR>
    <TR>
        <!-- TYPE  -->
        <TD width="10%" align="left"><STRONG>
		<FONT color="#ffffff" class="label1">Type</FONT></STRONG></TD>
        <TD width="21%">
        	<SELECT size="1" name="cboTypeList" class="SmallCombo" value="<%=beanExemptionInfo.getType()%>">
        	    <OPTION value="0" <%=beanExemptionInfo.getType() == 0 ? "selected" : ""%>>All</OPTION>
        	    <OPTION value="1" <%=beanExemptionInfo.getType() == 1 ? "selected" : ""%>>Permanent</OPTION>
        	    <OPTION value="2" <%=beanExemptionInfo.getType() == 2 ? "selected" : ""%>>Temporary</OPTION>
        	    <OPTION value="3" <%=beanExemptionInfo.getType() == 3 ? "selected" : ""%>>Weekly</OPTION>
        	</SELECT>
        </TD>
        <!-- TO DATE  -->
        <TD width="11%" align="left"><STRONG><FONT color="#ffffff" class="label1">To Date</FONT></STRONG></TD>
        <TD width="17%" >
            <INPUT type="text" name="txtToDateList" size="20" value="<%=beanExemptionInfo.getSearchToDate()%>" maxlength="8" class="SmallTextbox">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(txtToDateList, txtToDateList, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <!-- NAME  -->
        <TD width="10%" >
            <STRONG><FONT color="#ffffff" class="label1">Name</FONT></STRONG></TD>
        <TD width="18%" >
            <input type="text" name="txtDevName" value="<%=beanExemptionInfo.getDevName()%>" size="20" class="BigTextbox">
        </TD>
        <TD width="9%">&nbsp;</TD>
    </TR>
        <!-- SEARCH button  --> 
    <TR>
        <TD width="10%" align="left">&nbsp;</TD>
        <TD width="21%">&nbsp;</TD>
        <TD width="11%" align="left">&nbsp;</TD>
        <TD width="17%" >&nbsp;</TD>
        <TD width="10%" >&nbsp;</TD>
        <TD width="18%" >
            <INPUT type="button" name="Search" onclick='javascript:doSearch()' value="  Search " class="Button">
        </TD>
        <TD width="9%">&nbsp;</TD>
    </TR>
</TABLE>

<FONT color="#ffffff" class="label1">&nbsp;Date format:&nbsp;</FONT><FONT class="labelDate" color="yellow">&nbsp;(mm/dd/yy)</FONT>
<HR noshade size="1">
<%
    Collection colResult = (Collection)beanExemptionList.getExemptionList();
    Object[] arrResult = colResult.toArray();
    int nSize = arrResult.length;

    // Display error message for checking existing exemption
    // If have an error message --> display
    if (request.getAttribute("ERROR_MESSAGE") != null) {
%>
<TABLE>
<tr>
	<td>
		<font class="label1" color="red"><B><%=request.getAttribute("ERROR_MESSAGE")%></B></font>
	</td>
</tr>
</TABLE>
<%
        request.removeAttribute("ERROR_MESSAGE");
    }
%>

<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" bgcolor="#336699">
    <TR>
<%
    if (beanExemptionList.getTotalExemption() > 0) {
%>
        <TD height="10" valign="bottom">
<%
        int nItem = 50;
        int nPage = beanExemptionList.getCurrentPage();
        if (nSize > 0) {
%>
        <FONT color="#ffffff" class="label1">Result&nbsp;</FONT><FONT color="yellow" size="-1"><%=nPage * nItem + 1%> - <%=nPage * nItem + nSize%></FONT> <FONT color="#ffffff" class="label1"> of </FONT><FONT color="yellow" size="-1"><%=beanExemptionList.getTotalExemption()%></FONT> <FONT color="#ffffff" class="label1"> records in </FONT><FONT color="yellow" size="-1"><%=beanExemptionList.getTotalPage()%></FONT> <FONT color="#ffffff" class="label1"> page(s)</FONT>
<%
        }
        else {
%>
        <B> <FONT color="#ffffff" class="label1">Result&nbsp;</FONT> <FONT color="#ffffff" class="label1">0 - 0 </FONT> <FONT color="#ffffff" class="label1"> of </FONT> <FONT color="yellow" size="-1">0</FONT> <FONT color="#ffffff" class="label1"> records in </FONT> <FONT color="yellow" size="-1">0</FONT><FONT color="#ffffff" class="label1"> page(s)</FONT></B>
<%
        }
%>
        </TD>
        <TD align="right" height="10" valign="top">
<%
        if (beanExemptionList.getTotalExemption() > 50) {
            if (beanExemptionList.getCurrentPage() > 0) {
%>
        <A class="HeaderMenu" href="javascript:doViewExemption('Prev')">Prev</A> &nbsp;&nbsp; 
<%
            }
            if (beanExemptionList.getCurrentPage() + 1 < beanExemptionList.getTotalPage()) {
%>
        <A class="HeaderMenu" href="javascript:doViewExemption('Next')">Next</A>&nbsp;&nbsp;&nbsp; 
<%
            }
%>
        <INPUT type="text" onkeypress="javascript:numberAllowed()" size="4" name="txtPage" maxlength="5" value='<%=beanExemptionList.getCurrentPage() + 1%>' class="flatTextbox">
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
    <COLGROUP>
        <COL width="2%">
        <COL width="8%">
        <COL width="8%">
        <COL width="16%">
        <COL width="12%">
        <COL width="12%">
        <COL width="7%">
        <COL width="7%">
        <COL width="14%">
        <COL width="14%">
    </COLGROUP>
    <TR>
        <TD class="TableHeader1"><INPUT type="checkbox" name="chkAllExemptionId" value="Check All" onclick="JavaScript:checkAll();"></TD>
        <TD class="TableHeader1">Group</TD>
        <TD class="TableHeader1">Account</TD>
        <TD class="TableHeader1">Name</TD>
        <TD class="TableHeader1">Exemption Type</TD>
        <TD class="TableHeader1">Recurrence</TD>
        <TD class="TableHeader1">From Date</TD>
        <TD class="TableHeader1">To Date</TD>
        <TD class="TableHeader1">Reason</TD>
        <TD class="TableHeader1">Note</TD>
    </TR>
    <TR>
        <TD height="1"></TD>
    </TR>
<%
	Iterator itResult = beanExemptionList.getExemptionList().iterator();
	int nIterator = 0;
	if (itResult != null) {
        String strType = "";
        String strWeekDay = "";
        String strTemp = "";
        ExemptionBean beanExemption;
        while (itResult.hasNext()) {
	        strWeekDay = "";
            beanExemption = (ExemptionBean)itResult.next();
            if (beanExemption.getType() == 1) {
            	strType = "Permanent";
            }
            else if (beanExemption.getType() == 2) {
            	strType = "Temporary";
            }
            else if (beanExemption.getType() == 3) {
            	strType = "Weekly";
				if (beanExemption.getWeekDay() != null) {
					strWeekDay = "Every " + beanExemption.getWeekDayList();
				}
            }
            nIterator++;
            String strClass = ((nIterator % 2) == 1) ? "Row1" : "Row2";
%>
    <TR>
        <TD class="<%=strClass%>"><INPUT type="checkbox" name="chkExemptionId" value="<%=beanExemption.getExemptionId()%>"></TD>
		<INPUT type="hidden" name="hidGroupName" value="<%=beanExemption.getGroup()%>">
        <TD class="<%=strClass%>"><%=(beanExemption.getGroup() == null) ? "" : beanExemption.getGroup()%></TD>
        <TD class="<%=strClass%>"><%=(beanExemption.getAccount() == null) ? "" : beanExemption.getAccount()%></TD>
        <TD class="<%=strClass%>">
        	<a href="javascript:doUpdate('<%=beanExemption.getExemptionId()%>')"><%=(beanExemption.getFullName() == null) ? "" : beanExemption.getFullName()%></a>
	    </TD>
        <TD class="<%=strClass%>"><center><%=strType%></center></TD>
        <TD class="<%=strClass%>"><%=strWeekDay%></TD>
        <TD class="<%=strClass%>"><center><%=(beanExemption.getFromDate() == null) ? "" : beanExemption.getFromDate()%></center></TD>
        <TD class="<%=strClass%>"><center><%=(beanExemption.getToDate() == null) ? "" : beanExemption.getToDate()%></center></TD>
        <TD class="<%=strClass%>"><%=(beanExemption.getReason() == null) ? "" : beanExemption.getReason()%></TD>
        <TD class="<%=strClass%>"><%=(beanExemption.getNote() == null) ? "" : beanExemption.getNote()%></TD>
    </TR><%
        }//end while
%>
        <INPUT type="hidden" name="hidGroupName" value="">
        <INPUT type="hidden" name="chkExemptionId" value="">
<%
    }//end if
%>
</TABLE>
<P align="center">
<INPUT type="hidden" name="txtGroupName" value=<%=beanUserInfo.getGroupName()%> >
<INPUT type="button" name="Addnew" onclick='javascript:doAdd()' value="Addnew" class="Button">
<INPUT type="button" name="Delete" onclick='javascript:doDelete()' value="Delete" class="Button">
</P>
</DIV>
</FORM>

<SCRIPT language="javascript">
function doSearch() {
    var form = document.forms[0];
    if (!isValidForm()) {
    	return;
    }
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "ExemptionList";
    form.action = "TimesheetServlet";
    form.submit();
}

function isValidForm() {
    var count;
    var form = document.forms[0];

    if (form.txtFromDateList.value.length > 0 ) {
		if (isValidate(form.txtFromDateList.value)==false) {
			form.txtFromDateList.focus();
			return false;
		}
    }
    if (form.txtToDateList.value.length > 0 ) {
	  	if (isValidate(form.txtToDateList.value)==false) {
    		form.txtToDateList.focus();
    		return false;
    	}
    }
    if ((form.txtFromDateList.value.length > 0) && (form.txtToDateList.value.length > 0)) {
        if (compareDate(form.txtFromDateList , form.txtToDateList) > 0) {
            alert("From Date must lower or equal To Date");
            form.txtFromDateList.focus();
            return false;
        }
    }
    return true;
}

function doViewExemption(to) {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    if (to == "Next") {
        if (Next()) {
			form.hidAction.value = "AA";
			form.hidActionDetail.value = "ExemptionList";
		    form.action = "TimesheetServlet";
		    form.submit();
        }
    }
    else if(to == "Prev") {
        if (Prev()) {
			form.hidAction.value = "AA";
			form.hidActionDetail.value = "ExemptionList";
		    form.action = "TimesheetServlet";
		    form.submit();
        }
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
            form.hidActionDetail.value = "ExemptionList";
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

function checkAll() {
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == 'chkExemptionId') {
            e.checked = document.forms[0].chkAllExemptionId.checked;
        }
    }
}

function doAdd() {
	var form = document.forms[0];
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "ExemptionAdd";
    form.action = "TimesheetServlet";
    form.submit();
}

<%
String strRole = beanUserInfo.getRole();
if ( strRole.equals("1110000000") ) {
%>
function doDelete() {
    var nCount;
    var strValueId;
    var strGroupName;
    nCount = 0;
    strValueId = "";
    strGroupName = "";
    var form = document.forms[0];

	if (checkValid()) {
    for (var i = 0; i < form.chkExemptionId.length; i++) {
       	if (form.chkExemptionId[i].checked == 1) {
			if (form.hidUserGroupName.value != form.hidGroupName[i].value) {
				alert('Sorry, unauthorized access. You are not Group Leader of selected user');
				return;
			}
       		nCount++;
	        strValueId += form.chkExemptionId[i].value + ',';
	        strGroupName = form.hidGroupName[i].value;
       	}
    }
    if (nCount > 0) {
        //form.hidGroupName.value = strGroupName.substring(0, strGroupName.length - 1);
        form.hidGroupName.value = strGroupName;
		if (form.hidUserGroupName.value == form.hidGroupName.value) {
	    	    if (confirm("Do you want to delete selected records, continue?")) {
	    	        form.hidAction.value = "AA";
	    	        form.hidActionDetail.value = "ExemptionDelete";
	    	        form.action = "TimesheetServlet";
	    	        form.submit();
	    	    }
	    	}
		}
	}
}
<%
}
else if ( strRole.equals("1111110000") || strRole.equals("1000100000") || strRole.equals("1100100000") ) {
%>
function doDelete() {
    var form = document.forms[0];
    if (checkValid()) {
        if (confirm("Do you want to delete selected records, continue?")) {
            form.hidAction.value = "AA";
            form.hidActionDetail.value = "ExemptionDelete";
            form.action = "TimesheetServlet";
            form.submit();
        }
    }
}
<%
}
else {
%>
function doDelete() {
	alert('Sorry, unauthorized access. You are not Admin, Manager, QA, GL');
}
<%
}
%>

function checkValid() {
    var flag = true;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == "chkExemptionId" && e.type == "checkbox") {
            if (e.checked == 1) {
                flag = false;
            }
        }
    }
    if (flag) {
        alert("Please select exemption to delete!");
        return false;
    }
    return true;
}

<%
if ( strRole.equals("1111110000") || strRole.equals("1110000000") || strRole.equals("1000100000") || strRole.equals("1100100000") ) {
%>
function doUpdate(id) {
	var form = document.forms[0];
	form.hidExemptionId.value = id;
	form.hidAction.value = "AA";
	form.hidActionDetail.value = "ExemptionUpdate";
	form.action = "TimesheetServlet";
	form.submit();
}
<%
}
else {
%>
function doUpdate(id) {
	alert('Sorry, unauthorized access. You are not Admin, Manager, QA, GL');
}
<%
}
%>

<%
if ( strRole.equals("1111110000") || strRole.equals("1110000000") || strRole.equals("1000100000") || strRole.equals("1100100000") ) {
%>
function hasAllowAddDelete() {
	frmExemptionList.Addnew.disabled = false;
	frmExemptionList.Delete.disabled = false;
}
<%
}
else {
%>
function hasAllowAddDelete() {
	frmExemptionList.Addnew.disabled = true;
	frmExemptionList.Delete.disabled = true;
}
<%
}
%>

hasAllowAddDelete();
</SCRIPT>
</BODY>
</HTML>