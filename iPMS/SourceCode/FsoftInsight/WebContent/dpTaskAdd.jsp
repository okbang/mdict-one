<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>dpTaskAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	String sourcePage = (String) session.getAttribute("SourcePage");
%>

<%	
//	int caller = Integer.parseInt((String)session.getAttribute("caller"));
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtItem.focus();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.dpTaskAdd.Defectpreventiongoals")%> </P>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_DPTASK%>#defectprevention">
<input type="hidden" name="source" value=""> 
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.dpTaskAdd.Defectpreventiontask")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Name1")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="50" type="text" maxlength="50" name="txtItem"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Unit1")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="20" type="text" maxlength="50" name="txtUnit"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtUSL" class="numberTextBox"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtPlan" class="numberTextBox"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtLSL" class="numberTextBox"></TD>
        </TR>
        <TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Basic")%></TD>
			<TD class="CellBGRnews"><TEXTAREA rows="5" cols="60" name="txtCause"></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.DpTaskAdd.OK")%>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.DpTaskAdd.Cancel")%>" onclick="doAction(this)"></FORM>
<SCRIPT language="javascript">

var sourcePage = <%=sourcePage%>;

function doAction(button)
{
  	if (button.name=="btnOk") {
  	
  		if(frm.txtItem.value==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.DpTaskAdd.Thisfieldismandatory")%>");
  		 	frm.txtItem.focus();
  		 	return;
  		}
  		if(frm.txtItem.value.length>100){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.dpTaskAdd.ItemLengthCanNotGreaterThan100")%>");
  		  	frm.txtItem.focus(); 
  		  	return;
  	  	}
  		if(frm.txtUnit.value==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.DpTaskAdd.Thisfieldismandatory")%>");
  		 	frm.txtUnit.focus();
  		 	return;
  		}
  		if(frm.txtUnit.value.length>50){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.dpTaskAdd.UnitLengthCanNotGreaterThan50")%>");
  		  	frm.txtUnit.focus(); 
  		  	return;
  	  	}  		
  		if(frm.txtPlan.value==""){
 			window.alert("<%=languageChoose.getMessage("fi.jsp.DpTaskAdd.Thisfieldismandatory")%>");
  			frm.txtPlan.focus();  		
  			return;  
  		}  	  	
  	  	if(frm.txtPlan.value!="")
  	  	{
  	 	 	if(isNaN(frm.txtPlan.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.DpTaskAdd.Invalidnumberformat")%>");
  				frm.txtPlan.focus();
  				return;
  			}
  	 	 	if(frm.txtPlan.value<=0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.DpTaskAdd.Mustbegreaterthan0")%>");
  				frm.txtPlan.focus();
  				return;
  			}	
  		}	
  		
  		// Add by HaiMM - Start
  		if(frm.txtUSL.value==""){
 			window.alert("<%=languageChoose.getMessage("fi.jsp.DpTaskUpdate.Thisfieldismandatory")%>");
  			frm.txtUSL.focus();  		
  			return;  
  		}
  	  	if(frm.txtUSL.value!="")
  	  	{
  	 	 	if(isNaN(frm.txtUSL.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.DpTaskUpdate.Invalidnumberformat")%>");
  				frm.txtUSL.focus();
  				return;
  			}
  	 	 	if(frm.txtUSL.value<=0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.DpTaskUpdate.Mustbegreaterthan0")%>");
  				frm.txtUSL.focus();
  				return;
  			}	
  		}	

  		if(frm.txtLSL.value==""){
 			window.alert("<%=languageChoose.getMessage("fi.jsp.DpTaskUpdate.Thisfieldismandatory")%>");
  			frm.txtLSL.focus();  		
  			return;  
  		}
  	  	if(frm.txtLSL.value!="")
  	  	{
  	 	 	if(isNaN(frm.txtLSL.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.DpTaskUpdate.Invalidnumberformat")%>");
  				frm.txtLSL.focus();
  				return;
  			}
  	 	 	if(frm.txtLSL.value<=0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.DpTaskUpdate.Mustbegreaterthan0")%>");
  				frm.txtLSL.focus();
  				return;
  			}	
  		}	

		if (!checkContraint(frm.txtUSL, frm.txtPlan, frm.txtLSL)){
			return;
  		}

		// Add by HaiMM - End
  	  		if (frm.txtCause.value.length>300) {
  			window.alert("Cause length can not greater than 300");
  			frm.txtCause.focus();
  			return;
  		}
  	  	
  	  	if (sourcePage == 1) {
			frm.source.value = "1";
		}
  	  	
  	  	frm.submit();  		 	
  	}
  	if (button.name=="btnCancel") {
		if (sourcePage == 1) {
			jumpURL('qualityObjective.jsp');
		}
		else {
			jumpURL('woPerformanceView.jsp');
		}
  		return;
  	}
  	
	function checkContraint(uslObj, averObj, lslObj) {
		var result = true;
		var usl = new Number(uslObj.value);
		var aver = new Number(averObj.value);
		var lsl = new Number(lslObj.value);
	
		if((usl > aver) || (usl > lsl) || (aver > lsl)){
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.ContrainstDP")%>");
			uslObj.focus();
			result = false;
		}
		
		return result;
	}

}
</SCRIPT>
</BODY>
</HTML>