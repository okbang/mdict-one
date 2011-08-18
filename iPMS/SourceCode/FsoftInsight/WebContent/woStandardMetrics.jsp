<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
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
	<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>

	<style type="text/css">
	
	.ajaxtooltip{
	position: absolute; /*leave this alone*/
	display: none; /*leave this alone*/
	width: 300px;
	left: 0; /*leave this alone*/
	top: 0; /*leave this alone*/
	background: lightyellow;
	border: 2px solid gray;
	border-width: 1px 2px 2px 1px;
	padding: 5px;
	}
	
	</style>
	
	<script type="text/javascript" src="jscript/ajaxtooltip.js">
	
	/***********************************************
	* Ajax Tooltip script- by JavaScript Kit (www.javascriptkit.com)
	* This notice must stay intact for usage
	* Visit JavaScript Kit at http://www.javascriptkit.com/ for this script and 100s more
	***********************************************/
	
	</script>
	<TITLE>woStandardMetrics.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	String sourcePage = (String) session.getAttribute("pageSourceQuality");
%>
<BODY onload="loadPrjMenu()" class="BD"> 
<%
Vector perfVector = (Vector) session.getAttribute("WOPerformanceVector");
Vector stdMetrics = (Vector) session.getAttribute("WOStandardMetricMatrix");

%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woStandardMetrics.WorkorderStandardMetrics")%> </P>
<form name="frm_woSMUpdate" action="Fms1Servlet?reqType=<%=Constants.WO_STANDARD_METRIC_UPDATE%>#StandardMetrics" method="post">
<input type="hidden" name="source" value=""> 
<%--<INPUT type = "hidden" name ="qualityCost" value =<%=((MetricInfo)perfVector.elementAt(7)).plannedValue%> >
--%>
<br>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woStandardMetrics.StandardMetricList")%> </CAPTION>
    <TBODY>
       	<TR class="ColumnLabel">
	        <TD rowspan="2" align="left" width="25%"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Name")%> </TD>
	        <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Unit")%> </TD>
        	<TD rowspan="1" colspan="3" align="center"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Norm")%> </TD>		
	        <TD rowspan="1" colspan="3" align="center"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Target")%>* </TD>    	
	        <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.ActualValue")%>* </TD>
	        <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Deviation")%> </TD>            
	    	<TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Basic")%> </TD>
		</TR>
	    <TR class="ColumnLabel">
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LCL")%>&nbsp;&nbsp;</TD>
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>&nbsp;&nbsp;</TD>
	    	<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.UCL")%>&nbsp;&nbsp;</TD>
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%>&nbsp;&nbsp;</TD>
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>&nbsp;&nbsp;</TD>
	    	<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>&nbsp;&nbsp;</TD>
        </TR>
<%
String className;
for(int i = 0; i < stdMetrics.size() - 1; i++){
	NormInfo normInfo =(NormInfo)stdMetrics.elementAt(i);
	className=(i%2==0)?"CellBGRnews":"CellBGR3";
if (i ==0) {
%>
         <TR class="ColumnLabel">
	         <TD colspan="11"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Quality")%></TD>
         </TR>
<%}
if (i ==3) {
%>
         <TR class="ColumnLabel">
	         <TD colspan="11"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.COST")%></TD>
         </TR>
<%}
if (i ==5) {
%>
         <TR class="ColumnLabel">
	         <TD colspan="11"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.DELIVERY")%></TD>
         </TR>
<%}
%>
<tr class="<%=className%>">
<td><%=languageChoose.getMessage(normInfo.normName)%></td>
<td><%=normInfo.normUnit%></td>
<td><%=CommonTools.formatDouble(normInfo.lcl)%></td>
<td><%=CommonTools.formatDouble(normInfo.average)%></td>
<td><%=CommonTools.formatDouble(normInfo.ucl)%></td>
<!-- Add by HaiMM: Start -->
<TD><INPUT type="text" name="sm<%=i%>3" value="<%=(CommonTools.updateDouble(normInfo.usl) == "") ? CommonTools.updateDouble(normInfo.lcl): CommonTools.updateDouble(normInfo.usl)%>" size = "7" ></TD>
<!-- Add by HaiMM: End -->
<%if(i != 3){%>
	<td><INPUT type="text" name="sm<%=i%>0" value="<%=(CommonTools.updateDouble(normInfo.plannedValue) == "") ? CommonTools.updateDouble(normInfo.average) : CommonTools.updateDouble(normInfo.plannedValue)%>" size = "7" ></td>
<%}else{%>
	<td><%=CommonTools.updateDouble(normInfo.plannedValue)%></td>
	<INPUT type="hidden" name="sm<%=i%>0" value="<%=CommonTools.updateDouble(normInfo.plannedValue)%>" size = "7" readonly="readonly">
<%}%>
<!-- Add by HaiMM: Start -->
<TD><INPUT type="text" name="sm<%=i%>4" value="<%=(CommonTools.updateDouble(normInfo.lsl) == "") ? CommonTools.updateDouble(normInfo.ucl): CommonTools.updateDouble(normInfo.lsl)%>" size = "7" ></TD>
<!-- Add by HaiMM: End -->
<%if(i == 0){%>
	<td><INPUT type="text" name="sm<%=i%>1" value="<%=CommonTools.updateDouble(normInfo.actualValue)%>" size = "7" ></td>
<%}else{%>
	<td><%=CommonTools.formatDouble(normInfo.actualValue)%></td>
<%}%>
<td><%=CommonTools.formatDouble(normInfo.planDeviation)%></td>
<td><TEXTAREA name="sm<%=i%>2" rows="4" cols="50"><%=((normInfo.note==null)?"":normInfo.note)%></TEXTAREA></td>
</tr>
<%}%>
</table>
<BR>
<i><b>* Note:</b></i> <i>If Target is null, the Norm value (LCL, Average, UCL) will be proposed to metric.</i>
<BR>
</form>
<br>
<input type="button" name="Update" value=" <%=languageChoose.getMessage("fi.jsp.woStandardMetrics.OK")%> " class="BUTTON" onclick="javascript:on_Submit();">
<input type="button" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.woStandardMetrics.Cancel")%> " class="BUTTON" onclick="doCancel();">
<script language = "javascript">

	var sourcePage = <%=sourcePage%>;

	function doCancel() {
		if (sourcePage == 1) {
			jumpURL('qualityObjective.jsp');
		}
		else {
			jumpURL('woPerformanceView.jsp');		}
	}

	function copyActualUsageToBillableActual(){
		isBillableActualMandatory = true;
		document.frm_woPUpdate.billableActual.value=document.frm_woPUpdate.hiddenEffortUsageActual.value;
	}

	function on_Submit()
	{
  		if(isNaN(frm_woSMUpdate.sm01.value) && (frm_woSMUpdate.sm01.value != "")){
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
  			frm_woSMUpdate.sm01.focus();
  			return;
  		}
  	 	if(frm_woSMUpdate.sm01.value<0){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  			frm_woSMUpdate.sm01.focus();
  			return;
  		} 		
		if(frm_woSMUpdate.sm02.value.length > 600)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheTextInTheTextAreaIsTooLong")%>");
			frm_woSMUpdate.sm02.focus();
			return;
		}
		if(frm_woSMUpdate.sm12.value.length > 600)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheTextInTheTextAreaIsTooLong")%>");
			frm_woSMUpdate.sm12.focus();
			return;
		}
		if(frm_woSMUpdate.sm22.value.length > 600)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheTextInTheTextAreaIsTooLong")%>");
			frm_woSMUpdate.sm22.focus();
			return;
		}
		if(frm_woSMUpdate.sm32.value.length > 600)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheTextInTheTextAreaIsTooLong")%>");
			frm_woSMUpdate.sm32.focus();
			return;
		}
		if(frm_woSMUpdate.sm42.value.length > 600)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheTextInTheTextAreaIsTooLong")%>");
			frm_woSMUpdate.sm42.focus();
			return;
		}
		if(frm_woSMUpdate.sm52.value.length > 600)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheTextInTheTextAreaIsTooLong")%>");
			frm_woSMUpdate.sm52.focus();
			return;
		}
		if(frm_woSMUpdate.sm62.value.length > 600)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheTextInTheTextAreaIsTooLong")%>");
			frm_woSMUpdate.sm62.focus();
			return;
		}
		if(isNaN(frm_woSMUpdate.sm00.value) && (frm_woSMUpdate.sm00.value != "")){
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
  			frm_woSMUpdate.sm00.focus();
  			return;
  		}
  	 	if(frm_woSMUpdate.sm00.value<0){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  			frm_woSMUpdate.sm00.focus();
  			return;
  		}
  		if(isNaN(frm_woSMUpdate.sm10.value) && (frm_woSMUpdate.sm10.value != "")){
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
  			frm_woSMUpdate.sm10.focus();
  			return;
  		}
  	 	if(frm_woSMUpdate.sm10.value<0){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  			frm_woSMUpdate.sm10.focus();
  			return;
  		}
  		if(isNaN(frm_woSMUpdate.sm20.value) && (frm_woSMUpdate.sm20.value != "")){
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
  			frm_woSMUpdate.sm20.focus();
  			return;
  		}
  	 	if(frm_woSMUpdate.sm20.value<0){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  			frm_woSMUpdate.sm20.focus();
  			return;
  		}
  		if(isNaN(frm_woSMUpdate.sm30.value) && (frm_woSMUpdate.sm30.value != "")){
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
  			frm_woSMUpdate.sm30.focus();
  			return;
  		}
  	 	if(frm_woSMUpdate.sm30.value<0){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  			frm_woSMUpdate.sm30.focus();
  			return;
  		}
  		if(isNaN(frm_woSMUpdate.sm40.value) && (frm_woSMUpdate.sm40.value != "")){
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
  			frm_woSMUpdate.sm40.focus();
  			return;
  		}
  	 	if(frm_woSMUpdate.sm40.value<0){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  			frm_woSMUpdate.sm40.focus();
  			return;
  		}
  		if(isNaN(frm_woSMUpdate.sm50.value) && (frm_woSMUpdate.sm50.value != "")){
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
  			frm_woSMUpdate.sm50.focus();
			return;
  		}
		var str =  frm_woSMUpdate.sm50.value;
		if (str.indexOf(".")>=0)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeInteger")%>");
			frm_woSMUpdate.sm50.focus();
			return;
		}
  	 	if(frm_woSMUpdate.sm50.value<0){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  			frm_woSMUpdate.sm50.focus();
			return;
  		}
  		if(frm_woSMUpdate.sm50.value > 100){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueTimelinessCannotBe100")%>");
  			frm_woSMUpdate.sm50.focus();
  			return;
  		}
  		// Modify by HaiMM - Start
  		if(frm_woSMUpdate.sm53.value > 100){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueTimelinessCannotBe100")%>");
  			frm_woSMUpdate.sm53.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm54.value > 100){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueTimelinessCannotBe100")%>");
  			frm_woSMUpdate.sm54.focus();
  			return;
  		}
		// Modify by HaiMM - End
		
		if(isNaN(frm_woSMUpdate.sm60.value) && (frm_woSMUpdate.sm60.value != "")){
			alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
  			frm_woSMUpdate.sm60.focus();
			return;
  		}
   	 	if(frm_woSMUpdate.sm60.value<0){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  			frm_woSMUpdate.sm60.focus();
			return;
  		}
  		if(frm_woSMUpdate.sm60.value >100){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueRequirementCompletionCannotBe100")%>");
  			frm_woSMUpdate.sm60.focus();
  			return;
  		}
  		// Modify by HaiMM - Start
  		if(frm_woSMUpdate.sm63.value >100){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueRequirementCompletionCannotBe100")%>");
  			frm_woSMUpdate.sm63.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm64.value >100){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueRequirementCompletionCannotBe100")%>");
  			frm_woSMUpdate.sm64.focus();
  			return;
  		}
   		// Modify by HaiMM - End
   		
  		if(frm_woSMUpdate.sm00.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm00.focus();
  			return;
  		}
  		// Modify by HaiMM - Start
  		if(frm_woSMUpdate.sm03.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm03.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm04.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm04.focus();
  			return;
  		}
		// Modify by HaiMM - End

  		if(frm_woSMUpdate.sm01.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm01.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm10.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm10.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm20.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm20.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm30.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm30.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm40.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm40.focus();
  			return;
  		}

  		// Modify by HaiMM - Start
  		if(frm_woSMUpdate.sm13.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm13.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm14.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm14.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm23.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm23.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm24.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm24.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm33.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm33.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm34.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm34.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm43.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm43.focus();
  			return;
  		}
  		if(frm_woSMUpdate.sm44.value >1000000){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueIsTooLarge")%>");
  			frm_woSMUpdate.sm44.focus();
  			return;
  		}
		// Modify by HaiMM - End

		// Add by HaiMM: Start
  		if (!isBlank()){
			return;
		}
  		if (!isValidData()){
			return;
		}
  		if (!isValidConstraint()){
			return;
		}
		// Add by HaiMM: End
		if (sourcePage == 1) {
			frm_woSMUpdate.source.value = "1";
		}
		
		document.frm_woSMUpdate.submit();
	}

// Add by HaiMM
function isValidData() {
	var result = true;

	// row 0
	if(isNaN(frm_woSMUpdate.sm03.value) && (frm_woSMUpdate.sm03.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm03.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm03.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm03.focus();
  		result = false;
	}
	else if(isNaN(frm_woSMUpdate.sm04.value) && (frm_woSMUpdate.sm04.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm04.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm04.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm04.focus();
  		result = false;
	}

	// row 1
	else if(isNaN(frm_woSMUpdate.sm13.value) && (frm_woSMUpdate.sm13.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm13.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm13.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm13.focus();
  		result = false;
	}
	else if(isNaN(frm_woSMUpdate.sm14.value) && (frm_woSMUpdate.sm14.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm14.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm14.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm14.focus();
  		result = false;
	}

	// row 2
	else if(isNaN(frm_woSMUpdate.sm23.value) && (frm_woSMUpdate.sm23.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm23.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm23.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm23.focus();
  		result = false;
	}
	else if(isNaN(frm_woSMUpdate.sm24.value) && (frm_woSMUpdate.sm24.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm24.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm24.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm24.focus();
  		result = false;
	}
	
	// row 3
	else if(isNaN(frm_woSMUpdate.sm33.value) && (frm_woSMUpdate.sm33.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm33.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm33.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm33.focus();
  		result = false;
	}
	else if(isNaN(frm_woSMUpdate.sm34.value) && (frm_woSMUpdate.sm34.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm34.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm34.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm34.focus();
  		result = false;
	}
	
	// row 4
	else if(isNaN(frm_woSMUpdate.sm43.value) && (frm_woSMUpdate.sm43.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm43.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm43.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm43.focus();
  		result = false;
	}
	else if(isNaN(frm_woSMUpdate.sm44.value) && (frm_woSMUpdate.sm44.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm44.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm44.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm44.focus();
  		result = false;
	}
	
	// row 5
	else if(isNaN(frm_woSMUpdate.sm53.value) && (frm_woSMUpdate.sm53.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm53.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm53.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm53.focus();
  		result = false;
	}
	else if(isNaN(frm_woSMUpdate.sm54.value) && (frm_woSMUpdate.sm54.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm54.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm54.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm54.focus();
  		result = false;
	}
	
	// row 6
	else if(isNaN(frm_woSMUpdate.sm63.value) && (frm_woSMUpdate.sm63.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm63.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm63.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm63.focus();
  		result = false;
	}
	else if(isNaN(frm_woSMUpdate.sm64.value) && (frm_woSMUpdate.sm64.value != "")){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueMustBeANumber")%>");
		frm_woSMUpdate.sm64.focus();
		result = false;
	}
 	else if(frm_woSMUpdate.sm64.value<0){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.TheValueCannotBeNegative")%>");
  		frm_woSMUpdate.sm64.focus();
  		result = false;
	}
	
	return result;
}	

function isValidConstraint() {
	var result = true;

	if (!checkContraint(frm_woSMUpdate.sm03, frm_woSMUpdate.sm00, frm_woSMUpdate.sm04)){
		result = false;
	}
	else if (!checkContraint(frm_woSMUpdate.sm13, frm_woSMUpdate.sm10, frm_woSMUpdate.sm14)){
		result = false;
	}
	else if (!checkContraint(frm_woSMUpdate.sm23, frm_woSMUpdate.sm20, frm_woSMUpdate.sm24)){
		result = false;
	}
	else if (!checkContraint(frm_woSMUpdate.sm33, frm_woSMUpdate.sm30, frm_woSMUpdate.sm34)){
		result = false;
	}
	else if (!checkContraint(frm_woSMUpdate.sm43, frm_woSMUpdate.sm40, frm_woSMUpdate.sm44)){
		result = false;
	}
	else if (!checkContraint(frm_woSMUpdate.sm53, frm_woSMUpdate.sm50, frm_woSMUpdate.sm54)){
		result = false;
	}
	else if (!checkContraint(frm_woSMUpdate.sm63, frm_woSMUpdate.sm60, frm_woSMUpdate.sm64)){
		result = false;
	}
	
	return result;
}

function isBlank() {
	var result = true;

	if (!checkNull(frm_woSMUpdate.sm03, frm_woSMUpdate.sm00, frm_woSMUpdate.sm04)){
		result = false;
	}
	else if (!checkNull(frm_woSMUpdate.sm13, frm_woSMUpdate.sm10, frm_woSMUpdate.sm14)){
		result = false;
	}
	else if (!checkNull(frm_woSMUpdate.sm23, frm_woSMUpdate.sm20, frm_woSMUpdate.sm24)){
		result = false;
	}
	else if (!checkNull(frm_woSMUpdate.sm33, frm_woSMUpdate.sm34, frm_woSMUpdate.sm34)){
		result = false;
	}
	else if (!checkNull(frm_woSMUpdate.sm43, frm_woSMUpdate.sm40, frm_woSMUpdate.sm44)){
		result = false;
	}
	else if (!checkNull(frm_woSMUpdate.sm53, frm_woSMUpdate.sm50, frm_woSMUpdate.sm54)){
		result = false;
	}
	else if (!checkNull(frm_woSMUpdate.sm63, frm_woSMUpdate.sm60, frm_woSMUpdate.sm64)){
		result = false;
	}

	return result;
}

function checkContraint(uslObj, averObj, lslObj) {
	var result = true;
	var usl = new Number(uslObj.value);
	var aver = new Number(averObj.value);
	var lsl = new Number(lslObj.value);

	if((usl >= aver) || (usl >= lsl) || (aver >= lsl)){
		alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.ContrainstValue")%>");
		uslObj.focus();
		result = false;
	}
	
	return result;
}	

function checkNull(uslObj, averObj, lslObj) {
	var result = true;
	var usl = trim(uslObj.value).length;
	var aver = trim(averObj.value).length;
	var lsl = trim(lslObj.value).length;

	if(usl <= 0){
		alert("<%= languageChoose.getMessage("fi.jsp.woPerformanceView.Thisfieldismandatory")%>");
		uslObj.focus();
		result = false;
	}
	else if (aver <= 0) {
		alert("<%= languageChoose.getMessage("fi.jsp.woPerformanceView.Thisfieldismandatory")%>");
		averObj.focus();
		result = false;	
	}
	else if (lsl <= 0) {
		alert("<%= languageChoose.getMessage("fi.jsp.woPerformanceView.Thisfieldismandatory")%>");
		lslObj.focus();
		result = false;	
	}
	
	return result;
}	



// Add by HaiMM - End

</script>
</BODY>
</HTML>