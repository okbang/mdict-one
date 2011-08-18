<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>tailoringUpdate.jsp</TITLE>
<SCRIPT language="JavaScript">
	function doOnload(){
		window.open("header.jsp","header");
		loadOrgMenu();
	}
	
</SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
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
	Vector tailoringList = (Vector) session.getAttribute("vtTailoringDeviation");
	
	final int vectorID = Integer.parseInt(request.getParameter("vtTailoringId"));

	TailoringDeviationInfo info = (TailoringDeviationInfo) tailoringList.elementAt(vectorID);

	String strWuID = (String) session.getAttribute("wuID");
	String strTailoringType = (String) session.getAttribute("strTailoringType");
	String strTailoringCategory = (String) session.getAttribute("strTailoringCategory");
	
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

%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.paTailoringDetails.ProcessAssetsTailoringDeviatio")%>  <%=info.projectCode%></P><br>
<DIV align="left">
<TABLE cellspacing="1" class="Table">
    <COL span="1" width="100">
    <COL span="1" width="400">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paTailoringDetails.Modification")%></TD>
            <TD class="CellBGR3"><%=info.modification%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paTailoring.Action")%></TD>
            <TD class="CellBGR3">
			<%
			switch(info.action)
			{
				case 1:%> <%=languageChoose.getMessage("fi.jsp.paTailoringDetails.Added")%> <%break; 
				case 2:%> <%=languageChoose.getMessage("fi.jsp.paTailoringDetails.Modified")%> <%break;
				case 3:%> <%=languageChoose.getMessage("fi.jsp.paTailoringDetails.Deleted")%> <%break; 
			}
			%>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paTailoringDetails.Reason")%></TD>
            <TD class="CellBGR3"><%=info.reason%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paTailoringDetails.Type")%></TD>
            <TD class="CellBGR3">
			<%
			switch(info.type)
			{
				case 1:%> <%=languageChoose.getMessage("fi.jsp.paTailoringDetails.Tailoring")%> <%break; 
				case 2:%> <%=languageChoose.getMessage("fi.jsp.paTailoringDetails.Deviation")%> <%break;	 
			}
			%>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paTailoringDetails.Category")%></TD>
            <TD class="CellBGR3"><%=info.category == null ? "N/A" : info.category%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paTailoringDetails.Note")%></TD>
            <TD class="CellBGR3"><%=((info.note == null)? "" : info.note)%></TD>
        </TR>
    </TBODY>
</TABLE>
</DIV>
<BR>

<FORM name="frm" action="Fms1Servlet" method="post">
<INPUT type="hidden" name="reqType">
<INPUT type = "hidden" name = "cboGroup" value="<%=strWuID%>">
<INPUT type = "hidden" name = "cboTailoringType" value="<%=strTailoringType%>">
<INPUT type = "hidden" name = "cboTailoringCategory" value="<%=strTailoringCategory%>">
<INPUT type = "hidden" name = "fromDate" value="<%=fromDate%>">
<INPUT type = "hidden" name = "toDate" value="<%=toDate%>">
</FORM>

<INPUT type="button" name="Back" value=" <%=languageChoose.getMessage("fi.jsp.paTailoringDetails.Back")%> " class="BUTTON" onclick="onBack()">

</BODY>
</HTML>
<SCRIPT language = "javascript">
function onBack() {
	frm.reqType.value = "<%=Constants.PROASS_TAILORING_DEVIATION%>";
	frm.submit();
}
</SCRIPT>
