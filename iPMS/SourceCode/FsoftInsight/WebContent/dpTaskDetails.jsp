<%@page import="com.fms1.infoclass.*, com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML> 
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>dpTaskDetails.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	String sourcePage = (String) session.getAttribute("pageSourceQuality");
	//HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))){
		if (Integer.parseInt(archiveStatus) == 4){
			isArchive = true;
		}
	}
	int right = Security.securiPage("Project reports",request,response);
	Vector dpVt = (Vector)session.getAttribute("defectPrevention");
	String vtIDstr = request.getParameter("vtID");
	int vtID = Integer.parseInt(vtIDstr);
//	int caller = Integer.parseInt((String)session.getAttribute("caller"));
	DPTaskInfo dpTaskInfo = (DPTaskInfo)dpVt.get(vtID);
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.dpTaskDetails.Defectpreventiongoals")%> </p>
<FORM method="POST" action="dpTaskUpdate.jsp?vtID=<%=vtID%>" name="frm">
<input type="hidden" name="source" value=""> 
<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.dpTaskDetails.Defectpreventiontask")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Name1")%> </TD>
            <TD class="CellBGRnews"><%=dpTaskInfo.item%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Unit1")%> </TD>
            <TD class="CellBGRnews"><%=dpTaskInfo.unit%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%> </TD>
            <TD class="CellBGRnews"><%=CommonTools.formatDouble(dpTaskInfo.usl)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%> </TD>
            <TD class="CellBGRnews"><%=CommonTools.formatDouble(dpTaskInfo.planValue)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%> </TD>
            <TD class="CellBGRnews"><%=CommonTools.formatDouble(dpTaskInfo.lsl)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpTaskDetails.Actualvalue")%> </TD>
            <TD class="CellBGRnews"><%=CommonTools.formatDouble(dpTaskInfo.actualValue)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"> <%=languageChoose.getMessage("fi.jsp.dpTaskDetails.Deviation")%>(%) </TD>
            <%  String s = CommonTools.formatDouble(dpTaskInfo.deviationValue);
            	if (dpTaskInfo.deviationValue > 20) {%>
            	    <TD class="CellBGRnews"> <%=Color.setColor(Color.BADMETRIC, s)%></TD>
	           <%} else if (dpTaskInfo.deviationValue<0) {%>
	            	<TD class="CellBGRnews"><%=Color.setColor(Color.GOODMETRIC, s)%></TD>
	           <%} else {%>
	           		<TD class="CellBGRnews"><%=Color.setColor(Color.NOCOLOR, s)%></TD>
	           <%}            	
           %>    
        </TR>        
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Basic")%></TD>
            <TD class="CellBGRnews"><%=dpTaskInfo.dpCause%></TD>
        </TR>      
    </TBODY>
</TABLE>
<P>
<%if(right == 3 && !isArchive){%>
<INPUT type="hidden" name="vtID" value="<%=vtIDstr%>">
<INPUT type="hidden" name="dpTaskID" value="<%=dpTaskInfo.dptaskID%>">
<INPUT type="submit" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.DpTaskDetails.Update")%>" class="BUTTON">

<INPUT type="button" class="BUTTON" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.DpTaskDetails.Delete")%>" onclick="doDelete();">
<%}%>
<INPUT type="button" class="BUTTON" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.DpTaskDetails.Back")%>" onclick="jumpURL('woPerformanceView.jsp');">

</P>
</FORM>
<SCRIPT language="javascript">
	function doDelete()
  	{
  		if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.DpTaskDetails.Areyousuretodelete")%>")){   		
  			return;
  		}
  		frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_DPTASK%>#defectprevention";
  		frm.submit();	
	}
</SCRIPT> 
</BODY>
</HTML>