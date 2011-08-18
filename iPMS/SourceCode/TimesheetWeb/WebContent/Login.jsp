<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%  
	fpt.timesheet.bean.UserInfoBean beanUserInfo = (fpt.timesheet.bean.UserInfoBean)session.getAttribute("beanUserInfo");
//    fpt.timesheet.util.Rsa rsa = (fpt.timesheet.util.Rsa) session.getAttribute("rsa");
%>
<HTML>
<HEAD>
<TITLE>Timesheet Login</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/tsStyleSheet.css" type="text/css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT language="javascript" src="scripts/fnc_CookieFunctions.js"></SCRIPT>
<SCRIPT language="javascript" src="scripts/BigInt.js"></SCRIPT>
<%--	SCRIPT language="javascript" src="scripts/rsa.js"></SCRIPT>		--%>
<SCRIPT language="javascript" src="scripts/sha1.js"></SCRIPT>
</HEAD>
<BODY bgcolor="#336699" leftmargin="0" topmargin="0" onkeypress='javascript:setKeypress_Login(event.which)'>
<DIV align="center">
<TABLE bgcolor="#336699" border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="122" align="left"><IMG border="0" src="image/FPTsoft.gif" width="122" height="56"></TD>
        <TD width="468" align="left"><IMG border="0" src="image/fpt_soft2.gif" width="468" height="56"></TD>
        <TD width="80%" align="left"><IMG border="0" src="image/cell4.gif" width="100%" height="56" vspace="0" hspace="0"></TD>
    </TR>
</TABLE>
<TABLE bgcolor="#336699" width="100%">
    <TR>
        <TD bgcolor="#336699" height="15"></TD>
    </TR>
</TABLE>
</DIV>
<DIV align="center" height="1000"><BR>
<BR>
<BR>
<BR>
<FORM name="frmInput">
<TABLE border="0" width="50%" cellspacing="0" cellpadding="0">
    <TR>
        <TD width="100%" colspan="2">
        <H1><IMG align="top" src="image/tit_LoginTimesheet.gif"></H1>
        <B><FONT size="5"></FONT></B>
<%
    if (beanUserInfo.getMessage() != "") {
%>
        &nbsp;<FONT class="labelRMessage"><%=beanUserInfo.getMessage()%></FONT><%
    }
%>
        <BR><BR>
        </TD>
    </TR>
    <TR>
        <TD width="30%"><FONT class="label1" color="#ffffff">&nbsp;&nbsp;User&nbsp;name</FONT><FONT color="red">&nbsp;*&nbsp;</FONT></TD>
        <TD width="70%"><INPUT type="text" name="txtLoginName" size="40" class="BigTextbox" value="<%=beanUserInfo.getLoginName()%>"></TD>
    </TR>
    <TR>
        <TD width="30%"><FONT class="label1" color="#ffffff">&nbsp;&nbsp;Password</FONT><FONT color="red">&nbsp;*&nbsp;</FONT></TD>
        <TD width="70%"><INPUT type="password" name="txtLoginPassword" size="40" class="BigTextbox" value=""></TD>
    </TR>
    <TR>
        <TD width="30%"><FONT class="label1" color="#ffffff">&nbsp;&nbsp;Location</FONT></TD>
        <TD width="70%"><SELECT name="Location" size="1" class="BigCombo">
            <OPTION selected value="0">Timesheet</OPTION>
            <OPTION value="1">Approved for External Projects</OPTION>
            <OPTION value="2">Approved by Quality Assurance</OPTION>
            <OPTION value="3">Approve for Internal Projects</OPTION>
            <OPTION value="4">Administration (for QA only)</OPTION>
            <OPTION value="5">Timesheet Exemption</OPTION>
        </SELECT></TD>
    </TR>
    <TR>
        <TD width="30%"><BR>
        </TD>
        <TD width="70%"><BR>
        <INPUT type="button" name="Login" class="Button" onclick='javascript:doLogin()' value="   Login   "></TD>
    </TR>
</TABLE>
</FORM>
<FORM method="post" action="TimesheetServlet" name="frmLogin">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="txtAccount">
<INPUT type="hidden" name="txtPassword">
<INPUT type="hidden" name="Location">
</FORM>
</DIV>
<DIV>
<P><BR><BR><BR><BR><BR><BR></P>
<HR width="100%" color="orange">
<TABLE>
    <TR>
        <TD width="10%"></TD>
        <TD width="90%"><FONT class="label1" color="#ffffff">
        <P>Copyright © 2002-2007 FPT-Software. Fsoft Management Suite 2006, Timesheet Version 3.8.4<BR></P>
        </FONT></TD>
    </TR>
</TABLE>
</DIV>

<SCRIPT language="javascript">
var key;
//LogForm(frmInput, "load");
frmInput.txtLoginName.focus();

function doLogin() {
    //Save Cookie:
    //LogForm(frmInput, "save");

    frmLogin.txtAccount.value = frmInput.txtLoginName.value;
    frmLogin.txtPassword.value = hex_sha1(frmInput.txtLoginPassword.value);
    frmLogin.Location.value = frmInput.Location.value;
    frmLogin.hidAction.value = "AA";
    frmLogin.hidActionDetail.value = "TimesheetLogin";
    frmLogin.action = "TimesheetServlet";
    frmLogin.submit();
}

</SCRIPT>
</BODY>
</HTML>