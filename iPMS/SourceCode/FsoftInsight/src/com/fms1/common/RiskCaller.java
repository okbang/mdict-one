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
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fms1.infoclass.*;
import com.fms1.tools.*;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.Parameters; // Added by LamNT

import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

/**
 * Risk pages
 *@author: Hoang My Duc
 */
public final class RiskCaller {
	/**
	 * provide a list of risks for riskList.jsp
	 * @author: Hoang My Duc
	 */

	private static Object form;
	public static final void riskListInitCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		session.setAttribute("riskBookmark", "0");
		riskListCaller(request, response);
	}
	
	public static final void listOtherRiskCaller(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String strWuID = (String) request.getParameter("cboGroup");
			String strIsPlaned = request.getParameter("cboIsPlaned");
			String strProcess = request.getParameter("cboProcess");
			String strFromDate = request.getParameter("fromDate");
			String strToDate = request.getParameter("toDate");
			String customer = request.getParameter("customer");
			String strCurrentProject = request.getParameter("cboPrj");
			java.sql.Date fromDate;
			java.sql.Date toDate;
			long lWuID = Parameters.FSOFT_WU; // FSOFT by default
			if (strWuID != null)			
				lWuID = WorkUnit.getWorkUnitInfo(strWuID).workUnitID;
				
			ReportMonth rm = new ReportMonth();
			if (strFromDate == null)
				strFromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2, 4);
			if (strFromDate.equalsIgnoreCase(""))
				strFromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2, 4);
			fromDate = CommonTools.parseSQLDate(strFromDate);
			if (strToDate == null)
				strToDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2, 4);
			if (strToDate.equalsIgnoreCase(""))
				strToDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2, 4);
			toDate = CommonTools.parseSQLDate(strToDate);
			String strWhere = "";
			if (strIsPlaned != null) {
				if (!strIsPlaned.equalsIgnoreCase("-1")) {
					strWhere = strWhere + " AND c.UNPLANNED = " + strIsPlaned + " ";
				}
			}
			if (strProcess != null) {
				if (!strProcess.equalsIgnoreCase("0")) {
					strWhere = strWhere + " AND c.PROCESS_ID = " + strProcess + " ";
				}
			}
			Vector vtOccuredRisk = Risk.getOccuredRiskListForOtherRisk(lWuID, fromDate, toDate, strWhere, customer, strCurrentProject);
			final Vector vtProcess = Project.getProcessList();
			session.setAttribute("vtProcess", vtProcess);
			session.setAttribute("wuID", String.valueOf(lWuID));
			session.setAttribute("strIsPlaned", strIsPlaned);
			session.setAttribute("strProcess", strProcess);
			session.setAttribute("fromDate", strFromDate);
			session.setAttribute("toDate", strToDate);
			session.setAttribute("customer", customer);
			final Vector vtCustomer = Project.getCustomerList();
			session.setAttribute("vtCustomer", vtCustomer);
			session.setAttribute("vtOccuredRisk", vtOccuredRisk);
			session.setAttribute("strCurrentProject", strCurrentProject);
			Vector vtTopCommonRisk = Risk.getTopCommonRiskSource();
			session.setAttribute("vtTopCommonRisk", vtTopCommonRisk);
			Vector vtProject = Project.getProjectList();
			session.setAttribute("vtProject", vtProject);
			Vector vtGroup = Project.getGroupList();
			session.setAttribute("vtGroup", vtGroup);
			UserInfo userInfo = (UserInfo)session.getAttribute("UserLoginInfo");
			boolean isAdminOfFsoft = Roles.isAdminOfFsoft(userInfo.developerID);
			session.setAttribute("isAdminOfFsoft", String.valueOf(isAdminOfFsoft));
			
			Fms1Servlet.callPage("riskOtherList.jsp", request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void listOtherRiskCallBack(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("riskOtherList.jsp",request,response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void riskListCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final long workUnitID = Long.parseLong(session.getAttribute("projectID").toString());
			final Vector riskList = Risk.getRiskList(workUnitID);
			session.setAttribute("riskList", riskList);
			int nPlannedRisk = 0;
			int nOccurredRisk = 0;
			for (int i = 0; i < riskList.size(); i++) {
				if (((RiskInfo) riskList.elementAt(i)).unplanned == 0)
					nPlannedRisk++;
				if (((RiskInfo) riskList.elementAt(i)).riskStatus == 2)
					nOccurredRisk++;
			}
									
			session.setAttribute("nPlannedRisk", String.valueOf(nPlannedRisk));
			session.setAttribute("nOccurredRisk", String.valueOf(nOccurredRisk));
			
			String source = "";
			if (request.getParameter("source") != null) {
				source = request.getParameter("source");
			}
			if (source.equalsIgnoreCase("1")) {
				Fms1Servlet.callPage("plOverview.jsp",request,response);
			}
			else {
				Fms1Servlet.callPage("riskList.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void riskNextCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		int riskBookmark = Integer.parseInt((String) session.getAttribute("riskBookmark"));
		riskBookmark += 20; //@
		session.setAttribute("riskBookmark", String.valueOf(riskBookmark));
		riskListCaller(request, response);
	}
	public static final void riskPrevCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		int riskBookmark = Integer.parseInt((String) session.getAttribute("riskBookmark"));
		riskBookmark -= 20; //@
		session.setAttribute("riskBookmark", String.valueOf(riskBookmark));
		riskListCaller(request, response);
	}
	static final void dirtyMess(final HttpServletRequest request){
		final HttpSession session = request.getSession();
		String pageSource = request.getParameter("source");
		if (pageSource == null)
			pageSource = "0";
		if (pageSource.equals("1")) {
			session.setAttribute("plOverview_source", "1");
		}
		else {
			session.setAttribute("plOverview_source", "0");
		}
	}
	public static final void riskAddnewPrepCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			final Vector riskCategory = Risk.getRiskCategory();
			final Vector riskSource = Risk.getRiskSource();
			Vector userList = Assignments.getAllUserAssignment(prjID, "", "", 0);
			session.setAttribute("userList", userList);
			final Vector vtDeveloper = UserHelper.getAllUsers();
			session.setAttribute("devVector", vtDeveloper);
			session.setAttribute("riskCategory", riskCategory);
			session.setAttribute("riskSource", riskSource);
			session.setAttribute("projectRank", Project.getRankByProjectId(prjID));
			dirtyMess(request);
			Fms1Servlet.callPage("riskAddnew.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//LAMNT3 - 20081020 - CR
	public static final void riskAddPrepCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Fms1Servlet.callPage("riskAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		};
	}
	
//	public static final void riskAddnewCaller(final HttpServletRequest request, final HttpServletResponse response) {
//		final HttpSession session = request.getSession();
//		try {
//			final RiskInfo riskInfo = new RiskInfo();
//			riskInfo.sourceID = Long.parseLong(request.getParameter("cmbsource"));
//			riskInfo.type = Integer.parseInt(request.getParameter("type"));
//			riskInfo.condition = request.getParameter("condition");
//			riskInfo.consequence = request.getParameter("consequence");
//			riskInfo.threshold = request.getParameter("threshold");
//			riskInfo.probability = Integer.parseInt(request.getParameter("probability"));
//			//----prob------
//			final DecimalFormat form = new DecimalFormat("00000000000.00");
//			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
//			final String[] impactTo = request.getParameterValues("impactTo");
//			final String[] unit = request.getParameterValues("unit");
//			final String[] est = request.getParameterValues("estimatedImpact");
//			riskInfo.plannedImpact = "";
//			for (int i = 0; i < 3; i++) {
//				if ((impactTo[i] == null) || (impactTo[i].equals("")))
//					impactTo[i] = "z";
//				if ((unit[i] == null) || (unit[i].equals("")))
//					unit[i] = "z";
//				if ((est[i] == null) || (est[i].equals("")))
//					est[i] = "-0000000001.00";
//				else {
//					est[i] = form.format(Double.parseDouble(est[i]));
//				}
//				riskInfo.plannedImpact += impactTo[i] + unit[i] + est[i];
//			}
//			riskInfo.mitigation = request.getParameter("mitigation");
//			riskInfo.mitigationBenefit = request.getParameter("mitigationBenefit");
//			riskInfo.mitigationActual = request.getParameter("mitigationActual");
//			riskInfo.exposure = Integer.parseInt(request.getParameter("exposure")); //----esposure------
//			riskInfo.contingencyPlan = request.getParameter("contingency"); //----contingency plan------
//			riskInfo.triggerName = request.getParameter("triggerName"); //----trigger------
//			riskInfo.developerAcc = request.getParameter("developerID"); //----developer account------
//			final java.util.Date uDate = formatter.parse(request.getParameter("assessmentDate"));
//			riskInfo.assessmentDate = new java.sql.Date(uDate.getTime()); //----assessment date------
//			riskInfo.status = Integer.parseInt(request.getParameter("status")); //----status------
//			riskInfo.processId = Integer.parseInt(request.getParameter("processId")); //----processId------
//			riskInfo.actualRiskScenario = request.getParameter("actualRisk"); //----actual risk------
//			riskInfo.actualAction = request.getParameter("actualAction"); //----actual action------
//			riskInfo.actualImpact = "";
//			for (int i = 3; i < 6; i++) {
//				if ((impactTo[i] == null) || (impactTo[i].equals("")))
//					impactTo[i] = "z";
//				if ((unit[i] == null) || (unit[i].equals("")))
//					unit[i] = "z";
//				if ((est[i] == null) || (est[i].equals("")))
//					est[i] = "-0000000001.00";
//				else {
//					est[i] = form.format(Double.parseDouble(est[i]));
//				}
//				riskInfo.actualImpact += impactTo[i] + unit[i] + est[i];
//			}
//			if (request.getParameter("unplanned") != null) {
//				riskInfo.unplanned = 1;
//				riskInfo.mitigation = null;
//				//		riskInfo.contingencyPlan = null;
//			}
//			else
//				riskInfo.unplanned = 0; //----unplanned------
//
//			riskInfo.projectID = Long.parseLong((String) session.getAttribute("projectID"));
//			if (riskInfo.status != 2) {
//				riskInfo.actualRiskScenario = null;
//				riskInfo.actualAction = null;
//				riskInfo.unplanned = 0;
//			}
//			else {//occured, then we insert an issue
//				IssueInfo issue = new IssueInfo();
//				issue.workUnitID = WorkUnit.getWorkUnitByProjectId(riskInfo.projectID);
//				issue.description = "Issue from risk: " + riskInfo.condition;
//				issue.statusID = 0; // Open see IssueInfo
//				issue.priorityID = 2; // High
//				issue.typeID = 4; //Other
//				UserInfo userInfo = (UserInfo)session.getAttribute("UserLoginInfo");
//				String account = userInfo.account;
//				String fullname = userInfo.Name;
//				issue.owner = account.toUpperCase();
//				issue.creator = fullname.trim();
//				issue.startDate = riskInfo.assessmentDate;
//				issue.dueDate = riskInfo.assessmentDate;
//				issue.comment = riskInfo.actualAction;
//				issue.riskID = riskInfo.riskID;
//				Issues.addIssue(issue);
//			}
//			Risk.addRisk(riskInfo);
//			session.removeAttribute("userList");
//			String pageSource = (String) session.getAttribute("plOverview_source");
//			if (pageSource == null)
//				pageSource = "0";
//			if (pageSource.equals("1")) {
//                ProjectPlanCaller.doLoadPLProjectOverview(request, response, "");
//			}
//			else {
//				riskListCaller(request, response);
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	//LAMNT3 - 20081020
//	public static final void riskAddnewCaller(final HttpServletRequest request, final HttpServletResponse response) {
//		final HttpSession session = request.getSession();
//		try {
//			long prjId = Long.parseLong((String) session.getAttribute("projectID"));
//			final RiskInfo riskInfo = new RiskInfo();
//			riskInfo.sourceID = Long.parseLong(request.getParameter("cmbsource"));
//			riskInfo.condition = request.getParameter("condition");
//			if(request.getParameter("txtProbability")!=null && !request.getParameter("txtProbability").equals(""))
//				riskInfo.probability = Double.parseDouble(request.getParameter("txtProbability"));
//			if(request.getParameter("txtImpact")!=null && !request.getParameter("txtImpact").equals(""))
//				riskInfo.impact = Double.parseDouble(request.getParameter("txtImpact"));
//			if(request.getParameter("txtExposure")!=null && !request.getParameter("txtExposure").equals(""))
//				riskInfo.exposure = Double.parseDouble(request.getParameter("txtExposure"));
//			if(request.getParameter("priority")!=null && !request.getParameter("priority").equals(""))
//				riskInfo.priority = Integer.parseInt(request.getParameter("priority"));
//			if(request.getParameter("riskPriority")!=null && !request.getParameter("riskPriority").equals(""))
//				riskInfo.riskPriority = Integer.parseInt(request.getParameter("riskPriority"));
//			riskInfo.triggerName = request.getParameter("triggerName"); //----trigger------
//			if(request.getParameter("riskStatus")!=null && !request.getParameter("riskStatus").equals(""))
//				riskInfo.riskStatus = Integer.parseInt(request.getParameter("riskStatus")); //----status------
//			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
//			java.util.Date uDate;
//			if(request.getParameter("lastUpdatedDate")!=null && !request.getParameter("lastUpdatedDate").equals("")){
//				uDate = formatter.parse(request.getParameter("lastUpdatedDate"));
//				riskInfo.lastUpdatedDate = new java.sql.Date(uDate.getTime()); //----assessment date------
//			}
//			riskInfo.projectID = prjId;
//			
//			long riskID = Risk.addRisk(riskInfo);
//			
//			int numMitigation = Integer.parseInt(request.getParameter("numMitigation"));
//			//----prob------
//			String[] mitigation = request.getParameterValues("mitigation");
//			String[] contingency = request.getParameterValues("contingency"); //----contingency plan------
//			String[] mitigationCost = request.getParameterValues("mitigationCost");
//			String[] mitigationBenefit = request.getParameterValues("mitigationBenefit");			
//			String[] personInCharge = request.getParameterValues("personInCharge"); //----developer account - Old select
//			String[] personInChargeAll = request.getParameterValues("personInChargeAll"); //----developer account - New select
//			String[] planEndDate = request.getParameterValues("planEndDate");
//			String[] actualEndDate = request.getParameterValues("actualEndDate");
//			String[] actionStatus = request.getParameterValues("ActionStatus"); //----processId------		
//			String[] checkChoise = request.getParameterValues("CheckChoise");	
//			
//			RiskMitigationInfo riskMitigation;
//			
//			for (int i = 0; i < numMitigation; i++) {
//				riskMitigation = new RiskMitigationInfo();
//				riskMitigation.riskID = riskID;
//				riskMitigation.mitigation = mitigation[i];
//				riskMitigation.contingency = contingency[i];
//				if(mitigationCost[i] != null && !mitigationCost[i].trim().equals("")){
//					riskMitigation.mitigationCost = Long.parseLong(mitigationCost[i]);
//				}
//				
//				riskMitigation.mitigationBenefit = mitigationBenefit[i];
//				if(riskMitigation.mitigation != null && !riskMitigation.mitigation.equals("")){
//					if(checkChoise[i].equals("0"))
//						riskMitigation.personInCharge = personInCharge[i].trim();
//					else
//						riskMitigation.personInCharge = personInChargeAll[i].trim();	
//				}
//				
//				if(planEndDate[i]!=null && !planEndDate[i].trim().equals("")){
//					uDate = formatter.parse(planEndDate[i]);
//					riskMitigation.planEndDate = new java.sql.Date(uDate.getTime());
//				}
//				if(actualEndDate[i]!=null && !actualEndDate[i].trim().equals("")){
//					uDate = formatter.parse(actualEndDate[i]);
//					riskMitigation.actualEndDate = new java.sql.Date(uDate.getTime());
//				}
//				if(riskMitigation.mitigation != null && !riskMitigation.mitigation.equals(""))
//					riskMitigation.actionStatus = Integer.parseInt(actionStatus[i]);
//				
//				Risk.addRiskMitigation(riskMitigation);
//			}
//			
//			if(riskInfo.priority == 1 && (riskInfo.riskStatus == 1 || riskInfo.riskStatus == 2)){
//				OtherActInfo oaInfo;
//				uDate = new Date();
//				for (int i = 0; i < numMitigation; i++) {
//					UserInfo userInfo = UserProfileCaller.checkUserFilter(request,personInChargeAll[i],"");
//					oaInfo = new OtherActInfo();
//					oaInfo.activity = "Mitigation : "+mitigation[i];
//					oaInfo.conductor = userInfo.developerID;
//					oaInfo.conductorName = userInfo.account;
//					oaInfo.prjID = prjId;
//					oaInfo.risk_id = riskID;
//					oaInfo.pStartD = new java.sql.Date(uDate.getTime());
//					oaInfo.pEndD = new java.sql.Date(uDate.getTime());
//					QualityObjective.addOtherActivity(oaInfo);
//					
//					oaInfo = new OtherActInfo();
//					oaInfo.activity = "Contingency : "+contingency[i];
//					oaInfo.conductor = userInfo.developerID;
//					oaInfo.conductorName = userInfo.account;
//					oaInfo.prjID = prjId;
//					oaInfo.risk_id = riskID;
//					oaInfo.pStartD = new java.sql.Date(uDate.getTime());
//					oaInfo.pEndD = new java.sql.Date(uDate.getTime());
//					QualityObjective.addOtherActivity(oaInfo);
//				}
//			}
//			
//			riskListCaller(request, response);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	// Added by HaiMM - Start ----------------------
	public static final void riskAddnewCaller(
		HttpServletRequest request, HttpServletResponse response)
	{
		Vector vtRisks = new Vector();
		getAddRiskData(request, vtRisks );
		// Save new Risk information
		Vector vtReturnRisks = new Vector();
		vtReturnRisks = Risk.saveRiskAddNew(vtRisks);
		// Save mitigation of common risk sources as default mitigation
		saveMitigationTemp(vtReturnRisks);
		// return to Risk list page
		riskListCaller(request, response);
	}
	
	public static final void riskMigrate(HttpServletRequest request,HttpServletResponse response) 
	{
		String projectName = "";
		try {
		Vector vtProjectLists = new Vector();
		boolean result1 = false;
		boolean result2 = false;
		
		vtProjectLists = Project.getProjectList();
		for (int i = 0; i < vtProjectLists.size(); i++) {
			ProjectDateInfo projectInfo = (ProjectDateInfo) vtProjectLists.get(i);
			projectName = projectInfo.code;
			Vector vtRiskList = new Vector();
			if (projectInfo != null) {
				vtRiskList = Risk.getRiskList(projectInfo.projectID);
				for (int j = 0; j < vtRiskList.size(); j++) {
					RiskInfo riskInfo = (RiskInfo) vtRiskList.get(j);
					String mitigation = riskInfo.mitigation;
					String contingency = riskInfo.contingencyPlan;
					if (riskInfo.mitigation != null && riskInfo.mitigation != "") {
						result1 = Risk.addMitigationForMigrate(riskInfo);
					}
					if (riskInfo.contingencyPlan != null && riskInfo.contingencyPlan != "") {
						result2 = Risk.addContingencyForMigrate(riskInfo);
					}
				}
			}
		}
		if (result1 && result2) {
			riskIdentifyCaller(request, response);
			
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Project Name = " + projectName);
			
		}
	}
	
	private static void saveMitigationTemp(Vector vtReturnRisks) {
		try {
			for (int i = 0; i < vtReturnRisks.size(); i++) {
				RiskInfo riskInfo = (RiskInfo) vtReturnRisks.get(i);
				
				Vector riskMitigationTemp = new Vector();
				riskMitigationTemp = Risk.getMitigationTemp(riskInfo.sourceID, riskInfo.riskID);
				
				if (riskMitigationTemp.size() > 0) {
					Risk.addMitigationTemp(riskMitigationTemp);				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void getAddRiskData(HttpServletRequest request,
		Vector vtRisks)
	{
		final HttpSession session = request.getSession();
		final long projectId =
				Long.parseLong((String) session.getAttribute("projectID"));
		try {
			String[] riskSource = request.getParameterValues("risk_source");
			String[] description = request.getParameterValues("description");
			String[] probability = request.getParameterValues("probability");
			String[] impact = request.getParameterValues("impact");
			String[] exposure = request.getParameterValues("exposure");
			String[] riskPriority = request.getParameterValues("riskPriority");
			String[] trigger = request.getParameterValues("trigger");
			
			String[] impactTo1 = request.getParameterValues("impactTo1");
			String[] unit1 = request.getParameterValues("unit1");
			String[] est1 = request.getParameterValues("estImpact1");

			String[] impactTo2 = request.getParameterValues("impactTo2");
			String[] unit2 = request.getParameterValues("unit2");
			String[] est2 = request.getParameterValues("estImpact2");

			String[] impactTo3 = request.getParameterValues("impactTo3");
			String[] unit3 = request.getParameterValues("unit3");
			String[] est3 = request.getParameterValues("estImpact3");

			putToAddRiskList(vtRisks, projectId, riskSource,
			description, probability, impact,
			exposure, riskPriority, trigger,
			impactTo1, unit1, est1, 
			impactTo2, unit2, est2,
			impactTo3, unit3, est3);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	private static void putToAddRiskList(Vector riskList,
		long projectId, String[] riskSourceList, String[] descriptionList, String[] probabilityList, String[] impactList,
		String[] exposureList, String[] riskPriorityList, String[] triggerList,
		String[] impactTo1List, String[] unit1List, String[] est1List, 
		String[] impactTo2List, String[] unit2List, String[] est2List,
		String[] impactTo3List, String[] unit3List, String[] est3List)
	{
		for (int i = 0; i < riskSourceList.length; i++) {
			// If selected a risk source in this line
			if (Integer.parseInt(riskSourceList[i]) != -1) {
				RiskInfo riskInfo = new RiskInfo();
				riskInfo.projectID = projectId;
				riskInfo.riskStatus = 1;
				riskInfo.sourceID = Long.parseLong(riskSourceList[i]);
				riskInfo.condition = descriptionList[i];
				if(probabilityList[i]!= null && !probabilityList[i].equals(""))
					riskInfo.probability = Double.parseDouble(probabilityList[i]);
				if(impactList[i]!= null && !impactList[i].equals(""))
					riskInfo.impact = Double.parseDouble(impactList[i]);
				if(exposureList[i]!= null && !exposureList[i].equals(""))
					riskInfo.exposure = Double.parseDouble(exposureList[i]);
				if (riskInfo.exposure > 7.1) {
					riskInfo.priority = 1;
				} else if (riskInfo.exposure <= 7.0 && riskInfo.exposure >= 4) {
					riskInfo.priority = 2;				
				} else {
					riskInfo.priority = 3;
				}
				if(riskPriorityList[i]!= null && !riskPriorityList[i].equals(""))
					riskInfo.riskPriority = Integer.parseInt(riskPriorityList[i]);
				riskInfo.triggerName = triggerList[i];
				
				// Plan Impact -- Start
				final DecimalFormat form = new DecimalFormat("00000000000.00");
				riskInfo.plannedImpact = "";

				if ((impactTo1List[i] == null) || (impactTo1List[i].equals("")))
					impactTo1List[i] = "z";
				if ((unit1List[i] == null) || (unit1List[i].equals("")))
					unit1List[i] = "z";
				if ((est1List[i] == null) || (est1List[i].equals("")))
					est1List[i] = "-0000000001.00";
				else {
					est1List[i] = form.format(Double.parseDouble(est1List[i]));
				}
				
				if ((impactTo2List[i] == null) || (impactTo2List[i].equals("")))
					impactTo2List[i] = "z";
				if ((unit2List[i] == null) || (unit2List[i].equals("")))
					unit2List[i] = "z";
				if ((est2List[i] == null) || (est2List[i].equals("")))
					est2List[i] = "-0000000001.00";
				else {
					est2List[i] = form.format(Double.parseDouble(est2List[i]));
				}
				
				if ((impactTo3List[i] == null) || (impactTo3List[i].equals("")))
					impactTo3List[i] = "z";
				if ((unit3List[i] == null) || (unit3List[i].equals("")))
					unit3List[i] = "z";
				if ((est3List[i] == null) || (est3List[i].equals("")))
					est3List[i] = "-0000000001.00";
				else {
					est3List[i] = form.format(Double.parseDouble(est3List[i]));
				}
				
				riskInfo.plannedImpact += impactTo1List[i] + unit1List[i] + est1List[i] + 
										impactTo2List[i] + unit2List[i] + est2List[i] + 
										impactTo3List[i] + unit3List[i] + est3List[i];
			
				if (!isDataEmpty(riskSourceList[i], descriptionList[i], probabilityList[i],
					impactList[i], exposureList[i], riskPriorityList[i], triggerList[i], 
					impactTo1List[i], unit1List[i], est1List[i], 
					impactTo2List[i], unit2List[i], est2List[i], 
					impactTo3List[i], unit3List[i], est3List[i])) {
				
					riskList.add(riskInfo);
				}
			}
		}
	}
	
	public static boolean isDataEmpty(String riskSource, String description, String probability, 
			String impact, String exposure, String riskPriority, String trigger,
			String impactTo1List, String unit1List, String est1List, 
			String impactTo2List, String unit2List, String est2List,
			String impactTo3List, String unit3List, String est3List) {
		// Not selected risk source or not filled other data fields
		if (riskSource.equalsIgnoreCase("-1")) {
			return true;
		}
		else {
			return ((description == null || description.equals("")) && (probability == null || probability.equals(""))
			&& (impact == null || impact.equals("")) && (exposure == null || exposure.equals(""))
			&& (riskPriority == null || riskPriority.equals("")) && (trigger == null || trigger.equals(""))
			
			&& (impactTo1List.equals("a"))
			&& (impactTo2List.equals("a"))
			&& (impactTo3List.equals("a"))
			
			&& (unit1List.equals("a"))
			&& (unit2List.equals("a"))
			&& (unit3List.equals("a"))
			
			&& (est1List == null || est1List.equals(""))
			&& (est2List == null || est2List.equals(""))
			&& (est3List == null || est3List.equals("")));
		}
	}

	//	Added by HaiMM - End ----------------------
	
	public static final void riskSourcePrepUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			Fms1Servlet.callPage("riskSourceUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		};
	}
	
	public static final void riskDatabasePrepAddCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			Vector vtRiskSourceForAdd = Risk.getRiskForAdd();
			session.setAttribute("vtRiskSourceForAdd", vtRiskSourceForAdd);
			Fms1Servlet.callPage("riskDatabaseAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		};
	}
		
	public static final void riskAddCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final RiskInfo riskInfo = new RiskInfo();
			long prjID = CommonTools.parseLong((String)session.getAttribute("projectID"));
			String[] sRiskID = (String[]) session.getAttribute("sRiskID");
			Vector vtTopCommonRisk = (Vector) session.getAttribute("vtTopCommonRisk");
		
			String[] note = (String[])request.getParameterValues("condition");
			RiskSourceInfo riskSourceInfo = new RiskSourceInfo();
			RiskMitigationInfo riskMitigation;
			
			for (int i=0; i<sRiskID.length; i++) {
				if (note[i] == null) {
					note[i] = "";
				}
				riskSourceInfo = (RiskSourceInfo) vtTopCommonRisk.elementAt(Integer.parseInt(sRiskID[i]));
				riskInfo.sourceID = riskSourceInfo.sourceID;
				riskInfo.condition = note[i];
				riskInfo.projectID = prjID;
				riskInfo.assessmentDate = new java.sql.Date(new java.util.Date().getTime());
				long riskID = Risk.addRisk(riskInfo);
				
				// add to Issue if Risk is Occured
				if (riskInfo.status != 2) {
			
					IssueInfo issue = new IssueInfo();
					issue.workUnitID = WorkUnit.getWorkUnitByProjectId(riskInfo.projectID);
					issue.description = "Issue from risk: " + riskInfo.condition;
					issue.statusID = 0; // Open see IssueInfo
					issue.priorityID = 2; // High
					issue.typeID = 4; //Other
					UserInfo userInfo = (UserInfo)session.getAttribute("UserLoginInfo");
					String account = userInfo.account;
					String fullname = userInfo.Name;
					issue.owner = account.toUpperCase();
					issue.creator = fullname.trim();
					issue.startDate = riskInfo.assessmentDate;
					issue.dueDate = riskInfo.assessmentDate;
					issue.comment = riskInfo.actualAction;
					issue.riskID = riskInfo.riskID;
					Issues.addIssue(issue);
				}
				
				//add null to Mitigation
				riskMitigation = new RiskMitigationInfo();
				riskMitigation.riskID = riskID;
				
				Risk.addRiskMitigation(riskMitigation);
			}
			riskListCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void riskDatabaseUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final RiskInfo riskInfo = new RiskInfo();
			long riskSourceOld = Long.parseLong((String)session.getAttribute("riskSourceID"));
			long riskSourceNew = Long.parseLong((String)request.getParameter("selectedRiskSource"));
			
			long topRisk = Long.parseLong((String)request.getParameter("topRisk"));
			
			if(riskSourceOld != riskSourceNew){
				Risk.updateRiskSource(riskSourceOld, 0);
				Risk.updateRiskSource(riskSourceNew, topRisk);
			}else{
				Risk.updateRiskSource(riskSourceOld, topRisk);
			}
			
			RiskCaller.listOtherRiskCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void riskDatabaseRemoveCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final RiskInfo riskInfo = new RiskInfo();
			long riskSourceNew = Long.parseLong((String)request.getParameter("selectedRiskSource"));
		
			Risk.updateRiskSource(riskSourceNew, 0);			
		
			RiskCaller.listOtherRiskCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void riskDatabaseAddCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			long riskSourceNew = Long.parseLong((String)request.getParameter("selectedRiskSource"));
			long topRisk = Long.parseLong((String)request.getParameter("topRisk"));
			Risk.updateRiskSource(riskSourceNew, topRisk);
		
			RiskCaller.listOtherRiskCaller(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static final void riskSourceUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final RiskInfo riskInfo = new RiskInfo();
			long prjID = CommonTools.parseLong((String)session.getAttribute("projectID"));
			Vector vtTopCommonRisk = (Vector) session.getAttribute("vtTopCommonRisk");
		
			String[] sTopRisk = (String[])request.getParameterValues("topRisk");
			double[] topRisk = new double[vtTopCommonRisk.size()];
			for(int i = 0;i < sTopRisk.length;i++){
				topRisk[i] = Double.parseDouble(sTopRisk[i]);
			}
			
			RiskSourceInfo riskSourceInfo = new RiskSourceInfo();
			for (int i=0; i<vtTopCommonRisk.size(); i++) {
				riskSourceInfo = (RiskSourceInfo) vtTopCommonRisk.elementAt(i);
				if(riskSourceInfo.topRisk != topRisk[i]){
					Risk.updateRiskSource(riskSourceInfo.sourceID,topRisk[i]);
				}
			}
			RiskCaller.doLoadCommonRisks(request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void riskDatabaseUpdatePrepCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			long sourceUpdated = Long.parseLong((String)request.getParameter("riskSourceID"));
			session.setAttribute("riskSourceID",request.getParameter("riskSourceID"));
			Vector vtRiskSource = Risk.getRiskUpdateTo(sourceUpdated);
			session.setAttribute("vtRiskSource", vtRiskSource);
			
			Fms1Servlet.callPage("riskDatabaseUpdate.jsp",request,response);	  
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void riskUpdatePrepCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));

			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);           

			Vector userList = Assignments.getAllUserAssignment(prjID, "", "", 0);
			session.setAttribute("userList", userList);
			final long riskID = Integer.parseInt(request.getParameter("riskID"));
			final RiskInfo riskInfo = (RiskInfo) Risk.getRiskById(riskID);
			final Vector riskCategory = Risk.getRiskCategory();
			final Vector riskSource = Risk.getRiskSource();
			final Vector riskMitigation = Risk.getMitigationRiskByRiskId(riskID);
			dirtyMess(request);
			session.setAttribute("riskInfo", riskInfo);
			session.setAttribute("riskCategory", riskCategory);
			session.setAttribute("riskMitigation", riskMitigation);
			session.setAttribute("riskSource", riskSource);
			final Vector vtProcess = Project.getProcessList();
		    session.setAttribute("vtProcess", vtProcess);
			// Added by HaiMM -- Start ------------------
			final Vector riskContingency = Risk.getContigencyByRiskId(riskID);
			session.setAttribute("riskContingency", riskContingency);
			
			// Get Project status for Other Risk List back to Risk list page
			long closedPrjID = 0;
			if (request.getParameter("prjID") != null) {
				closedPrjID = Integer.parseInt(request.getParameter("prjID"));
			}
			ProjectInfo projectInfo = new ProjectInfo();
			if (closedPrjID > 0) {
				projectInfo = Project.getProjectInfo(closedPrjID);
			}
			
			int riskNo = -1; 
			if (request.getParameter("riskNo") != null) {
				riskNo = Integer.parseInt(request.getParameter("riskNo"));
			}
			if (riskNo >= 0) {
				projectInfo.riskNo = riskNo;
			}
			
			session.setAttribute("projectInfo", projectInfo);
			
			// Added by HaiMM -- End --------------------

			Fms1Servlet.callPage("riskDetail.jsp",request,response);	  
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void riskUpdatePrepCaller1(
			final HttpServletRequest request,
			final HttpServletResponse response) {
			final HttpSession session = request.getSession();
			try {
				//final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));

				//final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
				//session.setAttribute("prjDateInfo", prjDateInfo);           

				//Vector userList = Assignments.getAllUserAssignment(prjID, "", "", 0);
				//session.setAttribute("userList", userList);
				final long riskID = Integer.parseInt(request.getParameter("riskID"));
				final RiskInfo riskInfo = (RiskInfo) Risk.getRiskById(riskID);
				final Vector riskCategory = Risk.getRiskCategory();
				final Vector riskSource = Risk.getRiskSource();
				final Vector riskMitigation = Risk.getMitigationRiskByRiskId(riskID);
				dirtyMess(request);
				session.setAttribute("riskInfo", riskInfo);
				session.setAttribute("riskCategory", riskCategory);
				session.setAttribute("riskMitigation", riskMitigation);
				session.setAttribute("riskSource", riskSource);
				final Vector vtProcess = Project.getProcessList();
				session.setAttribute("vtProcess", vtProcess);
				// Added by HaiMM -- Start ------------------
				final Vector riskContingency = Risk.getContigencyByRiskId(riskID);
				session.setAttribute("riskContingency", riskContingency);
			
				// Get Project status for Other Risk List back to Risk list page
				long closedPrjID = 0;
				if (request.getParameter("prjID") != null) {
					closedPrjID = Integer.parseInt(request.getParameter("prjID"));
				}
				ProjectInfo projectInfo = new ProjectInfo();
				if (closedPrjID > 0) {
					projectInfo = Project.getProjectInfo(closedPrjID);
				}
			
				int riskNo = -1; 
				if (request.getParameter("riskNo") != null) {
					riskNo = Integer.parseInt(request.getParameter("riskNo"));
				}
				if (riskNo >= 0) {
					projectInfo.riskNo = riskNo;
				}
			
				session.setAttribute("projectInfo", projectInfo);
			
				// Added by HaiMM -- End --------------------

				Fms1Servlet.callPage("paRiskDetail.jsp",request,response);	  
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	// Added by HaiMM
	public static final void riskUpdateCaller(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		Vector mitigations = new Vector();
		Vector contingencies = new Vector();
		
		// Update Risk Info
		updateRiskInfo(request, response);

		// INSERT/UPDATE/DELETE mitigation/contingency from Risk Update Page
		try {
			getUpdateRiskData(request, mitigations, contingencies);

			// Save new LOC information
			boolean isSaved =
				Risk.saveMitiContiUpdate(mitigations, contingencies);
			if (isSaved) {
				// Satve data to Other Activity
				riskUpdateOtherActivity(request, response, mitigations, contingencies);
				//Return listing page
				RiskCaller.riskUpdatePrepCaller(request, response);
			} 
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static final void updateRiskInfo(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			long prjId = Long.parseLong((String) session.getAttribute("projectID"));
			final RiskInfo riskInfo = new RiskInfo();
			riskInfo.riskID = Long.parseLong(request.getParameter("riskID"));
			riskInfo.sourceID = Long.parseLong(request.getParameter("cmbsource"));
			riskInfo.condition = request.getParameter("condition");
			if(request.getParameter("txtProbability")!=null && !request.getParameter("txtProbability").equals(""))
				riskInfo.probability = Double.parseDouble(request.getParameter("txtProbability"));
			if(request.getParameter("txtImpact")!=null && !request.getParameter("txtImpact").equals(""))
				riskInfo.impact = Double.parseDouble(request.getParameter("txtImpact"));
			if(request.getParameter("txtExposure")!=null && !request.getParameter("txtExposure").equals(""))
				riskInfo.exposure = Double.parseDouble(request.getParameter("txtExposure"));
			if(request.getParameter("priority")!=null && !request.getParameter("priority").equals(""))
				riskInfo.priority = Integer.parseInt(request.getParameter("priority"));
			if(request.getParameter("riskPriority")!=null && !request.getParameter("riskPriority").equals(""))
				riskInfo.riskPriority = Integer.parseInt(request.getParameter("riskPriority"));
			riskInfo.triggerName = request.getParameter("triggerName"); //----trigger------
			if(request.getParameter("riskStatus")!=null && !request.getParameter("riskStatus").equals(""))
				riskInfo.riskStatus = Integer.parseInt(request.getParameter("riskStatus")); //----status------
			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
			java.util.Date uDate;
			if(request.getParameter("lastUpdatedDate")!=null && !request.getParameter("lastUpdatedDate").equals("")){
				uDate = formatter.parse(request.getParameter("lastUpdatedDate"));
				riskInfo.lastUpdatedDate = new java.sql.Date(uDate.getTime()); //----assessment date------
			}
			riskInfo.projectID = prjId;

			final DecimalFormat form = new DecimalFormat("00000000000.00");
			final String[] impactTo = request.getParameterValues("impactTo");
			final String[] unit = request.getParameterValues("unit");
			final String[] est = request.getParameterValues("estimatedImpact");
			riskInfo.plannedImpact = "";
			for (int i = 0; i < 3; i++) {
				if ((impactTo[i] == null) || (impactTo[i].equals("")))
					impactTo[i] = "z";
				if ((unit[i] == null) || (unit[i].equals("")))
					unit[i] = "z";
				if ((est[i] == null) || (est[i].equals("")))
					est[i] = "-0000000001.00";
				else {
					est[i] = form.format(Double.parseDouble(est[i]));
				}
				riskInfo.plannedImpact += impactTo[i] + unit[i] + est[i];
			}
			RiskInfo temp = Risk.getRiskById(riskInfo.riskID);
			Risk.updateRisk(riskInfo);
			// add to Issue if Risk is Occured
			if (riskInfo.riskStatus == 2 && temp.riskStatus!=riskInfo.riskStatus) {
				IssueInfo issue = new IssueInfo();
				issue.workUnitID = WorkUnit.getWorkUnitByProjectId(riskInfo.projectID);
				issue.description = "Issue from risk: " + riskInfo.condition;
				issue.statusID = 0; // Open see IssueInfo
				issue.priorityID = 2; // High
				issue.typeID = 4; //Other
				UserInfo userInfo = (UserInfo)session.getAttribute("UserLoginInfo");
				String account = userInfo.account;
				String fullname = userInfo.Name;
				issue.owner = account.toUpperCase();
				issue.creator = fullname.trim();
				issue.startDate = riskInfo.assessmentDate;
				issue.dueDate = riskInfo.assessmentDate;
				issue.comment = riskInfo.actualAction;
				issue.riskID = riskInfo.riskID;
				Issues.addIssue(issue);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get mitigation/contingency form data posted from client (Update Risk page)
	 * @param request
	 * @param mitigations
	 * @param contingencies
	 */
	private static void getUpdateRiskData(HttpServletRequest request,
		Vector mitigations, Vector contingencies) throws ParseException
	{
		final HttpSession session = request.getSession();
		final long riskID = Long.parseLong(request.getParameter("riskID"));
		try {
			String[] mitigation_id = request.getParameterValues("mitigation_id");
			String[] mitigation = request.getParameterValues("mitigation");
			String[] mitigationCost = request.getParameterValues("mitigationCost");
			String[] mitigationBenefit = request.getParameterValues("mitigationBenefit");
			
			String[] personInCharge = request.getParameterValues("personInCharge");
			String[] planEndDate = request.getParameterValues("planEndDate");
			String[] actualEndDate = request.getParameterValues("actualEndDate");
			String[] actionStatus = request.getParameterValues("actionStatus");
			
			putToUpdateMitigationList(mitigations, riskID,
					mitigation_id, mitigation, mitigationCost, mitigationBenefit, personInCharge, 
					planEndDate, actualEndDate, actionStatus);

			String[] contingency_id = request.getParameterValues("contingency_id");
			String[] contingency = request.getParameterValues("contingency");
			
			String[] contin_personInCharge = request.getParameterValues("contin_personInCharge");
			String[] contin_planEndDate = request.getParameterValues("contin_planEndDate");
			String[] contin_actualEndDate = request.getParameterValues("contin_actualEndDate");
			String[] contin_actionStatus = request.getParameterValues("contin_actionStatus");
			
			putToUpdateContingencyList(contingencies, riskID,
					contingency_id, contingency, contin_personInCharge, 
					contin_planEndDate, contin_actualEndDate, contin_actionStatus);
					
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private static void putToUpdateMitigationList(Vector mitiList,
		long riskId, String[] mitigationIdList,String[] mitigationList, String[] mitigationCostList,
			String[] mitigationBenefitList,String[] personInChargeList, String[] planEndDateList, 
			String[] actualEndDateList,	String[] actionStatusList) throws ParseException
	{
		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
		java.util.Date uDate;
		int mitigationId = 0;
		for (int i = 0; i < mitigationList.length; i++) {
			mitigationId = Integer.parseInt(mitigationIdList[i]);
			// If entered a mitigation in this line
			if (mitigationList[i] != null && !mitigationList[i].equals("")) {
				RiskMitigationInfo riskMitigation = new RiskMitigationInfo();
				
				riskMitigation.riskID = riskId;
				riskMitigation.riskMitigationId = Long.parseLong(mitigationIdList[i]);
				riskMitigation.mitigation = mitigationList[i];
				if(mitigationCostList[i] != null && !mitigationCostList[i].trim().equals("")){
					riskMitigation.mitigationCost = Double.parseDouble(mitigationCostList[i]);
				}
				riskMitigation.mitigationBenefit = mitigationBenefitList[i];
				if(personInChargeList[i].trim() != null && !personInChargeList[i].trim().equalsIgnoreCase("-1")){
					riskMitigation.personInCharge = personInChargeList[i].trim();	
				}
				if(planEndDateList[i]!=null && !planEndDateList[i].trim().equals("")){
					uDate = formatter.parse(planEndDateList[i]);
					riskMitigation.planEndDate = new java.sql.Date(uDate.getTime());
				}
				if(actualEndDateList[i]!=null && !actualEndDateList[i].trim().equals("")){
					uDate = formatter.parse(actualEndDateList[i]);
					riskMitigation.actualEndDate = new java.sql.Date(uDate.getTime());
				}
				if(actionStatusList[i].trim() != null && !actionStatusList[i].trim().equalsIgnoreCase("-1")){
					riskMitigation.actionStatus = Integer.parseInt(actionStatusList[i]);
				}

				if (mitigationId <= 0) {    // Selected a new mitigation
					riskMitigation.dmlType = RiskInfo.DML_INSERT;
				}
				else {  // Updated an existing line
					riskMitigation.dmlType = RiskInfo.DML_UPDATE;
				}
				// If reset all fields to blank => delete this language LOC
				if (inputDataIsEmpty(mitigationList[i], mitigationCostList[i], mitigationBenefitList[i],
					personInChargeList[i], planEndDateList[i], actualEndDateList[i], actionStatusList[i])) {
						
					riskMitigation.dmlType = RiskInfo.DML_DELETE;
				}
				mitiList.add(riskMitigation);
			}
			// De-selected a Mitigation => delete this Mitigation information
			else if (mitigationId > 0) {
				RiskMitigationInfo riskMitigation = new RiskMitigationInfo();
				riskMitigation.riskMitigationId = mitigationId;
				riskMitigation.riskID = riskId;
				riskMitigation.dmlType = RiskInfo.DML_DELETE;
				
				mitiList.add(riskMitigation);
			}
		}
	}
	
	private static void putToUpdateContingencyList(Vector continList,
			long riskId, String[] contingencyIdList, String[] contingencyList, String[] continPersonInChargeList, String[] continPlanEndDateList, 
				String[] continActualEndDateList, String[] continActionStatusList) throws ParseException
		{
			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
			java.util.Date uDate;
			int contingencyId = 0;
			for (int i = 0; i < contingencyList.length; i++) {
				contingencyId = Integer.parseInt(contingencyIdList[i]);
				// If entered a contingency in this line
				if (contingencyList[i] != null && !contingencyList[i].equals("")) {
					RiskContingencyInfo riskContingency = new RiskContingencyInfo();
				
					riskContingency.riskID = riskId;
					riskContingency.riskContingencyId = Long.parseLong(contingencyIdList[i]);
					riskContingency.contingency = contingencyList[i];
					if (continPersonInChargeList[i].trim() != null && !continPersonInChargeList[i].trim().equalsIgnoreCase("-1")) {
						riskContingency.personInCharge = continPersonInChargeList[i].trim();	
					}
					if(continPlanEndDateList[i]!=null && !continPlanEndDateList[i].trim().equals("")){
						uDate = formatter.parse(continPlanEndDateList[i]);
						riskContingency.planEndDate = new java.sql.Date(uDate.getTime());
					}
					if(continActualEndDateList[i]!=null && !continActualEndDateList[i].trim().equals("")){
						uDate = formatter.parse(continActualEndDateList[i]);
						riskContingency.actualEndDate = new java.sql.Date(uDate.getTime());
					}
					if (continActionStatusList[i].trim() != null && !continActionStatusList[i].equalsIgnoreCase("-1")) {
						riskContingency.actionStatus = Integer.parseInt(continActionStatusList[i]);						
					}
					
					if (contingencyId <= 0) {    // Selected a new mitigation
						riskContingency.dmlType = RiskInfo.DML_INSERT;
					}
					else {  // Updated an existing line
						riskContingency.dmlType = RiskInfo.DML_UPDATE;
					}
					// If reset all fields to blank => delete this language LOC
					if (inputDataContinIsEmpty(contingencyList[i], continPersonInChargeList[i],
						continPlanEndDateList[i], continActualEndDateList[i], continActionStatusList[i])) {
						
						riskContingency.dmlType = RiskInfo.DML_DELETE;
					}
					continList.add(riskContingency);
				}
				// De-selected a contingency => delete this Contingency information
				else if (contingencyId > 0) {
					RiskContingencyInfo riskContingency = new RiskContingencyInfo();
					riskContingency.riskContingencyId = contingencyId;
					riskContingency.riskID = riskId;
					riskContingency.dmlType = RiskInfo.DML_DELETE;
				
					continList.add(riskContingency);
				}
			}
		}
	/**
	 * Check input data (add/update functions) is empty
	 * @return
	 */
	public static boolean inputDataIsEmpty(String mitigation, String mitiCost, String mitiBenefit, 
			String personInCharge, String planDate, String actualDate, String actionStatus) {
		// Not selected mitigation or not filled other data fields
		if (mitigation.equals("") || mitigation == null) {
			return true;
		}
		else {
			return ((mitiCost == null || mitiCost.equals("")) && (mitiBenefit == null || mitiBenefit.equals(""))
			&& (personInCharge == null || personInCharge.equals("")) && (planDate == null || planDate.equals(""))
			&& (actualDate == null || actualDate.equals("")) && (actionStatus == null || actionStatus.equals("")));
		}
	}
	
	/**
	 * Check input data (add/update functions) is empty
	 * @return
	 */
	public static boolean inputDataContinIsEmpty(String contingency, 
			String personInCharge, String planDate, String actualDate, String actionStatus) {
		// Not selected mitigation or not filled other data fields
		if (contingency.equals("") || contingency == null) {
			return true;
		}
		else {
			return ((personInCharge == null || personInCharge.equals("")) && (planDate == null || planDate.equals(""))
			&& (actualDate == null || actualDate.equals("")) && (actionStatus == null || actionStatus.equals("")));
		}
	}

	public static int convStatusToOtherQual(int status) {
		int result = 0;
		if (status == 1) {
			result = 5;
		} else if (status == 2) {
			result = 3;
		} else if (status == 3) {
			result = 1;
		} else if (status == 4) {
			result = 2;
		} else if (status == 5) {
			result = 4;
		}
		
		return result;
	}
	
	public static final void riskUpdateOtherActivity(final HttpServletRequest request, final HttpServletResponse response,
							Vector mitigations, Vector contingencies) {
		final HttpSession session = request.getSession();
		try {
			long prjId = Long.parseLong((String) session.getAttribute("projectID"));
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjId);
			
			final RiskInfo riskInfo = new RiskInfo();
			riskInfo.riskID = Long.parseLong(request.getParameter("riskID"));
			if(request.getParameter("priority")!=null && !request.getParameter("priority").equals(""))
				riskInfo.priority = Integer.parseInt(request.getParameter("priority"));
			if(request.getParameter("riskStatus")!=null && !request.getParameter("riskStatus").equals(""))
				riskInfo.riskStatus = Integer.parseInt(request.getParameter("riskStatus")); //----status------
			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
			java.util.Date uDate;
			
			RiskInfo riskOldInfo = (RiskInfo)session.getAttribute("riskInfo");
			if (riskOldInfo.priority == 1) {
				Db.delete(riskInfo.riskID,"RISK_ID","OTHER_ACTIVITY");
			}
						
			if(riskInfo.priority == 1 && (riskInfo.riskStatus == 1 || riskInfo.riskStatus == 2)){
				Db.delete(riskInfo.riskID,"RISK_ID","OTHER_ACTIVITY");	
				
				OtherActInfo oaInfo;
				uDate = new Date();
				for (int i=0; i < mitigations.size(); i++) {
					RiskMitigationInfo riskMitigationInfo = (RiskMitigationInfo) mitigations.elementAt(i);
					if (riskMitigationInfo.mitigation != null && !riskMitigationInfo.mitigation.equals("")) {
						oaInfo = new OtherActInfo();
						oaInfo.activity = "Mitigation : "+ riskMitigationInfo.mitigation;
						oaInfo.conductor = Risk.getDeveloperID(riskMitigationInfo.personInCharge);
						oaInfo.conductorName = riskMitigationInfo.personInCharge;
						oaInfo.prjID = prjId;
						oaInfo.risk_id = riskInfo.riskID;
						oaInfo.pStartD = prjDateInfo.actualStartDate;
						oaInfo.pEndD = riskMitigationInfo.planEndDate;
						oaInfo.aEndD = riskMitigationInfo.actualEndDate;
						
						oaInfo.status = convStatusToOtherQual(riskMitigationInfo.actionStatus);
						oaInfo.risk_Miti_Contin_ID = riskMitigationInfo.riskMitigationId;
						oaInfo.risk_type = 1; // 1: Type of Mitigation
						QualityObjective.addOtherActivity(oaInfo);
					}
				}
				for (int i=0; i < contingencies.size(); i++) {
					RiskContingencyInfo riskContingencyInfo = (RiskContingencyInfo) contingencies.elementAt(i);
					if (riskContingencyInfo.contingency != null && !riskContingencyInfo.contingency.equals("")) {
						oaInfo = new OtherActInfo();
						oaInfo.activity = "Contingency : "+ riskContingencyInfo.contingency;
						oaInfo.conductor = Risk.getDeveloperID(riskContingencyInfo.personInCharge);
						oaInfo.conductorName = riskContingencyInfo.personInCharge;
						oaInfo.prjID = prjId;
						oaInfo.risk_id = riskInfo.riskID;
						oaInfo.pStartD = prjDateInfo.actualStartDate;
						oaInfo.pEndD = riskContingencyInfo.planEndDate;
						oaInfo.aEndD = riskContingencyInfo.actualEndDate;
						
						oaInfo.status = convStatusToOtherQual(riskContingencyInfo.actionStatus);
						oaInfo.risk_Miti_Contin_ID = riskContingencyInfo.riskContingencyId;
						oaInfo.risk_type = 2;	// 2: Type of contingency

						QualityObjective.addOtherActivity(oaInfo);
					}
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}	


//	public static final void riskUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
//		final HttpSession session = request.getSession();
//		
//		try {
//			final RiskInfo riskInfo = new RiskInfo();
//			//----prob------
//			final DecimalFormat form = new DecimalFormat("00000000000.00");
//			riskInfo.sourceID=Long.parseLong(request.getParameter("cmbsource"));
//			riskInfo.type=Integer.parseInt(request.getParameter("type"));
//			riskInfo.condition = request.getParameter("condition"); //----condition------
//			riskInfo.consequence = request.getParameter("consequence"); //----consequence------
//			riskInfo.threshold = request.getParameter("threshold");
//			riskInfo.probability = Integer.parseInt(request.getParameter("probability"));
//			final String[] impactTo = request.getParameterValues("impactTo");
//			final String[] unit = request.getParameterValues("unit");
//			final String[] est = request.getParameterValues("estimatedImpact");
//			riskInfo.plannedImpact = "";
//			for (int i = 0; i < 3; i++) {
//				if ((impactTo[i] == null) || (impactTo[i].equals("")))
//					impactTo[i] = "z";
//				if ((unit[i] == null) || (unit[i].equals("")))
//					unit[i] = "z";
//				if ((est[i] == null) || (est[i].equals("")))
//					est[i] = "-0000000001.00";
//				else {
//					est[i] = form.format(Double.parseDouble(est[i]));
//				}
//				riskInfo.plannedImpact += impactTo[i] + unit[i] + est[i];
//			}
//			riskInfo.exposure = Integer.parseInt(request.getParameter("exposure"));
//			riskInfo.mitigation = request.getParameter("mitigation");
//			riskInfo.mitigationBenefit = request.getParameter("mitigationBenefit");
//			riskInfo.mitigationActual = request.getParameter("mitigationActual");
//			riskInfo.contingencyPlan = request.getParameter("contingency");
//			riskInfo.triggerName = request.getParameter("triggerName");
//			riskInfo.developerAcc = request.getParameter("developerID");
//			final java.util.Date uDate = CommonTools.parseDate(request.getParameter("assessmentDate"));
//			riskInfo.assessmentDate = new java.sql.Date(uDate.getTime());
//			riskInfo.riskStatus = Integer.parseInt(request.getParameter("status"));
//			riskInfo.processId = Integer.parseInt(request.getParameter("processId")); //----processId------
//			riskInfo.actualRiskScenario = request.getParameter("actualRisk");
//			riskInfo.actualAction = request.getParameter("actualAction");
//			riskInfo.actualImpact = "";
//			for (int i = 3; i < 6; i++) {
//				if ((impactTo[i] == null) || (impactTo[i].equals("")))
//					impactTo[i] = "z";
//				if ((unit[i] == null) || (unit[i].equals("")))
//					unit[i] = "z";
//				if ((est[i] == null) || (est[i].equals("")))
//					est[i] = "-0000000001.00";
//				else {
//					est[i] = form.format(Double.parseDouble(est[i]));
//				}
//				riskInfo.actualImpact += impactTo[i] + unit[i] + est[i];
//			}
//			if (request.getParameter("unplanned") != null) {
//				riskInfo.unplanned = 1;
//			}
//			else
//				riskInfo.unplanned = 0;
//			if (riskInfo.riskStatus != 2) {
//				riskInfo.unplanned = 0;
//			}
//			riskInfo.projectID = Long.parseLong((String) session.getAttribute("projectID"));
//			riskInfo.riskID = Long.parseLong(request.getParameter("riskID"));
//			Risk.setRisk(riskInfo);
//			if (riskInfo.riskStatus == 2) {//occured
//
//				IssueInfo issue = Issues.getIssueByRiskId(riskInfo.riskID);
//				if (issue.issueID > 0) {
//					issue.comment = riskInfo.actualAction;
//					Issues.updateIssue(issue);
//				}
//				else {
//					issue.workUnitID = WorkUnit.getWorkUnitByProjectId(riskInfo.projectID);
//					issue.description = "Issue from risk: " + riskInfo.condition;
//					issue.statusID = 0; // Open see IssueInfo
//					issue.priorityID = 2; // High
//					issue.typeID = 4; //Other
//					UserInfo userInfo = (UserInfo)session.getAttribute("UserLoginInfo");
//					String account = userInfo.account;
//					String fullname = userInfo.Name;
//					issue.owner = account.toUpperCase();
//					issue.creator = fullname.trim();
//					issue.startDate = riskInfo.assessmentDate;
//					issue.dueDate = riskInfo.assessmentDate;
//					issue.comment = riskInfo.actualAction;
//					issue.riskID = riskInfo.riskID;
//					Issues.addIssue(issue);
//				}
//			}
//			session.removeAttribute("userList");
//			String pageSource = (String) session.getAttribute("plOverview_source");
//			if (pageSource == null)
//				pageSource = "0";
//			if (pageSource.equals("1")) {
//                ProjectPlanCaller.doLoadPLProjectOverview(request, response, "");
//			}
//			else {
//				riskListCaller(request, response);
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}



//LAMNT3 - 20081101
//	public static final void riskUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
//		final HttpSession session = request.getSession();
//		try {
//			long prjId = Long.parseLong((String) session.getAttribute("projectID"));
//			final RiskInfo riskInfo = new RiskInfo();
//			riskInfo.riskID = Long.parseLong(request.getParameter("riskID"));
//			riskInfo.sourceID = Long.parseLong(request.getParameter("cmbsource"));
//			riskInfo.condition = request.getParameter("condition");
//			if(request.getParameter("txtProbability")!=null && !request.getParameter("txtProbability").equals(""))
//				riskInfo.probability = Double.parseDouble(request.getParameter("txtProbability"));
//			if(request.getParameter("txtImpact")!=null && !request.getParameter("txtImpact").equals(""))
//				riskInfo.impact = Double.parseDouble(request.getParameter("txtImpact"));
//			if(request.getParameter("txtExposure")!=null && !request.getParameter("txtExposure").equals(""))
//				riskInfo.exposure = Double.parseDouble(request.getParameter("txtExposure"));
//			if(request.getParameter("priority")!=null && !request.getParameter("priority").equals(""))
//				riskInfo.priority = Integer.parseInt(request.getParameter("priority"));
//			if(request.getParameter("riskPriority")!=null && !request.getParameter("riskPriority").equals(""))
//				riskInfo.riskPriority = Integer.parseInt(request.getParameter("riskPriority"));
//			riskInfo.triggerName = request.getParameter("triggerName"); //----trigger------
//			if(request.getParameter("riskStatus")!=null && !request.getParameter("riskStatus").equals(""))
//				riskInfo.riskStatus = Integer.parseInt(request.getParameter("riskStatus")); //----status------
//			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
//			java.util.Date uDate;
//			if(request.getParameter("lastUpdatedDate")!=null && !request.getParameter("lastUpdatedDate").equals("")){
//				uDate = formatter.parse(request.getParameter("lastUpdatedDate"));
//				riskInfo.lastUpdatedDate = new java.sql.Date(uDate.getTime()); //----assessment date------
//			}
//			riskInfo.projectID = prjId;
//
//			long riskID = Risk.updateRisk(riskInfo);
//
//			int numMitigation = Integer.parseInt(request.getParameter("numMitigation"));
//			int numMitigationUpdate = Integer.parseInt(request.getParameter("numMitigationUpdate"));
//			//----prob------
//			String[] mitigation = request.getParameterValues("mitigation");
//			String[] contingency = request.getParameterValues("contingency"); //----contingency plan------
//			String[] mitigationCost = request.getParameterValues("mitigationCost");
//			String[] mitigationBenefit = request.getParameterValues("mitigationBenefit");			
//			String[] personInCharge = request.getParameterValues("personInCharge"); //----developer account - Old select
//			String[] personInChargeAll = request.getParameterValues("personInChargeAll"); //----developer account - New select
//			String[] planEndDate = request.getParameterValues("planEndDate");
//			String[] actualEndDate = request.getParameterValues("actualEndDate");
//			String[] actionStatus = request.getParameterValues("ActionStatus"); //----processId------
//			String[] riskMitigationId = request.getParameterValues("riskMitigationId");
//
//			RiskMitigationInfo riskMitigation;
//
//			for (int i = 0; i < numMitigationUpdate; i++) {
//				riskMitigation = new RiskMitigationInfo();
//				riskMitigation.riskMitigationId = Long.parseLong(riskMitigationId[i]);
//				riskMitigation.mitigation = mitigation[i];
//				riskMitigation.contingency = contingency[i];
//				if(mitigationCost[i] != null && !mitigationCost[i].trim().equals("")){
//					riskMitigation.mitigationCost = Long.parseLong(mitigationCost[i]);
//				}
//	
//				riskMitigation.mitigationBenefit = mitigationBenefit[i];
//				if(riskMitigation.mitigation != null && !riskMitigation.mitigation.equals("")){
//					riskMitigation.personInCharge = personInChargeAll[i].trim();	
//				}
//		
//				if(planEndDate[i]!=null && !planEndDate[i].trim().equals("")){
//					uDate = formatter.parse(planEndDate[i]);
//					riskMitigation.planEndDate = new java.sql.Date(uDate.getTime());
//				}
//				if(actualEndDate[i]!=null && !actualEndDate[i].trim().equals("")){
//					uDate = formatter.parse(actualEndDate[i]);
//					riskMitigation.actualEndDate = new java.sql.Date(uDate.getTime());
//				}
//				if(riskMitigation.mitigation != null && !riskMitigation.mitigation.equals(""))
//					riskMitigation.actionStatus = Integer.parseInt(actionStatus[i]);
//	
//				Risk.updateRiskMitigation(riskMitigation);
//			}
//			
//			if(numMitigationUpdate < numMitigation){
//				for (int i = numMitigationUpdate; i < numMitigation; i++) {
//					riskMitigation = new RiskMitigationInfo();
//					riskMitigation.riskID = riskID;
//					riskMitigation.mitigation = mitigation[i];
//					riskMitigation.contingency = contingency[i];
//					if(mitigationCost[i] != null && !mitigationCost[i].trim().equals("")){
//						riskMitigation.mitigationCost = Long.parseLong(mitigationCost[i]);
//					}
//
//					riskMitigation.mitigationBenefit = mitigationBenefit[i];
//					if(riskMitigation.mitigation != null && !riskMitigation.mitigation.equals("")){
//						riskMitigation.personInCharge = personInChargeAll[i].trim();	
//					}
//
//					if(planEndDate[i]!=null && !planEndDate[i].trim().equals("")){
//						uDate = formatter.parse(planEndDate[i]);
//						riskMitigation.planEndDate = new java.sql.Date(uDate.getTime());
//					}
//					if(actualEndDate[i]!=null && !actualEndDate[i].trim().equals("")){
//						uDate = formatter.parse(actualEndDate[i]);
//						riskMitigation.actualEndDate = new java.sql.Date(uDate.getTime());
//					}
//					if(riskMitigation.mitigation != null && !riskMitigation.mitigation.equals(""))
//						riskMitigation.actionStatus = Integer.parseInt(actionStatus[i]);
//
//					Risk.addRiskMitigation(riskMitigation);
//				}
//			}
//			
//			RiskInfo riskOldInfo = (RiskInfo)session.getAttribute("riskInfo");
//			if(riskOldInfo.priority != 1 && riskInfo.priority == 1 && (riskInfo.riskStatus == 1 || riskInfo.riskStatus == 2)){
//				OtherActInfo oaInfo;
//				uDate = new Date();
//				for (int i = 0; i < numMitigation; i++) {
//					UserInfo userInfo = UserProfileCaller.checkUserFilter(request,personInChargeAll[i],"");
//					oaInfo = new OtherActInfo();
//					oaInfo.activity = "Mitigation : "+mitigation[i];
//					oaInfo.conductor = userInfo.developerID;
//					oaInfo.conductorName = userInfo.account;
//					oaInfo.prjID = prjId;
//					oaInfo.risk_id = riskID;
//					oaInfo.pStartD = new java.sql.Date(uDate.getTime());
//					oaInfo.pEndD = new java.sql.Date(uDate.getTime());
//					QualityObjective.addOtherActivity(oaInfo);
//					
//					oaInfo = new OtherActInfo();
//					oaInfo.activity = "Contingency : "+contingency[i];
//					oaInfo.conductor = userInfo.developerID;
//					oaInfo.conductorName = userInfo.account;
//					oaInfo.prjID = prjId;
//					oaInfo.risk_id = riskID;
//					oaInfo.pStartD = new java.sql.Date(uDate.getTime());
//					oaInfo.pEndD = new java.sql.Date(uDate.getTime());
//					QualityObjective.addOtherActivity(oaInfo);
//				}
//			}
//			if(riskOldInfo.priority == 1 && (riskOldInfo.riskStatus == 1 || riskOldInfo.riskStatus == 2) && riskInfo.priority != 1){
//				Db.delete(riskID,"RISK_ID","OTHER_ACTIVITY");	
//			}
//
//			RiskCaller.riskUpdatePrepCaller(request, response);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}		  

	//LAMNT3 - 20081101		  
	public static final void riskDeleteCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final long riskID = Long.parseLong(request.getParameter("riskID"));
			boolean isSaved = Risk.delRisk(riskID);
			// Update Other Quality Activity
			if(isSaved) {
				Vector mitigations = new Vector();
				Vector contingencies = new Vector();
				
				mitigations = Risk.getMitigationRiskByRiskId(riskID);
				contingencies = Risk.getContigencyByRiskId(riskID);
				
				riskUpdateOtherActivity(request, response, mitigations, contingencies);
			}
			String pageSource = (String) session.getAttribute("plOverview_source");
			if (pageSource == null)
				pageSource = "0";
			if (pageSource.equals("1")) {
				ProjectPlanCaller.doLoadPLProjectOverview(request, response, "");
			}
			else {
				riskListCaller(request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// LAMNT3 - 20081101 - Delete from Risk Detail Page
	public static final void riskMitigationDeleteCaller(final HttpServletRequest request, final HttpServletResponse response) {
	  final HttpSession session = request.getSession();
		try {
			final long riskID = Long.parseLong(request.getParameter("riskID"));
		 	final long riskMitigationID = Long.parseLong(request.getParameter("riskMitigationID"));
			boolean isSaved = Risk.delRiskMitigation(riskMitigationID);
			// Update Other Quality Activity
			if(isSaved) {
				Vector mitigations = new Vector();
				Vector contingencies = new Vector();
				
				mitigations = Risk.getMitigationRiskByRiskId(riskID);
				contingencies = Risk.getContigencyByRiskId(riskID);
				
				riskUpdateOtherActivity(request, response, mitigations, contingencies);
			}

			RiskCaller.riskUpdatePrepCaller(request, response);
		  }
		  catch (Exception e) {
			  e.printStackTrace();
		  }
	  }

	// Added by HaiMM - Delete from Risk Detail Page
	public static final void riskContingencyDeleteCaller(final HttpServletRequest request, final HttpServletResponse response) {
	  final HttpSession session = request.getSession();
		try {
			final long riskID = Long.parseLong(request.getParameter("riskID"));
			final long riskContingencyID = Long.parseLong(request.getParameter("riskContingencyID"));
			boolean isSaved = Risk.delRiskContingency(riskContingencyID);
			// Update Other Quality Activity
			if(isSaved) {
				Vector mitigations = new Vector();
				Vector contingencies = new Vector();
				
				mitigations = Risk.getMitigationRiskByRiskId(riskID);
				contingencies = Risk.getContigencyByRiskId(riskID);
				
				riskUpdateOtherActivity(request, response, mitigations, contingencies);
			}

			RiskCaller.riskUpdatePrepCaller(request, response);
		  }
		  catch (Exception e) {
			  e.printStackTrace();
		  }
	  }


	public static final void doLoadCommonRisks(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			//LAMNT3 - 20082010
			//Vector vtOccuredRisk = Risk.getBaselinedRisks();
			Vector vtTopCommonRisk = Risk.getTopCommonRiskSource();
			final Vector vtProcess = Project.getProcessList();
			dirtyMess(request);
			session.setAttribute("vtProcess", vtProcess);
			session.setAttribute("vtTopCommonRisk", vtTopCommonRisk);
			Fms1Servlet.callPage("riskCommon.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param request
	 * @param response
	 */
	public static final void riskIdentifyCaller(
		final HttpServletRequest request, final HttpServletResponse response)
	{
		try {
			final HttpSession session = request.getSession();
			final long projectId = Long.parseLong((String) session.getAttribute("projectID"));
			
			Vector vtCommonRiskSource = Risk.getTopCommonRiskSource();
			Vector vtRiskSource = Risk.getAllRiskSource();
			
			dirtyMess(request);
			
			session.setAttribute("vtRiskSource", vtRiskSource);
			session.setAttribute("vtCommonRiskSource", vtCommonRiskSource);
			Fms1Servlet.callPage("riskIdentify.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doImportRisk(final HttpServletRequest request, final HttpServletResponse response) throws BiffException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
		
			try {
				FileItem formFileItem = processFormField(request, upload);
				InputStream inStream = formFileItem.getInputStream();
				readImportFile(request, inStream);
				Fms1Servlet.callPage("riskList.jsp",request,response);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static final FileItem processFormField(HttpServletRequest request, ServletFileUpload upload) throws FileUploadException {
		List items = upload.parseRequest(request);
		Iterator iter = items.iterator();
		FileItem item = null;
		while (iter.hasNext()) {
			FileItem fileItem = (FileItem) iter.next();

			if (fileItem.isFormField()) {
			//	processFormField(item);
			} else {
				item = fileItem;
			}
		}
		return item;
	}
	private static final void readImportFile(HttpServletRequest request, InputStream inStream) throws BiffException, IOException {
		try{
			final HttpSession session = request.getSession();
			final DecimalFormat form = new DecimalFormat("00000000000.00");
			Workbook workbook = Workbook.getWorkbook(inStream);
			
			try{
				Sheet sheet0 = workbook.getSheet(0);
				if(!sheet0.getCell(1, 99).getContents().trim().equalsIgnoreCase("Import Risk")){
					session.setAttribute("ImportFail","fail");
					return; 
				}
			}catch(ArrayIndexOutOfBoundsException e){
				session.setAttribute("ImportFail","fail");
				return;
			}
			
			Sheet sheet = workbook.getSheet(1);
			int[] added = new int[50];
			for(int i=0;i<50;i++){
				added[i] = -1;
			}
			int k = 0;
			UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
			String srtName = "";
			if (userLoginInfo != null){
				srtName = userLoginInfo.Name;
			}
		
			java.util.Date currentDate = new java.util.Date();
			String sDate = CommonTools.dateFormat(currentDate);
			
			for(int i=1;i<51;i++){
				RiskInfo riskInfo = new RiskInfo();
				NumberCell check = (NumberCell) sheet.getCell(0, i); 
				
				if(check.getValue() > 0 ){
					riskInfo.sourceID = Long.parseLong(sheet.getCell(2, i).getContents());
					riskInfo.type = Integer.parseInt(sheet.getCell(3, i).getContents());
					riskInfo.condition = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(4, i).getContents());
					riskInfo.consequence = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(5, i).getContents());
					riskInfo.threshold = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(6, i).getContents());
					riskInfo.probability = Integer.parseInt(sheet.getCell(7, i).getContents());
					String[] impactTo = new String[6];
					impactTo[0] = sheet.getCell(8, i).getContents();
					impactTo[1] = null;
					impactTo[2] = null;
					String[] unit = new String[6];
					unit[0] = sheet.getCell(9, i).getContents();
					unit[1] = null;
					unit[2] = null;
					String[] est = new String[6];
	 				est[0] = sheet.getCell(10, i).getContents();
					est[1] = null;
					est[2] = null;
					riskInfo.plannedImpact = "";				
					for (int j = 0; j < 3; j++) {
						if ((impactTo[j] == null) || (impactTo[j].equals("")))
							impactTo[j] = "z";
						if ((unit[j] == null) || (unit[j].equals("")))
							unit[j] = "z";
						if ((est[j] == null) || (est[j].equals("")))
							est[j] = "-0000000001.00";
						else {
							est[j] = form.format(Double.parseDouble(est[j]));
						}
						riskInfo.plannedImpact += impactTo[j] + unit[j] + est[j];
					}
					riskInfo.exposure = Integer.parseInt(sheet.getCell(11, i).getContents());
					riskInfo.mitigation = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(12, i).getContents());
					riskInfo.mitigationBenefit = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(14, i).getContents());//Note
					riskInfo.mitigationActual = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(13, i).getContents());
					riskInfo.contingencyPlan = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(15, i).getContents());
					riskInfo.triggerName = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(16, i).getContents());
					riskInfo.developerAcc = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(17, i).getContents()).toUpperCase();
					final java.util.Date uDate = CommonTools.parseDate(sheet.getCell(18, i).getContents());
					riskInfo.assessmentDate = new java.sql.Date(uDate.getTime());
					riskInfo.riskStatus = Integer.parseInt(sheet.getCell(19, i).getContents());
					riskInfo.processId = Integer.parseInt(sheet.getCell(20, i).getContents()); //----processId------
					riskInfo.actualRiskScenario = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(21, i).getContents());
					riskInfo.actualAction = CommonTools.parseAllForImportFromExcelFile(sheet.getCell(22, i).getContents());
					impactTo[3] = sheet.getCell(23, i).getContents();
					impactTo[4] = null;
					impactTo[5] = null;
					unit[3] = sheet.getCell(24, i).getContents();
					unit[4] = null;
					unit[5] = null;
					est[3] = sheet.getCell(25, i).getContents();
					est[4] = null;
					est[5] = null;					
					riskInfo.actualImpact = "";
					for (int j = 3; j < 6; j++) {
						if ((impactTo[j] == null) || (impactTo[j].equals("")))
							impactTo[j] = "z";
						if ((unit[j] == null) || (unit[j].equals("")))
							unit[j] = "z";
						if ((est[j] == null) || (est[j].equals("")))
							est[j] = "-0000000001.00";
						else {
							est[j] = form.format(Double.parseDouble(est[j]));
						}
						riskInfo.actualImpact += impactTo[j] + unit[j] + est[j];
					}
					if (sheet.getCell(26, i).getContents() != null && !sheet.getCell(26, i).getContents().equalsIgnoreCase("null")) {
						if(sheet.getCell(26, i).getContents().equalsIgnoreCase("TRUE")){
							riskInfo.unplanned = 1;
						}else{
							riskInfo.unplanned = 0;
						}
					}
					
					if (riskInfo.riskStatus != 2) {
						riskInfo.unplanned = 0;
					}
					riskInfo.projectID = Long.parseLong((String) session.getAttribute("projectID"));
					
					long result = Risk.addRisk(riskInfo); 
					if(result > 0){
						added[k] = i;
					}else{
						added[k] = -i;
					}
					k++;

					final long workUnitID = Long.parseLong(session.getAttribute("projectID").toString());
					final Vector riskList = Risk.getRiskList(workUnitID);
					session.setAttribute("riskList", riskList);
					
					riskInfo.riskID = Risk.getRiskID();
					
					if (riskInfo.riskStatus == 2) {
						IssueInfo issue = Issues.getIssueByRiskId(riskInfo.riskID);
						if (issue.issueID > 0) {
							issue.comment = riskInfo.actualAction;
							Issues.updateIssue(issue);
						}
						else {
							issue.workUnitID = WorkUnit.getWorkUnitByProjectId(riskInfo.projectID);
							issue.description = "Issue from risk: " + riskInfo.condition;
							issue.statusID = 0; // Open see IssueInfo
							issue.priorityID = 2; // High
							issue.typeID = 4; //Other
							UserInfo userInfo = (UserInfo)session.getAttribute("UserLoginInfo");
							String account = userInfo.account;
							String fullname = userInfo.Name;
							issue.owner = account.toUpperCase();
							issue.creator = fullname.trim();
							issue.startDate = riskInfo.assessmentDate;
							issue.dueDate = riskInfo.assessmentDate;
							issue.comment = riskInfo.actualAction;
							issue.riskID = riskInfo.riskID;
							Issues.addIssue(issue);
						}
					}
				}
			}
			session.setAttribute("AddedRecord",added);				
			session.setAttribute("Imported","true");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
