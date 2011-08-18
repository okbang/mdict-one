<%@ page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*, fpt.dms.bean.SetupEnvironment.*, fpt.dms.framework.util.StringUtil.*" %>
<%@ page isThreadSafe="false" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<%
	UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
	KPAListBean beanKPAList = (KPAListBean)request.getAttribute("beanKPAList");
%>

<html>
<head>
	<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
	<SCRIPT src='scripts/validate.js'></SCRIPT>
	<SCRIPT src='scripts/utils.js'></SCRIPT>

	<!-- Html Form Title -->
	<title>KPA List</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT language = "javascript">
function doAddNew()
{
	var form = document.frmKPAList;

	var strID;
	var strName;
	strID = form.txtID.value;
	form.txtName.value = trimSpaces(form.txtName.value);
	strName = form.txtName.value;

	if (strID.length == 0)
	{
		  alert('The value must not be empty!');
		  form.txtID.focus();
		  return;
	}
	if (strName.length == 0)
	{
		  alert('The value must not be empty!');
		  form.txtName.focus();
		  return;
	}
	if (strID == 0)
	{
		alert('ID must be greater than zero!');
		form.txtID.focus();
		return;
	}

	form.hidAction.value = "SE";
	form.hidActionDetail.value = "AddKPA";
	form.action = "DMSServlet";
	form.submit();
}

function doDelete()
{
	var form = document.frmKPAList;

	var nCount;
	nCount = 0;
	for (var i=0;i<form.elements.length;i++)
	{
		var e = form.elements[i];
		if (e.name == 'checkBox' && e.type == "checkbox")
		{
			if (e.checked == 1)
			{
				nCount++;
				//strValue = strValue + e.value + ',';
			}
		}
	}
	if (nCount <= 0)
	{
		alert('Please select records to delete!');
		return;
	}
	bOK = window.confirm("Do you want to delete selected records, continue?");
	if (!bOK)
	{
		return false;
	}

	form.hidAction.value = "SE";
	form.hidActionDetail.value = "DeleteKPA";
	form.action = "DMSServlet";
	form.submit();
}

function doUpdate(id, name)
{
	var form = document.frmKPAList;

	form.hidID.value = id;
	form.hidName.value = name;

  	form.hidAction.value = "SE";
	form.hidActionDetail.value = "UpdateKPA";
	form.action = "DMSServlet";
	form.submit();
}

function CheckAll()
{
	var form = document.frmKPAList;
	for (var i=0; i<form.elements.length; i++)
	{
		var e = form.elements[i];
		if (e.name != 'allbox')
		{
			e.checked = form.allbox.checked;
		}
	}
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
		   </td><i></i>
		        </tr>
		   </tbody>
 </TABLE>

<div ><p><img border="0" src="Images/KPAListing.gif" width="411" height="28"></p></div>

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
	<form method="post" action = "DMSServlet" name = "frmKPAList">
	  <!-- ************************************  Standard Screen Identification  ************************************ -->
		<input TYPE = "hidden" NAME = "hidActionDetail" VALUE = "">
    	<input TYPE = "hidden" NAME = "hidAction" VALUE = "">

       	  <!-- ************************************  Form Code Here  ************************************ -->
			<table border="0" width="100%" class="TblOut2">
                <tr>
                  <td width="9%"><b>User:</b> </td>
                  <td width="25%"><%= beanUserInfo.getUserName()%></td>

                  <td width="10%"><b>Group:</b></td>
                  <td width="28%"><%= beanUserInfo.getGroupName()%></td>

				  <td width="12%"><b>Login Date:</b> </td>
                  <td width="17%"><%= beanUserInfo.getDateLogin()%></td>
                </tr>
             </table>
		  <p></p>
          <table border="0" width="100%" cellspacing="0" cellpadding="0">
            <tr>
              <td width="3%"><b>ID</b></td>
              <td width="17%"><input type="text" name="txtID" size="20"  style="width: 121; height: 20" onkeypress="javascript:numberAllowed()" maxLength="5"></td>
              <td width="5%"><b> Name</b></td>
              <td width="34%"><input type="text" name="txtName" size="40" maxLength="3"></td>
              <td width="18%" align="left">
                <input type="button" value="AddNew" name="AddNewKPA" class="button" onClick='javascript:doAddNew()'></td>
            </tr>
          </table>

<!-- Preparing to update -->
 			<input type="hidden" name="hidID" value="" >
 			<input type="hidden" name="hidName" value="" >

		  <p></p>
          <table border="0" cellspacing="1" cellpadding="0" width="100%" bgcolor="#000000">
              <tr class="Row0">
                <td width="3%"  height="19"><input type="checkbox" name="allbox" value="CheckAll" onClick="JavaScript: CheckAll();"></td>
                <td align = "center" width="15%" height="19">ID</td>
                <td align = "center" width="82%" height="19">Name</td>
              </tr>

            <%
                for (int i = 0; i<beanKPAList.getKPAList().getNumberOfRows(); i++)
                {
                	String strID = beanKPAList.getKPAList().getCell(i,0);
                    String strName = beanKPAList.getKPAList().getCell(i,1);

                      if ((i%2)==0)
                      {
                      %>
                      	<tr class="Row2">
                      		<td width="3%"><input type="checkbox" name="checkBox" value = '<%= strID%>'></td>
                      		<td width="15%">&nbsp;<%= strID%></td>
                      		<td width="82%">&nbsp;<a href="javascript:doUpdate('<%= strID%>', '<%= strName%>')"><FONT color="#0000A0" face="Verdana"><%= strName%></FONT></a></td>
                      </tr>
                      <%
                      }
                      else
                      {
					  %>
					  	<tr class="Row1">
                      		<td width="3%"><input type="checkbox" name="checkBox" value = '<%= strID%>'></td>
					        <td width="15%">&nbsp;<%= strID%></td>
                      		<td width="82%">&nbsp;<a href="javascript:doUpdate('<%= strID%>', '<%= strName%>')"><FONT color="#0000A0" face="Verdana"><%= strName%></FONT></a></td>
                		</tr><%
                      }
                }

            %>
        </table>

	  <!-- ************************************  Form Code Here  ************************************ -->
		<p><input type="button" name="DeleteKPA" class="button" onClick='javascript:doDelete()' value="Delete"></p>

</form>
</body>
</html>
<script language = "Javascript">
  frmKPAList.txtID.focus();
</script>