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
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.*;
import com.fms1.common.group.*;
import com.fms1.tools.*;
import com.fms1.web.Fms1Servlet;
/**
 * Summary metrics and  Organization norms pages
 * @author Manu
 */
public class NormCaller {
	public static final void doGetNormTable(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final long prjID = Long.parseLong((String) session.getAttribute("projectID"));
			final Vector normInfoList = Norms.getNormList(prjID);
			if (normInfoList == null) {
				Fms1Servlet.callPage("error.jsp",request,response);
			}
			session.setAttribute("normTable", normInfoList);
			Fms1Servlet.callPage("norm.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void getPlannedNorms(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String error) {
		int lifecycleID = ProjectInfo.LIFECYCLE_DEVELOPMENT;;
		try {
			final HttpSession session = request.getSession();
			int type= Integer.parseInt(request.getParameter("type"));
			if (type == MetricDescInfo.GR_SOFTWARE){
				NormCaller.getPlannedNormsSoftware(request, response, error);
				return;
			}
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			String strYear = request.getParameter("txtYear");
			int year;

			String period;
			if (strYear != null) {
				//called after selecting combos
				year = Integer.parseInt(strYear);
				period = request.getParameter("txtPeriod");
				if(request.getParameter("txtLifecycle") != null)
					lifecycleID = Integer.parseInt(request.getParameter("txtLifecycle"));
			}
			else {
				Object temp = session.getAttribute("normPlan");
				if (temp != null) {
					//reuse the parameters from the previous consultation
					NormPlanInfo normPlanInfo = (NormPlanInfo) temp;
					year = normPlanInfo.year;
					period = normPlanInfo.term;
					lifecycleID = normPlanInfo.lifecycleID;
				}
				else {
					//called first time from the menu
					Calendar cal = new GregorianCalendar();
					cal.setTime(new java.util.Date());
					int month = cal.get(Calendar.MONTH);
					year = cal.get(Calendar.YEAR);
					period = (month > 6) ? "S2" : "S1";
					lifecycleID = ProjectInfo.LIFECYCLE_DEVELOPMENT;
				}
			}
			NormPlanInfo normPlanInfo = getPlannedNorms(period, year, workUnitID, type);

			session.setAttribute("normPlan", normPlanInfo);
			
//			get Norm of All Operation Group
			java.sql.Date[] dates = PCB.getDatesFromPrevPeriod(period, year);
			java.sql.Date[] dates2 = PCB.getDatesFromPeriod(period, year);
			  Vector prevAllOperationGroup = Norms.getNormOfAllOperationGroup(lifecycleID, dates[1]);
			  session.setAttribute("prevAllOperationGroup", prevAllOperationGroup);
			  Vector allOperationGroup = Norms.getNormOfAllOperationGroup(lifecycleID, dates2[1]);
			  session.setAttribute("allOperationGroup", allOperationGroup);
			
			Fms1Servlet.callPage("Group/normPlanSQA.jsp?error=" + error,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final NormPlanInfo getPlannedNorms(String period, int year,long workUnitID,int metricGoup ){
			int lifecycleID = ProjectInfo.LIFECYCLE_DEVELOPMENT;
			java.sql.Date[] dates = PCB.getDatesFromPrevPeriod(period, year);
			Vector childMetrics = Metrics.getChildrenMetrics(metricGoup);
			NormPlanInfo prevNormPlanInfo = Norms.getNormPlan(workUnitID, lifecycleID, dates[1]);
			//we need to get values for actual period in case the plan already exists
			java.sql.Date[] dates2 = PCB.getDatesFromPeriod(period, year);
			NormPlanInfo normPlanInfo = Norms.getNormPlan(workUnitID, lifecycleID, dates2[1]);
			//merge all the info into NormPlanInfo
			normPlanInfo.startDate=dates2[0];
			normPlanInfo.year = year;
			normPlanInfo.term = period;
			MetricDescInfo metricDescInfo;
			NormPlanInfo.Row theRow=null,rowTemp;
			boolean metricFound;
			for (int i =0;i<childMetrics.size();i++){
				metricDescInfo=(MetricDescInfo)childMetrics.elementAt(i);
				metricFound=false;
				for (int j=0;j<normPlanInfo.rows.size();j++){
					theRow=(NormPlanInfo.Row)normPlanInfo.rows.elementAt(j);
					if (theRow.metricID==metricDescInfo.metricConstant){
						metricFound=true;
						theRow.metricName=metricDescInfo.metricName;
						theRow.metricUnit=metricDescInfo.unit;
						theRow.displayIndex=metricDescInfo.displayIndex;
						break;
					}
				}
				if (!metricFound){
					theRow=normPlanInfo.new Row();
					theRow.metricName=metricDescInfo.metricName;
					theRow.metricUnit=metricDescInfo.unit;
					theRow.strMetricID=metricDescInfo.metricID;
					theRow.metricID=metricDescInfo.metricConstant;
					theRow.displayIndex=metricDescInfo.displayIndex;
					normPlanInfo.rows.add(theRow);
				}
				//dirty metrics actual value set in norms:
				if (theRow.metricID ==MetricDescInfo.PQA_SATISFACTION||theRow.metricID ==MetricDescInfo.SATISFIED_INDICATORS){
					theRow.actualValue=Metrics.getMetric("PQA",theRow.metricID,normPlanInfo.startDate).actualValue;
				}
				for (int j = 0; j < prevNormPlanInfo.rows.size(); j++) {
					rowTemp = (NormPlanInfo.Row) prevNormPlanInfo.rows.elementAt(j);
					if (theRow.metricID == rowTemp.metricID) {
						theRow.prevLCL = rowTemp.LCL;
						theRow.prevUCL = rowTemp.UCL;
						theRow.prevNorm = rowTemp.norm;
						break;
					}
				}
			}
			//in some cases we only need a part of the norms , remove unecessary metrics:
			for (int j=0;j<normPlanInfo.rows.size();j++){
				metricFound=false;
				theRow=(NormPlanInfo.Row)normPlanInfo.rows.elementAt(j);
				for (int i =0;i<childMetrics.size();i++){
					metricDescInfo=(MetricDescInfo)childMetrics.elementAt(i);
					if (metricDescInfo.metricConstant== theRow.metricID){
						metricFound=true;
						break;
					}
				}
				if (!metricFound)
					normPlanInfo.rows.remove(j--);
			}
			CommonTools.sortVector(normPlanInfo.rows,"displayIndex");

			return normPlanInfo;
	}
	
	public static final void getPlannedNormsSoftware(
		final HttpServletRequest request,
		final HttpServletResponse response,
		String error) {
		try {
			final HttpSession session = request.getSession();
			long workUnitID = Long.parseLong((String) session.getAttribute("workUnitID"));
			String strYear = request.getParameter("txtYear");
			int year;
			int lifecycleID;
			String period;
			if (strYear != null) {
				//called after selecting combos
				year = Integer.parseInt(strYear);
				period = request.getParameter("txtPeriod");
				lifecycleID = Integer.parseInt(request.getParameter("txtLifecycle"));
			}
			else {
				Object temp = session.getAttribute("normPlan");
				if (temp != null) {
					//reuse the parameters from the previous consultation
					NormPlanInfo normPlanInfo = (NormPlanInfo) temp;
					year = normPlanInfo.year;
					period = normPlanInfo.term;
					lifecycleID = normPlanInfo.lifecycleID;
				}
				else {
					//called first time from the menu
					Calendar cal = new GregorianCalendar();
					cal.setTime(new java.util.Date());
					int month = cal.get(Calendar.MONTH);
					year = cal.get(Calendar.YEAR);
					period = (month > 6) ? "S2" : "S1";
					lifecycleID = ProjectInfo.LIFECYCLE_DEVELOPMENT;
				}
			}
			java.sql.Date[] dates = PCB.getDatesFromPrevPeriod(period, year);
			Vector tempProjects = Project.getChildProjectsByWU(workUnitID, dates[0], dates[1], Project.CLOSEDPROJECTS);
			Vector projectList = PCBCaller.pcbFilterProjects(lifecycleID, tempProjects);
			//sofware metrics
			int[] metrics = new int[6];
            //metrics[0] = MetricDescInfo.GR_EFFORT;
            metrics[0] = MetricDescInfo.GR_REQUIREMENT;
			metrics[1] = MetricDescInfo.GR_SCHEDULE;
			metrics[2] = MetricDescInfo.GR_EFFORT;
			metrics[3] = MetricDescInfo.GR_PRODUCT_QUALITY;
			metrics[4] = MetricDescInfo.GR_PRODUCTIVITY;
			metrics[5] = MetricDescInfo.GR_PRODUCT_SIZE;
            Vector pcbMetrics ;
            
            boolean refreshCache = "Refresh PCB".equals(request.getParameter("refreshCache"));
            //System.out.println("Refresh PCB "+(refreshCache?"true":"false"));
            if (refreshCache)
                pcbMetrics= PCB.getPCBMetricGroupsRefreshCache(workUnitID,lifecycleID,metrics, projectList, dates[1]);
            else
                pcbMetrics = PCB.getPCBMetricGroups(workUnitID,lifecycleID,metrics, projectList, dates[1], true);
         
			//get distributions
			ProjectInfo projectInfo;
			int nprojects = projectList.size();
			String projectCodes[] = new String[nprojects];
			long projectIDs[] = new long[nprojects];
			for (int k = 0; k < nprojects; k++) {
				projectInfo = (ProjectInfo) projectList.elementAt(k);
				projectCodes[k] = projectInfo.getProjectCode();
				projectIDs[k] = projectInfo.getProjectId();
			}
			NormPlanInfo prevNormPlanInfo = Norms.getNormPlan(workUnitID, lifecycleID, dates[1]);
			//we need to get values for actual period in case the plan already exists
			java.sql.Date[] dates2 = PCB.getDatesFromPeriod(period, year);
			NormPlanInfo normPlanInfo = Norms.getNormPlan(workUnitID, lifecycleID, dates2[1]);
			//merge all the info into NormPlanInfo
			normPlanInfo.year = year;
			normPlanInfo.term = period;
			Vector newRows = new Vector();
			NormPlanInfo.Row row, rowTemp;
			for (int k = 0; k < pcbMetrics.size(); k++) {
				PCBMetricInfo metricInfo = (PCBMetricInfo) pcbMetrics.elementAt(k);
				row = normPlanInfo.new Row();
				row.metricName = metricInfo.name;
				row.metricID = metricInfo.metricConstant;
				row.strMetricID = metricInfo.metricID;
				row.metricUnit = metricInfo.unit;
				row.isMetricGroup = metricInfo.isMetricGroup;
				row.prevCalcLCL = metricInfo.LCL;
				row.prevCalcUCL = metricInfo.UCL;
				row.prevAverage = metricInfo.actualAvg;
				for (int i = 0; i < normPlanInfo.rows.size(); i++) {
					rowTemp = (NormPlanInfo.Row) normPlanInfo.rows.elementAt(i);
					if (rowTemp.metricID == row.metricID) {
						row.LCL = rowTemp.LCL;
						row.UCL = rowTemp.UCL;
						row.norm = rowTemp.norm;
						break;
					}
				}
				for (int j = 0; j < prevNormPlanInfo.rows.size(); j++) {
					rowTemp = (NormPlanInfo.Row) prevNormPlanInfo.rows.elementAt(j);
					if (row.metricID == rowTemp.metricID) {
						row.prevLCL = rowTemp.LCL;
						row.prevUCL = rowTemp.UCL;
						row.prevNorm = rowTemp.norm;
						break;
					}
				}
				newRows.add(row);
			}
			normPlanInfo.rows = newRows;
			session.setAttribute("normPlan", normPlanInfo);
			
//get Norm of All Operation Group
			Vector prevAllOperationGroup = Norms.getNormOfAllOperationGroup(lifecycleID, dates[1]);
			session.setAttribute("prevAllOperationGroup", prevAllOperationGroup);
			Vector allOperationGroup = Norms.getNormOfAllOperationGroup(lifecycleID, dates2[1]);
			session.setAttribute("allOperationGroup", allOperationGroup);
			
			Fms1Servlet.callPage("Group/normPlan.jsp?error=" + error,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void saveNormTable(final HttpServletRequest request, final HttpServletResponse response) {
		String error = saveNorms(request,response);
		getPlannedNorms(request, response, error);
	}
	private static final String saveNorms(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			String[] lcls = request.getParameterValues("lcl");
			String[] ucls = request.getParameterValues("ucl");
			String[] norms = request.getParameterValues("norm");
			String[] glcls = request.getParameterValues("glcl");
			String[] gucls = request.getParameterValues("gucl");
			String[] gaverages = request.getParameterValues("gaverage");
			String[] wp_lcls = request.getParameterValues("wp_lcl");
			String[] wp_ucls = request.getParameterValues("wp_ucl");
			String[] wp_norms = request.getParameterValues("wp_norm");
			String effProc = "4.6.";
			String effStage = "4.9.";
			String effStageold = "4.7.";
			String defOri = "5.8.";
			String defQCAct="5.9.";
			String defQCActStg="5.10.";
			String defWorkPro="5.11.";
			String defEffWorkPro="5.12.";
						
			NormPlanInfo normPlanInfo = (NormPlanInfo) session.getAttribute("normPlan");
			
			NormPlanInfo.Row row;
			int k = 0;
			int n=0;
			for (int i = 0; i < normPlanInfo.rows.size(); i++) {
				row = (NormPlanInfo.Row) normPlanInfo.rows.elementAt(i);
				
				if (row.strMetricID.startsWith(effStageold))
				continue;
				if ((row.strMetricID.startsWith(effProc))
					|| (row.strMetricID.startsWith(effStage))
					|| (row.strMetricID.startsWith(defOri))
                    || (row.strMetricID.startsWith(defQCAct))
                    || (row.strMetricID.startsWith(defQCActStg))) {
					row.norm =  CommonTools.parseDouble(request.getParameter("m"+row.strMetricID));
					continue;
				}
				if (!row.isMetricGroup) {
					if (row.strMetricID.startsWith(defWorkPro)|| row.strMetricID.startsWith(defEffWorkPro)){					
						row.LCL = CommonTools.parseDouble(wp_lcls[n]);
						row.UCL = CommonTools.parseDouble(wp_ucls[n]);
						row.norm = CommonTools.parseDouble(wp_norms[n]);
						n++;					
					}else {
						row.LCL = CommonTools.parseDouble(lcls[k]);
						row.UCL = CommonTools.parseDouble(ucls[k]);
						row.norm = CommonTools.parseDouble(norms[k]);
						k++;
					}
				}
			}

//
			Vector allOperationGroup = (Vector)session.getAttribute("allOperationGroup");
			CSSNormInfo cssNormInfo;
			if(request.getParameterValues("glcl")!=null && request.getParameterValues("gucl")!=null && request.getParameterValues("gaverage")!=null)
				for (int j = 0; j < allOperationGroup.size(); j++) {
					cssNormInfo = (CSSNormInfo)allOperationGroup.elementAt(j);
					cssNormInfo.lcl = CommonTools.parseDouble(glcls[j]);
					cssNormInfo.average = CommonTools.parseDouble(gaverages[j]);
					cssNormInfo.ucl = CommonTools.parseDouble(gucls[j]);
				}
			if(Norms.updateNormPlan(normPlanInfo)){
				if(Norms.updateCSSNorm(allOperationGroup, normPlanInfo.normPlanID)){
					return "Norms updated successfully";
				}else{
					return "Error occured during update";
				}
			}else{
				return "Error occured during update";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Error occured during update";
		}
	}
	public final static void setMetric(final HttpServletRequest request, final HttpServletResponse response) {
		final HttpSession session = request.getSession();
		NormPlanInfo normPlanInfo=(NormPlanInfo)session.getAttribute("normPlan");
		int id =Integer.parseInt(request.getParameter("id"));
		NormPlanInfo.Row row=(NormPlanInfo.Row)normPlanInfo.rows.elementAt(id);
		double val =CommonTools.parseDouble(request.getParameter("metricVal"));
		row.actualValue=val;
		Metrics.updateMetricValue("PQA",row.metricID,val,normPlanInfo.startDate);
		Fms1Servlet.callPage("closeMe.html",request,response);
	}
}
