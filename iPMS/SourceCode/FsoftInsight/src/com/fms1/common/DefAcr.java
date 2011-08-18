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
import com.fms1.infoclass.DefAcrInfo;
import java.util.Date;

public final class DefAcr {
	public static final int NUMBER_OF_ROW_ADDABLE = 10;

	public static final Vector getDefAcrList(final long prjID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {			
			conn = ServerHelper.instance().getConnection();			
			sql = "SELECT * "
				 +" FROM DEFINITIONS_ACRONYMS A" 
				 +" WHERE A.DEF_PRJ_ID = ?"
				 +" ORDER BY DEF_ID DESC";
					
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			DefAcrInfo info ;
			while (rs.next()) {
				info = new DefAcrInfo();
				info.defID = rs.getLong("DEF_ID");				
				info.defPrjID = rs.getLong("DEF_PRJ_ID");
				info.acronym = rs.getString("ACRONYM");
				info.definition = rs.getString("DEFINITION");				
				info.note = rs.getString("NOTE");				
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
	
	public static final int doAddDefAcr(final DefAcrInfo info, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;
		int iCount = 0;
	
		try {
			conn = ServerHelper.instance().getConnection();
		
			sql =  " INSERT INTO DEFINITIONS_ACRONYMS"
					+ "( DEF_ID, "
					+ "  DEF_PRJ_ID,"
					+ "  ACRONYM, "
					+ "  DEFINITION, "
					+ "  NOTE) "	
				 + " VALUES(NVL((SELECT MAX(DEF_ID)+1 FROM DEFINITIONS_ACRONYMS),1),"+ prjID+",?,?,?)";
			
			stm = conn.prepareStatement(sql);				
			stm.setString(1, info.acronym);
			stm.setString(2, info.definition);
			stm.setString(3, info.note);			
		
			stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final int doUpdateDefAcr(Vector vDefList, final long prjID) {		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;		
		DefAcrInfo info = null;
		int nSer = vDefList.size();
		int iRet = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < nSer; i++) {
				info = (DefAcrInfo) vDefList.elementAt(i);
							
				sql =  " UPDATE DEFINITIONS_ACRONYMS P "
						+ "SET P.ACRONYM = ?"
						+ "  , P.DEFINITION = ?"
						+ "  , P.NOTE = ?"						
					 + " WHERE P.DEF_ID = ?";
				
				stm = conn.prepareStatement(sql);				
				stm.setString(1, info.acronym);
				stm.setString(2, info.definition);
				stm.setString(3, info.note);
				stm.setLong(4, info.defID);
				
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
	
	public static final boolean doDeleteDefAcr(final long defID) {
			return Db.delete(defID, "DEF_ID", "DEFINITIONS_ACRONYMS");
	}
}
