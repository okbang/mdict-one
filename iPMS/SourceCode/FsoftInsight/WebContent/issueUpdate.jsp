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
<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>issueUpdate.jsp</TITLE>
</HEAD>


<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	Vector issueList = (Vector)session.getAttribute("issueList");
	int vtID=Integer.parseInt(request.getParameter("vtID"));
	IssueInfo issue= (IssueInfo)issueList.elementAt(vtID);
	if (request.getAttribute("IssueInfo") != null){
		issue = (IssueInfo)request.getAttribute("IssueInfo");
	}
	Vector vtProcess = (Vector)session.getAttribute("vtProcess");
	String strMenuType=CommonTools.getMnuFunc(session);
	//PQA specific------------
	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
	WUCombo wuCombo=null;
	if(workUnitID==Parameters.PQA_WU)
		wuCombo=new WUCombo(issue.wuID,WUCombo.MODE_UPDATE);

	//------------------------
%>
<BODY onLoad="<%=strMenuType%>frmOnload()" class="BD">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.issueUpdate.Issues")%></P>
<FORM action="Fms1Servlet?reqType=<%=Constants.ISSUE_UPDATE%>&vtID=<%=vtID%>" method="post" name="frm">
<TABLE class="Table" width="60%" cellspacing="1" >
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.issueUpdate.Updateissue")%></CAPTION>
    <TBODY >
        <TR class="CellBGR3">
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.Description")%>*</TD>
            <TD ><TEXTAREA rows="4" cols="50" name="txtDescription"><%=(issue.description)%></TEXTAREA></TD>
            <input type=hidden name="abcdef" value="Hoàng Minh Tiến">
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.Status")%></TD>
            <TD class="CellBGR3">
	            <SELECT name="cboStatus" onchange="occurredStatus()" class="COMBO" style='width:100'>
	                <%for (int i = 0; i < IssueInfo.statusFlds.length; i++) {%>
	                	<OPTION value="<%=i%>" <%=((i == issue.statusID)?"selected":"")%>>
	                	<%=languageChoose.getMessage(IssueInfo.statusFlds[i])%>
	                	</OPTION>
	                <%}%>
	            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.Priority")%></TD>
            <TD class="CellBGR3">
	            <SELECT name="cboPriority" class="COMBO" style='width:100'>
	                <%for (int i = 0; i < IssueInfo.priorityFlds.length; i++) { %>
		                <OPTION value="<%=i%>" <%=((i == issue.priorityID)?"selected":"")%> >
		                <%=languageChoose.getMessage(IssueInfo.priorityFlds[i])%>
		                </OPTION>
	                <%}%>
	            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.Type")%></TD>
            <TD class="CellBGR3"> 
            	<SELECT name="cboType" class="COMBO" style='width:100'>
	                <%for (int i = 0; i < IssueInfo.typeFlds.length; i++) { %>
		                <OPTION value="<%=i%>" <%=((i == issue.typeID)?"selected":"")%> >
		                <%=languageChoose.getMessage(IssueInfo.typeFlds[i])%>
		                </OPTION>
	                <%}%>
	            </SELECT>
	           </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.Processrelated")%></TD>
            <TD class="CellBGR3"><SELECT name="cboProcess" class="COMBO" style='width:178'>
           	<%
           		for(int i=0;i<vtProcess.size();i++){
           			ProcessInfo psInfo=(ProcessInfo)vtProcess.get(i);
           	%>
				<OPTION value="<%=psInfo.processId%>" <%=(psInfo.processId == issue.processId)?"selected":""%>><%=languageChoose.getMessage(psInfo.name)%></OPTION>
           	<%}%>
            </SELECT></TD>
        </TR>
        <%if (wuCombo!=null){%>
        <TR>
	        <TD height="23" class="ColumnLabel"><%=languageChoose.getMessage(wuCombo.label)%></TD>
	        <TD height="23" class="CellBGR3"><%=wuCombo.html%></TD>
	    </TR>
        <%}%>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.Owner")%></TD>
            <TD class="CellBGR3">
            	<INPUT name="strAccountName" size="30" type="text" value="<%=issue.owner%>"/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.issueUpdate.Search")%>" onclick="onCheckAccount(event)"> <BR>
	            <input type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.issueUpdate.Account")%>       <INPUT
				type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.issueUpdate.Name")%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.Creator")%></TD>
            <TD class="CellBGR3"><INPUT type="hidden" name="txtCreator" value="<%=issue.creator%>"><%=issue.creator%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.CreatedDate")%>*</TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" name="txtStartDate" value="<%=CommonTools.dateFormat(issue.startDate)%>" maxlength="9">
	            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showStartDate()'>            	
            	(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.DueDate")%>*</TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" name="txtDueDate" value="<%=CommonTools.dateFormat(issue.dueDate)%>" maxlength="9">
	            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showDueDate()'>            	
            	(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.CommentSolution")%></TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="txtComment"><%=((issue.comment==null)?"":(issue.comment))%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.ClosedDate")%>*</TD>
            <TD class="CellBGR3">
            	<INPUT size="9" type="text" name="txtClosedDate" value="<%=((issue.closeDate!= null)?CommonTools.dateFormat(issue.closeDate):"")%>" maxlength="9">
	            <IMG id="closeDateCalendar" name="closeDateCalendar" src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showCloseDate()'>            	
            	(DD-MMM-YY)
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueUpdate.Reference")%></TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="txtReference"><%=((issue.reference==null)?"":(issue.reference))%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" name="ok" value="<%=languageChoose.getMessage("fi.jsp.issueUpdate.OK")%>" onclick="doAction(this)" class="BUTTON"> 
<INPUT type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.issueUpdate.Cancel")%>" onclick="doIt(<%=Constants.ISSUE%>)" class="BUTTON">
</FORM>

<SCRIPT language="javascript">
//do execute filltering User ---Start
<%
	if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null){
%>
		alert("<%=languageChoose.getMessage("fi.jsp.issueUpdate.AccountError")%>");
<%
		request.removeAttribute(StringConstants.FILLTER_USER_ERROR);
	}
%>

	function onCheckAccount(event){
		if (trim(frm.strAccountName.value) == "") {
	  		window.alert("<%=languageChoose.getMessage("fi.jsp.issueUpdate.Ownercannotnotbeempty")%>");
	  		frm.strAccountName.focus();
	  		return;
	  	}
	    var rd =document.forms['frm'].rdAccountName;
	    var rdAccountName;
        for(var i=0;i<rd.length;i++){
		  if(rd[i].checked){
		  	rdAccountName = rd[i].value;
		  	break;
		  }         
        }
        var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_USER%>" + "&Account=" + frm.strAccountName.value + "&Type=" + rdAccountName;
		javascript:ajax_showOptions(document.forms['frm'].strAccountName, url, '', event);
	}
//do execute filltering User ---End
function doAction(button)
{  	  		
  		if (mandatoryFld(frm.txtDescription,"<%=languageChoose.getMessage("fi.jsp.issueUpdate.Description")%>"))
  		if (maxLength(frm.txtDescription,"<%=languageChoose.getMessage("fi.jsp.issueUpdate.Description")%>",500))
  		if (mandatoryDateFld(frm.txtStartDate,"<%=languageChoose.getMessage("fi.jsp.issueUpdate.CreatedDate")%>"))
  		if (mandatoryDateFld(frm.txtDueDate,"<%=languageChoose.getMessage("fi.jsp.issueUpdate.DueDate")%>"))
		if (maxLength(frm.txtComment,"<%=languageChoose.getMessage("fi.jsp.issueUpdate.CommentSolution")%>",500))
		if (dateFld(frm.txtClosedDate,"<%=languageChoose.getMessage("fi.jsp.issueUpdate.ClosedDate")%>")){
			if (compareDate(frm.txtStartDate.value, frm.txtDueDate.value) == -1){
				window.alert("<%=languageChoose.getMessage("fi.jsp.issueUpdate.Duedatemustbeaftercreateddate")%>");
				frm.txtDueDate.focus();
				return;
			}
			else if ((trim(frm.txtClosedDate.value) != "") && (compareDate(frm.txtStartDate.value, frm.txtClosedDate.value) == -1)) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.issueUpdate.Closeddatemustbeaftercreateddate")%>");
				frm.txtClosedDate.focus();
				return;

	 		}else if (trim(frm.strAccountName.value) == "") {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.issueUpdate.Ownercannotnotbeempty")%>");
	  			frm.strAccountName.focus();
				return;
	  		}
	 		else if ((trim(frm.txtClosedDate.value) != "") && (compareToToday(frm.txtClosedDate.value) == 1)) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.issueUpdate.Closeddatemustbetodayorbefore")%>");
				frm.txtClosedDate.focus();
				return;

	 		}else if (frm.txtReference.value.length > 200) {
	  			window.alert("Reference length cannot be longer than 200 characters!");
	  			frm.txtReference.focus();
				return;

	  		}else if ((frm.cboStatus.value == 1 || frm.cboStatus.value == 2 )&& trim(frm.txtClosedDate.value) == "") {
	  			window.alert("<%= languageChoose.getMessage("fi.jsp.issueUpdate.Closeddateismandatory")%>");
	  			frm.txtClosedDate.focus();
				return;
	  		}
	  		else{
  				frm.submit();
  			}
		}

}
function frmOnload() {
	frm.txtDescription.focus();
	<%if ((issue.statusID ==1)||(issue.statusID==2)){%>
		showObj('txtClosedDate');
		showObj('closeDateCalendar');
	<%}else {%>
		hideObj('txtClosedDate');
		hideObj('closeDateCalendar');
		frm.txtClosedDate.value="";
	<%}%>

}

function occurredStatus() {
	if ((frm.cboStatus.value == "1")||(frm.cboStatus.value == "2")) {
		showObj('txtClosedDate');
		showObj('closeDateCalendar');
	}
	else {
		hideObj('txtClosedDate');
		hideObj('closeDateCalendar');
		frm.txtClosedDate.value="";
	}
}
	
	function showDueDate(){
		showCalendar(frm.txtDueDate, frm.txtDueDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showCloseDate(){
		showCalendar(frm.txtClosedDate, frm.txtClosedDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showStartDate(){
		showCalendar(frm.txtStartDate, frm.txtStartDate, "dd-mmm-yy",null,1,-1,-1,true);
	}

</SCRIPT>
</BODY>
</HTML>