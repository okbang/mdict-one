<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plInterfaceUpdate.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu(); document.frm_plInterfaceUpdate.plInterface_name.focus();">
<%
	InterfaceInfo interfaceInfo = (InterfaceInfo)session.getAttribute("plInterfaceInfo");
	Vector devList = (Vector)session.getAttribute("plDevList");
%>

<form name="frm_plInterfaceUpdate" action="Fms1Servlet#Interfaces" method="get">
<input type="hidden" name="reqType" value="<%=Constants.PL_INTERFACE_UPDATE%>">
<input type="hidden" name="plInterface_ID" value="<%=interfaceInfo.interfaceID%>">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.ProjectplanInterface")%> </P>

<br>

<TABLE cellspacing="1" class="Table">
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.UpdateInterface")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Name")%>*</TD>
            <TD class="CellBGR3"><input type="text" name="plInterface_name" value="<%=((interfaceInfo.name == null) ? "" : interfaceInfo.name)%>" maxlength="50" size="50"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Position")%></TD>
            <TD class="CellBGR3"><input type="text" name="plInterface_position" value="<%=((interfaceInfo.position == null) ? "" : interfaceInfo.position)%>" maxlength="50" size="50"></TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Role")%></TD>
        	<TD class="CellBGR3">
	        	<SELECT name="plInterface_role" class="COMBO">
	         	 	<OPTION value="1" <%if(interfaceInfo.roleID == 1 ){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Customer")%> </OPTION>
	         	 	<OPTION value="2" <%if(interfaceInfo.roleID == 2 ){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.FSoftinterface")%> </OPTION>
	         	 	<OPTION value="3" <%if(interfaceInfo.roleID == 3 ){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.FPTinterface")%> </OPTION>
	         	 	<OPTION value="4" <%if(interfaceInfo.roleID == 4 ){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Managementboard")%> </OPTION>
	         	 	<OPTION value="5" <%if(interfaceInfo.roleID == 5 ){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Projectmanager")%> </OPTION>
	         	 	<OPTION value="6" <%if(interfaceInfo.roleID == 6 ){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Projectteam")%> </OPTION>
	         	 	<OPTION value="7" <%if(interfaceInfo.roleID == 7 ){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Subcontractor")%> </OPTION>         	
	            </SELECT>
	        </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Responsibility")%>*</TD>
            <TD class="CellBGR3">
            	<TEXTAREA name="plInterface_responsibility" cols="50" rows="4"><%=((interfaceInfo.responsibility == null) ? "" : interfaceInfo.responsibility)%></TEXTAREA>
            </TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Assignedto")%></TD>
        	<TD class="CellBGR3">
        		<SELECT name="plInterface_assignedID" class="COMBO">
         	<%
         		for (int k = 0; k < devList.size(); k++) {
         			PlanInterfacesInfo planInterfacesInfo = (PlanInterfacesInfo)devList.elementAt(k);
         		 	long devID = planInterfacesInfo.getDeveloperId();
         	%>
         			<OPTION value="<%=planInterfacesInfo.getDeveloperId()%>" 
         			<%
         				if(devID == interfaceInfo.assignedID) {
         			%>
         				selected
         			<%
         				}
         			%>>
         				<%=planInterfacesInfo.getDeveloperAccount() + " - " + planInterfacesInfo.getDeveloperName()%>
         			</OPTION>
         	<%
         		}
         	%>
            	</SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Communication")%></TD>
            <TD class="CellBGR3">
            	<TEXTAREA name="plInterface_communication" cols="50" rows="4"><%=((interfaceInfo.communication == null) ? "" : interfaceInfo.communication)%></TEXTAREA>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.TelFaxEmail")%></TD>
            <TD class="CellBGR3">
            	<TEXTAREA name="plInterface_contact" cols="50" rows="4"><%=((interfaceInfo.contact == null) ? "" : interfaceInfo.contact)%></TEXTAREA>
            </TD>
        </TR>
    </TBODY>
</TABLE>

</form>

<br>

<input type="button" name="update" value="<%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.OK")%>" onclick="javascript:on_Submit();" class="BUTTON">
<input type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.plInterfaceUpdate.Cancel")%>" class="BUTTON" onclick="doIt(<%=Constants.PL_STRUCTURE_GET_PAGE%>);">

<script language = "javascript">
	function on_Submit() {
  	 	if (mandatoryFld(frm_plInterfaceUpdate.plInterface_name, "<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Name")%>"))
 		if (mandatoryFld(frm_plInterfaceUpdate.plInterface_responsibility, "<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Responsibility")%>"))
  	 	if (maxLength(frm_plInterfaceUpdate.plInterface_responsibility, "<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Responsibility")%>", 200))
  	 	if (maxLength(frm_plInterfaceUpdate.plInterface_communication, "<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Communication")%>", 200))
  	 	if (maxLength(frm_plInterfaceUpdate.plInterface_contact, "<%=languageChoose.getMessage("fi.jsp.plInterfaceAdd.Contactinformation")%>", 200)) {
	  	 	document.frm_plInterfaceUpdate.submit();
	  	}
	}
</script>

</BODY>
</HTML>