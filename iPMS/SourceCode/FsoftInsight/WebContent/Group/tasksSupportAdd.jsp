<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.Vector,java.util.Calendar,com.fms1.tools.* ,com.fms1.html.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>

<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="../javaFns.jsp"%>
</SCRIPT>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
	String strCurrentGroup = (workUnitID == Parameters.PQA_WU)? "PQA":"SQA";
	int []types=null;
	if (workUnitID==Parameters.SQA_WU) 
		types = TaskInfo.typesSQA;
	else if(workUnitID==Parameters.PQA_WU)
		types = TaskInfo.typesPQA;
	Vector prjList=(Vector)session.getAttribute("projectstasks");
	Vector grpList=(Vector)session.getAttribute("groupstasks");
	TaskInfo taskInfo = new TaskInfo();
	String strEffort = "";
	if (request.getAttribute("usersTasksAdd") != null){
		taskInfo = (TaskInfo)request.getAttribute("usersTasksAdd");
		if (!Double.isNaN(taskInfo.effort)){
			strEffort = Double.toString(taskInfo.effort);
		}
	}
	String strMenuType ="loadSQAMenu";
%>
<TITLE>taskSQAAdd.jsp</TITLE>
</HEAD>
<BODY  class="BD" onload="<%=strMenuType%>('Monitoring');changeStatus();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Tasks")%> </P>
<br>
<P> <%=languageChoose.paramText(new String[]{"fi.jsp.taskSupportAdd.Note__Only__running__projects__during__the__selected__timeframe__will__be__displayed__in__the__~PARAM1_STRING~__project__~PARAM2_STRING~__combo","<i>","</i>"})%></P>

<FORM action="Fms1Servlet?reqType=<%=Constants.TASK_ADD%>" name="frm" method="post">
<INPUT type="hidden" name="<%=WUCombo.meUpdate%>">
<TABLE class="Table" cellspacing="1" width="560">
<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Addnewtask")%> </CAPTION>
	<TBODY>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.ProjectGroup")%>*  </TD>
			<TD class="CellBGR3">
				<SELECT name="wuID">
					<%GroupInfo grpInf;
					for(int i=0;i<grpList.size();i++){
						grpInf=(GroupInfo)grpList.elementAt(i);
						%> <OPTION value="<%=grpInf.wuID%>" <%=((grpInf.wuID == taskInfo.wuID)?"selected":"")%>> <%=grpInf.name%></OPTION>
					<%}
					ProjectInfo prjInf;
					for(int i = 0; i < prjList.size(); i++) {
						prjInf=(ProjectInfo)prjList.elementAt(i);
						%>
						<OPTION value="<%=prjInf.getWorkUnitId()%>" <%=((prjInf.getWorkUnitId() == taskInfo.wuID)?"selected":"")%>><%=prjInf.getProjectCode()%></OPTION>
					<%}%>
				</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Task")%>* </TD>
			<TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="desc"> <%=taskInfo.desc%></TEXTAREA></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Status")%>* </TD>
			<TD class="CellBGR3">
				<SELECT name="status" onChange='changeStatus()'>
					<%for(int i = 1; i < TaskInfo.statusSQA.length; i++){
						%>
						<OPTION value="<%=i%>" <%=(i==taskInfo.status?"selected":"")%>><%=languageChoose.getMessage(TaskInfo.statusSQA[i])%></OPTION>
					<%}%>
				</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Type")%>* </TD>
			<TD class="CellBGR3">
			<SELECT name="type">
				<%for (int i=0;i<types.length;i++){
					%> <OPTION value="<%=types[i]%>" <%=((types[i] == taskInfo.typeID)?"selected":"")%>><%=languageChoose.getMessage(TaskInfo.types[types[i]])%></OPTION>
				<%}%>
			</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Assignedto")%>* </TD>
            <TD class="CellBGR3">
            	<INPUT name="strAccountName" size="30" type="text" value="<%=taskInfo.assignedToStr%>"/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Search")%>" onclick="onCheckAccount(event)"> <BR>
	            <input type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Account")%>       <INPUT
				type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Name")%><BR>
            </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Actualeffortpd")%> </TD>
			<TD class="CellBGR3"><INPUT name="effort" size="15" type="text" maxlength="11" class="numberTextBox" value="<%=strEffort%>"></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Planneddate")%>* </TD>
			<TD class="CellBGR3"><INPUT name="planDate" size="9" maxlength="9" value="<%=("N/A".equals(CommonTools.dateFormat(taskInfo.planDate))? "": CommonTools.dateFormat(taskInfo.planDate))%>"> (DD-MMM-YY) </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Actualdate")%> <A name="starDate"></A></TD>
			<TD class="CellBGR3"><INPUT name="actualDate" size="9" maxlength="9" value="<%=("N/A".equals(CommonTools.dateFormat(taskInfo.actualDate))? "": CommonTools.dateFormat(taskInfo.actualDate))%>"> (DD-MMM-YY) </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Note")%> </TD>
			<TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="note"><%=taskInfo.note%></TEXTAREA></TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<BR>
<P>
<INPUT type="button" onclick="onOK();" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.OK")%> " class="BUTTON">
<%
if(workUnitID==Parameters.PQA_WU){%>
<INPUT type="button" onclick="onOK(1);" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.OKAddissue")%> " class="BUTTONWIDTH">
<%}%>
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.TASK_LIST%>);"></P>
<P>

</BODY>
<SCRIPT language="JavaScript">
<%
	if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null){
%>
		alert("<%=languageChoose.getMessage("fi.jsp.tasksSupportAdd.AccountError")%>");
		frm.strAccountName.focus();
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

function onOK(issue){
	if (mandatoryFld(frm.desc,"<%= languageChoose.getMessage("fi.jsp.taskSupportAdd.Task")%>"))
	if (mandatoryFld(frm.strAccountName,"<%= languageChoose.getMessage("fi.jsp.tasksSupportAdd.AssignedToNotEmpty")%>"))
	if (maxLength(frm.desc,"<%= languageChoose.getMessage("fi.jsp.taskSupportAdd.Task")%>",1000))
	if (positiveFld(frm.effort,"<%= languageChoose.getMessage("fi.jsp.taskSupportAdd.Effort") %>"))
	if (mandatoryDateFld(frm.planDate,"<%= languageChoose.getMessage("fi.jsp.taskSupportAdd.Planneddate")%>"))
	if (beforeTodayFld(frm.actualDate,"<%= languageChoose.getMessage("fi.jsp.taskSupportAdd.Actualdate")%>"))
	if (dateFld(frm.actualDate,"<%= languageChoose.getMessage("fi.jsp.taskSupportAdd.Actualdate")%>"))
	if (maxLength(frm.note,"<%= languageChoose.getMessage("fi.jsp.taskSupportAdd.Note")%>",200)){	
		var j = frm.status.value;
		if ((frm.actualDate.value.length==0)
		&&(j==<%=TaskInfo.STATUS_PASS%>||j==<%=TaskInfo.STATUS_NOT_PASSED%>)){
		 	window.alert("<%= languageChoose.getMessage("fi.jsp.taskSupportAdd.Actualdateismandatorywhenstatusispassedornotpassed")%>");
  			frm.actualDate.focus();  		
  			return;
		}
		if ((frm.actualDate.value.length>0)&&(j==<%=TaskInfo.STATUS_PENDING%>)){
		 	window.alert("<%= languageChoose.getMessage("fi.jsp.taskSupportAdd.theStatusShouldBe")%>");
  			frm.status.focus();  		
  			return;
		}
		if (issue==1)
			frm.<%=WUCombo.meUpdate%>.value=frm.wuID.options[frm.wuID.selectedIndex].value;
		frm.submit();
	}
}
function changeStatus(){
var j = frm.status.value;
	if ((j==<%=TaskInfo.STATUS_PASS%>||j==<%=TaskInfo.STATUS_NOT_PASSED%>))
		document.all['starDate'].innerText="*";
	else
		document.all['starDate'].innerText="";
}
changeStatus();
</SCRIPT>
</HTML>
