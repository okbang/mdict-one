<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>scheCriticalDependencies.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
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
	int right = Security.securiPage("Schedule",request,response); 
	Vector dependencyList = (Vector) session.getAttribute("PLDependencyList");			
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.scheCriticalDependencies.ScheduleCriticaldependencies")%> </P>
<FORM method="post" action="Fms1Servlet?reqType=<%=Constants.UPDATE_SCHEDULE_HEADER%>" name="frm">
<TABLE cellspacing="1" class="Table" width="95%">
<TR class="ColumnLabel">
	<TD width = "24" align = "center">#</TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.scheCriticalDependencies.Item")%> </TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.scheCriticalDependencies.Planneddeliverydate")%> </TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.scheCriticalDependencies.Actualdeliverydate")%> </TD>
</TR>
<%
boolean bl=true;
String rowStyle="";

for(int i = 0; i < dependencyList.size(); i++){
	
 	DependencyInfo dependencyInfo = (DependencyInfo) dependencyList.get(i);
 	String item="N/A";
 	if(dependencyInfo.item!=null){
 		item=dependencyInfo.item;
 		if(item.length()>50){
 			item=item.substring(0,50)+"...";
 		}
 	}
 	
 	rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  	bl=!bl; 
%>
<TR class="<%=rowStyle%>">
	<TD width = "24" align = "center"><%=i+1%></TD>
	<TD><a href="Fms1Servlet?reqType=<%=Constants.PL_DEPENDENCY_VIEW%>&plDependency_depID=<%=dependencyInfo.dependencyID%>"><%=ConvertString.toHtml(item)%></a></TD>
	<TD><%=((dependencyInfo.plannedDeliveryDate == null)? "N/A" : CommonTools.dateFormat(new java.util.Date(dependencyInfo.plannedDeliveryDate.getTime())))%></TD>
	<TD><%=((dependencyInfo.actualDeliveryDate == null)? "N/A" : CommonTools.dateFormat(new java.util.Date(dependencyInfo.actualDeliveryDate.getTime())))%></TD>

</TR>
<%}%>
</TABLE>
<P><%if(right == 3 && !isArchive){%><INPUT type="button" name="add" value=" <%=languageChoose.getMessage("fi.jsp.scheCriticalDependencies.Addnew")%> " class="BUTTON"  onclick="dependencyAdd();"><%}%></P>
</form>
<SCRIPT language="javascript">
function dependencyAdd(){
	frm.action="plDependencyAdd.jsp";
	frm.submit();
}
</script>
</BODY>
</HTML>
