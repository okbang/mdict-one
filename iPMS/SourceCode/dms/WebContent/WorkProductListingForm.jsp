<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language = "java" import = "java.util.*, javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.ProjectEnvironment.*,
            fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe = "false" errorPage = "error.jsp" contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo =
            (UserInfoBean)session.getAttribute("beanUserInfo");
    WorkProductListBean beanWorkProductList =
            (WorkProductListBean)request.getAttribute("beanWorkProductList");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<TITLE>Work Product List</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT language="javascript">
function doAddNew() {
	alert("This function is deprecated and moved to FSoft Insight");
/*    var form = document.frmWorkProductSizeList;
    var strWP, strUnit, strSize;
    strWP = form.cboWorkProduct.value;
    if (strWP == 0) {
        alert("The value must not be empty!");
        form.cboWorkProduct.focus();
        return;
    }
    strUnit = form.cboUnit.value;
    if (strUnit == 0) {
        alert("The value must not be empty!");
        form.cboUnit.focus();
        return;
    }
    strSize = form.txtSize.value;
    strSize = trimSpaces(strSize);
    if (strSize == 0) {
        alert("The value must not be empty!");
        form.txtSize.focus();
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
    strValue = form.txtReplannedDefect.value;
    strValue = trimSpaces(strValue);
    if (strValue.length > 0) {
        if (!validateNumber(form.txtReplannedDefect)) {
            return;
        }
    }
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "AddWorkProduct";
    form.action = "DMSServlet";
    form.submit();*/
}

function doUpdate(id, name, size, unit, planned, replanned) {
	alert("This function is deprecated and moved to FSoft Insight");
/*    var form = document.frmWorkProductSizeList;
    form.hidWPID.value = id;
    form.hidWPName.value = name;
    form.hidWPSize.value = size;
    form.hidWPUnit.value = unit;
    if (planned.length == 0) {
        planned = "0";
    }
    form.hidWPPlanned.value = planned;
    form.hidWPReplanned.value = replanned;
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "UpdateWorkProduct";
    form.action = "DMSServlet";
    form.submit();*/
}

function doDelete() {
	alert("This function is deprecated and moved to FSoft Insight");
/*    var form = document.frmWorkProductSizeList;
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
    if (!confirm("Do you want to delete selected records, continue?")) {
        return false;
    }
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "DeleteWorkProduct";
    form.action = "DMSServlet";
    form.submit();*/
}

function doQueryListing() {
    var form = document.frmWorkProductSizeList;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function CheckAll() {
    for (var i = 0; i < document.frmWorkProductSizeList.elements.length; i++) {
        var e = document.frmWorkProductSizeList.elements[i];
        if (e.name != "allbox") {
            e.checked = document.frmWorkProductSizeList.allbox.checked;
        }
    }
}

function numberAllowed() {
    if (window.event.keyCode > 57 || window.event.keyCode < 48) {
        window.event.keyCode    =   0;
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
<P><IMG border="0" src="Images/title_work_product_listing.gif" width="456" height="28"></P>
</DIV>
<FORM method="post" action="DMSServlet" name="frmWorkProductSizeList">
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
<TABLE border="0" width="95%" cellspacing="0" cellpadding="0" align="center">
    <COLGROUP>
        <COL width="13%">
        <COL width="25%">
        <COL width="15%">
        <COL width="25%">
        <COL width="5%">
        <COL width="10%">
    <TR>
        <TD><B>Work Product</B><FONT color="red">*</FONT></TD>
        <TD><SELECT name="cboWorkProduct" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 130pt"><%
    for (int i = 0; i < beanWorkProductList.getComboWorkProduct().getNumberOfRows(); i++) {
%>
            <OPTION value="<%=beanWorkProductList.getComboWorkProduct().getCell(i, 0)%>"><%=beanWorkProductList.getComboWorkProduct().getCell(i, 1)%></OPTION><%
    }
%>
        </TD>
        <TD><B>Unit</B><FONT color="red">*</FONT></TD>
        <TD><SELECT name="cboUnit" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 80pt"><%
    for (int i = 0; i < beanWorkProductList.getComboUnit().getNumberOfRows(); i++) {
%>
            <OPTION value="<%=beanWorkProductList.getComboUnit().getCell(i, 0)%>"><%=beanWorkProductList.getComboUnit().getCell(i, 1)%></OPTION><%
    }
%>
        </TD>
        <TD><B>Size</B><FONT color="red">*</FONT></TD>
        <TD><INPUT type="text" name="txtSize" onkeypress="javascript:numberAllowed()" size="10" maxlength="8"></TD>
    </TR>
    <TR>
        <TD><B>Planned Defect</B><FONT color="red">*</FONT></TD>
        <TD><INPUT type="text" name="txtPlannedDefect" style="WIDTH: 130pt" maxlength="8" value="" onkeypress="javascript:numberAllowed()">&nbsp;(wd)</TD>
        <TD><B>Re-planned Defect</B></TD>
        <TD><INPUT type="text" name="txtReplannedDefect" style="WIDTH: 80pt" maxlength="8" value="" onkeypress="javascript:numberAllowed()">&nbsp;(wd)</TD>
        <TD>&nbsp;</TD>
        <TD><INPUT type="button" class="button" name="AddnewWorkProductSize" onclick="javascript:doAddNew()" value="AddNew"></TD>
    </TR>
</TABLE>
<INPUT type="hidden" name="hidWPID" value="">
<INPUT type="hidden" name="hidWPName" value="">
<INPUT type="hidden" name="hidWPSize" value="">
<INPUT type="hidden" name="hidWPUnit" value="">
<INPUT type="hidden" name="hidWPPlanned" value="">
<INPUT type="hidden" name="hidWPReplanned" value=""> <BR>
<B><FONT color="#cc0001" style="FONT-FAMILY: Verdana; FONT-SIZE: 10pt"><%= beanWorkProductList.getClientMessage()%></FONT></B>
<P></P>
<TABLE width="100%">
    <TR>
        <TD align="right">(wd: Weighted Defect)</TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" bgcolor="#000000">
    <COLGROUP>
        <COL width="2%">
        <COL width="12%">
        <COL width="38%">
        <COL width="12%">
        <COL width="12%">
        <COL width="12%">
        <COL width="12%">
    <TR id="tabtitle" class="Row0">
        <TD align="center" height="19"><INPUT type="checkbox" name="allbox" value="CheckAll" onclick="JavaScript:CheckAll();"></TD>
        <TD align="center" height="19">WP ID</TD>
        <TD align="center" height="19">Work Product</TD>
        <TD align="center" height="19">Unit</TD>
        <TD align="center" height="19">Size</TD>
        <TD align="center" height="19">Planned Defect<BR>(wd)</TD>
        <TD align="center" height="19">Re-planned Defect<BR>(wd)</TD>
    </TR><%
    for (int i = 0; i < beanWorkProductList.getWorkProductSizeList().getNumberOfRows(); i++) {
        String strWPID = beanWorkProductList.getWorkProductSizeList().getCell(i, 0);
        String strWPName = beanWorkProductList.getWorkProductSizeList().getCell(i, 1);
        String strSize = beanWorkProductList.getWorkProductSizeList().getCell(i, 2);
        String strUnit = beanWorkProductList.getWorkProductSizeList().getCell(i, 3);
        String strPlannedDefect = beanWorkProductList.getWorkProductSizeList().getCell(i, 4);
        String strReplannedDefect = beanWorkProductList.getWorkProductSizeList().getCell(i, 5);
%>
    <TR class="Row<%=(i + 1) % 2 + 1%>">
        <TD><INPUT type="checkbox" name="checkBox" value="<%=strWPID%>"></TD>
        <TD><%=strWPID%></TD>
        <TD style="text-indent: 5px"><A href="javascript:doUpdate('<%=strWPID%>', '<%=strWPName%>', '<%=strSize%>', '<%=strUnit%>', '<%=strPlannedDefect%>', '<%=strReplannedDefect%>')"> <%=strWPName%></A></TD>
        <TD><%=strUnit%></TD>
        <TD><%=strSize%></TD>
        <TD><%=strPlannedDefect%></TD>
        <TD><%=strReplannedDefect%></TD>
    </TR><%
    }
%>
</TABLE>
<P><INPUT type="button" class="button" name="DeleteWorkProductSize" onclick="javascript:doDelete()" value="Delete"></P>
</FORM>
</BODY>
</HTML>