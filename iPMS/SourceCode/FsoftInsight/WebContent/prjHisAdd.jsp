<%@page import="com.fms1.infoclass.*, com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML> 
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>prjHisAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.prjHisAdd.ProjectHistory")%> </p>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_HIS%>#history">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.prjHisAdd.Editprojecthistory")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.prjHisAdd.Date")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtDate">(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.prjHisAdd.Event")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtEvent"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.prjHisAdd.Comments")%>*</TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtComment"></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<P>
<INPUT type="button" name="btnAdd" value="<%=languageChoose.getMessage("fi.jsp.prjHisAdd.Addnew") %>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.prjHisAdd.Cancel") %>" onclick="doIt(<%=Constants.GET_POST_MORTEM%>);">
</P>
</FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if(frm.txtDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.prjHisAdd.Thisfieldismandatory")%>");
  	 	frm.txtDate.focus();
  	 	return;
  	}
	if (!isDate(frm.txtDate.value)){
 		window.alert("<%= languageChoose.getMessage("fi.jsp.prjHisAdd.Thevalueinsertedisnotadate")%>");
  		frm.txtDate.focus();
  		return;
  	}
  	if(frm.txtEvent.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.prjHisAdd.Thisfieldismandatory")%>");
  	 	frm.txtEvent.focus();
  	 	return;
  	}
  	if(frm.txtComment.value==""){
 		window.alert("<%=languageChoose.getMessage("fi.jsp.prjHisAdd.Thisfieldismandatory")%>");
  		frm.txtComment.focus();
  		return;
  	}

	if(!beforeTodayFld(frm.txtDate,"<%= languageChoose.getMessage("fi.jsp.prjHisAdd.Historydate")%>"))
  		  return;
  	
  	frm.submit();
  }
 </SCRIPT> 
</BODY>
</HTML>
