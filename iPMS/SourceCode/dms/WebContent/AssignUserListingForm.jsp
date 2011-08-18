<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.ProjectEnvironment.*,
            fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    AssignListBean beanAssignList
            = (AssignListBean)session.getAttribute("beanAssignList");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Assign User List</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT language="javascript">
function doAddNew() {
    var form = document.frmAssignUserList;
    var strAssignTo;
    strAssignTo = form.cboAssignTo.value;
    if (strAssignTo == 0) {
          alert("Invalid Developer");
          form.cboAssignTo.focus();
          return;
    }
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "AddAssignedUser";
    form.action = "DMSServlet";
    form.submit();
}

function doDelete() {
    var form = document.frmAssignUserList;
    var nCount;
    nCount = 0;
    for (var i = 0; i < form.elements.length; i++) {
        var e = form.elements[i];
        if (e.name == "checkBox" && e.type == "checkbox") {
            if (e.checked == 1) {
                nCount++;
            }
        }
    }
    if (nCount <= 0) {
        alert("Please select records to delete!");
        return;
    }
    bOK = window.confirm("Do you want to delete selected records, continue?");
    if (!bOK) {
        return false;
    }
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "DeleteAssignedUser";
    form.action = "DMSServlet";
    form.submit();
}

function doUpdate(user) {
    var form = document.frmAssignUserList;
    form.UpdateUser.value = user;
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "UpdateAssignedUser";
    form.action = "DMSServlet";
    form.submit();
}

function CheckAll() {
    var form = document.frmAssignUserList;
    for (var i = 0; i < form.elements.length; i++) {
        var e = form.elements[i];
        if (e.name != "allbox") {
            e.checked = form.allbox.checked;
        }
    }
}

function doQueryListing() {
    var form = document.frmAssignUserList;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<DIV>
<P><IMG border="0" src="Images/AssignUserListing.gif" width="411" height="28"></P>
</DIV>
<FORM method="post" action="DMSServlet" name="frmAssignUserList">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
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
        <TD width="22%" align="right"><SELECT name="cboProjectList" disabled class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="QueryListing"%>','<%=""%>');"><%
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
        <TD width="22%" align="right"><SELECT name="cboProjectStatus" disabled class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="QueryListing"%>','<%=""%>');"><%
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
<BR>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0">
    <TR>
        <TD width="10%" align="left"><B>Developer</B></TD>
        <TD width="23%" align="left"><SELECT name="cboAssignTo" class="SmallCombo"><%
    for (int i = 0; i < beanAssignList.getListing().getNumberOfRows(); i++) {
%>
            <OPTION value="<%=beanAssignList.getListing().getCell(i, 0)%>"><%=beanAssignList.getListing().getCell(i, 1)%></OPTION><%
    }
%>
        </SELECT></TD>
        <TD width="9%" align="left"><B>Position</B></TD>
        <TD width="23%" align="left"><SELECT size="1" name="cboPosition" class="SmallCombo">
            <OPTION value="0">Developer</OPTION>
            <OPTION value="1">Tester/SQA</OPTION>
            <OPTION value="2">Project Leader</OPTION>
        </SELECT></TD>
        <TD width="8%" align="left"><B>Status</B></TD>
        <TD width="23%" align="left"><SELECT size="1" name="cboStatus" class="SmallCombo">
            <OPTION value="0">Active</OPTION>
            <OPTION value="1">Inactive</OPTION>
        </SELECT></TD>
        <TD width="15%" align="left"><INPUT type="button" class="button" name="AddnewUser" onclick="javascript:doAddNew()" value="AddNew"></TD>
    </TR>
</TABLE>
<INPUT type="hidden" name="UpdateUser" value="<%=beanAssignList.getUpdateUser()%>"> <FONT class "font" color="red"><%
    int nCheck = beanAssignList.getMessage();
    if (nCheck == 1) {
        out.print("Developer was existed");
    }
%></FONT>
<P></P>
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" bgcolor="#000000">
    <TR id="tabtitle" class="Row0">
        <TD width="2%" height="19"><INPUT type="checkbox" name="allbox" value="CheckAll" onclick="JavaScript: CheckAll();"></TD>
        <TD align="center" width="40%" height="19">Developer</TD>
        <TD align="center" width="30%" height="19">Position</TD>
        <TD align="center" width="30%" height="19">Status</TD>
    </TR><%
    for (int i = 0; i < beanAssignList.getAssignUserList().getNumberOfRows(); i++) {
%>
    <TR class="Row<%=(i + 1) % 2 + 1%>">
        <TD width="3%"><INPUT type="checkbox" name="checkBox" value="<%=beanAssignList.getAssignUserList().getCell(i, 0)%>"></TD>
        <TD width="40%"><A href="javascript:doUpdate('<%=i%>')"><%=beanAssignList.getAssignUserList().getCell(i, 1)%></A></TD>
        <TD width="30%"><%=beanAssignList.getAssignUserList().getCell(i, 2)%></TD>
        <TD width="30%"><%=beanAssignList.getAssignUserList().getCell(i, 3)%></TD>
    </TR><%
    }
%>
</TABLE>
<P><INPUT type="button" class="button" name="DeleteUser" onclick="javascript:doDelete()" value="Delete"></P>
</FORM>
</BODY>
</HTML>