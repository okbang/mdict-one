<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.DefectManagement.*,
            fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    DefectListingBean beanDefectUserList
            = (DefectListingBean)request.getAttribute("beanDefectUserList");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
    
    String strSortBy = beanDefectUserList.getSortBy();
    int nDirection = beanDefectUserList.getDirection();
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<TITLE>Defect User List</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT language="javascript">
function doSearch() {
    var form = document.frmDefectUserList;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doSort(type) {
    var form = document.frmDefectUserList;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.action = "DMSServlet";
    if (type == "Next") {
        if(Next())  form.submit();
    }
    else if(type =="Prev") {
        if(Prev()) form.submit();
    }
    else {
	    // Reverse direction of current sorted column
	    if (form.SortBy.value == type) {
	        if (form.Direction.value > 0) {
	            form.Direction.value = 0;
	        }
	        else {
	            form.Direction.value = 1;
	        }
	    }
	    // New column
	    else {
	        // Sort descending for date fields
	        if ((type == 'FixedDate') || (type == 'DueDate')) {
	            form.Direction.value = 0;
	        }
	        // Ascending for others
	        else {
                form.Direction.value = 1;
	        }
	    }
        form.SortBy.value = type;
        form.numPage.value = 0;
        form.submit();
    }
}

function doQueryListing() {
    var form = document.frmDefectUserList;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function Next() {
    var num;
    num = parseInt(document.forms[0].numPage.value);
    if (num < (document.forms[0].totalPage.value - 1)) {
        num++;
        document.forms[0].numPage.value = num;
        return true;
    }
    return false;
}

function Prev() {
    var num;
    num = parseInt(document.forms[0].numPage.value);
    if (num > 0) {
        num--;
        document.forms[0].numPage.value = num;
        if (num < 1) {
            num = 1;
        }
        return true;
    }
    return false;
}

function numberAllowed() {
    if (window.event.keyCode > 57 || window.event.keyCode < 48) {
        if (window.event.keyCode != 13) {
            window.event.keyCode = 0;
        }
    }
}

function doGoPage() {
    if (isNonNegativeInteger(document.forms[0].txtPage.value - 1)) {
        if ((parseInt(document.forms[0].txtPage.value)) > parseInt(document.forms[0].totalPage.value)) {
            alert("Invalid page.");
            return false;
        }
        else {
            document.forms[0].numPage.value = document.forms[0].txtPage.value - 1;
            document.forms[0].hidAction.value = "DM";
            document.forms[0].hidActionDetail.value = "SearchDefect";
            document.forms[0].action = "DMSServlet";
            document.forms[0].submit();
        }
    }
    else {
        alert("Invalid number.");
        document.forms[0].txtPage.focus();
        document.forms[0].txtPage.select();
        return false;
    }
}

function doAddNew() {
    var form = document.frmDefectUserList;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "AddDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doUpdate(defect_id) {
    var form = document.frmDefectUserList;
    form.hidUpdateDefect.value = defect_id;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "UpdateDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doBatchUpdate() {
    var form = document.frmDefectUserList;
    if (checkValid()) {
        form.hidAction.value = "DM";
        form.hidActionDetail.value = "BatchUpdateDefect";
        form.action = "DMSServlet";
        form.submit();
    }
    return;
}

function doRefresh() {
    var form = document.frmDefectUserList;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doExport() {
    var form = document.frmDefectUserList;
    form.hidExportAll.value = "true";   //Export all defects
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "ExportDefect";
    form.action = "DMSServlet";
    form.submit();
}

function checkValid() {
    var flag = true;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == "selected" && e.type == "checkbox") {
            if (e.checked == 1) {
                flag = false;
            }
        }
    }
    if (flag) {
        alert("Please select defects to do this action!");
        return false;
    }
    return true;
}

function CheckAll(form) {
    for (var i = 0; i < form.elements.length; i++) {
        var e = form.elements[i];
        if (e.name != "allbox") {
            e.checked = form.allbox.checked;
        }
    }
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" onkeypress="javascript:if (window.event.keyCode == 13) generateEvent(document.forms[0].GoPage)">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<DIV>
<P><IMG border="0" src="Images/DefectListing.gif" width="411" height="28"></P>
</DIV>
<FORM method="POST" action="DMSServlet" name="frmDefectUserList">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="SortBy" value="<%=beanDefectUserList.getSortBy()%>">
<INPUT type="hidden" name="Direction" value="<%=beanDefectUserList.getDirection()%>">
<INPUT type="hidden" name="userRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="numPage" value="<%=beanDefectUserList.getNumpage()%>">
<INPUT type="hidden" name="totalPage" value="<%=beanDefectUserList.getTotalpage()%>">
<INPUT type="hidden" name="hidUpdateDefect" value="">
<INPUT type="hidden" name="hidExportAll" value="false">
<INPUT type="hidden" name="hidTypeOfView" value="<%=beanUserInfo.getTypeOfView()%>">
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
        <TD width="22%" align="right"><SELECT name="cboProjectList" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="SearchDefect"%>','<%=""%>');"><%
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
        <TD width="22%" align="right"><SELECT name="cboProjectStatus" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="SearchDefect"%>','<%=""%>');"><%
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
<TABLE border="0" width="100%" cellpadding="0" cellspacing="0">
    <TR><%
    if (Integer.parseInt(beanDefectUserList.getTotalRecord()) > 0) {
%>
        <TD height="10" valign="bottom"><%
        int nItem = 20;
        int numPage = Integer.parseInt(beanDefectUserList.getNumpage());
        if (beanDefectUserList.getDefectList().getNumberOfRows() > 0) {
%>
        <B>Result <FONT color="red" size="-1"><%=numPage * nItem + 1%> - <%=numPage * nItem + beanDefectUserList.getDefectList().getNumberOfRows()%></FONT> of
        <FONT color="red" size="-1"><%=beanDefectUserList.getTotalRecord()%></FONT> defects in
        <FONT color="red" size="-1"><%=beanDefectUserList.getTotalpage()%></FONT> pages</B> <%
        }
        else {
%>
        <B> Result <FONT color="red" size="-1">0 - 0 </FONT> of
        <FONT color="red" size="-1">0</FONT> defects in
        <FONT color="red" size="-1">0</FONT> pages</B> <%
        }
%>
        </TD>
        <TD align="right" height="10" valign="top"><%
        if (Integer.parseInt(beanDefectUserList.getTotalRecord()) > 20) {
            if (Integer.parseInt(beanDefectUserList.getNumpage()) > 0) {
%>
        <A class="HeaderMenu" href="javascript:doSort('Prev')">Prev</A> &nbsp;&nbsp; <%
            }
            if (Integer.parseInt(beanDefectUserList.getNumpage()) + 1 < Integer.parseInt(beanDefectUserList.getTotalpage())) {
%>
        <A class="HeaderMenu" href="javascript:doSort('Next')">Next</A>&nbsp;&nbsp;&nbsp; <%
            }
%>
        <INPUT type="text" onkeypress="javascript:numberAllowed()" size="4" name="txtPage" maxlength="5" value="<%=Integer.parseInt(beanDefectUserList.getNumpage()) + 1%>">
        <INPUT type="button" name="GoPage" class="Button" onclick="javascript:doGoPage()" value="Go"></TD><%
        }
    }
    else {
%>
        <TD height="10" valign="bottom"><B> Total defects:&nbsp;<FONT color="red" size="-1">0</FONT></B></TD><%
    }
%>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" bgcolor="#000000">
    <COLGROUP>
        <COL width="3%">
        <COL width="5%">
        <COL width="35%">
        <COL width="10%">
        <COL width="11%">
        <COL width="8%">
        <COL width="6%">
        <COL width="8%">
        <COL width="8%">
        <COL width="8%">
    <TR class="Row0">
        <TD align="center" valign="middle"><INPUT type="checkbox" name="allbox" value="CheckAll" onclick="JavaScript: CheckAll(document.forms[0]);"></TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('DefectID')"><SPAN style="color: white">DefectID</SPAN></A>
<%
    if (strSortBy.equals("DefectID")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DefectID')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DefectID')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('Title')"><SPAN style="color: white">Title</SPAN></A>
<%
    if (strSortBy.equals("Title")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Title')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Title')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('Severity')"><SPAN style="color: white">Severity</SPAN></A>
<%
    if (strSortBy.equals("Severity")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Severity')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Severity')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('Priority')"><SPAN style="color: white">Priority</SPAN></A>
<%
    if (strSortBy.equals("Priority")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Priority')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Priority')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('Status')"><SPAN style="color: white">Status</SPAN></A>
<%
    if (strSortBy.equals("Status")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Status')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Status')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
       <TD align="center" valign="middle"><A href="javascript:doSort('DefectOwner')"><SPAN style="color: white">Defect Owner</SPAN></A>
<%
    if (strSortBy.equals("DefectOwner")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DefectOwner')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DefectOwner')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
       <TD align="center" valign="middle"><A href="javascript:doSort('AssignTo')"><SPAN style="color: white">Assigned To</SPAN></A>
<%
    if (strSortBy.equals("AssignTo")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('AssignTo')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('AssignTo')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('CreatedBy')"><SPAN style="color: white">Created By</SPAN></A>
<%
    if (strSortBy.equals("CreatedBy")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('CreatedBy')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('CreatedBy')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('FixedDate')"><SPAN style="color: white">Fixed Date</SPAN></A>
<%
    if (strSortBy.equals("FixedDate")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('FixedDate')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('FixedDate')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD align="center" valign="middle"><A href="javascript:doSort('DueDate')"><SPAN style="color: white">Due Date</SPAN></A>
<%
    if (strSortBy.equals("DueDate")) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DueDate')\" src='Images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('DueDate')\" src='Images/arrow_down.gif'>");
        }
    }
%>
        </TD>
    </TR><%
    for(int i = 0; i < beanDefectUserList.getDefectList().getNumberOfRows(); i++) {
%>
    <TR class="Row<%=(i + 1) % 2 + 1%>">
        <TD align="center"><INPUT type="checkbox" name="selected" value="<%=beanDefectUserList.getDefectList().getCell(i, 0)%>"></TD>
<!--    <TD align="center"><A href="javascript:doUpdate('<%=beanDefectUserList.getDefectList().getCell(i, 0)%>')"><%=beanDefectUserList.getDefectList().getCell(i, 0)%></A></TD>-->
        <TD align="center"><A href="DMSServlet?hidAction=DM&hidActionDetail=UpdateDefect&hidUpdateDefect=<%=beanDefectUserList.getDefectList().getCell(i, 0)%>&ProjectID=<%=beanUserInfo.getProjectID()%>"><%=beanDefectUserList.getDefectList().getCell(i, 0)%></A></TD>
        <TD><%=beanDefectUserList.getDefectList().getCell(i, 1)%></TD>
        <TD><%=beanDefectUserList.getDefectList().getCell(i, 2)%></TD>
        <TD><%=beanDefectUserList.getDefectList().getCell(i, 3)%></TD>
        <TD><%=beanDefectUserList.getDefectList().getCell(i, 4)%></TD>
        <TD><%=beanDefectUserList.getDefectList().getCell(i, 12)%></TD>
        <TD><%=beanDefectUserList.getDefectList().getCell(i, 5)%></TD>
        <TD><%=beanDefectUserList.getDefectList().getCell(i, 7)%></TD>
        <TD align="center"><%=beanDefectUserList.getDefectList().getCell(i, 8)%></TD>
        <TD align="center"><%=beanDefectUserList.getDefectList().getCell(i, 6)%></TD>
    </TR><%
    }
%>
</TABLE>
<BR>
<TABLE border="0" cellspacing="1" cellpadding="0" width="100%"><%
    String strRole = beanUserInfo.getRole();
    String strPosition = beanUserInfo.getPosition();
    String strIsDisabledMove = "disabled";
    String strIsDisabledDelete = "";
    if (strPosition.charAt(2) != '1' && strPosition.charAt(1) != '1') {
        strIsDisabledDelete = "disabled";
    }
    if (strRole.substring(4, 5).equals("1")) {
        strIsDisabledMove = "";
    }
%>
    <TR>
        <TD width="50%" align="left"><INPUT type="button" name="AddnewDefect" class="Button" onclick="javascript:doAddNew()" value="Add">&nbsp;&nbsp;
        <INPUT type="button" name="BatchUpdateDefect" class="Button" onclick="javascript:doBatchUpdate()" value="Batch Update">&nbsp;&nbsp;
        <INPUT type="button" name="Refresh" class="Button" onclick="javascript:doRefresh()" value="Refresh">&nbsp;&nbsp;
        <INPUT type="button" name="ExportDefect" class="Button" onclick="javascript:doExport()" value="Export"></TD>
    </TR>
</TABLE>
<INPUT type="text" name="txtHidden" style="visibility: hidden"></FORM>
</BODY>
</HTML>