<%@page import="java.util.Vector,com.fms1.infoclass.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.common.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<TITLE>ProjectPointPage.jsp</TITLE>
<SCRIPT language="JavaScript">
	function doOnload(){
		window.open("header.jsp","header");
		loadOrgMenu();
	}
	
</SCRIPT>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad="doOnload();" class="BD">


<%

	WorkUnitInfo wuInfo = (WorkUnitInfo) session.getAttribute("workUnit");
	WorkUnitInfo wuInfo1 = (WorkUnitInfo) session.getAttribute("workUnit1");
	String year=(String) request.getParameter("selectYear");
	String month=(String) request.getParameter("selectMonth");
	String CurrentMonth = (String) session.getAttribute("CurrentMonth");
	String PreviousMonth1 = (String) session.getAttribute("PreviousMonth1");

	Vector vtProject1 = (Vector) session.getAttribute("vtProject1");
	Vector vtProject2 = (Vector) session.getAttribute("vtProject2");

	Vector vtProjectPoint1 = (Vector) session.getAttribute("vtProjectPoint1");
	Vector vtProjectPoint2 = (Vector) session.getAttribute("vtProjectPoint2");
	
	Vector vtGroupRole = (Vector) session.getAttribute("groupList");
	Vector vtOrgRole = (Vector) session.getAttribute("orgList");

	String strProjectType = (String) session.getAttribute("cboProjectType");
	String strProjectCategory = (String) session.getAttribute("cboProjectCategory");
	String strProjectStatus = (String) session.getAttribute("cboProjectStatus");
	
	if (strProjectType == null)
		strProjectType = "-1";

	if (strProjectCategory == null)
		strProjectCategory = "-1";

	if (strProjectStatus == null)
		strProjectStatus = "-1";

%> 
<% if (wuInfo1.workUnitName==null) {%>
<P class="TITLE"><%=languageChoose.paramText(new String[]{"fi.jsp.ProjectPointPage.~PARAM1_NAME~projectpoints",(String)session.getAttribute("workUnitName")})%></P>
<%}else{%>
<P class="TITLE"><%=languageChoose.paramText(new String[]{"fi.jsp.ProjectPointPage.~PARAM1_NAME~projectpoints",wuInfo1.workUnitName}) %></P>
<%}%>
<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.PROJECT_SEARCH%>">
<INPUT type="hidden" name="workUnitID" value="<%=wuInfo.workUnitID%>">
<P> <B> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Group")%>  </B>
<SELECT name="cboGroup" class="COMBO">
	<%	
		for (int i = 0; i < vtOrgRole.size(); i++){
			RolesInfo groupInfo = (RolesInfo) vtOrgRole.elementAt(i);
	%><OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == wuInfo1.workUnitID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
	<%}

		for (int i = 0; i < vtGroupRole.size(); i++){
			RolesInfo groupInfo = (RolesInfo) vtGroupRole.elementAt(i);
	%><OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == wuInfo1.workUnitID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
	<%}%>
</SELECT>

<B>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Type")%>  </B>
<SELECT name="cboProjectType" class="COMBO">
	<OPTION value="-1" <%=(strProjectType.equalsIgnoreCase("-1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.All")%> </OPTION>
	<OPTION value=0 <%=(strProjectType.equalsIgnoreCase("0") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.External")%> </OPTION>
	<OPTION value=8 <%=(strProjectType.equalsIgnoreCase("8") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Internal")%> </OPTION>
</SELECT>

<B>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Category")%>  </B>
<SELECT name="cboProjectCategory" class="COMBO">
	<OPTION value="-1" <%=(strProjectCategory.equalsIgnoreCase("-1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.All1")%> </OPTION>
	<OPTION value=0 <%=(strProjectCategory.equalsIgnoreCase("0") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Development")%> </OPTION>
	<OPTION value=1 <%=(strProjectCategory.equalsIgnoreCase("1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Maintenance")%> </OPTION>
	<OPTION value=2 <%=(strProjectCategory.equalsIgnoreCase("2") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Other")%> </OPTION>
</SELECT>

<B>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Status")%>  </B>
<SELECT name="cboProjectStatus" class="COMBO">
	<OPTION value="-1" <%=(strProjectStatus.equalsIgnoreCase("-1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.All2")%> </OPTION>
	<OPTION value=0 <%=(strProjectStatus.equalsIgnoreCase("0") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Ongoing")%> </OPTION>
	<OPTION value=1 <%=(strProjectStatus.equalsIgnoreCase("1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Closed")%> </OPTION>
	<OPTION value=3 <%=(strProjectStatus.equalsIgnoreCase("3") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Tentative")%> </OPTION>
</SELECT>
<INPUT type="hidden" name="selectYear"	value="<%=year%>">
<INPUT type="hidden" name="selectMonth"	value="<%=month%>">
&nbsp;<INPUT type="submit" name="btnView" value=" <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.View")%> " class="BUTTON">
</P>
</FORM>

<TABLE>
	<TR>
		<TD width="620" align="right"><B><A href="Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=ProjectPointPageExport.jsp" target="about:blank"> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.ExporttoExcel")%> </A></B></TD>
	</TR>
</TABLE>

<TABLE class="Table" cellspacing="1">
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="24" align="center">#</TD>
			<TD width="290"> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Project")%> </TD>
			<TD width="100"><%=PreviousMonth1%></TD>
			<TD width="100"><%=CurrentMonth%></TD>
			<TD width="100"> <%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Evolution")%> </TD>
		</TR>
<%
	int index = 1;
	ProjectPointInfo ppointInfo1 = null, ppointInfo2 = null;
	ProjectInfo projectInfo1 = null, projectInfo2 = null;
	String projectCode1 = null, projectCode2 = null;
	
	for (int i = 0; i < vtProject2.size(); i ++)
	{
		projectInfo2 = (ProjectInfo) vtProject2.elementAt(i);
		projectCode2 = projectInfo2.getProjectCode();
		ppointInfo2 = (ProjectPointInfo) vtProjectPoint2.elementAt(i);
		boolean getAProject = false;
		
		for (int j = 0; j < vtProject1.size(); j ++)
		{
			projectInfo1 = (ProjectInfo) vtProject1.elementAt(j);
			projectCode1 = projectInfo1.getProjectCode();
			
			if (projectCode2.equalsIgnoreCase(projectCode1))
			{
				getAProject = true;
				ppointInfo1 = (ProjectPointInfo) vtProjectPoint1.elementAt(j);
%>
		<TR>
			<TD class="CellBGR3"><%=index%></TD>
			<TD class="CellBGR3"><A href="Fms1Servlet?reqType=<%=Constants.WORKUNIT_HOME%>&workUnitID=<%=projectInfo1.getWorkUnitId()%>"><%=projectCode2%></A></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(ppointInfo2.ProjectPoint)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(ppointInfo1.ProjectPoint)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(ppointInfo1.ProjectPoint - ppointInfo2.ProjectPoint)%></TD>
		</TR>
<%
				index = index + 1;
				break;
			}
		}
		
		if (!getAProject)
		{
			
%>
		<TR>
			<TD class="CellBGR3"><%=index%></TD>
			<TD class="CellBGR3"><A href="Fms1Servlet?reqType=<%=Constants.WORKUNIT_HOME%>&workUnitID=<%=projectInfo2.getWorkUnitId()%>"><%=projectCode2%></A></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(ppointInfo2.ProjectPoint)%></TD>
			<TD class="CellBGRNews">N/A</TD>
			<TD class="CellBGRNews">N/A</TD>
		</TR>
<%		
         index = index +1;
		                   
         }
	}

	index = index - 1;
	for (int i = 0; i < vtProject1.size(); i ++)
	{
		projectInfo1 = (ProjectInfo) vtProject1.elementAt(i);
		projectCode1 = projectInfo1.getProjectCode();
		ppointInfo1 = (ProjectPointInfo) vtProjectPoint1.elementAt(i);
		boolean doNotGetIt = false;
		
		for (int j = 0; j < vtProject2.size(); j ++)
		{
			projectInfo2 = (ProjectInfo) vtProject2.elementAt(j);
			projectCode2 = projectInfo2.getProjectCode();
			
			if (projectCode1.equalsIgnoreCase(projectCode2))
			{
				doNotGetIt = true;
				break;
			}
		}
		if (!doNotGetIt)
		{
			index = index + 1;
%>
		<TR>
			<TD class="CellBGR3"><%=index%></TD>
			<TD class="CellBGR3"><A href="Fms1Servlet?reqType=<%=Constants.WORKUNIT_HOME%>&workUnitID=<%=projectInfo1.getWorkUnitId()%>"><%=projectCode1%></A></TD>
			<TD class="CellBGRNews"> N/A</TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(ppointInfo1.ProjectPoint)%></TD>
			<TD class="CellBGRNews">N/A</TD>
		</TR>
<%		}
	}
%>
	</TBODY>
</TABLE>

<P>

<INPUT type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.ProjectPointPage.Back")%>" class="BUTTON" onclick="doIt('<%= Constants.WORKUNIT_HOME + "&workUnitID="+wuInfo.workUnitID + "&selectYear="+year+"&selectMonth="+month%>');">
</P>
</BODY>
<SCRIPT language="JavaScript">
var objToHide=new Array(frm.cboGroup,frm.cboGroup);
 
</SCRIPT >
</HTML>
