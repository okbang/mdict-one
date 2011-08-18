<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*, com.fms1.web.*,java.util.*,java.io.*,java.text.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>plDependencyView.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
%>
<BODY onload="loadPrjMenu()" class = "BD">
<%
    int caller=Integer.parseInt(session.getAttribute("caller").toString());
	int right = 1;
	String title;
	if(caller==Constants.SCHEDULE_CALLER){
		right = Security.securiPage("Schedule",request,response);
		title= languageChoose.getMessage("fi.jsp.plDependencyView.ScheduleCriticalDependencies");
	}
	else{	
		right = Security.securiPage("Project plan",request,response);		
		title= languageChoose.getMessage("fi.jsp.plDependencyView.ProjectplanCriticalDependencies");
	}

DependencyInfo depInfo = (DependencyInfo)session.getAttribute("plDependencyInfo");

%>
<form name="frm_plDependencyUpdatePrep" action="plDependencyUpdate.jsp" method = "get">
<input type = "hidden" name="plDependency_depID" value="<%=depInfo.dependencyID%>">

<P class="TITLE"><%= title%></P>

<br>
<TABLE cellspacing="1" class="Table" width = "90%">

<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plDependencyView.Dependencyinfo")%> </CAPTION>
    <TBODY>
        
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.Dependency")%> </TD>
            <TD class="CellBGR3">
            <%
            	char cr = 13;
            	char lf = 10;
            	
            	StringTokenizer strToken = new StringTokenizer( ((depInfo.item == null)? "" : depInfo.item), ""+cr+lf );
            	String noteString = "";
            	while(strToken.hasMoreElements()){
            		noteString += strToken.nextToken();
            		noteString += "<br>";
            	}
            	
 %>
            <%=((noteString.equals(""))? "N/A" : noteString)%>

            
           </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.Expecteddeliverydate")%> </TD>
            <TD class="CellBGR3"><%=((depInfo.plannedDeliveryDate == null)? "N/A" : CommonTools.dateFormat(new java.util.Date(depInfo.plannedDeliveryDate.getTime())))%></TD>
        </TR>
         <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plDependencyView.Actualdeliverydate")%> </TD>
            <TD class="CellBGR3"><%=((depInfo.actualDeliveryDate == null)? "N/A" : CommonTools.dateFormat(new java.util.Date(depInfo.actualDeliveryDate.getTime())))%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plDependencyView.Note")%> </TD>
            <TD class="CellBGR3">
            
            <%
            	cr = 13;
            	lf = 10;
            	
            	strToken = new StringTokenizer( ((depInfo.note == null)? "" : depInfo.note), ""+cr+lf );
            	noteString = "";
            	while(strToken.hasMoreElements()){
            		noteString += strToken.nextToken();
            		noteString += "<br>";
            	}
            	
 %>
            <%=((noteString.equals(""))? "N/A" : noteString)%>

            </TD>
        </TR>
        
    </TBODY>
</TABLE>

<br>
</form>
<form name="frm_plDependencyDelete" action="Fms1Servlet#dependencies" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.PL_DEPENDENCY_DELETE %>">
<input type = "hidden" name="plDependency_depID" value="<%=depInfo.dependencyID%>">
</form>

<%
if(right == 3 && !isArchive){
%>
		<INPUT type="button" name="update2" value=" <%=languageChoose.getMessage("fi.jsp.plDependencyView.Update")%> " onclick="javascript:on_Submit1();" class="BUTTON">
		<input type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.plDependencyView.Delete")%> "  onclick="javascript:on_Submit2();"  class="BUTTON">
<%
}
%>
<input type="button" name="Back" value=" <%=languageChoose.getMessage("fi.jsp.plDependencyView.Back")%> "    class="BUTTON" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_CRITICAL_DEPENDENCIES_GET_LIST:Constants.PL_DELIVERIES_DEPENDENCIES_GET_PAGE%>);">

</BODY>
</HTML>
<script language = "javascript">
	function on_Submit1()
	{
	 	document.frm_plDependencyUpdatePrep.submit();
	}
	function on_Submit2()
	{
		if (window.confirm("<%= languageChoose.getMessage("fi.jsp.plDependencyView.Areyousure")%>") != 0) {
			document.frm_plDependencyDelete.submit();
		}
		else{
			return;
		}	
		
	}		
</script> 
