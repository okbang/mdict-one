<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*, java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>otherActivityDetails.jsp</TITLE>
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
	int caller=Integer.parseInt(session.getAttribute("caller").toString());
	int right = 1;
	String title;
	if(caller==Constants.SCHEDULE_CALLER){//called from shedule
		right = Security.securiPage("Schedule",request,response);
		title=languageChoose.getMessage("fi.jsp.otherActivityDetails.ScheduleOtherqualityactivities");
	}
	else { //called from project plan
		right = Security.securiPage("Project plan",request,response);
		title=languageChoose.getMessage("fi.jsp.otherActivityDetails.ProjectplanOtherqualityactivities");
	}
	Vector vtOtherAct=(Vector)session.getAttribute("otherActVector");
	String vtIDstr=request.getParameter("vtID");
	int vtID=Integer.parseInt(vtIDstr);
	
	OtherActInfo oaInfo=(OtherActInfo)vtOtherAct.get(vtID);	
	String activity=(oaInfo.activity!=null)? oaInfo.activity:"N/A";
    String note=(oaInfo.note!=null)?oaInfo.note:"N/A";
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"><%=title%></p>
<FORM action="otherActivityUpdate.jsp?vtID=<%=vtID%>#otheract" method="post" name="frm">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Qualityactivitydetails")%></CAPTION>
    <TBODY>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Activity")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(activity)%><INPUT size="20" type="hidden" name="txtOtherActID" value="<%=oaInfo.otherActID%>"></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Type")%> </TD>
            <TD class="CellBGRnews"><%=languageChoose.getMessage(QCActivityInfo.getActivity(oaInfo.qcActivity).name)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Plannedstartdate")%></TD>
            <TD class="CellBGRnews"><%=CommonTools.dateFormat(oaInfo.pStartD)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Plannedenddate")%></TD>
            <TD class="CellBGRnews"><%=CommonTools.dateFormat(oaInfo.pEndD)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Actualenddate")%></TD>
            <TD class="CellBGRnews"><%=CommonTools.dateFormat(oaInfo.aEndD)%></TD> 
        </TR>
       <%if(oaInfo.qcActivity==QCActivityInfo.FINAL_INSPECTION){%>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.otherActivityDetails.SuccessfulTCCoverage")%> </TD>
            <TD class="CellBGRnews"><%=CommonTools.formatDouble(oaInfo.metric)%></TD> 
        </TR>
       <%}%>
        <TR>
            <TD class="ColumnLabel"> <%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Status")%> </TD>
            <TD class="CellBGRnews"><%=languageChoose.getMessage(TaskInfo.statusSQA_Updated[oaInfo.status])%></TD> 
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Conductor")%></TD>
            <TD class="CellBGRnews"><%=CommonTools.formatString(oaInfo.conductorName)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel"><%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Note")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(note)%></TD>
        </TR>
    </TBODY>
</TABLE>
<P>
<%if(right == 3 && !isArchive){%>
	<INPUT type="submit" value="<%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Update")%>" class="BUTTON" >
<%if (oaInfo.qcActivity!=QCActivityInfo.QUALITY_GATE_INSPECTION && oaInfo.risk_type != 1 && oaInfo.risk_type != 2){%>	
	<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Delete")%>" onclick="deleteMe()">
<%}
}%>
<INPUT type="button" class="BUTTON" value="<%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Back")%>" onclick="doIt(<%=(caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_OTHER_QUALITY_GET_LIST:Constants.GET_QUALITY_OBJECTIVE_LIST%>);">
</P>
</FORM>
<SCRIPT language="javascript">
  function deleteMe()  {
  	if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.otherActivityDetails.Areyousuretodelete")%>")){
  		return;
  	} 		
  	frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_OTHER_ACTIVITY%>#otheract";
  	frm.submit();
  }
 </SCRIPT> 
</BODY>

</HTML>
