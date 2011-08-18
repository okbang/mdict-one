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
import com.fms1.tools.ReportMonth;
import com.fms1.tools.CommonTools;
import com.fms1.tools.Db;
import com.fms1.web.*;
/**
 * Oranization logic
 * Author: HaiMM
 * 09-Feb-09
 */
public final class Organization {
	
	public static final Vector getOrgPlan(long prjId,Vector durationList) {
		DurationInfo durationInf=null;
		Vector orgList=new Vector();
		
		OrgInfo orgInf, orgInfPlan;
		Vector teamList = Assignments.getWOTeamList(prjId, null, null);
		Vector orgPlanList = Organization.getPlanOrgList(prjId);
		
		for (int i=0;i<durationList.size();i++){
			durationInf=(DurationInfo)durationList.elementAt(i);
			for (int j=0;j<teamList.size();j++){
				AssignmentInfo assInf = new AssignmentInfo();
				assInf=(AssignmentInfo)teamList.elementAt(j);
				
				orgInf= new OrgInfo();
				orgInf.week=durationInf.week;
				orgInf.month=durationInf.month;
				orgInf.year=durationInf.year;

				orgInf.assID=assInf.assID;
				orgInf.devName = assInf.devName;
				orgInf.role = assInf.roleID;
				
				orgInfPlan = new OrgInfo();
				orgInfPlan = OrgInfo.getPlan(orgPlanList, assInf.assID, 
											durationInf.week, durationInf.month, durationInf.year);
				if (orgInfPlan != null)
				orgInf.plannedValue = orgInfPlan.plannedValue;
			
				orgList.add(orgInf);
			}
		}
		return orgList;
	}
	
	public static final Vector getAssAvailable(long prjId, Vector teamList) {
		Vector assAvailable = new Vector();
		
		String oldRole = "";
		String oldDevName = "";
		
		String newRole = "";
		String newDevName = "";

		for (int i=0;i<teamList.size();i++){
			AssignmentInfo assInf = new AssignmentInfo();
			assInf=(AssignmentInfo)teamList.elementAt(i);
			
			newRole = assInf.roleID;
			newDevName = assInf.devName;
			
			if ((!newRole.equals(oldRole) && !newDevName.equals(oldDevName))
				|| (newRole.equals(oldRole) && !newDevName.equals(oldDevName))
				|| (!newRole.equals(oldRole) && newDevName.equals(oldDevName))) {
					
				OrgAssAvailableInfo orgAssInf = new OrgAssAvailableInfo();
				
				orgAssInf.devName = newDevName;
				orgAssInf.role = newRole;
				orgAssInf.assID = assInf.assID;
				
				assAvailable.add(orgAssInf);
			}
			
			oldRole = newRole;
			oldDevName = newDevName;
		}
		
		return assAvailable;
	}
	
	public static final Vector getPlanOrgList(long projectid)  {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs=null;
		Vector result = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT * FROM org_plan"
				+" WHERE project_id=?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectid);
			rs=prepStmt.executeQuery();
			OrgInfo inf;
			

			while (rs.next()){
				inf = new OrgInfo();
				inf.projectID = rs.getInt("PROJECT_ID");
				inf.assID = rs.getLong("ASSIGNMENT_ID");
				inf.week = rs.getInt("week");
				inf.month = rs.getInt("month");
				inf.year = rs.getInt("year");
				inf.plannedValue = Db.getDouble(rs,"plan_value");
							
				result.add(inf);
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
	
	public static final void updateOrgPlan(Vector orgPlanList, long pid) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		PreparedStatement prepStmtInsert = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE org_plan SET " 
				+ "plan_value = ?"
				+ " WHERE project_id = ? AND week=? AND month=? AND year=? AND assignment_id=?";
			prepStmt = conn.prepareStatement(sql);
	
			sql = "Insert INTO org_plan ("
					+ " plan_value "
					+" ,project_id,week, month, year,assignment_id)" 
									+ " VALUES(?,?,?,?,?,?)";
			prepStmtInsert= conn.prepareStatement(sql);
	
			OrgInfo inf;
			for (int k = 0; k < orgPlanList.size(); k++) {
				inf = (OrgInfo) orgPlanList.elementAt(k);
		
				Db.setDouble(prepStmt,1,inf.plannedValue);
				prepStmt.setLong(2,pid);
				prepStmt.setInt(3,inf.week);
				prepStmt.setInt(4,inf.month);
				prepStmt.setInt(5,inf.year);
				prepStmt.setLong(6,inf.assID);
		
				if (prepStmt.executeUpdate()<1){
					Db.setDouble(prepStmtInsert,1,inf.plannedValue);
					prepStmtInsert.setLong(2,pid);
					prepStmtInsert.setInt(3,inf.week);
					prepStmtInsert.setInt(4,inf.month);
					prepStmtInsert.setInt(5,inf.year);
					prepStmtInsert.setLong(6,inf.assID);
					prepStmtInsert.executeUpdate();
				}
				conn.commit();
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	
	public static final void deleteOrgPlan(final long prjId){
		Connection conn = null;
		PreparedStatement prepStm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();

			sql = "DELETE org_plan WHERE project_id=?";
			prepStm = conn.prepareStatement(sql);
			prepStm.setLong(1, prjId);
			prepStm.executeUpdate();
			prepStm.close();

			conn.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally {
			ServerHelper.closeConnection(conn, prepStm, null);
		}
	}
	
	public static final void deleteTotalPlan(final long prjId){
		Connection conn = null;
		PreparedStatement prepStm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();

			sql = "DELETE org_plan_total WHERE project_id=?";
			prepStm = conn.prepareStatement(sql);
			prepStm.setLong(1, prjId);
			prepStm.executeUpdate();
			prepStm.close();

			conn.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally {
			ServerHelper.closeConnection(conn, prepStm, null);
		}
	}
	public static final void insertTotalPlan(Vector totalPlanList, long pid) {
		String sql = null;
		Connection conn = null;
		PreparedStatement prepStmtInsert = null;
		try {
			conn = ServerHelper.instance().getConnection();
	
			sql = "Insert INTO org_plan_total ("
					+ " project_id, assignment_id, total_plan)"
					+ " VALUES(?,?,?)";
			prepStmtInsert= conn.prepareStatement(sql);
	
			OrgAssAvailableInfo inf;
			for (int k = 0; k < totalPlanList.size(); k++) {
				inf = (OrgAssAvailableInfo) totalPlanList.elementAt(k);
		
				prepStmtInsert.setLong(1,pid);
				prepStmtInsert.setLong(2,inf.assID);
				Db.setDouble(prepStmtInsert,3,inf.total);
				
				prepStmtInsert.executeUpdate();
				conn.commit();
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmtInsert, null);
		}
	}
}
