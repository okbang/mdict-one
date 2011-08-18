<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>conversionDetail.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
int right;
int level = Integer.parseInt((String)session.getAttribute("workUnitType"));
String menu;
if (level == Constants.RIGHT_ADMIN){ //called from admin section
	right=Security.securiPage("Parameters",request,response);
	menu="loadAdminMenu";
}
else{ //called from project
	right=Security.securiPage("Project parameters",request,response);
	menu="loadPrjMenu";
}
	
	String temp=request.getParameter("languageNo");
	int languageNo;	
	//check for bad parameters
	if ((temp==null)||(temp.equals("0"))||!ConvertString.isNumber(temp))
		languageNo=1;
	else
		languageNo=Integer.parseInt(temp);
	Vector conversionList= (Vector)session.getAttribute("conversionList");
	String[] languageDetail=(String[])conversionList.elementAt(languageNo);
	String[] Header=(String[])conversionList.elementAt(0);
	String strSizeUnit = languageDetail[1];
%>
<BODY onload="<%=menu%>()" class="BD"> 
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.conversionDetail.Conversiontable")%> </P>
<TABLE class="Table" cellspacing="1" width="560">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.conversionDetail.Conversionview")%> </CAPTION>
    <COL span="1" width="160">
    <COL span="1" width="400">
    <TBODY>
        <%for (int i = 0; i < 2; i++) {
       	%><TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage(Header[i])%></TD>
            <TD class="CellBGR3"><%=languageDetail[i]%></TD>
        </TR>
        <%}%>
        <%for (int i = 3; i < Header.length; i++) {
       	%><TR>
            <TD class="ColumnLabel"><%=Header[i]%></TD>
            <TD class="CellBGR3"><%=languageDetail[i]%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<FORM name="frm" action="Fms1Servlet">
<P>
<%
if ((level == Constants.RIGHT_ADMIN)&&(right ==3)) {
%>
<INPUT type="button" name="update" value=" <%=languageChoose.getMessage("fi.jsp.conversionDetail.Update")%> " class="BUTTON" onclick="onUpdate()">
<INPUT type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.conversionDetail.Delete")%> " class="BUTTON" onclick="onDelete()">
<%
}
%>
<INPUT type="button" name="back" value=" <%=languageChoose.getMessage("fi.jsp.conversionDetail.Back")%> " class="BUTTON" onclick="doIt(<%=Constants.CONVERSION_LIST_INIT%>)"></P>
</FORM>
<%
if ((level == Constants.RIGHT_ADMIN)&&(right ==3)) {
%>
<SCRIPT language="javascript">
function onUpdate() {
	location = "conversionUpdate.jsp?languageNo=<%=languageNo%>";
}

function onDelete() {
	if (window.confirm("<%=languageChoose.getMessage("fi.jsp.conversionDetail.AreYouSureToDeleteThisLanguage")%>") != 0) {
		location =  "Fms1Servlet?reqType=<%=Constants.CONVERSION_DELETE%>&languageNo=<%=languageNo%>&sizeUnit=<%=strSizeUnit%>";
	}
}
</SCRIPT>
<%}%>
</BODY>
</HTML>
