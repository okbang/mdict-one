<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>methodList.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.methodList.EstimationMethods")%> </P>
<%
int right=0;
String menu;
int level = Integer.parseInt((String)session.getAttribute("workUnitType"));

if (level == Constants.RIGHT_ADMIN){ //called from admin section
	right=Security.securiPage("Parameters",request,response);
	menu="loadAdminMenu";
}
else{ //called from project
	right=Security.securiPage("Project parameters",request,response);
	menu="loadPrjMenu";
}
Vector methodList = (Vector)session.getAttribute("methodList");

%>
<BODY onload="<%=menu%>()" class="BD">
<TABLE cellspacing="1" class="Table" width="95%">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.methodList.EstimationMethods1")%> </CAPTION>
    <COL span="1" width="3%" align="center">
    <COL span="1" width="46%">
    <COL span="1" width="46%">
    <TBODY>
        <TR class="ColumnLabel">
            <TD>#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.methodList.MethodName")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.methodList.Description")%> </TD>
        </TR>
        <%
        for (int i = 0; i < methodList.size(); i++) {
        	EstimationMethodInfo methodInfo = (EstimationMethodInfo)methodList.elementAt(i);
        	String className;
			if (i%2 == 0) 
				className = "CellBGRnews";
			else 
				className = "CellBGR3";
        %>
        <TR class="<%=className%>">
            <TD><%=i+1%></TD>
            <TD>
            <%
            if ((level == Constants.RIGHT_ADMIN)&&(right==3)) {
            %>
            <A href="methodUpdate.jsp?methodID=<%=i%>"><%=methodInfo.name%></A>
            <%
            }else{
            %>
            <%=methodInfo.name%>
            <%
            }
            %>
            </TD>
            <TD><%if (methodInfo.note != null) {%><%=methodInfo.note%><%} else {%> N/A <%}%></TD>
        </TR>
        <%
        }
        %>
    </TBODY>
</TABLE>
<P>
<%if ((level == Constants.RIGHT_ADMIN)&&(right==3)){%>
<FORM action="methodAddnew.jsp" name="frm">
<INPUT type="submit" class="BUTTON" name="addnew"  value=" <%=languageChoose.getMessage("fi.jsp.methodList.Addnew")%> ">
</FORM>
<%}%>
<P/>
</BODY>
</HTML>
