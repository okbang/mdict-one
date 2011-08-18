<%@page import="com.fms1.infoclass.*, com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
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
<TITLE>effInformation.jsp</TITLE>
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
	EffortInfo info=(EffortInfo)request.getAttribute("effortHeaderInfo");
	String newProject=(String)request.getAttribute("newproject");
	String effStatus=(String)request.getAttribute("effortStatus");
	Vector stageEffortVector=(Vector)request.getAttribute("stageEffortVector");
	Vector stageList=(Vector)request.getAttribute("stageList");
	int size=(stageEffortVector==null)?0:stageEffortVector.size();
%>
<BODY onload="loadPrjMenu()" class="BD">
<p class="TITLE"> <%=languageChoose.getMessage("fi.jsp.effInformation.EffortInformation")%> </p>
<TABLE width="95%" class="HDR">
    <TBODY>
        <TR>
            <TD  width="30%"> <%=languageChoose.getMessage("fi.jsp.effInformation.Budgetedeffort")%> </TD>
            <TD align="right"  width="15%"><%=CommonTools.formatDouble(info.budgetedEffort)%></TD>
            <TD width="5%" ></TD>
            <TD  width="30%"> <%=languageChoose.getMessage("fi.jsp.effInformation.Managementeffort")%> </TD>
            <TD align="right"  width="15%"><%=CommonTools.formatDouble(info.perManagementEffort)%></TD>
        </TR>
        <TR>
            <TD  width="30%"> <%=languageChoose.getMessage("fi.jsp.effInformation.Actualeffort")%> </TD>
            <TD align="right"  width="15%"><%=CommonTools.formatDouble(info.actualEffort)%></TD>
            <TD width="5%" ></TD>
            <TD  width="30%"> <%=languageChoose.getMessage("fi.jsp.effInformation.Developementeffort")%> </TD>
            <TD align="right"  width="15%"><%=CommonTools.formatDouble(info.perDevelopementEffort)%></TD>
        </TR>
        <TR>
            <TD  width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Calendareffort")%></TD>
            <TD align="right"  width="15%"><%=CommonTools.formatDouble(info.calendarEffort)%></TD>
            <TD width="5%" ></TD>
            <TD  width="30%"> <%=languageChoose.getMessage("fi.jsp.effInformation.Qualityeffort")%> </TD>
            <TD align="right"  width="15%"><%=CommonTools.formatDouble(info.perQualityEffort)%></TD>
        </TR>
        <TR>
            <TD  width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Effortefficiency")%></TD>
            <TD align="right"  width="15%"><%=CommonTools.formatDouble(info.effortEfficiency)%></TD>
            <TD width="5%" ></TD>
            <TD  width="30%" > <%=languageChoose.getMessage("fi.jsp.effInformation.Correctioneffort")%> </TD>
            <TD align="right"  width="15%" ><%=CommonTools.formatDouble(info.perCorrectionEffort)%></TD>
        </TR>
        <TR>
            <TD  width="30%"> <%=languageChoose.getMessage("fi.jsp.effInformation.Efforteffectiveness")%> </TD>
            <TD align="right"  width="15%"><%=CommonTools.formatDouble(info.effortUseage)%></TD>
            <TD width="5%" ></TD>
            <TD  width="30%"> <%=languageChoose.getMessage("fi.jsp.effInformation.Translationeffort")%> </TD>
            <TD align="right"  width="15%"><%=CommonTools.formatDouble(info.perTranslationEffort)%></TD>
        </TR>
        <TR>
            <TD  width="30%"> <%=languageChoose.getMessage("fi.jsp.effInformation.Effortdeviation")%> </TD>
            <TD align="right"  width="15%"><%=CommonTools.formatDouble(info.effortDeviation)%></TD>
            <TD width="5%" ></TD>
            <TD  width="30%"></TD>
            <TD align="right"  width="15%"></TD>
        </TR>
        <TR>
            <TD  width="30%"> <%=languageChoose.getMessage("fi.jsp.effInformation.Effortstatus")%> </TD>
            <TD align="right"  width="15%"><%=effStatus%></TD>
            <TD width="5%" ></TD>
            <TD  width="30%"></TD>
            <TD align="right"  width="15%"></TD>
        </TR>
    </TBODY>
</TABLE>
<p></P>
<%	
	//create Data
	String name = languageChoose.getMessage("fi.jsp.effInformation.Values");
  	ArrayList nameSeries = new ArrayList();
	nameSeries.add(name);
	HashMap valueSeries = new HashMap();
	ArrayList values = new ArrayList();
	valueSeries.put(name, values);
	ArrayList categorySeries = new ArrayList();
  	double actual=0;
	double plan=0;
	StageEffortInfo seinfo;
	StageInfo sinf;
	double retval;
	for (int i=0;i<stageList.size();i++){
		seinfo=(StageEffortInfo)stageEffortVector.elementAt(i);
		sinf=(StageInfo)stageList.elementAt(i);
		if (sinf.aEndD==null)
			break;
		plan=(Double.isNaN(seinfo.reEstimated) ? seinfo.estimated: seinfo.reEstimated)+actual;
		if (!Double.isNaN(seinfo.actual))
			actual+=seinfo.actual;
						
		if (plan==0 || Double.isNaN(plan))
			retval=0;
		else
			retval=(actual-plan)*100d/plan;
		values.add(String.valueOf(retval));
		categorySeries.add(sinf.stage);
	}
%>
<jsp:useBean id="CategoryData" class="com.fms1.chart.LinesDataSet" scope="page"  />
<jsp:useBean id="LineLabelled" class="com.fms1.chart.LinesChartLabels" scope="page"  />
<cewolf:chart id="XYChart" title='<%=languageChoose.getMessage("fi.jsp.effInformation.Effortdeviation1")%>' type="line" xaxislabel="" yaxislabel="" showlegend = "true" >
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
</BODY>
</HTML>
