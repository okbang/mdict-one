<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*, java.text.*, com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plConstraintAdd.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu();document.frm_plConstraintAdd.plDescription.focus();">
<%
String strCaller=(String)session.getAttribute("caller");
int caller;
if (strCaller!=null)
	caller=Integer.parseInt(strCaller);
else
	caller=Constants.PROJECT_PLAN_CALLER;
%>
<form name="frm_plConstraintAdd" action="Fms1Servlet#constraints" method = "post">
<input type = "hidden" name="reqType" value="<%=Constants.PL_CONSTRAINT_ADD%>">

<P class="TITLE"><%=(caller==Constants.PROJECT_PLAN_CALLER)?languageChoose.getMessage("fi.jsp.plConstraintAdd.ProjectplanConstraint"):languageChoose.getMessage("fi.jsp.plConstraintAdd.WorkOrderConstraint")%></P>

<br>
<TABLE cellspacing="1" class="Table">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Addnewconstraint")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Description")%>* </TD>
            <TD class="CellBGR3"><TEXTAREA name="plDescription" rows="4" cols="50"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Note")%> </TD>
            <TD class="CellBGR3"><TEXTAREA name="plNote" rows="4" cols="50"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Type")%> </TD>
            <TD class = "CellBGR3">
            <SELECT name="plType" class="COMBO"  >
            	<OPTION value = "0" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Businessexpertise")%> </OPTION>
            	<OPTION value = "1" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Customerproblem")%> </OPTION>  
            	<OPTION value = "2" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Customerrelationship")%> </OPTION>
            	<OPTION value = "3" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Environment")%> </OPTION>
            	<OPTION value = "4" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Estimation")%> </OPTION>  
            	<OPTION value = "5" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Geography")%> </OPTION>    
            	<OPTION value = "6" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Requirement")%> </OPTION>
            	<OPTION value = "7" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Resource")%> </OPTION>  
            	<OPTION value = "8" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Schedule")%> </OPTION>    
            	<OPTION value = "9" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Technical")%> </OPTION>
            	<OPTION value = "10" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Projectmanagement")%> </OPTION>  
            	<OPTION value = "11" > <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Qualitymanagement")%> </OPTION>        
            </SELECT>
            </TD>
        </TR>
    </TBODY>
</TABLE>

</form>
<br>
<INPUT type="button" name="Add" value=" <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.OK")%> " onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.plConstraintAdd.Cancel")%> " class="BUTTON" onclick="doIt(<%=(caller==Constants.PROJECT_PLAN_CALLER)?Constants.PL_OVERVIEW_GET_PAGE:Constants.WO_GENERAL_INFO_GET_LIST%>);">

</BODY>
<script language = "javascript">
	function on_Submit()
	{					
		
		
		if(trim(frm_plConstraintAdd.plDescription.value) == ""){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plConstraintAdd.youmustenterdescription")%>");
  		 	frm_plConstraintAdd.plDescription.focus();
  		 	return;
  	 	}
		
		if(frm_plConstraintAdd.plDescription.value.length > 200)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.plConstraintAdd.thetextinthetextareaistoolong")%>");
			frm_plConstraintAdd.plDescription.focus();
			return;
		}
		
		// landd add start
		if(frm_plConstraintAdd.plNote.value.length > 200)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.plConstraintAdd.thetextinthetextareaistoolong")%>");
			frm_plConstraintAdd.plNote.focus();
			return;
		}
		// landd add end
		document.frm_plConstraintAdd.submit();
	}	
</script> 
</HTML>
