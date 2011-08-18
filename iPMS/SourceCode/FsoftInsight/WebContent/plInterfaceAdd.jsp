<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plInterfaceAdd.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu(); document.frm_plInterfaceAdd.plInterface_name.focus();">
<%
	Vector devList = (Vector)session.getAttribute("plDevList");
%>

<form name="frm_plInterfaceAdd" action="Fms1Servlet#Interfaces" method="get">
<input type="hidden" name="reqType" value="<%=Constants.PL_INTERFACE_ADD%>">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.ProjectplanInterface")%> </P>

<br>

<TABLE cellspacing="1" class="Table">
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.AddnewInterface")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Name")%>*</TD>
            <TD class="CellBGR3"><input type="text" name="plInterface_name" maxlength="50" size="50"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Position")%></TD>
            <TD class="CellBGR3"><input type="text" name="plInterface_position" maxlength="50" size="50"></TD>
        </TR>
        <TR>
        <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Role")%></TD>
	        <TD class="CellBGR3">
	        	<SELECT name="plInterface_role" class="COMBO">
	         	 	<OPTION value="1"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Customer")%></OPTION>
	         	 	<OPTION value="2"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.FSoftinterface")%></OPTION>
	         	 	<OPTION value="3"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.FPTinterface")%></OPTION>
	         	 	<OPTION value="4"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Managementboard")%></OPTION>
	         	 	<OPTION value="5"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Projectmanager")%></OPTION>
	         	 	<OPTION value="6"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Projectteam")%></OPTION>
	         	 	<OPTION value="7"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Subcontractor")%></OPTION>
	            </SELECT>
	        </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Responsibility")%>*</TD>
            <TD class="CellBGR3">
            	<TEXTAREA name="plInterface_responsibility" cols="50" rows="4"></TEXTAREA>
            </TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Assignedto")%></TD>
        	<TD class="CellBGR3">
        		<SELECT name="plInterface_assignedID" class="COMBO">            
         		<%
         			for (int k = 0; k < devList.size(); k++) {
         				PlanInterfacesInfo planInterfacesInfo = (PlanInterfacesInfo)devList.elementAt(k);
         		%>
         			<OPTION value="<%=planInterfacesInfo.getDeveloperId()%>"><%=planInterfacesInfo.getDeveloperAccount() + " - " + planInterfacesInfo.getDeveloperName()%></OPTION>
         		<%
         			}
         		%>
            	</SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Communication")%></TD>
            <TD class="CellBGR3">
            	<TEXTAREA name="plInterface_communication" cols="50" rows="4"></TEXTAREA>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.TelFaxEmail")%></TD>
            <TD class="CellBGR3">
            	<TEXTAREA name="plInterface_contact" cols="50" rows="4"></TEXTAREA>
            </TD>
        </TR>
    </TBODY>
</TABLE>

</form>

<br>

<input type="button" name="update" value="<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.OK")%>" onclick="javascript:on_Submit();" class="BUTTON">
<input type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Cancel")%>" class="BUTTON" onclick="doIt(<%=Constants.PL_STRUCTURE_GET_PAGE%>);">

<script language = "javascript">
	function on_Submit() {
		if (mandatoryFld(frm_plInterfaceAdd.plInterface_name, "<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Name0")%>"))
 		if (mandatoryFld(frm_plInterfaceAdd.plInterface_responsibility, "<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Responsibility")%>"))
  	 	if (maxLength(frm_plInterfaceAdd.plInterface_responsibility, "<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Responsibility")%>", 200))
  	 	if (maxLength(frm_plInterfaceAdd.plInterface_communication, "<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Communication")%>", 200))
  	 	if (maxLength(frm_plInterfaceAdd.plInterface_contact, "<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Contactinformation")%>", 200))
	  		document.frm_plInterfaceAdd.submit();
	}
</script>

</BODY>
</HTML>