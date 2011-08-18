<%@ page language="java" import="javax.servlet.*, fpt.ncms.bean.*,
        fpt.ncms.util.StringUtil.*, fpt.ncms.constant.NCMS"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ConstantListBean beanConstantList = (ConstantListBean)session.getAttribute("beanConstantList");
    int i = 0;
    /** Number of pages */
    int nTotalPage = (beanConstantList.getTotal() % NCMS.NUM_CONST_PER_PAGE == 0)
            ? Math.round(beanConstantList.getTotal() / NCMS.NUM_CONST_PER_PAGE)
            : Math.round(beanConstantList.getTotal() / NCMS.NUM_CONST_PER_PAGE) + 1;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
<TITLE>Constant List</TITLE>
<SCRIPT language="javascript">
function doUpdate() {
    var nTotalRows;
    nTotalRows = 0;
    for (var i = 0; i < frmConstantList.elements.length; i++) {
        var e = frmConstantList.elements[i];
        if ((e.name == "checkbox") && (e.type == "checkbox") && (e.checked == 1)) {
            nTotalRows++;
        }
    }
    if (nTotalRows == 0) {
        alert("You must select a record!");
        return false;
    }
    else {
        document.frmConstantList.hidAction.value = "<%=NCMS.CONSTANT_UPDATE%>";
        document.frmConstantList.action = "NcmsServlet";
        document.frmConstantList.submit();
    }
}

function doAddConstant() {
    document.frmConstantList.action="NcmsServlet";
    document.frmConstantList.hidAction.value = "<%=NCMS.CONSTANT_ADD%>";
    document.frmConstantList.submit();
}

function doDelete() {
    var nTotalRows;
    nTotalRows = 0;
    for (var i = 0; i < frmConstantList.elements.length; i++) {
        var e = frmConstantList.elements[i];
        if ((e.name == "checkbox") && (e.type == "checkbox") && (e.checked == 1)) {
            nTotalRows++;
        }
    }
    if (nTotalRows == 0) {
        alert("You must select a record!");
        return false;
    }
    else {
        document.frmConstantList.hidAction.value = "<%=NCMS.CONSTANT_DELETE%>";
        document.frmConstantList.action = "NcmsServlet";
        if (confirm("Are you sure to permanently delete all checked record(s)?")) {
            document.frmConstantList.submit();
        }
        else {
            return false;
        }
    }
}

function doCheckAll() {
    var e;
    for (var i = 0; i < document.frmConstantList.elements.length; i++) {
        e = document.frmConstantList.elements[i];
        if (e.name == 'checkbox' && e.type== 'checkbox') {
            e.checked = document.frmConstantList.chkCheckAll.checked;
        }
    }
}

function doSort() {
    if (frmConstantList.PageNumber != null) {
        frmConstantList.PageNumber.value = "1";
    }
    frmConstantList.hidAction.value = "<%=NCMS.CONSTANT_LIST%>";
    frmConstantList.action = "NcmsServlet";
    frmConstantList.submit();
}

function doList() {
<%if (beanUserInfo.getLocation() == 0) {%>
    frmConstantList.hidAction.value = "<%=NCMS.NC_LIST%>";
<%} else {//if (beanUserInfo.getLocation() == 1) {%>
    frmConstantList.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
<%}%>    
    frmConstantList.action = "NcmsServlet";
    frmConstantList.submit();
}

function doLogOut() {
    frmConstantList.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmConstantList.action = "NcmsServlet";
    frmConstantList.submit();
}

function goNavNext() {
    frmConstantList.PageNumber.value = <%=beanConstantList.getNumPage() + 1%>;
    frmConstantList.hidAction.value = "<%=NCMS.CONSTANT_LIST%>";
    frmConstantList.action = "NcmsServlet";
    frmConstantList.submit();
}

function goNavBack() {
    frmConstantList.PageNumber.value = <%=beanConstantList.getNumPage() - 1%>;
    frmConstantList.hidAction.value = "<%=NCMS.CONSTANT_LIST%>";
    frmConstantList.action = "NcmsServlet";
    frmConstantList.submit();
}

function goNavPage() {
    if (isNaN(frmConstantList.PageNumber.value)) {
        alert("Invalid page number.");
        return false;
    }
    if ((frmConstantList.PageNumber.value > <%=nTotalPage%>) || (frmConstantList.PageNumber.value < 1)) {
        alert("Page number out of range.");
        return false;
    }
    frmConstantList.hidAction.value = "<%=NCMS.CONSTANT_LIST%>";
    frmConstantList.action = "NcmsServlet";
    frmConstantList.submit();
}

function pressKey() {
    if (window.event.keyCode == 13) {
        goNavPage();
    }
    if ((window.event.keyCode < 48) || (window.event.keyCode > 57)) {
        window.event.keyCode = 0;
    }
}
</SCRIPT>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<%if (beanUserInfo.getLocation() == 0) {%>
<%@ include file="Header.jsp"%>
<%}else {//if (beanUserInfo.getLocation() == 1) {%>
<%@ include file="HeaderCallLog.jsp"%>
<%}%>
<TABLE class="menu" cellpadding="0" cellspacing="0" width="100%" height="20pt">
    <TR>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doAddConstant()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Add&nbsp;&nbsp;</P></TD><%
        // NCMS
        if (beanUserInfo.getLocation() == 0) {%>
        <TD align="right" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" style="cursor:hand" onclick="javascript:doList()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">NCMS&nbsp;&nbsp;</P></TD><%
        }
        // CallLog
        else {//if (beanUserInfo.getLocation() == 1) {%>
        <TD align="right"><P class="menuitem" style="cursor:hand" onclick="javascript:doList()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">CallLog&nbsp;&nbsp;</P></TD><%
        }%>
        <TD align="right" width="5%"><P class="menuitem" style="cursor:hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">Logout&nbsp;</P></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="44%" valign="top" align="left">
        <P><IMG src="images/Headers/constantList.gif"></P>
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
<FORM method="post" name="frmConstantList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<INPUT type="hidden" name="hidMode" value="<%=beanUserInfo.getListingMode()%>">
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%">
    <TR>
        <TD align="left" width="30%">Constant Type:&nbsp;<SELECT name="cboSortBy" onchange="doSort()" style="width: 100pt"><%
    for (i = 0; i < beanConstantList.getComboSortBy().getNumberOfRows(); i++) {
        out.write("<OPTION value=\"" +
                  beanConstantList.getComboSortBy().getCell(i, 0) +
                  (beanConstantList.getComboSortBy().getCell(i, 0).equals(beanConstantList.getSortBy()) ? "\" selected>" : "\">") +
                  beanConstantList.getComboSortBy().getCell(i, 1) + "</OPTION>");
    }%>
        </SELECT></TD>
        <TD align="right">
        <TABLE border="0" cellpadding="0" cellspacing="0" width="20%">
            <TR>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="25"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="25"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="70"></TD>
                <TD><IMG border="0" src="images/Invisible.gif" height="1" width="25"></TD>
            </TR>
            <TR><%
    if (beanConstantList.getTotal() > NCMS.NUM_CONST_PER_PAGE) {
%>
                <TD><%
        if (beanConstantList.getNumPage() > 1) {
%>
                <IMG border="0" src="images/Buttons/b_previous_n.gif" onmouseout="this.src='images/Buttons/b_previous_n.gif'" onmouseover="this.src='images/Buttons/b_previous_p.gif'" onclick="javascript:goNavBack()" align="bottom"></TD><%
        }
%>
                <TD><IMG border="0" src="images/Buttons/b_go_n.gif" onmouseout="this.src='images/Buttons/b_go_n.gif'" onmouseover="this.src='images/Buttons/b_go_p.gif'" onclick="javascript:goNavPage()" align="bottom"></TD>
                <TD><INPUT type="text" name="PageNumber" value="<%=beanConstantList.getNumPage()%>" size="2" onkeypress="pressKey()">of <%=nTotalPage%></TD><%
        if (beanConstantList.getNumPage() < nTotalPage) {
%>
                <TD><IMG border="0" src="images/Buttons/b_next_n.gif" onmouseout="this.src='images/Buttons/b_next_n.gif'" onmouseover="this.src='images/Buttons/b_next_p.gif'" onclick="javascript:goNavNext()" align="bottom"></TD><%
        }
        else {
%>
                <TD></TD><%
        }
    }
%>
            </TR></TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE bgcolor="#9292CB" border="0" cellpadding="0" cellspacing="1" style="border-collapse: collapse" bordercolor="#111111" width="100%">
    <TR id="tabtitle" class="header">
        <TD width="4%" height="21" align="center"><INPUT name="chkCheckAll" type="checkbox" onclick="doCheckAll()"></TD>
        <TD width="20%" height="21" align="center">Constant ID</TD>
        <TD width="34%" align="center">Value</TD>
        <TD width="42%" align="center">Constant Type</TD>
    </TR><%
    for (i = 0; i < NCMS.NUM_CONST_PER_PAGE; i++) {
        if (i + (beanConstantList.getNumPage() - 1) * NCMS.NUM_CONST_PER_PAGE >= beanConstantList.getTotal()) {
            break;
        }
%>
    <TR id="tabcontent" class="row<%=i % 2 +1%>" onclick="javascript:document.frmConstantList('<%=i%>').click()" style="cursor: hand" onmouseout="this.style.color='#000000';" onmouseover="this.style.color='#FF0000';">
        <TD align="center"><INPUT type="checkbox" name="checkbox" id="<%=i%>" value='<%=beanConstantList.getConstantList().getCell(i, 0)%>'></TD>
        <TD><%=beanConstantList.getConstantList().getCell(i, 0)%></TD>
        <TD><%=beanConstantList.getConstantList().getCell(i, 1)%></TD>
        <TD><%=beanConstantList.getConstantList().getCell(i, 2)%></TD>
    </TR><%
    }
    if (i == 0) {%>
    <TR><TD colspan="4" class="row1">No record found! </TD></TR><%
    }
%>
</TABLE>
</FORM>
<TABLE align="left" width="100%">
    <TR>
        <TD width="70%">&nbsp; <IMG alt="Add New" name="imgAddNew" style="cursor: hand" onclick="doAddConstant()" src="images/Buttons/b_add_n.gif" width="64" height="23" onmouseout="this.src='images/Buttons/b_add_n.gif'" onmouseover="this.src='images/Buttons/b_add_p.gif'">
        <IMG name="imgEdit" alt="Update" style="cursor: hand" onclick="doUpdate()" tabindex="1" src="images/Buttons/b_change_n.gif" width="64" height="23" onmouseout="this.src='images/Buttons/b_change_n.gif'" onmouseover="this.src='images/Buttons/b_change_p.gif'">
        <IMG name="imgDelete" alt="Delete" style="cursor: hand" onclick="doDelete()" tabindex="2" src="images/Buttons/b_delete_n.gif" width="64" height="23" onmouseout="this.src='images/Buttons/b_delete_n.gif'" onmouseover="this.src='images/Buttons/b_delete_p.gif'"></TD>
    </TR>
</TABLE>
</BODY>
</HTML>