<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>plDependencyAdd.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu();frm_plDependencyAdd.plDependency_item.focus();">
<%

int caller=Integer.parseInt(session.getAttribute("caller").toString());
ProjectDateInfo prjDateInfo = (ProjectDateInfo)session.getAttribute("prjDateInfo");
String title;	
if(caller==Constants.SCHEDULE_CALLER)	
	title=languageChoose.getMessage("fi.jsp.plDependencyAdd.ScheduleCriticalDependencies");
else
	title= languageChoose.getMessage("fi.jsp.plDependencyAdd.ProjectplanCriticalDependencies");

%>
<form name="frm_plDependencyAdd" action="Fms1Servlet#dependencies" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.PL_DEPENDENCY_ADD%>">


<P class="TITLE"><%=title%></P>

<br>
<TABLE cellspacing="1" class="Table">

<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plDependencyAdd.Addnewcriticaldependency")%> </CAPTION> 
    <TBODY>
        
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.Dependency")%>* </TD>
            <TD class="CellBGR3">
            <TEXTAREA name = "plDependency_item" cols = "50" rows = "4"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.Expecteddeliverydate")%>* </TD>
            <TD class="CellBGR3"><input type="text" name="plDependency_plannedDate"  maxlength="10" size = "10">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPlannedDate()'>
            </TD>
        </TR>
         <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plDependencyAdd.Actualdeliverydate")%> </TD>
            <TD class="CellBGR3"><input type="text" name="plDependency_actualDate"  maxlength="10" size = "10">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showActualDate()'>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plDependencyAdd.Note")%> </TD>
            <TD class="CellBGR3">
            <TEXTAREA name = "plDependency_note" cols = "50" rows = "4"></TEXTAREA></TD>
        </TR>
        
    </TBODY>
</TABLE>
</form>
<br>
<INPUT type="button" name="update2" value=" <%=languageChoose.getMessage("fi.jsp.plDependencyAdd.OK")%> " onclick="on_Submit1();" class="BUTTON">
<input type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.plDependencyAdd.Cancel")%> " class="BUTTON" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_CRITICAL_DEPENDENCIES_GET_LIST:Constants.PL_DELIVERIES_DEPENDENCIES_GET_PAGE%>);">
</BODY>
<script language = "javascript">
	function showPlannedDate(){
		if(frm_plDependencyAdd.plDependency_plannedDate.value == null || frm_plDependencyAdd.plDependency_plannedDate.value ==""){
			frm_plDependencyAdd.plDependency_plannedDate.value = "01-01-08";
		}
		showCalendar(frm_plDependencyAdd.plDependency_plannedDate, frm_plDependencyAdd.plDependency_plannedDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showActualDate(){
		if(frm_plDependencyAdd.plDependency_actualDate.value == null || frm_plDependencyAdd.plDependency_actualDate.value ==""){
			frm_plDependencyAdd.plDependency_actualDate.value = "01-01-08";
		}
		showCalendar(frm_plDependencyAdd.plDependency_actualDate, frm_plDependencyAdd.plDependency_actualDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function on_Submit1()
	{
		if(trim(frm_plDependencyAdd.plDependency_item.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyAdd.Youmustenteritem")%>");
  		 	frm_plDependencyAdd.plDependency_item.focus();
  		 	return;
  	 	}
  	 	
  	 	if(frm_plDependencyAdd.plDependency_item.value.length > 200){
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyAdd.Thetextinthetextareaistoolong")%>");
  		 	frm_plDependencyAdd.plDependency_item.focus();
  		 	return;
  	 	}
  	 	
		if((!(isDate(frm_plDependencyAdd.plDependency_plannedDate.value))) && (trim(frm_plDependencyAdd.plDependency_plannedDate.value) != "")){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyAdd.thevalueinsertedisnotadate")%>");
  		 	frm_plDependencyAdd.plDependency_plannedDate.focus();
  		 	return;
  	 	}
  	 	if(trim(frm_plDependencyAdd.plDependency_plannedDate.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyAdd.Youmustenterplanneddeliverydate")%>");
  		 	frm_plDependencyAdd.plDependency_plannedDate.focus();
  		 	return;
  	 	}
  	 	<%if(prjDateInfo.plannedEndDate!=null){	%>
  	 		if(compareDate(frm_plDependencyAdd.plDependency_plannedDate.value, "<%=CommonTools.dateFormat(prjDateInfo.plannedEndDate)%>") == -1){
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyAdd.Planneddeliverydatemustbebeforeplannedenddateofproject")%>(<%=CommonTools.dateFormat(prjDateInfo.plannedEndDate)%>) !");
  		 	frm_plDependencyAdd.plDependency_plannedDate.focus();
  		 	return;
  	 		}
  	 		
  	 	<%}%>
  	 	if((!(isDate(frm_plDependencyAdd.plDependency_actualDate.value))) && (trim(frm_plDependencyAdd.plDependency_actualDate.value) != "")){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyAdd.thevalueinsertedisnotadate")%>");
  		 	frm_plDependencyAdd.plDependency_actualDate.focus();
  		 	return;
  	 	}
  	 	 if(compareToToday(frm_plDependencyAdd.plDependency_actualDate.value)>0){
  			 window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyAdd.Actualdeliverydatemustbeinthepastortoday")%>");
  		 	 frm_plDependencyAdd.plDependency_actualDate.focus();
  		 	 return;
		}
  	 	if(frm_plDependencyAdd.plDependency_note.value.length > 200){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyAdd.Thetextinthetextareaistoolong")%>");
  		 	frm_plDependencyAdd.plDependency_note.focus();
  		 	return;
  	 	}
  	 	frm_plDependencyAdd.submit();
	}
	
</script>
</HTML>
