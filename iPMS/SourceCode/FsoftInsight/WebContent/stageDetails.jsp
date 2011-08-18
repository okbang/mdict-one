<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>stageDetails.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	// landd add start
	String callerSession = session.getAttribute("caller").toString();
	int caller = 0;
	if (null != callerSession){
		caller=Integer.parseInt(callerSession);
	}
	// landd add end
	
	int right = 1;
	String title;
	if (caller==Constants.SCHEDULE_CALLER){ //called from shedule
		right = Security.securiPage("Schedule",request,response);
		title=languageChoose.getMessage("fi.jsp.stageDetails.Schedule");
	}
	else{ //called from work order
		right = Security.securiPage("Work Order",request,response);
		title=languageChoose.getMessage("fi.jsp.stageDetails.WorkOrder") ;
	}
	
	Vector vt=(Vector)session.getAttribute("stageVector");
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	String source = request.getParameter("source");
	if(source == null) {
		//System.out.println(languageChoose.getMessage("fi.jsp.stageDetails.SourceIsNull"));
		source = "0";		
	}
	
	StageInfo info=(StageInfo)vt.get(vtID);	
	
	String milestone="N/A";
	String description="N/A";	
	String bEndD=CommonTools.dateFormat(info.bEndD);
	String aEndD=CommonTools.dateFormat(info.aEndD);
	String pEndD=CommonTools.dateFormat(info.pEndD);
	if(info.milestone!=null){
		milestone=info.milestone;  							
	}
  	if(info.description!=null){
  		description=info.description;
  	}
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
<P class="TITLE"><%=title%></p>
<FORM method="post" action="stageUpdate.jsp?vtID=<%=vtID%><%if(source.equals("1")){%>&source=1<%}%>" name="frm">

<TABLE class="Table" width="560" cellspacing="1">
    <CAPTION class="TableCaption"> <%=languageChoose.getMessage("fi.jsp.stageDetails.Stagedetails")%> </CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageDetails.Stage")%></TD>
            <TD class="CellBGRnews"><INPUT size="9" type="hidden" name="txtMilestoneID" value="<%=info.milestoneID%>"><%=ConvertString.toHtml(info.stage)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageDetails.Plannedenddate")%></TD>
            <TD class="CellBGRnews"><%=bEndD%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageDetails.Re-plannedenddate")%></TD>
            <TD class="CellBGRnews"><%=pEndD%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageDetails.Actualenddate")%></TD>
            <TD class="CellBGRnews"><%=aEndD%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.stageDetails.Numberofinterations")%> </TD>
            <TD class="CellBGRnews"><%=info.iterationCnt%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.stageDetails.Description")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(description)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.stageDetails.Milestone")%> </TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(milestone)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.stageDetails.Isontime")%> </TD>
            <TD class="CellBGRnews"><%=languageChoose.getMessage(info.isOntime)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.stageDetails.Scheduledeviation")%> </TD>
            <TD class="CellBGRnews"><%=info.deviation%></TD>
        </TR>
    </TBODY> 
</TABLE>
<BR>
<P><%if(right == 3 && !isArchive){%>
<INPUT type="submit" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.stageDetails.Update")%>" class="BUTTON">
<INPUT type="button" class="BUTTON" name="txtDelete" value="<%=languageChoose.getMessage("fi.jsp.stageDetails.Delete")%>" onclick="doDelete();"><%}%>  
<INPUT type="button" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.stageDetails.Back")%>" class="BUTTON" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_STAGE_GET_LIST:Constants.WO_DELIVERABLE_GET_LIST%>);">
<%
  	  if(source.equals("1")){
  	  %>
  	    <INPUT  type="hidden" name="source" value="1">	  
  	  <%}%>
</FORM>
<SCRIPT language="javascript">
  function doDelete(){
  	  	
  	  if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.stageDetails.Areyousuretodelete")%>")){		
  			return;
  	  }		
  	  frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_STAGE%>&vtID=<%=vtID%>";		
  	  frm.submit();	    	    	
  }
 </SCRIPT> 
</BODY>
</HTML>
