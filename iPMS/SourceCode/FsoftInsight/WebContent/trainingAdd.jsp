<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>trainingAdd.jsp</TITLE>
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
	String title = "" ;
	if(caller!=Constants.SCHEDULE_CALLER){ //MANU :called from project plan
		//title=languageChoose.getMessage("fi.jsp.trainingAdd.ProjectPlanTrainingplan") ;
		fromProjectPlan=true;
	}		
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtTopic.focus();">
<p class="TITLE"><%=(caller==Constants.SCHEDULE_CALLER)? languageChoose.getMessage("fi.jsp.trainingAdd.ScheduleTrainingplan") : languageChoose.getMessage("fi.jsp.trainingAdd.ProjectPlanTrainingplan")%></p>
<%	
	String err=(String)session.getAttribute("trainingError");
	if(err!=null&&err!="")	
	{%><p class="ERROR"><%=err%></p>
		<%session.setAttribute("trainingError","");
	}%>

<FORM name="frm" method="Post" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_TRAINING%>">
<TABLE class="Table" cellspacing="1" >
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Addnewtrainingplan")%></CAPTION>
    <TBODY>
    	<TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Topic")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="51" type="text" maxlength="50" name="txtTopic"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Participants")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtParticipant"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Duration")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="51" type="text" maxlength="50" name="txtDuration"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Waiver")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtWaiver"></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<INPUT  type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT  type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">

<P>
<INPUT type="button" class="BUTTON" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.trainingAdd.OK")%>" onclick="add();"> <INPUT type="button" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.trainingAdd.Cancel")%>" class="BUTTON" onclick="doIt(<%=((caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_TRAINING_PLAN_GET_LIST:Constants.GET_TRAINING_LIST)%>);"></P>
</FORM>
<SCRIPT language="javascript">
  function add()
  {
  
  	  if(trim(frm.txtTopic.value)==""){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingAdd.Thisfieldismandatory")%>");
  			frm.txtTopic.focus();
  			return;
  	  }	 
  	  
  	  if(frm.txtTopic.value.length>50){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingAdd.TopicLengthCanNotBeGreaterThan50")%>");
  			frm.txtTopic.focus();
  			return;
  	  }	
  	   
  	  if(trim(frm.txtParticipant.value)==""){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingAdd.Thisfieldismandatory")%>");
  			frm.txtParticipant.focus();
  			return;
  	  }	 
  	   if(frm.txtParticipant.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingAdd.Lengthofthisfield")%>");
  			frm.txtParticipant.focus();
  			return;
  	  }	

  	  if(trim(frm.txtDuration.value)==""){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingAdd.Thisfieldismandatory")%>");
  			frm.txtDuration.focus();
  			return;
  	  }	 
  	  
  	  if(frm.txtDuration.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingAdd.Lengthofthisfield")%>");
  			frm.txtDuration.focus();
  			return;
  	  }	  
  	  
  	  if(trim(frm.txtWaiver.value)==""){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingAdd.Thisfieldismandatory")%>");
  			frm.txtWaiver.focus();
  			return;
   	  }	    	  
  	  	
  	 
  	  if(frm.txtWaiver.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.trainingAdd.Lengthofthisfield")%>");
  			frm.txtWaiver.focus();
  			return;
  	  }	  
  	  
  	  frm.submit();	    	    	
  }
 </SCRIPT> 

</BODY>
</HTML>
