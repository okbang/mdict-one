<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>Weekly effort</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%

	Vector vt=(Vector)session.getAttribute("weeklyEffortVector");
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	WeeklyEffortInfo info=(WeeklyEffortInfo)vt.get(vtID);
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtEstimated.focus();">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.EffortWeeklyeffort")%> </p>
<p><%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.Unlessspecifiedtheunit")%></p>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.UPDATE_WEEKLY_EFFORT%>#weekly">
<INPUT  type="hidden" name="txtWeeklyEffortID" value="<%=info.weeklyE_ID%>">
<INPUT  type="hidden" name="txtDate" value="<%=info.date.getTime()%>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.Weeklyeffortupdate")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.Startdate")%> </TD>
            <TD class="CellBGRnews"><%=CommonTools.dateFormat(info.date)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.Plannedeffort")%>* </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtEstimated" value="<%=Double.isNaN(info.estimatedE)?"":CommonTools.formatDouble(info.estimatedE)%>" class="numberTextBox">
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.Actualeffort")%> </TD>
            <TD class="CellBGRnews"><%=CommonTools.formatDouble(info.actualE)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.Deviation")%> </TD>
            <TD class="CellBGRnews"><%=CommonTools.formatDouble(info.deviation)%></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.OK")%>" class="BUTTON" onclick="doAction(this)"> <INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.Cancel")%>" onclick="doAction(this)">
</FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnOk") {
  	  	if(frm.txtEstimated.value!="")
  	  	{
  	 	 	if(isNaN(frm.txtEstimated.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.Invalidnumberformat")%>");
  				frm.txtEstimated.focus();
  				return;  
  			}	
 
  	 	 	if(frm.txtEstimated.value<0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.Mustbegreaterthan0")%>");
  				frm.txtEstimated.focus();  		
  				return;  
  			}	
  		}	
  		else {
  				window.alert("<%=languageChoose.getMessage("fi.jsp.weeklyEffortUpdate.Thisfieldismandatory")%>");
  				frm.txtEstimated.focus();  		
  				return;  
  			}	
  	  	frm.submit();  		 	
  	}
  	if (button.name=="btnCancel") {
  		doIt(<%=Constants.EFF_WEEKLY_GET_LIST%>);
  		
  		return;
  	} 	
}
 </SCRIPT> 
</BODY>
</HTML>

