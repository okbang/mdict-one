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

import java.sql.*;
import java.util.Vector;
import java.util.Iterator;
import java.util.Hashtable;

import com.fms1.web.*;
import com.fms1.tools.*;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.GroupInfo;

/**
 * Project plan and work order logic which doesn't fit in other objects.
 *
 */
public final class Project {
	private static int ISCONSTRAINT = 1;
	private static int ISASSUMPTION = 0;	
    
	/**
	 * Get performance metrics (used in WO, Weekly, Milestone, Post-mortem,...)
	 * up to current date
	 * @param prjID
	 * @return
	 */
	public static final Vector getCustomerMetric(final long prjID)
		{
			final Vector resultVector = new Vector();
			Connection conn = null;
			PreparedStatement stm = null;
			String sql = null;
			ResultSet rs = null;
			try {
				conn = ServerHelper.instance().getConnection();
				 sql = "SELECT * FROM CUS_METRICS  WHERE PROJECT_ID = ?"+
						" ORDER BY CODE";
				stm = conn.prepareStatement(sql);
				stm.setLong(1,prjID);
			
				rs = stm.executeQuery();
				while (rs.next()) {
					final WOCustomeMetricInfo cmInfo = new WOCustomeMetricInfo();
					cmInfo.cusMetricID = rs.getLong("CODE");
					cmInfo.name = rs.getString("NAME");
					cmInfo.unit = rs.getString("UNIT");
					cmInfo.plannedValue = Db.getDouble(rs, "PLANNED_VALUE");
					cmInfo.UCL = Db.getDouble(rs, "UCL");
					cmInfo.LCL = Db.getDouble(rs, "LCL");
					cmInfo.mileStoneID= rs.getLong("milestone_ID");
					if (cmInfo.plannedValue != 0)
						cmInfo.deviation = CommonTools.formatDouble((cmInfo.actualValue - cmInfo.plannedValue) * 100.0d / cmInfo.plannedValue);
					else
						cmInfo.deviation = "N/A";
					cmInfo.note = rs.getString("NOTE");
					cmInfo.projectID = rs.getInt("PROJECT_ID");
					cmInfo.causal = rs.getString("CAUSAL");
					resultVector.addElement(cmInfo);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn, stm, rs);
				return resultVector;
			}
								
		}
	public static final Vector getCustomerMetric(final long prjID, final long mileStoneID)
	{
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			/*
			 * 
			 sql = "SELECT * FROM CUS_METRICS  WHERE PROJECT_ID = ? AND MILESTONE_ID=?  " +
				" JOIN CUS_POINT cp" +
				"ORDER BY CODE";
			*/
			sql=	"SELECT a.code, a.actual_value, b.name,b.unit,b.planned_value, b.note, b.project_id, b.lcl,b.ucl,"
			        +"b.causal,a.milestone_ID"
			  		+" FROM cus_point a, cus_metrics b"
			  		+" where a.code = b.code"
					+" and a.milestone_id =?";		
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, mileStoneID);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				final WOCustomeMetricInfo cmInfo = new WOCustomeMetricInfo();
				cmInfo.cusMetricID = rs.getLong("CODE");
				cmInfo.name = rs.getString("NAME");
				cmInfo.unit = rs.getString("UNIT");
				cmInfo.plannedValue = Db.getDouble(rs, "PLANNED_VALUE");
				cmInfo.UCL = Db.getDouble(rs, "UCL");
				cmInfo.LCL = Db.getDouble(rs, "LCL");
				cmInfo.actualValue = Db.getDouble(rs, "ACTUAL_VALUE");
				cmInfo.mileStoneID= rs.getLong("milestone_ID");
				if (cmInfo.plannedValue != 0)
					cmInfo.deviation = CommonTools.formatDouble((cmInfo.actualValue - cmInfo.plannedValue) * 100.0d / cmInfo.plannedValue);
				else
					cmInfo.deviation = "N/A";
				cmInfo.note = rs.getString("NOTE");
				cmInfo.projectID = rs.getInt("PROJECT_ID");
				cmInfo.causal = rs.getString("CAUSAL");
				resultVector.addElement(cmInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
								
	}
	public static final Vector getPerformanceMetrics(final long prjID) {
		return getPerformanceMetrics(prjID, null);
	}
	/**
	 * Get performance metrics (used in WO, Weekly, Milestone, Post-mortem,...)
	 * up to specified date [uptoDate]
	 * @param prjID
	 * @return
	 */
	public static final Vector getPerformanceMetrics(final long prjID, java.util.Date uptoDate) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		// 01-Feb-07: Replaced by ProjectInfo.getDurrationMetric() function
		double endDateDeviation = Double.NaN;
		
		double startDateDeviation = Double.NaN;
		ResultSet rs = null;
		Vector metricInfoList = new Vector();
		try {
			ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			/**
			 * start date
			 */
			NormInfo schedDeviationNorm = Norms.getNorm(prjID, MetricDescInfo.PROJECT_SCHEDULE_DEVIATION);
			MetricInfo startDateInfo = new MetricInfo();
			startDateInfo.name = "Start date";
			startDateInfo.unit = "DD-MMM-YY";
			startDateInfo.actualValue = (projectInfo.getStartDate() == null) ? Double.NaN : (double)projectInfo.getStartDate().getTime();
			startDateInfo.plannedValue = (projectInfo.getPlanStartDate() == null) ? Double.NaN : (double)projectInfo.getPlanStartDate().getTime();
			startDateInfo.deviation = startDateDeviation;
			startDateInfo.lcl = schedDeviationNorm.lcl;
			startDateInfo.ucl = schedDeviationNorm.ucl;
			startDateInfo.colorType = Color.HIGHBAD;
			/**
			 * Planned end date
			 */
			MetricInfo pEndDateInfo = new MetricInfo();
			pEndDateInfo.name = "End date";
			pEndDateInfo.unit = "DD-MMM-YY";
			pEndDateInfo.actualValue = (projectInfo.getActualFinishDate() == null) ? Double.NaN : (double)projectInfo.getActualFinishDate().getTime();
			if (projectInfo.getBaseFinishDate() != null)
				pEndDateInfo.plannedValue = (double)projectInfo.getBaseFinishDate().getTime();
			if (projectInfo.getPlanFinishDate() != null)
				pEndDateInfo.rePlannedValue = (double)projectInfo.getPlanFinishDate().getTime();
			pEndDateInfo.deviation = endDateDeviation;
			pEndDateInfo.lcl = schedDeviationNorm.lcl;
			pEndDateInfo.ucl = schedDeviationNorm.ucl;
			pEndDateInfo.colorType = Color.HIGHBAD;
			/**
			 * duration
			 * 01-Feb-07: Replaced by ProjectInfo.getDurrationMetric() function
			 */
			MetricInfo durationInfo = projectInfo.getDurationMetric();
			/**  
			 * Project plan SQL: Maximum team size,
			 *     management effort, quality effort
			 */
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT MAX_TEAM_SIZE,MANAGEMENT_EFFORT, QUALITY_EFFORT" +
				" FROM PROJECT_PLAN WHERE PROJECT_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			double plannedTeam = 0;
			double plannedManEffort = 0;
			double plannedQuaEffort = 0;
			if (rs.next()){
				plannedTeam = Db.getDouble(rs, "MAX_TEAM_SIZE");
				plannedManEffort = Db.getDouble(rs, "MANAGEMENT_EFFORT");
				plannedQuaEffort = Db.getDouble(rs, "QUALITY_EFFORT");
			}
			rs.close();
			stm.close();
			/**
			 * Maximum team size
			 */
			MetricInfo maxTeamInfo = new MetricInfo();
			maxTeamInfo.name = "Maximum team size";
			maxTeamInfo.unit = "Persons";
			maxTeamInfo.actualValue = Assignments.getActualTeamSize(prjID);
			maxTeamInfo.plannedValue = plannedTeam;
			if ((!Double.isNaN(maxTeamInfo.plannedValue)) && (maxTeamInfo.plannedValue != 0) && (!Double.isNaN(maxTeamInfo.actualValue)))
				maxTeamInfo.deviation = (maxTeamInfo.actualValue - maxTeamInfo.plannedValue) * 100d / maxTeamInfo.plannedValue;
			maxTeamInfo.lcl = schedDeviationNorm.lcl;
			maxTeamInfo.ucl = schedDeviationNorm.ucl;
			maxTeamInfo.colorType = Color.HIGHBAD;
			/**
			 * Effort
			 */
			NormInfo effortNorm = Norms.getNorm(prjID, MetricDescInfo.EFFORT_DEVIATION);
			EffortInfo effortInfo = Effort.getEffortInfo(projectInfo);
			MetricInfo effortUsageInfo = new MetricInfo();
			effortUsageInfo.name = "Effort usage";
			effortUsageInfo.unit = "Person.day";
			effortUsageInfo.actualValue = effortInfo.actualEffort;
			effortUsageInfo.plannedValue = effortInfo.plannedEffort;
			effortUsageInfo.rePlannedValue = effortInfo.rePlannedEffort;
			effortUsageInfo.deviation = effortInfo.effortDeviation;
			effortUsageInfo.lcl = effortNorm.lcl;
			effortUsageInfo.ucl = effortNorm.ucl;
			effortUsageInfo.colorType = effortNorm.colorType;
			/**
			 * Billable Effort
			 * Add by HUYNH2
			 */            
			// This is not EFFORT_DEVIATION metric, in oder to use norm,
			// should define BILLABLE_EFFORT metric and store (re)planned value in Metric table
			//NormInfo effortBillableNorm = Norms.getNorm(prjID, MetricDescInfo.EFFORT_DEVIATION);

			// => replaced by billable effort get from Project table 
			//EffortInfo effortBillableInfo = Effort.getBillableEffortInfo(pInfo);
			NormInfo effortBillableNorm = new NormInfo();
			MetricInfo effortBillableUsageInfo = new MetricInfo();
			effortBillableUsageInfo.name = "Billable effort";
			//effortBillableUsageInfo.name = "Effort usage";
			effortBillableUsageInfo.unit = "Person.day";
			effortBillableUsageInfo.actualValue = projectInfo.getActualBillableEffort();
			effortBillableUsageInfo.plannedValue = projectInfo.getBaseBillableEffort();
			effortBillableUsageInfo.rePlannedValue = projectInfo.getPlanBillableEffort();
			effortBillableUsageInfo.deviation = CommonTools.metricDeviation(
				effortBillableUsageInfo.plannedValue,
				effortBillableUsageInfo.rePlannedValue,
				effortBillableUsageInfo.actualValue);
			// Have not defined Billable Effort in Norms so => not calculated
			// Billable Effort => should change the way of calculation for
			// actual value when defined Billable Effort norm
			effortBillableUsageInfo.lcl = effortBillableNorm.lcl;
			effortBillableUsageInfo.ucl = effortBillableNorm.ucl;
			effortBillableUsageInfo.colorType = effortBillableNorm.colorType;

			/**
			 * Actual calendar effort (total effort based on team assignments)
			 * Added by TrungTN
			 */            
			//NormInfo calendarEffortNorm = Norms.getNorm(prjID, MetricDescInfo.CALENDAR_EFFORT);
			NormInfo calendarEffortNorm = new NormInfo();
			MetricInfo calendarEffortInfo = new MetricInfo();
			calendarEffortInfo.name = "Calendar effort";
			calendarEffortInfo.unit = "Person.day";
			calendarEffortInfo.actualValue =
				Assignments.getActualCalendarEffort(projectInfo, uptoDate);
			calendarEffortInfo.plannedValue = projectInfo.getPlanCalendarEffort();
			calendarEffortInfo.rePlannedValue = projectInfo.getReplanCalendarEffort();
			calendarEffortInfo.deviation = CommonTools.metricDeviation(
				calendarEffortInfo.plannedValue,
				calendarEffortInfo.rePlannedValue,
				calendarEffortInfo.actualValue);
			calendarEffortInfo.lcl = calendarEffortNorm.lcl;
			calendarEffortInfo.ucl = calendarEffortNorm.ucl;
			calendarEffortInfo.colorType = calendarEffortNorm.colorType;

			/**
			 * Development effort, management effort, quality effort
			 */
			MetricInfo manEffortInfo = new MetricInfo();
			MetricInfo quaEffortInfo = new MetricInfo();
			MetricInfo devEffortInfo = new MetricInfo();
			/*sql = "SELECT  MANAGEMENT_EFFORT, QUALITY_EFFORT FROM PROJECT_PLAN " + " WHERE PROJECT_ID = ? ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			rs.next();
			if (rs.getString("MANAGEMENT_EFFORT") != null)
				manEffortInfo.plannedValue = rs.getDouble("MANAGEMENT_EFFORT");
			quaEffortInfo.plannedValue = rs.getDouble("QUALITY_EFFORT");
			*/
			manEffortInfo.plannedValue = plannedManEffort;
			quaEffortInfo.plannedValue = plannedQuaEffort;
			devEffortInfo.plannedValue = 100 - (manEffortInfo.plannedValue + quaEffortInfo.plannedValue);
            
			// Dev Effort
			devEffortInfo.name = "&nbsp&nbsp&nbsp Development";
			devEffortInfo.unit = "%";
			devEffortInfo.actualValue = effortInfo.perDevelopementEffort;
			if ((!Double.isNaN(devEffortInfo.plannedValue)) && (devEffortInfo.plannedValue != 0))
				devEffortInfo.deviation =
					(effortInfo.perDevelopementEffort - devEffortInfo.plannedValue) * 100 / devEffortInfo.plannedValue;
			// Management Effort
			manEffortInfo.name = "&nbsp&nbsp&nbsp Management";
			manEffortInfo.unit = "%";
			manEffortInfo.actualValue = effortInfo.perManagementEffort;
			if ((!Double.isNaN(manEffortInfo.plannedValue)) && (manEffortInfo.plannedValue != 0))
				manEffortInfo.deviation = (effortInfo.perManagementEffort - manEffortInfo.plannedValue) * 100 / manEffortInfo.plannedValue;
			// Quality Effort
			quaEffortInfo.name = "&nbsp&nbsp&nbsp Quality";
			quaEffortInfo.unit = "%";
			quaEffortInfo.actualValue = effortInfo.perQualityEffort;
			if ((!Double.isNaN(quaEffortInfo.plannedValue)) && (quaEffortInfo.plannedValue != 0))
				quaEffortInfo.deviation = (effortInfo.perQualityEffort - quaEffortInfo.plannedValue) * 100 / quaEffortInfo.plannedValue;
			//Do not change this order, some functions get the metrics using the index in the vector
			// Should not use fixed order, generalize this list using
			// Example put metricInfoList into a class and create functions to
			// access metrics example PerformanceMetric.getxxx() to return MetricInfo
			metricInfoList.add(startDateInfo);
			metricInfoList.add(pEndDateInfo);
			metricInfoList.add(durationInfo);
			metricInfoList.add(maxTeamInfo);
			metricInfoList.add(effortUsageInfo);
			metricInfoList.add(devEffortInfo);
			metricInfoList.add(manEffortInfo);
			metricInfoList.add(quaEffortInfo);
			metricInfoList.add(effortBillableUsageInfo);
			metricInfoList.add(calendarEffortInfo);
			return metricInfoList;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	/**
	 * Bean class of WO/Metrics/Update page
	 */
	public static final class updateWOPerformanceInfo {
		public java.sql.Date planStartDate;
		public java.sql.Date actualStartDate;
		public java.sql.Date planEndDate;
		public java.sql.Date rePlanEndDate;
		public java.sql.Date actualEndDate;
		public double planTeamSize = Double.NaN;
		public double effortUsage = Double.NaN;
		public double reEffortUsage = Double.NaN;
		// HUYNH2 Add Billable Effort
		public double billableEffortUsage = Double.NaN;
		public double reBillableEffortUsage = Double.NaN;
		public double billableActual = Double.NaN;
		// end
		public double planCalendarEffort = Double.NaN;
		public double replanCalendarEffort = Double.NaN;
		public double devEffort = Double.NaN;
		public double manEffort = Double.NaN;
		public double quaEffort = Double.NaN;
	}
	public static final boolean updateWOPerformanceList(final long prjID, final updateWOPerformanceInfo info) throws SQLException {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			//Start date, planned Finish date, actual finish date
			sql =
				"UPDATE PROJECT SET"
					+ " PLAN_START_DATE = ?,START_DATE = ?"
					+ ",BASE_FINISH_DATE = ?,PLAN_FINISH_DATE = ?,ACTUAL_FINISH_DATE = ?"
					+ ",BASE_EFFORT = ?,PLAN_EFFORT = ?"
					+ ",BASE_BILLABLE_EFFORT = ?,PLAN_BILLABLE_EFFORT = ?,ACTUAL_BILLABLE_EFFORT=?"
					+ " WHERE PROJECT_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setDate(1, info.planStartDate);
			stm.setDate(2, info.actualStartDate);
			stm.setDate(3, info.planEndDate);
			stm.setDate(4, info.rePlanEndDate);
			stm.setDate(5, info.actualEndDate);
			Db.setDouble(stm, 6, info.effortUsage);
			Db.setDouble(stm, 7, info.reEffortUsage);
			Db.setDouble(stm, 8, info.billableEffortUsage);
			Db.setDouble(stm, 9, info.reBillableEffortUsage);
			Db.setDouble(stm, 10, info.billableActual);
			stm.setLong(11, prjID);
			stm.executeUpdate();
			sql = "UPDATE PROJECT_PLAN SET"
				+ " MANAGEMENT_EFFORT = ?, QUALITY_EFFORT = ?, MAX_TEAM_SIZE=?"
				+ ",PLAN_CALENDAR_EFFORT = ?,REPLAN_CALENDAR_EFFORT = ?"
				+ " WHERE PROJECT_ID = ?";
			stm = conn.prepareStatement(sql);
			Db.setDouble(stm, 1, info.manEffort);
			Db.setDouble(stm, 2, info.quaEffort);
			Db.setDouble(stm, 3, info.planTeamSize);
			Db.setDouble(stm, 4, info.planCalendarEffort);
			Db.setDouble(stm, 5, info.replanCalendarEffort);
			stm.setLong(6, prjID);
			stm.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	// created by anhtv08..
	// add Milestone_id parametter to this function...
	// get customerMetrics by projectID and Milestone_ID
	
	public static final Vector getWOCustomeMetricList(final long prjID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM CUS_METRICS  WHERE PROJECT_ID = ?  ORDER BY CODE";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				final WOCustomeMetricInfo cmInfo = new WOCustomeMetricInfo();
				cmInfo.cusMetricID = rs.getLong("CODE");
				cmInfo.name = rs.getString("NAME");
				cmInfo.unit = rs.getString("UNIT");
				cmInfo.plannedValue = Db.getDouble(rs, "PLANNED_VALUE");
				cmInfo.UCL = Db.getDouble(rs, "UCL");
				cmInfo.LCL = Db.getDouble(rs, "LCL");
				cmInfo.actualValue = Db.getDouble(rs, "ACTUAL_VALUE");
				if (cmInfo.plannedValue != 0)
					cmInfo.deviation = CommonTools.formatDouble((cmInfo.actualValue - cmInfo.plannedValue) * 100.0d / cmInfo.plannedValue);
				else
					cmInfo.deviation = "N/A";
				cmInfo.note = rs.getString("NOTE");
				cmInfo.projectID = rs.getInt("PROJECT_ID");
				cmInfo.causal = rs.getString("CAUSAL");
				resultVector.addElement(cmInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
	public static final boolean addCM(final WOCustomeMetricInfo newInfo) throws SQLException {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		boolean result = false;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql =
				"INSERT INTO CUS_METRICS (CODE, NAME, UNIT, PLANNED_VALUE, LCL, UCL, NOTE, PROJECT_ID) "
					+ "VALUES (NVL((SELECT MAX(CODE)+1 FROM CUS_METRICS),1), ?, ?, ?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setString(1, newInfo.name);
			stm.setString(2, newInfo.unit);
			Db.setDouble(stm, 3, newInfo.plannedValue);
			Db.setDouble(stm, 4, newInfo.LCL);
			Db.setDouble(stm, 5, newInfo.UCL);
			stm.setString(6, newInfo.note);
			stm.setInt(7, newInfo.projectID);
			
			if (stm.executeUpdate() > 0) {
				stm.close();
				Vector milestoneList = new Vector();
				milestoneList = Project.getMilestoneListByProject(newInfo.projectID);
				for(int i=0;i<milestoneList.size();i++){
					stm.close();
					MilestoneInfo milestoneInfo = new MilestoneInfo();
					milestoneInfo = (MilestoneInfo)milestoneList.elementAt(i);
					sql =
						"INSERT INTO CUS_POINT (CUS_POINT_ID, MILESTONE_ID, CODE, POINT) "
							+ " VALUES (NVL((SELECT MAX(CUS_POINT_ID)+1 FROM CUS_POINT),1), ?, (SELECT MAX(CODE) FROM CUS_METRICS), null)";
					stm = conn.prepareStatement(sql);
					Db.setDouble(stm, 1, milestoneInfo.getMilestoneId());
					stm.executeUpdate();
				}
				stm.close();
			}
			conn.commit();
			conn.setAutoCommit(true);
			result = true;
		}
		catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
			e.printStackTrace();
			result = false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return result;
		}
	}
	public static final boolean updateCM(final WOCustomeMetricInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE CUS_METRICS SET "
					+ "	NAME 	= ?"
					+ ",UNIT 	= ?"
					+ ",PLANNED_VALUE 	= ?"
					+ ",NOTE	   		= ?"
					+ ",LCL	   		= ?"
					+ ",UCL	   		= ?"
					+ " WHERE CODE = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, newInfo.name);
			stm.setString(2, newInfo.unit);
			Db.setDouble(stm, 3, newInfo.plannedValue);
			
			stm.setString(4, newInfo.note);
			Db.setDouble(stm, 5, newInfo.LCL);
			Db.setDouble(stm, 6, newInfo.UCL);
			stm.setLong(7, newInfo.cusMetricID);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean deleteCM(final long metricID) {
		if(Db.delete(metricID, "CODE", "CUS_POINT") && Db.delete(metricID, "CODE", "CUS_METRICS")){
			return true;
		}else{
			return false;
		}
	}
	public static final Vector getStandardMetricList(final long prjID) {
		java.util.Date date = new java.util.Date();
		return getStandardMetricList(prjID, new Date(date.getTime()));
	}
	public static final Vector getStandardMetricList(final long prjID, Date date) {
		final Vector normVector = new Vector();
		ProjectInfo projectInfo;
		try {
			projectInfo = Project.getProjectInfo(prjID);
			int metricConstant = -1;

			//TODO Why not separate each line by a function call, example:
			// NormInfo normInfo = Norms.getNormDetails(MetricDescInfo.TIMELINESS, projectInfo, date);
			// normVector.add(normInfo);
			// NormInfo normInfo = Norms.getNormDetails(...
			// ...
			for (int k = 0; k < 8; k++) {
				switch (k) {
					case 0 :
						metricConstant = MetricDescInfo.CUSTOMER_SATISFACTION;
						break;
					case 1 :
						metricConstant = MetricDescInfo.LEAKAGE;						
						break;
					case 2 :
						metricConstant = MetricDescInfo.PROCESS_COMPLIANCE;
						break;
					case 3 :
						metricConstant = MetricDescInfo.EFFORT_EFFICIENCY;
						break;
					case 4 :
						metricConstant = MetricDescInfo.CORRECTION_COST;
						break;
					case 5 :
						metricConstant = MetricDescInfo.TIMELINESS;						
						break;
					case 6 :
						metricConstant = MetricDescInfo.REQUIREMENT_COMPLETENESS;
						break;
					case 7 : //This isn't a Standard Metric element, therefore, in a display page, i may take milestoneQO.size()-1	
						metricConstant = MetricDescInfo.OVERDUE_NCsOBs;
						break;
				}
				NormInfo normInfo = Norms.getNormDetails(metricConstant, projectInfo, date);
				
				normVector.add(normInfo);
			}
			return normVector;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 
	 * @param projectInfo
	 * @return
	 */
	public static final boolean doCloseCanCelProject(ProjectInfo projectInfo){
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		try{
			sql = "UPDATE PROJECT SET actual_Finish_date = ?, status = ?,"
                + " description = ?, PROJECT_STATUS_CODE = ?"
				+ " WHERE Project_ID = ?";
            conn = ServerHelper.instance().getConnection();
            preStm = conn.prepareStatement(sql);
			preStm.setDate(1, projectInfo.getActualFinishDate());
			preStm.setInt(2, projectInfo.getStatus());
			preStm.setString(3, projectInfo.getDescription());

            //--------------------------------------------------------------
            // Update for compatible with RMS: Project status code
            //--------------------------------------------------------------
            preStm.setString(4,
                com.fms1.integrate.Project.getProjectStatusCodeById(
                    projectInfo.getStatus()));
            //--------------------------------------------------------------
            // END: Update for compatible with RMS: Project status code
            //--------------------------------------------------------------

			preStm.setLong(5, projectInfo.getProjectId());
			if (preStm.executeUpdate() == 0){
				return false;
			}
			return true;
		}
		catch (SQLException ex){
			ex.printStackTrace();
			return false;
		}
		finally{
			ServerHelper.closeConnection(conn, preStm, null);
		}
	}
	public static final void reOpenProject(final long lProjectID){
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		ResultSet rs = null;
		long lMilestoneID = 0;
		try{
            sql = "UPDATE PROJECT SET actual_Finish_date = null, status = 0,"
                + " PROJECT_STATUS_CODE = ? "
                + " WHERE Project_ID = ?";
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			preStm = conn.prepareStatement(sql);
			preStm.setString(1,"ON_GOING");	
			preStm.setLong(2, lProjectID);
			preStm.executeUpdate();

			sql = "SELECT MILESTONE_ID, NVL(BASE_FINISH_DATE, PLAN_FINISH_DATE) PLANNED_FINISH_DATE" +
				  " FROM MILESTONE" +
				  " WHERE PROJECT_ID = ?" +
				  " ORDER BY PLANNED_FINISH_DATE ASC";
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, lProjectID);
			rs = preStm.executeQuery();

			while (rs.next()){
				lMilestoneID = rs.getLong("MILESTONE_ID");
			}
			if (lMilestoneID != 0){
				sql = "UPDATE MILESTONE SET ACTUAL_FINISH_DATE = null, COMPLETE = 0" +
					  " WHERE MILESTONE_ID = ? AND PROJECT_ID = ?";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, lMilestoneID);
				preStm.setLong(2, lProjectID);
				preStm.executeUpdate();
			}
			
			conn.commit();
			conn.setAutoCommit(true);
		}
		catch (SQLException ex){
			ex.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		finally{
			ServerHelper.closeConnection(conn, preStm, null);
		}
	}
    
	public static final boolean updateGeneralInfo(final long prjID, final ProjectInfo info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
            // Temporary removed due to issue of Db.setCLOB, it is not worked.
            //conn = Db.getOracleConn();
            conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql =
				"SELECT TABLEID FROM workunit WHERE UPPER(WORKUNITNAME) = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, info.getProjectCode().toUpperCase());
			rs = stm.executeQuery();
			if (rs.next()){
				long lProjectID = rs.getLong("TABLEID");
				// This project code have been existed!
				if (lProjectID != prjID){
					rs.close();
					return false;
				}
			}
            rs.close();
            stm.close();
			sql = "UPDATE PROJECT"
                + " SET TYPE = ?, CODE = ?, NAME = ?, GROUP_NAME = ?,"
                + " DIVISION_NAME = ?, CUSTOMER = ?, CUSTOMER_2ND = ?,"
                + " CATEGORY = ?, RANK = ?, LEADER = ?, PROJECT_LEVEL = ?,"
                + " status = ?,"
                + " UNIT_ID = ?, PROJECT_STATUS_CODE = ? , TYPE_CUSTOMER = ? , TYPE_CUSTOMER_2ND = ? "
                + " WHERE PROJECT_ID = ?";

			stm = conn.prepareStatement(sql);
			stm.setString(1, info.getProjectType());
			stm.setString(2, info.getProjectCode());
			stm.setString(3, info.getProjectName());
			stm.setString(4, info.getGroupName());
			stm.setString(5, info.getDivisionName());
			stm.setString(6, info.getCustomer());
			stm.setString(7, info.getSecondCustomer());
			stm.setString(8, info.getLifecycle().trim());
			stm.setString(9, info.getProjectRank().toUpperCase());
			stm.setString(10, info.getLeader().toUpperCase());
			stm.setString(11, info.getProjectLevel());
			stm.setInt(12, info.getStatus());

            //--------------------------------------------------------------
            // Update for compatible with RMS: Project status code,
            // Unit Id (please note that Unit Id comes from RMS_UNIT.unit_id
            // , not comes from WorkUnit.WorkUnitId of old FI.)
            //--------------------------------------------------------------
            stm.setInt(13, com.fms1.integrate.Unit.getUnitIdByGroupName(
                    info.getGroupName()));
            stm.setString(14,
                com.fms1.integrate.Project.getProjectStatusCodeById(
                    info.getStatus()));
            //--------------------------------------------------------------
            // END: Update for compatible with RMS: Project status code, Unit Id
            //--------------------------------------------------------------

			stm.setInt(15,info.getTypeCustomer());
			stm.setInt(16,info.getTypeCustomer2());
			stm.setLong(17, prjID);
			stm.executeUpdate();

			// When update project info, must update WorkunitName by project.Code
			WorkUnitInfo wuInfo = new WorkUnitInfo(); 
			wuInfo = WorkUnit.getWorkUnitInfo(info.getGroupName());
			sql = "UPDATE WORKUNIT SET WORKUNITNAME = ?, PARENTWORKUNITID = ? WHERE TABLEID =?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, info.getProjectCode());
			stm.setLong(2, wuInfo.workUnitID);
			stm.setLong(3, prjID);
			stm.executeUpdate();

			sql = "SELECT * FROM PROJECT_PLAN WHERE PROJECT_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			String objString;
			final String contractString;
			final String domainString;
			final String directorString;
			final String apptypeString;
			if (rs.next()) {
				sql =
					"UPDATE PROJECT_PLAN SET CONTRACT_TYPE = ?, DOMAIN = ?, DIRECTOR = ?, APPLICATION_TYPE = ? WHERE PROJECT_ID = ?";
				stm = conn.prepareStatement(sql);
                // Removed due to issue when debug, the Db.setCLOB is not worked!!
                //objString = info.getScopeAndObjective();
                //Db.setCLOB(stm, 1, objString);
                
				contractString = info.getContractType();               
				stm.setLong(1, Long.parseLong(contractString));
				domainString = info.getBusinessDomain();
				stm.setString(2, domainString);
				directorString = info.getLeader();
				stm.setString(3, directorString);
				apptypeString = info.getApplicationType();
				stm.setLong(4, Long.parseLong(apptypeString));
				stm.setLong(5, prjID);
                stm.executeUpdate();
                conn.commit();

                // 15-Jan-08: Fixed CLOB issue by using new function
                Db.writeClob("PROJECT_PLAN", "SCOPE_OBJECTIVE", "PROJECT_ID",
                        Long.toString(prjID), info.getScopeAndObjective());
			}
			else {
				//System.err.println("Project plan not found, creating new project plan");
				final long project_Plan_ID = ServerHelper.getNextSeq("PROJECT_PLAN_SEQ");
				sql =
					"INSERT INTO PROJECT_PLAN (PROJECT_PLAN_ID, PROJECT_ID, SCOPE_OBJECTIVE, CONTRACT_TYPE, DOMAIN, DIRECTOR, APPLICATION_TYPE)"
						+ " VALUES (?, ?, EMPTY_CLOB(), ?, ?, ?, ?)";
				stm = conn.prepareStatement(sql);
				stm.setLong(1, project_Plan_ID);
				stm.setLong(2, prjID);
                // 15-Jan-08: Used CLOB function instead of setString
				//objString = info.getScopeAndObjective();
				//stm.setString(3, objString);
                
				contractString = info.getContractType();
				stm.setLong(3, Long.parseLong(contractString));
				domainString = info.getBusinessDomain();
				stm.setString(4, domainString);
				directorString = info.getLeader();
				stm.setString(5, directorString);
				apptypeString = info.getApplicationType();
				stm.setLong(6, Long.parseLong(apptypeString));
				stm.executeUpdate();
				conn.commit();

                // 15-Jan-08: Fixed CLOB issue by using new function
                Db.writeClob("PROJECT_PLAN", "SCOPE_OBJECTIVE",
                        "PROJECT_PLAN_ID", Long.toString(project_Plan_ID),
                        info.getScopeAndObjective());
			}
			conn.setAutoCommit(true);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			}
            catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final WOChangeInfo getWOChange(final long changeID) {
		final WOChangeInfo changeInfo = new WOChangeInfo();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM CHANGES_OF_WORKORDER WHERE CHANGE_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, changeID);
			rs = stm.executeQuery();
			if (!rs.next())
				return null;
			changeInfo.changeID = rs.getLong("CHANGE_ID");
			changeInfo.projectID = rs.getInt("PROJECT_ID");
			changeInfo.item = rs.getString("ITEM");
			changeInfo.changes = rs.getString("CHANGES");
			changeInfo.reason = rs.getString("REASON");
			changeInfo.version = rs.getString("VERSION");
//			changeInfo.note = rs.getString("NOTE");
			changeInfo.action = rs.getString("action");
			changeInfo.changeDate = rs.getDate("CHANGE_DATE");
			return changeInfo;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	/**
	 * get number of change per each date change
	 * @param prjID ident.number of project
	 * @return Vector
	 */
	public static final Vector getWOChangeVersionList(long prjID) {
		Vector vResult = new Vector();
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		String strSQL =
			"SELECT COUNT(change_id) AS num,MAX(change_date) AS changedate,version "
				+ "FROM changes_of_workorder "
				+ "WHERE project_id=? GROUP BY version";
		try {
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(strSQL);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				WOChangeVersionInfo changeInfo = new WOChangeVersionInfo();
				changeInfo.setChangeDate(rs.getDate("changedate"));
				changeInfo.setNumChange(rs.getInt("num"));
				changeInfo.setVersion(rs.getString("version"));
				vResult.add(changeInfo);
			}
		}
		catch (Exception sqle) {
			sqle.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return vResult;
		}
	}
	public static final Vector getWOChangeList(final long prjID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = "SELECT * FROM CHANGES_OF_WORKORDER  WHERE PROJECT_ID = ? ORDER BY VERSION";
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final WOChangeInfo changeInfo = new WOChangeInfo();
				changeInfo.changeID = rs.getLong("CHANGE_ID");
				changeInfo.projectID = rs.getInt("PROJECT_ID");
				changeInfo.item = rs.getString("ITEM");
				changeInfo.changes = rs.getString("CHANGES");
				changeInfo.reason = rs.getString("REASON");
				changeInfo.version = rs.getString("VERSION");
				changeInfo.note = rs.getString("NOTE");
				changeInfo.changeDate = rs.getDate("CHANGE_DATE");
				changeInfo.action = rs.getString("action");
				
				resultVector.addElement(changeInfo);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
	public static final boolean addWOChange(final WOChangeInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"INSERT INTO CHANGES_OF_WORKORDER (CHANGE_ID, PROJECT_ID, ITEM, CHANGES, REASON, VERSION, ACTION, CHANGE_DATE ) "
					+ "VALUES (NVL((SELECT MAX(CHANGE_ID)+1 FROM CHANGES_OF_WORKORDER),1), ?, ?, ?, ?, ?, ?, SYSDATE)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, newInfo.projectID);
			stm.setString(2, newInfo.item);
			stm.setString(3, newInfo.changes);
			stm.setString(4, newInfo.reason);
			stm.setString(5, newInfo.version);
			stm.setString(6, newInfo.action);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean updateWOChange(final WOChangeInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE CHANGES_OF_WORKORDER 	SET      ITEM 	= ?"
					+ ",CHANGES       	= ?"
					+ ",REASON	   		= ?"
					+ ",VERSION	   		= ?"
					+ ",action	   		= ?"
					+ ",CHANGE_DATE	= SYSDATE"
					+ " WHERE CHANGE_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, newInfo.item);
			stm.setString(2, newInfo.changes);
			stm.setString(3, newInfo.reason);
			stm.setString(4, newInfo.version);
			stm.setString(5, newInfo.action);
			stm.setLong(6, newInfo.changeID);
			return (stm.executeUpdate() != 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean deleteWOChange(final long changeID) {
		return Db.delete(changeID, "CHANGE_ID", "CHANGES_OF_WORKORDER");
	}
	public static final WOChangeInfo getPLChange(final long changeID) {
		final WOChangeInfo changeInfo = new WOChangeInfo();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM CHANGES_OF_PROJECT_PLAN WHERE CHANGE_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, changeID);
			rs = stm.executeQuery();
			if (!rs.next())
				return null;
			changeInfo.changeID = rs.getLong("CHANGE_ID");
			changeInfo.projectID = rs.getInt("PROJECT_ID");
			changeInfo.item = rs.getString("ITEM");
			changeInfo.changes = rs.getString("CHANGES");
			changeInfo.reason = rs.getString("REASON");
			changeInfo.version = rs.getString("VERSION");
//			changeInfo.note = rs.getString("NOTE");
			changeInfo.action = rs.getString("action");
			changeInfo.changeDate = rs.getDate("CHANGE_DATE");
			return changeInfo;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final Vector getPLChangeList(final long prjID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM CHANGES_OF_PROJECT_PLAN  WHERE PROJECT_ID = ? ORDER BY CHANGE_DATE";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final WOChangeInfo changeInfo = new WOChangeInfo();
				changeInfo.changeID = rs.getLong("CHANGE_ID");
				changeInfo.projectID = rs.getInt("PROJECT_ID");
				changeInfo.item = rs.getString("ITEM");
				changeInfo.changes = rs.getString("CHANGES");
				changeInfo.reason = rs.getString("REASON");
				changeInfo.version = rs.getString("VERSION");
				changeInfo.note = rs.getString("NOTE");
				changeInfo.changeDate = rs.getDate("CHANGE_DATE");
				changeInfo.action = rs.getString("action");
				
				resultVector.addElement(changeInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
	public static final boolean addPLChange(final WOChangeInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"INSERT INTO CHANGES_OF_PROJECT_PLAN (CHANGE_ID, PROJECT_ID, ITEM, CHANGES, REASON, VERSION, ACTION, CHANGE_DATE ) "
					+ "VALUES (NVL((SELECT MAX(CHANGE_ID)+1 FROM CHANGES_OF_PROJECT_PLAN),1), ?, ?, ?, ?, ?, ?, SYSDATE)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, newInfo.projectID);
			stm.setString(2, newInfo.item);
			stm.setString(3, newInfo.changes);
			stm.setString(4, newInfo.reason);
			stm.setString(5, newInfo.version);
			stm.setString(6, newInfo.action);
			return (stm.executeUpdate() > 0);
		}
		catch (SQLException e) {
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean updatePLChange(final WOChangeInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE CHANGES_OF_PROJECT_PLAN 	SET      ITEM 	= ?"
					+ ",CHANGES       	= ?"
					+ ",REASON	   		= ?"
					+ ",VERSION	   		= ?"
					+ ",action	   		= ?"
					+ ",CHANGE_DATE	= SYSDATE"
					+ " WHERE CHANGE_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, newInfo.item);
			stm.setString(2, newInfo.changes);
			stm.setString(3, newInfo.reason);
			stm.setString(4, newInfo.version);
			stm.setString(5, newInfo.action);
			stm.setLong(6, newInfo.changeID);
			return (stm.executeUpdate() != 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean deletePLChange(final long changeID) {
		return Db.delete(changeID, "CHANGE_ID", "CHANGES_OF_PROJECT_PLAN");
	}
	public static final Vector getApprovalList(final long lProjectId, final int iType) {
		final Vector vtResult = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT A.APPROVAL_ID,"
					+ " A.APPROVAL_STATUS,"
					+ " A.APPROVAL_DATE,"
					+ " A.DEVELOPER_ID DEVELOPER_ID,"
					+ " B.NAME DEVELOPER_NAME,"
					+ " B.DESIGNATION DEVELOPER_POSITION,"
					+ " D.NAME DEVELOPER_ROLE"
					+ " FROM APPROVAL A, DEVELOPER B, ASSIGNMENT C, RESPONSIBILITY D"
					+ " WHERE A.PROJECT_ID = ? "
					+ " AND A.DEVELOPER_ID = B.DEVELOPER_ID"
                    + " AND A.PROJECT_ID = C.PROJECT_ID(+)"
                    + " AND A.DEVELOPER_ID = C.DEVELOPER_ID(+)"
                    + " AND D.RESPONSIBILITY_ID(+) = C.RESPONSE"
					+ " AND A.TYPE = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, lProjectId);
			stm.setInt(2, iType);
			rs = stm.executeQuery();
			while (rs.next()) {
                final SignatureInfo approvalInfo = new SignatureInfo();
				approvalInfo.setApprovalId(rs.getLong("APPROVAL_ID"));
                approvalInfo.setApprovalStatus(rs.getInt("APPROVAL_STATUS"));
                approvalInfo.setApprovalDate(rs.getDate("APPROVAL_DATE"));
                approvalInfo.setDeveloperId(rs.getLong("DEVELOPER_ID"));
                approvalInfo.setDeveloperName(rs.getString("DEVELOPER_NAME"));
                approvalInfo.setDeveloperPosition(rs.getString("DEVELOPER_POSITION"));
                approvalInfo.setDeveloperRole(rs.getString("DEVELOPER_ROLE"));
				vtResult.addElement(approvalInfo);
			}
			return vtResult;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final boolean updateApproval(final SignatureInfo info, final int iType) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE APPROVAL SET APPROVAL_STATUS = ?,"
					+ " APPROVAL_DATE = SYSDATE"
					+ " WHERE PROJECT_ID = ?"
					+ " AND DEVELOPER_ID = ?"
					+ " AND TYPE = ?";
			stm = conn.prepareStatement(sql);
			stm.setInt(1, info.getApprovalStatus());
			stm.setLong(2, info.getProjectId());
			stm.setLong(3, info.getDeveloperId());
			stm.setInt(4, iType);
			return (stm.executeUpdate() > 0);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean resetApproval(final int type, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE APPROVAL 	SET      APPROVAL_STATUS 	= 0"
					+ ",APPROVAL_DATE     	= SYSDATE"
					+ " WHERE PROJECT_ID = ?"
					+ " AND TYPE = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			stm.setInt(2, type);
			return (stm.executeUpdate() >= 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final String[] getWOAcceptance(final long prjID) {
		final String[] result = new String[4];
		Connection conn = null;
		PreparedStatement stm = null;
		String sql;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			String strAsset = null;
			String strProblem = null;
			String strRewardPenalty = null;
			String strProposal = null;
			sql = "SELECT * FROM PROJECT_PLAN  WHERE PROJECT_ID = ? ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			if (rs.next()) {
				strAsset = Db.getClob(rs, "ASSET");
				strProblem = Db.getClob(rs, "PROBLEM");
				strRewardPenalty = Db.getClob(rs, "REWARD_PENALTY");
				strProposal = Db.getClob(rs, "PROPOSAL");
				if (strAsset == null)
					strAsset = "";
				if (strProblem == null)
					strProblem = "";
				if (strRewardPenalty == null)
					strRewardPenalty = "";
				if (strProposal == null)
				strProposal = "";
			}
			else {
				strAsset = "";
				strProblem = "";
				strRewardPenalty = "";
				strProposal = "";
			}
			result[0] = strAsset;
			result[1] = strProblem;
			result[2] = strRewardPenalty;
			result[3] = strProposal;
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final Vector getMilestoneListByProject(final long prjID) {
		final Vector vtResult = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT MILESTONE_ID, NAME FROM MILESTONE WHERE PROJECT_ID = ? ORDER BY NAME";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
                final MilestoneInfo milestoneInfo = new MilestoneInfo();
                milestoneInfo.setMilestoneId(rs.getLong("MILESTONE_ID"));
                milestoneInfo.setName(rs.getString("NAME"));
                vtResult.addElement(milestoneInfo);
			}
			return vtResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
    /*
	public static final boolean updateWOAcceptance(final long prjID, final String[] info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = Db.getOracleConn();
			sql =
				"UPDATE PROJECT_PLAN SET LAST_UPDATE = SYSDATE "
					+ " ,ASSET  = ?"
					+ " ,PROBLEM  = ?"
					+ " ,REWARD_PENALTY  = ?"
					+ " WHERE PROJECT_ID = ?";
			stm = conn.prepareStatement(sql);
			Db.setCLOB(stm, 1, info[0]);
			Db.setCLOB(stm, 2, info[1]);
			Db.setCLOB(stm, 3, info[2]);
			stm.setLong(4, prjID);
			return (stm.executeUpdate() != 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
    */

	/**
     * 16-Jan-08: Fixed CLOB issue by normal JDBC function
     * Update project acceptance
     * @param prjID
     * @param String[] array of contents: Asset, Problem and Reward And Penalty
     * @return
     */
    public static final boolean updateWOAcceptance(
        final long prjID, final String[] info)
    {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        oracle.sql.CLOB ocAsset;
        oracle.sql.CLOB ocProblem;
        oracle.sql.CLOB ocRwdPen;
		oracle.sql.CLOB ocPro;
        boolean bResult = true;
        try {
            // Reset to empty LOB first to avoid putString() function issue:
            // when update text shorter than old data, the remain text of
            // old data is still stayed!!!
            String sqlSetEmpty = 
                "UPDATE PROJECT_PLAN SET LAST_UPDATE = SYSDATE," +
                " ASSET = EMPTY_CLOB(), PROBLEM = EMPTY_CLOB()," +
                " REWARD_PENALTY = EMPTY_CLOB() , PROPOSAL = EMPTY_CLOB() " +
                    " WHERE PROJECT_ID = ?";
            // Should lock row before trying to update LOB data
            String sqlLockRow =
                "SELECT ASSET, PROBLEM, REWARD_PENALTY, PROPOSAL FROM PROJECT_PLAN" +
                    " WHERE PROJECT_ID = ? FOR UPDATE";
            String sqlUpdate = 
                "UPDATE PROJECT_PLAN SET LAST_UPDATE = SYSDATE," +
                " ASSET = ?, PROBLEM = ?, REWARD_PENALTY=? , PROPOSAL=? " +
                    " WHERE PROJECT_ID = ?";
            conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);
            // Set EMPTY LOB for the LOB columns first
            pstm = conn.prepareStatement(sqlSetEmpty);
            pstm.setLong(1, prjID);
            pstm.executeUpdate();
            // Then open row for updating
            pstm = conn.prepareStatement(sqlLockRow);
            pstm.setLong(1, prjID);
            rs = pstm.executeQuery();
            if (rs.next()) {
                ocAsset = (oracle.sql.CLOB) rs.getClob(1);
                ocProblem = (oracle.sql.CLOB) rs.getClob(2);
                ocRwdPen = (oracle.sql.CLOB) rs.getClob(3);
                ocPro = (oracle.sql.CLOB) rs.getClob(4);
                // Set new data for update those LOB column
                ocAsset.putString(1, info[0]);
                ocProblem.putString(1, info[1]);
                ocRwdPen.putString(1, info[2]);
                ocPro.putString(1, info[3]);
                pstm = conn.prepareStatement(sqlUpdate);    // Update content
                pstm.setClob(1, ocAsset);
                pstm.setClob(2, ocProblem);
                pstm.setClob(3, ocRwdPen);
				pstm.setClob(4, ocPro);
                pstm.setLong(5, prjID);
                pstm.executeUpdate();
                pstm.close();
            }
            conn.commit();
            conn.setAutoCommit(true);
        }
        catch (Exception e) {
            bResult = false;
            e.printStackTrace();
            try {
                conn.rollback();
            }
            catch (SQLException sqe) {
                sqe.printStackTrace();
            }
        }
        finally {
            ServerHelper.closeConnection(conn, pstm, rs);
            return bResult;
        }
    }
	/**
	 * 
	 * @param type:  
	 * @param lProjectID
	 * @param strAccountName 
	 * @param strType
	 * @return
	 */
	public static final Vector getDevListForSign(final int type, final long lProjectID, final String strAccountName, final String strType){
		Vector vectorResult = new Vector();
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		String strCondition = "";
		ResultSet rs = null;
		try{
			if (strAccountName !=  null){
				if ("Account".equals(strType)){
					strCondition = " AND UPPER(ACCOUNT) LIKE '" + ConvertString.toSql(strAccountName.trim().toUpperCase(), ConvertString.adText) + "%'"
									+ " AND STATUS != 4";
				}
				else{
					strCondition = " AND UPPER(NAME) LIKE '%" + ConvertString.toSql(strAccountName.trim().toUpperCase(), ConvertString.adText) + "%'" 
									+ " AND STATUS != 4";
				}
			}
			conn = ServerHelper.instance().getConnection();
			sql =
				" SELECT * FROM DEVELOPER "
					+ " WHERE DEVELOPER_ID NOT IN "
					+ "       (SELECT DEVELOPER_ID FROM APPROVAL "
					+ "  	   WHERE TYPE = ? AND PROJECT_ID = ? ) "
					+ strCondition
					+ " ORDER BY ACCOUNT";
			preStm = conn.prepareStatement(sql);
			preStm.setInt(1, type);
			preStm.setLong(2, lProjectID);
			rs = preStm.executeQuery();
			while (rs.next()){
				UserInfo userInfo = new UserInfo();
				userInfo.account = rs.getString("ACCOUNT");
				userInfo.Name = rs.getString("NAME");
				userInfo.group = rs.getString("GROUP_NAME");
				vectorResult.add(userInfo);
			}
			return vectorResult;
		}
		catch (SQLException ex){
			ex.printStackTrace();
			return vectorResult;
		}
		finally{
			ServerHelper.closeConnection(conn, preStm, rs);
		}
	}
	public static final boolean addApproval(final int type, final long devID, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"INSERT INTO APPROVAL ("
					+ "  APPROVAL_ID"
					+ ", APPROVAL_STATUS "
					+ ", APPROVAL_DATE "
					+ ", TYPE "
					+ ", PROJECT_ID"
					+ ", DEVELOPER_ID )"
					+ " VALUES (NVL((SELECT MAX(APPROVAL_ID)+1 FROM APPROVAL),1), 0, SYSDATE,?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setInt(1, type);
			stm.setLong(2, prjID);
			stm.setLong(3, devID);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean deleteApproval(final long approvalID) {
		return Db.delete(approvalID, "APPROVAL_ID", "APPROVAL");
	}
	public static final long getPrjPlanIDFromPrjID(final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		long result = 0;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM PROJECT_PLAN WHERE PROJECT_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			if (rs.next())
				result = rs.getLong("PROJECT_PLAN_ID");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return result;
		}
	}
	
	public static final long getPrjIDFromPrjPlanID(final long PrjPlanID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		long result = 0;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM PROJECT_PLAN WHERE PROJECT_PLAN_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, PrjPlanID);
			rs = stm.executeQuery();
			if (rs.next())
				result = rs.getLong("PROJECT_ID");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return result;
		}
	}
	
	public static final ReferenceInfo getPLReference(final long refID) {
		final ReferenceInfo refInfo = new ReferenceInfo();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * from REFERENCES where REFERENCE_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, refID);
			rs = stm.executeQuery();
			if (!rs.next())
				return null;
			refInfo.referenceID = rs.getLong("REFERENCE_ID");
			refInfo.projectPlanID = rs.getLong("PROJECT_PLAN_ID");
			refInfo.issueDate = rs.getDate("ISSUED_DATE");
			refInfo.source = rs.getString("SOURCE");
			refInfo.note = rs.getString("NOTE");
			refInfo.document = rs.getString("DOCUMENT");
			return refInfo;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final Vector getPLReferenceList(final long prjID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT * FROM REFERENCES, PROJECT_PLAN"
					+ " WHERE REFERENCES.PROJECT_PLAN_ID = PROJECT_PLAN.PROJECT_PLAN_ID"
					+ " AND PROJECT_PLAN.PROJECT_ID=?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final ReferenceInfo refInfo = new ReferenceInfo();
				refInfo.referenceID = rs.getLong("REFERENCE_ID");
				refInfo.projectPlanID = rs.getLong("PROJECT_PLAN_ID");
				refInfo.issueDate = rs.getDate("ISSUED_DATE");
				refInfo.source = rs.getString("SOURCE");
				refInfo.note = rs.getString("NOTE");
				refInfo.document = rs.getString("DOCUMENT");
				resultVector.addElement(refInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
	public static final boolean addPLReference(final ReferenceInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"INSERT INTO REFERENCES (REFERENCE_ID, PROJECT_PLAN_ID, ISSUED_DATE , "
					+ " SOURCE, NOTE, DOCUMENT ) "
					+ " VALUES ((SELECT NVL(MAX(REFERENCE_ID)+1,1) FROM REFERENCES), ?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, newInfo.projectPlanID);
			stm.setDate(2, newInfo.issueDate);
			stm.setString(3, newInfo.source);
			stm.setString(4, newInfo.note);
			stm.setString(5, newInfo.document);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean updatePLReference(final ReferenceInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE REFERENCES    SET      ISSUED_DATE 	= ?"
					+ ", SOURCE	= ?"
					+ ", NOTE	= ?"
					+ ", DOCUMENT	= ?"
					+ "  WHERE REFERENCE_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setDate(1, newInfo.issueDate);
			stm.setString(2, newInfo.source);
			stm.setString(3, newInfo.note);
			stm.setString(4, newInfo.document);
			stm.setLong(5, newInfo.referenceID);
			return (stm.executeUpdate() != 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean deletePLReference(final long refID) {
		return Db.delete(refID, "REFERENCE_ID", "REFERENCES");
	}
	public static final DependencyInfo getPLDependency(final long dependencyID) {
		DependencyInfo dependencyInfo = null;
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			//stm = conn.createStatement();
			//ConvertString cs = new ConvertString();
			sql = "SELECT * from DEPENDENCIES where DEPENDENCY_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, dependencyID);
			rs = stm.executeQuery();
			if (rs.next()) {
				dependencyInfo = new DependencyInfo();
				dependencyInfo.dependencyID = rs.getLong("DEPENDENCY_ID");
				dependencyInfo.projectPlanID = rs.getLong("PROJECT_PLAN_ID");
				dependencyInfo.note = rs.getString("NOTE");
				dependencyInfo.actualDeliveryDate = rs.getDate("ACTUAL_DELIVERY_DATE");
				dependencyInfo.plannedDeliveryDate = rs.getDate("PLANNED_DELIVERY_DATE");
				dependencyInfo.item = rs.getString("ITEM");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return dependencyInfo;
		}
	}
	public static final Vector getPLDependencyList(final long prjID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			final long projectPlanID = Project.getPrjPlanIDFromPrjID(prjID);
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM DEPENDENCIES WHERE PROJECT_PLAN_ID = ? ORDER BY PLANNED_DELIVERY_DATE";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, projectPlanID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final DependencyInfo dependencyInfo = new DependencyInfo();
				dependencyInfo.dependencyID = rs.getLong("DEPENDENCY_ID");
				dependencyInfo.projectPlanID = rs.getLong("PROJECT_PLAN_ID");
				dependencyInfo.note = rs.getString("NOTE");
				dependencyInfo.actualDeliveryDate = rs.getDate("ACTUAL_DELIVERY_DATE");
				dependencyInfo.plannedDeliveryDate = rs.getDate("PLANNED_DELIVERY_DATE");
				dependencyInfo.item = rs.getString("ITEM");
				resultVector.addElement(dependencyInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
	public static final boolean addPLDependency(final DependencyInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"INSERT INTO DEPENDENCIES (DEPENDENCY_ID, PROJECT_PLAN_ID, NOTE , "
					+ " ACTUAL_DELIVERY_DATE, PLANNED_DELIVERY_DATE, ITEM ) "
					+ " VALUES (NVL((SELECT MAX(DEPENDENCY_ID)+1 from DEPENDENCIES),1), ?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, newInfo.projectPlanID);
			stm.setString(2, newInfo.note);
			stm.setDate(3, newInfo.actualDeliveryDate);
			stm.setDate(4, newInfo.plannedDeliveryDate);
			stm.setString(5, newInfo.item);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean updatePLDependency(final DependencyInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE DEPENDENCIES    SET      NOTE 	= ?"
					+ ", ACTUAL_DELIVERY_DATE	= ?"
					+ ", PLANNED_DELIVERY_DATE	= ?"
					+ ", ITEM	= ?"
					+ "  WHERE DEPENDENCY_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, newInfo.note);
			stm.setDate(2, newInfo.actualDeliveryDate);
			stm.setDate(3, newInfo.plannedDeliveryDate);
			stm.setString(4, newInfo.item);
			stm.setLong(5, newInfo.dependencyID);
			return (stm.executeUpdate() != 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean deletePLDependency(final long dependencyID) {
		return Db.delete(dependencyID, "DEPENDENCY_ID", "DEPENDENCIES");
	}
	private static int IterationGen(final long milestoneID) {
		int maxKey = 1;
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT MAX(ITERATION) MAX_CODE FROM ITERATION WHERE MILESTONE_ID = ? ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, milestoneID);
			rs = stm.executeQuery();
			if (rs.next()) {
				maxKey = rs.getInt("MAX_CODE") + 1;
			}
			return maxKey;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
		return 1;
	}
	public static final IterationInfo getPLIteration(final long iterationID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		final IterationInfo iterationInfo = new IterationInfo();
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM ITERATION WHERE ITERATION_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, iterationID);
			rs = stm.executeQuery();
			if (rs == null || !rs.next())
				return null;
			iterationInfo.iterationID = rs.getLong("ITERATION_ID");
			iterationInfo.description = rs.getString("DESCRIPTION");
			iterationInfo.milestone = rs.getString("MILESTONE");
			iterationInfo.milestoneID = rs.getLong("MILESTONE_ID");
			iterationInfo.iteration = rs.getInt("ITERATION");
			iterationInfo.planEndDate = rs.getDate("PLAN_END_DATE");
			iterationInfo.replanEndDate = rs.getDate("REPLAN_END_DATE");
			iterationInfo.actualEndDate = rs.getDate("ACTUAL_END_DATE");
			return iterationInfo;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final Vector getPLIterationList(final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		final Vector resultVector = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				" SELECT A.ITERATION_ID, A.DESCRIPTION, A.MILESTONE, A.MILESTONE_ID, A.ITERATION,"
					+ " A.PLAN_END_DATE, A.REPLAN_END_DATE, A.ACTUAL_END_DATE"
					+ " FROM ITERATION A, MILESTONE B "
					+ " WHERE A.MILESTONE_ID = B.MILESTONE_ID AND B.PROJECT_ID = ? "
					+ " ORDER BY A.MILESTONE_ID, A.ITERATION";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final IterationInfo iterationInfo = new IterationInfo();
				iterationInfo.iterationID = rs.getLong("ITERATION_ID");
				iterationInfo.description = rs.getString("DESCRIPTION");
				iterationInfo.milestone = rs.getString("MILESTONE");
				iterationInfo.milestoneID = rs.getLong("MILESTONE_ID");
				iterationInfo.iteration = rs.getInt("ITERATION");
				iterationInfo.planEndDate = rs.getDate("PLAN_END_DATE");
				iterationInfo.replanEndDate = rs.getDate("REPLAN_END_DATE");
				iterationInfo.actualEndDate = rs.getDate("ACTUAL_END_DATE");
				if ((iterationInfo.actualEndDate != null)) {
					if (iterationInfo.replanEndDate != null)
						iterationInfo.isOntime = (iterationInfo.actualEndDate.compareTo(iterationInfo.replanEndDate) <= 0) ? "Yes" : "No";
					else if (iterationInfo.planEndDate != null)
						iterationInfo.isOntime = (iterationInfo.actualEndDate.compareTo(iterationInfo.planEndDate) <= 0) ? "Yes" : "No";
				}
				if (iterationInfo.actualEndDate != null && iterationInfo.planEndDate != null) {
					if (iterationInfo.replanEndDate != null)
						iterationInfo.deviation =
							CommonTools.decForm.format(
								(iterationInfo.actualEndDate.getTime() - iterationInfo.replanEndDate.getTime()) / (24 * 3600 * 1000));
					else
						iterationInfo.deviation =
							CommonTools.decForm.format(
								(iterationInfo.actualEndDate.getTime() - iterationInfo.planEndDate.getTime()) / (24 * 3600 * 1000));
				}
				resultVector.addElement(iterationInfo);
			}
			return resultVector;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final boolean addPLIteration(final IterationInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		final int newIter = IterationGen(newInfo.milestoneID);
		try {
			long iterationId = ServerHelper.getNextSeq("ITERATION_SEQ");
			conn = ServerHelper.instance().getConnection();
			sql =
				" INSERT INTO ITERATION (ITERATION_ID, DESCRIPTION, MILESTONE, MILESTONE_ID, ITERATION, "
					+ " PLAN_END_DATE, REPLAN_END_DATE, ACTUAL_END_DATE) "
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			stm = conn.prepareStatement(sql);
			stm.setLong(1, iterationId);
			stm.setString(2, newInfo.description);
			stm.setString(3, newInfo.milestone);
			stm.setLong(4, newInfo.milestoneID);
			stm.setInt(5, newIter);
			stm.setDate(6, newInfo.planEndDate);
			stm.setDate(7, newInfo.replanEndDate);
			stm.setDate(8, newInfo.actualEndDate);
			return (stm.executeUpdate() != 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean updatePLIteration(final IterationInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE ITERATION SET DESCRIPTION=?, MILESTONE=?, PLAN_END_DATE=?, REPLAN_END_DATE=?, ACTUAL_END_DATE= ?"
					+ "WHERE ITERATION_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, newInfo.description);
			stm.setString(2, newInfo.milestone);
			stm.setDate(3, newInfo.planEndDate);
			stm.setDate(4, newInfo.replanEndDate);
			stm.setDate(5, newInfo.actualEndDate);
			stm.setLong(6, newInfo.iterationID);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final boolean deletePLIteration(final long iterationID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM ITERATION WHERE ITERATION_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, iterationID);
			rs = stm.executeQuery();
			if (!rs.next())
				return false;
			final long milestoneID = rs.getLong("MILESTONE_ID");
			final long iteration = rs.getLong("ITERATION");
			sql = "DELETE FROM  ITERATION WHERE ITERATION_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, iterationID);
			if (stm.executeUpdate() == 0)
				return false;
			sql = "UPDATE ITERATION A SET ITERATION = (ITERATION - 1)" + " WHERE MILESTONE_ID = ? AND ITERATION > ? ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, milestoneID);
			stm.setLong(2, iteration);
			return (stm.executeUpdate() != 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final String getPLOrjStructure(final long prjPlanID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT ORG_STRUCTURE from PROJECT_PLAN where PROJECT_PLAN_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjPlanID);
			rs = stm.executeQuery();
			String retVal = null;
			if (rs.next())
				retVal = Db.getClob(rs, "ORG_STRUCTURE");
			return (retVal == null) ? "" : retVal;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
    /* 16-Jan-08: removed due to CLOB issue when using deprecated function setCLOB()
	public static final boolean updatePLOrjStructure(final long prjPlanID, final String orgStructure) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = Db.getOracleConn();
			sql = "UPDATE PROJECT_PLAN   SET   ORG_STRUCTURE = ? WHERE PROJECT_PLAN_ID = ?";
			stm = conn.prepareStatement(sql);
			Db.setCLOB(stm, 1, orgStructure);
			stm.setLong(2, prjPlanID);
			return (stm.executeUpdate() != 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
    */

    /**
     * Update project organization structure
     * @param prjPlanID
     * @param orgStructure
     * @return
     */
    public static final boolean updatePLOrjStructure(final long prjPlanID,
        final String orgStructure)
    {
        return Db.writeClob("PROJECT_PLAN", "ORG_STRUCTURE", "PROJECT_PLAN_ID",
                Long.toString(prjPlanID), orgStructure);
    }
    
	public static final InterfaceInfo getPLInterface(final long interfaceID) {
		final InterfaceInfo interfaceInfo = new InterfaceInfo();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM INTERFACE WHERE INTERFACE_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, interfaceID);
			rs = stm.executeQuery();
			if (rs.next()) {
				interfaceInfo.interfaceID = rs.getLong("INTERFACE_ID");
				interfaceInfo.projectPlanID = rs.getLong("PROJECT_PLAN_ID");
				interfaceInfo.name = rs.getString("NAME");
				interfaceInfo.position = rs.getString("POSITION");
				interfaceInfo.responsibility = rs.getString("RESPONSIBILITY");
				interfaceInfo.contact = rs.getString("CONTACT");
				interfaceInfo.type = rs.getInt("TYPE");
				interfaceInfo.assignedID = Db.getDouble(rs, "ASSIGN_TO_ID");
				interfaceInfo.roleID = rs.getLong("ROLE_ID");
				interfaceInfo.communication = rs.getString("COMMUNICATION");
				// landd add new
				interfaceInfo.function = rs.getString("FUNCTION");
				interfaceInfo.department = rs.getString("DEPARTMENT");
				interfaceInfo.otherProjName = rs.getString("OTHER_PROJECT_NAME");
				interfaceInfo.dependency = rs.getString("DEPENDENCY");
				interfaceInfo.contactPerson = rs.getString("CONTACT_PERSON");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return interfaceInfo;
		}
	}
	public static final Vector getPLInterfaceList(final long prjPlanID, final int type) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;			
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = " SELECT A.INTERFACE_ID, A.PROJECT_PLAN_ID, A.NAME, A.POSITION, A.RESPONSIBILITY,A.CONTACT_PERSON," 	
						+ "A.CONTACT, A.TYPE, A.ASSIGN_TO_ID, A.ROLE_ID, A.COMMUNICATION, A.FUNCTION, A.DEPARTMENT,"
						+ "A.OTHER_PROJECT_NAME, A.DEPENDENCY, A.OTHER_PROJECT_NAME AS PROJECT_NAME, A.CONTACT_ACC AS CONTACT_ACCOUNT"
				 + " FROM INTERFACE A"
				 + " WHERE PROJECT_PLAN_ID = ? ";
				 
			if (type != 0) sql += " AND A.TYPE =" + type;
			sql += " ORDER BY A.INTERFACE_ID DESC";
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjPlanID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final InterfaceInfo interfaceInfo = new InterfaceInfo();
				interfaceInfo.interfaceID = rs.getLong("INTERFACE_ID");
				interfaceInfo.projectPlanID = rs.getLong("PROJECT_PLAN_ID");
				interfaceInfo.name = rs.getString("NAME");
				interfaceInfo.position = rs.getString("POSITION");
				interfaceInfo.responsibility = rs.getString("RESPONSIBILITY");
				interfaceInfo.contactPerson = rs.getString("CONTACT_PERSON");
				interfaceInfo.contact = rs.getString("CONTACT");
				interfaceInfo.type = rs.getInt("TYPE");
				interfaceInfo.assignedID = Db.getDouble(rs, "ASSIGN_TO_ID");
				interfaceInfo.roleID = rs.getLong("ROLE_ID");
				interfaceInfo.communication = rs.getString("COMMUNICATION");
				// landd add new
				interfaceInfo.function = rs.getString("FUNCTION");
				interfaceInfo.department = rs.getString("DEPARTMENT");
				interfaceInfo.otherProjName = rs.getString("PROJECT_NAME");
				interfaceInfo.dependency = rs.getString("DEPENDENCY");				
				interfaceInfo.otherProjName = rs.getString("PROJECT_NAME");
				interfaceInfo.contactAccount = rs.getString("CONTACT_ACCOUNT");
				resultVector.addElement(interfaceInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
	public static final boolean addPLInterface(final InterfaceInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"INSERT INTO INTERFACE (INTERFACE_ID, PROJECT_PLAN_ID, NAME , "
					+ " POSITION, RESPONSIBILITY, CONTACT, TYPE, ASSIGN_TO_ID, ROLE_ID, COMMUNICATION ) "
					+ " VALUES ((SELECT NVL(MAX(INTERFACE_ID)+1,1) FROM INTERFACE), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, newInfo.projectPlanID);
			stm.setString(2, newInfo.name);
			stm.setString(3, newInfo.position);
			stm.setString(4, newInfo.responsibility);
			stm.setString(5, newInfo.contact);
			stm.setLong(6, newInfo.type);
			Db.setDouble(stm, 7, newInfo.assignedID);
			stm.setLong(8, newInfo.roleID);
			stm.setString(9, newInfo.communication);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean updatePLInterface(final InterfaceInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			sql =
				"UPDATE INTERFACE SET NAME = ? "
					+ " , POSITION = ? "
					+ " , RESPONSIBILITY = ? "
					+ " , CONTACT = ? "
					+ " , TYPE = ? "
					+ " , ASSIGN_TO_ID = ? "
					+ " , ROLE_ID = ? "
					+ " , COMMUNICATION = ? "
					+ "  WHERE INTERFACE_ID = ?";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1, newInfo.name);
			stm.setString(2, newInfo.position);
			stm.setString(3, newInfo.responsibility);
			stm.setString(4, newInfo.contact);
			stm.setLong(5, newInfo.type);
			Db.setDouble(stm, 6, newInfo.assignedID);
			stm.setLong(7, newInfo.roleID);
			stm.setString(8, newInfo.communication);
			stm.setLong(9, newInfo.interfaceID);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean deletePLInterface(final long interfaceID) {
		return Db.delete(interfaceID, "INTERFACE_ID", "INTERFACE");
	}
	public static final SubcontractInfo getPLSubcontract(final long subcontractID) {
		final SubcontractInfo subcontractInfo = new SubcontractInfo();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * from SUBCONTRACT where SUBCONTRACT_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, subcontractID);
			rs = stm.executeQuery();
			if (!rs.next())
				return null;
			subcontractInfo.subcontractID = rs.getLong("SUBCONTRACT_ID");
			subcontractInfo.projectPlanID = rs.getLong("PROJECT_PLAN_ID");
			subcontractInfo.deliverable = rs.getString("DELIVERABLE");
			subcontractInfo.plannedDeliveryDate = rs.getDate("PLAN_DELIVERY_DATE");
			subcontractInfo.actualDeliveryDate = rs.getDate("ACTUAL_DELIVERY_DATE");
			subcontractInfo.job = rs.getString("JOB");
			subcontractInfo.note = rs.getString("NOTE");
			return subcontractInfo;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
		return null;
	}
	public static final Vector getPLSubcontractList(final long prjPlanID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM SUBCONTRACT WHERE PROJECT_PLAN_ID = ?  ORDER BY PLAN_DELIVERY_DATE";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjPlanID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final SubcontractInfo subcontractInfo = new SubcontractInfo();
				subcontractInfo.subcontractID = rs.getLong("SUBCONTRACT_ID");
				subcontractInfo.projectPlanID = rs.getLong("PROJECT_PLAN_ID");
				subcontractInfo.deliverable = rs.getString("DELIVERABLE");
				subcontractInfo.plannedDeliveryDate = rs.getDate("PLAN_DELIVERY_DATE");
				subcontractInfo.actualDeliveryDate = rs.getDate("ACTUAL_DELIVERY_DATE");
				subcontractInfo.job = rs.getString("JOB");
				subcontractInfo.note = rs.getString("NOTE");
				
				subcontractInfo.sName = rs.getString("SNAME");
				subcontractInfo.contactP = rs.getString("CONTACT_PERSON");
				subcontractInfo.refToContract = rs.getString("REF_TO_CONTRACT");
				resultVector.addElement(subcontractInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
	public static final boolean addPLSubcontract(final SubcontractInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"INSERT INTO SUBCONTRACT (SUBCONTRACT_ID, PROJECT_PLAN_ID, DELIVERABLE , "
					+ " PLAN_DELIVERY_DATE, ACTUAL_DELIVERY_DATE, JOB, NOTE, SNAME, CONTACT_PERSON,REF_TO_CONTRACT) "
					+ " VALUES ((SELECT NVL(MAX(SUBCONTRACT_ID)+1,1) FROM SUBCONTRACT), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, newInfo.projectPlanID);
			stm.setString(2, newInfo.deliverable);
			stm.setDate(3, newInfo.plannedDeliveryDate);
			stm.setDate(4, newInfo.actualDeliveryDate);
			stm.setString(5, newInfo.job);
			stm.setString(6, newInfo.note);
			stm.setString(7, newInfo.sName);
			stm.setString(8, newInfo.contactP);
			stm.setString(9, newInfo.refToContract);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final boolean updatePLSubcontract(final SubcontractInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE SUBCONTRACT    SET       DELIVERABLE = ? "
					+ ", PLAN_DELIVERY_DATE = ? "
					+ ", ACTUAL_DELIVERY_DATE = ? "
					+ ", JOB = ? "
					+ ", NOTE = ? "
					+ ", SNAME = ? "
					+ ", CONTACT_PERSON = ? "
					+ ", REF_TO_CONTRACT = ? "
					+ "  WHERE SUBCONTRACT_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, newInfo.deliverable);
			stm.setDate(2, newInfo.plannedDeliveryDate);
			stm.setDate(3, newInfo.actualDeliveryDate);
			stm.setString(4, newInfo.job);
			stm.setString(5, newInfo.note);
			
			stm.setString(6, newInfo.sName);
			stm.setString(7, newInfo.contactP);
			stm.setString(8, newInfo.refToContract);
			stm.setLong(9, newInfo.subcontractID);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	private static Vector getCon_AssList(final long prjID, final int isCon) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		final Vector vt = new Vector();
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT * FROM CONSTRAINTS,PROJECT_PLAN"
					+ " WHERE CONSTRAINTS.PROJECT_PLAN_ID=PROJECT_PLAN.PROJECT_PLAN_ID"
					+ " AND PROJECT_PLAN.PROJECT_ID=?"
					+ " AND IS_CONSTRAINT=?"
					+ " ORDER BY CONSTRAINT_ID";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			stm.setInt(2, isCon);
			rs = stm.executeQuery();
			while (rs.next()) {
				final ConstraintInfo constrInfor = new ConstraintInfo();
				constrInfor.constraintID = rs.getLong("CONSTRAINT_ID");
				constrInfor.description = rs.getString("DESCRIPTION");
				constrInfor.note = rs.getString("NOTE"); // landd add
				constrInfor.isConstraint = rs.getInt("IS_CONSTRAINT");
				constrInfor.type = rs.getInt("CONSTRAINT_TYPE");
				constrInfor.prjPlanID = rs.getLong("PROJECT_PLAN_ID");
				vt.add(constrInfor);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("sql stmt = " + sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return vt;
		}
	}
	public static boolean addCon_Ass(final ConstraintInfo ConstrInfo) {
		String insertStatement = "";
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			// landd add note start
			insertStatement =
				" INSERT INTO CONSTRAINTS (CONSTRAINT_ID,PROJECT_PLAN_ID,DESCRIPTION,NOTE,CONSTRAINT_TYPE,IS_CONSTRAINT)"
					+ " Values (NVL((SELECT MAX(CONSTRAINT_ID)+1 FROM CONSTRAINTS),1),?,?,?,?,?)";
			prepStmt = conn.prepareStatement(insertStatement);
			prepStmt.setLong(1, ConstrInfo.prjPlanID);
			prepStmt.setString(2, ConstrInfo.description);
			prepStmt.setString(3, ConstrInfo.note);	
			prepStmt.setInt(4, ConstrInfo.type);
			prepStmt.setInt(5, ConstrInfo.isConstraint);
			//landd add note end
			prepStmt.executeUpdate();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public static boolean updateCon_Ass(final ConstraintInfo ConstrInfo) {
		String updateStatement = "";
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = ServerHelper.instance().getConnection();
			updateStatement =
				" UPDATE CONSTRAINTS SET"
					+ " DESCRIPTION = ? ,"
					+ " NOTE = ? ,"
					+ " CONSTRAINT_TYPE = ?,"
					+ " IS_CONSTRAINT = ?"
					+ " WHERE CONSTRAINT_ID = ?";
			stm = conn.prepareStatement(updateStatement);
			stm.setString(1, ConstrInfo.description);
			stm.setString(2, ConstrInfo.note);
			stm.setInt(3, ConstrInfo.type);
			stm.setInt(4, ConstrInfo.isConstraint);
			stm.setLong(5, ConstrInfo.constraintID);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static boolean deleteCon_Ass(final long constrainID) {
		return Db.delete(constrainID, "CONSTRAINT_ID", "CONSTRAINTS");
	}
	/**
	* @return recordset of Constraints
	* @param ProjectId
	*/
	public static final Vector getConstraintList(final long prjID) {
		return getCon_AssList(prjID, ISCONSTRAINT);
	}
	/**
	* @return Assumption list of Constraints
	* @param ProjectId
	*/
	public static final Vector getAssumptionList(final long prjID) {
		return getCon_AssList(prjID, ISASSUMPTION);
	}
	private static long makeTrainID() {
		long key = 0;
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT MAX(TRAINING_PLAN_ID) MAX_TRAIN_ID FROM TRAINING_PLAN";
			rs = stm.executeQuery(sql);
			if (rs != null && rs.next()) {
				key = rs.getLong("MAX_TRAIN_ID") + 1;
			}
			return key;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
		return key;
	}
	private static long makeToolID() {
		long key = 0;
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT MAX(TOOL_ID) MAX_TOOL_ID FROM TOOLS";
			rs = stm.executeQuery(sql);
			if (rs != null && rs.next()) {
				key = rs.getLong("MAX_TOOL_ID") + 1;
			}
			return key;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
		}
		return key;
	}
	/**
	* return recordset of Training
	* param ProjectId
	*/
	public static final Vector getTrainingList(final long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		final Vector vt = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM TRAINING_PLAN WHERE PROJECT_PLAN_ID=" + prjID + " ORDER BY START_DATE DESC";
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				final TrainingInfo tnInfo = new TrainingInfo();
				tnInfo.trainingID = rs.getLong("TRAINING_PLAN_ID");
				tnInfo.prjID = rs.getLong("PROJECT_PLAN_ID");
//				tnInfo.description = rs.getString("DESCRIPTION");
				tnInfo.participant = rs.getString("PARTICIPANTS");
				tnInfo.waiver = rs.getString("WAIVER_CRITERIA");
//				tnInfo.startD = rs.getDate("START_DATE");
//				tnInfo.endD = rs.getDate("END_DATE");
//				tnInfo.actualEndD = rs.getDate("ACTUAL_END_DATE");
				tnInfo.topic = rs.getString("TOPIC");
//				tnInfo.verifyBy = rs.getInt("VERIFYBY");
				tnInfo.duration = rs.getString("duration");
				vt.add(tnInfo);
			}
			return vt;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
		return null;
	}
	/**
	* Get one start date and end date of project
	* @Param: ProjectID
	*
	*/
	public static final ProjectDateInfo getProjectDate(final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT START_DATE, NVL(PLAN_FINISH_DATE,BASE_FINISH_DATE) FINISHDATE," +
				" PLAN_START_DATE, ACTUAL_FINISH_DATE FROM PROJECT WHERE PROJECT_ID=?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			final ProjectDateInfo prjDateInfo = new ProjectDateInfo();
			if (rs.next()) {
				prjDateInfo.startD = rs.getDate("PLAN_START_DATE");
				prjDateInfo.plannedStartDate = prjDateInfo.startD;
				prjDateInfo.plannedEndDate = rs.getDate("FINISHDATE");
				prjDateInfo.endD = prjDateInfo.plannedEndDate;
				prjDateInfo.actualEndDate = rs.getDate("ACTUAL_FINISH_DATE");
				if (prjDateInfo.startD == null) {   // => prjDateInfo.startD is Planned start date
					prjDateInfo.startD = rs.getDate("START_DATE");
				}
				prjDateInfo.actualStartDate = rs.getDate("START_DATE");
			}
			return prjDateInfo;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("sql stmt = " + sql + ";project_id=" + prjID);
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	/**
	*  Add new Training to TRAINING_PLAN
	* @param:TrainingInfo
	* return boolean
	*/
	public static final boolean addTraining(final TrainingInfo tnInfo) {
		String insertStatement = "";
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			insertStatement =
				" INSERT INTO TRAINING_PLAN ("
					+ " TRAINING_PLAN_ID,"
					+ " PROJECT_PLAN_ID,"
//					+ " DESCRIPTION,"
					+ " PARTICIPANTS,"
					+ " WAIVER_CRITERIA,"
//					+ " START_DATE,"
//					+ " END_DATE,"
//					+ " ACTUAL_END_DATE,"
					+ " TOPIC,"
					+ " duration)"
					+ " VALUES (?,?,?,?,?,?)";
			prepStmt = conn.prepareStatement(insertStatement);
			prepStmt.setLong(1, makeTrainID());
			prepStmt.setLong(2, tnInfo.prjID);
			prepStmt.setString(3, tnInfo.participant);
			prepStmt.setString(4, tnInfo.waiver);
			prepStmt.setString(5, tnInfo.topic);
			prepStmt.setString(6, tnInfo.duration);
			prepStmt.executeUpdate();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	/**
	* uppdate TRAINING_PLAN
	* @param:Training
	* return boolean
	*/
	public static final boolean updateTraining(final TrainingInfo tnInfo) {
		String updateStatement = "";
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			updateStatement =
				" UPDATE TRAINING_PLAN "
					+ " SET duration = ?,"
					+ " PARTICIPANTS = ?,"
					+ " WAIVER_CRITERIA = ?,"
					+ " TOPIC = ?"
					+ " WHERE TRAINING_PLAN_ID = ?";
			prepStmt = conn.prepareStatement(updateStatement);
			prepStmt.setString(1, tnInfo.duration);
			prepStmt.setString(2, tnInfo.participant);
			prepStmt.setString(3, tnInfo.waiver);
			prepStmt.setString(4, tnInfo.topic);
			prepStmt.setLong(5, tnInfo.trainingID);
			final int rowCount = prepStmt.executeUpdate();
			if (rowCount == 0) {
				//System.err.println("Update TRAINING " + tnInfo.trainingID + " failed.");
				return false;
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	/**
	* Delete one Training from TRAINING_PLAN
	* @param:TrainingID
	* Return boolean
	*/
	public static final boolean deleteTraining(final long trainingId) {
		return Db.delete(trainingId, "TRAINING_PLAN_ID", "TRAINING_PLAN");
	}
	/**
	* return recordset of TOOL
	* param ProjectId
	*/
	public static final Vector getToolList(final long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		final Vector vt = new Vector();
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM TOOLS WHERE PROJECT_PLAN_ID=" + prjID + " ORDER BY TOOL_TYPE, TOOL_ID";
			rs = stm.executeQuery(sql);
			if (rs == null)
				return null;
			while (rs.next()) {
				final ToolInfo tlInfo = new ToolInfo();
				tlInfo.toolID = rs.getLong("TOOL_ID");
				tlInfo.prjID = rs.getLong("PROJECT_PLAN_ID");
				tlInfo.name = rs.getString("NAME");
				tlInfo.purpose = rs.getString("PURPOSE");
				tlInfo.source = rs.getString("SOURCE");
				tlInfo.description = rs.getString("DESCRIPTION");
				tlInfo.status = rs.getString("STATUS");
				tlInfo.dueD = rs.getDate("DUE_DATE");
				tlInfo.actualD = rs.getDate("ACTUAL_DATE");
				tlInfo.note = rs.getString("NOTE");
				tlInfo.tool_type = rs.getLong("TOOL_TYPE");
				tlInfo.expected_available_stage = rs.getString("EXPECTED_AVAILABILITY_STAGE");
				tlInfo.actual_available_stage = rs.getString("ACTUAL_AVAILABILITY_STAGE");
				vt.add(tlInfo);
			}
			return vt;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
		return null;
	}
	/**
	* Get one TOOL for a project
	* @Param: ProjectID, TOOLID
	*/
	public static final ToolInfo getTool(final long toolID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM TOOLS WHERE TOOL_ID=" + toolID;
			rs = stm.executeQuery(sql);
			if (rs == null)
				return null;
			if (rs.next()) {
				final ToolInfo tlInfo = new ToolInfo();
				tlInfo.toolID = rs.getLong("TOOL_ID");
				tlInfo.prjID = rs.getLong("PROJECT_PLAN_ID");
				tlInfo.name = rs.getString("NAME");
				tlInfo.purpose = rs.getString("PURPOSE");
				tlInfo.source = rs.getString("SOURCE");
				tlInfo.description = rs.getString("DESCRIPTION");
				tlInfo.status = rs.getString("STATUS");
				tlInfo.expected_available_stage = rs.getString("EXPECTED_AVAILABILITY_STAGE");
				tlInfo.actual_available_stage = rs.getString("ACTUAL_AVAILABILITY_STAGE");
				tlInfo.note = rs.getString("NOTE");
				tlInfo.tool_type = rs.getLong("TOOL_TYPE");
				return tlInfo;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
		return null;
	}
	/**
	*  Add new Tool to TOOL
	* @param:toolInfo
	* return boolean
	*/
	public static final boolean addTool(final ToolInfo tlInfo) {
		String insertStatement = "";
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = ServerHelper.instance().getConnection();
			insertStatement =
				" INSERT INTO TOOLS ("
					+ " TOOL_ID,"
					+ " PROJECT_PLAN_ID,"
					+ " NAME,"
					+ " PURPOSE,"
					+ " SOURCE,"
					+ " DESCRIPTION,"
					+ " STATUS,"
					+ " EXPECTED_AVAILABILITY_STAGE,"					
					+ " NOTE, TOOL_TYPE)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
			stm = conn.prepareStatement(insertStatement);
			stm.setLong(1, makeToolID());
			stm.setLong(2, tlInfo.prjID);
			stm.setString(3, tlInfo.name);
			stm.setString(4, tlInfo.purpose);
			stm.setString(5, tlInfo.source);
			stm.setString(6, tlInfo.description);
			stm.setString(7, tlInfo.status);
			stm.setString(8, tlInfo.expected_available_stage);			
			stm.setString(9, tlInfo.note);
			stm.setLong(10, tlInfo.tool_type);
			stm.executeUpdate();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,null);
		}
		return false;
	}
	/**
	* uppdate TOOL
	* @param:ToolInfo
	* return boolean
	*/
	public static final boolean updateTool(final ToolInfo tlInfo) {
		String updateStatement = "";
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = ServerHelper.instance().getConnection();
			updateStatement =
				" UPDATE TOOLS "
					+ " SET NAME = ?,"
					+ " PURPOSE = ?,"
					+ " SOURCE = ?,"
					+ " DESCRIPTION = ?,"
					+ " STATUS = ? ,"
					+ " EXPECTED_AVAILABILITY_STAGE = ?,"
					+ " ACTUAL_AVAILABILITY_STAGE = ?,"
					+ " NOTE= ?,"
					+ " TOOL_TYPE = ?"
					+ " WHERE TOOL_ID = ?";
			stm = conn.prepareStatement(updateStatement);
			stm.setString(1, tlInfo.name);
			stm.setString(2, tlInfo.purpose);
			stm.setString(3, tlInfo.source);
			stm.setString(4, tlInfo.description);
			stm.setString(5, tlInfo.status);
			stm.setString(6, tlInfo.expected_available_stage);
			stm.setString(7, tlInfo.actual_available_stage);
			stm.setString(8, tlInfo.note);
			stm.setLong(9, tlInfo.tool_type);
			stm.setLong(10, tlInfo.toolID);
			final int rowCount = stm.executeUpdate();
			if (rowCount == 0) {
				return false;
			}
			stm.executeUpdate();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
		return false;
	}
	/**
	* Delete one Tool from TOOL
	* @param:ToolID
	* Return boolean
	*/
	public static final boolean deleteTool(final long toolId) {
		return Db.delete(toolId, "TOOL_ID", "TOOLS");
	}
	/**
	* return recordset of FINANCIAL_PLAN
	* param ProjectId
	*/
	public static final Vector getFinanList(final long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		final Vector vt = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM FINANCIAL_PLAN WHERE PROJECT_PLAN_ID=" + prjID + " ORDER BY DUE_DATE DESC";
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				final FinancialInfo fnInfo = new FinancialInfo();
				fnInfo.finanID = rs.getLong("INVOICE_SCHEDULE_ID");
				fnInfo.prjID = rs.getLong("PROJECT_PLAN_ID");
				fnInfo.dueD = rs.getDate("DUE_DATE");
				fnInfo.value = rs.getDouble("VALUE");
				fnInfo.condition = rs.getString("CONDITIONS");
				fnInfo.item = rs.getString("ITEM");
				fnInfo.actualD = rs.getDate("ACTUAL_DATE");
				fnInfo.type = rs.getLong("TYPE");
				vt.add(fnInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return vt;
		}
	}
	/**
	* Get one FINANCIAL_PLAN for a project
	* @Param: ProjectID, FINANCIAL_PLAN_ID
	*/
	public static final FinancialInfo getFinan(final long toolID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		FinancialInfo fnInfo = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM FINANCIAL_PLAN WHERE INVOICE_SCHEDULE_ID=" + toolID;
			rs = stm.executeQuery(sql);
			if (rs.next()) {
				fnInfo = new FinancialInfo();
				fnInfo.finanID = rs.getLong("INVOICE_SCHEDULE_ID");
				fnInfo.prjID = rs.getLong("PROJECT_PLAN_ID");
				fnInfo.dueD = rs.getDate("DUE_DATE");
				fnInfo.value = rs.getDouble("VALUE");
				fnInfo.condition = rs.getString("CONDITIONS");
				fnInfo.item = rs.getString("ITEM");
				fnInfo.actualD = rs.getDate("ACTUAL_DATE");
				fnInfo.type = rs.getLong("TYPE");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return fnInfo;
		}
	}
	/**
	*  Add new Financial_Plan to FINANCIAL_PLAN
	* @param:Financial_PlanInfo
	* return boolean
	*/
	public static final boolean addFinan(final FinancialInfo fnInfo) {
		String insertStatement = "";
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = ServerHelper.instance().getConnection();
			insertStatement =
				" INSERT INTO FINANCIAL_PLAN ("
					+ " INVOICE_SCHEDULE_ID,"
					+ " PROJECT_PLAN_ID,"
					+ " DUE_DATE,"
					+ " VALUE,"
					+ " CONDITIONS,"
					+ " ITEM,"
					+ " ACTUAL_DATE,"
					+ " TYPE)"
					+ " VALUES (NVL((SELECT MAX(INVOICE_SCHEDULE_ID)+1 FINAN_ID FROM FINANCIAL_PLAN),1),?,?,?,?,?,?,?)";
			stm = conn.prepareStatement(insertStatement);
			stm.setLong(1, fnInfo.prjID);
			stm.setDate(2, fnInfo.dueD);
			stm.setDouble(3, fnInfo.value);
			stm.setString(4, fnInfo.condition);
			stm.setString(5, fnInfo.item);
			stm.setDate(6, fnInfo.actualD);
			stm.setLong(7, fnInfo.type);
			stm.executeUpdate();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	/**
	* uppdate FINANCIAL_PLAN
	* @param:Financial_PlanInfo
	* return boolean
	*/
	public static final boolean updateFinan(final FinancialInfo fnInfo) {
		String updateStatement = "";
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = ServerHelper.instance().getConnection();
			updateStatement =
				" UPDATE FINANCIAL_PLAN "
					+ " SET DUE_DATE = ?,"
					+ " VALUE = ?,"
					+ " CONDITIONS = ?,"
					+ " ITEM = ? ,"
					+ " ACTUAL_DATE = ?,"
					+ " TYPE= ?"
					+ " WHERE INVOICE_SCHEDULE_ID = ?";
			stm = conn.prepareStatement(updateStatement);
			stm.setDate(1, fnInfo.dueD);
			stm.setDouble(2, fnInfo.value);
			stm.setString(3, fnInfo.condition);
			stm.setString(4, fnInfo.item);
			stm.setDate(5, fnInfo.actualD);
			stm.setLong(6, fnInfo.type);
			stm.setLong(7, fnInfo.finanID);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	/**
	* Delete one financial_plan from FINANCIAL_PLAN
	* @param:financial_planID
	* Return boolean
	*/
	public static final boolean deleteFinan(final long finanId) {
		return Db.delete(finanId, "INVOICE_SCHEDULE_ID", "FINANCIAL_PLAN");
	}
	/**
	* return recordset of PROJECT_COST
	* param ProjectId
	*/
	public static final Vector getCostList(final long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		final Vector vt = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM PROJECT_COST WHERE PROJECT_PLAN_ID=" + prjID + " ORDER BY PROJECT_COST_ID";
			final ResultSet rs = stm.executeQuery(sql);
			if (rs == null)
				return null;
			while (rs.next()) {
				final CostInfo costInfo = new CostInfo();
				costInfo.costID = rs.getLong("PROJECT_COST_ID");
				costInfo.prjID = rs.getLong("PROJECT_PLAN_ID");
				costInfo.act = rs.getString("ACTIVITIES");
				costInfo.type = rs.getInt("TYPE");
				costInfo.effort = rs.getDouble("EFFORT");
				costInfo.cost = rs.getDouble("COST");
				vt.add(costInfo);
			}
			return vt;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	/**
	* Get one PROJECT_COST for a project
	* @Param: Project_Cost_ID
	*/
	public static final CostInfo getCost(final long costID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		CostInfo costInfo = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM PROJECT_COST WHERE PROJECT_COST_ID=" + costID;
			rs = stm.executeQuery(sql);
			if (rs.next()) {
				costInfo = new CostInfo();
				costInfo.costID = rs.getLong("PROJECT_COST_ID");
				costInfo.prjID = rs.getLong("PROJECT_PLAN_ID");
				costInfo.act = rs.getString("ACTIVITIES");
				costInfo.type = rs.getInt("TYPE");
				costInfo.effort = rs.getDouble("EFFORT");
				costInfo.cost = rs.getDouble("COST");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return costInfo;
		}
	}
	/**
	* Add new Cost to PROJECT_COST
	* @param:CostInfo
	* @return boolean
	*/
	public static final boolean addCost(final CostInfo costInfo) {
		String insertStatement = "";
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = ServerHelper.instance().getConnection();
			insertStatement =
				" INSERT INTO PROJECT_COST ("
					+ " PROJECT_COST_ID,"
					+ " PROJECT_PLAN_ID,"
					+ " ACTIVITIES,"
					+ " TYPE,"
					+ " EFFORT,"
					+ " COST)"
					+ " VALUES (NVL((SELECT MAX(PROJECT_COST_ID)+1 FROM PROJECT_COST),1),?,?,?,?,?)";
			stm = conn.prepareStatement(insertStatement);
			stm.setLong(1, costInfo.prjID);
			stm.setString(2, costInfo.act);
			stm.setInt(3, costInfo.type);
			stm.setDouble(4, costInfo.effort);
			stm.setDouble(5, costInfo.cost);
			stm.executeUpdate();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	/**
	* uppdate PROJECT_COST
	* @param:CostInfo
	* return boolean
	*/
	public static final boolean updateCost(final CostInfo costInfo) {
		String updateStatement = "";
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = ServerHelper.instance().getConnection();
			updateStatement =
				" UPDATE PROJECT_COST "
					+ " SET ACTIVITIES = ?,"
					+ " TYPE = ?,"
					+ " EFFORT = ?,"
					+ " COST = ? "
					+ " WHERE PROJECT_COST_ID = ?";
			stm = conn.prepareStatement(updateStatement);
			stm.setString(1, costInfo.act);
			stm.setInt(2, costInfo.type);
			stm.setDouble(3, costInfo.effort);
			stm.setDouble(4, costInfo.cost);
			stm.setLong(5, costInfo.costID);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	/**
	 * Delete one Cost from PROJECT_COST
	 * @param:Project_Cost_ID
	 * Return boolean
	 */
	public static final boolean deleteCost(final long finanId) {
		return Db.delete(finanId, "PROJECT_COST_ID", "PROJECT_COST");
	}
	/**
	 *
	 * Get one COST_TOTAL for a project
	 * @Param: Project_ID
	 */
	public static final CostTotalInfo getCostTotal(final long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT SUM(EFFORT) EFFORT_SUM, SUM(COST) COST_SUM FROM PROJECT_COST WHERE PROJECT_PLAN_ID=" + prjID;
			rs = stm.executeQuery(sql);
			if (rs == null)
				return null;
			final CostTotalInfo totalInfo = new CostTotalInfo();
			if (rs.next()) {
				totalInfo.effort = rs.getDouble("EFFORT_SUM");
				totalInfo.cost = rs.getDouble("COST_SUM");
			}
			rs = null;
			sql = "SELECT SUM(COST) LABOUR_SUM FROM PROJECT_COST WHERE TYPE=1 AND PROJECT_PLAN_ID=" + prjID;
			rs = stm.executeQuery(sql);
			if (rs.next()) {
				totalInfo.labour = rs.getDouble("LABOUR_SUM");
				totalInfo.n_labour = totalInfo.cost - totalInfo.labour;
			}
			return totalInfo;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	/**
	 * Delete one SUBCONTRACT
	 * @param:financial_planID
	 * Return boolean
	 */
	public static final boolean deleteSubcontract(final long subcontractID) {
		return Db.delete(subcontractID, "SUBCONTRACT_ID", "SUBCONTRACT");
	}
	/**
	 * update data for consistency between Dashboard and FSOFT Insight
	 * @param projectDatabaseInfo - data retrieved frim FSOFT Insight weekly report
	 * @throws SQL Exception - if occurs
	 */
	public static final void updateProjectDatabase(final ProjectDatabaseInfo pdInfo) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			sql =
				"UPDATE PROJECT SET "
					+ "FATALPENDINGDEFECT = ?,"
					+ "SERIOUSPENDINGDEFECT = ?,"
					+ "MEDIUMPENDINGDEFECT = ?,"
					+ "COSMETICPENDINGDEFECT = ?,"
					+ "TOTALFATALDEFECT = ?,"
					+ "TOTALSERIOUSDEFECT = ?,"
					+ "TOTALMEDIUMDEFECT = ?,"
					+ "TOTALCOSMETICDEFECT = ?,"
					+ "PER_COMPLETE = ?,"
					+ "ACTUAL_EFFORT = ?,"
					+ "TOTALDEFECT = ?,"
					+ "TOTALWEIGHTEDDEFECT = ?,"
					+ "TOTALREQUIREMENT = ?,"
					+ "COMMITTEDREQUIREMENT = ?,"
					+ "DESIGNEDREQUIREMENT = ?,"
					+ "CODEDREQUIREMENT = ?,"
					+ "TESTEDREQUIREMENT = ?,"
					+ "DEPLOYEDREQUIREMENT = ?,"
					+ "ACCEPTEDREQUIREMENT = ?,"
					+ "EFFORT_STATUS = ?,"
					+ "SCHEDULE_STATUS = ?,"
					+ "LAST_UPDATE = ?"
					+ " WHERE PROJECT_ID = ?";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, pdInfo.FatalPendingDefect);
			prepStmt.setInt(2, pdInfo.SeriousPendingDefect);
			prepStmt.setInt(3, pdInfo.MediumPendingDefect);
			prepStmt.setInt(4, pdInfo.CosmeticPendingDefect);
			prepStmt.setInt(5, pdInfo.TotalFatalDefect);
			prepStmt.setInt(6, pdInfo.TotalSeriousDefect);
			prepStmt.setInt(7, pdInfo.TotalMediumDefect);
			prepStmt.setInt(8, pdInfo.TotalCosmeticDefect);
			prepStmt.setInt(9, pdInfo.PercentComplete);
			Db.setDouble(prepStmt, 10, pdInfo.Effort);
			prepStmt.setInt(11, pdInfo.TotalWeightedPendingDefect);
			prepStmt.setInt(12, pdInfo.TotalWeightedDefect);
			prepStmt.setInt(13, pdInfo.TotalRequirement);
			prepStmt.setInt(14, pdInfo.CommittedRequirement);
			prepStmt.setInt(15, pdInfo.DesignedRequirement);
			prepStmt.setInt(16, pdInfo.CodedRequirement);
			prepStmt.setInt(17, pdInfo.TestedRequirement);
			prepStmt.setInt(18, pdInfo.DeployedRequirement);
			prepStmt.setInt(19, pdInfo.AcceptedRequirement);
			Db.setDouble(prepStmt, 20, pdInfo.EffortStatus);
			Db.setDouble(prepStmt, 21, pdInfo.ScheduleStatus);
			prepStmt.setDate(22, pdInfo.lastUpdated);
			prepStmt.setLong(23, pdInfo.projectID);
			prepStmt.executeUpdate();
			prepStmt.close();
			sql = "DELETE OPENISSUE WHERE PROJECT_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, pdInfo.projectID);
			prepStmt.executeUpdate();
			prepStmt.close();
			if (pdInfo.issueList.size() > 0) {
				sql = "INSERT INTO OPENISSUE" + "(PROJECT_ID,DESCRIPTION,STATUS,STARTDATE,ENDDATE) VALUES(?,?,?,?,?)";
				//conn.setAutoCommit(false);
				prepStmt = conn.prepareStatement(sql);
				for (int i = 0; i < pdInfo.issueList.size(); i++) {
					IssueInfo issue = (IssueInfo)pdInfo.issueList.elementAt(i);
					prepStmt.setLong(1, pdInfo.projectID);
					prepStmt.setString(2, issue.description);
					prepStmt.setString(3, issue.getStatusName().substring(0, 1));
					prepStmt.setDate(4, new java.sql.Date(issue.startDate.getTime()));
					prepStmt.setDate(5, new java.sql.Date(issue.dueDate.getTime()));
					prepStmt.executeUpdate();
				}
				prepStmt.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public static final int INPROGRESS_PROJECTS = 0;
	public static final int CLOSEDPROJECTS = 1;
	public static final int ALLPROJECTS = 2;
	/**
	 * In case change is needed,
	 *  please be carefull to update the ProjectInfo constructor as well
	 * note :tentative and cacelled projects are not returned
	 */
	public static Vector getChildProjectsByWU(long workUnitID, java.sql.Date actualStart, java.sql.Date actualEnd, int projectType) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		Vector projectInfList = new Vector();
		try {
			String dateConstraint = null;
			if (projectType == INPROGRESS_PROJECTS) {
				dateConstraint = " AND P.START_DATE <= ?";
				if (actualStart != null)
					dateConstraint = " AND (P.ACTUAL_FINISH_DATE IS NULL OR P.ACTUAL_FINISH_DATE >= ?)" + dateConstraint;
			}
			else if (projectType == CLOSEDPROJECTS)
				dateConstraint =
					((actualStart != null) ? " AND P.ACTUAL_FINISH_DATE >=?" : "") + " AND P.ACTUAL_FINISH_DATE <=? ";
			sql =
				"SELECT a.PARENTWORKUNITID p1, b.PARENTWORKUNITID p2,"
					+ " P.TYPE, P.PROJECT_ID, P.NAME PROJECTNAME, code, group_name, START_DATE,"
					+ " per_complete, leader, base_finish_date, plan_finish_date,"
					+ " actual_finish_date, base_effort, plan_effort, actual_effort,"
					+ " base_billable_effort,plan_billable_effort,actual_billable_effort,"
					+ " plan_calendar_effort,replan_calendar_effort,"
					+ " description, status, schedule_status, effort_status,"
					+ " p.last_update, category, customer, plan_start_date, a.WORKUNITID p3"
					+ " FROM WORKUNIT a,WORKUNIT b, PROJECT P, Project_Plan pp"
					+ " WHERE a.type(+)= 2 AND "
					+ " P.PROJECT_ID = a.TABLEID (+) "
					+ " AND P.PROJECT_ID=PP.PROJECT_ID(+)"
					+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
					+ " AND (a.PARENTWORKUNITID=? OR b.PARENTWORKUNITID=?)"
					+ ((dateConstraint == null) ? "" : dateConstraint)
					+ " AND P.STATUS <> "
					+ ProjectInfo.STATUS_CANCELLED
					+ " AND P.STATUS <> "
					+ ProjectInfo.STATUS_TENTATIVE
					+ " ORDER BY UPPER(code)";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, workUnitID);
			stm.setLong(2, workUnitID);
			//if dates specified, then the project must be started
			int i = 3;
			if (dateConstraint != null) {
				if (actualStart != null) {
                    stm.setDate(i++, actualStart);
				}
				stm.setDate(i, actualEnd);
			}
			rs = stm.executeQuery();
			while (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.setProjectName(rs.getString("PROJECTNAME"));
				projectInfo.setProjectId(rs.getLong("PROJECT_ID"));
				projectInfo.setPlanStartDate(rs.getDate("plan_start_date"));
				projectInfo.setBaseFinishDate(rs.getDate("base_finish_date"));
				projectInfo.setPlanFinishDate(rs.getDate("plan_finish_date"));
				projectInfo.setActualFinishDate(rs.getDate("actual_finish_date"));
				projectInfo.setStartDate(rs.getDate("START_DATE"));
				projectInfo.setProjectCode(rs.getString("code"));
				projectInfo.setGroupName(rs.getString("group_name"));
				projectInfo.setCustomer(rs.getString("customer"));
                projectInfo.setDescription("description");
				projectInfo.setParent(rs.getLong("p1"));
				projectInfo.setGrandParent(rs.getLong("p2"));
				projectInfo.setProjectType(ProjectInfo.parseType(rs.getString("type")));
				projectInfo.setLifecycleId(Integer.parseInt(rs.getString("category")));
				projectInfo.setLifecycle(ProjectInfo.parseLifecycle(rs.getString("category")));
				projectInfo.setBaseEffort(Db.getDouble(rs, "base_effort"));
				projectInfo.setPlanEffort(Db.getDouble(rs, "plan_effort"));
				projectInfo.setPlannedEffort((Double.isNaN(projectInfo.getPlanEffort())) ? projectInfo.getBaseEffort() : projectInfo.getPlanEffort());
				projectInfo.setPlannedFinishDate((projectInfo.getPlanFinishDate() == null) ? projectInfo.getBaseFinishDate() : projectInfo.getPlanFinishDate());
				// Add billable Effort
				projectInfo.setBaseBillableEffort(Db.getDouble(rs, "base_billable_effort"));
				projectInfo.setPlanBillableEffort(Db.getDouble(rs, "plan_billable_effort"));
				projectInfo.setActualBillableEffort(Db.getDouble(rs, "actual_billable_effort"));
				// Calendar effort
				projectInfo.setPlannedBillableEffort(
					(Double.isNaN(projectInfo.getPlanBillableEffort())) ?
					projectInfo.getBaseBillableEffort() : projectInfo.getPlanBillableEffort());

				projectInfo.setPlanCalendarEffort(Db.getDouble(rs, "plan_calendar_effort"));
				projectInfo.setReplanCalendarEffort(Db.getDouble(rs, "replan_calendar_effort"));
				projectInfo.setPlannedCalendarEffort(
					(Double.isNaN(projectInfo.getReplanCalendarEffort())) ?
					projectInfo.getPlanCalendarEffort() : projectInfo.getReplanCalendarEffort());
				projectInfo.setLeader(rs.getString("leader"));
				projectInfo.setWorkUnitId(rs.getLong("p3"));
				projectInfList.add(projectInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return projectInfList;
		}
	}
    
	public static final boolean isWOConsistent(long prjID) {
		ProjectInfo projectInfo = Project.getProjectInfo(prjID);
		return (projectInfo.getPlanStartDate() != null && projectInfo.getStartDate() != null && projectInfo.getBaseFinishDate() != null);
	}
	/**
	* used for project selection page
	* returns a vector of ProjectInfo, but only some fields used for filters are filled
	*/
	
	public static Vector getExternalProjectsIDList(String userName) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		Vector projectIDList = new Vector();		
		try {				
			conn = ServerHelper.instance().getConnection();			
			sql =
				  " SELECT PROJECT_ID FROM PROJECT_PLAN " 
				+ " WHERE PROJECT_PLAN_ID IN "
				+ " ( SELECT PROJECT_PLAN_ID FROM INTERFACE WHERE UPPER(CONTACT_ACC) = '"+userName.toUpperCase()+"' ) ";					
        
			stm = conn.prepareStatement(sql);			
			rs = stm.executeQuery();
			while (rs.next()) {
				projectIDList.add(""+rs.getLong("PROJECT_ID"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return projectIDList;
		}
	}
	
	public static Vector getProjectsByWUsAndEx(Vector projectRoles, Vector exPrjIDList) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		Vector projectInfList = new Vector();
		Hashtable hashWorkUnitID = new Hashtable();
		try {
			// to void error "maximum number of expressions in a list is 1000" which happen when we use
			// condition "IN" a String, we use Hashtable to store all data which you need check.
			hashWorkUnitID = Project.putProjectIDHashTable(projectRoles);
			StringBuffer prjList = new StringBuffer();			
			for(int i = 0; i < exPrjIDList.size(); i++) {
				prjList.append((String) exPrjIDList.elementAt(i)+",");
			}
			if (prjList.length() > 0) prjList.deleteCharAt(prjList.length()-1);
			
			conn = ServerHelper.instance().getConnection();
			
			sql =
			" SELECT PROJECT_ID, TYPE, CATEGORY, CODE, GROUP_NAME, STATUS, RANK,"
				+ "  WORKUNITID, PARENTWORKUNITID,"
				+ "  LEADER, CUSTOMER, ACTUAL_FINISH_DATE, START_DATE"
			+ " FROM "
			+ "("
				+ " SELECT PROJECT.PROJECT_ID, PROJECT.TYPE, CATEGORY, CODE, GROUP_NAME, STATUS, RANK,"
				+ " A.WORKUNITID, A.PARENTWORKUNITID,"
				+ " LEADER, CUSTOMER, ACTUAL_FINISH_DATE, START_DATE"
				+ " FROM WORKUNIT A, PROJECT"
				+ " WHERE A.TYPE= 2"
				+ " AND PROJECT.PROJECT_ID = A.TABLEID"

				+ " UNION"

				+ " SELECT PROJECT.PROJECT_ID, PROJECT.TYPE, CATEGORY, CODE, GROUP_NAME, STATUS, RANK,"
				+ " A.WORKUNITID, A.PARENTWORKUNITID,"
				+ " LEADER, CUSTOMER, ACTUAL_FINISH_DATE, START_DATE"
				+ " FROM WORKUNIT A, PROJECT"
				+ " WHERE A.TYPE= 2"
				+ " AND PROJECT.PROJECT_ID = A.TABLEID"
				+ " AND PROJECT.PROJECT_ID IN" 
				+ "("+prjList.toString()+")"			 
			+ ") "
			+ "ORDER BY UPPER(CODE)";
            
			stm = conn.prepareStatement(sql);			
			rs = stm.executeQuery();
			while (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.setWorkUnitId(rs.getLong("WORKUNITID"));
				projectInfo.setExternalStatus(false);
				Long lWorkUnitId = new Long(projectInfo.getWorkUnitId());
				if (hashWorkUnitID.get(lWorkUnitId) == null){
					// this WorkUnitID is not exist in Hashtable so we don't consider this WorkUnit
					if (!exPrjIDList.contains(""+rs.getLong("PROJECT_ID"))) {						
						continue;	
					} else projectInfo.setExternalStatus(true);
					
				}
				projectInfo.setProjectType(ProjectInfo.parseType(rs.getString("TYPE")));
				projectInfo.setProjectRank(rs.getString("RANK"));
				projectInfo.setLifecycleId(Integer.parseInt(rs.getString("CATEGORY")));
				projectInfo.setLifecycle(ProjectInfo.parseLifecycle(projectInfo.getLifecycleId()));
				projectInfo.setProjectCode(rs.getString("CODE"));
				projectInfo.setGroupName(rs.getString("GROUP_NAME"));
				projectInfo.setStatus(rs.getInt("STATUS"));
				projectInfo.setParent(rs.getLong("PARENTWORKUNITID"));
				projectInfo.setLeader(rs.getString("LEADER"));
				projectInfo.setCustomer(rs.getString("CUSTOMER"));
				projectInfo.setActualFinishDate(rs.getDate("ACTUAL_FINISH_DATE"));
				projectInfo.setActualStartDate(rs.getDate("START_DATE"));
				projectInfList.add(projectInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return projectInfList;
		}
	}
	
	
	public static Vector getProjectsByWUs(Vector projectRoles) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		Vector projectInfList = new Vector();
		Hashtable hashWorkUnitID = new Hashtable();
		try {
			// to void error "maximum number of expressions in a list is 1000" which happen when we use
			// condition "IN" a String, we use Hashtable to store all data which you need check.
			hashWorkUnitID = Project.putProjectIDHashTable(projectRoles);
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql =
				"SELECT PROJECT.TYPE, CATEGORY, CODE, GROUP_NAME, STATUS, RANK," 
					+  "a.WORKUNITID, a.PARENTWORKUNITID,"
					+ " LEADER, CUSTOMER, ACTUAL_FINISH_DATE, START_DATE"
					+ " FROM WORKUNIT a, PROJECT"
					+ " WHERE a.TYPE= 2 "
					+ " AND PROJECT.PROJECT_ID = a.TABLEID ORDER BY UPPER(CODE)";
            
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.setWorkUnitId(rs.getLong("WORKUNITID"));

				Long lWorkUnitId = new Long(projectInfo.getWorkUnitId());
				if (hashWorkUnitID.get(lWorkUnitId) == null){
					// this WorkUnitID is not exist in Hashtable so we don't consider this WorkUnit
					continue;
				}
				projectInfo.setProjectType(ProjectInfo.parseType(rs.getString("TYPE")));
				projectInfo.setProjectRank(rs.getString("RANK"));
				projectInfo.setLifecycleId(Integer.parseInt(rs.getString("CATEGORY")));
				projectInfo.setLifecycle(ProjectInfo.parseLifecycle(projectInfo.getLifecycleId()));
				projectInfo.setProjectCode(rs.getString("CODE"));
				projectInfo.setGroupName(rs.getString("GROUP_NAME"));
				projectInfo.setStatus(rs.getInt("STATUS"));
				projectInfo.setParent(rs.getLong("PARENTWORKUNITID"));
				projectInfo.setLeader(rs.getString("LEADER"));
				projectInfo.setCustomer(rs.getString("CUSTOMER"));
				projectInfo.setActualFinishDate(rs.getDate("ACTUAL_FINISH_DATE"));
				projectInfo.setActualStartDate(rs.getDate("START_DATE"));
				projectInfList.add(projectInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return projectInfList;
		}
	}
	
	/* HuyNH2 add some line code for project archive 
	 * get project with status is
	 *      + close
	 *      + cancel
	 *      + archive 
	 * 
	 * */
	public static boolean archive(final long prjID,final long devID){
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		boolean returnValue = true;
		int archive_status = 0;        
		try{            
			String statusBeforeArchive = "";
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false); 
			//check project_id exist in project_archive.
			 //cache actual_effort.
			 Effort.cacheActualEffort(prjID);
			 //cache cacheEffort_type
			 Effort.cacheEffortType(prjID);
			 //cache ACTUAL_MAN_EFFORT
			 Effort.cacheActualEffortByProcess(prjID,1);
			 //cache ACTUAL_DEV_EFFORT
			 Effort.cacheActualEffortByProcess(prjID,2);
			 //cache CORRECTION_COST
			 Effort.cacheActualEffortByProcess(prjID,3);
			 //cache TRANS_COST
			 Effort.cacheActualEffortByProcess(prjID,4);
			 // archive timesheet
			 sql = 
				"INSERT INTO " 
					+ " timesheet_archive "
					+ " ( "
					+ "         timesheet_id, "
					+ "         developer_id, "
					+ "         project_id, "
					+ "         create_date, "
					+ "         occur_date, "
					+ "         duration, "
					+ "         status, "
					+ "         kpa_id, "
					+ "         tow_id,  "
					+ "         process_id, " 
					+ "         wp_id, "
					+ "         approved_by_leader, " 
					+ "         approved_by_sepg,  "
					+ "         description,  "
					+ "         rcomment )  "
					+ " SELECT  "
					+ "         timesheet_id, "
					+ "         developer_id, "
					+ "         project_id, "
					+ "         create_date, "
					+ "         occur_date, "
					+ "         duration, "
					+ "         status, "
					+ "         kpa_id, "
					+ "         tow_id,  "
					+ "         process_id, " 
					+ "         wp_id,  "
					+ "         approved_by_leader, " 
					+ "         approved_by_sepg,  "
					+ "         description,  "
					+ "         rcomment  "
					+ "      FROM timesheet WHERE  project_id = ? " ;
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			stm.executeUpdate();
			stm.close();
			// delete from timesheet
			sql = "DELETE FROM TIMESHEET WHERE project_id = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			stm.executeUpdate();
			stm.close();                
			// remove defect to defect_archive
			sql =                
			  " INSERT INTO defect_archive "
			+ "         (defect_id, project_id, created_by, create_date, updated_by, "
			+ "          title, assigned_to, ds_id, wp_id, defs_id, module_id, at_id, "
			+ "          dp_id, dt_id, qa_id, process_id, sd_id, si_id, due_date, "
			+ "          close_date, description, solution, project_origin, image, "
			+ "          REFERENCE, fixed_date, cause_analysis, test_case) "
			+ " SELECT defect_id, project_id, created_by, create_date, updated_by, title, "
			+ "       assigned_to, ds_id, wp_id, defs_id, module_id, at_id, dp_id, dt_id, "
			+ "       qa_id, process_id, sd_id, si_id, due_date, close_date, description, "
			+ "       solution, project_origin, image, REFERENCE, fixed_date, "
			+ "       cause_analysis, test_case "
			+ "  FROM defect "
			+ " WHERE project_id = ? ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			stm.executeUpdate();
			stm.close();
			// delete from timesheet
			sql = "DELETE FROM DEFECT WHERE project_id = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			stm.executeUpdate();
			stm.close();                
			/*
			 * update project archive status
			 */
			sql = "select archive_status,status from project where project_id = ?"; 
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				archive_status = rs.getInt("archive_status");
				if(rs.getInt("status") == 1){
					statusBeforeArchive = "Closed";
				}
				else if(rs.getInt("status") == 2){
					statusBeforeArchive = "Cancelled";
				}    
                
			}
			rs.close();
			stm.close();
			if(archive_status==0){
				sql = "update project set archive_status = 4 where project_id=?";
				stm = conn.prepareStatement(sql);
				stm.setLong(1,prjID);
				stm.execute();                
			}else{
				returnValue = false;
			}             
			/*
			 * insert to history 
			 */
			 long project_archive_history_id = ServerHelper.getNextSeq("project_archive_history_seq");
			 sql = "insert into project_archive_history(Project_Archive_History_ID,project_id,Project_Archive_Status,Developer_Id,Effect_Date,Description) values( ";
			 sql += " ?,?,'Archive',?,SYSDATE,'"+ "change from " + statusBeforeArchive +" to archived')" ;
			 stm = conn.prepareStatement(sql);
			 stm.setLong(1,project_archive_history_id);
			 stm.setLong(2,prjID);
			 stm.setLong(3,devID);
			 stm.execute();
			/*
			 * after sucess commit
			 */ 
			if(returnValue){  
				conn.commit();
			}
			else{            
				conn.rollback();
			}
		}
		catch (Exception e) {
			conn.rollback();
			returnValue = false;    
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {            
			ServerHelper.closeConnection(conn, stm, rs);
			return returnValue;
		}        
        
	}
	public static boolean restore(final long prjID,final long devID){
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int archive_status = 0;
		boolean returnValue = true;
		try{            
			String statusBeforeArchive = "";
			conn = ServerHelper.instance().getConnection();
			// set session autocommit            
			conn.setAutoCommit(false);
			/*
			 * Change project archive status;
			 */
			sql = "select archive_status,status from project where project_id = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				archive_status = rs.getInt("archive_status");
				if(rs.getInt("status") == 1){
					statusBeforeArchive = "Closed";
				}
				else if(rs.getInt("status") == 2){
					statusBeforeArchive = "Cancelled";
				}                                            
			}
			if(archive_status != 0 ){
				sql = "update project set archive_status = 0 where project_id=?";
				stm = conn.prepareStatement(sql);
				stm.setLong(1,prjID);
				stm.execute();                
			}else{
				returnValue = false;
			}             
			/*
			 * insert to history 
			 */
			 long project_archive_history_id = ServerHelper.getNextSeq("project_archive_history_seq");
			 sql = "insert into project_archive_history(Project_Archive_History_ID,project_id,Project_Archive_Status,Developer_Id,Effect_Date,Description) values( ";
			 sql += " ?,?,'Restore',?,SYSDATE,'"+ "change from archived to " + statusBeforeArchive +"')" ;
			 stm = conn.prepareStatement(sql);
			 stm.setLong(1,project_archive_history_id);
			 stm.setLong(2,prjID);
			 stm.setLong(3,devID);
			 stm.execute();
			 /*
			 * restore database
			 * 
			 */
			 // restore from database archive
			 // restore timesheet
			 sql = 
				"INSERT INTO " 
					+ " timesheet "
					+ " ( "
					+ "         timesheet_id, "
					+ "         developer_id, "
					+ "         project_id, "
					+ "         create_date, "
					+ "         occur_date, "
					+ "         duration, "
					+ "         status, "
					+ "         kpa_id, "
					+ "         tow_id,  "
					+ "         process_id, " 
					+ "         wp_id, "
					+ "         approved_by_leader, " 
					+ "         approved_by_sepg,  "
					+ "         description,  "
					+ "         rcomment )  "
					+ " SELECT  "
					+ "         timesheet_id, "
					+ "         developer_id, "
					+ "         project_id, "
					+ "         create_date, "
					+ "         occur_date, "
					+ "         duration, "
					+ "         status, "
					+ "         kpa_id, "
					+ "         tow_id,  "
					+ "         process_id, " 
					+ "         wp_id,  "
					+ "         approved_by_leader, " 
					+ "         approved_by_sepg,  "
					+ "         description,  "
					+ "         rcomment  "
					+ "      FROM timesheet_archive WHERE  project_id = ? " ;
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			stm.executeUpdate();
			stm.close();
			// delete from TIMESHEET_ARCHIVE 
			sql = "DELETE FROM TIMESHEET_ARCHIVE WHERE project_id = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			stm.executeUpdate();
			stm.close();
			// restore defect
			sql =                
			  " INSERT INTO defect "
			+ "         (defect_id, project_id, created_by, create_date, updated_by, "
			+ "          title, assigned_to, ds_id, wp_id, defs_id, module_id, at_id, "
			+ "          dp_id, dt_id, qa_id, process_id, sd_id, si_id, due_date, "
			+ "          close_date, description, solution, project_origin, image, "
			+ "          REFERENCE, fixed_date, cause_analysis, test_case) "
			+ " SELECT defect_id, project_id, created_by, create_date, updated_by, title, "
			+ "       assigned_to, ds_id, wp_id, defs_id, module_id, at_id, dp_id, dt_id, "
			+ "       qa_id, process_id, sd_id, si_id, due_date, close_date, description, "
			+ "       solution, project_origin, image, REFERENCE, fixed_date, "
			+ "       cause_analysis, test_case "
			+ "  FROM defect_archive "
			+ " WHERE project_id = ? ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			stm.executeUpdate();
			stm.close();
			// delete from defect_archive
			sql = "DELETE FROM DEFECT_ARCHIVE WHERE project_id = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			stm.executeUpdate();
			stm.close();                
			// remove Effort_type
			Effort.removeEffortType(prjID);
			if(returnValue)
				conn.commit();
			else
				conn.rollback();
		}
		catch (Exception e) {
			conn.rollback();
			returnValue = false;    
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return returnValue;
		}
	}
	/**
	 * put all WorkunitIDs which is projects into a HashTable
	 * @param projectRoles
	 * @return Hashtable
	 */
	public static Hashtable putProjectIDHashTable(Vector projectRoles){
		Hashtable hashWorkUnitID = new Hashtable();
		try{
			for (int i = 0; i < projectRoles.size(); i ++){
				RolesInfo roleInfo = (RolesInfo)projectRoles.elementAt(i);
				hashWorkUnitID.put(new Long(roleInfo.workUnitID), roleInfo);
			}
			return hashWorkUnitID;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return hashWorkUnitID;
		}
	}
	public static Vector getProjectsArchiveByWUs(Vector projectRoles) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		// Vector hashWorkUnitID = new Vector();
        
		Vector projectInfList = new Vector();
		try {
			// to void error "maximum number of expressions in a list is 1000" which happen when we use
			// condition "IN" a String, we use Hashtable to store all data which you need check.
			Hashtable hashWorkUnitID = Project.putProjectIDHashTable(projectRoles);
			sql =
				"SELECT PROJECT.TYPE,PROJECT.project_id, CATEGORY, CODE, GROUP_NAME, STATUS, RANK," 
					+  "a.WORKUNITID, a.PARENTWORKUNITID,"
					+ " LEADER, upper(CUSTOMER) CUSTOMER, ACTUAL_FINISH_DATE, ARCHIVE_STATUS"
					+ " FROM WORKUNIT a, PROJECT"
					+ " WHERE a.TYPE= 2 AND (status = 1 OR status = 2)"
					+ " AND PROJECT.PROJECT_ID = a.TABLEID ORDER BY ACTUAL_FINISH_DATE";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
            
			while (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.setWorkUnitId(rs.getLong("WORKUNITID"));
				Long lWorkUnitId = new Long(projectInfo.getWorkUnitId());
				if (hashWorkUnitID.get(lWorkUnitId) == null){
					// this WorkUnitID is not exist in Hashtable so we don't consider this WorkUnit
					continue;
				}
				//HuyNH2 add code to get Project_id
				projectInfo.setProjectId(rs.getLong("project_id"));
				projectInfo.setProjectType(ProjectInfo.parseType(rs.getString("TYPE")));
				projectInfo.setProjectRank(rs.getString("RANK"));
				projectInfo.setLifecycleId(Integer.parseInt(rs.getString("CATEGORY")));
				projectInfo.setLifecycle(ProjectInfo.parseLifecycle(projectInfo.getLifecycleId()));
				projectInfo.setProjectCode(rs.getString("CODE"));
				projectInfo.setGroupName(rs.getString("GROUP_NAME"));
				projectInfo.setStatus(rs.getInt("STATUS"));
				projectInfo.setParent(rs.getLong("PARENTWORKUNITID"));
				projectInfo.setLeader(rs.getString("LEADER"));
				projectInfo.setCustomer(rs.getString("CUSTOMER"));
				projectInfo.setActualFinishDate(rs.getDate("ACTUAL_FINISH_DATE"));
				projectInfo.setArchiveStatus(Double.isNaN(rs.getDouble("archive_status"))?0:rs.getDouble("archive_status"));
				if(projectInfo.getArchiveStatus() !=0){
					projectInfo.setStatus(4);
				}
				projectInfList.add(projectInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return projectInfList;
		}
	}

	public static Vector getProjectsArchiveHistoryByWUs(Vector projectRoles) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		Hashtable hashWorkUnitID = new Hashtable();
		Vector projectInfList = new Vector();
		try {

			// to void error "maximum number of expressions in a list is 1000" which happen when we use
			// condition "IN" a String, we use Hashtable to store all data which you need check.
			  hashWorkUnitID = Project.putProjectIDHashTable(projectRoles);

			sql =
				"SELECT PROJECT.TYPE,PROJECT.project_id, PROJECT.CATEGORY, PROJECT.CODE, PROJECT.GROUP_NAME, PROJECT.STATUS,a.WORKUNITID, a.PARENTWORKUNITID, PROJECT.LEADER, PROJECT.CUSTOMER, PROJECT.ARCHIVE_STATUS,ArchiveMaxdate.effect_date, TO_CHAR(ArchiveMaxdate.effect_date, 'DD-Mon-YYYY hh:mm:ss') EFFECT_DATE_STR " 
				+ " FROM WORKUNIT a, PROJECT,"
				+ " ( "
				+ " SELECT * from project_archive_history " 
				+ " WHERE (project_id,effect_date) in " 
				+ " ("
				+ " SELECT project_id,max(effect_date) " 
				+ "  FROM " 
				+ "  project_archive_history "  
				+ "  group by project_id "
				+ " ) "
				+ " ) ArchiveMaxdate "
				+ "  WHERE a.TYPE= 2  " 
				+ " AND (PROJECT.status = 1 OR PROJECT.status = 2) " 
				+ " AND PROJECT.PROJECT_ID = a.TABLEID " 
				+ " AND PROJECT.PROJECT_ID = ArchiveMaxdate.PROJECT_ID "
				+ " ORDER BY effect_date DESC ";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.setWorkUnitId(rs.getLong("WORKUNITID"));
				Long lWorkUnitId = new Long(projectInfo.getWorkUnitId());
				if (hashWorkUnitID.get(lWorkUnitId) == null){
					// this WorkUnitID is not exist in Hashtable so we don't consider this WorkUnit
					continue;
				}
				//HuyNH2 add code to get Project_id
				projectInfo.setProjectId(rs.getLong("project_id"));
				projectInfo.setProjectType(ProjectInfo.parseType(rs.getString("TYPE")));
				projectInfo.setLifecycleId(Integer.parseInt(rs.getString("CATEGORY")));
				projectInfo.setLifecycle(ProjectInfo.parseLifecycle(projectInfo.getLifecycleId()));
				projectInfo.setProjectCode(rs.getString("CODE"));
				projectInfo.setGroupName(rs.getString("GROUP_NAME"));
				projectInfo.setStatus(rs.getInt("STATUS"));
				projectInfo.setParent(rs.getLong("PARENTWORKUNITID"));
				projectInfo.setLeader(rs.getString("LEADER"));
				projectInfo.setCustomer(rs.getString("CUSTOMER"));
				projectInfo.setActualFinishDate(rs.getDate("effect_date"));
				projectInfo.setActualFinishDateString(rs.getString("EFFECT_DATE_STR"));
				projectInfo.setArchiveStatus(Double.isNaN(rs.getDouble("archive_status"))?0:rs.getDouble("archive_status"));
				if(projectInfo.getArchiveStatus() !=0){
					projectInfo.setStatus(4);
				}
				projectInfList.add(projectInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return projectInfList;
		}
	}

	public static Vector getArchiveHistoryDetail(String project_id) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		Vector result = new Vector();
		try {
            
			// get project infor;
			sql = 
				"select p.code,p.customer, p.group_name,p.leader, p.start_date, " 
				+ " p.actual_finish_date, p.start_date, p.archive_status, "
				+ " TO_CHAR(p.actual_finish_date, 'DD-Mon-YYYY') actual_finish_date_str " 
				+ " from project p "
				+ " where p.project_id = ?";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1,project_id);
			rs = stm.executeQuery();
			while (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.setProjectId(Long.parseLong(project_id));
				projectInfo.setActualFinishDateString(rs.getString("actual_finish_date_str"));
				projectInfo.setActualFinishDate(rs.getDate("actual_finish_date"));
				projectInfo.setLeader(rs.getString("leader"));
				projectInfo.setCustomer(rs.getString("customer"));
				projectInfo.setStartDate(rs.getDate("start_date"));
				projectInfo.setProjectCode(rs.getString("code"));
				projectInfo.setGroupName(rs.getString("group_name"));
				projectInfo.setArchiveStatus(rs.getInt("archive_status"));
				result.add(projectInfo);
			}
			// get history detail;
			sql = 
				" select TO_CHAR(p.effect_date, 'DD-Mon-YYYY hh:mm:ss') effect_date_str, p.description, d.account "
				+ " from project_archive_history p , developer d "
				+ " where p.project_id = ?  "
				+ "     AND d.developer_id = p.developer_id order by effect_date desc";
			stm = conn.prepareStatement(sql);           
			stm.setString(1,project_id);
			rs = stm.executeQuery();
			Vector detailArcHis = new Vector();
			while (rs.next()) {
				ProjectArchiHisDetailInfo inforDetail = new ProjectArchiHisDetailInfo();
				inforDetail.effect_date_str = rs.getString("effect_date_str");
				inforDetail.description = rs.getString("description");
				inforDetail.implement = rs.getString("account");
				//result.add(projectInfo);
				detailArcHis.add(inforDetail);
			}
			result.add(detailArcHis);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return result;
		}
	}
	public static final Vector getProjectArchiveResult(Vector prjArch){
		final Vector reusltVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		if(prjArch.size()!=2){
			 return reusltVector;
		}else{
			String [] arrPrj = (String[])prjArch.elementAt(0);
			String [] arrStatus = (String[])prjArch.elementAt(1);
			String idConstraint = ConvertString.arrayToString(arrPrj, ",");
			int i = 0;
			try{
				conn = ServerHelper.instance().getConnection();
				if(arrPrj.length > 0){
					sql = "select p.project_id,p.code,p.group_name,p.customer,p.leader from project p where p.project_id in ("+idConstraint+") order by p.code";
					stm = conn.prepareStatement(sql);
					rs = stm.executeQuery();
					while(rs.next()){
						ProjectArchiveResultState prjArc = new ProjectArchiveResultState();
						prjArc.project_id = Long.parseLong(rs.getString("project_id"));
						prjArc.customer = rs.getString("customer");
						prjArc.group = rs.getString("group_name");
						prjArc.leader = rs.getString("leader");
						prjArc.project_code = rs.getString("code");
						for( i=0; i < arrStatus.length; i++ ){
							if(Long.parseLong(arrPrj[i])==prjArc.project_id)
								break;
						}
						prjArc.resultStatus = arrStatus[i];
						// add
						reusltVector.addElement(prjArc);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				ServerHelper.closeConnection(conn, stm, rs);
			}
			return reusltVector;
		}
	}
    
	public static final Vector getCustomerListForArchive(Vector projectRoles){
		final Vector reusltVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		Hashtable hashWorkUnitID = new Hashtable();
		try{
//			sql = "select distinct  upper(p.customer) customer from project p, workunit a  where  (p.status = 1 OR p.status = 2) "// stautus = 1 OR status = 2 => cancle OR close project
//							+ " AND  a.WORKUNITID " 
//							+ " IN ("  + idConstraint +  ") " 
//							+ " AND p.PROJECT_ID = a.TABLEID " 
//							+ " order by upper(p.customer) ";

			// to void error "maximum number of expressions in a list is 1000" which happen when we use
			// condition "IN" a String, we use Hashtable to store all data which you need check.
			hashWorkUnitID = Project.putProjectIDHashTable(projectRoles);

			sql = "select distinct  upper(p.customer) customer, a.WORKUNITID from project p, workunit a  where  (p.status = 1 OR p.status = 2) "// stautus = 1 OR status = 2 => cancle OR close project
							+ " AND p.PROJECT_ID = a.TABLEID " 
							+ " order by upper(p.customer) ";            
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			while(rs.next()){
				Long lWorkUnitId = new Long(rs.getLong("WORKUNITID"));
				if (hashWorkUnitID.get(lWorkUnitId) == null){
					// this WorkUnitID is not exist in Hashtable so we don't consider this WorkUnit
					continue;
				}
				if(rs.getString("customer")!="" && rs.getString("customer") != null){
					reusltVector.addElement(rs.getString("customer").trim());
				}
				else{
					reusltVector.addElement("N/A");
				}
			}
			return reusltVector;
		}
		catch(Exception e){
			e.printStackTrace();
			return reusltVector;
		}
		finally{
			ServerHelper.closeConnection(conn, stm, rs);
		}
		
	}
	/*End */
    
    
	public static final Vector getProjectSkillSet(final long prjID) {
		final Vector vtResult = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				" SELECT * FROM (SELECT A.ASSIGNMENT_ID ASSIGNMENT_ID, A.RESPONSE ROLE, D.NAME DEVELOPER_NAME, D.ACCOUNT DEVELOPER_ACCOUNT, RESPONSIBILITY.NAME RESPONSIBILITY_NAME, B.SKILL SKILL,"
					+ " B.PROCESS_ID PROCESS_ID, B.POINT SKILL_POINT, B.SKILLCOMMENT SKILL_COMMENT, RESPONSIBILITY.ORDER_NUMBER ORDER_NUMBER, D.DEVELOPER_ID DEVELOPER_ID"
					+ " FROM ASSIGNMENT A, PROJECTSKILL B, DEVELOPER D, RESPONSIBILITY"
					+ " WHERE A.PROJECT_ID = ? "
					+ " AND A.DEVELOPER_ID = D.DEVELOPER_ID"
					+ " AND RESPONSIBILITY.RESPONSIBILITY_ID = A.RESPONSE"
					+ " AND B.ASSIGNMENT_ID (+)= A.ASSIGNMENT_ID) SKILLSET, PROCESS WHERE"
					+ " PROCESS.PROCESS_ID (+)= SKILLSET.PROCESS_ID ORDER BY ORDER_NUMBER, ASSIGNMENT_ID";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final SkillInfo info = new SkillInfo();
				info.assignmentId = rs.getLong("ASSIGNMENT_ID");
				info.fullName = rs.getString("DEVELOPER_NAME");
				info.account = rs.getString("DEVELOPER_ACCOUNT");
				info.projectRole = rs.getString("RESPONSIBILITY_NAME");
				info.processId = rs.getLong("PROCESS_ID");
				info.process = rs.getString("NAME");
				info.skill = rs.getString("SKILL");
				info.point = rs.getInt("SKILL_POINT");
				info.skillComment = rs.getString("SKILL_COMMENT");
                vtResult.addElement(info);
			}
			return vtResult;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final Vector getProjectSkillSet2(final long prjID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				" SELECT A.ASSIGNMENT_ID F1, COUNT(D.NAME) F2"
					+ " FROM ASSIGNMENT A, PROJECTSKILL B, DEVELOPER D"
					+ " WHERE A.PROJECT_ID = ? "
					+ " AND A.DEVELOPER_ID = D.DEVELOPER_ID"
					+ " AND B.ASSIGNMENT_ID (+)= A.ASSIGNMENT_ID"
					+ " GROUP BY A.ASSIGNMENT_ID ORDER BY F1";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final SkillInfo info = new SkillInfo();
				info.assignmentId = rs.getLong("F1");
				info.point = rs.getInt("F2");
				resultVector.addElement(info);
			}
			return resultVector;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final Vector getSkillDetail(final long assignmentId) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		Vector vt = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql =
				"SELECT A.PROJECTSKILLID, A.ASSIGNMENT_ID, A.PROCESS_ID, A.SKILL, A.POINT, A.SKILLCOMMENT, B.NAME FROM"
					+ " PROJECTSKILL A, PROCESS B WHERE A.ASSIGNMENT_ID = "
					+ assignmentId
					+ " AND A.PROCESS_ID = B.PROCESS_ID"
    					+ " ORDER BY B.NAME, A.PROJECTSKILLID";
			rs = stm.executeQuery(sql);
			if (rs != null) {
				vt = new Vector();
				while (rs.next()) {
					final SkillInfo info = new SkillInfo();
					info.projectSkillId = rs.getLong("PROJECTSKILLID");
					info.assignmentId = rs.getLong("ASSIGNMENT_ID");
					info.processId = rs.getLong("PROCESS_ID");
					info.process = rs.getString("NAME");
					info.skill = rs.getString("SKILL");
					info.skillComment = rs.getString("SKILLCOMMENT");
					info.point = rs.getInt("POINT");
					vt.add(info);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return vt;
		}
	}
	public static boolean updateSkill(final SkillInfo info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE PROJECTSKILL SET SKILL = ?, POINT = ?, SKILLCOMMENT = ? WHERE PROJECTSKILLID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, info.skill);
			stm.setInt(2, info.point);
			stm.setString(3, info.skillComment);
			stm.setLong(4, info.projectSkillId);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static boolean deleteSkill(final long projectSkillId) {
		return Db.delete(projectSkillId, "PROJECTSKILLID", "PROJECTSKILL");
	}
	public static final Vector getProcessList() {
		return ProcessInfo.processList;
	}
	public static final Vector getCustomerList() {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		Vector vt = null;
		ResultSet rs = null;
		try {
			vt = new Vector();
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT CUS_NAME FROM CUSTOMER order by CUS_NAME ";
			rs = stm.executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					final ProjectInfo projectInfo = new ProjectInfo();
					projectInfo.setCustomer(rs.getString("CUS_NAME").trim());
					vt.add(projectInfo);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return vt;
		}
	}
	public static boolean addSkill(final SkillInfo info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "INSERT INTO PROJECTSKILL VALUES ((SELECT NVL(MAX(PROJECTSKILLID)+1,1) FROM PROJECTSKILL), ?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, info.assignmentId);
			stm.setLong(2, info.processId);
			stm.setString(3, info.skill);
			stm.setInt(4, info.point);
			stm.setString(5, info.skillComment);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	
	public static void delSkillNoPrjSId(final SkillInfo info, final int processId) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			String temp1 = "";
			if(info.skillComment!=null) {
				temp1 = info.skillComment;
			}
			if (temp1!="")
				sql = "select count(*) as abc from projectskill where assignment_id = "+info.assignmentId+" and process_id = "+processId+" and skill = '"+info.skill+"' and point = "+info.point+" and skillcomment='"+temp1+"'";
			else sql = "select count(*) as abc from projectskill where assignment_id = "+info.assignmentId+" and process_id = "+processId+" and skill = '"+info.skill+"' and point = "+info.point; 
			stm = conn.prepareStatement(sql);
			ResultSet temp = stm.executeQuery();
			while(temp.next()){
				int count = temp.getInt("ABC");
				if (temp1!="")
					sql = "delete from projectskill where assignment_id = "+info.assignmentId+" and process_id = "+processId+" and skill = '"+info.skill+"' and point = "+info.point+" and skillcomment='"+temp1+"'";
				else 
					sql = "delete from projectskill where assignment_id = "+info.assignmentId+" and process_id = "+processId+" and skill = '"+info.skill+"' and point = "+info.point;
				stm = conn.prepareStatement(sql);
				stm.executeQuery();
				if (count>1) {
					for (int i=0;i<(count-1);i++){
						sql = "INSERT INTO PROJECTSKILL VALUES ((SELECT NVL(MAX(PROJECTSKILLID)+1,1) FROM PROJECTSKILL), ?, ?, ?, ?, ?)";
						stm = conn.prepareStatement(sql);
						stm.setLong(1, info.assignmentId);
						stm.setLong(2, processId);
						stm.setString(3, info.skill);
						stm.setInt(4, info.point);
						stm.setString(5, temp1);
						stm.executeUpdate();		
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	/** get list of project
	 * @author Hoang My Duc
	 * @param projectID
	 * @return Vector of RequirementInfo
	 */
	public static final Vector getProjectList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		final Vector list = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT PROJECT_ID , NAME , CODE, GROUP_NAME FROM PROJECT ORDER BY CODE";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final ProjectDateInfo info = new ProjectDateInfo();
				info.projectID = rs.getInt("PROJECT_ID");
				info.name = rs.getString("NAME");
				info.code = rs.getString("CODE");
				info.groupName = rs.getString("GROUP_NAME");			
				list.addElement(info);
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
	
	public static final Vector getGroupList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		final Vector list = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT GROUP_ID , GROUPNAME FROM GROUPS ";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final GroupInfo info = new GroupInfo();
				info.groupID = rs.getInt("GROUP_ID");
				info.name = rs.getString("GROUPNAME");		
				list.addElement(info);
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
	 * get list of projects selected for SQA report
	 * @param startDate start date of report
	 * @param endDate end date of report
	 * @param nWorkUnitID ident.number of work unit that project belongs to
	 * @return Vector list of project 
	 */
	public static Vector getProjectListForSQA(Date startDate, Date endDate, long nWorkUnitID) {
		Connection con = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		Vector vtProjectList = new Vector();
		String strSQL;
		String strTemp;
		try {
			if (nWorkUnitID == 0) {
				strTemp = "AND w.parentworkunitid IS NOT NULL ";
			}
			else {
				strTemp = "AND w.parentworkunitid = ? ";
			}
			strSQL =
				"SELECT project_id, code, group_name "
					+ "FROM (SELECT p.project_id, p.code, p.group_name, COUNT (defect_id) AS defnum "
					+ "FROM project p, defect d, workunit w "
					+ "WHERE p.project_id = d.project_id "
					+ "AND p.project_id = w.tableid "
					+ "AND (p.actual_finish_date IS NULL OR p.actual_finish_date >= ?) "
					+ "AND p.start_date <= ? "
					+ "AND d.create_date BETWEEN ? AND ? "
					+ strTemp
					+ "GROUP BY p.code, p.project_id, p.group_name) "
					+ "WHERE defnum > 0";
			con = ServerHelper.instance().getConnection();
			prepStmt = con.prepareStatement(strSQL);
			prepStmt.setDate(1, startDate);
			prepStmt.setDate(2, endDate);
			prepStmt.setDate(3, startDate);
			prepStmt.setDate(4, endDate);
			if (nWorkUnitID != 0) {
				prepStmt.setLong(5, nWorkUnitID);
			}
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.setProjectId(rs.getInt("project_id"));
				projectInfo.setProjectCode(rs.getString("code"));
				projectInfo.setGroupName(rs.getString("group_name"));
				vtProjectList.add(projectInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(con, prepStmt, rs);
			return vtProjectList;
		}
	}
	public static final long getProjectId(final String strProjectCode) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		long lProjectId = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT PROJECT_ID FROM PROJECT WHERE CODE = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, strProjectCode);
			rs = stm.executeQuery();
			if (rs.next()) {
                lProjectId = rs.getLong("PROJECT_ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
            return lProjectId;
		}
	}
	public static void saveScheduleFile(UploadInfo uploadInf, long projectID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = Db.getOracleConn();
			sql = "UPDATE PROJECT_PLAN SET SCHEDULEFILE=?,SCHEDULEFILENAME=? WHERE PROJECT_ID=?";
			stm = conn.prepareStatement(sql);
			Db.setBLOB(stm, 1, uploadInf.file);
			stm.setString(2, uploadInf.fileName);
			stm.setLong(3, projectID);
			stm.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
			uploadInf.responseType = UploadInfo.UNKNOWN_ERROR;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static String getSchedFileName(long projectID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		String retval = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT SCHEDULEFILENAME FROM PROJECT_PLAN  WHERE PROJECT_ID=?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, projectID);
			rs = stm.executeQuery();
			if (rs.next())
				retval = rs.getString("SCHEDULEFILENAME");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return retval;
		}
	}
	public static UploadInfo getScheduleFile(long projectID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		UploadInfo uploadInf = new UploadInfo();
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT SCHEDULEFILENAME,SCHEDULEFILE FROM PROJECT_PLAN  WHERE PROJECT_ID=?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, projectID);
			rs = stm.executeQuery();
			if (rs.next()) {
				uploadInf.fileName = rs.getString("SCHEDULEFILENAME");
				uploadInf.file = Db.getBlob(rs, "SCHEDULEFILE");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			uploadInf.responseType = UploadInfo.UNKNOWN_ERROR;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return uploadInf;
		}
	}
	public static ProjectInfo getProjectInfo(long prjID) {
		long[] arr = { prjID };
		ProjectInfo projectInfo = new ProjectInfo();
		try {
			projectInfo = (ProjectInfo)getProjectInfos(arr).elementAt(0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return projectInfo;
		}
	}
	/**
	 * @return vector of ProjectInfo
	 * In case update is needed please also update Project.getChildProjectsByWU()
	 */
	public static Vector getProjectInfos(long[] prjIDs) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		Vector retVal = new Vector();
		Hashtable hashProjectID = new Hashtable(); 
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			// 
			for (int i = 0; i < prjIDs.length; i++){
				hashProjectID.put(new Long(prjIDs[i]), prjIDs);
			}
			//HuyNH2 add archive_status field to get archive status, add to sql query.

			sql =
				"SELECT a.PARENTWORKUNITID parent, b.PARENTWORKUNITID grandParent,"
					+ " p.type, code, p.PROJECT_ID, p.name project_name, p.group_name group_name, start_date,"
					+ " per_complete, leader, base_finish_date, plan_finish_date,"
					+ " actual_finish_date, base_effort, plan_effort, actual_effort,"
					+ " base_billable_effort, plan_billable_effort, actual_billable_effort,"
					+ " plan_calendar_effort,replan_calendar_effort,"
					+ " description, p.status, schedule_status, effort_status, p.archive_status,"
					+ " p.last_update, category, customer, plan_start_date, apply_PPM, Reason, a.WORKUNITID, pp.scope_objective,"
                    + " p.rank, p.project_level, p.customer_2nd, p.division_name , p.type_customer , p.type_customer_2nd, pp.domain, dev.name dev_name," 
                    + " apptype.type_name application_type, contract_type.contract_type_name contract_type"
					+ " FROM WORKUNIT a, WORKUNIT b, PROJECT P, PROJECT_PLAN PP, APPLICATION_TYPE APPTYPE, CONTRACT_TYPE, DEVELOPER DEV"
					+ " WHERE a.TYPE(+)= 2"
                    + " AND APPTYPE.APPLICATION_ID(+)= PP.APPLICATION_TYPE"
                    + " AND CONTRACT_TYPE.CONTRACT_TYPE_ID(+)= PP.CONTRACT_TYPE"
					+ " AND P.PROJECT_ID = a.TABLEID (+) "
					+ " AND a.PARENTWORKUNITID = b.WORKUNITID (+)"
					+ " AND P.PROJECT_ID = PP.PROJECT_ID(+)"
                    + " AND P.LEADER = DEV.ACCOUNT(+)";
					// + " AND P.PROJECT_ID in("
					// If input projects list is empty
					// + (prjIDs.length > 0 ? ConvertString.arrayToString(prjIDs, ",") : "0")
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.setProjectId(rs.getLong("PROJECT_ID"));
				Long lProjectID = new Long(projectInfo.getProjectId());
				if ( hashProjectID.get(lProjectID) == null){
					continue;
				}
				projectInfo.setProjectName(rs.getString("project_name"));
				projectInfo.setBaseFinishDate(rs.getDate("base_finish_date"));
				projectInfo.setPlanFinishDate(rs.getDate("plan_finish_date"));
				projectInfo.setActualFinishDate(rs.getDate("actual_finish_date"));
				projectInfo.setStartDate(rs.getDate("start_date"));
				if (rs.getDate("plan_start_date") != null){
					projectInfo.setPlanStartDate(rs.getDate("plan_start_date"));
				}
				else {
					projectInfo.setPlanStartDate(rs.getDate("start_date"));
				}
				projectInfo.setPerComplete(rs.getLong("per_complete"));
				projectInfo.setProjectCode(rs.getString("code"));
				projectInfo.setStatus(rs.getInt("status"));
				projectInfo.setGroupName(rs.getString("group_name"));
				projectInfo.setCustomer(rs.getString("customer"));
				projectInfo.setParent(rs.getLong("parent"));
				projectInfo.setGrandParent(rs.getLong("grandParent"));
				projectInfo.setProjectType(ProjectInfo.parseType(rs.getString("type")));
				projectInfo.setLifecycleId(Integer.parseInt(rs.getString("category")));
				projectInfo.setLifecycle(ProjectInfo.parseLifecycle(rs.getString("category")));
				projectInfo.setBaseEffort(Db.getDouble(rs, "base_effort"));
				projectInfo.setPlanEffort(Db.getDouble(rs, "plan_effort"));
				projectInfo.setPlannedEffort((Double.isNaN(projectInfo.getPlanEffort())) ? projectInfo.getBaseEffort() : projectInfo.getPlanEffort());
				// HUYNH2 Add billable Effort
				projectInfo.setBaseBillableEffort(Db.getDouble(rs, "base_billable_effort"));
				projectInfo.setPlanBillableEffort(Db.getDouble(rs, "plan_billable_effort"));
				projectInfo.setActualBillableEffort(Db.getDouble(rs, "actual_billable_effort"));
								
				/*
				if(Double.isNaN(projectInfo.base_billable_effort)){
					projectInfo.base_billable_effort = (Double.isNaN(projectInfo.base_billable_effort)) ? projectInfo.base_effort : projectInfo.base_billable_effort;
					projectInfo.plan_billable_effort = (Double.isNaN(projectInfo.plan_billable_effort)) ? projectInfo.plan_effort : projectInfo.plan_billable_effort;
				}
				*/
				// Calendar effort
				projectInfo.setPlannedBillableEffort(
					(Double.isNaN(projectInfo.getPlanBillableEffort())) ?
						projectInfo.getBaseBillableEffort() : projectInfo.getPlanBillableEffort());

				projectInfo.setPlanCalendarEffort(Db.getDouble(rs, "plan_calendar_effort"));
				projectInfo.setReplanCalendarEffort(Db.getDouble(rs, "replan_calendar_effort"));
				projectInfo.setPlannedCalendarEffort(
					(Double.isNaN(projectInfo.getReplanCalendarEffort())) ?
						projectInfo.getPlanCalendarEffort() : projectInfo.getReplanCalendarEffort());
				projectInfo.setPlannedFinishDate((projectInfo.getPlanFinishDate() == null) ? projectInfo.getBaseFinishDate() : projectInfo.getPlanFinishDate());
				// end
				projectInfo.setLeader(rs.getString("leader"));
                projectInfo.setLeaderName(rs.getString("dev_name"));
				projectInfo.setApplyPPM(rs.getInt("apply_PPM"));
				projectInfo.setReason(rs.getString("Reason"));
				projectInfo.setWorkUnitId(rs.getLong("WORKUNITID"));
                projectInfo.setScopeAndObjective(((Db.getClob(rs, "scope_objective") == null) ? "" : Db.getClob(rs, "scope_objective")));
                projectInfo.setBusinessDomain(rs.getString("domain"));
                projectInfo.setApplicationType(rs.getString("application_type"));
                projectInfo.setContractType(rs.getString("contract_type"));                
                projectInfo.setProjectRank(rs.getString("rank"));
                projectInfo.setProjectLevel(rs.getString("project_level"));
                projectInfo.setSecondCustomer(rs.getString("customer_2nd"));
                projectInfo.setDivisionName(rs.getString("division_name"));
				projectInfo.setTypeCustomer(rs.getInt("TYPE_CUSTOMER"));
				projectInfo.setTypeCustomer2(rs.getInt("TYPE_CUSTOMER_2ND"));
				// HuyNH2 add code to get archive status of projet
				projectInfo.setArchiveStatus(rs.getDouble("ARCHIVE_STATUS"));
				// get information about summay of Loc Productivity actual and Loc Quality actual.
				

				retVal.add(projectInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return retVal;
		}
	}
	public static Vector getRankList() {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null; 
		Vector rankInfoList = new Vector();
		try {
			sql = "SELECT DISTINCT RANK FROM PROJECT WHERE RANK IS NOT NULL"; 
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				RankInfo rankInfo = new RankInfo();
				rankInfo.rank = rs.getString("RANK");
				rankInfoList.add(rankInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(sql);
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return rankInfoList;
		}
   }
   //LAMnt3 - 20081020
   public static String getRankByProjectId(long project_Id) {
	   Connection conn = null;
	   PreparedStatement stm = null;
	   String sql = null;
	   ResultSet rs = null; 
	   String rank = "";
	   try {
		   sql = "SELECT RANK FROM PROJECT WHERE project_id = ?"; 
		   conn = ServerHelper.instance().getConnection();
		   stm = conn.prepareStatement(sql);
		   stm.setLong(1, project_Id);
		   rs = stm.executeQuery();
		   if (rs.next()) {
			   rank = rs.getString("RANK");
		   }
	   } catch (Exception e) {
		   e.printStackTrace();
		   System.err.println(sql);
	   } finally {
		   ServerHelper.closeConnection(conn, stm, rs);
		   return rank;
	   }
  }
   /**
	* @author HieuNV1
	* check all users which can be assigned into this project
	* @param projectID
	* @return false if has users else return true
	*/
   public static boolean checkDeleteProject(long projectID){
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		ResultSet rs = null;
		try{
			conn = ServerHelper.instance().getConnection();

			sql =	" SELECT count(D.DEVELOPER_ID) as Count " +
					" FROM  DEVELOPER D, ASSIGNMENT A" +
					" WHERE D.DEVELOPER_ID = A.DEVELOPER_ID" +
						" And A.PROJECT_ID =?";
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, projectID);
			rs = preStm.executeQuery();
			if (rs.next()){
				if (rs.getInt("Count") != 0){
					return false;
				}
			}
			return true;
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		finally{
			ServerHelper.closeConnection(conn, preStm, rs);
		}
   }
   /**
	* get Project infomation by projectID
	* @param projectID
	* @return ProjectInfo
	*/
   	
   public static ProjectInfo getProjectInfoForWU(final long projectID){
	   ProjectInfo result = new ProjectInfo();
	   Connection conn = null;
	   PreparedStatement preStm = null;
	   String sql = null;
	   ResultSet rs = null;
	   try {
		   conn = ServerHelper.instance().getConnection();
		   conn.setAutoCommit(false);
			
		   sql = "SELECT a.PROJECT_ID, a.GROUP_NAME, a.CODE, a.NAME, a.CUSTOMER, a.CUSTOMER_2ND, a.LEADER, b.SCOPE_OBJECTIVE, " 
				   + "a.CATEGORY, a.TYPE, a.RANK, START_DATE, PLAN_START_DATE , BASE_FINISH_DATE, PLAN_FINISH_DATE, STATUS " 
				   + " FROM PROJECT a, PROJECT_PLAN b"
				   + " WHERE a.PROJECT_ID =  b.PROJECT_ID(+) "
				   + " AND a.PROJECT_ID=?";
		   preStm = conn.prepareStatement(sql);
		   preStm.setLong(1, projectID);
		   rs = preStm.executeQuery();
		   conn.commit();
		   conn.setAutoCommit(true);
		   if (rs.next()){
			   result.setProjectId(rs.getLong("PROJECT_ID"));
			   result.setProjectName(rs.getString("NAME"));
			   result.setGroupName(rs.getString("GROUP_NAME"));
			   result.setProjectCode(rs.getString("CODE"));
			   result.setCustomer(rs.getString("CUSTOMER"));
			   result.setSecondCustomer(rs.getString("CUSTOMER_2ND"));
			   result.setLeader(rs.getString("LEADER"));
                result.setLifecycle(ProjectInfo.parseLifecycle(rs.getString("CATEGORY")));
                result.setProjectType(ProjectInfo.parseType(rs.getString("TYPE")));
			   result.setProjectRank(rs.getString("RANK"));
			   if (rs.getDate("PLAN_START_DATE") != null){
				   result.setPlanStartDate(rs.getDate("PLAN_START_DATE"));
                } else {
				   result.setPlanStartDate(rs.getDate("START_DATE"));
			   }
			   if (rs.getDate("BASE_FINISH_DATE") == null){
				   result.setPlanFinishDate(rs.getDate("PLAN_FINISH_DATE"));
                } else {
				   result.setPlanFinishDate(rs.getDate("BASE_FINISH_DATE"));
			   }
			   result.setScopeAndObjective(((Db.getClob(rs, "SCOPE_OBJECTIVE") == null) ? "" : Db.getClob(rs, "SCOPE_OBJECTIVE")));
			   result.setStatusName(ProjectInfo.parseStatus(rs.getString("STATUS")));
		   }
		   return result;
        } catch (Exception e) {
		   e.printStackTrace();
		   try {
			conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
		}
		   return null;
        } finally {
		   ServerHelper.closeConnection(conn, preStm, rs);
	   }
	}
	/**
	* perform generate XML structure of Projects of Group
	* @param strGroupName
	* @return
	*/

   public static Vector doGetProjectInfoToGenerateXML(String strGroupName){
	   Vector vtProjectInfo = new Vector();
	   Connection conn = null;
	   PreparedStatement preStm = null;
	   ResultSet rs = null;
	   String sql = null;
	   try{
		   conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT PROJECT_ID, code, customer, leader"
					+ " FROM project p"
					+ " WHERE STATUS = 0"
					+ " AND group_name=?"
					+ " AND code NOT LIKE '%Daily%'"
					+ " ORDER BY start_date DESC";
		   preStm = conn.prepareStatement(sql);
		   preStm.setString(1, strGroupName.toUpperCase());
		   rs = preStm.executeQuery();
		   while (rs.next()){
			   XMLGenerateBean xmlBean = new XMLGenerateBean();
			   xmlBean.setProjectID(rs.getLong("PROJECT_ID"));
			   xmlBean.setProjectName(rs.getString("code"));
			   xmlBean.setCustomer(rs.getString("customer"));
			   xmlBean.setProjectManager(rs.getString("leader"));
				xmlBean.setProjectPoint(
					getProjectPoint(xmlBean.getProjectID()));
			   vtProjectInfo.add(xmlBean);
		   }
		   return vtProjectInfo;
		} catch (SQLException ex) {
		   ex.printStackTrace();
		   return vtProjectInfo;
		} finally {
		   ServerHelper.closeConnection(conn, preStm, rs);
	   }
   }
   
   public static double getProjectPoint(long lProjectID){
		double dProjectPoint = 0;
		try{
			Vector vtStageList = Schedule.getStageList(lProjectID);
			StageInfo stageInfo = null;
			long stageID = 0;
			for (int i = vtStageList.size() - 1; i >= 0; i--) {
				stageInfo = (StageInfo) vtStageList.elementAt(i);
				if ((stageInfo.aEndD != null)
					&& (stageInfo.actualBeginDate != null)
					&& (stageInfo.plannedEndDate != null)
					&& (stageInfo.plannedBeginDate != null)) {
						stageID = stageInfo.milestoneID;
						// get lastest finished stage of project 
				    ProjectPointInfo ppointInfo =
				        Report.getProjectPoint(lProjectID, stageID);
						return ppointInfo.ProjectPoint;
				}
			}
			return dProjectPoint;
        } catch (Exception ex) {
			ex.printStackTrace();
			return dProjectPoint;
		}
   }
   /**
    * Get all Projects informations which can be by Group.
    * If you want to get all ProjectList then byGroup = null;
    * @param byGroup
    * @return
    */
    public static Vector getProjectList(String byGroup, int searchType){
    	Vector projectList = new Vector();
    	Connection conn =  null;
    	PreparedStatement preStm = null;
    	ResultSet rs = null;
    	String sql = null;
    	String condition = "";
    	try {
    		if (byGroup != null && !"".equals(byGroup)){
    			switch (searchType){
    				case Constants.HR_USER_BY_GROUP:
    					condition += " AND D.GROUP_NAME = ?";
    					break;
    				case Constants.HR_USER_BY_PROJECT:
    					condition += " AND P.GROUP_NAME = ?";
    					break;
    				case Constants.HR_USER_BY_ALL:
    					condition += " AND( P.GROUP_NAME = ? ";
    					condition += " OR D.GROUP_NAME = ? )";
    					break;
    			}
    		}
			sql = 
				"SELECT DISTINCT P.PROJECT_ID, P.CODE, P.GROUP_NAME, P.LEADER, P.STATUS, P.TYPE" 
				+ " FROM PROJECT P , ASSIGNMENT A, DEVELOPER D"
				+ " WHERE A.PROJECT_ID = P.PROJECT_ID"
				+ " AND D.DEVELOPER_ID = A.DEVELOPER_ID"
				+ condition
				+ " ORDER BY P.CODE ";
			conn = ServerHelper.instance().getConnection();
			preStm = conn.prepareStatement(sql);
			if (!"".equals(condition)) {
				preStm.setString(1, byGroup);
				if (searchType == Constants.HR_USER_BY_OTHER_GROUP){
					preStm.setString(2, byGroup);
				}
			}
			rs = preStm.executeQuery();
			while (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.setProjectId(rs.getLong("PROJECT_ID"));
				projectInfo.setProjectCode(rs.getString("CODE"));
				projectInfo.setGroupName(rs.getString("GROUP_NAME"));
				projectInfo.setStatus(rs.getInt("STATUS"));
				projectInfo.setType(rs.getInt("TYPE"));
				projectList.add(projectInfo);
			}
   		}
   		catch (SQLException ex){
   			ex.printStackTrace();
   		}
    	catch (Exception ex){
    		ex.printStackTrace();
    	}
    	finally {
    		ServerHelper.closeConnection(conn, preStm, rs);
    		return projectList;
    	}
    	
    	
    }
	public static void doMigrateCustomerMetricsData() throws SQLException {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = "";

		Vector prjList = Project.getProjectList();
		StageInfo stageInfo;
		// set all milestoneID to null.

		try {
			
			sql = " Update cus_metrics a set a.milestone_id = null";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.executeUpdate();
			stm.close();
			
			ProjectDateInfo prj = null;
			for (int i = 0; i < prjList.size(); i++) {
				// get StageList depend on
				prj = ((ProjectDateInfo) prjList.elementAt(i));
				if (!prj.code.equalsIgnoreCase("MISC")) {
					Vector stageList = Schedule.getStageList(prj.projectID);
					if (stageList != null && stageList.size() > 0) {
						stageInfo = getCurrentStage(stageList);
						sql = "UPDATE cus_metrics a set a.milestone_id =?"
								+ " WHERE a.project_id = ?";
						stm = conn.prepareStatement(sql);
						stm.setLong(1, stageInfo.milestoneID);
						stm.setInt(2, prj.projectID);
						stm.executeUpdate();
						stm.close();
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			ServerHelper.closeConnection(conn, stm, null);

		}

	}
	public static StageInfo getCurrentStage(Vector stageList) {
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
		return stageInfo;
	}
  

}