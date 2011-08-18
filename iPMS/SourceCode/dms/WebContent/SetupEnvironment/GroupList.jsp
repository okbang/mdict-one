<%@ page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*, fpt.dms.bean.SetupEnvironment.*, fpt.dms.framework.util.StringUtil.*" %>
<%@ page isThreadSafe="false" errorPage="error.jsp" %>
<%
	UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
	GroupListBean beanGroupList = (GroupListBean)request.getAttribute("beanGroupList");
%>

<html>
<head>
	<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
	<SCRIPT src='scripts/validate.js'></SCRIPT>
	<SCRIPT src='scripts/utils.js'></SCRIPT>

	<!-- Html Form Title -->
	<title>Group</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<link rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">

<SCRIPT language = "javascript">
function doAddNew()
{
	var form = document.frmGroupList;

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
	form.hidActionDetail.value = "AddGroup";
	form.action = "DMSServlet";
	form.submit();
}

function doDelete()
{
	var form = document.frmGroupList;

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
	form.hidActionDetail.value = "DeleteGroup";
	form.action = "DMSServlet";
	form.submit();
}

function doUpdate(name)
{
	var form = document.frmGroupList;

	form.hidName.value = name;

  	form.hidAction.value = "SE";
	form.hidActionDetail.value = "UpdateGroup";
	form.action = "DMSServlet";
	form.submit();
}

function CheckAll()
{
	var form = document.frmGroupList;
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

<div ><p><img border="0" src="Images/GroupListing.gif" width="411" height="28"></p></div>


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
	<form method="post" action = "DMSServlet" name="frmGroupList">
	  <!-- ************************************  Standard Screen Identification  ************************************ -->
		<input TYPE = "hidden" NAME = "hidActionDetail" VALUE = "">
    	<input TYPE = "hidden" NAME = "hidAction" VALUE = "">


       	  <!-- ************************************  Form Code Here  ************************************ -->
			<table border="0" width="100%" class="TblOut2">
                <tr>
                  <td width="9%"><b>User:</b> </td>
                  <td width="25%"><%=beanUserInfo.getUserName()%></td>

                  <td width="10%"><b>Group:</b></td>
                  <td width="28%"><%=beanUserInfo.getGroupName()%></td>

				  <td width="12%"><b>Login Date:</b> </td>
                  <td width="17%"><%=beanUserInfo.getDateLogin()%></td>
                </tr>
             </table>
		  <p></p>
          <table border="0" width="60%" cellspacing="0" cellpadding="0">
            <tr>
              <td width="5%"><b>Name:</b></td>
              <td width="34%"><input type="text" name="txtName" size="40" maxLength="30"></td>
              <td width="21%" align="left">
                <p><input type="button" value="AddNew" name="AddNewGroup" class="button"
                	 onClick='javascript:doAddNew()'>
                </p>

              </td>
            </tr>
          </table>
		  <p></p>
<!-- Preparing to update -->
 			<input type="hidden" name="hidName" value="" >

          <table border="0" cellspacing="1" cellpadding="0" width="100%" bgcolor="#000000">
              <tr class="Row0">
                <td width="3%"  height="19"><input type="checkbox" name="allbox" value="CheckAll" onClick="JavaScript: CheckAll();"></td>
                <td align = "center" width="97%" height="19">Name</a></td>
              </tr>

            <%
                for (int i = 0; i<beanGroupList.getGroupList().getNumberOfRows(); i++)
                {
                      String strName = beanGroupList.getGroupList().getCell(i,0);

                      if ((i%2)==0)
                      {
                    %><tr class="Row2">
							<td width="3%"><input type="checkbox" name="checkBox" value='<%= strName%>'></td>
							<td width="97%">&nbsp;<a href="javascript:doUpdate('<%= strName%>')"><FONT color="#0000A0" face="Verdana"><%= strName%></FONT></a></td>
                      	</tr><%
                      }
                      else
                      {
					  %>
					  	<tr class="Row1">
                      		<td width="3%"><input type="checkbox" name="checkBox" value='<%= strName%>'></td>
                      		<td width="97%">&nbsp;<a href="javascript:doUpdate('<%= strName%>')"><FONT color="#0000A0" face="Verdana"><%= strName%></FONT></a></td>
                      	</tr><%
                      }
                }
            %>
        </table>

	  <!-- ************************************  Form Code Here  ************************************ -->
		<p><input type="button" name="DeleteGroup" class="button" onClick='javascript:doDelete()' value="Delete"></p>

</form>
</body>
</html>
<script language = "Javascript">
  frmGroupList.txtName.focus();
</script>
