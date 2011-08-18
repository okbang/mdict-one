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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fms1.web.*;
import com.fms1.infoclass.*;
import com.fms1.tools.Db;
import com.fms1.tools.CommonTools;

/**
* Manages project assets.
* @author NgaHT-SEPG
*/

public class Assets {
    
	// PhuNT add Contants of Quality Activities
	// Quality Activities for Review and Test
	private static final byte QA_REVIEW_ACTIVITY_COMPLETENESS = 1;
	private static final byte QA_REVIEW_ACTIVITY_TIMELINESS = 2;
	private static final byte QA_TEST_ACTIVITY_COMPLETENESS = 3;
	private static final byte QA_TEST_ACTIVITY_TIMELINESS = 4;
	// End
    
	public static boolean Update_ApplyPPM(long prjID, int applyPPM, String reason) {
		String sql = "";
		Connection conn = null;
		PreparedStatement stm = null;
		try {
			conn = ServerHelper.instance().getConnection();
			if (applyPPM == 1) {
				sql = "UPDATE PROJECT SET APPLY_PPM = ? WHERE PROJECT_ID = " + prjID;
				stm = conn.prepareStatement(sql);
				stm.setInt(1, applyPPM);
			}
			else {
				sql = "UPDATE PROJECT SET APPLY_PPM = ?, REASON = ? WHERE PROJECT_ID = " + prjID;
				stm = conn.prepareStatement(sql);
				stm.setInt(1, applyPPM);
				stm.setString(2, reason);
			}
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
	 * return recordset of Practices
	 * param ProjectId
	 */
	public static Vector getPracticeList(long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		Vector vt = new Vector();
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM PRACTICES WHERE PROJECT_ID = " + prjID + " ORDER BY PRACTICE_ID";
			rs = stm.executeQuery(sql);
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				PracticeInfo pract = new PracticeInfo();
				pract.practiceId = rs.getInt("PRACTICE_ID");
				pract.scenario = rs.getString("SCENARIO");
				pract.practice = rs.getString("PRACTICE");
				pract.type = rs.getInt("TYPE");
				pract.category = rs.getString("CATEGORY");
				pract.projectId = rs.getInt("PROJECT_ID");
				vt.add(pract);
			}
			return vt;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
		}
	}

	public static Vector getPracticeList(final long workUnitID, final Date beginMonth, final Date endMonth, final String strWhere) {
		Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			sql = " SELECT c.*, PROJECT.CODE, PROJECT.START_DATE,project.leader, project.group_name"
				+ " FROM WORKUNIT a,WORKUNIT b, PROJECT, PRACTICES c"
				+ " WHERE a.type(+)= 2 AND"
				+ " PROJECT.PROJECT_ID = a.TABLEID (+) "
				+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
				+ " AND (a.PARENTWORKUNITID=? OR b.PARENTWORKUNITID=?)"
				+ " AND PROJECT.PROJECT_ID (+)= c.PROJECT_ID"
				+ " AND PROJECT.ACTUAL_FINISH_DATE >=?"
				+ " AND PROJECT.ACTUAL_FINISH_DATE <=?"
				+ strWhere
				+ " ORDER BY PROJECT.CODE";

			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, workUnitID);
			stm.setLong(2, workUnitID);
			stm.setDate(3, beginMonth);
			stm.setDate(4, endMonth);
            
			rs = stm.executeQuery();
			
			while (rs.next()) {
				PracticeInfo pract = new PracticeInfo();
				pract.practiceId = rs.getInt("PRACTICE_ID");
				pract.scenario = rs.getString("SCENARIO");
				pract.practice = rs.getString("PRACTICE");
				pract.type = rs.getInt("TYPE");
				pract.category = rs.getString("CATEGORY");
				pract.projectId = rs.getInt("PROJECT_ID");
				pract.projectCode = rs.getString("CODE");
				pract.start_date = rs.getDate("START_DATE");
				pract.projectManager = rs.getString("LEADER");
				pract.groupName = rs.getString("GROUP_NAME");
				resultVector.addElement(pract);
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
	 *  Add new Practice to PRACTICES
	 * @param:PracticeInfo
	 * return boolean
	 */
	public static boolean addPractice(PracticeInfo practInfo) {
		String insertStatement = "";
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			long practicesId = ServerHelper.getNextSeq("PRACTICES_SEQ");
			conn = ServerHelper.instance().getConnection();
			insertStatement =
				" INSERT INTO PRACTICES (PRACTICE_ID,PROJECT_ID,SCENARIO,TYPE,CATEGORY,PRACTICE)"
					+ " Values (?,?,?,?,?,?)";
			prepStmt = conn.prepareStatement(insertStatement);
			prepStmt.setLong(1, practicesId);
			prepStmt.setLong(2, practInfo.projectId);
			prepStmt.setString(3, practInfo.scenario);
			prepStmt.setInt(4, practInfo.type);
			prepStmt.setString(5, practInfo.category);
			prepStmt.setString(6, practInfo.practice);
			return (prepStmt.executeUpdate() > 0);
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
	 * uppdate PRACTICES
	 * @param:Practices
	 * return boolean
	 */
	public static boolean updatePractice(PracticeInfo practInfo) {
		String updateStatement = "";
		Connection conn = null;
		PreparedStatement prepStmt = null;
		try {
			conn = ServerHelper.instance().getConnection();
			updateStatement =
				" UPDATE PRACTICES "
					+ " SET PROJECT_ID = ?,"
					+ " SCENARIO = ? ,"
					+ " TYPE = ?,"
					+ " CATEGORY = ?,"
					+ " PRACTICE = ?"
					+ " WHERE PRACTICE_ID = ?";
			prepStmt = conn.prepareStatement(updateStatement);
			prepStmt.setLong(1, practInfo.projectId);
			prepStmt.setString(2, practInfo.scenario);
			prepStmt.setInt(3, practInfo.type);
			prepStmt.setString(4, practInfo.category);
			prepStmt.setString(5, practInfo.practice);
			prepStmt.setLong(6, practInfo.practiceId);
			int rowCount = prepStmt.executeUpdate();
			if (rowCount == 0) {
				return false;
			}
			prepStmt.executeUpdate();
			prepStmt.close();
			conn.close();
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}

	/**
	 * Delete one Practice from PRACTICES
	 * @param:PracticeID
	 * Return boolean
	 */
	public static boolean deletePractice(long PracticeId) {
		return Db.delete(PracticeId, "PRACTICE_ID", "PRACTICES");
	}
	public static Vector getTDCategory() {
		Vector result = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			result.addElement("General");
			result.addElement("FSOFT SLC");
			sql = "select NAME from PROCESS ORDER BY NAME ";
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					result.addElement(rs.getString("NAME"));
				}
			}
			rs = null;
			sql = "select NAME from WORKPRODUCT ORDER BY NAME ";
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					result.addElement(rs.getString("NAME"));
				}
			}
			return result;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
    
	public static DeviationInfo getDeviation(long deviationID) {
		DeviationInfo deviationInfo = new DeviationInfo();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "select * from PROJECT_DEVIATION where DEVIATION_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, deviationID);
			rs = stm.executeQuery();
			if (rs == null || !rs.next()) {
				return null;
			}
			deviationInfo.deviationID = rs.getLong("DEVIATION_ID");
			deviationInfo.projectID = rs.getInt("PROJECT_ID");
			deviationInfo.modification = rs.getString("MODIFICATION");
			deviationInfo.action = rs.getInt("ACTION");
			deviationInfo.reason = rs.getString("REASON");
			deviationInfo.category = rs.getString("CATEGORY");
			deviationInfo.note = rs.getString("NOTE");
			return deviationInfo;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}

	public static TailoringInfo getTailoring(long prjID, long pro_tail_id) {
		TailoringInfo tailoringInfo = new TailoringInfo();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = 
				"SELECT TAIL.PROJECT_ID, TAIL.ACTION, TAIL.NOTE, TAIL.PROCESS_TAIL_ID, PROTAIL.*, PRO.NAME FROM PROJECT_TAILORING TAIL, TAILORING PROTAIL, PROCESS PRO"
					+ " WHERE TAIL.PROCESS_TAIL_ID = PROTAIL.TAIL_ID AND PROTAIL.PROCESS_ID = PRO.PROCESS_ID (+) AND TAIL.PROJECT_ID = ? AND TAIL.PROCESS_TAIL_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			stm.setLong(2, pro_tail_id);
			rs = stm.executeQuery();
			if (rs == null || !rs.next()) {
				return null;
			}
			tailoringInfo.projectID = (int)prjID;
			tailoringInfo.process_tailID=(int)pro_tail_id;
			tailoringInfo.modification = rs.getString("TAIL_PER");
			tailoringInfo.action = rs.getInt("ACTION");
			tailoringInfo.reason = rs.getString("APP_CRI");
			switch (rs.getInt("PROCESS_ID")) {
				case ProcessInfo.FSOFT_SLC:
					tailoringInfo.category = "FSOFT SLC";
					break;
				case ProcessInfo.GENERAL:
					tailoringInfo.category = "General";
					break;
				default:
					tailoringInfo.category = rs.getString("NAME");
				}
				tailoringInfo.note = rs.getString("NOTE");
			return tailoringInfo;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}		
	
	public static Vector getDeviationList(final long prjID, final String strWhere) {
		Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM PROJECT_DEVIATION WHERE PROJECT_ID = ?";
			sql = sql + strWhere + " ORDER BY CATEGORY ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				TailoringDeviationInfo tdInfo = new TailoringDeviationInfo();
				tdInfo.tailoringID = rs.getLong("DEVIATION_ID");
				tdInfo.projectID = rs.getInt("PROJECT_ID");
				tdInfo.modification = rs.getString("MODIFICATION");
				tdInfo.action = rs.getInt("ACTION");
				tdInfo.reason = rs.getString("REASON");
				tdInfo.type = 2;
				tdInfo.category = rs.getString("CATEGORY");
				tdInfo.note = rs.getString("NOTE");
				resultVector.addElement(tdInfo);
			}
			stm.close();
			rs.close();

			sql = 
				"SELECT TAIL.PROJECT_ID, TAIL.ACTION, TAIL.NOTE, TAIL.PROCESS_TAIL_ID, PROTAIL.*, PRO.NAME FROM PROJECT_TAILORING TAIL, TAILORING PROTAIL, PROCESS PRO"
					+ " WHERE TAIL.PROCESS_TAIL_ID = PROTAIL.TAIL_ID AND PROTAIL.PROCESS_ID = PRO.PROCESS_ID (+) AND TAIL.PROJECT_ID = ?";
			sql = sql + strWhere + " ORDER BY PRO.NAME ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				TailoringDeviationInfo tdInfo = new TailoringDeviationInfo();
				tdInfo.projectID = rs.getInt("PROJECT_ID");
				tdInfo.process_tail_ID=rs.getInt("PROCESS_TAIL_ID");
				tdInfo.modification = rs.getString("TAIL_PER");
				tdInfo.action = rs.getInt("ACTION");
				tdInfo.reason = rs.getString("APP_CRI");
				tdInfo.type = 1;
				switch (rs.getInt("PROCESS_ID")) {
					case ProcessInfo.FSOFT_SLC:
						tdInfo.category = "FSOFT SLC";
						break;
					case ProcessInfo.GENERAL:
						tdInfo.category = "General";
						break;
					default:
						tdInfo.category = rs.getString("NAME");                       
				}
				tdInfo.note = rs.getString("NOTE");
				resultVector.addElement(tdInfo);
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
    
	// Add by HaiMM: Tailoring/Deviation reference list
	public static Vector getTailDevRefList(final long prjID, final String strWhereDev, final String strWhere) {
		Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
	
		try {
			conn = ServerHelper.instance().getConnection();
			/*
			sql = "SELECT * FROM PROJECT_DEVIATION WHERE PROJECT_ID = ?";
			sql = sql + strWhere + " ORDER BY CATEGORY ";
			*/
			sql =	"SELECT PROJECT.CODE, PROJECT.GROUP_NAME, c.* "
				+ " FROM WORKUNIT a,WORKUNIT b, PROJECT, project_deviation c"
				+ " WHERE a.type(+)= 2 AND"
				+ " PROJECT.PROJECT_ID = a.TABLEID (+) "
				+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
				+ " AND (a.PARENTWORKUNITID=? OR b.PARENTWORKUNITID=?)"
				+ " AND PROJECT.PROJECT_ID (+)= c.PROJECT_ID"
				+ strWhereDev
				+ " ORDER BY PROJECT.CODE";
			
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			stm.setLong(2, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				TailoringDeviationInfo tdInfo = new TailoringDeviationInfo();
				tdInfo.tailoringID = rs.getLong("DEVIATION_ID");
				tdInfo.projectID = rs.getInt("PROJECT_ID");
				tdInfo.modification = rs.getString("MODIFICATION");
				tdInfo.action = rs.getInt("ACTION");
				tdInfo.reason = rs.getString("REASON");
				tdInfo.type = 2;
				tdInfo.category = rs.getString("CATEGORY");
				tdInfo.note = rs.getString("NOTE");
				tdInfo.projectCode = rs.getString("CODE");
				tdInfo.groupName = rs.getString("GROUP_NAME");
				
				resultVector.addElement(tdInfo);
			}
			stm.close();
			rs.close();

			
			sql =	"SELECT PROJECT.CODE, PROJECT.GROUP_NAME, TAIL.PROJECT_ID, TAIL.ACTION, TAIL.NOTE, TAIL.PROCESS_TAIL_ID, PROTAIL.*, PRO.NAME "
				+ " FROM WORKUNIT a,WORKUNIT b, PROJECT, PROJECT_TAILORING TAIL, TAILORING PROTAIL, PROCESS PRO"
				+ " WHERE a.type(+)= 2 AND"
				+ " PROJECT.PROJECT_ID = a.TABLEID (+) "
				+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
				+ " AND (a.PARENTWORKUNITID=? OR b.PARENTWORKUNITID=?)"
				+ " AND PROJECT.PROJECT_ID (+)= TAIL.PROJECT_ID"
				+ " AND TAIL.PROCESS_TAIL_ID = PROTAIL.TAIL_ID AND PROTAIL.PROCESS_ID = PRO.PROCESS_ID (+)"
				+ strWhere
				+ " ORDER BY PROJECT.CODE";
			
			
			/*
			sql = 
				"SELECT TAIL.PROJECT_ID, TAIL.ACTION, TAIL.NOTE, TAIL.PROCESS_TAIL_ID, PROTAIL.*, PRO.NAME FROM PROJECT_TAILORING TAIL, TAILORING PROTAIL, PROCESS PRO"
					+ " WHERE TAIL.PROCESS_TAIL_ID = PROTAIL.TAIL_ID AND PROTAIL.PROCESS_ID = PRO.PROCESS_ID (+) AND TAIL.PROJECT_ID = ?";
			sql = sql + strWhere + " ORDER BY PRO.NAME ";
			*/
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			stm.setLong(2, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				TailoringDeviationInfo tdInfo = new TailoringDeviationInfo();
				tdInfo.projectID = rs.getInt("PROJECT_ID");
				tdInfo.process_tail_ID=rs.getInt("PROCESS_TAIL_ID");
				tdInfo.modification = rs.getString("TAIL_PER");
				tdInfo.action = rs.getInt("ACTION");
				tdInfo.reason = rs.getString("APP_CRI");
				tdInfo.type = 1;
				switch (rs.getInt("PROCESS_ID")) {
					case ProcessInfo.FSOFT_SLC:
						tdInfo.category = "FSOFT SLC";
						break;
					case ProcessInfo.GENERAL:
						tdInfo.category = "General";
						break;
					default:
						tdInfo.category = rs.getString("NAME");                       
				}
				tdInfo.note = rs.getString("NOTE");
				tdInfo.projectCode = rs.getString("CODE");
				tdInfo.groupName = rs.getString("GROUP_NAME");
				
				resultVector.addElement(tdInfo);
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
	
	public static Vector getTailoringDeviationList(final long workUnitID, final Date beginMonth, final Date endMonth, final String strWhereTail, final String strWhereDeviation) {
		Vector resultVector = new Vector();
		resultVector = getTailoringList(workUnitID, beginMonth, endMonth, strWhereTail);
		resultVector.addAll(getDeviationList(workUnitID, beginMonth, endMonth, strWhereDeviation));
		return resultVector;
	}
    
	public static Vector getDeviationList(final long workUnitID, final Date beginMonth, final Date endMonth, final String strWhere) {
		Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		String sqlTail = null, sqlDeviation = null;
		sql = " SELECT c.DEVIATION_ID as TAILORING_ID ,c.*, PROJECT.CODE, PROJECT.START_DATE, PROJECT.TYPE"
			+ " FROM WORKUNIT a, WORKUNIT b, PROJECT , PROJECT_DEVIATION c "
			+ " WHERE a.type(+)= 2 AND"
			+ " PROJECT.PROJECT_ID = a.TABLEID (+)"
			+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
			+ " AND (a.PARENTWORKUNITID=? OR b.PARENTWORKUNITID=?)"
			+ " AND PROJECT.PROJECT_ID (+)= c.PROJECT_ID"
			+ " AND PROJECT.ACTUAL_FINISH_DATE >=?"
			+ " AND PROJECT.ACTUAL_FINISH_DATE <=?"
			+ " AND PROJECT.STATUS = '1'"
			+ strWhere 
			+ " ORDER BY PROJECT.CODE, TAILORING_ID";
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, workUnitID);
			stm.setLong(2, workUnitID);
			stm.setDate(3, beginMonth);
			stm.setDate(4, endMonth);
			rs = stm.executeQuery();
			while (rs.next()) {
				TailoringDeviationInfo tdInfo = new TailoringDeviationInfo();
				tdInfo.tailoringID = rs.getLong("TAILORING_ID");
				tdInfo.projectID = rs.getInt("PROJECT_ID");
				tdInfo.projectCode = rs.getString("CODE");
				tdInfo.start_date = rs.getDate("START_DATE");
				tdInfo.modification = rs.getString("MODIFICATION");
				tdInfo.action = rs.getInt("ACTION");
				tdInfo.reason = rs.getString("REASON");
				tdInfo.category = rs.getString("CATEGORY");
				tdInfo.note = rs.getString("NOTE");
				tdInfo.type = 2; 
				resultVector.addElement(tdInfo);
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
        
	public static Vector getTailoringList(final long workUnitID, final Date beginMonth, final Date endMonth, final String strWhere) {
		Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		String sqlTail = null, sqlDeviation = null;
		sql = " SELECT c.PROCESS_ID, c.TAIL_ID as TAILORING_ID,PROJECT.PROJECT_ID, c.TAIL_PER as MODIFICATION, e.NAME as CATEGORY, c.APP_CRI as REASON, d.NOTE, d.ACTION, PROJECT.CODE, PROJECT.START_DATE, PROJECT.TYPE"
			+ " FROM WORKUNIT a, WORKUNIT b, PROJECT , TAILORING c, PROJECT_TAILORING d, PROCESS e"
			+ " WHERE a.type(+)= 2 AND"
			+ " PROJECT.PROJECT_ID = a.TABLEID (+)"
			+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
			+ " AND (a.PARENTWORKUNITID=? OR b.PARENTWORKUNITID=?)"
			+ " AND PROJECT.PROJECT_ID (+)= d.PROJECT_ID"
			+ " AND c.tail_id = d.process_tail_id (+) "
			+ " AND c.process_id  = e.process_id (+) "
			+ " AND PROJECT.ACTUAL_FINISH_DATE >=?"
			+ " AND PROJECT.ACTUAL_FINISH_DATE <=?"
			+ " AND PROJECT.STATUS = '1'" 
			+ strWhere 
			+ " ORDER BY PROJECT.CODE, TAILORING_ID ";
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, workUnitID);
			stm.setLong(2, workUnitID);
			stm.setDate(3, beginMonth);
			stm.setDate(4, endMonth);
			rs = stm.executeQuery();
			while (rs.next()) {
				TailoringDeviationInfo tdInfo = new TailoringDeviationInfo();
				tdInfo.tailoringID = rs.getLong("TAILORING_ID");
				tdInfo.projectID = rs.getInt("PROJECT_ID");
				tdInfo.projectCode = rs.getString("CODE");
				tdInfo.start_date = rs.getDate("START_DATE");
				tdInfo.modification = rs.getString("MODIFICATION");
				tdInfo.action = rs.getInt("ACTION");
				tdInfo.reason = rs.getString("REASON");
				switch (rs.getInt("PROCESS_ID")) {
					case ProcessInfo.FSOFT_SLC:
						tdInfo.category = "FSOFT SLC";
						break;
					case ProcessInfo.GENERAL:
						tdInfo.category = "General";
						break;    
					default:
						tdInfo.category = rs.getString("CATEGORY");    
				 }
				tdInfo.note = rs.getString("NOTE");
				tdInfo.type = 1;
				resultVector.addElement(tdInfo);
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
        
	public static boolean addTailoring(TailoringInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"INSERT INTO PROJECT_TAILORING (PROJECT_ID,PROCESS_TAIL_ID,ACTION,NOTE ) VALUES (?, ?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setInt(1, newInfo.projectID);
			stm.setInt(2, newInfo.process_tailID);
			stm.setInt(3, newInfo.action);
			stm.setString(4, newInfo.note);
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
	
	public static boolean Tailoringexisted(int prjID, long protailID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM PROJECT_TAILORING WHERE PROJECT_ID = ? AND PROCESS_TAIL_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setInt(1, prjID); 
			stm.setLong(2, protailID);
			rs = stm.executeQuery();
			return (rs.next());
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
    
	public static boolean updateTailoring(TailoringInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM PROJECT_TAILORING WHERE PROJECT_ID = ? AND PROCESS_TAIL_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, newInfo.projectID);
			stm.setLong(2, newInfo.process_tailID);
			rs = stm.executeQuery();
			if (rs == null || !rs.next()) {
				return false;
			}

			sql =
				"UPDATE PROJECT_TAILORING SET ACTION = ?,"
					+ " NOTE = ?"
					+ " WHERE PROJECT_ID = ? AND PROCESS_TAIL_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setInt(1, newInfo.action);
			stm.setString(2, newInfo.note);
			stm.setLong(3, newInfo.projectID);
			stm.setLong(4, newInfo.process_tailID);
			return (stm.executeUpdate() > 0);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	
	public static boolean deleteTailoring(long prjID, long protailID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "DELETE FROM PROJECT_TAILORING WHERE PROJECT_ID = ? AND PROCESS_TAIL_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			stm.setLong(2, protailID);
			return(stm.executeUpdate() > 0);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}		
	}
	
	public static boolean addDeviation(DeviationInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			long newKey = ServerHelper.getNextSeq("PROJECT_DEVIATION_SEQ");
			sql =
				"INSERT INTO PROJECT_DEVIATION (DEVIATION_ID, PROJECT_ID, MODIFICATION, ACTION,REASON, CATEGORY, NOTE ) VALUES (?, ?, ?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, newKey);
			stm.setInt(2, newInfo.projectID);
			stm.setString(3, newInfo.modification);
			stm.setInt(4, newInfo.action);
			stm.setString(5, newInfo.reason);
			stm.setString(6, newInfo.category);
			stm.setString(7, newInfo.note);
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
    
	public static boolean updateDeviation(DeviationInfo newInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT * FROM PROJECT_DEVIATION WHERE DEVIATION_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, newInfo.deviationID);
			rs = stm.executeQuery();
			if (rs == null || !rs.next()) {
				return false;
			}
			sql =
				"UPDATE PROJECT_DEVIATION SET MODIFICATION = ?"
					+ ",ACTION       	= ?"
					+ ",REASON	   		= ?"
					+ ",CATEGORY	   	= ?"
					+ ",NOTE		   	= ?"
					+ " WHERE DEVIATION_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, newInfo.modification);
			stm.setInt(2, newInfo.action);
			stm.setString(3, newInfo.reason);
			stm.setString(4, newInfo.category);
			stm.setString(5, newInfo.note);
			stm.setLong(6, newInfo.deviationID);
			return (stm.executeUpdate() > 0);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}		
	}
    
	public static boolean deleteDeviation(long deviationID) {
		return Db.delete(deviationID, "DEVIATION_ID", "PROJECT_DEVIATION");
	}
    
	public static Vector getProcessSchedule(StageInfo stageInfo) {
		Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String sql = null;
		try {
			sql =
				"SELECT NAME, PLANNED_RELEASE_DATE, REPLANNED_RELEASE_DATE, ACTUAL_RELEASE_DATE, " 
					+ " NVL(REPLANNED_RELEASE_DATE,PLANNED_RELEASE_DATE) PLANNED_DATE FROM MODULE "
					+ " WHERE PROJECT_ID = ? "
					+ " and NVL(REPLANNED_RELEASE_DATE,PLANNED_RELEASE_DATE) <= ? "
					+ " and NVL(REPLANNED_RELEASE_DATE,PLANNED_RELEASE_DATE) >= ? ";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, stageInfo.prjID);
			stm.setDate(2, stageInfo.plannedEndDate);
			stm.setDate(3, stageInfo.plannedBeginDate);
			rs = stm.executeQuery();
			while (rs.next()) {
				ProcessScheduleInfo processScheduleInfo = new ProcessScheduleInfo();
				processScheduleInfo.setProduct(rs.getString("NAME"));
				Date plan = rs.getDate("PLANNED_DATE");
				processScheduleInfo.setPlannedReleaseDate(plan);
				Date actual = rs.getDate("ACTUAL_RELEASE_DATE");
				processScheduleInfo.setActualReleaseDate(actual);

				if ((plan != null) && (actual != null) && (stageInfo.plannedBeginDate != null)) {
					double deviation =  
										(actual.getTime() - plan.getTime()) * 100.00 
										/ (plan.getTime() - stageInfo.plannedBeginDate.getTime());
					processScheduleInfo.setDeviation(deviation);
				}
				else {
					processScheduleInfo.setDeviation(Double.NaN);
				}
				resultVector.addElement(processScheduleInfo);
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

	public static Vector getProcessQA(StageInfo stageInfo, WeeklyReportInfo reportInfo) {        
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String sql = null;
		Vector processQAList = new Vector();
		try {
			QualityActivitiesInfo reviewActivity = new QualityActivitiesInfo();
			QualityActivitiesInfo testActivity = new QualityActivitiesInfo();
			reviewActivity.setActivity("Review");
			reviewActivity.setWeightedDefectsPlannedToBeFound(reportInfo.defectPlanReview);                        
			testActivity.setActivity("Test");
			testActivity.setWeightedDefectsPlannedToBeFound(reportInfo.defectPlanTest);            
			reviewActivity.setWeightedDefectsDetected(reportInfo.weightedDefectReview);
			testActivity.setWeightedDefectsDetected(reportInfo.weightedDefectTest);
            
			conn = ServerHelper.instance().getConnection();
			// Set Planned Review Effort
			sql =
				" SELECT SUM(NVL(RE_PLAN_EFFORT,PLAN_EFFORT)) REVIEW_EFFORT "
					+ " FROM REVIEW_EFFORT "
					+ " WHERE PROJECT_ID = ? ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, stageInfo.prjID);
			rs = stm.executeQuery();            
			double plannedReviewEffort = 0;
			if (rs.next()) {
				plannedReviewEffort = rs.getDouble("REVIEW_EFFORT");
				reviewActivity.setPlannedEffort(plannedReviewEffort);
			}
			else {
				reviewActivity.setPlannedEffort(Double.NaN);
			}
			rs.close();
			stm.close();
                        
			// Set Planned Test Effort
			sql =
				" SELECT SUM(NVL(RE_PLAN_EFFORT, PLAN_EFFORT)) TEST_EFFORT "
					+ " FROM PROCESS_EFFORT "
					+ " WHERE PROJECT_ID = ? "
					+ " AND PROCESS_ID = 7 ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, stageInfo.prjID);
			rs = stm.executeQuery();            
			double plannedTestEffort = 0;
			if (rs.next()) {
				plannedTestEffort = rs.getDouble("TEST_EFFORT");
				testActivity.setPlannedEffort(plannedTestEffort);
			}
			else {
				testActivity.setPlannedEffort(Double.NaN);
			}
			rs.close();
			stm.close();

			double reviewEffort = Effort.getEffortByTOW(stageInfo.prjID,EffortInfo.TOW_REVIEW, stageInfo.aEndD);
			reviewActivity.setActualEffort(reviewEffort);
			double testEffort = Effort.getEffortByTOW(stageInfo.prjID, EffortInfo.TOW_TEST, stageInfo.aEndD);
			testActivity.setActualEffort(testEffort);
            
			double[] arrActivityInfo;
            
			// Set the Completeness property of reviewActivity            
			arrActivityInfo = getArrayActivityInfo(conn, stageInfo, QA_REVIEW_ACTIVITY_COMPLETENESS);
			if (!Double.isNaN(arrActivityInfo[0])) {
				reviewActivity.setCompleteness((arrActivityInfo[1] == 0) ? Double.NaN : (arrActivityInfo[0] * 100 / arrActivityInfo[1]));
			}
			else {
				reviewActivity.setCompleteness(Double.NaN);
			}
            
			// Set the Timeliness property of reviewActivity            
			arrActivityInfo = getArrayActivityInfo(conn, stageInfo, QA_REVIEW_ACTIVITY_TIMELINESS);
			if (!Double.isNaN(arrActivityInfo[0])) {
				reviewActivity.setTimeliness((arrActivityInfo[1] == 0) ? Double.NaN : (arrActivityInfo[0] * 100 / arrActivityInfo[1]));
			}
			else {
				reviewActivity.setTimeliness(Double.NaN);
			}
            
			// Set the Completeness property of testActivity            
			arrActivityInfo = getArrayActivityInfo(conn, stageInfo, QA_TEST_ACTIVITY_COMPLETENESS);
			if (!Double.isNaN(arrActivityInfo[0])) {
				testActivity.setCompleteness((arrActivityInfo[1] == 0) ? Double.NaN : (arrActivityInfo[0] * 100 / arrActivityInfo[1]));
			}
			else {
				testActivity.setCompleteness(Double.NaN);
			}
            
			// Set the Timeliness property of testActivity            
			arrActivityInfo = getArrayActivityInfo(conn, stageInfo, QA_TEST_ACTIVITY_TIMELINESS);
			if (!Double.isNaN(arrActivityInfo[0])) {
				testActivity.setTimeliness((arrActivityInfo[1] == 0) ? Double.NaN : (arrActivityInfo[0] * 100 / arrActivityInfo[1]));
			}
			else {
				testActivity.setTimeliness(Double.NaN);
			}
            
			processQAList.add(reviewActivity);
			processQAList.add(testActivity);
            
			sql =
				" SELECT A.ACTIVITY F1, DECODE(B.RE_PLAN_EFFORT,-1, B.PLAN_EFFORT,B.RE_PLAN_EFFORT) F2, B.ACTUAL_EFFORT  F3, A.ACTUAL_END_DATE F4, A.PLANNED_END_DATE F5 "
					+ " FROM OTHER_ACTIVITY A, QUALITY_ACTIVITY_EFFORT B "
					+ " WHERE A.OTHER_ACTIVITY_ID = B.ACTIVITY_ID "
					+ " AND B.TYPE = 1 "
					+ " AND A.PLANNED_END_DATE <= ? "
					+ " AND A.PROJECT_PLAN_ID = ? ";
			stm = conn.prepareStatement(sql);
			stm.setDate(1, stageInfo.plannedEndDate);
			stm.setLong(2, stageInfo.prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				QualityActivitiesInfo qualityActivitiesInfo = new QualityActivitiesInfo();
				qualityActivitiesInfo.setActivity(rs.getString("F1"));
				qualityActivitiesInfo.setWeightedDefectsPlannedToBeFound(Double.NaN);
				qualityActivitiesInfo.setWeightedDefectsDetected(Double.NaN);
				qualityActivitiesInfo.setPlannedEffort((rs.getDouble("F2") == -1) ? Double.NaN : rs.getDouble("F2"));
				qualityActivitiesInfo.setActualEffort((rs.getDouble("F3") == -1) ? Double.NaN : rs.getDouble("F3"));
                
				// Set Completeness property
				Date actualEndDate = rs.getDate("F4");
				if (actualEndDate == null) {
					qualityActivitiesInfo.setCompleteness(0);
				}
				else {
					if (actualEndDate.compareTo(stageInfo.aEndD) <= 0) {
						qualityActivitiesInfo.setCompleteness(100);
					}
					else {
						qualityActivitiesInfo.setCompleteness(0);
					}
				}
                
				// Set Timeliness property
				Date planEndDate = rs.getDate("F5");
				if (planEndDate == null) {
					qualityActivitiesInfo.setTimeliness(Double.NaN);
				}
				else {
					if (actualEndDate == null) {
						qualityActivitiesInfo.setTimeliness(0);
					}
					else {
						if (actualEndDate.compareTo(planEndDate) <= 0) {
							qualityActivitiesInfo.setTimeliness(100);
						}
						else {
							qualityActivitiesInfo.setTimeliness(0);
						}
					}
				}
				processQAList.add(qualityActivitiesInfo);
			}
			return processQAList;
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
	 * @purpose : move the choosing SQL Statements into the separate function
	 * @author : PhuNT
	 * @return : the value of Review Activity and TestActivity
	 * @param : StageInfo to get ActualEndDate, PlannedEndDate, ProjectId
	 * @param : bType to select SQL Statement appropriate for the property of activity
	 */
	private static double[] getArrayActivityInfo (Connection conn, StageInfo stageInfo, byte bType) {
		PreparedStatement stm = null;
		ResultSet rs = null;
		String sql = null;
        
		double[] arrActivityInfo = new double[2];
		double a = Double.NaN;
		double b = Double.NaN;
        
		try {
			switch (bType) {
				case QA_REVIEW_ACTIVITY_COMPLETENESS:
					// Set the Completeness property of reviewActivity
					sql =
						" SELECT A, B FROM "
							+ " (SELECT COUNT(*) A FROM MODULE WHERE ACTUAL_REVIEW_DATE <= ? AND PROJECT_ID = ?),"
							+ " (SELECT COUNT(*) B FROM MODULE WHERE PLANNED_REVIEW_DATE <= ? AND PROJECT_ID = ?)";
					break;                         
				case QA_REVIEW_ACTIVITY_TIMELINESS:
					// Set the Timeliness property of reviewActivity
					sql =
						" SELECT A, B FROM "
							+ " (SELECT COUNT(*) A FROM MODULE WHERE (ACTUAL_REVIEW_DATE <= ?) AND (ACTUAL_REVIEW_DATE <= PLANNED_REVIEW_DATE) AND PROJECT_ID = ?),"
							+ " (SELECT COUNT(*) B FROM MODULE WHERE PLANNED_REVIEW_DATE <= ? AND PROJECT_ID = ?)";
					break;
				case QA_TEST_ACTIVITY_COMPLETENESS:
					// Set the Completeness property of testActivity
					sql =
						" SELECT A, B FROM "
							+ " (SELECT COUNT(*) A FROM MODULE WHERE ACTUAL_TEST_END_DATE <= ? AND PROJECT_ID = ? ),"
							+ " (SELECT COUNT(*) B FROM MODULE WHERE PLANNED_TEST_END_DATE <= ? AND PROJECT_ID = ?)";
					break;
				case QA_TEST_ACTIVITY_TIMELINESS:
					// Set the Timeliness property of testActivity
					sql =
						" SELECT A, B FROM "
							+ " (SELECT COUNT(*) A FROM MODULE WHERE (ACTUAL_TEST_END_DATE <= ?) AND (ACTUAL_TEST_END_DATE <= PLANNED_TEST_END_DATE) AND (PROJECT_ID = ?) ),"
							+ " (SELECT COUNT(*) B FROM MODULE WHERE PLANNED_TEST_END_DATE <= ? AND (PROJECT_ID = ?))";
					break;
			}
			stm = conn.prepareStatement(sql);
			stm.setDate(1, stageInfo.aEndD);
			stm.setLong(2, stageInfo.prjID);
			stm.setDate(3, stageInfo.plannedEndDate);
			stm.setLong(4, stageInfo.prjID);
			rs = stm.executeQuery();
			if (rs.next()) {
				a = rs.getDouble("A");
				b = rs.getDouble("B");
			}
			rs.close();
			stm.close();
			arrActivityInfo[0] = a;
			arrActivityInfo[1] = b;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(null, stm, rs);
			return arrActivityInfo;
		}
	}
}