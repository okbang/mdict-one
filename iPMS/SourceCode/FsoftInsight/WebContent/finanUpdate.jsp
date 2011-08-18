<%@page import="com.fms1.infoclass.*, com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>finanUpdate.jsp</TITLE>

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
	if(caller==Constants.SCHEDULE_CALLER)//called from project plan
		title= languageChoose.getMessage("fi.jsp.FinanUpdate.ScheduleFinance");
	else
		title=languageChoose.getMessage("fi.jsp.FinanUpdate.ProjectplanFinance");

	Vector finanVt=(Vector)session.getAttribute("finanVector");
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	FinancialInfo finanInfo=(FinancialInfo)finanVt.get(vtID);
	String dueD="";
    String actualD="";
    String value="";
	String note="";
	if(finanInfo.condition!=null){
    	note=finanInfo.condition;
    }
    if(finanInfo.value>0){
    	value=CommonTools.formatDouble(finanInfo.value);
    }
    if(finanInfo.actualD!=null){       
        actualD=CommonTools.dateFormat(finanInfo.actualD);
    }
    if(finanInfo.dueD!=null){        		
      dueD=CommonTools.dateFormat(finanInfo.dueD);
    }

%>
<BODY class="BD" onLoad="loadPrjMenu();frm.txtItem.focus();">
<P class="TITLE"><%=title%></p>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.UPDATE_FINAN %>">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.FinanUpdate.Updatefinancialplan")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanUpdate.Item")%>*<INPUT size="20" type="hidden" name="txtFinan" value="<%=finanInfo.finanID%>"></TD>
            <TD class="CellBGRnews"><INPUT size="50" type="text" maxlength="50" name="txtItem" value='<%=finanInfo.item%>'></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanUpdate.Duedate")%>*</TD>
            <TD class="CellBGRnews" nowrap="nowrap"><INPUT size="9" type="text" maxlength="9" name="txtDueD" value="<%=dueD%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showDueD()'>            
            	DD-MMM-YY </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanUpdate.Actualenddate")%></TD>
            <TD class="CellBGRnews" nowrap="nowrap"><INPUT size="9" type="text" maxlength="9" name="txtActualD" value="<%=actualD%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showActualD()'>            
            	DD-MMM-YY </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanUpdate.Amount")%>  (USD)* </TD>
            <TD class="CellBGRnews"><INPUT size="11" type="text" maxlength="11" name="txtValue" value="<%=value%>" class="numberTextBox"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.FinanUpdate.Note")%></TD>
            <TD class="CellBGRnews"><TEXTAREA rows="4" cols="50" name="txtCondition"><%=note%></TEXTAREA></TD>
        </TR>        
    </TBODY>
</TABLE>
<BR>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.FinanUpdate.OK")%>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.FinanUpdate.Cancel")%>" onclick="doAction(this)">
<INPUT type="hidden" name="vtIDstr" value="<%=vtIDstr%>">
</FORM>
<SCRIPT language="javascript">
  function doAction(button)
  {
  	if (button.name=="btnOk") {
  		if (mandatoryFld(frm.txtItem,"<%=languageChoose.getMessage("fi.jsp.FinanUpdate.Item1")%>"))
  		if (maxLength(frm.txtItem,"<%=languageChoose.getMessage("fi.jsp.FinanUpdate.Item2")%>",50))
  		if (mandatoryDateFld(frm.txtDueD,"<%=languageChoose.getMessage("fi.jsp.FinanUpdate.Duedate1")%>"))
		if (mandatoryFld(frm.txtValue,"<%=languageChoose.getMessage("fi.jsp.FinanUpdate.Amount1")%>"))
		if (positiveFld(frm.txtValue,"<%=languageChoose.getMessage("fi.jsp.FinanUpdate.Amount2")%>"))
		if (beforeTodayFld(frm.txtActualD,"<%=languageChoose.getMessage("fi.jsp.FinanUpdate.Actualenddate1")%>")) 		 
		if (maxLength(frm.txtCondition,"<%=languageChoose.getMessage("fi.jsp.FinanUpdate.Note1")%>",200))
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

