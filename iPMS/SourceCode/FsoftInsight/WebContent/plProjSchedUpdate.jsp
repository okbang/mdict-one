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
<TITLE>plProjSchedUpdate.jsp</TITLE>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	boolean isOver = false;
	int schedDisplayed;
	String rowStyle;	
	
	ProjSchedInfo schedInfo = null;	
	
	Vector vSched = (Vector) session.getAttribute("SchedBatchUpdateList");	
	
	Vector vErrSched = (Vector) request.getAttribute("ErrProjectScheduleList");
	if (vErrSched != null) {
		isOver = true;
		request.removeAttribute("ErrProjectScheduleList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
	
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<form name ="frmSchedUpdate" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.ProjectPlanProjectSchedule")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Update datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table" width="95%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.UpdateProjectScheduleItems")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">			
			<TD width="3%" align = "center"> # </TD>
			<TD width="15%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.Type")%>*</TD>
			<TD width="20%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.Activity")%>*</TD>
			<TD width="12%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.StartDate")%>*</TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.Responsible")%></TD>
			<TD width="22%"> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.Note")%></TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	Vector vTemp = new Vector();
	if (isOver)  vTemp = vErrSched;
	else vTemp = vSched;
	nRow = vTemp.size();
	
	// Display current list (last updated data)
	for (; row < nRow; row++) {
		schedInfo = (ProjSchedInfo) vTemp.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="sched_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD align = "center" width = "3%"><%=row + 1%><INPUT type="hidden" name ="schedID" value ="<%=schedInfo.schedID%>"/></TD>
			<TD height="57">
				<SELECT name="sched_type" class="COMBO">
					<OPTION value = "0" >&nbsp;</OPTION>
					<OPTION value = "1" <%=(schedInfo.schedType == 1) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DefectPreventionType")%></OPTION>
					<OPTION value = "2" <%=(schedInfo.schedType == 2) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.QualityControlType")%></OPTION>
					<OPTION value = "3" <%=(schedInfo.schedType == 3) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.ProjectTrackingType")%></OPTION>
					<OPTION value = "4" <%=(schedInfo.schedType == 4) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.ConfigurationManagementType")%></OPTION>
					<OPTION value = "5" <%=(schedInfo.schedType == 5) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.QAType")%></OPTION>
					<OPTION value = "6" <%=(schedInfo.schedType == 6) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DARType")%></OPTION>
					<OPTION value = "7" <%=(schedInfo.schedType == 7) ? "selected":"" %>> <%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.OtherType")%></OPTION>
				</SELECT>
			</TD>
			<TD height="57">
			 	<TEXTAREA style="width:100%;height:100%"  name="sched_activity"><%=schedInfo.schedActivity==null?"":schedInfo.schedActivity%></TEXTAREA> 
			</TD>
			<TD height="57">
				<INPUT type ="text" size = "9" maxlength ="9" name="sched_startDate" class="SmallTextbox" value="<%=CommonTools.dateFormat(schedInfo.schedStartDate)%>"/>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showStartDate(<%=row%>)"/><BR/>
				<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DateFormatDDMMMYY")%>
			</TD>
			<TD height="57">
				<TEXTAREA style="width:100%;height:100%"  name="sched_responsible"><%=schedInfo.schedResponsible==null?"":schedInfo.schedResponsible%></TEXTAREA> 
			</TD>
			<TD height="57">
				<TEXTAREA style="width:100%;height:100%"  name="sched_note"><%=schedInfo.schedNote==null?"":schedInfo.schedNote%></TEXTAREA> 
			</TD>
		</TR>
<%	
	}
	schedDisplayed = row;	// Indicate numbers of lines displayed
%>
	</TBODY>
</TABLE>
<P/>
<input type ="hidden" name = "reqType"/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.OK")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.Cancel")%>" class="BUTTON" onclick="doCancel();">
<INPUT type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">
</form>

<SCRIPT language="JavaScript">

var sched_nextHiddenIndex = <%=schedDisplayed + 1%>;

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
		frmSchedUpdate.reqType.value=<%=Constants.PL_PROJECT_SCHEDULE_UPDATE%>;
		frmSchedUpdate.submit();
	} else return false;	
}

function doCancel(){
	frmSchedUpdate.reqType.value =<%=Constants.PL_PROJECT_SCHEDULE_GET_LIST%>;
	frmSchedUpdate.submit();
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
				window.alert("<%= languageChoose.getMessage("fi.jsp.plProjSchedUpdate.TheValueInsertedIsNotADate")%>");  			
	  		 	arrTxt2[i].focus();
	  		 	return;
	  	 	}
			
			if(compareDate(arrTxt2[i].value,frmSchedUpdate.txtPrjStartD.value)==1){
		  		window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.schedule.Start__date__~PARAM1_DATE~")%>",frmSchedUpdate.txtPrjStartD.value)));
				arrTxt2[i].focus();
		  		return false;
		  	}
	  	  	if(compareDate(frmSchedUpdate.txtPrjEndD.value, arrTxt2[i].value)==1){
		  		window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.schedule.End__date__~PARAM1_DATE~")%>",frmSchedUpdate.txtPrjEndD.value)));
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
