<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java" import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.DefectManagement.*,fpt.dms.bo.combobox.*,fpt.dms.framework.util.StringUtil.*,fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    DefectHistoryBean beanDefectHistory
            = (DefectHistoryBean)request.getAttribute("beanDefectHistory");
    ComboProject beanComboProject
            = (ComboProject)session.getAttribute("beanComboProject");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<TITLE>Defect History</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT language="javascript">
function doBack() {
    var form = document.frmDefectHistory;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "UpdateDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doQueryListing() {
    var form = document.frmDefectHistory;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<DIV>
<P><IMG border="0" src="Images/ViewHistory.gif" width="411" height="28"></P>
</DIV>
<FORM method="POST" action="DMSServlet" name="frmDefectHistory">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="UserName" value="<%=beanUserInfo.getUserName()%>">
<INPUT type="hidden" name="Account" value="<%=beanUserInfo.getAccount()%>">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="Position" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="ProjectCode" value="<%=beanUserInfo.getProjectCode()%>">
<INPUT type="hidden" name="DateLogin" value="<%=beanUserInfo.getDateLogin()%>">
<INPUT type="hidden" name="ProjectID" value="<%=beanUserInfo.getProjectID()%>">
<TABLE border="0" width="100%">
    <TR>
        <TD align="right"><A href="javascript:doQueryListing()">View DefectListing</A></TD>
    </TR>
</TABLE>
<TABLE border="0" width="100%" class="TblOut2">
    <TR>
        <TD width="8%"><B>User:</B></TD>
        <TD width="24%"><%=beanUserInfo.getUserName()%></TD>
        <TD width="12%"><B>Login Date:</B></TD>
        <TD width="25%"><%=beanUserInfo.getDateLogin()%></TD>
        <TD width="9%"><B>Project</B></TD>
        <TD width="22%" align="right"><SELECT name="cboProjectList" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="DefectHistory"%>','<%=""%>');"><%
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
        <TD width="22%" align="right"><SELECT name="cboProjectStatus" class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="DefectHistory"%>','<%=""%>');"><%
    String strCurrentStatus = beanUserInfo.getCurrentStatus();
    for (int i = 0; i < beanComboProject.getStatusList().getNumberOfRows(); i++) {
        StringMatrix smStatus = beanComboProject.getStatusList();
        out.write("<OPTION ");
        out.write(strCurrentStatus.equals(smStatus.getCell(i,0)) ? " selected " : " ");
        out.write("value=\"" + smStatus.getCell(i, 0) + "\">" + smStatus.getCell(i, 1) + "</OPTION>");
    }
%>
        </SELECT></TD>

    </TR>
</TABLE>
<P></P>
<TABLE border="0" cellspacing="1" cellpadding="0" width="100%" bgcolor="#000000">
    <TR class="Row0" height="19">
        <TD valign="middle" align="center" width="100%"><%=beanUserInfo.getProjectCode() + " " + beanDefectHistory.getDefectID()%></TD>
    </TR>
    <TR class="Row2" height="19">
        <TD valign="middle" align="left" width="100%"><%=beanDefectHistory.getHistory()%></TD>
        <INPUT type="hidden" name="hidDefectID" value="<%=beanDefectHistory.getDefectID()%>">
    </TR>
</TABLE>
<P><INPUT type="button" name="Back" class="button" onclick="javascript:doBack()" value="Back"></P>
</FORM>
</BODY>
</HTML>