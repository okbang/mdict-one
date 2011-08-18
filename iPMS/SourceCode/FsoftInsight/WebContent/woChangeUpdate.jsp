<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woChangeUpdate.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu(); document.frm_changeUpdate.woChange_item.focus();">
<%
WOChangeInfo changeInfo = (WOChangeInfo)session.getAttribute("woChangeInfo");

String action = "";
int intAction = 0;
if (changeInfo.action == null) {
	action = "";
} else {
	intAction = Integer.parseInt(changeInfo.action);
	switch (intAction) {
		case 1: action = "Added"; break;
		case 2: action = "Modified"; break;
		case 3: action = "Deleted"; break;
	}
}

String change_source_page = (String) session.getAttribute("change_source_page");
String title;
if(change_source_page.equals("1")){ //MANU: called from project plan
	title=languageChoose.getMessage("fi.jsp.woChangeUpdate.ProjectPlanChanges");
}
else { //MANU: called from Work order
	title=languageChoose.getMessage("fi.jsp.woChangeUpdate.WorkOrderChanges");
}
%>
<form name="frm_changeUpdate" action="Fms1Servlet#change" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.WO_CHANGE_UPDATE%>">
<input type = "hidden" name="woChange_ID" value="<%=changeInfo.changeID%>">
<P class="TITLE"><%=title%></P>
<br>
<TABLE cellspacing="1" class="Table">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woChangeUpdate.Updatechange")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woChangeUpdate.Item")%>* </TD>
            <TD class = "CellBGR3">
            <input type="text" name="woChange_item" value="<%=((changeInfo.item == null)? "" : changeInfo.item)%>" maxlength="50" size = "50">
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> A*/M/D </TD>
            <TD class = "CellBGR3"><SELECT name="action" class="COMBO">
                <OPTION value="0" <%if(intAction == 0){%>selected<%}%>></OPTION>
                <OPTION value="1" <%if(intAction == 1){%>selected<%}%>>Added</OPTION>
                <OPTION value="2" <%if(intAction == 2){%>selected<%}%>>Modified</OPTION>
                <OPTION value="3" <%if(intAction == 3){%>selected<%}%>>Deleted </OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woChangeUpdate.Changes")%>*</TD>
            <TD class="CellBGR3">
            <TEXTAREA name="woChange_changes" rows="4" cols="50"><%=((changeInfo.changes == null)? "": changeInfo.changes) %></TEXTAREA>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woChangeUpdate.Reason")%>* </TD>
            <TD class="CellBGR3">
            <TEXTAREA name="woChange_reason" rows="4" cols="50"><%=((changeInfo.reason == null)? "": changeInfo.reason) %></TEXTAREA>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woChangeUpdate.Version")%> </TD>
            <TD class="CellBGR3"><input type="text" name="woChange_version" value="<%=((changeInfo.version == null)? "" : changeInfo.version)%>" maxlength="10" size = "11"></TD>
        </TR>
    </TBODY>
</TABLE>

</form>
<br>
<form name="frm_changeDelete" action="Fms1Servlet#change" method = "get">
				<input type = "hidden" name="reqType" value="<%=Constants.WO_CHANGE_DELETE %>" >
				<input type = "hidden" name="woChange_ID" value="<%=changeInfo.changeID%>">
</form>

<INPUT type="button" name="update2" value=" <%=languageChoose.getMessage("fi.jsp.woChangeUpdate.Update")%> " onclick="javascript:on_Submit1();" class="BUTTON">
<input type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.woChangeUpdate.Delete")%> "  onclick="javascript:on_Submit2();"  class="BUTTON">
<input type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.woChangeUpdate.Cancel")%> "    class="BUTTON" onclick="doIt('<%=Constants.WO_CHANGE_GET_LIST+"&change_source_page="+change_source_page%>');">

</BODY>

<script language = "javascript">
	function on_Submit1()
	{
		if(trim(frm_changeUpdate.woChange_item.value) == ""){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.woChangeUpdate.YouMustEnterItem")%>");
  		 	frm_changeUpdate.woChange_item.focus();
  		 	return;
  	 	}
  	 	if(trim(frm_changeUpdate.action.value) == "0"){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.woChangeUpdate.YouMustEnterAction")%>");
  		 	frm_changeUpdate.action.focus();
  		 	return;
  	 	}
  	 	if(trim(frm_changeUpdate.woChange_changes.value) == ""){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.woChangeUpdate.YouMustEnterChange")%>");
  		 	frm_changeUpdate.woChange_changes.focus();
  		 	return;
  	 	}
		if(trim(frm_changeUpdate.woChange_reason.value) == ""){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.woChangeUpdate.YouMustEnterReason")%>");
  		 	frm_changeUpdate.woChange_reason.focus();
  		 	return;
  	 	}
  	 	
  	 	if(frm_changeUpdate.woChange_changes.value.length > 200)
		{
			alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woChangeUpdate.The__length__of__this__field__must__be__below__200__characters__currently__~PARAM1_CHANGELEN~")%>',frm_changeUpdate.woChange_changes.value.length)));
			frm_changeUpdate.woChange_changes.focus();
			return;
		}
		if(frm_changeUpdate.woChange_reason.value.length > 200)
		{
			alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woChangeUpdate.The__length__of__this__field__must__be__below__200__characters__currently__~PARAM1_CHANGELEN~")%>',frm_changeUpdate.woChange_reason.value.length)));
			frm_changeUpdate.woChange_reason.focus();
			return;
		}	

		document.frm_changeUpdate.submit();
	}
	function on_Submit2()
	{
		if (window.confirm("<%= languageChoose.getMessage("fi.jsp.woChangeUpdate.AreYouSure")%>") != 0) {
			document.frm_changeDelete.submit();
		}
		else{
			return;
		}	
		
	}		
</script> 
</HTML>
