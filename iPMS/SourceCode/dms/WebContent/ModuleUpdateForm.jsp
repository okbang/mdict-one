<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.ProjectEnvironment.*,
            fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
    ModuleUpdateBean beanModuleUpdate
            = (ModuleUpdateBean)request.getAttribute("beanModuleUpdate");
    String strClientMessage = (String)request.getAttribute("ClientMessageResult");
    if (strClientMessage == null) {
        strClientMessage = "";
    }
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT src="scripts/validate.js"></SCRIPT>
<TITLE>Module Update</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT language="javascript">
function doSave() {
    var form = document.frmSetupModuleUpdate;
    var strValue;
    strValue = form.txtName.value;
    strValue = trimSpaces(strValue);
    if (strValue.length == 0) {
        alert("The value must not be empty!");
        form.txtName.focus();
        return;
    }
    if (!isPositiveNumberCombobox(form.cboWP)) {
        return;
    }
    strValue = form.txtPlannedDefect.value;
    strValue = trimSpaces(strValue);
    if (strValue.length == 0) {
        alert("The value must not be empty!");
        form.txtPlannedDefect.focus();
        return;
    }
    if (!validateNumber(form.txtPlannedDefect)) {
        return;
    }
    form.txtReplannedDefect.value = trimSpaces(form.txtReplannedDefect.value);
    strValue = form.txtReplannedDefect.value;
    if (strValue.length > 0) {
        if (!validateNumber(form.txtReplannedDefect)) {
            return;
        }
    }
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "SaveUpdateModule";
    form.action = "DMSServlet";
    form.submit();
}

function doBack() {
    var form = document.frmSetupModuleUpdate;
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "ModuleList";
    form.action = "DMSServlet";
    form.submit();
}

function doQueryListing() {
    var form = document.frmSetupModuleUpdate;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function numberAllowed() {
    if (window.event.keyCode > 57 || window.event.keyCode < 48) {
        window.event.keyCode = 0;
    }
}

function validateNumber(txtDuration) {
    var durationValue;
    durationValue = txtDuration.value;
    if (trimSpaces(durationValue) == "") {
        return true;
    }
    if ((isNaN(durationValue)) || (durationValue < 0)) {
        alert("Invalid number.");
        txtDuration.focus();
        return false;
    }
    return true;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<DIV>
<P><IMG border="0" src="Images/title_Module_Update.gif" width="409" height="28"></P>
</DIV>
<FORM method="post" action="DMSServlet" name="frmSetupModuleUpdate">
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
%>        </SELECT></TD>

    </TR>
</TABLE>
<P></P>
<TABLE border="0" cellpadding="1" cellspacing="1" align="center" width="95%">
    <TR>
        <TD width="100%" align="left"><B><FONT color="#cc0001" style="FONT-FAMILY: Verdana; FONT-SIZE: 10pt"><%=strClientMessage%></FONT></B>&nbsp;</TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="1" width="95%" bgcolor="#000000" align="center">
    <TR id="tabtitle" class="Row0">
        <TD align="center" width="5%" height="19">ID</TD>
        <TD align="center" width="20%" height="19">Name</TD>
        <TD align="center" width="20%" height="19">Work Product</TD>
        <TD align="center" width="20%" height="19">Planned Defect<BR>(wd)</TD>
        <TD align="center" width="20%" height="19">Re-planned Defect<BR>(wd)</TD>
    </TR>
    <TR class="Row2">
        <TD width="5%" align="center"><%=beanModuleUpdate.getSetupModuleList().getCell(0, 0)%>
        <INPUT type="hidden" name="hidModuleID" value="<%=beanModuleUpdate.getSetupModuleList().getCell(0, 0)%>"></TD>
        <TD width="20%"><INPUT type="text" name="txtName" value="<%=beanModuleUpdate.getSetupModuleList().getCell(0, 1)%>" class="midBox" style="width: 100%" maxlength="50"></TD>
        <TD width="20%"><SELECT name="cboWP" size="1" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 100%"><%
    String strSelectedWPID = beanModuleUpdate.getSetupModuleList().getCell(0, 5);
    for (int nRow = 0; nRow < beanModuleUpdate.getWorkProductList().getNumberOfRows(); nRow++) {
        String strValue = beanModuleUpdate.getWorkProductList().getCell(nRow, 0);
        int nValue = (strValue != null && ! "".equals(strValue)) ? Integer.parseInt(strValue) : 0; //WP_ID
        String strText = beanModuleUpdate.getWorkProductList().getCell(nRow, 1); //WP name
%>
            <OPTION value="<%=nValue%>" <%=(strSelectedWPID.equals(strValue)) ? " selected" : ""%>><%=strText%></OPTION><%
    }
%>
        </SELECT> <!-- planned/re-planned defect of each work product --> <%
    for (int i = 0; i < beanModuleUpdate.getWorkProductList().getNumberOfRows(); i++) {
        String strWPID = beanModuleUpdate.getWorkProductList().getCell(i, 0);
        String strPlannedDefect = beanModuleUpdate.getWorkProductList().getCell(i, 2);
        String strReplannedDefect = beanModuleUpdate.getWorkProductList().getCell(i, 3);
%>
        <INPUT type="hidden" name="hidPlannedDefect_<%=strWPID%>" value="<%=strPlannedDefect%>">
        <INPUT type="hidden" name="hidReplannedDefect_<%=strWPID%>" value="<%=strReplannedDefect%>"><%
    }
%>
        </TD>
        <TD width="20%"><INPUT type="text" name="txtPlannedDefect" value="<%=beanModuleUpdate.getSetupModuleList().getCell(0, 3)%>" class="midBox" style="width: 100%" maxlength="8" onkeypress="javascript:numberAllowed()"></TD>
        <TD width="20%"><INPUT type="text" name="txtReplannedDefect" value="<%=beanModuleUpdate.getSetupModuleList().getCell(0, 4)%>" class="midBox" style="width: 100%" maxlength="8" onkeypress="javascript:numberAllowed()"></TD>
    </TR>
</TABLE>
<P align="center"><INPUT type="button" name="UpdateSetupModule" class="Button" onclick="javascript:doSave()" value="Save">&nbsp;&nbsp;&nbsp;
<INPUT type="button" name="Back" class="Button" onclick="javascript:doBack()" value="Back"></P>
<INPUT type="text" name="txtHidden" style="visibility: hidden"></FORM>
</BODY>
</HTML>