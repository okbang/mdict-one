<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@
    page language="java"
    import="java.util.*,javax.servlet.*,
    		fpt.dms.bean.*,
            fpt.dms.bean.DefectManagement.*,
            fpt.dms.bean.ProjectEnvironment.*,
            fpt.dms.bo.combobox.*,
            fpt.dms.framework.util.StringUtil.*,
            fpt.dms.framework.util.CommonUtil.*,
            fpt.dms.constant.*" %><%@
    page isThreadSafe="false" errorPage="error.jsp"  contentType="text/html;charset=UTF-8"%><%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    ComboProject beanComboProject =
    		(ComboProject) session.getAttribute("beanComboProject");
    DefectAttachBean beanDefectAttach =
    		(DefectAttachBean) session.getAttribute("beanDefectAttach");

    // Summary attachment informations
    String strAvailableSize =
    	CommonUtil.roundFormat(beanDefectAttach.getAvailableSize() / (float) 1024, 2) + "&nbsp;Kb";
    if (beanDefectAttach.getAvailableSize() <= 0) {
    	strAvailableSize = "<FONT color=red>" + strAvailableSize + "</FONT>";
    }
    String strCurrentSize =
    	CommonUtil.roundFormat(beanDefectAttach.getCurrentSize() / (float) 1024, 2) + "&nbsp;Kb";
    String strFiles = beanDefectAttach.getFilesNumber() + "/" + DMS.MAX_UPLOAD_FILES;
    
    // Just posted files
    String strNewSize = "";
    if (session.getAttribute(DMS.UPLOAD_NEW_SIZE) != null) {
    	strNewSize +=  (String) session.getAttribute(DMS.UPLOAD_NEW_SIZE) + "&nbsp;Kb";
    }
    String strNewFiles = "";
    if (session.getAttribute(DMS.UPLOAD_NEW_FILES) != null) {
    	strNewFiles +=  (String) session.getAttribute(DMS.UPLOAD_NEW_FILES);
    }
    // Upload message
    String strMessage = (session.getAttribute(DMS.MSG_ATTACH_MESSAGE) != null ?
    					((String) session.getAttribute(DMS.MSG_ATTACH_MESSAGE)) :
    					"");
    // Detect original posted form: Add new form or Update form
    String strActionDetail = "";
    if (beanDefectAttach.getAttachMode() == DefectAttachBean.MODE_ADD_NEW) {
    	strActionDetail = DMS.DEFECT_ATTACH_NEW_DONE;
    }
    else if (beanDefectAttach.getAttachMode() == DefectAttachBean.MODE_UPDATE) {
    	strActionDetail = DMS.DEFECT_ATTACH_UPDATE_DONE;
    }
    
    StringMatrix smProjectStatus = beanComboProject.getStatusList();
    String strCurrentStatus = beanUserInfo.getCurrentStatus();
    
%>
<HTML>
<HEAD>
<TITLE>Upload Result</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT language="javascript">
function doOk() {
    var form = document.forms[0];
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "<%=strActionDetail%>";
    form.action = "DMSServlet";
    form.submit();
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<FORM method="post" action="DMSServlet" name="frmAttachmentResult">
<DIV><IMG border="0" src="Images/DefectAttach.gif" width="411" height="28"></DIV>
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidTypeOfView" value="<%=beanUserInfo.getTypeOfView()%>">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
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
        <TD width="22%" align="right"><SELECT name="cboProjectList" disabled class="SmallCombo" onchange="javascript:doChangeProject('DM','ImportDefect','');"><%
    int nCurrentProjectID = beanUserInfo.getProjectID();
    int nProjectID;
    for (int i = 0; i < beanComboProject.getListing().getNumberOfRows(); i++) {
        nProjectID = Integer.parseInt(beanComboProject.getListing().getCell(i, 0));
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
        <TD width="22%" align="right"><SELECT name="cboProjectStatus" disabled class="SmallCombo" onchange="javascript:doChangeProject('DM','ImportDefect','');"><%
    for (int i = 0; i < smProjectStatus.getNumberOfRows(); i++) {
        out.write("<OPTION ");
        out.write(strCurrentStatus.equals(smProjectStatus.getCell(i,0)) ? " selected " : " ");
        out.write("value=\"" + smProjectStatus.getCell(i, 0) + "\">" + smProjectStatus.getCell(i, 1) + "</OPTION>");
    }
%>        
        </SELECT></TD>

    </TR>
</TABLE>
<BR>
<TABLE border="0" width="80%" cellspacing="5" cellpadding="0" align="center">
    <TR>
        <TD><%=strMessage%></TD>
    </TR>
</TABLE>
<TABLE width="80%" cellspacing="5" cellpadding="0" align="center" class="TblOut2">
    <COLGROUP>
        <COL width="50%">
        <COL width="50%">
    <TR>
        <TD valign="top">
            <TABLE border="0" width="100%" cellspacing="0" cellpadding="0">
                <TR>
                    <TD align="left"><B>New files</B></TD>
                    <TD align="left"><%=strNewFiles%></TD>
                </TR>
                <TR>
                    <TD align="left"><B>New size</B></TD>
                    <TD align="left"><%=strNewSize%></TD>
                </TR>
            </TABLE>
        </TD>
        <TD valign="top">
            <TABLE border="0" width="100%" cellspacing="0" cellpadding="0">
                <TR>
                    <TD align="left"><B>Total files</B></TD>
                    <TD align="left"><%=strFiles%></TD>
                </TR>
                <TR>
                    <TD align="left"><B>Total size</B></TD>
                    <TD align="left"><%=strCurrentSize%></TD>
                </TR>
                <TR>
                    <TD align="left"><B>Available size</B></TD>
                    <TD align="left"><%=strAvailableSize%></TD>
                </TR>
            </TABLE>
        </TD>
    <TR>
</TABLE>
<P></P>
<TABLE border="0" cellspacing="1" cellpadding="1" width="80%" align="center">
    <TR>
        <TD align="left">
        	<INPUT type="button" class="button" onclick="javascript:doOk()" value="Done">
        </TD>
        <TD align="left"></TD>
        <TD align="left"></TD>
        <TD align="left"></TD>
    </TR>
</TABLE>
</FORM>
</BODY>
</HTML>