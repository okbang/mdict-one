<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<TITLE>paTailoring.jsp</TITLE>
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
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
</HEAD>
<BODY onLoad="doOnload();" class="BD">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.paTailoring.ProcessAssetsTailoringDeviatio")%></P>

<%
	Vector tailoringList = (Vector) session.getAttribute("vtTailoringDeviation");
	Vector catList = (Vector) session.getAttribute("tailoring_category");

	String strWuID = (String) session.getAttribute("wuID");
	String strTailoringType = (String) session.getAttribute("strTailoringType");
	String strTailoringCategory = (String) session.getAttribute("strTailoringCategory");

	Vector vtGroupList = (Vector) session.getAttribute("groupList");
	Vector vtOrgList = (Vector) session.getAttribute("orgList");
	
	String fromDate = (String) session.getAttribute("fromDate");
	String toDate = (String) session.getAttribute("toDate");
	
	long lWuID = 132; // FSOFT by default
	if (strWuID != null)
		lWuID = Long.parseLong(strWuID);

	if (strTailoringCategory == null)
		strTailoringCategory = "-1";

	if (strTailoringType == null)
		strTailoringType = "-1";

	ReportMonth rm = new ReportMonth();
	if (fromDate == null)
		fromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2,4);

	if (toDate == null)
		toDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2,4);
	
	int j = 0;

%>

<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.PROASS_TAILORING_DEVIATION%>">

<TABLE width="100%" class="NormalText">
	<TR>
		<TD><%=languageChoose.getMessage("fi.jsp.paTailoring.Fromdate")%></TD>
		<TD><INPUT type="text" name="fromDate" value="<%=fromDate%>" maxlength="9" size="9">(DD-MMM-YY)</TD>
		<TD><%=languageChoose.getMessage("fi.jsp.paTailoring.Todate")%></TD>
		<TD><INPUT type="text" name="toDate" value="<%=toDate%>" maxlength="9" size="9">(DD-MMM-YY)</TD>
		<TD><%=languageChoose.getMessage("fi.jsp.paTailoring.Group")%></TD>	
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
	</TR>
	<TR>
		<TD><%=languageChoose.getMessage("fi.jsp.paTailoring.Type")%> </TD>
		<TD>
			<SELECT name="cboTailoringType" class="COMBO">
				<OPTION value="-1" <%=(strTailoringType.equalsIgnoreCase("-1") ? " selected" : "")%>>All</OPTION>
				<OPTION value=1 <%=(strTailoringType.equalsIgnoreCase("1") ? " selected" : "")%>>Tailoring</OPTION>
				<OPTION value=2 <%=(strTailoringType.equalsIgnoreCase("2") ? " selected" : "")%>>Deviation</OPTION>
			</SELECT>
		</TD>
		<TD><%=languageChoose.getMessage("fi.jsp.paTailoring.Category ")%></TD>
		<TD>
			<SELECT name="cboTailoringCategory" class="COMBO">
			<OPTION value="-1"<%=(strTailoringCategory.equalsIgnoreCase("-1") ? " selected" : "")%>>All</OPTION>
				<%	
					j = catList.size();
					for (int i = 0; i < j; i++)
					{
				%>
			            <OPTION value = "<%=catList.elementAt(i)%>" <%if(strTailoringCategory.equals(catList.elementAt(i))){%>selected<%}%>><%=catList.elementAt(i)%></OPTION>
				<%
					}
				%>
			</SELECT>
		</TD>
		<TD><INPUT type="button" name="btnView" value="<%=languageChoose.getMessage("fi.jsp.paTailoring.View")%>" class="BUTTON" onclick="doAction()"></TD>
		<TD><B><A href="Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=paTailoringExport.jsp" target="about:blank"> <%=languageChoose.getMessage("fi.jsp.paTailoring.ExporttoExcel")%> </A></B></TD>
	</TR>
</TABLE>
</FORM>

<BR>

<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width = "24" align = "center"># </TD>
            <TD><%=languageChoose.getMessage("fi.jsp.paTailoring.Project")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.paTailoring.Modification")%></TD>
            <TD nowrap="nowrap"><%=languageChoose.getMessage("fi.jsp.paTailoring.Action")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.paTailoring.Reason")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.paTailoring.Category1")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.paTailoring.Type1")%></TD>
        </TR>
	<%
	j = tailoringList.size();
	for(int i = 0 ;i < j; i++)
	{
		String className;
 		className=(i%2==0)?"CellBGRnews":"CellBGR3";
 		
	 	TailoringDeviationInfo info = (TailoringDeviationInfo) tailoringList.elementAt(i);
	%>
	
	<TR class="<%=className%>">
	<TD width = "24" align = "center"><%=i+1%></TD>
	<TD><A href="paTailoringDetails.jsp?vtTailoringId=<%=i%>"><%=info.projectCode%></A></TD>
	<TD><%=((info.modification == null)?"N/A":ConvertString.toHtml(info.modification))%></TD>
	<TD>
	
	<%
	switch(info.action)
	{
		case 1:%><%=languageChoose.getMessage("fi.jsp.paTailoring.Added")%><%break; 
		case 2:%><%=languageChoose.getMessage("fi.jsp.paTailoring.Modified")%><%break;
		case 3:%><%=languageChoose.getMessage("fi.jsp.paTailoring.Deleted")%><%break; 
	}
	%>
	</TD>

	<TD><%=((info.reason == null)?"N/A":ConvertString.toHtml(info.reason))%></TD>
	<TD><%=info.category == null ? "N/A" : info.category %> </TD>
	<TD nowrap="nowrap">
		<%
		switch(info.type)
		{
			case 1:%> <%=languageChoose.getMessage("fi.jsp.paTailoring.Tailoring")%> <%break; 
			case 2:%> <%=languageChoose.getMessage("fi.jsp.paTailoring.Deviation")%> <%break;	 
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
  	if(!mandatoryFld(frm.fromDate, "<%=languageChoose.getMessage("fi.jsp.paTailoring.Fromdate1")%>")){
  		return;
  	}
	if (!dateFld(frm.fromDate, "<%=languageChoose.getMessage("fi.jsp.paTailoring.Fromdate1")%>")){
  		return;
  	}
  	if(!mandatoryFld(frm.toDate, "<%=languageChoose.getMessage("fi.jsp.paTailoring.Todate1")%>")){
  		return;
  	}
	if (!dateFld(frm.toDate, "<%=languageChoose.getMessage("fi.jsp.paTailoring.Todate1")%>")){
 		return;
  	}

  	if(compareDate(frm.fromDate.value, frm.toDate.value) == -1)
  	{
  	 	window.alert("<%=languageChoose.getMessage("fi.jsp.paTailoring.FromdatemustbebeforeTodate")%>");
  		frm.fromDate.focus();
  		return;
  	}
  	
  	frm.submit();
  }
var objToHide=new Array(frm.cboTailoringType,frm.cboTailoringCategory);
</SCRIPT >
</HTML>
