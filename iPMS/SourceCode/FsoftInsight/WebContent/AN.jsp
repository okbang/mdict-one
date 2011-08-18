<%@ page contentType="application/msword; charset=UTF-8" import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp"%>
<%
	ProjectInfo projectInfo = (ProjectInfo) session.getAttribute("WOGeneralInfo"); 
	Vector deliverableList = (Vector) session.getAttribute("deliverableList"); 
	Vector perfVector = (Vector) session.getAttribute("WOPerformanceVector"); 
	Vector stdMetrics = (Vector) session.getAttribute("WOStandardMetricMatrix"); 
	Vector cmList = (Vector) session.getAttribute("WOCustomeMetricList"); 
	Vector teamList = (Vector) session.getAttribute("WOTeamList"); 
	Vector changeList = (Vector) session.getAttribute("WOChangeList"); 
	Vector changeVersionList = (Vector) session.getAttribute("WOChangeVersionList"); 
	Vector constraintList = (Vector) session.getAttribute("PLConstraintList"); 
	Vector assumptionList = (Vector) session.getAttribute("PLAssumptionList"); 
	Vector vtStage=(Vector)session.getAttribute("stageVector"); 
	Vector subTeamList = (Vector) session.getAttribute("WOSubTeamList"); 
	Vector dpVt=(Vector)session.getAttribute("defectPrevention"); 
	List teamEvaluationMatric = (List) session.getAttribute("teamEvaluationMatric");
	Vector vt = Assets.getPracticeList(Long.parseLong((String) session.getAttribute("projectID")));
	String[] values = (String[]) session.getAttribute("WOAcceptanceMatrix");
	int i;
%>
<%
	// divide Team to SubTeam part and Non subteam part
	int teamSize = 0;
	int subTeamSize = 0;
	int eCount = 0;
	if (teamList != null) teamSize = teamList.size();
	if (subTeamList != null) subTeamSize = subTeamList.size();
	

	
	int idx = 0;
	// Exist team
	long teamID = 0;
	Vector[] existTeam = new Vector[subTeamSize];
	
	for (int n = 0; n < subTeamSize; n++) {
		idx = 0;
		eCount = 0;
		teamSize = teamList.size();
		
		SubTeamInfo sInfo = (SubTeamInfo) subTeamList.elementAt(n);
		teamID = sInfo.teamID;
		existTeam[n] = new Vector();
		while(eCount != teamSize)
		{
			AssignmentInfo info = new AssignmentInfo();
			info = (AssignmentInfo) teamList.elementAt(idx);
			if (info.teamID == teamID) {
				existTeam[n].addElement(info);
				teamList.removeElementAt(idx);			
			} else idx++;
			eCount++;
		}
	}
%>

<%
	// map deliverables to stage
	int deliverSize = 0;
	int stageSize = 0;
	ModuleInfo 	moduleInfo = null;

	if (vtStage != null)  stageSize = vtStage.size();
	if (deliverableList != null)  deliverSize = deliverableList.size();
	
	Vector[] existStage  = new Vector[stageSize + 1];
	Date[] dStageEndDate = new Date[stageSize];
	
	// get all stage end date
	for (i = 0; i < stageSize; i++) {
		StageInfo stageInfo=(StageInfo) vtStage.get(i);
		
		existStage[i] = new Vector();
		dStageEndDate[i] = new Date();
		dStageEndDate[i] = stageInfo.pEndD;
		if (dStageEndDate[i] == null) dStageEndDate[i] = stageInfo.bEndD;
	}
	existStage[stageSize] = new Vector();
	
	int test = 0;
	for (int n = 0; n < deliverSize; n++) {
		moduleInfo = (ModuleInfo) deliverableList.elementAt(n);
		Hashtable moduleTable = new Hashtable();
		moduleTable.put(""+n,moduleInfo);
		
		Date dDeliveryDate = moduleInfo.rePlannedReleaseDate;			
		if (dDeliveryDate == null) dDeliveryDate = moduleInfo.plannedReleaseDate;
		
		test = 0;		
		for (i = 0; i < stageSize; i++) {
			if (dDeliveryDate.before(dStageEndDate[i]) || dDeliveryDate.equals(dStageEndDate[i])) {
				existStage[i].addElement(moduleTable);
				test++;
				break;
			}
		}
		if(test == 0){
			existStage[stageSize].addElement(moduleTable);
		}
	}
%>

<html>
<head>
<META http-equiv="Content-Type" content="application/msword; charset=UTF-8">
<LINK rel="stylesheet" href="AN.css" type="text/css">
<title>Acceptance Note</title>
</head>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");	
%>

<body lang=EN-US style='tab-interval:.5in;text-justify-trim:punctuation'>

<div class=Section1>

<table border=0 cellspacing=0 cellpadding=0 width=780 style='width:6.5in;
 margin-left:-.05in;border-collapse:collapse;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr style='height:72.1pt'>
  <td width=248 style='width:148.5pt;padding:0in 5.4pt 0in 5.4pt;height:72.1pt'>
  <p class=HeaderTitle2 style='margin-left:0in'><span style='text-effect:none'><img width=137 height=85
  src="WO_files/image002.gif" v:shapes="_x0000_i1027"><o:p></o:p></span></p>
  </td>
  <td width=700 style='width:500pt;padding:0in 5.4pt 0in 5.4pt;height:72.1pt'>
  	<p class=HeaderTitle align=right style='margin-left:0in;text-align:right'><%=languageChoose.getMessage("fi.jsp.WO.AccNote")%><o:p></o:p></p>
  </td>
 </tr>
</table>

<p class=HeadingBig style='text-align:center'><b><%=projectInfo.getProjectName()%></b></p>

<p class="sectiontitle"><b>1. <%=languageChoose.getMessage("fi.jsp.WO.ProjectDescription")%> </b></p>
<table border=1 cellspacing=0 cellpadding=0 style='margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr style='mso-row-margin-left:.25in'>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.ProjectCode")%></b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=projectInfo.getProjectCode()%></p>
  </td>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.ContractType")%></b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt' >
  	<p class=bang><%=projectInfo.getContractType()%></p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:.25in'>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.Customer")%></b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=projectInfo.getCustomer()%></p>
  </td>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><sup>2nd</sup> <b><%=languageChoose.getMessage("fi.jsp.WO.Customer")%></b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=projectInfo.getSecondCustomer()%></p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:.25in'>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.ProjectLevel")%></b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
	<p class=bang><%=projectInfo.getProjectLevel()%></p>
  </td>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.ProjectRank")%></b>
  	</p>
  </td>            
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang>
  	<%
   		String rank;
        if (projectInfo.getProjectRank() == null)
        	rank = "N/A"; 
        else if ("?".equals(projectInfo.getProjectRank()))
        	rank = "Not Rank";
        else  
        	rank = projectInfo.getProjectRank();
    %>
    <%=rank%>
	</p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:.25in'>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.Group")%></b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=projectInfo.getGroupName()%></p>
  </td>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.Division")%></b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=projectInfo.getDivisionName()%></p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:.25in'>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.ProjectType")%></b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=projectInfo.getProjectType()%></p>
  </td>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.ProjectManager")%></b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=projectInfo.getLeaderName()%></p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:.25in'>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.ProjectCategory")%></b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
	<p class=bang><%=projectInfo.getLifecycle()%></p>
  </td>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.BusinessDomain")%> </b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=projectInfo.getBusinessDomain()%></p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:.25in'>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0">
  	<p class=MsoHeading7><b><%=languageChoose.getMessage("fi.jsp.WO.ApplicationType")%></b></p>
  </td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=projectInfo.getApplicationType()%></p>
  </td>
  <td width=134 style='border:none;padding:0in 5.4pt 0in 5.4pt' bgcolor="#C0C0C0"></td>
  <td width=165 style='border:none;padding:0in 5.4pt 0in 5.4pt'></td>
 </tr>
</table>
<br>
<p class="sectiontitle"><b>2. <%=languageChoose.getMessage("fi.jsp.WO.ScopeAndPurpose")%> </b></p>
<p class=MsoNormal>
	<%
    	char cr = 13;
        char lf = 10;

        StringTokenizer strToken = new StringTokenizer(((projectInfo.getScopeAndObjective() == null) ? "" : projectInfo.getScopeAndObjective()), ""+cr+lf);
        String noteString = "";
        while (strToken.hasMoreElements()) {
        	noteString += strToken.nextToken();
            noteString += "<br>";
        }
	%>
    <%=((noteString.equals("")) ? "N/A" : noteString)%>
</p>

<p class="sectiontitle"><b>3. <%=languageChoose.getMessage("fi.jsp.WO.AssumptionsAndConstraints")%> </b></p>
<table border=1 cellspacing=0 cellpadding=0 width="598" style='margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr style='height:25.85pt'>
  <td width=24 bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;border-left:
  none;mso-border-left-alt:solid windowtext .75pt;;
  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
  <p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.WO.No")%></b></p>
  </td>
  <td width=150 bgcolor = "#C0C0C0"; style='width: 206px;border:solid windowtext .75pt;border-left:
  none;mso-border-left-alt:solid windowtext .75pt;
  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
  <p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.WO.Description")%></b></p>
  </td>
  <td width=300 bgcolor = "#C0C0C0"; style='width: 138px;border:solid windowtext .75pt;border-left:
  none;mso-border-left-alt:solid windowtext .75pt;
  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
  <p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.WO.Note")%></b></p>
  </td>
  <td width=94 bgcolor = "#C0C0C0"; style='width: 185px;border:solid windowtext .75pt;border-left:
  none;mso-border-left-alt:solid windowtext .75pt;
  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
  <p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.WO.Type")%></b>
  </p>
  </td>
 </tr>
 <tr>
 	<td colspan="4" style='border-top:none;border-left:none;
						  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
						  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
						  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
		  <p class=Bangheader style='page-break-after:auto; text-align:left'><b><%=languageChoose.getMessage("fi.jsp.WO.Assumptions")%></b></p>
 		</b>
 	</td>
 </tr>
 <%
 	if (assumptionList.size() <= 0)
 	{
 %>
 <tr>
  <td style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang style='text-align:center'>N/A</p>
  </td>
    <td style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang style='text-align:center'>N/A</p>
  </td>
  <td style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
 </tr>
 <%
 	}
 	else
 	{
 %>
 <%
	for(i = 0; i < assumptionList.size(); i++)
	{
		ConstraintInfo assumptionInfo = (ConstraintInfo) assumptionList.elementAt(i);
 %>
 <tr>
 <td width=24 align=center style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=i+1%></p>
  </td>
  <td width=200 style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=(assumptionInfo.description == null||assumptionInfo.description.trim().equals(""))?"N/A": ConvertString.toHtml(assumptionInfo.description)%></p>
  </td>
   <td width=300 style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=(assumptionInfo.note == null||assumptionInfo.note.trim().equals(""))?"N/A": ConvertString.toHtml(assumptionInfo.note)%></p>
  </td>
  <td width=150 style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=languageChoose.getMessage(assumptionInfo.GetNameOfType())%></p>
  </td>
 </tr>
 <%
 	}
 
 	}
 %>
 <tr>
 	<td colspan="4" style='border-top:none;border-left:none;
						  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
						  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
						  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
		  <p class=Bangheader style='page-break-after:auto; text-align:left'><b><%=languageChoose.getMessage("fi.jsp.WO.Constraints")%></b></p>
 		</b>
 	</td>
 </tr>
 <% if (constraintList.size() <= 0)
 	{
 %>
 <tr>
  <td style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
  <td style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang style='text-align:center'>N/A</p>
  </td>
  <td style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang style='text-align:center'>N/A</p>
  </td>
  <td style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><![if !supportEmptyParas]>&nbsp;<![endif]><o:p></o:p></p>
  </td>
 </tr>
 <%
 	}
 	else
 	{
 %>
 <%
 	for(i = 0; i < constraintList.size(); i++)
	{
	 	ConstraintInfo constraintInfo = (ConstraintInfo) constraintList.elementAt(i);
 %>
 <tr>
  <td width=24 align=center style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=i+1%></p>
  </td>
  <td width=200 style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=(constraintInfo.description == null||constraintInfo.description.trim().equals(""))?"N/A": ConvertString.toHtml(constraintInfo.description)%></p>
  </td>
    <td width=300 style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=(constraintInfo.note == null||constraintInfo.note.trim().equals(""))?"N/A": ConvertString.toHtml(constraintInfo.note)%></p>
  </td>
  <td width=150 style='border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=bang><%=languageChoose.getMessage(constraintInfo.GetNameOfType())%></p>
  </td>
 </tr>
 <%
 	}
 %>
 <%
 	}
 %>
</table>

<br>

<p class="sectiontitle"><b>4. <%=languageChoose.getMessage("fi.jsp.WO.ProjectMilestoneAndDeliverables")%> </b></p>
<table border=1 cellspacing=0 cellpadding=0 bgcolor="#c0ded8" width = "598" style='margin-left:
 5.4pt;background:#C0DED8;border-collapse:collapse;border:none;mso-border-alt:
 solid windowtext .75pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr>
  <td width=20 valign=top  bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b>#</b></p>
  </td>
  <td width=200 valign=top  bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.woDeliverable.Stage")%></b></p>
  </td>
  <td width=150 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.woDeliverable.Firstcommitted")%></b></p>
  </td>
  <td width=150 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.woDeliverable.Lastcommitted")%></b></p>
  </td>
  <td width=130 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.woDeliverable.Actual")%></b></p>
  </td>
  <td width=60 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.woDeliverable.Deviation")%></b></p>
  </td>
  <td width=60 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.woDeliverable.Note")%></b></p>
  </td>
 </tr>
 
<TBODY>
<%
	String moduleKey = "";
	Hashtable mHash = null;
	moduleInfo = null;
	int iNo = 0;
	int subSize = 0;
	for(int n = 0;n < stageSize;n++){
		StageInfo stageInfo=(StageInfo) vtStage.get(n);		
%>
 <tr>
 	<td colspan="2" style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=Bangheader style='page-break-after:auto; text-align:left'><b><%=stageInfo.stage == null? "": stageInfo.stage%></b>
	 </p>
	</td>
 	
 	<td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<% if( stageInfo.bEndD != null) { 
	  		out.println(CommonTools.dateFormat(new java.util.Date(stageInfo.bEndD.getTime())));
	  	 }
	  	 else {
	  		out.println("N/A");
	  	 }
		%> 
		</p>
	</td>
 	<td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<% if( stageInfo.pEndD != null) { 
	  		out.println(CommonTools.dateFormat(new java.util.Date(stageInfo.pEndD.getTime())));
	  	 }
	  	 else {
	  		out.println("N/A");
	  	 }
		%>
		</p>
	</td>
	<td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<% if( stageInfo.aEndD != null) { 
	  		out.println(CommonTools.dateFormat(new java.util.Date(stageInfo.aEndD.getTime())));
	  	 }
	  	 else {
	  		out.println("N/A");
	  	 }
		%>  
		</p>
	</td>
    <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	&nbsp;
		</p>
	</td>
    <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	&nbsp;
		</p>
	</td>
 </tr>
<%
	moduleKey = "";
	subSize = existStage[n].size();
	mHash = null;
	moduleInfo = null;
	for(i = 0; i < subSize; i++){
		iNo++;
		mHash = (Hashtable) existStage[n].elementAt(i);
 		moduleInfo = (ModuleInfo) mHash.elements().nextElement();
 		moduleKey = (String) mHash.keys().nextElement();
%>
<tr>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<%=iNo%>
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<%=moduleInfo.name%>
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<% if( moduleInfo.plannedReleaseDate != null) {
		 	out.println(CommonTools.dateFormat(new java.util.Date(moduleInfo.plannedReleaseDate.getTime())));
		 }else{
		 	out.println("N/A");
		 }
		 %> 
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<% if( moduleInfo.rePlannedReleaseDate != null) {
		 	out.println(CommonTools.dateFormat(new java.util.Date(moduleInfo.rePlannedReleaseDate.getTime())));
		 }else{
		 	out.println("N/A");
		 }
		 %>
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<% if( moduleInfo.actualReleaseDate != null) {
		 	out.println(CommonTools.dateFormat(new java.util.Date(moduleInfo.actualReleaseDate.getTime())));
		 }else{
		 	out.println("N/A");
		 }
		 %>
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<%= CommonTools.formatDouble(moduleInfo.deviation)%> 
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<%=(moduleInfo.note==null)? "N/A":ConvertString.toHtml(moduleInfo.note)%> 
		</p>
 </td>
</tr>
<%
	}
}
subSize = existStage[stageSize].size();
if(subSize > 0){
	%>
	 <tr>
	 	<td colspan="7" style='border-top:none;border-left:none;
		  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
		  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
		  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
		  <p class=Bangheader style='page-break-after:auto; text-align:left'><b>Stages are not defined</b>
		 </p>
		</td>
	 </tr>
	<%
}
	moduleKey = "";
	mHash = null;
	moduleInfo = null;
	for(i = 0; i < subSize; i++){
		iNo++;
		mHash = (Hashtable) existStage[stageSize].elementAt(i);
 		moduleInfo = (ModuleInfo) mHash.elements().nextElement();
 		moduleKey = (String) mHash.keys().nextElement();
%>
 <tr>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<%=iNo%>
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<%=moduleInfo.name%>
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<% if( moduleInfo.plannedReleaseDate != null) {
		 	out.println(CommonTools.dateFormat(new java.util.Date(moduleInfo.plannedReleaseDate.getTime())));
		 }else{
		 	out.println("N/A");
		 }
		 %> 
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<% if( moduleInfo.rePlannedReleaseDate != null) {
		 	out.println(CommonTools.dateFormat(new java.util.Date(moduleInfo.rePlannedReleaseDate.getTime())));
		 }else{
		 	out.println("N/A");
		 }
		 %>
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<% if( moduleInfo.actualReleaseDate != null) {
		 	out.println(CommonTools.dateFormat(new java.util.Date(moduleInfo.actualReleaseDate.getTime())));
		 }else{
		 	out.println("N/A");
		 }
		 %>
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<%= CommonTools.formatDouble(moduleInfo.deviation)%>
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<%=(moduleInfo.note==null)? "N/A":ConvertString.toHtml(moduleInfo.note)%>  
		</p>
 </td>
</tr>
<%
	}
%>
</TBODY>
</table>

<br>
<p class="sectiontitle"><b>5. Standard Objectives </b></p>

<table border=1 cellspacing=0 cellpadding=0 width="598" style='margin-left:
 5.4pt;background:#C0DED8;border-collapse:collapse;border:none;mso-border-alt:
 solid windowtext .75pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr>
  <td width=150 valign=top bgcolor = "#C0C0C0"; style='width:117.35pt;border:solid windowtext .75pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b><%=languageChoose.getMessage("fi.jsp.WO.Metrics")%> </b></p>
  </td>
  <td width=100 valign=top bgcolor = "#C0C0C0"; style='width:85.05pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b><%=languageChoose.getMessage("fi.jsp.WO.Unit")%></b> </p>
  </td>
  <td width=100 valign=top bgcolor = "#C0C0C0"; style='width:67.6pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b><%=languageChoose.getMessage("fi.jsp.woDeliverable.Firstcommitted")%> </b></p>
  </td>
  <td width=100 valign=top bgcolor = "#C0C0C0"; style='width:67.05pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b><%=languageChoose.getMessage("fi.jsp.woDeliverable.Lastcommitted")%></b> </p>
  </td>
  <td width=100 valign=top bgcolor = "#C0C0C0"; style='width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b><%=languageChoose.getMessage("fi.jsp.WO.Act")%> </p>
  </td>
  <td width=100 valign=top bgcolor = "#C0C0C0"; style='width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b><%=languageChoose.getMessage("fi.jsp.WO.Deviation")%> </b></p>
  </td>
 <td width=100 valign=top bgcolor = "#C0C0C0"; style='width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b><%=languageChoose.getMessage("fi.jsp.woDeliverable.Note")%></b> </p>
  </td>
 </tr>
 <tr>
  <td width=150 bgcolor = "#C0C0C0"; style='width:117.35pt;border:solid windowtext .75pt;border-top:
  none;mso-border-top-alt:solid windowtext .75pt;background:transparent;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=languageChoose.getMessage("fi.jsp.WO.StartDate")%> </p>
  </td>
  <td width=100 bgcolor = "#C0C0C0"; style='width:85.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang> dd-mmm-yy </p>
  </td>
  <td width=100 bgcolor = "#C0C0C0"; style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.dateFormat(((MetricInfo)perfVector.elementAt(0)).plannedValue)%><o:p></o:p></p>
  </td>
  <td width=100 bgcolor = "#C0C0C0"; style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.dateFormat(((MetricInfo)perfVector.elementAt(0)).rePlannedValue)%><o:p></o:p></p>
  </td>
  <td width=100 bgcolor = "#C0C0C0"; style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <p class=bang><%=CommonTools.dateFormat(((MetricInfo)perfVector.elementAt(0)).actualValue)%><o:p></o:p></p> 
  </td>
  <td width=100 bgcolor = "#C0C0C0"; style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(0)).deviation)%><o:p></o:p></p>
  </td>
  <td width=100 bgcolor = "#C0C0C0"; style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=(((MetricInfo)perfVector.elementAt(0)).note == null) ? "":ConvertString.breakString(((MetricInfo)perfVector.elementAt(0)).note, 10)%><o:p></o:p></p>
  </td>

 </tr>
 <tr>
  <td width=150 style='width:117.35pt;border:solid windowtext .75pt;border-top:
  none;mso-border-top-alt:solid windowtext .75pt;background:transparent;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=languageChoose.getMessage("fi.jsp.WO.EndDate")%> </p>
  </td>
  <td width=100 style='width:85.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang> dd-mmm-yy </p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.dateFormat(((MetricInfo)perfVector.elementAt(1)).plannedValue)%><o:p></o:p>
  </p>
  </td>
  <td width=113 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.dateFormat(((MetricInfo)perfVector.elementAt(1)).rePlannedValue)%><o:p></o:p>
  </p>
  </td>
 <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <p class=bang><%=CommonTools.dateFormat(((MetricInfo)perfVector.elementAt(1)).actualValue)%><o:p></o:p></p> 
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(1)).deviation)%><o:p></o:p></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=(((MetricInfo)perfVector.elementAt(1)).note == null) ? "":ConvertString.breakString(((MetricInfo)perfVector.elementAt(1)).note,10)%><o:p></o:p></p>
  </td>
 <tr>
  <td width=150 style='width:117.35pt;border:solid windowtext .75pt;border-top:
  none;mso-border-top-alt:solid windowtext .75pt;background:transparent;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=languageChoose.getMessage("fi.jsp.WO.Duration")%> </p>
  </td>
  <td width=100 style='width:85.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang> <%=languageChoose.getMessage("fi.jsp.WO.ElapsedDays")%> </p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(2)).plannedValue)%><o:p></o:p>
  </p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(2)).rePlannedValue)%><o:p></o:p>
  </p>
  </td>
  <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(2)).actualValue)%><o:p></o:p></p> 
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(2)).deviation)%><o:p></o:p></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=(((MetricInfo)perfVector.elementAt(2)).note == null) ? "":ConvertString.breakString(((MetricInfo)perfVector.elementAt(2)).note,10)%><o:p></o:p></p>
  </td>
 </tr>
 <tr>
  <td width=150 style='width:117.35pt;border:solid windowtext .75pt;border-top:
  none;mso-border-top-alt:solid windowtext .75pt;background:transparent;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=languageChoose.getMessage("fi.jsp.WO.MaximumTeamSize")%> </p>
  </td>
  <td width=100 style='width:85.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang> <%=languageChoose.getMessage("fi.jsp.WO.Person")%> </p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(3)).plannedValue)%><o:p></o:p>
  </p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(3)).rePlannedValue)%><o:p></o:p>
  </p>
  </td>
 <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(3)).actualValue)%><o:p></o:p></p> 
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(3)).deviation)%><o:p></o:p></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=(((MetricInfo)perfVector.elementAt(3)).note == null) ? "":ConvertString.breakString(((MetricInfo)perfVector.elementAt(3)).note,10)%><o:p></o:p></p>
  </td>
 </tr>
 <!--Huynh2 add billable effort -->
  <tr>
  <td width=150 style='width:117.35pt;border:solid windowtext .75pt;border-top:
  none;mso-border-top-alt:solid windowtext .75pt;background:transparent;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=languageChoose.getMessage("fi.jsp.WO.BillableEffort")%> </p>
  </td>
  <td width=100 style='width:85.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang> <%=languageChoose.getMessage("fi.jsp.WO.Personday")%> </p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(8)).plannedValue)%><o:p></o:p>
  </p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(8)).rePlannedValue)%><o:p></o:p>
  </p>
  </td>
  <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(8)).actualValue)%><o:p></o:p></p> 
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(8)).deviation)%><o:p></o:p></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=(((MetricInfo)perfVector.elementAt(8)).note == null) ? "":ConvertString.breakString(((MetricInfo)perfVector.elementAt(8)).note,10)%><o:p></o:p></p>
  </td>
 </tr>
 <!-- end for billable effort-->
 <!-- Calendar effort -->
  <tr>
  <td width=150 style='width:117.35pt;border:solid windowtext .75pt;border-top:
  none;mso-border-top-alt:solid windowtext .75pt;background:transparent;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=languageChoose.getMessage("fi.jsp.AN.CalendarEffort")%> </p>
  </td>
  <td width=100 style='width:85.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang> <%=languageChoose.getMessage("fi.jsp.AN.Personday")%> </p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(9)).plannedValue)%><o:p></o:p>
  </p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(9)).rePlannedValue)%><o:p></o:p>
  </p>
  </td>
  <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(9)).actualValue)%><o:p></o:p></p> 
  </td>
  <td width=158 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(9)).deviation)%><o:p></o:p></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=(((MetricInfo)perfVector.elementAt(9)).note == null) ? "":ConvertString.breakString(((MetricInfo)perfVector.elementAt(9)).note,10)%><o:p></o:p></p>
  </td>
 </tr>
 <tr>
  <td width=150 style='width:117.35pt;border:solid windowtext .75pt;border-top:
  none;mso-border-top-alt:solid windowtext .75pt;background:transparent;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=languageChoose.getMessage("fi.jsp.WO.EffortUsage")%> </p>
  </td>
  <td width=100 style='width:85.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang> <%=languageChoose.getMessage("fi.jsp.AN.Personday")%></p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(4)).plannedValue)%><o:p></o:p>
  </p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(4)).rePlannedValue)%><o:p></o:p>
  </p>
  </td>
 <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(4)).actualValue)%><o:p></o:p></p> 
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(4)).deviation)%><o:p></o:p></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=(((MetricInfo)perfVector.elementAt(4)).note == null) ? "":ConvertString.breakString(((MetricInfo)perfVector.elementAt(4)).note,10)%><o:p></o:p></p>
  </td>
 </tr>
</table>

<p class=MsoNormal>&nbsp;<o:p></o:p></p>

<table border=1 cellspacing=0 cellpadding=0 width = "598" style='margin-left:
 5.4pt;border-collapse:collapse;border:none;mso-border-alt:
 solid windowtext .75pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
	<TR colspan="5" width="598" valign="center" style="width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt">
	<TD rowspan="2" align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'> <b><%=languageChoose.getMessage("fi.jsp.WO.Metrics")%> </b></p></TD>
	<TD rowspan="2"  align="left"  width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b> <%=languageChoose.getMessage("fi.jsp.WO.Unit")%></b> </p></TD>
	<TD rowspan="1" colspan="3" align="center"  width=300 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Target")%></b></p></TD>    	
	<TD rowspan="2"  align="left"  width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b> <%=languageChoose.getMessage("fi.jsp.woPerformanceView.ActualValue")%></b></p></TD>
	<TD rowspan="2"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'> <b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Deviation")%></b></p> </TD> 
	<TD rowspan="2"  align="left" width=150 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'> <b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Basic")%></b></p> </TD> 
	</TR>
	<TR colspan="5" width=158 valign="center" style="width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt">
	<TD rowspan="1"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%></b></p></TD>
	<TD rowspan="1"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%></b></p></TD>
	<TD rowspan="1"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%></b></p></TD>
	</TR>
<%
for(i = 0; i < stdMetrics.size() - 1; i++){
NormInfo normInfo=(NormInfo)stdMetrics.elementAt(i);
String devStandMetrict = "";
devStandMetrict = CommonTools.formatDouble(CommonTools.metricDeviation(normInfo.plannedValue, normInfo.plannedValue, normInfo.actualValue));


if (i ==0) {
%>
         <TR>
	         <TD colspan="8" width=158 valign=top style="width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt"><p class=MsoHeading7 style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Quality")%></b></p></TD>
         </TR>
<%}
if (i ==3) {
%>
         <TR>
	         <TD colspan="8" width=158 valign=top style="width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt"><p class=MsoHeading7 style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.COST")%></b> </p></TD>
         </TR>
<%} 
if (i ==5) {
%>
         <TR>
	         <TD colspan="8" width=158 valign=top style="width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt"><p class=MsoHeading7 style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.DELIVERY")%></b> </p></TD>
         </TR>
<%}
%>
 <tr>
  <td width=100 style='width:117.35pt;border:solid windowtext .75pt;border-top:
  none;mso-border-top-alt:solid windowtext .75pt;background:transparent;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=languageChoose.getMessage(normInfo.normName)%></p>
  </td>
  <td width=100 style='width:85.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=normInfo.normUnit%></p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0><%=CommonTools.formatDouble(normInfo.lsl)%><span
  style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0><%=CommonTools.formatDouble(normInfo.plannedValue)%><span
  style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0><%=CommonTools.formatDouble(normInfo.usl)%><span
  style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
 <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0><%=CommonTools.formatDouble(normInfo.actualValue)%><span
  style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0><%=devStandMetrict%><span
  style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
  <td width=150 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0><%=(normInfo.note == null)? "":ConvertString.breakString(normInfo.note,15)%><span
  style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
 </tr>
<%
}
%>
</table>

<br>

<p class="sectiontitle"><b>6. <%=languageChoose.getMessage("fi.jsp.woPerformanceView.SpecificObjectives")%></b></p>

<table border=1 cellspacing=0 cellpadding=0 bgcolor="#c0ded8" width= "598" style='margin-left:
 5.4pt;background:#C0DED8;border-collapse:collapse;border:none;mso-border-alt:
 solid windowtext .75pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
	<TR colspan="5" width="598" valign="center" style="width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt">
	<TD rowspan="2" align="left" width=150 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'> <b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Name1")%></b> </p></TD>
	<TD rowspan="2"  align="left"  width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'> <b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Unit1")%></b> </p></TD>
	<TD rowspan="1" colspan="3" align="center"  width=300 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Target")%></b></p></TD>    	
	<TD rowspan="2"  align="left"  width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'> <b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.ActualValue")%> </b></p></TD>
	<TD rowspan="2"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'> <b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Deviation")%></b></p> </TD> 
	</TR>
	<TR colspan="5" width=158 valign="center" style="width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt">
	<TD rowspan="1"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.LSL")%></b></p></TD>
	<TD rowspan="1"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.Average")%></b></p></TD>
	<TD rowspan="1"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.woPerformanceView.USL")%></b></p></TD>
	</TR>

<tr>
 	<td colspan="7" style='border-top:none;border-left:none;
						  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
						  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
						  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
		  <p class=Bangheader style='page-break-after:auto; text-align:left'><b><%=languageChoose.getMessage("fi.jsp.woExport.DP")%></b></p>
 		</b>
 	</td>
 </tr>
  <%
	for(i = 0; i < dpVt.size(); i++)
	{
		DPTaskInfo dpTaskInfo = (DPTaskInfo) dpVt.elementAt(i);
	%>
 <tr>
  <td width=150 style='width:117.35pt;border:solid windowtext .75pt;border-top:
  none;mso-border-top-alt:solid windowtext .75pt;background:transparent;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=ConvertString.toHtml(dpTaskInfo.item)%></p>
  </td>
  <td width=100 style='width:85.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=ConvertString.toHtml(dpTaskInfo.unit)%></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=CommonTools.formatDouble(dpTaskInfo.planValue)%></p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0><%=CommonTools.formatDouble(dpTaskInfo.planValue)%><span
  style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
   <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=CommonTools.formatDouble(dpTaskInfo.planValue)%></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=CommonTools.formatDouble(dpTaskInfo.actualValue)%></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=CommonTools.formatDouble(CommonTools.metricDeviation(dpTaskInfo.planValue, dpTaskInfo.planValue, dpTaskInfo.actualValue))%></p>
  </td>
 </tr>
  <%
 }
%>
<tr>
 	<td colspan="7" style='border-top:none;border-left:none;
						  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
						  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
						  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
		  <p class=Bangheader style='page-break-after:auto; text-align:left'><b><%=languageChoose.getMessage("fi.jsp.woExport.Other")%></b></p>
 		</b>
 	</td>
 </tr>
<%
for(i = 0; i < cmList.size(); i++){
 	WOCustomeMetricInfo info = (WOCustomeMetricInfo) cmList.elementAt(i);
%>
 <tr>
  <td width=150 style='width:117.35pt;border:solid windowtext .75pt;border-top:
  none;mso-border-top-alt:solid windowtext .75pt;background:transparent;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=ConvertString.toHtml(info.name)%></p>
  </td>
  <td width=100 style='width:85.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=bang><%=ConvertString.toHtml(info.unit)%></p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0><%=CommonTools.formatDouble(info.LCL)%><span
  style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
    <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0><%=CommonTools.formatDouble(info.plannedValue)%><span
  style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
    <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0><%=CommonTools.formatDouble(info.UCL)%><span
  style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=CommonTools.formatDouble(info.actualValue)%></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0> <%=CommonTools.formatDouble(CommonTools.metricDeviation(info.plannedValue, info.plannedValue, info.actualValue))%></p>
  </p>
  </td>
 </tr>
<%
}
%>
</table>
<br>
<p class="sectiontitle"><b>7. <%=languageChoose.getMessage("fi.jsp.WO.Team")%></b></p>

<table border=1 cellspacing=0 cellpadding=0 bgcolor="#c0ded8" width = "598" style='margin-left:
 5.4pt;background:#C0DED8;border-collapse:collapse;border:none;mso-border-alt:
 solid windowtext .75pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr>
  <td width=100 valign=top  bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.WO.Role")%></b></p>
  </td>
  <td width=150 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.WO.Responsibility")%></b></p>
  </td>
  <td width=150 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b>Qualification</b></p>
  </td>
  <td width=130 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b>Full name</b></p>
  </td>
  <td width=60 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b>Type</b></p>
  </td>
  <td width=60 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b>% Effort</b></p>
  </td>
  <td width=60 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b><%=ConvertString.breakString(languageChoose.getMessage("fi.jsp.WO.StartDate"), 5)%></b></p>
  </td>
  <td width=60 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bangheader><b><%=ConvertString.breakString(languageChoose.getMessage("fi.jsp.WO.EndDate"),4)%></b></p>
  </td>
 </tr>	
<%
int count = 0;
int tSize = teamList.size();
for(i = 0; i < tSize; i++){
 	AssignmentInfo info = (AssignmentInfo) teamList.elementAt(i);
%>
 <tr style='height:25.85pt;mso-row-margin-left:14.2pt'>
  <td width=114 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=(info.roleID == null ? "&nbsp;" : info.roleID)%>
  	<%
  	if (info.responsibilityID == 9)
  	{
  		count = count + 1;
  		if (count == 1)
  		{
  	%>
	<%
		}		
  	}
  	%>  	
  	</p>  	
  </td>  
  <td width=140 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=((info.note == null)? "N/A" : ConvertString.toHtml(info.note))%></p>
  </td>
  <td width=140 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=((info.qualification == null)? "N/A" : ConvertString.toHtml(info.qualification))%></p>
  </td>
  <td width=110 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=ConvertString.toHtml(info.devName)%></p>
  </td>
  <td width=140 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang>
  	<%
		switch(info.type){
			case 1:%><%=languageChoose.getMessage("fi.jsp.woTeam.Onsite")%><%break;
			case 2:%><%=languageChoose.getMessage("fi.jsp.woTeam.Offshore")%><%break;
			case 3:%><%=languageChoose.getMessage("fi.jsp.woTeam.Tentative")%><%break;
			case 4:%><%=languageChoose.getMessage("fi.jsp.woTeam.Training")%><%break;
			case 5:%><%=languageChoose.getMessage("fi.jsp.woTeam.Off")%><%break;
	}%>
  	</p>
  </td>
  <td width=68 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=CommonTools.formatDouble(info.workingTime)%>%</p>
  </td>
  <td width=72 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=ConvertString.breakString(CommonTools.dateFormat(info.beginDate),6)%></p>
  </td>
  <td width=72 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=ConvertString.breakString(CommonTools.dateFormat(info.endDate), 6)%></p>
  </td>
  
 </tr>
<%
}
%>
	<%
 	if (count == 0)
 	{
 	%>
 <tr style='height:25.85pt;mso-row-margin-left:14.2pt'>
  <td width=114 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=languageChoose.getMessage("fi.jsp.WO.ProjectCommunicator")%>
  	</p>
  </td>
  <td width=110 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=languageChoose.getMessage("fi.jsp.WO.NA")%></p>
  </td>
  <td width=110 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=languageChoose.getMessage("fi.jsp.WO.NA")%></p>
  </td>
  <td width=68 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=languageChoose.getMessage("fi.jsp.WO.NA")%></p>
  </td>
  <td width=72 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=languageChoose.getMessage("fi.jsp.WO.NA")%></p>
  </td>
  <td width=72 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=languageChoose.getMessage("fi.jsp.WO.NA")%></p>
  </td>
  <td width=140 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=languageChoose.getMessage("fi.jsp.WO.NA")%></p>
  </td>
  <td width=140 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=languageChoose.getMessage("fi.jsp.WO.NA")%></p>
  </td>
 </tr>
 	<%
 	}
 	%>
 	
 	
<!-- landd start new -->
<%
for (int j = 0; j < subTeamSize; j++) {
	if (existTeam[j].size() ==0) continue;
	SubTeamInfo subTeamInfo =  (SubTeamInfo) subTeamList.elementAt(j);
%>
<tr>
	<td colspan = "8" valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
	<p class=bang><b>&nbsp;&nbsp;&nbsp;<%=ConvertString.toHtml(subTeamInfo.teamName)%><%=(subTeamInfo.teamNote ==null) ? "":": "+ConvertString.toHtml(subTeamInfo.teamNote)%></b></p>
	</td>
</tr>

<%
	tSize = existTeam[j].size();
	for(i = 0; i < tSize; i++){
	 	AssignmentInfo info = (AssignmentInfo) existTeam[j].elementAt(i);
%>
 <tr style='height:25.85pt;mso-row-margin-left:14.2pt'>
  <td width=114 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=(info.roleID == null ? "&nbsp;" : info.roleID)%>
  	</p>  	
  </td>  
  <td width=140 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=((info.note == null)? "N/A" : (info.note))%></p>
  </td>
  <td width=140 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=((info.qualification == null)? "N/A" : (info.qualification))%></p>
  </td>
  <td width=110 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=ConvertString.toHtml(info.devName)%></p>
  </td>
  <td width=140 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang>
  	<%
		switch(info.type){
			case 1:%><%=languageChoose.getMessage("fi.jsp.woTeam.Onsite")%><%break;
			case 2:%><%=languageChoose.getMessage("fi.jsp.woTeam.Offshore")%><%break;
			case 3:%><%=languageChoose.getMessage("fi.jsp.woTeam.Tentative")%><%break;
			case 4:%><%=languageChoose.getMessage("fi.jsp.woTeam.Training")%><%break;
			case 5:%><%=languageChoose.getMessage("fi.jsp.woTeam.Off")%><%break;
	}%>
  	</p>
  </td>
  <td width=68 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=CommonTools.formatDouble(info.workingTime)%>%</p>
  </td>
  <td width=72 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=ConvertString.breakString(CommonTools.dateFormat(info.beginDate),6)%></p>
  </td>
  <td width=72 valign=top style='background:white;border:solid windowtext .75pt;border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt height:25.85pt'>
  	<p class=bang><%=ConvertString.breakString(CommonTools.dateFormat(info.endDate),6)%></p>
  </td>
  
 </tr>
<%
	}
}
%> 	

<!-- landd end --> 	
</table>

<!--<table border=1 cellspacing=0 cellpadding=0 style='margin-left:5.4pt;border-collapse:collapse;
 border:none;mso-border-alt:solid black .5pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr style='mso-row-margin-left:14.2pt'>
  <td colspan=2 valign=top style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading9><span style='color:maroon;mso-bidi-font-style:normal'><%=languageChoose.getMessage("fi.jsp.WO.ApprovalDate")%><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:14.2pt'>
  <td width=330 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.NameOfProjectManager")%>&gt;<o:p></o:p></span></p>
  </td>
  <td width=330 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.NameOfDivisionLeaderOrGroupLeader")%>&gt;<o:p></o:p></span>
  	</p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:14.2pt'>
  <td width=330 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.NameOfGroupLeaderOrDirector")%>&gt;<o:p></o:p></span>
  	</p>
  </td>
  <td width=330 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.QARepresentativeName")%>&gt;<o:p></o:p></span>
  	</p>
  </td>
 </tr>
</table>-->

<br>
<p class="sectiontitle"><b>8. <%=languageChoose.getMessage("fi.jsp.WO.ChangesToTheWorkOrder")%></b>
</p>
<table border=1 cellspacing=0 cellpadding=0 style='margin-left:5.4pt;border-collapse:collapse;
 border:none;mso-border-alt:solid black .5pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr>
  <td width=100 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;
  padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b>Effective Date </b></p>
  </td>
  <td width=100 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b>Changed Item </b></p>
  </td>
  <td width=50 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b>A*M, D</b></p>
  </td>
  <td width=140 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b>Change Description</b></p>
  </td>
  <td width=140 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b>Reason for Change</b></p>
  </td>
  <td width=100 valign=top bgcolor = "#C0C0C0" style='border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bangheader> <b>Revision Number</b></p>
  </td>
 </tr>

 <%
	String action = "";
	int intAction;
	
    if (changeList != null) {
    	int iSize = changeList.size();
		for (i = 0; i < iSize ; i++) {
			WOChangeInfo info = (WOChangeInfo) changeList.elementAt(i);
			
		if (info.action == null) {
 			action = "";
 		} else {
 			intAction = Integer.parseInt(info.action);
 			switch (intAction) {
				case 1: action = "Added"; break;
				case 2: action = "Modified"; break;
				case 3: action = "Deleted"; break;
			}
		}
%>

	<tr>
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=((info.changeDate == null)? "N/A" : CommonTools.dateFormat(new java.util.Date(info.changeDate.getTime())))%></td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=info.item == null ? "N/A": info.item%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=action%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=((info.changes == null||info.changes.equals(""))? "N/A":ConvertString.toHtml(info.changes))%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=((info.reason == null||info.reason.equals(""))? "N/A" :ConvertString.toHtml(info.reason))%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=((info.version == null)? "N/A" : info.version)%></td>
	</tr>
<%
		}
	}	
%>
 
</table>


<!--<table border=1 cellspacing=0 cellpadding=0 style='margin-left:5.4pt;border-collapse:collapse;
 border:none;mso-border-alt:solid black .5pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr style='mso-row-margin-left:14.2pt'>
  <td colspan=2 width=660 valign=left style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading9><span style='color:maroon;mso-bidi-font-style:normal'><%=languageChoose.getMessage("fi.jsp.WO.ApprovalDate")%><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:14.2pt'>
  <td width=330 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.NameOfProjectManager")%>&gt;<o:p></o:p></span></p>
  </td>
  <td width=330 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.NameOfDivisionLeaderOrGroupLeader")%>&gt;<o:p></o:p></span>
  	<sup><a style='mso-endnote-id:edn1' href="#_edn1" name="_ednref1" title="">1</a></sup>
  	</p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:14.2pt'>
  <td width=330 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.NameOfGroupLeaderOrDirector")%>&gt;<o:p></o:p></span>
  	<sup><a style='mso-endnote-id:edn2' href="#_edn2" name="_ednref2" title="">2</a></sup>
  	</p>
  </td>
  <td width=330 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.QARepresentativeName")%>&gt;<o:p></o:p></span>
  	</p>
  </td>
 </tr>
</table>-->

<br>

<p class="sectiontitle"><b>9. <%=languageChoose.getMessage("fi.jsp.WO.Assets")%></b>
  <sup><a style='mso-endnote-id:edn3' href="#_edn3" name="_ednref3" title="">3</a></sup>  </p>
<p class=MsoNormal>  <%=((values[0]==null ||values[0].equals(""))?"N/A" : ConvertString.toHtml(values[0]))%></p>

<p class="sectiontitle"><b>10. Lessons/Practices</b></p>
<p class=MsoNormal>&nbsp;<o:p></o:p></p>

<table border=1 cellspacing=0 cellpadding=0 width = "600" style='margin-left:
 5.4pt;border-collapse:collapse;border:none;mso-border-alt:
 solid windowtext .75pt;mso-padding-alt:0in 5.4pt 0in 5.4pt;page-break-after:auto'>
 <TR colspan="5" width="598" valign="center" style="width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt">
    <TD align="center" rowspan="1" align="left" width=30 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'> <b> # </b></p></TD>
	<TD rowspan="1"  align="left"  width=90 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b> <%=languageChoose.getMessage("fi.jsp.practice.ScenarioProblem")%></b> </p></TD>
	<TD rowspan="1"  align="left"  width=130 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b> <%=languageChoose.getMessage("fi.jsp.practice.PracticeLessonSuggestion")%> </b> </p></TD>
	<TD rowspan="1"  align="center"  width=300 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b><%=languageChoose.getMessage("fi.jsp.practice.PracticeLessonSuggestion")%></b></p></TD>    	
	<TD rowspan="1"  align="left"  width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b> <%=languageChoose.getMessage("fi.jsp.practice.Category")%></b></p></TD>
</TR>
        <%
        String rowStyle="";
        String typeStr="";
        String scen="";
        String prac="";
        PracticeInfo pracInfo=null;
        for(int i2=0;i2<vt.size();i2++)
        {
        	
        	typeStr=languageChoose.getMessage("fi.jsp.practice.Lesson");
        	pracInfo=(PracticeInfo)vt.get(i2);
        	if(pracInfo.type==1) typeStr=languageChoose.getMessage("fi.jsp.practice.Practice");
        	if(pracInfo.type==2) typeStr=languageChoose.getMessage("fi.jsp.practice.Suggestion");
        	scen= pracInfo.scenario;
        	prac=pracInfo.practice;
        %>
        
        	<TR colspan="5" width="598" valign="center" style="width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt">
	        	<td align=center style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=i2+1%> </td>
	        	<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=ConvertString.toHtml(scen)%> </td>
	        	<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=ConvertString.toHtml(typeStr)%> </td>
	        	<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=ConvertString.toHtml(prac)%> </td>
	        	<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=languageChoose.getMessage(pracInfo.category)%> </td>
	        </tr>
     
        <%}%>
</TABLE>

<br>

<table border=1 cellspacing=0 cellpadding=0 style='margin-left:5.4pt;border-collapse:collapse;
 border:none;mso-border-alt:solid black .5pt;mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr style='mso-row-margin-left:14.2pt'>
  <td colspan=2 valign=top style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading9><span style='color:maroon;mso-bidi-font-style:normal'><%=languageChoose.getMessage("fi.jsp.WO.ApprovalDate")%><o:p></o:p></span></p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:14.2pt'>
  <td width=335 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.NameOfProjectManager")%>&gt;<o:p></o:p></span></p>
  </td>
  <td width=335 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.NameOfDivisionLeaderOrGroupLeader")%>&gt;<o:p></o:p></span>
  	</p>
  </td>
 </tr>
 <tr style='mso-row-margin-left:14.2pt'>
  <td width=335 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.NameOfGroupLeaderOrDirector")%>&gt;<o:p></o:p></span>
  	</p>
  </td>
  <td width=335 height=100 align=center valign=bottom style='border:none;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=MsoHeading8><span style='color:black;mso-bidi-font-style:normal'>&lt;<%=languageChoose.getMessage("fi.jsp.WO.QARepresentativeName")%>&gt;<o:p></o:p></span>
  	</p>
  </td>
 </tr>
</table>

<span style="font-size: 12pt; font-family: Times New Roman">
 <br style="PAGE-BREAK-BEFORE: always" clear="all">
 </span>
 
<table class="MsoNormalTable" border="0" cellspacing="0" cellpadding="0" width="624" style="width: 468.0pt; border-collapse: collapse; margin-left: -3.6pt">
  <tr style="height: 72.1pt">
   <td width="157" style="width:117.55pt;padding:0cm 5.4pt 0cm 5.4pt;height:72.1pt">
   <p class="Bangheader"><span style="font-size:10.0pt">FPT Software</span></p></td>
   <td width="467" style="width:350.45pt;padding:0cm 5.4pt 0cm 5.4pt;height:72.1pt">
   <p class="Bangheader"><span style="font-size:10.0pt">Social Republic of Vietnam</span></p>
   <p class="Bangheader"><span style="font-size:10.0pt">Independence - Freedom - Happiness</span></td>
  </tr>
 </table>
 <p></p>
 <p class=MsoNormal>&nbsp;<o:p></o:p></p>
 <p class="TeamEvaluation">LIST OF EMPLOYEES ENTITLED TO PROJECT ALLOWANCE</p>
 
 <p></p>
 
 <p class="sectiontitle"><b>Project: <%=projectInfo.getProjectName()%></b></p>
 <p class="sectiontitle"><b>Group: <%=projectInfo.getGroupName()%></b></p>
 <p class="sectiontitle"><b>Proposal:</b><br> </p>
 <p class=MsoNormal>
	 <%=((values[3]==null ||values[0].equals(""))?"N/A" : ConvertString.toHtml(values[3]))%>
 </p>
<br><br>
<table border=1 cellspacing=0 cellpadding=0 width = "600" style='margin-left:
 5.4pt;border-collapse:collapse;border:none;mso-border-alt:
 solid windowtext .75pt;mso-padding-alt:0in 5.4pt 0in 5.4pt;page-break-after:auto'>
	<TR colspan="5" width="598" valign="center" style="width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt">
	<TD rowspan="2" align="left" width=30 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'> <b> No. </b></p></TD>
	<TD rowspan="2"  align="left"  width=90 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b> Employee ID</b> </p></TD>
	<TD rowspan="2"  align="left"  width=130 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b> Name </b> </p></TD>
	<TD rowspan="1" colspan="3" align="center"  width=300 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b>Evaluation</b></p></TD>    	
	<TD rowspan="2"  align="left"  width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b> Allowance</b></p></TD>
	<TD rowspan="2"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'> <b>Notes</b></p> </TD> 
	</TR>
	<TR colspan="5" width=158 valign="center" style="width:94.95pt;border:solid windowtext .75pt;
  border-left:none;mso-border-left-alt:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt">
	<TD rowspan="1"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b>&nbsp;&nbsp;Role&nbsp;&nbsp;</b></p></TD>
	<TD rowspan="1"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b>&nbsp;&nbsp;% Effort&nbsp;&nbsp;</b></p></TD>
	<TD rowspan="1"  align="left" width=100 bgcolor = "#C0C0C0"><p class=Bangheader style='page-break-after:auto'><b>&nbsp;&nbsp;Efficiency&nbsp;&nbsp;</b></p></TD>
	</TR>

	
	<% for (int i1=0;i1<teamEvaluationMatric.size();i1++) {
        	TeamEvaluationInfo temp = (TeamEvaluationInfo)teamEvaluationMatric.get(i1);
        	String tempRole = "";
        	String tempEff= "";
        	String tempNote = "";
        	if (temp.getRole().length == 1) {
        		 tempRole = temp.getRole()[0];
        		 tempEff = temp.getHq()[0].equalsIgnoreCase("")?"N/A":temp.getHq()[0];
        		 tempNote = temp.getNote()[0];
        	}
        	else {
        		tempRole = temp.getRole()[0];
        		tempEff = temp.getHq()[0].equalsIgnoreCase("")?"N/A":temp.getHq()[0];
        		tempNote =temp.getNote()[0];
        		for (int j=1;j<temp.getRole().length;j++) {
        			tempRole = tempRole + " + " + temp.getRole()[j];
        			if(temp.getHq()[j].equalsIgnoreCase("")) 
        				tempEff = tempEff + " + " + "N/A";
        			else tempEff = tempEff + " + " +temp.getHq()[j];
        			
        			if (!temp.getNote()[j].equalsIgnoreCase(""))	
        				tempNote =tempNote +" ; "+ temp.getNote()[j];	
        			
        		}
        	}
        %>
	        <tr>
	        	<td align=center style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=i1+1%> </td>
	        	<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=temp.getStaffID()==null?"":temp.getStaffID()%> </td>
	        	<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=temp.getName()%> </td>
	        	<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=tempRole%> </td>
	        	<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=temp.getPercentAttend()%> %</td>
	        	<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=tempEff%> </td>
	        	<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=temp.getPc()%> </td>
	        	<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <%=ConvertString.toHtml(tempNote)%> </td>
	        </tr>
	     <%}%>
	     
	      <tr >
	     	<%
	     		float sumPC = 0 ; 
	     		for (int i1=0;i1<teamEvaluationMatric.size();i1++) {
	     			TeamEvaluationInfo temp = (TeamEvaluationInfo)teamEvaluationMatric.get(i1);
	     			if (temp.getPc()!=-1)
	     				sumPC+=temp.getPc();	
	     		}
	     	%>
	     	<td> </td>
	     	<td colspan=2 style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> <b> <h4> Total </h4> </b> </td>
	     	<td> </td>
	     	<td> </td>
	     	<td> </td>
	     	<td><b><%=sumPC%></b> </td>
	     	<td> </td>
	     </tr>
	
</table>

<p></p><p></p>
<p class=MsoNormal>&nbsp;<o:p></o:p></p>

<table class="MsoNormalTable" border="0" cellspacing="0" cellpadding="0" width="624" style="width: 468.0pt; border-collapse: collapse; margin-left: -3.6pt">
  <tr style="height: 72.1pt">
   <td width="312" style="width:156pt;padding:0cm 5.4pt 0cm 5.4pt;height:72.1pt">
   <p class="Bangheader"><span style="font-size:10.0pt">&lt;Name of Director<sup><a style='mso-endnote-id:edn2' href="#_edn2" name="_ednref2" title="">4</a></sup>&gt;</span></p></td>
   <td width="312" style="width:156pt;padding:0cm 5.4pt 0cm 5.4pt;height:72.1pt">
   <p class="Bangheader"><span style="font-size:10.0pt">&lt;Name of Group Leader&gt;</span></p></td>
   <td width="312" style="width:156pt;padding:0cm 5.4pt 0cm 5.4pt;height:72.1pt">
   <p class="Bangheader"><span style="font-size:10.0pt">&lt;Name of Project Manager&gt;</span></p></td>
  </tr>
 </table>


<p class=MsoNormal><span style='mso-tab-count:7'></span></p>

<p class=MsoNormal>&nbsp;<o:p></o:p></p>

</div>

<div style='mso-element:endnote-list'>

<![if !supportEndnotes]><br clear=all>
<hr align=left size=1 width="33%">
<![endif]>



<div style='mso-element:endnote' id=edn3><p class=MsoEndnoteText><a style='mso-endnote-id:edn3' href="#_ednref3" name="_edn3" title="">
<span class=MsoEndnoteReference><span style='vertical-align:baseline;vertical-align:baseline'><sup>1</sup></span></span></a>
<%=languageChoose.getMessage("fi.jsp.WO.NoteNumber01")%> <br> <p class=MsoEndnoteText><a style='mso-endnote-id:edn4' href="#_ednref4" name="_edn4" title="">
<span class=MsoEndnoteReference><span style='vertical-align:baseline;vertical-align:baseline'><sup>2</sup></span></span></a>
<%=languageChoose.getMessage("fi.jsp.WO.NoteNumber02")%>
</p>

<div style='mso-element:endnote' id=edn2><p class=MsoEndnoteText><a style='mso-endnote-id:edn2' href="#_ednref2" name="_edn2" title="">
<span class=MsoEndnoteReference><span style='vertical-align:baseline;vertical-align:baseline'><sup>3</sup></span></span></a>
<%=languageChoose.getMessage("fi.jsp.WO.NoteNumber03")%><br> <p class=MsoEndnoteText><a style='mso-endnote-id:edn4' href="#_ednref4" name="_edn4" title="">
<span class=MsoEndnoteReference><span style='vertical-align:baseline;vertical-align:baseline'><sup>4</sup></span></span></a>
Branch Director is required if PM suggests an ADDITIONAL allowance amount
</p>
</div>

</p>
</div>



</body>
</html>
