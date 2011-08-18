<%@page import="com.fms1.infoclass.group.*,java.util.*, com.fms1.tools.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp?error=Please re-login" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>groupHome.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY  class="BD" onload="loadBlankMenu()">
<%
Vector groupList=(Vector)request.getAttribute("groups");
%>

<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.groupHome.GroupHome")%></p>
<TABLE cellspacing="1" width="60%" class="Table">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.groupHome.Pleaseselectagroup")%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD><%=languageChoose.getMessage("fi.jsp.groupHome.GroupName")%></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.groupHome.Type")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.groupHome.Leader")%> </TD>
        </TR>
        <%String classStyle = null;
        GroupInfo inf;
    	for (int i = 0; i < groupList.size(); i++) {
     		inf= (GroupInfo)groupList.elementAt(i);
 			classStyle =(i%2 == 0)? "CellBGRnews":"CellBGR3";
		%>
        <TR class="<%=classStyle%>">
            <TD><A href="javascript:doIt('<%=Constants.WORKUNIT_HOME%>&workUnitID=<%=inf.wuID%>')"><%=inf.name%></A></TD>
            <TD><%=(inf.isOperation? languageChoose.getMessage("fi.jsp.groupHome.Operation"): languageChoose.getMessage("fi.jsp.groupHome.Support"))%></TD>
            <TD><%=CommonTools.updateString(inf.leader)%></TD>
        </TR>
        <%}%>
        <TR>
            <TD colspan="3" class="TableLeft">&nbsp;</TD>
        </TR>
    </TBODY>
</TABLE>
</BODY>
</HTML>
