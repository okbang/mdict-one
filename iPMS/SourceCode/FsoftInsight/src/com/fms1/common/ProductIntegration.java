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
import com.fms1.infoclass.IntegrStratInfo;

public final class ProductIntegration {
	public static final int NUMBER_OF_ROW_ADDABLE = 10;

	public static final Vector getIntegrationStratList(final long prjID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {			
			conn = ServerHelper.instance().getConnection();
			// get the table of completeness rates
			//get the list of requirements
			sql = "SELECT A.INTEGR_ID," 
					   + "A.INTEGR_COMP_ID," 
					   + "A.INTEGR_DESCRIPTION,"
					   + "A.INTEGR_WITH_COMPONENTS," 
					   + "A.INTEGR_ORDER," 
					   + "A.INTEGR_READY_NEED"
				 +" FROM PRODUCT_INTEGRATION_STRATEGY A" 
				 +" WHERE A.INTEGR_PRJ_ID = ?"
				 +" ORDER BY A.INTEGR_ID DESC";
					
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			IntegrStratInfo info ;
			while (rs.next()) {
				info = new IntegrStratInfo();
				info.integrID=rs.getLong("INTEGR_ID");
				info.integrCompID = rs.getString("INTEGR_COMP_ID");
				info.integrDesc = rs.getString("INTEGR_DESCRIPTION");
				info.integrWithComp = rs.getString("INTEGR_WITH_COMPONENTS");
				info.integrOrder = rs.getString("INTEGR_ORDER");
				info.integrReadyNeed = rs.getString("INTEGR_READY_NEED");
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
	
	public static final int doAddProductIntegration(final IntegrStratInfo integrInfo, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;
		int iCount = 0;
	
		try {
			conn = ServerHelper.instance().getConnection();
		
			sql =  " INSERT INTO PRODUCT_INTEGRATION_STRATEGY("
					+ "INTEGR_ID, "
					+ "INTEGR_PRJ_ID,"
					+ "INTEGR_COMP_ID, "				    
					+ "INTEGR_DESCRIPTION, "
					+ "INTEGR_WITH_COMPONENTS, "
					+ "INTEGR_ORDER, "
					+ "INTEGR_READY_NEED)"
				 + " VALUES(NVL((SELECT MAX(INTEGR_ID)+1 FROM PRODUCT_INTEGRATION_STRATEGY),1),"+ prjID+",?,?,?,?,?)";
			stm = conn.prepareStatement(sql);				
			stm.setString(1, integrInfo.integrCompID);				
			stm.setString(2, integrInfo.integrDesc);
			stm.setString(3, integrInfo.integrWithComp);
			stm.setString(4, integrInfo.integrOrder);
			stm.setString(5, integrInfo.integrReadyNeed);
		
			stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	
	public static final int doUpdateProductIntegration(Vector vIntegrInfo, final long prjID) {
		// landd start
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;		
		IntegrStratInfo integrInfo = null;
		int nIntegr = vIntegrInfo.size();
		int iRet = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < nIntegr; i++) {
				integrInfo = (IntegrStratInfo) vIntegrInfo.elementAt(i);
							
				sql =  " UPDATE PRODUCT_INTEGRATION_STRATEGY P "
						+ "SET P.INTEGR_COMP_ID = ?"				    
						+ "  , P.INTEGR_DESCRIPTION = ?"
						+ "  , P.INTEGR_WITH_COMPONENTS = ?"
						+ "  , P.INTEGR_ORDER = ?"
						+ "  , P.INTEGR_READY_NEED = ?"
					 + " WHERE P.INTEGR_ID = ?";
				stm = conn.prepareStatement(sql);				
				stm.setString(1, integrInfo.integrCompID);				
				stm.setString(2, integrInfo.integrDesc);
				stm.setString(3, integrInfo.integrWithComp);
				stm.setString(4, integrInfo.integrOrder);
				stm.setString(5, integrInfo.integrReadyNeed);
				stm.setLong(6, integrInfo.integrID);
				
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
	
	public static final boolean doDeletePLProIntegration(final long integrID) {
			return Db.delete(integrID, "INTEGR_ID", "PRODUCT_INTEGRATION_STRATEGY");
	}
}
