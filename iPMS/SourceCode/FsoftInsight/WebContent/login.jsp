<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*,com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<HTML>
<%
	LanguageChoose languageChoose =new LanguageChoose(LanguageChoose.ENGLISH);
	session.setAttribute("LanguageChoose",languageChoose);
%>

<HEAD>
<TITLE><%=languageChoose.getMessage("fi.jsp.login.title")%></TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<script type="text/javascript" src="jscript/sha1.js"></script>
<SCRIPT language="JavaScript">
ImgPreload('_HPB_ROLLOVER1', '<%=languageChoose.getMessage("fi.img.login.loginButton")%>', '<%=languageChoose.getMessage("fi.img.login.loginButton1")%>');
</SCRIPT>
<SCRIPT language="javascript" src="jscript/BigInt.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<BODY bgcolor="#ffffff" onkeypress="javascript:if (window.event.keyCode==13) on_Submit()" onload = "bodyLoad();">
<%
String error = (String) request.getParameter("error");
%>
<IMG src="image/image.gif" width="556" height="149" border="0">
<P><BR></BR>
</P>
<FORM name = "frmLogin">

<TABLE border="0" width="462" cellpadding="0" cellspacing="0" align="center">
<caption class="ERROR"><%=(((error != null) && (error != ""))?languageChoose.getMessage(error):"")%></caption>
	<TBODY>
		<TR>
			<TD background="image/login/Box.gif" nowrap
				height="150" width="462" align="center">
				<P class ="TITLE" align="center"><%=languageChoose.getMessage("fi.jsp.login.InsightLogin")%>&nbsp;&nbsp;</P>
			<TABLE border="0" width="200" align="center">
			<caption class ="HDR"></caption>
				<TBODY>
					<TR >
						<TD class ="HDR"><%=languageChoose.getMessage("fi.jsp.login.userId")%></TD>
						<TD><INPUT size="20" type="text" name="loginName" onkeypress = "return onUsernamePress(event);"></TD>
					</TR>
					<TR >
						<TD class ="HDR"><%=languageChoose.getMessage("fi.jsp.login.password")%></TD>
						<TD><INPUT size="20" type="password" name="loginPass" onkeypress="return onPasswordPress(event);"></TD>
					</TR>
					<TR >
						<TD class ="HDR"><%=languageChoose.getMessage("fi.jsp.login.language")%></TD>
						<TD><SELECT name="cbolang" class="COMBO">
							<OPTION value="EN" selected>ENGLISH</OPTION>
							<OPTION value="JA">日本語</OPTION>
						</SELECT></TD>
					</TR>
				</TBODY>
			</TABLE>
			<BR>
		<IMG src="image/login/loginButton.gif" width="68" height="16"
				border="0" name="_HPB_ROLLOVER1"
				onmouseout="ImgSwap('_HPB_ROLLOVER1', 'image/login/loginButton.gif');"
				onmouseover="onMouseOver();"
				onclick="javascript:on_Submit();">
		</TD>
		</TR>
		<TR>
			<TD height="100">
				<A class="ERROR" HREF="help/recordofchanges.htm" target="help"><img src="<%=languageChoose.getMessage("fi.img.image.new")%>" border=0><%=languageChoose.getMessage("fi.jsp.login.FIChanges")%></A>
			</TD>
		</TR>
		<TR>
			<TD colspan="4" align="right" class="NormalText"> <A href="http://www.fpt-soft.com/" target="fpt"><%=languageChoose.getMessage("fi.jsp.login.CopyrightbyFPTSoftware")%> 2002-2007</A>   </TD>
		</TR>
	</TBODY>
</TABLE>

</FORM>
<FORM name = "frm" action="Fms1Servlet?reqType=<%=Constants.LOGIN%>" method = "post">
<INPUT size="20" type="hidden" name="loginName">
<INPUT size="20" type="hidden" name="loginPass">
<INPUT size="20" type="hidden" name="cbolang" >
</FORM>

<script language = "javascript">
	var key;
	function on_Submit(){
		var stringTemp;
		stringTemp = document.frmLogin.loginName.value;
		if((stringTemp == null) || (stringTemp == "")){
			alert("<%= languageChoose.getMessage("fi.jsp.login.Pleaseenterusername")%>");
			document.frmLogin.loginName.focus();
			return;
		}
		stringTemp = document.frmLogin.loginPass.value;
		if((stringTemp == null) || (stringTemp == "")){
			alert("<%= languageChoose.getMessage("fi.jsp.login.Pleaseenterpassword")%>");
			document.frmLogin.loginPass.focus();
			return;
		}
		frm.target="_top";
		with (document.frm) {
			document.frm.loginName.value = document.frmLogin.loginName.value;
			document.frm.loginPass.value = hex_sha1(document.frmLogin.loginPass.value);
			document.frm.cbolang.value = frmLogin.cbolang.value
		}
				
		document.frm.submit();
	}
	function onMouseOver(){	
		ImgSwap('_HPB_ROLLOVER1', '<%=languageChoose.getMessage("fi.img.login.loginButton1")%>');
		document.images('_HPB_ROLLOVER1').style.cursor="hand";
	}	
	function bodyLoad()	{
		document.frmLogin.loginName.focus();
	}
	
	function onPasswordPress(e)
	{
		var keycode;
		if (window.event) keycode = window.event.keyCode; 
		else if (e) keycode = e.which; 
		else return true;

		if (keycode == 13)
	    {
	    	on_Submit();
			return false;
		}
		else return true;
	}

	function onUsernamePress(e)
	{
		var keycode;
		if (window.event) keycode = window.event.keyCode;
		else if (e) keycode = e.which;
		else return true;

		if (keycode == 13)
	   	{
	   		document.frmLogin.loginPass.focus();
	   		return false;
	   	}
		else return true;	
	}
</script>
</BODY>
</HTML>
