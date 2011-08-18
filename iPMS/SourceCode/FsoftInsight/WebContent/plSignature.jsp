<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
	<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
	<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
	<SCRIPT src="jscript/ajax.js"></SCRIPT>
	<TITLE>plSignature.jsp</TITLE>
	<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
	<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
	</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	int right = Security.securiPage("Project plan",request,response);
	Vector sigList = new Vector();
	if (request.getAttribute("PLSigList") != null){
	 	sigList = (Vector) request.getAttribute("PLSigList");
	}
	Vector changeSigList = new Vector();
	if (request.getAttribute("PLChangeSigList") != null){
		changeSigList = (Vector) request.getAttribute("PLChangeSigList");
	}
	//recover UserAccount when add new has a error
	String strSigUser = "";
	String strChangeSigUser = "";
	String strError = (String)request.getAttribute(StringConstants.FILLTER_USER_ERROR);
	if (strError != null && !"".equals(strError)){
		int iType = Integer.parseInt(strError);
		if (iType == 3){
			strSigUser = (String)request.getParameter("Index");
		}
		else{
			strChangeSigUser = (String) request.getParameter("Index");
		}
	}
	int i = 0;
	String className = "";
	long id = 0;
	long app = -1;
	int authorized = 0;
	if(right == 3) authorized = 1;
	int isInList = 0;
	long userID = 0;
	UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
	if (userLoginInfo != null) {
		userID = userLoginInfo.developerID;
	}
	boolean oneIsApproved = false;
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plSignature.ProjectplanSignatures")%></P>
<br>
<form name="frm_woSigDelete" action="Fms1Servlet" method="get">
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plSignature.Projectplansignaturelist")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.plSignature.Name")%> </TD>
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plSignature.Role")%> </TD>
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plSignature.Position")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.plSignature.Approvaldate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.plSignature.Status")%> </TD>
        </TR>
<input type="hidden" name="reqType" value="<%=Constants.PL_SIG_DEL%>">
<%
for (i = 0; i < sigList.size(); i++) {
 	className = (i%2==0) ? "CellBGRnews" : "CellBGR3";
 	SignatureInfo signatureInfo = (SignatureInfo)sigList.elementAt(i);
 	id = signatureInfo.getDeveloperId();
 	app = signatureInfo.getApprovalStatus();
 	if ((id == userID) && (app == 0)) {
 		isInList = 1;
 	}
%>
<TR class="<%=className%>">
	<TD class="<%=className%>" width="24" align="center" class="CellBGR3">
<%if(authorized == 1){%>
	<INPUT type="checkbox" name="delWOApprovalID" value="<%=signatureInfo.getApprovalId()%>">
<%}else{%>
	<%=(i+1)%>
<%}%>
	</TD>
    <TD class="CellBGR3"><%=signatureInfo.getDeveloperName()%></TD>
    <TD class="CellBGR3"><%=signatureInfo.getDeveloperRole()%></TD>
    <TD class="CellBGR3"><%=signatureInfo.getDeveloperPosition()%></TD>
	<TD class="CellBGR3"><%if(signatureInfo.getApprovalStatus()==1){%><%=signatureInfo.getApprovalDate()%><%}%></TD>
	<TD class="CellBGR3"><font <%if(signatureInfo.getApprovalStatus()==1){
					oneIsApproved =true;// used for removing reset button if nothing to reset
					%>color="#0000aa"><%= languageChoose.getMessage("fi.jsp.plSignature.Approved")%>
				<%}else{%>color="#aa0000"><%=languageChoose.getMessage("fi.jsp.plSignature.Unapproved")%><%}%></font></TD>
</TR>
<%}%>
</TABLE>
</form>
<br>
<form name="frm_sigApprove" action="Fms1Servlet" method="get">
				<input type="hidden" name="reqType" value="<%=Constants.PL_SIG_APPROVE%>">
				<input type="hidden" name="woSig_devID" value="<%=userID%>">
				<input type="hidden" name="woSig_app" value="1">
</form>
<form name="frm_sigDisapprove" action="Fms1Servlet" method="get">
				<input type="hidden" name="reqType" value="<%=Constants.PL_SIG_RESET%>">
</form>
<FORM name="frm_woSigAdd">
<INPUT type="hidden" name="reqType" value="<%=Constants.PL_SIG_ADD%>">
	<TABLE cellspacing="1" class="Table" width="95%">
		<TBODY>
			<TR>
				<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plSignature.UserAccount")%></TD>
	            <TD class="CellBGR3">
	            	<INPUT name="strAccountName" size="30" type="text" value="<%=strSigUser%>"/>
	            	<INPUT type="button" class="BUTTON" name="check_woSigAdd" value="<%=languageChoose.getMessage("fi.jsp.plSignature.Search")%>" onclick="onCheckAccount(this,event)"><BR>
		            <INPUT type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.plSignature.Account")%>
		            <INPUT type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.plSignature.Name")%><BR>
	            </TD>
			</TR>
		</TBODY>
	</TABLE>
	<TABLE align="left">
		<TR>
	<%
			if (isInList == 1) {
				isInList = 0;
	%>
			<TD><input type="button" name="Add" value="<%=languageChoose.getMessage("fi.jsp.plSignature.Approve")%>" onclick="javascript:on_Submit1();" class="BUTTON"></TD>
	<%
			}
			if (authorized == 1) {
				if (oneIsApproved) {
	%>
			<TD><input type="button" name="Add" value="<%=languageChoose.getMessage("fi.jsp.plSignature.Reset")%>" onclick="javascript:on_Submit2();" class="BUTTON"></TD>
	<%
				}
				if (sigList.size() > 0) {
	%>
			<TD><input type="button" name="Del" value="<%=languageChoose.getMessage("fi.jsp.plSignature.Delete")%>" onclick="javascript:on_Submit7();" class="BUTTON"></TD>
<%
				}
%>
			<TD><input type="button" name="Add" value="<%=languageChoose.getMessage("fi.jsp.plSignature.Addnew")%>" onclick="javascript:on_Submit8();" class="BUTTON"></TD>
	<%
			}
	%>
		</TR>
	</TABLE>
</FORM>
<BR>
<BR>
<BR>
<form name="frm_changeSigDelete" action="Fms1Servlet#change" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.PL_CHANGE_SIG_DEL%>" >  
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"><A name="change"> <%=languageChoose.getMessage("fi.jsp.plSignature.Changesignaturelist")%> </A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD width="25%"> <%=languageChoose.getMessage("fi.jsp.plSignature.Name")%> </TD>
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plSignature.Role")%> </TD>
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plSignature.Position")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.plSignature.Approvaldate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.plSignature.Status")%> </TD>
        </TR>
<%
oneIsApproved=false;
for (i = 0; i < changeSigList.size(); i++) {
 	className = (i%2==0) ? "CellBGRnews" : "CellBGR3";
 	SignatureInfo signatureInfo2 = (SignatureInfo)changeSigList.elementAt(i);
 	id = signatureInfo2.getDeveloperId();
 	app = signatureInfo2.getApprovalStatus();
 	if ((id == userID) && (app == 0)) {
 		isInList = 1;
 	}
%>
<tr class="<%=className%>">
	<TD width="24" align="center" class="CellBGR3">
<%if(authorized == 1){%>
	<input type="checkbox" name="delWOApprovalID2" value="<%=signatureInfo2.getApprovalId()%>">
<%}else{%>
	<%=(i+1)%>
<%}%>
    </TD>
    <TD class="CellBGR3"><%=signatureInfo2.getDeveloperName()%></TD>
    <TD class="CellBGR3"><%=signatureInfo2.getDeveloperRole()%></TD>
    <TD class="CellBGR3"><%=signatureInfo2.getDeveloperPosition()%></TD>
	<TD class="CellBGR3"><%if(signatureInfo2.getApprovalStatus()==1){%><%=signatureInfo2.getApprovalDate()%><%}%></TD>
	<TD class="CellBGR3"><font <%if(signatureInfo2.getApprovalStatus()==1){ 
					oneIsApproved =true;// used for removing reset button if nothing to reset
					%>color="#0000aa"><%= languageChoose.getMessage("fi.jsp.plSignature.Approved")%>
				<%}else{%>color="#aa0000"><%= languageChoose.getMessage("fi.jsp.plSignature.Unapproved")%><%}%></font></TD>
</tr>
<%}%>
</table>
</form>
<br>
<form name="frm_changeSigApprove" action="Fms1Servlet#change" method="get">
				<input type="hidden" name="reqType" value="<%=Constants.PL_CHANGE_SIG_APPROVE%>">
				<input type="hidden" name="woChangeSig_devID" value="<%=userID%>">
				<input type="hidden" name="woChangeSig_app" value="1">
</form>
<form name="frm_changeSigDisapprove" action="Fms1Servlet" method="get">
				<input type="hidden" name="reqType" value="<%=Constants.PL_CHANGE_SIG_RESET%>">
</form>
<FORM name="frm_changeSigAdd" action="Fms1Servlet#change" method="get">
<input type="hidden" name="reqType" value="<%=Constants.PL_CHANGE_SIG_ADD%>">
	<TABLE cellspacing="1" class="Table" width="95%">
		<TBODY>
			<TR>
				<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plSignature.UserAccount")%></TD>
	            <TD class="CellBGR3">
	            	<INPUT name="strAccountName" size="30" type="text" value="<%=strChangeSigUser%>"/>
	            	<INPUT type="button" class="BUTTON" name="check_changeSigAdd" value="<%=languageChoose.getMessage("fi.jsp.plSignature.Search")%>" onclick="onCheckAccount(this,event)"><BR>
		            <INPUT type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.plSignature.Account")%>
		            <INPUT type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.plSignature.Name")%><BR>
	            </TD>
			</TR>
		</TBODY>
	</TABLE>
	<TABLE align="left">
		<TR>
<%
		if (isInList == 1) {
			isInList = 0;
%>
			<TD><input type="button" name="Add2" value="<%=languageChoose.getMessage("fi.jsp.plSignature.Approve")%>" onclick="javascript:on_Submit3();" class="BUTTON"></TD>
<%
		}
		if (authorized == 1) {
			if (oneIsApproved) {
%>
			<TD><input type="button" name="Add2" value="<%=languageChoose.getMessage("fi.jsp.plSignature.Reset")%>" onclick="javascript:on_Submit4();" class="BUTTON"></TD>
<%
			}
			if (changeSigList.size() > 0) {
%>
			<TD><input type="button" name="Del" value="<%=languageChoose.getMessage("fi.jsp.plSignature.Delete")%>" onclick="javascript:on_Submit9();" class="BUTTON"></TD>
<%
			}
%>
			<TD><input type="button" name="Add" value="<%=languageChoose.getMessage("fi.jsp.plSignature.Addnew")%>" onclick="javascript:on_Submit10();" class="BUTTON"></TD>
<%
		}
%>
		</TR>
	</TABLE>
</FORM>

<script language="javascript">
	//do execute filltering User ---Start
<%
	if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null) {
%>
		alert("<%=languageChoose.getMessage("fi.jsp.plSignature.AccountError")%>");
		if (frm_woSigAdd.strAccountName.value != ""){
			frm_woSigAdd.strAccountName.focus();
		}
		else {
			frm_changeSigAdd.strAccountName.focus();
		}
<%
		request.removeAttribute(StringConstants.FILLTER_USER_ERROR);
	}
	else {
%>
		frm_woSigAdd.strAccountName.focus();
<%
	}
%>

	function onCheckAccount(button,event) {
		var strUser = "";
		var strForm = "";
		var type = 3;
		if (button.name == "check_woSigAdd") {
			strUser = frm_woSigAdd.strAccountName;
			strForm = frm_woSigAdd;
		} else {
			strUser = frm_changeSigAdd.strAccountName;
			strForm = frm_changeSigAdd;
			type = 4;
		}
		if (trim(strUser.value) == "") {
	  		window.alert("<%=languageChoose.getMessage("fi.jsp.plSignature.YouMustInputUserAccount")%>");
	  		strUser.focus();
	  		return;
	  	}
	    var rd = strForm.rdAccountName;
	    var rdAccountName;
        for (var i = 0; i < rd.length; i++) {
		  if (rd[i].checked) {
		  	rdAccountName = rd[i].value;
		  	break;
		  }
        }
        var url = "Fms1Servlet?reqType=7000" + "&Account=" + strUser.value + "&Type=" + rdAccountName + "&Signature=" + type;
		javascript:ajax_showOptions(strUser, url, '', event);
	}
	// do execute filltering User ---End
	
	function checkSubmit(frm) {
		for(i = 0; i < frm.elements.length; i++) {
			if (frm.elements[i].type=="checkbox" &&  frm.elements[i].checked) {
				return true;
			}
		}
		return false;
	}
	function on_Submit1()
	{
		document.frm_sigApprove.submit();
	}
	function on_Submit2()
	{
		document.frm_sigDisapprove.submit();
	}
	
	function on_Submit3()
	{
		document.frm_changeSigApprove.submit();
	}
	function on_Submit4()
	{
		document.frm_changeSigDisapprove.submit();
	}
	function on_Submit7()
	{
		if (checkSubmit(frm_woSigDelete)){
			document.frm_woSigDelete.submit();
		}
		else {
			alert("<%=languageChoose.getMessage("fi.jsp.plSignature.PleaseSelectUser")%>");
		}
	}
	function on_Submit8()
	{
		if (frm_woSigAdd.strAccountName.value == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.plSignature.YouMustInputUserAccount	")%>");
			return;
		}
		document.frm_woSigAdd.submit();
	}
	function on_Submit9()
	{
		if (checkSubmit(frm_changeSigDelete)){
			document.frm_changeSigDelete.submit();
		}
		else {
			alert("<%=languageChoose.getMessage("fi.jsp.plSignature.PleaseSelectUser")%>");
		}
	}
	function on_Submit10()
	{
		document.frm_changeSigAdd.submit();
	}
</script>

</BODY>
</HTML>