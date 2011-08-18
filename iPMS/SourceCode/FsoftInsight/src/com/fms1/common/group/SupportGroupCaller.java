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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.*;
import com.fms1.common.*;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.*;
import com.fms1.tools.CommonTools;
import com.fms1.tools.ReportMonth;
import com.fms1.web.*;
/**
 * Support group(PQA/SQA) page logic.
 * @author phuongnt
 */
public class SupportGroupCaller implements Constants {
	public static void doGetSQADefectOriginReport(HttpServletRequest request, HttpServletResponse response) {
		try {
			Date startDate;
			Date endDate;
			long nWorkUnitID;
			HttpSession session = request.getSession();
			String strStartDate = (String) request.getParameter("startDate");
			String strEndDate = (String) request.getParameter("endDate");
			String strWorkUnitID = (String) request.getParameter("groupID");
			if ((strStartDate == null) || (strEndDate == null)) {
				java.util.Date today = new java.util.Date();
				endDate = new Date(today.getTime());
				today.setDate(today.getDate() - 6);
				startDate = new Date(today.getTime());
			}
			else {
				startDate = CommonTools.parseSQLDate(strStartDate);
				endDate = CommonTools.parseSQLDate(strEndDate);
			}
			if (strWorkUnitID == null) {
				nWorkUnitID = 0;
				strWorkUnitID = "0";
			}
			else {
				nWorkUnitID = Long.parseLong(strWorkUnitID);
			}
			Vector vtDefectOriginInfoList =
				SQA.getSQADefectOrigin(Project.getProjectListForSQA(startDate, endDate, nWorkUnitID), startDate, endDate);
			session.setAttribute("defectOriginList", vtDefectOriginInfoList);
			ArrayList defOrgParetoList = SQA.getSQADefectOriginPareto(vtDefectOriginInfoList);
			session.setAttribute("defectOriginParetoList", defOrgParetoList);
			session.setAttribute("startDate", CommonTools.dateFormat(startDate));
			session.setAttribute("endDate", CommonTools.dateFormat(endDate));
			session.setAttribute("groupID", strWorkUnitID);
			Vector vtGroup = WorkUnit.getChildrenGroups(Parameters.FSOFT_WU);
			session.setAttribute("grList", vtGroup);
			Fms1Servlet.callPage("Group/SQADefectOrigin.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doGetSQADefectTypeReport(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Date startDate;
			Date endDate;
			long nWorkUnitID;
			HttpSession session = request.getSession();
			String strStartDate = (String) request.getParameter("startDate");
			String strEndDate = (String) request.getParameter("endDate");
			String strWorkUnitID = (String) request.getParameter("groupID");
			if ((strStartDate == null) || (strEndDate == null)) {
				java.util.Date today = new java.util.Date();
				endDate = new Date(today.getTime());
				today.setDate(today.getDate() - 6);
				startDate = new Date(today.getTime());
			}
			else {
				startDate = CommonTools.parseSQLDate(strStartDate);
				endDate = CommonTools.parseSQLDate(strEndDate);
			}
			if (strWorkUnitID == null) {
				nWorkUnitID = 0;
				strWorkUnitID = "0";
			}
			else {
				nWorkUnitID = Long.parseLong(strWorkUnitID);
			}
			Vector vtDefectTypeInfoList;
			vtDefectTypeInfoList = SQA.getSQADefectType(Project.getProjectListForSQA(startDate, endDate, nWorkUnitID), startDate, endDate);
			session.setAttribute("defectTypeList", vtDefectTypeInfoList);
			ArrayList defTypeParetoList = SQA.getSQADefectTypePareto(vtDefectTypeInfoList);
			session.setAttribute("defectTypeParetoList", defTypeParetoList);
			ArrayList defFunctionalityParetoList = SQA.getSQADefectFunctionalityPareto(vtDefectTypeInfoList);
			session.setAttribute("defectFunctionalityParetoList", defFunctionalityParetoList);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
			session.setAttribute("startDate", sdf.format(startDate));
			session.setAttribute("endDate", sdf.format(endDate));
			session.setAttribute("groupID", strWorkUnitID);
			Vector vtGroup = WorkUnit.getChildrenGroups(Parameters.FSOFT_WU);
			session.setAttribute("grList", vtGroup);
			Fms1Servlet.callPage("Group/sqaDefectType.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Called from the home page (month- year)
	 * 
	 */
	public static final void SQAhomeCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			String strmonth = request.getParameter("month");
			String stryear = request.getParameter("year");
			int year, month;
			ReportMonth rp;
			
			int curYear, curYear2;
			int curMonth, lastMonth;

			if (strmonth == null || stryear == null) {
				rp = new ReportMonth().getPreviousMonth();
				year = rp.getYear();
				month = rp.getMonth();
			}
			else {
				year = Integer.parseInt(stryear);
				month = Integer.parseInt(strmonth);
				rp = new ReportMonth(month, year);
			}
			
			Date startDate = rp.getFirstDayOfMonth();
			Date endDate = rp.getLastDayOfMonth();
			Vector result = SQAhomeCaller3(startDate, endDate, rp, workUnitID);

			if (strmonth == null || stryear == null) {
				curYear = rp.getYear();
				curMonth = rp.getMonth();

				ReportMonth prevMonth = rp.getPreviousMonth();
				lastMonth = prevMonth.getMonth();
				curYear2 = prevMonth.getYear();
			}
			else {
				curYear = rp.getYear();
				curMonth = rp.getMonth();

				ReportMonth prevMonth = rp.getPreviousMonth();
				lastMonth = prevMonth.getMonth();
				curYear2 = prevMonth.getYear();
			}

			final Vector vt = new Vector();
			GroupPointBAInfo gpointBAInfo = null;
			gpointBAInfo = Report.getGroupPointBA(curMonth, Parameters.SQA_WU, curYear);
			if (gpointBAInfo != null) {
				vt.addElement(gpointBAInfo);
			}

			gpointBAInfo = Report.getGroupPointBA(lastMonth, Parameters.SQA_WU, curYear2);
			if (gpointBAInfo != null) {
				vt.addElement(gpointBAInfo);
			}
			
			int numGroupBA = 0;
			numGroupBA = WorkUnit.getNumSupportGroup();
			
			session.setAttribute("numBAGroup", String.valueOf(numGroupBA));

			request.setAttribute("metrics", result);
			request.setAttribute("year", Integer.toString(year));
			request.setAttribute("month", Integer.toString(month));

			request.setAttribute("lastMonth1", CommonTools.getMonth(curMonth) + "-" + String.valueOf(curYear));
			request.setAttribute("lastMonth2", CommonTools.getMonth(lastMonth) + "-" + String.valueOf(curYear2));
			request.setAttribute("groupPoint", vt);

			Fms1Servlet.callPage("Group/welcomeSQA.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Called from the report page (fromdate- todate)
	 * 
	 */
	private static final void SQAhomeCaller2(final HttpServletRequest request) {
		final HttpSession session = request.getSession();
		try {
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			String fromDateStr = request.getParameter("fromDate");
			String toDateStr = request.getParameter("toDate");
			//called 1st time
			java.sql.Date fromDate;
			java.sql.Date toDate;
			ReportMonth rp;
			if (toDateStr == null && fromDateStr == null) {
				rp = new ReportMonth();
				rp.moveToPreviousMonth();
				fromDate = rp.getFirstDayOfMonth();
				toDate = rp.getLastDayOfMonth();
			}
			else {
				fromDate = CommonTools.parseSQLDate(fromDateStr);
				toDate = CommonTools.parseSQLDate(toDateStr);
				if (toDate!=null)
					CommonTools.setJustBeforeMidnight(toDate);
				rp = new ReportMonth(toDate);
			}
			Vector result = SQAhomeCaller3(fromDate, toDate, rp, workUnitID);
			request.setAttribute("metrics", result);
			request.setAttribute("fromDate", fromDate);
			request.setAttribute("toDate", toDate);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final Vector SQAhomeCaller3(Date startDate, Date endDate, ReportMonth rp, long workUnitID) {
		NormPlanInfo normPlanInfo =
			NormCaller.getPlannedNorms(((rp.getMonth() > 6) ? "S2" : "S1"), rp.getYear(), workUnitID, MetricDescInfo.GR_SET_SQA_HOME_PAGE);
		Vector tasks = Tasks.getTasks(startDate, endDate,null, null, workUnitID);
		int size = normPlanInfo.rows.size();
		NormPlanInfo.Row row;
		MetricInfo inf;
		Vector result = new Vector();
		for (int i = 0; i < size; i++) {
			row = (NormPlanInfo.Row) normPlanInfo.rows.elementAt(i);
			inf = new MetricInfo();
			inf.id = row.strMetricID;
			inf.name = row.metricName;
			inf.unit = row.metricUnit;
			inf.plannedValue = row.norm;
			inf.actualValue = SQA.getMetric(row.metricID, startDate, endDate, tasks);
			inf.deviation = CommonTools.metricDeviation(inf.plannedValue, Double.NaN, inf.actualValue);
			result.add(inf);
		}
		return result;
	}
	public static final void PQAhomeCaller(final HttpServletRequest request, final HttpServletResponse response) {
		PQAhomeCaller2(request);
		Fms1Servlet.callPage("Group/welcomePQA.jsp", request, response);
	}
	private static final void PQAhomeCaller2(final HttpServletRequest request) {
		String fromDateStr = request.getParameter("fromDate");
		String toDateStr = request.getParameter("toDate");
		//called 1st time
		java.sql.Date fromDate;
		java.sql.Date toDate;
		ReportMonth rp;
		int intTemp;
		if (toDateStr == null && fromDateStr == null) {
			rp = new ReportMonth();
			rp.moveToPreviousMonth();
			fromDate = rp.getFirstDayOfMonth();
			toDate = rp.getLastDayOfMonth();
		}
		else {
			fromDate = CommonTools.parseSQLDate(fromDateStr);
			toDate = CommonTools.parseSQLDate(toDateStr);
			if (toDate!=null)
				CommonTools.setJustBeforeMidnight(toDate);
			rp = new ReportMonth(toDate);
		}
		NormPlanInfo normPlanInfo =
			NormCaller.getPlannedNorms(((rp.getMonth() > 6) ? "S2" : "S1"), rp.getYear(), Parameters.FSOFT_WU, MetricDescInfo.GR_PROCESS);
		NormPlanInfo.Row row;
		MetricInfo inf;
		Vector result = new Vector();
		Vector closedNCs = Ncms.getClosedNCs(fromDate, toDate);
		Vector createdNCs = Ncms.getCreatedNCs(fromDate, toDate);
		
		for (int i = 0; i < normPlanInfo.rows.size(); i++) {
			row = (NormPlanInfo.Row) normPlanInfo.rows.elementAt(i);
			inf = new MetricInfo();
			inf.id = row.strMetricID;
			inf.plannedValue = row.norm;
			inf.name = row.metricName;
			inf.unit = row.metricUnit;
			switch (row.metricID) {
				case MetricDescInfo.PROCESS_COMPLIANCE :
					/*2 types of internal audit, one is for groups (from manual tasks)
					one is for projects (QA activities)*/
					int [] inspection={TaskInfo.getTypeID(TaskInfo.INTERNAL_AUDIT)};
					Vector tasks=Tasks.getManualTaskListClosed(fromDate, toDate,inspection );
					intTemp = QualityObjective.getQualityObjectiveCount(fromDate, toDate, QCActivityInfo.INTERNAL_AUDIT);
     				intTemp+=tasks.size();
					
					if (intTemp != 0)
						inf.actualValue = Ncms.getNCCount(fromDate, toDate) / (double) intTemp;
					break;
				case MetricDescInfo.REPEATED_NCS :
					inf.actualValue = Ncms.getRepeatedNC(createdNCs);
					break;
				case MetricDescInfo.CP_TIME :
					inf.actualValue = Ncms.getCPTime(closedNCs);
					break;
				case MetricDescInfo.NCS_IN_TIME :
					inf.actualValue = Ncms.getNCTimeliness(closedNCs);
					break;
				case MetricDescInfo.PQA_RESPONSE_TIME :
					inf.actualValue = Ncms.getPQAResponseTime(fromDate, toDate);
					break;
				case MetricDescInfo.PQA_FIX_TIME :
					inf.actualValue = Ncms.getPQAFixTime(fromDate, toDate);
					break;
				case MetricDescInfo.PQA_SATISFACTION :
					inf.actualValue = Metrics.getMetric("PQA", MetricDescInfo.PQA_SATISFACTION, toDate).actualValue;
					break;
				case MetricDescInfo.OVERDUE_TARGETS :
					inf.actualValue = Tasks.getOverdueTasks(fromDate, toDate, TaskInfo.typesPlan);
					break;
				case MetricDescInfo.PQA_TIMELINESS :
					inf.actualValue = Tasks.getPQATimeliness(fromDate, toDate);
					break;
				case MetricDescInfo.FEASIBLE_DECISIONS :
					inf.actualValue = Tasks.getFeasibility(fromDate, toDate);
					break;
				case MetricDescInfo.SATISFIED_INDICATORS :
					inf.actualValue = Metrics.getMetric("PQA", MetricDescInfo.SATISFIED_INDICATORS, toDate).actualValue;
					break;
			}
			if (inf.plannedValue != 0)
				inf.deviation = (inf.actualValue - inf.plannedValue) * 100d / inf.plannedValue;
			result.add(inf);
		}

		int curYear, curYear2;
		int curMonth, lastMonth;

		if (fromDateStr != null){
			ReportMonth actualMonth = new ReportMonth(CommonTools.parseSQLDate(fromDateStr));
			curYear = actualMonth.getYear();
			curMonth = actualMonth.getMonth();

			ReportMonth prevMonth = actualMonth.getPreviousMonth();
			lastMonth = prevMonth.getMonth();
			curYear2 = prevMonth.getYear();
		}
		else
		{
			ReportMonth actualMonth = new ReportMonth();
			curYear = actualMonth.getYear();
			curMonth = actualMonth.getMonth();

			ReportMonth prevMonth = actualMonth.getPreviousMonth();
			lastMonth = prevMonth.getMonth();
			curYear2 = prevMonth.getYear();
		}

		final Vector vt = new Vector();
		GroupPointBAInfo gpointBAInfo = null;
		gpointBAInfo = Report.getGroupPointBA(curMonth, Parameters.PQA_WU, curYear);
		if (gpointBAInfo != null) {
			vt.addElement(gpointBAInfo);
		}

		gpointBAInfo = Report.getGroupPointBA(lastMonth, Parameters.PQA_WU, curYear2);
		if (gpointBAInfo != null) {
			vt.addElement(gpointBAInfo);
		}

		int numGroupBA = 0;
		numGroupBA = WorkUnit.getNumSupportGroup();
			
		request.setAttribute("numBAGroup", String.valueOf(numGroupBA));
		
		request.setAttribute("lastMonth1", CommonTools.getMonth(curMonth) + "-" + String.valueOf(curYear));
		request.setAttribute("lastMonth2", CommonTools.getMonth(lastMonth) + "-" + String.valueOf(curYear2));

		request.setAttribute("fromDate", fromDate);
		request.setAttribute("toDate", toDate);
		request.setAttribute("metrics", result);
		request.setAttribute("groupPoint", vt);
	}
	public static final void getSupportReport(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
		if (workUnitID == Parameters.SQA_WU)
			SQAhomeCaller2(request);
		else
			PQAhomeCaller2(request);
		IssueCaller.issueListCaller2(request, response);
		MonitoringCaller.reportTaskCaller(request, response);
		Fms1Servlet.callPage("Group/PQAReport.jsp", request, response);
	}
}
