<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language = "java" import = "java.util.*, javax.servlet.*,
            fpt.dms.bean.*, fpt.dms.bean.DefectManagement.*,
            fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe = "false" errorPage = "error.jsp" contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo
            = (UserInfoBean)session.getAttribute("beanUserInfo");
    ReportWeeklyBean beanReport
            = (ReportWeeklyBean)request.getAttribute("beanReport");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<TITLE>Project Weekly Report</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<LINK rel="StyleSheet" href="styles/pcal.css" type="text/css">
<STYLE type="text/css">
    .tooltiptitle{COLOR: #FFFFFF; TEXT-DECORATION: none; CURSOR: Default; font-family: arial; font-weight: bold; font-size: 8pt}
    .tooltipcontent{COLOR: #000000; TEXT-DECORATION: none; CURSOR: Default; font-family: arial; font-size: 8pt}

    #ToolTip{position:absolute; width: 100px; top: 0px; left: 0px; z-index:4; visibility:hidden;}
</STYLE>
<SCRIPT src="scripts/tooltip.js"></SCRIPT>
<SCRIPT language="javascript">
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<DIV>
<P><IMG border="0" src="Images/WeeklyReportListing.gif" width="411" height="28"></P>
</DIV>
<DIV id="ToolTip"></DIV>
<FORM method="POST" action="DMSServlet" name="frmReport">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
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
        <TD width="22%" align="right"><SELECT name="cboProjectList" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="ReportWeekly"%>','<%=""%>');"><%
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
        <TD width="22%" align="right"><SELECT name="cboProjectStatus" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="ReportWeekly"%>','<%=""%>');"><%
    String strCurrentStatus = beanUserInfo.getCurrentStatus();
    for (int i = 0; i < beanComboProject.getStatusList().getNumberOfRows(); i++) {
        StringMatrix smStatus = beanComboProject.getStatusList();
        out.write("<OPTION ");
        out.write(strCurrentStatus.equals(smStatus.getCell(i,0)) ? " selected " : " ");
        out.write("value=\"" + smStatus.getCell(i, 0) + "\">" + smStatus.getCell(i, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>

    </TR>
</TABLE>
<P align="center"><%
    int nTypeReport = 0;
    if (!"".equals(beanReport.getWeeklyReportList().getCell(0, 2))) {
        nTypeReport = Integer.parseInt(beanReport.getWeeklyReportList().getCell(0, 2));
    }
%>
<TABLE border="0" width="60%" cellspacing="0" cellpadding="0" align="center">
    <TR>
        <!-- FROM DATE -->
        <TD width="10%"><B>From Date</B></TD>
        <TD width="25%">
            <INPUT type="text" name="txtFromDate" tabindex="1" class="DateBox" value="<%=beanReport.getWeeklyReportList().getCell(0, 0)%>" size="10" maxlength="8">
            <IMG src="Images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].txtFromDate, document.forms[0].txtFromDate, "mm/dd/yy",null,1,-1,-1,true)'>
            <FONT color="blue">&nbsp;(mm/dd/yy)</FONT>
        </TD>
        <!-- REPORT TYPE -->
        <TD width="10%" align="right"><B>Report Type</B></TD>
        <TD width="15%" align="right"><SELECT size="1" name="cboTypeReport" tabindex="3" class="SmallerCombo">
            <OPTION selected value="0">All</OPTION>
            <OPTION value="1" <%=(nTypeReport == 1) ? "selected" : ""%>>Defect Tracking</OPTION>
            <OPTION value="2" <%=(nTypeReport == 2) ? "selected" : ""%>>Defect Leakage</OPTION>
            <OPTION value="3" <%=(nTypeReport == 3) ? "selected" : ""%>>Defect Type</OPTION>
            <OPTION value="4" <%=(nTypeReport == 4) ? "selected" : ""%>>Defect Distribution</OPTION>
            <OPTION value="5" <%=(nTypeReport == 5) ? "selected" : ""%>>Defect Summary</OPTION>
            <OPTION value="6" <%=(nTypeReport == 6) ? "selected" : ""%>>Defects by TC</OPTION>
        </SELECT></TD>
    </TR>
    <!-- TO DATE -->
    <TR>
        <TD width="10%"><B>To Date</B></TD>
        <TD width="25%">
            <INPUT type="text" name="txtToDate" tabindex="2" class="DateBox" size="10" value="<%=beanReport.getWeeklyReportList().getCell(0, 1)%>" maxlength="8">
            <IMG src="Images/cal.gif" style="CURSOR:hand" onclick='showCalendar(document.forms[0].txtToDate, document.forms[0].txtToDate, "mm/dd/yy",null,1,-1,-1,true)'>
            <FONT color="blue">&nbsp;(mm/dd/yy)</FONT>
        </TD>
        <TD width="10%"></TD>
        <TD width="15%" align="right"><INPUT type="button" tabindex="4" class="button" value="Report" name="WeeklyReport" onclick="doSearch()"></TD>
    </TR>
</TABLE>
<BR><%
    switch (nTypeReport) {
        case 0:
%>
<TABLE border="0;" width="80%">
    <TR>
        <TD align="right" width="100%"><FONT style="COLOR: black; FONT-FAMILY: Verdana; FONT-SIZE: xx-small">(Unit: number of defect)</FONT></TD>
    </TR>
</TABLE>
<CENTER>
<TABLE border="0" cellpadding="2" cellspacing="1" width="80%" bgcolor="#000000">
    <COLGROUP>
        <COL width="10%">
        <COL width="10%">
        <COL width="10%">
        <COL width="10%">
        <COL width="10%">
        <COL width="10%">
        <COL width="10%">
        <COL width="10%">
    <TR class="Row0">
        <TD align="center"><B>Severity</B></TD>
        <TD align="center"><B>Error</B></TD>
        <TD align="center"><B>Assigned</B></TD>
        <TD align="center"><B>Pending</B></TD>
        <TD align="center"><B>Tested</B></TD>
        <TD align="center"><B>Accepted</B></TD>
        <TD align="center"><B>Cancelled</B></TD>
        <TD align="center"><B>Total</B></TD>
    </TR><%
            for (int i = 0; i < beanReport.getWeeklyReportList().getNumberOfRows(); i++) {
                String clsTR = "Row2";
                if (i % 2 == 0) {
                    clsTR = "Row1";
                }
%>
    <TR class="<%=clsTR%>">
        <TD style="text-indent: 10"><%=beanReport.getWeeklyReportList().getCell(i, 3)%></TD>
        <TD align="center"><%=beanReport.getWeeklyReportList().getCell(i, 4)%></TD>
        <TD align="center"><%=beanReport.getWeeklyReportList().getCell(i, 5)%></TD>
        <TD align="center"><%=beanReport.getWeeklyReportList().getCell(i, 6)%></TD>
        <TD align="center"><%=beanReport.getWeeklyReportList().getCell(i, 7)%></TD>
        <TD align="center"><%=beanReport.getWeeklyReportList().getCell(i, 8)%></TD>
        <TD align="center"><%=beanReport.getWeeklyReportList().getCell(i, 9)%></TD>
        <TD align="center"><B><%=beanReport.getWeeklyReportList().getCell(i, 10)%></B></TD>
    </TR><%
            }
%>
</TABLE>
</CENTER><%
            break;
        case 1: // Report Defect Tracking
%>
<TABLE border="0;" width="100%">
    <TR>
        <TD align="right" width="100%"><FONT style="COLOR: black; FONT-FAMILY: Verdana; FONT-SIZE: xx-small">(Unit: number of defect)</FONT></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" bgcolor="#000000">
    <TR class="Row0">
        <TD width="18%" align="center" rowspan="2"><B>Work Product</B></TD>
        <TD width="18%" align="center" rowspan="2"><B>Module</B></TD>
        <TD width="20%" align="center" colspan="5"><B>By Reviewing</B></TD>
        <TD width="20%" align="center" colspan="5"><B>By Testing</B></TD>
        <TD width="20%" align="center" colspan="5"><B>By Others</B></TD>
        <TD width="20%" align="center" colspan="5"><B>Fixed Defects</B></TD>
    </TR>
    <TR class="Row0">
        <!-- BY REVIEWING  -->
        <TD width="3%" align="center"><B>F</B></TD>
        <TD width="3%" align="center"><B>S</B></TD>
        <TD width="3%" align="center"><B>M</B></TD>
        <TD width="3%" align="center"><B>C</B></TD>
        <TD width="3%" align="center"><B>W</B></TD>
        <!-- BY TESTING  -->
        <TD width="3%" align="center"><B>F</B></TD>
        <TD width="3%" align="center"><B>S</B></TD>
        <TD width="3%" align="center"><B>M</B></TD>
        <TD width="3%" align="center"><B>C</B></TD>
        <TD width="3%" align="center"><B>W</B></TD>
        <!-- BY OTHERS  -->
        <TD width="3%" align="center"><B>F</B></TD>
        <TD width="3%" align="center"><B>S</B></TD>
        <TD width="3%" align="center"><B>M</B></TD>
        <TD width="3%" align="center"><B>C</B></TD>
        <TD width="3%" align="center"><B>W</B></TD>
        <!-- FIXED DEFECTS  -->
        <TD width="3%" align="center"><B>F</B></TD>
        <TD width="3%" align="center"><B>S</B></TD>
        <TD width="3%" align="center"><B>M</B></TD>
        <TD width="3%" align="center"><B>C</B></TD>
        <TD width="3%" align="center"><B>W</B></TD>
    </TR><%
            boolean flag = true;
            String strCurrentItem = "", strName = "";
            int nClass = 0;
            for (int i = 1; i < beanReport.getWeeklyReportList().getNumberOfRows(); i++) {
                String strItem = beanReport.getWeeklyReportList().getCell(i, 3);
                strName = beanReport.getWeeklyReportList().getCell(i, 3);
                if (strCurrentItem.equals(strItem)) {
                    strName = "";
                    nClass -= 1;
                }
                strCurrentItem = strItem;
                String clsTR = "Row2";
                if (nClass % 2 == 0) {
                    clsTR = "Row1";
                }
%>
    <TR class="<%=clsTR%>">
        <!-- WorkProduct Name -->
        <TD width="18%"><%=strName%></TD>
        <!-- Module Name -->
        <TD width="18%"><%=beanReport.getWeeklyReportList().getCell(i, 24)%></TD>
        <!-- BY REVIEWING -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 4)%></TD>
        <!-- Fatal Review -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 5)%></TD>
        <!-- Serious Review -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 6)%></TD>
        <!-- Medium Review -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 7)%></TD>
        <!-- Cosmetic Review -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 8)%></TD>
        <!-- Weighted Review -->
        <!-- BY TESTING -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 9)%></TD>
        <!-- Fatal Testing -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 10)%></TD>
        <!-- Serious Testing -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 11)%></TD>
        <!-- Medium Testing -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 12)%></TD>
        <!-- Cosmetic Testing -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 13)%></TD>
        <!-- Weighted Testing -->
        <!-- BY OTHERS -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 14)%></TD>
        <!-- Fatal Other -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 15)%></TD>
        <!-- Serious Other -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 16)%></TD>
        <!-- Medium Other -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 17)%></TD>
        <!-- Cosmetic Other -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 18)%></TD>
        <!-- Weighted Other -->
        <!-- FIXED DEFECTS -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 19)%></TD>
        <!-- Fatal Fixed -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 20)%></TD>
        <!-- Serious Fixed -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 21)%></TD>
        <!-- Medium Fixed -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 22)%></TD>
        <!-- Cosmetic Fixed -->
        <TD width="3%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 23)%></TD>
        <!-- Weighted Fixed -->
    </TR><%
                nClass += 1;
            }
%>
</TABLE><%
            break;
        case 2: // Report Defect Leakage
%>
<TABLE border="0;" width="80%">
    <TR>
        <TD align="right" width="100%"><FONT style="COLOR: black; FONT-FAMILY: Verdana; FONT-SIZE: xx-small">(Unit: number of defect)</FONT></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="1" cellspacing="1" width="80%" bgcolor="#000000">
    <TR class="Row0">
        <TD width="20%" align="center" rowspan="2"><B>Work product</B></TD>
        <TD width="20%" align="center" rowspan="2"><B>Module</B></TD>
        <TD width="15%" align="center" colspan="5"><B>Total</B></TD>
        <TD width="15%" align="center" colspan="5"><B>Fixed</B></TD>
    </TR>
    <TR class="Row0">
        <TD width="5%" align="center"><B>F</B></TD>
        <TD width="5%" align="center"><B>S</B></TD>
        <TD width="5%" align="center"><B>M</B></TD>
        <TD width="5%" align="center"><B>C</B></TD>
        <TD width="5%" align="center"><B>W</B></TD>
        <TD width="5%" align="center"><B>F</B></TD>
        <TD width="5%" align="center"><B>S</B></TD>
        <TD width="5%" align="center"><B>M</B></TD>
        <TD width="5%" align="center"><B>C</B></TD>
        <TD width="5%" align="center"><B>W</B></TD>
    </TR><%
            String strLRCurrent = "", strLRDisplayed = "";
            int nLRTR = 0;
            for (int i = 1; i < beanReport.getWeeklyReportList().getNumberOfRows(); i++) {
                String strItem = beanReport.getWeeklyReportList().getCell(i, 3);
                strLRDisplayed = beanReport.getWeeklyReportList().getCell(i, 3);
                if (strLRCurrent.equals(strItem)) {
                    strLRDisplayed = "";
                    nLRTR -= 1;
                }
                strLRCurrent = strItem;
                String clsTR = "Row2";
                if (nLRTR % 2 == 0) {
                    clsTR = "Row1";
                }
%>
    <TR class="<%=clsTR%>">
        <TD width="20%"><%=strLRDisplayed%></TD>
        <TD width="20%"><%=beanReport.getWeeklyReportList().getCell(i, 4)%>&nbsp;</TD>
        <TD width="5%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 5)%></TD>
        <TD width="5%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 6)%></TD>
        <TD width="5%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 7)%></TD>
        <TD width="5%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 8)%></TD>
        <TD width="5%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 13)%></TD>
        <TD width="5%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 9)%></TD>
        <TD width="5%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 10)%></TD>
        <TD width="5%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 11)%></TD>
        <TD width="5%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 12)%></TD>
        <TD width="5%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 14)%></TD>
    </TR><%
                nLRTR += 1;
            }
%>
</TABLE><%
            break;
        case 3: // Report Defect Type
%>
<TABLE border="0;" width="100%">
    <TR>
        <TD align="right" width="100%"><FONT style="COLOR: black; FONT-FAMILY: Verdana; FONT-SIZE: xx-small">(Unit: number of defect)</FONT></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" bgcolor="#000000">
    <TR class="Row0">
        <TD width="28%" align="center"><B>Defect Type</B></TD>
        <TD width="12%" align="center"><B>Fatal</B></TD>
        <TD width="12%" align="center"><B>Serious</B></TD>
        <TD width="12%" align="center"><B>Medium</B></TD>
        <TD width="12%" align="center"><B>Cosmetic</B></TD>
        <TD width="12%" align="center"><B>Total</B></TD>
        <TD width="12%" align="center"><B>Total Wdef</B></TD>
    </TR><%
            for (int i = 0; i < beanReport.getWeeklyReportList().getNumberOfRows(); i++) {
%>
    <TR class="Row<%=i % 2 + 1%>">
        <TD width="28%"><%=beanReport.getWeeklyReportList().getCell(i, 3)%></TD>
        <TD width="12%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 4)%></TD>
        <TD width="12%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 5)%></TD>
        <TD width="12%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 6)%></TD>
        <TD width="12%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 7)%></TD>
        <TD width="12%" align="center"><B><%=beanReport.getWeeklyReportList().getCell(i, 8)%></B></TD>
        <TD width="12%" align="center"><B><%=beanReport.getWeeklyReportList().getCell(i, 9)%></B></TD>
    </TR><%
            }
%>
</TABLE><%
            break;
        case 4: // Report Defect Distribution
%>
<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" bgcolor="#000000">
    <TR class="Row0">
        <TD width="20%" align="center" rowspan="2"><B>QC Activity</B></TD>
        <TD width="20%" align="center" colspan="5"><B>Requirement</B></TD>
        <TD width="20%" align="center" colspan="5"><B>Design</B></TD>
        <TD width="20%" align="center" colspan="5"><B>Coding</B></TD>
        <TD width="20%" align="center" colspan="5"><B>Other</B></TD>
    </TR>
    <TR class="Row0">
        <TD width="4%" align="center"><B>F</B></TD>
        <TD width="4%" align="center"><B>S</B></TD>
        <TD width="4%" align="center"><B>M</B></TD>
        <TD width="4%" align="center"><B>C</B></TD>
        <TD width="4%" align="center"><B>W</B></TD>
        <TD width="4%" align="center"><B>F</B></TD>
        <TD width="4%" align="center"><B>S</B></TD>
        <TD width="4%" align="center"><B>M</B></TD>
        <TD width="4%" align="center"><B>C</B></TD>
        <TD width="4%" align="center"><B>W</B></TD>
        <TD width="4%" align="center"><B>F</B></TD>
        <TD width="4%" align="center"><B>S</B></TD>
        <TD width="4%" align="center"><B>M</B></TD>
        <TD width="4%" align="center"><B>C</B></TD>
        <TD width="4%" align="center"><B>W</B></TD>
        <TD width="4%" align="center"><B>F</B></TD>
        <TD width="4%" align="center"><B>S</B></TD>
        <TD width="4%" align="center"><B>M</B></TD>
        <TD width="4%" align="center"><B>C</B></TD>
        <TD width="4%" align="center"><B>W</B></TD>
    </TR><%
            int nWeightedDefect = 0;
            for (int i = 0; i < beanReport.getWeeklyReportList().getNumberOfRows(); i++) {
                String strRW = beanReport.getWeeklyReportList().getCell(i, 8);
                String strDW = beanReport.getWeeklyReportList().getCell(i, 13);
                String strCW = beanReport.getWeeklyReportList().getCell(i, 18);
                String strOW = beanReport.getWeeklyReportList().getCell(i, 23);
                if (i == beanReport.getWeeklyReportList().getNumberOfRows() - 1) {
                    int nRW = "".equals(strRW) ? 0 : Integer.parseInt(strRW);
                    int nDW = "".equals(strDW) ? 0 : Integer.parseInt(strDW);
                    int nCW = "".equals(strCW) ? 0 : Integer.parseInt(strCW);
                    int nOW = "".equals(strOW) ? 0 : Integer.parseInt(strOW);
                    nWeightedDefect += nRW + nDW + nCW + nOW;
                }
%>
    <TR class="Row<%=i % 2 + 1%>">
        <TD width="20%"><%=beanReport.getWeeklyReportList().getCell(i, 3)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 4)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 5)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 6)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 7)%></TD>
        <TD width="4%" align="center"><B><%=strRW%></B></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 9)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 10)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 11)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 12)%></TD>
        <TD width="4%" align="center"><B><%=strDW%></B></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 14)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 15)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 16)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 17)%></TD>
        <TD width="4%" align="center"><B><%=strCW%></B></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 19)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 20)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 21)%></TD>
        <TD width="4%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 22)%></TD>
        <TD width="4%" align="center"><B><%=strOW%></B></TD>
    </TR><%
            }
%>
</TABLE>
<P align="left"><B><FONT face="Verdana" size="1">Total weighted defects: <%=nWeightedDefect%></FONT></B></P><%
            break;
        case 6: // Report Defect By TestCaseId
%>
<TABLE border="0;" width="100%">
    <TR>
        <TD align="left" width="100%"><FONT style="COLOR: BLUE; FONT-FAMILY: Verdana; FONT-SIZE: xx-small">Only defects found by Testing (Type of activity is "test") will be reported here</FONT></TD>
    </TR>
</TABLE>

<TABLE border="0;" width="100%">
    <TR>
        <TD align="right" width="100%"><FONT style="COLOR: black; FONT-FAMILY: Verdana; FONT-SIZE: xx-small">(Unit: number of defect)</FONT></TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" bgcolor="#000000">
    <TR class="Row0">
        <TD width="28%" align="center"><B>Test case name/ID</B></TD>
        <TD width="12%" align="center"><B>Fatal</B></TD>
        <TD width="12%" align="center"><B>Serious</B></TD>
        <TD width="12%" align="center"><B>Medium</B></TD>
        <TD width="12%" align="center"><B>Cosmetic</B></TD>
        <TD width="12%" align="center"><B>Total</B></TD>
        <TD width="12%" align="center"><B>Total Wdef</B></TD>
    </TR><%
            for (int i = 0; i < beanReport.getWeeklyReportList().getNumberOfRows(); i++) {
%>
    <TR class="Row<%=i % 2 + 1%>">
        <TD width="28%"><%=beanReport.getWeeklyReportList().getCell(i, 3)%></TD>
        <TD width="12%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 4)%></TD>
        <TD width="12%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 5)%></TD>
        <TD width="12%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 6)%></TD>
        <TD width="12%" align="center"><%=beanReport.getWeeklyReportList().getCell(i, 7)%></TD>
        <TD width="12%" align="center"><B><%=beanReport.getWeeklyReportList().getCell(i, 8)%></B></TD>
        <TD width="12%" align="center"><B><%=beanReport.getWeeklyReportList().getCell(i, 9)%></B></TD>
    </TR><%
            }
%>
</TABLE><%
            break;
            
        //Defect Summary Report
        case 5: {
%>
<DIV align="right" style="COLOR: black; FONT-FAMILY: Verdana; FONT-SIZE: xx-small">(Unit: number of weighted defect)</DIV>
<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" bgcolor="#000000">
    <COLGROUP>
        <COL width="20%">
        <COL width="20%">
        <COL width="15%">
        <COL width="15%">
        <COL width="15%">
        <COL width="15%">
    <TR class="Row0">
        <TD align="center" height="19"><B>Work Product</B></TD>
        <TD align="center" height="19"><B>Module</B></TD>
        <TD align="center" height="19"><B>Planned Defect</B></TD>
        <TD align="center" height="19"><B>Re-planned Defect</B></TD>
        <TD align="center" height="19"><B><A href="javascript:doViewActualDetail()" onmouseover="EnterContent('ToolTip', 'Detailed by', 'QC Activity or Process injected'); Activate();" onmouseout="deActivate(); overhere();" onmousemove="overhere()" style="color: yellow; TEXT-DECORATION: NONE">Actual Defect</A></B><INPUT type="hidden" name="hidActualDetail" value="0"></TD>
        <TD align="center" height="19"><B>Deviation</B></TD>
    </TR><%
            String strCurrent = "", strDisplayed = "";
            int nTR = 0;
            for (int i = 0; i < beanReport.getWeeklyReportList().getNumberOfRows(); i++) {
                String strItem = beanReport.getWeeklyReportList().getCell(i, 3);
                strDisplayed = beanReport.getWeeklyReportList().getCell(i, 3);
                if (strCurrent.equals(strItem)) {
                    strDisplayed = "";
                    nTR -= 1;
                }
                strCurrent = strItem;
                String clsTR = "Row2";
                if (nTR % 2 == 0) {
                    clsTR = "Row1";
                }
%>
    <TR class="<%=clsTR%>">
        <!-- WorkProduct Name -->
        <TD><%=strDisplayed%></TD>
        <!-- Module Name -->
        <TD><%=beanReport.getWeeklyReportList().getCell(i,  4)%></TD>
        <TD align="center"><%=beanReport.getWeeklyReportList().getCell(i, 5)%></TD>
        <!-- Planned defect -->
        <TD align="center"><%=beanReport.getWeeklyReportList().getCell(i, 6)%></TD>
        <!-- Re-planned defect -->
        <TD align="center"><%=beanReport.getWeeklyReportList().getCell(i, 7)%></TD>
        <!-- Actual defect -->
        <TD align="center"><%=beanReport.getWeeklyReportList().getCell(i, 8)%></TD>
        <!-- Deviation -->
    </TR><%
                nTR += 1;
            }
%>
</TABLE>
<BR><%
            if (beanReport.getSubReportType() > 0) {
                String isSelected1 = "", isSelected2 = "";
                if (beanReport.getSubReportType() == 1) {
                    isSelected1 = "selected";
                }
                else {
                    isSelected2 = "selected";
                }
%>
<!-- Detailed table  -->
<TABLE border="0" cellpadding="1" cellspacing="1" width="100%">
    <TR>
        <TD width="50%">
        <TABLE border="0" cellpadding="1" cellspacing="1" width="50%" align="left">
            <TR>
                <TD><B>Actual Defect Detail</B></TD>
                <TD><SELECT size="1" name="cboDetailedBy" class="SmallerCombo">
                    <OPTION value="1" <%=isSelected1%>>By QC Activity</OPTION>
                    <OPTION value="2" <%=isSelected2%>>By Process injected</OPTION>
                </SELECT></TD>
                <TD><INPUT type="button" name="ViewDetailedBy" value="View Detail" onclick='javascript:doViewDetail()'></TD>
            </TR>
        </TABLE>
        </TD>
    </TR>
    <TR>
        <TD width="100%"><%
                if (beanReport.getSubReportType() == 1) {
%>
        <DIV align="right" style="COLOR: black; FONT-FAMILY: Verdana; FONT-SIZE: xx-small">(Unit: number of weighted defect)</DIV>
        <TABLE border="0" cellpadding="1" cellspacing="1" width="100%" bgcolor="#000000">
            <COLGROUP>
                <COL width="18%">
                <COL width="18%">
                <COL width="8%">
                <COL width="8%">
                <COL width="8%">
                <COL width="8%">
                <COL width="8%">
                <COL width="8%">
                <COL width="8%">
                <COL width="8%">
            <TR class="Row0">
                <TD align="center"><B>Work Product</B></TD>
                <TD align="center"><B>Module</B></TD>
                <TD align="center"><B>Document Review</B></TD>
                <TD align="center"><B>Prototype Review</B></TD>
                <TD align="center"><B>Code Review</B></TD>
                <TD align="center"><B>Unit Test</B></TD>
                <TD align="center"><B>Integration Test</B></TD>
                <TD align="center"><B>System Test</B></TD>
                <TD align="center"><B>Acceptance Test</B></TD>
                <TD align="center"><B>Others</B></TD>
            </TR><%
                    strCurrent = ""; strDisplayed = "";
                    nTR = 0;
                    for (int i = 0; i < beanReport.getSubSummaryReport().getNumberOfRows(); i++) {
                        String strItem = beanReport.getSubSummaryReport().getCell(i, 1);
                        strDisplayed = beanReport.getSubSummaryReport().getCell(i, 1);
                        if (strCurrent.equals(strItem)) {
                            strDisplayed = "";
                            nTR -= 1;
                        }
                        strCurrent = strItem;
                        String clsTR = "Row2";
                        if (nTR % 2 == 0) {
                            clsTR = "Row1";
                        }
%>
            <TR class="<%=clsTR%>">
                <!-- WorkProduct Name -->
                <TD><%=strDisplayed%></TD>
                <!-- Module Name -->
                <TD><%=beanReport.getSubSummaryReport().getCell(i, 2)%></TD>
                <TD align="center"><%=beanReport.getSubSummaryReport().getCell(i, 3)%></TD>
                <!-- Document review -->
                <TD align="center"><%=beanReport.getSubSummaryReport().getCell(i, 4)%></TD>
                <!-- Prototype review-->
                <TD align="center"><%=beanReport.getSubSummaryReport().getCell(i, 5)%></TD>
                <!-- Code review -->
                <TD align="center"><%=beanReport.getSubSummaryReport().getCell(i, 6)%></TD>
                <!-- Unit test -->
                <TD align="center"><%=beanReport.getSubSummaryReport().getCell(i, 7)%></TD>
                <!-- Integration test -->
                <TD align="center"><%=beanReport.getSubSummaryReport().getCell(i, 8)%></TD>
                <!-- System test -->
                <TD align="center"><%=beanReport.getSubSummaryReport().getCell(i, 9)%></TD>
                <!-- Acceptance test -->
                <TD align="center"><%=beanReport.getSubSummaryReport().getCell(i, 10)%></TD>
                <!-- Others -->
            </TR><%
                        nTR += 1;
                    }
%>
        </TABLE><%
                }
                else if (beanReport.getSubReportType() == 2) {
%>
        <DIV align="right" style="COLOR: black; FONT-FAMILY: Verdana; FONT-SIZE: xx-small">(W: weighted)</DIV>
        <TABLE border="0" cellpadding="1" cellspacing="1" width="100%" bgcolor="#000000">
            <TR class="Row0">
                <TD width="20%" align="center" rowspan="2"><B>Work Product</B></TD>
                <TD width="20%" align="center" rowspan="2"><B>Module</B></TD>
                <TD width="15%" align="center" colspan="5"><B>Requirement</B></TD>
                <TD width="15%" align="center" colspan="5"><B>Design</B></TD>
                <TD width="15%" align="center" colspan="5"><B>Coding</B></TD>
                <TD width="15%" align="center" colspan="5"><B>Other</B></TD>
            </TR>
            <TR class="Row0">
                <TD width="3%" align="center"><B>F</B></TD>
                <TD width="3%" align="center"><B>S</B></TD>
                <TD width="3%" align="center"><B>M</B></TD>
                <TD width="3%" align="center"><B>C</B></TD>
                <TD width="3%" align="center"><B>W</B></TD>
                <TD width="3%" align="center"><B>F</B></TD>
                <TD width="3%" align="center"><B>S</B></TD>
                <TD width="3%" align="center"><B>M</B></TD>
                <TD width="3%" align="center"><B>C</B></TD>
                <TD width="3%" align="center"><B>W</B></TD>
                <TD width="3%" align="center"><B>F</B></TD>
                <TD width="3%" align="center"><B>S</B></TD>
                <TD width="3%" align="center"><B>M</B></TD>
                <TD width="3%" align="center"><B>C</B></TD>
                <TD width="3%" align="center"><B>W</B></TD>
                <TD width="3%" align="center"><B>F</B></TD>
                <TD width="3%" align="center"><B>S</B></TD>
                <TD width="3%" align="center"><B>M</B></TD>
                <TD width="3%" align="center"><B>C</B></TD>
                <TD width="3%" align="center"><B>W</B></TD>
            </TR><%
                    strCurrent = ""; strDisplayed = "";
                    nTR = 0;
                    for (int i = 0; i < beanReport.getSubSummaryReport().getNumberOfRows(); i++) {
                        String strItem = beanReport.getSubSummaryReport().getCell(i, 1);
                        strDisplayed = beanReport.getSubSummaryReport().getCell(i, 1);
                        if (strCurrent.equals(strItem)) {
                            strDisplayed = "";
                        }
                        strCurrent = strItem;
                        String clsTR = "Row2";
                        if (nTR % 2 == 0) {
                            clsTR = "Row1";
                        }
%>
            <TR class="<%=clsTR%>">
                <!-- WorkProduct Name -->
                <TD><%=strDisplayed%></TD>
                <!-- Module Name -->
                <TD><%=beanReport.getSubSummaryReport().getCell(i, 2)%></TD>
                <!-- REQUIREMENT  -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 3)%></TD>
                <!-- Fatal requirement -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 4)%></TD>
                <!-- Serious requirement -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 5)%></TD>
                <!-- Medium review -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 6)%></TD>
                <!-- Cosmetic test -->
                <TD width="3%" align="center"><B><%=beanReport.getSubSummaryReport().getCell(i, 7)%></B></TD>
                <!-- Weighted -->
                <!-- DESIGN -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 8) %></TD>
                <!-- Fatal design -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 9) %></TD>
                <!-- Serious design -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 10)%></TD>
                <!-- Medium design -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 11)%></TD>
                <!-- Cosmetic design -->
                <TD width="3%" align="center"><B><%=beanReport.getSubSummaryReport().getCell(i, 12)%></B></TD>
                <!-- Weighted -->
                <!-- CODING  -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 13) %></TD>
                <!-- Fatal coding -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 14) %></TD>
                <!-- Serious coding -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 15)%></TD>
                <!-- Medium coding -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 16)%></TD>
                <!-- Cosmetic coding -->
                <TD width="3%" align="center"><B><%=beanReport.getSubSummaryReport().getCell(i, 17)%></B></TD>
                <!-- Weighted -->
                <!-- OTHER -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 18) %></TD>
                <!-- Fatal other -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 19) %></TD>
                <!-- Serious other -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 20)%></TD>
                <!-- Medium other -->
                <TD width="3%" align="center"><%=beanReport.getSubSummaryReport().getCell(i, 21)%></TD>
                <!-- Cosmetic other -->
                <TD width="3%" align="center"><B><%=beanReport.getSubSummaryReport().getCell(i, 22)%></B></TD>
                <!-- Weighted -->
            </TR><%
                        nTR += 1;
                    }
%>
        </TABLE><%
                }   //end else
%>
        </TD>
    </TR>
</TABLE><!-- End Detailed table  --><%
            }   //end if (request)
            break;
        }
    }//end switch
%>
</FORM>
<BR>
<BR>
<BR>
<BR>
<BR>
</BODY>
</HTML>
<SCRIPT language="javascript">
function doSearch() {
    if (!isValidForm()) {
        return;
    }
    var form = document.frmReport;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "ReportWeekly";
    form.action = "DMSServlet";
    form.submit();
}

function doViewActualDetail() {
    var form = document.frmReport;
    form.hidActualDetail.value = "1";       //By QC Activity
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "ReportWeekly";
    form.action = "DMSServlet";
    form.submit();
}

function doViewDetail() {
    var form = document.frmReport;
    if (!isValidForm()) {
        return;
    }
    form.hidActualDetail.value = form.cboDetailedBy.value;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "ReportWeekly";
    form.action = "DMSServlet";
    form.submit();
}

function doQueryListing() {
    var form = document.frmReport;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}


function isValidForm() {
    var count;
    var form = document.forms[0];
    for (count = 0x00; count < form.length; count++) {
        if (!isValidControl(form.item(count))) {
            form.item(count).focus();
            return false;
        }
    }

    if ((form.txtFromDate.value.length > 0) && (form.txtToDate.value.length > 0)) {
        if (CompareDate(form.txtFromDate, form.txtToDate) > 0) {
            alert("From Date must lower or equal To Date");
            form.txtFromDate.focus();
            return false;
        }
    }
    return true;
}

function isValidControl(control) {
    if (control.name == "txtFromDate" || control.name == "txtToDate") {
        if (control.value.length <= 0) {
            return true;
        }
        else if (isDate(control)){
            return true;
        }
        else {
            return false;
        }
    }
    return true;

    /*
        if (document.forms[0].txtFromDate.value != "") {
            control.name = "FromDate";
            if (!isDate(control)) {
                control.name = "txtFromDate";
                return false;
            }
            else {
                control.name = "txtFromDate";
                return true;
            }
        }
    }
    if (control.name == "txtToDate") {
        if (document.forms[0].txtToDate.value != "") {
            control.name = "ToDate";
            if (!isDate(control)) {
                control.name = "txtToDate";
                return false;
            }
            else {
                control.name = "txtToDate";
                return true;
            }
        }
    }
    return true;
    */
}
</SCRIPT>