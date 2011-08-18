<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>practiceAdd.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	String err=(String)session.getAttribute("practiceError");
	if(err!=null&&err!="")
	{
%>	<p class="ERROR"><%=err%></p>
<%	}
	session.setAttribute("practiceError","");
	Vector psVt=(Vector)session.getAttribute("processVector");	
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtScenario.focus();">
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.practiceAdd.PracticesAndLessons")%></p>
<FORM method="Post" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_PRACTICE%>" name="frm">
<TABLE cellspacing="1" class="Table" width="640">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.practiceAdd.Addnewpracticeandlesson")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceAdd.ScenarioProblem")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="txtScenario"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceAdd.PracticeLessonSuggestion")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="txtPractice"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceAdd.Type")%></TD>
            <TD class="CellBGR3"><SELECT name="cmbType" class="COMBO">
                <OPTION value="0" selected> <%=languageChoose.getMessage("fi.jsp.practiceAdd.Lesson")%> </OPTION>
                <OPTION value="1"> <%=languageChoose.getMessage("fi.jsp.practiceAdd.Practice")%> </OPTION>
                <OPTION value="2"> <%=languageChoose.getMessage("fi.jsp.practiceAdd.Suggestion")%> </OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceAdd.Categoty")%></TD>
            <TD class="CellBGR3"><SELECT name="cmbCategory" class="COMBO">
                <%for(int i=0;i<psVt.size();i++){
           			ProcessInfo psInfo=(ProcessInfo)psVt.get(i);
           		%><OPTION value="<%=psInfo.name%>"><%=languageChoose.getMessage(psInfo.name)%></OPTION>
                <%}%>
            </SELECT></TD>
        </TR>
    </TBODY>
</TABLE>
<P align="left"><INPUT type="button" class="BUTTON" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.practiceAdd.OK") %>"onclick="add();"> <INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.practiceAdd.Cancel") %>" onclick="doIt(<%=Constants.GET_PRACTICE_LIST%>);"></P>
</FORM>
<SCRIPT language="javascript">
  function add()
  {
  	  if(frm.txtScenario.value==0){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.practiceAdd.YoumustinputScenario")%>");
  			frm.txtScenario.focus();
  			return;
  	  }	  	
  	  if(frm.txtScenario.value.length>500){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.practiceAdd.MaxlengforScenariois500")%>");
  			frm.txtScenario.focus();
  			return;
  	  }	
  	  if(frm.txtPractice.value==0){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.practiceAdd.YoumustinputPractice")%>");
  			frm.txtPractice.focus();
  			return;
  	  }	  
  	  if(frm.txtPractice.value.length>500){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.practiceAdd.MaxlengforPracticeis500")%>");
  			frm.txtPractice.focus();
  			return;
  	  }	  	  
  	  frm.submit();	    	    	
  }
 </SCRIPT> 
</BODY>
</HTML>

