<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.ProjectEnvironment.*,
            fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    AssignUpdateBean beanAssignUpdate
            = (AssignUpdateBean)request.getAttribute("beanAssignUpdate");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Assign User Update</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT language="javascript">
function doSave() {
    var form = document.frmAssignUserUpdate;
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "SaveUpdateAssignedUser";
    form.action = "DMSServlet";
    form.submit();
}

function doQueryListing() {
    var form = document.frmAssignUserUpdate;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function doBack() {
    var form = document.frmAssignUserUpdate;
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "AssignList";
    form.action = "DMSServlet";
    form.submit();
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<DIV>
<P><IMG border="0" src="Images/AssignUserUpdate.gif" width="411" height="28"></P>
</DIV>
<FORM method="post" action="DMSServlet" name="frmAssignUserUpdate">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="hidID" value="<%=beanAssignUpdate.getAssignUserList().getCell(0, 0)%>">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<TABLE border="0" width="100%">
    <TR>
        <TD align="right"><A href="javascript:doQueryListing()">View DefectListing</A></TD>
    </TR>
</TABLE><TABLE border="0" width="100%" class="TblOut2">
    <TR>
        <TD width="8%"><B>User:</B></TD>
        <TD width="24%"><%=beanUserInfo.getUserName()%></TD>
        <TD width="12%"><B>Login Date:</B></TD>
        <TD width="25%"><%=beanUserInfo.getDateLogin()%></TD>
        <TD width="9%"><B>Project</B></TD>
        <TD width="22%" align="right"><SELECT name="cboProjectList" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="QueryListing"%>','<%=""%>');"><%
    for (int i = 0; i < beanComboProject.getListing().getNumberOfRows(); i++) {
		int nCurrentProjectID = beanUserInfo.getProjectID();
		int nProjectID = Integer.parseInt(beanComboProject.getListing().getCell(i, 0));
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
        <TD width="22%" align="right"><SELECT name="cboProjectStatus" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="QueryListing"%>','<%=""%>');"><%
	String strCurrentStatus = beanUserInfo.getCurrentStatus();
    for (int i = 0; i < beanComboProject.getStatusList().getNumberOfRows(); i++) {
		StringMatrix smStatus = beanComboProject.getStatusList();
        out.write("<OPTION ");
        out.write(strCurrentStatus.equals(smStatus.getCell(i,0)) ? " selected " : " ");
        out.write("value=\"" + smStatus.getCell(i, 0) + "\">" + smStatus.getCell(i, 1) + "</OPTION>");
    }
%>        
        </SELECT></TD>

    </TR>
</TABLE>
<P></P>
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" bgcolor="#000000">
    <TR id="tabtitle" class="Row0">
        <TD align="center" width="40%" height="19">Developer</TD>
        <TD align="center" width="30%" height="19">Position</TD>
        <TD align="center" width="30%" height="19">Status</TD>
    </TR>
    <TR class="Row2">
        <TD width="40%" align="center"><%=beanAssignUpdate.getAssignUserList().getCell(0, 1)%></TD>
        <TD width="30%"><SELECT size="1" name="cboPosition" class="SmallCombo" style="width: 100%"><%
    if (beanAssignUpdate.getAssignUserList().getCell(0, 2).regionMatches(true, 0, "Developer", 0, 9)) {
%>
            <OPTION selected value="0">Developer</OPTION><%
    }
    else {
%>
            <OPTION value="0">Developer</OPTION><%
    }
    if (beanAssignUpdate.getAssignUserList().getCell(0, 2).regionMatches(true, 0, "Tester", 0, 6)) {
%>
            <OPTION selected value="1">Tester/SQA</OPTION><%
    }
    else {
%>
            <OPTION value="1">Tester/SQA</OPTION><%
    }
    if (beanAssignUpdate.getAssignUserList().getCell(0, 2).regionMatches(true, 0, "Project Leader", 0, 14)) {
%>
            <OPTION selected value="2">Project Leader</OPTION><%
    }
    else {
%>
            <OPTION value="2">Project Leader</OPTION><%
    }
%>
        </SELECT></TD>
        <TD width="30%"><SELECT size="1" name="cboStatus" class="SmallCombo" style="width: 100%"><%
    if (beanAssignUpdate.getAssignUserList().getCell(0, 3).equals("Active")) {
%>
            <OPTION selected value="0">Active</OPTION><%
    }
    else {
%>
            <OPTION value="0">Active</OPTION><%
    }
    if (beanAssignUpdate.getAssignUserList().getCell(0, 3).equals("Inactive")) {
%>
            <OPTION selected value="1">Inactive</OPTION><%
    }
    else {
%>
            <OPTION value="1">Inactive</OPTION><%
    }
%>
        </SELECT></TD>
    </TR>
</TABLE>
<P><INPUT type="button" name="UpdateUser" class="button" onclick="javascript:doSave()" value="Save">
&nbsp;&nbsp;&nbsp; <INPUT type="button" name="Back" class="button" onclick="javascript:doBack()" value="Back"></P>
</FORM>
</BODY>
</HTML>