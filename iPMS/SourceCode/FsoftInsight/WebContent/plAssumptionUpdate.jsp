<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*, com.fms1.web.*,java.util.*,java.io.*,java.text.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plAssumptionUpdate.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu();document.frm_plAssumptionUpdate.plDescription.focus();">
<%
	long assumptionID = Long.parseLong(request.getParameter("plAssumption_ID"));
	Vector assumptionList = (Vector) session.getAttribute("PLAssumptionList");
	ConstraintInfo assumptionInfo=null;
	int i;
	for(i = 0; i < assumptionList.size(); i++){
	 	assumptionInfo = (ConstraintInfo) assumptionList.elementAt(i);
	 	if (assumptionInfo.constraintID==assumptionID)
	 		break;
	}
	String strCaller=(String)session.getAttribute("caller");
	int caller;
	if (strCaller!=null)
		caller=Integer.parseInt(strCaller);
	else
		caller=Constants.PROJECT_PLAN_CALLER;
%>
<form name="frm_plAssumptionUpdate" action="Fms1Servlet#Assumptions">
<input type = "hidden" name="reqType" value="<%=Constants.PL_ASSUMPTION_UPDATE %>">
<input type = "hidden" name="plAssumption_ID" value="<%=assumptionInfo.constraintID %>">

<P class="TITLE"><%=(caller==Constants.PROJECT_PLAN_CALLER)?languageChoose.getMessage("fi.jsp.plAssumptionUpdate.ProjectplanAssumption"):languageChoose.getMessage("fi.jsp.plAssumptionUpdate.WorkOrderAssumption")%></P>
<br>
<TABLE cellspacing="1" class="Table">

<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Updateassumption")%> </CAPTION>
    <TBODY>
		<TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Description")%>* </TD>
            <TD class="CellBGR3"><TEXTAREA name="plDescription" rows="4" cols="50"><%=((assumptionInfo.description == null)? "": assumptionInfo.description) %></TEXTAREA></TD>
        </TR>
        <!-- landd add Note start -->        
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Note")%></TD>
            <TD class="CellBGR3"><TEXTAREA name="plNote" rows="4" cols="50"><%=((assumptionInfo.note == null)? "": assumptionInfo.note) %></TEXTAREA></TD>
        </TR>
        <!-- landd add Note end -->        
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Type")%> </TD>
            <TD class = "CellBGR3">
            <SELECT name="plType" class="COMBO"  >
            	<OPTION value = "0" <%if( assumptionInfo.type == 0){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Businessexpertise")%> </OPTION>
            	<OPTION value = "1" <%if( assumptionInfo.type == 1){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Customerproblem")%> </OPTION>  
            	<OPTION value = "2" <%if( assumptionInfo.type == 2){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Customerrelationship")%> </OPTION>
            	<OPTION value = "3" <%if( assumptionInfo.type == 3){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Environment")%> </OPTION>
            	<OPTION value = "4" <%if( assumptionInfo.type == 4){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Estimation")%> </OPTION>  
            	<OPTION value = "5" <%if( assumptionInfo.type == 5){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Geography")%> </OPTION>    
            	<OPTION value = "6" <%if( assumptionInfo.type == 6){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Requirement")%> </OPTION>
            	<OPTION value = "7" <%if( assumptionInfo.type == 7){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Resource")%> </OPTION>  
            	<OPTION value = "8" <%if( assumptionInfo.type == 8){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Schedule")%> </OPTION>    
            	<OPTION value = "9" <%if( assumptionInfo.type == 9){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Technical")%> </OPTION>
            	<OPTION value = "10" <%if( assumptionInfo.type == 10){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Projectmanagement")%> </OPTION>  
            	<OPTION value = "11" <%if( assumptionInfo.type == 11){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Qualitymanagement")%> </OPTION>        
            </SELECT>
            </TD>
        </TR>
        
        
    </TBODY>
</TABLE>

</form>
<br>
<form name="frm_plAssumptionDelete" action="Fms1Servlet#Assumptions" method = "get">
				<input type = "hidden" name="reqType" value="<%=Constants.PL_ASSUMPTION_DELETE%>" >
				<input type = "hidden" name="plAssumption_ID" value="<%=assumptionInfo.constraintID%>">
</form>

		<INPUT type="button" name="update2" value=" <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Update")%> " onclick="javascript:on_Submit1();" class="BUTTON">
		<input type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Delete")%> "  onclick="javascript:on_Submit2();"  class="BUTTON">
		<input type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Cancel")%> "    class="BUTTON" onclick="doIt(<%=(caller==Constants.PROJECT_PLAN_CALLER)?Constants.PL_OVERVIEW_GET_PAGE:Constants.WO_GENERAL_INFO_GET_LIST%>);">


</BODY>
<script language = "javascript">
	function on_Submit1()
	{
	
		if(trim(frm_plAssumptionUpdate.plDescription.value) == ""){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Youmustenterdescription")%>");
  		 	frm_plAssumptionUpdate.plDescription.focus();
  		 	return;
  	 	}
		
		if(frm_plAssumptionUpdate.plDescription.value.length > 200)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Thetextinthetextareaistoolong")%>");
			frm_plAssumptionUpdate.plDescription.focus();
			return;
		}
		
		// landd add start
		if(frm_plAssumptionUpdate.plNote.value.length > 200)
		{
			alert("<%= languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Thetextinthetextareaistoolong")%>");
			frm_plAssumptionUpdate.plNote.focus();
			return;
		}
		// landd add end
		document.frm_plAssumptionUpdate.submit();
	}
	function on_Submit2()
	{
		
		if (window.confirm("<%= languageChoose.getMessage("fi.jsp.plAssumptionUpdate.Areyousure")%>") != 0) {
			document.frm_plAssumptionDelete.submit();
		}
		else{
			return;
		}	
		
	}		
</script> 
</HTML>


