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
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fms1.infoclass.*;
import com.fms1.tools.CommonTools;
import com.fms1.web.Constants;
import com.fms1.web.Fms1Servlet;
/**
 * Tools pages
 *
 */
public final class ToolCaller {
	public static final void doGetToolList(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			final Vector vtTool = Project.getToolList(prjID);			
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			session.setAttribute("toolVector", vtTool);			
			Fms1Servlet.callPage("tool.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doPrepareAddTool(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			final ProjectDateInfo prjDateInfo = Project.getProjectDate(prjID);
			session.setAttribute("prjDateInfo", prjDateInfo);
			
			final Vector stageVt = Schedule.getStageList(prjID);
			session.setAttribute("stageVector", stageVt);			
			Fms1Servlet.callPage("toolAdd.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void doGetTool(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String strToolID = request.getParameter("toolID");			
			if (strToolID == null) return;
			
			final String prjIDstr = (String) session.getAttribute("projectID");
			final long prjID = Long.parseLong(prjIDstr);
			final int trainID = Integer.parseInt(strToolID);
			final ToolInfo toolInfo = Project.getTool(trainID);
			
			session.setAttribute("toolInfo", toolInfo);
			final Vector stageVt = Schedule.getStageList(prjID);
			session.setAttribute("stageVector", stageVt);	
			
			Fms1Servlet.callPage("toolDetails.jsp",request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doAddTool(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final ToolInfo toolInfo = new ToolInfo();
			toolInfo.prjID = Integer.parseInt((String) session.getAttribute("projectID"));
			toolInfo.toolID = 0;
			toolInfo.name =  request.getParameter("txtName").trim();
			toolInfo.purpose = request.getParameter("txtPurpose").trim();
			toolInfo.source = request.getParameter("txtSource").trim();
			toolInfo.description = request.getParameter("txtDescription").trim();
			toolInfo.status = request.getParameter("cmbStatus").trim();
			toolInfo.expected_available_stage = request.getParameter("ex_avail_stage");			
			toolInfo.note = request.getParameter("txtNote").trim();

			final String tool_type = request.getParameter("tool_type").trim();
			if (tool_type != null) {
				toolInfo.tool_type = Long.parseLong(tool_type);
			}

			if (Project.addTool(toolInfo)) {
				doGetToolList(request, response);
				return;
			}
			ToolInfo oldInfo=(ToolInfo)session.getAttribute("toolInfo");
			ProjectPlanCaller.addChangeAuto(toolInfo.prjID,Constants.ACTION_ADD,Constants.PL_TOOL,"Infrastructures list>Item"+oldInfo.name,null,null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doUppdateTool(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String strToolID = request.getParameter("txtToolID").trim();
			final String name = request.getParameter("txtName").trim();
			final String purpose = request.getParameter("txtPurpose").trim();
			final String source = request.getParameter("txtSource").trim();
			final String des = request.getParameter("txtDescription").trim();
			final String status = request.getParameter("cmbStatus").trim();
			final String strExStage = request.getParameter("ex_avail_stage").trim();
			final String strAcStage = request.getParameter("ac_avail_stage").trim();
			final String note = request.getParameter("txtNote").trim();
			final String tool_type = request.getParameter("tool_type").trim();

			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
			final ToolInfo toolInfo = new ToolInfo();
			toolInfo.prjID = Integer.parseInt(prjID);
			toolInfo.toolID = Integer.parseInt(strToolID);
			toolInfo.name = name;
			toolInfo.purpose = purpose;
			toolInfo.source = source;
			toolInfo.description = des;
			toolInfo.status = status;
			if (strExStage.compareTo("") == 0) {
				toolInfo.expected_available_stage = null;
			}
			else {
				toolInfo.expected_available_stage = strExStage;
			}
			if (strAcStage.compareTo("") == 0) {
				toolInfo.actual_available_stage = null;
			}
			else {
				toolInfo.actual_available_stage = strAcStage;
			}
			if (tool_type != null) {
				toolInfo.tool_type = Long.parseLong(tool_type);
			}
			toolInfo.note = note;
			if (Project.updateTool(toolInfo) == true) {
				doGetToolList(request, response);

				ToolInfo oldInfo=(ToolInfo)session.getAttribute("toolInfo");
				String oldValue,newValue;
				//Item
				oldValue = oldInfo.name;
				newValue = toolInfo.name;
				if (!newValue.equalsIgnoreCase(oldValue))
				{
					ProjectPlanCaller.addChangeAuto(Long.parseLong(prjID),Constants.ACTION_UPDATE,Constants.PL_TOOL,"Tools and infrastructures list>Item",newValue,oldValue);
				}
				//Purpose
				oldValue = oldInfo.purpose;
				newValue = toolInfo.purpose;
				if (!newValue.equalsIgnoreCase(oldValue))
				{
					ProjectPlanCaller.addChangeAuto(Long.parseLong(prjID),Constants.ACTION_UPDATE,Constants.PL_TOOL,"Tools and infrastructures list>Perpose",newValue,oldValue);
				}
				//Due date
				oldValue = CommonTools.dateFormat(oldInfo.dueD);
				newValue = CommonTools.dateFormat(toolInfo.dueD);
				if (!newValue.equalsIgnoreCase(oldValue))
				{
					ProjectPlanCaller.addChangeAuto(Long.parseLong(prjID),Constants.ACTION_UPDATE,Constants.PL_TOOL,"Tools and infrastructures list>Due date",newValue,oldValue);
				}
				return;
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final void doDeleteTool(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			final String prjID = (String) session.getAttribute("projectID");
			final String trainIDstr = request.getParameter("txtToolID");
			final int trainID = Integer.parseInt(trainIDstr);
			if (Project.deleteTool(trainID) == true) {
				doGetToolList(request, response);
				ToolInfo oldInfo=(ToolInfo)session.getAttribute("toolInfo");
				ProjectPlanCaller.addChangeAuto(Long.parseLong(prjID),Constants.ACTION_DELETE,Constants.PL_TOOL,"Tools and infrastructures list>Item>"+oldInfo.name,null,null);
			}
			else {
				System.err.println("Can't delete the tool");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
