<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@
    page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.DefectManagement.*,fpt.dms.bean.ProjectEnvironment.*,
            fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" 
%>
<%@
    page isThreadSafe="false" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"
%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    DefectListingBean beanDefectList = (DefectListingBean)request.getAttribute("beanDefectList");
    ComboProject beanComboProject = (ComboProject)session.getAttribute("beanComboProject");
    ComboAssignTo comboCreatedBy = (ComboAssignTo)session.getAttribute("comboCreatedBy");
    String strMessage = (String) request.getAttribute("uploadMessage");

    StringMatrix smDefectList = beanDefectList.getDefectList();
    boolean submit = false;
    if (smDefectList == null || smDefectList.getNumberOfRows() == 0){
    	if (session.getAttribute("ImportDefectList") != null){
    		smDefectList = (StringMatrix)session.getAttribute("ImportDefectList");
    	}
    }
    else {
    	submit = true;
    }
    StringMatrix smProjectMembers = comboCreatedBy.getListing();
    StringMatrix smProjectStatus = beanComboProject.getStatusList();
	String strCurrentStatus = beanUserInfo.getCurrentStatus();
	String strCreatedBy = (String) session.getAttribute("diCreatedBy");

	if (strCreatedBy == null) {
		strCreatedBy = beanUserInfo.getAccount();
	}

    String strCreatedByDisabled = "";
    if (!beanUserInfo.isTester() && !beanUserInfo.isProjectLeader()) {
    	strCreatedByDisabled = "disabled";
    }
    String strImportDisabled = "";
    if (!strCurrentStatus.equals(DMS.PROJECT_VALUE_STATUS_ONGOING) || smProjectMembers.getNumberOfRows() <= 0) {
    	strImportDisabled = "disabled";
    }
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Import Defect</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT language="javascript">
function doImport() {
	var form = document.forms[0];
	var cboCreatedBy = form.cboCreatedBy;
	if (!isValidForm()) {
		return;
	}
	form.hidAction.value = "DM";
	form.hidActionDetail.value = "ImportDefect";
	form.action = "DMSServlet";
	form.encoding = "multipart/form-data";
	form.submit();
}

function doSubmit(){
	var form = document.forms[0];
	form.hidAction.value = "DM";
	form.hidActionDetail.value = "SubmitImportDefect";
	form.action = "DMSServlet";
	form.encoding = "multipart/form-data";
	form.submit();
}
function doQueryListing() {
    var form = document.forms[0];
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function isValidForm() {
	var form = document.forms[0];
	var cboCreatedBy = form.cboCreatedBy;
	var fileBrowsed = form.importFile;
	if (cboCreatedBy.value <= 0) {
		alert("Invalid Developer");
		cboCreatedBy.focus();
		return false;
	}
	if (fileBrowsed.value.length <= 0) {
		alert("Invalid file name");
		fileBrowsed.focus();
		return false;
	}
    else {
        var ext = fileBrowsed.value.substring(
        				fileBrowsed.value.lastIndexOf(".") + 1,
        				fileBrowsed.value.length);
        ext = ext.toLowerCase();
        if (ext != "xls") {
            alert("Select MS Excel file");
			fileBrowsed.focus();
            return false;
        }
    }
	
	return true;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<FORM method="post" action="DMSServlet" name="frmImportDefect">
<DIV><IMG border="0" src="Images/DefectImport.gif" width="411" height="28"></DIV>
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidTypeOfView" value="<%= beanUserInfo.getTypeOfView()%>">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<TABLE border="0" width="100%">
    <TR>
        <TD align="right"><A href="javascript:doQueryListing()">View DefectListing</A></TD>
    </TR>
</TABLE>
<TABLE border="0" width="100%" class="TblOut2">
    <TR>
        <TD width="8%"><B>User:</B></TD>
        <TD width="24%"><%=beanUserInfo.getUserName()%></TD>
        <TD width="12%"><B>Login Date:</B></TD>
        <TD width="25%"><%=beanUserInfo.getDateLogin()%></TD>
        <TD width="9%"><B>Project</B></TD>
        <TD width="22%" align="right"><SELECT name="cboProjectList" class="SmallCombo" onchange="javascript:doChangeProject('DM','ImportDefect','');"><%
	int nCurrentProjectID = beanUserInfo.getProjectID();
	int nProjectID;
    for (int i = 0; i < beanComboProject.getListing().getNumberOfRows(); i++) {
		nProjectID = Integer.parseInt(beanComboProject.getListing().getCell(i, 0));
        out.write("<OPTION ");
        out.write(nProjectID == nCurrentProjectID ? " selected " : " ");
        out.write("value='" + nProjectID + "'>" + beanComboProject.getListing().getCell(i, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
    </TR>
    <TR>
        <TD width="8%"><B>Group:</B></TD>
        <TD width="24%"><%=beanUserInfo.getGroupName()%></TD>
        <TD width="12%"><B>Position:</B></TD>
        <TD width="25%"><%=beanUserInfo.getPositionName()%></TD>
        <TD width="9%"><B>Status</B></TD>
        <TD width="22%" align="right"><SELECT name="cboProjectStatus" disabled class="SmallCombo" onchange="javascript:doChangeProject('DM','ImportDefect','');"><%
    for (int i = 0; i < smProjectStatus.getNumberOfRows(); i++) {
        out.write("<OPTION ");
        out.write(strCurrentStatus.equals(smProjectStatus.getCell(i,0)) ? " selected " : " ");
        out.write("value=\"" + smProjectStatus.getCell(i, 0) + "\">" + smProjectStatus.getCell(i, 1) + "</OPTION>");
    }
%>        
        </SELECT></TD>
    </TR>
</TABLE>
<BR>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0">
    <COLGROUP>
        <COL width="12%">
        <COL width="20%">
        <COL width="10%">
        <COL width="30%">
        <COL width="25%">
    <TR>
        <TD align="left"><B>Created By&nbsp;*</B></TD>
        <TD align="left">
        <SELECT name="cboCreatedBy" class="SmallCombo"><%
    if (smProjectMembers != null) {
    	String strAccount;
	    for (int i = 0; i < smProjectMembers.getNumberOfRows(); i++) {
	    	strAccount = smProjectMembers.getCell(i, 1);
	    	if(!strAccount.equalsIgnoreCase(beanUserInfo.getAccount()) && strCreatedByDisabled == "disabled") continue;
%>
            <OPTION value="<%=strAccount%>" <%=(strAccount.equals(strCreatedBy) ? "selected" : "")%>><%=strAccount%></OPTION><%
	    }
	}
%>
        </SELECT></TD>
        <TD align="left"><B>File Name&nbsp;*</B></TD>
        <TD align="left"><INPUT type="file" name="importFile"></TD>
        <TD align="left"><INPUT type="button" class="button" name="ImportDefect" onclick="javascript:doImport()" value="View Defect" <%=strImportDisabled%>></TD>
    </TR>
    <TR>
        <TD align="left"></TD>
        <TD align="left"></TD>
        <TD align="left"></TD>
        <TD align="left">(Max file size:&nbsp;2M)</TD>
        <TD align="left"><%=(strMessage == null ? "" : strMessage)%></TD>
    </TR>
    <TR>
        <TD align="left"></TD>
        <TD align="left"></TD>
        <TD align="left"></TD>
        <TD align="left"><A href="Template_Defect log.xls">Download Template File</A></TD>
    </TR>
</TABLE>
<%if (smDefectList != null && submit){%>
<P align="center">
	<INPUT type="button" class="Button" name="SubmitImportDefect" value="Submit" onclick="doSubmit()">
</P>
<%}%>
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" bgcolor="#000000">
    <COLGROUP>
        <COL width="5%">
        <COL width="28%">
        <COL width="10%">
        <COL width="11%">
        <COL width="8%">
        <COL width="6%">
        <COL width="8%">
        <COL width="8%">
        <COL width="8%">
        <COL width="8%">
    <TR class="Row0">
        <TD align="center" valign="middle"><SPAN style="color: white">#</SPAN></TD>
        <TD align="center" valign="middle"><SPAN style="color: white">Title</SPAN></TD>
        <TD align="center" valign="middle"><SPAN style="color: white">Severity</SPAN></TD>
        <TD align="center" valign="middle"><SPAN style="color: white">Priority</SPAN></TD>
        <TD align="center" valign="middle"><SPAN style="color: white">Status</SPAN></TD>
        <TD align="center" valign="middle"><SPAN style="color: white">Defect Owner</SPAN></TD>
        <TD align="center" valign="middle"><SPAN style="color: white">Assign To</SPAN></TD>
        <TD align="center" valign="middle"><SPAN style="color: white">Created By</SPAN></TD>
        <TD align="center" valign="middle"><SPAN style="color: white">Fixed Date</SPAN></TD>
        <TD align="center" valign="middle"><SPAN style="color: white">Due Date</SPAN></TD>
    </TR><%
    if (smDefectList != null) {
	    for (int i = 0; i < smDefectList.getNumberOfRows(); i++) {
	    	String title = smDefectList.getCell(i, 1);
	    	int row = (i + 1) % 2 + 1;
	    	if ("N/A".equals(title)){
	    		row = 3;
	    	}
%>
    <TR class="Row<%=row%>">
		<TD align="center"><%=smDefectList.getCell(i, 0)%></TD>
        <TD><%=smDefectList.getCell(i, 1)%></TD>
        <TD><%=smDefectList.getCell(i, 2)%></TD>
        <TD><%=smDefectList.getCell(i, 3)%></TD>
        <TD><%=smDefectList.getCell(i, 4)%></TD>
        <TD><%=smDefectList.getCell(i, 9)%></TD>
        <TD><%=smDefectList.getCell(i, 5)%></TD>
        <TD><%=smDefectList.getCell(i, 7)%></TD>
        <TD align="center"><%=smDefectList.getCell(i, 8)%></TD>
        <TD align="center"><%=smDefectList.getCell(i, 6)%></TD>
    </TR>
		<%}%>
	<%}%>
</TABLE>
<%if (smDefectList != null && submit){%>
<P align="Center">
	<INPUT type="button" class="Button" name="SubmitImportDefect" value="Submit" onclick="doSubmit()">
</P>
<%}%>
</FORM>
</BODY>
</HTML>