<%@page import="com.fms1.tools.*, com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>darPlanAdd.jsp</TITLE>

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
</HEAD>
<%	
	Vector userList = (Vector)session.getAttribute("userList");
	ProjectDateInfo prjDateInfo = (ProjectDateInfo)session.getAttribute("prjDateInfo");
	String focus = "frm.txtItem.focus();";
	if (userList.size() == 0) {
	    focus = "";
	}
%>
<BODY class="BD" onLoad="loadPrjMenu();<%=focus%>">
<P class="TITLE">DAR plan</P>
<% 
	if (userList.size() == 0) {%>
		<P class="ERROR"><%=languageChoose.getMessage("fi.jsp.darPlanAdd.Errormessage")%></P>
		<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Back")%>" onclick='doIt("<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>#darplan")'>
<%  } else {%>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_DARPLAN%>#darplan">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.darPlanAdd.Caption")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.darPlanAdd.Item")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="50" type="text" maxlength="100" name="txtItem"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" nowrap="nowrap"><%=languageChoose.getMessage("fi.jsp.darPlanAdd.Doer")%>*</TD>
            <TD class="CellBGRnews"><SELECT name="cmbConductor" class="COMBO">
                <%for(int i=0;i<userList.size();i++){
		            	AssignmentInfo assInfo = (AssignmentInfo) userList.elementAt(i);
               		%><OPTION value="<%=assInfo.devID%>"><%=assInfo.account%> - <%=assInfo.devName%></OPTION>
                <%}%>
            </SELECT></TD>
        </TR>        
        <TR >
            <TD class="ColumnLabel"  nowrap="nowrap"><%=languageChoose.getMessage("fi.jsp.darPlanAdd.Targetdate")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtPStartD">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPStartD()'>
            	(DD-MMM-YY)</TD>
        </TR>
        
        <TR>
            <TD class="ColumnLabel" nowrap="nowrap"><%=languageChoose.getMessage("fi.jsp.darPlanAdd.Actualdate")%></TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtPEndD">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPEndD()'>	
            	(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" nowrap="nowrap"><%=languageChoose.getMessage("fi.jsp.darPlanAdd.Cause")%></TD>
            <TD class="CellBGRnews"><TEXTAREA rows="5" cols="60" name="txtCause"></TEXTAREA></TD>
        </TR>
       
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Ok")%>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Cancel")%>" onclick="doAction(this)"></FORM>
<SCRIPT language="javascript">
var txtPrjStartD="<%=(prjDateInfo.startD== null)?"":CommonTools.dateFormat(prjDateInfo.startD)%>";
var txtPrjEndD="<%=(prjDateInfo.endD== null)?"":CommonTools.dateFormat(prjDateInfo.endD)%>";
function doAction(button)
{
  	if (button.name=="btnOk") {
  	
		if(!mandatoryFld(frm.txtItem,"<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Item")%> ")){
	  	  	return;  			  	  		
	  	}
  		if(!maxLength(frm.txtItem,"<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Item")%> ",100)){
  		  	return;
  	  	} 	  	  	  	
		if(!mandatoryFld(frm.txtPStartD,"<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Targetdate")%> ")){
	  	  	return;  			  	  		
	  	}
		if(!dateFld(frm.txtPStartD, "<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Targetdate")%> ")) {
	  		return;
	  	}
	  	if(compareDate(frm.txtPStartD.value,txtPrjStartD)==1){
		    window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Target_date_must_between_planned_start_date_and_re_planned_end_date_of_project_from_~PARAM1_DATE~_to_~PARAM2_DATE~")%> ",txtPrjStartD,txtPrjEndD)));
			frm.txtPStartD.focus();
			return;
	 	}
	 	if(compareDate(frm.txtPStartD.value,txtPrjEndD)==-1){
		    window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Target_date_must_between_planned_start_date_and_re_planned_end_date_of_project_from_~PARAM1_DATE~_to_~PARAM2_DATE~")%> ",txtPrjStartD,txtPrjEndD)));
			frm.txtPStartD.focus();
			return;
	 	}
	 	if (trim(frm.txtPEndD.value)!="") { 		 	 		 	 		 		 	
			if(!dateFld(frm.txtPEndD, "<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Actualdate")%> ")) {
		  		return;
		  	}
		  	if(compareDate(frm.txtPEndD.value,txtPrjStartD)==1){
		        window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Actual_date_must_between_planned_start_date_and_re_planned_end_date_of_project_from_~PARAM1_DATE~_to_~PARAM2_DATE~")%> ",txtPrjStartD,txtPrjEndD)));
				frm.txtPEndD.focus();
				return;
		 	}
		 	if(compareDate(frm.txtPEndD.value,txtPrjEndD)==-1){
		        window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Actual_date_must_between_planned_start_date_and_re_planned_end_date_of_project_from_~PARAM1_DATE~_to_~PARAM2_DATE~")%> ",txtPrjStartD,txtPrjEndD)));
				frm.txtPEndD.focus();
				return;
		 	} 			 				 			 		
	 	} 	 
	 	if(!maxLength(frm.txtCause,"<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Cause")%> ",300)){
  		  	return;
  	  	}	 	  	 	    		  	  		  	    		
  	  	frm.submit();  		 	
  	}
  	if (button.name=="btnCancel") {
  		doIt("<%=Constants.GET_QUALITY_OBJECTIVE_LIST%>#darplan");
  		return;
  	} 	  	
}
	function showPStartD(){
		showCalendar(frm.txtPStartD, frm.txtPStartD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showPEndD(){
		showCalendar(frm.txtPEndD, frm.txtPEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
</SCRIPT>
<%}%> 
</BODY>
</HTML>