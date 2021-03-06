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
<TITLE>TeamSizeProgress.jsp</TITLE>
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
int right=Security.securiPage("Work order",request,response);  
TeamSizeInfo info=(TeamSizeInfo)session.getAttribute("team size info");
int size=info.teamSize.size();
String backbutton=(String) request.getParameter("back");
String backButton="doIt("+backbutton+")";

%>

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.TeamSizeProgress.TeamSizeProgress")%> </P>
<%if (size >= 1){
	//create Data
		String series1 = languageChoose.getMessage("fi.jsp.TeamSizeProgress.TeamSize");
	  	ArrayList nameSeries = new ArrayList();
		nameSeries.add(series1);
		HashMap valueSeries = new HashMap();
		HashMap values1 = new HashMap();
		valueSeries.put(series1, values1);
		Date date;
	    for (int i=0;i<size;i++){
			date = (Date)info.periods.elementAt(i);
			values1.put(date,new Double((String)info.teamSize.elementAt(i)));
	    }
%>
<jsp:useBean id="TimeData" class="com.fms1.chart.TimeDataSet" scope="page"  />
<jsp:useBean id="TimeLabelled2" class="com.fms1.chart.TimeChartLabel2" scope="page"  />
<cewolf:chart id="TimeChart" title="" type="timeseries" xaxislabel='<%=languageChoose.getMessage("fi.jsp.TeamSizeProgress.Time")%>' yaxislabel='<%=languageChoose.getMessage("fi.jsp.TeamSizeProgress.TeamSize")%>' showlegend = "false" >
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
    <cewolf:chartpostprocessor id = "TimeLabelled2"/>
</cewolf:chart>
<cewolf:img chartid="TimeChart" renderer="/cewolf" width="700" height="380">
  <cewolf:map linkgeneratorid="xyLinkGenerator" useJFreeChartTooltipGenerator="true">
  </cewolf:map>
</cewolf:img>
<br>
<%}else{%>
<P><B> <%=languageChoose.getMessage("fi.jsp.TeamSizeProgress.Theteamsizeprogresswillbeavail")%> </B></P>
<%}%>


<INPUT type="button" class="BUTTON" name="back" value=" <%=languageChoose.getMessage("fi.jsp.TeamSizeProgress.Back")%> " onclick="<%=backButton%>">
</BODY>
</HTML>
