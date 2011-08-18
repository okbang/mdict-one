<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*, java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>futherWorkAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtItem.focus();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Furtherwork")%> </p>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_FURTHER_WORK%>#furtherwork">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Addnewfurtherwork")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Item")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="50" type="text" maxlength="50" name="txtItem"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Resulttobedone")%>* </TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtResult"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Timetododay")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtTime" class="numberTextBox"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Responsibility")%>* </TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtResponsibility"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Note")%> </TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtNote"></TEXTAREA></TD>
        </TR>        
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.OK")%>" class="BUTTON" onclick="doAction(this)"> <INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Cancel")%>" onclick="doAction(this)"></FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnOk") {
  	
  		if(frm.txtItem.value==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Thisfieldismandatory")%>");
  		 	frm.txtItem.focus();
  		 	return;
  		}
  		if(frm.txtItem.value.length>50){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.ItemLengthCanNotGreaterThan50")%>");
  		  	frm.txtItem.focus(); 
  		  	return;
  	  	}
  		if(frm.txtResult.value==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Thisfieldismandatory")%>");
  		 	frm.txtResult.focus();
  		 	return;
  		}
  		if(frm.txtResult.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Lengthofthisfieldmustbelessthan200")%>");
  		  	frm.txtResult.focus(); 
  		  	return;
  	  	}
  		
  		if(frm.txtTime.value==""){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Thisfieldismandatory")%>");
  				frm.txtTime.focus();  		
  				return;  
  		}
  	  	
  	  	if(frm.txtTime.value!="")
  	  	{
  	 	 	if(isNaN(frm.txtTime.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Invalidnumberformat")%>");
  				frm.txtTime.focus();  		
  				return;  
  			}	
 
  	 	 	if(frm.txtTime.value<=0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Mustbegreaterthan0")%>");
  				frm.txtTime.focus();  		
  				return;  
  			}	
  		}	
  		
  		if(frm.txtResponsibility.value==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Thisfieldismandatory")%>");
  		 	frm.txtResponsibility.focus();
  		 	return;
  		}
  	 	
  	 	if(frm.txtResponsibility.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Lengthofthisfieldmustbelessthan200")%>");
  		  	frm.txtResponsibility.focus(); 
  		  	return;
  	  	}
  	  	if(frm.txtNote.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.furtherWorkAdd.Lengthofthisfieldmustbelessthan200")%>");
  		  	frm.txtNote.focus(); 
  		  	return;
  	  	}
  	  	
  	  	frm.submit();  		 	
  	}
  	if (button.name=="btnCancel") {
  		doIt(<%=Constants.GET_POST_MORTEM%>);
  		return;
  	} 	
  	
  }
 </SCRIPT>
</BODY>
</HTML>

