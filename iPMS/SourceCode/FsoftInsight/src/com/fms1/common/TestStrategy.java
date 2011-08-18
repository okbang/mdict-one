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
import com.fms1.tools.CommonTools;
import com.fms1.tools.ConvertString;
import com.fms1.tools.Db;
import com.fms1.web.*;
/**
 * Other quality activities logic
 *
 */
public final class TestStrategy {
	public static final Vector getTestStrategyList(final long prjID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {			
			conn = ServerHelper.instance().getConnection();			
			//get the list of strategy of meeting quality objectives
			
			sql = "SELECT  A.TEST_ID," 
						+ "A.TEST_ITEM,"
						+ "A.TEST_TYPE,"
						+ "A.TEST_REVIEWER,"
						+ "A.TEST_COMPLETION_CRITERIA,"
						+ "A.TEST_ENTRY_CRITERIA"
				 +" FROM TEST_STRATEGY A" 
				 +" WHERE A.TEST_PRJ_ID = ?"				 
				 +" ORDER BY A.TEST_TYPE, A.TEST_ID DESC";
				
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			TestStrategyInfo info ;
			while (rs.next()) {
				info = new TestStrategyInfo();
				info.testID=rs.getLong("TEST_ID");
				info.testItem = rs.getString("TEST_ITEM");
				info.testType = rs.getInt("TEST_TYPE");
				info.testReviewer = rs.getString("TEST_REVIEWER");
				info.testComplCriteria = rs.getString("TEST_COMPLETION_CRITERIA");
				info.testEntryCriteria = rs.getString("TEST_ENTRY_CRITERIA");
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
	
	public static final int doAddPLTestStrategy(final TestStrategyInfo testInfo, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;
		int iCount = 0;
	
		try {
			conn = ServerHelper.instance().getConnection();
		
			sql =  " INSERT INTO TEST_STRATEGY("
					+ "TEST_ID,"
					+ "TEST_PRJ_ID,"
					+ "TEST_ITEM,"				    
					+ "TEST_TYPE,"
					+ "TEST_REVIEWER,"
					+ "TEST_COMPLETION_CRITERIA,"
					+ "TEST_ENTRY_CRITERIA)"
				 + " VALUES(NVL((SELECT MAX(TEST_ID)+1 FROM TEST_STRATEGY),1),"+ prjID+",?,?,?,?,?)";
			stm = conn.prepareStatement(sql);
			stm.setString(1, testInfo.testItem);
			stm.setInt(2, testInfo.testType);
			stm.setString(3, testInfo.testReviewer);
			stm.setString(4, testInfo.testComplCriteria);
			stm.setString(5, testInfo.testEntryCriteria);
			
			stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final int doUpdatePLTestStrategy(Vector vTestStratList, final long prjID) {		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;		
		TestStrategyInfo testInfo = null;
		int ntest = vTestStratList.size();
		int iRet = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < ntest; i++) {
				testInfo = (TestStrategyInfo) vTestStratList.elementAt(i);
							
				sql =  " UPDATE TEST_STRATEGY R "
						+ "SET R.TEST_PRJ_ID = ?"
						+ "  , R.TEST_ITEM = ?"
						+ "  , R.TEST_TYPE = ?"
						+ "  , R.TEST_REVIEWER = ?"
						+ "  , R.TEST_COMPLETION_CRITERIA = ?"
						+ "  , R.TEST_ENTRY_CRITERIA = ?"
					 + " WHERE R.TEST_ID = ?";
				stm = conn.prepareStatement(sql);
				stm.setLong(1, prjID);
				stm.setString(2, testInfo.testItem);				
				stm.setInt(3, testInfo.testType);	
				stm.setString(4, testInfo.testReviewer);
				stm.setString(5, testInfo.testComplCriteria);			
				stm.setString(6, testInfo.testEntryCriteria);
				stm.setLong(7, testInfo.testID);
				
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
	
	public static final boolean doDeletePLTestStrategy(final long testID) {
			return Db.delete(testID, "TEST_ID", "TEST_STRATEGY");
	}
}
