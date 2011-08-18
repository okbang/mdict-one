<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, fpt.timesheet.bean.*,
            fpt.timesheet.bean.Approval.*,
            fpt.timesheet.InputTran.ejb.TimeSheetInfo,
            fpt.timesheet.bo.ComboBox.ProjectComboBO,
            fpt.timesheet.bo.ComboBox.CommonListBO,
            fpt.timesheet.bo.Mapping.MappingBO,
            fpt.timesheet.bean.Mapping.*,
            fpt.timesheet.framework.util.StringUtil.StringMatrix,
            fpt.timesheet.framework.util.CommonUtil.*,
            java.util.Collection, java.util.Iterator" 
%>
<%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    TSUpdateBean beanTSUpdate = (TSUpdateBean)request.getAttribute("beanTSUpdate");
    MappingDetailBean beanMappingDetail = (MappingDetailBean)request.getAttribute("beanMappingDetail");
%>
<HEAD>
<TITLE>Update Timesheet</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT src='scripts/Mapping.js'></SCRIPT>
<SCRIPT src='scripts/pr_wp_map.js'></SCRIPT>
<SCRIPT src='scripts/popcalendar.js'></SCRIPT>
</HEAD>
<BODY bgcolor="#336699" leftmargin="0" topmargin="0">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H1><IMG align="top" src="image/tit_UpdateTimesheet.gif"></H1>
<FORM method="post" action="TimesheetServlet" name="frmTimesheetAdd" onreset="javascript:resetUpdate();">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="maxRow" value="<%=beanTSUpdate.getTimesheetList().size()%>">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName()%></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR noshade size="1">
<TABLE border="0" cellpadding="1" cellspacing="0" width="100%" bgcolor="#336699">
    <COLGROUP>
        <COL width="9%">
        <COL width="5%">
        <COL width="25%">
        <COL width="10%">
        <COL width="15%">
        <COL width="10%">
        <COL width="15%">
    <TR>
        <TD class="TableHeader1">Date</TD>
        <TD class="TableHeader1">Time</TD>
        <TD class="TableHeader1">Description</TD>
        <TD class="TableHeader1">Project</TD>
        <TD class="TableHeader1">Process</TD>
        <TD class="TableHeader1">Work</TD>
        <TD class="TableHeader1">Product</TD>
    </TR>
<%
    ProjectComboBO comboProject = new ProjectComboBO();
    CommonListBO commonList = new CommonListBO();
    StringMatrix smtCommonList = null;
    StringMatrix smtProjectList = comboProject.getProjectComboList(
            beanUserInfo.getRole(), beanUserInfo.getUserId(), 0x00,
            fpt.timesheet.constant.Timesheet.PROJECT_STATUS_RUNNING);
    smtProjectList.setCell(0, 0, "0");
    Iterator itResult = beanTSUpdate.getTimesheetList().iterator();
    if (itResult != null) {
        TimeSheetInfo tsRow;
        int nCount = 0;
        int nCurrentValue = 0x00;
        while (itResult.hasNext()) {
            tsRow = (TimeSheetInfo)itResult.next();
            nCount ++;
            String strClass = ((nCount % 2) == 1) ? "Row1" : "Row2";
%>
    <TR>
        <TD align="center" class="<%=strClass%>">
            <INPUT type="hidden" name="TimesheetID" value="<%=tsRow.getTimeSheetID()%>">
            <INPUT type="text" name="Date" class="VerySmallTextBox" value="<%=tsRow.getDate()%>" readonly="readonly"><IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(Date[<%=nCount - 1%>], Date[<%=nCount - 1%>], "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <TD class="<%=strClass%>" align="center">
            <INPUT name="Duration" type="text" value="<%=tsRow.getDuration()%>" size="5" maxlength="4" class="SmallestTextbox"></TD>
        <TD class="<%=strClass%>" align="center">
            <INPUT name="Description" type="text" value="<%=CommonUtil.correctHTMLError(tsRow.getDescription())%>" class="VeryBigTextbox" rows="1" cols="20" maxlength="99"></TD>
        <!-- Project -->
        <TD class="<%=strClass%>"><%
            nCurrentValue = tsRow.getProject();
%>
        <CENTER><SELECT name="Project" class="SmallCombo"><%
            for (int nRow = 0x01; nRow < smtProjectList.getNumberOfRows(); nRow++) {
                int nValue = Integer.parseInt(smtProjectList.getCell(nRow, 0));
                String strText = smtProjectList.getCell(nRow, 1);
                out.write("<OPTION value='" + nValue + "' " + ((nValue == nCurrentValue) ? "selected" : "") + ">" + strText + "</OPTION>");
            }
%>
        </SELECT></CENTER>
        </TD>
        <!-- Process --><%
            smtCommonList = commonList.getProcessList();
            nCurrentValue = tsRow.getProcess();
%>
        <TD class="<%=strClass%>">
        <CENTER><SELECT name="Process" class="SmallCombo" onchange="javascript:selectProcess(<%=nCount-1%>, this.value, <%=tsRow.getWorkProduct()%>);"><%
            for (int nRow = 0x00; nRow < smtCommonList.getNumberOfRows(); nRow++) {
                int nValue = Integer.parseInt(smtCommonList.getCell(nRow, 0));
                String strText = smtCommonList.getCell(nRow, 1);
                out.write("<OPTION value='" + nValue + "' " + ((nValue == nCurrentValue) ? "selected" : "") + ">" + strText + "</OPTION>");
            }
%>
        </SELECT></CENTER>
        </TD>
        <!-- TypeOfWork --><%
            smtCommonList = commonList.getTypeOfWorkList();
            nCurrentValue = tsRow.getTypeofWork();
%>
        <TD class="<%=strClass%>">
        <CENTER><SELECT name="TypeOfWork" class="VerySmallCombo"><%
            for (int nRow = 0x00; nRow < smtCommonList.getNumberOfRows(); nRow++) {
                int nValue = Integer.parseInt(smtCommonList.getCell(nRow, 0));
                String strText = smtCommonList.getCell(nRow, 1);
                out.write("<OPTION value='" + nValue + "' " + ((nValue == nCurrentValue) ? "selected" : "") + ">" + strText + "</OPTION>");
            }
%>
        </SELECT></CENTER>
        </TD>
        <!-- Product -->
        <TD class="<%=strClass%>">
        <CENTER><SELECT name="Product" class="SmallCombo">
        	<OPTION value = <%=tsRow.getWorkProduct()%>></OPTION>
        </SELECT></CENTER>
        </TD>
	    <INPUT type="hidden" name="oldProcess" value="<%=tsRow.getProcess()%>">
    	<INPUT type="hidden" name="oldProduct" value="<%=tsRow.getWorkProduct()%>">
    </TR>
    <TR>
        <TD height="1"></TD>
    </TR>
<%
        } //~while (itResult.hasNext())
    } // end if
%>
    <INPUT type="hidden" name="TimesheetID" value="">
    <INPUT type="hidden" name="Date" value="">
    <INPUT type="hidden" name="Project" value="">
    <INPUT type="hidden" name="Process" value="">
    <INPUT type="hidden" name="TypeOfWork" value="">
    <INPUT type="hidden" name="Product" value="">
    <INPUT type="hidden" name="Duration" value="">
    <INPUT type="hidden" name="Description" value="">
    <INPUT type="hidden" name="oldProcess" value="">
	<INPUT type="hidden" name="oldProduct" value="">
<SCRIPT>
	setProductList();
</SCRIPT>
</TABLE>
<P align="center"><INPUT type="button" name="Update" value="Update" class="Button" onclick='javascript:doSave()'>
<INPUT type="reset" value="Reset" name="cmdReset" class="Button">
<INPUT type="button" value="Back" name="cmdCancel" class="Button" onclick='javascript:doBack()'></P>
</DIV>
</FORM>
<SCRIPT language="javascript">
function doSave() {
    var form = document.forms[0];
    if (!formValidate()) {
        return false;
    }
    
    // Modified by HaiMM
    // if (!LogDateValidate()) {
    //    return false;
    // }
    
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "SaveUpdateTimesheet";
    form.action = "TimesheetServlet";
    form.submit();
}

function doBack() {
    var form = document.forms[0];
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "ListTimesheet";
    form.action = "TimesheetServlet";
    form.submit();
}

function formValidate() {
    var bIsCorrect = true;
    var nInvalidRows = 0;
    var nTotalRows = 0;
    var nMaxRow;
    nMaxRow = document.forms[0].maxRow.value;

    var sDate;
    var nProject;
    var nProcess;
    var nTypeOfWork;
    var nProduct;
    var sDuration;
    var sDescription;
    var nInvalid = -1;

    for (i = 0; i < nMaxRow; i++) {
        var bInvalidRow;
        bInvalidRow = false;

        sDate = document.forms[0].Date[i].value;
        nProject = document.forms[0].Project[i].value;
        nProcess = document.forms[0].Process[i].value;
        nTypeOfWork = document.forms[0].TypeOfWork[i].value;
        nProduct = document.forms[0].Product[i].value;
        sDuration = document.forms[0].Duration[i].value;
        document.forms[0].Description[i].value = trim(document.forms[0].Description[i].value);
        sDescription = document.forms[0].Description[i].value;

        // Date checking up ...
        if (sDate.length <= 0){
            bInvalidRow = true;
        }
        else{
            if (!dateValidate(document.forms[0].Date[i])) {
                bInvalidRow = true;
                bIsCorrect = false;
            }
        }
        // Project checking up ...
        if (parseInt(nProject, 10) <= 0) {
            bInvalidRow = true;
        }
        // Process checking up ...
        if (parseInt(nProcess, 10) <= 0) {
            bInvalidRow = true;
        }
        // TypeOfWork checking up ...
        if (parseInt(nTypeOfWork, 10) <= 0) {
            bInvalidRow = true;
        }
        // Product checking up ...
        if (parseInt(nProduct, 10) <= 0) {
            bInvalidRow = true;
        }
        // Duration checking up ...
        if (sDuration.length <= 0) {
            bInvalidRow = true;
        }
        else {
            if (!durationValidate(document.forms[0].Duration[i])) {
                bInvalidRow = true;
                bIsCorrect = false;
            }
        }
        // Description checking up ...
        if (sDescription.length <= 0) {
            bInvalidRow = true;
        }
        
        if (parseInt(nProject, 10) > 0 || parseInt(nProcess, 10) > 0
                || parseInt(nTypeOfWork, 10) > 0 || parseInt(nProduct, 10) > 0
                || sDuration.length > 0 || sDescription.length > 0) {
            nTotalRows++;
            if (bInvalidRow) {
                nInvalidRows++;
                //Capture invalid row
                if (nInvalid < 0) {
                	nInvalid = i;
                }
            }
        }
    }
    if (!bIsCorrect) {
        return false;
    }
    if (nTotalRows == 0) {
        alert("Please input some information before submiting");
        focusInvalidRow(0);
        return false;
    }
    if (nInvalidRows > 0 && nInvalidRows < nTotalRows) {
        alert("You have " + nInvalidRows + " invalid row(s) in total " +
        		nTotalRows + " row(s) needed to update." +
        		"\nPlease input correct informations before update");
    	focusInvalidRow(nInvalid);
        return false;
    }
    if ((nInvalidRows == nTotalRows) && (nTotalRows > 0)) {
        alert("All of your information is not correct");
        focusInvalidRow(nInvalid);
        return false;
    }
    return true;
}

function LogDateValidate() {
    var nInvalidRows = 0;
    var nTotalRows = 0;
    var nMaxRow;
    nMaxRow = document.forms[0].maxRow.value;

    var sDate;
    var nProject;
    var nInvalid = 0;
    
<%
   out.write("var P_COUNT=" + (smtProjectList.getNumberOfRows() - 1) + ";");
   out.write("\nvar prj = new Array(P_COUNT);");
   for (int nRow = 1; nRow < smtProjectList.getNumberOfRows(); nRow++) {
       // Project_ID, StartDate, PlanStartdate
       out.write("\nprj[" + (nRow - 1) + "]=new Array(3);");
       out.write("prj[" + (nRow - 1) + "][0]=" + smtProjectList.getCell(nRow, 0) + ";");
       out.write("prj[" + (nRow - 1) + "][1]='" + smtProjectList.getCell(nRow, 5) + "';");
       out.write("prj[" + (nRow - 1) + "][2]='" + smtProjectList.getCell(nRow, 6) + "';");
   }
%>

    for (i = 0; i < nMaxRow; i++) {
        var bInvalidRow;
        bInvalidRow = false;

		var bInvalidStartDate;
		bInvalidStartDate = false;

		var sStartdate;
		var sPlanStartdate;
		var sDateTemp;

        sDate = document.forms[0].Date[i].value;
        nProject = document.forms[0].Project[i].value;

	    for (j = 0; j < P_COUNT; j++) {
          if (nProject == prj[j][0]) {
             sStartdate = prj[j][1];
             sPlanStartdate = prj[j][2];
             break;
          }
          else {
			 sStartdate = '';
			 sPlanStartdate = '';
          }
        }
		
		if ((sStartdate == '') && (sPlanStartdate == '')) {
			sDateTemp = '';
		}
		else if ((sStartdate.length > 0) && (sPlanStartdate.length > 0)) {
			if (CompareValue(sStartdate, sPlanStartdate) < 0) {
				sDateTemp = sStartdate;
			}
			else {
				sDateTemp = sPlanStartdate;
			}
		}
		else if (sStartdate.length > sPlanStartdate.length ) {
			sDateTemp = sStartdate;
		}
		else {
			sDateTemp = sPlanStartdate;
		}
		
		if ((sDateTemp == '') || (CompareValue(sDateTemp, sDate) < 0)) {
			continue;
	    }
	    else {
	        alert("The log_date must be >= the start_date or plan_start_date of project");
			document.forms[0].Project[i].focus();
	        return false;
		}
    } // end For
    return true;
}

function focusInvalidRow(nRow) {
	if (nRow >= 0) {
	    objDate = document.forms[0].Date[nRow];
	    objProject = document.forms[0].Project[nRow];
	    objProcess = document.forms[0].Process[nRow];
	    objTypeOfWork = document.forms[0].TypeOfWork[nRow];
	    objProduct = document.forms[0].Product[nRow];
	    objDuration = document.forms[0].Duration[nRow];
	    objDescription = document.forms[0].Description[nRow];
		
		if (objProject.value <= 0) {
			objProject.focus();
		}
		else if (objProcess.value <= 0) {
			objProcess.focus();
		}
		else if (objTypeOfWork.value <= 0) {
			objTypeOfWork.focus();
		}
		else if (objProduct.value <= 0) {
			objProduct.focus();
		}
		else if (objDescription.value.length <= 0) {
			objDescription.focus();
		}
		else if (!isDate(objDate.value)) {
			objDate.focus();
		}
		else {
			objDuration.focus();
		}
	}
}
</SCRIPT>
</BODY>
</HTML>