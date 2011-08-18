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
<TITLE>planDREDefectRate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	int right=Security.securiPage("Defects ",request,response);
	String error = (request.getParameter("error") == null) ? "" : request.getParameter("error");
    Vector stageVt = (Vector) session.getAttribute("stageVector");
    DreByStageInfo dreDefectRate = (DreByStageInfo) session.getAttribute("dreDefectRate");
    Double normDefectRate = (Double) session.getAttribute("normDefectRate");
%>
<BR>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.planDREDefectRate.DefectRate")%></P>
<P class="ERROR"><%=languageChoose.getMessage(error)%></P>
<FORM action="Fms1Servlet?reqType=<%=Constants.PLAN_DRE_DEFECT_RATE_SAVE%>" name="frm" method="post">
<TABLE cellspacing="1" class="Table">
	<TBODY>
		<TR class="ColumnLabel">
			<TD> <%=languageChoose.getMessage("fi.jsp.planDREDefectRate.Stage")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.planDREDefectRate.Norm")%> <BR> (WD/UCP)</TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.planDREDefectRate.Plan")%> <BR> (WD/UCP)</TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.planDREDefectRate.Replan")%> <BR> (WD/UCP)</TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.planDREDefectRate.Actual")%> <BR> (WD/UCP)</TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.planDREDefectRate.Deviation")%> <BR> (%) </TD>
		</TR>
		<%
		// Only use first row to store Defect Rate table
		DreByStageInfo.Row row = (DreByStageInfo.Row) dreDefectRate.rows.get(0);
        for (int j = 0; j < dreDefectRate.runningStage - 1; j++) {%>
		<TR class="CellBGR3" style="text-align: left">
			<TD><%=((StageInfo) stageVt.get(j)).stage%></TD>
			<TD><%=CommonTools.formatDouble(normDefectRate.doubleValue())%></TD>
			<TD><%=CommonTools.formatDouble(row.plans[j])%></TD>
			<TD><%=CommonTools.formatDouble(row.replan_defect_rate[j])%></TD>
			<TD><%=CommonTools.formatDouble(row.actuals[j])%></TD>
			<TD><%=CommonTools.formatDouble(row.deviations[j])%></TD>
		</TR>
		<%}
		for (int j = dreDefectRate.runningStage - 1; j < row.replans.length; j++) {%>
		<TR class="CellBGRnews" style="text-align: left">
			<TD><%=((StageInfo) stageVt.get(j)).stage%></TD>
			<TD><%=CommonTools.formatDouble(normDefectRate.doubleValue())%></TD>
			<TD><%=CommonTools.formatDouble(row.plans[j])%></TD>
            <%if (right==3){%><TD class="CellBGRnews"><INPUT id="rp.<%=j%>" name="replan_dr.<%=((StageInfo) stageVt.get(j)).milestoneID%>" maxlength="11" size = "7" value="<%=CommonTools.updateDouble(row.replan_defect_rate[j])%>">
            <%}
              else{%><TD class="CellBGRnews"><%=CommonTools.formatDouble(row.replan_defect_rate[j])%>
            <%}%>
            </TD>
			<TD></TD>
			<TD></TD>
		</TR>
		<%}%>
	</TBODY>
</TABLE>
<BR>
<%if ((right == 3) && (dreDefectRate.runningStage <= dreDefectRate.stages)) {%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREDefectRate.Update")%>" onclick="update()">
<%}%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDREDefectRate.Back")%>" onclick="doIt('<%=Constants.GET_PAGE+"&page=planDREByQcStage.jsp"%>')">
<BR><P></P>
</FORM>
<SCRIPT language="JavaScript">
var stagenum=<%=stageVt.size()%>;
function update() {
    if (! validForm()) return;
    document.frm.submit();
}
function validForm() {
    var result = true;
    for (j = <%=dreDefectRate.runningStage - 1%>; j < stagenum; j++) {
        if (! numberFld(document.all["rp." + j], "<%=languageChoose.getMessage("fi.jsp.planDREDefectRate.replan")%>" )) {
            result = false;
            break;
        }
        else if (parseFloat(document.all["rp." + j].value) < 0) {
        alert("<%= languageChoose.getMessage("fi.jsp.planDREDefectRate.Pleaseenterpositivenumber")%>");
        result = false;
        document.all["rp." + j].focus();
        break;
        }
    }
    return result;
}
</SCRIPT>
</BODY>
</HTML>
