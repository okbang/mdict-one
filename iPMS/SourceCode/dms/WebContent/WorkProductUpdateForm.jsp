<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.ProjectEnvironment.*,
            fpt.dms.framework.util.StringUtil.*,
            fpt.dms.bo.combobox.*,
            fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    WorkProductUpdateBean beanWorkProductUpdate
            = (WorkProductUpdateBean)request.getAttribute("beanWorkProductUpdate");
    String strClientMessage = (String)request.getAttribute("ClientMessageResult");
    if (strClientMessage == null) {
        strClientMessage = "";
    }
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<TITLE>Work Product Update</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT language="javascript">
function doSave() {
    var form = document.frmWorkProductSizeUpdate;
    var strUnit, strSize;
    strSize = form.txtSize.value;
    strSize = trimSpaces(strSize);
    if (strSize == 0) {
        alert("The value must not be empty!");
        form.txtSize.focus();
        return;
    }
    strUnit = form.cboUnit.value;
    if (strUnit == 0) {
        alert("The value must not be empty!");
        form.cboUnit.focus();
        return;
    }
    var strValue = form.txtPlannedDefect.value;
    strValue = trimSpaces(strValue);
    if (strValue.length == 0) {
        alert("Invalid planned defect.");
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
    form.hidActionDetail.value = "SaveUpdateWorkProduct";
    form.action = "DMSServlet";
    form.submit();
}

function doBack() {
    var form = document.frmWorkProductSizeUpdate;
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "WorkProductList";
    form.action = "DMSServlet";
    form.submit();
}

function doQueryListing() {
    var form = document.frmWorkProductSizeUpdate;
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
<P><IMG border="0" src="Images/title_Work_Product_Update.gif" width="435" height="28"></P>
</DIV>
<FORM method="post" action="DMSServlet" name="frmWorkProductSizeUpdate">
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
<P></P>
<TABLE border="0" cellpadding="1" cellspacing="1" align="center" width="95%">
    <TR>
        <TD width="100%" align="left"><B><FONT color="#cc0001" style="FONT-FAMILY: Verdana; FONT-SIZE: 10pt"><%=strClientMessage%></FONT></B>&nbsp;</TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="1" width="95%" bgcolor="#000000">
    <TR id="tabtitle" class="Row0">
        <TD align="center" width="40%" height="19">Work Product</TD>
        <TD align="center" width="15%" height="19">Size</TD>
        <TD align="center" width="15%" height="19">Unit</TD>
        <TD align="center" width="15%" height="19">Planned Defect<BR>(wd)</TD>
        <TD align="center" width="15%" height="19">Re-planned Defect<BR>(wd)</TD>
    </TR>
    <TR class="Row2">
        <INPUT type="hidden" name="hidWP_ID" value="<%=beanWorkProductUpdate.getWorkProductSizeList().getCell(0, 0)%>">
        <TD width="40%" align="left"><%=beanWorkProductUpdate.getWorkProductSizeList().getCell(0, 1)%>
        <INPUT type="hidden" name="hidWPName" value="<%=beanWorkProductUpdate.getWorkProductSizeList().getCell(0, 1)%>"></TD>
        <TD width="15%"><INPUT type="text" name="txtSize" onkeypress="javascript:numberAllowed()" value="<%=beanWorkProductUpdate.getWorkProductSizeList().getCell(0,2)%>" size="10" maxlength="8" style="width: 100%"></TD>
        <TD width="15%"><SELECT size="1" name="cboUnit" class="SmallCombo" style="width: 100%"><%
    for (int nRow = 0x00; nRow < beanWorkProductUpdate.getComboUnit().getNumberOfRows(); nRow++) {
        String strValue = beanWorkProductUpdate.getComboUnit().getCell(nRow, 0); //WP_ID
        int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
        String strText = beanWorkProductUpdate.getComboUnit().getCell(nRow, 1); //WP Name
        String strTextList = beanWorkProductUpdate.getWorkProductSizeList().getCell(0, 3); //Unit name
        if (strText.equals(strTextList)) {
            out.write("<OPTION");
            out.write(" selected ");
            out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
        }
        else {
            out.write("<OPTION");
            out.write(" ");
            out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
        }
    }
%>
        </SELECT></TD>
        <TD width="15%"><INPUT type="text" name="txtPlannedDefect" value="<%=beanWorkProductUpdate.getWorkProductSizeList().getCell(0, 4)%>" size="10" maxlength="8" style="width: 100%" onkeypress="javascript:numberAllowed()"></TD>
        <TD width="15%"><INPUT type="text" name="txtReplannedDefect" value="<%=beanWorkProductUpdate.getWorkProductSizeList().getCell(0, 5)%>" size="10" maxlength="8" style="width: 100%" onkeypress="javascript:numberAllowed()"></TD>
    </TR>
</TABLE>
<P><INPUT type="button" name="UpdateWorkProductSize" class="button" onclick="javascript:doSave()" value="Save">&nbsp;&nbsp;&nbsp;
<INPUT type="button" name="Back" class="button" onclick="javascript:doBack()" value="Back"></P>
</FORM>
</BODY>
</HTML>