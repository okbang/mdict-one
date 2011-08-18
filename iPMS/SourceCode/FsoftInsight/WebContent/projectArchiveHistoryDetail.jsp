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
<TITLE>projectArchiveHistoryDetail.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
    Vector vtPrjArchiveHisDetailInfo = (Vector)session.getAttribute("archiveHistoryDetail"); 
    String strClassStyle = "";
    int right = Security.securiPage("Project Archive",request,response); 
%>
<BODY  class="BD" onload="loadAdminMenu()">

<% if(vtPrjArchiveHisDetailInfo.size() == 2){ 
	ProjectInfo prjInfor = (ProjectInfo)vtPrjArchiveHisDetailInfo.elementAt(0);	
	Vector detailHisArchive = (Vector)vtPrjArchiveHisDetailInfo.elementAt(1);	
%>

<TABLE class="HDR">
    <TBODY>
        <TR>
            <TD width="60%"><%=languageChoose.getMessage("fi.jsp.projectHome.Projectcode")%></TD>
            <TD><%=(prjInfor.getProjectCode() == null)? "N/A" : prjInfor.getProjectCode()%></TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.projectHome.Group")%></TD>
            <TD><%=(prjInfor.getGroupName() == null)? "N/A" : prjInfor.getGroupName()%></TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.projectHome.Customer")%></TD>
            <TD><%=(prjInfor.getCustomer() == null)? "N/A" : prjInfor.getCustomer()%></TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.projectHome.ProjectMangage")%></TD>
            <TD><%=(prjInfor.getLeader() == null)? "N/A" : prjInfor.getLeader()%></TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.projectHome.ActualStartDate")%></TD>
            <TD><%=CommonTools.dateFormat(prjInfor.getStartDate())%></TD>
        </TR>
        <TR>
            <TD><%=languageChoose.getMessage("fi.jsp.projectHome.ActualCloseDate")%></TD>
            <TD><%=CommonTools.dateFormat(prjInfor.getActualFinishDate())%></TD>
        </TR>    
    </TBODY>    
</TABLE>
<BR>

<TABLE cellspacing="1" width="95%" class="Table">
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Date")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.Description")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.projectHome.ImplementedBy")%> </TD>
        </TR>
        <%
        	boolean bl=false;
        	for (int i = 0; i < detailHisArchive.size(); i++) 
        	{
            	ProjectArchiHisDetailInfo prjArcHisInfo = (ProjectArchiHisDetailInfo)detailHisArchive.elementAt(i);
            		strClassStyle =(bl)? "CellBGRnews":"CellBGR3";
            		bl=!bl;
        %>
        
        <TR class="<%=strClassStyle%>">
            <TD><%=prjArcHisInfo.effect_date_str%></TD>
            <TD><%=prjArcHisInfo.description%></TD>
            <TD><%=prjArcHisInfo.implement%></TD>
        </TR>
		<%
			}
		%>	
		<TR>
            <TD colspan="3" class="TableLeft" align='right'>&nbsp;
            </TD>
        </TR>
    </TBODY>
</TABLE>
<BR>		
<%if(right==3){%>
<%
	String goPage = "Fms1Servlet?reqType="+Constants.PROJECT_ARCHIVE_LIST;
	if(request.getParameter("prev")!= null){		
		String prevPage = (String)request.getParameter("prev");
		if(prevPage.equals("2")){
			goPage = "Fms1Servlet?reqType="+Constants.PROJECT_ARCHIVE_HISTORY;
		}
	}
%>
<%
	String archiveStatus = "1";
	if(prjInfor.getArchiveStatus() == 4){
		archiveStatus = "0";
	}
%>
<FORM action = "Fms1Servlet?reqType=<%=prjInfor.getArchiveStatus()==4?Constants.PROJECT_ARCHIVE_RESTORE:Constants.PROJECT_ARCHIVE_ARCHIVE%>&selectProject=<%=prjInfor.getProjectId()%>&archive=<%=archiveStatus%>" method="post" >
<%
	if (prjInfor.getArchiveStatus() == 4){
%>
<INPUT type="submit" value="<%=languageChoose.getMessage("fi.jsp.projectHome.Restore")%>" class="BUTTON"> &nbsp;
<%
	}else{
%>
<INPUT type="submit" value="<%=languageChoose.getMessage("fi.jsp.projectHome.Archive")%>" class="BUTTON"> &nbsp;
<%
	}
%>
<INPUT type="button" value="Back" onclick="window.location='<%=goPage%>'"  class="BUTTON">
</FORM>
<%}%>
<%
}
%>