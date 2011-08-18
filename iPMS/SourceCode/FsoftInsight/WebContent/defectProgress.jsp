<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.infoclass.StageInfo,com.fms1.web.*,java.util.*,java.io.*,java.text.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
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
<TITLE>defect progress</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
int right=Security.securiPage("Defects",request,response);  
int [][] defects=(int [][])session.getAttribute("defectProgress");
long startDate= ((Date)session.getAttribute("projStartDate")).getTime();
int size=defects[1].length;
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.defectProgress.Defectprogress")%> </P>

<%
	if (size > 1){
		//create Data
		String series1 = languageChoose.getMessage("fi.jsp.defectProgress.Defectsfound");
		String series2 = languageChoose.getMessage("fi.jsp.defectProgress.Defectsclosed");
	  	ArrayList nameSeries = new ArrayList();
		nameSeries.add(series1);
		nameSeries.add(series2);
		HashMap valueSeries = new HashMap();
		HashMap values1 = new HashMap();
		HashMap values2 = new HashMap();
		valueSeries.put(series1, values1);
		valueSeries.put(series2, values2);
		Date date;
	    for (int i=0;i<size;i++){
			date = new Date(startDate+(long)i*86400000l);
			values1.put(date,new Double(defects[0][i]));
			values2.put(date,new Double(defects[1][i]));
	    }
%>

<TABLE>
<TR>
<TD>
<jsp:useBean id="TimeData" class="com.fms1.chart.TimeDataSet" scope="page"  />
<jsp:useBean id="TimeLabelled" class="com.fms1.chart.TimeChartLabels" scope="page"  />
<cewolf:chart id="TimeChart" title="" type="timeseries" xaxislabel='<%=languageChoose.getMessage("fi.jsp.defectProgress.Time")%>' yaxislabel='<%=languageChoose.getMessage("fi.jsp.defectProgress.Numberofdefect")%>' showlegend = "true" >
    <cewolf:colorpaint color="#AAAAFFEE"/>
    <cewolf:data>
        <cewolf:producer id="TimeData">
        	<cewolf:param 
                name="<%=StringConstants.paramNameSeries%>" 
                value="<%= nameSeries %>"/>
            <cewolf:param 
                name= "<%=StringConstants.paramValueSeries%>"
                value="<%= valueSeries %>"/>
        </cewolf:producer>
    </cewolf:data>
    <cewolf:chartpostprocessor id="TimeLabelled"/>
</cewolf:chart>
<cewolf:img chartid="TimeChart" renderer="/cewolf" width="700" height="380">
  <cewolf:map linkgeneratorid="xyLinkGenerator" useJFreeChartTooltipGenerator="true">
  </cewolf:map>
</cewolf:img>
</TD>
</TR>
</TABLE>
<%
	}else{
%>
<P><B> <%=languageChoose.getMessage("fi.jsp.defectProgress.Thedefectprogresswillbeavailab")%> </B></P>
<%
	}
%>
</BODY>
</HTML>
