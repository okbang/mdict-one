<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java"
    import="
        java.util.*,
        javax.servlet.*,
        fpt.dashboard.constant.*,
        fpt.dashboard.bean.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp" %><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Dashboard Homepage</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<SCRIPT language="Javascript">
function doProjectList() {
    var form = document.frmDBHomepage;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewProjectList";
    form.action = "DashboardServlet";
    form.submit();
}

function doDashboard() {
    var form = document.frmDBHomepage;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewDashboard";
    form.action = "DashboardServlet";
    form.submit();
}

<%
    // External viewers at project level OR project leaders not allow to access Staff and Resource
    if (Integer.toString(DATA.LOGIN_EXTERNAL_PL).equals(beanUserInfo.getRole()) ||
    	Integer.toString(DATA.LOGIN_EXTERNAL_GL).equals(beanUserInfo.getRole()) ||
    	(DATA.ROLE_PROJECTLEADER.equals(beanUserInfo.getSRole()))) {
%>
function doStaffList() {
    alert('You have not permission to access this page');
}

function doResource() {
    alert('You have not permission to access this page');
}
<%
    }
    else {
%>
function doStaffList() {
    var form = document.frmDBHomepage;
    form.hidAction.value = "SM";
    form.hidActionDetail.value = "ViewStaffList";
    form.action = "DashboardServlet";
    form.submit();
}

function doResource() {
    var form = document.frmDBHomepage;
    form.hidAction.value = "RM";
    form.hidActionDetail.value = "ViewResource";
    form.action = "DashboardServlet";
    form.submit();
}
<%
    }
    // External viewers at project level OR project leaders not allow to access Staff and Resource
    if (Integer.toString(DATA.LOGIN_EXTERNAL_PL).equals(beanUserInfo.getRole()) ||
    	Integer.toString(DATA.LOGIN_EXTERNAL_GL).equals(beanUserInfo.getRole())) {
%>
function doUtility() {
    alert('You have not permission to access this page');
}

<%
    }
    else {
%>
function doUtility() {
    var form = document.frmDBHomepage;
    if (form.hidSubMenu.value == "OPEN") {
        form.hidSubMenu.value = "CLOSE";
    }
    else {
        form.hidSubMenu.value = "OPEN";
    }
    form.action = "Dashboard.jsp"
    form.submit();
}
<%
    }
%>
function doOtherAssignment() {
    var form = document.frmDBHomepage;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ListOtherAssignment";
    form.action = "DashboardServlet";
    form.submit();
}

function doViewMilestone() {
    var form = document.frmDBHomepage;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ListAllMilestone";
    form.action = "DashboardServlet";
    form.submit();
}
function doLogout() {
    var form = document.frmDBHomepage;

    // Clear history
    /*for (var i = 0; i < window.history.length; i++) {
        window.location.replace("");
        window.history.back();
    }*/

    form.hidAction.value = "DashboardLogin";
    form.action = "DashboardServlet";
    form.submit();
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#E3E3EB">
<FORM method="post" action="DashboardServlet" name="frmDBHomepage"><INPUT
    type="hidden" name="hidAction" value=""> <INPUT type="hidden"
    name="hidActionDetail" value=""> <INPUT type="hidden" name="hidSubMenu"
    value="<%=request.getParameter("hidSubMenu")%>">
<TABLE border="0" width="100%" cellpadding="0" cellspacing="0">
    <TR>
        <TD width="100%" height="41" background="images/header.gif"><IMG
            border="0" src="images/header.gif" width="46" height="52"></TD>
    </TR>
    <TR>
        <TD width="100%" height="325">
        <TABLE border="0" width="100%" cellpadding="0" cellspacing="0">
            <TR>
                <TD width="50%"><IMG border="0" src="images/logo.gif" width="442"
                    height="456"></TD>
                <TD width="50%">
                <TABLE border="0" width="100%" cellpadding="0" cellspacing="0"
                    height="176">
                    <TR>
                        <TD width="100%" height="50"><IMG border="0"
                            src="images/title.gif" width="322" height="51"></TD>
                    </TR>
                    <TR>
                        <TD width="100%" height="126">
                        <TABLE border="0" width="100%" cellpadding="0" cellspacing="0"
                            height="124">
                            <TR>
                                <TD width="12%" height="33"><A href="javascript:doProjectList()"><IMG
                                    border="0" src="images/bullet1.gif" width="16" height="16"
                                    align="right"></A></TD>
                                <TD width="88%" height="33"><A href="javascript:doProjectList()">PROJECT
                                LIST</A></TD>
                            </TR>
                            <TR>
                                <TD width="12%" height="33"><A href="javascript:doDashboard()"><IMG
                                    border="0" src="images/bullet5.gif" width="16" height="16"
                                    align="right"></A></TD>
                                <TD width="88%" height="33"><A href="javascript:doDashboard()">PROJECT
                                DASHBOARD</A></TD>
                            </TR>
                            <TR><%
    String strLink = "";
    if (beanUserInfo.getRole().equals("3") || beanUserInfo.getRole().equals("5")
            || (beanUserInfo.getSRole().equals("1100000000"))) {
        strLink ="javascript:alert('You have not permission to access this page')";
    }
    else {
        strLink = "javascript:doStaffList()";
    }
%>
                                <TD width="12%" height="33"><A href="<%=strLink%>"><IMG
                                    border="0" src="images/bullet3.gif" width="16" height="16"
                                    align="right"></A></TD>
                                <TD width="88%" height="33"><A href="<%=strLink%>">STAFF</A></TD>
                            </TR>
                            <TR><%
    String strResLink = "";
    if (beanUserInfo.getRole().equals("3") || beanUserInfo.getRole().equals("5")
            || (beanUserInfo.getSRole().equals("1100000000"))) {
        strResLink ="javascript:alert('You have not permission to access this page')";
    }
    else {
        strResLink = "javascript:doResource()";
    }
%>

                                <TD width="12%" height="33"><A href="<%=strResLink%>"><IMG
                                    border="0" src="images/bullet6.gif" width="16" height="16"
                                    align="right"></A></TD>
                                <TD width="88%" height="33"><A href="<%=strResLink%>">RESOURCE</A></TD>
                            </TR>
                            <!-- Khong can  Submit -->
                            <TR>
                                <TD width="12%" height="33"><A href="javascript:doUtility()"><IMG
                                    border="0" src="images/bullet7.gif" width="16" height="16"
                                    align="right"></A></TD>
                                <TD width="88%" height="33"><A href="javascript:doUtility()">UTILITY</A></TD>
                            </TR><%
    String strAction = request.getParameter("hidSubMenu");
    if ((strAction != null) && ("OPEN".equals(strAction))) {
%>
                            <TR>
                                <TD width="12%" height="33"></TD>
                                <TD width="88%" height="33">
                                <TABLE border="0" width="100%" cellspacing="0" cellpadding="0">
                                    <TR>
                                        <TD width="14%" height="10"><A
                                            href="javascript:doOtherAssignment()"><IMG border="0"
                                            src="images/bullet1.gif" align="right" width="16" height="16"></A></TD>
                                        <TD width="86%" height="10"><A
                                            href="javascript:doOtherAssignment()">OTHER ASSIGNMENT</A></TD>
                                    </TR>
                                    <TR>
                                        <TD width="14%" height="10"><A
                                            href="javascript:doViewMilestone()"><IMG border="0"
                                            src="images/bullet6.gif" align="right" width="16" height="16"></A></TD>
                                        <TD width="86%" height="10"><A
                                            href="javascript:doViewMilestone()">MILESTONE</A></TD>
                                    </TR>
                                </TABLE>
                                </TD>
                            </TR>
                            <%
    }
    else {
    }
%>
                            <TR>
                                <TD width="12%" height="33"><A href="javascript:doLogout()"><IMG
                                    border="0" src="images/bullet7.gif" width="16" height="16"
                                    align="right"></A></TD>
                                <TD width="88%" height="33"><A href="javascript:doLogout()">LOGOUT</A></TD>
                            </TR>
                        </TABLE>
                        </TD>
                    </TR>
                </TABLE>
                </TD>
            </TR>
        </TABLE>
        </TD>
    </TR>
    <TR>
        <TD width="100%" height="53" background="images/footer.gif"><CENTER><br><%@ include file="footer.jsp"%></CENTER></TD>
    </TR>
    
</TABLE>
</FORM>
<SCRIPT>
    //window.history.forward(1);
</SCRIPT>
</BODY>
</HTML>
