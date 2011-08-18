<%@page import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.tools.*,java.text.*,com.fms1.web.*,java.util.*,java.io.*" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<LINK href="stylesheet/fms.css" rel="stylesheet" type="text/css">
<TITLE>trainingDetails.jsp</TITLE>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<%
	int right = 1;
	int caller=Integer.parseInt(session.getAttribute("caller").toString());
	boolean fromProjectPlan=false;

	String title= "";
	
	if(caller==Constants.SCHEDULE_CALLER){//called from shedule
		right = Security.securiPage("Schedule",request,response);
		title= languageChoose.getMessage("fi.jsp.trainingDetails.Trainingplan");
	}
	else { //MANU :called from project plan
		right = Security.securiPage("Project plan",request,response);
		title=languageChoose.getMessage("fi.jsp.trainingDetails.ProjectPlan");
		fromProjectPlan=true;
	}
		
	Vector vt=(Vector)session.getAttribute("trainingVector");
	
	String vtIDstr=request.getParameter("vtID"); 
	int vtID=Integer.parseInt(vtIDstr);
	TrainingInfo trainInfo=(TrainingInfo)vt.get(vtID);
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
<p class="TITLE"><%=title%></p>
<FORM name="frm" method="Post" action="trainingUpdate.jsp?vtID=<%=vtID%>">
<TABLE class="Table" width="95%" cellspacing="1">
    <CAPTION class="TableCaption"><%=languageChoose.getMessage("fi.jsp.trainingDetails.Trainingplandetails")%></CAPTION>
    <TBODY>
    	<TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Topic")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(trainInfo.topic)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Participants")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(trainInfo.participant)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Duration")%><INPUT size="20" type="hidden" name="txtTrainingID" value="<%=trainInfo.trainingID%>"></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(trainInfo.duration)%></TD>
        </TR>
        <TR>
            <TD class="ColumnLabel" width="160"><%=languageChoose.getMessage("fi.jsp.trainingAdd.Waiver")%></TD>
            <TD class="CellBGRnews"><%=ConvertString.toHtml(trainInfo.waiver)%></TD>
        </TR>  
    </TBODY>
</TABLE>
<P><%if(right == 3 && !isArchive){%><INPUT type="submit" class="BUTTON" name="btnUpdate" value="<%=languageChoose.getMessage("fi.jsp.trainingDetails.Update")%>"> <INPUT type="button" class="BUTTON" name="btnDelete" value="<%=languageChoose.getMessage("fi.jsp.trainingDetails.Delete")%>" onclick="doDelete();"><%}%> <INPUT type="button" class="BUTTON" name="btnBack" value="<%=languageChoose.getMessage("fi.jsp.trainingDetails.Back")%>" onclick="doIt(<%=((caller==Constants.SCHEDULE_CALLER)?Constants.SCHE_TRAINING_PLAN_GET_LIST:Constants.GET_TRAINING_LIST)%>);"></P>
</FORM>
<%if(right == 3 && !isArchive){%>
<SCRIPT language="javascript">
  function doDelete()
  {  	
  		if(!window.confirm("<%=languageChoose.getMessage("fi.jsp.trainingDetails.Areyousuretodelete")%>")){  		
  			return;
  		}
  		
  		frm.action="Fms1Servlet?reqType=<%=Constants.DELETE_TRAINING%>";
  		frm.submit();  	
  }
 </SCRIPT> 
 <%}%>
</BODY>
</HTML>
