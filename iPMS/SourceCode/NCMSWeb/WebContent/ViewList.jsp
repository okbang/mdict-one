<%@ page language="java" import="javax.servlet.*, fpt.ncms.bean.*,
        fpt.ncms.util.StringUtil.*, fpt.ncms.constant.NCMS"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ViewListBean beanViewList = (ViewListBean)session.getAttribute("beanViewList");
    int i = 0;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
<TITLE>Views List</TITLE>
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
</TABLE><%
    if (!"".equals(beanUserInfo.getMessage())){%>
<FONT face="Verdana" color="red"><%=beanUserInfo.getMessage()%></FONT><%
        beanUserInfo.setMessage("");
    }
%>
<FORM method="POST" name="frmView"><INPUT type="hidden" name="hidID" value=""> <INPUT type="hidden" name="hidAction"><INPUT type="hidden" name="selectedProjectHidden" value="">
<TABLE bgcolor="#9292CB" border="0" cellpadding="0" cellspacing="1" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber2">
    <TR class="header">
        <TD width="4%" height="21"><P align="center">&nbsp;</P></TD>
        <TD width="25%" height="21">Title</TD>
        <TD width="55%" height="21">Fields</TD>
        <TD width="16%" height="21">Order by</TD>
    </TR><%
    for (i = 0; i < beanViewList.getViewList().getNumberOfRows(); i++) {
%>
    <TR class="row<%=i % 2 + 1%>" onclick="javascript:document.frmView.rdoView<%=i%>.click()" ondblclick="javascript:document.imgEdit.click()" style="cursor: hand" onmouseout="this.style.color='#000000';" onmouseover="this.style.color='#FF0000';">
        <TD><INPUT type="radio" id="<%=i%>" value="<%=beanViewList.getViewList().getCell(i, 0)%>)" name="rdoView<%=i%>" onclick="javascript:frmView.hidID.value='<%=beanViewList.getViewList().getCell(i, 0)%>'"></TD>
        <TD><%=beanViewList.getViewList().getCell(i, 1)%></TD>
        <TD><%=beanViewList.getViewList().getCell(i, 2)%></TD>
        <TD><%=beanViewList.getViewList().getCell(i, 3)%></TD>
    </TR><%
    }
    if (i == 0) {
%>
    <TR class="Row1">
        <TD colspan="4">&nbsp;&nbsp;&nbsp;No record found</TD>
    </TR><%
    }
%>
</TABLE>
</FORM>
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" leftmargin="10">
    <TR>
        <TD width="70%">&nbsp;<IMG border="0" onclick="doAddView()" src="images/Buttons/b_add_n.gif" onmouseout="this.src='images/Buttons/b_add_n.gif'" style="cursor: hand" onmouseover="this.src='images/Buttons/b_add_p.gif'" width="64" height="23" name="imgAdd">
        <IMG border="0" onclick="doUpdateView()" src="images/Buttons/b_change_n.gif" onmouseout="this.src='images/Buttons/b_change_n.gif'" onmouseover="this.src='images/Buttons/b_change_p.gif'" style="cursor: hand" width="64" height="23" name="imgEdit">
        <IMG border="0" onclick="doDeleteView()" src="images/Buttons/b_delete_n.gif" onmouseout="this.src='images/Buttons/b_delete_n.gif'" onmouseover="this.src='images/Buttons/b_delete_p.gif'" style="cursor: hand" width="64" height="23" name="imgDelete">
        <IMG border="0" onclick="doNCList()" src="images/Buttons/b_back_n.gif" onmouseout="this.src='images/Buttons/b_back_n.gif'" onmouseover="this.src='images/Buttons/b_back_p.gif'" style="cursor: hand" width="64" height="23" name="imgBack"></TD>
    </TR>
</TABLE>
</BODY>
</HTML>
<SCRIPT language="javascript">
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

function doAddView() {
    frmView.hidAction.value = "<%=NCMS.VIEW_ADD%>";
    frmView.action = "NcmsServlet";
    frmView.submit();
}

function doDeleteView() {
    if (frmView.hidID.value == "") {
        alert("You must select a view!");
        return false;
    }
    else {
        if (confirm("Are you sure to permanently delete this view?")) {
            frmView.hidAction.value = "<%=NCMS.VIEW_DELETE%>";
            frmView.action = "NcmsServlet";
            frmView.submit();
        }
        else {
            return false;
        }
    }
}

function doUpdateView() {
    if (frmView.hidID.value == "") {
        alert("You must select a view!");
        return false;
    }
    else {
        frmView.hidAction.value = "<%=NCMS.VIEW_UPDATE%>";
        frmView.action = "NcmsServlet";
        frmView.submit();
    }
}
function doLogOut() {
    frmView.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmView.action = "NcmsServlet";
    frmView.submit();
}

</SCRIPT>