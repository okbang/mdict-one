<%@page import="com.fms1.tools.*"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.fms1.infoclass.*,java.util.Vector, com.fms1.web.*" contentType="text/html;charset=UTF-8"%>
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

<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>UserProfileViewOnly.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/BigInt.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/sha1.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">

<%
	String errorChangePass = (String) request.getAttribute("errorChangePass");
//	Rsa rsa=new Rsa(160);
//	session.setAttribute("rsa",rsa);

	UserInfo userInfo  = (UserInfo)session.getAttribute("userInfo");
	String id = userInfo.account;
	UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
	String pass = userLoginInfo.Password.trim();
	String name = userInfo.Name;
	String designation = userInfo.designation;
	String group = userInfo.group;
	String status = userInfo.status;

	Vector rightVector = (Vector)session.getAttribute("rightList");	
%>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onload="<%="1".equals((String) request.getAttribute("closeMe"))?"self.close();":""%>" onkeypress="javascript:if (window.event.keyCode==13) on_ChangePassword()">

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.UserProfileDetail")%></P>
<TABLE width="80%" cellspacing="1" class="Table">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.UserProfileDetail")%></CAPTION>
    <TBODY>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.UserID")%></P>
            </TD>
            <TD width="307" class="CellBGR3"><%=id%> &nbsp;</TD>
        </TR>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.UserName")%></P>
            </TD>
            <TD width="307" class="CellBGR3"><%=name%> &nbsp;</TD>
        </TR>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Position")%></P>
            </TD>
            <TD width="307" class="CellBGR3"><%=((designation==null)?"N/A":designation)%>&nbsp;</TD>
        </TR>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp; <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Group")%> </P>
            </TD>
            <TD width="307" class="CellBGR3"><%=((group==null)?"N/A":group)%>&nbsp;</TD>
        </TR>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Status")%></P>
            </TD>
            <TD width="307" class="CellBGR3"><%
            	switch (Integer.parseInt(status)) {
            		case 1:%> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Staff")%> <%break;
            		case 2:%> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Collaborator")%> <%break;
            		case 3:%> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.External")%> <%break;
            		case 4:%> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.OutPlaced")%> <%break;
            	}%>&nbsp;</TD>
        </TR>
        <TR>
            <TD width="105" class="ColumnLabel">
            <P>&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.ToolsRole")%></P>
            </TD>
            <TD width="307" class="CellBGR3">
            <%
            	if (userInfo.role.equals("1000000000")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Developer")%> <%}
            	if (userInfo.role.equals("1100000000")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.ProjectLeader")%> <%}
            	if (userInfo.role.equals("1110000000")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.GroupLeader")%> <%}
            	if (userInfo.role.equals("1000100000")){%> PQA <%}
            	if (userInfo.role.equals("1111110000")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Manager")%> <%}
				if (userInfo.role.equals("0000001000")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Externalofprojectlevel")%> <%}
				if (userInfo.role.equals("0000001100")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Externalofgrouplevel")%> <%}
				if (userInfo.role.equals("0000000010")){%> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Communicator")%> <%}
			%>
            </TD>
        </TR>
    </TBODY>
</TABLE>
<P><BR>
</P>
<TABLE width="80%" cellspacing="1" class="Table">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Rightgroupforuserbyworkunit")%></CAPTION>
    <TBODY>
        <TR>
            <TD align="center" class="ColumnLabel">
            <P><%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Workunitname")%>
            
            </P>
            </TD>
            <TD align="center" class="ColumnLabel">
            <P><%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Rolename")%></P>
            </TD>
        </TR>
        <%
			for (int i = 0; i < rightVector.size(); i++)
			{
				RolesInfo rightInfo = (RolesInfo) rightVector.elementAt(i);
				String gr = rightInfo.rightGroupID;
				String wu = rightInfo.workunitName;
				String className = "";
  				if (i%2==0) className="CellBGRnews";
  				else className = "CellBGR3";
				%>
        <TR>
            <TD align="center" class="<%=className%>"><%=wu%></TD>
            <TD align="center" class="<%=className%>"><%=gr%></TD>
        </TR>
        <%}
		%>
    </TBODY>
</TABLE>
<P><BR>
</P>
<%if (errorChangePass=="error"){%>
	<p><font color="red"> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Theoldpasswordisincorrect")%> </p>
<%}%>
<FORM name = "frmChangePassword">
<TABLE class="Table" cellSpacing="1" width="80%" id="table3">
	<caption class="TableCaption" align="left"> <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Changepassword")%> </caption>
	<tr>
		<td class="ColumnLabel" width="130">&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.OldPassword")%>*</td>
		<td class="CellBGR3" width="307">&nbsp;<INPUT size="22" type="password" name="OldPassword" maxlength="20"></td>
	</tr>
	<tr>
		<td class="ColumnLabel" width="130">&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.NewPassword")%>*</td>
		<td class="CellBGR3" width="307">&nbsp;<INPUT size="22" type="password" name="NewPassword" maxlength="20" onkeyup="passwordStrength()"></td>
	</tr>
	<tr>
		<td class="ColumnLabel" width="130">&nbsp;<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.ConfirmPassword")%>*</td>
		<td class="CellBGR3" width="307">&nbsp;<INPUT size="22" type="password" name="ConfirmPassword" maxlength="20"></td>
	</tr>	
	
	<tr>
		<td class="ColumnLabel" width="130" rowspan="2">&nbsp;<label for="passwordStrength"><a href="javascript:PasswordPolicy();" style="cursor: hand"><FONT class="label1" color="#ffffff">Password strength</FONT></a></label></td>
			<td class="CellBGR3" width="307" rowspan="2">&nbsp;
				<a href="javascript:PasswordPolicy();"><div id="passwordDescription">Password not entered</div></a>
				<div id="passwordStrength" class="strength0"></div>
			</td>
	</tr>
</TABLE>
</FORM>
<FORM name ="frm" action="Fms1Servlet?reqType=<%=Constants.USER_CHANGE_PASSWORD%>" method = "post">
<input type="hidden" name="OldPassword" value="">
<input type="hidden" name="NewPassword" value="">
<input class="BUTTON" onclick="on_ChangePassword();" type="button" value=" <%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Update")%> " name="Changepassword">
</FORM>
</BODY>
<script language="javascript">

	document.frmChangePassword.OldPassword.focus();

	function on_ChangePassword()
	{
		var stringOldPassword,stringNewPassword, stringConfirmassword;

		stringOldPassword = document.frmChangePassword.OldPassword.value;				
		if ((stringOldPassword == null) || (trim(stringOldPassword) == ""))
		{
			alert("<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Youmustenteroldpassword")%>");
			document.frmChangePassword.OldPassword.focus();
			return;
		}	
		
		stringNewPassword = document.frmChangePassword.NewPassword.value;
		if (check_SpaceCharater(stringNewPassword))
		{
			alert("<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Thepasswordisnot")%>");
			document.frmChangePassword.NewPassword.value="";
			document.frmChangePassword.ConfirmPassword.value="";
			document.frmChangePassword.NewPassword.focus();
			return;
		}

		if ((stringNewPassword == null) || (trim(stringNewPassword) == ""))
		{
			alert("<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Youmustenternewpassword")%>");
			document.frmChangePassword.ConfirmPassword.value="";
			document.frmChangePassword.NewPassword.focus();
			return;
		}				
		
		stringConfirmPassword = document.frmChangePassword.ConfirmPassword.value;		
		if ((stringConfirmPassword == null) || (trim(stringConfirmPassword) == ""))
		{
			alert("<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Youmustenterconfirmpassword")%>");
			document.frmChangePassword.ConfirmPassword.focus();
			return;
		}	
		
		//Check new password must be different from User ID
	 	if (stringNewPassword=="<%=id%>")
	 	{
			alert("<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.Thenewpasswordmustbedifferent")%>");
			document.frmChangePassword.NewPassword.value="";
			document.frmChangePassword.ConfirmPassword.value="";
			document.frmChangePassword.NewPassword.focus();
			return;	 	
	 	}	 				

		//Check confirm password must be the same new password
		
	 	if (stringNewPassword!=stringConfirmPassword)
	 	{
			alert("<%=languageChoose.getMessage("fi.jsp.UserProfileViewOnly.TheNewPasswordWasNotCorrectlyConfirmed")%>");
			document.frmChangePassword.ConfirmPassword.value="";
			document.frmChangePassword.ConfirmPassword.focus();
			return;	 	
	 	}		
	    if(!passwordStrength()){
	    	window.alert("Password strength must be at least 'Better'");
	    	document.frmChangePassword.NewPassword.focus();
	    	return;
	    }	
		
		document.frm.OldPassword.value = hex_sha1(document.frmChangePassword.OldPassword.value);
		document.frm.NewPassword.value = hex_sha1(document.frmChangePassword.NewPassword.value);
		document.frm.submit();
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
		
		var password = document.frmChangePassword.NewPassword.value;
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

	function check_SpaceCharater(strChecking)
	{	
		for (i=0 ;i<strChecking.length;i++)
		{
			if (strChecking.charAt(i)==" ")
			return true;
		}
		return false;		
	}
	
	//if stringCheking.legnth<limitnumber then return: true
	//else return: false
	function check_stringlength(stringCheking,CharacterNumber)
	{
		return (stringCheking.length<CharacterNumber);
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
</script> 
</HTML>
