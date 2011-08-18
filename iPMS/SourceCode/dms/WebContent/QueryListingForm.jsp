<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
      fpt.dms.bean.DefectManagement.*,fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    QueryListingBean beanQueryListing
            = (QueryListingBean)request.getAttribute("beanQueryListing");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
%>
<HTML>
<HEAD>
<TITLE>View Defect Listing</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">
function doAllDefects() {
    var form = document.frmViewDefectListing;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.hidTypeOfView.value = "ViewAllDefects";
    form.action = "DMSServlet";
    form.submit();
}

function doAllOpenDefects() {
    var form = document.frmViewDefectListing;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.hidTypeOfView.value = "ViewAllOpenDefects";
    form.action = "DMSServlet";
    form.submit();
}
function doAllLeakageDefects() {
    var form = document.frmViewDefectListing;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.hidTypeOfView.value = "ViewAllLeakageDefects";
    form.action = "DMSServlet";
    form.submit();
}

function doAddQuery() {
    var form = document.frmViewDefectListing;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "AddQuery";
    form.action = "DMSServlet";
    form.submit();
}

function doDeleteQuery() {
    var form = document.frmViewDefectListing;
    if (checkValid1() || checkValid2()) {
        if (confirm("Do you want to delete selected records, continue?")) {
            form.hidAction.value = "DM";
            form.hidActionDetail.value = "DeleteQuery";
            form.action = "DMSServlet";
            form.submit();
        }
    }
    else {
        alert("Please select queries to do delete!");
    }
}

function doQueryDefectList(id) {
    var form = document.frmViewDefectListing;
    form.hidTypeOfView.value = id;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "UserQueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function checkValid1() {
    var flag = false;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == "selectIndex1" && e.type == "checkbox") {
            if (e.checked == 1) {
                flag = true;
            }
        }
    }
    return flag;
}

function checkValid2() {
    var flag = false;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == "selectIndex2" && e.type == "checkbox") {
            if (e.checked == 1) {
                flag = true;
            }
        }
    }
    return flag;
}

function CheckAll1(form) {
    for (var i = 0; i < form.elements.length; i++) {
        var e = form.elements[i];
        if (e.name == "selectIndex1") {
            e.checked = form.allbox1.checked;
        }
    }
}

function CheckAll2(form) {
    for (var i = 0; i < form.elements.length; i++) {
        var e = form.elements[i];
        if (e.name == "selectIndex2" && e.type == "checkbox") {
            e.checked = form.allbox2.checked;
        }
    }
}

</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<DIV>
<P><IMG border="0" src="Images/ViewDefectListing.gif" width="411" height="28"></P>
</DIV>
<FORM method="POST" action="DMSServlet" name="frmViewDefectListing">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="hidTypeOfView" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<P></P>
<TABLE border="0" width="100%" class="TblOut2">
    <TR>
        <TD width="8%"><B>User:</B></TD>
        <TD width="24%"><%=beanUserInfo.getUserName()%></TD>
        <TD width="12%"><B>Login Date:</B></TD>
        <TD width="25%"><%=beanUserInfo.getDateLogin()%></TD>
        <TD width="9%"><B>Project</B></TD>
        <TD width="22%" align="right"><SELECT name="cboProjectList" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="QueryListing"%>','<%=""%>');"><%
	int nCurrentProjectID = beanUserInfo.getProjectID();
    for (int i = 0; i < beanComboProject.getListing().getNumberOfRows(); i++) {
		int nProjectID = Integer.parseInt(beanComboProject.getListing().getCell(i, 0));
        out.write("<OPTION ");
        out.write(nProjectID == nCurrentProjectID ? " selected " : " ");
        out.write("value=\"" + nProjectID + "\">" + beanComboProject.getListing().getCell(i, 1) + "</OPTION>");
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
//	String strCurrentStatus = beanComboProject.getCurrentStatus();
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
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" class="TblOut">
    <TR>
        <TD>
        <TABLE border="0" cellspacing="1" cellpadding="0" width="100%" bgcolor="#000000">
            <TR class="Row0">
                <TD width="3%"></TD>
                <TD width="59%" align="center" height="19" colspan="1"><B>Fixed Queries</B></TD>
            </TR>
            <TR class="Row2">
                <TD width="3%"></TD>
                <TD width="59%" height="19"><A href="javascript:doAllDefects()">All Defects</A></TD>
            </TR>
            <TR class="Row1">
                <TD width="3%"></TD>
                <TD width="59%" height="19"><A href="javascript:doAllOpenDefects()">All Open Defects</A></TD>
            </TR>
            <TR class="Row2">
                <TD width="3%"></TD>
                <TD width="59%" height="19"><A href="javascript:doAllLeakageDefects()">All Leakage Defects</A></TD>
            </TR>
        </TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" class="TblOut">
    <TR>
        <TD>
        <TABLE border="0" cellspacing="1" cellpadding="1" width="100%" bgcolor="#000000">
            <TR class="Row0">
                <TD width="3%" align="center" height="19"><INPUT type="checkbox" name="allbox1" value="CheckAll" onclick="JavaScript: CheckAll1(document.forms[0])" <%=("1".equals(beanUserInfo.getPosition().substring(2,3)))? "":"disabled"%>></TD>
                <TD width="59%" align="center" height="19"><B>Public Queries</B></TD>
            </TR><%
    for (int i = 0; i < beanQueryListing.getQueryList().getNumberOfRows(); i++) {
        if (beanQueryListing.getQueryList().getCell(i, 2).equals("0")) {
            if ((i % 2) == 0) {
%>
            <TR class="Row2">
                <TD width="3%" height="19" align="center"><INPUT type="checkbox" name="selectIndex1" value="<%=beanQueryListing.getQueryList().getCell(i, 0)%>" <%=("1".equals(beanUserInfo.getPosition().substring(2, 3))) ? "" : "disabled"%>></TD>
                <TD width="59%" height="19"><A href="javascript:doQueryDefectList(<%=beanQueryListing.getQueryList().getCell(i, 0)%>)"> <%=beanQueryListing.getQueryList().getCell(i, 1)%> </A></TD>
            </TR><%
            }
            else {
%>
            <TR class="Row1">
                <TD width="3%" height="19" align="center"><INPUT type="checkbox" name="selectIndex1" value="<%=beanQueryListing.getQueryList().getCell(i, 0)%>" <%=("1".equals(beanUserInfo.getPosition().substring(2, 3))) ?  "" : "disabled"%>></TD>
                <TD width="59%" height="19"><A href="javascript:doQueryDefectList(<%=beanQueryListing.getQueryList().getCell(i, 0)%>)"> <%=beanQueryListing.getQueryList().getCell(i, 1)%> </A></TD>
            </TR><%
            }
        }
    }
%>
        </TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" class="TblOut">
    <TR>
        <TD>
        <TABLE border="0" cellspacing="1" cellpadding="1" width="100%" bgcolor="#000000">
            <TR class="Row0">
                <TD width="3%" align="center" height="19"><INPUT type="checkbox" name="allbox2" value="CheckAll" onclick="JavaScript: CheckAll2(document.forms[0])"></TD>
                <TD width="59%" align="center" height="19"><B>Private Queries</B></TD>
            </TR><%
    for (int i = 0; i < beanQueryListing.getQueryList().getNumberOfRows(); i++) {
        if (beanQueryListing.getQueryList().getCell(i, 2).equals("1")) {
            if ((i % 2) == 0) {
%>
            <TR class="Row2">
                <TD width="3%" align="center" height="19"><INPUT type="checkbox" name="selectIndex2" value="<%=beanQueryListing.getQueryList().getCell(i, 0)%>"></TD>
                <TD width="59%" height="19"><A href="javascript:doQueryDefectList(<%=beanQueryListing.getQueryList().getCell(i, 0)%>)"> <%=beanQueryListing.getQueryList().getCell(i, 1)%> </A></TD>
            </TR><%
            }
            else {
%>
            <TR class="Row1">
                <TD width="3%" align="center" height="19"><INPUT type="checkbox" name="selectIndex2" value="<%=beanQueryListing.getQueryList().getCell(i, 0)%>"></TD>
                <TD width="59%" height="19"><A href="javascript:doQueryDefectList(<%=beanQueryListing.getQueryList().getCell(i, 0)%>)"> <%=beanQueryListing.getQueryList().getCell(i, 1)%> </A></TD>
            </TR><%
            }
        }
    }
%>
        </TABLE>
        </TD>
    </TR>
</TABLE>
<P><INPUT type="button" name="btnAddQuery" class="Button" onclick="javascript:doAddQuery()" value="Add Query"> &nbsp;
<INPUT type="button" name="btnDeleteQuery" class="Button" onclick="javascript:doDeleteQuery()" value="Delete Query"> &nbsp;

</FORM>
</BODY>
</HTML>