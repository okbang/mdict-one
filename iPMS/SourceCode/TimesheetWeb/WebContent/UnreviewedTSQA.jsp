<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" import="
   	javax.servlet.*, 
   	fpt.timesheet.bean.*,
  	fpt.timesheet.bean.Report.WeeklyReportBean,
  	fpt.timesheet.bean.Report.SummaryReportBean,
  	fpt.timesheet.bo.ComboBox.ProjectComboBO,
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
%>
<%
    StringMatrix mtxGroup = beanWeeklyReport.getGroupList();
    StringMatrix smtProjectList = beanWeeklyReport.getProjectList();
    smtProjectList.setCell(0, 0, "0");
    
    StringMatrix smtGroupList = beanWeeklyReport.getGroupList();
    StringMatrix smtPQANameList = beanWeeklyReport.getPQANameList();
    StringMatrix smtProjecStatustList = beanWeeklyReport.getProjectStatusList();
%>
<HTML>
<HEAD>
<TITLE>Unapproved By Process Quality Assuarance</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT src='scripts/popcalendar.js'></SCRIPT>
</HEAD>

<BODY bgcolor="#336699" onkeypress='javascript:setKeypress()'>
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H3><FONT color="yellow">Unreviewed Timesheet Quality Assurance</H3>

<FORM method="POST" action="TimesheetServlet" name="frmLackTSProject">
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
    	<TD width="9%">
    		<STRONG><FONT color="#ffffff" class="label1">Group</FONT></STRONG>
    	</TD>
    	<TD width="18%">
	   	<%if ( strRole.equals("1111110000") || strRole.equals("1000100000") ) {%>
        	<SELECT name="cboGroup" class="BigCombo" onchange="changeCombo()">
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
        <TD width="11%">
        	<STRONG><FONT color="#ffffff" class="label1">From date</FONT></STRONG>
        <FONT color="yellow">
        	<FONT color="#FF0000" class="label1">*</FONT></TD>
        <TD width="20%">
        	<INPUT type="text" name="txtFromDateUnreview" size="20" value="<%=beanWeeklyReport.getSearchFromDate()%>" maxlength="8" class="smallTextbox">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, txtFromDateUnreview, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
    	<!----------------------------- REPORT ------------------------------------>    	
        <TD align="left" width="9%">
        	<STRONG><FONT color="#ffffff" class="label1">Report</FONT></STRONG>
		</TD>
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
		<!----------------------------- PROJECT ------------------------------------->
        <TD width="9%">
        	<STRONG><FONT color="#ffffff" class="label1">Project</FONT></STRONG>
        </TD>
        <TD align="left" width="18%">
        	<SELECT name="cboProject" class="BigCombo" value="<%=beanWeeklyReport.getProject()%>">
        	<%for (int nRow = 0x00; nRow < smtProjectList.getNumberOfRows(); nRow++) {
	    	    int nValue = Integer.parseInt(smtProjectList.getCell(nRow, 0));
	    	    String strText = smtProjectList.getCell(nRow, 1);
	    	    out.write("<OPTION ");
	    	    out.write(beanWeeklyReport.getProject() == nValue ? " selected " : " ");
	    	    out.write("value='" + nValue + "'>" + strText + "</OPTION>");
	    	}%>
        	</SELECT>
      	</TD>
        <TD align="left" width="11%">&nbsp;</TD>
        <!----------------------------- TO DATE ------------------------------------>
        <TD align="left" width="11%"><STRONG><FONT color="#ffffff" class="label1">To date</FONT></STRONG><FONT color="yellow"><FONT color="#FF0000" class="label1"> *</FONT></TD>
        <TD width="20%">
        	<INPUT type="text" name="txtToDateUnreview" size="20" value="<%=beanWeeklyReport.getSearchToDate()%>" maxlength="8" class="smallTextbox">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, txtToDateUnreview, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
	    <!----------------------------- PROJECT STATUS ------------------------------------->
		<TD align="left" width="9%"><STRONG>
		<FONT color="#ffffff" class="label1">Project Status</FONT></STRONG>
		</TD>
        <TD>
        	<SELECT name="cboProjectStatus" onchange="changeCombo()" size="1" class="BigCombo">
				<%String strCurrentProjectStatus = Integer.toString(beanWeeklyReport.getProjectStatus());
		    	for (int nRow = 0; nRow < smtProjecStatustList.getNumberOfRows(); nRow++) {
		    	    String strValue = smtProjecStatustList.getCell(nRow, 0);
		    	    String strText = smtProjecStatustList.getCell(nRow, 1);
				}%>
            	<OPTION value="<%=beanWeeklyReport.PROJECT_STATUS_ALL%>" <%=beanWeeklyReport.getProjectStatus() == beanWeeklyReport.PROJECT_STATUS_ALL ? "selected" : ""%>>	All status</OPTION>
            	<OPTION value="<%=beanWeeklyReport.PROJECT_STATUS_ONGOING%>" <%=beanWeeklyReport.getProjectStatus() == beanWeeklyReport.PROJECT_STATUS_ONGOING?"selected" : ""%>>On-going</OPTION>
				<OPTION value="<%=beanWeeklyReport.PROJECT_STATUS_TENTATIVE%>" <%=beanWeeklyReport.getProjectStatus() == beanWeeklyReport.PROJECT_STATUS_TENTATIVE?"selected" : ""%>>Tentative</OPTION>
			</SELECT>
		</TD>
    </TR>
    <TR>
    <!----------------------------- LOG BY ------------------------------------->
        <TD width="7%">
        	<FONT color="#ffffff" class="label1">Log By </FONT><FONT color="yellow">
        	<FONT color="#FF0000" class="label1">*</FONT></TD>
        <TD width="18%">
        	<INPUT type="text" name="txtLogDateUnreview" size="8" value=<%=beanWeeklyReport.getLogDate()%> maxlength="8" class="verySmallTextbox"> 
			<IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, txtLogDateUnreview, "mm/dd/yy",null,1,-1,-1,true)'><font class="label1"><font color="#ffffff">&nbsp;&nbsp;&nbsp;&nbsp; Time</font></font>
			<INPUT type="text" name="txtLogTimeUnreview" size="5" value=<%=beanWeeklyReport.getLogTime()%> maxlength="5" class="verySmallTextbox"></TD>
        <TD width="11%"></TD>
        <TD width="11%"><STRONG><FONT color="#ffffff" class="label1">PQA Name</FONT></STRONG></TD>
        <TD width="20%">
	        <SELECT name="cboPQAName" class="VerySmallCombo">
        	<%for (int nRow = 0; nRow < smtPQANameList.getNumberOfRows(); nRow++) {
        		String strText = smtPQANameList.getCell(nRow, 0);
        		out.write("<OPTION value='" + strText + "'" + (strText.equals(beanWeeklyReport.getPQAName()) ? "selected" : "") + ">" + strText + "</OPTION>");
    		}%>
        	</SELECT>
	    </TD>
        <TD width="9%"></TD>
        <TD align="left">
	        <INPUT type="button" name="Search" value="  Search  " class="Button" onclick="javascript:doSearch()">
	    </TD>
    </TR>
    <TR>
        <TD width="7%">&nbsp;</TD>
        <TD width="18%">&nbsp;</TD>
        <TD width="11%">&nbsp;</TD>
        <TD width="11%">&nbsp;</TD>
        <TD width="20%">&nbsp;</TD>
        <TD width="9%">&nbsp;</TD>
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
<DIV style="MARGIN: 0px; OVERFLOW-X: auto; OVERFLOW-Y: auto; PADDING-BOTTOM: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 0px; WIDTH: 95%; HEIGHT: <%=strHeight%> " align="center">

<TABLE border="0" cellspacing="1" bgcolor="#336699" width=<%=strWidth%> >
    <COLGROUP>
        <COL width="15%">
        <COL width="10%">
        <COL width="15%">
        <COL width="10%">
        <COL width="15%">
        <COL width="30%">
    </COLGROUP>
<%
        //This For loop has purpose of get Title of Table 
        //Group name Full name Account 08/12/06 08/14/06 08/15/06 08/16/06 08/17/06 08/18/06 
        // row 0..
        out.write("<TR>");
        for (col = 0; col < nCols; col++) {
            out.write("<TD class = \"TableHeader1\">"+mtxReport.getCell(0, col)+"</TD>");
        }
        out.write("</TR>");

		//This	For loop has purpose of get all the data of table
        // row 1-n..
        for (row = 1; row < nRows; row++) {
            String strClass = (row % 2 == 1) ? "Row1" : "Row2";
            out.write("<TR class = \"" + strClass + "\" >");
			for (col = 0; col < nCols; col++) {
                if (col > 1) {                	
               		out.write("<TD align = 'center'>" + mtxReport.getCell(row, col) + "</TD>");
                }
                else {
               		out.write("<TD align = 'center'>" + mtxReport.getCell(row, col) + "</TD>");
                }
            }
            out.write("</TR>");
        }
%>
</TABLE>
<%
    }
%>
</DIV>
</CENTER>
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

    if (!IsValidTime(form.txtLogTimeUnreview.value)) {
    	form.txtLogTimeUnreview.focus();
    	return false;
    }
    if (form.txtFromDateUnreview.value.length <= 0) {
        alert("Please enter From Date");
        form.txtFromDateUnreview.focus();
        return false;
    }
    if (form.txtToDateUnreview.value.length <= 0) {
        alert("Please enter To Date");
        form.txtToDateUnreview.focus();
        return false;
    }
    if (form.txtFromDateUnreview.value.length > 0 ) {
		if (isValidate(form.txtFromDateUnreview.value)==false) {
			form.txtFromDateUnreview.focus();
			return false;
		}
    }
    if (form.txtToDateUnreview.value.length > 0 ) {
        if (isValidate(form.txtToDateUnreview.value)==false) {
    		form.txtToDateUnreview.focus();
    		return false;
    	}
    }
    if ((form.txtFromDateUnreview.value.length > 0) && (form.txtToDateUnreview.value.length > 0)) {
        if (compareDate(form.txtFromDateUnreview , form.txtToDateUnreview) > 0) {
            alert("From Date must lower or equal To Date");
            form.txtFromDateUnreview.focus();
            return false
        }
    }
    if (form.txtLogDateUnreview.value.length <= 0) {
        alert("Please enter Log Date");
        form.txtLogDateUnreview.focus();
        return false;
    }
    if (form.txtLogDateUnreview.value.length > 0 ) {
        if (isValidate(form.txtLogDateUnreview.value)==false) {
    		form.txtLogDateUnreview.focus();
    		return false;
    	}
    }
    if (compareDate(form.txtToDateUnreview, form.txtLogDateUnreview) > 0) {
        alert("Log Date must be greater than or equal To Date");
        form.txtLogDateUnreview.focus();
        return false;
    }
    return true;
}

function setKeypress() {
    if (window.event.keyCode==13) doSearch();
}
</SCRIPT>

<SCRIPT>
    <%
    out.write("var P_COUNT=" + (smtProjectList.getNumberOfRows() - 1) + ";");
    out.write("\nvar prj = new Array(P_COUNT);");
    for (int nRow = 1; nRow < smtProjectList.getNumberOfRows(); nRow++) {
        // Project_ID, Code, Group, Status --> ProjectComboEJB
        out.write("\nprj[" + (nRow - 1) + "]=new Array(4);");
        out.write("prj[" + (nRow - 1) + "][0]=" + smtProjectList.getCell(nRow, 0) + ";");
        out.write("prj[" + (nRow - 1) + "][1]='" + smtProjectList.getCell(nRow, 1) + "';");
        out.write("prj[" + (nRow - 1) + "][2]='" + smtProjectList.getCell(nRow, 3) + "';");
        out.write("prj[" + (nRow - 1) + "][3]='" + smtProjectList.getCell(nRow, 4) + "';");
    }
    %>
    
    function changeCombo() {    	
        var myForm = document.forms[0];
        var myElement;
        for (var i = myForm.cboProject.options.length - 1; i >= 0; i--) {
            myForm.cboProject.options[i] = null;
        }
        
        var bGroupAll = false;
        var bStatusAll = false;
        if (myForm.cboGroup.value == "All") {
            bGroupAll = true;
        }
        if (myForm.cboProjectStatus.value == <%=fpt.timesheet.constant.Timesheet.PROJECT_STATUS_ALL%>) {
            bStatusAll = true;
        }
        
        myElement = document.createElement("option");
        myElement.value = 0;
        myElement.text = "All projects";        
        myForm.cboProject.add(myElement);
        
        var bAddThis = false;
        myForm.hidProjectsList.value = "";
        //alert("HanhTN - P_COUNT = " + P_COUNT);
        for (i = 0; i < P_COUNT; i++) {
            bAddThis = false;
            if (bGroupAll && bStatusAll) {
                bAddThis = true;
            }
            else if (bGroupAll) {
                if (myForm.cboProjectStatus.value == prj[i][3]) {
                    bAddThis = true;
                }
            }
            else if (bStatusAll) {
                if (myForm.cboGroup.value == prj[i][2]) {
                    bAddThis = true;
                }
            }
            else {
                if ((myForm.cboGroup.value == prj[i][2]) && (myForm.cboProjectStatus.value == prj[i][3])) {
                    bAddThis = true;
                }
            }
            
            if (bAddThis) {
                myElement = document.createElement("option");
                myElement.value = prj[i][0];
                myElement.text  = prj[i][1];                
                myForm.cboProject.add(myElement);
                if (myElement.value == '<%=beanWeeklyReport.getProject()%>') {
                    myElement.selected =true;
                }
                myForm.hidProjectsList.value += prj[i][0] + ",";
            }
        }
        myForm.hidProjectsList.value += "0";
        
        if (myForm.cboProject.options.length == 1) {
            myForm.cboProject.options[0].selected = true;
        }
    }
    
    changeCombo();
</SCRIPT>
</BODY>
</HTML>