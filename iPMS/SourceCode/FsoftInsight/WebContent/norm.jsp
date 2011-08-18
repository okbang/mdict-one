<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>norm.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu('Summary metrics');makeScrollableTable('tablenorm',false,'auto')" class="BD"> 
<%
String strMetricID = (String)session.getAttribute("metricID");
String strParentWorkUnitID = (String)session.getAttribute("parentWorkUnitID");
Vector normInfoList = (Vector) session.getAttribute("normTable");
int inControl=0;
int calculated=0;
int i=0;
for(i =0;i<normInfoList.size();i++){
	NormInfo normInfo = (NormInfo)normInfoList.elementAt(i);
	if (!Double.isNaN(normInfo.actualValue)){
		calculated++;
		
		if ((normInfo.actualValue <=normInfo.ucl)&&(normInfo.actualValue >=normInfo.lcl))
			inControl++;
	}
}%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.norm.SummaryMetrics")%> </P>
<TABLE width="95%" class="HDR">
    <TBODY>
        <TR>
            <TD width="30%"> <%=languageChoose.getMessage("fi.jsp.norm.Indicatorsincontrollimit")%> </TD>
            <TD><%=((calculated == 0)? "N/A":CommonTools.decForm.format((double)inControl*100/(double)calculated))%> %</TD>
        </TR>
        <TR>
            <TD width="30%"> <%=languageChoose.getMessage("fi.jsp.norm.Calculatedindicators")%> </TD>
            <TD><%=CommonTools.decForm.format((double)calculated*100/(double)normInfoList.size())%> %</TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<TABLE cellspacing="1" class="Table" width="95%" id="tablenorm">
	<COLGROUP>
		<COL width="5%">
		<COL width="20.5%">
		<COL width="4.5%" align="center">
		<COL width="7%" align="right">
		<COL width="7%" align="right">
		<COL width="7%" align="right">
		<COL width="7%" align="right">
		<COL width="7%" align="right">
		<COL width="7%" align="right">
		<COL width="7%" align="right">
		<COL width="7%" align="right">
		<COL width="7%" align="right">
	<THEAD>
	<TR class="ColumnLabel">
		<TD rowspan="2" > ID </TD>
		<TD rowspan="2" > <%=languageChoose.getMessage("fi.jsp.norm.Name")%> </TD>
		<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.norm.Unit")%> </TD>
		<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.norm.ActualValue")%> </TD>
		<TD colspan="3" align="center"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Target")%> </TD>
		<TD colspan="3" align="center"> <%=languageChoose.getMessage("fi.jsp.norm.Norm")%> </TD>
		<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.norm.Deviationfromplan")%> </TD>
		<TD rowspan="2" align="center"> <%=languageChoose.getMessage("fi.jsp.norm.Deviationfromnorm")%> </TD>
	</TR>
	<TR class="ColumnLabel">
		<td align="center"><span style=""> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%> </span></td> <!-- Add by HaiMM -->
		<td align="center"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%><span style=""> </span></td>
		<td align="center"><span style=""><%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%></span></td> <!-- Add by HaiMM -->
		<td align="center"><span style=""> <%=languageChoose.getMessage("fi.jsp.norm.LCL")%> </span></td>
		<td align="center"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%><span style=""> </span></td>
		<td align="center"><span style=""> <%=languageChoose.getMessage("fi.jsp.norm.UCL")%> </span></td>
	</TR>
	
	</THEAD>
        <%for (int k =2;k<11;k++){
        	String ttlMetrics=null;
        	//no metrics from 8 to 9
        	if (k==8) k=10;
        	switch(k){
        		case 2:
        			ttlMetrics= languageChoose.getMessage("fi.jsp.norm.Requirement");
        			break;
        		case 3:
        			ttlMetrics=languageChoose.getMessage("fi.jsp.norm.Schedule");
        			break;
        		case 4:
        			ttlMetrics=languageChoose.getMessage("fi.jsp.norm.Effort");
        			break;
        		case 5:
        			ttlMetrics=languageChoose.getMessage("fi.jsp.norm.Productquality");
        			break;
        		case 6:
        			ttlMetrics=languageChoose.getMessage("fi.jsp.norm.Productivity");
        			break;
        		case 7:
        			ttlMetrics=languageChoose.getMessage("fi.jsp.norm.Productsize");
        			break;
        		case 10:
        			ttlMetrics=languageChoose.getMessage("fi.jsp.norm.Processmanagement");
        			break;
	       	}%>
	<TBODY>
		<TR class="ColumnLabel">
			<TD><%=k%></TD>
			<TD><%=ttlMetrics%></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
			<TD></TD>
		</TR>
		<%
			for(int j = 0; j < normInfoList.size(); j++) {
	        	NormInfo normInfo = (NormInfo)normInfoList.elementAt(j);
	        	// Modify by HaiMM -- Remove 2 metrics Project Management, Quality Cost (&& !normInfo.normID.equalsIgnoreCase("4.4") && !normInfo.normID.equalsIgnoreCase("4.5"))
	        	if ((normInfo.normID!=null) && normInfo.normID.startsWith(k + ".", 0)) {

		%>
		<TR class="CellBGRnews">
			<TD><%=normInfo.normID%></TD>
			<TD><%=languageChoose.getMessage(normInfo.normName)%></TD>
			<TD><%=normInfo.normUnit%></TD>
			<TD><%=CommonTools.formatNumber(normInfo.actualValue, true)%></TD>
			<TD><%=(CommonTools.formatNumber(normInfo.usl, true) == "") ? CommonTools.formatNumber(normInfo.lcl, true) : CommonTools.formatNumber(normInfo.usl, true)%></TD>	<!-- Add by HaiMM -->
			<TD><%=(CommonTools.formatNumber(normInfo.plannedValue, true) == "") ? CommonTools.formatNumber(normInfo.average, true) : CommonTools.formatNumber(normInfo.plannedValue, true)%></TD>
			<TD><%=(CommonTools.formatNumber(normInfo.lsl, true) == "") ? CommonTools.formatNumber(normInfo.ucl, true) : CommonTools.formatNumber(normInfo.lsl, true)%></TD> <!-- Add by HaiMM -->
			<TD><%=CommonTools.formatNumber(normInfo.lcl, true)%></TD>
			<TD><%=CommonTools.formatNumber(normInfo.average, true)%></TD>
			<TD><%=CommonTools.formatNumber(normInfo.ucl, true)%></TD>
			<TD><%=CommonTools.formatNumber(normInfo.planDeviation, true)%></TD>
			<TD><%=Color.colorByNorm(CommonTools.formatNumber(normInfo.normDeviation, true),normInfo.actualValue,normInfo.lcl,normInfo.ucl,normInfo.colorType)%></TD>
			
		</TR>
	<%
				}
        	}
        }
	%>
	</TBODY>
</TABLE>
<%if (strParentWorkUnitID != null) {%>
	<FORM name="frm" method="POST">
	<P align="center"><INPUT type="button" class="BUTTON" name="btnDrillUp" value=" <%=languageChoose.getMessage("fi.jsp.norm.DrillUp")%> " onclick="onDrillUp()"></P>
	</FORM>
<%}%>
<SCRIPT language="javascript">
<%if (strParentWorkUnitID != null) {%>
function onDrillUp() {
	frm.action="Fms1Servlet?reqType=<%=Constants.INDEX_DRILL%>&metricID=<%=strMetricID%>&workUnitID=<%=strParentWorkUnitID%>";
	frm.submit();
}
<%}%>
</SCRIPT>
</BODY>
</HTML>