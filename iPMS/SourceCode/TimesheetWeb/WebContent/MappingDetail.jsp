<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="javax.servlet.*, fpt.timesheet.bean.*,
    fpt.timesheet.bean.Approval.*,
    fpt.timesheet.framework.util.StringUtil.*,
    fpt.timesheet.bean.Mapping.MappingDetailBean,
    java.util.Collection,
    java.util.Iterator"
%>
<%@ page isThreadSafe="true" errorPage="error.jsp"  contentType="text/html; charset=UTF-8"%>
<%
    UserInfoBean beanUserInfo = (UserInfoBean)session.getAttribute("beanUserInfo");
    MappingDetailBean beanMappingDatail = (MappingDetailBean)session.getAttribute("beanMappingDatail");
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
function doReset(strValue, strText){
    var form = document.forms[0];
    var src = form.lstCurrentWorkProduct.options;
    var listValue = [];
    var listText = [];
    var itemValues = strValue.split(',');
    var itemTexts = strText.split(',');
    for(var i=0; i<  itemValues.length; i++){
        listValue[listValue.length] = itemValues[i];
        listText[listText.length] = itemTexts[i];
    }

    while(src.length>0){
        src[0] = null;
    }

    for (var j = 0; j < listValue.length; j++) {
        if (listValue[j] == "") {
            continue;
        }
        src[src.length] = new Option(listText[j], listValue[j]);
    }
}

function doSave(){
    var form = document.forms[0];
    var sep = ",";
    var strCurrentWorkProductIDList = "";
    var src = form.lstCurrentWorkProduct.options;
    for(var i=0; i<src.length; i++){
        if(i>0){
            strCurrentWorkProductIDList += sep;
        }
        strCurrentWorkProductIDList += src[i].value;
    }

    form.hidCurrentWorkProductIDList.value = strCurrentWorkProductIDList;
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "MappingSave";
    form.action = "TimesheetServlet";
    form.submit();
}

function doBack(){
    var form = document.forms[0];
    form.hidAction.value = "AA";
    form.hidActionDetail.value = "MappingList";
    form.action = "TimesheetServlet";
    form.submit();
}

function clickAdd(){
    var form = document.forms[0];
    var currentWorkProduct = form.lstWorkProduct.options;
    var currentSelectedWorkProduct = form.lstCurrentWorkProduct.options;
    addOptions(currentWorkProduct, currentSelectedWorkProduct);

}

function clickRemove(){
    var form = document.forms[0];
    var currentWorkProduct = form.lstCurrentWorkProduct.options;
    removeOptions(currentWorkProduct);
}


function addOptions(src, dst){
    var valueList = [];
    var textList = [];

    for (var i = 0; i < dst.length; i++) {
        valueList[valueList.length] = dst[i].value;
        textList[textList.length] = dst[i].text;
    }

    for(var i=0; i<src.length; i++) {
        if(src[i].selected){
            var exists = 0;
            for (var j = 0; j < dst.length; j++) {
                if (dst[j].value == src[i].value) {
                    exists = 1;
                    break;
                }
            }
            if (!exists) {
                valueList[valueList.length] = src[i].value;
                textList[textList.length] = src[i].text;
            }
        }
    }

    while (dst.length >= 1) {
        dst[0] = null;
    }
    for (var j = 0; j < valueList.length; j++) {
        if (valueList[j] == "") {
            continue;
        }
        dst[dst.length] = new Option(textList[j], valueList[j]);
    }
}

function removeOptions(src){
    for (var i = 0; i < src.length; i++) {
        if (src[i].selected) {
            src[i--] = null;
        }
    }
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

<INPUT type="hidden" name="hidMappingState" value="<%=beanMappingDatail.getCurrentState()%>">
<INPUT type="hidden" name="hidCurrentWorkProductIDList" value="">
<INPUT type="hidden" name="hidCurrentProcessName" value="<%=beanMappingDatail.getCurrentProcessName()%>">
<INPUT type="hidden" name="hidHistoryList" value="">

<DIV>&nbsp;&nbsp;<FONT class="label1" color="#ffffff">User&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getFullName()%></FONT><BR>
&nbsp;&nbsp;<FONT class="label1" color="#ffffff">Role&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label1" color="yellow"><%=beanUserInfo.getRoleName()%></FONT>
<HR>

<DIV><CENTER><FONT class="label3" color="#ffffff">Process:&nbsp;&nbsp;&nbsp;</FONT>
<FONT class="label3" color="yellow"><%=beanMappingDatail.getCurrentProcessName()%></FONT><BR>
</CENTER>
<INPUT type="hidden" name="cboProcess" value="<%=beanMappingDatail.getCurrentProcessID()%>">

<CENTER>
<TABLE border="0">

    <TBODY>
        <TR>
            <TD width="43%" align="right">
            <SELECT name="lstWorkProduct" multiple="multiple" size="15" style="width: 200">
            <%
            for(int i=0; i<beanMappingDatail.getWorkProductList().getNumberOfRows(); i++){
            %>
            <OPTION value="<%=beanMappingDatail.getWorkProductList().getCell(i,0)%>"><%=beanMappingDatail.getWorkProductList().getCell(i,1)%> </OPTION>

            <%
            }

            %>
            </SELECT>

            </TD>
            <TD width="14%"><CENTER>
            <TABLE border="0">
                <TBODY>
                    <TR>
                        <TD>
                        <INPUT type="button" name="btnSeclect" value="-->" class="Button" onclick="clickAdd()">
                        </TD>
                    </TR>
                    <TR>
                        <TD><BR></TD>
                    </TR>

                    <TR>
                        <TD>
                        <INPUT type="button" name="btnDeSeclect" value="<--" class="Button" onclick="clickRemove()">
                        </TD>
                    </TR>
                </TBODY>
            </TABLE>

            </CENTER></TD>
            <TD width="43%" align="left">
            <SELECT name="lstCurrentWorkProduct" multiple="multiple" size="15" style="width: 200">
            <%
            for(int i=0; i<beanMappingDatail.getCurrentWorkProductList().getNumberOfRows(); i++){
            %>
            <OPTION value="<%=beanMappingDatail.getCurrentWorkProductList().getCell(i,0)%>"><%=beanMappingDatail.getCurrentWorkProductList().getCell(i,1)%> </OPTION>

            <%
            }

            %>

            </SELECT>

            </TD>
        </TR>

    </TBODY>

</TABLE>

</CENTER>
<%
    String strValue = "";
    String strText = "";
    for(int i=0; i<beanMappingDatail.getCurrentWorkProductList().getNumberOfRows();  i++){
        if(i>0){
            strValue += ",";
            strText += ",";
        }
        strValue += beanMappingDatail.getCurrentWorkProductList().getCell(i, 0);
        strText += beanMappingDatail.getCurrentWorkProductList().getCell(i,1);
    }
%>

<P align="center">
<%
if(beanMappingDatail.getSavingResult() != 0){
    %><FONT class="label1" color="#ffffff">
    You 've added already existed mapping(s) <BR><BR>
    </FONT>
    <%
}

%>
    <INPUT type="button" name="Reset"
    onclick="javascript:doReset('<%=strValue%>','<%=strText%>')" value="Reset" class="Button"> <INPUT
    type="button" name="Save" onclick="javascript:doSave()" value="Save"
    class="Button"> <INPUT type="button" name="Back"
    onclick='javascript:doBack()' value="Back" class="Button"></P>
</DIV>
</FORM>
</BODY>
</HTML>