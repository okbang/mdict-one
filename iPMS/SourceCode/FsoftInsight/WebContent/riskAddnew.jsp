<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>riskAddnew.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" media="all" href="stylesheet/auto_complete.css"></LINK>
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<SCRIPT src="jscript/ajax-dynamic-list.js"></SCRIPT>
<SCRIPT src="jscript/ajax.js"></SCRIPT>
<SCRIPT language="javascript">
	<%@ include file="javaFns.jsp"%>
</SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad="loadPrjMenu();loadSourceList(document.getElementById('cmbcategoty'),frmAddnew.cmbsource);" class="BD">
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
	Vector userList = (Vector)session.getAttribute("userList");
	Vector vtProcess = (Vector)session.getAttribute("vtProcess");
	
	Vector riskCategory=(Vector)session.getAttribute("riskCategory");
	Vector riskSource=(Vector)session.getAttribute("riskSource");
	String rank = (String)session.getAttribute("projectRank");	
	
	int row = 0;
	int maxColumn = 8;
%>

<P class="TITLE"><%=title%></P>
<FORM action="Fms1Servlet#risks" method="get" name="frmAddnew">
<TABLE class="Table" cellspacing="1">
    <COL span="1" width="160">
    <COL span="1" width="400">
    <CAPTION align="left" class="TableCaption"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Addnewrisk")%></CAPTION>
    <TBODY>
   		<TR>
			<TD class="ColumnLabel" nowrap="nowrap"> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Category")%> </TD>
			<TD class="CellBGR3"><SELECT name="cmbcategoty" class="COMBO" tabindex="<%=row+1%>" id="cmbcategoty" onChange="loadSourceList(document.getElementById('cmbcategoty'),frmAddnew.cmbsource);">
				<%RiskCategoryInfo riskCatInfo;
				for (int i=0;i<riskCategory.size();i++){
					riskCatInfo=(RiskCategoryInfo) riskCategory.elementAt(i);
				%>
					<OPTION value="<%=riskCatInfo.categoryID%>" <%if (riskCatInfo.categoryID == 0) {%> selected	<%}%>><%=languageChoose.getMessage(riskCatInfo.categoryName)%></OPTION>
				<%}%>
			</SELECT>
			</TD>
			<%
			row=0;
				for(int i=1;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" class="CellBGR3"></TD>
			<%}
				row++;
			%>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Source")%> </TD>
			<TD class="CellBGR3"><SELECT name="cmbsource" class="COMBO" tabindex="<%=row+1%>"></SELECT></TD>
			<%
				for(int i=1;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" class="CellBGR3"></TD>
			<%}
				row++;
			%>
		</TR>
		<TR>
            <TD height="57" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Description")%>*</TD>
            <TD height="57" class="CellBGR3"><TEXTAREA rows="3" tabindex="<%=row+1%>" cols="50" name="condition"><%=((existingRisk!=null && existingRisk.actualRiskScenario!=null)?existingRisk.actualRiskScenario:"")%></TEXTAREA>
            </TD>
            <%
				for(int i=1;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" class="CellBGR3"></TD>
			<%}
				row++;
			%>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Probability") %></TD>
            <TD class="CellBGR3"><input type = "TEXT" size="9" name = "txtProbability" tabindex="<%=row+1%>" onblur="CalculateExposure()"></input>
            </TD>
            <%
				for(int i=1;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" class="CellBGR3"></TD>
			<%}
				row++;
			%>
        </TR>
        <TR>
            <TD class="ColumnLabel">Impact</TD>
            <TD class="CellBGR3"><input type = "TEXT" size="9" name = "txtImpact" tabindex="<%=row+1%>" onblur="CalculateExposure()"></input>
            </TD>
            <%
				for(int i=1;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" class="CellBGR3"></TD>
			<%}
				row++;
			%>
        </TR>
        <TR>
            <TD class="ColumnLabel">Exposure</TD>
            <TD class="CellBGR3">
            	<input type = "TEXT" size="9" name = "txtExposure" tabindex="<%=row+1%>" readonly="readonly"></input>
	            
	            <SELECT name="priority" class="COMBO" tabindex="<%=row+1%>" onblur="Check()">
	                <OPTION value="-1" style="color: red;"></OPTION>
	                <OPTION value="1" style="color: red;">High</OPTION>
	                <OPTION value="2" style="color: blue;Red">Medium</OPTION>
	                <OPTION value="3" style="color: black;">Low</OPTION>
	            </SELECT>
            </TD>
            <%
				for(int i=1;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" class="CellBGR3"></TD>
			<%}
				row++;
			%>
        </TR>
        <TR>
            <TD class="ColumnLabel">Risk Priority</TD>
            <TD class="CellBGR3"><input type="text" size="9" name="riskPriority" tabindex="<%=row+1%>"></TD>
            <%
				for(int i=1;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" class="CellBGR3"></TD>
			<%}
				row++;
			%>
        </TR>
        <TR>
            <TD height="18" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Trigger")%></TD>
            <TD height="18" class="CellBGR3"><TEXTAREA tabindex="<%=row+1%>" rows="3" cols="50" name="triggerName"></TEXTAREA>
            </TD>
            <%
				for(int i=1;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" class="CellBGR3"></TD>
			<%}
				row++;
			%>
        </TR>
        <TR>
            <TD height="17" class="ColumnLabel">Risk Status</TD>
            <TD height="17" class="CellBGR3"><SELECT name="riskStatus" class="COMBO" tabindex="<%=row+1%>">
                <OPTION value="1" selected> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Open")%> </OPTION>
                <OPTION value="2"> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Occurred")%> </OPTION>
                <OPTION value="3"> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Closed")%> </OPTION>
            </SELECT>
            </TD>
            <%
				for(int i=1;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" class="CellBGR3"></TD>
			<%}
				row++;
			%>
        </TR>
        <TR>
            <TD class="ColumnLabel">Last Updated Date</TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" tabindex="<%=row+1%>" name="lastUpdatedDate" maxlength="9">(DD-MMM-YY)
            </TD>
            <%
				for(int i=1;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" class="CellBGR3"></TD>
			<%}
				row++;
			%>
        </TR>
        
<!-- Block Option-->
        <TR>
            <TD height="23" class="CellBGR3">&nbsp</TD>
            <%
				for(int i=0;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" height="23" class="CellBGR3"></TD>
			<%}
				row++;
			%>
        </TR>
        
        <TR>
            <TD height="23" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Mitigation")%></TD>
            <%
				for(int i=0;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" height="23" class="CellBGR3"><TEXTAREA rows="3" cols="50" name="mitigation" tabindex="<%=i*10+row%>"></TEXTAREA></TD>
			<%}
				row++;
			%>
        </TR>
		<TR>
            <TD height="20" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Contingency")%></TD>
            <%
				for(int i=0;i<maxColumn;i++){
			%>
            <TD id="td<%=row%><%=i%>" height="20" class="CellBGR3">
            	<INPUT type="hidden" name="txtCreator" value='<%=strName%>'>
            	<TEXTAREA rows="3" tabindex="<%=i*10+row%>" cols="50" name="contingency"></TEXTAREA>
            </TD>
            <%}
            	row++;
            %>
        </TR>
        
		<TR>
			<TD height="23" class="ColumnLabel">Mitigation Cost</TD>
			<%
				for(int i=0;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" height="23" class="CellBGR3"><input type="text" size="9" name="mitigationCost" tabindex="<%=i*10+row%>"></input>
			</TD>
			<%}
				row++;
			%>
		</TR>
		
        <TR>
			<TD height="23" class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Mitigationbenefit")%> </TD>
			<%
				for(int i=0;i<maxColumn;i++){
			%>
			<TD id="td<%=row%><%=i%>" height="23" class="CellBGR3"><TEXTAREA tabindex="<%=i*10+row%>" rows="3" cols="50" name="mitigationBenefit"></TEXTAREA>
			</TD>
			<%}
				row++;
			%>
		</TR>
        <TR>
            <TD height="18" class="ColumnLabel">Person in charge</TD>
            <%
				for(int i=0;i<maxColumn;i++){
			%>
	            <TD id="td<%=row%><%=i%>" height="18" class="CellBGR3">
		            <p id="pAccMore<%=i%>">
			            <SELECT name="personInCharge" class="COMBO" tabindex="<%=i*10+row%>">
						<%
			            	for (int j = 0; j < userList.size(); j++) {
				            	AssignmentInfo assInfo = 
				            		(AssignmentInfo)userList.elementAt(j);
			            %>
			                <OPTION value="<%=assInfo.account%>">
			                	<%=assInfo.account%> - <%=assInfo.devName%>
			                </OPTION>
						<%}%>
			            </SELECT>
		            <A href="javascript:More(<%=i%>);">  Select in Fsoft  </A>
		            </p>
	            	<p id="pAccBack<%=i%>">
		            	<INPUT name="personInChargeAll" size="30" type="text" value="" tabindex="<%=i*10+row%>"/>
		            	<INPUT type="button" class="BUTTON" tabindex="<%=i*10+row+1%>" value="<%=languageChoose.getMessage("fi.jsp.woTeamAdd.Search")%>" onclick="javascript:onCheckAccount(<%=i%>,event);"></INPUT>
		            	<A href="javascript:Back(<%=i%>);">  Return  </A>
		            	<BR>
			            <INPUT type="radio" name="rdAccountName<%=i%>" value="Account" checked> <%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Account")%>
			            <INPUT type="radio" name="rdAccountName<%=i%>" value="Name"><%=languageChoose.getMessage("fi.jsp.groupInfoUpdate.Name")%>
					</p>
					<input type="hidden" name="CheckChoise"></input>
	            </TD>
            <%}
            	row++;
            %>
        </TR>
        <TR>
            <TD class="ColumnLabel">Plan end date</TD>
            <%
				for(int i=0;i<maxColumn;i++){
			%>
	            <TD id="td<%=row%><%=i%>" class="CellBGR3"><INPUT size="9" type="text" tabindex="<%=i*10+row%>" name="planEndDate" maxlength="9">(DD-MMM-YY)</TD>
            <%}
            	row++;
            %>
        </TR>
        <TR>
            <TD class="ColumnLabel">Actual end date</TD>
             <%
				for(int i=0;i<maxColumn;i++){
			%>
            <TD id="td<%=row%><%=i%>" class="CellBGR3"><INPUT size="9" type="text" tabindex="<%=i*10+row%>" name="actualEndDate" maxlength="9">(DD-MMM-YY)</TD>
            <%}
            	row++;
            %>
        </TR>
        
        <TR>
            <TD height="17" class="ColumnLabel">Action Status</TD>
            <%
				for(int i=0;i<maxColumn;i++){
			%>
            <TD id="td<%=row%><%=i%>" height="17" class="CellBGR3"><SELECT name="ActionStatus" tabindex="<%=i*10+row%>" onchange="" class="COMBO">
                <OPTION value="1" selected>Open</OPTION>
                <OPTION value="2" selected>In-progress</OPTION>
                <OPTION value="3" selected>Passed </OPTION>
                <OPTION value="4" selected>Pending</OPTION>
                <OPTION value="5" selected>Cancelled</OPTION>
            </SELECT></TD>
            <%}
            	row++;
            %>
       </TR>
    </TBODY>
</TABLE>
<INPUT type="hidden" name="reqType" value="">
<INPUT type="hidden" name="numMitigation" value="">
<TABLE cellspacing="10" width="100%">
	<TR>
		<TD id="addMore"><a href="javascript:addMore();" tabindex="<%=maxColumn*10%>">Add More Mitigation</a></TD>
		<TD></TD>
		<TD id="removeOne"><a href="javascript:removeOne();" tabindex="<%=maxColumn*10+1%>">Remove One</a></TD>
	</TR>
</TABLE>

<INPUT type="button" tabindex="<%=(maxColumn+1)*10%>" name="ok" value="<%=languageChoose.getMessage("fi.jsp.riskAddnew.OK") %>" onclick="doAction(this);" class="BUTTON">
<INPUT type="button" tabindex="<%=(maxColumn+1)*10+1%>" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.riskAddnew.Cancel") %>" onclick="doAction(this);" class="BUTTON">

<BR>
<BR>
<BR>
</FORM>

<SCRIPT language="JavaScript">

function onCheckAccount(k,event){
	if (trim(frmAddnew.personInChargeAll[k].value) == ""){
		window.alert("<%=languageChoose.getMessage("fi.jsp.woTeamAdd.YouMustInputUserAccount")%>");
		frmAddnew.personInChargeAll[k].focus();
		return ;
	}
	
	var rdName = 'rdAccountName'+k;
    var rd =document.forms['frmAddnew'][rdName];
    var rdAccountName;
    for(var i=0;i<rd.length;i++){
	  if(rd[i].checked){
	  	rdAccountName = rd[i].value;
	  	break;
	  }
    }
    var url = "Fms1Servlet?reqType=<%=Constants.FILLTER_USER%>" + "&Account=" + frmAddnew.personInChargeAll[k].value + "&Type=" + rdAccountName;
	javascript:ajax_showOptions(document.forms['frmAddnew'].personInChargeAll[k], url, '', event);
}

function Back(k){
	frmAddnew.CheckChoise[k].value = 0;
	var pAccBackk = 'pAccBack'+k;
	document.getElementById(pAccBackk).style.display = "none";
	var pAccMorek = 'pAccMore'+k;
	document.getElementById(pAccMorek).style.display = "";
}
function More(k){
	frmAddnew.CheckChoise[k].value = 1;
	var pAccMorek = 'pAccMore'+k;
	document.getElementById(pAccMorek).style.display = "none";
	var pAccBackk = 'pAccBack'+k;
	document.getElementById(pAccBackk).style.display = "";
}

var form = document.forms[0];
var nextHiddenIndex = 1;
var maxColumn = <%=maxColumn%>;
var row = <%=row-1%>;
var rank = '<%=rank%>';

function ChangeNumMitigation(){
	if(frmAddnew.maxMitigation.value < 3 || frmAddnew.maxMitigation.value > 8){
		window.alert("The max value must between 3 and 11");
		frmAddnew.maxMitigation.focus();
		return;
	}
	frmAddnew.reqType.value = "<%=Constants.RISK_ADDNEW_PREP%>";
	frmAddnew.submit();
}
function addMore() {
	for(var i=0;i<row+1;i++){
		document.getElementById("td"+i+nextHiddenIndex).style.display = "";
	}
    if(nextHiddenIndex >= maxColumn-1 )
        document.getElementById("addMore").style.display = "none";
    nextHiddenIndex++;
    if(nextHiddenIndex > 1)
        document.getElementById("removeOne").style.display = "";
}
function removeOne() {
	nextHiddenIndex--;
	for(var i=0;i<row+1;i++){
		document.getElementById("td"+i+nextHiddenIndex).style.display = "none";
	}
    if(nextHiddenIndex < 2)
        document.getElementById("removeOne").style.display = "none";
    if(nextHiddenIndex <= maxColumn-1)
        document.getElementById("addMore").style.display = "";
}
function init(){
	for(var i=0;i<row+1;i++)
		for(var j=1;j<maxColumn;j++)
			document.getElementById("td"+i+""+j).style.display = "none";
			 	
	if(nextHiddenIndex < 2)
        document.getElementById("removeOne").style.display = "none";
    document.getElementById("cmbcategoty").focus();
    if(frmAddnew.lastUpdatedDate.value == "N/A")
    	frmAddnew.lastUpdatedDate.value = "";
    if(frmAddnew.planEndDate.value == "N/A")
    	frmAddnew.planEndDate.value = "";
    if(frmAddnew.actualEndDate.value == "N/A")
    	frmAddnew.actualEndDate.value = "";
}
function initDeveloper(){
	for(var j=0;j<maxColumn;j++)
		Back(j);
}
function CalculateExposure(){
	if(isNaN(frmAddnew.txtProbability.value)){
		window.alert("The value must be a positive number");
		frmAddnew.txtProbability.focus();
		return;
	}
	if(isNaN(frmAddnew.txtImpact.value)){
		window.alert("The value must be a positive number");
		frmAddnew.txtImpact.focus();
		return;
	}
	
	frmAddnew.txtExposure.value = frmAddnew.txtProbability.value * frmAddnew.txtImpact.value;
	
	if(frmAddnew.txtExposure.value > 7.1){
		frmAddnew.priority.value = 1;
	}else if(frmAddnew.txtExposure.value <= 7.0 && frmAddnew.txtExposure.value >=4){
		frmAddnew.priority.value = 2;
	}else if(frmAddnew.txtExposure.value <= 3.9 && frmAddnew.txtExposure.value >= 0.1){
		frmAddnew.priority.value = 3;
	}
}
function Check(){
	if(frmAddnew.txtExposure.value > 7.1){
		frmAddnew.priority.value = 1;
	}else if(frmAddnew.txtExposure.value <= 7.0 && frmAddnew.txtExposure.value >=4){
		frmAddnew.priority.value = 2;
	}else if(frmAddnew.txtExposure.value <= 3.9 && frmAddnew.txtExposure.value >= 0.1){
		frmAddnew.priority.value = 3;
	}else{
		frmAddnew.priority.value = 0;
	}
}

 RiskSourceArray=new Array();
<% int j; 
   RiskSourceInfo sourceInfo;
   for(int i=0;i<riskSource.size();i++){   
   sourceInfo=(RiskSourceInfo) riskSource.elementAt(i);
   j=i*3;%>   
   RiskSourceArray[<%=j++%>]=<%=sourceInfo.categoryID%>;
   RiskSourceArray[<%=j++%>]=<%=sourceInfo.sourceID%>;
   RiskSourceArray[<%=j%>]="<%=languageChoose.getMessage(sourceInfo.sourceName)%>";
<%}%>

function doAction(button) {
  	if (button.name == "cancel"){
  		frmAddnew.reqType.value = "<%=Constants.RISK_LIST_INIT%>";
		frmAddnew.submit();
	}
  	if (button.name == "ok") {
	 	if ((trim(frmAddnew.condition.value) == "") || (frmAddnew.condition.value.length > 200)) {
			window.alert("<%= languageChoose.getMessage("fi.jsp.riskAddnew.Conditionismandatoryandcantbelongerthan200characters")%>");
			frmAddnew.condition.focus();
  			return;
		}
		if ((trim(frmAddnew.txtProbability.value) != "") && (isNaN(trim(frmAddnew.txtProbability.value)) || (frmAddnew.txtProbability.value < 0))) {
			window.alert("Probability must be a positive nuber");
			frmAddnew.txtProbability.focus();
  			return;
		}
  		if(frmAddnew.txtExposure.value > 10){
  			window.alert("Exposure must be a number and less or equal than 10");
  			frmAddnew.txtProbability.focus();
  			return;
  		}
		if ((trim(frmAddnew.txtImpact.value) != "") && (isNaN(trim(frmAddnew.txtImpact.value)) || (frmAddnew.txtImpact.value < 0))) {
			window.alert("Impact must be a positive nuber");
			frmAddnew.txtImpact.focus();
  			return;
		}
  		if ((trim(frmAddnew.riskPriority.value) != "") && (isNaN(trim(frmAddnew.riskPriority.value)) || (frmAddnew.riskPriority.value < 0))) {
			window.alert("Risk priority must be a positive nuber");
			frmAddnew.riskPriority.focus();
  			return;
		}
  		if ((trim(frmAddnew.lastUpdatedDate.value) != "") && !isDate(frmAddnew.lastUpdatedDate.value)) {
  			window.alert("lastUpdatedDate must be formated as DD-MMM-YY");
  			frmAddnew.lastUpdatedDate.focus();
  			return;
  		}
		if ((trim(frmAddnew.lastUpdatedDate.value) != "") && (compareToToday(frmAddnew.lastUpdatedDate.value) == 1)) {
			window.alert("lastUpdatedDate should be today or a day before");
			frmAddnew.lastUpdatedDate.focus();
			return;
	 	}
	 	
	  	for(var i=0;i<nextHiddenIndex;i++){
	  		if ((trim(frmAddnew.planEndDate[i].value) != "") && !isDate(frmAddnew.planEndDate[i].value)) {
	  			window.alert("planEndDate must be formated as DD-MMM-YY");
	  			frmAddnew.planEndDate[i].focus();
	  			return;
	  		}
			<%--
			if ((trim(frmAddnew.planEndDate[i].value) != "") && (compareToToday(frmAddnew.planEndDate[i].value) == 1)) {
				window.alert("planEndDate should be today or a day before");
				frmAddnew.planEndDate[i].focus();
				return;
		 	}
		 	--%>
		 	if ((trim(frmAddnew.actualEndDate[i].value) != "") && !isDate(frmAddnew.actualEndDate[i].value)) {
	  			window.alert("actualEndDate must be formated as DD-MMM-YY");
	  			frmAddnew.actualEndDate[i].focus();
	  			return;
	  		}
			if ((trim(frmAddnew.actualEndDate[i].value) != "") && (compareToToday(frmAddnew.actualEndDate[i].value) == 1)) {
				window.alert("actualEndDate should be today or a day before");
				frmAddnew.actualEndDate[i].focus();
				return;
		 	}
		 	if ((trim(frmAddnew.mitigationCost[i].value) != "") && isNaN(trim(frmAddnew.mitigationCost[i].value))) {
				window.alert("Because risk priority is High and rank of this project is A, so mitigationCost is mandatory and must be a number");
				frmAddnew.mitigationCost[i].focus();
	  			return;
			}
			if (trim(frmAddnew.personInChargeAll[i].value) == "" && frmAddnew.CheckChoise[i].value == 1){
				window.alert("<%=languageChoose.getMessage("fi.jsp.woTeamAdd.YouMustInputUserAccount")%>");
				frmAddnew.personInChargeAll[i].focus();
				return ;
			}
			if(nextHiddenIndex > 1){
				if (trim(frmAddnew.mitigation[i].value) == "") {
					window.alert("mitigation can not blank");
					frmAddnew.mitigation[i].focus();
		  			return;
				}
			}
		}
		if(frmAddnew.priority.value == 1){
			for(var i=0;i<nextHiddenIndex;i++){
				if ((trim(frmAddnew.mitigation[i].value) == "") || (frmAddnew.mitigation[i].value.length > 200)) {
					window.alert("Because risk priority is High, so mitigation is mandatory and length is less than 200");
					frmAddnew.mitigation[i].focus();
		  			return;
				}
				if ((trim(frmAddnew.contingency[i].value) == "") || (frmAddnew.contingency[i].value.length > 200)) {
					window.alert("Because risk priority is High, so contingency is mandatory and length is less than 200");
					frmAddnew.contingency[i].focus();
		  			return;
				}
				if(rank=='A'){
					if (!(trim(frmAddnew.mitigationCost[i].value) != "") && !(frmAddnew.mitigationCost[i].value.length > 200)) {
						window.alert("Because risk priority is High and rank of this project is A, so mitigationCost is mandatory and length is less than 200");
						frmAddnew.mitigationCost[i].focus();
			  			return;
					}
					if (!(trim(frmAddnew.mitigationBenefit[i].value) != "") && !(frmAddnew.mitigationBenefit[i].value.length > 200)) {
						window.alert("Because risk priority is High and rank of this project is A, so mitigationBenefit is mandatory and length is less than 200");
						frmAddnew.mitigationBenefit[i].focus();
			  			return;
					}
				}
			}
		}
		frmAddnew.reqType.value = "<%=Constants.RISK_ADDNEW%>"
		frmAddnew.numMitigation.value = nextHiddenIndex;
	  	frmAddnew.submit();
  	}
}

function loadSourceList(objCategory,objSource){
	objSource.length=0;	
	for (var i=0; i<RiskSourceArray.length;i++){				
		if (objCategory.value==RiskSourceArray[i]){			
			appendOption(objSource, RiskSourceArray[i+1], RiskSourceArray[i+2], false);
		}
		i+=2;
	}	
}

// Add new option element to a Select control
function appendOption(ctrlSelect, strValue, strText, bSelected) {
    var optNew = new Option(strText, strValue);
    ctrlSelect.options[ctrlSelect.length] = optNew;
    if (bSelected == true) {
        optNew.selected = true;
    }
}

init();
initDeveloper();
</SCRIPT> 

</BODY>
</HTML>
