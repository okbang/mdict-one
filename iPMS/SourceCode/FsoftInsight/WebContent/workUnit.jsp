<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<TITLE>workUnit.jsp</TITLE>
	<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
	<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
	<SCRIPT language="javascript">
		<%@ include file="javaFns.jsp"%>
	</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	int right = Security.securiPage("Work Unit",request,response);
%>
<BODY onload="loadAdminMenu()" class="BD">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.workUnit.Wrokunits")%></p>
<FORM action="Fms1Servlet?reqType=<%=Constants.GET_WORK_UNIT_LIST%>" name="frm" target="main" method="POST">
<%
	Vector vt = (Vector)session.getAttribute("workUnitVector");
	String strPage = request.getParameter("iPage");
	int currentPage;

	if(request.getParameter("pageCombobox") != null) {
		currentPage = Integer.parseInt((String)request.getParameter("pageCombobox"));
	}
	else {
		currentPage = 1;
	}
	String searchName = "";
	int searchType = -1;
	if (session.getAttribute("searchName") != null){
		searchName = (String)session.getAttribute("searchName");
	}
	if (session.getAttribute("searchType") != null){
		searchType = Integer.parseInt((String)session.getAttribute("searchType"));
	}
	int iPage;
	if (ConvertString.isNumber(strPage)) {
		iPage = Integer.parseInt(strPage);
	}
	else {
		iPage = 1;
	}
	
	if (iPage > currentPage) {
		currentPage = iPage;
	}
	else {
		iPage = currentPage;
	}
	int nWU = vt.size();
	int pageCount;
	// 20: is number workunits viewed in a page
	if ((nWU % 20) == 0) {
		pageCount = nWU / 20;
	}
	else {
		pageCount = nWU / 20 + 1;
	}
	if (currentPage > pageCount && pageCount >0)
		currentPage = 1;
	String pageCombobox = "";

	if(pageCount > 0) {
		pageCombobox += "<select name=\"pageCombobox\" onChange=\"this.form.submit();\" class=\"COMBO\">";
		for(int i = 1; i <= pageCount; i++) {
			if(currentPage==i){
				pageCombobox += "<option value='"+i+"' selected>"+i+"</option>";
			}
			else {
				pageCombobox += "<option value='"+i+"'>"+i+"</option>";
			}
		}
		pageCombobox += "</select>";
	}
%>
<BR>
<BR>
<TABLE width="96%">
	<TBODY>
		<TR class="NormalText">
			<TD width="100%"></TD>
			<TD align="left" width="100%"><%=languageChoose.getMessage("fi.jsp.workUnit.SearchType")%></TD>
			<TD align="left" width="100%"><%=languageChoose.getMessage("fi.jsp.workUnit.SearchName")%></TD>
		</TR>
		<TR align="right">
			<TD width="100%"></TD>
			<TD align="right" width="100%"><SELECT name="searchType" class="COMBO">
				<OPTION value="-1"><%=languageChoose.getMessage("fi.jsp.workUnit.All")%></OPTION>
				<OPTION value="0"
					<%=(searchType == 0)?"selected":""%>><%=languageChoose.getMessage("fi.jsp.workUnit.Organization")%></OPTION>
				<OPTION value="1" <%=(searchType == 1)?"selected":""%>><%=languageChoose.getMessage("fi.jsp.workUnit.Group")%></OPTION>
				<OPTION value="2"
					<%=(searchType == 2)?"selected":""%>><%=languageChoose.getMessage("fi.jsp.workUnit.Project")%></OPTION>
			</SELECT></TD>
			<TD align="right" width="100%"><INPUT size="20" type="text"
				name="searchName" value="<%=searchName%>"></TD>
			<TD align="right" width="100%"><INPUT type="submit" class="BUTTON"
				name="searchButton"
				value="<%=languageChoose.getMessage("fi.jsp.workUnit.Search")%>">
			</TD>
		</TR>
	</TBODY>
</TABLE>
<DIV align="left">
<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.workUnit.WrokUnitList")%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD><%=languageChoose.getMessage("fi.jsp.workUnit.WrokUnitName")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.workUnit.Type")%></TD>
        </TR>
   		<%
		if (pageCount == 0) {
		%>
			<TR>
				<TD colspan="8">
					<P class = "ERROR"><%=languageChoose.getMessage("fi.jsp.workUnit.NoWorkUnitMatchesYourCriteria")%></P>
				</TD>
			</TR>
		<%
		} else {
	        boolean bl=true;
	        String rowStyle="";
			for(int i = ((currentPage-1)*20); (i < (currentPage*20))&&(i < nWU); i++){
				WorkUnitInfo wuInfo = (WorkUnitInfo)vt.get(i);
				if (bl) {
					rowStyle = "CellBGRnews";
				}
				else {
					rowStyle = "CellBGR3";
				}
	
				bl = !bl;
				String type = "Organization";
				if (wuInfo.type == 1) {
					type = "Group";
				}
	
				if (wuInfo.type == 2) {
					type = "Project";
				}
	
				if(wuInfo.type == 3){
					type = "Admin";
				}
		%>
	        <TR class="<%=rowStyle%>">
				<%
					if (right == 3) {
				%>
					<TD>
						<A href="Fms1Servlet?reqType=<%=Constants.GET_WORK_UNIT%>&amp;workUnitID=<%=wuInfo.workUnitID%>"><%=ConvertString.toHtml(wuInfo.workUnitName)%></A>
					</TD>
		            <TD width="30%"><%=languageChoose.getMessage(ConvertString.toHtml(type))%></TD>
				<%
				}
				else {
				%>
					<TD>
						<%=ConvertString.toHtml(wuInfo.workUnitName)%></TD>
		            <TD width="30%"><%=ConvertString.toHtml(type)%></TD>
				<%
				}
				%>
	        </TR>
	    <%
			}
		%>
	        <TR class="TableLeft">
				<TD colspan="3" align="right">
					<%if(pageCombobox != "")%>
						<%="Page " + pageCombobox%><%=" of " + pageCount%>
					&nbsp;
					<%if(currentPage > 1){%>
						<A href="workUnit.jsp?iPage=<%=currentPage-1%>"
							class="TableLeft"><%=languageChoose.getMessage("fi.jsp.workUnit.Back")%>
						</A>
					<%}%> 
					<% if (currentPage < pageCount){%>
						<A href="workUnit.jsp?iPage=<%=currentPage+1%>">
							<%=languageChoose.getMessage("fi.jsp.workUnit.Next")%>
						</A>
					<%}%>
				</TD>
	        </TR>
        <%}%>
    </TBODY>
</TABLE>
</DIV>
<BR>
<%
if(right == 3){
%>
	<DIV align="left">
		<INPUT type = "submit" name = "AddOrganization" value = "<%=languageChoose.getMessage("fi.jsp.workUnit.AddOrganization")%>" class = "BUTTON" onClick="addOrg()">
		<INPUT type = "submit" name = "AddGroup" value = "<%=languageChoose.getMessage("fi.jsp.workUnit.AddGroup")%>" class = "BUTTON" onClick = "addGroup()">
		<INPUT type = "submit" name = "AddProject" value = "<%=languageChoose.getMessage("fi.jsp.workUnit.AddProject")%>" class = "BUTTON" onClick = "addProject()">
	</DIV>
<%
}
%>
</FORM>
<SCRIPT language="javascript">
  function addOrg(){
  	frm.action = "workUnitAddOrg.jsp";
  	frm.submit();
  }
  
  function addGroup(){
  	frm.action = "workUnitAddGroup.jsp";
  	frm.submit();
  }
  
  function addProject(){
  	frm.action = "workUnitProjectAdd.jsp";
 	frm.submit();
  }

</SCRIPT>
</BODY>
</HTML>