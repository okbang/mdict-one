<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<TITLE>pcbConclusion.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
int right = Security.securiPage("Organization PCB",request,response);
PCBGalInfo pcbGalInfo = (PCBGalInfo)session.getAttribute("pcbGalInfo");
int nWorkUnitType = Integer.parseInt((String)session.getAttribute("workUnitType"));
%>
<BODY class="BD" onload="<%=CommonTools.getMnuFunc(session)%>">
<FORM name="frmConclusions" method ="POST" action="Fms1Servlet?reqType=<%=Constants.PCB_SAVECONCLUSIONS%>">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.pcbConclusion.PCBreportConclusion")%> </P>
<TABLE class="Table" width="95%" cellspacing="1">
	<TBODY>
		<TR>
			<TD class="ColumnLabel" width="20%"> <%=languageChoose.getMessage("fi.jsp.pcbConclusion.Period")%> </TD>
			<TD class="CellBGRnews"> <%= languageChoose.paramText(new String[] {"fi.jsp.pcbConclusion.from__~PARAM1_START_DATE~__to__~PARAM2_END_DATE~", CommonTools.dateFormat(pcbGalInfo.startDate), CommonTools.dateFormat(pcbGalInfo.endDate)}) %> </TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbConclusion.Reporttype")%> </TD>
			<TD class="CellBGRnews"><%=pcbGalInfo.reportTypeComment%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbConclusion.Lastproblemsreview")%> </TD>
			<TD class="CellBGRnews"><%if (right==3){%><TEXTAREA rows="7" cols="70" name="txtLastProblemsReview"><%=((pcbGalInfo.lastProblemsReview==null)?"":pcbGalInfo.lastProblemsReview)%></TEXTAREA><%}else{%><%=((pcbGalInfo.lastProblemsReview==null)?"":ConvertString.toHtml(pcbGalInfo.lastProblemsReview))%><%}%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbConclusion.Lastsuggestionreview")%> </TD>
			<TD class="CellBGRnews"><%if (right==3){%><TEXTAREA rows="7" cols="70" name="txtLastSuggestionsReview"><%=((pcbGalInfo.lastSuggestionsReview==null)?"":pcbGalInfo.lastSuggestionsReview)%></TEXTAREA><%}else{%><%=((pcbGalInfo.lastSuggestionsReview==null)?"":ConvertString.toHtml(pcbGalInfo.lastSuggestionsReview))%><%}%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbConclusion.Problems")%> </TD>
			<TD class="CellBGRnews"><%if (right==3){%><TEXTAREA rows="7" cols="70" name="txtProblems"><%=((pcbGalInfo.problems==null)?"":pcbGalInfo.problems)%></TEXTAREA><%}else{%><%=((pcbGalInfo.problems==null)?"":ConvertString.toHtml(pcbGalInfo.problems))%><%}%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.pcbConclusion.Suggestions")%> </TD>
			<TD class="CellBGRnews"><%if (right==3){%><TEXTAREA rows="7" cols="70" name="txtSuggestions"><%=((pcbGalInfo.suggestions==null)?"":pcbGalInfo.suggestions)%></TEXTAREA><%}else{%><%=((pcbGalInfo.suggestions==null)?"":ConvertString.toHtml(pcbGalInfo.suggestions))%><%}%></TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<BR>
<%if (right==3){%>
<INPUT	type="button" name="btnUpdate" value=" <%=languageChoose.getMessage("fi.jsp.pcbConclusion.Save")%> " class="BUTTON" onClick="saveComments()">
<%}%>
<P>
</BODY>
<SCRIPT language="JavaScript">
function saveComments(){
	if (maxLength(frmConclusions.txtLastProblemsReview,"<%=languageChoose.getMessage("fi.jsp.pcbConclusion.Lastproblemsreview")%>",1000))
		if (maxLength(frmConclusions.txtLastSuggestionsReview,"<%=languageChoose.getMessage("fi.jsp.pcbConclusion.Lastsuggestionsreview")%>",1000))
			if (maxLength(frmConclusions.txtProblems,"<%=languageChoose.getMessage("fi.jsp.pcbConclusion.Problems")%>",1000))
				if (maxLength(frmConclusions.txtSuggestions,"<%=languageChoose.getMessage("fi.jsp.pcbConclusion.Suggestion")%>",1000))
					frmConclusions.submit();
}
</SCRIPT>
</HTML>
