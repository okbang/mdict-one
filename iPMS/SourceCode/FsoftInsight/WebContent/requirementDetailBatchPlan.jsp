<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*"%>
<%
	response.setHeader("Content-Type", "text/html; charset=UTF-8");
%>
<%//the xxx2 variable are name like this to avoid naming conflics when inculding in milestone JSP
	
	LanguageChoose RDBL_languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	boolean isMilestone=Integer.toString(Constants.MILESTONE_GET_PAGE).equals(request.getParameter("reqType"));
	int right2=Security.securiPage((isMilestone)?"Project reports":"Requirements",request,response);
	boolean isReplan="1".equals(session.getAttribute("replan"));
	Vector stageList2=(Vector)session.getAttribute("stageList");
	Vector planRCRList=(Vector)session.getAttribute("planRCRList");
	boolean isUpdate=false;
	boolean oneIsClosed=false;
	StageInfo milestoneInfo=null;
	if (isMilestone){
		isUpdate=false;
		oneIsClosed=true;
		milestoneInfo=(StageInfo)session.getAttribute("stageInfo");
	}
	else{
		isUpdate=("1".equals(request.getParameter("update")) && !"1".equals(request.getAttribute("updated")));
		oneIsClosed=isReplan?true:"1".equals((String)request.getAttribute("oneClosed"));
	}
	StageInfo stinf;
	StageInfo currentStinf=null;
	int currentStage=0;

	for (int i=0;i<stageList2.size();i++){
		currentStinf=(StageInfo)stageList2.elementAt(i);
		if ((isMilestone && currentStinf.milestoneID== milestoneInfo.milestoneID) 
		|| (currentStinf.aEndD==null && currentStage==0 ||i==stageList2.size()-1)){
			currentStage=i;
			break;
		}
	}
String title=isReplan? RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.ReplanRCRbystageprocess"):RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.PlanRCRbystageprocess");
if (!isMilestone){
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>requirementDetailBatchPlan.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<BODY class="BD" onload="loadPrjMenu();<%=isUpdate ?"recalc()":""%>">

<P class="TITLE"><%=title%></P>
 <BR>
 <P class="HDR"><%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Stage")%><%=currentStinf.stage%></P>
<%}%>
<BR>
<form name='frm' action ='Fms1Servlet?reqType=<%=Constants.REQUIREMENT_PLAN_RCR_BATCH_SAVE%>' method='POST' >
<TABLE class="Table" cellspacing="1" >
<%if (isMilestone){%>
  <CAPTION class="TableCaption"><%=title%></CAPTION>
<%}%>

<TR class="ColumnLabel">
	<TD rowspan =2>#</TD>
	<TD rowspan =2> <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Engineeringprocess")%> </TD>
	<%=isUpdate ?"<TD rowspan =2>" + RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Totalplan") + "</TD>":""%>
	<%for (int i=0;i<stageList2.size();i++){
		stinf=(StageInfo)stageList2.elementAt(i);
	%><TD colspan=2><%=stinf.stage%></TD>
	<%}%>
</TR>
<TR class="ColumnLabel">
	<%for (int i=0;i<stageList2.size();i++){
		if (isReplan){
			stinf=(StageInfo)stageList2.elementAt(i);
		%>
		<TD width='40'><%=(i >= currentStage)?RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Forecast"):RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Actual")%></TD>
		<TD width='40'><%=i==0 ?RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Plan"): RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Replan")%></TD>
		<%}else{%>
		<TD width='40'> <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Norm")%> </TD>
		<TD width='40'> <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Plan")%> </TD>
		<%}
	}%>
</TR>
<%
boolean myflag=false;
String color=null;

for (int i=0;i<ProcessInfo.RCRProcesses.length;i++){
	color=myflag?"CellBGRnews":"CellBGR3";
%>
<TR class='<%=color%>'>
	<TD><%=i+1%></TD>
	<TD><%=RDBL_languageChoose.getMessage(ProcessInfo.getProcessName(ProcessInfo.RCRProcesses[i]))%></TD>
	<%=isUpdate ?"<TD><A name='totalrow"+i+"'></A></TD>":""%>
<%	
	PlanRCRInfo inf;
	int k=0;
	for (int j=0;j<stageList2.size();j++){
		stinf=(StageInfo)stageList2.elementAt(j);
		for (k=0;k<planRCRList.size();k++){
			inf=(PlanRCRInfo)planRCRList.elementAt(k);
			if (inf.processid==ProcessInfo.RCRProcesses[i]&& inf.stage==stinf.milestoneID){
				if (isReplan){%>
					<TD><div id='rox<%=i%>stage<%=j%>'><%=Color.setColor(inf.color,CommonTools.formatDouble((j >= currentStage)?inf.forecasted:inf.actual))%></div></TD>
				<%}else{%>
					<TD><div id='rox<%=i%>stage<%=j%>'><%=CommonTools.formatDouble(inf.norm)%></div></TD>
				<%}
				if (isUpdate && stinf.aEndD==null){%>
					<TD class="CellBGRnews"><input id ='row<%=i%>stage<%=j%>' name='plan<%=k%>' value='<%=CommonTools.updateDouble(inf.plannedValue)%>' onchange="recalc2();" maxlength = '6' size='4'></TD>
				<%}else{%>
					<TD class="CellBGRnews"><div id='row<%=i%>stage<%=j%>'><%=CommonTools.formatDouble(inf.plannedValue)%></div></TD>
				<%}
			break;
			}
		}
	}%>
</TR>
<%}%>
</TABLE>
<P>

<%if (!isMilestone && right2 == 3 ){
	if (!isUpdate){
//		if((isReplan || !oneIsClosed) && currentStinf.aEndD==null){%>
<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Update")%> " onclick="doIt('<%=Constants.GET_PAGE+"&page=requirementDetailBatchPlan.jsp&update=1"%>')">
		<%//}%>
<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Back")%> " onclick="doIt(<%=Constants.REQUIREMENT_PLAN%>)">
	<%}else{%>
<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Save")%> " onclick="valid()">
<INPUT type="button" class="BUTTONWIDTH" value="<%=isReplan? RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.UseForecast"): RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Usenorm")%>" onclick="reuse()">
<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Cancel")%> " onclick="doIt('<%=Constants.GET_PAGE+"&page=requirementDetailBatchPlan.jsp"%>')">

	<%}
}%>

</FORM>




<SCRIPT language="javascript">

//crazy IE 5 bug, recalc cant be called directly
function recalc2(){
recalc();
}
function recalc(option){

	var indexMetric;
	var fieldTotal;
	var fieldPercent;
	var max = <%=ProcessInfo.RCRProcesses.length%>;
	for (var i=0;i < max;i++){
		fieldTotal=document.all['totalrow'+i];
		fieldTotal.innerText=0;
		var max1 = <%=stageList2.size()%>;
		for (var j=0;j < max;j++){
			if ((j>=<%=currentStage%>||<%=!isReplan%>)&&<%=isUpdate%>)
				fieldPercent=document.all['row'+i+'stage'+j];
			else
				fieldPercent=document.all['rox'+i+'stage'+j];
			if (fieldPercent){
				if(fieldPercent.value!=null){
					if (option=="valid" ){
						if (!mandatoryFld(fieldPercent,"<%= RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.PlannedRCR")%>") ||!percentageFld(fieldPercent,"<%= RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.PlannedRCR")%>"))
							return false;
					}
					if(trim(fieldPercent.value)!='')
						document.all['totalrow'+i].innerText =parseFloat(document.all['totalrow'+i].innerText)+parseFloat(fieldPercent.value);
				}
				else{
					if(trim(fieldPercent.innerText )!='')
						document.all['totalrow'+i].innerText =parseFloat(document.all['totalrow'+i].innerText)+parseFloat(fieldPercent.innerText);
				}
			}
		}
		fieldTotal.innerText=fixstr(fieldTotal.innerText);
		if (fieldTotal.innerText != 100){
			if (option=="valid"){
				alert(getParamText(new Array("<%= RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.The__total__of__distribution__values__must__be__equal__to__100__currently__~PARAM1_TOTAL_VALUE~")%>", fieldTotal.innerText)));
				document.all['row'+i+'stage<%=currentStage%>'].focus();
				return false;
			}
		}
	}
	return true;
}
function reuse(){
 	var max = <%=ProcessInfo.RCRProcesses.length%>;
	for (var i=0;i < max;i++){
		var max1 = <%=stageList2.size()%>;
		for (var j=0;j < max1;j++){
			if (document.all['row'+i+'stage'+j])
				document.all['row'+i+'stage'+j].value=document.all['rox'+i+'stage'+j].innerText;

		}

	}
	recalc();

}
function valid(){
	if (recalc('valid')){
		frm.submit();
	}
}



</SCRIPT>
<%if (!isMilestone ){%>
</BODY>
</HTML>
<%}%>
