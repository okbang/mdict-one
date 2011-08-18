<%@page import="com.fms1.infoclass.*,com.fms1.common.*, java.text.*,com.fms1.web.*,java.util.*,java.io.*,java.sql.Date,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>dpLogAdd.jsp</TITLE>

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	String prjojectcode = (String)session.getAttribute("projCode");
	Vector cdVt=(Vector)session.getAttribute("vtCommDefect");
	long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
	DPLogInfo dpLogInfo = new DPLogInfo();
	if (request.getAttribute("DefectInfoAddNew") != null){
		dpLogInfo = (DPLogInfo)request.getAttribute("DefectInfoAddNew");
	}
	int iRequestType = Constants.FILLTER_USER;
	if (workUnitID != Parameters.SQA_WU){
		iRequestType = Constants.FILLTER_USER_ASSIGNED_PROJECT;
	}
	
	String strCurDate = CommonTools.dateFormat(new java.util.Date());
	
	if (workUnitID == Parameters.SQA_WU)
	{
%>
<BODY class="BD" onload="loadSQAMenu();frm.dpaction.focus();frmOnload();">
<%
	}else{
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.dpaction.focus();frmOnload();">
<%
	}
%>

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Defectpreventionlog")%> </p>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.DEFECT_LOG_ADDNEW%>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Defectpreventiontask")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.DPTaskAction")%>* </TD>
            <TD class="CellBGR3"><TEXTAREA rows="6" cols="50" name="dpaction"><%=dpLogInfo.dpaction%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Process")%>* </TD>
            <TD class="CellBGR3">
            <SELECT name="processid" class="COMBO" style='width:100'>
                <OPTION value="0" selected> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Prevention")%> </OPTION>
                <OPTION value="1"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Requirement")%> </OPTION>
                <OPTION value="2"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Design")%> </OPTION>
                <OPTION value="3"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Coding")%> </OPTION>
                <OPTION value="4"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Test")%> </OPTION>
                <OPTION value="5"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Training")%> </OPTION>
            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Status")%>* </TD>
            <TD class="CellBGR3">
            <SELECT name="dpstatus" onchange="occurredStatus()" class="COMBO" style='width:100'>
                <OPTION value="0" selected> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Open")%> </OPTION>
                <OPTION value="1"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Closed")%> </OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Commdef.code")%> </TD>
            <TD class="CellBGR3">
            <SELECT name="commdefcode" class="COMBO">
            <OPTION value=""></OPTION>
			<%
            	for (int i = 0; i < cdVt.size(); i++) {
	            	CommDefInfo info2 = (CommDefInfo)cdVt.elementAt(i);
            %>
                <OPTION value="<%=info2.commonDefCode%>" <%=((info2.commonDefCode.equals(dpLogInfo.commonDefCode))?"selected":"")%>><%=info2.commonDefCode%></OPTION>
			<%}%>
            </SELECT>
            </TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Createdate")%> </TD>
        	<TD class="CellBGR3"><INPUT size="9" type="text" name="createdate" maxlength="9" value=<%=CommonTools.dateFormat(new java.util.Date())%>>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showCreatedate()'>         		
        		(DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Assignto")%>* </TD>
            <TD class="CellBGR3">
            	<INPUT name="strAccountName" size="30" type="text" value="<%=dpLogInfo.devAccount%>"/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.dpLogAdd.Search")%>" onclick="onCheckAccount(event)"> <BR>
	            <INPUT type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Account")%>       <INPUT
				type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.dpLogAdd.Name")%><BR>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Targetdate")%>* </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" name="targetdate" maxlength="9" value="<%=("N/A".equals(CommonTools.dateFormat(dpLogInfo.targetDate))? "": CommonTools.dateFormat(dpLogInfo.targetDate))%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showTargetdate()'>
            	(DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Closeddate")%>* </TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" name="closeddate" maxlength="9" value="<%=("N/A".equals(CommonTools.dateFormat(dpLogInfo.closedDate))? "": CommonTools.dateFormat(dpLogInfo.closedDate))%>"> 
				<IMG id="closeddateImage" name="closeddateImage" src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showCloseddate()'>            
            	(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.ResultBenefit")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="6" cols="50" name="dpbenefit"><%=dpLogInfo.dpBenefit%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpLogAdd.Note")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="6" cols="50" name="dpnote"><%=dpLogInfo.dpNote%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="hidden" name="prjojectcode" value="<%=prjojectcode%>">
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.DpLogAdd.OK")%>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.DpLogAdd.Cancel")%>" onclick="doAction(this)"></FORM>
<SCRIPT language="javascript">
  //do execute filltering User ---Start
<%
	if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null){
		if (workUnitID == Parameters.SQA_WU){
%>
			alert("<%=languageChoose.getMessage("fi.jsp.DpLogAdd.AccountError")%>");
			frm.strAccountName.focus();
<%
		}
		else{
%>
			alert("<%=languageChoose.getMessage("fi.jsp.DpLogAdd.AccountErrorAssignedProject")%>");
			frm.strAccountName.focus();
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
  	if (button.name=="btnOk") {
  	
  		if(trim(frm.dpaction.value)==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.DpLogAdd.Thisfieldismandatory")%>");
  		 	frm.dpaction.focus();
  		 	return;
  		}
		if (trim(frm.strAccountName.value) == "") {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogAdd.YouMustInputAssignTo")%>");
	  			frm.strAccountName.focus();
	  			return;
	  	}
  		
  		if(frm.dpaction.value.length>300){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogAdd.ItemLengthCanNotGreaterThan300")%>");
  		  	frm.dpaction.focus(); 
  		  	return;
  	  	}
  		if(frm.targetdate.value==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.DpLogAdd.Thisfieldismandatory")%>");
  		 	frm.targetdate.focus();
  		 	return;
  		}
  		if (!isDate(frm.createdate.value)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogAdd.InvalidDate")%>");
  			frm.createdate.focus();
  		 	return;
  		}
  		if (!beforeTodayFld(frm.createdate,"<%=languageChoose.getMessage("fi.jsp.dpLogAdd.Createdate")%>")) {
	  		 	return;
		}
  		if (!isDate(frm.targetdate.value)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogAdd.InvalidDate")%>");
  			frm.targetdate.focus();
  		 	return;
  		}
		if (frm.dpstatus.value == "1") {
	  		if(frm.closeddate.value==""){
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.DpLogAdd.Thisfieldismandatory")%>");
	  		 	frm.closeddate.focus();
	  		 	return;
		  	}
	  		if (!isDate(frm.closeddate.value)) {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogAdd.InvalidDate")%>");
	  			frm.closeddate.focus();
	  		 	return;
	  		}
			if (!beforeTodayFld(frm.closeddate,"<%=languageChoose.getMessage("fi.jsp.dpLogAdd.Closeddate")%>")) {
	  		 	return;
			}
		}
  		if(frm.dpbenefit.value.length>300){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogAdd.ItemLengthCanNotGreaterThan300")%>");
  		  	frm.dpbenefit.focus(); 
  		  	return;
  	  	}
  		if(frm.dpnote.value.length>300){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.dpLogAdd.ItemLengthCanNotGreaterThan300")%>");
  		  	frm.dpnote.focus(); 
  		  	return;
  	  	}
  		
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
		showObj('closeddateImage');
		frm.closeddate.value="<%=strCurDate%>";
	}
	else {
		hideObj('closeddate');
		hideObj('closeddateImage');
		frm.closeddate.value="";
	}
}

function frmOnload() {
	if (frm.dpstatus.value == "1") {
		showObj('closeddate');
		showObj('closeddateImage');
	}
	else {
		hideObj('closeddate');
		hideObj('closeddateImage');
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