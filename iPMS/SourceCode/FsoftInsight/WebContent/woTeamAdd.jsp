<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*,com.fms1.html.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>woTeamAdd.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu();">
<%
ProjectDateInfo projectDateInfo = (ProjectDateInfo) session.getAttribute("projectDateInfo");
AssignmentInfo assInfo = null;
SubTeamInfo subTeamInfo = null;
ResponsibilityInfo resInfo = null;
String projectStartDate = CommonTools.dateFormat(projectDateInfo.plannedStartDate);
String projectEndDate = CommonTools.dateFormat(projectDateInfo.plannedEndDate);
String team_source_page = (String) session.getAttribute("team_source_page");
if(team_source_page == null) team_source_page = "0";

int maxRow = Assignments.NUMBER_OF_ROW_ADDABLE;
int nTeamSize = 0;
int nResSize = 0;
int nRow = 1;
int row = 0;

//Vector vtProjectAllocationConflict = (Vector) request.getAttribute("allocationConflict");
boolean isOver = false;// Is Over allocated => if yes, lastAssignment is forwarded (not null)
Vector lastAssignment = (Vector) request.getAttribute("lastAssignment");

if (lastAssignment != null){
	isOver = true;
	nRow = lastAssignment.size();
}

// get all role to assignmet for team menber.
// Avoid this call, it's broken MVC approach, should put this list into request then forward to this JSP
Vector list = Assignments.getResponsibilityList();
if (list != null) nResSize = list.size();

Vector subTeamList = (Vector) session.getAttribute("WOSubTeamList");
if (subTeamList != null) nTeamSize = subTeamList.size();

String title;
if(team_source_page.equals("1")){ // called from project plan
	title=languageChoose.getMessage("fi.jsp.woTeamAdd.ProjectplanTeam");
}
else { // called from Work order
	title=languageChoose.getMessage("fi.jsp.woTeamAdd.WorkOrderTeam");
}

// landd start

String rowStyle;
int assDisplayed;
String strErr = (String) request.getParameter("err");
// landd end

%>
<P class="TITLE"><%=title%></P>
<!-- landd merge start -->
<form name ="frm" method= "post" action = "Fms1Servlet#Team">
<input type = "hidden" name="reqType" value="<%=Constants.WO_TEAM_ADD %>">
<% 
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">System error</P>
<%
	}
%>	
<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woTeamAdd.Addnewassignment")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="24" align = "center"> # </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.woTeamAdd.Name")%>*</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.woTeam.Type")%>*</TD>			
			<TD><%=languageChoose.getMessage("fi.jsp.woTeam.SubTeam")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.woTeam.EffordPercent")%>*</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.woTeam.Startdate")%>*</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.woTeam.Enddate")%>*</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.woTeam.Role")%>*</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.woTeam.Qualification")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.woTeam.Responsibility")%></TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		if (isOver)	assInfo = (AssignmentInfo) lastAssignment.elementAt(row);
%>
		<TR id="ass_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD valign="top"> <%=row + 1%></TD>
			<TD class="CellBGR3">
            	<INPUT name="strAccountName" size="30" type="text" value="<%=isOver ? assInfo.account + "" : ""%>"/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.woTeamAdd.Search")%>" onclick="javascript:onCheckAccount(<%=row%>,event);"> <BR>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Account")%>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Name"><%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Name")%><BR>
            </TD>
            <TD class="CellBGR3">
            	<SELECT name="woTeam_type" class = "COMBO">
            		<OPTION value = "0">&nbsp;</OPTION>
            		<OPTION value = "1" <%=(isOver && assInfo.type == 1) ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.woTeamAdd.Onsite")%></OPTION>
            		<OPTION value = "2" <%=(isOver && assInfo.type == 2) ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.woTeamAdd.Offshore")%></OPTION>
            		<OPTION value = "3" <%=(isOver && assInfo.type == 3) ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.woTeamAdd.Tentative")%></OPTION>
            		<OPTION value = "4" <%=(isOver && assInfo.type == 4) ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.woTeamAdd.Training")%></OPTION>
            		<OPTION value = "5" <%=(isOver && assInfo.type == 5) ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.woTeamAdd.Off")%></OPTION>            		
            	</SELECT>
            </TD>
            <TD class="CellBGR3">
            	<SELECT name="woTeam_TeamName" class = "COMBO">
            		<Option value ="0">&nbsp;</OPTION>
            	<%
   					for (int i = 0; i < nTeamSize; i++) {
						subTeamInfo = (SubTeamInfo)subTeamList.elementAt(i);
   				%>
   					<OPTION value="<%=subTeamInfo.teamID%>" <%=(isOver && (assInfo.teamID == subTeamInfo.teamID)) ? "selected" : ""%>><%=subTeamInfo.teamName%></OPTION>
   				<%
   					}
   				%>
            	</SELECT>            	
            </TD>
            <TD class="CellBGR3">
            	<INPUT type="text" name="woTeam_workingTime" value="<%=isOver ? assInfo.workingTime + "" : ""%>" maxlength="9" size = "9" style="text-align: right">
            </TD>
            <TD class="CellBGR3" nowrap="nowrap">
				<INPUT type="text" name="woTeam_startDate" value="<%=isOver ? CommonTools.dateFormat(assInfo.beginDate) : projectStartDate%>" maxlength = "9" size=9>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showStartDate(<%=row%>)'>
			</TD>
			<TD class="CellBGR3" nowrap="nowrap">
				<INPUT type="text" name="woTeam_endDate" value="<%=isOver ? CommonTools.dateFormat(assInfo.endDate) : projectEndDate%>" maxlength = "9" size=9>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showEndDate(<%=row%>)'>
			</TD>
			<TD class = "CellBGR3">
				<SELECT name="cboRole" class = "COMBO">					
   				<%
   					for (int i = 0; i < nResSize; i++) {
						resInfo = (ResponsibilityInfo)list.elementAt(i);
   				%>
   					<OPTION value="<%=resInfo.id%>" <%=(isOver && assInfo.responsibilityID == resInfo.id) ? "selected" : ""%>> <%=resInfo.name%></OPTION>
   				<%
   					}
   				%>
				</SELECT>
			</TD>
			<TD class="CellBGR3"><TEXTAREA name="woTeam_qualification" rows="5" cols="30"><%=isOver ? assInfo.qualification : ""%></TEXTAREA></TD>
			<TD class="CellBGR3"><TEXTAREA name="woTeam_note" rows="5" cols="30"><%=isOver ? assInfo.note : ""%></TEXTAREA></TD>
		</TR>
<%
	}
	assDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="ass_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD valign="top"> <%=row + 1%></TD>
			<TD class="CellBGR3">
            	<INPUT name="strAccountName" size="30" type="text" value=""/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.woTeamAdd.Search")%>" onclick="javascript:onCheckAccount(<%=row%>,event);"> <BR>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Account")%>
	            <INPUT type="radio" name="rdAccountName<%=row%>" value="Name"><%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Name")%><BR>
            </TD>
            <TD class="CellBGR3">
            	<SELECT name="woTeam_type">
            		<OPTION value = "0">&nbsp;</OPTION>
            		<OPTION value = "1"><%=languageChoose.getMessage("fi.jsp.woTeamAdd.Onsite")%></OPTION>
            		<OPTION value = "2"><%=languageChoose.getMessage("fi.jsp.woTeamAdd.Offshore")%></OPTION>
            		<OPTION value = "3"><%=languageChoose.getMessage("fi.jsp.woTeamAdd.Tentative")%></OPTION>
            		<OPTION value = "4"><%=languageChoose.getMessage("fi.jsp.woTeamAdd.Training")%></OPTION>
            		<OPTION value = "5"><%=languageChoose.getMessage("fi.jsp.woTeamAdd.Off")%></OPTION>            		
            	</SELECT>
            </TD>
            <TD class="CellBGR3">
            	<SELECT name="woTeam_TeamName" class = "COMBO">
            		<Option value ="0">&nbsp;</OPTION>
            	<%
   					for (int i = 0; i < nTeamSize; i++) {
						subTeamInfo = (SubTeamInfo)subTeamList.elementAt(i);
   				%>
   					<OPTION value="<%=subTeamInfo.teamID%>"><%=subTeamInfo.teamName%></OPTION>
   				<%
   					}
   				%>
            	</SELECT>            	
            </TD>
            <TD class="CellBGR3">
            	<INPUT type="text" name="woTeam_workingTime" value="" maxlength="9" size = "9" style="text-align: right">
            </TD>
            <TD class="CellBGR3" nowrap="nowrap">
				<INPUT type="text" name="woTeam_startDate" value="<%=projectStartDate%>" maxlength = "9" size=9>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showStartDate(<%=row%>)'>
			</TD>
			<TD class="CellBGR3" nowrap="nowrap">
				<INPUT type="text" name="woTeam_endDate" value="<%=projectEndDate%>" maxlength = "9" size=9>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showEndDate(<%=row%>)'>
			</TD>
			<TD class = "CellBGR3">
				<SELECT name="cboRole" class = "COMBO">					
   				<%
   					for (int i = 0; i < nResSize; i++) {
						resInfo = (ResponsibilityInfo)list.elementAt(i);
   				%>
   					<OPTION value="<%=resInfo.id%>"> <%=resInfo.name%></OPTION>
   				<%
   					}
   				%>
				</SELECT>
			</TD>
			<TD class="CellBGR3"><TEXTAREA name="woTeam_qualification" rows="5" cols="30"></TEXTAREA></TD>
			<TD class="CellBGR3"><TEXTAREA name="woTeam_note" rows="5" cols="30"></TEXTAREA></TD>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="ass_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.woTeam.AddMoreTeamMembers")%> </a></p>
<BR>
<!-- landd merge end -->
<br>
<INPUT type="button" name="Add" value=" <%=languageChoose.getMessage("fi.jsp.woTeamAdd.OK")%> " onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.woTeamAdd.Cancel")%> " class="BUTTON" onclick="jumpURL('<%if(team_source_page.equals("1")){%>plStructure.jsp<%}else{%>woTeam.jsp<%}%>');">
</form>
<script language = "javascript">
	var ass_nextHiddenIndex = <%=(assDisplayed + 1)%>;
	function addMoreRow() {
	     getFormElement("ass_row" + ass_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
		 ass_nextHiddenIndex++;
		 if(ass_nextHiddenIndex > <%=maxRow%>)
		     getFormElement("ass_addMoreLink").style.display = "none";
		
	}
	init();
	function init(){
	   if(ass_nextHiddenIndex > <%=maxRow%>) 
	       getFormElement("ass_addMoreLink").style.display = "none";    
	}

	frm.strAccountName[0].focus();
<%

	if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null){
%>
		alert("<%=languageChoose.getMessage("fi.jsp.woTeamAdd.AccountError")%>");
		frm.strAccountName[0].focus();
<%
		request.removeAttribute(StringConstants.FILLTER_USER_ERROR);
		request.removeAttribute("errIdx");
	}
	if (request.getAttribute(StringConstants.WO_TEAM_ADD) != null){
%>
		alert("<%=request.getAttribute(StringConstants.WO_TEAM_ADD)%>");
		frm.strAccountName[0].focus();
<%
		request.removeAttribute(StringConstants.WO_TEAM_ADD);
	}
%>	
	
	function showStartDate(row){
		var woTeam_startDate = frm.woTeam_startDate[row];
		showCalendar(woTeam_startDate, woTeam_startDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showEndDate(row){
		var woTeam_endDate = frm.woTeam_endDate[row];
		showCalendar(woTeam_endDate, woTeam_endDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function onCheckAccount(rowCheck, event){
		if (trim(frm.strAccountName[rowCheck].value) == ""){
			window.alert("<%=languageChoose.getMessage("fi.jsp.woTeamAdd.YouMustInputUserAccount")%>");
			frm.strAccountName[rowCheck].focus();
			return ;
		}
	    var rd = document.getElementsByName("rdAccountName".concat(rowCheck));
	    var rdAccountName;
        for(var i=0;i<rd.length;i++){
		  if(rd[i].checked){
		  	rdAccountName = rd[i].value;
		  	break;
		  }
        }
        var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_USER%>" + "&Account=" + frm.strAccountName[rowCheck].value + "&Type=" + rdAccountName;
		javascript:ajax_showOptions(document.forms['frm'].strAccountName[rowCheck], url, '', event);
	}

	function on_Submit(){
		for(i = 0; i < ass_nextHiddenIndex-1; i++) {
			if (trim(frm.strAccountName[i].value) == ""){
				window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.YouMustInputUserAccount")%>");
				frm.strAccountName[i].focus();
				return;
			}
			
			if (!mandatorySelect(frm.woTeam_type[i], 'Please choose user type'))
				return false;
				
			if(isNaN(frm.woTeam_workingTime[i].value) && (frm.woTeam_workingTime[i].value != "")){
				alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.TheValueMustBeANumber")%>");  			
	  			frm.woTeam_workingTime[i].focus();  		
	  			return;  
	  		}	
	 
	  	 	if((frm.woTeam_workingTime[i].value<1) || (frm.woTeam_workingTime[i].value>100)){
				window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.TheEffordMustBeBetween1And100")%>");
	  			frm.woTeam_workingTime[i].focus();  		
	  			return;  
	  		}	
			if((!(isDate(frm.woTeam_startDate[i].value))) && (frm.woTeam_startDate[i].value != "")){  	  
				window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.TheValueInsertedIsNotADate")%>");  			
	  		 	frm.woTeam_startDate[i].focus();
	  		 	return;
	  	 	}
	  	 	if(frm.woTeam_startDate[i].value == ""){
				window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.YouMustEnterStartDate")%>");  			
	  		 	frm.woTeam_startDate[i].focus();
	  		 	return;
	  	 	}
	  	 	
	  	 	if((!(isDate(frm.woTeam_endDate[i].value))) && (frm.woTeam_endDate[i].value != "")){
				window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.TheValueInsertedIsNotADate")%>");  			
	  		 	frm.woTeam_endDate[i].focus();
	  		 	return;
	  	 	}
	  	 	if(frm.woTeam_endDate[i].value == ""){
				window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.YouMustEnterEndDate")%>");  			
	  		 	frm.woTeam_endDate[i].focus();
	  		 	return;
	  	 	}
	  	 	
	  	 	if(compareDate(frm.woTeam_startDate[i].value, frm.woTeam_endDate[i].value) == -1){
				window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.TheEndDateMustBeLaterThanTheBeginDate")%>");  			
	  		 	frm.woTeam_endDate[i].focus();
	  		 	return;
	  	 	}
	  	 	
	  	 	<%if(!projectStartDate.equals("") ){%>
	  	 		if(compareDate(frm.woTeam_startDate[i].value,"<%=projectStartDate%>") == 1){
					window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.ThisDateIsOutOfRangeOfTheProjectPlanning")%>");  			
		  		 	frm.woTeam_startDate[i].focus();
		  		 	return;
	  	 		}
	  	 		if(compareDate("<%=projectStartDate%>",frm.woTeam_endDate[i].value) == -1){
					window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.ThisDateIsOutOfRangeOfTheProjectPlanning")%>");  			
		  		 	frm.woTeam_endDate[i].focus();
		  		 	return;
	  	 		}
	
	  	 	<%}if(!projectEndDate.equals("")){%>
	  	 		if(compareDate(frm.woTeam_startDate[i].value,"<%=projectEndDate%>") == -1){
					window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.ThisEndDateIsOutOfRangeOfTheProjectPlanning")%>");  			
		  		 	frm.woTeam_startDate[i].focus();
		  		 	return;
	  	 		}
	  	 		if(compareDate( "<%=projectEndDate%>",frm.woTeam_endDate[i].value) == 1){
					window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamAdd.ThisEndDateIsOutOfRangeOfTheProjectPlanning")%>");  			
		  		 	frm.woTeam_endDate[i].focus();
		  		 	return;
	  	 		}
	            
	  	 	<%}%>
	  	 	if (frm.woTeam_qualification[i].value.length >200){
	  	 		alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woTeamAdd.The__length__of__this__field__must__be__below__200__characters__currently__~PARAM1_NOTELEN~")%>', frm.woTeam_qualification[i].value.length )));
		  	 	frm.woTeam_qualification[i].focus();
		  	 	return;
	  	 	}
	  	 	
	  	 	if (frm.woTeam_note[i].value.length >200){
	  	 		alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woTeamAdd.The__length__of__this__field__must__be__below__200__characters__currently__~PARAM1_NOTELEN~")%>', frm.woTeam_note[i].value.length )));
		  	 	frm.woTeam_note[i].focus();
		  	 	return;
	  	 	}
	  	 }
		document.frm.submit();
	}	
	
</script>
</BODY>
</HTML>