<%@ page language="java" import="javax.servlet.*, fpt.ncms.bean.*, fpt.ncms.constant.NCMS,
            fpt.ncms.util.StringUtil.*"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCReportBean beanNCReport = (NCReportBean)session.getAttribute("beanNCReport");
    int i = 0;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
<TITLE>NC Report</TITLE>
<SCRIPT language="javascript">
function doLogOut() {
    frmNCReport.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmNCReport.action = "NcmsServlet";
    frmNCReport.submit();
}

function doNCListSearch() {
    frmNCReport.hidAction.value = "<%=NCMS.NC_LIST%>";
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
    if ((!frmNCReport.chkNC.checked) &&
        (!frmNCReport.chkOB.checked) &&
        (!frmNCReport.chkCC.checked) &&
        (!frmNCReport.chkPB.checked))
    {
        alert("Please choose at least NC, OB ,CC or PB to show report");
        frmNCReport.chkNC.focus();
        return false;
    }
    return true;
}
</SCRIPT>
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
<FORM method="post" name="frmNCReport" action="NcmsServlet">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%" leftmargin="10">
    <COLGROUP>
        <COL width="10%">
        <COL width="14%">
        <COL width="5%">
        <COL width="27%">
        <COL width="10%">
        <COL width="20%">
        <COL width="10%">
    <TR>
        <TD>&nbsp;From date</TD>
        <TD><INPUT type="text" size="15" name="txtFromDate" maxlength="11" tabindex="1" value="<%=beanNCReport.getFromDate()%>"></TD>
        <TD>&nbsp;To</TD>
        <TD><INPUT type="text" size="15" name="txtToDate" maxlength="11" tabindex="2" value="<%=beanNCReport.getToDate()%>">&nbsp;(dd-MMM-yy)</TD>
        <TD>&nbsp;&nbsp;&nbsp;&nbsp;Report&nbsp;level&nbsp;&nbsp;<SELECT name="optReportBy" style="width: 70pt" tabindex="3"><%
    for (int nRow = 0; nRow < beanNCReport.getComboGroupBy().getNumberOfRows(); nRow++) {
        out.write("<OPTION value=\"" + beanNCReport.getComboGroupBy().getCell(nRow, 0)
                + (beanNCReport.getComboGroupBy().getCell(nRow, 0).equals(beanNCReport.getGroupBy()) ? "\" selected>" : "\">")
                + beanNCReport.getComboGroupBy().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD>&nbsp;&nbsp;<IMG border="0" title="View report" onclick="doNCReport()" style="cursor: hand" onmouseout="this.src='images/Buttons/b_view_n.gif'" onmouseover="this.src='images/Buttons/b_view_p.gif'" src="images/Buttons/b_view_n.gif" name="imgView"></TD>
    </TR>
    <TR>
        <TD></TD>
        <TD></TD>
        <TD></TD>
        <TD></TD>
        <TD></TD>
        <TD>&nbsp;
        	<INPUT type="checkbox" name="chkNC" <%=beanNCReport.isCheckedNC() ? "checked" : ""%> title="Include NCs" tabindex="2">&nbsp;NC
            <INPUT type="checkbox" name="chkOB" <%=beanNCReport.isCheckedOB() ? "checked" : ""%> title="Include OBs" tabindex="2">&nbsp;OB
            <INPUT type="checkbox" name="chkCC" <%=beanNCReport.isCheckedCC() ? "checked" : ""%> title="Include CCs" tabindex="2">&nbsp;CC
            <INPUT type="checkbox" name="chkPB" <%=beanNCReport.isCheckedPB() ? "checked" : ""%> title="Include PBs" tabindex="2">&nbsp;PB
        </TD>
    </TR>
</TABLE>
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
        <TD>&nbsp;Name</TD>
        <TD>&nbsp;Total</TD>
        <TD>&nbsp;Delaying</TD>
        <TD>&nbsp;Closed</TD>
        <TD>&nbsp;In time</TD>
        <TD>&nbsp;Overtime</TD>
        <TD>Average fix time (days)</TD>
        <TD>&nbsp;Repeated</TD>
    </TR><%
    for (i = 0; i < beanNCReport.getReport().getNumberOfRows(); i++) {
%>
    <TR class="row<%= (i % 2) + 1%>" align="center">
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
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReport.getReport().getCell(i, 7)%></P>
        </TD>
        <TD>
        <P style="MARGIN: 1px 3px 1px 3px"><%=beanNCReport.getReport().getCell(i, 4)%></P>
        </TD>
    </TR><%
    }
    if (i == 0) {%>
    <TR><TD colspan="8" class="row1">No record found! </TD></TR><%
    }
%>
</TABLE>
</FORM>
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
    <TR>
        <TD width="70%">&nbsp;
        <IMG border="0" onclick="doNCReportPivot()" onmouseout="this.src='images/Buttons/b_change_n.gif'" onmouseover="this.src='images/Buttons/b_change_p.gif'" style="cursor: hand" title="Change kind of report" src="images/Buttons/b_change_n.gif" name="imgChange">
        <IMG border="0" onclick="doPrintReport()" style="cursor: hand" title="Print report" onmouseout="this.src='images/Buttons/b_print_n.gif'" onmouseover="this.src='images/Buttons/b_print_p.gif'" src="images/Buttons/b_print_n.gif" name="imgPrint">
        </TD>
    </TR>
</TABLE>
</BODY>
</HTML>