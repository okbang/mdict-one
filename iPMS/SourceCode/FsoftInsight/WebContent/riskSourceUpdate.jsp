<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>riskSourceUpdate.jsp</TITLE>
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
    int flag =0; 
	int riskID=0;
	String[] sRiskID = (String[]) request.getParameterValues("riskID");
	session.setAttribute("sRiskID",sRiskID);
	Vector vtTopCommonRisk = (Vector) session.getAttribute("vtTopCommonRisk");
	String title;
%>
<form name="frm_riskSourceUpdate" action="Fms1Servlet#riskSourceUpdate" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.RISK_SOURCE_UPDATE%>">
<input type = "hidden" name="projectID" value="<%=session.getAttribute("projectID")%>">
<DIV align="left">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.UpdateOrderRiskSource")%></P>
<TABLE cellspacing="1" class="Table" width="95%" >
<TBODY>
 	<TR class="ColumnLabel">
 		<TD width = "22" align = "center">#</TD>
        <TD><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.SourceName")%>*</TD>
        <TD><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.CategoryName")%></TD>
        <TD width="5%"><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.Priority")%></TD>
    </TR>
<%
	RiskSourceInfo riskSourceInfo = new RiskSourceInfo();
	for (int i=0; i<vtTopCommonRisk.size(); i++) {
		String className = (i%2 == 0) ?"CellBGRnews":"CellBGR3";
		riskSourceInfo = (RiskSourceInfo) vtTopCommonRisk.elementAt(i);
%>		
        <TR>
        	<TD width = "22" align = "center" class="<%=className%>"><%=i+1%></TD>
        	<TD  class="<%=className%>"><%=riskSourceInfo.sourceName%></TD>
            <TD class="<%=className%>" ><%=riskSourceInfo.categoryName%></TD>
            <TD class="<%=className%>" align="center"><input type = "TEXT" name="topRisk" size="5" value = "<%=riskSourceInfo.topRisk%>"></input></TD>
        </TR>
     <%}%>
    </TBODY>
</TABLE>

</DIV>
</form>
<br>
<INPUT type="button" name="Update" value="<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.Apply")%>" onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.Cancel")%>" onclick="javascript:on_Cancel();" class="BUTTON">
<INPUT type="hidden" name="reqType">
</BODY>
</HTML>
<script language = "javascript">
	function on_Submit()
	{
        var form = document.frm_riskSourceUpdate;
        var max = <%=vtTopCommonRisk.size()%>;
		for (var i = 0; i < max; i++) {
			if (form.topRisk[i].value <= 0 || form.topRisk[i].value > 10 || isNaN(form.topRisk[i].value)) {
				alert("<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.OrderMustBeAPositiveNumberAndLessThan10")%>");
				frm_riskSourceUpdate.topRisk[i].focus();
				return;
			}
	    }
		document.frm_riskSourceUpdate.submit();
	}
	function on_Cancel()
	{
		frm_riskSourceUpdate.reqType.value="<%=Constants.RISK_COMMON%>";
		frm_riskSourceUpdate.submit();
	}
</script> 