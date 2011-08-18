<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp" %><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<TITLE>Setup Environment</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT language="javascript">
function doDefectType() {
    var form = document.frmSetup;
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "DefectTypeList";
    form.action = "DMSServlet";
    form.submit();
}
/*
function doGroup() {
    var form = document.frmSetup;
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "GroupList";
    form.action = "DMSServlet";
    form.submit();
}
*/
function doKPA() {
    var form = document.frmSetup;
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "KPAList";
    form.action = "DMSServlet";
    form.submit();
}

function doPriority() {
    var form = document.frmSetup;
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "PriorityList";
    form.action = "DMSServlet";
    form.submit();
}

function doProcess() {
    var form = document.frmSetup;
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "ProcessList";
    form.action = "DMSServlet";
    form.submit();
}

function doProjectStage() {
    var form = document.frmSetup;
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "ProjectStageList";
    form.action = "DMSServlet";
    form.submit();
}

function doQCActivity() {
    var form = document.frmSetup;
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "QCActivityList";
    form.action = "DMSServlet";
    form.submit();
}

function doSeverity() {
    var form = document.frmSetup;
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "SeverityList";
    form.action = "DMSServlet";
    form.submit();
}

function doStatus() {
    var form = document.frmSetup;
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "StatusList";
    form.action = "DMSServlet";
    form.submit();
}

function doTypeOfWork() {
    var form = document.frmSetup;
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "TypeOfWorkList";
    form.action = "DMSServlet";
    form.submit();
}

function doWorkProduct() {
    var form = document.frmSetup;
    form.hidAction.value = "SE";
    form.hidActionDetail.value = "WorkProductList";
    form.action = "DMSServlet";
    form.submit();
}
function doBack() {
    var form = document.frmSetup;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" topmargin="0" leftmargin="0">
<TABLE bgcolor="#000000" border="0" cellpadding="0" cellspacing="0" height="51" width="100%">
    <TBODY>
        <TR>
            <TD bgcolor="#310C52" width="212" height="51" background="Images/bgr_header.gif"><IMG border="0" src="Images/defect_logop1.gif"><BR>
            </TD>
            <TD bgcolor="#310C52" height="51" width="50%" background="Images/bgr_header.gif" align="left" valign="top"><IMG border="0" src="Images/defect_logop2.gif"></TD>
            <TD bgcolor="#310C52" height="51" width="50%" background="Images/bgr_header.gif" align="right" valign="top"><IMG border="0" src="Images/header.gif"></TD>
        </TR>
        <TR>
            <TD bgcolor="#000084" align="left" width="111"><IMG border="0" src="Images/logo2.gif"></TD>
            <TD bgcolor="#310C52" valign="middle" align="left" colspan="2"></TD>
            <I></I>
        </TR>
    </TBODY>
</TABLE>
<FORM method="post" action="DMSServlet" name="frmSetup">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0">
    <TR>
        <TD width="1%"></TD>
        <TD width="98%">
        <DIV>
        <P><BR>
        <IMG border="0" src="Images/title_dms.gif" width="411" height="28"><BR>
        <BR>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<TABLE border="0" width="100%">
		    <TR>
        		<TD align="right"><A href="javascript:doBack()">Back to DefectListing</A></TD>
		    </TR>
		</TABLE>
        <TABLE border="0" width="100%" bgcolor="#000000" cellspacing="1" cellpadding="0">
            <TR>
                <TD width="100%" bgcolor="#F4F5CB" height="19">
                <P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<A href="javascript:doProjectStage()"><FONT face="Arial" size="2" color="#000084">ProjectStage</FONT></A>
                <FONT face="Arial" size="2" color="#000084">&nbsp;&nbsp;I&nbsp;&nbsp;</FONT><A href="javascript:doDefectType()">
                <FONT face="Arial" size="2" color="#000084">DefectType</FONT></A>
                <FONT face="Arial" size="2" color="#000084">&nbsp;&nbsp;I&nbsp;&nbsp;</FONT><A href="javascript:doSeverity()">
                <FONT face="Arial" size="2" color="#000084">Severity</FONT></A>
                <FONT face="Arial" size="2" color="#000084">&nbsp;&nbsp;I&nbsp;&nbsp;&nbsp;</FONT><A href="javascript:doPriority()">
                <FONT face="Arial" size="2" color="#000084">Priority</FONT></A>
                <FONT face="Arial" size="2" color="#000084">&nbsp;&nbsp;I&nbsp;&nbsp;</FONT><A href="javascript:doQCActivity()">
                <FONT face="Arial" size="2" color="#000084">QCActivity</FONT></A>
                <FONT face="Arial" size="2" color="#000084">&nbsp;I&nbsp;&nbsp;</FONT><A href="javascript:doStatus()">
                <FONT face="Arial" size="2" color="#000084">Status</FONT></A>
                <FONT face="Arial" size="2" color="#000084">&nbsp;&nbsp;I&nbsp;&nbsp;&nbsp;</FONT><A href="javascript:doWorkProduct()">
                <FONT face="Arial" size="2" color="#000084">WorkProduct</FONT></A>
                <FONT face="Arial" size="2" color="#000084">&nbsp;&nbsp;I&nbsp;&nbsp;&nbsp;</FONT><A href="javascript:doProcess()">
                <FONT face="Arial" size="2" color="#000084">Process</FONT></A>
                <FONT face="Arial" size="2" color="#000084">&nbsp;&nbsp;I&nbsp;&nbsp;&nbsp;</FONT><A href="javascript:doKPA()">
                <FONT face="Arial" size="2" color="#000084">KPA</FONT></A>
                <FONT face="Arial" size="2" color="#000084">&nbsp;&nbsp;I&nbsp;&nbsp;&nbsp;</FONT><A href="javascript:doTypeOfWork()">
                <FONT face="Arial" size="2" color="#000084">TypeOfWork</FONT></A>
                <!--FONT face="Arial" size="2" color="#000084">&nbsp;&nbsp;I&nbsp;&nbsp;&nbsp;</FONT><A href="javascript:doGroup()">
                <FONT face="Arial" size="2" color="#000084">Group</FONT></A-->
                <FONT face="Arial" size="2" color="#000084">&nbsp;&nbsp;I&nbsp;&nbsp;</FONT><A href="javascript:doLogout(document.forms[0])">
                <FONT face="Arial" size="2" color="#000084">Logout</FONT></A></P>
                </TD>
            </TR>
        </TABLE>
    </TR>
</TABLE>
</FORM>
</BODY>
</HTML>