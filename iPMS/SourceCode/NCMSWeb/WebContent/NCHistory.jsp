<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java"
    import="java.util.Calendar,
            javax.servlet.*,
            fpt.ncms.bean.*,
            fpt.ncms.model.NCModel,
            fpt.ncms.util.StringUtil.*,
            fpt.ncms.constant.NCMS"%><%@
    page isThreadSafe="false" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    NCAddBean beanNCAdd = (NCAddBean) session.getAttribute("beanNCAdd");
%>
<HTML>
<HEAD>
<TITLE>NC History</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/QMSStylesheet.css">
</HEAD>
<body topmargin="0" leftmargin="0">
<form name="frmHistory" method="post" action="NcmsServlet">
<input type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<input type="hidden" name="hidID" value="<%=beanNCAdd.getNCModel().getNCID()%>">
<%@ include file="Header.jsp"%>
<table border="0" cellPadding="0" cellSpacing="0"
style="BACKGROUND-COLOR: '#000071';COLOR: white;FONT-FAMILY: Verdana;FONT-SIZE: xx-small;FONT-WEIGHT: bold;HEIGHT: 20px;TEXT-DECORATION: none"
height="20" width="100%">
    <tr>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doBack()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Back&nbsp;&nbsp;</P></TD>
        <TD align="right"><P class="menuitem">&nbsp;</P></TD>
    </tr>
</table>
<TABLE border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD width="44%" valign="top" align="left">
        <P><IMG src="images/Headers/ncHistory.gif"></P>
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
<TABLE bgcolor="#3333CC" bordercolor="#111111" border="0" cellpadding="0" cellspacing="1" style="border-collapse: collapse" width="100%" id="AutoNumber2">
    <TR class="Header">
        <TD align="left" valign="middle" width="100%"><%=beanNCAdd.getNCModel().getCode()%></TD>
    </TR>
    <TR class="Row2" height="19">
        <TD valign="middle" align="left" width="100%"><%=beanNCAdd.getHistory()%></TD>
    </TR>
</TABLE>
<P>&nbsp;<IMG border="0" style="cursor: hand" tabindex="2" onclick="doBack()" onkeypress="javascript:if (window.event.keyCode==13 || window.event.keyCode==32) doBack();" onmouseout="this.src='images/Buttons/b_back_n.gif'" onmouseover="this.src='images/Buttons/b_back_p.gif'" src="images/Buttons/b_back_n.gif"></P>
</FORM>
<SCRIPT language="javascript">
var myForm = document.forms[0];

function doBack() {
	myForm.hidAction.value = "<%=NCMS.NC_UPDATE%>";
    myForm.action = "NcmsServlet";
    myForm.submit();
}
</SCRIPT>
</BODY>
</HTML>