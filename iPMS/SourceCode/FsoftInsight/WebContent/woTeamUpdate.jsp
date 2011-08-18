<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*,com.fms1.html.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>woTeamUpdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu();frm_teamUpdate.woTeam_type[0].focus()">
<%
ProjectDateInfo projectDateInfo = (ProjectDateInfo) session.getAttribute("projectDateInfo");
String startDate = CommonTools.dateFormat(projectDateInfo.plannedStartDate);
String endDate = CommonTools.dateFormat(projectDateInfo.plannedEndDate);
long assID=0;
if (ConvertString.isNumber(request.getParameter("assID")))
	assID=Long.parseLong(request.getParameter("assID"));
else
	Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);

String title=languageChoose.getMessage("fi.jsp.woTeamUpdate.WorkOrderTeam");
Vector teamList=(Vector)session.getAttribute("WOTeamList");
AssignmentInfo assignmentInfo=null;
for (int i=0;i<teamList.size();i++){
	assignmentInfo =(AssignmentInfo)teamList.elementAt(i);
	if (assignmentInfo.assID ==assID)
		break;
}
if (assignmentInfo==null)//trying to update a user out of current project
	Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);

int nTeamSize = 0;
Vector subTeamList = (Vector) session.getAttribute("WOSubTeamList");
if (subTeamList != null) nTeamSize = subTeamList.size();

Vector vtProjectAllocationConflict = (Vector) request.getAttribute("allocationConflict");
boolean isOver = false;// Is Over allocated => if yes, lastAssignment is forwarded (not null)
AssignmentInfo lastAssignment = (AssignmentInfo) request.getAttribute("lastAssignment");
%>
<form name="frm_teamUpdate" action="Fms1Servlet" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.WO_TEAM_UPDATE%>">
<input type = "hidden" name="woTeam_assID" value="<%=assignmentInfo.assID%>">
<P class="TITLE"><%=title%></P>
<br>
<%
if ((vtProjectAllocationConflict != null) && (vtProjectAllocationConflict.size() > 0)) {%>
<TABLE cellspacing="1" class="Table" id="tableAllocated">
<CAPTION align="left" class="Error"><%=languageChoose.getMessage("fi.jsp.woTeamAdd.OverAllocatedError")%></CAPTION>
<THEAD>
<TR class="ColumnLabel">
<TD width="100"> <%=languageChoose.getMessage("fi.jsp.woTeam.ProjectCode")%> </TD>
<TD width="120"> <%=languageChoose.getMessage("fi.jsp.woTeam.Name")%> </TD>
<TD width="70"> <%=languageChoose.getMessage("fi.jsp.woTeam.Workingtime")%> </TD>
<TD width="100"> <%=languageChoose.getMessage("fi.jsp.woTeam.Startdate")%> </TD>
<TD width="100"> <%=languageChoose.getMessage("fi.jsp.woTeam.Enddate")%> </TD>
</TR>
</THEAD>
<TBODY>
<%
isOver = true;
assignmentInfo = lastAssignment;  // If conflict found then get last submitted information
String className;
for(int i = 0; i < vtProjectAllocationConflict.size() - 1; i++){
 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
 	AssignmentInfo info = (AssignmentInfo) vtProjectAllocationConflict.elementAt(i);
%>
<tr class="<%=className%>">
<td><%=info.projectCode%></td>
<td><%=info.devName%></td>
<td style="text-align: left"><%=info.workingTime%></td>
<td><%=CommonTools.dateFormat(info.beginDate)%></td>
<td><%=CommonTools.dateFormat(info.endDate)%></td>
</tr>
<%}
// Load conflict information
AssignmentInfo info = (AssignmentInfo)
    vtProjectAllocationConflict.elementAt(vtProjectAllocationConflict.size() - 1);
%>
<tr class="Error">
<td><%=languageChoose.getMessage("fi.jsp.woTeamAdd.OverAllocated")%></td>
<td><%=info.devName%></td>
<td><%=info.workingTime%></td>
<td><%=CommonTools.dateFormat(info.beginDate)%></td>
<td><%=CommonTools.dateFormat(info.endDate)%></td>
</tr>
</TBODY>
</TABLE>
<br>
<%}%>
<TABLE cellspacing="1" class="Table">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Updateassignment")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.FullName")%> </TD>
            <TD class = "CellBGR3">
            <input type="hidden" name="woTeam_devID"  value=<%=assignmentInfo.devID%>  >
            <%=assignmentInfo.devName%>
		    </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Type")%></TD>
            <TD class="CellBGR3">
            <SELECT name="woTeam_type" class = "COMBO" WIDTH="100" STYLE="width: 100px">        		
        		<OPTION value = "1" <%=(assignmentInfo.type == 1 )? "selected":""%>><%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Onsite")%></OPTION>
        		<OPTION value = "2" <%=(assignmentInfo.type == 2 )? "selected":""%>><%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Offshore")%></OPTION>
        		<OPTION value = "3" <%=(assignmentInfo.type == 3 )? "selected":""%>><%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Tentative")%></OPTION>
        		<OPTION value = "4" <%=(assignmentInfo.type == 4 )? "selected":""%>><%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Training")%></OPTION>
        		<OPTION value = "5" <%=(assignmentInfo.type == 5 )? "selected":""%>><%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Off")%></OPTION>            		
        	</SELECT>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.SubTeam")%> </TD>
            <TD class="CellBGR3">
            <SELECT name="woTeam_TeamName" class = "COMBO" WIDTH="100" STYLE="width: 100px">
        		<Option value ="0">&nbsp;</OPTION>
        <%
   			for (int i = 0; i < nTeamSize; i++) {
				SubTeamInfo subTeamInfo = (SubTeamInfo)subTeamList.elementAt(i);
   		%>
   				<OPTION value="<%=subTeamInfo.teamID%>" <%=(subTeamInfo.teamID ==assignmentInfo.teamID) ? "selected":""%>><%=subTeamInfo.teamName%></OPTION>
   		<%
   			}
   		%>
        	</SELECT>  
        </TR>
        
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.EffordPercent")%>* </TD>
            <TD class="CellBGR3"><input type="text" name="woTeam_workingTime" value = "<%=assignmentInfo.workingTime%>" maxlength="11" size = "11" style="text-align: right">(%)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Startdate")%>* </TD>
            <TD class="CellBGR3"><input type="text" name="woTeam_startDate" value="<%=((assignmentInfo.beginDate == null)? "" : CommonTools.dateFormat(new java.util.Date(assignmentInfo.beginDate .getTime())))%>" maxlength="9" size = "9">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Enddate")%>* </TD>
            <TD class="CellBGR3"><input type="text" name="woTeam_endDate" value="<%=((assignmentInfo.endDate == null)? "" : CommonTools.dateFormat(new java.util.Date(assignmentInfo.endDate.getTime())))%>" maxlength="9" size = "9">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Role")%> </TD>
            <TD class="CellBGR3">
			<%=new ResponsibilityCbo(assignmentInfo.responsibilityID).html%>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Qualification")%> </TD>
            <TD class="CellBGR3"><TEXTAREA name="woTeam_qualification" rows="4" cols="50"><%=((assignmentInfo.qualification == null)? "": assignmentInfo.qualification) %></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Responsibility")%> </TD>
            <TD class="CellBGR3"><TEXTAREA name="woTeam_note" rows="4" cols="50"><%=((assignmentInfo.note == null)? "": assignmentInfo.note) %></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>

</form>
<br>
<form name="frm_teamDelete" action="Fms1Servlet" method = "get">
				<input type = "hidden" name="reqType" value="<%=Constants.WO_TEAM_DELETE %>" >
				<input type = "hidden" name="woTeam_assID" value="<%=assignmentInfo.assID%>">
				<input type = "hidden" name="Developer_id" value="<%=assignmentInfo.devID%>">
</form>
<INPUT type="button" name="update2" value=" <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Update")%> " onclick="javascript:on_Submit1();" class="BUTTON">
<input type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Delete")%> "  onclick="javascript:on_Submit2();"  class="BUTTON">
<input type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.woTeamUpdate.Cancel")%> "    class="BUTTON" onclick="jumpURL('woTeam.jsp');">
<script language = "javascript">
	function on_Submit1()
	{	
		if(isNaN(frm_teamUpdate.woTeam_workingTime.value) && (frm_teamUpdate.woTeam_workingTime.value != "")){
			alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.TheValueMustBeANumber")%>");  			
  			frm_teamUpdate.woTeam_workingTime.focus();  		
  			return;  
  		}	
 
  	 	if((frm_teamUpdate.woTeam_workingTime.value<1) || (frm_teamUpdate.woTeam_workingTime.value>100)){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.TheWorkingTimeMustBeBetween1And100")%>");  			
  			frm_teamUpdate.woTeam_workingTime.focus();  		
  			return;  
  		}	
		if((!(isDate(frm_teamUpdate.woTeam_startDate.value))) && (frm_teamUpdate.woTeam_startDate.value != "")){  	  
			window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.TheValueInsertedIsNotADate")%>");  			
  		 	frm_teamUpdate.woTeam_startDate.focus();
  		 	return;
  	 	}
  	 	if(frm_teamUpdate.woTeam_startDate.value == ""){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.YouMustEnterStartDate")%>");  			
  		 	frm_teamUpdate.woTeam_startDate.focus();
  		 	return;
  	 	}
  	 	
  	 	if((!(isDate(frm_teamUpdate.woTeam_endDate.value))) && (frm_teamUpdate.woTeam_endDate.value != "")){  	  
			window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.TheValueInsertedIsNotADate")%>");  			
  		 	frm_teamUpdate.woTeam_endDate.focus();
  		 	return;
  	 	}
  	 	if(frm_teamUpdate.woTeam_endDate.value == ""){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.YouMustEnterEndDate")%>");  			
  		 	frm_teamUpdate.woTeam_endDate.focus();
  		 	return;
  	 	}
  	 	
  	 	if(compareDate(frm_teamUpdate.woTeam_startDate.value, frm_teamUpdate.woTeam_endDate.value) == -1){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.TheEndDateMustBeLaterThanTheBeginDate")%>");  			
  		 	frm_teamUpdate.woTeam_endDate.focus();
  		 	return;
  	 	}
  	 	
  	 	<%
  	 	if(!startDate.equals("")){
  	 	%>
  	 		if(compareDate("<%=startDate%>",frm_teamUpdate.woTeam_startDate.value) == -1){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.ThisDateIsOutOfRangeOfTheProjectPlanning")%>");  			
  			frm_teamUpdate.woTeam_startDate.focus();
  			return;
  	 		}
  	 		if(compareDate("<%=startDate%>",frm_teamUpdate.woTeam_endDate.value) == -1){
			window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.ThisDateIsOutOfRangeOfTheProjectPlanning")%>");  			
  		 	frm_teamUpdate.woTeam_endDate.focus();
  		 	return;
  	 		}
  	 	<%
  	 	}
  	 	%>
  	 	<%
  	 	if(!endDate.equals("")){
  	 	%>
  	 		if(compareDate(frm_teamUpdate.woTeam_startDate.value, "<%=endDate%>") == -1){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.ThisDateIsOutOfRangeOfTheProjectPlanning")%>");  			
  		 	frm_teamUpdate.woTeam_startDate.focus();
  		 	return;
  	 		}
  	 		if(compareDate(frm_teamUpdate.woTeam_endDate.value, "<%=endDate%>") == -1){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.ThisDateIsOutOfRangeOfTheProjectPlanning")%>");  			
  		 	frm_teamUpdate.woTeam_endDate.focus();
  		 	return;
  	 		}
  	 	<%
  	 	}
  	 	%>
  	 	 if (frm_teamUpdate.woTeam_note.value.length >200){
	  	 	alert("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.Thelengthofthisfieldmustbebelow200characters")%>");
	  	 	frm_teamUpdate.woTeam_note.focus();
	  	 	return;
  	 	}
		document.frm_teamUpdate.submit();
	}
	function on_Submit2()
	{
		if (window.confirm("<%= languageChoose.getMessage("fi.jsp.woTeamUpdate.AreYouSure")%>") != 0) {
			document.frm_teamDelete.submit();
		}
		else{
			return;
		}	
		
	}		
</script>
</BODY>
</HTML>