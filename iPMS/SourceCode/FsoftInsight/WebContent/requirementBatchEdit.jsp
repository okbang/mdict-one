<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>requirementBatchEdit.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	Vector deliverableList = (Vector)request.getAttribute("deliverableList");
	Vector requirementList = (Vector)request.getAttribute("requirementList");	
	ProjectInfo prjInfo = (ProjectInfo)request.getAttribute("projectInfo");
	
	boolean isMaintenance = (prjInfo.getLifecycle().equals("Maintenance"));
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Requirements")%> </P>
<BR>
<FORM name="frm" action="Fms1Servlet" method="post">
<TABLE  class="Table" cellspacing="1" width="1200">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Batchedit")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD style="text-align: center">#</TD>
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Requirement")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Deliverable")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Type")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Size")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Requirementsection")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Designsection")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.CodemoduleFile")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Testcasesection")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Releasenote")%> </TD>
        </TR>
        <%
        if (requirementList != null)
        for (int i = 0; i < requirementList.size(); i++) {
        	String className;
        	RequirementInfo reqInfo = (RequirementInfo)requirementList.elementAt(i);
			if (i%2 == 0) 
				className = "CellBGRnews";
			else 
				className = "CellBGR3";
        %>
            <TR class="<%=className%>">
            <TD style="text-align: center"><%=i + 1%></TD>
            <TD class="<%=className%>"><%=reqInfo.name%></TD>
            <TD class="CellBGR3">
            <SELECT class="COMBO" name="deliverable">
                <OPTION value="0" selected> </OPTION>
                <%
                if (deliverableList != null)
            	for (int j = 0; j < deliverableList.size(); j++) {
            		ModuleInfo moduleInfo = (ModuleInfo)deliverableList.elementAt(j);
            		if (moduleInfo != null) {
            	%> 
              	<OPTION value="<%=moduleInfo.moduleID%>" <%if (moduleInfo.name.equalsIgnoreCase(reqInfo.moduleName)) {%>selected<%}%>><%=moduleInfo.name%></OPTION>
            	<%
            		}
                }
                %>
            </SELECT>
            </TD>
            <TD class="CellBGR3">
            <SELECT class="COMBO" name="type" id="type">
                <OPTION value="1" <%if (reqInfo.type == 1) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.New")%> </OPTION>
                <OPTION value="2" <%if (reqInfo.type == 2) {%>selected<%}%>> CR </OPTION>
	            <%
	            if (isMaintenance) {
	            %>
                <OPTION value="3" <%if (reqInfo.type == 3) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Defect")%> </OPTION>
	           <%}%>
            </SELECT>
            </TD>
            <TD class="CellBGR3">
            <SELECT class="COMBO" name="size">
                <OPTION value="1" <%if (reqInfo.size == 1) {%>selected<%}%>>1</OPTION>
                <OPTION value="2" <%if (reqInfo.size == 2) {%>selected<%}%>>2</OPTION>
                <OPTION value="3" <%if (reqInfo.size == 3) {%>selected<%}%>>3</OPTION>
                <OPTION value="4" <%if (reqInfo.size == 4) {%>selected<%}%>>4</OPTION>
                <OPTION value="5" <%if (reqInfo.size == 5) {%>selected<%}%>>5</OPTION>
            </SELECT>
            </TD>
            <TD class="<%=className%>"><INPUT size="20" type="text" maxlength="300" name="srs" value='<%=reqInfo.requirementSection != null ? reqInfo.requirementSection : ""%>'></TD>
			<TD class="<%=className%>"><INPUT size="20" type="text" maxlength="300" name="dd" value='<%=reqInfo.detailDesign != null ? reqInfo.detailDesign : ""%>'></TD>
			<TD class="<%=className%>"><INPUT size="20" type="text" maxlength="50" name="codeModule" value='<%=reqInfo.codeModuleName != null ? reqInfo.codeModuleName : ""%>'></TD>
			<TD class="<%=className%>"><INPUT size="20" type="text" maxlength="150" name="testCase" value='<%=reqInfo.testCase != null ? reqInfo.testCase : ""%>'></TD>
			<TD class="<%=className%>"><INPUT size="20" type="text" maxlength="300" name="releaseNote" value='<%=reqInfo.releaseNote != null ? reqInfo.releaseNote : ""%>'></TD>
        </TR>
		<INPUT type="hidden" name="requirementID" value="<%=reqInfo.requirementID%>">
		<%}%>
    </TBODY>
</TABLE>
<P>
<INPUT type="button" class="BUTTON" name="Ok" value=" <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.OK")%> " onclick="onOK()">
<INPUT type="button" class="BUTTON" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.requirementBatchEdit.Cancel")%> " onclick="onCancel()">
</P>
</FORM>
<SCRIPT language="javascript">
	function onOK(){
		frm.action = "Fms1Servlet?reqType=<%=Constants.REQUIREMENT_BATCH_UPDATE%>";
		frm.submit();
	}
	function onCancel() {
		doIt(<%=Constants.REQUIREMENT_LIST_INIT%>);
	}
</SCRIPT>
</BODY>
</HTML>