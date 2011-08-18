<%@ page contentType="application/vnd.ms-excel; charset=UTF-8"%>
<%@page import="com.fms1.common.*,com.fms1.web.*,com.fms1.tools.*,com.fms1.infoclass.*,com.fms1.infoclass.group.*,java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<TITLE>paRiskExport.jsp</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<STYLE type="text/css">
.header {
    FONT-WEIGHT: bold;
    BACKGROUND-COLOR: #C0C0C0;
}
.footer {
    FONT-WEIGHT: bold;
}
.Title {
	font-weight: bold;
	font-size: 14pt;
	margin-left: 0px;
	margin-top: 20px
}
</STYLE>
</HEAD>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");
	response.addHeader("Content-Disposition", "attachment;filename=paRisk.xls");
%>
<BODY>

<P class="Title"> <%=languageChoose.getMessage("fi.jsp.paRiskExport.ProcessAssetsRisksEncountered")%> <P>

<%
	Vector occuredRisk = (Vector) session.getAttribute("vtOccuredRisk");
	Vector vtProcess = (Vector)session.getAttribute("vtProcess");
	
	int j = 0;

	char cr = 13;
	char lf = 10;
%>

<TABLE border="1" cellspacing="1" cellpadding="0" width="100%">
<CAPTION align="left" class="TableCaption"></CAPTION>
    <TBODY>
        <TR class="header">
            <TD width = "24" align = "center"># </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.ProjectCode")%> </TD>
            <TD> Description </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Probability")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.EstimatedImpact")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Exposure")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Mitigationplan")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Contingencyplan")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Triggers")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Assignee")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Assesmentdate")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Status")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.SourceProcess")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Projecttype")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Note")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Year")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Effortlost")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Scheduledelay")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Reason")%> </TD>
            <TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Group")%> </TD>
        </TR>
	<%
	j = occuredRisk.size();
	for(int i = 0 ;i < j; i++)
	{
	 	FullRiskInfo fullInfo = (FullRiskInfo) occuredRisk.elementAt(i);
	 	RiskInfo info = fullInfo.getRiskInfo();
	%>
	
	<TR>
	<TD width = "24" align = "center"><%=i+1%></TD>
	<TD><%=info.projectCode%></TD>
	<TD><%=((info.condition == null)?"N/A":ConvertString.toHtmlForExcel(info.condition))%></TD>
	<TD><%=info.probability%></TD>
	<%
		String estimated_impact = "";
		
		String imp[] = new String[6];
		String unt[] = new String[6];
		double est[] = new double[6];
		
		imp[3] = info.plannedImpact.substring(0, 1);
		unt[3] = info.plannedImpact.substring(1, 2);
		est[3] = Double.parseDouble(info.plannedImpact.substring(2, 16));
	
		if (imp[3].equals("a")) imp[3] = "N/A";
		else if (imp[3].equals("b")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Schedule");
		else if (imp[3].equals("c")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Effort");
		else if (imp[3].equals("d")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Finance");
		else if (imp[3].equals("e")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Team");
		else if (imp[3].equals("f")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Timeliness");
		else if (imp[3].equals("g")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Requirement");
		else if (imp[3].equals("h")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Leakage");
		else if (imp[3].equals("i")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Customer");
		else if (imp[3].equals("j")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.CorrectionCost");
		else if (imp[3].equals("k")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Other");
		if (unt[3].equals("a")) unt[3] = "N/A";
		else if (unt[3].equals("b")) unt[3] = "%";
		else if (unt[3].equals("c")) unt[3] = "day";
		else if (unt[3].equals("d")) unt[3] = "month";
		else if (unt[3].equals("e")) unt[3] = "person.day";
		else if (unt[3].equals("f")) unt[3] = "person.month";
		else if (unt[3].equals("g")) unt[3] = "$";
		else if (unt[3].equals("h")) unt[3] = "#";
		
	    if (est[3] > 0)
	    {
	    	estimated_impact = imp[3] + ": "+ CommonTools.formatDouble(est[3]) + " " + unt[3] + "; ";
	    }

		imp[4] = info.plannedImpact.substring(16, 17);
		unt[4] = info.plannedImpact.substring(17, 18);
		est[4] = Double.parseDouble(info.plannedImpact.substring(18, 32));

		if (imp[4].equals("a")) imp[4] = "N/A";
		else if (imp[4].equals("b")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Schedule");
		else if (imp[4].equals("c")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Effort");
		else if (imp[4].equals("d")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Finance");
		else if (imp[4].equals("e")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Team");
		else if (imp[4].equals("f")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Timeliness");
		else if (imp[4].equals("g")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Requirement");
		else if (imp[4].equals("h")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Leakage");
		else if (imp[4].equals("i")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Customer");
		else if (imp[4].equals("j")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.CorrectionCost");
		else if (imp[4].equals("k")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Other");
		if (unt[4].equals("a")) unt[4] = "N/A";
		else if (unt[4].equals("b")) unt[4] = "%";
		else if (unt[4].equals("c")) unt[4] = "day";
		else if (unt[4].equals("d")) unt[4] = "month";
		else if (unt[4].equals("e")) unt[4] = "person.day";
		else if (unt[4].equals("f")) unt[4] = "person.month";
		else if (unt[4].equals("g")) unt[4] = "$";
		else if (unt[4].equals("h")) unt[4] = "#";
		
	    if (est[4] > 0)
	    {
	    	estimated_impact = estimated_impact + imp[4] + ": "+ CommonTools.formatDouble(est[4]) + " " + unt[4] + "; ";
	    }

		imp[5] = info.plannedImpact.substring(32, 33);
		unt[5] = info.plannedImpact.substring(33, 34);
		est[5] = Double.parseDouble(info.plannedImpact.substring(34, 48));
	
		if (imp[5].equals("a")) imp[5] = "N/A";
		else if (imp[5].equals("b")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Schedule");
		else if (imp[5].equals("c")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Effort");
		else if (imp[5].equals("d")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Finance");
		else if (imp[5].equals("e")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Team");
		else if (imp[5].equals("f")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Timeliness");
		else if (imp[5].equals("g")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Requirement");
		else if (imp[5].equals("h")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Leakage");
		else if (imp[5].equals("i")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Customer");
		else if (imp[5].equals("j")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.CorrectionCost");
		else if (imp[5].equals("k")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Other");
		if (unt[5].equals("a")) unt[5] = "N/A";
		else if (unt[5].equals("b")) unt[5] = "%";
		else if (unt[5].equals("c")) unt[5] = "day";
		else if (unt[5].equals("d")) unt[5] = "month";
		else if (unt[5].equals("e")) unt[5] = "person.day";
		else if (unt[5].equals("f")) unt[5] = "person.month";
		else if (unt[5].equals("g")) unt[5] = "$";
		else if (unt[5].equals("h")) unt[5] = "#";
		
		
	    if (est[5] > 0)
	    {
	    	estimated_impact = estimated_impact + imp[5] + ": "+ CommonTools.formatDouble(est[5]) + " " + unt[5] ;
	    }
	%>
	<TD><%=estimated_impact%></TD>
	<TD><%=info.exposure%></TD>
	<TD><%=((info.mitigation == null)?"N/A":ConvertString.toHtmlForExcel(info.mitigation))%></TD>
	<TD><%=((info.contingencyPlan == null)?"N/A":ConvertString.toHtmlForExcel(info.contingencyPlan))%></TD>
	<TD><%=((info.triggerName == null)?"N/A":ConvertString.toHtmlForExcel(info.triggerName))%></TD>
	<TD><%=info.developerAcc%></TD>
	<TD><%=CommonTools.dateFormat(info.assessmentDate)%></TD>
	<TD> <%=languageChoose.getMessage("fi.jsp.paRiskExport.Occurred")%> </TD>
	
	<%
		String actual_impact = "";
		String effort_lost = "";
		String schedule_lost = "";
		
		imp[3] = (info.actualImpact == null) ? "N/A" : info.actualImpact.substring(0, 1);
		unt[3] = (info.actualImpact == null) ? "N/A" : info.actualImpact.substring(1, 2);
		est[3] = (info.actualImpact == null) ? Double.NaN : Double.parseDouble(info.actualImpact.substring(2, 16));
	
		if (imp[3].equals("a")) imp[3] = "N/A";
		else if (imp[3].equals("b")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Schedule");
		else if (imp[3].equals("c")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Effort");
		else if (imp[3].equals("d")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Finance");
		else if (imp[3].equals("e")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Team");
		else if (imp[3].equals("f")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Timeliness");
		else if (imp[3].equals("g")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Requirement");
		else if (imp[3].equals("h")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Leakage");
		else if (imp[3].equals("i")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Customer");
		else if (imp[3].equals("j")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.CorrectionCost");
		else if (imp[3].equals("k")) imp[3] = languageChoose.getMessage("fi.jsp.paRiskExport.Other");
		if (unt[3].equals("a")) unt[3] = "N/A";
		else if (unt[3].equals("b")) unt[3] = "%";
		else if (unt[3].equals("c")) unt[3] = "day";
		else if (unt[3].equals("d")) unt[3] = "month";
		else if (unt[3].equals("e")) unt[3] = "person.day";
		else if (unt[3].equals("f")) unt[3] = "person.month";
		else if (unt[3].equals("g")) unt[3] = "$";
		else if (unt[3].equals("h")) unt[3] = "#";
		
	    if (est[3] > 0)
	    {
	    	actual_impact = imp[3] + ": "+CommonTools.formatDouble(est[3]) + " " + unt[3] +  "; ";
	    	if (imp[3].equals("Effort"))
	    	{
		    	effort_lost = effort_lost + CommonTools.formatDouble(est[3]) + " " + unt[3]  + "; ";
		    	
	    	}

	    	if (imp[3].equals("Schedule"))
	    	{
		    	schedule_lost = schedule_lost + CommonTools.formatDouble(est[3]) + " " + unt[3]  + "; ";
	    	}
	    }

		imp[4] = (info.actualImpact == null) ? "N/A" : info.actualImpact.substring(16, 17);
		unt[4] = (info.actualImpact == null) ? "N/A" : info.actualImpact.substring(17, 18);
		est[4] = (info.actualImpact == null) ? Double.NaN : Double.parseDouble(info.actualImpact.substring(18, 32));

		if (imp[4].equals("a")) imp[4] = "N/A";
		else if (imp[4].equals("b")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Schedule");
		else if (imp[4].equals("c")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Effort");
		else if (imp[4].equals("d")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Finance");
		else if (imp[4].equals("e")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Team");
		else if (imp[4].equals("f")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Timeliness");
		else if (imp[4].equals("g")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Requirement");
		else if (imp[4].equals("h")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Leakage");
		else if (imp[4].equals("i")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Customer");
		else if (imp[4].equals("j")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.CorrectionCost");
		else if (imp[4].equals("k")) imp[4] = languageChoose.getMessage("fi.jsp.paRiskExport.Other");
		if (unt[4].equals("a")) unt[4] = "N/A";
		else if (unt[4].equals("b")) unt[4] = "%";
		else if (unt[4].equals("c")) unt[4] = "day";
		else if (unt[4].equals("d")) unt[4] = "month";
		else if (unt[4].equals("e")) unt[4] = "person.day";
		else if (unt[4].equals("f")) unt[4] = "person.month";
		else if (unt[4].equals("g")) unt[4] = "$";
		else if (unt[4].equals("h")) unt[4] = "#";
		
	    if (est[4] > 0)
	    {
	    	actual_impact = estimated_impact + imp[4] + ": " +CommonTools.formatDouble(est[4]) + " " + unt[4] +  "; ";
	    	if (imp[4].equals("Effort"))
	    	{
		    	effort_lost = effort_lost + CommonTools.formatDouble(est[4]) + " " + unt[4]  + "; ";
		    	
	    	}

	    	if (imp[4].equals("Schedule"))
	    	{
		    	schedule_lost = schedule_lost + CommonTools.formatDouble(est[4]) + " " + unt[4]  + "; ";
	    	}
	    }
	    
		imp[5] = (info.actualImpact == null) ? "N/A" : info.actualImpact.substring(32, 33);
		unt[5] = (info.actualImpact == null) ? "N/A" : info.actualImpact.substring(33, 34);
		est[5] = (info.actualImpact == null) ? Double.NaN : Double.parseDouble(info.actualImpact.substring(34, 48));
	
		if (imp[5].equals("a")) imp[5] = "N/A";
		else if (imp[5].equals("b")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Schedule");
		else if (imp[5].equals("c")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Effort");
		else if (imp[5].equals("d")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Finance");
		else if (imp[5].equals("e")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Team");
		else if (imp[5].equals("f")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Timeliness");
		else if (imp[5].equals("g")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Requirement");
		else if (imp[5].equals("h")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Leakage");
		else if (imp[5].equals("i")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Customer");
		else if (imp[5].equals("j")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.CorrectionCost");
		else if (imp[5].equals("k")) imp[5] = languageChoose.getMessage("fi.jsp.paRiskExport.Other");
		if (unt[5].equals("a")) unt[5] = "N/A";
		else if (unt[5].equals("b")) unt[5] = "%";
		else if (unt[5].equals("c")) unt[5] = "day";
		else if (unt[5].equals("d")) unt[5] = "month";
		else if (unt[5].equals("e")) unt[5] = "person.day";
		else if (unt[5].equals("f")) unt[5] = "person.month";
		else if (unt[5].equals("g")) unt[5] = "$";
		else if (unt[5].equals("h")) unt[5] = "#";
		
	    if (est[5] > 0)
	    {
	    	actual_impact = estimated_impact + imp[5] + ": " +CommonTools.formatDouble(est[5]) + " " + unt[5] ;
	    	if (imp[5].equals("Effort"))
	    	{
		    	effort_lost = effort_lost + CommonTools.formatDouble(est[5]) + " " + unt[5]  + "; ";
		    	
	    	}
	    	
	    	if (imp[5].equals("Schedule"))
	    	{
		    	schedule_lost = schedule_lost + CommonTools.formatDouble(est[5]) + " " + unt[5]  + "; ";
	    	}
	    }
	%>

   
	<TD>
   	<%
   		String processName = "Other";
   		for(int k=0;k<vtProcess.size();k++)
   		{
   			ProcessInfo psInfo=(ProcessInfo)vtProcess.get(k);
   			if (psInfo.processId == info.processId)
   			{
				processName = psInfo.name;
       			break;
   			}
   		}
   	%>
	<%=languageChoose.getMessage(processName)%>
	</TD>
	<TD><%=languageChoose.getMessage(ProjectInfo.parseLifecycle(info.projectType))%></TD>
	<TD></TD>
	<%
		String start_date = CommonTools.dateFormat(info.start_date);
	%>
	<TD><%="20" + start_date.substring(7,9)%></TD>
	<TD><%=effort_lost%></TD>
	<TD><%=schedule_lost%></TD>
	<TD></TD>
	<TD><%=info.groupName%></TD>
    </TR>
	
	<%
	}
	%>

</TABLE>
</BODY>
</HTML>
