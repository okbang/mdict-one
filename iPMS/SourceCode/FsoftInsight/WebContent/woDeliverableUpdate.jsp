<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woDeliverableUpdate.jsp</TITLE>

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">

<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadPrjMenu();frm_deliveryUpdate.txtCommitedDate.focus();changeStatus();">
<%
	Vector deliverableList = (Vector) session.getAttribute("deliverableList");
	String vtid=request.getParameter("vtid");
	ModuleInfo moduleInfo = (ModuleInfo)deliverableList.elementAt(Integer.parseInt(vtid));
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");

	String errMessage=request.getParameter("error");
	String msgContent=request.getParameter("content");
	// landd add stage start
	Vector vtStage=(Vector)session.getAttribute("stageVector");
	// landd add stage end
%>
<form name="frm_deliveryUpdate" action="Fms1Servlet" method = "get">
<INPUT type = "hidden" name="reqType" value="<%=Constants.WO_DELIVERABLE_UPDATE%>">
<INPUT type = "hidden" name="deliverable_ID" value="<%=moduleInfo.moduleID%>">
<INPUT type = "hidden" name="vtid" value="<%=vtid%>">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.WorkorderDeliverables")%> </P>
<br>
<P class="error"><%=((errMessage==null)?"":languageChoose.getMessage(errMessage) + msgContent)%></P>
<TABLE cellspacing="1" class="Table">

<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.UpdateDeliverable")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Deliverable")%> </TD>
            <TD class = "CellBGR3"><%=moduleInfo.name%></TD>
        </TR>
		<!-- Comment by HaiMM
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Deliverylocation")%> </TD>
            <TD class="CellBGR3">
            <input type="text" name="deliverable_location" value = "<%=((moduleInfo.deliveryLocation == null)? "" : moduleInfo.deliveryLocation)%>" maxlength="50" size = "50">
			</TD>
        </TR>
        -->
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Firstcommiteddate")%>* </TD>
            <TD class="CellBGR3"><input size="9" maxlength ="9" name = "txtCommitedDate" value ="<%=CommonTools.dateUpdate(moduleInfo.plannedReleaseDate)%>"> 
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showBEndD()'> (DD-MMM-YY)
            </TD>
            
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Lastcommitteddate")%> </TD>
            <TD class="CellBGR3"><input size="9" maxlength ="9" name = "txtReCommitedDate" value ="<%=CommonTools.dateUpdate(moduleInfo.rePlannedReleaseDate)%>">
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPEndD()'> (DD-MMM-YY)
            </TD>
            
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Actualdate")%> <A name="star"><A></TD>
            <TD class="CellBGR3"><input size="9" maxlength ="9" type="text" name = "txtActualDate" value="<%=CommonTools.dateUpdate(moduleInfo.actualReleaseDate)%>"> 
            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showAEndD()'> (DD-MMM-YY)
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Status")%> </TD>
            <TD class="CellBGR3">
             <SELECT name="cmbStatus" class="COMBO" onChange="changeStatus();">                
                <OPTION value="1"<%if(moduleInfo.status==1){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Pending")%> </OPTION>
                <OPTION value="2"<%if(moduleInfo.status==2){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Accepted")%> </OPTION>
                <OPTION value="3"<%if(moduleInfo.status==3){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Rejected")%> </OPTION>
                <OPTION value="4"<%if(moduleInfo.status==4){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Cancelled")%> </OPTION>
                <OPTION value="5"<%if(moduleInfo.status==5){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Other")%> </OPTION>
                <OPTION value="6"<%if(moduleInfo.status==6){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Inprogress")%> </OPTION>
            </SELECT></TD>
        </TR> 
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Note")%> </TD>
            <TD class="CellBGR3"><TEXTAREA name="deliverable_note" rows="4" cols="50"><%=((moduleInfo.note == null) ? "" : moduleInfo.note)%></TEXTAREA></TD>
        </TR>

</TABLE>

</form>
<br>
<form name="frm_deliverableDelete" action="Fms1Servlet" method = "get">
    <input type = "hidden" name="reqType" value="<%=Constants.WO_DELIVERABLE_DELETE %>" >
	<INPUT type = "hidden" name="vtid" value="<%=vtid%>">
</form>

<INPUT type="button" name="update2" value=" <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Update")%> " onclick="javascript:on_Submit1();" class="BUTTON">
<INPUT type="button" name="delete" value=" <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Delete")%> "  onclick="javascript:on_Submit2();"  class="BUTTON">
<INPUT type="button" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Cancel")%> "  class="BUTTON" onclick="doIt(<%=Constants.WO_DELIVERABLE_GET_LIST%>);">

</BODY>
</HTML>
<SCRIPT language = "javascript">
	var myCombo=frm_deliveryUpdate.cmbStatus;
	var txtPrjStartD="<%=(prjDateInfo.startD== null)?"":CommonTools.dateFormat(prjDateInfo.startD)%>";
	var txtPrjEndD="<%=(prjDateInfo.endD== null)?"":CommonTools.dateFormat(prjDateInfo.endD)%>";
	var reviewDate;
	var testDate;
	var releaseDate;
	
	reviewDate="<%=(CommonTools.dateFormat(moduleInfo.plannedReviewDate).equals("N/A"))?"":CommonTools.dateFormat(moduleInfo.plannedReviewDate)%>";
	testDate="<%=(CommonTools.dateFormat(moduleInfo.plannedTestEndDate).equals("N/A"))?"":CommonTools.dateFormat(moduleInfo.plannedTestEndDate)%>";
	
	function showBEndD(){
		showCalendar(frm_deliveryUpdate.txtCommitedDate, frm_deliveryUpdate.txtCommitedDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showPEndD(){
		showCalendar(frm_deliveryUpdate.txtReCommitedDate, frm_deliveryUpdate.txtReCommitedDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showAEndD(){
		showCalendar(frm_deliveryUpdate.txtActualDate, frm_deliveryUpdate.txtActualDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function on_Submit1(){
		var validateValue;
		
		if(!maxLength(frm_deliveryUpdate.deliverable_note,"<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Note")%>",600))
			return;
		
		// change requirement from Mr Rau
		if (!mandatoryDateFld(frm_deliveryUpdate.txtCommitedDate,"<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Committeddate")%>"))
			return;
 
 		if (compareDate(frm_deliveryUpdate.txtCommitedDate.value,txtPrjStartD)==1
 			||compareDate(frm_deliveryUpdate.txtCommitedDate.value,txtPrjEndD)==-1){
			 alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Committed__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__~PARAM1_PJSDATE~__From__~PARAM2_PJEDATE~__To")%>', txtPrjStartD , txtPrjEndD )));
  		 	 frm_deliveryUpdate.txtCommitedDate.focus();
  		 	 return;
  	  	}
  	  	if(!dateFld(frm_deliveryUpdate.txtReCommitedDate,"<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Lastcommitteddate")%>"))
			return;
		if ((frm_deliveryUpdate.txtReCommitedDate.value.length>0)&&(compareDate(frm_deliveryUpdate.txtReCommitedDate.value,txtPrjStartD)==1
 			||compareDate(frm_deliveryUpdate.txtReCommitedDate.value,txtPrjEndD)==-1)){
			 alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Last__committed__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__~PARAM1_PJSDATE~__From__~PARAM2_PJEDATE~__To")%>', txtPrjStartD, txtPrjEndD)));
  		 	 frm_deliveryUpdate.txtReCommitedDate.focus();
  		 	 return;
  	  	}
		if(!beforeTodayFld(frm_deliveryUpdate.txtActualDate,"<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Actualdate")%>"))
  		  	return;
		if ((frm_deliveryUpdate.txtActualDate.value.length==0)&&(myCombo.options[myCombo.selectedIndex].value=="2")){
  			alert("<%= languageChoose.getMessage("fi.jsp.woDeliverableUpdate.ActualDateIsMandatoryWhenStatusIsAccepted")%>");				  		
	  		frm_deliveryUpdate.txtActualDate.focus();
	  		return;
		}
		
		releaseDate=(frm_deliveryUpdate.txtReCommitedDate.value.length>0 ? frm_deliveryUpdate.txtReCommitedDate.value:frm_deliveryUpdate.txtCommitedDate.value);
		if ((!reviewDate=="")||(!testDate==""))
		{	
			if (compareDate(testDate,reviewDate)==1 )
			{
						if (compareDate(releaseDate,reviewDate)==1){
						window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Planned__release__date__must__be__equal__or__after__planned__review__date__~PARAM1_REVIEWDATE~")%>',reviewDate)));
						if (frm_deliveryUpdate.txtReCommitedDate.value.length>0){
							frm_deliveryUpdate.txtReCommitedDate.focus();
							return;
						}else{
							frm_deliveryUpdate.txtCommitedDate.focus();
							return;
						}
					}
					
					
			}else{
				    if (compareDate(releaseDate,testDate)==1){
						window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliverableUpdate.Planned__release__date__must__be__equal__or__after__planned__test__end__date__~PARAM1_TESTDATE~")%>',testDate)));
						if (frm_deliveryUpdate.txtReCommitedDate.value.length>0){
							frm_deliveryUpdate.txtReCommitedDate.focus();
							return;
						}else{
							frm_deliveryUpdate.txtCommitedDate.focus();
							return;
						}
					}
			}
		
	}	
		document.frm_deliveryUpdate.submit();
	}
	
	
	function on_Submit2(){
		if (window.confirm("<%= languageChoose.getMessage("fi.jsp.woDeliverableUpdate.AreYouSureToDeleteThisDeliverable")%>")) 
			document.frm_deliverableDelete.submit();
	}
	
	function changeStatus(){
		if (myCombo.selectedIndex>=0 && myCombo.options[myCombo.selectedIndex].value=="2" )
			document.all["star"].innerHTML='*';
		else
			document.all["star"].innerHTML='';
	}
</SCRIPT> 
