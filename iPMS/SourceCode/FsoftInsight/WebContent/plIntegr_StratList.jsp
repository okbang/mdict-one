<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plIntegr_StratList.jsp</TITLE>
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
	
	Vector vIntegrList = (Vector) session.getAttribute("ProIntegrList");
	
	String archiveStatus = (String) session.getAttribute("archiveStatus");
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
		if (Integer.parseInt(archiveStatus) == 4) {
			isArchive = true;
		}
	}
%>

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.ProjectPlanProductIntegrationStrategy")%></P>
<BR>
<TABLE cellspacing="1" width="95%" class="Table" id="tableReqChangesMng">
	<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.RequirementChangesManagement")%></CAPTION>
		<THEAD>
			<TR class="ColumnLabel">
				<TD width = "3%" align = "center"> # </TD>
				<TD width="17%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.ComponentID")%> </TD>
				<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.Description")%> </TD>
				<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.IntegratedWithComponents")%> </TD>
				<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.IntegrationOrder")%> </TD>
				<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.IntegrationReadyNeed")%> </TD>
			</TR>
		</THEAD>
		<TBODY>	
<%
		String className;
		IntegrStratInfo integrInfo;
		int integrSize = vIntegrList.size();
		for(int i = 0; i < integrSize; i++){
			integrInfo = (IntegrStratInfo) vIntegrList.elementAt(i);
		 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
%>		
			<TR class="<%=className%>">
				<TD align = "center"><%=i+1%></TD>
				<TD><%=integrInfo.integrCompID == null ? "N/A": ConvertString.toHtml(integrInfo.integrCompID)%></TD>
				<TD><%=integrInfo.integrDesc == null ? "N/A": ConvertString.toHtml(integrInfo.integrDesc)%></TD>
				<TD><%=integrInfo.integrWithComp == null ? "N/A": ConvertString.toHtml(integrInfo.integrWithComp)%></TD>
				<TD><%=integrInfo.integrOrder == null ? "N/A": ConvertString.toHtml(integrInfo.integrOrder)%></TD>
				<TD><%=integrInfo.integrReadyNeed == null ? "N/A": ConvertString.toHtml(integrInfo.integrReadyNeed)%></TD>
			</TR>
<%
		}
%>			
		</TBODY>
</TABLE>
<BR>
<%if(right == 3 && !isArchive ){%>
<form name = "frmIntegrationStrategy" method = "post" action="Fms1Servlet">
<input type="hidden" name="reqType" value="0">
<input type="button" name="plIntegrAdd" value="<%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.AddNew")%>" class = "BUTTON" onclick = "javascript:addClick();">
<% if (integrSize > 0) { %>
<input type="button" name="plIntegrUpdate" value="<%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.Update")%>" class = "BUTTON" onclick = "javascript:updateClick();">
<% } %>
<INPUT type="button" name="plBack" value="<%=languageChoose.getMessage("fi.jsp.plIntegr_StratList.Back")%>" class="BUTTON" onclick ="javascript:doBack();">
</form>
<%}%>
<BR>
<SCRIPT language="javascript">

function doBack(){
	frmIntegrationStrategy.reqType.value = <%=Constants.WORKUNIT_HOME%>;
	frmIntegrationStrategy.submit();
}

function addClick(){		
	frmIntegrationStrategy.reqType.value=<%=Constants.PL_INTEGRATION_STRATEGY_PREPARE_ADD%>;	
	frmIntegrationStrategy.submit();
}

function updateClick(){		
	frmIntegrationStrategy.reqType.value=<%=Constants.PL_INTEGRATION_STRATEGY_PREPARE_UPDATE%>;
	frmIntegrationStrategy.submit();
}
</SCRIPT>

</BODY>
</HTML>
