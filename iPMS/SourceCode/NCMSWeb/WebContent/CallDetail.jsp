<%@ page language="java"
    import="java.util.Calendar,
            javax.servlet.*,
            fpt.ncms.bean.*,
            fpt.ncms.util.StringUtil.*,
            fpt.ncms.constant.NCMS"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
    boolean bFailed = beanNCAdd.isDBError();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/CallStylesheet.css">
<TITLE>Call Log</TITLE>
</head>
<body topmargin="0" leftmargin="0">
<form name="frmDetailCall" method="post" action="NcmsServlet">
<input type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<input type="hidden" name="hidNotify" value="1">
<%@ include file="HeaderCallLog.jsp"%>
<table cellPadding="0" cellSpacing="0" class="menu" height="20" width="99%">
  <tbody>
    <tr>
      <td width="12%" height="21" style="border-right:#ffffff thin solid;border-width:1px">
            <p class="menuitem" align="center" style="cursor:hand" onclick="javascript:doCallLogList()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;List&nbsp;&nbsp;</p>
      </td>
      <td align="right" width="95%" height="21" style="border-right:#ffffff thin solid;border-width:1px">
            <p class="menuitem" style="cursor:hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'" >&nbsp;&nbsp;Logout&nbsp;&nbsp;</p>
      </td>
    </tr>
  </tbody>
</table>
<table border="0" cellPadding="0" cellSpacing="0" width="100%">
  <tbody>
    <tr>
      <td align="left" vAlign="top" width="44%">
          <P><FONT size="6">Log call</FONT></P>
      </td>
      <td vAlign="top" width="56%">
        <div align="right">
          <table border="0" cellPadding="0" cellSpacing="0" height="50" width="199">
            <tbody>
              <tr>
                <td>
                  <table border="0" cellPadding="0" cellSpacing="0" width="100%">
                    <tbody>
                      <tr>
                        <td width="75">&nbsp;</td>
                        <td width="70"><b>User:</b></td>
                        <TD width="54"><B><%=beanUserInfo.getLoginName()%></B></TD>
                      </tr>
                      <tr>
                        <td width="75">&nbsp;</td>
                        <td width="70"><b>Role:</b></td>
                        <TD width="54"><B><%=beanUserInfo.getRoleName()%></B></TD>
                      </tr>
                    </tbody>
                  </table>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </td>
    </tr>
  </tbody>
</table>
<table border="0" cellPadding="0" cellSpacing="0" width="100%">
  <colgroup>
    <col width="50%">
    <col width="50%">
  <tbody>
    <TR>
    <TD>
    <%=beanNCAdd.isDBError() ? "<FONT color=red size=3>&nbsp;Cannot log this Call</FONT>" : ""%>
    </TD>
    <TD>
    </TD>
    </TR>
    <tr>
      <td height="25"><FONT color="#0000ff">&nbsp;(Date format: dd-MMM-yy, Time: HH24:mm)</FONT>
      </td>
      <td>
      </td>
    </tr>
    <tr>
      <td vAlign="top">
        <table border="0" cellPadding="0" cellSpacing="1" width="100%">
          <colgroup>
            <col width="30%">
            <col width="70%">
          <tbody>
              <tr>
                <td vAlign="top" height="42">&nbsp;Request title&nbsp;<FONT color=red>*</FONT></td>
                <td height="42"><textarea name="txtDescription" rows="1" style="HEIGHT: 30pt; WIDTH: 97%" tabIndex="1" cols="20"><%=bFailed ? beanNCAdd.getNCModel().getDescription() : ""%></textarea></td>
              </tr>
              <tr>
                <td height="25">&nbsp;Creator</td>
                <td height="25"><input name="txtCreator" readOnly size="28" style="BACKGROUND-COLOR: #eeebf7; WIDTH: 97%" value="<%=beanNCAdd.getNCModel().getCreator()%>" tabIndex="2"></td>
              </tr>
              <tr>
                <td height="25">&nbsp;Log date&nbsp;<FONT color=red>*</FONT></td>
                <td height="25">
                  <input maxLength="9" name="txtCreateDate" value="<%=beanNCAdd.getNCModel().getCreateDateString()%>" size="20" tabIndex="3">&nbsp;-&nbsp;Time
                  <input maxLength="5" name="txtCreateTime" value="<%=beanNCAdd.getNCModel().getCreateTime()%>" size="5" tabIndex="3">
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;Request to</td>
                <TD><SELECT name="optTypeOfCause" style="width: 97%" tabIndex="4"><%
    for (int nRow = 0; nRow < beanNCAdd.getComboTypeOfCause().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboTypeOfCause().getCell(nRow, 0)
                + "\">" + beanNCAdd.getComboTypeOfCause().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
              </tr>
              <tr>
                <td height="25">&nbsp;Response date</td>
                <td height="25">
                  <input maxLength="9" name="txtReviewDate" size="20" tabIndex="5">&nbsp;-&nbsp;Time
                  <input maxLength="5" name="txtReviewTime" size="5" tabIndex="5">
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;Status&nbsp;<FONT color=red>*</FONT></td>
                <TD><SELECT name="optStatus" onchange="doCheckStatus()" style="width: 87pt" tabindex=6><%
    for (int nRow = 0; nRow < beanNCAdd.getComboStatus().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboStatus().getCell(nRow, 0) +
                  "\">" + beanNCAdd.getComboStatus().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
              </tr>
              <tr>
                <td height="25">&nbsp;Type of Solution</td>
                <TD><SELECT name="optTypeOfAction" style="width: 97%" tabindex=7><%
    for (int nRow = 0; nRow < beanNCAdd.getComboTypeOfAction().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" +
                beanNCAdd.getComboTypeOfAction().getCell(nRow, 0) + "\">" +
                beanNCAdd.getComboTypeOfAction().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
              </tr>
              <tr>
                <td height="25"  vAlign="top">&nbsp;Solution</td>
                <td height="42"><textarea name="txtCPAction" onchange="doChangeSolution()" rows="1" style="HEIGHT: 30pt; WIDTH: 97%" tabIndex="8" cols="20"><%=(bFailed && beanNCAdd.getNCModel().getCPAction() != null) ? beanNCAdd.getNCModel().getCPAction() : ""%></textarea></td>
              </tr>
          </tbody>
        </table>
      </td>
      <td vAlign="top">
        <table border="0" cellPadding="0" cellSpacing="1" width="100%">
          <colgroup>
            <col width="30%">
            <col width="70%">
          <tbody>
              <tr>
                <td vAlign="top" height="42">&nbsp;Request detail&nbsp;<FONT color=red>*</FONT></td>
                <td height="25"><textarea name="txtCause" rows="1" style="HEIGHT: 30pt; WIDTH: 97%" tabIndex="1" cols="20"><%=(bFailed && beanNCAdd.getNCModel().getCause() != null) ? beanNCAdd.getNCModel().getCause() : ""%></textarea></td>
              </tr>
              <tr>
                <td height="25">&nbsp;Project&nbsp;<FONT color=red>*</FONT></td>
                <td height="25"><select name="optProject" style="WIDTH: 97%" tabIndex="2">
                  <option value="General">General</option><%
    for (int i = 0; i < beanNCAdd.getComboProject().getNumberOfRows(); i++) {
        String strProjectID = beanNCAdd.getComboProject().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strProjectID + "\"");
        out.write(">" + strProjectID + "</OPTION>");
    }%>
                  </select></td>
              </tr>
              <tr>
                <td height="25">&nbsp;Deadline</td>
                <td height="25">
                  <input maxLength="9" name="txtDeadLine" tabIndex="3" size="20" value="<%=(bFailed && (beanNCAdd.getNCModel().getDeadLineString() != null)) ? beanNCAdd.getNCModel().getDeadLineString() : ""%>">&nbsp;-&nbsp;Time
                  <input maxLength="5" name="txtDeadLineTime" value="<%=(bFailed && (beanNCAdd.getNCModel().getDeadLine() != null)) ? (beanNCAdd.getNCModel().getDeadLineTime()) : ""%>" size="5" tabIndex="3">
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;Implementer</td>
                <TD><SELECT name="optAssignee" style="width: 97%" onchange="doChangeAssignee()" tabindex=4><%
    for (int nRow = 0; nRow < beanNCAdd.getComboAssignee().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" +
                  beanNCAdd.getComboAssignee().getCell(nRow, 0) + "\">" +
                  beanNCAdd.getComboAssignee().getCell(nRow, 0) + "</OPTION>");
    }%>
                </SELECT></TD>
              </tr>
              <tr>
                <td height="25">&nbsp;Closed date</td>
                <td height="25">
                  <input maxLength="9" name="txtClosureDate" readOnly style="BACKGROUND-COLOR: #eeebf7" size="20" tabIndex="5">&nbsp;-&nbsp;Time
                  <input maxLength="5" name="txtClosureTime" readOnly style="BACKGROUND-COLOR: #eeebf7" size="5" tabIndex="5">
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;Priority</td>
                <td height="25"><select name="optRepeat" style="WIDTH: 87pt" tabIndex="6"><%
    for (int nRow = 0; nRow < beanNCAdd.getComboPriority().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboPriority().getCell(nRow, 0) + "\">" +
                  beanNCAdd.getComboPriority().getCell(nRow, 1) + "</OPTION>");
    }%>
                </select>
              </tr>
              <tr>
                <td height="25">&nbsp;Process</td>
                <TD><SELECT name="optProcess" style="width: 97%" tabindex=7><%
    for (int nRow = 0; nRow < beanNCAdd.getComboProcess().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboProcess().getCell(nRow, 0) +
                  "\">" + beanNCAdd.getComboProcess().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
              </tr>
              <tr>
                <td height="25" vAlign="top">&nbsp;Result</td>
                <td height="25"><textarea name="txtImpact" rows="1" style="HEIGHT: 30pt; WIDTH: 97%" tabIndex="8" cols="20"><%=(bFailed && beanNCAdd.getNCModel().getImpact() != null) ? beanNCAdd.getNCModel().getImpact() : ""%></textarea></td>
              </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>
<hr color="#adadd6" SIZE="1" width="99%">
<table border="0" cellPadding="0" cellSpacing="1" width="100%">
  <tbody>
    <tr>
    <td>
    &nbsp;<INPUT type="checkbox" name="chkNotify" checked="checked" onclick="doCheckNotify()"></INPUT>Send mail notification
    </td>
    </tr>
    <tr>
      <td width="70%">&nbsp;
        <INPUT type="button" name="btnSave" class="button" value=" Save " onclick="doSaveCallLog()" onkeypress="javascript:if (window.event.keyCode==13) doSaveCallLog();">
        <INPUT type="button" name="btnBack" class="button" value=" Back " onclick="doCallLogList()" onkeypress="javascript:if (window.event.keyCode==13) doCallLogList();">
      </td>
    </tr>
  </tbody>
</table>
</form>
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
<SCRIPT>
var myForm = document.forms[0];
var strToday = convertToSimpleDate(
                "<%=beanNCAdd.getCurrentDate()%>");
function doChangeAssignee() {
    if (myForm.optAssignee.value == "" && <%=beanUserInfo.isOpenEnabled()%>) {
        myForm.optStatus.value = "<%=NCMS.NC_STATUS_OPENED%>";
        //myForm.txtDeadLine.value = "";
        //myForm.txtDeadLineTime.value = "";
        myForm.txtReviewDate.value = "";
        myForm.txtReviewTime.value = "";
    }
    else if (<%=beanUserInfo.isAssignEnabled()%>) {
        myForm.optStatus.value = "<%=NCMS.NC_STATUS_ASSIGNED%>";
        if (myForm.txtCPAction.value.length > 0) {
            myForm.txtReviewDate.value = "<%=beanNCAdd.getCurrentDate()%>";
            myForm.txtReviewTime.value = "<%=beanNCAdd.getCurrentTime()%>";
        }
    }
}

function doCheckStatus() {
    if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_OPENED%>") {
        myForm.optAssignee.value = "";
        
        myForm.txtReviewDate.value = "";
        myForm.txtReviewTime.value = "";
        //myForm.txtDeadLine.value = "";
        //myForm.txtDeadLineTime.value = "";
    }
    else if (myForm.txtCPAction.value.length > 0) {
        myForm.txtReviewDate.value = "<%=beanNCAdd.getCurrentDate()%>";
        myForm.txtReviewTime.value = "<%=beanNCAdd.getCurrentTime()%>";
    }
}

function doCheckNotify() {
    if (myForm.chkNotify.checked) {
        myForm.hidNotify.value="1";
    }
    else {
        myForm.hidNotify.value="0";
    }
}

function doChangeSolution() {
    if ((myForm.txtCPAction.value.length > 0) &&
        (myForm.optAssignee.value.length > 0) &&
        (myForm.txtReviewDate.value.length == 0))
    {
        myForm.txtReviewDate.value = "<%=beanNCAdd.getCurrentDate()%>";
        myForm.txtReviewTime.value = "<%=beanNCAdd.getCurrentTime()%>";
    }
}

function doCallLogList() {
    myForm.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
    myForm.action = "NcmsServlet";
    myForm.submit();
}
function doLogOut() {
    myForm.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    myForm.action = "NcmsServlet";
    myForm.submit();
}
function doSaveCallLog() {
    if (!isValidForm()) {
        return;
    }
    myForm.hidAction.value = "<%=NCMS.CALL_LOG_SAVE_NEW%>";
    myForm.action = "NcmsServlet";
    myForm.submit();
}

function doCheckMandatoryFields() {
    if (trim(myForm.txtDescription.value) == "") {
        alert("Please fill up request title.");
        myForm.txtDescription.focus();
        return false;
    }
    if (trim(myForm.txtCreateDate.value) == "") {
        alert("Please fill up create date.");
        myForm.txtCreateDate.focus();
        return false;
    }
    if (trim(myForm.txtCause.value) == "") {
        alert("Please fill up request detail.");
        myForm.txtCause.focus();
        return false;
    }
    return true;
}

function doValidateCreateDate() {
    var strToday = convertToSimpleDate(
                    "<%=beanNCAdd.getNCModel().getCreateDateString()%>");
    var strCreateDate = convertToSimpleDate(myForm.txtCreateDate.value);
    
    if (dateCompare(strToday, strCreateDate) < 0) {
        alert("Log date cannot be future date");
        myForm.txtCreateDate.focus();
        return false;
    }
    return true;
}

function doValidateByCreateDate(objControl, objControlTime,
                                strControlTitle, isMandatory,
                                objCreateDate, objCreateTime) {
    if (objControl.value.length <= 0) {
        if (isMandatory) {
            alert("Please fill up " + strControlTitle);
            objControl.focus();
            return false;
        }
        else if ((objControlTime.value.length <= 0) ||
                 (objControlTime.value.length == null))
        {
            return true;
        }
    }
    
    if (!validDates(myForm, new Array(objControl.name), true, strControlTitle + " is incorrect")) {
        return false;
    }
    strStartDate = convertToSimpleDate(objCreateDate.value);
    strEndDate = convertToSimpleDate(objControl.value);
    if (!isDateTime(objControl, objControlTime, false, true)) {
        return false;
    }
    else if (dateCompare(strStartDate, strEndDate) > 0) {
        alert(strControlTitle + " must be greater than Create date!");
        objControl.focus();
        return false;
    }
    else if (dateCompare(strStartDate, strEndDate) < 0) {
        return true;
    }
    else if ((objControl.value.length > 0) &&
             (objControlTime.value.length <= 0) &&
             (getTimeInMinutes(objCreateTime) > 0)) {
        alert(strControlTitle + " must be greater than Create date!");
        objControlTime.focus();
        return false;
    }
    else if ((objControlTime.value.length > 0) &&
             getTimeInMinutes(objControlTime) < getTimeInMinutes(objCreateTime))
    {
        alert(strControlTitle + " must be greater than Create date!");
        objControlTime.focus();
        return false;
    }
    return true;
}

function doValidateDeadLine() {
    var bMandatory = false;
    if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_ASSIGNED%>") {
        bMandatory = true;
    }
    return doValidateByCreateDate(myForm.txtDeadLine, myForm.txtDeadLineTime,
                                  "DeadLine", bMandatory,
                                  myForm.txtCreateDate, myForm.txtCreateTime);
}

function doValidateReviewDate() {
    var bMandatory = false;
    var bIsValidated = true;
    if ((myForm.optStatus.value != "<%=NCMS.NC_STATUS_OPENED%>") && (myForm.txtCPAction.value.length > 0))
    {
        bMandatory = true;
    }
    bIsValidated = doValidateByCreateDate(myForm.txtReviewDate,myForm.txtReviewTime,
                              "Response date", bMandatory,
                              myForm.txtCreateDate, myForm.txtCreateTime,
                              "create date");
    
    if ((bIsValidated) && (myForm.txtReviewDate.value.length == 0) &&
        (myForm.txtCPAction.value.length > 0) && (myForm.optAssignee.value.length > 0))
    {
        alert("Please fill up Response date");
        myForm.txtReviewDate.focus();
        bIsValidated = false;
    }
    
    if ((bIsValidated) && (myForm.txtReviewDate.value.length > 0)) {
        var strDate = convertToSimpleDate(myForm.txtReviewDate.value);
        if (dateCompare(strToday, strDate) < 0) {
            alert("Response date cannot be future date");
            myForm.txtReviewDate.focus();
            bIsValidated = false;
        }
    }

    if ((bIsValidated) && (myForm.txtReviewDate.value.length > 0) &&
        (myForm.txtCPAction.value.length == 0))
    {
        alert("Please fill up Solution before set the Response date");
        myForm.txtCPAction.focus();
        bIsValidated = false;
    }
    return bIsValidated;
}

function isValidDateTimeValues() {
    if (!isDateTime(myForm.txtCreateDate, myForm.txtCreateTime, true, true) ||
        !isDateTime(myForm.txtReviewDate, myForm.txtReviewTime, false, true) ||
        !isDateTime(myForm.txtDeadLine, myForm.txtDeadLineTime, false, true))
    {
        return false;
    }
    return true;
}

function isValidTextLengths() {
    var MAX_LEN = 1000;
    if (myForm.txtDescription.value.length > MAX_LEN) {
        alert("Only allows maximum " + MAX_LEN + " characters for Request title, please try again");
        myForm.txtDescription.focus();
        return false
    }
    if (myForm.txtCause.value.length > MAX_LEN) {
        alert("Only allows maximum " + MAX_LEN + " characters for Request detail, please try again");
        myForm.txtCause.focus();
        return false
    }
    if (myForm.txtCPAction.value.length > MAX_LEN) {
        alert("Only allows maximum " + MAX_LEN + " characters for Solution, please try again");
        myForm.txtCPAction.focus();
        return false
    }
    if (myForm.txtImpact.value.length > MAX_LEN) {
        alert("Only allows maximum " + MAX_LEN + " characters for Result, please try again");
        myForm.txtImpact.focus();
        return false
    }
    return true;
}

function isValidForm() {
    if (!doCheckMandatoryFields()) {
        return false;
    }

    if (!isValidDateTimeValues ()) {
        return false;
    }
    
    if (!doValidateCreateDate()) {
        return false;
    }
    
    if (!doValidateDeadLine()) {
        return false;
    }
    
    if (!doValidateReviewDate()) {
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
    
    return true;
}

// Global scripts
myForm.txtDescription.focus();
</SCRIPT>
</body>
</html>