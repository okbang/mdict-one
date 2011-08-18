<%@page import="com.fms1.infoclass.*,java.util.*,java.text.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>requirementUpdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();onPageLoad()">
<%
	Vector deliverableList = (Vector)request.getAttribute("deliverableList");
	Vector projectList = (Vector)request.getAttribute("prjList");
	ProjectInfo generalInfo = (ProjectInfo)request.getAttribute("generalInfo");
	ScheduleHeaderInfo schHdrInfo = (ScheduleHeaderInfo)request.getAttribute("schHdrInfo");
	Date prjStartDate = schHdrInfo.aStartD;
	if (prjStartDate == null)
		prjStartDate = new Date();
	
	String strLifecycle = generalInfo.getLifecycle();
	
	boolean isMaintenance = false;
	boolean isDevelopment = false;
	
	if(strLifecycle.equals("Development"))
		isDevelopment = true;
	else if (strLifecycle.equals("Maintenance"))
		isMaintenance = true;
	
	Vector requirementList = (Vector)request.getAttribute("requirementList");
	long reqID=Long.parseLong(request.getParameter("requirementID"));
	RequirementInfo reqInfo=null;
	for (int i = 0; i < requirementList.size(); i++) {
		reqInfo=(RequirementInfo)requirementList.elementAt(i);
		if (reqInfo.requirementID==reqID)
			break;
	}
	String committed = reqInfo.committedDate != null ? CommonTools.dateFormat(reqInfo.committedDate) : "";
	String designed = reqInfo.designedDate != null ? CommonTools.dateFormat(reqInfo.designedDate) : "";
	String coded = reqInfo.codedDate != null ? CommonTools.dateFormat(reqInfo.codedDate) : "";
	String tested = reqInfo.testedDate != null ? CommonTools.dateFormat(reqInfo.testedDate) : "";
	String deployed = reqInfo.deployedDate != null ? CommonTools.dateFormat(reqInfo.deployedDate) : "";
	String accepted = reqInfo.acceptedDate != null ? CommonTools.dateFormat(reqInfo.acceptedDate) : "";
	String cancelled = reqInfo.cancelledDate != null ? CommonTools.dateFormat(reqInfo.cancelledDate) : "";
	String receivedDate = reqInfo.receivedDate != null ? CommonTools.dateFormat(reqInfo.receivedDate) : "";
	String responsedDate = reqInfo.responseDate != null ? CommonTools.dateFormat(reqInfo.responseDate) : "";
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Requirements")%> </P>
<BR>
<FORM name="frm" action="Fms1Servlet">
<TABLE class="Table" cellspacing="1" width="95%">
    <COL span="1" width="160">
    <COL span="1" width="400">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Updaterequirement")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Requirement")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="name"><%=reqInfo.name%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Deliverable")%> </TD>
            <TD class="CellBGR3"><SELECT class="COMBO" name="deliverable">
                <OPTION value="0" selected>        </OPTION>
                <%
                if (deliverableList != null)
            	for (int i = 0; i < deliverableList.size(); i++) {
            		ModuleInfo moduleInfo = (ModuleInfo)deliverableList.elementAt(i);
            		if (moduleInfo != null) {
            	%> 
              	<OPTION value="<%=moduleInfo.moduleID%>" <%if (moduleInfo.moduleID == reqInfo.moduleID) {%>selected<%}%>><%=moduleInfo.name%></OPTION>
            	<%
            		}
                }
                %>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Type")%> </TD>
            <TD class="CellBGR3">
            <SELECT class="COMBO" name="type" id="type" onchange="onTypeChange();">
                <OPTION value="1" <%if (reqInfo.type == 1) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.New")%> </OPTION>
                <OPTION value="2" <%if (reqInfo.type == 2) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Changerequest")%> </OPTION>
	            <%
	            if (isMaintenance) {
	            %>
                <OPTION value="3" <%if (reqInfo.type == 3) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Defect")%> </OPTION>
	           <%}%>
            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Size")%> </TD>
            <TD class="CellBGR3"><SELECT class="COMBO" name="size">
                <OPTION value="1" <%if (reqInfo.size == 1) {%>selected<%}%>>1</OPTION>
                <OPTION value="2" <%if (reqInfo.size == 2) {%>selected<%}%>>2</OPTION>
                <OPTION value="3" <%if (reqInfo.size == 3) {%>selected<%}%>>3</OPTION>
                <OPTION value="4" <%if (reqInfo.size == 4) {%>selected<%}%>>4</OPTION>
                <OPTION value="5" <%if (reqInfo.size == 5) {%>selected<%}%>>5</OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Committed")%> </TD>
            <TD class="CellBGR3">
            <INPUT type="hidden" name="status" value="<%=CommonTools.dateFormat(prjStartDate)%>"><INPUT size="9" type="text" maxlength="9" name="status" value="<%=committed%>">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Designed")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="status" value="<%=designed%>">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Coded")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="status" value="<%=coded%>">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Tested")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="status" value="<%=tested%>">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Deployed")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="status" value="<%=deployed%>">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Accepted")%> </TD>
            <TD class="CellBGR3">
            <INPUT size="9" type="text" maxlength="9" name="status" value="<%=accepted%>">(DD-MMM-YY)
			<INPUT type="hidden" name="status" value="<%=CommonTools.dateFormat(new Date())%>">
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Cancelled")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="cancelled" value="<%=cancelled%>">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Requirementsection")%> </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="srs" value='<%=reqInfo.requirementSection != null ? reqInfo.requirementSection : ""%>'></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Designsection")%> </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="dd" value='<%=reqInfo.detailDesign != null ? reqInfo.detailDesign : ""%>'></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.CodemoduleFile")%> </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="codeModule" value='<%=reqInfo.codeModuleName != null ? reqInfo.codeModuleName : ""%>'></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Testcasesection")%> </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="testCase" value='<%=reqInfo.testCase != null ? reqInfo.testCase : ""%>'></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Releasenote")%> </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="releaseNote" value='<%=reqInfo.releaseNote != null ? reqInfo.releaseNote : ""%>'></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Effortpd")%> </TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="effort" value='<%=reqInfo.effort >= 0 ? CommonTools.formatDouble(reqInfo.effort) : ""%>'></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Elapseddays")%> </TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="elapsedDays" value='<%=reqInfo.elapsedDay >= 0 ? String.valueOf(reqInfo.elapsedDay) : ""%>'></TD>
        </TR>
        <TR> 
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Previousprojectcode")%> </TD>
            <%if (!isDevelopment) {%>
            <TD class="CellBGR3"><SELECT class="COMBO" name="prevPrjID">
                <OPTION value="0" selected>        </OPTION>
                <%
                if (projectList != null)
            	for (int i = 0; i < projectList.size(); i++) {
            		ProjectDateInfo projectInfo = (ProjectDateInfo)projectList.elementAt(i);
            		if ((projectInfo != null) && (projectInfo.projectID != Integer.parseInt(session.getAttribute("projectID").toString()))){
		            	%>
		                <OPTION value="<%=projectInfo.projectID%>" <%if (reqInfo.prevPrjID == projectInfo.projectID) {%>selected<%}%>><%=projectInfo.code%></OPTION>
		          <%}
                }%>
            </SELECT></TD>
            <%}else {%>
            <TD class="CellBGR3">N/A</TD>
            <%}%>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Receiveddate")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="receivedDate" value="<%=receivedDate%>">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Responsedate")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="responsedDate" value="<%=responsedDate%>">(DD-MMM-YY)</TD>
        </TR>
    </TBODY>
</TABLE>
<P>
<INPUT type="button" class="BUTTON" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.requirementUpdate.OK")%> " onclick="onOK()"> <INPUT type="button" class="BUTTON" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.requirementUpdate.Cancel")%> " onclick="onCancel()"></P>
<INPUT type="hidden" name="reqType" value="<%=Constants.REQUIREMENT_UPDATE%>">
<INPUT type="hidden" name="requirementID" value="<%=reqInfo.requirementID%>">
</FORM>
<SCRIPT language="javascript">
function onOK() {
	if (trim(frm.name.value) == "") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.requirementUpdate.Nameofrequirementcannotbeempty")%>");
		frm.name.focus();
		return;
	}
	if (trim(frm.name.value).length > 600){
		window.alert("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.NameofRequirementCannotGreaterThan600")%>" + trim(frm.name.value).length);
		frm.name.focus();
		return;
	}
	for (var i = 1; i < 7; i++)
		if (trim(frm.status[i].value) != "") {
			if (!isDate(frm.status[i].value)) {
				window.alert("<%= languageChoose.getMessage("fi.jsp.requirementUpdate.StatusdatemustbeintheformatddMMMyyyy")%>");
				frm.status[i].focus();
				return;
			}
		}

	for (var i = 1; i < 7; i++) {
		if (trim(frm.status[i].value) != "") {
			var b = true;
			for (var j = i - 1; j >= 0; j--) {
				if ((trim(frm.status[j].value) != "") && 
					(compareDate(frm.status[j].value, frm.status[i].value) < 0)) {
					b = false;
					break;
				}
			}
			if (b)
				for (var j = i + 1; j < 8; j++) {
					if ((trim(frm.status[j].value) != "") && 
						(compareDate(frm.status[i].value, frm.status[j].value) < 0)) {
						b = false;
						break;
					}
				}
			if (!b) {
				window.alert(getParamText(new Array('<%= languageChoose.getMessage("fi.jsp.requirementUpdate.Status__date__must__be__between__previous__status__dates__or__project__start__date__~PARAM1_START_DATE~__and__next__status__dates__or__today")%>', '<%=CommonTools.dateFormat(prjStartDate)%>')));
				frm.status[i].focus();
				return;
			}
		}
	}
	
	if (trim(frm.cancelled.value) != "") {
		if (!isDate(frm.cancelled.value)) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementUpdate.CancelleddatemustbeintheformatddMMMyy")%>");
			frm.cancelled.focus();
			return;
		}
		if ((compareDate(frm.status[0].value, frm.cancelled.value) < 0) ||
			(compareDate(frm.cancelled.value, frm.status[7].value) < 0)) {
			window.alert(getParamText(new Array('<%= languageChoose.getMessage("fi.jsp.requirementUpdate.Cancelleddatemustbebetweenprojectstartdate~PARAM1_DATE~andtoday")%>','<%=CommonTools.dateFormat(prjStartDate)%>')));
			frm.cancelled.focus();
			return;
		}
	}
	
	if (trim(frm.effort.value) != "") {
		if (isNaN(frm.effort.value)) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementUpdate.Effortmustbeaintegernumber")%>");
			frm.effort.focus();
			return;
		}
		else if (parseInt(frm.effort.value) < 0) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementUpdate.Effortmustbeapositivenumber")%>");
			frm.effort.focus();
			return;
		}
	}
	
	if (trim(frm.elapsedDays.value) != "") {
		if (isNaN(frm.elapsedDays.value)) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementUpdate.Elapseddaysmustbeaintegernumber")%>");
			frm.elapsedDays.focus();
			return;
		}
		else if (parseInt(frm.elapsedDays.value) < 0) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementUpdate.Elapseddaysmustbeapositivenumber")%>");
			frm.elapsedDays.focus();
			return;
		}
	}

	if ((trim(frm.receivedDate.value) != "") && (!isDate(frm.receivedDate.value))) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.requirementUpdate.ReceiveddatemustbeintheformatddMMMyyyy")%>");
		frm.receivedDate.focus();
		return;
	}
	
	if (trim(frm.responsedDate.value) != "") {
		if (!isDate(frm.responsedDate.value)) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementUpdate.ResponseddatemustbeintheformatddMMMyyyy")%>");
			frm.responsedDate.focus();
			return;
		}
		else if (compareDate(frm.receivedDate.value, frm.responsedDate.value) < 0) {
			window.alert("<%=languageChoose.getMessage("fi.jsp.requirementUpdate.Responseddatemustbeafterreceiveddate")%>");
			frm.responsedDate.focus();
			return;
		}
	}
	
	frm.submit();
}

function onCancel() {
	doIt(<%=Constants.REQUIREMENT_DETAIL%>+ "&requirementID=" + <%=reqInfo.requirementID%>);
}

function onTypeChange() {
	if ((frm.type.value == 2) || (frm.type.value == 3)) {
		showObj('effort');
		showObj('elapsedDays');
	}
	else {
		hideObj('effort');
		hideObj('elapsedDays');
	}
	if (frm.type.value ==2)
	{
		showObj('receivedDate');
		showObj('responsedDate');
	
	}else
	{
		hideObj('receivedDate');
		hideObj('responsedDate');
}
}

function onPageLoad() {
	frm.name.focus();
	onTypeChange();
}
</SCRIPT>
</BODY>
</HTML>