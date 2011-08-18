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
<TITLE>woDeliveryBatchAdd.jsp</TITLE>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	int maxRow = ProjectSchedule.NUMBER_OF_ROW_ADDABLE;
	boolean isOver = false;
	int deliverDisplayed;
	String rowStyle;	
	
	ModuleInfo moduleInfo = null;	
	String note=null;
	String PlannedReleaseDate="";
	String ReplannedReleaseDate="";
	String ActualReleaseDate="";
	
	Vector moduleList = (Vector)session.getAttribute("woModuleList");
	Vector vErrDeliver = (Vector) request.getAttribute("ErrDeliveryBatchList");
	if (vErrDeliver != null) {
		isOver = true;
		nRow = vErrDeliver.size();
		request.removeAttribute("ErrDeliveryBatchList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
	
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	
%>
<BODY onLoad="loadPrjMenu();document.frmDeliveryBatchAdd.deliverable_ID[0].focus();" class="BD">

<form name ="frmDeliveryBatchAdd" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.WorkorderDeliverables")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Add datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Addnewdeliverable")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="24" align = "center"> # </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Deliverable")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Firstcommitteddate")%>* </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Lastcommitteddate")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Actualdate")%> <A name="star"></A></TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Status")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Note")%> </TD>
		</TR>		
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	for (; (row < nRow && row < maxRow); row++) {
		if (isOver)  moduleInfo = (ModuleInfo) vErrDeliver.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="deliver_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD align = "center" width = "24"><%=row + 1%></TD>
			<TD height="50">
			<SELECT name="deliverable_ID" class="COMBO" onchange="Selectvalue('<%=row%>');">
<%

			for(int i = 0; i < moduleList.size(); i++){
				ModuleInfo info = (ModuleInfo) moduleList.elementAt(i);
				if (i==0)
				{
					note=info.baselineNote;
					PlannedReleaseDate=(info.plannedReleaseDate==null)?"":CommonTools.dateFormat(info.plannedReleaseDate);
					ReplannedReleaseDate=(info.rePlannedReleaseDate==null)?"":CommonTools.dateFormat(info.rePlannedReleaseDate);
					ActualReleaseDate=(info.actualReleaseDate==null)?"":CommonTools.dateFormat(info.actualReleaseDate);
				}
%>				<OPTION value="<%=info.moduleID%>"><%=info.name%></OPTION>
<%			
			}
%>         
        	</SELECT>
			</TD>
			<!-- Comment by HaiMM
			<TD height="50">
			 	<TEXTAREA rows="4" cols="25" name="deliverable_location"><%=(isOver && moduleInfo.deliveryLocation !=null)? moduleInfo.deliveryLocation:""%></TEXTAREA> 
			</TD>
			-->
			<TD height="50">
				<INPUT type ="text" size = "9" maxlength ="9" name="txtCommitedDate" value="<%=isOver? CommonTools.dateUpdate(moduleInfo.plannedReleaseDate):PlannedReleaseDate%>"/>&nbsp;<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showDate1(<%=row%>)"/><BR/>
				<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DateFormatDDMMMYY")%>
			</TD>
			<TD height="50">
				<INPUT type ="text" size = "9" maxlength ="9" name="txtReCommitedDate" value="<%=isOver ? CommonTools.dateUpdate(moduleInfo.rePlannedReleaseDate) :ReplannedReleaseDate%>"/>&nbsp;<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showDate2(<%=row%>)"/><BR/>
				<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DateFormatDDMMMYY")%>
			</TD>
			<TD height="50">
				<INPUT type ="text" size = "9" maxlength ="9" name="txtActualDate" value="<%= isOver ? CommonTools.dateUpdate(moduleInfo.actualReleaseDate):ActualReleaseDate%>"/>&nbsp;<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showDate3(<%=row%>)"/><BR/>
				<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DateFormatDDMMMYY")%>
			</TD>
			<TD height="50">
				<SELECT name="cmbStatus" class="COMBO">                
	                <OPTION value="1"<%if(isOver && moduleInfo.status==1){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Pending")%> </OPTION>
	                <OPTION value="2"<%if(isOver && moduleInfo.status==2){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Accepted")%> </OPTION>
	                <OPTION value="3"<%if(isOver && moduleInfo.status==3){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Rejected")%> </OPTION>
	                <OPTION value="4"<%if(isOver && moduleInfo.status==4){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Cancelled")%> </OPTION>
	                <OPTION value="5"<%if(isOver && moduleInfo.status==5){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Other")%> </OPTION>
<%  if (isOver) {%>	                
	                <OPTION value="6"<%if(moduleInfo.status==6){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.InProgress")%> </OPTION>
<% 
	} else {
%>
					<OPTION value="6" selected><%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.InProgress")%> </OPTION>
<%	
	}
%>	               
	            </SELECT>
            </TD>
			<TD height="50">
				<TEXTAREA rows="4" cols="25" name="deliverable_note"><%= (isOver && moduleInfo.note!=null) ? moduleInfo.note:""%></TEXTAREA> 				
			</TD>
		</TR>
<%	
	}
	deliverDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>

		<TR id="deliver_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD align = "center" width = "24"><%=row + 1%></TD>
			<TD height="50">
			<SELECT name="deliverable_ID" class="COMBO" onchange="Selectvalue('<%=row%>');">
<%

			for(int i = 0; i < moduleList.size(); i++){
				ModuleInfo info = (ModuleInfo) moduleList.elementAt(i);
				if (i==0)
				{
					note=info.baselineNote;
					PlannedReleaseDate=(info.plannedReleaseDate==null)?"":CommonTools.dateFormat(info.plannedReleaseDate);
					ReplannedReleaseDate=(info.rePlannedReleaseDate==null)?"":CommonTools.dateFormat(info.rePlannedReleaseDate);
					ActualReleaseDate=(info.actualReleaseDate==null)?"":CommonTools.dateFormat(info.actualReleaseDate);
				}
%>				<OPTION value="<%=info.moduleID%>"><%=info.name%></OPTION>
<%}%>         
        	</SELECT>
			</TD>
			<!-- Comment by HaiMM
			<TD height="50">
			 	<TEXTAREA rows="4" cols="25"  name="deliverable_location"></TEXTAREA> 
			</TD>
			-->
			<TD height="50">
				<INPUT type ="text" size = "9" maxlength ="9" name="txtCommitedDate" value="<%=PlannedReleaseDate%>"/>&nbsp;<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showDate1(<%=row%>)"/><BR/>
				<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DateFormatDDMMMYY")%>
			</TD>
			<TD height="50">
				<INPUT type ="text" size = "9" maxlength ="9" name="txtReCommitedDate" value="<%=ReplannedReleaseDate%>"/>&nbsp;<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showDate2(<%=row%>)"/><BR/>
				<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DateFormatDDMMMYY")%>
			</TD>
			<TD height="50">
				<INPUT type ="text" size = "9" maxlength ="9" name="txtActualDate" value="<%=ActualReleaseDate%>"/>&nbsp;<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showDate3(<%=row%>)"/><BR/>
				<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DateFormatDDMMMYY")%>
			</TD>
			<TD height="50">
				<SELECT name="cmbStatus" class="COMBO">
	                <OPTION value="1"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Pending")%> </OPTION>
	                <OPTION value="2"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Accepted")%> </OPTION>
	                <OPTION value="3"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Rejected")%> </OPTION>
	                <OPTION value="4"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Cancelled")%> </OPTION>
	                <OPTION value="5"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Other")%> </OPTION>
	                <OPTION value="6" selected> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.InProgress")%> </OPTION>
	            </SELECT>
            </TD>
			<TD height="50">
				<TEXTAREA rows="4" cols="25" name="deliverable_note"></TEXTAREA> 				
			</TD>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="deliver_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.AddMoreDeliverables")%> </a></p>
<BR>
<input type ="hidden" name = "reqType"/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.OK")%>" class="BUTTON" onclick="addSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Cancel")%>" class="BUTTON" onclick="jumpURL('woDeliverable.jsp');">
<input type ="hidden" name ="vRow"/>
</form>

<SCRIPT language="JavaScript">

	var deliver_nextHiddenIndex = <%=deliverDisplayed + 1%>;
	var deliverableCombo= document.getElementsByName("deliverable_ID");
	function addMoreRow() {
	     getFormElement("deliver_row" + deliver_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	     deliver_nextHiddenIndex++;
		 if(deliver_nextHiddenIndex > <%=maxRow%>)
		     getFormElement("deliver_addMoreLink").style.display = "none";		 
		
	}
	init();
	function init(){
	   if(deliver_nextHiddenIndex > <%=maxRow%>) 
	       getFormElement("deliver_addMoreLink").style.display = "none";    
	}


	var myCombo = document.getElementsByName("cmbStatus");
	
	
	var txtPrjStartD="<%=(prjDateInfo.startD== null)?"":CommonTools.dateFormat(prjDateInfo.startD)%>";
	var txtPrjEndD="<%=(prjDateInfo.endD== null)?"":CommonTools.dateFormat(prjDateInfo.endD)%>";
	var reviewDate = new Array(<%=moduleList.size()%>);
	var testDate = new Array(<%=moduleList.size()%>);
	var releaseDate;
		
	arrmoduleID = new Array(<%=moduleList.size()%>);
	arrmoduleNote = new Array(<%=moduleList.size()%>);
	arrPReleaseDate= new Array(<%=moduleList.size()%>);
	arrRPReleaseDate= new Array(<%=moduleList.size()%>);
	arrAReleaseDate= new Array(<%=moduleList.size()%>);
	arrPReviewDate= new Array(<%=moduleList.size()%>);
	arrPTestDate= new Array(<%=moduleList.size()%>);
<%
for(int i=0;i<moduleList.size();i++){
		ModuleInfo info = (ModuleInfo) moduleList.elementAt(i);
		if (i==0)
		{%>	
		reviewDate[i]="<%=(CommonTools.dateFormat(info.plannedReviewDate).equals("N/A"))?"":CommonTools.dateFormat(info.plannedReviewDate)%>";
		testDate[i]="<%=(CommonTools.dateFormat(info.plannedTestEndDate).equals("N/A"))?"":CommonTools.dateFormat(info.plannedTestEndDate)%>";
		<%}%>
	
		arrmoduleID[<%=i%>] = "<%=info.moduleID%>";
		arrmoduleNote[<%=i%>] = "<%=(info.baselineNote==null?"":ConvertString.toJScript(info.baselineNote.trim()))%>";
		arrPReleaseDate[<%=i%>] = "<%=(CommonTools.dateFormat(info.plannedReleaseDate).equals("N/A"))?"":CommonTools.dateFormat(info.plannedReleaseDate)%>";
		arrRPReleaseDate[<%=i%>] = "<%=(CommonTools.dateFormat(info.rePlannedReleaseDate).equals("N/A"))?"":CommonTools.dateFormat(info.rePlannedReleaseDate)%>";
		arrAReleaseDate[<%=i%>] = "<%=(CommonTools.dateFormat(info.actualReleaseDate).equals("N/A"))?"":CommonTools.dateFormat(info.actualReleaseDate)%>";
		arrPReviewDate[<%=i%>] = "<%=(CommonTools.dateFormat(info.plannedReviewDate).equals("N/A"))?"":CommonTools.dateFormat(info.plannedReviewDate)%>";
		arrPTestDate[<%=i%>] = "<%=(CommonTools.dateFormat(info.plannedTestEndDate).equals("N/A"))?"":CommonTools.dateFormat(info.plannedTestEndDate)%>";
		
<%}%>


	function showDate1(i){
		var start_date = document.getElementsByName("txtCommitedDate")[i];
		if(start_date.value == null || start_date.value ==""){			
			//frmSchedAdd.module_startDate[i].value =  javascript:dateFormat(new Date(),"dd-mmm-yy");
			start_date.value =  "01-Jan-09";
		}
		showCalendar(start_date, start_date, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showDate2(i){
		var start_date = document.getElementsByName("txtReCommitedDate")[i];
		if(start_date.value == null || start_date.value ==""){			
			//frmSchedAdd.module_startDate[i].value =  javascript:dateFormat(new Date(),"dd-mmm-yy");
			start_date.value =  "01-Jan-09";
		}
		showCalendar(start_date, start_date, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showDate3(i){
		var start_date = document.getElementsByName("txtActualDate")[i];
		if(start_date.value == null || start_date.value ==""){			
			//frmSchedAdd.module_startDate[i].value =  javascript:dateFormat(new Date(),"dd-mmm-yy");
			start_date.value =  "01-Jan-09";
		}
		showCalendar(start_date, start_date, "dd-mmm-yy",null,1,-1,-1,true);
	}

	function addSubmit(){	
		if (checkValid()) {
			frmDeliveryBatchAdd.reqType.value=<%=Constants.WO_DELIVERABLE_BATCH_ADD%>;
			frmDeliveryBatchAdd.vRow.value = deliver_nextHiddenIndex-1;			
			frmDeliveryBatchAdd.submit();
		} else return false;	
	}
	
function checkValid(){
	
//	var deliverable_location	= document.getElementsByName("deliverable_location");
	var txtCommitedDate			= document.getElementsByName("txtCommitedDate");
	var txtReCommitedDate		= document.getElementsByName("txtReCommitedDate");
	var txtActualDate			= document.getElementsByName("txtActualDate");
	var deliverable_note		= document.getElementsByName("deliverable_note");
	var reviewDate 				= document.getElementsByName("reviewDate");
	var testDate 				= document.getElementsByName("testDate");
	
	var length = deliver_nextHiddenIndex-1;
	var checkAllBlank = 0;
	var idx;
	
	for(i = 0; i < length;i++) {
	
		//if(!maxLength(deliverable_location[i],"<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Deliverylocation")%>",50))
		//	return;
			
		if(!maxLength(deliverable_note[i],"<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Note")%>",600))
			return;
		
		if (!mandatoryDateFld(txtCommitedDate[i],"<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Committeddate")%>"))
			return;
 
 		if ((compareDate(txtCommitedDate[i].value,txtPrjStartD)==1)	|| (compareDate(txtCommitedDate[i].value,txtPrjEndD)==-1))
 		{ 			 
			 alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Committed__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__~PARAM1_PJSDATE~__From__~PARAM2_PJEDATE~__To")%>', txtPrjStartD , txtPrjEndD)));
  		 	 txtCommitedDate[i].focus();
  		 	 return;
  	  	}
  	  	
		if(!dateFld(txtReCommitedDate[i],"<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Lastcommitteddate")%>"))
			return;
			
		if ((txtReCommitedDate[i].value.length>0)&&(compareDate(txtReCommitedDate[i].value,txtPrjStartD)==1
 			||compareDate(txtReCommitedDate[i].value,txtPrjEndD)==-1)){
			 alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Last__committed__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__~PARAM1_PJSDATE~__From__~PARAM2_PJEDATE~__To")%>',txtPrjStartD , txtPrjEndD)));
  		 	 txtReCommitedDate[i].focus();
  		 	 return;
  	  	}
  	  	if(!beforeTodayFld(txtActualDate[i],"<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Actualdate")%>"))
  		  	return;
  		 	
  		
		if ((txtActualDate[i].value.length==0)&&(myCombo[i].options[myCombo[i].selectedIndex].value=="2")){
  			alert("<%= languageChoose.getMessage("fi.jsp.woDeliveryAdd.ActualDateIsMandatoryWhenStatusIsAccepted")%>");				  		
	  		txtActualDate[i].focus();
	  		return;
		
		}		
		
		releaseDate=(txtReCommitedDate[i].value.length>0 ? txtReCommitedDate[i].value:txtCommitedDate[i].value);
		if ((!reviewDate[i]=="")||(!testDate[i]==""))
		{	
			if (compareDate(testDate[i],reviewDate[i])==1 )
			{
				if (compareDate(releaseDate,reviewDate[i])==1){
				window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Planned__release__date__must__be__equal__or__after__planned__review__date__~PARAM1_REVIEWDATE~")%>', reviewDate[i] )));
					if (txtReCommitedDate[i].value.length>0){
						txtReCommitedDate[i].focus();
						return;
					}else{
						txtCommitedDate[i].focus();
						return;
					}
				}	
			}else{
				if (compareDate(releaseDate,testDate[i])==1){
 			  		window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Planned__release__date__must__be__equal__or__after__planned__test__end__date__~PARAM1_TESTDATE~")%>', testDate[i] )));
					if (txtReCommitedDate[i].value.length>0){
						txtReCommitedDate[i].focus();
						return;
					}else{
						txtCommitedDate[i].focus();
						return;
					}
				}
			}
		}
		
		idx=contains(deliverableCombo,deliverableCombo[i],i,length-1);
		if (idx != -1) {
			alert("Deliverable has been existed !");			
			deliverableCombo[idx].focus();			
			return false;
		}
	}
	
	return true;
}

	
	function Selectvalue(j)
	{
    	 for (var i = 0; i < arrmoduleID.length; i++)
    	 {	
	 		if (deliverableCombo[j].options[deliverableCombo[j].selectedIndex].value==arrmoduleID[i])
	 		{	 			
	 			document.getElementsByName("txtCommitedDate")[j].value=arrPReleaseDate[i];
	 			document.getElementsByName("txtReCommitedDate")[j].value=arrRPReleaseDate[i];
	 			document.getElementsByName("txtActualDate")[j].value=arrAReleaseDate[i];
	 			document.getElementsByName("deliverable_note")[j].value=arrmoduleNote[i];
	 			reviewDate[j]=arrPReviewDate[i];
	 			testDate[j]=arrPTestDate[i];
	 		}
         }
	}
	
	function contains(a,e,idx, iLength) {
		for(j=iLength;j>=0;j--) {
			if (j==idx) continue;
			if (a[j].value==e.value) return j;  
		}
		return -1;
	}
	
</SCRIPT>
</BODY>
</HTML>
