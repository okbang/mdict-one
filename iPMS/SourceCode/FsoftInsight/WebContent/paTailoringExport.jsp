<%@ page contentType="application/vnd.ms-excel; charset=UTF-8" %>
<%@page import="com.fms1.common.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>paTailoringExport.jsp</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<STYLE type="text/css">
.header {
    FONT-WEIGHT: bold;
    BACKGROUND-COLOR: #C0C0C0;
}
.footer {
    FONT-WEIGHT: bold;
}
.Title {
	font-weight: bold;
	font-size: 14pt;
	margin-left: 0px;
	margin-top: 20px
}
</STYLE>
</HEAD>

<BODY>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>

<P class="Title"> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.ProcessAssetsTailoringDeviatio")%> <P>

<%
	response.addHeader("Content-Disposition", "attachment;filename=paTailoring.xls");
	Vector tailoringList = (Vector) session.getAttribute("vtTailoringDeviation");
	int j = 0;
%>

<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
    <TBODY>
        <TR class="header">
            <TD width = "24" align = "center"># </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.ProjectCode")%> </TD>
            <TD > <%=languageChoose.getMessage("fi.jsp.paTailoringExport.StageProcessDocument")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.AddedModifiedDeleted")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.Reason")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.Type")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.Category")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.Note")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.Year")%> </TD>
        </TR>
	
	<%
	j = tailoringList.size();
	
	for(int i = 0 ;i < j; i++)
	{
	 	TailoringDeviationInfo info = (TailoringDeviationInfo) tailoringList.elementAt(i);
	%>
	
	<TR>
		<TD width = "24" align = "center"><%=i+1%></TD>
		<TD><%=info.projectCode%></TD>
		<TD><%=((info.modification == null)?"N/A":ConvertString.toHtmlForExcel(info.modification))%></TD>
		<TD>
		
		<%
		switch(info.action)
		{
			case 1:%> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.Added")%> <%break; 
			case 2:%> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.Modified")%> <%break;
			case 3:%> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.Deleted")%> <%break; 
		}
		%>
		</TD>
		
		<TD><%=((info.reason == null)?"N/A":ConvertString.toHtmlForExcel(info.reason))%></TD>
		<TD>
		<%
			switch(info.type)
			{
				case 1:%> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.Tailoring")%> <%break; 
				case 2:%> <%=languageChoose.getMessage("fi.jsp.paTailoringExport.Deviation")%> <%break;	 
			}
			%>
		</TD>
		
		<TD><%=info.category == null ? "N/A" : info.category%></TD>
		<TD><%=info.note == null ? " " : info.note%></TD>
		<%
		if(info.start_date != null){
			String start_date = CommonTools.dateFormat(info.start_date);
		%>
		<TD><%="20" + start_date.substring(7,9)%></TD>
	</TR>
	<%}else{%>
		<TD></TD>
	</TR>
	<%
		}
	}
	%>
	
  </TBODY>
</TABLE>
</BODY>
</HTML>
