<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woCustomeMetricAdd.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	String sourcePage = (String) session.getAttribute("SourcePage");
	// get stagelist from depend on projectID
	// create by anhtv08
	// date :3/7/2009

	Vector stageList = (Vector)session.getAttribute("WOMetricsStageList");
	StageInfo stageInfo=(StageInfo)session.getAttribute("StageInfo");
	
	
%>
<BODY class="BD" onload = "loadPrjMenu();document.frm_woCMAdd.woCM_name.focus();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.SpecificObjectives")%> </P><br>
<form name="frm_woCMAdd" action="Fms1Servlet#custom" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.WO_CUS_METRIC_ADD%>">
<input type = "hidden" name="woCM_ID" value="1">
<input type = "hidden" name="projectID" value="<%=session.getAttribute("projectID")%>">
<input type="hidden" name="source" value=""> 
<DIV align="left">
<TABLE  cellspacing="1" class="Table">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.AddSpecificObjectives")%>  </CAPTION>
    <TBODY>
         <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.Name")%>* </TD>
            <TD class="CellBGR3"><INPUT type="text" name="woCM_name"  maxlength = "50" size = "50"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.Unit")%>* </TD>
            <TD class="CellBGR3"><INPUT type="text" name="woCM_unit"  maxlength = "50" size = "50"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%></TD>
            <TD class="CellBGR3"><INPUT type="text" name="woCM_LCL" maxlength = "11" size = "11" style="text-align: right"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.TargetedValue")%></TD>
            <TD class="CellBGR3"><INPUT type="text" name="woCM_plannedValue" maxlength = "11" size = "11" style="text-align: right"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%></TD>
            <TD class="CellBGR3"><INPUT type="text" name="woCM_UCL" maxlength = "11" size = "11" style="text-align: right"></TD>
        </TR> 
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woCustomeMetricUpdate.Note")%> </TD>
            <TD class="CellBGR3"><TEXTAREA name="woCM_note" rows="4" cols="50"></TEXTAREA></TD>
        </TR>
        
    </TBODY>
</TABLE>
</div>
<BR>
<i><b>* Note:</b></i> <i>If USL or LSL is null, default value (= Average) will be proposed to this metric.</i>
<BR>
</form>
<br>
<INPUT type="button" name="Add" value="<%=languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.Ok")%>" onclick="on_Submit()" class="BUTTON">
<INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.Cancel")%>" class="BUTTON" onclick="doCancel()">
</BODY>


<script language = "javascript">
	var sourcePage = <%=sourcePage%>;

	function doCancel() {
		if (sourcePage == 1) {
			jumpURL('qualityObjective.jsp');
		}
		else {
			jumpURL('woPerformanceView.jsp');		
		}
	}


	function on_Submit()
	{	
		if(trim(frm_woCMAdd.woCM_name.value) == ""){
  			alert("<%= languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.YouMustEnterNameOfMetric")%>");
  			frm_woCMAdd.woCM_name.focus();  		
  			return;  
  		}
		
		if(trim(frm_woCMAdd.woCM_unit.value) == ""){
  			alert("<%= languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.YouMustEnterUnitOfMetric")%>");		
  			frm_woCMAdd.woCM_unit.focus();  		
  			return;  
  		}
		if(isNaN(frm_woCMAdd.woCM_plannedValue.value) && (trim(frm_woCMAdd.woCM_plannedValue.value) != "")){
  			alert("<%= languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.TheValueMustBeANumber")%>");				
  			frm_woCMAdd.woCM_plannedValue.focus();  		
  			return;  
  		}	
 			
		if(isNaN(frm_woCMAdd.woCM_LCL.value) && (trim(frm_woCMAdd.woCM_LCL.value) != "")){
  			alert("<%= languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.TheValueMustBeANumber")%>");				
  			frm_woCMAdd.woCM_LCL.focus();  		
  			return;  
  		}
  		if(isNaN(frm_woCMAdd.woCM_UCL.value) && (trim(frm_woCMAdd.woCM_UCL.value) != "")){
  			alert("<%= languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.TheValueMustBeANumber")%>");				
  			frm_woCMAdd.woCM_UCL.focus();
  			return;  
  		}
  		if(frm_woCMAdd.woCM_note.value.length > 200)
		{
			alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.The__length__of__this__field__must__be__below__200__characters__currently__~PARAM1_NOTELEN~")%>', frm_woCMAdd.woCM_note.value.length)));
			frm_woCMAdd.woCM_note.focus();
			return;
		}	
		if(eval(frm_woCMAdd.woCM_LCL.value) >= eval(frm_woCMAdd.woCM_plannedValue.value) && frm_woCMAdd.woCM_plannedValue.value !=""){
  			alert("<%= languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.TheValueOfLCLAverageUCLMustFolowOrder")%>");				
  			frm_woCMAdd.woCM_plannedValue.focus();  		
  			return;  
  		}
  		if(eval(frm_woCMAdd.woCM_plannedValue.value) >= eval(frm_woCMAdd.woCM_UCL.value) && frm_woCMAdd.woCM_UCL.value !=""){
  			alert("<%= languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.TheValueOfLCLAverageUCLMustFolowOrder")%>");				
  			frm_woCMAdd.woCM_UCL.focus();
  			return;  
  		}
  		if(eval(frm_woCMAdd.woCM_LCL.value) >= eval(frm_woCMAdd.woCM_UCL.value) && frm_woCMAdd.woCM_UCL.value !=""){
  			alert("<%= languageChoose.getMessage("fi.jsp.woCustomeMetricAdd.TheValueOfLCLAverageUCLMustFolowOrder")%>");				
  			frm_woCMAdd.woCM_UCL.focus();
  			return;  
  		}
  		
  		if (sourcePage == 1) {
			frm_woCMAdd.source.value = "1";
		}
  		
		document.frm_woCMAdd.submit();
	}	
</script> 
</HTML>
