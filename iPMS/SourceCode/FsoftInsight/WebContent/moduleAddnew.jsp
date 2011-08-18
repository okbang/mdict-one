<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>moduleAddnew.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.category.focus();">
<%
	int schedRight=Security.getRights("Schedule",request);
	Vector workProductList = (Vector)session.getAttribute("workProductList");
	
	String fromPage = (String) request.getAttribute("fromPage");
	String anchor = "";
	if (fromPage == null) {
		fromPage ="";
		anchor = "modulelist";
	} else {
		request.removeAttribute("fromPage");
		anchor = "revtest";
	}
	
	Vector languageList = (Vector)session.getAttribute("languageList");
	Vector methodList = (Vector)session.getAttribute("methodList");
	Vector relevantLanguageList = (Vector)session.getAttribute("relevantLanguageList");
	Vector relevantMethodList = (Vector)session.getAttribute("relevantMethodList");
%>

<SCRIPT language="javascript">
	arrEstimationMethod = new Array(<%=methodList.size()%>);
	arrEstimationMethodId = new Array(<%=methodList.size()%>);
	arrLanguage = new Array(<%=languageList.size()%>);
	arrLanguageId = new Array(<%=languageList.size()%>);
<%	for(int i=0;i<methodList.size();i++){
		EstimationMethodInfo emInfo = (EstimationMethodInfo) methodList.elementAt(i);
		%>arrEstimationMethod[<%=i%>] = "<%=emInfo.name%>";
		arrEstimationMethodId[<%=i%>] = "<%=emInfo.methodID%>";
	<%}
	for(int i=0;i<languageList.size();i++){
		LanguageInfo langInfo = (LanguageInfo) languageList.elementAt(i);
		%>arrLanguage[<%=i%>] = "<%=langInfo.name+" "+langInfo.sizeUnit%>";
		arrLanguageId[<%=i%>] = "<%=langInfo.languageID%>";
<%}%>
	
	function selectAll()
	{
	    var myElement;
	    for (var i = frm.estUnitID.options.length; i >= 1; i--){
	        frm.estUnitID.options[i] = null;
	    }
	    for (var i = frm.actUnitID.options.length; i >= 1; i--){
	        frm.actUnitID.options[i] = null;
	    }
	    for (var i = 0; i < arrEstimationMethod.length; i++){
            myElement = document.createElement("option");
            myElement.value = "1" + arrEstimationMethodId[i];
            myElement.text = arrEstimationMethod[i];
            frm.estUnitID.add(myElement);
	    }
        for (var i = 0; i < arrLanguage.length; i++){
            myElement = document.createElement("option");
            myElement.value = "0" + arrLanguageId[i];
            myElement.text = arrLanguage[i];
            frm.estUnitID.add(myElement);
	    }
	}
	
</SCRIPT> 

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Productsize")%> </P>
<BR>
<FORM name="frm" action="Fms1Servlet#<%=anchor%>">
<TABLE class="Table" cellspacing="1" width="560">
    <COL span="1" width="160">
    <COL span="1" width="400">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Addnewproductsize")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Workproduct")%>* </TD>
            <TD class="CellBGR3"><SELECT class="COMBO" name="category" onchange="onWPChange()">
                <OPTION value="N/A"></OPTION>
            	<%for (int i = 0; i < workProductList.size(); i++) {
            		WorkProductInfo wpInfo = (WorkProductInfo)workProductList.elementAt(i);
            	%><OPTION value="<%=wpInfo.workProductID%>"><%=languageChoose.getMessage(wpInfo.workProductName)%></OPTION>
                <%}%>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Name")%>* </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="name"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Plannedsizeunit")%>* </TD>
            <TD class="CellBGR3"><SELECT class="COMBO" name="estUnitID" onchange="onEstUnitChange()">
                <OPTION value="N/A"></OPTION>
	            <%
	        	for (int i = 0; i < relevantMethodList.size(); i++){
	        		EstimationMethodInfo emInfo = (EstimationMethodInfo) relevantMethodList.elementAt(i);
	        		%><OPTION value="1<%=emInfo.methodID%>"><%=emInfo.name%></OPTION>
	            <%}
	        	for (int i = 0; i < relevantLanguageList.size(); i++){
	        		LanguageInfo langInfo = (LanguageInfo) relevantLanguageList.elementAt(i);
	        		%><OPTION value="0<%=langInfo.languageID%>"><%=langInfo.name+" "+langInfo.sizeUnit%></OPTION>
	            <%}%>
            </SELECT><A href="javascript:selectAll()">  <%=languageChoose.getMessage("fi.jsp.moduleAddnew.more")%>  </A></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Plannedsize")%>* </TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="estSize"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Replannedsize")%> </TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="reestSize"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Actualsizeunit")%> </TD>
            <TD class="CellBGR3"><SELECT class="COMBO" name="actUnitID">
                <OPTION value="N/A"></OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Actualsize")%> </TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="actSize"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Reuse")%> </TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="reuse">%</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Description")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="desc"></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<P>
<INPUT type="button" class="BUTTON" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.moduleAddnew.OK")%> " onclick="onOK('<%=fromPage%>')">
<INPUT type = "hidden" name ="fromPage" value = "<%=fromPage%>"/>
<%if (schedRight==3)%><INPUT type="button" class="BUTTONWIDTH" value=" <%=languageChoose.getMessage("fi.jsp.moduleAddnew.OKupdateschedule")%> " onclick="onOK('sched')">
<INPUT type="button" class="BUTTON" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Cancel")%> " onclick="doIt(<%=(fromPage == null) ? Constants.MODULE_LIST:Constants.GET_QUALITY_OBJECTIVE_LIST%>);"></P>
<INPUT type="hidden" name="reqType" value="<%=Constants.MODULE_ADDNEW%>">

</FORM>
<SCRIPT language="javascript">

var langLen = <%=relevantLanguageList.size()%>;
var methodLen = <%=relevantMethodList.size()%>;

function onEstUnitChange() {
	if (frm.estUnitID.value.substring(0, 1) == "1") {
		frm.actUnitID.length = langLen + 1;
		frm.actUnitID.options[1].value = frm.estUnitID.options[frm.estUnitID.selectedIndex].value;
		frm.actUnitID.options[1].text = frm.estUnitID.options[frm.estUnitID.selectedIndex].text;
		for (var i = 2; i < frm.actUnitID.length; i ++) {
			frm.actUnitID.options[i].value = frm.estUnitID.options[i + methodLen].value;
			frm.actUnitID.options[i].text = frm.estUnitID.options[i + methodLen].text;
		}
	}
	else {
		frm.actUnitID.length = 2;
		frm.actUnitID.options[1].value = frm.estUnitID.options[frm.estUnitID.selectedIndex].value;
		frm.actUnitID.options[1].text = frm.estUnitID.options[frm.estUnitID.selectedIndex].text;
	}
}

function onWPChange() {
	frm.name.focus();
	frm.name.value = frm.category.options[frm.category.selectedIndex].text;
}

function onOK(dowhat) {
	if (trim(frm.name.value) == "") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleAddnew.Modulenamecannotbeempty")%>");
		frm.name.focus();
		return;
	}
	
	if (frm.category.value == "N/A") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleAddnew.Categorynamecannotbeempty")%>");
		frm.category.focus();
		return;
	}
	
	if (frm.estUnitID.value == "N/A") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleAddnew.Plannedunitcannotbeempty")%>");
		frm.estUnitID.focus();
		return;
	}
	if (frm.estSize.value == ""){
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleAddnew.Plannedunitcannotbeempty")%>");
		frm.estSize.focus();
		return;
	}
	if (frm.estUnitID.value == <%=LanguageInfo.NUMBER_OF_TEST_CASE%>){
		if (!integerFld(frm.estSize, "<%=languageChoose.getMessage("fi.jsp.moduleAddnew.Plannedsize")%>")){
			return;
		}
		if (frm.reestSize.value != "" && !integerFld(frm.reestSize, "<%=languageChoose.getMessage("fi.jsp.moduleAddnew.Replannedsize")%>")){
			return;
		}
	}
	if (isNaN(frm.estSize.value) || (frm.estSize.value < 0)){
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleAddnew.Plannedsizemustbeapositivenumber")%>");
		frm.estSize.focus();
		return;
	}
	if ((trim(frm.reestSize.value) != "") && (isNaN(frm.reestSize.value) || (frm.reestSize.value < 0))) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleAddnew.Replannedsizemustbeapositivenumber")%>");
		frm.reestSize.focus();
		return;
	}

	if ((frm.actUnitID.value == "N/A") && (trim(frm.actSize.value) != "") ||
		(frm.actUnitID.value != "N/A") && (trim(frm.actSize.value) == "")) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleAddnew.Actualsizeunitandactualsizeboth")%>");
		if (frm.actUnitID.value == "N/A"){
			frm.actUnitID.focus();
		}
		else{
			frm.actSize.focus();
		}
		return;
	}

	if (frm.actUnitID.value != "N/A" && frm.actUnitID.value == <%=LanguageInfo.NUMBER_OF_TEST_CASE%>){
		if (!integerFld(frm.actSize, "<%=languageChoose.getMessage("fi.jsp.moduleAddnew.Actualsizeunit")%>")){
			return;
		}
	}

	if ((trim(frm.actSize.value) != "") && (isNaN(frm.actSize.value) || (frm.actSize.value < 0))) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleAddnew.Actualsizemustbeapositivenumber")%>");
		frm.actSize.focus();
		return;
	}
	if ((trim(frm.actSize.value) != "") && frm.category.options[frm.category.selectedIndex].value==<%=WorkProductInfo.SOFTWARE_PACKAGE%> && frm.actSize.value ==0) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleAddnew.Softwarepackageactualsizemustbeabove")%>");
		frm.actSize.focus();
		return;
	}
	if ((trim(frm.reuse.value) != "") && (isNaN(frm.reuse.value) || (frm.reuse.value < 0) || (frm.reuse.value > 100))) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleAddnew.Reusemustbeintherange")%>");
		frm.reuse.focus();
		return;
	}
	
	if (frm.desc.value.length > 200) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleAddnew.Descriptioncannotbelonger")%>");
		frm.desc.focus();
		return;
	}
	
	if (dowhat=='sched')
		frm.reqType.value='<%=Constants.SCHE_REVIEW_TEST_UPDATE2%>';
	frm.submit();
}
</SCRIPT>
</BODY>
</HTML>
