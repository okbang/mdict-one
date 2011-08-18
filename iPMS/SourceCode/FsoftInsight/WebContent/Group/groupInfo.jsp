<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.*,java.util.Calendar,com.fms1.tools.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<%	int right=Security.securiPage("Group home",request,response);
	GroupInfo inf=(GroupInfo)session.getAttribute("groupInfo");
%>
<TITLE>GroupInfo</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY  class="BD" onload="<%=CommonTools.getMnuFunc(session)%>">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.groupInfo.Groupinformation")%> </P>
<P><BR></P>
<TABLE cellspacing="1" class="Table" width = "95%">
	<TBODY>
		<TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.groupInfo.Groupleader")%> </TD>
            <TD class="CellBGR3"><%=CommonTools.formatString(inf.leader)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.groupInfo.Groupdescription")%> </TD>
            <TD class="CellBGR3"><%=(inf.desc==null?"N/A":ConvertString.toHtml(inf.desc))%></TD>
        </TR>
	</TBODY>
</TABLE>
<P><BR></P>
<%if (right==3) {%>
<INPUT type="BUTTON" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.groupInfo.Update")%> " onclick="doIt('<%=Constants.GET_PAGE%>&page=Group/groupInfoUpdate.jsp')">
<%}%>
<p>
</BODY>
</HTML>
