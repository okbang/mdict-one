<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woCustomeMetricUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

</HEAD>
<%LanguageChoose languageChoose =
	(LanguageChoose) session.getAttribute("LanguageChoose");
String sourcePage = (String) session.getAttribute("SourcePage");
%>
<BODY class="BD"
	onload="loadPrjMenu();document.frm_woCMUpdate.woCM_plannedValue.focus();">
<%Vector cmList = (Vector) session.getAttribute("WOCustomeMetricList");
String id = request.getParameter("id");
int vectId = 0;
if (id != null) {
	vectId = Integer.parseInt(id);
	if ((vectId > cmList.size() - 1) || (vectId < 0))
		Fms1Servlet.callPage(
			"error.jsp?error=Bad parameters",
			request,
			response);
} else
	Fms1Servlet.callPage("error.jsp?error=Bad parameters", request, response);
WOCustomeMetricInfo info = (WOCustomeMetricInfo) cmList.elementAt(vectId);
//HuyNH2 add code for project archive
String archiveStatus = (String) session.getAttribute("archiveStatus");
boolean isArchive = false;
if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
	if (Integer.parseInt(archiveStatus) == 4) {
		isArchive = true;
	}
}
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.SpecificObjectives")%>
</P>
<br>
<form name="frm_woCMUpdate" action="Fms1Servlet#custom" method="get"><input
	type="hidden" name="reqType"
	value="<%=Constants.WO_CUS_METRIC_UPDATE%>"> <input type="hidden"
	name="id" value="<%=vectId%>">
<TABLE cellspacing="1" class="Table">
	<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.UpdateSpecificObjectives")%>
	</CAPTION>
	<TBODY>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.Name")%>*
			</TD>
			<TD class="CellBGR3"><INPUT type="text" name="woCM_name"
				value="<%=ConvertString.toHtml(info.name)%>" maxlength="50"
				size="50" style="text-align: left"></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.Unit")%>*
			</TD>
			<TD class="CellBGR3"><INPUT type="text" name="woCM_unit"
				value="<%=ConvertString.toHtml(info.unit)%>" maxlength="50"
				size="50" style="text-align: left"></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%></TD>
			<TD class="CellBGR3"><INPUT type="text" name="woCM_LCL"
				value="<%=(CommonTools.updateDouble(info.LCL) == "")
	? CommonTools.updateDouble(info.plannedValue)
	: CommonTools.updateDouble(info.LCL)%>"
				maxlength="11" size="11" style="text-align: right"></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.TargetedValue")%></TD>
			<TD class="CellBGR3"><INPUT type="text" name="woCM_plannedValue"
				value="<%=CommonTools.updateDouble(info.plannedValue)%>"
				maxlength="11" size="11" style="text-align: right"></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%></TD>
			<TD class="CellBGR3"><INPUT type="text" name="woCM_UCL"
				value="<%=(CommonTools.updateDouble(info.UCL) == "")
	? CommonTools.updateDouble(info.plannedValue)
	: CommonTools.updateDouble(info.UCL)%>"
				maxlength="11" size="11" style="text-align: right"></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.Note")%>
			</TD>
			<TD class="CellBGR3"><TEXTAREA name="woCM_note" rows="4" cols="50"><%=((info.note == null) ? "" : info.note)%></TEXTAREA></TD>
		</TR>

	</TBODY>
</TABLE>
<BR>
<i><b>* Note:</b></i> <i>If USL or LSL is null, default value (=
Average) will be proposed to metric.</i> <BR>

</form>

<form name="frm_woCMDelete" action="Fms1Servlet#custom" method="get"><input
	type="hidden" name="reqType"
	value="<%=Constants.WO_CUS_METRIC_DELETE%>"> <input type="hidden"
	name="id" value="<%=vectId%>"></form>
<br>
<%if (!isArchive) {%>
<INPUT type="button" name="update2"
	value="<%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.Update")%>"
	onclick="javascript:on_Submit1();" class="BUTTON">
<input type="button" name="delete"
	value="<%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.Delete")%>"
	onclick="javascript:on_Submit2();" class="BUTTON">
<%}%>
<input type="button" name="cancel"
	value="<%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.Cancel")%>"
	class="BUTTON" onclick="doCancel()">

</BODY>
</HTML>
<script language="javascript">
	var sourcePage = <%=sourcePage%>;

	function doCancel() {
		if (sourcePage == 1) {
			jumpURL('qualityObjective.jsp');
		}
		else {
			jumpURL('woPerformanceView.jsp');		
		}
	}

	function on_Submit1()
	{
		if(trim(frm_woCMUpdate.woCM_name.value) == ""){
  			alert("<%=languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.YouMustEnterNameOfMetric")%>");
  			frm_woCMUpdate.woCM_name.focus();  		
  			return;  
  		}
		
		if(trim(frm_woCMUpdate.woCM_unit.value) == ""){
  			alert("<%=languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.YouMustEnterUnitOfMetric")%>");		
  			frm_woCMUpdate.woCM_unit.focus();  		
  			return;  
  		}
		if(isNaN(frm_woCMUpdate.woCM_plannedValue.value) && (frm_woCMUpdate.woCM_plannedValue.value != "")){
  			alert("<%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.TheValueMustBeANumber")%>");		
  			frm_woCMUpdate.woCM_plannedValue.focus();  		
  			return;  
  		}
  		
  		if(frm_woCMUpdate.woCM_note.value.length > 200)
		{
			alert(getParamText(new Array('<%=languageChoose.getMessage(
	"fi.jsp.woCustomeMetricUpdate.The__length__of__this__field__must__be__below__200__characters__currently__~PARAM1_NOTELEN~")%>',frm_woCMUpdate.woCM_note.value.length)));
			frm_woCMUpdate.woCM_note.focus();
			return;
		}
		if(isNaN(frm_woCMUpdate.woCM_LCL.value) && (trim(frm_woCMUpdate.woCM_LCL.value) != "")){
  			alert("<%=languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.TheValueMustBeANumber")%>");				
  			frm_woCMUpdate.woCM_LCL.focus();  		
  			return;  
  		}
  		if(isNaN(frm_woCMUpdate.woCM_UCL.value) && (trim(frm_woCMUpdate.woCM_UCL.value) != "")){
  			alert("<%=languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.TheValueMustBeANumber")%>");				
  			frm_woCMUpdate.woCM_UCL.focus();
  			return;  
  		}
  		if(eval(frm_woCMUpdate.woCM_LCL.value) >= eval(frm_woCMUpdate.woCM_plannedValue.value) && frm_woCMUpdate.woCM_plannedValue.value !=""){
  			alert("<%=languageChoose.getMessage(
	"fi.jsp.woCustomeMetricAdd.TheValueOfLCLAverageUCLMustFolowOrder")%>");				
  			frm_woCMUpdate.woCM_plannedValue.focus();  		
  			return;  
  		}
  		if(eval(frm_woCMUpdate.woCM_plannedValue.value) >= eval(frm_woCMUpdate.woCM_UCL.value) && frm_woCMUpdate.woCM_UCL.value !=""){
  			alert("<%=languageChoose.getMessage(
	"fi.jsp.woCustomeMetricAdd.TheValueOfLCLAverageUCLMustFolowOrder")%>");				
  			frm_woCMUpdate.woCM_UCL.focus();
  			return;  
  		}
  		if(eval(frm_woCMUpdate.woCM_LCL.value) >= eval(frm_woCMUpdate.woCM_UCL.value) && frm_woCMUpdate.woCM_UCL.value !=""){
  			alert("<%=languageChoose.getMessage(
	"fi.jsp.woCustomeMetricAdd.TheValueOfLCLAverageUCLMustFolowOrder")%>");				
  			frm_woCMUpdate.woCM_UCL.focus();
  			return;  
  		}

  		
  		
		document.frm_woCMUpdate.submit();
	}
	function on_Submit2()
	{
		if (window.confirm("<%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.AreYouSureToDelete")%>") != 0) {
	  		
			document.frm_woCMDelete.submit();
		}
		else{
			return;
		}	
		
	}		
</script>