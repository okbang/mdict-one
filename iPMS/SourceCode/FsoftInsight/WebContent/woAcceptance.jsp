<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woAcceptance.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onload="loadPrjMenu();document.frm_woAcceptanceUpdate.acceptance00.focus();">
<%
String [] values = (String[]) session.getAttribute("WOAcceptanceMatrix");

%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woAcceptance.WorkOrderAcceptance")%> </P>
<form name="frm_woAcceptanceUpdate" action="Fms1Servlet" method = "post">
<input type = "hidden" name="reqType" value="<%=Constants.WO_ACCEPTANCE_UPDATE%>">
<br>
<TABLE cellspacing="1" class="Table">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woAcceptance.Updateacceptance")%>  </CAPTION>
    <TBODY>
        
<%for(int i = 0; i < 3; i++){
	if (i==0) {	
%>
<tr>
	<td class="ColumnLabel" width="160">
	<%
	switch (i) {
		case 0:%> <%=languageChoose.getMessage("fi.jsp.woAcceptance.Assets")%> <%break;
		case 1:%> <%=languageChoose.getMessage("fi.jsp.woAcceptance.Problems")%> <%break;
		case 2:%> <%=languageChoose.getMessage("fi.jsp.woAcceptance.RewardandPenalty")%> <%break;
	}
	%>
	</td>
    <td class="CellBGR3"><TEXTAREA name="acceptance<%=i%>0" rows="6" cols="70"><%=((values[i] == null)? "": values[i])%></TEXTAREA></td>
</tr>
<%} else { %>
	<input type=hidden name="acceptance<%=i%>0" value="<%=((values[i] == null)? "": values[i])%>">
<%}}%>
<tr>
	<td class="ColumnLabel" width="160">
		Proposal
	</td>
	<td class="CellBGR3">
		<TEXTAREA name="acceptance30" rows="6" cols="70"><%=((values[3] == null)? "": values[3])%></TEXTAREA>
	</td>
</tr>
</table>
</form>
<br>
<input type="button" name="Update" value=" <%=languageChoose.getMessage("fi.jsp.woAcceptance.OK")%> " class="BUTTON" onclick="javascript:on_Submit();">
<input type="button" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.woAcceptance.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.WO_ACCEPTANCE_GET_LIST%>);">
</BODY>
</HTML>
<script language = "javascript">
	function on_Submit()
	{	
		if(maxLength(frm_woAcceptanceUpdate.acceptance00,"Assets",500)==false){
  	 		frm_woAcceptanceUpdate.acceptance00.focus();
  		 	return;
  	 	}
  	 	if(maxLength(frm_woAcceptanceUpdate.acceptance30,"Proposal",500)==false){
  	 		frm_woAcceptanceUpdate.acceptance00.focus();
  		 	return;
  	 	}  	
		/*if(maxLength(frm_woAcceptanceUpdate.acceptance10,"Problems",4000)==false){
  	 		frm_woAcceptanceUpdate.acceptance10.focus();
  		 	return;
  	 	} 
  	 	if(maxLength(frm_woAcceptanceUpdate.acceptance20,"Reward and Penalty",4000)==false){
  	 		frm_woAcceptanceUpdate.acceptance20.focus();
  		 	return;
  	 	} */ 	 		 	
		document.frm_woAcceptanceUpdate.submit();
	}
		
</script> 
