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
	int right=Security.securiPage("Defects ",request,response);
	String error = (request.getParameter("error") == null) ? "" : request.getParameter("error");
	String warning = "";
    Vector stageVt = (Vector) session.getAttribute("stageVector");
    DreByStageInfo dreByStage = (DreByStageInfo) session.getAttribute("dreByStage");
    int incorrectLine = 0;
    boolean isCorrectPlans = true;// Check plan totals are 100
    boolean isWarning = false;
    Boolean isNewProject = (Boolean) session.getAttribute("isNewProject");               
%>
<BR>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.planDREByQcStage.PlanDefectRemovalEfficiency")%></P>
<P class="ERROR"><%=languageChoose.getMessage(error)%></P>
<FORM action="Fms1Servlet?reqType=<%=Constants.PLAN_DRE_BY_QC_STAGE_SAVE%>" name="frm" method="post">
<%
if (dreByStage.stages <= 0) {%>
<P class="ERROR"> <%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Pleasedefineatleastonestagetoa")%> </P>
<%} else if (!isNewProject.booleanValue()) {%>
<P class="ERROR"><%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Thisfunctionisdisabledforoldprojects")%></P>
<%} else {%>
<TABLE cellspacing="1" class="Table">
    <TBODY>
        <TR class="ColumnLabel">
            <TD colspan= 9> <%=languageChoose.getMessage("fi.jsp.planDREByQcStage.PlannedDefectRemovalEfficiency")%> </TD>
        </TR>
        <TR class="CellBGRnews">
            <TD class="ColumnLabel" width="160" colspan=2></TD>
            <%for(int i = 0; i < stageVt.size(); i++){%>
            <TD class="ColumnLabel" width="80"><%=((StageInfo) stageVt.get(i)).stage%></TD>
            <%}%>
            <TD class="ColumnLabel" width="40"> <%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Total")%> </TD>
        </TR>
        <%
        String qcActName;
        double sum=0;
        int i;
        double averageDistribution=0;
        for (i = 0; i < dreByStage.rows.size(); i++) {
            DreByStageInfo.Row row=(DreByStageInfo.Row) dreByStage.rows.get(i);
            qcActName = QCActivityInfo.qcNames[i];
        %>
         <TR class="CellBGR3">
            <TD class="ColumnLabel" width="100" rowspan=2><%=languageChoose.getMessage(qcActName)%></TD>
            <TD class="ColumnLabel" width="50"> <%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Norm")%> </TD>
            <%
            sum=0;
            for (int j = 0; j < row.norms.length; j++) {
                if (! Double.isNaN(row.norms[j])) {
                    sum += (row.norms[j] / dreByStage.stageCoeff[j]);
                }
            }    
            averageDistribution=(sum < 100) ? (100-sum)/row.norms.length : 0;
            
            sum=0;
            for (int j = 0; j < row.norms.length; j++) {
                if (! Double.isNaN(row.norms[j])) {
                    sum += (row.norms[j] / dreByStage.stageCoeff[j])+averageDistribution;
                }
                %><TD><%=CommonTools.formatDouble(row.norms[j] / dreByStage.stageCoeff[j]+averageDistribution)%>
                <INPUT type="hidden" id="n_<%=i%>_<%=j%>" value="<%=CommonTools.formatDouble(row.norms[j] / dreByStage.stageCoeff[j] + averageDistribution)%>">
                </TD>
            <%}%>
            <TD><%=CommonTools.updateDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Plan")%> </TD>
            <%sum=0;
            for (int j = 0; j < row.plans.length; j++){
                if (! Double.isNaN(row.plans[j])) {
                    sum+=row.plans[j];
                }
                if (right==3 && !isArchive){%><TD><INPUT id="p_<%=i%>_<%=j%>" onchange="return changePlan(<%=i%>, this)" name="plan.<%=i + "." + ((StageInfo) stageVt.get(j)).milestoneID%>" maxlength="5" size = "7" value="<%=CommonTools.updateDouble(row.plans[j])%>">
                    <%}else{%><TD><%=CommonTools.updateDouble(row.plans[j])%><%}%>
                </TD>
            <%}
            if (isCorrectPlans && (sum != 100) && (sum != 0)) {
                isCorrectPlans = false;
                incorrectLine = i;
            }
            %>
            <TD><DIV id="totalPlan<%=i%>"><%=CommonTools.updateDouble(sum)%></DIV></TD>
        </TR>
        <%}%>

         <TR class="CellBGR3">
            <TD class="ColumnLabel" width="100" rowspan=2> <%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Total1")%> </TD>
            <TD class="ColumnLabel" width="50"> <%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Norm1")%> </TD>
            <%sum=0;
            for (int j = 0; j < dreByStage.sumRow.norms.length; j++) {
                if (! Double.isNaN(dreByStage.sumRow.norms[j])) {
                    sum += (dreByStage.sumRow.norms[j] / dreByStage.stageCoeff[j]);
                }
            }    
            averageDistribution=(sum < 100) ? (100-sum)/dreByStage.sumRow.norms.length : 0;
                
			sum=0;            
            for (int j = 0; j < dreByStage.sumRow.norms.length; j++) {
                if (! Double.isNaN(dreByStage.sumRow.norms[j])) {
                    sum += (dreByStage.sumRow.norms[j] / dreByStage.stageCoeff[j]+averageDistribution);
                }
                %><TD><%=CommonTools.formatDouble(dreByStage.sumRow.norms[j] / dreByStage.stageCoeff[j]+averageDistribution)%></TD>
            <%}%>
            <TD><%=CommonTools.updateDouble(sum)%></TD>
        </TR>
        <TR class="CellBGR3">
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Plan1")%> </TD>
            <%sum=0;
            for (int j = 0; j < dreByStage.sumRow.plans.length; j++){
                if (! Double.isNaN(dreByStage.sumRow.plans[j])) {
                    sum+=dreByStage.sumRow.plans[j];
                }%>
                <TD><%=CommonTools.updateDouble(dreByStage.sumRow.plans[j])%></TD>
            <%}%>
            <TD><%=CommonTools.updateDouble(sum)%></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<%}
if ((! dreByStage.isConsistent) && (dreByStage.planRecords > 0)) {
    warning = languageChoose.getMessage("fi.jsp.planDREByQcStage.WarningProjectmilestoneshasbeenchanged");
    isWarning = true;
}
else if (! isCorrectPlans) {
    warning = languageChoose.paramText(new String[]{"fi.jsp.planDREByQcStage.Warning1__Total__of__~PARAM1_STRING~__plans__is__not__100", QCActivityInfo.qcNames[incorrectLine]});
    isWarning = true;
}
if ((right == 3 && !isArchive) && (dreByStage.stages > 0) && (isNewProject.booleanValue())){
    if (isWarning) {%>
<P class="ERROR"><%=warning%></P>
<%  }%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Update")%>" onclick="update()">
<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.planDREByQcStage.UseNorms")%> " onclick="useNorms()">
<% 
// If planned and consistent between project milestones and PLANS_QC_STAGE milestones => enable other functions
    if ((dreByStage.isConsistent) && (dreByStage.planRecords > 0)) {%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREByQcStage.PlanDefect")%>" onclick="doIt('<%=Constants.PLAN_DRE_PLAN_DEFECT%>')">
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Defectrate")%>" onclick="doIt('<%=Constants.PLAN_DRE_DEFECT_RATE%>')">
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Leakage")%>" onclick="doIt('<%=Constants.PLAN_DRE_LEAKAGE%>')">
<%  }
}%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREByQcStage.Back")%>" onclick="doIt('<%=Constants.DEFECT_VIEW%>')">
<BR><P></P>
</FORM>
<SCRIPT language="JavaScript">
var qcnum=<%=DreByStageInfo.QC_ACTIVITIES%>;
var stagenum=<%=stageVt.size()%>;
function update() {
    if (! validForm()) return;
    document.frm.submit();
}
function validForm() {
    var result = true;
    result = checkSummary("plan.");
    return result;
}
function checkSummary(prefix) {
    var test="";
    var temp;
    var effProcIndex=0;
    var bkup="";
    var size=prefix.length+2;
    var sum=0;
    for (i=0;i<document.all.length;i++){
        temp=document.all[i].name;
        if (temp && temp.indexOf(prefix)>=0){
            if (percentageFld(document.all[i],"<%= languageChoose.getMessage("fi.jsp.planDREByQcStage.Distribution")%>")){
                if(temp.indexOf(bkup.substr(0,size))<0){
                    if (sum!=100){
                        alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.planDREByQcStage.The__total__of__distribution__values__must__be__equal__to__100__currently__~PARAM1_NUMBER~")%>", sum)));
                        document.all[bkup].focus();
                        return false;
                    }
                    sum=Number(document.all[i].value);
                }
                else{
                    sum +=Number(document.all[i].value);
                }
                bkup= temp;
            }
            else
                return false;
        }
    }
    //one more time for the last row
    if (sum!=100){
        alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.planDREByQcStage.The__total__of__distribution__values__must__be__equal__to__100__currently__~PARAM1_NUMBER~")%>", sum)));
        document.all[bkup].focus();
        return false;
    }
    return true;
}
function useNorms() {
	for (i=0;i<qcnum;i++) {
		for (j=0;j<stagenum;j++) {
			if (document.all['p_'+i+'_'+j] && !isNaN(document.all['n_'+i+'_'+j].value))
				document.all['p_'+i+'_'+j].value=document.all['n_'+i+'_'+j].value;
		}
		recalc(i);
	}
}
function changePlan(i, control) {
    if (parseFloat(control.value) < 0) {
        control.focus();
        alert("<%= languageChoose.getMessage("fi.jsp.planDREByQcStage.Pleaseenterpositivenumber")%>");
        return false;
    }
	recalc(i);
}
function recalc(i) {
    var sum = 0;
    for (j = 0; j < stagenum; j++) {
		if (document.all['p_'+i+'_'+j] && !isNaN(document.all['p_'+i+'_'+j].value) &&
				document.all['p_'+i+'_'+j].value.length > 0) {
			sum += parseFloat(document.all['p_'+i+'_'+j].value);
		}
    }
    document.all["totalPlan" + i].innerText = sum;
}
</SCRIPT>
</BODY>
</HTML>
