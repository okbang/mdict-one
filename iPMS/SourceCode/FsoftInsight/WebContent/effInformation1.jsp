<%@page import="com.fms1.infoclass.*, com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>effInformation1.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right = Security.securiPage("Effort",request,response);
	EffortInfo info=(EffortInfo)request.getAttribute("effortHeaderInfo");
%>
<BODY onload="loadPrjMenu()" class="BD">
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.effInformation1.EffortInformation")%></p>
<p><%=languageChoose.getMessage("fi.jsp.effInformation1.Unlessspecifiedtheunitforeffortmetricsispersonday")%></p>
<FORM name=frm method="POST">
<TABLE width="95%" class="HDR">
    <TBODY>
        <TR>
            <TD class="CellBGRnews" width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Budgetedeffort")%></TD>
            <TD class="CellBGRnews" width="15%" align="right"><%=CommonTools.formatDouble(info.budgetedEffort)%></TD>
            <TD width="5%" ></TD>
            <TD class="CellBGRnews" width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Managementeffort")%></TD>
            <TD class="CellBGRnews" width="15%" align="right"><%=CommonTools.formatDouble(info.perManagementEffort)%></TD>
        </TR>

        <TR>
            <TD class="CellBGRnews" width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Actualeffort")%></TD>
            <TD class="CellBGRnews" width="15%" align="right"><%=CommonTools.formatDouble(info.actualEffort)%></TD>
            <TD width="5%" ></TD>
            <TD class="CellBGRnews" width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Developementeffort")%></TD>
            <TD class="CellBGRnews" width="15%" align="right"><%=CommonTools.formatDouble(info.perDevelopementEffort)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRnews" width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Calendareffort")%></TD>
            <TD class="CellBGRnews" width="15%" align="right"><%=CommonTools.formatDouble(info.calendarEffort)%></TD>
            <TD width="5%" ></TD>
            <TD class="CellBGRnews" width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Qualityeffort")%></TD>
            <TD class="CellBGRnews" width="15%" align="right"><%=CommonTools.formatDouble(info.perQualityEffort)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRnews" width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Effortefficiency")%></TD>
            <TD class="CellBGRnews" width="15%" align="right"><%=CommonTools.formatDouble(info.effortEfficiency)%></TD>
            <TD width="5%" ></TD>
            <TD class="CellBGRnews" width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Correctioneffort")%></TD>
            <TD class="CellBGRnews" width="15%" align="right"><%=CommonTools.formatDouble(info.perCorrectionEffort)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRnews" width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Efforteffectiveness")%></TD>
            <TD class="CellBGRnews" width="15%" align="right"><%=CommonTools.formatDouble(info.effortUseage)%></TD>
            <TD width="5%" ></TD>
            <TD class="CellBGRnews" width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Translationeffort")%></TD>
            <TD class="CellBGRnews" width="15%" align="right"><%=CommonTools.formatDouble(info.perTranslationEffort)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRnews" width="30%"><%=languageChoose.getMessage("fi.jsp.effInformation1.Effortdeviation")%></TD>
            <TD align="right" class="CellBGRnews" width="15%"><%=CommonTools.formatDouble(info.effortDeviation)%></TD>
            <TD width="5%" ></TD>
            <TD class="CellBGRnews" width="30%"></TD>
            <TD align="right" class="CellBGRnews" width="15%"></TD>
        </TR>
    </TBODY>
</TABLE>
<p></P>
</FORM>
</BODY>
</HTML>
