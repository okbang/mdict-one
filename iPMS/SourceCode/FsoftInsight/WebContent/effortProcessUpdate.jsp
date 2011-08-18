<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,com.fms1.infoclass.StageInfo,com.fms1.web.*,java.util.*,java.io.*,java.text.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<TITLE>effortProcessUpdate.jsp</TITLE>
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
<BODY onload="loadPrjMenu()" class="BD">
<%
	Vector vtProcess=(Vector)session.getAttribute("processEffortVector");
%>
<p class="TITLE"><%=languageChoose.getMessage("fi.jsp.effortProcessUpdate.EffortProcesseffort")%></p>

<FORM name="frm" action="Fms1Servlet" method="POST">
<INPUT type="hidden" name="reqType" value="<%=Constants.BATCH_UPDATE_PROCESS_EFFORT%>">
	
<TABLE class="Table" cellspacing="1">
	<CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.effortProcessUpdate.Updateprocesseffort")%></CAPTION>
	<TBODY>
		<TR class="ColumnLabel">
            <TD class="ColumnLabel" width="24" align="center">#</TD>
			<TD width="120" class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effortProcessUpdate.Process")%></TD>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effortProcessUpdate.Norms")%></TD>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effortProcessUpdate.Planned")%></TD>
			<TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.effortProcessUpdate.Replanned")%></TD>
		</TR>
        <%         	
         	boolean bl=false;
        	String rowStyle="";
       		
        	for(int i=0;i<vtProcess.size();i++)
        	{
        		ProcessEffortInfo processInfo=(ProcessEffortInfo)vtProcess.get(i);
        		
        		rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				
  				String normValue = null;
  				String estimatedValue = null;
  				String reEstimatedValue = null;

  				if (!Double.isNaN(processInfo.norm))
  					normValue = CommonTools.formatDouble(processInfo.norm);
  				else
  					normValue = "";
  				
  				if (!Double.isNaN(processInfo.estimated))
  					estimatedValue = CommonTools.formatDouble(processInfo.estimated);
  				else
  					estimatedValue = "";

  				if (!Double.isNaN(processInfo.reEstimated))
  					reEstimatedValue = CommonTools.formatDouble(processInfo.reEstimated);
  				else
  					reEstimatedValue = "";
        		      		
        %>
			<INPUT type="hidden" name="processEffortId" value="<%=processInfo.proEffID%>">
	        <TR class=<%=rowStyle%>>
	            <TD align="center"><%=i+1%></TD>
	            <TD><%=languageChoose.getMessage(processInfo.process)%></TD>
	            <TD><%=normValue%></TD>
	            <TD><INPUT size="15" type="text" maxlength="11" name="estimated" class="numberTextBox" value="<%=estimatedValue%>"></TD>
	            <TD><INPUT size="15" type="text" maxlength="11" name="reestimated" class="numberTextBox" value="<%=reEstimatedValue%>" ></TD>
	        </TR>
        <%}%>
	</TBODY>
</TABLE>

<br>
<%if(!isArchive){%>
<INPUT type="button" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.effortProcessUpdate.Update")%>" class="BUTTON" onclick="validateNum()">
<%}%>	
<INPUT type="button" name="cancel" value="<%=languageChoose.getMessage("fi.jsp.effortProcessUpdate.Cancel")%>" class="BUTTON" onclick="doIt(<%=Constants.EFF_STAGE_GET_LIST%>)">
</FORM>

<SCRIPT language="javascript">
function validateNum()
{
	for (var i=0;i<frm.estimated.length;i++){
		if (!positiveFld(frm.estimated[i],"<%=languageChoose.getMessage("fi.jsp.effortProcessUpdate.Thisvalue")%>"))
			return;
			
		if (!positiveFld(frm.reestimated[i],"<%=languageChoose.getMessage("fi.jsp.effortProcessUpdate.Thisvalue")%>"))
			return;
		if (frm.estimated[i].value=="" && frm.reestimated[i].value!=""){
			alert("<%=languageChoose.getMessage("fi.jsp.effortProcessUpdate.PleasefillinEstimatedvalue")%>");
			frm.estimated[i].focus();
			return;
		}
	}
	frm.submit();
  	
}
</SCRIPT>
</BODY>
</HTML>
