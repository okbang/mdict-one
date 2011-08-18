<%@page import="com.fms1.tools.*"%> 
 <%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>C.I. Register</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
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
	int right =Security.securiPage("CI Tracking",request,response);
    Vector vectCIRegister = (Vector)session.getAttribute("CIRegister");
      
	if (vectCIRegister.size() > 16)
	{
%>

<BODY onload="loadPrjMenu();makeScrollableTable('tableCI',true,'auto')" class="BD">
<%
	} else {
%>
<BODY class="BD" onload="loadPrjMenu()">
<%
	}
%>

<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.CIRegister.CIRegister")%></P>
<P> <%=languageChoose.getMessage("fi.jsp.CIRegister.NoteOnlyproductswithplannedend")%> </P>
<TABLE class="Table" width="95%" cellspacing="1" id="tableCI">
<THEAD>
<TR class="ColumnLabel">
	<TD><%=languageChoose.getMessage("fi.jsp.CIRegister.Stage")%></TD>
    <TD><%=languageChoose.getMessage("fi.jsp.CIRegister.Workproduct")%></TD>
    <TD><%=languageChoose.getMessage("fi.jsp.CIRegister.Product")%></TD>
    <TD><%=languageChoose.getMessage("fi.jsp.CIRegister.Baseline")%></TD>
    <TD width="50" align="center"> <%=languageChoose.getMessage("fi.jsp.CIRegister.Dev")%> </TD>
    <TD width="50" align="center"> <%=languageChoose.getMessage("fi.jsp.CIRegister.Review")%> </TD>
    <TD width="50" align="center"> <%=languageChoose.getMessage("fi.jsp.CIRegister.Test")%> </TD>
    <TD width="50" align="center"> <%=languageChoose.getMessage("fi.jsp.CIRegister.Release")%> </TD>
    <TD width="150" align="center"><%=languageChoose.getMessage("fi.jsp.CIRegister.Note")%></TD>
</TR>
</THEAD>
<TBODY>
        <%
        
        if (vectCIRegister != null) {
                for (int i = 0; i < vectCIRegister.size(); i++) {
                        ModuleInfo objCIRegisterInfo = (ModuleInfo)vectCIRegister.elementAt(i);
                        String className ;
                        if (i%2 == 0) className = "CellBGRnews";
                        else className = "CellBGR3";
						%> 
				        <TR class="<%=className%>">
				            <TD><%=objCIRegisterInfo.stage%></TD>
				            <TD><%=languageChoose.getMessage(objCIRegisterInfo.wpName)%></TD>
				            <TD><%=objCIRegisterInfo.name%></TD>
				            <TD><%=((objCIRegisterInfo.baseline==null) ? "N/A" : objCIRegisterInfo.baseline)%></TD>
            				<TD align="center"><INPUT type="radio" <%if ( objCIRegisterInfo.baselineStatus ==1) {%>checked<%}%> name="GROUPRADIO<%=i%>" value="1" disabled></TD>
				            <TD align="center"><INPUT type="radio" <%if ( objCIRegisterInfo.baselineStatus ==2) {%>checked<%}%> name="GROUPRADIO<%=i%>" value="2" disabled></TD>
				            <TD align="center"><INPUT type="radio" <%if ( objCIRegisterInfo.baselineStatus ==3) {%>checked<%}%> name="GROUPRADIO<%=i%>" value="3" disabled></TD>
				            <TD align="center"><INPUT type="radio" <%if ( objCIRegisterInfo.baselineStatus ==4) {%>checked<%}%> name="GROUPRADIO<%=i%>" value="4" disabled></TD>
				            <TD ><%=((objCIRegisterInfo.baselineNote==null) ? "N/A" : objCIRegisterInfo.baselineNote)%></TD>
        </TR>
        				<%
                }
        }
		%>
    </TBODY>
</TABLE>
<% if ((right == 3)&& vectCIRegister.size()>0 && !isArchive){%>
<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.CIRegister.Update")%>" class="BUTTON"  onclick="doIt('<%=Constants.GET_PAGE%>&page=CIRegisterUpdate.jsp')"><BR>
<% }%>
</BODY>
</HTML>
