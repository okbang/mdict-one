<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<LINK rel="stylesheet" type="text/css" href="stylesheet/fms.css">
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>plProjSchedAdd.jsp</TITLE>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	int maxRow = ProjectSchedule.NUMBER_OF_ROW_ADDABLE;
	boolean isOver = false;
	int schedDisplayed;
	String rowStyle;	
	
	ProjSchedInfo schedInfo = null;	
	
	Vector vErrSched = (Vector) request.getAttribute("ErrProjectScheduleList");
	if (vErrSched != null) {
		isOver = true;
		nRow = vErrSched.size();
		request.removeAttribute("ErrProjectScheduleList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
	
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	
%>
<BODY onLoad="loadPrjMenu();document.frmSchedAdd.sched_type[0].focus();" class="BD">

<form name ="frmSchedAdd" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.ProjectPlanProjectSchedule")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.AddNewProjectScheduleItems")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="3%" align = "center">#</TD>
			<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.Type")%>*</TD>
			<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.Activity")%>*</TD>
			<TD width="12%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.StartDate")%>*</TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.Responsible")%></TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.Note")%></TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (isOver)  schedInfo = (ProjSchedInfo) vErrSched.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="sched_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD width="3%" align = "center"><%=row + 1%></TD>
			<TD height="57">
				<SELECT name="sched_type" class="COMBO">
					<OPTION value = "0" >&nbsp;</OPTION>
					<OPTION value = "1" <%=(isOver && schedInfo.schedType == 1) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.DefectPreventionType")%></OPTION>
					<OPTION value = "2" <%=(isOver && schedInfo.schedType == 2) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.QualityControlType")%></OPTION>
					<OPTION value = "3" <%=(isOver && schedInfo.schedType == 3) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.ProjectTrackingType")%></OPTION>
					<OPTION value = "4" <%=(isOver && schedInfo.schedType == 4) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.ConfigurationManagementType")%></OPTION>
					<OPTION value = "5" <%=(isOver && schedInfo.schedType == 5) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.QAType")%></OPTION>
					<OPTION value = "6" <%=(isOver && schedInfo.schedType == 6) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.DARType")%></OPTION>
					<OPTION value = "7" <%=(isOver && schedInfo.schedType == 7) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.OtherType")%></OPTION>
				</SELECT>
			</TD>
			<TD height="57">
			 	<TEXTAREA style="width:100%;height:100%" name="sched_activity"><%= isOver? ConvertString.toHtml(schedInfo.schedActivity):""%></TEXTAREA> 
			</TD>
			<TD height="57">
				<INPUT type ="text" size = "9" maxlength ="9" name="sched_startDate" class="SmallTextbox" value="<%=isOver? CommonTools.dateFormat(schedInfo.schedStartDate):""%>"/>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showStartDate(<%=row%>)"/><BR/>
				<%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.DateFormatDDMMMYY")%>
			</TD>
			<TD height="57">
				<TEXTAREA style="width:100%;height:100%" name="sched_responsible"><%=isOver? ConvertString.toHtml(schedInfo.schedResponsible):""%></TEXTAREA> 
			</TD>
			<TD height="57">
				<TEXTAREA style="width:100%;height:100%" name="sched_note"><%=isOver? ConvertString.toHtml(schedInfo.schedNote):""%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	schedDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="sched_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD width="3%" align = "center"> <%=row + 1%> </TD>
			<TD height="57">
				<SELECT name="sched_type" class="COMBO">
					<OPTION value = "0">&nbsp;</OPTION>
					<OPTION value = "1"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.DefectPreventionType")%></OPTION>
					<OPTION value = "2"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.QualityControlType")%></OPTION>
					<OPTION value = "3"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.ProjectTrackingType")%></OPTION>
					<OPTION value = "4"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.ConfigurationManagementType")%></OPTION>
					<OPTION value = "5"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.QAType")%></OPTION>
					<OPTION value = "6"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.DARType")%></OPTION>
					<OPTION value = "7"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.OtherType")%></OPTION>
				</SELECT>
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="sched_activity"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <INPUT type ="text" size = "9" maxlength ="9" name="sched_startDate" class="SmallTextbox" value=""/>
			 <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showStartDate(<%=row%>)";/><BR/>
			 <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.DateFormatDDMMMYY")%>
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="sched_responsible"></TEXTAREA> 
			</TD>
			<TD height="57">
			 <TEXTAREA style="width:100%;height:100%" name="sched_note"></TEXTAREA> 
			</TD>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="sched_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.AddMoreProjectScheduleItem")%> </a></p>
<BR>
<input type ="hidden" name = "reqType"/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.OK")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plProjSchedAdd.Cancel")%>" class="BUTTON" onclick="doCancel();">
<INPUT type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">
</form>

<SCRIPT language="JavaScript">

var sched_nextHiddenIndex = <%=schedDisplayed + 1%>;
function addMoreRow() {
     getFormElement("sched_row" + sched_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 sched_nextHiddenIndex++;
	 if(sched_nextHiddenIndex > <%=maxRow%>)
	     getFormElement("sched_addMoreLink").style.display = "none";
	
}
init();
function init(){
   if(sched_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("sched_addMoreLink").style.display = "none";    
}

function showStartDate(i){
	var start_date = document.getElementsByName("sched_startDate")[i];
	if(start_date.value == null || start_date.value ==""){			
		//frmSchedAdd.sched_startDate[i].value =  javascript:dateFormat(new Date(),"dd-mmm-yy");
		start_date.value =  "01-Jan-09";
	}
	showCalendar(start_date, start_date, "dd-mmm-yy",null,1,-1,-1,true);
}

function addSubmit(){	
	if (checkValid()) {
		frmSchedAdd.reqType.value=<%=Constants.PL_PROJECT_SCHEDULE_ADD%>;
		frmSchedAdd.submit();
	} else return false;	
}

function doCancel(){
	frmSchedAdd.reqType.value =<%=Constants.PL_PROJECT_SCHEDULE_GET_LIST%>;
	frmSchedAdd.submit();
}

function checkValid(){
	var arrTxt= document.getElementsByName("sched_type");
	var arrTxt1= document.getElementsByName("sched_activity");
	var arrTxt2= document.getElementsByName("sched_startDate");
	var arrTxt3= document.getElementsByName("sched_responsible");
	var arrTxt4= document.getElementsByName("sched_note");
	
	var length = sched_nextHiddenIndex-1;
	var checkAllBlank = 0;
	
	for(i=0; i < length;i++) {
		if  (   arrTxt[i].value =='0'
				&& trim(arrTxt1[i].value) =='' 
				&& trim(arrTxt2[i].value) =='' 
				&& trim(arrTxt3[i].value) ==''
				&& trim(arrTxt4[i].value) =='' 
			) 
		{
			checkAllBlank++;				
		} else {
			if (arrTxt[i].value =='0')  {
				alert("Project schedule type is mandatory");
				arrTxt[i].focus();
				return false;
			}
			
			if (trim(arrTxt1[i].value) =='')  {
				alert("Activity is mandatory");
				arrTxt1[i].focus();
				return false;
			}
			
			if (trim(arrTxt2[i].value) =='')  {
				alert("Start date is mandatory");
				arrTxt2[i].focus();
				return false;
			}
			
			if (!(isDate(trim(arrTxt2[i].value))) ){
				window.alert("<%= languageChoose.getMessage("fi.jsp.plProjSchedAdd.TheValueInsertedIsNotADate")%>");  			
	  		 	arrTxt2[i].focus();
	  		 	return false;
	  	 	}
	  	  	if(compareDate(arrTxt2[i].value,frmSchedAdd.txtPrjStartD.value)==1){
		  		window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.schedule.Start__date__~PARAM1_DATE~")%>",frmSchedAdd.txtPrjStartD.value)));
				arrTxt2[i].focus();
		  		return false;
		  	}
	  	  	if(compareDate(frmSchedAdd.txtPrjEndD.value, arrTxt2[i].value)==1){
		  		window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.schedule.End__date__~PARAM1_DATE~")%>",frmSchedAdd.txtPrjEndD.value)));
				arrTxt2[i].focus();
		  		return false;
		  	}
					
			if(!maxLength(arrTxt1[i],"Please input less than 600 characters",600))
			return false;
			if(!maxLength(arrTxt3[i],"Please input less than 600 characters",600))
			return false;
			if(!maxLength(arrTxt4[i],"Please input less than 600 characters",600))
			return false;
		}
	}
	
	if (checkAllBlank==length) {
		alert("Please input data");
		arrTxt[0].focus();
		return false;
	}
	return true;
}
</SCRIPT>
</BODY>
</HTML>
