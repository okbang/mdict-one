<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language = "java" import = "java.util.*, javax.servlet.*,
            fpt.dms.bean.*, fpt.dms.bean.ProjectEnvironment.*,
            fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe = "false" errorPage = "error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo
            = (UserInfoBean)session.getAttribute("beanUserInfo");
    ModuleListBean beanModuleList
            = (ModuleListBean)request.getAttribute("beanModuleList");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT src="scripts/validate.js"></SCRIPT>
<TITLE>Module List</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT language="javascript">
function doAddNew() {
	alert("This function is deprecated and moved to FSoft Insight");
/*    var form = document.frmSetupModuleList;
    var strValue;
    strValue = form.txtName.value;
    strValue = trimSpaces(strValue);
    if (strValue.length == 0) {
        alert("Invalid module name.");
        form.txtName.focus();
        return;
    }
    strValue = form.txtPlannedDefect.value;
    strValue = trimSpaces(strValue);
    if (strValue.length == 0) {
        alert("Invalid planned defect.");
        form.txtPlannedDefect.focus();
        return;
    }
    if (!validateNumber(form.txtPlannedDefect)) {
        return;
    }
    if (!isPositiveNumberCombobox(form.cboWP)) {
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
    form.hidActionDetail.value = "AddModule";
    form.action = "DMSServlet";
    form.submit();*/
}

function doUpdate(id, name, planned, replanned, wpid, wpname) {
	alert("This function is deprecated and moved to FSoft Insight");
/*    var form = document.frmSetupModuleList;
    form.hidMID.value = id;
    form.hidMName.value = name;
    if (planned.length == 0) {
        planned = "0";
    }
    form.hidMPlanned.value = planned;
    form.hidMReplanned.value = replanned;
    form.hidMWPID.value = wpid;
    form.hidMWPName.value = wpname;
    form.hidAction.value = "PE";
    form.hidActionDetail.value = "UpdateModule";
    form.action = "DMSServlet";
    form.submit();*/
}

function doDelete() {
	alert("This function is deprecated and moved to FSoft Insight");
/*    var form = document.frmSetupModuleList;
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
    form.hidActionDetail.value = "DeleteModule";
    form.action = "DMSServlet";
    form.submit();*/
}

function doQueryListing() {
    var form = document.frmSetupModuleList;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function CheckAll() {
    for (var i = 0; i < document.frmSetupModuleList.elements.length; i++) {
        var e = document.frmSetupModuleList.elements[i];
        if (e.name != "allbox") {
            e.checked = document.frmSetupModuleList.allbox.checked;
        }
    }
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
<P><IMG border="0" src="Images/title_Module_Listing.gif" width="408" height="28"></P>
</DIV>
<FORM method="post" action="DMSServlet" name="frmSetupModuleList">
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
<BR>
<TABLE border="0" width="85%" cellspacing="0" cellpadding="0" align="center">
    <COLGROUP>
        <COL width="10%">
        <COL width="20%">
        <COL width="15%">
        <COL width="10%">
        <COL width="5%">
        <COL width="15%">
        <COL width="10%">
    <TR>
        <TD><B>Name</B><FONT color="red">*</FONT></TD>
        <TD><INPUT type="text" name="txtName" maxlength="50" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 130pt" tabindex="1"></TD>
        <TD><B>Planned Defect</B><FONT color="red">*</FONT></TD>
        <TD><INPUT type="text" name="txtPlannedDefect" size="10" maxlength="8" value="" tabindex="3" onkeypress="javascript:numberAllowed()">&nbsp;(wd)</TD>
        <TD>&nbsp;</TD>
    </TR>
    <TR>
        <TD><B>Work Product</B><FONT color="red">*</FONT></TD>
        <TD><SELECT name="cboWP" style="FONT-FAMILY: Arial; FONT-SIZE: 8pt; WIDTH: 130pt" tabindex="2"><%
    for (int i = 0; i < beanModuleList.getWorkProductList().getNumberOfRows(); i++) {
%>
            <OPTION value="<%=beanModuleList.getWorkProductList().getCell(i, 0)%>"><%=beanModuleList.getWorkProductList().getCell(i, 1)%></OPTION><%
    }
%>
        </SELECT><!-- planned/re-planned defect of each work product --><%
    for (int i = 0; i < beanModuleList.getWorkProductList().getNumberOfRows(); i++) {
        String strWPID = beanModuleList.getWorkProductList().getCell(i, 0);
        String strPlannedDefect = beanModuleList.getWorkProductList().getCell(i, 2);
        String strReplannedDefect = beanModuleList.getWorkProductList().getCell(i, 3);
%>
        <INPUT type="hidden" name="hidPlannedDefect_<%=strWPID%>" value="<%=strPlannedDefect%>"><INPUT type="hidden" name="hidReplannedDefect_<%=strWPID%>" value="<%=strReplannedDefect%>"><%
    }
%>
        </TD>
        <TD><B>Re-planned Defect</B></TD>
        <TD><INPUT type="text" name="txtReplannedDefect" size="10" maxlength="8" value="" tabindex="4" onkeypress="javascript:numberAllowed()">&nbsp;(wd)</TD>
        <TD align="right"><INPUT type="button" value="AddNew" name="AddnewSetupModule" class="Button" tabindex="5" onclick="javascript:doAddNew()"></TD>
    </TR>
</TABLE>
<BR>
<INPUT type="hidden" name="hidMID" value="">
<INPUT type="hidden" name="hidMName" value="">
<INPUT type="hidden" name="hidMPlanned" value="">
<INPUT type="hidden" name="hidMReplanned" value="">
<INPUT type="hidden" name="hidMWPID" value="">
<INPUT type="hidden" name="hidMWPName" value="">
<B><FONT color="#cc0001" style="FONT-FAMILY: Verdana; FONT-SIZE: 10pt"><%=beanModuleList.getClientMessage()%></FONT></B>
<P></P>
<TABLE width="100%">
    <TR>
        <TD align="right">(wd: Weighted Defect)</TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" bgcolor="#000000" align="center">
    <COLGROUP>
        <COL width="2%">
        <COL width="10%">
        <COL width="30%">
        <COL width="30%">
        <COL width="14%">
        <COL width="14%">
    <TR id="tabtitle" class="Row0">
        <TD height="19"><INPUT type="checkbox" name="allbox" value="CheckAll" onclick="JavaScript: CheckAll();"></TD>
        <TD align="center" height="19">Module ID</TD>
        <TD align="center" height="19">Module</TD>
        <TD align="center" height="19">Work Product</TD>
        <TD align="center" height="19">Planned Defect<BR>(wd)</TD>
        <TD align="center" height="19">Re-planned Defect<BR>(wd)</TD>
    </TR><%
    for (int i = 0; i < beanModuleList.getSetupModuleList().getNumberOfRows(); i++) {
        String strMID = beanModuleList.getSetupModuleList().getCell(i, 0);
        String strMName = beanModuleList.getSetupModuleList().getCell(i, 1);
        String strWPName = beanModuleList.getSetupModuleList().getCell(i, 2);
        String strPlannedDefect = beanModuleList.getSetupModuleList().getCell(i, 3);
        String strReplannedDefect = beanModuleList.getSetupModuleList().getCell(i, 4);
        String strWPID = beanModuleList.getSetupModuleList().getCell(i, 5);
%>
    <TR class="Row<%=(i + 1) % 2 +1%>">
        <TD><INPUT type="checkbox" name="checkBox" value="<%=strMID%>"></TD>
        <TD>&nbsp;<%=strMID%></TD>
        <TD style="text-indent: 10"><A href="javascript:doUpdate('<%=strMID%>', '<%=strMName%>', '<%=strPlannedDefect%>', '<%=strReplannedDefect%>', '<%=strWPID%>', '<%=strWPName%>')"> <%=strMName%></A></TD>
        <TD>&nbsp;<%=strWPName%></TD>
        <TD>&nbsp;<%=strPlannedDefect%></TD>
        <TD>&nbsp;<%=strReplannedDefect%></TD>
    </TR><%
    }
%>
</TABLE>
<P><INPUT type="button" class="button" name="DeleteSetupModule" onclick="javascript:doDelete()" value="Delete"></P>
<INPUT type="text" name="txtHidden" style="visibility: hidden"></FORM>
</BODY>
<SCRIPT language="JavaScript">
     document.forms[0].txtName.focus();
</SCRIPT>
</HTML>