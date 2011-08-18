<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>scheInformation.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
%>
<%int right = Security.securiPage("Schedule", request, response);
ScheduleHeaderInfo headerInfo =
	(ScheduleHeaderInfo) session.getAttribute("scheduleHeaderInfo");
String aStartD = "";
String aEndD = "";
String timeliness = "N/A";
String stageSchDev = "N/A";
String delSchDev = "N/A";

if (headerInfo.aStartD != null) {
	aStartD = CommonTools.dateFormat(headerInfo.aStartD);
}
if (headerInfo.aEndD != null) {
	aEndD = CommonTools.dateFormat(headerInfo.aEndD);
}
String pStartD = CommonTools.dateFormat(headerInfo.pStartD);
String pEndD = CommonTools.dateFormat(headerInfo.pEndD);
if (headerInfo.timeliness >= 0) {
	timeliness = CommonTools.formatDouble(headerInfo.timeliness);
}
if (headerInfo.stageSchDev != -1) {
	stageSchDev = CommonTools.formatDouble(headerInfo.stageSchDev);
}
if (headerInfo.delSchDev != -1) {
	delSchDev = CommonTools.formatDouble(headerInfo.delSchDev);
}
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.scheInformation.ScheduleInformation")%> </P>
<FORM action="Fms1Servlet?reqType=<%=Constants.UPDATE_SCHEDULE_HEADER%>"
	name="frm" method="post">
<TABLE class="Table" width="95%" cellspacing="1">
	<TBODY>
		<TR>
			<TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.scheInformation.Plannedstartdate")%></TD>
			<TD class="CellBGRnews"><%=pStartD%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.scheInformation.Plannedenddate")%></TD>
			<TD class="CellBGRnews"><%=pEndD%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.scheInformation.Remainingdays")%></TD>
			<TD class="CellBGRnews"><%=CommonTools.formatDouble(headerInfo.remainD)%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.scheInformation.Timeliness")%>(%)</TD>
			<TD class="CellBGRnews"><%=timeliness%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.scheInformation.Actualstartdate")%><%if (right == 3 && !isArchive) {%>*<%}%></TD>
			<TD class="CellBGRnews"><%if (right == 3 && !isArchive) {%>
				<INPUT size="9"	type="text" maxlength="9" name="txtAStartD" value="<%=aStartD%>"><%} else {%><%=((headerInfo.aStartD == null) ? "N/A" : aStartD)%><%}%>
	            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showAStartD()'> (dd-mmm-yy)
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.scheInformation.Actualenddate")%></TD>
			<TD class="CellBGRnews"><%if (right == 3 && !isArchive) {%>
				<INPUT size="9"	type="text" maxlength="9" name="txtAEndD" value="<%=aEndD%>"><%} else {%><%=((headerInfo.aEndD == null) ?"N/A" : aEndD)%><%}%>
	            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showAEndD()'> (dd-mmm-yy)
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.scheInformation.Stagescheduledeviation")%>(%)</TD>
			<TD class="CellBGRnews"><%=stageSchDev%></TD>
		</TR>
		<TR>
			<TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.scheInformation.Deliveryscheduledeviation")%>(%)</TD>
			<TD class="CellBGRnews"><%=delSchDev%></TD>
		</TR>

	</TBODY>
</TABLE>
<p><%if (right == 3 && !isArchive) {%><INPUT type="button" name="btnOk"
	value="<%=languageChoose.getMessage("fi.jsp.scheInformation.Update")%>" class="BUTTON" onclick="headerUpdate();"><%}%></p>
</FORM>
<SCRIPT language="javascript">
function showAStartD(){
		showCalendar(frm.txtAStartD, frm.txtAStartD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showAEndD(){
		showCalendar(frm.txtAEndD, frm.txtAEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
function headerUpdate()
{	
	if(frm.txtAStartD.value!=""){
 	  if(!(isDate(frm.txtAStartD.value))){  	  
 	  	window.alert("<%=languageChoose.getMessage("fi.jsp.scheInformation.Invaliddateformat")%>");
 	  	frm.txtAStartD.focus();
 	  	return;
 	  }
 	}
 	else{
	 	window.alert("<%=languageChoose.getMessage("fi.jsp.scheInformation.Thisfieldismandatory")%>");
	 	frm.txtAStartD.focus();
 		return;
 	}
 	
 	if (frm.txtAEndD.value!=""){
 		if(!(isDate(frm.txtAEndD.value))){
 			window.alert("<%=languageChoose.getMessage("fi.jsp.scheInformation.Invaliddateformat")%>");
 			frm.txtAEndD.focus();
 			return;
 		}
 	}
	if((frm.txtAStartD.value!="")&&(frm.txtAEndD.value!="")){
		if(compareDate(frm.txtAEndD.value,frm.txtAStartD.value)==1){
  			 window.alert("<%= languageChoose.getMessage("fi.jsp.scheInformation.Actualenddatemustbeafteractualstartdate")%>");
  			 frm.txtAEndD.focus();
  		 	 return;
		}
		if(compareToToday(frm.txtAEndD.value)>0){
  			 window.alert("<%= languageChoose.getMessage("fi.jsp.scheInformation.Actualenddatemustbeinthepast")%>");
  		 	 frm.txtAEndD.focus();
  		 	 return;
		}
	}
	frm.submit();	
}
</SCRIPT>
</BODY>
</HTML>
