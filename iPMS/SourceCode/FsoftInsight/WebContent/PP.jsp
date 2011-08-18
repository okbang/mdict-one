<%@ page contentType="application/msword; charset=UTF-8" import="com.fms1.infoclass.*,com.fms1.common.*,com.fms1.web.*,java.util.*,java.io.*,java.text.*,com.fms1.tools.*" errorPage="error.jsp"%>

<%
	int right = Security.securiPage("Project plan",request,response);
	int i = 0;
	int j = 0;
	int iSize = 0;
	
	EffortTypeInfo effortTypeInfo = (EffortTypeInfo) session.getAttribute("PLEffortInfo");
	ProjectInfo projectInfo = (ProjectInfo) session.getAttribute("PLGeneralInfo");
	Vector constraintList = (Vector) session.getAttribute("PLConstraintList");
	Vector assumptionList = (Vector) session.getAttribute("PLAssumptionList");
	
	Vector perfVector = (Vector) session.getAttribute("WOPerformanceVector");
	Vector dpVt=(Vector)session.getAttribute("defectPrevention");
	Vector cmList = (Vector) session.getAttribute("WOCustomeMetricList");
	Vector riskList       = (Vector)session.getAttribute("riskList");
	
	// 2. PROJECT DEVELOPMENT APPROACH
	// 2.1 Project Process
	Vector tailoringList = (Vector) session.getAttribute("tailoringList");
	
	final DefectByProcessInfo[] defectProcess = (DefectByProcessInfo[])session.getAttribute("defectProcess");
	Vector dModuleList = (Vector) session.getAttribute("defectModuleList");
    Hashtable Process_WorkProduct =  (Hashtable) session.getAttribute("Process_WP");
    // Map product with process
	int mSize = 0;
	int tmpSize = 0;
	if (dModuleList != null) mSize = dModuleList.size();
	Vector[] vModule = new Vector[4];
	
	vModule[0] = new Vector();
	vModule[1] = new Vector();
	vModule[2] = new Vector();
	vModule[3] = new Vector();
	
	for (i = 0; i < mSize; i++) {
		WPSizeInfo mInfo = (WPSizeInfo) dModuleList.elementAt(i);
		if (Process_WorkProduct.containsKey(mInfo.categoryName)) {
			if ("Requirement".equals(Process_WorkProduct.get(mInfo.categoryName))) {
				mInfo.processType = 1;
				vModule[0].addElement(mInfo);
			}
			else if ("Design".equals(Process_WorkProduct.get(mInfo.categoryName))) {
				mInfo.processType = 2;
				vModule[1].addElement(mInfo);
			}
			else if ("Coding".equals(Process_WorkProduct.get(mInfo.categoryName))) {
				mInfo.processType = 3;
				vModule[2].addElement(mInfo);
			}
			else if ("Other".equals(Process_WorkProduct.get(mInfo.categoryName))) {
				mInfo.processType = 4;
				vModule[3].addElement(mInfo);
			}
		}
	}
	
	double[] totalPlanReview = new double[4];
	double[] totalPlanTest	 = new double[4];
	// init data
	for (int j1=0;j1<4;j1++){
		totalPlanReview[j1] = 0;
		totalPlanTest[j1] = 0;
	}
	
	for (int i1 = 0; i1 < 4; i1++){
		for (int j1 = 0; j1 < vModule[i1].size(); j1++)
		{
			WPSizeInfo mInfo = (WPSizeInfo) vModule[i1].elementAt(j1);
			if (mInfo.isDefectReview!=0) {
				if (!CommonTools.formatDouble(mInfo.newPlanSizeReview).equalsIgnoreCase("N/A"))
	  			totalPlanReview[i1] = totalPlanReview[i1] + mInfo.newPlanSizeReview;
	  		}
	  		if (mInfo.isDefectTest!=0) {
				if (!CommonTools.formatDouble(mInfo.newPlanSizeTest).equalsIgnoreCase("N/A"))
		  		totalPlanTest[i1] = totalPlanTest[i1] + mInfo.newPlanSizeTest;
			}
		}
	}
	
	ReqChangesInfo reqInfo 	= (ReqChangesInfo) session.getAttribute("ReqChangesInfo");
	Vector vIntegrList 		= (Vector) session.getAttribute("ProIntegrList");
	Vector vStratList 		= (Vector) session.getAttribute("StratOfMeetingList");
	Vector vRevList 		= (Vector) session.getAttribute("ReviewStrategyList");
	Vector vMeasList 		= (Vector) session.getAttribute("MeasurementsProgList");
	Vector vTestList 		= (Vector) session.getAttribute("TestStrategyList");
	
	// 3. ESTIMATE
	Vector moduleList = (Vector) session.getAttribute("moduleList");
	Vector vt=(Vector)session.getAttribute("trainingVector");
	Vector vProjSchedList = (Vector) session.getAttribute("ProjectScheduleList");
	Vector vtToolList = (Vector)session.getAttribute("toolVector");
	
	// 4. PROJECT ORGANIZATION START
	String orgStructure = (String) session.getAttribute("plOrgStructure");
	Vector teamList = (Vector) session.getAttribute("WOTeamList");
	Vector subTeamList = (Vector) session.getAttribute("WOSubTeamList");
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
	
	for (i = 0; i < subTeamSize; i++) {
		idx = 0;
		eCount = 0;
		teamSize = teamList.size();
		
		SubTeamInfo sInfo = (SubTeamInfo) subTeamList.elementAt(i);
		teamID = sInfo.teamID;
		existTeam[i] = new Vector();
		while(eCount != teamSize)
		{
			AssignmentInfo info = new AssignmentInfo();
			info = (AssignmentInfo) teamList.elementAt(idx);
			if (info.teamID == teamID) {
				existTeam[i].addElement(info);
				teamList.removeElementAt(idx);			
			} else idx++;
			eCount++;
		}
	}
	
	// INTERFACE
	Vector interfaceList = (Vector) session.getAttribute("plInterfaceList");
	// divide Interface to 3 part start
	// 1: Fsoft, 2: Customer, 3: Other project
	
	int iType = 0;
	InterfaceInfo iInfo = null;
	if (interfaceList != null) iSize = interfaceList.size();
	
	Vector[] vIList = new Vector[3];
	
	
	vIList[0] = new Vector();
	vIList[1] = new Vector();
	vIList[2] = new Vector();
	
	
	for (i = 0; i < iSize; i++) {		
		iInfo = (InterfaceInfo) interfaceList.elementAt(i);
		iType = iInfo.type;
		switch(iType) {
			case 1 :
				vIList[0].addElement(iInfo);
				break;
			case 2 :
				vIList[1].addElement(iInfo);
				break;
			case 3 :
				vIList[2].addElement(iInfo);
				break;
			default : break;
		}
	}
	
	Vector subcontractList = (Vector) session.getAttribute("subcontractVector");
	
	// 5.	COMMUNICATION & REPORTING
	Vector vComReportList = (Vector) session.getAttribute("ComReportList");
	
	
	// REFERENCES
	Vector refList		  = (Vector) session.getAttribute("PLReferenceList");
	
	Vector deliverableList = (Vector) session.getAttribute("deliverableList");
	Vector dependencyList = (Vector) session.getAttribute("PLDependencyList");
	Vector iterationList = (Vector) session.getAttribute("PLIterationList");
	Vector vtStage=(Vector)session.getAttribute("stageVector");
	Vector milestoneList = (Vector)session.getAttribute("plMilestoneList");
	
	Vector ToolsInfrastructure=(Vector)session.getAttribute("toolVector");
	Vector finanVt=(Vector)session.getAttribute("finanVector");
	Vector costVt=(Vector)session.getAttribute("costVector");
	CostTotalInfo totalInfo=(CostTotalInfo)session.getAttribute("costTotalInfo");
	Vector stdMetrics = (Vector) session.getAttribute("WOStandardMetricMatrix");
	String strQltObj=(String)session.getAttribute("qltObjective");
	Vector ReviewAndTest = (Vector)session.getAttribute("moduleVector");
	Vector vtOtherAct=(Vector)session.getAttribute("otherActVector");
	
	String ProjectOverview=(String)session.getAttribute("ProjectOverview"); 
	String ProjectDevelopmentApproach=(String)session.getAttribute("ProjectDevelopmentApproach");
	//String ProjectLifecycle=(String)session.getAttribute("ProjectLifecycle");
	String Estimate=(String)session.getAttribute("Estimate");
	String ProjectOrganization=(String)session.getAttribute("ProjectOrganization");
	String Communication_N_Reporting=(String)session.getAttribute("Communication_N_Reporting");
	String SecurityAspects=(String)session.getAttribute("SecurityAspects");
	
	String totalEstimatedSize = (String) session.getAttribute("TotalEstimatedSize");
	String duration = (String) session.getAttribute("duration");
	
	session.setAttribute("change_source_page", "1"); //for risks
	
	String className;
	
	int exportOrder = 0;
	
	// HuyNH2 add code for project archive
	String archiveStatus = (String)session.getAttribute("archiveStatus");	
	boolean isArchive = false;
	if ((archiveStatus != null) && (!"".equals(archiveStatus))) {
		if (Integer.parseInt(archiveStatus) == 4) {
			isArchive = true;
		}
	}
	
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
<LINK rel="stylesheet" href="PP.css" type="text/css">
<META http-equiv="Content-Type" content="application/msword; charset=UTF-8">
<title>Project Plan</title>
</head>
<%
	LanguageChoose languageChoose = (LanguageChoose)session.getAttribute("LanguageChoose");	
%>

<body lang=EN-US style='tab-interval:.5in;text-justify-trim:punctuation'>

<div class=Section1>

<p class="MsoNormal" style="line-height: normal; text-autospace: ideograph-numeric ideograph-other; margin-left: 0in; margin-right: 0in; margin-top: 0in; margin-bottom: .0001pt">&nbsp;</p>
<p class="HeadingBig" style="line-height: normal; text-autospace: ideograph-numeric ideograph-other; margin-left: 0in; margin-right: 0in; margin-top: 6.0pt; margin-bottom: .0001pt" align="center"><![if !vml]><img border=0 width=137 height=85
src="WO_files/image002.gif" v:shapes="_x0000_s1025"><![endif]></p>
<p class="HeadingBig" style="line-height: normal; text-autospace: ideograph-numeric ideograph-other; margin-left: 0in; margin-right: 0in; margin-top: 6.0pt; margin-bottom: .0001pt">
&nbsp;</p>
<p class="HeadingBig" style="line-height: normal; text-autospace: ideograph-numeric ideograph-other; margin-left: 0in; margin-right: 0in; margin-top: 6.0pt; margin-bottom: .0001pt">
&nbsp;</p>
<p class="HeadingBig" style="line-height: normal; text-autospace: ideograph-numeric ideograph-other; margin-left: 0in; margin-right: 0in; margin-top: 6.0pt; margin-bottom: .0001pt">
&nbsp;</p>
<p class="HeadingBig" style="line-height: normal; text-autospace: ideograph-numeric ideograph-other; margin-left: 0in; margin-right: 0in; margin-top: 6.0pt; margin-bottom: .0001pt">
<b><span style="font-size: 20.0pt; letter-spacing: 1.5pt">&nbsp;</span></b></p>
<p class="HeadingBig" style="line-height: normal; text-autospace: ideograph-numeric ideograph-other; margin-left: 0in; margin-right: 0in; margin-top: 6.0pt; margin-bottom: .0001pt">
<b><span style="font-size: 20.0pt; letter-spacing: 1.5pt">&nbsp;</span></b></p>
<p class="HeadingBig" style="line-height: normal; text-autospace: ideograph-numeric ideograph-other; margin-left: 0in; margin-right: 0in; margin-top: 6.0pt; margin-bottom: .0001pt" align="center">
<b><span style="font-size: 20.0pt; letter-spacing: 1.5pt">&lt;<%=projectInfo.getProjectName()%>&gt;</span></b></p>
<p class="HeadingBig" style="line-height: normal; text-autospace: ideograph-numeric ideograph-other; margin-left: 0in; margin-right: 0in; margin-top: 6.0pt; margin-bottom: .0001pt" align="center">
<b><span style="font-size: 20.0pt; letter-spacing: 1.5pt">Project Plan</span></b></p>
<p class="MsoNormal">&nbsp;</p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span style="line-height: 150%; font-family: Tahoma">Project Code: &lt;<%=projectInfo.getProjectCode()%>&gt;</span></p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span lang="FR" style="line-height: 150%; font-family: Tahoma">Document Code: 
&lt;Code of the document &gt;– v&lt;x.x&gt;</span></p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span lang="FR" style="line-height: 150%; font-family: Tahoma">&nbsp;</span></p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span lang="FR" style="line-height: 150%; font-family: Tahoma">&nbsp;</span></p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span lang="FR" style="line-height: 150%; font-family: Tahoma">&nbsp;</span></p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span lang="FR" style="line-height: 150%; font-family: Tahoma">&nbsp;</span></p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span lang="FR" style="line-height: 150%; font-family: Tahoma">&nbsp;</span></p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span lang="FR" style="line-height: 150%; font-family: Tahoma">&nbsp;</span></p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span lang="FR" style="line-height: 150%; font-family: Tahoma">&nbsp;</span></p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span lang="FR" style="line-height: 150%; font-family: Tahoma">&nbsp;</span></p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span lang="FR" style="line-height: 150%; font-family: Tahoma">&nbsp;</span></p>
<p class="NormalCaption" align="center" style="text-align: center; line-height: 150%; layout-grid-mode: both; margin-left: 0in">
<span lang="FR" style="line-height: 150%; font-family: Tahoma">&nbsp;</span></p>
<p class="NormalTB" style="margin-top: 6.0pt" align="center"><b>
<span style="font-family: Verdana; layout-grid-mode: line">&lt;Location, issued 
date of the Document&gt;</span></b></p>
<p class="MsoNormal" style="line-height: normal; text-autospace: ideograph-numeric ideograph-other; margin-left: 0in; margin-right: 0in; margin-top: 0in; margin-bottom: .0001pt">&nbsp;</p>
<p class="MsoNormal" style="line-height: normal; text-autospace: ideograph-numeric ideograph-other; margin-left: 0in; margin-right: 0in; margin-top: 0in; margin-bottom: .0001pt">&nbsp;</p>

<%
if(ProjectOverview.equals("true")){
	exportOrder++;
%>
<%-- 1.Project Overview --%>

<h1 style="text-indent: -27.35pt; line-height: normal; page-break-before: always; page-break-after: auto; text-autospace: ideograph-numeric ideograph-other; margin-left: 27.35pt; margin-right: 0in; margin-top: .25in; margin-bottom: 6.0pt">
<b>
<span style="font-size: 12.0pt; font-family: Arial; color: #000000; text-transform: uppercase; letter-spacing: 0pt">
<%=exportOrder%>.<span style="font:7.0pt &quot;Times New Roman&quot;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>Project 
Overview</span></b></h1>
<BR/>
<p/>
<p class="sectiontitlePP"><b>1.1. <%=languageChoose.getMessage("fi.jsp.WO.ProjectDescription")%> </b></p>
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

<%--1.2.	Scope and Purpose--%>

<br>
<p class="sectiontitlePP"><b>1.2. <%=languageChoose.getMessage("fi.jsp.WO.ScopeAndPurpose")%> </b></p>
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

<%--1.3.	Assumptions & Constraints  --%>

<p class="sectiontitlePP"><b>1.3. <%=languageChoose.getMessage("fi.jsp.WO.AssumptionsAndConstraints")%> </b></p>
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
<%-- 1.4.	Project Objectives --%>
<p class="sectiontitlePP"><b>1.4. Project Objectives </b></p>
<p class="sectiontitlePP1"><b>1.4.1. Standard Objectives </b></p>
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
  <p class=Bangheader> <b><%=languageChoose.getMessage("fi.jsp.WO.Act")%> </b> </p>
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
    <p class=bang><%=(CommonTools.dateFormat(((MetricInfo)perfVector.elementAt(0)).rePlannedValue).equals("N/A"))?"":CommonTools.dateFormat(((MetricInfo)perfVector.elementAt(0)).rePlannedValue)%><o:p></o:p></p>
  </td>
  <td width=100 bgcolor = "#C0C0C0"; style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <p class=bang><o:p></o:p></p>
  </td>
  <td width=100 bgcolor = "#C0C0C0"; style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
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
    <p class=bang><%=(CommonTools.dateFormat(((MetricInfo)perfVector.elementAt(1)).rePlannedValue).equals("N/A"))?"":CommonTools.dateFormat(((MetricInfo)perfVector.elementAt(1)).rePlannedValue)%><o:p></o:p></p>
  </p>
  </td>
 <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
<p class=bang><o:p></o:p></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
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
    <p class=bang><%=(CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(2)).rePlannedValue).equals("N/A"))?"":CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(2)).rePlannedValue)%><o:p></o:p></p>
  </td>
  <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <!--<p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(2)).actualValue)%><o:p></o:p></p> -->
 <p class=bang><o:p></o:p></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
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
    <p class=bang><%=(CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(3)).rePlannedValue).equals("N/A"))?"":CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(3)).rePlannedValue)%><o:p></o:p></p>
  </td>
 <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <!--<p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(3)).actualValue)%><o:p></o:p></p> -->
 <p class=bang><o:p></o:p></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
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
    <p class=bang><%=(CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(8)).rePlannedValue).equals("N/A"))?"":CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(8)).rePlannedValue)%><o:p></o:p></p>
  </td>
  <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <!--<p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(8)).actualValue)%><o:p></o:p></p> -->
 <p class=bang><o:p></o:p></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
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
    <p class=bang><%=(CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(9)).rePlannedValue).equals("N/A"))?"":CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(9)).rePlannedValue)%><o:p></o:p></p>
  </td>
  <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <!--<p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(9)).actualValue)%><o:p></o:p></p> -->
 <p class=bang><o:p></o:p></p>
  </td>
  <td width=158 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
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
  <p class=bang><%=(CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(4)).rePlannedValue).equals("N/A"))?"":CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(4)).rePlannedValue)%><o:p></o:p>
  </td>
 <td width=100 style='width:67.05pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
 <!--<p class=bang><%=CommonTools.formatDouble(((MetricInfo)perfVector.elementAt(4)).actualValue)%><o:p></o:p></p> -->
 <p class=bang><o:p></o:p></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
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
  <p class=Bang0><%=CommonTools.formatDouble(normInfo.usl)%><span
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
  <p class=Bang0><%=CommonTools.formatDouble(normInfo.lsl)%><span
  style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
 <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  
  <p class=Bang0><span style='mso-bidi-font-family:Tahoma;mso-fareast-language:JA'><o:p></o:p></span></p>
  </td>
  <td width=100 style='width:67.6pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0><span
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

<p class="sectiontitlePP1"><b>1.4.2. <%=languageChoose.getMessage("fi.jsp.woPerformanceView.SpecificObjectives")%></b></p>

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
  <p class=Bang0></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0></p>
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
  <p class=Bang0></p>
  </td>
  <td width=100 style='width:94.95pt;border-top:none;border-left:none;
  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
  <p class=Bang0></p>
  </p>
  </td>
 </tr>
<%
}
%>
</table>
<br>
<%-- 1.5. Critical Dependencies--%>
<p class="sectiontitlePP"><b>1.5. Critical Dependencies</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
	<tr>
		<td width=24 bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>#</b></td>
		<td style="width: 270px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in; background: #C0C0C0">
		<p class=Bangheader style="page-break-after:auto"><b>Critical dependency</b></td>
		<td style="width: 100px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in; background: #C0C0C0">
		<p class=Bangheader style="page-break-after:auto"><b>Expected Delivery Date</b> </td>
		<td style="width: 124px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in; background: #C0C0C0">
		<p class=Bangheader style="page-break-after:auto"><b>Note</b></td>
		<td style="width: 78px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in; background: #C0C0C0">
		<p class=Bangheader style="page-break-after:auto"><b>Status</b></td>
	</tr>
	<%
	for(i = 0; i < dependencyList.size(); i++){
		className = (i%2==0)?"CellBGRnews":"CellBGR3";
	 	DependencyInfo dependencyInfo = (DependencyInfo) dependencyList.elementAt(i);
	%>
	<tr style="height: 25.85pt">
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
		<p class="Bang"><%=i+1%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=((dependencyInfo.item == null||dependencyInfo.item.equals(""))?"N/A":ConvertString.toHtml(dependencyInfo.item))%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=((dependencyInfo.plannedDeliveryDate == null)? "N/A": CommonTools.dateFormat(new java.util.Date(dependencyInfo.plannedDeliveryDate.getTime())))%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=((dependencyInfo.note == null||dependencyInfo.note.equals(""))?"N/A":ConvertString.toHtml(dependencyInfo.note))%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"></td>
	</tr>
	<%}%>
</table>
<br>
<%-- 1.6.	Project Risk  --%>
<p class="sectiontitlePP"><b>1.6. Project Risk</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 	<tr>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>#</b></td>
		<td bgcolor = "#C0C0C0"; style="width:100px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.plOverview.Description")%></b></td>
		<td bgcolor = "#C0C0C0"; style="width:140px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.riskAddnew.Source")%></b></td>
		<td bgcolor = "#C0C0C0"; style="width:100px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.riskAddnew.Probability")%></b></td>
		<td bgcolor = "#C0C0C0"; style="width:100px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.paRiskExport.Exposure")%></b></td>
		<td bgcolor = "#C0C0C0"; style="width:132px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.riskAddnew.Trigger")%></b></td>
	</tr>
	<%if (riskList != null) {
		for (i = 0; i < riskList.size(); i++) {
		RiskInfo risk = (RiskInfo) riskList.elementAt(i);
		className = "";
		String trimmedCondition = risk.condition;
		if (trimmedCondition != null) {
			if (trimmedCondition.length() > 50) {
				trimmedCondition = trimmedCondition.substring(0, 50) + "...";
			}
		} else {
			trimmedCondition = "N/A";
		}

		className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";%>
		<TR class="<%=className%>">
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=i+1%></td>
			<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
			<p class="Bang"><%=trimmedCondition%></td>
			<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
			<p class="Bang"><%=risk.sourceName%></td>
			<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
			<p class="Bang"><%=CommonTools.formatDouble(risk.probability)%></td>
			<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
			<p class="Bang"><%=CommonTools.formatDouble(risk.exposure)%></td>
			<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
			<p class="Bang"><%=risk.triggerName == null ? "":ConvertString.breakString(risk.triggerName,12)%></td>
		</TR>
		
		<%}
	}%>
	
</table>

<%
}
%>

<%-- Section --%>

<%
if(ProjectDevelopmentApproach.equals("true")){
	exportOrder++;
%>
<%-- 2.	PROJECT DEVELOPMENT APPROACH --%>
<h1 style="text-indent: -27.35pt; line-height: normal; page-break-before: always; page-break-after: auto; text-autospace: ideograph-numeric ideograph-other; margin-left: 27.35pt; margin-right: 0in; margin-top: .25in; margin-bottom: 6.0pt">
<b>
<span style="font-size: 12.0pt; font-family: Arial; color: #000000; text-transform: uppercase; letter-spacing: 0pt">
<%=exportOrder%>.<span style="font:7.0pt &quot;Times New Roman&quot;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
 PROJECT DEVELOPMENT APPROACH</span></b></h1>
<BR/>
<p/>
<%-- 2.1.	Project process --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.1. Project Process</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>#</b></td>
		<td bgcolor = "#C0C0C0"; style="width:120px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.tailoring.Description")%></b></td>
		<td bgcolor = "#C0C0C0"; style="width:120px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.tailoring.Reason")%></b></td>
		<td bgcolor = "#C0C0C0"; style='width:120px;border:solid windowtext .75pt; padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.tailoring.Action")%></b></td>
		<td bgcolor = "#C0C0C0"; style='width:82px;border:solid windowtext .75pt; padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.tailoring.Type")%></b></td>
	</tr>
<%
    if (tailoringList != null) {
    	iSize = tailoringList.size();
		for (i = 0; i < iSize ; i++) {
			TailoringDeviationInfo info = (TailoringDeviationInfo) tailoringList.elementAt(i);
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
		<p class="Bang"><%=i+1%></td>		
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=((info.modification == null)?"N/A":ConvertString.toHtml(info.modification))%></td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=((info.reason == null)?"N/A":ConvertString.toHtml(info.reason))%></td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang">
			<%switch(info.action){
				case 1:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Added")%> <%break; 
				case 2:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Modified")%> <%break;
				case 3:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Deleted")%> <%break; 
			}%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang">
			<%switch(info.type){
				case 1:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Tailoring")%> <%break; 
				case 2:%> <%=languageChoose.getMessage("fi.jsp.tailoring.Deviation")%> <%break;	 
			}%></td>
	</tr>
<%
		}
	}	
%>
</table>

<br/>

<%-- 2.2.	Requirement Change Management --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.2. Requirement Change Management</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 <tr>
  <td style="width:250px;height: 25.85pt; border-right: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
  	<p class=Bang>Where is the change request logged?</p>
  </td>
  <td style='width:348px; ;padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bang><%=reqInfo.reqLogLocation == null ? "N/A":ConvertString.toHtml(reqInfo.reqLogLocation)%></p>
  </td>  
 </tr>
 <tr>
  <td style="width:250px;height: 25.85pt; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
  	<p class=Bang>Who logs the change request?</p>
  </td>
  <td style='width:348px; padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bang><%= reqInfo.reqCreator == null ? "N/A":ConvertString.toHtml(reqInfo.reqCreator)%></p>
  </td>  
 </tr>
 <tr>
  <td style="width:250px;height: 25.85pt;border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
  	<p class=Bang>Who reviews the change request?</p>
  </td>
  <td style='width:348px; padding:0in 5.4pt 0in 5.4pt'>
	<p class=Bang><%= reqInfo.reqReviewer == null ? "N/A":ConvertString.toHtml(reqInfo.reqReviewer)%></p>
  </td>  
 </tr>
 <tr>
  <td style="width:250px;height: 25.85pt; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
  	<p class=Bang>Who approves the change request?</p>
  </td>
  <td style='width:348px; padding:0in 5.4pt 0in 5.4pt'>
  	<p class=Bang><%= reqInfo.reqApprover == null ? "N/A":ConvertString.toHtml(reqInfo.reqApprover)%></p>
  </td> 
 </tr>
</table>
<br>

<%-- 2.3.	Product Integration Strategy --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.3. Product Integration Strategy</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>#</b></td>
		<td bgcolor = "#C0C0C0"; style="width:120px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Component ID</b></td>
		<td bgcolor = "#C0C0C0"; style="width:120px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Description</b></td>
		<td bgcolor = "#C0C0C0"; style='width:120px;border:solid windowtext .75pt; padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Integrated with Components</b></td>
		<td bgcolor = "#C0C0C0"; style='width:82px;border:solid windowtext .75pt; padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Integration order</b></td>
		<td bgcolor = "#C0C0C0"; style='width:100px;border:solid windowtext .75pt; padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Intergration ready need</b></td>
	</tr>
<%
    if (vIntegrList != null) {
    	iSize = vIntegrList.size();
		for (i = 0; i < iSize ; i++) {
			IntegrStratInfo integrInfo = (IntegrStratInfo) vIntegrList.elementAt(i);
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
		<p class="Bang"><%=i+1%></td>		
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=integrInfo.integrCompID == null ? "N/A": integrInfo.integrCompID%></td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=integrInfo.integrDesc == null ? "N/A": integrInfo.integrDesc%></td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=integrInfo.integrWithComp == null ? "N/A": integrInfo.integrWithComp%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=integrInfo.integrOrder == null ? "N/A": integrInfo.integrOrder%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=integrInfo.integrOrder == null ? "N/A": integrInfo.integrReadyNeed%></td>
	</tr>
<%
		}
	}	
%>
</table>
<br/>

<%-- 2.4.	Quality Management --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.4. Quality Management</b></p>
<%-- 2.4.1.	Estimates of Defects to be detected --%>
<p class="sectiontitlePP1"><b><%=exportOrder%>.4.1. Estimates of Defects to be detected</b></p>
<p class="sectiontitlePP1"><b>&nbsp;&nbsp;Pre-release review defects</b></p>
<TABLE border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
	<TR>
		<td bgcolor = "#C0C0C0"; style='width:160px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Process</b></td>
		<td bgcolor = "#C0C0C0"; style='width:110px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Planned found by review</b></td>
		<td bgcolor = "#C0C0C0"; style='width:110px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Actual found by review</b></td>
	</TR>
<%
	double totalNorm = 0;
	double totalNewPlanSize = 0;	
	double totalActual = 0;
	DefectByProcessInfo processObj;	
	int iCount = 0;
	boolean existAdd = false;
	for (i = 0; i < 4; i++){
	
		processObj=defectProcess[i];
		
		if (!CommonTools.formatDouble(processObj.normReview).equalsIgnoreCase("N/A"))
	  		totalNorm = totalNorm + processObj.normReview;  	
	
	  	if (!CommonTools.formatDouble(processObj.actualReview).equalsIgnoreCase("N/A"))
	  		totalActual = totalActual + processObj.actualReview;

%>
	<TR>
		<td style="font-weight: bold;height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=languageChoose.getMessage(processObj.processName)%></td>
		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=CommonTools.formatDouble(totalPlanReview[i])%></td>
		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><B><%=CommonTools.formatDouble(processObj.actualReview)%></B></td>
		
	</TR>
	
<%		
    	tmpSize = vModule[i].size();
		for (j = 0; j < tmpSize; j++)
		{
			WPSizeInfo mInfo = (WPSizeInfo) vModule[i].elementAt(j);
			if (mInfo.isDefectReview==0) {
				existAdd = true;
				continue;
			}
			
			iCount++;
			if (!CommonTools.formatDouble(mInfo.newPlanSizeReview).equalsIgnoreCase("N/A"))
	  		totalNewPlanSize = totalNewPlanSize + mInfo.newPlanSizeReview;
	  		
%>
	<TR>
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=mInfo.name%></td>
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=CommonTools.formatDouble(mInfo.newPlanSizeReview)%></td>
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=CommonTools.formatDouble(mInfo.actualDefReview)%></td>
	</TR>
<%
		}
	}
%>
	<TR>
		<td bgcolor = "#C0C0C0"; style='width:160px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bang style="page-break-after:auto">Total</td>
		<td bgcolor = "#C0C0C0"; style='width:110px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bang style="page-break-after:auto"><%=CommonTools.formatDouble(totalNewPlanSize)%></td>
		<td bgcolor = "#C0C0C0"; style='width:110px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bang style="page-break-after:auto"><%=CommonTools.formatDouble(totalActual)%></td>
	</TR>    
</TABLE>
<BR>
<p class="sectiontitlePP1"><b>&nbsp;&nbsp;Pre-release test defects</b></p>
<TABLE border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>	
 
	<TR>
		<td bgcolor = "#C0C0C0"; style='width:160px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Process</b></td>
		<td bgcolor = "#C0C0C0"; style='width:110px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Planned found by test</b></td>
		<td bgcolor = "#C0C0C0"; style='width:110px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Actual found by test</b></td>
	</TR>
<%
		//only for coding now but might evolve
    	totalNorm = 0;
    	totalNewPlanSize = 0;    	
    	totalActual = 0;
    	iCount = 0;
    	
		for (i = 0; i < 4; i++)
		{
			processObj=defectProcess[i];
  			if (!CommonTools.formatDouble(processObj.normTest).equalsIgnoreCase("N/A"))
  				totalNorm = totalNorm + processObj.normTest;
  			
  			if (!CommonTools.formatDouble(processObj.actualTest).equalsIgnoreCase("N/A"))
  				totalActual = totalActual + processObj.actualTest;

%>
	<TR>
		<td style="font-weight: bold;height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=languageChoose.getMessage(processObj.processName)%></td>
		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=CommonTools.formatDouble(totalPlanTest[i])%></td>
		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><B><%=CommonTools.formatDouble(processObj.actualTest)%></B></td>
		
	</TR>
<%		
    	tmpSize = vModule[i].size();
		for (j = 0; j < tmpSize; j++)
		{			
			WPSizeInfo mInfo = (WPSizeInfo) vModule[i].elementAt(j);
			if (mInfo.isDefectTest == 0) {
				existAdd = true;
				continue;
			}
			iCount++;			
			if (!CommonTools.formatDouble(mInfo.newPlanSizeTest).equalsIgnoreCase("N/A"))
	  		totalNewPlanSize = totalNewPlanSize + mInfo.newPlanSizeTest;
%>
	<TR>
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=mInfo.name%></td>
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=CommonTools.formatDouble(mInfo.newPlanSizeTest)%></td>
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=CommonTools.formatDouble(mInfo.actualDefTest)%></td>
	</TR>
<%
		}
	}
%>
	<TR>
		<td bgcolor = "#C0C0C0"; style='width:160px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bang style="page-break-after:auto">Total</td>
		<td bgcolor = "#C0C0C0"; style='width:110px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bang style="page-break-after:auto"><B><%=CommonTools.formatDouble(totalNewPlanSize)%></B></td>
		<td bgcolor = "#C0C0C0"; style='width:110px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bang style="page-break-after:auto"><B><%=CommonTools.formatDouble(totalActual)%></B></td>
	</TR>  
</TABLE>
<br/>

<%-- 2.4.2.	Strategy for Meeting Quality Objectives --%>
<p class="sectiontitlePP1"><b><%=exportOrder%>.4.2. Strategy for Meeting Quality Objectives</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Strategy</b></td>
		<td bgcolor = "#C0C0C0"; style="width:286px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Expected Benefits</b></td>		
	</tr>
<%
    if (vStratList != null) {
    	iSize = vStratList.size();
		for (i = 0; i < iSize ; i++) {
			StrategyOfMeetingInfo stratInfo = (StrategyOfMeetingInfo) vStratList.elementAt(i);
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=stratInfo.stratDesc == null ? "N/A": stratInfo.stratDesc%></td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=stratInfo.stratExBene == null ? "N/A": stratInfo.stratExBene%></td>
	</tr>
<%
		}
	}	
%>
</table>
<br/>

<%-- 2.4.3.	Quality Control --%>
<p class="sectiontitlePP1"><b><%=exportOrder%>.4.3. Quality Control</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Review Item</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Type of Review</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Reviewer</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>When</b></td>
	</tr>
<%
    if (vRevList != null) {
    	iSize = vRevList.size();
		for (i = 0; i < iSize ; i++) {
			ReviewStrategyInfo revInfo = (ReviewStrategyInfo) vRevList.elementAt(i);
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=revInfo.revItem == null ? "N/A": revInfo.revItem%></td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang">
		<%=revInfo.revType%>
		</td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=revInfo.revReviewer == null ? "N/A": revInfo.revReviewer%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=revInfo.revWhen == null ? "N/A": revInfo.revWhen%></td>
	</tr>
<%
		}
	}	
%>
</table>
<br/>

<%-- 2.4.4.	Measurements Program --%>
<p class="sectiontitlePP1"><b><%=exportOrder%>.4.4. Measurements Program</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style='page-break-after:auto'><b>Data to be collected</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style='page-break-after:auto'><b>Purpose</b></td>
		<td bgcolor = "#C0C0C0"; style="width:100px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style='page-break-after:auto'><b>Responsible</b></td>
		<td bgcolor = "#C0C0C0"; style="width:123px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style='page-break-after:auto'><b>When</b></td>
	</tr>
<%
    if (vMeasList != null) {
    	iSize = vMeasList.size();
		for (i = 0; i < iSize ; i++) {
			MeasureProgInfo measInfo = (MeasureProgInfo) vMeasList.elementAt(i);
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=measInfo.mes_data_colect == null ? "N/A": measInfo.mes_data_colect%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=measInfo.mes_purpose == null ? "N/A": measInfo.mes_purpose%></td>
		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=measInfo.mes_responsible == null ? "N/A": measInfo.mes_responsible%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=measInfo.mes_when == null ? "N/A": measInfo.mes_when%></td>
	</tr>
<%
		}
	}	
%>
</table>
<br/>

<%-- 2.5.	Unit Testing Strategy --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.5. Unit Testing Strategy</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 	<tr>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Item</b></td>
		<td bgcolor = "#C0C0C0"; style="width:100px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Reviewer</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Completion criteria</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Entry criteria</b></td>
	</tr>
<%
    if (vTestList != null) {
    	iSize = vTestList.size();    	
		for (i = 0; i < iSize ; i++) {
			TestStrategyInfo testInfo = (TestStrategyInfo) vTestList.elementAt(i);
			if (testInfo.testType != 1) continue;			
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testItem == null ? "N/A": testInfo.testItem%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testReviewer == null ? "N/A": testInfo.testReviewer%></td>
		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testComplCriteria == null ? "N/A": testInfo.testComplCriteria%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testEntryCriteria == null ? "N/A": testInfo.testEntryCriteria%></td>
	</tr>
<%
		}
	}	
%>
</table>
<br/>
<%-- 2.6.	Integration Testing  Strategy --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.6. Integration Testing  Strategy</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 	<tr>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Item</b></td>
		<td bgcolor = "#C0C0C0"; style="width:100px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Reviewer</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Completion criteria</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Entry criteria</b></td>
	</tr>
<%
    if (vTestList != null) {
    	iSize = vTestList.size();    	
		for (i = 0; i < iSize ; i++) {			
			TestStrategyInfo testInfo = (TestStrategyInfo) vTestList.elementAt(i);
			if (testInfo.testType != 2) continue;
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testItem == null ? "N/A": testInfo.testItem%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testReviewer == null ? "N/A": testInfo.testReviewer%></td>
		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testComplCriteria == null ? "N/A": testInfo.testComplCriteria%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testEntryCriteria == null ? "N/A": testInfo.testEntryCriteria%></td>
	</tr>
<%
		}
	}	
%>
</table>
<br/>
<%-- 2.7.	System Test Strategy --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.7. System Testing Strategy</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 	<tr>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Item</b></td>
		<td bgcolor = "#C0C0C0"; style="width:100px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Reviewer</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Completion criteria</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Entry criteria</b></td>
	</tr>
<%
    if (vTestList != null) {
    	iSize = vTestList.size();    	
		for (i = 0; i < iSize ; i++) {			
			TestStrategyInfo testInfo = (TestStrategyInfo) vTestList.elementAt(i);
			if (testInfo.testType != 3) continue;
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testItem == null ? "N/A": testInfo.testItem%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testReviewer == null ? "N/A": testInfo.testReviewer%></td>
		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testComplCriteria == null ? "N/A": testInfo.testComplCriteria%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=testInfo.testEntryCriteria == null ? "N/A": testInfo.testEntryCriteria%></td>
	</tr>
<%
		}
	}	
%>
</table>
<br/>
<%}%>



<%-- Section --%>

<%
if(Estimate.equals("true")){
	exportOrder++;
%>
<%-- 3.	ESTIMATE --%>
<h1 style="text-indent: -27.35pt; line-height: normal; page-break-before: always; page-break-after: auto; text-autospace: ideograph-numeric ideograph-other; margin-left: 27.35pt; margin-right: 0in; margin-top: .25in; margin-bottom: 6.0pt">
<b>
<span style="font-size: 12.0pt; font-family: Arial; color: #000000; text-transform: uppercase; letter-spacing: 0pt">
<%=exportOrder%>.<span style="font:7.0pt &quot;Times New Roman&quot;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
 ESTIMATE</span></b></h1>
<BR/>
<p/>
<%-- 3.1. Size --%>

<p class="sectiontitlePP"><b><%=exportOrder%>.1. Size</b></p>
<TABLE border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
		<TR>			
			<td align = "center" bgcolor= "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style="page-break-after:auto"><b> <%=languageChoose.getMessage("fi.jsp.moduleList.Name")%></b></td>			
			<td align = "center" bgcolor= "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.moduleList.Workproduct")%></b></td>
			<td align = "center" bgcolor= "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.moduleList.PlannedsizeUCP")%></b></td>
			<td align = "center" bgcolor= "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.moduleList.ReplannedsizeUCP")%></b></td>
			<td align = "center" bgcolor= "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.moduleList.ActualsizeUCP")%></b></td>
			<td align = "center" bgcolor= "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.moduleList.CreatedsizeUCP")%></b></td>
			<td align = "center" bgcolor= "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style="page-break-after:auto"><b><%=languageChoose.getMessage("fi.jsp.moduleList.Description")%></b></td>			
		</TR>
		<%			
			iSize = 0;
			if (moduleList != null) iSize = moduleList.size();
			
			for (i = 0; i < iSize; i++) 
			{
				WPSizeInfo module = (WPSizeInfo) moduleList.elementAt(i);
				className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
		%>
		<TR class="<%=className%>">
			<TD><p class="Bang"><%= ConvertString.breakString(ConvertString.toHtml(module.name),15)%></TD>
			<TD><p class="Bang"> <%=languageChoose.getMessage(module.categoryName)%> </TD>
			<TD><p class="Bang"> <%=CommonTools.formatDouble(module.estimatedSizeConv)%> </TD>
			<TD><p class="Bang"> <%=CommonTools.formatDouble(module.reestimatedSizeConv)%> </TD>
			<TD><p class="Bang"> <%=CommonTools.formatDouble(module.actualSizeConv)%> </TD>
			<TD><p class="Bang"> <%=CommonTools.formatDouble(module.createdSize)%> </TD>
			<TD><p class="Bang"> <%=module.description != null ? ConvertString.breakString(ConvertString.toHtml(module.description),22) : "N/A"%> </TD>
		</TR>
		<%						
			}
		%>
	
</TABLE>

<br>

<%-- 3.2. Effort --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.2. Effort</b></p>
<p class=MsoNormal><i>	(Refer to excel file)</i></p>

<%-- 3.3. Schedule --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.3. Schedule</b></p>
<br>
<%-- 3.3.1 Project Milestone & Deliverables --%>
<p class="sectiontitlePP1"><b><%=exportOrder%>.3.1. Project Milestone & Deliverables</b></p>
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
		 	out.println("");
		 }
		 %>
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<%=(CommonTools.formatDouble(moduleInfo.deviation).equals("N/A")? "":CommonTools.formatDouble(moduleInfo.deviation))%> 
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<%=(moduleInfo.note==null)? "":ConvertString.toHtml(moduleInfo.note)%> 
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
	  	<%=(CommonTools.formatDouble(moduleInfo.deviation).equals("N/A")? "":CommonTools.formatDouble(moduleInfo.deviation))%> 
		</p>
 </td>
 <td style='border-top:none;border-left:none;
	  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
	  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
	  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
	  <p class=bang>
	  	<%=(moduleInfo.note==null)? "":ConvertString.toHtml(moduleInfo.note)%>  
		</p>
 </td>
</tr>
<%
	}
%>
</TBODY>
</table>
<br>

<%-- 3.3.2 Project Schedule --%>
<p class="sectiontitlePP1"><b><%=exportOrder%>.3.2. Project Schedule</b></p>
<TABLE border=1 cellspacing=0 cellpadding=0 width="598" style='margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
	<TR class="ColumnLabel">
		<td width = "24" bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.plProjSchedList.Activity")%></b></td>		
		<td bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.plProjSchedList.StartDate")%></b></td>		
		<td bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.plProjSchedList.Responsible")%></b></td>		
		<td bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.plProjSchedList.Note")%></b></td>
	</TR>

<%		
		iSize = 0;
		if (vProjSchedList != null) iSize = vProjSchedList.size();
		ProjSchedInfo projSchedInfo;
		int currentType = 0;
		int nextType = 0;
		for(i = 0; i < iSize-1; i++){
			projSchedInfo = (ProjSchedInfo) vProjSchedList.elementAt(i);
		 	currentType = projSchedInfo.schedType;		 	
		 	nextType = ((ProjSchedInfo) vProjSchedList.elementAt(i+1)).schedType;		 	
		 	if (i == 0) {
%>		
		
		<TR>
			<TD colspan="4" style='border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style='page-break-after:auto; text-align:left'><b>
			<%
				switch(currentType){
					case 1:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DefectPreventionType")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QualityControlType")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ProjectTrackingType")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ConfigurationManagementType")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QAType")%><%break;
					case 6:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DARType")%><%break;
					case 7:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.OtherType")%><%break;
				}
			%>
			</b></p></TD>
		</TR>
		<%	
			}  
			if (currentType != nextType ) {
		%>
		<TR>			
			<td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%= projSchedInfo.schedActivity == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedActivity)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			<td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=projSchedInfo.schedStartDate == null ? "N/A": CommonTools.dateFormat(projSchedInfo.schedStartDate)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=projSchedInfo.schedResponsible == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedResponsible)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=projSchedInfo.schedNote == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedNote)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
		</TR>
		
		<TR>
			<TD colspan="4" style='border-top:none;border-left:none;
						  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
						  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
						  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
				<p class=Bangheader style='page-break-after:auto; text-align:left'><b>
			<%
				switch(nextType){
					case 1:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DefectPreventionType")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QualityControlType")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ProjectTrackingType")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ConfigurationManagementType")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QAType")%><%break;
					case 6:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DARType")%><%break;
					default :%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.OtherType")%><%break;
				}
			%>
			</b></p></TD>
		</TR>
<%				
			} else {
%>			
		<TR>
		
		<td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=projSchedInfo.schedActivity == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedActivity)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			<td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=projSchedInfo.schedStartDate == null ? "N/A": CommonTools.dateFormat(projSchedInfo.schedStartDate)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=projSchedInfo.schedResponsible == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedResponsible)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=projSchedInfo.schedNote == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedNote)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
		
		</TR>
<%			
			}
		}// end for
		if (iSize > 0) {
			projSchedInfo = (ProjSchedInfo) vProjSchedList.elementAt(iSize-1);
			currentType = projSchedInfo.schedType;
			if (iSize == 1) {
%>
		<TR>
			<TD colspan="4" style='border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
				<p class=Bangheader style='page-break-after:auto; text-align:left'><b>
				<%
					switch(currentType){
						case 1:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DefectPreventionType")%><%break;
						case 2:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QualityControlType")%><%break;
						case 3:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ProjectTrackingType")%><%break;
						case 4:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.ConfigurationManagementType")%><%break;
						case 5:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.QAType")%><%break;
						case 6:%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.DARType")%><%break;
						default :%><%=languageChoose.getMessage("fi.jsp.plProjSchedList.OtherType")%><%break;
					}
				%>
				</b></p>
			</TD>
		</TR>

<%
			}
%> 
		<TR>
		
			<td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=projSchedInfo.schedActivity == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedActivity)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			<td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=projSchedInfo.schedStartDate == null ? "N/A": CommonTools.dateFormat(projSchedInfo.schedStartDate)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=projSchedInfo.schedResponsible == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedResponsible)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=projSchedInfo.schedNote == null ? "N/A": ConvertString.toHtml(projSchedInfo.schedNote)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
		
		</TR>
<%
		}	
%>		
	</TBODY>
</TABLE>
<BR/>

<%-- 3.4. Resource --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.4. Resource</b></p>
<p class=MsoNormal>Specified as in the section Project Team</p>

<%-- 3.5. Infratructure --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.5. Infratructure</b></p>
<TABLE border=1 cellspacing=0 cellpadding=0 width="598" style='margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
	<TR>
		<td bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.tool.WorkProduct")%></b></td>
		
	    <td bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.tool.Purpose")%></b></td>
	    <td bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.tool.ExpectedAvailabilityBy")%></b></td>
		<td bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.tool.Note")%></b></td>
	</TR>	
        <%
       
       	iSize = 0;
       	if (vtToolList != null )  iSize = vtToolList.size();
        String pur="";
        String note="";		
		
        ToolInfo toolInfo=null;
        for(i = 0; i < iSize-1; i++)
        {        	
  			pur="N/A";
  			note="N/A";  			
        	
        	toolInfo=(ToolInfo) vtToolList.get(i);
        	currentType = new Long(toolInfo.tool_type).intValue();
        	if (currentType > 8 || currentType < 6) currentType = 9;
		 	nextType = new Long(((ToolInfo) vtToolList.elementAt(i+1)).tool_type).intValue();
		 	if (nextType > 8 || nextType < 6) nextType = 9;		 	
		 	
        	if(toolInfo.purpose!=null){
        		pur=toolInfo.purpose;        		
        		if(pur.length()>50) pur=pur.substring(0,50)+"...";
        	}
        	if(toolInfo.note!=null){
        		note=toolInfo.note;
        		if(note.length()>50) note=note.substring(0,50)+"..."; 
        	}
        	if (i == 0) {
        %>
        <TD colspan="4" style='border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style='page-break-after:auto; text-align:left'><b>
			<%
				switch(currentType){
					case 6:%><%=languageChoose.getMessage("fi.jsp.tool.DevelopmentEnvironment")%><%break;
					case 7:%><%=languageChoose.getMessage("fi.jsp.tool.Hardware_N_Software")%><%break;
					case 8:%><%=languageChoose.getMessage("fi.jsp.tool.OtherTools")%><%break;
					case 9:%><%=languageChoose.getMessage("fi.jsp.tool.OldDataType")%><%break;
				}
			%>
			</b></p>
			</TD>
		</TR>
        <%	
			}
			if (currentType != nextType ) {
		%>
        <TR> 
        
        	<td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=ConvertString.toHtml(toolInfo.name)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			<td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=ConvertString.toHtml(pur)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=(toolInfo.expected_available_stage == null) ? "N/A": ConvertString.toHtml(toolInfo.expected_available_stage)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=ConvertString.toHtml(note)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
        
        </TR>
         <TD colspan="4" style='border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style='page-break-after:auto; text-align:left'><b>
			<%
				switch(nextType){
					case 6:%><%=languageChoose.getMessage("fi.jsp.tool.DevelopmentEnvironment")%><%break;
					case 7:%><%=languageChoose.getMessage("fi.jsp.tool.Hardware_N_Software")%><%break;
					case 8:%><%=languageChoose.getMessage("fi.jsp.tool.OtherTools")%><%break;
					case 9:%><%=languageChoose.getMessage("fi.jsp.tool.OldDataType")%><%break;
				}
			%>
			</b></p>
			</TD>
		</TR>
<%				
			} else {
				
%>			
		<TR>            
            <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=ConvertString.toHtml(toolInfo.name)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			<td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=ConvertString.toHtml(pur)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=(toolInfo.expected_available_stage == null) ? "N/A": ConvertString.toHtml(toolInfo.expected_available_stage)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=ConvertString.toHtml(note)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
        </TR>
<%			
			}
		}// end for
		if (iSize > 0) {
			toolInfo = (ToolInfo) vtToolList.elementAt(iSize-1);			
			if(toolInfo.purpose!=null){
				pur=toolInfo.purpose;        		
				if(pur.length()>50) pur=pur.substring(0,50)+"...";
	    	}
	    	if(toolInfo.note!=null){
	    		note=toolInfo.note;
	    		if(note.length()>50) note=note.substring(0,50)+"..."; 
	    	}
	    	if (iSize == 1) {
	    		currentType = new Long(toolInfo.tool_type).intValue();
	        	if (currentType > 8 || currentType < 6) currentType = 9;
%>
		  <TD colspan="4" style='border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style='page-break-after:auto; text-align:left'><b>
			<%
				switch(currentType){
					case 6:%><%=languageChoose.getMessage("fi.jsp.tool.DevelopmentEnvironment")%><%break;
					case 7:%><%=languageChoose.getMessage("fi.jsp.tool.Hardware_N_Software")%><%break;
					case 8:%><%=languageChoose.getMessage("fi.jsp.tool.OtherTools")%><%break;
					case 9:%><%=languageChoose.getMessage("fi.jsp.tool.OldDataType")%><%break;
				}
			%>
			</b></p>
			</TD>
		</TR>
<%
 			} 		
%>
		<TR>
            <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=ConvertString.toHtml(toolInfo.name)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			<td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=ConvertString.toHtml(pur)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=(toolInfo.expected_available_stage == null) ? "N/A": ConvertString.toHtml(toolInfo.expected_available_stage)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
			 
			 <td width=50 style='width:50pt;border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  padding:0in 5.4pt 0in 5.4pt;height:25.85pt'>
			  <p class=Bang0 align=left style='margin-top:4.0pt;margin-right:0in;
			  margin-bottom:4.0pt;margin-left:0in;text-align:left'>
			  <%=ConvertString.toHtml(note)%>
			  <span style='mso-bidi-font-family:Tahoma'><o:p></o:p></span></p>
			 </td>
        </TR>
<%
		} 
%> 
</TABLE>
<br>

<%-- 3.6. Training Plan --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.6. Training Plan</b></p>

<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Training Area</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Participants</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>When, Duration</b></td>
		<td bgcolor = "#C0C0C0"; style="width:143px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Waiver Criteria</b></td>
	</tr>
	
	<%
       	for(i=0;i<vt.size();i++)
       	{
       		TrainingInfo trainInfo=(TrainingInfo)vt.get(i);
  			String dur=(trainInfo.duration != null) ? trainInfo.duration : "";
  			if(dur.length()>50) dur=dur.substring(0,50)+"...";
        %>
		
	<tr>
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=(trainInfo.topic != null) ? ConvertString.toHtml(trainInfo.topic) : "" %></td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=(trainInfo.participant != null) ? ConvertString.toHtml(trainInfo.participant) : "" %></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=ConvertString.toHtml(dur)%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=(trainInfo.waiver != null) ? ConvertString.toHtml(trainInfo.waiver) : ""%></td>
	</tr>
	<%
	}%>
</table>
<br/>

<%-- 3.7. Finance --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.7. Finance</b></p>
<p class=MsoNormal><i>	(Refer to excel file)</i></p>

<br/>
<%}%>


<%
if(ProjectOrganization.equals("true")){
	exportOrder++;
%>

<%-- 4.	PROJECT ORGANIZATION --%>
<h1 style="text-indent: -27.35pt; line-height: normal; page-break-before: always; page-break-after: auto; text-autospace: ideograph-numeric ideograph-other; margin-left: 27.35pt; margin-right: 0in; margin-top: .25in; margin-bottom: 6.0pt">
<b>
<span style="font-size: 12.0pt; font-family: Arial; color: #000000; text-transform: uppercase; letter-spacing: 0pt">
<%=exportOrder%>. <span style="font:7.0pt &quot;Times New Roman&quot;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>
PROJECT ORGANIZATION</span></b></h1>

<%-- 4.1. Organization Structure --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.1. Organization Structure </b></p>
<p class=MsoNormal>
<%=((orgStructure==null || orgStructure.equals(""))? "N/A" : ConvertString.toHtml(orgStructure))%>	
</p>

<%-- 4.2. Project Team --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.2. Project Team </b></p>
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
  	<p class=bang><%=languageChoose.getMessage("fi.jsp.WO.ProjectCommunicator")%></p>
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

<%
for (j = 0; j < subTeamSize; j++) {
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
  	<p class=bang><%=ConvertString.breakString(CommonTools.dateFormat(info.endDate),6)%></p>
  </td>
  
 </tr>
<%
	}
}
%>
</table>
<br/>
<p class=MsoNormal>	The detail of Human resource budget allocation over the whole project life: <i>Refer to excel file</i></p>

<%-- 4.3. External Interfaces --%>
<p class="sectiontitlePP"><b><%=exportOrder%>.3. External Interfaces </b></p>
<br>
<%-- 4.3.1.	Fsoft Interfaces --%>
<p class="sectiontitlePP1"><b><%=exportOrder%>.3.1. Fsoft Interfaces </b></p>
<table border=1 cellspacing=0 cellpadding=0 width = "598" style='margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 		<td width ="24" bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b>#</b></td>
		<td width ="116" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Function</b></td>
		<td width ="129" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Contact Person<br/>(name, position)</b></td>
		<td width ="129" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Contact address<br/>(email, telephone)</b></td>
		<td width ="200" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Responsibility</b></td>
	</tr>
<%
    if (vIList != null) {
    	iSize = vIList[0].size();    	
		for (i = 0; i < iSize ; i++) {			
			InterfaceInfo interfaceInfo = (InterfaceInfo) vIList[0].elementAt(i);			
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
		<p class="Bang"><%=(i+1)%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.function == null ? "N/A": interfaceInfo.function%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.contactPerson == null ? "N/A": interfaceInfo.contactPerson%></td>
		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.contact == null ? "N/A": interfaceInfo.contact%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.responsibility == null ? "N/A": interfaceInfo.responsibility%></td>
	</tr>
<%
		}
	}	
%>
</table>
<br/>

<%-- 4.3.2.	Customer's Interfaces --%>
<p class="sectiontitlePP1"><b><%=exportOrder%>.3.2. Customer's Interfaces </b></p>
<table border=1 cellspacing=0 cellpadding=0 width = "598" style='margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 		<td width = "24" bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b>#</b></td>
		<td width = "116" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Department</b></td>
		<td width = "129" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Contact Person<br>(name, position)</b></td>
		<td width = "129" bgcolor = "#C0C0C0"; style="width:129px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Contact address<br>(email, telephone)</b></td>
		<td width = "200" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Responsibility</b></td>
	</tr>
<%
    if (vIList != null) {
    	iSize = vIList[1].size();    	
		for (i = 0; i < iSize ; i++) {			
			InterfaceInfo interfaceInfo = (InterfaceInfo) vIList[1].elementAt(i);			
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
		<p class="Bang"><%=(i+1)%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.department == null ? "N/A": interfaceInfo.department%></td>
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.contactPerson == null ? "N/A": interfaceInfo.contactPerson%></td>
		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.contact == null ? "N/A": interfaceInfo.contact%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.responsibility == null ? "N/A": interfaceInfo.responsibility%></td>
	</tr>
<%
		}
	}	
%>
</table>
<br/>


<%-- 4.3.3.	Other Projects  --%>
<p class="sectiontitlePP1"><b><%=exportOrder%>.3.3. Other Projects </b></p>
<table border=1 cellspacing=0 cellpadding=0 width = "598" style='margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 		<td width = "24" bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b>#</b></td>
		<td width = "116" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Project</b></td>
		<td width = "129" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Contact Person<br>(name, position)</b></td>
		<td width = "129" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Contact address<br>(email, telephone)</b></td>
		<td width = "200" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Dependency</b></td>
	</tr>
<%
    if (vIList != null) {
    	iSize = vIList[2].size();    	
		for (i = 0; i < iSize ; i++) {			
			InterfaceInfo interfaceInfo = (InterfaceInfo) vIList[2].elementAt(i);			
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td width = "24" style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
		<p class="Bang"><%=(i+1)%></td>
		<td width = "116" style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.otherProjName == null ? "N/A": interfaceInfo.otherProjName%></td>
		<td width = "129" style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.contactPerson == null ? "N/A": interfaceInfo.contactPerson%></td>
		<td width = "129" style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.contact == null ? "N/A": interfaceInfo.contact%></td>
		<td width = "200" style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=interfaceInfo.dependency == null ? "N/A": interfaceInfo.dependency%></td>
	</tr>
<%
		}
	}	
%>
</table>
<br/>

<%-- 4.3.4.	Supplier & Sub-Contractor  --%>
<p class="sectiontitlePP1"><b><%=exportOrder%>.3.4. Supplier & Sub-Contractor </b></p>
<table border=1 cellspacing=0 cellpadding=0 width = "598" style='margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 		<td width = "24" bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b>#</b></td>
		<td width = "116" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Work/Product</b></td>		
		<td width = "116" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Supplier/Sub-<br>contractor's name</b></td>
		<td width = "129" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Contact person<br>(email, telephone)</b></td>
		<td width = "129" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Expected delivery <br>date</b></td>
		<td width = "200" bgcolor = "#C0C0C0"; style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader><b>Ref. to contract</b></td>
	</tr>
<%
    if (subcontractList != null) {
    	iSize = subcontractList.size();    	
		for (i = 0; i < iSize ; i++) {			
			SubcontractInfo subInfo = (SubcontractInfo) subcontractList.elementAt(i);			
			className = (i % 2 == 0) ? "CellBGRnews" : "CellBGR3";
%>
	<tr class="<%=className%>">
		<td width = "24" style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
		<p class="Bang"><%=(i+1)%></td>
		<td width = "116" style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=subInfo.deliverable == null ? "N/A": subInfo.deliverable%></td>
		<td width = "129" style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=subInfo.sName == null ? "N/A": subInfo.sName%></td>
		<td width = "129" style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=subInfo.contactP == null ? "N/A": subInfo.contactP%></td>
		<td width = "129" style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%= subInfo.plannedDeliveryDate == null ? "N/A": CommonTools.dateFormat(subInfo.plannedDeliveryDate)%></td>
		<td width = "200" style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=subInfo.refToContract == null ? "N/A": subInfo.refToContract%></td>
	</tr>
<%
		}
	}	
%>
</table>
<br/>
<% 
	}
%>


<%
if(Communication_N_Reporting.equals("true")){
	exportOrder++;
%>
<%-- 5.	COMMUNICATION & REPORTING --%>
<h1 style="text-indent: -27.35pt; line-height: normal; page-break-before: always; page-break-after: auto; text-autospace: ideograph-numeric ideograph-other; margin-left: 27.35pt; margin-right: 0in; margin-top: .25in; margin-bottom: 6.0pt">
<b>
<span style="font-size: 12.0pt; font-family: Arial; color: #000000; text-transform: uppercase; letter-spacing: 0pt">
<%=exportOrder%>.<span style="font:7.0pt &quot;Times New Roman&quot;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
 COMMUNICATION & REPORTING</span></b></h1>
<BR/>
<p/>
<TABLE border=1 cellspacing=0 cellpadding=0 width = "598" style='margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>			
	<TR class="ColumnLabel">		
		<td  bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.plComReportList.CommunicationType")%></b></td>		
		<td  bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.plComReportList.MethodTool")%></b></td>		
		<td  bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.plComReportList.When")%></b></td>		
		<td bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader><b><%=languageChoose.getMessage("fi.jsp.plComReportList.Information")%></b></td>		
		<td bgcolor = "#C0C0C0"; style='border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader>
			<b>
				<%=languageChoose.getMessage("fi.jsp.plComReportList.Participants")%> /<BR>
				<%=languageChoose.getMessage("fi.jsp.plComReportList.Responsible")%>
			</b>
		</td>
	</TR>		
<%		
		ComReportInfo comReportInfo;		
		int currentType = 0;
		int nextType = 0;		
		iSize = 0;
		if (vComReportList != null) iSize = vComReportList.size();
		
		for(i = 0; i < iSize-1; i++){
			comReportInfo = (ComReportInfo) vComReportList.elementAt(i);		 	
		 	currentType = comReportInfo.comParentType;		 	
		 	nextType = ((ComReportInfo) vComReportList.elementAt(i+1)).comParentType;		 	
		 	if (i == 0) {
%>		
		<TR>
			<TD colspan="4" style='border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style='page-break-after:auto; text-align:left'><b>
			<%
				switch(currentType){
					case 1:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTaskTrackingType")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectMeetingType")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTrackingType")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CustomerCommunicationReportingType")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CommunicationWithSeniorManagementType")%><%break;
					case 6:%><%=languageChoose.getMessage("fi.jsp.plComReportList.OtherCommunicationAndReportingType")%><%break;
				}
			%>
			</b></p>
			</TD>
		</TR>
		<%	
			}  
			if (currentType != nextType ) {
		%>
		<TR>			
			<td  style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comType == null ? "N/A": ConvertString.toHtml(comReportInfo.comType)%></td>			
			<td  style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comMethodTool == null ? "N/A": comReportInfo.comMethodTool%></td>			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comWhen == null ? "N/A": ConvertString.toHtml(comReportInfo.comWhen)%></td>			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comInfo == null ? "N/A": ConvertString.toHtml(comReportInfo.comInfo)%></td>			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comResp == null ? "N/A": ConvertString.toHtml(comReportInfo.comResp)%></td>
		</TR>
		<TR>
			<TD colspan="4" style='border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style='page-break-after:auto; text-align:left'><b>
			<%
				switch(nextType){
					case 1:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTaskTrackingType")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectMeetingType")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTrackingType")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CustomerCommunicationReportingType")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CommunicationWithSeniorManagementType")%><%break;
					case 6:%><%=languageChoose.getMessage("fi.jsp.plComReportList.OtherCommunicationAndReportingType")%><%break;
				}
			%>
			</b></p>
			</TD>
		</TR>
<%				
			} else {				
%>			
		<TR>			
			<td  style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comType == null ? "N/A": ConvertString.toHtml(comReportInfo.comType)%></td>			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comMethodTool == null ? "N/A": comReportInfo.comMethodTool%></td>			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comWhen == null ? "N/A": ConvertString.toHtml(comReportInfo.comWhen)%></td>			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comInfo == null ? "N/A": ConvertString.toHtml(comReportInfo.comInfo)%></td>			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comResp == null ? "N/A": ConvertString.toHtml(comReportInfo.comResp)%></td>	
		</TR>
<%			
			}
		}// end for
		if (iSize > 0) {			
			comReportInfo = (ComReportInfo) vComReportList.elementAt(iSize-1);
			currentType = comReportInfo.comParentType;			
			if (iSize == 1) {
%>
		<TR>
			<TD colspan="4" style='border-top:none;border-left:none;
			  border-bottom:solid windowtext .75pt;border-right:solid windowtext .75pt;
			  mso-border-top-alt:solid windowtext .75pt;mso-border-left-alt:solid windowtext .75pt;
			  background:transparent;padding:0in 5.4pt 0in 5.4pt'>
			<p class=Bangheader style='page-break-after:auto; text-align:left'><b>
			<%
				switch(currentType){
					case 1:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTaskTrackingType")%><%break;
					case 2:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectMeetingType")%><%break;
					case 3:%><%=languageChoose.getMessage("fi.jsp.plComReportList.ProjectTrackingType")%><%break;
					case 4:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CustomerCommunicationReportingType")%><%break;
					case 5:%><%=languageChoose.getMessage("fi.jsp.plComReportList.CommunicationWithSeniorManagementType")%><%break;
					case 6:%><%=languageChoose.getMessage("fi.jsp.plComReportList.OtherCommunicationAndReportingType")%><%break;
				}
			%>
			</b></p>
			</TD>
		</TR>
<%
			}
%>		
		<TR>			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comType == null ? "N/A": ConvertString.toHtml(comReportInfo.comType)%></td>
			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comMethodTool == null ? "N/A": comReportInfo.comMethodTool%></td>
			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comWhen == null ? "N/A": ConvertString.toHtml(comReportInfo.comWhen)%></td>
			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comInfo == null ? "N/A": ConvertString.toHtml(comReportInfo.comInfo)%></td>
			
			<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
			<p class="Bang"><%=comReportInfo.comResp == null ? "N/A": ConvertString.toHtml(comReportInfo.comResp)%></td>	
		</TR>
<%
		}
%>	
</TABLE>
<BR/>
<%}%>

<% exportOrder++; 
String ref = "<Refer to the CM plan or insert here the contents of the CM plan as appropriated>"; %>
<%-- 6.	CONFIGURATION MANAGEMENT --%>
<h1 style="text-indent: -27.35pt; line-height: normal; page-break-before: always; page-break-after: auto; text-autospace: ideograph-numeric ideograph-other; margin-left: 27.35pt; margin-right: 0in; margin-top: .25in; margin-bottom: 6.0pt">
<b>
<span style="font-size: 12.0pt; font-family: Arial; color: #000000; text-transform: uppercase; letter-spacing: 0pt">
<%=exportOrder%>.<span style="font:7.0pt &quot;Times New Roman&quot;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
 CONFIGURATION MANAGEMENT</span></b></h1>
<p class=MsoNormal><%=ConvertString.toHtml(ref)%></p>
<BR/>
<%
if(SecurityAspects.equals("true")){
	exportOrder++;
%>

<%-- 7.	SECURITY ASPECTS --%>
<h1 style="text-indent: -27.35pt; line-height: normal; page-break-before: always; page-break-after: auto; text-autospace: ideograph-numeric ideograph-other; margin-left: 27.35pt; margin-right: 0in; margin-top: .25in; margin-bottom: 6.0pt">
<b>
<span style="font-size: 12.0pt; font-family: Arial; color: #000000; text-transform: uppercase; letter-spacing: 0pt">
<%=exportOrder%>.<span style="font:7.0pt &quot;Times New Roman&quot;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
 SECURITY ASPECTS</span></b></h1>
 <p class=MsoNormal>N/A</p>
<BR/>
<%}%>

<p class="sectiontitlePP2"><b>REFERENCES</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 	<tr>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>#</b></td>
		<td bgcolor = "#C0C0C0"; style="width:120px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Reference item</b></td>
		<td bgcolor = "#C0C0C0"; style="width:120px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Issued date</b></td>
		<td bgcolor = "#C0C0C0"; style='width:120px;border:solid windowtext .75pt; padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Source</b></td>
		<td bgcolor = "#C0C0C0"; style='width:82px;border:solid windowtext .75pt; padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Note</b></td>		
	</tr>
<%
    if (refList != null) {
    	iSize = refList.size();
		for (i = 0; i < iSize ; i++) {
			ReferenceInfo refInfo = (ReferenceInfo) refList.elementAt(i);			
%>
	<tr>
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in" align="center">
		<p class="Bang"><%=i+1%></td>		
		<td style="height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=refInfo.document == null ? "N/A": refInfo.document%></td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=refInfo.issueDate == null ? "N/A": CommonTools.dateFormat(refInfo.issueDate)%></td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=refInfo.source == null ? "N/A": refInfo.source%></td>
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"><%=refInfo.note == null ? "N/A": refInfo.note%></td>	
	</tr>
<%
		}
	}	
%>
</table>
<br/>

<p class="sectiontitlePP2"><b>DEFINITIONS AND ACRONYMS</b></p>
<table border=1 cellspacing=0 cellpadding=0 style='width:598px; margin-left:5.4pt;
 border-collapse:collapse;border:none;mso-border-alt:solid black .5pt;
 mso-padding-alt:0in 5.4pt 0in 5.4pt'>
 	<tr>
 		<td bgcolor = "#C0C0C0"; style='width:26px;border:solid windowtext .75pt;padding:0in 5.4pt 0in 5.4pt'>
		<p class=Bangheader style="page-break-after:auto"><b>Acronym</b></td>
		<td bgcolor = "#C0C0C0"; style="width:120px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Definition</b></td>
		<td bgcolor = "#C0C0C0"; style="width:120px; height: 25.85pt; border-left: medium none; border-right: 1.0pt solid windowtext; border-top: 1.0pt solid windowtext; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in;">
		<p class=Bangheader style="page-break-after:auto"><b>Note</b></td>			
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> PM</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Project Manager</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> PTL</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Project Technical Leader</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> QA</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Quality Assurance Officer</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> CC</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Infrastructure Configuration Controller</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> DV</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Developer</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> URD</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> User Requirement Document</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> SRS</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Software Requirement Specification</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> ADD</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Architecture Design Document</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> DDD</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Detail Design Document</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> TP</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Test Plan</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> TC</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Test Case</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> SC</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Source Code</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> CM</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Configuration Management</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> CSCI</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Computer Software Configuration Items</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> CI</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Configuration Item</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
	<tr>		
		<td style="height: 25.85pt; border-left: 1.0pt solid windowtext; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> CCB</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> Change Control Board</td>		
		<td style="border-left: medium none; border-right: 1.0pt solid windowtext; border-top: medium none; border-bottom: 1.0pt solid windowtext; padding-left: 5.4pt; padding-right: 5.4pt; padding-top: 0in; padding-bottom: 0in">
		<p class="Bang"> &nbsp; </td>		
	</tr>
</table>

</div>
</body>
</html>