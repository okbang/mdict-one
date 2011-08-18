<%@page import="com.fms1.tools.*"%> 
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
<%	
	Vector userList=(Vector)session.getAttribute("userstasks");
	int reqType =Integer.parseInt(request.getParameter("type"));


%>
<TITLE>decision</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY  class="BD" onload="loadOrgMenu();">
<P class="TITLE"><%=((reqType==Constants.DECISION)?languageChoose.getMessage("fi.jsp.decisionAdd.ManagementDecisions"):languageChoose.getMessage("fi.jsp.decisionAdd.Plans"))%></P>
<br>
<FORM action="Fms1Servlet?reqType=<%=((reqType==Constants.DECISION)?Constants.DECISION_ADD:Constants.PLAN_ADD)%>" name="frm" method="post">
<TABLE class="Table" cellspacing="1" width="560">
<CAPTION class="TableCaption"> <%=((reqType==Constants.DECISION)?languageChoose.getMessage("fi.jsp.decisionAdd.AddnewDecision"):languageChoose.getMessage("fi.jsp.decisionAdd.AddnewPlan"))%></CAPTION>
	<TBODY>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.decisionAdd.Code")%>* </TD>
			<TD class="CellBGR3"><INPUT name="code" size="9" type="text" maxlength="20"></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"><%=((reqType==Constants.DECISION)?languageChoose.getMessage("fi.jsp.decisionAdd.Decision"):languageChoose.getMessage("fi.jsp.decisionAdd.Plan"))%>*</TD>
			<TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="desc"></TEXTAREA></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.decisionAdd.Planneddate")%>* </TD>
			<TD class="CellBGR3"><INPUT name="planDate" size="9" maxlength="9"> (DD-MMM-YY) </TD>
		</TR>
		<%if (reqType==Constants.PLAN){%>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.decisionAdd.Replanneddate")%> </TD>
			<TD class="CellBGR3"><INPUT name="rePlanDate" size="9" maxlength="9"> (DD-MMM-YY) </TD>
		</TR>
		
		<%}%>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.decisionAdd.Actualdate")%> </TD>
			<TD class="CellBGR3"><INPUT name="actualDate" size="9" maxlength="9"> (DD-MMM-YY) </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.decisionAdd.Assignedto")%>* </TD>
			<TD class="CellBGR3">
				<SELECT name="assignedTo">
					<%UserInfo info;
					for(int i=0;i<userList.size();i++){
						info=(UserInfo)userList.elementAt(i);
						%><OPTION value="<%=info.developerID%>"><%=info.account%></OPTION>
					<%} %>
				</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.decisionAdd.Process")%> </TD>
				<TD class="CellBGR3">
					<SELECT name="process">
						<%ProcessInfo pinfo;
						for(int i=0;i<ProcessInfo.processList.size();i++){
							pinfo=(ProcessInfo)ProcessInfo.processList.elementAt(i);
							%><OPTION value="<%=pinfo.processId%>"><%=languageChoose.getMessage(pinfo.name)%></OPTION>
						<%}%>
					</SELECT>
				</TD>
		</TR>
		<% if (reqType==Constants.DECISION){%>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.decisionAdd.Feasible")%> </TD>
			<TD class="CellBGR3"><input type="checkbox" value=1 name="feasible"></TD>
		</TR>
		<%}%>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.decisionAdd.Note")%> </TD>
			<TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="note"></TEXTAREA></TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<P>
<INPUT type="button" onclick="onOK();" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.decisionAdd.OK")%> " class="BUTTON">
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.decisionAdd.Cancel")%> " class="BUTTON" onclick="doIt(<%=reqType%>);"></P>
<P>

</BODY>
<SCRIPT language="JavaScript">
function onOK()	{	
	if (mandatoryFld(frm.code,"<%=languageChoose.getMessage("fi.jsp.decisionAdd.Code1")%>"))
	<% if (reqType==Constants.DECISION){%>
	if (mandatoryFld(frm.desc,"<%=languageChoose.getMessage("fi.jsp.decisionAdd.Decision")%>"))
	<%}else{%>
	if (mandatoryFld(frm.desc,"<%=languageChoose.getMessage("fi.jsp.decisionAdd.Plan")%>"))
	<%}%>
	if (maxLength(frm.desc,"<%=languageChoose.getMessage("fi.jsp.decisionAdd.Decision")%>",1000))
	if (mandatoryDateFld(frm.planDate,"<%=languageChoose.getMessage("fi.jsp.decisionAdd.Planneddate1")%>"))
	<%if (reqType==Constants.PLAN){%>
	if (dateFld(frm.rePlanDate,"<%=languageChoose.getMessage("fi.jsp.decisionAdd.Replanneddate")%>"))	
	<%}%>
	if (dateFld(frm.actualDate,"<%=languageChoose.getMessage("fi.jsp.decisionAdd.Actualdate")%>"))	
	if (beforeTodayFld(frm.actualDate,"<%=languageChoose.getMessage("fi.jsp.decisionAdd.Actualdate")%>"))
	if (maxLength(frm.note,"<%=languageChoose.getMessage("fi.jsp.decisionAdd.Note")%>",200))
		frm.submit();

}
</SCRIPT>
</HTML>
