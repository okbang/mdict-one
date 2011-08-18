<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>requirementList.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
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
	int right = Security.securiPage("Requirements",request,response);
	Vector requirementList = (Vector)request.getAttribute("requirementList");
	ProjectInfo projectInfo = (ProjectInfo)request.getAttribute("projectInfo");
	String lifecycle = projectInfo.getLifecycle();
	Vector deliverableList = (Vector)request.getAttribute("deliverableList");
	String deliverableId = (String) request.getAttribute("deliverableId");
	if (deliverableId == null) {
		deliverableId = "0";
	}
	long lDeliverableId = Long.parseLong(deliverableId);
	int j = requirementList.size();
	if (j >= 20){%>
		<BODY onload="loadPrjMenu();makeScrollableTable('tableReq',true,'auto');javascript:Import();" class="BD">
	<%} else {%>
		<BODY class="BD" onload="loadPrjMenu()">
	<%}%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.requirementList.RequirementsStatusandtraceabil")%> </P>

<br>
<!--
<TABLE cellspacing="1" class="HDR" width="100%">
	<TBODY>
		<TR>
			<TD width="10%"></TD>
			<TD></TD>
			<TD align="right"><P align=right><%if(right == 3 && !isArchive){%><a href="javascript:Import();"><%=languageChoose.getMessage("fi.jsp.requirementList.ImportRequirement")%></a><%}%></p></TD>
		</TR>
	</TBODY>
</TABLE>
-->
<br>
<FORM name="frm_Import" action="Fms1Servlet?reqType=<%=Constants.REQUIREMENT_IMPORT%>" enctype="MULTIPART/FORM-DATA" method=POST >
<TABLE cellspacing="1" class="HDR" width="95%" id="ImportTable">
	<TBODY>
		<TR>
			<TD align="right"><STRONG>File Name&nbsp;*</STRONG></TD>
	        <TD align="left">
	        	<INPUT type="file" name="importFile">
	       		<INPUT type="button" name="Import" onclick="checkName()" value=" Import " class="Button">
            	<INPUT type="hidden" name="filename">
	        </TD>
	        </TR>
	        <TR>
	        	<TD></TD>
	        	<TD align="center"><A href="Template_Import_REQUIREMENT.xls"><FONT color="blue" class="label1">Download Template File</FONT></A></TD>
	        </TR>
	</TBODY>
</TABLE>
</FORM>

<BR>

<%
String checkImport = (String)session.getAttribute("Imported");
if(checkImport == "true"){
	int[] result = (int[])session.getAttribute("AddedRecord");
	int importedOK = 0;
	int importedFail = 0;
	int l = 0;
	while(l < 50){
			if(result[l] > 0){
				importedOK ++;
			}
			l++;
		}
	if(importedOK > 0){
	%><br>Imported successfull records:<%
		l=0;
		importedOK =0;
		importedFail =0;
		while(l < 50){
			if(result[l] > 0){
				if(importedOK > 0){
					%> ,<%
				}
				%>
				<%=result[l]%>
				<%
				importedOK ++;
			}else if(result[l] < 0){
				importedFail++;
			}
			l++;
		}
}	
	if(importedOK == 0){
	%><br>0 record is imported
	  <br>
	  <br>
	<%
	}
	if(importedFail > 0 && importedOK > 0){
		%><br>There are some records is failed 
		<%
	}
	
	String error = (String)session.getAttribute("error");
	String errorCancel = (String)session.getAttribute("errorCancel");
	if(error != null || errorCancel != null){%><br>because one of these reasons :<%}%>
	  <%if(error != null){
	  	%><BR><%=error%><%
	  	session.removeAttribute("error");
	  }%>
	  <%if(errorCancel != null){
	  	%><BR><%=errorCancel%><%
	  	session.removeAttribute("errorCancel");	
	  }
	session.removeAttribute("Imported");
	session.removeAttribute("AddedRecord");
}
%>
<%
String isImport = (String)session.getAttribute("ImportFail");
if(isImport == "fail"){
	%><br><p style="color: red;">Import Fail<%	
	session.removeAttribute("ImportFail");
}
%>

<TABLE class="Table" width="98%" cellspacing="1" id="tableReq">
    <COL span="1" width="24">
	<THEAD>
        <TR class="ColumnLabel">
            <TD style="text-align: center">#</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementList.Requirement")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementList.Deliverable")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementList.Completenessrate")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementList.Type")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementList.Size")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.requirementList.Status")%></TD>
        </TR>
	</THEAD>
    <TBODY>
        <%
        if (requirementList != null)
        for (int i = 0; i < j; i++) {
        	String className;
        	RequirementInfo reqInfo = (RequirementInfo)requirementList.elementAt(i);
			String typeName = "N/A";
			switch (reqInfo.type) {
				case 1: typeName = languageChoose.getMessage("fi.jsp.requirementList.New");
						break;
				case 2: typeName = languageChoose.getMessage("fi.jsp.requirementList.Changerequest");
						break;
				case 3: typeName = languageChoose.getMessage("fi.jsp.requirementList.Defect");
						break;
			}
			className = (i%2 == 0) ? "CellBGRnews":"CellBGR3";
        %>
            <TR class="<%=className%>">
            <TD style="text-align: center"><%=i + 1%></TD>
            <TD><A href="Fms1Servlet?reqType=<%=Constants.REQUIREMENT_DETAIL%>&requirementID=<%=reqInfo.requirementID%>"><%=reqInfo.name%></A></TD>
            <TD><%=reqInfo.moduleName != null ? reqInfo.moduleName : "N/A"%></TD>
            <TD><%=reqInfo.completenessRate >= 0 ?  CommonTools.formatDouble(reqInfo.completenessRate) : "N/A"%></TD>
            <TD><%=typeName%></TD>
            <TD><%=reqInfo.size%></TD>
            <TD><%=reqInfo.statusName != null ? languageChoose.getMessage(reqInfo.statusName) : "N/A"%></TD>
        </TR>
		<%
		}
		%>
    </TBODY>
</TABLE>
<FORM name="frm" action="Fms1Servlet" method="post">
<INPUT type="hidden" name="reqType" value="<%=Constants.REQUIREMENT_ADDNEW_PREP%>" >
<P>
<%if (right == 3 && !isArchive){%>
<INPUT type="button" class="BUTTON" name="AddNew" value=" <%=languageChoose.getMessage("fi.jsp.requirementList.Addnew")%> " onclick="onAdnew()">
<INPUT type="button" class="BUTTON" name="UpdateStatus" value=" <%=languageChoose.getMessage("fi.jsp.requirementList.Reqstatus")%> " onclick="onReqStatus()">
<INPUT type="button" class="BUTTON" name="BatchEdit" value=" <%=languageChoose.getMessage("fi.jsp.requirementList.Batchedit")%> " onclick="onBatchEdit()">
<%}%>

<INPUT type="button" class="BUTTON" name="GroupBy" value=" <%=languageChoose.getMessage("fi.jsp.requirementList.Groupby")%> " onclick="onGroup()">
<SELECT class="COMBO" name="deliverable">
    <OPTION value="0" selected> <%=languageChoose.getMessage("fi.jsp.requirementList.Alldeliverables")%> </OPTION>
    <%
    if (deliverableList != null)
	for (int i = 0; i < deliverableList.size(); i++) {
		ModuleInfo moduleInfo = (ModuleInfo)deliverableList.elementAt(i);
		if (moduleInfo != null) {
			if (moduleInfo.moduleID == lDeliverableId) {
	%> 
  	<OPTION value="<%=moduleInfo.moduleID%>" selected><%=moduleInfo.name%></OPTION>
			<%}else {%> 
  	<OPTION value="<%=moduleInfo.moduleID%>"><%=moduleInfo.name%></OPTION>
	<%		}
		}
    }
    %>
</SELECT>
</P>
</FORM>
<SCRIPT language="javascript">
var isImportHide = true;
	function Import(){
		isImportHide = !isImportHide;
	 	var ImportTable = document.getElementById("ImportTable");
  		if (isImportHide) {
    		ImportTable.style.display="";
   		}else{
    		ImportTable.style.display="none";
		}
    }
</SCRIPT>

<SCRIPT language="javascript">
function doImport(ext) {
    if (ext != '') {
        document.frm_Import.submit();
    }
}

function checkName() {
    var name = document.frm_Import.importFile.value;
    
    if (name == "" || name == null) {
        alert("Select MS Excel file!");
        document.frm_Import.importFile.focus();
        return;
    }
    else {
        var ext = name.substring(name.lastIndexOf(".") + 1, name.length);
        ext = ext.toLowerCase();
        if (ext != "xls") {
            alert("Select MS Excel file!");
            document.frm_Import.importFile.focus();
            return;
        }
        else {
            doImport(ext);
        }
    }
}
</SCRIPT>

<SCRIPT language="javascript">

function onAdnew() {
	frm.reqType.value = <%=Constants.REQUIREMENT_ADDNEW_PREP%>;
	frm.submit();	
}
function onReqStatus() {
	frm.reqType.value = <%=Constants.REQUIREMENT_STATUS%>;
	frm.submit();	
}
function onBatchEdit() {
	frm.reqType.value = <%=Constants.REQUIREMENT_BATCH_EDIT%>;
	frm.submit();	
}
function onGroup() {
	frm.reqType.value = <%=Constants.REQUIREMENT_GROUP_BY%>;
	frm.submit();	
}
</SCRIPT>
<SCRIPT>
	Import();
</SCRIPT>
</BODY>
</HTML>
