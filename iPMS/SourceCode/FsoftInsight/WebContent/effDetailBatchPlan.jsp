<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*"%>
<%
	response.setHeader("Content-Type", "text/html; charset=UTF-8");
%>

<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	int right2=Security.securiPage("Effort",request,response);
	boolean isReplan="1".equals(request.getParameter("isReplan"));
	boolean isUpdate=false;
	isUpdate="1".equals(request.getParameter("update"));
	ProjectInfo projectInfo = (ProjectInfo)session.getAttribute("projectInfo");
	
    Vector processEffortByStages=(Vector)session.getAttribute("processEffortByStages" );
    boolean oneIsClosed = false;
    ProcessEffortByStageInfo inf=null;
    int currentStage = 0;
    ProcessEffortByStageInfo currentStageInf = null;
    for (; currentStage < processEffortByStages.size(); currentStage++) {
        currentStageInf = (ProcessEffortByStageInfo)processEffortByStages.elementAt(currentStage);
        if (!currentStageInf.isOpen ) {
            oneIsClosed = true;
        }
        else
        	break;
    }
         
    double totalPlannedEffort = 0; 
    double averageAdjustment = 0; 

	double totalNormEffort = 0;
	double averageAdjustment2 = 0;

	if (!isReplan){
		for (int i=0;i<ProcessInfo.trackedProcessId.length;i++){
			for (int j=0; j < processEffortByStages.size(); j++) {
				inf = (ProcessEffortByStageInfo)processEffortByStages.elementAt(j);
				if (!Double.isNaN(inf.planned[i]))
					totalPlannedEffort = totalPlannedEffort + inf.planned[i];
					totalNormEffort = totalNormEffort + inf.norm[i];
			}
		}
	}
	averageAdjustment = (projectInfo.getBaseEffort() - totalPlannedEffort) / (ProcessInfo.trackedProcessId.length * processEffortByStages.size());
	averageAdjustment2 = (projectInfo.getBaseEffort() - totalNormEffort) / (ProcessInfo.trackedProcessId.length * processEffortByStages.size());
	
    String title;
    if (isReplan) {
        if (currentStage != 0) {
            title = languageChoose.getMessage("fi.jsp.effDetailBatchPlan.ReplanEffortByStageProcess");
        }
        else {
            title = languageChoose.getMessage("fi.jsp.effDetailBatchPlan.PlanEffortByStageProcess");
        }
    }
    else {
        title = languageChoose.getMessage("fi.jsp.effDetailBatchPlan.PlanEffortByStageProcess");
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>Plan batch edit</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<BODY class="BD" onload="loadPrjMenu();recalc()">

<P class="TITLE"><%=title%></P>
<p><%=languageChoose.getMessage("fi.jsp.EffDetailBatchPlan.Unlessspecifiedtheunitforeffortmetricsispersonday")%></p>
<%if (isReplan){%>
<p><font color="red"> <%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.RCRmustbereplannedinordertoget")%></font></p>
<%}%>
 <BR>
 <P class="HDR"> <%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Stage")%>  <%=currentStageInf.stage_name%></P>
 <P class="HDR"><%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Projectplannedeffort")%> <%=CommonTools.formatDouble(projectInfo.getBaseEffort())%></P>
  <P class="HDR"><%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Projectreplannedeffort")%><%=CommonTools.formatDouble(projectInfo.getPlanEffort())%></P>

<BR>
<form name='frm' action ='Fms1Servlet?reqType=<%=Constants.EFF_UPDATE_BATCH_PLAN%>' method='POST' >
<TABLE class="Table" cellspacing="1" >
<TR class="ColumnLabel">
	<TD rowspan =2>#</TD>
	<TD rowspan =2> <%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Process")%> </TD>
	<TD rowspan =2> <%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Totalplanned")%> </TD>
	<%for (int i=0; i < processEffortByStages.size(); i++) {
		inf = (ProcessEffortByStageInfo)processEffortByStages.elementAt(i);
	%><TD colspan=2><%=inf.stage_name%></TD>
	<%}%>
</TR>
<TR class="ColumnLabel">
	<%for (int i=0; i < processEffortByStages.size(); i++) {
		if (isReplan){
			inf = (ProcessEffortByStageInfo)processEffortByStages.elementAt(i);
		%>
        <TD width='40'>
        <%
            String colTitle = null;
            if (currentStage == 0) {
                colTitle = languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Norm");
            }
            else {
                colTitle = (i >= currentStage)?
                    languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Forecast"):
                    languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Actual");
            }
        %>
        <%=colTitle%>
        </TD>
        <TD width='40'>
        <%
            if (currentStage == 0) {
                colTitle = languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Plan");
            }
            else {
                colTitle = (i == 0)?
                    languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Plan"):
                    languageChoose.getMessage("fi.jsp.effDetailBatchPlan.RePlanned");
            }
        %>
        <%=colTitle%>
        </TD>
		<%}else{%>
		<TD width='40'> <%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Norm")%> </TD>
		<TD width='40'> <%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Plan")%> </TD>
		<%}
	}%>
</TR>
<%
boolean myflag=false;
String color=null;

int numStage = processEffortByStages.size();
double [] totalPlan = new double[numStage];
double [] totalForecast = new double[numStage];
double [] totalNorm = new double[numStage];

double updateValue = 0;

Arrays.fill(totalPlan,Double.NaN);
Arrays.fill(totalForecast,Double.NaN);
Arrays.fill(totalNorm,Double.NaN);

for (int i=0;i<ProcessInfo.trackedProcessId.length;i++){
	color=myflag?"CellBGRnews":"CellBGR3";
	
%>
<TR class='<%=color%>'>
	<TD><%=i+1%></TD>
	<TD><%=languageChoose.getMessage(ProcessInfo.getProcessName(ProcessInfo.trackedProcessId[i]))%></TD>
	<TD><A name='totalrow<%=i%>'></A></TD>
<%	
	int k=0;			
	for (int j=0; j < processEffortByStages.size(); j++) {
		inf = (ProcessEffortByStageInfo)processEffortByStages.elementAt(j);
		
		//totalPlan[j] = totalPlan[j] + inf.planned[i];		
		if (!Double.isNaN(inf.forecast[i])){
			if (Double.isNaN(totalForecast[j]))
				totalForecast[j] = inf.forecast[i];
			else
				totalForecast[j] =totalForecast[j] + inf.forecast[i];
		}
					
		if (!Double.isNaN(inf.norm[i])){
			if (Double.isNaN(totalNorm[j]))
				totalNorm[j] = inf.norm[i];
			else
				totalNorm[j] =totalNorm[j] + inf.norm[i];
		}			
			
		if (isReplan){%>
			<TD><div id='rox<%=i%>stage<%=j%>'>
		<%			
			updateValue = Double.NaN;
			
			if (currentStage == 0)				
				updateValue = inf.norm[i];//+averageAdjustment2
			else
				updateValue = (j >= currentStage)?inf.forecast[i]:inf.actual[i];
			%>
			<%=CommonTools.formatDouble(updateValue)%>
			</div></TD>
		<%}else{%>
			<TD><div id='rox<%=i%>stage<%=j%>'><%=CommonTools.formatDouble(inf.norm[i]+averageAdjustment2)%></div></TD>
		<%}
		
		if (isUpdate && inf.isOpen){		
			updateValue = Double.NaN;			
			if (currentStage == 0)
				updateValue = inf.planned[i];
			else
				updateValue = (j==0)?inf.planned[i]:inf.rePlanned[i];
						
		%>
			<TD class="CellBGRnews"><input id ='row<%=i%>stage<%=j%>' name='plan<%=i+"p"+j%>' value='<%=CommonTools.updateDouble(updateValue)%>' onchange="recalc2();" maxlength = '11' size='7'></TD>
		<%}
		else
		{
			updateValue = Double.NaN;
			if (currentStage == 0)
				updateValue = inf.planned[i];
			else
				updateValue = (isReplan && j>0)?inf.rePlanned[i]:inf.planned[i];//+averageAdjustment;
			
		%>
			<TD class="CellBGRnews"><div id='row<%=i%>stage<%=j%>'><%=CommonTools.formatDouble(updateValue)%></div></TD>
		<% 		
		}
		
		if (!Double.isNaN(updateValue)){
			if (Double.isNaN(totalPlan[j]))
			 totalPlan[j] = updateValue;
			else
			 totalPlan[j] = totalPlan[j] + updateValue;
		}		
			
	}
%>
</TR>
<%}%>
<TR class='TableLeft'>
    <TD colspan=2> <%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Total")%> </TD>
    <TD><div id='total'></DIV></TD>
<%for (int j=0; j < processEffortByStages.size(); j++) {
	inf = (ProcessEffortByStageInfo)processEffortByStages.elementAt(j);
	if (inf.isOpen || !isReplan){
		if (currentStage == 0){
	%>
	<TD><%=CommonTools.formatDouble(totalNorm[j])%></TD>
	<%}else{%>
	<TD><%=CommonTools.formatDouble(totalForecast[j])%></TD>
	<%}%>
	 <TD><DIV id='total<%=j%>'></DIV></TD>
 <%}else{%>    
 <TD><DIV id='total<%=j%>'></DIV></TD>
	<TD><%=CommonTools.formatDouble(totalPlan[j])%></TD>
 <%}
 }%>
</TR>
</TABLE>
<P>

<%
if ( right2 == 3 ){
    if (!isUpdate){
        if(currentStage != 0){
            if (isReplan){
		        if (projectInfo.getActualFinishDate() == null){%>
					<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Update")%>" onclick="doIt('<%=Constants.GET_PAGE+"&page=effDetailBatchPlan.jsp&update=1&isReplan=1"%>')">
		        <%}
       		}
       }else{
        %>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Update")%>" onclick="doIt('<%=Constants.GET_PAGE+"&page=effDetailBatchPlan.jsp&update=1"%>')">
        <%}%>
			<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Back")%>" onclick="doIt(<%=Constants.EFF_STAGE_GET_LIST%>)">
   	<%}else{%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Save")%>" onclick="valid()">

<%      if(currentStage != 0){%>
<INPUT type="button" class="BUTTONWIDTH" value="<%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Useforecast")%>" onclick="reuse()">
        <%}else{%>
<INPUT type="button" class="BUTTONWIDTH" value="Use Norms" onclick="reuse()">
        <%}%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Cancel")%>" onclick="doIt('<%=Constants.EFF_STAGE_GET_LIST%>')">

    <%}
}
%>
</FORM>

<SCRIPT language="javascript">
//crazy IE 5 bug, recalc cant be called directly
var procnum=<%=ProcessInfo.trackedProcessId.length%>;
var stagenum=<%=processEffortByStages.size()%>;
var jcurrentStage=<%=currentStage%>;
var jbase_effort=<%=projectInfo.getBaseEffort()%>;
function recalc2(){
recalc();
}
function recalc(option){
	var indexMetric;
	var fieldTotal;
	var fieldPercent;
	var value;
	var peffort=<%=CommonTools.formatDouble(projectInfo.getPlannedEffort())%>;
	document.all['total'].innerText =0;
	for (j=0;j<stagenum;j++)
		document.all['total'+j].innerText=0;
		
		
	for (i=0;i<procnum;i++){
		fieldTotal=document.all['totalrow'+i];
		fieldTotal.innerText=0;
		for (j=0;j<stagenum;j++){
			if (j>=<%=currentStage%>||<%=!isReplan%>)
				fieldPercent=document.all['row'+i+'stage'+j];
			else
				fieldPercent=document.all['rox'+i+'stage'+j];
			
			if (fieldPercent){
				if(fieldPercent.value!=null){
					if (option=="valid" ){
						if(<%=!isReplan%>){
							if (!mandatoryFld(fieldPercent,"<%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Plannedeffort")%>") || !positiveFld(fieldPercent,"<%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Plannedeffort")%>"))
								return false;
						}
						else{
							if (!mandatoryFld(fieldPercent,"<%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Replannedeffort")%>") || !positiveFld(fieldPercent,"<%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.Replannedeffort")%>"))
								return false;
							
						}
					}
					value=fieldPercent.value;
				}
				else{
					value=fieldPercent.innerText;
				}
				if(trim(value )!=''&& !isNaN(value)){
					fieldTotal.innerText =parseFloat(fieldTotal.innerText)+parseFloat(value);
					document.all['total'+j].innerText =parseFloat(document.all['total'+j].innerText)+parseFloat(value);
					document.all['total'].innerText =parseFloat(document.all['total'].innerText)+parseFloat(value);
				}else{
					document.all['total'+j].innerText =null;
				}
			}
		}
	
		fieldTotal.innerText=fixstr(fieldTotal.innerText);

	}
	

	
	for (j=0;j<stagenum;j++)		
		if (!isNaN(document.all['total'+j].innerText))
			document.all['total'+j].innerText=fixstr(document.all['total'+j].innerText);
		else
			document.all['total'+j].innerText='N/A';			
			
		document.all['total'].innerText=fixstr(document.all['total'].innerText);
	var total=parseFloat(document.all['total'].innerText);

	if (jcurrentStage > 0)
	{
        if ((total<fixstr( 0.8*peffort)
            ||total>fixstr(1.2*peffort))
            && (option=="valid")){
            alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.effDetailBatchPlan.The__total__of__distribution__values__must__be__between__~PARAM1_NUMBER~__and__~PARAM2_NUMBER~__currently__~PARAM3_NUMBER~")%>',fixstr(0.8*peffort),fixstr(1.2*peffort),document.all['total'].innerText)));
            return false;
        }
	}
	else {
		if (total!=fixstr(jbase_effort)
			&& (option=="valid")){
			alert("The total of distribution values must be equal "+fixstr(jbase_effort)+" (currently: "+document.all['total'].innerText+")");
			return false;
		}
	}
	return true;
}
function reuse(){
	for (i=0;i<procnum;i++){
		for (j=0;j<stagenum;j++){
			if (document.all['row'+i+'stage'+j] && !isNaN(document.all['rox'+i+'stage'+j].innerText))
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

</BODY>
</HTML>
