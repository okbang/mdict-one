<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, fpt.timesheet.bean.*,
            fpt.timesheet.bean.Approval.*,
            fpt.timesheet.framework.util.CommonUtil.*,
            fpt.timesheet.bo.Mapping.MappingBO,
            fpt.timesheet.bean.Mapping.*,
            fpt.timesheet.framework.util.StringUtil.StringMatrix,
            fpt.timesheet.framework.util.SqlUtil.SqlUtil,
            java.util.Collection, java.util.Iterator" 
%>
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    QAUpdateBean beanQAUpdate = (QAUpdateBean)request.getAttribute("beanQAUpdate");
    MappingDetailBean beanMappingDetail = (MappingDetailBean)request.getAttribute("beanMappingDetail");
%>
<HTML>
<HEAD>
<TITLE>Update Timesheet by QA</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT src='scripts/Mapping.js'></SCRIPT>
<SCRIPT src='scripts/pr_wp_map.js'></SCRIPT>
<SCRIPT src='scripts/popcalendar.js'></SCRIPT>
</HEAD>
<DIV align="left"><%@ include file="HeaderPage.jsp" %></DIV>
<H1><IMG align="top" src="image/tit_UpdateTimesheetByQA.gif"></H1>

<BODY bgcolor="#336699" leftmargin="0" topmargin="0">
<FORM method="post" action="TimesheetServlet" name="frmQAUpdate" onreset="javascript:resetUpdate();">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="hidTypeOfView" value="<%=beanUserInfo.getTypeOfView()%>">
<INPUT type="hidden" name="maxRow" value="<%=beanQAUpdate.getTimesheetList().getNumberOfRows()%>">

<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName() %></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR noshade size="1">
<%
    // Common variable
    int i;
    int j;
    int maxrows = 0;
    String gItemValue = "";
    String gItemDisplay = "";
%>

<TABLE border="0" cellpadding="1" cellspacing="0" width="100%" bgcolor="#336699">
    <TR>
        <!--TD width="5%" class="TableHeader1"-->
        <!--INPUT type="checkbox" name="allbox" value="CheckAll" onclick="JavaScript:checkAll();" checked--><!--/TD-->
        <TD width="10%" class="TableHeader1">Project</TD>
        <TD width="10%" class="TableHeader1">Account</TD>
        <TD width="8%" class="TableHeader1">Date</TD>
        <TD width="5%" class="TableHeader1">Time</TD>
        <TD width="20%" class="TableHeader1">Description</TD>
        <TD width="17%" class="TableHeader1">Process</TD>
        <TD width="8%" class="TableHeader1">Work</TD>
        <TD width="17%" class="TableHeader1">Product</TD>
        <TD width="5%" class="TableHeader1">KPA</TD>
    </TR><%
    String strStatus = "1";
    int nRows = 0;
    try {
        StringMatrix mtxTimesheet = beanQAUpdate.getTimesheetList();
        StringMatrix mtxProcess = beanQAUpdate.getProcessList();
        int processRows = mtxProcess.getNumberOfRows();
        StringMatrix mtxType = beanQAUpdate.getTypeList();
        int typeRows = mtxType.getNumberOfRows();
        StringMatrix mtxProduct = beanQAUpdate.getProductList();
        int productRows = mtxProduct.getNumberOfRows();
        StringMatrix mtxKpa = beanQAUpdate.getKpaList();
        int kpaRows = mtxKpa.getNumberOfRows();
        i = 0;
        maxrows = mtxTimesheet.getNumberOfRows();
        nRows = maxrows;
        int nProcessID = 0;
        while (i < maxrows) {
            String sId = mtxTimesheet.getCell(i, 0);
            String sProject = mtxTimesheet.getCell(i, 1);
            String sAccount = mtxTimesheet.getCell(i, 2);
            String sDate = mtxTimesheet.getCell(i, 3);
            String sDescription = mtxTimesheet.getCell(i, 4);
            String sDuration = mtxTimesheet.getCell(i, 5);
            String sProcessId = mtxTimesheet.getCell(i, 6);
            String sTypeId = mtxTimesheet.getCell(i, 7);
            String sProductId = mtxTimesheet.getCell(i, 8);
            String sKpaId = mtxTimesheet.getCell(i, 9);
            strStatus = mtxTimesheet.getCell(i, 10);
            nProcessID = Integer.parseInt(sProcessId);
            String strClass = ((i%2)==1)?"Row2":"Row1";
%>
    <TR>
        <!--TD width="5%" align="center" class="<!--%=strClass%>"-->
        <INPUT type="hidden" name="uId" value="<%=sId%>"><!--/TD-->
        <TD width="10%" class="<%=strClass%>">&nbsp;<%=sProject%></TD>
        <TD width="10%" class="<%=strClass%>">&nbsp;<%=sAccount%></TD>
        <TD width="10%" class="<%=strClass%>" align="center">
            <INPUT type="text" name="Date" class="VerySmallTextBox" value="<%=sDate%>"><IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(Date[<%=i%>], Date[<%=i%>], "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <TD width="5%" class="<%=strClass%>">&nbsp;<%=sDuration%></TD>
        <TD width="15%" class="<%=strClass%>"><%=CommonUtil.correctHTMLError(sDescription)%></TD>
        <!-- Must update -->
        <!-- sProcess -->
        <TD width="10%" class="<%=strClass%>"><SELECT size="1" name="Process" class="SmallCombo" onchange="javascript:selectProcess(<%=i%>, this.value, <%=sProductId%>);changeProcess_mapKPA(<%=i%>);"><%
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
        <TD width="10%" class="<%=strClass%>"><SELECT size="1" name="Type" align="center" class="VerySmallCombo"><%
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
        <TD width="10%" align="center" class="<%=strClass%>">
            <SELECT size="1" name="Product" class="SmallCombo">
                <OPTION value="<%=sProductId%>"></OPTION>
            </SELECT></TD>
        <TD width="5%" align="center" class="<%=strClass%>"><SELECT size="1" name="Kpa" class="SmallestCombo"><%
            j = 0;
            while (j < kpaRows) {
                gItemValue = mtxKpa.getCell(j, 0);
                gItemDisplay = mtxKpa.getCell(j, 1);
                if (gItemValue.equals(sKpaId)) {
%>
            <OPTION selected value="<%=gItemValue%>"><%=gItemDisplay%></OPTION><%
                }
                else {
                    if (("0".equals(sKpaId)) && (SqlUtil.mapProcessKPA(nProcessID) == Integer.parseInt(gItemValue))) {
%>
            <OPTION selected value="<%=gItemValue%>"><%=gItemDisplay%></OPTION><%
                    }
                    else {
%>
            <OPTION value="<%=gItemValue%>"><%=gItemDisplay%></OPTION><%
                    }
                }
                j++;
            }
%>
        </SELECT></TD>
        <INPUT type="hidden" name="oldProcess" value="<%=sProcessId%>">
        <INPUT type="hidden" name="oldProduct" value="<%=sProductId%>">
    </TR>
    <TR>
        <TD height="1"></TD>
    </TR><%
            i++;
        }
    }
    catch (Exception ex) {
        ex.toString();
    }
%>
    <INPUT type="hidden" name="uId" value="">
    <INPUT type="hidden" name="Process" value="">
    <INPUT type="hidden" name="Type" value="">
    <INPUT type="hidden" name="Product" value="">
    <INPUT type="hidden" name="Kpa" value="">
    <INPUT type="hidden" name="Date" value="">
    <INPUT type="hidden" name="oldProcess" value="">
    <INPUT type="hidden" name="oldProduct" value="">
<SCRIPT language="javascript">
    setProductList();
</SCRIPT>
</TABLE><%
    String strDisabled = "";
    if ("1".equals(strStatus) || "5".equals(strStatus)) {
        strDisabled = "disabled";
    }
%>
<P align="center"><INPUT type="button" class="Button" name="CorrectAndApprove" onclick='javascript:doUpdateAndApprove()' value="Update and Approve" <%=strDisabled%>>
<INPUT type="button" class="Button" name="Correct" onclick='javascript:doUpdate()' value="Update">
<INPUT type="reset" class="Button" value="Reset" name="Reset">
<INPUT type="button" class="Button" name="Back" onclick='javascript:doBack()' value="Back"></P>
</DIV>
</FORM>

<SCRIPT language="javascript">
function doUpdateAndApprove() {
    var form = document.forms[0];
    if (!formValidate()) {
        return false;
    }
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "SaveUpdateAndApproveQA";
    form.action = "TimesheetServlet";
    form.submit();
}

function doUpdate() {
    var form = document.forms[0];
    if (!formValidate()) {
        return false;
    }
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "SaveUpdateQA";
    form.action = "TimesheetServlet";
    form.submit();
}

function doBack() {
    var form = document.forms[0];
    if (form.hidTypeOfView.value == "InquiryReport") {
        form.hidAction.value = "RA";
        form.hidActionDetail.value = "InquiryReport";
    }
    else {
        form.hidAction.value = "AA";
        form.hidActionDetail.value = "ListQA";
    }
    form.action = "TimesheetServlet";
    form.submit();
}

/*
function checkAll() {
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name != 'allbox' && !e.disabled) {
            e.checked = document.forms[0].allbox.checked;
        }
    }
}
function hasCheck() {
    var nCount = 0;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name != 'allbox' && e.type == "checkbox") {
            if (e.checked == 1) {
                nCount++;
            }
        }
    }
    if (nCount > 0) {
        return true;
    }
    return false;
}
function doCheckBox(i) {
    var myForm = document.forms[0];
    if (myForm.uId[i].checked == true) {
        disableLine(i, false);
    }
    else {
        disableLine(i, true);
    }
}
*/

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
    var nInvalid = -1;

    for (i = 0; i < nMaxRow; i++) {
        var bInvalidRow;
        bInvalidRow = false;

        sDate = document.forms[0].Date[i].value;
        nProcess = document.forms[0].Process[i].value;
        nTypeOfWork = document.forms[0].Type[i].value;
        nProduct = document.forms[0].Product[i].value;

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
        
        if (parseInt(nProcess, 10) > 0 || parseInt(nTypeOfWork, 10) > 0 ||
                parseInt(nProduct, 10) > 0 ) {
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
        
        if (objProcess.value <= 0) {
            objProcess.focus();
        }
        else if (objTypeOfWork.value <= 0) {
            objTypeOfWork.focus();
        }
        else if (objProduct.value <= 0) {
            objProduct.focus();
        }
        else {
            objDate.focus();
        }
    }
}

function mapProcessKPA(nProcessID) {
    var nResult = 0;
    switch (nProcessID) {
        case "<%=SqlUtil.PROCESS_CONTRACT_MAN%>":
        case "<%=SqlUtil.PROCESS_REQUIREMENT%>":
            return "<%=SqlUtil.KPA_RM%>";
        
        case "<%=SqlUtil.PROCESS_PROJECT_MAN%>":
            return "<%=SqlUtil.KPA_PT%>";

        case "<%=SqlUtil.PROCESS_QUALITY_CONTROL%>":
        case "<%=SqlUtil.PROCESS_INTERNAL_AUDIT%>":
        case "<%=SqlUtil.PROCESS_CORRECTION%>":
            return "<%=SqlUtil.KPA_QA%>";

        case "<%=SqlUtil.PROCESS_CONFIGURATION_MAN%>":
            return "<%=SqlUtil.KPA_CM%>";

        case "<%=SqlUtil.PROCESS_DESIGN%>":
        case "<%=SqlUtil.PROCESS_CODING%>":
        case "<%=SqlUtil.PROCESS_DEPLOYMENT%>":
        case "<%=SqlUtil.PROCESS_CUSTOMER_SUPPORT%>":
        case "<%=SqlUtil.PROCESS_TEST%>":
        case "<%=SqlUtil.PROCESS_TRAINING%>":
            return "<%=SqlUtil.KPA_PE%>";

        case "<%=SqlUtil.PROCESS_PREVENTION%>":
            return "<%=SqlUtil.KPA_DP%>";

        default:    // Others
            return "<%=SqlUtil.KPA_PT%>";
    }
}

function changeProcess_mapKPA(nRow) {
    var myForm = document.forms[0];
    var nMappedKPA = 0;
    nMappedKPA = mapProcessKPA(myForm.Process[nRow].value);
    for (var i = 0; i < myForm.Kpa[nRow].options.length; i++) {
        if (myForm.Kpa[nRow].options[i].value == nMappedKPA) {
            myForm.Kpa[nRow].selectedIndex = i;
            break;
        }
    }
}
</SCRIPT>
</BODY>
</HTML>