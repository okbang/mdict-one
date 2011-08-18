<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>dstrRateList.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	Vector completenessList = (Vector)session.getAttribute("CompletenessList");
	String className;
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.dstrRateUpdate.DistributionRates")%> </P>
<P class="HDR"> <%=languageChoose.getMessage("fi.jsp.dstrRateUpdate.LastUpdate")%>  <%=CommonTools.dateFormat(((CompletenessRateInfo)completenessList.elementAt(0)).lastUpdate)%></P>
<FORM name="frm" action="Fms1Servlet?reqType=<%=Constants.COMPL_RATE_UPDATE%>" method="POST">
<TABLE cellspacing="1" class="Table" width="95%">
    <COL span="1" width="15%">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.dstrRateUpdate.CompletenessRates")%>(%) </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <%for (int j = 0; j < RequirementInfo.statusList.length; j++) {%>
            <TD><%=languageChoose.getMessage(RequirementInfo.getStatusName(j))%></TD>
            <%}%>
        </TR>
		<TR class="CellBGR3">
            <%for (int j = 0; j < RequirementInfo.statusList.length; j++) {
 		       	for (int k =0;k < completenessList.size();k++) {
 		       		CompletenessRateInfo cpltInfo = (CompletenessRateInfo)completenessList.elementAt(k);
 		       		if (cpltInfo.statusID== RequirementInfo.statusList[j]){
 		     		%><TD ><INPUT size="11" maxlength="11" name="complValue" value="<%= CommonTools.updateDouble(cpltInfo.value)%>"></TD>
	            	<%	break;
 		       		}
 		       	}	
            }%>
        </TR>
    </TBODY>
</TABLE>
<P><INPUT type="button" class="BUTTON" name="ok" value="<%=languageChoose.getMessage("fi.jsp.dstrRateUpdate.OK")%> " onclick="onOK()">
<INPUT type="button" class="BUTTON" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.dstrRateUpdate.Cancel")%> " onclick="doIt(<%=Constants.DISTR_RATE_INIT%>)"></P>
</FORM>
<SCRIPT>
function onOK() {
	var nStage = <%=RequirementInfo.statusList.length%>;
	for (var i = 0; i < nStage; i++) {
		if (!mandatoryFld(frm.complValue[i],"<%=languageChoose.getMessage("fi.jsp.dstrRateUpdate.Completenessrate")%>") || !percentageFld(frm.complValue[i],"<%=languageChoose.getMessage("fi.jsp.dstrRateUpdate.Completenessrate")%>")) {
			return;
		}
	}
	for (var i = 0; i < nStage-1 ; i++) {
		if (parseFloat(frm.complValue[i].value) > parseFloat(frm.complValue[i+1].value)) {
			window.alert("<%=languageChoose.getMessage("fi.jsp.dstrRateUpdate.TheLeftValueMustBeSmallerThanTheRightOne")%>");
			frm.complValue[i].focus();
			return;
		}
	}
	frm.submit();
}
</SCRIPT>
</BODY>
</HTML>