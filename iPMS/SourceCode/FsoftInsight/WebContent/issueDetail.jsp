<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.tools.*, com.fms1.web.* ,com.fms1.html.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>issueDetail.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
	int right  = Security.securiPage("Project issues",request,response);
	Vector issueList = (Vector)session.getAttribute("issueList");
	Vector vtProcess = (Vector)session.getAttribute("vtProcess");
	int vtID=Integer.parseInt(request.getParameter("vtID"));
	IssueInfo issue= (IssueInfo)issueList.elementAt(vtID);
	String strMenuType=CommonTools.getMnuFunc(session);
		//PQA specific------------
	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
	//------------------------
%>
<BODY onload="<%=strMenuType%>" class="BD">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.issueDetail.Issues")%></P>
<FORM method="post" name="frmDetail">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.issueDetail.Issuedetails")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width ="30%"><%=languageChoose.getMessage("fi.jsp.issueDetail.Description")%></TD>
            <TD class="CellBGR3"><%=ConvertString.toHtml(issue.description)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueDetail.Status")%></TD>
            <TD class="CellBGR3"><%=languageChoose.getMessage(issue.getStatusName())%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueDetail.Priority")%></TD>
            <TD class="CellBGR3"><%=languageChoose.getMessage(issue.getPriorityName())%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueDetail.Type")%></TD>
            <TD class="CellBGR3"><%=languageChoose.getMessage(issue.getTypeName())%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueDetail.Processrelated")%></TD>
            <TD class="CellBGR3">
           	<%
           		for(int i=0;i<vtProcess.size();i++)
           		{
           			ProcessInfo psInfo=(ProcessInfo)vtProcess.get(i);
           			if (psInfo.processId == issue.processId) {
           	%>
				<%=languageChoose.getMessage(psInfo.name)%>
           	<%
           			break;
           			}
           		}
           	%>
            </TD>
        </TR>
        <%if(workUnitID==Parameters.PQA_WU){%>
        <TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage(WUCombo.label)%></TD>
            <TD class="CellBGR3"><%=issue.wuName%></TD>
        </TR>
        <%}%>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueDetail.Owner")%></TD>
            <TD class="CellBGR3"><%=issue.ownerName%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueDetail.Creator")%></TD>
            <TD class="CellBGR3"><%=issue.creator%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueDetail.CreatedDate")%></TD>
            <TD class="CellBGR3"><%=CommonTools.dateFormat(issue.startDate)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueDetail.DueDate")%></TD>
            <TD class="CellBGR3"><%=CommonTools.dateFormat(issue.dueDate)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueDetail.CommentSolution")%></TD>
            <TD class="CellBGR3"><%=((issue.comment==null)?"N/A":ConvertString.toHtml(issue.comment))%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueDetail.ClosedDate")%></TD>
            <TD class="CellBGR3"><%=CommonTools.dateFormat(issue.closeDate)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueDetail.Reference")%></TD>
            <TD class="CellBGR3"><%=((issue.reference==null)?"N/A":ConvertString.toHtml(issue.reference))%></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="hidden" name="txtIssueID" value="<%=issue.issueID%>">
<%if(right == 3 && !isArchive){%>
<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.issueDetail.Update")%>" onclick="doAction(this);" class="BUTTON"> 
<INPUT type="button" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.issueDetail.Delete")%>" onclick="doAction(this);" class="BUTTON">
<%}%><INPUT type="button" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.issueDetai.Back")%>" onclick="doIt(<%=Constants.ISSUE%>)" class="BUTTON"> 
</FORM>
<SCRIPT language="javascript">
function doAction(button)
{  	
	if (button.name == "btnDelete")
		if (window.confirm("<%= languageChoose.getMessage("fi.jsp.issueDetail.Areyousuretodeletethisissue")%>") != 0) {
			frmDetail.action = "Fms1Servlet?reqType=<%=Constants.ISSUE_DELETE%>&vtID=<%=vtID%>";
			frmDetail.submit();
		}
	if (button.name == "btnUpdate") {
		frmDetail.action = "issueUpdate.jsp?vtID=<%=vtID%>";
		frmDetail.submit();
		
	}  	
}
</SCRIPT> 
</BODY>
</HTML>
