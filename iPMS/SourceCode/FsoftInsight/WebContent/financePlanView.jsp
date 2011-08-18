<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*"%>
<%
	response.setHeader("Content-Type", "text/html; charset=UTF-8");
%>
<%
	
	LanguageChoose RDBL_languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	boolean isMilestone=Integer.toString(Constants.MILESTONE_GET_PAGE).equals(request.getParameter("reqType"));
	int right2=Security.securiPage((isMilestone)?"Project reports":"Requirements",request,response);
	boolean isReplan="1".equals(session.getAttribute("replan"));
	Vector durationList=(Vector)session.getAttribute("durationList");

	Vector financePlanList =(Vector)session.getAttribute("financePlanList");
	Vector financePlanTotalList =(Vector)session.getAttribute("financePlanTotalList");
	final String sumTotal = (String)session.getAttribute("sumTotal");
	
	boolean isUpdate=false;
	boolean oneIsClosed=false;

	isUpdate=("1".equals(request.getParameter("update")) && !"1".equals(request.getAttribute("updated")));
	oneIsClosed=isReplan?true:"1".equals((String)request.getAttribute("oneClosed"));

	DurationInfo durationInf;
	
	// Reset parameter - Start
	oneIsClosed = false;
	// Reset parameter - End
	
	// make scrollbar - start
    int row, col;
    int nCols = durationList.size();
   	int nRows = financePlanTotalList.size();
   	
   	String strWidth= "";
   	if (nCols <=10){
   		strWidth = "100%";
   	}
   	else if ((nCols >=10) && (nCols <=20)){
   		strWidth = "180%";
   	}
   	else if ((nCols >=20) && (nCols <=35)){
   		strWidth = "250%";
   	}
   	
   	String strHeight = "";
   	if (nRows <=8){
   		strHeight = "420px"; // fix Height (optional: 320px)
   	}
   	else if (nRows >8){
   		strHeight = "420px"; // fix Height
   	}
	// make scrollbar - end
	
// String title=isReplan? RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.ReplanRCRbystageprocess"):RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.PlanRCRbystageprocess");
if (!isMilestone){
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>financePlanView.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<BODY class="BD" onload="loadPrjMenu();">

<P class="TITLE"><%=RDBL_languageChoose.getMessage("fi.jsp.finance.FinanceTitle")%></P>
 <BR>

<%}%>
<BR>
<form name='frm' action ='Fms1Servlet' method='POST' >
<TABLE width="95%">
	<TR>
		<TD align="right" valign="bottom"><A href="financePlanExport.jsp" target="about:blank"><%=RDBL_languageChoose.getMessage("fi.jsp.finance.Export")%></A></TD>
	</TR>
</TABLE>

<!-- <DIV id=tbl-container style="MARGIN: 0px; OVERFLOW-X: auto; OVERFLOW-Y: auto; PADDING-BOTTOM: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 0px; WIDTH: 95%; HEIGHT: <%=strHeight%> " align="left"> -->

<TABLE class="Table" cellspacing="1">
<TR class="ColumnLabel">
	<TD rowspan =2 width = "20" align="center" >#</TD>
	<%if (!isUpdate){%>
	<TD rowspan =2 width = "20" align="center" ></TD>
	<%}%>
	<TD rowspan =2 > <%=RDBL_languageChoose.getMessage("fi.jsp.finance.Item")%> </TD>
	
	<%=!isUpdate ?"<TD rowspan =2>" + RDBL_languageChoose.getMessage("fi.jsp.finance.Total") + "</TD>":""%>
	<%=!isUpdate ?"<TD rowspan =2>" + RDBL_languageChoose.getMessage("fi.jsp.finance.BudgetPercen") + "</TD>":""%>
	<TD align="center" colspan=<%=durationList.size()%> > <%=RDBL_languageChoose.getMessage("fi.jsp.finance.BudgetTitle")%> </TD>
	<TD rowspan =2 > <%=RDBL_languageChoose.getMessage("fi.jsp.finance.Note")%> </TD>
</TR>
<TR class="ColumnLabel">
	<%for (int i=0;i<durationList.size();i++){
	durationInf=(DurationInfo)durationList.elementAt(i);
		%>
		<TD width='40'> <%=durationInf.name%> </TD>

	<%}%>
</TR>
<%
boolean myflag=false;
String color=null;

double sumTotalValue = Double.parseDouble(sumTotal);

double[] arrNoEffort = new double[durationList.size()]; // to calculate Total Number of Effort
double[] arrPercenEffort = new double[durationList.size()]; // to calculate Total Percen of Effort

for (int i=0;i<durationList.size();i++){
	arrNoEffort[i] = 0;
	arrPercenEffort[i] = 0;
}

FinanceTotalInfo financeTotalInfo;
for (int i=0;i<financePlanTotalList.size();i++){
	financeTotalInfo = (FinanceTotalInfo)financePlanTotalList.elementAt(i);
	color=myflag?"CellBGRnews":"CellBGR3";
%>
	<TR class='<%=color%>'>
		<TD><%=i+1%></TD>
		<%if (!isUpdate){%>
		<TD> <A href="javascript:OnDelete(<%=i%>)"><IMG src="image/delete1.gif" title="Delete"></img></A></TD>
		<%}%>
		<TD><%=financeTotalInfo.financeName%></TD>
		<%if (!isUpdate){%>
			<TD><B><%=CommonTools.updateDouble(financeTotalInfo.totalBg)%></B></TD>
			<TD><%=(sumTotalValue > 0) ? CommonTools.formatDouble(financeTotalInfo.totalBg*100/sumTotalValue) : "N/A"%></TD>
		<%}
						
		FinanceInfo inf;
		int k=0;
		for (int j=0;j<durationList.size();j++){
			durationInf=(DurationInfo)durationList.elementAt(j);
			for (k=0;k<financePlanList.size();k++){
				inf=(FinanceInfo)financePlanList.elementAt(k);
				if (inf.financeName.equals(financeTotalInfo.financeName) && inf.week==durationInf.week
					&& inf.month==durationInf.month&& inf.year==durationInf.year){
					if (isUpdate){%>
						<TD class="CellBGRnews"><input id ='row<%=i%>stage<%=j%>' name='plan<%=k%>' value='<%=CommonTools.updateDouble(inf.plannedValue)%>' maxlength = '6' size='7'></TD>
					<%}else{%>
						<TD class="CellBGRnews"><div id='row<%=i%>stage<%=j%>'><%=(CommonTools.formatDouble(inf.plannedValue).equalsIgnoreCase("N/A")) ? "" : CommonTools.formatDouble(inf.plannedValue)%></div></TD>
					<%}
				// Calculate Total Number of Effort - Start
				if (!Double.isNaN(inf.plannedValue)) {
					arrNoEffort[j] += inf.plannedValue;
				}
				// Calculate Total Number of Effort - End
				
				break;
				}
			}
		}%>
		<%
		if (isUpdate){%>
			<TD class="CellBGRnews"><input id ='note<%=i%>'' name='note<%=i%>' value='<%=financeTotalInfo.note%>' maxlength = '255' size='50'></TD>
		<%}else{%>
			<TD><%=ConvertString.toHtml(financeTotalInfo.note)%></TD>
		<%}%>
		
	</TR>
<%}%>
	<!-- Show Total in case view page only (isUpdate = false) -->
	<%if (!isUpdate){%>
		<TR class="TableLeft">
			<TD></TD>
			<TD></TD>
			<TD><B><%=RDBL_languageChoose.getMessage("fi.jsp.DefectView.Total")%></B></TD>
			<TD><B><%=CommonTools.formatDouble(sumTotalValue)%></B></TD>
			<TD><B><%=(sumTotalValue > 0) ? "100" : "0"%></B>%</TD>
		
			<%for (int j=0;j<durationList.size();j++){
			%>
			<TD><%=(CommonTools.formatDouble(arrNoEffort[j]).equalsIgnoreCase("0")) ? "":CommonTools.formatDouble(arrNoEffort[j])%></TD>
			<%
			}%>
			<TD></TD>
		</TR>
	<%}%>
</TABLE>

<!-- </DIV> -->

<P>
<INPUT type="hidden" name="reqType" value="">
<INPUT type="hidden" name="elementIdx" value="">
<%if (!isMilestone && right2 == 3 ){
	if (!isUpdate && financePlanTotalList.size() > 0){%>
	<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.estEffort.Update")%> " onclick="doIt('<%=Constants.GET_PAGE+"&page=financePlanView.jsp&update=1"%>')">
	<%}
	if (!isUpdate){	%>
	<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.estEffort.Add")%> " onclick="addFinance()">
	<%
	}else{%>
<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Save")%> " onclick="valid()">
<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Cancel")%> " onclick="doIt('<%=Constants.GET_PAGE+"&page=financePlanView.jsp"%>')">
	<%}
}%>

</FORM>




<SCRIPT language="javascript">

//crazy IE 5 bug, recalc cant be called directly
function recalc2(){
	recalc();
}
function recalc(option){

	var indexMetric;
	var fieldTotal;
	var fieldPercent;
	var max = <%=financePlanTotalList.size()%>;
	for (var i=0;i < max;i++){
		fieldTotal=document.all['totalrow'+i];
		var max1 = <%=durationList.size()%>;
		for (var j=0;j < max1;j++){
			if (<%=isUpdate%>)
				fieldPercent=document.all['row'+i+'stage'+j];
			
			if (fieldPercent){
				if(fieldPercent.value!=null){
					if (option=="valid" ){
						if (!positiveFld(fieldPercent,"<%= RDBL_languageChoose.getMessage("fi.jsp.finance.FinanceTitle")%>"))
							return false;
					}
				}
				
			}
		}

	}
	return true;
}

function valid(){
	if (recalc('valid')){
		frm.reqType.value = "<%=Constants.FINANCE_UPDATE%>";
		frm.submit();
	}
}

function OnDelete(elementProIdx){
	if (window.confirm("Are you sure to delete ?") != 0) {
		frm.reqType.value = "<%=Constants.FINANCE_DELETE%>";
		frm.elementIdx.value = elementProIdx;
		frm.submit();
	}
}

function addFinance() {
	frm.reqType.value="<%=Constants.FINANCE_ADD_PRE%>";
	frm.submit();
}


</SCRIPT>
<%if (!isMilestone ){%>
</BODY>
</HTML>
<%}%>
