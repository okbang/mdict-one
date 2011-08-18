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
	<TITLE>calendarEffort.jsp</TITLE>
</HEAD>
<%
	int right = Security.securiPage("Human Resource",request,response);
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onkeypress="javascript:if (window.event.keyCode==13) doView()" onload="<%=CommonTools.getMnuFunc(session)%>">
<FORM name="frm" target="main" method="POST">
<%
	Vector calendarEffort = (Vector) session.getAttribute("GroupCalendarEffort");
	Vector projectList = (Vector) session.getAttribute("GroupProjectList");
	HumanResourceInfo resourceBean = (HumanResourceInfo)session.getAttribute("CalendarEffortBean");
	String strGroup = "", strFromDate = "", strToDate = "";
	int status = -1;
	int projectType = -1;
	long projectId = resourceBean.getProjectId();
	strGroup = resourceBean.getUserGroup();
	status = resourceBean.getStatus();
	projectType = resourceBean.getProjectType();

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
	int numberProject = calendarEffort.size() -1;
	Double totalCalendar;
	try{
		totalCalendar = (Double)calendarEffort.get(numberProject);
	}catch(Exception e){
		totalCalendar = null;
	}
	int pageCount;
	if (((numberProject) % Constants.GROUP_CALENDAR_EFFORT_MAX_NUMBER) == 0){
		pageCount = (numberProject) / Constants.GROUP_CALENDAR_EFFORT_MAX_NUMBER;
	}
	else {
		pageCount = (numberProject) / Constants.GROUP_CALENDAR_EFFORT_MAX_NUMBER + 1;
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
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.HumanResource.GroupCalendarEffort")%></P>
<BR>	
<TABLE cellspacing="3" width="95%">
	<TR class="NormalText">
		<TD width="97"><%=languageChoose.getMessage("fi.jsp.HumanResource.ProjectStatus")%></TD>
		<TD width="200">
			<SELECT name="cboStatus" class="COMBO" style='width:100' onchange="onChange()">
				<OPTION value ="-1" <%=((status==-1)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.StatusAll")%></OPTION>
            	<OPTION value="<%=ProjectInfo.STATUS_ONGOING%>"<%=((status==ProjectInfo.STATUS_ONGOING)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.StatusOngoing")%></OPTION>
				<OPTION value="<%=ProjectInfo.STATUS_CLOSED%>"<%=((status==ProjectInfo.STATUS_CLOSED)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.StatusClosed")%></OPTION>
            	<OPTION value="<%=ProjectInfo.STATUS_CANCELLED%>"<%=((status==ProjectInfo.STATUS_CANCELLED)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.StatusCancelled")%></OPTION>
            	<OPTION value="<%=ProjectInfo.STATUS_TENTATIVE%>"<%=((status==ProjectInfo.STATUS_TENTATIVE)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.StatusTentative")%></OPTION>
			</SELECT>
		</TD>
		<!-- FROM DATE  -->
		<TD width="97"><%=languageChoose.getMessage("fi.jsp.HumanResource.FromDate")%></TD>
		<TD>
			<INPUT type="text" name="FromDate" size="9" maxlength="9" value="<%=strFromDate%>">
			<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showFromDate();">
			<FONT class="lableDate" color="blue"><%=languageChoose.getMessage("fi.jsp.HumanResource.DateType")%></FONT>
		</TD>
	</TR>
	<TR class="NormalText">
		<TD align="left" width="97"><%=languageChoose.getMessage("fi.jsp.HumanResource.ProjectType")%></TD>
		<TD width="200">
			<SELECT name="cboProjectType" class="COMBO" style='width:100' onchange="onChange()">
				<OPTION value ="-1" <%=((projectType==-1)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.ProjectTypeAll")%></OPTION>
            	<OPTION value="<%=ProjectInfo.PROJECT_INTERNAL%>"<%=((projectType==ProjectInfo.PROJECT_INTERNAL)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.ProjectTypeInternal")%></OPTION>
				<OPTION value="<%=ProjectInfo.PROJECT_EXTERNAL%>"<%=((projectType==ProjectInfo.PROJECT_EXTERNAL)?"selected":"")%>><%=languageChoose.getMessage("fi.jsp.HumanResource.ProjectTypeExternal")%></OPTION>
			</SELECT>
		</TD>
		<!-- TO DATE  -->
		<TD width="97" align="left"><%=languageChoose.getMessage("fi.jsp.HumanResource.ToDate")%></TD>
		<TD >
			<INPUT type="text" name="ToDate" size="9" maxlength="9" value="<%=strToDate%>">
			<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick="showToDate();">
			<FONT class="lableDate" color="blue"><%=languageChoose.getMessage("fi.jsp.HumanResource.DateType")%></FONT>
		</TD>
	</TR>
	<TR class="NormalText">
		<!-- PROJECT  -->
		<TD align="left" width="97"><%=languageChoose.getMessage("fi.jsp.HumanResource.Project")%></TD>
		<TD width="200">
			<SELECT size="1" name="cboProject" class="COMBO" style='width:150'>
				<OPTION value="0">All</OPTION>
			</SELECT>
		</TD>
		<TD></TD>
		<TD align="left">
			<INPUT type="button" name="Search" value="<%=languageChoose.getMessage("fi.jsp.HumanResource.View")%>" class="BUTTON" onclick="doView()">
		</TD>
	</TR>
</TABLE>
<BR>
<DIV align="left">
	<TABLE class="Table" cellspacing="1" width="90%">
		<CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.HumanResource.CalendarEffortOf")%><%=strGroup%></CAPTION>
		<% if (pageCount > 0 ){%>
	        <TR class="TableLeft">
				<TD colspan="3" align="right">
					<%if(pageCombobox != "")%>
						<%="Page " + pageCombobox%><%=" of " + pageCount%>
					&nbsp;
					<%if(currentPage > 1){%>
						<A href="Fms1Servlet?reqType=<%=Constants.GROUP_CALENDAR_EFFORT%>&iPage=<%=currentPage-1%>"
							class="TableLeft"><%=languageChoose.getMessage("fi.jsp.HumanResource.Back")%>
						</A>
					<%}%> 
					<% if (currentPage < pageCount){%>
						<A href="Fms1Servlet?reqType=<%=Constants.GROUP_CALENDAR_EFFORT%>&iPage=<%=currentPage+1%>">
							<%=languageChoose.getMessage("fi.jsp.HumanResource.Next")%>
						</A>
					<%}%>
				</TD>
	        </TR>
		<%}%>
		<TBODY>
			<TR class="ColumnLabel">
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.ProjectCode")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.PlannedCalendarEffort")%></TD>
				<TD><%=languageChoose.getMessage("fi.jsp.HumanResource.ActualCalendarEffort")%></TD>
			</TR>
		<%
		if (pageCount == 0) {
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
			for(int i = ((currentPage-1)*Constants.GROUP_CALENDAR_EFFORT_MAX_NUMBER); (i < (currentPage*Constants.GROUP_CALENDAR_EFFORT_MAX_NUMBER))&&(i < numberProject); i++) {
				CalendarEffort info = (CalendarEffort)calendarEffort.get(i);
				if (bl) {
					rowStyle = "CellBGRnews";
				}
				else {
					rowStyle = "CellBGR3";
				}
				bl = !bl;
		%>
	        <TR class="<%=rowStyle%>">
				<TD>
					<A href="Fms1Servlet?reqType=<%=Constants.WO_TEAM_GET_LIST%>&amp;projectID=<%=info.getProjectId()%>&amp;projectCode=<%=info.getProjectCode()%>">
					<%=ConvertString.toHtml(info.getProjectCode())%>
					</A>
				</TD>
	            <TD><%=CommonTools.formatDouble(info.getPlannedCalendarEffort())%></TD>
	            <TD><%=CommonTools.formatDouble(info.getActualCalendarEffort())%></TD>
	        </TR>
			
		<%
			}
		%>
		<TR class="TableLeft">
			<% if(totalCalendar != null) { %>
			<TD align="left"><%=languageChoose.getMessage("fi.jsp.HumanResource.TotalCalendarEffort")%></TD>
			<TD align="left"><%=CommonTools.formatDouble(totalCalendar.doubleValue())%></TD>
			<TD align="left"><%=CommonTools.formatDouble(totalCalendar.doubleValue())%></TD>
			<% } else { %>
			<TD align="left"><P class = "ERROR"><%=languageChoose.getMessage("fi.jsp.HumanResource.NoResultsMatchesYourCriteria")%></P></TD>
			<% } %>
		</TR>
		<%
		}
		%>
		</TBODY>
	</TABLE>
</DIV>
</FORM>
<SCRIPT language="javascript">
var project = new Array();
var lastProjectId = <%=projectId%>
<%
	for (int i = 0; i < projectList.size(); i++) {
		ProjectInfo info = (ProjectInfo) projectList.get(i);
		out.write("project[" + i + "] = new Array(4);");
		out.write("project[" + i + "][0]=" + info.getProjectId() + ";");
		out.write("project[" + i + "][1]='" + ConvertString.toJScript(info.getProjectCode()) + "';");
		out.write("project[" + i + "][2]=" + info.getStatus() + ";");
		out.write("project[" + i + "][3]=" + info.getType() + ";\n");
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
			frm.action = "Fms1Servlet?reqType=<%=Constants.GROUP_CALENDAR_EFFORT%>";
			frm.submit();
		}
	}
	function doSelectCombo(){
		frm.action = "Fms1Servlet?reqType=<%=Constants.GROUP_CALENDAR_EFFORT%>&iCombo=<%=currentPage%>";
		frm.submit();
	}
	function onChange(){
		while (frm.cboProject.options.length > 0 ){
			frm.cboProject.options[0] = null;
		}
		var count = 0;
		if (frm.cboStatus.value != -1 && frm.cboProjectType.value != -1){
			for (var i = 0; i < project.length; i++){
				if (project[i][2] == frm.cboStatus.value && project[i][3] == frm.cboProjectType.value){
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
		else if (frm.cboStatus.value != -1){
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
		else if(frm.cboProjectType.value != -1){
			for (var i = 0; i < project.length; i++){
				if (project[i][3] == frm.cboProjectType.value){
					count++;
					if (count ==1){
						appendOption(frm.cboProject, 0,
	                		"All", frm.cboProjectType.value,
		                	true);
					}
					appendOption(frm.cboProject, project[i][0],
	               		project[i][1], project[i][3],
	                	(lastProjectId == project[i][0]));
				}
			}
		}
		else {
			for (var i = 0; i < project.length; i++){
				count++;
				if (count ==1){
					appendOption(frm.cboProject, 0, "All", frm.cboStatus.value,true);
					}
					appendOption(frm.cboProject, project[i][0],
	               		project[i][1], project[i][2],
	                	(lastProjectId == project[i][0]));
				}
		}
	}
	function init(){
		onChange();
	}
	init();
	var objToHide=new Array(frm.cboStatus,frm.cboProject, frm.cboProjectType);
	
	function showFromDate(){
		showCalendar(frm.FromDate, frm.FromDate, "dd-mmm-yy",null,1,-1,-1,false);
	}
	
	function showToDate(){		
		showCalendar(frm.ToDate, frm.ToDate, "dd-mmm-yy",null,1,-1,-1,false);
	}
</SCRIPT>
</BODY>
</HTML>