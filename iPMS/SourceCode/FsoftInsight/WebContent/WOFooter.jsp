<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>WOFooter.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();onLoad();">
<%
session.setAttribute("change_source_page", "0");

%>
<FORM name="frm" method="post" action="Fms1Servlet">
<P>
<INPUT type="button" class="BUTTON" onclick="onSubmit1()" name="Info" value=" <%=languageChoose.getMessage("fi.jsp.WOFooter.Information")%> ">
<INPUT type="button" class="BUTTON" onclick="onSubmit2()" name="Deli" value=" <%=languageChoose.getMessage("fi.jsp.WOFooter.Deliverables")%> ">
<INPUT type="button" class="BUTTON" onclick="onSubmit3()" name="Metr" value=" <%=languageChoose.getMessage("fi.jsp.WOFooter.Metrics")%> ">
<INPUT type="button" class="BUTTON" onclick="onSubmit6()" name="Team" value=" <%=languageChoose.getMessage("fi.jsp.WOFooter.Team")%> ">
<INPUT type="button" class="BUTTON" onclick="onSubmit7()" name="Chan" value=<%=languageChoose.getMessage("fi.jsp.WOFooter.Changes")%>>
<INPUT type="button" class="BUTTON" onclick="onSubmit8()" name="Acce" value=" <%=languageChoose.getMessage("fi.jsp.WOFooter.Acceptance")%> ">
<INPUT type="button" class="BUTTON" onclick="onSubmit9()" name="Sign" value=" <%=languageChoose.getMessage("fi.jsp.WOFooter.Signatures")%> ">
</P>
<INPUT type="hidden" name="reqType">
<INPUT type="hidden" name="change_source_page">
</FORM>
</BODY > 
<SCRIPT language="javascript">
function onLoad() {	
	frm.Info.style.color = '#FF66FF';
	frm.Deli.style.color = 'White';
	frm.Metr.style.color = 'White';
	frm.Team.style.color = 'White';
	frm.Chan.style.color = 'White';
	frm.Acce.style.color = 'White';
	frm.Sign.style.color = 'White';
	onSubmit1();
	
}
function onSubmit1() {	
	frm.Info.style.color = '#FF66FF';
	frm.Deli.style.color = 'White';
	frm.Metr.style.color = 'White';
	frm.Team.style.color = 'White';
	frm.Chan.style.color = 'White';
	frm.Acce.style.color = 'White';
	frm.Sign.style.color = 'White';
	frm.target = "WOMain";
	frm.reqType.value = "<%=Constants.WO_GENERAL_INFO_GET_LIST %>";
	frm.submit();
}
function onSubmit2() {
	frm.Info.style.color = 'White';
	frm.Deli.style.color = '#FF66FF';
	frm.Metr.style.color = 'White';
	frm.Team.style.color = 'White';
	frm.Chan.style.color = 'White';
	frm.Acce.style.color = 'White';
	frm.Sign.style.color = 'White';
	frm.target = "WOMain";
	frm.reqType.value = "<%=Constants.WO_DELIVERABLE_GET_LIST%>";
	frm.submit();
}

function onSubmit3() {
	frm.Info.style.color = 'White';
	frm.Deli.style.color = 'White';
	frm.Metr.style.color = '#FF66FF';
	frm.Team.style.color = 'White';
	frm.Chan.style.color = 'White';
	frm.Acce.style.color = 'White';
	frm.Sign.style.color = 'White';
	frm.target = "WOMain";
	frm.reqType.value = "<%=Constants.WO_PERFORMANCE_GET_LIST %>";
	frm.submit();
}
function onSubmit6() {
	frm.Info.style.color = 'White';
	frm.Deli.style.color = 'White';
	frm.Metr.style.color = 'White';
	frm.Team.style.color = '#FF66FF';
	frm.Chan.style.color = 'White';
	frm.Acce.style.color = 'White';
	frm.Sign.style.color = 'White';
	frm.target = "WOMain";
	frm.reqType.value = "<%=Constants.WO_TEAM_GET_LIST %>";
	frm.submit();
}
function onSubmit7() {
	frm.Info.style.color = 'White';
	frm.Deli.style.color = 'White';
	frm.Metr.style.color = 'White';
	frm.Team.style.color = 'White';
	frm.Chan.style.color = '#FF66FF';
	frm.Acce.style.color = 'White';
	frm.Sign.style.color = 'White';
	frm.target = "WOMain";
	frm.reqType.value = "<%=Constants.WO_CHANGE_GET_LIST%>";
	frm.change_source_page.value = "0";
	frm.submit();
}
function onSubmit8() {
	frm.Info.style.color = 'White';
	frm.Deli.style.color = 'White';
	frm.Metr.style.color = 'White';
	frm.Team.style.color = 'White';
	frm.Chan.style.color = 'White';
	frm.Acce.style.color = '#FF66FF';
	frm.Sign.style.color = 'White';
	frm.target = "WOMain";
	frm.reqType.value = "<%=Constants.WO_ACCEPTANCE_GET_LIST %>";
	frm.submit();
}
function onSubmit9() {
	frm.Info.style.color = 'White';
	frm.Deli.style.color = 'White';
	frm.Metr.style.color = 'White';
	frm.Team.style.color = 'White';
	frm.Chan.style.color = 'White';
	frm.Acce.style.color = 'White';
	frm.Sign.style.color = '#FF66FF';
	frm.target = "WOMain";
	frm.reqType.value = "<%=Constants.WO_SIG_GET_LIST%>";
	frm.submit();
}
</SCRIPT>
</HTML>
