<%@ page language="java" import="javax.servlet.*, fpt.ncms.bean.*,
        fpt.ncms.util.StringUtil.*, fpt.ncms.constant.NCMS,
        java.util.ArrayList, java.util.StringTokenizer"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCListPersonalBean beanNCListPersonal =
            (NCListPersonalBean)session.getAttribute("beanNCListPersonal");
    int i = 0;
    int nTotalPage = (beanNCListPersonal.getTotal() % NCMS.NUM_PER_PAGE == 0)
            ? Math.round(beanNCListPersonal.getTotal() / NCMS.NUM_PER_PAGE)
            : Math.round(beanNCListPersonal.getTotal() / NCMS.NUM_PER_PAGE) + 1;
    int nNumFields = beanNCListPersonal.getNumFields() - 1;
    StringTokenizer sk = new StringTokenizer(beanNCListPersonal.getFields(), ",");
    ArrayList aFieldName = new ArrayList(nNumFields);
    while (sk.hasMoreTokens()) {
        aFieldName.add(sk.nextToken());
    }
    
    String strOrderBy = beanNCListPersonal.getOrderBy();
    int nDirection = beanNCListPersonal.getDirection();
    int nRepeatIndex = -1;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
<TITLE>NC List System</TITLE>
<SCRIPT language="javascript" src='inc/util.js'></SCRIPT>
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
<SCRIPT language="javascript">
<%
    out.write("var arrItemGroup = new Array();\n");
	for (i = 0; i < beanNCListPersonal.getComboGroup().getNumberOfRows(); i++) {
        out.write("arrItemGroup[" + i + "] = \"" + beanNCListPersonal.getComboGroup().getCell(i, 0) + "\";\n");
    }
    out.write("var arrProjectCode = new Array(" + beanNCListPersonal.getComboProject().getNumberOfRows() + ");\n");
    out.write("var arrProjectGroupName = new Array(" + beanNCListPersonal.getComboProject().getNumberOfRows() + ");\n");
    for (i = 0; i < beanNCListPersonal.getComboProject().getNumberOfRows(); i++) {
	    out.write("arrProjectCode[" + i + "] = \"" + beanNCListPersonal.getComboProject().getCell(i, 0) + "\";\n");
        out.write("arrProjectGroupName[" + i + "] = \"" + beanNCListPersonal.getComboProject().getCell(i, 1) + "\";\n");
    }
%>
function doAdmin(){
	<%if (NCMS.ROLE_PQA.equals(beanUserInfo.getRoleName())){%>
    	document.forms[0].hidAction.value = "<%=NCMS.CONSTANT_LIST%>";
	    document.forms[0].action = "NcmsServlet";
    	document.forms[0].submit();
    <%} else {%>
    	alert("You are not Admin");
    <%}%>
}

function doAddNC() {
    frmNCList.hidAction.value = "<%=NCMS.NC_ADD%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function doUpdateNC() {
    frmNCList.hidAction.value = "<%=NCMS.NC_UPDATE%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function doSearch() {
    if (!formValidate()) {
        return;
    }
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function doCreateReport() {
    frmNCList.hidAction.value = "<%=NCMS.NC_REPORT%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function doChangeView() {
    if (!formValidate()) {
        return;
    }
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function doSort(SortBy) {
    if (!formValidate()) {
        return;
    }
    // Re-sort direction
    if (frmNCList.hidPerOrderBy.value == SortBy) {
        if (frmNCList.hidPerDirection.value > 0) {
            frmNCList.hidPerDirection.value = 0;
        }
        else {
            frmNCList.hidPerDirection.value = 1;
        }
    }
    // New column
    else {
        frmNCList.hidPerDirection.value = 1;
    }

    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.hidPerOrderBy.value = SortBy;
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function doExportNC() {
    var qrStr = "NcmsServlet?hidAction=<%=NCMS.NC_EXPORT%>";
    var sFeature = "width=780 height=550 top=0 left=0 menubar=yes";
    var exp_wd = window.open(qrStr, "NCExport", sFeature);
}
function doLogOut() {
    frmNCList.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function doChangeListingMode() {
    clearInvalidControls();
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.hidMode.value = "<%=NCMS.SYSTEM_MODE%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function doViewList() {
    frmNCList.hidAction.value = "<%=NCMS.VIEW_LIST%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function goNavNext() {
    if (!formValidate()) {
        return;
    }
    frmNCList.PageNumber.value = <%=beanNCListPersonal.getNumPage() + 1%>;
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function goNavBack() {
    if (!formValidate()) {
        return;
    }
    frmNCList.PageNumber.value = <%=beanNCListPersonal.getNumPage() - 1%>;
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function goNavPage() {
    if (!formValidate()) {
        return;
    }
    if (isNaN(frmNCList.PageNumber.value)) {
        alert("Invalid page number.");
        return false;
    }
    if ((frmNCList.PageNumber.value > <%=nTotalPage%>) || (frmNCList.PageNumber.value < 1)) {
        alert("Page number out of range.");
        return false;
    }
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}

function formValidate() {
    if (!validDates(document.forms[0], new Array('txtFromDate', 'txtToDate'),
        true, "This date is incorrect"))
    {
        return false;
    }
    strStartDate = convertToSimpleDate(document.forms[0].txtFromDate.value);
    strEndDate = convertToSimpleDate(document.forms[0].txtToDate.value);
    if (!compareDate(strStartDate, strEndDate)) {
        alert("From Date must be lower than To Date!");
        document.forms[0].txtFromDate.focus();
        return false;
    }
    return true;
}

function clearInvalidControls() {
    if (!isDate(document.forms[0].txtFromDate.value)) {
        document.forms[0].txtFromDate.value = "";
    }
    if (!isDate(document.forms[0].txtToDate.value)) {
        document.forms[0].txtToDate.value = "";
    }
    strStartDate = convertToSimpleDate(document.forms[0].txtFromDate.value);
    strEndDate = convertToSimpleDate(document.forms[0].txtToDate.value);
    if (!compareDate(strStartDate, strEndDate)) {
        txtToDate.value = "";
    }
}

function pressKey() {
    if (window.event.keyCode == 13) {
        goNavPage();
    }
    if ((window.event.keyCode < 48) || (window.event.keyCode > 57)) {
        window.event.keyCode = 0;
    }
}
function doLogOut() {
    frmNCList.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
</SCRIPT>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<%@ include file="Header.jsp"%>
<TABLE class="menu" cellpadding="0" cellspacing="0" width="100%" height="20pt">
    <TR>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doAddNC()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Add&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doExportNC()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Export&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doCreateReport()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Report&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doAdmin()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Admin&nbsp;&nbsp;</P></TD>
        <TD align="right"><P class="menuitem" style="cursor:hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">Logout&nbsp;&nbsp;</P></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="44%" valign="top" align="left">
        <P><IMG src="images/Headers/ncListPersonal.gif"></P>
        </TD>
        <TD width="56%" valign="top">
        <DIV align="right">
        <TABLE width="199" border="0" cellspacing="0" cellpadding="0" height="72">
            <TR>
                <TD background="images/Headers/logonName.gif">
                <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
                    <TR>
                        <TD width="75">&nbsp;</TD>
                        <TD width="70"><B>User: </B></TD>
                        <TD width="54"><B><%=beanUserInfo.getLoginName()%></B></TD>
                    </TR>
                    <TR>
                        <TD width="75">&nbsp;</TD>
                        <TD width="70"><B>Role: </B></TD>
                        <TD width="54"><B><%=beanUserInfo.getRoleName()%></B></TD>
                    </TR>
                </TABLE>
                </TD>
            </TR>
        </TABLE>
        </DIV>
        </TD>
    </TR>
</TABLE><%
    if (!"".equals(beanUserInfo.getMessage())){%>
<FONT face="Verdana" color="red"><%=beanUserInfo.getMessage()%></FONT><%
        beanUserInfo.setMessage("");
    }
%>
<FORM method="POST" name="frmNCList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<INPUT type="hidden" name="hidMode" value="<%=beanUserInfo.getListingMode()%>">
<INPUT type="hidden" name="hidID" value="">
<INPUT type="hidden" name="hidPerOrderBy" value="<%=strOrderBy%>">
<INPUT type="hidden" name="hidPerDirection" value="<%=nDirection%>">
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
	<COLGROUP>
    <COL width="15%">
    <COL width="18%">
    <COL width="15%">
    <COL width="21%">
    <COL width="16%">
    <COL width="15%">
	<TR>
		<TD><B> Creator </B></TD>
		<TD><SELECT name="cboCreator" class="SmallerCombo"><%
	String strCurrentCreator = beanNCListPersonal.getNCModel().getCreator();
    for (i = 0; i < beanNCListPersonal.getComboCreator().getNumberOfRows(); i++) {
    	String strCreator = beanNCListPersonal.getComboCreator().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strCreator.equals(strCurrentCreator) ? strCreator + "\" selected" : strCreator + "\"");
        out.write(">" + beanNCListPersonal.getComboCreator().getCell(i, 0) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B> Group/Department </B></TD>
		<TD><SELECT name="cboGroup" class="SmallCombo" onchange="selectChangeGroup('<%=beanNCListPersonal.getNCModel().getProjectID()%>', true);"><%
	String strCurrentGroup = beanNCListPersonal.getNCModel().getGroupName();
    for (i = 0; i < beanNCListPersonal.getComboGroup().getNumberOfRows(); i++) {
    	String strGroup = beanNCListPersonal.getComboGroup().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strGroup.equals(strCurrentGroup) ? strGroup + "\" selected" : strGroup + "\"");
        out.write(">" + beanNCListPersonal.getComboGroup().getCell(i, 0) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B>&nbsp; KPA </B></TD>
		<TD><SELECT name="cboKPA" class="SmallerCombo"><%
	int nCurrentKPA = beanNCListPersonal.getNCModel().getKPA();
    for (i = 0; i < beanNCListPersonal.getComboKPA().getNumberOfRows(); i++) {
    	int nKPA = Integer.parseInt(beanNCListPersonal.getComboKPA().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nKPA == nCurrentKPA) ? nKPA + "\" selected" : nKPA + "\"");
        out.write(">" + beanNCListPersonal.getComboKPA().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
	</TR>
	<TR>
		<TD><B> Assignee </B></TD>
		<TD><SELECT name="cboAssignee" class="SmallerCombo"><%
	String strCurrentAssignee = beanNCListPersonal.getNCModel().getAssignee();
    for (i = 0; i < beanNCListPersonal.getComboAssignee().getNumberOfRows(); i++) {
    	String strAssignee = beanNCListPersonal.getComboAssignee().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strAssignee.equals(strCurrentAssignee) ? strAssignee + "\" selected" : strAssignee + "\"");
        out.write(">" + beanNCListPersonal.getComboAssignee().getCell(i, 0) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B> Project </B></TD>
		<TD><SELECT name="cboProject" class="SmallCombo"></SELECT></TD>
		<TD><B>&nbsp; ISO Cause </B></TD>
		<TD><SELECT name="cboISOCause" class="SmallerCombo"><%
	int nCurrentISOClause = beanNCListPersonal.getNCModel().getISOClause();
    for (i = 0; i < beanNCListPersonal.getComboISOClause().getNumberOfRows(); i++) {
    	int nISOClause = Integer.parseInt(beanNCListPersonal.getComboISOClause().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nISOClause == nCurrentISOClause) ? nISOClause + "\" selected" : nISOClause + "\"");
        out.write(">" + beanNCListPersonal.getComboISOClause().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
	</TR>
	<TR>
		<TD><B> Reviewer </B></TD>
		<TD><SELECT name="cboReviewer" class="SmallerCombo"><%
	String strCurrentReviewer = beanNCListPersonal.getNCModel().getReviewer();
    for (i = 0; i < beanNCListPersonal.getComboReviewer().getNumberOfRows(); i++) {
    	String strReviewer = beanNCListPersonal.getComboReviewer().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strReviewer.equals(strCurrentReviewer) ? strReviewer + "\" selected" : strReviewer + "\"");
        out.write(">" + beanNCListPersonal.getComboReviewer().getCell(i, 0) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B> Process </B></TD>
		<TD><SELECT name="cboProcess" class="SmallCombo"><%
	int nCurrentProcess = beanNCListPersonal.getNCModel().getProcess();
    for (i = 0; i < beanNCListPersonal.getComboProcess().getNumberOfRows(); i++) {
    	int nProcess = Integer.parseInt(beanNCListPersonal.getComboProcess().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nProcess == nCurrentProcess) ? nProcess + "\" selected" : nProcess + "\"");
        out.write(">" + beanNCListPersonal.getComboProcess().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B>&nbsp; Type of Cause </B></TD>
		<TD><SELECT name="cboTypeOfCause" class="SmallerCombo"><%
	int nCurrentTypeOfCause = beanNCListPersonal.getNCModel().getTypeOfCause();
	for (i = 0; i < beanNCListPersonal.getComboTypeOfCause().getNumberOfRows(); i++) {
    	int nTypeOfCause = Integer.parseInt(beanNCListPersonal.getComboTypeOfCause().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nTypeOfCause == nCurrentTypeOfCause) ? nTypeOfCause + "\" selected" : nTypeOfCause + "\"");
        out.write(">" + beanNCListPersonal.getComboTypeOfCause().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
	</TR>
	<TR>
		<TD><B> Level </B></TD>
		<TD><SELECT name="cboLevel" class="SmallerCombo"><%
	int nCurrentLevel = beanNCListPersonal.getNCModel().getNCLevel();
    for (i = 0; i < beanNCListPersonal.getComboLevel().getNumberOfRows(); i++) {
    	int nLevel = Integer.parseInt(beanNCListPersonal.getComboLevel().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nLevel == nCurrentLevel) ? nLevel + "\" selected" : nLevel + "\"");
        out.write(">" + beanNCListPersonal.getComboLevel().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B> Detected by </B></TD>
		<TD><SELECT name="cboDetectedBy" class="SmallCombo"><%
	int nCurrentDetectedBy = beanNCListPersonal.getNCModel().getDetectedBy();
    for (i = 0; i < beanNCListPersonal.getComboDetectedBy().getNumberOfRows(); i++) {
    	int nDetectedBy = Integer.parseInt(beanNCListPersonal.getComboDetectedBy().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nDetectedBy == nCurrentDetectedBy) ? nDetectedBy + "\" selected" : nDetectedBy + "\"");
        out.write(">" + beanNCListPersonal.getComboDetectedBy().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B>&nbsp; Type of action </B></TD>
		<TD><SELECT name="cboTypeOfAction" class="SmallerCombo"><%
	int nCurrentTypeOfAction = beanNCListPersonal.getNCModel().getTypeOfAction();
    for (i = 0; i < beanNCListPersonal.getComboTypeOfAction().getNumberOfRows(); i++) {
    	int nTypeOfAction = Integer.parseInt(beanNCListPersonal.getComboTypeOfAction().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nTypeOfAction == nCurrentTypeOfAction) ? nTypeOfAction + "\" selected" : nTypeOfAction + "\"");
        out.write(">" + beanNCListPersonal.getComboTypeOfAction().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
	</TR>
	<TR>
		<TD><B> Type of NC </B></TD>
		<TD><SELECT name="cboTypeOfNC" class="SmallerCombo"><%
	int nCurrentTypeOfNC = beanNCListPersonal.getNCModel().getNCType();
    for (i = 0; i < beanNCListPersonal.getComboTypeOfNC().getNumberOfRows(); i++) {
    	int nTypeOfNC = Integer.parseInt(beanNCListPersonal.getComboTypeOfNC().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nTypeOfNC == nCurrentTypeOfNC) ? nTypeOfNC + "\" selected" : nTypeOfNC + "\"");
        out.write(">" + beanNCListPersonal.getComboTypeOfNC().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B> Status </B></TD>
		<TD><SELECT name="cboStatus" class="SmallCombo"><%
	int nCurrentStatus = beanNCListPersonal.getNCModel().getStatus();
    for (i = 0; i < beanNCListPersonal.getComboStatus().getNumberOfRows(); i++) {
    	int nStatus = Integer.parseInt(beanNCListPersonal.getComboStatus().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nStatus == nCurrentStatus) ? nStatus + "\" selected" : nStatus + "\"");
        out.write(">" + beanNCListPersonal.getComboStatus().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
	</TR>
	<TR>
		<TD><B>From Date </B></TD>
        <TD><INPUT type="text" name="txtFromDate" class="DateBox" value="<%=beanNCListPersonal.getCurrentFromDate() != null ? beanNCListPersonal.getCurrentFromDate() : ""%>"  maxlength="9"></TD>
        <TD><B>To Date </B></TD>
        <TD><INPUT type="text" name="txtToDate" class="DateBox" value="<%=beanNCListPersonal.getCurrentToDate() != null ? beanNCListPersonal.getCurrentToDate() : ""%>" maxlength="9"></TD>
        <TD></TD>
        <TD><INPUT type="button" name="btnSearchNC" class="button" onclick="javascript:doSearch()" value="Search"></TD>
	</TR>
	<TR>
		<TD colspan="5"><FONT color="blue">Date format (dd-MMM-yy)</FONT></TD>
	</TR>
</TABLE>
<TABLE border="0" cellpadding="1" cellspacing="1" width="100%">
    <TR>
        <TD width="15%" valign="bottom"><B>View:&nbsp; </B> <SELECT name="cboView" onchange="doChangeView()" style="width: 100pt"><%
    for (i = 0; i < beanNCListPersonal.getComboView().getNumberOfRows(); i++) {
        out.write("<OPTION value=\"" + beanNCListPersonal.getComboView().getCell(i, 0));
        out.write(beanNCListPersonal.getComboView().getCell(i, 0).equals(beanNCListPersonal.getViewID()) ? "\" selected" : "\"");
        out.write(">" + beanNCListPersonal.getComboView().getCell(i, 1) + "</OPTION>");
    }
	%></SELECT></TD>
        <TD width="10%" align="left"><IMG border="0" onclick ="doViewList()" style="cursor:hand" onmouseout="this.src='images/Buttons/b_define_n.gif'" onmouseover="this.src='images/Buttons/b_define_p.gif'" src="images/Buttons/b_define_n.gif"></TD>
        <TD align="right" width="60%">
        <TABLE border="0" cellpadding="0" cellspacing="0" width="125">
            <TR>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="25"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="25"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="30"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="40"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="25"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="25"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="112"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="105"></TD>
            </TR>
            <TR><%
    if (beanNCListPersonal.getTotal() > NCMS.NUM_PER_PAGE) {
%>
                <TD><%
        if (beanNCListPersonal.getNumPage() > 1) {
%>
                <IMG border="0" src="images/Buttons/b_previous_n.gif" onmouseout="this.src='images/Buttons/b_previous_n.gif'" onmouseover="this.src='images/Buttons/b_previous_p.gif'" onclick="javascript:goNavBack()" align="bottom"></TD><%
        }
%>
                <TD><IMG border="0" src="images/Buttons/b_go_n.gif" onmouseout="this.src='images/Buttons/b_go_n.gif'" onmouseover="this.src='images/Buttons/b_go_p.gif'" onclick="javascript:goNavPage()" align="bottom"></TD>
                <TD><INPUT type="text" name="PageNumber" value="<%=beanNCListPersonal.getNumPage()%>" size="2" onkeypress="pressKey()"></TD>
                <TD>of&nbsp;<%=nTotalPage%></TD><%
        if (beanNCListPersonal.getNumPage() < nTotalPage) {
%>
                <TD>&nbsp;<IMG border="0" src="images/Buttons/b_next_n.gif" onmouseout="this.src='images/Buttons/b_next_n.gif'" onmouseover="this.src='images/Buttons/b_next_p.gif'" onclick="javascript:goNavNext()" align="bottom"></TD><%
        }
        else {
%>
                <TD></TD><%
        }
    }
    else {
%>
                <TD></TD>
                <TD></TD>
                <TD></TD>
                <TD></TD>
                <TD></TD><%
    }
%>
                <TD></TD>
                <TD><IMG border="0" src="images/tabs/t_personalView_p.gif" align="bottom"></TD>
                <TD><IMG border="0" onclick ="doChangeListingMode()" style="cursor:hand" onmouseout="this.src='images/tabs/t_systemView_n.gif'" onmouseover="this.src='images/tabs/t_systemView_p.gif'" src="images/tabs/t_systemView_n.gif" align="bottom"></TD>
            </TR></TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE bgcolor="#9292CB" border="0" cellpadding="0" cellspacing="1" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber2">
    <COLGROUP><%
    for (i = 0; i < nNumFields; i++) {
%>
        <COL width="<%=Math.round(97 / nNumFields)%>%">
<%
    }
%>
    <TR class="Header">
        <%
    for (i = 0; i < nNumFields; i++) {
    	if ("Description".equals(aFieldName.get(i).toString()) ||
    			"Cause".equals(aFieldName.get(i).toString()) ||
    			"Impact".equals(aFieldName.get(i).toString()) ||
    			"Note".equals(aFieldName.get(i).toString()) ||
    			"CPAction".equals(aFieldName.get(i).toString())) { %>
    	<TD>&nbsp;<%=aFieldName.get(i).toString()%></TD><%
    	}
    	else { %>
        <TD title="Sort by <%=aFieldName.get(i).toString()%>" style="cursor: hand" onclick="javascript:doSort('<%=aFieldName.get(i).toString()%>')">&nbsp;<%=aFieldName.get(i).toString()%><%
            // Sort by a column
            if (strOrderBy.equals(aFieldName.get(i).toString())) {
                if (nDirection > 0) {
                    out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
                }
                else {
                    out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
                }
            }%>
        </TD>
<%
            // Remember the index of Repeat column
            if ("Repeat".equals(aFieldName.get(i).toString())) {
                nRepeatIndex = i;
            }
        }
    }
%>
    </TR><%
    for (i = 0; i < NCMS.NUM_PER_PAGE; i++) {
        if (i + (beanNCListPersonal.getNumPage() - 1) * NCMS.NUM_PER_PAGE >= beanNCListPersonal.getTotal()) {
            break;
        }
%>
    <TR class="row<%=i % 2 + 1%>" title="Click to edit" onclick="javascript:frmNCList.hidID.value='<%=beanNCListPersonal.getNCList().getCell(i, aFieldName.indexOf("NCID"))%>';doUpdateNC();" onmouseout="this.style.color='#000000';" onmouseover="this.style.color='#FF0000';" style="cursor: hand"><%
        for (int j = 0; j < nNumFields; j++) {
            if (j == nRepeatIndex) {
    	        if ("0".equals(beanNCListPersonal.getNCList().getCell(i, j))) {
%>
        <TD><P style="MARGIN: 1px 3px 1px 3px">No</P></TD>
<%
    	        }
    	        else if ("1".equals(beanNCListPersonal.getNCList().getCell(i, j))) {
%>
        <TD><P style="MARGIN: 1px 3px 1px 3px">Yes</P></TD>
<%
    	        }
    	        else {
%>
        <TD><P style="MARGIN: 1px 3px 1px 3px"></P></TD>
<%
    	        }
    	    }
    	    else {
            %>
        <TD><P style="MARGIN: 1px 3px 1px 3px"><%=beanNCListPersonal.getNCList().getCell(i, j)%></P></TD><%
            }
        }
%>
    </TR><%
    }
    if (i == 0) {%>
    <TR><TD colspan="<%=nNumFields + 1%>" class="row1">No record found! </TD></TR><%
    }
%>
</TABLE>
</FORM>
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
    <TR>
        <TD width="70%">&nbsp;<IMG border="0" onclick="doAddNC()" style="cursor: hand" onmouseout="this.src='images/Buttons/b_add_n.gif'" onmouseover="this.src='images/Buttons/b_add_p.gif'" style="cursor:hand" src="images/Buttons/b_add_n.gif" name="imgAdd">
        <IMG border="0" style="cursor: hand" onmouseout="this.src='images/Buttons/b_export_n.gif'" onmouseover="this.src='images/Buttons/b_export_p.gif'" style="cursor:hand" onclick="doExportNC()" src="images/Buttons/b_export_n.gif" name="imgExport">
        <IMG border="0" onclick="doCreateReport()" style="cursor: hand" onmouseout="this.src='images/Buttons/b_report_n.gif'" onmouseover="this.src='images/Buttons/b_report_p.gif'" style="cursor:hand" src="images/Buttons/b_report_n.gif" name="imgReport"></TD>
    </TR>
</TABLE>
<SCRIPT>
selectChangeGroup('<%=beanNCListPersonal.getNCModel().getProjectID()%>', true);
</SCRIPT>
</BODY>
</HTML>