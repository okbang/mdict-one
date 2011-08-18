<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<TITLE>workUnitAddOrg.jsp</TITLE>
	<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
	<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
	<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
	</SCRIPT>
	<TITLE>workUnitAddOrg.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadAdminMenu();">
<BR>
<FORM name="frm" method="POST" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_WORK_UNIT_ORGANIZATION%>" onsubmit="return doSave();" >
<%
	String err=(String)session.getAttribute("addWUerr");
	if(err!=null && err!= "") {
%>
	<p class="ERROR"><%=err%></p>
<%
	session.setAttribute("addWUerr","");
	}
	// store workunit which user input
	OrganizationInfo orgInfo = new OrganizationInfo();
	if (request.getAttribute("OrganizationInfo") != null){
		orgInfo = (OrganizationInfo) request.getAttribute("OrganizationInfo");
	}
	else{
		orgInfo.orgName = "";
	}
%>
<DIV align="left">
	<TABLE cellspacing="1" class="Table" width="95%">
	    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.AddnewWorkUnitOrg")%></CAPTION>
	    <TBODY>
	        <TR>
	            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.OrganizationName")%>* </TD>
	            <TD class="CellBGRnews"><INPUT type="text" maxlength="30" name="txtOrgName" size="60" value="<%=ConvertString.toHtml(orgInfo.orgName)%>"></TD>
	        </TR>
	    </TBODY>
	</TABLE>
</DIV>
<BR>
<p align="left">
<INPUT type="button"  name="OK" value="<%=languageChoose.getMessage("fi.jsp.workUnitAdd.OK")%>" onclick="doSave()" class="BUTTON">&nbsp; <INPUT type="button"  name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.workUnitAdd.Cancel")%>" onclick="doBack()" class="BUTTON"></p>
</FORM>
<SCRIPT language="javascript">
<%
		if (request.getAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE) != null){
%>
			alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.WorkUnitNameAlreadyExist")%>");
<%
			request.removeAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE);
	}
%>

	function doSave(){
  		if (trim(frm.txtOrgName.value) == ""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.YouMustInputOrgName")%>");
  			frm.txtOrgName.focus();
  			return false;
  		}
		frm.submit();
  		return true;
    }
	function doBack(){
		doIt(<%=Constants.GET_WORK_UNIT_LIST%>);
	}
	frm.txtOrgName.focus();
</SCRIPT>
</BODY>
</HTML>