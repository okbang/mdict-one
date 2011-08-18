<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plMeasureProgList.jsp</TITLE>
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
	
	Vector vMeasList = (Vector) session.getAttribute("MeasurementsProgList");
	
	String archiveStatus = (String) session.getAttribute("archiveStatus");
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
		if (Integer.parseInt(archiveStatus) == 4) {
			isArchive = true;
		}
	}
%>

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plMeasureProgList.ProjectPlanMeasurementsProgram")%></P>
<BR>
<TABLE cellspacing="1" class="Table" id="tableReqChangesMng" width="95%">
	<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.plMeasureProgList.MeasurementsProgram")%></CAPTION>
		<THEAD>
			<TR class="ColumnLabel">
				<TD width="3%" align = "center"> # </TD>
				<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgList.DataToBeCollected")%> </TD>
				<TD width="25%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgList.Purpose")%> </TD>
				<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgList.Responsible")%> </TD>
				<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plMeasureProgList.When")%> </TD>
			</TR>
		</THEAD>
		<TBODY>	
<%
		String className;
		MeasureProgInfo measInfo;
		int measSize = vMeasList.size();
		for(int i = 0; i < measSize; i++){
			measInfo = (MeasureProgInfo) vMeasList.elementAt(i);
		 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
%>		
			<TR class="<%=className%>">
				<TD align = "center"><%=i+1%></TD>
				<TD><%=measInfo.mes_data_colect == null ? "N/A": ConvertString.toHtml(measInfo.mes_data_colect)%></TD>
				<TD><%=measInfo.mes_purpose == null ? "N/A": ConvertString.toHtml(measInfo.mes_purpose)%></TD>
				<TD><%=measInfo.mes_responsible == null ? "N/A": ConvertString.toHtml(measInfo.mes_responsible)%></TD>
				<TD><%=measInfo.mes_when == null ? "N/A": ConvertString.toHtml(measInfo.mes_when)%></TD>				
			</TR>
<%
		}
%>			
		</TBODY>
</TABLE>
<BR>
<%if(right == 3 && !isArchive ){%>
<form name = "frmMeasurementsProgram" method = "post" action="Fms1Servlet">
<input type="hidden" name="reqType" value="0">
<input type="button" name="plMeasAdd" value="<%=languageChoose.getMessage("fi.jsp.plMeasureProgList.AddNew")%>" class = "BUTTON" onclick = "javascript:addClick();">
<% if (measSize > 0) { %>
<input type="button" name="plMeasUpdate" value="<%=languageChoose.getMessage("fi.jsp.plMeasureProgList.Update")%>" class = "BUTTON" onclick = "javascript:updateClick();">
<% } %>
<INPUT type="button" name="plBack" value="<%=languageChoose.getMessage("fi.jsp.plMeasureProgList.Back")%>" class="BUTTON" onclick ="javascript:doBack();">
</form>
<%}%>
<BR>
<SCRIPT language="javascript">

function doBack(){
	frmMeasurementsProgram.reqType.value = <%=Constants.WORKUNIT_HOME%>;
	frmMeasurementsProgram.submit();
}

function addClick(){		
	frmMeasurementsProgram.reqType.value=<%=Constants.PL_MEASUREMENTS_PROGRAM_PREPARE_ADD%>;	
	frmMeasurementsProgram.submit();
}

function updateClick(){		
	frmMeasurementsProgram.reqType.value=<%=Constants.PL_MEASUREMENTS_PROGRAM_PREPARE_UPDATE%>;
	frmMeasurementsProgram.submit();
}
</SCRIPT>

</BODY>
</HTML>
