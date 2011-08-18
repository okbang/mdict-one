<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>moduleBatchAddnew.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();frm.category[0].focus();">
<%
	int schedRight=Security.getRights("Schedule",request);
	Vector workProductList = (Vector)session.getAttribute("workProductList");
	
	String fromPage = (String) request.getAttribute("fromPage");
	if (fromPage == null) fromPage ="";
	else request.removeAttribute("fromPage");
	
	Vector languageList = (Vector)session.getAttribute("languageList");
	Vector methodList = (Vector)session.getAttribute("methodList");
	Vector relevantLanguageList = (Vector)session.getAttribute("relevantLanguageList");
	Vector relevantMethodList = (Vector)session.getAttribute("relevantMethodList");
	
	// landd start
	int maxRow = ModuleCaller.NUMBER_OF_ROW_ADDABLE;
	int nRow = 1;
	int row = 0;
	int moduleDisplayed = 0;
	
	boolean isOver = false;
	Vector lastModule = (Vector) request.getAttribute("lastModule");
	
	if (lastModule != null){
		isOver = true;
		nRow = lastModule.size();
	}
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
	// landd end
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
	
	function selectAll(index)
	{
	    var myElement;
	    for (var i = frm.estUnitID[index].options.length; i >= 1; i--){
	        frm.estUnitID[index].options[i] = null;
	    }
	    for (var i = frm.actUnitID[index].options.length; i >= 1; i--){
	        frm.actUnitID[index].options[i] = null;
	    }
	    for (var i = 0; i < arrEstimationMethod.length; i++){
            myElement = document.createElement("option");
            myElement.value = "1" + arrEstimationMethodId[i];
            myElement.text = arrEstimationMethod[i];
            frm.estUnitID[index].add(myElement);
	    }
        for (var i = 0; i < arrLanguage.length; i++){
            myElement = document.createElement("option");
            myElement.value = "0" + arrLanguageId[i];
            myElement.text = arrLanguage[i];
            frm.estUnitID[index].add(myElement);
	    }
	}
	
</SCRIPT> 

<!-- landd merge start -->
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Productsize")%> </P>
<form name ="frm" method= "post" action = "Fms1Servlet#modulelist">
<input type = "hidden" name="reqType" value="<%=Constants.BATCH_MODULE_ADD%>">
<% 
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">System error</P>
<%
	}
%>	
<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.moduleAddnew.Addnewproductsize")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD width="24" align = "center"> # </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Workproduct")%>*</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Name")%>*</TD>			
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Plannedsizeunit")%>*</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Plannedsize")%>*</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Replannedsize")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Actualsizeunit")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Actualsize")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Reuse")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Description")%></TD>
		</TR>
	</THEAD>
	<TBODY>
<%
	// Display current list (last updated data)
	String reestSize="";
	String actSize="";
	String reuse="";
	String desc="";
	String rowStyle="";
	WPSizeInfo moduleInfo = new WPSizeInfo();
	for (; (row < nRow && row < maxRow); row++) {
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		if (isOver)	{
			moduleInfo = (WPSizeInfo) lastModule.elementAt(row);
			reestSize =(moduleInfo.reestimatedSize >= 0)? CommonTools.formatDouble(moduleInfo.reestimatedSize):"";
			actSize = (moduleInfo.actualSize >= 0)?		CommonTools.formatDouble(moduleInfo.actualSize):"";
			reuse = (moduleInfo.reusePercentage >= 0)?	CommonTools.formatDouble(moduleInfo.reusePercentage):"";
			desc = (moduleInfo.description != null)? 	moduleInfo.description:"";
		}
%>
		<TR id="module_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD valign="top" width="24"> <%=row + 1%></TD>
			<TD class="CellBGR3">
            	<SELECT class="COMBO" name="category" onchange="onWPChange(<%=row%>)">
                <OPTION value="N/A"></OPTION>
            	<%
            	for (int i = 0; i < workProductList.size(); i++) {
            		WorkProductInfo wpInfo = (WorkProductInfo)workProductList.elementAt(i);
            		String selected = "";
            		if (isOver && moduleInfo.categoryID == wpInfo.workProductID)
            			selected = "selected";
            		%>	<OPTION value="<%=wpInfo.workProductID%>" <%=selected%>><%=languageChoose.getMessage(wpInfo.workProductName)%></OPTION>
                <%}%>
            	</SELECT>
            </TD>
            <TD class="CellBGR3">
            	<TEXTAREA rows="4" cols="25" name="name"><%=isOver? moduleInfo.name :""%></TEXTAREA>
            </TD>
            <TD class="CellBGR3">
            	<SELECT class="COMBO" name="estUnitID" onchange="onEstUnitChange(<%=row%>)">
            		<OPTION value="N/A"></OPTION>
            		<%
		        	for (int i = 0; i < relevantMethodList.size(); i++){
		        		EstimationMethodInfo emInfo = (EstimationMethodInfo) relevantMethodList.elementAt(i);
		        		%><OPTION value="1<%=emInfo.methodID%>" <%=(isOver && moduleInfo.estimatedSizeUnitID == emInfo.methodID)? "selected":"" %> ><%=emInfo.name%></OPTION>
		            <%}
		        	for (int i = 0; i < relevantLanguageList.size(); i++){
		        		LanguageInfo langInfo = (LanguageInfo) relevantLanguageList.elementAt(i);
		        		%><OPTION value="0<%=langInfo.languageID%>" <%=(isOver && moduleInfo.estimatedSizeUnitID == langInfo.languageID)? "selected":"" %>><%=langInfo.name+" "+langInfo.sizeUnit%></OPTION>
		            <%}%>
            	</SELECT><A href="javascript:selectAll(<%=row%>)">  <%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.more")%>  </A>          	
            </TD>
            <TD class="CellBGR3">
            	<INPUT size="11" style="text-align: right" type="text" maxlength="11" name="estSize" value="<%=isOver ? CommonTools.formatDouble(moduleInfo.estimatedSize):""%>">
            </TD>
            <TD class="CellBGR3">
				<INPUT size="11" style="text-align: right" type="text" maxlength="11" name="reestSize" value="<%=isOver ? reestSize:""%>">
			</TD>
			<TD class="CellBGR3">
				<SELECT class="COMBO" name="actUnitID" style="width:200px">
                	<OPTION value="N/A"></OPTION>
            	</SELECT>
			</TD>
			<TD class = "CellBGR3">
				<INPUT size="11" style="text-align: right" type="text" maxlength="11" name="actSize" value="<%=isOver? actSize:""%>">
			</TD>
			<TD class="CellBGR3">
				<INPUT size="11" style="text-align: right" type="text" maxlength="11" name="reuse" value="<%=isOver? reuse:""%>">%
			</TD>
			<TD class="CellBGR3">
				<TEXTAREA rows="4" cols="30" name="desc"><%=isOver? desc:""%></TEXTAREA>
			</TD>
		</TR>
<%
	}
	moduleDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
		<TR id="module_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD valign="top" width="24"> <%=row + 1%></TD>
			<TD class="CellBGR3">
            	<SELECT class="COMBO" name="category" onchange="onWPChange(<%=row%>)">
                	<OPTION value="N/A"></OPTION>
            	<%
            	for (int i = 0; i < workProductList.size(); i++) {
            		WorkProductInfo wpInfo = (WorkProductInfo)workProductList.elementAt(i);           		
            	%>	
            		<OPTION value="<%=wpInfo.workProductID%>"><%=languageChoose.getMessage(wpInfo.workProductName)%></OPTION>
                <%}%>
            	</SELECT>
            </TD>
            <TD class="CellBGR3">
            	<TEXTAREA rows="4" cols="25" name="name"></TEXTAREA>
            </TD>
            <TD class="CellBGR3">
            	<SELECT class="COMBO" name="estUnitID" onchange="onEstUnitChange(<%=row%>)">
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
            	</SELECT><A href="javascript:selectAll(<%=row%>)">  <%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.more")%>  </A>          	          	
            </TD>
            <TD class="CellBGR3">
            	<INPUT size="11" style="text-align: right" type="text" maxlength="11" name="estSize"">
            </TD>
            <TD class="CellBGR3">
				<INPUT size="11" style="text-align: right" type="text" maxlength="11" name="reestSize"">
			</TD>
			<TD class="CellBGR3">
					<SELECT class="COMBO" name="actUnitID" style="width:200px">
						<OPTION value="N/A"></OPTION>
					</SELECT>
			</TD>
			<TD class = "CellBGR3">
				<INPUT size="11" style="text-align: right" type="text" maxlength="11" name="actSize">
			</TD>
			<TD class="CellBGR3">
				<INPUT size="11" style="text-align: right" type="text" maxlength="11" name="reuse">%
			</TD>
			<TD class="CellBGR3">
				<TEXTAREA rows="4" cols="30" name="desc"></TEXTAREA>
			</TD>
		</TR>
<%
	}
%>
	</TBODY>
</TABLE>
<p id="module_addMoreLink"><a href="javascript:addMoreRow()"> <%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.AddMoreproductsize")%> </a></p>
<BR>
<!-- landd merge end -->
<INPUT type="button" class="BUTTON" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.OK")%> " onclick="onOK('<%=fromPage%>')">
<INPUT type = "hidden" name ="fromPage" value = "<%=fromPage%>"/>
<INPUT type="button" class="BUTTON" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Cancel")%> " onclick="doIt(<%=Constants.MODULE_LIST%>);">
<INPUT type="hidden" name="reqType" value="<%=Constants.BATCH_MODULE_ADD%>">

</FORM>
<SCRIPT language="javascript">

var module_nextHiddenIndex = <%=(moduleDisplayed + 1)%>;
function addMoreRow() {
     getFormElement("module_row" + module_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	 module_nextHiddenIndex++;
	 if(module_nextHiddenIndex > <%=maxRow%>)
	     getFormElement("module_addMoreLink").style.display = "none";
	
}
init();
function init(){
   if(module_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("module_addMoreLink").style.display = "none";    
}


var langLen = <%=relevantLanguageList.size()%>;
var methodLen = <%=relevantMethodList.size()%>;

function onEstUnitChange(index) {
	if (frm.estUnitID[index].value.substring(0, 1) == "1") {
		frm.actUnitID[index].length = langLen + 1;
		frm.actUnitID[index].options[1].value = frm.estUnitID[index].options[frm.estUnitID[index].selectedIndex].value;
		frm.actUnitID[index].options[1].text = frm.estUnitID[index].options[frm.estUnitID[index].selectedIndex].text;
		for (var i = 2; i < frm.actUnitID[index].length; i ++) {
			frm.actUnitID[index].options[i].value = frm.estUnitID[index].options[i + methodLen].value;
			frm.actUnitID[index].options[i].text = frm.estUnitID[index].options[i + methodLen].text;
		}
	}
	else {		
		frm.actUnitID[index].length = 2;
		frm.actUnitID[index].options[1].value = frm.estUnitID[index].options[frm.estUnitID[index].selectedIndex].value;
		frm.actUnitID[index].options[1].text = frm.estUnitID[index].options[frm.estUnitID[index].selectedIndex].text;
	}
}

function onWPChange(i) {
	frm.name[i].focus();
	frm.name[i].value = frm.category[i].options[frm.category[i].selectedIndex].text;
}

function onOK(dowhat) {
	for (i = 0;i < module_nextHiddenIndex-1;i++) {
		if (trim(frm.name[i].value) == "" 
			&& frm.category[i].value == "N/A"
			&& frm.estUnitID[i].value == "N/A"
			&& trim(frm.estSize[i].value) == "" 
			&& trim(frm.reestSize[i].value) == ""
			&& frm.actUnitID[i].value == "N/A"
			&& trim(frm.actSize[i].value) == ""
			&& trim(frm.reuse[i].value) == ""
			&& trim(frm.desc[i].value) == ""
			) continue;
			
		if (trim(frm.name[i].value) == "") {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Modulenamecannotbeempty")%>");
			frm.name[i].focus();
			return;
		}
		
		if (frm.category[i].value == "N/A") {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Categorynamecannotbeempty")%>");
			frm.category[i].focus();
			return;
		}
		
		if (frm.estUnitID[i].value == "N/A") {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Plannedunitcannotbeempty")%>");
			frm.estUnitID[i].focus();
			return;
		}
		if (frm.estSize[i].value == ""){
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Plannedunitcannotbeempty")%>");
			frm.estSize[i].focus();
			return;
		}
		if (frm.estUnitID[i].value == <%=LanguageInfo.NUMBER_OF_TEST_CASE%>){
			if (!integerFld(frm.estSize[i], "<%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Plannedsize")%>")){
				return;
			}
			if (frm.reestSize[i].value != "" && !integerFld(frm.reestSize[i], "<%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Replannedsize")%>")){
				return;
			}
		}
		if (isNaN(frm.estSize[i].value) || (frm.estSize[i].value < 0)){
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Plannedsizemustbeapositivenumber")%>");
			frm.estSize[i].focus();
			return;
		}
		if ((trim(frm.reestSize[i].value) != "") && (isNaN(frm.reestSize[i].value) || (frm.reestSize[i].value < 0))) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Replannedsizemustbeapositivenumber")%>");
			frm.reestSize[i].focus();
			return;
		}
	
		if ((frm.actUnitID[i].value == "N/A") && (trim(frm.actSize[i].value) != "") ||
			(frm.actUnitID[i].value != "N/A") && (trim(frm.actSize[i].value) == "")) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Actualsizeunitandactualsizeboth")%>");
			if (frm.actUnitID[i].value == "N/A"){
				frm.actUnitID[i].focus();
			}
			else{
				frm.actSize[i].focus();
			}
			return;
		}
	
		if (frm.actUnitID[i].value != "N/A" && frm.actUnitID[i].value == <%=LanguageInfo.NUMBER_OF_TEST_CASE%>){
			if (!integerFld(frm.actSize[i], "<%=languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Actualsizeunit")%>")){
				return;
			}
		}
	
		if ((trim(frm.actSize[i].value) != "") && (isNaN(frm.actSize[i].value) || (frm.actSize[i].value < 0))) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Actualsizemustbeapositivenumber")%>");
			frm.actSize[i].focus();
			return;
		}
		if ((trim(frm.actSize[i].value) != "") && frm.category[i].options[frm.category[i].selectedIndex].value==<%=WorkProductInfo.SOFTWARE_PACKAGE%> && frm.actSize[i].value ==0) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Softwarepackageactualsizemustbeabove")%>");
			frm.actSize[i].focus();
			return;
		}
		if ((trim(frm.reuse[i].value) != "") && (isNaN(frm.reuse[i].value) || (frm.reuse[i].value < 0) || (frm.reuse[i].value > 100))) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Reusemustbeintherange")%>");
			frm.reuse[i].focus();
			return;
		}
		
		if (frm.desc[i].value.length > 200) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchAddnew.Descriptioncannotbelonger")%>");
			frm.desc[i].focus();
			return;
		}
	}
	
	if (dowhat=='sched')
		frm.reqType.value='<%=Constants.SCHE_REVIEW_TEST_UPDATE4%>';
	frm.submit();
}
</SCRIPT>
</BODY>
</HTML>
