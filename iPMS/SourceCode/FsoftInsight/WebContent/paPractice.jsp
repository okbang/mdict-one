<%@page contentType="text/html;charset=UTF-8"%><%@page import="java.util.Vector,com.fms1.infoclass.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.common.*" errorPage="error.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Content-Type" content="text/html;charset=UTF-8;">
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<TITLE>paPractice.jsp</TITLE>
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
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.paPractice.ProcessAssetsPracticesLessons")%> </P>

<%
	Vector vtPracticeList = (Vector) session.getAttribute("vtPracticeList");
	Vector vtProcess = (Vector) session.getAttribute("vtProcess");

	String strWuID = (String) session.getAttribute("wuID");
	String strLessonType = (String) session.getAttribute("strLessonType");
	String strLessonProcess = (String) session.getAttribute("strLessonProcess");

	Vector vtGroupList = (Vector) session.getAttribute("groupList");
	Vector vtOrgList = (Vector) session.getAttribute("orgList");
	
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
	
	int j = 0;

%>

<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.PROASS_PRACTICE_LESSON%>">

<TABLE width="100%" class="NormalText">
	<TR>
		<TD> <%=languageChoose.getMessage("fi.jsp.paPractice.Fromdate")%> </TD>
		<TD><INPUT type="text" name="fromDate" value="<%=fromDate%>" maxlength="9" size="9"> (DD-MMM-YY) </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.paPractice.Todate")%> </TD>
		<TD><INPUT type="text" name="toDate" value="<%=toDate%>" maxlength="9" size="9"> (DD-MMM-YY) </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.paPractice.Group")%> </TD>
		<TD>
			<SELECT name="cboGroup" class="COMBO">
				<%	
					j = vtOrgList.size();
					for (int i = 0; i < j; i++)
					{
						RolesInfo groupInfo = (RolesInfo) vtOrgList.elementAt(i);
				%>
			<OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
				<%
					}
				%>
				<%	
					j = vtGroupList.size();
					for (int i = 0; i < j; i++)
					{
						RolesInfo groupInfo = (RolesInfo) vtGroupList.elementAt(i);
				%>
			<OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
				<%
					}
				%>
			</SELECT>
		</TD>
		<TD></TD>
	</TR>
	<TR>
		<TD> <%=languageChoose.getMessage("fi.jsp.paPractice.Type")%> </TD>
		<TD>
			<SELECT name="cboLessonType" class="COMBO">
				<OPTION value="-1" <%=(strLessonType.equalsIgnoreCase("-1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paPractice.All")%> </OPTION>
				<OPTION value=0 <%=(strLessonType.equalsIgnoreCase("0") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paPractice.Lesson")%> </OPTION>
				<OPTION value=1 <%=(strLessonType.equalsIgnoreCase("1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paPractice.Practice")%> </OPTION>
				<OPTION value=2 <%=(strLessonType.equalsIgnoreCase("2") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paPractice.Suggestion")%> </OPTION>
			</SELECT>
		</TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.paPractice.Process")%>  </TD>
		<TD>
			<SELECT name="cboLessonProcess" class="COMBO">
			<OPTION value="-1"<%=(strLessonProcess.equalsIgnoreCase("-1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paPractice.All1")%> </OPTION>
				<%	
					j = vtProcess.size();
					for (int i = 0; i < j; i++)
					{
						ProcessInfo proInfo = (ProcessInfo) vtProcess.elementAt(i);
				%>
			            <OPTION value = "<%=proInfo.name%>" <%if(strLessonProcess.equals(proInfo.name)){%>selected<%}%>><%=languageChoose.getMessage(proInfo.name)%></OPTION>
				<%
					}
				%>
			</SELECT>
		</TD>
		<TD><INPUT type="button" name="btnView" value=" <%=languageChoose.getMessage("fi.jsp.paPractice.View")%> " class="BUTTON" onclick="doAction()"></TD>
		<TD><B><A href="Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=paPracticeExport.jsp" target="about:blank"> <%=languageChoose.getMessage("fi.jsp.paPractice.ExporttoExcel")%> </A></B></TD>
	</TR>
</TABLE>

</FORM>

<BR>

<TABLE cellspacing="1" class="Table" width="100%">
<CAPTION align="left" class="TableCaption"></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width = "24" align = "center"># </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPractice.Project")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPractice.ScenarioProblem")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPractice.PracticeLessonSuggestion")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paPractice.Process1")%> </TD>
        </TR>
	<%
	j = vtPracticeList.size();
	for(int i = 0 ;i < j; i++)
	{
		String className;
 		className=(i%2==0)?"CellBGR3":"CellBGRnews";
 		
	 	PracticeInfo info = (PracticeInfo) vtPracticeList.elementAt(i);
	%>
	
	<TR class="<%=className%>">
	<TD width = "24" align = "center"><%=i+1%></TD>
	<TD><A href="paPracticeDetail.jsp?vtPracticeId=<%=i%>"><%=info.projectCode%></A></TD>
	<TD><%=((info.scenario == null)?"N/A":ConvertString.toHtml(info.scenario))%></TD>
	<TD><%=((info.practice  == null)?"N/A":ConvertString.toHtml(info.practice))%></TD>
	<TD><%=languageChoose.getMessage(info.category)%></TD>
	</TR>
	
	<%
	}
	%>

</TABLE>
<SCRIPT language="JavaScript">
  function doAction()
  {
  	if(frm.fromDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.paPractice.Thisfieldismandatory")%>");
  	 	frm.fromDate.focus();
  	 	return;
  	}
	if (!isDate(frm.fromDate.value)){
 		window.alert("<%= languageChoose.getMessage("fi.jsp.paPractice.Invaliddatevalue")%>");
  		frm.fromDate.focus();
  		return;
  	}

  	if(frm.toDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.paPractice.Thisfieldismandatory")%>");
  	 	frm.toDate.focus();
  	 	return;
  	}
	if (!isDate(frm.toDate.value)){
 		window.alert("<%= languageChoose.getMessage("fi.jsp.paPractice.Invaliddatevalue")%>");
  		frm.toDate.focus();
  		return;
  	}

  	if(compareDate(frm.fromDate.value, frm.toDate.value) == -1)
  	{
  	 	window.alert("<%= languageChoose.getMessage("fi.jsp.paPractice.FromdatemustbebeforeTodate")%>");
  		frm.fromDate.focus();
  		return;
  	}
  	
  	frm.submit();
  }
var objToHide=new Array(frm.cboLessonType,frm.cboLessonProcess);
</SCRIPT >
</BODY>
</HTML>
