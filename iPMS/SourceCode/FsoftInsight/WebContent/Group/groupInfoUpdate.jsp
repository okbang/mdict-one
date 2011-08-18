<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.*,java.util.Calendar,com.fms1.html.* "%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="JavaScript">
        <%@ include file="../javaFns.jsp"%>
</SCRIPT>
<%	int right = Security.securiPage("Group home",request,response);
	GroupInfo inf = (GroupInfo)session.getAttribute("groupInfo");
	
	String strGroupLeader = "";
	if (inf.leader != null){
		strGroupLeader = inf.leader;
	}
%>
<TITLE>GroupInfoUpdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY  class="BD" onload="loadGrpMenu()">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Groupinformation")%> </P>
<P><BR></P>
<FORM name='frm' action="Fms1Servlet?reqType=<%=Constants.GROUP_INFO_UPDATE%>" method="POST">
<TABLE cellspacing="1" class="Table" width = "95%">
	<TBODY>
		<TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Groupleader")%> </TD>
            <TD class="CellBGR3">
            	<INPUT name="strAccountName" size="30" type="text" value="<%=strGroupLeader%>"/>
            	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Search")%>" onclick="onCheckAccount(event)"> <BR>
	            <input type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Account")%>       <INPUT
				type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Name")%><BR>
            </TD>
        </TR> 
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Groupdescription")%> </TD>
            <TD class="CellBGR3"><TEXTAREA name="desc" rows="10" cols="65"><%=CommonTools.updateString(inf.desc)%></TEXTAREA></TD>
        </TR>
	</TBODY>
</TABLE>
</FORM>
<BR>
<P>
<INPUT type="BUTTON" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.OK")%> " onclick="onSubmit()">
<INPUT type="BUTTON" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Cancel")%> " onclick="doIt(<%=Constants.GROUP_INFO%>)">
</p>
</BODY>
<script language = "javascript">
<%
	if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null){
%>
		alert("<%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.AccountError")%>");
<%
		request.removeAttribute(StringConstants.FILLTER_USER_ERROR);
	}
%>

	function onCheckAccount(event){
		if (trim(frm.strAccountName.value) == ""){
			window.alert("<%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.BlankAccount")%>");
			frm.strAccountName.focus();
			return ;
		}
	    var rd =document.forms['frm'].rdAccountName;
	    var rdAccountName;
        for(var i=0;i<rd.length;i++){
		  if(rd[i].checked){
		  	rdAccountName = rd[i].value;
		  	break;
		  }
        }
        var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_USER%>" + "&Account=" + frm.strAccountName.value + "&Type=" + rdAccountName + "&Group=<%=inf.name%>";
		javascript:ajax_showOptions(document.forms['frm'].strAccountName, url, '', event);
	}
	function onSubmit(){
		if (trim(frm.strAccountName.value) == ""){
			window.alert("<%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.BlankAccount")%>");
			frm.strAccountName.focus();
			return ;
		}
		if (maxLength(frm.desc," <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.description")%> ",2000))
			frm.submit();
	}
	frm.strAccountName.focus();
</Script>
</HTML>