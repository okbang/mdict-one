<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
	<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
	</SCRIPT>
	<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
	<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
	<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
	<SCRIPT src="jscript/ajax.js"></SCRIPT>
	<TITLE>woSignature.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onload="loadPrjMenu()">
<%
	int right = Security.securiPage("Work Order",request,response);
	UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
	Vector sigList = new Vector();
	if (request.getAttribute("WOSigList") != null){
		sigList = (Vector) request.getAttribute("WOSigList");
	}
	Vector changeSigList = new Vector();
	if (request.getAttribute("WOChangeSigList") != null){
		changeSigList = (Vector) request.getAttribute("WOChangeSigList");
	}
	Vector internalSigList = new Vector();
	if (request.getAttribute("WOInternalSigList") != null){
		internalSigList = (Vector) request.getAttribute("WOInternalSigList");
	}
	
	String strSigUser = "";
	String strChangeUser = "";
	String strInternalUser = "";
	String strError = (String)request.getAttribute(StringConstants.FILLTER_USER_ERROR);
	if (strError != null && !"".equals(strError)){
		int iType = Integer.parseInt(strError);
		switch(iType){
			case 0: strSigUser = (String)request.getParameter("Index");break;
			case 1: strChangeUser = (String)request.getParameter("Index");break;
			case 2: strInternalUser = (String)request.getParameter("Index");break;
		}
	}
	int i = 0;
	String className;
	long id = 0;
	long app = -1;
	int authorized = 0;
	
	if(right == 3) authorized = 1;
	
	int isInList = 0;
	
	long userID = userLoginInfo.developerID;
	
	boolean oneIsApproved=false;
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.woSignature.WorkOrderSignatures")%></P>
<FORM name="frm_woSigDelete" action="Fms1Servlet" method = "get">
	<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_SIG_DEL%>">
	
	<p class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woSignature.WorkorderSignatureList")%></p>
	<TABLE cellspacing="1" class="Table" width="95%">
		<TBODY>
			<TR class="ColumnLabel">
	            <TD width = "24" align = "center"># </TD>
	            <TD width = "25%" > <%=languageChoose.getMessage("fi.jsp.woSignature.Name")%> </TD>
	            <TD width = "20%"> <%=languageChoose.getMessage("fi.jsp.woSignature.Role")%> </TD>
	            <TD width = "20%"> <%=languageChoose.getMessage("fi.jsp.woSignature.Position")%> </TD>
	            <TD> <%=languageChoose.getMessage("fi.jsp.woSignature.Approvaldate")%> </TD>
	            <TD> <%=languageChoose.getMessage("fi.jsp.woSignature.Status")%> </TD>
	        </TR>
		<%
			for (i = 0; i < sigList.size(); i++) {
			 	className=(i%2==0)?"CellBGRnews":"CellBGR3";
			 	SignatureInfo signatureInfo = (SignatureInfo)sigList.elementAt(i);
			 	id = signatureInfo.getDeveloperId();
			 	app = signatureInfo.getApprovalStatus();
			 	if ((id == userID) && (app == 0)) {
			 		isInList = 1;
			 	}
		%>
			<TR>
				<TD width = "24" align = "center" class="CellBGR3">
					<%if(authorized == 1){%>
						<INPUT type="checkbox" name="delWOApprovalID" value="<%=signatureInfo.getApprovalId()%>"> 
					<%}else{%>            
						<%=(i+1)%>
					<%}%>
				</TD>
				<TD class="CellBGR3"><%=signatureInfo.getDeveloperName()%></TD>
				<TD class="CellBGR3"><%=signatureInfo.getDeveloperRole()%></TD>
				<TD class="CellBGR3"><%=signatureInfo.getDeveloperPosition()%></TD>
				<TD class="CellBGR3"><%if (signatureInfo.getApprovalStatus()==1){%><%=signatureInfo.getApprovalDate()%><%}%></TD>
	            <TD class="CellBGR3"><font  <%if(signatureInfo.getApprovalStatus()==1){ 
							oneIsApproved =true;// used for removing reset button if nothing to reset
							%>color="#0000aa"><%=languageChoose.getMessage("fi.jsp.woSignature.Approved")%>
						<%}else{%>
							color="#aa0000"><%=languageChoose.getMessage("fi.jsp.woSignature.Unapproved")%> <%}%></font></TD>
			</TR>
<%
			}
%>
		</TBODY>
	</TABLE>
</FORM>
<FORM name="frm_sigApprove" action="Fms1Servlet" method = "get">
	<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_SIG_APPROVE%>" >
	<INPUT type = "hidden" name="woSig_devID" value="<%=Long.toString(userLoginInfo.developerID)%>">
	<INPUT type = "hidden" name="woSig_app" value="1">
</FORM>
<FORM name="frm_sigDisapprove" action="Fms1Servlet" method = "get">
	<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_SIG_RESET%>" >
</FORM>
<BR>
<FORM name="frm_woSigAdd">
<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_SIG_ADD%>">
	<TABLE cellspacing="1" class="Table" width="95%">
		<TBODY>
			<TR>
				<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woSignature.UserAccount")%></TD>
	            <TD class="CellBGR3">
	            	<INPUT name="strAccountName" size="30" type="text" value="<%=strSigUser%>"/>
	            	<INPUT type="button" class="BUTTON" name="check_woSigAdd" value="<%=languageChoose.getMessage("fi.jsp.woSignature.Search")%>" onclick="onCheckAccount(this, event)"> <BR>
		            <INPUT type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.woSignature.Account")%>
		            <INPUT type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.woSignature.Name")%><BR>
	            </TD>
			</TR>
		</TBODY>
	</TABLE>
	<TABLE align="left">
		<TR>
	<%
			if(isInList == 1){
				isInList = 0;
	%>
			<TD><INPUT type="button" name="Add" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Approve")%> " onclick="javascript:on_Submit1();" class="BUTTON"></TD>
	<%
			}
			if (authorized == 1 ){
				if ( oneIsApproved ){
	%>
			<TD><INPUT type="button" name="Add" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Reset")%> " onclick="javascript:on_Submit2();" class="BUTTON"></TD>
	<%
				}
				if (sigList.size() > 0) {
	%>
			<TD><INPUT type="button" name="Del" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Delete")%> " onclick="javascript:on_Submit7();" class="BUTTON"></TD>
<%
				}
%>
			<TD><INPUT type="button" name="Add" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Addnew")%> " onclick="javascript:on_Submit8();" class="BUTTON"></TD>
	<%
			}
	%>
		</TR>
	</TABLE>
</FORM>
<BR>
<BR>
<BR>
<FORM name="frm_changeSigDelete" action="Fms1Servlet#change" method = "get">
<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_CHANGE_SIG_DEL%>">
	<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION align="left" class="TableCaption"><A name="change"> <%=languageChoose.getMessage("fi.jsp.woSignature.ChangeSignatureList")%> </A></CAPTION>
	<TBODY>
        <TR class="ColumnLabel" >
            <TD width = "24" align = "center"># </TD>
            <TD width = "25%"> <%=languageChoose.getMessage("fi.jsp.woSignature.Name1")%> </TD>
            <TD width = "20%"> <%=languageChoose.getMessage("fi.jsp.woSignature.Role1")%> </TD>
            <TD width = "20%"> <%=languageChoose.getMessage("fi.jsp.woSignature.Position1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.woSignature.Approvaldate1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.woSignature.Status1")%> </TD>
        </TR>
<%
	oneIsApproved=false;
	for (i = 0; i < changeSigList.size(); i++) {
	 	className =(i%2==0)?"CellBGRnews":"CellBGR3";
		SignatureInfo signatureInfo2 = (SignatureInfo)changeSigList.elementAt(i);
	 	id = signatureInfo2.getDeveloperId();
	 	
	 	app = signatureInfo2.getApprovalStatus();
	 	if((id == userID) && (app == 0)) {
	 		isInList = 1;
	 	}
%>
		<TR class="<%=className%>">
			<TD width = "24" align = "center" class="CellBGR3">
<%
			if(authorized == 1){
%>
			<INPUT type="checkbox" name="delWOApprovalID2" value="<%=signatureInfo2.getApprovalId()%>">
<%			}else{%>
				<%=(i+1)%>
<%
			}
%>		            
			</TD>
			<TD class="CellBGR3"><%=signatureInfo2.getDeveloperName()%></TD>
			<TD class="CellBGR3"><%=signatureInfo2.getDeveloperRole()%></TD>
			<TD class="CellBGR3"><%=signatureInfo2.getDeveloperPosition()%></TD>
			<TD class="CellBGR3"><%if(signatureInfo2.getApprovalStatus()==1){%><%=signatureInfo2.getApprovalDate()%><%}%></TD>
			<TD class="CellBGR3"><FONT <%if(signatureInfo2.getApprovalStatus()==1){ 
								oneIsApproved =true;// used for removing reset button if nothing to reset
								%>color="#0000aa"><%=languageChoose.getMessage("fi.jsp.woSignature.Approved1")%>
							<%}else{%>
								color="#aa0000"><%=languageChoose.getMessage("fi.jsp.woSignature.Unapproved1")%><%}%></FONT></TD>
		</TR>
<%
	}
%>
	</TBODY>
	</TABLE>
</FORM>
<BR>
<FORM name="frm_changeSigApprove" action="Fms1Servlet#change" method = "get">
	<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_CHANGE_SIG_APPROVE%>" >
	<INPUT type = "hidden" name="woChangeSig_devID" value="<%=Long.toString(userLoginInfo.developerID)%>">
	<INPUT type = "hidden" name="woChangeSig_app" value="1">
</FORM>
<FORM name="frm_changeSigDisapprove" action="Fms1Servlet#change" method = "get">
	<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_CHANGE_SIG_RESET%>" >
</FORM>
<FORM name = "frm_changeSigAdd" action="Fms1Servlet#change" method = "get">
<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_CHANGE_SIG_ADD%>">
	<TABLE cellspacing="1" class="Table" width="95%">
		<TBODY>
			<TR>
				<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woSignature.UserAccount")%></TD>
	            <TD class="CellBGR3">
	            	<INPUT name="strAccountName" size="30" type="text" value="<%=strChangeUser%>"/>
	            	<INPUT type="button" class="BUTTON" name="check_changeSigAdd" value="<%=languageChoose.getMessage("fi.jsp.woSignature.Search")%>" onclick="onCheckAccount(this, event)"> <BR>
		            <INPUT type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.woSignature.Account")%>
		            <INPUT type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.woSignature.Name")%><BR>
	            </TD>
			</TR>
		</TBODY>
	</TABLE>
	<TABLE align="left">
		<TR>
<%
		if(isInList == 1){
			isInList = 0;
%>
			<TD><INPUT type="button" name="Add2" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Approve1")%> " onclick="javascript:on_Submit3();" class="BUTTON"></TD>
<%
		}
		if (authorized == 1){
			if (oneIsApproved){
%>
			<TD><INPUT type="button" name="Add2" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Reset1")%> " onclick="javascript:on_Submit4();" class="BUTTON"></TD>
<%
			}
			if (changeSigList.size() > 0){
%>
			<TD><INPUT type="button" name="Del" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Delete1")%> " onclick="javascript:on_Submit9();" class="BUTTON"></TD>
<%
			}
%>
			<TD><INPUT type="button" name="Add" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Addnew1")%> " onclick="javascript:on_Submit10();" class="BUTTON"></TD>
<%
		}
%>
		</TR>
	</TABLE>
</FORM>
<BR>
<BR>
<BR>
<FORM name="frm_internalSigDelete" action="Fms1Servlet#internal" method = "get">
	<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_INTERNAL_SIG_DEL%>" >  
	<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION align="left" class="TableCaption"><A name="internal"> <%=languageChoose.getMessage("fi.jsp.woSignature.InternalAcceptanceSignatureLis")%> </A></CAPTION>
	<TBODY>
        <TR class="ColumnLabel">
            <TD width = "24" align = "center"># </TD>
            <TD width = "25%"> <%=languageChoose.getMessage("fi.jsp.woSignature.Name2")%> </TD>
            <TD width = "20%"> <%=languageChoose.getMessage("fi.jsp.woSignature.Role2")%> </TD>
            <TD width = "20%"> <%=languageChoose.getMessage("fi.jsp.woSignature.Position2")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.woSignature.Approvaldate2")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.woSignature.Status2")%> </TD>
        </TR>
<%
		oneIsApproved=false;
		for (i = 0; i < internalSigList.size(); i++) {
		 	className =(i%2==0)?"CellBGRnews":"CellBGR3";
			SignatureInfo signatureInfo3 = (SignatureInfo)internalSigList.elementAt(i);
		 	id = signatureInfo3.getDeveloperId();
		 	app = signatureInfo3.getApprovalStatus();
		 	if ((id == userID) && (app == 0)) {
		 		isInList = 1;
		 	}
%>
		<TR class="<%=className%>">
			<TD width = "24" align = "center" class="CellBGR3">
<%
			if(authorized == 1){
%>
				<INPUT type="checkbox" name="delWOApprovalID3" value="<%=signatureInfo3.getApprovalId()%>">
<%
			}else{%>
				<%=(i+1)%>
<%
			}
%>
			</TD>
			<TD class="CellBGR3"><%=signatureInfo3.getDeveloperName()%></TD>
			<TD class="CellBGR3"><%=signatureInfo3.getDeveloperRole()%></TD>
			<TD class="CellBGR3"><%=signatureInfo3.getDeveloperPosition()%></TD>
			<TD class="CellBGR3"><%if(signatureInfo3.getApprovalStatus()==1){%><%=signatureInfo3.getApprovalDate()%><%}%></TD>
			<TD class="CellBGR3"><FONT <%if(signatureInfo3.getApprovalStatus()==1){ 
            					oneIsApproved =true;// used for removing reset button if nothing to reset
								%>color="#0000aa"><%=languageChoose.getMessage("fi.jsp.woSignature.Approved2")%>
							<%}else{%>
								color="#aa0000"><%=languageChoose.getMessage("fi.jsp.woSignature.Unapproved2")%><%}%></FONT></TD>
		</TR>
<%
		}
%>
	</TBODY>
	</TABLE>
</FORM>
<BR>
<FORM name="frm_internalSigApprove" action="Fms1Servlet#internal" method = "get">
	<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_INTERNAL_SIG_APPROVE%>" >
	<INPUT type = "hidden" name="woInternalSig_devID" value="<%=Long.toString(userLoginInfo.developerID)%>">
	<INPUT type = "hidden" name="woInternalSig_app" value="1">
</FORM>
<FORM name="frm_internalSigDisapprove" action="Fms1Servlet#internal" method = "get">
	<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_INTERNAL_SIG_RESET%>" >
</FORM>
<FORM name = "frm_internalSigAdd" action="Fms1Servlet#internal" method = "get">
<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_INTERNAL_SIG_ADD%>" >
	<TABLE cellspacing="1" class="Table" width="95%">
		<TBODY>
			<TR>
				<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.woSignature.UserAccount")%></TD>
	            <TD class="CellBGR3">
	            	<INPUT name="strAccountName" size="30" type="text" value="<%=strInternalUser%>"/>
	            	<INPUT type="button" class="BUTTON" name="check_internalSigAdd" value="<%=languageChoose.getMessage("fi.jsp.woSignature.Search")%>" onclick="onCheckAccount(this, event)"> <BR>
		            <INPUT type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.woSignature.Account")%>
		            <INPUT type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.woSignature.Name")%><BR>
	            </TD>
			</TR>
		</TBODY>
	</TABLE>
	<TABLE align="left">
		<TR>
<%
			if(isInList == 1){
				isInList = 0;
%>
			<TD><INPUT type="button" name="Add3" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Approve2")%> " onclick="javascript:on_Submit5();" class="BUTTON"></TD>
<%
			}
			if(authorized == 1){
				if (oneIsApproved) {
%>
			<TD><INPUT type="button" name="Add3" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Reset2")%> " onclick="javascript:on_Submit6();" class="BUTTON"></TD>
<%
				}
				if (internalSigList.size() > 0){
%>
			<TD><INPUT type="button" name="Del" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Delete2")%> " onclick="javascript:on_Submit11();" class="BUTTON"></TD>
<%
				}
%>
			<TD><INPUT type="button" name="Add" value=" <%=languageChoose.getMessage("fi.jsp.woSignature.Addnew2")%> " onclick="javascript:on_Submit12();" class="BUTTON"></TD>
<%
			}
%>
		</TR>
	</TABLE>
</FORM>

<script language = "javascript">
	// do execute filltering User ---Start
<%
	if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null) {
%>
		alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.AccountError")%>");
		if (frm_woSigAdd.strAccountName.value != ""){
			frm_woSigAdd.strAccountName.focus();
		}
		else if (frm_changeSigAdd.strAccountName.value != ""){
			frm_changeSigAdd.strAccountName.focus();
		}
		else {
			frm_internalSigAdd.strAccountName.focus();
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

	function onCheckAccount(button, event){
		var strUser = "";
		var strForm = "";
		var type = 0;
		if (button.name == "check_woSigAdd"){
			strUser = frm_woSigAdd.strAccountName;
			strForm = frm_woSigAdd;
		}
		else{
			if (button.name == "check_changeSigAdd"){
				strUser = frm_changeSigAdd.strAccountName;
				strForm = frm_changeSigAdd;
				type = 1;
			}
			else {
				strUser = frm_internalSigAdd.strAccountName;
				strForm = frm_internalSigAdd;
				type = 2;
			}
		}
		if (trim(strUser.value) == "") {
	  		window.alert("<%=languageChoose.getMessage("fi.jsp.woSignature.YouMustInputUserAccount")%>");
	  		strUser.focus();
	  		return;
	  	}
	    var rd = strForm.rdAccountName;
	    var rdAccountName;
        for(var i = 0; i < rd.length; i++){
		  if(rd[i].checked){
		  	rdAccountName = rd[i].value;
		  	break;
		  }         
        }
        var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_USER%>" + "&Account=" + strUser.value + "&Type=" + rdAccountName + "&Signature=" + type;
		javascript:ajax_showOptions(strUser, url, '', event);
	}
	// do execute filltering User ---End
	
	function checkSubmit(frm){
		for(i = 0; i < frm.elements.length; i++){
			if(frm.elements[i].type=="checkbox" &&  frm.elements[i].checked){
				return true;
			}
		}
		return false;
	}
	
	function on_Submit1() {
		document.frm_sigApprove.submit();
	}

	function on_Submit2() {
		document.frm_sigDisapprove.submit();
	}

	function on_Submit3() {
		document.frm_changeSigApprove.submit();
	}

	function on_Submit4() {
		document.frm_changeSigDisapprove.submit();
	}

	function on_Submit5() {
		document.frm_internalSigApprove.submit();
	}

	function on_Submit6() {
		document.frm_internalSigDisapprove.submit();
	}

	function on_Submit7() {
		if (checkSubmit(frm_woSigDelete)){
			document.frm_woSigDelete.submit();
		}
		else {
			alert("<%=languageChoose.getMessage("fi.jsp.woSignature.PleaseSelectUser")%>");
		}
	}

	function on_Submit8() {
		if (frm_woSigAdd.strAccountName.value == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.woSignature.YouMustInputUserAccount")%>");
			frm_woSigAdd.strAccountName.focus();
			return;
		}
		document.frm_woSigAdd.submit();
	}

	function on_Submit9() {
		if (checkSubmit(frm_changeSigDelete)){
			document.frm_changeSigDelete.submit();
		}
		else {
			alert("<%=languageChoose.getMessage("fi.jsp.woSignature.PleaseSelectUser")%>");
		}
	}

	function on_Submit10() {
		if (frm_changeSigAdd.strAccountName.value == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.woSignature.YouMustInputUserAccount")%>");
			frm_changeSigAdd.strAccountName.focus();
			return;
		}
		document.frm_changeSigAdd.submit();
	}

	function on_Submit11() {
		if (checkSubmit(frm_internalSigDelete)){
			document.frm_internalSigDelete.submit();
		}
		else {
			alert("<%=languageChoose.getMessage("fi.jsp.woSignature.PleaseSelectUser")%>");
		}
	}

	function on_Submit12() {
		if (frm_internalSigAdd.strAccountName.value == ""){
			alert("<%=languageChoose.getMessage("fi.jsp.woSignature.YouMustInputUserAccount")%>");
			frm_internalSigAdd.strAccountName.focus();
			return;
		}
		document.frm_internalSigAdd.submit();
	}
</script>
</BODY>
</HTML>