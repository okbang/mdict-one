<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>plIterationUpdate.jsp</TITLE>

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>

<BODY class="BD" onLoad="loadPrjMenu(); document.frm_plIterationUpdate.txtBEndD.focus();">

<%
	IterationInfo iterationInfo = (IterationInfo)session.getAttribute("plIterationInfo");
	Vector milestoneList = (Vector)session.getAttribute("plMilestoneList");
	String pEndD = (iterationInfo.replanEndDate != null) ? CommonTools.dateFormat(iterationInfo.replanEndDate) : "";
	String aEndD = (iterationInfo.actualEndDate != null) ? CommonTools.dateFormat(iterationInfo.actualEndDate) : "";
	String bEndD = (iterationInfo.planEndDate != null) ? CommonTools.dateFormat(iterationInfo.planEndDate) : "";
	int caller = Integer.parseInt(session.getAttribute("caller").toString());
%>

<FORM name="frm_plIterationUpdate" action="Fms1Servlet#iteration" method="get">
<INPUT type="hidden" name="reqType" value="<%=Constants.PL_ITERATION_UPDATE%>">
<INPUT type="hidden" name="plIteration_ID" value="<%=iterationInfo.iterationID %>">
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Iterationobjectives")%></P>
<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Updateiteration")%></CAPTION>
    <TBODY>
		<TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Stage")%></TD>
            <TD class="CellBGR3">
	<% 
		if (milestoneList != null) {
			for (int i = 0; i < milestoneList.size(); i++) {
				MilestoneInfo milestoneInfo = (MilestoneInfo)milestoneList.elementAt(i);
				long milestoneID = milestoneInfo.getMilestoneId();
				if (iterationInfo.milestoneID == milestoneID) {
	%>	
		<%=milestoneInfo.getName()%>
	<%
				}
			}
		}
	%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Iteration")%></TD>
            <TD class="CellBGR3"><%=iterationInfo.iteration%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Plannedenddate")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtBEndD" value="<%=bEndD%>">
            	<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showBEndD()'>            	
            	(dd-MMM-yy)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Replannedenddate")%></TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtPEndD" value="<%=pEndD%>">
            	<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPEndD()'>
            	(dd-MMM-yy)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Actualenddate")%></TD>
			<TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtAEndD" value="<%=aEndD%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showAEndD()'>			
				(dd-MMM-yy)</TD>
        </TR>
		<TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Description")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA name="plIteration_description" rows="4" cols="50"><%=((iterationInfo.description == null) ? "" : iterationInfo.description)%></TEXTAREA></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Milestone")%>*</TD>
            <TD class="CellBGR3"><TEXTAREA name="plIteration_milestone" rows="4" cols="50"><%=((iterationInfo.milestone == null) ? "" : iterationInfo.milestone)%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
</FORM>

<BR>

<FORM name="frm_plIterationDelete" action="Fms1Servlet#iteration" method="get">
<INPUT type="hidden" name="reqType" value="<%=Constants.PL_ITERATION_DELETE%>">
<INPUT type="hidden" name="plIteration_ID" value="<%=iterationInfo.iterationID%>">
</FORM>
<INPUT type="button" name="update2" value="<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Update")%>" onclick="javascript:on_Submit1();" class="BUTTON">
<INPUT type="button" name="delete" value="<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Delete")%>" onclick="javascript:on_Submit2();" class="BUTTON">
<INPUT type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Cancel")%>" class="BUTTON" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_STAGE_GET_LIST:Constants.PL_LIFECYCLE_GET_PAGE%>);">

<script language="javascript">
	function on_Submit1() {
		if (trim(frm_plIterationUpdate.plIteration_description.value) == "") {
  	 		window.alert("<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.youmustenterdescription")%>");
  		 	frm_plIterationUpdate.plIteration_description.focus();
  		 	return;
  	 	}
		if (frm_plIterationUpdate.plIteration_description.value.length > 200) {
			alert("<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.thetextinthetextareaistoolong")%>");
			frm_plIterationUpdate.plIteration_description.focus();
			return;
		}
		if (trim(frm_plIterationUpdate.plIteration_milestone.value) == "") {
  	 		window.alert("<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.youmustentermilestone")%>");
  		 	frm_plIterationUpdate.plIteration_milestone.focus();
  		 	return;
  	 	}
  	 	if (frm_plIterationUpdate.plIteration_milestone.value.length > 200) {
			alert("<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.thetextinthetextareaistoolong")%>");
			frm_plIterationUpdate.plIteration_milestone.focus();
			return;
		}
	  	if (trim(frm_plIterationUpdate.txtBEndD.value) != "") {
		  	if (!(isDate(frm_plIterationUpdate.txtBEndD.value))) {
		  		window.alert("<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Invaliddateformat")%>");
		  		frm_plIterationUpdate.txtBEndD.focus();
		  		return;
		  	}
		} else {
		 	window.alert("<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.plannedenddateismandatory")%>");
  		 	frm_plIterationUpdate.txtBEndD.focus();
  		 	return;
  	 	}
	  	if (frm_plIterationUpdate.txtPEndD.value != "") {
	  	  	if (!(isDate(frm_plIterationUpdate.txtPEndD.value))) {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Invaliddateformat")%>");
	  			frm_plIterationUpdate.txtPEndD.focus();
	  			return;
	  		}
  		}
	  	if (frm_plIterationUpdate.txtAEndD.value != "") {
	  		if (!(isDate(frm_plIterationUpdate.txtAEndD.value))) {
		  		window.alert("<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Invaliddateformat")%>");
		  		frm_plIterationUpdate.txtAEndD.focus();
		  		return;
		  	}
		  	if (compareToToday(frm_plIterationUpdate.txtAEndD.value)> 0) {
  			 window.alert("<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Actualenddatemustbeinthepastortoday")%>");
  		 	 frm_plIterationUpdate.txtAEndD.focus();
  		 	 return;
			}
		}
		document.frm_plIterationUpdate.submit();
	}
	
	function on_Submit2() {
		if (window.confirm("<%=languageChoose.getMessage("fi.jsp.plIterationUpdate.Areyousure")%>") != 0) {
			document.frm_plIterationDelete.submit();
		} else {
			return;
		}
	}
	function showBEndD(){
		showCalendar(frm_plIterationUpdate.txtBEndD, frm_plIterationUpdate.txtBEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showPEndD(){
		showCalendar(frm_plIterationUpdate.txtPEndD, frm_plIterationUpdate.txtPEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showAEndD(){
		showCalendar(frm_plIterationUpdate.txtAEndD, frm_plIterationUpdate.txtAEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
</script>

</BODY>
</HTML>