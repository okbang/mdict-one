<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.sql.Date,com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
	<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
	<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
	<SCRIPT src="jscript/ajax.js"></SCRIPT>
	<TITLE>dpLogUpdate.jsp</TITLE>
	
	<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
	<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
	<SCRIPT language="javascript" src="jscript/javaFns.js">
	</SCRIPT>
	<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
	</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	String prjojectcode = (String)session.getAttribute("projCode");
	Vector dpVt=(Vector)session.getAttribute("vtDefectLog");
	Vector cdVt=(Vector)session.getAttribute("vtCommDefect");
	String vtIDstr=request.getParameter("vtID");
	int vtID = Integer.parseInt(vtIDstr);

	Date curDate = new Date(new java.util.Date().getTime());
	String strCurDate = CommonTools.dateFormat(curDate);
  	
  	DPLogInfo info = (DPLogInfo) dpVt.get(vtID);
	if (request.getAttribute("DefectInfoUpdate") != null){
		info = (DPLogInfo)request.getAttribute("DefectInfoUpdate");
	}

	int right = Security.securiPage("Defects",request,response); 

	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
	int iRequestType = Constants.FILLTER_USER;
	if (workUnitID != Parameters.SQA_WU){
		iRequestType = Constants.FILLTER_USER_ASSIGNED_PROJECT;
	}
	
	String strMenu = "";
	if (workUnitID == Parameters.SQA_WU){
		strMenu = "loadSQAMenu()";
	}
	else{
		strMenu = "loadPrjMenu()";
	}
%>
<BODY class="BD" onload="<%=strMenu%>;frm.dpaction.focus();frmOnload();">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Defectpreventionlog")%> </p>
<FORM method="POST" name="frm" action="Fms1Servlet">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Defectpreventiontask")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.DPcode")%> </TD>
            <TD class="CellBGR3"><%=info.dpcode%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.DPTaskAction")%>* </TD>
            <TD class="CellBGR3"><TEXTAREA rows="6" cols="50" name="dpaction"><%=info.dpaction%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Process")%>* </TD>
            <TD class="CellBGR3">
			<SELECT name="processid" class="COMBO" style='width:100'>
				<OPTION value="0" <%if (info.processID == 0) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Prevention")%> </OPTION>
				<OPTION value="1" <%if (info.processID == 1) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Requirement")%> </OPTION>
				<OPTION value="2" <%if (info.processID == 2) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Design")%> </OPTION>
				<OPTION value="3" <%if (info.processID == 3) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Coding")%> </OPTION>
				<OPTION value="4" <%if (info.processID == 4) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Test")%> </OPTION>
				<OPTION value="5" <%if (info.processID == 5) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Training")%> </OPTION>
			</SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Status")%>* </TD>
            <TD class="CellBGR3">
            <SELECT name="dpstatus" onchange="occurredStatus()" class="COMBO" style='width:100'>
				<OPTION value="0" <%if (info.dpStatus == 0) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Open")%> </OPTION>
				<OPTION value="1" <%if (info.dpStatus == 1) {%> selected
					<%}%>> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Closed")%> </OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Commdefcode")%> </TD>
            <TD class="CellBGR3">
            <SELECT name="commdefcode" class="COMBO">
            <OPTION value=""></OPTION>
			<%
            	for (int i = 0; i < cdVt.size(); i++) {
	            	CommDefInfo info2 = (CommDefInfo)cdVt.elementAt(i);
            %>
                <OPTION value="<%=info2.commonDefCode%>" <% if (info2.commonDefCode.equalsIgnoreCase(info.commonDefCode)) {%> selected <%}%>>
                	<%=info2.commonDefCode%>
                </OPTION>
			<%}%>
            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Createdate")%>* </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" name="createdate" maxlength="9" value ="<%=CommonTools.dateFormat(info.createDate)%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showCreatedate()'>         		                        	
            	(DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Assignto")%>* </TD>
            <TD class="CellBGR3">
            	<INPUT name="strAccountName" size="30" type="text" value="<%=info.devAccount%>"/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.dpLogAdd.Search")%>" onclick="onCheckAccount(event)"> <BR>
	            <INPUT type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Account")%>       <INPUT
				type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.dpLogAdd.Name")%><BR>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Targetdate")%>* </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" name="targetdate" maxlength="9" value ="<%=CommonTools.dateFormat(info.targetDate)%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showTargetdate()'>         		            
            	(DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Closeddate")%>* </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" name="closeddate" maxlength="9" value ="<%=(info.closedDate == null) ? "":CommonTools.dateFormat(info.closedDate)%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showCloseddate()'>                        
            	(DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.ResultBenefit")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="6" cols="50" name="dpbenefit"><%=(info.dpBenefit == null) ? "": info.dpBenefit%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Note")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="6" cols="50" name="dpnote"><%=(info.dpNote == null) ? "": info.dpNote%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="hidden" name="reqType" value="<%=Constants.DEFECT_LOG_UPDATE%>">
<INPUT type="hidden" name="prjojectcode" value="<%=prjojectcode%>">
<INPUT type="hidden" name="dplogid" value="<%=info.dplogID%>">
<INPUT type="hidden" name="dpcode" value="<%=info.dpcode%>">
<P>
<%if (right == 3){%>
<INPUT type="button" name="btnUpdate" value=" <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Update")%> " class="BUTTON" onclick="doAction(this)">
<INPUT type="button" name="btnDelete" value=" <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Delete")%> " class="BUTTON" onclick="doAction(this)">
<%}%>
<INPUT type="button" name="btnCancel" value=" <%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Cancel")%> " class="BUTTON" onclick="doAction(this)"></FORM>
<SCRIPT language="javascript">
  //do execute filtering User ---Start
<%
	if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null){
		if (workUnitID == Parameters.SQA_WU){
%>
			alert("<%=languageChoose.getMessage("fi.jsp.DpLogAdd.AccountError")%>");
<%
		}
		else{
%>
			alert("<%=languageChoose.getMessage("fi.jsp.DpLogAdd.AccountErrorAssignedProject")%>");
<%
		}
		request.removeAttribute(StringConstants.FILLTER_USER_ERROR);
	}
%>

	function onCheckAccount(event){
		if (trim(frm.strAccountName.value) == "") {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogAdd.YouMustInputAssignTo")%>");
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
        var url = "Fms1Servlet?reqType=<%=iRequestType%>" + "&Account=" + frm.strAccountName.value + "&Type=" + rdAccountName;
		javascript:ajax_showOptions(document.forms['frm'].strAccountName, url, '', event);
	}
//do execute filltering User ---End
  function doAction(button)
  {
  	if (button.name=="btnUpdate") {
  	
  		if(trim(frm.dpaction.value)==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.DpLogUpdate.Thisfieldismandatory")%>");
  		 	frm.dpaction.focus();
  		 	return;
  		}
		if (trim(frm.strAccountName.value) == "") {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogAdd.YouMustInputAssignTo")%>");
	  			frm.strAccountName.focus();
	  			return;
	  	}
  		if(frm.dpaction.value.length>300){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogUpdate.ItemLengthCanNotGreaterThan300")%>");
  		  	frm.dpaction.focus(); 
  		  	return;
  	  	}
  		if(frm.targetdate.value==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.DpLogUpdate.Thisfieldismandatory")%>");
  		 	frm.targetdate.focus();
  		 	return;
  		}
  		if (!isDate(frm.targetdate.value)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogUpdate.InvalidDate")%>");
  			frm.targetdate.focus();
  		 	return;
  		}
  		if(frm.createdate.value==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.DpLogUpdate.Thisfieldismandatory")%>");
  		 	frm.createdate.focus();
  		 	return;
  		}
  		if (!isDate(frm.createdate.value)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogUpdate.InvalidDate")%>");
  			frm.createdate.focus();
  		 	return;
  		}
  		if (!beforeTodayFld(frm.createdate,"<%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Createdate")%>")) {
	  		return;
		}
		if (frm.dpstatus.value == "1") {
	  		if(frm.closeddate.value==""){
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.DpLogUpdate.Thisfieldismandatory")%>");
	  		 	frm.closeddate.focus();
	  		 	return;
		  	}
	  		if (!isDate(frm.closeddate.value)) {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogUpdate.InvalidDate")%>");
	  			frm.closeddate.focus();
	  		 	return;
	  		}
			if (!beforeTodayFld(frm.closeddate,"<%=languageChoose.getMessage("fi.jsp.dpLogUpdate.Closeddate")%>")) {
	  		 	return;
			}
		}
  		if(frm.dpbenefit.value.length>300){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogUpdate.ItemLengthCanNotGreaterThan300")%>");
  		  	frm.dpbenefit.focus(); 
  		  	return;
  	  	}
  		if(frm.dpnote.value.length>300){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogUpdate.ItemLengthCanNotGreaterThan300")%>");
  		  	frm.dpnote.focus(); 
  		  	return;
  	  	}
  		
  	  	frm.submit();
  	}
  	if (button.name=="btnDelete") {
  		if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.DpLogUpdate.Areyousuretodelete")%>")){   		
  			return;
  		}
  	  	frm.reqType.value = "<%=Constants.DEFECT_LOG_DELETE%>";
  	  	frm.submit();
  	} 	
  	if (button.name=="btnCancel") {
  		doIt(<%=Constants.DEFECT_LOG%>);
  		return;
  	}
  	
  }

function occurredStatus() {
	if (frm.dpstatus.value == "1") {
		showObj('closeddate');
		frm.closeddate.value="<%=strCurDate%>";
	}
	else {
		hideObj('closeddate');
		frm.closeddate.value="";
	}
}

function frmOnload() {
	if (frm.dpstatus.value == "1") {
		showObj('closeddate');
	}
	else {
		hideObj('closeddate');
		frm.closeddate.value="";
	}
}
	
	function showCreatedate(){
		showCalendar(frm.createdate, frm.createdate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showTargetdate(){
		showCalendar(frm.targetdate, frm.targetdate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showCloseddate(){
		showCalendar(frm.closeddate, frm.closeddate, "dd-mmm-yy",null,1,-1,-1,true);
	}
 </SCRIPT>
</BODY>
</HTML>

