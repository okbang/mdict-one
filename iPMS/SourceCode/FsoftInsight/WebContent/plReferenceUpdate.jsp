<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>plReferenceUpdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu(); frm_plReferenceUpdate.plReference_document.focus();">
<%
ReferenceInfo refInfo = (ReferenceInfo)session.getAttribute("plReferenceInfo");
%>
<form name="frm_plReferenceUpdate" action="Fms1Servlet#References" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.PL_REFERENCE_UPDATE%>">
<input type = "hidden" name="plReference_refID" value="<%=refInfo.referenceID%>">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plReferenceUpdate.ProjectplanReference")%> </P>

<br>
<TABLE cellspacing="1" class="Table">

<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plReferenceUpdate.Updatereference")%> </CAPTION>
    <TBODY>
        
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plReferenceUpdate.Document")%>* </TD>
            <TD class="CellBGR3">
            <input type="text" name="plReference_document" value = "<%=((refInfo.document == null)? "" : refInfo.document)%>" maxlength="50" size = "50"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plReferenceUpdate.Issueddate")%>* </TD>
            <TD class="CellBGR3"><input type="text" name="plReference_issuedDate" value = "<%=((refInfo.issueDate == null)? "" : CommonTools.dateFormat(new java.util.Date(refInfo.issueDate.getTime())))%>" maxlength="9" size = "9"> 
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPlReference_issuedDate()'>            	
            	(DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plReferenceUpdate.Source")%> </TD>
            <TD class="CellBGR3"><input type="text" name="plReference_source" value="<%=((refInfo.source == null)? "" : refInfo.source)%>" maxlength="50" size = "50"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plReferenceUpdate.Note")%> </TD>
            <TD class="CellBGR3"><input type="text" name="plReference_note" value="<%=((refInfo.note == null)? "" : refInfo.note)%>" maxlength="50" size = "50"></TD>
        </TR>
        
    </TBODY>
</TABLE>

</form>
<br>
<form name="frm_plReferenceDelete" action="Fms1Servlet#References" method = "get">
				<input type = "hidden" name="reqType" value="<%=Constants.PL_REFERENCE_DELETE %>" >
				<input type = "hidden" name="plReference_refID" value="<%=refInfo.referenceID%>">
</form>

<INPUT type="button" name="update2" value=" <%=languageChoose.getMessage("fi.jsp.plReferenceUpdate.Update")%> " onclick="javascript:on_Submit1();" class="BUTTON">
<input type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.plReferenceUpdate.Delete")%> "  onclick="javascript:on_Submit2();"  class="BUTTON">
<input type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.plReferenceUpdate.Cancel")%> "    class="BUTTON" onclick="doIt(<%=Constants.PL_OVERVIEW_GET_PAGE%>);">

</BODY>

<script language = "javascript">
	function showPlReference_issuedDate(){
		showCalendar(frm_plReferenceUpdate.plReference_issuedDate, frm_plReferenceUpdate.plReference_issuedDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function on_Submit1()
	{
		if(trim(frm_plReferenceUpdate.plReference_document.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plReferenceUpdate.Youmustenterdocument")%>");
  		 	frm_plReferenceUpdate.plReference_document.focus();
  		 	return;
  	 	}
  	 	
  	 	if(trim(frm_plReferenceUpdate.plReference_issuedDate.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plReferenceUpdate.Youmustenterissueddate")%>");
  		 	frm_plReferenceUpdate.plReference_issuedDate.focus();
  		 	return;
  	 	}
  	 	
		if((!(isDate(frm_plReferenceUpdate.plReference_issuedDate.value))) && (trim(frm_plReferenceUpdate.plReference_issuedDate.value) != "")){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plReferenceUpdate.thevalueinsertedisnotadate")%>");
  		 	frm_plReferenceUpdate.plReference_issuedDate.focus();
  		 	return;
  	 	}
  	 	frm_plReferenceUpdate.submit();
	}
	function on_Submit2()
	{
		if (window.confirm("<%= languageChoose.getMessage("fi.jsp.plReferenceUpdate.Areyousure")%>") != 0) 
			frm_plReferenceDelete.submit();
		else
			return;
	}		
</script> 
</HTML>
