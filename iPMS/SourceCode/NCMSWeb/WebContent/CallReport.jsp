<%@ page language="java" import="javax.servlet.*, fpt.ncms.bean.*, fpt.ncms.constant.NCMS,
            fpt.ncms.util.StringUtil.*"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCReportBean beanNCReport = (NCReportBean)session.getAttribute("beanNCReport");
    int i = 0;
    // Display first field as Project of Group
    String strName = beanNCReport.getGroupBy().equals("GroupName") ? "Group" : "Project";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" type="text/css" href="inc/CallStylesheet.css">
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
<TITLE>Call Report</TITLE>
<SCRIPT language="javascript">
function doLogOut() {
    frmNCReport.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmNCReport.action = "NcmsServlet";
    frmNCReport.submit();
}

function doCallList() {
    frmNCReport.hidAction.value = "<%=NCMS.CALL_LOG_LIST%>";
    frmNCReport.action = "NcmsServlet";
    frmNCReport.submit();
}

function doNCReportPivot() {
    frmNCReport.hidAction.value = "<%=NCMS.NC_REPORT_PIVOT%>";
    frmNCReport.action = "NcmsServlet";
    frmNCReport.submit();
}

function doNCReport() {
    if (formValidate()) {
        frmNCReport.hidAction.value = "<%=NCMS.NC_REPORT%>";
        frmNCReport.action = "NcmsServlet";
        frmNCReport.submit();
    }
}

function doPrintReport() {
    bV = parseInt(navigator.appVersion);
    if (bV >= 4) {
        window.print();
    }
}

function formValidate() {
    if (frmNCReport.txtFromDate != "") {
        if (!validDates(document. frmNCReport, new Array('txtFromDate'), true, "From date is incorrect")) {
            return false;
        }
    }
    if (frmNCReport.txtToDate != "") {
        if (!validDates(document. frmNCReport, new Array('txtToDate'), true, "To date is incorrect")) {
            return false;
        }
        else {
            strStartDate = convertToSimpleDate(frmNCReport.txtFromDate.value);
            strEndDate = convertToSimpleDate(frmNCReport.txtToDate.value);
            if (!compareDate(strStartDate, strEndDate)) {
                alert("To date must be greater than From date!");
                frmNCReport.txtToDate.focus();
                return false;
            }
        }
    }
    return true;
}
</SCRIPT>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<%@ include file="HeaderCallLog.jsp"%>
<TABLE class="menu" cellpadding="0" cellspacing="0" width="99%" height="20pt">
    <TR>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doCallList()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;List&nbsp;&nbsp;</P></TD>
        <TD align="right"><P class="menuitem" style="cursor:hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">Logout&nbsp;&nbsp;</P></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="44%" valign="top" align="left">
        <P><FONT size="6">Report</FONT>
        </TD>
        <TD width="56%" valign="top">
        <DIV align="right">
        <TABLE border="0" cellspacing="0" cellpadding="0">
            <TR></TR>
            <TR>
                <TD>
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
</TABLE>
<FORM method="post" name="frmNCReport" action="NcmsServlet">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%" leftmargin="10">
    <COLGROUP>
        <COL width="10%">
        <COL width="20%">
        <COL width="10%">
        <COL width="20%">
        <COL width="10%">
        <COL width="15%">
        <COL width="20%">
    <TR>
        <TD>&nbsp;From date</TD>
        <TD><INPUT type="text" size="15" name="txtFromDate" maxlength="11" tabindex="1" value="<%=beanNCReport.getFromDate()%>">&nbsp;(dd-MMM-yy)</TD>
        <TD>&nbsp;Report level</TD>
        <TD><SELECT name="optReportBy" class="SmallCombo" tabindex="3"><%
    for (int nRow = 0; nRow < beanNCReport.getComboGroupBy().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCReport.getComboGroupBy().getCell(nRow, 0)
                + (beanNCReport.getComboGroupBy().getCell(nRow, 0).equals(beanNCReport.getGroupBy()) ? "\" selected>" : "\">")
                + beanNCReport.getComboGroupBy().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD></TD>
    </TR>
    <TR>
        <TD>&nbsp;To date</TD>
        <TD><INPUT type="text" size="15" name="txtToDate" maxlength="11" tabindex="2" value="<%=beanNCReport.getToDate()%>">&nbsp;(dd-MMM-yy)</TD>
        <TD>&nbsp;Request to</TD>
        <TD><SELECT name="optTypeOfCause" class="SmallCombo" tabindex="3"><%
    for (int nRow = 0; nRow < beanNCReport.getComboTypeOfCause().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCReport.getComboTypeOfCause().getCell(nRow, 0)
                + (beanNCReport.getComboTypeOfCause().getCell(nRow, 0).equals("" + beanNCReport.getTypeOfCause()) ? "\" selected>" : "\">")
                + beanNCReport.getComboTypeOfCause().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD><INPUT type="button" name="btnView" title="Show report" class="button" onclick="doNCReport()" value="Show"></TD>
    </TR>
</TABLE>
<BR>
<TABLE bgcolor="#9292CB" border="0" cellpadding="0" cellspacing="1" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber2">
    <COLGROUP>
        <COL width="16%">
        <COL width="12%">
        <COL width="12%">
        <COL width="12%">
        <COL width="12%">
        <COL width="12%">
        <COL width="12%">
        <COL width="12%">
    <TR class="Header" align="center">
        <TD>&nbsp;<%=strName%></TD>
        <TD>&nbsp;Total</TD>
        <TD>&nbsp;Delaying</TD>
        <TD>&nbsp;Closed</TD>
        <TD>&nbsp;In time</TD>
        <TD>&nbsp;Overtime</TD>
        <TD>&nbsp;Response time (hours)</TD>
        <TD>Average fix time (hours)</TD>
    </TR><%
    for (i = 0; i < beanNCReport.getReport().getNumberOfRows(); i++) {
%>
    <TR class="row<%= (i % 2) + 1%>" align="center"
    <%=((i + 1) == beanNCReport.getReport().getNumberOfRows()) ? " style=\"FONT-WEIGHT: bold\"" : ""%>>
        <TD align="left">
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReport.getReport().getCell(i, 0)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReport.getReport().getCell(i, 1)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReport.getReport().getCell(i, 3)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReport.getReport().getCell(i, 5)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReport.getReport().getCell(i, 2)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReport.getReport().getCell(i, 6)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReport.getReport().getCell(i, 8)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReport.getReport().getCell(i, 7)%></P>
        </TD>
    </TR>
    <%
    }
    if (i == 0) {%>
    <TR><TD colspan="8" class="row1">No record found!</TD></TR><%
    }
%>
</TABLE>
</FORM>
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
    <TR>
        <TD width="70%">&nbsp;
        <INPUT type="button" name="btnSwitch" title="Switch to Distribution report" class="button" onclick="doNCReportPivot()" value="Switch">
        <INPUT type="button" name="btnPrint" title="Print report" class="button" onclick="doPrintReport()" value=" Print ">
        </TD>
    </TR>
</TABLE>
</BODY>
</HTML>