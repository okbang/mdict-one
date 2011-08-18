<%@page import="com.fms1.infoclass.*,java.util.*,java.text.*, com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*, com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
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
<TITLE>requirementAddnew.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();onPageLoad();">
<%
	int projectID = Integer.parseInt(session.getAttribute("projectID").toString());
	Vector deliverableList = (Vector)request.getAttribute("deliverableList");
	Vector projectList = (Vector)request.getAttribute("prjList");
	ScheduleHeaderInfo schHdrInfo = (ScheduleHeaderInfo)request.getAttribute("schHdrInfo");
    ProjectInfo prjInfo = (ProjectInfo)request.getAttribute("projectInfo")   ;

	Date prjStartDate = schHdrInfo.aStartD;
	if (prjStartDate == null){
		prjStartDate = new Date();
	}
	boolean isMaintenance = false;
	boolean isDevelopment = false;
	if(prjInfo.getLifecycle().equals("Development"))
		isDevelopment = true;
	else if (prjInfo.getLifecycle().equals("Maintenance"))
		isMaintenance = true;
		
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Requirements")%> </P>
<BR>
<FORM name="frm" method="POST">
<TABLE class="Table" cellspacing="1" width="95%">
    <COL span="1" width="160">
    <COL span="1" width="400">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Addnewrequirement")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Requirement")%>* </TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="name"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Deliverable")%> </TD>
            <TD class="CellBGR3"><SELECT class="COMBO" name="deliverable">
                <OPTION value="-1" selected>        </OPTION>
                <%
                if (deliverableList != null){
	            	for (int i = 0; i < deliverableList.size(); i++) {
	            		ModuleInfo moduleInfo = (ModuleInfo)deliverableList.elementAt(i);
	            		if (moduleInfo != null) {
	            	%> 
	              	<OPTION value="<%=moduleInfo.moduleID%>"><%=moduleInfo.name%></OPTION>
	            	<%
	            		}
	                }
                }
                %>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Type")%> </TD>
            <TD class="CellBGR3">
            <SELECT class="COMBO" name="type" onclick="onTypeChange()">
                <OPTION value="1" selected> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.New")%> </OPTION>
                <OPTION value="2"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Changerequest")%> </OPTION>
	            <%
	            if (isMaintenance) {
	            %>
                <OPTION value="3"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Defect")%> </OPTION>
	            <%
	            }
	            %>
            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Size")%> </TD>
            <TD class="CellBGR3"><SELECT class="COMBO" name="size">
                <OPTION value="1" selected>1</OPTION>
                <OPTION value="2">2</OPTION>
                <OPTION value="3">3</OPTION>
                <OPTION value="4">4</OPTION>
                <OPTION value="5">5</OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Committed")%> </TD>
            <TD class="CellBGR3">
            <INPUT type="hidden" name="status" value="<%=CommonTools.dateFormat(prjStartDate)%>">
            <INPUT size="9" type="text" maxlength="9" name="status">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Designed")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="status">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Coded")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="status">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Tested")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="status">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Deployed")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="status">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Accepted")%> </TD>
            <TD class="CellBGR3">
            <INPUT size="9" type="text" maxlength="9" name="status">(DD-MMM-YY)
			<INPUT type="hidden" name="status" value="<%=CommonTools.dateFormat(new Date())%>">
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Cancelled")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="cancelled">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Requirementsection")%> </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="srs"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Designsection")%> </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="dd"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.CodemoduleFile")%> </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="codeModule"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Testcasesection")%> </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="testCase"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Releasenote")%> </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="releaseNote"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Effortpd")%> </TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="effort"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Elapseddays")%> </TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="elapsedDays"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Previousprojectcode")%> </TD>
	        <%
		        if (!isDevelopment) {
	        %>
            <TD class="CellBGR3"><SELECT class="COMBO" name="prevPrjID">
                <OPTION value="0" selected>        </OPTION>
                <%
                if (projectList != null){
	            	for (int i = 0; i < projectList.size(); i++) {
	            		ProjectDateInfo projectInfo = (ProjectDateInfo)projectList.elementAt(i);
	            		if ((projectInfo != null) && (projectInfo.projectID != projectID)) {
	            	%>
	                <OPTION value="<%=projectInfo.projectID%>"><%=projectInfo.code%></OPTION>
	                <%
	            		}
	                }
                }
                %>
            </SELECT></TD>
	        <%
	        	}
	        	else {
	        %>
	        <TD class="CellBGR3">N/A</TD>
	        <%
	        	}
	        %>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Receiveddate")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="receivedDate"> (DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Responseddate")%> </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" maxlength="9" name="responsedDate"> (DD-MMM-YY) </TD>
        </TR>
    </TBODY>
</TABLE>
<P>
<INPUT type="button" class="BUTTON" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.requirementAddnew.OK")%> " onclick="onOK()">
<INPUT type="button" class="BUTTON" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.requirementAddnew.Cancel")%> " onclick="onCancel()"></P>
</FORM>
<SCRIPT language="javascript">
function onOK() {
	if (trim(frm.name.value) == "") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.Nameofrequirementcannotbeempty")%>");
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
				window.alert("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.StatusdatemustbeintheformatyyyyMMdd")%>");
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
				window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.Status__date__must__be__between__previous__status__dates__or__project__start__date__~PARAM1_START_DATE~__and__next__status__dates__or__today")%>", "<%=CommonTools.dateFormat(prjStartDate)%>")));
				frm.status[i].focus();
				return;
			}
		}
	}
	
	if (trim(frm.cancelled.value) != "") {
		if (!isDate(frm.cancelled.value)) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.CancelleddatemustbeintheformatyyyyMMdd")%>");
			frm.cancelled.focus();
			return;
		}
		if ((compareDate(frm.status[0].value, frm.cancelled.value) < 0) ||
			(compareDate(frm.cancelled.value, frm.status[7].value) < 0)) {
			window.alert(getParamText(new Array('<%= languageChoose.getMessage("fi.jsp.requirementAddnew.Cancelleddatemustbebetweenprojectstartdate~PARAM1_DATE~andtoday")%>', '<%=CommonTools.dateFormat(prjStartDate)%>')));
			frm.cancelled.focus();
			return;
		}
	}
	
	if (trim(frm.effort.value) != "") {
		if (isNaN(frm.effort.value)) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.Effortmustbeaintegernumber")%>");
			frm.effort.focus();
			return;
		}
		else if (parseInt(frm.effort.value) < 0) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.Effortmustbeapositivenumber")%>");
			frm.effort.focus();
			return;
		}
	}
	
	if (trim(frm.elapsedDays.value) != "") {
		if (isNaN(frm.elapsedDays.value)) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.Elapseddaysmustbeafloatnumber")%>");
			frm.elapsedDays.focus();
			return;
		}
		else if (parseInt(frm.elapsedDays.value) < 0) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.Elapseddaysmustbeapositivenumber")%>");
			frm.elapsedDays.focus();
			return;
		}
	}

	if ((trim(frm.receivedDate.value) != "") && (!isDate(frm.receivedDate.value))) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.ReceiveddatemustbeintheformatyyyyMMdd")%>");
		frm.receivedDate.focus();
		return;
	}
	
	if (trim(frm.responsedDate.value) != "") {
		if (!isDate(frm.responsedDate.value)) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.ResponseddatemustbeintheformatyyyyMMdd")%>");
			frm.responsedDate.focus();
			return;
		}
		else if (compareDate(frm.receivedDate.value, frm.responsedDate.value) < 0) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.requirementAddnew.Responseddatemustbeafterreceiveddate")%>");
			frm.responsedDate.focus();
			return;
		}
	}
	frm.action = "Fms1Servlet?reqType=<%=Constants.REQUIREMENT_ADDNEW%>";
	frm.submit();
}
function onCancel() {
	doIt(<%=Constants.REQUIREMENT_LIST_INIT%>);
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
}
function onPageLoad() {
	frm.name.focus();
	onTypeChange();
}
</SCRIPT>
</BODY>
</HTML>