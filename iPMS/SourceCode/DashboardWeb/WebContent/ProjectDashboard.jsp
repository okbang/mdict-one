<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*,
            fpt.dashboard.constant.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ProjectDashboardBean beanProjectDashboard
            = (ProjectDashboardBean)request.getAttribute("beanProjectDashboard");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Project Dashboard</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="styles/StyleSheet.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
function doSearch() {
    var form = document.frmProjectDashboard;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewDashboard";
    form.action = "DashboardServlet";
    form.submit();
}

function doProjectDetail(id) {
    var form = document.frmProjectDashboard;
    form.hidProjectID.value = id;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewProjectDetail";
    form.action = "DashboardServlet";
    form.submit();
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<%
    String strTitle = "Project Dashboard";
%>
<%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmProjectDashboard">
<INPUT type="hidden" name="hidAction" value=""> <INPUT type="hidden"
    name="hidActionDetail" value=""> <INPUT type="hidden"
    name="hidProjectID" value=""> <INPUT type="hidden" name="hidTypeOfView"
    value="ViewDashboard">
<TABLE border="0" cellspacing="1">
    <TR>
        <TD colspan="12" align="left"><!-- GROUP COMBO -->
        <TABLE border="0" cellspacing="0" width="70%">
            <TR>
                <TD><B><FONT color="#000080">&nbsp;&nbsp;Group&nbsp;&nbsp;</FONT></B></TD>
                <%
    String strGroup = beanProjectDashboard.getGroupName();
    String strDisabled = "";
    // Disable external viewers from see other group's projects
    if (("4".equals(beanUserInfo.getRole())) || ("5".equals(beanUserInfo.getRole())) ||
        (DATA.ROLE_GROUPLEADER.equals(beanUserInfo.getSRole())))
    {
        strDisabled = "disabled";
    }
%>
                <TD><SELECT style="width: 70" name="cboGroup" <%=strDisabled%>>
                    <%
    for (int nRow = 0x00; nRow < beanProjectDashboard.getGroupList().getNumberOfRows(); nRow++) {
        String strText = beanProjectDashboard.getGroupList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectDashboard.getGroupName() != null) {
            out.write(beanProjectDashboard.getGroupName().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">" + strText + "</OPTION>");
    }
%>
                </SELECT> <!-- incase disabling Group combo, it will make request.getParameter() return a NULL value -->
                <INPUT type="hidden" name="hidGroup" value="<%=strGroup%>"><!-- don't delete --></TD>

                <!-- ThaiLH -->

                <TD class="required_left_cell_bkgrnd" align="left" valign="middle"
                    width="20%"><B><FONT color="#000080">&nbsp;&nbsp;Type&nbsp;&nbsp;</FONT></B>
                <SELECT name="cboType" size="1" style="width: 80">
<%
    for (int nRow = 0x00; nRow < beanProjectDashboard.getTypeList().getNumberOfRows(); nRow++) {
        String strText = beanProjectDashboard.getTypeList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectDashboard.getType() != null) {
            out.write(beanProjectDashboard.getType().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">"
                + beanProjectDashboard.getTypeList().getCell(nRow, 1) + "</OPTION>");
    }
%>
                </SELECT></TD>

                <!-- ThaiLH -->
                <TD class="required_left_cell_bkgrnd" align="left" valign="middle"
                    width="20%"><B><FONT color="#000080">&nbsp;&nbsp;Status&nbsp;&nbsp;</FONT></B>
                <SELECT name="cboStatus" size="1" style="width: 90">
                    <%
    String selectedValue = beanProjectDashboard.getSelectStatus();
    String selectedDisplay = "";
    String tValue = "";
    String tDisplay = "";
    StringMatrix mtxStatus = beanProjectDashboard.getStatusList();
    int maxrows = mtxStatus.getNumberOfRows();
    int ik = 0;
    while (ik < maxrows) {
        tValue = mtxStatus.getCell(ik, 0);
        tDisplay = mtxStatus.getCell(ik, 1);
        if (!(tValue.equalsIgnoreCase(selectedValue))) {
%>
                    <OPTION value="<%=tValue%>"><%=tDisplay%></OPTION>
                    <%
        }
        else {
            selectedDisplay = tDisplay;
        }
        ik++;
    }
    if (!(selectedValue.equalsIgnoreCase("all"))) {
%>
                    <OPTION selected value="<%=selectedValue%>"><%=selectedDisplay%></OPTION>
                    <%
    }
%>
                </SELECT></TD>
                <TD class="required_left_cell_bkgrnd" align="left" valign="middle"
            width="20%"><B><FONT color="#000080">&nbsp;&nbsp;Category&nbsp;&nbsp;</FONT></B>
        <SELECT name="cboCategory" size="1" style="width: 100">
            <%
    for (int nRow = 0x00; nRow < beanProjectDashboard.getCategoryList().getNumberOfRows(); nRow++) {
        String strText = beanProjectDashboard.getCategoryList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectDashboard.getCategory() != null) {
            out.write(beanProjectDashboard.getCategory().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">"
                + beanProjectDashboard.getCategoryList().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>

                <TD><B><FONT color="#000080">&nbsp;&nbsp;Sort&nbsp;by&nbsp;&nbsp;</FONT></B></TD>
                <TD><SELECT style="width: 100" name="cboOrder">
                    <%
    for (int nRow = 0x00; nRow < beanProjectDashboard.getOrderList().getNumberOfRows(); nRow++) {
        String strText = beanProjectDashboard.getOrderList().getCell(nRow, 0);
        out.write("<OPTION ");
        out.write(String.valueOf(beanProjectDashboard.getOrderBy()).equals(strText) ? " selected " : " ");
        out.write("value=\"" + strText + "\">" + beanProjectDashboard.getOrderList().getCell(nRow, 1) + "</OPTION>");
    }
%>
                </SELECT></TD>
                <TD><INPUT type="button" name="DoSearchProject"
                    onclick="javascript:doSearch()" value="Search"></TD>
            </TR>
        </TABLE>
        </TD>
    </TR>
    <!-- PROJECT LIST -->
    <TR class="table_header">
        <!-- TABLE HEADER -->
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Code</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Project Name</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Complete</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Schedule Status</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Effort Status</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Start Date</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Planned Finish</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Replanned Finish</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Actual Finish</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Planned Effort</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Replanned Effort</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN
            style="color: white">Actual Effort</SPAN></FONT></B></TD>
    </TR>
    <%
    int nNumberOfRows = beanProjectDashboard.getProjectDashboardList().getNumberOfRows();
    if (nNumberOfRows > 0) {
        for (int i = 0; i < nNumberOfRows; i++) {
%>
    <TR class="Row<%=(i + 1) % 2 + 1%>">
        <!-- CODE -->
        <TD><FONT face="Verdana" size="1"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 1)%></FONT></TD>
        <!-- NAME -->
        <TD><FONT face="Verdana" size="1"> <A
            href="javascript:doProjectDetail('<%=beanProjectDashboard.getProjectDashboardList().getCell(i, 0)%>')"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 2)%></A></FONT></TD>
        <!-- COMPLETE -->
        <TD><FONT face="Verdana" size="1"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 3)%></FONT></TD>
        <!-- SCHEDULE STATUS -->
        <TD align="center"><FONT face="Verdana" size="1"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 4)%></FONT></TD>
        <!-- EFFORT STATUS -->
        <TD align="center"><FONT face="Verdana" size="1"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 5)%></FONT></TD>
        <!-- START DATE -->
        <TD><FONT face="Verdana" size="1"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 6)%></FONT></TD>
        <!-- BASE FINISH DATE -->
        <TD><FONT face="Verdana" size="1"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 7)%></FONT></TD>
        <!-- PLAN FINISH DATE -->
        <TD><FONT face="Verdana" size="1"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 8)%></FONT></TD>
        <!-- ACTUAL FINISH DATE -->
        <TD><FONT face="Verdana" size="1"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 9)%></FONT></TD>
        <!-- BASE EFFORT -->
        <TD><FONT face="Verdana" size="1"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 10)%></FONT></TD>
        <!-- PLAN EFFORT -->
        <TD><FONT face="Verdana" size="1"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 11)%></FONT></TD>
        <!-- ACTUAL EFFORT -->
        <TD><FONT face="Verdana" size="1"><%=beanProjectDashboard.getProjectDashboardList().getCell(i, 12)%></FONT></TD>
    </TR>
    <%
        } //end for
    }  //end if
    else {
 %>
    <TR>
        <TD colspan="14">No records found</TD>
    </TR>
    <%
    } //end else
%>
</TABLE>
<BR>
</FORM>
</BODY>
</HTML>
