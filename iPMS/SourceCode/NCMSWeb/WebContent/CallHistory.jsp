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
<TITLE>Call History</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="inc/CallStylesheet.css">
</HEAD>
<body topmargin="0" leftmargin="0">
<form name="frmHistory" method="post" action="NcmsServlet">
<input type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="selectedProjectHidden" value="">
<input type="hidden" name="hidID" value="<%=beanNCAdd.getNCModel().getNCID()%>">
<%@ include file="HeaderCallLog.jsp"%>
<table border="0" cellPadding="0" cellSpacing="0"
style="BACKGROUND-COLOR: slategray;COLOR: white;FONT-FAMILY: Verdana;FONT-SIZE: xx-small;FONT-WEIGHT: bold;HEIGHT: 20px;TEXT-DECORATION: none"
height="20" width="100%">
    <tr>
        <TD width="5%" height="21" style="border-Right:#FFFFFF thin solid;border-width:1px"><P class="menuitem" align="center" style="cursor:hand" onclick="javascript:doBack()" onmouseover="this.style.color='yellow'" onmouseout="this.style.color='white'">&nbsp;&nbsp;Back&nbsp;&nbsp;</P></TD>
        <TD align="right"><P class="menuitem">&nbsp;</P></TD>
    </tr>
</table>
<table border="0" cellPadding="0" cellSpacing="0" width="99%">
  <tbody>
    <tr>
      <td align="left" vAlign="top" width="44%">
          <P><FONT size="6">Call History</FONT></P>
      </td>
      <td vAlign="top" width="56%">
        <div align="right">
          <table border="0" cellPadding="0" cellSpacing="0" height="50" width="199">
            <tbody>
              <tr>
                <td>
                  <table border="0" cellPadding="0" cellSpacing="0" width="100%">
                    <tbody>
                      <tr>
                        <td width="75">&nbsp;</td>
                        <td width="70"><b>User:</b></td>
                        <TD width="54"><B><%=beanUserInfo.getLoginName()%></B></TD>
                      </tr>
                      <tr>
                        <td width="75">&nbsp;</td>
                        <td width="70"><b>Role:</b></td>
                        <TD width="54"><B><%=beanUserInfo.getRoleName()%></B></TD>
                      </tr>
                    </tbody>
                  </table>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </td>
    </tr>
  </tbody>
</table>
<TABLE bgcolor="#3333CC" bordercolor="#111111" border="0" cellpadding="0" cellspacing="1" style="border-collapse: collapse" width="100%" id="AutoNumber2">
    <TR class="Header">
        <TD align="left" valign="middle" width="100%"><%=("#" + beanNCAdd.getNCModel().getNCID())%></TD>
    </TR>
    <TR class="Row2" height="19">
        <TD valign="middle" align="left" width="100%"><%=beanNCAdd.getHistory()%></TD>
    </TR>
</TABLE>
<P><INPUT type="button" name="Back" class="button" onclick="javascript:doBack()" value="Back"></P>
</FORM>
<SCRIPT language="javascript">
var myForm = document.forms[0];

function doBack() {
	myForm.hidAction.value = "<%=NCMS.CALL_LOG_UPDATE%>";
    myForm.action = "NcmsServlet";
    myForm.submit();
}
</SCRIPT>
</BODY>
</HTML>