<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>practiceDetails.jsp</TITLE>
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
	PracticeInfo pracInfo=(PracticeInfo)session.getAttribute("practiceInfo");
	String type=languageChoose.getMessage("fi.jsp.practiceDetais.Lesson");
	if(pracInfo.type==1) type=languageChoose.getMessage("fi.jsp.practiceDetails.Practice");
    if(pracInfo.type==2) type=languageChoose.getMessage("fi.jsp.practiceDetails.Suggestion");
%>
<BODY onload="loadPrjMenu()" class="BD">
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.practiceDetais.PracticesAndLessons")%></p>
<FORM name="frm" method="Post">
<TABLE cellspacing="1" class="Table" width="95%">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.practiceDetails.Practiceandlessondetails")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceDetails.ScenarioProblem")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(pracInfo.scenario)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceDetails.PracticeLessonSuggestion")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(pracInfo.practice)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceDetails.Type")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(type)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceDetails.Category")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(languageChoose.getMessage(pracInfo.category))%></TD>
        </TR>
    </TBODY>
</TABLE>
<P align="left">
<%if (right==3 && !isArchive){%>
<INPUT type="button" class="BUTTON" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.practiceDetails.Update") %>" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.practiceDetails.Delete") %>" onclick="doAction(this)">
<%}%>
<INPUT type="button" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.practiceDetails.Back") %>" class="BUTTON" onclick="doIt(<%=Constants.GET_PRACTICE_LIST%>);"></P>
</FORM>
<%if (right==3 && !isArchive){%>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnUpdate") {
  		frm.action="practiceUpdate.jsp";
  		frm.submit();
  	}
  	if (button.name=="btnDelete"){
  	
  		if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.practiceDetails.Areyousuretodeletethisitem")%>")){  		
  			return;
  		}
  		frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_PRACTICE%>";
  		frm.submit();
  	}
  	
  }
 </SCRIPT> 
 <%}%>
</BODY>
</HTML>
