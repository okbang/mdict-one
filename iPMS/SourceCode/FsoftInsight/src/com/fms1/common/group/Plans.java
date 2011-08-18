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
 
 package com.fms1.common.group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.fms1.tools.CommonTools;
import com.fms1.tools.Db;
import com.fms1.web.ServerHelper;
import com.fms1.infoclass.group.*;
import com.fms1.infoclass.*;
import com.fms1.common.*;
import java.util.*;
/**
 * Plans logic
 * @author manu
 */
public class Plans {
	public static long getPlanningID(long wuID, int year, float version) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		long retVal = 0;
		String strSQL =
			"SELECT PLANNING_ID FROM PLANNING WHERE WORKUNIT_ID=? AND YEAR=? AND VERSION=?";
		try {
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(strSQL);
			prepStmt.setLong(1, wuID);
			prepStmt.setInt(2, year);
			prepStmt.setFloat(3, version);
			rs = prepStmt.executeQuery();
			if (rs.next())
				retVal = rs.getLong("PLANNING_ID");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return retVal;
		}
	}
	public static double getPlannedVal(long wuID, int metricConstant, int year, int month) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		double dRetVal = Double.NaN;
        long nWorkUnitID = 0;
        long nGroupID = -1;

        String strSQL = "SELECT PLANS.* "
				+ "FROM PLANS,PLANNING "
                + "WHERE PLANS.METRIC_ID=? "
				+ "AND PLANS.PLANNING_ID=PLANNING.PLANNING_ID "
				+ "AND PLANNING.WORKUNIT_ID=? "
                + "AND PLANNING.YEAR=? "
                + "AND PLANS.GROUP_ID=?";
		try {
            WorkUnitInfo workUnitInfo = WorkUnit.getWorkUnitInfo(wuID);
            if (workUnitInfo.type == WorkUnitInfo.TYPE_ORGANIZATION) {
                nWorkUnitID = wuID;
                nGroupID = -1;
            }
            else if (workUnitInfo.type == WorkUnitInfo.TYPE_GROUP) {
                nWorkUnitID = workUnitInfo.parentWorkUnitID;
                nGroupID = workUnitInfo.tableID;
            }
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(strSQL);
			prepStmt.setInt(1, metricConstant);
			prepStmt.setLong(2, nWorkUnitID);
			prepStmt.setInt(3, year);
            prepStmt.setLong(4, nGroupID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				dRetVal = Db.getDouble(rs, CommonTools.getMonth(month));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return dRetVal;
		}
	}
	public static PlanningInfo getPlanning(long wuID, int year, float version, String section) {
		PlanningInfo planningInfo = new PlanningInfo();
		planningInfo.workUnit = wuID;
		planningInfo.planningID = 0;
		planningInfo.year = year;
		planningInfo.version = version;
		planningInfo.planType = section;
		PlanningInfo.Row row;
		boolean isAll = section.equals("All");
		//is it a group or an organization
		WorkUnitInfo wuInfo = WorkUnit.getWorkUnitInfo(wuID);
		planningInfo.isGroup = (wuInfo.type == WorkUnitInfo.TYPE_GROUP);
		Vector childGroups = null;
		Vector opGroup = null;
		if (!planningInfo.isGroup) {
			//for organization get children groups
			childGroups = WorkUnit.getChildrenGroups(wuID);
			opGroup = new Vector();
			GroupInfo grInfo;
			for (int i = 0; i < childGroups.size(); i++) {
				grInfo = (GroupInfo) childGroups.elementAt(i);
				if (grInfo.isOperation)
					opGroup.add(grInfo);
			}
		}
		String positive="Positive";
		String integer="Integer";
		//create plan squelletton, poor attempt in systematic Jscriping for auto calc feature
		if (isAll || section.equals("Staff")) {
			planningInfo.rows.add(planningInfo.new Row("Staff"));
			fillPlan(
				planningInfo,
				childGroups,
				MetricDescInfo.TOTAL_STAFF,
				"colSum(this);",
				"rowMax(this);",
				"rowMax(this);",positive+integer);
			fillPlan(
				planningInfo,
				childGroups,
				MetricDescInfo.TOTAL_DEVELOPERS,
				"colSum(this);",
				"rowMax(this);",
				"rowMax(this);",positive+integer);
			fillPlan(planningInfo, opGroup, MetricDescInfo.BUSY_RATE, "", "", "",positive);
			fillPlan(planningInfo, opGroup, MetricDescInfo.BILLABLE_RATE, "", "", "",positive);
			fillPlan(
				planningInfo,
				childGroups,
				MetricDescInfo.TOTAL_TEMPRORARY_STAFF,
				"colSum(this);",
				"rowMax(this);",
				"rowMax(this);",positive+integer);
		}
		else if (isAll || section.equals("Finance")) {
			planningInfo.rows.add(planningInfo.new Row("Finance"));
			fillPlan(planningInfo, opGroup, MetricDescInfo.REVENUE, "colSum(this);", "rowSum(this);", "rowSum(this);","");
			fillPlan(
				planningInfo,
				opGroup,
				MetricDescInfo.COST_OF_GOOD_SOLD,
				"colSum(this);",
				"rowSum(this);",
				"rowSum(this);",positive);
			fillPlan(
				planningInfo,
				opGroup,
				MetricDescInfo.OPERATION_EXPENSE,
				"colSum(this);",
				"rowSum(this);",
				"rowSum(this);",positive);
		}
		else if (isAll || section.equals("Infrastructure")) {
			planningInfo.rows.add(planningInfo.new Row("Infrastructure"));
			fillPlan(planningInfo, null, MetricDescInfo.INTERNET_BANDWIDTH, "", "rowMax(this);", "rowMax(this);",positive);
		}
		else if (isAll || section.equals("Product")) {
			planningInfo.rows.add(planningInfo.new Row("Product"));
			fillPlan(planningInfo, opGroup, MetricDescInfo.IN_PROGRESS_PROJECTS, "colSum(this);", "", "colSum(this);",positive+integer);
			fillPlan(planningInfo, opGroup, MetricDescInfo.NONCONFORMING_PRODUCT_RATE, "", "", "",positive);
		}
		else if (isAll || section.equals("Process")) {
			planningInfo.rows.add(planningInfo.new Row("Process"));
			fillPlan(
				planningInfo,
				childGroups,
				MetricDescInfo.SPENT_EFFORT,
				"colSum(this);",
				"colSum(this);",
				"colSum(this);",positive);
		}
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		String sql =
			"SELECT PLANS.*,PLANNING.PLANNING_ID THEPLANID,PLANNING.LASTUPDATE "
				+ "FROM PLANS,PLANNING "
				+ "WHERE PLANS.PLANNING_ID=PLANNING.PLANNING_ID "
				+ "AND PLANNING.WORKUNIT_ID=? "
				+ "AND PLANNING.YEAR=? "
				+ "AND PLANNING.VERSION=? "
				+ "ORDER BY METRIC_ID ";
		try {
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, wuID);
			prepStmt.setInt(2, year);
			prepStmt.setFloat(3, version);
			rs = prepStmt.executeQuery();
			int i, j;
			int  metricConstant;
			long groupID;
			while (rs.next()) {
				groupID = rs.getLong("GROUP_ID");
				metricConstant = rs.getInt("METRIC_ID");
				for (j = 0; j < planningInfo.rows.size(); j++) {
					row = (PlanningInfo.Row) planningInfo.rows.elementAt(j);
//					-2 are the headers
					if (row.groupID != -2 && row.metricConstant== metricConstant && row.groupID == groupID) {
						for (i = 1; i < 13; i++)
							row.values[i - 1] = Db.getDouble(rs, CommonTools.getMonth(i));
						row.assumption = rs.getString("ASSUMPTION");
						row.yearTotal = Db.getDouble(rs, "YEARTOTAL");
						if (planningInfo.planningID == 0)
							planningInfo.planningID = rs.getLong("THEPLANID");
						planningInfo.lastUpdate = rs.getDate("LASTUPDATE");
						break;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return planningInfo;
		}
	}
	public static boolean savePlanning(PlanningInfo planningInfo) {
		String sql, sqlInsert, sqlInsert2, sqlUpdate;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		PreparedStatement prepStmt2 = null;
		try {
			conn = ServerHelper.instance().getConnection();
			PlanningInfo.Row row;
			if (planningInfo.planningID == 0) {
				//new planning
				sql =
					"INSERT INTO PLANNING (PLANNING_ID,YEAR,WORKUNIT_ID,VERSION,LASTUPDATE)"
						+ " VALUES((SELECT NVL(MAX(PLANNING_ID)+1,1) FROM PLANNING),?,?,?,?)";
				sqlInsert =
					"INSERT INTO PLANS(PLANNING_ID,METRIC_ID,ASSUMPTION,GROUP_ID,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC,YEARTOTAL)"
						+ " VALUES((SELECT MAX(PLANNING_ID)FROM PLANNING),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setInt(1, planningInfo.year);
				prepStmt.setLong(2, planningInfo.workUnit);
				prepStmt.setFloat(3, planningInfo.version);
				prepStmt.setDate(4, planningInfo.lastUpdate);
				if (prepStmt.executeUpdate() < 1)
					return false;
				prepStmt.close();
				prepStmt = conn.prepareStatement(sqlInsert);
				for (int i = 0; i < planningInfo.rows.size(); i++) {
					row = (PlanningInfo.Row) planningInfo.rows.elementAt(i);
					if (row.groupID != -2) {
						//-2 are the headers
						prepStmt.setInt(1, row.metricConstant);
						prepStmt.setString(2, row.assumption);
						prepStmt.setLong(3, row.groupID);
						for (int k = 4; k < 16; k++)
							Db.setDouble(prepStmt, k, row.values[k - 4]);
						Db.setDouble(prepStmt, 16, row.yearTotal);
						prepStmt.executeUpdate();
					}
				}
			}
			else {
				//update planning
				sql = "UPDATE PLANNING SET LASTUPDATE=? WHERE PLANNING_ID=?";
				sqlUpdate =
					"UPDATE PLANS SET JAN=?,FEB=?,MAR=?,APR=?,MAY=?,JUN=?,JUL=?,AUG=?,SEP=?,OCT=?,NOV=?,DEC=?,ASSUMPTION=?,YEARTOTAL=?"
						+ " WHERE PLANNING_ID=? AND GROUP_ID=? AND METRIC_ID=?";
				sqlInsert2 =
					"INSERT INTO PLANS(PLANNING_ID,METRIC_ID,ASSUMPTION,GROUP_ID,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC,YEARTOTAL)"
						+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setDate(1, planningInfo.lastUpdate);
				prepStmt.setLong(2, planningInfo.planningID);
				if (prepStmt.executeUpdate() < 1)
					return false;
				prepStmt.close();
				prepStmt = conn.prepareStatement(sqlUpdate);
				for (int i = 0; i < planningInfo.rows.size(); i++) {
					row = (PlanningInfo.Row) planningInfo.rows.elementAt(i);
					if (row.groupID != -2) {
						//-2 are the headers
						for (int k = 1; k < 13; k++)
							Db.setDouble(prepStmt, k, row.values[k - 1]);
						prepStmt.setString(13, row.assumption);
						Db.setDouble(prepStmt, 14, row.yearTotal);
						prepStmt.setLong(15, planningInfo.planningID);
						prepStmt.setLong(16, row.groupID);
						prepStmt.setInt(17, row.metricConstant);
						if (prepStmt.executeUpdate() < 1) {
							prepStmt2 = conn.prepareStatement(sqlInsert2);
							prepStmt2.setLong(1, planningInfo.planningID);
							
							prepStmt2.setInt(2, row.metricConstant);
							prepStmt2.setString(3, row.assumption);
							prepStmt2.setLong(4, row.groupID);
							for (int k = 5; k < 17; k++)
								Db.setDouble(prepStmt2, k, row.values[k - 5]);
							Db.setDouble(prepStmt2, 17, row.yearTotal);
							prepStmt2.executeUpdate();
							prepStmt2.close();
						}
					}
				}
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
			ServerHelper.closeConnection(null, prepStmt2, null);
		}
	}
	/**
	 * 4 secial param at the end, the 3 first are used in jsp to calculate automatically the sum of data for the month of for the group
	 * the last param determines the input contraints for the plan
	 */
	private static void fillPlan(
		PlanningInfo planningInfo,
		Vector groups,
		int metricConstant,
		String colFormula,
		String rowFormula,
		String yearFormula,
		String inputConstraint) {
		PlanningInfo.Row row;
		planningInfo.rows.add(planningInfo.new Row(metricConstant, -1, "", "", "", yearFormula,inputConstraint));
		if ((!planningInfo.isGroup) && groups != null)
			for (int i = 0; i < groups.size(); i++) {
				GroupInfo grInfo = (GroupInfo) groups.elementAt(i);
				row = planningInfo.new Row(metricConstant, grInfo.groupID, grInfo.name, colFormula, rowFormula, "","");
				planningInfo.rows.add(row);
			}
	}
}
