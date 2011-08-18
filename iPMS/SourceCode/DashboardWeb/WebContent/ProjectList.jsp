<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*, javax.servlet.*,
            fpt.dashboard.bean.*, fpt.dashboard.bean.ProjectManagement.*,
            fpt.dashboard.util.StringUtil.*,
            fpt.dashboard.constant.* " %><%@
    page isThreadSafe="true" errorPage="error.jsp" contentType="text/html; charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ProjectListBean beanProjectList
            = (ProjectListBean)request.getAttribute("beanProjectList");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Project List</TITLE>
<LINK rel="stylesheet" type="text/css" href="styles/StyleSheet.css">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
</HEAD>
<BODY bgcolor="#FFFFFF">
<%!
    String strTitle = "Project List";
%>
<%@
    include file = "PageHeader.inc"
%>
<FORM method="post" action="DashboardServlet" name="frmSearchProject"><INPUT
    type="hidden" name="hidAction" value=""> <INPUT type="hidden"
    name="hidActionDetail" value="">
<TABLE width="50%" border="0" cellpadding="3" cellspacing="3">
    <TR>
        <TD class="required_left_cell_bkgrnd" align="left" valign="middle"
            width="20%"><B><FONT color="#000080">&nbsp;&nbsp;Group&nbsp;&nbsp;</FONT></B><%
    String strGroup = beanProjectList.getGroup();
    String strDisableGroup = "";
    String strDisabled = "";
    // Disable external viewers from see other group's projects
    if (("4".equals(beanUserInfo.getRole())) || ("5".equals(beanUserInfo.getRole())) ||
        (DATA.ROLE_GROUPLEADER.equals(beanUserInfo.getSRole())))
    {
        strDisableGroup = "disabled";
    }

    // Only QA/Manager can create project
    if ("2".equals(beanUserInfo.getRole())) {
        //strDisabled = "";
    }
    else {
        strDisabled = "disabled";
    }
%>
<SELECT name="cboGroup" size="1" style="width: 120" <%=strDisableGroup%>>
            <%
    for (int nRow = 0x00; nRow < beanProjectList.getGroupList().getNumberOfRows(); nRow++) {
        String strText = beanProjectList.getGroupList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanUserInfo.getGroupName() != null) {
            out.write(beanProjectList.getGroup().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">" + strText + "</OPTION>");
    }
%>
        </SELECT> <INPUT type="hidden" name="hidGroup" value="<%= strGroup%>"><!-- don't delete --></TD>
        <TD class="required_left_cell_bkgrnd" align="left" valign="middle"
            width="20%"><B><FONT color="#000080">&nbsp;&nbsp;Type&nbsp;&nbsp;</FONT></B>
        <SELECT name="cboType" size="1" style="width: 100">
            <%
    for (int nRow = 0x00; nRow < beanProjectList.getTypeList().getNumberOfRows(); nRow++) {
        String strText = beanProjectList.getTypeList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectList.getType() != null) {
            out.write(beanProjectList.getType().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">"
                + beanProjectList.getTypeList().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD class="required_left_cell_bkgrnd" align="left" valign="middle"
            width="20%"><B><FONT color="#000080">&nbsp;&nbsp;Category&nbsp;&nbsp;</FONT></B>
        <SELECT name="cboCategory" size="1" style="width: 100">
            <%
    for (int nRow = 0x00; nRow < beanProjectList.getCateList().getNumberOfRows(); nRow++) {
        String strText = beanProjectList.getCateList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectList.getCate() != null) {
            out.write(beanProjectList.getCate().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">"
                + beanProjectList.getCateList().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD class="required_left_cell_bkgrnd" align="left" valign="middle"
            width="20%"><B><FONT color="#000080">&nbsp;&nbsp;Status&nbsp;&nbsp;</FONT></B>
        <SELECT name="cboStatus" size="1" style="width: 100">
            <%
    for (int nRow = 0x00; nRow < beanProjectList.getStatusList().getNumberOfRows(); nRow++) {
        String strText = beanProjectList.getStatusList().getCell(nRow, 0);
        out.write("<OPTION ");
        if (beanProjectList.getStatus() != null) {
            out.write(beanProjectList.getStatus().equals(strText) ? " selected " : " ");
        }
        out.write("value=\"" + strText + "\">"
                + beanProjectList.getStatusList().getCell(nRow, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD><INPUT type="button" name="DoSearchProject"
            onclick="javascript:doSearchProject()" value="Search"></TD>
    </TR>
</TABLE>
</FORM>
<FORM method="post" action="DashboardServlet" name="frmProjectList"><INPUT
    type="hidden" name="hidProjectID" value=""> <INPUT type="hidden"
    name="hidTypeOfView" value="ViewProjectList">
<TABLE border="0" cellspacing="1" cellpadding="0" width="100%">
    <TR class="table_header">
        <TD width="10%" bgcolor="#006699" align="center" height="25"><B><FONT
            color="#ffffff" face="Verdana">&nbsp;D</FONT></B></TD>
        <TD width="10%" bgcolor="#006699" height="25"><B><FONT face="Verdana"
            color="#ffffff">&nbsp;Code</FONT></B></TD>
        <TD width="40%" bgcolor="#006699" height="25"><B><FONT face="Verdana"
            color="#ffffff">&nbsp;Name</FONT></B></TD>
        <TD width="20%" bgcolor="#006699" height="25"><B><FONT face="Verdana"
            color="#ffffff">&nbsp;Category</FONT></B></TD>
        <TD width="20%" bgcolor="#006699" height="25"><B><FONT face="Verdana"
            color="#ffffff">&nbsp;Leader</FONT></B></TD>
    </TR>
    <%
    StringMatrix table = beanProjectList.getProjectList();
    int maxrows = table.getNumberOfRows();
    String id ="";
    String code = "";
    String name = "";
    String leader = "";
    String cate="";

    int i = 0;
    String rClass = "";
    while ( i < maxrows ) {
        id = table.getCell(i, 0);
        code = table.getCell(i, 1);
        name = table.getCell(i, 2);
        leader = table.getCell(i, 3);
        cate=table.getCell(i, 4);

        if (i % 2 == 0) {
            rClass = "row2";
        }
        else {
            rClass = "row1";
        }
%>
    <TR class="<%=rClass%>">
        <TD width="10%" align="center"><INPUT type="checkbox"
            name="ProjectCheckList" value="<%=id%>"></TD>
        <TD width="10%" align="left"><FONT face="Verdana">&nbsp;<%=code%></FONT></TD>
        <TD width="40%" align="left">&nbsp;<A
            href="javascript:doProjectDetail('<%=id%>')"><%=name%></A></TD>
        <TD width="20%"><FONT face="Verdana">&nbsp;<%=cate%></FONT></TD>
        <TD width="20%"><FONT face="Verdana">&nbsp;<%=leader%></FONT></TD>
    </TR>
    <%
        i++;
    }
%>
    <TR height="10">
        <TD width="10%" align="center"><INPUT type="hidden"
            name="ProjectCheckList"></TD>
        <TD width="10%"></TD>
        <TD width="20%"></TD>
        <TD width="20%"></TD>
    </TR>
</TABLE>
<INPUT type="hidden" name="hidAction" value=""> <INPUT type="hidden"
    name="hidActionDetail" value="">
<P align="center">
<INPUT type="hidden" name="DoAddProject" onclick="javascript:doAddNew()" value="Create Project" <%=strDisabled%>>&nbsp;
</P>
</FORM>
<SCRIPT language="javascript">
function doSearchProject() {
    var form = document.frmSearchProject;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewProjectList";
    form.action = "DashboardServlet";
    form.submit();
}
<%
    // External viewers and project leaders cannot create or delete project
    if ("2".equals(beanUserInfo.getRole())) {
%>
function doAddNew() {
    var form = document.frmProjectList;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "AddProject";
    form.action = "DashboardServlet";
    form.submit();
}
<%
    } //~ if ("2".equals(beanUserInfo.getRole()))
%>
function doProjectDetail(id) {
    var form = document.frmProjectList;
    form.hidProjectID.value = id;
    form.hidAction.value = "PM";
    form.hidActionDetail.value = "ViewProjectDetail";
    form.action = "DashboardServlet";
    form.submit();
}

function checkValid() {
    var bChecked = false;
    for (var i = 0; i < document.forms[1].elements.length; i++) {
        var e = document.forms[1].elements[i];
        if (e.name == "ProjectCheckList" && e.type == "checkbox") {
            if (e.checked == 1) {
                bChecked = true;
            }
        }
    }
    if (!bChecked) {
        alert("Please select project(s) to delete.");
        return false;
    }
    return true;
}
</SCRIPT>
</BODY>
</HTML>
