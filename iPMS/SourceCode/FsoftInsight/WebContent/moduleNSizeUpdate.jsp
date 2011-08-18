<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>moduleNSizeUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
	int schedRight=Security.getRights("Schedule",request);
	
	Vector workProductList = (Vector)session.getAttribute("workProductList");
	Vector languageList = (Vector)session.getAttribute("languageList");
	Vector relevantLanguageList = (Vector)session.getAttribute("relevantLanguageList");
	Vector methodList = (Vector)session.getAttribute("methodList");
	Vector relevantMethodList = (Vector)session.getAttribute("relevantMethodList");
	Vector moduleList = (Vector)session.getAttribute("moduleList");
	String vtID=request.getParameter("vtID");
	WPSizeInfo moduleInfo = (WPSizeInfo)moduleList.elementAt(Integer.parseInt(vtID));

	String reestSize =(moduleInfo.reestimatedSize >= 0)? CommonTools.formatDouble(moduleInfo.reestimatedSize):"";
	String actSize = (moduleInfo.actualSize >= 0)?		CommonTools.formatDouble(moduleInfo.actualSize):"";
	String reuse = (moduleInfo.reusePercentage >= 0)?	CommonTools.formatDouble(moduleInfo.reusePercentage):"";
	String desc = (moduleInfo.description != null)? 	moduleInfo.description:"";

	int acMethodBasedSize;
	Double d = new Double(moduleInfo.acMethodBasedSize);
	acMethodBasedSize = d.intValue();
	
	Double c = new Double(moduleInfo.actualSizeUnitID);
	int actualSizeUnitID;
	actualSizeUnitID = c.intValue();
	boolean fromsched=("1".equals(request.getParameter("fromsched")));

%>
<BODY class="BD" onLoad="<%=((fromsched)?"":"loadPrjMenu()")%>;onLoad()">
<SCRIPT language="javascript">

	estMet = new Array(<%=methodList.size()%>);
	estMetId = new Array(<%=methodList.size()%>);
	lang = new Array(<%=languageList.size()%>);
	langId = new Array(<%=languageList.size()%>);

<%	for(int i=0;i<methodList.size();i++){
		EstimationMethodInfo emInfo = (EstimationMethodInfo) methodList.elementAt(i);
		%>estMet[<%=i%>] = "<%=emInfo.name%>";
		estMetId[<%=i%>] = "<%=emInfo.methodID%>";
	<%}
	for(int i=0;i<languageList.size();i++){
		LanguageInfo langInfo = (LanguageInfo) languageList.elementAt(i);
		%>lang[<%=i%>] = "<%=langInfo.name+" "+langInfo.sizeUnit%>";
		langId[<%=i%>] = "<%=langInfo.languageID%>";
<%}%>
	function selectAll()
	{
	    var myElement;
	    
	    for (var i = frm.estUnitID.options.length; i >= 1; i--)
	        frm.estUnitID.options[i] = null;
	    for (var i = frm.actUnitID.options.length; i >= 1; i--)
	        frm.actUnitID.options[i] = null;
  
        for (var i = 0; i < estMet.length; i++){
            myElement = document.createElement("option");
            myElement.value = "1" + estMetId[i];
            myElement.text = estMet[i];
            frm.estUnitID.add(myElement);
	    }

        for (var i = 0; i < lang.length; i++){
            myElement = document.createElement("option");
            myElement.value = "0" + langId[i];
            myElement.text = lang[i];
            frm.estUnitID.add(myElement);
	    }
		langLen = <%=languageList.size()%>;
		methodLen = <%=methodList.size()%>;

	}
</SCRIPT> 

<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Productsandsize")%> </P>
<FORM name="frm" action="Fms1Servlet#modulelist" method ="POST">
<TABLE class="Table" cellspacing="1" width="560">
    <COL span="1" width="160">
    <COL span="1" width="400">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Updateproductsize")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Workproduct")%>* </TD>
            <TD class="CellBGR3"><SELECT class="COMBO" name="category" onchange="onWPChange()">
                <OPTION value="null"></OPTION>
            	<%
            	for (int i = 0; i < workProductList.size(); i++) {
            		WorkProductInfo wpInfo = (WorkProductInfo)workProductList.elementAt(i);
            		String selected = "";
            		if (moduleInfo.categoryID == wpInfo.workProductID)
            			selected = "selected";
            		%>	<OPTION value="<%=wpInfo.workProductID%>" <%=selected%>><%=languageChoose.getMessage(wpInfo.workProductName)%></OPTION>
                <%}%>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Name")%>* </TD>
            <TD class="CellBGR3"><INPUT size="50" type="text" maxlength="50" name="name" value="<%=moduleInfo.name%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Plannedsizeunit")%>*</TD>
            <TD class="CellBGR3"><SELECT class="COMBO" name="estUnitID" onchange="onEstUnitChange()">
            <OPTION value="null"></OPTION>
	        	<%if (moduleInfo.methodBasedSize == 0){%>
		            <OPTION value="0<%=moduleInfo.estimatedSizeUnitID%>" selected><%=moduleInfo.estimatedSizeUnitName%></OPTION>
	            <%
		        	for (int i = 0; i < relevantLanguageList.size(); i++){
		        		LanguageInfo langInfo = (LanguageInfo) relevantLanguageList.elementAt(i);
	            		if (moduleInfo.estimatedSizeUnitID != langInfo.languageID){
		        		%>	<OPTION value="0<%=langInfo.languageID%>"><%=langInfo.name+" "+langInfo.sizeUnit%></OPTION>
		            <%	}
		            }
		        	for (int i = 0; i < relevantMethodList.size(); i++){
		        		EstimationMethodInfo emInfo = (EstimationMethodInfo) relevantMethodList.elementAt(i);
			        	%>	<OPTION value="1<%=emInfo.methodID%>"><%=emInfo.name%></OPTION>
		            <%}
	
	        	} else {
	        	%>
	            <OPTION value="1<%=moduleInfo.estimatedSizeUnitID%>" selected><%=moduleInfo.estimatedSizeUnitName%></OPTION>
	            <%
		        	for (int i = 0; i < relevantMethodList.size(); i++){
		        		EstimationMethodInfo emInfo = (EstimationMethodInfo) relevantMethodList.elementAt(i);
	            		if (moduleInfo.estimatedSizeUnitID != emInfo.methodID){
		        		%>	<OPTION value="1<%=emInfo.methodID%>"><%=emInfo.name%></OPTION>
		            <%	}
					}
		        	for (int i = 0; i < relevantLanguageList.size(); i++){
		        		LanguageInfo langInfo = (LanguageInfo) relevantLanguageList.elementAt(i);
		        		%>	<OPTION value="0<%=langInfo.languageID%>"><%=langInfo.name+" "+langInfo.sizeUnit%></OPTION>
		            <%}
	            }%>
            </SELECT><A href="javascript:selectAll()">  <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.more")%>  </A></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Plannedsize")%>* </TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="estSize" value="<%=CommonTools.formatDouble(moduleInfo.estimatedSize)%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Replannedsize")%> </TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="reestSize" value="<%=reestSize%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Actualsizeunit")%> </TD>
            <TD class="CellBGR3"><SELECT class="COMBO" name="actUnitID">
                <OPTION value="null"></OPTION>
            </SELECT></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Actualsize")%> </TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="actSize" value="<%=actSize%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Reuse")%>*</TD>
            <TD class="CellBGR3"><INPUT size="11" style="text-align: right" type="text" maxlength="11" name="reuse" value="<%=reuse%>">%</TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Description")%> </TD>
            <TD class="CellBGR3"><TEXTAREA rows="4" cols="50" name="desc"><%=desc%></TEXTAREA></TD>
        </TR>
    </TBODY>
</TABLE>
<P>
<INPUT type="button" class="BUTTON" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Ok")%> " onclick="onOK('save')">
<%if (schedRight==3 && !fromsched && !isArchive){%>
	<INPUT type="button" class="BUTTONWIDTH" value=" <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.OKupdateschedule")%> " onclick="onOK('sched')">
<%}%>
<INPUT type="button" class="BUTTON" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Cancel")%> " onclick="onCancel()">
</P>
<INPUT type="hidden" name="reqType" value="<%=Constants.MODULE_UPDATE%>">
<%if (fromsched){%>
	<INPUT type="hidden" name="fromsched" value="1">
<%}%>
<INPUT type="hidden" name="vtID" value="<%=vtID%>">
</FORM>

<SCRIPT language="javascript">
function onLoad() {
	onEstUnitChange();
	frm.name.focus();
	for (var i = 0; i < frm.actUnitID.length; i++) {
		if (frm.actUnitID.options[i].value == "<%=acMethodBasedSize%><%=actualSizeUnitID%>") {
			frm.actUnitID.options[i].selected = true;
		}
	}
	frm.category.focus();
}

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
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Modulenamecannotbeempty")%>");
		frm.name.focus();
		return;
	}
	
	if (frm.category.value == "null") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Categorynamecannotbeempty")%>");
		frm.category.focus();
		return;
	}
	
	if (frm.estUnitID.value == "null") {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Plannedunitcannotbeempty")%>");
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
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Plannedsizemustbeapositivenumber")%>");
		frm.estSize.focus();
		return;
	}
	
	if ((frm.reestSize.value != "") && (isNaN(frm.reestSize.value) || (frm.reestSize.value < 0))) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Replannedsizemustbeapositivenumber")%>");
		frm.reestSize.focus();
		return;
	}

	if ((frm.actUnitID.value == "null") && (frm.actSize.value != "") ||
		(frm.actUnitID.value != "null") && (frm.actSize.value == "")) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Actualsizeunitandactualsize")%>");
		if (frm.actUnitID.value == "null"){
			frm.actUnitID.focus();
		}
		else{
			frm.actSize.focus();
		}
		return;
	}
	if (frm.actUnitID.value != "null" && frm.actUnitID.value == <%=LanguageInfo.NUMBER_OF_TEST_CASE%>){
		if (!integerFld(frm.actSize, "<%=languageChoose.getMessage("fi.jsp.moduleAddnew.Actualsizeunit")%>")){
			return;
		}
	}
	
	if ((frm.actSize.value != "") && (isNaN(frm.actSize.value) || (frm.actSize.value < 0))) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Actualsizemustbeapositivenumber")%>");
		frm.actSize.focus();
		return;
	}
	if ((frm.actSize.value != "") && frm.category.options[frm.category.selectedIndex].value==<%=WorkProductInfo.SOFTWARE_PACKAGE%> && frm.actSize.value ==0) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Softwarepackageactualsizemustbeabove")%>");
		frm.actSize.focus();
		return;
	}

<%
	if (fromsched)
	{
	%>
	if (frm.reuse.value == "") 
	{
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Reusepercentagemustbeinput")%>");
		frm.reuse.focus();
		return;
	}
<%
	}
	else
	{
%>
	if (frm.actSize.value != 0)
	{
		if (frm.reuse.value == "") 
		{
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Reusepercentagemustbeinput")%>");
			frm.reuse.focus();
			return;
		}
	}
<%
	}
%>
	
	if ((frm.reuse.value != "") && (isNaN(frm.reuse.value) || (frm.reuse.value < 0) || (frm.reuse.value > 100))) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Reusemustbeintherange")%>");
		frm.reuse.focus();
		return;
	}
	
	if (frm.desc.value.length > 200) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.moduleNSizeUpdate.Descriptioncannotbelonger")%>");
		frm.desc.focus();
		return;
	}
	
	if (dowhat=='sched')
		frm.reqType.value='<%=Constants.SCHE_REVIEW_TEST_UPDATE%>';
	frm.submit();
	<% if (fromsched){%>
		window.returnValue = ((frm.actSize.value != "") && !(isNaN(frm.actSize.value) || (frm.actSize.value < 0))) ;
		window.close();
	<%}%>
	
}

function onCancel() {
<% if (fromsched){%>
	window.returnValue = false;
	window.close();
<%}else{%>
	window.open("moduleList.jsp", "main");
<%}%>
}
</SCRIPT>
</BODY>
</HTML>
