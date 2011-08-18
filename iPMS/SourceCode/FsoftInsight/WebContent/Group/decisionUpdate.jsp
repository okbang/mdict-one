<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.Vector,java.util.Calendar,com.fms1.tools.* " contentType="text/html;charset=UTF-8"%>
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
<%int vtID = Integer.parseInt(request.getParameter("vtID"));
	Vector tasks=(Vector)session.getAttribute("tasks");
	TaskInfo theTask=(TaskInfo)tasks.elementAt(vtID);
	Vector userList=(Vector)session.getAttribute("userstasks");
%>
<TITLE>decisionUpdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD"
	onload="loadOrgMenu();">
<P class="TITLE"><%=((theTask.type==TaskInfo.DECISION)?languageChoose.getMessage("fi.jsp.decisionUpdate.ManagementDecisions"):languageChoose.getMessage("fi.jsp.decisionUpdate.Plans"))%></P>
<br>
<FORM
	action="Fms1Servlet?reqType=<%=(theTask.type == TaskInfo.DECISION)
	? Constants.DECISION_UPDATE
	: Constants.PLAN_UPDATE%>"
	name="frm" method="post"><INPUT type="hidden" name="vtID"
	value="<%=vtID%>">
<TABLE class="Table" cellspacing="1" width="560">
<CAPTION class="TableCaption"><%=((theTask.type==TaskInfo.DECISION)?languageChoose.getMessage("fi.jsp.decisionUpdate.UpdateDecision"):languageChoose.getMessage("fi.jsp.decisionUpdate.UpdatePlan"))%></CAPTION>
	<TBODY>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.decisionUpdate.Code")%>*</TD>
			<TD class="CellBGR3"><INPUT name="code" size="9" maxlength="20"
				value="<%=theTask.code%>"></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=((theTask.type==TaskInfo.DECISION)?languageChoose.getMessage("fi.jsp.decisionUpdate.Decision"):languageChoose.getMessage("fi.jsp.decisionUpdate.Plan"))%>*</TD>
			<TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="desc"><%=theTask.desc%></TEXTAREA></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.decisionUpdate.Planneddate")%>*</TD>
			<TD class="CellBGR3"><INPUT name="planDate" size="9" maxlength="9"
				value="<%=CommonTools.dateUpdate(theTask.planDate)%>">(DD-MMM-YY)</TD>
		</TR>
		<%if (theTask.type==TaskInfo.PLAN){%>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.decisionUpdate.Replanneddate")%></TD>
			<TD class="CellBGR3"><INPUT name="rePlanDate" size="9" maxlength="9"
				value="<%=CommonTools.dateUpdate(theTask.rePlanDate)%>">(DD-MMM-YY)</TD>
		</TR>
		<%}%>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.decisionUpdate.Actualdate")%></TD>
			<TD class="CellBGR3"><INPUT name="actualDate" size="9" maxlength="9"
				value="<%=CommonTools.dateUpdate(theTask.actualDate)%>">(DD-MMM-YY)</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.decisionUpdate.Assignedto")%>*</TD>
			<TD class="CellBGR3"><SELECT name="assignedTo">
					<%UserInfo info;
					for(int i=0;i<userList.size();i++){
	info = (UserInfo) userList.elementAt(i);%>
				<OPTION
					value="<%=info.developerID %>"
	<%= ((info.developerID == theTask.assignedTo) ? " selected" : "") %> >
	 <%= info.account%></OPTION>
					<%}%>
				</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.decisionUpdate.Process")%></TD>
				<TD class="CellBGR3">
					<SELECT name="process">
						<%ProcessInfo pinfo;
						for(int i=0;i<ProcessInfo.processList.size();i++){
	pinfo = (ProcessInfo) ProcessInfo.processList.elementAt(i);%><OPTION value="<%=pinfo.processId %>"
	<%= ((pinfo.processId == theTask.processID) ? " selected" : "")%> >
	<%= languageChoose.getMessage(pinfo.name)%></OPTION>
						<%}%>
					</SELECT>
				</TD>
		</TR>
		<% if (theTask.type==TaskInfo.DECISION){%>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.decisionUpdate.Feasible")%></TD>
			<TD class="CellBGR3"><input type="checkbox" name="feasible" value=1 <%=((theTask.feasible)?" checked":"")%>></TD>
		</TR>
		<%}%>
		<TR>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.decisionUpdate.Note")%></TD>
			<TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="note"><%=CommonTools.updateString(theTask.note)%></TEXTAREA></TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<P>
<INPUT type="button" onclick="onOK();" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.decisionUpdate.Ok")%> " class="BUTTON">
<INPUT type="button" onclick="onDelete();" value=" <%=languageChoose.getMessage("fi.jsp.decisionUpdate.Delete")%> " class="BUTTON">
<INPUT type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.decisionUpdate.Cancel")%> " class="BUTTON" onclick="doIt(<%=(theTask.type==TaskInfo.DECISION)?Constants.DECISION:Constants.PLAN%>);"></P>
<P>

</BODY>
<SCRIPT language="JavaScript">
function onOK()	{	
	if (mandatoryFld(frm.code,"<%=languageChoose.getMessage("fi.jsp.decisionUpdate.Code1")%>"))
	<% if (theTask.type==TaskInfo.DECISION){%>
	if (mandatoryFld(frm.desc,"<%=languageChoose.getMessage("fi.jsp.decisionUpdate.Decision")%>"))
	<%}else{%>
	if (mandatoryFld(frm.desc,"<%=languageChoose.getMessage("fi.jsp.decisionUpdate.Plan")%>"))
	<%}%>
	if (maxLength(frm.desc,"<%=languageChoose.getMessage("fi.jsp.decisionUpdate.Decision")%>",1000))
	if (mandatoryDateFld(frm.planDate,"<%=languageChoose.getMessage("fi.jsp.decisionUpdate.Planneddate")%>"))
	<%if (theTask.type==TaskInfo.PLAN){%>
	if (dateFld(frm.rePlanDate,"<%=languageChoose.getMessage("fi.jsp.decisionUpdate.Replanneddate")%>"))	
	<%}%>
	if (dateFld(frm.actualDate,"<%=languageChoose.getMessage("fi.jsp.decisionUpdate.Actualdate")%>"))
	if (beforeTodayFld(frm.actualDate,"<%=languageChoose.getMessage("fi.jsp.decisionUpdate.Actualdate")%>"))
	if (maxLength(frm.note,"<%=languageChoose.getMessage("fi.jsp.decisionUpdate.Note")%>",200))
		frm.submit();

}
function onDelete()	{	
	if (window.confirm("<%=languageChoose.getMessage("fi.jsp.decisionUpdate.Areyousuretodeletethistask")%>")){
  		frm.action="Fms1Servlet?reqType=<%=(theTask.type == TaskInfo.DECISION)
		? Constants.DECISION_DELETE: Constants.PLAN_DELETE%>&vtID=<%=vtID%>";
		frm.submit();
}
}
</SCRIPT>
</HTML>
