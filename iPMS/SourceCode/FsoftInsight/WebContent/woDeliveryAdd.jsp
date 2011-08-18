<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*, com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>woDeliveryAdd.jsp</TITLE>

<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class ="BD" onLoad="loadPrjMenu();frm_deliveryAdd.deliverable_ID.focus();changeStatus();">
<%
Vector moduleList = (Vector)session.getAttribute("woModuleList");

ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
String note=null;
String PlannedReleaseDate="";
String ReplannedReleaseDate="";
String ActualReleaseDate="";
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.WorkorderDeliverables")%> </P>

<br>
<form name="frm_deliveryAdd" action="Fms1Servlet" method = "get">
<input type = "hidden" name="reqType" value="<%=Constants.WO_DELIVERABLE_ADD%>">
<TABLE cellspacing="1" class="Table">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Addnewdeliverable")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Deliverable")%> </TD>
            <TD class="CellBGR3">
            <SELECT name="deliverable_ID" class="COMBO" onchange="Selectvalue();">
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
%><OPTION value="<%=info.moduleID%>"><%=info.name%></OPTION>
<%}%>         
        </SELECT>
        </TD>
        </TR>
		<!-- Comment by HaiMM
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Deliverylocation")%> </TD>
            <TD class="CellBGR3">
            <input type="text" name="deliverable_location" maxlength="50" size = "50">
			</TD>
        </TR>
        -->
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Firstcommitteddate")%>*</TD>
            <TD class="CellBGR3"><INPUT name="txtCommitedDate" maxlength="9" size = "9" value=<%=PlannedReleaseDate%>> (DD-MMM-YY) </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Lastcommitteddate")%> </TD>
            <TD class="CellBGR3"><INPUT name="txtReCommitedDate" maxlength="9" size = "9" value=<%=ReplannedReleaseDate%>> (DD-MMM-YY) </TD>
        </TR>
         <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Actualdate")%> <A name="star"><A></TD>
            <TD class="CellBGR3"><INPUT name="txtActualDate" maxlength="9" size = "9" value=<%=ActualReleaseDate%>> (DD-MMM-YY) </TD>
        </TR>
         <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Status")%> </TD>
            <TD class="CellBGR3">
            <SELECT name="cmbStatus" class="COMBO" onChange="changeStatus();">                
                <OPTION value="1"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Pending")%> </OPTION>
                <OPTION value="2"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Accepted")%> </OPTION>
                <OPTION value="3"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Rejected")%> </OPTION>
                <OPTION value="4"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Cancelled")%> </OPTION>
                <OPTION value="5"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Other")%> </OPTION>
                <OPTION value="6" selected> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.InProgress")%> </OPTION>
            </SELECT>
            </TD>
        </TR>
        
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Note")%> </TD>
            <TD class="CellBGR3"><TEXTAREA name="deliverable_note" rows="4" cols="50"><%=(note==null?"":note)%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
</form>
<br>
<INPUT type="button" name="Add" value=" <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.OK")%> " onclick="javascript:on_Submit();" class="BUTTON">
<INPUT type="button" name="Cancel" value=" <%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Cancel")%> " class="BUTTON" onclick="doIt(<%=Constants.WO_DELIVERABLE_GET_LIST%>);">

<script language = "javascript">
	var myCombo=frm_deliveryAdd.cmbStatus;
	var deliverableCombo=frm_deliveryAdd.deliverable_ID;
	var txtPrjStartD="<%=(prjDateInfo.startD== null)?"":CommonTools.dateFormat(prjDateInfo.startD)%>";
	var txtPrjEndD="<%=(prjDateInfo.endD== null)?"":CommonTools.dateFormat(prjDateInfo.endD)%>";
	var reviewDate;
	var testDate;
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
		reviewDate="<%=(CommonTools.dateFormat(info.plannedReviewDate).equals("N/A"))?"":CommonTools.dateFormat(info.plannedReviewDate)%>";
		testDate="<%=(CommonTools.dateFormat(info.plannedTestEndDate).equals("N/A"))?"":CommonTools.dateFormat(info.plannedTestEndDate)%>";
		<%}%>
	
		arrmoduleID[<%=i%>] = "<%=info.moduleID%>";
		arrmoduleNote[<%=i%>] = "<%=(info.baselineNote==null?"":ConvertString.toJScript(info.baselineNote.trim()))%>";
		arrPReleaseDate[<%=i%>] = "<%=(CommonTools.dateFormat(info.plannedReleaseDate).equals("N/A"))?"":CommonTools.dateFormat(info.plannedReleaseDate)%>";
		arrRPReleaseDate[<%=i%>] = "<%=(CommonTools.dateFormat(info.rePlannedReleaseDate).equals("N/A"))?"":CommonTools.dateFormat(info.rePlannedReleaseDate)%>";
		arrAReleaseDate[<%=i%>] = "<%=(CommonTools.dateFormat(info.actualReleaseDate).equals("N/A"))?"":CommonTools.dateFormat(info.actualReleaseDate)%>";
		arrPReviewDate[<%=i%>] = "<%=(CommonTools.dateFormat(info.plannedReviewDate).equals("N/A"))?"":CommonTools.dateFormat(info.plannedReviewDate)%>";
		arrPTestDate[<%=i%>] = "<%=(CommonTools.dateFormat(info.plannedTestEndDate).equals("N/A"))?"":CommonTools.dateFormat(info.plannedTestEndDate)%>";
		
<%}%>
	
	
	
	function Selectvalue()
	{
    	 for (var i = 0; i < arrmoduleID.length; i++){    	 	
	 		if (deliverableCombo.options[deliverableCombo.selectedIndex].value==arrmoduleID[i])
	 		{
	 			
	 			frm_deliveryAdd.txtCommitedDate.value=arrPReleaseDate[i];
	 			frm_deliveryAdd.txtReCommitedDate.value=arrRPReleaseDate[i];
	 			frm_deliveryAdd.txtActualDate.value=arrAReleaseDate[i];
	 			frm_deliveryAdd.deliverable_note.value=arrmoduleNote[i];
	 			reviewDate=arrPReviewDate[i];
	 			testDate=arrPTestDate[i];
	 			
	 		}
         }
	}
	
	function on_Submit()
	{					
  	 	if(!maxLength(frm_deliveryAdd.deliverable_note,"<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Note")%>",600))
			return;
		if (!mandatoryDateFld(frm_deliveryAdd.txtCommitedDate,"<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Committeddate")%>"))
			return;
 
 		if ((compareDate(frm_deliveryAdd.txtCommitedDate.value,txtPrjStartD)==1)
 		||(compareDate(frm_deliveryAdd.txtCommitedDate.value,txtPrjEndD)==-1))
 		{
			 alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Committed__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__~PARAM1_PJSDATE~__From__~PARAM2_PJEDATE~__To")%>', txtPrjStartD , txtPrjEndD)));
  		 	 frm_deliveryAdd.txtCommitedDate.focus();
  		 	 return;
  	  	}
		if(!dateFld(frm_deliveryAdd.txtReCommitedDate,"<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Lastcommitteddate")%>"))
			return;
		if ((frm_deliveryAdd.txtReCommitedDate.value.length>0)&&(compareDate(frm_deliveryAdd.txtReCommitedDate.value,txtPrjStartD)==1
 			||compareDate(frm_deliveryAdd.txtReCommitedDate.value,txtPrjEndD)==-1)){
			 alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Last__committed__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__~PARAM1_PJSDATE~__From__~PARAM2_PJEDATE~__To")%>',txtPrjStartD , txtPrjEndD)));
  		 	 frm_deliveryAdd.txtReCommitedDate.focus();
  		 	 return;
  	  	}
  	  	if(!beforeTodayFld(frm_deliveryAdd.txtActualDate,"<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Actualdate")%>"))
  		  	return;
		if ((frm_deliveryAdd.txtActualDate.value.length==0)&&(myCombo.options[myCombo.selectedIndex].value=="2")){
  			alert("<%= languageChoose.getMessage("fi.jsp.woDeliveryAdd.ActualDateIsMandatoryWhenStatusIsAccepted")%>");				  		
	  		frm_deliveryAdd.txtActualDate.focus();
	  		return;
		
		}
		
		releaseDate=(frm_deliveryAdd.txtReCommitedDate.value.length>0 ? frm_deliveryAdd.txtReCommitedDate.value:frm_deliveryAdd.txtCommitedDate.value);
		if ((!reviewDate=="")||(!testDate==""))
		{	
			if (compareDate(testDate,reviewDate)==1 )
			{
						if (compareDate(releaseDate,reviewDate)==1){
						window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Planned__release__date__must__be__equal__or__after__planned__review__date__~PARAM1_REVIEWDATE~")%>', reviewDate )));
						if (frm_deliveryAdd.txtReCommitedDate.value.length>0){
							frm_deliveryAdd.txtReCommitedDate.focus();
							return;
						}else{
							frm_deliveryAdd.txtCommitedDate.focus();
							return;
						}
					}
					
					
			}else{
				    if (compareDate(releaseDate,testDate)==1){
 			  			window.alert(getParamText(new Array('<%=languageChoose.getMessage("fi.jsp.woDeliveryAdd.Planned__release__date__must__be__equal__or__after__planned__test__end__date__~PARAM1_TESTDATE~")%>', testDate )));
						if (frm_deliveryAdd.txtReCommitedDate.value.length>0){
							frm_deliveryAdd.txtReCommitedDate.focus();
							return;
						}else{
							frm_deliveryAdd.txtCommitedDate.focus();
							return;
						}
					}
			}
			
			
					
		
		
	}	
	document.frm_deliveryAdd.submit();
	}	
	
	
	function changeStatus(){
		if (myCombo.selectedIndex>=0 && myCombo.options[myCombo.selectedIndex].value=="2" )
			document.all["star"].innerHTML='*';
		else
			document.all["star"].innerHTML='';
	}
</script> 
</BODY>
</HTML>
