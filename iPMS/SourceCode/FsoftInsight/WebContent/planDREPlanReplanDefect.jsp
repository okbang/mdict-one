<%@page import="com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
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
<TITLE>planDREByQcStage.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	int right=Security.securiPage("Defects ",request,response);
	Language lang=(Language)session.getAttribute("lang");
	String error = (request.getParameter("error") == null) ? "" : request.getParameter("error");
    Vector stageVt = (Vector) session.getAttribute("stageVector");
    DreByStageInfo dreByStage = (DreByStageInfo) session.getAttribute("dreByStage");
    boolean isReplan = ("1".equals(request.getParameter("isReplan")));
%>
<%!
// Format cell for stages bellow running stage
String formatCell(int i, int upperBound) {
    return ((i <= upperBound) ? "" : " class='CellBGRnews'");
}
%>
<BR>
<P class="TITLE"><%=isReplan ? languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.ReplanDefect"): languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.PlanDefectByStage")%></P>
<P class="ERROR"><%=languageChoose.getMessage(error)%></P>
<FORM action="Fms1Servlet?reqType=<%=Constants.PLAN_DRE_REPLAN_SAVE + "&isReplan=1"%>" name="frm" method="post">
<TABLE cellspacing="1" class="Table">
    <TBODY>
        <TR class="ColumnLabel">
            <TD colspan= 9> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.PlannedDefectRemovalEfficiency")%> </TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel" width="160" colspan=2></TD>
            <%for(int i = 0; i < stageVt.size(); i++){%>
            <TD class="ColumnLabel" width="80"><%=((StageInfo) stageVt.get(i)).stage%></TD>
            <%}%>
            <TD class="ColumnLabel" width="40"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Total")%> </TD>
        </TR>
        <%
        String qcActName;
        double sum=0;
        for (int i = 0; i < dreByStage.rows.size(); i++) {
            DreByStageInfo.Row row=(DreByStageInfo.Row) dreByStage.rows.get(i);
            qcActName = QCActivityInfo.qcNames[i];
            if (isReplan) {
        %>
         <TR class="CellBGR3">
            <TD class="ColumnLabel" width="100" rowspan=2><%=languageChoose.getMessage(qcActName)%></TD>
            <TD class="ColumnLabel" width="50"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Forecast")%> </TD>
            <%sum=0;
            for (int j = 0; j < dreByStage.runningStage - 1; j++) {
                if (! Double.isNaN(row.actuals[j])) {
                    sum += row.actuals[j];
                }%>
                <TD><%=CommonTools.updateDouble(row.actuals[j])%></TD>
            <%}
            double runningForecast = 0;
            for (int j = dreByStage.runningStage - 1; j < row.forecasts.length; j++) {
                if (! Double.isNaN(row.forecasts[j])) {
                    sum += row.forecasts[j];
                    runningForecast += row.forecasts[j];
                }%>
                <TD class="CellBGRnews"><%=CommonTools.formatDouble(row.forecasts[j])%>
                <INPUT type=hidden name="fc.<%=i + "." + j%>" value="<%=CommonTools.formatDouble(row.forecasts[j])%>"></TD>
            <%}%>
            <TD><%=CommonTools.formatDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Replan")%> </TD>
            <%sum=0;
            double closedReplan = 0;
            for (int j = 0; j < dreByStage.runningStage - 1; j++) {
                if (! Double.isNaN(row.replans[j])) {
                    sum += row.replans[j];
                }%>
                <TD><%=CommonTools.formatDouble(row.replans[j])%></TD>
            <%}
            closedReplan = sum;
            for (int j = dreByStage.runningStage - 1; j < row.replans.length; j++){
                if (! Double.isNaN(row.replans[j])) {
                    sum += row.replans[j];
                }
                if (right==3){%><TD class="CellBGRnews"><INPUT id="rp.<%=i + "." + j%>" name="replan.<%=i + "." + ((StageInfo) stageVt.get(j)).milestoneID%>" maxlength="11" size = "7" value="<%=CommonTools.updateDouble(row.replans[j])%>" onchange="return replan(this, <%=i%>, <%=j%>)">
                    <%}else{%><TD class="CellBGRnews"><%=CommonTools.formatDouble(row.replans[j])%><%}%>
                </TD>
            <%}%>
            <TD><DIV id="total<%=i%>"><%=CommonTools.formatDouble(sum)%></DIV>
            <INPUT type=hidden name="closedRp<%=i%>" value="<%=CommonTools.formatDouble(closedReplan)%>">
            <INPUT type=hidden name="runningFc<%=i%>" value="<%=CommonTools.formatDouble(runningForecast)%>"></TD>
        </TR>
        <%  }
            else {%>
         <TR class="CellBGR3">
            <TD class="ColumnLabel" width="100" rowspan=4><%=languageChoose.getMessage(qcActName)%></TD>
            <TD class="ColumnLabel" width="50"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Plan")%> </TD>
            <%sum=0;
            for (int j = 0; j < row.plan_wd.length; j++) {
                if (! Double.isNaN(row.plan_wd[j])) {
                    sum += Math.round(row.plan_wd[j]);
                }
                %><TD<%=formatCell(j, dreByStage.runningStage - 1)%>><%=CommonTools.formatDouble(Math.round(row.plan_wd[j]))%></TD>
            <%}%>
            <TD><%=CommonTools.updateDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Replan1")%> </TD>
            <%sum=0;
            for (int j = 0; j < row.replans.length; j++){
                if (! Double.isNaN(row.replans[j])) {
                    sum += row.replans[j];
                }%>
                <TD<%=formatCell(j, dreByStage.runningStage - 1)%>><%=CommonTools.formatDouble(row.replans[j])%></TD>
            <%}%>
            <TD><%=CommonTools.updateDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Actual")%> <BR> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Forecast1")%> </TD>
            <%sum=0;
            for (int j = 0; ((j < dreByStage.runningStage) && (j < row.actuals.length)); j++) {
                if (! Double.isNaN(row.actuals[j])) {
                    sum += row.actuals[j];
                }%>
                <TD><%=CommonTools.updateDouble(row.actuals[j])%></TD>
            <%}
            for (int j = dreByStage.runningStage; j < row.forecasts.length; j++) {
                if (! Double.isNaN(row.forecasts[j])) {
                    sum += row.forecasts[j];
                }%>
                <TD class="CellBGRnews"><%=CommonTools.updateDouble(row.forecasts[j])%></TD>
            <%}%>
            <TD><%=CommonTools.updateDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Deviation")%> </TD>
            <%sum=0;
            for (int j = 0; j < row.deviations.length; j++){
                if (j < dreByStage.runningStage) {%>
                <TD><%=CommonTools.formatDouble(row.deviations[j])%></TD>
                <%
                } else {%>
                <TD class="CellBGRnews"></TD>
            <%  }
            }%>
            <TD></TD>
        </TR>
        <%} //~ else
        }//~ for()%>

        <%if (isReplan) {%>
        <TR class="CellBGR3">
            <TD class="ColumnLabel" width="100" rowspan=2> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Total1")%> </TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Forecast2")%> </TD>
            <%sum=0;
            for (int j = 0; (j < dreByStage.runningStage - 1); j++) {
                if (! Double.isNaN(dreByStage.sumRow.actuals[j])) {
                    sum += dreByStage.sumRow.actuals[j];
                }%>
                <TD><%=CommonTools.formatDouble(dreByStage.sumRow.actuals[j])%></TD>
            <%}
            double runningForecast = 0;
            for (int j = dreByStage.runningStage - 1; j < dreByStage.sumRow.forecasts.length; j++) {
                if (! Double.isNaN(dreByStage.sumRow.forecasts[j])) {
                    sum += dreByStage.sumRow.forecasts[j];
                    runningForecast += dreByStage.sumRow.forecasts[j];
                }%>
                <TD class="CellBGRnews"><DIV id="sumRowFc<%=j%>"><%=CommonTools.formatDouble(dreByStage.sumRow.forecasts[j])%></DIV></TD>
            <%}%>
            <TD><%=CommonTools.updateDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Replan2")%> </TD>
            <%sum=0;
            double closedReplan = 0;
            for (int j = 0; j < dreByStage.runningStage - 1; j++) {
                if (! Double.isNaN(dreByStage.sumRow.replans[j])) {
                    sum += dreByStage.sumRow.replans[j];
                }%>
                <TD><%=CommonTools.formatDouble(dreByStage.sumRow.replans[j])%></TD>
            <%}
            closedReplan = sum;
            for (int j = dreByStage.runningStage - 1; j < dreByStage.sumRow.replans.length; j++){
                if (! Double.isNaN(dreByStage.sumRow.replans[j])) {
                    sum += dreByStage.sumRow.replans[j];
                }%>
                <TD class="CellBGRnews"><DIV id="sumRowRp<%=j%>"><%=CommonTools.formatDouble(dreByStage.sumRow.replans[j])%></DIV></TD>
            <%}%>
            <TD><DIV id="sumRowRp"><%=CommonTools.formatDouble(sum)%></DIV>
            <INPUT type=hidden name="sumClosedRp" value="<%=CommonTools.formatDouble(closedReplan)%>">
            <INPUT type=hidden name="sumRunningFc" value="<%=CommonTools.formatDouble(runningForecast)%>"></TD>
        </TR>
        <%} else {%>
        <TR class="CellBGR3">
            <TD class="ColumnLabel" width="100" rowspan=4> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Total2")%> </TD>
            <TD class="ColumnLabel" width="50"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Plan1")%> </TD>
            <%sum=0;
            for (int j = 0; j < dreByStage.sumRow.plan_wd.length; j++) {
                if (! Double.isNaN(dreByStage.sumRow.plan_wd[j])) {
                    sum += Math.round(dreByStage.sumRow.plan_wd[j]);
                }
                %><TD<%=formatCell(j, dreByStage.runningStage - 1)%>><%=CommonTools.formatDouble(Math.round(dreByStage.sumRow.plan_wd[j]))%></TD>
            <%}%>
            <TD><%=CommonTools.updateDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Replan3")%> </TD>
            <%sum=0;
            for (int j = 0; j < dreByStage.sumRow.replans.length; j++){
                if (! Double.isNaN(dreByStage.sumRow.replans[j])) {
                    sum += dreByStage.sumRow.replans[j];
                }%>
                <TD<%=formatCell(j, dreByStage.runningStage - 1)%>><%=CommonTools.formatDouble(dreByStage.sumRow.replans[j])%></TD>
            <%}%>
            <TD><%=CommonTools.updateDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Actual1")%> <BR> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Forecast3")%> </TD>
            <%sum=0;
            for (int j = 0; ((j < dreByStage.runningStage) && (j < dreByStage.sumRow.actuals.length)); j++) {
                if (! Double.isNaN(dreByStage.sumRow.actuals[j])) {
                    sum += dreByStage.sumRow.actuals[j];
                }%>
                <TD><%=CommonTools.formatDouble(dreByStage.sumRow.actuals[j])%></TD>
            <%}
            for (int j = dreByStage.runningStage; j < dreByStage.sumRow.forecasts.length; j++) {
                if (! Double.isNaN(dreByStage.sumRow.forecasts[j])) {
                    sum += dreByStage.sumRow.forecasts[j];
                }%>
                <TD class="CellBGRnews"><%=CommonTools.formatDouble(dreByStage.sumRow.forecasts[j])%></TD>
            <%}%>
            <TD><%=CommonTools.updateDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Deviation1")%> </TD>
            <%sum=0;
            for (int j = 0; j < dreByStage.sumRow.deviations.length; j++){
                if (j < dreByStage.runningStage) {%>
                <TD><%=CommonTools.formatDouble(dreByStage.sumRow.deviations[j])%></TD>
                <%
                } else {%>
                <TD class="CellBGRnews"></TD>
            <%  }
            }%>
            <TD></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<BR>
<%if (right == 3) {%>
<%  if (isReplan) {
        if (dreByStage.runningStage <= dreByStage.stages) {%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Update")%>" onclick="update()">
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Useforecast")%>" onclick="useForecast()">
<%      }%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Back")%>" onclick="doIt('<%=Constants.GET_PAGE+"&page=planDREPlanReplanDefect.jsp"%>')">
<%  }
    else {
        // If there are no closed stages OR all stages are closed then DISABLE replan
        if ((dreByStage.runningStage > 1) && (dreByStage.runningStage <= dreByStage.stages)) {
%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Replan")%>" onclick="doIt('<%=Constants.PLAN_DRE_PLAN_DEFECT+"&isReplan=1"%>')">
<%      }%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Back")%>" onclick="doIt('<%=Constants.GET_PAGE+"&page=planDREByQcStage.jsp"%>')">
<%  }
}%>
<BR>
<BR><P></P>
</FORM>
<%if (isReplan) {%>
<SCRIPT language="JavaScript">
var qcnum=<%=DreByStageInfo.QC_ACTIVITIES%>;
var stagenum=<%=stageVt.size()%>;
function update() {
    if (! validForm()) return;
    document.frm.submit();
}
function useForecast() {
    var total;
	for (i=0;i<qcnum;i++) {
		for (j=0;j<stagenum;j++) {
			if (document.all['rp.'+i+'.'+j] && !isNaN(document.all['fc.'+i+'.'+j].value))
				document.all['rp.'+i+'.'+j].value=document.all['fc.'+i+'.'+j].value;
		}
	}
	for (i=0;i<qcnum;i++) {
	    document.all["total" + i].innerText =
	        parseInt(document.all["closedRp" + i].value) + parseInt(document.all["runningFc" + i].value);
	}
	for (j=0;j<stagenum;j++) {
		if (document.all['sumRowRp'+j] && document.all['sumRowFc'+j])
			document.all['sumRowRp'+j].innerText = document.all['sumRowFc'+j].innerText;
	}
    document.all["sumRowRp"].innerText = 
        parseInt(document.all["sumClosedRp"].value) + parseInt(document.all["sumRunningFc"].value);
}
function replan(control, iQc, iStage) {
    if (parseFloat(control.value) < 0) {
        control.focus();
        alert("<%= languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Pleaseenterpositivenumber")%>");
        return false;
    }
    var rowTotal, colTotal;
    rowTotal = parseInt(document.all["closedRp" + iQc].value);
    for (j = <%=dreByStage.runningStage - 1%>; j < stagenum; j++) {
        if (! isNaN(parseInt(document.all["rp." + iQc + "." + j].value))) {
            rowTotal += parseInt(document.all["rp." + iQc + "." + j].value);
        }
    }
    document.all["total" + iQc].innerText = rowTotal;

    colTotal = 0;
    for (i = 0; i < qcnum; i++) {
        if (! isNaN(parseInt(document.all["rp." + i + "." + iStage].value))) {
            colTotal += parseInt(document.all["rp." + i + "." + iStage].value);
        }
    }
    document.all["sumRowRp" + iStage].innerText = colTotal;
    
    colTotal = 0;
    for (i = 0; i < qcnum; i++) {
        if (! isNaN(parseInt(document.all["total" + i].innerText))) {
            colTotal += parseInt(document.all["total" + i].innerText);
        }
    }
    document.all["sumRowRp"].innerText = colTotal;
}
function validForm() {
    var result = true;
	for (i=0;i<qcnum;i++) {
        for (j = <%=dreByStage.runningStage - 1%>; j < stagenum; j++) {
            if (! numberFld(document.all["rp." + i + "." + j], "<%=languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.replan")%>")) {
                result = false;
                break;
            }
            else if (parseFloat(document.all["rp." + i + "." + j].value) < 0) {
                document.all["rp." + i + "." + j].focus();
                alert("<%= languageChoose.getMessage("fi.jsp.planDREPlanReplanDefect.Pleaseenterpositivenumber")%>");
                result = false;
                break;
            }
        }
        if (result == false) {
            break;
        }
    }
    return result;
}
</SCRIPT>
<%}%>
</BODY>
</HTML>
