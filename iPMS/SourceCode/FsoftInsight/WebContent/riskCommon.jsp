<%@page import="java.util.Vector,com.fms1.infoclass.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.common.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<TITLE>riskCommon.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad="loadPrjMenu();" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.riskCommon.Commonrisks")%> </P>

<%
	int right = Security.securiPage("Risks",request,response);
	Vector vtTopCommonRisk = (Vector) session.getAttribute("vtTopCommonRisk");
	Vector riskList = (Vector)session.getAttribute("riskList");
	UserInfo userInfo = (UserInfo)session.getAttribute("UserLoginInfo");
%>

<FORM name="frmRiskAdd" action="Fms1Servlet#riskAdd" method="POST">
<input type = "hidden" name="reqType" value="<%=Constants.RISK_ADD_PREP%>">
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
        	<TD width = "22"></TD>
            <TD width = "22" align = "center"># </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.riskCommon.SourceName")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.riskCommon.CategoryName")%> </TD>
        </TR>
	<%
	String sentence;
	RiskSourceInfo info;
	boolean style = false;
	boolean b = false;
	for(int i = 0 ;i < vtTopCommonRisk.size(); i++){
 		info = (RiskSourceInfo) vtTopCommonRisk.elementAt(i);
 		b = false;
 			style= !style;
 			for(int j = 0 ;j < riskList.size(); j++){
 				RiskInfo risk = (RiskInfo)riskList.elementAt(j);
 				if(risk.sourceID == info.sourceID){
 					b=true;
 					break;
 				}
 			}
	%>
	<TR class="<%=((style)?"CellBGRnews":"CellBGR3")%>">
		<TD><input <%= b == true ? "disabled" : "" %> type="checkbox" name="riskID" value="<%=i%>"></TD>
		<TD width = "22" align = "center"><%=i+1%></TD>
		<TD><%=info.sourceName%></TD>
		<TD><%=info.categoryName%></TD>
	</TR>
	<%}%>
</TABLE>
<P>
<%if(right==3){%>
	<INPUT type='button' class='BUTTON' value=' <%=languageChoose.getMessage("fi.jsp.riskCommon.Apply")%> ' onclick='selectMe()'>
	<INPUT type='button' class='BUTTON' value=' <%=languageChoose.getMessage("fi.jsp.riskCommon.Addnew")%> ' onclick='onAddNew()'>
	<%if(userInfo.getUserRoleLogin()==2){%>
	<INPUT type='submit' class='BUTTON' value='<%=languageChoose.getMessage("fi.jsp.riskCommon.SetPriority")%>' onclick="onUpdate()">
	<%}%>
<%}%>
<INPUT type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.riskCommon.RiskReferrence")%>" onclick="otherRiskList()" class="BUTTONWIDTH">
<INPUT type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.riskCommon.Back")%>" onclick="on_Cancel()" class="BUTTON">
</FORM>

<SCRIPT language="JavaScript">
function selectMe(){
	for (i=0;i<frmRiskAdd.riskID.length;i++){
		if (frmRiskAdd.riskID[i].checked){
			frmRiskAdd.submit();
			return;
		}
	}
	alert("<%= languageChoose.getMessage("fi.jsp.riskCommon.NoRiskSeleted")%>");
}
function onAddNew() {
	frmRiskAdd.reqType.value="<%=Constants.RISK_ADDNEW_PREP%>";
 	frmRiskAdd.submit();
}
function onUpdate() {
	frmRiskAdd.reqType.value="<%=Constants.RISK_SOURCE_UPDATE_PREP%>";
 	frmRiskAdd.submit();
}
function otherRiskList() {
	frmRiskAdd.reqType.value="<%=Constants.RISK_LIST_OTHER%>";
 	frmRiskAdd.submit();
}
function on_Cancel() {
	frmRiskAdd.reqType.value="<%=Constants.RISK_LIST_INIT%>";
 	frmRiskAdd.submit();
}
</SCRIPT >
</BODY>
</HTML>
