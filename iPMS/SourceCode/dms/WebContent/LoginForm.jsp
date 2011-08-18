<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
	page isThreadSafe="false" errorPage="error.jsp"import="fpt.dms.framework.util.CommonUtil.*,fpt.dms.constant.*" %><%
    fpt.dms.bean.login.LoginBean beanLogin
            = (fpt.dms.bean.login.LoginBean)request.getAttribute("beanLogin");
    if (beanLogin == null){
        beanLogin = new fpt.dms.bean.login.LoginBean();
    }
%>
<HTML>
<HEAD>
<SCRIPT language="javascript" src="scripts/fnc_CookieFunctions.js"></SCRIPT>
<SCRIPT language="javascript" src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT language="javascript" src="scripts/BigInt.js"></SCRIPT>
<SCRIPT language="javascript" src="scripts/rsa.js"></SCRIPT>
<script language="javascript" src="scripts/sha1.js"></script>
<TITLE>DMS Login Home Page</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
</HEAD>
<BODY bgcolor="#FFFFFF" topmargin="0" leftmargin="0"onkeypress='javascript:setKeypress(event.which)'>
<TABLE border="0" cellpadding="0" cellspacing="0" height="51" width="100%">
    <TBODY>
        <TR>
            <TD bgcolor="#310c52" width="212" height="51" background="Images/bgr_header.gif"> <IMG border="0" src="Images/defect_logop1.gif"><BR>
            </TD>
            <TD bgcolor="#310C52" height="51" width="50%" background="Images/bgr_header.gif" align="left" valign="top"><IMG border="0" src="Images/defect_logop2.gif"></TD>
            <TD bgcolor="#310C52" height="51" width="50%" background="Images/bgr_header.gif" align="right" valign="top"><IMG border="0" src="Images/header.gif"></TD>
        </TR>
        <TR>
            <TD bgcolor="#000084" align="left" width="111"><IMG border="0" src="Images/logo2.gif"></TD>
            <TD bgcolor="#310C52" valign="middle" align="left" colspan="2"></TD>
        </TR>
    </TBODY>
</TABLE>
<DIV>
<P><BR>
<IMG border="0" src="Images/Login.gif" width="411" height="28"></P>
</DIV>
<HR class="Line1">
<BR>
<%
    if ((beanLogin.getMessage() != null) && (!beanLogin.getMessage().equals(""))) {
%>
<table border="0">
	<tr>
		<td><FONT color="red"><B>
			<%out.print(beanLogin.getMessage());%>
			</B></FONT>
		</td>
	</tr>
</table>
<%
}
%>
<FORM name="frmLoginPost">
<TABLE border="0" width="411">
    <TR>
        <TD width="70%" align="right"><B>User name</B></TD>
        <TD width="5%"></TD>
        <TD width="25%" align="right"><INPUT type="text" name="txtAccount" class="SmallBox" value="<%=beanLogin.getAccount()%>"></TD>
    </TR>
    <TR>
        <TD width="70%" align="right"><B>Password</B></TD>
        <TD width="5%"></TD>
        <TD width="25%" align="right"><INPUT type="password" name="txtPassword" class="SmallBox"></TD>
    </TR>
    <TR>
    </TR>
    <TR>
        <TD width="70%" align="right"></TD>
        <TD width="5%"></TD>
        <TD width="25%">
        <P><INPUT type="button" name="Login" class="Button" onclick="DoLogin()" value="Login"></P>
        </TD>
    </TR>
</TABLE>
</FORM>
<FORM method="POST" action="DMSServlet" name="frmLogin">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="txtAccount">
<INPUT type="hidden" name="txtPassword">
</FORM>
<DIV>
<P><BR><BR><BR><BR><BR><BR><BR><BR></P>
<HR class="Line1">
<TABLE>
    <TR>
        <TD width="10%"></TD>
        <TD width="90%"><FONT class="label1">
        Copyright © 2002-2007 FPT-Software. Fsoft Management Suite 2006, DMS Version 3.8.2<BR>
        </FONT>
        </TD>
    </TR>
</TABLE>
</DIV>
<SCRIPT language="Javascript">
    var key;
    //LogForm(frmLogin, "load");
    document.frmLoginPost.txtAccount.focus();
function DoLogin() {
    //Save Cookie:
    //LogForm(document.frmLogin, "save");
    var form = document.frmLogin;
    document.frmLogin.txtAccount.value = document.frmLoginPost.txtAccount.value;
    document.frmLogin.txtPassword.value = hex_sha1(document.frmLoginPost.txtPassword.value);
    document.frmLogin.hidAction.value = "ViewDefectListing";
    document.frmLogin.action = "DMSServlet";
    document.frmLogin.submit();
}
function setKeypress(key) {
    if (navigator.appName != "Netscape") {
        key = event.keyCode;
    }
    if (key==13) {
        DoLogin();
    }
}
</SCRIPT></BODY>
</HTML>
