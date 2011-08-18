<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" import="
   	javax.servlet.*,
   	fpt.timesheet.bean.*,
  	fpt.timesheet.bean.Report.WeeklyReportBean,
  	fpt.timesheet.bean.Report.SummaryReportBean,
   	fpt.timesheet.framework.util.CommonUtil.*,
   	fpt.timesheet.constant.Timesheet,
   	fpt.timesheet.framework.util.StringUtil.StringMatrix"
%>
<%@page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    String strRole = beanUserInfo.getRole();
    WeeklyReportBean beanWeeklyReport = (WeeklyReportBean)request.getAttribute("beanWeeklyReport");
%>
<%
    String strDisabled = "";
    if (beanUserInfo.getStatus().equals("3")) {
        strDisabled = "disabled"; //External
    }
	StringMatrix smtGroupList = beanWeeklyReport.getGroupList();
%>
<HTML>
<HEAD>
<TITLE>Lack Timesheet Group</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<LINK rel="stylesheet" type="text/css" href="styles/locked-column.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT src='scripts/popcalendar.js'></SCRIPT>
</HEAD>

<BODY bgcolor="#336699" onkeypress='javascript:setKeypress()'>
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H3><FONT color="yellow">Lack Timesheet Group</FONT></H3>

<FORM method="POST" action="TimesheetServlet" name="frmLackTSGroup">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidProjectsList" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="hidTypeOfView" value="LackTSGroup">

<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName() %></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR>

<TABLE border="0" cellpadding="2" cellspacing="0" width="100%" align="center" id="table1">
     <TR>
    	<!----------------------------- GROUP ------------------------------------>
    	<TD width="9%"><STRONG><FONT color="#ffffff" class="label1">Group</FONT></STRONG></TD>
    	<TD width="18%">
    	<%if ( strRole.equals("1111110000") || strRole.equals("1000100000") ) {%>
        	<SELECT name="cboGroup" class="BigCombo">
        	<%for (int nRow = 0; nRow < smtGroupList.getNumberOfRows(); nRow++) {
        		String strText = smtGroupList.getCell(nRow, 0);
        		out.write("<OPTION value='" + strText + "'" + (strText.equals(beanWeeklyReport.getGroup()) ? "selected" : "") + ">" + strText + "</OPTION>");
    		}%>
        	</SELECT>
        <%}else {%>
        	<SELECT name="cboGroup" class="BigCombo" disabled>
        		<option value="<%=beanWeeklyReport.getGroup()%>"><%=beanWeeklyReport.getGroup()%></option>
        	</SELECT>
        <%}%>
        </TD>
        <!----------------------------- FROM DATE ---------------------------------->
        <TD width="11%">&nbsp;</TD>
        <TD width="11%"><STRONG><FONT color="#ffffff" class="label1">From date </FONT>
		<FONT color="#FF0000" class="label1">*</FONT></STRONG></TD>
        <TD width="20%">
        	<INPUT type="text" name="txtFromDateTracking" size="20" value="<%=beanWeeklyReport.getSearchFromDate()%>" maxlength="8" class="smallTextbox">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, txtFromDateTracking, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
    	<!----------------------------- REPORT ------------------------------------>
        <TD align="left" width="9%"><STRONG>
		<FONT color="#ffffff" class="label1">Report</FONT></STRONG></TD>
        <TD>
        	<SELECT size="1" name="cboReportType" class="BigCombo" value="<%=beanWeeklyReport.getReportType()%>">
	        	<OPTION <%=beanWeeklyReport.getReportType() == Timesheet.REPORTTYPE_LACKTS_GROUP ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_LACKTS_GROUP%>">Lack Timesheet by Group</OPTION>
				<OPTION <%=beanWeeklyReport.getReportType() == Timesheet.REPORTTYPE_LACKTS_PROJECT ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_LACKTS_PROJECT%>">Lack Timesheet by Project</OPTION>
				<OPTION <%=beanWeeklyReport.getReportType() == Timesheet.REPORTTYPE_UNAPPROVED_PM ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_UNAPPROVED_PM%>">Unreviewed by Project Manager</OPTION>
				<OPTION <%=beanWeeklyReport.getReportType() == Timesheet.REPORTTYPE_UNAPPROVED_QA ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_UNAPPROVED_QA%>">Unreviewed by Quality Assurance</OPTION>
	        </SELECT>
	    </TD>
    </TR>
	<TR>
		<!----------------------------- LOG BY ------------------------------------->
        <TD width="9%"><FONT color="#ffffff" class="label1">Log By </FONT>
		<FONT color="#FF0000" class="label1">*</FONT></TD>
        <TD align="left" width="18%">
        	<INPUT type="text" name="txtLogDateTracking" size="8" value=<%=beanWeeklyReport.getLogDate()%> maxlength="8" class="verySmallTextbox"> 
			<IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, txtLogDateTracking, "mm/dd/yy",null,1,-1,-1,true)'><font class="label1"><font color="#ffffff">&nbsp;&nbsp;&nbsp;&nbsp; Time</font></font>
			<INPUT type="text" name="txtLogTimeTracking" size="5" value=<%=beanWeeklyReport.getLogTime()%> maxlength="5" class="verySmallTextbox">
  	    </TD>
        <TD align="left" width="11%">&nbsp;</TD>
        <!----------------------------- TO DATE ------------------------------------>
        <TD align="left" width="11%"><STRONG><FONT color="#ffffff" class="label1">To date </FONT>
		<FONT color="#FF0000" class="label1">*</FONT></STRONG></TD>
        <TD width="20%">
        	<INPUT type="text" name="txtToDateTracking" size="20" value="<%=beanWeeklyReport.getSearchToDate()%>" maxlength="8" class="smallTextbox">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, txtToDateTracking, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
	    <!----------------------------- SEARCH ------------------------------------->
	    <TD align="left" width="9%">&nbsp;</TD>
        <TD align="left">
	        <INPUT type="button" name="Search" value="  Search  " class="Button" onclick="javascript:doSearch()">
        </TD>
    </TR>
    <TR>
        <TD width="7%">&nbsp;</TD>
        <TD width="18%">&nbsp;</TD>
        <TD width="11%"></TD>
        <TD width="11%"></TD>
        <TD width="20%"></TD>
        <TD width="9%"></TD>
        <TD align="left">&nbsp;</TD>
    </TR>
</TABLE>
<font class="label1" color="#ffffff">Date format:&nbsp;</font><font class="labelDate" color="yellow">&nbsp;(mm/dd/yy)</font>
<HR noshade size="1">
<BR>
<CENTER>
<%
    StringMatrix mtxReport = beanWeeklyReport.getReportList();    
    if (mtxReport != null) {
        int row, col;
        int nCols = mtxReport.getNumberOfCols();
       	int nRows = mtxReport.getNumberOfRows();
       	
       	String strWidth= "";
       	if (nCols <=10){
       		strWidth = "100%";
       	}
       	else if ((nCols >=10) && (nCols <=20)){
       		strWidth = "180%";
       	}
       	else if ((nCols >=20) && (nCols <=35)){
       		strWidth = "250%";
       	}
       	
       	String strHeight = "";
       	if (nRows <=12){
       		strHeight = "320px";
       	}
       	else if (nRows >12){
       		strHeight = "480px";
       	}
%>
<DIV id=tbl-container style="MARGIN: 0px; OVERFLOW-X: auto; OVERFLOW-Y: auto; PADDING-BOTTOM: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 0px; WIDTH: 95%; HEIGHT: <%=strHeight%> " align="center">
<TABLE border="0" cellspacing="1" bgcolor="#336699" width=<%=strWidth%> >
<%
        //This For loop has purpose of get Title of Table 
        //Group name Full name Account 08/12/06 08/14/06 08/15/06 08/16/06 08/17/06 08/18/06 
        // row 0..
        out.println("<COLGROUP>");
        for (col = 0; col < nCols; col++) {
            if (col == 0) {
                strWidth = " width='50px'";
            }
            else if (col == 1) {
                strWidth = " width='60px'";
            }
            else if (col == 2) {
                strWidth = " width='200px'";
            }
            else if (col == 3) {
                strWidth = " width='150px'";
            }
            else {
                strWidth = " width='50px'";
            }
            out.println("<COL" + strWidth + " align='Center'>");
        }

        out.write("<THEAD>");
        out.write("<TR>");
        for (col = 0; col < nCols; col++) {
            out.write("<TH>" +mtxReport.getCell(0, col)+"</TH>");
        }
        out.println("</TR>");
        out.println("</THEAD>");
        out.println("<TBODY>");
		//This	For loop has purpose of get all the data of table
        // row 1-n..
        for (row = 1; row < nRows; row++) {
            String strClass = (row % 2 == 1) ? "Row1" : "Row2";
            out.write("<TR class = \"" + strClass + "\" >");
			for (col = 0; col < nCols; col++) {
                if (col > 1) {
               		out.write("<TD>" + mtxReport.getCell(row, col) + "</TD>");
                }
                else {
               		out.write("<TD>" + mtxReport.getCell(row, col) + "</TD>");
                }
            }
            out.println("</TR>");
        }
        out.println("</TBODY>");
%>
</TABLE>
</DIV>
<%
    }
%>
</CENTER>
</DIV>
</FORM>

<SCRIPT language="javascript">
function doSearch() {
    var form = document.forms[0];
    if (!isValidForm()) {
    	return;
    }
    form.hidAction.value = "RA";
    form.hidActionDetail.value = "LackTSGroup";
    form.action = "TimesheetServlet";
    form.submit();
}

function isValidForm() {
    var count;
    var form = document.forms[0];

    if (!IsValidTime(form.txtLogTimeTracking.value)) {
    	form.txtLogTimeTracking.focus();
    	return false;
    }
    if (form.txtFromDateTracking.value.length > 0 ) {
		if (isValidate(form.txtFromDateTracking.value)==false) {
			form.txtFromDateTracking.focus();
			return false;
		}
    }
    if (form.txtToDateTracking.value.length > 0 ) {
        if (isValidate(form.txtToDateTracking.value)==false) {
    		form.txtToDateTracking.focus();
    		return false;
    	}
    }
    if (form.txtFromDateTracking.value.length <= 0) {
        alert("Please enter From Date");
        form.txtFromDateTracking.focus();
        return false;
    }
    if (form.txtToDateTracking.value.length <= 0) {
        alert("Please enter To Date");
        form.txtToDateTracking.focus();
        return false;
    }
    if (daysBetween(form.txtFromDateTracking.value, form.txtToDateTracking.value) > 30) {
        alert("Days between From Date and To Date must lower than 31 days");
        form.txtFromDateTracking.focus();
        return false;
    }
    if ((form.txtFromDateTracking.value.length > 0) && (form.txtToDateTracking.value.length > 0)) {
        if (compareDate(form.txtFromDateTracking , form.txtToDateTracking) > 0) {
            alert("From Date must lower or equal To Date");
            form.txtFromDateTracking.focus();
            return false;
        }
    }
    if (form.txtLogDateTracking.value.length <= 0) {
        alert("Please enter Log Date");
        form.txtLogDateTracking.focus();
        return false;
    }
    if (form.txtLogDateTracking.value.length > 0 ) {
        if (isValidate(form.txtLogDateTracking.value)==false) {
    		form.txtLogDateTracking.focus();
    		return false;
    	}
    }
    if (compareDate(form.txtToDateTracking, form.txtLogDateTracking) > 0) {
        alert("Log Date must be greater than or equal To Date");
        form.txtLogDateTracking.focus();
        return false;
    }
    return true;
}

function setKeypress() {
    if (window.event.keyCode==13) doSearch();
}
</SCRIPT>
</BODY>
</HTML>