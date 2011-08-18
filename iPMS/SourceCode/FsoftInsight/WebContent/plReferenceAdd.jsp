<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>plReferenceAdd.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu(); document.frm_plReferenceAdd.plReference_document.focus();">
<form name="frm_plReferenceAdd" action="Fms1Servlet#References" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.PL_REFERENCE_ADD %>">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plReferenceAdd.ProjectplanReference")%> </P>
<br>
<TABLE cellspacing="1" class="Table">

<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plReferenceAdd.Addnewreference")%> </CAPTION>
    <TBODY>
        
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plReferenceAdd.Document")%>* </TD>
            <TD class="CellBGR3">
            <input type="text" name="plReference_document"  maxlength="50" size = "50"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plReferenceAdd.Issueddate")%>* </TD>
            <TD class="CellBGR3"><input type="text" name="plReference_issuedDate"  maxlength="9" size = "9"> 
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPlReference_issuedDate()'>            	
            	(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plReferenceAdd.Source")%> </TD>
            <TD class="CellBGR3"><input type="text" name="plReference_source"  maxlength="50" size = "50"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plReferenceAdd.Note")%> </TD>
            <TD class="CellBGR3"><input type="text" name="plReference_note"  maxlength="50" size = "50"></TD>
        </TR>
        
    </TBODY>
</TABLE>

</form>
<br>
<INPUT type="button" name="Add" value=" <%=languageChoose.getMessage("fi.jsp.plReferenceAdd.OK")%> " onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.plReferenceAdd.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.PL_OVERVIEW_GET_PAGE%>);">

</BODY>
<script language = "javascript">
	function showPlReference_issuedDate(){
		showCalendar(frm_plReferenceAdd.plReference_issuedDate, frm_plReferenceAdd.plReference_issuedDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function on_Submit()
	{					
		
		if(trim(frm_plReferenceAdd.plReference_document.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plReferenceAdd.Youmustenterdocument")%>");
  		 	frm_plReferenceAdd.plReference_document.focus();
  		 	return;
  	 	}
  	 	
  	 	if(trim(frm_plReferenceAdd.plReference_issuedDate.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plReferenceAdd.Youmustenterissueddate")%>");
  		 	frm_plReferenceAdd.plReference_issuedDate.focus();
  		 	return;
  	 	}
  	 	
		if((!(isDate(frm_plReferenceAdd.plReference_issuedDate.value))) && (trim(frm_plReferenceAdd.plReference_issuedDate.value) != "")){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plReferenceAdd.thevalueinsertedisnotadate")%>");
  		 	frm_plReferenceAdd.plReference_issuedDate.focus();
  		 	return;
  	 	}
  	 	
  	 	
  	 	document.frm_plReferenceAdd.submit();
		
	}	
</script> 
