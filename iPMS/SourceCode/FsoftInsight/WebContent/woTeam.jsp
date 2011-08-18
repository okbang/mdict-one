<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<LINK rel="stylesheet" type="text/css" href="stylesheet/fms.css">
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woTeam.jsp</TITLE>
</HEAD>

<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right = Security.securiPage("Work Order",request,response); 
	
	Vector vtProjectAllocationConflict = (Vector) request.getAttribute("allocationConflict");
	if (vtProjectAllocationConflict != null) request.removeAttribute("allocationConflict");
	boolean isOver = false;// Is Over allocated => if yes, lastAssignment is forwarded (not null)
	AssignmentInfo lastAssignment = (AssignmentInfo) request.getAttribute("lastAssignment");
	if (lastAssignment != null) request.removeAttribute("lastAssignment");
	
	Double totalCalendarEffort = (Double) session.getAttribute("totalCalendarEffort");
	String strGroupName = (String)session.getAttribute("GroupName");
	// landd get sub team information start
	Vector subTeamList = (Vector) session.getAttribute("WOSubTeamList");
	// landd get sub team information end
	
	Vector teamList = (Vector) session.getAttribute("WOTeamList");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}

	if (teamList.size() > 23){%>
<BODY onload="Import();loadPrjMenu();" class="BD">
<%} else {%>
<BODY class="BD" onload="loadPrjMenu()">
<%}%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woTeam.WorkorderSubTeamTeam")%> </P>
<br>
<!-- landd add sub team table start -->
<TABLE cellspacing="1" class="Table" id="tableSubTeamAllocated">
	<CAPTION class="TableCaption"><A name="SubTeam"><%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam")%></A></CAPTION>
		<THEAD>
			<TR class="ColumnLabel">
				<TD width = "24" align = "center"> # </TD>
				<TD width="180"><%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.SubTeamName")%></TD>
				<TD width="300"> <%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.Note")%> </TD>
			</TR>
		</THEAD>
		<TBODY>	
<%
		String className;
		SubTeamInfo subTeamInfo;
		int subTeamSize = subTeamList.size();
		for(int i = 0; i < subTeamSize; i++){
			subTeamInfo = (SubTeamInfo) subTeamList.elementAt(i);
		 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
%>		
			<TR class="<%=className%>">
				<TD width = "24" align = "center"><%=i+1%></TD>
				<TD><%=ConvertString.toHtml(subTeamInfo.teamName)%></TD>
				<TD><%=subTeamInfo.teamNote==null? "N/A":(subTeamInfo.teamNote)%></TD>
			</TR>
<%
		}
%>			
		</TBODY>
</TABLE>
<BR>
<%if(right == 3 && !isArchive ){%>
<form name = "frmSubTeam" method = "post" action="Fms1Servlet">
<input type="hidden" name="reqType" value="0">
<input type="button" name="woSubTeamAdd" value="<%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.Addnew")%>" class = "BUTTON" onclick = "addSubmit();">
<% if (subTeamSize > 0) { %>
<input type="button" name="woSubTeamUpdate" value="<%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.Update")%>" class = "BUTTON" onclick = "updateSubmit();">
<% } %>
<INPUT type="button" name="woBack" value="<%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam.Back")%>" class="BUTTON" onclick="doBack()">
</form>
<%}%>
<BR>
<!-- landd add sub team table end -->

<br>
<!-- landd hide start
<TABLE cellspacing="1" class="HDR" width="100%">
	<TBODY>
		<TR>
			<TD width="10%"></TD>
			<TD></TD>
			<TD align="right"><P align=right><%if(right == 3 && !isArchive){%><a href="javascript:Import();"><%=languageChoose.getMessage("fi.jsp.woTeam.ImportTeam")%></a><%}%></p></TD>
		</TR>
	</TBODY>
</TABLE>
<br>

<FORM name="frm_Import" action="Fms1Servlet?reqType=<%=Constants.WO_TEAM_IMPORT%>" enctype="MULTIPART/FORM-DATA" method=POST >
<TABLE cellspacing="1" class="HDR" width="95%" id="ImportTable">
	<TBODY>
		<TR>
			<TD align="right"><STRONG>File Name&nbsp;*</STRONG></TD>
	        <TD align="left">
	        	<INPUT type="file" name="importFile">
	       		<INPUT type="button" name="Import" onclick="checkName()" value=" Import " class="Button">
            	<INPUT type="hidden" name="filename">
	        </TD>
	        </TR>
	         <TR>
	         	<TD></TD>
	         	<TD align="center"><A href="Template_Import_WORKORDER.xls"><FONT color="blue" class="label1">Download Template File</FONT></A></TD>
	         </TR>
	</TBODY>
</TABLE>
</FORM>
landd hide end -->
<BR>

<%
String checkImport = (String)session.getAttribute("Imported");
if(checkImport == "true"){
	int[] result = (int[])session.getAttribute("AddedRecord");
	int importedOK = 0;
	int importedFail = 0;
	int l = 0;
	while(l < 50){
			if(result[l] > 0){
				importedOK ++;
			}else if(result[l] < 0){
				importedFail++;
			}
			l++;
		}
	if(importedOK > 0){
	%><br>Imported successfull <%=importedOK%> records:<%
		l=0;
		importedOK =0;
		while(l < 50){
			if(result[l] > 0){
				if(importedOK > 0){
					%> ,<%
				}
				%>
				<%=result[l]%>
				<%
				importedOK ++;
			}
			l++;
		}
	}
	if(importedFail > 0){
	%><br>Imported failed <%=importedFail%> records:<%
		l=0;
		importedFail =0;
		while(l < 50){
			if(result[l] < 0){
				if(importedFail > 0){
					%> ,<%
				}
				%>
				<font color="red"><%=-result[l]%></font>
				<%
				importedFail ++;
			}
			l++;
		}
	}
 	  %>
	  <br>
	  <br>
	  <%
	if(importedOK == 0){
	%><br>0 record is imported because one of these reasons :
	  <br>1. Allocation is conflicted
	  <br>2. This assignment have been exist! please update it in assignments list
	  <br>
	  <br>
	<%
	}
	if(importedFail > 0 && importedOK > 0){
	%><br>There are <%=importedFail%> records is failed because one of these reasons :
	  <br>1. Allocation is conflicted
	  <br>2. This assignment have been exist! please update it in assignments list
	  <br>
	  <br>
	<%
	}
	session.removeAttribute("Imported");
	session.removeAttribute("AddedRecord");
}
%>
<%
String isImport = (String)session.getAttribute("ImportFail");
if(isImport == "fail"){
	%><br><p style="color: red;">Import Fail<%	
	session.removeAttribute("ImportFail");
}
%>

<%
if ((vtProjectAllocationConflict != null) && (vtProjectAllocationConflict.size() > 0)) {%>
<TABLE cellspacing="1" class="Table" id="tableAllocated">
		<CAPTION align="left" class="Error">Conflict in resource allocation. Please check below :</CAPTION>
		<THEAD>
			<TR class="ColumnLabel">
				<TD width="100"><%=languageChoose.getMessage("fi.jsp.woTeam.ProjectCode")%></TD>				
				<TD><%=languageChoose.getMessage("fi.jsp.woTeam.Account")%></TD>
				<TD width="70"><%=languageChoose.getMessage("fi.jsp.woTeam.EffordPercent")%></TD>
				<TD width="70"><%=languageChoose.getMessage("fi.jsp.woTeam.Startdate")%></TD>
				<TD width="70"><%=languageChoose.getMessage("fi.jsp.woTeam.Enddate")%></TD>
			</TR>
		</THEAD>
		<TBODY>
<%
		String lastDevName = "";
		int aSize = vtProjectAllocationConflict.size()-1;
		AssignmentInfo info = null;
		AssignmentInfo nextInfo = null;
		for(int i = 0; i < aSize; i++){
		 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
		 	info = (AssignmentInfo) vtProjectAllocationConflict.elementAt(i);
		 	if (aSize - i > 0) 	nextInfo = (AssignmentInfo) vtProjectAllocationConflict.elementAt(i+1);
		 	if (i==0) {
%>
			<TR class="CellBGRLongnews" style="font-weight: bold">
				<TD colspan="5">&nbsp;&nbsp;<%=ConvertString.toHtml(info.devName)%></TD>
			</TR>
<%
			}
		 	if ( !(nextInfo.devName).equals(info.devName) ) {
%>
			<TR class="Error">
				<TD><%=languageChoose.getMessage("fi.jsp.woTeamAdd.OverAllocated")%></TD>				
				<TD><%=ConvertString.toHtml(info.account.toLowerCase())%></TD>
				<TD><%=info.workingTime%></TD>
				<TD nowrap="nowrap"><%=CommonTools.dateFormat(info.beginDate)%></TD>
				<TD nowrap="nowrap"><%=CommonTools.dateFormat(info.endDate)%></TD>
			</TR>
			<TR class="CellBGRLongnews" style="font-weight: bold">
				<TD colspan="5">&nbsp;&nbsp;<%=ConvertString.toHtml(nextInfo.devName)%></TD>
			</TR>
<% 	
			} else {
%>
			<TR class="<%=className%>">
				<TD><%=ConvertString.toHtml(info.projectCode)%></TD>				
				<TD><%=ConvertString.toHtml(info.account.toLowerCase())%></TD>
				<TD style="text-align: left"><%=info.workingTime%></TD>
				<TD nowrap="nowrap"><%=CommonTools.dateFormat(info.beginDate)%></TD>
				<TD nowrap="nowrap"><%=CommonTools.dateFormat(info.endDate)%></TD>
			</TR>
<%
			}
		} // end for
		info = (AssignmentInfo) vtProjectAllocationConflict.elementAt(aSize);
%>	
			<TR class="Error">
				<TD><%=languageChoose.getMessage("fi.jsp.woTeamAdd.OverAllocated")%></TD>				
				<TD><%=ConvertString.toHtml(info.account.toLowerCase())%></TD>
				<TD><%=info.workingTime%></TD>
				<TD nowrap="nowrap"><%=CommonTools.dateFormat(info.beginDate)%></TD>
				<TD nowrap="nowrap"><%=CommonTools.dateFormat(info.endDate)%></TD>
			</TR>
		</TBODY>
</TABLE>
<BR>
<%}%>
<BR>

<P class="HDR"> <%=languageChoose.getMessage("fi.jsp.woTeam.TotalCalendarEffort")%> (<%=languageChoose.getMessage("person.day")%>): <%=CommonTools.formatDouble(totalCalendarEffort.doubleValue())%></P>
<TABLE cellspacing="1" class="Table" width="100%" id="tableTeam">
	<CAPTION class="TableCaption"><A name="Team"><%=languageChoose.getMessage("fi.jsp.woTeam.Team")%></A></CAPTION>
		<THEAD>
			<TR class="ColumnLabel">
				<TD><INPUT id="selectAll" type ="checkbox" name="selectAll" onclick="selectAll()"/></TD>
				<TD width = "24" align = "center">#</TD>
				<TD><%=languageChoose.getMessage("fi.jsp.woTeam.Role")%></TD>
				<TD width="150"><%=languageChoose.getMessage("fi.jsp.woTeam.Responsibility")%></TD>
				<TD width="100"><%=languageChoose.getMessage("fi.jsp.woTeam.Qualification")%></TD>
				<TD width ="15%"><%=languageChoose.getMessage("fi.jsp.woTeam.FullName")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.woTeam.Account")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.woTeam.Type")%></TD>
				<TD align ="center"><%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam")%></TD>
				<TD width="40" align="center"><%=languageChoose.getMessage("fi.jsp.woTeam.EffordPercent")%></TD>
				<TD width="70"><%=languageChoose.getMessage("fi.jsp.woTeam.Startdate")%></TD>
				<TD width="70"><%=languageChoose.getMessage("fi.jsp.woTeam.Enddate")%></TD>
				<TD width="40"><%=languageChoose.getMessage("fi.jsp.woTeam.Calendar")%></TD>
			</TR>
		</THEAD>
		<TBODY>
	<%
		//String className;
		int tSize = teamList.size();
		for(int i = 0; i < tSize; i++){
		 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
		 	AssignmentInfo info = (AssignmentInfo) teamList.elementAt(i);
	%>
			<TR class="<%=className%>">
				<TD><INPUT type ="checkbox" name="checkForUpdate"/></TD>
				<TD align = "center"><%=i+1%>
				<INPUT TYPE = "hidden" name = "assID" value = "<%=info.assID%>"/>
				<INPUT TYPE = "hidden" name = "devID" value = "<%=info.devID%>"/>
				</TD>
				<TD><%=((info.roleID == null)? "N/A" : info.roleID)%></TD>
				<TD><%=((info.note == null)? "N/A" :ConvertString.toHtml(info.note))%></TD>
				<TD><%=((info.qualification == null)? "N/A" :ConvertString.toHtml(info.qualification))%></TD>
				<TD><%if(right == 3 && !isArchive){%><a href="Fms1Servlet?reqType=<%=Constants.WO_TEAM_MNG%>&woTeam_assID=<%=info.assID%>"><%}%><%=ConvertString.toHtml(info.devName)%><%if(right == 3 && !isArchive ){%></a><%}%></TD>
				<TD><%=ConvertString.toHtml(info.account.toLowerCase())%></TD>
				<TD>
				<%
				switch(info.type){
					case 1:%><%=languageChoose.getMessage("fi.jsp.woTeam.Onsite")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.woTeam.Offshore")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.woTeam.Tentative")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.woTeam.Training")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.woTeam.Off")%><%break;
				}%>
				</TD>
				<TD><%=((info.teamName == null)? "N/A" :(info.teamName))%></TD>
				<TD style="text-align: left"><%=info.workingTime%></TD> 
				<TD nowrap="nowrap"><%=CommonTools.dateFormat(info.beginDate)%></TD>
				<TD nowrap="nowrap"><%=CommonTools.dateFormat(info.endDate)%></TD>
				<TD style="text-align: left"><b><%=info.total%></b></TD>				
				
			</TR>
	<%
		}
	%>
		</TBODY>
</table>
<BR>
<%if(right == 3 && !isArchive ){%>
<FORM name="frm_teamAdd" action="Fms1Servlet" method = "POST">
	<input type="hidden" name="reqType" value="<%=Constants.WO_TEAM_ADD_PREPARE%>" >
	<input type="hidden" name="subType" value="0" >
	<input type="hidden" name="listUpdate" value="0" >
	<input type="hidden" name="devListUpdate" value="0" >
	<input type="submit" name="woTeamAdd" value="<%=languageChoose.getMessage("fi.jsp.woTeam.Addnew")%>" class = "BUTTON">	
<% if (tSize > 0) { %>
	<input type="button" name="woTeamUpdate" value="<%=languageChoose.getMessage("fi.jsp.woTeam.Update")%>" class = "BUTTON" onclick=" return checkBatchUpdate();">
	<input type="button" name="woTeamDelete" value="<%=languageChoose.getMessage("fi.jsp.woTeam.Delete")%>" class = "BUTTON" onclick=" return checkDelete();">
<% } %>
	<INPUT type="button" name="woBack" value="<%=languageChoose.getMessage("fi.jsp.HumanResource.Back")%>" class="BUTTON" onclick="doBack()">
</FORM>
<%}%>

<SCRIPT language="javascript">
/*
var isImportHide = true;
	function Import(){
		isImportHide = !isImportHide;
	 	var ImportTable = document.getElementById("ImportTable");
  		if (isImportHide) {
    		ImportTable.style.display="";
   		}else{
    		ImportTable.style.display="none";
		}
    }
*/
function Import(){
}
</SCRIPT>

<SCRIPT language="javascript">
/*
function doImport(ext) {
    if (ext != '') {
        document.frm_Import.submit();
    }
}

function checkName() {
    var name = document.frm_Import.importFile.value;
    
    if (name == "" || name == null) {
        alert("Select MS Excel file!");
        document.frm_Import.importFile.focus();
        return;
    }
    else {
        var ext = name.substring(name.lastIndexOf(".") + 1, name.length);
        ext = ext.toLowerCase();
        if (ext != "xls") {
            alert("Select MS Excel file!");
            document.frm_Import.importFile.focus();
            return;
        }
        else {
            doImport(ext);
        }
    }
}
*/
</SCRIPT>

<SCRIPT language="javascript">
	function doBack(){
	<%if (strGroupName != null && !"".equals(strGroupName)){%>
		frm_teamAdd.action = "Fms1Servlet?reqType=<%=Constants.GROUP_CALENDAR_EFFORT%>&groupName=<%=strGroupName%>";
	<%}else{%>
		frm_teamAdd.action = "Fms1Servlet?reqType=<%=Constants.WORKUNIT_HOME%>";
	<%}%>
		frm_teamAdd.submit();
	}
	
	function selectAll(){
		var uCheck = document.getElementsByName("checkForUpdate");
		
		for (i = 0; i < uCheck.length; i++) {
			uCheck[i].checked = document.getElementById("selectAll").checked;
		}
	}
	function addSubmit(){
		frmSubTeam.reqType.value = <%=Constants.WO_SUB_TEAM_ADD_PREPARE%>;
		frmSubTeam.submit();
	}
	
	function checkBatchUpdate() {	
		var uCheck = document.getElementsByName("checkForUpdate");
		var idList = document.getElementsByName("assID");		
		
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
			frm_teamAdd.listUpdate.value = uList;			
			frm_teamAdd.action = "Fms1Servlet?reqType=<%=Constants.WO_TEAM_BATCH_UPDATE_PREPARE%>";
			frm_teamAdd.submit();
		}
	}
	
	function checkDelete() {	
		var uCheck = document.getElementsByName("checkForUpdate");
		var idList = document.getElementsByName("assID");
		var idevList = document.getElementsByName("devID");
		
		var uList = "";
		var devList = "";
		var uLength = uCheck.length;
		var nChecked = 0; 
		
		for (i = 0; i < uLength; i++) {
			if (uCheck[i].checked) {
				uList = uList + idList[i].value + ",";
				devList = devList + idevList[i].value + ",";
				nChecked = 1;				
			}
		}
		
		if (nChecked == 0) {
			alert("Please choose data to delete");
			return false;
		} else {
			if (confirm("Do you really want to delete ?")) {
				uList = uList.substring(0,uList.length-1);
				frm_teamAdd.listUpdate.value = uList;
				frm_teamAdd.devListUpdate.value = devList;
				frm_teamAdd.action = "Fms1Servlet?reqType=<%=Constants.WO_TEAM_BATCH_DELETE%>";
				frm_teamAdd.submit();
			} else return false;
		}
	}
	
	function updateSubmit(){
		frmSubTeam.reqType.value = <%=Constants.WO_SUB_TEAM_UPDATE_PREPARE%>;
		frmSubTeam.submit();
	}
</SCRIPT>
<SCRIPT>
	/*Import();*/
</SCRIPT>
</BODY>
</HTML>