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
import com.fms1.web.*;
/**
 * Role page access definition 
 * @author NgaHT
 *
 */
public final class RightForPage {
	public static final int RIGHT_MANAGE=3;
	public static final int RIGHT_VIEW=2;
	public static final int RIGHT_NONE=1;

	/**
	 * return recordset of RightForPage
	 * param RightGroupId
	 */
	public static final Vector getRightForPage(final String id) {
		Connection conn = null;
		PreparedStatement prepStm = null;
		String sql = null;
		ResultSet rs =null;
		final Vector vt = new Vector();
		if (id == null ||id.length()==0)
			return vt;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT p.PAGENAME,p.PAGEID, p.ORDR,p.TYPE, nvl(r.PRIVILEGE,1) PRIVILEGE "
				+" FROM RIGHTFORPAGE r, PAGE p"
				+" WHERE r.PAGEID (+)=p.PAGEID"
				+" AND TRIM(r.RIGHTGROUPID (+)) = ? ORDER BY p.ORDR";
			prepStm = conn.prepareStatement(sql);
			prepStm.setString(1, id.trim());
			rs = prepStm.executeQuery();
			while (rs.next()) {
				final RightForPageInfor rpi = new RightForPageInfor();
				rpi.rightGroupID = id;
				rpi.pageName2 = rs.getString("PAGENAME");
				rpi.pageID=rs.getLong("PAGEID");
				rpi.ordr = rs.getInt("ORDR");
				rpi.privilege = rs.getInt("PRIVILEGE");
				rpi.level=rs.getInt("TYPE");
				vt.add(rpi);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStm,rs);
			return vt;
		}
	}
	/**
	 * uppdate RightForPage
	 * @param: RightGroupID, PageID
	 * return status
	 */
	public static final boolean uppdateRightForPage(final String rgId, final long pId, final int mode) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
     	try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql =
				"UPDATE RIGHTFORPAGE SET PRIVILEGE="
					+ mode
					+ " WHERE RIGHTGROUPID='"
					+ rgId
					+ "' AND PAGEID="
					+ pId;
          	int updatedRows =stm.executeUpdate(sql);
			if (updatedRows ==0)
				return addnewRightForPage( rgId,pId,mode);
			return true;

		}
		catch (SQLException e) {
			System.err.println("RightForPage.uppdateRightForPage Error :");
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				if (stm != null)
					stm.close();
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * addnew RightForPage
	 * @param: RightGroupID, PageID
	 * return status
	 */
	public static final boolean addnewRightForPage(final String rgId, final long pId, final int mode) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;

		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql =
				"INSERT INTO RIGHTFORPAGE (RIGHTGROUPID,PAGEID,PRIVILEGE) VALUES ('"
					+ rgId
					+ "',"
					+ pId
					+ ","
					+ mode
					+ ")";
			stm.executeQuery(sql);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				if (stm != null)
					stm.close();
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			}
			catch (SQLException e2) {
				e2.printStackTrace();
			}
		}

	}

}