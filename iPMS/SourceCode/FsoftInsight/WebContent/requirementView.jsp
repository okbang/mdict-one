<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
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
<TITLE>requirementView.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	int right = Security.securiPage("Requirements",request,response);
	Vector requirementList = (Vector)request.getAttribute("requirementList");
	long reqID = Long.parseLong(request.getParameter("requirementID"));
	RequirementInfo reqInfo = null;
	for (int i = 0; i < requirementList.size(); i++){
		reqInfo = (RequirementInfo)requirementList.elementAt(i);
		if (reqInfo.requirementID == reqID)
			break;
	}
	String deliverable = reqInfo.moduleName != null ? reqInfo.moduleName : "N/A";
	String type = "N/A";
	switch (reqInfo.type) {
		case 1: type = languageChoose.getMessage("fi.jsp.requirementView.New");
				break;
		case 2: type = languageChoose.getMessage("fi.jsp.requirementView.Changerequest");
				break;
		case 3: type = languageChoose.getMessage("fi.jsp.requirementView.Defect");
				break;
	}
	String size = reqInfo.size > 0 ? String.valueOf(reqInfo.size) : "N/A";
	String committed = reqInfo.committedDate != null ? CommonTools.dateFormat(reqInfo.committedDate) : "N/A";
	String designed = reqInfo.designedDate != null ? CommonTools.dateFormat(reqInfo.designedDate) : "N/A";
	String coded = reqInfo.codedDate != null ? CommonTools.dateFormat(reqInfo.codedDate) : "N/A";
	String tested = reqInfo.testedDate != null ? CommonTools.dateFormat(reqInfo.testedDate) : "N/A";
	String deployed = reqInfo.deployedDate != null ? CommonTools.dateFormat(reqInfo.deployedDate) : "N/A";
	String accepted = reqInfo.acceptedDate != null ? CommonTools.dateFormat(reqInfo.acceptedDate) : "N/A";
	String cancelled = reqInfo.cancelledDate != null ? CommonTools.dateFormat(reqInfo.cancelledDate) : "N/A";
	String receivedDate = reqInfo.receivedDate != null ? CommonTools.dateFormat(reqInfo.receivedDate) : "N/A";
	String responsedDate = reqInfo.responseDate != null ? CommonTools.dateFormat(reqInfo.responseDate) : "N/A";
	String responsedTime = (reqInfo.responseTime >= 0 && reqInfo.receivedDate != null && reqInfo.responseDate != null) ? String.valueOf(reqInfo.responseTime) : "N/A";
	String completenessRate = reqInfo.completenessRate >= 0 ? CommonTools.formatDouble(reqInfo.completenessRate) : "N/A";
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.requirementView.Requirements")%> </P>
<BR>
<FORM name="frm" action="Fms1Servlet">
<TABLE class="Table" cellspacing="1" width="95%">
    <COL span="1" width="160">
    <COL span="1" width="400">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.requirementView.Requirementview")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Requirement")%> </TD>
            <TD class="CellBGR3"><%=reqInfo.name%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Deliverable")%> </TD>
            <TD class="CellBGR3"><%=deliverable%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Completenessrate")%> </TD>
            <TD class="CellBGR3"><%=completenessRate%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Type")%> </TD>
            <TD class="CellBGR3"><%=type%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Size")%> </TD>
            <TD class="CellBGR3"><%=size%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Committed")%> </TD>
            <TD class="CellBGR3"><%=committed%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Designed")%> </TD>
            <TD class="CellBGR3"><%=designed%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Coded")%> </TD>
            <TD class="CellBGR3"><%=coded%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Tested")%> </TD>
            <TD class="CellBGR3"><%=tested%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Deployed")%> </TD>
            <TD class="CellBGR3"><%=deployed%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Accepted")%> </TD>
            <TD class="CellBGR3"><%=accepted%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Cancelled")%> </TD>
            <TD class="CellBGR3"><%=cancelled%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Requirementsection")%> </TD>
            <TD class="CellBGR3"><%=reqInfo.requirementSection != null ? reqInfo.requirementSection : "N/A"%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Designsection")%> </TD>
            <TD class="CellBGR3"><%=reqInfo.detailDesign != null ? reqInfo.detailDesign : "N/A"%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.CodemoduleFile")%> </TD>
            <TD class="CellBGR3"><%=reqInfo.codeModuleName != null ? reqInfo.codeModuleName : "N/A"%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Testcasesection")%> </TD>
            <TD class="CellBGR3"><%=reqInfo.testCase != null ? reqInfo.testCase : "N/A"%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Releasenote")%> </TD>
            <TD class="CellBGR3"><%=reqInfo.releaseNote != null ? reqInfo.releaseNote : "N/A"%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Effortpd")%> </TD>
            <TD class="CellBGR3"><%=reqInfo.effort >= 0 ? CommonTools.formatDouble(reqInfo.effort) : "N/A"%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Elapseddays")%> </TD>
            <TD class="CellBGR3"><%=reqInfo.elapsedDay >= 0 ? CommonTools.formatDouble(reqInfo.elapsedDay) : "N/A"%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Previousprojectcode")%> </TD>
            <TD class="CellBGR3"><%=reqInfo.prevPrjName != null ? reqInfo.prevPrjName : "N/A"%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Receiveddate")%> </TD>
            <TD class="CellBGR3"><%=receivedDate%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Responsedate")%> </TD>
            <TD class="CellBGR3"><%=responsedDate%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.requirementView.Responsetimedays")%> </TD>
            <TD class="CellBGR3"><%=responsedTime%></TD>
        </TR>
    </TBODY>
</TABLE>
<P>
<%
if(right == 3 && !isArchive){
%>
<INPUT type="button" class="BUTTON" name="update" value=" <%=languageChoose.getMessage("fi.jsp.requirementView.Update")%> " onclick="onUpdate()">
<INPUT type="button" class="BUTTON" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.requirementView.Delete")%> " onclick="onDelete()">
<%}%>
<INPUT type="button" class="BUTTON" name="back" value=" <%=languageChoose.getMessage("fi.jsp.requirementView.Back")%> " onclick="doIt(<%=Constants.REQUIREMENT_LIST_INIT%>);">

</P>
<INPUT type="hidden" name="reqType" value="<%=Constants.REQUIREMENT_UPDATE_PREP%>">
<INPUT type="hidden" name="requirementID" value="<%=reqInfo.requirementID%>">
</FORM>
<SCRIPT language="javascript">
	function onUpdate() {
		frm.reqType.value = <%=Constants.REQUIREMENT_UPDATE_PREP%>;
		frm.submit();	
	}
	
	function onDelete() {
		if (window.confirm("<%= languageChoose.getMessage("fi.jsp.requirementView.Areyousuretodeletethisrequirement")%>") != 0) {
			frm.reqType.value = <%=Constants.REQUIREMENT_DELETE%>;
			frm.submit();
		}
	}
	
	function onBack() {
		doIt(<%=Constants.REQUIREMENT_LIST_INIT%>);
	}
</SCRIPT>
</BODY>
</HTML>