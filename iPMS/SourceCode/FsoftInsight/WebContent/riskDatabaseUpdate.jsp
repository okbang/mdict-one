<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>riskDatabaseUpdate.jsp</TITLE>
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
	Vector vtRiskSource = (Vector)session.getAttribute("vtRiskSource");
	long selectedRiskSource = 0;
	if(session.getAttribute("riskSourceID") != null){
		selectedRiskSource = Long.parseLong((String)session.getAttribute("riskSourceID"));
	}
%>
<form name="frm_riskSourceUpdate" action="Fms1Servlet#riskSourceUpdate" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.RISK_DATABASE_UPDATE%>">
<input type = "hidden" name="projectID" value="<%=session.getAttribute("projectID")%>">
<DIV align="left">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.CommonRiskUpdate")%></P>
<TABLE cellspacing="1" class="Table" width="95%" >
<TBODY>
 	<TR class="ColumnLabel">
        <TD><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.SourceName")%>*</TD>
        <TD width="5%"><%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.Priority")%></TD>
    </TR>
    <TR class="CellBGRnews">
    	<TD>
    		<SELECT name="selectedRiskSource" class="COMBO" onchange="changeValue()">
					<%	
						for (int i = 0; i < vtRiskSource.size(); i++){
				           	RiskSourceInfo riskSourceInfo = (RiskSourceInfo)vtRiskSource.get(i);
					%>
				<OPTION value="<%=riskSourceInfo.sourceID%>" <%=((riskSourceInfo.sourceID == selectedRiskSource) ? " selected" : "")%>><%=riskSourceInfo.sourceName%></OPTION>
					<%}%>
			</SELECT>
    	</TD>
        <TD align="center">
        	<input type = "TEXT" name="topRisk" size="5" value = ""></input>
        	<input type = "hidden" name="oldTopRisk" size="5" value = ""></input>
        </TD>
    </TR>
    </TBODY>
</TABLE>

</DIV>
</form>
<br>
<INPUT type="button" name="Update" value="<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.Update")%>" onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Update" value="<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.Remove")%>" onclick="javascript:on_Remove();" class="BUTTON">
<INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.Cancel")%>" onclick="javascript:on_Cancel();" class="BUTTON">
<INPUT type="hidden" name="reqType">
<script language = "javascript">
	<%
    out.write("var arrSourceID = new Array();\n");
    out.write("var arrTopRisk = new Array();\n");
    out.write("var count = " + vtRiskSource.size() +";\n");
    int i = 0;
	for (i = 0; i < vtRiskSource.size(); i++) {
		RiskSourceInfo riskSourceInfo = (RiskSourceInfo)vtRiskSource.get(i);
        out.write("arrSourceID[" + i + "] = \"" + riskSourceInfo.sourceID + "\";\n");
        out.write("arrTopRisk[" + i + "] = \"" + riskSourceInfo.topRisk + "\";\n");
    }
	%>
	
	function on_Submit()
	{
        var form = document.frm_riskSourceUpdate;
		if (form.topRisk.value <= 0 || form.topRisk.value > 10 || isNaN(form.topRisk.value)) {
			alert("<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.OrderMustBeAPositiveNumberAndLessThan10")%>");
			form.topRisk.focus();
			return;
	    }
	    if (form.topRisk.value.indexOf(".") > 0) {
			alert("<%=languageChoose.getMessage("fi.jsp.riskSourceUpdate.OrderMustBeAIntegerNumber")%>");
			form.topRisk.focus();
			return;
	    }
		form.submit();
	}
	
	function on_Remove(){
		frm_riskSourceUpdate.reqType.value="<%=Constants.RISK_DATABASE_REMOVE%>";
		frm_riskSourceUpdate.submit();
	}
	
	function on_Cancel()
	{
		frm_riskSourceUpdate.reqType.value="<%=Constants.RISK_LIST_OTHER%>";
		frm_riskSourceUpdate.submit();
	}
	function changeValue(){
		for(var i=0; i< count; i++){
			if(frm_riskSourceUpdate.selectedRiskSource.value == arrSourceID[i]){
				if(arrTopRisk[i] == 0){
					frm_riskSourceUpdate.topRisk.value = "";
				}else{
					frm_riskSourceUpdate.topRisk.value = arrTopRisk[i];
				}
			}
			if(<%=selectedRiskSource%> == arrSourceID[i]){
				frm_riskSourceUpdate.oldTopRisk.value = arrTopRisk[i];
			}
		}
	}
	changeValue();
window.name="myMainWindow";
</script> 
</BODY>
</HTML>
