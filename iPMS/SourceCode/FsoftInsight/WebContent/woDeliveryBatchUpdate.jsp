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
<TITLE>woDeliveryBatchUpdate.jsp</TITLE>

</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int row = 0;
	int nRow = 1;
	boolean isOver = false;
	int deliveryDisplayed;
	String rowStyle;	
	
	ModuleInfo moduleInfo = null;	
	
	Vector vDelivery = (Vector) session.getAttribute("DeliveryBatchUpdateList");	
	
	Vector vErrDelivery = (Vector) request.getAttribute("ErrDeliveryBatchList");
	if (vErrDelivery != null) {
		isOver = true;
		request.removeAttribute("ErrDeliveryBatchList");
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
	
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
%>
<BODY onLoad="loadPrjMenu();" class="BD">

<form name ="frmDeliveryUpdate" method= "post" action = "Fms1Servlet">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.WorkorderDeliverables")%> </P>
<% 	
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">Update datas failure! System error </P>
<%
	} 
%>
<BR>
<TABLE cellspacing="1" class="Table" width="125%">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.UpdateDeliverable")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">			
			<TD width="24" align = "center"> # </TD>
			<TD class="ColumnLabel" width="50"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Deliverable")%> </TD>
			<TD class="ColumnLabel" width = "12%"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Firstcommiteddate")%>* </TD>
			<TD class="ColumnLabel" width = "12%"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Lastcommitteddate")%> </TD>
			<TD class="ColumnLabel" width = "12%"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Actualdate")%> <A name="star"></A></TD>
			<TD class="ColumnLabel" width = "10%"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Status")%> </TD>
			<TD class="ColumnLabel" width = "18%"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Note")%> </TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	Vector vTemp = new Vector();
	if (isOver)  vTemp = vErrDelivery;
	else vTemp = vDelivery;
	nRow = vTemp.size();
	
	// Display current list (last updated data)
	for (; row < nRow; row++) {
		moduleInfo = (ModuleInfo) vTemp.elementAt(row);
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="module_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD align = "center" width = "3%"><%=row + 1%><INPUT type="hidden" name ="deliverable_ID" value ="<%=moduleInfo.moduleID%>"/></TD>
			<TD height="57"><%=moduleInfo.name%></TD>
			<!-- Comment by HaiMM
			<TD height="57">
			 	<TEXTAREA style="width:100%;height:100%"  name="deliverable_location"><%=moduleInfo.deliveryLocation==null?"":moduleInfo.deliveryLocation%></TEXTAREA> 
			</TD>
			-->
			<TD height="57">
				<INPUT type ="text" size = "9" maxlength ="9" name="txtCommitedDate" value="<%=CommonTools.dateUpdate(moduleInfo.plannedReleaseDate)%>"/>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showDate1(<%=row%>)"/><BR/>
				<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DateFormatDDMMMYY")%>
			</TD>
			<TD height="57">
				<INPUT type ="text" size = "9" maxlength ="9" name="txtReCommitedDate" value="<%=CommonTools.dateUpdate(moduleInfo.rePlannedReleaseDate)%>"/>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showDate2(<%=row%>)"/><BR/>
				<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DateFormatDDMMMYY")%>
			</TD>
			<TD height="57">
				<INPUT type ="text" size = "9" maxlength ="9" name="txtActualDate" value="<%=CommonTools.dateUpdate(moduleInfo.actualReleaseDate)%>"/>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showDate3(<%=row%>)"/><BR/>
				<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.DateFormatDDMMMYY")%>
			</TD>
			<TD height="57">
				<SELECT name="cmbStatus" class="COMBO" onChange="">                
	                <OPTION value="1"<%if(moduleInfo.status==1){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Pending")%> </OPTION>
	                <OPTION value="2"<%if(moduleInfo.status==2){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Accepted")%> </OPTION>
	                <OPTION value="3"<%if(moduleInfo.status==3){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Rejected")%> </OPTION>
	                <OPTION value="4"<%if(moduleInfo.status==4){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Cancelled")%> </OPTION>
	                <OPTION value="5"<%if(moduleInfo.status==5){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Other")%> </OPTION>
	                <OPTION value="6"<%if(moduleInfo.status==6){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Inprogress")%> </OPTION>
	            </SELECT>
            </TD>
			<TD height="57">
				<TEXTAREA style="width:100%;height:100%" name="deliverable_note"><%=moduleInfo.note==null?"":moduleInfo.note%></TEXTAREA> 
				<input type ="hidden" name  ="reviewDate" value = "<%=(CommonTools.dateFormat(moduleInfo.plannedReviewDate).equals("N/A"))?"":CommonTools.dateFormat(moduleInfo.plannedReviewDate)%>"/>
				<input type ="hidden" name  ="testDate" value = "<%=(CommonTools.dateFormat(moduleInfo.plannedTestEndDate).equals("N/A"))?"":CommonTools.dateFormat(moduleInfo.plannedTestEndDate)%>"/>
			</TD>
		</TR>
<%	
	}
	deliveryDisplayed = row;	// Indicate numbers of lines displayed
%>
	</TBODY>
</TABLE>
<P/>
<input type ="hidden" name = "reqType"/>
<INPUT type="button" name="plOK" value="<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.OK")%>" class="BUTTON" onclick="updateSubmit();">
<INPUT type="button" name="plCancel" value="<%=languageChoose.getMessage("fi.jsp.plProjSchedUpdate.Cancel")%>" class="BUTTON" onclick="jumpURL('woDeliverable.jsp');">
<INPUT type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">
</form>

<SCRIPT language="JavaScript">	

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

	function updateSubmit(){	
		if (checkValid()) {
			frmDeliveryUpdate.reqType.value=<%=Constants.WO_DELIVERABLE_BATCH_UPDATE%>;
			frmDeliveryUpdate.submit();
		} else return false;	
	}
	
	var txtPrjStartD="<%=(prjDateInfo.startD== null)?"":CommonTools.dateFormat(prjDateInfo.startD)%>";
	var txtPrjEndD="<%=(prjDateInfo.endD== null)?"":CommonTools.dateFormat(prjDateInfo.endD)%>";	
	

	function checkValid(){
		var myCombo 				= document.getElementsByName("cmbStatus");
//		var deliverable_location	= document.getElementsByName("deliverable_location");
		var txtCommitedDate			= document.getElementsByName("txtCommitedDate");
		var txtReCommitedDate		= document.getElementsByName("txtReCommitedDate");
		var txtActualDate			= document.getElementsByName("txtActualDate");
		var deliverable_note		= document.getElementsByName("deliverable_note");
		var reviewDate 				= document.getElementsByName("reviewDate");
		var testDate 				= document.getElementsByName("testDate");

		var validateValue;
		var releaseDate;
		
		var length = <%=deliveryDisplayed%>;
		var checkAllBlank = 0;
		
		for(i=0; i < length;i++) {
			
			if(!maxLength(deliverable_note[i],"<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Note")%>",600))
				return;
			
			// change requirement from Mr Rau
			if (!mandatoryDateFld(txtCommitedDate[i],"<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Committeddate")%>"))
				return false;
	 		
	 		if (compareDate(txtCommitedDate[i].value,txtPrjStartD)==1
	 			||compareDate(txtCommitedDate[i].value,txtPrjEndD)==-1){
				 alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Committed__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__~PARAM1_PJSDATE~__From__~PARAM2_PJEDATE~__To")%>', txtPrjStartD , txtPrjEndD )));
	  		 	 txtCommitedDate[i].focus();
	  		 	 return false;
	  	  	}
	  	  	
	  	  	if(!dateFld(txtReCommitedDate[i],"<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Lastcommitteddate")%>"))
				return false;
			
			if ((txtReCommitedDate[i].value.length>0)&&(compareDate(txtReCommitedDate[i].value,txtPrjStartD)==1
	 			||compareDate(txtReCommitedDate[i].value,txtPrjEndD)==-1)){
				 alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Last__committed__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__~PARAM1_PJSDATE~__From__~PARAM2_PJEDATE~__To")%>', txtPrjStartD, txtPrjEndD)));
	  		 	 txtReCommitedDate[i].focus();
	  		 	 return false;
	  	  	}
	  	  	
			if(!beforeTodayFld(txtActualDate[i],"<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Actualdate")%>"))
	  		  	return false;
	  		
			if ((txtActualDate[i].value.length==0)&&(myCombo[i].options[myCombo[i].selectedIndex].value=="2")){
	  			alert("<%= languageChoose.getMessage("fi.jsp.woDeliverableUpdate.ActualDateIsMandatoryWhenStatusIsAccepted")%>");				  		
		  		txtActualDate[i].focus();
		  		return false;
			}
			
			releaseDate=(txtReCommitedDate[i].value.length>0 ? txtReCommitedDate[i].value:txtCommitedDate[i].value);
			if ((!reviewDate[i]=="")||(!testDate[i]==""))
			{	
				if (compareDate(testDate[i].value,reviewDate[i].value)==1 )
				{					
					if (compareDate(releaseDate,reviewDate[i].value)==1){						
						alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Planned__release__date__must__be__equal__or__after__planned__review__date__~PARAM1_REVIEWDATE~")%>',reviewDate[i])));
						if (txtReCommitedDate[i].value.length>0){
							txtReCommitedDate[i].focus();
							return false;
						}else{
							txtCommitedDate[i].focus();
							return false;
						}						
					}		
				}else{					
				    if (compareDate(releaseDate,testDate[i].value)==1){				    	
						alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Planned__release__date__must__be__equal__or__after__planned__test__end__date__~PARAM1_TESTDATE~")%>',testDate[i])));
						if (txtReCommitedDate[i].value.length>0){
							txtReCommitedDate[i].focus();
							return false;
						}else{
							txtCommitedDate[i].focus();
							return false;
						}						
					}
				}
			}
		}
		return true;
	}

</SCRIPT>
</BODY>
</HTML>
