<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*, fpt.dashboard.constant.DATA" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ProjectDetailBean beanProjectDetail
            = (ProjectDetailBean)request.getAttribute("beanProjectDetail");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Project Detail Information</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/StyleSheet.css">
</HEAD>
<BODY>
<DIV align="left"><%!
    String strTitle = "Project Detail Information";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmProjectDetail">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidTypeOfView" value="<%=beanUserInfo.getTypeOfView()%>">
<INPUT type="hidden" name="hidProjectID" value="<%=beanProjectDetail.getId()%>"><%
    String pId;
    String dCode;
    String dName;

    String dPerComplete;
    String dType;
    String dGroup;

    String dScheduleStatus;
    String dEffortStatus;
    String dDescription;

    String dLeader;
    String dPlanStartDate;
    String dStartDate;
    String dFinishDate;
    String strActualFinishDate;

    int dPlanDuration;
    int dBaseDuration;
    int dActualDuration;
    String dSVariance;

    String dPlanEffort;
    String dBaseEffort;
    String dActualEffort;
    String dEVariance;

    String dTotalDefect;
    String dTotalWeightedDefect;
    String dTotalRequirement;
    String dStatus;
    String dLastUpdate;

    pId = beanProjectDetail.getId();
    dCode = beanProjectDetail.getCode();
    dName = beanProjectDetail.getName();
    dLeader = beanProjectDetail.getLeader();

    dType = beanProjectDetail.getType();
    dGroup = beanProjectDetail.getGroup();
    dPerComplete = beanProjectDetail.getPerComplete();
	dPlanStartDate = beanProjectDetail.getPlanStart();
	dStartDate = beanProjectDetail.getStartDate();
	if ("".equals(beanProjectDetail.getPlanFinishDate())){
		dFinishDate = beanProjectDetail.getBaseFinishDate();
	}
	else {
		dFinishDate = beanProjectDetail.getPlanFinishDate();
	}
	strActualFinishDate = beanProjectDetail.getActualFinishDate();
    dDescription = beanProjectDetail.getDescription();
    dPlanDuration = beanProjectDetail.getPlanDuration();
    dBaseDuration = beanProjectDetail.getBaseDuration();
    dActualDuration = beanProjectDetail.getActualDuration();

    dSVariance = beanProjectDetail.getScheduleVariance();
    dScheduleStatus = beanProjectDetail.getScheduleStatus();

    dPlanEffort = beanProjectDetail.getPlanEffort();
    dBaseEffort = beanProjectDetail.getBaseEffort();
    dActualEffort = beanProjectDetail.getActualEffort();

    dEVariance = beanProjectDetail.getEffortVariance();
    dEffortStatus = beanProjectDetail.getEffortStatus();

    dTotalDefect = beanProjectDetail.getTotalDefect();
    dTotalWeightedDefect = beanProjectDetail.getTotalWeightedDefect();
    dTotalRequirement = beanProjectDetail.getTotalRequirement();
    dStatus = beanProjectDetail.getStatus();
    dLastUpdate = beanProjectDetail.getLastUpdate();
    String customer = beanProjectDetail.getCustomer();
    String cate = beanProjectDetail.getCate();

    String strTotalPending = beanProjectDetail.getTotalPending();
%>
<TABLE border="0" cellpadding="2" cellspacing="0" width="100%">
    <TBODY>
        <TR>
            <TD bgcolor="#DDDDFF" width="80%" height="25"><B><FONT color="#006699">&nbsp;Major Information &nbsp;</FONT></B></TD>
            <TD bgcolor="#DDDDFF" width="20%" height="25" align="right"><B><FONT color="#006699">&nbsp;Last Update: &nbsp;</FONT></B><%=dLastUpdate%></TD>
        </TR>
        <TR>
            <TD width="100%" colspan="2">
            <TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
                <TBODY>
                    <TR>
                        <TD width="30%" height="20"><B>&nbsp;Name:</B>&nbsp;<%=dName%></TD>
                        <TD width="20%" height="20"><B>&nbsp;Status:</B>&nbsp;<%=dStatus%></TD>
                        <TD width="25%" height="20"><B>&nbsp;Type:</B>&nbsp;<%=dType%></TD>
                        <TD width="25%" height="20"><B>&nbsp;Group:</B>&nbsp;<%=dGroup%></TD>
                    </TR>
                    <TR>
                        <TD width="30%" height="20"><B>&nbsp;Customer:</B>&nbsp;<%=customer%></TD>
                        <TD width="20%" height="20"><B>&nbsp;Category:</B>&nbsp;<%=cate%></TD>
                        <TD width="20%" height="20"><B>&nbsp;Schedule Status:</B>&nbsp;<%=dScheduleStatus%></TD>
                        <TD width="25%" height="20"><B>&nbsp;Effort Status:</B>&nbsp;<%=dEffortStatus%></TD>
                    </TR>
                    <TR>
                        <TD width="30%" height="20"><B>&nbsp;Code:</B>&nbsp;<%=dCode%></TD>
                        <TD width="20%" height="20"><B>&nbsp;Plan Start Date:</B>&nbsp;<%=dPlanStartDate%></TD>
                        <TD width="25%" height="20"><B>&nbsp;Plan Finish Date:</B>&nbsp;<%=dFinishDate%></TD>
                        <TD width="25%" height="20">&nbsp;<B>Project database:&nbsp;&nbsp;&nbsp;</B><A href="javascript:dProjectDatabase(<%=pId%>)">View detail</A></TD>
                    </TR>
                    <TR>
                        <TD width="30%" height="20"><B>&nbsp;Leader:</B>&nbsp;<%=dLeader%></TD>
                        <TD width="20%" height="20"><B>&nbsp;Actual Start Date:&nbsp;</B><%=dStartDate%></TD>
                        <TD width="25%" height="20"><B>&nbsp;Actual Finish Date:</B>&nbsp;<%=strActualFinishDate%></TD>
                        <TD width="25%" height="20">&nbsp;<B>Per Complete:&nbsp;</B><%=dPerComplete%> %</TD>
                    </TR>
                </TBODY>
            </TABLE>
            </TD>
        </TR>
        <TR>
            <TD width="100%" colspan="2">
            <TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
                <TBODY>
                    <TR>
                        <TD width="15%" height="20">&nbsp;<B>Planned Duration</B></TD>
                        <TD width="15%" height="20">&nbsp;<%=(dBaseDuration > 0 ? (dBaseDuration + " d") : "")%></TD>
                        <TD width="20%" height="20">&nbsp;<B>Replanned Duration</B></TD>
                        <TD width="25%" height="20">&nbsp;<%=(dPlanDuration > 0 ? (dPlanDuration + " d") : "")%></TD>
                        <TD width="15%" height="20">&nbsp;<B>Actual Duration</B></TD>
                        <TD width="10%" height="20">&nbsp;<%=(dActualDuration > 0 ? (dActualDuration + " d") : "")%></TD>
                    </TR>
                    <TR>
                        <TD width="15%" height="20">&nbsp;<B>Planned Effort</B></TD>
                        <TD width="15%" height="20">&nbsp;<%=(!"".equals(dBaseEffort) ? (dBaseEffort + " d") : "")%></TD>
                        <TD width="20%" height="20">&nbsp;<B>Replanned Effort</B></TD>
                        <TD width="25%" height="20">&nbsp;<%=(!"".equals(dPlanEffort) ? (dPlanEffort + " d") : "")%></TD>
                        <TD width="15%" height="20">&nbsp;<B>Actual Effort</B></TD>
                        <TD width="10%" height="20">&nbsp;<%=(!"".equals(dActualEffort) ? (dActualEffort + " d") : "")%></TD>
                    </TR>
                    <TR>
                        <TD width="15%" height="20">&nbsp;<B>Pending Defect</B></TD>
                        <TD width="15%" height="20">&nbsp;<%=strTotalPending%></TD>
                        <TD width="20%" height="20"><B>&nbsp;Total Weighted Defect</B></TD>
                        <TD width="25%" height="20">&nbsp;<A href="javascript:doDChart('<%=beanProjectDetail.getDefectChartDataQueryList()%>')"><%=dTotalWeightedDefect%></A></TD>
                        <TD width="15%" height="20"><B>&nbsp;Total Requirement</B></TD>
                        <TD width="10%" height="20">&nbsp;<A href="javascript:doRChart('<%=beanProjectDetail.getRequirementChartDataQueryList()%>')"><%=dTotalRequirement%></A></TD>
                    </TR>
                </TBODY>
            </TABLE>
            </TD>
        </TR>
</TABLE><%
    int i = 0;
    int mlen = 0;
    String mLine = "";
    String rClass = "";
%>
<TABLE border="0" cellpadding="2" cellspacing="0" width="100%">
    <TR>
        <TD bgcolor="#DDDDFF" width="100%" height="30"><B><FONT color="#006699">&nbsp;Milestones&nbsp;</FONT></B></TD>
    </TR>
    <TR>
        <TD width="100%">
        <TABLE border="0" cellspacing="1" width="100%" cellpadding="0">
            <TBODY>
                <TR class="table_header">
                    <TD width="40%" align="left" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Major Milestones</B></FONT></TD>
                    <TD width="15%" align="center" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Complete</B></FONT></TD>
                    <TD width="15%" align="center" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Planned Finish</B></FONT></TD>
                    <TD width="15%" align="center" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Replanned Finish</B></FONT></TD>
                    <TD width="15%" align="center" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Actual Finish</B></FONT></TD>
                </TR><%
    String mName = "";
    String mComplete = "";
    String mfBdate = "";
    String mfPdate = "";
    String mfAdate = "";
    Collection mList = beanProjectDetail.getMilestoneList();
    Iterator itM = mList.iterator();

    while (itM.hasNext()) {
        mLine = (String)itM.next().toString();
        mlen = mLine.length();
        mfAdate = mLine.substring(mlen - 8);
        mfPdate = mLine.substring(mlen - 16, mlen - 8);
        mfBdate = mLine.substring(mlen - 24, mlen - 16);
        mComplete = mLine.substring(mlen - 25, mlen - 24);
        if (mfBdate.equals("00000000")) {
            mfBdate = "";
        }
        if (mfPdate.equals("00000000")) {
            mfPdate = "";
        }
        if (mfAdate.equals("00000000")) {
            mfAdate = "";
        }
        if (mComplete.equals("0")) {
            mComplete = "No";
        }
        else {
            mComplete = "Yes";
        }
        mName = mLine.substring(0, mlen - 25);
        if (i % 2 == 0 ) {
            rClass = "row2";
        }
        else {
            rClass = "row1";
        }
        i++;
%>
                <TR class="<%=rClass%>">
                    <TD width="40%" align="left">&nbsp;&nbsp;<%=mName%></TD>
                    <TD width="15%" align="center"><%=mComplete%></TD>
                    <TD width="15%" align="center"><%=mfBdate%></TD>
                    <TD width="15%" align="center"><%=mfPdate%></TD>
                    <TD width="15%" align="center"><%=mfAdate%></TD>
                </TR><%
    }
%>
            </TBODY>
        </TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="2" cellspacing="0" width="100%">
    <TR>
        <TD bgcolor="#DDDDFF" width="100%" height="25"><B><FONT color="#006699">&nbsp;Project Assignment&nbsp;</FONT></B></TD>
    </TR>
    <TR>
        <TD width="100%">
        <TABLE border="0" cellpadding="0" cellspacing="1" width="100%">
            <TBODY>
                <TR class="table_header">
                    <TD width="25%" align="left" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Name</B></FONT></TD>
                    <TD width="15%" align="center" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Join Date</B></FONT></TD>
                    <TD width="15%" align="center" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Release Date</B></FONT></TD>
                    <TD width="15%" align="center" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Type</B></FONT></TD>
                    <TD width="15%" align="center" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Usage(%)</B></FONT></TD>
                    <TD width="15%" align="center" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Responsibility</B></FONT></TD>
                </TR><%
    StringTokenizer stkLineA;
    String aName = "";
    String aValue = "";
    char ttA = 2;
    String strDiffA = String.valueOf(ttA);
    String strTempA = "";
    i = 0;
    String txtName = "";
    String txtBdate = "";
    String txtEdate = "";
    String txtType = "";
    String txtUsage = "";
    String txtres = "";
    Collection aList = beanProjectDetail.getAssignmentList();
    Iterator itA = aList.iterator();
    while (itA.hasNext()) {
        mLine = (String)itA.next().toString();
        stkLineA = new StringTokenizer(mLine, strDiffA);
        if (stkLineA.hasMoreElements()) {
            strTempA = (String)stkLineA.nextElement();
            if (strTempA != null) {
                if (strTempA.length() > 0) {
                    txtName = strTempA;
                }
            }
        }
        if (stkLineA.hasMoreElements()) {
            strTempA = (String)stkLineA.nextElement();
            if (strTempA != null) {
                if (strTempA.length() > 0) {
                    if(strTempA.equals("3")) {
                        txtType = "Tentative";
                    }
                    else {
                        if(strTempA.equals("2")) {
                            txtType = "Allocated";
                        }
                    }
                }
            }
        }
        if (stkLineA.hasMoreElements()) {
            strTempA = (String)stkLineA.nextElement();
            if (strTempA != null) {
                if (strTempA.length() > 0) {
                    txtUsage = strTempA;
                }
            }
        }
        if (stkLineA.hasMoreElements()) {
            strTempA = (String)stkLineA.nextElement();
            if (strTempA != null) {
                if (strTempA.length() > 0) {
                    txtres = strTempA;
                }
            }
        }
        if (stkLineA.hasMoreElements()) {
            strTempA = (String)stkLineA.nextElement();
            if (strTempA != null) {
                if (strTempA.length() > 0) {
                    txtBdate = strTempA;
                }
            }
        }
        if (stkLineA.hasMoreElements()) {
            strTempA = (String)stkLineA.nextElement();
            if (strTempA != null) {
                if (strTempA.length() > 0) {
                    txtEdate = strTempA;
                }
            }
        }
        if (i % 2 == 0) {
            rClass ="row2";
        }
        else {
            rClass ="row1";
        }
        i++;
%>
                <TR class="<%=rClass%>">
                    <TD width="25%" align="left">&nbsp;&nbsp;<%=txtName%></TD>
                    <TD width="15%" align="center"><%=txtBdate%></TD>
                    <TD width="15%" align="center"><%=txtEdate%></TD>
                    <TD width="15%" align="center"><%=txtType%></TD>
                    <TD width="15%" align="center"><%=txtUsage%></TD>
                    <TD width="15%" align="center"><%=txtres%></TD>
                </TR><%
    } //~ while (itA.hasNext())
%>
        </TABLE>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="2" cellspacing="0" width="100%">
    <TR>
        <TD bgcolor="#DDDDFF" width="100%" height="25"><B><FONT color="#006699">&nbsp;Issue&nbsp;</FONT></B></TD>
    </TR>
    <TR>
        <TD width="100%">
        <TABLE border="0" cellspacing="1" width="100%" cellpadding="0">
            <TBODY>
                <TR class="table_header">
                    <TD width="70%" align="left" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Description</B></FONT></TD>
                    <TD width="15%" align="center" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Start Date</B></FONT></TD>
                    <TD width="15%" align="center" bgcolor="#006699"><FONT color="#FFFFFF" size="1"><B>&nbsp;Due Date</B></FONT></TD>
                </TR><%
    StringTokenizer stkLine;
    String cName = "";
    String cValue = "";
    char tt = 2;
    String strDiff = String.valueOf(tt);
    String strTemp = "";
    i = 0;
    String iId = "";
    String iDescription = "";
    String iSdate = "";
    String iEdate = "";
    Collection iList = beanProjectDetail.getIssueList();
    Iterator itI = iList.iterator();
    while (itI.hasNext()) {
        mLine = (String)itI.next().toString();
        stkLine = new StringTokenizer(mLine, strDiff);
        if (stkLine.hasMoreElements()) {
            strTemp = (String)stkLine.nextElement();
            if (strTemp != null) {
                if (strTemp.length() > 0) {
                    iId = strTemp;
                }
            }
        }
        if (stkLine.hasMoreElements()) {
            strTemp = (String)stkLine.nextElement();
            if (strTemp != null) {
                if (strTemp.length() > 0) {
                    iDescription = strTemp;
                }
            }
        }
        if (stkLine.hasMoreElements()) {
            strTemp = (String)stkLine.nextElement();
            if (strTemp != null) {
                if (strTemp.length() > 0) {
                    iSdate = strTemp;
                }
            }
        }
        if (stkLine.hasMoreElements()) {
            strTemp = (String)stkLine.nextElement();
            if (strTemp != null) {
                if (strTemp.length() > 0) {
                    iEdate = strTemp;
                }
            }
        }
        if (i % 2 == 0) {
            rClass ="row2";
        }
        else {
            rClass ="row1";
        }
        i++;
%>
                <TR class="<%=rClass%>">
                    <TD width="70%" align="left">&nbsp;&nbsp;<%=iDescription%></TD>
                    <TD width="15%" align="center"><%=iSdate%></TD>
                    <TD width="15%" align="center"><%=iEdate%></TD>
                </TR><%
    } //end while
%>
        </TABLE>
        </TD>
    </TR>
</TABLE>
<BR>
<INPUT type="hidden" name="hidProjectStatus" value="<%=dStatus%>">
<INPUT type="hidden" name="hidDescription" value="<%=dDescription%>">
<P align="center">
<INPUT type="button" name="DoBackFromProjectDetail" onclick="javascript:doBack()" value="Back">
</P>
</FORM>
<BR>
</DIV>
<SCRIPT language="javascript">
	function doBack() {
	    var form = document.frmProjectDetail;
	    form.hidAction.value = "PM";
	    if (form.hidTypeOfView.value == "ViewProjectResource") {
	        form.hidAction.value = "RM";
	    }
	    form.hidActionDetail.value = form.hidTypeOfView.value;
	    form.action = "DashboardServlet";
	    form.submit();
	}
	
	function doRChart(str) {
	    var qrStr = "ProjectRequirementChart.jsp?" + str;
	    var sFeature = "width = 580 height = 280 top = 200 status=no  menu=no";
	    var requirementchart_wd = window.open(qrStr, "RequirementChart", sFeature);
	}
	
	function doDChart(str) {
	    var qrStr = "ProjectDefectChart.jsp?" + str;
	    var sFeature = "width = 580 height = 280 top = 200 status=no  menu=no";
	    var requirementchart_wd = window.open(qrStr, "RequirementChart", sFeature);
	}
	function dProjectDatabase(str) {
	    var qrStr = "ExcelFile.jsp?ProjectID=" + str;
	    var sFeature = "width = 780 height = 550 top = 0 left=0";
	    var requirementchart_wd = window.open(qrStr, "Projectdatabase", sFeature);
	}
</SCRIPT>
</BODY>
</HTML>