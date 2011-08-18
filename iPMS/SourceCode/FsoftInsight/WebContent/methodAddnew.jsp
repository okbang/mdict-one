<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>methodAddnew.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></Script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadAdminMenu();frm.methodName.focus()">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.methodAddnew.EstimationMethods")%> </P>
<FORM name="frm" action="Fms1Servlet" method="post">
<TABLE class="Table" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.methodAddnew.Addnewestimationmethod")%> </CAPTION>
    <COL span="1" width="160">
    <COL span="1" width="400">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.methodAddnew.Estimationmethodname")%>* </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" name="methodName" maxlength="50"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.methodAddnew.Description")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="methodDesc"></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<P><INPUT type="button" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.methodAddnew.OK")%> " class="BUTTON" onclick="onOK();">
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.methodAddnew.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.METHOD_LIST%>)"></P>
<INPUT type="hidden" name="reqType" value="<%=Constants.METHOD_ADDNEW%>">
</FORM>
</BODY>
<SCRIPT>
function onOK() {
	if (trim(frm.methodName.value) == "") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.methodAddnew.Estimationmethodnamecannotbeempty")%>");
		frm.methodName.focus();
		return;
	}
	if (frm.methodDesc.value.length > 200) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.methodAddnew.Descriptioncannotbelongerthan200characters")%>");
		frm.methodDesc.focus();
		return;
	}
	frm.reqType.value = "<%=Constants.METHOD_ADDNEW%>";
	frm.submit();
}


</SCRIPT>
</HTML>
