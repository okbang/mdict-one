<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java"
    import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.DefectManagement.*,
            fpt.dms.bo.combobox.*,
            fpt.dms.framework.util.StringUtil.*,
            fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    DefectAddBean beanDefectAdd = (DefectAddBean)request.getAttribute("beanDefectAdd");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
    DefectAttachBean beanDefectAttach =
            (DefectAttachBean) session.getAttribute("beanDefectAttach");
    String strAttachLink = "";
    if (beanDefectAttach != null) {
        if (beanDefectAttach.getAttachMode() == DefectAttachBean.MODE_ADD_NEW) {
            strAttachLink = beanDefectAttach.writeHtmlLink();
        }
        else {
            beanDefectAttach.discardTempAttach();
            session.removeAttribute("beanDefectAttach");
        }
    }
    int hidSaveNewCounter = (session.getAttribute("hidSaveNewCounter") != null ?
            Integer.parseInt((String) session.getAttribute("hidSaveNewCounter")): 0);
    session.setAttribute("hidSaveNewCounter", Integer.toString(hidSaveNewCounter + 1));
%>
<HTML>
<HEAD>
<SCRIPT type="text/javascript" src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT type="text/javascript" src="scripts/utils.js"></SCRIPT>
<SCRIPT type="text/javascript" src="scripts/popcalendar.js"></SCRIPT>
<TITLE>Add New Defect</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<LINK rel="StyleSheet" href="styles/pcal.css" type="text/css">
<SCRIPT language="javascript">
function doSave() {
    var form = document.frmDefectAdd;
    document.forms[0].NewDefect.disabled = true;
    if (!isValidForm()) {
        document.forms[0].NewDefect.disabled = false;
        return;
    }
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SaveNewDefect";
    form.action = "DMSServlet";
    form.hidSaveNewCounter.value = parseInt(form.hidSaveNewCounter.value) + 1;  // Validator for check user refresh button
    form.submit();
}

function doBack() {
    var form = document.frmDefectAdd;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doQueryListing() {
    var form = document.frmDefectAdd;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function doAttach() {
    var form = document.forms[0];
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "<%=DMS.DEFECT_ATTACH_NEW_FORM%>";
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
        removeAttachFile(nIndex, form, "<%=DMS.DEFECT_ATTACH_FILE_REMOVE_NEW%>");
    }
}

function isValidForm() {
    var count;
    var form = document.frmDefectAdd;
    for (count = 0x00; count < document.forms[0].length; count++) {
        if (!isValidControl(document.forms[0].elements[count])) {
            return false;
        }
    }
    if ((form.DueDate.value.length > 0)) {
        if (CompareDate(form.DueDate , form.CreateDate) < 0) {
            alert("Due Date must greater than or equals to Create Date");
            form.DueDate.focus();
            return false;
        }
    }
    return true;
}

function isValidControl(control) {
    if (control.name == "CreateDate") {
        if (isDate(control) && isLessThanCurDate(control)) {
            return true;
        }
        else {
            return false;
        }
    }
    else if (control.name == "DueDate") {
        if (control.value.length <= 0 || isDate(control)) {
            return true;
        }
        else {
            return false;
        }
    }
    else if (control.name == "Title") {
        return textValidate(control, 1, 150);
    }
    else if (control.name == "WorkProduct") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "Severity") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "TypeofActivity"){
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "QCActivity") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "Type") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "DefectOrigin") {
        return isPositiveNumberCombobox(control);
    }
    else if (control.name == "txtDescription") {
        control.name = "Description";
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
    return true;
}
//modified by MinhPT
//09-Dec-03
//for fix TypeOfAcitvity when select QCActivity
function ChangeTypeActivity() {
    var form = document.forms[0];
    var QCActivity = form.QCActivity.value;
    var check = QCActivity.substring(0,1);

    while (form.TypeofActivity.options.length > 0) {
        form.TypeofActivity.options[0] = null;
    }

    switch (check) {
        case "1": //Test
            appendOption(form.TypeofActivity, 1, '<%=beanDefectAdd.getComboTypeofActivity().getCell(3, 1)%>', true);
            break;
        case "2": //Review
            appendOption(form.TypeofActivity, 2, '<%=beanDefectAdd.getComboTypeofActivity().getCell(2, 1)%>', true);
            break;
        case "3": //Inspection
            appendOption(form.TypeofActivity, 3, '<%=beanDefectAdd.getComboTypeofActivity().getCell(1, 1)%>', true);
            break;
        case "4": //Audit
            appendOption(form.TypeofActivity, 4, '<%=beanDefectAdd.getComboTypeofActivity().getCell(0, 1)%>', true);
            break;
    }
}

//added by MinhPT
//06-Dec-03
<%
    int nNumberOfModule = beanDefectAdd.getComboModuleCode().getNumberOfRows();
    out.write("var nNumberOfModule = " + nNumberOfModule + ";\n");
    out.write("var arrModuleID = new Array(" + nNumberOfModule + ");\n");
    out.write("var arrModuleName = new Array(" + nNumberOfModule + ");\n");
    out.write("var arrModuleWPID = new Array(" + nNumberOfModule + ");\n");
    for (int i = 0; i < nNumberOfModule; i++) {
        String strValue = beanDefectAdd.getComboModuleCode().getCell(i, 0);
        out.write("arrModuleID[" + i + "] = \"" + strValue + "\";\n");
        strValue = beanDefectAdd.getComboModuleCode().getCell(i, 1);
        out.write("arrModuleName[" + i + "] = \"" + strValue + "\";\n");
        strValue = beanDefectAdd.getComboModuleCode().getCell(i, 4);
        out.write("arrModuleWPID[" + i + "] = \"" + strValue + "\";\n");
    }
%>
//added by MinhPT
//06-Dec-03
//Corrected by TrungTN
function selectWorkProduct(){
    var form = document.forms[0];
    var i;
    while (form.ModuleCode.options.length > 0) {
        form.ModuleCode.options[0] = null;
    }
    appendOption(form.ModuleCode, "0", "", false);
    
    currentWPID = form.cboWorkProduct.value;
    for (i = 0; i < nNumberOfModule; i++) {
        if (arrModuleWPID[i] == currentWPID) {
            if (arrModuleID[i] == '<%=beanDefectAdd.getModuleCode()%>') {
                appendOption(form.ModuleCode, arrModuleID[i], arrModuleName[i], true);
            }
            else {
                appendOption(form.ModuleCode, arrModuleID[i], arrModuleName[i], false);
		    }
        }
    }
    if (form.ModuleCode.options.length == 1) {
        // Important when combo have only one empty element
        form.ModuleCode.options[0].selected=true;
    }
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<FORM method="POST" action="DMSServlet" name="frmDefectAdd">
<DIV><IMG border="0" src="Images/DefectAddnew.gif" width="411" height="28"></DIV>
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="hidTypeOfView" value="<%=beanUserInfo.getTypeOfView()%>">
<INPUT type="hidden" name="UserName" value="<%=beanUserInfo.getUserName()%>">
<INPUT type="hidden" name="Account" value="<%=beanUserInfo.getAccount()%>">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="Position" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="ProjectCode" value="<%=beanUserInfo.getProjectCode()%>">
<INPUT type="hidden" name="DateLogin" value="<%=beanUserInfo.getDateLogin()%>">
<INPUT type="hidden" name="ProjectID" value="<%=beanUserInfo.getProjectID()%>">
<INPUT type="hidden" name="hidFileIndex" value="">
<INPUT type="hidden" name="hidSaveNewCounter" value="<%=hidSaveNewCounter%>">
<TABLE border="0" width="100%">
    <TR>
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
        <TD width="22%" align="right"><SELECT name="cboProjectList" disabled class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="QueryListing"%>','<%=""%>');"><%
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
        <TD width="22%" align="right"><SELECT name="cboProjectStatus" disabled class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="QueryListing"%>','<%=""%>');"><%
    String strCurrentStatus = beanUserInfo.getCurrentStatus();
    for (int i = 0; i < beanComboProject.getStatusList().getNumberOfRows(); i++) {
        StringMatrix smStatus = beanComboProject.getStatusList();
        out.write("<OPTION ");
        out.write(strCurrentStatus.equals(smStatus.getCell(i,0)) ? " selected " : " ");
        out.write("value=\"" + smStatus.getCell(i, 0) + "\">" + smStatus.getCell(i, 1) + "</OPTION>");
    }
%>        
        </SELECT></TD>

    </TR>
</TABLE>
<BR>
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" class="TblOut">
    <TR class="Row0">
        <TD width="100%" align="center"><FONT size="+1">Main</FONT></TD>
    </TR>
</TABLE>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="1">
    <COLGROUP>
        <COL width="15%">
        <COL width="45%">
        <COL width="5%">
        <COL width="10%">
        <COL width="30%">
        <INPUT type="hidden" name="DefectID" value="<%=beanDefectAdd.getDefectID()%>">
    <TR>
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Status:&nbsp; Error</FONT></B> <INPUT type="hidden" name="hidStatus" value="<%=beanDefectAdd.getStatus()%>"></TD>
        <TD valign="middle" align="left"><B><FONT color="black">Created user:</FONT></B>&nbsp;<B><%=beanDefectAdd.getCreateUser()%></B></TD>
        <INPUT type="hidden" name="CreateUser" value="<%=beanDefectAdd.getCreateUser()%>">
        <TD>&nbsp;</TD>
        <!-- Created Date  -->
        <TD nowrap valign="middle" align="left" id="idCreatedDate"><B><FONT color="black">Created date</FONT></B><FONT color="red">&nbsp;*</FONT></TD>
        <TD valign="middle" align="left">
            <INPUT type="text" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 45pt" name="CreateDate" value="<%=beanDefectAdd.getCreateDate()%>" maxlength="8">
            <IMG src="Images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.getElementById("idCreatedDate"), document.forms[0].CreateDate, "mm/dd/yy",null,1,-1,-1,true)'>
            <FONT color="blue">&nbsp;(mm/dd/yy)</FONT>
        </TD>
    </TR>
    <TR>
        <!-- Title -->
        <TD valign="middle" align="left"><B><FONT color="black">Title</FONT></B><FONT color="red">&nbsp;*</FONT></TD>
        <TD valign="middle" align="left"><INPUT type="text" class="LongBox" maxlength="150" name="Title" value="<%=beanDefectAdd.getTitle()%>"></TD>
        <!-- Project Origin -->
        <TD>&nbsp;</TD>
        <TD><B><FONT color="black">Project Origin</FONT></B></TD>
        <TD><INPUT type="text" class="SmallBox" maxlength="50" name="ProjectOrigin" value="<%=beanDefectAdd.getStrProject_Origin()%>"></TD>
    </TR>
    <TR>
        <!-- Description -->
        <TD valign="middle" align="left"><B><FONT color="black">Description</FONT></B><FONT color="red">&nbsp;*</FONT></TD>
        <TD valign="middle"><TEXTAREA class="LargeTextarea" rows="3" wrap="virtual" name="txtDescription" onchange="Description.value=txtDescription.value"><%=beanDefectAdd.getDescription()%></TEXTAREA> <INPUT type="hidden" name="Description" value="<%=beanDefectAdd.getDescription()%>"></TD>
        <TD>&nbsp;</TD>
        <!-- Reference -->
        <TD valign="top"><B><FONT color="black">Reference</FONT></B></TD>
        <TD valign="top"><INPUT type="text" class="SmallBox" maxlength="30" name="Ref" value="<%=beanDefectAdd.getReference()%>"></TD>
    </TR>
    <TR>
        <!-- QC Activity -->
        <TD valign="middle" align="left"><FONT color="black"><B>QC activity</B></FONT><FONT color="red">&nbsp;*</FONT></TD>
        <TD valign="middle" align="left"><SELECT name="QCActivity" class="SmallCombo" onchange="ChangeTypeActivity()"><%
    for (int nRow = 0x00; nRow < beanDefectAdd.getComboQCActivity().getNumberOfRows(); nRow++) {
        String strValue = beanDefectAdd.getComboQCActivity().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectAdd.getComboQCActivity().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectAdd.getQCActivity() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD>&nbsp;</TD>
        <!-- Defect Origin  -->
        <TD valign="middle" align="left"><FONT color="black"><B>Defect origin</B></FONT><FONT color="red">&nbsp;*</FONT></TD>
        <TD valign="middle" align="left"><SELECT name="DefectOrigin" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectAdd.getComboDefectOrigin().getNumberOfRows(); nRow++) {
        String strValue = beanDefectAdd.getComboDefectOrigin().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectAdd.getComboDefectOrigin().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectAdd.getDefectOrigin() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <!-- Type of Activity -->
        <TD valign="middle" align="left"><B><FONT color="black">Type of activity</FONT></B><FONT color="red">&nbsp;*</FONT></TD>
        <TD valign="middle" align="left"><SELECT name="TypeofActivity" class="SmallCombo">
        </SELECT></TD>
        <TD>&nbsp;</TD>
        <!-- Stage injected -->
        <TD valign="middle" align="left"><B><FONT color="black">Stage injected&nbsp;</FONT></B></TD>
        <TD valign="middle" align="left"><SELECT name="StageInjected" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectAdd.getComboStageInjected().getNumberOfRows(); nRow++) {
        String strValue = beanDefectAdd.getComboStageInjected().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectAdd.getComboStageInjected().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectAdd.getStageInjected() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <!-- Stage detected -->
        <TD nowrap valign="middle" align="left"><B><FONT color="black">Stage detected</FONT></B></TD>
        <TD valign="middle" align="left"><SELECT name="StageDetected" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectAdd.getComboStageDetected().getNumberOfRows(); nRow++) {
        String strValue = beanDefectAdd.getComboStageDetected().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectAdd.getComboStageDetected().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectAdd.getStageDetected() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD>&nbsp;</TD>
        <!-- Priority -->
        <TD valign="middle" align="left"><B><FONT color="black">Priority</FONT></B></TD>
        <TD valign="middle" align="left"><SELECT name="Priority" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectAdd.getComboPriority().getNumberOfRows(); nRow++) {
        String strValue = beanDefectAdd.getComboPriority().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectAdd.getComboPriority().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectAdd.getPriority() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <!-- Work Product COMBO -->
        <TD valign="middle" align="left"><B><FONT color="black">Work product</FONT></B><FONT color="red">&nbsp;*</FONT></TD>
        <TD valign="middle" align="left"><SELECT name="cboWorkProduct" class="SmallCombo" onchange="javascript:selectWorkProduct();"><%
    for (int nRow = 0x00; nRow < beanDefectAdd.getComboWorkProduct().getNumberOfRows(); nRow++) {
        String strValue = beanDefectAdd.getComboWorkProduct().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectAdd.getComboWorkProduct().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectAdd.getWorkProduct() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD>&nbsp;</TD>
        <!-- Severity -->
        <TD valign="middle" align="left"><B><FONT color="black">Severity</FONT></B><FONT color="red">&nbsp;*</FONT></TD>
        <TD valign="middle" align="left"><SELECT name="Severity" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectAdd.getComboSeverity().getNumberOfRows(); nRow++) {
        String strValue = beanDefectAdd.getComboSeverity().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectAdd.getComboSeverity().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectAdd.getSeverity() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <!-- Module Code -->
        <TD valign="middle" align="left"><B><FONT color="black">Module</FONT></B></TD>
        <TD valign="middle" align="left"><SELECT name="ModuleCode" class="LongCombo">
        </SELECT></TD>
        <TD>&nbsp;</TD>
        <!-- Type -->
        <TD valign="middle" align="left"><B><FONT color="black">Type</FONT></B><FONT color="red">&nbsp;*</FONT></TD>
        <TD valign="middle" align="left"><SELECT name="Type" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectAdd.getComboType().getNumberOfRows(); nRow++) {
        String strValue = beanDefectAdd.getComboType().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectAdd.getComboType().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectAdd.getType() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
        <!-- Defect Owner -->
    	<TD valign="middle" align="left"><B><FONT color="black">Defect owner</FONT></B></TD>
        <TD valign="middle" align="left"><SELECT name="DefectOwner" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectAdd.getComboAssignTo().getNumberOfRows(); nRow++) {
        String strText = beanDefectAdd.getComboAssignTo().getCell(nRow, 1);
		out.write("<OPTION");
        out.write(beanDefectAdd.getDefectOwner().toUpperCase().equals(strText.toUpperCase()) ? " selected " : " ");
		out.write(" value=\"" + strText + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    <TR>
    
    <TR>
        <!-- AssignTo -->
        <TD valign="middle" align="left"><B><FONT color="black">Assigned To</FONT></B></TD>
        <TD valign="middle" align="left"><SELECT name="AssignTo" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectAdd.getComboAssignTo().getNumberOfRows(); nRow++) {
        String strText = beanDefectAdd.getComboAssignTo().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectAdd.getAssignTo().toUpperCase().equals(strText.toUpperCase()) ? " selected " : " ");
        out.write(" value=\"" + strText + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD>&nbsp;</TD>
        <!-- Due Date -->
        <TD valign="middle" align="left" id="idDueDate"><B><FONT color="black">Due date</FONT></B></TD>
        <TD valign="middle" align="left">
            <INPUT type="text" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 45pt" name="DueDate" value="<%=beanDefectAdd.getDueDate()%>" maxlength="8">
            <IMG src="Images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.getElementById("idDueDate"), document.forms[0].DueDate, "mm/dd/yy",null,1,-1,-1,true)'>
            <FONT color="blue">&nbsp;(mm/dd/yy)</FONT></TD>
    </TR>
    <TR>
        <TD rowspan="2" nowrap valign="middle" align="left"><B><FONT color="black">Cause analysis&nbsp;</FONT></B></TD>
        <TD rowspan="2"><TEXTAREA class="LargeTextarea" wrap="virtual" name="txtCauseAnalysis" onchange="hidCauseAnalysis.value=txtCauseAnalysis.value"><%=beanDefectAdd.getCauseAnalysis()%></TEXTAREA> <INPUT type="hidden" name="hidCauseAnalysis" value="<%=beanDefectAdd.getCauseAnalysis()%>"></TD>
        <TD rowspan="2">&nbsp;</TD>
        <TD valign="top"><B><FONT color="black">Test Case ID</FONT></B></TD>
        <TD valign="top"><INPUT type="text" class="SmallBox" maxlength="50" name="txtTestCase" value="">
        <DIV id="overDiv" style="position: absolute; visibility: hide; FONT-SIZE: xx-small; FONT-FAMILY: Verdana"></DIV>
        <BR>
        <INPUT type="hidden" name="Image" value=""></TD>
    </TR>
    <TR></TR>
    <TR>
        <!-- Corrective Action -->
        <TD rowspan="2" valign="middle" align="left"><B><FONT color="black">Corrective action&nbsp;</FONT></B></TD>
        <TD rowspan="2"><TEXTAREA class="LargeTextarea" wrap="virtual" name="txtCorrectiveAction" onchange="CorrectiveAction.value=txtCorrectiveAction.value"><%=beanDefectAdd.getCorrectiveAction()%></TEXTAREA></TD>
        <TD rowspan="2">&nbsp;</TD>
        <TD valign="top" colspan="2">&nbsp;
        <DIV id="overDiv" style="position: absolute; visibility: hide; FONT-SIZE: xx-small; FONT-FAMILY: Verdana"></DIV>
        <BR>
        <INPUT type="hidden" name="Image" value=""></TD>
        <INPUT type="hidden" name="CorrectiveAction" value="<%=beanDefectAdd.getCorrectiveAction()%>">
    </TR>
    <TR>
    	<TD height="1"></TD>
    	<TD height="1"></TD>
    	<TD height="1"></TD>
    </TR>
</TABLE>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="1">
    <TR>
        <TD><%=strAttachLink%></TD>
    </TR>
</TABLE>
<P>
<INPUT type="button" name="NewDefect" class="Button" onclick="javascript:doSave()" value="Save">&nbsp;&nbsp;&nbsp;&nbsp;
<INPUT type="button" name="Back" class="Button" onclick="javascript:doBack()" value="Defect List">&nbsp;&nbsp;&nbsp;&nbsp;
<INPUT type="button" name="Attach" class="Button" onclick="javascript:doAttach()" value="Attach file">
</P>
</FORM>
<BR>
<BR>
<BR>
<SCRIPT language="JavaScript">
    selectWorkProduct();
    ChangeTypeActivity();
    document.forms[0].Title.focus();
</SCRIPT>
</BODY>
</HTML>
