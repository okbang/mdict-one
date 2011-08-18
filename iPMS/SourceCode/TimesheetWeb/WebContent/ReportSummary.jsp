<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="
    		javax.servlet.*,java.util.ArrayList,
    		fpt.timesheet.bean.*,
            fpt.timesheet.bean.Report.SummaryReportBean,
            fpt.timesheet.framework.util.CommonUtil.*,
            fpt.timesheet.framework.util.StringUtil.StringMatrix,
            fpt.timesheet.constant.Timesheet"
%>
<%@ page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    String strRole = beanUserInfo.getRole();
    SummaryReportBean beanSummaryReport = (SummaryReportBean)request.getAttribute("beanSummaryReport");
%>
<HTML>
<HEAD>
<TITLE>Summary Report</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<LINK rel="stylesheet" type="text/css" href="styles/pcal.css">
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT src='scripts/popcalendar.js'></SCRIPT>
</HEAD>
<BODY bgcolor="#336699" onkeypress='javascript:setKeypress()'>
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H1><IMG align="top" src="image/tit_SummaryReport.gif"></H1>
<FORM method="post" action="TimesheetServlet" name="frmReportSummary">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="hidTypeOfView" value="SummaryReport">
<INPUT type="hidden" name="hidProjectsList" value="">
<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName() %></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR>
<%
    StringMatrix smtProjectList = beanSummaryReport.getProjectList();
    StringMatrix smtGroupList = beanSummaryReport.getGroupList();
    smtProjectList.setCell(0, 0, "0");
    StringMatrix smtProjecStatustList = beanSummaryReport.getProjectStatusList();
%>
<TABLE border="0" cellpadding="2" cellspacing="0" width="100%" align="center">
    <COLGROUP>
        <COL width="7%">
        <COL width="20%">
        <COL width="10%">
        <COL width="20%">
        <COL width="10%">
        <COL width="20%">
        <COL width="13%">
    <TR>
    	<!-- Group -->
    	<TD><STRONG><FONT color="#ffffff" class="label1">Group</FONT></STRONG></TD>
        <TD>
        	<SELECT name="srptGroup" class="SmallCombo" onchange="changeCombo()">
        	<%for (int nRow = 0; nRow < smtGroupList.getNumberOfRows(); nRow++) {
        		String strText = smtGroupList.getCell(nRow, 0);
        		out.write("<OPTION value='" + strText + "'" + (strText.equals(beanSummaryReport.getGroup()) ? "selected" : "") + ">" + strText + "</OPTION>");
    		}%>
        	</SELECT>
        </TD>
        <!-- FROM DATE  -->
        <TD><STRONG><FONT color="#ffffff" class="label1">From date</FONT></STRONG></TD>
        <TD><INPUT type="text" name="FromDate" size="20" value="<%=beanSummaryReport.getFromDate()%>" maxlength="8" class="smallTextbox">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, FromDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <!-- REPORT TYPE  -->
        <TD align="left"><STRONG><FONT color="#ffffff" class="label1">Report</FONT></STRONG></TD>
        <TD>        
        <SELECT size="1" name="ReportType" class="BigCombo" value="<%=beanSummaryReport.getReportType()%>">
            <OPTION <%=beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_SUMMARY ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_SUMMARY%>">Summary</OPTION>
            <OPTION <%=beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PROCESS_TOW ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_PROCESS_TOW%>">Process and TypeOfWork</OPTION>
            <OPTION <%=beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PRODUCT_TOW ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_PRODUCT_TOW%>">WorkProduct and TypeOfWork</OPTION>
            <OPTION <%=beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_KPA_TOW ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_KPA_TOW%>">KPA and TypeOfWork</OPTION>
            <OPTION <%=beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_ACCOUNT_DATE ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_ACCOUNT_DATE%>">Account and Date</OPTION>
            <OPTION <%=beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PROJECT_PROCESS ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_PROJECT_PROCESS%>">Project and Process</OPTION>
            <OPTION <%=beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PROJECT_PRODUCT ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_PROJECT_PRODUCT%>">Project and WorkProduct</OPTION>
            <OPTION <%=beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PROJECT_KPA ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_PROJECT_KPA%>">Project and KPA</OPTION>
            <OPTION <%=beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PROJECT_TOW ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_PROJECT_TOW%>">Project and TypeOfWork</OPTION>
            <OPTION <%=beanSummaryReport.getReportType() == Timesheet.REPORTTYPE_PROJECT_SUMMARY ? "selected" : ""%> value="<%=Timesheet.REPORTTYPE_PROJECT_SUMMARY%>">Project Summary</OPTION>
        </SELECT>
        </TD>
        <TD>&nbsp;</TD>
    </TR>
    <TR>
        <!-- PROJECT STATUS -->
        <TD><STRONG><FONT color="#ffffff" class="label1">Project Status</FONT></STRONG></TD>
        <TD align="left">
        	<SELECT name="srptProjectStatus" onchange="changeCombo()" size="1" class="SmallCombo">
        	<%String strCurrentProjectStatus = Integer.toString(beanSummaryReport.getProjectStatus());
    		for (int nRow = 0; nRow < smtProjecStatustList.getNumberOfRows(); nRow++) {
        		String strValue = smtProjecStatustList.getCell(nRow, 0);
       	 		String strText = smtProjecStatustList.getCell(nRow, 1);
        		out.write("<OPTION value='" + strValue + "'" + (strCurrentProjectStatus.equals(strValue) ? "selected" : "") + ">" + strText + "</OPTION>");
    		}%>
        	</SELECT>
        </TD>
        <!-- TO DATE  -->
        <TD align="left"><STRONG><FONT color="#ffffff" class="label1">To date</FONT></STRONG></TD>
        <TD><INPUT type="text" name="ToDate" size="20" value="<%=beanSummaryReport.getToDate()%>" maxlength="8" class="smallTextbox">
            <IMG src="image/cal.gif" style="CURSOR:hand" onclick='showCalendar(this, ToDate, "mm/dd/yy",null,1,-1,-1,true)'>
        </TD>
        <TD align="left"><STRONG><FONT color="#ffffff" class="label1">Project type</FONT></STRONG></TD>
        <!-- PROJECT TYPE  -->
        <TD align="left">
        	<SELECT name="srptProjectType" size="1" class="BigCombo">
        	    <OPTION value="10" <%=beanSummaryReport.getProjectType().equals("10") ? "selected" : ""%>>All types</OPTION>
        	    <OPTION value='0' <%=beanSummaryReport.getProjectType().equals("0") ? "selected" : ""%>>External</OPTION>
        	    <OPTION value='9' <%=beanSummaryReport.getProjectType().equals("9") ? "selected" : ""%>>Other</OPTION>
        	    <OPTION value='8' <%=beanSummaryReport.getProjectType().equals("8") ? "selected" : ""%>>Internal</OPTION>
        	</SELECT>
        </TD>
        <!-- SEARCH button  -->
        <TD align="right"></TD>
    </TR>
    <TR>
        <!-- PROJECT -->
        <TD><STRONG><FONT color="#ffffff" class="label1">Project</FONT></STRONG></TD>
        <TD>
        	<SELECT name="srcboProject" class="SmallCombo" value="<%=beanSummaryReport.getProject()%>" onchange="checkPro()">
        	</SELECT>
        </TD>
        <TD align="left"><STRONG><FONT color="#ffffff" class="label1">Account</FONT></STRONG></TD>
        <TD>
        	<INPUT type="text" name="srptAccount" value="<%=beanSummaryReport.getAccount()%>" size="20"
            class="smallTextbox" onkeypress="javascript:toUpperCase()">
        </TD>
        <!-- TIMESHEET STATUS -->
        <TD><STRONG><FONT color="#ffffff" class="label1">Timesheet Status</FONT></STRONG></TD>
        <TD><SELECT name="Status" class="BigCombo" value="<%=beanSummaryReport.getStatus()%>">
            <OPTION value="<%=beanSummaryReport.STATUS_UNAPPROVED%>" <%=beanSummaryReport.getStatus() == beanSummaryReport.STATUS_UNAPPROVED ? "selected" : ""%>>Unapproved</OPTION>
            <OPTION value="<%=beanSummaryReport.STATUS_PLGL%>" <%=beanSummaryReport.getStatus() == beanSummaryReport.STATUS_PLGL?"selected" : ""%>>Approved By PL/GL</OPTION>
            <OPTION value="<%=beanSummaryReport.STATUS_QA%>" <%=beanSummaryReport.getStatus() == beanSummaryReport.STATUS_QA ? "selected" : ""%>>Approved By QA</OPTION>
            <OPTION value="<%=beanSummaryReport.STATUS_ALL%>" <%=beanSummaryReport.getStatus() == beanSummaryReport.STATUS_ALL ? "selected" : ""%>>All (except Rejected)</OPTION>
        </SELECT></TD>
        <TD align="left"><STRONG><FONT color="#ffffff" class="label1"></FONT></STRONG></TD>
    </TR>
    <TR>
        <TD></TD>
        <TD></TD>
        <TD></TD>
        <TD></TD>
        <TD></TD>
        <TD align="left"><INPUT type="button" name="Search"
            onclick="javascript:doViewEffort()" value="View Effort"
            class="Button"></TD>
        <TD align="right"></TD>
    </TR>
</TABLE>
<FONT color="#ffffff" class="label1">&nbsp;Date format:&nbsp;</FONT><FONT class="labelDate" color="yellow">&nbsp;(mm/dd/yy)</FONT>
<HR noshade size="1">
<BR>
<%
    StringMatrix smtReport = beanSummaryReport.getReportList();
    if (smtReport != null) {
        int row, col;
        int rpType = beanSummaryReport.getReportType();
        if (rpType == beanSummaryReport.REPORTTYPE_SUMMARY) {
%>
<TABLE align="center" border="0" cellpadding="1" cellspacing="1" bgcolor="#336699" width="60%">
    <TR>
        <TD class="TableHeader1" width="30%">Effort</TD>
        <TD class="TableHeader1" width="30%">Selected period (pd)</TD>
        <TD class="TableHeader1" width="30%">Up to current date (pd)</TD>
    </TR><%
            if (beanSummaryReport.getReportList() != null) {
                StringMatrix mtxReport = beanSummaryReport.getReportList();
                int maxrows = mtxReport.getNumberOfRows();
                try {
                    int i = maxrows;
                    while (i > 0) {
                        String sEffort = mtxReport.getCell(i - 1, 0);
                        if (sEffort.equals("0.0")) {
                            sEffort = "";
                        }
                        String sSelectedPeriod = mtxReport.getCell(i - 1, 1);
                        if (sSelectedPeriod.equals("0.0")
                                || sSelectedPeriod.equals("1.0E")) {
                            sSelectedPeriod = "";
                        }
                        else {
                            sSelectedPeriod += "0";
                            int nFloat = sSelectedPeriod.indexOf(".", 0);
                            sSelectedPeriod = sSelectedPeriod.substring(0, nFloat + 3);
                        }
                        String sUptoCurrentDate = mtxReport.getCell(i - 1, 2);
                        if (sUptoCurrentDate.equals("0.0")) {
                            sUptoCurrentDate = "" ;
                        }
                        else {
                            sUptoCurrentDate += "0";
                            int nFloat = sUptoCurrentDate.indexOf(".", 0);
                            sUptoCurrentDate = sUptoCurrentDate.substring(0, nFloat + 3);
                        }
                        String strClass = ((i % 2) == 1) ? "Row2" : "Row1";
                        if (i != 3) {
%>
    <TR class="<%=strClass%>">
        <TD width="10%">&nbsp;<%=sEffort%></TD>
        <TD width="10%" align="right"><%=sSelectedPeriod%></TD>
        <TD width="10%" align="right"><%=sUptoCurrentDate%></TD>
    </TR><%
                        }
                        else {
%>
    <TR class="<%=strClass%>">
        <TD width="10%">&nbsp;<B><%=sEffort%></B></TD>
        <TD width="10%" align="right"><B><%=sSelectedPeriod%></B></TD>
        <TD width="10%" align="right"><B><%=sUptoCurrentDate%></B></TD>
    </TR><%
                        }
                        i--;
                    }
                }
                catch (Exception ex) {
                }
            }
%>
</TABLE>
<TABLE align="center" border="0" cellpadding="1" cellspacing="1" width="60%">
    <TR>
        <TD><FONT color="#ffffff" style="FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 11px">(pd: person-day)</FONT></TD>
    </TR>
</TABLE><%
        }
        else if ((rpType == Timesheet.REPORTTYPE_PROCESS_TOW) ||
        		(rpType == Timesheet.REPORTTYPE_PRODUCT_TOW) ||
        		(rpType == Timesheet.REPORTTYPE_KPA_TOW) ||
        		(rpType == Timesheet.REPORTTYPE_ACCOUNT_DATE)) {
%>
<CENTER>
<DIV style="MARGIN: 0px; OVERFLOW-X: auto; PADDING-BOTTOM: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 0px; WIDTH: 95%" align="center">
<TABLE border="0" cellpadding="1" cellspacing="1" bgcolor="#336699" width="75%" align="center">
    <COLGROUP>
        <COL width="20%"><%
            long nColWidth = Math.round((1.0 / (smtReport.getNumberOfCols() + 1)) * 80);
            for (col = 1; col < smtReport.getNumberOfCols(); col++) {
                out.write("<COL width =\"" + nColWidth + "%\">");
            }
%>
    <TBODY><%
            // row 0..
            out.write("<TR>");
            // Name
            out.write("<TD align=\"center\" class = \"TableHeader1\" >" + smtReport.getCell(0, 0) + "</TD>");
            // TOWs
            String strPer = "";
            if (rpType == beanSummaryReport.REPORTTYPE_ACCOUNT_DATE) {
                strPer = "ph";
            }
            else {
                strPer = "pd";
            }
            for (col = 0x01; col < smtReport.getNumberOfCols(); col++) {
                out.write("<TD align=\"center\" class = \"TableHeader1\" >"
                        + smtReport.getCell(0, col) + " (" + strPer + ")</TD>");
            }
            out.write("</TR>");
            // row 1-n..
            String strClass = "";
            for (row = 1; row < smtReport.getNumberOfRows(); row++) {
                strClass = (row % 2 == 1) ? "Row2" : "Row1";
                out.write("<TR class = \"" + strClass + "\" >");
                for (col = 0x00; col < smtReport.getNumberOfCols(); col++) {
                    String sReport = smtReport.getCell(row, col);
                    if (sReport.equals("0.00") || sReport.equals("0")) {
                        sReport = "";
                    }
                    if (col == 0x00) {
                        out.write("<TD>"+ sReport + "</TD>");
                    }
                    else {
                        out.write("<TD align=\"right\" >" + sReport + "</TD>");
                    }
                }
                out.write("</TR>");
            }
%>
</TABLE>
<TABLE border="0" cellpadding="1" cellspacing="1" width="75%" align="center"><%
            if (rpType == beanSummaryReport.REPORTTYPE_ACCOUNT_DATE) {
%>
    <TR>
        <TD><FONT color="#ffffff" style="FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 11px">(ph: person-hour)</FONT></TD><%
            }
            else {
%>
    <TR>
        <TD><FONT color="#ffffff" style="FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 11px">(pd: person-day)</FONT></TD><%
            }
%>
    </TR>
</TABLE>
<BR>
</DIV>
</CENTER><%
        }
        else if ((rpType == Timesheet.REPORTTYPE_PROJECT_PROCESS) ||
        		(rpType == Timesheet.REPORTTYPE_PROJECT_PRODUCT) ||
        		(rpType == Timesheet.REPORTTYPE_PROJECT_KPA) ||
        		(rpType == Timesheet.REPORTTYPE_PROJECT_TOW)) {
%>
<CENTER>
<DIV style="MARGIN: 0px; OVERFLOW-X: auto; PADDING-BOTTOM: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 0px; WIDTH: 95%;" align="center">
<TABLE border="0" cellpadding="1" cellspacing="1" bgcolor="#336699" width="75%" align="center">
    <COLGROUP>
        <COL width="20%"><%
        	int N_PRECISION = 2;
        	ConstantRow rowConst;
        	ProjectPivotRow rowProject;
			ArrayList listReport = beanSummaryReport.getProjectSummary();
			ArrayList listConst = (ArrayList) listReport.get(0);
			ArrayList listRow = (ArrayList) listReport.get(1);
			long nColWidth = Math.round((1.0 / (listConst.size() + 3)) * 80);
			float f_sum[] = new float[listConst.size() + 2];	// Summary row
            for (col = 1; col < (listConst.size() + 3); col++) {
                out.write("<COL width=\""+ nColWidth + "%\">");
            }
%>
    <TBODY><%
            // Header row
            out.write("<TR>");
            out.write("<TD align=\"center\" class = \"TableHeader1\">Project</TD>");
            out.write("<TD align=\"center\" class = \"TableHeader1\">Total</TD>");
            for (col = 0; col < listConst.size(); col++) {
            	rowConst = (ConstantRow) listConst.get(col);
                out.write("<TD align=\"center\" class = \"TableHeader1\">" +
                		 rowConst.getTitle() + " (pd)</TD>");
            }
            out.write("<TD align=\"center\" class = \"TableHeader1\">Others</TD>");
            out.write("</TR>");
            //End: Header row
            
            // row 1-n..
            String strClass = "";
        	float f_Effort;
            for (row = 0; row < listRow.size(); row++) {
                strClass = (row % 2 == 0) ? "Row2" : "Row1";
                out.write("<TR class=\"" + strClass + "\">");
            	rowProject = (ProjectPivotRow) listRow.get(row);
            	out.write("<TD>" + rowProject.getProjectCode() + "</TD>");
            	if (rowProject.getTotal() == 0) {
            		out.write("<TD></TD>");
            	}
            	else {
	            	out.write("<TD align='right'><B>" + CommonUtil.roundFormat(rowProject.getTotal(), N_PRECISION) + "</B></TD>");
            	}
            	f_sum[0] += rowProject.getTotal();
                for (col = 0; col < listConst.size(); col++) {
                    f_Effort = rowProject.getArrEffort()[col];
                    f_sum[col + 1] += f_Effort;
                    if (f_Effort == 0.0) {
                        out.write("<TD></TD>");
                    }
                    else {
                        out.write("<TD align='right'>" + CommonUtil.roundFormat(f_Effort, N_PRECISION) + "</TD>");
                    }
                }
                // Row: Others
                f_Effort = rowProject.getOthers();
                f_sum[listConst.size() + 1] += f_Effort;
                if (f_Effort == 0.0) {
                    out.write("<TD></TD>");
                }
                else {
                    out.write("<TD align='right'>" + CommonUtil.roundFormat(f_Effort, N_PRECISION) + "</TD>");
                }
                out.write("</TR>");
            }
            
            // Summary row
            out.write("<TR class='" + ((row % 2 == 0) ? "Row2" : "Row1") + "'>");
    		out.write("<TD><B>Total</B></TD>");
            for (col = 0; col < f_sum.length; col++) {
            	if (f_sum[col] == 0.0) {
            		out.write("<TD></TD>");
            	}
            	else {
            		out.write("<TD align='right'><B>" + CommonUtil.roundFormat(f_sum[col], N_PRECISION) + "</B></TD>");
            	}
            }
            out.write("</TR>");
%>
</TABLE>
<TABLE border="0" cellpadding="1" cellspacing="1" width="75%" align="center">
    <TR>
        <TD><FONT color="#ffffff" style="FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 11px">(pd: person-day)</FONT></TD>
    </TR>
</TABLE>
<BR>
</DIV>
</CENTER><%
        }
        else if (rpType == Timesheet.REPORTTYPE_PROJECT_SUMMARY) {
%>
<CENTER>
<DIV style="MARGIN: 0px; OVERFLOW-X: auto; PADDING-BOTTOM: 0px; PADDING-LEFT: 0px; PADDING-RIGHT: 0px; PADDING-TOP: 0px; WIDTH: 95%" align="center">
<TABLE border="0" cellpadding="1" cellspacing="1" bgcolor="#336699" width="75%" align="center">
    <COLGROUP>
        <COL width="20%"><%
        	int nCols = 6;
        	int N_PRECISION = 2;
        	ConstantRow rowConst;
        	ProjectSummaryRow rowProject;
			ArrayList listRow = beanSummaryReport.getProjectSummary();
			long nColWidth = Math.round((1.0 / nCols) * 80);
			float f_sum[] = new float[nCols];	// Summary row
            for (col = 0; col < nCols; col++) {
                out.write("<COL width=\""+ nColWidth + "%\">");
            }
%>
    <TBODY><%
            // Header row
            out.write("<TR>");
            out.write("<TD align=\"center\" class = \"TableHeader1\">Project</TD>");
            out.write("<TD align=\"center\" class = \"TableHeader1\">Development (pd)</TD>");
            out.write("<TD align=\"center\" class = \"TableHeader1\">Quality (pd)</TD>");
            out.write("<TD align=\"center\" class = \"TableHeader1\">Management (pd)</TD>");
            out.write("<TD align=\"center\" class = \"TableHeader1\">Total (pd)</TD>");
            out.write("<TD align=\"center\" class = \"TableHeader1\">Translation (pd)</TD>");
            out.write("<TD align=\"center\" class = \"TableHeader1\">Correction (pd)</TD>");
            out.write("</TR>");
            //End: Header row
            
            // row 1-n..
            String strClass = "";
            for (row = 0; row < listRow.size(); row++) {
                strClass = (row % 2 == 0) ? "Row2" : "Row1";
                out.write("<TR class=\"" + strClass + "\">");
            	rowProject = (ProjectSummaryRow) listRow.get(row);
            	out.write("<TD>" + rowProject.getProjectCode() + "</TD>");
            	// Development effort
            	if (rowProject.getDevelopment() == 0) {
            		out.write("<TD></TD>");
            	}
            	else {
	            	out.write("<TD align='right'>" + CommonUtil.roundFormat(rowProject.getDevelopment(), N_PRECISION) + "</TD>");
            	}
            	f_sum[0] += rowProject.getDevelopment();
            	
            	// Quality effort
            	if (rowProject.getQuality() == 0) {
            		out.write("<TD></TD>");
            	}
            	else {
	            	out.write("<TD align='right'>" + CommonUtil.roundFormat(rowProject.getQuality(), N_PRECISION) + "</TD>");
            	}
            	f_sum[1] += rowProject.getQuality();
            	
            	// Management effort
            	if (rowProject.getManagement() == 0) {
            		out.write("<TD></TD>");
            	}
            	else {
	            	out.write("<TD align='right'>" + CommonUtil.roundFormat(rowProject.getManagement(), N_PRECISION) + "</TD>");
            	}
            	f_sum[2] += rowProject.getManagement();
            	
            	// Total = Development + Quality + Management
            	if (rowProject.getTotal() == 0) {
            		out.write("<TD></TD>");
            	}
            	else {
	            	out.write("<TD align='right'><B>" + CommonUtil.roundFormat(rowProject.getTotal(), N_PRECISION) + "</B></TD>");
            	}
            	f_sum[3] += rowProject.getTotal();
            	
            	// Translation effort
            	if (rowProject.getTranslation() == 0) {
            		out.write("<TD></TD>");
            	}
            	else {
	            	out.write("<TD align='right'>" + CommonUtil.roundFormat(rowProject.getTranslation(), N_PRECISION) + "</TD>");
            	}
            	f_sum[4] += rowProject.getTranslation();
            	
            	// Correction effort
            	if (rowProject.getCorrection() == 0) {
            		out.write("<TD></TD>");
            	}
            	else {
	            	out.write("<TD align='right'>" + CommonUtil.roundFormat(rowProject.getCorrection(), N_PRECISION) + "</TD>");
            	}
            	f_sum[5] += rowProject.getCorrection();
                out.write("</TR>");
            }
            
            // Summary row
            out.write("<TR class='" + ((row % 2 == 0) ? "Row2" : "Row1") + "'>");
    		out.write("<TD><B>Total</B></TD>");
            for (col = 0; col < f_sum.length; col++) {
            	if (f_sum[col] == 0.0) {
            		out.write("<TD></TD>");
            	}
            	else {
            		out.write("<TD align='right'><B>" + CommonUtil.roundFormat(f_sum[col], N_PRECISION) + "</B></TD>");
            	}
            }
            out.write("</TR>");
%>
</TABLE>
<TABLE border="0" cellpadding="1" cellspacing="1" width="75%" align="center">
    <TR>
        <TD><FONT color="#ffffff" style="FONT-FAMILY: Verdana, sans-serif; FONT-SIZE: 11px">(pd: person-day)</FONT></TD>
    </TR>
</TABLE>
<BR>
</DIV>
</CENTER><%
        }
    }
%>
</DIV>
</FORM>
<SCRIPT language="javascript">
if (document.forms[0].srcboProject.value == 0) {
   document.forms[0].srptProjectType.disabled = false;
}
else {
   document.forms[0].srptProjectType.disabled = true;
   document.forms[0].srptProjectType.value = 10;
}
<%
   out.write("var P_COUNT=" + (smtProjectList.getNumberOfRows() - 1) + ";");
   out.write("\nvar prj = new Array(P_COUNT);");
   for (int nRow = 1; nRow < smtProjectList.getNumberOfRows(); nRow++) {
       // Project_ID, Code, Group, Status:
       out.write("\nprj[" + (nRow - 1) + "]=new Array(4);");
       out.write("prj[" + (nRow - 1) + "][0]=" + smtProjectList.getCell(nRow, 0) + ";");
       out.write("prj[" + (nRow - 1) + "][1]='" + smtProjectList.getCell(nRow, 1) + "';");
       out.write("prj[" + (nRow - 1) + "][2]='" + smtProjectList.getCell(nRow, 3) + "';");
       out.write("prj[" + (nRow - 1) + "][3]='" + smtProjectList.getCell(nRow, 4) + "';");
   }
%>

function changeCombo() {
   var myForm = document.forms[0];
   var myElement;
   for (var i = myForm.srcboProject.options.length - 1; i >= 0; i--) {
       myForm.srcboProject.options[i] = null;
   }

   var bGroupAll = false;
   var bStatusAll = false;
   if (myForm.srptGroup.value == "All") {
      bGroupAll = true;
   }
   if (myForm.srptProjectStatus.value == <%=fpt.timesheet.constant.Timesheet.PROJECT_STATUS_ALL%>) {
      bStatusAll = true;
   }

   myElement = document.createElement("option");
   myElement.value = 0;
   myElement.text = "All projects";
   myForm.srcboProject.add(myElement);

   var bAddThis = false;
   myForm.hidProjectsList.value = "";
   for (i = 0; i < P_COUNT; i++) {
       bAddThis = false;
       if (bGroupAll && bStatusAll) {
          bAddThis = true;
       }
       else if (bGroupAll) {
          if (myForm.srptProjectStatus.value == prj[i][3]) {
             bAddThis = true;
          }
       }
       else if (bStatusAll) {
          if (myForm.srptGroup.value == prj[i][2]) {
             bAddThis = true;
          }
       }
       else {
          if ((myForm.srptGroup.value == prj[i][2]) && (myForm.srptProjectStatus.value == prj[i][3])) {
               bAddThis = true;
          }
       }

       if (bAddThis) {
           myElement = document.createElement("option");
           myElement.value = prj[i][0];
           myElement.text = prj[i][1];
           myForm.srcboProject.add(myElement);
           if (myElement.value == '<%=beanSummaryReport.getProject()%>') {
               myElement.selected =true;
           }
           myForm.hidProjectsList.value += prj[i][0] + ",";
        }
    }
    myForm.hidProjectsList.value += "0";

    if (myForm.srcboProject.options.length == 1) {
        myForm.srcboProject.options[0].selected = true;
    }
}

function doViewEffort() {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    form.hidAction.value = "RA";
    form.hidActionDetail.value = "SummaryReport";
    form.action = "TimesheetServlet";
    form.submit();
}

/*function changeProjectStatus() {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    form.hidAction.value = "RA";
    form.hidActionDetail.value = "SummaryReport";
    form.action = "TimesheetServlet";
    form.srcboProject.value = 0;
    form.submit();
}*/

function checkPro() {
    if (document.forms[0].srcboProject.value == 0) {
        document.forms[0].srptProjectType.disabled = false;
    }
    else {
        document.forms[0].srptProjectType.disabled = true;
        document.forms[0].srptProjectType.value = 10;
    }
}

function isValidForm() {
    var count;
    var form = document.forms[0];

  	if (form.FromDate.value.length > 0 ) {
		if (isValidate(form.FromDate.value)==false) {
			form.FromDate.focus();
			return false;
		}
  	}
	if (form.ToDate.value.length > 0 ) {
	    if (isValidate(form.ToDate.value)==false) {
    		form.ToDate.focus();
    		return false;
    	}
	}
    if ((form.FromDate.value.length > 0) && (form.ToDate.value.length > 0)) {
        if (compareDate(form.FromDate , form.ToDate) > 0) {
            alert("From Date must lower or equal To Date");
            form.FromDate.focus();
            return false
        }
    }
    //Account and date report
    if (form.ReportType.value == 4) {
        if (form.FromDate.value.length <= 0) {
            alert("Please enter From Date");
            form.FromDate.focus();
            return false;
        }
        if (form.ToDate.value.length <= 0) {
            alert("Please enter To Date");
            form.ToDate.focus();
            return false;
        }
        if (daysBetween(form.FromDate.value, form.ToDate.value) > 30) {
            alert("Days between From Date and To Date must lower than 31 days");
            form.FromDate.focus();
            return false;
        }
    }
    return true;
}

function setKeypress() {
    if (window.event.keyCode==13) doViewEffort();
}
</SCRIPT>
<SCRIPT>
changeCombo();
</SCRIPT>
</BODY>
</HTML>