<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" errorPage="error.jsp?error=Please re-login" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>projectArchiveHome.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
    Vector vtProjectInfo = (Vector)session.getAttribute("projectArchiveResult"); 
    String strClassStyle = "";
    String title = "Project archive result";
    if(request.getParameter("archive")!=null){
    	if(!((String)request.getParameter("archive")).equals("1")){
    		title = "Project restore result";
    	}
    }else{
    	title = "Project restore result";
    }
%>
<BODY  class="BD" onload="loadAdminMenu()">

<p class="TITLE"><%=title%></p>

<P></P>
<TABLE cellspacing="1" width="95%" class="Table">
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Projectcode")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Group1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Customer")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.ProjectMangage")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Status")%> </TD>          
        </TR>
        <%
        	boolean bl=false;
        	for (int i = 0; i < vtProjectInfo.size(); i++) 
        	{
            	ProjectArchiveResultState prjInfo = (ProjectArchiveResultState)vtProjectInfo.elementAt(i);
            		strClassStyle =(bl)? "CellBGRnews":"CellBGR3";
            		bl=!bl;
        %>
        
        <TR class="<%=strClassStyle%>">
            <TD><A href="Fms1Servlet?reqType=<%=Constants.PROJECT_ARCHIVE_HISTORY_DETAIL%>&workUnitID=<%= prjInfo.project_id %>"><%=prjInfo.project_code%></A></TD>
            <TD><%=prjInfo.group%></TD>
            <TD><%=CommonTools.formatString(prjInfo.customer)%></TD>
            <TD><%=prjInfo.leader%></TD>
      		<TD>
      			<%=prjInfo.resultStatus%>
      		</TD>
        </TR>
		<% 	}
		%>
		<TR>
            <TD colspan="5" class="TableLeft" align='left'>
            	&nbsp;
            </TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<%
	String goPage = "Fms1Servlet?reqType="+Constants.PROJECT_ARCHIVE_LIST;
	if(request.getParameter("prev")!=null){		
		String prevPage = (String)request.getParameter("prev");
		if(prevPage.equals("2")){
			goPage = "Fms1Servlet?reqType="+Constants.PROJECT_ARCHIVE_HISTORY;
		}	
	}
%>
<INPUT type="button" class="BUTTON" value="Back" onclick="window.location='<%=goPage%>'" >
</BODY>
</HTML>