<%@ page language="java"
        import="javax.servlet.*,
                fpt.ncms.bean.*,
                fpt.ncms.constant.NCMS,
                fpt.ncms.model.NCModel,
                fpt.ncms.util.StringUtil.*"
%>
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
    boolean bFailed = beanNCAdd.isDBError();
       
    NCListBean beanNCList = (NCListBean)session.getAttribute("beanNCList");
    int nTotalPage = (beanNCList.getTotal() % NCMS.NUM_PER_PAGE == 0)
            ? Math.round(beanNCList.getTotal() / NCMS.NUM_PER_PAGE)
            : Math.round(beanNCList.getTotal() / NCMS.NUM_PER_PAGE) + 1;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
<SCRIPT language="javascript">

var arrCode = new Array();
<%
//  HanhTN added - 09/09/2005
//	out.write("var arrCode = new Array();\n");
	for (int i = 0; i < NCMS.NUM_PER_PAGE; i++) {
	    if (i + (beanNCList.getNumPage() - 1) * NCMS.NUM_PER_PAGE >= beanNCList.getTotal()) {
	        break;
		}
		out.write("arrCode[" + i + "] = \"" + beanNCList.getNCList().getCell(i, 8) + "\";\n");%>
<%}%>

//-----------------------------
var arrItemGroup = new Array();
<%
    for (int i = 0; i < beanNCAdd.getComboGroup().getNumberOfRows(); i++) {
        out.write("arrItemGroup[" + i + "] = \"" + beanNCAdd.getComboGroup().getCell(i, 0) + "\";\n");
    }
%>

function selectChangeLevel() {
    var myElement;
    for (var i = frmDetailNC.optGroup.options.length; i >= 0; i--) {
        frmDetailNC.optGroup.options[i] = null;
    }
    if (frmDetailNC.optLevel.value == 18) {
        myElement = document.createElement("option");
        myElement.value = "General";
        myElement.text = "General";
        frmDetailNC.optGroup.add(myElement);
    }
    else {
        for (var i = 0; i < arrItemGroup.length; i++) {
            myElement = document.createElement("option");
            myElement.value = arrItemGroup[i];
            myElement.text = arrItemGroup[i];
            frmDetailNC.optGroup.add(myElement);
        }
    }
    selectChangeGroup();
}

var arrItemProject = new Array(arrItemGroup.length);
var arrNumProject = new Array(arrItemGroup.length);

<%
    int nProjectIndex = 0;
    for (int i = 0; i < beanNCAdd.getComboGroup().getNumberOfRows(); i++) {
        int nProject = Integer.parseInt(beanNCAdd.getComboGroup().getCell(i, 1));
        out.write("arrItemProject[" + i + "] = new Array(" + nProject + ");\n");
        out.write("arrNumProject[" + i + "] = " + nProject + ";\n");
        for (int j = 0; j < nProject; j++) {
            out.write("arrItemProject[" + i + "][" + j + "] = \"" + beanNCAdd.getComboProject().getCell(nProjectIndex, 0) + "\";\n");
            nProjectIndex++;
        }
    }
%>

function selectChangeGroup() {
    var myElement;
    for (var i = frmDetailNC.optProject.options.length; i >= 0; i--) {
        frmDetailNC.optProject.options[i] = null;
    }
    if ((frmDetailNC.optLevel.value == 19) || (frmDetailNC.optGroup.value == "General")) {
        myElement = document.createElement("option");
        myElement.value = "General";
        myElement.text = "General";
        frmDetailNC.optProject.add(myElement);
    }
    else {
        for (var i = 0; i < arrItemGroup.length; i++) {
            if (frmDetailNC.optGroup.value == arrItemGroup[i]) {
                for (var j = 0; j < arrNumProject[i]; j++) {
                    myElement = document.createElement("option");
                    myElement.value = arrItemProject[i][j];
                    myElement.text = arrItemProject[i][j];
                    frmDetailNC.optProject.add(myElement);
                }
                break;
            }
        }
    }
}
</SCRIPT>
<TITLE>Add new NC</TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0" onload="selectChangeLevel()">
<FORM name="frmDetailNC" method="post" action="NcmsServlet">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<%@ include file="Header.jsp"%>
<TABLE class="menu" cellpadding="0" cellspacing="0" width="100%" height="20pt">
    <TR>
    </TR>
    <TR>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doNCList()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;List&nbsp;&nbsp;</P></TD>
        <TD align="right"><P class="menuitem" style="cursor:hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">Logout&nbsp;&nbsp;</P></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="44%" valign="top" align="left">
        <P><IMG src="images/Headers/ncDetail.gif"></P>
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
</TABLE>
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
    <TR>
    <TD>
    <%=beanNCAdd.isDBError() ? "<FONT color=red size=3>&nbsp;Cannot add this NC, code already exists!</FONT>" : ""%>
    </TD>
    </TR>
    <TR>
        <TD>
        <TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
            <COLGROUP>
                <COL width="15%">
                <COL width="35%">
                <COL width="15%">
                <COL width="35%">
            <TR>
                <TD valign="top">&nbsp;Description&nbsp;<FONT color=red>*</FONT></TD>
                <TD><TEXTAREA name="txtDescription" style="height: 30pt; width: 97%" rows="1" cols="20" tabindex=1><%=bFailed ? beanNCAdd.getNCModel().getDescription() : ""%></TEXTAREA></TD>
                <TD valign="top">&nbsp;C&P action</TD>
                <TD><TEXTAREA name="txtCPAction" style="height: 30pt; width: 97%" rows="1" cols="20" tabindex=1><%=(bFailed && beanNCAdd.getNCModel().getCPAction() != null) ? beanNCAdd.getNCModel().getCPAction() : ""%></TEXTAREA></TD>
            </TR>
            <TR>
                <TD>&nbsp;Assignee</TD>
                <TD><SELECT name="optAssignee" style="width: 97%" onchange="doChangeAssignee()" tabindex=2><%
    for (int nRow = 0; nRow < beanNCAdd.getComboAssignee().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" +
                  beanNCAdd.getComboAssignee().getCell(nRow, 0) + "\">" +
                  beanNCAdd.getComboAssignee().getCell(nRow, 0) + "</OPTION>");
    }%>
                </SELECT></TD>
                <TD>&nbsp;Type of action</TD>
                <TD><SELECT name="optTypeOfAction" style="width: 97%" tabindex=3><%
    for (int nRow = 0; nRow < beanNCAdd.getComboTypeOfAction().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" +
                beanNCAdd.getComboTypeOfAction().getCell(nRow, 0) + "\">" +
                beanNCAdd.getComboTypeOfAction().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;Deadline</TD>
                <TD><INPUT type="text" size="20" maxlength="9" name="txtDeadLine" value="<%=(bFailed && (beanNCAdd.getNCModel().getDeadLineString() != null)) ? beanNCAdd.getNCModel().getDeadLineString() : ""%>" tabindex=4>&nbsp;(dd-MMM-yy)</TD>
                <TD>&nbsp;Code&nbsp;<FONT color=red>*</FONT></TD>
                <TD><INPUT type="text" size="28" maxlength="50" name="txtTitle" value="<%=bFailed ? beanNCAdd.getNCModel().getCode() : ""%>" style="width: 97%" tabindex=5></TD>
            </TR>
            <TR>
                <TD>&nbsp;Creator</TD>
                <TD><INPUT type="text" size="20" name="txtCreator" readonly style="BACKGROUND-COLOR: #eeebf7" width="97%" value="<%=beanUserInfo.getLoginName()%>" tabindex=6></TD>
                <TD>&nbsp;Reviewer</TD>
                <TD><INPUT type="text" size="20" name="txtReviewer" readonly style="BACKGROUND-COLOR: #eeebf7" width="97%" value="" tabindex=7></TD>
            </TR>
            <TR>
                <TD>&nbsp;Create Date&nbsp;<FONT color=red>*</FONT></TD>
                <TD><INPUT type="text" size="20" maxlength="9" name="txtCreationDate" value="<%=beanNCAdd.getNCModel().getCreateDateString()%>" tabindex=8>&nbsp;(dd-MMM-yy)</TD>
                <TD>&nbsp;Closure Date</TD>
                <TD><INPUT type="text" size="20" maxlength="9" name="txtClosureDate" value="" readonly style="BACKGROUND-COLOR: #eeebf7" tabindex=8>&nbsp;(dd-MMM-yy)</TD>
            </TR>
            <TR>
                <TD rowspan="2" valign="top">&nbsp;Impact</TD>
                <TD rowspan="2"><TEXTAREA name="txtImpact" style="height: 30pt; width: 97%" rows="1" cols="20" tabindex=9><%=bFailed ? beanNCAdd.getNCModel().getImpact() : ""%></TEXTAREA></TD>
                <TD>&nbsp;Status&nbsp;<FONT color=red>*</FONT></TD>
                <TD><SELECT name="optStatus" style="width: 87pt" onchange="doCheckStatus()" tabindex=10><%
    for (int nRow = 0; nRow < beanNCAdd.getComboStatus().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboStatus().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboStatus().getCell(nRow, 0)) ==
                   beanNCAdd.getNCModel().getStatus() ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboStatus().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;Repeated</TD>
                <TD><SELECT name="optRepeat" style="width: 87pt" tabindex=11>
                    <OPTION value=-1></OPTION>
                    <OPTION value=0>No</OPTION>
                    <OPTION value=1>Yes</OPTION>
                </SELECT></TD>
                <!--INPUT type="radio" name="rdoRepeat" id="rdoYes" value="<!--%=NCMS.NC_REPEAT%>" <!--%=beanNCAdd.visibility(13, 1)%>>Yes <INPUT type="radio" name="rdoRepeat" id="rdoNo" value="<!--%=NCMS.NC_NONREPEAT%>" <!--%=beanNCAdd.visibility(13, 1)%>>No</TD-->
            </TR>
            <TR>
                <TD>&nbsp;Level&nbsp;<FONT color=red>*</FONT></TD>
                <TD><SELECT name="optLevel" style="width: 97%" tabindex=12 onchange="selectChangeLevel()"><%
    for (int nRow = 0; nRow < beanNCAdd.getComboLevel().getNumberOfRows(); nRow++) {
        //if (!bFailed) {
            out.write("<OPTION value=\"" +
                    beanNCAdd.getComboLevel().getCell(nRow, 0) + "\">" +
                    beanNCAdd.getComboLevel().getCell(nRow, 1) + "</OPTION>");
        //}
        //else {
        //    out.write("<OPTION value=" +
        //          beanNCAdd.getComboLevel().getCell(nRow, 0) +
        //          (Integer.parseInt(beanNCAdd.getComboLevel().getCell(nRow, 0)) == beanNCAdd.getNCModel().getNCLevel() ? " selected" : "") + ">" +
        //          beanNCAdd.getComboLevel().getCell(nRow, 1) + "</OPTION>");
        //}
    }%>
                </SELECT></TD>
                <TD>&nbsp;Process</TD>
                <TD><SELECT name="optProcesses" style="width: 97%" tabindex=13><%
    for (int nRow = 0; nRow < beanNCAdd.getComboProcess().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboProcess().getCell(nRow, 0) + "\">" + beanNCAdd.getComboProcess().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;Group/Department&nbsp;<FONT color=red>*</FONT></TD>
                <TD><SELECT name="optGroup" style="width: 97%" tabindex=14 onchange="selectChangeGroup()">
                </SELECT></TD>
                <TD>&nbsp;Detected by</TD>
                <TD><SELECT name="optDetectedBy" style="width: 97%" tabindex=15><%
    for (int nRow = 0; nRow < beanNCAdd.getComboDetectedBy().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboDetectedBy().getCell(nRow, 0)
                + "\">" + beanNCAdd.getComboDetectedBy().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;Project&nbsp;<FONT color=red>*</FONT></TD>
                <TD><SELECT name="optProject" style="width: 97%" tabindex=16>
                </SELECT></TD>
                <TD>&nbsp;Category&nbsp;<FONT color=red>*</FONT></TD>
                <TD><SELECT name="optTypeOfNC" style="width: 97%" tabindex=17><%
    for (int nRow = 0; nRow < beanNCAdd.getComboTypeOfNC().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboTypeOfNC().getCell(nRow, 0) +
                  (beanNCAdd.getNCModel().getNCType() == Integer.parseInt(beanNCAdd.getComboTypeOfNC().getCell(nRow, 0)) ? "\" selected" : "\"") +
                  ">" + beanNCAdd.getComboTypeOfNC().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;ISO Clause</TD>
                <TD><SELECT name="optISOClause" style="width: 97%" tabindex=18><%
    for (int nRow = 0; nRow < beanNCAdd.getComboISOClause().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboISOClause().getCell(nRow, 0)
                + "\">" + beanNCAdd.getComboISOClause().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
                <TD valign="top">&nbsp;KPA</TD>
                <TD valign="top"><SELECT name="optKPA" style="width: 97%" tabindex=19><%
    for (int nRow = 0; nRow < beanNCAdd.getComboKPA().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboKPA().getCell(nRow, 0)
                + "\">" + beanNCAdd.getComboKPA().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;Type of cause</TD>
                <TD><SELECT name="optTypeOfCause" style="width: 97%" tabindex=20><%
    for (int nRow = 0; nRow < beanNCAdd.getComboTypeOfCause().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboTypeOfCause().getCell(nRow, 0)
                + "\">" + beanNCAdd.getComboTypeOfCause().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
                <TD>&nbsp;Review Date</TD>
                <TD><INPUT type="text" size="20" maxlength="9" name="txtReviewDate" readonly style="BACKGROUND-COLOR: #eeebf7" tabindex=22>&nbsp;(dd-MMM-yy)</TD>
            </TR>
            <TR class="BrgColor" rowspan="5" valign="top">
                <TD valign="top">&nbsp;Cause</TD>
                <TD rowspan="20" height="35pt"><TEXTAREA name="txtCause" style="height: 30pt; width: 97%" rows="1" cols="20" tabindex=21><%=bFailed ? beanNCAdd.getNCModel().getCause() : ""%></TEXTAREA></TD>
                <TD valign="top">&nbsp;Note</TD>
                <TD rowspan="20" height="35pt"><TEXTAREA name="txtNote" style="height: 30pt; width: 97%" rows="1" cols="20" tabindex=22><%=bFailed ? beanNCAdd.getNCModel().getNote() : ""%></TEXTAREA></TD>
            </TR>           
        </TABLE>
        </TD>
    </TR>
</TABLE>

<HR width="100%" size="1" color="#ADADD6">
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
    <TR>
        <TD width="70%">
        &nbsp;<IMG border="0" onclick="doSaveNC()" style="cursor:hand" onkeypress="javascript:if (window.event.keyCode==13 || window.event.keyCode==32) doSaveNC();" onMouseOut="this.src='images/Buttons/b_ok_n.gif'" onMouseOver="this.src='images/Buttons/b_ok_p.gif'" src="images/Buttons/b_ok_n.gif" tabindex="23">
        &nbsp;<IMG border="0" onclick="doNCList()" style="cursor: hand" onkeypress="javascript:if (window.event.keyCode==13 || window.event.keyCode==32) doNCList();" onmouseout="this.src='images/Buttons/b_back_n.gif'" onmouseover="this.src='images/Buttons/b_back_p.gif'" src="images/Buttons/b_back_n.gif" tabindex="23"></TD>
    </TR>
</TABLE>
</FORM>
<SCRIPT language="javascript">
var myForm = document.forms[0];
myForm.txtDescription.focus();

function doNCList() {
    myForm.hidAction.value = "<%=NCMS.NC_LIST%>";
    myForm.action = "NcmsServlet";
    myForm.submit();
}

function doLogOut() {
    myForm.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    myForm.action = "NcmsServlet";
    myForm.submit();
}

function doSaveNC() {
    if (formValidate()) {
        myForm.hidAction.value = "<%=NCMS.NC_SAVE_NEW%>";
        myForm.action = "NcmsServlet";
        myForm.submit();
    }
}

function doChangeAssignee() {
    if (myForm.optAssignee.value == "" && <%=beanUserInfo.isOpenEnabled()%>) {
        myForm.optStatus.value = "<%=NCMS.NC_STATUS_OPENED%>";
        myForm.txtDeadLine.value = "";
        myForm.txtReviewDate.value = "";
        myForm.txtReviewer.value = "";
    }
    else if (<%=beanUserInfo.isAssignEnabled()%>) {
        myForm.optStatus.value = "<%=NCMS.NC_STATUS_ASSIGNED%>";
        myForm.txtReviewDate.value = "<%=beanNCAdd.getCurrentDate()%>";
        myForm.txtReviewer.value = "<%=beanUserInfo.getLoginName()%>";
    }
}

function doCheckStatus() {
    if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_OPENED%>") {
        myForm.optAssignee.value = "";
        
        myForm.txtReviewDate.value = "";
        myForm.txtReviewer.value = "";
        myForm.txtDeadLine.value = "";
    }
    else {
        myForm.txtReviewDate.value = "<%=beanNCAdd.getCurrentDate()%>";
        myForm.txtReviewer.value = "<%=beanUserInfo.getLoginName()%>";
    }
}

function doCheckMandatoryFields() {
    if (trim(myForm.txtDescription.value) == "") {
        alert("Please fill up description.");
        myForm.txtDescription.focus();
        return false;
    }
    if (trim(myForm.txtTitle.value) == "") {
        alert("Please fill up NC code.");
        myForm.txtTitle.focus();
        return false;
    }
    if(((myForm.txtTitle.value.charAt(myForm.txtTitle.value.length - 1)) == "/") || ((myForm.txtTitle.value.charAt(myForm.txtTitle.value.length - 1)) == "\\") || ((myForm.txtTitle.value.charAt(myForm.txtTitle.value.length - 1)) == "$")) {
    	alert("<%=NCMS.JSP_THE_CHARACTER_AT_THE_END_OF_STRING_IS_INVALID%>");
        myForm.txtTitle.focus();
        return false;
    }
    if (trim(myForm.txtCreationDate.value) == "") {
        alert("Please fill up create date.");
        myForm.txtCreationDate.focus();
        return false;
    }
    return true;
}

function doValidateCreateDate() {
    if (!isDate(myForm.txtCreationDate.value)) {
        alert("Invalid date format");
        myForm.txtCreationDate.focus();
        return false;
    }
    
    var strToday = convertToSimpleDate(
                    "<%=beanNCAdd.getNCModel().getCreateDateString()%>");
    var strCreateDate = convertToSimpleDate(myForm.txtCreationDate.value);
    
    if (dateCompare(strToday, strCreateDate) < 0) {
        alert("Create date cannot be future date");
        myForm.txtCreationDate.focus();
        return false;
    }
    return true;
}

function doValidateByCreateDate(objControl, strControlTitle, isMandatory, objCreateDate) {
    if (isMandatory) {
        if (objControl.value.length <= 0) {
            alert("Please fill up " + strControlTitle);
            objControl.focus();
            return false;
        }
    }
    if (!validDates(myForm, new Array(objControl.name), true, strControlTitle + " is incorrect")) {
        return false;
    }
    strStartDate = convertToSimpleDate(objCreateDate.value);
    strEndDate = convertToSimpleDate(objControl.value);
    if (!compareDate(strStartDate, strEndDate)) {
        alert(strControlTitle + " must be greater than Create date!");
        objControl.focus();
        return false;
    }
    return true;
}

function doValidateDeadLine() {
    var bMandatory = false;
    if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_ASSIGNED%>") {
        bMandatory = true;
    }
    return doValidateByCreateDate(myForm.txtDeadLine, "DeadLine",
                                  bMandatory, myForm.txtCreationDate);
}

function isValidTextLengths() {
    var MAX_LEN = 1000;
    if (myForm.txtDescription.value.length > MAX_LEN) {
        alert("Only allows maximum " + MAX_LEN + " characters for Description, please try again");
        myForm.txtDescription.focus();
        return false
    }
    if (myForm.txtCPAction.value.length > MAX_LEN) {
        alert("Only allows maximum " + MAX_LEN + " characters for C&P Action, please try again");
        myForm.txtCPAction.focus();
        return false
    }
    if (myForm.txtImpact.value.length > MAX_LEN) {
        alert("Only allows maximum " + MAX_LEN + " characters for Impact, please try again");
        myForm.txtImpact.focus();
        return false
    }
    if (myForm.txtCause.value.length > MAX_LEN) {
        alert("Only allows maximum " + MAX_LEN + " characters for Cause, please try again");
        myForm.txtCause.focus();
        return false
    }
    if (myForm.txtNote.value.length > MAX_LEN) {
        alert("Only allows maximum " + MAX_LEN + " characters for Note, please try again");
        myForm.txtNote.focus();
        return false
    }
    return true;
}

function formValidate() {
    if (!doCheckMandatoryFields()) {
        return false;
    }

    if (!doValidateCreateDate()) {
        return false;
    }
    
    if (!doValidateDeadLine()) {
        return false;
    }
    
    if (!isValidTextLengths()) {
        return false;
    }
    
    if (myForm.optStatus.value != "<%=NCMS.NC_STATUS_OPENED%>" &&
             myForm.optAssignee.value.length <= 0) {
        alert("Please choose an assignee first.");
        myForm.optAssignee.focus();
        return false;
    }
    
    // HanhTN added - 09/09/2005
	for (var i = 0; i < arrCode.length; i++) {
	    if (frmDetailNC.txtTitle.value == arrCode[i]) {
	    	alert("Code already exists, please try again");
	    	frmDetailNC.txtTitle.focus();
	    	return false;
	    }
	}

    return true;
}

// Set focus to Code if previous add failed, usually causes by Code already existed.
<%=bFailed ? "myForm.txtTitle.focus();" : ""%>
</SCRIPT>
</BODY>
</HTML>