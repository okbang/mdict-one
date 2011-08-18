<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.*,java.util.Vector " contentType="text/html;charset=UTF-8"%>
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

<META http-equiv="Content-Style-Type" content="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="JavaScript">
        <%@ include file="../javaFns.jsp"%>
</SCRIPT>
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<TITLE>pcbDetail.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
int right = Security.securiPage("Organization PCB",request,response);
Vector metricList=(Vector)session.getAttribute("pcbMetrics");
int vectorIndex=Integer.parseInt(request.getParameter("vtID"));
PCBMetricInfo metricInfo =(PCBMetricInfo)metricList.elementAt(vectorIndex);
PCBGalInfo pcbGalInfo = (PCBGalInfo)session.getAttribute("pcbGalInfo");
int nWorkUnitType = Integer.parseInt((String)session.getAttribute("workUnitType"));
//is the metric time or project based
String colLabel=(metricInfo.timeValues!=null)?languageChoose.getMessage("fi.jsp.pcbDetail.Month1"):languageChoose.getMessage("fi.jsp.pcbDetail.Project");
double[] values= (metricInfo.timeValues!=null)?metricInfo.timeValues:metricInfo.projectValues;
String[] labels=(metricInfo.timeValues!=null)?metricInfo.timeLabels:pcbGalInfo.projectCodes;
%>
<BODY class="BD" onload="<%=CommonTools.getMnuFunc(session)%>">
<P class="TITLE"> <%=languageChoose.paramText(new String[]{"fi.jsp.pcbDetail.PCBreport~PARAM1_PCBREPORT_NAME~",metricInfo.name})%></P>
<TABLE cellspacing="1" class="Table" width="95%">
	<TBODY>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbDetail.Period")%> </TD>
			<TD class="CellBGRnews" colspan=3> <%=languageChoose.paramText(new String[]{"fi.jsp.pcbDetail.from~PARAM1_START_DATE~to~PARAM2_END_DATE~",CommonTools.dateFormat(pcbGalInfo.startDate),CommonTools.dateFormat(pcbGalInfo.endDate)})%>  </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbDetail.Reporttype")%> </TD>
			<TD class="CellBGRnews" colspan=3><%=pcbGalInfo.reportTypeComment%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbDetail.Definition")%> </TD>
			<TD class="CellBGRnews" colspan=3><%=(metricInfo.definition==null?"N/A":metricInfo.definition)%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbDetail.Unit")%> </TD>
			<TD class="CellBGRnews" colspan=3><%=metricInfo.unit%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel" width="25%"> <%=languageChoose.getMessage("fi.jsp.pcbDetail.Average")%> </TD>
			<TD class="CellBGRnews" width="25%"><%=CommonTools.formatDouble(metricInfo.actualAvg)%></TD>
			<TD class="ColumnLabel" width="25%"> <%=languageChoose.getMessage("fi.jsp.pcbDetail.Standarddeviation")%> </TD>
			<TD class="CellBGRnews" width="25%"><%=CommonTools.formatDouble(metricInfo.stDev)%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> LCL </TD>
			<TD class="CellBGRnews"><%=CommonTools.formatDouble(metricInfo.LCL)%></TD>
			<TD class="ColumnLabel"> K </TD>
			<TD class="CellBGRnews"><%=CommonTools.formatDouble(metricInfo.K)%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> UCL </TD>
			<TD class="CellBGRnews"><%=CommonTools.formatDouble(metricInfo.UCL)%></TD>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbDetail.Sigma")%> </TD>
			<TD class="CellBGRnews"><%=CommonTools.formatDouble(metricInfo.sigma)%></TD>
		</TR>
	</TBODY>
</TABLE>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<TBODY>
		<TR>
			<TD class="ColumnLabel" width="14%"><%=colLabel%></TD>
			<TD class="ColumnLabel" width="14%"> <%=languageChoose.getMessage("fi.jsp.pcbDetail.Value")%> </TD>
			<TD rowspan="<%=values.length+1%>" bgcolor="white">
			<%	
				//create Data
				String name = languageChoose.getMessage("fi.jsp.pcbDetail.Values");
				String name2 = "LCL";
				String name3 = "ULC";
			  	ArrayList nameSeries = new ArrayList();
				nameSeries.add(name);
				HashMap valueSeries = new HashMap();
				ArrayList listValuesSeries = new ArrayList();
				ArrayList listValuesSeries2 = new ArrayList();
				ArrayList listValuesSeries3 = new ArrayList();
				valueSeries.put(name, listValuesSeries);
				ArrayList categorySeries = new ArrayList();
			  	
			  	if (!Double.isNaN(metricInfo.UCL)){
			  		nameSeries.add(name2);
			  		nameSeries.add(name3);
			  		valueSeries.put(name2, listValuesSeries2);
			  		valueSeries.put(name3, listValuesSeries3);
			  	}
					String value;
					String nameCategory;
					for (int i=0;i<values.length;i++){
							value=(Double.isNaN(values[i]))?"0":CommonTools.formatDouble(values[i]);
						if (!Double.isNaN(metricInfo.UCL)){
							listValuesSeries2.add(String.valueOf(metricInfo.LCL));
						    listValuesSeries3.add(String.valueOf(metricInfo.UCL));
						}
						listValuesSeries.add(value);
						nameCategory =(labels[i].length()>8)?labels[i].substring(0,7)+"...":labels[i];
						categorySeries.add(nameCategory);
					}
				%>
				<jsp:useBean id="CategoryData" class="com.fms1.chart.LinesDataSet" scope="page"  />
				<jsp:useBean id="LineLabelled" class="com.fms1.chart.LinesChartLabels" scope="page"  />
				<cewolf:chart id="XYChart" title='<%=languageChoose.getMessage(metricInfo.name)%>' type="line" xaxislabel="" yaxislabel="" showlegend = "true" >
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
				<cewolf:img chartid="XYChart" renderer="/cewolf" width="600" height="400">
				  <cewolf:map linkgeneratorid="xyLinkGenerator" useJFreeChartTooltipGenerator="true">
				  	
				  </cewolf:map>
				</cewolf:img>
			</TD> 
		</TR>
		<%
		
		for (int i=0;i<values.length;i++){
		%><TR class="CellBGRnews">
			<TD><%=labels[i]%></TD>
			<TD><%=CommonTools.formatDouble(values[i])%></TD>
		</TR>
<%}%>
	</TBODY>
</TABLE>
<BR>
<%
int valBelowLCL=0;
int valAboveUCL=0;
for (int i =0; i<values.length;i++){
	if (values[i]<metricInfo.LCL)
		valBelowLCL++;
	else if(values[i]>metricInfo.UCL)
		valAboveUCL++;

}
%>
<FORM name="frmComments" method ="POST" action="Fms1Servlet?reqType=<%=Constants.PCB_SAVEDETAILS%>">
<input type="hidden" name="vtID" value="<%=vectorIndex%>">
<TABLE cellspacing="1" class="Table" width="95%">
	<TBODY>
		<TR class="ColumnLabel">
			<TD rowspan="<%=valBelowLCL+valAboveUCL+2%>" > <%=languageChoose.getMessage("fi.jsp.pcbDetail.Analysis")%> </TD>
			<TD colspan=2><%=((valAboveUCL==0)? languageChoose.paramText(new String[]{"fi.jsp.pcbDetail.No~PARAM1_LABEL~withvalueshigherthanUCL",colLabel}): languageChoose.paramText(new String[]{"fi.jsp.pcbDetail.~PARAM1_LABEL~withvalueshigherthanUCL",colLabel}))%></TD>
		</TR>
		<%for (int i =0;i<values.length;i++){
			if (values[i]>metricInfo.UCL){
		%><TR class="CellBGRnews">
			<TD><%=labels[i]%>:</TD>
			<TD>
			<%if (right==3){%><INPUT type="text" name="txtProject<%=i%>" size="60" maxlength="200" value="<%=((metricInfo.projectComments[i]==null)?"":metricInfo.projectComments[i])%>">
			<%}else{%><%=((metricInfo.projectComments[i]==null)?"":metricInfo.projectComments[i])%>
			<%}%></TD>
		</TR>
		<%	}
		}%>
		<TR>
			<TD class="ColumnLabel" colspan=2><%=((valBelowLCL==0)? languageChoose.paramText(new String[]{"fi.jsp.pcbDetail.No~PARAM1_LABEL~withvalueslowerthanLCL",colLabel}): languageChoose.paramText(new String[]{"fi.jsp.pcbDetail.~PARAM1_LABEL~withvalueslowerthanLCL",colLabel}))%> </TD>
		</TR>
		<%for (int i =0;i<values.length;i++){
			if (values[i]<metricInfo.LCL){
		%><TR class="CellBGRnews">
			<TD><%=labels[i]%>:</TD>
			<TD>
			<%if (right==3){%><INPUT type="text" name="txtProject<%=i%>" size="60"	maxlength="200" value="<%=((metricInfo.projectComments[i]==null)?"":metricInfo.projectComments[i])%>">
			<%}else{%><%=((metricInfo.projectComments[i]==null)?"":metricInfo.projectComments[i])%>
			<%}%></TD>
		</TR>
		<%	}
		}%>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbDetail.Suggestion")%> </TD>
			<TD class="CellBGRnews" colspan=2><%if (right==3){%><TEXTAREA rows="7" cols="70" name="txtSuggestion"><%=((metricInfo.suggestion==null)?"":metricInfo.suggestion)%></TEXTAREA><%}else{%><%=((metricInfo.suggestion==null)?"":ConvertString.toHtml(metricInfo.suggestion))%><%}%></TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<BR>
<%if (right==3)	{%>
<INPUT	type="button" name="btnOK" value=" <%=languageChoose.getMessage("fi.jsp.pcbDetail.Save")%> " class="BUTTON" onClick="saveComments()">
<%}%>
<INPUT	type="button" name="btnOK" value=" <%=languageChoose.getMessage("fi.jsp.pcbDetail.Back")%> " class="BUTTON" onClick="doIt('<%=Constants.GET_PAGE%>&page=Group/pcbMetrics.jsp')">
<P></P>
</BODY>
<SCRIPT language="JavaScript">
function saveComments(){
	if (maxLength(frmComments.txtSuggestion,"<%=languageChoose.getMessage("fi.jsp.pcbDetail.Suggestion")%>",400))
		frmComments.submit();
}
</SCRIPT>
</HTML>
