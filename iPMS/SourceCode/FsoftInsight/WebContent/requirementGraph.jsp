<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<%@taglib uri="/WEB-INF/cewolf-1.1.tld" prefix="cewolf" %>
<%@page import="java.util.*"%>
<%@page import="de.laures.cewolf.*"%>
<%@page import="de.laures.cewolf.tooltips.*"%>
<%@page import="de.laures.cewolf.links.*"%>
<%@page import="org.jfree.data.gantt.*"%>
<%@page import="org.jfree.chart.*"%>
<%@page import="org.jfree.chart.plot.CategoryPlot"%>
<%@page import="java.awt.*" %>
<%@ page import="de.laures.cewolf.taglib.CewolfChartFactory" %>
<%@ page import="org.jfree.chart.event.ChartProgressListener" %>
<%@ page import="org.jfree.chart.event.ChartProgressEvent" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>requirementGraph.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right = Security.securiPage("Requirements",request,response);
	final Vector stageList= (Vector)request.getAttribute("stageList");
	RequirementInfo reqHdrInfo = (RequirementInfo)request.getAttribute("reqHdrInfo");
	final Vector rcrByStageInfos = (Vector)request.getAttribute("RCRByStageInfos");
	int size=(rcrByStageInfos==null)?0:rcrByStageInfos.size();
	
	// HaiMM: CR - Add Requirement section
	boolean existData = false;
	ReqChangesInfo reqInfo = (ReqChangesInfo) session.getAttribute("ReqChangesInfo");	
	if (reqInfo != null) existData = true;
	
%>
<BODY class="BD" onload="loadPrjMenu()">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Requirements")%> </P>
<TABLE class="HDR">
    <TBODY>
        <TR>
            <TD></TD>
            <TD></TD>
            <TD width="20%"></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Totalcommittedsize")%> </TD>
            <TD style="text-align: right"><%=reqHdrInfo.sumSizeCommitted%></TD>
            <TD></TD>
        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Projectlifecycle")%> </TD>
            <TD><%=languageChoose.getMessage(ProjectInfo.parseLifecycle(reqHdrInfo.lifecycleID))%></TD>
            <TD></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Totaldesignedsize")%> </TD>
            <TD style="text-align: right"><%=reqHdrInfo.sumSizeDesigned%></TD>
            <TD></TD>
        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Requirementcompletion")%> </TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(reqHdrInfo.sumSizeCompletion)%></TD>
            <TD>%</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Totalcodedsize")%> </TD>
            <TD style="text-align: right"><%=reqHdrInfo.sumSizeCoded%></TD>
            <TD></TD>
        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Requirementstability")%> </TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(reqHdrInfo.sumSizeStability)%></TD>
            <TD>%</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Totaltestedsize")%> </TD>
            <TD style="text-align: right"><%=reqHdrInfo.sumSizeTested%></TD>
            <TD></TD>
        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Totalrequirementsize")%> </TD>
            <TD style="text-align: right"><%=reqHdrInfo.sumSizeNotCancelled%></TD>
            <TD></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Totaldeployedsize")%> </TD>
            <TD style="text-align: right"><%=reqHdrInfo.sumSizeDeployed%></TD>
            <TD></TD>
        </TR>
        <TR>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Averageresponsetimedays")%> </TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(reqHdrInfo.avgResponseTime)%></TD>
            <TD></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.requirementGraph.Totalacceptedsize")%> </TD>
            <TD style="text-align: right"><%=reqHdrInfo.sumSizeAccepted%></TD>
            <TD></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<%	
	//create Data
  	ArrayList nameSeries = new ArrayList();
	nameSeries.add("RCR Deviation (%)");
	HashMap valueSeries = new HashMap();
	ArrayList values = new ArrayList();
	valueSeries.put("RCR Deviation (%)", values);
	ArrayList categorySeries = new ArrayList();
  	
  	double actual1=0;
	double plan1=0;
	RCRByStageInfo info1;
	StageInfo sinf1;
	double retval1;
	for (int i=0;i<stageList.size();i++){
		info1=(RCRByStageInfo)rcrByStageInfos.elementAt(i);
		sinf1=(StageInfo)stageList.elementAt(i);
		if (sinf1.aEndD==null)
			break;
		plan1=(Double.isNaN(info1.rePlan) ? info1.plan : info1.rePlan)+actual1;
		if (!Double.isNaN(info1.actual))
			actual1+=info1.actual;
		if (plan1==0 || Double.isNaN(plan1))
			retval1=0;
		else
			retval1=(actual1-plan1)*100d/plan1;
		values.add(String.valueOf(retval1));
		categorySeries.add(info1.stageName);
	}
%>
<jsp:useBean id="CategoryData" class="com.fms1.chart.LinesDataSet" scope="page"  />
<jsp:useBean id="LineLabelled" class="com.fms1.chart.LinesChartLabels" scope="page"  />
<cewolf:chart id="XYChart" title="" type="line" xaxislabel="" yaxislabel="" showlegend = "true" >
    <cewolf:colorpaint color="#AAAAFFEE"/>
    <cewolf:data>
        <cewolf:producer id="CategoryData">
        	<cewolf:param 
                name="<%=StringConstants.paramNameSeries%>" 
                value="<%= nameSeries %>"/>
            <cewolf:param 
                name= "<%=StringConstants.paramValueSeries%>"
                value="<%= valueSeries %>"/>
            <cewolf:param 
                name= "<%=StringConstants.paramCategorySeries%>"
                value="<%= categorySeries %>"/>
        </cewolf:producer>
    </cewolf:data>
    <cewolf:chartpostprocessor id="LineLabelled"/>
</cewolf:chart>
<cewolf:img chartid="XYChart" renderer="/cewolf" width="600" height="300">
  <cewolf:map linkgeneratorid="xyLinkGenerator" useJFreeChartTooltipGenerator="true">
  	
  </cewolf:map>
</cewolf:img>
<BR>
<BR>
<% if (!existData) { %>
<P class="ERROR"> There is no requirement changes data.</P>
<% } else { %>
<TABLE cellspacing="1" class="Table" width="85%" id="tableReqChangesMng">
<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plReqChangesMng.RequirementChangesManagement")%> </CAPTION>
   	<TBODY>
        <TR>
            <TD class="ColumnLabel" width = 35%> Where is the change request logged? </TD>
            <TD class="CellBGR3">&nbsp;<%=reqInfo.reqLogLocation == null ? "N/A": ConvertString.toHtml(reqInfo.reqLogLocation)%> </TD>
        </TR>        
    	<TR>
        	<TD class="ColumnLabel" width = 35%> Who logs the change request? </TD>
            <TD class="CellBGR3" >&nbsp;<%= reqInfo.reqCreator == null ? "N/A": ConvertString.toHtml(reqInfo.reqCreator)%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width = 35%> Who reviews the change request? </TD>
            <TD class="CellBGR3">&nbsp;<%=reqInfo.reqReviewer == null ? "N/A": ConvertString.toHtml(reqInfo.reqReviewer)%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width = 35%> Who approves the change request? </TD>
            <TD class="CellBGR3">&nbsp;<%=reqInfo.reqApprover == null ? "N/A": ConvertString.toHtml(reqInfo.reqApprover)%> </TD>
        </TR>
    </TBODY>
</TABLE>
<% } %>
<BR>
<BR>
</BODY>
</HTML>
