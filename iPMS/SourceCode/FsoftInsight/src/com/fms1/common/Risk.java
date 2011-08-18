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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.fms1.infoclass.ProcessInfo;
import com.fms1.infoclass.RiskCategoryInfo;
import com.fms1.infoclass.RiskContingencyInfo;
import com.fms1.infoclass.RiskInfo;
import com.fms1.infoclass.RiskMitigationInfo;
import com.fms1.infoclass.RiskSourceInfo;
import com.fms1.tools.ConvertString;
import com.fms1.tools.Db;
import com.fms1.web.Parameters;
import com.fms1.web.ServerHelper;
/**
 * Risks logic
 * @author: Hoang My Duc
 */
public class Risk {
	/**
	 * add a risk
	 * @author: Hoang My Duc
	 * @return: Status
	 * @param: RiskInfo
	 */
	public static long addRisk(RiskInfo risk) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT DISTINCT MAX(RISK_ID) MAX_RISK_ID FROM RISK";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			long riskID;
			if ((rs != null) && (rs.next()))
				riskID = rs.getLong("MAX_RISK_ID") + 1;
			else
				riskID = 0;
				
			sql =
				"INSERT INTO RISK(RISK_ID, SOURCE_ID, TYPE, CONDITION, CONSEQUENCE, THRESHOLD, PROB, "
					+ "PLANNED_IMPACT, EXPOSURE, MITIGATION, MITIGATION_BENEFIT, ACTUAL_MITIGATION, CONTIGENCY_PLAN, "
					+ "TRIGGER_NAME, DEVELOPER_ACC, ASSESSMENT_DATE, STATUS, "
					+ "ACTUAL_RISK_SCENARIOR, ACTUAL_ACTION, ACTUAL_IMPACT, UNPLANNED, PROJECT_ID, PROCESS_ID, "
					+ "PRIORITY, RISK_PRIORITY, IMPACT, LAST_UPDATED_DATE) "
					+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, riskID);
			prepStmt.setLong(2, risk.sourceID);
			prepStmt.setInt(3, risk.type);
			prepStmt.setString(4, risk.condition);
			prepStmt.setString(5, risk.consequence);
			prepStmt.setString(6, risk.threshold);
			prepStmt.setDouble(7, risk.probability);
			prepStmt.setString(8, risk.plannedImpact);
			prepStmt.setDouble(9, risk.exposure);
			prepStmt.setString(10, risk.mitigation);
			prepStmt.setString(11, risk.mitigationBenefit);
			prepStmt.setString(12, risk.mitigationActual);
			prepStmt.setString(13, risk.contingencyPlan);
			prepStmt.setString(14, risk.triggerName);
			prepStmt.setString(15, risk.developerAcc);
			prepStmt.setDate(16, risk.assessmentDate);
			prepStmt.setInt(17, risk.riskStatus);
			prepStmt.setString(18, risk.actualRiskScenario);
			prepStmt.setString(19, risk.actualAction);
			prepStmt.setString(20, risk.actualImpact);
			prepStmt.setInt(21, risk.unplanned);
			prepStmt.setLong(22, risk.projectID);
			prepStmt.setInt(23, risk.processId);
			prepStmt.setInt(24, risk.priority);
			prepStmt.setInt(25, risk.riskPriority);
			prepStmt.setDouble(26, risk.impact);
			prepStmt.setDate(27, risk.lastUpdatedDate);
			prepStmt.executeUpdate();
			return riskID;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1L;
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,null);
		}

	}
	
	//LAMNT3 - 2008-10-20
	public static boolean addRiskMitigation(RiskMitigationInfo riskMitigation) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT DISTINCT MAX(RISK_MITIGATION_ID) MAX_RISK_MITIGATION_ID FROM RISK_MITIGATION";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			long riskMitigationID;
			if ((rs != null) && (rs.next()))
				riskMitigationID = rs.getLong("MAX_RISK_MITIGATION_ID") + 1;
			else
				riskMitigationID = 0;
				
			sql =
				"INSERT INTO RISK_MITIGATION(RISK_MITIGATION_ID, RISK_ID, MITIGATION, CONTIGENCY, MITIGATION_COST, MITIGATION_BENEFIT, DEVELOPER_ACC, "
					+ " PLAN_END_DATE, ACTUAL_END_DATE, ACTION_STATUS) "
					+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, riskMitigationID);
			prepStmt.setLong(2, riskMitigation.riskID);
			prepStmt.setString(3, riskMitigation.mitigation);
			prepStmt.setString(4, riskMitigation.contingency);
			prepStmt.setDouble(5, riskMitigation.mitigationCost);
			prepStmt.setString(6, riskMitigation.mitigationBenefit);
			prepStmt.setString(7, riskMitigation.personInCharge);
			prepStmt.setDate(8, riskMitigation.planEndDate);
			prepStmt.setDate(9, riskMitigation.actualEndDate);
			prepStmt.setInt(10, riskMitigation.actionStatus);
			return (prepStmt.executeUpdate()>0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,null);
		}
	}
//	LAMNT3 - 2008-11-01
	  public static boolean updateRiskMitigation(RiskMitigationInfo riskMitigation) {
		  String sql = null;
		  Connection conn = null;
		  PreparedStatement prepStmt = null;
		  ResultSet rs=null;
		  try {
			  conn = ServerHelper.instance().getConnection();
			  
			  sql =
				  "UPDATE RISK_MITIGATION SET MITIGATION = ?, CONTIGENCY = ?, MITIGATION_COST = ?, MITIGATION_BENEFIT = ?, DEVELOPER_ACC = ?, "
					  + " PLAN_END_DATE = ?, ACTUAL_END_DATE = ?, ACTION_STATUS = ? "
					  + " WHERE RISK_MITIGATION_ID = ? ";
			  prepStmt = conn.prepareStatement(sql);
			  prepStmt.setString(1, riskMitigation.mitigation);
			  prepStmt.setString(2, riskMitigation.contingency);
			  prepStmt.setDouble(3, riskMitigation.mitigationCost);
			  prepStmt.setString(4, riskMitigation.mitigationBenefit);
			  prepStmt.setString(5, riskMitigation.personInCharge);
			  prepStmt.setDate(6, riskMitigation.planEndDate);
			  prepStmt.setDate(7, riskMitigation.actualEndDate);
			  prepStmt.setInt(8, riskMitigation.actionStatus);
			  prepStmt.setLong(9, riskMitigation.riskMitigationId);
			  return (prepStmt.executeUpdate()>0);
		  }
		  catch (Exception e) {
			  e.printStackTrace();
			  return false;
		  }
		  finally {
			  ServerHelper.closeConnection(conn,prepStmt,null);
		  }
	  }
	
	public static long getRiskID() {
			String sql = null;
			Connection conn = null;
			PreparedStatement prepStmt = null;
			ResultSet rs=null;
			long riskID = 0;
			try {
				conn = ServerHelper.instance().getConnection();
				sql = "SELECT DISTINCT MAX(RISK_ID) MAX_RISK_ID FROM RISK";
				prepStmt = conn.prepareStatement(sql);
				rs = prepStmt.executeQuery();
				if ((rs != null) && (rs.next()))
					riskID = rs.getLong("MAX_RISK_ID");
				else
					riskID = 0;
			}
			catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			finally {
				ServerHelper.closeConnection(conn,prepStmt,null);
				return riskID;
			}
		}
	/**
	 * change a risk
	 * @author: Hoang My Duc
	 * @return: Status
	 * @param: RiskInfo
	 */
	public static long updateRisk(RiskInfo risk) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE RISK SET SOURCE_ID = ?, TYPE = ?, CONDITION = ?, CONSEQUENCE = ?, THRESHOLD = ? ,PROB = ?, "
					+ "PLANNED_IMPACT = ?, EXPOSURE = ?, MITIGATION = ?, MITIGATION_BENEFIT = ? , ACTUAL_MITIGATION = ? , "
					+ "CONTIGENCY_PLAN = ?, TRIGGER_NAME = ?, DEVELOPER_ACC = ?, "
					+ "ASSESSMENT_DATE = ?, STATUS = ?, ACTUAL_RISK_SCENARIOR = ?, "
					+ "ACTUAL_ACTION = ?, ACTUAL_IMPACT = ?, UNPLANNED = ?, PROJECT_ID = ?, PROCESS_ID = ?, "
					+ "PRIORITY = ?, RISK_PRIORITY = ?, IMPACT = ?, LAST_UPDATED_DATE = ? "
					+ "WHERE RISK_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, risk.sourceID);
			prepStmt.setInt(2, risk.type);
			prepStmt.setString(3, risk.condition);
			prepStmt.setString(4, risk.consequence);
			prepStmt.setString(5, risk.threshold);
			prepStmt.setDouble(6, risk.probability);
			prepStmt.setString(7, risk.plannedImpact);
			prepStmt.setDouble(8, risk.exposure);
			prepStmt.setString(9, risk.mitigation);
			prepStmt.setString(10, risk.mitigationBenefit);
			prepStmt.setString(11, risk.mitigationActual);
			prepStmt.setString(12, risk.contingencyPlan);
			prepStmt.setString(13, risk.triggerName);
			prepStmt.setString(14, risk.developerAcc);
			prepStmt.setDate(15, risk.assessmentDate);
			prepStmt.setInt(16, risk.riskStatus);
			prepStmt.setString(17, risk.actualRiskScenario);
			prepStmt.setString(18, risk.actualAction);
			prepStmt.setString(19, risk.actualImpact);
			prepStmt.setInt(20, risk.unplanned);
			prepStmt.setLong(21, risk.projectID);
			prepStmt.setInt(22, risk.processId);
			prepStmt.setInt(23, risk.priority);
			prepStmt.setInt(24, risk.riskPriority);
			prepStmt.setDouble(25, risk.impact);
			prepStmt.setDate(26, risk.lastUpdatedDate);
			prepStmt.setLong(27, risk.riskID);
			
			prepStmt.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1L;
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,null);
			return risk.riskID;
		}
	}

	/**
	 * get a list of risks
	 * @author: Hoang My Duc
	 * @return: RiskInfo
	 * @param: ProjectID
	 */
	public static Vector getRiskList(long projectID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		Vector riskList = new Vector();
		ResultSet rs =null;
		try {
			conn = ServerHelper.instance().getConnection();
			// Modify by HaiMM
			sql =
				"SELECT RISK.*, DEVELOPER.*, risk_source.source_name FROM RISK, DEVELOPER, risk_source WHERE RISK.source_id = risk_source.source_id AND RISK.PROJECT_ID = ? AND DEVELOPER_ACC = ACCOUNT(+) ORDER BY EXPOSURE DESC";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				RiskInfo risk = new RiskInfo();
				risk.riskID = rs.getLong("RISK_ID");				
				risk.sourceID = rs.getLong("SOURCE_ID");
				risk.type = rs.getInt("TYPE");
				risk.condition = rs.getString("CONDITION");
				risk.consequence = rs.getString("CONSEQUENCE");
				risk.threshold = rs.getString("THRESHOLD");
				risk.probability = rs.getDouble("PROB");
				risk.plannedImpact = rs.getString("PLANNED_IMPACT");
				risk.mitigation = rs.getString("MITIGATION");
				risk.mitigationBenefit = rs.getString("MITIGATION_BENEFIT");
				risk.mitigationActual = rs.getString("ACTUAL_MITIGATION");
				risk.contingencyPlan = rs.getString("CONTIGENCY_PLAN");
				risk.triggerName = rs.getString("TRIGGER_NAME");
				risk.developerAcc = rs.getString("DEVELOPER_ACC");
				risk.assessmentDate = rs.getDate("ASSESSMENT_DATE");
				risk.riskStatus = rs.getInt("STATUS");
				risk.actualRiskScenario = rs.getString("ACTUAL_RISK_SCENARIOR");
				risk.actualAction = rs.getString("ACTUAL_ACTION");
				risk.actualImpact = rs.getString("ACTUAL_IMPACT");
				risk.unplanned = rs.getInt("UNPLANNED");
				risk.projectID = rs.getLong("PROJECT_ID");
				risk.exposure = rs.getDouble("EXPOSURE");
				risk.developerName = rs.getString("NAME");
				risk.processId  = rs.getInt("PROCESS_ID");
				risk.priority  = rs.getInt("PRIORITY");
				risk.riskPriority  = rs.getInt("RISK_PRIORITY");
				risk.lastUpdatedDate = rs.getDate("LAST_UPDATED_DATE");
				risk.impact = rs.getDouble("IMPACT");
				risk.sourceName = rs.getString("source_name"); // Added by HaiMM
				if (risk.processId ==0)
					risk.processId=ProcessInfo.OTHER;
				risk.parseImpact();
				riskList.addElement(risk);				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return riskList;
		}
	}
	
	/**
	 * get a list other risks
	 * @author: LAMNT3 - 20081020
	 * @return: RiskInfo
	 * @param: ProjectID
	 */
	public static RiskInfo getRiskById(long riskID) {
		RiskInfo risk = new RiskInfo();;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs =null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				" SELECT * FROM RISK r, risk_source rs, risk_category rc, DEVELOPER " +
				" WHERE r.RISK_ID = ? " +
				" AND DEVELOPER_ACC = ACCOUNT(+) " +
				" AND r.source_id = rs.source_id " +
				" AND rs.category_id = rc.category_id ";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, riskID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				risk.riskID = rs.getLong("RISK_ID");
				risk.categoryName = rs.getString("CATEGORY_NAME");
				risk.sourceName = rs.getString("SOURCE_NAME");
				risk.sourceID = rs.getLong("SOURCE_ID");
				risk.type = rs.getInt("TYPE");
				risk.condition = rs.getString("CONDITION");
				risk.consequence = rs.getString("CONSEQUENCE");
				risk.threshold = rs.getString("THRESHOLD");
				risk.probability = rs.getDouble("PROB");
				risk.plannedImpact = rs.getString("PLANNED_IMPACT");
				risk.mitigation = rs.getString("MITIGATION");
				risk.mitigationBenefit = rs.getString("MITIGATION_BENEFIT");
				risk.mitigationActual = rs.getString("ACTUAL_MITIGATION");
				risk.contingencyPlan = rs.getString("CONTIGENCY_PLAN");
				risk.triggerName = rs.getString("TRIGGER_NAME");
				risk.developerAcc = rs.getString("DEVELOPER_ACC");
				risk.assessmentDate = rs.getDate("ASSESSMENT_DATE");
				risk.riskStatus = rs.getInt("STATUS");
				risk.actualRiskScenario = rs.getString("ACTUAL_RISK_SCENARIOR");
				risk.actualAction = rs.getString("ACTUAL_ACTION");
				risk.actualImpact = rs.getString("ACTUAL_IMPACT");
				risk.unplanned = rs.getInt("UNPLANNED");
				risk.projectID = rs.getLong("PROJECT_ID");
				risk.exposure = rs.getDouble("EXPOSURE");
				risk.developerName = rs.getString("NAME");
				risk.processId  = rs.getInt("PROCESS_ID");
				risk.priority  = rs.getInt("PRIORITY");
				risk.riskPriority = rs.getInt("RISK_PRIORITY");
				risk.impact = rs.getDouble("IMPACT");
				risk.lastUpdatedDate = rs.getDate("LAST_UPDATED_DATE");
				if (risk.processId ==0)
					risk.processId=ProcessInfo.OTHER;
				risk.parseImpact();				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return risk;
		}
	}
	/**
	 * get a list other risks
	 * @author: LAMNT3 - 20081020
	 * @return: RiskInfo
	 * @param: ProjectID
	 */
	public static Vector getMitigationRiskByRiskId(long riskID) {
		RiskMitigationInfo risk;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs =null;
		Vector vMitigation = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT * FROM risk_mitigation r WHERE r.risk_id = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, riskID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				risk = new RiskMitigationInfo();
				risk.riskMitigationId = rs.getLong("risk_mitigation_id");				
				risk.riskID = rs.getLong("risk_id");
				risk.mitigation = rs.getString("MITIGATION");
				risk.mitigationCost = rs.getDouble("mitigation_cost");
				risk.mitigationBenefit = rs.getString("mitigation_benefit");
				risk.personInCharge = rs.getString("developer_acc");
				risk.planEndDate = rs.getDate("plan_end_date");
				risk.actualEndDate = rs.getDate("actual_end_date");
				risk.actionStatus = rs.getInt("ACTION_STATUS");
				vMitigation.add(risk);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return vMitigation;
		}
	}
	
	/**
	 * get a list other risks
	 * @author: HaiMM - 20081209
	 * @return: RiskInfo
	 * @param: ProjectID
	 */
	public static Vector getContigencyByRiskId(long riskID) {
		RiskContingencyInfo risk;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs =null;
		Vector vContigency = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT * FROM risk_contigency r WHERE r.risk_id = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, riskID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				risk = new RiskContingencyInfo();
				risk.riskContingencyId= rs.getLong("risk_contigency_id");				
				risk.riskID = rs.getLong("risk_id");
				risk.contingency = rs.getString("contigency");
				risk.personInCharge = rs.getString("developer_acc");
				risk.planEndDate = rs.getDate("plan_end_date");
				risk.actualEndDate = rs.getDate("actual_end_date");
				risk.actionStatus = rs.getInt("ACTION_STATUS");
				vContigency.add(risk);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return vContigency;
		}
	}
	
	public static String getAllContigencyByRiskId(long riskID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs =null;
		String contigency = "";
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT contigency FROM risk_contigency r WHERE r.risk_id = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, riskID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				contigency += rs.getString("contigency") + "\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return contigency;
		}
	}
	
	/**
	 * get a list other risks
	 * @author: LAMNT3
	 * @return: RiskInfo
	 * @param: ProjectID
	 */
	public static Vector getOtherRiskList() {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		Vector riskList = new Vector();
		ResultSet rs =null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT a.risk_id, p.code AS project_code, a.source_id,r.source_name, r.category_id," 
				+ " a.condition, p.CATEGORY as project_category "
				+ " FROM risk a, project p, risk_source r "
				+ " WHERE p.project_id = a.project_id "
				+ " AND a.source_id = r.source_id "
				+ " AND p.status = '1' "
				+ " AND a.status = '2' "
				+ " AND a.condition is not null ";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				RiskInfo risk = new RiskInfo();
				risk.riskID = rs.getLong("RISK_ID");				
				risk.sourceID = rs.getLong("SOURCE_ID");
				risk.sourceName = rs.getString("source_name");
				risk.condition = rs.getString("CONDITION");
				risk.projectCode = rs.getString("project_code");
				risk.project_category = rs.getInt("project_category");
				
				riskList.addElement(risk);				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return riskList;
		}
	}
//LAMNT3 - 20081101
	public static boolean delRisk(long riskID) {
        if(Db.delete(riskID,"RISK_ID","RISK_MITIGATION") && Db.delete(riskID,"RISK_ID","RISK_CONTIGENCY")
        	&&(Db.delete(riskID,"RISK_ID","OTHER_ACTIVITY")))
			return Db.delete(riskID,"RISK_ID","RISK");
		else
			return false;	
	}

//	LAMNT3 - 20081101
	public static boolean delRiskMitigation(long riskMitigationID){
		return Db.delete(riskMitigationID,"RISK_MITIGATION_ID","RISK_MITIGATION");	
	}

// Added by HaiMM
	public static boolean delRiskContingency(long riskContingencyID){
		return Db.delete(riskContingencyID,"RISK_CONTIGENCY_ID","RISK_CONTIGENCY");	
	}

	/**
	 * result [0]= total num of risks
	 * result [1]= num of risks occurred
	 * 
	 */
	public static int[] getRiskCount(long prjID, java.sql.Date sRptDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs =null;
		int [] result =new int [2];
		result [0]=0;
		result [1]=0;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT COUNT(RISK_ID) N_RISK FROM RISK"
			+"  WHERE ASSESSMENT_DATE <= ? AND PROJECT_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setDate(1, sRptDate);
			prepStmt.setLong(2, prjID);
			rs = prepStmt.executeQuery();
			if ( rs.next()) {
				result [0]= rs.getInt("N_RISK");
			}
			rs.close();
			prepStmt.close();
			sql = "SELECT COUNT(RISK_ID) N_RISK FROM RISK " 
			+ "WHERE STATUS = 2 AND ASSESSMENT_DATE <= ? AND PROJECT_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setDate(1, sRptDate);
			prepStmt.setLong(2, prjID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				result [1]= rs.getInt("N_RISK");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return result;
		}
	}
	/**
	 * For projects
	 */
	public static final Vector getOccuredRisks(final long projectID, final java.util.Date reportDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs=null;
		final Vector list = new Vector();
		final java.sql.Date sRptDate = new java.sql.Date(reportDate.getTime());
		try {
			sql =
				"SELECT "
					+ "ACTUAL_RISK_SCENARIOR, ACTUAL_ACTION,  "
					+ "ACTUAL_IMPACT, "
					+ "STATUS, "
					+ "UNPLANNED, ASSESSMENT_DATE "
					+ "FROM  "
					+ "RISK "
					+ "WHERE "
					+ "ASSESSMENT_DATE <= ? AND "
					+ "STATUS = 2 AND "
					+ "PROJECT_ID = ?";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setDate(1, sRptDate);
			prepStmt.setLong(2, projectID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final RiskInfo info = new RiskInfo();
				info.actualRiskScenario = rs.getString("ACTUAL_RISK_SCENARIOR");
				info.actualAction = rs.getString("ACTUAL_ACTION");
				info.actualImpact = rs.getString("ACTUAL_IMPACT");
				info.unplanned = rs.getInt("UNPLANNED");
				info.assessmentDate = rs.getDate("ASSESSMENT_DATE");
				info.riskStatus = rs.getInt("STATUS");
				info.parseImpact();
				list.addElement(info);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
		return list;
	}
	/**
	 * For groups
	 */
	public static Vector getBaselinedRisks() {
		return getOccuredRiskList(Parameters.FSOFT_WU, null,null, " AND BASELINED=1 ");
	}
	public static Vector getOccuredRiskList(final long workUnitID, final java.sql.Date beginMonth, final java.sql.Date endMonth, final String strWhere) {
		Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			//note when called from getBaselinedRisks, beginmonth is null
		sql =	"SELECT PROJECT.CODE, PROJECT.GROUP_NAME, PROJECT.CATEGORY, c.*, PROJECT.START_DATE "
				+ " FROM WORKUNIT a,WORKUNIT b, PROJECT, RISK c"
				+ " WHERE a.type(+)= 2 AND c.STATUS = 2 AND"
				+ " PROJECT.PROJECT_ID = a.TABLEID (+) "
				+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
				+ " AND (a.PARENTWORKUNITID=? OR b.PARENTWORKUNITID=?)"
				+ " AND PROJECT.PROJECT_ID (+)= c.PROJECT_ID"
				+ ((beginMonth!=null)?" AND PROJECT.ACTUAL_FINISH_DATE >=? AND PROJECT.ACTUAL_FINISH_DATE <=?":"")
				+ strWhere
				+ ((beginMonth!=null)?" ORDER BY PROJECT.CODE":" ORDER BY C.PROCESS_ID");
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, workUnitID);
			stm.setLong(2, workUnitID);
			if (beginMonth!=null){
				stm.setDate(3, beginMonth);
				stm.setDate(4, endMonth);
			}

			rs = stm.executeQuery();
			
			while (rs.next()) {
				RiskInfo riskInfo = new RiskInfo();
				
				riskInfo.riskID = rs.getLong("RISK_ID");
				riskInfo.projectID = rs.getInt("PROJECT_ID");
				riskInfo.projectCode = rs.getString("CODE");
				riskInfo.groupName = rs.getString("GROUP_NAME");
				riskInfo.projectType = rs.getInt("CATEGORY");
				riskInfo.start_date = rs.getDate("START_DATE");

				riskInfo.actualRiskScenario = rs.getString("ACTUAL_RISK_SCENARIOR");
				riskInfo.actualAction = rs.getString("ACTUAL_ACTION");
				riskInfo.actualImpact = rs.getString("ACTUAL_IMPACT");
				riskInfo.unplanned  = rs.getInt("UNPLANNED");
				riskInfo.exposure = rs.getInt("EXPOSURE");				
				
				riskInfo.sourceID = rs.getLong("SOURCE_ID");
				riskInfo.type = rs.getInt("TYPE");
				riskInfo.condition = rs.getString("CONDITION");
				riskInfo.consequence = rs.getString("CONSEQUENCE");
				riskInfo.threshold = rs.getString("THRESHOLD");
				riskInfo.probability = rs.getInt("PROB");
				riskInfo.plannedImpact = rs.getString("PLANNED_IMPACT");
				riskInfo.mitigation = rs.getString("MITIGATION");
				riskInfo.mitigationBenefit = rs.getString("MITIGATION_BENEFIT");
				riskInfo.mitigationActual = rs.getString("ACTUAL_MITIGATION");
				riskInfo.contingencyPlan = rs.getString("CONTIGENCY_PLAN");
				riskInfo.triggerName = rs.getString("TRIGGER_NAME");
				riskInfo.developerAcc = rs.getString("DEVELOPER_ACC");
				riskInfo.assessmentDate = rs.getDate("ASSESSMENT_DATE");
				//if process is not defined then process is "other"
				riskInfo.processId  = rs.getInt("PROCESS_ID");
				if (riskInfo.processId ==0)
					riskInfo.processId=ProcessInfo.OTHER;
				riskInfo.baselined=(rs.getInt("BASELINED")==1);
				riskInfo.parseImpact();
				resultVector.addElement(riskInfo);
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
	/**
	 * Change the status of risk baseline
	 */
	public static final void updateBaseline(final long [] riskIDs) {
		Connection conn = null;
		Statement prepStmt = null;
		String sql = null;
		try {
			sql =
				"UPDATE RISK SET BASELINED= DECODE(BASELINED,0,1,null,1,1,0) "
					+ "WHERE "
					+ "RISK_ID IN("+ConvertString.arrayToString(riskIDs,",")+")";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.createStatement();
			prepStmt.executeQuery(sql);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}

	}
	
	/**
	 * Get risk category
	 * @author binhnt	 
	 * @return Vector riskCategory
	 */
	public static Vector getRiskCategory(){
		Connection conn=null;
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		String sql = null;
		Vector riskCategory = new Vector();
		try{
			conn=ServerHelper.instance().getConnection();
			sql = "SELECT * FROM RISK_CATEGORY WHERE CATEGORY_ID >= 20 ORDER BY CATEGORY_ID";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				RiskCategoryInfo riskCatInfo= new RiskCategoryInfo();
				riskCatInfo.categoryID = rs.getLong("CATEGORY_ID");
				riskCatInfo.categoryName = rs.getString("CATEGORY_NAME");
				riskCategory.addElement(riskCatInfo);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return riskCategory;
		}		
	}
	/**
	 * Get risk category
	 * @author binhnt	 
	 * @return Vector riskSource
	 */
	public static Vector getRiskSource(){
		Connection conn=null;	
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		String sql = null;
		Vector riskSource = new Vector();
		try{
			conn=ServerHelper.instance().getConnection();
			sql = "SELECT RISK_SOURCE.* FROM RISK_SOURCE INNER JOIN RISK_CATEGORY ON RISK_SOURCE.CATEGORY_ID=RISK_CATEGORY.CATEGORY_ID ORDER BY RISK_SOURCE.CATEGORY_ID,RISK_SOURCE.SOURCE_NAME";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				RiskSourceInfo riskSourceInfo= new RiskSourceInfo();
				riskSourceInfo.sourceID = rs.getLong("SOURCE_ID");
				riskSourceInfo.categoryID = rs.getLong("CATEGORY_ID");
				riskSourceInfo.sourceName = rs.getString("SOURCE_NAME");
				riskSource.addElement(riskSourceInfo);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return riskSource;
		}		
	}	
	
	// Added by HaiMM - 11/DEC/2008
	public static Vector getAllRiskSource(){
		Connection conn=null;	
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		String sql = null;
		Vector riskSource = new Vector();
		try{
			conn=ServerHelper.instance().getConnection();
			sql = "SELECT RISK_SOURCE.* FROM RISK_SOURCE WHERE RISK_SOURCE.source_id > 100 ORDER BY RISK_SOURCE.CATEGORY_ID,RISK_SOURCE.SOURCE_NAME";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				RiskSourceInfo riskSourceInfo= new RiskSourceInfo();
				riskSourceInfo.sourceID = rs.getLong("SOURCE_ID");
				riskSourceInfo.categoryID = rs.getLong("CATEGORY_ID");
				riskSourceInfo.sourceName = rs.getString("SOURCE_NAME");
				riskSource.addElement(riskSourceInfo);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return riskSource;
		}		
	}	


	/**
	 * Get Top Common Risk
	 * @author LAMNT3 - 20081020
	 * @return Vector riskSource
	 */
	public static Vector getTopCommonRiskSource(){
		Connection conn=null;	
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		String sql = null;
		Vector riskSource = new Vector();
		try{
			conn=ServerHelper.instance().getConnection();
			sql = "select rs.source_id, rs.source_name, rc.category_name, rs.TOP_RISK "
	  			+ " from risk_source rs, risk_category rc "
				+ " where rs.category_id = rc.category_id "
				+ " and rs.top_risk >0 "
				+ " order by top_risk, SOURCE_NAME ";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				RiskSourceInfo riskSourceInfo= new RiskSourceInfo();
				riskSourceInfo.sourceID = rs.getLong("SOURCE_ID");
				riskSourceInfo.sourceName = rs.getString("SOURCE_NAME");
				riskSourceInfo.categoryName = rs.getString("category_name");
				riskSourceInfo.topRisk = rs.getInt("TOP_RISK");
				riskSource.addElement(riskSourceInfo);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return riskSource;
		}		
	}
	/**
	 * Get Risk Database
	 * @author LAMNT3 - 20090403
	 * @return Vector riskSource
	 */
	public static Vector getRiskDatabase(){
		Connection conn=null;	
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		String sql = null;
		Vector riskSource = new Vector();
		try{
			conn=ServerHelper.instance().getConnection();
			sql = "select rs.source_id, rs.source_name, rc.category_name, rs.TOP_RISK "
				+ " from risk_source rs, risk_category rc "
				+ " where rs.category_id = rc.category_id "
				+ " and rs.SOURCE_ID > 100 "
				+ " order by top_risk, SOURCE_NAME ";
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				RiskSourceInfo riskSourceInfo= new RiskSourceInfo();
				riskSourceInfo.sourceID = rs.getLong("SOURCE_ID");
				riskSourceInfo.sourceName = rs.getString("SOURCE_NAME");
				riskSourceInfo.categoryName = rs.getString("category_name");
				riskSourceInfo.topRisk = rs.getInt("TOP_RISK");
				riskSource.addElement(riskSourceInfo);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return riskSource;
		}		
	}
	/**
	 * Get Risk Update To
	 * @author LAMNT3 - 20090407
	 * @return Vector riskSource
	 */
	public static Vector getRiskUpdateTo(long sourceUpdated){
		Connection conn=null;	
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		String sql = null;
		Vector riskSource = new Vector();
		try{
			conn=ServerHelper.instance().getConnection();
			sql = "select rs.source_id, rs.source_name, rc.category_name, rs.TOP_RISK "
				+ " from risk_source rs, risk_category rc "
				+ " where rs.category_id = rc.category_id "
				+ " and rs.SOURCE_ID > 100 "
				+ " and (rs.top_risk is null or rs.SOURCE_ID = ?) "
				+ " order by top_risk, SOURCE_NAME ";
			prepStmt = conn.prepareStatement(sql);
			
			prepStmt.setLong(1, sourceUpdated);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				RiskSourceInfo riskSourceInfo= new RiskSourceInfo();
				riskSourceInfo.sourceID = rs.getLong("SOURCE_ID");
				riskSourceInfo.sourceName = rs.getString("SOURCE_NAME");
				riskSourceInfo.categoryName = rs.getString("category_name");
				riskSourceInfo.topRisk = rs.getInt("TOP_RISK");
				riskSource.addElement(riskSourceInfo);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return riskSource;
		}		
	}
	
	/**
	 * Get Risk for Add function
	 * @author LAMNT3 - 20090407
	 * @return Vector riskSource
	 */
	public static Vector getRiskForAdd(){
		Connection conn=null;	
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		String sql = null;
		Vector riskSource = new Vector();
		try{
			conn=ServerHelper.instance().getConnection();
			sql = "select rs.source_id, rs.source_name, rc.category_name, rs.TOP_RISK "
				+ " from risk_source rs, risk_category rc "
				+ " where rs.category_id = rc.category_id "
				+ " and rs.SOURCE_ID > 100 "
				+ " and rs.top_risk is null "
				+ " order by top_risk, SOURCE_NAME ";
			prepStmt = conn.prepareStatement(sql);
		
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				RiskSourceInfo riskSourceInfo= new RiskSourceInfo();
				riskSourceInfo.sourceID = rs.getLong("SOURCE_ID");
				riskSourceInfo.sourceName = rs.getString("SOURCE_NAME");
				riskSourceInfo.categoryName = rs.getString("category_name");
				riskSourceInfo.topRisk = rs.getInt("TOP_RISK");
				riskSource.addElement(riskSourceInfo);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return riskSource;
		}		
	}
		
	/**
	 * Update Risk Source
	 * @author LAMNT3 - 20081020
	 * @return Vector riskSource
	 */
	public static final void updateRiskSource(long sourceID,double topRisk){
		Connection conn=null;	
		PreparedStatement prepStmt = null;
		String sql = null;
		try{
			conn=ServerHelper.instance().getConnection();
			if(topRisk == 0){
			sql = "Update risk_source rs set rs.TOP_RISK = null "
				+ " where rs.source_id = ?";
			}else{
				sql = "Update risk_source rs set rs.TOP_RISK = ? "
					+ " where rs.source_id = ?";
			}
			prepStmt = conn.prepareStatement(sql);
			if(topRisk == 0){
				prepStmt.setLong(1,sourceID);
			}else{
				prepStmt.setDouble(1,topRisk);
				prepStmt.setLong(2,sourceID);
			}
			prepStmt.executeQuery();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			ServerHelper.closeConnection(conn,prepStmt,null);
		}		
	}
	
	/**
	 * Save Mitigation/Contingency update form (INSERT, UPDATE, DELETE)
	 * @param mitigations
	 * @param contingencies
	 * @return
	 */
	public static boolean saveMitiContiUpdate(
		Vector mitigations, Vector contingencies)
	{
		boolean result = false;
		Connection conn = null;
		PreparedStatement stm = null;
		String sqlInsertMiti = "INSERT INTO RISK_MITIGATION(RISK_MITIGATION_ID, RISK_ID, MITIGATION, MITIGATION_COST, MITIGATION_BENEFIT, DEVELOPER_ACC, "
					+ " PLAN_END_DATE, ACTUAL_END_DATE, ACTION_STATUS) "
					+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		String sqlInsertContin = "INSERT INTO RISK_CONTIGENCY(RISK_CONTIGENCY_ID, RISK_ID, CONTIGENCY, DEVELOPER_ACC, "
					+ " PLAN_END_DATE, ACTUAL_END_DATE, ACTION_STATUS) "
					+ " VALUES ( ?, ?, ?, ?, ?, ?, ?) ";
        
		// For records being updated, add more conditions to sure the exact data
		String sqlUpdateMiti = "UPDATE RISK_MITIGATION SET MITIGATION = ?, MITIGATION_COST = ?, MITIGATION_BENEFIT = ?, DEVELOPER_ACC = ?, "
				  + " PLAN_END_DATE = ?, ACTUAL_END_DATE = ?, ACTION_STATUS = ? "
				  + " WHERE RISK_MITIGATION_ID = ? ";
		String sqlUpdateContin = "UPDATE RISK_CONTIGENCY SET CONTIGENCY=?, "
			+ " DEVELOPER_ACC=?,PLAN_END_DATE=?,ACTUAL_END_DATE=?,ACTION_STATUS=?"
			+ " WHERE RISK_CONTIGENCY_ID=? ";
        
		// For records being deleted, add more conditions to sure the exact data
		String sqlDeleteMiti = "DELETE RISK_MITIGATION"
			+ " WHERE RISK_MITIGATION_ID=? AND RISK_ID=?";
		String sqlDeleteContin = "DELETE RISK_CONTIGENCY"
			+ " WHERE RISK_CONTIGENCY_ID=? AND RISK_ID=?";
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);  // Multi-records => enable transaction
			// Mitigation
			for (int i = 0; i < mitigations.size(); i++) {
				RiskMitigationInfo mitigationInfo = (RiskMitigationInfo) mitigations.get(i);
				// This record marked as being updated
				if (mitigationInfo.dmlType == RiskInfo.DML_UPDATE) {
					stm = conn.prepareStatement(sqlUpdateMiti);
					setMitiUpdateFields(stm, mitigationInfo);
					stm.executeUpdate();
				}
				// This record marked as being deleted
				else if (mitigationInfo.dmlType == RiskInfo.DML_DELETE) {
					stm = conn.prepareStatement(sqlDeleteMiti);
					setMitiDeleteFields(stm, mitigationInfo);
					stm.executeUpdate();
				}
				// Otherwise, this record marked as being inserted
				else {
					long id = ServerHelper.getNextSeq("RISK_MITIGATION_SEQ");
					stm = conn.prepareStatement(sqlInsertMiti);
					setMitiInsertFields(stm, id, mitigationInfo);
					stm.executeUpdate();
				}
				// Close before prepare to open another statement
				stm.close();
			}
			// Contingency
			for (int i = 0; i < contingencies.size(); i++) {
				RiskContingencyInfo contingencyInfo = (RiskContingencyInfo) contingencies.get(i);
				// This record marked as being updated
				if (contingencyInfo.dmlType == RiskInfo.DML_UPDATE) {
					stm = conn.prepareStatement(sqlUpdateContin);
					setContinUpdateFields(stm, contingencyInfo);
					stm.executeUpdate();
				}
				// This record marked as being deleted
				else if (contingencyInfo.dmlType == RiskInfo.DML_DELETE) {
					stm = conn.prepareStatement(sqlDeleteContin);
					setContinDeleteFields(stm, contingencyInfo);
					stm.executeUpdate();
				}
				// Otherwise, this record marked as being inserted
				else {
					long id = ServerHelper.getNextSeq("RISK_CONTINGENCY_SEQ");
					stm = conn.prepareStatement(sqlInsertContin);
					setContinInsertFields(stm, id, contingencyInfo);
					stm.executeUpdate();
				}
				// Close before prepare to open another statement
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
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return result;
		}
	}
	
	public static boolean addMitigationTemp(Vector mitigations)
	{
		boolean result = false;
		Connection conn = null;
		PreparedStatement stm = null;
		String sqlInsertMiti = "INSERT INTO RISK_MITIGATION(RISK_MITIGATION_ID, RISK_ID, MITIGATION, MITIGATION_COST, MITIGATION_BENEFIT, DEVELOPER_ACC, "
					+ " PLAN_END_DATE, ACTUAL_END_DATE, ACTION_STATUS) "
					+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);  // Multi-records => enable transaction
			for (int i = 0; i < mitigations.size(); i++) {
				RiskMitigationInfo mitigationInfo = (RiskMitigationInfo) mitigations.get(i);
				long id = ServerHelper.getNextSeq("RISK_MITIGATION_SEQ");
				stm = conn.prepareStatement(sqlInsertMiti);
				setMitiInsertFields(stm, id, mitigationInfo);
				stm.executeUpdate();
				
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
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return result;
		}
	}
	
	/**
	 * Set data for prepared statement, used for Insert new Mitigation
	 * @param stm
	 * @param id
	 * @param info
	 * @throws SQLException
	 */
	private static void setMitiInsertFields(PreparedStatement prepStmt,
		long riskMitigationID, RiskMitigationInfo riskMitigation) throws SQLException
	{
		prepStmt.setLong(1, riskMitigationID);
		prepStmt.setLong(2, riskMitigation.riskID);
		prepStmt.setString(3, riskMitigation.mitigation);
		prepStmt.setDouble(4, riskMitigation.mitigationCost);
		prepStmt.setString(5, riskMitigation.mitigationBenefit);
		prepStmt.setString(6, riskMitigation.personInCharge);
		prepStmt.setDate(7, riskMitigation.planEndDate);
		prepStmt.setDate(8, riskMitigation.actualEndDate);
		prepStmt.setInt(9, riskMitigation.actionStatus);
	}
	/**
	 * Set data for prepared statement, used for Update Contingency
	 * @param prepStmt
	 * @param riskMitigation
	 * @throws SQLException
	 */
	private static void setMitiUpdateFields(PreparedStatement prepStmt,
		RiskMitigationInfo riskMitigation) throws SQLException
	{
		prepStmt.setString(1, riskMitigation.mitigation);
		prepStmt.setDouble(2, riskMitigation.mitigationCost);
		prepStmt.setString(3, riskMitigation.mitigationBenefit);
		prepStmt.setString(4, riskMitigation.personInCharge);
		prepStmt.setDate(5, riskMitigation.planEndDate);
		prepStmt.setDate(6, riskMitigation.actualEndDate);
		prepStmt.setInt(7, riskMitigation.actionStatus);
		prepStmt.setLong(8, riskMitigation.riskMitigationId);
	}
	/**
	 * Set data for prepared statement, used for Delete Contingency
	 * @param prepStmt
	 * @param info
	 * @throws SQLException
	 */
	private static void setMitiDeleteFields(PreparedStatement prepStmt,
		RiskMitigationInfo riskMitigation) throws SQLException
	{
		prepStmt.setLong(1, riskMitigation.riskMitigationId);
		prepStmt.setLong(2, riskMitigation.riskID);
	}
	
	/**
	 * Set data for prepared statement, used for Insert new Contingency
	 * @param prepStmt
	 * @param riskContingencyID
	 * @param info
	 * @throws SQLException
	 */
	private static void setContinInsertFields(PreparedStatement prepStmt,
		long riskContingencyID, RiskContingencyInfo riskContingency) throws SQLException
	{
		prepStmt.setLong(1, riskContingencyID);
		prepStmt.setLong(2, riskContingency.riskID);
		prepStmt.setString(3, riskContingency.contingency);
		prepStmt.setString(4, riskContingency.personInCharge);
		prepStmt.setDate(5, riskContingency.planEndDate);
		prepStmt.setDate(6, riskContingency.actualEndDate);
		prepStmt.setInt(7, riskContingency.actionStatus);
	}
	/**
	 * Set data for prepared statement, used for Update Contingency
	 * @param prepStmt
	 * @param riskContingency
	 * @throws SQLException
	 */
	private static void setContinUpdateFields(PreparedStatement prepStmt,
		RiskContingencyInfo riskContingency) throws SQLException
	{
		prepStmt.setString(1, riskContingency.contingency);
		prepStmt.setString(2, riskContingency.personInCharge);
		prepStmt.setDate(3, riskContingency.planEndDate);
		prepStmt.setDate(4, riskContingency.actualEndDate);
		prepStmt.setInt(5, riskContingency.actionStatus);
		prepStmt.setLong(6, riskContingency.riskContingencyId);
	}
	/**
	 * Set data for prepared statement, used for Delete Contingency
	 * @param prepStmt
	 * @param info
	 * @throws SQLException
	 */
	private static void setContinDeleteFields(PreparedStatement prepStmt,
		RiskContingencyInfo riskContingency) throws SQLException
	{
		prepStmt.setLong(1, riskContingency.riskContingencyId);
		prepStmt.setLong(2, riskContingency.riskID);
	}
	
	public static long getDeveloperID(String account) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = "";
		long devID = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT developer_id FROM DEVELOPER WHERE ACCOUNT ='"
					+ account.toUpperCase()
					+ "'";
			stm = conn.prepareStatement(sql);
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				devID = rs.getLong("DEVELOPER_ID");
				} else {
					return 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ServerHelper.closeConnection(conn, stm, null);
			return devID;
		}
	}
	
	public static Vector saveRiskAddNew(
		Vector vtRisks)
	{
		Vector vtReturnRisks = new Vector();
		boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql =
			"INSERT INTO RISK(RISK_ID, SOURCE_ID, TYPE, CONDITION, CONSEQUENCE, THRESHOLD, PROB, "
			+ "PLANNED_IMPACT, EXPOSURE, MITIGATION, MITIGATION_BENEFIT, ACTUAL_MITIGATION, CONTIGENCY_PLAN, "
			+ "TRIGGER_NAME, DEVELOPER_ACC, ASSESSMENT_DATE, STATUS, "
			+ "ACTUAL_RISK_SCENARIOR, ACTUAL_ACTION, ACTUAL_IMPACT, UNPLANNED, PROJECT_ID, PROCESS_ID, "
			+ "PRIORITY, RISK_PRIORITY, IMPACT, LAST_UPDATED_DATE) "
			+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);  // Multi-records => enable transaction
			prepStmt = conn.prepareStatement(sql);
			for (int i = 0; i < vtRisks.size(); i++) {
				RiskInfo risk = (RiskInfo) vtRisks.get(i);
				
				long riskID = ServerHelper.getNextSeq("RISK_SEQ");
				prepStmt.setLong(1, riskID);
				prepStmt.setLong(2, risk.sourceID);
				prepStmt.setInt(3, risk.type);
				prepStmt.setString(4, risk.condition);
				prepStmt.setString(5, risk.consequence);
				prepStmt.setString(6, risk.threshold);
				prepStmt.setDouble(7, risk.probability);
				prepStmt.setString(8, risk.plannedImpact);
				prepStmt.setDouble(9, risk.exposure);
				prepStmt.setString(10, risk.mitigation);
				prepStmt.setString(11, risk.mitigationBenefit);
				prepStmt.setString(12, risk.mitigationActual);
				prepStmt.setString(13, risk.contingencyPlan);
				prepStmt.setString(14, risk.triggerName);
				prepStmt.setString(15, risk.developerAcc);
				prepStmt.setDate(16, risk.assessmentDate);
				prepStmt.setInt(17, risk.riskStatus);
				prepStmt.setString(18, risk.actualRiskScenario);
				prepStmt.setString(19, risk.actualAction);
				prepStmt.setString(20, risk.actualImpact);
				prepStmt.setInt(21, risk.unplanned);
				prepStmt.setLong(22, risk.projectID);
				prepStmt.setInt(23, risk.processId);
				prepStmt.setInt(24, risk.priority);
				prepStmt.setInt(25, risk.riskPriority);
				prepStmt.setDouble(26, risk.impact);
				prepStmt.setDate(27, risk.lastUpdatedDate);
				
				prepStmt.executeUpdate();
				
				RiskInfo riskReturn = new RiskInfo();
				riskReturn.riskID = riskID;
				riskReturn.sourceID = risk.sourceID;
				
				vtReturnRisks.add(riskReturn);
			}
			prepStmt.close();
			conn.commit();
			conn.setAutoCommit(true);
			result = true;
		}
		catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
			return vtReturnRisks;
		}
	}
	
	public static Vector getMitigationTemp(long sourceId, long riskId){
			Connection conn=null;	
			PreparedStatement prepStmt = null;
			ResultSet rs=null;
			String sql = null;
			Vector riskMitigationTemp = new Vector();
			try{
				conn=ServerHelper.instance().getConnection();
				sql = "SELECT a.risk_source_mitigation_id, a.source_id, a.mitigation_temp "
					+ " FROM risk_source_mitigation a "
					+ " WHERE a.source_id = ?";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setLong(1,sourceId);
				rs = prepStmt.executeQuery();
				while (rs.next()) {
					RiskMitigationInfo riskMitigationInfo= new RiskMitigationInfo();
					riskMitigationInfo.riskID = riskId;
					riskMitigationInfo.mitigation = rs.getString("mitigation_temp");

					riskMitigationTemp.addElement(riskMitigationInfo);
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
			finally{
				ServerHelper.closeConnection(conn,prepStmt,rs);
				return riskMitigationTemp;
			}		
		}
	
	public static Vector getOccuredRiskListForOtherRisk(final long workUnitID, final java.sql.Date beginMonth, final java.sql.Date endMonth, final String strWhere, final String customer, final String strCurrentProject) {
		Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			//note when called from getBaselinedRisks, beginmonth is null
		sql =	"SELECT PROJECT.CODE, PROJECT.GROUP_NAME, PROJECT.CATEGORY, c.*, PROJECT.START_DATE "
				+ " FROM WORKUNIT a,WORKUNIT b, PROJECT, RISK c"
				+ " WHERE a.type(+)= 2 AND c.STATUS = 2 AND"
				+ " PROJECT.PROJECT_ID = a.TABLEID (+) "
				+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
				+ " AND (a.PARENTWORKUNITID=? OR b.PARENTWORKUNITID=?)"
				+ " AND PROJECT.PROJECT_ID (+)= c.PROJECT_ID"
				+ " AND PROJECT.STATUS = 1"
				+ ((customer != null && !customer.equals("All"))? " AND (LOWER(trim(PROJECT.CUSTOMER)) = '" + customer.trim().toLowerCase() + "' or LOWER(trim(PROJECT.CUSTOMER_2ND)) = '" + customer.trim().toLowerCase() + "') " : "")
				+ ((strCurrentProject != null && !strCurrentProject.equals("(All)"))? " AND LOWER(trim(PROJECT.CODE)) = '" + strCurrentProject.trim().toLowerCase() + "'" : "")
				+ ((beginMonth!=null)?" AND PROJECT.ACTUAL_FINISH_DATE >=? AND PROJECT.ACTUAL_FINISH_DATE <=?":"")
				+ strWhere
				+ ((beginMonth!=null)?" ORDER BY PROJECT.CODE":" ORDER BY C.PROCESS_ID");
				
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, workUnitID);
			stm.setLong(2, workUnitID);
			if (beginMonth!=null){
				stm.setDate(3, beginMonth);
				stm.setDate(4, endMonth);
			}
	System.out.println("sql="+sql);
			rs = stm.executeQuery();
		
			while (rs.next()) {
				RiskInfo riskInfo = new RiskInfo();
			
				riskInfo.riskID = rs.getLong("RISK_ID");
				riskInfo.projectID = rs.getInt("PROJECT_ID");
				riskInfo.projectCode = rs.getString("CODE");
				riskInfo.groupName = rs.getString("GROUP_NAME");
				riskInfo.projectType = rs.getInt("CATEGORY");
				riskInfo.start_date = rs.getDate("START_DATE");
	
				riskInfo.actualRiskScenario = rs.getString("ACTUAL_RISK_SCENARIOR");
				riskInfo.actualAction = rs.getString("ACTUAL_ACTION");
				riskInfo.actualImpact = rs.getString("ACTUAL_IMPACT");
				riskInfo.unplanned  = rs.getInt("UNPLANNED");
				riskInfo.exposure = rs.getInt("EXPOSURE");
			
			
				riskInfo.sourceID = rs.getLong("SOURCE_ID");
				riskInfo.type = rs.getInt("TYPE");
				riskInfo.condition = rs.getString("CONDITION");
				riskInfo.consequence = rs.getString("CONSEQUENCE");
				riskInfo.threshold = rs.getString("THRESHOLD");
				riskInfo.probability = rs.getInt("PROB");
				riskInfo.plannedImpact = rs.getString("PLANNED_IMPACT");
				riskInfo.mitigation = rs.getString("MITIGATION");
				riskInfo.mitigationBenefit = rs.getString("MITIGATION_BENEFIT");
				riskInfo.mitigationActual = rs.getString("ACTUAL_MITIGATION");
				riskInfo.contingencyPlan = rs.getString("CONTIGENCY_PLAN");
				riskInfo.triggerName = rs.getString("TRIGGER_NAME");
				riskInfo.developerAcc = rs.getString("DEVELOPER_ACC");
				riskInfo.assessmentDate = rs.getDate("ASSESSMENT_DATE");
				//if process is not defined then process is "other"
				riskInfo.processId  = rs.getInt("PROCESS_ID");
				if (riskInfo.processId ==0)
					riskInfo.processId=ProcessInfo.OTHER;
				riskInfo.baselined=(rs.getInt("BASELINED")==1);
				riskInfo.parseImpact();
				//LamNT3
				riskInfo.contigency = getAllContigencyByRiskId(riskInfo.riskID);
				resultVector.addElement(riskInfo);
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
	
	// HaiMM - Migration start
	public static RiskInfo getRiskByPrjId(long prjID) {
		RiskInfo risk = new RiskInfo();;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs =null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				" SELECT * FROM RISK " +
				" WHERE r.project_id = ? ";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				risk.riskID = rs.getLong("RISK_ID");
				risk.mitigation = rs.getString("MITIGATION");
				risk.contingencyPlan = rs.getString("CONTIGENCY_PLAN");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,rs);
			return risk;
		}
	}
	
	// Migration - Start
	public static boolean addMitigationForMigrate(RiskInfo riskInfo)
		{
			boolean result = false;
			Connection conn = null;
			PreparedStatement stm = null;
			String sqlInsertMiti = "INSERT INTO RISK_MITIGATION(RISK_MITIGATION_ID, RISK_ID, MITIGATION, MITIGATION_BENEFIT, DEVELOPER_ACC) "
									+ " VALUES ( ?, ?, ?, ?, ?) ";
			try {
				conn = ServerHelper.instance().getConnection();
				long id = ServerHelper.getNextSeq("RISK_MITIGATION_SEQ");
				stm = conn.prepareStatement(sqlInsertMiti);
				stm.setLong(1, id);
				stm.setLong(2, riskInfo.riskID);
				stm.setString(3, riskInfo.mitigation);
				stm.setString(4, riskInfo.mitigationBenefit);
				stm.setString(5, riskInfo.developerAcc);
				stm.executeUpdate();
				stm.close();
				conn.commit();
				result = true;
			}
			catch (SQLException e) {
				if (conn != null) {
					conn.rollback();
				}
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn, stm, null);
				return result;
			}
		}
	public static boolean addContingencyForMigrate(RiskInfo riskInfo)
		{
		boolean result = false;
		Connection conn = null;
		PreparedStatement stm = null;
		String sqlInsertMiti = "INSERT INTO RISK_CONTIGENCY(RISK_CONTIGENCY_ID, RISK_ID, CONTIGENCY) "
								+ " VALUES ( ?, ?, ?) ";
		try {
			conn = ServerHelper.instance().getConnection();
			long id = ServerHelper.getNextSeq("RISK_CONTINGENCY_SEQ");
			stm = conn.prepareStatement(sqlInsertMiti);
			stm.setLong(1, id);
			stm.setLong(2, riskInfo.riskID);
			stm.setString(3, riskInfo.contingencyPlan);
			stm.executeUpdate();
			stm.close();
			conn.commit();
			result = true;
		}
		catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return result;
		}
	}

}
