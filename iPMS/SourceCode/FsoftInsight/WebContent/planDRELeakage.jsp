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
<TITLE>planDRELeakage.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	int right=Security.securiPage("Defects ",request,response);
	String error = (request.getParameter("error") == null) ? "" : request.getParameter("error");
    Vector stageVt = (Vector) session.getAttribute("stageVector");
    DreByStageInfo dreLeakage = (DreByStageInfo) session.getAttribute("dreLeakage");
%>
<BR>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.planDRELeakage.Leakage")%></P>
<P class="ERROR"><%=error%></P>
<FORM action="Fms1Servlet?reqType=<%=Constants.PLAN_DRE_LEAKAGE%>" name="frm" method="post">
<TABLE cellspacing="1" class="Table">
	<TBODY>
		<TR class="ColumnLabel">
			<TD><%=languageChoose.getMessage("fi.jsp.planDRELeakage.Stage")%> </TD>
			<TD><%=languageChoose.getMessage("fi.jsp.planDRELeakage.PlannedPossible")%><BR><%=languageChoose.getMessage("fi.jsp.planDRELeakage.LeakageWD")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.planDRELeakage.ActualPossible")%><BR><%=languageChoose.getMessage("fi.jsp.planDRELeakage.LeakageWD1")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.planDRELeakage.PossibleLeakage")%><BR><%=languageChoose.getMessage("fi.jsp.planDRELeakage.Deviation")%></TD>
		</TR>
		<%
		// Only use first row to store Defect Rate table
		DreByStageInfo.Row row = (DreByStageInfo.Row) dreLeakage.rows.get(0);
        for (int j = 0; j < dreLeakage.runningStage - 1; j++) {%>
		<TR class="CellBGR3" style="text-align: left">
			<TD><%=((StageInfo) stageVt.get(j)).stage%></TD>
			<TD><%=CommonTools.formatDouble(Math.round(row.plans[j]))%></TD>
			<TD><%=CommonTools.formatDouble(row.actuals[j])%></TD>
			<TD><%=CommonTools.formatDouble(row.deviations[j])%></TD>
		</TR>
		<%}
		for (int j = dreLeakage.runningStage - 1; j < row.plans.length; j++) {%>
		<TR class="CellBGRnews" style="text-align: left">
			<TD><%=((StageInfo) stageVt.get(j)).stage%></TD>
			<TD><%=CommonTools.formatDouble(Math.round(row.plans[j]))%></TD>
			<TD>N/A</TD>
            <TD>N/A</TD>
		</TR>
		<%}%>
	</TBODY>
</TABLE>
<BR>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.planDRELeakage.Back")%>" onclick="doIt('<%=Constants.GET_PAGE+"&page=planDREByQcStage.jsp"%>')">
<BR><P></P>
</FORM>
</BODY>
</HTML>
