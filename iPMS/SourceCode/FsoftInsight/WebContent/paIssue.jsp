<%@page import="java.util.Vector,com.fms1.infoclass.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.common.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<TITLE>paIssue.jsp</TITLE>
<SCRIPT language="JavaScript">
	function doOnload(){
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
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.paIssue.ProcessAssetsIssues")%> </P>

<%
	Vector vtIssueList = (Vector) session.getAttribute("vtIssueList");
	
	String strWuID = (String) session.getAttribute("wuID");
	String strIssueType = (String) session.getAttribute("strIssueType");
	String strProcess = (String) session.getAttribute("strProcess");
	
	Vector vtGroupList = (Vector) session.getAttribute("groupList");
	Vector vtOrgList = (Vector) session.getAttribute("orgList");
	
	String fromDate = (String) session.getAttribute("fromDate");
	String toDate = (String) session.getAttribute("toDate");
	
	long lWuID = 132; // FSOFT by default
	if (strWuID != null)
		lWuID = Long.parseLong(strWuID);

	if (strIssueType == null)
		strIssueType = "-1";

	int iProcess = 0;
	if (strProcess != null)
		iProcess = Integer.parseInt(strProcess);

	ReportMonth rm = new ReportMonth();
    if (fromDate == null)
		fromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2,4);

	if (toDate == null)
		toDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2,4);
	
	int j = 0;

	Vector vtProcess = (Vector)session.getAttribute("vtProcess");
%>

<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.PROASS_ISSUE%>">

<TABLE width="100%" class="NormalText">
	<TR>
		<TD> <%=languageChoose.getMessage("fi.jsp.paIssue.Fromdate")%> </TD>
		<TD><INPUT type="text" name="fromDate" value="<%=fromDate%>" maxlength="9" size="9"> (DD-MMM-YY) </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.paIssue.Todate")%> </TD>
		<TD><INPUT type="text" name="toDate" value="<%=toDate%>" maxlength="9" size="9"> (DD-MMM-YY) </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.paIssue.Group")%> </TD>
		<TD>
			<SELECT name="cboGroup" class="COMBO">
				<%	
					j = vtOrgList.size();
					for (int i = 0; i < j; i++)
					{
						RolesInfo groupInfo = (RolesInfo) vtOrgList.elementAt(i);
				%>
			<OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
				<%
					}
				%>
				<%	
					j = vtGroupList.size();
					for (int i = 0; i < j; i++)
					{
						RolesInfo groupInfo = (RolesInfo) vtGroupList.elementAt(i);
				%>
			<OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
				<%
					}
				%>
			</SELECT>
		</TD>
		<TD></TD>
	</TR>
	<TR>
	<TD> <%=languageChoose.getMessage("fi.jsp.paIssue.Type")%> </TD>
	<TD>
		<SELECT name="cboIssueType" class="COMBO">
			<OPTION value="-1" <%=(strIssueType.equalsIgnoreCase("-1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paIssue.All")%> </OPTION>
			<OPTION value=0 <%=(strIssueType.equalsIgnoreCase("0") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paIssue.Organization")%> </OPTION>
			<OPTION value=1 <%=(strIssueType.equalsIgnoreCase("1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paIssue.Customer")%> </OPTION>
			<OPTION value=2 <%=(strIssueType.equalsIgnoreCase("2") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paIssue.Resource")%> </OPTION>
			<OPTION value=3 <%=(strIssueType.equalsIgnoreCase("3") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paIssue.Operation")%> </OPTION>
			<OPTION value=4 <%=(strIssueType.equalsIgnoreCase("4") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paIssue.Others")%> </OPTION>
		</SELECT>
	</TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.paIssue.Process")%> </TD>
	<TD>
		<SELECT name="cboProcess" class="COMBO">
		<OPTION value="0" <%=(iProcess == 0 ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paIssue.All")%> </OPTION>
			<%	
				j = vtProcess.size();
				for (int i = 0; i < j; i++)
				{
		           	ProcessInfo psInfo = (ProcessInfo)vtProcess.get(i);
			%>
		<OPTION value="<%=psInfo.processId%>"<%=(psInfo.processId == iProcess ? " selected" : "")%>><%=languageChoose.getMessage(psInfo.name)%></OPTION>
			<%
				}
			%>
		</SELECT>
	</TD>
	<TD><INPUT type="button" name="btnView" value=" <%=languageChoose.getMessage("fi.jsp.paIssue.View")%> " class="BUTTON" onclick="doAction()"></TD>
	<TD><B><A href="Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=paIssueExport.jsp" target="about:blank"> <%=languageChoose.getMessage("fi.jsp.paIssue.ExporttoExcel")%> </A></B></P>
	</TR>
</TABLE>

<BR>

</FORM>

<TABLE cellspacing="1" class="Table" width="100%">
<CAPTION align="left" class="TableCaption"></CAPTION>
    <COL span="1" width="300">
    <TBODY>
        <TR class="ColumnLabel">
            <TD width = "24" align = "center"># </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssue.Project")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssue.Description")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssue.CommentSolution")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paIssue.Type1")%> </TD>
        </TR>
	<%
	j = vtIssueList.size();
	for(int i = 0 ;i < j; i++)
	{
		String className;
 		className=(i%2==0)?"CellBGRnews":"CellBGR3";
 		
	 	IssueInfo info = (IssueInfo) vtIssueList.elementAt(i);
	%>
	
	<TR class="<%=className%>">
	<TD width = "24" align = "center"><%=i+1%></TD>
	<TD><A href="paIssueDetail.jsp?vtIssueId=<%=i%>"><%=info.projectCode%></A></TD>
	<TD><%=((info.description == null)?"N/A":ConvertString.toHtml(info.description))%></TD>
	<TD><%=((info.comment == null)?"N/A":ConvertString.toHtml(info.comment))%></TD>
	<TD>
	
	<%
	switch(info.typeID)
	{
		case 0:%> <%=languageChoose.getMessage("fi.jsp.paIssue.Organization1")%> <%break; 
		case 1:%> <%=languageChoose.getMessage("fi.jsp.paIssue.Customer1")%> <%break;
		case 2:%> <%=languageChoose.getMessage("fi.jsp.paIssue.Resource1")%> <%break; 
		case 3:%> <%=languageChoose.getMessage("fi.jsp.paIssue.Operation1")%> <%break; 
		case 4:%> <%=languageChoose.getMessage("fi.jsp.paIssue.Others1")%> <%break; 
	}
	%>
	</TD>

	</TR>
	
	<%
	}
	%>

</TABLE>
</BODY>
<SCRIPT language="JavaScript">
  function doAction()
  {
  	if(frm.fromDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.paIssue.Thisfieldismandatory")%>");
  	 	frm.fromDate.focus();
  	 	return;
  	}
	if (!isDate(frm.fromDate.value)){
 		window.alert("<%= languageChoose.getMessage("fi.jsp.paIssue.Invaliddatevalue")%>");
  		frm.fromDate.focus();
  		return;
  	}

  	if(frm.toDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.paIssue.Thisfieldismandatory")%>");
  	 	frm.toDate.focus();
  	 	return;
  	}
	if (!isDate(frm.toDate.value)){
 		window.alert("<%= languageChoose.getMessage("fi.jsp.paIssue.Invaliddatevalue")%>");
  		frm.toDate.focus();
  		return;
  	}

  	if(compareDate(frm.fromDate.value, frm.toDate.value) == -1)
  	{
  	 	window.alert("<%= languageChoose.getMessage("fi.jsp.paIssue.FromdatemustbebeforeTodate")%>");
  		frm.fromDate.focus();
  		return;
  	}
  	 		
  	frm.submit();
  }
  var objToHide=new Array(frm.cboIssueType);
  
</SCRIPT >
</HTML>
