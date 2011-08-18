<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.web.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.*,java.util.Calendar,com.fms1.tools.* " contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv="Pragma" content="no-cache">
	<META http-equiv="Cache-Control" content="no-cache">
	<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
	<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/pcal.css">
	<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
	<SCRIPT language="JavaScript">
	        <%@ include file="../javaFns.jsp"%>
	</SCRIPT>
	<SCRIPT src='jscript/popcalendar.js'></SCRIPT>
	<TITLE>ResourceAllocation.jsp</TITLE>
</HEAD>
<%
	int right = Security.securiPage("Human Resource",request,response);
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onkeypress="javascript:if (window.event.keyCode==13) doView()" onload="<%=CommonTools.getMnuFunc(session)%>">
<FORM name="frm" target="main" method="POST">
<%
	Vector projectList = (Vector) session.getAttribute("hrProjectList");
	HumanResourceInfo resourceBean = (HumanResourceInfo) session.getAttribute("ResourceBean");
	Vector resourceList = (Vector) session.getAttribute("ResourceInfoList");
	
	String strGroup, strFromDate, strToDate;
	long projectId;
	int status = resourceBean.getStatus();
	strGroup = resourceBean.getUserGroup();
	projectId = resourceBean.getProjectId();
	strFromDate = "";
	strToDate = "";
	if (resourceBean.getFromDate() != null){
		strFromDate = resourceBean.getFromDate();
	}
	if (resourceBean.getToDate() != null) {
		strToDate = resourceBean.getToDate();
	}
	String strPage = request.getParameter("iPage");
	int currentPage;
	if(request.getParameter("pageCombobox") != null) {
		currentPage = Integer.parseInt((String)request.getParameter("pageCombobox"));
	}
	else {
		currentPage = 1;
	}
	
	int iPage;
	if (ConvertString.isNumber(strPage)) {
		iPage = Integer.parseInt(strPage);
	}
	else {
		iPage = 1;
	}
	if (iPage > currentPage) {
		currentPage = iPage;
	}
	else {
		iPage = currentPage;
	}
	int numberResource = resourceList.size() - 1;
	Double totalUserEffort;
	try{
		totalUserEffort = (Double)resourceList.get(numberResource); //the last element of resourceList is total User Effort
	}catch(Exception e){
		totalUserEffort = null;
	}
	int pageCount;
	if (((numberResource) % Constants.GROUP_CALENDAR_EFFORT_MAX_NUMBER) == 0){
		pageCount = numberResource / Constants.GROUP_CALENDAR_EFFORT_MAX_NUMBER;
	}
	else {
		pageCount = numberResource / Constants.GROUP_CALENDAR_EFFORT_MAX_NUMBER + 1;
	}
	if (currentPage > pageCount && pageCount >0){
		currentPage = 1;
	}
	String pageCombobox = "";

	if(pageCount > 0) {
		pageCombobox += "<select name=\"pageCombobox\" onChange=\"doSelectCombo();\" class=\"COMBO\">";
		for(int i = 1; i <= pageCount; i++) {
			if(currentPage == i){
				pageCombobox += "<option value='"+i+"' selected>"+i+"</option>";
			}
			else {
				pageCombobox += "<option value='"+i+"'>"+i+"</option>";
			}
		}
		pageCombobox += "</select>";
	}

%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.HumanResource.GroupResourceAllocation")%></P>
<BR>
<TABLE cellspacing="3" width="95%">
	<TR class="NormalText">
		<TD width="100"><%=languageChoose.getMessage("fi.jsp.HumanResource.ProjectStatus")%></TD>
		<TD width="100">
			<SELECT name="cboStatus" class="COMBO" style='width:100' onchange="onChangeStatus()">
				<OPTION value ="-1" <%=((status==-1)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.StatusAll")%></OPTION>
            	<OPTION value="0"<%=((status==0)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.StatusOngoing")%></OPTION>
				<OPTION value="1"<%=((status==1)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.StatusClosed")%></OPTION>
            	<OPTION value="2"<%=((status==2)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.StatusCancelled")%></OPTION>
            	<OPTION value="3"<%=((status==3)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.StatusTentative")%></OPTION>
			</SELECT>
		</TD>
		<!-- FROM DATE  -->
		<TD width="100"><%=languageChoose.getMessage("fi.jsp.HumanResource.FromDate")%></TD>
		<TD width="122">
			<INPUT type="text" name="FromDate" size="9" maxlength="9" value="<%=strFromDate%>">
			<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showFromDate();">
		</TD>
	</TR>
	<TR class="NormalText">
		<!-- PROJECT  -->
		<TD width="97"><%=languageChoose.getMessage("fi.jsp.HumanResource.Project")%></TD>
		<TD width="200">
			<SELECT size="1" name="cboProject" class="COMBO" style='width:150'>
			</SELECT>
		</TD>
		<TD align="left"><%=languageChoose.getMessage("fi.jsp.HumanResource.ToDate")%></TD>
		<TD>
			<INPUT type="text" name="ToDate" size="9" maxlength="9" value="<%=strToDate%>">
			<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showToDate();">
		</TD>
		<TD>
			<INPUT type="button" name="View" value="<%=languageChoose.getMessage("fi.jsp.HumanResource.View")%>" class="BUTTON" onclick="doView()">
		</TD>
	</TR>
	<TR class="NormalText">
		<TD  width="97"><%=languageChoose.getMessage("fi.jsp.HumanResource.UserBy")%></TD>
		<TD width="200">
			<SELECT name="cboUserBy" class="COMBO">
				<OPTION value="<%=Constants.HR_USER_BY_ALL%>" <%=(resourceBean.getUserBy() == Constants.HR_USER_BY_ALL) ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.HumanResource.All")%></OPTION>
				<OPTION value="<%=Constants.HR_USER_BY_GROUP%>" <%=(resourceBean.getUserBy() == Constants.HR_USER_BY_GROUP) ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.HumanResource.Group")%></OPTION>
				<OPTION value="<%=Constants.HR_USER_BY_PROJECT%>" <%=(resourceBean.getUserBy() == Constants.HR_USER_BY_PROJECT) ? "selected" : ""%>><%=languageChoose.getMessage("fi.jsp.HumanResource.Project")%></OPTION>
			</SELECT>
		</TD>
		<TD colspan="2">
			<FONT class="lableDate" color="blue"><%=languageChoose.getMessage("fi.jsp.HumanResource.DateFormat")%><%=languageChoose.getMessage("fi.jsp.HumanResource.DateType")%></FONT>
		</TD>
	</TR>
</TABLE>
<BR>
<DIV align="left">
	<TABLE class="Table" cellspacing="1" width="100%">
		<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.HumanResource.ResourceAllocationOf")%><%=strGroup%></CAPTION>
		<TBODY>
			<%if (numberResource > 0){%>
		        <TR class="TableLeft">
					<TD colspan="11" align="right">
						<%if(pageCombobox != "")%>
							<%="Page " + pageCombobox%><%=" of " + pageCount%>
						&nbsp;
						<%if(currentPage > 1){%>
							<A href="Fms1Servlet?reqType=<%=Constants.GROUP_RESOURCE_ALLOCATION%>&iPage=<%=currentPage-1%>"
								class="TableLeft"><%=languageChoose.getMessage("fi.jsp.HumanResource.Back")%>
							</A>
						<%}%> 
						<% if (currentPage < pageCount){%>
							<A href="Fms1Servlet?reqType=<%=Constants.GROUP_RESOURCE_ALLOCATION%>&iPage=<%=currentPage+1%>">
								<%=languageChoose.getMessage("fi.jsp.HumanResource.Next")%>
							</A>
						<%}%>
					</TD>
		        </TR>
			<%}%>
			<TR class="ColumnLabel">
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.ProjectGroup")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.UserGroup")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.ProjectCode")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.ProjectStatus")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.Account")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.WorkingTime")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.BeginAssignment")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.EndAssignment")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.Role")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.ActualCalendarEffort")%></TD>
			</TR>
			<%if (numberResource == 0) {
			%>
			<TR>
				<TD colspan="8">
					<P class = "ERROR"><%=languageChoose.getMessage("fi.jsp.HumanResource.NoResultsMatchesYourCriteria")%></P>
				</TD>
			</TR>
			<%
			} else {
				boolean bl = true;
				String rowStyle = "";
				for(int i = ((currentPage-1)*Constants.GROUP_CALENDAR_EFFORT_MAX_NUMBER); (i < (currentPage*Constants.GROUP_CALENDAR_EFFORT_MAX_NUMBER))&&(i < numberResource); i++) {
					HumanResourceInfo info = (HumanResourceInfo)resourceList.get(i);
					if (bl) {
						rowStyle = "CellBGRnews";
					}
					else {
						rowStyle = "CellBGR3";
					}
					bl = !bl;
			%>
			        <TR class="<%=rowStyle%>">
						<TD><%=ConvertString.toHtml(info.getProjectGroup())%></TD>
			            <TD><%=ConvertString.toHtml(info.getUserGroup())%></TD>
			            <TD><%=ConvertString.toHtml(info.getProjectCode())%></TD>
			            <TD><%=ConvertString.toHtml(info.getProjectStatusName())%></TD>
			            <TD><%=ConvertString.toHtml(info.getUserAccount())%></TD>
			            <TD><%=info.getWorkingTime()%></TD>
			            <TD><%=CommonTools.dateFormat(info.getBeginAssignment())%></TD>
			            <TD><%=CommonTools.dateFormat(info.getEndAssignment())%></TD>
			            <TD><%=ConvertString.toHtml(info.getUserRole())%></TD>
			            <TD><%=info.getUserCalendarEffort()%></TD>
			        </TR>
			<%}%>
					<TR class="TableLeft">
						<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.TotalCalendarEffort")%></TD>
						<TD colspan="8"></TD>
						<% if(totalUserEffort != null) { %>
						<TD><%=CommonTools.formatDouble(totalUserEffort.doubleValue())%></TD>
						<% } else { %>
						<P class = "ERROR"><%=languageChoose.getMessage("fi.jsp.HumanResource.NoResultsMatchesYourCriteria")%></P>
						<% } %>
					</TR>
			<%}%>
		</TBODY>
	</TABLE>
</DIV>
</FORM>
<%if (numberResource > 0 ){%>
<P align="center">
	<INPUT type="button" name="Export" value="<%=languageChoose.getMessage("fi.jsp.HumanResource.Export")%>" class="BUTTON" onclick="doExport()">
</P>
<%}%>
<SCRIPT language="javascript">
var project = new Array();
var lastProjectId = <%=projectId%>
<%
	for (int i = 0; i < projectList.size(); i++) {
		ProjectInfo info = (ProjectInfo) projectList.get(i);
		out.write("project[" + i + "] = new Array(3);");
		out.write("project[" + i + "][0]=" + info.getProjectId() + ";");
		out.write("project[" + i + "][1]='" + ConvertString.toJScript(info.getProjectCode()) + "';");
		out.write("project[" + i + "][2]=" + info.getStatus() + ";\n");
	}
	
%>

	function checkData(){
		var fromDate = trim(frm.FromDate.value);
		var toDate = trim(frm.ToDate.value);
		if (fromDate != "" && !isDate(fromDate)){
			window.alert("<%=languageChoose.getMessage("fi.jsp.HumanResource.FromDateIsInvalid")%>");
			frm.FromDate.focus();
			return false;
		}
		if (toDate != "" && !isDate(toDate)){
			window.alert("<%=languageChoose.getMessage("fi.jsp.HumanResource.ToDateIsInvalid")%>");
			frm.ToDate.focus();
			return false;
		}
		if (toDate!="" && fromDate != "" && compareDate(fromDate, toDate) == -1){
  			window.alert("<%=languageChoose.getMessage("fi.jsp.HumanResource.FromDateMustBeBeforeToDate")%>");
  			frm.FromDate.focus();
  			return false;
  		}

		if (frm.cboProject.options.length < 1){
			window.alert("<%=languageChoose.getMessage("fi.jsp.HumanResource.DonotHaveAnyProjectForThisType")%>");
			return false;
		}
		return true;
	}
	function doView(){
		if (checkData()){
			frm.action = "Fms1Servlet?reqType=<%=Constants.GROUP_RESOURCE_ALLOCATION%>";
			frm.submit();
		}
	}
	function doSelectCombo(){
		frm.action = "Fms1Servlet?reqType=<%=Constants.GROUP_RESOURCE_ALLOCATION%>&iCombo=<%=currentPage%>";
		frm.submit();
	}
	
	function doExport(){
		if (checkData()){
			frm.action = "Fms1Servlet?reqType=<%=Constants.GROUP_RESOURCE_ALLOCATION_EXPORT%>";
			frm.submit();
		}
	}
	function onChangeStatus(){
		while (frm.cboProject.options.length > 0 ){
			frm.cboProject.options[0] = null;
		}
		var count = 0;
		if (frm.cboStatus.value != -1){
			for (var i = 0; i < project.length; i++){
				if (project[i][2] == frm.cboStatus.value){
					count++;
					if (count ==1){
						appendOption(frm.cboProject, 0,
	                		"All", frm.cboStatus.value,
		                	true);
					}
					appendOption(frm.cboProject, project[i][0],
                		project[i][1], project[i][2],
	                	(lastProjectId == project[i][0]));
				}
			}
		}
		else {
			for (var i = 0; i < project.length; i++){
				count++;
				if (count ==1){
					appendOption(frm.cboProject, 0,
                		"All", frm.cboStatus.value,
	                	true);
				}
				appendOption(frm.cboProject, project[i][0],
               		project[i][1], project[i][2],
                	(lastProjectId == project[i][0]));
			}
		}
	}
	function init(){
		onChangeStatus();
	}
	init();
	var objToHide=new Array(frm.cboProject,frm.cboStatus, frm.cboUserBy);
	function showFromDate(){
		showCalendar(frm.FromDate, frm.FromDate, "dd-mmm-yy",null,1,-1,-1,false);
	}
	
	function showToDate(){		
		showCalendar(frm.ToDate, frm.ToDate, "dd-mmm-yy",null,1,-1,-1,false);
	}
</SCRIPT>
</BODY>
</HTML>