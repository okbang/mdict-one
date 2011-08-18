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
 
 package  com.fms1.common.group;

import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fms1.common.Project;
import com.fms1.common.WorkUnit;
import com.fms1.tools.CommonTools;
import com.fms1.tools.ReportMonth;
import com.fms1.infoclass.group.HumanResourceInfo;
import com.fms1.web.Constants;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.StringConstants;

/**
 *
 * @author Nguyen Van Hieu
 * @version 1.0 20/Oct/2007
 * This class will process all actions involve Human resource
 * 	+ Calendar Effort
 *  + Resource allocation 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class HumanResourceCaller {
	/**
	 * get all information about Human resource follow user Criteria
	 * @param request
	 * @param response
	 */
	public static void doGetHumanResourceList(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession();
			Vector vtResourceInfo = new Vector();
			String iPage = request.getParameter("iPage");
			if (iPage != null || request.getParameter("iCombo") != null){
				Fms1Servlet.callPage("Group/ResourceAllocation.jsp?iPage=" + iPage, request, response);
			}
			else {
				// get all information on the ResourceAllocation.jsp
				HumanResourceInfo resourceInfo = getHumanResourceInfoBean(request);
				Vector projectList = Project.getProjectList(resourceInfo.getUserGroup(), resourceInfo.getUserBy());
				if (projectList.size() > 0){
					vtResourceInfo = HumanResource.getResourceAllocationList(resourceInfo);
				}
				session.setAttribute("hrProjectList", projectList);
				session.setAttribute("ResourceBean", resourceInfo);
				session.setAttribute("ResourceInfoList", vtResourceInfo);
				Fms1Servlet.callPage("Group/ResourceAllocation.jsp?iPage=1", request, response);
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * To export Resource allocation
	 * @param request
	 * @param response
	 * @param servlet
	 */
	public static void doHumanResourceAllocationExport(
			HttpServletRequest request,
			HttpServletResponse response,
			Fms1Servlet servlet){
		try{
			HumanResourceInfo resourceInfo = getHumanResourceInfoBean(request);
			if (HumanResource.doExportResourceAllocationList(resourceInfo, servlet)) {
				response.setContentType("application/download");
				response.setHeader("Content-Disposition","attachment;filename=\"resourceAllocation.xls\"");
				byte[] fileData = CommonTools.getFileData(resourceInfo.getFileName(), Constants.SIZE_OF_FILE);
				ServletOutputStream sos = response.getOutputStream();
				sos.write(fileData);
				sos.close();
				CommonTools.doDeleteFile(resourceInfo.getFileName());
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Get all information from group/CalendarEffort.jsp
	 * @param request
	 * @return
	 */
	public static HumanResourceInfo getHumanResourceInfoBean(HttpServletRequest request){
		HumanResourceInfo humanResourceInfo = new HumanResourceInfo();
		try{
			HttpSession session = request.getSession();
			long projectId = 0;
			int status = -1;
			int userBy = 2;
			String fromDate = "";
			String toDate = "";
			String strGroup = (String)session.getAttribute("workUnitName");
			if (request.getParameter("cboProject") != null){
				projectId = Long.parseLong((String)request.getParameter("cboProject"));
			}
			if (request.getParameter("cboStatus") != null){
				status = Integer.parseInt(request.getParameter("cboStatus"));
			}
			if (request.getParameter("cboUserBy") != null){
				userBy = Integer.parseInt(request.getParameter("cboUserBy"));
			}
			
			if (request.getParameter("FromDate") != null){
				fromDate = request.getParameter("FromDate");
			}
			else {
				ReportMonth report = new ReportMonth();
				fromDate = CommonTools.dateFormat(report.getFirstDayOfMonth());
			}
			
			if (request.getParameter("ToDate") != null){
				toDate = request.getParameter("ToDate");
			}
			else {
				ReportMonth report = new ReportMonth();
				toDate = CommonTools.dateFormat(report.getLastDayOfMonth());
			}

			humanResourceInfo.setUserGroup(strGroup);
			humanResourceInfo.setProjectGroup(strGroup);
			humanResourceInfo.setProjectId(projectId);
			humanResourceInfo.setStatus(status);
			humanResourceInfo.setUserBy(userBy);
			if (fromDate != null && !"".equals(fromDate)){
				humanResourceInfo.setFromDate(fromDate);
			}
			if (toDate != null && !"".equals(toDate)){
				humanResourceInfo.setToDate(toDate);
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally{
			return humanResourceInfo;
		}
		
	}
	/**
	 * get all required information  from client(group/ResourceAllocation.jsp)
	 * @param request
	 * @return
	 */
	public static HumanResourceInfo getCalendarEffortInfoBean(HttpServletRequest request){
		HumanResourceInfo humanResourceInfo = new HumanResourceInfo();
		try{
			HttpSession session = request.getSession();
			long projectId = 0;
			int status = -1;
			int projectType = -1;
			String fromDate = "";
			String toDate = "";
			if (request.getParameter("groupName") != null){
				session.setAttribute("workUnitName", request.getParameter("groupName"));
				session.removeAttribute("GroupName");
			}
			String strGroup = (String)session.getAttribute("workUnitName");
			if (request.getParameter("cboProject") != null){
				session.setAttribute("cboProject", request.getParameter("cboProject"));
			}
			if (session.getAttribute("cboProject") != null){
				projectId = Long.parseLong((String)session.getAttribute("cboProject"));
			}
			if (request.getParameter("cboStatus") != null){
				session.setAttribute("cboStatus", request.getParameter("cboStatus"));
			}
			if (session.getAttribute("cboStatus") != null){
				status = Integer.parseInt((String)session.getAttribute("cboStatus"));
			}
			if (request.getParameter("cboProjectType") != null){
				session.setAttribute("cboProjectType", request.getParameter("cboProjectType"));
			}
			if (session.getAttribute("cboProjectType") != null){
				projectType = Integer.parseInt((String)session.getAttribute("cboProjectType"));
			}
			if (request.getParameter("FromDate") != null){
				session.setAttribute("ResourceFromDate", request.getParameter("FromDate"));
				
			}
			if (session.getAttribute("ResourceFromDate") != null){
				fromDate = (String)session.getAttribute("ResourceFromDate") ;
			}
			else {
				ReportMonth report = new ReportMonth();
				fromDate = CommonTools.dateFormat(report.getFirstDayOfMonth());
			}
			
			if (request.getParameter("ToDate") != null){
				session.setAttribute("ResourceToDate", request.getParameter("ToDate"));
			}
			if (session.getAttribute("ResourceToDate") != null){
				toDate = (String)session.getAttribute("ResourceToDate");
			}
			else {
				ReportMonth report = new ReportMonth();
				toDate = CommonTools.dateFormat(report.getLastDayOfMonth());
			}

			humanResourceInfo.setUserGroup(strGroup);
			humanResourceInfo.setProjectGroup(strGroup);
			humanResourceInfo.setProjectId(projectId);
			humanResourceInfo.setStatus(status);
			humanResourceInfo.setProjectType(projectType);
			if (fromDate != null && !"".equals(fromDate)){
				humanResourceInfo.setFromDate(fromDate);
			}
			if (toDate != null && !"".equals(toDate)){
				humanResourceInfo.setToDate(toDate);
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally{
			return humanResourceInfo;
		}
		
	}
	/**
	 * excute get and calculate calendar effort follow user Criteria
	 * @param request
	 * @param response
	 */
	public static void doGetCalendarEffortList(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession();
			String iPage = request.getParameter("iPage");
			if (iPage != null || request.getParameter("iCombo") != null){
				Fms1Servlet.callPage("Group/calendarEffort.jsp?iPage=" + iPage, request, response);
			}
			else {
				HumanResourceInfo resourceInfo = getCalendarEffortInfoBean(request);
				Vector calendarEffort = new Vector();
				Vector projectList = Project.getProjectList(resourceInfo.getUserGroup(), Constants.HR_USER_BY_PROJECT);
				if (projectList.size() > 0){
					calendarEffort = HumanResource.getCalendarEffortList(resourceInfo);
				}
				session.setAttribute("CalendarEffortBean", resourceInfo);
				session.setAttribute("GroupProjectList", projectList);
				session.setAttribute("GroupCalendarEffort", calendarEffort);
				Fms1Servlet.callPage("Group/calendarEffort.jsp?iPage=1", request, response);
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
}