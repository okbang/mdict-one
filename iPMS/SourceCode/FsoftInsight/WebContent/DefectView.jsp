<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
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
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>DefectView.jsp</TITLE>
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
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	DefectInfo defectInfo = (DefectInfo)session.getAttribute("defectInfo");
	final DefectByProcessInfo[] defectProcess = (DefectByProcessInfo[])session.getAttribute("defectProcess");
	Vector defectDevTrackingVector=(Vector) session.getAttribute("productDefDevTrackingVector");
	int right=Security.securiPage("Defects ",request,response);
	DefectDevTrackingProductInfo defectDevTrackingInfo;
    Boolean isNewProject = (Boolean) session.getAttribute("isNewProject");                       
    Vector stageVt = (Vector) session.getAttribute("stageVector");
    DreByStageInfo dreLeakage = (DreByStageInfo) session.getAttribute("dreLeakage");
    DreByStageInfo dreByStage = (DreByStageInfo) session.getAttribute("dreByStage");
    Vector moduleList = (Vector) session.getAttribute("defectModuleList");
    Hashtable Process_WorkProduct =  (Hashtable) session.getAttribute("Process_WP");
    boolean existAddRev = false;
	boolean existAddTest = false;
%>

<%
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
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.DefectView.Defects")%></P>
<TABLE class="HDR" width="560">
    <COL span="2">
    <COL span="1" width="15%">
    <TBODY>
        <TR>
            <TD></TD>
            <TD></TD>
            <TD></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Testeffectiveness")%>:</TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(defectInfo.testEffect)%></TD>
            <TD>wd/pd</TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Opendefects")%>:</TD>
            <TD style="text-align: right"><%=defectInfo.pendingDefect%></TD>
            <TD></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Defectremovalefficiency")%>:</TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(defectInfo.defectRemovalEfficiency*100)%></TD>
            <TD>%</TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Openweighteddefects")%>:</TD>
            <TD style="text-align: right"><%=defectInfo.pendingWeightedDefect%></TD>
            <TD>wd</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Reviewefficiency")%>:</TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(defectInfo.reviewEfficiency*100)%></TD>
            <TD>%</TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Totaldefects")%>:</TD>
            <TD style="text-align: right"><%=defectInfo.totalDefect%></TD>
            <TD></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Testefficiency")%>:</TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(defectInfo.testEfficiency*100)%></TD>
            <TD>%</TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Totalweighteddefects")%>:</TD>
            <TD style="text-align: right"><%=defectInfo.totalWeightedDefect%></TD>
            <TD>wd</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Estimateddefects")%>:</TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(defectInfo.estimatedDefect)%></TD>
            <TD>wd</TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Defectrate")%>:</TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(defectInfo.defectRate)%></TD>
            <TD>wd/UCP</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Reestimateddefects")%>:</TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(defectInfo.reestimatedDefect)%></TD>
            <TD>wd</TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Leakage")%>:</TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(defectInfo.leakage)%></TD>
            <TD>wd/UCP</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Toberemoveddefects")%>:</TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(defectInfo.removedDefect)%></TD>
            <TD>wd</TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Revieweffectiveness")%>:</TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(defectInfo.reviewEffect)%></TD>
            <TD>wd/pd</TD> 
            <TD><%=languageChoose.getMessage("fi.jsp.DefectView.Possibleleakage")%>:</TD>
            <TD style="text-align: right"><%=CommonTools.formatDouble(defectInfo.possibleLeakage)%></TD>
            <TD>wd</TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<%if ((isNewProject != null) && (isNewProject.booleanValue())) {
	String name1= languageChoose.getMessage("fi.jsp.DefectView.DefectFoundDeviation");
	String name2= languageChoose.getMessage("fi.jsp.DefectView.PossibleLeakageDeviation");
  	ArrayList nameSeries = new ArrayList();
	nameSeries.add(name1);
	nameSeries.add(name2);
	HashMap valueSeries = new HashMap();
	ArrayList values1 = new ArrayList();
	ArrayList values2 = new ArrayList();
	valueSeries.put(name1, values1);
	valueSeries.put(name2, values2);
	ArrayList categorySeries = new ArrayList();
	
	StageInfo sinf;
	DreByStageInfo.Row row = (DreByStageInfo.Row) dreLeakage.rows.get(0);
	for (int i = 0; i < dreByStage.runningStage - 1; i++) {
		sinf=(StageInfo)stageVt.elementAt(i);
		categorySeries.add(sinf.stage);
		values1.add(String.valueOf(CommonTools.nanToZero(dreByStage.sumRow.deviations[i])));
		values2.add(String.valueOf(CommonTools.nanToZero(row.deviations[i])));
	}
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.DefectView.DefectPlanningModel")%> </P>
<br>
<jsp:useBean id="CategoryData" class="com.fms1.chart.LinesDataSet" scope="page"  />
<jsp:useBean id="LineLabelled" class="com.fms1.chart.LinesChartLabels" scope="page"  />
<cewolf:chart id="XYChart" title='<%=languageChoose.getMessage("fi.jsp.DefectView.Deviation")%>' type="line" xaxislabel="" yaxislabel="" showlegend = "true" >
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
<%	name1= languageChoose.getMessage("fi.jsp.DefectView.ProductDefectFoundDeviation");
	name2= languageChoose.getMessage("fi.jsp.DefectView.ProductDefectRemovedDeviation");
  	nameSeries.clear();
	nameSeries.add(name1);
	nameSeries.add(name2);
	valueSeries.clear();
	values1.clear();
	values2.clear();
	valueSeries.put(name1, values1);
	valueSeries.put(name2, values2);
	categorySeries.clear();
	categorySeries.add("Pro");
	values1.add(String.valueOf(0));
	values2.add(String.valueOf(0));
	int n=2;
	for(int i=0; i<defectDevTrackingVector.size();i++){
		defectDevTrackingInfo=(DefectDevTrackingProductInfo)defectDevTrackingVector.elementAt(i);	
		if (!Double.isNaN(defectDevTrackingInfo.productDefectFoundDeviation) && !Double.isNaN(defectDevTrackingInfo.productDefectRemovedDeviation)){
			categorySeries.add(defectDevTrackingInfo.moduleName);
			values1.add(CommonTools.updateDouble(defectDevTrackingInfo.productDefectFoundDeviation));
			values2.add(CommonTools.updateDouble(defectDevTrackingInfo.productDefectRemovedDeviation));
		}
	}
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.DefectView.ProductDefectPlanningModel")%> </P>
<br>
<cewolf:chart id="XYChart2" title='<%=languageChoose.getMessage("fi.jsp.DefectView.ProductDefectDeviationTracking")%>' type="line" xaxislabel="" yaxislabel="" showlegend = "true" >
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
<cewolf:img chartid="XYChart2" renderer="/cewolf" width="600" height="300">
  <cewolf:map linkgeneratorid="xyLinkGenerator" useJFreeChartTooltipGenerator="true">
  </cewolf:map>
</cewolf:img>
<%} else {%>
<a name="DefectReview"></a>
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
	String rowStyle = "";	
	int iCount = 0;	
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
				existAddRev = true;
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
<input type="hidden" name="fromPage" value="defect">
<%	
	if (right == 3 && !isArchive){			
%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.DefectView.Add")%>" onclick="javascript:addReviewClick();">
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
			<TD><INPUT id="selectAll" type ="checkbox" name="selectTestAll" onclick="selectTestAll()"/></TD>
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
				existAddTest = true;
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
<input type="hidden" name="fromPage" value="defect">
<%	
	if (right == 3 && !isArchive){			
%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.DefectView.Add")%>" onclick="javascript:addTestClick();">
<% 		if (iCount > 0) { %>
<INPUT TYPE ="hidden" name = "listUpdate"/>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.DefectView.Update")%>" onclick="javascript:updateTestClick();">
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.DefectView.Delete")%>" onclick="javascript:deleteTestClick();">
<%		} 		%>
<%	}	%>
<%}	%>
</FORM>
<BR><P></P>
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
	function addTestClick(){
		<% if (!existAddTest) { %>		
			alert("There is no more product to add");
			return false;
		<% } %>
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
		<% if (!existAddRev) { %>		
			alert("There is no more product to add");
			return false;
		<% } %>	
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
