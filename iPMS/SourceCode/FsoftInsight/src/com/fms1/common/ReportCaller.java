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
 
 package com.fms1.common;

import java.text.*;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fms1.tools.*;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.*;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.Parameters;

/**
 * Weekly, milestone and Post-mortem reports pages.
 * TODO:Group points should be moved somewhere else
 *
 */

public final class ReportCaller implements com.fms1.web.Constants {

    public final static void weeklyReportCallerInit(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			session.removeAttribute("reportDate");
			weeklyReportCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void weeklyReportCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			final ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			final Date today = new Date();
			final Date projectActualStartDate =
				(projectInfo.getStartDate() != null) ? projectInfo.getStartDate() : today;
			final Date projectActualEndDate =
				(projectInfo.getActualFinishDate() != null) ? projectInfo.getActualFinishDate() : today;
			final Calendar cal = new GregorianCalendar();
			final Calendar cal1 = new GregorianCalendar();
			cal.setTime(projectActualStartDate);
			cal1.setTime(projectActualEndDate);
			// Reset calendar to midnight
			Calendar calTemp = null;
			for (int i = 0; i < 2; i++) {
				calTemp = (i == 0) ? cal : cal1;
				calTemp.set(Calendar.HOUR_OF_DAY, 0);
				calTemp.set(Calendar.MINUTE, 0);
				calTemp.set(Calendar.SECOND, 0);
				calTemp.set(Calendar.MILLISECOND, 0);
				while (calTemp.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    calTemp.add(Calendar.DAY_OF_WEEK, 1);
				}
			}
			session.setAttribute("firstMonday", cal.getTime());
			session.setAttribute("lastMonday", cal1.getTime());
			Date reportDate =
				(request.getParameter("reportDate") != null)
					? CommonTools.dateForm.parse(request.getParameter("reportDate"))
					: cal1.getTime();
			java.sql.Date sqlReportDate = new java.sql.Date(reportDate.getTime());
			// Comments of report
			String comments = Report.getReportComments(prjID, Report.WEEKLY_REPORT, sqlReportDate);
			if (comments == null) {
                comments = "";
			}
			session.setAttribute("wrComments", comments);
			final WeeklyReportInfo wrInfo = Report.getReportCommonInfo(projectInfo, sqlReportDate);
			// Remove requirements after report date
			int k = wrInfo.requirementList.size();
			while (k > 0) {
				final RequirementInfo reqInfo = (RequirementInfo) wrInfo.requirementList.elementAt(--k);
				if (reqInfo.committedDate != null) {
					final Date d = new Date();
					d.setTime(reqInfo.committedDate.getTime());
					if (reqInfo.committedDate.compareTo(reportDate) > 0 || reqInfo.type != 2) {
						wrInfo.requirementList.removeElementAt(k);
					}
				} else {
					if (reqInfo.type != 2) {
						wrInfo.requirementList.removeElementAt(k);
					}
				}
			}
			// Get previous week start date
			java.sql.Date prevMonday = new java.sql.Date(reportDate.getTime()-7*24*3600*1000);
			java.sql.Date sundayNight= new java.sql.Date(reportDate.getTime()-1);
			java.sql.Date nextSunday= new java.sql.Date(reportDate.getTime() +7*24*3600*1000 -1);
			Vector lastWeekTasks = Tasks.getClosedProjectTasks(prjID,prevMonday,sundayNight);
			Vector nextWeekTasks = Tasks.getOpenProjectTasks(prjID,nextSunday);
            Vector metricInfo = (Vector) Report.getReportMetrics(wrInfo, Report.WEEKLY_REPORT);            
			session.setAttribute("lastWeekTasks", lastWeekTasks);
			session.setAttribute("nextWeekTasks", nextWeekTasks);
			session.setAttribute("reqHdrInfo", wrInfo.requirementInfo);
			session.setAttribute("reportDate", reportDate);
			session.setAttribute("projectInfo", projectInfo);
			session.setAttribute("requirementList", wrInfo.requirementList);
			session.setAttribute("defectWeeklyInfo", wrInfo.defectWeeklyInfo);
            session.setAttribute("metricInfo", metricInfo);
			session.setAttribute("riskList", wrInfo.riskList);
			session.setAttribute("issueList", wrInfo.issueList);
			Fms1Servlet.callPage("weeklyReport.jsp",request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateWRComments(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			final String reportDate = request.getParameter("wrDate");
			final String comments = request.getParameter("txtComments");
			Report.setReportComments(prjID, "WR", CommonTools.parseSQLDate(reportDate), comments);
			weeklyReportCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateMRComments(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final long milestoneID = Long.parseLong(request.getParameter("milestoneID"));
			final String comments = request.getParameter("txtComments");
			Report.setMilestoneReportComments(milestoneID, comments);
			doGetMilestone(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doGetGroupPoint(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long wuID = Long.parseLong(request.getParameter("workUnitID"));
			java.text.SimpleDateFormat yearFrmt = new java.text.SimpleDateFormat("yyyy");
			int curYear = Integer.parseInt(yearFrmt.format(new java.util.Date()));
			final Vector vt = new Vector();
			for (int i = 1; i < 13; i++) {
				GroupPointInfo gpointInfo = Report.getGroupPoint(i, wuID, curYear);
				if (gpointInfo != null) {
					vt.addElement(gpointInfo);
				}
				else {
					break;
				}
			}

			final WorkUnitInfo wuInfo = WorkUnit.getWorkUnitInfo(wuID);
			java.text.SimpleDateFormat monFrmt = new java.text.SimpleDateFormat("MM");
			int curMonth = Integer.parseInt(monFrmt.format(new java.util.Date()));
			session.setAttribute("curMonth", String.valueOf(curMonth));
			session.setAttribute("curYear", String.valueOf(curYear));
			session.setAttribute("groupPoint", vt);
			session.setAttribute("workUnit", wuInfo);
			int numGroup = 0;
			numGroup = WorkUnit.getNumOperationGroup();
			session.setAttribute("numOperationGroup", String.valueOf(numGroup));
			Fms1Servlet.callPage("grpPointUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doGetGroupPoint2(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();

			final String strMonth = request.getParameter("curMonth");
			final String strYear = request.getParameter("curYear");
			
			java.text.SimpleDateFormat yearFrmt = new java.text.SimpleDateFormat("yyyy");
			java.text.SimpleDateFormat monFrmt = new java.text.SimpleDateFormat("MM");
			int curYear, curMonth;
			
			if (strMonth == null)
				curMonth = Integer.parseInt(monFrmt.format(new java.util.Date()));
			else
				curMonth = Integer.parseInt(strMonth);
			
			if (strYear == null)
				curYear = Integer.parseInt(yearFrmt.format(new java.util.Date()));
			else
				curYear = Integer.parseInt(strYear);
			
			Vector vtGroup = new Vector();
			vtGroup = WorkUnit.getChildrenGroups(Parameters.FSOFT_WU);
			Vector vtPoint = new Vector();
			
			for (int i = 0; i<vtGroup.size();i++)
			{
				GroupInfo info = (GroupInfo) vtGroup.elementAt(i);
				
				if (info.isOperation)
				{
					GroupPointInfo gpInfo = Report.getGroupPoint(curMonth, info.wuID, curYear);
					gpInfo.groupName = info.name;
					
					vtPoint.add(gpInfo);
				}
			}
			
			session.setAttribute("curMonth", String.valueOf(curMonth));
			session.setAttribute("curYear", String.valueOf(curYear));
			session.setAttribute("vtPoint", vtPoint);
			session.setAttribute("vtGroups", vtGroup);
			
			Fms1Servlet.callPage("grpPointUpdate2.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    // HanhTN added
	public static final void doGetGroupPoint3(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HttpSession session = request.getSession();

			String strStartDate = request.getParameter("strStartDate");
			String strEndDate = request.getParameter("strEndDate");
			String strMonth = request.getParameter("curMonth");
			String strYear = request.getParameter("curYear");
			
			ReportMonth objRpMonth = new ReportMonth();
            int curYear, curMonth;
			java.sql.Date startDate, endDate;
			
			// startDate & endDate
			if (strStartDate == null) {
				startDate = objRpMonth.getFirstDayOfMonth();
			}
			else {
				startDate = CommonTools.parseSQLDate(strStartDate);
			}
			if (strEndDate == null) {
				endDate = objRpMonth.getLastDayOfMonth();
			}
			else {
				endDate = CommonTools.parseSQLDate(strEndDate);
			}

			// curMonth & curYear
			if (strMonth == null) {
				curMonth = objRpMonth.getMonth();
			}
			else {
				curMonth = Integer.parseInt(strMonth);	
			}
			if (strYear == null) {
				curYear = objRpMonth.getYear();
			}
			else {
				curYear = Integer.parseInt(strYear);	
			}
									
			 Vector vtGroup = new Vector();
			 vtGroup = WorkUnit.getChildrenGroups(Parameters.FSOFT_WU);
			 Vector vtPoint = new Vector();
		
			 GroupInfo info;
			 GroupPointBAInfo gpInfo;
			 for (int i = 0; i<vtGroup.size(); i++)
			 {
				 info = (GroupInfo) vtGroup.elementAt(i);
				 if (!info.isOperation)
				 {					
					 gpInfo = Report.getGroupPointBA(curMonth, info.wuID, curYear, startDate, endDate, info.name);
					 gpInfo.groupName = info.name;
					 vtPoint.add(gpInfo);
				 }
			 }
			 session.setAttribute("curMonth", String.valueOf(curMonth));
			 session.setAttribute("curYear", String.valueOf(curYear));
			 session.setAttribute("strStartDate", String.valueOf(strStartDate));
			 session.setAttribute("strEndDate", String.valueOf(strEndDate));			
			 session.setAttribute("vtPoint", vtPoint);
			 session.setAttribute("vtGroups", vtGroup);
			 //Origin
			 Fms1Servlet.callPage("grpPointUpdate3.jsp",request,response);
		}
		catch (Exception e) {
            e.printStackTrace();
		}
	}		
	
	// HanhTN added
	public static final void doUpdateGroupPointBA(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String strMonth = request.getParameter("curMonth");
			final String strYear = request.getParameter("curYear");
			java.text.SimpleDateFormat yearFrmt = new java.text.SimpleDateFormat("yyyy");
			java.text.SimpleDateFormat monFrmt = new java.text.SimpleDateFormat("MM");
			int curYear, curMonth;
			
			if (strMonth == null)
				curMonth = Integer.parseInt(monFrmt.format(new java.util.Date()));
			else
				curMonth = Integer.parseInt(strMonth);
			if (strYear == null)
				curYear = Integer.parseInt(yearFrmt.format(new java.util.Date()));
			else
				curYear = Integer.parseInt(strYear);
			String[] arrWU = request.getParameterValues("wuID");						
			String[] arrTimeLiness = request.getParameterValues("timeLiness");
            // String[] arrResponseTime = request.getParameterValues("responseTime");
			String[] arrBudgetPerformance = request.getParameterValues("budgetPerformance");
			String[] arrBusyRate = request.getParameterValues("busyRate");
			String[] arrCustomerSatisfaction = request.getParameterValues("customerSatisfaction");
			String[] arrValueAchievement = request.getParameterValues("valueAchievement");
			String[] arrLanguageIndex = request.getParameterValues("languageIndex");
			String[] arrTechnologyIndex = request.getParameterValues("technologyIndex");
			int i, noGroup;
			noGroup = arrWU.length;
			GroupPointBAInfo info;

			for (i = 0; i < noGroup; i++)
			{
				info = new GroupPointBAInfo();				
				info.workUnitId = Long.parseLong(arrWU[i]);
				info.year = curYear;
				info.month = curMonth;

				info.timeLiness = CommonTools.parseDouble(arrTimeLiness[i]);
                // info.responseTime = CommonTools.parseDouble(arrResponseTime[i]);
				info.budgetPerformance = CommonTools.parseDouble(arrBudgetPerformance[i]);
				info.busyRate = CommonTools.parseDouble(arrBusyRate[i]);
				info.customerSatisfaction = CommonTools.parseDouble(arrCustomerSatisfaction[i]);
				info.valueAchievement = CommonTools.parseDouble(arrValueAchievement[i]);
				info.languageIndex = CommonTools.parseDouble(arrLanguageIndex[i]);
				info.technologyIndex = CommonTools.parseDouble(arrTechnologyIndex[i]);
				info.GroupRanking = 1; // Default value
				Report.setGroupPointBA(info);
			}
			Report.groupRankingAllBA(curMonth, curYear);
			doGetGroupPoint3(request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doViewGroupPoint(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long wuID = Long.parseLong(request.getParameter("wuID"));
			String strCurYear = request.getParameter("curYear");
			int curYear;
			if (!strCurYear.equalsIgnoreCase("")) {
				curYear = Integer.parseInt(strCurYear);
			}
			else {
				java.text.SimpleDateFormat yearFrmt = new java.text.SimpleDateFormat("yyyy");
				curYear = Integer.parseInt(yearFrmt.format(new java.util.Date()));
			}
			final Vector vt = new Vector();
			for (int i = 1; i < 13; i++) {
				GroupPointInfo gpointInfo = Report.getGroupPoint(i, wuID, curYear);
				if (gpointInfo != null) {
					vt.addElement(gpointInfo);
				}
				else {
					break;
				}
			}
			final WorkUnitInfo wuInfo = WorkUnit.getWorkUnitInfo(wuID);
			java.text.SimpleDateFormat monFrmt = new java.text.SimpleDateFormat("MM");
			int curMonth = Integer.parseInt(monFrmt.format(new java.util.Date()));
			session.setAttribute("curMonth", String.valueOf(curMonth));
			session.setAttribute("curYear", String.valueOf(curYear));
			session.setAttribute("groupPoint", vt);
			session.setAttribute("workUnit", wuInfo);
			int numGroup = 0;
			numGroup = WorkUnit.getNumOperationGroup();
			session.setAttribute("numOperationGroup", String.valueOf(numGroup));
			Fms1Servlet.callPage("grpPointUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateGroupPoint(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final long wuID = Long.parseLong(request.getParameter("wuID"));
			final int curYear = Integer.parseInt(request.getParameter("curYear"));
			final int selectedMonth = Integer.parseInt(request.getParameter("month"));
			
			String[] arrProposal = request.getParameterValues("Proposal");
			String[] arrRevenue = request.getParameterValues("Revenue");
			String[] arrNetIncome = request.getParameterValues("NetIncome");
			String[] arrReceivable = request.getParameterValues("Receivable");
			String[] arrLanguage = request.getParameterValues("Language");
			String[] arrExperience = request.getParameterValues("Experience");
			String[] arrTurnover = request.getParameterValues("Turnover");
			String[] arrTechnology = request.getParameterValues("Technology");
			
			int i;
			i = selectedMonth - 1;
			
			GroupPointInfo info = new GroupPointInfo();
			
			info.workUnitId = wuID;
			info.year = curYear;
			info.month = i + 1;
			info.Proposal = CommonTools.parseDouble(arrProposal[i]);
			info.Revenue = CommonTools.parseDouble(arrRevenue[i]);
			info.NetIncome = CommonTools.parseDouble(arrNetIncome[i]);
			info.Receivable = CommonTools.parseDouble(arrReceivable[i]);
			info.Language = CommonTools.parseDouble(arrLanguage[i]);
			info.Experience = CommonTools.parseDouble(arrExperience[i]);
			info.Turnover = CommonTools.parseDouble(arrTurnover[i]);
			info.Technology = CommonTools.parseDouble(arrTechnology[i]);

			/*
			if (wuID == Parameters.FSOFT_WU) // FSOFT
				info.Project = Report.getFsoftPropjectPoint(info.month, curYear);
			else
			*/
			info.Project = Report.sumGroupPoint(wuID, info.month, curYear);

			info.GroupRanking = 1; // Default value
			Report.setGroupPoint(info);
			Report.groupRankingAll(info.month, curYear);
			doViewGroupPoint(request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateGroupPoint2(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String strMonth = request.getParameter("curMonth");
			final String strYear = request.getParameter("curYear");
			
			java.text.SimpleDateFormat yearFrmt = new java.text.SimpleDateFormat("yyyy");
			java.text.SimpleDateFormat monFrmt = new java.text.SimpleDateFormat("MM");
			int curYear, curMonth;
			
			if (strMonth == null)
				curMonth = Integer.parseInt(monFrmt.format(new java.util.Date()));
			else
				curMonth = Integer.parseInt(strMonth);
			
			if (strYear == null)
				curYear = Integer.parseInt(yearFrmt.format(new java.util.Date()));
			else
				curYear = Integer.parseInt(strYear);
			
			String[] arrWU = request.getParameterValues("wuID");
			String[] arrProposal = request.getParameterValues("Proposal");
			String[] arrRevenue = request.getParameterValues("Revenue");
			String[] arrNetIncome = request.getParameterValues("NetIncome");
			String[] arrReceivable = request.getParameterValues("Receivable");
			String[] arrLanguage = request.getParameterValues("Language");
			String[] arrExperience = request.getParameterValues("Experience");
			String[] arrTurnover = request.getParameterValues("Turnover");
			String[] arrTechnology = request.getParameterValues("Technology");
			
			int i, noGroup;
			if(arrWU != null)
			{
				noGroup = arrWU.length;	

				for (i = 0; i < noGroup; i++)
				{
					
					GroupPointInfo info = new GroupPointInfo();
				
					info.workUnitId = Long.parseLong(arrWU[i]);

					info.year = curYear;
					info.month = curMonth;

					info.Proposal = CommonTools.parseDouble(arrProposal[i]);
					info.Revenue = CommonTools.parseDouble(arrRevenue[i]);
					info.NetIncome = CommonTools.parseDouble(arrNetIncome[i]);
					info.Receivable = CommonTools.parseDouble(arrReceivable[i]);
					info.Language = CommonTools.parseDouble(arrLanguage[i]);
					info.Experience = CommonTools.parseDouble(arrExperience[i]);
					info.Turnover = CommonTools.parseDouble(arrTurnover[i]);
					info.Technology = CommonTools.parseDouble(arrTechnology[i]);
	
					info.Project = Report.sumGroupPoint(info.workUnitId, info.month, curYear);
	
					info.GroupRanking = 1; // Default value
					Report.setGroupPoint(info);
				}
			}
			
			
			
			Report.groupRankingAll(curMonth, curYear);
			
			doGetGroupPoint2(request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateProjectPoint(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();

			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			final long milestoneID = Long.parseLong(request.getParameter("milestoneID"));
			/* Comment by HaiMM:
			 * 1- Parameter: 
			 * CS: CustomerSatisfactionPoint; LK: LeakageWdefectUCP; NC: ProcessCompliance
			 * PS: Project Size; EE: EffortEfficiency; CC: CorrectionCost
			 * TL: Timeliness; RC: RequirementCompleteness
			 * 
			 * 2- Metrics Index
			 * Customer Satisfaction = 41   (0)
			 * Leakage = 36 				(1)
			 * Process Compliance = 65 		(2)
			 * Effort Efficiency = 254 		(3)
			 * Correction Cost = 15			(4)
			 * Timeliness = 4				(5)
			 * Requirement Completeness = 2	(6)
			 * 
			 */
			 
			 // Add by HaiMM - Start
			/*
			final Vector stdMetrics = Project.getStandardMetricList(prjID);
			
			NormInfo normInfoCS =(NormInfo)stdMetrics.elementAt(0);
			NormInfo normInfoLK =(NormInfo)stdMetrics.elementAt(1);
			NormInfo normInfoNC =(NormInfo)stdMetrics.elementAt(2);
			NormInfo normInfoEE =(NormInfo)stdMetrics.elementAt(3);
			NormInfo normInfoCC =(NormInfo)stdMetrics.elementAt(4);
			NormInfo normInfoTL =(NormInfo)stdMetrics.elementAt(5);
			NormInfo normInfoRC =(NormInfo)stdMetrics.elementAt(6);
			// Add by HaiMM - End
			*/
			ProjectPointInfo info = new ProjectPointInfo();
			final String strPS = request.getParameter("PS");
			double dbPS = CommonTools.parseDouble(strPS);
			if (Double.isNaN(dbPS))
				dbPS = 0;

			double dbPS2;
			
			dbPS2 = (dbPS - 150) * 0.02;	
			if (dbPS2 > 6) 
				dbPS = 6;
			else if (dbPS <= 150) 
				dbPS = 0;
			else 
				dbPS = dbPS2;
			info.ProjectSize = dbPS;

			ProjectInfo info2 = Project.getProjectInfo(prjID);
			NormInfo normForProjectPoint = Norms.getNorm(info2.getParent(), info2.getGrandParent(), Metrics.getMetricID(MetricDescInfo.EFFORT_EFFICIENCY), info2.getLifecycleId(), info2.getStartDate());
			final String strEE = request.getParameter("EE");
			double dbEE = CommonTools.parseDouble(strEE);
			double dbEE2 = dbEE;
			
			// Modify by HaiMM: Start
//			if (normInfoEE.usl >= 0) { // based on Project Norm
//				dbEE = (Double.isNaN(dbEE)) ? -2 :(dbEE < normInfoEE.usl)? -2:(dbEE >= normInfoEE.usl && dbEE <= normInfoEE.average)? 2:(dbEE > normInfoEE.average && dbEE <= normInfoEE.lsl)? 3:4;	 
//			}
//			else { // based on FSOFT Norm
				dbEE = (Double.isNaN(dbEE)) ? -2 :(dbEE < normForProjectPoint.lcl)? -2:(dbEE >= normForProjectPoint.lcl && dbEE <= normForProjectPoint.average)? 2:(dbEE > normForProjectPoint.average && dbEE <= normForProjectPoint.ucl)? 3:4;				
//			}
			// Modify by HaiMM: End
			
			info.EffortEfficiency = dbEE;

			normForProjectPoint = Norms.getCSSNorm(MetricDescInfo.CUSTOMER_SATISFACTION,info2.getLifecycleId(),WorkUnit.getGroupsIDbyWorkUnitID(info2.getWorkUnitId()), info2.getStartDate());
			final String strCS = request.getParameter("CS");
			double dbCS = CommonTools.parseDouble(strCS);
			double dbCS2 = dbCS;
			
			// Modify by HaiMM: Start
//			if (normInfoCS.usl >= 0) { // based on Project Norm
//				dbCS = (Double.isNaN(dbCS)) ? 0 :(dbCS < 40) ? -5 : (dbCS >= 40 && dbCS < normInfoCS.usl) ? -3 : (dbCS >= normInfoEE.usl && dbCS <= normInfoEE.average)? 3 : (dbCS > normInfoEE.average && dbCS <= normInfoEE.lsl)? 5 : 7 ;
//			}
//			else { // based on FSOFT Norm
				dbCS = (Double.isNaN(dbCS)) ? 0 :(dbCS < 40) ? -5 : (dbCS >= 40 && dbCS < normForProjectPoint.lcl) ? -3 : (dbCS >= normForProjectPoint.lcl && dbCS <= normForProjectPoint.average)? 3 : (dbCS > normForProjectPoint.average && dbCS <= normForProjectPoint.ucl)? 5 : 7 ;				
//			}
			// Modify by HaiMM: End
			
			info.CusSatisfaction = dbCS;

			normForProjectPoint = Norms.getNorm(info2.getParent(), info2.getGrandParent(), Metrics.getMetricID(MetricDescInfo.REQUIREMENT_COMPLETENESS),info2.getLifecycleId(), info2.getStartDate());
			double dbRC = CommonTools.parseDouble(request.getParameter("RC"));
			
			// Modify by HaiMM: Start
//			if (normInfoRC.usl >= 0) { // based on Project Norm
//				dbRC = (Double.isNaN(dbRC)) ? -2 : (dbRC > normInfoRC.lsl) ? 2 : (dbRC < normInfoRC.usl) ? -2 : 1;
//			}
//			else { // based on FSOFT Norm
				dbRC = (Double.isNaN(dbRC)) ? -2 : (dbRC > normForProjectPoint.ucl) ? 2 : (dbRC < normForProjectPoint.lcl) ? -2 : 1;
//			}
			// Modify by HaiMM: End
			
			info.ReqCompleteness = dbRC;

			normForProjectPoint = Norms.getNorm(info2.getParent(), info2.getGrandParent(), Metrics.getMetricID(MetricDescInfo.TIMELINESS),info2.getLifecycleId(), info2.getStartDate());
			double dbTL = CommonTools.parseDouble( request.getParameter("TL"));
			
			// Modify by HaiMM: Start
//			if (normInfoTL.usl >= 0) {
//				dbTL = (Double.isNaN(dbTL)) ? -2 : (dbTL > normInfoTL.lsl) ? 3 : (dbTL < normInfoTL.usl) ? -2 : (dbTL <= normInfoTL.lsl && dbTL > normInfoTL.average) ? 2 : 1;
//			}
//			else {
				dbTL = (Double.isNaN(dbTL)) ? -2 : (dbTL > normForProjectPoint.ucl) ? 3 : (dbTL < normForProjectPoint.lcl) ? -2 : (dbTL <= normForProjectPoint.ucl && dbTL > normForProjectPoint.average) ? 2 : 1;
//			}
			// Modify by HaiMM: End

			info.Timeliness = dbTL;

			normForProjectPoint = Norms.getNorm(info2.getParent(), info2.getGrandParent(), Metrics.getMetricID(MetricDescInfo.LEAKAGE),info2.getLifecycleId(), info2.getStartDate());
			double dbLK = CommonTools.parseDouble(request.getParameter("LK"));

			// Modify by HaiMM: Start
//			if (normInfoLK.usl >= 0) { // based on Project Norm
//				dbLK = (Double.isNaN(dbLK)) ? -2 : (dbLK > normInfoLK.lsl) ? -2 : (dbLK < normInfoLK.usl) ? 3 :(dbLK >= normInfoLK.usl && dbLK < normInfoLK.average)? 2:1;
//			}
//			else { // based on FSOFT Norm
				dbLK = (Double.isNaN(dbLK)) ? -2 : (dbLK > normForProjectPoint.ucl) ? -2 : (dbLK < normForProjectPoint.lcl) ? 3 :(dbLK >= normForProjectPoint.lcl && dbLK < normForProjectPoint.average)? 2:1;
//			}
			// Modify by HaiMM: End
			
			info.Leakage = dbLK;

			normForProjectPoint = Norms.getNorm(info2.getParent(), info2.getGrandParent(), Metrics.getMetricID(MetricDescInfo.CORRECTION_COST),info2.getLifecycleId(), info2.getStartDate());
			double dbCC = CommonTools.parseDouble(request.getParameter("CC"));
			
			// Modify by HaiMM: Start
//			if (normInfoCC.usl >= 0) { // based on Project Norm
//				dbCC = (Double.isNaN(dbCC)) ? -2 : (dbCC > normInfoCC.lsl) ? -2 : (dbCC < normInfoCC.usl) ?  3 :(dbCC >= normInfoCC.usl && dbCC < normInfoCC.average)? 2:1;
//			}
//			else { // based on FSOFT Norm
				dbCC = (Double.isNaN(dbCC)) ? -2 : (dbCC > normForProjectPoint.ucl) ? -2 : (dbCC < normForProjectPoint.lcl) ?  3 :(dbCC >= normForProjectPoint.lcl && dbCC < normForProjectPoint.average)? 2:1;
//			}
			// Modify by HaiMM: End

			info.CorrectionCost = dbCC;

			final Vector vtOtherAct = QualityObjective.getOtherActivityList(prjID);
			OtherActInfo oaInfo;
			int test = 0;
			for(int i=0;i<vtOtherAct.size();i++){
				oaInfo=(OtherActInfo)vtOtherAct.get(i);
				if(oaInfo.qcActivity == 42 && oaInfo.status == 1){
					test++;
				}
			}
			normForProjectPoint = Norms.getNorm(info2.getParent(), info2.getGrandParent(), Metrics.getMetricID(MetricDescInfo.PROCESS_COMPLIANCE),info2.getLifecycleId(), info2.getStartDate());
			double dbNC = CommonTools.parseDouble(request.getParameter("NC"));
			if (!Double.isNaN(dbNC)) {
				dbNC = (dbNC > normForProjectPoint.ucl) ? -2 : (dbNC < normForProjectPoint.lcl) ? 2 : 1;
			}
            else {
            	if(test > 0){
            		dbNC = 2;
            	}else{
            		dbNC = 0;
            	}
            }
            
            info.ProjectNC = dbNC;
			final double dbWO = CommonTools.parseDouble(request.getParameter("WO"));
			info.WOPoint = dbWO;
			final double dbAN = CommonTools.parseDouble(request.getParameter("AN"));
			info.ANPoint = dbAN;
			final double dbPP = CommonTools.parseDouble(request.getParameter("PP"));
			info.PPPoint = dbPP;
			final double dbPR = CommonTools.parseDouble(request.getParameter("PR"));
			info.PRPoint = dbPR;
			double OverdueNCsObsPoint = 0;
			double OverdueNCsObsCount = CommonTools.parseDouble(request.getParameter("OverdueNCsObsCount"));
			if(Double.isNaN(OverdueNCsObsCount)) OverdueNCsObsCount = 0;
			info.OverdueNCsObsCount = OverdueNCsObsCount;
			OverdueNCsObsPoint = - OverdueNCsObsCount * 0.5;
			if(OverdueNCsObsPoint < -2){
				OverdueNCsObsPoint = -2;
			}
			info.OverdueNCsObsPoint = OverdueNCsObsPoint;
			double dbPrestige = CommonTools.parseDouble(request.getParameter("Prestige"));
			if (Double.isNaN(dbPrestige))
				dbPrestige = 0;
			info.PrestigePoint = dbPrestige;

			double dbCusPoint = CommonTools.parseDouble(request.getParameter("CusPoint"));
			if (Double.isNaN(dbCusPoint))
				dbCusPoint = 0;
			info.CusPoint = dbCusPoint;			

			String[] strPoint = request.getParameterValues("cmValue");
			Vector cusMetric = (Vector)session.getAttribute("cusMetric");
			CusMetricInfo cusMetricInfo;
			double cusPoint = 0;
			for(int i=0;i<cusMetric.size();i++){
				cusMetricInfo = (CusMetricInfo)cusMetric.elementAt(i);
				cusPoint = cusPoint + CommonTools.parseDouble(strPoint[i]);
				cusMetricInfo.point = CommonTools.parseDouble(strPoint[i]);
			}
			
			double dbPPoint=
					dbPS
					+ dbEE
					+ dbCS
					+ dbRC
					+ dbTL
					+ dbLK
					+ dbCC
					+ dbNC
					+ dbWO
					+ dbAN
					+ dbPP
					+ dbPR
					+ dbPrestige
					+ dbCusPoint
					+ cusPoint
					+ OverdueNCsObsPoint;
			info.ProjectPoint = dbPPoint;
			if (dbCS2 <= 40 || dbEE2 < 50 || dbPPoint <=0) {
				info.ProjectEval = "Failed";
			}else if (dbWO < -1 || dbAN < -1 || info.ProjectEval == "Failed") {
				info.ProjectEval = "No Reward";
			}	
            else if (dbPPoint > 30) {
                info.ProjectEval = "Excellence";
            }
			else if (dbPPoint > 20) {
				info.ProjectEval = "Good";
			}
			else if (dbPPoint > 10) {
				info.ProjectEval = "Fair";
			}
			else if (dbPPoint > 0) {
				info.ProjectEval = "Acceptable";
			}
			
			Report.setProjectPoint(milestoneID, info);
			Metrics.insertCusPoint(cusMetric);
			doGetMilestone(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doViewProjectPoint(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			long wuID = Long.parseLong(request.getParameter("workUnitID"));
			final String strGroup = request.getParameter("cboGroup");
			final String strProjectType = request.getParameter("cboProjectType");
			final String strProjectCategory = request.getParameter("cboProjectCategory");
			final String strProjectStatus = request.getParameter("cboProjectStatus");
			session.setAttribute("cboProjectType", strProjectType);
			session.setAttribute("cboProjectCategory", strProjectCategory);
			session.setAttribute("cboProjectStatus", strProjectStatus);
			String strWhere = "";
			if (strGroup != null) {
				if (!strGroup.equalsIgnoreCase("-1"))
					wuID = Long.parseLong(strGroup.trim());
				if (!strProjectType.equalsIgnoreCase("-1"))
					strWhere = strWhere + " AND PROJECT.TYPE = '" + strProjectType.trim() + "'";
				if (!strProjectCategory.equalsIgnoreCase("-1"))
					strWhere = strWhere + " AND PROJECT.CATEGORY = '" + strProjectCategory.trim() + "'";
				if (!strProjectStatus.equalsIgnoreCase("-1"))
					strWhere = strWhere + " AND PROJECT.STATUS = '" + strProjectStatus.trim() + "'";
			}

			WorkUnitInfo wuInfo = WorkUnit.getWorkUnitInfo(wuID);
			ReportMonth actualMonth = new ReportMonth();
			int curYear = actualMonth.getYear();
			int curMonth = actualMonth.getMonth();

			String curY = (String) request.getParameter("selectYear");
			if (curY != null)
				curYear = CommonTools.parseInt(curY);

			String curM = (String) request.getParameter("selectMonth");
			if (curM != null)
				curMonth = CommonTools.parseInt(curM);

			actualMonth = new ReportMonth(curMonth, curYear);

			String CurrentMonth;
			CurrentMonth = CommonTools.getMonth(curMonth) + "-" + String.valueOf(curYear);

			String beginMonth, beginMonth1;
			String endMonth, endMonth1;

			// beginMonth = CommonTools.getMonth(curMonth) + "/" + "01/" + String.valueOf(curYear);
			beginMonth = "Jan/01/" + String.valueOf(curYear);
			endMonth =
				CommonTools.getMonth(curMonth)
					+ "/"
					+ CommonTools.getNoDay(curMonth, curYear)
					+ "/"
					+ String.valueOf(curYear);

			// beginMonth1 = "01-" + CommonTools.getMonth(curMonth) + "-" + String.valueOf(curYear).trim().substring(2, 4);
			beginMonth1 = "01-Jan-" + String.valueOf(curYear).trim().substring(2, 4);
			endMonth1 =
				CommonTools.getNoDay(curMonth, curYear)
					+ "-"
					+ CommonTools.getMonth(curMonth)
					+ "-"
					+ String.valueOf(curYear).trim().substring(2, 4);
			Vector vtProject1 = new Vector();
			vtProject1 = WorkUnit.getProjects(wuID, beginMonth, endMonth, strWhere);
			Vector vtProjectPoint1 = new Vector();
			vtProjectPoint1 = Report.getProjectPoint(vtProject1, beginMonth1, endMonth1);

			actualMonth.moveToPreviousMonth();

			int previousYear = actualMonth.getYear();
			int previousMonth = actualMonth.getMonth();

			String PreviousMonth;
			PreviousMonth = CommonTools.getMonth(previousMonth) + "-" + String.valueOf(previousYear);

			String beginMonth2, beginMonth21;
			String endMonth2, endMonth21;

			// beginMonth2 = CommonTools.getMonth(previousMonth) + "/" + "01/" + String.valueOf(previousYear);
			beginMonth2 = "Jan/01/" + String.valueOf(previousYear);
			endMonth2 =
				CommonTools.getMonth(previousMonth)
					+ "/"
					+ CommonTools.getNoDay(previousMonth, previousYear)
					+ "/"
					+ String.valueOf(previousYear);
			// beginMonth21 =
			//	"01-" + CommonTools.getMonth(previousMonth) + "-" + String.valueOf(previousYear).trim().substring(2, 4);
			beginMonth21 ="01-Jan-" + String.valueOf(previousYear).trim().substring(2, 4);
			endMonth21 =
				CommonTools.getNoDay(previousMonth, previousYear)
					+ "-"
					+ CommonTools.getMonth(previousMonth)
					+ "-"
					+ String.valueOf(previousYear).trim().substring(2, 4);

			Vector vtProject2 = new Vector();
			vtProject2 = WorkUnit.getProjects(wuID, beginMonth2, endMonth2, strWhere);

			Vector vtProjectPoint2 = new Vector();
			vtProjectPoint2 = Report.getProjectPoint(vtProject2, beginMonth21, endMonth21);

			session.setAttribute("workUnit1", wuInfo);
			session.setAttribute("curYear", String.valueOf(curYear));
			session.setAttribute("curMonth", CommonTools.getMonth(curMonth));
			session.setAttribute("CurrentMonth", CurrentMonth);
			session.setAttribute("PreviousMonth1", PreviousMonth);
			session.setAttribute("vtProject1", vtProject1);
			session.setAttribute("vtProject2", vtProject2);
			session.setAttribute("vtProjectPoint1", vtProjectPoint1);
			session.setAttribute("vtProjectPoint2", vtProjectPoint2);
			Fms1Servlet.callPage("ProjectPointPage.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * submit weekly report to Dashboard
	 */
	public final static void submitWeeklyReport(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final ProjectInfo projectInfo = (ProjectInfo) session.getAttribute("projectInfo");
			Date reportDate = (Date) session.getAttribute("reportDate");
			ProjectDatabaseInfo projectDatabaseInfo = new ProjectDatabaseInfo();
			projectDatabaseInfo.projectID = projectInfo.getProjectId();
			java.sql.Date sqlReportDate = new java.sql.Date(reportDate.getTime());
			projectDatabaseInfo.lastUpdated = sqlReportDate;
			//------------------------------Defect ----------------------------------
			if (reportDate != null) {
                final Vector defectWeeklyInfoList = (Vector)session.getAttribute("defectWeeklyInfo");
                if (defectWeeklyInfoList != null) {
                    DefectWeeklyInfo openingDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(0);
                    DefectWeeklyInfo totalDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(1);
                    DefectWeeklyInfo weightedOpeningDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(2);
                    DefectWeeklyInfo weightedTotalDefectWeeklyInfo = (DefectWeeklyInfo)defectWeeklyInfoList.elementAt(3);
				
                    projectDatabaseInfo.FatalPendingDefect = openingDefectWeeklyInfo.getFatal();
    				projectDatabaseInfo.SeriousPendingDefect = openingDefectWeeklyInfo.getSerious();
    				projectDatabaseInfo.MediumPendingDefect = openingDefectWeeklyInfo.getMedium();
    				projectDatabaseInfo.CosmeticPendingDefect = openingDefectWeeklyInfo.getCosmetic();
    				projectDatabaseInfo.TotalFatalDefect = totalDefectWeeklyInfo.getFatal();
    				projectDatabaseInfo.TotalSeriousDefect = totalDefectWeeklyInfo.getSerious();
    				projectDatabaseInfo.TotalMediumDefect = totalDefectWeeklyInfo.getMedium();
    				projectDatabaseInfo.TotalCosmeticDefect = totalDefectWeeklyInfo.getCosmetic();
    				projectDatabaseInfo.TotalWeightedPendingDefect = weightedOpeningDefectWeeklyInfo.getTotal();
    				projectDatabaseInfo.TotalWeightedDefect = weightedTotalDefectWeeklyInfo.getTotal();
                }
			}
			//----------Requirement----------
			final RequirementInfo reqHdrInfo = (RequirementInfo) session.getAttribute("reqHdrInfo");
			projectDatabaseInfo.CommittedRequirement = reqHdrInfo.sumSizeCommitted;
			projectDatabaseInfo.DesignedRequirement = reqHdrInfo.sumSizeDesigned;
			projectDatabaseInfo.CodedRequirement = reqHdrInfo.sumSizeCoded;
			projectDatabaseInfo.TestedRequirement = reqHdrInfo.sumSizeTested;
			projectDatabaseInfo.DeployedRequirement = reqHdrInfo.sumSizeDeployed;
			projectDatabaseInfo.AcceptedRequirement = reqHdrInfo.sumSizeAccepted;
			projectDatabaseInfo.TotalRequirement = reqHdrInfo.sumSizeCommitted;
			//----------------Metrics--------------------
			//EffortInfo effortInfo = Effort.getEffortInfo(projectInfo, sqlReportDate);
			double actual=Effort.getActualEffort(projectInfo.getProjectId(), null,sqlReportDate);
            projectDatabaseInfo.Effort = actual;
			projectDatabaseInfo.PercentComplete = (Double.isNaN(reqHdrInfo.sumSizeCompletion))?0:(int)reqHdrInfo.sumSizeCompletion;
			if ((projectInfo.getPlannedEffort() != 0) && (reqHdrInfo.sumSizeDeployedOrAccepted != 0))
				projectDatabaseInfo.EffortStatus =
					(actual/ projectInfo.getPlannedEffort())
						* (reqHdrInfo.sumSizeCommitted / reqHdrInfo.sumSizeDeployedOrAccepted);
			StageInfo stageInfo = null;
			final Vector stageList = Schedule.getStageList(projectInfo.getProjectId());
			long stageID = 0;
			projectDatabaseInfo.ScheduleStatus = 0;
			for (int i = stageList.size() - 1; i >= 0; i--) {
				stageInfo = (StageInfo) stageList.elementAt(i);
				if (stageInfo.aEndD != null) {
					stageID = stageInfo.milestoneID;
					break;
				}
			}
			if (stageID != 0) {
				double stagePlannedDuration =
					(double) CommonTools.dateDiff(stageInfo.plannedBeginDate, stageInfo.plannedEndDate);
				NormInfo nrmStageDeviation =
					Norms.getNorm(projectInfo.getProjectId(), MetricDescInfo.STAGE_SCHEDULE_DEVIATION);
				if ((nrmStageDeviation.plannedValue != 0) && (stagePlannedDuration != 0)) {
					projectDatabaseInfo.ScheduleStatus =
						(double) CommonTools.dateDiff(stageInfo.plannedEndDate, stageInfo.aEndD)
							* 100d
							/ (stagePlannedDuration * nrmStageDeviation.plannedValue);
				}
			}
			projectDatabaseInfo.issueList = (Vector) session.getAttribute("issueList");
			Project.updateProjectDatabase(projectDatabaseInfo);
			Fms1Servlet.callPage("weeklyReport.jsp?dash=1",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doGetMilestone(final HttpServletRequest request, final HttpServletResponse response) {
		try {
            // TODO Check this function, it called to Effort.getEffortInfo(
            // ProjectInfo pinf, Date date) many times
      		final HttpSession session = request.getSession();
            session.setAttribute("caller", String.valueOf(MILESTONE_CALLER));
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			final ProjectInfo projectInfo = Project.getProjectInfo(prjID);
            final Vector stageList = Schedule.getStageList(projectInfo);
			final Vector dar = Dar.getDarPlan(prjID);
            final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
            session.setAttribute("prjDateInfo", prjDateInfo);
            Vector userList = Assignments.getWOTeamList(prjID, null, null);
            session.setAttribute("userList", userList);
			long stageID = 0;
			// If the project not defined stage yet --> Go to inform page
			if (stageList.size() == 0) {
				Fms1Servlet.callPage("milestone.jsp?noStage=1", request, response);
				return;
			}
			Vector processEffortByStages = Effort.getProcessEffortByStage(projectInfo,stageList);
			String strStageID = request.getParameter("stageID");
            StageInfo stageInfo = null;
			if (strStageID == null) //from the update comments
				strStageID = request.getParameter("milestoneID");
			if (strStageID == null) {
				// get the last stage with actual finish date
				// get current stage..
				for (int i = stageList.size() - 1; i >= 0; i--) {
					stageInfo = (StageInfo) stageList.elementAt(i);
					if ((stageInfo.aEndD != null)
						&& (stageInfo.actualBeginDate != null)
						&& (stageInfo.plannedEndDate != null)
						&& (stageInfo.plannedBeginDate != null)) {
						stageID = stageInfo.milestoneID;
						break;
					}
				}
				// If stage is defined but have no stage completed --> Go to inform page
				if (stageID == 0) {
					Fms1Servlet.callPage("milestone.jsp?noStage=1",request,response);
					return;
				}
			}
			else {
				stageID = Long.parseLong(strStageID);
				stageInfo = Schedule.getStageByID(stageID);
				if (stageInfo.milestoneID == 0) {
					System.err.println("Bad stage ID : " + strStageID);
					return;
				}
			}
			final WeeklyReportInfo wrInfo =
				Report.getReportCommonInfo(projectInfo, new java.sql.Date(stageInfo.aEndD.getTime()));
			// process effort for the stage
			Vector milestoneEffort = null;
			Vector milestoneEffort2 = null;
            // Avoid null pointer exception of start_date
            if ((! Parameters.dayOfValidation.after(projectInfo.getStartDate())) &&
                (projectInfo.getApplyPPM() == 1))
			{// Apply for new projects				
				// process effort for the stage
				milestoneEffort = Effort.getProcessEffortList(projectInfo,processEffortByStages,stageInfo.milestoneID,false);
				// process effort for the project
				milestoneEffort2 = Effort.getProcessEffortList(projectInfo,processEffortByStages,stageInfo.milestoneID,true);
			}
			else {
				// process effort for the stage
				milestoneEffort = Effort.getProcessEffortList(projectInfo, stageInfo.actualBeginDate, stageInfo.aEndD);
				// process effort for the project
				milestoneEffort2 = Effort.getProcessEffortList(projectInfo, projectInfo.getStartDate(), stageInfo.aEndD);
			}
            final Vector milestoneQA = Assets.getProcessQA(stageInfo, wrInfo);
			final Vector milestoneQO = Project.getStandardMetricList(prjID, stageInfo.aEndD);
            final Vector milestoneSchedule = Assets.getProcessSchedule(stageInfo);
			final Vector defectTask = Defect.getDPTask(prjID,stageID);
			final ProjectPointInfo projectPointInfo = Report.getProjectPoint(prjID, stageID);
			// used for project points
			NormInfo nrmAcceptanceRate =
				Norms.getNormDetails(MetricDescInfo.ACCEPTANCE_RATE, projectInfo, stageInfo.aEndD);
			double acceptanceRate = nrmAcceptanceRate.actualValue;
			session.setAttribute("acceptanceRate", CommonTools.formatDouble(acceptanceRate));
            
			// ----------------Metrics--------------------
			Vector metricInfo = Report.getReportMetrics(wrInfo, Report.MILESTONE_REPORT);
			// Add milestone specific metrics
			// --Schedule status
			ReportMetricInfo scheduleStatusMetric = new ReportMetricInfo();
			scheduleStatusMetric.name = "Schedule status";
			scheduleStatusMetric.unit = "%";
			scheduleStatusMetric.estimated = 100;
			double stagePlannedDuration =
				 CommonTools.dateDiff(stageInfo.plannedBeginDate, stageInfo.plannedEndDate);
			NormInfo nrmStageDeviation =
				Norms.getNormGeneralInfo(
					MetricDescInfo.STAGE_SCHEDULE_DEVIATION,
					projectInfo,
					stageInfo.plannedEndDate);
			if ((nrmStageDeviation.plannedValue != 0) && (stagePlannedDuration != 0)) {
				scheduleStatusMetric.spent =
					 CommonTools.dateDiff(stageInfo.plannedEndDate, stageInfo.aEndD)
						* 10000d
						/ (stagePlannedDuration * nrmStageDeviation.average);
				scheduleStatusMetric.remain = scheduleStatusMetric.estimated - scheduleStatusMetric.spent;
				scheduleStatusMetric.color =Color.getColor(scheduleStatusMetric.spent,80,100,Color.HIGHBAD);
			}
			// --Stage duration
			ReportMetricInfo stageDurationMetric = new ReportMetricInfo();
			stageDurationMetric.name = "Stage duration";
			stageDurationMetric.unit = "elapsed days";
			stageDurationMetric.estimated = stagePlannedDuration;
			stageDurationMetric.spent =  CommonTools.dateDiff(stageInfo.actualBeginDate, stageInfo.aEndD);
			stageDurationMetric.remain = stageDurationMetric.estimated - stageDurationMetric.spent;
			// --Stage effort
			ReportMetricInfo stageEffortMetric = new ReportMetricInfo();
			stageEffortMetric.name = "Stage effort usage";
			stageEffortMetric.unit = "person.day";
            StageEffortInfo effort = Effort.getStageEffort(projectInfo,processEffortByStages,stageID);
			stageEffortMetric.estimated =
				((!Double.isNaN(effort.reEstimated)) ? effort.reEstimated : effort.estimated);
			stageEffortMetric.spent = effort.actual;
			stageEffortMetric.remain = stageEffortMetric.estimated - stageEffortMetric.spent;

			metricInfo.add(scheduleStatusMetric);
			metricInfo.add(stageDurationMetric);
			metricInfo.add(stageEffortMetric);
            
			Vector allClosedTasks = Tasks.getClosedProjectTasks(prjID,stageInfo.actualBeginDate,stageInfo.aEndD);
			Vector nextWeekTasks = Tasks.getOpenProjectTasks(prjID,stageInfo.aEndD);
//			Vector cusMetricList = Project.getWOCustomeMetricList(prjID);
			Vector cusMetricList = Project.getCustomerMetric(prjID,stageID);
			Vector cusMetric =  Metrics.getCusMetric(stageID);
			session.setAttribute("cusMetric", cusMetric);
			session.setAttribute("cusMetricList", cusMetricList);
            session.setAttribute("dar", dar);
            session.setAttribute("stageList", stageList);
            session.setAttribute("stageInfo", stageInfo);
            session.setAttribute("metricInfo", metricInfo);
			session.setAttribute("projectInfo", projectInfo);
            session.setAttribute("milestoneQA", milestoneQA);
			session.setAttribute("milestoneQO", milestoneQO);
            session.setAttribute("milestoneSchedule", milestoneSchedule);
			session.setAttribute("milestoneEffort", milestoneEffort);
			session.setAttribute("milestoneEffort2", milestoneEffort2);
			session.setAttribute("milestoneRisk", wrInfo.riskList);
			session.setAttribute("milestoneIssue", wrInfo.issueList);
			session.setAttribute("projectPoint", projectPointInfo);
			session.setAttribute("defectPrevention", defectTask);
			session.setAttribute("allClosedTasks", allClosedTasks);
			session.setAttribute("allOpenTasks", nextWeekTasks);
			// Must be called after 'stageList' is saved in the session
			RequirementCaller.getRCRForMilestone(request, response, stageInfo.milestoneID);
			Fms1Servlet.callPage("milestone.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
/***********************POST MORTEM***********************************************/
    public static final void doGetPostMortemReport(
        final HttpServletRequest request,
        final HttpServletResponse response) {
        doGetPostMortemReport(request, response, "");
    }
    public static final void doGetPostMortemReport(
        final HttpServletRequest request,
        final HttpServletResponse response,
        String index) {
        try {
            final HttpSession session = request.getSession();
            long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			final ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			final Vector stageList = Schedule.getStageList(projectInfo);
            final Vector dar = Dar.getDarPlan(prjID);
			long stageID = 0;
			if (stageList==null||stageList.size() == 0) {
				Fms1Servlet.callPage("postMortemReport.jsp?noStage=1", request, response);
				return;
			}
			StageInfo stageInfo=null;
			// get current milestone
			if(stageList!=null && stageList.size()>0)
			{
				for (int i = stageList.size() - 1; i >= 0; i--) {
				stageInfo = (StageInfo) stageList.elementAt(i);
					if ((stageInfo.aEndD != null)
						&& (stageInfo.actualBeginDate != null)
						&& (stageInfo.plannedEndDate != null)
						&& (stageInfo.plannedBeginDate != null)) {
						stageID = stageInfo.milestoneID;
						break;
					}
				}
			}
			final Vector defectTask = Defect.getDPTask(prjID,stageID);
            final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
            session.setAttribute("prjDateInfo", prjDateInfo);
            
            session.setAttribute("caller", String.valueOf(SCHEDULE_CALLER));
            
            Vector userList = Assignments.getWOTeamList(prjID, null, null);
            session.setAttribute("userList", userList);
            
            PmReportHeaderInfo pmHeaderInfo = Report.getPmHeader(prjID);
            ProjectSizeInfo projectSizeInfo = new ProjectSizeInfo(prjID);
            EffortInfo effortInfo = Effort.getEffortInfo(projectInfo);
            CostTotalInfo costTotalInfo = Project.getCostTotal(prjID);
            Vector stage = Schedule.getStageList(projectInfo);
            Vector processEffortByStages = Effort.getProcessEffortByStage(projectInfo, stage);

            Vector finance = Project.getFinanList(prjID);
            double financeTotal = 0;
            for (int i = 0; i < finance.size(); i++) {
                final FinancialInfo financialInfo = (FinancialInfo) finance.get(i);
                financeTotal += Double.parseDouble(CommonTools.decForm.format(financialInfo.value));
            }

            Vector deliverableList = WorkProduct.getDeliverableList(prjID);
            Vector performanceMetrics = Project.getPerformanceMetrics(prjID);
            Vector standardMetrics = Project.getStandardMetricList(prjID);
            Vector customerMetrics =Project.getCustomerMetric(prjID,stageID); //Project.getWOCustomeMetricList(prjID);
            
            Vector module = WorkProduct.getModuleListSchedule(prjID, WorkProduct.ORDER_BY_PRELEASE);

			Vector processEffort = null; 
            // Avoid null pointer exception of start_date
            if ((! Parameters.dayOfValidation.after(projectInfo.getStartDate())) &&
                (projectInfo.getApplyPPM() == 1))
			{ // Apply for new projects
                processEffort = Effort.getProcessEffortList(projectInfo, processEffortByStages);
			} 
            else {
                processEffort = Effort.getProcessEffortList(projectInfo, null, null);
            }
            
            final Vector processEffortPM = new Vector();
            double effort;
            float totalP = 0;
            float totalA = 0;
            boolean bl1 = false;
            boolean bl2 = false;
            for (int i = 0; i < processEffort.size(); i++) {
                final ProcessEffortInfo info = (ProcessEffortInfo)processEffort.get(i);
                final ProcessEffortPMInfo infoPM = new ProcessEffortPMInfo();
                infoPM.process = info.process;
                effort = (Double.isNaN(info.reEstimated)) ? info.estimated : info.reEstimated;
                if (!Double.isNaN(effort)) {
                    infoPM.plan1 = effort;
                    totalP += effort;
                    bl1 = true;
                }
                infoPM.deviation = info.deviation;
                infoPM.actual1 = info.actual;
                if (!Double.isNaN(info.actual)) {
                    totalA += info.actual;
                    bl2 = true;
                }
                processEffortPM.add(infoPM);
            }
            final String[] strArr = new String[2];
            strArr[0] = (bl1) ? CommonTools.decForm.format(totalP) : "N/A";
            strArr[1] = (bl2) ? CommonTools.decForm.format(totalA) : "N/A";
            Vector vtEffortDistributionByType = Effort.getEffortDistributionByType(prjID);
            Vector vtWeightedDefect = Defect.getWeightedDefect(prjID);
            Vector vtSeverityDefect = Defect.getSeverityDefect(prjID);
            TypeDefectInfo[] typeDefectArr = Defect.getTypeDefect(prjID);
            ConformityNCInfo[] confNCArr = Ncms.getConfNCDefect(prjID);
            Vector norm = Norms.getNormList(prjID);
            java.util.Date reportDate = new java.util.Date();
            Vector risk = Risk.getOccuredRisks(prjID, reportDate);
            Vector tool = Project.getToolList(prjID);
            Vector furtherWork = WorkProduct.getFurtherWorkList(prjID);
           // Vector defectPrevention = Defect.getDPTask(prjID);
            Vector skillSet = Project.getProjectSkillSet(prjID);
            Vector skillSet2 = Project.getProjectSkillSet2(prjID);
            Vector projectHistory = Report.getProjectHis(prjID);
            session.setAttribute("dar", dar);
            session.setAttribute("norm", norm);
            session.setAttribute("stage", stage);
            session.setAttribute("tool", tool);
            session.setAttribute("module", module);
            session.setAttribute("risk", risk);
            session.setAttribute("strArr", strArr);
            session.setAttribute("PMHeaderInfo", pmHeaderInfo);
            session.setAttribute("WOGeneralInfo", projectInfo);
            session.setAttribute("ProjectSizeInfo", projectSizeInfo);
            session.setAttribute("EffortInfo", effortInfo);
            session.setAttribute("CostTotalInfo", costTotalInfo);
            session.setAttribute("FinanceAmountTotal", String.valueOf(financeTotal));
            session.setAttribute("DeliverableList", deliverableList);
            session.setAttribute("WOPerformanceMetrics", performanceMetrics);
            session.setAttribute("WOStandardMetrics", standardMetrics);
            session.setAttribute("WOCustomeMetrics", customerMetrics);
            session.setAttribute("ProcessEffortPM", processEffortPM);
            session.setAttribute("EffortDistributionByType", vtEffortDistributionByType);
            session.setAttribute("WeightedDefect", vtWeightedDefect);
            session.setAttribute("SeverityDefect", vtSeverityDefect);
            session.setAttribute("TypeDefectArr", typeDefectArr);
            session.setAttribute("ConfNCArr", confNCArr);
            session.setAttribute("FurtherWork", furtherWork);
            session.setAttribute("defectPrevention", defectTask);
            session.setAttribute("ProjectSkillSet", skillSet);
            session.setAttribute("ProjectSkillSet2", skillSet2);
            session.setAttribute("ProjectHistory", projectHistory);
            
            
            ///
			final Vector vtProcess = Project.getProcessList();
			session.setAttribute("vtProcess", vtProcess);
            ///
            
            
            Fms1Servlet.callPage("postMortemReport.jsp" + index, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public final static void doUpdateFurtherWork(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final String fwID = request.getParameter("txtFurtherWorkID").trim();
			final String name = request.getParameter("txtItem").trim();
			final String result = request.getParameter("txtResult").trim();
			final String time = request.getParameter("txtTime").trim();
			final String note = request.getParameter("txtNote").trim();
			final String res = request.getParameter("txtResponsibility").trim();
			final FurtherWorkInfo info = new FurtherWorkInfo();
			info.fwID = Long.parseLong(fwID);
			info.name = name;
			info.result = result;
			info.note = note;
			info.responsibility = res;
			if (!time.equals("")) {
				info.time = Double.parseDouble(time);
			}
			WorkProduct.updateFurtherWork(info);
			doGetPostMortemReport(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doAddFurtherWork(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String name = request.getParameter("txtItem").trim();
			final String result = request.getParameter("txtResult").trim();
			final String time = request.getParameter("txtTime").trim();
			final String note = request.getParameter("txtNote").trim();
			final String res = request.getParameter("txtResponsibility").trim();
			final FurtherWorkInfo info = new FurtherWorkInfo();
			info.prjID = Long.parseLong(prjID);
			info.name = name;
			info.result = result;
			info.note = note;
			info.responsibility = res;
			if (!time.equals("")) {
				info.time = Float.parseFloat(time);
			}
			WorkProduct.addFurtherWork(info);
			doGetPostMortemReport(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	public final static void doAddDPTask(final HttpServletRequest request, final HttpServletResponse response, final int intFromWhere) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String item = request.getParameter("txtItem").trim();
			final String unit = request.getParameter("txtUnit").trim();
			final String plValue = request.getParameter("txtPlan").trim();
			// Add by HaiMM
			final String usl = request.getParameter("txtUSL");
			final String lsl = request.getParameter("txtLSL");

			          
            final String dpCause = request.getParameter("txtCause").trim();
			final DPTaskInfo info = new DPTaskInfo();
			info.prjID = Long.parseLong(prjID);
			info.item = item;
			info.unit = unit;
			info.planValue = CommonTools.parseDouble(plValue);
			// Add by HaiMM
			info.usl = CommonTools.parseDouble(usl);
			info.lsl = CommonTools.parseDouble(lsl);
            info.dpCause = dpCause;
			Defect.addDPTask(info);
			
			/** Comment by HaiMM
			if (intFromWhere == QUALITY_OBJECTIVE_CALLER)
				QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
			else
				doGetPostMortemReport(request, response, "");
			**/
			// Add by HaiMM
			String source = (String)session.getAttribute("SourcePage");
			StageInfo stageInfo = (StageInfo)session.getAttribute("StageInfo");
			if (source.equalsIgnoreCase("1")) {
				Vector dpVt;
				if(stageInfo==null)
				{
					dpVt = new Vector();	
				}
				else 
				{
					dpVt = Defect.getDPTask(Integer.parseInt(prjID),stageInfo.milestoneID);	
				}
				session.setAttribute("defectPrevention", dpVt);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
			}
			else {
				WorkOrderCaller.doLoadWOPerformanceList(request, response, "");
			}			
		}
		catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	/** Get data entered, then call to Add method to Database  
	 * @param request
	 * @param response
	 * @param intFromWhere 
     * @author Thimb
     * Created on Mar 03, 2006
	 */
	public final static void doAddDarPlan(final HttpServletRequest request, final HttpServletResponse response, final int intFromWhere) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final DARPlanInfo info = new DARPlanInfo();
			final String item = request.getParameter("txtItem");                 
			final String doer = request.getParameter("cmbConductor");
			final Date planDate = CommonTools.parseSQLDate(request.getParameter("txtPStartD"));
			final Date actualDate = CommonTools.parseSQLDate(request.getParameter("txtPEndD"));                
			String cause =  request.getParameter("txtCause");                           				
			final long pjrID = Long.parseLong(prjID);
			info.darItem = item;
			info.doer = doer;
			info.planDate = planDate;
			info.actualDate = actualDate;
			info.darCause = cause;				
			Dar.addDarPlan(info, pjrID);				
			if (intFromWhere == QUALITY_OBJECTIVE_CALLER) {
                QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
			} else {
                doGetPostMortemReport(request, response, "");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public final static void doDeleteFurtherWork(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final String fwID = request.getParameter("txtFurtherWorkID").trim();
			final long id = Long.parseLong(fwID);
			WorkProduct.deleteFurther(id);
			doGetPostMortemReport(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doDeleteDPTask(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final int intFromWhere) {
		try {
			
			HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String dpTaskID = request.getParameter("dpTaskID").trim();
			 long id = Long.parseLong(dpTaskID);
			Defect.deleteDPTask(id);
			/** Comment by HaiMM
			if (intFromWhere == QUALITY_OBJECTIVE_CALLER) 
				QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
			else
				doGetPostMortemReport(request, response, "");
			**/
			// Add by HaiMM
			
			StageInfo stageInfo = (StageInfo)session.getAttribute("StageInfo");
			String source = (String)session.getAttribute("SourcePage");
			Vector dpVt;
			if (source.equalsIgnoreCase("1")) {
				
				if(stageInfo==null)
				{
					dpVt = new Vector();	
				}
				else 
				{
					dpVt = Defect.getDPTask(Integer.parseInt(prjID),stageInfo.milestoneID);	
				}
				session.setAttribute("defectPrevention", dpVt);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
				}
			else {
				
				if(stageInfo==null)
				{
					dpVt = new Vector();	
				}
				else 
				{
					dpVt = Defect.getDPTask(Integer.parseInt(prjID),stageInfo.milestoneID);	
				}
				session.setAttribute("defectPrevention", dpVt);
				Fms1Servlet.callPage("woPerformanceView.jsp",request,response);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doUpdateDPTask(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final int intFromWhere) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String dpTaskID = request.getParameter("dpTaskID");
			final String item = request.getParameter("txtItem");
			final String unit = request.getParameter("txtUnit");
			final String plValue = request.getParameter("txtPlan");
			// Add by HaiMM
			final String usl = request.getParameter("txtUSL");
			final String lsl = request.getParameter("txtLSL");

			final String acValue = request.getParameter("txtActual");
            final String dpCause = request.getParameter("txtCause");
			final DPTaskInfo info = new DPTaskInfo();
			info.prjID = Long.parseLong(prjID);
			info.dptaskID = Long.parseLong(dpTaskID);
			info.item = item;
			info.unit = unit;
			info.planValue = CommonTools.parseDouble(plValue);            
			// Add by HaiMM
			info.usl = CommonTools.parseDouble(usl);
			info.lsl = CommonTools.parseDouble(lsl);
			
           	info.actualValue = CommonTools.parseDouble(acValue);                        
            info.dpCause = dpCause;
			/** Comment by HaiMM
			if (intFromWhere == QUALITY_OBJECTIVE_CALLER){
				Defect.updateDPTask(info);
				QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
			}
			else{
				Defect.updateADPTask(info);
				if (intFromWhere ==MILESTONE_CALLER){
					doGetMilestone(request, response);
				}
				else{
					doGetPostMortemReport(request, response, "");
				}
			}
			**/
			
			// Add by HaiMM
			
			Defect.updateDPTask(info);
			StageInfo stageInfo = (StageInfo)session.getAttribute("StageInfo");
			String source = (String)session.getAttribute("SourcePage");
			Vector dpVt;
			if (source.equalsIgnoreCase("1")) {
				
				if(stageInfo==null)
				{
					dpVt = new Vector();	
				}
				else 
				{
					dpVt = Defect.getDPTask(Integer.parseInt(prjID),stageInfo.milestoneID);	
				}
				session.setAttribute("defectPrevention", dpVt);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
				}
			else {
				if(stageInfo==null)
				{
					dpVt = new Vector();	
				}
				else 
				{
					dpVt = Defect.getDPTask(Integer.parseInt(prjID),stageInfo.milestoneID);	
				}
				session.setAttribute("defectPrevention", dpVt);
				Fms1Servlet.callPage("woPerformanceView.jsp",request,response);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	/**
     * Get Dar_Plan_ID, then call delete method
	 * @param request
	 * @param response
	 * @param intFromWhere
     * @author Thimb
     * Created on Mar 03, 2006
	 */
	public final static void doDeleteDarPlan(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final int intFromWhere) {
		try {
			final String darPlanID = request.getParameter("darPlanID").trim();
			final long id = Long.parseLong(darPlanID);				
			Dar.deleteDarPlan(id);		
			if (intFromWhere == QUALITY_OBJECTIVE_CALLER)
				QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
			else
				doGetPostMortemReport(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
   
	/** 
     * get Data from pview page, then Update to database
	 * @param request
	 * @param response
	 * @param intFromWhere
     * @author Thimb
     * Created on Mar 03, 2006
	 */
	public final static void doUpdateDarPlan(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final int intFromWhere) {
        try {
        	final HttpSession session = request.getSession();
        	final String prjID = (String) session.getAttribute("projectID");
        	final DARPlanInfo info = new DARPlanInfo();				
        	final String darPlanID = request.getParameter("darPlanID");
        	final String item = request.getParameter("txtItem");
        	final String doer = request.getParameter("cmbConductor");
        	final Date planDate = CommonTools.parseSQLDate(request.getParameter("txtPStartD"));
        	final Date actualDate = CommonTools.parseSQLDate(request.getParameter("txtPEndD"));            
        	final String cause =  request.getParameter("txtCause");                       
        	final long pjrID = Long.parseLong(prjID);
        	info.darPlanID = Long.parseLong(darPlanID);
        	info.darItem = item;
        	info.doer = doer;
        	info.planDate = planDate;
        	info.actualDate = actualDate;
        	info.darCause = cause;			        								
        	if (intFromWhere == QUALITY_OBJECTIVE_CALLER){                
                Dar.updateDarPlan(info, pjrID);						
        		QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
        	}else{                
        		Dar.updateRepDarPlan(info);
        		if (intFromWhere ==MILESTONE_CALLER){
        			doGetMilestone(request, response);
        		}else{
        			doGetPostMortemReport(request, response, "");
          	    }				
        	}		
        }	
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
	public final static void doGetSkillDetail(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();

			final String assignmentId = request.getParameter("assignmentID").trim();
			final String assignmentName = request.getParameter("fullName").trim();
			final long lAssignmentId = Long.parseLong(assignmentId);

			final Vector vtSkills = Project.getSkillDetail(lAssignmentId);

			session.setAttribute("vtSkills", vtSkills);
			session.setAttribute("assignmentName", assignmentName);
			session.setAttribute("assignmentId", assignmentId);

			Fms1Servlet.callPage("skillDetail.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doUpdateSkill(final HttpServletRequest request, final HttpServletResponse response) {
		try {

			final String prjSkillId = request.getParameter("projectSkillId").trim();
			final String skill = request.getParameter("txtSkill").trim();
			final String point = request.getParameter("txtPoint").trim();
			final String comment = request.getParameter("txtComment").trim();

			final SkillInfo info = new SkillInfo();

			info.projectSkillId = Long.parseLong(prjSkillId);
			info.skill = skill;
			info.point = Integer.parseInt(point);
			info.skillComment = comment;

			Project.updateSkill(info);

			doGetSkillDetail(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doDeleteSkill(final HttpServletRequest request,final HttpServletResponse response) {
		try {
			final String prjSkillId = request.getParameter("projectSkillId").trim();
			final long projectSkillId = Long.parseLong(prjSkillId);

			Project.deleteSkill(projectSkillId);

			doGetSkillDetail(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doAddSkill(final HttpServletRequest request, final HttpServletResponse response) {
		try {

			final HttpSession session = request.getSession();

			final Vector vtProcess = Project.getProcessList();
			session.setAttribute("vtProcess", vtProcess);

			Fms1Servlet.callPage("skillAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doAddSkill2(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String assignmentID = request.getParameter("assignmentID").trim();
			final String processId = request.getParameter("cmbProcess").trim();
			final String skill = request.getParameter("txtSkill").trim();
			final String point = request.getParameter("txtPoint").trim();
			final String comment = request.getParameter("txtComment").trim();

			final SkillInfo info = new SkillInfo();

			info.assignmentId = Long.parseLong(assignmentID);
			info.processId = Long.parseLong(processId);
			info.skill = skill;
			info.point = Integer.parseInt(point);
			info.skillComment = comment;

			Project.addSkill(info);

			doGetSkillDetail(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public final static void doDeleteTeam(final HttpServletRequest request, final HttpServletResponse response) {
		try{
			long prjID = Long.parseLong((String) request.getSession().getAttribute("projectID"));
			String pos = request.getParameter("pos");
			String processId = request.getParameter("processId");
			Vector skillSet = (Vector) request.getSession().getAttribute("ProjectSkillSet");
			SkillInfo tempDel = (SkillInfo)skillSet.get(Integer.parseInt(pos.trim()));
			Project.delSkillNoPrjSId(tempDel,Integer.parseInt(processId.trim()));
			
			skillSet = Project.getProjectSkillSet(prjID);
			String value = (String)request.getSession().getAttribute("value");
			StringTokenizer token = new StringTokenizer(value.trim(),",");
			Vector id = new Vector();
				while (token.hasMoreElements()) {
					id.addElement(token.nextToken().toString());
				}
				Vector newSkillSet = new Vector();
				for (int i=0;i<skillSet.size();i++){
				SkillInfo temp = (SkillInfo) skillSet.get(i);
				for (int j=0;j<id.size();j++) {
					String tempString = (String) id.get(j);
					if (temp.assignmentId == Long.parseLong(tempString)) {
						newSkillSet.addElement(temp);
						break;
					}
				}
			}
			request.getSession().setAttribute("ProjectSkillSet",newSkillSet);
			Fms1Servlet.callPage("postMorternTeamBatchUpdate.jsp",request,response);
		}
		catch(Exception e){
		}
	}
	public final static void doUpdateTeam(final HttpServletRequest request, final HttpServletResponse response) {
		try{
			String value = request.getParameter("value");
			request.getSession().setAttribute("value",value);
			StringTokenizer token = new StringTokenizer(value.trim(),",");
			Vector id = new Vector();
			while (token.hasMoreElements()) {
				id.addElement(token.nextToken().toString());
			}
			Vector newSkillSet = new Vector();
			Vector skillSet = (Vector) request.getSession().getAttribute("ProjectSkillSet");
			for (int i=0;i<skillSet.size();i++){
				SkillInfo temp = (SkillInfo) skillSet.get(i);
				for (int j=0;j<id.size();j++) {
					String tempString = (String) id.get(j);
					if (temp.assignmentId == Long.parseLong(tempString)) {
						newSkillSet.addElement(temp);
						break;
					}
				}
			}
			request.getSession().setAttribute("ProjectSkillSet",newSkillSet);
			Fms1Servlet.callPage("postMorternTeamBatchUpdate.jsp",request,response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public final static void doBatchUpdatePostMortem(final HttpServletRequest request, final HttpServletResponse response) {
		try{
			final String[] process = request.getParameterValues("cmbProcess");
			final String[] skill = request.getParameterValues("skill");
			final String[] point  = request.getParameterValues("point");
			final String[] note = request.getParameterValues("Note");
			final String[] assId = request.getParameterValues("assId");
			final String[] isUpdate = request.getParameterValues("isUpdate");
			final String[] vtId = request.getParameterValues("vtId");
			final String[] processId = request.getParameterValues("processId");
			
			
			for (int i=0;i<process.length;i++){
				if (Integer.parseInt(vtId[i].trim())==0) {
					Vector vtSkills = Project.getSkillDetail(Long.parseLong(assId[i].trim()));
					for (int j=0;j<vtSkills.size();j++){
						SkillInfo temp = (SkillInfo)vtSkills.get(j);
						Project.deleteSkill(temp.projectSkillId);
					}
				}
			}
			
			for (int i=0;i<process.length;i++){
				if (isUpdate[i].equalsIgnoreCase("1")) {
					SkillInfo info = new SkillInfo();
					info.assignmentId = Long.parseLong(assId[i].trim());
					info.processId = Long.parseLong(processId[i].trim());
					info.skill = skill[i].trim();
					info.point = Integer.parseInt(point[i]);
					info.skillComment = note[i].trim();
	
					Project.addSkill(info);	
				}
				else {
					if (!skill[i].trim().equals("")){
						SkillInfo info = new SkillInfo();
						info.assignmentId = Long.parseLong(assId[i].trim());
						info.processId = Long.parseLong(process[i].trim());
						info.skill = skill[i].trim();
						info.point = Integer.parseInt(point[i]);
						info.skillComment = note[i].trim();
	
						Project.addSkill(info);	
					}
					
				}
				
			}
			
			doGetPostMortemReport(request,response);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public final static void doRefreshSkillList(final HttpServletRequest request,final HttpServletResponse response) {
		try {
			doGetPostMortemReport(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Header of post-mortem
	 */
	public final static void doUpdatePmHeader(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String reviewers = request.getParameter("txtReviewer").trim();
			final String approvers = request.getParameter("txtApprover").trim();
			final PmReportHeaderInfo info = new PmReportHeaderInfo();
			info.prjID = Long.parseLong(prjID);
			info.reviewers = reviewers;
			info.approvers = approvers;
			Report.updatePmHeader(info);
			doGetPostMortemReport(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doUpdateHis(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();

			final String prjHisId = request.getParameter("prjHisId").trim();
			final long lPrjHisId = Long.parseLong(prjHisId);

			final ProjectHisInfo projectHisInfo = Report.getProjectHis2(lPrjHisId);

			session.setAttribute("projectHisInfo", projectHisInfo);

			Fms1Servlet.callPage("prjHisDetails.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doSaveHis(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			ProjectHisInfo prjHisInfo = new ProjectHisInfo();

            prjHisInfo.projectHisId = Long.parseLong(request.getParameter("prjHisId").trim());
            prjHisInfo.eventDate = CommonTools.parseSQLDate(request.getParameter("txtDate").trim());
            prjHisInfo.events = request.getParameter("txtEvent").trim();
			final String comments = request.getParameter("txtComment");
			prjHisInfo.comments = (comments == null) ? "" : comments.trim();

			Report.updateProjectHis(prjHisInfo);

			doGetPostMortemReport(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doDeleteHis(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String prjHisId = request.getParameter("prjHisId").trim();
			final long lPrjHisId = Long.parseLong(prjHisId);

			Report.deleteProjectHis(lPrjHisId);

			doGetPostMortemReport(request, response, "");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doAddnewHis(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();

			ProjectHisInfo prjHisInfo = new ProjectHisInfo();

			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			prjHisInfo.projectId = prjID;

			final String eventDate = request.getParameter("txtDate").trim();
			prjHisInfo.eventDate = CommonTools.parseSQLDate(eventDate);

			final String event = request.getParameter("txtEvent").trim();
			prjHisInfo.events = event;

			final String comments = request.getParameter("txtComment");
			prjHisInfo.comments = (comments == null) ? "" : comments.trim();

			Report.addProjectHis(prjHisInfo);

			doGetPostMortemReport(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Causal analysis of post-mortem
	 */
	public final static void doUpdatePmCause(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			String[] arrCause = request.getParameterValues("txtCause");
			String[] arrMetric = request.getParameterValues("mID");
			String[] arrCusCause = request.getParameterValues("txtCusCause");
			String[] arrCusMetric = request.getParameterValues("cmID");
			Metrics.updateCausal(Long.parseLong(prjID), arrMetric, arrCause);
			if(!(null == arrCusMetric)){
				Metrics.updateCusMetricNote(Long.parseLong(prjID), arrCusMetric, arrCusCause);
			}
			doGetPostMortemReport(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Causal analysis export to practice/lesson
	 */
	public final static void doExportCausal(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			Vector normInfoList = (Vector) session.getAttribute("norm");
			int color;
			NormInfo normInfo;
			for(int i = 0; i<normInfoList.size(); i++){
	        	normInfo = (NormInfo)normInfoList.get(i);
	        	//only display metrics out of norms (only out of norm are formated by Color)
	        	if (normInfo.cause!=null && normInfo.cause.trim().length()!=0){
	        		color=Color.getColor(normInfo.actualValue, normInfo.lcl, normInfo.ucl, normInfo.colorType);
	        		if (color!=Color.NOCOLOR){
				        final PracticeInfo pracInfo = new PracticeInfo();
						pracInfo.projectId = prjID;
						pracInfo.practiceId = 0; //Integer.parseInt(practID);
						pracInfo.scenario = normInfo.normID+" "+normInfo.normName+" ("+normInfo.normUnit
							+")\n Planned: "+CommonTools.formatDouble(normInfo.plannedValue)
							+" Actual: "+CommonTools.formatDouble(normInfo.actualValue)
							+"\n Norm: "+CommonTools.formatDouble(normInfo.average)
							+" Norm deviation: "+CommonTools.formatDouble(normInfo.normDeviation);
						pracInfo.practice = normInfo.cause;
						pracInfo.category =" ";
						pracInfo.type = (color==Color.BADMETRIC)?PracticeInfo.TYPE_LESSON:PracticeInfo.TYPE_PRACTICE;
						Assets.addPractice(pracInfo);
	        		}
	        	}
			}
			PracticeCaller.doGetPracticeList(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//anhtv08-start
	public static void updateSpecificObjective(HttpServletRequest request, HttpServletResponse response,String index)
	{
		HttpSession session = request.getSession();
		Vector cusMetricList= (Vector)session.getAttribute("cusMetricList");
		Vector cusMetricListTemp=new Vector();
		String[] actualValueList=request.getParameterValues("actualValue");
		if(cusMetricList!=null)
		{
			for(int i=0;i<cusMetricList.size();i++)
			{
				WOCustomeMetricInfo woCusMetricInfo = (WOCustomeMetricInfo)cusMetricList.elementAt(i);
				double actualValue= CommonTools.parseDouble(actualValueList[i]);
				woCusMetricInfo.actualValue=actualValue;		
				cusMetricListTemp.add(i,woCusMetricInfo);
			}
		}
		
		// update to customerMetric to database
		session.setAttribute("cusMetricList",cusMetricListTemp);
		Metrics.updateCusMetricActualValue(cusMetricListTemp);
		Fms1Servlet.callPage("milestone.jsp"+index,request,response);
	}
	// added anhtv08-start
	public static void updateDefect(HttpServletRequest request, HttpServletResponse response,String index)
	{
		HttpSession session = request.getSession();
		Vector defectList= (Vector)session.getAttribute("defectPrevention");
		Vector defectListTemp=new Vector();
		String[] actualValueList=request.getParameterValues("actualValue");
		if(defectList!=null)
		{
			for(int i=0;i<defectList.size();i++)
			{
				DPTaskInfo dPTaskInfo = (DPTaskInfo)defectList.elementAt(i);
				double actualValue= CommonTools.parseDouble(actualValueList[i]);
				dPTaskInfo.actualValue=actualValue;		
				defectListTemp.add(i,dPTaskInfo);
			}
		}
		
		// update to customerMetric to database
		session.setAttribute("defectPrevention",defectListTemp);
		
		Defect.updateDefectActualValue(defectListTemp);
		Fms1Servlet.callPage("milestone.jsp"+index,request,response);
	}	
}