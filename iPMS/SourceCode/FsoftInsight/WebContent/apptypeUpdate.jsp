<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>apptypeUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></Script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadAdminMenu();frm.apptypeName.focus()">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.apptypeUpdate.ApplicationType")%> </P>
<FORM name="frm" action="Fms1Servlet" method="post">
<%
	final int apptypeID = Integer.parseInt(request.getParameter("apptypeID"));
	
	Vector apptypeList = (Vector)session.getAttribute("apptypeList");
	final AppTypeInfo apptypeInfo = (AppTypeInfo) apptypeList.elementAt(apptypeID);
%>
<TABLE class="Table" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.apptypeUpdate.Updateapplicationtype")%> </CAPTION>
    <COL span="1" width="150">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.apptypeUpdate.ApplicationType")%>* </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" name="apptypeName" maxlength="50" value="<%=apptypeInfo.name%>"></TD>
        </TR>
         <TR>
        	<TD class="ColumnLabel">Status</TD>
        	<TD class="ColumnLabel">
        		<SELECT name = "selStatus" class="COMBO"> 
        		    <% byte aStatus = apptypeInfo.appStatus; %>
        			<OPTION value="<%=aStatus%>"><%=aStatus == 0 ? "Open" : "Expired" %></OPTION>
        			<OPTION value="<%=aStatus == 0 ? "1" :"0" %>"><%=aStatus == 1 ? "Open" : "Expired" %></OPTION>
        		</SELECT>
        	</TD>	
        </TR>
    </TBODY>
</TABLE>

<P><INPUT type="button" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.apptypeUpdate.OK")%> " class="BUTTON" onclick="onOK();">
<INPUT type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.apptypeUpdate.Delete")%> " class="BUTTON" onclick="onDelete()" >
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.apptypeUpdate.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.APP_TYPE%>)"></P>
<INPUT type="hidden" name="reqType">
<INPUT type="hidden" name="apptypeID" value="<%=apptypeID%>">
</FORM>
</BODY>
<SCRIPT>

function onOK() {
	if (trim(frm.apptypeName.value) == "") {
		window.alert("<%=languageChoose.getMessage("fi.jsp.apptypeUpdate.ApplicationTypeNameCannotBeEmpty")%>");
		frm.apptypeName.focus();
		return;
	}
	frm.reqType.value = "<%=Constants.APPTYPE_UPDATE%>";
	frm.submit();
}
function onDelete() {
	if (window.confirm("<%=languageChoose.getMessage("fi.jsp.apptypeUpdate.AreYouSureToDeleteThisApplicationType")%>") != 0) {
		frm.reqType.value = "<%=Constants.APPTYPE_REMOVE%>";
		frm.submit();
	}
}

</SCRIPT>
</HTML>
