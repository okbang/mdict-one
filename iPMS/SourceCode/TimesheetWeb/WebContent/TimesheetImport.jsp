<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, fpt.timesheet.bean.*,
            fpt.timesheet.bean.Report.InquiryReportBean,
            fpt.timesheet.framework.util.CommonUtil.*,
            fpt.timesheet.framework.util.StringUtil.StringMatrix,
            java.util.Collection, java.util.Iterator" 
%>
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    InquiryReportBean beanInquiryReport = (InquiryReportBean)request.getAttribute("beanInquiryReport");
%>
<HTML>
<HEAD>
<TITLE>Timesheet Import</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<STYLE type="text/css">
A:link {
    COLOR: #ffffff;
    FONT-WEIGHT: normal;
    TEXT-DECORATION: none;
    font-size: 11px;
    font-family: tahoma, sans-serif;
    FONT-WEIGHT: bold;
}

A:visited {
    COLOR: #ffffff;
    FONT-WEIGHT: normal;
    TEXT-DECORATION: none;
    font-size: 11px;
    font-family: tahoma, sans-serif;
    FONT-WEIGHT: bold;
}

A:hover {
    COLOR: #ff0000;
    FONT-WEIGHT: normal;
    TEXT-DECORATION: none;
    font-size: 11px;
    font-family: tahoma, sans-serif;
    FONT-WEIGHT: bold;
}

INPUT.flatTextbox {
    BACKGROUND-COLOR: #d6e7ef;
    COLOR: #000066;
    FONT-FAMILY: Arial;
    FONT-SIZE: 11px;
    WIDTH: 30pt;
    BORDER-TOP: #104a7b 1px solid;
    BORDER-LEFT: #104a7b 1px solid;
    BORDER-BOTTOM: #afc4d5 1px solid;
    BORDER-RIGHT: #afc4d5 1px solid;
    HEIGHT: 20px
}
</STYLE>
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
</HEAD>
<BODY bgcolor="#336699" leftmargin="0" topmargin="0" style="FONT-FAMILY: tahoma, sans-serif; FONT-SIZE: 11px" onkeypress='javascript:setKeypress(event.which)'>
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H1><FONT color="yellow" size="3">Import Timesheet By Quality Assurance</FONT></H1>
<FORM method="post" action="TimesheetServlet" name="frmTimesheetList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="hidTypeOfView" value="InquiryReport">
<INPUT type="hidden" name="TimesheetID">
<INPUT type="hidden" name="hidCurrentPage" value='<%=beanInquiryReport.getCurrentPage()%>'>
<INPUT type="hidden" name="hidTotalPage" value='<%=beanInquiryReport.getTotalPage()%>'>
<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName()%></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR>
<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" align="center">
    <TR>
        <TD width="12%" height="31"><STRONG><FONT color="#ffffff" class="label1">Import For&nbsp;*</FONT></STRONG></TD>
        <TD width="26%" height="31">
            <INPUT name="userAccount" class="text" value="" onkeypress="toUpperCase()">
        </TD>
        <TD width="12%" height="31"><STRONG><FONT color="#ffffff" class="label1">File Name&nbsp;*</FONT></STRONG></TD>
        <TD width="32%" height="31">
            <INPUT type="file" name="importFile"></TD>
        <TD width="16%" height="31">
            <INPUT type="button" name="Import" onclick="checkName()" value=" Import " class="Button">
            <INPUT type="hidden" name="filename">
        </TD>
    </TR>
    <TR>
        <TD width="12%" height="12"></TD>
        <TD width="26%" height="12">
        <%if (request.getAttribute("uploadMessage") != null) { %>
            <%=request.getAttribute("uploadMessage")%>
        <%}%>
        </TD>
        <TD width="12%" height="12"></TD>
        <TD width="32%" height="12"><FONT color="#ffffff" class="label1">(MS Excel, maximum 256K)</FONT></TD>
        <TD width="16%" height="12"></TD>
    </TR>
    <TR>
        <TD align="left"></TD>
        <TD align="left"></TD>
        <TD align="left"></TD>
        <TD align="left"><A href="Template_Timesheet log.xls"><FONT color="yellow" class="label1">Download Template File</FONT></A></TD>
    </TR>
</TABLE>
<HR noshade size="1"><%
    // Common variable
    int i = 0;
    int maxrows = 0;
    int tmp = 0;
    String tmpStr = "";
    String gItemValue = "";
    String gItemDisplay = "";

    StringMatrix mtxTimesheet = null;
    maxrows = 0;
    if (beanInquiryReport.getTimesheetList() != null) {
        mtxTimesheet = beanInquiryReport.getTimesheetList();
        maxrows = mtxTimesheet.getNumberOfRows();
    }
%>
<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" bgcolor="#336699">
    <TR><%
    if (beanInquiryReport.getTotalTimesheet() > 0) {
%>
        <TD height="10" valign="bottom"><%
        int MAX = 50;
        int nPage = beanInquiryReport.getCurrentPage();
        if (maxrows > 0) {
%>
        <FONT color="#ffffff" class="label1">Result&nbsp;</FONT>
        <FONT color="yellow" size="-1"><%=nPage * MAX + 1%> - <%=nPage * MAX + maxrows%></FONT>
        <FONT color="#ffffff" class="label1"> of </FONT>
        <FONT color="yellow" size="-1"><%=beanInquiryReport.getTotalTimesheet()%></FONT>
        <FONT color="#ffffff" class="label1"> records in </FONT>
        <FONT color="yellow" size="-1"><%=beanInquiryReport.getTotalPage()%></FONT>
        <FONT color="#ffffff" class="label1"> page(s)</FONT><%
        }
        else {
%>
        <B><FONT color="#ffffff" class="label1">Result&nbsp;</FONT>
        <FONT color="#ffffff" class="label1">0 - 0 </FONT>
        <FONT color="#ffffff" class="label1"> of </FONT>
        <FONT color="yellow" size="-1">0</FONT>
        <FONT color="#ffffff" class="label1"> records in </FONT>
        <FONT color="yellow" size="-1">0</FONT>
        <FONT color="#ffffff" class="label1"> page(s)</FONT></B>
<%
        }
%>
        </TD>
        <TD align="right" height="10" valign="top"><%
        if (beanInquiryReport.getTotalTimesheet() > 50) {
            if (beanInquiryReport.getCurrentPage() > 0) {
%>
        <A class="HeaderMenu" href="javascript:doViewTimesheet('Prev')">Prev</A>&nbsp;&nbsp;&nbsp;<%
            }
            if (beanInquiryReport.getCurrentPage() + 1 < beanInquiryReport.getTotalPage()) {
%>
        <A class="HeaderMenu" href="javascript:doViewTimesheet('Next')">Next</A>&nbsp;&nbsp;&nbsp;<%
            }
%>
        <INPUT type="text" onkeypress="javascript:numberAllowed()" size="4" name="txtPage" maxlength="10" value='<%=beanInquiryReport.getCurrentPage() +1%>' class="flatTextbox">
        <INPUT type="button" name="GoPage" class="Button" onclick='javascript:doGoPage()' value="Go"></TD><%
        }
    }
    else {
%>
        <TD width="20%" height="10" valign="bottom"><FONT color="#ffffff" class="label1">Total:&nbsp;</FONT><FONT color="yellow" size="-1">0</FONT></TD><%
    }
%>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="3" cellspacing="0" bgcolor="#336699" width="100%">
    <COLGROUP>
        <COL width="10%">
        <COL width="10%">
        <COL width="8%">
        <COL width="30%">
        <COL width="3%">
        <COL width="12%">
        <COL width="12%">
        <COL width="12%">
        <COL width="3%">
        <COL width="10%">
    <TR>
        <TD class="TableHeader1">Project</TD>
        <TD class="TableHeader1">Account</TD>
        <TD class="TableHeader1">Date</TD>
        <TD class="TableHeader1">Description</TD>
        <TD class="TableHeader1">Time</TD>
        <TD class="TableHeader1">Process</TD>
        <TD class="TableHeader1">Work</TD>
        <TD class="TableHeader1">Product</TD>
        <TD class="TableHeader1">KPA</TD>
        <TD class="TableHeader1">Approver</TD>
    </TR><%
    if (mtxTimesheet != null) {
        i = 0;
        while (i < maxrows ) {
            String sProject = mtxTimesheet.getCell(i, 0);
            String sAccount = mtxTimesheet.getCell(i, 1);
            String sDate = mtxTimesheet.getCell(i, 2);
            String sDescription = mtxTimesheet.getCell(i, 3);
            String sDuration = mtxTimesheet.getCell(i, 4);
            String sProcess = mtxTimesheet.getCell(i, 5);
            String sType = mtxTimesheet.getCell(i, 6);
            String sProduct = mtxTimesheet.getCell(i, 7);
            String sKpa = mtxTimesheet.getCell(i, 8);
            String sApprover = mtxTimesheet.getCell(i, 9);
            String strClass = ((i % 2) == 1) ? "Row2" : "Row1";
            String sTimesheetID = mtxTimesheet.getCell(i, 10);
            String sProjectType = mtxTimesheet.getCell(i, 11);
            String sProjectStatus = mtxTimesheet.getCell(i, 12);
%>
    <TR><%
            if ("0".equals(sProjectStatus) || "3".equals(sProjectStatus)) {
%>
        <TD class="<%=strClass%>"><%=sProject%></TD><%
            }
            else {
%>
        <TD class="<%=strClass%>" height="25"><%=sProject%></TD><%
            }
%>
        <TD class="<%=strClass%>"><%=sAccount%></TD>
        <TD class="<%=strClass%>" align="center"><%=sDate%></TD>
        <TD class="<%=strClass%>"><%=CommonUtil.correctHTMLError(sDescription)%></TD>
        <TD class="<%=strClass%>" align="center"><%=sDuration%></TD>
        <TD class="<%=strClass%>"><%=sProcess%></TD>
        <TD class="<%=strClass%>"><%=sType%></TD>
        <TD class="<%=strClass%>"><%=sProduct%></TD>
        <TD class="<%=strClass%>">&nbsp;<%=sKpa%></TD>
        <TD class="<%=strClass%>">&nbsp;<%=sApprover%></TD>
    </TR><%
            i++;
        }
    }
%>
</TABLE>
</DIV>
</FORM>
</BODY>
<SCRIPT language="javascript">
function doImport(ext) {
    if (ext != '') {
        document.forms[0].hidAction.value = "RA";
        document.forms[0].hidActionDetail.value = "ImportTimesheet";
        document.forms[0].action = "TimesheetServlet";
        document.forms[0].encoding = "multipart/form-data";
        document.forms[0].submit();
    }
}

function checkName() {
    var name = document.forms[0].importFile.value;
    
    var strAccount = document.forms[0].userAccount.value;
    if (strAccount == "" || strAccount == null) {
        alert("Select user account for import!");
        document.forms[0].userAccount.focus();
        return;
    }
    
    if (name == "" || name == null) {
        alert("Select MS Excel file!");
        document.forms[0].importFile.focus();
        return;
    }
    else {
        var ext = name.substring(name.lastIndexOf(".") + 1, name.length);
        ext = ext.toLowerCase();
        if (ext != "xls") {
            alert("Select MS Excel file!");
            document.forms[0].importFile.focus();
            return;
        }
        else {
            doImport(ext);
        }
    }
}

function setKeypress(key) {
    if (navigator.appName != "Netscape") {
        key = event.keyCode;
    }
    if (key==13) {
        checkName();
    }
}

document.forms[0].userAccount.focus();

</SCRIPT>
</HTML>