<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>plReqChangesAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	boolean isErr = false;	
	ReqChangesInfo errReqInfo = (ReqChangesInfo) request.getAttribute("ErrReqChangesInfo");
	if (errReqInfo != null) {
		isErr = true;
		request.removeAttribute("ErrReqChangesInfo");
	}
%>
<BODY onLoad="loadPrjMenu();document.frmReqChangesAdd.req_logged_location.focus();" class="BD">
<form name ="frmReqChangesAdd" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plReqChangesMng.ProjectPlanRequirementChangesManagement")%> </P>
<% 	
	if (isErr) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>

<TABLE cellspacing="1" class="Table" width="95%" id="tableReqChangesAdd">
<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plReqChangesAdd.AddNewRequirementChanges")%> </CAPTION>
   	<TBODY>
        <TR>
            <TD class="ColumnLabel" width="35%"> Where is the change request logged?* </TD>
            <TD class="CellBGR3" height="57">
            	<TEXTAREA style="width:100%;height:100%" name="req_logged_location"><%=isErr? ConvertString.toHtml(errReqInfo.reqLogLocation) :""%></TEXTAREA>
            </TD>
        </TR>        
    	<TR>
        	<TD class="ColumnLabel" width="35%"> Who logs the change request?* </TD>
            <TD class="CellBGR3" height="57">
            	<TEXTAREA style="width:100%;height:100%" name="req_creator"><%=isErr? ConvertString.toHtml(errReqInfo.reqCreator):""%></TEXTAREA>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="35%"> Who reviews the change request?* </TD>
            <TD class="CellBGR3" height="57">
            	<TEXTAREA style="width:100%;height:100%" name="req_reviewer"><%=isErr? ConvertString.toHtml(errReqInfo.reqReviewer) : ""%></TEXTAREA>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="35%"> Who approves the change request?* </TD>
            <TD class="CellBGR3" height="57">
            	<TEXTAREA style="width:100%;height:100%" name="req_approver"><%=isErr? ConvertString.toHtml(errReqInfo.reqApprover) :""%></TEXTAREA>
            </TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<input type ="hidden" name = "reqType" value = ""/>
<INPUT type="button" name="woSave" value="<%=languageChoose.getMessage("fi.jsp.plReqChangesAdd.Ok")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="woCancel" value="<%=languageChoose.getMessage("fi.jsp.plReqChangesAdd.Cancel")%>" class="BUTTON" onclick="jumpURL('plLifecycle.jsp');">
</form>

<SCRIPT language="JavaScript">

function addSubmit(){	
	if (checkValid()) {
		frmReqChangesAdd.reqType.value=<%=Constants.PL_REQ_CHANGES_MNG_ADD%>;	
		frmReqChangesAdd.submit();
	} else return false;	
}

function doCancel(){
	frmReqChangesAdd.reqType.value =<%=Constants.PL_REQ_CHANGES_MNG_GET_LIST%>;
	frmReqChangesAdd.submit();
}

function checkValid(){
	var arrTxt1	= document.frmReqChangesAdd("req_creator");
	var arrTxt2	= document.frmReqChangesAdd("req_reviewer");
	var arrTxt3	= document.frmReqChangesAdd("req_approver");
	var arrTxt4	= document.frmReqChangesAdd("req_logged_location");
	
	if (trim(arrTxt1.value) =='')  {
		alert("Creator is mandatory");
		arrTxt1.focus();
		return false;
	}
	
	if (trim(arrTxt2.value) =='')  {
		alert("Reviewer is mandatory");
		arrTxt2.focus();
		return false;
	}
	
	if (trim(arrTxt3.value) =='')  {
		alert("Approver is mandatory");
		arrTxt3.focus();
		return false;
	}
	
	if (trim(arrTxt4.value) =='')  {
		alert("Logged location is mandatory");
		arrTxt4.focus();
		return false;
	}

	if(!maxLength(arrTxt1,"Please input less than 600 characters",600))
	return false;			
	if(!maxLength(arrTxt2,"Please input less than 600 characters",600))
	return false;
	if(!maxLength(arrTxt3,"Please input less than 600 characters",600))
	return false;
	if(!maxLength(arrTxt4,"Please input less than 600 characters",600))
	return false;
	
	return true;
}
</SCRIPT>
</BODY>
</HTML>
