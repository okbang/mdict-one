<%@page import="com.fms1.tools.*"%> 
<%@page contentType="text/html;charset=UTF-8"%><%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8;">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>practiceUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	PracticeInfo pracInfo=(PracticeInfo)session.getAttribute("practiceInfo");
	Vector psVt=(Vector)session.getAttribute("processVector");
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtScenario.focus();">
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.practiceUpdate.PracticesAndLessons")%></p>
<FORM name="frm" method="post" action="Fms1Servlet?reqType=<%=Constants.UPDATE_PRACTICE%>">
<TABLE cellspacing="1" class="Table" width="640">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.practiceUpdate.Updatepracticeandlesson")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceUpdate.ScenarioProblem")%>*<INPUT size="20" type="hidden" maxlength="20" name="txtPracticeID" value="<%=pracInfo.practiceId%>"></TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="txtScenario"><%=pracInfo.scenario%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceUpdate.PracticeLessonSuggestion")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="txtPractice"><%=pracInfo.practice%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceUpdate.Type")%></TD>
            <TD class="CellBGR3"><SELECT name="cmbType" class="COMBO">
                <OPTION value="0" <%if (pracInfo.type==0) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.practiceUpdate.Lesson")%> </OPTION>
                <OPTION value="1" <%if (pracInfo.type==1) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.practiceUpdate.Practice")%> </OPTION>
                <OPTION value="2" <%if (pracInfo.type==2) {%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.practiceUpdate.Suggestion")%> </OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.practiceUpdate.Category")%></TD>
            <TD class="CellBGR3"><SELECT name="cmbCategory" class="COMBO">
                <%for(int i=0;i<psVt.size();i++){
           			ProcessInfo psInfo=(ProcessInfo)psVt.get(i);
           		%><OPTION value="<%=psInfo.name%>" <%if (pracInfo.category.compareTo(psInfo.name)==0){%>selected <%}%>><%=languageChoose.getMessage(psInfo.name)%></OPTION>
                <%}%>
            </SELECT></TD>
        </TR>
    </TBODY>
</TABLE>
<P align="left"><INPUT type="button" class="BUTTON" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.practiceUpdate.OK") %>" onclick="update();">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.practiceUpdate.Cancel") %>" onclick="doIt(<%=Constants.GET_PRACTICE_LIST%>);"></P>
</FORM>
<SCRIPT language="javascript">
  function update()
  {
  	  if(frm.txtScenario.value==0){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.practiceUpdate.YoumustinputScenario") %>");
  			frm.txtScenario.focus();
  			return;
  	  }	  	
  	  if(frm.txtScenario.value.length>500){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.practiceUpdate.MaxlengforScenariois500") %>");
  			frm.txtScenario.focus();
  			return;
  	  }	
  	  if(frm.txtPractice.value==0){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.practiceUpdate.YoumustinputPractice") %>");
  			frm.txtPractice.focus();
  			return;
  	  }	  
  	  if(frm.txtPractice.value.length>500){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.practiceUpdate.MaxlengforPracticeis500") %>");
  			frm.txtPractice.focus();
  			return;
  	  }	  	  
  	  frm.submit();
  	
  }
 </SCRIPT> 
</BODY>
</HTML>
