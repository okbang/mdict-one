<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java"
    import="java.util.*,
    javax.servlet.*,
    fpt.dashboard.bean.*,
    fpt.dashboard.bean.StaffManagement.*,
    fpt.dashboard.util.StringUtil.*,
    fpt.dashboard.constant.DB"%><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    StaffListBean beanStaffList = (StaffListBean)request.getAttribute("beanStaffList");
    String strSortBy = beanStaffList.getSortBy();
    String strDirection = beanStaffList.getDirection();
    int nDirection = 0;
    if ((strDirection != null) && (strDirection.length() > 0)) {
        nDirection = Integer.parseInt(strDirection);
    }
    int nPages = (beanStaffList.getTotalRecords() + DB.PAGE_SIZE_STAFF - 1) / DB.PAGE_SIZE_STAFF;
    int nCurrentPage = beanStaffList.getPageNumber();
    if ((nCurrentPage <= 0) || (nCurrentPage > nPages)) {
    	nCurrentPage = 1;
    }
    int nFirstRow = (nCurrentPage - 1) * DB.PAGE_SIZE_STAFF + 1;
    int nLastRow = nCurrentPage * DB.PAGE_SIZE_STAFF;
    if (nLastRow > beanStaffList.getTotalRecords()) {
    	nLastRow = beanStaffList.getTotalRecords();
    }
    String[] arrGroup = beanStaffList.getArrGroup();
    String strGroup = beanStaffList.getSelectedGroup();
	
	// Disable external user from change group combo box
    String strDisabled = "disabled";
    if (beanUserInfo.getRole().equals("2")) {
        strDisabled = "";
    }
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>StaffList.jsp</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY bgcolor="#FFFFFF">
<%
    String strTitle = "Staff List";
%>
<%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmStaffList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidStaffID" value="">
<INPUT type="hidden" name="hidSortBy" value="<%=strSortBy%>">
<INPUT type="hidden" name="hidDirection" value="<%=nDirection%>">
<INPUT type="hidden" name="hidSelectedGroup" value="<%=beanStaffList.getSelectedGroup()%>">
<INPUT type="hidden" name="hidPageNumber" value="<%=nCurrentPage%>">
<CENTER>
<TABLE border="0" width="90%" cellspacing="1" cellpadding="0">
    <TR>
        <TD width="8%" height="25"><B>Group</B></TD>
        <TD width="22%" height="25">
			<SELECT name="cboGroup" size="1" style="width: 120" onchange="javascript:doFilter();" <%=strDisabled%>>
            <%
    for (int i = 0; i < arrGroup.length; i++) {
%>
				<OPTION value="<%=arrGroup[i]%>" <%=(strGroup.equals(arrGroup[i]) ? " selected" : "")%>><%=arrGroup[i]%></OPTION><%
    }
%>
	        </SELECT>
	    </TD>
        <TD width="8%" height="25"><B>Account</B></TD>
        <TD width="27%" height="25">
    	    <INPUT type="text" size="27" name="txtAccount" value="<%=beanStaffList.getAccount()%>" maxlength="20">
        </TD>
        <TD width="8%" height="25"></TD>
        <TD width="27%" height="25"></TD>
    </TR>
    <TR>
        <TD width="8%" valign="middle"><B>Status</B></TD>
        <TD width="22%" height="25">
			<SELECT name="cboStaffStatus" size="1" style="width: 120" onchange="javascript:doFilter();"><%
    for (int nRow = 0x00; nRow < beanStaffList.getStatusList().getNumberOfRows(); nRow++) {
        String strText = beanStaffList.getStatusList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanStaffList.getStatus() != null) {
            out.write(beanStaffList.getStatus().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">"
                + beanStaffList.getStatusList().getCell(nRow, 1) + "</OPTION>");
    }
%>
	        </SELECT>
	    </TD>
        <TD width="8%" height="25"><B>Name</B></TD>
        <TD width="27%" height="25">
	        <INPUT type="text" size="27" name="txtName" value="<%=beanStaffList.getName()%>" maxlength="50">
	    </TD>
	    <TD width="8%" height="25">
	    	<INPUT type="button" value="Search" onclick="doFilter()">
	    </TD>
	    <TD width="27%" height="25"></TD>
    </TR>
</TABLE><BR><%
	if (nPages > 0) {
%>
<TABLE border="0" width="90%" cellspacing="1" cellpadding="0">
    <TR>
        <TD width="50%" align="left" height="25"><B>Result</B>&nbsp;<%=nFirstRow + "-" + nLastRow%>&nbsp;<B>of</B>&nbsp;<%=beanStaffList.getTotalRecords()%>
        &nbsp;<B>in</B>&nbsp;<%=nPages%>&nbsp;<B><%=nPages > 1 ? "pages" : "page"%></B></TD><%
		if (nPages > 1) {
%>
        <TD width="50%" align="left" height="25">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>Page:</B>&nbsp;
        <SELECT name="lstPage" onchange="changePage()"><%
        	for (int i = 1; i <= nPages; i++) {
        		out.write("<OPTION value=" + i + (i == nCurrentPage ? " selected" : "") + ">" + i + "</OPTION>");
        	}
%>
		</SELECT>&nbsp;&nbsp;<%
        	if (nCurrentPage > 1) {
        		out.write("<A href='javascript:prevPage()' title='Previous page'>Prev</A>&nbsp;");
        	}
        	else {
        		out.write("<U><FONT color='gray'>Prev</FONT></U>&nbsp;");
        	}
        	if (nCurrentPage < nPages) {
        		out.write("<A href='javascript:nextPage()' title='Next page'>Next</A>");
        	}
        	else {
        		out.write("<U><FONT color='gray'>Next</FONT></U>&nbsp;");
        	}
%>
        </TD><%
		}
%>
    </TR>
</TABLE><%
	}
%>
<TABLE border="0" width="90%" cellspacing="1" cellpadding="0">
    <TR class="table_header">
        <TD width="3%"><B>D</B></TD>
        <TD width="12%" align="center" valign="middle"><A href="javascript:doSort('Account')"><SPAN style="color: white">Account</SPAN></A><%
    if ("Account".equals(strSortBy)) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Account')\" src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Account')\" src='images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD width="25%"><A href="javascript:doSort('Name')"><SPAN style="color: white">Name</SPAN></A><%
    if ("Name".equals(strSortBy)) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Name')\" src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Name')\" src='images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD width="22%"><A href="javascript:doSort('Designation')"><SPAN style="color: white">Designation</SPAN></A><%
    if ("Designation".equals(strSortBy)) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Designation')\" src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Designation')\" src='images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD width="10%"><A href="javascript:doSort('Status')"><SPAN style="color: white">Status</SPAN></A><%
    if ("Status".equals(strSortBy)) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Status')\" src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Status')\" src='images/arrow_down.gif'>");
        }
    }
%>
        </TD>
        <TD width="8%"><A href="javascript:doSort('Group_Name')"><SPAN style="color: white">Group</SPAN></A><%
    if ("Group_Name".equals(strSortBy)) {
        if (nDirection > 0) {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Group_Name')\" src='images/arrow_up.gif'>");
        }
        else {
            out.write("&nbsp;<IMG style=\"cursor: hand\" onclick=\"doSort('Group_Name')\" src='images/arrow_down.gif'>");
        }
    }
%>
        </TD>
    </TR>
    <%
    for (int i = 0; i < beanStaffList.getDeveloperList().getNumberOfRows(); i++) {
%>
    <TR class="row<%=(i + 1) % 2 + 1%>">
        <TD width="5%" height="10" align="center"><INPUT type="checkbox"
            name="dev_id"
            value="<%=beanStaffList.getDeveloperList().getCell(i, 0)%>"></TD>
        <TD width="12%" height="10" align="left"><A
            href="javascript:doUpdate('<%=beanStaffList.getDeveloperList().getCell(i, 0)%>')"><%=beanStaffList.getDeveloperList().getCell(i, 6)%></A></TD>
        <TD width="20%" height="10" align="left"><%=beanStaffList.getDeveloperList().getCell(i, 1)%></TD>
        <TD width="20%" height="10" align="left"><%=beanStaffList.getDeveloperList().getCell(i, 2)%></TD>
        <TD width="15%" height="10" align="left"><%=beanStaffList.getDeveloperList().getCell(i, 5)%></TD>
        <TD width="8%" height="10" align="left"><%=beanStaffList.getDeveloperList().getCell(i, 3)%></TD>
    </TR>
    <%
    }
%>
</TABLE>
<TABLE border="0" width="90%">
    <TR>	   	
        <TD width="100%" align="left">
        <INPUT type="button" name="DoAddNewDeveloper"
        	onclick="javascript:doAdd()" value="Add"
            <%
    if (!"1".equals(beanUserInfo.getSRole().substring(4, 5))) {
        out.print("disabled");
    }
%>>
        <INPUT type="button" name="DoDeleteDeveloper"
            onclick="javascript:doDelete()" value="Delete"
            <%
    if (!"1".equals(beanUserInfo.getSRole().substring(4, 5))) {
        out.print("disabled");
    }
%>>
        </TD>
    </TR>
</TABLE>
</CENTER>
</FORM>
<SCRIPT language="javascript">
var form = document.frmStaffList;

function doFilter() {
    form.hidAction.value = "SM";
    form.hidActionDetail.value = "ViewStaffList";
    form.action = "DashboardServlet";
    form.submit();
}

function changePage() {
	doFilter();
}

function nextPage() {
	form.lstPage.selectedIndex = form.lstPage.selectedIndex + 1;
	doFilter();
}

function prevPage() {
	form.lstPage.selectedIndex = form.lstPage.selectedIndex - 1;
	doFilter();
}

function doSort(type) {
    form.hidAction.value = "SM";
    form.hidActionDetail.value = "ViewStaffList";
    form.action = "DashboardServlet";
    if (form.hidSortBy.value == type) {
        if (form.hidDirection.value > 0) {
            form.hidDirection.value = 0;
        }
        else {
            form.hidDirection.value = 1;
        }
    }
    else {
        form.hidDirection.value = 1;
    }
    form.hidSortBy.value = type;
    //form.numPage.value = 0;
    form.submit();
}

function doAdd() {
    // window.open('/rms2');
    form.hidAction.value = "SM";
    form.hidActionDetail.value = "AddStaff";
    form.action = "DashboardServlet";
    form.submit();
}

function doDelete() {
	//window.open('/rms2');
	if (checkValid()) {
        if (confirm("Do you want to delete selected records, continue?")) {
            form.hidAction.value = "SM";
            form.hidActionDetail.value = "DeleteStaff";
            form.action = "DashboardServlet";
            form.submit();
        }
    }
}

function doUpdate(id) {
    form.hidStaffID.value = id;
    form.hidAction.value = "SM";
    form.hidActionDetail.value = "UpdateStaff";
    form.action = "DashboardServlet";
    form.submit();
}

function checkValid() {
    var flag = true;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == "dev_id" && e.type == "checkbox") {
            if (e.checked == 1) {
                flag = false;
            }
        }
    }
    if (flag) {
        alert("Please select developers to do this action!");
        return false;
    }
    return true;
}
</SCRIPT>
</BODY>
</HTML>
