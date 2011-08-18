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
import com.fms1.infoclass.SubTeamInfo;

public final class SubTeams {
	public static final int NUMBER_OF_ROW_ADDABLE = 10;
	//	landd add sub team infor start
	public static final Vector getWOSubTeamList(final long prjID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				" SELECT A.TEAM_ID, A.TEAM_NAME, A.TEAM_LEADER_ID, A.NOTE "
					+ " FROM TEAM A"
					+ " WHERE PARENT_TEAM_ID = ? "
					+ " ORDER BY A.TEAM_NAME ";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				final SubTeamInfo subTeamInfo = new SubTeamInfo();
				subTeamInfo.teamID = rs.getLong("TEAM_ID");
				subTeamInfo.teamName = rs.getString("TEAM_NAME");
				subTeamInfo.teamLeaderID = rs.getLong("TEAM_LEADER_ID");
				subTeamInfo.teamNote = rs.getString("NOTE");
				resultVector.addElement(subTeamInfo);
			}
			return resultVector;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}

	public static final boolean checkRefSubTeam(final long subTeamID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int nCount = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =" SELECT COUNT(*) AS ASSNUM FROM ASSIGNMENT WHERE TEAM_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, subTeamID);
			rs = stm.executeQuery();
			if (rs.next()) {
				nCount = rs.getInt("ASSNUM");
			} 
			if (nCount > 0) return true;
			else return false;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	
	public static final int doAddSubTeam(final SubTeamInfo subTeamInfo, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;
		int iCount = 0;
			
		try {
			conn = ServerHelper.instance().getConnection();				
				
			sql =  " SELECT COUNT(*) AS TEAMCOUNT FROM TEAM S"
					+ " WHERE UPPER(S.TEAM_NAME) = ?"
					+ " AND S.PARENT_TEAM_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, subTeamInfo.teamName.toUpperCase());
			stm.setLong(2, prjID);
			rs = stm.executeQuery();			
			
			if (rs.next()) {
				iCount = rs.getInt("TEAMCOUNT"); 
				if ( iCount > 0) iRet = 2;
			    else {
					sql =  " INSERT INTO TEAM(TEAM_ID, PARENT_TEAM_ID,TEAM_NAME, NOTE)"
						 + " VALUES(NVL((SELECT MAX(TEAM_ID)+1 FROM TEAM),1),"+ prjID+",?,?)";
					stm = conn.prepareStatement(sql);				
					stm.setString(1, subTeamInfo.teamName);				
					stm.setString(2, subTeamInfo.teamNote);
					stm.executeUpdate();
			    }
			}
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
		 
		return iRet;
	}
	
	public static final int doUpdateSubTeam(final Vector vSubTeam) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;		
		SubTeamInfo subTeamInfo = null;
		int nSubTeam = vSubTeam.size();
		int iRet = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < nSubTeam; i++) {
				subTeamInfo = (SubTeamInfo) vSubTeam.elementAt(i);			
				sql =  " UPDATE TEAM A"
					 + " SET A.TEAM_NAME = ?, A.NOTE = ? "
					 + " WHERE A.TEAM_ID = ?";
				stm = conn.prepareStatement(sql);				
				stm.setString(1, subTeamInfo.teamName);				
				stm.setString(2, subTeamInfo.teamNote);
				stm.setLong(3, subTeamInfo.teamID);				
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
			e.printStackTrace();
			iRet = 1;
		} finally {
			ServerHelper.closeConnection(conn, stm,null);
		}
		
		return iRet;
	}
	
	public static final boolean doDeleteSubTeam(final long subTeamID) {
		return Db.delete(subTeamID, "TEAM_ID", "TEAM");
	}
	// landd add sub team infor end
}
