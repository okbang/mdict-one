<%@page import="com.fms1.tools.*"%>
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
	
	<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
	<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
	<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
	<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
	</SCRIPT>
	<TITLE>woPerformance.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onload="loadPrjMenu()">
<%
Vector perfVector = (Vector) session.getAttribute("WOPerformanceVector");
String sourcePage = (String) session.getAttribute("pageSourceQuality");
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.woPerformance.workOrderMetrics")%></P> 
<p></P>
<FORM name="frm_woPUpdate" action="Fms1Servlet" method = "post">
<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_PERFORMANCE_UPDATE %>">
<input type="hidden" name="source" value=""> 
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
    <TBODY>
        <TR class="ColumnLabel">
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformance.Metric")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformance.Unit")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformance.Committed")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformance.Recommitted")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformance.Actual")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.woPerformance.Deviation")%>(%)</TD>           
        </TR>

 	<%//plan start date
 	 MetricInfo metricInfo=(MetricInfo)perfVector.elementAt(0);%>
 		<TR class="CellBGRnews">
			<TD><%=languageChoose.getMessage(metricInfo.name)%>*</TD>
			<TD><%=metricInfo.unit%></TD>
			<TD><INPUT type="text" name="planStartDate" value="<%=CommonTools.dateUpdate(metricInfo.plannedValue)%>" maxlength = "9" size=9>
			<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPlanStartDate()'>
			</TD>
			<TD></TD>
			<TD><INPUT type="text" name="actualStartDate" value="<%=CommonTools.dateUpdate(metricInfo.actualValue)%>" maxlength = "9" size=9>
			<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showActualStartDate()'>
			</TD>
			<TD><%=Color.colorByNorm(CommonTools.formatDouble(metricInfo.deviation),metricInfo.deviation, metricInfo.lcl, metricInfo.ucl, metricInfo.colorType)%></TD>
		</TR>
	<%//plan end date 
 	metricInfo=(MetricInfo)perfVector.elementAt(1);%>
 		<TR class="CellBGR3">
			<TD><%=languageChoose.getMessage(metricInfo.name)%>*</TD>
			<TD><%=metricInfo.unit%></TD>
			<TD><INPUT type="text" name="planEndDate" value="<%=CommonTools.dateUpdate(metricInfo.plannedValue)%>" maxlength = "9" size=9>
			<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPlanEndDate()'>
			</TD>
			<TD><INPUT type="text" name="rePlanEndDate" value="<%=CommonTools.dateUpdate(metricInfo.rePlannedValue)%>" maxlength = "9"size=9 >
			<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showRePlanEndDate()'>
			</TD>
			<TD><INPUT type="text" name="actualEndDate" value="<%=CommonTools.dateUpdate(metricInfo.actualValue)%>" maxlength = "9" size=9>
			<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showActualEndDate()'>
			</TD>
			<TD><%=Color.colorByNorm(CommonTools.formatDouble(metricInfo.deviation),metricInfo.deviation, metricInfo.lcl, metricInfo.ucl, metricInfo.colorType)%></TD>
		</TR>
	<%//Duration
 	metricInfo=(MetricInfo)perfVector.elementAt(2);%>
 		<TR class="CellBGRnews">
			<TD><%=languageChoose.getMessage(metricInfo.name)%></TD>
			<TD><%=metricInfo.unit%></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.plannedValue)%></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.rePlannedValue)%></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.actualValue)%></TD>
			<TD><%=Color.colorByNorm(CommonTools.formatDouble(metricInfo.deviation),metricInfo.deviation, metricInfo.lcl, metricInfo.ucl, metricInfo.colorType)%></TD>
		</TR>
	 <%//Max Team Size
 	metricInfo=(MetricInfo)perfVector.elementAt(3);%>
 		<TR class="CellBGR3">
			<TD><%=languageChoose.getMessage(metricInfo.name)%></TD>
			<TD><%=metricInfo.unit%></TD>
			<TD><INPUT type="text" name="planTeamSize" value="<%=CommonTools.updateDouble(metricInfo.plannedValue)%>" maxlength = "4" size=9></TD>
			<TD></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.actualValue)%></TD>
			<TD><%=Color.colorByNorm(CommonTools.formatDouble(metricInfo.deviation),metricInfo.deviation, metricInfo.lcl, metricInfo.ucl, metricInfo.colorType)%></TD>
		</TR>
	<%//Huynh2 add some line code%>
	<% // Billable Effort
		metricInfo=(MetricInfo)perfVector.elementAt(8);%>
 		<TR class="CellBGRnews">
			<TD><%=languageChoose.getMessage(metricInfo.name)%>*</TD>
			<TD><%=metricInfo.unit%></TD>
			<TD><INPUT type="text" name="planBillableEffort" value="<%=CommonTools.updateDouble(metricInfo.plannedValue)%>" maxlength = "9" size=9></TD>
			<TD><INPUT type="text" name="rePlanBillableEffort" value="<%=CommonTools.updateDouble(metricInfo.rePlannedValue)%>" maxlength = "9" size=9></TD>
			<TD><INPUT type="text" name="billableActual" value="<%=CommonTools.updateDouble(metricInfo.actualValue)%>" maxlength = "9" size=9><a href="#" onclick="copyActualUsageToBillableActual();return false;"><b>Update</b></a></TD>
			<TD><%=Color.colorByNorm(CommonTools.formatDouble(metricInfo.deviation),metricInfo.deviation, metricInfo.lcl, metricInfo.ucl, metricInfo.colorType)%></TD>
		</TR>		
	<% // Calendar Effort
	metricInfo=(MetricInfo)perfVector.elementAt(9);%>
 		<TR class="CellBGR3">
			<TD><%=languageChoose.getMessage(metricInfo.name)%>*</TD>
			<TD><%=metricInfo.unit%></TD>
			<TD><INPUT type="text" name="planCalendarEffort" value="<%=CommonTools.updateDouble(metricInfo.plannedValue)%>" maxlength = "9" size=9></TD>
			<TD><INPUT type="text" name="rePlanCalendarEffort" value="<%=CommonTools.updateDouble(metricInfo.rePlannedValue)%>" maxlength = "9" size=9></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.actualValue)%></TD>
			<TD><%=Color.colorByNorm(CommonTools.formatDouble(metricInfo.deviation),metricInfo.deviation, metricInfo.lcl, metricInfo.ucl, metricInfo.colorType)%></TD>
		</TR>
	<%//Effort Usage
 	metricInfo=(MetricInfo)perfVector.elementAt(4);%>
 		<TR class="CellBGR3">
			<TD><%=languageChoose.getMessage(metricInfo.name)%>*</TD>
			<TD><%=metricInfo.unit%></TD>
			<TD><INPUT type="text" name="planEffort" value="<%=CommonTools.updateDouble(metricInfo.plannedValue)%>" maxlength = "9" size=9></TD>
			<TD><INPUT type="text" name="rePlanEffort" value="<%=CommonTools.updateDouble(metricInfo.rePlannedValue)%>" maxlength = "9" size=9></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.actualValue)%><INPUT type="hidden" name="hiddenEffortUsageActual" value="<%=CommonTools.formatDouble(metricInfo.actualValue)%>"></TD>
			<TD><%=Color.colorByNorm(CommonTools.formatDouble(metricInfo.deviation),metricInfo.deviation, metricInfo.lcl, metricInfo.ucl, metricInfo.colorType)%></TD>
		</TR>
<!-- HaiMM comment to remove Development, Management, Quality (WO Change Request)
	<%//devEffort
 	metricInfo=(MetricInfo)perfVector.elementAt(5);%>
 		<TR class="CellBGRnews">
			<TD><%= "&nbsp&nbsp&nbsp " +languageChoose.getMessage(metricInfo.name)%></TD>
			<TD><%=metricInfo.unit%></TD>
			<TD><INPUT type="text" name="devEffort" value="<%=CommonTools.updateDouble(metricInfo.plannedValue)%>" maxlength = "5" onchange="effort();"size=9 ></TD>
			<TD></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.deviation)%></TD>
		</TR>
	<%//management Effort
 	metricInfo=(MetricInfo)perfVector.elementAt(6);%>
 		<TR class="CellBGR3">
			<TD><%="&nbsp&nbsp&nbsp " +languageChoose.getMessage(metricInfo.name)%></TD>
			<TD><%=metricInfo.unit%></TD>
			<TD><INPUT type="text" name="manEffort" value="<%=CommonTools.updateDouble(metricInfo.plannedValue)%>" maxlength = "5" onchange="effort();"size=9></TD>
			<TD></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.deviation)%></TD>
		</TR>
	<%//quality Effort
 	metricInfo=(MetricInfo)perfVector.elementAt(7);%>
 		<TR class="CellBGRnews">
			<TD><%="&nbsp&nbsp&nbsp " +languageChoose.getMessage(metricInfo.name)%></TD>
			<TD><%=metricInfo.unit%></TD>
			<TD><A NAME="qualityEffort"><%=CommonTools.formatDouble(metricInfo.plannedValue)%></TD>
			<TD></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.actualValue)%></TD>
			<TD><%=CommonTools.formatDouble(metricInfo.deviation)%></TD>
		</TR>
-->
		 </TBODY>
</TABLE>

</FORM>
<br>
<input type="button" name="Update" value=" <%=languageChoose.getMessage("fi.jsp.woPerformance.OK")%> " class="BUTTON" onclick="on_Submit();">
<input type="button" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.woPerformance.Cancel")%> " class="BUTTON" onclick="doCancel();" >
</BODY>
</HTML>
<script language = "javascript">

	function showPlanStartDate(){
		if(frm_woPUpdate.planStartDate.value == null || frm_woPUpdate.planStartDate.value ==""){
			frm_woPUpdate.planStartDate.value = "01-01-08";
		}
		showCalendar(frm_woPUpdate.planStartDate, frm_woPUpdate.planStartDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showActualStartDate(){
		if(frm_woPUpdate.actualStartDate.value == null || frm_woPUpdate.actualStartDate.value ==""){
			frm_woPUpdate.actualStartDate.value = "01-01-08";
		}
		showCalendar(frm_woPUpdate.actualStartDate, frm_woPUpdate.actualStartDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showPlanEndDate(){
		if(frm_woPUpdate.planEndDate.value == null || frm_woPUpdate.planEndDate.value ==""){
			frm_woPUpdate.planEndDate.value = "01-01-08";
		}
		showCalendar(frm_woPUpdate.planEndDate, frm_woPUpdate.planEndDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showRePlanEndDate(){
		if(frm_woPUpdate.rePlanEndDate.value == null || frm_woPUpdate.rePlanEndDate.value ==""){
			frm_woPUpdate.rePlanEndDate.value = "01-01-08";
		}
		showCalendar(frm_woPUpdate.rePlanEndDate, frm_woPUpdate.rePlanEndDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showActualEndDate(){
		if(frm_woPUpdate.actualEndDate.value == null || frm_woPUpdate.actualEndDate.value ==""){
			frm_woPUpdate.actualEndDate.value = "01-01-08";
		}
		showCalendar(frm_woPUpdate.actualEndDate, frm_woPUpdate.actualEndDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
<% 
		// get billable effort
		metricInfo=(MetricInfo)perfVector.elementAt(8);
		if(CommonTools.updateDouble(metricInfo.actualValue)!=""){
%>
	var isBillableActualMandatory = true;
<%
		}
		else{
%>
	var isBillableActualMandatory = false;	
<%		
		}
%>

	var sourcePage = <%=sourcePage%>;

	function doCancel() {
		if (sourcePage == 1) {
			jumpURL('qualityObjective.jsp');
		}
		else {
			jumpURL('woPerformanceView.jsp');
		}
	}

	function copyActualUsageToBillableActual(){
		isBillableActualMandatory = true;
		document.frm_woPUpdate.billableActual.value=document.frm_woPUpdate.hiddenEffortUsageActual.value;
	}
	
	
	function on_Submit()
	{	
		if (mandatoryDateFld(frm_woPUpdate.planStartDate,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Plannedstartdate")%>"))
		if (beforeTodayFld(frm_woPUpdate.actualStartDate,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Actualstartdate")%>"))
		if (mandatoryDateFld(frm_woPUpdate.planEndDate,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Plannedenddate")%>"))
		if (dateFld(frm_woPUpdate.rePlanEndDate,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Replannedenddate")%>"))
		if (beforeTodayFld(frm_woPUpdate.actualEndDate,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Actualenddate")%>"))
		if (integerFld(frm_woPUpdate.planTeamSize,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Plannedteamsize")%>"))
		if (positiveFld(frm_woPUpdate.planTeamSize,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Plannedteamsize")%>"))
		if (mandatoryFld(frm_woPUpdate.planBillableEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Billableeffort")%>"))
		if (mandatoryFld(frm_woPUpdate.planEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Effortusage")%>"))
		if (mandatoryFld(frm_woPUpdate.planCalendarEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Calendareffort")%>"))
		if (positiveFld(frm_woPUpdate.planBillableEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Billableeffort")%>"))
		if (positiveFld(frm_woPUpdate.rePlanBillableEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Billableeffort")%>"))
		if (positiveFld(frm_woPUpdate.planEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Effortusage")%>"))
		if (positiveFld(frm_woPUpdate.rePlanEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Effortusage")%>"))
		if (positiveFld(frm_woPUpdate.planCalendarEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Calendareffort")%>"))
		if (positiveFld(frm_woPUpdate.rePlanCalendarEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Calendareffort")%>"))
//		if (percentageFld(frm_woPUpdate.devEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Developmenteffort")%>"))
//		if (percentageFld(frm_woPUpdate.manEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Managementeffort")%>"))
		if (allOrNot()){
			if(isBillableActualMandatory){
				if(!mandatoryFld(frm_woPUpdate.billableActual,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Billableeffort")%>"))
					return;
				if(!positiveFld(frm_woPUpdate.billableActual,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Billableeffort")%>"))
					return;

			}
			if (compareDate( frm_woPUpdate.planStartDate.value, frm_woPUpdate.planEndDate.value)<=0){
				alert("<%= languageChoose.getMessage("fi.jsp.woPerformance.PlanStartDateMustBeBeforePlanEndDate")%>");				  		
				frm_woPUpdate.planStartDate.focus();
				return;
			}
			if (trim(frm_woPUpdate.rePlanEndDate.value).length>0 && compareDate( frm_woPUpdate.planStartDate.value, frm_woPUpdate.rePlanEndDate.value)<=0){
				alert("<%= languageChoose.getMessage("fi.jsp.woPerformance.PlanStartDateMustBeBeforeRePlanEndDate")%>");				  		
				frm_woPUpdate.rePlanEndDate.focus();
				return;
			}
			if (trim(frm_woPUpdate.actualEndDate.value).length>0 && compareDate( frm_woPUpdate.actualStartDate.value, frm_woPUpdate.actualEndDate.value)<=0){
				alert("<%= languageChoose.getMessage("fi.jsp.woPerformance.ActualStartDateMustBeBeforeActualEndDate")%>");				  		
				frm_woPUpdate.actualStartDate.focus();
				return;
			}
//			if (parseFloat(frm_woPUpdate.manEffort.value)+parseFloat(frm_woPUpdate.devEffort.value)>100){
//				alert("<%= languageChoose.getMessage("fi.jsp.woPerformance.TheSumOfManagementAndDevelopmentEffortCantBeAbove100")%>");				  		
//				frm_woPUpdate.manEffort.focus();
//				return;
//			}

			if (sourcePage == 1) {
				frm_woPUpdate.source.value = "1";
			}
			
			frm_woPUpdate.submit();
		}

	}

//	function effort(){
//		if (
//		(trim(frm_woPUpdate.devEffort.value)!="")
//		&& (trim(frm_woPUpdate.manEffort.value)!="") 
//		&& (percentageFld(frm_woPUpdate.devEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Developmenteffort")%>"))	
//		&& (percentageFld(frm_woPUpdate.manEffort,"<%= languageChoose.getMessage("fi.jsp.woPerformance.Managementeffort")%>"))
//		) 
//			document.all["qualityEffort"].innerText = 100.0 - parseFloat(frm_woPUpdate.manEffort.value)-parseFloat(frm_woPUpdate.devEffort.value);
//		return;
//	}
	function allOrNot(){
//		if (
//		(trim(frm_woPUpdate.devEffort.value)!="") !=
//		(trim(frm_woPUpdate.manEffort.value)!="") ) {
//			window.alert("<%= languageChoose.getMessage("fi.jsp.woPerformance.DevelopmentAndManagementEffortMustBeBothDefinedOrBothBlank")%>");				  		
//			frm_woPUpdate.devEffort.focus();
//			return false;
//		}
		return true;
	}
</script>
