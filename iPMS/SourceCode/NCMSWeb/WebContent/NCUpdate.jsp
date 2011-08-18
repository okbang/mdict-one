<%@ page language="java" 
         import="javax.servlet.*,
                 fpt.ncms.bean.*,
                 fpt.ncms.constant.NCMS,
                 fpt.ncms.util.StringUtil.*,
                 fpt.ncms.util.DateUtil.DateUtil,
                 fpt.ncms.model.NCModel"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCAddBean beanNCAdd = (NCAddBean)session.getAttribute("beanNCAdd");
    String strCurrentGroup = beanNCAdd.getNCModel().getGroupName();
    String strCurrentProjectID = beanNCAdd.getNCModel().getProjectID();
    NCModel modelNC = beanNCAdd.getNCModel();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
<SCRIPT language="javascript">
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
            if (myElement.value == "<%=strCurrentGroup%>") {
                myElement.selected = true;
            }
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
function doExport() {<%
	if (modelNC.getNCType() == NCMS.NCTYPE_CC ||
			modelNC.getNCType() == NCMS.NCTYPE_NC ||
			modelNC.getNCType() == NCMS.NCTYPE_OB ||
			modelNC.getNCType() == NCMS.NCTYPE_PB) {
%>
	frmDetailNC.hidAction.value = "<%=(modelNC.getNCType() == NCMS.NCTYPE_CC) ? NCMS.NC_EXPORT_CC : NCMS.NC_EXPORT_NC%>";
	frmDetailNC.action = "NcmsServlet";
	frmDetailNC.target = "_blank";
	frmDetailNC.submit();
	frmDetailNC.target = "";<%
	}
%>	
}
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
		            if (myElement.value == "<%=strCurrentProjectID%>") {
		                myElement.selected = true;
		            }
                }
            }
        }
    }
}

</SCRIPT>
<TITLE>NC Detail</TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="frmDetailNC" method="post" action="NcmsServlet">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<INPUT type="hidden" name="hidID" value="<%=modelNC.getNCID()%>">
<%@ include file="Header.jsp"%>
<TABLE class="menu" cellpadding="0" cellspacing="0" width="100%" height="20pt">
    <TR>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doNCListSearch()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;List&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doHistory()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;History&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doExport()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Export&nbsp;to&nbsp;MS&nbsp;Word&nbsp;&nbsp;</P></TD>
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
    <%=beanNCAdd.isDBError() ? "<FONT color=red size=3>&nbsp;Cannot update this NC, code already exists!</FONT>" : ""%>
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
                <TD><TEXTAREA name="txtDescription" style="height: 30pt; width: 97%" rows="1" cols="20" tabindex=1><%=modelNC.getDescription() != null ? modelNC.getDescription() : ""%></TEXTAREA></TD>
                <TD valign="top">&nbsp;C&P action</TD>
                <TD><TEXTAREA name="txtCPAction" style="height: 30pt; width: 97%" rows="1" cols="20" tabindex=1><%=modelNC.getCPAction() != null ? modelNC.getCPAction() : ""%></TEXTAREA></TD>
            </TR>
            <TR>
                <TD>&nbsp;Assignee</TD>
                <TD><SELECT name="optAssignee" style="width: 97%" onchange="doChangeAssignee()" tabindex=2><%
    for (int nRow = 0; nRow < beanNCAdd.getComboAssignee().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboAssignee().getCell(nRow, 0)
                + (beanNCAdd.getComboAssignee().getCell(nRow, 0).equalsIgnoreCase(modelNC.getAssignee()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboAssignee().getCell(nRow, 0) + "</OPTION>");
    }%>
                </SELECT></TD>
                <TD>&nbsp;Type of action</TD>
                <TD><SELECT name="optTypeOfAction" style="width: 97%" tabindex=3><%
    for (int nRow = 0; nRow < beanNCAdd.getComboTypeOfAction().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboTypeOfAction().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboTypeOfAction().getCell(nRow, 0)) == (modelNC.getTypeOfAction()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboTypeOfAction().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;Deadline</TD>
                <TD><INPUT type="text" size="20" maxlength="9" name="txtDeadLine" value="<%=modelNC.getDeadLine() != null ? modelNC.getDeadLineString() : ""%>" tabindex=4>&nbsp;(dd-MMM-yy)</TD>
                <TD>&nbsp;Code&nbsp;<FONT color=red>*</FONT></TD>
                <TD><INPUT type="text" size="28" maxlength="50" name="txtTitle" value="<%=modelNC.getCode()%>" style="width: 97%" tabindex=5></TD>
            </TR>
            <TR>
                <TD>&nbsp;Creator</TD>
                <TD><INPUT type="text" size="20" name="txtCreator" value="<%=modelNC.getCreator()%>" readonly style="BACKGROUND-COLOR: #eeebf7" tabindex=6></TD>
                <TD>&nbsp;Reviewer</TD>
                <TD><INPUT type="text" size="20" name="txtReviewer" readonly style="BACKGROUND-COLOR: #eeebf7" value="<%=modelNC.getReviewer() != null ? modelNC.getReviewer() : ""%>" tabindex=7></TD>
            </TR>
            <TR>
                <TD>&nbsp;Create Date&nbsp;<FONT color=red>*</FONT></TD>
                <TD><INPUT type="text" size="20" maxlength="9" name="txtCreationDate" value="<%=modelNC.getCreateDate() != null ? modelNC.getCreateDateString() : ""%>" readonly style="BACKGROUND-COLOR: #eeebf7" tabindex=8></TD>
                <TD>&nbsp;Closure Date</TD>
                <TD><INPUT type="text" size="20" maxlength="9" name="txtClosureDate" value="<%=modelNC.getClosureDate() != null ? modelNC.getClosureDateString() : ""%>" tabindex=9>&nbsp;(dd-MMM-yy)</TD>
            </TR>
            <TR>
                <TD rowspan="2" valign="top">&nbsp;Impact</TD>
                <TD rowspan="2"><TEXTAREA name="txtImpact" style="height: 30pt; width: 97%" rows="1" cols="20" tabindex=10><%=modelNC.getImpact() != null ? modelNC.getImpact() : ""%></TEXTAREA></TD>
                <TD >&nbsp;Status&nbsp;<FONT color=red>*</FONT></TD>
                <TD>
                <SELECT
					name="optStatus" style="width: 87pt" onchange="doCheckStatus()"
					tabindex="11">
					<%
    for (int nRow = 0; nRow < beanNCAdd.getComboStatus().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboStatus().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboStatus().getCell(nRow, 0)) == (modelNC.getStatus()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboStatus().getCell(nRow, 1) + "</OPTION>");
    }%>
				</SELECT></TD></TR>
            
            <TR>
                <TD>&nbsp;Repeated</TD>
                <TD><SELECT name="optRepeat" style="width: 87pt" tabindex=12>
                	<OPTION value=-1 <%=modelNC.getRepeat() == -1 ? "selected" : ""%>></OPTION>
                	<OPTION value=0 <%=modelNC.getRepeat() == 0 ? "selected" : ""%>>No</OPTION>
                	<OPTION value=1 <%=modelNC.getRepeat() == 1 ? "selected" : ""%>>Yes</OPTION>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;Level&nbsp;<FONT color=red>*</FONT></TD>
                <TD><SELECT name="optLevel" style="width: 97%" tabindex=13 onchange="selectChangeLevel()"><%
    for (int nRow = 0; nRow < beanNCAdd.getComboLevel().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboLevel().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboLevel().getCell(nRow, 0)) == (modelNC.getNCLevel()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboLevel().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
                <TD>&nbsp;Process</TD>
                <TD><SELECT name="optProcesses" style="width: 97%" tabindex=14><%
    for (int nRow = 0; nRow < beanNCAdd.getComboProcess().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboProcess().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboProcess().getCell(nRow, 0)) == (modelNC.getProcess()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboProcess().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;Group/Department&nbsp;<FONT color=red>*</FONT></TD>
                <TD><SELECT name="optGroup" style="width: 97%" tabindex=15 onchange="selectChangeGroup()">
                <OPTION></OPTION>
                </SELECT></TD>
                <TD>&nbsp;Detected by</TD>
                <TD><SELECT name="optDetectedBy" style="width: 97%" tabindex=16><%
    for (int nRow = 0; nRow < beanNCAdd.getComboDetectedBy().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboDetectedBy().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboDetectedBy().getCell(nRow, 0)) == (modelNC.getDetectedBy()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboDetectedBy().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;Project&nbsp;<FONT color=red>*</FONT></TD>
                <TD><SELECT name="optProject" style="width: 97%" tabindex=17>
                <OPTION></OPTION>
                </SELECT></TD>
                <TD>&nbsp;Category&nbsp;<FONT color=red>*</FONT></TD>
                <TD><SELECT name="optTypeOfNC" style="width: 97%" tabindex=18 onchange="CheckMandatoryEffect()"><%
    for (int nRow = 0; nRow < beanNCAdd.getComboTypeOfNC().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboTypeOfNC().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboTypeOfNC().getCell(nRow, 0)) == (modelNC.getNCType()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboTypeOfNC().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;ISO Clause</TD>
                <TD><SELECT name="optISOClause" style="width: 97%" tabindex=19><%
    for (int nRow = 0; nRow < beanNCAdd.getComboISOClause().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboISOClause().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboISOClause().getCell(nRow, 0)) == (modelNC.getISOClause()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboISOClause().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
                <TD>&nbsp;KPA</TD>
                <TD><SELECT name="optKPA" style="width: 97%" tabindex=20><%
    for (int nRow = 0; nRow < beanNCAdd.getComboKPA().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboKPA().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboKPA().getCell(nRow, 0)) == (modelNC.getKPA()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboKPA().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
            </TR>
            <TR>
                <TD>&nbsp;Type of cause</TD>
                <TD><SELECT name="optTypeOfCause" style="width: 97%" tabindex=21><%
    for (int nRow = 0; nRow < beanNCAdd.getComboTypeOfCause().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCAdd.getComboTypeOfCause().getCell(nRow, 0)
                + (Integer.parseInt(beanNCAdd.getComboTypeOfCause().getCell(nRow, 0)) == (modelNC.getTypeOfCause()) ? "\" selected" : "\"")
                + ">" + beanNCAdd.getComboTypeOfCause().getCell(nRow, 1) + "</OPTION>");
    }%>
                </SELECT></TD>
                <TD>&nbsp;Review Date</TD>
                <TD><INPUT type="text" size="20" maxlength="9" name="txtReviewDate" value="<%=modelNC.getReviewDate() != null ? modelNC.getReviewDateString() : ""%>" tabindex=22>&nbsp;(dd-MMM-yy)</TD>
            </TR>
            <TR class="BrgColor" rowspan="8" valign="top">
                <TD valign="top" rowspan="2">&nbsp;Cause</TD>                
                <TD rowspan="2"><TEXTAREA name="txtCause" style="height: 60pt; width: 97%" rows="1" cols="20" tabindex=24><%=modelNC.getCause()!= null ? modelNC.getCause() : ""%></TEXTAREA></TD>
                <TD valign="top" height="40">&nbsp;Effect of changes <IMG name="asterisk" src="images/asterisk.bmp"></TD>
                <TD height="40"><TEXTAREA name="txtEffectOfChange" style="height: 30pt; width: 97%" rows="1" cols="20" tabindex="23"><%=modelNC.getEffectOfChange() != null ? modelNC.getEffectOfChange() : ""%></TEXTAREA></TD>
            </TR>
			<TR>
				<TD valign="top" height="41">&nbsp;Note</TD>
				<TD height="41">
				<TEXTAREA name="txtNote" style="height: 30pt; width: 97%" rows="1" cols="20" tabindex=24><%=modelNC.getNote() != null ? modelNC.getNote() : ""%></TEXTAREA>
				</TD> 
			</TR>
			<TR>
				<TD colspan="4" align="center">&nbsp;<SPAN
					style='font-size: 11.0pt; font-family: "Times New Roman"; mso-fareast-font-family: "Times New Roman"; color: red; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA'>*Note:</SPAN><SPAN
					style='font-size: 12.0pt; font-family: "Times New Roman"; mso-fareast-font-family: "Times New Roman"; mso-ansi-language: EN-US; mso-fareast-language: EN-US; mso-bidi-language: AR-SA'>
				<SPAN style="color: red">If you want other users to refer to your
				document, can you copy the link in to text area in form.</SPAN></SPAN></TD>			
			</TR>

        </TABLE>
        </TD>
    </TR>
</TABLE>

<HR width="100%" size="1" color="#ADADD6">
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
    <TR>
        <TD width="40%">
        <%if (beanUserInfo.isModifyEnabled()) {%>
        &nbsp;<IMG border="0" onclick="doSaveUpdateNC()" style="cursor:hand" tabindex="25" onkeypress="javascript:if (window.event.keyCode==13 || window.event.keyCode==32) doSaveUpdateNC();" onMouseOut="this.src='images/Buttons/b_ok_n.gif'" onMouseOver="this.src='images/Buttons/b_ok_p.gif'" src="images/Buttons/b_ok_n.gif">
        <%}%>
        &nbsp;<IMG border="0" onclick="doNCList()" style="cursor: hand" tabindex="27" onkeypress="javascript:if (window.event.keyCode==13 || window.event.keyCode==32) doNCList();" onmouseout="this.src='images/Buttons/b_back_n.gif'" onmouseover="this.src='images/Buttons/b_back_p.gif'" src="images/Buttons/b_back_n.gif">
        &nbsp;<IMG border="0" onclick="doHistory()" style="cursor: hand" tabindex="27" onkeypress="javascript:if (window.event.keyCode==13 || window.event.keyCode==32) doHistory();" onmouseout="this.src='images/Buttons/b_viewHistory_n.gif'" onmouseover="this.src='images/Buttons/b_viewHistory_p.gif'" src="images/Buttons/b_viewHistory_n.gif"></TD>
        <TD width="30%">&nbsp;</TD>
    </TR>
</TABLE>
</FORM>
<SCRIPT language="javascript">
var myForm = document.forms[0];
var strToday = convertToSimpleDate(
                "<%=beanNCAdd.getCurrentDate()%>");
myForm.txtDescription.focus();
selectChangeLevel();
//Add by Binhnt
var mandatoryEffect=false;
CheckMandatoryEffect();

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
        removeOtherValues(myForm.optGroup);
        removeOtherValues(myForm.optLevel);
        
        myForm.txtDeadLine.style.background = "#eeebf7";
        myForm.txtDeadLine.readOnly = true;
        myForm.txtReviewDate.style.background = "#eeebf7";
        myForm.txtReviewDate.readOnly = true;
    }
}

setReadOnly();

function doNCListSearch() {
    myForm.hidAction.value = "<%=NCMS.NC_LIST%>";
    myForm.action = "NcmsServlet";
    myForm.target = "_self";
    myForm.submit();
}

function doLogOut() {
    myForm.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    myForm.action = "NcmsServlet";
    myForm.target = "_self";
    myForm.submit();
}

function doSaveUpdateNC() {
    if (formValidate()) {
        myForm.hidAction.value = "<%=NCMS.NC_SAVE_UPDATE%>";
        myForm.action = "NcmsServlet";
    	myForm.target = "_self";    	
        myForm.submit();
    }
}

function doNCList() {
    myForm.hidAction.value = "<%=NCMS.NC_LIST%>";
    myForm.action = "NcmsServlet";
    myForm.target = "_self";
    myForm.submit();
}

function doHistory() {
    myForm.hidAction.value = "<%=NCMS.NC_HISTORY%>";
    myForm.action = "NcmsServlet";
    myForm.submit();
}

function doCheckMandatoryFields() {
    if (trim(myForm.txtDescription.value) == "") {
        alert("Please fill up NC description.");
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
    if (mandatoryEffect && trim(myForm.txtEffectOfChange.value) == "") {
    	alert("Please fill up Effect of change.");
        myForm.txtEffectOfChange.focus();
        return false;
   	}
    return true;
}

function doCheckAllOtherFields() {
    if (trim(myForm.txtCPAction.value) == "") {
        alert("Please fill up C&P action.");
        myForm.txtCPAction.focus();
        return false;
    }
    if (myForm.optTypeOfAction.value == "") {
        alert("Please fill up type of action.");
        myForm.optTypeOfAction.focus();
        return false;
    }
    if (trim(myForm.txtImpact.value) == "") {
        alert("Please fill up impact.");
        myForm.txtImpact.focus();
        return false;
    }
    if (myForm.optProcesses.value == "") {
        alert("Please fill up process.");
        myForm.optProcesses.focus();
        return false;
    }
    if (myForm.optISOClause.value == "") {
        alert("Please fill up ISO clause.");
        myForm.optISOClause.focus();
        return false;
    }
    if (myForm.optKPA.value == "") {
        alert("Please fill up KPA.");
        myForm.optKPA.focus();
        return false;
    }
    if (myForm.optProcesses.value == "") {
        alert("Please fill up process.");
        myForm.optProcesses.focus();
        return false;
    }
    if (myForm.optTypeOfCause.value == "") {
        alert("Please fill up type of cause.");
        myForm.optTypeOfCause.focus();
        return false;
    }
    if (trim(myForm.txtCause.value) == "") {
        alert("Please fill up cause.");
        myForm.txtCause.focus();
        return false;
    }
    return true;
}

function doValidateByCreateDate(objControl, strControlTitle,
                                isMandatory,
                                objCreateDate, strCreateDateTitle) {
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
        alert(strControlTitle + " must be greater than " + strCreateDateTitle);
        objControl.focus();
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
    return doValidateByCreateDate(myForm.txtDeadLine, "DeadLine",
                                  bMandatory, myForm.txtCreationDate);
}

function doValidateClosureDate() {
    var bMandatory = false;
    var bIsValidated = false;
    if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_CLOSED%>") {
        bMandatory = true;
	}
    bIsValidated = doValidateByCreateDate(myForm.txtClosureDate, "ClosureDate",
                                  bMandatory, myForm.txtCreationDate, "CreateDate");
    if ((bIsValidated) && (myForm.txtClosureDate.value.length > 0)) {
        var strDate = convertToSimpleDate(myForm.txtClosureDate.value);
        if (dateCompare(strToday, strDate) < 0) {
            alert("Close date cannot be future date");
            myForm.txtClosureDate.focus();
            bIsValidated = false;
        }
    }
    if (bIsValidated) {
        bIsValidated = doValidateByCreateDate(myForm.txtClosureDate, "ClosureDate",
                                  bMandatory, myForm.txtReviewDate, "ReviewDate");
    }
    
    return bIsValidated;
}

function doValidateReviewDate() {
    var bMandatory = false;
    var bIsValidated = true;
    if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_ASSIGNED%>" ||
        myForm.optStatus.value == "<%=NCMS.NC_STATUS_PENDING%>" ||
        myForm.optStatus.value == "<%=NCMS.NC_STATUS_CLOSED%>")
    {
        bMandatory = true;
	}
    bIsValidated = doValidateByCreateDate(myForm.txtReviewDate, "ReviewDate",
                                  bMandatory, myForm.txtCreationDate, "CreateDate");
    if ((bIsValidated) && (myForm.txtReviewDate.value.length > 0)) {
        var strDate = convertToSimpleDate(myForm.txtReviewDate.value);
        if (dateCompare(strToday, strDate) < 0) {
            alert("Review date cannot be future date");
            myForm.txtReviewDate.focus();
            bIsValidated = false;
        }
    }
    return bIsValidated;
}

function doCheckStatus() {
    var currentClosureDate = myForm.txtClosureDate.value;
    myForm.txtClosureDate.value = "";
    if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_OPENED%>") {
        myForm.optAssignee.value = "";
        myForm.txtReviewer.value = "";
        myForm.txtReviewDate.value = "";
    }
    else if (myForm.optStatus.value == <%=NCMS.NC_STATUS_CANCELLED%>) {
        myForm.optAssignee.value = "";
        myForm.txtDeadLine.value = "";
        myForm.txtReviewer.value = "<%=beanUserInfo.getLoginName()%>";
        myForm.txtReviewDate.value = "<%=beanNCAdd.getCurrentDate()%>";
    }
    // PQA or Revewer asign task
    else if (myForm.optStatus.value == <%=NCMS.NC_STATUS_ASSIGNED%> &&
             <%=beanUserInfo.getRoleName().equals(NCMS.ROLE_PQA) ||
                beanUserInfo.getRoleName().equals(NCMS.ROLE_REVIEWER)%>)
    {
        if (myForm.txtReviewer.value.length == 0) {
            myForm.txtReviewer.value = "<%=beanUserInfo.getLoginName()%>";
        }
        if (myForm.txtReviewDate.value.length == 0) {
            myForm.txtReviewDate.value = "<%=beanNCAdd.getCurrentDate()%>";
        }
    }
    else if (myForm.optStatus.value == <%=NCMS.NC_STATUS_CLOSED%>) {
	    // Assign default value
	    if (currentClosureDate.length == 0) {
            myForm.txtClosureDate.value = "<%=beanNCAdd.getCurrentDate()%>";
        }
        // Restore previous value
        else {
            myForm.txtClosureDate.value = currentClosureDate;
        }                
    }
   
    CheckMandatoryEffect();
}

function doChangeAssignee() {
    if (myForm.optAssignee.value == "" && <%=beanUserInfo.isOpenEnabled()%>) {
        myForm.optStatus.value = "<%=NCMS.NC_STATUS_OPENED%>";
        myForm.txtDeadLine.value = "";
        myForm.txtReviewDate.value = "";
        myForm.txtReviewer.value = "";
    }
    // Assign a NC to one person
    else if (<%=beanNCAdd.getNCModel().getStatus() == NCMS.NC_STATUS_OPENED%>) {
        myForm.optStatus.value = "<%=NCMS.NC_STATUS_ASSIGNED%>";
        myForm.txtReviewDate.value = "<%=beanNCAdd.getCurrentDate()%>";
        myForm.txtReviewer.value = "<%=beanUserInfo.getLoginName()%>";
    }
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
    if (myForm.txtEffectOfChange.value.length > MAX_LEN) {
        alert("Only allows maximum " + MAX_LEN + " characters for Effect of change, please try again");
        myForm.txtEffectOfChange.focus();
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
        <%=modelNC.getStatus() != NCMS.NC_STATUS_OPENED ? "true" : "false"%>) {
        if (confirm("Do you want to open this NC again?")) {
            myForm.optAssignee.value = "";
            myForm.txtClosureDate.value = "";
        }
        else{
            return false;
        }
    }
    else if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_CLOSED%>") {
        if (!doCheckAllOtherFields()) {
            return false;
        }
	    if (myForm.optRepeat.value < 0) {
	        alert("Please decide this NC is repeat or not.");
	        myForm.optRepeat.focus();
	        return false;
	    }
        if (!confirm("Do you want to close this NC?")) {
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

function CheckMandatoryEffect(){
	var AgntUsr=navigator.userAgent.toLowerCase();
	var AppVer=navigator.appVersion.toLowerCase();
	var DomYes=document.getElementById?1:0;
	var NavYes=AgntUsr.indexOf('mozilla')!=-1&&AgntUsr.indexOf('compatible')==-1?1:0;
	var ExpYes=AgntUsr.indexOf('msie')!=-1?1:0;
	var Nav4=NavYes&&!DomYes&&document.layers?1:0;
	var Exp4=ExpYes&&!DomYes&&document.all?1:0;	
	var M_Hide=Nav4?'hide':'hidden';
	var M_Show=Nav4?'show':'visible';
	
	if (myForm.optStatus.value == "<%=NCMS.NC_STATUS_CLOSED%>" && myForm.optTypeOfNC.value =="<%=NCMS.NCTYPE_PB%>"){
		mandatoryEffect=true;
		parent.myForm.asterisk.style.visibility=M_Show;
	}else{
		mandatoryEffect=false;
		parent.myForm.asterisk.style.visibility=M_Hide;
	}
}
</SCRIPT>
</BODY>
</HTML>