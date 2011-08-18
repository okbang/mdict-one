<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.DefectManagement.*,fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/validate.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>View Defect Creating</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT language="javascript">
function doAdd() {
    var form = document.frmQueryAdd;
    if (checkValidate(form)) {
        form.hidAction.value = "DM";
        form.hidActionDetail.value = "SaveNewQuery";
        form.submit();
    }
}

function doBack() {
    var form = document.frmQueryAdd;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.submit();
}

function doQueryListing() {
    var form = document.frmQueryAdd;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function checkValidate(form) {
    if (!validateRequiredTextField(form.Name, "Query name could not be empty!")) {
        return false;
    }
    var i = 0;
    var flag = true;
    var temp;
    var block = true;
    for (i = 0; i < 10; i++) {
        if (form.FieldName[i].value != 0) {
            if (block) {
                flag = false;
                if (!validateRequiredTextField(form.Values[i],
                        "Please enter value of this field!")) {
                    return false;
                }
                else {
                    temp = form.FieldName[i].value;
                    if ((temp == "defect.qa_id") || (temp == "defect.ds_id")
                            || (temp == "defect.defs_id")) {
                        if (!validateNonNegativeIntegerTextField(form.Values[i],
                                "Invalid number!")) {
                            form.Values[i].focus();
                            form.Values[i].select();
                            return false;
                        }
                    }
                    if ((temp == "defect.close_date") ||
                        (temp == "defect.create_date") ||
                        (temp == "defect.due_date"))
                    {
                        if (!isDate(form.Values[i])) {
                            form.Values[i].focus();
                            form.Values[i].select();
                            return false;
                        }
                    }
                }
            }
            else {
                alert("Can not define expression in this line!");
                form.Values[i].focus();
                form.Values[i].select();
                return false;
            }
        }
        else {
            block=false;
        }
    }   //end for
    if (flag) {
        alert("Select field name to create a query!");
        return false;
    }
    return true;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<DIV>
<P><IMG border="0" src="Images/ViewDefectCreating.gif" width="411" height="28"></P>
<FORM method="POST" action="DMSServlet" name="frmQueryAdd"><INPUT type="hidden" name="hidActionDetail" value=""> <INPUT type="hidden" name="hidAction" value=""> <INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
</DIV>
<TABLE border="0" width="100%" class="TblOut2">
    <TR>
        <TD width="8%"><B>User:</B></TD>
        <TD width="24%"><%=beanUserInfo.getUserName()%></TD>
        <TD width="12%"><B>Login Date:</B></TD>
        <TD width="25%"><%=beanUserInfo.getDateLogin()%></TD>
        <TD width="9%"><B>Project</B></TD>
        <TD width="22%" align="right"><SELECT name="cboProjectList" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="AddQuery"%>','<%=""%>');"><%
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
        <TD width="22%" align="right"><SELECT name="cboProjectStatus" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="AddQuery"%>','<%=""%>');"><%
    String strCurrentStatus = beanUserInfo.getCurrentStatus();
    for (int i = 0; i < beanComboProject.getStatusList().getNumberOfRows(); i++) {
        StringMatrix smStatus = beanComboProject.getStatusList();
        out.write("<OPTION ");
        out.write(strCurrentStatus.equals(smStatus.getCell(i,0)) ? " selected " : " ");
        out.write("value=\"" + smStatus.getCell(i, 0) + "\">" + smStatus.getCell(i, 1) + "</OPTION>");
    }
%>        </SELECT></TD>

    </TR>
</TABLE>
<P></P>
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%">
    <TR>
        <TD width="15%" colspan="3"><B>Query name:</B></TD>
        <TD width="35%" colspan="3"><INPUT type="text" name="Name" size="35"></TD>
        <TD width="49%" colspan="3"><%
    if (beanUserInfo.getPosition().charAt(2) == '1') {
%>
        <INPUT type="checkbox" name="Scope" value="0"> <B>Public query</B> <%
    }
%>
        </TD>
    </TR>
</TABLE>
<P></P>
<TABLE border="0" cellspacing="1" cellpadding="1" width="100%" bgcolor="#000000">
    <TR class="Row0" height="19">
        <TD width="15%" align="center"><B>Field Name&nbsp;</B></TD>
        <TD width="7%" align="center"><B>Not</B></TD>
        <TD width="8%" align="center"><B>Criteria</B></TD>
        <TD width="37%" align="center"><B>Value</B></TD>
        <TD width="9%" align="center"><B>Logical</B></TD>
        <TD width="11%" align="center"><B>Group</B></TD>
    </TR><%
    for (int i = 0; i < 10; i++) {
%>
    <TR class="Row<%=(i + 1) % 2 + 1%>">
        <TD width="15%" align="center"><SELECT size="1" name="FieldName">
            <OPTION selected value="0"></OPTION>
            <OPTION value="defect.assigned_to">Assigned to</OPTION>
            <OPTION value="defect.Defect_Owner">Defect owner</OPTION>
            <OPTION value="defect.qa_id">QC Activity</OPTION>
            <OPTION value="defect.defs_id">Severity</OPTION>
            <OPTION value="defect.ds_id">Status</OPTION>
            <OPTION value="defect.title">Title</OPTION>
            <OPTION value="defect.created_by">Created by</OPTION>
            <OPTION value="defect.create_date">Create Date</OPTION>
            <OPTION value="defect.close_date">Close Date</OPTION>
            <OPTION value="defect.due_date">Due Date</OPTION>
        </SELECT></TD>

        <TD width="7%" align="center"><INPUT type="checkbox" name="Not" value="<%=i%>"></TD>
        <TD width="8%" align="center"><SELECT size="1" name="Criteria">
            <OPTION value="=">=</OPTION>
            <OPTION value=">">&gt;</OPTION>
            <OPTION value="<">&lt;</OPTION>
            <OPTION value=">=">&gt;=</OPTION>
            <OPTION value="<=">&lt;=</OPTION>
            <OPTION value="LIKE">LIKE</OPTION>
        </SELECT></TD>
        <TD width="37%" align="center"><INPUT type="text" name="Values" size="45"></TD>
        <TD width="9%" align="center"><SELECT size="1" name="Logical">
            <OPTION value="AND">AND</OPTION>
            <OPTION value="OR">OR</OPTION>
        </SELECT></TD>
        <TD width="11%" align="center"><SELECT size="1" name="Group">
            <OPTION value="1">Group 1</OPTION>
            <OPTION value="2">Group 2</OPTION>
            <OPTION value="3">Group 3</OPTION>
            <OPTION value="4">Group 4</OPTION>
        </SELECT></TD>
    </TR><%
    }
%>

</TABLE>
<FONT style="FONT-FAMILY: Verdana; FONT-SIZE: xx-small; FONT-WEIGHT: bold; COLOR: #000080">Date Format:&nbsp; mm/dd/yy</FONT>
<P><INPUT type="button" name="btnAdd" class="Button" onclick="javascript:doAdd()" value="Add">&nbsp;&nbsp;&nbsp;&nbsp;
<INPUT type="button" name="btnBack" class="Button" onclick="javascript:doBack()" value="Back"></P>
</FORM>
</BODY>
</HTML>
<SCRIPT language="JavaScript">
     document.forms[0].Name.focus();
</SCRIPT>