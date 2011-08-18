<%@ page language="java" import="javax.servlet.*, fpt.ncms.bean.*, fpt.ncms.constant.NCMS,
            fpt.ncms.util.StringUtil.*"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCReportPivotBean beanNCReportPivot =
            (NCReportPivotBean)session.getAttribute("beanNCReportPivot");
    int i = 0;
    int nFields = beanNCReportPivot.getNumReportField();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
<TITLE>NC Distribution Report</TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<%@ include file="Header.jsp"%>
<TABLE class="menu" cellpadding="0" cellspacing="0" width="100%" height="20pt">
    <TR>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doNCListSearch()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;List&nbsp;&nbsp;</P></TD>
        <TD align="right"><P class="menuitem" style="cursor:hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">Logout&nbsp;&nbsp;</P></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="44%" valign="top" align="left">
        <P><IMG src="images/Headers/ncReport.gif"></P>
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
</TABLE>
<FORM method="POST" name="frmNCReport">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<INPUT type="hidden" name="hidID" value="">
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%" leftmargin="10">
    <COLGROUP>
        <COL width="15%">
        <COL width="30%">
        <COL width="12%">
        <COL width="20%">
        <COL width="20%">
    <TR>
        <TD>&nbsp;From Date</TD>
        <TD><INPUT type="text" size="15" name="txtFromDateP" maxlength="11" tabindex="1" value="<%=beanNCReportPivot.getFromDate()%>">&nbsp;(dd-MMM-yy)</TD>
        <TD>&nbsp;Report level</TD>
        <TD><SELECT name="optReportByP" tabindex="3" class="SmallCombo"><%
    for (int nRow = 0; nRow < beanNCReportPivot.getComboGroupBy().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCReportPivot.getComboGroupBy().getCell(nRow, 0)
                + (beanNCReportPivot.getComboGroupBy().getCell(nRow, 0).equals(beanNCReportPivot.getGroupBy()) ? "\" selected>" : "\">")
                + beanNCReportPivot.getComboGroupBy().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD>&nbsp;&nbsp;<IMG border="0" onclick="doNCReportPivot()" style="cursor: hand" onmouseout="this.src='images/Buttons/b_view_n.gif'" onmouseover="this.src='images/Buttons/b_view_p.gif'" src="images/Buttons/b_view_n.gif" name="imgView">
    </TR>
    <TR>
        <TD>&nbsp;To Date</TD>
        <TD><INPUT type="text" size="15" name="txtToDateP" maxlength="11" tabindex="2" value="<%=beanNCReportPivot.getToDate()%>">&nbsp;(dd-MMM-yy)</TD>
        <TD>&nbsp;Distributed by</TD>
        <TD><SELECT name="optReportTypeP" tabindex="4" class="SmallCombo"><%
    for (int nRow = 0; nRow < beanNCReportPivot.getComboReportType().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCReportPivot.getComboReportType().getCell(nRow, 0)
                + (beanNCReportPivot.getComboReportType().getCell(nRow, 0).equals(beanNCReportPivot.getReportType()) ? "\" selected>" : "\">")
                + beanNCReportPivot.getComboReportType().getCell(nRow, 0) + "</OPTION>");
    }
%>
        </SELECT>
        </TD>
        <TD>&nbsp;
        	<INPUT type="checkbox" name="chkPivotNC" <%=beanNCReportPivot.isCheckedNC() ? "checked" : ""%> title="Include NCs" tabindex="5">&nbsp;NC
            <INPUT type="checkbox" name="chkPivotOB" <%=beanNCReportPivot.isCheckedOB() ? "checked" : ""%> title="Include OBs" tabindex="5">&nbsp;OB
            <INPUT type="checkbox" name="chkPivotCC" <%=beanNCReportPivot.isCheckedCC() ? "checked" : ""%> title="Include CCs" tabindex="5">&nbsp;CC
            <INPUT type="checkbox" name="chkPivotPB" <%=beanNCReportPivot.isCheckedPB() ? "checked" : ""%> title="Include PBs" tabindex="5">&nbsp;PB
        </TD>
    </TR>
</TABLE>
<TABLE bgcolor="#9292CB" border="0" cellpadding="0" cellspacing="1" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber2">
    <TR class="Header" align="center"><%
    for (i = 0; i < nFields; i++) {
%>
        <TD width="<%=Math.round(100 / nFields)%>%"><P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReportPivot.getReport().getCell(0, i)%></P></TD><%
    }
%>
    </TR><%
    for (i = 1; i < beanNCReportPivot.getReport().getNumberOfRows(); i++) {
%>
    <TR class="row<%=(i % 2) + 1%>"><%
        for (int j = 0; j < nFields; j++) {
%>
        <TD><P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReportPivot.getReport().getCell(i, j)%></TD><%
        }
%>
    </TR><%
    }
%>
</TABLE>
</FORM>
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
    <TR>
        <TD width="70%">&nbsp;
        <IMG border="0" onclick="doNCReport()" onmouseout="this.src='images/Buttons/b_change_n.gif'" onmouseover="this.src='images/Buttons/b_change_p.gif'" style="cursor: hand" title="Change kind of report" src="images/Buttons/b_change_n.gif" name="imgChange">
        <IMG border="0" onclick="doPrintReport()" style="cursor: hand" onmouseout="this.src='images/Buttons/b_print_n.gif'" onmouseover="this.src='images/Buttons/b_print_p.gif'" src="images/Buttons/b_print_n.gif" name="imgPrint">
	</TR>
</TABLE>
</BODY>
</HTML>
<SCRIPT language="javascript">
function doLogOut() {
    frmNCReport.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmNCReport.action = "NcmsServlet";
    frmNCReport.submit();
}

function doNCReportPivot() {
    if (formValidate()) {
        frmNCReport.hidAction.value = "<%=NCMS.NC_REPORT_PIVOT%>";
        frmNCReport.action = "NcmsServlet";
        frmNCReport.submit();
    }
}

function doNCReport() {
    frmNCReport.hidAction.value = "<%=NCMS.NC_REPORT%>";
    frmNCReport.action = "NcmsServlet";
    frmNCReport.submit();
}

function doNCListSearch() {
    frmNCReport.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmNCReport.action = "NcmsServlet";
    frmNCReport.submit();
}

function doPrintReport() {
    bV = parseInt(navigator.appVersion);
    if (bV >= 4) {
        window.print();
    }
}

function formValidate() {
    if (frmNCReport.txtFromDateP != "") {
        if (!validDates(document. frmNCReport, new Array('txtFromDateP'), true, "From date is incorrect")) {
            return false;
        }
    }
    if (frmNCReport.txtToDateP != "") {
        if (!validDates(document. frmNCReport, new Array('txtToDateP'), true, "To date is incorrect")) {
            return false;
        }
        else {
            strStartDate = convertToSimpleDate(frmNCReport.txtFromDateP.value);
            strEndDate = convertToSimpleDate(frmNCReport.txtToDateP.value);
            if (!compareDate(strStartDate, strEndDate)) {
                alert("To date must be greater than From date!");
                frmNCReport.txtToDateP.focus();
                return false;
            }
        }
    }
    if ((!frmNCReport.chkPivotNC.checked) &&
        (!frmNCReport.chkPivotOB.checked) &&
        (!frmNCReport.chkPivotCC.checked) &&
        (!frmNCReport.chkPivotPB.checked))
    {
        alert("Please choose at least NC, OB , CC or PB to show report");
        frmNCReport.chkPivotNC.focus();
        return false;
    }
    return true;
}
</SCRIPT>