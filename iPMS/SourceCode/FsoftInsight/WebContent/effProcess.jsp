<%@page import="com.fms1.infoclass.*, com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet"
	type="text/css">
<TITLE>effProcess.jsp</TITLE>
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
	int right = Security.securiPage("Effort",request,response);
	Vector vtProcess = (Vector)session.getAttribute("processEffortVector");
	Vector vtProcessByStage = (Vector)session.getAttribute("processEffortByStageVector");
	Vector vtStage = (Vector)session.getAttribute("stageEffortVector");
%>
<BODY onload="loadPrjMenu()" class="BD">
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.effProcess.EffortProcessEffort")%></p>
<p><%=languageChoose.getMessage("fi.jsp.effProcess.Unlessspecifiedtheunitforeffortmetricsispersonday")%></p>
<TABLE class="Table" cellspacing="1" width="95%">
<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.effProcess.Processeffort")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="24" align="center">#</TD>
            <TD class="ColumnLabel" width="25%"><%=languageChoose.getMessage("fi.jsp.effProcess.Process")%></TD>
            <TD class="ColumnLabel" width="25%"><%=languageChoose.getMessage("fi.jsp.effProcess.Norm")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effProcess.Planned")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effProcess.Replanned")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effProcess.Actual")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effProcess.Deviation")%></TD>
        </TR>
        <%
         	boolean bl = false;
        	String rowStyle = "";
        	double totalNorm = 0;
        	double totalEstimated = 0;
        	double totalReestimated = 0;
        	double totalActual = 0;
        	for(int i=0;i<vtProcess.size();i++){
        		ProcessEffortInfo processInfo = (ProcessEffortInfo)vtProcess.get(i);
        		rowStyle = (bl)?"CellBGRnews":"CellBGR3";
  				bl = !bl;
  				totalEstimated = CommonTools.addDouble(totalEstimated, processInfo.estimated);
	        	totalReestimated = CommonTools.addDouble(totalReestimated, processInfo.reEstimated);
	        	totalNorm = CommonTools.addDouble(processInfo.norm, totalNorm);
        		totalActual = CommonTools.addDouble(processInfo.actual, totalActual);
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><%=ConvertString.toHtml(languageChoose.getMessage(processInfo.process))%></TD>
            <TD><%=CommonTools.formatDouble(processInfo.norm)%></TD>
            <TD><%=CommonTools.formatDouble(processInfo.estimated)%></TD>
            <TD><%=CommonTools.formatDouble(processInfo.reEstimated)%></TD>
            <TD><%=CommonTools.formatDouble(processInfo.actual)%></TD>
            <TD><%=CommonTools.formatDouble(processInfo.deviation)%></TD>
        </TR>
        <%}%>
        <TR class="TableLeft">
            <TD align="center"></TD>
            <TD><B><%=languageChoose.getMessage("fi.jsp.effProcess.Total")%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalNorm)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalEstimated)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalReestimated)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalActual)%></B></TD>
            <TD><B></B></TD>
        </TR>
    </TBODY>
</TABLE>
<P>
<%if (right == 3 && !isArchive){%>
<INPUT type="button" class="BUTTON" name="Update" value="<%=languageChoose.getMessage("fi.jsp.effProcess.Update")%>" onclick="jumpURL('effortProcessUpdate.jsp')">
<%}%>
</P>
<p></p>
<FORM name=frm method="POST">
<TABLE class="Table" cellspacing="1" width="95%">
<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.effProcess.Stageeffort")%></CAPTION>
    <TBODY >
        <TR  class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD width="25%"><%=languageChoose.getMessage("fi.jsp.effProcess.Stage")%></TD>
            <TD class="ColumnLabel" width="25%"><%=languageChoose.getMessage("fi.jsp.effProcess.Norm")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effProcess.Planned")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effProcess.Replanned")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effProcess.Actual")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effProcess.Deviation")%></TD>
        </TR>
        <%
        	bl = false;
        	rowStyle = "";
        	totalNorm = 0;
        	totalEstimated = 0;
        	totalReestimated = 0;
        	totalActual = 0;
        	for(int i = 0; i < vtStage.size(); i++){
        		StageEffortInfo stageInfo = (StageEffortInfo)vtStage.get(i);
        		rowStyle = (bl)?"CellBGRnews":"CellBGR3";
  				bl = !bl;
	        	totalEstimated = CommonTools.addDouble(totalEstimated, stageInfo.estimated);
	        	totalReestimated = CommonTools.addDouble(totalReestimated, stageInfo.reEstimated);
        		totalNorm = CommonTools.addDouble(totalNorm, stageInfo.norm);
        		totalActual = CommonTools.addDouble(totalActual, stageInfo.actual);
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><%if(right == 3){%><A href="stageEffortUpdate.jsp?vtID=<%=i%>"><%=ConvertString.toHtml(stageInfo.stage)%></A><%}else{%><%=ConvertString.toHtml(stageInfo.stage)%><%}%></TD>
            <TD><%=CommonTools.formatDouble(stageInfo.norm)%></TD>
            <TD><%=CommonTools.formatDouble(stageInfo.estimated)%></TD>
            <TD><%=CommonTools.formatDouble(stageInfo.reEstimated)%></TD>
            <TD><%=CommonTools.formatDouble(stageInfo.actual)%></TD>
            <TD><%=CommonTools.formatDouble(stageInfo.deviation)%></TD>
        </TR>
        <%}%>
        <TR class="TableLeft">
            <TD align="center"></TD>
            <TD><B><%=languageChoose.getMessage("fi.jsp.effProcess.Total")%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalNorm)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalEstimated)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalReestimated)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalActual)%></B></TD>
            <TD><B></B></TD>
        </TR>
    </TBODY>
</TABLE>
<p></p>
<br>
<TABLE class="Table" cellspacing="1" width="98%">
<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.effProcess.Processeffortforeachstage")%></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD  width="24" align="center">#</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.Stage")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.Requirement")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.Design")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.Coding")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.Deployment")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.CustomerSupport")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.Test")%>&nbsp;&nbsp;&nbsp;&nbsp;</TD>
			<TD>CM&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.Planning")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.Monitoring")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.QualityControl")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.Training")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.effProcess.Other")%></TD>
		</TR>
		<%         	
			bl = false;
        	float totalByProcess1 = Float.NaN;
        	float totalByProcess2 = Float.NaN;
        	float totalByProcess3 = Float.NaN;
        	float totalByProcess4 = Float.NaN;
        	float totalByProcess5 = Float.NaN;
        	float totalByProcess6 = Float.NaN;
        	float totalByProcess7 = Float.NaN;
        	float totalByProcess8 = Float.NaN;
        	float totalByProcess9 = Float.NaN;
        	float totalByProcess10 = Float.NaN;
        	float totalByProcess11 = Float.NaN;
        	float totalByProcess12 = Float.NaN;
        	for(int i = 0; i < vtProcessByStage.size(); i++){
        		ProcessEffortByStageInfo_Old processInfoByStage = (ProcessEffortByStageInfo_Old)vtProcessByStage.get(i);
        		if(bl){
        			rowStyle = "CellBGRnews";
        		}
  				else{
  					rowStyle = "CellBGR3";
  				}
  				bl = !bl;
  				totalByProcess1 = CommonTools.addFloat(totalByProcess1, processInfoByStage.requirement);
  				totalByProcess2 = CommonTools.addFloat(totalByProcess2, processInfoByStage.design);
  				totalByProcess3 = CommonTools.addFloat(totalByProcess3, processInfoByStage.coding);
  				totalByProcess4 = CommonTools.addFloat(totalByProcess4, processInfoByStage.deployment);
  				totalByProcess5 = CommonTools.addFloat(totalByProcess5, processInfoByStage.customerSupport);
  				totalByProcess6 = CommonTools.addFloat(totalByProcess6, processInfoByStage.test);
  				totalByProcess7 = CommonTools.addFloat(totalByProcess7, processInfoByStage.configManagement);
  				totalByProcess8 = CommonTools.addFloat(totalByProcess8, processInfoByStage.projectPlaning);
  				totalByProcess9 = CommonTools.addFloat(totalByProcess9, processInfoByStage.projectMonitoring);
  				totalByProcess10 = CommonTools.addFloat(totalByProcess10,processInfoByStage.qualityControl);
  				totalByProcess11 = CommonTools.addFloat(totalByProcess11,processInfoByStage.training);
  				totalByProcess12 = CommonTools.addFloat(totalByProcess12, processInfoByStage.other);
        %>
		<TR class="<%=rowStyle%>">
			<TD align="center"><%=i+1%></TD>
			<TD><%=processInfoByStage.stage_name%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.requirement)%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.design)%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.coding)%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.deployment)%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.customerSupport)%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.test)%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.configManagement)%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.projectPlaning)%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.projectMonitoring)%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.qualityControl)%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.training)%></TD>
			<TD><%=CommonTools.formatDouble(processInfoByStage.other)%></TD>
		</TR>
		<%}%>
		<TR class="TableLeft">
			<TD align="center"></TD>
			<TD><B><%=languageChoose.getMessage("fi.jsp.effProcess.Total")%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess1)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess2)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess3)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess4)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess5)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess6)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess7)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess8)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess9)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess10)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess11)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalByProcess12)%></B></TD>
		</TR>
	</TBODY>
</TABLE>
<p></p>
</FORM>
</BODY>
</HTML>