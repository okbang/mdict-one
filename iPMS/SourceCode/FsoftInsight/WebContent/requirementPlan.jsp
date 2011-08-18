<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>requirementPlan.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right = Security.securiPage("Requirements",request,response);
	boolean oneIsClosed="1".equals((String)request.getAttribute("oneClosed"));
	Vector RCRByStageInfos=(Vector)session.getAttribute("RCRByStageInfos");
	Vector RCRByProcessInfos=(Vector)session.getAttribute("RCRByProcessInfos");
	double [] totals=new double[5];
	Arrays.fill(totals,Double.NaN);
	%>
<BODY class="BD" onload="loadPrjMenu()">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.requirementPlan.StageandprocessRCR")%> </P>

<BR>
<TABLE class="Table" width="95%" cellspacing="1" id="tableReq">
  <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.requirementPlan.RCRtablebystage")%> </CAPTION>
  <TR  class="ColumnLabel">
  	<TD width="10">#</TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Stage")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Norm")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Plan")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Replan")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Actual")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Deviation")%> </TD>
  </TR>
  <%RCRByStageInfo stageInf;
   String className;
 
  for (int i=0;i<RCRByStageInfos.size();i++){
   	stageInf=(RCRByStageInfo)RCRByStageInfos.elementAt(i);
   	className= (i%2==0) ?"CellBGRnews":"CellBGR3";
   	CommonTools.totals(totals,new double[]{stageInf.norm,stageInf.plan,stageInf.rePlan,stageInf.actual});
   	%>
   	
   <TR  class="<%=className%>">
  	<TD><%=i+1%></TD>
  	<TD ><%=stageInf.stageName%></TD>
  	<TD ><%=CommonTools.formatDouble(stageInf.norm)%></TD>
  	<TD ><%=CommonTools.formatDouble(stageInf.plan)%></TD>
  	<TD ><%=CommonTools.formatDouble(stageInf.rePlan)%></TD>
  	<TD ><%=CommonTools.formatDouble(stageInf.actual)%></TD>
  	<TD ><%=CommonTools.formatDouble(stageInf.deviation)%></TD>
  </TR>
  <%}%>
   <TR  class="TableLeft">
  	<TD colspan=2> <%=languageChoose.getMessage("fi.jsp.requirementPlan.Total")%> </TD>
  	<TD ><%=CommonTools.formatDouble(totals[0])%></TD>
  	<TD ><%=CommonTools.formatDouble(totals[1])%></TD>
  	<TD ><%=CommonTools.formatDouble(totals[2])%></TD>
  	<TD ><%=CommonTools.formatDouble(totals[3])%></TD>
  	<TD ><%=CommonTools.formatDouble(CommonTools.metricDeviation(totals[1],totals[2],totals[3]))%></TD>
  </TR>
</TABLE>
<BR>
<TABLE class="Table" width="95%" cellspacing="1" id="tableReq">
  <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.requirementPlan.RCRtablebyprocess")%> </CAPTION>
  <TR  class="ColumnLabel">
  	<TD width="10">#</TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Engineeringprocess")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Norm1")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Plan1")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Replan1")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Actual1")%> </TD>
  	<TD > <%=languageChoose.getMessage("fi.jsp.requirementPlan.Deviation1")%> </TD>
  </TR>
  <%RCRByProcessInfo procInf;
  Arrays.fill(totals,Double.NaN);
  for (int i=0;i<RCRByProcessInfos.size();i++){
   	procInf=(RCRByProcessInfo)RCRByProcessInfos.elementAt(i);
   	className= (i%2==0) ?"CellBGRnews":"CellBGR3";
   	%>
   	
   <TR  class="<%=className%>">
  	<TD><%=i+1%></TD>
  	<TD ><%=languageChoose.getMessage(procInf.processName)%></TD>
  	<TD ><%=CommonTools.formatDouble(procInf.norm)%></TD>
  	<TD ><%=CommonTools.formatDouble(procInf.plan)%></TD>
  	<TD ><%=CommonTools.formatDouble(procInf.rePlan)%></TD>
  	<TD ><%=CommonTools.formatDouble(procInf.actual)%></TD>
  	<TD ><%=CommonTools.formatDouble(procInf.deviation)%></TD>
  </TR>
  <% 
      CommonTools.totals(totals,new double[]{procInf.norm,procInf.plan,procInf.rePlan,procInf.actual}); 
   } 
  %> 
   <TR  class="TableLeft">
  	<TD colspan=2> <%=languageChoose.getMessage("fi.jsp.requirementPlan.Total1")%> </TD>
  	<TD ><%=CommonTools.formatDouble(totals[0])%></TD>
  	<TD ><%=CommonTools.formatDouble(totals[1])%></TD>
  	<TD ><%=CommonTools.formatDouble(totals[2])%></TD>
  	<TD ><%=CommonTools.formatDouble(totals[3])%></TD>
  	<TD ><%=CommonTools.formatDouble(CommonTools.metricDeviation(totals[1],totals[2],totals[3]))%></TD>
  </TR>
</TABLE>
<BR>
<%if (right==3){%>
<INPUT type="button"  class="BUTTONWIDTH" value=" <%=languageChoose.getMessage("fi.jsp.requirementPlan.Planprocess")%> " onclick="doIt('<%=Constants.GET_PAGE%>&page=requirementPlanProcess.jsp')" >
<%}%>
<INPUT type="button" class="BUTTONWIDTH"  value=" <%=languageChoose.getMessage("fi.jsp.requirementPlan.Detailplan")%> " onclick="doIt(<%=Constants.REQUIREMENT_PLAN_RCR_BATCH%>)">
<%if (oneIsClosed){%>
<INPUT type="button" class="BUTTONWIDTH" value=" <%=languageChoose.getMessage("fi.jsp.requirementPlan.Detailreplan")%> " onclick="doIt(<%=Constants.REQUIREMENT_REPLAN_RCR_BATCH%>)">
<%}%>

</BODY>
</HTML>
