<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plAssumptionAdd.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu();document.frm_plAssumptionAdd.plDescription.focus();">
<%
String strCaller=(String)session.getAttribute("caller");
int caller;
if (strCaller!=null)
	caller=Integer.parseInt(strCaller);
else
	caller=Constants.PROJECT_PLAN_CALLER;
%>
<form name="frm_plAssumptionAdd" action="Fms1Servlet#Assumptions" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.PL_ASSUMPTION_ADD%>">

<P class="TITLE"><%=((caller==Constants.PROJECT_PLAN_CALLER)?languageChoose.getMessage("fi.jsp.plAssumptionAdd.ProjectplanAssumption"):languageChoose.getMessage("fi.jsp.plAssumptionAdd.WorkOrderAssumption"))%></P>

<br>
<TABLE cellspacing="1" class="Table">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Addnewassumption")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Description")%>* </TD>
            <TD class="CellBGR3"><TEXTAREA name="plDescription" rows="4" cols="50"></TEXTAREA></TD>
        </TR>
        <!-- landd add Note start -->
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Note")%></TD>
            <TD class="CellBGR3"><TEXTAREA name="plNote" rows="4" cols="50"></TEXTAREA></TD>
        </TR>
        <!-- landd add Note end -->
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Type")%> </TD>
            <TD class = "CellBGR3">
            <SELECT name="plType" class="COMBO"  >
            	<OPTION value = "0" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Businessexpertise")%> </OPTION>
            	<OPTION value = "1" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Customerproblem")%> </OPTION>  
            	<OPTION value = "2" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Customerrelationship")%> </OPTION>
            	<OPTION value = "3" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Environment")%> </OPTION>
            	<OPTION value = "4" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Estimation")%> </OPTION>  
            	<OPTION value = "5" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Geography")%> </OPTION>    
            	<OPTION value = "6" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Requirement")%> </OPTION>
            	<OPTION value = "7" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Resource")%> </OPTION>  
            	<OPTION value = "8" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Schedule")%> </OPTION>    
            	<OPTION value = "9" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Technical")%> </OPTION>
            	<OPTION value = "10" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Projectmanagement")%> </OPTION>  
            	<OPTION value = "11" > <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Qualitymanagement")%> </OPTION>        
            </SELECT>
            </TD>
        </TR>
    </TBODY>
</TABLE>

</form>
<br>
<INPUT type="button" name="Add" value=" <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.OK")%> " onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.plAssumptionAdd.Cancel")%> " class="BUTTON" onclick="doIt(<%=(caller==Constants.PROJECT_PLAN_CALLER)?Constants.PL_OVERVIEW_GET_PAGE:Constants.WO_GENERAL_INFO_GET_LIST%>);">
</BODY>
<script language = "javascript">
	function on_Submit()
	{					
		
		if(trim(frm_plAssumptionAdd.plDescription.value) == ""){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plAssumptionAdd.youmustenterdescription")%>");
  		 	frm_plAssumptionAdd.plDescription.focus();
  		 	return;
  	 	}
		
		if(frm_plAssumptionAdd.plDescription.value.length > 200)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.plAssumptionAdd.thetextinthetextareaistoolong")%>");
			frm_plAssumptionAdd.plDescription.focus();
			return;
		}
		// landd add note validation start
		if(frm_plAssumptionAdd.plNote.value.length > 200)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.plAssumptionAdd.thetextinthetextareaistoolong")%>");
			frm_plAssumptionAdd.plNote.focus();
			return;
		}
		// landd add note validation end
		
		document.frm_plAssumptionAdd.submit();
	}	
</script> 
</HTML>


