<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML> 
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>StageUpdate.jsp</TITLE>

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
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
	// landd add start
	String callerSession = session.getAttribute("caller").toString();
	int caller = 0;
	if (null != callerSession){
		caller=Integer.parseInt(callerSession);
	}
	// landd add end	
	
	String title;
	if(caller==Constants.SCHEDULE_CALLER) //called from shedule
		title=languageChoose.getMessage("fi.jsp.stageUpdate.Schedule");
	else //called from work order
		title=languageChoose.getMessage("fi.jsp.stageUpdate.WorkOrder") ;

	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");	
	ProjectInfo prjInfo = Project.getProjectInfo(Long.parseLong((String) session.getAttribute("projectID")));
	Vector vt=(Vector)session.getAttribute("stageVector");
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	String source = request.getParameter("source");
	if(source == null)
		source = "0";

	StageInfo info=(StageInfo)vt.get(vtID);	
	boolean prevClosed=true;
	if (vtID>0){
		StageInfo infoPrev=(StageInfo)vt.get(vtID-1);
		prevClosed= (infoPrev.aEndD!=null)	;		
	}
	// constraint : The end date must be between previous stage end date and next stage end date
	String previousEndDate =null;
	String nextEndDate =null;
	if (vtID>0){// there is a previous stage
		StageInfo previousStage=(StageInfo)vt.get(vtID-1);
		if(previousStage.aEndD!=null)
			previousEndDate=CommonTools.dateFormat(previousStage.aEndD);
	}
	if (vtID<(vt.size()-1)){// there is a next stage
		StageInfo nextStage=(StageInfo)vt.get(vtID+1);
		if(nextStage.aEndD!=null)
			nextEndDate=CommonTools.dateFormat(nextStage.aEndD);
	}
	String milestone="";
	String description="";	
	int standardStage = 0;
	if(!info.milestone.equalsIgnoreCase("N/A"))
		milestone=info.milestone;  							
  	if(!info.description.equalsIgnoreCase("N/A"))
  		description=info.description;
    String pEndD=(info.pEndD!=null)?CommonTools.dateFormat(info.pEndD):"";
 	String aEndD=(info.aEndD!=null)?CommonTools.dateFormat(info.aEndD):"";
 	String bEndD=(info.bEndD!=null)?CommonTools.dateFormat(info.bEndD):"";
    standardStage = info.StandardStage;
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtStage.focus();">
<P class="TITLE"><%=title%></p>
<FORM name="miles">
<%for(int i=0;i<vt.size();i++){
	if (i!=vtID){%>
<INPUT  type="hidden" name="miles<%=i%>" value="<%=((StageInfo)vt.elementAt(i)).stage.toUpperCase()%>">
	<%}
}%>
</FORM>
<FORM method="post" action="Fms1Servlet?reqType=<%=Constants.UPDATE_STAGE%>" name="frm">
<INPUT  type="hidden" name="vtIDstr" value="<%=vtID%>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.stageUpdate.Updatestage")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageUpdate.Stage")%>*</TD>
            <TD class="CellBGRnews"><INPUT type="hidden" name="txtMilestoneID" value="<%=info.milestoneID%>">
            	<INPUT size="33" type="text" maxlength="30" name="txtStage" value="<%=info.stage%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.stageUpdate.Standardstage")%>*</TD>
            <TD class="CellBGRnews"><SELECT name="cmbStandardStage" class="COMBO">                
                <OPTION value="0"></OPTION>
                <%for (int i=1;i<7;i++){%>
                <OPTION value="<%=i%>"<%if(standardStage==i){%>selected<%}%>><%=languageChoose.getMessage(StageInfo.getStageName(i))%></OPTION>
                <%}%>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageUpdate.Plannedenddate")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtBEndD" value="<%=bEndD%>">
            	<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showBEndD()'>            
            	(DD-MMM-YY) 
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageUpdate.Replannedenddate")%></TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtPEndD" value="<%=pEndD%>">
	            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPEndD()'>            
    	        (DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageUpdate.Actualenddate")%></TD>
            <%  if ((vtID==vt.size()-1) || !prevClosed)	{
            %>	<TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtAEndD" value="<%=aEndD%>" readonly>         	
            	(DD-MMM-YY) <BR><font color="red">(*)<%=(vtID==vt.size()-1)? languageChoose.getMessage("fi.jsp.stageUpdate.Projectactualenddate") : languageChoose.getMessage("fi.jsp.stageUpdate.Mustclosepreviousstagebeforethisone")%></font></TD>
            <%}else{%>
				<TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtAEndD" value="<%=aEndD%>">
		            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showAEndD()'>				
					(DD-MMM-YY) </TD>
            <%}%>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.stageUpdate.Numberofinterations")%> </TD>
            <TD class="CellBGRnews"><%=info.iterationCnt%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageUpdate.Description")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtDescription"><%=description%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.stageUpdate.Milestone")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtMilestone"><%=milestone%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.stageUpdate.Isontime")%> </TD>
            <TD class="CellBGRnews"><%=languageChoose.getMessage(info.isOntime)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.stageUpdate.Scheduledeviation")%> </TD>
            <TD class="CellBGRnews"><%=info.deviation%></TD>
        </TR>
    </TBODY> 
</TABLE>
<font color="red"> <%=languageChoose.getMessage("fi.jsp.stageUpdate.Pleasemapyourstagewithstandard")%> </font>
<BR><BR>
<INPUT  type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT  type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">
<p>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.stageUpdate.OK")%>" class="BUTTON" onclick="update();">
<INPUT type="button" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.stageUpdate.Cancel")%>" class="BUTTON" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_STAGE_GET_LIST:Constants.WO_DELIVERABLE_GET_LIST%>);">
</p>
<%if(source.equals("1")){%>
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
	
  function update()
  {
  
  	  if(!(mandatoryFld(frm.txtStage, "<%=languageChoose.getMessage("fi.jsp.stageUpdate.Stagename")%>")
  		&& mandatorySelect(frm.cmbStandardStage, "<%=languageChoose.getMessage("fi.jsp.stageUpdate.Standardstage")%>")
		&& maxLength(frm.txtStage,"<%=languageChoose.getMessage("fi.jsp.stageUpdate.Stagename")%>",30)
		&& mandatoryDateFld(frm.txtBEndD, "<%=languageChoose.getMessage("fi.jsp.stageUpdate.Plannedenddate")%>")
		&& mandatoryFld(frm.txtDescription, "<%=languageChoose.getMessage("fi.jsp.stageUpdate.Description")%>")
		&& maxLength(frm.txtDescription, "<%=languageChoose.getMessage("fi.jsp.stageUpdate.Description")%>",400)
		&& mandatoryFld(frm.txtMilestone, "<%=languageChoose.getMessage("fi.jsp.stageUpdate.Milestone")%>")
		&& maxLength(frm.txtMilestone, "<%=languageChoose.getMessage("fi.jsp.stageUpdate.Milestone")%>",200)))
			return;

  	  var stage=frm.txtStage.value.toUpperCase();
  	  for(i=0;i<miles.elements.length;i++){
  	  	if(stage==miles.elements[i].value){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.stageUpdate.ThisStageNameExists")%>");
  	  		frm.txtStage.focus();
  	  		return;
  	  	}
  	  }
  	  if(compareDate(frm.txtBEndD.value,frm.txtPrjStartD.value)==1){
			 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.stageUpdate.Planned__end__date__must__be__after__planned__start__date__of__project__~PARAM1_PJSDATE~")%>',frm.txtPrjStartD.value)));
  		 	 frm.txtBEndD.focus();
  		 	 return;
  	  }
  	  if (frm.txtPEndD.value.length >0){ 
	  	  if(compareDate(frm.txtPEndD.value,frm.txtPrjEndD.value)==-1){
	  	  		 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.stageUpdate.Replanned__end__date__must__be__before__replanned__end__date__of__project__~PARAM1_PJEDATE~")%>',frm.txtPrjEndD.value)));
	  		 	 frm.txtPEndD.focus();
	  		 	 return;
	  	  }
  	  }
  	  else{
		if(compareDate(frm.txtBEndD.value,frm.txtPrjEndD.value)==-1){
			window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.stageUpdate.Planned__end__date__must__be__before__replanned__end__date__of__project__~PARAM1_PJEDATE~")%>',frm.txtPrjEndD.value)));
			frm.txtBEndD.focus();
			return;
		}
  	  }	
  	  
  	  
  	  if(frm.txtPEndD.value!=""){
  	  		if(!(isDate(frm.txtPEndD.value))){  	  
  		  		window.alert("<%=languageChoose.getMessage("fi.jsp.stageUpdate.Invaliddateformat")%>");
  		  		frm.txtPEndD.focus();
  		  		return;
  		  	}  	
  		    if(compareDate(frm.txtPEndD.value,frm.txtPrjStartD.value)==1){
				 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.stageUpdate.Replanned__end__date__must__be__after__planned__start__date__of__project__~PARAM1_PJSDATE~")%>',frm.txtPrjStartD.value)));
  		 		 frm.txtPEndD.focus();
  		 		 return;
  			}
  	  
  	  		if(compareDate(frm.txtPEndD.value,frm.txtPrjEndD.value)==-1){
				 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.stageUpdate.Replanned__end__date__must__be__before__replanned__end__date__of__project__~PARAM1_PJEDATE~")%>',frm.txtPrjEndD.value )));
  		 		 frm.txtPEndD.focus();
  		 		 return;
  	 		 }		  	
  	  }
  	  
  	  if(frm.txtAEndD.value!=""){
	  	  if(!beforeTodayFld(frm.txtAEndD,"<%=languageChoose.getMessage("fi.jsp.stageUpdate.Actualenddate")%>"))
	  		  	return;
<%if (prjInfo.getActualFinishDate() !=null ) {%> 	
  		  	if(compareDate(frm.txtAEndD.value,"<%=CommonTools.dateFormat(prjInfo.getActualFinishDate())%>")== - 1){
  	  			 window.alert("<%=languageChoose.getMessage("fi.jsp.stageUpdate.ActualEndDateOfTheStageMustBeBeforeActualEndDateOfProject")%> (<%=CommonTools.dateFormat(prjInfo.getActualFinishDate())%>)");
  		 		 frm.txtAEndD.focus();
  		 		 return;
  			}
<%}if (prjInfo.getStartDate() !=null ){%>
  			if(compareDate(frm.txtAEndD.value,"<%=CommonTools.dateFormat(prjInfo.getStartDate())%>")== 1){
  	  			 window.alert("<%=languageChoose.getMessage("fi.jsp.stageUpdate.ActualEndDateOfTheStageMustBeAfterActualStartDateOfProject")%> (<%=CommonTools.dateFormat(prjInfo.getStartDate())%>)");
  		 		 frm.txtAEndD.focus();
  		 		 return;
  			}
<%}if (previousEndDate!=null){%>
  			if(compareDate(frm.txtAEndD.value,"<%=previousEndDate%>")>= 0){
				 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.stageUpdate.Actual__end__date__of__the__stage__must__be__after__previous__stage__end__date__~PARAM1_PREVIOUSENDDATE~")%>',"<%=previousEndDate%>")));
  		 		 frm.txtAEndD.focus();
  		 		 return;
  			}

<%}if (nextEndDate!=null){%>
  			if(trim(frm.txtAEndD.value)==''){
  	  			 window.alert("<%=languageChoose.getMessage("fi.jsp.stageUpdate.ActualEndDateOfTheStageMustBeSetBecauseActualEndDateOfNextStageIsAlreadySet")%> ");
  		 		 frm.txtAEndD.focus();
  		 		 return;
  			}
  			if(compareDate(frm.txtAEndD.value,"<%=nextEndDate%>")<= 0){
				 window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.stageUpdate.Actual__end__date__of__the__stage__must__be__before__next__stage__end__date__~PARAM1_NEXTENDDATE~")%>',"<%=nextEndDate%>")));
  		 		 frm.txtAEndD.focus();
  		 		 return;
  			}
  	  }
  	  else  if(trim(frm.txtAEndD.value)==''){
  	  			 window.alert("<%=languageChoose.getMessage("fi.jsp.stageUpdate.ActualEndDateOfTheStageMustBeSetBecauseActualEndDateOfNextStageIsAlreadySet")%>");
  		 		 frm.txtAEndD.focus();
  		 		 return;
  			} 
<%}else{%>
		}
<%}%>	
 	  
  	  frm.submit();	    	    	
  }
 </SCRIPT>
</BODY>
</HTML>
