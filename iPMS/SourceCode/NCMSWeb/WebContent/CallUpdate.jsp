<%@ page language="java"
    import="java.util.Calendar,
            javax.servlet.*,
            fpt.ncms.bean.*,
            fpt.ncms.model.NCModel,
            fpt.ncms.util.StringUtil.*,
            fpt.ncms.constant.NCMS"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
    String strCurrentGroup = beanNCAdd.getNCModel().getGroupName();
    String strCurrentProjectID = beanNCAdd.getNCModel().getProjectID();
    NCModel modelNC = beanNCAdd.getNCModel();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/CallStylesheet.css">
<TITLE>Call Detail</TITLE>
</head>
<body topmargin="0" leftmargin="0">
<form name="frmDetailCall" method="post" action="NcmsServlet">
<input type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<input type="hidden" name="hidID" value="<%=beanNCAdd.getNCModel().getNCID()%>">
<input type="hidden" name="hidNotify" value="1">
<%@ include file="HeaderCallLog.jsp"%>
<table cellPadding="0" cellSpacing="0" class="menu" height="20" width="99%">
  <tbody>
    <tr>
      <td width="10%" height="21" style="border-right:#ffffff thin solid;border-width:1px">
        <p class="menuitem" align="center" style="cursor:hand" onclick="javascript:doCallLogList()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;List&nbsp;&nbsp;</p>
      </td>
      <td width="5%" height="21" style="border-right:#ffffff thin solid;border-width:1px">
        <p class="menuitem" align="center" style="cursor:hand" onclick="javascript:doHistory()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;History&nbsp;&nbsp;</p>
      </td>
      <td align="right" width="95%" height="21" style="border-right:#ffffff thin solid;border-width:1px">
        <p class="menuitem" style="cursor:hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'" >&nbsp;&nbsp;Logout&nbsp;&nbsp;</p>
      </td>
    </tr>
  </tbody>
</table>
<table border="0" cellPadding="0" cellSpacing="0" width="99%">
  <tbody>
    <tr>
      <td align="left" vAlign="top" width="44%">
          <P><FONT size="6">Call Detail</FONT></P>
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
<table border="0" cellPadding="0" cellSpacing="1" width="99%">
  <colgroup>
    <col width="50%">
    <col width="50%">
  <tbody>
    <TR>
    <TD>
    <%=beanNCAdd.isDBError() ? "<FONT color=red size=3>&nbsp;Cannot update this Call</FONT>" : ""%>
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
                <td height="42"><textarea name="txtDescription" rows="1" style="HEIGHT: 30pt; WIDTH: 97%" tabIndex="1" cols="20"><%=beanNCAdd.getNCModel().getDescription()%></textarea></td>
              </tr>
              <tr>
                <td height="25">&nbsp;Creator</td>
                <td height="25"><input name="txtCreator" size="28" readOnly style="BACKGROUND-COLOR: #eeebf7; WIDTH: 97%" value="<%=beanNCAdd.getNCModel().getCreator()%>" tabIndex="2"></td>
              </tr>
              <tr>
                <td height="25">&nbsp;Log date&nbsp;<FONT color=red>*</FONT></td>
                <td height="25">
                  <input maxLength="9" name="txtCreateDate" readOnly style="BACKGROUND-COLOR: #eeebf7" value="<%=beanNCAdd.getNCModel().getCreateDateString()%>" size="20" tabIndex="1">&nbsp;-&nbsp;Time
                  <input maxLength="5" name="txtCreateTime" readOnly style="BACKGROUND-COLOR: #eeebf7" value="<%=beanNCAdd.getNCModel().getCreateTime()%>" size="5" tabIndex="1">
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;Request to</td>
                <TD><SELECT name="optTypeOfCause" style="width: 97%" tabIndex="1"><%
    for (int nRow = 0; nRow < beanNCAdd.getComboTypeOfCause().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" +
                beanNCAdd.getComboTypeOfCause().getCell(nRow, 0) +
                (Integer.parseInt(beanNCAdd.getComboTypeOfCause().getCell(nRow, 0)) == modelNC.getTypeOfCause() ? "\" selected" : "\"") +
                ">" + beanNCAdd.getComboTypeOfCause().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
              </tr>
              <tr>
                <td height="25">&nbsp;Response date</td>
                <td height="25">
                  <input maxLength="9" name="txtReviewDate" value="<%=modelNC.getReviewDate() != null ? modelNC.getReviewDateString() : ""%>" tabIndex="2" size="20">&nbsp;-&nbsp;Time
                  <input maxLength="5" name="txtReviewTime" value="<%=modelNC.getReviewDate() != null ? modelNC.getReviewTime() : ""%>" size="5" tabIndex="2">
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;Status&nbsp;<FONT color=red>*</FONT></td>
                <TD><SELECT name="optStatus" onchange="doCheckStatus()" style="width: 87pt" tabindex=1><%
    for (int nRow = 0; nRow < beanNCAdd.getComboStatus().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboStatus().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboStatus().getCell(nRow, 0)) == (modelNC.getStatus()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboStatus().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
              </tr>
              <tr>
                <td height="25">&nbsp;Type of Solution</td>
                <TD><SELECT name="optTypeOfAction" style="width: 97%" tabindex=2><%
    for (int nRow = 0; nRow < beanNCAdd.getComboTypeOfAction().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboTypeOfAction().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboTypeOfAction().getCell(nRow, 0)) == (modelNC.getTypeOfAction()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboTypeOfAction().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
              </tr>
              <tr>
                <td height="25"  vAlign="top">&nbsp;Solution</td>
                <td height="42"><textarea name="txtCPAction" onchange="doChangeSolution()" rows="1" style="HEIGHT: 30pt; WIDTH: 97%" tabIndex="2" cols="20"><%=(beanNCAdd.getNCModel().getCPAction() != null) ? beanNCAdd.getNCModel().getCPAction() : ""%></textarea></td>
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
                <td height="25"><textarea name="txtCause" rows="1" style="HEIGHT: 30pt; WIDTH: 97%" tabIndex="2" cols="20"><%=(beanNCAdd.getNCModel().getCause() != null) ? beanNCAdd.getNCModel().getCause() : ""%></textarea></td>
              </tr>
              <tr>
                <td height="25">&nbsp;Project&nbsp;<FONT color=red>*</FONT></td>
                <td height="25"><select name="optProject" style="WIDTH: 97%" tabIndex="1">
                  <option value="General">General</option><%
    for (int i = 0; i < beanNCAdd.getComboProject().getNumberOfRows(); i++) {
        String strProjectID = beanNCAdd.getComboProject().getCell(i,0);
        out.write("<OPTION value=\"");
        out.write(strProjectID + "\"");
        out.write(strProjectID.equalsIgnoreCase(modelNC.getProjectID()) ? "selected" : "");
        out.write(">" + strProjectID + "</OPTION>");
    }%>
                  </select></td>
              </tr>
              <tr>
                <td height="25">&nbsp;Deadline</td>
                <td height="25">
                  <input maxLength="9" name="txtDeadLine" tabIndex="2" size="20" value="<%=(beanNCAdd.getNCModel().getDeadLine() != null) ? beanNCAdd.getNCModel().getDeadLineString() : ""%>">&nbsp;-&nbsp;Time
                  <input maxLength="5" name="txtDeadLineTime" value="<%=(beanNCAdd.getNCModel().getDeadLine() != null) ? beanNCAdd.getNCModel().getDeadLineTime() : ""%>" size="5" tabIndex="2">
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;Implementer</td>
                <TD><SELECT name="optAssignee" style="width: 97%" onchange="doChangeAssignee()" tabindex=1><%
    for (int nRow = 0; nRow < beanNCAdd.getComboAssignee().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboAssignee().getCell(nRow, 0)
                + (beanNCAdd.getComboAssignee().getCell(nRow, 0).equalsIgnoreCase(modelNC.getAssignee()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboAssignee().getCell(nRow, 0) + "</OPTION>");
    }%>
                </SELECT></TD>
              </tr>
              <tr>
                <td height="25">&nbsp;Closed date</td>
                <td height="25">
                  <input maxLength="9" name="txtClosureDate" value="<%=modelNC.getClosureDate() != null ? modelNC.getClosureDateString() : ""%>" size="20" tabIndex="2">&nbsp;-&nbsp;Time
                  <input maxLength="5" name="txtClosureTime" value="<%=modelNC.getClosureDate() != null ? modelNC.getClosureTime() : ""%>" size="5" tabIndex="2">
                </td>
              </tr>
              <tr>
                <td height="25">&nbsp;Priority</td>
                <td height="25"><select name="optRepeat" style="WIDTH: 87pt" tabIndex="2"><%
    for (int nRow = 0; nRow < beanNCAdd.getComboPriority().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboPriority().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboPriority().getCell(nRow, 0)) == (modelNC.getRepeat()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboPriority().getCell(nRow, 1) + "</OPTION>");
    }%>
                </select>
              </tr>
              <tr>
                <td height="25">&nbsp;Process</td>
                <TD><SELECT name="optProcess" style="width: 97%" tabindex=1><%
    for (int nRow = 0; nRow < beanNCAdd.getComboProcess().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboProcess().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboProcess().getCell(nRow, 0)) == (modelNC.getProcess()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboProcess().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
              </tr>
              <tr>
                <td height="25" vAlign="top">&nbsp;Result</td>
                <td height="25"><textarea name="txtImpact" rows="1" style="HEIGHT: 30pt; WIDTH: 97%" tabIndex="2" cols="20"><%=(beanNCAdd.getNCModel().getImpact() != null) ? beanNCAdd.getNCModel().getImpact() : ""%></textarea></td>
              </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>
<hr color="#adadd6" SIZE="1" width="99%">
<table border="0" cellPadding="0" cellSpacing="1" width="99%">
  <tbody>
    <tr>
    <td>
    &nbsp;<INPUT type="checkbox" name="chkNotify" checked="checked" onclick="doCheckNotify()"></INPUT>Send mail notification
    </td>
    </tr>
    <tr>
      <td width="70%">&nbsp;
        <INPUT type="button" name="btnSave" class="button" value=" Save " onclick="doSaveCallLog()" <%=beanUserInfo.isModifyEnabled() ? "" : "disabled"%>>
        <INPUT type="button" name="btnBack" class="button" value=" Back " onclick="doCallLogList()">
        <INPUT type="button" name="btnDelete" class="button" value="Delete" onclick="doDeleteCallLog()"
        <%=beanUserInfo.getLoginName().equals(beanNCAdd.getNCModel().getCreator()) && 
           (beanNCAdd.getNCModel().getStatus() != NCMS.NC_STATUS_CLOSED) ? "" : "disabled"%>>
        <INPUT type="button" name="btnHistory" class="button" value="History" onclick="doHistory()">
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
    myForm.hidAction.value = "<%=NCMS.CALL_LOG_SAVE_UPDATE%>";
    myForm.action = "NcmsServlet";
    myForm.submit();
}
function doCheckNotify() {
    if (myForm.chkNotify.checked) {
        myForm.hidNotify.value="1";
    }
    else {
        myForm.hidNotify.value="0";
    }
}

function doDeleteCallLog() {
    if (!window.confirm("Delete this call ?")) {
        return false;
    }
    myForm.hidAction.value = "<%=NCMS.CALL_LOG_DELETE%>";
    myForm.action = "NcmsServlet";
    myForm.submit();
}

function doHistory() {
    myForm.hidAction.value = "<%=NCMS.NC_HISTORY%>";
    myForm.action = "NcmsServlet";
    myForm.submit();
}

function doCheckMandatoryFields() {
    if (trim(myForm.txtDescription.value) == "") {
        alert("Please fill up request title.");
        myForm.txtDescription.focus();
        return false;
    }
    if (trim(myForm.txtCause.value) == "") {
        alert("Please fill up request detail.");
        myForm.txtCause.focus();
        return false;
    }
    return true;
}

function doCheckAllOtherFields() {
    if (trim(myForm.txtCPAction.value) == "") {
        alert("Please fill up solution.");
        myForm.txtCPAction.focus();
        return false;
    }
    if (myForm.optTypeOfAction.value == "") {
        alert("Please fill up Type of solution.");
        myForm.optTypeOfAction.focus();
        return false;
    }
    if (trim(myForm.txtImpact.value) == "") {
        alert("Please fill up Result.");
        myForm.txtImpact.focus();
        return false;
    }
    if (myForm.optProcess.value == "") {
        alert("Please fill up process.");
        myForm.optProcesses.focus();
        return false;
    }
    if (myForm.optTypeOfCause.value == "") {
        alert("Please fill up Request type.");
        myForm.optTypeOfCause.focus();
        return false;
    }
    return true;
}

function doValidateByCreateDate(objControl, objControlTime,
                                strControlTitle, isMandatory,
                                objCreateDate, objCreateTime, strCreateDateTilte) {
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

    var strStartDate = convertToSimpleDate(objCreateDate.value);
    var strEndDate = convertToSimpleDate(objControl.value);
    if (!isDateTime(objControl, objControlTime, false, true)) {
        return false;
    }
    else if (dateCompare(strStartDate, strEndDate) > 0) {
        alert(strControlTitle + " must be greater than " + strCreateDateTilte);
        objControl.focus();
        return false;
    }
    else if (dateCompare(strStartDate, strEndDate) < 0) {
        return true;
    }
    else if ((objControl.value.length > 0) &&
             (objControlTime.value.length <= 0) &&
             (getTimeInMinutes(objCreateTime) > 0)) {
        alert(strControlTitle + " must be greater than " + strCreateDateTilte);
        objControlTime.focus();
        return false;
    }
    else if ((objControlTime.value.length > 0) &&
             (getTimeInMinutes(objControlTime) < getTimeInMinutes(objCreateTime))) {
        alert(strControlTitle + " must be greater than " + strCreateDateTilte);
        objControlTime.focus();
        return false;
    }
    return true;
}

function doValidateDeadLine() {
    var bMandatory = false;
    if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_CLOSED%>" ||
        myForm.optStatus.value == "<%=NCMS.NC_STATUS_PENDING%>" ||
        myForm.optStatus.value == "<%=NCMS.NC_STATUS_ASSIGNED%>")
    {
        bMandatory = true;
    }
    return doValidateByCreateDate(myForm.txtDeadLine, myForm.txtDeadLineTime,
                                  "Deadline", bMandatory,
                                  myForm.txtCreateDate, myForm.txtCreateTime,
                                  "created date");
}

function doValidateClosureDate() {
    var bMandatory = false;
    var bIsValidated = false;
    if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_CLOSED%>") {
        bMandatory = true;
    }
    
    bIsValidated = doValidateByCreateDate(myForm.txtClosureDate,myForm.txtClosureTime,
                              "Closed date", bMandatory,
                              myForm.txtCreateDate, myForm.txtCreateTime,
                              "create date");
    
    if ((bIsValidated) && (myForm.txtClosureDate.value.length > 0)) {
        var strDate = convertToSimpleDate(myForm.txtClosureDate.value);
        if (dateCompare(strToday, strDate) < 0) {
            alert("Close date cannot be future date");
            myForm.txtClosureDate.focus();
            bIsValidated = false;
        }
    }
    if (bIsValidated) {
        bIsValidated = doValidateByCreateDate(myForm.txtClosureDate, myForm.txtClosureTime,
                                      "Closed date", bMandatory,
                                      myForm.txtReviewDate, myForm.txtReviewTime,
                                      "Response date");
    }
    return bIsValidated;
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

function doCheckStatus() {
    var currentClosureDate = myForm.txtClosureDate.value;
    var currentClosureTime = myForm.txtClosureTime.value;
    myForm.txtClosureDate.value = "";
    myForm.txtClosureTime.value = "";
    if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_OPENED%>") {
        myForm.optAssignee.value = "";
        
        myForm.txtReviewDate.value = "";
        myForm.txtReviewTime.value = "";
        myForm.txtClosureDate.value = "";
        myForm.txtClosureTime.value = "";
        //myForm.txtDeadLine.value = "";
        //myForm.txtDeadLineTime.value = "";
    }
    else if (myForm.optStatus.value == <%=NCMS.NC_STATUS_CANCELLED%>) {
        myForm.optAssignee.value = "";
        //myForm.txtDeadLine.value = "";
        //myForm.txtDeadLineTime.value = "";
        if ((myForm.txtReviewDate.value.length == 0) || (myForm.txtReviewDate.value.length == null)) {
            myForm.txtReviewDate.value = "<%=beanNCAdd.getCurrentDate()%>";
            myForm.txtReviewTime.value = "<%=beanNCAdd.getCurrentTime()%>";
        }
    }
    // PQA or Revewer asign task
    else if (myForm.optStatus.value == <%=NCMS.NC_STATUS_ASSIGNED%> &&
             <%=beanUserInfo.getRoleName().equals(NCMS.ROLE_PQA) ||
                beanUserInfo.getRoleName().equals(NCMS.ROLE_REVIEWER)%>)
    {
        if ((myForm.txtReviewDate.value.length == 0) && (myForm.txtCPAction.value.length > 0)) {
            myForm.txtReviewDate.value = "<%=beanNCAdd.getCurrentDate()%>";
            myForm.txtReviewTime.value = "<%=beanNCAdd.getCurrentTime()%>";
        }
    }
    else if (myForm.optStatus.value == <%=NCMS.NC_STATUS_CLOSED%>) {
        // Assign default value
        if (currentClosureDate.length == 0) {
            myForm.txtClosureDate.value = "<%=beanNCAdd.getCurrentDate()%>";
            myForm.txtClosureTime.value = "<%=beanNCAdd.getCurrentTime()%>";
        }
        // Restore previous value
        else {
            myForm.txtClosureDate.value = currentClosureDate;
            myForm.txtClosureTime.value = currentClosureTime;
        }
    }
}

function doChangeAssignee() {
    if (<%=!beanUserInfo.isModifyEnabled()%>) {
        return;
    }
    
    else if (myForm.optAssignee.value == "" && <%=beanUserInfo.isOpenEnabled()%>) {
        myForm.optStatus.value = "<%=NCMS.NC_STATUS_OPENED%>";
        //myForm.txtDeadLine.value = "";
        //myForm.txtDeadLineTime.value = "";
        myForm.txtReviewDate.value = "";
        myForm.txtReviewTime.value = "";
    }
    // Assign a Call to one person
    else if (<%=beanNCAdd.getNCModel().getStatus() == NCMS.NC_STATUS_OPENED%>) {
        myForm.optStatus.value = "<%=NCMS.NC_STATUS_ASSIGNED%>";
        if (myForm.txtCPAction.value.length > 0) {
            myForm.txtReviewDate.value = "<%=beanNCAdd.getCurrentDate()%>";
            myForm.txtReviewTime.value = "<%=beanNCAdd.getCurrentTime()%>";
        }
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

function isValidDateTimeValues() {
    if (!isDateTime(myForm.txtCreateDate, myForm.txtCreateTime, false, true) ||
        !isDateTime(myForm.txtDeadLine, myForm.txtDeadLineTime, false, true) ||
        !isDateTime(myForm.txtReviewDate, myForm.txtReviewTime, false, true) ||
        !isDateTime(myForm.txtClosureDate, myForm.txtClosureTime, false, true) )
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

    if (!isValidDateTimeValues()) {
        return false;
    }
    
    if (!doValidateDeadLine()) {
        return false;
    }
    if (!doValidateClosureDate()) {
        return false;
    }
    if (!doValidateReviewDate()) {
        return false;
    }
    if (!isValidTextLengths()) {
        return false;
    }
    
    if (myForm.optStatus.value != "<%=NCMS.NC_STATUS_OPENED%>" &&
             myForm.optStatus.value != "<%=NCMS.NC_STATUS_CANCELLED%>" &&
             myForm.optAssignee.value.length <= 0) {
        alert("Please choose an assignee first.");
        myForm.optAssignee.focus();
        return false;
    }
    
    if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_OPENED%>" &&
        <%=modelNC.getStatus() != NCMS.NC_STATUS_OPENED%>) {
        if (confirm("Do you want to open this Call again ?")) {
            myForm.optAssignee.value = "";
            myForm.txtClosureDate.value = "";
            myForm.txtClosureTime.value = "";
        }
        else{
            return false;
        }
    }
    else if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_CLOSED%>" &&
             <%=beanUserInfo.isModifyEnabled()%>)
    {
        if (!doCheckAllOtherFields()) {
            return false;
        }
        if (!confirm("Do you want to close this Call ?")) {
            return false;
        }
    }
    else if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_PENDING%>") {
        if (!doCheckAllOtherFields()) {
            return false;
        }
    }
    
    doCheckStatus();
    return true;
}

function removeOtherValues(optControl) {
    var valCurrent = optControl.value;
    for (var i = optControl.options.length - 1; i >= 0; i--) {
        if (optControl.options[i].value != valCurrent) {
            optControl.options[i] = null;
        }
    }
}

// Set fields to not changeable
function setReadOnly() {
    var myForm = document.forms[0];
    if (<%=beanUserInfo.getLoginName().equals(beanNCAdd.getNCModel().getAssignee()) ||
           beanUserInfo.getRoleName().equals(NCMS.ROLE_ASSIGNEE) ||
           beanUserInfo.getRoleName().equals(NCMS.ROLE_CREATOR)%>) {
        removeOtherValues(myForm.optProject);
        
        //myForm.txtDeadLine.style.background = "#eeebf7";
        //myForm.txtDeadLine.readOnly = true;
        //myForm.txtDeadLineTime.style.background = "#eeebf7";
        //myForm.txtDeadLineTime.readOnly = true;
        myForm.txtReviewDate.style.background = "#eeebf7";
        myForm.txtReviewDate.readOnly = true;
        myForm.txtReviewTime.style.background = "#eeebf7";
        myForm.txtReviewTime.readOnly = true;
        myForm.txtClosureDate.style.background = "#eeebf7";
        myForm.txtClosureDate.readOnly = true;
        myForm.txtClosureTime.style.background = "#eeebf7";
        myForm.txtClosureTime.readOnly = true;
    }
}

// Global scripts
myForm.txtDescription.focus();
setReadOnly();
</SCRIPT>
</body>
</html>