<%@ page language="java"
         import="java.util.ArrayList,
                 java.util.StringTokenizer,
                 javax.servlet.*,
                 fpt.ncms.bean.*,
                 fpt.ncms.util.StringUtil.*,
                 fpt.ncms.constant.NCMS,
                 fpt.ncms.model.NCModel"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCListPersonalBean beanNCListPersonal =
            (NCListPersonalBean)session.getAttribute("beanNCListPersonal");
    NCModel modelNC = beanNCListPersonal.getNCModel();
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
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/CallStylesheet.css">
<TITLE>Call Log List Personal</TITLE>
<SCRIPT language="javascript" src='inc/util.js'></SCRIPT>
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<%@ include file="HeaderCallLog.jsp"%>
<TABLE class="menu" cellpadding="0" cellspacing="0" width="99%" height="20pt">
    <TR>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doAddCallLog()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Add&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doExportNC()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Export&nbsp;&nbsp;</P></TD>
        <TD align="right"><P class="menuitem" style="cursor:hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">Logout&nbsp;&nbsp;</P></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="44%" valign="top" align="left">
        <P><FONT size="6">Customize Listing</FONT>
        <!--IMG src="images/Headers/ncListPersonal.gif"--></P>
        </TD>
        <TD width="56%" valign="top">
        <DIV align="right">
        <TABLE width="199" border="0" cellspacing="0" cellpadding="0">
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
	String strCurrentCreator = modelNC.getCreator();
    for (i = 0; i < beanNCListPersonal.getComboCreator().getNumberOfRows(); i++) {
    	String strCreator = beanNCListPersonal.getComboCreator().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strCreator.equals(strCurrentCreator) ? strCreator + "\" selected" : strCreator + "\"");
        out.write(">" + beanNCListPersonal.getComboCreator().getCell(i, 0) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B> Type of Solution </B></TD>
		<TD><SELECT
            name="cboTypeOfAction"
            class="SmallCombo">
            <%
	int nCurrentTypeOfAction = modelNC.getTypeOfAction();
    for (i = 0; i < beanNCListPersonal.getComboTypeOfAction().getNumberOfRows(); i++) {
    	int nTypeOfAction = Integer.parseInt(beanNCListPersonal.getComboTypeOfAction().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nTypeOfAction == nCurrentTypeOfAction) ? nTypeOfAction + "\" selected" : nTypeOfAction + "\"");
        out.write(">" + beanNCListPersonal.getComboTypeOfAction().getCell(i, 1) + "</OPTION>");
    }%>
        </SELECT></TD>
		<TD><B>&nbsp; Group </B></TD>
		<TD><SELECT
            name="cboGroup"
            class="SmallerCombo"
            onchange="selectChangeGroup('<%=modelNC.getGroupName()%>');">
            <%
    for (i = 0; i < beanNCListPersonal.getComboGroup().getNumberOfRows(); i++) {
    	String strGroup = beanNCListPersonal.getComboGroup().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strGroup.equals(modelNC.getGroupName()) ? strGroup + "\" selected" : strGroup + "\"");
        out.write(">" + strGroup + "</OPTION>");
    }%>
        </SELECT></TD>
	</TR>
	<TR>
		<TD><B> Assignee </B></TD>
		<TD><SELECT name="cboAssignee" class="SmallerCombo"><%
	String strCurrentAssignee = modelNC.getAssignee();
    for (i = 0; i < beanNCListPersonal.getComboAssignee().getNumberOfRows(); i++) {
    	String strAssignee = beanNCListPersonal.getComboAssignee().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strAssignee.equals(strCurrentAssignee) ? strAssignee + "\" selected" : strAssignee + "\"");
        out.write(">" + strAssignee + "</OPTION>");
    }%></SELECT></TD>
		<TD><B> Request To </B></TD>
		<TD><SELECT
            name="cboTypeOfCause"
            class="SmallCombo">
            <%
	int nCurrentTypeOfCause = modelNC.getTypeOfCause();
    for (i = 0; i < beanNCListPersonal.getComboTypeOfCause().getNumberOfRows(); i++) {
    	int nTypeOfCause = Integer.parseInt(beanNCListPersonal.getComboTypeOfCause().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nTypeOfCause == nCurrentTypeOfCause) ? nTypeOfCause + "\" selected" : nTypeOfCause + "\"");
        out.write(">" + beanNCListPersonal.getComboTypeOfCause().getCell(i, 1) + "</OPTION>");
    }%>
        </SELECT></TD>
		<TD><B>&nbsp; Project </B></TD>
		<TD><SELECT
            name="cboProject"
            class="SmallerCombo">
        </SELECT></TD>
	</TR>
	<TR>
		<TD><B> Reviewer </B></TD>
		<TD><SELECT name="cboReviewer" class="SmallerCombo"><%
	String strCurrentReviewer = modelNC.getReviewer();
    for (i = 0; i < beanNCListPersonal.getComboReviewer().getNumberOfRows(); i++) {
    	String strReviewer = beanNCListPersonal.getComboReviewer().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strReviewer.equals(strCurrentReviewer) ? strReviewer + "\" selected" : strReviewer + "\"");
        out.write(">" + strReviewer + "</OPTION>");
    }%></SELECT></TD>
		<TD><B> Process </B></TD>
		<TD><SELECT name="cboProcess" class="SmallCombo"><%
	int nCurrentProcess = modelNC.getProcess();
    for (i = 0; i < beanNCListPersonal.getComboProcess().getNumberOfRows(); i++) {
    	int nProcess = Integer.parseInt(beanNCListPersonal.getComboProcess().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nProcess == nCurrentProcess) ? nProcess + "\" selected" : nProcess + "\"");
        out.write(">" + beanNCListPersonal.getComboProcess().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
		<TD><B>&nbsp; Priority </B></TD>
		<TD><SELECT
            name="cboPriority"
            class="SmallerCombo">
            <%
	int nCurrentPriority = modelNC.getPriority();
    for (i = 0; i < beanNCListPersonal.getComboPriority().getNumberOfRows(); i++) {
    	int nPriority = Integer.parseInt(beanNCListPersonal.getComboPriority().getCell(i,0));
        out.write("<OPTION value=\"");
        out.write((nPriority == nCurrentPriority) ? nPriority + "\" selected" : nPriority + "\"");
        out.write(">" + beanNCListPersonal.getComboPriority().getCell(i, 1) + "</OPTION>");
    }%>
        </SELECT></TD>
	</TR>
	<TR>
		<TD><B> From Date </B></TD>
		<TD><INPUT
            type="text"
            name="txtFromDate"
            class="DateBox"
            value='<%=beanNCListPersonal.getCurrentFromDate() != null ? beanNCListPersonal.getCurrentFromDate() : ""%>'
            maxlength="9"></TD>
		<TD><B> To Date </B></TD>
		<TD><INPUT
            type="text"
            name="txtToDate"
            class="DateBox"
            value='<%=beanNCListPersonal.getCurrentToDate() != null ? beanNCListPersonal.getCurrentToDate() : ""%>'
            maxlength="9"></TD>
		<TD><B>&nbsp; Status </B></TD>
		<TD><SELECT name="cboStatus" class="SmallerCombo"><%
	String strCurrentStatus = Integer.toString(beanNCListPersonal.getNCModel().getStatus());
    for (i = 0; i < beanNCListPersonal.getComboStatus().getNumberOfRows(); i++) {
    	String strStatus = beanNCListPersonal.getComboStatus().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strStatus.equals(strCurrentStatus) ? strStatus + "\" selected" : strStatus + "\"");
        out.write(">" + beanNCListPersonal.getComboStatus().getCell(i, 1) + "</OPTION>");
    }%></SELECT></TD>
	</TR>
	<TR>
		<TD colspan="5"><FONT color="blue">&nbsp;Date format (dd-MMM-yy)</FONT></TD>
		<TD><INPUT
            type="button"
            name="btnSearchNC"
            class="button"
            onclick="javascript:doSearch()"
            value="Search"></TD>
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
        <TD width="10%" align="left">
        <INPUT type="button" name="btnDefine" class="button" onclick ="doViewList()" value="Define">
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
                <TD>of <%=nTotalPage%></TD><%
        if (beanNCListPersonal.getNumPage() < nTotalPage) {
%>
                <TD><IMG border="0" src="images/Buttons/b_next_n.gif" onmouseout="this.src='images/Buttons/b_next_n.gif'" onmouseover="this.src='images/Buttons/b_next_p.gif'" onclick="javascript:goNavNext()" align="bottom"></TD><%
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
                <TD><IMAGE border="0" src="images/tabs/t_personalView_p.gif" align="bottom"></TD>
                <TD><IMAGE border="0" onclick ="doChangeListingMode()" style="cursor:hand" onmouseout="this.src='images/tabs/t_systemView_n.gif'" onmouseover="this.src='images/tabs/t_systemView_p.gif'" src="images/tabs/t_systemView_n.gif" align="bottom"></TD>
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
    for (i = 0; i < nNumFields; i++) {%>
        <TD>&nbsp;<%=aFieldName.get(i).toString()%></TD><%
    }
%>
    </TR><%
    for (i = 0; i < NCMS.NUM_PER_PAGE; i++) {
        if (i + (beanNCListPersonal.getNumPage() - 1) * NCMS.NUM_PER_PAGE >= beanNCListPersonal.getTotal()) {
            break;
        }
%>
    <TR class="row<%=i % 2 + 1%>" title="Click to edit" onclick="javascript:frmCallLogList.hidID.value='<%=beanNCListPersonal.getNCList().getCell(i, aFieldName.indexOf("NCID"))%>';doUpdateCallLog();" onmouseout="this.style.color='#000000';" onmouseover="this.style.color='#FF0000';" style="cursor: hand"><%
        for (int j = 0; j < nNumFields; j++) {%>
        <TD><P style="MARGIN: 1px 3px 1px 3px"><%=beanNCListPersonal.getNCList().getCell(i, j)%></P></TD><%
        }
%>
    </TR><%
    }
    if (i == 0) {%>
    <TR><TD colspan="<%=nNumFields + 1%>" class="row1">No record found! </TD></TR><%
    }
%>
</TABLE>
<HR color="#adadd6" SIZE="1" width="100%">
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
    <TR>
        <TD width="70%">&nbsp;
        <INPUT type="button" name="btnAddNC" class="button" onclick="doAddCallLog()" value="  Add  ">
        <INPUT type="button" name="btnExport" class="button" onclick="doExportNC()" value="Export">
        </TD>
    </TR>
</TABLE>
</FORM>
<SCRIPT language="javascript">
var myForm = document.forms[0];

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
function doSearch() {
    if (!isValidForm()) {
        return;
    }
    frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}
function doCreateReport() {
    frmCallLogList.hidAction.value = "<%=NCMS.NC_REPORT%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}
function doChangeView() {
    clearInvalidControls();
    frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
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


function doLogOut() {
    frmCallLogList.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function doChangeListingMode() {
    clearInvalidControls();
    frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
    frmCallLogList.hidMode.value = "<%=NCMS.SYSTEM_MODE%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function doViewList() {
    clearInvalidControls();
    frmCallLogList.hidAction.value = "<%=NCMS.VIEW_LIST%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function goNavNext() {
    if (!isValidForm()) {
        return;
    }
    frmCallLogList.PageNumber.value = <%=beanNCListPersonal.getNumPage() + 1%>;
    frmCallLogList.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
    frmCallLogList.action = "NcmsServlet";
    frmCallLogList.submit();
}

function goNavBack() {
    if (!isValidForm()) {
        return;
    }
    frmCallLogList.PageNumber.value = <%=beanNCListPersonal.getNumPage() - 1%>;
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
    frmCallLogList.hidAction.value = "<%=NCMS.NC_LIST%>";
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

selectChangeGroup("<%=modelNC.getProjectID()%>");
</SCRIPT>
</BODY>
</HTML>