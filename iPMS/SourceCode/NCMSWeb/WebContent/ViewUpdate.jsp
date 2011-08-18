<%@ page language="java" import="javax.servlet.*, fpt.ncms.bean.*,
        fpt.ncms.util.StringUtil.*, fpt.ncms.constant.NCMS"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ViewAddBean beanViewAdd = (ViewAddBean)session.getAttribute("beanViewAdd");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
<TITLE>View Detail</TITLE>
<SCRIPT language="javascript" src='inc/ComboLib.js'></SCRIPT>
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<%@ include file="Header.jsp"%>
<TABLE class="menu" cellpadding="0" cellspacing="0" width="100%" height="20pt">
    <TR>
    <TR>
        <TD width="5%" height="21" style="border-Right: #FFFFFF thin solid; border-width: 1px">
        <P class="menuitem" align="center" style="cursor: hand" onclick="javascript:doNCListSearch()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;NCList&nbsp;&nbsp;</P>
        </TD>
        <TD align="right">
        <P class="menuitem" style="cursor: hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">Logout&nbsp;&nbsp;</P>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="44%" valign="top" align="left">
        <P><IMG src="images/Headers/definePersonalViews.gif"></P>
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
<FORM name="frmView" method="POST"><INPUT type="hidden" name="hidAction" value=""><INPUT type="hidden" name="selectedProjectHidden" value=""><INPUT type="hidden" name="hidFields" value="">
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10" height="175">
    <COLGROUP>
        <COL width="15%">
        <COL width="30%">
        <COL width="10%">
        <COL width="15%">
        <COL width="15%">
        <COL width="15%">
    <TR>
        <TD height="12"></TD>
        <TD height="12">&nbsp;<B>Title</B>:</TD>
        <TD height="12"></TD>
        <TD height="12">&nbsp;</TD>
        <TD height="12">&nbsp;<B>Order by</B>:</TD>
        <TD height="12"></TD>
    </TR>
    <TR>
        <TD height="20">&nbsp;</TD>
        <TD height="20">&nbsp;<INPUT type="textbox" name="txtTitle" size="50" maxlength="63" value="<%=beanViewAdd.getTitle()%>"></TD>
        <TD height="20"></TD>
        <TD height="20">&nbsp;</TD>
        <TD height="20"><SELECT name="cboOrderBy" style="width: 100%"><%
    for (int i = 0; i < beanViewAdd.getComboOrderBy().getNumberOfRows(); i++) {
        out.write("<OPTION value=\"");
        out.write(beanViewAdd.getComboOrderBy().getCell(i, 0)
                + (beanViewAdd.getComboOrderBy().getCell(i, 0).equals(
                        beanViewAdd.getOrderBy()) ? "\" selected" : "\""));
        out.write(">" + beanViewAdd.getComboOrderBy().getCell(i, 0) + "</OPTION>");
    }
%>
        </SELECT></TD>
        <TD height="20"></TD>
    </TR>
    <TR>
        <TD colspan="6" height="26">
        <P align="center"><IMG border="0" src="images/Buttons/chooseColumn.gif" width="140" height="15" align="left">Mandatory columns: NCID, Description, Type, CreationDate, Status</P>
        </TD>
    </TR>
    <TR>
        <TD height="12"></TD>
        <TD height="12">&nbsp;<B>Optional</B></TD>
        <TD height="12"></TD>
        <TD height="12" colspan="2">&nbsp;<B>Selected</B></TD>
        <TD height="12"></TD>
    </TR>
    <TR>
        <TD height="100"></TD>
        <TD height="100"><SELECT name="lstField" size="10" style="width: 266; height: 100" multiple ondblclick="move(lstField,lstSelect)" title="Double click to move a field">
            <OPTION value="Assignee">Assignee</OPTION>
            <OPTION value="Cause">Cause</OPTION>
            <OPTION value="ClosureDate">ClosureDate</OPTION>
            <OPTION value="Code">Code</OPTION>
            <OPTION value="CPAction">CPAction</OPTION>
            <OPTION value="Creator">Creator</OPTION>
            <OPTION value="Deadline">Deadline</OPTION>
            <OPTION value="DetectedBy">DetectedBy</OPTION>
            <OPTION value="GroupName">GroupName</OPTION>
            <OPTION value="Impact">Impact</OPTION>
            <OPTION value="ISOClause">ISOClause</OPTION>
            <OPTION value="KPA">KPA</OPTION>
            <OPTION value="NCLevel">NCLevel</OPTION>
            <OPTION value="Note">Note</OPTION>
            <OPTION value="Repeat">Repeat</OPTION>
            <OPTION value="Reviewer">Reviewer</OPTION>
            <OPTION value="ProjectID">ProjectID</OPTION>
            <OPTION value="Process">Process</OPTION>
            <OPTION value="TypeOfAction">TypeOfAction</OPTION>
            <OPTION value="TypeOfCause">TypeOfCause</OPTION>
        </SELECT></TD>
        <TD align="center" height="100"><IMG border="0" src="images/Buttons/add.gif" width="14" height="13" onclick="move(lstField,lstSelect)"><BR>
        <BR>
        <IMG border="0" src="images/Buttons/move.gif" width="14" height="13" onclick="move(lstSelect,lstField)"></TD>
        <TD height="100" colspan="2"><SELECT name="lstSelect" size="10" style="width: 266; height: 100" ondblclick="move(lstSelect,lstField)" multiple title="Double click to move a field"></SELECT></TD>
        <TD height="100"></TD>
    </TR>
</TABLE>
<HR size="1" bgcolor="#bdbdbd" color="#C0C0C0">
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
    <TR>
        <TD width="70%">&nbsp;<IMG border="0" onclick="doSaveUpdateView()" onmouseout="this.src='images/Buttons/b_ok_n.gif'" onmouseover="this.src='images/Buttons/b_ok_p.gif'" src="images/Buttons/b_ok_n.gif" width="64" height="23"> <IMG border="0" onclick="javascript:doViewList()" src="images/Buttons/b_cancel_n.gif" onmouseout="this.src='images/Buttons/b_cancel_n.gif'" onmouseover="this.src='images/Buttons/b_cancel_p.gif'" width="64" height="23">
        </TD>
    </TR>
</TABLE>
</FORM>
</BODY>
</HTML>
<SCRIPT language="javascript">
    removeFields(frmView.lstField, "<%=beanViewAdd.getField()%>");
    addFields(frmView.lstSelect, "<%=beanViewAdd.getField()%>");

function doNCListSearch() {
    frmView.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmView.action = "NcmsServlet";
    frmView.submit();
}

function doNCList() {
    frmView.hidAction.value = "<%=NCMS.NC_LIST%>";
    frmView.action = "NcmsServlet";
    frmView.submit();
}

function doViewList() {
    frmView.hidAction.value = "<%=NCMS.VIEW_LIST%>";
    frmView.action = "NcmsServlet";
    frmView.submit();
}

function doSaveUpdateView() {
    if (formValidate()) {
        frmView.hidFields.value = getList(frmView. lstSelect);
        frmView.hidAction.value = "<%=NCMS.VIEW_SAVE_UPDATE%>";
        frmView.action = "NcmsServlet";
        frmView.submit();
    }
}

function formValidate() {
    if (trim(frmView.txtTitle.value) == "") {
        alert("View title cannot be empty.");
        frmView.txtTitle.focus();
        return false;
    }
    return true;
}
function doLogOut() {
    frmView.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmView.action = "NcmsServlet";
    frmView.submit();
}

</SCRIPT>