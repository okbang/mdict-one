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
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fms1.infoclass.*;
import com.fms1.tools.*;
import com.fms1.web.Constants;
import com.fms1.web.Fms1Servlet;
import java.util.Hashtable;
/**
 * Quality pages of Project plan
 *
 */
public final class QualityObjectiveCaller implements Constants {
	/**
	 * Get all modules for one project.
	 */
	public static final void doGetQualityObjectiveList(final HttpServletRequest request, final HttpServletResponse response) {
		doGetQualityObjectiveList(request, response, "");
	}
	public static final void doGetQualityObjectiveList(final HttpServletRequest request, final HttpServletResponse response,String index) {
		try {
			final HttpSession session = request.getSession();
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			Vector stageList= Schedule.getStageList(prjID);
			if(stageList==null)
			{
				stageList= new Vector();
			}
			session.setAttribute("stageList",stageList);
            ProjectInfo projectInfo = Project.getProjectInfo(prjID);
            session.setAttribute("projectInfo", projectInfo);
			session.setAttribute("caller", String.valueOf(QUALITY_OBJECTIVE_CALLER));
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);           
			final String str = QualityObjective.getQualityObjective(prjID);
			
			final Vector vtStrategyOfMeeting = QualityObjective.getStrategyOfMeetingList(prjID);
			session.setAttribute("StratOfMeetingList", vtStrategyOfMeeting);
			
			final Vector vtReviewStrategy = QualityObjective.getReviewStrategyList(prjID);
			session.setAttribute("ReviewStrategyList", vtReviewStrategy);
			
			final Vector vtTestStrategy = TestStrategy.getTestStrategyList(prjID);
			session.setAttribute("TestStrategyList", vtTestStrategy);
			
			final Vector info = MeasurementsProg.getMeasurementsProgList(prjID);
			session.setAttribute("MeasurementsProgList", info);
			
			session.setAttribute("qltObjective", str);
			final Vector vtModule = WorkProduct.getModuleListSchedule(prjID, WorkProduct.ORDER_BY_PRELEASE);
			session.setAttribute("moduleVector", vtModule);
			final Vector vtDeveloper = UserHelper.getAllUsers();
			session.setAttribute("devVector", vtDeveloper);
			final Vector vtOtherAct = QualityObjective.getOtherActivityList(prjID);
			session.setAttribute("otherActVector", vtOtherAct);
			Vector userList = Assignments.getAllUserAssignment(prjID, "", "", 0);
            session.setAttribute("userList", userList);
            final Vector darVt = Dar.getDarPlan(prjID);
			session.setAttribute("dar", darVt);
			
			// HaiMM add - Start
			final Vector estDefect = Defect.getEstDefect(prjID);
			session.setAttribute("estDefect", estDefect);
			
			EstDefectInfo estDefInfo;
			double sumPlanValue = 0;
			for (int i =0;i<estDefect.size();i++){
				estDefInfo=(EstDefectInfo)estDefect.get(i);
				sumPlanValue += estDefInfo.target;
			}
			
			session.setAttribute("sumPlanValue", Double.toString(sumPlanValue));
			
			final Vector performanceVector = Project.getPerformanceMetrics(prjID);
			session.setAttribute("WOPerformanceVector", performanceVector);
			final Vector stdMetrics = Project.getStandardMetricList(prjID);
			session.setAttribute("WOStandardMetricMatrix", stdMetrics);
			//
			
			StageInfo stageInfo = null;
					for (int i = stageList.size() - 1; i >= 0; i--) {
						stageInfo = (StageInfo) stageList.elementAt(i);
						if ((stageInfo.aEndD != null)
							&& (stageInfo.actualBeginDate != null)
							&& (stageInfo.plannedEndDate != null)
							&& (stageInfo.plannedBeginDate != null)) {
							break;
						}
					}
			
//			anhtv08 -start
            
			Vector dpVt;
			Vector cmList;
			if(stageInfo==null){
				 dpVt= new Vector();
				cmList= new Vector();
			}
			else 
			{
				 dpVt = Defect.getDPTask(prjID,stageInfo.milestoneID);
				cmList = Project.getCustomerMetric(prjID,stageInfo.milestoneID);
			}
			session.setAttribute("defectPrevention", dpVt);
			session.setAttribute("StageInfo",stageInfo);
			session.setAttribute("WOCustomeMetricList", cmList);
			final ProjectInfo pinf = Project.getProjectInfo(prjID);
			final EffortInfo effortHeader = Effort.getEffortInfo(pinf);
			session.setAttribute("effortHeaderInfo", effortHeader);
			
			final DefectByProcessInfo[] defectProcess = Defect.getWeigthedDefectByOrigin(prjID);
			session.setAttribute("defectProcess", defectProcess);
			
			final Vector moduleList = WorkProduct.getDefectModuleListSize(prjID);
			session.setAttribute("defectModuleList", moduleList);
			
			final Hashtable Process_WorkProduct =  WorkProduct.getProcessWorkProductList();
			session.setAttribute("Process_WP",Process_WorkProduct);
			
			// HaiMM add - End
			
			session.setAttribute("SourcePage", "1");
			Fms1Servlet.callPage("qualityObjective.jsp"+index,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateModule(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			WorkProductCaller.doUpdateModule(request);
			doGetQualityObjectiveList(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateObjective(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			String qltObjective = request.getParameter("txtQltObjective").trim();
			if (QualityObjective.updateQualityObjective(prjID, qltObjective)) {
				doGetQualityObjectiveList(request, response);
				return;
			}
			else {
				System.err.println("QualityObjectiveCaller.doUpdateObjective : Error");
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/********************************OtherActivity********************************************/
	public static final void doAddOtherAct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			WorkProductCaller.doAddOtherAct(request);
			doGetQualityObjectiveList(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateOtherAct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			WorkProductCaller.doUpdateOtherAct(request, response);
			doGetQualityObjectiveList(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeleteOtherAct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			WorkProductCaller.doDeleteOtherAct(request, response);
			doGetQualityObjectiveList(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static final void doAddProductsPrepare(final HttpServletRequest request,final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();	
			final Vector methodList = Param.getEstimationMethodList();
			final Vector languageList = Param.getLanguageList();
			final Vector workProductList = WorkProduct.getWPList();
			final Vector relevantMethodList = Param.getRelevantEstimationMethodList();
			final Vector relevantLanguageList = Param.getRelevantLanguageList();
			session.setAttribute("relevantMethodList", relevantMethodList);
			session.setAttribute("relevantLanguageList", relevantLanguageList);
			session.setAttribute("methodList", methodList);
			session.setAttribute("languageList", languageList);
			session.setAttribute("workProductList", workProductList);
			request.setAttribute("fromPage","qual");
			Fms1Servlet.callPage("moduleAddnew.jsp",request,response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static final void doPrepareAddTestStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plTestStrategyAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static final void doPrepareUpdateTestStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plTestStrategyUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static final void doPrepareAddReviewStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plReviewStrategyAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static final void doPrepareUpdateReviewStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plReviewStrategyUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doAddTestStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));			
			Vector errDataVector = new Vector();
			TestStrategyInfo testInfo = null;			
			int iRet = 0;			

			String[] aTestItem = request.getParameterValues("test_item");
			String[] aTestType = request.getParameterValues("test_type");
			String[] aTestReviewer = request.getParameterValues("test_reviewer");
			String[] aTestComplCriteria = request.getParameterValues("test_completion_criteria");
			String[] aTestEntryCriteria = request.getParameterValues("test_entry_criteria");

			int size = aTestItem.length;
			for (int i = 0; i < size; i++) {
				if (aTestItem[i] == null || "".equals(aTestItem[i].trim())) continue;			
				testInfo = new TestStrategyInfo();
								
				testInfo.testItem = ConvertString.toStandardizeString(aTestItem[i].trim());										
				testInfo.testType = Integer.parseInt(aTestType[i].trim());						
				testInfo.testReviewer = ConvertString.toStandardizeString(aTestReviewer[i].trim());				
				testInfo.testComplCriteria = ConvertString.toStandardizeString(aTestComplCriteria[i].trim());
				testInfo.testEntryCriteria = ConvertString.toStandardizeString(aTestEntryCriteria[i].trim());

				// If not system error then call add strategy				
				if (iRet != 1) iRet = TestStrategy.doAddPLTestStrategy(testInfo, prjID);
				// If error then add to vector
				if (iRet != 0) {					
					errDataVector.addElement(testInfo);
				} else {
					ProjectPlanCaller.addChangeAuto(  prjID,
									Constants.ACTION_ADD,
									Constants.PL_TEST_STRATEGY_ADD,
									"Test strategy",
									testInfo.testItem,
									null);
				}
			}			

			if ( iRet == 0) {				
				Vector testList = TestStrategy.getTestStrategyList(prjID);
				if (testList == null) {
					testList = new Vector();
				}
				session.setAttribute("TestStrategyList", testList);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
			} else {
				request.setAttribute("ErrTestStrategyList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("plTestStrategyAdd.jsp",request,response);			
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdateTestStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			int size = 0;			
			Vector vNewTest = new Vector();
			Vector vOldTest = (Vector) session.getAttribute("TestStrategyList");
			Vector vErrTest = new Vector();

			TestStrategyInfo newTestInfo = null;
			TestStrategyInfo oldTestInfo = null;
		
			String[] aTestID = request.getParameterValues("testID");
			String[] aTestItem = request.getParameterValues("test_item");
			String[] aTestType = request.getParameterValues("test_type");
			String[] aTestReviewer = request.getParameterValues("test_reviewer");
			String[] aTestComplCriteria = request.getParameterValues("test_completion_criteria");
			String[] aTestEntryCriteria = request.getParameterValues("test_entry_criteria");

			if (aTestItem != null) size = aTestItem.length;
			for (int i = 0; i < size; i++) {
				newTestInfo = new TestStrategyInfo();
				oldTestInfo = (TestStrategyInfo) vOldTest.elementAt(i);
			
				if (aTestItem[i] == null) aTestItem[i] = "";
				else aTestItem[i] = ConvertString.toStandardizeString(aTestItem[i]);

				if (aTestReviewer[i] == null) aTestReviewer[i] = "";
				else aTestReviewer[i] = ConvertString.toStandardizeString(aTestReviewer[i]);
				
				if (aTestComplCriteria[i] == null) aTestComplCriteria[i] = "";
				else aTestComplCriteria[i] = ConvertString.toStandardizeString(aTestComplCriteria[i]);
				
				if (aTestEntryCriteria[i] == null) aTestEntryCriteria[i] = "";
				else aTestEntryCriteria[i] = ConvertString.toStandardizeString(aTestEntryCriteria[i]);
			
			
				if (oldTestInfo.testItem == null) oldTestInfo.testItem = "";				
				if (oldTestInfo.testReviewer == null) oldTestInfo.testReviewer = "";
				if (oldTestInfo.testComplCriteria == null) oldTestInfo.testComplCriteria = "";
				if (oldTestInfo.testEntryCriteria == null) oldTestInfo.testEntryCriteria = "";
			
				newTestInfo.testItem 		= 	aTestItem[i];
				newTestInfo.testType = Integer.parseInt(aTestType[i]);
				newTestInfo.testReviewer 	= 	aTestReviewer[i];
				newTestInfo.testComplCriteria 	= 	aTestComplCriteria[i];
				newTestInfo.testEntryCriteria 	= 	aTestEntryCriteria[i];
				newTestInfo.testID = Long.parseLong(aTestID[i]);

				if ( aTestItem[i].compareToIgnoreCase(oldTestInfo.testItem) != 0
					 || newTestInfo.testType != oldTestInfo.testType
					 || aTestReviewer[i].compareToIgnoreCase(oldTestInfo.testReviewer) != 0
					 || aTestComplCriteria[i].compareToIgnoreCase(oldTestInfo.testComplCriteria) != 0
					 || aTestEntryCriteria[i].compareToIgnoreCase(oldTestInfo.testEntryCriteria) != 0
				) 
				{
					ProjectPlanCaller.addChangeAuto(prjID,
								Constants.ACTION_UPDATE,
								Constants.PL_TEST_STRATEGY_UPDATE,
								"Test strategy",
								aTestItem[i],
								oldTestInfo.testItem);
					vNewTest.addElement(newTestInfo);
				}				
				vErrTest.addElement(newTestInfo);
			}

			if (TestStrategy.doUpdatePLTestStrategy(vNewTest,prjID)==0) {				
				Vector testList = TestStrategy.getTestStrategyList(prjID);
				if (testList == null) {
					testList = new Vector();
				}
				session.setAttribute("TestStrategyList", testList);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
			} else {
				request.setAttribute("ErrTestStrategyList",vErrTest);
				request.setAttribute("ErrType","1");
				Fms1Servlet.callPage("plTestStrategyUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeleteTestStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector vErrTest = new Vector();
			int size = 0;
		
			long delTestID = Long.parseLong(request.getParameter("delTestID"));

			TestStrategyInfo newTestInfo = null;
		
			String[] aTestID = request.getParameterValues("testID");
			String[] aTestItem = request.getParameterValues("test_item");
			String[] aTestType = request.getParameterValues("test_type");
			String[] aTestReviewer = request.getParameterValues("test_reviewer");
			String[] aTestComplCriteria = request.getParameterValues("test_completion_criteria");
			String[] aTestEntryCriteria = request.getParameterValues("test_completion_criteria");
			
			if (aTestItem != null) size = aTestItem.length;
			for (int i = 0; i < size; i++) {
				newTestInfo = new TestStrategyInfo();				

				if (aTestItem[i] == null) aTestItem[i] = "";
				else aTestItem[i] = ConvertString.toStandardizeString(aTestItem[i]);

				if (aTestReviewer[i] == null) aTestReviewer[i] = "";
				else aTestReviewer[i] = ConvertString.toStandardizeString(aTestReviewer[i]);
	
				if (aTestComplCriteria[i] == null) aTestComplCriteria[i] = "";
				else aTestComplCriteria[i] = ConvertString.toStandardizeString(aTestComplCriteria[i]);
				
				if (aTestEntryCriteria[i] == null) aTestEntryCriteria[i] = "";
				else aTestEntryCriteria[i] = ConvertString.toStandardizeString(aTestEntryCriteria[i]);

				newTestInfo.testItem 		= 	aTestItem[i];
				newTestInfo.testReviewer 	= 	aTestReviewer[i];
				newTestInfo.testComplCriteria 	= 	aTestComplCriteria[i];
				newTestInfo.testEntryCriteria 	= 	aTestEntryCriteria[i];
				newTestInfo.testType = Integer.parseInt(aTestType[i]);
				newTestInfo.testID = Long.parseLong(aTestID[i]);
			
				vErrTest.addElement(newTestInfo);				
			}
		
			if (TestStrategy.doDeletePLTestStrategy(delTestID)) {
				ProjectPlanCaller.addChangeAuto(	prjID,
								Constants.ACTION_DELETE,
								Constants.PL_TEST_STRATEGY_DELETE,
								"Test strategy",
								null,
								null);
				Vector testList = TestStrategy.getTestStrategyList(prjID);
				if (testList == null) {
					testList = new Vector();
				}
				session.setAttribute("TestStrategyList", testList);							
			} else {
				request.setAttribute("ErrTestStrategyList",vErrTest);
				request.setAttribute("ErrType","1");				
			}
			Fms1Servlet.callPage("plTestStrategyUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// end test strategy
	
	public static final void doAddReviewStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));			
			Vector errDataVector = new Vector();
			ReviewStrategyInfo revInfo = null;			
			int iRet = 0;

			String[] aRevItem = request.getParameterValues("rev_item");
			String[] aRevType = request.getParameterValues("rev_type");
			String[] aRevReviewer = request.getParameterValues("rev_reviewer");
			String[] aRevWhen = request.getParameterValues("rev_when");

			int size = aRevItem.length;
			for (int i = 0; i < size; i++) {
				if (aRevItem[i] == null || "".equals(aRevItem[i].trim())) continue;			
				revInfo = new ReviewStrategyInfo();	
		
				// Review item				
				revInfo.revItem = ConvertString.toStandardizeString(aRevItem[i].trim());
		
				// Type of review				
				revInfo.revType = ConvertString.toStandardizeString(aRevType[i].trim());
				
				// Reviewer				
				revInfo.revReviewer = ConvertString.toStandardizeString(aRevReviewer[i].trim());
							 
				// When				
				revInfo.revWhen = ConvertString.toStandardizeString(aRevWhen[i].trim());
			

				// If not system error then call add strategy				
				if (iRet != 1) iRet = QualityObjective.doAddPLReviewStrategy(revInfo, prjID);
				// If error then add to vector
				if (iRet != 0) {
					errDataVector.addElement(revInfo);
				} else {
					ProjectPlanCaller.addChangeAuto(  prjID,
									Constants.ACTION_ADD,
									Constants.PL_REVIEW_STRATEGY_ADD,
									"Review strategy",
									revInfo.revItem,
									null);
				}
			}
			if ( iRet == 0) {				
				Vector revList = QualityObjective.getReviewStrategyList(prjID);
				if (revList == null) {
					revList = new Vector();
				}
				session.setAttribute("ReviewStrategyList", revList);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
			} else {
				request.setAttribute("ErrReviewStrategyList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("plReviewStrategyAdd.jsp",request,response);			
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doUpdateReviewStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			int size = 0;			
			Vector vNewRev = new Vector();
			Vector vOldRev = (Vector) session.getAttribute("ReviewStrategyList");
			Vector vErrRev = new Vector();

			ReviewStrategyInfo newRevInfo = null;
			ReviewStrategyInfo oldRevInfo = null;
		
			String[] aRevID = request.getParameterValues("revID");
			String[] aRevItem = request.getParameterValues("rev_item");
			String[] aRevType = request.getParameterValues("rev_type");
			String[] aRevReviewer = request.getParameterValues("rev_reviewer");
			String[] aRevWhen = request.getParameterValues("rev_when");

			if (aRevItem != null) size = aRevItem.length;
			for (int i = 0; i < size; i++) {
				newRevInfo = new ReviewStrategyInfo();
				oldRevInfo = (ReviewStrategyInfo) vOldRev.elementAt(i);
			
				if (aRevItem[i] == null) aRevItem[i] = "";
				else aRevItem[i] = ConvertString.toStandardizeString(aRevItem[i]);

				if (aRevReviewer[i] == null) aRevReviewer[i] = "";
				else aRevReviewer[i] = ConvertString.toStandardizeString(aRevReviewer[i]);
				
				if (aRevWhen[i] == null) aRevWhen[i] = "";
				else aRevWhen[i] = ConvertString.toStandardizeString(aRevWhen[i]);
			
			
				if (oldRevInfo.revItem == null) oldRevInfo.revItem = "";				
				if (oldRevInfo.revReviewer == null) oldRevInfo.revReviewer = "";
				if (oldRevInfo.revWhen == null) oldRevInfo.revWhen = "";
			
				newRevInfo.revItem 		= 	aRevItem[i];
				newRevInfo.revReviewer 	= 	aRevReviewer[i];
				newRevInfo.revWhen 	= 	aRevWhen[i];
				newRevInfo.revType = (aRevType[i].trim());
				newRevInfo.revID = Long.parseLong(aRevID[i]);

				if ( aRevItem[i].compareToIgnoreCase(oldRevInfo.revItem) != 0
					 || newRevInfo.revType != oldRevInfo.revType
					 || aRevReviewer[i].compareToIgnoreCase(oldRevInfo.revReviewer) != 0
					 || aRevWhen[i].compareToIgnoreCase(oldRevInfo.revWhen) != 0
				) 
				{
					ProjectPlanCaller.addChangeAuto(prjID,
								Constants.ACTION_UPDATE,
								Constants.PL_REVIEW_STRATEGY_UPDATE,
								"Review strategy",
								aRevItem[i],
								oldRevInfo.revItem);
					vNewRev.addElement(newRevInfo);
				}				
				vErrRev.addElement(newRevInfo);				
			}

			if (QualityObjective.doUpdatePLReviewStrategy(vNewRev,prjID)==0) {				
				Vector revList = QualityObjective.getReviewStrategyList(prjID);
				if (revList == null) {
					revList = new Vector();
				}
				session.setAttribute("ReviewStrategyList", revList);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
			} else {
				request.setAttribute("ErrReviewStrategyList",vErrRev);
				request.setAttribute("ErrType","1");
				Fms1Servlet.callPage("plReviewStrategyUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void doDeleteReviewStrat(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector vErrRev = new Vector();
			int size = 0;
		
			long stratID = Long.parseLong(request.getParameter("delRevID"));

			ReviewStrategyInfo newRevInfo = null;
		
			String[] aRevID = request.getParameterValues("revID");
			String[] aRevItem = request.getParameterValues("rev_item");
			String[] aRevType = request.getParameterValues("rev_type");
			String[] aRevReviewer = request.getParameterValues("rev_reviewer");
			String[] aRevWhen = request.getParameterValues("rev_when");
			
			if (aRevItem != null) size = aRevItem.length;
			for (int i = 0; i < size; i++) {
				newRevInfo = new ReviewStrategyInfo();				

				if (aRevItem[i] == null) aRevItem[i] = "";
				else aRevItem[i] = ConvertString.toStandardizeString(aRevItem[i]);

				if (aRevReviewer[i] == null) aRevReviewer[i] = "";
				else aRevReviewer[i] = ConvertString.toStandardizeString(aRevReviewer[i]);
	
				if (aRevWhen[i] == null) aRevWhen[i] = "";
				else aRevWhen[i] = ConvertString.toStandardizeString(aRevWhen[i]);

				newRevInfo.revItem 		= 	aRevItem[i];
				newRevInfo.revReviewer 	= 	aRevReviewer[i];
				newRevInfo.revWhen 	= 	aRevWhen[i];
				newRevInfo.revType = (aRevType[i].trim());
				newRevInfo.revID = Long.parseLong(aRevID[i]);
			
				vErrRev.addElement(newRevInfo);				
			}
		
			if (QualityObjective.doDeletePLReviewStrategy(stratID)) {
				ProjectPlanCaller.addChangeAuto(	prjID,
								Constants.ACTION_DELETE,
								Constants.PL_REVIEW_STRATEGY_DELETE,
								"Review strategy",
								null,
								null);
				Vector revList = QualityObjective.getReviewStrategyList(prjID);
				if (revList == null) {
					revList = new Vector();
				}
				session.setAttribute("ReviewStrategyList", revList);							
			} else {
				request.setAttribute("ErrReviewStrategyList",vErrRev);
				request.setAttribute("ErrType","1");				
			}
			Fms1Servlet.callPage("plReviewStrategyUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static final void doPrepareAddStratForMeeting(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plQO_StratForMeetingAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static final void doPrepareUpdateStratForMeeting(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("plQO_StratForMeetingUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Add Strategy for meeting quality objectives start
	public static final void doAddStratForMeeting(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));			
			Vector errDataVector = new Vector();
			StrategyOfMeetingInfo stratInfo = null;			
			int iRet = 0;
			boolean isExist = false;

			String[] aStratDesc = request.getParameterValues("strat_desc");
			String[] aStratExBene = request.getParameterValues("strat_ex_bene");

			int size = aStratDesc.length;
			for (int i = 0; i < size; i++) {
				if (aStratDesc[i] == null || "".equals(aStratDesc[i].trim())) continue;			
				stratInfo = new StrategyOfMeetingInfo();	
			
				// Strategy				
				stratInfo.stratDesc = ConvertString.toStandardizeString(aStratDesc[i].trim());
			
				// Expected benefits				
				stratInfo.stratExBene = ConvertString.toStandardizeString(aStratExBene[i].trim());			
				

				// If not system error then call add strategy				
				if (iRet != 1) iRet = QualityObjective.doAddPLStrategyForMeeting(stratInfo, prjID);
				// If error then add to vector
				if (iRet != 0) {
					if (iRet == 2) isExist = true;
					errDataVector.addElement(stratInfo);
				} else {
					ProjectPlanCaller.addChangeAuto(  prjID,
									Constants.ACTION_ADD,
									Constants.PL_STRATEGY_FOR_MEETING_ADD,
									"Strategy for meeting",
									stratInfo.stratDesc,
									null);
				}
			}

			if (isExist) iRet = 2;

			if ( iRet == 0 && !isExist) {				
				Vector stratList = QualityObjective.getStrategyOfMeetingList(prjID);
				if (stratList == null) {
					stratList = new Vector();
				}
				session.setAttribute("StratOfMeetingList", stratList);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
			} else {
				request.setAttribute("ErrStratOfMeetingList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("plQO_StratForMeetingAdd.jsp",request,response);			
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doUpdateStratForMeeting(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			int size = 0;			
			Vector vNewStrat = new Vector();
			Vector vOldStrat = (Vector) session.getAttribute("StratOfMeetingList");
			Vector vErrStrat = new Vector();

			StrategyOfMeetingInfo newStratInfo = null;
			StrategyOfMeetingInfo oldStratInfo = null;
			
			String[] aStratID 		= request.getParameterValues("stratID");
			String[] aStratDesc 	= request.getParameterValues("strat_desc");
			String[] aStratExBene 	= request.getParameterValues("strat_ex_bene");

			if (aStratDesc != null) size = aStratDesc.length;
			for (int i = 0; i < size; i++) {
				newStratInfo = new StrategyOfMeetingInfo();
				oldStratInfo = (StrategyOfMeetingInfo) vOldStrat.elementAt(i);
				
				if (aStratDesc[i] == null) aStratDesc[i] = "";
				else aStratDesc[i] = ConvertString.toStandardizeString(aStratDesc[i]);
	
				if (aStratExBene[i] == null) aStratExBene[i] = "";
				else aStratExBene[i] = ConvertString.toStandardizeString(aStratExBene[i]);
				
				
				if (oldStratInfo.stratDesc == null) oldStratInfo.stratDesc = "";
				if (oldStratInfo.stratExBene == null) oldStratInfo.stratExBene = "";
				
				
				newStratInfo.stratDesc 		= 	aStratDesc[i];
				newStratInfo.stratExBene 	= 	aStratExBene[i];				
				newStratInfo.stratID = Long.parseLong(aStratID[i]);
	
				if ( aStratDesc[i].compareToIgnoreCase(oldStratInfo.stratDesc) != 0
					 || aStratExBene[i].compareToIgnoreCase(oldStratInfo.stratExBene) != 0						
				) 
				{
					ProjectPlanCaller.addChangeAuto(prjID,
								Constants.ACTION_UPDATE,
								Constants.PL_STRATEGY_FOR_MEETING_UPDATE,
								"Strategy for meeting",
								aStratDesc[i],
								oldStratInfo.stratDesc);
					vNewStrat.addElement(newStratInfo);
				}				
				vErrStrat.addElement(newStratInfo);				
			}

			if (QualityObjective.doUpdatePLStrategyForMeeting(vNewStrat,prjID)==0) {				
				Vector stratList = QualityObjective.getStrategyOfMeetingList(prjID);
				if (stratList == null) {
					stratList = new Vector();
				}
				session.setAttribute("StratOfMeetingList", stratList);
				Fms1Servlet.callPage("qualityObjective.jsp",request,response);
			} else {
				request.setAttribute("ErrStratOfMeetingList",vErrStrat);
				request.setAttribute("ErrType","1");
				Fms1Servlet.callPage("plQO_StratForMeetingUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doDeleteStratForMeeting(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector vErrStrat = new Vector();
			int size = 0;
			
			long stratID = Long.parseLong(request.getParameter("delStratID"));

			StrategyOfMeetingInfo newStratInfo = null;
			
			String[] aStratID 		= request.getParameterValues("stratID");
			String[] aStratDesc 	= request.getParameterValues("strat_desc");
			String[] aStratExBene 	= request.getParameterValues("strat_ex_bene");

			if (aStratDesc != null) size = aStratDesc.length;
			for (int i = 0; i < size; i++) {
				newStratInfo = new StrategyOfMeetingInfo();
				if (aStratDesc[i] == null) aStratDesc[i] = "";
				else aStratDesc[i] = ConvertString.toStandardizeString(aStratDesc[i]);
	
				if (aStratExBene[i] == null) aStratExBene[i] = "";
				else aStratExBene[i] = ConvertString.toStandardizeString(aStratExBene[i]);
				
				newStratInfo.stratDesc 		= 	aStratDesc[i];
				newStratInfo.stratExBene 	= 	aStratExBene[i];				
				newStratInfo.stratID = Long.parseLong(aStratID[i]);
				vErrStrat.addElement(newStratInfo);				
			}	
			
			if (QualityObjective.doDeletePLStrategyForMeeting(stratID)) {
				ProjectPlanCaller.addChangeAuto(	prjID,
								Constants.ACTION_DELETE,
								Constants.PL_STRATEGY_FOR_MEETING_DELETE,
								"Strategy for meeting",
								null,
								null);
				Vector stratList = QualityObjective.getStrategyOfMeetingList(prjID);
				if (stratList == null) {
					stratList = new Vector();
				}
				session.setAttribute("StratOfMeetingList", stratList);								
			} else {
				request.setAttribute("ErrStratOfMeetingList",vErrStrat);
				request.setAttribute("ErrType","1");				
			}
			Fms1Servlet.callPage("plQO_StratForMeetingUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Add Strategy for meeting quality objectives end
}
