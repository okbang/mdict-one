<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*"%>
<%
	response.setHeader("Content-Type", "text/html; charset=UTF-8");
%>
<%
	LanguageChoose RDBL_languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	int right2=Security.securiPage("Requirements",request,response);
	Vector stageList2=(Vector)session.getAttribute("stageList");

	StageInfo stinf;
	
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
<TITLE>estEffProcessAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<BODY class="BD" onload="loadPrjMenu();">

<P class="TITLE"><%=RDBL_languageChoose.getMessage("fi.jsp.estEffort.EstEffortTitle")%></P>
 <BR>
<BR>
<form name='frm' action ='Fms1Servlet' method='POST' >
<TABLE class="Table" cellspacing="1">
<CAPTION class="TableCaption"><%=RDBL_languageChoose.getMessage("fi.jsp.menuProject.EstEffort")%></CAPTION>

<TR class="ColumnLabel">
	<TD>#</TD>
	<TD> <%=RDBL_languageChoose.getMessage("fi.jsp.estEffort.ActProcess")%> </TD>
	<%for (int i=0;i<stageList2.size();i++){
		stinf=(StageInfo)stageList2.elementAt(i);
	%><TD><%=stinf.stage%></TD>
	<%}%>
</TR>
<%
boolean myflag=false;
String color=null;

for (int i=0;i<maxRow;i++){
	color=myflag?"CellBGRnews":"CellBGR3";
%>
	<TR class='<%=color%>'>
		<TD><%=i+1%></TD>
		<TD> <INPUT size="25" type="text" maxlength="100" name="process" id="process<%=i%>" value=""> </TD>
		<%
		for (int j=0;j<stageList2.size();j++){
		%>
			<TD class="CellBGRnews"><input size="7" type="text" maxlength="10" id ='row<%=i%>stage<%=j%>' name='plan<%=i%><%=j%>' value=""></TD>
		<%}%>
	</TR>
<%}%>
</TABLE>
<P>
<INPUT type="hidden" name="reqType" value="">
<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Save")%> " onclick="saveEstEffort()">
<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Cancel")%> " onclick="jumpURL('estEffProcess.jsp');">

</FORM>

<SCRIPT language="javascript">

function saveEstEffort(){
	if (isValidData()) {
		frm.reqType.value="<%=Constants.EST_EFFORT_ADD%>";
		frm.submit();
	}
}

function isValidData(){
	var fieldPercent;
	var process;
	var max = <%=maxRow%>;
	for (var i=0;i < max;i++){
		var max1 = <%=stageList2.size()%>;
		process = trim(frm.process[i].value).length;
		if(process > 0) {
			for (var j=0;j < max1;j++){
			fieldPercent=document.all['row'+i+'stage'+j];
				if (fieldPercent){
					if(fieldPercent.value!=null){
						if (!mandatoryFld(fieldPercent,"<%= RDBL_languageChoose.getMessage("fi.jsp.estEffort.EstEffort")%>") ||!positiveFld(fieldPercent,"<%= RDBL_languageChoose.getMessage("fi.jsp.estEffort.EstEffort")%>"))
							return false;
					}
				}	
			}

		}
		if(process <= 0) {
			for (var j=0;j < max1;j++){
			fieldPercent=document.all['row'+i+'stage'+j];
				if (fieldPercent){
					if(fieldPercent.value.length > 0){
						if (!mandatoryFld(frm.process[i],"<%= RDBL_languageChoose.getMessage("fi.jsp.finance.FinanceTitle")%>"))
							return false;
					}
				}	
			}
		}
	}
	return true;
}

<!--
    data = ['Requirement','Design','Coding',
            'Unit Testing','Testing','Deployment',
            'Support for Acceptance Test','Project Planning',
            'Project monitoring','Quality Assurance','Training','Others'];
	
<%
	for (int i = 0; i < maxRow; i++) {
		out.write("AutoComplete_Create('process" + i + "', data);");
	}
%>	
// -->


</SCRIPT>
</BODY>
</HTML>
