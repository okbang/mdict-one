<%@page import="com.fms1.infoclass.*, com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>effStage.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right = Security.securiPage("Effort",request,response);
	Vector vtStage=(Vector)session.getAttribute("stageEffortVector");
	Vector vtProcess=(Vector)session.getAttribute("processEffortVector");
	Vector processEffortByStages=(Vector)session.getAttribute("processEffortByStages" );	
    boolean oneIsClosed = false;
    ProcessEffortByStageInfo inf;
    int currentStage = 0;
    for (; currentStage < processEffortByStages.size(); currentStage++) {
        inf = (ProcessEffortByStageInfo)processEffortByStages.elementAt(currentStage);
        if (!inf.isOpen ) {
            oneIsClosed = true;
            break;
        }
    }
	double [] totals=new double[5];
	Arrays.fill(totals,Double.NaN);
%>
<BODY onload="loadPrjMenu()" class="BD">
<p class="TITLE"> <%=languageChoose.getMessage("fi.jsp.effStage.EffortStageandprocess")%> </p>
<p><%=languageChoose.getMessage("fi.jsp.EffStage.Unlessspecifiedtheunitforeffortmetricsispersonday")%></p>
<FORM name=frm method="POST">
<TABLE class="Table" cellspacing="1" width="95%">
    <TBODY >
        <TR  class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.effStage.Stage")%> </TD>
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.effStage.Norm")%> </TD>                       
            <TD> <%=languageChoose.getMessage("fi.jsp.effStage.Planned")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.effStage.RePlanned")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.effStage.Actual")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.effStage.Deviation")%> </TD>
        </TR>
        <%    
        	boolean bl=false;
        	String rowStyle="";
        	
        	for(int i=0;i<vtStage.size();i++)
        	{
        		StageEffortInfo stageInfo=(StageEffortInfo)vtStage.get(i);
        		
        		rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
     			CommonTools.totals(totals,new double[]{stageInfo.norm,stageInfo.estimated,stageInfo.reEstimated,stageInfo.actual});
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><%=ConvertString.toHtml(stageInfo.stage)%></TD>
            <TD><%=CommonTools.formatDouble(stageInfo.norm)%></TD>             
            <TD><%=CommonTools.formatDouble(stageInfo.estimated)%></TD>
            <TD><%=CommonTools.formatDouble(stageInfo.reEstimated)%></TD>    
            <TD><%=CommonTools.formatDouble(stageInfo.actual)%></TD>                   
            <TD><%=CommonTools.formatDouble(stageInfo.deviation)%></TD>
            
        </TR>
        <%}%>
        <TR class="TableLeft">
            <TD align="center"></TD>
            <TD><B> <%=languageChoose.getMessage("fi.jsp.effStage.Total")%> </B></TD>
            <TD><B><%=CommonTools.formatDouble(totals[0])%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totals[1])%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totals[2])%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totals[3])%></B></TD>
            <TD><B><%=CommonTools.formatDouble(CommonTools.metricDeviation(totals[1],totals[2],totals[3]))%></B></TD>
        </TR>
    </TBODY> 
</TABLE>
<p><BR></p>
<TABLE class="Table" cellspacing="1" width="95%">
<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.EffStage.Processeffort")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="24" align="center">#</TD>
            <TD class="ColumnLabel" width="25%"> <%=languageChoose.getMessage("fi.jsp.effStage.Process")%> </TD>
            <TD class="ColumnLabel" width="25%"> <%=languageChoose.getMessage("fi.jsp.effStage.Norm1")%> </TD>                       
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.effStage.Planned1")%> </TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.effStage.RePlanned1")%> </TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.effStage.Actual1")%> </TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.effStage.Deviation1")%> </TD>
            
        </TR>
        <%         	
        	ProcessEffortInfo processInfo;
        	Arrays.fill(totals,Double.NaN);
        	for(int i=0;i<vtProcess.size();i++){
        		processInfo=(ProcessEffortInfo)vtProcess.get(i);
        		
        		rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
				CommonTools.totals(totals,new double[]{processInfo.norm,processInfo.estimated,processInfo.reEstimated,processInfo.actual});
        		      		
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
            <TD><B> <%=languageChoose.getMessage("fi.jsp.effStage.Total1")%> </B></TD>
            <TD><B><%=CommonTools.formatDouble(totals[0])%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totals[1])%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totals[2])%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totals[3])%></B></TD>
            <TD><B><%=CommonTools.formatDouble(CommonTools.metricDeviation(totals[1],totals[2],totals[3]))%></B></TD>
        </TR>
    </TBODY>
</TABLE>
<P>
<INPUT type="button" class="BUTTONWIDTH"  value=" <%=languageChoose.getMessage("fi.jsp.effStage.Detailplan")%> " onclick="doIt(<%=Constants.EFF_GET_BATCH_PLAN%>)">
<%if (oneIsClosed){%>
<INPUT type="button" class="BUTTONWIDTH" value=" <%=languageChoose.getMessage("fi.jsp.effStage.Detailreplan")%> " onclick="doIt('<%=Constants.EFF_GET_BATCH_PLAN%>&isReplan=1')">
<%}%>
</P>

</FORM>
</BODY>
</HTML>
