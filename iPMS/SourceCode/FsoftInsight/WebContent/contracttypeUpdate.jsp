<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>contracttypeUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></Script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadAdminMenu();frm.contracttypeName.focus()">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.contracttypeUpdate.ContractType")%> </P>
<FORM name="frm" action="Fms1Servlet" method="post">
<%
	final int contracttypeID = Integer.parseInt(request.getParameter("contracttypeID"));
			
	Vector contractTypeList = (Vector)session.getAttribute("contracttypeList");
	final ContractTypeInfo contractTypeInfo = (ContractTypeInfo) contractTypeList.elementAt(contracttypeID);
%>
<TABLE class="Table" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.contracttypeUpdate.UpdateContractType")%> </CAPTION>
    <COL span="1" width="150">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.contracttypeUpdate.ContractType")%>* </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" name="contracttypeName" maxlength="50" value="<%=contractTypeInfo.contracttypeName%>"></TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.contracttypeUpdate.Description")%></TD>
        	<TD class="CellBGR3"> <TEXTAREA rows="5" cols="100" name="contracttypeDescription"><%=contractTypeInfo.contracttypeDescription%></TEXTAREA></TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel">Status</TD>
        	<TD class="ColumnLabel">
        		<SELECT name="selStatus" class="COMBO"> 
        		    <% byte status = contractTypeInfo.contracttypeStatus; %>
        			<OPTION value="<%=status%>"><%=status == 0 ? "Open" : "Expired" %></OPTION>
        			<OPTION value="<%=status == 0 ? "1" :"0" %>"><%=status == 1 ? "Open" : "Expired" %></OPTION>
        		</SELECT>
        	</TD>	
        </TR>
    </TBODY>
</TABLE>

<P><INPUT type="button" name="ok" value="<%=languageChoose.getMessage("fi.jsp.contracttypeUpdate.OK")%>" class="BUTTON" onclick="onOK();">
<INPUT type="button" name="delete" value="<%=languageChoose.getMessage("fi.jsp.contracttypeUpdate.Delete")%>" class="BUTTON" onclick="onDelete()" >
<INPUT type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.contracttypeUpdate.Cancel")%>" class="BUTTON" onclick="doIt(<%=Constants.CONTRACT_TYPE%>)"></P>
<INPUT type="hidden" name="reqType">
<INPUT type="hidden" name="contracttypeID" value="<%=contracttypeID%>">
</FORM>
<SCRIPT>
function onOK() {
	if (trim(frm.contracttypeName.value) == "") {
		window.alert("<%=languageChoose.getMessage("fi.jsp.contracttypeUpdate.ContractTypeNameCannotBeEmpty")%>");
		frm.contracttypeName.focus();
		return;
	}
	frm.reqType.value = "<%=Constants.CONTRACT_TYPE_UPDATE%>";
	frm.submit();
}
function onDelete() {
	if (window.confirm("<%=languageChoose.getMessage("fi.jsp.contracttypeUpdate.AreYouSureToDeleteThisContractType")%>") != 0) {
		frm.reqType.value = "<%=Constants.CONTRACT_TYPE_REMOVE%>";
		frm.submit();
	}
}
</SCRIPT>
</BODY>
</HTML>