<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>trainingUpdate.jsp</TITLE>
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

	boolean fromProjectPlan=false;
	int caller=Integer.parseInt(session.getAttribute("caller").toString());
	String title= "" ;
	if(caller!=Constants.SCHEDULE_CALLER){ //MANU :called from project plan
		//title=languageChoose.getMessage("fi.jsp.trainingUpdate.ProjectPlan")+title;
		fromProjectPlan=true;
	}
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	Vector vt=(Vector)session.getAttribute("trainingVector");
	
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	TrainingInfo trainInfo=(TrainingInfo)vt.get(vtID);
	String top=(trainInfo.topic != null) ? trainInfo.topic : "";
	String par=(trainInfo.participant != null) ? trainInfo.participant : "";
	String dur=(trainInfo.duration != null) ? trainInfo.duration : "";
	String wai=(trainInfo.waiver != null) ? trainInfo.waiver : "";
   	String verifyBy=trainInfo.getVerifyBy();
%> 
<BODY class="BD" onLoad="loadPrjMenu();frm.txtTopic.focus();">
<p class="TITLE"><%=(caller==Constants.SCHEDULE_CALLER)? languageChoose.getMessage("fi.jsp.trainingUpdate.Schedule"):languageChoose.getMessage("fi.jsp.trainingUpdate.ProjectPlan")%></p>
<FORM name="frm" method="Post" action="Fms1Servlet?reqType=<%=Constants.UPDATE_TRAINING%>">
<TABLE class="Table" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.trainingUpdate.Updatetrainingplan")%> </CAPTION>
    <TBODY>
    	<TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Topic")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="50" name="txtTopic" value='<%=top%>' type="text" maxlength="50"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Participants")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtParticipant"><%=par%></TEXTAREA></TD>
        </TR>
    	<TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Duration")%>*<INPUT size="20" type="hidden" name="trainingID" value="<%=trainInfo.trainingID%>"></TD>
            <TD class="CellBGRnews"><INPUT size="50" name="txtDuration" value='<%=dur%>' type="text" maxlength="50"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Waiver")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtWaiver"><%=wai%></TEXTAREA></TD>
        </TR>   
    </TBODY>
</TABLE>
<INPUT  type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT  type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">

<P>
<INPUT type="button" class="BUTTON" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.trainingUpdate.OK")%>" onclick="update();"> <INPUT type="button" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.trainingUpdate.Cancel")%>" class="BUTTON" onclick="doIt(<%=((caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_TRAINING_PLAN_GET_LIST:Constants.GET_TRAINING_LIST)%>);"></P>
</FORM>
<SCRIPT language="javascript">
  function update()  
  {
  	  if(frm.txtTopic.value==0){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingUpdate.Thisfieldismandatory")%>");
  	  		frm.txtTopic.focus();
  			return;
  	  }	 
  	  
  	  if(frm.txtTopic.value.length>50){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingUpdate.TopicLengthCanNotBeGreaterThan50Topic")%>");
  			frm.txtTopic.focus();
  			return;
  	  }	
  	  if(frm.txtParticipant.value==0){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingUpdate.Thisfieldismandatory")%>");
  			frm.txtParticipant.focus();
  			return;
  	  }	 
  	  if(frm.txtParticipant.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingUpdate.Lengthofthisfield")%>");
  			frm.txtParticipant.focus();
  			return;
  	  }
  	  
  	  if(frm.txtDuration.value==0){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingUpdate.Thisfieldismandatory")%>");
  			frm.txtDuration.focus();
  			return;
  	  }	  
  	  if(frm.txtDuration.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingUpdate.Lengthofthisfield")%>");
  			frm.txtDuration.focus();
  			return;
  	  }	 

  	  if(frm.txtWaiver.value==0){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingUpdate.Thisfieldismandatory")%>");
  			frm.txtWaiver.focus();
  			return;
  	  }	 	  
  	  if(frm.txtWaiver.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingUpdate.Lengthofthisfield")%>");
  			frm.txtWaiver.focus();
  			return;
  	  }	 
  	  
  	  frm.submit();	    	    	
  }
 </SCRIPT> 
</BODY>
</HTML>
