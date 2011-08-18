<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page isThreadSafe="true" errorPage="error.jsp" %><%
    fpt.dashboard.bean.UserInfoBean beanUserInfo
            = (fpt.dashboard.bean.UserInfoBean)session.getAttribute("beanUserInfo");
//    fpt.dashboard.util.CommonUtil.Rsa rsa =
//            (fpt.dashboard.util.CommonUtil.Rsa) session.getAttribute("rsa");
%>
<HTML>
<HEAD>
<SCRIPT language="javascript" src="scripts/fnc_CookieFunctions.js"></SCRIPT>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT language="javascript" src="scripts/BigInt.js"></SCRIPT>
<SCRIPT language="javascript" src="scripts/sha1.js"></SCRIPT>
<TITLE>Login Dashboard</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</HEAD>
<BODY width="100%" onkeypress="onSign(event.which)" marginwidth="0" marginheight="0" topmargin="0" bottommargin="0" rightmargin="0" leftmargin="0" bgcolor="#E3E3EB">
<FORM name="frmInput">
<TABLE border="0" width="100%" height="437" cellpadding="0" cellspacing="0">
    <TR>
        <TD width="100%" height="41" background="images/header.gif">&nbsp;</TD>
    </TR>
    <TR>
        <TD width="100%"><BR>
        </TD>
    </TR>
    <TR>
        <TD width="100%" height="325">
        <TABLE border="0" width="100%" cellpadding="0" cellspacing="0">
            <TR>
                <TD width="50%"><IMG border="0" src="images/logo.gif" width="442" height="456"></TD>
                <TD width="50%" valign="middle">
                <TABLE border="0" cellpadding="0" cellspacing="0" height="176">
                    <TR>
                        <TD height="50"><IMG border="0" src="images/title.gif" width="322" height="51"></TD>
                    </TR>
                    <TR>
                        <TD height="126">
                        <TABLE border="0" cellpadding="0" cellspacing="0" height="124">
                            <TR>
                                <TD colspan="2"><B><FONT color="red"><%
    if (!beanUserInfo.getMessage().equals("")) {
        out.print(beanUserInfo.getMessage());
    }
%>
                                </FONT></B></TD>
                            </TR>
                            <TR>
                                <TD align="left" nowrap width="30%">&nbsp;&nbsp; <B>User Name&nbsp;</B></TD>
                                <TD align="left" width="70%"><INPUT name="txtLoginName" class="SmallBox" value="<%=beanUserInfo.getAccount()%>"></TD>
                            </TR>
                            <TR>
                                <TD align="left" width="30%">&nbsp;&nbsp; <B>Password</B></TD>
                                <TD align="left" width="70%"><INPUT type="password" name="txtLoginPassword" class="SmallBox"></TD>
                            </TR>
                            <TR>
                                <TD width="30%"></TD>
                                <TD align="left" width="70%">
                                <P><INPUT type="button" name="Login" class="Button" onclick="javascript:DoLogin()" value="Login"></P>
                                </TD>
                            </TR>
                        </TABLE>
                        </TD>
                    </TR>
                </TABLE>
                </TD>
            </TR>
        </TABLE>
        </TD>
    </TR>
    <TR>
        <TD width="100%" height="53" background="images/footer.gif"><CENTER><br><%@ include file="footer.jsp"%></CENTER></TD>
    </TR>
</TABLE>
</FORM>
<FORM method="post" action="DashboardServlet" name="frmLogin">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="txtAccount">
<INPUT type="hidden" name="txtPassword">
</FORM>
<SCRIPT language="Javascript">
    var key;
    document.frmInput.txtLoginName.focus();
    //LogForm(frmInput, "load");
function DoLogin() {
    //LogForm(frmInput, "save");
    document.frmLogin.txtAccount.value = document.frmInput.txtLoginName.value;
    document.frmLogin.txtPassword.value = hex_sha1(document.frmInput.txtLoginPassword.value);
    document.frmLogin.hidAction.value = "DashboardHomepage";
    document.frmLogin.action = "DashboardServlet";
    document.frmLogin.submit();
}

function onSign(key) {
    if (navigator.appName != "Netscape") {
        key = event.keyCode;
    }
    if (key==13) {
        DoLogin();
    }
}
</SCRIPT>
</BODY>
</HTML>