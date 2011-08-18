<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, fpt.timesheet.bean.*,
    fpt.timesheet.bean.Approval.*,
    fpt.timesheet.bean.Mapping.*,
    fpt.timesheet.framework.util.StringUtil.*,
    java.util.Collection,
    java.util.Iterator" 
%>
<%@
    page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    MappingListBean beanMappingList = (MappingListBean)session.getAttribute("beanMappingList");
%>
<HTML>
<HEAD>
<TITLE>Process-Work Product Mapping</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK rel="stylesheet" type="text/css" href="styles/tsStyleSheet.css">
<STYLE type="text/css">
A:link {
    COLOR: #ffffff;
    FONT-WEIGHT: normal;
    TEXT-DECORATION: none;
    font-size: 11px;
    font-family: tahoma, sans-serif;
    FONT-WEIGHT: bold;
}

A:visited {
    COLOR: #ffffff;
    FONT-WEIGHT: normal;
    TEXT-DECORATION: none;
    font-size: 11px;
    font-family: tahoma, sans-serif;
    FONT-WEIGHT: bold;
}

A:hover {
    COLOR: #ff0000;
    FONT-WEIGHT: normal;
    TEXT-DECORATION: none;
    font-size: 11px;
    font-family: tahoma, sans-serif;
    FONT-WEIGHT: bold;
}

INPUT.flatTextbox {
    BACKGROUND-COLOR: #d6e7ef;
    COLOR: #000066;
    FONT-FAMILY: Arial;
    FONT-SIZE: 11px;
    WIDTH: 30pt;
    BORDER-TOP: #104a7b 1px solid;
    BORDER-LEFT: #104a7b 1px solid;
    BORDER-BOTTOM: #afc4d5 1px solid;
    BORDER-RIGHT: #afc4d5 1px solid;
    HEIGHT: 20px
}
</STYLE>
<SCRIPT src='scripts/CommonScript.js'></SCRIPT>
<SCRIPT src='scripts/validate.js'></SCRIPT>
<SCRIPT language="javascript">
function doSave() {
    var form = document.forms[0];
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "SaveJavaScript";
    form.action = "TimesheetServlet";
    form.submit();
}

function doAdd() {
    var form = document.forms[0];
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "MappingAdd";
    form.action = "TimesheetServlet";
    form.hidMappingState.value = "Adding";
    form.submit();
}

function doDelete() {
    var form = document.forms[0];
    var sep = ",";
    var count = 0;
    var strCurrentWorkProductIDList = "";
    var src = form.lstCurrentWorkProduct.options;
    for(var i=0; i<src.length; i++){
        if(src[i].selected){
            if(i>0){
                strCurrentWorkProductIDList += sep;
            }
            strCurrentWorkProductIDList += src[i].value;
            count++;
        }
    }

    if(count == 0){
        alert("Please select some process/product mapping.");
        return;
    }
    form.hidCurrentWorkProductIDList.value = strCurrentWorkProductIDList;

    form.hidAction.value = "AA";
    form.hidActionDetail.value = "MappingDelete";
    form.action = "TimesheetServlet";
    form.submit();
}

function doUpdate() {
    var form = document.forms[0];
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "MappingUpdate";
    form.action = "TimesheetServlet";
    form.hidMappingState.value = "Updating";
    form.submit();
}

function doView(){
    var form = document.forms[0];
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "MappingList";
    form.action = "TimesheetServlet";
    form.submit();

}
</SCRIPT>
</HEAD>
<BODY bgcolor="#336699" leftmargin="0" topmargin="0" style="FONT-FAMILY: tahoma, sans-serif; FONT-SIZE: 11px">
<DIV align="left"><%@ include file="HeaderPage.jsp"%></DIV>
<H1><IMG align="top" src="image/tit_wpmapping.gif"></H1>
<FORM method="post" action="TimesheetServlet" name="frmMapList">
<INPUT type="hidden" name="hidAction" value="">
<INPUT type="hidden" name="hidActionDetail" value="">
<INPUT type="hidden" name="Role" value="<%=beanUserInfo.getRole()%>">

<INPUT type="hidden" name="hidMappingState" value="">
<INPUT type="hidden" name="hidCurrentProcessName" value="<%=beanMappingList.getCurrentProcessName()%>">
<INPUT type="hidden" name="hidCurrentWorkProductIDList" value="">

<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName()%></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR>
<CENTER>
<TABLE>
    <TR>
        <TD width="100%">
        <FONT class="label3" color="#ffffff">Process:&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label3" color="yellow"><%=beanMappingList.getCurrentProcessName()%></FONT> <BR><BR>
        </TD>
    </TR>
</TABLE>
</CENTER>
<TABLE border="0" cellpadding="3" cellspacing="0" width="100%" bgcolor="#336699">
    <TR>
    <TD width="50%">
    <TABLE border="0" align="right">
        <TBODY>
            <TR>
                <TD width="100%">
                <SELECT name="cboProcess" class="BigCombo" onchange="javascript:doView();">
                <%
                for(int i=0; i<beanMappingList.getProcessList().getNumberOfRows(); i++){
                    String strValue = beanMappingList.getProcessList().getCell(i, 0);
                    String strSelected;
                    if(strValue != null){
                        strSelected = (strValue.equals(beanMappingList.getCurrentProcessID()) ?  " selected " : "");
                    }
                    else{
                        strSelected = "";
                    }
                    String strText = beanMappingList.getProcessList().getCell(i, 1);
                    out.write("<OPTION value='" + strValue + "' "+ strSelected +">" + strText + "</OPTION>");
                }
                %>
                </SELECT>
                </TD>
            </TR>

        </TBODY>
    </TABLE>
    </TD>
    <TD width="50%">
            <SELECT name="lstCurrentWorkProduct" multiple="multiple" size="15" style="width: 200">
            <%
            for(int i=0; i<beanMappingList.getCurrentWorkProductList().getNumberOfRows(); i++){
            %>
            <OPTION value="<%=beanMappingList.getCurrentWorkProductList().getCell(i, 0)%>"><%=beanMappingList.getCurrentWorkProductList().getCell(i, 1)%> </OPTION>
            <%
            }
            %>
            </SELECT>
    </TD>
    </TR>
</TABLE>

<P align="center">
<INPUT type="button" name="Save" onclick='javascript:doSave()' value="Save" class="Button">
<INPUT type="button" name="Addnew" onclick='javascript:doAdd()' value="Addnew" class="Button">
<INPUT type="button" name="Update" onclick='javascript:doUpdate()' value="Update" class="Button">
<INPUT type="button" name="Delete" onclick='javascript:doDelete()' value="Delete" class="Button"></P>
</DIV>
</FORM>
</BODY>
</HTML>