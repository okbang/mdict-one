<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.ProjectEnvironment.*,
            fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    DefectPlanBean beanDefectPlan
            = (DefectPlanBean)request.getAttribute("beanDefectPlan");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
    String strClientMessage = (String)request.getAttribute("ClientMessageResult");
    if (strClientMessage == null) {
        strClientMessage = "";
    }
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT src="scripts/validate.js"></SCRIPT>
<TITLE>Manage Planned Defect</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/DMSStyleSheet.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
function doAddNew() {
	alert("This function is deprecated and moved to FSoft Insight");
/*    var form = document.frmDefectPlan;
    var bAdded = false;
    var strValue;
    if (!isPositiveNumberCombobox(form.cboWorkProduct)) {
        return;
    }
    var objQC;
    objQC = form.txtDocumentR;
    strValue = trimSpaces(objQC.value);
    if (strValue.length > 0) {
        if (!validateNumber(form.txtDocumentR)) {
            return;
        }
        bAdded = true;
    }
    objQC = form.txtPrototypeR;
    strValue = trimSpaces(objQC.value);
    if (strValue.length > 0) {
        if (!validateNumber(form.txtPrototypeR)) {
            return;
        }
        bAdded = true;
    }
    objQC = form.txtCodeR;
    strValue = trimSpaces(objQC.value);
    if (strValue.length > 0) {
        if (!validateNumber(form.txtCodeR)) {
            return;
        }
        bAdded = true;
    }
    objQC = form.txtUnitT;
    strValue = trimSpaces(objQC.value);
    if (strValue.length > 0) {
        if (!validateNumber(form.txtUnitT)) {
            return;
        }
        bAdded = true;
    }
    objQC = form.txtIntegrationT;
    strValue = trimSpaces(objQC.value);
    if (strValue.length > 0) {
        if (!validateNumber(form.txtIntegrationT)) {
            return;
        }
        bAdded = true;
    }
    objQC = form.txtSystemT;
    strValue = trimSpaces(objQC.value);
    if (strValue.length > 0) {
        if (!validateNumber(form.txtSystemT)) {
            return;
        }
        bAdded = true;
    }
    objQC = form.txtAcceptanceT;
    strValue = trimSpaces(objQC.value);
    if (strValue.length > 0) {
        if (!validateNumber(form.txtAcceptanceT)) {
            return;
        }
        bAdded = true;
    }
    objQC = form.txtOthers;
    strValue = trimSpaces(objQC.value);
    if (strValue.length > 0) {
        if (!validateNumber(form.txtOthers)) {
            return;
        }
        bAdded = true;
    }
    if (!bAdded) {
        return;
    }
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "AddDefectPlan";
    form.action = "DMSServlet";
    form.submit();*/
}

function doDelete(row) {
	alert("This function is deprecated and moved to FSoft Insight");
/*    var form = document.frmDefectPlan;
    var bOK = window.confirm("Are you sure to clear?");
    if (!bOK) {
        return;
    }
    form.hidSelectedItem.value = row;
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "DeleteDefectPlan";
    form.action = "DMSServlet";
    form.submit();*/
}

function doQueryListing() {
    var form = document.frmDefectPlan;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function numberAllowed() {
    if (window.event.keyCode > 57 || window.event.keyCode < 48) {
        window.event.keyCode = 0;
    }
}

function validateNumber(txtDuration) {
    var durationValue;
    durationValue = txtDuration.value;
    if (trimSpaces(durationValue) == "") {
        return true;
    }
    if ((isNaN(durationValue)) || (durationValue < 0)) {
        alert("Invalid number.");
        txtDuration.focus();
        return false;
    }
    return true;
}

function trimSpaces(value) {
    var length = value.length;
    for (i = 0; i <length; i++) {
        if (value.charAt(i) != ' ') {
            break;
        }
    }
    var start = i;
    for (i = length - 1; i > start; i--) {
        if (value.charAt(i) != ' ') {
            break;
        }
    }
    var end = length - i - 1;
    value = value.substring(start,length - end);
    return value;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<DIV>
<P><IMG border="0" src="Images/title_Planned_Defect_Listing.gif" width="451" height="28"></P>
</DIV>
<FORM method="post" action="DMSServlet" name="frmDefectPlan">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
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
%>        </SELECT></TD>

    </TR>
</TABLE>
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="hidSelectedItem" value="<%=beanDefectPlan.getSelectedItem()%>">
<BR>
<TABLE border="0" cellpadding="1" cellspacing="1" width="100%">
    <TR>
        <TD width="70%" align="left"><B><FONT color="#cc0001" style="FONT-FAMILY: Verdana; FONT-SIZE: 10pt"><%=strClientMessage%></FONT></B>&nbsp;</TD>
        <TD width="30%" align="right"><FONT style="COLOR: black; FONT-FAMILY: Verdana; FONT-SIZE: xx-small">(Unit: weighted defect)</FONT></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" bgcolor="#000000">
    <COLGROUP>
        <COL width="3%">
        <COL width="21%">
        <COL width="21%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
    <TR class="Row0">
        <TD align="center" rowspan="2">C</TD>
        <TD align="center" rowspan="2"><B>Work Product</B></TD>
        <TD align="center" rowspan="2"><B>Module</B></TD>
        <TD align="center" rowspan="2"><B>Planned Defect</B></TD>
        <TD align="center" rowspan="2"><B>Re-planned Defect</B></TD>
        <TD align="center" colspan="9"><B>Plan by QC Activity</B></TD>
    </TR>
    <TR class="Row0">
        <TD align="center">DR</TD>
        <TD align="center">PR</TD>
        <TD align="center">CR</TD>
        <TD align="center">UT</TD>
        <TD align="center">IT</TD>
        <TD align="center">ST</TD>
        <TD align="center">AT</TD>
        <TD align="center">Others</TD>
        <TD align="center"><B>Total</B></TD>
    </TR><%
    String strCurrentItem = "", strName = "";
    int nClass = 0;
    int nNumberOfRows = beanDefectPlan.getPlannedDefectList().getNumberOfRows();
    for (int i = 0; i < nNumberOfRows; i++) {
        String strItem = beanDefectPlan.getPlannedDefectList().getCell(i, 1);
        strName = beanDefectPlan.getPlannedDefectList().getCell(i, 1);
        if (strCurrentItem.equals(strItem)) {
            strName = "";
            nClass -= 1;
        }
        strCurrentItem = strItem;
        String clsTR = "Row2";
        if (nClass % 2 == 0) {
            clsTR = "Row1";
        }
        String strHREF = "javascript:doDelete('"
                + beanDefectPlan.getPlannedDefectList().getCell(i, 0) + "')";
        if (i == nNumberOfRows - 1) {//the last row
            strHREF = "#";
        }
        String strNote = "";
        if (!"".equals(strName) && i < nNumberOfRows - 1) {
            String strWPPlannedDefect = beanDefectPlan.getPlannedDefectList().getCell(i, 14);
            String strWPReplannedDefect = beanDefectPlan.getPlannedDefectList().getCell(i, 15);
            strNote = !"".equals(strWPReplannedDefect) ? strWPReplannedDefect : strWPPlannedDefect;
            if (!"".equals(strNote)) {
                strNote = "(" + strNote + ")";
            }
        }
%>
    <TR class="<%= clsTR%>">
        <TD align="center"><A href="<%=strHREF%>" title="Clear">C</A></TD>

        <!-- WorkProduct Name -->
        <TD style="text-indent: 5px"><%=strName%>&nbsp;<%=strNote%></TD>
        <!-- Module Name -->
        <TD style="text-indent: 5px"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 2)%></TD>

        <TD align="center"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 3)%></TD>
        <!-- Planned defect -->
        <TD align="center"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 4)%></TD>
        <!-- Re-planned defect -->
        <TD align="center"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 5)%></TD>
        <!-- Document review -->
        <TD align="center"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 6)%></TD>
        <!-- Prototype review -->
        <TD align="center"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 7)%></TD>
        <!-- Code review -->
        <TD align="center"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 8)%></TD>
        <!-- Unit test -->
        <TD align="center"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 9)%></TD>
        <!-- Integration test -->
        <TD align="center"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 10)%></TD>
        <!-- System test -->
        <TD align="center"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 11)%></TD>
        <!-- Acceptance test -->
        <TD align="center"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 12)%></TD>
        <!-- Others -->
        <TD align="center"><%=beanDefectPlan.getPlannedDefectList().getCell(i, 13)%></TD>
        <!-- Total of actual defects -->
    </TR><%
        nClass += 1;
    }
%>
</TABLE>
<BR>
<DIV align="center"><!-- ADDNEW table  -->
<TABLE border="0" width="90%" cellspacing="0" cellpadding="0" align="center">
    <COLGROUP>
        <COL width="10%">
        <COL width="35%">
        <COL width="15%">
        <COL width="10%">
        <COL width="5%">
        <COL width="15%">
        <COL width="10%">
    <TR>
        <TD><B>Work Product</B><FONT color="red">*</FONT></TD>
        <TD><SELECT name="cboWorkProduct" size="1" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 130pt" tabindex="1"><%
    for (int nRow = 0x00; nRow < beanDefectPlan.getComboWorkProduct().getNumberOfRows(); nRow++) {
        String strValue = beanDefectPlan.getComboWorkProduct().getCell(nRow, 0);
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanDefectPlan.getComboWorkProduct().getCell(nRow, 1);
        out.write("<OPTION");
        out.write(beanDefectPlan.getWorkProduct() == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT><%
    for (int nRow = 0x00; nRow < beanDefectPlan.getComboWorkProduct().getNumberOfRows(); nRow++) {
        String strWPID = beanDefectPlan.getComboWorkProduct().getCell(nRow, 0);
        String strPlannedDefect = beanDefectPlan.getComboWorkProduct().getCell(nRow, 2);
        String strReplannedDefect = beanDefectPlan.getComboWorkProduct().getCell(nRow, 3);
%>
        <INPUT type="hidden" name="hidWPPlannedDefect_<%=strWPID%>" value="<%=strPlannedDefect%>">
        <INPUT type="hidden" name="hidWPReplannedDefect_<%=strWPID%>" value="<%=strReplannedDefect%>"><%
    }
%>
        </TD>
        <TD><B>Document Review</B></TD>
        <TD><INPUT type="text" name="txtDocumentR" size="10" maxlength="8" tabindex="3" onkeypress="javascript:numberAllowed()">
        <INPUT type="hidden" name="hidDocumentReview" value="20"></TD>
        <TD></TD>
        <TD><B>Unit Test</B></TD>
        <TD><INPUT type="text" name="txtUnitT" size="10" maxlength="8" tabindex="6" onkeypress="javascript:numberAllowed()">
        <INPUT type="hidden" name="hidUnitTest" value="10"></TD>
    </TR>
    <TR>
        <TD><B>Module</B></TD>
        <TD><SELECT name="cboModule" size="1" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 130pt" tabindex="2"><%
    for (int nRow = 0x00; nRow < beanDefectPlan.getComboModuleCode().getNumberOfRows(); nRow++) {
        String strModuleID = beanDefectPlan.getComboModuleCode().getCell(nRow, 0);
        int nValue = (strModuleID != null && strModuleID != "") ? Integer.parseInt(strModuleID) : 0;
        String strText = beanDefectPlan.getComboModuleCode().getCell(nRow, 1);
        String strSelected = "";
        if (beanDefectPlan.getModuleCode() == nValue) {
            strSelected = "selected";
        }
%>
            <OPTION value="<%=nValue%>" <%=strSelected%>><%=strText%></OPTION><%
    }
%>
        </SELECT><%
    for (int nRow = 0x00; nRow < beanDefectPlan.getComboModuleCode().getNumberOfRows(); nRow++) {
        String strModuleID = beanDefectPlan.getComboModuleCode().getCell(nRow, 0);
        String strPlannedDefect = beanDefectPlan.getComboModuleCode().getCell(nRow, 2);
        String strReplannedDefect = beanDefectPlan.getComboModuleCode().getCell(nRow, 3);
%>
        <INPUT type="hidden" name="hidPlannedDefect_<%=strModuleID%>" value="<%=strPlannedDefect%>">
        <INPUT type="hidden" name="hidReplannedDefect_<%=strModuleID%>" value="<%=strReplannedDefect%>"><%
    }
%>
        </TD>
        <TD><B>Prototype Review</B></TD>
        <TD><INPUT type="text" name="txtPrototypeR" size="10" maxlength="8" tabindex="4" onkeypress="javascript:numberAllowed()">
        <INPUT type="hidden" name="hidPrototypeReview" value="23"></TD>
        <TD></TD>
        <TD><B>Integration Test</B></TD>
        <TD><INPUT type="text" name="txtIntegrationT" size="10" maxlength="8" tabindex="7" onkeypress="javascript:numberAllowed()">
        <INPUT type="hidden" name="hidIntegrationTest" value="11"></TD>
    </TR>
    <TR>
        <TD></TD>
        <TD></TD>
        <TD><B>Code Review</B></TD>
        <TD><INPUT type="text" name="txtCodeR" size="10" maxlength="8" tabindex="5" onkeypress="javascript:numberAllowed()">
        <INPUT type="hidden" name="hidCodeReview" value="21"></TD>
        <TD></TD>
        <TD><B>System Test</B></TD>
        <TD><INPUT type="text" name="txtSystemT" size="10" maxlength="8" tabindex="8" onkeypress="javascript:numberAllowed()">
        <INPUT type="hidden" name="hidSystemTest" value="12"></TD>
    </TR>
    <TR>
        <TD><INPUT type="button" name="AddNew" value="Add New" size="10" onclick="javascript:doAddNew()" tabindex="11"></TD>
        <TD></TD>
        <TD><B>Others</B></TD>
        <TD><INPUT type="text" name="txtOthers" size="10" maxlength="8" tabindex="10" onkeypress="javascript:numberAllowed()"></TD>
        <TD></TD>
        <TD><B>Acceptance Test</B></TD>
        <TD><INPUT type="text" name="txtAcceptanceT" size="10" maxlength="8" tabindex="9" onkeypress="javascript:numberAllowed()">
        <INPUT type="hidden" name="hidAcceptanceTest" value="13"></TD>
    </TR>
</TABLE>
</DIV>
<BR>
<HR>
<!-- HELP table  -->
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center">
    <TR>
        <TD width="100%" colspan="5"><B><FONT color="#cc0001">Using Information</FONT></B></TD>
    </TR>
    <TR>
        <TD width="100%" colspan="5">This form is used to plan and track and measure the review and testing activities.</TD>
    </TR>
    <TR>
        <TD width="100%" colspan="5"><B><FONT color="#cc0001">Notes</FONT></B></TD>
        <TD width="20%">&nbsp;</TD>
        <TD width="20%">&nbsp;</TD>
        <TD width="20%">&nbsp;</TD>
        <TD width="20%">&nbsp;</TD>
    </TR>
    <TR>
        <TD width="100%" colspan="5">1. Weighted defect is calculated by formula: Weighted defects = Sum(number of defects * defect weight), where defect weight is taken in accordance with the table below:</TD>
    </TR>
    <TR>
        <TD width="20%">&nbsp;&nbsp;&nbsp;&nbsp;Defect severity</TD>
        <TD width="10%">Fatal</TD>
        <TD width="10%">Serious</TD>
        <TD width="10%">Medium</TD>
        <TD width="50%">Cosmetic</TD>
    </TR>
    <TR>
        <TD width="20%">&nbsp;&nbsp;&nbsp;&nbsp;Defect weight</TD>
        <TD width="10%">&nbsp;10</TD>
        <TD width="10%">&nbsp;&nbsp;&nbsp;5</TD>
        <TD width="10%">&nbsp;&nbsp;&nbsp;&nbsp;3</TD>
        <TD width="50%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1</TD>
    </TR>
    <TR>
        <TD width="100%" colspan="5">2. Conduct re-plan defects found in work products, if needed, for cases when the product size is changed.</TD>
    </TR>
    <TR>
        <TD width="100%" colspan="5">3. Conduct re-plan defects found in next work products, if needed, for cases when the deviations are out of the control limit rank.</TD>
    </TR>
</TABLE>
</FORM>
</BODY>
</HTML>