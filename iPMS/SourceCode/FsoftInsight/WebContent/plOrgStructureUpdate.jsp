<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plOrgStructureUpdate.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class = "BD">
<%
String orgStructure = (String)session.getAttribute("plOrgStructure");
%>
<form name="frm_plOrgStructureUpdate" action="Fms1Servlet" method = "post">
<input type = "hidden" name="reqType" value="<%=Constants.PL_ORG_STRUCTURE_UPDATE%>">


<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plOrgStructureUpdate.ProjectplanOrganizationStructu")%></P>

<br>
<TABLE cellspacing="1" class="Table">

<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.plOrgStructureUpdate.ProjectOrganization")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="CellBGR3">
            <TEXTAREA name = "plOrgStructure_txt" cols = "100" rows = "8"><%=((orgStructure == null)? "" : orgStructure)%></TEXTAREA></TD>
        </TR>
        
        
    </TBODY>
</TABLE>

</form>
<br>
<INPUT type="button" name="update2" value="<%=languageChoose.getMessage("fi.jsp.plOrgStructureUpdate.OK")%>" onclick="javascript:on_Submit();" class="BUTTON">
<input type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.plOrgStructureUpdate.Cancel")%>"    class="BUTTON" onclick="jumpURL('plStructure.jsp');">

</BODY>
<script language = "javascript">
	function on_Submit()
	{		
		if(trim(frm_plOrgStructureUpdate.plOrgStructure_txt.value) == ""){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plOrgStructureUpdate.youmustenterorganizationstructure")%>");
  		 	frm_plOrgStructureUpdate.plOrgStructure_txt.focus();
  		 	return;  		
  	 	}
  	 	if(maxLength(frm_plOrgStructureUpdate.plOrgStructure_txt,"Project Organization",4000)==false){
  	 		frm_plOrgStructureUpdate.plOrgStructure_txt.focus();
  		 	return;
  	 	}  		 	
  	 	document.frm_plOrgStructureUpdate.submit();
	}
</script> 
</HTML>

