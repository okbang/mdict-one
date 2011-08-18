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
import com.fms1.infoclass.InterfaceInfo;

public final class Interface {
	public static final int NUMBER_OF_ROW_ADDABLE = 10;
	
	public static final int doAddInterfaces(final InterfaceInfo info, final long prjPlanID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;
		int iCount = 0;		
		
		try {
			conn = ServerHelper.instance().getConnection();
			
			sql =  " INSERT INTO INTERFACE("
					+ "INTERFACE_ID, "
					+ "PROJECT_PLAN_ID,"
					+ "FUNCTION, "
					+ "CONTACT_PERSON, "
					+ "CONTACT, "
					+ "RESPONSIBILITY, "					
					+ "DEPARTMENT, "
					+ "OTHER_PROJECT_NAME, "
					+ "DEPENDENCY,"
					+ "CONTACT_ACC,"
					+ "TYPE)"
				 + " VALUES(NVL((SELECT MAX(INTERFACE_ID)+1 FROM INTERFACE),1),"+ prjPlanID+",?,?,?,?,?,?,?,?,?)";
			stm = conn.prepareStatement(sql);				
			stm.setString(1, info.function);				
			stm.setString(2, info.contactPerson);
			stm.setString(3, info.contact);
			stm.setString(4, info.responsibility);
			stm.setString(5, info.department);
			stm.setString(6, info.otherProjName);
			stm.setString(7, info.dependency);
			stm.setString(8, info.contactAccount);
			stm.setInt(9, info.type);
			
			stm.executeUpdate();
			
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
	 
		return iRet;
	}
	
	public static final int doUpdateInterface(Vector vInterfaceInfo, final long prjID) {		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;		
		InterfaceInfo info = null;
		int nReq = vInterfaceInfo.size();
		int iRet = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < nReq; i++) {
				info = (InterfaceInfo) vInterfaceInfo.elementAt(i);
							
				sql =  " UPDATE INTERFACE I "
						+ "SET I.FUNCTION = ?"				    
						+ "  , I.CONTACT_PERSON = ?"						
						+ "  , I.CONTACT = ?"
						+ "  , I.RESPONSIBILITY = ?"
						+ "  , I.DEPARTMENT = ?"
						+ "  , I.DEPENDENCY = ?"
						+ "  , I.OTHER_PROJECT_NAME = ?"
						+ "  , I.CONTACT_ACC = ?"
					 + " WHERE I.INTERFACE_ID = ?";
				stm = conn.prepareStatement(sql);				
				stm.setString(1, info.function);				
				stm.setString(2, info.contactPerson);
				stm.setString(3, info.contact);
				stm.setString(4, info.responsibility);
				stm.setString(5, info.department);
				stm.setString(6, info.dependency);
				stm.setString(7, info.otherProjName);				
				stm.setString(8, info.contactAccount);
				stm.setLong(9, info.interfaceID);
				
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
			iRet = 1;
		} finally {
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final boolean doDeletePLInterface(final long interfaceID) {
			return Db.delete(interfaceID, "INTERFACE_ID", "INTERFACE");
	}
	
	public static Vector getProjectPlanIDOfUser(String strUserName) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		Vector projectPlanList = new Vector();
		long ppID = 0;
		try {
			conn = ServerHelper.instance().getConnection();			
			sql =
				  " SELECT A.PROJECT_PLAN_ID"
				+ " FROM INTERFACE A"
				+ " WHERE UPPER(A.CONTACT_ACC) = UPPER(?)";
            
			stm = conn.prepareStatement(sql);
			stm.setString(1, strUserName);
			rs = stm.executeQuery(sql);
			while (rs.next()) {				
				ppID = rs.getLong("PROJECT_PLAN_ID");				
				projectPlanList.add(""+ppID);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(sql);
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return projectPlanList;
		}
	} 
	
}