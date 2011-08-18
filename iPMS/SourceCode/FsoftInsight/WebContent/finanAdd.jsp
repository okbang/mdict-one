<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>finanAdd.jsp</TITLE>

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	String title;
	int caller=Integer.parseInt(session.getAttribute("caller").toString());
	if(caller==Constants.SCHEDULE_CALLER) 
		title=languageChoose.getMessage("fi.jsp.FinanAdd.ScheduleFinance");
	else // called from project plan
		title=languageChoose.getMessage("fi.jsp.FinanAdd.ProjectplanFinance");
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtItem.focus();">
<P class="TITLE"><%=title%></p>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.ADDNEW_FINAN%>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.FinanAdd.Addnewfinancialplan")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanAdd.Item")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="20" type="text" maxlength="50" name="txtItem"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanAdd.Duedate")%>*</TD>
            <TD class="CellBGRnews" nowrap="nowrap"><INPUT size="9" type="text" maxlength="9" name="txtDueD">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showDueD()'>            
            	(DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanAdd.Actualenddate")%></TD>
            <TD class="CellBGRnews" nowrap="nowrap"><INPUT size="9" type="text" maxlength="9" name="txtActualD">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showActualD()'>
            	(DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanAdd.Amount")%> (USD)* </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtValue" class="numberTextBox"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanAdd.Note")%></TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtCondition"></TEXTAREA></TD>
        </TR>        
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.FinanAdd.OK")%>" class="BUTTON" onclick="doAction(this)"> <INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.FinanAdd.Cancel")%>" onclick="doAction(this)"></FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnOk") {
  	
  		if(trim(frm.txtItem.value)==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.FinanAdd.Thisfieldismandatory")%>");
  		 	frm.txtItem.focus();
  		 	return;
  		}
  		if(trim(frm.txtDueD.value)==""){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.FinanAdd.Thisfieldismandatory")%>");
  		 	frm.txtDueD.focus();
  		 	return;
  		}
  		
  		if(!(isDate(frm.txtDueD.value))){  	  
  		  	window.alert("<%=languageChoose.getMessage("fi.jsp.FinanAdd.Invaliddateformat")%>");
  		 	frm.txtDueD.focus();
  		 	return;
  	 	}
  	  	if(!beforeTodayFld(frm.txtActualD,"<%=languageChoose.getMessage("fi.jsp.finanAdd.Actualenddate1")%>")){
  		  		return;
  	  	}
  	  	if(frm.txtCondition.value.length>200){
  	  		window.alert("<%=languageChoose.getMessage("fi.jsp.FinanAdd.Lengthofthisfieldmustbelessthan200")%>");
  		  	frm.txtCondition.focus(); 
  		  	return;
  	  	}
  	  	if(trim(frm.txtValue.value)==""){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.FinanAdd.Thisfieldismandatory")%>");
  				frm.txtValue.focus();  		
  				return;  
  		}
  	  	
  	  	if(trim(frm.txtValue.value)!="")
  	  	{
  	 	 	if(isNaN(frm.txtValue.value)){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.FinanAdd.Invalidnumberformat")%>");
  				frm.txtValue.focus();  		
  				return;  
  			}	
 
  	 	 	if(frm.txtValue.value<=0){
  				window.alert("<%=languageChoose.getMessage("fi.jsp.FinanAdd.Mustbegreaterthan0")%>");
  				frm.txtValue.focus();  		
  				return;  
  			}	
  		}	
 		
  	  	
  	  	frm.submit();  		 	
  	}
  	if (button.name=="btnCancel") {
  		doIt(<%=(caller==Constants.SCHEDULE_CALLER) ?Constants.SCHE_FINANCIAL_PLAN_GET_LIST:Constants.GET_FINAN_LIST%>);
  		return;
  	} 	
  	
  }
	function showActualD(){
		showCalendar(frm.txtActualD, frm.txtActualD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showDueD(){
		showCalendar(frm.txtDueD, frm.txtDueD, "dd-mmm-yy",null,1,-1,-1,true);
	}
 </SCRIPT>
</BODY>
</HTML>

