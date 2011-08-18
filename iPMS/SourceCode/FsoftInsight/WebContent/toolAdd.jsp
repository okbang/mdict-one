<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>toolAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	int sSize = 0;
	Vector vtStageList =(Vector)session.getAttribute("stageVector");
	if (vtStageList != null) sSize = vtStageList.size();
	
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtName.focus();">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.toolAdd.ProjectPlanInfrastructures")%></p>
<br/>
<FORM name="frm" method="post" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_TOOL%>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.toolAdd.AddnewInfrastructure")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="180"><%=languageChoose.getMessage("fi.jsp.toolAdd.WorkProduct")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="60" type="text" maxlength="60" name="txtName"></TD>
        </TR>
        <TR >
            <TD class="ColumnLabel" style="height:57px"><%=languageChoose.getMessage("fi.jsp.toolAdd.Purpose")%>*</TD>
            <TD class="CellBGRnews" style="height:57px"><TEXTAREA style="width:100%;height:100%" name="txtPurpose"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" ><%=languageChoose.getMessage("fi.jsp.toolAdd.Source")%></TD>
            <TD class="CellBGRnews"><INPUT size="60" type="text" name="txtSource" maxlength="60"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" style="height:57px"><%=languageChoose.getMessage("fi.jsp.toolAdd.Description")%></TD>
            <TD class="CellBGRnews" style="height:57px"><TEXTAREA style="width:100%;height:100%" name="txtDescription"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolAdd.Status")%></TD>
            <TD class="CellBGRnews">
            	<SELECT name="cmbStatus" class="COMBO">
	                <OPTION value="Exist"> <%=languageChoose.getMessage("fi.jsp.toolAdd.Exist")%> </OPTION>
	                <OPTION value="Create"> <%=languageChoose.getMessage("fi.jsp.toolAdd.Create")%> </OPTION>
	                <OPTION value="Buy"> <%=languageChoose.getMessage("fi.jsp.toolAdd.Buy")%> </OPTION>
	                <OPTION value="Other" selected> <%=languageChoose.getMessage("fi.jsp.toolAdd.Other")%> </OPTION>
            	</SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" ><%=languageChoose.getMessage("fi.jsp.toolAdd.ExpectedAvailabilityBy")%>*</TD>
            <TD class="CellBGRnews">            	
            	<SELECT name = "ex_avail_stage" class = "COMBO">
            	<option value='0'></option>
	       <%
	       		String strStageName = "";
	       		for (int i = 0; i < sSize; i++) {
	       			strStageName = ((StageInfo) vtStageList.elementAt(i)).stage;
	       %>	             
		             <option value="<%= strStageName%>"><%=strStageName%> </option>
		   <%
		   		}
		   %>
	            </SELECT>
            </TD>
        </TR>        
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.toolAdd.Type")%> </TD>
            <TD class="CellBGRnews"><SELECT name = "tool_type" class = "COMBO" style="width:180px">             
             <option value='6' selected> <%=languageChoose.getMessage("fi.jsp.toolAdd.DevelopmentEnvironment")%> </option>
             <option value='7'> <%=languageChoose.getMessage("fi.jsp.toolAdd.Hardware_N_Software")%> </option>
             <option value='8'> <%=languageChoose.getMessage("fi.jsp.toolAdd.OtherTools")%> </option>             
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" style="height:57px"><%=languageChoose.getMessage("fi.jsp.toolAdd.Note")%></TD>
            <TD class="CellBGRnews" style="height:57px"><TEXTAREA style="width:100%;height:100%" name="txtNote"></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<INPUT  type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT  type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">
<BR>
<INPUT type="button" class="BUTTON" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.toolAdd.OK")%>" onclick="add();"> 
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.toolAdd.Cancel")%>" onclick="doIt(<%=Constants.GET_TOOL_LIST%>);">
</FORM>
<SCRIPT language="javascript">
  function add()
  {
  	  if(trim(frm.txtName.value)==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.toolAdd.Thisfieldismandatory")%>"); 
  	  	frm.txtName.focus();  	
  	  	return;  		
  	  		
  	  }
  	  if(frm.txtName.value.length>50){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.toolAdd.LengthOfNameFieldCanNotBeGreaterThan50")%>");
  	  	frm.txtName.focus();  	
  	  	return;  		
  	  		
  	  }
  	   
  	  if(trim(frm.txtPurpose.value)==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.toolAdd.Thisfieldismandatory")%>");
  	  	frm.txtPurpose.focus();
  	  	return;  		
  	  }
  	  
  	  if(frm.ex_avail_stage.value=='0'){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.toolAdd.Thisfieldismandatory")%>");
  	  	frm.ex_avail_stage.focus();
  	  	return;  		
  	  }
  	  if(frm.txtSource.value.length>50){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.toolAdd.LengthOfSourceFieldCanNotBeGreaterThan50")%>");
  	  	frm.txtSource.focus();  	
  	  	return;  	  		
  	  }  	  
  	  
  	  if(frm.txtDescription.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.toolAdd.Lengthofthisfield")%>");
  			frm.txtDescription.focus();
  			return;
  	  }	 	
  	  if(frm.txtPurpose.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.toolAdd.Lengthofthisfield")%>");
  			frm.txtParticipant.focus();
  			return;
  	  }	
  	  if(frm.txtNote.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.toolAdd.Lengthofthisfield")%>");
  			frm.txtWaiver.focus();
  			return;
  	  }	 
  	 
  	  frm.submit();	    	    	
  }
 </SCRIPT> 

</BODY>
</HTML>
