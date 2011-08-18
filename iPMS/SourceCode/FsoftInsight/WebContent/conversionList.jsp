<%@page import="com.fms1.infoclass.*, java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>ConversionList.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>

<%
int right=0;
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
Vector conversionList = (Vector)session.getAttribute("conversionList");
String strPage=request.getParameter("iPage");
int iPage;
if (ConvertString.isNumber(strPage))
	iPage= Integer.parseInt(strPage);
else
	iPage=1; 
String searchedName=request.getParameter("searchedName");
String[] header=(String[])conversionList.elementAt(0);
int nColumns=header.length-1;
int nLanguages = conversionList.size()-1;

final int pageCount;
if ((nLanguages % 20) == 0) {
	pageCount = nLanguages / 20;
}
else {
	pageCount = nLanguages / 20 + 1;
}
if ((iPage<1) && (iPage>nLanguages))
	Fms1Servlet.callPage("error.jsp?error=Wrong page number",request,response);
String lastUpdate=request.getParameter("convLastUpdate");
Vector errors=(Vector)request.getAttribute("error");

%>
<BODY onload="<%=menu%>()" class="BD"> 
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.conversionList.Conversiontable")%> </P>
<P class="HDR"> <%=languageChoose.getMessage("fi.jsp.conversionList.Lastupdate")%>  <%=(lastUpdate==null)? "N/A":lastUpdate%></P>
<%
if (errors!=null){%>
<P class="ERROR"> <%=languageChoose.getMessage("fi.jsp.conversionList.Couldnotdeletethelanguagebecau")%> <BR>
<%
String [] strInf;
for(int i=0;i<errors.size();i++){
strInf=(String[])errors.elementAt(i);
%>
<%=strInf[1]+": "+strInf[0]%><BR>
<%}%>
</P>
<%}%>
<FORM action="Fms1Servlet" name="frm">
<P class="HDR" style="text-indent: 250px"> <%=languageChoose.getMessage("fi.jsp.conversionList.LanguageName")%>  <INPUT size="20" type="text" maxlength="50" name="searchedLangName" value="<%=(searchedName==null)?"":searchedName%>"><INPUT type="button" class="BUTTON" name="search" value=" <%=languageChoose.getMessage("fi.jsp.conversionList.Search")%> " onclick="onSearch()"></P>
<P><%=(searchedName==null)?"": languageChoose.paramText(new String[]{"fi.jsp.conversionList.Results__for__language__names__like__~PARAM1_STRING~",searchedName})%>
<TABLE class="Table" cellspacing="1" width="95%">
    <COL span="1" width="10%">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.conversionList.Conversiontable1")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <%for (int i = 0; i < 2; i++) {
            //do not display note column
            %><TD><%=languageChoose.getMessage(header[i])%></TD>
            <%}%>
            <%for (int i = 3; i < nColumns; i++) {
            if (i==2) i++;//do not display note column
            %><TD><%=header[i]%></TD>
            <%}%>
        </TR>
        <%String className;
        String[] row;
        for (int i =1+(iPage-1)*20; (i < conversionList.size())&&(i<(iPage-1)*20+20); i++) {
        	row=(String[])conversionList.elementAt(i);
			className =(i%2 == 0)? "CellBGRnews":"CellBGR3";					
        %><TR class="<%=className%>">
			<TD ><A href="conversionDetail.jsp?languageNo=<%=i%>"><%=row[0]%></A></TD>
            <%for (int j = 1; j < nColumns; j++) {
            if (j==2) j++;//do not display note column
            %><TD align="left"><%=row[j]%></TD>
            <%}%>
        </TR>
        <%}%>
        <TR align="right">
            <TD colspan="<%=nColumns%>" class="TableLeft" > <%=languageChoose.getMessage("fi.jsp.conversionList.Page")%>  <%=iPage%>/<%=pageCount%>
				<%if (iPage > 1) {%>
				<A href="conversionList.jsp?iPage=<%=iPage-1%><%=(searchedName==null)?"":"&searchedName="+searchedName%><%=(lastUpdate==null)?"":"&convLastUpdate="+lastUpdate%>"> <%=languageChoose.getMessage("fi.jsp.conversionList.Prev")%> </A> 
				<%}if (iPage < pageCount) {%>
				<A href="conversionList.jsp?iPage=<%=iPage+1%><%=(searchedName==null)?"":"&searchedName="+searchedName%><%=(lastUpdate==null)?"":"&convLastUpdate="+lastUpdate%>"> <%=languageChoose.getMessage("fi.jsp.conversionList.Next")%> </A> 
				<%}%>
			</TD>
        </TR>
    </TBODY>
</TABLE>
<P>
<%//languages can only be updated from admin section
if ((level == Constants.RIGHT_ADMIN) && (right ==3)) {
%>
<INPUT type="button" class="BUTTON" onclick="onAddnew()" name="addnew" value=" <%=languageChoose.getMessage("fi.jsp.conversionList.Addnew")%> "></P>
<%}%>

<INPUT type="hidden" name="reqType" value = "<%=Constants.CONVERSION_LIST_NAME%>">
</FORM>
<SCRIPT language="javascript">
<%//languages can only be updated from admin section
if ((level == Constants.RIGHT_ADMIN) && (right ==3)) {
%>
function onAddnew() {
	window.location= "conversionAddNew.jsp";
}
<%}%>
function onSearch() {
	frm.reqType.value = "<%=Constants.CONVERSION_LIST_NAME%>";
	frm.submit();
}
</SCRIPT>
</BODY>
</HTML>
