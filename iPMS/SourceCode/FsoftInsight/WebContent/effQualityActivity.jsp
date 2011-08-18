<%@page import="com.fms1.infoclass.*, com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">


<LINK href="stylesheet/fms.css" rel="stylesheet"
	type="text/css">
<TITLE>effQualityActivity.jsp</TITLE>
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
	int right = Security.securiPage("Effort",request,response);
	Vector vtQlt=(Vector)session.getAttribute("qltActivityEffortVector");
%>
<BODY onload="loadPrjMenu()" class="BD">
<p class="TITLE"> <%=languageChoose.getMessage("fi.jsp.effQualityActivity.EffortOtherqualityactivities")%> </p>
<p><%=languageChoose.getMessage("fi.jsp.EffQualityActivity.Unlessspecifiedtheunitforeffortmetricsispersonday")%></p>
<FORM name=frm method="POST">
<TABLE class="Table" cellspacing="1" width="95%">
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="24" align="center">#</TD>
            <TD class="ColumnLabel" width="25%"> <%=languageChoose.getMessage("fi.jsp.effQualityActivity.Activity")%> </TD>
            <TD class="ColumnLabel" width="25%"> <%=languageChoose.getMessage("fi.jsp.effQualityActivity.Norm")%> </TD>                       
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.effQualityActivity.Planned")%> </TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.effQualityActivity.RePlaned")%> </TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.effQualityActivity.Actual")%> </TD>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.effQualityActivity.Deviation")%> </TD>
            
        </TR>
        <%        
          	boolean bl=false;
        	String rowStyle="";
        	String estimated="";        	
        	String reEstimated="";
        	String actual;
 
         	double totalNorm = 0;
        	double totalEstimated = 0;
        	double totalReestimated = 0;
        	double totalActual = 0;

        	for(int i=0;i<vtQlt.size();i++)
        	{
        		QltActivityEffortInfo qltInfo=(QltActivityEffortInfo)vtQlt.get(i);
        		rowStyle=(bl)?"CellBGRnews": "CellBGR3";
  				bl=!bl;
  				estimated="N/A";
  				reEstimated="N/A";
  				actual="N/A";
        		if(qltInfo.estimated>0){
        			estimated=CommonTools.formatDouble(qltInfo.estimated);
	        		totalEstimated = totalEstimated + (double) qltInfo.estimated;
        		}
        		if(qltInfo.reEstimated>0){
        			reEstimated=CommonTools.formatDouble(qltInfo.reEstimated);
	        		totalReestimated = totalReestimated + (double) qltInfo.reEstimated;
        		}
        		
        		if(qltInfo.actual>0){
        			actual=CommonTools.formatDouble(qltInfo.actual);
	        		totalActual = totalActual + (double) qltInfo.actual;
        		}

        		if (!"N/A".equalsIgnoreCase(qltInfo.norm))
	        		totalNorm = totalNorm + CommonTools.parseDouble(qltInfo.norm);


        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><%if(right == 3 && !isArchive){%><A href="qltActivityEffortUpdate.jsp?vtID=<%=i%>"><%=ConvertString.toHtml(qltInfo.activity)%></A><%}else{%><%=ConvertString.toHtml(qltInfo.activity)%><%}%></TD>
            <TD><%=qltInfo.norm%></TD>            
            <TD><%=estimated%></TD>
            <TD><%=reEstimated%></TD> 
             <TD><%=actual%></TD>                      
            <TD><%=qltInfo.deviation%></TD>      
            
        </TR>
        <%
        	}
        %>
        <TR class="TableLeft">
            <TD align="center"></TD>
            <TD><B> <%=languageChoose.getMessage("fi.jsp.effQualityActivity.Total")%> </B></TD>
            <TD><B><%=CommonTools.formatDouble(totalNorm)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalEstimated)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalReestimated)%></B></TD>
            <TD><B><%=CommonTools.formatDouble(totalActual)%></B></TD>
            <TD><B></B></TD>
        </TR>
    </TBODY>
</TABLE>
<p></p>

</FORM>
</BODY>
</HTML>
