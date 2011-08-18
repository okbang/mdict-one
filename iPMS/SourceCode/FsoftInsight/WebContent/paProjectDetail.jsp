<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<SCRIPT language="JavaScript" src="jscript/javaFns.js"></SCRIPT>
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>paProjectDetail.jsp</TITLE>
<SCRIPT language="JavaScript">
	function doOnload() {
		window.open("header.jsp","header");
		loadOrgMenu();
	}
</SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad="doOnload();" class="BD">
<%
	Vector devList = (Vector) session.getAttribute("devList");
	Vector toolList = (Vector) session.getAttribute("tool");
	ProjectInfo projectInfo = (ProjectInfo) session.getAttribute("projectInfo");
	
	String fromDate = (String) request.getParameter("fromDate");
	String toDate = (String) request.getParameter("toDate");
	
	String strWuID = (String) session.getAttribute("wuID");
	
	String strApplicationType = (String) session.getAttribute("strApplicationType");
	String strBusinessDomain = (String) session.getAttribute("strBusinessDomain");
	String strCustomer = (String) session.getAttribute("strCustomer");
	
	int i;
	char cr = 13;
	char lf = 10;
%>
<TABLE width="95%">
	<TR>
		<TD><P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.paProjectDetail.ProcessAssets")%> </P></TD>
	</TR>
</TABLE>

<br>

<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION align="left" class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.paProjectDetail.ProjectDescription")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.Projectcode")%></TD>
            <TD class="CellBGR3"><%=projectInfo.getProjectCode()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.Projectname")%></TD>
            <TD class="CellBGR3"><%=projectInfo.getProjectName()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.Description")%></TD>
            <TD class="CellBGR3">
        <%
        	StringTokenizer strToken = new StringTokenizer( ((projectInfo.getScopeAndObjective() == null) ? "" : projectInfo.getScopeAndObjective()), ""+cr+lf);
            String noteString = "";
            while (strToken.hasMoreElements()) {
            	noteString += strToken.nextToken();
            	noteString += "<br>";
            }
       	%>
            <%=((noteString.equals("")) ? "N/A" : noteString)%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.paProjectDetail.Team")%> </TD> 
            <TD class="CellBGR3">
		<%
		    String strDevList = "";
		   	for (i = 0; i < devList.size(); i++) {
		   		AssignmentInfo devInfo = (AssignmentInfo) devList.elementAt(i);
			   	strDevList = strDevList + devInfo.devName + "; ";
			}
		%>
		    <%=strDevList%>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.Customer")%></TD>
            <TD class="CellBGR3"><%=projectInfo.getCustomer()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.Projecttype")%></TD>
            <TD class="CellBGR3"><%=projectInfo.getLifecycle()%></TD>
        </TR>
        <TR>
        <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.ApplicationType")%></TD>
            <TD class="CellBGR3"><%=projectInfo.getApplicationType()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.Businessdomain")%></TD>
            <TD class="CellBGR3"><%=projectInfo.getBusinessDomain()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.Group")%></TD>
            <TD class="CellBGR3"><%=projectInfo.getGroupName()%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.OS")%></TD>
            <TD class="CellBGR3">
		<%
		    String osList = "";
		   	for (i = 0; i < toolList.size(); i++) {
		   		ToolInfo toolInfo = (ToolInfo) toolList.elementAt(i);
		   		if (toolInfo.tool_type == 1) {
				   	osList = osList + toolInfo.name + "; ";
			   	}
			}
		%>
		    <%=osList%>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.DBMS")%></TD>
            <TD class="CellBGR3">
		<%
		    String dbmsList = "";
		   	for (i = 0; i < toolList.size(); i++) {
		   		ToolInfo toolInfo = (ToolInfo) toolList.elementAt(i);
		   		if (toolInfo.tool_type == 2) {
				   	dbmsList = dbmsList + toolInfo.name + "; ";
			   	}
			}
		%>
		    <%=dbmsList%>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.Languages")%></TD>
            <TD class="CellBGR3">
		<%
		   	String languageList = "";
		   	for (i = 0; i < toolList.size(); i++) {
		   		ToolInfo toolInfo = (ToolInfo) toolList.elementAt(i);
		   		if (toolInfo.tool_type == 3) {
				   	languageList = languageList + toolInfo.name + "; ";
			   	}
			}
		%>
		    <%=languageList%>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.Tools")%></TD>
            <TD class="CellBGR3">
		<%
		    String swToolList = "";
		   	for (i = 0; i < toolList.size(); i++) {
		   		ToolInfo toolInfo = (ToolInfo) toolList.elementAt(i);
		   		if (toolInfo.tool_type == 4) {
				   	swToolList = swToolList + toolInfo.name + "; ";
			   	}
			}
		%>
		    <%=swToolList%>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.Hardwares")%></TD>
            <TD class="CellBGR3">
		<%
		    String hardwareList = "";
		   	for (i = 0; i < toolList.size(); i++) {
		   		ToolInfo toolInfo = (ToolInfo) toolList.elementAt(i);
		   		if (toolInfo.tool_type == 5) {
				   	hardwareList = hardwareList + toolInfo.name + "; ";
			   	}
			}
		%>
		    <%=hardwareList%>
			</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paProjectDetail.Startdate")%></TD>
            <TD class="CellBGR3"><%=CommonTools.dateFormat(projectInfo.getPlanStartDate())%></TD>
        </TR>
    </TBODY>
</TABLE>

<FORM name="frm" action="Fms1Servlet" method="post">
<INPUT type="hidden" name="reqType">
<INPUT type="hidden" name="workUnitID" value="<%=projectInfo.getWorkUnitId()%>">
<INPUT type="hidden" name="fromDate" value="<%=fromDate%>">
<INPUT type="hidden" name="toDate" value="<%=toDate%>">
<INPUT type="hidden" name="cboGroup" value="<%=strWuID%>">
<INPUT type="hidden" name="cboApplicationType" value="<%=strApplicationType%>">
<INPUT type="hidden" name="cboBusinessDomain" value="<%=strBusinessDomain%>">
<INPUT type="hidden" name="cboCustomer" value="<%=strCustomer%>">
</FORM>
<BR>
<P>
<INPUT type="button" name="b1" value=" <%=languageChoose.getMessage("fi.jsp.paProjectDetail.Goto")%> " class="BUTTON" onclick="onGoto()">
<INPUT type="button" name="b2" value=" <%=languageChoose.getMessage("fi.jsp.paProjectDetail.Tailorings")%> " class="BUTTON" onclick="onTD()">
<INPUT type="button" name="b3" value=" <%=languageChoose.getMessage("fi.jsp.paProjectDetail.Risks")%> " class="BUTTON" onclick="onRisk()">
<INPUT type="button" name="b4" value=" <%=languageChoose.getMessage("fi.jsp.paProjectDetail.Lessons")%> " class="BUTTON" onclick="onPL()">
<INPUT type="button" name="b5" value=" <%=languageChoose.getMessage("fi.jsp.paProjectDetail.Issues")%> " class="BUTTON" onclick="onIssue()">
<INPUT type="button" name="b5" value=" <%=languageChoose.getMessage("fi.jsp.paProjectDetail.Back")%> " class="BUTTON" onclick="jumpURL('paProjectDesc.jsp');">
</P>

<SCRIPT>
	function onGoto() {
		frm.reqType.value = "<%=Constants.WORKUNIT_HOME%>";
		frm.submit();
	}
	function onTD() {
		frm.reqType.value = "<%=Constants.PROASS_TAILORING_DEVIATION%>";
		frm.submit();
	}
	function onRisk() {
		frm.reqType.value = "<%=Constants.PROASS_RISK%>";
		frm.submit();
	}
	function onPL() {
		frm.reqType.value = "<%=Constants.PROASS_PRACTICE_LESSON%>";
		frm.submit();
	}
	function onIssue() {
		frm.reqType.value = "<%=Constants.PROASS_ISSUE%>";
		frm.submit();
	}
</SCRIPT>

</BODY>
</HTML>