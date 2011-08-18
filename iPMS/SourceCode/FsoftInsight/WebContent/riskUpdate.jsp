<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<script type="text/javascript" src="jscript/jquery-1.2.2.pack.js"></script>
<style type="text/css">
.ajaxtooltip{
position: absolute; /*leave this alone*/
display: none; /*leave this alone*/
width: 300px;
left: 0; /*leave this alone*/
top: 0; /*leave this alone*/
background: lightyellow;
border: 2px solid gray;
border-width: 1px 2px 2px 1px;
padding: 5px;
}
</style>
<script type="text/javascript" src="jscript/ajaxtooltip.js">
</script>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>riskUpdate.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>

<SCRIPT language="javascript" src="jscript/popcalendar.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="stylesheet/pcal.css">
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
<BODY onLoad="loadPrjMenu();" class="BD">
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
	Vector riskMitigation = (Vector)session.getAttribute("riskMitigation");
	int viewColumn = riskMitigation.size();
	RiskMitigationInfo riskMitigationInfo = new RiskMitigationInfo();
	RiskInfo riskInfo = (RiskInfo)session.getAttribute("riskInfo");
	int row = 0;
	int maxColumn = 6;
	
	// Added by HaiMM - Start
	int addition = 0;
	int mitigationDisplayed = 0;
	int contingencyDisplayed = 0;
	
	String rowStyle;
	
	int maxRow = riskInfo.LINES_MAX;
	int minRow = riskInfo.LINES_MIN_DISPLAY;
	
	Vector riskContingency = (Vector)session.getAttribute("riskContingency");
	int maxContingency = riskContingency.size();
	RiskContingencyInfo riskContingencyInfo = new RiskContingencyInfo();
	
	ProjectDateInfo prjDateInfo=(ProjectDateInfo)session.getAttribute("prjDateInfo");
	
	// Added by HaiMM - End
%>
<%!
String impactTypeCombo(String selected,LanguageChoose languageChoose) {
	String retVal = "";
	String sel;
	for (int i = 0; i < RiskInfo.impactTypes.length; i++) {
		sel = (RiskInfo.impactTypes[i].equals(selected)) ? "selected" : "";
		retVal =
			retVal
				+ "<OPTION value='"
				+ RiskInfo.alfa[i]
				+ "' "
				+ sel
				+ ">"
				+ languageChoose.getMessage(RiskInfo.impactTypes[i])
				+ "</OPTION>";
	}
	return retVal;
}
String impactUnitCombo(String selected) {
	String retVal = "";
	String sel;
	for (int i = 0; i < RiskInfo.impactUnits.length; i++) {
		sel = (RiskInfo.impactUnits[i].equals(selected)) ? "selected" : "";
		retVal =
			retVal
				+ "<OPTION value='"
				+ RiskInfo.alfa[i]
				+ "' "
				+ sel
				+ ">"
				+ RiskInfo.impactUnits[i]
				+ "</OPTION>";
	}
	return retVal;
}
%>
<P class="TITLE"><%=title%></P>
<FORM action="Fms1Servlet#risks" method="post" name="frmUpdate">
<TABLE class="Table" cellspacing="1">
    <COL span="1" width="160">
    <COL span="1" width="400">
    <CAPTION align="left" class="TableCaption">Risk Update</CAPTION>
    <TBODY>
   		<TR>
			<TD class="ColumnLabel" nowrap="nowrap"> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Category")%> </TD>
			<TD class="CellBGR3"><SELECT name="cmbcategoty" class="COMBO" tabindex="<%=row+1%>" id="cmbcategoty" onclick="loadSourceList(document.getElementById('cmbcategoty'),frmUpdate.cmbsource);">
				<%RiskCategoryInfo riskCatInfo;
				for (int i=0;i<riskCategory.size();i++){
					riskCatInfo=(RiskCategoryInfo) riskCategory.elementAt(i);
				%>
					<OPTION value="<%=riskCatInfo.categoryID%>" <%if (riskCatInfo.categoryName.equalsIgnoreCase(riskInfo.categoryName)) {%> selected	<%}%>><%=languageChoose.getMessage(riskCatInfo.categoryName)%></OPTION>
				<%}%>
			</SELECT>
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Source")%> </TD>
			<TD class="CellBGR3"><SELECT name="cmbsource" class="COMBO" tabindex="<%=row+1%>">
				<%RiskSourceInfo riskSourceInfo;
				for (int i=0;i<riskSource.size();i++){
					riskSourceInfo=(RiskSourceInfo) riskSource.elementAt(i);
				%>
					<OPTION value="<%=riskSourceInfo.sourceID%>" <%if (riskSourceInfo.sourceID == riskInfo.sourceID ) {%> selected	<%}%>><%=languageChoose.getMessage(riskSourceInfo.sourceName)%></OPTION>
				<%}%>
			
			
			</SELECT></TD>
		</TR>
		<TR>
            <TD height="57" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Description")%>*</TD>
            <TD height="57" class="CellBGR3"><TEXTAREA rows="3" tabindex="<%=row+1%>" cols="50" name="condition"><%=riskInfo.condition%></TEXTAREA>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Probability") %></TD>
            <TD class="CellBGR3"><input type = "TEXT" name = "txtProbability" size="9" value="<%=CommonTools.formatDouble(riskInfo.probability)%>" tabindex="<%=row+1%>" onblur="CalculateExposure()"></input>
            (* 0.1 - 1.0)
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">Impact</TD>
            <TD class="CellBGR3">
            <A name="plannedImpactTable">
			<TABLE class="Table" cellspacing="1">
				<TBODY>
					<TR class="ColumnLabel">
						<TD><%=languageChoose.getMessage("fi.jsp.riskUpdate.Impactto")%></TD>
						<TD><%=languageChoose.getMessage("fi.jsp.riskUpdate.Unit")%></TD>
						<TD><%=languageChoose.getMessage("fi.jsp.riskUpdate.Impact")%></TD>
					</TR>
					  <% if (riskInfo.pimp != null) {
						for (int i = 0; i < riskInfo.pimp.length; i++) {%>
						<TR class="CellBGR3">
							<TD><SELECT name="impactTo" class="COMBO" id="i<%=i + 1%>">
								<%=impactTypeCombo(riskInfo.pimp[i],languageChoose)%>
							</SELECT></TD>
							<TD><SELECT name="unit" class="COMBO" id="u<%=i + 1%>">
								<%=impactUnitCombo(riskInfo.punt[i])%>
							</SELECT></TD>
							<TD><INPUT size="11" maxlength="11" type="text"
								name="estimatedImpact" style="text-align: right"
								value="<%=(riskInfo.pest[i].equals("N/A") ? "" : riskInfo.pest[i])%>"
								id="e<%=i + 1%>">
							</TD>
						</TR>
						<%}%>
					<%}%>
					
					<% if (riskInfo.pimp == null) {
						for (int i = 0; i < 3; i++) {%>
						<TR class="CellBGR3">
							<TD><SELECT name="impactTo" class="COMBO" id="i<%=i + 1%>">
								<%=impactTypeCombo("",languageChoose)%>
							</SELECT></TD>
							<TD><SELECT name="unit" class="COMBO" id="u<%=i + 1%>">
								<%=impactUnitCombo("")%>
							</SELECT></TD>
							<TD><INPUT size="11" maxlength="11" type="text"
								name="estimatedImpact" style="text-align: right"
								value=""
								id="e<%=i + 1%>">
							</TD>
						</TR>
						<%}%>
					<%}%>
					
				</TBODY>
			</TABLE>
			</A>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">Total Impact</TD>
            <TD class="CellBGR3">
            	<input type = "TEXT" name = "txtImpact" size="9" value="<%=CommonTools.formatDouble(riskInfo.impact)%>" tabindex="<%=row+1%>" onblur="CalculateExposure()"></input>
	            (* 8-10: High, 4.0-7.0: Moderate, 0.0-3.0: Low)
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">Exposure</TD>
            <TD class="CellBGR3">
            	<input style="background-color: rgb(211, 211, 211);" readonly="readonly" type = "TEXT" name = "txtExposure" size="9" value="<%=CommonTools.formatDouble(riskInfo.exposure)%>" tabindex="<%=row+1%>"></input>
            	(* 7.1-10: <font color="#000000">
				<span style="background-color: #FF0000">High</span></font>, 
				4.0-7.0: <span style="background-color: #FFFF00">Medium</span>, 
				0.1-3.9: <span style="background-color: #808000">Low</span>)
            </TD>
        </TR>
        <TR>
        <TR>
        
            <TD class="ColumnLabel">Risk Priority</TD>
            <TD class="CellBGR3"><input type="text" size="9" name="riskPriority" value="<%=riskInfo.riskPriority%>" tabindex="<%=row+1%>">
            (* 1 - 10: Highest to Lowest)
            </TD>
        </TR>
        <TR>
            <TD height="18" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Trigger")%></TD>
            <TD height="18" class="CellBGR3"><TEXTAREA tabindex="<%=row+1%>" rows="3" cols="50" name="triggerName"><%=ConvertString.toHtml(riskInfo.triggerName)%></TEXTAREA>
            </TD>
        </TR>
        <TR>
            <TD height="17" class="ColumnLabel">Risk Status</TD>
            <TD height="17" class="CellBGR3"><SELECT name="riskStatus" class="COMBO" tabindex="<%=row+1%>">
                <OPTION value="1" <%if(riskInfo.riskStatus==1){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Open")%> </OPTION>
                <OPTION value="2" <%if(riskInfo.riskStatus==2){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Occurred")%> </OPTION>
                <OPTION value="3" <%if(riskInfo.riskStatus==3){%>selected<%}%>> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Closed")%> </OPTION>
            </SELECT>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">Last Updated Date</TD>
            <TD class="CellBGR3"><INPUT size="9" type="text" value="<%=CommonTools.dateFormat(riskInfo.lastUpdatedDate)%>" tabindex="<%=row+1%>" name="lastUpdatedDate" maxlength="9">
	            <IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showBEndD()'>
	            (DD-MMM-YY)
            </TD>
        </TR>
        
<!-- Block Option-->
    </TBODY>
</TABLE>

<BR>

<TABLE width="95%" cellspacing="1" class="table">
	<CAPTION class="TableCaption"> Mitigation </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD> # </TD>
			<TD> Mitigation </TD>
			<TD> Mitigation Cost (pd) </TD>
			<TD> Mitigation Benefit </TD>
			<TD> Person in charge </TD>
			<TD> Plan end date </TD>
			<TD> Actual end date </TD>
			<TD> Action Status </TD>
		</TR>
	</THEAD>
	<TBODY><%
	// Display current list (last updated data)
	for (; (row < viewColumn && row < maxRow); row++) {
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		riskMitigationInfo = (RiskMitigationInfo)riskMitigation.elementAt(row);
		%>
		<TR id="miti_row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD> <%=row + 1%> <INPUT type="hidden" name="mitigation_id" value="<%=riskMitigationInfo.riskMitigationId %>"> </TD>
			<TD> <TEXTAREA rows="3" cols="40" name="mitigation" ><%=riskMitigationInfo.mitigation%></TEXTAREA> </TD>
			<TD><input type="text" size="9" name="mitigationCost" id="mitigationCost<%=row%>" value="<%=CommonTools.formatDouble(riskMitigationInfo.mitigationCost)%>"></input> </TD>
			<TD> <TEXTAREA rows="3" cols="40" name="mitigationBenefit" ><%=(riskMitigationInfo.mitigationBenefit==null)?"":riskMitigationInfo.mitigationBenefit%></TEXTAREA> </TD>
			<TD>
	            <SELECT class="COMBO" name="personInCharge" style="width=80">
				<OPTION value="-1"></OPTION>
				<%AssignmentInfo assInfo;
				for (int i=0;i<userList.size();i++){
					assInfo = (AssignmentInfo)userList.elementAt(i);
				%>
					<OPTION value="<%=assInfo.account%>" <%if (assInfo.account.equalsIgnoreCase(riskMitigationInfo.personInCharge)) {%> selected	<%}%>><%=languageChoose.getMessage(assInfo.account)%></OPTION>
				<%}%>

	            </SELECT>
			 </TD>
			<TD nowrap="nowrap"><input type="text" size="9" name="planEndDate" id="planEndDate<%=row%>" value="<%=CommonTools.dateFormat(riskMitigationInfo.planEndDate)%>"></input>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPlanEndDate(<%=row%>)'>															
			</TD>
			<TD nowrap="nowrap"><input type="text" size="9" name="actualEndDate" id="actualEndDate<%=row%>" value="<%=CommonTools.dateFormat(riskMitigationInfo.actualEndDate)%>"></input>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showActualEndDate(<%=row%>)'>															
			</TD>
            <TD><SELECT name="actionStatus" class="COMBO">
				<OPTION value="-1"></OPTION>
                <OPTION value="1" <%if(riskMitigationInfo.actionStatus==1){%>selected<%}%>>Open</OPTION>
                <OPTION value="2" <%if(riskMitigationInfo.actionStatus==2){%>selected<%}%>>In-progress</OPTION>
                <OPTION value="3" <%if(riskMitigationInfo.actionStatus==3){%>selected<%}%>>Passed </OPTION>
                <OPTION value="4" <%if(riskMitigationInfo.actionStatus==4){%>selected<%}%>>Pending</OPTION>
                <OPTION value="5" <%if(riskMitigationInfo.actionStatus==5){%>selected<%}%>>Cancelled</OPTION>
            </SELECT></TD>
		</TR>
		<%
	}
	mitigationDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		%>
		<TR id="miti_row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD> <%=row + 1%> <INPUT type="hidden" name="mitigation_id" value="0"> </TD>
			<TD> <TEXTAREA rows="3" cols="40" name="mitigation" ></TEXTAREA> </TD>
			<TD><input type="text" size="9" name="mitigationCost" id="mitigationCost<%=row%>" value=""></input> </TD>
			<TD> <TEXTAREA rows="3" cols="40" name="mitigationBenefit" ></TEXTAREA> </TD>
			<TD>
	            <SELECT class="COMBO" name="personInCharge" style="width=80">
				<OPTION value="-1"></OPTION>
				<%AssignmentInfo assInfo;
				for (int i=0;i<userList.size();i++){
					assInfo = (AssignmentInfo)userList.elementAt(i);
				%>
					<OPTION value="<%=assInfo.account%>"><%=languageChoose.getMessage(assInfo.account)%></OPTION>
				<%}%>

	            </SELECT>
			 </TD>
			<TD nowrap="nowrap"><input type="text" size="9" name="planEndDate" id="planEndDate<%=row%>" value=""></input>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showPlanEndDate(<%=row%>)'>																		
			</TD>
			<TD nowrap="nowrap"><input type="text" size="9" name="actualEndDate" id="actualEndDate<%=row%>" value=""></input>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showActualEndDate(<%=row%>)'>																		
			</TD>
            <TD><SELECT name="actionStatus" class="COMBO">
                <OPTION value="-1"></OPTION>
                <OPTION value="1">Open</OPTION>
                <OPTION value="2">In-progress</OPTION>
                <OPTION value="3">Passed </OPTION>
                <OPTION value="4">Pending</OPTION>
                <OPTION value="5">Cancelled</OPTION>
            </SELECT></TD>
		</TR><%
	}

	%>
	</TBODY>
</TABLE>
<p id="miti_addMoreLink"><a href="javascript:addMoreRow('miti_')"> Add More Mitigations </a></p>

<BR>

<TABLE width="80%" cellspacing="1" class="table">
	<CAPTION class="TableCaption"> Contingency </CAPTION>
	<THEAD>
		<TR class="ColumnLabel">
			<TD> # </TD>
			<TD> Contingency </TD>
			<TD> Person in charge </TD>
			<TD> Plan end date </TD>
			<TD> Actual end date </TD>
			<TD> Action Status </TD>
		</TR>
	</THEAD>
	<TBODY><%

	// Reset for Contingency
	row = 0;
	addition = 0;
	// Display current list (last updated data)
	for (; (row < maxContingency && row < maxRow); row++) {
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		riskContingencyInfo = (RiskContingencyInfo)riskContingency.elementAt(row);
		%>
		<TR id="row<%=row + 1%>" class="<%=rowStyle%>" valign="top">
			<TD> <%=row + 1%> <INPUT type="hidden" name="contingency_id" value="<%=riskContingencyInfo.riskContingencyId %>"> </TD>
			<TD> <TEXTAREA rows="3" cols="40" name="contingency" ><%=(riskContingencyInfo.contingency==null || riskContingencyInfo.contingency.equalsIgnoreCase("N/A  "))?"":riskContingencyInfo.contingency%></TEXTAREA> </TD>
			<TD>
	            <SELECT class="COMBO" name="contin_personInCharge" style="width=80">
				<OPTION value="-1"></OPTION>
				<%AssignmentInfo assInfo;
				for (int i=0;i<userList.size();i++){
					assInfo = (AssignmentInfo)userList.elementAt(i);
				%>
					<OPTION value="<%=assInfo.account%>" <%if (assInfo.account.equalsIgnoreCase(riskContingencyInfo.personInCharge)) {%> selected	<%}%>><%=languageChoose.getMessage(assInfo.account)%></OPTION>
				<%}%>

	            </SELECT>
			 </TD>
			<TD nowrap="nowrap"><input type="text" size="9" name="contin_planEndDate" id="contin_planEndDate<%=row%>" value="<%=CommonTools.dateFormat(riskContingencyInfo.planEndDate)%>"></input> 
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showContinPlanEndDate(<%=row%>)'>			
			</TD>
			<TD nowrap="nowrap"><input type="text" size="9" name="contin_actualEndDate" id="contin_actualEndDate<%=row%>" value="<%=CommonTools.dateFormat(riskContingencyInfo.actualEndDate)%>"></input>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showContinActualEndDate(<%=row%>)'>						
			</TD>
            <TD><SELECT name="contin_actionStatus" class="COMBO">
				<OPTION value="-1"></OPTION>
                <OPTION value="1" <%if(riskContingencyInfo.actionStatus==1){%>selected<%}%>>Open</OPTION>
                <OPTION value="2" <%if(riskContingencyInfo.actionStatus==2){%>selected<%}%>>In-progress</OPTION>
                <OPTION value="3" <%if(riskContingencyInfo.actionStatus==3){%>selected<%}%>>Passed </OPTION>
                <OPTION value="4" <%if(riskContingencyInfo.actionStatus==4){%>selected<%}%>>Pending</OPTION>
                <OPTION value="5" <%if(riskContingencyInfo.actionStatus==5){%>selected<%}%>>Cancelled</OPTION>
            </SELECT></TD>
		</TR>
		<%
	}
	contingencyDisplayed = row;	// Indicate numbers of lines displayed

	// Display the rest lines
	for (; row < maxRow; row++)
	{
		rowStyle = (row % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		%>
		<TR id="row<%=row + 1%>" class="<%=rowStyle%>" style="display:none" valign="top">
			<TD> <%=row + 1%> <INPUT type="hidden" name="contingency_id" value="0"> </TD>
			<TD> <TEXTAREA rows="3" cols="40" name="contingency" ></TEXTAREA> </TD>
			<TD>
	            <SELECT class="COMBO" name="contin_personInCharge" style="width=80">
				<OPTION value="-1"></OPTION>
				<%AssignmentInfo assInfo;
				for (int i=0;i<userList.size();i++){
					assInfo = (AssignmentInfo)userList.elementAt(i);
				%>
					<OPTION value="<%=assInfo.account%>"><%=languageChoose.getMessage(assInfo.account)%></OPTION>
				<%}%>

	            </SELECT>
			 </TD>
			<TD nowrap="nowrap"><input type="text" size="9" name="contin_planEndDate" id="contin_planEndDate<%=row%>" value=""></input>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showContinPlanEndDate(<%=row%>)'>						
			</TD>
			<TD nowrap="nowrap"><input type="text" size="9" name="contin_actualEndDate" id="contin_actualEndDate<%=row%>" value=""></input>
				<IMG src="image/cal.gif" ONMOUSEOVER="this.style.color = '#FF9C01';this.style.cursor= 'pointer';" onclick='showContinActualEndDate(<%=row%>)'>									
			</TD>
            <TD><SELECT name="contin_actionStatus" class="COMBO">
                <OPTION value="-1"></OPTION>
                <OPTION value="1">Open</OPTION>
                <OPTION value="2">In-progress</OPTION>
                <OPTION value="3">Passed </OPTION>
                <OPTION value="4">Pending</OPTION>
                <OPTION value="5">Cancelled</OPTION>
            </SELECT></TD>
		</TR><%
	}

	%>
	</TBODY>
</TABLE>
<p id="addMoreLink"><a href="javascript:addMoreRow()"> Add More Contingencies </a></p>

<BR>
<INPUT type="hidden" name="reqType" value="">
<INPUT type="hidden" name="numMitigation" value="">
<INPUT type="hidden" name="numMitigationUpdate" value="<%=viewColumn%>">
<INPUT type="hidden" name="riskID" value="<%=riskInfo.riskID%>">
<INPUT type="hidden" name="priority" value="">
<INPUT type="hidden" name="txtPrjStartD" value="<%=CommonTools.dateFormat(prjDateInfo.startD)%>">
<INPUT type="hidden" name="txtPrjEndD" value="<%=CommonTools.dateFormat(prjDateInfo.endD)%>">

<INPUT type="button" name="OK" value="Save" onclick="doAction(this);" class="BUTTON">
<INPUT type="button" name="Cancel" value="<%=languageChoose.getMessage("fi.jsp.riskAddnew.Cancel") %>" onclick="doAction(this);" class="BUTTON">

<BR>
<BR>
<i><b>Note:</b></i> 
<p><i>1. If Risk Source is common, available Mitigation will be proposed to 
project.</i></p>
<p><i>2. If Risk Exposure = High and Status = (Open, Occuring), Mitigation and 
Contigency are automatically added to Other Quality Activities when information 
of these are inputted.</i><br></p>
<BR>

</FORM>

<SCRIPT language="JavaScript">

	function showPlanEndDate(row){
		var woTeam_PlanEndDate = frmUpdate.planEndDate[row];
		showCalendar(woTeam_PlanEndDate, woTeam_PlanEndDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showActualEndDate(row){
		var woTeam_ActualEndDate = frmUpdate.actualEndDate[row];
		showCalendar(woTeam_ActualEndDate, woTeam_ActualEndDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showContinPlanEndDate(row){
		var woTeam_ContinPlanEndDate = frmUpdate.contin_planEndDate[row];
		showCalendar(woTeam_ContinPlanEndDate, woTeam_ContinPlanEndDate, "dd-mmm-yy",null,1,-1,-1,true);
	}
	
	function showContinActualEndDate(row){
		var woTeam_ContinActualEndDate = frmUpdate.contin_actualEndDate[row];
		showCalendar(woTeam_ContinActualEndDate, woTeam_ContinActualEndDate, "dd-mmm-yy",null,1,-1,-1,true);
	}

var rank = '<%=rank%>';

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

function showBEndD(){
		showCalendar(frmUpdate.lastUpdatedDate, frmUpdate.lastUpdatedDate, "dd-mmm-yy",null,1,-1,-1,true);
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

var miti_nextHiddenIndex = <%=mitigationDisplayed + 1%>;
var nextHiddenIndex = <%=contingencyDisplayed + 1%>;
function addMoreRow(prefix) {
    if (prefix == null || prefix == '') {
        getFormElement("row" + nextHiddenIndex).style.display = document.all ? "block" : "table-row";
	    nextHiddenIndex++;
	    if(nextHiddenIndex > <%=maxRow%>)
	        getFormElement("addMoreLink").style.display = "none";
    }
    else {
	     getFormElement("miti_row" + miti_nextHiddenIndex).style.display = document.all ? "block" : "table-row";
		 miti_nextHiddenIndex++;
		 if(miti_nextHiddenIndex > <%=maxRow%>)
		     getFormElement("miti_addMoreLink").style.display = "none";
	}
}


function CalculateExposure(){
	if(isNaN(frmUpdate.txtProbability.value)){
		window.alert("The value must be a number");
		frmUpdate.txtProbability.focus();
		return;
	}
	if(isNaN(frmUpdate.txtImpact.value)){
		window.alert("The value must be a number");
		frmUpdate.txtImpact.focus();
		return;
	}
	
	frmUpdate.txtExposure.value = frmUpdate.txtProbability.value * frmUpdate.txtImpact.value;
	
	if(frmUpdate.txtExposure.value > 7.1){
		frmUpdate.priority.value = 1;
	}else if(frmUpdate.txtExposure.value <= 7.0 && frmUpdate.txtExposure.value >=4){
		frmUpdate.priority.value = 2;
	}else{
		frmUpdate.priority.value = 3;
	}
}

function setPriority(){
	if(frmUpdate.txtExposure.value > 7.1){
		frmUpdate.priority.value = 1;
	}else if(frmUpdate.txtExposure.value <= 7.0 && frmUpdate.txtExposure.value >=4){
		frmUpdate.priority.value = 2;
	}else{
		frmUpdate.priority.value = 3;
	}
}

function doAction(button) {
  	if (button.name == "Cancel"){
  		frmUpdate.reqType.value = "<%=Constants.RISK_LIST_INIT%>";
		frmUpdate.submit();
	}
  	if (button.name == "OK") {
		if ((isValidRiskForm()) && (isValidForm()) && (isPriority())) {
			// Submit		
			frmUpdate.reqType.value = "<%=Constants.RISK_UPDATE%>"
			frmUpdate.numMitigation.value = miti_nextHiddenIndex;
		  	frmUpdate.submit();
	    }
  	}
}

function isValidForm() {
    var result = true;
	var mitigationCost, mitigationBenefit, personInCharge, planEndDate, actualEndDate, actionStatus
	var miti_deleteIndex = -1;
	var miti_deleteItems = "";	// Deleting lines
	var deleteIndex = -1;
	var deleteItems = "";	// Deleting lines
	var miti_notSelectedIndex = -1;
	var miti_notSelectedItems = "";	// Filled information but not selected languages
	var notSelectedIndex = -1;
	var notSelectedItems = "";	// Filled information but not selected languages

    for (i = 0; i < (miti_nextHiddenIndex - 1); i++) {
		mitigationCost = frmUpdate.mitigationCost[i];
		mitigationBenefit = frmUpdate.mitigationBenefit[i];
		personInCharge = frmUpdate.personInCharge[i];

		planEndDate = frmUpdate.planEndDate[i];
		actualEndDate = frmUpdate.actualEndDate[i];
		actionStatus = frmUpdate.actionStatus[i];
		
		// Entered a mitigation
    	if (frmUpdate.mitigation[i].value.length > 0) {
    		if (!isValidData()){
				result = false;
				break;
			} else if (!isValidDate()) {
				result = false;
				break;
			}
    	}
    	// Not selected a mitigation but filled other data
    	else if (frmUpdate.mitigation[i].value.length <= 0 &&
    		!isBlank(mitigationCost, mitigationBenefit, personInCharge, planEndDate, actualEndDate, actionStatus))
    	{
    		miti_notSelectedItems = miti_notSelectedItems + (i + 1) + "; ";
    		
    		if (miti_notSelectedIndex == -1) {
    			miti_notSelectedIndex = i;
    		}
    	}
		// Trying to delete a mitigation, mark this line for confirm later
		else if (frmUpdate.mitigation_id[i].value > 0) {
			miti_deleteItems = miti_deleteItems + (i + 1) + "; ";
			if (miti_deleteIndex == -1) {
				miti_deleteIndex = i;
			}
		}
    }
    // If above checks is invalid => return result(false)
    if (!result) {
    	return result;
    }

	// Section for Contingency -- Start
	
	for (i = 0; i < (nextHiddenIndex - 1); i++) {
		personInCharge = frmUpdate.contin_personInCharge[i];
		
		planEndDate = frmUpdate.contin_planEndDate[i];
		actualEndDate = frmUpdate.contin_actualEndDate[i];
		actionStatus = frmUpdate.contin_actionStatus[i];
		
		// Entered a contingency
    	if (frmUpdate.contingency[i].value.length > 0) {
    		if (!isValidData_Contin()){
				result = false;
				break;
			} else if (!isValidDate_Contin()) {
				result = false;
				break;
			}
    	}
    	// Not selected a contingency but filled other data
    	else if (frmUpdate.contingency[i].value.length <= 0 &&
    		!isBlank_Contin(personInCharge, planEndDate, actualEndDate, actionStatus))
    	{
    		notSelectedItems = notSelectedItems + (i + 1) + "; ";
    		
    		if (notSelectedIndex == -1) {
    			notSelectedIndex = i;
    		}
    	}
		// Trying to delete a contingency, mark this line for confirm later
		else if (frmUpdate.contingency_id[i].value > 0) {
			deleteItems = deleteItems + (i + 1) + "; ";
			if (deleteIndex == -1) {
				deleteIndex = i;
			}
		}
    }
    // If above checks is invalid => return result(false)
    if (!result) {
    	return result;
    }
	// Section for Contingency -- End
    
	// Not selected a language but filled other data
    if (miti_notSelectedIndex >= 0 || notSelectedIndex >= 0) {
    	var msg = "";
    	if (miti_notSelectedIndex >= 0) {
    		msg += "You filled other information but not entered a mitigation at line " + miti_notSelectedItems + "\n";
    	}
    	if (notSelectedIndex >= 0) {
    		msg += "You filled other information but not entered a contingency at line " + notSelectedItems + "\n";
    	}
    	msg += "<%=languageChoose.getMessage("fi.jsp.productLocPages.TheyWillNotBeSaved")%>";
    	result = window.confirm(msg);
    	if (!result) {
    		if (miti_notSelectedIndex >= 0) {
    			frmUpdate.mitigation[miti_notSelectedIndex].focus();
    		}
    		else if (notSelectedIndex >= 0) {
    			frmUpdate.contingency[notSelectedIndex].focus();
    		}
    	}
    }
    // If above checks is invalid => return result(false)
    if (!result) {
    	return result;
    }

    // Trying to remove some lines
    if (miti_deleteIndex >= 0 || deleteIndex >= 0) {
    	var msg = "<%=languageChoose.getMessage("fi.jsp.productLocPages.AreYouSureToDelete")%>\n";
    	if (miti_deleteIndex >= 0) {
    		msg += "Mitigation at line " + miti_deleteItems + "\n";
    	}
    	if (deleteIndex >= 0) {
    		msg += "Contingency at line " + deleteItems;
    	}
    	result = window.confirm(msg);
    	if (!result) {
    		if (miti_deleteIndex >= 0) {
    			frmUpdate.mitigation[miti_deleteIndex].focus();
    		}
    		else if (deleteIndex >= 0) {
    			frmUpdate.contingency[deleteIndex].focus();
    		}
    	}
    }
    return result;

}

function isBlank(mitigationCost, mitigationBenefit, personInCharge, planEndDate, actualEndDate, actionStatus)
{
	return (trim(mitigationCost.value).length <= 0 &&
			trim(mitigationBenefit.value).length <= 0 &&
			trim(personInCharge.value) <= 0 && // value of combo box
			trim(planEndDate.value).length <= 0 &&
			trim(actualEndDate.value).length <= 0 &&
			trim(actionStatus.value) <= 0); // value of combo box

}

function isBlank_Contin(personInCharge, planEndDate, actualEndDate, actionStatus)
{
	return (trim(personInCharge.value) <= 0 && // value of combo box
			trim(planEndDate.value).length <= 0 &&
			trim(actualEndDate.value).length <= 0 &&
			trim(actionStatus.value) <= 0); // value of combo box

}

// Validate Risk Form
function isValidRiskForm() {
	var result = true;
	
 	if ((trim(frmUpdate.condition.value) == "") || (frmUpdate.condition.value.length > 600)) {
		window.alert("Description can not be longer than 600 characters");
		frmUpdate.condition.focus();
  		result = false;
	}
	else if (frmUpdate.triggerName.value.length > 600) {
		window.alert("Trigger can not be longer than 600 characters");
		frmUpdate.triggerName.focus();
		result = false;
	}
	else if ((trim(frmUpdate.txtProbability.value) != "") && (isNaN(trim(frmUpdate.txtProbability.value)) || (frmUpdate.txtProbability.value < 0))) {
		window.alert("Probability must be a positive number");
		frmUpdate.txtProbability.focus();
		result = false;
	}
	else if ((trim(frmUpdate.txtProbability.value) != "") && (trim(frmUpdate.txtProbability.value)) > 1) {
		window.alert("Probability must be a positive number from 0.1 to 1.0 ");
		frmUpdate.txtProbability.focus();
		result = false;
	}
  	else if(frmUpdate.txtImpact.value > 10){
  		window.alert("Total impact must be a number and less or equal than 10");
  		frmUpdate.txtImpact.focus();
  		result = false;
  	}
  	else if(frmUpdate.txtExposure.value > 10){
  		window.alert("Exposure must be a number and less or equal than 10");
  		frmUpdate.txtProbability.focus();
  		result = false;
  	}
  	else if ((trim(frmUpdate.riskPriority.value) != "") && (isNaN(trim(frmUpdate.riskPriority.value)) || (frmUpdate.riskPriority.value < 0) || frmUpdate.riskPriority.value < 1 || frmUpdate.riskPriority.value > 10 || frmUpdate.riskPriority.value.indexOf('.') >= 0)) {
		window.alert("Risk priority must be a integer value between 1 and 10");
		frmUpdate.riskPriority.focus();
  		result = false;
	}
	else if ((trim(frmUpdate.lastUpdatedDate.value) != "") && !isDate(frmUpdate.lastUpdatedDate.value)) {
  		window.alert("Last Updated Date must be formated as DD-MMM-YY");
  		frmUpdate.lastUpdatedDate.focus();
  		result = false;
  	}
	else if ((trim(frmUpdate.lastUpdatedDate.value) != "") && (compareToToday(frmUpdate.lastUpdatedDate.value) == 1)) {
		window.alert("Last Updated Date should be today or a day before");
		frmUpdate.lastUpdatedDate.focus();
		result = false;
	}

	else {
		var i;
		for (i = 0; i < 3; i++)
	  		if (frmUpdate.impactTo[i].value != "a")
	  			break;
	  		if (i == 3) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.riskUpdate.AtleastonerowofPlannedimpactmustbefillwhenplanned")%>");
				frmUpdate.impactTo[0].focus();
				result = false;
		}
	    
	    if (!result) {
    		return result;
	    }

	  	for (i = 0; i < 3; i++) {
			if ((!(frmUpdate.impactTo[i].value == "a") ||
	  		 	  !(frmUpdate.unit[i].value == "a") ||
	  		 	  !(trim(frmUpdate.estimatedImpact[i].value) == "")) && 
				  ((frmUpdate.impactTo[i].value == "a") ||
	  		   	   (frmUpdate.unit[i].value == "a") ||
	  		  	   (frmUpdate.estimatedImpact[i].value == ""))) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.riskUpdate.allfieldshavetobefilled")%>");
	  			frmUpdate.impactTo[i].focus();
		  		result = false;
		  		return result;
	  		}
		}
		
		if (!result) {
    		return result;
	    }
		
		for (i = 0; i < 3; i++) {
			if ((trim(frmUpdate.estimatedImpact[i].value) != "") && (isNaN(frmUpdate.estimatedImpact[i].value) || (frmUpdate.estimatedImpact[i].value < 0))) {
				window.alert("<%=languageChoose.getMessage("fi.jsp.riskUpdate.Estimatedimpactismandatoryandmustbeanumber")%>");
				frmUpdate.estimatedImpact[i].focus();
				result = false;
				return result;
			}
		}
		
		if (!result) {
    		return result;
	    }
		
	}	 	
	return result;
}

// if entered Mitigation, must validate other related data
function isValidData() {
	var result = true;

	if ((trim(frmUpdate.planEndDate[i].value) != "") && !isDate(frmUpdate.planEndDate[i].value)) {
		window.alert("Plan End Date must be formated as DD-MMM-YY");
		frmUpdate.planEndDate[i].focus();
		result = false;
	}
	else if ((trim(frmUpdate.actualEndDate[i].value) != "") && !isDate(frmUpdate.actualEndDate[i].value)) {
		window.alert("Actual End Date must be formated as DD-MMM-YY");
		frmUpdate.actualEndDate[i].focus();
		result = false;
	}
	else if ((trim(frmUpdate.actualEndDate[i].value) != "") && (compareToToday(frmUpdate.actualEndDate[i].value) == 1)) {
		window.alert("Actual End Date should be today or a day before");
		frmUpdate.actualEndDate[i].focus();
		result = false;
 	}
 	else if ((trim(frmUpdate.mitigationCost[i].value) != "") && isNaN(trim(frmUpdate.mitigationCost[i].value))) {
		window.alert("Mitigation Cost must be a positive number");
		frmUpdate.mitigationCost[i].focus();
	  	result = false;
	}
	else if (frmUpdate.mitigationBenefit[i].value.length > 600) {
		window.alert("Mitigation Benefit can not be longer than 600 characters");
		frmUpdate.mitigationBenefit[i].focus();
		result = false;
	}
	else if((frmUpdate.priority.value == 1) && (rank == 'A') && (trim(frmUpdate.mitigationCost[i].value) == "")){
 	 	window.alert("Because risk priority is High and rank of this project is A, so Mitigation Cost is mandatory !");
		frmUpdate.mitigationCost[i].focus();
		result = false;
 	}
	if((frmUpdate.priority.value == 1) && (rank == 'A') && (trim(frmUpdate.mitigationBenefit[i].value) == "")){
 	 	window.alert("Because risk priority is High and rank of this project is A, so Mitigation Benefit is mandatory !");
		frmUpdate.mitigationBenefit[i].focus();
		result = false;
 	}
	else if((frmUpdate.priority.value == 1) && (trim(frmUpdate.personInCharge[i].value) == "-1")){
 	 	window.alert("Because risk priority is High so Person In Charge is mandatory !");
		frmUpdate.personInCharge[i].focus();
		result = false;
 	}

	else if((frmUpdate.priority.value == 1) && (trim(frmUpdate.planEndDate[i].value) == "")){
 	 	window.alert("Because risk priority is High so Plan End Date is mandatory !");
		frmUpdate.planEndDate[i].focus();
		result = false;
 	}


	return result;
}

function isPriority() {
    var result = true;
	var count = 0;
	var totalRow = miti_nextHiddenIndex - 1;
	
 	if((frmUpdate.priority.value == 1) && totalRow == 0 ){
 		window.alert("Because risk priority is High, so Mitigation is mandatory !");
 		result = false;
 	}else if (frmUpdate.priority.value == 1) {
		for(var i=0; i < totalRow; i++){
			if (frmUpdate.mitigation[i].value.length <= 0) {
				count = count + 1;
			}
		}
		if (count == totalRow) {
	 		window.alert("Because risk priority is High, so Mitigation is mandatory !");
			result = false;
		}
 	}
 	return result;
}

// if entered Contingency, must validate other related data
function isValidData_Contin() {
	var result = true;

	if ((trim(frmUpdate.contin_planEndDate[i].value) != "") && !isDate(frmUpdate.contin_planEndDate[i].value)) {
		window.alert("Plan End Date must be formated as DD-MMM-YY");
		frmUpdate.contin_planEndDate[i].focus();
		result = false;
	}
	else if ((trim(frmUpdate.contin_actualEndDate[i].value) != "") && !isDate(frmUpdate.contin_actualEndDate[i].value)) {
		window.alert("Actual End Date must be formated as DD-MMM-YY");
		frmUpdate.contin_actualEndDate[i].focus();
		result = false;
	}
	else if ((trim(frmUpdate.contin_actualEndDate[i].value) != "") && (compareToToday(frmUpdate.contin_actualEndDate[i].value) == 1)) {
		window.alert("Actual End Date should be today or a day before");
		frmUpdate.contin_actualEndDate[i].focus();
		result = false;
 	}

	else if((frmUpdate.priority.value == 1) && (trim(frmUpdate.contin_personInCharge[i].value) == "-1")){
 	 	window.alert("Because risk priority is High so Person In Charge is mandatory !");
		frmUpdate.contin_personInCharge[i].focus();
		result = false;
 	}
 	else if((frmUpdate.priority.value == 1) && (trim(frmUpdate.contin_planEndDate[i].value) == "")){
 	 	window.alert("Because risk priority is High so Plan End Date is mandatory !");
		frmUpdate.contin_planEndDate[i].focus();
		result = false;
 	}
 	
	return result;
}


function calculateImpact() {
	var es1 = new Number(frmUpdate.estimatedImpact[0].value);
	var es2 = new Number(frmUpdate.estimatedImpact[1].value);
	var es3 = new Number(frmUpdate.estimatedImpact[2].value);
	
	if (!isNaN(es1 + es2 + es3)) {
		frmUpdate.txtImpact.value = es1 + es2 + es3 ;
	}
	else {
		frmUpdate.txtImpact.value = 0;
	}
	
	frmUpdate.txtExposure.value = frmUpdate.txtProbability.value * frmUpdate.txtImpact.value;

	if(frmUpdate.txtExposure.value > 7.1){
		frmUpdate.priority.value = 1;
	}else if(frmUpdate.txtExposure.value <= 7.0 && frmUpdate.txtExposure.value >=4){
		frmUpdate.priority.value = 2;
	}else{
		frmUpdate.priority.value = 3;
	}

}

function isValidDate() {
	var result = true;
	
  	if(compareDate(frmUpdate.planEndDate[i].value,frmUpdate.txtPrjEndD.value)==-1){
  		 window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.riskMitiContin.Planned__end__date__~PARAM1_DATE~")%>",frmUpdate.txtPrjEndD.value)));
		 frmUpdate.planEndDate[i].focus();
  		 result = false;
  	}
	if (!result) {
    	return result;
    }

	<%if (prjDateInfo.actualEndDate !=null ) {%>
		if(trim(frmUpdate.actualEndDate[i].value)!=""){ 	
			if(compareDate(frmUpdate.actualEndDate[i].value,"<%=CommonTools.dateFormat(prjDateInfo.actualEndDate)%>")== - 1){
				window.alert("<%= languageChoose.paramText(new String[]{"fi.jsp.riskMitiContin.Actual__~PARAM1_DATE~", CommonTools.dateFormat(prjDateInfo.actualEndDate)})%>");
				frmUpdate.actualEndDate[i].focus();
				result = false;
			}
		}	
	 if (!result) {
    	return result;
	 }
	
	<%}	if (prjDateInfo.actualStartDate !=null ){%>
		  	if(trim(frmUpdate.planEndDate[i].value)!=""){
				if(compareDate(frmUpdate.planEndDate[i].value,"<%=CommonTools.dateFormat(prjDateInfo.actualStartDate)%>")== 1){
					 window.alert("<%= languageChoose.paramText(new String[]{"fi.jsp.riskMitiContin.Plan__after__actual__start__date__of__project__~PARAM1_DATE~", CommonTools.dateFormat(prjDateInfo.actualStartDate)})%>");
					 frmUpdate.planEndDate[i].focus();
					 result = false;
				}
			}

		  	if(trim(frmUpdate.actualEndDate[i].value)!=""){
				if(compareDate(frmUpdate.actualEndDate[i].value,"<%=CommonTools.dateFormat(prjDateInfo.actualStartDate)%>")== 1){
					 window.alert("<%= languageChoose.paramText(new String[]{"fi.jsp.riskMitiContin.Actual__after__actual__start__date__of__project__~PARAM1_DATE~", CommonTools.dateFormat(prjDateInfo.actualStartDate)})%>");
					 frmUpdate.actualEndDate[i].focus();
					 result = false;
				}
			}
	<%}%>
	
	if (!result) {
    		return result;
	}
	
	return result;
}

function isValidDate_Contin() {
	var result = true;
	
  	if(compareDate(frmUpdate.contin_planEndDate[i].value,frmUpdate.txtPrjEndD.value)==-1){
  		 window.alert(getParamText(new Array("<%= languageChoose.getMessage("fi.jsp.riskMitiContin.Planned__end__date__~PARAM1_DATE~")%>",frmUpdate.txtPrjEndD.value)));
		 frmUpdate.contin_planEndDate[i].focus();
  		 result = false;
  	}
	if (!result) {
    	return result;
    }

	<%if (prjDateInfo.actualEndDate !=null ) {%>
		if(trim(frmUpdate.contin_actualEndDate[i].value)!=""){ 	
			if(compareDate(frmUpdate.contin_actualEndDate[i].value,"<%=CommonTools.dateFormat(prjDateInfo.actualEndDate)%>")== - 1){
				window.alert("<%= languageChoose.paramText(new String[]{"fi.jsp.riskMitiContin.Actual__~PARAM1_DATE~", CommonTools.dateFormat(prjDateInfo.actualEndDate)})%>");
				frmUpdate.contin_actualEndDate[i].focus();
				result = false;
			}
		}	
	 if (!result) {
    	return result;
	 }
	
	<%}	if (prjDateInfo.actualStartDate !=null ){%>
		  	if(trim(frmUpdate.contin_planEndDate[i].value)!=""){
				if(compareDate(frmUpdate.contin_planEndDate[i].value,"<%=CommonTools.dateFormat(prjDateInfo.actualStartDate)%>")== 1){
					 window.alert("<%= languageChoose.paramText(new String[]{"fi.jsp.riskMitiContin.Plan__after__actual__start__date__of__project__~PARAM1_DATE~", CommonTools.dateFormat(prjDateInfo.actualStartDate)})%>");
					 frmUpdate.contin_planEndDate[i].focus();
					 result = false;
				}
			}

		  	if(trim(frmUpdate.contin_actualEndDate[i].value)!=""){
				if(compareDate(frmUpdate.contin_actualEndDate[i].value,"<%=CommonTools.dateFormat(prjDateInfo.actualStartDate)%>")== 1){
					 window.alert("<%= languageChoose.paramText(new String[]{"fi.jsp.riskMitiContin.Actual__after__actual__start__date__of__project__~PARAM1_DATE~", CommonTools.dateFormat(prjDateInfo.actualStartDate)})%>");
					 frmUpdate.contin_actualEndDate[i].focus();
					 result = false;
				}
			}
	<%}%>
	
	if (!result) {
    		return result;
	}
	
	return result;
}

init();

setPriority();

function init(){

   if(miti_nextHiddenIndex > <%=maxRow%>) 
       getFormElement("miti_addMoreLink").style.display = "none";

    if(frmUpdate.lastUpdatedDate.value == "N/A")
    	frmUpdate.lastUpdatedDate.value = "";
    	
    for(var i=0; i<miti_nextHiddenIndex - 1; i++){
	    if(frmUpdate.planEndDate[i].value == "N/A")
	    	frmUpdate.planEndDate[i].value = "";
	    if(frmUpdate.actualEndDate[i].value == "N/A")
	    	frmUpdate.actualEndDate[i].value = "";
	}
	
	for(var i=0; i<nextHiddenIndex - 1; i++){
	    if(frmUpdate.contin_planEndDate[i].value == "N/A")
	    	frmUpdate.contin_planEndDate[i].value = "";
	    if(frmUpdate.contin_actualEndDate[i].value == "N/A")
	    	frmUpdate.contin_actualEndDate[i].value = "";
	}
}


</SCRIPT> 

</BODY>
</HTML>
