<%@ page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*, fpt.dms.bean.SetupEnvironment.*, fpt.dms.framework.util.StringUtil.*" %>
<%@ page isThreadSafe="false" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<%
	UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
	KPAUpdateBean beanKPAUpdate = (KPAUpdateBean)request.getAttribute("beanKPAUpdate");
%>

<html>
<head>
	<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
	<SCRIPT src='scripts/validate.js'></SCRIPT>
	<SCRIPT src='scripts/utils.js'></SCRIPT>

	<!-- Html Form Title -->
	<title>KPA Update</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">

<SCRIPT language = "javascript">
function doSave()
{
	var form = document.frmKPAUpdate;

	var strName;
	form.txtName.value = trimSpaces(form.txtName.value);
	strName = form.txtName.value;

	  if (strName.length == 0)
	  {
	          alert('The value must not be empty!');
	          form.txtName.focus();
	          return;
  }

  	form.hidAction.value = "SE";
	form.hidActionDetail.value = "SaveUpdateKPA";
	form.action = "DMSServlet";
	form.submit();
}

function doBack()
{
	var form = document.frmKPAUpdate;

	form.hidAction.value = "SE";
	form.hidActionDetail.value = "KPAList";
	form.action = "DMSServlet";
	form.submit();
}
</SCRIPT>
</head>

<body bgcolor="#FFFFFF" topmargin="0" leftmargin="0">
<TABLE bgColor=#000000 border= "0" cellPadding="0" cellSpacing="0" height=51
		width="100%">
		  <tbody>
		  <TR>
		    <TD bgColor=#310C52 width=212 height="51" background="Images/bgr_header.gif"><img border="0" src="Images/defect_logop1.gif"><br></TD>
		    <TD bgColor=#310C52 height=51 width="50%" background="Images/bgr_header.gif" align="left" valign="top"><img border="0" src="Images/defect_logop2.gif"></TD>
		    <TD bgColor=#310C52 height=51 width="50%" background="Images/bgr_header.gif" align="right" valign="top"><img border="0" src="Images/header.gif"></TD></TR>
		  <tr>
		          <td bgcolor="#000084" align="left" width="111"><img border="0" src="Images/logo2.gif"></td>
		          <td bgcolor="#310C52" valign="middle" align="left" colspan="2">
		   </td><i></i></tr>
		   </tbody>
 </TABLE>

<div ><p><img border="0" src="Images/KPAUpdate.gif" width="411" height="28"></p></div>

<table border="0" width="100%">
	<tr >
		<td align="left" class="Href" width="50%">
			<p><a href="javascript:doSetupEnvironment(document.forms[0])">
			<span style="color:blue; font-family:arial, helvetica; font-size:10pt; font-weight:bold;">Return Setup Environment</span></a></p>
		</td>
		<td align="right" class="Href" width="50%">
			<p><a href="javascript:doLogout(document.forms[0])">
			<span style="color:blue; font-family:arial, helvetica; font-size:10pt; font-weight:bold;">Logout</span></a></p>
		</td>
	</tr>
</table>

  <!-- ************************************  Form Body  ************************************ -->
	<form method="post" action = "DMSServlet" name="frmKPAUpdate">
	  <!-- ************************************  Standard Screen Identification  ************************************ -->
		<input TYPE = "hidden" NAME = "hidActionDetail" VALUE = "">
    	<input TYPE = "hidden" NAME = "hidAction" VALUE = "">

	  <!-- ************************************  Form variables  ************************************ -->
     <table border="0" width="100%">
     <tr>
     	<td width="15%"></td>
     	<td>
     	<table border="0" width="100%" class="TblOut2">
	 		    <tr>
				  <td width="3%"></td>
	 		      <td width="6%"><b>User:</b> </td>
	 		      <td width="15%"><%=beanUserInfo.getUserName()%></td>

	 		      <td width="6%"><b>Group:</b></td>
	 		      <td width="15%"><%=beanUserInfo.getGroupName()%></td>

	 			  <td width="9%"><b>Login Date:</b> </td>
	 		      <td width="13%"><%=beanUserInfo.getDateLogin()%></td>

	 		    </tr>
		 </table>
		 </td>
		 <td width="15%"></td>
	 </tr>
	 </table>

     <p></p>

     <table border="0" width="100%">
     <tr>
     	<td width="15%"></td>
     	<td>
     	<table border="0" cellspacing="1" cellpadding="0" width="100%" bgcolor="#000000">
              <tr class="Row0">
                <td align="center" width="15%" height="19">ID</td>
                <td align="center" width="50%" height="19">Name</td>
              </tr>
              <tr class="Row2">
                <td width="15%">&nbsp;<%=beanKPAUpdate.getKPAList().getCell(0,0)%>
                	<input type = "hidden" name = "hidID" value = "<%=beanKPAUpdate.getKPAList().getCell(0,0)%>"></td>
                <td width="50%"><input type="text"name = "txtName" value = "<%=beanKPAUpdate.getKPAList().getCell(0,1)%>"  style = "width:100%" maxLength="3"></td>
               </tr>
          </table>
          </td>
          <td width="15%"></td>
       </tr>
       </table>

	  <!-- ************************************  Form Code Here  ************************************ -->

		<p align="center"><input type="button" name="UpdateKPA" class="Button" onClick='javascript:doSave()' value="Update">&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="Back" class="Button" onClick='javascript:doBack()' value="Back"></p>
		<input type="text" name="hiddenText" style="Visibility:hidden">

	  <!-- ************************************  End Form Code  ************************************ -->
</form>
</body>
</html>