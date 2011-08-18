<%@ page language="java" import="fpt.ncms.constant.NCMS"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    fpt.ncms.bean.UserInfoBean beanUserInfo =
            (fpt.ncms.bean.UserInfoBean)session.getAttribute("beanUserInfo");
//    fpt.ncms.util.CommonUtil.Rsa rsa =
//            (fpt.ncms.util.CommonUtil.Rsa) session.getAttribute("rsa");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
<SCRIPT language="javascript" src="inc/fnc_CookieFunctions.js"></SCRIPT>
<SCRIPT language="javascript" src="inc/BigInt.js"></SCRIPT>
<%--	<SCRIPT language="javascript" src="inc/rsa.js"></SCRIPT>		--%>
<SCRIPT language="javascript" src="inc/sha1.js"></SCRIPT>
<TITLE>NCMS Login Page</TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0" onkeypress='javascript:setKeypress(event.which)'>
<%@ include file="Header.jsp"%>
<FORM name="frmInput">
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%" leftmargin="10">
    <COLGROUP>
        <COL width="25%">
        <COL width="50%">
        <COL width="25%">
    <TR>
        <TD></TD>
        <TD><FONT face="Verdana" color="red">
        <DIV id="divMsg"><%
    if (beanUserInfo.getMessage() != "") {
        out.write(beanUserInfo.getMessage());
        beanUserInfo.setMessage("");
    }%></DIV>
        </FONT></TD>
        <TD></TD>
    </TR>
    <TR>
        <TD></TD>
        <TD>&nbsp;</TD>
        <TD></TD>
    </TR>
    <TR>
        <TD></TD>
        <TD style="Border-Bottom: #bdbdbd thin solid; Border-Left: #bdbdbd thin solid; Border-Right: #bdbdbd thin solid; Border-Top: #bdbdbd thin solid; border-width: 1px">

        <TABLE bgcolor="#EFEFEF" border="0" cellspacing="0" cellpadding="0" width="100%">
            <COL width="20%">
            <COL width="20%" style="Font-weight: bold">
            <COL width="15%">
            <COL width="15%">
            <COL width="30%">
            <TR>
                <TD>&nbsp;</TD>
                <TD></TD>
                <TD></TD>
                <TD></TD>
            </TR>
            <TR>
                <TD></TD>
                <TD>User&nbsp;Name</TD>
                <TD colspan="2">&nbsp;<INPUT name="txtLoginName" size="15" maxlength="30" tabindex="1" value="<%=beanUserInfo.getLoginName()%>"></TD>
                <TD></TD>
            </TR>
            <TR>
                <TD></TD>
                <TD>Password</TD>
                <TD colspan="2">&nbsp;<INPUT type="password" name="txtLoginPassword" size="15" maxlength="30" tabindex="2" onkeypress="javascript:if (window.event.keyCode==13) doLogin();"></TD>
                <TD></TD>
            </TR>
            <TR>
                <TD></TD>
                <TD></TD>
                <TD>&nbsp;<INPUT type="button" name="btnLogin" class="button" onclick="javascript:doLogin()" value="Login">
                <!--IMG border="0" src="images/Buttons/b_login_n.gif" tabindex="4" onkeypress="javascript:if (window.event.keyCode==13) doLogin();" onmouseover="this.style.cursor='hand';window.event.srcElement.src = 'images/Buttons/b_login_p.gif'" onmouseout="this.src = 'images/Buttons/b_login_n.gif'" onclick="javascript:doLogin()" width="32" height="21"-->
                </TD>
            </TR>
            <TR>
                <TD></TD>
                <TD>&nbsp;</TD>
                <TD></TD>
                <TD></TD>
            </TR>
        </TABLE>
        </TD>
        <TD></TD>
    </TR>
</TABLE>
<TABLE width="100%" height="50%">
<TR>
</TR>
</TABLE>
<TABLE width="100%">
    <TR>
        <TD width="100%"><FONT class="label1" color="#000fff">
        <CENTER><P>Copyright © 2002-2005 FPT-Software<BR>Fsoft Management Suite 2005, Ncms Version 1.7</P></CENTER>
        </FONT></TD>
    </TR>
</TABLE>
</FORM>
<FORM name="frmLogin" method="post" action="NcmsServlet">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">

<INPUT type="hidden" name="txtAccount">
<INPUT type="hidden" name="txtPassword">
<INPUT type="hidden" name="cboMode" value="0">
</FORM>
<SCRIPT language="javascript">
    var key;
    //LogForm(frmInput, "load");
    frmInput.txtLoginName.focus();
function doLogin() {
	var stringTemp;
	stringTemp = document.frmInput.txtLoginName.value;
	if((stringTemp == null) || (stringTemp == "")){
		alert("<%=NCMS.JSP_PLEASE_ENTER_USERNAME%>");
		document.frmInput.txtLoginName.focus();
		return;
	}
	stringTemp = document.frmInput.txtLoginPassword.value;
	if((stringTemp == null) || (stringTemp == "")){
		alert("<%=NCMS.JSP_PLEASE_ENTER_PASSWORD%>");
		document.frmInput.txtLoginPassword.focus();
		return;
	}
    //LogForm(frmInput, "save");
    frmLogin.txtAccount.value = frmInput.txtLoginName.value;
    frmLogin.txtPassword.value = hex_sha1(frmInput.txtLoginPassword.value);
    //frmLogin.cboMode.value = frmInput.cboMode.value;
    frmLogin.hidAction.value = "<%=NCMS.LOGIN_ACTION%>";
    frmLogin.action = "NcmsServlet";
    frmLogin.submit();
}

function setKeypress(key) {
    if (navigator.appName != "Netscape") {
        key = event.keyCode;
    }
    if (key==13) {
        doLogin();
    }
}
</SCRIPT>
</BODY>
</HTML>