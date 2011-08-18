<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, 
		 fpt.timesheet.bean.*,
         fpt.timesheet.bean.Approval.ChangePasswordBean" 
%>
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ChangePasswordBean beanChangePassword = (ChangePasswordBean)request.getAttribute("beanChangePassword");
%>
<HTML>
<HEAD>
<style>

#passwordStrength
{
	height:10px;
	display:block;
	float:left;
}

.strength0
{
	width:250px;
	background:#cccccc;
}

.strength1
{
	width:50px;
	background:#ff0000;
}

.strength2
{
	width:100px;	
	background:#ff5f5f;
}

.strength3
{
	width:150px;
	background:#56e500;
}

.strength4
{
	background:#4dcd00;
	width:200px;
}

.strength5
{
	background:#399800;
	width:250px;
}
</style>

<TITLE>Change Password</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT src='scripts/sha1.js'></SCRIPT>
</HEAD>
<BODY bgcolor="#336699" leftmargin="0" topmargin="0" onload='javascript:setDefaultFocus()'>
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H1><IMG align="top" src="image/tit_ChangePassword.gif"></H1>

<FORM name = "frm" method = "post" action="TimesheetServlet">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="actionType" value="">

<INPUT size="20" type="hidden" name="OldPassword">
<INPUT size="20" type="hidden" name="NewPassword">
</FORM>

<FORM method="post" name="frmChangePassword">
<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName()%></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR noshade size="1">

<%
    if (beanChangePassword.getMessage().length() > 0) {
%>
<P align="center"><FONT style="font-family: Verdana; font-size: 11px; FONT-WEIGHT: bold" color="yellow"><%=beanChangePassword.getMessage()%></FONT></P><%
    }
%>
<P>
<TABLE border="0" width="50%" cellspacing="0" cellpadding="0" align="center">
    <COLGROUP>
        <COL width="40%">
        <COL width="60%">
    <TR>
        <TD><FONT class="label1" color="#ffffff"><STRONG>Old password</STRONG></FONT></TD>
        <TD><INPUT type="password" name="OldPass" class="flatTextbox" maxlength="19"></TD>
    </TR>
    <TR>
        <TD><FONT class="label1" color="#ffffff"><STRONG>New password</STRONG></FONT></TD>
        <TD><INPUT type="password" name="NewPass" class="flatTextbox" maxlength="19" onkeyup="passwordStrength()"></TD>
    </TR>
    <TR>
        <TD align="left"><FONT class="label1" color="#ffffff"><STRONG>Confirm password</STRONG></FONT></TD>
        <TD align="left"><INPUT type="password" name="ConfirmPassword" class="flatTextbox" maxlength="19"></TD>
    </TR>
    
    <tr>
		<td align="left"><label for="passwordStrength"><a href="javascript:PasswordPolicy();" style="cursor: pointer;"><FONT class="label1" color="#ffffff"><STRONG>Password strength</STRONG></FONT></a></label></TD>
			<td align="left">
				<a href="javascript:PasswordPolicy();"><FONT class="label1" color="#ffffff"><div id="passwordDescription">Password not entered</div></FONT></a>
				<div id="passwordStrength" class="strength0"></div>
			</td>
	</tr>
	
</TABLE>
<BR>
<BR>
<TABLE border="0" width="50%" cellspacing="0" cellpadding="0" align="center">
    <COLGROUP>
        <COL width="40%">
        <COL width="60%">
    <TR>
        <TD></TD>
        <TD><INPUT type="button" value="Change" name="ChangePassword" class="Button" onclick="javascript:doChange()">
        &nbsp;&nbsp;&nbsp;&nbsp; <INPUT type="reset" value="Reset" name="Reset" class="Button"></TD>
    </TR>
</TABLE>
</DIV>
</FORM>

<SCRIPT language="javascript">
document.forms[1].OldPass.focus();

function doChange() {
    var form = document.forms[1];
    if (form.NewPass.value == '') {
        alert('The password can not blank.');
        form.NewPass.focus();
        return;
    }
    else {
        if (form.NewPass.value != form.ConfirmPassword.value) {
            alert('The new password was not correctly confirmed.');
            form.ConfirmPassword.focus();
            return;
        }
    }
    if(!passwordStrength()){
    	window.alert("Password strength must be at least 'Better'");
    	form.NewPass.focus();
    	return;
    }
    document.frm.OldPassword.value = hex_sha1(form.OldPass.value);
    document.frm.NewPassword.value = hex_sha1(form.NewPass.value);
    frm.hidAction.value = "AA";
    frm.hidActionDetail.value = "SaveNewPassword";
    frm.action = "TimesheetServlet";
    frm.submit();
}
function passwordStrength()
	{
		var desc = new Array();
		desc[0] = "Very Weak";
		desc[1] = "Weak";
		desc[2] = "Better";
		desc[3] = "Medium";
		desc[4] = "Strong";
		desc[5] = "Strongest";
		
		var password = document.forms[1].NewPass.value;
		var score   = 0;
	
		//if password bigger than 6 give 1 point
		if (password.length > 7) {
			score++;
		
			//if password has both lower and uppercase characters give 1 point	
			if ( ( password.match(/[a-z]/) ) && ( password.match(/[A-Z]/) ) ) score++;
		
			//if password has at least one number give 1 point
			if (password.match(/\d+/)) score++;
		
			//if password has at least one special caracther give 1 point
			if ( password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/) )	score++;
		
			//if password bigger than 12 give another 1 point
			if (password.length > 12) score++;
		
			 document.getElementById("passwordDescription").innerHTML = desc[score];
			 document.getElementById("passwordStrength").className = "strength" + score;
		}else{
			score = 0;
			document.getElementById("passwordDescription").innerHTML = desc[score];
			document.getElementById("passwordStrength").className = "strength" + score;
		}
		if(score > 1) return true;
		else return false;
	}

function setDefaultFocus() {
    document.forms[1].OldPass.focus();
}
	function PasswordPolicy()
	{
		  var generator=window.open('','name','height=320,width=500');
		  
		  generator.document.write('<html><head><title>Password Policy</title>');
		  generator.document.write('<link rel="stylesheet" href="style.css">');
		  generator.document.write('</head><body>');
		  generator.document.write('Passwords strength guideline:<br>');
		  generator.document.write('Password must has at least 8 characters (1 point) and<br>');
		  generator.document.write('1) It contains both small case and upper case characters: 1 point<br>');
		  generator.document.write('2) It contains numerical characters (0-9): 1 point<br>');
		  generator.document.write('3) It contains special characters (.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]): 1 point<br>');
		  generator.document.write('4) It contains more than 12 characters: 1 point<br>');
		  generator.document.write('Passwords strength evaluation:<br>');
		  generator.document.write('0 - Very Weak<br>');
		  generator.document.write('1 - Weak<br>');
		  generator.document.write('2 - Better<br>');
		  generator.document.write('3 - Medium<br>');
		  generator.document.write('4 - Strong<br>');
		  generator.document.write('5 - Strongest<br>');
	  	  generator.document.write('Password must has at least 2 point<br>');
		  generator.document.write('<a href="javascript:self.close()"> Close</a> the popup.<br><br>');
		  generator.document.write('</body></html>');
		  generator.document.close();
	}
</SCRIPT>

</BODY>
</HTML>