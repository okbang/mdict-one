<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>riskDetail.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/languages.js"></SCRIPT>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad="loadOrgMenu();" class="BD">
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
	RiskInfo riskInfo = (RiskInfo)session.getAttribute("riskInfo");
	Vector riskMitigation = (Vector)session.getAttribute("riskMitigation");
	
	int row = 0;
	int maxColumn = riskMitigation.size();
	RiskMitigationInfo riskMitigationInfo = new RiskMitigationInfo();
	// Modified by HaiMM - 03/DEC/2008
	String rowStyle;
	Vector riskContingency = (Vector)session.getAttribute("riskContingency");
	int maxContingency = riskContingency.size();
	RiskContingencyInfo riskContingencyInfo = new RiskContingencyInfo();
	ProjectInfo projectInfo = (ProjectInfo)session.getAttribute("projectInfo");
	boolean projStatus = false;
	if (projectInfo != null && (projectInfo.getStatus() == 1)) {
		projStatus = true; // click URL from Risk Reference page
	}
	boolean isRiskNo = false;
	if (projectInfo != null && projectInfo.riskNo >= 0) {
		isRiskNo = true;
	}
	
%>

<P class="TITLE"><%=title%></P>
<FORM action="Fms1Servlet#risks" method="get" name="frmDetail">
<TABLE class="Table" cellspacing="1">
    <COL span="1" width="160">
    <COL span="1" width="400">
    <CAPTION align="left" class="TableCaption">Risk Detail</CAPTION>
    <TBODY>
   		<TR>
			<TD class="ColumnLabel" nowrap="nowrap"> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Category")%> </TD>
			<TD class="CellBGR3">
				<%=riskInfo.categoryName%>
			</TD>
		</TR>
		<TR>
			<TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.riskAddnew.Source")%> </TD>
			<TD class="CellBGR3"><%=riskInfo.sourceName%></TD>
		</TR>
		<TR>
            <TD class="ColumnLabel">Description*</TD>
            <TD class="CellBGR3"><%=ConvertString.toHtml(riskInfo.condition)%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Probability") %></TD>
            <TD class="CellBGR3"><%=CommonTools.formatDouble(riskInfo.probability)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">Impact</TD>
            <TD class="CellBGR3">
            <TABLE class="Table" cellspacing="1">
                <COL span="1" width="150">
                <COL span="1" width="100">
                <COL span="1" width="150">
                <TBODY>
                    <TR class="ColumnLabel">
                        <TD><%=languageChoose.getMessage("fi.jsp.riskDetail.Impactto") %></TD>
                        <TD><%=languageChoose.getMessage("fi.jsp.riskDetail.Unit") %></TD>
                        <TD><%=languageChoose.getMessage("fi.jsp.riskDetail.Estimatedimpact") %></TD>
                    </TR>
                    <% if (riskInfo.pimp != null) {
                    	for (int i=0;i<riskInfo.pimp.length;i++){%>
	                    <TR class="CellBGR3">
	                        <TD><%=languageChoose.getMessage(riskInfo.pimp[i])%></TD>
	                        <TD><%=riskInfo.punt[i]%></TD>
	                        <TD><%=riskInfo.pest[i]%></TD>
	                    </TR>
		                <%}%>
                    <%}%>
                </TBODY>
            </TABLE>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.paRiskExport.Exposure")%></TD>
            <TD class="CellBGR3">
            	<%=CommonTools.formatDouble(riskInfo.exposure)%>
            	<%=(riskInfo.priority==1)?" (--> High)":((riskInfo.priority==2)?"(--> Medium)":"(--> Low)")%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">Risk Priority</TD>
            <TD class="CellBGR3"><%=riskInfo.riskPriority%></TD>
        </TR>
        <TR>
            <TD height="18" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.riskAddnew.Trigger")%></TD>
            <TD height="18" class="CellBGR3"><%=ConvertString.toHtml(riskInfo.triggerName)%></TD>
        </TR>
        <TR>
            <TD height="17" class="ColumnLabel">Risk Status</TD>
            <TD height="17" class="CellBGR3"><%
            	switch (riskInfo.riskStatus) {
            	case 1: %> <%=languageChoose.getMessage("fi.jsp.riskDetail.Open")%> <%break;
            	case 2: %> <%=languageChoose.getMessage("fi.jsp.riskDetail.Ocurred")%> <%break;
            	case 3: %> <%=languageChoose.getMessage("fi.jsp.riskDetail.Closed")%> <%break;
            	}%>
            </TD>
        </TR>
        <TR>
            <TD class="ColumnLabel">Last Updated Date</TD>
            <TD class="CellBGR3"><%=(riskInfo.lastUpdatedDate==null)?"":CommonTools.dateFormat(riskInfo.lastUpdatedDate)%></TD>
        </TR>
<!-- Block Option-->        
    </TBODY>
</TABLE>

<p>
<BR>
</p>

<TABLE class="table" cellspacing="1" width="95%" id="table3">
	<CAPTION align="left" class="TableCaption">Mitigation</CAPTION>
	<COLGROUP>
		<COL align="center" width="3%">
		<COL width="20">
		<COL width="10%">
		<COL width="20%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
	</COLGROUP>
	<thead>
		<tr class="ColumnLabel">
			<td> # </td>
			<td> Mitigation </td>
			<td> Mitigation Cost (pd)</td>
			<td> Mitigation Benefit </td>
			<td> Person in charge </td>
			<TD> Plan end date </TD>
			<TD> Actual end date</TD>
			<TD> Action Status</TD>
		</tr>
	</thead>
	<tbody><%
	for (int i = 0; i < maxColumn; i++) {
		rowStyle = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		riskMitigationInfo = (RiskMitigationInfo)riskMitigation.elementAt(i);
		%>
		<TR class="<%=rowStyle%>">
			<TD> <%=i + 1%> </TD>
			<TD> <%=riskMitigationInfo.mitigation%> </TD>
			<TD> <%=CommonTools.formatDouble(riskMitigationInfo.mitigationCost)%> </TD>
			<TD> <%=(riskMitigationInfo.mitigationBenefit==null)?"":riskMitigationInfo.mitigationBenefit%> </TD>
			<TD> <%=(riskMitigationInfo.personInCharge==null)?"":riskMitigationInfo.personInCharge%> </TD>
			<TD> <%=(riskMitigationInfo.planEndDate==null)?"":CommonTools.dateFormat(riskMitigationInfo.planEndDate)%> </TD>
			<TD> <%=(riskMitigationInfo.actualEndDate==null)?"":CommonTools.dateFormat(riskMitigationInfo.actualEndDate)%> </TD>
			<TD> <%
                	if(riskMitigationInfo.actionStatus==1)
                 %>
                	Open
                 <%
                	if(riskMitigationInfo.actionStatus==2)
                 %>
                	In-progress
                 <%
                	if(riskMitigationInfo.actionStatus==3)
                 %>
                	Passed
                 <%
                	if(riskMitigationInfo.actionStatus==4)
                 %>
                	Pending
                 <%
                	if(riskMitigationInfo.actionStatus==5)
                 %>
                	Cancelled
              </TD>
		</TR>
		<%
	}
	%>
	</tbody>
</TABLE>
<p>

<BR>

</p>


<table class="table" cellspacing="1" width="95%" id="table3">
<p class="TableCaption">Contingency</p>
	<COLGROUP>
		<COL align="center" width="2%">
		<COL width="23%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
		<COL width="10%">
	</COLGROUP>
	<thead>
		<tr class="ColumnLabel">
			<td> # </td>
			<td> Contingency </td>
			<td> Person in charge </td>
			<TD width="70"> Plan end date </TD>
			<TD width="67"> Actual end date</TD>
			<TD width="73"> Action Status</TD>
		</tr>
	</thead>
	<tbody><%
	for (int i = 0; i < maxContingency; i++) {
		rowStyle = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		riskContingencyInfo = (RiskContingencyInfo)riskContingency.elementAt(i);
		%>
		<TR class="<%=rowStyle%>">
			<TD> <%=i + 1%> </TD>
			<TD> <%=(riskContingencyInfo.contingency==null || riskContingencyInfo.contingency.equalsIgnoreCase("N/A  "))?"":riskContingencyInfo.contingency%> </TD>
			<TD> <%=(riskContingencyInfo.personInCharge==null)?"":riskContingencyInfo.personInCharge%> </TD>
			<TD> <%=(riskContingencyInfo.planEndDate==null)?"":CommonTools.dateFormat(riskContingencyInfo.planEndDate)%> </TD>
			<TD> <%=(riskContingencyInfo.actualEndDate==null)?"":CommonTools.dateFormat(riskContingencyInfo.actualEndDate)%> </TD>
			<TD> <%
                	if(riskContingencyInfo.actionStatus==1)
                 %>
                	Open
                 <%
                	if(riskContingencyInfo.actionStatus==2)
                 %>
                	In-progress
                 <%
                	if(riskContingencyInfo.actionStatus==3)
                 %>
                	Passed
                 <%
                	if(riskContingencyInfo.actionStatus==4)
                 %>
                	Pending
                 <%
                	if(riskContingencyInfo.actionStatus==5)
                 %>
                	Cancelled
              </TD>
		</TR>
		<%
	}
	%>
	</tbody>
</table>
<p>

<BR>

</p>
<INPUT type="hidden" name="reqType" value="">
<INPUT type="hidden" name="priority" value="">
<INPUT type="hidden" name="riskStatus" value="">
<INPUT type="hidden" name="riskID" value="">
<INPUT type="hidden" name="riskMitigationID" value="">
<INPUT type="hidden" name="riskContingencyID" value="">

<INPUT type="button" name="Back" value="Back" onclick="doBack();" class="BUTTON">
<BR>
<BR>
</FORM>

<SCRIPT language="JavaScript">
/**
var form = document.forms[0];
var nextHiddenIndex = 1;
var maxColumn = <%=maxColumn%>;
var row = <%=row-1%>;
*/

var maxRow = <%=riskMitigation.size()%>;
var priority = <%=riskInfo.priority%>;
var projStatus = <%=projStatus%>;
var riskNo = <%=isRiskNo%>;

function doAction(button) {
  	if (button.name == "Cancel"){
  		if (riskNo == true) {
  			frmDetail.reqType.value = "<%=Constants.PL_OVERVIEW_GET_PAGE%>";
			frmDetail.submit();
  		} 
  		else {
  			frmDetail.reqType.value = "<%=Constants.RISK_LIST_INIT%>";
			frmDetail.submit();
		}
	}
  	if (button.name == "Update") {
		frmDetail.action = "riskUpdate.jsp";
		frmDetail.submit();
  	}
  	if (button.name == "Delete") {
	  	if (window.confirm("<%=languageChoose.getMessage("fi.jsp.riskDetail.Areyousuretodeletethisrisk")%>") != 0) {
			frmDetail.reqType.value = "<%=Constants.RISK_DELETE%>";
			frmDetail.riskID.value = "<%=riskInfo.riskID%>";
			frmDetail.riskStatus.value = "<%=riskInfo.riskStatus%>";
			frmDetail.priority.value = "<%=riskInfo.priority%>";
			
			frmDetail.submit();
		}
  	}
}

function OnDelete(MitigationID){
	if (projStatus == true) {
		window.alert("You can not delete this item !");
		return;	
	}
	else if (maxRow == 1 && priority == 1) {
		window.alert("Because risk priority is High, so Mitigation is mandatory !");
		return;	
	}else if (window.confirm("Are you sure to delete this mitigation ?") != 0) {
		frmDetail.reqType.value = "<%=Constants.RISK_MITIGATION_DELETE%>";
		frmDetail.riskID.value = "<%=riskInfo.riskID%>";
		frmDetail.riskStatus.value = "<%=riskInfo.riskStatus%>";
		frmDetail.priority.value = "<%=riskInfo.priority%>";
		frmDetail.riskMitigationID.value = MitigationID;
		frmDetail.submit();
	}
}

function OnDeleteContigency(contingencyID){
	if (projStatus == true) {
		window.alert("You can not delete this item !");
		return;	
	}
	else if (window.confirm("Are you sure to delete this contingency ?") != 0) {
		frmDetail.reqType.value = "<%=Constants.RISK_CONTINGENCY_DELETE%>";
		frmDetail.riskID.value = "<%=riskInfo.riskID%>";
		frmDetail.riskStatus.value = "<%=riskInfo.riskStatus%>";
		frmDetail.priority.value = "<%=riskInfo.priority%>";
		frmDetail.riskContingencyID.value = contingencyID;
		frmDetail.submit();
	}
}

function doBack(){
	jumpURL('paRisk.jsp');
}


</SCRIPT> 

</BODY>
</HTML>
