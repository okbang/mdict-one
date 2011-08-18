<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>methodUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></Script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadAdminMenu();frm.methodName.focus()">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.methodUpdate.EstimationMethods")%> </P>
<FORM name="frm" action="Fms1Servlet" method="post">
<%
	final int methodID = Integer.parseInt(request.getParameter("methodID"));
	Vector methodList=(Vector)session.getAttribute("methodList");
	final EstimationMethodInfo methodInfo = (EstimationMethodInfo) methodList.elementAt(methodID);
%>
<TABLE class="Table" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.methodUpdate.Updateestimationmethod")%> </CAPTION>
    <COL span="1" width="160">
    <COL span="1" width="400">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.methodUpdate.Estimationmethodname")%>* </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" name="methodName" maxlength="50" value="<%=methodInfo.name%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.methodUpdate.Description")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="methodDesc"><%if (methodInfo.note != null) {%><%=methodInfo.note%><%}%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<P><INPUT type="button" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.methodUpdate.OK")%> " class="BUTTON" onclick="onOK();">
<INPUT type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.methodUpdate.Delete")%> " class="BUTTON" onclick="onDelete()" >
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.methodUpdate.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.METHOD_LIST%>)"></P>
<INPUT type="hidden" name="reqType">
<INPUT type="hidden" name="methodID" value="<%=methodID%>">
</FORM>
</BODY>
<SCRIPT>
function onOK() {
	if (trim(frm.methodName.value) == "") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.methodUpdate.Estimationmethodnamecannotbeempty")%>");
		frm.methodName.focus();
		return;
	}
	if (frm.methodDesc.value.length > 200) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.methodUpdate.Descriptioncannotbelongerthan200characters")%>");
		frm.methodDesc.focus();
		return;
	}
	frm.reqType.value = "<%=Constants.METHOD_UPDATE%>";
	frm.submit();
}
function onDelete() {
	<%
	if (methodInfo.methodID != 3) {
	%>
	if (window.confirm("<%= languageChoose.getMessage("fi.jsp.methodUpdate.Areyousuretodelete")%>") != 0) {
		frm.reqType.value = "<%=Constants.METHOD_REMOVE %>";
		frm.submit();
	}
	<%
	}
	else {
	%>
		window.alert("<%= languageChoose.getMessage("fi.jsp.methodUpdate.Sorryyoucannotdelete")%>");
	<%
	}
	%>
}

</SCRIPT>
</HTML>
