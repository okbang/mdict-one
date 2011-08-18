<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java"
    import="java.util.*,
    		javax.servlet.*,
    		fpt.dms.bean.*,
            fpt.dms.bean.DefectManagement.*,
            fpt.dms.framework.util.DateUtil.*,
            fpt.dms.bo.combobox.*,
            fpt.dms.framework.util.StringUtil.*,
            fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    DefectUpdateBean beanDefectUpdate
            = (DefectUpdateBean)request.getAttribute("beanDefectUpdate");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
    DefectAttachBean beanDefectAttach =
    		(DefectAttachBean) session.getAttribute("beanDefectAttach");
	String strAttachLink = "";
	String strAttachIcon = "";
	if (beanDefectAttach != null) {
		if (beanDefectAttach.getAttachMode() == DefectAttachBean.MODE_UPDATE) {
			strAttachLink = beanDefectAttach.writeHtmlLink();
		}
		else {
			beanDefectAttach.discardTempAttach();
			session.removeAttribute("beanDefectAttach");
		}
		if ((strAttachLink != null) && (! "".equals(strAttachLink))) {
			strAttachIcon = "<IMG src='Images/attach.gif'>&nbsp;";
		}
	}
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" href="styles/DMSStyleSheet.css" type="text/css">
<LINK rel="stylesheet" href="styles/pcal.css" type="text/css">
<TITLE>Update Defect</TITLE>
<SCRIPT language="javascript">
function doSave() {
    var form = document.frmDefectUpdate;
    if (!isValidForm()) {
        return;
    }
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SaveUpdateDefect";
    form.action = "DMSServlet";
    form.submit();
}
<%
    // Simply remove the Delete function when this user is not defect's owner
    if (beanDefectUpdate.getCreateUser().equals(beanUserInfo.getAccount())) {
%>
function doDelete() {
    if (confirm("Do you want to delete this defect?")) {
        var form = document.frmDefectUpdate;
        form.hidAction.value = "DM";
        form.hidActionDetail.value = "DeleteDefect";
        form.action = "DMSServlet";
        form.submit();
    }
}
<%
    }
%>
function doBack() {
    var form = document.frmDefectUpdate;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doQueryListing() {
    var form = document.frmDefectUpdate;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function doViewDefect(type) {
    var form = document.frmDefectUpdate;
    form.hidDirect.value = type;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "UpdateDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doViewHistory() {
    var form = document.frmDefectUpdate;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "DefectHistory";
    form.action = "DMSServlet";
    form.submit();
}

function doAttach() {
    var form = document.frmDefectUpdate;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "<%=DMS.DEFECT_ATTACH_UPDATE_FORM%>";
    form.action = "DMSServlet";
    form.submit();
}

function showTempFile(nIndex) {
	var form = document.forms[0];
	showAttachFile(nIndex, form, "<%=DMS.DEFECT_ATTACH_FILE_VIEW%>");
}

function removeTempFile(nIndex) {
    if (confirm("Do you want to remove this temporary attach file?")) {
	    var form = document.forms[0];
		removeAttachFile(nIndex, form, "<%=DMS.DEFECT_ATTACH_FILE_REMOVE_UPDATE%>");
	}
}

function showDbFile(nIndex) {
	var form = document.forms[0];
	showAttachData(nIndex, form, "<%=DMS.DEFECT_ATTACH_DB_VIEW%>");
}

function removeDbFile(nIndex) {
    if (confirm("Do you want to remove this permanent attach file?\n" +
    			"Note: Attach file will be removed even not save this defect")) {
	    var form = document.forms[0];
		removeAttachData(nIndex, form, "<%=DMS.DEFECT_ATTACH_DB_REMOVE_UPDATE%>");
	}
}

/*
	* Modified by HUYNH2
	* If Status is Pending/Tested/Accepted/Cancelled --> Corrective Action is mandatory
	*
*/
function isValidForm() {
    var count;
    var form = document.frmDefectUpdate;
   
    for (count = 0x00; count < form.elements.length; count++) {
        if (!isValidControl(form.elements[count])) {
            return false;
        }
    }
 
    if ((form.txtDueDate.value.length > 0)) {
        if (CompareDate(form.txtDueDate , form.txtCreateDate) < 0) {
            alert("Due Date must greater than or equals to Create Date");
            form.txtDueDate.focus();
            return false;
        }
    }
    if ((form.txtFixedDate.value.length > 0)) {
        if (CompareDate(form.txtFixedDate , form.txtCreateDate) < 0) {
            alert("Fix Date must greater than or equals to Create Date");
            form.txtFixedDate.focus();
            return false;
        }
    }
    // Assign to is mandatory when status is assigned,pending or closed    
    if ((form.cboStatus.value == 2) || (form.cboStatus.value == 3) ||
         (form.cboStatus.value == 4))
    {
        if (form.cboAssignTo.value.length == 0) {
            alert("Please assign to a developer");
            form.cboAssignTo.focus();
            return false;
        }
    }
    //If Status is Pending/Tested/Accepted/Cancelled --> Corrective Action is mandatory
    /*
	2-Assigned
	3-Pending
	4-Tested
	5-Accepted
	6-Cancelled
	*/
    if ((form.cboStatus.value == 3) || (form.cboStatus.value == 4) || (form.cboStatus.value == 5)||(form.cboStatus.value == 6))
    {
        if (form.txtCorrectiveAction.value.replace(/^\s*|\s*$/g,'').length == 0) {
            alert("Please fill to Corrective Action");
            form.txtCorrectiveAction.focus();
            return false;
        }
    }
    return true;
}

//modified by MinhPT
//09-Dec-03
//for fix TypeOfAcitvity when select QCActivity
function ChangeTypeActivity() {
    var form = document.forms[0];
    var QCActivity = form.cboQCActivity.value;
    var check = QCActivity.substring(0,1);

    while (form.cboTypeofActivity.options.length > 0) {
        form.cboTypeofActivity.options[0] = null;
    }
    switch (check) {
        case "1": //Test
            appendOption(form.cboTypeofActivity, 1, '<%=beanDefectUpdate.getComboTypeofActivity().getCell(3, 1)%>', true);
            break;
        case "2": //Review
            appendOption(form.cboTypeofActivity, 2, '<%=beanDefectUpdate.getComboTypeofActivity().getCell(2, 1)%>', true);
            break;
        case "3": //Inspection
            appendOption(form.cboTypeofActivity, 3, '<%=beanDefectUpdate.getComboTypeofActivity().getCell(1, 1)%>', true);
            break;
        case "4": //Audit
            appendOption(form.cboTypeofActivity, 4, '<%=beanDefectUpdate.getComboTypeofActivity().getCell(0, 1)%>', true);
            break;
    }
}

function changeAssignTo() {
    var form = document.forms[0];
    // Status=1: 1-Error
    // Status=2: 2-Assigned
    // Status=6: 6-Cancelled
    if ((form.cboAssignTo.value.length == 0) && (form.cboStatus.value != 6)) {
        form.cboStatus.value = 1;
        form.txtFixedDate.value = "";
    }
    else if ((form.cboAssignTo.value.length > 0) && (form.cboStatus.value == 1)) {
        form.cboStatus.value = 2;
    }
}

function isValidControl(control) {
    if (control.name == "txtCreateDate") {
        if (isDate(control) && isLessThanCurDate(control)) {
            return true;
        }
        else {
            return false;
        }
    }
    else if (control.name == "txtDueDate") {
        if (control.value.length <= 0 || isDate(control)) {
            return true;
        }
        else {
            return false;
        }
    }
    else if (control.name == "txtFixedDate") {
        if (control.value.length > 0) {
            if (isDate(control) && isLessThanCurDate(control)) {
                return true;
            }
            else return false;
        }
    }
    else if (control.name == "cboStatus") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "txtTitle") {
        return textValidate(control, 1, 150);
    }
    else if (control.name == "cboWorkProduct") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "cboSeverity") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "cboTypeofActivity") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "cboQCActivity") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "cboType") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "cboDefectOrigin") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "txtDescription") {
        control.name = "hidDescription";
        if (textValidate(control, 1, 1200)) {
            control.name = "txtDescription";
            return true;
        }
        else {
            control.name = "txtDescription";
            return false;
        }
    }
    else if (control.name == "txtCorrectiveAction") {
        control.name = "Corrective Actions";
        if (textValidate(control, 0, 1200)) {
            control.name = "txtCorrectiveAction";
            return true;
        }
        else {
            control.name = "txtCorrectiveAction";
            return false;
        }
    }
    else if (control.name == "txtCauseAnalysis") {
	    control.name = "Cause Analysis";
	    if (textValidate(control, 0, 1200)) {
	        control.name = "txtCauseAnalysis";
	        return true;
	    }
	    else {
	        control.name = "txtCauseAnalysis";
	        return false;
	    }
    }
    return true;
}

function CheckRoleStatus(position) {
    var newValue = document.forms[0].cboStatus.value;
    var oldValue = document.forms[0].hidCurrentStatus.value;
    //Restore FixedDate
    document.forms[0].txtFixedDate.value = document.forms[0].hidFixedDate.value;
    var permit = true;
    switch (newValue) {//follow value of status
        case "1": //Error
            //Error => Could not assign FixedDate
            document.forms[0].txtFixedDate.value = "";
            document.forms[0].cboAssignTo.value = "";
            break;
        case "6": //Cancelled
            //Cancelled => Clear FixedDate
            document.forms[0].txtFixedDate.value = "";
            break;
        case "4": //Tested
            if (position.substring(1, 2) != 1 && position.substring(2, 3) != 1 && position.substring(6, 7) != 1) {//tester
                permit = false;
            }
            break;
        case "5": //Accepted
            if (position.substring(2, 3) != 1 && position.substring(6, 7) != 1) {//project leader OR external user
                permit = false;
            }
            break;
    }
    if (!permit) {
        alert("You have no permission to change this status!");
        document.forms[0].cboStatus.value = oldValue;
    }
    else {
        GenFixedDate();
    }
}

function GenFixedDate() {
    // Status=3: Pending, 4: Tested
    if ((document.forms[0].txtFixedDate.value == "") &&
        (document.forms[0].cboStatus.value != "<%=beanDefectUpdate.getStatus()%>") &&
        (document.forms[0].cboStatus.value == "3" || document.forms[0].cboStatus.value == "4"))
    {
        document.forms[0].txtFixedDate.value = "<%=DateUtil.getCurrentDate()%>";
    }
    else if ((document.forms[0].txtFixedDate.value == "") &&
             (<%=(beanDefectUpdate.getFixedDate() != null) &&
                 (!"".equals(beanDefectUpdate.getFixedDate()))%>) &&
             (document.forms[0].cboStatus.value == "3" ||
              document.forms[0].cboStatus.value == "4"))
    {
        document.forms[0].txtFixedDate.value = "<%=beanDefectUpdate.getFixedDate()%>";
    }
}

//added by MinhPT
//06-Dec-03
<%
    int nNumberOfModule = beanDefectUpdate.getComboModuleCode().getNumberOfRows();
    out.write("var nNumberOfModule = " + nNumberOfModule + ";\n");
    out.write("var arrModuleID = new Array(" + nNumberOfModule + ");\n");
    out.write("var arrModuleName = new Array(" + nNumberOfModule + ");\n");
    out.write("var arrModuleWPID = new Array(" + nNumberOfModule + ");\n");
    for (int i = 0; i < nNumberOfModule; i++) {
        String strValue = beanDefectUpdate.getComboModuleCode().getCell(i, 0);
        out.write("arrModuleID[" + i + "] = \"" + strValue + "\";\n");

        strValue = beanDefectUpdate.getComboModuleCode().getCell(i, 1);
        out.write("arrModuleName[" + i + "] = \"" + strValue + "\";\n");

        strValue = beanDefectUpdate.getComboModuleCode().getCell(i, 4);
        out.write("arrModuleWPID[" + i + "] = \"" + strValue + "\";\n");
    }
%>
//added by MinhPT
//06-Dec-03
function selectWorkProduct(nCurrentModuleCode) {
    var form = document.forms[0];
    var i;
    while (form.cboModuleCode.options.length > 0) {
        form.cboModuleCode.options[0] = null;
    }
    appendOption(form.cboModuleCode, "0", "", false);
    
    currentWPID = form.cboWorkProduct.value;
    for (i = 0; i < nNumberOfModule; i++) {
        if(arrModuleWPID[i] == currentWPID) {
            if (arrModuleID[i] == nCurrentModuleCode) {
                appendOption(form.cboModuleCode, arrModuleID[i], arrModuleName[i], true);
            }
            else {
                appendOption(form.cboModuleCode, arrModuleID[i], arrModuleName[i], false);
		    }
        }
    }
    if (form.cboModuleCode.options.length <= 1) {
        form.cboModuleCode.options[0].selected = true;
    }
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<FORM method="POST" action="DMSServlet" name="frmDefectUpdate">
<DIV><IMG border="0" src="Images/DefectViewOrEdit.gif" width="411" height="28"></DIV>
<INPUT type="hidden" name="hidDefectID" value="<%=beanDefectUpdate.getDefectID()%>">
<INPUT type="hidden" name="hidCreateUser" value="<%=beanDefectUpdate.getCreateUser()%>">
<INPUT type="hidden" name="attachedPicture" value="<%=beanDefectUpdate.getStrImage()%>">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="hidTypeOfView" value="<%=beanUserInfo.getTypeOfView()%>">
<INPUT type="hidden" name="hidCurrentRow" value="<%=beanDefectUpdate.getSelectedRow()%>">
<INPUT type="hidden" name="UserName" value="<%=beanUserInfo.getUserName()%>">
<INPUT type="hidden" name="Account" value="<%=beanUserInfo.getAccount()%>">
<INPUT type="hidden" name="Position" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="ProjectCode" value="<%=beanUserInfo.getProjectCode()%>">
<INPUT type="hidden" name="DateLogin" value="<%=beanUserInfo.getDateLogin()%>">
<INPUT type="hidden" name="ProjectID" value="<%=beanUserInfo.getProjectID()%>">
<INPUT type="hidden" name="hidDirect" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="hidFileIndex" value="">
<INPUT type="hidden" name="hidDataIndex" value="">
<TABLE border="0" width="100%">
    <TR>
        <TD align="left"><A href="javascript:doViewHistory()">View History</A></TD>
        <TD align="right"><A href="javascript:doQueryListing()">View DefectListing</A></TD>
    </TR>
</TABLE>
<TABLE border="0" width="100%" class="TblOut2">
    <TR>
        <TD width="8%"><B>User:</B></TD>
        <TD width="24%"><%=beanUserInfo.getUserName()%></TD>
        <TD width="12%"><B>Login Date:</B></TD>
        <TD width="25%"><%=beanUserInfo.getDateLogin()%></TD>
        <TD width="9%"><B>Project</B></TD>
        <TD width="22%" align="right"><SELECT name="cboProjectList" disabled class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="SearchDefect"%>','<%=""%>');"><%
    for (int i = 0; i < beanComboProject.getListing().getNumberOfRows(); i++) {
        int nCurrentProjectID = beanUserInfo.getProjectID();
        int nProjectID = Integer.parseInt(beanComboProject.getListing().getCell(i, 0));
        out.write("<OPTION ");
        out.write(nProjectID == nCurrentProjectID ? " selected " : " ");
        out.write("value='" + nProjectID + "'>" + beanComboProject.getListing().getCell(i, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD width="8%"><B>Group:</B></TD>
        <TD width="24%"><%=beanUserInfo.getGroupName()%></TD>
        <TD width="12%"><B>Position:</B></TD>
        <TD width="25%"><%=beanUserInfo.getPositionName()%></TD>
        <TD width="9%"><B>Status</B></TD>
        <TD width="22%" align="right"><SELECT name="cboProjectStatus" disabled class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="SearchDefect"%>','<%=""%>');"><%
    String strCurrentStatus = beanUserInfo.getCurrentStatus();
    for (int i = 0; i < beanComboProject.getStatusList().getNumberOfRows(); i++) {
        StringMatrix smStatus = beanComboProject.getStatusList();
        out.write("<OPTION ");
        out.write(strCurrentStatus.equals(smStatus.getCell(i,0)) ? " selected " : " ");
        out.write("value=\"" + smStatus.getCell(i, 0) + "\">" + smStatus.getCell(i, 1) + "</OPTION>");
    }
%>        </SELECT></TD>

    </TR>
</TABLE>
<TABLE border="0" width="100%">
    <TR>
        <TD width="20%">&nbsp;</TD>
        <TD width="20%">&nbsp;</TD>
        <TD width="20%">&nbsp;</TD>
        <TD width="20%" align="right">&nbsp;</TD>
        <TD width="20%" align="right">&nbsp; <%
    if (beanDefectUpdate.getIsPrev()) {
%>
        <A class="HeaderMenu" href="javascript:doViewDefect('Prev')">Prev</A> | <%
    }
    else {
%>
        Prev | <%
    }
    if (beanDefectUpdate.getIsNext()) {
%>
        <A class="HeaderMenu" href="javascript:doViewDefect('Next')">Next</A> &nbsp;&nbsp; <%
    }
    else {
%>
        Next <%
    }
%>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="0" cellpadding="1" width="100%">
    <TR class="Row0">
        <TH><FONT size="+1">Main&nbsp;</FONT></TH>
    </TR>
</TABLE>
<!-- SEARCH TABLE -->
<TABLE border="0" width="100%" cellspacing="0" cellpadding="1">
    <TR>
    <!--
    <INPUT name="Date" value="<!--%=intDate%>">
    <INPUT name="Month" value="<!--%=intMonth%>">
    <INPUT name="Year" value="<!--%=intYear%>">
    -->
    </TR>
    <COLGROUP>
        <COL width="10%">
        <COL width="45%">
        <COL width="5%">
        <COL width="10%">
        <COL width="30%">
    <TR>
        <TD nowrap valign="middle" align="left" rowspan="1"><B><FONT color="black">Defect ID</FONT></B></TD><%
    if (beanDefectUpdate.getStatus() != 4) {
%>
        <TD valign="middle" align="left"><B><%=strAttachIcon + beanDefectUpdate.getDefectID()%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Created User&nbsp;&nbsp;&nbsp;&nbsp;<%=beanDefectUpdate.getCreateUser()%></B></TD>
        <TD>&nbsp;</TD>
        <TD>&nbsp;</TD>
        <TD>&nbsp;</TD><%
    }
    else {
%>
        <TD valign="middle" align="left"><B><%=beanDefectUpdate.getDefectID()%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Created User &nbsp;&nbsp;&nbsp;&nbsp;<%=beanDefectUpdate.getCreateUser()%> </B></TD>
        <TD valign="middle" align="left">&nbsp;</TD>
        <!-- Closed Date -->
        <TD nowrap valign="middle"><B><FONT color="black">Closed Date</FONT></B></TD>
        <TD valign="middle" align="left" valign="top"><B><FONT color="black"><%=beanDefectUpdate.getCloseDate()%></FONT></B> <FONT color="blue">(mm/dd/yy)</FONT></TD><%
    }
%>
    </TR>
    <TR>
        <!-- Status -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Status</FONT></B><FONT color="red">*</FONT>
        <INPUT type="hidden" name="hidCurrentStatus" value="<%=beanDefectUpdate.getStatus()%>"></TD>
        <TD valign="middle" align="left"><SELECT name="cboStatus" size="1" class="SmallCombo" onchange="CheckRoleStatus('<%=beanUserInfo.getPosition()%>')"><%
    for (int nRow = 0x00; nRow < beanDefectUpdate.getComboStatus().getNumberOfRows(); nRow++) {
        String strValue = beanDefectUpdate.getComboStatus().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectUpdate.getComboStatus().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectUpdate.getStatus() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD><%
    String strIsDisabled = "";
    if ((beanDefectUpdate.getCloseDate() != null)
            && (!"".equals(beanDefectUpdate.getCloseDate()))
            && (beanDefectUpdate.getFixedDate() != null)
            && (!"".equals(beanDefectUpdate.getFixedDate()))) {
        if (DateUtil.CompareDate(beanDefectUpdate.getFixedDate(), beanDefectUpdate.getCloseDate()) == 1) {
//              strIsDisabled = "disabled";
        }
    }
%>
        <TD>&nbsp;</TD>
        <!-- Fixed Date  -->
        <TD nowrap valign="middle" align="left" id="idFixedDate"><B><FONT color="black">Fixed Date</FONT></B></TD>
        <TD valign="middle" align="left">
            <INPUT type="text" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 45pt" name="txtFixedDate" value="<%=beanDefectUpdate.getFixedDate()%>" maxlength="8" <%=strIsDisabled%>>
            <IMG src="Images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.getElementById("idFixedDate"), document.forms[0].txtFixedDate, "mm/dd/yy",null,1,-1,-1,true)'>
            <FONT color="blue">&nbsp;(mm/dd/yy)</FONT>
        	<INPUT type="hidden" name="hidFixedDate" value="<%=beanDefectUpdate.getFixedDate()%>">
        </TD>
    </TR>
    <TR>
        <!-- Title -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Title</FONT></B><FONT color="red">*</FONT></TD>
        <TD valign="middle" align="left"><INPUT type="text" class="LongBox" name="txtTitle" value="<%=beanDefectUpdate.getTitle()%>" maxlength="150"></TD>
        <TD>&nbsp;</TD>
        <!-- Created Date  -->
        <TD nowrap valign="middle" align="left" id="idCreatedDate"><B><FONT color="black">Created Date</FONT></B><FONT color="red">*</FONT></TD>
        <TD valign="middle" align="left" rowspan="1">
            <INPUT type="text" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 45pt" name="txtCreateDate" value="<%=beanDefectUpdate.getCreateDate()%>" maxlength="8">
            <IMG src="Images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.getElementById("idCreatedDate"), document.forms[0].txtCreateDate, "mm/dd/yy",null,1,-1,-1,true)'>
            <FONT color="blue">&nbsp;(mm/dd/yy)</FONT>
        </TD>
    </TR>
    <TR>
        <!-- Description -->
        <TD nowrap valign="middle" align="left" rowspan="2"><B><FONT color="black">Description</FONT></B><FONT color="red">*</FONT></TD>
        <TD rowspan="2"><TEXTAREA class="LargeTextarea" rows="3" wrap="virtual" name="txtDescription" onchange="hidDescription.value=txtDescription.value"><%=beanDefectUpdate.getDescription()%></TEXTAREA>
        <INPUT type="hidden" name="hidDescription" value="<%=beanDefectUpdate.getDescription()%>"></TD>
        <TD rowspan="2">&nbsp;</TD>
        <TD><B><FONT color="black">Project Origin</FONT></B></TD>
        <TD><INPUT type="text" class="SmallBox" maxlength="50" name="txtProjectOrigin" value="<%=beanDefectUpdate.getStrProject_Origin()%>"></TD>
    </TR>
    <TR>
        <TD valign="top"><B><FONT color="black">Reference</FONT></B></TD>
        <TD valign="top"><INPUT type="text" class="SmallBox" maxlength="30" name="txtRef" value="<%=beanDefectUpdate.getReference()%>"></TD>
    </TR>
    <TR>
        <!-- QC Activity -->
        <TD valign="middle" align="left"><FONT color="black"><B>QC Activity</B></FONT><FONT color="red">*</FONT></TD>
        <TD valign="middle" align="left"><SELECT name="cboQCActivity" size="1" class="SmallCombo" onchange="ChangeTypeActivity()"><%
    for (int nRow = 0x00; nRow < beanDefectUpdate.getComboQCActivity().getNumberOfRows(); nRow++) {
        String strValue = beanDefectUpdate.getComboQCActivity().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectUpdate.getComboQCActivity().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectUpdate.getQCActivity() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD>&nbsp;</TD>
        <!-- Defect Origin  -->
        <TD valign="middle" align="left"><FONT color="black"><B>Defect Origin</B></FONT><FONT color="red">*</FONT></TD>
        <TD valign="middle" align="left"><SELECT name="cboDefectOrigin" size="1" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectUpdate.getComboDefectOrigin().getNumberOfRows(); nRow++) {
        String strValue = beanDefectUpdate.getComboDefectOrigin().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectUpdate.getComboDefectOrigin().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectUpdate.getDefectOrigin() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <!-- Type of Activity -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Type of Activity&nbsp;</FONT></B><FONT color="red">*</FONT></TD>
        <TD valign="middle" align="left"><SELECT name="cboTypeofActivity" size="1" class="SmallCombo">              
        </SELECT></TD>
        <TD>&nbsp;</TD>    
        <!-- Stage injected -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Stage injected&nbsp;</FONT></B></TD>
        <TD valign="middle" align="left"><SELECT name="cboStageInjected" size="1" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectUpdate.getComboStageInjected().getNumberOfRows(); nRow++) {
        String strValue = beanDefectUpdate.getComboStageInjected().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectUpdate.getComboStageInjected().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectUpdate.getStageInjected() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <!-- Stage detected -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Stage detected</FONT></B></TD>
        <TD valign="middle" align="left"><SELECT name="cboStageDetected" size="1" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectUpdate.getComboStageDetected().getNumberOfRows(); nRow++) {
        String strValue = beanDefectUpdate.getComboStageDetected().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectUpdate.getComboStageDetected().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectUpdate.getStageDetected() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD>&nbsp;</TD>
        <!-- Priority -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Priority</FONT></B></TD>
        <TD valign="middle" align="Left"><SELECT name="cboPriority" size="1" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectUpdate.getComboPriority().getNumberOfRows(); nRow++) {
        String strValue = beanDefectUpdate.getComboPriority().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectUpdate.getComboPriority().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectUpdate.getPriority() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <!-- Work Product COMBO -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Work Product&nbsp;</FONT></B><FONT color="red">*</FONT></TD>
        <TD valign="middle" align="left"><SELECT name="cboWorkProduct" size="1" class="SmallCombo" onchange="javascript:selectWorkProduct(<%=beanDefectUpdate.getModuleCode()%>);"><%
    for (int nRow = 0x00; nRow < beanDefectUpdate.getComboWorkProduct().getNumberOfRows(); nRow++) {
        String strValue = beanDefectUpdate.getComboWorkProduct().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectUpdate.getComboWorkProduct().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectUpdate.getWorkProduct() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD>&nbsp;</TD>
        <!-- Severity -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Severity</FONT></B><FONT color="red">*</FONT></TD>
        <TD valign="middle" align="Left"><SELECT name="cboSeverity" size="1" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectUpdate.getComboSeverity().getNumberOfRows(); nRow++) {
        String strValue = beanDefectUpdate.getComboSeverity().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectUpdate.getComboSeverity().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectUpdate.getSeverity() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <!-- Module Code -->
        <TD nowrap valign="middle" align="left" rowspan="1"><B><FONT color="black">Module Code</FONT></B></TD>
        <TD valign="middle" align="left"><SELECT name="cboModuleCode" size="1" class="LongCombo">
        </SELECT></TD>
        <TD>&nbsp;</TD>
        <!-- Type -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Type</FONT></B><FONT color="red">&nbsp;*</FONT></TD>
        <TD valign="middle" align="Left"><SELECT name="cboType" size="1" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectUpdate.getComboType().getNumberOfRows(); nRow++) {
        String strValue = beanDefectUpdate.getComboType().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectUpdate.getComboType().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectUpdate.getType() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    
     <TR>
        <!-- Defect owner -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Defect owner</FONT></B></TD>
        <TD valign="middle" align="Left"><SELECT name="cboDefectOwner" size="1" class="SmallCombo" onchange="changeAssignTo()"><%
    for (int nRow = 0x00; nRow < beanDefectUpdate.getComboAssignTo().getNumberOfRows(); nRow++) {
        String strText = beanDefectUpdate.getComboAssignTo().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectUpdate.getDefectOwner().toUpperCase().equals(strText.toUpperCase()) ? " selected " : " ");
        out.write("value=\"" + strText + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    
    <TR>
        <!-- AssignTo -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Assigned To</FONT></B></TD>
        <TD valign="middle" align="Left"><SELECT name="cboAssignTo" size="1" class="SmallCombo" onchange="changeAssignTo()"><%
    for (int nRow = 0x00; nRow < beanDefectUpdate.getComboAssignTo().getNumberOfRows(); nRow++) {
        String strText = beanDefectUpdate.getComboAssignTo().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectUpdate.getAssignTo().toUpperCase().equals(strText.toUpperCase()) ? " selected " : " ");
        out.write("value=\"" + strText + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD>&nbsp;</TD>
        <!-- Due Date -->
        <TD nowrap valign="middle" align="left" rowspan="1" id="idDueDate"><B><FONT color="black">Due Date</FONT></B></TD>
        <TD valign="middle" align="left">
            <INPUT type="text" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 45pt" name="txtDueDate" value="<%=beanDefectUpdate.getDueDate()%>" maxlength="8">
            <IMG src="Images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.getElementById("idDueDate"), document.forms[0].txtDueDate, "mm/dd/yy",null,1,-1,-1,true)'>
            <FONT color="blue">&nbsp;(mm/dd/yy)</FONT>
        </TD>
    </TR>

<!-- add  -->

    <TR>
        <TD rowspan="2" nowrap valign="middle" align="left"><B><FONT color="black">Cause analysis&nbsp;</FONT></B></TD>
        <TD rowspan="2"><TEXTAREA class="LargeTextarea" wrap="virtual" name="txtCauseAnalysis" onchange="hidCauseAnalysis.value=txtCauseAnalysis.value"><%=beanDefectUpdate.getCauseAnalysis()%></TEXTAREA> <INPUT type="hidden" name="hidCauseAnalysis" value="<%=beanDefectUpdate.getCauseAnalysis()%>"></TD>
        <TD rowspan="2">&nbsp;</TD>
        <TD valign="top"><B><FONT color="black">Test Case ID</FONT></B></TD>
        <TD valign="top"><INPUT type="text" class="SmallBox" maxlength="50" name="txtTestCase" value="<%=beanDefectUpdate.getTestCase()%>">
        <DIV id="overDiv" style="position: absolute; visibility: hide; FONT-SIZE: xx-small; FONT-FAMILY: Verdana"></DIV>
        <BR>
        <INPUT type="hidden" name="Image" value="<%=beanDefectUpdate.getStrImage()%>"></TD>
    </TR>
	<TR>
		<TD>
		</TD>
	</TR>


<!-- end -->    

    
    <TR>
        <!-- Corrective Actions -->
        <TD rowspan="2" nowrap valign="middle" align="left"><B><FONT color="black">Corrective Action&nbsp;</FONT></B></TD>
        <TD rowspan="2"><TEXTAREA class="LargeTextarea" wrap="virtual" name="txtCorrectiveAction" onchange="hidCorrectiveAction.value=txtCorrectiveAction.value"><%=beanDefectUpdate.getCorrectiveAction()%></TEXTAREA> <INPUT type="hidden" name="hidCorrectiveAction" value="<%=beanDefectUpdate.getCorrectiveAction()%>"></TD>
        <TD rowspan="2">&nbsp;</TD>
        <TD nowrap valign="middle" align="left" ></TD>
        <TD valign="middle">
        <DIV id="overDiv" style="position: absolute; visibility: hide; FONT-SIZE: xx-small; FONT-FAMILY: Verdana"></DIV>
        <BR>
        <INPUT type="hidden" name="Image" value="<%=beanDefectUpdate.getStrImage()%>"></TD>
    </TR>
    <TR>
    	<TD>
			&nbsp;
    	</TD>
    	<TD>
			&nbsp;
    	</TD>
    </TR>
    
</TABLE>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="1">
    <TR>
        <TD><%=strAttachLink%></TD>
    </TR>
</TABLE>
<%
    String strPosition = beanUserInfo.getPosition();
    String strDisabledUpdate = "";
	//0 --> Developer
	//1 --> Tester
	//2 --> Project Leader
	//6 --> External User
    if (strPosition.charAt(2)!='1' && strPosition.charAt(1)!='1' && strPosition.charAt(0)!='1' && strPosition.charAt(6)!='1') {
        strDisabledUpdate = "disabled";
    }

    String strDisabledDelete = "";
    if (!beanDefectUpdate.getCreateUser().equals(beanUserInfo.getAccount())) {
        strDisabledDelete = "disabled";
    }
%>
<P align="left">
&nbsp;<INPUT type="button" name="UpdateDefect" class="Button" onclick="javascript:doSave()" value="Save" <%=strDisabledUpdate%>>
&nbsp;<INPUT type="button" name="DeleteDefect" class="Button" onclick="javascript:doDelete()" value="Delete" <%=strDisabledDelete%>>
&nbsp;<INPUT type="button" name="Back" class="Button" onclick="javascript:doBack()" value="Defect List">
&nbsp;<INPUT type="button" name="Attach" class="Button" onclick="javascript:doAttach()" value="Attach File">
</P>
</FORM>
<BR>
<BR>
<BR>
<BR>
<SCRIPT language="JavaScript">
selectWorkProduct(<%=beanDefectUpdate.getModuleCode()%>);
ChangeTypeActivity();
document.forms[0].txtTitle.focus();
</SCRIPT>
</BODY>
</HTML>