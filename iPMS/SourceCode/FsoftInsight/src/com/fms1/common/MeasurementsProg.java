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
import com.fms1.infoclass.MeasureProgInfo;

public final class MeasurementsProg {
	public static final int NUMBER_OF_ROW_ADDABLE = 10;

	public static final Vector getMeasurementsProgList(final long prjID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {			
			conn = ServerHelper.instance().getConnection();			
			sql = "SELECT A.MES_ID," 
					   + "A.MES_PRJ_ID," 
					   + "A.MES_DATA_COLECT,"
					   + "A.MES_PURPOSE," 
					   + "A.MES_RESPONSIBLE," 
					   + "A.MES_WHEN"
				 +" FROM MEASUREMENTS_PROGRAM A" 
				 +" WHERE A.MES_PRJ_ID = ?"
				 +" ORDER BY A.MES_ID DESC";
					
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			MeasureProgInfo info ;
			while (rs.next()) {
				info = new MeasureProgInfo();
				info.mes_id=rs.getLong("MES_ID");				
				info.mes_prj_id = rs.getLong("MES_PRJ_ID");
				info.mes_data_colect = rs.getString("MES_DATA_COLECT");
				info.mes_purpose = rs.getString("MES_PURPOSE");
				info.mes_responsible = rs.getString("MES_RESPONSIBLE");
				info.mes_when = rs.getString("MES_WHEN");
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
	
	public static final int doAddMeasurementsProgram(final MeasureProgInfo mesInfo, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;
		int iCount = 0;
	
		try {
			conn = ServerHelper.instance().getConnection();
		
			sql =  " INSERT INTO MEASUREMENTS_PROGRAM"
					+ "( MES_ID, "
					+ "  MES_PRJ_ID,"
					+ "  MES_DATA_COLECT, "				    
					+ "  MES_PURPOSE, "
					+ "  MES_RESPONSIBLE, "
					+ "  MES_WHEN ) "					
				 + " VALUES(NVL((SELECT MAX(MES_ID)+1 FROM MEASUREMENTS_PROGRAM),1),"+ prjID+",?,?,?,?)";
			
			stm = conn.prepareStatement(sql);				
			stm.setString(1, mesInfo.mes_data_colect);				
			stm.setString(2, mesInfo.mes_purpose);
			stm.setString(3, mesInfo.mes_responsible);
			stm.setString(4, mesInfo.mes_when);			
		
			stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final int doUpdateMeasurementsProgram(Vector vMesInfo, final long prjID) {		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;		
		MeasureProgInfo info = null;
		int nIntegr = vMesInfo.size();
		int iRet = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < nIntegr; i++) {
				info = (MeasureProgInfo) vMesInfo.elementAt(i);
							
				sql =  " UPDATE MEASUREMENTS_PROGRAM P "
						+ "SET P.MES_DATA_COLECT = ?"				    
						+ "  , P.MES_PURPOSE = ?"
						+ "  , P.MES_RESPONSIBLE = ?"
						+ "  , P.MES_WHEN = ?"						
					 + " WHERE P.MES_ID = ?";
				
				stm = conn.prepareStatement(sql);				
				stm.setString(1, info.mes_data_colect);				
				stm.setString(2, info.mes_purpose);
				stm.setString(3, info.mes_responsible);
				stm.setString(4, info.mes_when);
				stm.setLong(5, info.mes_id);
				
				
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
	
	public static final boolean doDeleteMeasurementsProgram(final long mesID) {
			return Db.delete(mesID, "MES_ID", "MEASUREMENTS_PROGRAM");
	}
}
