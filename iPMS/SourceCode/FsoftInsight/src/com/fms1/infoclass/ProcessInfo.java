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
 
 package com.fms1.infoclass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import com.fms1.tools.ConvertString;
import com.fms1.web.ServerHelper;
public final class ProcessInfo {
	//below are the process that we track effort for,
	// their value is mapped with ID the process table
	public static final int REQUIREMENT = 2;
	public static final int DESIGN = 3;
	public static final int CODING = 4;
	public static final int DEPLOYMENT = 5;
	public static final int CUSTOMER_SUPPORT = 6;
	public static final int TEST = 7;
	public static final int CM = 8;
	public static final int QUALITY_CONTROL = 12;
    public static final int INTERNAL_AUDIT = 13;
	public static final int TRAINING = 17;
	//Important in insight (unlike other tools)"Other" means all non software process
	public static final int OTHER = 26;
	public static final int PROJECT_PLANNING = 666;
	public static final int PROJECT_MONITORING = 667;
	public static final int PROJECT_MANAGEMENT = 9;
	//NON tracked PROCESSES
	public static final int QUALITY_PLANNING = 10;
	public static final int PREVENTION = 28;
	public static final int DOCUMENT_CONTROL = 18;
	public static final int MANAGEMENT_REVIEW = 15;
	//	fake processid used for tailoring and deviation
	public static final int FSOFT_SLC = 1500;
    public static final int GENERAL = 2000;
	public static final int[] engineeringProcesses = { REQUIREMENT, DESIGN, CODING, DEPLOYMENT, CUSTOMER_SUPPORT, TEST, CM };
	public static final int[] preventionProcesses = { PREVENTION, TRAINING };
	//Carefull, must map with MetricDescInfo.trackedEffortProcessId
	public static final int[] trackedProcessId =
		{
			REQUIREMENT,
			DESIGN,
			CODING,
			DEPLOYMENT,
			CUSTOMER_SUPPORT,
			TEST,
			CM,
			PROJECT_PLANNING,
			PROJECT_MONITORING,
			QUALITY_CONTROL,
			TRAINING,
			OTHER };
	// must map with the arrays in requirementInfo used for RCR planning and completion metrics array in metricInfo
	public static final int[] RCRProcesses = { REQUIREMENT, DESIGN, CODING, TEST, DEPLOYMENT, CUSTOMER_SUPPORT };

	//	important : in the same order as in the metric des table  used for RCR NORMS
	public static final int[] trackedProcessIdForRCR =
		{
			ProcessInfo.REQUIREMENT,
			ProcessInfo.DESIGN,
			ProcessInfo.CODING,
			ProcessInfo.DEPLOYMENT,
			ProcessInfo.CUSTOMER_SUPPORT,
			ProcessInfo.TEST,
			ProcessInfo.CM,
			ProcessInfo.QUALITY_CONTROL,
			ProcessInfo.PROJECT_PLANNING,
			ProcessInfo.PROJECT_MONITORING,
			ProcessInfo.TRAINING,
			ProcessInfo.OTHER };
	public static final Vector projectProcessList = getProjectProcessList();
	public static final Vector processList = getProcessList();
	public int processId;
	public String name;
	public int metricConstant; //the norm that might be linked to the process
	public ProcessInfo() {
		processId = 0;
		name = "";
	}
	/**
	 * returns  the metric linked with the process
	 */
	public static final int getMetric(int processID) {
		for (int i = 0; i < trackedProcessId.length; i++) {
			if (trackedProcessId[i] == processID)
				return MetricDescInfo.trackedEffortProcessId[i];
		}
		return -1;
	}
	/**
	 * returns  the process linked with the metric 
	 */
	public static final int getMetricMapping(int metricID) {
		for (int i = 0; i < MetricDescInfo.trackedEffortProcessId.length; i++) {
			if (MetricDescInfo.trackedEffortProcessId[i] == metricID)
				return trackedProcessId[i];
		}
		return -1;
	}
	/**
	 * @return Vector of ProcessInfo
	 */
	static final Vector getProjectProcessList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		final Vector list = new Vector();
		String idConstraint = null;
		try {
			idConstraint = ConvertString.arrayToString(ProcessInfo.trackedProcessId, ",");
			sql = "SELECT NAME,PROCESS_ID FROM PROCESS  WHERE PROCESS_ID IN(" + idConstraint + ") ORDER BY PROCESS_ID";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			ProcessInfo info;
			while (rs.next()) {
				info = new ProcessInfo();
				info.processId = rs.getInt("PROCESS_ID");
				info.name = rs.getString("NAME");
				info.metricConstant = getMetric(info.processId);
				list.add(info);
			}
			info = new ProcessInfo();
			info.processId = ProcessInfo.PROJECT_PLANNING;
			info.name = "Project planning";
			info.metricConstant = getMetric(info.processId);
			list.add(info);
			info = new ProcessInfo();
			info.processId = ProcessInfo.PROJECT_MONITORING;
			info.name = "Project monitoring";
			info.metricConstant = getMetric(info.processId);
			list.add(info);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("idConstraint" + idConstraint);
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return list;
		}
	}
	/**
	 * @return Vector of ProcessInfo
	 */
	static final Vector getProcessList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		final Vector list = new Vector();
		String idConstraint = null;
		try {
			sql = "SELECT NAME,PROCESS_ID FROM PROCESS  ORDER BY NAME";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			ProcessInfo info;
			while (rs.next()) {
				info = new ProcessInfo();
				info.processId = rs.getInt("PROCESS_ID");
				info.name = rs.getString("NAME");
				list.add(info);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return list;
		}
	}
	
	/**
	 * used in one jsp
	 */
    public static final String getProcessName(int processID) {
        ProcessInfo procInfo;
        if (processID == PROJECT_MONITORING)
            return "Project monitoring";
        if (processID == PROJECT_PLANNING)
            return "Project planning";
        for (int i = 0; i < processList.size(); i++) {
            procInfo = (ProcessInfo)processList.elementAt(i);
            if (procInfo.processId == processID)
                return procInfo.name;
        }
        return "N/A";
    }
}
