<%@page import="com.fms1.infoclass.*, com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML> 
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>prjHisDetails.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right = Security.securiPage("Project reports",request,response);
	final ProjectHisInfo projectHisInfo = (ProjectHisInfo) session.getAttribute("projectHisInfo");
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.prjHisDetails.ProjectHistory")%> </p>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.SAVE_HIS%>#history">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.prjHisDetails.Editprojecthistory")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.prjHisDetails.Date")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtDate" value="<%=CommonTools.dateFormat(projectHisInfo.eventDate)%>">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.prjHisDetails.Event")%>* </TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtEvent"><%=projectHisInfo.events%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.prjHisDetails.Comments")%>* </TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtComment"><%=projectHisInfo.comments%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<P>
<%
if(right == 3){
%>
<INPUT type="hidden" name="prjHisId" value="<%=projectHisInfo.projectHisId%>">
<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.prjHisDetails.Update")%>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.prjHisDetails.Delete")%>" onclick="doDelete();">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.prjHisDetails.Cancel")%>" onclick="doIt(<%=Constants.GET_POST_MORTEM%>);">
<%
}
else{
%>
<INPUT type="button" class="BUTTON" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.prjHisDetails.Back")%>" onclick="javascript:window.history.back();">
<%
}
%>
</P>
</FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if(frm.txtDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.prjHisDetails.Thisfieldismandatory")%>");
  	 	frm.txtDate.focus();
  	 	return;
  	}
	if (!isDate(frm.txtDate.value)){
 		window.alert("<%= languageChoose.getMessage("fi.jsp.prjHisDetails.Thevalueinsertedisnotadate")%>");
  		frm.txtDate.focus();
  		return;
  	}
  	if(frm.txtEvent.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.prjHisDetails.Thisfieldismandatory")%>");
  	 	frm.txtEvent.focus();
  	 	return;
  	}
  	if(frm.txtComment.value==""){
 		window.alert("<%=languageChoose.getMessage("fi.jsp.prjHisDetails.Thisfieldismandatory")%>");
  		frm.txtComment.focus();
  		return;
  	}

	if(!beforeTodayFld(frm.txtDate,'<%= languageChoose.getMessage("fi.jsp.prjHisDetails.Historydate")%>'))
  		  return;
  	
  	frm.submit();
  }
  	
  function doDelete()
  {  	
  		if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.prjHisDetails.Areyousuretodelete")%>")){   		
  			return;
  		}
  		frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_HIS%>#history";
  		frm.submit();	
  	
  }
 </SCRIPT> 
</BODY>
</HTML>
