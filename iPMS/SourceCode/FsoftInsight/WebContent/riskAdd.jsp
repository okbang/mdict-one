<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>riskAdd.jsp</TITLE>
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
    String[] s = request.getParameterValues("riskID");
	for (int i=0; i<s.length; i++)
		flag=CommonTools.parseInt(s[0]);
	String title;
%>
<form name="frm_riskAdd" action="Fms1Servlet#tailoring" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.RISK_ADD%>">
<input type = "hidden" name="projectID" value="<%=session.getAttribute("projectID")%>">
<DIV align="left">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.riskAdd.AddCommonRisk")%></P> 
<TABLE cellspacing="1" class="Table" width="95%" >
<TBODY>
 	<TR>
        <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAdd.SourceName")%>*</TD>
        <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAdd.CategoryName")%></TD>
        <TD class="ColumnLabel" width="25%"><%=languageChoose.getMessage("fi.jsp.riskAdd.Description")%></TD>
    </TR>
<%
	RiskSourceInfo riskSourceInfo = new RiskSourceInfo();
	for (int i=0; i<s.length; i++) {
		String className = (i%2 == 0) ?"CellBGRnews":"CellBGR3";
		flag=CommonTools.parseInt(s[i]);
		riskSourceInfo = (RiskSourceInfo) vtTopCommonRisk.elementAt(Integer.parseInt(sRiskID[i]));
%>		
        <TR>
        	<TD  class="<%=className%>"><%=riskSourceInfo.sourceName%></TD>
            <TD class="<%=className%>" ><%=riskSourceInfo.categoryName%></TD>
            <TD class="<%=className%>" ><TEXTAREA name="condition" rows="5" cols="30" ></TEXTAREA></TD>
        </TR>
     <%}%>   
    </TBODY>
</TABLE>

</DIV>
</form>
<br>
<INPUT type="button" name="Add" value="<%=languageChoose.getMessage("fi.jsp.riskAdd.Add")%>" onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.riskAdd.Cancel")%>" onclick="javascript:on_Cancel();" class="BUTTON">
<INPUT type="hidden" name="reqType">
</BODY>
</HTML>
<script language = "javascript">
function on_Submit()
{
    var form = document.frm_riskAdd;
    if (<%=s.length%> > 1) {
		for (var i = 0; i < <%=s.length%>; i++) {
			if (form.condition[i].value.length >200) {
				alert("<%=languageChoose.getMessage("fi.jsp.riskAdd.TheLengthOfDescriptionIsLessThan200")%>");
				frm_riskAdd.condition[i].focus();
				return;
			}
			if (form.condition[i].value.length == 0) {
				alert("<%=languageChoose.getMessage("fi.jsp.riskAdd.TheTextForDescriptionCannotBeBlank")%>");
				frm_riskAdd.condition[i].focus();
				return;
			}
	    }
	} else {
		if (form.condition.value.length>200) {
			alert("<%= languageChoose.getMessage("fi.jsp.riskAdd.TheLengthOfDescriptionIsLessThan200")%>");
			frm_riskAdd.condition.focus();
			return;
		}
		if (form.condition.value.length == 0) {
			alert("<%=languageChoose.getMessage("fi.jsp.riskAdd.TheTextForDescriptionCannotBeBlank")%>");
			frm_riskAdd.condition.focus();
			return;
		}
	}    
	document.frm_riskAdd.submit();
}

function on_Cancel()
{
	frm_riskAdd.reqType.value="<%=Constants.RISK_COMMON%>";
	frm_riskAdd.submit();
}	
</script> 