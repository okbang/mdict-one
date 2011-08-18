<%@page import="java.util.Vector,com.fms1.infoclass.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.common.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>paPracticeDetail.jsp</TITLE>
<SCRIPT language="JavaScript">
	function doOnload(){
		window.open("header.jsp","header");
		loadOrgMenu();
	}
	
</SCRIPT>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad="doOnload();" class="BD">
<%
	Vector practiceList = (Vector) session.getAttribute("vtPracticeList");
	
	final int vectorID = Integer.parseInt(request.getParameter("vtPracticeId"));
	
	PracticeInfo pracInfo = (PracticeInfo) practiceList.elementAt(vectorID);
	
	String strWuID = (String) session.getAttribute("wuID");
	String strLessonType = (String) session.getAttribute("strLessonType");
	String strLessonProcess = (String) session.getAttribute("strLessonProcess");
	
	String fromDate = (String) session.getAttribute("fromDate");
	String toDate = (String) session.getAttribute("toDate");
	
	long lWuID = 132; // FSOFT by default
	if (strWuID != null)
		lWuID = Long.parseLong(strWuID);
	
	if (strLessonProcess == null)
		strLessonProcess = "-1";
	
	if (strLessonType == null)
		strLessonType = "-1";
	
	ReportMonth rm = new ReportMonth();
	if (fromDate == null)
		fromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2,4);

	if (toDate == null)
		toDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2,4);
	
	String type=languageChoose.getMessage("fi.jsp.paPracticeDetail.Lesson");
	if(pracInfo.type==1) type=languageChoose.getMessage("fi.jsp.paPracticeDetail.Practice");
    if(pracInfo.type==2) type=languageChoose.getMessage("fi.jsp.paPracticeDetail.suggestion");
%>
<BODY onload="loadPrjMenu()" class="BD">
<p class="TITLE"> <%=languageChoose.getMessage("fi.jsp.paPracticeDetail.ProcessAssetsPracticesLessons")%> </p>
<FORM name="frm" action="Fms1Servlet" method="POST">
<TABLE cellspacing="1" class="Table">
    <COL span="1" width="100">
    <COL span="1" width="400">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.paPracticeDetail.Practiceandlessondetails")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.paPracticeDetail.ScenarioProblem")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(pracInfo.scenario)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.paPracticeDetail.PracticeLessonSuggestion")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(pracInfo.practice)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.paPracticeDetail.Type")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(type)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.paPracticeDetail.Category")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(pracInfo.category)%></TD>
        </TR>
    </TBODY>
</TABLE>
<P align="left">
<INPUT type="hidden" name="reqType">
<INPUT type = "hidden" name = "cboGroup" value="<%=strWuID%>">
<INPUT type = "hidden" name = "cboLessonType" value="<%=strLessonType%>">
<INPUT type = "hidden" name = "cboLessonProcess" value="<%=strLessonProcess%>">
<INPUT type = "hidden" name = "fromDate" value="<%=fromDate%>">
<INPUT type = "hidden" name = "toDate" value="<%=toDate%>">
<INPUT type = "button" name = "btnBack" value="<%=languageChoose.getMessage("fi.jsp.paPracticeDetail.Back")%>" onclick="onBack()" class="BUTTON">
</FORM>

<SCRIPT>
function onBack() {
	frm.reqType.value = "<%=Constants.PROASS_PRACTICE_LESSON%>";
	frm.submit();
}
</SCRIPT>

</BODY>
</HTML>
