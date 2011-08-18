<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plReqChangesMng.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose =	(LanguageChoose) session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu();" class="BD">
<%
	int right = Security.securiPage("Project plan", request, response);
	boolean existData = false;
	ReqChangesInfo reqInfo = (ReqChangesInfo) session.getAttribute("ReqChangesInfo");	
	if (reqInfo != null) existData = true;
	
	String archiveStatus = (String) session.getAttribute("archiveStatus");
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
		if (Integer.parseInt(archiveStatus) == 4) {
			isArchive = true;
		}
	}
%>

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plReqChangesMng.ProjectPlanRequirementChangesManagement")%></P>
<BR>
<% if (!existData) { %>
<P class="ERROR"> There is no requirement changes data.</P>
<% } else { %>
<TABLE cellspacing="1" class="Table" width="95%" id="tableReqChangesMng">
<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plReqChangesMng.RequirementChangesManagement")%> </CAPTION>
   	<TBODY>
        <TR>
            <TD class="ColumnLabel" width = 35%> Where is the change request logged? </TD>
            <TD class="CellBGR3">&nbsp;<%=reqInfo.reqLogLocation == null ? "N/A": ConvertString.toHtml(reqInfo.reqLogLocation)%> </TD>
        </TR>        
    	<TR>
        	<TD class="ColumnLabel" width = 35%> Who logs the change request? </TD>
            <TD class="CellBGR3" >&nbsp;<%= reqInfo.reqCreator == null ? "N/A": ConvertString.toHtml(reqInfo.reqCreator)%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width = 35%> Who reviews the change request? </TD>
            <TD class="CellBGR3">&nbsp;<%=reqInfo.reqReviewer == null ? "N/A": ConvertString.toHtml(reqInfo.reqReviewer)%> </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width = 35%> Who approves the change request? </TD>
            <TD class="CellBGR3">&nbsp;<%=reqInfo.reqApprover == null ? "N/A": ConvertString.toHtml(reqInfo.reqApprover)%> </TD>
        </TR>
    </TBODY>
</TABLE>
<% } %>
<BR>
<%if(right == 3 && !isArchive ){%>
<form name = "frmReqChangesMng" method = "post" action="Fms1Servlet">
	<input type="hidden" name="reqType" value="0">
	<input type = "hidden" name ="groupName" value = "0">
<% if (!existData) { %>
	<input type="button" name="woReqChangesAdd" value="<%=languageChoose.getMessage("fi.jsp.plReqChangesMng.AddNew")%>" class = "BUTTON" onclick = "javascript:addClick();">
<% } else { %>
	<input type="button" name="woReqChangesUpdate" value="<%=languageChoose.getMessage("fi.jsp.plReqChangesMng.Update")%>" class = "BUTTON" onclick = "javascript:updateClick();">
<% } %>
	<INPUT type="button" name="woBack" value="<%=languageChoose.getMessage("fi.jsp.plReqChangesMng.Back")%>" class="BUTTON" onclick="javascript:doBack();">
</form>
<%}%>
<BR>

<SCRIPT language="javascript">
function doBack(){
	frmReqChangesMng.reqType.value = <%=Constants.WORKUNIT_HOME%>;
	frmReqChangesMng.submit();
}

function addClick(){		
	frmReqChangesMng.reqType.value=<%=Constants.PL_REQ_CHANGES_MNG_PREPARE_ADD%>;	
	frmReqChangesMng.submit();
}

function updateClick(){		
	frmReqChangesMng.reqType.value=<%=Constants.PL_REQ_CHANGES_MNG_PREPARE_UPDATE%>;	
	frmReqChangesMng.submit();
}
</SCRIPT>

</BODY>
</HTML>
