<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>toolUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%

	ToolInfo toolInfo=(ToolInfo)session.getAttribute("toolInfo");
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	
	int sSize = 0;
	Vector vtStageList =(Vector)session.getAttribute("stageVector");
	if (vtStageList != null) sSize = vtStageList.size();
	
    String note=(toolInfo.note==null)?"":toolInfo.note;
    String source=(toolInfo.source==null)?"":toolInfo.source;
    String des=(toolInfo.description==null)?"":toolInfo.description;
%>

<BODY class="BD" onLoad="loadPrjMenu();">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.toolUpdate.ProjectPlanInfrastructures")%></p>
<BR/>
<FORM name="frm" method="post" action="Fms1Servlet?reqType=<%=Constants.UPDATE_TOOL%>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.toolUpdate.UpdateInfrastructure")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="180"><INPUT size="20" type="hidden" name="txtToolID" value="<%=toolInfo.toolID %>"><%=languageChoose.getMessage("fi.jsp.toolDetails.WorkProduct")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="60" type="text" maxlength="60" name="txtName" value="<%=toolInfo.name%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" style="height:57px"><%=languageChoose.getMessage("fi.jsp.toolUpdate.Purpose")%>*</TD>
            <TD class="CellBGRnews" style="height:57px"><TEXTAREA style="width:100%;height:100%" name="txtPurpose"><%=toolInfo.purpose%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolUpdate.Source")%></TD>
            <TD class="CellBGRnews"><INPUT size="60" type="text" name="txtSource" maxlength="60" value="<%=source%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" style="height:57px"><%=languageChoose.getMessage("fi.jsp.toolUpdate.Description")%></TD>
            <TD class="CellBGRnews" style="height:57px"><TEXTAREA style="width:100%;height:100%" name="txtDescription"><%=des%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolUpdate.Status")%></TD>
            <TD class="CellBGRnews"><SELECT name="cmbStatus" class="COMBO">
                <OPTION value="Exist" <%if(toolInfo.status.compareTo(languageChoose.getMessage("fi.jsp.toolUpdate.Exist"))==0){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.toolUpdate.Exist")%> </OPTION>
                <OPTION value="Create" <%if(toolInfo.status.compareTo(languageChoose.getMessage("fi.jsp.toolUpdate.Create"))==0){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.toolUpdate.Create")%> </OPTION>
                <OPTION value="Buy" <%if(toolInfo.status.compareTo(languageChoose.getMessage("fi.jsp.toolUpdate.Buy"))==0){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.toolUpdate.Buy")%> </OPTION>
                <OPTION value="Other" <%if(toolInfo.status.compareTo(languageChoose.getMessage("fi.jsp.toolUpdate.Other"))==0){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.toolUpdate.Other")%> </OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolUpdate.ExpectedAvailabilityBy")%>*</TD>
            <TD class="CellBGRnews">            	
            	<SELECT name = "ex_avail_stage" class = "COMBO">            	
		       	<%
		       		String strStageName = "";
		       		for (int i = 0; i < sSize; i++) {
		       			strStageName = ((StageInfo) vtStageList.elementAt(i)).stage;
		       	%>	             
			        <option value="<%= strStageName%>" <%=strStageName.equalsIgnoreCase(toolInfo.expected_available_stage) ? "selected":""%>><%=strStageName%> </option>
			   	<%
			   		}
			   	%>
	            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolUpdate.ActualAvailabilityBy")%></TD>
            <TD class="CellBGRnews">            	
            	<SELECT name = "ac_avail_stage" class = "COMBO">            	
		       	<%		       		
		       		for (int i = 0; i < sSize; i++) {
		       			strStageName = ((StageInfo) vtStageList.elementAt(i)).stage;
		       	%>	             
			             <option value="<%= strStageName%>" <%=strStageName.equalsIgnoreCase(toolInfo.actual_available_stage) ? "selected":""%>><%=strStageName%> </option>
			   	<%
			   		}
			   	%>
	            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.toolUpdate.Type")%></TD>
            <TD class="CellBGRnews"><SELECT name = "tool_type" class = "COMBO"  style="width:180px">             
             <option <%=(toolInfo.tool_type == 6)?"selected":""%> value='6'> <%=languageChoose.getMessage("fi.jsp.toolUpdate.DevelopmentEnvironment")%> </option>
             <option <%=(toolInfo.tool_type == 7)?"selected":""%> value='7'> <%=languageChoose.getMessage("fi.jsp.toolUpdate.Hardware_N_Software")%> </option>
             <option <%=(toolInfo.tool_type == 8)?"selected":""%> value='8'> <%=languageChoose.getMessage("fi.jsp.toolUpdate.OtherTools")%> </option>             
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" style="height:57px"><%=languageChoose.getMessage("fi.jsp.toolUpdate.Note")%></TD>
            <TD class="CellBGRnews" style="height:57px"><TEXTAREA style="width:100%;height:100%" name="txtNote" ><%=note%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<INPUT  type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT  type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD) %>">
<BR>
<INPUT type="button" class="BUTTON" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.toolUpdate.OK")%>" onclick="update();"> <INPUT type="reset" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.toolUpdate.Cancel")%>" onclick="doIt(<%=Constants.GET_TOOL_LIST%>);"></FORM>
<SCRIPT language="javascript">
  function update()
  {
  	  if(trim(frm.txtName.value)==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.toolUpdate.Thisfieldismandatory")%>");
  	  	frm.txtName.focus();  	
  	  	return;  		
  	  		
  	  }
  	  if(frm.txtName.value.length>50){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.toolUpdate.LengthOfNameFieldCanNotBeGreaterThan50")%>");
  	  	frm.txtName.focus();  	
  	  	return;  		
  	  		
  	  }
  	   
  	   if(trim(frm.txtPurpose.value)==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.toolUpdate.Thisfieldismandatory")%>");
  	  	frm.txtPurpose.focus();
  	  	return;  		
  	  }
  	  
  	   if(frm.ex_avail_stage.value=='0'){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.toolAdd.Thisfieldismandatory")%>");
  	  	frm.ex_avail_stage.focus();
  	  	return;  		
  	  }
  	  if(frm.txtSource.value.length>50){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.toolUpdate.LengthOfSourceFieldCanNotBeGreaterThan50")%>");
  	  	frm.txtSource.focus();  	
  	  	return;  		
  	  		
  	  }
  	   
  	  if(frm.txtDescription.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.toolUpdate.Lengthofthisfield")%>");
  			frm.txtDescription.focus();
  			return;
  	  }	 	
  	  if(frm.txtPurpose.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.toolUpdate.Lengthofthisfield")%>");
  			frm.txtPurpose.focus();
  			return;
  	  }	
  	  if(frm.txtNote.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.toolUpdate.Lengthofthisfield")%>"); 
  			frm.txtNote.focus();
  			return;
  	  }
  	 
  	  frm.submit();	    	    	
  }
 </SCRIPT>
 </BODY>
</HTML>
