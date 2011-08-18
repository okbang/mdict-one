<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plComReportList.jsp</TITLE>
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
	
	Vector vComReportList = (Vector) session.getAttribute("ComReportList");
	int comSize = 0;
	if (vComReportList != null) comSize = vComReportList.size();
	
	String archiveStatus = (String) session.getAttribute("archiveStatus");
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
		if (Integer.parseInt(archiveStatus) == 4) {
			isArchive = true;
		}
	}
%>

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectPlanComReport")%></P>
<BR>
<TABLE cellspacing="1" width="95%" class="Table" id="tableComReport">
	<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.plComReportList.Communication_N_ReportingList")%></CAPTION>
		<THEAD>
			<TR class="ColumnLabel">
				<TD width="3%" align="center"><INPUT id="selectAll" type ="checkbox" name="selectAll" onclick="selectAll()"/></TD>
				<TD width="3%" align="center"> # </TD>
				<TD width="21%"> <%=languageChoose.getMessage("fi.jsp.plComReportList.CommunicationType")%> </TD>
				<TD width="16%"> <%=languageChoose.getMessage("fi.jsp.plComReportList.MethodTool")%> </TD>
				<TD width="16%"> <%=languageChoose.getMessage("fi.jsp.plComReportList.When")%> </TD>
				<TD width="25%"> <%=languageChoose.getMessage("fi.jsp.plComReportList.Information")%> </TD>
				<TD width="11%"> 
					<%=languageChoose.getMessage("fi.jsp.plComReportList.Participants")%> /<BR>
					<%=languageChoose.getMessage("fi.jsp.plComReportList.Responsible")%>
				</TD>
			</TR>
		</THEAD>
		<TBODY>	
<%
		String className;
		ComReportInfo comReportInfo;		
		int currentType = 0;
		int nextType = 0;
		int classType = 0;
		for(int i = 0; i < comSize-1; i++){
			comReportInfo = (ComReportInfo) vComReportList.elementAt(i);
		 	className=(classType%2==0)? "CellBGR3":"CellBGRnews";
		 	currentType = comReportInfo.comParentType;		 	
		 	nextType = ((ComReportInfo) vComReportList.elementAt(i+1)).comParentType;		 	
		 	if (i == 0) {
%>		
		<TR class="CellBGRLongnews" style="font-weight: bold">
			<TD colspan="7">&nbsp;
			<%
				switch(currentType){
					case 1:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTaskTrackingType")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectMeetingType")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTrackingType")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CustomerCommunicationReportingType")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CommunicationWithSeniorManagementType")%><%break;
					case 6:%><%=languageChoose.getMessage("fi.jsp.plComReportList.OtherCommunicationAndReportingType")%><%break;
				}
			%>
			</TD>
		</TR>
		<%	
			}  
			if (currentType != nextType ) {
		%>
		<TR class="<%=className%>">
			<TD align="center"><INPUT type ="checkbox" name="checkForUpdate"/></TD>
			<TD align="center"><%=i+1%><INPUT TYPE ="hidden" name ="comID" value = "<%=comReportInfo.comID%>"/></TD>
			<TD><%=comReportInfo.comType == null ? "N/A": ConvertString.toHtml(comReportInfo.comType)%></TD>
			<TD><%=comReportInfo.comMethodTool == null ? "N/A": comReportInfo.comMethodTool%></TD>
			<TD><%=comReportInfo.comWhen == null ? "N/A": ConvertString.toHtml(comReportInfo.comWhen)%></TD>
			<TD><%=comReportInfo.comInfo == null ? "N/A": ConvertString.toHtml(comReportInfo.comInfo)%></TD>
			<TD><%=comReportInfo.comResp == null ? "N/A": ConvertString.toHtml(comReportInfo.comResp)%></TD>
		</TR>
		<TR class="CellBGRLongnews" style="font-weight: bold">
			<TD colspan="7">&nbsp;
			<%
				switch(nextType){
					case 1:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTaskTrackingType")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectMeetingType")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTrackingType")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CustomerCommunicationReportingType")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CommunicationWithSeniorManagementType")%><%break;
					case 6:%><%=languageChoose.getMessage("fi.jsp.plComReportList.OtherCommunicationAndReportingType")%><%break;
				}
			%>
			</TD>
		</TR>
<%
				classType = 0;
			} else {
				classType++;
%>			
		<TR class="<%=className%>">
			<TD align="center"><INPUT type ="checkbox" name="checkForUpdate"/></TD>
			<TD align="center"><%=i+1%><INPUT TYPE ="hidden" name ="comID" value = "<%=comReportInfo.comID%>"/></TD>
			<TD><%=comReportInfo.comType == null ? "N/A": ConvertString.toHtml(comReportInfo.comType)%></TD>
			<TD><%=comReportInfo.comMethodTool == null ? "N/A": comReportInfo.comMethodTool%></TD>
			<TD><%=comReportInfo.comWhen == null ? "N/A": ConvertString.toHtml(comReportInfo.comWhen)%></TD>
			<TD><%=comReportInfo.comInfo == null ? "N/A": ConvertString.toHtml(comReportInfo.comInfo)%></TD>
			<TD><%=comReportInfo.comResp == null ? "N/A": ConvertString.toHtml(comReportInfo.comResp)%></TD>		
		</TR>
<%			
			}
		}// end for
		if (comSize > 0) {			
			comReportInfo = (ComReportInfo) vComReportList.elementAt(comSize-1);
			currentType = comReportInfo.comParentType;		 	
			className=(comSize%2==0)? "CellBGR3":"CellBGRnews";
			if (comSize == 1) {
%>
		<TR class="CellBGRLongnews" style="font-weight: bold">
			<TD colspan="7">&nbsp;
			<%
				switch(currentType){
					case 1:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTaskTrackingType")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectMeetingType")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTrackingType")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CustomerCommunicationReportingType")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CommunicationWithSeniorManagementType")%><%break;
					case 6:%><%=languageChoose.getMessage("fi.jsp.plComReportList.OtherCommunicationAndReportingType")%><%break;
				}
			%>
			</TD>
		</TR>
<%
			}
%>		
		<TR class="<%=className%>">
			<TD align="center"><INPUT type ="checkbox" name="checkForUpdate"/></TD>
			<TD align="center"><%=comSize%><INPUT TYPE ="hidden" name ="comID" value = "<%=comReportInfo.comID%>"/></TD>
			<TD><%=comReportInfo.comType == null ? "N/A": ConvertString.toHtml(comReportInfo.comType)%></TD>
			<TD><%=comReportInfo.comMethodTool == null ? "N/A": comReportInfo.comMethodTool%></TD>
			<TD><%=comReportInfo.comWhen == null ? "N/A": ConvertString.toHtml(comReportInfo.comWhen)%></TD>
			<TD><%=comReportInfo.comInfo == null ? "N/A": ConvertString.toHtml(comReportInfo.comInfo)%></TD>
			<TD><%=comReportInfo.comResp == null ? "N/A": ConvertString.toHtml(comReportInfo.comResp)%></TD>		
		</TR>
<%
		}
%>				
	</TBODY>
</TABLE>
<P/>
<%if(right == 3 && !isArchive ){%>
<form name = "frmComReport" method = "post" action="Fms1Servlet">
<input type="hidden" name="reqType" value="0">
<input type="button" name="plAdd" value="<%=languageChoose.getMessage("fi.jsp.plComReportList.AddNew")%>" class = "BUTTON" onclick = "javascript:addClick();">
<% if (comSize > 0) { %>
<INPUT TYPE ="hidden" name = "listUpdate"/>
<input type="button" name="plUpdate" value="<%=languageChoose.getMessage("fi.jsp.plComReportList.Update")%>" class = "BUTTON" onclick = "javascript:updateClick();">
<input type="button" name="plDelete" value="<%=languageChoose.getMessage("fi.jsp.plComReportList.Delete")%>" class = "BUTTON" onclick = "javascript:deleteClick();">
<% } %>
</form>
<%}%>
<BR>
<SCRIPT language="javascript">
	function selectAll(){
		var uCheck = document.getElementsByName("checkForUpdate");
		
		for (i = 0; i < uCheck.length; i++) {
			uCheck[i].checked = document.getElementById("selectAll").checked;
		}
	}
	
	function doBack(){
		frmComReport.reqType.value = <%=Constants.WORKUNIT_HOME%>;
		frmComReport.submit();
	}
	
	function addClick(){		
		frmComReport.reqType.value=<%=Constants.COMREPORT_PRE_ADD%>;	
		frmComReport.submit();
	}
	
	function updateClick(){	
		if (checkBatchUpdate()) {	
			frmComReport.reqType.value=<%=Constants.COMREPORT_PRE_UPDATE%>;
			frmComReport.submit();
		}
	}
	
	function deleteClick(){	
		if (checkBatchDelete()) {
			frmComReport.reqType.value=<%=Constants.COMREPORT_DELETE%>;
			frmComReport.submit();
		}
	}

	function checkBatchUpdate() {	
		var uCheck = document.getElementsByName("checkForUpdate");
		var idList = document.getElementsByName("comID");		
		
		var uList = "";
		
		var uLength = uCheck.length;
		var nChecked = 0; 
		
		for (i = 0; i < uLength; i++) {
			if (uCheck[i].checked) {
				uList = uList + idList[i].value + ",";				
				nChecked = 1;				
			}
		}
		
		if (nChecked == 0) {
			alert("Please choose data to update");
			return false;
		} else {
			uList = uList.substring(0,uList.length-1);
			frmComReport.listUpdate.value = uList;
			return true;
		}
	}
	
	function checkBatchDelete() {	
		var uCheck = document.getElementsByName("checkForUpdate");
		var idList = document.getElementsByName("comID");		
		
		var uList = "";
		var uLength = uCheck.length;
		var nChecked = 0; 
		
		for (i = 0; i < uLength; i++) {
			if (uCheck[i].checked) {
				uList = uList + idList[i].value + ",";				
				nChecked = 1;
			}
		}
		
		if (nChecked == 0) {
			alert("Please choose data to delete");
			return false;
		} else {
			if (confirm("Do you really want to delete ?")) {
				uList = uList.substring(0,uList.length-1);
				frmComReport.listUpdate.value = uList;				
				return true;
			} else return false;
		}
	}
</SCRIPT>

</BODY>
</HTML>
