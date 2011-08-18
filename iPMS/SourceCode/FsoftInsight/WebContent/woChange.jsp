<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woChange.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onload="loadPrjMenu()">
<%
	int right = Security.securiPage("Work Order",request,response);
	
	String change_source_page = (String) session.getAttribute("change_source_page");
	String title;
	if(change_source_page.equals("1")){ //MANU: called from project plan
		//right = Integer.parseInt((String)session.getAttribute("021"));
		title=languageChoose.getMessage("fi.jsp.woChange.ProjectplanChanges");
	}
	else { //MANU: called from Work order
		//right = Integer.parseInt((String)session.getAttribute("020"));
		title=languageChoose.getMessage("fi.jsp.woChange.WorkOrderChanges");
	}
	Vector changeList = (Vector) session.getAttribute("WOChangeList");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
	
	String action = "";
	int intAction;
	
%>
<P class="TITLE"><%=title%></P>
<br>
<TABLE cellspacing="1" class="Table" width="95%">
<TR class="ColumnLabel" >
	<TD width = "24" align = "center">#</TD>
	<TD> &nbsp;<%=languageChoose.getMessage("fi.jsp.woChange.Date")%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.woChange.Item")%> </TD>
	<TD> &nbsp;A*/M/D&nbsp; </TD>	
	<TD> &nbsp;<%=languageChoose.getMessage("fi.jsp.woChange.Change")%> </TD>
	<TD> &nbsp;<%=languageChoose.getMessage("fi.jsp.woChange.Reason")%> </TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.woChange.Version")%> </TD>
</TR>
<%
String className;
for(int i = 0; i < changeList.size(); i++){
	className=(i%2==0)?"CellBGRnews":"CellBGR3";
 	WOChangeInfo info = (WOChangeInfo) changeList.elementAt(i);
 	if (info.action == null) {
 		action = "";
 	} else {
 		intAction = Integer.parseInt(info.action);
 		switch (intAction) {
			case 1: action = "Added"; break;
			case 2: action = "Modified"; break;
			case 3: action = "Deleted"; break;
		}
	}
%>
<TR class="<%=className%>">
	<td  width = "24" align = "center"><%=i+1%></TD>
	<TD><%=((info.changeDate == null)? "N/A" : CommonTools.dateFormat(new java.util.Date(info.changeDate.getTime())))%></TD>
	<TD><%if(right == 3 && !isArchive){%><a href="Fms1Servlet?reqType=<%=Constants.WO_CHANGE_MNG%>&woChange_ID=<%=info.changeID%>"><%}%><%=((info.item == null)? "N/A" : info.item)%><%if(right == 3 && !isArchive){%></a><%}%></TD>
	
	<TD><%=action%></TD>
	<TD><%=((info.changes == null||info.changes.equals(""))? "N/A":ConvertString.toHtml(info.changes))%></TD>
	<TD><%=((info.reason == null||info.reason.equals(""))? "N/A" :ConvertString.toHtml(info.reason))%></TD>
	<TD><%=((info.version == null)? "N/A" : info.version)%></TD>
</TR>
<%}%>
</table>
<br>
<%if(right == 3 && !isArchive){%>
<form name="frm_changeAdd" action="woChangeAdd.jsp" method = "get">
	<input type = "submit" name="woChangeAdd" value=" <%=languageChoose.getMessage("fi.jsp.woChange.Addnew")%> " class = "BUTTON">				
</form>
<%}%>
</BODY>
</HTML>
