<%@page import="java.util.Vector,com.fms1.infoclass.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.common.*" errorPage="error.jsp" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache">
<TITLE>welcomePage.jsp</TITLE>
<LINK rel="stylesheet" href="stylesheet/fms.css" type="text/css">
<% 
	long wuID= CommonTools.parseInt((String)session.getAttribute("workUnitID"));

	String wuType = (String)session.getAttribute("workUnitType");
	int type =Integer.parseInt(wuType);
	String mnuDisable=request.getParameter("mnuDisable");
	if (mnuDisable==null)mnuDisable="";
	
	Vector metricList = (Vector)session.getAttribute("metricInfoAtWelcomePage");
    ReportMetricInfo reportMetricInfo=null;
    
	Vector allClosedTasks = (Vector)session.getAttribute("allClosedTasks");
	Vector nextWeekTasks = (Vector)session.getAttribute("nextWeekTasks");
	
	int allTask = 0;
	int allCloseTask = 0;
	
	if (allClosedTasks != null){
		allTask = allClosedTasks.size();
		allCloseTask = allClosedTasks.size();
	}

	if (nextWeekTasks != null)
		allTask = allTask + nextWeekTasks.size();

	boolean style=false;
	String className = null;
	String menu=null;
	
	switch(type) {
		case Constants.RIGHT_ADMIN:
			menu="loadAdminMenu";
			break;
		case Constants.RIGHT_PROJECT:
			menu=mnuDisable.equals("1")?"loadPrjMenuDis":"loadPrjMenu";
			break;
		case Constants.RIGHT_GROUP:
			menu=(wuID==Parameters.SQA_WU)?"loadSQAMenu":"loadGrpMenu";
			break;
		case Constants.RIGHT_ORGANIZATION:
			menu="loadOrgMenu";
			break;
	}
%>
<SCRIPT language="javascript" src="jscript/javaFns.js"></SCRIPT>
<SCRIPT language="javascript">
<%@ include file="javaFns.jsp"%>
</SCRIPT>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
%>
<BODY onLoad="<%=menu%>('Home','');" class="BD">
<%switch(type){
	case Constants.RIGHT_ADMIN:
		%><P class="WELCOME"><%=languageChoose.paramText(new String[]{"fi.jsp.welcomePage.Welcometo~PARAM1_NAME~",(String)session.getAttribute("workUnitName")})%></P><%break;
	case Constants.RIGHT_PROJECT:
		%><P class="TITLE"><%=languageChoose.paramText(new String[]{"fi.jsp.welcomePage.Project~PARAM1_NAME~",(String)session.getAttribute("workUnitName")})%></P>

<%
	int intNumStage = CommonTools.parseInt((String) session.getAttribute("numStage"));
	String effortPlan = "N/A";
	String effortETC = "N/A";
	String schedulePlan = "N/A";
	String scheduleETC = "N/A";
	if (metricList != null) {
		reportMetricInfo=(ReportMetricInfo)metricList.elementAt(0);
		effortPlan = CommonTools.formatDouble(reportMetricInfo.estimated);
		effortETC = CommonTools.formatDouble(reportMetricInfo.spent + reportMetricInfo.remain);
		reportMetricInfo=(ReportMetricInfo)metricList.elementAt(1);
		schedulePlan = CommonTools.dateFormat(reportMetricInfo.estimated);
		scheduleETC = CommonTools.dateFormat(reportMetricInfo.remain);
	}
	
%> 
<TABLE class="HDR">
    <TBODY>
        <TR>
            <TD></TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.welcomePage.Plan")%> </TD>
            <TD> ETC </TD>
        </TR>
        <TR>
            <TD width = "160">1. <%=languageChoose.getMessage("fi.jsp.welcomePage.Effortpd")%> </TD>
            <TD width = "100"><%=effortPlan%></TD>
            <TD width = "100"><%=effortETC%></TD>
        </TR>
        <TR>
            <TD width = "160">2. <%=languageChoose.getMessage("fi.jsp.welcomePage.Schedule")%> </TD>
            <TD width = "100"><%=schedulePlan%></TD>
            <TD width = "100"><%=scheduleETC%></TD>
        </TR>
        
<%  
	ProjectPointInfo projectPoint = null, ppo1 = null, ppo2 = null;
	String errorMessage="";
	String value=null;
	if (intNumStage == 0){
		errorMessage=languageChoose.getMessage("fi.jsp.welcomePage.ThereIsNoStageCompletedThereforeProjectPointIsNotAvailable");
		value="N/A";
	}
	else if (intNumStage == 1){
		projectPoint = (ProjectPointInfo) session.getAttribute("projectPoint1");
		if (projectPoint.ProjectEval.equalsIgnoreCase("N/A"))
			errorMessage= languageChoose.getMessage("fi.jsp.welcomePage.ThereIsOneStageCompletedButTheScoringIsNotDoneThereforeProjectPointIsNotAvailable");
		value = projectPoint.ProjectEval;
	}
	else if (intNumStage == 2){
		ppo1 = (ProjectPointInfo) session.getAttribute("projectPoint1");
		ppo2 = (ProjectPointInfo) session.getAttribute("projectPoint2");
		value = ppo1.ProjectEval;		
	}
	
	Vector cusMetricStage1 = (Vector)session.getAttribute("cusMetricStage1");
	Vector cusMetricStage2 = (Vector)session.getAttribute("cusMetricStage2");
%>
	<TR>
	    <TD width="160">3. <%=languageChoose.getMessage("fi.jsp.welcomePage.Evaluation")%> </TD>
	    <TD colspan="2"><%=value%></TD>
    </TR>
    </TBODY>
</TABLE>
<br>
<TABLE class="HDR">
    <TBODY>
        <TR>
            <TD width="160"><%=languageChoose.getMessage("fi.jsp.welcomePage.Taskcompleteness")%></TD>
<%if (allTask > 0){%>
			<TD width="100"><%=CommonTools.formatDouble((allCloseTask * 100)/allTask)%> (%)</TD>
<%} else {%>
			<TD width = "100"> N/A </TD>
<%}
%>
	</TR>
    </TBODY>
</TABLE>

<%=errorMessage%>
<%if ((intNumStage == 2)||(intNumStage == 1 && !projectPoint.ProjectEval.equalsIgnoreCase("N/A"))){%>
<TABLE class="Table" cellspacing="1" width="98%">
    <COL span="1" width="400">
    <COL span="1" width="400">
    <TR>
        <TD class="CellBGR3" valign="top">
     <TABLE class="Table" width="100%" cellspacing="1">
<%}else{%>   
	<TABLE class="Table" width="95%" cellspacing="1">
<%}%>
    <COL span="1" width="24">
    <TBODY>
        <TR class="ColumnLabel">
            <TD> <%=languageChoose.getMessage("fi.jsp.welcomePage.Pendingtasksfornextweek")%> </TD>
            <TD width="20%"> <%=languageChoose.getMessage("fi.jsp.welcomePage.Plannedenddate")%> </TD>
        </TR>
        <%
        java.util.Date now=new java.util.Date();
        if (nextWeekTasks != null){
        	int color;
	        for (int i = 0; i < nextWeekTasks.size(); i++) {
	        	TaskInfo taskInfo = (TaskInfo)nextWeekTasks.elementAt(i);
				style=!style;
				className=(style)?"CellBGRnews":"CellBGR3";
	      	  	color=(taskInfo.planDate.before(now))?Color.BADMETRIC:Color.NOCOLOR;
	      	  		
	        %>
	        <TR class="<%=className%>">
	            <TD><A href="<%=taskInfo.getLink()%>"><%=languageChoose.getMessage(taskInfo.type)%></A>: <%=ConvertString.toHtml( taskInfo.desc)%></TD>
	            <TD NOWRAP><%=Color.setColor(color,CommonTools.dateFormat(taskInfo.planDate))%></TD>
	        </TR> 
	    <%	}
	    } %>

    </TBODY>
</TABLE>
<%if (intNumStage==1 && !projectPoint.ProjectEval.equalsIgnoreCase("N/A")){%>
        
        <TD class="CellBGR3" valign="top">
<TABLE class="Table" cellspacing="1" align="right">
	<TBODY>
		<TR class="ColumnLabel">
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomePage.Projectpoint")%> </TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomePage.Milestone")%>  <%=projectPoint.stageName%></TD>
		</TR>
		<TR class="ColumnLabel">
			<TD colspan="2"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.STANDARDMETRICS")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD colspan="2"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Quality")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.CustomerSatisfaction")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.CusSatisfaction)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Leakage")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.Leakage)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.ProcessCompliance")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.ProjectNC)%></TD>
		</TR>
		
		<TR class="CellBGRNews">
			<TD colspan="2"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Cost")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.ProjectSize")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.ProjectSize)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD >&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.EffortEfficiency")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.EffortEfficiency)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.CorrectionCost")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.CorrectionCost)%></TD>
		</TR>
		
		<TR class="CellBGRNews">
			<TD colspan="2"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Delivery")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Timeliness")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.Timeliness)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.RequirementCompleteness")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.ReqCompleteness)%></TD>
		</TR>
		
		<TR  class="CellBGRNews">
			<TD colspan="2"></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.WorkOrder")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.WOPoint)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.AcceptanceNote")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.ANPoint)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.ProjectPlan")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.PPPoint)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.ProjectReports")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.PRPoint)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.OverdueNCsObs")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.OverdueNCsObsPoint)%></TD>
		</TR>
		<TR  class="ColumnLabel">
			<TD colspan="4"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.CUSTOMIZEDMETRICS")%> </B></TD>
		</TR>
		<%
		if(cusMetricStage1 != null){
			for(int i=0;i< cusMetricStage1.size();i++){
				CusMetricInfo cusInfo1 = new CusMetricInfo();
					cusInfo1 = (CusMetricInfo)cusMetricStage1.elementAt(i);				
		%>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=cusInfo1.name%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(cusInfo1.point)%></TD>
		</TR>
		<%		
			}
		}	
		%>
		
		<TR  class="ColumnLabel">
			<TD colspan="2"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.BONUSPENALTY")%> </B></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.PrestigePoint")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.PrestigePoint)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.SpecialPoint")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(projectPoint.CusPoint)%></TD>
		</TR>
		<TR class="TableLeft">
			<TD width="195"> <%=languageChoose.getMessage("fi.jsp.welcomePage.Total")%> </TD>
			<TD width="195" align="center"><%=CommonTools.formatDouble(projectPoint.ProjectPoint)%></TD>
		</TR>
	</TBODY>
</TABLE>
		</TD>
</TABLE>
<%}else if (intNumStage == 2){%>
        </TD>
        <TD class="CellBGR3" valign="top">
<TABLE class="Table" cellspacing="1" align="right">
	<TBODY>
		<TR class="ColumnLabel">
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomePage.Projectpoint1")%> </TD>
			<TD width="70"><%=ppo2.stageName%></TD>
			<TD width="70"><%=ppo1.stageName%></TD>
			<TD width="70"> <%=languageChoose.getMessage("fi.jsp.welcomePage.Evolution")%> </TD>
		</TR>
		<TR class="ColumnLabel">
			<TD colspan="4"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.STANDARDMETRICS")%> </B></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD colspan="4"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Quality")%> </B></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.CustomerSatisfaction1")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.CusSatisfaction)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.CusSatisfaction)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.CusSatisfaction - ppo2.CusSatisfaction)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Leakage1")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.Leakage)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.Leakage)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.Leakage - ppo2.Leakage)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.ProcessCompliance1")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.ProjectNC)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.ProjectNC)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.ProjectNC - ppo2.ProjectNC)%></TD>
		</TR>
		
		<TR  class="CellBGRNews">
			<TD colspan="4"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Cost")%> </B></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.ProjectSize1")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.ProjectSize)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.ProjectSize)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.ProjectSize - ppo2.ProjectSize)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.EffortEfficiency")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.EffortEfficiency)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.EffortEfficiency)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.EffortEfficiency - ppo2.EffortEfficiency)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.CorrectionCost1")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.CorrectionCost)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.CorrectionCost)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.CorrectionCost - ppo2.CorrectionCost)%></TD>
		</TR>
		
		<TR class="CellBGRNews">
			<TD colspan="4"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Delivery")%> </B></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Timeliness1")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.Timeliness)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.Timeliness)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.Timeliness - ppo2.Timeliness)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.RequirementCompl")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.ReqCompleteness)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.ReqCompleteness)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.ReqCompleteness - ppo2.ReqCompleteness)%></TD>
		</TR>
		
		<TR  class="CellBGRNews">
			<TD colspan="4"></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.WorkOrder1")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.WOPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.WOPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.WOPoint - ppo2.WOPoint)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.AcceptanceNote1")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.ANPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.ANPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.ANPoint - ppo2.ANPoint)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.ProjectPlan1")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.PPPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.PPPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.PPPoint - ppo2.PPPoint)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.ProjectReports1")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.PRPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.PRPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.PRPoint - ppo2.PRPoint)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.OverdueNCsObs")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.OverdueNCsObsPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.OverdueNCsObsPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.OverdueNCsObsPoint - ppo2.OverdueNCsObsPoint)%></TD>
		</TR>
		
		<TR  class="ColumnLabel">
			<TD colspan="4"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.CUSTOMIZEDMETRICS")%> </B></TD>
		</TR>
		<%
		if(cusMetricStage1 != null && cusMetricStage2 != null){
			for(int i=0;i< cusMetricStage1.size();i++){
				CusMetricInfo cusInfo1 = new CusMetricInfo();
					cusInfo1 = (CusMetricInfo)cusMetricStage1.elementAt(i);
				CusMetricInfo cusInfo2 = new CusMetricInfo();
					cusInfo2 = (CusMetricInfo)cusMetricStage2.elementAt(i);
				
		%>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=cusInfo1.name%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(cusInfo2.point)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(cusInfo1.point)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(cusInfo1.point - cusInfo2.point)%></TD>
		</TR>
		<%		
			}
		}	
		%>
		
		<TR  class="ColumnLabel">
			<TD colspan="4"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.BONUSPENALTY")%> </B></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.PrestigePoint1")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.PrestigePoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.PrestigePoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.PrestigePoint - ppo2.PrestigePoint)%></TD>
		</TR>
		<TR  class="CellBGRNews">
			<TD>&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.SpecialPoint")%> </TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo2.CusPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.CusPoint)%></TD>
			<TD align="center"><%=CommonTools.formatDouble(ppo1.CusPoint - ppo2.CusPoint)%></TD>
		</TR>
		<TR class="TableLeft">
			<TD width="160"> <%=languageChoose.getMessage("fi.jsp.welcomePage.Total1")%> </TD>
			<TD width="80" align="center"><%=CommonTools.formatDouble(ppo2.ProjectPoint)%></TD>
			<TD width="80" align="center"><%=CommonTools.formatDouble(ppo1.ProjectPoint)%></TD>
			<TD width="80" align="center"><%=CommonTools.formatDouble(ppo1.ProjectPoint - ppo2.ProjectPoint)%></TD>
		</TR>
	</TBODY>
</TABLE>
		</TD>
    </TR>
</TABLE>
<%}
	
	break;
case Constants.RIGHT_GROUP:
case Constants.RIGHT_ORGANIZATION:

	Vector groupPoint = (Vector) session.getAttribute("groupPoint");
	WorkUnitInfo wuInfo = (WorkUnitInfo) session.getAttribute("workUnit");
	String numDev = (String) session.getAttribute("numDev");
	String numProj = (String) session.getAttribute("numProj");
	String busyRate = (String) session.getAttribute("busyRate");
	
	String lstMonth1 = (String) session.getAttribute("lastMonth1");
	String lstMonth2 = (String) session.getAttribute("lastMonth2");

	String numOperationGroup = (String) session.getAttribute("numOperationGroup");
	String numBAGroup = (String) session.getAttribute("numBAGroup");

	GroupPointInfo gInfo1 = null, gInfo2 = null;
	GroupPointBAInfo gBaInfo1 = null, gBaInfo2 = null;
	double value1, value2;

	int curYear = CommonTools.parseInt((String) session.getAttribute("curYear"));
	int curMonth = CommonTools.parseInt((String) session.getAttribute("curMonth"));
	
%>
<P class="TITLE"><%=languageChoose.paramText(new String[]{"fi.jsp.welcomePage.~PARAM1_NAME~points", wuInfo.workUnitName})%></P>

<FORM name="frm" action="Fms1Servlet" method="POST">
<TABLE class="HDR">
    <TBODY>
        <TR>
			<TD class="NormalText"> <%=languageChoose.getMessage("fi.jsp.welcomePage.Month")%>&nbsp; <SELECT class="COMBO" name="selectMonth">
					<%	
						for (int i = 1; i <= 12; i++) {
					%>
					<OPTION value="<%=i%>"<%=(i == curMonth ? " selected" : "")%>><%=CommonTools.getMonth(i)%></OPTION>
					<%
						}
					%>
				</SELECT>
 &nbsp;<%=languageChoose.getMessage("fi.jsp.welcomePage.Year")%>&nbsp;<SELECT name="selectYear" class="COMBO">
					<%
						int nStartYear = 2000;
						java.text.SimpleDateFormat yearFormat = new java.text.SimpleDateFormat("yyyy");
						int nEndYear = CommonTools.parseInt(yearFormat.format(new java.util.Date()));
						for (int i = nEndYear; i >= nStartYear; i--) {
					%>
					<OPTION value="<%=i%>"<%=(i == curYear ? " selected" : "")%>><%=i%></OPTION>
					<%
						}
					%>
				</SELECT>
			</TD>
			<TD colspan="2" align="center"><INPUT type="button" value=" <%=languageChoose.getMessage("fi.jsp.welcomePage.Go")%> " class="BUTTON" onclick="onMonthChange();"></TD>
        </TR>
        <TR>
            <TD width = "300"></TD>
            <TD width = "50"></TD>
        </TR>
<%
 		if (
 			(wuInfo.workUnitName.equalsIgnoreCase(Parameters.PQA_ROLE)) ||
 			(wuInfo.workUnitName.equalsIgnoreCase(Parameters.SQA_ROLE)) ||
 			(wuInfo.groupType == WorkUnitInfo.GROUP_SUPPORT) 
 			)
 		{
 				// We care about busy rate only
%>
        <TR>
            <TD>1. <%=languageChoose.getMessage("fi.jsp.welcomePage.Numberofstaffs")%> </TD>
            <TD><%=numDev%></TD>

        </TR>
        <TR>
            <TD>2. <%=languageChoose.getMessage("fi.jsp.welcomePage.BusyRate")%> (%)</TD>
            <TD><%=busyRate%> </TD>
        </TR>
<%
 		}
 		else
 		{
%>
        <TR>
            <TD width = "300">1. <%=languageChoose.getMessage("fi.jsp.welcomePage.Numberofstaffs1")%></TD>
            <TD width = "50"><%=numDev%></TD>
        </TR>
        <TR>
            <TD width = "300">2. <%=languageChoose.getMessage("fi.jsp.welcomePage.Numberofrunningprojects")%> (<%=lstMonth1%>)</TD>
            <TD width = "50"><%=numProj%> </TD>
        </TR>
        <TR>
            <TD width = "300">3. <%=languageChoose.getMessage("fi.jsp.welcomePage.BusyRate")%> (%) </TD>
            <TD width = "50"><%=busyRate%> </TD>
        </TR>
<%
 		}
%>
    </TBODY>
</TABLE>

<br>
<INPUT type="hidden" name="workUnitID" value="<%=wuInfo.workUnitID%>">

<%
 		if (
 			(wuInfo.workUnitName.equalsIgnoreCase(Parameters.PQA_ROLE)) ||
 			(wuInfo.workUnitName.equalsIgnoreCase(Parameters.SQA_ROLE)) ||
 			(wuInfo.groupType == WorkUnitInfo.GROUP_SUPPORT) 
 			)
{
%>
<TABLE class="Table" cellspacing="1">
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="180"> <%=languageChoose.getMessage("fi.jsp.welcomePage.Groupinformation")%> </TD>
			<TD><%=lstMonth2%></TD>
			<TD><%=lstMonth1%></TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomePage.Evolution1")%> </TD><%
        		gBaInfo1=(GroupPointBAInfo)groupPoint.elementAt(1);
        		gBaInfo2=(GroupPointBAInfo)groupPoint.elementAt(0);
        		
        		value1 = gBaInfo1.timeLiness;
        		value2 = gBaInfo2.timeLiness;
			%>
		</TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Timeliness2")%> </TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Responsetime")%> </TD><%
        		value1 = gBaInfo1.responseTime;
        		value2 = gBaInfo2.responseTime;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Sum")%> </B></TD><%
        		value1 = gBaInfo1.timeLiness + gBaInfo1.responseTime;
        		value2 = gBaInfo2.timeLiness + gBaInfo2.responseTime;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>

        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.BudgetPerformance")%> </TD><%
        		value1 = gBaInfo1.budgetPerformance;
        		value2 = gBaInfo2.budgetPerformance;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.BusyRate")%></TD><%
        		value1 = gBaInfo1.busyRate;
        		value2 = gBaInfo2.busyRate;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.CustomerSatisfaction")%> </TD><%
        		value1 = gBaInfo1.customerSatisfaction;
        		value2 = gBaInfo2.customerSatisfaction;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.ValueAchievement")%> </TD><%
        		value1 = gBaInfo1.valueAchievement;
        		value2 = gBaInfo2.valueAchievement;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Sum1")%> </B></TD><%
        		value1 = gBaInfo1.budgetPerformance + gBaInfo1.busyRate + gBaInfo1.customerSatisfaction + gBaInfo1.valueAchievement;
        		value2 = gBaInfo2.budgetPerformance + gBaInfo2.busyRate + gBaInfo2.customerSatisfaction + gBaInfo2.valueAchievement;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>

        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.LanguageIndex")%> </TD><%
        		value1 = gBaInfo1.languageIndex;
        		value2 = gBaInfo2.languageIndex;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD width="180" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.TechnologyIndex")%> </TD><%
        		value1 = gBaInfo1.technologyIndex;
        		value2 = gBaInfo2.technologyIndex;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Sum2")%> </B></TD><%
        		value1 = gBaInfo1.languageIndex + gBaInfo1.technologyIndex;
        		value2 = gBaInfo2.languageIndex + gBaInfo2.technologyIndex;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>

        <TR>
            <TD class="CellBGRNews"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Total2")%> </B></TD><%
        		value1 = gBaInfo1.timeLiness + gBaInfo1.responseTime+ 
        		         gBaInfo1.budgetPerformance + gBaInfo1.busyRate +
        		         gBaInfo1.customerSatisfaction + gBaInfo1.valueAchievement +
        		         gBaInfo1.languageIndex + gBaInfo1.technologyIndex;
        		value2 = gBaInfo2.timeLiness + gBaInfo2.responseTime +
        		         gBaInfo2.budgetPerformance + gBaInfo2.busyRate +
        		         gBaInfo2.customerSatisfaction + gBaInfo2.valueAchievement +
        		         gBaInfo2.languageIndex + gBaInfo2.technologyIndex;
			%>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
			<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
        </TR>
        <TR class="TableLeft" >
            <TD> <%=languageChoose.getMessage("fi.jsp.welcomePage.Ranking")%> </TD><%
        		value1 = gBaInfo1.GroupRanking;
        		value2 = gBaInfo2.GroupRanking;
			%>
			<TD><%=CommonTools.formatDouble(value1) + "/" + numBAGroup%></TD>
			<TD><%=CommonTools.formatDouble(value2) + "/" + numBAGroup%></TD>
			<TD><%=CommonTools.formatDouble(value1 - value2)%></TD>
        </TR>
	</TBODY>
</TABLE>
<%
}
else
{
%>

<TABLE class="Table" cellspacing="1">
	<TBODY>
		<TR class="ColumnLabel">
			<TD width="150"> <%=languageChoose.getMessage("fi.jsp.welcomePage.Groupinformation1")%> </TD>
			<TD><%=lstMonth2%></TD>
			<TD><%=lstMonth1%></TD>
			<TD> <%=languageChoose.getMessage("fi.jsp.welcomePage.Evolution2")%> </TD>
		</TR>
		<TR>
			<TD class="CellBGRNews" colspan = "13"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Production")%> </B></TD>
		</TR>
		<TR>
			<TD width="120" class="CellBGR3"><A href="javascript:viewProjectPoint()">&nbsp;&nbsp;<U> <%=languageChoose.getMessage("fi.jsp.welcomePage.Project")%> </U></A></TD>
	        <%   
        		gInfo1=(GroupPointInfo)groupPoint.elementAt(1);
        		gInfo2=(GroupPointInfo)groupPoint.elementAt(0);
        		
        		value1 = gInfo1.Project;
        		value2 = gInfo2.Project;
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
		</TR>
		<TR>
			<TD width="120" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Proposal")%> </TD>
	        <%   
        		value1 = gInfo1.Proposal;
        		value2 = gInfo2.Proposal;
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
		</TR>
		<TR>
			<TD class="CellBGRNews" colspan = "13"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Business")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="120" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Revenue")%> </TD>
	        <%   
        		value1 = gInfo1.Revenue;
        		value2 = gInfo2.Revenue;
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="120" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.NetIncome")%> </TD>
	        <%   
        		value1 = gInfo1.NetIncome;
        		value2 = gInfo2.NetIncome;
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="120" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Receivable")%> </TD>
	        <%   
        		value1 = gInfo1.Receivable;
        		value2 = gInfo2.Receivable;
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
		</TR>
		<TR>
			<TD class="CellBGRNews" colspan = "13"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Staff")%> </B></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="120" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Language")%> </TD>
	        <%   
        		value1 = gInfo1.Language;
        		value2 = gInfo2.Language;
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="120" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Experience")%> </TD>
	        <%   
        		value1 = gInfo1.Experience;
        		value2 = gInfo2.Experience;
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="120" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Turnover")%> </TD>
	        <%   
        		value1 = gInfo1.Turnover;
        		value2 = gInfo2.Turnover;
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
		</TR>
		<TR class="CellBGRNews">
			<TD width="120" class="CellBGR3">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Technology")%> </TD>
	        <%   
        		value1 = gInfo1.Technology;
        		value2 = gInfo2.Technology;
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
		</TR>
<%if (type == Constants.RIGHT_GROUP){%>
		<TR class="CellBGRNews">
			<TD width="120" class="CellBGR3"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Total3")%> </B></TD>
	        <%   
        		value1 = gInfo1.Total;
        		value2 = gInfo2.Total;
			%>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value1)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2)%></TD>
				<TD class="CellBGRNews"><%=CommonTools.formatDouble(value2 - value1)%></TD>
		</TR>
		<TR class="TableLeft">
			<TD width="120">&nbsp;&nbsp; <%=languageChoose.getMessage("fi.jsp.welcomePage.Ranking1")%> </TD>
	        <%
        		value1 = gInfo1.GroupRanking;
        		value2 = gInfo2.GroupRanking;
			%>
				<TD><%=CommonTools.formatDouble(value1) + "/" + numOperationGroup%></TD>
				<TD><%=CommonTools.formatDouble(value2) + "/" + numOperationGroup%></TD>
				<TD><%=CommonTools.formatDouble(value1 - value2)%></TD>
		</TR>
<%}else	{%>
		<TR class="TableLeft">
			<TD width="120"><B> <%=languageChoose.getMessage("fi.jsp.welcomePage.Total4")%> </B></TD>
	        <%   
        		value1 = gInfo1.Total;
        		value2 = gInfo2.Total;
			%>
				<TD><%=CommonTools.formatDouble(value1)%></TD>
				<TD><%=CommonTools.formatDouble(value2)%></TD>
				<TD><%=CommonTools.formatDouble(value2 - value1)%></TD>
		</TR>
<%
  }
}
%>
	</TBODY>
</TABLE>
</FORM>

<SCRIPT language="javascript">
//objs to hide when submenu is displayed
var objToHide=new Array(frm.selectMonth,frm.selectYear);

function onMonthChange() {
	frm.action = "Fms1Servlet?reqType=<%=Constants.WORKUNIT_HOME2%>";
	frm.submit();
}
function viewProjectPoint() {
	frm.action = "Fms1Servlet?reqType=<%=Constants.VIEW_PROJECT_POINT+"&selectYear="+curYear+"&selectMonth="+curMonth%> ";
	frm.submit();
}

</SCRIPT>

<%
	break;
}
%>
<P><BR></P>
</BODY>
</HTML>
