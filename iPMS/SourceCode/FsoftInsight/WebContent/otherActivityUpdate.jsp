<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*, java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>otherActivityUpdate.jsp</TITLE>

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
	int caller=Integer.parseInt(session.getAttribute("caller").toString());
	String title;
	
	if(caller==Constants.SCHEDULE_CALLER)//called from shedule
		title=languageChoose.getMessage("fi.jsp.otherActivityUpdate.ScheduleOtherqualityactivities");
	else  //called from project plan
		title=languageChoose.getMessage("fi.jsp.otherActivityUpdate.ProjectplanOtherqualityactivities");

	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	Vector userList=(Vector)session.getAttribute("userList");
	
	Vector vtOtherAct=(Vector)session.getAttribute("otherActVector");
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	OtherActInfo oaInfo=(OtherActInfo)vtOtherAct.get(vtID);	
	String riskActivity = oaInfo.activity;
	String activity=(oaInfo.activity!=null)?oaInfo.activity:"";
    String pEndD=(oaInfo.pEndD!=null)?CommonTools.dateFormat(oaInfo.pEndD):"";
    String pStartD=(oaInfo.pStartD!=null)?CommonTools.dateFormat(oaInfo.pStartD):"";
    String aEndD=(oaInfo.aEndD!=null)?CommonTools.dateFormat(oaInfo.aEndD):"";
    String note=(oaInfo.note!=null)?oaInfo.note:"";
    Vector vt=(Vector)session.getAttribute("devVector");
    //in case it is a :QUALITY_GATE_INSPECTION
    String disab,focus;
       String previousEndDate =null;
	String nextEndDate =null;
	OtherActInfo oaInfoTemp;
    boolean isQG=(oaInfo.qcActivity==QCActivityInfo.QUALITY_GATE_INSPECTION || oaInfo.risk_type == 1 || oaInfo.risk_type == 2);
    if (isQG){
   		disab="disabled";
   		focus="frm.txtAEndD.focus()";
   		if (vtID>0){// there is a previous stage (QGates come first in the vector)
			oaInfoTemp=(OtherActInfo)vtOtherAct.get(vtID-1);
			if(oaInfoTemp.aEndD!=null)
				previousEndDate=CommonTools.dateFormat(oaInfoTemp.aEndD);
		}
		if (vtID<(vtOtherAct.size()-1)){// there is a next stage or another QA after
			oaInfoTemp=(OtherActInfo)vtOtherAct.get(vtID+1);
			if(oaInfoTemp.aEndD!=null &&(oaInfoTemp.qcActivity==QCActivityInfo.QUALITY_GATE_INSPECTION))
				nextEndDate=CommonTools.dateFormat(oaInfoTemp.aEndD);
		}
    }
    else{
    	disab="";
    	focus="frm.txtActivity.focus()";
    }


    
%>
<SCRIPT language="javascript">
	arrItemDeveloper = new Array(<%=vt.size()%>);
	arrItemDeveloperID = new Array(<%=vt.size()%>);
<%
for(int i=0;i<vt.size();i++){
		UserInfo devInfo=(UserInfo)vt.get(i);
%>		arrItemDeveloper[<%=i%>] = "<%=devInfo.account%>";
		arrItemDeveloperID[<%=i%>] = "<%=devInfo.developerID%>";
<%}%>
	
	function selectAllDevelopers(){
	    var myElement;
	    
	    for (var i = frm.cmbConductor.options.length; i >=0; i--){
	        frm.cmbConductor.options[i] = null;
	    }
        for (var i = 0; i < arrItemDeveloper.length; i++){
                myElement = document.createElement("option");
	            myElement.value =arrItemDeveloperID[i];
	            myElement.text = arrItemDeveloper[i];
	            frm.cmbConductor.add(myElement);
	    }
	}
</SCRIPT> 

<BODY class="BD" onLoad="loadPrjMenu();<%=focus%>;dispMetric()">
<P class="TITLE"><%=title%></P>
<FORM action="Fms1Servlet?reqType=<%=Constants.UPDATE_OTHER_ACTIVITY%>#otheract"
	method="POST" name="frm">
<TABLE class="Table" width="95%" cellspacing="1">
	<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Updatequalityactivity")%></CAPTION>
	<TBODY>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Activity")%>*</TD>
			<TD class="CellBGRnews"><INPUT size="20" type="hidden"
				name="vtID" value="<%=vtID%>">
				<TEXTAREA rows="4" cols="50" name="txtActivity" <%=disab%>><%=activity%></TEXTAREA></TD>
		</TR>
		<TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Type")%>*</TD>
            <TD class="CellBGRnews">
	            <SELECT name="txtType" onchange="dispMetric()" <%=disab%>>
	            <OPTION value="-1"></OPTION>
		<%
		int size=QCActivityInfo.qcActivitiesList.size();
			QCActivityInfo info;
			for (int i=0;i<size;i++){
				info=(QCActivityInfo)QCActivityInfo.qcActivitiesList.elementAt(i);
				if(isQG || info.id!=QCActivityInfo.QUALITY_GATE_INSPECTION)
		%>	<OPTION value="<%=info.id %>" <%=((info.id==oaInfo.qcActivity)?" selected ":"")%>> <%=languageChoose.getMessage(info.name)%></OPTION>
		<%}%>
	            </SELECT>
            </TD>
        </TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Plannedstartdate")%>*</TD>
			<TD class="CellBGRnews" nowrap="nowrap"><INPUT size="9" type="text" maxlength="9" name="txtPStartD" value="<%=pStartD%>"> 
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPStartD()'>            				
				(DD-MMM-YY) </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Plannedenddate")%>*</TD>
			<TD class="CellBGRnews" nowrap="nowrap"><INPUT size="9" type="text" maxlength="9" name="txtPEndD" value="<%=pEndD%>"> 
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPEndD()'>            				
				(DD-MMM-YY) </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Actualenddate")%></TD>
			<TD class="CellBGRnews" nowrap="nowrap"><INPUT size="9" type="text" maxlength="9" name="txtAEndD" value="<%=aEndD%>"> 
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showAEndD()'>            			
				(DD-MMM-YY) <A name="metricLabel">&nbsp;&nbsp;&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.SuccessfulTCcoverage")%> &nbsp;&nbsp;&nbsp;</A><INPUT name="metricVal" size="9" maxlength="9" value="<%=CommonTools.updateDouble(oaInfo.metric)%>"></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Status")%>*</TD>
			<TD class="CellBGRnews">
				<SELECT name="status">
					<%for(int i=0;i<TaskInfo.statusSQA_Updated.length;i++){
						%><OPTION value="<%=i %>" <%=((i==oaInfo.status)?" selected":"")%>> <%=languageChoose.getMessage(TaskInfo.statusSQA_Updated[i])%></OPTION>
					<%}%>
				</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Conductor")%>*</TD>
			
			<TD class="CellBGRnews" ><SELECT name="cmbConductor" class="COMBO">
				<%if (oaInfo.conductor>0){%>
                <OPTION value="<%=oaInfo.conductor%>"selected><%=oaInfo.conductorName%></OPTION>
                <%}
                for(int i=0;i<userList.size();i++){
		            	AssignmentInfo assInfo = (AssignmentInfo) userList.elementAt(i);
						if(oaInfo.conductor != assInfo.devID) {
			                %><OPTION value="<%=assInfo.devID%>"><%=assInfo.account%></OPTION>
			            <%}
                }%>
                
            </SELECT><A href="javascript:selectAllDevelopers()">  <%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.more")%>  </A></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Note")%></TD>
			<TD class="CellBGRnews" <%=disab%>><TEXTAREA rows="4" cols="50" name="txtNote"><%=note%></TEXTAREA></TD>
		</TR>
	</TBODY>
</TABLE>
<INPUT type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">
<INPUT type="hidden" name="activityRisk" value="<%=riskActivity%>">

<p><INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.OK")%>" class="BUTTON" onclick="doUpdate();">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Cancel")%>" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_OTHER_QUALITY_GET_LIST:Constants.GET_QUALITY_OBJECTIVE_LIST%>);"></p>
</FORM>
<SCRIPT language="javascript">
function dispMetric(){
	if (frm.txtType.value==<%=QCActivityInfo.FINAL_INSPECTION%>){
		showObj("metricLabel");
		showObj("metricVal");
	}
	else{
		hideObj("metricLabel");
		hideObj("metricVal");
	}
}
function doUpdate()
  {
  		
	var isRisk = '<%=isQG%>';
  	
  	  if(trim(frm.txtActivity.value)==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Thisfieldismandatory")%>"); 
  	  	if(!frm.txtActivity.disabled)
	  	  	frm.txtActivity.focus();  	
  	  	return;  		
  	  		
  	  }  	  
  	  if(frm.txtActivity.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Lengthofthisfieldmustbelessthan200")%>");
  			if(!frm.txtActivity.disabled)
	  			frm.txtActivity.focus();
  			return;
  	  }	 
  	  if(!isRisk && frm.txtType.value < 0){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Thisfieldismandatory")%>"); 
  	  	frm.txtType.focus();  	
  	  	return;  		
  	  }  
  	  
  	  if(trim(frm.txtPStartD.value)==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Thisfieldismandatory")%>"); 
  	  	if(!frm.txtPStartD.disabled)
	  	  	frm.txtPStartD.focus();  	
  	  	return;  		
  	  		
  	  }
  	   
  	  if(!(isDate(frm.txtPStartD.value))){  	  
  		  window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Invaliddateformat")%>");
  		  if(!frm.txtPStartD.disabled)
	  		  frm.txtPStartD.focus();
  		  return;
  	  }
  	  
  	  if(compareDate(frm.txtPStartD.value,frm.txtPrjStartD.value)==1){
  	  		 window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.otherActivityUpdate.Planned__start__date__must__be__between__planned__start__date__and__replanned__end__date__of__project~PARAM1_DATE~__to__~PARAM2_DATE~")%>",frm.txtPrjStartD.value,frm.txtPrjEndD.value)));
  		 	 if(!frm.txtPStartD.disabled)
	  		 	 frm.txtPStartD.focus();
  		 	 return;
  	  }
  	  if(compareDate(frm.txtPStartD.value,frm.txtPrjEndD.value)==-1){
  	  		 window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.otherActivityUpdate.Planned__start__date__must__be__between__planned__start__date__and__replanned__end__date__of__project~PARAM1_DATE~__to__~PARAM2_DATE~")%>",frm.txtPrjStartD.value,frm.txtPrjEndD.value)));
  		 	 if(!frm.txtPStartD.disabled)
	  		 	 frm.txtPStartD.focus();
  		 	 return;
  	  }
  	  
  	  if(trim(frm.txtPEndD.value)==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Thisfieldismandatory")%>"); 
  	  	if(!frm.txtPEndD.disabled)
	  	  	frm.txtPEndD.focus();  	
  	  	return;  		
  	  		
  	  }  	  
  	  
  	  if(!(isDate(frm.txtPEndD.value))){  	  
  		  window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Invaliddateformat")%>");
  		  if(!frm.txtPEndD.disabled)
	  		  frm.txtPEndD.focus();
  		  return;
  	  }
  	  if(compareDate(frm.txtPEndD.value,frm.txtPStartD.value)==1){
  	  		 window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.otherActivityUpdate.Planned__end__date__must__be__between__planned__start__date__of__activity__and__replanned__end__date__of__project~PARAM1_DATE~__to__~PARAM2_DATE~")%>",frm.txtPStartD.value,frm.txtPrjEndD.value)));

  		 	 return;
  	  }
  	  if(compareDate(frm.txtPEndD.value,frm.txtPrjEndD.value)==-1){
  	  		 window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.otherActivityUpdate.Planned__end__date__must__be__between__planned__start__date__of__activity__and__replanned__end__date__of__project~PARAM1_DATE~__to__~PARAM2_DATE~")%>",frm.txtPStartD.value,frm.txtPrjEndD.value)));
  		 	 if(!frm.txtPEndD.disabled)
	  		 	 frm.txtPEndD.focus();
  		 	 return;
  	  }
  	 if(trim(frm.txtAEndD.value)!=""){
		if(!(isDate(frm.txtAEndD.value))){  	  
  		  window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Invaliddateformat")%>");
  		  if(!frm.txtAEndD.disabled)
	  		  frm.txtAEndD.focus();
  		  return;
		} 
		if (frm.txtType.value==<%=QCActivityInfo.FINAL_INSPECTION%>){	
			if (trim(frm.metricVal.value)==""){
				window.alert("<%= languageChoose.getMessage("fi.jsp.otherActivityUpdate.Themetricvalueismandatorywhenactualdateisset")%>");
				if(!frm.metricVal.disabled)
					frm.metricVal.focus();
				return;
			}
			if(!percentageFld(frm.metricVal,"<%= languageChoose.getMessage("fi.jsp.otherActivityUpdate.Metric")%>"))
				return;
		} 
	
<%if (isQG){%>
		 	if(!beforeTodayFld(frm.txtAEndD,"<%= languageChoose.getMessage("fi.jsp.otherActivityUpdate.Actualenddate")%>"))
				return;
	<%if (prjDateInfo.actualEndDate !=null ) {%> 	
			if(compareDate(frm.txtAEndD.value,"<%=CommonTools.dateFormat(prjDateInfo.actualEndDate)%>")== - 1){
				window.alert("<%= languageChoose.paramText(new String[]{"fi.jsp.otherActivityUpdate.Actual__before__actual__end__date__of__project__~PARAM1_DATE~", CommonTools.dateFormat(prjDateInfo.actualEndDate)})%>");
		 		if(!frm.txtAEndD.disabled)
			 		frm.txtAEndD.focus();
		 		return;
			}
	<%}	if (prjDateInfo.actualStartDate !=null ){%>
			if(compareDate(frm.txtAEndD.value,"<%=CommonTools.dateFormat(prjDateInfo.actualStartDate)%>")== 1){
				 window.alert("<%= languageChoose.paramText(new String[]{"fi.jsp.otherActivityUpdate.Actual__after__actual__start__date__of__project__~PARAM1_DATE~", CommonTools.dateFormat(prjDateInfo.actualStartDate)})%>");
		 		 if(!frm.txtAEndD.disabled)
			 		 frm.txtAEndD.focus();
		 		 return;
			}
	<%}	if (previousEndDate!=null){%>
			if(compareDate(frm.txtAEndD.value,"<%=previousEndDate%>")>= 0){
				 window.alert("<%= languageChoose.paramText(new String[]{"fi.jsp.otherActivityUpdate.Actual__after__previous__stage__end__date__~PARAM1_DATE~", previousEndDate})%>");
		 		 if(!frm.txtAEndD.disabled)
			 		 frm.txtAEndD.focus();
		 		 return;
			}
	<%}	if (nextEndDate!=null){%>
			if(compareDate(frm.txtAEndD.value,"<%=nextEndDate%>")<= 0){
				 window.alert("<%=languageChoose.paramText(new String[]{"fi.jsp.otherActivityUpdate.Actual__before__next__stage__end__date__~PARAM1_DATE~", nextEndDate})%>");
		 		 if(!frm.txtAEndD.disabled)
			 		 frm.txtAEndD.focus();
		 		 return;
			}
	<%}%>
	}
<%}else{%>
		var j=frm.status.value;
		if (j==<%=(int)TaskInfo.STATUS_PENDING%>){
		 	window.alert("<%= languageChoose.getMessage("fi.jsp.otherActivityUpdate.Whenactualdateisfilled")%>");
  			if(!frm.status.disabled)
	  			frm.status.focus();  		
  			return;
		}
	}
	else{
	  	//because QG status combo is frozen
	  	var j=frm.status.value;
		if (j==<%=(int)TaskInfo.STATUS_PASS%>){
		 	window.alert("<%= languageChoose.getMessage("fi.jsp.otherActivityUpdate.Actualdateismandatorywhenstatusispassedornotpassed")%>");
  			if(!frm.txtAEndD.disabled)
	  			frm.txtAEndD.focus();  		
  			return;
		}
	}
<%}%>
  	  if(frm.txtNote.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Lengthofthisfieldmustbelessthan200")%>");
  			if(!frm.txtNote.disabled)
	  			frm.txtNote.focus();
  			return;
  	  }	 	
  	   	  
  	  frm.submit();	    	    	
  }
  
	function showPStartD(){
		showCalendar(frm.txtPStartD, frm.txtPStartD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showPEndD(){
		showCalendar(frm.txtPEndD, frm.txtPEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showAEndD(){
		showCalendar(frm.txtAEndD, frm.txtAEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
 </SCRIPT>
</BODY>
</HTML>
