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
<TITLE>estDefectUpdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();formPlanDefect.elements[1].focus()">
<%
	final DefectByProcessInfo[] defectProcess = (DefectByProcessInfo[])session.getAttribute("defectProcess");
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.estDefectView.EstimateDefect")%> </P>
<BR>
<FORM action="Fms1Servlet#EstimateDefect" method="post" name="formPlanDefect">
<INPUT type="hidden" name="reqType" value="<%=Constants.UPDATE_EST_DEFECT%>">
<TABLE cellspacing="1" class="Table" width="50%">
<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.estDefectView.UpdateEstDefTitle")%></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width ="20%"><%=languageChoose.getMessage("fi.jsp.estDefectView.ReviewTest")%></TD>
			<TD width ="15%"><%=languageChoose.getMessage("fi.jsp.estDefectView.Target")%></TD>
			<TD width ="30%"><%=languageChoose.getMessage("fi.jsp.estDefectView.Basic")%></TD>
		</TR>
		<%
		boolean bl=false;
		String rowStyle;
		DefectByProcessInfo processObj;
  		double planValue = Double.NaN;
  		String note = "";
		
		for (int i =0;i<6;i++){
		processObj=defectProcess[i] ;
		if(bl) rowStyle="CellBGRnews";
  		else rowStyle = "CellBGR3";
  		bl=!bl;
  		
		planValue = (i <=2) ? processObj.planReview: processObj.planTest;
		note = (i <=2) ? processObj.noteReview: processObj.noteTest;
		%>
		<TR class="<%=rowStyle%>">
			<TD><%=languageChoose.getMessage(processObj.processName)%>*</TD>
			<TD><INPUT size="20" type="text" maxlength="9" name="planValue" value="<%=CommonTools.updateDouble(planValue)%>"></TD>
			<TD> <TEXTAREA rows="4" cols="40" name="note" ><%=ConvertString.toHtml(note)%></TEXTAREA> </TD>
		</TR>
		<%}%>
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" class="BUTTON" name="ok" value="<%=languageChoose.getMessage("fi.jsp.Defectupdate.OK")%>" onclick="doSubmit();">
<INPUT type="button" class="BUTTON" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.defectupdate.Cancel")%>" onclick="jumpURL('qualityObjective.jsp')">
<P></P>
</FORM>
<SCRIPT language="javascript">

function doSubmit(){
	var note;
	var planValue;

	for (i = 0; i < 6; i++) {
		note = formPlanDefect.note[i];
		planValue = formPlanDefect.planValue[i];

		if(!positiveFld(planValue,"<%=languageChoose.getMessage("fi.jsp.defectupdate.thisfield")%>"))
			return;
		
		if(trim(planValue.value) == "") {
			window.alert("<%=languageChoose.getMessage("fi.jsp.defectupdate.ThisValueCannotBeEmpty")%>");
			planValue.focus();
			return;
		}

		if (note.value.length > 600) {
			window.alert("<%=languageChoose.getMessage("fi.jsp.estDefectUpdate.Thetextfornotecannotbe")%>");
			note.focus();
			return;
		}
	}
	formPlanDefect.submit();
}



</SCRIPT>
</BODY>

</HTML>
