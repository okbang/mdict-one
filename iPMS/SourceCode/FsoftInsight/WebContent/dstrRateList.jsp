<%@page import="com.fms1.infoclass.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
	<TITLE>dstrRateList.jsp</TITLE>
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
%>
<BODY onload="loadPrjMenu()" class="BD">
<%
	int right = Security.securiPage("Project parameters", request, response);
	Vector effortStgVector = (Vector)request.getAttribute("EffortStageList");
	Vector effortDstVector = (Vector)request.getAttribute("EffortProcList");
	Vector defectDstVector = (Vector)request.getAttribute("DefectProcList");
	Vector completenessList = (Vector)session.getAttribute("CompletenessList");
	String className;
%>
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.dstrRateList.DistributionRates")%> </P>
<P class="HDR"> <%=languageChoose.getMessage("fi.jsp.dstrRateList.LastUpdate")%> <%=CommonTools.dateFormat(((CompletenessRateInfo)completenessList.elementAt(0)).lastUpdate)%></P>
<TABLE cellspacing="1" class="Table" width="95%">
    <COL span="1" width="15%">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.dstrRateList.CompletenessRates")%>(%) </CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
	<%
    	for (int j = 0; j < RequirementInfo.statusList.length; j++) {
	%>
            <TD><%=languageChoose.getMessage(RequirementInfo.getStatusName(j))%></TD>
	<%
		}
	%>
        </TR>
	<%
		CompletenessRateInfo cpltInfo = null;
	%>
		<TR class="CellBGR3">
	<%
		for (int j = 0; j < RequirementInfo.statusList.length; j++) {
			for (int k = 0; k < completenessList.size(); k++) {
				cpltInfo = (CompletenessRateInfo)completenessList.elementAt(k);
				if (cpltInfo.statusID == RequirementInfo.statusList[j]) {
	%>
			<TD><%=CommonTools.formatDouble(cpltInfo.value)%></TD>
	<%
					break;
				}
			}	
		}
	%>
        </TR>
    </TBODY>
</TABLE>
<%
	if (right == 3 && !isArchive) {
%>
	<P><INPUT type="button" value="<%=languageChoose.getMessage("fi.jsp.dstrRateList.Update")%>" class="BUTTON" onClick="jumpURL('dstrRateUpdate.jsp')"></P>
<%
	}
%>
<%
	Vector [] arrVectors = {effortDstVector,defectDstVector};
	String [] arrNames = {languageChoose.getMessage("fi.jsp.dstrRateList.EffortDistributionByProcess"), languageChoose.getMessage("fi.jsp.dstrRateList.DefectDistributionByOrigin")};
	Vector theVector;
	String theName;
	for (int iArr = 0 ; iArr < arrVectors.length; iArr++) {
		theVector = arrVectors[iArr];
		theName = arrNames[iArr];
%>
<TABLE cellspacing="1" class="Table" width="95%">
    <COL span="1" width="15%">
    <CAPTION class="TableCaption" style="padding-top: 10px"><%=theName%></CAPTION>
    <TBODY>
	<%
		Vector dstList = new Vector();
		dstList = (Vector)theVector.elementAt(0);
	%>
		<!-- Begin the first row : The Name of Process -->
		<TR class="ColumnLabel">
			<!-- Begin the first column : The Name of Life Cycle -->
			<TD><%=languageChoose.getMessage("Life cycle")%></TD>
			<!-- End the first column : The Name of Life Cycle -->
	<%
		for (int j = 0; j < dstList.size(); j++) {
			NormRefInfo nref = (NormRefInfo) dstList.elementAt(j);
	%>		
            <TD><%=languageChoose.getMessage(ConvertString.toHtml(nref.prcName))%></TD>
	<%
		}
	%>
		</TR>
		<!-- End the first row : The Name of Process -->
	<%
		for (int i = 0; i < theVector.size(); i++) {
			dstList = (Vector)theVector.elementAt(i);
			className = (i%2 == 0) ? "CellBGRnews" : "CellBGR3";
	%>
		<TR class="<%=className%>">
			<!-- Begin the first column : The Name of Life Cycle -->
			<TD><%=languageChoose.getMessage(ProjectInfo.parseLifecycle(ProjectInfo.lifecycles[i]))%></TD>
			<!-- End the first column : The Name of Life Cycle -->
	<%
			for (int j = 0; j < dstList.size(); j++) {
				NormRefInfo nref = (NormRefInfo) dstList.elementAt(j);
	%>
			<TD><%=languageChoose.getMessage(CommonTools.formatDouble(nref.value))%></TD>
	<%
			}
		}
	%>
		</TR>
    </TBODY>
</TABLE>
<%
	}
%>
<TABLE cellspacing="1" class="Table" width="95%">
    <COL span="1" width="15%">
    <CAPTION class="TableCaption" style="padding-top: 10px"> <%=languageChoose.getMessage("fi.jsp.dstrRateList.Effortdistributionbystage")%> </CAPTION>
    <TBODY>
		<TR class="ColumnLabel">
	<%
    	for (int k = 0; k < StageInfo.stageNames.length; k++) {
	%>
			<TD><%=languageChoose.getMessage(StageInfo.stageNames[k])%></TD>
	<%
		}
	%>
		</TR>    	
    	<TR class="CellBGR3">
	<%
		for (int j = 0; j < effortStgVector.size(); j++) {
	%>
			<TD><%=CommonTools.formatDouble(((NormRefInfo)effortStgVector.elementAt(j)).value)%></TD>
	<%
		}
	%>
        </TR>
    </TBODY>
</TABLE>
</BODY>
</HTML>