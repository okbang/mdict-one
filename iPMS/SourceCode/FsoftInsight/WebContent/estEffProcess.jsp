<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*"%>
<%
	response.setHeader("Content-Type", "text/html; charset=UTF-8");
%>
<%//the xxx2 variable are name like this to avoid naming conflics when inculding in milestone JSP
	
	LanguageChoose RDBL_languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	boolean isMilestone=Integer.toString(Constants.MILESTONE_GET_PAGE).equals(request.getParameter("reqType"));
	int right2=Security.securiPage((isMilestone)?"Project reports":"Requirements",request,response);
	boolean isReplan="1".equals(session.getAttribute("replan"));
	Vector stageList2=(Vector)session.getAttribute("stageList");
	Vector planRCRList=(Vector)session.getAttribute("planRCRList");

	Vector estEffortList =(Vector)session.getAttribute("estEffortList");
	//Vector processNameList =(Vector)session.getAttribute("processNameList");
	Vector estEffortTotalList =(Vector)session.getAttribute("estEffortTotalList");
	final String sumTotal = (String)session.getAttribute("sumTotal");
	
	boolean isUpdate=false;
	boolean oneIsClosed=false;
	StageInfo milestoneInfo=null;
	if (isMilestone){
		isUpdate=false;
		oneIsClosed=true;
		milestoneInfo=(StageInfo)session.getAttribute("stageInfo");
	}
	else{
		isUpdate=("1".equals(request.getParameter("update")) && !"1".equals(request.getAttribute("updated")));
		oneIsClosed=isReplan?true:"1".equals((String)request.getAttribute("oneClosed"));
	}
	StageInfo stinf;
	StageInfo currentStinf=null;
	int currentStage=0;

	for (int i=0;i<stageList2.size();i++){
		currentStinf=(StageInfo)stageList2.elementAt(i);
		if ((isMilestone && currentStinf.milestoneID== milestoneInfo.milestoneID) 
		|| (currentStinf.aEndD==null && currentStage==0 ||i==stageList2.size()-1)){
			currentStage=i;
			break;
		}
	}
	
	// Reset parameter - Start
	oneIsClosed = false;
	// Reset parameter - End

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
<TITLE>estEffProcess.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<BODY class="BD" onload="loadPrjMenu();<%=isUpdate ?"recalc()":""%>">

<P class="TITLE"><%=RDBL_languageChoose.getMessage("fi.jsp.estEffort.EstEffortTitle")%></P>
 <BR>
 <P class="HDR"><%=RDBL_languageChoose.getMessage("fi.jsp.estEffort.CurrentStage")%><%=currentStinf.stage%></P>
<%}%>
<BR>
<form name='frm' action ='Fms1Servlet' method='POST' >
<TABLE width="95%">
	<TR>
		<TD align="right" valign="bottom"><A href="estEffProcessExport.jsp" target="about:blank"><%=RDBL_languageChoose.getMessage("fi.jsp.estEffort.Export")%></A></TD>
	</TR>
</TABLE>
<TABLE class="Table" cellspacing="1">
<TR class="ColumnLabel">
	<TD rowspan =2 width = "20" align="center" >#</TD>
	<%if (!isUpdate){%>
	<TD rowspan =2 width = "20" align="center" ></TD>
	<%}%>
	<TD rowspan =2 > <%=RDBL_languageChoose.getMessage("fi.jsp.estEffort.ActProcess")%> </TD>
	<%=!isUpdate ?"<TD rowspan =2>" + RDBL_languageChoose.getMessage("fi.jsp.estEffort.TotalPd") + "</TD>":""%>
	<%=!isUpdate ?"<TD rowspan =2>" + RDBL_languageChoose.getMessage("fi.jsp.estEffort.TotalPercen") + "</TD>":""%>
	<%for (int i=0;i<stageList2.size();i++){
		stinf=(StageInfo)stageList2.elementAt(i);
	%><TD colspan=2><%=stinf.stage%></TD>
	<%}%>
</TR>
<TR class="ColumnLabel">
	<%for (int i=0;i<stageList2.size();i++){
		%>
		<TD width='40'> <%=RDBL_languageChoose.getMessage("fi.jsp.estEffort.No")%> </TD>
		<TD width='40'> % </TD>
	<%}%>
</TR>
<%
boolean myflag=false;
String color=null;

double sumTotalValue = Double.parseDouble(sumTotal);

double[] arrNoEffort = new double[stageList2.size()]; // to calculate Total Number of Effort
double[] arrPercenEffort = new double[stageList2.size()]; // to calculate Total Percen of Effort

for (int i=0;i<stageList2.size();i++){
	arrNoEffort[i] = 0;
	arrPercenEffort[i] = 0;
}

EstEffortTotalInfo estEffTotalInf;
for (int i=0;i<estEffortTotalList.size();i++){
	estEffTotalInf = (EstEffortTotalInfo)estEffortTotalList.elementAt(i);
	color=myflag?"CellBGRnews":"CellBGR3";
%>
	<TR class='<%=color%>'>
		<TD><%=i+1%></TD>
		<%if (!isUpdate){%>
		<TD> <A href="javascript:OnDelete(<%=i%>)"><IMG src="image/delete1.gif" title="Delete"></img></A></TD>
		<%}%>
		<TD><%=estEffTotalInf.processName%></TD>
		<%if (!isUpdate){%>
			<TD><B><%=CommonTools.updateDouble(estEffTotalInf.totalPd)%></B></TD>
			<TD><%=(sumTotalValue > 0) ? CommonTools.formatDouble(estEffTotalInf.totalPd*100/sumTotalValue) : "N/A"%></TD>
		<%}
						
		EstEffortInfo inf;
		int k=0;
		for (int j=0;j<stageList2.size();j++){
			stinf=(StageInfo)stageList2.elementAt(j);
			for (k=0;k<estEffortList.size();k++){
				inf=(EstEffortInfo)estEffortList.elementAt(k);
				if (inf.processName.equals(estEffTotalInf.processName) && inf.milestoneId==stinf.milestoneID){
					if (isUpdate && stinf.aEndD==null){%>
						<TD class="CellBGRnews"><input id ='row<%=i%>stage<%=j%>' name='plan<%=k%>' value='<%=CommonTools.updateDouble(inf.plannedValue)%>' onchange="recalc2();" maxlength = '6' size='7'></TD>
						<TD><div id='rox<%=i%>stage<%=j%>'></div></TD>
					<%}else{%>
						<TD class="CellBGRnews"><div id='row<%=i%>stage<%=j%>'><%=CommonTools.formatDouble(inf.plannedValue)%></div></TD>
						<TD><div id='rox<%=i%>stage<%=j%>'><%=CommonTools.formatDouble(inf.plannedValue*100/estEffTotalInf.totalPd)%></div></TD>
					<%}
				// Calculate Total Number of Effort - Start
				arrNoEffort[j] += inf.plannedValue;
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
			<TD></TD>
			<TD><B><%=RDBL_languageChoose.getMessage("fi.jsp.DefectView.Total")%></B></TD>
			<TD><B><%=CommonTools.formatDouble(sumTotalValue)%></B></TD>
			<TD><B><%=(sumTotalValue > 0) ? "100" : "0"%></B>%</TD>
		
			<%for (int j=0;j<stageList2.size();j++){
			%>
			<TD><%=CommonTools.formatDouble(arrNoEffort[j])%></TD>
			<TD><%=(CommonTools.formatDouble(arrNoEffort[j]*100/sumTotalValue).equalsIgnoreCase("N/A")) ? "0" : CommonTools.formatDouble(arrNoEffort[j]*100/sumTotalValue)%>%</TD>
			<%
			}%>
		</TR>
	<%}%>
</TABLE>
<P>
<INPUT type="hidden" name="reqType" value="">
<INPUT type="hidden" name="elementIdx" value="">
<%if (!isMilestone && right2 == 3 ){
	if (!isUpdate && estEffortTotalList.size() > 0){%>
	<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.estEffort.Update")%> " onclick="doIt('<%=Constants.GET_PAGE+"&page=estEffProcess.jsp&update=1"%>')">
	<%}
	if (!isUpdate){	%>
	<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.estEffort.Add")%> " onclick="addEstEffort()">
	<%
	}else{%>
<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Save")%> " onclick="valid()">
<INPUT type="button" class="BUTTON" value=" <%=RDBL_languageChoose.getMessage("fi.jsp.requirementDetailBatchPlan.Cancel")%> " onclick="doIt('<%=Constants.GET_PAGE+"&page=estEffProcess.jsp"%>')">

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
	var max = <%=estEffortTotalList.size()%>;
	var max1 = <%=estEffortList.size()%>;
	for (var i=0;i < max;i++){
		fieldTotal=document.all['totalrow'+i];
		for (var j=0;j < max1;j++){
			if (<%=isUpdate%>)
				fieldPercent=document.all['row'+i+'stage'+j];
			
			if (fieldPercent){
				if(fieldPercent.value!=null){
					if (option=="valid" ){
						if (!mandatoryFld(fieldPercent,"<%= RDBL_languageChoose.getMessage("fi.jsp.estEffort.EstEffort")%>") ||!positiveFld(fieldPercent,"<%= RDBL_languageChoose.getMessage("fi.jsp.estEffort.EstEffort")%>"))
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
		frm.reqType.value = "<%=Constants.EST_EFFORT_UPDATE%>";
		frm.submit();
	}
}

function OnDelete(elementProIdx){
	if (window.confirm("Are you sure to delete ?") != 0) {
		frm.reqType.value = "<%=Constants.EST_EFFORT_DELETE%>";
		frm.elementIdx.value = elementProIdx;
		frm.submit();
	}
}

function addEstEffort() {
	frm.reqType.value="<%=Constants.EST_EFFORT_ADD_PRE%>";
	frm.submit();
}

</SCRIPT>
<%if (!isMilestone ){%>
</BODY>
</HTML>
<%}%>
