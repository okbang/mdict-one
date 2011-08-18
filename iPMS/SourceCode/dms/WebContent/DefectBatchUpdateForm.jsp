<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@
    page language="java"
    import="java.util.*,javax.servlet.*, fpt.dms.bean.*,
            fpt.dms.bean.DefectManagement.*,
            fpt.dms.bo.combobox.*,
            fpt.dms.framework.util.StringUtil.*,fpt.dms.framework.util.DateUtil.*,
            fpt.dms.constant.*" 
%>
<%@
    page isThreadSafe="false" errorPage="error.jsp" contentType="text/html;charset=UTF-8"
%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    DefectBatchUpdateBean beanDefectBatchUpdate = (DefectBatchUpdateBean)request.getAttribute("beanDefectBatchUpdate");
    ComboProject beanComboProject = (ComboProject)session.getAttribute("beanComboProject");
%>
<HTML>
<HEAD>
<SCRIPT src="scripts/CommonScript.js"></SCRIPT>
<SCRIPT src="scripts/utils.js"></SCRIPT>
<SCRIPT src="scripts/popcalendar.js"></SCRIPT>
<TITLE>Batch Update Defect</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="StyleSheet" href="styles/DMSStyleSheet.css" type="text/css">
<LINK rel="StyleSheet" href="styles/pcal.css" type="text/css">
</HEAD>
<BODY bgcolor="#FFFFFF">
<SCRIPT LANGUAGE = "JavaScript">
	function selectAssignTo(index) {	
		var oldvalue = frmDefectBatchUpdate.oldstatus[index].value;
		if(oldvalue == "1") {
			frmDefectBatchUpdate.newstatus[index].value = "2";
		}
		else {
			if (frmDefectBatchUpdate.cboAssignTo[index].value == "") {
				frmDefectBatchUpdate.newstatus[index].value = "1";
	 		}
	 		else {
				frmDefectBatchUpdate.newstatus[index].value = oldvalue;
	 		}
	 	}
	} 
</SCRIPT>
<DIV align = "left"><%@ include file = "HeaderPage.jsp"%></DIV>
<DIV>
<P><IMG border="0" src="Images/DefectBatchUpdate.gif" width="411" height="28"></P>
</DIV>
<FORM method="POST" action="DMSServlet" name="frmDefectBatchUpdate">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="CheckRole" value="<%=beanUserInfo.getPosition()%>">
<INPUT type="hidden" name="hidTypeOfView" value="<%=beanUserInfo.getTypeOfView()%>">
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
        <TD width="22%" align="right">
        <SELECT name="cboProjectList" disabled class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="BatchUpdateDefect"%>','<%=""%>');">
<%
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
        <TD width="22%" align="right">
        <SELECT name="cboProjectStatus" disabled class="SmallCombo" onchange="javascript:doChangeProject('<%="DM"%>','<%="BatchUpdateDefect"%>','<%=""%>');">
<%
    String strCurrentStatus = beanUserInfo.getCurrentStatus();
    for (int i = 0; i < beanComboProject.getStatusList().getNumberOfRows(); i++) {
        StringMatrix smStatus = beanComboProject.getStatusList();
        out.write("<OPTION ");
        out.write(strCurrentStatus.equals(smStatus.getCell(i,0)) ? " selected " : " ");
        out.write("value=\"" + smStatus.getCell(i, 0) + "\">" + smStatus.getCell(i, 1) + "</OPTION>");
    }
%>
        </SELECT>
        </TD>
    </TR>
</TABLE>
<P></P>
<TABLE border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000">
    <TR class="Row0" height= "19">
        <TH align="center" valign="middle" width="9%">Defect ID</TH>
        <TH align="center" valign="middle" width="22%">Defect Title</TH>
        <TH align="center" valign="middle" width="11%">Due Date</TH>
        <TH align="center" valign="middle" width="9%">Severity<FONT color="red">*</FONT></TH>
        <TH align="center" valign="middle" width="9%">Priority</TH>
        <TH align="center" valign="middle" width="9%">Defect Owner</TH>
        <TH align="center" valign="middle" width="9%">Assigned To</TH>
     <!--  <TH align="center" valign="middle" width="20%">Corrective Action</TH> -->
    </TR>
    <INPUT type="hidden" name="hidNumberOfRows" value="<%=beanDefectBatchUpdate.getBatchUpdateList().getNumberOfRows()%>">
<%	
	int nStatus = 0;
    int nNumberRow = beanDefectBatchUpdate.getBatchUpdateList().getNumberOfRows();
    for (int nTableRow = 0x00; nTableRow < beanDefectBatchUpdate.getBatchUpdateList().getNumberOfRows(); nTableRow++) {
        int nDefectID = Integer.parseInt(beanDefectBatchUpdate.getBatchUpdateList().getCell(nTableRow, 0));
        	nStatus = Integer.parseInt(beanDefectBatchUpdate.getBatchUpdateList().getCell(nTableRow, 1));
        int nSeverity = Integer.parseInt(beanDefectBatchUpdate.getBatchUpdateList().getCell(nTableRow, 2));
        int nPriority = Integer.parseInt(beanDefectBatchUpdate.getBatchUpdateList().getCell(nTableRow, 3));
        String sAssignTo = beanDefectBatchUpdate.getBatchUpdateList().getCell(nTableRow, 4);
        String strDueDate = beanDefectBatchUpdate.getBatchUpdateList().getCell(nTableRow, 5);
        String strFixedDate = beanDefectBatchUpdate.getBatchUpdateList().getCell(nTableRow, 7);
        String strCorAction = beanDefectBatchUpdate.getBatchUpdateList().getCell(nTableRow, 8);
        String sDefectOwner = beanDefectBatchUpdate.getBatchUpdateList().getCell(nTableRow, 9);
        int nRow = 0; // variable used in FOR statement counter..
%>
    <TR class="Row<%=(nTableRow + 1) % 2 + 1%>">
        <!-- DefectID -->
        <INPUT type="hidden" name="hidDefectID" value="<%=nDefectID%>">
        <INPUT type="hidden" name="txtFixedDate" value="<%=strFixedDate%>">
        <INPUT type="hidden" name="hidFixedDate" value="<%=strFixedDate%>">
        <TD width="9%" align="center"><%=nDefectID%></TD>
        <TD width="22%"><%=beanDefectBatchUpdate.getBatchUpdateList().getCell(nTableRow, 6)%></TD>
        <!-- DueDate -->
        <TD width="11%">
            <INPUT type="text" name="txtDueDate" value="<%=strDueDate%>" size="13" class="DateBox" maxlength="8"><IMG
                src="Images/cal.gif" style="CURSOR:hand"
                onclick=<%=(nNumberRow > 1) ?
                            "'showCalendar(txtDueDate[" + nTableRow + "], txtDueDate[" + nTableRow + "], \"mm/dd/yy\",null,1,-1,-1,true)'" :
                            "'showCalendar(txtDueDate, txtDueDate, \"mm/dd/yy\",null,1,-1,-1,true)'"
                %>>
        </TD>
       <INPUT type="hidden" name="oldstatus" value="<%=nStatus%>">
       <INPUT type="hidden" name="newstatus" value="<%=nStatus%>">
        <!-- Severity -->
        <TD width="9%">
        <SELECT name="cboSeverity" size="1" class="VerySmallCombo">
<%
        for (nRow = 0x00; nRow < beanDefectBatchUpdate.getComboSeverity().getNumberOfRows(); nRow++) {
            String strValue = beanDefectBatchUpdate.getComboSeverity().getCell(nRow, 0);
            int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
            String strText = beanDefectBatchUpdate.getComboSeverity().getCell(nRow, 1);
            out.write("<OPTION");
            out.write(nSeverity == nValue ? " selected " : " ");
            out.write("value=\"" + nValue + "\">" + strText + "</OPTION>");
        }
%>
        </SELECT>
        </TD>
        
        <!-- Priority -->
        <TD width="9%">
        <SELECT name="cboPriority" size="1" class="VerySmallCombo">
<%
        for (nRow = 0x00; nRow < beanDefectBatchUpdate.getComboPriority().getNumberOfRows(); nRow++) {
            String strValue = beanDefectBatchUpdate.getComboPriority().getCell(nRow, 0);
            int nValue = (strValue != null && strValue != "") ? Integer.parseInt(strValue) : 0;
            String strText = beanDefectBatchUpdate.getComboPriority().getCell(nRow, 1);
            out.write("<OPTION");
            out.write(nPriority == nValue ? " selected " : " ");
            out.write(" value=\"" + nValue + "\">" + strText + "</OPTION>");
        }
%>
        </SELECT>
        </TD>
        <!-- Defect Owner -->
        <TD width="9%"><SELECT name="cboDefectOwner" size="1" class="VerySmallCombo">
<%
        for (nRow = 0x00; nRow < beanDefectBatchUpdate.getComboDefectOwner().getNumberOfRows(); nRow++) {
            String strText = beanDefectBatchUpdate.getComboDefectOwner().getCell(nRow, 1);
            out.write("<OPTION");
            out.write(sDefectOwner.toUpperCase().equals(strText.toUpperCase()) ? " selected " : " ");
            out.write("value=\"" + strText + "\">" + strText + "</OPTION>");
        }
%>
        </SELECT>
        </TD>
        <!-- AssignTo -->
        <TD width="9%"><SELECT name="cboAssignTo" size="1" class="VerySmallCombo" onchange = "Javascript : selectAssignTo(<%=nTableRow%>);">
<%
        for (nRow = 0x00; nRow < beanDefectBatchUpdate.getComboAssignTo().getNumberOfRows(); nRow++) {
            String strText = beanDefectBatchUpdate.getComboAssignTo().getCell(nRow, 1);
            out.write("<OPTION");
            out.write(sAssignTo.toUpperCase().equals(strText.toUpperCase()) ? " selected " : " ");
            out.write("value=\"" + strText + "\">" + strText + "</OPTION>");
        }
%>
        </SELECT>
        </TD>
    </TR>
   
<%
    }
%>
 <TR>
     <TD>
	     <INPUT type="hidden" name="oldstatus" value="<%=nStatus%>">    	 
     </TD>
     <TD>
	     <INPUT type="hidden" name="newstatus" value="<%=nStatus%>">
	 </TD>  
	 <TD>
   	 <input type="hidden" name="cboAssignTo">
	 </TD>
    </TR>
</TABLE>
<P></P>
<P><INPUT type="button" name="BatchUpdateStatus" class="button" onclick="javascript:doSave()" value="Save">
&nbsp;&nbsp;&nbsp;&nbsp; <INPUT type="button" name="Back" class="button" onclick="javascript:doBack()" value="Defect List"></P>
</FORM>
<BR>
<BR>
<BR>
<SCRIPT language="javascript">
function doSave() {
    var form = document.frmDefectBatchUpdate;
    if (!isValidForm()) {
        return;
    }
    //GenFixedDate();
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SaveBatchUpdateDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doBack() {
    var form = document.frmDefectBatchUpdate;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "SearchDefect";
    form.action = "DMSServlet";
    form.submit();
}

function doQueryListing() {
    var form = document.frmDefectBatchUpdate;
    form.hidAction.value = "DM";
    form.hidActionDetail.value = "QueryListing";
    form.action = "DMSServlet";
    form.submit();
}

function isValidForm() {
    var count;
    for (count = 0x00; count < document.forms(0).length; count++) {
        if (!isValidControl(document.forms(0).item(count))) {
            return false;
        }
    }
    return true;
}

function isValidControl(control) {
	//var form = document.frmDefectBatchUpdate;
    if (control.name == "txtDueDate") {
        if (control.value.length <= 0 || isDate(control)) {
            return true;
        }
        else {
            return false;
        }
    }
    /*
    else if (control.name == "cboStatus") {
        return isPositiveNumberCombobox(control);
    }*/
    else if (control.name=="cboSeverity") {
        return isPositiveNumberCombobox(control);
    }
    if (control.name == "txtCorAction") {
        if (control.value.length <= 0) {
            alert("Please fill to Corrective Action");
            control.focus();
            return false;
        }
    }
    return true;
}

/*
function CheckRoleStatus(position, numberRow, row) {
    var newValue;
    var oldValue;
    if (numberRow > 1) {
        newValue = document.forms[0].cboStatus[row].value;
        oldValue = document.forms[0].hidCurrentStatus[row].value;
        document.forms[0].txtFixedDate[row].value = document.forms[0].hidFixedDate[row].value;
    }
    else {
        newValue = document.forms[0].cboStatus.value;
        oldValue = document.forms[0].hidCurrentStatus.value;
        document.forms[0].txtFixedDate.value = document.forms[0].hidFixedDate.value;
    }
    var permit = true;
    switch (newValue) { //follow value of status
        case "1": //Error => Cannot assign FixedDate
        case "6": //Cancelled => Clear FixedDate
            if (numberRow > 1) {
                document.forms[0].txtFixedDate[row].value = "";
            }
            else {
                document.forms[0].txtFixedDate.value = "";
            }
            break;
        case "4": //Closed
            if (position.substring(1, 2) != 1 && position.substring(2, 3) != 1 && position.substring(6, 7) != 1) {//tester
                permit = false;
            }
            break;
        case "5": //Accepted
            if (position.substring(2, 3) != 1 && position.substring(6, 7) != 1) {//project leader Or external user
                permit = false;
            }
            break;
    }
    
    if (!permit) {
        if (numberRow > 1) {
            document.forms[0].cboStatus[row].value = oldValue;
        }
        else {
            document.forms[0].cboStatus.value = oldValue;
        }
        alert("You have no permission to change this status!");
    }
 } */

/*
function GenFixedDate() {
    var myForm = document.forms[0];
<    // Status=3: Pending, 4: Tested
if (beanDefectBatchUpdate.getBatchUpdateList().getNumberOfRows() > 1) {%>

    for (var i = 0; i < myForm.cboStatus.length; i++) {
        if ((myForm.txtFixedDate[i].value == "") &&
            (myForm.cboStatus[i].value != myForm.hidCurrentStatus[i].value) &&
            (myForm.cboStatus[i].value == "3" || myForm.cboStatus[i].value == "4"))
        {
            myForm.txtFixedDate[i].value = "<=DateUtil.getCurrentDate()%>";
        }
        else if ((myForm.txtFixedDate[i].value == "") &&
             (myForm.hidFixedDate[i].value != "") &&
             (myForm.cboStatus[i].value == "3" || myForm.cboStatus[i].value == "4"))
        {
            myForm.txtFixedDate[i].value = myForm.hidFixedDate[i].value;
        }
    }
<} else {%>
    if ((myForm.txtFixedDate.value == "") &&
        (myForm.cboStatus.value != myForm.hidCurrentStatus.value) &&
        (myForm.cboStatus.value == "3" || myForm.cboStatus.value == "4"))
    {
        myForm.txtFixedDate.value = "<=DateUtil.getCurrentDate()%>";
    }
    else if ((myForm.txtFixedDate.value == "") &&
             (myForm.hidFixedDate.value != "")
              && (myForm.cboStatus.value == "3" || myForm.cboStatus.value == "4"))
    {
        myForm.txtFixedDate.value = myForm.hidFixedDate.value;
    }
<}%> 
}*/
</SCRIPT>
</BODY>
</HTML>
