<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woChangeAdd.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu(); document.frm_changeAdd.woChange_item.focus();">
<%

String change_source_page = (String) session.getAttribute("change_source_page");
String title;
if(change_source_page.equals("1")){ //MANU: called from project plan
	title=languageChoose.getMessage("fi.jsp.woChangeAdd.ProjectPlanChange");
}
else { //MANU: called from Work order
	title=languageChoose.getMessage("fi.jsp.woChangeAdd.WorkOrderChange");
}
%>
<form name="frm_changeAdd" action="Fms1Servlet#change" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.WO_CHANGE_ADD %>">
<P class="TITLE"><%=title%></P>
<br>
<TABLE cellspacing="1" class="Table">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woChangeAdd.Addnewchange")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woChangeAdd.Item")%>* </TD>
            <TD class = "CellBGR3">
            <input type="text" name="woChange_item"  maxlength="50" size = "50">
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> A*/M/D </TD>
            <TD class = "CellBGR3"><SELECT name="action" class="COMBO">
                <OPTION value="1">Added</OPTION>
                <OPTION value="2">Modified</OPTION>
                <OPTION value="3">Deleted </OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woChangeAdd.Changes")%>*</TD>
            <TD class="CellBGR3">
            <TEXTAREA name="woChange_changes" rows="4" cols="50"></TEXTAREA>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woChangeAdd.Reason")%>* </TD>
            <TD class="CellBGR3">
            <TEXTAREA name="woChange_reason" rows="4" cols="50"></TEXTAREA>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woChangeAdd.Version")%> </TD>
            <TD class="CellBGR3"><input type="text" name="woChange_version"  maxlength="10" size = "11"></TD>
        </TR>
    </TBODY>
</TABLE>

</form>
<br>
<INPUT type="button" name="Add" value=" <%=languageChoose.getMessage("fi.jsp.woChangeAdd.OK")%> " onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.woChangeAdd.Cancel")%> " class="BUTTON" onclick="doIt('<%=Constants.WO_CHANGE_GET_LIST+"&change_source_page="+change_source_page%>');">

</BODY>

<script language = "javascript">
	function on_Submit()
	{	
		if(trim(frm_changeAdd.woChange_item.value) == ""){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.woChangeAdd.YouMustEnterItem")%>");
  		 	frm_changeAdd.woChange_item.focus();
  		 	return;
  	 	}
  	 	if(trim(frm_changeAdd.woChange_changes.value) == ""){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.woChangeAdd.YouMustEnterChange")%>");
  		 	frm_changeAdd.woChange_changes.focus();
  		 	return;
  	 	}
		if(trim(frm_changeAdd.woChange_reason.value) == ""){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.woChangeAdd.YouMustEnterReason")%>");
  		 	frm_changeAdd.woChange_reason.focus();
  		 	return;
  	 	}
		
		if(frm_changeAdd.woChange_changes.value.length > 200)
		{
			alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woChangeAdd.The__length__of__this__field__must__be__below__200__characters__currently__~PARAM1_CHANGELEN~")%>',frm_changeAdd.woChange_changes.value.length)));
			frm_changeAdd.woChange_changes.focus();
			return;
		}
		if(frm_changeAdd.woChange_reason.value.length > 200)
		{
			alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woChangeAdd.The__length__of__this__field__must__be__below__200__characters__currently__~PARAM1_CHANGELEN~")%>',frm_changeAdd.woChange_reason.value.length)));
			frm_changeAdd.woChange_reason.focus();
			return;
		}	
	

		document.frm_changeAdd.submit();
	}	
</script> 
</HTML>
