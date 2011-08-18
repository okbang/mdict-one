<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,java.util.Vector" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<META http-equiv="Content-Style-Type" content="text/css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<LINK href="stylesheet/fms.css" rel="stylesheet"	type="text/css">
<TITLE>PCB Report</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
Vector prjList=(Vector)session.getAttribute("PCBProjectList");
boolean noProjects=(prjList.size()==0);

%>
<BODY class="BDPOPUP" onload="<%=CommonTools.getMnuFunc(session)%>">
<p class="TableCaption"><%=((noProjects)?languageChoose.getMessage("fi.jsp.pcbSelectProject.Noprojectsavailableinthisperiod"):languageChoose.getMessage("fi.jsp.pcbSelectProject.Pleaseselecttheprojectstoincludeinthereport"))%></P>
<TABLE>
<%
ProjectInfo prjInfo;
for (int i=0;i<prjList.size();i++){
	prjInfo=(ProjectInfo)prjList.elementAt(i);
%><TR class="NormalText">
	<TD><INPUT type="checkbox" name="chkProject" value="<%=prjInfo.getProjectId()%>" checked><TD>
	<TD><%=prjInfo.getProjectCode()%><TD>
	<TD><%=prjInfo.getProjectName()%><TD>
</TR>
<%}%>
</TABLE>

<%if (!noProjects) {%>
<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.pcbSelectProject.Ok")%> " OnClick="btnOk()">
<%}%>
<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.pcbSelectProject.Cancel")%> " OnClick="window.close()">
</BODY>
</HTML>
<SCRIPT language="javascript">
var matrixArr =new Array();
<%
for (int i=0;i<prjList.size();i++){
	prjInfo=(ProjectInfo)prjList.elementAt(i);
%>	matrixArr[<%=i%>]=[<%=prjInfo.getProjectId()%>,"<%=prjInfo.getProjectCode()%>","<%=prjInfo.getProjectName()%>"];
<%}%>

function btnOk()
{
	var checkedProjects =new Array();
	var j=0;
	if (matrixArr.length>1)
		for (var i=0;i<matrixArr.length;i++){
			if (chkProject[i].checked)
				checkedProjects[j++]=matrixArr[i];
		}
	else if (chkProject.checked)
		checkedProjects[0]=matrixArr[0];
	if (checkedProjects==null || checkedProjects.length ==0){
 		alert("<%= languageChoose.getMessage("fi.jsp.pcbSelectProject.PleaseCheckALeastOneProject")%>");  			
		return;
	}
	window.returnValue = checkedProjects;
	window.close();
}
</SCRIPT>
