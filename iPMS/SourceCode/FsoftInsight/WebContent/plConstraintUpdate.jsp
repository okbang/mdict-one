<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*, com.fms1.web.*,java.util.*,java.io.*,java.text.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plConstraintUpdate.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js" >
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu();document.frm_plConstraintUpdate.plDescription.focus();">
<%
long constraintID = Long.parseLong(request.getParameter("plConstraint_ID"));
Vector constraintList = (Vector) session.getAttribute("PLConstraintList");
ConstraintInfo constraintInfo=null;
int i;
for(i = 0; i < constraintList.size(); i++){
 	constraintInfo = (ConstraintInfo) constraintList.elementAt(i);
 	if (constraintInfo.constraintID==constraintID)
 		break;
}
//can be called from WO or PP
String strCaller=(String)session.getAttribute("caller");
int caller; 
if (strCaller!=null)
	caller=Integer.parseInt(strCaller);
else
	caller=Constants.PROJECT_PLAN_CALLER;

%>
<form name="frm_plConstraintUpdate" action="Fms1Servlet#constraints" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.PL_CONSTRAINT_UPDATE %>">
<input type = "hidden" name="plConstraint_ID" value="<%=constraintInfo.constraintID %>">

<P class="TITLE"><%=(caller==Constants.PROJECT_PLAN_CALLER)?languageChoose.getMessage("fi.jsp.plConstraintAdd.ProjectplanConstraint"):languageChoose.getMessage("fi.jsp.plConstraintAdd.WorkOrderConstraint")%></P>

<br>
<TABLE cellspacing="1" class="Table">

<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Updateconstraint")%> </CAPTION>
    <TBODY>
		<TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Description")%>* </TD>
            <TD class="CellBGR3"><TEXTAREA name="plDescription" rows="4" cols="50"><%=((constraintInfo.description == null)? "": constraintInfo.description) %></TEXTAREA></TD>
        </TR>
        <!-- landd add Note start -->        
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Note")%></TD>
            <TD class="CellBGR3"><TEXTAREA name="plNote" rows="4" cols="50"><%=((constraintInfo.note == null)? "": constraintInfo.note) %></TEXTAREA></TD>
        </TR>
        <!-- landd add Note end -->
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Type")%>* </TD>
            <TD class = "CellBGR3">
            <SELECT name="plType" class="COMBO"  >
            	<OPTION value = "0" <%if( constraintInfo.type == 0){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Businessexpertise")%> </OPTION>
            	<OPTION value = "1" <%if( constraintInfo.type == 1){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Customerproblem")%> </OPTION>  
            	<OPTION value = "2" <%if( constraintInfo.type == 2){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Customerrelationship")%> </OPTION>
            	<OPTION value = "3" <%if( constraintInfo.type == 3){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Environment")%> </OPTION>
            	<OPTION value = "4" <%if( constraintInfo.type == 4){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Estimation")%> </OPTION>  
            	<OPTION value = "5" <%if( constraintInfo.type == 5){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Geography")%> </OPTION>    
            	<OPTION value = "6" <%if( constraintInfo.type == 6){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Requirement")%> </OPTION>
            	<OPTION value = "7" <%if( constraintInfo.type == 7){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Resource")%> </OPTION>  
            	<OPTION value = "8" <%if( constraintInfo.type == 8){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Schedule")%> </OPTION>    
            	<OPTION value = "9" <%if( constraintInfo.type == 9){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Technical")%> </OPTION>
            	<OPTION value = "10" <%if( constraintInfo.type == 10){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Projectmanagement")%> </OPTION>  
            	<OPTION value = "11" <%if( constraintInfo.type == 11){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Qualitymanagement")%> </OPTION>        
            </SELECT>
            </TD>
        </TR>
        
        
    </TBODY>
</TABLE>

</form>
<br>
<form name="frm_plConstraintDelete" action="Fms1Servlet#constraints" method = "get">
				<input type = "hidden" name="reqType" value="<%=Constants.PL_CONSTRAINT_DELETE%>" >
				<input type = "hidden" name="plConstraint_ID" value="<%=constraintInfo.constraintID%>">
</form>

<INPUT type="button" name="update2" value="<%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Update")%>" onclick="javascript:on_Submit1();" class="BUTTON">
<input type="button" name="delete" value="<%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Delete")%>"  onclick="javascript:on_Submit2();"  class="BUTTON">
<input type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.Cancel")%>"    class="BUTTON" onclick="doIt(<%=(caller==Constants.PROJECT_PLAN_CALLER)?Constants.PL_OVERVIEW_GET_PAGE:Constants.WO_GENERAL_INFO_GET_LIST%>);">
</BODY>
</HTML>
<script language = "javascript" src=jscript/javaFns.js></script>
<script language = "javascript">
	function on_Submit1()
	{
	
		if(trim(frm_plConstraintUpdate.plDescription.value) == ""){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plConstraintUpdate.youmustenterdescription")%>");
  		 	frm_plConstraintUpdate.plDescription.focus();
  		 	return;
  	 	}
		
		if(frm_plConstraintUpdate.plDescription.value.length > 200)
		{
			alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.The__length__of__this__field__must__be__below__200__characters__currently__~PARAM1_NUMBER~")%>",frm_plConstraintUpdate.plDescription.value.length)));
			frm_plConstraintUpdate.plDescription.focus();
			return;
		}
		// landd add start
		if(frm_plConstraintUpdate.plNote.value.length > 200)
		{
			alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.plConstraintUpdate.The__length__of__this__field__must__be__below__200__characters__currently__~PARAM1_NUMBER~")%>",frm_plConstraintUpdate.plNote.value.length)));
			frm_plConstraintUpdate.plNote.focus();
			return;
		}
		// landd add end
		document.frm_plConstraintUpdate.submit();
	}
	function on_Submit2()
	{
		
		if (window.confirm("<%= languageChoose.getMessage("fi.jsp.plConstraintUpdate.Areyousure")%>") != 0) {
			document.frm_plConstraintDelete.submit();
		}
		else{
			return;
		}	
		
	}		
</script> 
