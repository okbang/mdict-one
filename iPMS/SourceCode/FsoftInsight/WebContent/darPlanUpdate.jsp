<%@page import="com.fms1.tools.*, com.fms1.infoclass.*, com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>darPlanUpdate.jsp</TITLE>

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	Vector darVt = (Vector)session.getAttribute("dar");
	String vtIDstr = request.getParameter("vtID");
	int vtID = Integer.parseInt(vtIDstr);
	int caller = Integer.parseInt((String)session.getAttribute("caller"));
	DARPlanInfo darPlanInfo = (DARPlanInfo)darVt.get(vtID);
	Vector userList = (Vector)session.getAttribute("userList");
	String doer = (String) session.getAttribute("doer");
	for (int i = 0; i < userList.size(); i++) {
		AssignmentInfo aInfo = (AssignmentInfo) userList.elementAt(i);
		if ((aInfo!=null) && ((aInfo.account).equals(doer))) {
			userList.removeElementAt(i);
			userList.add(0, aInfo);
		}
	}
	
	ProjectDateInfo prjDateInfo = (ProjectDateInfo)session.getAttribute("prjDateInfo");
    
    String callPage;
    String disab = "";
    String callParam = "";
    String focus;
    // Determine what form is returned (parent form that called this page, return to JSP instead of call to servlet)
    switch (caller) {
        case Constants.MILESTONE_CALLER:
           StageInfo stageInfo = (StageInfo) session.getAttribute("stageInfo");
           callParam="&stageID="+stageInfo.milestoneID;
           callPage="'milestone.jsp" + callParam + "#darplan'";
           disab="disabled";
           focus="";
           break;
        case Constants.QUALITY_OBJECTIVE_CALLER:
           callPage= "'qualityObjective.jsp#darplan'";
           disab="";
           focus="frm.txtItem.focus();";
           break;
        default:
           callPage= "'postMortemReport.jsp#darplan'";
           disab="disabled";
           focus="";
    }
    /*
    if (caller == Constants.MILESTONE_CALLER){
    	StageInfo stageInfo = (StageInfo) session.getAttribute("stageInfo");
    	callParam="&stageID="+stageInfo.milestoneID;
    	callPage="'"+Constants.MILESTONE_GET_PAGE+callParam+ "#defectprevention" + "'";
    	disab="disabled";
    	focus="";
    }
    else {
    	callPage= "'" + Integer.toString(caller==Constants.QUALITY_OBJECTIVE_CALLER ?
    	        Constants.GET_QUALITY_OBJECTIVE_LIST: Constants.GET_POST_MORTEM)
    	        +  "#defectprevention" + "'";
    	disab=(caller==Constants.QUALITY_OBJECTIVE_CALLER? "":"disabled");
    	focus=(caller==Constants.QUALITY_OBJECTIVE_CALLER?"frm.txtItem.focus();":"");
    }
    */

    if (userList.size() == 0) {
      focus = "";
    }
%>
<BODY class="BD" onLoad="loadPrjMenu();<%=focus%>">
<P class="TITLE">DAR plan</P>
<% 
	if (userList.size() == 0) {%>
		<P class="ERROR"><%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Errormessage")%></P>
		<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Back")%>" onclick=doIt(<%=callPage%>);>
<%  } else {%>
<FORM method="POST" name="frm" action="Fms1Servlet?reqType=<%=Constants.UPDATE_DARPLAN+callParam%>#darplan">
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Caption")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Item")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="50" type="text" maxlength="100" name="txtItem" value="<%=darPlanInfo.darItem%>"  <%=disab%>></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" nowrap="nowrap"><%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Doer")%>*</TD>
            <TD class="CellBGRnews"><SELECT name="cmbConductor" class="COMBO" <%=disab%> >
                <%for(int i=0;i<userList.size();i++){
		            	AssignmentInfo assInfo = (AssignmentInfo) userList.elementAt(i);		            	
               		%><OPTION value="<%=assInfo.devID %>"><%=assInfo.account%> - <%=assInfo.devName%></OPTION>
                <%}%>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160" nowrap="nowrap"><%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Targetdate")%>*</TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtPStartD" class="numberTextBox" value="<%=CommonTools.dateFormat(darPlanInfo.planDate)%>" <%=disab%>>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPStartD()'>            
            	(DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160" nowrap="nowrap"><%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Actualdate")%></TD>
            <TD class="CellBGRnews"><INPUT size="9" type="text" maxlength="9" name="txtPEndD" class="numberTextBox" value="<%=(CommonTools.dateFormat(darPlanInfo.actualDate)=="N/A")?"":CommonTools.dateFormat(darPlanInfo.actualDate)%>">
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPEndD()'>	
	            (DD-MMM-YY)</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" ><%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Cause")%></TD>
            <TD class="CellBGRnews"><TEXTAREA rows="5" cols="60" name="txtCause"><%=darPlanInfo.darCause%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<BR>
<INPUT type="hidden" name="darPlanID" value="<%=darPlanInfo.darPlanID%>">
<INPUT type="button" name="btnOk" value="<%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Ok")%>" class="BUTTON" onclick="doAction(this)">
<INPUT type="button" class="BUTTON" name="btnCancel" value="<%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Back")%>" onclick=jumpURL(<%=callPage%>);>
</FORM>
<SCRIPT language="javascript">
var txtPrjStartD="<%=(prjDateInfo.startD== null)?"":CommonTools.dateFormat(prjDateInfo.startD)%>";
var txtPrjEndD="<%=(prjDateInfo.endD== null)?"":CommonTools.dateFormat(prjDateInfo.endD)%>";
function doAction(button)
{
	if (button.name=="btnOk") {	  	
		if(!mandatoryFld(frm.txtItem,"<%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Item")%> ")){
	  	  	return;  			  	  		
	  	}
	  	if(!maxLength(frm.txtItem,"<%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Cause")%> ", 100)){
	  	  	return;
	  	} 	  	
	  	  	
		if(!mandatoryFld(frm.txtPStartD,"<%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Targetdate")%> ")){
	  	  	return;  			  	  		
	  	}
		if(!dateFld(frm.txtPStartD, "<%=languageChoose.getMessage("fi.jsp.FinanAdd.Invaliddateformat")%> ")) {
	  		return;
	  	}
	  	if(compareDate(frm.txtPStartD.value,txtPrjStartD) == 1){
			window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Target_date_must_between_planned_start_date_and_re_planned_end_date_of_project_from_~PARAM1_DATE~_to_~PARAM2_DATE~")%> ",txtPrjStartD,txtPrjEndD)));
			frm.txtPStartD.focus();
			return;
	 	}
	 	if(compareDate(frm.txtPStartD.value,txtPrjEndD) == -1){
			window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Target_date_must_between_planned_start_date_and_re_planned_end_date_of_project_from_~PARAM1_DATE~_to_~PARAM2_DATE~")%> ",txtPrjStartD,txtPrjEndD)));
			frm.txtPStartD.focus();
			return;
	 	} 
	 	if (trim(frm.txtPEndD.value)!="") { 		 	 		 	 		 			 	
			if(!dateFld(frm.txtPEndD, "<%=languageChoose.getMessage("fi.jsp.FinanAdd.Invaliddateformat")%> ")) {
		  		return;
		  	}
		  	if(compareDate(frm.txtPEndD.value,txtPrjStartD) == 1){
				window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Actual_date_must_be_between_planned_start_date_of_activity_and_re_planned_end_date_of_project_from_~PARAM1_DATE~_to_~PARAM2_DATE~")%>",txtPrjStartD,txtPrjEndD)));
				frm.txtPEndD.focus();
				return;
		 	}
		 	if(compareDate(frm.txtPEndD.value,txtPrjEndD) == -1){
				window.alert(getParamText(new Array("<%=languageChoose.getMessage("fi.jsp.darPlanAdd.Actual_date_must_be_between_planned_start_date_of_activity_and_re_planned_end_date_of_project_from_~PARAM1_DATE~_to_~PARAM2_DATE~")%>",txtPrjStartD,txtPrjEndD)));
				frm.txtPEndD.focus();
				return;
		 	} 			 				 			 		
	 	} 	 
	 	
	 	if(!maxLength(frm.txtCause,"<%=languageChoose.getMessage("fi.jsp.darPlanUpdate.Cause")%> ", 300)){
  		  	return;
  	  	}	 	  	 	    		  	  		  	    		
  	  	frm.submit();  		 	
  	}  				 	  
}

	function showPStartD(){
		showCalendar(frm.txtPStartD, frm.txtPStartD, "dd-mmm-yy",null,1,-1,-1,true);
	}
	function showPEndD(){
		showCalendar(frm.txtPEndD, frm.txtPEndD, "dd-mmm-yy",null,1,-1,-1,true);
	}
</SCRIPT> 
<%}%> 
</BODY>
</HTML>