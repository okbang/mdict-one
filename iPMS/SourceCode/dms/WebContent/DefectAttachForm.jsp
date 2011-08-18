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
    String strMessage = (String) request.getAttribute("uploadMessage");
    DefectAttachBean beanDefectAttach =
    		(DefectAttachBean) session.getAttribute("beanDefectAttach");
    String strAvailableSize =
    	CommonUtil.roundFormat(beanDefectAttach.getAvailableSize() / (float) 1024, 2) + "&nbsp;Kb";
    if (beanDefectAttach.getAvailableSize() <= 0) {
    	strAvailableSize = "<FONT color=red>" + strAvailableSize + "</FONT>";
    }
    String strCurrentSize =
    	CommonUtil.roundFormat(beanDefectAttach.getCurrentSize() / (float) 1024, 2) + "&nbsp;Kb";
    String strFiles = beanDefectAttach.getFilesNumber() + "/" + DMS.MAX_UPLOAD_FILES;
    String strMaxFileSize =
    	CommonUtil.roundFormat(DMS.MAX_ATTACH_FILE_SIZE / (float) 1024, 2) + "&nbsp;Kb";
    String strAttachLink = beanDefectAttach.writeHtmlLink();
    
    String strActionCancel = "";
    String strActionAttach = "";
    if (beanDefectAttach.getAttachMode() == DefectAttachBean.MODE_ADD_NEW) {
    	strActionCancel = DMS.DEFECT_ATTACH_NEW_CANCEL;
    	strActionAttach = DMS.DEFECT_ATTACH_NEW;
    }
    else if (beanDefectAttach.getAttachMode() == DefectAttachBean.MODE_UPDATE) {
    	strActionCancel = DMS.DEFECT_ATTACH_UPDATE_CANCEL;
    	strActionAttach = DMS.DEFECT_ATTACH_UPDATE;
    }
    
    StringMatrix smProjectStatus = beanComboProject.getStatusList();
    String strCurrentStatus = beanUserInfo.getCurrentStatus();
    String strAttachDisabled = "";
    if ((beanDefectAttach.getFilesNumber() >= DMS.MAX_UPLOAD_FILES) ||
    	(beanDefectAttach.getCurrentSize() >= DMS.MAX_ATTACH_SIZE))
    {
        strAttachDisabled = "disabled";
    }
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<TITLE>Upload Attachment</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT language="javascript">
var arrBlocked = new Array(
	"ade", "adp", "app","asa", "asp", "bas", "bat",
	"cdx", "cer", "chm", "class", "cmd", "com", "cpl", "crt", "csh",
	"dll", "exe", "fxp", "hlp", "hta", "htr", "htw",
	"ida", "idc", "idq", "ins", "isp", "its", "js", "jse", "jsp", "ksh", "lnk",
	"mad", "maf", "mag", "mam", "maq", "mar", "mas", "mat", "mau", "mav", "maw",
	"mda", "mdb", "mde", "mdt", "mdw", "mdz", "msc", "msi", "msp", "mst",
	"ops", "pcd", "pif", "prf", "prg", "printer", "pst", "reg",
	"scf", "scr", "sct", "shb", "shs", "shtm", "shtml", "stm",
	"url", "vb", "vbe", "vbs", "wsc", "wsf", "wsh");
function doAttach() {
    var form = document.forms[0];
    if (!isValidForm()) {
        return;
    }
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "<%=strActionAttach%>";
    form.action = "DMSServlet";
    form.encoding = "multipart/form-data";
    form.submit();
}

function showTempFile(nIndex) {
	var form = document.forms[0];
	showAttachFile(nIndex, form, "<%=DMS.DEFECT_ATTACH_FILE_VIEW%>");
}

function removeTempFile(nIndex) {
    if (confirm("Do you want to remove this temporary attach file?")) {
	    var form = document.forms[0];
		removeAttachFile(nIndex, form, "<%=DMS.DEFECT_ATTACH_FILE_REMOVE_UPLOAD%>");
	}
}

function showDbFile(nIndex) {
	var form = document.forms[0];
	showAttachData(nIndex, form, "<%=DMS.DEFECT_ATTACH_DB_VIEW%>");
}

function removeDbFile(nIndex) {
    if (confirm("Do you want to remove this permanent attach file?\n" +
    			"Note: Attach file will be removed even not save this defect")) {
	    var form = document.forms[0];
		removeAttachData(nIndex, form, "<%=DMS.DEFECT_ATTACH_DB_REMOVE_UPLOAD%>");
	}
}

function doCancel() {
    var form = document.forms[0];
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "<%=strActionCancel%>";
    form.action = "DMSServlet";
    form.submit();
}

function doQueryListing() {
    var form = document.forms[0];
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function getExt(strFileName) {
	var strExt = "";
	if (strFileName != null) {
		if (strFileName.lastIndexOf(".") >= 0) {
			strExt = strFileName.substring(
	                    strFileName.lastIndexOf(".") + 1,
	                    strFileName.length);
		}
	}
	return strExt;
}

function isExec(strExt) {
	var bResult = false;
	for (var i = 0; i < arrBlocked.length; i++) {
		if (strExt.toLowerCase() == arrBlocked[i]) {
			bResult = true;
			break;
		}
	}
	return bResult;
}

function isValidForm() {
	var bResult = true;
	var bFileExisted = false;
    var form = document.forms[0];
    var control;
    for (var i = 0; i < form.length; i++) {
    	control = form.elements[i];
    	if (control.type.toLowerCase() == "file") {
    		if (isExec(getExt(control.value))) {
    			alert("Executable or script not allowed");
    			control.focus();
    			control.select();
    			bResult = false;
    			break;
    		}
    		else if (control.value.length > 0) {
    			bFileExisted = true;
    		}
    	}
    }
    if ((bResult) && (!bFileExisted)) {
    	alert("Please select at least one file to attach");
    	form.file<%=beanDefectAttach.getFilesNumber() + 1%>.focus();
    	bResult = false;
    }
    return bResult;
}
</SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<FORM method="post" action="DMSServlet" name="frmAttachment">
<DIV><IMG border="0" src="Images/DefectAttach.gif" width="411" height="28"></DIV>
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidTypeOfView" value="<%=beanUserInfo.getTypeOfView()%>">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">
<INPUT type="hidden" name="hidFileIndex" value="">
<INPUT type="hidden" name="hidDataIndex" value="">
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
<TABLE width="90%" cellspacing="5" cellpadding="0" align="center" class="TblOut2">
    <COLGROUP>
        <COL width="60%">
        <COL width="40%">
    <TR>
        <TD valign="top">
            <TABLE border="0" width="100%" cellspacing="0" cellpadding="0">
                <TR><%=strAttachLink%>
                </TR>
<%
	for (int iFile = beanDefectAttach.getFilesNumber() + 1; iFile <= DMS.MAX_UPLOAD_FILES; iFile++) {
%>
                <TR>
                    <TD align="left" width="20%"><B>File&nbsp;<%=iFile%></B></TD>
                    <TD align="left" width="80%"><INPUT type="file" name="file<%=iFile%>"
                    title="Please use JPG/JPEG or PNG format to reduce picture attachment size"></TD>
                </TR><%
	}%>
            </TABLE>
        </TD>
        <TD valign="top">
            <TABLE border="0" width="100%" cellspacing="0" cellpadding="0">
                <COLGROUP>
                    <COL width="60%">
                    <COL width="40%">
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
                <TR>
                    <TD align="left"><B>Max file size (*)</B></TD>
                    <TD align="left"><%=strMaxFileSize%></TD>
                </TR>
            </TABLE>
        </TD>
    </TR>
    <TR>
        <TD align="left"><FONT color="blue">(*) Save images as JPG/JPEG or PNG format to reduce attachment size</FONT></TD>
    </TR>
</TABLE>
<P></P>
<TABLE border="0" cellspacing="1" cellpadding="1" width="80%" align="center">
    <TR>
        <TD align="left">
        	<INPUT type="button" class="button" name="Attach" <%=strAttachDisabled%> onclick="javascript:doAttach()" value="Attach">
        	<INPUT type="button" class="button" name="Cancel" onclick="javascript:doCancel()" value="Cancel">
        </TD>
        <TD align="left"></TD>
        <TD align="left"></TD>
        <TD align="left"></TD>
    </TR>
</TABLE>
</FORM>
</BODY>
</HTML>