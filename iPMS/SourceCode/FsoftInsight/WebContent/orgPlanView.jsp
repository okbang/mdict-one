<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*"%>
<%
	response.setHeader("Content-Type", "text/html; charset=UTF-8");
%>
<%
	
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	boolean isMilestone=Integer.toString(Constants.MILESTONE_GET_PAGE).equals(request.getParameter("reqType"));
	int right2=Security.securiPage((isMilestone)?"Project reports":"Requirements",request,response);
	boolean isReplan="1".equals(session.getAttribute("replan"));
	Vector durationList=(Vector)session.getAttribute("durationList");

	Vector orgPlanList =(Vector)session.getAttribute("orgPlanList");
//	Vector orgPlanTotalList =(Vector)session.getAttribute("orgPlanTotalList");
	Vector orgAssAvailableList =(Vector)session.getAttribute("orgAssAvailableList");
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
   	int nRows = orgAssAvailableList.size();
	
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

	switch(nRows){
		case 1: strHeight = "140px"; break; // fixed
		case 2: strHeight = "180px"; break; // step = 40px
		case 3: strHeight = "220px"; break;
		case 4: strHeight = "260px"; break;
		case 5: strHeight = "300px"; break;
		case 6: strHeight = "340px"; break;
		case 7: strHeight = "380px"; break;
	}

	if (nRows >8){
   		strHeight = "420px"; // fix Height
   	}
	// make scrollbar - end
	
// String title=isReplan? languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.ReplanRCRbystageprocess"):languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.PlanRCRbystageprocess");
if (!isMilestone){
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>orgPlanView.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<BODY class="BD" onload="loadPrjMenu();">

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.organization.OrgTitle")%></P>
 <BR>

<%}%>
<BR>
<form name='frm' action ='Fms1Servlet' method='POST' >
<TABLE width="95%">
	<TR>
		<TD align="right" valign="bottom"><A href="orgPlanExport.jsp" target="about:blank"><%=languageChoose.getMessage("fi.jsp.organization.Export")%></A></TD>
	</TR>
</TABLE>
<DIV id=tbl-container style="MARGIN: 0px; OVERFLOW-X: auto; OVERFLOW-Y: auto; PADDING-BOTTOM: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 0px; WIDTH: 95%; HEIGHT: <%=strHeight%> " align="left">
<TABLE class="Table" cellspacing="1">
<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.organization.OrgCaption")%></CAPTION>
<TR class="ColumnLabel">
	<TD>#</TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.organization.OrgRole")%> </TD>
	<TD width='150'> <%=languageChoose.getMessage("fi.jsp.organization.OrgName")%> </TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.organization.OrgTotalPd")%> </TD>
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

OrgAssAvailableInfo assInf;
for (int i=0;i<orgAssAvailableList.size();i++){
	assInf = (OrgAssAvailableInfo)orgAssAvailableList.elementAt(i);
	color=myflag?"CellBGRnews":"CellBGR3";
%>
	<TR class='<%=color%>'>
		<TD><%=i+1%></TD>
		<TD><%=assInf.role%></TD>
		<TD width='100'><%=assInf.devName%></TD>
		<%if (!isUpdate){%>
		<TD><B><%=(CommonTools.updateDouble(assInf.total).equalsIgnoreCase("0")) ? "" : CommonTools.updateDouble(assInf.total)%></B></TD>
		<%} else {%>
		<TD><input id ='row<%=i%>' name='totalPlan<%=i%>' value='<%=(CommonTools.updateDouble(assInf.total).equalsIgnoreCase("0")) ? "" : CommonTools.updateDouble(assInf.total)%>' maxlength = '10' size='10'></TD>
		<%}		
		OrgInfo inf;
		int k=0;
		double dConst = 2.5; // width of cell

		for (int j=0;j<durationList.size();j++){
			durationInf=(DurationInfo)durationList.elementAt(j);
			for (k=0;k<orgPlanList.size();k++){
				inf=(OrgInfo)orgPlanList.elementAt(k);
				double plan = 0;
				
				if (inf.role.equals(assInf.role) && inf.devName.equals(assInf.devName) && inf.week==durationInf.week
					&& inf.month==durationInf.month&& inf.year==durationInf.year){
					if (isUpdate){%>
						<TD class="CellBGRnews"><input id ='row<%=i%>stage<%=j%>' name='plan<%=k%>' value='<%=(CommonTools.updateDouble(inf.plannedValue).equalsIgnoreCase("0")) ? "" : CommonTools.updateDouble(inf.plannedValue)%>' maxlength = '6' size='7'></TD>
					<%}else if (!CommonTools.formatDouble(inf.plannedValue).equalsIgnoreCase("N/A")) {
							plan = inf.plannedValue; %>
						 <TD class="CellBGRnews"><div id='row<%=i%>stage<%=j%>' style='width: <%=plan*dConst/5.5%>; height: 5px; background:#FF5050' title='Effort: <%=CommonTools.formatDouble(plan)%>%'></div>
						 </TD>
						<%}
						 else {%>
							<TD class="CellBGRnews"><div id='row<%=i%>stage<%=j%>' style='width: <%=100*dConst/5.5%>; height: 5px; background:#f7f7f7'></div></TD>
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
	</TR>
<%}%>
	<!-- Show Total in case view page only (isUpdate = false) -->
	<%if (!isUpdate){%>
		<TR class="TableLeft">
			<TD></TD>
			<TD><B><%=languageChoose.getMessage("fi.jsp.organization.OrgTotalPd")%></B></TD>
			<TD></TD>
			<TD><B><%=CommonTools.formatDouble(sumTotalValue)%></B></TD>
			<%for (int j=0;j<durationList.size();j++){
			%>
			<TD><%=(CommonTools.formatDouble(arrNoEffort[j]).equalsIgnoreCase("0")) ? "":CommonTools.formatDouble(arrNoEffort[j]*5.5/100)%></TD>
			<%
			}%>
		</TR>
	<%}%>
</TABLE>
</DIV>
<P>
<INPUT type="hidden" name="reqType" value="">
<INPUT type="hidden" name="elementIdx" value="">
<%if (!isMilestone && right2 == 3 ){
	if (!isUpdate){	%>
	<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.estEffort.Update")%> " onclick="doIt('<%=Constants.GET_PAGE+"&page=orgPlanView.jsp&update=1"%>')">
	<%
	}else{%>
<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Save")%> " onclick="valid()">
<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Cancel")%> " onclick="doIt('<%=Constants.GET_PAGE+"&page=orgPlanView.jsp"%>')">

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
	var fieldTotal = new Number(0);
	var fieldPercent;
	var sumEffort = new Number();
	var max = <%=orgAssAvailableList.size()%>;
	for (var i=0;i < max;i++){
		if (<%=isUpdate%>)
			totalPlan=document.all['row'+i];
		if (totalPlan){
			if(totalPlan.value!=null){
				if (option=="valid" ){
					if (!positiveFld(totalPlan,"Total plan"))
						return false;
				}
			}
		}
		var max1 = <%=durationList.size()%>;
		for (var j=0;j < max1;j++){
			//fieldTotal = 0;
			if (<%=isUpdate%>)
				fieldPercent=document.all['row'+i+'stage'+j];
			if (fieldPercent){
				if(fieldPercent.value!=null){
					if (option=="valid" ){
						if (!positiveFld(fieldPercent,"<%= languageChoose.getMessage("fi.jsp.organization.OrgTitle")%>") || !percentageFld(fieldPercent,"<%= languageChoose.getMessage("fi.jsp.organization.OrgTitle")%>"))
							return false;
						fieldTotal += new Number(fieldPercent.value);
					}
					
				}
			}
		}
		/*
		sumEffort = fieldTotal*5.5/100;
		if (totalPlan.value < sumEffort) {
			var msg = "Sum of effort (" +  sumEffort + ") must less than or equal Total effort field (" +  totalPlan.value + ") !"
			window.alert(msg);
			totalPlan.focus();
			return false;
		}
		*/
		
	}
	return true;
}

function valid(){
	if (recalc('valid')){
		frm.reqType.value = "<%=Constants.ORGANIZATION_UPDATE%>";
		frm.submit();
	}
}


</SCRIPT>
<%if (!isMilestone ){%>
</BODY>
</HTML>
<%}%>
