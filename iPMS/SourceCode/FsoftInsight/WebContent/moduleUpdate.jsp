<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>moduleUpdate.jsp</TITLE>

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
<%
	int caller=Integer.parseInt(session.getAttribute("caller").toString());
	String title;
	
	if(caller==Constants.SCHEDULE_CALLER){//called from shedule
		title=languageChoose.getMessage("fi.jsp.moduleUpdate.ScheduleReviewandtestactivities");
	}
	else { //called from project plan
		title=languageChoose.getMessage("fi.jsp.moduleUpdate.ProjectplanReviewandtestactivities");
	}
		
	Vector vtModun=(Vector)session.getAttribute("moduleVector");
	ModuleInfo modun=null;
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	modun=(ModuleInfo)vtModun.get(vtID);
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	Vector userList=(Vector)session.getAttribute("userList");
	Vector vt=(Vector)session.getAttribute("devVector");
	if (modun.approver.equals("N/A")) modun.approver="";
	if (modun.reviewer.equals("N/A")) modun.reviewer="";
%>

<SCRIPT language="javascript">
	dev = new Array(<%=vt.size()%>);
<%	for(int i=0;i<vt.size();i++){
		UserInfo devInfo=(UserInfo)vt.get(i);
		%>dev[<%=i%>]="<%=devInfo.Name%>";
<%}%>
	
	function selectAllDevelopers()
	{
	    var myElement;
	    for (var i = frm.cmbConductor.options.length; i >= 1; i--){
	        frm.cmbConductor.options[i] = null;
	    }
        for (var i = 0; i < dev.length; i++){
            if (dev[i] != "<%=modun.conductor%>"){
	            myElement = document.createElement("option");
	            myElement.value = dev[i];
	            myElement.text = dev[i];
	            frm.cmbConductor.add(myElement);
            }
	    }
	}
</SCRIPT> 

<BODY class="BD" onLoad="loadPrjMenu();frm.txtPReviewD.focus(); disabRel();">
<P class="TITLE"><%=title%></p>
<BR>
<FORM method="post" action="Fms1Servlet?reqType=<%=Constants.UPDATE_REVIEW_TEST %>#revtest" name="frm">
<INPUT type="hidden" name="txtModuleID" value="<%=modun.moduleID%>">
<TABLE class="Table" width="100%" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.moduleUpdate.Updatereviewandtestactivity")%></CAPTION>
    <TBODY>
    	<TR>
            <TD class="ColumnLabel" width='220px'><%=languageChoose.getMessage("fi.jsp.moduleUpdate.Product")%></TD>
            <TD class="CellBGRnews" colspan=3><%=ConvertString.toHtml(modun.name)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.moduleUpdate.Workproduct")%></TD>
            <TD class="CellBGRnews" colspan=3><%=languageChoose.getMessage(ConvertString.toHtml(modun.wpName))%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleUpdate.Plannedactualreviewdate")%> </TD>
            <TD class="CellBGRnews" colspan=1 nowrap="nowrap"><INPUT size="9" name="txtPReviewD" maxlength="9" value="<%=CommonTools.dateUpdate(modun.plannedReviewDate)%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPReviewD()'></TD>
            <TD class="CellBGRnews" colspan=2 nowrap="nowrap"><INPUT size="9" type="text" maxlength="9" name="txtAReviewD" value="<%=CommonTools.dateUpdate(modun.actualReviewDate)%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showAReviewD()'>            
	            (DD-MMM-YY) </TD>
        </TR>
        <%if(modun.isNormal){%>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleUpdate.Plannedactualtestenddate")%> </TD>
            <TD class="CellBGRnews" nowrap="nowrap"><INPUT size="9" maxlength="9" name="txtPTestD" value="<%=CommonTools.dateUpdate(modun.plannedTestEndDate)%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPTestD()'></TD>
        	<TD class="CellBGRnews" colspan=2 nowrap="nowrap"><INPUT size="9" maxlength="9" name="txtATestD" value="<%=CommonTools.dateUpdate(modun.actualTestEndDate)%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showATestD()'>        	
	        	(DD-MMM-YY) </TD>
        </TR>
        <%}%>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleUpdate.Plannedreplannedactualreleasedate")%> </TD>
            <TD class="CellBGRnews" nowrap="nowrap"><INPUT size="9" maxlength="9" name="txtPReleaseD" value="<%=CommonTools.dateUpdate(modun.plannedReleaseDate)%>">
   				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPReleaseD()'></TD>
             <TD class="CellBGRnews" nowrap="nowrap"><INPUT size="9" maxlength="9" name="txtRPReleaseD" value="<%=CommonTools.dateUpdate(modun.rePlannedReleaseDate)%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showRPReleaseD()'></TD>
            <TD class="CellBGRnews" nowrap="nowrap"><INPUT size="9" maxlength="9" name="txtAReleaseD" value="<%=CommonTools.dateUpdate(modun.actualReleaseDate)%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showAReleaseD()'>            
	            (DD-MMM-YY) <BR><A href='javascript:clickMe()' name='theSize'></A></TD>
        </TR>
         <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.moduleUpdate.ReviewLeader")%></TD>
            <TD class="CellBGRnews" colspan=3><SELECT name="cmbConductor" class="COMBO">
                <OPTION value="<%=modun.conductor%>"selected><%=modun.conductor%></OPTION>

                <%  for(int i=0;i<userList.size();i++){
		            	AssignmentInfo assInfo = (AssignmentInfo) userList.elementAt(i);
						if(modun.conductor.toLowerCase().compareTo(assInfo.devName.toLowerCase())!=0){
                			%><OPTION value="<%=assInfo.account%>"><%=assInfo.devName%></OPTION>
                			<%}
                	}%>
                
            </SELECT> <A href="javascript:selectAllDevelopers()">  <%=languageChoose.getMessage("fi.jsp.moduleUpdate.more")%>  </A> </TD>
        </TR>
        <%if(modun.isDel){%>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleUpdate.Deliverablestatus")%> </TD>
            <TD class="CellBGRnews" colspan=3><SELECT name="cmbStatus" class="COMBO">
            	<%for (int i=1;i<ModuleInfo.statusNames.length;i++){%>                
	            <OPTION value="<%=i%>"<%if(modun.status==i){%>selected<%}%>><%=languageChoose.getMessage(ModuleInfo.statusNames[i])%></OPTION>
	            <%}%>
            </SELECT></TD>
        </TR> 
        <%}%>
    </TBODY> 
</TABLE>
<BR>
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.moduleUpdate.OK")%>" class="BUTTON" onclick="doUpdate();">
<INPUT type="button" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Cancel")%>" class="BUTTON" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_REVIEW_TEST_GET_LIST:Constants.GET_QUALITY_OBJECTIVE_LIST%>);">
</FORM>
<SCRIPT language="javascript">

	function showPReviewD(){
		showCalendar(frm.txtPReviewD, frm.txtPReviewD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showAReviewD(){
		showCalendar(frm.txtAReviewD, frm.txtAReviewD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showPTestD(){
		showCalendar(frm.txtPTestD, frm.txtPTestD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showATestD(){
		showCalendar(frm.txtATestD, frm.txtATestD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showPReleaseD(){
		showCalendar(frm.txtPReleaseD, frm.txtPReleaseD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showRPReleaseD(){
		showCalendar(frm.txtRPReleaseD, frm.txtRPReleaseD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showAReleaseD(){
		showCalendar(frm.txtAReleaseD, frm.txtAReleaseD, "dd-mmm-yy",null,1,-1,-1,true);
	}

var txtPrjStartD="<%=(prjDateInfo.startD== null)?"":CommonTools.dateFormat(prjDateInfo.startD)%>";
var txtPrjEndD="<%=(prjDateInfo.endD== null)?"":CommonTools.dateFormat(prjDateInfo.endD)%>";
var txtType=<%=((modun.isNormal)?1:0)%>;
var disabRelease=<%=((modun.actualSize>=0)?"false":"true")%>;
function disabRel(){
	frm.txtAReleaseD.disabled=disabRelease;
	if (disabRelease)
		document.all['theSize'].innerText="<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Pleaseinputsizepriortoupdate")%>";
	else
		document.all['theSize'].innerText="";
}
function clickMe(){
	var rv = window.showModalDialog("Fms1Servlet?reqType=<%=Constants.SCHE_SIZE_INPUT%>&modID=<%=modun.moduleID%>", null,"dialogWidth: 600px; dialogHeight: 350px");
	disabRelease=rv;
	disabRel();
}
function doUpdate(){
	if(!mandatoryDateFld(frm.txtPReviewD,"<%= languageChoose.getMessage("fi.jsp.moduleUpdate.Plannedreviewdate")%>"))
		return;		
 	
	if(compareDate(frm.txtPReviewD.value,txtPrjStartD)==1){
		window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Planned__review__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__from__~PARAM1_DATE~__to__~PARAM2_DATE~")%>",txtPrjStartD,txtPrjEndD)));
		if(!frm.txtPReviewD.disabled)
			frm.txtPReviewD.focus();
		return;
	}
	if(compareDate(frm.txtPReviewD.value,txtPrjEndD)==-1){
		window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Planned__review__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__from__~PARAM1_DATE~__to__~PARAM2_DATE~")%>",txtPrjStartD,txtPrjEndD)));
		if(!frm.txtPReviewD.disabled)
			frm.txtPReviewD.focus();
		return;
	}
	if(!dateFld(frm.txtAReviewD,"<%= languageChoose.getMessage("fi.jsp.moduleUpdate.Actualreviewdate")%>"))
		return;
	if(txtType==1){
		if(!mandatoryDateFld(frm.txtPTestD,"<%= languageChoose.getMessage("fi.jsp.moduleUpdate.Plannedtestenddate")%>"))
			return;
	
		if(compareDate(frm.txtPTestD.value,txtPrjStartD)==1){
			window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.moduleUpdate.Planned__test__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__from__~PARAM1_DATE~__to__~PARAM2_DATE~")%>",txtPrjStartD,txtPrjEndD)));
			if(!frm.txtPTestD.disabled)
				frm.txtPTestD.focus();
			return;
	 	}
	 	if(compareDate(frm.txtPTestD.value,txtPrjEndD)==-1){
			window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.moduleUpdate.Planned__test__date__must__be__between__planned__start__date__and__planned__end__date__of__the__project__from__~PARAM1_DATE~__to__~PARAM2_DATE~")%>",txtPrjStartD,txtPrjEndD)));
			if(!frm.txtPTestD.disabled)
				frm.txtPTestD.focus();
			return;
	 	}
		if(!dateFld(frm.txtATestD,"<%= languageChoose.getMessage("fi.jsp.moduleUpdate.Actualtestenddate")%>"))
			return;
	}
	if(!mandatoryDateFld(frm.txtPReleaseD,"<%= languageChoose.getMessage("fi.jsp.moduleUpdate.Plannedreleasedate")%>"))
		return;
	if(!dateFld(frm.txtRPReleaseD,"<%= languageChoose.getMessage("fi.jsp.moduleUpdate.Replannedreleasedate")%>"))
		return;
	if(txtType==1){
	    if (frm.txtRPReleaseD.value.length>0){
	    	if(frm.txtRPReleaseD.value.length>0 &&compareDate(frm.txtRPReleaseD.value,frm.txtPTestD.value)==1){
             	window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Re__planned__release__date__must__be__equal__or__after__planned__test__end__date__~PARAM1_DATE~")%>",frm.txtPTestD.value)));
				if(!frm.txtRPReleaseD.disabled)
					frm.txtRPReleaseD.focus();
				return;
			
			}
		}
		else{
			if(compareDate(frm.txtPReleaseD.value,frm.txtPTestD.value)==1){
				window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Planned__release__date__must__be__equal__or__after__planned__test__end__date__~PARAM1_DATE~")%>",frm.txtPTestD.value)));
				if(!frm.txtPReleaseD.disabled)
					frm.txtPReleaseD.focus();
				return;
			}
		}
	}
	if(txtType==1){
	    if (frm.txtRPReleaseD.value.length>0){
	    	if(frm.txtRPReleaseD.value.length>0 &&compareDate(frm.txtRPReleaseD.value,frm.txtPReviewD.value)==1){
				window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Re__planned__release__date__must__be__equal__or__after__planned__review__end__date__~PARAM1_DATE~")%>",frm.txtPReviewD.value)));
				if(!frm.txtRPReleaseD.disabled)
					frm.txtRPReleaseD.focus();
				return;
			
			}
		}
		else{
			if(compareDate(frm.txtPReleaseD.value,frm.txtPReviewD.value)==1){
				window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Planned__release__date__must__be__equal__or__after__planned__review__end__date__~PARAM1_DATE~")%>",frm.txtPReviewD.value)));
				if(!frm.txtPReleaseD.disabled)
					frm.txtPReleaseD.focus();
				return;
			}
		}
	}
		
   
         <%if(!modun.isNormal){%>
		if (frm.txtRPReleaseD.value.length >0){
			     if(frm.txtRPReleaseD.value.length>0 &&compareDate(frm.txtRPReleaseD.value,frm.txtPReviewD.value)==1){
				 	window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Re__planned__release__date__must__be__equal__or__after__planned__review__end__date__~PARAM1_DATE~")%>",frm.txtPReviewD.value)));
					if(!frm.txtRPReleaseD.disabled)
						frm.txtRPReleaseD.focus();
					return;
				 }
		}
		else{
			if(compareDate(frm.txtPReleaseD.value,frm.txtPReviewD.value)==1){
				window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Planned__release__date__must__be__equal__or__after__planned__review__end__date__~PARAM1_DATE~")%>",frm.txtPReviewD.value)));
				if(!frm.txtPReleaseD.disabled)
					frm.txtPReleaseD.focus();
				return;
			}
	    }
	<%}%>
			
	 if(compareDate(frm.txtPReleaseD.value,txtPrjEndD)==-1){
		window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Planned__release__date__must__be__equal__or__after__planned__review__end__date__~PARAM1_DATE~")%>",txtPrjEndD)));
		if(!frm.txtPReleaseD.disabled)
			frm.txtPReleaseD.focus();
		return;
	}
	if(frm.txtRPReleaseD.value.length>0 && compareDate(frm.txtRPReleaseD.value,txtPrjEndD)==-1){
		window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.moduleUpdate.Re__planned__release__date__must__be__before__planned__end__date__of__the__project~PARAM1_DATE~")%>",txtPrjEndD)));
		if(!frm.txtRPReleaseD.disabled)
			frm.txtRPReleaseD.focus();
		return;
	}
	if(dateFld(frm.txtAReleaseD,"<%= languageChoose.getMessage("fi.jsp.moduleUpdate.Actualreleasedate")%>"))
	//if(maxLength(frm.txtReviewer,"<%= languageChoose.getMessage("fi.jsp.moduleUpdate.Reviewers")%>",200))
	//if(maxLength(frm.txtApprover,"<%= languageChoose.getMessage("fi.jsp.moduleUpdate.Approvers")%>",200))
		frm.submit();			
}
 </SCRIPT> 
</BODY>
</HTML>
