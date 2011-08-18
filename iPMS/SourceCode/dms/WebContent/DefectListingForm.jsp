<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.DefectManagement.*,
            fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    DefectListingBean beanDefectList
            = (DefectListingBean)request.getAttribute("beanDefectList");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
    
    String strSortBy = beanDefectList.getSortBy();
    int nDirection = beanDefectList.getDirection();
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<TITLE><%
    String strTitle = "View All Defects";
    if ("ViewAllDefects".equals(beanUserInfo.getTypeOfView())) {
        strTitle = "DefectListingForm.jsp";
    }
    if ("ViewAllOpenDefects".equals(beanUserInfo.getTypeOfView())) {
        strTitle = "All Open Defects";
    }
    if ("ViewAllLeakageDefects".equals(beanUserInfo.getTypeOfView())) {
        strTitle = "All Leakage Defects";
    }
    out.write(strTitle);
%></TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8;">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<LINK rel="StyleSheet" href="styles/pcal.css" type="text/css">
<SCRIPT language="javascript">
function doSearch() {
    var form = document.frmAllDefectList;
    if (!isValidForm()) {
        return;
    }
    form.numPage.value = 0;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doSort(type) {
    var form = document.frmAllDefectList;
    if (!isValidForm()) {
        return;
    }
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.action = "DMSServlet";
    if (type == "Next") {
        if (Next()) {
            form.submit();
        }
    }
    else if (type == "Prev") {
        if (Prev()) {
            form.submit();
        }
    }
    else {
        // Reverse direction of current sorted column
        if (form.SortBy.value == type) {
            if (form.Direction.value > 0) {
                form.Direction.value = 0;
            }
            else {
                form.Direction.value = 1;
            }
        }
        // New column
        else {
            // Sort descending for date fields
            if ((type == 'FixedDate') || (type == 'DueDate')) {
                form.Direction.value = 0;
            }
            else {
                form.Direction.value = 1;
            }
        }
        form.SortBy.value = type;
        form.numPage.value = 0;
        form.submit();
    }
}

function doQueryListing() {
    var form = document.frmAllDefectList;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function Next() {
    var num;
    num = parseInt(document.forms[0].numPage.value);
    if (num < (document.forms[0].totalPage.value - 1)) {
        num++;
        document.forms[0].numPage.value = num;
        return true;
    }
    return false;
}

function Prev() {
    var num;
    num = parseInt(document.forms[0].numPage.value);
    if (num > 0) {
        num--;
        document.forms[0].numPage.value = num;
        if (num < 1) {
            num = 1;
        }
        return true;
    }
    return false;
}

function numberAllowed() {
    if (window.event.keyCode > 57 || window.event.keyCode < 48) {
        if (window.event.keyCode != 13) {
            window.event.keyCode = 0;
        }
    }
}

function doGoPage() {
    if (!isValidForm()) {
        return;
    }
    if (isNonNegativeInteger(document.forms[0].txtPage.value - 1)) {
        if ((parseInt(document.forms[0].txtPage.value)) > parseInt(document.forms[0].totalPage.value)) {
            alert("Invalid page.");
            return false;
        }
        else {
            document.forms[0].numPage.value = document.forms[0].txtPage.value - 1;
            document.forms[0].hidAction.value = "DM";
            document.forms[0].hidActionDetail.value = "SearchDefect";
            document.forms[0].action = "DMSServlet";
            document.forms[0].submit();
        }
    }
    else {
        alert("Invalid number.");
        document.forms[0].txtPage.focus();
        document.forms[0].txtPage.select();
        return false;
    }
}

function doAddNew() {
    var form = document.frmAllDefectList;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "AddDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doUpdate(defect_id) {
    var form = document.frmAllDefectList;
    if (!isValidForm()) {
        return;
    }
    form.hidUpdateDefect.value = defect_id;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "UpdateDefect";
    form.action = "DMSServlet";
    form.submit();
}
function doBatchUpdate() {
    var form = document.frmAllDefectList;
    if(checkValid()) {
        form.hidAction.value = "DM";
        form.hidActionDetail.value = "BatchUpdateDefect";
        form.action = "DMSServlet";
        form.submit();
    }
    return;
}

function doRefresh() {
    var form = document.frmAllDefectList;
    if (!isValidForm()) {
        return;
    }
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doExport() {
    var form = document.frmAllDefectList;
    if (!isValidForm()) {
        return;
    }
    form.onsubmit = window.open('about:blank','popup','width=780,height=550,top=0,left=0,menubar=yes');
    form.target = "popup";
    form.hidExportAll.value = "true";
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "ExportDefect";
    form.action = "DMSServlet";
    form.submit();
    //Reset to default settings
    form.onsubmit = "";
    form.target = "";
}

function doMoveDefect() {
    var form = document.frmAllDefectList;
//    if (!isValidForm()) {
//        return;
//    }
    if (checkValid()) {
        form.onsubmit = "";
        form.target = "";
        form.hidAction.value = "DM";
        form.hidActionDetail.value = "MoveDefect";
        form.action = "DMSServlet";
        form.submit();
    }
    return;
}

function isValidForm() {
    var count;
    var form = document.forms[0];
    for (count = 0x00; count < form.elements.length; count++) {
        if (!isValidControl(form.elements[count])) {
            return false;
        }
    }
    
    if ((form.txtFromDate.value.length > 0) && (form.txtToDate.value.length > 0)) {
        if (CompareDate(form.txtFromDate , form.txtToDate) > 0) {
            alert("From Date must lower or equal To Date");
            form.txtFromDate.focus();
            return false;
        }
    }
    if ((form.txtFixedFrom.value.length > 0) && (form.txtFixedTo.value.length > 0)) {
        if (CompareDate(form.txtFixedFrom , form.txtFixedTo) > 0) {
            alert("Fixed From date must lower or equal Fixed To date");
            form.txtFixedFrom.focus();
            return false;
        }
    }
    if (!isNonNegativeInteger(form.txtDefectID.value)) {
        alert("Please enter a positive number for DefectID");
        form.txtDefectID.focus();
        return false;
    }
    return true;
}

function isValidControl(control) {
    if ((control.name == "txtFromDate") || (control.name == "txtToDate") ||
        (control.name == "txtFixedFrom") || (control.name == "txtFixedTo")) {
        if (control.value.length <= 0 || isDate(control)) {
            return true;
        }
        else {
            return false;
        }
    }
    return true;
}

function checkValid() {
    var flag = true;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == "selected" && e.type == "checkbox") {
            if (e.checked == 1) {
                flag = false;
            }
        }
    }
    if(flag) {
        alert("Please select defects to do this action!");
        return false;
    }
    return true;
}

function CheckAll(form) {
    for (var i = 0; i < form.elements.length; i++) {
        var e = form.elements[i];
        if (e.name != "allbox") {
            e.checked = form.allbox.checked;
        }
    }
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<DIV>
<P><%
    String strImage = "<img border=0 src=Images/AllDefectListing.gif width=411 height=28>";
    if ("ViewAllOpenDefects".equals(beanUserInfo.getTypeOfView())) {
        strImage = "<img border=0 src=Images/AllOpenDefectListing.gif width=510 height=28>";
    }
    if ("ViewAllLeakageDefects".equals(beanUserInfo.getTypeOfView())) {
        strImage = "<img border=0 src=Images/AllLeakageDefectListing.gif width=537 height=28>";
    }
    out.write(strImage);
%></P>
</DIV>
<FORM method="POST" action="DMSServlet" name="frmAllDefectList">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="userRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="hidTypeOfView" value="<%=beanUserInfo.getTypeOfView()%>">
<INPUT type="hidden" name="SortBy" value="<%=beanDefectList.getSortBy()%>">
<INPUT type="hidden" name="Direction" value="<%=beanDefectList.getDirection()%>">
<INPUT type="hidden" name="numPage" value="<%=beanDefectList.getNumpage()%>">
<INPUT type="hidden" name="totalPage" value="<%=beanDefectList.getTotalpage()%>">
<INPUT type="hidden" name="hidExportAll" value="false">
<INPUT type="hidden" name="hidNewProject" value="">
<INPUT type="hidden" name="hidUpdateDefect" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<TABLE border="0" width="100%">
    <TR>
        <TD align="right"><A href="javascript:doQueryListing()">View DefectListing</A></TD>
    </TR>
</TABLE>
<TABLE border="0" width="100%" cellspacing="1" cellpadding="0" class="TblOut2">
    <TR>
        <TD width="8%"><B>User:</B></TD>
        <TD width="24%"><%=beanUserInfo.getUserName()%></TD>
        <TD width="12%"><B>Login Date:</B></TD>
        <TD width="25%"><%=beanUserInfo.getDateLogin()%></TD>
        <TD width="9%"><B>Project</B></TD>
        <TD width="22%" align="right"><SELECT name="cboProjectList" class="SmallCombo" onchange="javascript:doChangeProject('DM','SearchDefect','<%=beanUserInfo.getTypeOfView()%>');"><%
    int nCurrentProjectID = beanUserInfo.getProjectID();
    int nProjectID;
    for (int i = 0; i < beanComboProject.getListing().getNumberOfRows(); i++) {
        nProjectID = Integer.parseInt(beanComboProject.getListing().getCell(i, 0));
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
        <TD width="22%" align="right"><SELECT name="cboProjectStatus" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="SearchDefect"%>','<%=beanUserInfo.getTypeOfView()%>');"><%
    String strCurrentStatus = beanUserInfo.getCurrentStatus();
    String strStatus;
    for (int i = 0; i < beanComboProject.getStatusList().getNumberOfRows(); i++) {
        strStatus = beanComboProject.getStatusList().getCell(i,0);
        out.write("<OPTION ");
        out.write(strCurrentStatus.equals(strStatus) ? " selected " : " ");
        out.write("value=\"" + strStatus + "\">" + beanComboProject.getStatusList().getCell(i, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>

    </TR>
</TABLE>
<P></P>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="1">
    <COLGROUP>
        <COL width="15%">
        <COL width="18%">
        <COL width="15%">
        <COL width="18%">
        <COL width="15%">
        <COL width="19%" align="right">
    <TR>
		<TD><B>Defect Owner</B></TD>
        <TD><SELECT name="ListingDefectOwner" class="verySmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboDefectOwner().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboDefectOwner().getCell(nRow, 0));
        String strText = beanDefectList.getComboDefectOwner().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(beanDefectList.getDefectOwner().equals(strText) ? " selected " : " ");
        out.write("value=\"" + strText + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD><B>Status</B></TD>
        <TD><SELECT name="ListingStatus" class="verySmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboDefectStatus().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboDefectStatus().getCell(nRow, 0));
        String strText = beanDefectList.getComboDefectStatus().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(Integer.parseInt(beanDefectList.getStatus()) == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD><B>Module Code</B></TD>
        <TD><SELECT class="SmallCombo" name="ListingModuleCode"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboModuleCode().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboModuleCode().getCell(nRow, 0));
        String strText = beanDefectList.getComboModuleCode().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(Integer.parseInt(beanDefectList.getModuleCode()) == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD><B>Assigned To</B></TD>
        <TD><SELECT name="ListingAssignto" class="verySmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboAssignTo().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboAssignTo().getCell(nRow, 0));
        String strText = beanDefectList.getComboAssignTo().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(beanDefectList.getAssignTo().equals(strText) ? " selected " : " ");
        out.write("value=\"" + strText + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD><B>Severity</B></TD>
        <TD><SELECT name="ListingSeverity" class="verySmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboSeverity().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboSeverity().getCell(nRow, 0));
        String strText = beanDefectList.getComboSeverity().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(Integer.parseInt(beanDefectList.getSeverity()) == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD><B>Type</B></TD>
        <TD><SELECT name="ListingType" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboType().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboType().getCell(nRow, 0));
        String strText = beanDefectList.getComboType().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(Integer.parseInt(beanDefectList.getType()) == nValue ? " selected " : " ");
        out.write("value=\"" + nValue +"\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD><B>Created By</B></TD>
        <TD><SELECT name="ListingCreatedBy" class="verySmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboAssignTo().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboAssignTo().getCell(nRow, 0));
        String strText = beanDefectList.getComboAssignTo().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(beanDefectList.getCreatedBy().equals(strText) ? " selected " : " ");
        out.write("value=\"" + strText + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD><B>Reference</B></TD>
        <TD><SELECT name="Reference" class="verySmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboRef().getNumberOfRows(); nRow++) {
        String sValue = (beanDefectList.getComboRef().getCell(nRow, 0));
        String strText = beanDefectList.getComboRef().getCell(nRow, 1);
        out.write("<OPTION ");
        if (beanDefectList.getRef().equals(sValue)) {
            out.write("selected ");
        }
        out.write("value=\"" + sValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD><B>Defect Origin</B></TD>
        <TD><SELECT name="ListingDefectOrigin" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboDefectOrigin().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboDefectOrigin().getCell(nRow, 0));
        String strText = beanDefectList.getComboDefectOrigin().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(Integer.parseInt(beanDefectList.getDefectOrigin()) == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
       <TD><B>Stage Detected</B></TD>
        <TD><SELECT name="ListingStageDetected" class="verySmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboStageDetected().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboStageDetected().getCell(nRow, 0));
        String strText = beanDefectList.getComboStageDetected().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(Integer.parseInt(beanDefectList.getStageDefected()) == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD><B>Stage Injected</B></TD>
        <TD><SELECT name="ListingStageInjected" class="verySmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboStageInjected().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboStageInjected().getCell(nRow, 0));
        String strText = beanDefectList.getComboStageInjected().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(Integer.parseInt(beanDefectList.getStageInjected()) == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD><B>QC Activity</B></TD>
        <TD><SELECT name="ListingQCActivity" class="SmallCombo"><%
    for (int nRow = 0x00; nRow < beanDefectList.getComboQCActivity().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboQCActivity().getCell(nRow, 0));
        String strText = beanDefectList.getComboQCActivity().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(Integer.parseInt(beanDefectList.getQCActivity()) == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
    	<TD><B>Created From </B></TD>
        <TD>
            <INPUT type="text" name="txtFromDate" class="DateBox" value="<%=beanDefectList.getFromDate()%>" maxlength="8">
            <IMG src="Images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].txtFromDate, document.forms[0].txtFromDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <TD><B>Created To </B></TD>
        <TD>
            <INPUT type="text" name="txtToDate" class="DateBox" value="<%=beanDefectList.getToDate()%>" maxlength="8">
            <IMG src="Images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].txtToDate, document.forms[0].txtToDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <TD><B>Work Product</B></TD>
        <TD><SELECT name="ListingWorkProduct" class="SmallCombo"><%
        
    for (int nRow = 0x00; nRow < beanDefectList.getComboWorkProduct().getNumberOfRows(); nRow++) {
    	if (beanDefectList.getComboWorkProduct().getCell(nRow, 0) == null || beanDefectList.getComboWorkProduct().getCell(nRow, 0).trim().isEmpty()) continue;
        int nValue = Integer.parseInt(beanDefectList.getComboWorkProduct().getCell(nRow, 0));
        String strText = beanDefectList.getComboWorkProduct().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(Integer.parseInt(beanDefectList.getWorkProduct()) == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD><B>Fixed From </B></TD>
        <TD>
            <INPUT type="text" name="txtFixedFrom" class="DateBox" value="<%=beanDefectList.getFixedFrom()%>" maxlength="8">
            <IMG src="Images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].txtFixedFrom, document.forms[0].txtFixedFrom, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <TD><B>Fixed To </B></TD>
        <TD>
            <INPUT type="text" name="txtFixedTo" class="DateBox" value="<%=beanDefectList.getFixedTo()%>" maxlength="8">
            <IMG src="Images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].txtFixedTo, document.forms[0].txtFixedTo, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <TD><B>Priority</B></TD>
        <TD><SELECT name="ListingPriority" class="SmallCombo" > <%
    for (int nRow = 0x00; nRow < beanDefectList.getComboPriority().getNumberOfRows(); nRow++) {
        int nValue = Integer.parseInt(beanDefectList.getComboPriority().getCell(nRow, 0));
        String strText = beanDefectList.getComboPriority().getCell(nRow, 1);
        out.write("<OPTION ");
        out.write(Integer.parseInt(beanDefectList.getPriority()) == nValue ? " selected " : " ");
        out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
    } 
%>
		</SELECT></TD>
    </TR>
    <TR>
         <TD><B>DefectID </B></TD>
         <TD><INPUT type="text" name="txtDefectID" value="<%=beanDefectList.getDefectID()%>" maxlength="15" ></TD>	
         <TD><B>Test Case ID</B></TD>
         <TD><INPUT type="text" name="txtLstTestCase" title="Full text search" value="<%=beanDefectList.getTestCase() %>" maxlength="45"></TD>
         <TD><B>Title </B></TD>
         <TD><INPUT type="text" name="txtLstTitle" title="Full text search" class="SmallBox" value="<%=beanDefectList.getTitle()%>" maxlength="20"></TD>
    </TR>
    <TR>
        <TD colspan="5"><FONT color="blue">Date format: mm/dd/yy</FONT></TD>
        <TD><INPUT type="button" name="btnSearchDefect" class="button" onclick="javascript:doSearch()" value="Search"></TD>
    </TR>
</TABLE>
<P></P>
<TABLE border="0" width="100%" cellpadding="0" cellspacing="0">
    <TR><%
    if (Integer.parseInt(beanDefectList.getTotalRecord()) > 0) {
%>
        <TD height="10" valign="bottom"><%
        int nItem = 20;
        int numPage = Integer.parseInt(beanDefectList.getNumpage());
        if (beanDefectList.getDefectList().getNumberOfRows() > 0) {
%>
        <B>Result <FONT color="red" size="-1"><%=numPage * nItem + 1%> - <%=numPage * nItem + beanDefectList.getDefectList().getNumberOfRows()%></FONT> of
        <FONT color="red" size="-1"><%=beanDefectList.getTotalRecord()%></FONT> defects in
        <FONT color="red" size="-1"><%=beanDefectList.getTotalpage()%></FONT> pages</B> <%
        }
        else {
%>
        <B> Result <FONT color="red" size="-1">0 - 0 </FONT> of
        <FONT color="red" size="-1">0</FONT> defects in
        <FONT color="red" size="-1">0</FONT> pages</B> <%
        }
%>
        </TD>
        <TD align="right" height="10" valign="top"><%
        if (Integer.parseInt(beanDefectList.getTotalRecord()) > 20) {
            if (Integer.parseInt(beanDefectList.getNumpage()) > 0) {
%>
        <A class="HeaderMenu" href="javascript:doSort('Prev')">Prev</A> &nbsp;&nbsp; <%
        }
        if (Integer.parseInt(beanDefectList.getNumpage()) + 1 < Integer.parseInt(beanDefectList.getTotalpage())) {
%>
        <A class="HeaderMenu" href="javascript:doSort('Next')">Next</A>&nbsp;&nbsp;&nbsp; <%
        }
%>
        <INPUT type="text" onkeypress="javascript:numberAllowed()" size="4" name="txtPage" maxlength="5" value="<%=Integer.parseInt(beanDefectList.getNumpage()) + 1%>">
        <INPUT type="button" name="GoPage" class="Button" onclick="javascript:doGoPage()" value="Go"></TD><%
        }
    }
    else {%>
        <TD height="10" valign="bottom"><B> Total defects:&nbsp;<FONT color="red" size="-1">0</FONT></B></TD><%
    }
%>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" bgcolor="#000000">
    <COLGROUP>
        <COL width="3%">
        <COL width="8%">
        <COL width="29%">
        <COL width="8%">
        <COL width="8%">
        <COL width="11%">
        <COL width="8%">
        <COL width="6%">
        <COL width="8%">
        <COL width="8%">
        <COL width="8%">
    <TR class="Row0">
        <TD align="center" valign="middle"><INPUT type="checkbox" name="allbox" value="CheckAll" onclick="JavaScript: CheckAll(document.forms[0]);"></TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('DefectID')"><SPAN style="color: white">DefectID</SPAN></A>
<%
    if (strSortBy.equals("DefectID")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DefectID')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DefectID')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('Title')"><SPAN style="color: white">Title</SPAN></A>
<%
    if (strSortBy.equals("Title")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Title')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Title')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('TestCase')"><SPAN style="color: white">Test Case ID</SPAN></A>
<%
    if (strSortBy.equals("TestCase")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('TestCase')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('TestCase')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>

        <TD align="center" valign="middle"><A href="javascript:doSort('Severity')"><SPAN style="color: white">Severity</SPAN></A>
<%
    if (strSortBy.equals("Severity")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Severity')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Severity')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('Priority')"><SPAN style="color: white">Priority</SPAN></A>
<%
    if (strSortBy.equals("Priority")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Priority')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Priority')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('Status')"><SPAN style="color: white">Status</SPAN></A>
<%
    if (strSortBy.equals("Status")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Status')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Status')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
             <TD align="center" valign="middle"><A href="javascript:doSort('DefectOwner')"><SPAN style="color: white">Defect Owner</SPAN></A>
<%
    if (strSortBy.equals("DefectOwner")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DefectOwner')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DefectOwner')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('AssignTo')"><SPAN style="color: white">Assigned To</SPAN></A>
<%
    if (strSortBy.equals("AssignTo")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('AssignTo')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('AssignTo')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('CreatedBy')"><SPAN style="color: white">Created By</SPAN></A>
<%
    if (strSortBy.equals("CreatedBy")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('CreatedBy')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('CreatedBy')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('FixedDate')"><SPAN style="color: white">Fixed Date</SPAN></A>
<%
    if (strSortBy.equals("FixedDate")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('FixedDate')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('FixedDate')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('DueDate')"><SPAN style="color: white">Due Date</SPAN></A>
<%
    if (strSortBy.equals("DueDate")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DueDate')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DueDate')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
    </TR><%
    for (int i = 0; i < beanDefectList.getDefectList().getNumberOfRows(); i++) {
%>
    <TR class="Row<%=(i + 1) % 2 + 1%>">
        <TD align="center"><INPUT type="checkbox" name="selected" value="<%=beanDefectList.getDefectList().getCell(i, 0)%>"></TD>
        <TD align="center"><A href="DMSServlet?hidAction=DM&hidActionDetail=UpdateDefect&hidUpdateDefect=<%=beanDefectList.getDefectList().getCell(i, 0)%>&ProjectID=<%=beanUserInfo.getProjectID()%>"><%=beanDefectList.getDefectList().getCell(i, 0)%></A></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 1)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 11)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 2)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 3)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 4)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 12)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 5)%></TD>
        <TD><%=beanDefectList.getDefectList().getCell(i, 7)%></TD>
        <TD align="center"><%=beanDefectList.getDefectList().getCell(i, 8)%></TD>
        <TD align="center"><%=beanDefectList.getDefectList().getCell(i, 6)%></TD>
    </TR><%
    }
%>
</TABLE>
<BR>
<TABLE border="0" cellspacing="1" cellpadding="0" width="100%"><%
    String strRole = beanUserInfo.getRole();
    String strPosition = beanUserInfo.getPosition();
    String strIsDisabledMove = "disabled";
    String strIsDisabledDelete = "";
    if (strPosition.charAt(2) != '1' && strPosition.charAt(1) != '1') {
        strIsDisabledDelete = "disabled";
    }
    if (strRole.substring(4, 5).equals("1")) {
        strIsDisabledMove = "";
    }
%>
    <TR>
        <TD width="50%" align="left">
        <INPUT type="button" name="AddnewDefect" class="Button" onclick="javascript:doAddNew()" value="Add New">
        <INPUT type="button" name="BatchUpdateDefect" class="Button" onclick="javascript:doBatchUpdate()" value="Batch Update">
        <INPUT type="button" name="Refresh" class="Button" onclick="javascript:doRefresh()" value="Refresh">
        <INPUT type="button" name="ExportDefect" class="Button" onclick="javascript:doExport()" value="Export">
    </TR>
    <TR>
        <TD></TD>
    </TR>
    <TR>
        <TD width="50%" align="left">Move defect(s) to project&nbsp; <SELECT name="cboProject" class="SmallCombo" <%=strIsDisabledMove%>><%
    for (int i = 0; i < beanDefectList.getProjectList().getNumberOfRows();i++) {
%>
            <OPTION value="<%=beanDefectList.getProjectList().getCell(i, 0)%>"><%=beanDefectList.getProjectList().getCell(i, 1)%></OPTION><%
    }
%>
        </SELECT>&nbsp;<INPUT type="button" name="MoveDefect" class="Button" onclick="javascript:doMoveDefect()" value="Move Defect" <%=strIsDisabledMove%>></TD>
    </TR>
</TABLE>
</FORM>
</BODY>
</HTML>