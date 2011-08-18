<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>plDependencyUpdate.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class = "BD">
<%
DependencyInfo depInfo = (DependencyInfo)session.getAttribute("plDependencyInfo");
int caller=Integer.parseInt(session.getAttribute("caller").toString());
ProjectDateInfo prjDateInfo = (ProjectDateInfo)session.getAttribute("prjDateInfo");
String title;	
if(caller==Constants.SCHEDULE_CALLER)
	title=languageChoose.getMessage("fi.jsp.plDependencyUpdate.ScheduleCriticalDependencies");
else
	title= languageChoose.getMessage("fi.jsp.plDependencyUpdate.ProjectplanCriticalDependencies");
%>
<form name="frm_plDependencyUpdate" action="Fms1Servlet#dependencies" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.PL_DEPENDENCY_UPDATE%>">
<input type = "hidden" name="plDependency_depID" value="<%=depInfo.dependencyID%>">
<P class="TITLE"><%=title%></P>
<br>
<TABLE cellspacing="1" class="Table">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plDependencyUpdate.Updatecriticaldependency")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.Dependency")%>* </TD>
            <TD class="CellBGR3">
            <TEXTAREA name = "plDependency_item" cols = "50" rows = "4"><%=((depInfo.item == null)? "" : depInfo.item)%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plDeliverables_Dependencies.Expecteddeliverydate")%>* </TD>
            <TD class="CellBGR3" nowrap="nowrap"><input type="text" name="plDependency_plannedDate" value = "<%=((depInfo.plannedDeliveryDate == null)? "" : CommonTools.dateFormat(new java.util.Date(depInfo.plannedDeliveryDate.getTime())))%>" maxlength="9" size = "9">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPlDependency_plannedDate()'>            			            
            	(dd-MMM-yy)</TD>
        </TR>
         <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plDependencyUpdate.Actualdeliverydate")%> </TD>
            <TD class="CellBGR3" nowrap="nowrap"><input type="text" name="plDependency_actualDate" value = "<%=((depInfo.actualDeliveryDate == null)? "" : CommonTools.dateFormat(new java.util.Date(depInfo.actualDeliveryDate.getTime())))%>" maxlength="9" size = "9">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPlDependency_actualDate()'>            			            	
            	(dd-MMM-yy)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.plDependencyUpdate.Note")%> </TD>
            <TD class="CellBGR3">
            <TEXTAREA name = "plDependency_note" cols = "50" rows = "4"><%=((depInfo.note == null)? "" : depInfo.note)%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
</form>
<br>
<INPUT type="button" name="update2" value=" <%=languageChoose.getMessage("fi.jsp.plDependencyUpdate.OK")%> " onclick="javascript:on_Submit1();" class="BUTTON">
<input type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.plDependencyUpdate.Cancel")%> "    class="BUTTON" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_CRITICAL_DEPENDENCIES_GET_LIST:Constants.PL_DELIVERIES_DEPENDENCIES_GET_PAGE%>);">
</BODY>
</HTML>
<script language = "javascript">

	function on_Submit1()
	{
		if(trim(frm_plDependencyUpdate.plDependency_item.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyUpdate.Youmustenteritem")%>");
  		 	frm_plDependencyUpdate.plDependency_item.focus();
  		 	return;
  	 	}
  	 	if(frm_plDependencyUpdate.plDependency_item.value.length > 200){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyUpdate.Thetextinthetextareaistoolong")%>");
  		 	frm_plDependencyUpdate.plDependency_item.focus();
  		 	return;
  	 	}
		if((!(isDate(frm_plDependencyUpdate.plDependency_plannedDate.value))) && 
			 (trim(frm_plDependencyUpdate.plDependency_plannedDate.value) != "")){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyUpdate.thevalueinsertedisnotadate")%>");
  		 	frm_plDependencyUpdate.plDependency_plannedDate.focus();
  		 	return;
  	 	}
  	 	if(trim(frm_plDependencyUpdate.plDependency_plannedDate.value) == ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyUpdate.Youmustenterplanneddeliverydate")%>");
  		 	frm_plDependencyUpdate.plDependency_plannedDate.focus();
  		 	return;
  	 	}
  	 	<%if(prjDateInfo.plannedEndDate!=null){	%>
  	 		if(compareDate(frm_plDependencyUpdate.plDependency_plannedDate.value, "<%=CommonTools.dateFormat(prjDateInfo.plannedEndDate)%>") == -1){
  	 		window.alert("<%= languageChoose.paramText(new String[]{"fi.jsp.plDependencyUpdate.Planned__delivery__date__must__be__before__planned__end__date__of__project__~PARAM1_DATE~", CommonTools.dateFormat(prjDateInfo.plannedEndDate)})%>");
  	 		
  		 	frm_plDependencyUpdate.plDependency_plannedDate.focus();
  		 	return;
  	 		}
  	 		
  	 	<%}%>
  	 	
  	 	if((!isDate(frm_plDependencyUpdate.plDependency_actualDate.value)) && 
  	 		 trim(frm_plDependencyUpdate.plDependency_actualDate.value) != ""){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyUpdate.thevalueinsertedisnotadate")%>");
  		 	frm_plDependencyUpdate.plDependency_actualDate.focus();
  		 	return;
  	 	}
  	 	if(compareToToday(frm_plDependencyUpdate.plDependency_actualDate.value)>0){
  			 window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyUpdate.Actualdeliverydatemustbeinthepastortoday")%>");
  		 	 frm_plDependencyUpdate.plDependency_actualDate.focus();
  		 	 return;
		}
  	 	if(frm_plDependencyUpdate.plDependency_note.value.length > 200){  	  
  		  	window.alert("<%= languageChoose.getMessage("fi.jsp.plDependencyUpdate.Thetextinthetextareaistoolong")%>");
  		 	frm_plDependencyUpdate.plDependency_note.focus();
  		 	return;
  	 	}
  	 	frm_plDependencyUpdate.submit();
	}
	
	function showPlDependency_plannedDate(){
		showCalendar(frm_plDependencyUpdate.plDependency_plannedDate, frm_plDependencyUpdate.plDependency_plannedDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showPlDependency_actualDate(){
		showCalendar(frm_plDependencyUpdate.plDependency_actualDate, frm_plDependencyUpdate.plDependency_actualDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
</script> 
