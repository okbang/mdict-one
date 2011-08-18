<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plSubcontractUpdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu();document.frm_plSubcontractUpdate.plSubcontract_job.focus();">
<%
SubcontractInfo subcontractInfo = (SubcontractInfo)session.getAttribute("plSubcontractInfo");
ProjectDateInfo projectDateInfo = (ProjectDateInfo) session.getAttribute("projectDateInfo");
String startDate = CommonTools.dateFormat(projectDateInfo.plannedStartDate);
String endDate = CommonTools.dateFormat(projectDateInfo.plannedEndDate);
%>
<form name="frm_plSubcontractUpdate" action="Fms1Servlet#Subcontracts" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.PL_SUBCONTRACT_UPDATE%>">
<input type = "hidden" name="plSubcontract_ID" value="<%=subcontractInfo.subcontractID%>">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plSubcontractUpdate.ProjectplanSubcontract")%> </P>

<br>
<TABLE cellspacing="1" class="Table">

<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plSubcontractUpdate.SubcontractUpdate")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Job")%>* </TD>
            <TD class="CellBGR3">
            <TEXTAREA name = "plSubcontract_job" cols = "50" rows = "4"><%=((subcontractInfo.job == null)? "" : subcontractInfo.job)%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Deliverable")%>* </TD>
            <TD class="CellBGR3">
            <TEXTAREA name = "plSubcontract_deliverable" cols = "50" rows = "4"><%=((subcontractInfo.deliverable == null)? "" : subcontractInfo.deliverable)%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Planneddeliverydate")%>* </TD>
            <TD class="CellBGR3"><input type="text" name="plSubcontract_plannedDate" value = "<%=((subcontractInfo.plannedDeliveryDate == null)? "" : CommonTools.dateFormat(subcontractInfo.plannedDeliveryDate))%>" maxlength="9" size = "9"> (DD-MMM-YY) </TD>
        </TR>
         <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Actualdeliverydate")%> </TD>
            <TD class="CellBGR3"><input type="text" name="plSubcontract_actualDate" value = "<%=((subcontractInfo.actualDeliveryDate == null)? "" : CommonTools.dateFormat(subcontractInfo.actualDeliveryDate))%>" maxlength="9" size = "9"> (DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Note")%> </TD>
            <TD class="CellBGR3">
            <TEXTAREA name = "plSubcontract_note" cols = "50" rows = "4"><%=((subcontractInfo.note == null)? "" : subcontractInfo.note)%></TEXTAREA></TD>
        </TR>
        
    </TBODY>
</TABLE>

</form>
<br>


<INPUT type="button" name="update2" value=" <%=languageChoose.getMessage("fi.jsp.plSubcontractUpdate.OK")%> " onclick="javascript:on_Submit1();" class="BUTTON">
<input type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Cancel")%> "    class="BUTTON" onclick="jumpURL('plStructure.jsp');">

</BODY>


<script language = "javascript">
	function on_Submit1()
	{
	
		
		if(trim(frm_plSubcontractUpdate.plSubcontract_job.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Youmustenteritem")%>");
  		 	frm_plSubcontractUpdate.plSubcontract_job.focus();
  		 	return;
  	 	}
  	 	
  	 	if(frm_plSubcontractUpdate.plSubcontract_job.value.length > 200){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Thetextinthetextareaistoolong")%>");
  		 	frm_plSubcontractUpdate.plSubcontract_job.focus();
  		 	return;
  	 	}
		
		if(trim(frm_plSubcontractUpdate.plSubcontract_deliverable.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Youmustenteritem")%>");
  		 	frm_plSubcontractUpdate.plSubcontract_deliverable.focus();
  		 	return;
  	 	}
  	 	
  	 	if(frm_plSubcontractUpdate.plSubcontract_deliverable.value.length > 200){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Thetextinthetextareaistoolong")%>");
  		 	frm_plSubcontractUpdate.plSubcontract_deliverable.focus();
  		 	return;
  	 	}
  	 	
		if(trim(frm_plSubcontractUpdate.plSubcontract_plannedDate.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Youmustenterplanneddeliverydate")%>");
  		 	frm_plSubcontractUpdate.plSubcontract_plannedDate.focus();
  		 	return;
  	 	}
		
		if((!(isDate(frm_plSubcontractUpdate.plSubcontract_plannedDate.value))) && (trim(frm_plSubcontractUpdate.plSubcontract_plannedDate.value) != "")){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractUpdate.thevalueinsertedisnotadate")%>");
  		 	frm_plSubcontractUpdate.plSubcontract_plannedDate.focus();
  		 	return;
  	 	}
  	 	
  	 	<%if(!startDate.equals("")){%>
  	 		if(compareDate("<%=startDate%>",frm_plSubcontractUpdate.plSubcontract_plannedDate.value) == -1){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Planneddeliverydatemustbeafterplannedstartdateofproject")%>");
  		 	frm_plSubcontractUpdate.plSubcontract_plannedDate.focus();
  		 	return;
  	 		}
  	 		
  	 	<%}
		if(!endDate.equals("")){	%>
  	 		if(compareDate(frm_plSubcontractUpdate.plSubcontract_plannedDate.value, "<%=endDate%>") == -1){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Planneddeliverydatemustbebeforeplannedenddateofproject")%>");
  		 	frm_plSubcontractUpdate.plSubcontract_plannedDate.focus();
  		 	return;
  	 		}
  	 		
  	 	<%}%>
  	 	
  	 	if((!(isDate(frm_plSubcontractUpdate.plSubcontract_actualDate.value))) && (trim(frm_plSubcontractUpdate.plSubcontract_actualDate.value) != "")){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractUpdate.thevalueinsertedisnotadate")%>");
  		 	frm_plSubcontractUpdate.plSubcontract_actualDate.focus();
  		 	return;
  	 	}
  	 	if(frm_plSubcontractUpdate.plSubcontract_note.value.length > 200){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractUpdate.Thetextinthetextareaistoolong")%>");
  		 	frm_plSubcontractUpdate.plDSubcontract_note.focus();
  		 	return;
  	 	}
  	 	document.frm_plSubcontractUpdate.submit();
	}
	
</script> 
</HTML>
