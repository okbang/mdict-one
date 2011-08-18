<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, fpt.timesheet.bean.*,
    fpt.timesheet.bean.Approval.*,
    fpt.timesheet.framework.util.StringUtil.*,
    fpt.timesheet.InputTran.ejb.TimeSheetInfo,
    fpt.timesheet.Exemption.ejb.ExemptionBean,
    fpt.timesheet.bean.ExemptionInfoBean,
    fpt.timesheet.bean.ExemptionListBean,
    fpt.timesheet.bo.ComboBox.ProjectComboBO,
    java.util.Calendar,
    java.util.Collection,
    java.util.Iterator"
%>
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
	UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ExemptionInfoBean beanExemptionInfo = (ExemptionInfoBean)request.getAttribute("beanExemptionInfo");
    ExemptionListBean beanExemptionList = (ExemptionListBean)request.getAttribute("beanExemptionList");

    StringMatrix smtGroupList = beanExemptionList.getGroupList();
    StringMatrix smtDevList = beanExemptionList.getDevList();
%>
<HTML>
<HEAD>
<TITLE>Exemption Add</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT src='scripts/popcalendar.js'></SCRIPT>
</HEAD>

<BODY bgcolor="#336699" leftmargin="0" topmargin="0" style="FONT-FAMILY: tahoma, sans-serif; FONT-SIZE: 11px" onkeypress='javascript:setKeypress_Save(event.which)'>
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H3><FONT color="yellow">Add Timesheet Exemption</FONT></H3>

<FORM method="post" action="TimesheetServlet" name="frmExemptionList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="arrSelectedID" value="">
<INPUT type="hidden" name="hidExemptionId" value="">
<INPUT type="hidden" name="hidDevId" value="<%=beanExemptionInfo.getDeveloperId()%>">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">

<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName()%></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR>

<FONT color="#ffffff" class="label1">&nbsp;Date format:&nbsp;</FONT><FONT class="labelDate" color="yellow">&nbsp;(mm/dd/yy)</FONT>
<HR noshade size="1">
<div align="center">
<TABLE border="0" cellpadding="1" cellspacing="3" width="100%" id="table1">
        <colgroup>
        	<col width = "40%"></col>
        	<col width = "160%"></col>
        </colgroup>
<%
    // Display error message for checking existing exemption
    // If have an error message --> display
    if (request.getAttribute("ERROR_MESSAGE") != null) {
%>
        <TR>
			<TD width="20%">&nbsp;</TD><TD width="51%" colspan="2">
			<font class="label1" color="red"><b><%=request.getAttribute("ERROR_MESSAGE")%></b></font>
			</TD>
			<TD width="28%">&nbsp;</TD>
		</TR>
        <TR>
			<TD width="20%">&nbsp;</TD><TD width="12%">&nbsp;</TD>
			<TD width="39%" align="center">&nbsp;</TD>
			<TD width="28%">&nbsp;</TD>
		</TR>
<%
		//Not removeAttribute --> must use below
        //request.removeAttribute("ERROR_MESSAGE");
    }
%>
        <TR>
		<td width="20%">&nbsp;</TD><TD width="12%"><strong><font class="label1">
			<font color="#ffffff">Group </font><font color="#FF0000">*</font></font></strong></td>
		<td width="39%" align="center">
		<p align="left">
           	<SELECT name="cboGroupName" class="VerySmallCombo" onchange="selectGroup();">
        	<%for (int nRow = 0; nRow < smtGroupList.getNumberOfRows(); nRow++) {
        		String strText = smtGroupList.getCell(nRow, 0);
        		out.write("<OPTION value='" + strText + "'" + (strText.equals(beanExemptionInfo.getGroupName()) ? "selected" : "") + ">" + strText + "</OPTION>");
    		}%>
        	</SELECT>
        </TD>
		<TD width="28%">&nbsp;</TD>
		</tr>
        <TR>
		<TD width="20%">&nbsp;</TD><TD width="12%"><strong><font class="label1">
		<font color="#ffffff">Name 
		</font><font color="#FF0000">*</font></font></strong></TD>
		<TD width="39%" align="center">
		<p align="left"><SELECT name="cboDev" class="BigCombo"></SELECT>
    	</TD>
		<TD width="28%">&nbsp;</TD>
	</TR>
	<TR>
		<TD width="20%">&nbsp;</TD><TD width="12%">
		<strong><font class="label1"><font color="#ffffff">Type </font>
		<font color="#FF0000">*</font></font></strong></TD>
		<TD width="39%" align="center">
		<p align="left">
			<input type="radio" name="radType" value="1" <%=beanExemptionInfo.getType() == 1 ? "checked" : ""%> onclick="javascript:selectDate()" <%if (beanExemptionInfo.getDeveloperId() == 0) {%> checked <%}%>>
			<font color="#ffffff" class="label1"><span style="font-weight: 400">Permanent</span></font>&nbsp;
			<input type="radio" name="radType" value="2" <%=beanExemptionInfo.getType() == 2 ? "checked" : ""%> onclick="javascript:selectDate()">
			<font color="#ffffff" class="label1"><span style="font-weight: 400">Temporary</span></font>&nbsp;
			<input type="radio" name="radType" value="3" <%=beanExemptionInfo.getType() == 3 ? "checked" : ""%> onclick="javascript:selectDate()">
			<font color="#ffffff" class="label1"><span style="font-weight: 400">Weekly</span></font><TD width="28%">&nbsp;
		</TD>
	</TR>
	<TR>
		<TD width="20%">&nbsp;</TD><TD width="12%">
		<strong><font class="label1">
		<font color="#ffffff">Weekday </font><font color="#FF0000">*</font></font></strong></TD>
		<TD width="39%" align="center">
		<table border="0" width="100%">
			<tr>
				<td style="border-style: solid; border-width: 1px">
		<table border="0" width="100%" id="table2">
			<tr>
				<td align="right"><input type="checkbox" name="chkSunday" value="<%=Calendar.SUNDAY%>"<%=("1".equals(beanExemptionInfo.getSunday())) ? " checked" : ""%>></td>
				<td><font color="#ffffff" class="label1"><span style="font-weight: 400">Sunday</span></font></td>
				<td align="right"><input type="checkbox" name="chkMonday" value="<%=Calendar.MONDAY%>"<%=("2".equals(beanExemptionInfo.getMonday())) ? " checked" : ""%>></td>
				<td><font color="#ffffff" class="label1"><span style="font-weight: 400">Monday</span></font></td>
				<td align="right"><input type="checkbox" name="chkTuesday" value="<%=Calendar.TUESDAY%>"<%=("3".equals(beanExemptionInfo.getTuesday())) ? " checked" : ""%>></td>
				<td><font color="#ffffff" class="label1"><span style="font-weight: 400">Tuesday</span></font></td>
				<td align="right"><input type="checkbox" name="chkWednesday" value="<%=Calendar.WEDNESDAY%>"<%=("4".equals(beanExemptionInfo.getWednesday())) ? " checked" : ""%>></td>
				<td><font color="#ffffff" class="label1"><span style="font-weight: 400">Wednesday</span></font></td>
			</tr>
			<tr>
				<td align="right"><input type="checkbox" name="chkThursday" value="<%=Calendar.THURSDAY%>"<%=("5".equals(beanExemptionInfo.getThursday())) ? " checked" : ""%>></td>
				<td><font color="#ffffff" class="label1"><span style="font-weight: 400">Thursday</span></font></td>
				<td align="right"><input type="checkbox" name="chkFriday" value="<%=Calendar.FRIDAY%>"<%=("6".equals(beanExemptionInfo.getFriday())) ? " checked" : ""%>></td>
				<td><font color="#ffffff" class="label1"><span style="font-weight: 400">Friday</span></font></td>
				<td align="right"><input type="checkbox" name="chkSaturday" value="<%=Calendar.SATURDAY%>"<%=("7".equals(beanExemptionInfo.getSaturday())) ? " checked" : ""%>></td>
				<td><font color="#ffffff" class="label1"><span style="font-weight: 400">Saturday</span></font></td>
				<td align="right">&nbsp;</td><td>&nbsp;</td>
			</tr>
		</table>
				</td>
			</tr>
		</table>
		<TD width="28%">&nbsp;</TD>
	</TR>
	<TR>
		<TD width="20%">&nbsp;</TD><TD width="12%"><strong><font class="label1">
		<font color="#ffffff">From date
		</font><font color="#FF0000">*</font><font color="#ffffff"> </font>
		</font></strong> </TD>
		<TD width="39%" align="center">
		<p align="left">
        <INPUT type="text" name="txtFromDate" size="20" maxlength="8" value="<%=(beanExemptionInfo.getFromDate() == null) ? "" : beanExemptionInfo.getFromDate() %>" class="SmallTextbox">
            <IMG name="calFromDate" src="image/cal.gif" style="CURSOR:hand" onclick='if ( (document.frmExemptionList.radType[1].checked == true) || (document.frmExemptionList.radType[2].checked == true) ) showCalendar(txtFromDate, txtFromDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
		<TD width="28%">&nbsp;</TD>
	</TR>
	<TR>
		<TD width="20%">&nbsp;</TD><TD width="12%"><strong><font class="label1">
		<font color="#ffffff">To date </font><font color="#FF0000">*</font></font></strong></TD>
		<TD width="39%" align="center">
		<p align="left">
            <INPUT type="text" name="txtToDate" size="20" value="<%=(beanExemptionInfo.getToDate() == null) ? "" : beanExemptionInfo.getToDate() %>" maxlength="8" class="SmallTextbox">
            <IMG name="calToDate" src="image/cal.gif" style="CURSOR:hand" onclick='if ( (document.frmExemptionList.radType[1].checked == true) || (document.frmExemptionList.radType[2].checked == true) ) showCalendar(txtToDate, txtToDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
		<TD width="28%">&nbsp;</TD>
	</TR>
	<TR>
		<TD width="20%">&nbsp;</TD><TD width="12%"><strong><font class="label1">
		<font color="#ffffff">Reason</font></font></strong></TD>
		<TD width="39%" align="center">
		<p align="left">
		<TEXTAREA rows="3" name="txtReason" cols="46"><%=(beanExemptionInfo.getReason() == null) ? "" : beanExemptionInfo.getReason() %></TEXTAREA></TD>
		<TD width="28%">&nbsp;</TD>
	</TR>
	<TR>
		<TD width="20%">&nbsp;</TD><TD width="12%"><strong><font class="label1">
		<font color="#ffffff">Note</font></font></strong></TD>
		<TD width="39%" align="center">
		<p align="left">
		<TEXTAREA rows="3" name="txtNote" cols="46"><%=(beanExemptionInfo.getNote() == null) ? "" : beanExemptionInfo.getNote() %></TEXTAREA></TD>
		<TD width="28%">&nbsp;</TD>
	</TR>
	<TR>
		<TD width="20%">&nbsp;</TD><TD width="12%">&nbsp;</TD>
		<TD width="39%" align="center">&nbsp;</TD>
		<TD width="28%">&nbsp;</TD>
	</TR>
	<TR>
		<TD width="20%">&nbsp;</TD><TD width="12%">&nbsp;</TD>
		<TD width="39%" align="center">
			<INPUT type="button" name="cmdSave"  value="  Save  " onclick='javascript:doSave()' class="Button">
			<INPUT type="button"  name="cmdReset" value="  Reset  " onclick='javascript:doReset()' class="Button">
			<INPUT type="button" name="cmdBack"  value="  Back  " onclick='javascript:doBack()' class="Button">
		</TD>
		<TD width="28%">&nbsp;</TD>
	</TR>
	</TABLE>
</DIV>
</DIV>
</FORM>

<SCRIPT language="javascript">
var dev = new Array();
<%
    for (int i = 0; i < beanExemptionList.getDevList().getNumberOfRows(); i++) {
    	String strDev = beanExemptionList.getDevList().getCell(i, 1) + "  -  " + beanExemptionList.getDevList().getCell(i, 2);
        out.write("\ndev[" + i + "]=new Array(3);");
        out.write("dev[" + i + "][0]=" + beanExemptionList.getDevList().getCell(i, 0) + ";"); 	//DevID
        out.write("dev[" + i + "][1]='" + strDev + "';");										//Account, DevName
        out.write("dev[" + i + "][2]='" + beanExemptionList.getDevList().getCell(i, 3) + "';");	//GroupName
    }
%>
<%
String strRole = beanUserInfo.getRole();
if ( strRole.equals("1111110000") || strRole.equals("1110000000") || strRole.equals("1000100000") || strRole.equals("1100100000") ) {
%>
function doSave() {
   	var form = document.forms[0];
    if (!isValidForm()) {
    	return;
    }
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "ExemptionSaveAdd";
    form.action = "TimesheetServlet";
    form.submit();
}
<%
}
else {
%>
function doSave() {
	alert('Sorry, unauthorized access. You are not Admin, Manager, QA, GL');
}
<%
}
%>

function doReset() {
	var form = document.forms[0];
	form.reset();
	selectGroup();

	if (form.hidDevId.value == 0) {
		form.radType[0].checked 	= true;
		form.chkSunday.disabled 	= true;
		form.chkMonday.disabled 	= true;
		form.chkTuesday.disabled 	= true;
		form.chkWednesday.disabled  = true;
		form.chkThursday.disabled 	= true;
		form.chkFriday.disabled 	= true;
		form.chkSaturday.disabled 	= true;
	}
	else {
		selectType();
	}
}

function doBack() {
    var form = document.forms[0];
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "ExemptionList";
    form.action = "TimesheetServlet";
    form.submit();
}

function isValidForm() {
    var count;
    var flag = true;
    var form = document.forms[0];

    if (form.radType[2].checked) {
	    for (var i = 0; i < document.forms[0].elements.length; i++) {
	        var e = document.forms[0].elements[i];
	        if (e.type == "checkbox") {
	            if (e.checked == 1) {
	                flag = false;
	            }
	        }
	    }
	    if (flag) {
	        alert("Please select Week Day");
	        return false;
	    }
    }
    if (form.txtFromDate.value.length > 0 ) {
		if (isValidate(form.txtFromDate.value)==false) {
			form.txtFromDate.focus();
			return false;
		}
    }
    if (form.txtToDate.value.length > 0 ) {
        if (isValidate(form.txtToDate.value)==false) {
    		form.txtToDate.focus();
    		return false;
    	}
    }
    if ((form.txtFromDate.value.length > 0) && (form.txtToDate.value.length > 0)) {
        if (compareDate(form.txtFromDate , form.txtToDate) > 0) {
            alert("From Date must lower or equal To Date");
            form.txtFromDate.focus();
            return false;
        }
    }
    if (form.radType[1].checked || form.radType[2].checked) {
        if (form.txtFromDate.value.length <= 0) {
	        alert("Please enter From Date");
	        form.txtFromDate.focus();
	        return false;
	    }
	    if (form.txtToDate.value.length <= 0) {
	        alert("Please enter To Date");
	        form.txtToDate.focus();
	        return false;
	    }
    }
    return true;
}

function selectDate() {
	var form = document.forms[0];
	if ( form.radType[0].checked ) {
		form.chkSunday.disabled 	= true;
		form.chkMonday.disabled 	= true;
		form.chkTuesday.disabled 	= true;
		form.chkWednesday.disabled  = true;
		form.chkThursday.disabled 	= true;
		form.chkFriday.disabled 	= true;
		form.chkSaturday.disabled 	= true;

		form.txtFromDate.disabled 	= true;
		form.txtToDate.disabled 	= true;
		form.txtFromDate.value 		= "";
		form.txtToDate.value 		= "";
		form.txtReason.value 		= "";
		form.txtNote.value 			= "";

		form.chkSunday.checked 		= "";
		form.chkMonday.checked 		= "";
		form.chkTuesday.checked 	= "";
		form.chkWednesday.checked 	= "";
		form.chkThursday.checked 	= "";
		form.chkFriday.checked 		= "";
		form.chkSaturday.checked 	= "";
	}
	else if ( form.radType[1].checked ) {
		form.chkSunday.disabled 	= true;
		form.chkMonday.disabled 	= true;
		form.chkTuesday.disabled	= true;
		form.chkWednesday.disabled 	= true;
		form.chkThursday.disabled 	= true;
		form.chkFriday.disabled 	= true;
		form.chkSaturday.disabled 	= true;

		form.txtFromDate.disabled 	= false;
		form.txtToDate.disabled 	= false;
		form.txtFromDate.value 		= "";
		form.txtToDate.value 		= "";
		form.txtReason.value 		= "";
		form.txtNote.value 			= "";

		form.chkSunday.checked 		= "";
		form.chkMonday.checked 		= "";
		form.chkTuesday.checked 	= "";
		form.chkWednesday.checked 	= "";
		form.chkThursday.checked 	= "";
		form.chkFriday.checked 		= "";
		form.chkSaturday.checked 	= "";
	}
	else if (form.radType[2].checked) {
		form.chkSunday.disabled 	= false;
		form.chkMonday.disabled 	= false;
		form.chkTuesday.disabled 	= false;
		form.chkWednesday.disabled 	= false;
		form.chkThursday.disabled 	= false;
		form.chkFriday.disabled 	= false;
		form.chkSaturday.disabled 	= false;

		form.txtFromDate.disabled 	= false;
		form.txtToDate.disabled 	= false;
		form.txtFromDate.value 		= "";
		form.txtToDate.value 		= "";
		form.txtReason.value 		= "";
		form.txtNote.value 			= "";
	}
}

function selectType() {
	var form = document.forms[0];
	if ( form.radType[0].checked ) {
		form.chkSunday.disabled 	= true;
		form.chkMonday.disabled 	= true;
		form.chkTuesday.disabled 	= true;
		form.chkWednesday.disabled  = true;
		form.chkThursday.disabled 	= true;
		form.chkFriday.disabled 	= true;
		form.chkSaturday.disabled 	= true;
		form.txtFromDate.disabled 	= true;
		form.txtToDate.disabled 	= true;
	}
	else if ( form.radType[1].checked ) {
		form.chkSunday.disabled 	= true;
		form.chkMonday.disabled 	= true;
		form.chkTuesday.disabled	= true;
		form.chkWednesday.disabled 	= true;
		form.chkThursday.disabled 	= true;
		form.chkFriday.disabled 	= true;
		form.chkSaturday.disabled 	= true;
		form.txtFromDate.disabled 	= false;
		form.txtToDate.disabled 	= false;
	}
}

function doBlank() {
	var form = document.forms[0];
	form.radType[0].checked 	= true;
	form.chkSunday.disabled 	= true;
	form.chkMonday.disabled 	= true;
	form.chkTuesday.disabled 	= true;
	form.chkWednesday.disabled  = true;
	form.chkThursday.disabled 	= true;
	form.chkFriday.disabled 	= true;
	form.chkSaturday.disabled 	= true;
	form.txtFromDate.disabled 	= true;
	form.txtToDate.disabled 	= true;

	form.txtFromDate.value 		= "";
	form.txtToDate.value 		= "";
	form.txtReason.value 		= "";
	form.txtNote.value 			= "";
}

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
    var form = document.forms[0];
    while (form.cboDev.options.length > 0) {
        form.cboDev.options[0] = null;
    }

    if ((form.cboGroupName.value != "All") && (form.cboGroupName.value.length > 0)) {
        for (var i = 0; i < dev.length; i++) {
           if (dev[i][2] == form.cboGroupName.value) {
               var intDevId = form.hidDevId.value;
	           if (intDevId == dev[i][0]) {
				   form.txtNote.focus();
				   appendOption(form.cboDev, dev[i][0], dev[i][1], dev[i][2], true);
               }
               else {
	               appendOption(form.cboDev, dev[i][0], dev[i][1], dev[i][2], false);
               }
           }
        } //end for
    }
    else {
        for (var i = 0; i < dev.length; i++) {
            appendOption(form.cboDev, dev[i][0], dev[i][1], dev[i][2], false);
        }
    }
    return true;
}

<%
if ( strRole.equals("1111110000") || strRole.equals("1000100000") || strRole.equals("1100100000") ) {
%>
function isPQA() {
	frmExemptionList.cboGroupName.disabled = false;
}
<%
}
else if ( strRole.equals("1110000000") ) {
%>
function isPQA() {
	frmExemptionList.cboGroupName.disabled = true;
}
<%
}
else {
%>
function isPQA() {
	frmExemptionList.cboGroupName.disabled = true;
}
<%
}
%>

isPQA();
selectGroup();
doBlank();
selectDate();

<%
if (request.getAttribute("ERROR_MESSAGE") != null) {
%>
selectType();
<%
}
%>

</SCRIPT>
</BODY>
</HTML>