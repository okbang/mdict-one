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
 
 /**
 * Fsoft
 * Project:FMS1
 * NgaHT-SEPG
 */
package com.fms1.common;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import com.fms1.infoclass.*;
import com.fms1.web.*;
/**
 * Role definition
 *
 */
public final class RightGroup  {
	public static final RightGroupInfor getRightGroup(final String id) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
        ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM RIGHTGROUP WHERE RIGHTGROUPID='" + id + "'";
			rs = stm.executeQuery(sql);
			if (!rs.next())
				return null;
			final RightGroupInfor rgi = new RightGroupInfor();
			rgi.rightGroupID = rs.getString("RIGHTGROUPID");
			rgi.mngLevel = rs.getString("MNGLEVEL");
			rgi.description = rs.getString("DESCRIPTION");
			rgi.protection=rs.getInt("PROTECTED");
			return rgi;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
            ServerHelper.closeConnection(conn,stm,rs);
		}
		return null;
	}
	/**
	 *  Add new RightGroup to RIGHTGROUP
	 * @param:RightGroupInfor
	 * return status
	 */
	public static final boolean addRightGroup(final RightGroupInfor rgInfor) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql =
				"INSERT INTO RIGHTGROUP (RIGHTGROUPID,MNGLEVEL,DESCRIPTION) VALUES('"
					+ rgInfor.rightGroupID
					+ "','"
					+ rgInfor.mngLevel
					+ "','"
					+ rgInfor.description
					+ "')";
            return stm.executeUpdate(sql)>0;

		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
            ServerHelper.closeConnection(conn,stm,null);
		}

	}
	/**
	 * get  uppdate RightGroup to RIGHTGROUP
	 * @param:RightGroupInfor, RightGroupID
	 * return status
	 */
	public static final boolean updateRightGroup(final RightGroupInfor rgInfor, final String id) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql =
				"UPDATE RIGHTGROUP SET RIGHTGROUPID='"
					+ rgInfor.rightGroupID
					+ "',MNGLEVEL='"
					+ rgInfor.mngLevel
					+ "',DESCRIPTION='"
					+ rgInfor.description
					+ "' WHERE RIGHTGROUPID='"
					+ id
					+ "'";
			return (stm.executeUpdate(sql)>0);
		}
		catch (SQLException e) {
			e.printStackTrace();
            return false;
		}
		finally {
            ServerHelper.closeConnection(conn,stm,null);
		}

	}
	/**
	 * get  delete RightGroup
	 * @param:RightGroupID
	 * return status
	 */
	public static final boolean deleteRightGroup(final String id) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "DELETE RIGHTFORPAGE WHERE RIGHTGROUPID='" + id + "'";
			stm.executeUpdate(sql);
			sql = "DELETE RIGHTGROUPOFUSERBYWORKUNIT WHERE RIGHTGROUPID='" + id + "'";
			stm.executeUpdate(sql);
			sql = "DELETE RIGHTGROUP WHERE RIGHTGROUPID='" + id + "'";
			stm.executeUpdate(sql);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		finally {
			ServerHelper.closeConnection(conn,stm,null);
		}

	}
	/**
	 * get vector of RightGroup
	 * element of Vector is RightGroupInfor
	 */
	public static final Vector getRightGroupVector() {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs =null;
		final Vector vt = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM RIGHTGROUP ORDER BY UPPER(RIGHTGROUPID)";
			rs = stm.executeQuery(sql);
			RightGroupInfor rgi;
			while (rs.next()) {
				rgi = new RightGroupInfor();
				rgi.rightGroupID = rs.getString("RIGHTGROUPID");
				rgi.mngLevel = rs.getString("MNGLEVEL");
				rgi.description = rs.getString("DESCRIPTION");
				rgi.protection=rs.getInt("PROTECTED");
				vt.add(rgi);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
			return vt;
		}

	}

}