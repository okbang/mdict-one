<%@page import="com.fms1.tools.*"%> 
<%@page import="com.fms1.infoclass.*,com.fms1.infoclass.group.*,com.fms1.tools.*,java.util.*, java.text.*, com.fms1.common.*, com.fms1.web.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>estDefectView.jsp</TITLE>
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
	final DefectByProcessInfo[] defectProcess = (DefectByProcessInfo[])session.getAttribute("defectProcess");
	final String sumPlanValue = (String)session.getAttribute("sumPlanValue");
	int right=Security.securiPage("Defects ",request,response);
%>
<P class="TITLE"><%=languageChoose.getMessage("fi.jsp.estDefectView.EstimateDefect")%></P>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.estDefectView.EstimateDefectTitle")%></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
			<TD width ="30%"><%=languageChoose.getMessage("fi.jsp.estDefectView.ReviewTest")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.estDefectView.Target")%></TD>
			<TD><%=languageChoose.getMessage("fi.jsp.estDefectView.Detected")%></TD>
			<TD width ="30%"><%=languageChoose.getMessage("fi.jsp.estDefectView.Basic")%></TD>
		</TR>
		<%
    	
    	double totalPlanValue = Double.parseDouble(sumPlanValue);
    	
    	int percentage = 0;
    	
		boolean bl=false;
		
		String rowStyle = "CellBGRnews";
		double planValue = Double.NaN;
		String note = "";
		
		DefectByProcessInfo processObj;
		
		for (int i =0;i<6;i++){
			processObj=defectProcess[i] ;
			rowStyle=(bl)?"CellBGRnews":"CellBGR3";
	  		bl=!bl;
	
			planValue = (i <=2) ? processObj.planReview: processObj.planTest;
			note = (i <=2) ? processObj.noteReview: processObj.noteTest;
		
		%>
		<TR class="<%=rowStyle%>">
			<TD><%=languageChoose.getMessage(processObj.processName)%></TD>
			<TD><%=CommonTools.formatDouble(planValue)%></TD>
			<TD><%=CommonTools.formatDouble(planValue*100/totalPlanValue)%></TD>
			<TD><%=ConvertString.toHtml(note)%></TD>
		</TR>
		<%}%>

		<TR class="TableLeft">
			<TD><B><%=languageChoose.getMessage("fi.jsp.DefectView.Total")%></B></TD>
			<TD><B><%=CommonTools.formatDouble(totalPlanValue)%></B></TD>
			<TD><B>100 %</B></TD>
			<TD></TD>
		</TR>
    </TBODY>
</TABLE>
<BR>
<%if (right == 3 && !isArchive){%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.DefectView.Update")%>" onclick="jumpURL('estDefectUpdate.jsp')">
<%}%>
<BR><P></P>
</BODY>
</HTML>
