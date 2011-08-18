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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fms1.infoclass.*;
import com.fms1.infoclass.group.DreByStageInfo;
import com.fms1.tools.*;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.Parameters;
import com.fms1.web.StringConstants;
import com.fms1.web.Constants;

import java.sql.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;

/**
 * Defect menu pages
 *
 */
public final class DefectCaller {
	public static final void defectViewCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final long projectID;
			long workUnitId = 0;
			String strProjectID = (String)request.getParameter("projectID");
			if (strProjectID != null) {
				projectID = Long.parseLong(strProjectID);
				workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
				WorkUnitCaller.setWorkUnitHome(request, workUnitId);
			}
			else {
				projectID = Long.parseLong(session.getAttribute("projectID").toString());
				workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
			}
						
			final DefectInfo defectInfo = Defect.getDefect(projectID); // 1. getDefect() is mofified by HaiMM: CR - Khong tinh cac defect co QC Activity = Baseline Audit, Quality Gate Inspection
			final Vector defectProductPlanVector = Defect.getDefectProductPlan(projectID); // 5. Mofified by HaiMM
			final Vector productDefDevTrackingVector=Defect.getproductDefectDevTracking(projectID,defectProductPlanVector); // 6. Mofified by HaiMM 
			final DefectByProcessInfo[] defectProcess = Defect.getWeigthedDefectByOrigin(projectID); // 7. Modified by HaiMM
            session.setAttribute("defectProcess", defectProcess);
			session.setAttribute("defectInfo", defectInfo);
			session.setAttribute("productDefDevTrackingVector", productDefDevTrackingVector);						
			
			ProjectInfo ProInfo = Project.getProjectInfo(projectID);
            Boolean isNewProject = new Boolean(ProInfo.getStartDate().after(Parameters.dayOfValidation) && (ProInfo.getApplyPPM()==1));
            session.setAttribute("isNewProject", isNewProject);                       

            final Vector stageVt = Schedule.getStageList(projectID);
            int [] stdStageCount=Schedule.getStdStageCount(stageVt);
            // Get norms appropriate with project end date
            final Vector qcNorms = Norms.getQcNorms(projectID);
            final Vector qcStageNorms = Norms.getQcStageNorms(projectID);
            ProjectSizeInfo projectInfo = new ProjectSizeInfo(projectID);
            DreByStageInfo dreByStage = new DreByStageInfo(stageVt.size());
            Defect.calcDreByStage(projectID, dreByStage, stageVt, stdStageCount, qcNorms, qcStageNorms);
            DreByStageInfo dreLeakage = Defect.calcDREDefectChart(projectID,
                    projectInfo, defectInfo, dreByStage, stageVt);
            
            session.setAttribute("dreByStage", dreByStage);
            session.setAttribute("dreLeakage", dreLeakage);
            session.setAttribute("stageVector", stageVt);
            
			final Vector moduleList = WorkProduct.getDefectModuleListSize(projectID);
			session.setAttribute("defectModuleList", moduleList);
			
			final Hashtable Process_WorkProduct =  WorkProduct.getProcessWorkProductList();
			session.setAttribute("Process_WP",Process_WorkProduct);
            
			Fms1Servlet.callPage("DefectView.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Add by HaiMM
	public static final void estDefectViewCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final long projectID;
			long workUnitId = 0;
			String strProjectID = (String)request.getParameter("projectID");
			if (strProjectID != null) {
				projectID = Long.parseLong(strProjectID);
				workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
				WorkUnitCaller.setWorkUnitHome(request, workUnitId);
			}
			else {
				projectID = Long.parseLong(session.getAttribute("projectID").toString());
				workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
			}
					
			final DefectByProcessInfo[] defectProcess = Defect.getEstimateDefect(projectID);
			session.setAttribute("defectProcess", defectProcess);

			double sumPlanValue = 0;
			for (int i = 0; i < defectProcess.length; i++){
				sumPlanValue += (i <= 2) ? defectProcess[i].planReview : defectProcess[i].planTest; 
			}
			session.setAttribute("sumPlanValue", Double.toString(sumPlanValue));

			Fms1Servlet.callPage("estDefectView.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doPrepareUpdateEstDef(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector estDefect=(Vector)session.getAttribute("estDefect");
			String listUpdate = (String) request.getParameter("listUpdate");						
			
			Vector vUpdate = new Vector();
			final StringTokenizer strSerUpdateIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			Vector vUpdateListId =new Vector();
			int i = 0;
			while (strSerUpdateIDList.hasMoreElements()) {
				vUpdateListId.addElement(strSerUpdateIDList.nextToken());				
			}

			int tSize = estDefect.size();
			for(i = 0; i < tSize; i++){				
				EstDefectInfo info = (EstDefectInfo) estDefect.elementAt(i);
				if ( vUpdateListId.contains(info.estDefectID+"")) {
					vUpdate.addElement(info);
				}
			}
			session.setAttribute("estDefUpdateList",vUpdate);
			Fms1Servlet.callPage("estDefUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void updateProductDefectTracking(final HttpServletRequest request, final HttpServletResponse response) {
			final HttpSession session = request.getSession();
			try {
				final long projectID = Long.parseLong((String) session.getAttribute("projectID"));
				Vector defectDevTrackingVector=(Vector) session.getAttribute("productDefDevTrackingVector");
				String strReasion[]=request.getParameterValues("reasion");
				String strAction[]=request.getParameterValues("action");
				if (!Defect.updateProductDefectDevTracking(projectID,defectDevTrackingVector,strReasion,strAction))
					Fms1Servlet.callPage("error.jsp?error=Update product defect tracking failed",request,response);									
				
				defectViewCaller(request, response);																							
			}
			catch (Exception e) {
				e.printStackTrace();
			}
	}	
	
	public static final void getDetailDefectPlan(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {			
			final long projectID = Long.parseLong((String) session.getAttribute("projectID"));			
			ProjectInfo ProInfo = Project.getProjectInfo(projectID);
												
			if (ProInfo.getStartDate().after(Parameters.dayOfValidation) && (ProInfo.getApplyPPM()==1)){//Apply for new projects				
				final Vector defectProductPlan = Defect.getDefectProductPlan(projectID);
				final Vector wpList = WorkProduct.getWPAndSizeList(projectID);
				final Vector moduleList = WorkProduct.getModuleList(projectID);	
				double normDefectRE[]={0,0,0,0,0,0,0,0,0};
				NormInfo Normtemp;
								
				Normtemp = Norms.getNorm(ProInfo.getParent(), ProInfo.getGrandParent(),Metrics.getMetricID(MetricDescInfo.DRE_QC_RREVIEW), ProInfo.getLifecycleId(), ProInfo.getStartDate());	
				normDefectRE[0] = CommonTools.parseInt(CommonTools.formatNumber(Normtemp.average,false));				
				Normtemp = Norms.getNorm(ProInfo.getParent(), ProInfo.getGrandParent(),Metrics.getMetricID(MetricDescInfo.DRE_QC_DREVIEW), ProInfo.getLifecycleId(), ProInfo.getStartDate());	
				normDefectRE[1] = CommonTools.parseInt(CommonTools.formatNumber(Normtemp.average,false));				
				Normtemp = Norms.getNorm(ProInfo.getParent(), ProInfo.getGrandParent(),Metrics.getMetricID(MetricDescInfo.DRE_QC_CREVIEW), ProInfo.getLifecycleId(), ProInfo.getStartDate());
				normDefectRE[2] = CommonTools.parseInt(CommonTools.formatNumber(Normtemp.average,false));			
				Normtemp = Norms.getNorm(ProInfo.getParent(), ProInfo.getGrandParent(),Metrics.getMetricID(MetricDescInfo.DRE_QC_UTEST), ProInfo.getLifecycleId(), ProInfo.getStartDate());
				normDefectRE[3] = CommonTools.parseInt(CommonTools.formatNumber(Normtemp.average,false));			
				Normtemp = Norms.getNorm(ProInfo.getParent(), ProInfo.getGrandParent(),Metrics.getMetricID(MetricDescInfo.DRE_QC_ITEST), ProInfo.getLifecycleId(), ProInfo.getStartDate());
				normDefectRE[4] = CommonTools.parseInt(CommonTools.formatNumber(Normtemp.average,false));				
				Normtemp = Norms.getNorm(ProInfo.getParent(), ProInfo.getGrandParent(),Metrics.getMetricID(MetricDescInfo.DRE_QC_STEST), ProInfo.getLifecycleId(), ProInfo.getStartDate());
				normDefectRE[5] = CommonTools.parseInt(CommonTools.formatNumber(Normtemp.average,false));			
				Normtemp = Norms.getNorm(ProInfo.getParent(), ProInfo.getGrandParent(),Metrics.getMetricID(MetricDescInfo.DRE_QC_OTHER), ProInfo.getLifecycleId(), ProInfo.getStartDate());
				normDefectRE[6] = CommonTools.parseInt(CommonTools.formatNumber(Normtemp.average,false));
				//Add Other Review and Other Test							
				Normtemp = Norms.getNorm(ProInfo.getParent(), ProInfo.getGrandParent(),Metrics.getMetricID(MetricDescInfo.DRE_QC_OREVIEW), ProInfo.getLifecycleId(), ProInfo.getStartDate());
				normDefectRE[7] = CommonTools.parseInt(CommonTools.formatNumber(Normtemp.average,false));	
				Normtemp = Norms.getNorm(ProInfo.getParent(), ProInfo.getGrandParent(),Metrics.getMetricID(MetricDescInfo.DRE_QC_OTEST), ProInfo.getLifecycleId(), ProInfo.getStartDate());
				normDefectRE[8] = CommonTools.parseInt(CommonTools.formatNumber(Normtemp.average,false));	
				//END ADD								
				session.setAttribute("defectProductPlan", defectProductPlan);				
				session.setAttribute("wpList", wpList);
				session.setAttribute("moduleList", moduleList);
				session.setAttribute("normDefectRE", normDefectRE);												
				Fms1Servlet.callPage("defectProductPlan.jsp",request,response);
			}else{//Apply for old projects		
				final Vector defectPlanDetail = Defect.getDefectsByProcess(projectID);
				final Vector wpList = WorkProduct.getWPAndSizeList(projectID);
				final Vector moduleList = WorkProduct.getModuleList(projectID);						
				final DefectInfo defectInfo = Defect.getDefect(projectID);
				
				session.setAttribute("defectPlanDetail", defectPlanDetail);			
				session.setAttribute("defectInfo", defectInfo);
				session.setAttribute("wpList", wpList);
				session.setAttribute("moduleList", moduleList);			
				Fms1Servlet.callPage("defectPlanDetail.jsp",request,response);
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void updateDetailDefectPlan(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		
		final HttpSession session = request.getSession();
		try {
			final long projectID = Long.parseLong((String) session.getAttribute("projectID"));			
			ProjectInfo ProInfo = Project.getProjectInfo(projectID);
						
			if (ProInfo.getStartDate().after(Parameters.dayOfValidation) && (ProInfo.getApplyPPM()==1)){//Apply for new projects
				DefectProductPlanInfo defectInfo = new DefectProductPlanInfo();		
				Vector wpList = (Vector) session.getAttribute("wpList");
				WorkProductInfo wpInfo =(WorkProductInfo) wpList.elementAt(Integer.parseInt(request.getParameter("cboWorkProduct")) - 1);										
				
				int moduleIndex = Integer.parseInt(request.getParameter("cboModule"));
				Vector moduleList = (Vector) session.getAttribute("moduleList");				
				WPSizeInfo moduleInfo = (WPSizeInfo) moduleList.elementAt(moduleIndex - 1);
				
				defectInfo.wpID= wpInfo.workProductID;
				defectInfo.moduleID  = moduleInfo.moduleID;						
				//RR","DR","CR","UT","IT","ST","OR","OT2","OT				
				defectInfo.requirementReview[0]=CommonTools.parseDouble(request.getParameter("RR"));
				defectInfo.designReview[0]=CommonTools.parseDouble(request.getParameter("DR"));
				defectInfo.codeReview[0]=CommonTools.parseDouble(request.getParameter("CR"));
				defectInfo.unitTest[0]=CommonTools.parseDouble(request.getParameter("UT"));
				defectInfo.integrationTest[0]=CommonTools.parseDouble(request.getParameter("IT"));
				defectInfo.systemTest[0]=CommonTools.parseDouble(request.getParameter("ST"));
				defectInfo.otherReview[0] = CommonTools.parseDouble(request.getParameter("OR"));
				defectInfo.otherTest[0] = CommonTools.parseDouble(request.getParameter("OT2"));
				defectInfo.others[0]=CommonTools.parseDouble(request.getParameter("OT"));				
				
				if (!Defect.updateDefectProductPlan_New(projectID, defectInfo))
					Fms1Servlet.callPage("error.jsp?error=Update product failed",request,response);
				getDetailDefectPlan(request, response);	
				
			}else{//Apply for old projects					
				DefectPlanInfo defectInfo = new DefectPlanInfo();			
				defectInfo.acceptanceTest = CommonTools.parseDouble(request.getParameter("AT"));
				defectInfo.codeReview = CommonTools.parseDouble(request.getParameter("CR"));
				defectInfo.documentReview = CommonTools.parseDouble(request.getParameter("DR"));
				defectInfo.integrationTest = CommonTools.parseDouble(request.getParameter("IT"));
				defectInfo.others = CommonTools.parseDouble(request.getParameter("OT"));
				defectInfo.plannedWDefect = CommonTools.parseDouble(request.getParameter("PD"));
				defectInfo.prototypeReview = CommonTools.parseDouble(request.getParameter("PR"));
				defectInfo.rePlannedWDefect = CommonTools.parseDouble(request.getParameter("RPD"));
				defectInfo.systemTest = CommonTools.parseDouble(request.getParameter("ST"));
				defectInfo.unitTest = CommonTools.parseDouble(request.getParameter("UT"));
				
				int moduleIndex = Integer.parseInt(request.getParameter("cboModule"));
				if (moduleIndex == 0) {
					defectInfo.moduleID=0;
					Vector wpList = (Vector) session.getAttribute("wpList");
					WorkProductInfo wpInfo =
						(WorkProductInfo) wpList.elementAt(Integer.parseInt(request.getParameter("cboWorkProduct")) - 1);
					defectInfo.wpID= wpInfo.workProductID;
					
				}
				else {
					Vector moduleList = (Vector) session.getAttribute("moduleList");
					WPSizeInfo moduleInfo = (WPSizeInfo) moduleList.elementAt(moduleIndex - 1);
					defectInfo.moduleID  = moduleInfo.moduleID;
				}
				
				if (!Defect.updateDefectPlanDetail(projectID, defectInfo))
						Fms1Servlet.callPage("error.jsp?error=Update work product failed",request,response);
				getDetailDefectPlan(request, response);				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static final void deleteDetailDefectPlan(		
		final HttpServletRequest request,
		final HttpServletResponse response) {		
		final HttpSession session = request.getSession();
		try {
			final long projectID = Long.parseLong(session.getAttribute("projectID").toString());
			ProjectInfo ProInfo = Project.getProjectInfo(projectID);
						
			if (ProInfo.getStartDate().after(Parameters.dayOfValidation) && (ProInfo.getApplyPPM()==1)){//Apply for new projects				
				double vectorID= CommonTools.parseDouble(request.getParameter("deleteID"));
				if (!Double.isNaN(vectorID)){
					Vector defectInfoVector = (Vector)session.getAttribute("defectProductPlan");
					if (vectorID>=0 ||vectorID <defectInfoVector.size()){
						DefectProductPlanInfo defectInfo=(DefectProductPlanInfo)defectInfoVector.elementAt((int)vectorID);
						if (Defect.deleteDefectProductPlan(projectID,defectInfo)){
							getDetailDefectPlan(request, response);
							return;
						}
					}
				}
			}else{ //Apply for old projects				
				double vectorID= CommonTools.parseDouble(request.getParameter("deleteID"));
				if (!Double.isNaN(vectorID)){
					Vector defectInfoVector = (Vector)session.getAttribute("defectPlanDetail");
					if (vectorID>=0 ||vectorID <defectInfoVector.size()){
						DefectPlanInfo defectInfo=(DefectPlanInfo)defectInfoVector.elementAt((int)vectorID);
						if (Defect.deleteDetailDefectPlan(projectID,defectInfo)){
							getDetailDefectPlan(request, response);
							return;
						}
					}
				}	
			}			
			Fms1Servlet.callPage("error.jsp?error=Bad parameters",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
		
	public static final void getDetailDefectReplan(
			final HttpServletRequest request,
			final HttpServletResponse response) {
			
			final HttpSession session = request.getSession();		
		try{				
			final long projectID = Long.parseLong((String) session.getAttribute("projectID"));			
			Vector defectInfoVector =Defect.getDefectProductPlan(projectID);											
			final Vector defectProductReplan = Defect.getDefectProductReplan(projectID,defectInfoVector);			
			session.setAttribute("defectProductReplan", defectProductReplan);														
			Fms1Servlet.callPage("defectProductReplan.jsp",request,response);		
		}
		catch (Exception e) {
			e.printStackTrace();					
		}
	}	
	
	public static final void updateDetailDefectReplan(		
	final HttpServletRequest request,
	final HttpServletResponse response) {
	
		final HttpSession session = request.getSession();		
		try {
			DefectProductReplanInfo defectInfo;
			int n=0;
			final long projectID = Long.parseLong((String) session.getAttribute("projectID"));								
			Vector defectProductReplanVector = (Vector)session.getAttribute("defectProductReplan");
			String replan_RR[]= request.getParameterValues("replan_RR");			
			String replan_DR[]= request.getParameterValues("replan_DR");
			String replan_CR[]= request.getParameterValues("replan_CR");
			String replan_UT[]= request.getParameterValues("replan_UT");
			String replan_IT[]= request.getParameterValues("replan_IT");
			String replan_ST[]= request.getParameterValues("replan_ST");
			// Add Other Review and Other Test
			String replan_OR[]= request.getParameterValues("replan_OR");
			String replan_OT2[]= request.getParameterValues("replan_OT2");
			// The end						
			String replan_OT[]= request.getParameterValues("replan_OT");	
											
			for(int i=0;i<defectProductReplanVector.size();i++){
				defectInfo=(DefectProductReplanInfo)defectProductReplanVector.elementAt(i);
				if (!defectInfo.released){
					defectInfo.requirementReview[1]=CommonTools.parseDouble(replan_RR[n]);
					defectInfo.designReview[1]=CommonTools.parseDouble(replan_DR[n]);
					defectInfo.codeReview[1]=CommonTools.parseDouble(replan_CR[n]);
					defectInfo.unitTest[1]=CommonTools.parseDouble(replan_UT[n]);
					defectInfo.integrationTest[1]=CommonTools.parseDouble(replan_IT[n]);
					defectInfo.systemTest[1]=CommonTools.parseDouble(replan_ST[n]);
					defectInfo.otherReview[1] = CommonTools.parseDouble(replan_OR[n]);
					defectInfo.otherTest[1] = CommonTools.parseDouble(replan_OT2[n]);
					defectInfo.others[1]=CommonTools.parseDouble(replan_OT[n]);	
									
					n++;
				}
			}								
			
			if (!Defect.updateDefectProductReplan(projectID,defectProductReplanVector))
				Fms1Servlet.callPage("error.jsp?error=Update product defect replan failed",request,response);
			
			getDetailDefectPlan(request, response);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}				
	}
	
	public static final void getDetailDefectRate(
		final HttpServletRequest request,
		final HttpServletResponse response) {			
		final HttpSession session = request.getSession();		
		try{				
			final long projectID = Long.parseLong((String) session.getAttribute("projectID"));			
			Vector defectInfoVector =Defect.getDefectProductPlan(projectID);											
			final Vector defectRateReplan = Defect.getDefectRateReplan(projectID,defectInfoVector);			
			session.setAttribute("defectRateReplan", defectRateReplan);														
			Fms1Servlet.callPage("defectRate.jsp",request,response);		
		}catch (Exception e) {
			e.printStackTrace();					
		}
	}


	public static final void updateDetailDefectRateReplan(		
	final HttpServletRequest request,
	final HttpServletResponse response) {	
		final HttpSession session = request.getSession();		
		try {
			DefectRateInfo defectRateInfo;
			int n=0;			
			final long projectID = Long.parseLong((String) session.getAttribute("projectID"));								
			Vector defectRateReplanVector = (Vector)session.getAttribute("defectRateReplan");
			String replanValue[]= request.getParameterValues("Replan");			
			String noteValue[]= request.getParameterValues("Note");
												
			for(int i=0;i<defectRateReplanVector.size();i++){
				defectRateInfo=(DefectRateInfo)defectRateReplanVector.elementAt(i);
				if (!defectRateInfo.released){
					defectRateInfo.replan=CommonTools.parseDouble(replanValue[n]);
					defectRateInfo.note=noteValue[n];
					n++;	
				}
			}	
							
			if (!Defect.updateDefectRateReplan(projectID,defectRateReplanVector))
				Fms1Servlet.callPage("error.jsp?error=Update defect rate replan failed",request,response);
			
				getDetailDefectPlan(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}				
	}

	public static final void defectUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final long projectID = Long.parseLong(session.getAttribute("projectID").toString());
			UpdateDefectPlanInfo defectInfo = new UpdateDefectPlanInfo();
			defectInfo.requirementPlanReview = CommonTools.parseDouble(request.getParameter("RequirementPlanReview"));
			defectInfo.requirementRePlanReview =
				CommonTools.parseDouble(request.getParameter("RequirementRePlanReview"));
			defectInfo.requirementPlanTest= CommonTools.parseDouble(request.getParameter("RequirementPlanTest"));
			defectInfo.requirementRePlanTest =
				CommonTools.parseDouble(request.getParameter("RequirementRePlanTest"));	
				
			defectInfo.designPlanReview = CommonTools.parseDouble(request.getParameter("DesignPlanReview"));
			defectInfo.designRePlanReview = CommonTools.parseDouble(request.getParameter("DesignRePlanReview"));
			defectInfo.designPlanTest = CommonTools.parseDouble(request.getParameter("DesignPlanTest"));
			defectInfo.designRePlanTest = CommonTools.parseDouble(request.getParameter("DesignRePlanTest"));
			
			
			defectInfo.codingPlanReview = CommonTools.parseDouble(request.getParameter("CodingPlanReview"));
			defectInfo.codingRePlanReview = CommonTools.parseDouble(request.getParameter("CodingRePlanReview"));
			defectInfo.codingPlanTest = CommonTools.parseDouble(request.getParameter("CodingPlanTest"));
			defectInfo.codingRePlanTest = CommonTools.parseDouble(request.getParameter("CodingRePlanTest"));
			defectInfo.otherPlanReview = CommonTools.parseDouble(request.getParameter("OtherPlanReview"));
			defectInfo.otherRePlanReview = CommonTools.parseDouble(request.getParameter("OtherRePlanReview"));
			defectInfo.otherPlanTest = CommonTools.parseDouble(request.getParameter("OtherPlanTest"));
			defectInfo.otherRePlanTest = CommonTools.parseDouble(request.getParameter("OtherRePlanTest"));
			
			
			
			Defect.updateDefectPlan(projectID, defectInfo);
			final DefectInfo defectsInfo = Defect.getDefect(projectID);
			final DefectByProcessInfo[] defectProcess = Defect.getWeigthedDefectByOrigin(projectID);
			session.setAttribute("defectProcess", defectProcess);
			session.setAttribute("defectInfo", defectsInfo);
			Fms1Servlet.callPage("DefectView.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Add by HaiMM
	public static final void estDefectUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final long projectID = Long.parseLong(session.getAttribute("projectID").toString());
			UpdateDefectPlanInfo defectInfo = new UpdateDefectPlanInfo();

			String[] planValue = request.getParameterValues("planValue");
			String[] note = request.getParameterValues("note");
			
			defectInfo.requirementPlanReview = CommonTools.parseDouble(planValue[0]);
			defectInfo.requirementNoteReview = note[0];

			defectInfo.designPlanReview = CommonTools.parseDouble(planValue[1]);
			defectInfo.designNoteReview = note[1];
			
			defectInfo.codingPlanReview = CommonTools.parseDouble(planValue[2]);
			defectInfo.codingNoteReview = note[2];
			
			defectInfo.requirementPlanTest= CommonTools.parseDouble(planValue[3]);
			defectInfo.requirementNoteTest = note[3];
			
			defectInfo.designPlanTest = CommonTools.parseDouble(planValue[4]);
			defectInfo.designNoteTest = note[4];
			
			defectInfo.codingPlanTest = CommonTools.parseDouble(planValue[5]);
			defectInfo.codingNoteTest= note[5];
			
			Defect.updateDefectPlan(projectID, defectInfo);
			final DefectByProcessInfo[] defectProcess = Defect.getEstimateDefect(projectID);
			session.setAttribute("defectProcess", defectProcess);
			
			double sumPlanValue = 0;
			sumPlanValue = defectInfo.requirementPlanReview + defectInfo.designPlanReview + defectInfo.codingPlanReview
							+ defectInfo.requirementPlanTest + defectInfo.designPlanTest + defectInfo.codingPlanTest;
			session.setAttribute("sumPlanValue", Double.toString(sumPlanValue));
			// Forward to qualityObjective.jsp
			Fms1Servlet.callPage("qualityObjective.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void estDefectAddCaller(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			EstDefectInfo defInfo = null;
	
			String[] reviewTest = request.getParameterValues("reviewTest");
			String[] basic = request.getParameterValues("basic");
			String[] target = request.getParameterValues("target");
			
	
			int size = reviewTest.length;
			for (int i = 0; i < size; i++) {
				if (reviewTest[i] == null || "".equals(reviewTest[i].trim())) continue;
				defInfo = new EstDefectInfo();
				
				defInfo.reviewTest = reviewTest[i].trim();
				if (basic[i] == null) basic[i] = "";
				defInfo.basicEst = basic[i].trim();
				defInfo.target = CommonTools.parseDouble(target[i]);
				
				Defect.addEstDef(prjID, defInfo);
				
			}
	
			QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void estDefUpdateCaller(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			EstDefectInfo defInfo = null;
			
			String[] estDefID	= request.getParameterValues("estDefID");
			String[] reviewTest = request.getParameterValues("reviewTest");
			String[] basic = request.getParameterValues("basic");
			String[] target = request.getParameterValues("target");
		

			int size = reviewTest.length;
			for (int i = 0; i < size; i++) {
				defInfo = new EstDefectInfo();
				
				defInfo.estDefectID = CommonTools.parseInt(estDefID[i]);
				defInfo.reviewTest = reviewTest[i].trim();
				if (basic[i] == null) basic[i] = "";
				defInfo.basicEst = basic[i].trim();
				defInfo.target = CommonTools.parseDouble(target[i]);
			
				Defect.updateEstDef(prjID, defInfo);
			
			}

			QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doDeleteComReport(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int comID = 0;
			int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			String listUpdate = (String) request.getParameter("listUpdate");

			final StringTokenizer strComDeleteIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			while (strComDeleteIDList.hasMoreElements()) {
				comID = Integer.parseInt(strComDeleteIDList.nextToken());
				
				Defect.doDeleteEstDef(comID);
			}

			QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		
	public static final void defectProgressCaller(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			long projectID = Long.parseLong(session.getAttribute("projectID").toString());
			ProjectInfo proj = Project.getProjectInfo(projectID);
			Date endDate=(proj.getActualFinishDate()==null)?new Date(new java.util.Date().getTime()):proj.getActualFinishDate();
			int [][] defects =Defect.getProgress(projectID,proj.getStartDate(),endDate);
			session.setAttribute("defectProgress", defects);
			session.setAttribute("projStartDate", proj.getStartDate());
			Fms1Servlet.callPage("defectProgress.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void defectDPLogListing(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			long projectID = 0;
			
			if (workUnitID == Parameters.SQA_WU){
				 Vector vtCommDefect = Defect.getCommDefDatabase(Parameters.FSOFT_WU);
				if(vtCommDefect==null)
				{
					vtCommDefect= new Vector();
				}
				session.setAttribute("vtCommDefect", vtCommDefect);
			}
			else{
				projectID = Long.parseLong(session.getAttribute("projectID").toString());
				
				ProjectInfo proj = Project.getProjectInfo(projectID);
				session.setAttribute("projStartDate", proj.getStartDate());
				session.setAttribute("projCode", proj.getProjectCode());
				
				session.setAttribute("groupName", proj.getGroupName());
				
				final Vector vtCommDefect = Defect.getCommDef(projectID);
				session.setAttribute("vtCommDefect", vtCommDefect);
			}
			
			Vector vtDefectLog = Defect.getDPLog(workUnitID);
			if(vtDefectLog==null)
			{
				vtDefectLog= new Vector();
			}
			session.setAttribute("vtDefectLog", vtDefectLog);
			
			Fms1Servlet.callPage("defectDPLog.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doUpdateDPLog(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			long lProjectID = Long.parseLong((String)session.getAttribute("projectID"));
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			
			String prjojectcode = null;
			if (workUnitID == Parameters.SQA_WU)
				prjojectcode = Parameters.SQA_ROLE;
			else
				prjojectcode = request.getParameter("prjojectcode");
			
			final String dplogid = request.getParameter("dplogid");
			final String dpaction = request.getParameter("dpaction");
			final String processid = request.getParameter("processid");
			final String commdefcode = request.getParameter("commdefcode");
			final String createdate = request.getParameter("createdate");
			final String targetdate = request.getParameter("targetdate");
			final String closeddate = request.getParameter("closeddate");
			final String dpstatus = request.getParameter("dpstatus");
			final String dpbenefit = request.getParameter("dpbenefit");
			final String dpnote = request.getParameter("dpnote");
			final String devid = request.getParameter("strAccountName");
			
			final DPLogInfo info = new DPLogInfo();
			
			info.workunitID = workUnitID;
			info.dplogID = Long.parseLong(dplogid);
			info.devAccount = devid;
			
			if (dpaction != null)
				info.dpaction = dpaction;
			
			info.dpcode = prjojectcode + "/DP" + String.valueOf(dplogid);
			info.processID = Integer.parseInt(processid);
			
			if (commdefcode != null)
				info.commonDefCode = commdefcode;

			if (createdate != null)
				info.createDate = CommonTools.parseSQLDate(createdate);
				
			if (targetdate != null)
				info.targetDate = CommonTools.parseSQLDate(targetdate);

			if (closeddate != null)
				info.closedDate = CommonTools.parseSQLDate(closeddate);
			
			info.dpStatus = Integer.parseInt(dpstatus);
			
			if (dpbenefit != null)
				info.dpBenefit = dpbenefit;
			
			if (dpnote != null)
				info.dpNote = dpnote;
			UserInfo userInfo = null;
			AssignmentInfo assInfo = null;
			if (workUnitID == Parameters.SQA_WU){
				userInfo =  UserProfileCaller.checkUserFilter(request, devid, null);
				if (userInfo != null){
					info.devID = userInfo.developerID;
					info.devAccount = userInfo.account;
				}
			}
			else{
				assInfo = UserProfileCaller.checkUserAssignment(request, devid, lProjectID);
				if (assInfo != null){
					info.devID = assInfo.devID;
					info.devAccount = assInfo.account;
				}
			}
			//int i=0;
			if ((userInfo != null || assInfo != null) && Defect.updateDPLog(info)){
				defectDPLogListing(request, response);
			}
			else{
				//request.setAttribute("DefectInfoUpdate", info);
				request.setAttribute("error","Error");
				Fms1Servlet.callPage("error.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doDeleteDPLog(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String dplogid = request.getParameter("dplogid");
			final String dpcode = request.getParameter("dpcode");

			Defect.deleteDPLog(Long.parseLong(dplogid), dpcode);

			defectDPLogListing(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doAddnewDPLog(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			long lProjectID = Long.parseLong((String)session.getAttribute("projectID"));
			String prjojectcode = null;
			if (workUnitID == Parameters.SQA_WU)
				prjojectcode = Parameters.SQA_ROLE;
			else
				prjojectcode = request.getParameter("prjojectcode");
			
			final String dpaction = request.getParameter("dpaction");
			final String processid = request.getParameter("processid");
			final String commdefcode = request.getParameter("commdefcode");
			final String createdate = request.getParameter("createdate");
			final String targetdate = request.getParameter("targetdate");
			final String closeddate = request.getParameter("closeddate");
			final String dpstatus = request.getParameter("dpstatus");
			final String dpbenefit = request.getParameter("dpbenefit");
			final String dpnote = request.getParameter("dpnote");
			final String strDevAccountName = request.getParameter("strAccountName");

			final DPLogInfo info = new DPLogInfo();

			info.workunitID = workUnitID;

			if (dpaction != null)
				info.dpaction = dpaction;

			info.dpcode = prjojectcode + "/DP";
			info.processID = Integer.parseInt(processid);
			info.devAccount = strDevAccountName;

			if (commdefcode != null)
				info.commonDefCode = commdefcode;

			if (createdate != null)
				info.createDate = CommonTools.parseSQLDate(createdate);

			if (targetdate != null)
				info.targetDate = CommonTools.parseSQLDate(targetdate);

			if (closeddate != null)
				info.closedDate = CommonTools.parseSQLDate(closeddate);

			info.dpStatus = CommonTools.parseInt(dpstatus);

			if (dpbenefit != null)
				info.dpBenefit = dpbenefit;

			if (dpnote != null)
				info.dpNote = dpnote;
			UserInfo userInfo = null;
			AssignmentInfo assInfo = null;
			if (workUnitID == Parameters.SQA_WU){
				userInfo =  UserProfileCaller.checkUserFilter(request, strDevAccountName, null);
				if (userInfo != null){
					info.devID = userInfo.developerID;
					info.devAccount = userInfo.account;
				}
			}
			else{
				assInfo = UserProfileCaller.checkUserAssignment(request, strDevAccountName, lProjectID);
				if (assInfo != null){
					info.devID = assInfo.devID;
					info.devAccount = assInfo.account;
				}
			}

			if ((userInfo != null || assInfo != null) && Defect.addDPLog(info)){
				defectDPLogListing(request, response);
			}
			else{
				request.setAttribute("DefectInfoAddNew", info);
				Fms1Servlet.callPage("dpLogAdd.jsp", request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void dpDrill( HttpServletRequest request,  HttpServletResponse response) {
		final HttpSession session = request.getSession();
		long currentWU = Long.parseLong((String)session.getAttribute("workUnitID"));
		
		int vtID = Integer.parseInt(request.getParameter("vtID"));
		
		Vector dpVt = (Vector) session.getAttribute("vtDefectLog");
		DPLogInfo info = (DPLogInfo) dpVt.elementAt(vtID);
		
		if (currentWU!=info.workunitID)
		{
			WorkUnitCaller.setWorkUnitHome(request, info.workunitID);
			Fms1Servlet.callPage(info.linkTo, request, response);
		}
		else
		{
			defectDPLogListing(request, response);
		}
	}
	public static final void commDefListing(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			long projectID = Long.parseLong(session.getAttribute("projectID").toString());
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));

			ProjectInfo proj = Project.getProjectInfo(projectID);
			session.setAttribute("projStartDate", proj.getStartDate());
			session.setAttribute("projCode", proj.getProjectCode());
			session.setAttribute("groupName", proj.getGroupName());

			final Vector vtCommDefect = Defect.getCommDef(projectID);
			session.setAttribute("vtCommDefect", vtCommDefect);

			final Vector vtDefectLog = Defect.getDPLog(workUnitID);
			session.setAttribute("vtDefectLog", vtDefectLog);

			final Vector dtList = Defect.getDefectType();
			session.setAttribute("defectType", dtList);

			Fms1Servlet.callPage("defectComm.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doUpdateCommDef(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			
			final String prjojectid = (String) session.getAttribute("projectID");
			final String commdefid = request.getParameter("commdefid");
			final String defdesc = request.getParameter("defdesc");
			final String dpcode = request.getParameter("dpcode");
			final String deftype = request.getParameter("deftype");
			final String rootcause = request.getParameter("rootcause");
			final String causecate = request.getParameter("causecate");
			
			final CommDefInfo info = new CommDefInfo();
			
			info.prjID = Long.parseLong(prjojectid);
			info.commdefID = Long.parseLong(commdefid);
			
			if (defdesc != null)
				info.commdef = defdesc;
			
			if (dpcode != null)
				info.dpcode = dpcode;
			
			info.defecttype = Integer.parseInt(deftype);
			
			if (rootcause != null)
				info.rootcause = rootcause;
			
			if (causecate != null)
				info.causecate = Integer.parseInt(causecate);
			
			Defect.updateCommDef(info);
			
			commDefListing(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doDeleteCommDef(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String commdefid = request.getParameter("commdefid");
			final String commdefcode = request.getParameter("commdefcode");

			Defect.deleteCommDef(Long.parseLong(commdefid), commdefcode);

			commDefListing(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public final static void doAddnewCommDef(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			
			final String prjojectid = (String) session.getAttribute("projectID");
			final String prjojectcode = request.getParameter("prjojectcode");
			final String defdesc = request.getParameter("defdesc");
			final String dpcode = request.getParameter("dpcode");
			final String deftype = request.getParameter("deftype");
			final String rootcause = request.getParameter("rootcause");
			final String causecate = request.getParameter("causecate");
			
			final CommDefInfo info = new CommDefInfo();
			
			info.prjID = Long.parseLong(prjojectid);
			info.commonDefCode = prjojectcode + "/CD";
			
			if (defdesc != null)
				info.commdef = defdesc;
			
			if (dpcode != null)
				info.dpcode = dpcode;
			
			info.defecttype = Integer.parseInt(deftype);
			
			if (rootcause != null)
				info.rootcause = rootcause;
			
			if (causecate != null)
				info.causecate = Integer.parseInt(causecate);
			
			Defect.addCommDef(info);
			
			commDefListing(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void defectDPDatabase(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		try {
			final String strWuID = (String)request.getParameter("cboGroup");
			final String strStatus = request.getParameter("cboDPStatus");
			final String strOnTime = request.getParameter("cboDPOnTime");
			final String strProjectId = request.getParameter("cboProject");
			
			long lWuID = Parameters.FSOFT_WU; // FSOFT by default
			if (strWuID != null)
				lWuID = Long.parseLong(strWuID);
			
			String strFromDate = request.getParameter("fromDate");
			String strToDate = request.getParameter("toDate");
			
			Date  fromDate;
			Date  toDate;
			
			ReportMonth rm = new ReportMonth();
			if (strFromDate == null)
				strFromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2,4);
			if (strFromDate.equalsIgnoreCase(""))
				strFromDate = "01-Jan-" + String.valueOf(rm.getYear()).substring(2,4);
			fromDate = CommonTools.parseSQLDate(strFromDate);
			
			if (strToDate == null)
				strToDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2,4);
			if (strToDate.equalsIgnoreCase(""))
				strToDate = "01-" + CommonTools.getMonth(rm.getMonth() + 1) + "-" + String.valueOf(rm.getYear()).substring(2,4);
			toDate = CommonTools.parseSQLDate(strToDate);
			
			String strWhere = "";
			if (strStatus != null) {
				if (!strStatus.equalsIgnoreCase("-1")) {
					strWhere = strWhere + " AND c.DPSTATUS = " + strStatus + " ";
				}
			}
			
			long lprojectId = Parameters.FSOFT_WU;
			if (strProjectId != null) {
				if (!strProjectId.equalsIgnoreCase("-1")) {
					lprojectId = Long.parseLong(strProjectId);
					final Vector vtDefectLog = Defect.getDPLog(lprojectId);
					session.setAttribute("vtDefectLog", vtDefectLog);
				} else {
					final Vector vtDefectLog = Defect.getDPLogDatabase(lWuID, fromDate, toDate, strWhere);
					session.setAttribute("vtDefectLog", vtDefectLog);
				}
			} else{
				final Vector vtDefectLog = Defect.getDPLogDatabase(lWuID, fromDate, toDate, strWhere);
				session.setAttribute("vtDefectLog", vtDefectLog);
			}
			
			Vector users = UserHelper.getAllUsers();
			session.setAttribute("userList", users);
			
			final Vector dtList = Defect.getDefectType();
			session.setAttribute("defectType", dtList);
			
			final Vector vtCommDefect = Defect.getCommDefDatabase(lWuID);
			session.setAttribute("vtCommDefect", vtCommDefect);
			
			session.setAttribute("strStatus", strStatus);
			session.setAttribute("strOnTime", strOnTime);
			session.setAttribute("strProjectId", strProjectId);
			
			session.setAttribute("wuID", String.valueOf(lWuID));
			session.setAttribute("fromDate", strFromDate);
			session.setAttribute("toDate", strToDate);
			
			Fms1Servlet.callPage("dpDatabase.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static final void getPlanDreByQcStage(final HttpServletRequest request, final HttpServletResponse response, String error) {
        try {
            final HttpSession session = request.getSession();
            final long projectID;
            long workUnitId = 0;
            String strProjectID = (String)request.getParameter("projectID");
            if (strProjectID != null) {
                projectID = Long.parseLong(strProjectID);
                workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
                WorkUnitCaller.setWorkUnitHome(request, workUnitId);
            }
            else {
                projectID = Long.parseLong(session.getAttribute("projectID").toString());
                workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
            }
			ProjectInfo ProInfo = Project.getProjectInfo(projectID);
            Boolean isNewProject = new Boolean(ProInfo.getStartDate().after(Parameters.dayOfValidation) && (ProInfo.getApplyPPM()==1));
            session.setAttribute("isNewProject", isNewProject);

            final Vector stageVt = Schedule.getStageList(projectID);
            int [] stdStageCount=Schedule.getStdStageCount(stageVt);
            // Get norms appropriate with project end date
            final Vector qcNorms = Norms.getQcNorms(projectID);
            final Vector qcStageNorms = Norms.getQcStageNorms(projectID);
            DreByStageInfo dreByStage = new DreByStageInfo(stageVt.size());
            Defect.calcDreByStage(projectID, dreByStage, stageVt, stdStageCount, qcNorms, qcStageNorms);
            session.setAttribute("stageVector", stageVt);
            session.setAttribute("dreByStage", dreByStage);

            Fms1Servlet.callPage("planDREByQcStage.jsp?error=" + error,request,response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void updatePlanDreByQcStage(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HttpSession session = request.getSession();
            DreByStageInfo dreByStage = (DreByStageInfo)session.getAttribute("dreByStage");
            String strMsg = Defect.updatePlannedQcStage(dreByStage, request);
            // ************************************************************************************************
            // Then update PROJECT_PLAN values for Test/Review activities...
            // ************************************************************************************************
            updateForProjectPlan(request, response, "");

            getPlanDreByQcStage(request, response, strMsg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void getPlanDrePlanDefect(final HttpServletRequest request, final HttpServletResponse response, String error) {
        try {
            String strIsReplan = (request.getParameter("isReplan") != null) ?
                    "&isReplan=" + request.getParameter("isReplan") : "";
            getPlanReplanDefect(request, response, error);
            Fms1Servlet.callPage("planDREPlanReplanDefect.jsp?" + strIsReplan +
                    "&error=" + error,request,response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final void getPlanReplanDefect(final HttpServletRequest request, final HttpServletResponse response, String error) {
        try {
            final HttpSession session = request.getSession();
            final long projectID;
            long workUnitId = 0;
            String strProjectID = (String)request.getParameter("projectID");
            if (strProjectID != null) {
                projectID = Long.parseLong(strProjectID);
                workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
                WorkUnitCaller.setWorkUnitHome(request, workUnitId);
            }
            else {
                projectID = Long.parseLong(session.getAttribute("projectID").toString());
                workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
            }
            ProjectSizeInfo projectInfo = new ProjectSizeInfo(projectID);
            DefectInfo defectInfo = Defect.getDefect(projectID);
            // Get last session parameters from getPlanDreByQcStage() 
            DreByStageInfo dreByStage = (DreByStageInfo) session.getAttribute("dreByStage");
            final Vector stageVt = (Vector) session.getAttribute("stageVector");
            Defect.calcDREPlanReplan(projectID, projectInfo, defectInfo, dreByStage, stageVt);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void updateDreReplan(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HttpSession session = request.getSession();
            DreByStageInfo dreByStage = (DreByStageInfo)session.getAttribute("dreByStage");
            String strMsg = Defect.updateDreReplan(dreByStage, request);
            // ************************************************************************************************
            // Then update PROJECT_PLAN values for Test/Review activities...
            // ************************************************************************************************
            updateForProjectReplan(request, response, "");
            
            getPlanDrePlanDefect(request, response, strMsg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Update PROJECT_PLAN table 
    private static final void updateForProjectPlan(final HttpServletRequest request, final HttpServletResponse response, String error) {
        try {
            final HttpSession session = request.getSession();
            final long projectID;
            String strProjectID = (String)request.getParameter("projectID");
            if (strProjectID != null) {
                projectID = Long.parseLong(strProjectID);
            }
            else {
                projectID = Long.parseLong(session.getAttribute("projectID").toString());
            }
            getPlanReplanDefect(request, response, error);
            // Get last session parameters from getPlanDreByQcStage() 
            DreByStageInfo dreByStage = (DreByStageInfo) session.getAttribute("dreByStage");
            Defect.updateForProjectPlan(dreByStage, projectID);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Update PROJECT_PLAN table 
    private static final void updateForProjectReplan(final HttpServletRequest request, final HttpServletResponse response, String error) {
        try {
            final HttpSession session = request.getSession();
            final long projectID;
            String strProjectID = (String)request.getParameter("projectID");
            if (strProjectID != null) {
                projectID = Long.parseLong(strProjectID);
            }
            else {
                projectID = Long.parseLong(session.getAttribute("projectID").toString());
            }
            getPlanReplanDefect(request, response, error);
            // Get last session parameters from getPlanDreByQcStage() 
            DreByStageInfo dreByStage = (DreByStageInfo) session.getAttribute("dreByStage");
            Defect.updateForProjectReplan(dreByStage, projectID);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void getPlanDreDefectRate(final HttpServletRequest request, final HttpServletResponse response, String error) {
        try {
            final HttpSession session = request.getSession();
            final long projectID;
            long workUnitId = 0;
            String strProjectID = (String)request.getParameter("projectID");
            if (strProjectID != null) {
                projectID = Long.parseLong(strProjectID);
                workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
                WorkUnitCaller.setWorkUnitHome(request, workUnitId);
}
            else {
                projectID = Long.parseLong(session.getAttribute("projectID").toString());
                workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
            }
            ProjectSizeInfo projectInfo = new ProjectSizeInfo(projectID);
            DefectInfo defectInfo = Defect.getDefect(projectID);
            // Get last session parameters from getPlanDreByQcStage() 
            DreByStageInfo dreByStage = (DreByStageInfo) session.getAttribute("dreByStage");
            final Vector stageVt = (Vector) session.getAttribute("stageVector");
            DreByStageInfo dreDefectRate = Defect.calcDREDefectRate(projectID,
                    projectInfo, defectInfo, dreByStage, stageVt);
            session.setAttribute("normDefectRate", new Double(defectInfo.normDefectRate));
            session.setAttribute("dreDefectRate", dreDefectRate);
            Fms1Servlet.callPage("planDREDefectRate.jsp?" + "&error=" + error,request,response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void updateDreDefectRate(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HttpSession session = request.getSession();
            DreByStageInfo dreByStage = (DreByStageInfo)session.getAttribute("dreByStage");
            String strMsg = Defect.updateDreDefectRate(dreByStage, request);
            getPlanDreDefectRate(request, response, strMsg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void getPlanDreLeakage(final HttpServletRequest request, final HttpServletResponse response, String error) {
        try {
            final HttpSession session = request.getSession();
            final long projectID;
            long workUnitId = 0;
            String strProjectID = (String)request.getParameter("projectID");
            if (strProjectID != null) {
                projectID = Long.parseLong(strProjectID);
                workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
                WorkUnitCaller.setWorkUnitHome(request, workUnitId);
            }
            else {
                projectID = Long.parseLong(session.getAttribute("projectID").toString());
                workUnitId = WorkUnit.getWorkUnitByProjectId(projectID);
            }
            ProjectSizeInfo projectInfo = new ProjectSizeInfo(projectID);
            DefectInfo defectInfo = Defect.getDefect(projectID);
            // Get last session parameters from getPlanDreByQcStage() 
            DreByStageInfo dreByStage = (DreByStageInfo) session.getAttribute("dreByStage");
            final Vector stageVt = (Vector) session.getAttribute("stageVector");
            DreByStageInfo dreLeakage = Defect.calcDRELeakage(projectID,
                    projectInfo, defectInfo, dreByStage, stageVt);
            
            session.setAttribute("dreLeakage", dreLeakage);
            Fms1Servlet.callPage("planDRELeakage.jsp?" + "&error=" + error,request,response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	public static final void doPrepareAddDefectRevProduct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("defectRevProductAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doPrepareUpdateDefectRevProduct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector defectModuleList = (Vector) session.getAttribute("defectModuleList");
			String listUpdate = (String) request.getParameter("listUpdate");

			Vector vUpdate = new Vector();
			final StringTokenizer strAssUpdateIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			Vector vUpdateListId =new Vector();
			int i = 0;
			while (strAssUpdateIDList.hasMoreElements()) {
				vUpdateListId.addElement(strAssUpdateIDList.nextToken());
			}

			int dSize = defectModuleList.size();
			for(i = 0; i < dSize; i++){
				WPSizeInfo info = (WPSizeInfo) defectModuleList.elementAt(i);
				if ( vUpdateListId.contains(info.moduleID+"")) {
					vUpdate.addElement(info);
				}
			}
			session.setAttribute("DefectModuleBatchUpdateList",vUpdate);
			
			Fms1Servlet.callPage("defectRevProductUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doDeleteDefectRevProduct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();			
			String listUpdate = (String) request.getParameter("listUpdate");
			String fromPage = (String) request.getParameter("fromPage");			
			Defect.doDeleteReviewDefectProduct(listUpdate);
			if ("defect".equals(fromPage))	defectViewCaller(request,response);
			else if ("quality".equals(fromPage)) QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doDeleteDefectTestProduct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();			
			String listUpdate = (String) request.getParameter("listUpdate");
			String fromPage = (String) request.getParameter("fromPage");			
			Defect.doDeleteTestDefectProduct(listUpdate);
			if ("defect".equals(fromPage))	defectViewCaller(request,response);
			else if ("quality".equals(fromPage)) QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doPrepareAddDefectTestProduct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Fms1Servlet.callPage("defectTestProductAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doPrepareUpdateDefectTestProduct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector defectModuleList = (Vector) session.getAttribute("defectModuleList");
			String listUpdate = (String) request.getParameter("listUpdate");

			Vector vUpdate = new Vector();
			final StringTokenizer strAssUpdateIDList = new StringTokenizer (listUpdate==null? "":listUpdate,",");
			Vector vUpdateListId =new Vector();
			int i = 0;
			while (strAssUpdateIDList.hasMoreElements()) {
				vUpdateListId.addElement(strAssUpdateIDList.nextToken());
			}

			int dSize = defectModuleList.size();
			for(i = 0; i < dSize; i++){
				WPSizeInfo info = (WPSizeInfo) defectModuleList.elementAt(i);
				if ( vUpdateListId.contains(info.moduleID+"")) {
					vUpdate.addElement(info);
				}
			}
			session.setAttribute("DefectModuleBatchUpdateList",vUpdate);
			
			Fms1Servlet.callPage("defectTestProductUpdate.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doAddDefectRevProduct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector errDataVector = new Vector();
			WPSizeInfo mInfo = null;
			int iRet = 0;
			 
			String[] product = request.getParameterValues("product");
			String[] planSize = request.getParameterValues("planSize");
			String fromPage = (String) request.getParameter("fromPage");

			int size = product.length;
			for (int i = 0; i < size; i++) {
				if (product[i] == null || "0".equals(product[i].trim())) continue;
				mInfo = new WPSizeInfo();				
				
				mInfo.moduleID = Integer.parseInt(product[i].trim());

				if ("".equals(planSize[i].trim()))mInfo.newPlanSizeReview = -1;
				else mInfo.newPlanSizeReview = Double.parseDouble(planSize[i].trim());

				if (iRet != 1) iRet = Defect.doAddReviewProduct(mInfo);

				// If error then add to vector
				if (iRet != 0) {
					errDataVector.addElement(mInfo);
				}
			}

			if ( iRet == 0) {
				if ("defect".equals(fromPage))	defectViewCaller(request,response);
				else if ("quality".equals(fromPage)) QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
			} else {
				request.setAttribute("ErrDefectRevProductList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("defectRevProductAdd.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doAddDefectTestProduct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector errDataVector = new Vector();
			WPSizeInfo mInfo = null;			
			int iRet = 0;
			 
			String[] product = request.getParameterValues("product");
			String[] planSize = request.getParameterValues("planSize");
			String fromPage = (String) request.getParameter("fromPage");

			int size = product.length;
			for (int i = 0; i < size; i++) {
				if (product[i] == null || "0".equals(product[i].trim())) continue;
				mInfo = new WPSizeInfo();				
				
				mInfo.moduleID = Integer.parseInt(product[i].trim());

				if ("".equals(planSize[i].trim()))mInfo.newPlanSizeTest = -1;
				else mInfo.newPlanSizeTest = Double.parseDouble(planSize[i].trim());

				if (iRet != 1) iRet = Defect.doAddTestProduct(mInfo);

				// If error then add to vector
				if (iRet != 0) {
					errDataVector.addElement(mInfo);
				}
			}

			if ( iRet == 0) {
				if ("defect".equals(fromPage))	defectViewCaller(request,response);
				else if ("quality".equals(fromPage)) QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
			} else {
				request.setAttribute("ErrDefectTestProductList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("defectTestProductAdd.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doUpdateDefectRevProduct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector errDataVector = new Vector();
			WPSizeInfo mInfo = null;
			int iRet = 0;
			 
			String[] moduleID = request.getParameterValues("moduleID");
			String[] planSize = request.getParameterValues("planSize");
			String fromPage = (String) request.getParameter("fromPage");

			int size = moduleID.length;
			for (int i = 0; i < size; i++) {
				if (moduleID[i] == null || "0".equals(moduleID[i].trim())) continue;
				mInfo = new WPSizeInfo();				
				
				mInfo.moduleID = Integer.parseInt(moduleID[i].trim());

				if ("".equals(planSize[i].trim()))mInfo.newPlanSizeReview = -1;
				else mInfo.newPlanSizeReview = Double.parseDouble(planSize[i].trim());

				if (iRet != 1) iRet = Defect.doUpdateReviewProduct(mInfo);

				// If error then add to vector
				if (iRet != 0) {
					errDataVector.addElement(mInfo);
				}
			}

			if ( iRet == 0) {				
				if ("defect".equals(fromPage))	defectViewCaller(request,response);
				else if ("quality".equals(fromPage)) QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
			} else {
				request.setAttribute("ErrDefectRevProductList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("defectRevProductUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doUpdateDefectTestProduct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			Vector errDataVector = new Vector();
			WPSizeInfo mInfo = null;
			int iRet = 0;
			 
			String[] moduleID = request.getParameterValues("moduleID");
			String[] planSize = request.getParameterValues("planSize");
			String fromPage = (String) request.getParameter("fromPage");

			int size = moduleID.length;
			for (int i = 0; i < size; i++) {
				if (moduleID[i] == null || "0".equals(moduleID[i].trim())) continue;
				mInfo = new WPSizeInfo();				
				
				mInfo.moduleID = Integer.parseInt(moduleID[i].trim());

				if ("".equals(planSize[i].trim()))mInfo.newPlanSizeTest = -1;
				else mInfo.newPlanSizeTest = Double.parseDouble(planSize[i].trim());

				if (iRet != 1) iRet = Defect.doUpdateTestProduct(mInfo);

				// If error then add to vector
				if (iRet != 0) {
					errDataVector.addElement(mInfo);
				}
			}

			if ( iRet == 0) {
				if ("defect".equals(fromPage))	defectViewCaller(request,response);
				else if ("quality".equals(fromPage)) QualityObjectiveCaller.doGetQualityObjectiveList(request, response, "");
			} else {
				request.setAttribute("ErrDefectTestProductList", errDataVector);
				request.setAttribute("ErrType", iRet+"");
				Fms1Servlet.callPage("defectTestProductUpdate.jsp",request,response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
