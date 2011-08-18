<%@page import="com.fms1.infoclass.*,com.fms1.tools.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*, java.sql.Date" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>riskList.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
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
<BODY onload="loadPrjMenu();javascript:Import()" class="BD">
<%
	session.setAttribute("change_source_page", "Risks");
    int right = Security.securiPage("Risks",request,response);    
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.riskList.Risks") %></P>
<TABLE cellspacing="1" class="HDR" width="95%">
	<TBODY>
		<TR>
			<TD width="10%"></TD>
			<TD></TD>
			<TD align="right"></TD>
		</TR>
	</TBODY>
</TABLE>
<FORM name="frm_Import" action="Fms1Servlet?reqType=<%=Constants.RISK_IMPORT%>" enctype="MULTIPART/FORM-DATA" method=POST >
<TABLE cellspacing="1" class="HDR" width="95%" id="ImportTable">
	<TBODY>
		<TR>
			<TD align="right"><STRONG>File Name&nbsp;*</STRONG></TD>
	        <TD align="left">
	        	<INPUT type="file" name="importFile">
	       		<INPUT type="button" name="Import" onclick="checkName()" value=" Import " class="Button">
            	<INPUT type="hidden" name="filename">
            </TD>	
	    </TR>
	    <TR>
	    	<TD></TD>
			<TD align="center"><A href="Template_Import_RISK.xls"><FONT color="blue" class="label1">Download Template File</FONT></A></TD>	    
	    </TR>    
	</TBODY>
</TABLE>
</FORM>

<%
String checkImport = (String)session.getAttribute("Imported");
if(checkImport == "true"){
int[] result = (int[])session.getAttribute("AddedRecord");
%><br>Imported successfull records:<%
int l = 0;
int k=0;
	while(l < 50){
		if(result[l] > 0){
			if(k>0){
				%> ,<%
			}
			%>
			<%=result[l]%>
			<%
			k++;
		}
		l++;
	}
	session.removeAttribute("Imported");
	session.removeAttribute("AddedRecord");
}	
%>
<%
String isImport = (String)session.getAttribute("ImportFail");
if(isImport != null && isImport == "fail"){
	%><br><p style="color: red;">Import Fail<%	
	session.removeAttribute("ImportFail");
}
%>
<BR>
<TABLE class="Table" cellspacing="1" width="95%">
    <COL span="1" width="4%" align="center">
    <COL span="1" width="50%">
    <COL span="1" width="20%">
    <COL span="1" width="9%">
    <TBODY>
        <TR class="ColumnLabel">
            <TD>#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.riskList.Description")%> </TD>
            <TD><%=languageChoose.getMessage("fi.jsp.riskList.LastUpdatedDate") %></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.riskList.Status")%> </TD>
        </TR>
        <%
        Vector riskList = (Vector)session.getAttribute("riskList");
        String strRiskBookmark=(String)session.getAttribute("riskBookmark");
        int riskBookmark=0;
        if (strRiskBookmark!=null)
            riskBookmark = Integer.parseInt(strRiskBookmark);
        int riskEnd = riskList.size();
        
        if (riskList != null) {
			for (int i = riskBookmark; i < riskEnd; i++) {
				RiskInfo risk = (RiskInfo)riskList.elementAt(i);
				String className = "";
				java.util.Date date = null;
				if(risk.lastUpdatedDate != null)
					date = new java.util.Date(risk.lastUpdatedDate.getTime());
					
				String trimmedActualScenario = risk.actualRiskScenario;
				
				String status = "";
				switch (risk.riskStatus) {
					case 1: status = languageChoose.getMessage("fi.jsp.riskList.Open");break;
					case 2: status = languageChoose.getMessage("fi.jsp.riskList.Occurred");break;
					case 3: status = languageChoose.getMessage("fi.jsp.riskList.Closed");break;
				}
				
				className = "CellBGR3";
				switch (risk.priority) {
					case 1: className = "RED";break;
					case 2: className = "YELLOW";break;
					case 3: className = "CellBGROLIVE";break;
				}
			
		%>
        <TR class="<%=className%>">
            <TD><%=i+1%></TD>
            <TD><A href="Fms1Servlet?reqType=<%=Constants.RISK_UPDATE_PREP%>&riskID=<%=risk.riskID%>"><%=ConvertString.toHtml(risk.condition)%></A></TD>
            <TD><%=CommonTools.dateFormat(date)%></TD>
            <TD><%=status%></TD>
        </TR>
        <%
			}
		}

		%>
        <TR align="right">
            <TD colspan="5" class="TableLeft" align="right"><%=languageChoose.getMessage("fi.jsp.riskList.Page")%><%
            //int riskPage = riskBookmark/10 + 1;//@
            //int riskPageNumber = (riskList.size() - 1)/10 + 1;//@
            int riskPage = 1;//@
            int riskPageNumber = 1;//@
			%>
			<%=riskPage%>/<%=riskPageNumber%>
				<%if (riskPage > 1) {%>
				<A href="Fms1Servlet?reqType=<%=Constants.RISK_PREV%>"><%=languageChoose.getMessage("fi.jsp.riskList.Prev")%></A> 
				<%}
				if (riskPage < riskPageNumber) {%>
				<A href="Fms1Servlet?reqType=<%=Constants.RISK_NEXT%>"><%=languageChoose.getMessage("fi.jsp.riskList.Next")%></A> 
				<%}%>
			</TD>
        </TR>
    </TBODY>
</TABLE>
<%if(right == 3 && !isArchive) {%>
<FORM name="frmRiskIdentify" action="Fms1Servlet" method="post">
<INPUT type="hidden" name="reqType" value="">
<BR>
<INPUT type="button" name="identify" value=" <%=languageChoose.getMessage("fi.jsp.riskList.Identify")%> " onclick="identifyRisk()" class="BUTTON">
<INPUT type="button" name="otherRisk" value="<%=languageChoose.getMessage("fi.jsp.riskCommon.RiskReference")%>" onclick="otherRiskList()" class="BUTTONWIDTH">
</FORM>
<%}%>

<SCRIPT language="javascript">
var isImportHide = true;
var isMigrate = false; // set True for migrate
	function Import(){
		isImportHide = !isImportHide;
	 	var ImportTable = document.getElementById("ImportTable");
  		if (isImportHide) {
    		ImportTable.style.display="";
   		}else{
    		ImportTable.style.display="none";
		}
    }
</SCRIPT>

<SCRIPT language="javascript">
function doImport(ext) {
    if (ext != '') {
        document.frm_Import.submit();
    }
}

function checkName() {
    var name = document.frm_Import.importFile.value;
    
    if (name == "" || name == null) {
        alert("Select MS Excel file!");
        document.frm_Import.importFile.focus();
        return;
    }
    else {
        var ext = name.substring(name.lastIndexOf(".") + 1, name.length);
        ext = ext.toLowerCase();
        if (ext != "xls") {
            alert("Select MS Excel file!");
            document.frm_Import.importFile.focus();
            return;
        }
        else {
            doImport(ext);
        }
    }
}

function otherRiskList() {
	frmRiskIdentify.reqType.value="<%=Constants.RISK_LIST_OTHER%>";
 	frmRiskIdentify.submit();
}

function identifyRisk() {
	if (isMigrate == true) {
		frmRiskIdentify.reqType.value="<%=Constants.RISK_MIGRATE%>";
 		frmRiskIdentify.submit();
	}
	else {	
		frmRiskIdentify.reqType.value="<%=Constants.RISK_IDENTIFY%>";
 		frmRiskIdentify.submit();
 	}
}

</SCRIPT>
<BR>
<i><b>* Note:</b></i> Risk priority <BR>

<div style="position: absolute; visibility: hidden; background-color: rgb(145, 151, 145); width: 140px; height: 64px; font-family: verdana,geneva,arial,helvetica,sans-serif; font-weight: bold; font-style: normal; font-size: 9pt; z-index: 102; top: 0pt; left: 0pt;"><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 1px;">Weekly</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 22px;">Milestone</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 43px;">Post mortem</div></div><div style="position: absolute; visibility: hidden; background-color: rgb(145, 151, 145); width: 140px; height: 64px; font-family: verdana,geneva,arial,helvetica,sans-serif; font-weight: bold; font-style: normal; font-size: 9pt; z-index: 102; top: 0pt; left: 0pt;"><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 1px;">Dist. rates</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 22px;">Estim. methods</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 43px;">Conversion rates</div></div><div style="position: absolute; visibility: hidden; background-color: rgb(145, 151, 145); width: 140px; height: 127px; font-family: verdana,geneva,arial,helvetica,sans-serif; font-weight: bold; font-style: normal; font-size: 9pt; z-index: 102; top: 0pt; left: 0pt;"><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 1px;">Information</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 22px;">Prod. defect plan</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 43px;">Stage defect plan</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 64px;">Progress</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 85px;">DPLog</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 106px;">Common defects</div></div><div style="position: absolute; visibility: hidden; background-color: rgb(145, 151, 145); width: 140px; height: 43px; font-family: verdana,geneva,arial,helvetica,sans-serif; font-weight: bold; font-style: normal; font-size: 9pt; z-index: 102; top: 126px; left: 0px;"><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 1px;">Products &amp; size</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 22px;">Product size (LOC)</div></div><div style="position: absolute; visibility: hidden; background-color: rgb(145, 151, 145); width: 140px; height: 106px; font-family: verdana,geneva,arial,helvetica,sans-serif; font-weight: bold; font-style: normal; font-size: 9pt; z-index: 102; top: 0pt; left: 0pt;"><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 1px;">Information</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 22px;">Stage&amp;process</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 43px;">Review</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 64px;">Other quality activ.</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 85px;">Weekly</div></div><div style="position: absolute; visibility: hidden; background-color: rgb(145, 151, 145); width: 140px; height: 169px; font-family: verdana,geneva,arial,helvetica,sans-serif; font-weight: bold; font-style: normal; font-size: 9pt; z-index: 102; top: 84px; left: 0px;"><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 1px;">Information</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 22px;">Stage &amp; Iteration</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 43px;">Review &amp; Test</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 64px;">Other quality</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 85px;">Training</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 106px;">Dependencies</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 127px;">Subcontracts</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 148px;">Finance</div></div><div style="position: absolute; visibility: hidden; background-color: rgb(145, 151, 145); width: 170px; height: 64px; font-family: verdana,geneva,arial,helvetica,sans-serif; font-weight: bold; font-style: normal; font-size: 9pt; z-index: 102; top: 0pt; left: 0pt;"><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 158px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 1px;">Information</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 158px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 22px;">Stage &amp; process RCR</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 158px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 43px;">Status &amp; traceability</div></div><div style="position: absolute; visibility: hidden; background-color: rgb(145, 151, 145); width: 140px; height: 211px; font-family: verdana,geneva,arial,helvetica,sans-serif; font-weight: bold; font-style: normal; font-size: 9pt; z-index: 102; top: 42px; left: 0px;"><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 1px;">Information</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 22px;">Deliv. &amp; Depend.</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 43px;">Lifecycle</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 64px;">Organization</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 85px;">Training</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 106px;">Tools</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 127px;">Finance</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 148px;">Quality</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 169px;">Changes</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 190px;">Signatures</div></div><div style="position: absolute; visibility: hidden; background-color: rgb(145, 151, 145); width: 140px; height: 148px; font-family: verdana,geneva,arial,helvetica,sans-serif; font-weight: bold; font-style: normal; font-size: 9pt; z-index: 102; top: 21px; left: 0px;"><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 1px;">Information</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 22px;">Deliverables</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 43px;">Metrics</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 64px;">Team</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 85px;">Changes</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 106px;">Acceptance</div><div style="overflow: hidden; position: absolute; visibility: inherit; cursor: default; color: black; background-color: rgb(195, 212, 228); text-align: left; width: 128px; height: 19px; padding-left: 10px; padding-top: 1px; left: 1px; top: 127px;">Signatures</div></div>
<table class="Table" cellspacing="1" width="16%" id="table1">
        
        <tr class="CellBGRnews" style="color: red;">
            <td width="64" align="center" style="border-right-style: none; border-right-width: medium; background-color: #FF0000">&nbsp;</td>
            <td width="63" style="border-style: none; border-width: medium" align="left">
			<font color="#000000">&nbsp;High</font></td>
        </tr>
        
        <tr class="CellBGR3" style="color: red;">
            <td width="64" style="border-right-style: none; border-right-width: medium; background-color: #FFFF00">&nbsp;</td>
            <td width="63" style="border-style: none; border-width: medium" align="left">
			<font color="#000000">&nbsp;Medium</font></td>
        </tr>
        
        <tr class="CellBGR3" style="color: red;">
            <td width="64" height="23" style="border-right-style: none; border-right-width: medium; background-color: #808000">&nbsp;</td>
            <td width="63" height="23" style="border-style: none; border-width: medium" align="left">
			<p align="left"><font color="#000000">&nbsp;Low</font></td>
        </tr>
        
</table>

</BODY>
</HTML>
