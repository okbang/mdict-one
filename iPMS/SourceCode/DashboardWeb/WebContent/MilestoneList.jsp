<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*" %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    MilestoneListBean beanMilestoneList
            = (MilestoneListBean)request.getAttribute("beanMilestoneList");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Project Milestone</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/StyleSheet.css">
<SCRIPT language="javascript">
function doAdd() {
    var form = document.frmMilestoneList;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "AddMilestone";
    form.action = "DashboardServlet";
    form.submit();
}

function doDelete() {
    var form = document.frmMilestoneList;
    if (checkValid()) {
        if (confirm("Do you want to delete selected milestone(s), continue?")) {
            form.hidAction.value = "PM";
            form.hidActionDetail.value = "DeleteMilestone";
            form.action = "DashboardServlet";
            form.submit();
        }
    }
}

function doUpdate(id) {
    var form = document.frmMilestoneList;
    form.hidMilestoneID.value = id;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "UpdateMilestone";
    form.action = "DashboardServlet";
    form.submit();
}

function doBack() {
    var form = document.frmMilestoneList;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewProjectDetail";
    form.action = "DashboardServlet";
    form.submit();
}

function checkValid() {
    var bChecked = false;
    for (var i = 0; i < document.forms[0].elements.length; i++) {
        var e = document.forms[0].elements[i];
        if (e.name == "selected" && e.type == "checkbox") {
            if (e.checked == 1) {
                bChecked = true;
            }
        }
    }
    if (!bChecked) {
        alert("Please select milestone(s) for this action!");
        return false;
    }
    return true;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF"><%
    String strTitle = "Project Milestone";
%><%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmMilestoneList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<CENTER>
<TABLE border="0" width="90%">
    <TR>
        <TD width="10%" align="left"><B><FONT face="Verdana" size="1" color="#000042">Project Code</FONT></B></TD>
        <TD width="75%" align="left"><FONT face="Verdana" size="1" color="#000042"><%=beanMilestoneList.getProjectCode()%></FONT></TD>
    </TR>
    <TR>
        <TD width="10%" align="left"><B><FONT face="Verdana" size="1" color="#000042">Project Name</FONT></B></TD>
        <TD width="75%" align="left"><FONT face="Verdana" size="1" color="#000042"><%=beanMilestoneList.getProjectName()%></FONT></TD>
    </TR>
</TABLE>
<TABLE border="0" cellspacing="1" width="90%">
    <TR>
        <TD colspan="8" align="left"><!-- PROJECT ID AND PROJECT NAME --></TD>
    </TR>
    <!-- MILESTONE  LIST -->
    <TR class="table_header">
        <!-- TABLE HEADER -->
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN style="color: white">#</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN style="color: white">ID</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN style="color: white">Major Milestone</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN style="color: white">Complete</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN style="color: white">Planned Finish</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN style="color: white">Replanned Finish</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN style="color: white">Actual Finish</SPAN></FONT></B></TD>
        <TD><B><FONT face="Verdana" size="1" color="white"><SPAN style="color: white">Description</SPAN></FONT></B></TD>
    </TR><%
    int nNumberOfRows = beanMilestoneList.getMilestoneList().getNumberOfRows();
    if (nNumberOfRows > 0) {
        for (int i = 0; i < nNumberOfRows; i++) {
%>
    <TR class="Row<%=(i + 1) % 2 + 1%>">
        <!-- CHECKBOX  -->
        <TD><FONT face="Verdana" size="1"><INPUT type="checkbox" name="selected" value="<%=beanMilestoneList.getMilestoneList().getCell(i, 0)%>"></FONT></TD>
        <!-- ID  -->
        <TD><FONT face="Verdana" size="1"><%=beanMilestoneList.getMilestoneList().getCell(i, 0)%></FONT></TD>
        <!-- MAJOR MILESTONES  -->
        <TD><FONT face="Verdana" size="1"> <A href="javascript:doUpdate('<%=beanMilestoneList.getMilestoneList().getCell(i, 0)%>')"><%=beanMilestoneList.getMilestoneList().getCell(i, 1)%></A></FONT></TD>
        <!-- COMPLETE  -->
        <TD><FONT face="Verdana" size="1"><%=beanMilestoneList.getMilestoneList().getCell(i, 2)%></FONT></TD>
        <!-- Base Finish Date  -->
        <TD><FONT face="Verdana" size="1"><%=beanMilestoneList.getMilestoneList().getCell(i, 3)%></FONT></TD>
        <!-- Plan Finish Date  -->
        <TD><FONT face="Verdana" size="1"><%=beanMilestoneList.getMilestoneList().getCell(i, 4)%></FONT></TD>
        <!-- Actual Finish Date  -->
        <TD><FONT face="Verdana" size="1"><%=beanMilestoneList.getMilestoneList().getCell(i, 5)%></FONT></TD>
        <!-- Description  -->
        <TD><FONT face="Verdana" size="1"><%=beanMilestoneList.getMilestoneList().getCell(i, 9)%></FONT></TD>
    </TR><%
        } //end for
    }  //end if
    else {
 %>
    <TR>
        <TD colspan="14">No milestone found</TD>
    </TR><%
    } //end else
%>
</TABLE>
<INPUT type="hidden" name="hidMilestoneID" value="">
<INPUT type="hidden" name="hidProjectID" value="<%=beanMilestoneList.getProjectID()%>">
<INPUT type="hidden" name="hidProjectCode" value="<%=beanMilestoneList.getProjectCode()%>">
<INPUT type="hidden" name="hidProjectName" value="<%=beanMilestoneList.getProjectName()%>">
<INPUT type="hidden" name="hidPrStartDate" value="<%=beanMilestoneList.getProjectStartDate()%>">
<INPUT type="hidden" name="hidPrBaseFinishDate" value="<%=beanMilestoneList.getProjectBaseFinishDate()%>">
<INPUT type="hidden" name="hidPrPlanFinishDate" value="<%= beanMilestoneList.getProjectPlanFinishDate()%>">
<INPUT type="hidden" name="hidPrActualFinishDate" value="<%= beanMilestoneList.getProjectActualFinishDate()%>">
<BR>
<P align="center"><INPUT type="button" name="DoAddMileStone" onclick="javascript:doAdd()" value="Add">&nbsp;
<INPUT type="button" name="DoDeleteMileStone" onclick="javascript:doDelete()" value="Delete">&nbsp;
<INPUT type="button" name="DoBackFromListMilestone" onclick="javascript:doBack()" value="Back"></P>
</CENTER>
</FORM>
</BODY>
</HTML>