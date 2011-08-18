<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>REQUIREMENT</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
	int right = Security.securiPage("Requirements",request,response);
	Vector RCRByProcessInfos=(Vector)session.getAttribute("RCRByProcessInfos");
		double [] totals=new double[5];
	Arrays.fill(totals,Double.NaN);
%>
<BODY class="BD" onload="loadPrjMenu()">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.StageandprocessRCR")%> </P>

<BR>
<FORM name="frm" action="Fms1Servlet?reqType=<%=Constants.REQUIREMENT_UPDATE_PROCESS%>" method="POST">
<TABLE class="Table" width="95%" cellspacing="1" id="tableReq">
  <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.RCRtablebyprocess")%> </CAPTION>
  <TR  class="ColumnLabel">
  	<TD width="10">#</TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.Engineeringprocess")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.Norm")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.Plan")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.Replan")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.Actual")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.Deviation")%> </TD>
  </TR>
  <%RCRByProcessInfo procInf;
  Arrays.fill(totals,Double.NaN);
  String className;
  for (int i=0;i<RCRByProcessInfos.size();i++){
   	procInf=(RCRByProcessInfo)RCRByProcessInfos.elementAt(i);
   	className= (i%2==0) ?"CellBGRnews":"CellBGR3";
   	CommonTools.totals(totals,new double[]{procInf.norm,procInf.plan,procInf.rePlan,procInf.actual});
   	%>
   	
   <TR  class="<%=className%>">
  	<TD><%=i+1%></TD>
  	<TD ><%=languageChoose.getMessage(procInf.processName)%></TD>
  	<TD ><div id='norm<%=i%>'><%=CommonTools.formatDouble(procInf.norm)%></div></TD>
  	<TD ><input name="plan<%=i%>" value="<%=CommonTools.updateDouble(procInf.plan)%>" onchange="assbug()" maxlength = '6' size='4'></TD>
  	<TD ><%=CommonTools.formatDouble(procInf.rePlan)%></TD>
  	<TD ><%=CommonTools.formatDouble(procInf.actual)%></TD>
  	<TD ><%=CommonTools.formatDouble(procInf.deviation)%></TD>
  </TR>
  <%}%>
   <TR  class="TableLeft">
  	<TD colspan=2> <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.Total")%> </TD>
  	<TD ><%=CommonTools.formatDouble(totals[0])%></TD>
  	<TD ><div id='fieldTotal'><%=CommonTools.formatDouble(totals[1])%></div></TD>
  	<TD ><%=CommonTools.formatDouble(totals[2])%></TD>
  	<TD ><%=CommonTools.formatDouble(totals[3])%></TD>
  	<TD ><%=CommonTools.formatDouble(CommonTools.metricDeviation(totals[1],totals[2],totals[3]))%></TD>
  </TR>
</TABLE>
<BR>
<%if (right==3 && !isArchive){%>
<INPUT type="button" class="BUTTON" onclick="dome()" value=" <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.Update")%> ">
<INPUT type="button" class="BUTTON" onclick="reuse()" value=" <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.Usenorms")%> ">
<%}%>
<INPUT type="button" class="BUTTON" onclick="doIt(<%=Constants.REQUIREMENT_PLAN%>);" value=" <%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.Cancel")%> ">
</FORM>
<SCRIPT language="javascript">
function reuse(){
var max = <%=RCRByProcessInfos.size()%>;
for (var i=0;i < max;i++)
	document.all['plan'+i].value =document.all['norm'+i].innerText;
recalc();

}
function dome(){
if (recalc("valid"))
frm.submit();
}
function assbug(){
recalc();
}
function recalc(option){

	var fieldTotal=document.all['fieldTotal'];
	var fieldPercent;
	fieldTotal.innerText=0;
	var max = <%=RCRByProcessInfos.size()%>;
	for (var i=0;i < max;i++){
		
		fieldPercent=document.all['plan'+i];
		if(trim(fieldPercent.value)!='')
			fieldTotal.innerText =parseFloat(fieldTotal.innerText)+parseFloat(fieldPercent.value);


		if (option=="valid" ){
			if (!(mandatoryFld(fieldPercent,"<%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.PlannedRCR")%>") && percentageFld(fieldPercent,"<%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.PlannedRCR")%>")))
				return false;
		}
	}
	fieldTotal.innerText=fixstr(fieldTotal.innerText);
	if (fieldTotal.innerText != 100){
		if (option=="valid"){
			alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.requirementPlanProcess.The__total__of__distribution__values__must__be__equal__to__100__currently__~PARAM1_NUMBER~") %>', fieldTotal.innerText)));
			document.all['plan0'].focus();
			return false;
		}
	}
	return true;
}


</SCRIPT>

</BODY>
</HTML>
