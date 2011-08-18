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
import java.util.Vector;
import com.fms1.infoclass.*;
import com.fms1.tools.*;
import com.fms1.web.Constants;
/**
 * Schedule Quality page
 *
 */
final class WorkProductCaller {

	/**
	 * Update review and test activity
	 */
	public static final void doUpdateModule(final HttpServletRequest request) {
		try {
			final ModuleUpdateInfo modunInfo = new ModuleUpdateInfo();
			modunInfo.moduleID = Long.parseLong(request.getParameter("txtModuleID"));
			modunInfo.pReleaseD = CommonTools.parseSQLDate(request.getParameter("txtPReleaseD"));
			modunInfo.rpReleaseD = CommonTools.parseSQLDate(request.getParameter("txtRPReleaseD"));
			modunInfo.aReleaseD =CommonTools.parseSQLDate(request.getParameter("txtAReleaseD"));
			modunInfo.pReviewD = CommonTools.parseSQLDate(request.getParameter("txtPReviewD"));
			modunInfo.aReviewD = CommonTools.parseSQLDate(request.getParameter("txtAReviewD"));
			modunInfo.pTestD = CommonTools.parseSQLDate(request.getParameter("txtPTestD"));
			modunInfo.aTestD = CommonTools.parseSQLDate(request.getParameter("txtATestD"));
			modunInfo.conductor = request.getParameter("cmbConductor").trim();
			//modunInfo.reviewer = request.getParameter("txtReviewer").trim();
			//modunInfo.approver = request.getParameter("txtApprover").trim();
			//modunInfo.reviewer = "";
			//modunInfo.approver = "";
			final String status = request.getParameter("cmbStatus");
			//deliverables
			modunInfo.status = (status != null)?Integer.parseInt(status):-1;
			WorkProduct.updateModule(modunInfo);
			final HttpSession session = request.getSession();
			Vector vtModun=(Vector)session.getAttribute("moduleVector");
			ModuleInfo modun = null;
			int prjID = Integer.parseInt((String)session.getAttribute("projectID"));
			int i;
			for(i=0;i<vtModun.size();i++)
			{
				modun = (ModuleInfo)vtModun.get(i);	
				if (modun.moduleID == modunInfo.moduleID )
					break;
			}
			if (i<vtModun.size())
			{
				String oldValue = CommonTools.dateFormat(modun.plannedReleaseDate);
				String newValue = CommonTools.dateFormat(modunInfo.pReleaseD);
				if((modun.isDel)&&(!modun.plannedReleaseDate.equals(modunInfo.pReleaseD)))
					WorkOrderCaller.addChangeAuto(prjID,Constants.ACTION_UPDATE,Constants.SCHEDULE_REVIEW_AND_TEST,modun.name,newValue,oldValue);
			}
				
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Add new OtherActivity
	 * @param OtherActivityInfo
	 */
	public static final void doAddOtherAct(final HttpServletRequest request) {
		try {
			final HttpSession session = request.getSession();
			final OtherActInfo oaInfo = new OtherActInfo();
			oaInfo.prjID = Long.parseLong( (String) session.getAttribute("projectID"));
			oaInfo.activity = request.getParameter("txtActivity").trim();
			oaInfo.pStartD = CommonTools.parseSQLDate(request.getParameter("txtPStartD"));
			oaInfo.pEndD = CommonTools.parseSQLDate(request.getParameter("txtPEndD"));
			oaInfo.aEndD = CommonTools.parseSQLDate(request.getParameter("txtAEndD"));
			oaInfo.conductor = Long.parseLong(request.getParameter("cmbConductor"));
			oaInfo.note = request.getParameter("txtNote").trim();
			oaInfo.qcActivity=Integer.parseInt(request.getParameter("txtType"));
			oaInfo.status = Integer.parseInt(request.getParameter("status"));
			oaInfo.metric=CommonTools.parseDouble(request.getParameter("metricVal"));
			QualityObjective.addOtherActivity(oaInfo);
			final QltActivityEffortInfo info = new QltActivityEffortInfo();
			info.prjID = oaInfo.prjID;
			info.activityID = oaInfo.otherActID;
			info.type = QltActivityEffortInfo.TYPE_OTHER_QUALITY;
			Effort.addQltActEffort(info);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Update OtherActivity
	 * @return void
	 * @param OtherActivityInfo
	 */
	public static final void doUpdateOtherAct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			Vector vtOtherAct=(Vector)session.getAttribute("otherActVector");
			int vtID=Integer.parseInt(request.getParameter("vtID"));
			OtherActInfo oaInfo=(OtherActInfo)vtOtherAct.get(vtID);
			oaInfo.pStartD = CommonTools.parseSQLDate(request.getParameter("txtPStartD"));
			oaInfo.pEndD =CommonTools.parseSQLDate(request.getParameter("txtPEndD"));
			oaInfo.aEndD = CommonTools.parseSQLDate(request.getParameter("txtAEndD"));
			oaInfo.conductor = Long.parseLong(request.getParameter("cmbConductor"));
			oaInfo.note = request.getParameter("txtNote");
			if(oaInfo.qcActivity!=QCActivityInfo.QUALITY_GATE_INSPECTION && oaInfo.risk_type !=1 && oaInfo.risk_type !=2){
				oaInfo.qcActivity = Integer.parseInt(request.getParameter("txtType"));
				oaInfo.status = Integer.parseInt(request.getParameter("status"));
			}
			oaInfo.metric=CommonTools.parseDouble(request.getParameter("metricVal"));
			// Added by HaiMM - Update to Risk
			if (oaInfo.risk_type == 1 || oaInfo.risk_type == 2) {
				UserInfo userInfo = new UserInfo();
				userInfo = UserHelper.getUser(oaInfo.conductor);
				String account = userInfo.account; 
				oaInfo.status = Integer.parseInt(request.getParameter("status"));
				int status = convStatusToRisk(oaInfo.status);
				
				QualityObjective.updateToRisk(oaInfo, account, status);
				QualityObjective.uppdateOtherActivity(oaInfo);
			} else {
				oaInfo.activity = request.getParameter("txtActivity");
				QualityObjective.uppdateOtherActivity(oaInfo);	
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int convStatusToRisk(int status) {
			int result = 0;
			if (status == 5) {
				result = 1;
			} else if (status == 3) {
				result = 2;
			} else if (status == 1) {
				result = 3;
			} else if (status == 2) {
				result = 4;
			} else if (status == 4) {
				result = 5;
			}
		
			return result;
		}
    
	public static final void doDeleteOtherAct(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String otherActIDstr = request.getParameter("txtOtherActID").trim();
			final long otherActID = Long.parseLong(otherActIDstr);
			QualityObjective.deleteOtherActivity(otherActID);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}