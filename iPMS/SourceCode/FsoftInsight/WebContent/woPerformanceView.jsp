<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woPerformanceView.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT type="text/javascript" src="jscript/jquery-1.2.3.pack.js"></SCRIPT>
<SCRIPT type="text/javascript" src="jscript/htmltooltip.js"></SCRIPT>

<SCRIPT language="javascript">

<%@ include file="javaFns.jsp"%>
</SCRIPT>
<style type="text/css">

DIV.htmltooltip{
position: absolute; /*leave this alone*/
display: none; /*leave this alone*/
width: 300px;
left: 0; /*leave this alone*/
top: 0; /*leave this alone*/
background: lightyellow;
border: 2px solid gray;
border-width: 1px 2px 2px 1px;
padding: 5px;
}

</style>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	String sourcePage = (String) session.getAttribute("PageSource");
%>
<%
	int right = Security.securiPage("Work Order",request,response);
	int i = 0;
	session.removeAttribute("pageSourceQuality");
	Vector perfVector = (Vector) session.getAttribute("WOPerformanceVector");
	Vector stdMetrics = (Vector) session.getAttribute("WOStandardMetricMatrix");
	Vector cmList = (Vector) session.getAttribute("WOCustomeMetricList");
	String mnuDisable = request.getParameter("mnuDisable");
	EffortInfo info1=(EffortInfo)session.getAttribute("effortHeaderInfo"); 
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
	// Add by HaiMM
	Vector dpVt=(Vector)session.getAttribute("defectPrevention");
	
	// get stagelist from depend on projectID
	// create by anhtv08
	// date :3/7/2009

	Vector stageList = (Vector)session.getAttribute("WOMetricsStageList");
	StageInfo stageInfo= (StageInfo)session.getAttribute("StageInfo");
	String projectCatalog= (String)session.getAttribute("projectCatalog");
	StageInfo stageInfoDP= (StageInfo)session.getAttribute("StageInfoDP");
	
	int check = 0;
	if (stageList != null && stageList.size() > 0) check = 1;
	
%>

<BODY class="BD" onload="<%=((mnuDisable!=null)?"refresh()":"loadPrjMenu()")%>">

<!--
	created by :anhtv08- start
	tooltip for workorder metrics .
-->
<!--
	Customer Satisfaction  
-->

<DIV class="htmltooltip">
	<DL>
		<DT><b>- Objective</b></DT>
		<DD>Measures the satisfaction of customer requirements.</DD>
		<DT><B>- Formula and Application Scope</B></DT>
		<%
			if(projectCatalog.equalsIgnoreCase("0"))
			{
				%>
				<DD><b>+ Development</b>:<BR>
				= Total point of customer survey point
				
				Note: 
				1. Max is 100, Min = 0
				2. Point of Customer survey: Refer to “Regulation” sheet of CSS template for point calculation. 
				3. For a project that Customer survey is not done
				 Case 1: If project can NOT give a proper explanation and clear evidence, by default customer survey point is 40 
				Case 2: if project can give a proper explanation and clear evidence which are approved by Branch QA Manager, by default customer survey point is 50. 
						</DD>
				<%
								
			} else if(projectCatalog.equalsIgnoreCase("1"))
			{
				%>
		<DD><B>+ Maintenance:</B>  <BR>= ((Requirement Size * Requirement Completed Rate)/ Total committed (Requirement Size))*100%
		</DD>
		<%}
			else 
			{%>
			<DD><B>+ Test: </B><BR> = Total point of customer survey point
			
			Note: 
			1. Max is 100, Min = 0
			2. Point of Customer survey: Refer to “Regulation” sheet of CSS template for point calculation. 
			3. For a project that Customer survey is not done
			 Case 1: If project can NOT give a proper explanation and clear evidence, by default customer survey point is 40 
			Case 2: if project can give a proper explanation and clear evidence which are approved by Branch QA Manager, by default customer survey point is 50. 

			</DD>
			<DD><B>+ Call Center</B> :<BR>= Total point of customer survey point
		
			Note: 
			1. Max is 100, Min = 0
			2. Point of Customer survey: Refer to “Regulation” sheet of CSS template for point calculation. 
			3. For a project that Customer survey is not done
			 Case 1: If project can NOT give a proper explanation and clear evidence, by default customer survey point is 40 
			Case 2: if project can give a proper explanation and clear evidence which are approved by Branch QA Manager, by default customer survey point is 50. 
		
			</DD>
			<DD><b>+ Migration/Conversion:</B> <BR>'= Total point of customer survey point
		
			Note: 
			1. Max is 100, Min = 0
			2. Point of Customer survey: Refer to “Regulation” sheet of CSS template for point calculation. 
			3. For a project that Customer survey is not done
			 Case 1: If project can NOT give a proper explanation and clear evidence, by default customer survey point is 40 
			Case 2: if project can give a proper explanation and clear evidence which are approved by Branch QA Manager, by default customer survey point is 50. 
		
			</DD>
			<%}%>
		
		
	</DL> 
</DIV >
<!--
	Leakage 
-->
<DIV class="htmltooltip">
	<DL>
		<DT><b>- Objective</b></DT>
		<DD>Measure quality of the product and service after delivery for acceptance test. 
</DD>
		<DT><B>- Formula and Application Scope</B></DT>
		<%
		if(projectCatalog.equalsIgnoreCase("0"))
		{%>
					<DD><b>+ Development</b>:<BR>= No of Defects detected after delivery for acceptance test of a product/Product size
					OR
					= No of Defects detected after delivery for acceptance test of a product/No of defect detected during project life cycle
					
					OR
					= No of Defect detected by Customer after delivery for acceptance test of a product/Total No of Defect detected on the product
							</DD>		
						
		<%}
		else if(projectCatalog.equalsIgnoreCase("1"))
		{
		%>
		<DD><B>+ Maintenance:</B>  <BR>= No of Defects detected after delivery for acceptance test of a product/Product size
		OR
		= No of Defects detected after delivery for acceptance test of a product/No of defect detected during project life cycle
		OR
		= No of Defect detected by Customer after delivery for acceptance test of a product/Total No of Defect detected on the product
		</DD>
	
		<%}
		else 
		{%>
		
		<DD><B>+ Test: </B><BR> = No of Defects detected after delivery for acceptance test of a product/Product size
		OR
		= No of Defects detected after delivery for acceptance test of a product/No of defect detected during project life cycle
		OR
		= No of Defect detected by Customer after delivery for acceptance test of a product/Total No of Defect detected on the product
		</DD>
		<DD><B>+ Call Center</B> :<BR>= No of customer complaints or defect detected by external QA/Total No of request
		</DD>
		<DD><b>+ Migration/Conversion:</B> <BR>(= No of Defects detected after delivery for acceptance test of a product/Product size
		OR
		= No of Defects detected after delivery for acceptance test of a product/No of defect detected during project life cycle
		OR
		= No of Defect detected by Customer after delivery for acceptance test of a product/Total No of Defect detected on the product
		</DD>		
		<%}%>	
		
		
	</DL> 
</DIV >
<!--
	Process Compliance  
-->
<DIV class="htmltooltip">
	<DL>
		<DT><b>- Objective</b></DT>
		<DD>Measure the compliance of project activities as with the defined rule/standard/process/regulation.</DD>
		<DT><B>- Formula and Application Scope</B></DT>
		<%if(projectCatalog.equalsIgnoreCase("0"))
		{
		%>
			<DD><b>+ Development</b>:<BR>=(Requirement Size * Requirement Completed Rate)/ Total committed (Requirement Size)) *100%
		</DD>
		<%}
		
		else if(projectCatalog.equalsIgnoreCase("1"))
		{
		%>
		<DD><B>+ Maintenance:</B>  <BR>=  No of NCs detected on the/time, OR
		= No of NCs detected on the area/No of Review or Audit
		</DD>
		
		<%}
		
		else 
		{%>
		
		<DD><B>+ Test: </B><BR> = = No of NCs detected on the/time, OR
		= No of NCs detected on the area/No of Review or Audit
		</DD>
		<DD><B>+ Call Center</B> :<BR>= No of NCs detected on the/time, OR
		= No of NCs detected on the area/No of Review or Audit
		</DD>
		<DD><b>+ Migration/Conversion:</B> <BR> No of NCs detected on the/time, OR
		= No of NCs detected on the area/No of Review or Audit
		</DD>
		<%}%>
		
		
		
	</DL> 
</DIV >
<!--
	Effort Efficiency  
-->
<DIV class="htmltooltip">
	<DL>
		<DT><b>- Objective</b></DT>
		<DD>Measure efficiency of project effort usage</DD>
		<DT><B>- Formula and Application Scope</B></DT>
		<% if(projectCatalog.equalsIgnoreCase("0"))
		{
		%>
		<DD><b>+ Development</b>:<BR>=(Billable effort/Calendar effort)*100%
		</DD>
		<%}
		else if (projectCatalog.equalsIgnoreCase("1"))
		{
			%>
		<DD><B>+ Maintenance:</B>  <BR>=(Billable effort/Calendar effort)*100%
		</DD>
		<%}
		
		else 
		{
		%>
		<DD><B>+ Test: </B><BR> = (Billable effort/Calendar effort)*100%
		</DD>
		<DD><B>+ Call Center</B> :<BR> (Billable effort/Calendar effort)*100%
		</DD>
		<DD><b>+ Migration/Conversion:</B> <BR> (Billable effort/Calendar effort)*100%
		</DD>
		<%}%>		
		
		
	</DL> 
</DIV >
<!--
	Correction Cost 
-->
<DIV class="htmltooltip">
	<DL>
		<DT><b>- Objective</b></DT>
		<DD>Indicates cost of correcting a defective product and the associated rework/damage costs</DD>
		<DT><B>- Formula and Application Scope</B></DT>
		<% if (projectCatalog.equalsIgnoreCase("0"))
		{
		%>
		<DD><b>+ Development</b>:<BR>=(Effort Usage on Technical Rework/Total Effort Usage)*100%
		<br>
		where
		<br>
		- Effort Usage on Technical rework is effort spent on "Engineering processes" with Type = Correct
		<br>- Engineering processes are: Requirement, Design, Coding, Deployment, Customer Support, Testing
		</DD>
		<%}
		
		else if (projectCatalog.equalsIgnoreCase("1"))
		{
		%>
			
		<DD><B>+ Maintenance:</B>  <BR>=  (Effort Usage on Technical Rework/Total Effort Usage)*100%
		<br>where
		<br>
		- Effort Usage on Technical rework is effort spent on "Engineering processes" with Type = Correct
		<br>
		- Engineering processes are: Requirement, Design, Coding, Deployment, Customer Support, Testing
		</DD>
		<%}
		
		else
		{%>
		
		<DD><B>+ Test: </B><BR> = = (Effort Usage on Technical Rework/Total Effort Usage)*100%
		<br>where<br>
		 - Effort Usage on Technical rework is effort spent on "Engineering processes" with Type = Correct
		<br>
		- Engineering processes are: Requirement, Design, Coding, Deployment, Customer Support, Testing
		</DD>
		
		<DD><B>+ Call Center</B> :<BR> (Effort Usage on Technical Rework/Total Effort Usage)*100%<br>
		where
		<br>
		- Effort Usage on Technical rework is effort spent on "Engineering processes" with Type = Correct
		<br>
		- Engineering processes are: Requirement, Design, Coding, Deployment, Customer Support, Testing
		</DD>
		<DD><b>+ Migration/Conversion:</B> <BR>(Effort Usage on Technical Rework/Total Effort Usage)*100%
		<br>where
		<br>
		- Effort Usage on Technical rework is effort spent on "Engineering processes" with Type = Correct
		<br>
		- Engineering processes are: Requirement, Design, Coding, Deployment, Customer Support, Testing
		</DD>
		<%}%>
	
		
		
	</DL> 
</DIV >

<!--
	Timeliness  
-->
<DIV class="htmltooltip">
	<DL>
		<DT><b>- Objective</b></DT>
		<DD>Measures the ability to satisfy customer in timeliness.</DD>
		<DT><B>- Formula and Application Scope</B></DT>
		<% if (projectCatalog.equalsIgnoreCase("0"))
		{
		%>
		<DD><b>+ Development</b>:<BR>= (No of deliverable deliveried on time/ Total # of deliverables deliveried)*100%
		</DD>
		<%}
		else if (projectCatalog.equalsIgnoreCase("1"))
		{
		%>
		<DD><B>+ Maintenance:</B>  <BR>= (No of deliverable deliveried in time/ Total # of deliverables deliveried)*100%
		
		<%}
		
		else 
		{
		%>
		<DD><B>+ Test: </B><BR> =  (No of deliverable deliveried in time/ Total # of deliverables deliveried)*100%
		</DD>
		<DD><B>+ Call Center</B> :<BR> (No of deliverable deliveried in time/ Total # of deliverables deliveried)*100%
		</DD>
		<DD><b>+ Migration/Conversion:</B> <BR>= (No of deliverable deliveried in time/ Total # of deliverables deliveried)*100%
		</DD>
		<%}%>
		
		
	</DL> 
</DIV>
<!--
	Requirement Completeness..
-->
<DIV class="htmltooltip">
	<DL>
		<DT><b>- Objective</b></DT>
		<DD>Measures percentage of requirement completed against the committed and predict the amount of remain work</DD>
		<DT><B>- Formula and Application Scope</B></DT>
		<% if (projectCatalog.equalsIgnoreCase("0"))
		{
		%>
		<DD><b>+ Development</b>:<BR>=(Requirement Size * Requirement Completed Rate)/ Total committed (Requirement Size)) *100%
		</DD>
		
		<%}
		
		else if (projectCatalog.equalsIgnoreCase("1"))
		{
		%>
				<DD><B>+ Maintenance:</B>  <BR>= ((Requirement Size * Requirement Completed Rate)/ Total committed (Requirement Size))*100%
		</DD>
		
		<%}
		
		else 
		{
		%>
		<DD><B>+ Test: </B><BR> = ((Testing Size * Testing Completed Rate)/ Total committed (Testing Size))*100% 
				<BR>(Where Testing Size = No of Test case or Function or Test request)
		</DD>
		
		<DD><B>+ Call Center</B> :<BR>(No of Request Closed/ Total No of Request)*100%
		</DD>
		<DD><b>+ Migration/Conversion:</B> <BR>((Requirement Size * Requirement Completed Rate)/ Total committed (Requirement Size))*100%
		</DD>
		<%}%>	
	</DL> 
</DIV >
<!-- tooltip for metrics-->

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.workOrderMetrics")%></P> 
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.StandardObjectives")%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD ><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Metric")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Unit")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Committed")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformanceView.ReCommitted")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Actual")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Deviation")%></TD>           
        </TR>
<%
MetricInfo metricInfo;
String className ;
String planVal;
String rePlanVal;
String actVal;
boolean style = false;
// HUYNH2 comment and add Billable Effort
// some index
// 0 - StartDate
// 1 - End Date
// 2 - Duration
// 3 - Max Tem size
// 4 - Effort Usage
// 5 - Development 
// 6 - Management
// 7 - Quality
// 8 - Billable Effort
// 9 - Calendar effort    // Added by trungtn, 31-Jan-07
// some line code below process move Billable Effort, Calendar effort to above Effort Usage
Vector temMetricInfo = new Vector();
for(i = 0; i < 8; i++){
	if(i==3){
		temMetricInfo.addElement((MetricInfo)perfVector.elementAt(i));
		temMetricInfo.addElement((MetricInfo)perfVector.elementAt(8));
		temMetricInfo.addElement((MetricInfo)perfVector.elementAt(9));
	}else{
		temMetricInfo.addElement((MetricInfo)perfVector.elementAt(i));
	}
}
// end move Billable Effort, Calendar effort  to above Effort Usage


for(i = 0; i < 7; i++){ // Modified by HaiMM: Change from 10 --> 7 to remove Development, Management, Quality (Change Request)
	metricInfo=(MetricInfo)temMetricInfo.elementAt(i);
	//metricInfo=(MetricInfo)perfVector.elementAt(i);
	style=!style;
 	className=(style)?"CellBGRnews":"CellBGR3";
 	if (metricInfo.unit.equalsIgnoreCase("dd-mmm-yy")){ 
 		//metric is a date
 		planVal=CommonTools.dateFormat(metricInfo.plannedValue);
 		rePlanVal=CommonTools.dateFormat(metricInfo.rePlannedValue);
 		actVal=CommonTools.dateFormat(metricInfo.actualValue);
	}
 	else{
 		//metric is a number
 		planVal=CommonTools.formatDouble(metricInfo.plannedValue);
 		rePlanVal=CommonTools.formatDouble(metricInfo.rePlannedValue);
 		actVal=CommonTools.formatDouble(metricInfo.actualValue);
	}
// Use metric name instead
//	if ((i==0||i==3||i>5))
//		rePlanVal="";
	if (metricInfo.name.equalsIgnoreCase("Start date") ||
	    metricInfo.name.equalsIgnoreCase("Maximum team size") ||
	    metricInfo.name.equalsIgnoreCase("&nbsp&nbsp&nbsp Development") ||
	    metricInfo.name.equalsIgnoreCase("&nbsp&nbsp&nbsp Management") ||
	    metricInfo.name.equalsIgnoreCase("&nbsp&nbsp&nbsp Quality"))
	{
		rePlanVal="";
	}
 	%> <TR class="<%=className%>">
 	<% if (metricInfo.name.equals("Maximum team size")){%>
			<TD><A href="Fms1Servlet?reqType=<%=Constants.TEAM_SIZE_PROGRESS%>&back=<%=Constants.WO_PERFORMANCE_GET_LIST%>"><%=languageChoose.getMessage(metricInfo.name)%></A></TD>
	<%} else {%>
			<TD><%=languageChoose.getMessage(metricInfo.name)%></TD>
	<%}%>
			<TD><%=metricInfo.unit%></TD>
			<TD><%=planVal%></TD>
			<TD><%=rePlanVal%></TD>
			<TD><%=actVal%></TD>
			<TD><%=Color.colorByNorm(CommonTools.formatNumber(metricInfo.deviation, true), metricInfo.deviation, metricInfo.lcl, metricInfo.ucl, metricInfo.colorType)%></TD>
		</TR>
<%}%>
</TABLE>
<BR>
<%if(right == 3 && !isArchive){%>
<INPUT type="button" name="Update" value=" <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Update")%> " class="BUTTON" onClick="jumpURL('woPerformance.jsp');">
<%}%>
<BR>
<BR>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
<!--<CAPTION align="left" class="TableCaption"><A name="StandardMetrics"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.StandardMetric")%> </A> </CAPTION>-->
    <TBODY>

        <TR class="ColumnLabel">
	        <TD rowspan="2" align="left" width="25%"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Name")%> </TD>
	        <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Unit")%> </TD>
        	<TD rowspan="1" colspan="3" align="center"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Norm")%> </TD>		
	        <TD rowspan="1" colspan="3" align="center"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Target")%>* </TD>    	
	        <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.ActualValue")%>* </TD>
	        <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Deviation")%> </TD>            
	    	<TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Basic")%> </TD>
		</TR>
	    <TR class="ColumnLabel">
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LCL")%>&nbsp;&nbsp;</TD>
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>&nbsp;&nbsp;</TD>
	    	<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.UCL")%>&nbsp;&nbsp;</TD>
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%>&nbsp;&nbsp;</TD>
	        <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>&nbsp;&nbsp;</TD>
	    	<TD  rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>&nbsp;&nbsp;</TD>
			
        </TR>
            
<%
for(i = 0; i < stdMetrics.size() - 1; i++){
NormInfo normInfo =(NormInfo)stdMetrics.elementAt(i);
className=(i%2==0)?"CellBGRnews":"CellBGR3";
if (i ==0) {
%>
         <TR class="ColumnLabel">
	         <TD colspan="11"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Quality")%></TD>
         </TR>
<%}
if (i ==3) {
%>
         <TR class="ColumnLabel">
	         <TD colspan="11"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.COST")%></TD>
         </TR>
<%}
if (i ==4) 
    if (info1 != null){ 
		normInfo.actualValue = info1.perCorrectionEffort; 
    } 
if (i ==5) {
%>
         <TR class="ColumnLabel">
	         <TD colspan="11"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.DELIVERY")%></TD>
         </TR>
<%}      
%>
	<tr class="<%=className%>">
		<td><A href="#" rel="htmltooltip"> <%=languageChoose.getMessage(normInfo.normName)%> </A></td>
		<td><%=normInfo.normUnit%></td>
		<td><%=CommonTools.formatDouble(normInfo.lcl)%></td>
		<td><%=CommonTools.formatDouble(normInfo.average)%></td>
		<td><%=CommonTools.formatDouble(normInfo.ucl)%></td>
		<td><%=((CommonTools.updateDouble(normInfo.usl)=="")? CommonTools.updateDouble(normInfo.lcl):CommonTools.updateDouble(normInfo.usl))%></td>
		<td><%=CommonTools.updateDouble(normInfo.plannedValue)%></td>
		<td><%=((CommonTools.updateDouble(normInfo.lsl)=="")? CommonTools.updateDouble(normInfo.ucl):CommonTools.updateDouble(normInfo.lsl))%></td>
		<td><%=(CommonTools.updateDouble(normInfo.usl) == "") ? Color.colorByNorm(CommonTools.formatDouble(normInfo.actualValue),normInfo.actualValue, normInfo.usl, normInfo.lsl, normInfo.colorType) : Color.colorByNorm(CommonTools.formatDouble(normInfo.actualValue),normInfo.actualValue, normInfo.lcl, normInfo.ucl, normInfo.colorType)%></td>
		<td><%=(CommonTools.updateDouble(normInfo.plannedValue) == "") ? CommonTools.formatDouble(CommonTools.metricDeviation(normInfo.average, normInfo.average, normInfo.actualValue)) : CommonTools.formatDouble(CommonTools.metricDeviation(normInfo.plannedValue, normInfo.plannedValue, normInfo.actualValue))%></td>
		<td><%=ConvertString.toHtml(normInfo.note)%></td>			
	</tr>
	
<%}%>
</table>
<br>
<%if(right == 3 && !isArchive){%>
<input type="button" name="Update" value=" <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Update1")%> " class="BUTTON" onClick="jumpURL('Fms1Servlet?reqType=<%=Constants.WO_STANDARD_METRIC%>');" >
<%}%>
<BR>
<BR>
<BR>
<!-- form to update  specified objectives-->
<!--
	anhtv08-start
-->

<FORM id="WOLoadCusMetrics" action="Fms1Servlet?reqType=<%=Constants.WO_CUS_METRIC_AUTO_UPDATE%>" method="POST">
<TABLE width="95%">
	<TBODY>
		<TR>
		<%
			if(stageList != null && stageList.size() > 0 ){
		%>
		<TD  width="80%" style="FONT-FAMILY: Verdana, Geneva, Arial, Helvetica, sans-serif; FONT-SIZE: 13" align="right"  colspan="1" >
			 <A>Milestone:</A>
		</TD>
		<TD  class="COMBO"  align="right" colspan="1" nowrap="nowrap" >
			<SELECT name="stageID" class="COMBO" onchange="WOLoadCusMetrics.submit()" >
				<%
		           for (int j = stageList.size()-1; j >= 0; j--) {
	            	StageInfo stageInfoTemp = (StageInfo) stageList.elementAt(j);
				        %>
	    			        <OPTION  value="<%=stageInfoTemp.milestoneID%>"<%=((stageInfoTemp.milestoneID==stageInfo.milestoneID)? " selected":"")%>><%=stageInfoTemp.stage%></OPTION>
						<%
	            	}%>
		           
				</SELECT>
		</TD>
		<%}%>
		</TR>
	</TBODY>
</TABLE>
<TABLE width="95%">
	<TBODY>
		<TR>
		<TD class="TableCaption" width="60%" colspan="2">
		<A><%=languageChoose.getMessage("fi.jsp.woPerformanceView.SpecificObjectives")%></A>
		</TD>
		<td style="font-family: inherit;font-size: 11" nowrap="nowrap" align="right">
			(Note: Select <B>Milestone</B> to display the actual values below)
		</td>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<TABLE cellspacing="1" class="Table" width="95%">
    <TBODY>
            <TR class="ColumnLabel">
	            <TD align = "center" rowspan="2"># </TD>		
	            <TD rowspan="2" align="left" width="25%"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Name1")%> </TD>
	            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Unit1")%> </TD>
	            <TD rowspan="1" colspan="3" align="center"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Target")%></TD>    	
	            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.ActualValue1")%>* </TD>
	            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Deviation1")%> </TD>            
	            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Basic")%> </TD>
			</TR>
	        <TR class="ColumnLabel">
	            <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%>&nbsp;&nbsp;</TD>
	            <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>&nbsp;&nbsp;</TD>
	    		<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>&nbsp;&nbsp;</TD>
            </TR>
<%
if(cmList!=null){
	for(i = 0; i < cmList.size(); i++){
 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
 	WOCustomeMetricInfo info = (WOCustomeMetricInfo) cmList.elementAt(i);
	%>
		<TR class="<%=className%>">
			<TD align = "center"><%=i+1%></TD>		
			<TD><%if(right == 3){%><a href="woCustomeMetricUpdate.jsp?id=<%=i%>"><%}%><%=ConvertString.toHtml(info.name)%><%if(right == 3){%></a><%}%></TD>
			<TD><%=ConvertString.toHtml(info.unit)%></TD>
			<TD style="text-align: left"><%=CommonTools.formatDouble(info.LCL)%></TD>
			<TD style="text-align: left"><%=CommonTools.formatDouble(info.plannedValue)%></TD>
			<TD style="text-align: left"><%=CommonTools.formatDouble(info.UCL)%></TD>
		    <TD style="text-align: left"><%=CommonTools.formatDouble(info.actualValue)%></TD>
		    <TD style="text-align: left"><%=info.deviation%></TD>
		    <TD><%=ConvertString.toHtml(info.note)%></TD>
		</TR>
	<%}%>
<%}%>
</TABLE>

<BR>
<%if((right == 3) && !isArchive){%>
<input type="button" name="Add new" value="<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Addnew")%>" class="BUTTON" onclick="addCusMetric()">
<%}%>
<BR>
<BR>
<BR>
<!-- Add by HaiMM: This section is moving from PP -->
<!-- Add by anhtv08 add combo for defection prevention division-->

<FORM id="WOLoadDefMetrics" action="Fms1Servlet?reqType=<%=Constants.WO_DEF_METRIC_AUTO_UPDATE%>" method="POST">
<TABLE width="95%">
	<TBODY>
		<TR>
		<TD  width="80%" style="FONT-FAMILY: Verdana, Geneva, Arial, Helvetica, sans-serif; FONT-SIZE: 13" align="right"  colspan="1">
			 <A>Milestone:</A>
		</TD>
		<%
			if(stageList != null && stageList.size() > 0 ){
		%>
		<TD  class="COMBO"  align="right" colspan="1" nowrap="nowrap" >
			<SELECT name="stageID_DP" class="COMBO" onchange="WOLoadDefMetrics.submit()" >
				<%
		           for (int j = stageList.size()-1; j >= 0; j--) {
	            	StageInfo stageInfoTemp = (StageInfo) stageList.elementAt(j);
				        %>
	    			        <OPTION  value="<%=stageInfoTemp.milestoneID%>"<%=((stageInfoTemp.milestoneID==stageInfoDP.milestoneID)? " selected":"")%>><%=stageInfoTemp.stage%></OPTION>
						<%
	            	}%>       
				</SELECT>
				<%}%>
		</TD>

		</TR>
	</TBODY>
</TABLE>
</FORM>
<TABLE width="95%">
	<TBODY>
		<TR>
		<TD class="TableCaption" width="60%" colspan="2">
		<A><%=languageChoose.getMessage("fi.jsp.qualityObjective.DefectPreventionGoals")%></A>
		</TD>
		<td style="font-family: inherit;font-size: 11" nowrap="nowrap" align="right">
			<p>(Note: Select <B>Milestone</B> to display the actual values below)</P>
		</td>
		</TR>
	</TBODY>
</TABLE>

<TABLE class="Table" width="95%" cellspacing="1">
    
    <TBODY>
        <TR class="ColumnLabel">
            <TD align = "center" rowspan="2"># </TD>		
            <TD rowspan="2" align="left" width="25%"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Name1")%> </TD>
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Unit1")%> </TD>
            <TD rowspan="1" colspan="3" align="center"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Target")%></TD>    	
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.ActualValue1")%>* </TD>
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Deviation1")%> </TD>            
            <TD rowspan="2"  align="left"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Basic")%> </TD>
		</TR>
        <TR class="ColumnLabel">
            <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%>&nbsp;&nbsp;</TD>
            <TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%>&nbsp;&nbsp;</TD>
    		<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>&nbsp;&nbsp;</TD>

		
        </TR>
        <%
        	boolean bl = true;
        	String rowStyle = "";
        	double totalPlanValue = 0;
        	if(dpVt!=null){
        		for(int tmp=0;tmp<dpVt.size();tmp++){
       			rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				DPTaskInfo dpTaskInfo = (DPTaskInfo) dpVt.get(tmp);
        		if(dpTaskInfo.planValue > 0){
					totalPlanValue = totalPlanValue + dpTaskInfo.planValue;
        		}
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=tmp+1%></TD>
            <TD><A href="dpTaskUpdate.jsp?vtID=<%=tmp%>"><%=dpTaskInfo.item%></A></TD>
            <TD><%=dpTaskInfo.unit%></TD>            
            <TD><%=CommonTools.formatDouble(dpTaskInfo.usl)%></TD>
            <TD><%=CommonTools.formatDouble(dpTaskInfo.planValue)%></TD>
            <TD><%=CommonTools.formatDouble(dpTaskInfo.lsl)%></TD>
            <TD><%=CommonTools.formatDouble(dpTaskInfo.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(dpTaskInfo.deviationValue)%></TD>
		    <TD><%=ConvertString.toHtml(dpTaskInfo.dpCause)%></TD>
        </TR>
        <%}%>
        <%}%>
    </TBODY>
</TABLE>
<%if (right == 3 && !isArchive){%>
<P><INPUT type="button" class="BUTTON" name="btnAddDPTask" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Addnew") %>" onclick="addDPTask();"></P>
<%}%>

<br>

<SCRIPT language="JavaScript">
	var check = <%=check%>;
	function refresh(){
		window.open("menuProject.jsp?mnuDisable="+<%=mnuDisable%>,'menu');
	}
	
	function addCusMetric(){
		if (check > 0) {
			jumpURL('woCustomeMetricAdd.jsp');
		} else {
			window.alert("Please input the Stages of the project first (menu Work order/Stage & Deliv)");
		}
	}
	
	// Add by HaiMM
	function addDPTask(){
		if (check > 0) {
			jumpURL("dpTaskAdd.jsp");
		} else {
			window.alert("Please input the Stages of the project first (menu Work order/Stage & Deliv)");
		}
	}
</SCRIPT>
</BODY>
</HTML>
