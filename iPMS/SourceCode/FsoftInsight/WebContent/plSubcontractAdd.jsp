<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
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
<TITLE>plSubcontractAdd.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY
    class="BD" onLoad="loadPrjMenu();document.frm_plSubcontractAdd.plSubcontract_job.focus();">
<%

ProjectDateInfo projectDateInfo = (ProjectDateInfo) session.getAttribute("projectDateInfo");
String startDate = CommonTools.dateFormat(projectDateInfo.plannedStartDate);
String endDate = CommonTools.dateFormat(projectDateInfo.plannedEndDate);
%>
<form name="frm_plSubcontractAdd" action="Fms1Servlet#Subcontracts" method="get"><input type="hidden" name="reqType" value="<%=Constants.PL_SUBCONTRACT_ADD%>">


<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plSubcontractAdd.ProjectplanSubcontract")%> </P>

<br>
<TABLE cellspacing="1" class="Table">

    <CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plSubcontractAdd.AddnewSubcontract")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plSubcontractAdd.Job")%>* </TD>
            <TD class="CellBGR3"><TEXTAREA name="plSubcontract_job" cols="50" rows="4"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plSubcontractAdd.Deliverable")%>* </TD>
            <TD class="CellBGR3"><TEXTAREA name="plSubcontract_deliverable" cols="50" rows="4"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plSubcontractAdd.Planneddeliverydate")%>* </TD>
            <TD class="CellBGR3"><input type="text" name="plSubcontract_plannedDate" maxlength="9" size="9"> (DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plSubcontractAdd.Actualdeliverydate")%> </TD>
            <TD class="CellBGR3"><input type="text" name="plSubcontract_actualDate" maxlength="9" size="9"> (DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plSubcontractAdd.Note")%> </TD>
            <TD class="CellBGR3"><TEXTAREA name="plSubcontract_note" cols="50" rows="4"></TEXTAREA></TD>
        </TR>

    </TBODY>
</TABLE>

</form>
<br>


<INPUT type="button" name="addnew" value=" <%=languageChoose.getMessage("fi.jsp.plSubcontractAdd.OK")%> " onclick="javascript:on_Submit1();" class="BUTTON">
<input type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.plSubcontractAdd.Cancel")%> " class="BUTTON" onclick="jumpURL('plStructure.jsp');">

</BODY>

<script language = "javascript">
	function on_Submit1()
	{
	
		
		if(trim(frm_plSubcontractAdd.plSubcontract_job.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractAdd.Youmustenterjob")%>");
  		 	frm_plSubcontractAdd.plSubcontract_job.focus();
  		 	return;
  	 	}
  	 	
  	 	if(frm_plSubcontractAdd.plSubcontract_job.value.length > 200){  	  
			window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractAdd.Thetextinthetextareaistoolong")%>");
  		 	frm_plSubcontractAdd.plSubcontract_job.focus();
  		 	return;
  	 	}
		
		if(trim(frm_plSubcontractAdd.plSubcontract_deliverable.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractAdd.Youmustenterdeliverable")%>");
  		 	frm_plSubcontractAdd.plSubcontract_deliverable.focus();
  		 	return;
  	 	}
  	 	
  	 	if(frm_plSubcontractAdd.plSubcontract_deliverable.value.length > 200){  	  
			window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractAdd.Thetextinthetextareaistoolong")%>");
  		 	frm_plSubcontractAdd.plSubcontract_deliverable.focus();
  		 	return;
  	 	}
  	 	
		if(trim(frm_plSubcontractAdd.plSubcontract_plannedDate.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractAdd.Youmustenterplanneddeliverydate")%>");
  		 	frm_plSubcontractAdd.plSubcontract_plannedDate.focus();
  		 	return;
  	 	}
		
		if((!(isDate(frm_plSubcontractAdd.plSubcontract_plannedDate.value))) && (trim(frm_plSubcontractAdd.plSubcontract_plannedDate.value) != "")){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractAdd.thevalueinsertedisnotadate")%>");
  		 	frm_plSubcontractAdd.plSubcontract_plannedDate.focus();
  		 	return;
  	 	}
  	 	
  	 	<%if(!startDate.equals("")){%>
  	 		if(compareDate("<%=startDate%>",frm_plSubcontractAdd.plSubcontract_plannedDate.value) == -1){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractAdd.Planneddeliverydatemustbeafterplannedstartdateofproject")%>");
  		 	frm_plSubcontractAdd.plSubcontract_plannedDate.focus();
  		 	return;
  	 		}
  	 		
  	 	<%}
		if(!endDate.equals("")){%>
  	 		if(compareDate(frm_plSubcontractAdd.plSubcontract_plannedDate.value, "<%=endDate%>") == -1){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractAdd.Planneddeliverydatemustbebeforeplannedenddateofproject")%>");
  		 	frm_plSubcontractAdd.plSubcontract_plannedDate.focus();
  		 	return;
  	 		}
  	 		
  	 	<%}%>
  	 	
  	 	if((!(isDate(frm_plSubcontractAdd.plSubcontract_actualDate.value))) && (trim(frm_plSubcontractAdd.plSubcontract_actualDate.value) != "")){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractAdd.thevalueinsertedisnotadate")%>");
  		 	frm_plSubcontractAdd.plSubcontract_actualDate.focus();
  		 	return;
  	 	}
  	 	if(frm_plSubcontractAdd.plSubcontract_note.value.length > 200){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plSubcontractAdd.Thetextinthetextareaistoolong")%>");
  		 	frm_plSubcontractAdd.plDSubcontract_note.focus();
  		 	return;
  	 	}
  	 	document.frm_plSubcontractAdd.submit();
	}
	
</script>
</HTML>
