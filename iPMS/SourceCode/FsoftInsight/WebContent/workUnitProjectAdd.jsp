<%@page import="com.fms1.tools.*"%>
<%@page import="com.fms1.infoclass.*, com.fms1.infoclass.group.*, com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<TITLE>workUnitProjectAdd.jsp</TITLE>
	<LINK rel="stylesheet" href = "stylesheet/fms.css" type = "text/css">
	<SCRIPT language = "JavaScript" src="jscript/javaFns.js"></SCRIPT>
	<SCRIPT language = "javascript"><%@ include file = "javaFns.jsp"%></SCRIPT>
	<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
	<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
	<SCRIPT src="jscript/ajax.js"></SCRIPT>
	<TITLE>workUnitProjectAdd.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class = "BD" onLoad="loadAdminMenu();">
<BR>
<FORM name="frm" method="POST" onsubmit="return doSave();">
<%
	String err=(String)session.getAttribute("addWUerr");
	if(err!=null&&err!="") {
%>
		<p class="ERROR"><%=err%></p>
<%
		session.setAttribute("addWUerr","");
	}

    Vector grpVector = (Vector)session.getAttribute("WUgrpVector");
    UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
    String userGroupInfo = userLoginInfo.group;

	// store workunit which user input
	ProjectInfo projectInfo = new ProjectInfo();
	if (request.getAttribute("WorkUnitProjectInfo") != null){
		projectInfo = (ProjectInfo) request.getAttribute("WorkUnitProjectInfo");
		userGroupInfo = projectInfo.getGroupName();
	}
	String strMoreSelected = "";
	if (request.getParameter("MoreStatus") == null){
		strMoreSelected = userGroupInfo;
	}
%>
<DIV align="left">
<TABLE cellspacing="1" class = "Table" width="95%">
	<CAPTION align="left" class = "TableCaption"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.AddnewWorkUnitProject")%></CAPTION>
	<TBODY>
		<TR>
			<TD class = "ColumnLabel"><%= languageChoose.getMessage("fi.jsp.workUnit.Group")%>* </TD>
			<TD class = "CellBGR3">
				<SELECT name="cboGroup" class = "COMBO" style='width:100'>
					<%for (int i = 0; i< grpVector.size(); i++) {
						WorkUnitInfo wuInfoTemp = (WorkUnitInfo) grpVector.get(i);
						String strText = wuInfoTemp.workUnitName;
		        		out.write("<OPTION value='" + strText + "'" + (strText.equals(userGroupInfo) ? "selected" : "") + ">" + strText + "</OPTION>");
					}%>
				</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectCode")%>* </TD>
			<TD class = "CellBGR3"><INPUT type="text" name="txtProjectCode" value="<%=projectInfo.getProjectCode()%>" maxlength="50" size="50"></TD>
		</TR>
		<TR>
			<TD class = "ColumnLabel"><%= languageChoose.getMessage("fi.jsp.workUnitAdd.WorkUnitProjectName")%>* </TD>
			<TD class = "CellBGR3"><INPUT type="text" name="txtProjectName" value="<%=projectInfo.getProjectName()%>" maxlength="50" size="50"></TD>
		</TR>
		<TR>
			<TD class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectCustomer")%></TD>
			<TD class = "CellBGR3"><INPUT type="text" name="txtCustomer" value="<%=projectInfo.getCustomer()%>" maxlength="50" size="50"></TD>
		</TR>
		<TR>
			<TD class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectSecondaryCustomer")%></TD>
			<TD class = "CellBGR3"><INPUT type="text" name="txt2NDCustomer" value="<%=projectInfo.getSecondCustomer()%>" maxlength="50" size="50"></TD>
		</TR>
		<TR>
			<TD class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectCategory")%>* </TD>
			<TD class = "CellBGR3">
				<SELECT name = "cboLifeCycle" class = "COMBO" style='width:100'>
					<OPTION value = "0"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.Development")%></OPTION>
					<OPTION value = "1"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.Maintenance")%></OPTION>
					<OPTION value = "2"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.Others")%></OPTION>
				</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectType")%>* </TD>
			<TD class = "CellBGR3">
				<SELECT name="cboType" class = "COMBO" style='width:100'>
					<OPTION value = "0"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.External")%></OPTION>
					<OPTION value = "8"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.Internal")%></OPTION>
					<OPTION value = "9"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.Public")%></OPTION>
				</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectRank")%></TD>
			<TD class = "CellBGR3">
				<SELECT name="cboRank" class = "COMBO" style='width:100'>
					<OPTION value=""><%=languageChoose.getMessage("fi.jsp.workUnitAdd.NA")%></OPTION>
					<OPTION value="?"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.NR")%></OPTION>
					<OPTION value="A"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.A")%></OPTION>
					<OPTION value="B"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.B")%></OPTION>
					<OPTION value="C"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.C")%></OPTION>
					<OPTION value="D"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.D")%></OPTION>
				</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectManager")%>* </TD>
            <TD class="CellBGR3">
            	<INPUT name="strAccountName" size="30" type="text" value="<%=projectInfo.getLeader()%>"/>
            	<INPUT type="button" class="BUTTON" value="Search" onclick="onCheckAccount(event)"> <BR>
	            <INPUT type="radio" name="rdAccountName" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.issueAddnew.Account")%>       <INPUT
				type="radio" name="rdAccountName" value="Name"><%=languageChoose.getMessage("fi.jsp.issueAddnew.Name")%><BR>
            </TD>
		</TR>
		<TR>
			<TD class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectPlannedStartDate")%>* </TD>
			<TD class = "CellBGR3"><INPUT type="text" maxlength="9" name="txtPlanStartDate" size="9" value = "<%=("N/A".equals(CommonTools.dateFormat(projectInfo.getPlanStartDate()))? "" : CommonTools.dateFormat(projectInfo.getPlanStartDate()))%>"> <%=languageChoose.getMessage("fi.jsp.workUnitAdd.DatetimeFormat")%></TD>
		</TR>
		<TR>
			<TD class = "ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectPlannedEndDate")%>* </TD>
			<TD class = "CellBGR3"><INPUT type="text" maxlength="9" name="txtPlanEndDate" size="9" value = "<%=("N/A".equals(CommonTools.dateFormat(projectInfo.getBaseFinishDate()))? "" : CommonTools.dateFormat(projectInfo.getBaseFinishDate()))%>"> <%=languageChoose.getMessage("fi.jsp.workUnitAdd.DatetimeFormat")%></TD>
		</TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectScopeObject")%></TD>
            <TD class="CellBGR3"><TEXTAREA rows="10" cols="70" name="txtScopeObjective"><%=projectInfo.getScopeAndObjective()%></TEXTAREA>
            </TD>
        </TR>
        <TR>
        	<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectStatus")%>* </TD>
        	<TD class="CellBGR3">
        		<SELECT name = "cboStatus" class="COMBO">
        			<OPTION value="3"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectStatusTentative")%></OPTION>
        			<OPTION value="0"><%=languageChoose.getMessage("fi.jsp.workUnitAdd.ProjectStatusOngoing")%></OPTION>
        		</SELECT>
        	</TD>
        </TR>
	</TBODY>
</TABLE>
</DIV>
<BR>
<P align="left">
	<INPUT type="button"  name="OK" value="<%=languageChoose.getMessage("fi.jsp.workUnitAdd.OK")%>" onclick="doSave()" class="BUTTON">&nbsp;
	<INPUT type="button"  name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.workUnitAdd.Cancel")%>" onclick="doBack()" class="BUTTON">
</p>
</FORM>
<SCRIPT language="javascript">
	frm.txtProjectCode.focus();
//do execute filltering User ---Start
<%
	if (request.getAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE) != null){
%>
		alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.WorkUnitNameAlreadyExist")%>");
		frm.txtProjectCode.focus();
<%
		request.removeAttribute(StringConstants.WORK_UNIT_ERROR_MESSAGE);
	}
%>
<%
	if (request.getAttribute(StringConstants.FILLTER_USER_ERROR) != null){
%>
		alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.InvalidAccount")%>");
		frm.strAccountName.focus();
<%
		request.removeAttribute(StringConstants.FILLTER_USER_ERROR);
	}
%>

	function onCheckAccount(event){
		if (trim(frm.strAccountName.value) == "") {
	  			window.alert("<%=languageChoose.getMessage("fi.jsp.issueAddnew.Ownercannotnotbeempty")%>");
	  			frm.strAccountName.focus();
	  	}
	    var rd = document.forms['frm'].rdAccountName;
	    var rdAccountName;
        for(var i = 0; i < rd.length; i++){
		  if(rd[i].checked){
		  	rdAccountName = rd[i].value;
		  	break;
		  }         
        }
        var url = "Fms1Servlet?reqType=7000" + "&Account=" + frm.strAccountName.value + "&Type=" + rdAccountName;
		javascript:ajax_showOptions(document.forms['frm'].strAccountName, url, '', event);
	}
//do execute filltering User ---End
	function doSave(){
  		if(maxLength(frm.txtScopeObjective,"Scope and Objective",4000)==false){
  	 		frm.txtScopeObjective.focus();
  		 	return;
  	 	} 
  		
  		if (! validateAddProjectForm()){
  			return false;
  		}
  		frm.action = "Fms1Servlet?reqType=<%=Constants.ADDNEW_WORK_UNIT_PROJECT%>";
  		frm.submit();
  		return true;
	}
	function doBack(){
		doIt(<%=Constants.GET_WORK_UNIT_LIST%>);
	}

	function validateAddProjectForm(){
 		var	errorMessage = "E R R O R (s) :  \n\n";
  		if (trim(frm.txtProjectCode.value) == "") {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.YouMustInputProjectCode")%>");
  			frm.txtProjectCode.focus();
  			return false;
  		}
  		if (trim(frm.txtProjectName.value) == "") {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.YouMustInputProjectName")%>");
  			frm.txtProjectName.focus();
  			return false;
  		}
  		//variable verifies datetime type
  		var _planStartDate = trim(frm.txtPlanStartDate.value);
  		var _planEndDate = trim(frm.txtPlanEndDate.value);
  		if (_planStartDate == "") {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.YouMustInputPlanStartDate")%>");
  			frm.txtPlanStartDate.focus();
  			return false;
  		}
  		if (!isDate(_planStartDate)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.InvalidPlanStartDate")%>");
  			frm.txtPlanStartDate.focus();
  			return false;
  		}
  		if (_planEndDate == "") {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.YouMustInputPlanEndDate")%>");
  			frm.txtPlanEndDate.focus();
  			return false;
  		}
  		if (!isDate(_planEndDate)) {
  			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.InvalidPlanEndDate")%>");
  			frm.txtPlanEndDate.focus();
  			return false;
  		}

  		if (compareDate(_planStartDate, _planEndDate) != 1){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.workUnitAdd.PlanStartDateMustBeBeforePlanEndDate")%>");
  			frm.txtPlanEndDate.focus();
  			return false;
  		}
  		return true;
	}
	var objToHide=new Array(frm.cboLifeCycle,frm.cboType, frm.cboRank);
</SCRIPT>
</BODY>
</HTML>