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
<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>issueAddNew.jsp</TITLE>
</HEAD>

<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");

	Vector vtProcess = (Vector)session.getAttribute("vtProcess");
	UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
	IssueInfo oldIssueInfo = new IssueInfo();
	if (request.getAttribute("IssueInfo") != null){
		oldIssueInfo = (IssueInfo)request.getAttribute("IssueInfo");
	}
	String srtName = "";
	if (userLoginInfo != null){
		srtName = userLoginInfo.Name;
	}
	Date currentDate = new Date();
	String sDate = CommonTools.dateFormat(currentDate);
	String strDueDate = "";
	String strClosedDate = "";
	if (oldIssueInfo.dueDate != null){
		strDueDate = CommonTools.dateFormat(oldIssueInfo.dueDate);
	}
	if (oldIssueInfo.closeDate != null){
		strClosedDate = CommonTools.dateFormat(oldIssueInfo.closeDate);
	}

	String strMenuType=CommonTools.getMnuFunc(session);
	//PQA specific------------
	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
	WUCombo wuCombo=null;
	if(workUnitID==Parameters.PQA_WU)
		wuCombo=new WUCombo(request,WUCombo.MODE_UPDATE);

	//------------------------
%>
<BODY onLoad="<%=strMenuType%>frmOnload();" class="BD">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Issues")%></P>
<FORM action="Fms1Servlet" name="frm" method="POST">
<TABLE class="Table" width="60%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Addnewissue")%></CAPTION>
    <TBODY>
        <TR>
            <TD height="57" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Description")%>*</TD>
            <TD height="57" class="CellBGR3"><TEXTAREA rows="4" cols="50" name="txtDescription"><%=ConvertString.toHtml(oldIssueInfo.description)%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Priority")%></TD>
            <TD class="CellBGR3">
            	<SELECT name="cboPriority" class="COMBO" style='width:100'>
	                <%for (int i = 0; i < IssueInfo.priorityFlds.length; i++) { %>
		                <OPTION value="<%=i%>" <%=((i == oldIssueInfo.priorityID)?"selected":"")%> >
		                <%=languageChoose.getMessage(IssueInfo.priorityFlds[i])%>
		                </OPTION>
	                <%}%>
	            </SELECT>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Status")%></TD>
            <TD class="CellBGR3">
            	<SELECT name="cboStatus" onchange="occurredStatus()" class="COMBO" style='width:100'>
	                <%for (int i = 0; i < IssueInfo.statusFlds.length; i++) {%>
	                	<OPTION value="<%=i%>" <%=((i == oldIssueInfo.statusID)?"selected":"")%>>
	                	<%=languageChoose.getMessage(IssueInfo.statusFlds[i])%>
	                	</OPTION>
	                <%}%>
	            </SELECT>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Type")%></TD>
            <TD class="CellBGR3">
           		<SELECT name="cboType" class="COMBO" style='width:100'>
	                <%for (int i = 0; i < IssueInfo.typeFlds.length; i++) { %>
		                <OPTION value="<%=i%>" <%=((i == oldIssueInfo.typeID)?"selected":"")%> >
		                <%=languageChoose.getMessage(IssueInfo.typeFlds[i])%>
		                </OPTION>
	                <%}%>
	            </SELECT>
	        </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Processrelated")%></TD>
            <TD class="CellBGR3">
            	<SELECT name="cboProcess" class="COMBO" style='width:182'>
		           	<%
		           		for(int i=0;i<vtProcess.size();i++){
		           			ProcessInfo psInfo=(ProcessInfo)vtProcess.get(i);
		           	%>
		           	<OPTION value="<%=psInfo.processId%>" <%=((psInfo.processId == oldIssueInfo.processId)?"selected":"")%>><%=languageChoose.getMessage(psInfo.name)%></OPTION>
		           	<%}%>
	            </SELECT>
			</TD>
        </TR>
        <%if (wuCombo != null){ %>
         <TR>
	        <TD height="23" class="ColumnLabel"><%=languageChoose.getMessage(wuCombo.label)%></TD>
	        <TD height="23" class="CellBGR3"><%=wuCombo.html%></TD>
	     </TR>
        <%}%>
        <TR>
            <TD height="23" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Owner")%></TD>
            <TD class="CellBGR3">
            	<INPUT name="strAccountName" size="30" type="text" value="<%=oldIssueInfo.owner%>"/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.issueAddnew.Search")%>" onclick="onCheckAccount(event)"> <BR>
	            <INPUT type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.issueAddnew.Account")%>       <INPUT
				type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Name")%><BR>
            </TD>
        </TR>
        <TR>
            <TD height="20" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Creator")%></TD>
            <TD height="20" class="CellBGR3"><INPUT type="hidden" name="txtCreator" value='<%=srtName%>'>
            	<%=srtName%></TD>
        </TR>
        <TR>
            <TD height="18" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.CreatedDate")%></TD>
            <TD height="18" class="CellBGR3">
            	<INPUT size="15" type="hidden" name="txtStartDate" value="<%=sDate%>"><%=sDate%>
            </TD>
        </TR>
        <TR>
            <TD height="18" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.DueDate")%>*</TD>
            <TD height="18" class="CellBGR3"><INPUT size="9" type="text" name="txtDueDate" value="<%=strDueDate%>" maxlength="9">
	            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showDueDate()'>
            	(DD-MMM-YY)]
            </TD>
        </TR>
        <TR>
            <TD height="17" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.ClosedDate")%></TD>
            <TD height="17" class="CellBGR3"><INPUT size="9" type="text" name="txtClosedDate" value="<%=strClosedDate%>" maxlength="9">
	            
            	<IMG id="closeDateCalendar" name="closeDateCalendar" src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showCloseDate()'>
            	(DD-MMM-YY)]
            </TD>
        </TR>
        <TR>
            <TD height="59" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.CommentSolution")%></TD>
            <TD height="59" class="CellBGR3"><TEXTAREA rows="4" cols="50" name="txtComment"><%=oldIssueInfo.comment%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD height="48" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Reference")%></TD>
            <TD height="48" class="CellBGR3"><TEXTAREA rows="4" cols="50" name="txtReference" ><%=oldIssueInfo.reference%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="hidden" name="reqType" value="<%=Constants.ISSUE_ADDNEW%>">
<INPUT type="button" name="ok" value="<%=languageChoose.getMessage("fi.jsp.issueAddnew.OK")%>" onclick="doAction(this);" class="BUTTON">
<INPUT type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.issueAddnew.Cancel")%>" onclick="doAction(this);" class="BUTTON">
</FORM>

<SCRIPT language="javascript">
	function showDueDate(){
		showCalendar(frm.txtDueDate, frm.txtDueDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showCloseDate(){
		showCalendar(frm.txtClosedDate, frm.txtClosedDate, "dd-mmm-yy",null,1,-1,-1,true);
		
	}
//do execute filltering User ---Start
<%if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null){%>
		alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.AccountError")%>");
		frm.strAccountName.focus();
<%
		request.removeAttribute(StringConstants.FILLTER_USER_ERROR);
	}else{%>
		frm.txtDescription.focus();
		
<%}%>
	function onCheckAccount(event){
		if (trim(frm.strAccountName.value) == "") {
	  		window.alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.Ownercannotnotbeempty")%>");
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
        var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_USER%>" + "&Account=" + frm.strAccountName.value + "&Type=" + rdAccountName;
		javascript:ajax_showOptions(document.forms['frm'].strAccountName, url, '', event);
	}
//do execute filltering User ---End
	function doAction(button)
	{  	  		
	  	if (button.name == "cancel") 
	  		doIt(<%=Constants.ISSUE%>);
	  	if (button.name == "ok") {
	  		if (frm.txtClosedDate.value == null)
	  			frm.txtClosedDate.value = "";
	  		if (trim(frm.txtDescription.value) == "") {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.Descriptioncannotbeempty")%>");
	  			frm.txtDescription.focus();
	  			return;
	  		}
	  		else if (frm.txtDescription.value.length > 500) {
	  			window.alert("Description length cannot be longer than 500 characters!");
	  			frm.txtDescription.focus();
	  			return;
	  		}
	  		else if (trim(frm.strAccountName.value) == "") {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.Ownercannotnotbeempty")%>");
	  			frm.strAccountName.focus();
	  			return;
	  		}
	  		else if (!isDate(frm.txtStartDate.value)) {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.Createddateisinvalid")%>");
	  			frm.txtStartDate.focus();
	  			return;
	  		}
	  		else if (trim(frm.txtDueDate.value) == "") {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.Duedatecannotbeempty")%>");
				frm.txtDueDate.focus();
				return;
	  		}
	  		else if ((trim(frm.txtDueDate.value) != "") && (!isDate(frm.txtDueDate.value))) {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.Duedateisinvalid")%>");
	  			frm.txtDueDate.focus();
	  			return;
	  		}
	  		else if ((trim(frm.txtDueDate.value) != "") && (compareDate(frm.txtStartDate.value, frm.txtDueDate.value) == -1)) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.Duedatemustbeaftercreateddate")%>");
				frm.txtDueDate.focus();
				return;
		 	}
	  		else if (frm.txtComment.value.length > 500) {
	  			window.alert("Comment length cannot be longer than 500 characters!");
	  			frm.txtComment.focus();
	  			return;
	  		}
	  		else if ((trim(frm.txtClosedDate.value) != "") && (!isDate(frm.txtClosedDate.value))) {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.Closeddateisinvalid")%>");
	  			frm.txtClosedDate.focus();
	  			return;
	  		}
	  		else if ((trim(frm.txtClosedDate.value) != "") && (compareDate(frm.txtStartDate.value, frm.txtClosedDate.value) == -1)) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.Closeddatemustbeaftercreateddate")%>");
				frm.txtClosedDate.focus();
	  			return;
		 	}
			else if ((trim(frm.txtClosedDate.value) != "") && (compareToToday(frm.txtClosedDate.value) == 1)) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.Closeddatemustbetodayorbefore")%>");
				frm.txtClosedDate.focus();
	  			return;
	 		}
	  		else if (frm.txtReference.value.length > 200) {
	  			window.alert("Reference length cannot be longer than 200 characters!");
	  			frm.txtReference.focus();
	  			return;
	  		}
	  		else if ((frm.cboStatus.value == 1 || frm.cboStatus.value == 2 ) && trim(frm.txtClosedDate.value) == "") {
	  			window.alert("<%= languageChoose.getMessage("fi.jsp.issueAddnew.Closeddateismandatory")%>");
	  			frm.txtClosedDate.focus();
	  			return;
	  		}
	  		else {
	  			frm.submit();
	  		}
	  	}
	}
	
	function frmOnload() {
	 hideObj('txtClosedDate');
	 occurredStatus();
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
</SCRIPT> 
</BODY>
</HTML>