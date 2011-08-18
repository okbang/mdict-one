<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.fms1.infoclass.*,java.util.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>UserProfiles.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadAdminMenu()" class="BD">
<%
int right = Security.securiPage("User Profiles",request,response);
String searchRule=request.getParameter("searchRule");
String searchName=request.getParameter("searchName");
String strPage=request.getParameter("iPage");
int iPage;
if (ConvertString.isNumber(strPage))
	iPage= Integer.parseInt(strPage);
else
	iPage=1;
	
Vector result = (Vector) session.getAttribute("userList");
int nUsers = result.size();

final int pageCount;
if ((nUsers % 20) == 0) {
	pageCount = nUsers / 20;
}
else {
	pageCount = nUsers / 20 + 1;
}
if ((iPage<1) || ((pageCount >0)&&(iPage>pageCount)))
	Fms1Servlet.callPage("error.jsp?error=Wrong page number",request,response);
	
String searched;
if ((searchRule==null) ||(searchName==null) ||(searchName.equals(""))||(searchRule.equals(""))){
	searchRule=""; //avoir null pointer exception when testing string
	searchName="";
	searched="";
}
else if (pageCount==0)
	searched = languageChoose.paramText(new String[]{"fi.jsp.UserProfiles.No__results__for__~PARAM1_SEARCHRULE~__like__~PARAM2_SEARCHNAME~", searchRule, searchName});
else
	searched = languageChoose.paramText(new String[]{"fi.jsp.UserProfiles.Results__for__~PARAM1_SEARCHRULE~__like__~PARAM2_SEARCHNAME~", searchRule, searchName});
	
%>

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.UserProfiles.UserProfiles")%></P>
<DIV align="left">
<TABLE align="center" width="90%">
    <TBODY>
        <TR>
			<TD colspan="3" align="right" bgcolor="#ffffff" width="80%"><BR>
			<FORM action="Fms1Servlet?reqType=<%=Constants.SEARCH_USER_PROFILES%>" method="post">
				<SELECT name="searchRule"	class="COMBO">
					<OPTION value="NAME" <%=searchRule.equals("NAME")?"selected":""%>><%=languageChoose.getMessage("fi.jsp.UserProfiles.Name")%></OPTION>
					<OPTION value="ID" <%=searchRule.equals("ID")?"selected":""%>><%=languageChoose.getMessage("fi.jsp.UserProfiles.ID")%></OPTION>
				</SELECT> 
				<INPUT size="20" type="text" name="searchName" value="<%=searchName%>">
				<INPUT	type="submit" name="searchButton" value="<%=languageChoose.getMessage("fi.jsp.UserProfiles.Search")%>" class="BUTTON">
			</FORM>
			</TD>
		</TR>
    </TBODY>
</TABLE>
</DIV>
<%=searched%>
<DIV align="left">
<TABLE cellspacing="1" class="Table" width="95%">
    <COL span="2">
    <COL span="1" width="275">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.UserProfiles.UserProfiles")%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel" >
            <TD width="10%"><%=languageChoose.getMessage("fi.jsp.UserProfiles.ID")%></TD>
            <TD width="20%"><%=languageChoose.getMessage("fi.jsp.UserProfiles.UserName")%></TD>
            <TD width="15%"><%=languageChoose.getMessage("fi.jsp.UserProfiles.Position")%></TD>
            <TD width="10%"><%=languageChoose.getMessage("fi.jsp.UserProfiles.Status")%></TD>
            <TD width="30%"><%=languageChoose.getMessage("fi.jsp.UserProfiles.Workunits")%></TD>
        </TR>
     <%UserInfo info ;
     String className;
	for(int i = ((iPage-1)*20); (i < (iPage*20))&&(i<nUsers); i++){
		info = (UserInfo) result.elementAt(i);
		className= (i%2==0) ?"CellBGRnews":"CellBGR3";
	%> <TR>
            <TD class="<%=className%>" width="10%"><A href="Fms1Servlet?reqType=<%=Constants.GET_USER_PROFILE%>&id=<%=info.developerID%>">
            <%=info.account%></A></TD>
            <TD class="<%=className%>" width="20%"><%=info.Name%></TD>
            <TD class="<%=className%>" width="15%"><%=((info.designation==null)?"N/A":info.designation)%></TD>
            <TD class="<%=className%>" width="10%"><%
            if (info.status !=null){
            	switch (Integer.parseInt(info.status)) {
            		case 1:%> <%=languageChoose.getMessage("fi.jsp.UserProfiles.Staff")%> <%break;
            		case 2:%> <%=languageChoose.getMessage("fi.jsp.UserProfiles.Collaborator")%> <%break;
            		case 3:%> <%=languageChoose.getMessage("fi.jsp.UserProfiles.External")%> <%break;
            		case 4:%> <%=languageChoose.getMessage("fi.jsp.UserProfiles.OutPlaced")%> <%break;
            	}
            }
            %>
            </TD>
            <TD class="<%=className%>" width="30%"><%=info.wuNote%></TD>
        </TR>
	<%}%>
        <TR class="submenu">
            <TD align="right" colspan="5" class="TableLeft"><%=languageChoose.getMessage("fi.jsp.UserProfiles.Page")%>: <%=iPage%>/<%=pageCount%> 
<%if(iPage>1){%>   
            <A href="UserProfiles.jsp?iPage=<%=iPage-1%>"><%=languageChoose.getMessage("fi.jsp.UserProfiles.Prev")%></A>
<%}if(iPage<pageCount){%>
			<A href="UserProfiles.jsp?iPage=<%=iPage+1%>"><%=languageChoose.getMessage("fi.jsp.UserProfiles.Next")%></A>			
<%}%>

</TD>
        </TR>
    </TBODY>
</TABLE>
<BR>

<% if(right == 3){ %>

<P class="ERROR">To add new a user, please use correlative functions of <a href="/dashboard/DashboardServlet" target="/d">dashboard</a></P>

<BR>

<P align="left"><INPUT type="button" name="mySubmit" value="<%=languageChoose.getMessage("fi.jsp.UserProfiles.Addnew")%>" class="BUTTON" onClick="window.open('/dashboard/DashboardServlet')"></P>

<BR>

<%}%>
</DIV>

</BODY>
</HTML>