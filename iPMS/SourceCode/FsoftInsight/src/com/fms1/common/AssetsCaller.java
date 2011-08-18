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
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fms1.tools.*;
import com.fms1.infoclass.*;
import com.fms1.web.Constants;
import com.fms1.web.Parameters;
import com.fms1.web.Fms1Servlet;

/**
 * Tailoring and deviation pages
 */

public final class AssetsCaller {
	public static final void doApplyPPM(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID = Long.parseLong((String)session.getAttribute("projectID"));
			int applyPPM = Integer.parseInt((String)request.getParameter("applyPPM"));
			String reason = (String)request.getParameter("reason");
			if (!Assets.Update_ApplyPPM(prjID, applyPPM, reason)) {
				Fms1Servlet.callPage("error.jsp?error=Update applying PPM feature to pjoject", request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doLoadTailoringPage(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID = Long.parseLong((String)session.getAttribute("projectID"));
			ProjectInfo ProInfo = Project.getProjectInfo(prjID);
			int tailoringCnt = 0;
			int deviationCnt = 0;
			Vector tailoringList = Assets.getDeviationList(prjID, "");
			for (int i = 0; i < tailoringList.size(); i++) {
				if (((TailoringDeviationInfo)tailoringList.elementAt(i)).type == 1) {
					tailoringCnt++;
				}
				else {
					deviationCnt++;
				}
			}
			int tailoringPageCnt = tailoringList.size() / 10;
			if (tailoringList.size() % 10 != 0) {
				tailoringPageCnt++;
			}
			int tailoringPageNumber = 1;
			String strTailoringPageNumber = (String)session.getAttribute("tailoringPageNumber");
			//go back to the same page after update/add/delete
			if (strTailoringPageNumber != null) {
				int oldTailoringPageNumber = Integer.parseInt(strTailoringPageNumber);
				if (oldTailoringPageNumber <= tailoringPageCnt) {
                    tailoringPageNumber = oldTailoringPageNumber;
				}
			}
			session.setAttribute("tailoringPageCnt", "" + tailoringPageCnt);
			session.setAttribute("tailoringPageNumber", "" + tailoringPageNumber);
			session.setAttribute("deviationCnt", "" + deviationCnt);
			session.setAttribute("tailoringCnt", "" + tailoringCnt);
			session.setAttribute("tailoringList", tailoringList);
			//if (ProInfo.start_date.after(Parameters.dayOfValidation) && (ProInfo.apply_PPM==1)){//Apply for new projects
			session.setAttribute("applyDate", "" + (ProInfo.getStartDate().after(Parameters.dayOfValidation) ? 1 : 0));
			session.setAttribute("applyPPM", "" + ProInfo.getApplyPPM());
			session.setAttribute("reason", ProInfo.getReason());
			String updateAction = (String)request.getParameter("updateAction");
			session.setAttribute("updateAction", updateAction);
			session.setAttribute("ProjectInfo", ProInfo);
			Fms1Servlet.callPage("tailoring.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doNextTailoring(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int tailoringPageNumber = Integer.parseInt((String)session.getAttribute("tailoringPageNumber"));
			tailoringPageNumber++;
			session.setAttribute("tailoringPageNumber", "" + tailoringPageNumber);
			Fms1Servlet.callPage("tailoring.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doPrevTailoring(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			int tailoringPageNumber = Integer.parseInt((String)session.getAttribute("tailoringPageNumber"));
			tailoringPageNumber--;
			session.setAttribute("tailoringPageNumber", "" + tailoringPageNumber);
			Fms1Servlet.callPage("tailoring.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//	public static final void doPrepareTailoringUpdate(final HttpServletRequest request, final HttpServletResponse response) {
	//		try {
	//			final HttpSession session = request.getSession();
	//			final long tdID = Long.parseLong(request.getParameter("tailoring_ID"));
	//			final Vector catList = Assets.getTDCategory();
	//			session.setAttribute("tailoring_category", catList);
	//			final TailoringDeviationInfo info = Assets.getTD(tdID);
	//			session.setAttribute("tailoringInfo", info);
	//			String tailoring_source = request.getParameter("tailoring_source");
	//			if (tailoring_source == null)
	//				tailoring_source = "0"; 
	//			session.setAttribute("tailoring_source", tailoring_source);
	//			Fms1Servlet.callPage("tailoringUpdate.jsp",request,response);
	//		}
	//		catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}
	//	
	public static final void doPrepareTailoringAdd(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = CommonTools.parseInt((String)session.getAttribute("projectID"));
			Vector proTailoringList = (Vector)session.getAttribute("ProTailoringList");
			long protailID = 0;
			Vector catList = Assets.getTDCategory();
			session.setAttribute("tailoring_category", catList);
			String tailoring_source = request.getParameter("tailoring_source");
			if (tailoring_source == null) {
				tailoring_source = "0";
			}
			session.setAttribute("tailoring_source", tailoring_source);
			Fms1Servlet.callPage("tailoringAdd.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doPrepareDeviationAdd(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final Vector catList = Assets.getTDCategory();
			session.setAttribute("tailoring_category", catList);
			String tailoring_source = request.getParameter("tailoring_source");
			if (tailoring_source == null) {
                tailoring_source = "0";
			}
			session.setAttribute("tailoring_source", tailoring_source);
			Fms1Servlet.callPage("deviationAdd.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doAddTailoring(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = CommonTools.parseInt((String)session.getAttribute("projectID"));
			String[] sProtailID = (String[])request.getParameterValues("process_tailID");
			// Modify by HaiMM - Start
			//String[] sAction = (String[])request.getParameterValues("tailoring_action");
			String[] sAction = (String[])request.getParameterValues("action");
			//Modify by HaiMM - End
			
			String[] note = (String[])request.getParameterValues("tailoring_note");
			int nAction = 0;
			int nProtailID = 0;
			for (int i = 0; i < sAction.length; i++) {
				if (note[i] == null) {
					note[i] = "";
				}
				nAction = Integer.parseInt(sAction[i]);
				nProtailID = Integer.parseInt(sProtailID[i]);
				final TailoringInfo tailoringInfo =
					new TailoringInfo(
						nProtailID,
						prjID,
						"",
						"",
						nAction,
						"",
						"",
						note[i]);
				Assets.addTailoring(tailoringInfo);
				if ("1".equals((String)session.getAttribute("tailoring_source"))) {
					ProjectPlanCaller.addChangeAuto(
						prjID,
						Constants.ACTION_ADD,
						Constants.PL_LIFECYCLE,
						"Tailoring deviation>" + tailoringInfo.modification,
						null,
						null);
				}
			}
			if ("1".equals((String)session.getAttribute("tailoring_source"))) {
				ProjectPlanCaller.doLoadPLLifecycle(request, response, "");
			}
			else {
				doLoadTailoringPage(request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doPrepareTailoringUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long pro_tail_ID = Long.parseLong(request.getParameter("pro_tail_ID"));
			final long prjID = Long.parseLong((String)session.getAttribute("projectID"));
			final TailoringInfo info = Assets.getTailoring(prjID, pro_tail_ID);
			session.setAttribute("tailoringInfo", info);
			String tailoring_source = request.getParameter("tailoring_source");
			if (tailoring_source == null) {
                tailoring_source = "0";
			}
			session.setAttribute("tailoring_source", tailoring_source);
			Fms1Servlet.callPage("tailoringUpdate.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateTailoring(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int pro_tail_ID = CommonTools.parseInt(request.getParameter("pro_tail_ID"));
			final int prjID = CommonTools.parseInt((String)session.getAttribute("projectID"));
			// Modify by HaiMM - Start
			//final int action = CommonTools.parseInt(request.getParameter("tailoring_action"));
			final int action = CommonTools.parseInt(request.getParameter("action"));
			// Modify by HaiMM - End
			String note = request.getParameter("tailoring_note");
			if (note == null) {
				note = "";
			}
			final TailoringInfo tailoringInfo =
				new TailoringInfo(
					pro_tail_ID,
					prjID,
					"",
					"",
					action,
					"",
					"",
					note);
			Assets.updateTailoring(tailoringInfo);
			if ("1".equals((String)session.getAttribute("tailoring_source"))) {
				TailoringInfo oldInfo = (TailoringInfo)session.getAttribute("tailoringInfo");
				String newValue, oldValue;
				newValue = tailoringInfo.modification;
				oldValue = oldInfo.modification;
				if (!newValue.equalsIgnoreCase(oldValue)) {
					ProjectPlanCaller.addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.PL_LIFECYCLE,
						"Tailoring deviation>Modification",
						newValue,
						oldValue);
                }
				newValue = tailoringInfo.reason;
				oldValue = oldInfo.reason;
				if (!newValue.equalsIgnoreCase(oldValue)) {
					ProjectPlanCaller.addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.PL_LIFECYCLE,
						"Tailoring deviation>Reason",
						newValue,
						oldValue);
                }
				switch (oldInfo.action) {
					case 1 :
						oldValue = "Added";
						break;
					case 2 :
						oldValue = "Modified";
						break;
					case 3 :
						oldValue = "Deleted";
						break;
					default :
						oldValue = "";
				}
				switch (tailoringInfo.action) {
					case 1 :
						newValue = "Added";
						break;
					case 2 :
						newValue = "Modified";
						break;
					case 3 :
						newValue = "Deleted";
						break;
					default :
						newValue = "";
				}
				if (!newValue.equalsIgnoreCase(oldValue)) {
					ProjectPlanCaller.addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.PL_LIFECYCLE,
						"Tailoring deviation>Action",
						newValue,
						oldValue);
                }
				ProjectPlanCaller.doLoadPLLifecycle(request, response, "");
			}
			else {
				doLoadTailoringPage(request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeleteTailoring(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = CommonTools.parseInt((String)session.getAttribute("projectID"));
			final long pro_tail_ID = CommonTools.parseLong(request.getParameter("pro_tail_ID"));
			Assets.deleteTailoring(prjID, pro_tail_ID);
			if ("1".equals((String)session.getAttribute("tailoring_source"))) {
				TailoringInfo info = (TailoringInfo)session.getAttribute("tailoringInfo");
				ProjectPlanCaller.addChangeAuto(
					prjID,
					Constants.ACTION_DELETE,
					Constants.PL_LIFECYCLE,
					"Tailoring deviation>" + info.modification,
					null,
					null);
				ProjectPlanCaller.doLoadPLLifecycle(request, response, "");
			}
			else {
				doLoadTailoringPage(request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doAddDeviation(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String)session.getAttribute("projectID"));
			final String modification = request.getParameter("tailoring_modification");
			final int action = Integer.parseInt(request.getParameter("tailoring_action"));
			final String reason = request.getParameter("tailoring_reason");
			final String category = request.getParameter("tailoring_category");
			String note = request.getParameter("tailoring_note");
			if (note == null) {
				note = "";
			}
			final DeviationInfo deviationInfo =
				new DeviationInfo(
					0,
					prjID,
					"",
					modification,
					action,
					reason,
					category,
					note);
			Assets.addDeviation(deviationInfo);
			if ("1".equals((String)session.getAttribute("tailoring_source"))) {
				ProjectPlanCaller.addChangeAuto(
					prjID,
					Constants.ACTION_ADD,
					Constants.PL_LIFECYCLE,
					"Tailoring deviation>" + deviationInfo.modification,
					null,
					null);
				ProjectPlanCaller.doLoadPLLifecycle(request, response, "");
			}
			else {
				doLoadTailoringPage(request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doPrepareDeviationUpdate(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long deviationID = Long.parseLong(request.getParameter("deviation_ID"));
			final Vector catList = Assets.getTDCategory();
			session.setAttribute("tailoring_category", catList);
			final DeviationInfo info = Assets.getDeviation(deviationID);
			session.setAttribute("deviationInfo", info);
			String tailoring_source = request.getParameter("tailoring_source");
			if (tailoring_source == null) {
                tailoring_source = "0";
			}
			session.setAttribute("tailoring_source", tailoring_source);
			Fms1Servlet.callPage("DeviationUpdate.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUpdateDeviation(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long tdID = Long.parseLong(request.getParameter("deviation_tdID"));
			final int prjID = Integer.parseInt((String)session.getAttribute("projectID"));
			final String modification = request.getParameter("deviation_modification");
			final int action = Integer.parseInt(request.getParameter("deviation_action"));
			final String reason = request.getParameter("deviation_reason");
			// final int type = Integer.parseInt(request.getParameter("tailoring_type"));
			final String category = request.getParameter("deviation_category");
			String note = request.getParameter("deviation_note");
			if (note == null) {
				note = "";
			}
			final DeviationInfo deviationInfo =
				new DeviationInfo(
					tdID,
					prjID,
					"",
					modification,
					action,
					reason,
					category,
					note);
			Assets.updateDeviation(deviationInfo);
			if ("1".equals((String)session.getAttribute("tailoring_source"))) {
				DeviationInfo oldInfo = (DeviationInfo)session.getAttribute("deviationInfo");
				String newValue, oldValue;
				newValue = deviationInfo.modification;
				oldValue = oldInfo.modification;
				if (!newValue.equalsIgnoreCase(oldValue)) {
					ProjectPlanCaller.addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.PL_LIFECYCLE,
						"Tailoring deviation>Modification",
						newValue,
						oldValue);
                }
				newValue = deviationInfo.reason;
				oldValue = oldInfo.reason;
				if (!newValue.equalsIgnoreCase(oldValue)) {
					ProjectPlanCaller.addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.PL_LIFECYCLE,
						"Tailoring deviation>Reason",
						newValue,
						oldValue);
                }
				switch (oldInfo.action) {
					case 1 :
						oldValue = "Added";
						break;
					case 2 :
						oldValue = "Modified";
						break;
					case 3 :
						oldValue = "Deleted";
						break;
					default :
						oldValue = "";
				}
				switch (deviationInfo.action) {
					case 1 :
						newValue = "Added";
						break;
					case 2 :
						newValue = "Modified";
						break;
					case 3 :
						newValue = "Deleted";
						break;
					default :
						newValue = "";
				}
				if (!newValue.equalsIgnoreCase(oldValue)) {
					ProjectPlanCaller.addChangeAuto(
						prjID,
						Constants.ACTION_UPDATE,
						Constants.PL_LIFECYCLE,
						"Tailoring deviation>Action",
						newValue,
						oldValue);
                }
				ProjectPlanCaller.doLoadPLLifecycle(request, response, "");
			}
			else {
				doLoadTailoringPage(request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeleteDeviation(
		final HttpServletRequest request,
		final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int prjID = Integer.parseInt((String)session.getAttribute("projectID"));
			final long tdID = Long.parseLong(request.getParameter("deviation_deleteID"));
			Assets.deleteDeviation(tdID);
			if ("1".equals((String)session.getAttribute("tailoring_source"))) {
				DeviationInfo info = (DeviationInfo)session.getAttribute("deviationInfo");
				ProjectPlanCaller.addChangeAuto(
					prjID,
					Constants.ACTION_DELETE,
					Constants.PL_LIFECYCLE,
					"Tailoring deviation>" + info.modification,
					null,
					null);
				ProjectPlanCaller.doLoadPLLifecycle(request, response, "");
			}
			else {
				doLoadTailoringPage(request, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}