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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fms1.tools.*;
import com.fms1.web.*;
import com.fms1.infoclass.ProjSchedInfo;
import java.util.Date;

public final class ProjectSchedule {
	public static final int NUMBER_OF_ROW_ADDABLE = 10;

	public static final Vector getProjectScheduleList(final long prjID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {			
			conn = ServerHelper.instance().getConnection();			
			sql = "SELECT A.SCHED_ID," 
					   + "A.SCHED_PRJ_ID," 
					   + "A.SCHED_ACTIVITY,"
					   + "A.SCHED_STARTDATE," 
					   + "A.SCHED_RESPONSIBLE," 
					   + "A.SCHED_NOTE,"
					   + "A.SCHED_TYPE"
				 +" FROM PROJECT_SCHEDULE A" 
				 +" WHERE A.SCHED_PRJ_ID = ?"
				 +" ORDER BY SCHED_TYPE ASC, A.SCHED_ID DESC";
					
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			ProjSchedInfo info ;
			while (rs.next()) {
				info = new ProjSchedInfo();
				info.schedID = rs.getLong("SCHED_ID");				
				info.schedPrjID = rs.getLong("SCHED_PRJ_ID");
				info.schedActivity = rs.getString("SCHED_ACTIVITY");
				info.schedStartDate = rs.getDate("SCHED_STARTDATE");
				info.schedResponsible = rs.getString("SCHED_RESPONSIBLE");
				info.schedNote = rs.getString("SCHED_NOTE");
				info.schedType = rs.getInt("SCHED_TYPE");
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
	
	public static final int doAddProjectSchedule(final ProjSchedInfo schedInfo, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;
		int iCount = 0;
	
		try {
			conn = ServerHelper.instance().getConnection();
		
			sql =  " INSERT INTO PROJECT_SCHEDULE"
					+ "( SCHED_ID, "
					+ "  SCHED_PRJ_ID,"
					+ "  SCHED_ACTIVITY, "				    
					+ "  SCHED_STARTDATE, "
					+ "  SCHED_RESPONSIBLE, "
					+ "  SCHED_NOTE, "
					+ "  SCHED_TYPE ) "					
				 + " VALUES(NVL((SELECT MAX(SCHED_ID)+1 FROM PROJECT_SCHEDULE),1),"+ prjID+",?,?,?,?,?)";
			
			stm = conn.prepareStatement(sql);				
			stm.setString(1, schedInfo.schedActivity);				
			stm.setString(2, CommonTools.dateFormat(schedInfo.schedStartDate));
			stm.setString(3, schedInfo.schedResponsible);
			stm.setString(4, schedInfo.schedNote);
			stm.setInt(5, schedInfo.schedType);
		
			stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final int doUpdateProjectSchedule(Vector vSchedInfo, final long prjID) {		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;		
		ProjSchedInfo info = null;
		int nSched = vSchedInfo.size();
		int iRet = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < nSched; i++) {
				info = (ProjSchedInfo) vSchedInfo.elementAt(i);
							
				sql =  " UPDATE PROJECT_SCHEDULE P "
						+ "SET P.SCHED_ACTIVITY = ?"				    
						+ "  , P.SCHED_STARTDATE = ?"
						+ "  , P.SCHED_RESPONSIBLE = ?"
						+ "  , P.SCHED_NOTE = ?"
						+ "  , P.SCHED_TYPE = ?"
					 + " WHERE P.SCHED_ID = ?";
				
				stm = conn.prepareStatement(sql);				
				stm.setString(1, info.schedActivity);				
				stm.setString(2, CommonTools.dateFormat(info.schedStartDate));
				stm.setString(3, info.schedResponsible);
				stm.setString(4, info.schedNote);
				stm.setInt(5, info.schedType);
				stm.setLong(6, info.schedID);
				
				
				stm.executeUpdate();
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			try {
				conn.rollback();
				conn.setAutoCommit(true);
			} catch (SQLException ex) {			
				ex.printStackTrace();
			}
			finally {
				e.printStackTrace();
				iRet = 1;
			}
		} finally {
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final boolean doDeleteProjectSchedule(final long schedID) {
			return Db.delete(schedID, "SCHED_ID", "PROJECT_SCHEDULE");
	}
}
