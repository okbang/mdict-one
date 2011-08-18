<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java"
         import="javax.servlet.*,
                 fpt.ncms.bean.*,
                 fpt.ncms.util.StringUtil.*,
                 fpt.ncms.constant.NCMS,
                 fpt.ncms.model.NCModel"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCListBean beanNCList = (NCListBean)session.getAttribute("beanNCList");
    int i = 0;
    int nTotalPage = (beanNCList.getTotal() % NCMS.NUM_PER_PAGE == 0)
            ? Math.round(beanNCList.getTotal() / NCMS.NUM_PER_PAGE)
            : Math.round(beanNCList.getTotal() / NCMS.NUM_PER_PAGE) + 1;
    NCModel modelNC = beanNCList.getNCModel();
    String strOrderBy = beanNCList.getOrderBy();
    int nDirection = beanNCList.getDirection();
%>
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/CallStylesheet.css">

<TITLE>Call Log List System</TITLE>
<SCRIPT language="javascript" src='inc/util.js'></SCRIPT>
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<%@ include file="HeaderCallLog.jsp"%>
<TABLE class="menu" cellpadding="0" cellspacing="0" width="99%" height="20pt">
    <TR>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doAddCallLog()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Add&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doExportNC()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Export&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doReport()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Report&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doAdmin()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Admin&nbsp;&nbsp;</P></TD>
        <TD align="right"><P class="menuitem" style="cursor:hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">Logout&nbsp;&nbsp;&nbsp;</P></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="44%" valign="top" align="left">
        <P><FONT size="6">Listing</FONT>
        <!--IMG src="images/Headers/ncListSystem.gif"--></P>
        </TD>
        <TD width="56%" valign="top">
        <DIV align="right">
        <TABLE border="0" cellspacing="0" cellpadding="0">
            <TR></TR>
            <TR>
                <TD>
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
<FORM method="POST" name="frmCallLogList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<INPUT type="hidden" name="hidMode" value="<%=beanUserInfo.getListingMode()%>">
<INPUT type="hidden" name="hidID" value="">
<INPUT type="hidden" name="hidOrderBy" value="<%=strOrderBy%>">
<INPUT type="hidden" name="hidDirection" value="<%=nDirection%>">
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%">
	<COLGROUP>
    <COL width="15%">
    <COL width="18%">
    <COL width="15%">
    <COL width="21%">
    <COL width="16%">
    <COL width="15%">
    <TBODY>
	<TR>
		<TD><B> Creator </B></TD>
		<TD><SELECT name="cboCreator" class="SmallerCombo"><%
	String strCurrentCreator = modelNC.getCreator();
    for (i = 0; i < beanNCList.getComboCreator().getNumberOfRows(); i++) {
    	String strCreator = beanNCList.getComboCreator().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strCreator.equals(strCurrentCreator) ? strCreator + "\" selected" : strCreator + "\"");
        out.write(">" + beanNCList.getComboCreator().getCell(i, 0) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B> Request to </B></TD>
		<TD><SELECT
            name="cboTypeOfCause"
            class="SmallCombo">
            <%
	int nCurrentTypeOfCause = modelNC.getTypeOfCause();
    for (i = 0; i < beanNCList.getComboTypeOfCause().getNumberOfRows(); i++) {
    	int nTypeOfCause = Integer.parseInt(beanNCList.getComboTypeOfCause().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nTypeOfCause == nCurrentTypeOfCause) ? nTypeOfCause + "\" selected" : nTypeOfCause + "\"");
        out.write(">" + beanNCList.getComboTypeOfCause().getCell(i, 1) + "</OPTION>");
    }%>
        </SELECT></TD>
		<TD><B> Priority </B></TD>
		<TD><SELECT
            name="cboRepeat"
            class="SmallerCombo">
            <%
	int nCurrentPriority = modelNC.getRepeat();
    for (i = 0; i < beanNCList.getComboPriority().getNumberOfRows(); i++) {
    	int nPriority = Integer.parseInt(beanNCList.getComboPriority().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nPriority == nCurrentPriority) ? nPriority + "\" selected" : nPriority + "\"");
        out.write(">" + beanNCList.getComboPriority().getCell(i, 1) + "</OPTION>");
    }%>
        </SELECT></TD>
	</TR>
	<TR>
		<TD><B> Project </B></TD>
		<TD><SELECT name="cboProject" class="SmallerCombo"><%
	String strCurrentProjectID = modelNC.getProjectID();
    for (i = 0; i < beanNCList.getComboProject().getNumberOfRows(); i++) {
    	String strProjectID = beanNCList.getComboProject().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strProjectID.equals(strCurrentProjectID) ? strProjectID + "\" selected" : strProjectID + "\"");
        out.write(">" + strProjectID + "</OPTION>");
    }%>
        </SELECT></TD>
		<TD><B> Implementer </B></TD>
		<TD><SELECT name="cboAssignee" class="SmallCombo"><%
	String strCurrentAssignee = modelNC.getAssignee();
    for (i = 0; i < beanNCList.getComboAssignee().getNumberOfRows(); i++) {
    	String strAssignee = beanNCList.getComboAssignee().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strAssignee.equals(strCurrentAssignee) ? strAssignee + "\" selected" : strAssignee + "\"");
        out.write(">" + strAssignee + "</OPTION>");
    }%></SELECT></TD>
		<TD><B> Type of solution </B></TD>
		<TD><SELECT
            name="cboTypeOfAction"
            class="SmallerCombo">
            <%
	int nCurrentTypeOfAction = modelNC.getTypeOfAction();
    for (i = 0; i < beanNCList.getComboTypeOfAction().getNumberOfRows(); i++) {
    	int nTypeOfAction = Integer.parseInt(beanNCList.getComboTypeOfAction().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nTypeOfAction == nCurrentTypeOfAction) ? nTypeOfAction + "\" selected" : nTypeOfAction + "\"");
        out.write(">" + beanNCList.getComboTypeOfAction().getCell(i, 1) + "</OPTION>");
    }%>
        </SELECT></TD>
	</TR>
	<TR>
		<TD><B> Status </B></TD>
		<TD><SELECT name="cboStatus" class="SmallerCombo"><%
	String strCurrentStatus = Integer.toString(beanNCList.getNCModel().getStatus());
    for (i = 0; i < beanNCList.getComboStatus().getNumberOfRows(); i++) {
    	String strStatus = beanNCList.getComboStatus().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strStatus.equals(strCurrentStatus) ? strStatus + "\" selected" : strStatus + "\"");
        out.write(">" + beanNCList.getComboStatus().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B> Process </B></TD>
		<TD><SELECT name="cboProcess" class="SmallCombo"><%
	int nCurrentProcess = modelNC.getProcess();
    for (i = 0; i < beanNCList.getComboProcess().getNumberOfRows(); i++) {
    	int nProcess = Integer.parseInt(beanNCList.getComboProcess().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nProcess == nCurrentProcess) ? nProcess + "\" selected" : nProcess + "\"");
        out.write(">" + beanNCList.getComboProcess().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
       <TD></TD>
		<TD><INPUT
            type="button"
            name="btnSearchNC"
            class="button"
            onclick="javascript:doSearch()"
            value="Search"></TD>
	</TR>
	<TR>
		<TD><B> Created From </B></TD>
		<TD><INPUT
            type="text"
            name="txtFromDate"
            class="DateBox"
            value='<%=beanNCList.getCurrentFromDate() != null ? beanNCList.getCurrentFromDate() : ""%>'
            maxlength="9"></TD>
		<TD><B> Created To </B></TD>
		<TD><INPUT
            type="text"
            name="txtToDate"
            class="DateBox"
            value='<%=beanNCList.getCurrentToDate() != null ? beanNCList.getCurrentToDate() : ""%>'
            maxlength="9"></TD>
		<TD></TD>
	</TR>
	<TR>
		<TD colspan="5"><FONT color="blue">&nbsp;Date format (dd-MMM-yy)</FONT></TD>
	</TR>
	<TR>
	</TR>
    </TBODY>
</TABLE>
<BR>
<TABLE border="0" cellpadding="1" cellspacing="1" width="100%">
    <TR>
        <!--TD width="15%" valign="bottom">
        </TD-->
        <TD width="40%">
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
    if (beanNCList.getTotal() > NCMS.NUM_PER_PAGE) {
%>
                <TD>Page&nbsp;<INPUT type="text" name="PageNumber" value="<%=beanNCList.getNumPage()%>" size="2" onkeypress="pressKey()">of&nbsp;<%=nTotalPage%>&nbsp;</TD>
                <TD><INPUT type="button" class="button" onclick="javascript:goNavPage()" value="Go"></TD><%
        if (beanNCList.getNumPage() > 1) {
%>
                <TD title="Back page" onclick="goNavBack();" onmouseout="this.style.color='#003399';" onmouseover="this.style.color='#FF0000';" style="cursor: hand"><B>&nbsp;Back</B></TD><%
        }
        if (beanNCList.getNumPage() < nTotalPage) {
%>
                <TD title="Next page" onclick="goNavNext();" onmouseout="this.style.color='#003399';" onmouseover="this.style.color='#FF0000';" style="cursor: hand"><B>&nbsp;Next</B></TD><%
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
                <!--TD><IMG border="0" onclick="doChangeListingMode()" style="cursor:hand" onmouseout="this.src='images/tabs/t_personalView_n.gif'" onmouseover="this.src='images/tabs/t_personalView_p.gif'" src="images/tabs/t_personalView_n.gif" align="bottom"></TD>
                <TD><IMG border="0" src="images/tabs/t_systemView_p.gif" align="bottom"></TD-->
            </TR>
        </TABLE>
        </TD>
    </TR>
</TABLE>
<HR color="#adadd6" SIZE="1" width="100%">
<TABLE bgcolor="#9292CB" border="0" cellpadding="0" cellspacing="1" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber2">
    <COLGROUP>
        <COL width="8%">
        <COL width="20%">
        <COL width="9%">
        <COL width="12%">
        <COL width="11%">
        <COL width="11%">
        <COL width="11%">
        <COL width="11%">
        <COL width="10%">
        <COL width="10%">
    <TBODY>
    <TR class="Header">
        <TD title="Sort by call ID" style="cursor: hand" onclick="javascript:doSort('NCID')">&nbsp;ID
<%
    if (strOrderBy.equals("NCID")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD>&nbsp;Description</TD>
        <TD title="Sort by group" style="cursor: hand" onclick="javascript:doSort('GroupName')">&nbsp;Group
<%
    if (strOrderBy.equals("GroupName")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by project code" style="cursor: hand" onclick="javascript:doSort('ProjectID')">&nbsp;Project
<%
    if (strOrderBy.equals("ProjectID")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by Request To" style="cursor: hand" onclick="javascript:doSort('TypeOfCause')">&nbsp;RequestTo
<%
    if (strOrderBy.equals("TypeOfCause")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by assignee" style="cursor: hand" onclick="javascript:doSort('Assignee')">&nbsp;Assignee
<%
    if (strOrderBy.equals("Assignee")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by created date" style="cursor: hand" onclick="javascript:doSort('CreationDate')">&nbsp;CreateDate
<%
    if (strOrderBy.equals("CreationDate")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by deadline" style="cursor: hand" onclick="javascript:doSort('Deadline')">&nbsp;Deadline
<%
    if (strOrderBy.equals("Deadline")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by priority" style="cursor: hand" onclick="javascript:doSort('Repeat')">&nbsp;Priority
<%
    if (strOrderBy.equals("Repeat")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by status" style="cursor: hand" onclick="javascript:doSort('Status')">&nbsp;Status
<%
    if (strOrderBy.equals("Status")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
    </TR><%
    for (i = 0; i < NCMS.NUM_PER_PAGE; i++) {
        if (i + (beanNCList.getNumPage() - 1) * NCMS.NUM_PER_PAGE >= beanNCList.getTotal()) {
            break;
        }
%>
    <TR class="row<%=i % 2 + 1%>" title="Click to edit" onclick="javascript:frmCallLogList.hidID.value='<%=beanNCList.getNCList().getCell(i, 1)%>';doUpdateCallLog();" onmouseout="this.style.color='#000000';" onmouseover="this.style.color='#FF0000';" style="cursor: hand">
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 1)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 2)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 3)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 4)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 10)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 5)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 7)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 6)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 9)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 0)%></P>
        </TD>
    </TR><%
    }
    if (i == 0) {%>
    <TR><TD colspan="10" class="row1">No record found! </TD></TR><%
    }
%>
    </TBODY>
</TABLE>
&nbsp;
<HR color="#adadd6" SIZE="1" width="100%">
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%">
    <TR>
        <TD width="70%">&nbsp;
        <INPUT type="button" name="btnAddNC" class="button" onclick="doAddCallLog()" value="  Add  ">
        <INPUT type="button" name="btnExport" class="button" onclick="doExportNC()" value="Export">
        <INPUT type="button" name="btnReport" class="button" onclick="doReport()" value="Report">
        </TD>
    </TR>
</TABLE>
</FORM>
<SCRIPT language="javascript">
<%
    out.write("var arrItemGroup = new Array();\n");
	for (i = 0; i < beanNCList.getComboGroup().getNumberOfRows(); i++) {
        out.write("arrItemGroup[" + i + "] = \"" + beanNCList.getComboGroup().getCell(i, 0) + "\";\n");
    }
    out.write("var arrProjectCode = new Array(" + beanNCList.getComboProject().getNumberOfRows() + ");\n");
    out.write("var arrProjectGroupName = new Array(" + beanNCList.getComboProject().getNumberOfRows() + ");\n");
    for (i = 0; i < beanNCList.getComboProject().getNumberOfRows(); i++) {
	    out.write("arrProjectCode[" + i + "] = \"" + beanNCList.getComboProject().getCell(i, 0) + "\";\n");
        out.write("arrProjectGroupName[" + i + "] = \"" + beanNCList.getComboProject().getCell(i, 1) + "\";\n");
    }

%>
function doAdmin(){
	<%if (NCMS.ROLE_PQA.equals(beanUserInfo.getRoleName())){%>
    	frmCallLogList.hidAction.value = "<%=NCMS.CONSTANT_LIST%>";
	    frmCallLogList.action = "NcmsServlet";
    	frmCallLogList.submit();
    <%} else {%>
    	alert("You are not Admin");
    <%}%>
}

function doAddCallLog() {
    frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_ADD%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function doUpdateCallLog() {
	frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_UPDATE%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function doCreateReport() {
    frmCallLogList.hidAction.value = "<%=NCMS.NC_REPORT%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function doSearch() {
    if (!isValidForm()) {
        return;
    }
    frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function doSort(SortBy) {
    if (!isValidForm()) {
        return;
    }
    // Re-sort direction
    if (frmCallLogList.hidOrderBy.value == SortBy) {
        if (frmCallLogList.hidDirection.value > 0) {
            frmCallLogList.hidDirection.value = 0;
        }
        else {
            frmCallLogList.hidDirection.value = 1;
        }
    }
    // New column
    else {
        frmCallLogList.hidDirection.value = 1;
    }
    
    frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
    frmCallLogList.hidOrderBy.value = SortBy;
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function doExportNC() {
    if (!isValidForm()) {
        return;
    }
    var qrStr = "NcmsServlet?hidAction=<%=NCMS.NC_EXPORT%>";
    var sFeature = "width=780 height=550 top=0 left=0 menubar=yes";
    var exp_wd = window.open(qrStr, "NCExport", sFeature);
}

function doReport() {
    clearInvalidControls();
    frmCallLogList.hidAction.value = "<%=NCMS.NC_REPORT%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function doChangeListingMode() {
    clearInvalidControls();
    frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
    frmCallLogList.hidMode.value = "<%=NCMS.PERSONAL_MODE%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function goNavNext() {
    if (!isValidForm()) {
        return;
    }
    frmCallLogList.PageNumber.value = <%=beanNCList.getNumPage() + 1%>;
    frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function goNavBack() {
    if (!isValidForm()) {
        return;
    }
    frmCallLogList.PageNumber.value = <%=beanNCList.getNumPage() - 1%>;
    frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function goNavPage() {
    if (!isValidForm()) {
        return;
    }
    if (isNaN(frmCallLogList.PageNumber.value)) {
        alert("Invalid page number.");
        return false;
    }
    if ((frmCallLogList.PageNumber.value > <%=nTotalPage%>) || (frmCallLogList.PageNumber.value < 1)) {
        alert("Page number out of range.");
        return false;
    }
    frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function doLogOut() {
    frmCallLogList.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function pressKey() {
    if (window.event.keyCode == 13) {
        goNavPage();
    }
    if ((window.event.keyCode < 48) || (window.event.keyCode > 57)) {
        window.event.keyCode = 0;
    }
}

function isValidForm() {
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

// Global scripts
window.name="myMainWindow";
//selectChangeGroup('<%=beanNCList.getNCModel().getProjectID()%>');
</SCRIPT>
</BODY>
</HTML>