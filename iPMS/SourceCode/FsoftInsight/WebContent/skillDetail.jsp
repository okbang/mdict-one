<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>skillDetail.jsp</TITLE>
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
%>
<BODY onload="loadPrjMenu()" class="BD">
<%

Vector skillList = (Vector) session.getAttribute("vtSkills");
String fullName = (String) session.getAttribute("assignmentName");
String assignmentId = (String) session.getAttribute("assignmentId");
SkillInfo info = null;

%>
<P class="TITLE"> <%=languageChoose.paramText(new String[]{"fi.jsp.skillDetail.Team__evaluation__~PARAM1_FULLNAME~", fullName})%></P>
<DIV align="left">
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class = "TableCaption"><A name="iteration"> <%=languageChoose.getMessage("fi.jsp.skillDetail.Skilllist")%> </A></CAPTION>

<TR class="ColumnLabel">
<TD width = "24" align = "center">#</TD>
<TD> <%=languageChoose.getMessage("fi.jsp.skillDetail.Process")%> </TD>
<TD> <%=languageChoose.getMessage("fi.jsp.skillDetail.SkillTechnology")%> </TD>
<TD> <%=languageChoose.getMessage("fi.jsp.skillDetail.Point")%> </TD>
<TD> <%=languageChoose.getMessage("fi.jsp.skillDetail.Comment")%> </TD>
</TR>
<%
int totalPoint = 0;
for(int i = 0; i < skillList.size(); i++)
{
	
	String className = "";
 	if (i%2==0) className="CellBGRnews";
 	else className = "CellBGR3";
 	
 	info  = (SkillInfo) skillList.elementAt(i);
 	
 	totalPoint = totalPoint + info.point;
%>
<input type=hidden name="size" value="<%=skillList.size()%>">
<TR>
<TD class="<%=className%>" width = "24" align = "center"><%=i+1%></TD>
<TD class="<%=className%>"><A href="skillUpdate.jsp?&vtID=<%=i%>"><%=languageChoose.getMessage(info.process)%></A></TD>
<TD class="<%=className%>"><%=info.skill%></TD>
<TD class="<%=className%>"><%=info.point%></TD>
<TD class="<%=className%>"><%=((info.skillComment == null)? "" : info.skillComment)%></TD>
</TR>
<%
}
%>
<TR class="TableLeft">
    <TD></TD>
    <TD><B> <%=languageChoose.getMessage("fi.jsp.skillDetail.Total")%> </B></TD>
    <TD></TD>
    <TD><B><%=totalPoint%><B></TD>
    <TD></TD>
</TR>
</TABLE>

<P>
<FORM name="frm" action="Fms1Servlet" method = "post">
<INPUT type = "hidden" name="assignmentId" value="<%=assignmentId%>">
</FORM>
<%if(!isArchive){%>
<INPUT type="button" class="BUTTON" value=" <%=languageChoose.getMessage("fi.jsp.skillDetail.Addnew")%> " class="BUTTON" onclick="doAddNew();">
<%}%>
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.skillDetail.Back")%>" onclick="doCancel();">
</P>


</BODY>
</HTML>

<script language="javascript">
<%if(!isArchive){%>	
function doAddNew()
{  	
	try {
		if (document.getElementsByName("size")[0].value==5) {
			 alert('Can not add more');
			 return;
		}
	}
	catch(error){
	}
	frm.action="Fms1Servlet?reqType=<%=Constants.TO_ADD_SKILL%>";
	frm.submit();	
}
<%}%>
function doCancel()
{  	
	frm.action="Fms1Servlet?reqType=<%=Constants.REFRESH_SKILL_LIST%>#teamevaluation";
	frm.submit();	
}
</script>
