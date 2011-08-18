<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<TITLE> rightGroup.jsp</TITLE>
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
%>
<BODY onload="loadAdminMenu()" class="BD">
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.rightGroup.Roles")%></p>
<FORM name="frm" action="Fms1Servlet?reqType=<%=Constants.GET_PAGE_LIST%>" method="POST">
<%
Vector vt=(Vector)session.getAttribute("getRightGroupVector");
if (vt!=null) {
%>
<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.rightGroup.RoleList") %></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.rightGroup.Rolename")%></TD>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.rightGroup.Description")%></TD>
        </TR>
<%
String className = "";
boolean bl=true;

for(int i=0;i<vt.size();i++){
	RightGroupInfor rgInfor=(RightGroupInfor)vt.get(i); 
   	className=(bl)?"CellBGRnews":"CellBGR3";
  	bl=!bl;
%>      <TR>
            <TD class="<%=className%>"><A href="Fms1Servlet?reqType=<%=Constants.GET_RIGHT_GROUP%>&rightGroupID=<%=rgInfor.rightGroupID%>"><%=ConvertString.toHtml(rgInfor.rightGroupID)%></A>
            </TD>
            <TD class="<%=className%>"><%=ConvertString.toHtml(rgInfor.description)%></TD>
        </TR>
<%}%>
        <TR class="ColumnLabel">
            <TD colspan="2" class="TableLeft">&nbsp;</TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<%
}
if(right == 3){%>
<INPUT type="submit" name="addRightGroup" value="<%=languageChoose.getMessage("fi.jsp.rightGroup.Addnew") %>" class="BUTTON">
<%}%>
</FORM>
</BODY>
</HTML>
