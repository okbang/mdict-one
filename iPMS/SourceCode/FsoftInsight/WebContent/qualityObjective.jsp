<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>qualityObjective.jsp</TITLE>
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
	if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
		if (Integer.parseInt(archiveStatus) == 4) {
			isArchive = true;
		}
	}
	int right = Security.securiPage("Project plan",request,response); 
	Vector vt=(Vector)session.getAttribute("moduleVector");
	Vector vtOtherAct=(Vector)session.getAttribute("otherActVector");
	String strQltObj=(String)session.getAttribute("qltObjective");
	Vector dpVt=(Vector)session.getAttribute("defectPrevention");
	Vector darVt=(Vector)session.getAttribute("dar");
	
	Vector vtStrat = (Vector) session.getAttribute("StratOfMeetingList");
	Vector vtReviewStrat = (Vector) session.getAttribute("ReviewStrategyList");
	Vector vtTestStrat = (Vector) session.getAttribute("TestStrategyList");
	Vector vMeasList = (Vector) session.getAttribute("MeasurementsProgList");

	// Add by HaiMM
	Vector estDefect=(Vector)session.getAttribute("estDefect");
	String sumPlanValue = (String)session.getAttribute("sumPlanValue");
	
	Vector perfVector = (Vector) session.getAttribute("WOPerformanceVector");
	Vector stdMetrics = (Vector) session.getAttribute("WOStandardMetricMatrix");
	Vector cmList = (Vector) session.getAttribute("WOCustomeMetricList");
	EffortInfo info1=(EffortInfo)session.getAttribute("effortHeaderInfo"); 
	
	ProjectDateInfo prjDateInfo = (ProjectDateInfo) session.getAttribute("prjDateInfo");
	Date fixed = new Date("03-Apr-2009");
	boolean compareDate = false;
	if (prjDateInfo.actualStartDate != null) compareDate = prjDateInfo.actualStartDate.before(fixed);
	
	// Estimate defect add - Start
	
	DefectInfo defectInfo = (DefectInfo)session.getAttribute("defectInfo");
	final DefectByProcessInfo[] defectProcess = (DefectByProcessInfo[])session.getAttribute("defectProcess");
    Vector moduleList = (Vector) session.getAttribute("defectModuleList");
    Hashtable Process_WorkProduct =  (Hashtable) session.getAttribute("Process_WP");
	Vector stageList = (Vector)session.getAttribute("stageList");
	int minStageSize= stageList.size();

	// Map product with process
	int mSize = 0;
	int tmpSize = 0;
	if (moduleList != null) mSize = moduleList.size();
	Vector[] vModule = new Vector[4];
	
	vModule[0] = new Vector();
	vModule[1] = new Vector();
	vModule[2] = new Vector();
	vModule[3] = new Vector();
	
	for (int i = 0; i < mSize; i++) {
		WPSizeInfo mInfo = (WPSizeInfo) moduleList.elementAt(i);
		if (Process_WorkProduct.containsKey(mInfo.categoryName)) {
			if ("Requirement".equals(Process_WorkProduct.get(mInfo.categoryName))) {
				mInfo.processType = 1;
				vModule[0].addElement(mInfo);
			}
			else if ("Design".equals(Process_WorkProduct.get(mInfo.categoryName))) {
				mInfo.processType = 2;
				vModule[1].addElement(mInfo);
			}
			else if ("Coding".equals(Process_WorkProduct.get(mInfo.categoryName))) {
				mInfo.processType = 3;
				vModule[2].addElement(mInfo);
			}
			else if ("Other".equals(Process_WorkProduct.get(mInfo.categoryName))) {
				mInfo.processType = 4;
				vModule[3].addElement(mInfo);
			}
		}
	}
	session.setAttribute("RequirementProductList",vModule[0]);
	session.setAttribute("DesignProductList",vModule[1]);
	session.setAttribute("CodingProductList",vModule[2]);
	session.setAttribute("OtherProductList",vModule[3]);
	String note = "";
	
	double[] totalPlanReview = new double[4];
	double[] totalPlanTest	 = new double[4];
	// init data
	for (int i=0;i<4;i++){
		totalPlanReview[i] = 0;
		totalPlanTest[i] = 0;
	}
	
	for (int i = 0; i < 4; i++){
		for (int j = 0; j < vModule[i].size(); j++)
		{
			WPSizeInfo mInfo = (WPSizeInfo) vModule[i].elementAt(j);
			if (mInfo.isDefectReview!=0) {
				if (!CommonTools.formatDouble(mInfo.newPlanSizeReview).equalsIgnoreCase("N/A"))
	  			totalPlanReview[i] = totalPlanReview[i] + mInfo.newPlanSizeReview;
	  		}
	  		if (mInfo.isDefectTest!=0) {
				if (!CommonTools.formatDouble(mInfo.newPlanSizeTest).equalsIgnoreCase("N/A"))
		  		totalPlanTest[i] = totalPlanTest[i] + mInfo.newPlanSizeTest;
			}
		}
	}
	
	// Estimate defect add - End
%>
	

<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.qualityObjective.Projectplanquality")%></P>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.StandardObjectives")%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Metric")%></TD>
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
for(int i = 0; i < 8; i++){
	if(i==3){
		temMetricInfo.addElement((MetricInfo)perfVector.elementAt(i));
		temMetricInfo.addElement((MetricInfo)perfVector.elementAt(8));
		temMetricInfo.addElement((MetricInfo)perfVector.elementAt(9));
	}else{
		temMetricInfo.addElement((MetricInfo)perfVector.elementAt(i));
	}
}
// end move Billable Effort, Calendar effort  to above Effort Usage


for(int i = 0; i < 7; i++){ // Modified by HaiMM: Change from 10 --> 7 to remove Development, Management, Quality (Change Request)
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
	    	<TD rowspan="1"  align="left">&nbsp;&nbsp;<%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%>&nbsp;&nbsp;</TD>
        </TR>
            
<%
for(int i = 0; i < stdMetrics.size() - 1; i++){
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
		<td><%=languageChoose.getMessage(normInfo.normName)%></td>
		<td><%=normInfo.normUnit%></td>
		<td><%=CommonTools.formatDouble(normInfo.lcl)%></td>
		<td><%=CommonTools.formatDouble(normInfo.average)%></td>
		<td><%=CommonTools.formatDouble(normInfo.ucl)%></td>
		<td><%=(CommonTools.updateDouble(normInfo.usl) == "") ? CommonTools.formatDouble(normInfo.lcl) : CommonTools.formatDouble(normInfo.usl)%></td>
		<td><%=(CommonTools.updateDouble(normInfo.plannedValue) == "") ? CommonTools.formatDouble(normInfo.average) : CommonTools.formatDouble(normInfo.plannedValue)%></td>
		<td><%=(CommonTools.updateDouble(normInfo.lsl) == "") ? CommonTools.formatDouble(normInfo.ucl) : CommonTools.formatDouble(normInfo.lsl)%></td>
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
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"><A name="custom"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.SpecificObjectives")%> </A></CAPTION>
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
for(int i = 0; i < cmList.size(); i++){
 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
 	WOCustomeMetricInfo info = (WOCustomeMetricInfo) cmList.elementAt(i);
%>
<TR class="<%=className%>">
	<TD align = "center"><%=i+1%></TD>		
	<TD><%if(right == 3){%><a href="woCustomeMetricUpdate.jsp?id=<%=i%>"><%}%><%=ConvertString.toHtml(info.name)%><%if(right == 3){%></a><%}%></TD>
	<TD><%=ConvertString.toHtml(info.unit)%></TD>
	<TD style="text-align: left"><%=(CommonTools.formatDouble(info.LCL) == "") ? CommonTools.formatDouble(info.plannedValue) : CommonTools.formatDouble(info.LCL)%></TD>
	<TD style="text-align: left"><%=CommonTools.formatDouble(info.plannedValue)%></TD>
	<TD style="text-align: left"><%=(CommonTools.formatDouble(info.UCL) == "") ? CommonTools.formatDouble(info.plannedValue) : CommonTools.formatDouble(info.UCL)%></TD>
    <TD style="text-align: left"><%=CommonTools.formatDouble(info.actualValue)%></TD>
    <TD style="text-align: left"><%=info.deviation%></TD>
    <TD><%=ConvertString.toHtml(info.note)%></TD>
</TR>
<%}%>
</TABLE>

<BR>
<%if((right == 3) && !isArchive){%>
<input type="button" name="Add new" value="<%=languageChoose.getMessage("fi.jsp.woPerformanceView.Addnew")%>" class="BUTTON" onclick="checkAdd()">
<%}%>
<BR>
<BR>
<BR>
<!-- Add by HaiMM: This section is moved from PP -->
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><A name="defectprevention"> <%=languageChoose.getMessage("fi.jsp.qualityObjective.DefectPreventionGoals")%> </A></CAPTION>
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
		<!--
        <TR class="TableLeft">
            <TD></TD>
            <TD><B> <%=languageChoose.getMessage("fi.jsp.qualityObjective.Total")%> </B></TD>
            <TD></TD>
            <TD><B><%=CommonTools.formatDouble(totalPlanValue)%></B></TD>
        </TR>
		-->
    </TBODY>
</TABLE>
<%if (right == 3 && !isArchive){%>
<P><INPUT type="button" class="BUTTON" name="btnAddDPTask" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Addnew") %>" onclick="addDPTask();"></P>
<%}%>

<br>
<% if (compareDate) { %>
<FORM name="frm" method="post">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.qualityObjective.Strategytoachievethequalityobjectives")%></CAPTION>
    <TBODY>
        <TR>          
            <TD class="CellBGRnews"><%=com.fms1.tools.CommonTools.stringReplace(strQltObj,"\n","<br>")%></TD>
        </TR>
    </TBODY>
</TABLE>
<p>
<%if (right == 3 && !isArchive){%>
<INPUT type="button" name="btnUpdateObj" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Update") %>" class="BUTTON" onclick="doAction(this)"></p>
<%}%>

</FORM>
<br>
<%}%>

<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.DefectView.Weightedprereleasereviewdefectsbyprocess")%></CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD><INPUT id="selectAll" type ="checkbox" name="selectAll" onclick="selectAll()"/></TD>
			<TD width ="24" align ="center">#</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.DefectView.Process")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.DefectView.Normofwdfoundbyreview")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.DefectView.PlannedFoundByReview")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.DefectView.ActualFoundByReview")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.DefectView.ReviewDeviation")%>(%)</TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	double totalNorm = 0;
	double totalNewPlanSize = 0;	
	double totalActual = 0;
	DefectByProcessInfo processObj;
	rowStyle = "";	
	int iCount = 0;
	boolean existAdd = false;
	for (int i = 0; i < 4; i++){
	
		processObj=defectProcess[i];
		
		if (!CommonTools.formatDouble(processObj.normReview).equalsIgnoreCase("N/A"))
	  		totalNorm = totalNorm + processObj.normReview;  	
	
	  	if (!CommonTools.formatDouble(processObj.actualReview).equalsIgnoreCase("N/A"))
	  		totalActual = totalActual + processObj.actualReview;

%>
		<TR class="CellBGRLongnews">		
			<TD colspan="3" style="font-weight: bold">&nbsp;<%=languageChoose.getMessage(processObj.processName)%></TD>
			<TD style="font-weight: bold"><%=CommonTools.formatDouble(processObj.normReview)%></TD>
			<TD><B><%=CommonTools.formatDouble(totalPlanReview[i])%></B></TD>
			<TD style="font-weight: bold"><%=CommonTools.formatDouble(processObj.actualReview)%></TD>
			<TD style="font-weight: bold"><%=CommonTools.formatDouble(processObj.deviationReview)%></TD>
		</TR>
	
<%		
    	tmpSize = vModule[i].size();
		for (int j = 0; j < tmpSize; j++)
		{
			WPSizeInfo mInfo = (WPSizeInfo) vModule[i].elementAt(j);
			if (mInfo.isDefectReview==0) {
				existAdd = true;
				continue;
			}
			
			iCount++;			
			rowStyle = (j%2==0) ? "CellBGR3":"CellBGRnews";
			if (!CommonTools.formatDouble(mInfo.newPlanSizeReview).equalsIgnoreCase("N/A"))
	  		totalNewPlanSize = totalNewPlanSize + mInfo.newPlanSizeReview;
	  		
%>
		<TR class="<%=rowStyle%>">
			<TD align="center"><INPUT type ="checkbox" name="ReviewCheckForUpdate"/></TD>
			<TD align="center"><%=iCount%><INPUT TYPE ="hidden" name ="moduleReviewID" value = "<%=mInfo.moduleID%>"/></TD>
			<TD><%=mInfo.name%></TD>
			<TD>&nbsp;</TD>
			<TD><%=CommonTools.formatDouble(mInfo.newPlanSizeReview)%></TD>
			<TD><%=CommonTools.formatDouble(mInfo.actualDefReview)%></TD>
			<TD>&nbsp;</TD>
		</TR>
<%
		}
	}
%>
		<TR class="TableLeft">
			<TD colspan = "3"><B> <%=languageChoose.getMessage("fi.jsp.DefectView.Total")%> </B></TD>			
			<TD><B><%=CommonTools.formatDouble(totalNorm)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalNewPlanSize)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalActual)%></B></TD>
			<TD><B></B></TD>
		</TR>		
    </TBODY>
</TABLE>
<BR>
<form name = "frmReview" method = "post" action="Fms1Servlet">
<input type="hidden" name="reqType" value="0">
<input type="hidden" name="fromPage" value="quality"/>
<%	
	if (right == 3 && !isArchive){			
%>
<% 		if (existAdd) { %>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.DefectView.Add")%>" onclick="javascript:addReviewClick();">
<% 		}	%>
<% 		if (iCount > 0) { %>
<INPUT TYPE ="hidden" name = "listUpdate"/>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.DefectView.Update")%>" onclick="javascript:updateReviewClick();">
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.DefectView.Delete")%>" onclick="javascript:deleteReviewClick();">
<%		} 		%>
<%	}	%>
</FORM>
<BR>
<BR>
<a name="DefectTest"></a>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.DefectView.Weightedprereleasetestdefectsbyprocess")%></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD><INPUT id="selectTestAll" type ="checkbox" name="selectAll" onclick="selectTestAll()"/></TD>
			<TD width ="24" align ="center">#</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.DefectView.Process")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.DefectView.Normofwdfoundbytest")%></TD>			
			<TD><%=languageChoose.getMessage("fi.jsp.DefectView.PlannedFoundByTest")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.DefectView.ActualFoundByTest")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.DefectView.TestDeviation")%>(%)</TD>
		</TR>
<%
		//only for coding now but might evolve
    	totalNorm = 0;
    	totalNewPlanSize = 0;    	
    	totalActual = 0;
    	iCount = 0;
    	
		for (int i = 0; i < 4; i++)
		{
			processObj=defectProcess[i];
  			if (!CommonTools.formatDouble(processObj.normTest).equalsIgnoreCase("N/A"))
  				totalNorm = totalNorm + processObj.normTest;
  			
  			if (!CommonTools.formatDouble(processObj.actualTest).equalsIgnoreCase("N/A"))
  				totalActual = totalActual + processObj.actualTest;

%>
		<TR class="CellBGRLongnews">
			<TD colspan="3" style="font-weight: bold">&nbsp;<%=languageChoose.getMessage(processObj.processName)%></TD>
			<TD style="font-weight: bold"><%=CommonTools.formatDouble(processObj.normTest)%></TD>
			<TD><B><%=CommonTools.formatDouble(totalPlanTest[i])%></B></TD>		
			<TD style="font-weight: bold"><%=CommonTools.formatDouble(processObj.actualTest)%></TD>
			<TD style="font-weight: bold"><%=CommonTools.formatDouble(processObj.deviationTest)%></TD>
		</TR>		
<%		
    	tmpSize = vModule[i].size();
		for (int j = 0; j < tmpSize; j++)
		{			
			WPSizeInfo mInfo = (WPSizeInfo) vModule[i].elementAt(j);
			if (mInfo.isDocument) continue;
			if (mInfo.isDefectTest == 0) {
				existAdd = true;
				continue;
			}
			iCount++;
			rowStyle = (j%2==0) ? "CellBGR3":"CellBGRnews";
			if (!CommonTools.formatDouble(mInfo.newPlanSizeTest).equalsIgnoreCase("N/A"))
	  		totalNewPlanSize = totalNewPlanSize + mInfo.newPlanSizeTest;
%>
		<TR class="<%=rowStyle%>">
			<TD align="center"><INPUT type ="checkbox" name="TestCheckForUpdate"/></TD>
			<TD align="center"><%=iCount%><INPUT TYPE ="hidden" name ="moduleTestID" value = "<%=mInfo.moduleID%>"/></TD>
			<TD><%=mInfo.name%></TD>
			<TD>&nbsp;</TD>
			<TD><%=CommonTools.formatDouble(mInfo.newPlanSizeTest)%></TD>
			<TD><%=CommonTools.formatDouble(mInfo.actualDefTest)%></TD>
			<TD>&nbsp;</TD>
		</TR>
<%
		}
	}
%>
		<TR class="TableLeft">
			<TD colspan = "3"><B> <%=languageChoose.getMessage("fi.jsp.DefectView.Total")%> </B></TD>			
			<TD><B><%=CommonTools.formatDouble(totalNorm)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalNewPlanSize)%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalActual)%></B></TD>
			<TD><B></B></TD>
		</TR>
    </TBODY>
</TABLE>
<BR>

<form name = "frmTest" method = "post" action="Fms1Servlet">
<input type="hidden" name="reqType" value="0">
<input type="hidden" name="fromPage" value="quality"/>
<%	
	if (right == 3 && !isArchive){			
%>
<% 		if (existAdd) { %>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.DefectView.Add")%>" onclick="javascript:addTestClick();">
<%		} %>
<% 		if (iCount > 0) { %>
<INPUT TYPE ="hidden" name = "listUpdate"/>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.DefectView.Update")%>" onclick="javascript:updateTestClick();">
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.DefectView.Delete")%>" onclick="javascript:deleteTestClick();">
	<%}%>
<%}%>
</form>
<BR>
<FORM name="frmQualityStrat" method="post" action = "Fms1Servlet">
<a name ="MeetingStrat"></a>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.qualityObjective.StrategyForMeetingQualityObjectives")%></CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width = "24" align = "center"> # </TD>
			<TD width="300"> <%=languageChoose.getMessage("fi.jsp.qualityObjective.Strategy")%> </TD>
			<TD width="300"> <%=languageChoose.getMessage("fi.jsp.qualityObjective.ExpectedBenefits")%> </TD>	
		</TR>
	</THEAD>
    <TBODY>
<%
		StrategyOfMeetingInfo stratInfo;
		int stratSize = vtStrat.size();
		for(int i = 0; i < stratSize; i++){
			stratInfo = (StrategyOfMeetingInfo) vtStrat.elementAt(i);
		 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
%>    
        <TR class="<%=className%>">          
            <TD width = "24" align = "center"><%=i+1%></TD>
            <TD><%=stratInfo.stratDesc == null ? "N/A": ConvertString.toHtml(stratInfo.stratDesc)%></TD>
			<TD><%=stratInfo.stratExBene == null ? "N/A": ConvertString.toHtml(stratInfo.stratExBene)%></TD>
        </TR>
<%
		}
%>            
    </TBODY>
</TABLE>
<P/>
<%if (right == 3 && !isArchive){%>
<input type="hidden" name="reqType" value="0">
<INPUT type="button" name="btnAdd" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Addnew") %>" class="BUTTON" onclick="javascript:addStrat();">
<%	if (stratSize > 0) { %>
<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Update") %>" class="BUTTON" onclick="javascript:updateStrat();">
<% 
 	} 
 }
%>
</FORM>

<br>
<FORM name="frmReviewStrat" method="post" action = "Fms1Servlet">
<a name ="ReviewStrat"></a>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.qualityObjective.ReviewStrategy")%></CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="3%" align = "center"> # </TD>
			<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.qualityObjective.ReviewItem")%> </TD>
			<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.qualityObjective.TypeOfReview")%> </TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.qualityObjective.Reviewer")%> </TD>
			<TD width="25%"> <%=languageChoose.getMessage("fi.jsp.qualityObjective.When")%> </TD>
		</TR>
	</THEAD>
    <TBODY>
<%
		ReviewStrategyInfo revInfo;
		int revSize = vtReviewStrat.size();
		for(int i = 0; i < revSize; i++){
			revInfo = (ReviewStrategyInfo) vtReviewStrat.elementAt(i);
		 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
%>    
        <TR class="<%=className%>">          
            <TD align = "center"><%=i+1%></TD>
            <TD><%=revInfo.revItem == null ? "N/A": ConvertString.toHtml(revInfo.revItem)%></TD>
			<TD>
 	           <%=ConvertString.toHtml(revInfo.revType)%>
			</TD>
			<TD><%=revInfo.revReviewer == null ? "N/A": ConvertString.toHtml(revInfo.revReviewer)%></TD>
			<TD><%=revInfo.revWhen == null ? "N/A": ConvertString.toHtml(revInfo.revWhen)%></TD>
        </TR>
<%
		}
%>            
    </TBODY>
</TABLE>
<P/>
<%if (right == 3 && !isArchive){%>
<input type="hidden" name="reqType" value="0">
<INPUT type="button" name="btnAdd" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Addnew") %>" class="BUTTON" onclick="javascript:addRevStrat();">
<% if (revSize > 0) { %>
<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Update") %>" class="BUTTON" onclick="javascript:updateRevStrat();">
<% }
 }
%>
</FORM>

<br>
<FORM name="frmTestStrat" method="post" action = "Fms1Servlet">
<a name ="TestStrat"></a>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.qualityObjective.TestStrategy")%></CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width = "24" align = "center"> # </TD>
			<TD width="265"> <%=languageChoose.getMessage("fi.jsp.qualityObjective.TestItem")%> </TD>
			<TD width="100"> Test Stage </TD>
			<TD width="110"> <%=languageChoose.getMessage("fi.jsp.qualityObjective.PersonInCharge")%> </TD>
			<TD width="140"> <%=languageChoose.getMessage("fi.jsp.qualityObjective.CompletionCriteria")%> </TD>
			<TD width="130"> <%=languageChoose.getMessage("fi.jsp.qualityObjective.EntryCriteria")%> </TD>
		</TR>
	</THEAD>
    <TBODY>
<%
		TestStrategyInfo testInfo;
		int testSize = vtTestStrat.size();
		for(int i = 0; i < testSize; i++){
			testInfo = (TestStrategyInfo) vtTestStrat.elementAt(i);
		 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
%>    
        <TR class="<%=className%>">          
            <TD width = "24" align = "center"><%=i+1%></TD>
            <TD><%=testInfo.testItem == null ? "N/A": ConvertString.toHtml(testInfo.testItem)%></TD>
			<TD>
            <%
            	switch (testInfo.testType) {
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.qualityObjective.UnitTest")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.qualityObjective.IntegrationTest")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.qualityObjective.SystemTest")%> <%break;
            	}
            %>
			</TD>
			<TD><%=testInfo.testReviewer == null ? "N/A": ConvertString.toHtml(testInfo.testReviewer)%></TD>
			<TD><%=testInfo.testComplCriteria == null ? "N/A": ConvertString.toHtml(testInfo.testComplCriteria)%></TD>
			<TD><%=testInfo.testEntryCriteria == null ? "N/A": ConvertString.toHtml(testInfo.testEntryCriteria)%></TD>
        </TR>
<%
		}
%>            
    </TBODY>
</TABLE>
<P/>
<%if (right == 3 && !isArchive){%>
<input type="hidden" name="reqType" value="0">
<INPUT type="button" name="btnAdd" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Addnew") %>" class="BUTTON" onclick="javascript:addTestStrat();">
<% if (testSize > 0) { %>
<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Update") %>" class="BUTTON" onclick="javascript:updateTestStrat();">
<% }
 } 
%>
</FORM>
<BR>
<FORM name="frmMeasurementsProgram" method="post" action = "Fms1Servlet">
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"><a name ="Measurement"><%=languageChoose.getMessage("fi.jsp.plMeasureProgList.MeasurementsProgram")%></a></CAPTION>
		<THEAD>
			<TR class="ColumnLabel">
				<TD width="3%" align = "center"> # </TD>
				<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgList.DataToBeCollected")%> </TD>
				<TD width="25%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgList.Purpose")%> </TD>
				<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgList.Responsible")%> </TD>
				<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgList.When")%> </TD>
			</TR>
		</THEAD>
		<TBODY>	
<%		
		MeasureProgInfo measInfo;
		int measSize = vMeasList.size();
		for(int i = 0; i < measSize; i++){
			measInfo = (MeasureProgInfo) vMeasList.elementAt(i);
		 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
%>		
			<TR class="<%=className%>">
				<TD align = "center"><%=i+1%></TD>
				<TD><%=measInfo.mes_data_colect == null ? "N/A": ConvertString.toHtml(measInfo.mes_data_colect)%></TD>
				<TD><%=measInfo.mes_purpose == null ? "N/A": ConvertString.toHtml(measInfo.mes_purpose)%></TD>
				<TD><%=measInfo.mes_responsible == null ? "N/A": ConvertString.toHtml(measInfo.mes_responsible)%></TD>
				<TD><%=measInfo.mes_when == null ? "N/A": ConvertString.toHtml(measInfo.mes_when)%></TD>				
			</TR>
<%
		}
%>			
		</TBODY>
</TABLE>
<P/>
<%if(right == 3 && !isArchive ){%>
	<input type="hidden" name="reqType" value="0">
	<input type="button" name="btnAdd" value="<%=languageChoose.getMessage("fi.jsp.plMeasureProgList.AddNew")%>" class = "BUTTON" onclick = "javascript:addMeas();">
<%  if (measSize > 0) { %>
	<input type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.plMeasureProgList.Update")%>" class = "BUTTON" onclick = "javascript:updateMeas();">
<% 
	} 
}
%>
</FORM>

<FORM method="POST" name="frmDPTask">
<br>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><A name="darplan">DAR plan</A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="3%" align="center">#</TD>
            <TD>Item</TD>
            <TD>Doer</TD>
            <TD>Target date</TD>            
        </TR>
        <%
        	bl = true;
        	rowStyle = "";
        	bl = true;
        	rowStyle = "";        	
        	for(int i=0;i<darVt.size();i++){
       			rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				DARPlanInfo darPlanInfo = (DARPlanInfo) darVt.get(i);        		       		
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD width="50%"><A href="darPlanDetails.jsp?vtID=<%=i%>"><%=darPlanInfo.darItem%></A></TD>
            <TD><%=darPlanInfo.doer%></TD>            
            <TD><%=CommonTools.dateFormat(darPlanInfo.planDate)%></TD>                                            
        </TR>
        <%}%>                     
    </TBODY>
</TABLE>

<%if (right == 3 && !isArchive){%>
<P><INPUT type="button" class="BUTTON" name="btnAddDarPlan" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Addnew") %>" onclick="addDarPlan();"></P>
<%}%>
<br>
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"><A name="revtest"><%=languageChoose.getMessage("fi.jsp.qualityObjective.Reviewandtestactivitieslist") %></A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD  width="24" align="center">#</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.qualityObjective.Product") %></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.qualityObjective.Plannedreviewdate") %></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.qualityObjective.Plannedtestenddate") %></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.qualityObjective.Lastplannedreleasedate")%> </TD>    
            <TD><%=languageChoose.getMessage("fi.jsp.qualityObjective.ReviewLeader") %></TD>  
            <TD><%=languageChoose.getMessage("fi.jsp.qualityObjective.Stage")%></TD>  
        </TR>
        <%
        	bl=true;
        	rowStyle="";
        	for(int i=0;i<vt.size();i++)
        	{
        		ModuleInfo modun=(ModuleInfo)vt.get(i);
        		rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl; 
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center" width="24"><%=i+1%></TD>
            <TD><A href="moduleDetails.jsp?vtID=<%=i%>"><%=((modun.isDel)?"<B>"+ConvertString.toHtml(modun.name)+"</B>":ConvertString.toHtml(modun.name))%></A></TD>
            <TD><%=CommonTools.dateFormat(modun.plannedReviewDate)%></TD>
            <TD><%if(modun.isNormal){%><%=CommonTools.dateFormat(modun.plannedTestEndDate)%><%}%></TD>
            <TD><%=CommonTools.dateFormat(modun.thePlanReleaseDate)%></TD>            
             <TD><%=ConvertString.toHtml(modun.conductor)%></TD>
            <TD><%=ConvertString.toHtml(modun.stage)%></TD>                       
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<P/>
<%if (right == 3 && !isArchive){%>
<INPUT type="button" name="btnAddRevTest" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Addnew") %>" class="BUTTON" onclick="doAction(this)"></P>
<%}%>
<BR>
<TABLE width="95%" cellspacing="1" class="Table">
    <CAPTION class="TableCaption"><A name="otheract"><%=languageChoose.getMessage("fi.jsp.qualityObjective.Otherqualityactivitylist") %></A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD align="center" width="24">#</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.qualityObjective.Activity") %></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.qualityObjective.Plannedstartdate") %></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.qualityObjective.Plannedenddate") %></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.qualityObjective.Conductor")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.qualityObjective.Note") %></TD>
        </TR>
        <%                                                                                                                             	       	
        	String activity;
        	note = "";
        	OtherActInfo oaInfo;
        	for(int i=0;i<vtOtherAct.size();i++){  
	        	oaInfo=(OtherActInfo)vtOtherAct.get(i);
        		activity=oaInfo.activity==null?"N/A":ConvertString.trunc(oaInfo.activity,50);
	        	note=oaInfo.note==null?"N/A":ConvertString.trunc(oaInfo.note,50);
	        	rowStyle=(bl)?"CellBGRnews":"CellBGR3";
	  			bl=!bl;   			
        %>
        <TR class="<%=rowStyle%>">
        	<TD align="center" width="24"><%=i+1%></TD>
            <TD><A href="otherActivityDetails.jsp?vtID=<%=i%>"><%=ConvertString.toHtml(activity)%></A></TD>
            <TD><%=CommonTools.dateFormat(oaInfo.pStartD)%></TD>
            <TD><%=CommonTools.dateFormat(oaInfo.pEndD)%></TD>
            <TD><%=CommonTools.formatString(oaInfo.conductorName)%></TD> 
            <TD><%=ConvertString.toHtml(note)%></TD>      
        </TR>
        <%}%>        
    </TBODY>
</TABLE>
<P/>
<%if (right == 3 && !isArchive){%>
<INPUT type="button" name="btnAddAct" value="<%=languageChoose.getMessage("fi.jsp.qualityObjective.Addnew") %>" class="BUTTON" onclick="doAction(this)"></P>
<%}%>
<BR>
</FORM>

<SCRIPT language="javascript">
	function selectAll(){
		var uCheck = document.getElementsByName("ReviewCheckForUpdate");

		for (i = 0; i < uCheck.length; i++) {
			uCheck[i].checked = document.getElementById("selectAll").checked;
		}
	}
	function selectTestAll(){
		var uCheck = document.getElementsByName("TestCheckForUpdate");

		for (i = 0; i < uCheck.length; i++) {
			uCheck[i].checked = document.getElementById("selectTestAll").checked;
		}
	}
	
	function addEstDef(){		
		frmEstDefect.reqType.value=<%=Constants.EST_DEFECT_PRE_ADD%>;	
		frmEstDefect.submit();
	}
	
	function updateEstDef(){	
		if (checkBatchItem()) {	
			frmEstDefect.reqType.value=<%=Constants.EST_DEFECT_PRE_UPDATE%>;
			frmEstDefect.submit();
		}
	}
	
	function deleteEstDef(){	
		if (checkBatchItem()) {
			frmEstDefect.reqType.value=<%=Constants.EST_DEFECT_DELETE%>;
			frmEstDefect.submit();
		}
	}

	function checkBatchItem() {	
		var uCheck = document.getElementsByName("checkItem");
		var idList = document.getElementsByName("estDefID");		
		
		var uList = "";
		
		var uLength = uCheck.length;
		var nChecked = 0; 
		
		for (i = 0; i < uLength; i++) {
			if (uCheck[i].checked) {
				uList = uList + idList[i].value + ",";				
				nChecked = 1;				
			}
		}
		
		if (nChecked == 0) {
			alert("Please select one item !");
			return false;
		} else {
			uList = uList.substring(0,uList.length-1);
			frmEstDefect.listUpdate.value = uList;
			return true;
		}
	}
	
	function addDPTask(){
		if(<%=(minStageSize<=0)%>)
		{
			window.alert("Please input the Stages of the project first (menu Work order/Stage & Deliv)");
		}
		frm.action="dpTaskAdd.jsp";
		frm.submit();
	}
	
	function addDarPlan(){
		frmDPTask.action="darPlanAdd.jsp";
		frmDPTask.submit();
	}

    function doAction(button){
	  	if (button.name=="btnUpdateObj") {
	  		frm.action="strategyUpdate.jsp";
	  		frm.submit();
	  		return;
	  	}
	  	if (button.name=="btnAddAct"){
	  		frmDPTask.action="otherActivityAdd.jsp";
	  		frmDPTask.submit();
	  	}
	  	if (button.name=="btnAddRevTest"){
	  		frmDPTask.action="Fms1Servlet?reqType=<%=Constants.PL_ADD_PRODUCT_FROM_QUALITY%>";
	  		frmDPTask.submit();
	  	}
  	}
  	
  	function doBack(){
		frmQualityStrat.reqType.value = <%=Constants.WORKUNIT_HOME%>;
		frmQualityStrat.submit();
	}
	
	function addStrat(){		
		frmQualityStrat.reqType.value=<%=Constants.PL_STRATEGY_FOR_MEETING_PREPARE_ADD%>;	
		frmQualityStrat.submit();
	}
	
	function updateStrat(){		
		frmQualityStrat.reqType.value=<%=Constants.PL_STRATEGY_FOR_MEETING_PREPARE_UPDATE%>;
		frmQualityStrat.submit();
	}
	
	function addRevStrat(){		
		frmReviewStrat.reqType.value=<%=Constants.PL_REVIEW_STRATEGY_PREPARE_ADD%>;
		frmReviewStrat.submit();
	}
	
	function updateRevStrat(){		
		frmReviewStrat.reqType.value=<%=Constants.PL_REVIEW_STRATEGY_PREPARE_UPDATE%>;
		frmReviewStrat.submit();
	}
	
	function addTestStrat(){		
		frmTestStrat.reqType.value=<%=Constants.PL_TEST_STRATEGY_PREPARE_ADD%>;
		frmTestStrat.submit();
	}
	
	function updateTestStrat(){		
		frmTestStrat.reqType.value=<%=Constants.PL_TEST_STRATEGY_PREPARE_UPDATE%>;
		frmTestStrat.submit();
	}
	
	function addMeas(){		
		frmMeasurementsProgram.reqType.value=<%=Constants.PL_MEASUREMENTS_PROGRAM_PREPARE_ADD%>;	
		frmMeasurementsProgram.submit();
	}
	
	function updateMeas(){		
		frmMeasurementsProgram.reqType.value=<%=Constants.PL_MEASUREMENTS_PROGRAM_PREPARE_UPDATE%>;
		frmMeasurementsProgram.submit();
	}
	
	function checkAdd(){
		if(<%=cmList.size()%> > 14){
		window.alert("<%= languageChoose.getMessage("fi.jsp.woStandardMetrics.MaximumOfCustomMetricIs15")%>");
  			return; 
  		}
  		else if (<%=(minStageSize<=0)%>)
  		{
  			window.alert("Please input the Stages of the project first (menu Work order/Stage & Deliv)");
  			return;
  		}
  		else{
	  		jumpURL('woCustomeMetricAdd.jsp');
  		}
	}
	function addDPTask(){
		if(<%=(minStageSize<=0)%>)
		{
			window.alert("Please input the Stages of the project first (menu Work order/Stage & Deliv)");
			return;
		}
		jumpURL("dpTaskAdd.jsp");
	}
	
		function addTestClick(){		
		frmTest.reqType.value=<%=Constants.DEFECT_TEST_PRODUCT_PRE_ADD%>;	
		frmTest.submit();
	}
	
	function updateTestClick(){	
		if (checkTestBatchUpdate()) {	
			frmTest.reqType.value=<%=Constants.DEFECT_TEST_PRODUCT_PRE_UPDATE%>;
			frmTest.submit();
		}
	}
	
	function deleteTestClick(){	
		if (checkTestBatchDelete()) {
			frmTest.reqType.value=<%=Constants.DEFECT_TEST_PRODUCT_DELETE%>;
			frmTest.submit();
		}
	}
	
	function checkTestBatchUpdate() {	
		var uCheck = document.getElementsByName("TestCheckForUpdate");
		var idList = document.getElementsByName("moduleTestID");		
		
		var uList = "";
		
		var uLength = uCheck.length;
		var nChecked = 0; 
		
		for (i = 0; i < uLength; i++) {
			if (uCheck[i].checked) {
				uList = uList + idList[i].value + ",";				
				nChecked = 1;				
			}
		}
		
		if (nChecked == 0) {
			alert("Please choose data to update");
			return false;
		} else {
			uList = uList.substring(0,uList.length-1);
			frmTest.listUpdate.value = uList;			
		}
		return true;
	}
	
	function checkTestBatchDelete() {	
		var uCheck = document.getElementsByName("TestCheckForUpdate");
		var idList = document.getElementsByName("moduleTestID");		
		
		var uList = "";
		var uLength = uCheck.length;
		var nChecked = 0; 
		
		for (i = 0; i < uLength; i++) {
			if (uCheck[i].checked) {
				uList = uList + idList[i].value + ",";				
				nChecked = 1;				
			}
		}
		
		if (nChecked == 0) {
			alert("Please choose data to delete");
			return false;
		} else {
			if (confirm("Do you really want to delete ?")) {
				uList = uList.substring(0,uList.length-1);
				frmTest.listUpdate.value = uList;
			} else return false;
		}
		return true;
	}
	
	function addReviewClick(){		
		frmReview.reqType.value=<%=Constants.DEFECT_REV_PRODUCT_PRE_ADD%>;	
		frmReview.submit();
	}
	
	function updateReviewClick(){	
		if (checkReviewBatchUpdate()) {	
			frmReview.reqType.value=<%=Constants.DEFECT_REV_PRODUCT_PRE_UPDATE%>;
			frmReview.submit();
		}
	}
	
	function deleteReviewClick(){	
		if (checkReviewBatchDelete()) {
			frmReview.reqType.value=<%=Constants.DEFECT_REV_PRODUCT_DELETE%>;
			frmReview.submit();
		}
	}
	
	function checkReviewBatchUpdate() {	
		var uCheck = document.getElementsByName("ReviewCheckForUpdate");
		var idList = document.getElementsByName("moduleReviewID");		
		
		var uList = "";
		
		var uLength = uCheck.length;
		var nChecked = 0; 
		
		for (i = 0; i < uLength; i++) {
			if (uCheck[i].checked) {
				uList = uList + idList[i].value + ",";				
				nChecked = 1;				
			}
		}
		
		if (nChecked == 0) {
			alert("Please choose data to update");
			return false;
		} else {
			uList = uList.substring(0,uList.length-1);
			frmReview.listUpdate.value = uList;			
		}
		return true;
	}
	
	function checkReviewBatchDelete() {	
		var uCheck = document.getElementsByName("ReviewCheckForUpdate");
		var idList = document.getElementsByName("moduleReviewID");		
		
		var uList = "";
		var uLength = uCheck.length;
		var nChecked = 0; 
		
		for (i = 0; i < uLength; i++) {
			if (uCheck[i].checked) {
				uList = uList + idList[i].value + ",";				
				nChecked = 1;				
			}
		}
		
		if (nChecked == 0) {
			alert("Please choose data to delete");
			return false;
		} else {
			if (confirm("Do you really want to delete ?")) {
				uList = uList.substring(0,uList.length-1);
				frmReview.listUpdate.value = uList;
			} else return false;
		}
		return true;
	}
	
</SCRIPT> 
</BODY>
</HTML>
