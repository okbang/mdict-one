<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" errorPage="error.jsp?error=Please re-login" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>orgPointUpdate.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadAdminMenu()" class="BD">
<%
	int right = Security.securiPage("Org Point",request,response);
%>

<p class="TITLE"> <%=languageChoose.getMessage("fi.jsp.orgPointUpdate.ManageFSOFTgrouppoint")%> </p>
<DIV align="left"><%
        Vector orgList = (Vector)session.getAttribute("orgList");
        Vector groupList = (Vector)session.getAttribute("groupList");
        if ((groupList != null)&&(groupList.size()!=0)) {
        %>
<TABLE cellspacing="1" width="60%" class="Table">
    <CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.orgPointUpdate.PleaseselectaOrganizationGroup")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.orgPointUpdate.OrganizationGroupName")%> </TD>
        </TR>
        <%
        	for (int i = 0; i < orgList.size(); i++) {
         		RolesInfo ru = (RolesInfo)orgList.elementAt(i);
         		String classStyle = "";
         		if (i%2 == 0)
         			classStyle = "CellBGRnews";
         		else
         			classStyle = "CellBGR3";
		%>
        <TR class="<%=classStyle%>">
            <TD><A href="Fms1Servlet?reqType=<%=Constants.UPDATE_GROUP_POINT%>&workUnitID=<%=ru.workUnitID%>"><%=ru.workunitName%></A></TD>
        </TR>
        <%
			}
		%>

        <%
        	for (int i = 0; i < groupList.size(); i++) {
         		RolesInfo ru = (RolesInfo)groupList.elementAt(i);
         		String classStyle = "";
         		if (i%2 == 0)
         			classStyle = "CellBGRnews";
         		else
         			classStyle = "CellBGR3";
         		
         		if (
         			(ru.workunitName.equalsIgnoreCase(Parameters.PQA_ROLE)) ||
         			(ru.workunitName.equalsIgnoreCase(Parameters.SQA_ROLE)) ||
         			(ru.workunitName.equalsIgnoreCase(Parameters.TMG_ROLE)) ||
         			(ru.workunitName.equalsIgnoreCase(Parameters.ADMIN_ROLE)) ||
         			(ru.workunitName.equalsIgnoreCase(Parameters.DDC_ROLE)) ||
         			(ru.workunitName.equalsIgnoreCase(Parameters.FIST_ROLE)) ||
         			(ru.workunitName.equalsIgnoreCase(Parameters.FWB_ROLE)) ||
         			(ru.workunitName.equalsIgnoreCase(Parameters.JCT_ROLE)) ||
         			(ru.workunitName.equalsIgnoreCase(Parameters.SEPG_ROLE)) 
         			)
         			{
         				// Will do point later
         			}
         		else
         		{
		%>
        <TR class="<%=classStyle%>">
            <TD><A href="Fms1Servlet?reqType=<%=Constants.UPDATE_GROUP_POINT%>&workUnitID=<%=ru.workUnitID%>"><%=ru.workunitName%></A></TD>
        </TR>
        <%
				}
			}
		%>
    </TBODY>
</TABLE>
		<%
		}
		else
		{
		%>
			<P class="ERROR"><%=languageChoose.getMessage("fi.jsp.orgPointUpdate.Youhavenorightinthispage")%></P>
		<%
		}
		%>
</DIV>

<BR>
<!--
<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="text" name="reqType" value="<%=Constants.UPDATE_GROUP_POINT2%>">
<INPUT type="text" name="reqType" value="<%=Constants.UPDATE_GROUP_POINT3%>">
<INPUT type="submit" name="btnViewOP" value="OP Groups" class="BUTTON">
<INPUT type="submit" name="btnViewBA" value="BA Groups" class="BUTTON">
-->
<FORM method="post" name="frmPointUpdate">
<INPUT type="button" name="btnViewOP" value=" <%=languageChoose.getMessage("fi.jsp.orgPointUpdate.OPGroups")%> " onclick="doAction(this);" class="BUTTON">
<INPUT type="button" name="btnViewBA" value=" <%=languageChoose.getMessage("fi.jsp.orgPointUpdate.BAGroups")%> " onclick="doAction(this);" class="BUTTON">
<TABLE class="Note">	
	<tr><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td></tr>	
	<tr>
		<td> <%=languageChoose.getMessage("fi.jsp.orgPointUpdate.OPGroupsOperationGroups")%> </td>
	</tr>
	<tr>
		<td> <%=languageChoose.getMessage("fi.jsp.orgPointUpdate.BAGroupsBusinessAssuaranceGrou")%>  </td>	
	</tr>
</TABLE>
</FORM>

<SCRIPT language="javascript">
function doAction(button)
{  	
	if (button.name == "btnViewOP")
	{
			frmPointUpdate.action = "Fms1Servlet?reqType=<%=Constants.UPDATE_GROUP_POINT2%>";
			frmPointUpdate.submit();
	}
	if (button.name == "btnViewBA")
	{
			frmPointUpdate.action = "Fms1Servlet?reqType=<%=Constants.UPDATE_GROUP_POINT3%>";
			frmPointUpdate.submit();
	}
}
</SCRIPT> 

</BODY>
</HTML>
