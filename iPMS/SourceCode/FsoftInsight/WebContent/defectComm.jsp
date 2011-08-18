<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>defectComm.jsp</TITLE>
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
	int right = Security.securiPage("Defects",request,response); 
	Vector cdVt=(Vector)session.getAttribute("vtCommDefect");
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.defectComm.DefectPreventionCommondefects")%> </p>

<P align="right"><B><A href="Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=Group/CommonDefects.jsp" target="Common defects"> <%=languageChoose.getMessage("fi.jsp.defectComm.ExporttoExcel")%> </A></B></P>
<FORM method="POST" name="frm">
<TABLE class="Table" width="99%" cellspacing="1">
    <CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.defectComm.Commondefects")%> </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectComm.Commondefect")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectComm.Commdef.Code")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectComm.DPCode")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectComm.Defecttype")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectComm.RootCause")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.defectComm.CauseCategory")%> </TD>
        </TR>
        <%
        	boolean bl = true;
        	String rowStyle = "";
        	for(int i=0;i<cdVt.size();i++){
       			rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				CommDefInfo info = (CommDefInfo) cdVt.get(i);
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A href="cdefUpdate.jsp?vtID=<%=i%>"><%=info.commdef%></A></TD>
            <TD NOWRAP><%=info.commonDefCode%></TD>
            <TD NOWRAP><%=(info.dpcode == null)?"N/A": info.dpcode%></TD>
            <TD NOWRAP>
            <%
            	switch (info.defecttype) {
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.defectComm.01FunctionalityOther")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.defectComm.02UserInterface")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.defectComm.03Performance")%> <%break;
            	case 4: %> <%=languageChoose.getMessage("fi.jsp.defectComm.04Designissue")%> <%break;
            	case 5: %> <%=languageChoose.getMessage("fi.jsp.defectComm.05Codingstandard")%> <%break;
            	case 6: %> <%=languageChoose.getMessage("fi.jsp.defectComm.06Document")%> <%break;
            	case 7: %> <%=languageChoose.getMessage("fi.jsp.defectComm.07DataDatabaseintegrity")%> <%break;
            	case 8: %> <%=languageChoose.getMessage("fi.jsp.defectComm.08SecurityAccessControl")%> <%break;
            	case 9: %> <%=languageChoose.getMessage("fi.jsp.defectComm.09Portability")%> <%break;
            	case 10: %> <%=languageChoose.getMessage("fi.jsp.defectComm.10Other")%> <%break;
            	case 11: %> <%=languageChoose.getMessage("fi.jsp.defectComm.11Tools")%> <%break;
            	case 12: %> <%=languageChoose.getMessage("fi.jsp.defectComm.011Reqmisunderstanding")%> <%break;
            	case 13: %> <%=languageChoose.getMessage("fi.jsp.defectComm.012Featuremissing")%> <%break;
            	case 14: %> <%=languageChoose.getMessage("fi.jsp.defectComm.013Codinglogic")%> <%break;
            	case 15: %> <%=languageChoose.getMessage("fi.jsp.defectComm.014Businesslogic")%> <%break;
            	}
            %>
            </TD>
            <TD><%=(info.rootcause == null)? "N/A": info.rootcause%></TD>
            <TD>
            <%
            	switch (info.causecate) {
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.defectComm.Training")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.defectComm.Communication")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.defectComm.Oversight")%> <%break;
            	case 4: %> <%=languageChoose.getMessage("fi.jsp.defectComm.Understanding")%> <%break;
            	case 5: %> <%=languageChoose.getMessage("fi.jsp.defectComm.Other")%> <%break;
            	}
            %>
            </TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<%if (right == 3 && !isArchive){%>
<P><INPUT type="button" class="BUTTON" name="btnAddCD" value=" <%=languageChoose.getMessage("fi.jsp.defectComm.Addnew")%> " onclick="cdefAdd();"></P>
<%}%>

<SCRIPT language="javascript">
	function cdefAdd(){
		frm.action="cdefAdd.jsp";
		frm.submit();
	}
</SCRIPT> 
</BODY>
</HTML>
