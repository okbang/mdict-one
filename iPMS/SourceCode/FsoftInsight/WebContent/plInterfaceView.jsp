<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plInterfaceView.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
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
	InterfaceInfo interfaceInfo = (InterfaceInfo)session.getAttribute("plInterfaceInfo");
	Vector devList = (Vector)session.getAttribute("plDevList");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
		if (Integer.parseInt(archiveStatus) == 4) {
			isArchive = true;
		}
	}
%>

<form name="frm_plInterfaceUpdatePrep" action="Fms1Servlet" method="get">
<input type="hidden" name="reqType" value="<%=Constants.PL_INTERFACE_UPDATE_PREPARE%>">
<input type="hidden" name="plInterface_ID" value="<%=interfaceInfo.interfaceID%>">

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plInterfaceView.ProjectplanInterface")%> </P>

<br>

<TABLE cellspacing="1" class="Table" width="90%">
	<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plInterfaceView.Interfaceinformation")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceView.Name")%></TD>
            <TD class="CellBGR3"><%=((interfaceInfo.name == null) ? "N/A" : interfaceInfo.name)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceView.Position")%></TD>
            <TD class="CellBGR3"><%=((interfaceInfo.position == null) ? "N/A" : interfaceInfo.position)%></TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceView.Role")%></TD>
        	<TD class="CellBGR3">
         	 	<%if(interfaceInfo.roleID == 1 ){%> <%=languageChoose.getMessage("fi.jsp.plInterfaceView.Customer")%> <%}%>
         	 	<%if(interfaceInfo.roleID == 2 ){%> <%=languageChoose.getMessage("fi.jsp.plInterfaceView.FSoftinterface")%> <%}%>
         	 	<%if(interfaceInfo.roleID == 3 ){%> <%=languageChoose.getMessage("fi.jsp.plInterfaceView.FPTinterface")%> <%}%>
         	 	<%if(interfaceInfo.roleID == 4 ){%> <%=languageChoose.getMessage("fi.jsp.plInterfaceView.Managementboard")%> <%}%>
         	 	<%if(interfaceInfo.roleID == 5 ){%> <%=languageChoose.getMessage("fi.jsp.plInterfaceView.Projectmanager")%> <%}%>
         	 	<%if(interfaceInfo.roleID == 6 ){%> <%=languageChoose.getMessage("fi.jsp.plInterfaceView.Projectteam")%> <%}%>
         	 	<%if(interfaceInfo.roleID == 7 ){%> <%=languageChoose.getMessage("fi.jsp.plInterfaceView.Subcontractor")%> <%}%>         
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.plInterfaceView.Responsibility")%></TD>
            <TD class="CellBGR3">
				<%=((interfaceInfo.responsibility == null) ? "N/A" : ConvertString.toHtml(interfaceInfo.responsibility))%>
            </TD>
        </TR>        
        <TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plInterfaceView.Assignedto")%></TD>
        	<TD class="CellBGR3">
         	<%
         		for (int k = 0; k < devList.size(); k++) {
	         		PlanInterfacesInfo planInterfacesInfo = (PlanInterfacesInfo)devList.elementAt(k);
	         		long devID = planInterfacesInfo.getDeveloperId();
	         		if (devID == interfaceInfo.assignedID ) {
			%>
	        	<%=planInterfacesInfo.getDeveloperName()%>
         	<%
	         			break;
         			}
         		}
         	%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.plInterfaceView.Communication")%></TD>
            <TD class="CellBGR3">
   	        	<%=((interfaceInfo.communication == null) ? "N/A" : ConvertString.toHtml(interfaceInfo.communication))%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.plInterfaceView.TelFaxEmail")%></TD>
            <TD class="CellBGR3">
            	<%=((interfaceInfo.contact == null) ? "N/A" : ConvertString.toHtml(interfaceInfo.contact))%>
            </TD>
        </TR>    
    </TBODY>
</TABLE>

</form>

<br>

<form name="frm_plInterfaceDelete" action="Fms1Servlet#Interfaces" method="get">
	<input type="hidden" name="reqType" value="<%=Constants.PL_INTERFACE_DELETE%>" >
	<input type="hidden" name="plInterface_ID" value="<%=interfaceInfo.interfaceID%>">
</form>

<%
	if(right == 3 && !isArchive) {
%>
		<input type="button" name="update" value="<%=languageChoose.getMessage("fi.jsp.plInterfaceView.Update")%>" onclick="javascript:on_Submit1();" class="BUTTON">
		<input type="button" name="delete" value="<%=languageChoose.getMessage("fi.jsp.plInterfaceView.Delete")%>"  onclick="javascript:on_Submit2();"  class="BUTTON">
<%
	}
%>
		<input type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.plInterfaceView.Back")%>" class="BUTTON" onclick="doIt(<%=Constants.PL_STRUCTURE_GET_PAGE%>);">

<script language = "javascript">
	function on_Submit1() {
  	 	document.frm_plInterfaceUpdatePrep.submit();
	}
	function on_Submit2() {
		if (window.confirm("<%=languageChoose.getMessage("fi.jsp.plInterfaceView.Areyousure")%>") != 0) {
			document.frm_plInterfaceDelete.submit();
		} else {
			return;
		}		
	}
</script>

</BODY>
</HTML>