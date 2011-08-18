/*
 * Copyright (c) 2009, FPT Software JSC
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *	- Neither the name of the FPT Software JSC nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 package com.fms1.common.group;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fms1.common.*;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.*;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.Parameters;

import java.sql.Date;
/**
 * Page logic of PCB report
 * @author manu
 * @date Jul 17, 2003
 */
public class PCBCaller {
	public static final void pcbLoadSearchPage(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			Vector pcbList = PCB.getPCBs(workUnitID);
			session.setAttribute("pcbList", pcbList);
			Fms1Servlet.callPage("Group/pcbReport.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void pcbCreate(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			PCBGalInfo pcbGalInfo = new PCBGalInfo();
			pcbGalInfo.year = Integer.parseInt(request.getParameter("txtYear"));
			pcbGalInfo.period = request.getParameter("txtPeriod");
			pcbGalInfo.reportName = request.getParameter("txtReportName");
			UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
			pcbGalInfo.author = userLoginInfo.Name;
			pcbGalInfo.workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			
			String[] strSoftwareChkbox = (String[]) request.getParameterValues("chkSoftware");
			//temp
			String[] strSQAChkbox = (String[])request.getParameterValues("chkSQAGroup");
			pcbGalInfo.authorID = userLoginInfo.developerID;
			pcbGalInfo.reportDate = new Date(new java.util.Date().getTime());
			String reportType = request.getParameter("txtType");
			if (reportType == null)
				pcbGalInfo.setReportType(PCB.REPORT_ALL);
			else if (reportType.equals("cus"))
				pcbGalInfo.setReportType(PCB.REPORT_CUSTOM);
			else if (reportType.equals("all"))
				pcbGalInfo.setReportType(PCB.REPORT_ALL);
			else if (reportType.equals("dev"))
				pcbGalInfo.setReportType(PCB.REPORT_DEVELOPMENT);
			else if (reportType.equals("mai"))
				pcbGalInfo.setReportType(PCB.REPORT_MAINTENANCE);
			else if (reportType.equals("oth"))
				pcbGalInfo.setReportType(PCB.REPORT_OTHER);

			//check if this report already exists
			//note: empty strings are converted to null by oracle, so for easyer comparison:
			if ("".equals(pcbGalInfo.reportName))
				pcbGalInfo.reportName = null;
			Vector pcbList = (Vector) session.getAttribute("pcbList");
			PCBGalInfo pcbInfoExisting;// = (PCBGalInfo) pcbList.elementAt(0);
			for (int i = 0; i < pcbList.size(); i++) {
				pcbInfoExisting = (PCBGalInfo) pcbList.elementAt(i);
				if ((pcbInfoExisting.year == pcbGalInfo.year)
					&& pcbInfoExisting.period.equals(pcbGalInfo.period)
					&& (pcbInfoExisting.reportType == pcbGalInfo.reportType)
					&& ((pcbInfoExisting.reportName == pcbGalInfo.reportName)
						|| ((pcbInfoExisting.reportName != null && pcbGalInfo.reportName != null)
							&& (pcbInfoExisting.reportName.equals(pcbGalInfo.reportName))))) {
					Fms1Servlet.callPage(
						"Group/pcbReport.jsp?error=Please choose another name, a report with the same year, period, type and name already exists",request,response);
					return;
				}
			}
			String [] theArray=pcbGalInfo.workUnitID==Parameters.SQA_WU ?strSQAChkbox:strSoftwareChkbox;
			pcbGalInfo.metricIDs = new int[theArray.length];
			for (int i = 0; i < theArray.length; i++)
				pcbGalInfo.metricIDs[i] = Integer.parseInt(theArray[i]);
			Date[] dateArray = PCB.getDatesFromPeriod(pcbGalInfo.period, pcbGalInfo.year);
			pcbGalInfo.startDate = dateArray[0];
			pcbGalInfo.endDate = dateArray[1];
			//get all the closed projects in the period
			Vector prjInfList =
				Project.getChildProjectsByWU(
					Parameters.FSOFT_WU,
					pcbGalInfo.startDate,
					pcbGalInfo.endDate,
					Project.CLOSEDPROJECTS);
			Vector prevPrjInfList = null;
			ProjectInfo projectInfo;
			Vector projectList, prevProjectList;
			Date[] prevDateArray = PCB.getDatesFromPrevPeriod(pcbGalInfo.period, pcbGalInfo.year);
			Date prevStartDate = prevDateArray[0];
			Date prevEndDate = prevDateArray[1];
			//Pick the good projects (depending on report type)
			if (pcbGalInfo.reportType == PCB.REPORT_CUSTOM) {
				projectList = new Vector();
				String strProjectIDs = request.getParameter("projectIDs");
				java.util.StringTokenizer st = new java.util.StringTokenizer(strProjectIDs, ",");
				pcbGalInfo.projectIDs = new long[st.countTokens()];
				int arrayID = 0;
				while (st.hasMoreTokens()) {
					pcbGalInfo.projectIDs[arrayID] = Long.parseLong(st.nextToken());
					for (int i = 0; i < prjInfList.size(); i++) {
						projectInfo = (ProjectInfo) prjInfList.elementAt(i);
						if (projectInfo.getProjectId() == pcbGalInfo.projectIDs[arrayID]) {
							projectList.add(projectInfo);
							break;
						}
					}
					arrayID++;
				}
				prevProjectList = projectList;
			}
			else {
				prevPrjInfList =
					Project.getChildProjectsByWU(
						Parameters.FSOFT_WU,
						prevStartDate,
						prevEndDate,
						Project.CLOSEDPROJECTS);
				projectList = pcbFilterProjects(pcbGalInfo.reportType, prjInfList);
				prevProjectList = pcbFilterProjects(pcbGalInfo.reportType, prevPrjInfList);
			}
			if (projectList.size() == 0) {
				Fms1Servlet.callPage("Group/pcbReport.jsp?error=Sorry, no project matches your criteria",request,response);
				return;
			}
			Vector pcbMetrics = PCB.getPCBMetricGroups(pcbGalInfo.workUnitID,pcbGalInfo.lifecycleID,pcbGalInfo.metricIDs, projectList, pcbGalInfo.endDate);
			if (prevProjectList.size() > 0) {
				Vector prevPcbMetrics = PCB.getPCBMetricGroups(pcbGalInfo.workUnitID,pcbGalInfo.lifecycleID,pcbGalInfo.metricIDs, prevProjectList, prevEndDate);
				//merge the 2 vectors
				PCBMetricInfo metricInfo, prevMetricInfo;
				for (int i = 0; i < pcbMetrics.size(); i++) {
					metricInfo = (PCBMetricInfo) pcbMetrics.elementAt(i);
					prevMetricInfo = (PCBMetricInfo) prevPcbMetrics.elementAt(i);
					metricInfo.prevLCL = prevMetricInfo.LCL;
					metricInfo.prevUCL = prevMetricInfo.UCL;
					metricInfo.prevAvg = prevMetricInfo.actualAvg;
				}
			}
			pcbGalInfo.projectCodes = new String[projectList.size()];
			//for dbase update we need the array of projectIDs
			//we might override the array defined if custom report, but they should be the same
			pcbGalInfo.projectIDs = new long[projectList.size()];
			for (int k = 0; k < projectList.size(); k++) {
				projectInfo = (ProjectInfo) projectList.elementAt(k);
				pcbGalInfo.projectCodes[k] = projectInfo.getProjectCode();
				pcbGalInfo.projectIDs[k] = projectInfo.getProjectId();
			}
			pcbGalInfo.metrics = new String[1];
			pcbGalInfo.metrics[0] =pcbGalInfo.workUnitID==Parameters.FSOFT_WU? "Software: ":"SQA: ";
			
			for (int i = 0; i < pcbGalInfo.metricIDs.length; i++) {
				//get metric group names
				pcbGalInfo.metrics[0] += Metrics.getMetricGroup(pcbGalInfo.metricIDs[i]).groupName
					+ ((i == pcbGalInfo.metricIDs.length - 1) ? "" : ", ");
			}
			pcbGalInfo.pcbID = PCB.createPCB(pcbGalInfo);
			//we must save the metric selection in the DB in case the user does not press save button
			PCB.updatePCBMetrics(pcbGalInfo, pcbMetrics);
			session.setAttribute("pcbGalInfo", pcbGalInfo);
			session.setAttribute("pcbMetrics", pcbMetrics);
			Fms1Servlet.callPage("Group/pcbInformation.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * PCB VIEW
	 * Improvements are welcome, but be carefull !!!
	 *
	 */
	public static final void pcbView(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int vtID = Integer.parseInt(request.getParameter("vtID"));
			Vector pcbList = (Vector) session.getAttribute("pcbList");
			PCBGalInfo pcbGalInfo = (PCBGalInfo) pcbList.elementAt(vtID);
			Date[] dateArray = PCB.getDatesFromPeriod(pcbGalInfo.period, pcbGalInfo.year);
			pcbGalInfo.startDate = dateArray[0];
			pcbGalInfo.endDate = dateArray[1];
			//get all the closed project in the period
			Vector prjInfList =
				Project.getChildProjectsByWU(
					Parameters.FSOFT_WU,
					pcbGalInfo.startDate,
					pcbGalInfo.endDate,
					Project.CLOSEDPROJECTS);
			Vector prevPrjInfList = null;
			ProjectInfo projectInfo;
			Vector projectList, prevProjectList;
			Date[] prevDateArray = PCB.getDatesFromPrevPeriod(pcbGalInfo.period, pcbGalInfo.year);
			Date prevStartDate = prevDateArray[0];
			Date prevEndDate = prevDateArray[1];
			//Pick the good projects (depending on report type)
			if (pcbGalInfo.reportType == PCB.REPORT_CUSTOM) {
				projectList = new Vector();
				Long projectID;
				Vector projectIDs = PCB.getCustomProjectIDs(pcbGalInfo.pcbID);
				for (int j = 0; j < projectIDs.size(); j++) {
					projectID = (Long) projectIDs.elementAt(j);
					for (int i = 0; i < prjInfList.size(); i++) {
						ProjectInfo projectInfoTemp = (ProjectInfo) prjInfList.elementAt(i);
						if (projectInfoTemp.getProjectId() == projectID.longValue()){
							projectList.add(projectInfoTemp);
							break;
						}
					}
				}
				prevProjectList = projectList;
			}
			else {
				prevPrjInfList =
					Project.getChildProjectsByWU(
						Parameters.FSOFT_WU,
						prevStartDate,
						prevEndDate,
						Project.CLOSEDPROJECTS);
				projectList = pcbFilterProjects(pcbGalInfo.reportType, prjInfList);
				prevProjectList = pcbFilterProjects(pcbGalInfo.reportType, prevPrjInfList);
			}
			if (projectList.size() == 0) {
				Fms1Servlet.callPage(
					"Group/pcbReport.jsp?error=Sorry, no project matches your criteria, because some projects have been deleted since the report was created",request,response);
				return;
			}
			// get metrics from DB
			Vector metricList = PCB.getPCBMetricList(pcbGalInfo);
			pcbGalInfo.metricIDs =PCB.loadPCBMetricGroups(pcbGalInfo.pcbID);
			Vector pcbMetrics = PCB.getPCBMetricGroups(pcbGalInfo.workUnitID,pcbGalInfo.lifecycleID,pcbGalInfo.metricIDs, projectList, pcbGalInfo.endDate);
			//merge the metrics saved in DB with the metrics dynamically calculated
			for (int i = 0; i < pcbMetrics.size(); i++) {
				PCBMetricInfo pcbMetricInfo = (PCBMetricInfo) pcbMetrics.elementAt(i);
				for (int j = 0; j < metricList.size(); j++) {
					PCBMetricInfo metricInfo = (PCBMetricInfo) metricList.elementAt(j);
					if (metricInfo.metricID.equals(pcbMetricInfo.metricID)){
						pcbMetricInfo.analysis=metricInfo.analysis;
						pcbMetricInfo.suggestion=metricInfo.suggestion;
					}
				}
			}
			if (prevProjectList.size() > 0) {
				Vector prevPcbMetrics = PCB.getPCBMetricGroups(pcbGalInfo.workUnitID,pcbGalInfo.lifecycleID,pcbGalInfo.metricIDs, prevProjectList, prevEndDate);
				//merge the 2 vectors
				PCBMetricInfo metricInfo, prevMetricInfo;
				for (int i = 0; i < pcbMetrics.size(); i++) {
					metricInfo = (PCBMetricInfo) pcbMetrics.elementAt(i);
					prevMetricInfo = (PCBMetricInfo) prevPcbMetrics.elementAt(i);
					metricInfo.prevLCL = prevMetricInfo.LCL;
					metricInfo.prevUCL = prevMetricInfo.UCL;
					metricInfo.prevAvg = prevMetricInfo.actualAvg;
				}
			}
			pcbGalInfo.projectCodes = new String[projectList.size()];
			//for dbase update we need the array of projectIDs
			//we might override the array defined if custom report, but they should be the same
			pcbGalInfo.projectIDs = new long[projectList.size()];
			for (int k = 0; k < projectList.size(); k++) {
				projectInfo = (ProjectInfo) projectList.elementAt(k);
				pcbGalInfo.projectCodes[k] = projectInfo.getProjectCode();
				pcbGalInfo.projectIDs[k] = projectInfo.getProjectId();
			}
			/*the pcbMetrics will be updated with project comment, carefull to set the
			 * pcbGalInfo.projectIDs array before calling this function
			 */
			PCB.loadProjectComments(pcbGalInfo,pcbMetrics);
			pcbGalInfo.metrics = new String[1];
			pcbGalInfo.metrics[0] =pcbGalInfo.workUnitID==Parameters.FSOFT_WU? "Software: ":"SQA: ";
			for (int i = 0; i < pcbGalInfo.metricIDs.length; i++) {
				//get String of children metrics for each metric group
				pcbGalInfo.metrics[0] += Metrics.getMetricGroup(pcbGalInfo.metricIDs[i]).groupName
					+ ((i == pcbGalInfo.metricIDs.length - 1) ? "" : ", ");
			}
			session.setAttribute("pcbGalInfo", pcbGalInfo);
			session.setAttribute("pcbMetrics", pcbMetrics);
			Fms1Servlet.callPage("Group/pcbInformation.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final Vector pcbFilterProjects(int prjType, Vector prjInfList) {
		Vector projectList = new Vector();
		ProjectInfo projectInfo;
		if (prjType == PCB.REPORT_ALL)
			projectList = prjInfList;
		else {
			//the report constants excepted ALL and custom are already mapped with lifecycleIDs
			for (int i = 0; i < prjInfList.size(); i++) {
				projectInfo = (ProjectInfo) prjInfList.elementAt(i);
				if ((projectInfo.getLifecycleId() == prjType)) {
					projectList.add(projectInfo);
				}
			}
		}
		return projectList;
	}
	/**
	 * called with the popup for custom PCBs
	 */
	public static final void pcbSelectProject(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int year = Integer.parseInt(request.getParameter("year"));
			String period = request.getParameter("period");
			String wuID = (String) session.getAttribute("workUnitID");
			Date[] dateArray = PCB.getDatesFromPeriod(period, year);
			Date startDate = dateArray[0];
			Date endDate = dateArray[1];
			//add date and lifecycle constraints
			Vector prjInfList =
				Project.getChildProjectsByWU(Long.parseLong(wuID), startDate, endDate, Project.CLOSEDPROJECTS);
			Vector prjInfList2 = new Vector();
			for (int i = 0; i < prjInfList.size(); i++) {
				ProjectInfo projectInfo = (ProjectInfo) prjInfList.elementAt(i);
				if ((projectInfo.getStartDate() != null)
					&& projectInfo.getStartDate().before(endDate)
					&& (projectInfo.getActualFinishDate() == null || projectInfo.getActualFinishDate().after(startDate))) {
					prjInfList2.add(projectInfo);
				}
			}
			session.setAttribute("PCBProjectList", prjInfList2);
			Fms1Servlet.callPage("Group/pcbSelectProject.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void pcbSaveGalInfo(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			String methodology = request.getParameter("txtMethodology");
			PCBGalInfo pcbGalInfo = (PCBGalInfo) session.getAttribute("pcbGalInfo");
			pcbGalInfo.methodology = methodology;
			PCB.updatePCB(pcbGalInfo);
			Fms1Servlet.callPage("Group/pcbInformation.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void pcbSaveDetails(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			PCBGalInfo pcbGalInfo = (PCBGalInfo) session.getAttribute("pcbGalInfo");
			Vector metricList = (Vector) session.getAttribute("pcbMetrics");
			String suggestion = request.getParameter("txtSuggestion");
			int vtID = Integer.parseInt(request.getParameter("vtID"));
			PCBMetricInfo metricInfo = (PCBMetricInfo) metricList.elementAt(vtID);
			metricInfo.suggestion = suggestion;
			for (int i = 0; i < metricInfo.projectValues.length; i++) {
				metricInfo.projectComments[i] = request.getParameter("txtProject" + i);
			}
			PCB.updatePCBMetrics(pcbGalInfo, metricList);
			Fms1Servlet.callPage("Group/pcbDetail.jsp?vtID=" + vtID,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void pcbSaveComments(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			PCBGalInfo pcbGalInfo = (PCBGalInfo) session.getAttribute("pcbGalInfo");
			Vector metricList = (Vector) session.getAttribute("pcbMetrics");
			pcbGalInfo.galComment = request.getParameter("txtGeneral");
			//See pcbComments.jsp to understand the index
			int indexLCL = 0;
			int indexUCL = 0;
			for (int i = 0; i < metricList.size(); i++) {
				PCBMetricInfo metricInfo = (PCBMetricInfo) metricList.elementAt(i);
				if (metricInfo.actualAvg < metricInfo.normLCL) {
					metricInfo.analysis = request.getParameter("txtMetricLCL" + indexLCL);
					indexLCL++;
				}
				else if (metricInfo.actualAvg > metricInfo.normUCL) {
					metricInfo.analysis = request.getParameter("txtMetricUCL" + indexUCL);
					indexUCL++;
				}
			}
			PCB.updatePCB(pcbGalInfo);
			PCB.updatePCBMetrics(pcbGalInfo, metricList);
			Fms1Servlet.callPage("Group/pcbComments.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void pcbSaveConclusions(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			PCBGalInfo pcbGalInfo = (PCBGalInfo) session.getAttribute("pcbGalInfo");
			pcbGalInfo.lastProblemsReview = request.getParameter("txtLastProblemsReview");
			pcbGalInfo.lastSuggestionsReview = request.getParameter("txtLastSuggestionsReview");
			pcbGalInfo.problems = request.getParameter("txtProblems");
			pcbGalInfo.suggestions = request.getParameter("txtSuggestions");
			PCB.updatePCB(pcbGalInfo);
			Fms1Servlet.callPage("Group/pcbConclusion.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void pcbDelete(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			PCBGalInfo pcbGalInfo = (PCBGalInfo) session.getAttribute("pcbGalInfo");
			PCB.deletePCB(pcbGalInfo.pcbID);
			pcbLoadSearchPage(request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
