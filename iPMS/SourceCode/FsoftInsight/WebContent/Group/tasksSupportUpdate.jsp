<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.Vector,java.util.Calendar,com.fms1.tools.* " %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>

<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="JavaScript">
        <%@ include file="../javaFns.jsp"%>
</SCRIPT>
<%
	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
	String strCurrentGroup = (workUnitID == Parameters.PQA_WU)? "PQA":"SQA";
	int []types = null;
	if (workUnitID == Parameters.SQA_WU) 
		types = TaskInfo.typesSQA;
	else if(workUnitID == Parameters.PQA_WU)
		types = TaskInfo.typesPQA;
	int vtID = Integer.parseInt(request.getParameter("vtID"));
	Vector tasks = (Vector)session.getAttribute("tasks");
	TaskInfo theTask = (TaskInfo)tasks.elementAt(vtID);
	if (request.getAttribute("usersTasksUpdate") != null){
		theTask = (TaskInfo)request.getAttribute("usersTasksUpdate");
	}
	Vector prjList = (Vector)session.getAttribute("projectstasks");
	Vector grpList = (Vector)session.getAttribute("groupstasks");
	String strMenuType = "loadSQAMenu";
%>
<TITLE>taskSupportUpdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY  class="BD" onload="<%=strMenuType%>('Monitoring');changeStat();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Tasks")%></P>
<br>
<P> <%=languageChoose.paramText(new String[]{"fi.jsp.tasksSupportUpdate.Note__Only__running__projects", "<i>", "</i>"})%></P>

<FORM action="Fms1Servlet?reqType=<%=Constants.TASK_UPDATE%>" name="frm" method="post">
<INPUT type="hidden" name="vtID" value="<%=vtID%>">
<TABLE class="Table" cellspacing="1" width="560">
<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Updatetask")%> </CAPTION>
	<TBODY>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.ProjectGroup")%>*  </TD>
			<TD class="CellBGR3">
				<SELECT name="wuID">
					<%GroupInfo grpInf;
					for(int i=0;i<grpList.size();i++){
						grpInf=(GroupInfo)grpList.elementAt(i);
						%> <OPTION value='<%=grpInf.wuID %>'<%=((grpInf.wuID==theTask.wuID)?" selected":"")%>><%=grpInf.name%></OPTION>
					<%}
					ProjectInfo prjInf;
					for(int i = 0; i < prjList.size(); i++) {
						prjInf=(ProjectInfo)prjList.elementAt(i);
						%> <OPTION value='<%=prjInf.getWorkUnitId()%>'<%=((prjInf.getWorkUnitId()==theTask.wuID)?" selected":"")%>><%=prjInf.getProjectCode()%></OPTION>
					<%}%>
				</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Task")%>* </TD>
			<TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="desc"><%=theTask.desc%></TEXTAREA></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Status")%>* </TD>
			<TD class="CellBGR3">
				<SELECT name="status" onChange='changeStat()'>
					<%for(int i=1;i<TaskInfo.statusSQA.length;i++){
						%> <OPTION value='<%=i%>'<%=((i==theTask.status)?" selected":"")%>><%=languageChoose.getMessage(TaskInfo.statusSQA[i])%></OPTION>
					<%}%>
				</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Type")%>* </TD>
			<TD class="CellBGR3">
			<SELECT name="type">
				<%for (int i=0;i<types.length;i++){
					%> <OPTION value='<%=types[i]%>'<%=((types[i]==theTask.typeID)?" selected":"")%>><%=languageChoose.getMessage(TaskInfo.types[types[i]])%></OPTION>
				<%}%>
			</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Assignedto")%>* </TD>
            <TD class="CellBGR3">
            	<INPUT name="strAccountName" size="30" type="text" value="<%=theTask.assignedToStr%>"/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Search")%>" onclick="onCheckAccount(event)"> <BR>
	            <input type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Account")%>       <INPUT
				type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Name")%><BR>
            </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Actualeffortpd")%> </TD>
			<TD class="CellBGR3"><INPUT name="effort" size="15" type="text" maxlength="11" class="numberTextBox" value="<%=CommonTools.updateDouble(theTask.effort)%>"></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Planneddate")%>* </TD>
			<TD class="CellBGR3"><INPUT name="planDate" size="9" maxlength="9" value="<%=CommonTools.dateUpdate(theTask.planDate)%>"> (DD-MMM-YY) </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Actualdate")%> <A name="starDate"></A></TD>
			<TD class="CellBGR3"><INPUT name="actualDate" size="9" maxlength="9" value="<%=CommonTools.dateUpdate(theTask.actualDate)%>"> (DD-MMM-YY) </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Note")%> </TD>
			<TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="note"><%=(theTask.note == null ? "":theTask.note)%></TEXTAREA></TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<P>
<INPUT type="button" onclick="onOK();" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Ok")%> " class="BUTTON">
<INPUT type="button" onclick="onDelete();" value=" <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Delete")%> " class="BUTTON">
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.TASK_LIST%>);"></P>
<P>

</BODY>
<SCRIPT language="JavaScript">
<%
	if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null){
%>
		alert("<%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.AccountError")%>");
<%
		request.removeAttribute(StringConstants.FILLTER_USER_ERROR);
	}
%>
	function onCheckAccount(event){
		if (trim(frm.strAccountName.value) == "") {
	  		window.alert("<%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.AssignedToNotEmpty")%>");
	  		frm.strAccountName.focus();
	  		return;	
	  	}
	    var rd = document.forms['frm'].rdAccountName;
	    var rdAccountName;
        for(var i = 0; i < rd.length; i++){
		  if(rd[i].checked){
		  	rdAccountName = rd[i].value;
		  	break;
		  }
        }
        var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_USER%>" + "&Account=" + frm.strAccountName.value + "&Type=" + rdAccountName + "&Group=<%=strCurrentGroup%>";
		javascript:ajax_showOptions(document.forms['frm'].strAccountName, url, '', event);
	}

function onOK()	{	
	if (mandatoryFld(frm.desc,"<%= languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Task")%>"))
	if (mandatoryFld(frm.strAccountName,"<%= languageChoose.getMessage("fi.jsp.tasksSupportAdd.AssignedToNotEmpty")%>"))
	if (maxLength(frm.desc,"<%= languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Task")%>",1000))
	if (positiveFld(frm.effort,"<%= languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Effort")%>"))
	if (mandatoryDateFld(frm.planDate,"<%= languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Planneddate")%>"))
	if (beforeTodayFld(frm.actualDate,"<%= languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Actualdate")%>"))
	if (dateFld(frm.actualDate,"<%= languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Actualdate")%>"))
	if (maxLength(frm.note,"<%= languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Note")%>",200)){
		var j=frm.status.value;
		if ((frm.actualDate.value.length==0)
		&&(j==<%=TaskInfo.STATUS_PASS%>||j==<%=TaskInfo.STATUS_NOT_PASSED%>)){
		 	window.alert("<%= languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Actualdateismandatorywhenstatusispassedornotpassed")%>");
  			frm.actualDate.focus();  		
  			return;
		}
		if ((frm.actualDate.value.length>0)&&(j==<%=TaskInfo.STATUS_PENDING%>)){
		 	window.alert("<%= languageChoose.getMessage("fi.jsp.tasksSupportUpdate.thestatusshouldbe")%>");
  			frm.status.focus();  		
  			return;
		}
		frm.submit();
	}
}
function onDelete()	{	
	if (window.confirm("<%= languageChoose.getMessage("fi.jsp.tasksSupportUpdate.Areyousuretodeletethistask")%>")){
  		frm.action="Fms1Servlet?reqType=<%=Constants.TASK_DELETE%>";
		frm.submit();
	}
}
function changeStat(){
var j=frm.status.value;
	if ((j==<%=TaskInfo.STATUS_PASS%>||j==<%=TaskInfo.STATUS_NOT_PASSED%>))
		document.all['starDate'].innerText="*";
	else
		document.all['starDate'].innerText="";
}
</SCRIPT>
</HTML>
