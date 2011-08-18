<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>moduleBatchUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY class="BD" onLoad="loadPrjMenu();onLoad()">
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

	int nRow = 1;
	int row = 0;
	int moduleDisplayed = 0;
	
	boolean isOver = false;
	Vector lastModule = (Vector) request.getAttribute("lastModule");
	if (lastModule != null) isOver = true;
	
	String strErr =  (String) request.getAttribute("ErrType");
	if (strErr != null) request.removeAttribute("ErrType");
	
	Vector vUpdateList = (Vector) request.getAttribute("ModuleBatchUpdateList");
	if (vUpdateList !=  null) {	
		request.removeAttribute("ModuleBatchUpdateList");
	}
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
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Productsize")%> </P>
<form name ="frm" method= "post" action = "Fms1Servlet#modulelist">
<input type = "hidden" name="reqType" value="<%=Constants.BATCH_MODULE_UPDATE%>">
<% 
	if ("1".equals(strErr)) {
%>		
<P class="ERROR">System error</P>
<%
	}
%>	
<BR>
<TABLE cellspacing="1" class="Table">
	<CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Updateproductsize")%> </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD style ="width:24px" align = "center"> # </TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Workproduct")%>*</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Name")%>*</TD>			
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Plannedsizeunit")%>*</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Plannedsize")%>*</TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Replannedsize")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Actualsizeunit")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Actualsize")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Reuse")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Description")%></TD>
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
	
	Vector vTemp = new Vector();
	if (isOver) vTemp = lastModule;
	else if (vUpdateList != null) vTemp = vUpdateList;
	nRow = vTemp.size();
	int acMethodBasedSize[] = new int[nRow];
	int actualSizeUnitID[] = new int[nRow];
	
	for (; row < nRow; row++) {
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		moduleInfo = (WPSizeInfo) vTemp.elementAt(row);
		reestSize =CommonTools.formatDouble(moduleInfo.reestimatedSize);
		actSize = CommonTools.formatDouble(moduleInfo.actualSize);
		reuse = CommonTools.formatDouble(moduleInfo.reusePercentage);
		desc = moduleInfo.description;
		Double d = new Double(moduleInfo.acMethodBasedSize);
		acMethodBasedSize[row] = d.intValue();
		Double c = new Double(moduleInfo.actualSizeUnitID);	
		actualSizeUnitID[row] = c.intValue();
		
%>
		<TR id="module_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">			
			<TD valign="top" style ="width:24px" align = "center"><%=row + 1%></TD>
			<TD class="CellBGR3">
				<input type="hidden" name = "moduleID" value = "<%=moduleInfo.moduleID%>"/>
            	<SELECT class="COMBO" name="category" onchange="onWPChange(<%=row%>)">
                <OPTION value="N/A"></OPTION>
            	<%
            	for (int i = 0; i < workProductList.size(); i++) {
            		WorkProductInfo wpInfo = (WorkProductInfo)workProductList.elementAt(i);
            		String selected = "";
            		if (moduleInfo.categoryID == wpInfo.workProductID)
            			selected = "selected";
            		%>	<OPTION value="<%=wpInfo.workProductID%>" <%=selected%>><%=languageChoose.getMessage(wpInfo.workProductName)%></OPTION>
                <%}%>
            	</SELECT>
            </TD>
            <TD class="CellBGR3">
            	<TEXTAREA rows="4" cols="25" name="name"><%= ConvertString.toHtml(moduleInfo.name)%></TEXTAREA>
            </TD>
            <TD class="CellBGR3">
            	<SELECT class="COMBO" name="estUnitID" onchange="onEstUnitChange(<%=row%>)">
            		<OPTION value="N/A"></OPTION>
            		<%
		        	for (int i = 0; i < relevantMethodList.size(); i++){
		        		EstimationMethodInfo emInfo = (EstimationMethodInfo) relevantMethodList.elementAt(i);
		        		%><OPTION value="1<%=emInfo.methodID%>" <%=(moduleInfo.estimatedSizeUnitID == emInfo.methodID)? "selected":"" %> ><%=ConvertString.toHtml(emInfo.name)%></OPTION>
		            <%}
		        	for (int i = 0; i < relevantLanguageList.size(); i++){
		        		LanguageInfo langInfo = (LanguageInfo) relevantLanguageList.elementAt(i);
		        		%><OPTION value="0<%=langInfo.languageID%>" <%=(moduleInfo.estimatedSizeUnitID == langInfo.languageID)? "selected":"" %>><%=ConvertString.toHtml(langInfo.name)+" "+ConvertString.toHtml(langInfo.sizeUnit)%></OPTION>
		            <%}%>
            	</SELECT><A href="javascript:selectAll(<%=row%>)">  <%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.more")%>  </A>          	
            </TD>
            <TD class="CellBGR3">
            	<INPUT size="11" style="text-align: right" type="text" maxlength="11" name="estSize" value="<%=CommonTools.formatDouble(moduleInfo.estimatedSize)%>">
            </TD>
            <TD class="CellBGR3">
				<INPUT size="11" style="text-align: right" type="text" maxlength="11" name="reestSize" value="<%=("N/A".equals(reestSize))? "":reestSize%>">
			</TD>
			<TD class="CellBGR3">
				<SELECT class="COMBO" name="actUnitID" style="width:200px">
                	<OPTION value="N/A"></OPTION>
            	</SELECT>
			</TD>
			<TD class = "CellBGR3">
				<INPUT size="11" style="text-align: right" type="text" maxlength="11" name="actSize" value="<%=("N/A".equals(actSize))?"":actSize %>">
			</TD>
			<TD class="CellBGR3">
				<INPUT size="8" style="text-align: right" type="text" maxlength="8" name="reuse" value="<%=reuse%>">%
			</TD>
			<TD class="CellBGR3">
				<TEXTAREA rows="4" cols="30" name="desc"><%=(desc==null) ? "":desc%></TEXTAREA>
			</TD>
		</TR>
<%
	}
	moduleDisplayed = row;	// Indicate numbers of lines displayed
%>
		<TR style="display:none">
			<TD><INPUT type="hidden" name = "moduleID"/></TD>
			<TD>
            	<SELECT name="category">
            	</SELECT>
            </TD>
            <TD class="CellBGR3">
            	<TEXTAREA rows="4" cols="25" name="name"></TEXTAREA>
            </TD>
            <TD class="CellBGR3">
            	<SELECT name="estUnitID">            		
            	</SELECT>         	
            </TD>
            <TD class="CellBGR3">
            	<INPUT name="estSize"/>
            </TD>
            <TD class="CellBGR3">
				<INPUT name="reestSize"/>
			</TD>
			<TD class="CellBGR3">
				<SELECT name="actUnitID">                	
            	</SELECT>
			</TD>
			<TD class = "CellBGR3">
				<INPUT name="actSize"/>
			</TD>
			<TD class="CellBGR3">
				<INPUT name="reuse"/>
			</TD>
			<TD class="CellBGR3">
				<TEXTAREA name="desc"></TEXTAREA>
			</TD>
		</TR>
	</TBODY>
</TABLE>
<!-- landd merge end -->
<INPUT type="button" class="BUTTON" name="ok" value=" <%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.OK")%> " onclick="onOK('<%=fromPage%>')">
<INPUT type = "hidden" name ="fromPage" value = "<%=fromPage%>"/>
<INPUT type="button" class="BUTTON" name="cancel" value=" <%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Cancel")%> " onclick="doIt(<%=Constants.MODULE_LIST%>);"><P/>
</FORM>
<SCRIPT language="javascript">

var module_nextHiddenIndex = <%=(moduleDisplayed + 1)%>;
var langLen = <%=relevantLanguageList.size()%>;
var methodLen = <%=relevantMethodList.size()%>;

function onLoad() {
	<% for (int x = 0; x < moduleDisplayed; x++) { %>
		onEstUnitChange(<%=x%>);
		frm.name[<%=x%>].focus();
		for (var i = 0; i < frm.actUnitID[<%=x%>].length; i++) {
			if (frm.actUnitID[<%=x%>].options[i].value == "<%=acMethodBasedSize[x]%><%=actualSizeUnitID[x]%>") {
				frm.actUnitID[<%=x%>].options[i].selected = true;
			}
		}
	<% } %>
	frm.category[0].focus();
}
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
		if (trim(frm.name[i].value) == "") {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Modulenamecannotbeempty")%>");
			frm.name[i].focus();
			return;
		}
		
		if (frm.category[i].value == "N/A") {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Categorynamecannotbeempty")%>");
			frm.category[i].focus();
			return;
		}
		
		if (frm.estUnitID[i].value == "N/A") {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Plannedunitcannotbeempty")%>");
			frm.estUnitID[i].focus();
			return;
		}
		if (frm.estSize[i].value == ""){
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Plannedunitcannotbeempty")%>");
			frm.estSize[i].focus();
			return;
		}
		if (frm.estUnitID[i].value == <%=LanguageInfo.NUMBER_OF_TEST_CASE%>){
			if (!integerFld(frm.estSize[i], "<%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Plannedsize")%>")){
				return;
			}
			if (frm.reestSize[i].value != "" && !integerFld(frm.reestSize[i], "<%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Replannedsize")%>")){
				return;
			}
		}
		if (isNaN(frm.estSize[i].value) || (frm.estSize[i].value < 0)){
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Plannedsizemustbeapositivenumber")%>");
			frm.estSize[i].focus();
			return;
		}
		if ((trim(frm.reestSize[i].value) != "") && (isNaN(frm.reestSize[i].value) || (frm.reestSize[i].value < 0))) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Replannedsizemustbeapositivenumber")%>");
			frm.reestSize[i].focus();
			return;
		}
	
		if ((frm.actUnitID[i].value == "N/A") && (trim(frm.actSize[i].value) != "") ||
			(frm.actUnitID[i].value != "N/A") && (trim(frm.actSize[i].value) == "")) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Actualsizeunitandactualsizeboth")%>");
			if (frm.actUnitID[i].value == "N/A"){
				frm.actUnitID[i].focus();
			}
			else{
				frm.actSize[i].focus();
			}
			return;
		}
	
		if (frm.actUnitID[i].value != "N/A" && frm.actUnitID[i].value == <%=LanguageInfo.NUMBER_OF_TEST_CASE%>){
			if (!integerFld(frm.actSize[i], "<%=languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Actualsizeunit")%>")){
				return;
			}
		}
	
		if ((trim(frm.actSize[i].value) != "") && (isNaN(frm.actSize[i].value) || (frm.actSize[i].value < 0))) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Actualsizemustbeapositivenumber")%>");
			frm.actSize[i].focus();
			return;
		}
		if ((trim(frm.actSize[i].value) != "") && frm.category[i].options[frm.category[i].selectedIndex].value==<%=WorkProductInfo.SOFTWARE_PACKAGE%> && frm.actSize[i].value ==0) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Softwarepackageactualsizemustbeabove")%>");
			frm.actSize[i].focus();
			return;
		}
		if ((trim(frm.reuse[i].value) != "") && (isNaN(frm.reuse[i].value) || (frm.reuse[i].value < 0) || (frm.reuse[i].value > 100))) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Reusemustbeintherange")%>");
			frm.reuse[i].focus();
			return;
		}
		
		if (frm.desc[i].value.length > 200) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.moduleBatchUpdate.Descriptioncannotbelonger")%>");
			frm.desc[i].focus();
			return;
		}
	}
	
	if (dowhat=='sched')
		frm.reqType.value='<%=Constants.SCHE_REVIEW_TEST_UPDATE3%>';
	frm.submit();
}
</SCRIPT>
</BODY>
</HTML>
