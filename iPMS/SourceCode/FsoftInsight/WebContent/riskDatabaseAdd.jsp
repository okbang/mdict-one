<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>riskDatabaseAdd.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onload = "loadPrjMenu();">
<%
	Vector vtRiskSourceForAdd = (Vector)session.getAttribute("vtRiskSourceForAdd");
%>
<form name="frm_riskSourceUpdate" action="Fms1Servlet#riskSourceUpdate" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.RISK_DATABASE_ADD%>">
<DIV align="left">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.RiskDatabaseAdd")%></P>
<TABLE cellspacing="1" class="Table" width="95%" >
<TBODY>
 	<TR class="ColumnLabel">
        <TD><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.SourceName")%>*</TD>
        <TD width="5%"><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.Priority")%></TD>
    </TR>
        <TR>
        	<TD>
        		<SELECT name="selectedRiskSource" class="COMBO">
						<%	
							for (int i = 0; i < vtRiskSourceForAdd.size(); i++){
					           	RiskSourceInfo riskSourceInfo = (RiskSourceInfo)vtRiskSourceForAdd.get(i);
						%>
					<OPTION value="<%=riskSourceInfo.sourceID%>"><%=riskSourceInfo.sourceName%></OPTION>
						<%}%>
				</SELECT>
        	</TD>
            <TD align="center">
            	<input type = "TEXT" name="topRisk" size="5" value = ""></input>
            </TD>
        </TR>
    </TBODY>
</TABLE>

</DIV>
</form>
<br>
<INPUT type="button" name="Add" value="<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.Add")%>" onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.Cancel")%>" onclick="javascript:on_Cancel();" class="BUTTON">
<INPUT type="hidden" name="reqType">
<script language = "javascript">	
	function on_Submit()
	{
        var form = document.frm_riskSourceUpdate;
		if (form.topRisk.value <= 0 || form.topRisk.value > 10 || isNaN(form.topRisk.value)) {
			alert("<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.OrderMustBeAPositiveNumberAndLessThan10")%>");
			form.topRisk.focus();
			return;
	    }
		form.submit();
	}
	function on_Cancel()
	{
		frm_riskSourceUpdate.reqType.value="<%=Constants.RISK_LIST_OTHER%>";
		frm_riskSourceUpdate.submit();
	}
window.name="myMainWindow";
</script> 
</BODY>
</HTML>
