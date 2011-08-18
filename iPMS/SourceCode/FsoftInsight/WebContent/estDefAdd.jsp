<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*"%>
<%
	response.setHeader("Content-Type", "text/html; charset=UTF-8");
%>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	int right2=Security.securiPage("Requirements",request,response);

	
	int maxRow = 10;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="stylesheet/AutoComplete.css" media="screen" type="text/css">
<script language="javascript" type="text/javascript" src="jscript/autocomplete.js"></script>
<TITLE>estDefAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<BODY class="BD" onload="loadPrjMenu();">

<P class="TITLE">Estimate Defect</P>
 <BR>
<BR>
<form name='frm' action ='Fms1Servlet' method='POST' >
<TABLE cellspacing="1" class="Table" width="50%">
<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.estDefectView.EstimateDefectTitle")%></CAPTION>

<TR class="ColumnLabel">
	<TD align="center" width ="3%" >#</TD>
		<TD width ="20%"><%=languageChoose.getMessage("fi.jsp.estDefectView.ReviewTest")%>*</TD>
		<TD width ="15%"><%=languageChoose.getMessage("fi.jsp.estDefectView.Target")%>*</TD>
		<TD width ="30%"><%=languageChoose.getMessage("fi.jsp.estDefectView.Basic")%></TD>
</TR>
<%
boolean myflag=false;
String color=null;

for (int i=0;i<maxRow;i++){
	color=myflag?"CellBGRnews":"CellBGR3";
%>
	<TR class='<%=color%>'>
		<TD align="center"><%=i+1%></TD>
		<TD> <INPUT size="30" type="text" maxlength="100" name="reviewTest" id="reviewTest<%=i%>" value=""> </TD>
		<TD> <INPUT size="13" type="text" maxlength="10" name="target" id="target<%=i%>" value=""> </TD>
		<TD> <TEXTAREA rows="3" cols="40" name="basic" ></TEXTAREA> </TD>
	</TR>
<%}%>
</TABLE>
<P>
<INPUT type="hidden" name="reqType" value="">
<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Save")%> " onclick="saveEstDefect()">
<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Cancel")%> " onclick="jumpURL('qualityObjective.jsp');">

</FORM>

<SCRIPT language="javascript">

function saveEstDefect(){
	if (isValidData()) {
		frm.reqType.value="<%=Constants.EST_DEFECT_ADD%>";
		frm.submit();
	}
}

function isValidData(){
	var basic, target, item;
	var maxRow = <%=maxRow%>;
	
	for (i = 0; i < maxRow; i++) {
		item = frm.reviewTest[i];
		basic = frm.basic[i];
		target = frm.target[i];
		
		if (item.value.length > 0) {
			if(!positiveFld(target,"<%=languageChoose.getMessage("fi.jsp.defectupdate.thisfield")%>"))
				return false;
			
			if(trim(target.value) == "") {
				window.alert("<%=languageChoose.getMessage("fi.jsp.defectupdate.ThisValueCannotBeEmpty")%>");
				target.focus();
				return;
			}
	
			if (basic.value.length > 600) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.estDefectUpdate.Thetextfornotecannotbe")%>");
				basic.focus();
				return false;
			}
		}
		
		if (item.value.length == 0) {
			if((trim(target.value).length > 0) || (trim(basic.value).length > 0)) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.defectupdate.ThisValueCannotBeEmpty")%>");
				item.focus();
				return false;
			}
		
		}
	}
	return true;
}

<!--
    data = ['Requirements review','Design review','Code review',
            'Unit Testing','Integration & System testing','Acceptance testing'];
	
<%
	for (int i = 0; i < maxRow; i++) {
		out.write("AutoComplete_Create('reviewTest" + i + "', data);");
	}
%>	
// -->


</SCRIPT>
</BODY>
</HTML>
