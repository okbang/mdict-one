<%@page import="com.fms1.infoclass.*,com.fms1.common.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*,com.fms1.tools.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">

<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>scheTrainingPlan.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js">
</SCRIPT>
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
	int right = Security.securiPage("Schedule",request,response); 
	Vector vtTraining=(Vector)session.getAttribute("trainingVector");
%>
<BODY onload="loadPrjMenu()" class="BD">
<P class="TITLE"> <%=languageChoose.getMessage("fi.jsp.scheTrainingPlan.ScheduleTrainingplan")%> </P>
<FORM method="post" action="Fms1Servlet?reqType=<%=Constants.UPDATE_SCHEDULE_HEADER%>" name="frm">
<TABLE class="Table" cellspacing="1" width="95%">
    <TBODY>
        <TR class="ColumnLabel">
            <TD width="24" align="center" nowrap>#</TD>
            <TD width="35%"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Topic")%></TD>
            <TD width="35%"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Participants")%></TD>                       
            <TD><%=languageChoose.getMessage("fi.jsp.trainingAdd.Duration")%></TD>
            <TD><%=languageChoose.getMessage("fi.jsp.trainingAdd.Waiver")%></TD>
        </TR>
        <%  	
   		  	boolean bl=true;
        	String rowStyle;
           	String topic;
        	String startD;
       		String endD;
       		String actEndD;
        	for(int i=0;i<vtTraining.size();i++)
        	{
        		TrainingInfo trainInfo=(TrainingInfo)vtTraining.get(i);
        		rowStyle=(bl)?"CellBGRnews":"CellBGR3";
  				bl=!bl;
  				topic=trainInfo.topic;  				
       			startD=(trainInfo.startD==null)?"N/A":CommonTools.dateFormat(trainInfo.startD);
       			endD=(trainInfo.endD==null)?"N/A":CommonTools.dateFormat(trainInfo.endD);
       			actEndD=(trainInfo.actualEndD==null)?"N/A":CommonTools.dateFormat(trainInfo.actualEndD);

        %>
        <TR class=<%=rowStyle%>>
            <TD align="center"><%=i+1%></TD>
            <TD><A href="trainingDetails.jsp?vtID=<%=i%>"><%=ConvertString.toHtml(trainInfo.topic)%></A></TD>
            <TD><%=ConvertString.toHtml(trainInfo.participant)%></TD>
            <TD><%=ConvertString.toHtml(trainInfo.duration)%></TD>                       
            <TD><%=ConvertString.toHtml(trainInfo.waiver)%></TD>
        </TR>
        <%}%>        
    </TBODY>
</TABLE>
<P><%if(right == 3 && !isArchive){%><INPUT type="submit" class="BUTTON" name="btnAdd" value="<%=languageChoose.getMessage("fi.jsp.scheTrainingPlan.Addnew")%>" onclick="addTraining();"><%}%></P>
</form>
<SCRIPT language="javascript">
function addTraining(){
	frm.action="trainingAdd.jsp";
	frm.submit();
}
</script>
</BODY>
</HTML>
