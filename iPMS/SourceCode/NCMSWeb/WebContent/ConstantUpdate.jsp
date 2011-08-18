<%@ page language="java" import="javax.servlet.*, fpt.ncms.bean.*,
        fpt.ncms.util.StringUtil.*, fpt.ncms.constant.NCMS"%><%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ConstantAddBean beanConstantAdd = (ConstantAddBean)session.getAttribute("beanConstantAdd");
    int i = 0;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
<TITLE>Constant Detail</TITLE>
<SCRIPT language="javascript" src='inc/Common.js'></SCRIPT>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<%if (beanUserInfo.getLocation() == 0) {%>
<%@ include file="Header.jsp"%>
<%}else {//if (beanUserInfo.getLocation() == 1) {%>
<%@ include file="HeaderCallLog.jsp"%>
<%}%>
<TABLE class="menu" cellpadding="0" cellspacing="0" width="100%" height="20pt">
    <TR>
        <TD width="15%" height="21" style="border-Right: #FFFFFF thin solid; border-width: 1px">
        <P class="menuitem" align="center" style="cursor: hand" onclick="javascript:doCancel()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Constant List&nbsp;&nbsp;</P>
        </TD>
        <TD align="right">
        <P class="menuitem" style="cursor: hand" onclick="javascript:doLogOut()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">Logout&nbsp;&nbsp;</P>
        </TD>
    </TR>
</TABLE>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="44%" valign="top" align="left">
        <P><IMG src="images/Headers/constantsUpdate.gif"></P>
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
<FORM method="post" name="frmConstantList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<TABLE bgcolor="#9292CB" border="0" cellpadding="0" cellspacing="1" style="border-collapse: collapse" bordercolor="#111111" width="100%">
    <TR id="tabtitle" class="header">
        <TD width="4%" height="21"></TD>
        <TD width="34%" align="center">Value</TD>
        <TD width="42%" align="center">Constant Type</TD>
    </TR><%
    for (i = 0; i < beanConstantAdd.getConstantList().getNumberOfRows(); i++) {
%>
    <TR id="tabcontent" class="row<%=i % 2 + 1%>" style="cursor: hand" onmouseout="this.style.color='#000000';" onmouseover="this.style.color='#FF0000';">
        <TD align="center"><INPUT type="hidden" name="hidCode" id="<%=i%>" value='<%=beanConstantAdd.getConstantList().getCell(i, 0)%>'></TD>
        <TD><INPUT type="text" name="txtDescription" maxlength="255" onkeypress="doCheck()" style="width: 100%" id="Description<%=i%>" value="<%=beanConstantAdd.getConstantList().getCell(i, 1)%>"></TD>
        <TD><SELECT name="txtConstantType" disabled style="width: 60%" id="Type<%=i%>"><%
        for (int j = 0; j < beanConstantAdd.getComboConstantType().getNumberOfRows(); j++) {
            out.write("<OPTION value=\"" +
                      beanConstantAdd.getComboConstantType().getCell(j, 0) +
                      (beanConstantAdd.getConstantList().getCell(i, 2).equals(beanConstantAdd.getComboConstantType().getCell(j, 0)) ? "\" selected>" : "\">") +
                      beanConstantAdd.getComboConstantType().getCell(j, 1) + "</OPTION>");
        }
%>
        </SELECT></TD>
    </TR><%
    }
%>
</TABLE>
<BR>
<TABLE align="left" width="100%">
    <TR>
        <TD width="70%">&nbsp;<IMG name="imgOK" alt="Save" style="cursor: hand" onclick="javascript:doUpdate()" tabindex="1" src="images/Buttons/b_ok_n.gif" width="64" height="23" onmouseout="this.src='images/Buttons/b_ok_n.gif'" onmouseover="this.src='images/Buttons/b_ok_p.gif'">
        <IMG name="imgBack" src="images/Buttons/b_cancel_n.gif" onmouseout="this.src='images/Buttons/b_cancel_n.gif'" onmouseover="this.src='images/Buttons/b_cancel_p.gif'" style="cursor: hand" onclick="javascript:doCancel()">
        </TD>
    </TR>
</TABLE>
<BR>
</FORM>
</BODY>
</HTML>
<SCRIPT language="javascript">
function doCheck() {
	if (window.event.keyCode == 13) {
       doUpdate();
    }
}

function doLogOut() {
    frmConstantList.hidAction.value = "<%=NCMS.HOMEPAGE_ACTION%>";
    frmConstantList.action = "NcmsServlet";
    frmConstantList.submit();
}

function doCancel() {
    frmConstantList.hidAction.value = "<%=NCMS.CONSTANT_LIST%>"
    frmConstantList.action = "NcmsServlet";
    frmConstantList.submit();
}

function doUpdate() {
    var nTotalRows;
    for (var i = 0; i < frmConstantList.elements.length; i++) {
        var e = frmConstantList.elements[i];
        if ((e.name == "txtDescription") && (e.type == "text") && (trim(e.value) == "")) {
            alert("Invalid value for description");
            frmConstantList.txtDescription[i].focus();
            return false;
        }
    }
    frmConstantList.hidAction.value = "<%=NCMS.CONSTANT_SAVE_UPDATE%>"
    frmConstantList.action = "NcmsServlet";
    frmConstantList.submit();
}
</SCRIPT>