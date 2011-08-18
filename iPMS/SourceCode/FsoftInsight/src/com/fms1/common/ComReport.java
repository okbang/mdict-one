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
import com.fms1.infoclass.ComReportInfo;
import java.util.Date;

public final class ComReport {
	public static final int NUMBER_OF_ROW_ADDABLE = 10;

	public static final Vector getCommunicationReportingList(final long prjID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {			
			conn = ServerHelper.instance().getConnection();			
			sql = "SELECT A.COM_ID," 
					   + "A.COM_PRJ_ID," 
					   + "A.COM_TYPE,"
					   + "A.COM_PARENT_TYPE," 
					   + "A.COM_METHOD_TOOL," 
					   + "A.COM_WHEN,"
					   + "A.COM_INFORMATION,"
					   + "A.COM_RESPONSIBLE"
				 +" FROM COMMUNICATION_REPORT A" 
				 +" WHERE A.COM_PRJ_ID = ?"
				 +" ORDER BY COM_PARENT_TYPE ASC, A.COM_ID DESC";
					
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			ComReportInfo info ;
			while (rs.next()) {
				info = new ComReportInfo();
				info.comID = rs.getLong("COM_ID");				
				info.comPrjID = rs.getLong("COM_PRJ_ID");
				info.comType = rs.getString("COM_TYPE");
				info.comParentType = rs.getInt("COM_PARENT_TYPE");
				info.comMethodTool = rs.getString("COM_METHOD_TOOL");
				info.comWhen = rs.getString("COM_WHEN");
				info.comInfo = rs.getString("COM_INFORMATION");
				info.comResp = rs.getString("COM_RESPONSIBLE");
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
	
	public static final int doAddComReport(final ComReportInfo comInfo, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;
		int iCount = 0;
	
		try {
			conn = ServerHelper.instance().getConnection();
		
			sql =  " INSERT INTO COMMUNICATION_REPORT"
					+ "( COM_ID, "
					+ "  COM_PRJ_ID,"
					+ "  COM_TYPE, "				    
					+ "  COM_PARENT_TYPE, "
					+ "  COM_METHOD_TOOL, "
					+ "  COM_WHEN, "
				    + "  COM_INFORMATION, "
					+ "  COM_RESPONSIBLE ) "					
				 + " VALUES(NVL((SELECT MAX(COM_ID)+1 FROM COMMUNICATION_REPORT),1),"+ prjID+",?,?,?,?,?,?)";
			
			stm = conn.prepareStatement(sql);				
			stm.setString(1, comInfo.comType);				
			stm.setInt(2, comInfo.comParentType);
			stm.setString(3, comInfo.comMethodTool);
			stm.setString(4, comInfo.comWhen);
			stm.setString(5, comInfo.comInfo);
			stm.setString(6, comInfo.comResp);
		
			stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final int doUpdateComReport(Vector vComList, final long prjID) {		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;		
		ComReportInfo info = null;
		int nSched = vComList.size();
		int iRet = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < nSched; i++) {
				info = (ComReportInfo) vComList.elementAt(i);
							
				sql =  " UPDATE COMMUNICATION_REPORT P "
						+ "SET P.COM_TYPE = ?"				    
						+ "  , P.COM_PARENT_TYPE = ?"
						+ "  , P.COM_METHOD_TOOL = ?"
						+ "  , P.COM_WHEN = ?"
						+ "  , P.COM_INFORMATION = ?"
				        + "  , P.COM_RESPONSIBLE = ?"
					 + " WHERE P.COM_ID = ?";
				
				stm = conn.prepareStatement(sql);				
				stm.setString(1, info.comType);				
				stm.setInt(2, info.comParentType);
				stm.setString(3, info.comMethodTool);
				stm.setString(4, info.comWhen);
				stm.setString(5, info.comInfo);
				stm.setString(6, info.comResp);
				stm.setLong(7, info.comID);
				
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
	
	public static final boolean doDeleteProjectSchedule(final long comID) {
			return Db.delete(comID, "COM_ID", "COMMUNICATION_REPORT");
	}
}
