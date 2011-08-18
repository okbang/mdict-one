<%@
    page language="java" import="javax.servlet.*, fpt.ncms.bean.*,
        fpt.ncms.util.StringUtil.*, fpt.ncms.constant.NCMS, fpt.ncms.constant.NCMS"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCListBean beanNCList = (NCListBean)session.getAttribute("beanNCList");
    int i = 0;
    int nTotalPage = (beanNCList.getTotal() % NCMS.NUM_PER_PAGE == 0)
            ? Math.round(beanNCList.getTotal() / NCMS.NUM_PER_PAGE)
            : Math.round(beanNCList.getTotal() / NCMS.NUM_PER_PAGE) + 1;
    
    String strOrderBy = beanNCList.getOrderBy();
    int nDirection = beanNCList.getDirection();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
<TITLE>NC List System</TITLE>
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
<SCRIPT language="javascript">
<%
    out.write("var arrItemGroup = new Array();\n");
	for (i = 0; i < beanNCList.getComboGroup().getNumberOfRows(); i++) {
        out.write("arrItemGroup[" + i + "] = \"" + beanNCList.getComboGroup().getCell(i, 0) + "\";\n");
    }
    out.write("var prjNum = " + beanNCList.getComboProject().getNumberOfRows() + ";\n");
    out.write("var prj = new Array(prjNum);\n");
    for (i = 0; i < (beanNCList.getComboProject().getNumberOfRows()); i++) {
    	out.write("\nprj[" + (i) + "]=new Array(3);");
        out.write("prj[" + (i) + "][0]=\"" + beanNCList.getComboProject().getCell(i, 0) + "\";");//code
        out.write("prj[" + (i) + "][1]=\"" + beanNCList.getComboProject().getCell(i, 1) + "\";");//group
        out.write("prj[" + (i) + "][2]=\"" + beanNCList.getComboProject().getCell(i, 2) + "\";");//status
    }
%>
//added by LAMNT3
//17-March-08
//for gen Project Combo depend on Group Combo
var valueN;
var flag = 0;
function changeGroup() {
	flag = (document.getElementById('cboPrj').selectedIndex);
	valueN = document.getElementById('cboPrj').value;
    var myElement;
    var form = document.forms[0];
    for (var i = form.cboPrj.options.length; i >= 0; i--) {
        form.cboPrj.options[i] = null;
    }
    var currGroup = form.cboGroup.value;
    
    var bStatusAll = false;
    var bGroupAll = false;
   if (currGroup == "(All)") {
      bGroupAll = true;
   }
   if(form.cboPrjStatus.value == -1){
   		bStatusAll = true;
   }
    
   myElement = document.createElement("option");
   myElement.value = 0;
   myElement.text = "(All)";
   
   if(form.cboPrjStatus.value != -1 )
      form.cboPrj.add(myElement);
      
   if(form.cboPrjStatus.value == -1 && currGroup != "(All)" )
   	   form.cboPrj.add(myElement);

   var bAdd = false;
   for (i = 1; i <= prjNum; i++) {
       bAdd = false;
       if (bGroupAll && bStatusAll) {
          bAdd = true;
       }
       else if (bStatusAll) {
          if (form.cboGroup.value == prj[i-1][1]) {
             bAdd = true;
          }
       }
       else if (bGroupAll) {
          if (form.cboPrjStatus.value == prj[i-1][2]) {
             bAdd = true;
          }
       }
       else {
          if ((form.cboGroup.value == prj[i-1][1]) && (form.cboPrjStatus.value == prj[i-1][2])) {
               bAdd = true;
          }
       }
       if (bAdd == true) {
           myElement = document.createElement("option");
           myElement.value = prj[i-1][0];
           myElement.text = prj[i-1][0];
           form.cboPrj.add(myElement);
        }
    }
    if (form.cboPrj.options.length == 1) {
        form.cboPrj.options[0].selected = true;
    }
}

function setCurrentProject(){
	if (flag!=0)
		document.getElementById('cboPrj').value = valueN;
}

function doAdmin(){
	<%if (NCMS.ROLE_PQA.equals(beanUserInfo.getRoleName())){%>
    	document.forms[0].hidAction.value = "<%=NCMS.CONSTANT_LIST%>";
	    document.forms[0].action = "NcmsServlet";
    	document.forms[0].submit();
    <%} else {%>
    	alert("You are not Admin");
    <%}%>
}

function doAddNC() {
    frmNCList.hidAction.value = "<%=NCMS.NC_ADD%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}

function doUpdateNC() {
	frmNCList.hidAction.value = "<%=NCMS.NC_UPDATE%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}

function doCreateReport() {
    if (!formValidate()) {
        return;
    }
    frmNCList.hidAction.value = "<%=NCMS.NC_REPORT%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}

function doSearch() {
	frmNCList.selectedProjectHidden.value = document.forms[0].cboPrj.options[document.forms[0].cboPrj.selectedIndex].text;
	
    if (!formValidate()) {
        return;
    }
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}

function doSort(SortBy) {
    if (!formValidate()) {
        return;
    }
    // Re-sort direction
    if (frmNCList.hidOrderBy.value == SortBy) {
        if (frmNCList.hidDirection.value > 0) {
            frmNCList.hidDirection.value = 0;
        }
        else {
            frmNCList.hidDirection.value = 1;
        }
    }
    // New column
    else {
        frmNCList.hidDirection.value = 1;
    }
    
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.hidOrderBy.value = SortBy;
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}

function doExportNC() {
    if (!formValidate()) {
        return;
    }
    var qrStr = "NcmsServlet?hidAction=<%=NCMS.NC_EXPORT%>";
    var sFeature = "width=780 height=550 top=0 left=0 menubar=yes";
    var exp_wd = window.open(qrStr, "NCExport", sFeature);
}

function doChangeListingMode() {
    clearInvalidControls();
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.hidMode.value = "<%=NCMS.PERSONAL_MODE%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}
function goNavNext() {
    if (!formValidate()) {
        return;
    }
    frmNCList.PageNumber.value = <%=beanNCList.getNumPage() + 1%>;
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}

function goNavBack() {
    if (!formValidate()) {
        return;
    }
    frmNCList.PageNumber.value = <%=beanNCList.getNumPage() - 1%>;
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}

function goNavPage() {
    if (!formValidate()) {
        return;
    }
    if (isNaN(frmNCList.PageNumber.value)) {
        alert("Invalid page number.");
        return false;
    }
    if ((frmNCList.PageNumber.value > <%=nTotalPage%>) || (frmNCList.PageNumber.value < 1)) {
        alert("Page number out of range.");
        return false;
    }
    frmNCList.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}

function pressKey() {
    if (window.event.keyCode == 13) {
        goNavPage();
    }
    if ((window.event.keyCode < 48) || (window.event.keyCode > 57)) {
        window.event.keyCode = 0;
    }
}
function doLogOut() {
    frmNCList.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmNCList.action = "NcmsServlet";
    frmNCList.submit();
}

function formValidate() {
    if (!validDates(document.forms[0], new Array('txtFromDate', 'txtToDate'),
        true, "This date is incorrect"))
    {
        return false;
    }
    strStartDate = convertToSimpleDate(document.forms[0].txtFromDate.value);
    strEndDate = convertToSimpleDate(document.forms[0].txtToDate.value);
    if (!compareDate(strStartDate, strEndDate)) {
        alert("From Date must be lower than To Date!");
        document.forms[0].txtFromDate.focus();
        return false;
    }
    return true;
}

function clearInvalidControls() {
    if (!isDate(document.forms[0].txtFromDate.value)) {
        document.forms[0].txtFromDate.value = "";
    }
    if (!isDate(document.forms[0].txtToDate.value)) {
        document.forms[0].txtToDate.value = "";
    }
    strStartDate = convertToSimpleDate(document.forms[0].txtFromDate.value);
    strEndDate = convertToSimpleDate(document.forms[0].txtToDate.value);
    if (!compareDate(strStartDate, strEndDate)) {
        txtToDate.value = "";
    }
}

window.name="myMainWindow";
</SCRIPT>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<%@ include file="Header.jsp"%>
<TABLE class="menu" cellpadding="0" cellspacing="0" width="100%" height="20pt">
    <TR>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doAddNC()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Add&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doExportNC()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Export&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doCreateReport()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Report&nbsp;&nbsp;</P></TD>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doAdmin()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Admin&nbsp;&nbsp;</P></TD>
        <TD align="right"><P class="menuitem" style="cursor:hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">Logout&nbsp;&nbsp;</P></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="44%" valign="top" align="left">
        <P><IMG src="images/Headers/ncListSystem.gif"></P>
        </TD>
        <TD width="56%" valign="top">
        <DIV align="right">
        <TABLE width="199" border="0" cellspacing="0" cellpadding="0" height="72">
            <TR>
                <TD background="images/Headers/logonName.gif">
                <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
                    <TR>
                        <TD width="75">&nbsp;</TD>
                        <TD width="70"><B>User: </B></TD>
                        <TD width="54"><B><%=beanUserInfo.getLoginName()%></B></TD>
                    </TR>
                    <TR>
                        <TD width="75">&nbsp;</TD>
                        <TD width="70"><B>Role: </B></TD>
                        <TD width="54"><B><%=beanUserInfo.getRoleName()%></B></TD>
                    </TR>
                </TABLE>
                </TD>
            </TR>
        </TABLE>
        </DIV>
        </TD>
    </TR>
</TABLE><%
    if (!"".equals(beanUserInfo.getMessage())){%>
<FONT face="Verdana" color="red"><%=beanUserInfo.getMessage()%></FONT><%
        beanUserInfo.setMessage("");
    }
%>
<FORM method="POST" name="frmNCList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<INPUT type="hidden" name="hidMode" value="<%=beanUserInfo.getListingMode()%>">
<INPUT type="hidden" name="hidID" value="">
<INPUT type="hidden" name="hidOrderBy" value="<%=strOrderBy%>">
<INPUT type="hidden" name="hidDirection" value="<%=nDirection%>">
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
	<COLGROUP>
    <COL width="15%">
    <COL width="18%">
    <COL width="15%">
    <COL width="21%">
    <COL width="16%">
    <COL width="15%">
	<TR>
		<TD><B> Creator </B></TD>
		<TD><SELECT name="cboCreator" class="SmallerCombo"><%
	    String strCurrentCreator = beanNCList.getNCModel().getCreator();
	    for (i = 0; i < beanNCList.getComboCreator().getNumberOfRows(); i++) {
	    	String strCreator = beanNCList.getComboCreator().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strCreator.equals(strCurrentCreator) ? strCreator + "\" selected" : strCreator + "\"");
	        out.write(">" + strCreator + "</OPTION>");
	    }%></SELECT></TD>
		<TD><B> Group/Department </B></TD>
		<TD><SELECT name="cboGroup" class="SmallCombo" onchange="changeGroup();"><%
		String strCurrentGroup = beanNCList.getNCModel().getGroupName();
	    for (i = 0; i < beanNCList.getComboGroup().getNumberOfRows(); i++) {
	    	String strGroup = beanNCList.getComboGroup().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strGroup.equals(strCurrentGroup) ? strGroup + "\" selected" : strGroup + "\"");
	        out.write(">" + strGroup + "</OPTION>");
	    }
    	%>
    	</SELECT></TD>
			<TD><B>&nbsp; NC Status </B></TD>
		<TD><SELECT name="cboStatus" class="SmallerCombo"><%
		String strCurrentStatus = Integer.toString(beanNCList.getNCModel().getStatus());
	    for (i = 0; i < beanNCList.getComboStatus().getNumberOfRows(); i++) {
	    	String strStatus = beanNCList.getComboStatus().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strStatus.equals(strCurrentStatus) ? strStatus + "\" selected" : strStatus + "\"");
	        out.write(">" + beanNCList.getComboStatus().getCell(i, 1) + "</OPTION>");
	    }%></SELECT></TD>
		</TR>
		<TR>
		<TD><B> Assignee </B></TD>
		<TD><SELECT name="cboAssignee" class="SmallerCombo"><%
		String strCurrentAssignee = beanNCList.getNCModel().getAssignee();
	    for (i = 0; i < beanNCList.getComboAssignee().getNumberOfRows(); i++) {
	    	String strAssignee = beanNCList.getComboAssignee().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strAssignee.equals(strCurrentAssignee) ? strAssignee + "\" selected" : strAssignee + "\"");
	        out.write(">" + strAssignee + "</OPTION>");
	    }%></SELECT></TD>
    
    	<TD><B> Project Status </B></TD>
		<TD><SELECT name="cboPrjStatus" class="SmallCombo" onchange="changeGroup();">
		<%
		String strCurrentPrjStatus = Integer.toString(beanNCList.getNCModel().getPrjStatus());
     	StringMatrix smtPrjStatus = beanNCList.getComboProjectStatus();
			for (int nRow = 0; nRow < smtPrjStatus.getNumberOfRows(); nRow++) {
        		String strValue = smtPrjStatus.getCell(nRow, 0);
       	 		String strText = smtPrjStatus.getCell(nRow, 1);
        		out.write("<OPTION value=\"");
		        out.write(strValue.equals(strCurrentPrjStatus) ? strValue + "\" selected" : strValue + "\"");
		        out.write(">" + strText + "</OPTION>");
    		}%>
		</SELECT></TD>
		<TD><B>&nbsp; KPA </B></TD>
		<TD><SELECT name="cboKPA" class="SmallerCombo"><%
		String strCurrentKPA = Integer.toString(beanNCList.getNCModel().getKPA());
	    for (i = 0; i < beanNCList.getComboKPA().getNumberOfRows(); i++) {
	    	String strKPA = beanNCList.getComboKPA().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strKPA.equals(strCurrentKPA) ? strKPA + "\" selected" : strKPA + "\"");
	        out.write(">" + beanNCList.getComboKPA().getCell(i, 1) + "</OPTION>");
	    }%></SELECT></TD>
		</TR>
		<TR>
		<TD><B> Reviewer </B></TD>
		<TD><SELECT name="cboReviewer" class="SmallerCombo"><%
		String strCurrentReviewer = beanNCList.getNCModel().getReviewer();
   	 	for (i = 0; i < beanNCList.getComboReviewer().getNumberOfRows(); i++) {
	    	String strReviewer = beanNCList.getComboReviewer().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strReviewer.equals(strCurrentReviewer) ? strReviewer + "\" selected" : strReviewer + "\"");
	        out.write(">" + strReviewer + "</OPTION>");
	    }%></SELECT></TD>
    	<TD><B> Project </B></TD>
		<TD>
		<SELECT name="cboPrj" class="SmallCombo">
			<%
				String strCurrentProject = beanNCList.getNCModel().getProjectID();
				if (strCurrentProject == null) strCurrentProject="(All)";
		   	 	for (i = 0; i < beanNCList.getComboProject().getNumberOfRows(); i++) {
			    	String strProject = beanNCList.getComboProject().getCell(i,0);
			        out.write("<OPTION value=\"");
			        out.write(strCurrentProject.equals(strProject) ? strProject + "\" selected" : strProject + "\"");
			        out.write(">" + beanNCList.getComboProject().getCell(i,0) + "</OPTION>");
			    }
			%>
		</SELECT></TD>
		
		<TD><B>&nbsp; ISO Clause </B></TD>
		<TD><SELECT name="cboISOClause" class="SmallerCombo"><%
		String strCurrentISOClause = Integer.toString(beanNCList.getNCModel().getISOClause());
	    for (i = 0; i < beanNCList.getComboISOClause().getNumberOfRows(); i++) {
	    	String strISOClause = beanNCList.getComboISOClause().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strISOClause.equals(strCurrentISOClause) ? strISOClause + "\" selected" : strISOClause + "\"");
	        out.write(">" + beanNCList.getComboISOClause().getCell(i, 1) + "</OPTION>");
	    }%></SELECT></TD>
		</TR>
		<TR>
		<TD><B> Level </B></TD>
		<TD><SELECT name="cboLevel" class="SmallerCombo"><%
		String strCurrentLevel = Integer.toString(beanNCList.getNCModel().getNCLevel());
	    for (i = 0; i < beanNCList.getComboLevel().getNumberOfRows(); i++) {
	    	String strLevel = beanNCList.getComboLevel().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strLevel.equals(strCurrentLevel) ? strLevel + "\" selected" : strLevel + "\"");
	        out.write(">" + beanNCList.getComboLevel().getCell(i, 1) + "</OPTION>");
	    }%></SELECT></TD>
		<TD><B> Process </B></TD>
		<TD><SELECT name="cboProcess" class="SmallCombo"><%
		String strCurrentProcess = Integer.toString(beanNCList.getNCModel().getProcess());
	    for (i = 0; i < beanNCList.getComboProcess().getNumberOfRows(); i++) {
	    	String strProcess = beanNCList.getComboProcess().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strProcess.equals(strCurrentProcess) ? strProcess + "\" selected" : strProcess + "\"");
	        out.write(">" + beanNCList.getComboProcess().getCell(i, 1) + "</OPTION>");
	    }%></SELECT></TD>
		<TD><B>&nbsp; Type of Cause </B></TD>
		<TD><SELECT name="cboTypeOfCause" class="SmallerCombo"><%
		String strCurrentTypeOfCause = Integer.toString(beanNCList.getNCModel().getTypeOfCause());
	    for (i = 0; i < beanNCList.getComboTypeOfCause().getNumberOfRows(); i++) {
	    	String strTypeOfCause = beanNCList.getComboTypeOfCause().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strTypeOfCause.equals(strCurrentTypeOfCause) ? strTypeOfCause + "\" selected" : strTypeOfCause + "\"");
	        out.write(">" + beanNCList.getComboTypeOfCause().getCell(i, 1) + "</OPTION>");
	    }%></SELECT></TD>
		</TR>
		<TR>
		<TD><B> Category </B></TD>
		<TD><SELECT name="cboTypeOfNC" class="SmallerCombo"><%
		String strCurrentTypeOfNC = Integer.toString(beanNCList.getNCModel().getNCType());
	    for (i = 0; i < beanNCList.getComboTypeOfNC().getNumberOfRows(); i++) {
	    	String strTypeOfNC = beanNCList.getComboTypeOfNC().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strTypeOfNC.equals(strCurrentTypeOfNC) ? strTypeOfNC + "\" selected" : strTypeOfNC + "\"");
	        out.write(">" + beanNCList.getComboTypeOfNC().getCell(i, 1) + "</OPTION>");
	    }%></SELECT></TD>

		<TD><B> Detected by </B></TD>
		<TD><SELECT name="cboDetectedBy" class="SmallCombo"><%
		String strCurrentDetectedBy = Integer.toString(beanNCList.getNCModel().getDetectedBy());
	    for (i = 0; i < beanNCList.getComboDetectedBy().getNumberOfRows(); i++) {
	    	String strDetectedBy = beanNCList.getComboDetectedBy().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strDetectedBy.equals(strCurrentDetectedBy) ? strDetectedBy + "\" selected" : strDetectedBy + "\"");
	        out.write(">" + beanNCList.getComboDetectedBy().getCell(i, 1) + "</OPTION>");
	    }%></SELECT></TD>
	    <TD><B>&nbsp; Type of action </B></TD>
		<TD><SELECT name="cboTypeOfAction" class="SmallerCombo"><%
		String strCurrentTypeOfAction = Integer.toString(beanNCList.getNCModel().getTypeOfAction());
	    for (i = 0; i < beanNCList.getComboTypeOfAction().getNumberOfRows(); i++) {
	    	String strTypeOfAction = beanNCList.getComboTypeOfAction().getCell(i,0);
	        out.write("<OPTION value=\"");
	        out.write(strTypeOfAction.equals(strCurrentTypeOfAction) ? strTypeOfAction + "\" selected" : strTypeOfAction + "\"");
	        out.write(">" + beanNCList.getComboTypeOfAction().getCell(i, 1) + "</OPTION>");
	    }%></SELECT></TD>
		</TR>
		<TR>
		<TD><B>From Date </B></TD>
        <TD><INPUT type="text" name="txtFromDate" class="DateBox" value="<%=beanNCList.getCurrentFromDate() != null ? beanNCList.getCurrentFromDate() : ""%>" maxlength="9"></TD>
        <TD><B>To Date </B></TD>
        <TD><INPUT type="text" name="txtToDate" class="DateBox" value="<%=beanNCList.getCurrentToDate() != null ? beanNCList.getCurrentToDate() : ""%>" maxlength="9"></TD>
		<TD></TD>
        <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<INPUT type="button" name="btnSearchNC" class="button" onclick="javascript:doSearch()" value="Search"></TD>
		</TR>
		<TR>
		<TD colspan="5"><FONT color="blue">Date format (dd-MMM-yy)</FONT></TD>
	</TR>
</TABLE>
<TABLE border="0" cellpadding="1" cellspacing="1" width="100%">
    <TR>
        <TD width="15%">&nbsp;</TD>
        <TD>&nbsp;</TD>
        <TD width="15%" valign="bottom">
        </TD>
        <TD align="right" width="40%">
        <TABLE border="0" cellpadding="0" cellspacing="0" width="125">
            <TR>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="25"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="25"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="30"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="40"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="25"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="25"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="112"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="105"></TD>
            </TR>
            <TR><%
    if (beanNCList.getTotal() > NCMS.NUM_PER_PAGE) {
%>
                <TD><%
        if (beanNCList.getNumPage() > 1) {
%>
                <IMG border="0" src="images/Buttons/b_previous_n.gif" onmouseout="this.src='images/Buttons/b_previous_n.gif'" onmouseover="this.src='images/Buttons/b_previous_p.gif'" onclick="javascript:goNavBack()" align="bottom"></TD><%
        }
%>
                <TD><IMG border="0" src="images/Buttons/b_go_n.gif" onmouseout="this.src='images/Buttons/b_go_n.gif'" onmouseover="this.src='images/Buttons/b_go_p.gif'" onclick="javascript:goNavPage()" align="bottom"></TD>
                <TD><INPUT type="text" name="PageNumber" value="<%=beanNCList.getNumPage()%>" size="2" onkeypress="pressKey()"></TD>
                <TD>of&nbsp;<%=nTotalPage%></TD><%
        if (beanNCList.getNumPage() < nTotalPage) {
%>
                <TD>&nbsp;<IMG border="0" src="images/Buttons/b_next_n.gif" onmouseout="this.src='images/Buttons/b_next_n.gif'" onmouseover="this.src='images/Buttons/b_next_p.gif'" onclick="javascript:goNavNext()" align="bottom"></TD><%
        }
        else {
%>
                <TD></TD><%
        }
    }
    else {
%>
                <TD></TD>
                <TD></TD>
                <TD></TD>
                <TD></TD>
                <TD></TD><%
    }
%>
                <TD></TD>
                <TD><IMG border="0" onclick="doChangeListingMode()" style="cursor:hand" onmouseout="this.src='images/tabs/t_personalView_n.gif'" onmouseover="this.src='images/tabs/t_personalView_p.gif'" src="images/tabs/t_personalView_n.gif" align="bottom"></TD>
                <TD><IMG border="0" src="images/tabs/t_systemView_p.gif" align="bottom"></TD>
            </TR></TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE bgcolor="#9292CB" border="0" cellpadding="0" cellspacing="1" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber2">
    <COLGROUP>
        <COL width="12%">
        <COL width="25%">
        <COL width="8%">
        <COL width="12%">
        <COL width="11%">
        <COL width="12%">
        <COL width="12%">
        <COL width="8%">
    <TR class="Header">
        <TD title="Sort by code" style="cursor: hand" onclick="javascript:doSort('Code')">&nbsp;Code
<%
    if (strOrderBy.equals("Code")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD>&nbsp;Description</TD>
        <TD title="Sort by group" style="cursor: hand" onclick="javascript:doSort('GroupName')">&nbsp;Group
<%
    if (strOrderBy.equals("GroupName")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by project code" style="cursor: hand" onclick="javascript:doSort('ProjectID')">&nbsp;Project
<%
    if (strOrderBy.equals("ProjectID")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by assignee" style="cursor: hand" onclick="javascript:doSort('Assignee')">&nbsp;Assignee
<%
    if (strOrderBy.equals("Assignee")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by created date" style="cursor: hand" onclick="javascript:doSort('CreationDate')">&nbsp;CreateDate
<%
    if (strOrderBy.equals("CreationDate")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by deadline" style="cursor: hand" onclick="javascript:doSort('Deadline')">&nbsp;Deadline
<%
    if (strOrderBy.equals("Deadline")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
        <TD title="Sort by status" style="cursor: hand" onclick="javascript:doSort('Status')">&nbsp;Status
<%
    if (strOrderBy.equals("Status")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG src='images/arrow_down.gif'>");
        }
    }
%>        
        </TD>
    </TR><%
    for (i = 0; i < NCMS.NUM_PER_PAGE; i++) {
        if (i + (beanNCList.getNumPage() - 1) * NCMS.NUM_PER_PAGE >= beanNCList.getTotal()) {
            break;
        }
%>
    <TR class="row<%=i % 2 + 1%>" title="Click to edit" onclick="javascript:frmNCList.hidID.value='<%=beanNCList.getNCList().getCell(i, 1)%>';doUpdateNC();" onmouseout="this.style.color='#000000';" onmouseover="this.style.color='#FF0000';" style="cursor: hand">
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 8)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 2)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 3)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 4)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 5)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 7)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 6)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCList.getNCList().getCell(i, 0)%></P>
        </TD>
    </TR><%
    }
    if (i == 0) {%>
    <TR><TD colspan="8" class="row1">No record found! </TD></TR><%
    }
%>
</TABLE>
</FORM>
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
    <TR>
        <TD width="70%">&nbsp;<IMG border="0" onclick="doAddNC()" style="cursor: hand" onmouseout="this.src='images/Buttons/b_add_n.gif'" onmouseover="this.src='images/Buttons/b_add_p.gif'" style="cursor:hand" src="images/Buttons/b_add_n.gif" name="imgAdd">
        <IMG border="0" style="cursor: hand" onmouseout="this.src='images/Buttons/b_export_n.gif'" onmouseover="this.src='images/Buttons/b_export_p.gif'" style="cursor:hand" onclick="doExportNC()" src="images/Buttons/b_export_n.gif" name="imgExport">
        <IMG border="0" onclick="doCreateReport()" style="cursor: hand" onmouseout="this.src='images/Buttons/b_report_n.gif'" onmouseover="this.src='images/Buttons/b_report_p.gif'" style="cursor:hand" src="images/Buttons/b_report_n.gif" name="imgReport"></TD>
    </TR>
</TABLE>
<SCRIPT>
	changeGroup();
	setCurrentProject();
</SCRIPT>
</BODY>
</HTML>