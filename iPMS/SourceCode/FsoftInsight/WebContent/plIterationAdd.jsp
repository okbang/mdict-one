<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plIterationAdd.jsp</TITLE>
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	Vector milestoneList = (Vector)session.getAttribute("plMilestoneList");
	int caller=Integer.parseInt(session.getAttribute("caller").toString());
%>
<FORM name="frm_plIterationAdd" action="Fms1Servlet#iteration" method="get">
<INPUT type="hidden" name="reqType" value="<%=Constants.PL_ITERATION_ADD%>">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plIterationAdd.Iterationobjective")%> </P>
<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.plIterationAdd.Addnewiteration")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plIterationAdd.Stage")%></TD>
            <TD class="CellBGR3">
            <SELECT name="plIteration_milestoneID" class="COMBO">
	<%
        if (milestoneList != null) {
			for (int i = 0; i < milestoneList.size(); i++) {
				MilestoneInfo milestoneInfo = (MilestoneInfo)milestoneList.elementAt(i);
	%>
				<OPTION value="<%=milestoneInfo.getMilestoneId()%>"><%=milestoneInfo.getName()%></OPTION>
	<%
			}
		}
	%>
            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.plIterationAdd.Plannedenddate")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtBEndD">
            	<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showBEndD()'>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.plIterationAdd.Replannedenddate")%></TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtPEndD">
            	<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPEndD()'>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.plIterationAdd.Actualenddate")%></TD>
			<TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtAEndD">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showAEndD()'>
			</TD>
        </TR>
		<TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plIterationAdd.Description")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA name="plIteration_description" rows="4" cols="50"></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plIterationAdd.Milestone")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA name="plIteration_milestone" rows="4" cols="50"></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
</FORM>

<BR>

<INPUT type="button" name="Add" value="<%=languageChoose.getMessage("fi.jsp.plIterationAdd.OK")%>" class="BUTTON" onclick="javascript:on_Submit()"> 
<INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.plIterationAdd.Cancel")%>" class="BUTTON" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_STAGE_GET_LIST:Constants.PL_LIFECYCLE_GET_PAGE%>);">

<script language="javascript">
	function showBEndD(){
		showCalendar(frm_plIterationAdd.txtBEndD, frm_plIterationAdd.txtBEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showPEndD(){
		showCalendar(frm_plIterationAdd.txtPEndD, frm_plIterationAdd.txtPEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showAEndD(){
		showCalendar(frm_plIterationAdd.txtAEndD, frm_plIterationAdd.txtAEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function on_Submit() {
		if (trim(frm_plIterationAdd.plIteration_description.value) == "") {
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plIterationAdd.youmustenterdescription")%>");
  		 	frm_plIterationAdd.plIteration_description.focus();
  		 	return;
  	 	}
		if (frm_plIterationAdd.plIteration_description.value.length > 200) {
			alert("<%= languageChoose.getMessage("fi.jsp.plIterationAdd.thetextinthetextareaistoolong")%>");
			frm_plIterationAdd.plIteration_description.focus();
			return;
		}
		if (trim(frm_plIterationAdd.plIteration_milestone.value) == "") {
  	 		window.alert("<%= languageChoose.getMessage("fi.jsp.plIterationAdd.youmustentermilestone")%>");
  		 	frm_plIterationAdd.plIteration_milestone.focus();
  		 	return;
  	 	}
		if (frm_plIterationAdd.plIteration_milestone.value.length > 200) {
			alert("<%= languageChoose.getMessage("fi.jsp.plIterationAdd.thetextinthetextareaistoolong")%>");
			frm_plIterationAdd.plIteration_milestone.focus();
			return;
		}
	  	if (trim(frm_plIterationAdd.txtBEndD.value) != "") {
		  	if (!(isDate(frm_plIterationAdd.txtBEndD.value))) {
		  		window.alert("<%=languageChoose.getMessage("fi.jsp.plIterationAdd.Invaliddateformat")%>");
		  		frm_plIterationAdd.txtBEndD.focus();
		  		return;
		  	}
		} else {
  	 		window.alert("<%=languageChoose.getMessage("fi.jsp.plIterationAdd.plannedenddateismandatory")%>");
  		 	frm_plIterationAdd.txtBEndD.focus();
  		 	return;
  	 	}
	  	if (frm_plIterationAdd.txtPEndD.value != "") {
	  	  	if (!(isDate(frm_plIterationAdd.txtPEndD.value))) {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.plIterationAdd.Invaliddateformat")%>");
	  			frm_plIterationAdd.txtPEndD.focus();
	  			return;
	  		}
  		}
	  	if (frm_plIterationAdd.txtAEndD.value != "") {
	  		if (!(isDate(frm_plIterationAdd.txtAEndD.value))) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.plIterationAdd.Invaliddateformat")%>");
				frm_plIterationAdd.txtAEndD.focus();
				return;
		  	}
		  	if (compareToToday(frm_plIterationAdd.txtAEndD.value) > 0) {
				window.alert("<%= languageChoose.getMessage("fi.jsp.plIterationAdd.Actualenddatemustbeinthepastortoday")%>");
				frm_plIterationAdd.txtAEndD.focus();
				return;
			}
		}
		document.frm_plIterationAdd.submit();
	}
</script>

</BODY>
</HTML>