<%@page import="java.util.Vector,com.fms1.infoclass.*,java.util.Vector,com.fms1.infoclass.group.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.common.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<TITLE>paRisk.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad="loadOrgMenu();" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.paRisk.ProcessAssetsRisksEncountered")%> </P>

<%
	int right = Security.securiPage("Process Assets",request,response);
	Vector occuredRisk = (Vector) session.getAttribute("vtOccuredRisk");
	String strWuID = (String) session.getAttribute("wuID");
	String strIsPlaned = (String) session.getAttribute("strIsPlaned");
	Vector vtGroupList = (Vector) session.getAttribute("groupList");
	Vector vtOrgList = (Vector) session.getAttribute("orgList");
	
	String fromDate = (String) session.getAttribute("fromDate");
	String toDate = (String) session.getAttribute("toDate");
	
	long lWuID = Parameters.FSOFT_WU; // FSOFT by default
	if (strWuID != null)
		lWuID = Long.parseLong(strWuID);

	if (strIsPlaned == null)
		strIsPlaned = "-1";
	
	int iProcess = CommonTools.parseInt((String) session.getAttribute("strProcess"));

	ReportMonth rm = new ReportMonth();
	if (fromDate == null)
		fromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2,4);

	if (toDate == null)
		toDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2,4);
	
	int j = 0;

	Vector vtProcess = (Vector)session.getAttribute("vtProcess");

%>

<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.PROASS_RISK%>">

<TABLE width="100%" class="NormalText">
	<TR>
		<TD> <%=languageChoose.getMessage("fi.jsp.paRisk.Fromdate")%> </TD>
		<TD><INPUT type="text" name="fromDate" value="<%=fromDate%>" maxlength="9" size="9"> (DD-MMM-YY) </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.paRisk.Todate")%> </TD>
		<TD><INPUT type="text" name="toDate" value="<%=toDate%>" maxlength="9" size="9"> (DD-MMM-YY) </TD>
		<TD> <%=languageChoose.getMessage("fi.jsp.paRisk.Group")%> </TD>
		<TD>
			<SELECT name="cboGroup" class="COMBO">
				<%	j = vtOrgList.size();
					for (int i = 0; i < j; i++)	{
						RolesInfo groupInfo = (RolesInfo) vtOrgList.elementAt(i);
				%><OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
				<%}	
					j = vtGroupList.size();
					for (int i = 0; i < j; i++)	{
						RolesInfo groupInfo = (RolesInfo) vtGroupList.elementAt(i);
				%>
			<OPTION value="<%=groupInfo.workUnitID%>"<%=(groupInfo.workUnitID == lWuID ? " selected" : "")%>><%=groupInfo.workunitName%></OPTION>
				<%}%>
			</SELECT>
		</TD>
		<TD></TD>
	</TR>
	<TR>
	<TD> <%=languageChoose.getMessage("fi.jsp.paRisk.ProcessSource")%>  </TD>
	<TD>
		<SELECT name="cboProcess" class="COMBO">
		<OPTION value="0" <%=(iProcess == 0 ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paRisk.All")%> </OPTION>
			<%	
				j = vtProcess.size();
				for (int i = 0; i < j; i++){
		           	ProcessInfo psInfo = (ProcessInfo)vtProcess.get(i);
			%>
		<OPTION value="<%=psInfo.processId%>"<%=(psInfo.processId == iProcess ? " selected" : "")%>><%=languageChoose.getMessage(psInfo.name)%></OPTION>
			<%}%>
		</SELECT>
	</TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.paRisk.Isunplanned")%> </TD>
	<TD>
		<SELECT name="cboIsPlaned" class="COMBO">
			<OPTION value="-1" <%=(strIsPlaned.equalsIgnoreCase("-1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paRisk.All1")%> </OPTION>
			<OPTION value=1 <%=(strIsPlaned.equalsIgnoreCase("1") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paRisk.Yes")%> </OPTION>
			<OPTION value=0 <%=(strIsPlaned.equalsIgnoreCase("0") ? " selected" : "")%>> <%=languageChoose.getMessage("fi.jsp.paRisk.No")%> </OPTION>
		</SELECT>
	</TD>
	<TD><INPUT type="button" name="btnView" value=" <%=languageChoose.getMessage("fi.jsp.paRisk.View")%> " class="BUTTON" onclick="doAction()"></TD>
	<TD><B><A href="Fms1Servlet?reqType=<%=Constants.GET_PAGE%>&page=paRiskExport.jsp" target="about:blank"> <%=languageChoose.getMessage("fi.jsp.paRisk.ExporttoExcel")%> </A></B></TD>
	</TR>
</TABLE>

</FORM>
<BR>
<FORM name="frmBaseline" action="Fms1Servlet?reqType=<%=Constants.RISK_BASELINE%>" method="POST">
<TABLE cellspacing="1" class="Table" width="100%">
<CAPTION align="left" class="TableCaption"></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
        	
            <TD width = "24" align = "center">#</TD>
            <TD># </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRisk.Project")%> </TD>
            <TD> Description </TD>
            <TD> Mitigation </TD>
            <TD> Contingency  </TD>
            <TD> Plan Impact 1  </TD>
            <TD> Plan Impact 2  </TD>
            <TD> Plan Impact 3 </TD>
        </TR>
	<%
	j = occuredRisk.size();
	String className;
	String sentence;
	for(int i = 0 ;i < j; i++){
		className=(i%2==0)?"CellBGRnews":"CellBGR3";
 		FullRiskInfo fullInfo = (FullRiskInfo) occuredRisk.elementAt(i);
 		RiskInfo info = (RiskInfo)fullInfo.getRiskInfo();
	%>
	<TR class="<%=className%>">
	<TD width = "24" align = "center"><%=i+1%></TD>
	<TD><INPUT TYPE='checkbox' name='baselined' value='<%=i+"'"+(info.baselined ? "checked" : "")+(right==3?"":" disabled")%>'></TD>
	<TD><A href="Fms1Servlet?reqType=<%=Constants.RISK_MENUORG%>&riskID=<%=info.riskID%>"><%=ConvertString.toHtml(info.projectCode)%></A></TD>
	<TD><%=((info.condition == null)?"N/A":ConvertString.toHtml(info.condition))%></TD>
	
	<TD> <%=ConvertString.toHtml(info.mitigation)%> </TD>
	
	<TD> <%=ConvertString.toHtml(info.contingencyPlan)%> </TD>
		<% if (info.pimp != null) {
			for (int k =0;k<info.pimp.length;k++){
				sentence=info.pimp[k].equals("N/A")?"N/A":languageChoose.getMessage(info.pimp[k]) + ": " + info.pest[k] + info.punt[k]  ;
			    %><TD><%=sentence%></TD>
		   <%}
		   } else {%>
		   		<TD>N/A</TD>
		   		<TD>N/A</TD>
		   		<TD>N/A</TD>
		   <%}%>
    </TR>
	<%}%>
</TABLE>
<P>
<%if(right==3){%>
<INPUT type='submit' class='BUTTONWIDTH' value='<%=languageChoose.getMessage("fi.jsp.paRisk.Baseline")%>'>
<%}%>
</FORM>
</BODY>
<SCRIPT language="JavaScript">
  function doAction() {
  	if(frm.fromDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.paRisk.Thisfieldismandatory")%>");
  	 	frm.fromDate.focus();
  	 	return;
  	}
	if (!isDate(frm.fromDate.value)){
 		window.alert("<%=languageChoose.getMessage("fi.jsp.paRisk.Invaliddatevalue")%>");
  		frm.fromDate.focus();
  		return;
  	}

  	if(frm.toDate.value==""){
  		window.alert("<%=languageChoose.getMessage("fi.jsp.paRisk.Thisfieldismandatory")%>");
  	 	frm.toDate.focus();
  	 	return;
  	}
	if (!isDate(frm.toDate.value)){
 		window.alert("<%=languageChoose.getMessage("fi.jsp.paRisk.Invaliddatevalue")%>");
  		frm.toDate.focus();
  		return;
  	}

  	if(compareDate(frm.fromDate.value, frm.toDate.value) == -1){
  	 	window.alert("<%=languageChoose.getMessage("fi.jsp.paRisk.FromdatemustbebeforeTodate")%>");
  		frm.fromDate.focus();
  		return;
  	}
  	
  	
  	frm.submit();
  }
  var objToHide = new Array(frm.cboProcess);
</SCRIPT >
</HTML>
