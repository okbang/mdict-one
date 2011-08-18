<%@page import="com.fms1.infoclass.*, com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>effReview.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript" src="jscript/ScrollableTable.js"></SCRIPT>
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
	int right = Security.securiPage("Effort",request,response);
	Vector vtReview=(Vector)session.getAttribute("reviewEffortVector");
	
	if (vtReview.size() > 20)
	{
%>

<BODY onload="loadPrjMenu();makeScrollableTable('tableReview',true,'auto')" class="BD">
<%
	} else {
%>
<BODY class="BD" onload="loadPrjMenu()">
<%
	}
%>
<p class="TITLE"> <%=languageChoose.getMessage("fi.jsp.effReview.EffortRevieweffort")%> </p>
<p><%=languageChoose.getMessage("fi.jsp.EffReview.Unlessspecifiedtheunitforeffortmetricsispersonday")%></p>
<FORM name=frm method="POST">
<TABLE class="Table" cellspacing="1" width="95%" id="tableReview">
<THEAD>
<TR class="ColumnLabel">
    <TD width="24" align="center">#</TD>
    <TD width="250"> <%=languageChoose.getMessage("fi.jsp.effReview.Product")%> </TD>
    <TD width="100"> <%=languageChoose.getMessage("fi.jsp.effReview.Norm")%> </TD>
    <TD width="100"> <%=languageChoose.getMessage("fi.jsp.effReview.Planned")%> </TD>
    <TD width="100"> <%=languageChoose.getMessage("fi.jsp.effReview.RePlanned")%> </TD>
    <TD width="100"> <%=languageChoose.getMessage("fi.jsp.effReview.Actual")%> </TD>
    <TD> <%=languageChoose.getMessage("fi.jsp.effReview.Deviation")%> </TD>
</TR>
</THEAD>
<TBODY>
        <%         	
         	boolean bl=false;
        	String rowStyle="";

        	double totalNorm = 0;
        	double totalEstimated = 0;
        	double totalReestimated = 0;
        	double totalActual = 0;

        	for(int i=0;i<vtReview.size();i++)
        	{
        		ReviewEffortInfo reviewInfo=(ReviewEffortInfo)vtReview.get(i);
        		
        		rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;      		
        		if (!Double.isNaN(reviewInfo.norm))
	        		totalNorm = totalNorm + reviewInfo.norm;

        		if (!CommonTools.formatDouble(reviewInfo.estimated).equalsIgnoreCase("N/A"))
	        		totalEstimated = totalEstimated + reviewInfo.estimated;

        		if (!CommonTools.formatDouble(reviewInfo.reEstimated).equalsIgnoreCase("N/A"))
	        		totalReestimated = totalReestimated + reviewInfo.reEstimated;

        		if (!CommonTools.formatDouble(reviewInfo.actual).equalsIgnoreCase("N/A"))
	        		totalActual = totalActual + reviewInfo.actual;

        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><%if(right == 3 && !isArchive){%><A href="reviewEffortUpdate.jsp?vtID=<%=i%>"><%=ConvertString.toHtml(reviewInfo.moduleName)%></A><%}else{%><%=ConvertString.toHtml(reviewInfo.moduleName)%><%}%></TD>
            <TD><%=CommonTools.formatDouble(reviewInfo.norm)%></TD>            
            <TD><%=CommonTools.formatDouble(reviewInfo.estimated)%></TD>
            <TD><%=CommonTools.formatDouble(reviewInfo.reEstimated)%></TD>   
            <TD><%=CommonTools.formatDouble(reviewInfo.actual)%></TD>                     
            <TD><%=CommonTools.formatDouble(reviewInfo.deviation)%></TD>      
            
        </TR>
        <%
        	}
        %>        
        <TR class="TableLeft">
            <TD align="center"></TD>
            <TD><B> <%=languageChoose.getMessage("fi.jsp.effReview.Total")%> </B></TD>
            <TD><B><%=CommonTools.formatDouble(totalNorm)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalEstimated)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalReestimated)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalActual)%></B></TD>
            <TD></TD>
        </TR>
    </TBODY>
</TABLE>
<p></p>
</FORM>
</BODY>
</HTML>
