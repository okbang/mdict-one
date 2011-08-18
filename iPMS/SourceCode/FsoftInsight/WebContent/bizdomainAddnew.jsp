<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>bizdomainAddnew.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></Script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadAdminMenu();frm.bizdomainName.focus()">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.bizdomainAddnew.BusinessDomain")%> </P>
<FORM name="frm" action="Fms1Servlet" method="post">
<TABLE class="Table" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.bizdomainAddnew.Addnewbusinessdomain")%> </CAPTION>
    <COL span="1" width="150">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.bizdomainAddnew.BusinessDomain1")%>* </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" name="bizdomainName" maxlength="50"></TD>
        </TR>
    </TBODY>
</TABLE>
<P><INPUT type="button" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.bizdomainAddnew.OK")%> " class="BUTTON" onclick="onOK();">
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.bizdomainAddnew.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.BUSINESS_DOMAIN%>)"></P>
<INPUT type="hidden" name="reqType" value="<%=Constants.BIZDOMAIN_ADD%>">
</FORM>
<SCRIPT>
function onOK() {
	if (trim(frm.bizdomainName.value) == "") {
		window.alert("<%=languageChoose.getMessage("fi.jsp.bizdomainAddnew.BusinessDomainNameCannotBeEmpty")%>");
		frm.bizdomainName.focus();
		return;
	}
	frm.reqType.value = "<%=Constants.BIZDOMAIN_ADD%>";
	frm.submit();
}
</SCRIPT>
</BODY>
</HTML>
