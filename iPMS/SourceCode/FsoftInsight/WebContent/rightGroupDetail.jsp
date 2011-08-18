<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>rightGroupDetail.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right = Security.securiPage("Roles",request,response); 
	Vector vt=(Vector)session.getAttribute("getRightForPageVector");
	RightGroupInfor rgInfor=(RightGroupInfor)session.getAttribute("getRightGroup");
%>
<BODY onload="loadAdminMenu()"  class="BD">
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Roles") %></P>
<FORM method="POST" name="frm">
<BR>
<TABLE cellspacing="1" class="Table" width="80%">
	<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Roledetails") %></CAPTION>
	<TBODY align="center">
		<TR>
			<TD colspan="6" class="ColumnLabel"><B><%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Roleinformation") %></B></TD>
		</TR>
		<TR>
			<TD colspan="5" class="CellBGR3" align="left"><%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Rolename")%></TD>
			<TD class="CellBGRnews" align="left"><%=ConvertString.toHtml(rgInfor.rightGroupID)%>
			<INPUT size="20" type="hidden" name="rightGroupName"
				value="<%=rgInfor.rightGroupID%>"></TD>
		</TR>
		<TR>
			<TD colspan="5" class="CellBGR3" align="left"><%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Description")%></TD>
			<TD class="CellBGRnews" align="left"><%=ConvertString.toHtml(rgInfor.description)%></TD>
		</TR>
		<TR>
			<TD colspan="6" class="ColumnLabel"><B><%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Rolemanagement")%></B></TD>
		</TR>
		<TR>
			<TD colspan="6" class="CellBGR3" align="left"><%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Mode")%> <B> 1 </B>:
			<%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Noright")%><BR>
			<%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Mode")%> <B>  2 </B>: <%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Viewonlyright")%><BR>
			<%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Mode")%> <B> 3 </B>: <%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Managerright")%></TD>
		</TR>
		<TR>
			<TD colspan="3" class="ColumnLabel"><B><%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Mode")%></B></TD>
			<TD rowspan="2" colspan="3" class="ColumnLabel"><B><%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Page")%></B></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> 1 </TD>
			<TD class="ColumnLabel"> 2 </TD>
			<TD class="ColumnLabel"> 3 </TD>
		</TR>
<%
String className;
boolean bl=true; 
for(int i=0;i<vt.size();i++){		 	
	RightForPageInfor rfpInfor=(RightForPageInfor)vt.elementAt(i);	
	className=(bl)?"CellBGRnews":"CellBGR3";
%>
		<TR>
			<TD class="<%=className%>"><INPUT type="radio" name="<%=i+1%>"
				value="1" <%if (rfpInfor.privilege==1){%> checked <%}%> readonly
				disabled></TD>
			<TD class="<%=className%>"><INPUT type="radio" name="<%=i+1%>"
				value="2" <%if (rfpInfor.privilege==2){%> checked <%}%> readonly
				disabled></TD>
			<TD class="<%=className%>"><INPUT type="radio"
				name="<%=i+1%>" value="3" <%if (rfpInfor.privilege==3){%> checked
				<%}%> readonly disabled></TD>
	<%if(rfpInfor.pageName2.lastIndexOf("home") !=-1) {//left column for home pages%>
			<TD align="left" class="<%=className%>" colspan="2"><%=rfpInfor.pageName2%></TD>
			<TD  class="<%=className%>">&nbsp;</TD>
	<%}else{%>
			<TD align="left" class="<%=className%>" colspan="2">&nbsp;</TD>
			<TD align="left" class="<%=className%>" ><%=rfpInfor.pageName2%></TD>
	<%}%>
		</TR>
<%}%>
	</TBODY>
</TABLE>
<BR>
<%if((right == 3)&&(rgInfor.protection!=rgInfor.FULL_PROTECT)){%>
<INPUT type="button" name="Update" value="<%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Update") %>" onclick="doAction(this)" class="BUTTON">
<%if(rgInfor.protection!=rgInfor.PARTIAL_PROTECT){%>
<INPUT type="button" name="Delete" value="<%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Delete") %>" onclick="doAction(this)" class="BUTTON">
<%}
}%>
<INPUT type="button" name="Back" value="<%=languageChoose.getMessage("fi.jsp.rightGroupDetail.Back") %>" onclick="doAction(this)" class="BUTTON">
</FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="Update"){  				
  		frm.action="rightGroupUpdate.jsp";
  		frm.submit();
  	}
  	if (button.name=="Delete"){
  		if(window.confirm("<%=languageChoose.getMessage("fi.jsp.rightGroupDetail.AreyousuretodeletethisRole")%>")){
  			frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_RIGHT_GROUP %>";
  			frm.submit();
  		}
  		else{
  			return;
  		}
  	}
  	if (button.name=="Back") 
  		doIt(<%=Constants.GET_RIGHT_GROUP_LIST%>);
  }
 </SCRIPT> 
</BODY>
</HTML>
