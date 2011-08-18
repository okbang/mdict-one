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

import java.sql.Date;
import java.util.Vector;
import javax.servlet.http.*;

import com.fms1.tools.*;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.FullRiskInfo;
import com.fms1.web.Constants;
import com.fms1.web.Parameters;
import com.fms1.web.Fms1Servlet;

/**
 * Process assets pages
 * @author Linhdd
 *
 */

public final class ProcessAssetsCaller implements Constants {
	public static final void doLoadProjectDesc(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String strWuID = (String) request.getParameter("cboGroup");
			String strProjectCategory = request.getParameter("cboProjectCategory");
			String strApplicationType = request.getParameter("cboApplicationType");
			String strBusinessDomain = request.getParameter("cboBusinessDomain");
			String strCustomer = request.getParameter("cboCustomer");
			String strFromDate = request.getParameter("fromDate");
			String strToDate = request.getParameter("toDate");
			int iApplicationType;
			int iBusinessDomain;
			int iCustomer;
			
            iApplicationType = CommonTools.parseInt(strApplicationType);
			if (iApplicationType == 0) {
                strApplicationType = null;
			}
			
            iBusinessDomain = CommonTools.parseInt(strBusinessDomain);
			if (iBusinessDomain == 0) {
                strBusinessDomain = null;
			}
            
			iCustomer = CommonTools.parseInt(strCustomer);
			if (iCustomer == 0) {
                strCustomer = null;
			}
            
			Date fromDate;
			Date toDate;
            
			long lWuID = Parameters.FSOFT_WU; // FSOFT by default
			if (strWuID != null) {
                lWuID = Long.parseLong(strWuID);
			}
            
			ReportMonth rm = new ReportMonth();
			if (strFromDate == null) {
                strFromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2, 4);
			}
			if (strFromDate.equalsIgnoreCase("")) {
                strFromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2, 4);
			}
			fromDate = CommonTools.parseSQLDate(strFromDate);
			if (strToDate == null) {
                strToDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2, 4);
			}
			if (strToDate.equalsIgnoreCase("")) {
                strToDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2, 4);
			}
			toDate = CommonTools.parseSQLDate(strToDate);
			String strWhere = "";
			if ((strProjectCategory == null) || (strProjectCategory.equalsIgnoreCase("-1"))) {
				strProjectCategory = "-1";
			} else {
                strWhere = " AND PROJECT.CATEGORY = '" + strProjectCategory + "' ";
			}

			if (strApplicationType != null) {
				if (!strApplicationType.equalsIgnoreCase("0")) {
					strWhere = strWhere + " AND PROJECT_PLAN.APPLICATION_TYPE = " + strApplicationType + " ";
				}
			}
			if (strBusinessDomain != null) {
				if (!strBusinessDomain.equalsIgnoreCase("0")) {
					strWhere = strWhere + " AND PROJECT_PLAN.DOMAIN = '" + strBusinessDomain + "' ";
				}
			}
			if (strCustomer != null) {
				if (!strCustomer.equalsIgnoreCase("0")) {
					strWhere = strWhere + " AND PROJECT.CUSTOMER LIKE '" + strCustomer + "%' ";
				}
			}
            
			Vector vtClosedProject = WorkUnit.getClosedProjects(lWuID, fromDate, toDate, strWhere);
			Vector vtProjectList = new Vector();
            Vector vtProcessAssetsList = new Vector();
            
            ProjectInfo projectInfo;
            Date dtActualFinishDate;
            
			int numProjects = vtClosedProject.size();
            for (int i = 0; i < numProjects; i++) {
                projectInfo = (ProjectInfo) vtClosedProject.elementAt(i);
                dtActualFinishDate = projectInfo.getActualFinishDate();
                projectInfo = Project.getProjectInfo(projectInfo.getProjectId());
                ProcessAssetsInfo processAssetsInfo = new ProcessAssetsInfo();
                
				final Vector devList = Assignments.getAllUserAssignment(projectInfo.getProjectId(), "", "", 0);
				final Vector vtTool = Project.getToolList(projectInfo.getProjectId());
                final int j = devList.size();
				
                String strDevList = "";
				
                if (j > 0) {
					for (int k = 0; k < j; k++) {
						AssignmentInfo devInfo = (AssignmentInfo) devList.elementAt(k);
						strDevList = strDevList + devInfo.devName + "; ";
					}
				}
                processAssetsInfo.setTeam(strDevList);
				final int l = vtTool.size();
				if (l > 0) {
					String osList = "";
					String dbmsList = "";
					String languageList = "";
					String swToolList = "";
					String hardwareList = "";
					for (int m = 0; m < l; m++) {
						ToolInfo toolInfo = (ToolInfo) vtTool.elementAt(m);
						if (toolInfo.tool_type == 1) {
							osList = osList + toolInfo.name;
                            processAssetsInfo.setOsList(osList);
						}
						if (toolInfo.tool_type == 2) {
							dbmsList = dbmsList + toolInfo.name;
                            processAssetsInfo.setDbmsList(dbmsList);
						}
						if (toolInfo.tool_type == 3) {
							languageList = languageList + toolInfo.name;
                            processAssetsInfo.setLanguageList(languageList);
						}
						if (toolInfo.tool_type == 4) {
							swToolList = swToolList + toolInfo.name;
                            processAssetsInfo.setSwToolList(swToolList);
						}
						if (toolInfo.tool_type == 5) {
							hardwareList = hardwareList + toolInfo.name;
                            processAssetsInfo.setHardwareList(hardwareList);
						}
					}
				}
				if (dtActualFinishDate != null) {
					String strActualFinishDate = CommonTools.dateFormat(dtActualFinishDate);
                    processAssetsInfo.setYear("20" + strActualFinishDate.substring(7, 9));
				}
                vtProjectList.addElement(projectInfo);
				vtProcessAssetsList.addElement(processAssetsInfo);
			}
            
			final Vector apptypeList = Param.getAppTypeList();
			final Vector bizdomainList = Param.getBizDomainList();
			final Vector vtCustomer = Project.getCustomerList();
			
            session.setAttribute("vtCustomer", vtCustomer);
			session.setAttribute("wuID", String.valueOf(lWuID));
			session.setAttribute("strProjectCategory", strProjectCategory);
			session.setAttribute("strApplicationType", strApplicationType);
			session.setAttribute("strBusinessDomain", strBusinessDomain);
			session.setAttribute("strCustomer", strCustomer);
			session.setAttribute("fromDate", strFromDate);
			session.setAttribute("toDate", strToDate);
			session.setAttribute("bizdomainList", bizdomainList);
			session.setAttribute("apptypeList", apptypeList);
            session.setAttribute("projectInfoList", vtProjectList);
            session.setAttribute("processAssetsList", vtProcessAssetsList);
			Fms1Servlet.callPage("paProjectDesc.jsp", request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doLoadProjectDetails(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			String strProjectCode = (String) request.getParameter("projectCode");
			final long projectId = Project.getProjectId(strProjectCode);
			final Vector vtDevList = Assignments.getAllUserAssignment(projectId, "", "", 0);
			final Vector vtTool = Project.getToolList(projectId);
			ProjectInfo projectInfo = Project.getProjectInfo(projectId);
			session.setAttribute("projectId", String.valueOf(projectId));
			session.setAttribute("devList", vtDevList);
			session.setAttribute("tool", vtTool);
			session.setAttribute("projectInfo", projectInfo);
			Fms1Servlet.callPage("paProjectDetail.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doLoadTailorings(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String strWuID = (String) request.getParameter("cboGroup");
			String strTailoringType = request.getParameter("cboTailoringType");
			String strTailoringCategory = request.getParameter("cboTailoringCategory");
			String strFromDate = request.getParameter("fromDate");
			String strToDate = request.getParameter("toDate");
			Date fromDate;
			Date toDate;
			long lWuID =(strWuID != null)?Long.parseLong(strWuID): Parameters.FSOFT_WU; // FSOFT by default
			
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
            if (strTailoringType == null ) {
                strTailoringType = "-1";
            }
            int type = Integer.parseInt(strTailoringType);
			String strWhere = " ", strWhereTail = " ", strWhereDeviation =" ";
            if (strTailoringCategory != null) {
                if (!strTailoringCategory.equalsIgnoreCase("-1")) {
                    switch (type) {
                        case -1:
                            strWhereTail =  " AND e.NAME = '" + strTailoringCategory + "' ";
                            strWhereDeviation =  " AND c.CATEGORY = '" + strTailoringCategory + "' ";
                            break;        
                        case 1:
                            strWhere =  " AND e.NAME = '" + strTailoringCategory + "' ";
                            break;
                        case 2:
                            strWhere =  " AND c.CATEGORY = '" + strTailoringCategory + "' ";
                            break;
                    }
                     
                }
            }
            
            Vector vtTailoringDeviation = new Vector() ;
            switch (type) {
                case -1:
                    vtTailoringDeviation = Assets.getTailoringDeviationList(lWuID, fromDate, toDate, strWhereTail, strWhereDeviation);                                   
                    break;
                case 1:
                    vtTailoringDeviation = Assets.getTailoringList(lWuID, fromDate, toDate, strWhere);
                    break;
                case 2:
                    vtTailoringDeviation = Assets.getDeviationList(lWuID, fromDate, toDate, strWhere);
                    break;
            }
		    Vector catList = Assets.getTDCategory();
          	session.setAttribute("wuID", String.valueOf(lWuID));
			session.setAttribute("strTailoringType", strTailoringType);
			session.setAttribute("strTailoringCategory", strTailoringCategory);
			session.setAttribute("fromDate", strFromDate);
			session.setAttribute("toDate", strToDate);
           
			session.setAttribute("tailoring_category", catList);
			session.setAttribute("vtTailoringDeviation", vtTailoringDeviation);
          
			Fms1Servlet.callPage("paTailoring.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doLoadRisksEncountered(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String strWuID = (String) request.getParameter("cboGroup");
			String strIsPlaned = request.getParameter("cboIsPlaned");
			String strProcess = request.getParameter("cboProcess");
			String strFromDate = request.getParameter("fromDate");
			String strToDate = request.getParameter("toDate");
			Date fromDate;
			Date toDate;
			long lWuID = Parameters.FSOFT_WU; // FSOFT by default
			if (strWuID != null)
				lWuID = Long.parseLong(strWuID);
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
			Vector vtOccuredRisk = Risk.getOccuredRiskList(lWuID, fromDate, toDate, strWhere);
			Vector vtOccuredRiskFull = new Vector();
			for (int i=0;i<vtOccuredRisk.size();i++){
				RiskInfo temp = (RiskInfo)vtOccuredRisk.get(i); 
				RiskInfo temp1 = (RiskInfo) Risk.getRiskById(temp.riskID);
				temp.condition = temp1.condition;
				Vector temp2 = Risk.getMitigationRiskByRiskId(temp.riskID);
				Vector temp3 = Risk.getContigencyByRiskId(temp.riskID);
				FullRiskInfo temp4 = new FullRiskInfo();
				String tempMiti = "";
				String tempConti = "";
				
				for (int k1=0;k1<temp2.size();k1++){
					RiskMitigationInfo tempRiskMiti;
					try {
						tempRiskMiti = (RiskMitigationInfo)temp2.get(k1);
						tempRiskMiti.mitigation = tempRiskMiti.mitigation.trim();
						if (tempRiskMiti.mitigation.charAt(0)!='-')
						tempMiti = tempMiti + " - " + tempRiskMiti.mitigation + "\r\n";
						else tempMiti = tempMiti + tempRiskMiti.mitigation + "\r\n"; 
					}
					catch(Exception e){
					}
					 
				}
				
				for (int k1=0;k1<temp3.size();k1++){
					RiskContingencyInfo tempRiskConti;
					try {
						tempRiskConti = (RiskContingencyInfo)temp3.get(k1);
						tempRiskConti.contingency = tempRiskConti.contingency.trim();
						if (tempRiskConti.contingency.charAt(0)!='-')
						tempConti = tempConti + " - " + tempRiskConti.contingency + "\r\n";
						else tempConti = tempConti + tempRiskConti.contingency + "\r\n"; 
					}
					catch(Exception e){
					}
					 
				}
				
				temp.contingencyPlan = tempConti ; 
				temp.mitigation = tempMiti ;   
				
				temp4.setRiskInfo(temp);
				temp4.setRiskContingencyInfo(temp3);
				temp4.setRiskMitigationInfo(temp2);
				vtOccuredRiskFull.add(temp4);
			}
			final Vector vtProcess = Project.getProcessList();
			session.setAttribute("vtProcess", vtProcess);
			session.setAttribute("wuID", String.valueOf(lWuID));
			session.setAttribute("strIsPlaned", strIsPlaned);
			session.setAttribute("strProcess", strProcess);
			session.setAttribute("fromDate", strFromDate);
			session.setAttribute("toDate", strToDate);
			session.setAttribute("vtOccuredRisk", vtOccuredRiskFull);
			Fms1Servlet.callPage("paRisk.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doLoadPracticeLesson(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String strWuID = (String) request.getParameter("cboGroup");
			String strLessonProcess = request.getParameter("cboLessonProcess");
			String strLessonType = request.getParameter("cboLessonType");
			String strFromDate = request.getParameter("fromDate");
			String strToDate = request.getParameter("toDate");
			Date fromDate;
			Date toDate;
			long lWuID = Parameters.FSOFT_WU; // FSOFT by default
			if (strWuID != null)
				lWuID = Long.parseLong(strWuID);
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
			if (strLessonProcess != null) {
				if (!strLessonProcess.equalsIgnoreCase("-1")) {
					strWhere = strWhere + " AND c.CATEGORY = '" + strLessonProcess + "' ";
				}
			}
			if (strLessonType != null) {
				if (!strLessonType.equalsIgnoreCase("-1")) {
					strWhere = strWhere + " AND c.TYPE = " + strLessonType + " ";
				}
			}
			Vector vtPracticeList = Assets.getPracticeList(lWuID, fromDate, toDate, strWhere);
			Vector vtProcess = Project.getProcessList();
			session.setAttribute("vtProcess", vtProcess);
			session.setAttribute("wuID", String.valueOf(lWuID));
			session.setAttribute("fromDate", strFromDate);
			session.setAttribute("toDate", strToDate);
			session.setAttribute("strLessonProcess", strLessonProcess);
			session.setAttribute("strLessonType", strLessonType);
			session.setAttribute("vtPracticeList", vtPracticeList);
			Fms1Servlet.callPage("paPractice.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doLoadIssue(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String strWuID = (String) request.getParameter("cboGroup");
			String strIssueType = request.getParameter("cboIssueType");
			String strProcess = request.getParameter("cboProcess");
			String strFromDate = request.getParameter("fromDate");
			String strToDate = request.getParameter("toDate");
			Date fromDate;
			Date toDate;
			long lWuID = Parameters.FSOFT_WU; // FSOFT by default
			if (strWuID != null)
				lWuID = Long.parseLong(strWuID);
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
			if (strIssueType != null) {
				if (!strIssueType.equalsIgnoreCase("-1")) {
					strWhere = strWhere + " AND c.TYPEID = " + strIssueType + " ";
				}
			}
			if (strProcess != null) {
				if (!strProcess.equalsIgnoreCase("0")) {
					strWhere = strWhere + " AND c.PROCESS_ID = " + strProcess + " ";
				}
			}
			Vector vtIssueList = Issues.getIssueList(lWuID, fromDate, toDate, strWhere);
			Vector vtProcess = Project.getProcessList();
			session.setAttribute("vtProcess", vtProcess);
			session.setAttribute("wuID", String.valueOf(lWuID));
			session.setAttribute("fromDate", strFromDate);
			session.setAttribute("toDate", strToDate);
			session.setAttribute("strIssueType", strIssueType);
			session.setAttribute("strProcess", strProcess);
			session.setAttribute("vtIssueList", vtIssueList);
			Fms1Servlet.callPage("paIssue.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doBaselineRisks(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector occuredRisk = (Vector) session.getAttribute("vtOccuredRisk");
			final String [] riskIDsTemp = request.getParameterValues("baselined");
			int  [] riskIDs=ConvertString.arrayToArray(riskIDsTemp);
			RiskInfo info ;
			Vector tempVt=new Vector();
			boolean found;
			for(int i = 0 ;i < occuredRisk.size(); i++){
				
				found=false;
		 		info = ((FullRiskInfo)occuredRisk.get(i)).getRiskInfo();
		 		if (riskIDs!=null){
		 			for (int k =0;k<riskIDs.length;k++){
		 				if (i==riskIDs[k]){
		 					found=true;
			 				 if(!info.baselined){
			 					tempVt.add(info);
			 					break;
			 				}
		 				}
		 			}
		 		}
		 		if (!found && info.baselined){
		 			tempVt.add(info);
		 		}
			}
			if (tempVt.size()>0){
				long ids[]= new long[tempVt.size()];
				for (int i=0;i<tempVt.size();i++){
					info = (RiskInfo) tempVt.elementAt(i);
					ids[i]=info.riskID;
				}
				Risk.updateBaseline(ids);
			}
			doLoadRisksEncountered(request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}