<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, fpt.timesheet.bean.*,
            fpt.timesheet.bean.Approval.*,
            fpt.timesheet.framework.util.CommonUtil.*,
            fpt.timesheet.bo.Mapping.MappingBO,
            fpt.timesheet.bean.Mapping.*,
            fpt.timesheet.framework.util.StringUtil.StringMatrix,
            java.util.Collection, java.util.Iterator" 
%>
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    PLUpdateBean beanPLUpdate = (PLUpdateBean)request.getAttribute("beanPLUpdate");
    MappingDetailBean beanMappingDetail = (MappingDetailBean)request.getAttribute("beanMappingDetail");
%>
<HTML>
<HEAD>
<TITLE>Update and Approve Billable Timesheet</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT src='scripts/Mapping.js'></SCRIPT>
<SCRIPT src='scripts/pr_wp_map.js'></SCRIPT>
<SCRIPT src='scripts/popcalendar.js'></SCRIPT>
</HEAD>
<BODY bgcolor="#336699">
<DIV align="left"><%@ include file="HeaderPage.jsp" %></DIV>
<H1><IMG align="top" src="image/tit_UpdateTimesheetByPL.gif"></H1>
<FORM method="post" action="TimesheetServlet" name="frmPLUpdate" onreset="javascript:resetUpdate();">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="maxRow" value="<%=beanPLUpdate.getTimesheetList().getNumberOfRows()%>">
<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName() %></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR noshade size="1">
<%
    // Common variable
    int i;
    int j;
    int maxrows;
    String gItemValue = "";
    String gItemDisplay = "";
%>
<TABLE border="0" cellpadding="1" cellspacing="0" width="100%" bgcolor="#336699">
    <TR>
        <TD width="10%" class="TableHeader1">Project</TD>
        <TD width="10%" class="TableHeader1">Account</TD>
        <TD width="16%" class="TableHeader1">Process</TD>
        <TD width="8%" class="TableHeader1">Work</TD>
        <TD width="17%" class="TableHeader1">Product</TD>
        <TD width="9%" class="TableHeader1">Date</TD>
        <TD width="4%" class="TableHeader1">Time</TD>
        <TD width="25%" class="TableHeader1">Description</TD>
    </TR><%
    int nRows=0;
    try {
        StringMatrix mtxTimesheet = beanPLUpdate.getTimesheetList();
        StringMatrix mtxProcess = beanPLUpdate.getProcessList();
        int processRows = mtxProcess.getNumberOfRows();
        StringMatrix mtxType = beanPLUpdate.getTypeList();
        int typeRows = mtxType.getNumberOfRows();
        StringMatrix mtxProduct = beanPLUpdate.getProductList();
        int productRows = mtxProduct.getNumberOfRows();
        i = 0;
        maxrows = mtxTimesheet.getNumberOfRows();
        nRows = maxrows;
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
            String strClass = ((i % 2) == 1) ? "Row2" : "Row1";
%>
    <INPUT type="hidden" name="uId" value="<%=sId%>">
    <TR>
        <TD width="10%" class="<%=strClass%>"><%=sProject%></TD>
        <TD width="10%" class="<%=strClass%>"><%=sAccount%></TD>
        <!-- Must update -->
        <!-- sProcess -->
        <TD width="17%" class="<%=strClass%>"><SELECT size="1" name="Process" class="SmallCombo" onchange="javascript:selectProcess(<%=i%>, this.value, <%=sProductId%>);"><%
            j = 0;
            while (j < processRows) {
                gItemValue = mtxProcess.getCell(j, 0);
                gItemDisplay = mtxProcess.getCell(j, 1);
                if (!(gItemValue.equalsIgnoreCase(sProcessId))) {
%>
            <OPTION value="<%=gItemValue%>"><%=gItemDisplay%></OPTION><%
                }
                else {
%>
            <OPTION selected value="<%=gItemValue%>"><%=gItemDisplay%></OPTION><%
                }
                j++;
            }
%>
        </SELECT></TD>
        <TD width="8%" class="<%=strClass%>"><SELECT size="1" name="Type" align="center" class="VerySmallCombo"><%
            j = 0;
            while (j < typeRows) {
                gItemValue = mtxType.getCell(j, 0);
                gItemDisplay = mtxType.getCell(j, 1);
                if (!(gItemValue.equalsIgnoreCase(sTypeId))) {
%>
            <OPTION value="<%=gItemValue%>"><%=gItemDisplay%></OPTION><%
                }
                else {
%>
            <OPTION selected value="<%=gItemValue%>"><%=gItemDisplay%></OPTION><%
                }
                j++;
            }
%>
        </SELECT></TD>
        <TD width="16%" align="center" class="<%=strClass%>">
        <SELECT size="1" name="Product" style="width: 150" class="SmallCombo">
            <OPTION value="<%=sProductId%>"></OPTION>
        </SELECT></TD>
        <TD width="9%" class="<%=strClass%>" align="center">
            <INPUT type="text" name="Date" class="VerySmallTextBox" value="<%=sDate%>"><IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(Date[<%=i%>], Date[<%=i%>], "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <TD width="4%" align="center" class="<%=strClass%>">
            <INPUT type="text" name="Duration" size="5" maxlength="4" class="SmallestTextbox" value="<%=sDuration%>"></TD>
        <TD width="25%" align="center" class="<%=strClass%>">
            <INPUT type="text" name="Description" class="VeryBigTextBox" value="<%=CommonUtil.correctHTMLError(sDescription)%>" size="30"></TD>
            <INPUT type="hidden" name="oldProcess" value="<%=sProcessId%>">
            <INPUT type="hidden" name="oldProduct" value="<%=sProductId%>">
    </TR>
    <TR>
        <TD height="1"></TD>
    </TR><%
            i++;
        }
    }
    catch(Exception ex) {
        ex.toString();
    }
%>
    <INPUT type="hidden" name="uId" value="">
    <INPUT type="hidden" name="Process" value="">
    <INPUT type="hidden" name="Type" value="">
    <INPUT type="hidden" name="Product" value="">
    <INPUT type="hidden" name="Duration" value="">
    <INPUT type="hidden" name="Description" value="">
    <INPUT type="hidden" name="Date" value="">
    <INPUT type="hidden" name="oldProcess" value="">
    <INPUT type="hidden" name="oldProduct" value="">
<SCRIPT language="javascript">
    setProductList();
</SCRIPT>
</TABLE>
<P align="center"><INPUT type="button" class="Button" name="CorrectAndApprove" onclick='javascript:doUpdateAndApprove()' value="Update and Approve">
<INPUT type="button" class="Button" name="Back" onclick='javascript:doBack()' value="Back">
<INPUT type="reset" class="Button" value="Reset" name="Reset"></P>
</DIV>
</FORM>

<SCRIPT language="javascript">
function doUpdateAndApprove() {
    var form = document.forms[0];
    if (!formValidate()) {
        return false;
    }
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "SaveUpdateAndApprovePL";
    form.action = "TimesheetServlet";
    form.submit();
}

function doBack() {
    var form = document.forms[0];
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "ListPL";
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
        nProcess = document.forms[0].Process[i].value;
        nTypeOfWork = document.forms[0].Type[i].value;
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
        // Process checking up ...
        if (parseInt(nProcess, 10) == 0) {
            bInvalidRow = true;
        }
        // TypeOfWork checking up ...
        if (parseInt(nTypeOfWork, 10) == 0) {
            bInvalidRow = true;
        }
        // Product checking up ...
        if (parseInt(nProduct, 10) == 0) {
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
        if (sDescription.length<=0){
            bInvalidRow = true;
        }
        
        if (parseInt(nProcess, 10) > 0 || parseInt(nTypeOfWork, 10) > 0 ||
                parseInt(nProduct, 10) > 0 ||
                sDuration.length > 0 || sDescription.length > 0) {
            nTotalRows++;
            if (bInvalidRow) {
                nInvalidRows++;
                //Capture invalid row
                if (nInvalid < 0) {
                    nInvalid = i;
                }
            }
        }
	    if (document.forms[0].Date[i].value.length > 0 ) {
    	    if (isValidate(document.forms[0].Date[i].value)==false) {
    			document.forms[0].Date[i].focus();
    			return false;
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

function focusInvalidRow(nRow) {
    if (nRow >= 0) {
        objDate = document.forms[0].Date[nRow];
        objProcess = document.forms[0].Process[nRow];
        objTypeOfWork = document.forms[0].Type[nRow];
        objProduct = document.forms[0].Product[nRow];
        objDuration = document.forms[0].Duration[nRow];
        objDescription = document.forms[0].Description[nRow];
        
        if (objProcess.value <= 0) {
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