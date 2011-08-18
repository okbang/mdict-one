<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>practice.jsp</TITLE>
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
	int right = Security.securiPage("Practice and Lessons",request,response);
	Vector vt=(Vector)session.getAttribute("practiceVector");
%>
<BODY onload="loadPrjMenu('Lessons')" class="BD">
<p class="TITLE"><%= languageChoose.getMessage("fi.jsp.practice.PracticesAndLessons") %></p>
<FORM action="practiceAdd.jsp" method="Post" name="frm">
<TABLE class="Table" cellspacing="1" width="95%">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.practice.Practiceandlessonlist")%></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD><%=languageChoose.getMessage("fi.jsp.practice.ScenarioProblem")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.practice.Type")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.practice.PracticeLessonSuggestion")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.practice.Category")%></TD>
        </TR>
        <%
        boolean bl=true;
        String rowStyle="";
        String typeStr="";
        String scen="";
        String prac="";
        PracticeInfo pracInfo=null;
        for(int i=0;i<vt.size();i++)
        {
        	rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  			bl=!bl;
        	typeStr=languageChoose.getMessage("fi.jsp.practice.Lesson");
        	pracInfo=(PracticeInfo)vt.get(i);
        	if(pracInfo.type==1) typeStr=languageChoose.getMessage("fi.jsp.practice.Practice");
        	if(pracInfo.type==2) typeStr=languageChoose.getMessage("fi.jsp.practice.Suggestion");
        	scen= pracInfo.scenario;
        	prac=pracInfo.practice;
        	if(scen.length()>50) scen=scen.substring(0,50)+"...";
        	if(prac.length()>50) prac=prac.substring(0,50)+"...";
        %>
        <TR class="<%=rowStyle%>">
            <TD align="center"><%=i+1%></TD>
            <TD><A href="Fms1Servlet?reqType=<%=Constants.GET_PRACTICE+"&vtID="+i%>"><%=ConvertString.toHtml(scen)%></A></TD>
            <TD><%=ConvertString.toHtml(typeStr)%></TD>
            <TD><%=ConvertString.toHtml(prac)%></TD>
            <TD><%=ConvertString.toHtml(languageChoose.getMessage(pracInfo.category))%></TD>
        </TR>
        <%}%>
    </TBODY>
</TABLE>
<%if (right==3 && !isArchive){%>
<P align="left"><INPUT type="submit" class="BUTTON" name="btnAdd" value="<%= languageChoose.getMessage("fi.jsp.practice.Addnew") %>"></P>
<%}%>
</FORM>
</BODY>
</HTML>
