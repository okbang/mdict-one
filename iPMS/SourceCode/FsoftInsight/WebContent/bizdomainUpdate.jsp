<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date, com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>bizdomainUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></Script>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadAdminMenu();frm.bizdomainName.focus()">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.bizdomainUpdate.BusinessDomain")%> </P>
<FORM name="frm" action="Fms1Servlet" method="post">
<%
	final int bizdomainID = Integer.parseInt(request.getParameter("bizdomainID"));
	Vector bizdomainList=(Vector)session.getAttribute("bizdomainList");
	final BizDomainInfo bizdomainInfo = (BizDomainInfo) bizdomainList.elementAt(bizdomainID);
%>
<TABLE class="Table" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.bizdomainUpdate.Updatebusinessdomain")%> </CAPTION>
    <COL span="1" width="150">
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.bizdomainUpdate.BusinessDomain")%>* </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" name="bizdomainName" maxlength="50" value="<%=bizdomainInfo.name%>"></TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel">Status</TD>
        	<TD class="ColumnLabel">
        		<SELECT name = "selStatus" class="COMBO"> 
        		    <% byte bStatus = bizdomainInfo.domainStatus; %>
        			<OPTION value="0" <%=bStatus == 0 ? "selected" : ""%> >Open</OPTION>
        			<OPTION value="1" <%=bStatus == 1 ? "selected" : ""%> >Expired</OPTION>
        		</SELECT>
        	</TD>	
        </TR>
    </TBODY>
</TABLE>

<P><INPUT type="button" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.bizdomainUpdate.OK")%> " class="BUTTON" onclick="onOK();">
<INPUT type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.bizdomainUpdate.Delete")%> " class="BUTTON" onclick="onDelete()" >
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.bizdomainUpdate.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.BUSINESS_DOMAIN%>)"></P>
<INPUT type="hidden" name="reqType">
<INPUT type="hidden" name="bizdomainID" value="<%=bizdomainID%>">
</FORM>
</BODY>
<SCRIPT>

function onOK() {
	if (trim(frm.bizdomainName.value) == "") {
		window.alert("<%=languageChoose.getMessage("fi.jsp.bizdomainUpdate.BusinessDomainNameCannotBeEmpty")%>");
		frm.bizdomainName.focus();
		return;
	}
	frm.reqType.value = "<%=Constants.BIZDOMAIN_UPDATE%>";
	frm.submit();
}
function onDelete() {
	if (window.confirm("<%=languageChoose.getMessage("fi.jsp.bizdomainUpdate.AreYouSureToDeleteThisBusinessDomain")%>")!= 0) {
		frm.reqType.value = "<%=Constants.BIZDOMAIN_REMOVE%>";
		frm.submit();
	}
}

</SCRIPT>
</HTML>
