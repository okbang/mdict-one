<%@page import="com.fms1.infoclass.*,com.fms1.tools.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
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
<TITLE>Defectupdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();formPlanDefect.elements[1].focus()">
<%
	final DefectByProcessInfo[] defectProcess = (DefectByProcessInfo[])session.getAttribute("defectProcess");
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.defectupdate.Defects")%> </P>
<BR>
<FORM action="Fms1Servlet" method="post" name="formPlanDefect">
<INPUT type="hidden" name="reqType" value="<%=Constants.UPDATE_DEFECTS%>">
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.Defectupdate.Weightedprereleasereviewdefectsbyprocess")%></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width ="15%"><%=languageChoose.getMessage("fi.jsp.Defectupdate.Process")%></TD>
			<TD width ="15%"><%=languageChoose.getMessage("fi.jsp.Defectupdate.Normofwdfoundbyreview")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.Defectupdate.PlannedFoundByReview")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.Defectupdate.RePlannedFoundByReview")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.Defectupdate.ActualFoundByReview")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.Defectupdate.ReviewDeviation")%>(%)</TD>
		</TR>
		<%
		boolean bl=false;
		String rowStyle;
		DefectByProcessInfo processObj;
		for (int i =0;i<4;i++){
		processObj=defectProcess[i] ;
		if(bl) rowStyle="CellBGRnews";
  		else rowStyle = "CellBGR3";
  		bl=!bl;
		%>
		<TR class="<%=rowStyle%>">
			<TD><%=languageChoose.getMessage(processObj.processName)%>*</TD>
			<TD><%=CommonTools.formatDouble(processObj.normReview)%></TD>
			<TD><INPUT size="11" type="text" maxlength="9" name="<%=processObj.processName+"PlanReview"%>" value="<%=CommonTools.updateDouble(processObj.planReview)%>"></TD>
			<TD><INPUT size="11" type="text" maxlength="9" name="<%=processObj.processName+"RePlanReview"%>" value="<%=CommonTools.updateDouble(processObj.rePlanReview)%>"></TD>
			<TD><%=CommonTools.formatDouble(processObj.actualReview)%></TD>
			<TD><%=CommonTools.formatDouble(processObj.deviationReview)%></TD>
		</TR>
		<%}%>
    </TBODY>
</TABLE>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.Defectupdate.Weightedprereleasetestdefectsbyprocess")%></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width ="15%"><%=languageChoose.getMessage("fi.jsp.Defectupdate.Process")%></TD>
			<TD width ="15%"><%=languageChoose.getMessage("fi.jsp.Defectupdate.Normofwdfoundbytest")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.Defectupdate.PlannedFoundByTest")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.Defectupdate.RePlannedFoundByTest")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.Defectupdate.ActualFoundByTest")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.Defectupdate.TestDeviation")%>(%)</TD>
		</TR>
		<%
		//only for coding now but might evolve
		for (int i =0;i<4;i++){
		processObj=defectProcess[i] ;
		if(bl) rowStyle="CellBGRnews";
  		else rowStyle = "CellBGR3";
  		bl=!bl;
		%>
		<TR class="<%=rowStyle%>">
			<TD><%=languageChoose.getMessage(processObj.processName)%>*</TD>
			<TD><%=CommonTools.formatDouble(processObj.normTest)%></TD>
			<TD><INPUT size="11" type="text" maxlength="9" name="<%=processObj.processName+"PlanTest"%>" value="<%=CommonTools.updateDouble(processObj.planTest)%>"></TD>
			<TD><INPUT size="11" type="text" maxlength="9" name="<%=processObj.processName+"RePlanTest"%>" value="<%=CommonTools.updateDouble(processObj.rePlanTest)%>"></TD>
			<TD><%=CommonTools.formatDouble(processObj.actualTest)%></TD>
			<TD><%=CommonTools.formatDouble(processObj.deviationTest)%></TD>
		</TR>
		<%}%>
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" class="BUTTON" name="ok" value="<%=languageChoose.getMessage("fi.jsp.Defectupdate.OK")%>" onclick="doSubmit();">
<INPUT type="button" class="BUTTON" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.defectupdate.Cancel")%>" onclick="doIt(<%=Constants.DEFECT_VIEW%>)">
<P></P>
</FORM>
<SCRIPT language="javascript">

function doSubmit(){
	//first element is reqtype, the last 2 are the buttons
	
	for (z=1;z<formPlanDefect.elements.length-2;z++){
		if(!positiveFld(formPlanDefect.elements[z],"<%=languageChoose.getMessage("fi.jsp.defectupdate.thisfield")%>"))
			return;

		if (((z%2)==0)&&(z!=1)&&(trim(formPlanDefect.elements[z].value) !="")&&(trim(formPlanDefect.elements[z-1].value) =="")){
			window.alert("<%=languageChoose.getMessage("fi.jsp.Defectupdate.PlanRePlan")%>");
			formPlanDefect.elements[z-1].focus();
			return;
		}

	}
	
	for (z=0;z<formPlanDefect.elements.length-3;z++){
		z = z + 1;
		if(trim(formPlanDefect.elements[z].value) == "") {
			window.alert("<%=languageChoose.getMessage("fi.jsp.defectupdate.ThisValueCannotBeEmpty")%>");
			formPlanDefect.elements[z].focus();
			return;
		}
	}
	
	formPlanDefect.submit();
}



</SCRIPT>
</BODY>

</HTML>
