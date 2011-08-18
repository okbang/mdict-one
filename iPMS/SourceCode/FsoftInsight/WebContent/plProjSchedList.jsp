<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plProjSchedList.jsp</TITLE>
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
	
	Vector vProjSchedList = (Vector) session.getAttribute("ProjectScheduleList");
	int pSize = 0;
	if (vProjSchedList != null) pSize = vProjSchedList.size();
	
	String archiveStatus = (String) session.getAttribute("archiveStatus");
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
		if (Integer.parseInt(archiveStatus) == 4) {
			isArchive = true;
		}
	}
%>

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ProjectPlanProjectSchedule")%></P>
<BR>
<TABLE cellspacing="1" width="95%" class="Table" id="tableProjectSchedule">
	<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ProjectSchedule")%></CAPTION>
		<THEAD>
			<TR class="ColumnLabel">
				<TD width="3%" align="center"><INPUT id="selectAll" type ="checkbox" name="selectAll" onclick="selectAll()"/></TD>
				<TD width="3%" align="center"> # </TD>
				<TD width="21%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedList.Activity")%> </TD>
				<TD width="8%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedList.StartDate")%> </TD>
				<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedList.Responsible")%> </TD>
				<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedList.Note")%> </TD>
			</TR>
		</THEAD>
		<TBODY>	
<%
		String className;
		ProjSchedInfo projSchedInfo;		
		int currentType = 0;
		int nextType = 0;
		int classType = 0;
		for(int i = 0; i < pSize-1; i++){
			projSchedInfo = (ProjSchedInfo) vProjSchedList.elementAt(i);
		 	className=(classType%2==0)? "CellBGR3":"CellBGRnews";
		 	currentType = projSchedInfo.schedType;		 	
		 	nextType = ((ProjSchedInfo) vProjSchedList.elementAt(i+1)).schedType;		 	
		 	if (i == 0) {
%>		
		<TR class="CellBGRLongnews" style="font-weight: bold">
			<TD colspan="6">&nbsp;
			<%
				switch(currentType){
					case 1:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DefectPreventionType")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QualityControlType")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ProjectTrackingType")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ConfigurationManagementType")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QAType")%><%break;
					case 6:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DARType")%><%break;
					case 7:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.OtherType")%><%break;
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
			<TD align="center"><%=i+1%><INPUT TYPE ="hidden" name ="schedID" value = "<%=projSchedInfo.schedID%>"/></TD>
			<TD><%=projSchedInfo.schedActivity == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedActivity)%></TD>
			<TD><%=projSchedInfo.schedStartDate == null ? "N/A": CommonTools.dateFormat(projSchedInfo.schedStartDate)%></TD>
			<TD><%=projSchedInfo.schedResponsible == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedResponsible)%></TD>
			<TD><%=projSchedInfo.schedNote == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedNote)%></TD>				
		</TR>
		<TR class="CellBGRLongnews" style="font-weight: bold">
			<TD colspan="6">&nbsp;
			<%
				switch(nextType){
					case 1:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DefectPreventionType")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QualityControlType")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ProjectTrackingType")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ConfigurationManagementType")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QAType")%><%break;
					case 6:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DARType")%><%break;
					default :%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.OtherType")%><%break;
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
			<TD align="center"><%=i+1%><INPUT TYPE ="hidden" name ="schedID" value = "<%=projSchedInfo.schedID%>"/></TD>
			<TD><%=projSchedInfo.schedActivity == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedActivity)%></TD>
			<TD><%=projSchedInfo.schedStartDate == null ? "N/A": CommonTools.dateFormat(projSchedInfo.schedStartDate)%></TD>
			<TD><%=projSchedInfo.schedResponsible == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedResponsible)%></TD>
			<TD><%=projSchedInfo.schedNote == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedNote)%></TD>				
		</TR>
<%			
			}
		}// end for
		if (pSize > 0) {
			projSchedInfo = (ProjSchedInfo) vProjSchedList.elementAt(pSize-1);
			currentType = projSchedInfo.schedType;		 	
			className=(pSize%2==0)? "CellBGR3":"CellBGRnews";
			if (pSize == 1) {
%>
		<TR class="CellBGRLongnews" style="font-weight: bold">
			<TD colspan="6">&nbsp;
			<%
				switch(currentType){
					case 1:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DefectPreventionType")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QualityControlType")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ProjectTrackingType")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ConfigurationManagementType")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QAType")%><%break;
					case 6:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DARType")%><%break;
					default :%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.OtherType")%><%break;
				}
			%>
			</TD>
		</TR>

<%
			}
%> 
		<TR class="<%=className%>">
			<TD align="center"><INPUT type ="checkbox" name="checkForUpdate"/></TD>
			<TD align="center"><%=pSize%><INPUT TYPE ="hidden" name ="schedID" value = "<%=projSchedInfo.schedID%>"/></TD>
			<TD><%=projSchedInfo.schedActivity == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedActivity)%></TD>
			<TD><%=projSchedInfo.schedStartDate == null ? "N/A": CommonTools.dateFormat(projSchedInfo.schedStartDate)%></TD>
			<TD><%=projSchedInfo.schedResponsible == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedResponsible)%></TD>
			<TD><%=projSchedInfo.schedNote == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedNote)%></TD>				
		</TR>
<%
		}	
%>		
	</TBODY>
</TABLE>
<P/>
<%if(right == 3 && !isArchive ){%>
<form name = "frmProjectSchedule" method = "post" action="Fms1Servlet">
<input type="hidden" name="reqType" value="0">
<input type="button" name="plAdd" value="<%=languageChoose.getMessage("fi.jsp.plProjSchedList.AddNew")%>" class = "BUTTON" onclick = "javascript:addClick();">
<% if (pSize > 0) { %>
<INPUT TYPE ="hidden" name = "listUpdate"/>
<input type="button" name="plUpdate" value="<%=languageChoose.getMessage("fi.jsp.plProjSchedList.Update")%>" class = "BUTTON" onclick = "javascript:updateClick();">
<input type="button" name="plDelete" value="<%=languageChoose.getMessage("fi.jsp.plProjSchedList.Delete")%>" class = "BUTTON" onclick = "javascript:deleteClick();">
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
		frmProjectSchedule.reqType.value = <%=Constants.WORKUNIT_HOME%>;
		frmProjectSchedule.submit();
	}
	
	function addClick(){		
		frmProjectSchedule.reqType.value=<%=Constants.PL_PROJECT_SCHEDULE_PREPARE_ADD%>;	
		frmProjectSchedule.submit();
	}
	
	function updateClick(){	
		if (checkBatchUpdate()) {	
			frmProjectSchedule.reqType.value=<%=Constants.PL_PROJECT_SCHEDULE_PREPARE_UPDATE%>;
			frmProjectSchedule.submit();
		}
	}
	
	function deleteClick(){	
		if (checkBatchDelete()) {
			frmProjectSchedule.reqType.value=<%=Constants.PL_PROJECT_SCHEDULE_DELETE%>;
			frmProjectSchedule.submit();
		}
	}

	function checkBatchUpdate() {	
		var uCheck = document.getElementsByName("checkForUpdate");
		var idList = document.getElementsByName("schedID");		
		
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
			frmProjectSchedule.listUpdate.value = uList;			
			frmProjectSchedule.action = "Fms1Servlet?reqType=<%=Constants.PL_PROJECT_SCHEDULE_PREPARE_UPDATE%>";
			frmProjectSchedule.submit();
		}
	}
	
	function checkBatchDelete() {	
		var uCheck = document.getElementsByName("checkForUpdate");
		var idList = document.getElementsByName("schedID");		
		
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
				frmProjectSchedule.listUpdate.value = uList;				
				frmProjectSchedule.action = "Fms1Servlet?reqType=<%=Constants.PL_PROJECT_SCHEDULE_DELETE%>";
				frmProjectSchedule.submit();
			} else return false;
		}
	}
</SCRIPT>

</BODY>
</HTML>
