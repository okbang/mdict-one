<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>riskIdentify.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT language="javascript" src="jscript/riskSources.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad="loadPrjMenu()" class="BD">
<%
	String sourcePage = (String) session.getAttribute("plOverview_source");
	String strName = "";
	UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
	if (userLoginInfo != null)	{
		strName = userLoginInfo.Name;
	}
	if(sourcePage == null) sourcePage = "0";
	String change_source_page = (String) session.getAttribute("change_source_page");
	if (change_source_page==null){
		change_source_page="0";
	}
	String title;
	
	if(change_source_page.equals("1")){ //MANU: called from project plan
		title = languageChoose.getMessage("fi.jsp.riskAddnew.ProjectplanRisk");
	}
	else { //MANU: called from risk
		title = languageChoose.getMessage("fi.jsp.riskAddnew.Risks");
	}
	//called as add new or as add existing
	String strExisting = request.getParameter("existing");
	RiskInfo existingRisk = null;
	if (strExisting != null && Integer.parseInt(strExisting) >= 0) {
		int existing = Integer.parseInt(strExisting);
		//get existing risk
		Vector occuredRisk = (Vector) session.getAttribute("vtOccuredRisk");
		existingRisk = (RiskInfo) occuredRisk.elementAt(existing);
	}
	
	int row = 0;
	int maxColumn = 8;
	
	// Added by HaiMM -- Start
	Vector vtRiskSource=(Vector)session.getAttribute("vtRiskSource");
	Vector vtCommonRiskSource=(Vector)session.getAttribute("vtCommonRiskSource");
	
	int maxRow = 10;
	int minRow = 4;	
	
	int addition = 0;
	int mitigationDisplayed = 0;
	int contingencyDisplayed = 0;
	
	String rowStyle;

	// Added by HaiMM -- End
%>
<%!
	String impactTypeCombo(String selected,LanguageChoose languageChoose){
	String retVal="";
	String sel="";
	for (int i=0;i<RiskInfo.impactTypes.length;i++){
		if (selected!=null)
			sel=(RiskInfo.impactTypes[i].equals(selected))?"selected":"";
		retVal=retVal+"<OPTION value='"+RiskInfo.alfa[i]+"' " +sel+">" + languageChoose.getMessage(RiskInfo.impactTypes[i])+"</OPTION>";
	}
	return retVal;
}
String impactUnitCombo(String selected){
	String retVal="";
	String sel="";
	for (int i=0;i<RiskInfo.impactUnits.length;i++){
		if (selected!=null)
			sel=(RiskInfo.impactUnits[i].equals(selected))?"selected":"";
		retVal=retVal+"<OPTION value='"+RiskInfo.alfa[i]+"' " +sel+">"+RiskInfo.impactUnits[i]+"</OPTION>";
	}
	return retVal;
}
%>

<P class="TITLE"><%=title%></P>
<FORM action="Fms1Servlet#risks" method="post" name="frmAddnew">
<TABLE width="95%" cellspacing="1" class="table">
	<CAPTION class="TableCaption"> Identify Risk </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD rowspan="2"> # </TD>
			<TD rowspan="2"> Risk Source* </TD>
			<TD rowspan="2"> Description* </TD>
			<TD rowspan="2"> Probability* </TD>
			<TD rowspan="1" align="center"> Estimated Impact* </TD>
			<TD rowspan="2"> Total Impact </TD>
			<TD rowspan="2"> Exposure </TD>
			<TD rowspan="2"> Risk Priority* </TD>
			<TD rowspan="2"> Trigger </TD>
		</TR>
	</THEAD>
	<TBODY><%
	// If numbers of lines does not reach the minimum displaying lines => display more
	for (; (row < minRow &&
			row < maxRow); row++, addition++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
	%>
		<TR id="risk_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD> <%=row + 1%> <INPUT type="hidden" name="risk_id" value="0"> </TD>
            <TD>
            <SELECT class="COMBO" name="risk_source" style="width=200">
                <OPTION value="-1"></OPTION>
				<%
				for (int i=0;i < vtCommonRiskSource.size();i++){
					RiskSourceInfo riskSourceInfo= (RiskSourceInfo) vtCommonRiskSource.get(i);
				%>
                <OPTION value="<%=riskSourceInfo.sourceID%>" <%if (riskSourceInfo.sourceID == 0) {%> selected	<%}%>>* <%=riskSourceInfo.sourceName%>
                </OPTION>
                <%}%>
            </SELECT>
            <A href="javascript:selectAll(document.forms[0].risk_source[<%=row%>], 1)"> more other risk sources... </A>
            </TD>
			<TD> <TEXTAREA rows="5" cols="30" name="description" ></TEXTAREA> </TD>
			<TD> <INPUT size="7" type="text" maxlength="10" name="probability" id="probability<%=row%>" value="" onblur="calculateExposure('<%=row%>')"> </TD>
            <TD class="CellBGR3">
            <A name="plannedImpactTable">
            <TABLE class="Table" cellspacing="1">
                <TBODY>
                    <% for (int i=0;i<3;i++){%>
                    <TR class="CellBGR3">
						<TD>
							<SELECT name="impactTo<%=i+1%>" class="COMBO" id="i<%=i+1%>">
							<%=impactTypeCombo(null, languageChoose)%>
							</SELECT>
						</TD>
						<TD>
							<SELECT name="unit<%=i+1%>" class="COMBO" id="u<%=i+1%>">
							<%=impactUnitCombo(null)%>
							</SELECT>
						</TD>
						<TD>
							<INPUT size="11" maxlength="11" type="text" name="estImpact<%=i+1%>" style="text-align: right" id="e<%=i+1%>" value="" >
						</TD>
                    </TR>
					<%}%>
                </TBODY>
            </TABLE>
            </A>
			</TD>
			<TD> <INPUT size="7" type="text" maxlength="10" name="impact" id="impact<%=row%>" value="" onblur="calculateExposure('<%=row%>')"> </TD>
			<TD> <INPUT size="7" style="background-color: rgb(211, 211, 211);" readonly="readonly" type="text" maxlength="10" name="exposure" id="exposure<%=row%>" value=""> </TD>
			<TD> <INPUT size="7" type="text" maxlength="10" name="riskPriority" id="riskPriority<%=row%>" value=""> </TD>
			<TD> <TEXTAREA rows="5" cols="30" name="trigger" ></TEXTAREA> </TD>
		</TR><%
	}
	mitigationDisplayed = row;	// Indicate numbers of lines displayed
	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		%>
		<TR id="risk_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD> <%=row + 1%> <INPUT type="hidden" name="risk_id" value="0"> </TD>
            <TD>
            <SELECT class="COMBO" name="risk_source" style="width=200">
                <OPTION value="-1"></OPTION>
				<%
				for (int i=0;i < vtCommonRiskSource.size();i++){
					RiskSourceInfo riskSourceInfo= (RiskSourceInfo) vtCommonRiskSource.get(i);
				%>
                <OPTION value="<%=riskSourceInfo.sourceID%>" <%if (riskSourceInfo.sourceID == 0) {%> selected	<%}%>>* <%=riskSourceInfo.sourceName%>
                </OPTION>
                <%}%>
            </SELECT>
            <A href="javascript:selectAll(document.forms[0].risk_source[<%=row%>], 1)"> <%=languageChoose.getMessage("fi.jsp.productLocPages.More")%> </A>
            </TD>
			<TD> <TEXTAREA rows="5" cols="30" name="description" ></TEXTAREA> </TD>
			<TD> <INPUT size="7" type="text" maxlength="10" name="probability" id="probability<%=row%>" value="" onblur="calculateExposure('<%=row%>')"> </TD>
            <TD class="CellBGR3">
            <A name="plannedImpactTable">
            <TABLE class="Table" cellspacing="1">
                <TBODY>
                    <% for (int i=0;i<3;i++){%>
                    <TR class="CellBGR3">
						<TD>
							<SELECT name="impactTo<%=i+1%>" class="COMBO" id="i<%=i+1%>">
							<%=impactTypeCombo(null, languageChoose)%>
							</SELECT>
						</TD>
						<TD>
							<SELECT name="unit<%=i+1%>" class="COMBO" id="u<%=i+1%>">
							<%=impactUnitCombo(null)%>
							</SELECT>
						</TD>
						<TD>
							<INPUT size="11" maxlength="11" type="text" name="estImpact<%=i+1%>" style="text-align: right" id="e<%=i+1%>" value="" >
						</TD>
                    </TR>
					<%}%>
                </TBODY>
            </TABLE>
            </A>
			</TD>
			<TD> <INPUT size="7" type="text" maxlength="10" name="impact" id="impact<%=row%>" value="" onblur="calculateExposure('<%=row%>')"> </TD>
			<TD> <INPUT size="7" style="background-color: rgb(211, 211, 211);" readonly="readonly" type="text" maxlength="10" name="exposure" id="exposure<%=row%>" value=""> </TD>
			<TD> <INPUT size="7" type="text" maxlength="10" name="riskPriority" id="riskPriority<%=row%>" value=""> </TD>
			<TD> <TEXTAREA rows="5" cols="30" name="trigger" ></TEXTAREA> </TD>
		</TR><%
	}
	%>
	</TBODY>
</TABLE>
<p id="addMoreLink"><a href="javascript:addMoreRisk()"> Add More Risks </a></p>

<BR>

<INPUT type="hidden" name="reqType" value="">
<input type="hidden" name="source" value=""> 
<INPUT type="button" name="OK" value="Save" onclick="doAction(this);" class="BUTTON">
<INPUT type="button" name="Cancel" value="Cancel" onclick="doAction(this);" class="BUTTON">
<BR>
<BR>
<i><b>Note:</b></i> <font color="#FF0000">(*) Common risk sources</font><BR>
<br>

<table class="Table" id="table1" width="572" cellspacing="1">
        
        <tbody><tr class="CellBGRnews" style="color: red;">
            <td style="border-right-style: none; border-right-width: medium; " width="92" align="center">
			<b><font color="#000000">Column</font></b></td>
            <td style="border-style: none; border-width: medium;" width="331" align="center">
			<b><font color="#000000">Value</font></b></td>
            <td style="border-style: none; border-width: medium;" width="140" align="center">
			<b><font color="#000000">&nbsp;Formula</font></b></td>
        </tr>
        
        <tr class="CellBGR3" style="color: red;">
            <td style="border-right-style: none; border-right-width: medium; " width="92">
			<i>Probability</i></td>
            <td style="border-style: none; border-width: medium;" width="331" align="left">
			<i><font color="#0000FF">High (0.8 – 1.0); Medium (0.4 – 0.7); Low 
			(0.0 – 0.3)</font></i></td>
            <td style="border-style: none; border-width: medium;" width="140" align="left">
			&nbsp;</td>
        </tr>
        
        <tr class="CellBGR3" style="color: red;">
            <td style="border-right-style: none; border-right-width: medium; " width="92">
			<i>Total Impact </i></td>
            <td style="border-style: none; border-width: medium;" width="331" align="left">
			<i><font color="#0000FF">High (8.0 – 10.0); Moderate (4.0 – 7.0); 
			Low (0.0 – 3.0)</font></i></td>
            <td style="border-style: none; border-width: medium;" width="140" align="left">
			<i><font color="#0000FF">&nbsp;SUM(Estimated impact)</font></i></td>
        </tr>
        
        <tr class="CellBGR3" style="color: red;">
            <td style="border-right-style: none; border-right-width: medium; " width="92">
			<i>Exposure</i></td>
            <td style="border-style: none; border-width: medium;" width="331" align="left">
			<i><font color="#0000FF">High (7.1 – 10.0); Medium (4.0 – 7.0); Low 
			(0.1 – 3.9)</font></i></td>
            <td style="border-style: none; border-width: medium;" width="140" align="left">
			<i><font color="#0000FF">&nbsp;Probability * Impact</font></i></td>
        </tr>
        
        <tr class="CellBGR3" style="color: red;">
            <td style="border-right-style: none; border-right-width: medium; " width="92" height="23">
			<i>Risk Priority</i></td>
            <td style="border-style: none; border-width: medium;" width="331" align="left" height="23">
			<i><font color="#0000FF">1 to 10 (highest to lowest)</font></i></td>
            <td style="border-style: none; border-width: medium;" width="140" align="left" height="23">
			<i><font color="#0000FF">&nbsp;</font></i></td>
        </tr>
        
</tbody></table>


<br>
<br>
</FORM>

<SCRIPT language="JavaScript">

var nextHiddenIndex = 5;
var indexForFillCombo = <%=vtCommonRiskSource.size()%>;

function addMoreRisk() {
    getFormElement("risk_row" + nextHiddenIndex).style.display = document.all ? "block" : "table-row";
    nextHiddenIndex++;
    if(nextHiddenIndex > <%=maxRow%>)
        getFormElement("addMoreLink").style.display = "none";

}

function selectAll(combo) {
    // Fill more other Risk Source
    fillMoreRiskSources(combo, true, null, indexForFillCombo);
}

var sourcePage = <%=sourcePage%>;
function doAction(button) {
	if (button.name == "Cancel"){
		if (sourcePage == 1) {
			frmAddnew.reqType.value = "<%=Constants.PL_OVERVIEW_GET_PAGE%>";
			frmAddnew.submit();
		}
		else {
	  		frmAddnew.reqType.value = "<%=Constants.RISK_LIST_INIT%>";
			frmAddnew.submit();
		}
	}
	if (button.name == "OK") {
		if (isValidForm()) {
			// Submit		
			if (sourcePage == 1) {
				frmAddnew.source.value = "1";
				frmAddnew.reqType.value = "<%=Constants.RISK_ADDNEW%>";
				frmAddnew.submit();
			}
			else {
				frmAddnew.reqType.value = "<%=Constants.RISK_ADDNEW%>";
			  	frmAddnew.submit();
		    }
	    }
	}
}

function isValidForm() {
    var result = true;
	var notSelectedIndex = -1;
	var notSelectedItems = "";	// Filled information but not selected Risk Source
    
    for (i = 0; i < (nextHiddenIndex - 1); i++) {
		description = frmAddnew.description[i];
		probability = frmAddnew.probability[i];
		impact = frmAddnew.impact[i];
		exposure = frmAddnew.exposure[i];
		riskPriority = frmAddnew.riskPriority[i];
		trigger = frmAddnew.trigger[i];

		estImpact1 = frmAddnew.estImpact1[i];
		estImpact2 = frmAddnew.estImpact2[i];
		estImpact3 = frmAddnew.estImpact3[i];

		unit1 = frmAddnew.unit1[i];
		unit2 = frmAddnew.unit2[i];
		unit3 = frmAddnew.unit3[i];

		impactTo1 = frmAddnew.impactTo1[i];
		impactTo2 = frmAddnew.impactTo2[i];
		impactTo3 = frmAddnew.impactTo3[i];

    	if (frmAddnew.risk_source[i].value > 0) {
			if (!isValidData(
				description, probability, impact, exposure, riskPriority, trigger, estImpact1, estImpact2, estImpact3,
				impactTo1, impactTo2, impactTo3))
			{
    			result = false;
    			break;
			}
			
			if (!isFillAll(
				estImpact1, estImpact2, estImpact3, unit1, unit2, unit3, impactTo1, impactTo2, impactTo3))
			{
    			result = false;
    			break;
			}
    	}
    	// Not selected a risk source but filled other data
    	else if (!isBlank(
    			description, probability, impact, riskPriority, trigger) || 
    			!isBlankImpact(
    			estImpact1, estImpact2, estImpact3, unit1, unit2, unit3, impactTo1, impactTo2, impactTo3))
    	{
    		notSelectedItems = notSelectedItems + (i + 1) + "  ";
    		if (notSelectedIndex == -1) {
    			notSelectedIndex = i;
    		}
    	}
    }
    // If above checks is invalid => return result(false)
    if (!result) {
    	return result;
    }
    
	// Not selected a Risk Source but filled other data
    if (notSelectedIndex >= 0) {
    	var msg = "";
    	if (notSelectedIndex >= 0) {
    		msg += "You filled other information but not entered a Risk Source at line " + notSelectedItems + "\n";
    	}
    	msg += "<%=languageChoose.getMessage("fi.jsp.productLocPages.TheyWillNotBeSaved")%>";
    	result = window.confirm(msg);
    	if (!result) {
			if (notSelectedIndex >= 0) {
    			frmAddnew.risk_source[notSelectedIndex].focus();
    		}
    	}
    }
    return result;
}

// Validate a selected line. If selected a Risk Source then at least one value should be filled
function isValidData(
				description, probability, impact, exposure, riskPriority, trigger, estImpact1, estImpact2, estImpact3,
				impactTo1, impactTo2, impactTo3) {
	var result = true;
	// If selected a risk source then should fill other data
	if (description.value == "")
	{
		alert("Please fill up description for selected risk source !");
		description.focus();
		result = false;
	}
	else if (probability.value == "")
	{
		alert("Please fill up probability for selected risk source !");
		probability.focus();
		result = false;
	}
	else if (impactTo1.value == "a" && impactTo2.value == "a" && impactTo3.value == "a")
	{
		alert("Please fill up at least one impact for selected risk source !");
		impactTo1.focus();
		result = false;
	}
	else if (riskPriority.value == "")
	{
		alert("Please fill up priority for selected risk source !");
		riskPriority.focus();
		result = false;
	}
	// Check all numbers are positive integer numbers
	else if (
		!positiveInt(riskPriority,"Risk Priority"))
	{
		result = false;
	}
	// Check data relation
	else if (!isValidValue(description, probability, impact, exposure, riskPriority, trigger, estImpact1, estImpact2, estImpact3)) {
		result = false;
	}
	return result;
}

// Validate Impact
function isFillAll(
				estImpact1, estImpact2, estImpact3, unit1, unit2, unit3, impactTo1, impactTo2, impactTo3) {
	var result = true;

	if ((!(impactTo1.value == "a") ||
	  		 	  !(unit1.value == "a") ||
	  		 	  !(estImpact1.value == "")) && 
				  ((impactTo1.value == "a") ||
	  		   	   (unit1.value == "a") ||
	  		  	   (estImpact1.value == ""))) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.riskAddnew.fillallFields")%>");
  		impactTo1.focus();
  		result = false;
	}

	else if ((!(impactTo2.value == "a") ||
	  		 	  !(unit2.value == "a") ||
	  		 	  !(estImpact2.value == "")) && 
				  ((impactTo2.value == "a") ||
	  		   	   (unit2.value == "a") ||
	  		  	   (estImpact2.value == ""))) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.riskAddnew.fillallFields")%>");
  		impactTo2.focus();
  		result = false;
	}
	
	else if ((!(impactTo3.value == "a") ||
	  		 	  !(unit3.value == "a") ||
	  		 	  !(estImpact3.value == "")) && 
				  ((impactTo3.value == "a") ||
	  		   	   (unit3.value == "a") ||
	  		  	   (estImpact3.value == ""))) {
		window.alert("<%= languageChoose.getMessage("fi.jsp.riskAddnew.fillallFields")%>");
  		impactTo3.focus();
  		result = false;
	}

	return result;
}

function isValidValue(description, probability, impact, exposure, riskPriority, trigger, estImpact1, estImpact2, estImpact3) {
	var result = true;
	
 	if ((trim(description.value) == "") || (description.value.length > 600)) {
		window.alert("Description can not be longer than 600 characters");
		description.focus();
  		result = false;
	}
	else if (trigger.value.length > 600) {
		window.alert("Trigger can not be longer than 600 characters");
		trigger.focus();
		result = false;
	}
  	else if(impact.value > 10){
  		window.alert("Total impact must be a number and less or equal than 10");
  		estImpact1.focus();
  		result = false;
  	}
  	else if(exposure.value > 10){
  		window.alert("Exposure must be a number and less or equal than 10");
  		probability.focus();
  		result = false;
  	}
  	else if ((trim(riskPriority.value) != "") && (isNaN(trim(riskPriority.value)) || (riskPriority.value < 0) || riskPriority.value < 1 || riskPriority.value > 10 || riskPriority.value.indexOf('.') >= 0)) {
		window.alert("Risk priority must be a integer value between 1 and 10");
		riskPriority.focus();
  		result = false;
	}
	else if ((trim(estImpact1.value) != "") && (isNaN(estImpact1.value) || (estImpact1.value < 0))) {
		window.alert("Estimate impact must be a positive number");
		estImpact1.focus();
		result = false;
	}
			 	
	else if ((trim(estImpact2.value) != "") && (isNaN(estImpact2.value) || (estImpact2.value < 0))) {
		window.alert("Estimate impact must be a positive number");
		estImpact2.focus();
		result = false;
	}

	else if ((trim(estImpact3.value) != "") && (isNaN(estImpact3.value) || (estImpact3.value < 0))) {
		window.alert("Estimate impact must be a positive number");
		estImpact3.focus();
		result = false;
	}

	return result;
}

function isBlank(
		description, probability, impact, riskPriority, trigger)
{
	return (trim(description.value).length <= 0 &&
			trim(probability.value).length <= 0 &&
//			trim(impact.value).length <= 0 &&
			trim(riskPriority.value).length <= 0 &&
			trim(trigger.value).length <= 0);
}

function isBlankImpact(
		estImpact1, estImpact2, estImpact3, unit1, unit2, unit3, impactTo1, impactTo2, impactTo3)
{
	return (trim(estImpact1.value).length <= 0 &&
			trim(estImpact2.value).length <= 0 &&
			trim(estImpact3.value).length <= 0 &&

			trim(unit1.value) == "a" &&
			trim(unit2.value) == "a" &&
			trim(unit3.value) == "a" &&
			
			trim(impactTo1.value) == "a" &&
			trim(impactTo2.value) == "a" &&
			trim(impactTo3.value) == "a");
}

function calculateExposure(i) {
	if(frmAddnew.probability[i].value.length > 0 && isNaN(frmAddnew.probability[i].value)){
		window.alert("The value must be a positive number");
		frmAddnew.probability[i].focus();
		return;
	}
	if(frmAddnew.probability[i].value.length > 0 && frmAddnew.probability[i].value > 1){
		window.alert("The value must be a number from 0.1 to 1.0");
		frmAddnew.probability[i].focus();
		return;
	}

	if(frmAddnew.impact[i].value.length > 0 && isNaN(frmAddnew.impact[i].value)){
		window.alert("The value must be a positive number");
		frmAddnew.impact[i].focus();
		return;
	}
	
	if(frmAddnew.impact[i].value.length > 0 && frmAddnew.impact[i].value > 10){
		window.alert("The value must be a number from 0 to 10");
		frmAddnew.impact[i].focus();
		return;
	}
	
	frmAddnew.exposure[i].value = frmAddnew.probability[i].value * frmAddnew.impact[i].value;

}

function calculateImpact(i) {
	var es1 = new Number(frmAddnew.estImpact1[i].value);
	var es2 = new Number(frmAddnew.estImpact2[i].value);
	var es3 = new Number(frmAddnew.estImpact3[i].value);
	
	if (!isNaN(es1 + es2 + es3)) {
		frmAddnew.impact[i].value = es1 + es2 + es3 ;
	}
	else {
		frmAddnew.impact[i].value = 0;
	}
	
	frmAddnew.exposure[i].value = frmAddnew.probability[i].value * frmAddnew.impact[i].value;

}

</SCRIPT> 

</BODY>
</HTML>
