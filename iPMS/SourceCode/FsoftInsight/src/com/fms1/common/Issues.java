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
import com.fms1.infoclass.*;
import com.fms1.web.*;
import com.fms1.tools.*;
final public class Issues {
	public static IssueInfo getIssueByRiskId(final long riskId) {
		Connection conn = null;
		String sql = null;
		Statement stmt = null;
		ResultSet rs = null;
		final IssueInfo issueInfo = new IssueInfo();
		try {
			conn = ServerHelper.instance().getConnection();
			stmt = conn.createStatement();
			sql = "SELECT * FROM ISSUE, DEVELOPER WHERE OWNER = ACCOUNT AND RISKID = " + riskId ;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				issueInfo.issueID = rs.getInt("ISSUEID");
				issueInfo.workUnitID = rs.getLong("WORKUNITID");
				issueInfo.description = rs.getString("DESCRIPTION");
				issueInfo.statusID = rs.getInt("STATUSID");
				issueInfo.priorityID = rs.getInt("PRIORITYID");
				issueInfo.typeID = rs.getInt("TYPEID");
				issueInfo.owner = rs.getString("OWNER");
				issueInfo.startDate = rs.getDate("STARTDATE");
				issueInfo.dueDate = rs.getDate("DUEDATE");
				issueInfo.closeDate = rs.getDate("CLOSEDDATE");
				issueInfo.comment = rs.getString("COMMENTS");
				issueInfo.reference = rs.getString("REFERENCE");
				issueInfo.creator = rs.getString("CREATOR");
				issueInfo.ownerName = rs.getString("NAME");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, rs);
			return issueInfo;
		}
		
	}
	public static boolean updateIssue(final IssueInfo newIssueInfo) {
		Connection conn = null;
		String sql = null;
		PreparedStatement stmt = null;
		try {
			sql =
				"UPDATE ISSUE SET"
					+ "  WORKUNITID = ?"
					+ ", DESCRIPTION = ?"
					+ ", STATUSID = ?"
					+ ", PRIORITYID = ?"
					+ ", TYPEID = ?"
					+ ", OWNER = ?"
					+ ", STARTDATE = ?"
					+ ", DUEDATE = ?"
					+ ", CLOSEDDATE = ?"
					+ ", COMMENTS = ?"
					+ ", REFERENCE = ?"
					+ ", CREATOR = ?"
					+ ", PROCESS_ID = ?"
					+ ", WU_ID = ?"
					+ " WHERE ISSUEID = ?";
			conn = ServerHelper.instance().getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, newIssueInfo.workUnitID);
			stmt.setString(2, newIssueInfo.description);
			stmt.setInt(3, newIssueInfo.statusID);
			stmt.setInt(4, newIssueInfo.priorityID);
			stmt.setInt(5, newIssueInfo.typeID);
			stmt.setString(6, newIssueInfo.owner);
			stmt.setDate(7, newIssueInfo.startDate);
			stmt.setDate( 8, newIssueInfo.dueDate);
			stmt.setDate( 9, newIssueInfo.closeDate);
			stmt.setString(10, newIssueInfo.comment);
			stmt.setString(11, newIssueInfo.reference);
			stmt.setString(12, newIssueInfo.creator);
			stmt.setInt(13, newIssueInfo.processId);
			stmt.setLong(14, newIssueInfo.wuID);
			stmt.setLong(15, newIssueInfo.issueID);
			return (stmt.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, null);
		}
	}
	public static boolean addIssue(IssueInfo newIssueInfo) {
		Connection conn = null;
		String sql = null;
		PreparedStatement stmt = null;
		try {
			sql =
				"INSERT INTO ISSUE (issueid, workunitid, description, statusid, priorityid, typeid,"
					+ "owner, startdate, duedate, comments, closeddate, reference,creator,riskid, process_id,WU_ID) "
					+ " VALUES (NVL((SELECT DISTINCT MAX(ISSUEID)+1 MAXISSUEID FROM ISSUE),1),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			conn = ServerHelper.instance().getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, newIssueInfo.workUnitID);
			stmt.setString(2, newIssueInfo.description);
			stmt.setInt(3, newIssueInfo.statusID);
			stmt.setInt(4, newIssueInfo.priorityID);
			stmt.setInt(5, newIssueInfo.typeID);
			stmt.setString(6, newIssueInfo.owner);
			stmt.setDate( 7, newIssueInfo.startDate);
			stmt.setDate( 8, newIssueInfo.dueDate);
			stmt.setString(9, newIssueInfo.comment);
			stmt.setDate( 10, newIssueInfo.closeDate);
			stmt.setString(11, newIssueInfo.reference);
			stmt.setString(12, newIssueInfo.creator);
			stmt.setLong(13, newIssueInfo.riskID);
			stmt.setInt(14, newIssueInfo.processId);
			stmt.setLong(15, newIssueInfo.wuID);
			return (stmt.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("sql stmt = " + sql);
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, null);
		}
	}
	public static boolean deleteIssue(final int issueID) {
        return Db.delete(issueID,"ISSUEID","ISSUE");

	}
	/**
	 * returns vector of IssueInfo
	 */
	public static final Vector getIssueListByWorkUnit(final long workUnitID) {
		Connection conn = null;
		String sql = null;
		PreparedStatement stmt = null;
		final Vector vector = new Vector();
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			
			if (workUnitID!=Parameters.PQA_WU  ){
				sql =
					"SELECT ISSUE.*,DEVELOPER.NAME FROM ISSUE, DEVELOPER"
					+" WHERE OWNER = ACCOUNT AND WORKUNITID = ?"
					+ " ORDER BY DUEDATE DESC";
			}
			else{//for PQA we need to know which project/group the issue is related to (issue.wu_id)
				sql =
					"SELECT ISSUE.*,DEVELOPER.NAME,WORKUNIT.PARENTWORKUNITID,WORKUNIT.WORKUNITNAME FROM ISSUE, DEVELOPER,WORKUNIT"
					+" WHERE OWNER = ACCOUNT AND ISSUE.WORKUNITID = ? AND ISSUE.WU_ID  = WORKUNIT.WORKUNITID (+)"
					+" ORDER BY DUEDATE DESC";
			
			}
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1,workUnitID);
			rs = stmt.executeQuery();
			while (rs.next()) {
				final IssueInfo issueInfo = new IssueInfo();
				issueInfo.issueID = rs.getInt("ISSUEID");
				issueInfo.workUnitID = workUnitID;
				issueInfo.description = rs.getString("DESCRIPTION");
				issueInfo.statusID = rs.getInt("STATUSID");
				issueInfo.priorityID = rs.getInt("PRIORITYID");
				issueInfo.typeID = rs.getInt("TYPEID");
				issueInfo.owner = rs.getString("OWNER");
				issueInfo.startDate = rs.getDate("STARTDATE");
				issueInfo.dueDate = rs.getDate("DUEDATE");
				issueInfo.closeDate = rs.getDate("CLOSEDDATE");
				issueInfo.comment = rs.getString("COMMENTS");
				issueInfo.reference = rs.getString("REFERENCE");
				issueInfo.creator = rs.getString("CREATOR");
				issueInfo.ownerName = rs.getString("NAME");
				issueInfo.processId = rs.getInt("PROCESS_ID");
				if (workUnitID==Parameters.PQA_WU ){
					issueInfo.wuID= rs.getLong("WU_ID");
					issueInfo.parentwuID= rs.getLong("PARENTWORKUNITID");
					issueInfo.wuName=rs.getString("WORKUNITNAME");
					if (issueInfo.wuID<0)
						issueInfo.wuName="General";
					else
						issueInfo.wuName=rs.getString("WORKUNITNAME");
				}
				vector.add(issueInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("sql stmt = " + sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, rs);
			return vector;
		}
	}
	public static Vector getIssueList(final long workUnitID, final java.sql.Date beginMonth, final java.sql.Date endMonth, final String strWhere) {
		Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
		sql =	"SELECT c.*, PROJECT.CODE, PROJECT.START_DATE"
				+ " FROM WORKUNIT a,WORKUNIT b, PROJECT, ISSUE c"
				+ " WHERE a.type(+) = 2 AND"
				+ " PROJECT.PROJECT_ID = a.TABLEID (+)"
				+ " AND a.PARENTWORKUNITID = b.WORKUNITID (+)"
				+ " AND (a.PARENTWORKUNITID =? OR b.PARENTWORKUNITID=?)"
				+ " AND a.WORKUNITID = c.WORKUNITID"
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
				final IssueInfo issueInfo = new IssueInfo();
				
				issueInfo.issueID = rs.getInt("ISSUEID");
				issueInfo.workUnitID = rs.getLong("WORKUNITID");
				issueInfo.start_date = rs.getDate("START_DATE");
				issueInfo.projectCode = rs.getString("CODE");
				issueInfo.description = rs.getString("DESCRIPTION");
				issueInfo.statusID = rs.getInt("STATUSID");
				issueInfo.priorityID = rs.getInt("PRIORITYID");
				issueInfo.typeID = rs.getInt("TYPEID");
				issueInfo.owner = rs.getString("OWNER");
				issueInfo.startDate = rs.getDate("STARTDATE");
				issueInfo.dueDate = rs.getDate("DUEDATE");
				issueInfo.closeDate = rs.getDate("CLOSEDDATE");
				issueInfo.comment = rs.getString("COMMENTS");
				issueInfo.reference = rs.getString("REFERENCE");
				issueInfo.creator = rs.getString("CREATOR");
				issueInfo.processId = rs.getInt("PROCESS_ID");
				
				resultVector.addElement(issueInfo);
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
	public static final Vector getIssuesByDate(final long projectID, final java.util.Date reportDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		final Vector list = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT "
					+ "DESCRIPTION, STATUSID, PRIORITYID, STARTDATE, DUEDATE "
					+ "FROM "
					+ "ISSUE, WORKUNIT "
					+ "WHERE "
					+ "(STATUSID = 0 OR STATUSID = 3) AND "
					+ "STARTDATE <= ? AND "
					+ "WORKUNIT.WORKUNITID =ISSUE.WORKUNITID AND WORKUNIT.TYPE=2 AND  WORKUNIT.TABLEID =?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setDate( 1, CommonTools.toSQLDate(reportDate));
			prepStmt.setLong(2, projectID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				final IssueInfo info = new IssueInfo();
				info.description = rs.getString("DESCRIPTION");
				info.statusID = rs.getInt("STATUSID");
				info.priorityID = rs.getInt("PRIORITYID");
				info.startDate = rs.getDate("STARTDATE");
				info.dueDate = rs.getDate("DUEDATE");
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
	 * used for reports
	 * result[0]= num issues not cancelled
	 * result[1]= num issues closed
	 */
	public static int[] getIssueCount(final long projectID, final java.sql.Date reportDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		int[] result = new int[2];
		result[0] = 0;
		result[1] = 0;
		try {
			//num issues not cancelled
			sql =
				"SELECT "
					+ "COUNT(ISSUEID) N_ISSUE "
					+ "FROM "
					+ "ISSUE ,WORKUNIT "
					+ "WHERE "
					+ "WORKUNIT.WORKUNITID =ISSUE.WORKUNITID AND WORKUNIT.TYPE=2 AND  WORKUNIT.TABLEID =? AND "
					+ "STARTDATE <= ? AND STATUSID <> 1";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectID);
			prepStmt.setDate(2, reportDate);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				result[0] = rs.getInt("N_ISSUE");
			}
			rs.close();
			prepStmt.close();
			//num issues closed
			sql =
				"SELECT "
					+ "COUNT(ISSUEID) N_ISSUE "
					+ "FROM "
					+ "ISSUE,WORKUNIT "
					+ "WHERE "
					+ "WORKUNIT.WORKUNITID =ISSUE.WORKUNITID AND WORKUNIT.TYPE=2 AND  WORKUNIT.TABLEID =? AND "
					+ "STARTDATE < ? AND "
					+ "(STATUSID = 2) AND CLOSEDDATE <= ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectID);
			prepStmt.setDate(2, reportDate);
			prepStmt.setDate(3, reportDate);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				result[1] = rs.getInt("N_ISSUE");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return result;
		}
	}
}
