<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>stageAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%

	int caller=Integer.parseInt(session.getAttribute("caller").toString());
	String title;
	if(caller==Constants.SCHEDULE_CALLER){ //called from shedule
		title=languageChoose.getMessage("fi.jsp.stageAdd.Schedule");
	}
	else{ //called from project plan
		title=languageChoose.getMessage("fi.jsp.stageAdd.Projectplan") ;
	}
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
 	ProjectInfo prjInfo = Project.getProjectInfo(Long.parseLong((String) session.getAttribute("projectID")));
	String source = request.getParameter("source");
	Vector vt=(Vector)session.getAttribute("stageVector");
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtStage.focus();">
<P class="TITLE"><%=title%></p>
<FORM name="miles">
<%
Date latestEnd=null;
Date latestPlannedEnd=null;
StageInfo stInf;
for(int i=0;i<vt.size();i++){
stInf=(StageInfo)vt.elementAt(i);
if (stInf.aEndD!=null ){
	if (latestEnd==null ||latestEnd.before(stInf.aEndD)){
		latestEnd=stInf.aEndD;
		latestPlannedEnd=stInf.plannedEndDate;
	}
}
%>
	<INPUT  type="hidden" name="miles<%=i%>" value="<%=stInf.stage.toUpperCase()%>">
<%}%>
</FORM>

<FORM method="post" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_STAGE%>" name="frm">
<TABLE class="Table" width="640" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.stageAdd.Addnewstage")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageAdd.Stage")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="33" type="text" maxlength="30" name="txtStage"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.stageAdd.Standardstage")%>* </TD>
            <TD class="CellBGRnews"><SELECT name="cmbStandardStage" class="COMBO">                
                <OPTION value="0"></OPTION>
                <%for (int i=1;i<7;i++){%>
                <OPTION value="<%=i%>"><%=languageChoose.getMessage(StageInfo.getStageName(i))%></OPTION>
                <%}%>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageAdd.Plannedenddate")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="10" type="text" maxlength="10" name="txtBEndD">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showBEndD()'>
			</TD>        
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageAdd.Re-plannedenddate")%></TD>
            <TD class="CellBGRnews"><INPUT size="10" type="text" maxlength="10" name="txtPEndD">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPEndD()'>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageAdd.Actualenddate")%></TD>
            <TD class="CellBGRnews"><INPUT size="10" type="text" maxlength="10" name="txtAEndD">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showAEndD()'>
			</TD>
        </TR>        
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageAdd.Description")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtDescription"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.stageAdd.Milestone")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtMilestone"></TEXTAREA></TD>
        </TR>        
        
    </TBODY> 
</TABLE>
<font color="red"> <%=languageChoose.getMessage("fi.jsp.stageAdd.Pleasemapyourstagewithstandard")%> </font>
<BR><BR>
<INPUT  type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT  type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">
<p><INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.stageAdd.OK")%>" class="BUTTON" onclick="add();"> <INPUT type="button" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.stageAdd.Cancel")%>" class="BUTTON" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_STAGE_GET_LIST:Constants.WO_DELIVERABLE_GET_LIST%>);"></p>
<%if("1".equals(source)){ %>
  	    <INPUT  type="hidden" name="source" value="1">	  
 <%}%>
</FORM>
<SCRIPT language="javascript">
	function showBEndD(){
		showCalendar(frm.txtBEndD, frm.txtBEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showPEndD(){
		showCalendar(frm.txtPEndD, frm.txtPEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showAEndD(){
		showCalendar(frm.txtAEndD, frm.txtAEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
		
  function add() 
  {	 
  	  if(!(mandatoryFld(frm.txtStage, "<%=languageChoose.getMessage("fi.jsp.stageAdd.Stagename")%>")
		&& mandatorySelect(frm.cmbStandardStage, "<%=languageChoose.getMessage("fi.jsp.stageAdd.Standardstage")%>")
		&& maxLength(frm.txtStage,"<%=languageChoose.getMessage("fi.jsp.stageAdd.Stagename")%>",30)
		&& mandatoryDateFld(frm.txtBEndD, "<%=languageChoose.getMessage("fi.jsp.stageAdd.Plannedenddate")%>")
		&& mandatoryFld(frm.txtDescription, "<%=languageChoose.getMessage("fi.jsp.stageAdd.Description")%>")
		&& maxLength(frm.txtDescription, "<%=languageChoose.getMessage("fi.jsp.stageAdd.Description")%>",400)
		&& mandatoryFld(frm.txtMilestone, "<%=languageChoose.getMessage("fi.jsp.stageAdd.Milestone")%>")
		&& maxLength(frm.txtMilestone, "<%=languageChoose.getMessage("fi.jsp.stageAdd.Milestone")%>",200)))
		return;
							
	    var stage=frm.txtStage.value.toUpperCase();
  	  for(i=0;i<miles.elements.length;i++){
  	  	if(stage==miles.elements[i].value){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.stageAdd.ThisStageNameExists")%>");
  	  		frm.txtStage.focus();
  	  		return;
  	  	}
  	  } 
  	    	  
  	  if(compareDate(frm.txtBEndD.value,frm.txtPrjStartD.value)==1){
			 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.stageAdd.Planned__end__date__must__be__after__planned__start__date__of__project__~PARAM1_PJSDATE~")%>',frm.txtPrjStartD.value)));  
  		 	 frm.txtBEndD.focus();
  		 	 return;
  	  }
  	  
  	  if(compareDate(frm.txtBEndD.value,frm.txtPrjEndD.value)==-1){
			 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.stageAdd.Planned__end__date__must__be__before__end__date__of__project__~PARAM1_PJEDATE~")%>',frm.txtPrjEndD.value)));
  		 	 frm.txtBEndD.focus();
  		 	 return;
  	  }
  	  
  	  if(trim(frm.txtPEndD.value)!=""){
  	  		if(!(isDate(frm.txtPEndD.value))){  	  
  		  		window.alert("<%=languageChoose.getMessage("fi.jsp.stageAdd.Invaliddateformat")%>");
  		  		frm.txtPEndD.focus();
  		  		return;
  		  	}  	
  		    if(compareDate(frm.txtPEndD.value,frm.txtPrjStartD.value)==1){
				 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.stageAdd.Replanned__end__date__must__be__after__planned__start__date__of__project__~PARAM1_PJSDATE~")%>',frm.txtPrjStartD.value)));
  		 		 frm.txtPEndD.focus();
  		 		 return;
  			}
  	  
  	  		if(compareDate(frm.txtPEndD.value,frm.txtPrjEndD.value)==-1){
				 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.stageAdd.Replanned__end__date__must__be__before__planned__end__date__of__project__~PARAM1_PJEDATE~")%>',frm.txtPrjEndD.value)));
  		 		 frm.txtPEndD.focus();
  		 		 return;
  	 		 }	
  	 		 	  	
  	  
  	 <% if (latestPlannedEnd!=null ) {%> 
  	 		if(compareDate(frm.txtPEndD.value,"<%=CommonTools.dateFormat(latestPlannedEnd)%>")>=  0){
  	  			 window.alert("<%= languageChoose.getMessage("fi.jsp.stageAdd.ReplannedEndDateOfTheStageMustBeAfterLatestClosedStageRePlannedEndDate")%>(<%=CommonTools.dateFormat(latestPlannedEnd)%>)");
  		 		 frm.txtPEndD.focus();
  		 		 return;
  			}
  	 	
  		}
  		else if(compareDate(frm.txtBEndD.value,"<%=CommonTools.dateFormat(latestPlannedEnd)%>")>=  0){
  	  			 window.alert("<%= languageChoose.getMessage("fi.jsp.stageAdd.PlannedEndDateOfTheStageMustBeAfterLatestClosedStageRePlannedEndDate")%>(<%=CommonTools.dateFormat(latestPlannedEnd)%>)");
  		 		 frm.txtBEndD.focus();
  		 		 return;
  		}
	<%}else{%>
		}
	<%}%>
  	  
  	  
  	  if(trim(frm.txtAEndD.value)!=""){
  	  		if(!beforeTodayFld(frm.txtAEndD,"<%=languageChoose.getMessage("fi.jsp.stageAdd.Actualenddate")%>"))
				return;
<% if (prjInfo.getActualFinishDate() !=null ) {%> 	
  		  	if(compareDate(frm.txtAEndD.value,"<%=CommonTools.dateFormat(prjInfo.getActualFinishDate())%>")== - 1){
  	  			 window.alert("<%= languageChoose.getMessage("fi.jsp.stageAdd.ActualEndDateOfTheStageMustBeBeforeActualEndDateOfProject")%> (<%=CommonTools.dateFormat(prjInfo.getActualFinishDate())%>)");
  		 		 frm.txtPEndD.focus();
  		 		 return;
  			}
<%}
if (latestEnd!=null ) {%> 	
  		  
  		  	if(compareDate(frm.txtAEndD.value,"<%=CommonTools.dateFormat(latestEnd)%>")>=  0){
  	  			 window.alert("<%= languageChoose.getMessage("fi.jsp.stageAdd.ActualEndDateOfTheStageMustBeAfterLatestStageActualEndDate")%>(<%=CommonTools.dateFormat(latestEnd)%>)");
  		 		 frm.txtPEndD.focus();
  		 		 return;
  			}
<%}
if (prjInfo.getStartDate() !=null ){%>
  			if(compareDate(frm.txtAEndD.value,"<%=CommonTools.dateFormat(prjInfo.getStartDate())%>")== 1){
  	  			 window.alert("<%= languageChoose.getMessage("fi.jsp.stageAdd.ActualEndDateOfTheStageMustBeAfterActualStartDateOfProject")%>(<%=CommonTools.dateFormat(prjInfo.getStartDate())%>)");
  		 		 frm.txtPEndD.focus();
  		 		 return;
  			}
<%}%>		
  	  }
  	  frm.submit();	    	    	
  }
 </SCRIPT>
</BODY>
</HTML>
