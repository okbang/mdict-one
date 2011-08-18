<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<TITLE>otherActivityAdd.jsp</TITLE>
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
		title=languageChoose.getMessage("fi.jsp.otherActivityAdd.ScheduleOtherqualityactivities");
	else //called from project plan
		title=languageChoose.getMessage("fi.jsp.otherActivityAdd.ProjectplanOtherqualityactivities");

	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	Vector userList=(Vector)session.getAttribute("userList");
%>

<SCRIPT language="javascript">

<%	Vector vt=(Vector)session.getAttribute("devVector");%>	
	arrItemDeveloper = new Array(<%=vt.size()%>);
	arrItemDeveloperID = new Array(<%=vt.size()%>);
<%	for(int i=0;i<vt.size();i++){
		UserInfo devInfo=(UserInfo)vt.get(i);
	%>	arrItemDeveloper[<%=i%>] = "<%=devInfo.account%>";
		arrItemDeveloperID[<%=i%>] = "<%=devInfo.developerID%>";
<%}%>
	
	function selectAllDevelopers(){
	    var myElement;
	    
	    for (var i = frm.cmbConductor.options.length; i >= 0; i--){
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

<BODY class="BD" onLoad="loadPrjMenu();frm.txtActivity.focus();dispMetric()">
<P class="TITLE"><%=title%></p>
<FORM action="Fms1Servlet?reqType=<%=Constants.ADDNEW_OTHER_ACTIVITY%>#otheract" method="POST" name="frm">
<TABLE class="Table" width="640" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Addnewqualityactivity")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Activity")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtActivity"></TEXTAREA></TD>
        </TR>
         <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Type")%>*</TD>
            <TD class="CellBGRnews">
	            <SELECT name="txtType" onchange="dispMetric()">
   				<OPTION value="-1"></OPTION>
		<%
			int size=QCActivityInfo.qcActivitiesList.size();
			QCActivityInfo info;
			for (int i=0;i<size;i++){
				info=(QCActivityInfo)QCActivityInfo.qcActivitiesList.elementAt(i);
				if (info.id!=QCActivityInfo.QUALITY_GATE_INSPECTION){
					%><OPTION value=<%=info.id%>><%=languageChoose.getMessage(info.name)%></OPTION>
					<%}
				}%>
	            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Plannedstartdate")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtPStartD">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPStartD()'>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Plannedenddate")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtPEndD">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPEndD()'>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityUpdate.Actualenddate")%></TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtAEndD">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showAEndD()'>
            <A name="metricLabel">&nbsp;&nbsp;&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.otherActivityAdd.SuccessfulTCcoverage")%> &nbsp;&nbsp;&nbsp;</A><INPUT name="metricVal"  size="9" maxlength="9"></TD>
        </TR>
        <TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Status")%>*</TD>
			<TD class="CellBGRnews">
				<SELECT name="status">
					<%for(int i=1;i<TaskInfo.statusSQA_Updated.length;i++){
						%><OPTION value="<%=i%>" <%=((i == TaskInfo.STATUS_PENDING)?" selected":"")%>><%=languageChoose.getMessage(TaskInfo.statusSQA_Updated[i])%></OPTION>
					<%}%>
				</SELECT>
			</TD>
		</TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Conductor")%>*</TD>
            <TD class="CellBGRnews"><SELECT name="cmbConductor" class="COMBO">
                <%for(int i=0;i<userList.size();i++){
		            	AssignmentInfo assInfo = (AssignmentInfo) userList.elementAt(i);
               		%><OPTION value="<%=assInfo.devID%>"><%=assInfo.account%></OPTION>
                <%}%>
            </SELECT> <A href="javascript:selectAllDevelopers()">  <%=languageChoose.getMessage("fi.jsp.otherActivityAdd.more")%>  </A> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Note")%></TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtNote"></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<p>
<INPUT  type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT  type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.OK")%>" class="BUTTON" onclick="doAdd();"> <INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Cancel")%>" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_OTHER_QUALITY_GET_LIST:Constants.GET_QUALITY_OBJECTIVE_LIST%>);"></P>
</FORM>
<SCRIPT language="javascript">
	function showPStartD(){
		if(frm.txtPStartD.value == null || frm.txtPStartD.value ==""){
			frm.txtPStartD.value = "01-01-08";
		}
		showCalendar(frm.txtPStartD, frm.txtPStartD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showPEndD(){
		if(frm.txtPEndD.value == null || frm.txtPEndD.value ==""){
			frm.txtPEndD.value = "01-01-08";
		}
		showCalendar(frm.txtPEndD, frm.txtPEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showAEndD(){
		if(frm.txtAEndD.value == null || frm.txtAEndD.value ==""){
			frm.txtAEndD.value = "01-01-08";
		}
		showCalendar(frm.txtAEndD, frm.txtAEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
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
function doAdd() {
  	  if(trim(frm.txtActivity.value)==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Thisfieldismandatory")%>"); 
  	  	frm.txtActivity.focus();  	
  	  	return;  		
  	  		
  	  }  	  
  	  if(frm.txtActivity.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Lengthofthisfieldmustbelessthan200")%>");
  			frm.txtActivity.focus();
  			return;
  	  }	 
  	   if(frm.txtType.value < 0){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Thisfieldismandatory")%>"); 
  	  	frm.txtType.focus();  	
  	  	return;  		
  	  		
  	  }  
  	  if(trim(frm.txtPStartD.value)==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Thisfieldismandatory")%>"); 
  	  	frm.txtPStartD.focus();  	
  	  	return;  		
  	  		
  	  }
  	   
  	  if(!(isDate(frm.txtPStartD.value))){  	  
  		  window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Invaliddateformat")%>");
  		  frm.txtPStartD.focus();
  		  return;
  	  }
  	  
  	  if(compareDate(frm.txtPStartD.value,frm.txtPrjStartD.value)==1){
  	  		 window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.otherActivityAdd.Planned__start__date__must__be__between__planned__start__date__and__replanned__end__date__of__project~PARAM1_DATE~__to__~PARAM2_DATE~")%>",frm.txtPrjStartD.value,frm.txtPrjEndD.value)));
  		 	 frm.txtPStartD.focus();
  		 	 return;
  	  }
  	  if(compareDate(frm.txtPStartD.value,frm.txtPrjEndD.value)==-1){
  	  		 window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.otherActivityAdd.Planned__start__date__must__be__between__planned__start__date__and__replanned__end__date__of__project~PARAM1_DATE~__to__~PARAM2_DATE~")%>",frm.txtPrjStartD.value,frm.txtPrjEndD.value)));
  		 	 frm.txtPStartD.focus();
  		 	 return;
  	  }
  	  
  	  if(trim(frm.txtPEndD.value)==""){
  	  	window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Thisfieldismandatory")%>"); 
  	  	frm.txtPEndD.focus();  	
  	  	return;  		
  	  		
  	  }
  	  
  	 if(!(isDate(frm.txtPEndD.value))){  	  
  		  window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Invaliddateformat")%>");
  		  frm.txtPEndD.focus();
  		  return;
  	  }
  	  if(compareDate(frm.txtPEndD.value,frm.txtPStartD.value)==1){
  	  		 window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.otherActivityAdd.Planned__end__date__must__be__between__planned__start__date__of__activity__and__replanned__end__date__of__project~PARAM1_DATE~__to__~PARAM2_DATE~")%>",frm.txtPrjStartD.value,frm.txtPrjEndD.value)));
  		 	 frm.txtPEndD.focus();
  		 	 return;
  	  }
  	  if(compareDate(frm.txtPEndD.value,frm.txtPrjEndD.value)==-1){
  	  		 window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.otherActivityAdd.Planned__end__date__must__be__between__planned__start__date__of__activity__and__replanned__end__date__of__project~PARAM1_DATE~__to__~PARAM2_DATE~")%>",frm.txtPrjStartD.value,frm.txtPrjEndD.value)));
  		 	 frm.txtPEndD.focus();
  		 	 return;
  	  }
  	  
  	  if(trim(frm.txtAEndD.value)!=""){
	  	  if(!(isDate(frm.txtAEndD.value))){  	  
	  		  window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Invaliddateformat")%>");
	  		  frm.txtAEndD.focus();
	  		  return;
	  	  } 
	  	 var j=frm.status.value;
		if (j==<%=(int)TaskInfo.STATUS_PENDING%>){
		 	window.alert("<%= languageChoose.getMessage("fi.jsp.otherActivityAdd.Whenactualdateisfilled")%>");
  			frm.status.focus();  		
  			return;
		}
  	  	if (frm.txtType.value==<%=QCActivityInfo.FINAL_INSPECTION%>){	
			if (trim(frm.metricVal.value)==""){
				window.alert("<%= languageChoose.getMessage("fi.jsp.otherActivityAdd.Themetricvalueismandatorywhenactualdateisset")%>");
				frm.metricVal.focus();
				return;
			}
			if(!percentageFld(frm.metricVal,"Metric"))
					return;
		} 
  	}	  
  	 else{
  	 	var j=frm.status.value;
		if (j==<%=TaskInfo.STATUS_PASS%>){
		 	window.alert("<%= languageChoose.getMessage("fi.jsp.otherActivityAdd.Actualdateismandatorywhenstatusispassedornotpassed")%>");
  			frm.txtAEndD.focus();  		
  			return;
		}
  	 }
  	  
  	  if(frm.txtNote.value.length > 200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.otherActivityAdd.Lengthofthisfieldmustbelessthan200")%>");
  			frm.txtNote.focus();
  			return;
  	  }	 	
  	   	  
  	  frm.submit();	    	    	
  }
 </SCRIPT> 
</BODY>
</HTML>
