<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
<TITLE>scheStage.jsp</TITLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%	
	int right = Security.securiPage("Schedule",request,response); 
	Vector vtStage=(Vector)session.getAttribute("stageVector");
	// Not used
	//Vector perfVector = (Vector) session.getAttribute("WOPerformanceVector");
	MetricInfo durationMetric = (MetricInfo) session.getAttribute("durationMetric");
	String aEndD="";
	String pEndD="N/A";
	Vector iterationList = (Vector) session.getAttribute("PLIterationList");
	Vector milestoneList = (Vector)session.getAttribute("plMilestoneList");
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
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.scheStage.ScheduleStageandIteration")%> </P>
<FORM method="post" action="Fms1Servlet?reqType=<%=Constants.UPDATE_SCHEDULE_HEADER%>" name="frm">
<TABLE class="Table" cellspacing="1" width="95%">
<CAPTION class = "TableCaption"><A name="stage"> <%=languageChoose.getMessage("fi.jsp.scheStage.Stage")%> </A></CAPTION>
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center">#</TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.scheStage.Stage1")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.scheStage.Plannedenddate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.scheStage.Replannedenddate")%> </TD>                       
            <TD> <%=languageChoose.getMessage("fi.jsp.scheStage.Actualenddate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.scheStage.Isontime")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.scheStage.Scheduledeviation")%> <BR>(%)</TD>
        </TR>
        <%   
           	boolean bl=false;
        	String rowStyle="";
    		double dbPlanTotalDuration = 0;

        	String bEndD;
        	for(int i=0;i<vtStage.size();i++)
        	{
        		StageInfo stageInfo=(StageInfo)vtStage.get(i);
        		rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;		
        		if (!stageInfo.pDuration.equalsIgnoreCase("N/A"))
	        		dbPlanTotalDuration = dbPlanTotalDuration + Double.parseDouble(stageInfo.pDuration);
        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A href="stageDetails.jsp?vtID=<%=i%>"><%=stageInfo.stage%></A></TD>
            <TD><%=CommonTools.dateFormat(stageInfo.bEndD)%></TD>
            <TD><%=CommonTools.dateFormat(stageInfo.pEndD)%></TD>                       
            <TD><%=CommonTools.dateFormat(stageInfo.aEndD)%></TD>   
            <TD><%= (stageInfo.isOntime == "N/A")? "N/A": languageChoose.getMessage(stageInfo.isOntime)%></TD>                       
            <TD><%=stageInfo.deviation%></TD>      
        </TR>
        <%}%>        
    </TBODY>
</TABLE>
<P><%if(right == 3 && !isArchive){%><INPUT type="button" class="BUTTON" name="btnAdd" value="<%=languageChoose.getMessage("fi.jsp.scheStage.Addnew")%>" onclick="addStage();"><%}%></P>
</FORM>
<%
	double planVal = (Double.isNaN(durationMetric.rePlannedValue))
					 ? durationMetric.plannedValue
					 : durationMetric.rePlannedValue;
	if (dbPlanTotalDuration != 0) {
		dbPlanTotalDuration = dbPlanTotalDuration + (vtStage.size() - 1);
		if (planVal != dbPlanTotalDuration)	{
		%><p class="ERROR"> <%=languageChoose.paramText(new String[]{"fi.jsp.scheStage.NoteInconsistencybetweentotalp~PARAM1_NUM~andplandurationofproject~PARAM2_NUM2~", CommonTools.formatDouble(dbPlanTotalDuration), CommonTools.formatDouble(planVal)})%><p>
		<%
		}
	}
%>
<br>
<TABLE cellspacing="1" class="Table" width="95%">
<CAPTION class="TableCaption"><A name="iteration"> <%=languageChoose.getMessage("fi.jsp.scheStage.Iteration")%> </A></CAPTION>
<TR class="ColumnLabel">
	<TD width="24" align="center">#</TD>
	<TD><%=languageChoose.getMessage("fi.jsp.scheStage.Stage2")%></TD>
	<TD><%=languageChoose.getMessage("fi.jsp.scheStage.Iteration1")%></TD>
	<TD><%=languageChoose.getMessage("fi.jsp.scheStage.Plannedenddate")%></TD>
	<TD><%=languageChoose.getMessage("fi.jsp.scheStage.Replannedenddate")%></TD>                       
	<TD><%=languageChoose.getMessage("fi.jsp.scheStage.Actualenddate")%></TD>
	<TD><%=languageChoose.getMessage("fi.jsp.scheStage.Isontime1")%></TD>
	<TD><%=languageChoose.getMessage("fi.jsp.scheStage.Deviationdays")%></TD>
</TR>
<%
String className;
for(int i = 0; i < iterationList.size(); i++){
	className=(i%2==0)?"CellBGRnews":"CellBGR3";
 	IterationInfo iterationInfo = (IterationInfo) iterationList.elementAt(i);
%>
<TR class="<%=className%>">
<TD width = "24" align = "center"><%=i+1%></TD>
<TD><p>	<%if(right == 3 && !isArchive){%>
		<a href="Fms1Servlet?reqType=<%=Constants.PL_ITERATION_UPDATE_PREPARE %>&plIteration_ID=<%=iterationInfo.iterationID%>">
		<%}
for (int j = 0; j < milestoneList.size(); j++) {
	MilestoneInfo milestoneInfo = (MilestoneInfo)milestoneList.elementAt(j);
	long milestoneID = milestoneInfo.getMilestoneId();
	if (iterationInfo.milestoneID == milestoneID) {
		%><%=milestoneInfo.getName()%>
<%	}
}
if(right == 3 && !isArchive){%></a><%}%>
</TD>
<TD><%=iterationInfo.iteration%></TD>
<%
String intebEndD=(iterationInfo.planEndDate!=null)?CommonTools.dateFormat(iterationInfo.planEndDate):"";
String intepEndD=(iterationInfo.replanEndDate!=null)?CommonTools.dateFormat(iterationInfo.replanEndDate):"";
String inteaEndD=(iterationInfo.actualEndDate!=null)?CommonTools.dateFormat(iterationInfo.actualEndDate):"";
%>
<TD><%=intebEndD%></TD>
<TD><%=intepEndD%></TD>
<TD><%=inteaEndD%></TD>
<TD><%=(iterationInfo.isOntime == "N/A") ? "N/A" : languageChoose.getMessage(iterationInfo.isOntime)%></TD>
<TD><%=iterationInfo.deviation%></TD>
</TR>
<%}%>
</table>
<%if(right == 3 && !isArchive){%>
<form name="frm_plIterationAddPrep" action="Fms1Servlet" method="get"><BR>
<input type="hidden" name="reqType" value="<%=Constants.PL_ITERATION_ADD_PREPARE%>">
<P><input type="submit" name="add" value="<%=languageChoose.getMessage("fi.jsp.scheStage.Addnew")%>" class="BUTTON" ></p>
</form>
<%}%>
<SCRIPT language="javascript">
function addStage(){
	frm.action="stageAdd.jsp";
	frm.submit();
}
</SCRIPT>
</BODY>
</HTML>