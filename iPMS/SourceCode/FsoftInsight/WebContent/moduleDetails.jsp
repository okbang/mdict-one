<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>moduleDetails.jsp</TITLE>
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
	int right;//see below
	int caller = Integer.parseInt(session.getAttribute("caller").toString());
	String vtIDstr = request.getParameter("vtID");
	String title;
	String backButton;
	int reqType = 0;
	String sreqType = request.getParameter("reqType");
	if (sreqType != null)
		reqType = Integer.parseInt(sreqType);
	if(caller == Constants.SCHEDULE_CALLER && reqType != Constants.SCHE_REVIEW_TEST_VIEW){//called from shedule
		right = Security.securiPage("Schedule",request,response);
		title = languageChoose.getMessage("fi.jsp.moduleDetails.ScheduleReviewandtestactivities"); 
		backButton = "doIt("+Constants.SCHE_REVIEW_TEST_GET_LIST+")";
	}
	else { //called from project plan
		right = Security.securiPage("Project plan",request,response);
		title = languageChoose.getMessage("fi.jsp.moduleDetails.ProjectplanReviewandtestactivities");
		if (reqType == Constants.SCHE_REVIEW_TEST_VIEW )//called from moduleView.jsp
			backButton = "window.history.back()";
		else
			backButton = "doIt("+Constants.GET_QUALITY_OBJECTIVE_LIST+")";
	}
	Vector vt = (Vector)session.getAttribute("moduleVector");
	
	int vtID = Integer.parseInt(vtIDstr);
	
	ModuleInfo modun = (ModuleInfo)vt.get(vtID);
	if ("".equals(modun.approver)) modun.approver = "N/A";
	if ("".equals(modun.reviewer)) modun.reviewer = "N/A";
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"><%=title%></p>
<BR>
<FORM method="post" action="moduleUpdate.jsp?vtID=<%=vtID%>" name="frm">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.moduleDetails.Reviewandtestactivitydetails")%></CAPTION>
    <TBODY>
    	<TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.moduleDetails.Product")%></TD>
            <TD class="CellBGRnews" colspan=3><%=ConvertString.toHtml(modun.name)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.moduleDetails.Workproduct")%></TD>
            <TD class="CellBGRnews"colspan=3><%=languageChoose.getMessage(ConvertString.toHtml(modun.wpName))%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.moduleDetails.Plannedactualreviewdate")%> </TD>
            <TD class="CellBGRnews" width="80px"><%=CommonTools.dateFormat(modun.plannedReviewDate)%></TD>
        	 <TD class="CellBGRnews" colspan=2><%=CommonTools.dateFormat(modun.actualReviewDate)%></TD>
        </TR>
        <%if(modun.isNormal){%>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.moduleDetails.Plannedactualtestenddate")%> </TD>
            <TD class="CellBGRnews" width="80px"><%=CommonTools.dateFormat(modun.plannedTestEndDate)%></TD>
            <TD class="CellBGRnews" colspan=2><%=CommonTools.dateFormat(modun.actualTestEndDate)%></TD>
        </TR>
        <%}%>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.moduleDetails.Plannedreplannedactualreleased")%> </TD>
            <TD class="CellBGRnews" width="80px"><%=CommonTools.dateFormat(modun.plannedReleaseDate)%></TD>
            <TD class="CellBGRnews" width="80px"><%=CommonTools.dateFormat(modun.rePlannedReleaseDate)%></TD>
            <TD class="CellBGRnews" ><%=CommonTools.dateFormat(modun.actualReleaseDate)%></TD>
        </TR>
        <%if(modun.isDel){%>
        <TR>
            <TD class="ColumnLabel" width="240"> <%=languageChoose.getMessage("fi.jsp.moduleDetails.Deliverablestatus")%> </TD>
            <TD class="CellBGRnews"  colspan=3><%=languageChoose.getMessage(modun.getStatusName())%>
			</TD>
        </TR>
        <%}%>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.moduleDetails.Stage")%></TD>
            <TD class="CellBGRnews" colspan=3><%=ConvertString.toHtml(modun.stage)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.moduleDetails.ReviewLeader")%></TD>
            <TD class="CellBGRnews" colspan=3><%=ConvertString.toHtml(modun.conductor) %></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.moduleDetails.Releasedontime")%></TD>
            <TD class="CellBGRnews" colspan=3><%=languageChoose.getMessage(modun.isRelease)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.moduleDetails.Reviewedontime")%></TD>
            <TD class="CellBGRnews" colspan=3><%=languageChoose.getMessage(modun.isReview)%></TD>
        </TR>
        <%if(modun.isNormal){%>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.moduleDetails.Testedontime")%></TD>
            <TD class="CellBGRnews" colspan=3><%=languageChoose.getMessage(modun.isTest)%></TD>
        </TR>
        <%}%>
        <TR>
            <TD class="ColumnLabel" width="240"><%=languageChoose.getMessage("fi.jsp.moduleDetails.Scheduledeviation")%>(%)</TD>
            <TD class="CellBGRnews" colspan=3><%=CommonTools.formatDouble(modun.deviation)%></TD>
        </TR>
    </TBODY> 
</TABLE>
<P><%if(right == 3 && !isArchive){%>
<INPUT type="submit" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.moduleDetails.Update")%>" class="BUTTON">
<%}%>
<INPUT type="button" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.moduleDetails.Back")%>" class="BUTTON" onclick="<%=backButton%>">
</P>
</FORM>
</BODY>
</HTML>
