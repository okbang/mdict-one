<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plSercureAspectsList.jsp</TITLE>
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
	
	Vector vDefAcrList = (Vector) session.getAttribute("DefAcrList");
	int defSize = 0;	
	
	String archiveStatus = (String) session.getAttribute("archiveStatus");
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
		if (Integer.parseInt(archiveStatus) == 4) {
			isArchive = true;
		}
	}
%>

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plDefAcrList.ProjectPlanDefinitions_N_Acronyms")%></P>
<BR>
<TABLE cellspacing="1" width="95%" class="Table" id="tableComReport">
	<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.plDefAcrList.Definitions_N_AcronymsList")%></CAPTION>
		<THEAD>
			<TR class="ColumnLabel">
				<TD width="3%" align="center">&nbsp;</TD>
				<TD width="3%" align="center"> # </TD>
				<TD width="30%"> <%=languageChoose.getMessage("fi.jsp.plDefAcrList.Acronym")%> </TD>
				<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plDefAcrList.Definition")%> </TD>
				<TD width="26%"> <%=languageChoose.getMessage("fi.jsp.plDefAcrList.Note")%> </TD>				
			</TR>
		</THEAD>
		<TBODY>	
<%
		String className;
		DefAcrInfo defInfo = null;
		if (vDefAcrList != null) defSize = vDefAcrList.size();
		
		for(int i = 0; i < defSize; i++){
			defInfo = (DefAcrInfo) vDefAcrList.elementAt(i);
		 	className=(i%2==0)? "CellBGR3":"CellBGRnews";		 	
		
%>
		<TR class="<%=className%>">
			<TD align="center"><INPUT type ="checkbox" name="checkForUpdate"/></TD>
			<TD align="center"><%=i+1%><INPUT TYPE ="hidden" name ="defID" value = "<%=defInfo.defID%>"/></TD>
			<TD><%=defInfo.acronym == null ? "N/A": ConvertString.toHtml(defInfo.acronym)%></TD>			
			<TD><%=defInfo.definition == null ? "N/A": ConvertString.toHtml(defInfo.definition)%></TD>
			<TD><%=defInfo.note == null ? "N/A": ConvertString.toHtml(defInfo.note)%></TD>
		</TR>

<%			
		}// end for		
%>	
	</TBODY>
</TABLE>
<P/>
<%if(right == 3 && !isArchive ){%>
<form name = "frmSercurityAspects" method = "post" action="Fms1Servlet">
<input type="hidden" name="reqType" value="0">
<input type="button" name="plAdd" value="<%=languageChoose.getMessage("fi.jsp.plDefAcrList.AddNew")%>" class = "BUTTON" onclick = "javascript:addClick();">
<% if (defSize > 0) { %>
<INPUT TYPE ="hidden" name = "listUpdate"/>
<input type="button" name="plUpdate" value="<%=languageChoose.getMessage("fi.jsp.plDefAcrList.Update")%>" class = "BUTTON" onclick = "javascript:updateClick();">
<input type="button" name="plDelete" value="<%=languageChoose.getMessage("fi.jsp.plDefAcrList.Delete")%>" class = "BUTTON" onclick = "javascript:deleteClick();">
<% } %>
</form>
<%}%>
<BR>
<SCRIPT language="javascript">

	function doBack(){
		frmSercurityAspects.reqType.value = <%=Constants.WORKUNIT_HOME%>;
		frmSercurityAspects.submit();
	}
	
	function addClick(){		
		frmSercurityAspects.reqType.value=<%=Constants.DEFINITIONS_ACRONYMS_PRE_ADD%>;	
		frmSercurityAspects.submit();
	}
	
	function updateClick(){	
		if (checkBatchUpdate()) {	
			frmSercurityAspects.reqType.value=<%=Constants.DEFINITIONS_ACRONYMS_PRE_UPDATE%>;
			frmSercurityAspects.submit();
		}
	}
	
	function deleteClick(){	
		if (checkBatchDelete()) {
			frmSercurityAspects.reqType.value=<%=Constants.DEFINITIONS_ACRONYMS_DELETE%>;
			frmSercurityAspects.submit();
		}
	}

	function checkBatchUpdate() {	
		var uCheck = document.getElementsByName("checkForUpdate");
		var idList = document.getElementsByName("defID");		
		
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
			frmSercurityAspects.listUpdate.value = uList;
			return true;
		}
	}
	
	function checkBatchDelete() {	
		var uCheck = document.getElementsByName("checkForUpdate");
		var idList = document.getElementsByName("defID");		
		
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
				frmSercurityAspects.listUpdate.value = uList;				
				return true;
			} else return false;
		}
	}
</SCRIPT>

</BODY>
</HTML>
