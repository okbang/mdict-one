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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fms1.infoclass.*;
import com.fms1.tools.*;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.Parameters;
/**
 * Effort menu pages
 *
 */
public final class EffortCaller  {
    public static final void doGetEffInformation(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HttpSession session = request.getSession();
            final long prjID = Long.parseLong((String)session.getAttribute("projectID"));
			ProjectInfo pnf = Project.getProjectInfo(prjID);
            final EffortInfo effortHeader = Effort.getEffortInfo(pnf);
			request.setAttribute("effortHeaderInfo", effortHeader);
			
			if (
				(pnf.getStartDate().after(Parameters.dayOfValidation)) &&
				(pnf.getApplyPPM() == 1)
				)
				{//Apply for new projects
				double completeness = Requirement.getActualRCR(pnf, Requirement.getRequirementList(pnf));
				double effortStatus = Effort.calcEffortStatus(pnf.getPlannedEffort(), effortHeader.actualEffort, completeness);
				Vector stageList=Schedule.getStageList(pnf);   
				if (stageList.size()>0){
					Vector processEffortByStages = Effort.getProcessEffortByStage(pnf,stageList);
					Vector vt1 = Effort.getStageEffortList(pnf, processEffortByStages);
					request.setAttribute("stageEffortVector", vt1);
				}
				request.setAttribute("stageList", stageList);
				request.setAttribute("effortStatus", CommonTools.formatDouble(effortStatus));
				Fms1Servlet.callPage("effInformation.jsp", request, response);
			}
			else
			{
				Fms1Servlet.callPage("effInformation1.jsp", request, response);
			}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void doGetEffReview(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HttpSession session = request.getSession();
            final String prjIDstr = (String)session.getAttribute("projectID");
            final long prjID = Long.parseLong(prjIDstr);
            Vector vt2 = Effort.getReviewEffortList(prjID);
            session.setAttribute("reviewEffortVector", vt2);
            Fms1Servlet.callPage("effReview.jsp", request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void doGetEffQualityActivity(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HttpSession session = request.getSession();
            final String prjIDstr = (String)session.getAttribute("projectID");
            final long prjID = Long.parseLong(prjIDstr);
			Vector vt4 = Effort.getQltActivityEffortList(Project.getProjectInfo(prjID));
            session.setAttribute("qltActivityEffortVector", vt4);
            Fms1Servlet.callPage("effQualityActivity.jsp", request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void doGetEffWeekly(final HttpServletRequest request, final HttpServletResponse response) {
        doGetEffWeekly(request, response, "");
    }
    public static final void doGetEffWeekly(final HttpServletRequest request, final HttpServletResponse response, String index) {
        try {
            final HttpSession session = request.getSession();
            final long prjID = Long.parseLong((String)session.getAttribute("projectID"));
			ProjectInfo projectInfo = Project.getProjectInfo(prjID);
            Vector vt5 = Effort.getWeeklyEffortList(prjID);
            session.setAttribute("weeklyEffortVector", vt5);
            double totalP = 0;
            double totalA = 0;
            int nweeks = vt5.size();
            WeeklyEffortInfo info;
            for (int i = 0; i < nweeks; i++) {
                info = (WeeklyEffortInfo)vt5.get(i);
                //caculate total plan effort and total deviation
                if (!Double.isNaN(info.estimatedE))
                    totalP += info.estimatedE;
                if (!Double.isNaN(info.actualE))
                    totalA += info.actualE;
            }
            int pageCount;
            if ((nweeks % 20) == 0)
                pageCount = nweeks / 20;
            else
                pageCount = nweeks / 20 + 1;
            final String page = (String)session.getAttribute("weekECurPage");
            if ((page == null) || page.equalsIgnoreCase("") || Integer.parseInt(page) > pageCount) {
                //go to last page by default
                session.setAttribute("weekECurPage", String.valueOf(pageCount));
            }
            final String[] totalWeeklyEffort = new String[2];
            totalWeeklyEffort[0] = CommonTools.decForm.format(totalP);
            totalWeeklyEffort[1] = (totalP == 0) ? "N/A" : CommonTools.decForm.format((totalA - totalP) * 100.0 / totalP);
            session.setAttribute("totalWeeklyEffortArr", totalWeeklyEffort);
            double actualEffort = Effort.getActualEffort(projectInfo.getProjectId());
            session.setAttribute("actualEffort", CommonTools.formatDouble(actualEffort));
            Fms1Servlet.callPage("effWeekly.jsp" + index, request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void doUpdateReviewEffort(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final String strID = request.getParameter("txtReviewEffortID").trim();
            final String strEstimated = request.getParameter("txtEstimated").trim();
            final String strReEstimated = request.getParameter("txtReEstimated").trim();
            final String strActual = request.getParameter("txtActual").trim();
            final String strModuleID = request.getParameter("txtModuleID").trim();
            final ReviewEffortInfo info = new ReviewEffortInfo();
            info.reviewE_ID = Integer.parseInt(strID);
            if (!strEstimated.equals(""))
                info.estimated = Double.parseDouble(strEstimated);
            if (!strReEstimated.equals(""))
                info.reEstimated = Double.parseDouble(strReEstimated);
            if (!strActual.equals(""))
                info.actual = Double.parseDouble(strActual);
            HttpSession session = request.getSession();
            info.prjID = Long.parseLong((String)session.getAttribute("projectID"));
            info.moduleID = Long.parseLong(strModuleID);
            Effort.updateReviewEffort(info);
            doGetEffReview(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void doUpdateQltActivityEffort(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HttpSession session = request.getSession();
            final int vtID = Integer.parseInt(request.getParameter("vtID"));
            final String strEstimated = request.getParameter("txtEstimated").trim();
            final String strReEstimated = request.getParameter("txtReEstimated").trim();
            final String strActual = request.getParameter("txtActual").trim();
            QltActivityEffortInfo info = null;
            final Vector vt = (Vector)session.getAttribute("qltActivityEffortVector");
            if ((vtID >= 0) && (vtID < vt.size())) {
                info = (QltActivityEffortInfo)vt.elementAt(vtID);
            }
            else {
                Fms1Servlet.callPage("qltActEffortUpdate.jsp", request, response);
            }
            if (strEstimated.equals("")) {
                info.estimated = -1;
            }
            else {
                info.estimated = Float.parseFloat(strEstimated);
            }
            if (strReEstimated.equals("")) {
                info.reEstimated = -1;
            }
            else {
                info.reEstimated = Float.parseFloat(strReEstimated);
            }
            if (strActual.equals("")) {
                info.actual = -1;
            }
            else {
                info.actual = Float.parseFloat(strActual);
            }
            Effort.updateQltActivityEffort(info);
            doGetEffQualityActivity(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void doUpdateWeeklyEffort(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final String strID = request.getParameter("txtWeeklyEffortID").trim();
            final String strEstimated = request.getParameter("txtEstimated").trim();
            final String strDate = request.getParameter("txtDate");
            final WeeklyEffortInfo info = new WeeklyEffortInfo();
            HttpSession session = request.getSession();
            info.prjID = Long.parseLong((String)session.getAttribute("projectID"));
            info.weeklyE_ID = Integer.parseInt(strID);
            info.date = new Date(Long.parseLong(strDate));
            if (strEstimated.equals("")) {
                info.estimatedE = -1;
            }
            else {
                info.estimatedE = Float.parseFloat(strEstimated);
            }
            Effort.updateWeeklyEffort(info);
            doGetEffWeekly(request, response, "");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final void doGetEffStageProcess(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HttpSession session = request.getSession();
            final long prjID = Long.parseLong((String)session.getAttribute("projectID"));
			ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			Vector stageList = Schedule.getStageList(projectInfo);   
			if (stageList.size()==0){
				Fms1Servlet.callPage("error.jsp?error=Please define at least one stage to access this page",request,response);
				return;
			}
			if (
				(projectInfo.getStartDate().after(Parameters.dayOfValidation)) &&
				(projectInfo.getApplyPPM() == 1)
				){//Apply for new projects

	            Vector processEffortByStages = Effort.getProcessEffortByStage(projectInfo, stageList);
	            Vector vt1 = Effort.getStageEffortList(projectInfo, processEffortByStages);
	            Vector vt3 = Effort.getProcessEffortList(projectInfo,processEffortByStages);				
	            session.setAttribute("processEffortVector", vt3);
	            session.setAttribute("projectInfo", projectInfo);
	            session.setAttribute("processEffortByStages", processEffortByStages);
	            session.setAttribute("stageEffortVector", vt1);
	            Fms1Servlet.callPage("effStage.jsp", request, response);
			}
			else {
				Vector vt3 = Effort.getProcessEffortList(projectInfo, null, null);
				Vector vt4 = Effort.getProcessEffortByStageList(projectInfo, stageList);
				//Hieunv1 change reference from Long prjID into ProjectInfo.
				//to avoid getProjectInfo again. increase performance.
				Vector vt1 = Effort.getEffortListByStage(projectInfo, stageList);
				session.setAttribute("stageEffortVector", vt1);
				session.setAttribute("processEffortVector", vt3);
				session.setAttribute("processEffortByStageVector", vt4);
				Fms1Servlet.callPage("effProcess.jsp",request,response);
			}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Add by HaiMM
	public static final void doGetEstEffort(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID = Long.parseLong((String)session.getAttribute("projectID"));
			ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			Vector stageList = Schedule.getStageList(projectInfo);   
			if (stageList.size()==0){
				Fms1Servlet.callPage("error.jsp?error=Please define at least one stage to access this page",request,response);
				return;
			}

			Vector estEffort = Effort.getEstEffortList(projectInfo);
			session.setAttribute("estEffort", estEffort);
			session.setAttribute("stageList", stageList);
			
			Fms1Servlet.callPage("estEffProcess.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static final void getBatchPlan(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID = Long.parseLong((String)session.getAttribute("projectID"));
			ProjectInfo pnf = Project.getProjectInfo(prjID);
			Vector stageList=Schedule.getStageList(pnf);   
			if (stageList.size()==0){
				Fms1Servlet.callPage("error.jsp?error=Please define at least one stage to access this page",request,response);
				return;
			}

			final String strIsReplan = request.getParameter("isReplan");
			
			boolean isReplan = false;
			if (strIsReplan != null)
				isReplan="1".equals(strIsReplan.trim());
			
			Vector processEffortByStages = (isReplan)?Effort.getProcessEffortByStage(pnf,stageList):Effort.getProcessEffortByStage(pnf,stageList,0);
			session.setAttribute("processEffortByStages", processEffortByStages);

	        Fms1Servlet.callPage("effDetailBatchPlan.jsp", request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static final void updateBatchPlan(final HttpServletRequest request, final HttpServletResponse response) {
            final HttpSession session = request.getSession();

			final long prjID = Long.parseLong((String)session.getAttribute("projectID"));
			ProjectInfo pnf = Project.getProjectInfo(prjID);
			Vector stageList=Schedule.getStageList(pnf);
			StageInfo firstStage = (StageInfo)stageList.elementAt(0);
			
			boolean isFirstStage = false;
			if (firstStage.aEndD == null)
				isFirstStage = true;     

            Vector processEffortByStages=(Vector)session.getAttribute("processEffortByStages");
            ProcessEffortByStageInfo inf;
            PlanRCRInfo upd;
            Vector updates=new Vector();
            long curStage=-1;
            for (int i=0; i < processEffortByStages.size(); i++) {
                inf = (ProcessEffortByStageInfo)processEffortByStages.elementAt(i);
                if (inf.isOpen){
                    if (curStage==-1)
                        curStage=inf.stageid;
                    for (int j=0;j<ProcessInfo.trackedProcessId.length;j++){
                        upd=new PlanRCRInfo();
                        upd.milestone=curStage;
                        upd.stage=inf.stageid;
                        upd.processid=ProcessInfo.trackedProcessId[j];
                        /*
                        if (!isFirstStage)
                        {
							inf.rePlanned[j]=CommonTools.parseDouble(request.getParameter("plan"+j+"p"+i));
							upd.plannedEffort=inf.rePlanned[j];
                        }
                        else
                        {
							inf.planned[j]=CommonTools.parseDouble(request.getParameter("plan"+j+"p"+i));
							upd.plannedEffort=inf.planned[j];
                        }
                        */
						inf.rePlanned[j]=CommonTools.parseDouble(request.getParameter("plan"+j+"p"+i));
						upd.plannedEffort=inf.rePlanned[j];
                        updates.add(upd);
                    }
                }
            }

            Effort.updatePlanEffortStageProcess(updates); 
			doGetEffStageProcess(request,response);
		/*
		if (!isFirstStage)
	    	Fms1Servlet.callPage("effDetailBatchPlan.jsp?isReplan=1",request,response);
	    else
			Fms1Servlet.callPage("effDetailBatchPlan.jsp",request,response);
		*/
    }
	public static final void doBatchUppdateProcessEffort(final HttpServletRequest request, final HttpServletResponse response) {
		try {

			String[] arrProcessEffortId = request.getParameterValues("processEffortId");
			String[] arrEstimated = request.getParameterValues("estimated");
			String[] arrReEstimated = request.getParameterValues("reestimated");

			if (arrProcessEffortId != null) {
				for (int i = 0; i < arrProcessEffortId.length; i++) {
					if (arrProcessEffortId[i].length() > 0) {

						String strID = arrProcessEffortId[i];
						String strEstimated = arrEstimated[i];
						String strReEstimated = arrReEstimated[i];

						final ProcessEffortInfo info = new ProcessEffortInfo();

						info.proEffID = Integer.parseInt(strID);
						info.estimated = CommonTools.parseDouble(strEstimated);
						info.reEstimated =CommonTools.parseDouble(strReEstimated);

						Effort.updateProcessEffort(info);

					}
				}
			}

			doGetEffProcess(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doGetEffProcess(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			Vector stageList = Schedule.getStageList(projectInfo);
			Vector vt3 = Effort.getProcessEffortList(projectInfo, null, null);
			Vector vt4 = Effort.getProcessEffortByStageList(projectInfo, stageList);
			session.setAttribute("processEffortVector", vt3);
			session.setAttribute("processEffortByStageVector", vt4);
			Fms1Servlet.callPage("effProcess.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUppdateStageEffort(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final int vtID = Integer.parseInt(request.getParameter("vtID"));
			final String strEstimated = request.getParameter("txtEstimated").trim();
			final String strReEstimated = request.getParameter("txtReEstimated").trim();
			StageEffortInfo info = new StageEffortInfo();
			final Vector vt = (Vector) session.getAttribute("stageEffortVector");
			if ((vtID >= 0) && (vtID < vt.size()))
				info = (StageEffortInfo) vt.elementAt(vtID);
			else
				Fms1Servlet.callPage("error.jsp?error=Bad vector",request,response);
			info.estimated = CommonTools.parseDouble(strEstimated);
			info.reEstimated = CommonTools.parseDouble(strReEstimated);
			Effort.updateStageEffort(info);
			doGetEffStageProcess(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
