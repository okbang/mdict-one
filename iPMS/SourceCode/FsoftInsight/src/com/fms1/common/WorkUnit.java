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
import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.*;
import com.fms1.tools.*;
import com.fms1.web.*;
import javax.servlet.http.*;
import java.util.Date;
/**
 * Organization, group, project administrative management
 *
 */
public class WorkUnit {
	public static final WorkUnitInfo getWorkUnitInfo(final long wuID) {
		WorkUnitInfo result = new WorkUnitInfo();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs=null;
		try {
			conn = ServerHelper.instance().getConnection();
			
			sql = "SELECT * FROM WORKUNIT WHERE WORKUNITID = ?" ;
			stm = conn.prepareStatement(sql);
			stm.setLong(1,wuID);
			rs = stm.executeQuery();
			if (rs.next()) {
				result.workUnitID = rs.getLong("WORKUNITID");
				result.workUnitName = ((rs.getString("WORKUNITNAME") == null)? "" : rs.getString("WORKUNITNAME"));
				result.type = rs.getInt("TYPE");
				result.parentWorkUnitID = rs.getLong("PARENTWORKUNITID");
				result.tableID = rs.getLong("TABLEID");
				result.protect= (rs.getInt("PROTECTED")==1) ;
			}
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
		}
	}
    /**
     * Get projects that running between period, used by Product index pages
     * @param wuID
     * @param startDate
     * @param endDate
     * @return
     */
    public static Vector getChildrenProjectUnit(final long wuID,
        final java.sql.Date startDate, final java.sql.Date endDate)
    {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs=null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT WORKUNITNAME,WORKUNIT.TYPE,WORKUNITID,TABLEID "
					+ " FROM WORKUNIT,PROJECT WHERE PARENTWORKUNITID=?"
					+ " AND PROJECT_ID=TABLEID"
					+ " AND PROJECT.START_DATE <= ? "
					+ " AND (PROJECT.ACTUAL_FINISH_DATE IS NULL OR PROJECT.ACTUAL_FINISH_DATE >=?)"
                    + " AND PROJECT.STATUS <> "
                    + ProjectInfo.STATUS_CANCELLED
                    + " AND PROJECT.STATUS <> "
                    + ProjectInfo.STATUS_TENTATIVE
					+ " ORDER BY WORKUNITNAME";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, wuID);
			prepStmt.setDate(2, endDate);
			prepStmt.setDate(3, startDate);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				WorkUnitInfo result = new WorkUnitInfo();
				result = new WorkUnitInfo();
				result.workUnitID = rs.getLong("WORKUNITID");
				result.workUnitName = (rs.getString("WORKUNITNAME") == null) ? "N/A" : rs.getString("WORKUNITNAME");
				result.type = rs.getInt("TYPE");
				result.parentWorkUnitID = wuID;
                result.tableID = rs.getLong("TABLEID");
				resultVector.addElement(result);
			}
			return resultVector;
		}
		catch (Exception e) {
			System.err.println("WorkUnit.getChildrenProjectUnit error :" + e.toString());
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
		return null;
	}
	/**
	 * @return Vector of groupinfo
	 */
	public static Vector getChildrenGroups(final long wuID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs=null;
		try {
			sql = "SELECT  GROUPNAME,GROUP_ID,WORKUNITID,ISOPERATIONGROUP FROM WORKUNIT,GROUPS WHERE PARENTWORKUNITID=? AND TABLEID=GROUP_ID ORDER BY GROUPNAME";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);

			stm.setLong(1,wuID);
			rs = stm.executeQuery();
			GroupInfo result;
			while (rs.next()) {

				result =  new GroupInfo();
				result.wuID = rs.getLong("WORKUNITID");
				result.name = rs.getString("GROUPNAME");
				result.isOperation = (rs.getInt("ISOPERATIONGROUP")==1);
				result.groupID = rs.getLong("GROUP_ID");;
				resultVector.addElement(result);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
			return resultVector;
		}
	}
	/**
	 * @return Vector of groupinfo
	 */
	public static GroupInfo getGroupInfo(final long groupid) {
		long[] tempArr=new long[1];
		tempArr[0]=groupid;
		Vector temp =getGroups(tempArr) ;
		return (GroupInfo)temp.elementAt(0);
	}
	public static Vector getGroups(final long groupids[]) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs=null;
		try {
			sql = "SELECT  g.GROUPNAME,g.DESCRIPTION,w.WORKUNITID,g.GROUP_ID,g.ISOPERATIONGROUP,d.ACCOUNT,g.LEADER"
			+ " FROM DEVELOPER d,GROUPS g,WORKUNIT w WHERE g.GROUP_ID in("
			+ConvertString.arrayToString(groupids,",")
			+") AND d.DEVELOPER_ID(+)= g.LEADER"
			+" AND w.TABLEID = g.GROUP_ID AND w.TYPE="+WorkUnitInfo.TYPE_GROUP
			+ " ORDER BY g.GROUPNAME";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			GroupInfo result;
			while (rs.next()) {
				result =  new GroupInfo();
				result.wuID = rs.getLong("WORKUNITID");
				result.name = rs.getString("GROUPNAME");
				result.isOperation = (rs.getInt("ISOPERATIONGROUP")==1);
				result.groupID = rs.getLong("GROUP_ID");
				result.leader = rs.getString("ACCOUNT");
				result.desc= rs.getString("DESCRIPTION");
				result.leaderID = rs.getLong("LEADER");
				resultVector.addElement(result);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
			return resultVector;
		}
	}
	public static long getGroupsIDbyWorkUnitID(final long WorkUnitID) {
			final Vector resultVector = new Vector();
			long groupID = 0;
			Connection conn = null;
			PreparedStatement stm = null;
			String sql = null;
			ResultSet rs=null;
			try {
				sql = "SELECT GROUP_ID FROM workunit wu1, workunit wu2, GROUPS g WHERE wu1.workunitid = wu2.parentworkunitid AND wu1.workunitname = g.groupname AND wu2.workunitid = ?";
				conn = ServerHelper.instance().getConnection();
				stm = conn.prepareStatement(sql);
				stm.setLong(1,WorkUnitID);
				rs = stm.executeQuery();
				if(rs.next()) {
					groupID = rs.getLong("GROUP_ID");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn,stm,rs);
				return groupID;
			}
		}
	public static void updateGroup(GroupInfo inf) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			sql = "UPDATE GROUPS SET DESCRIPTION=?,LEADER=?"
				+ " WHERE GROUP_ID =?";

			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1,inf.desc);
			stm.setLong(2,inf.leaderID);
			stm.setLong(3,inf.groupID);
			stm.executeQuery();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,null);
		}
	}
	/**
	 * returns vector of WorkUnitInfo
	 */
	public static Vector getChildrenWU(final long wuID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT  WORKUNITNAME,TYPE,WORKUNITID,TABLEID FROM WORKUNIT WHERE PARENTWORKUNITID=? ORDER BY WORKUNITNAME";	
			stm = conn.prepareStatement(sql);
			stm.setLong(1,wuID);
			rs = stm.executeQuery();
			while (rs.next()) {
				WorkUnitInfo result = new WorkUnitInfo();
				result = new WorkUnitInfo();
				result.workUnitID = rs.getLong("WORKUNITID");
				result.workUnitName = rs.getString("WORKUNITNAME");
				result.type = rs.getInt("TYPE");
				result.tableID= rs.getLong("TABLEID");
				result.parentWorkUnitID = wuID;
				resultVector.addElement(result);
			}
			return resultVector;
		}
		catch (Exception e) {
			System.err.println("WorkUnit.getChildrenWU error :" + e.toString());
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
		}
	}
	/**
	 * returns vector of WorkUnitInfo
	 */
	public static Vector getWUList(final String byName) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			final String condition;
			if (byName == null) //get full list
				condition = "";
			else {
				//	search by name
				condition = " WHERE UPPER(WORKUNITNAME) LIKE '%" + ConvertString.toSql(byName, ConvertString.adText).toUpperCase() + "%'";
			}
			final String sql = "SELECT * FROM WORKUNIT " + condition + " ORDER BY UPPER(WORKUNITNAME)";
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				final WorkUnitInfo result = new WorkUnitInfo();
				result.workUnitID = rs.getLong("WORKUNITID");
				result.workUnitName = rs.getString("WORKUNITNAME");
				result.type = rs.getInt("TYPE");
				result.protect= (rs.getInt("PROTECTED")==1) ;
				result.parentWorkUnitID = rs.getLong("PARENTWORKUNITID");
				resultVector.addElement(result);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
			return resultVector;
		}
	}
	/**
	 * return vector of group
	 * @param Group name
	 * @return Vector
	 */
	public static Vector getGroupInfo(final String byName) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs =null;
		
		try {
			conn = ServerHelper.instance().getConnection();
			final String condition;
			if (byName == null) //get full list
				condition = "";
			else {
				//search by name
				condition = " WHERE UPPER(GROUPNAME) LIKE '%" + ConvertString.toSql(byName, ConvertString.adText).toUpperCase() + "%'";
			}
			final String sql = "SELECT * FROM GROUPS " + condition + " ORDER BY UPPER(GROUPNAME)";
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				final GroupInfo result = new GroupInfo();				
				result.groupID = rs.getLong("GROUP_ID");
				result.name = rs.getString("GROUPNAME");
				result.isOperation = rs.getBoolean("ISOPERATIONGROUP");				
				resultVector.addElement(result);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
			return resultVector;
		}
	}
	public static final Vector getWUByType(final int type) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs=null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT  * FROM WORKUNIT WHERE TYPE = ? ORDER BY WORKUNITNAME";
			stm = conn.prepareStatement(sql);
			stm.setInt(1,type);
			rs = stm.executeQuery();
			while (rs.next())
				resultVector.addElement(getWorkUnitInfo(rs.getLong("WORKUNITID")));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
			return resultVector;
		}
	}
	public static final Vector getOperationGroupList() {
		final Vector resultVector = new Vector();
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		 ResultSet rs=null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "select groupname FROM groups,workunit" +
            " where groups.group_id=workunit.tableid" +
            " and workunit.type=1 and workunit.parentworkunitid=" +Parameters.FSOFT_WU+
            " ORDER BY groupname";

			rs = stm.executeQuery(sql);
			while (rs.next()) 
				resultVector.addElement(rs.getString("GROUPNAME"));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
			return resultVector;
		}
	}
	// ADD, Update, Delete WorkUnit (Organization, Group, Project) which are create by Hieunv1  - Start 
	/**
	 * perform delete a Organization
	 * @param long id
	 * @param String orgName
	 * @return void
	 */
	public static final void deleteWorkUnitOrg(final long lWorkUnitID, String orgName){
		Connection conn = null;
		PreparedStatement prepStm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);

			sql = "DELETE WORKUNIT WHERE WORKUNITID=?";
			prepStm = conn.prepareStatement(sql);
			prepStm.setLong(1, lWorkUnitID);
			prepStm.executeUpdate();
			prepStm.close();

			sql = "DELETE FROM ORGANIZATION WHERE UPPER(ORGNAME)=?";
			prepStm = conn.prepareStatement(sql);
			prepStm.setString(1, orgName.toUpperCase());
			prepStm.executeUpdate();
			prepStm.close();

			conn.commit();
			conn.setAutoCommit(true);
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally {
			ServerHelper.closeConnection(conn, prepStm, null);
		}
	}
	/**
	 * add a organization which is type of workUnit
	 * @param wuInfo
	 * @return boolean
	 */
	public static final boolean addWorkUnitOrgAction(final OrganizationInfo orgInfo){
		Connection conn = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		String sql = null;
		boolean bResult = true;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);

			sql = "SELECT COUNT(*) WORKUNITID FROM WORKUNIT WHERE UPPER(WORKUNITNAME) = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setString(1, orgInfo.orgName.toUpperCase());
			rs = preStm.executeQuery();
			if (rs.next()){
				if (rs.getLong("WORKUNITID") != 0){
					bResult = false;
				}
			}
			rs.close();

			if (bResult){
				long org_id = ServerHelper.getNextSeq("ORG_SEQ");
				long workunit_id = ServerHelper.getNextSeq("WORKUNIT_SEQ");
				sql =
					"INSERT INTO WORKUNIT (WORKUNITID,WORKUNITNAME,TYPE,PARENTWORKUNITID,TABLEID) "
						+ " VALUES (?,?,?,?,?"
						+ ")";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, workunit_id);
				preStm.setString(2, orgInfo.orgName);
				preStm.setInt(3, 0);
				preStm.setLong(4, 0);
				preStm.setLong(5, org_id);
				preStm.executeUpdate();
				preStm.close();

				sql =
					"INSERT INTO ORGANIZATION (ORG_ID, ORGNAME) "
						+ " VALUES (?,?)";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, org_id);
				preStm.setString(2, orgInfo.orgName);
				preStm.executeUpdate();
				preStm.close();

				conn.commit();
			}

			conn.setAutoCommit(true);			
			return bResult;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, rs);
		}
	}
	/**
	 * Update Organization which is a WorkUnit type
	 * @param wuName
	 * @return true if update successfully, else if update fail then return false;
	 */
	public static final boolean updateWorkUnitOrg(final WorkUnitInfo wuInfo){
		Connection conn = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		String sql = null;
		boolean bResult = true;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql =
				"SELECT TABLEID FROM workunit WHERE UPPER(WORKUNITNAME) = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setString(1, wuInfo.workUnitName.toUpperCase());
			rs = preStm.executeQuery();
			if (rs.next()){
				long lOrgID = rs.getLong("TABLEID");
				// This Group have been existed!
				if (lOrgID != wuInfo.tableID){
					bResult = false;
				}
			}
			rs.close();
			if (bResult){
				sql =
					"UPDATE WORKUNIT SET WORKUNITNAME = ?"
						+ ",TYPE = ?"
						+ ",PARENTWORKUNITID = ?"
						+ " WHERE WORKUNITID = ?"
						+ "";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, wuInfo.workUnitName);
				preStm.setInt(2, wuInfo.type);
				preStm.setLong(3, wuInfo.parentWorkUnitID);
				preStm.setLong(4, wuInfo.workUnitID);
				preStm.executeUpdate();
				preStm.close();

				sql = 
					"UPDATE ORGANIZATION SET ORGNAME = ?"
						+ " WHERE ORG_ID = ?"
						+ "";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, wuInfo.workUnitName);
				preStm.setLong(2, wuInfo.tableID);
				preStm.executeUpdate();
				preStm.close();
	
				conn.commit();
			}
			conn.setAutoCommit(true);
			return bResult;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, rs);
		}
	}
	/**
	 * Add new Group
	 * @param WorkUnitInfo: wuInfo
	 * @param GroupInfo: groupInfo
	 * @return true if add WorkUnitGroup successfully, else add fail return false;
	 */
	public static final boolean addWorkUnitGroup(final WorkUnitInfo wuInfo, GroupInfo groupInfo){
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		ResultSet rs = null;
		boolean bResult = true;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql = "SELECT COUNT(*)WORKUNITID FROM WORKUNIT WHERE UPPER(WORKUNITNAME) = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setString(1, wuInfo.workUnitName.toUpperCase());
			rs = preStm.executeQuery();
			if (rs.next()){
				if (rs.getLong("WORKUNITID") != 0){
					bResult = false;
				}
			}
			rs.close();

			if (bResult){
				long group_id = ServerHelper.getNextSeq("GROUP_SEQ");
				long workunit_id = ServerHelper.getNextSeq("WORKUNIT_SEQ");
				sql =
					"INSERT INTO WORKUNIT (WORKUNITID,WORKUNITNAME,TYPE,PARENTWORKUNITID,TABLEID) "
						+ " VALUES (?, ?, ?, ?, ? )";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, workunit_id);
				preStm.setString(2, wuInfo.workUnitName);
				preStm.setInt(3, wuInfo.type);
				preStm.setLong(4, wuInfo.parentWorkUnitID);
				preStm.setLong(5, group_id);
				preStm.executeUpdate();
				preStm.close();

				sql =
					"INSERT INTO GROUPS (GROUP_ID,GROUPNAME, ISOPERATIONGROUP) "
						+ " VALUES (?, ?, ?)";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, group_id);
				preStm.setString(2, groupInfo.name);
				preStm.setInt(3, groupInfo.isOperation ? 1 : 0);
				preStm.executeUpdate();
				preStm.close();

				conn.commit();
			}

			conn.setAutoCommit(true);
			return bResult;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn,preStm, rs);
		}
	}
	/**
	 * update group infomation
	 * @param wuInfo
	 * @return true if update WorkUnitGroup successfully, else update fail return false;
	 */
	public static final boolean updateWorkUnitGroup(final WorkUnitInfo wuInfo, final GroupInfo groupInfo, final String preGroupName){
		Connection conn = null;
		String sql = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		boolean bResult = true;
		try{
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql =
				"SELECT TABLEID FROM workunit WHERE UPPER(WORKUNITNAME) = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setString(1, wuInfo.workUnitName.toUpperCase());
			rs = preStm.executeQuery();
			if (rs.next()){
				long lGroupID = rs.getLong("TABLEID");
				// This Group have been existed!
				if (lGroupID != groupInfo.groupID){
					bResult = false;
				}
			}
			rs.close();

			if (bResult){
				sql =
					"UPDATE WORKUNIT SET"
						+ " WORKUNITNAME = ?" 
						+ ", TYPE = ?"
						+ ", PARENTWORKUNITID = ?"
						+ " WHERE WORKUNITID = ?";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, wuInfo.workUnitName);
				preStm.setInt(2, wuInfo.type);
				preStm.setLong(3, wuInfo.parentWorkUnitID);
				preStm.setLong(4, wuInfo.workUnitID);
				preStm.executeUpdate();
				preStm.close();

				sql = 
					"UPDATE DEVELOPER SET"
						+ " GROUP_NAME = ?"
						+ " WHERE GROUP_NAME = ?";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, groupInfo.name);
				preStm.setString(2, preGroupName);
				preStm.executeUpdate();
				preStm.close();

				sql = 
					"UPDATE PROJECT SET"
						+ " GROUP_NAME = ?"
						+ " WHERE GROUP_NAME = ?";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, groupInfo.name);
				preStm.setString(2, preGroupName);
				preStm.executeUpdate();
				preStm.close();

				sql =
					"UPDATE USER_PROFILE SET"
						+ " GROUP_NAME = ?"
						+ " WHERE GROUP_NAME = ?";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, groupInfo.name);
				preStm.setString(2, preGroupName);
				preStm.executeUpdate();
				preStm.close();

				sql =
					"UPDATE CALL SET"
						+ " GROUPNAME = ?"
						+ " WHERE GROUPNAME = ?";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, groupInfo.name);
				preStm.setString(2, preGroupName);
				preStm.executeUpdate();
				preStm.close();

				sql =
					"UPDATE METRICGROUP SET"
						+ " GROUPNAME = ?"
						+ " WHERE GROUPNAME = ?";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, groupInfo.name);
				preStm.setString(2, preGroupName);
				preStm.executeUpdate();
				preStm.close();

				sql =
					"UPDATE NC SET"
						+ " GROUPNAME = ?"
						+ " WHERE GROUPNAME = ?";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, groupInfo.name);
				preStm.setString(2, preGroupName);
				preStm.executeUpdate();
				preStm.close();

				sql =
					"UPDATE GROUPS SET"
						+ " GROUPNAME = ?"
						+ ", ISOPERATIONGROUP = ?"
						+ " WHERE GROUP_ID = ?";
				preStm = conn.prepareStatement(sql);
				preStm.setString(1, groupInfo.name);
				preStm.setInt(2, groupInfo.isOperation ? 1 : 0);
				preStm.setLong(3, groupInfo.groupID);
				preStm.executeUpdate();
				preStm.close();

				conn.commit();
			}

			conn.setAutoCommit(true);
			return bResult;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, rs);
		}
	}
	/**
	 * Delete a Group which is a WorkUnit type
	 * @param id
	 * @param groupName
	 */
	public static final boolean doDeleteWorkUnitGroupAction(final long id, final long groupID, final String groupName){
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);

			sql = "DELETE DEVELOPER WHERE GROUP_NAME = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setString(1, groupName);
			preStm.executeUpdate();
			preStm.close();

			sql = "DELETE PROJECT WHERE GROUP_NAME = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setString(1, groupName);
			preStm.executeUpdate();
			preStm.close();
						
			sql = "DELETE CALL WHERE GROUPNAME = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setString(1, groupName);
			preStm.executeUpdate();
			preStm.close();

			sql = "DELETE METRICGROUP WHERE GROUPNAME = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setString(1, groupName);
			preStm.executeUpdate();
			preStm.close();

			sql = "DELETE NC WHERE GROUPNAME = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setString(1, groupName);
			preStm.executeUpdate();
			preStm.close();

			sql = "DELETE GROUPS WHERE GROUP_ID = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, groupID);
			preStm.executeUpdate();
			preStm.close();

			sql = "DELETE WORKUNIT WHERE WORKUNITID = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, id);
			preStm.executeUpdate();
			preStm.close();

			conn.commit();
			conn.setAutoCommit(true);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		finally{
			ServerHelper.closeConnection(conn, preStm, null);
		}
	}
	/**
	 * Add new a project which is a type of WorkUnit
	 * @param WorkUnitInfo: wuInfo
	 * @param ProjectInfo ProjectInfo
	 * @return
	 */
	public static final boolean addWorkUnitProjectAction(WorkUnitInfo wuInfo, ProjectInfo projectInfo, AssignmentInfo assInfo){
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		ResultSet rs = null;
		boolean bResult = true;
		try {
			/*-------------------------------------------------------------
             * 15-Jan-08: removed Oracle connection mechanism because the
             * Db.setCLOB() function still not work on development so we
             * temporary removed to avoid this issue. In the future, should
             * use the way to working with CLOB data like comments on FI
             * Development guideline
             *-------------------------------------------------------------*/
            //conn = Db.getOracleConn();
            conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql = "SELECT COUNT(*) WORKUNITID FROM WORKUNIT WHERE UPPER(WORKUNITNAME) = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setString(1, wuInfo.workUnitName.toUpperCase());
			rs = preStm.executeQuery();
			if (rs.next()){
				if (rs.getLong("WORKUNITID") != 0){
					bResult = false;
				}
			}
			rs.close();

			if (bResult){
				final long lProjectID = ServerHelper.getNextSeq("project_seq");
				final long lWorkUnitID = ServerHelper.getNextSeq("WORKUNIT_SEQ");
				sql =
					"INSERT INTO WORKUNIT (WORKUNITID,WORKUNITNAME,TYPE,PARENTWORKUNITID,TABLEID) "
						+ " VALUES (?, ?, ?, ?, ?)";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, lWorkUnitID);
				preStm.setString(2, wuInfo.workUnitName);
				preStm.setInt(3, wuInfo.type);
				preStm.setLong(4, wuInfo.parentWorkUnitID);
				preStm.setLong(5, lProjectID);
				preStm.executeUpdate();
				preStm.close();

                //--------------------------------------------------------------
                // 11-Jan-08. Update for compatible with RMS:
                // Project status code, Unit id
                //--------------------------------------------------------------
				sql = "INSERT INTO PROJECT (PROJECT_ID, GROUP_NAME, CODE, NAME,"
                    + " CUSTOMER, CUSTOMER_2ND, LEADER, CATEGORY, TYPE, RANK,"
                    + " PLAN_START_DATE, BASE_FINISH_DATE, STATUS,"
                    + " ARCHIVE_STATUS, UNIT_ID, PROJECT_STATUS_CODE, PROJECT_CATEGORY_CODE, PROJECT_TYPE_CODE) " 
				    + " VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?, ?, ? )";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, lProjectID);
				preStm.setString(2, projectInfo.getGroupName());
				preStm.setString(3, projectInfo.getProjectCode());
				preStm.setString(4, projectInfo.getProjectName());
				preStm.setString(5, projectInfo.getCustomer());
				preStm.setString(6, projectInfo.getSecondCustomer());
				preStm.setString(7, projectInfo.getLeader());
				preStm.setString(8, projectInfo.getLifecycle());
				preStm.setString(9, projectInfo.getProjectType());
				preStm.setString(10, projectInfo.getProjectRank());
				preStm.setDate(11, projectInfo.getPlanStartDate());
				preStm.setDate(12, projectInfo.getBaseFinishDate());
				preStm.setInt(13, projectInfo.getStatus());
                //--------------------------------------------------------------
                // Update for compatible with RMS: Project status code,
                // Unit Id (please note that Unit Id comes from RMS_UNIT.unit_id
                // , not comes from WorkUnit.WorkUnitId of old FI.)
                //--------------------------------------------------------------
                preStm.setInt(14, com.fms1.integrate.Unit.getUnitIdByGroupName(
                        projectInfo.getGroupName()));
                preStm.setString(15,
                    com.fms1.integrate.Project.getProjectStatusCodeById(
                        projectInfo.getStatus()));
                //--------------------------------------------------------------
                // END: Update for compatible with RMS: Project status code, Unit Id
                //--------------------------------------------------------------
				//Lamnt3 add for RMS
				preStm.setString(16, ProjectInfo.parseLifecycle(projectInfo.getLifecycle()).toUpperCase());
				preStm.setString(17, ProjectInfo.parseType(projectInfo.getProjectType()).toUpperCase());
				preStm.executeUpdate();
				preStm.close();

				final long project_Plan_Id = ServerHelper.getNextSeq("PROJECT_PLAN_SEQ");
				sql =
					"INSERT INTO PROJECT_PLAN ("
						+ "  PROJECT_PLAN_ID"
						+ ", PROJECT_ID"
						+ ", LAST_UPDATE, SCOPE_OBJECTIVE, DIRECTOR )"
						+ "  VALUES (?, ?, SYSDATE, EMPTY_CLOB(), ?)";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, project_Plan_Id);
				preStm.setLong(2, lProjectID);
				// Removed on 15-jan-08 to avoid CLOB problem
                //Db.setCLOB(preStm, 3, projectInfo.getScopeAndObjective());
                preStm.setString(3, projectInfo.getLeader());
				preStm.executeUpdate();
				preStm.close();

				WorkOrderCaller.addChangeAuto(assInfo.projectID, Constants.ACTION_ADD, Constants.WO_TEAM, assInfo.devName, null, null);
				final long assignment_id = ServerHelper.getNextSeq("ASSIGNMENT_SEQ");
				sql =
					"INSERT INTO ASSIGNMENT (ASSIGNMENT_ID, PROJECT_ID, DEVELOPER_ID, "
						+ " TYPE, BEGIN_DATE, END_DATE, RESPONSE, USAGE,"
                //--------------------------------------------------------------
                // Update for compatible with RMS: Project position code
                //--------------------------------------------------------------
                        + " PROJECT_POSITION_CODE) "
                //--------------------------------------------------------------
                // END: Update for compatible with RMS: Project position code
                //--------------------------------------------------------------
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, assignment_id);
				preStm.setLong(2, lProjectID);
				preStm.setLong(3, assInfo.devID);
				preStm.setInt(4, assInfo.type);
				preStm.setDate(5, assInfo.beginDate);
				preStm.setDate(6, assInfo.endDate);
				preStm.setLong(7, assInfo.responsibilityID);
				preStm.setDouble(8, assInfo.workingTime);
                //--------------------------------------------------------------
                // Update for compatible with RMS: Project position code
                //--------------------------------------------------------------
                String positionCode =
                    com.fms1.integrate.Project.getProjectPositionCodeById(
                        assInfo.responsibilityID);
                preStm.setString(9, positionCode);
                //--------------------------------------------------------------
                // END: Update for compatible with RMS: Project position code
                //--------------------------------------------------------------
				preStm.executeUpdate();
				preStm.close();
				
				sql = 
					"INSERT INTO RIGHTGROUPOFUSERBYWORKUNIT (DEVELOPERID, WORKUNITID, RIGHTGROUPID)"
					+ " VALUES ( ?, ?, ?)";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, assInfo.devID);
				preStm.setLong(2, lWorkUnitID);
				preStm.setString(3, "PD");
				preStm.executeUpdate();
				preStm.close();
			
				sql = 
					"INSERT INTO DEFECT_PERMISSION (PROJECT_ID, DEVELOPER_ID, STATUS, POSITION)"
					+ " VALUES (?, ?, ?, ?)";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, lProjectID);
				preStm.setLong(2, assInfo.devID);
				preStm.setString(3, "0");
				preStm.setString(4, CommonTools.getDefectPermission(assInfo.responsibilityID));
				preStm.executeUpdate();
				preStm.close();

				conn.commit();

                // Modified on 15-jan-08 to avoid CLOB problem
                Db.writeClob("PROJECT_PLAN", "SCOPE_OBJECTIVE",
                        "PROJECT_PLAN_ID", Long.toString(project_Plan_Id),
                        projectInfo.getScopeAndObjective());

                //--------------------------------------------------------------
                // Update for compatible with RMS: User role
                //--------------------------------------------------------------
                // This is new project just generated so should set it because
                // the following code needed this property.                
                assInfo.projectID = lProjectID;
                bResult = com.fms1.integrate.Project.updateRoleByAddAssignment(
                                assInfo);
                if (!bResult) {
                    conn.rollback();
                }
                //--------------------------------------------------------------
                // END: Update for compatible with RMS: User role
                //--------------------------------------------------------------
			}

			conn.setAutoCommit(true);
		}
		catch (Exception e) {
            bResult = false;
			e.printStackTrace();
			try {
				conn.rollback();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, rs);
            return bResult;
		}
	}
	/**
	 * Delete a Project
	 * @param workUnitId
	 * @param projectId
	 * @return void
	 */
	public static final boolean doDeleteWorkUnitProjectAction(final long workUnitId, final long projectId) throws SQLException{
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);

			sql = "DELETE rms_user_role WHERE PROJECT_ID = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, projectId);
			preStm.executeUpdate();
			preStm.close();

			sql = "DELETE WORKUNIT WHERE WORKUNITID = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, workUnitId);
			preStm.executeUpdate();
			preStm.close();

			sql = "DELETE PROJECT WHERE PROJECT_ID = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, projectId);
			preStm.executeUpdate();
			preStm.close();

			conn.commit();
			conn.setAutoCommit(true);
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, null);
		}
	}

	/**
	 * Get WorkUnit by ProjectID
	 * @param projectId
	 * @return workUnitId
	 */
	public static final long getWorkUnitByProjectId(final long projectId) throws SQLException {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs=null;
		long workUnitId = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT WORKUNITID FROM WORKUNIT WHERE TYPE = 2 AND TABLEID = ?" ;
			stm = conn.prepareStatement(sql);
			stm.setLong(1,projectId);
			rs = stm.executeQuery();
			if (rs.next()){
				workUnitId = rs.getLong("WORKUNITID");
			}
			return workUnitId;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
		}
	}
	/**
	 * Get all Work Unit
	 * @return Vector
	 */
	public static Vector getAllWU(){
		final Vector resultVector = new Vector();
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		try{
			conn = ServerHelper.instance().getConnection();

			sql = "SELECT * FROM WORKUNIT ORDER BY UPPER(WORKUNITNAME)";
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			WorkUnitInfo result;
			while (rs.next()) {
				result = new WorkUnitInfo();
				result.workUnitID = rs.getLong("WORKUNITID");
				result.workUnitName = rs.getString("WORKUNITNAME");
				result.type = rs.getInt("TYPE");
				result.protect= (rs.getInt("PROTECTED")==1) ;
				result.parentWorkUnitID = rs.getLong("PARENTWORKUNITID");
				resultVector.addElement(result);
			}
			return resultVector;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	/**
	 * Get all WorkUnit by workUnit type
	 * @param byType
	 * @return Vector
	 */
	public static Vector getWUListByType(final int byType){
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		ResultSet rs = null;
		try{
			conn = ServerHelper.instance().getConnection();
			String condition = "";
			if (byType == -1) {
				condition = "";
			} else{
				condition = " WHERE TYPE =?";
			}
			sql = "SELECT * FROM WORKUNIT " + condition + " ORDER BY UPPER(WORKUNITNAME)";
			preStm = conn.prepareStatement(sql);
			preStm.setInt(1, byType);
			rs = preStm.executeQuery();
			while (rs.next()) {
				final WorkUnitInfo result = new WorkUnitInfo();
				result.workUnitID = rs.getLong("WORKUNITID");
				result.workUnitName = rs.getString("WORKUNITNAME");
				result.type = rs.getInt("TYPE");
				result.protect= (rs.getInt("PROTECTED")==1) ;
				result.parentWorkUnitID = rs.getLong("PARENTWORKUNITID");
				resultVector.addElement(result);
			}
			return resultVector;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, rs);
		}
	}

	/**
	 * get all WorkUnit byte workUnit name and workUnit type (Organization, group, project, Admin)
	 * @param searchType
	 * @param searchName
	 * @return Vector
	 */
	public static Vector getWUList(final int searchType, final String searchName) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			String condition = "";
			if (searchType == -1) {
				condition = " WHERE UPPER(WORKUNITNAME) LIKE '%" 
							+ ConvertString.toSql(searchName.trim().toUpperCase(), ConvertString.adText) + "%'";
			}
			else {
				condition = " WHERE UPPER(WORKUNITNAME) LIKE '%" 
				+ ConvertString.toSql(searchName.trim().toUpperCase(), ConvertString.adText) + "%' AND TYPE =" + searchType;
			}
			sql = "SELECT * FROM WORKUNIT " + condition + " ORDER BY UPPER(WORKUNITNAME)";
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				final WorkUnitInfo result = new WorkUnitInfo();
				result.workUnitID = rs.getLong("WORKUNITID");
				result.workUnitName = rs.getString("WORKUNITNAME");
				result.type = rs.getInt("TYPE");
				result.protect= (rs.getInt("PROTECTED")==1) ;
				result.parentWorkUnitID = rs.getLong("PARENTWORKUNITID");
				resultVector.addElement(result);
			}
			return resultVector;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	/**
	 * check: this Group has any users belong to
	 * @param strGroupName
	 * @return false if has uses (do not allow delete this group), else return true(You can delete this group)
	 */
	public static final boolean getUserBelongGroupAssignments(String strGroupName){
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		ResultSet rs = null;
		try{
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT count(D.DEVELOPER_ID) as Count "
				+ " FROM DEVELOPER D, ASSIGNMENT A"
				+ " WHERE D.DEVELOPER_ID = A.DEVELOPER_ID"
				+ " AND D.GROUP_NAME =?";			
			preStm = conn.prepareStatement(sql);
			preStm.setString(1, strGroupName);
			rs = preStm.executeQuery();
			if (rs.next()){
				if (rs.getInt("Count") != 0){
					return false;
				}
			}
			return true;
		}
		catch (SQLException ex){
			ex.printStackTrace();
			return false;
		}
		finally{
			ServerHelper.closeConnection(conn, preStm, rs);
		}
	}
	// ADD, Update, Delete WorkUnit (Organization, Group, Project) which are create by Hieunv1  - End
	/**
	 * Get WorkUnit by Name
	 * param:name of work unit
	 * result WorkUnitInfor
	 */
	public static final WorkUnitInfo getWorkUnitInfo(final String wuName) {
		final WorkUnitInfo result = new WorkUnitInfo();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs=null;
		try {
			conn = ServerHelper.instance().getConnection();
			
			sql = "SELECT * FROM WORKUNIT WHERE UPPER(WORKUNITNAME) =?";
			stm = conn.prepareStatement(sql);
			stm.setString(1,wuName.toUpperCase());
			rs = stm.executeQuery();
			if (!rs.next()){
				return null;
			}
			result.workUnitID = rs.getLong("WORKUNITID");
			result.workUnitName = rs.getString("WORKUNITNAME");
			result.type = rs.getInt("TYPE");
			result.parentWorkUnitID = rs.getLong("PARENTWORKUNITID");
			result.protect= (rs.getInt("PROTECTED")==1) ;
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
		}
	}
	public static final int getNumOperationGroup() {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;

		int numGroup = 1;

		try {
			sql = "SELECT COUNT(WORKUNITNAME) numGroup FROM WORKUNIT, GROUPS WHERE TYPE = 1 AND PARENTWORKUNITID = "+Parameters.FSOFT_WU+" AND WORKUNIT.TABLEID=GROUPS.GROUP_ID AND GROUPS.ISOPERATIONGROUP = 1";
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);

			if (rs.next())
				numGroup = rs.getInt(1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return numGroup;
		}
	}
	
	public static final int getNumSupportGroup() {
		Connection conn = null;
		PreparedStatement stm = null;
//		Statement stm = null;
		String sql = null;
		ResultSet rs = null;

		int numGroup = 1;

		try {
			sql = "SELECT COUNT(WORKUNITNAME) numGroup FROM WORKUNIT, GROUPS WHERE TYPE = 1 AND PARENTWORKUNITID = "+Parameters.FSOFT_WU+" AND WORKUNIT.TABLEID=GROUPS.GROUP_ID AND GROUPS.ISOPERATIONGROUP IS NULL";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
//			stm = conn.createStatement();
			rs = stm.executeQuery(sql);

			if (rs.next())
				numGroup = rs.getInt(1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return numGroup;
		}
	}

	public static final Vector getProjects(final long workUnitID, final String beginMonth, final String endMonth, final String strWhere) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		Vector vt =new Vector();
		try {

		sql =
			"SELECT a.PARENTWORKUNITID p1,b.PARENTWORKUNITID p2,PROJECT.PROJECT_ID, PROJECT.CODE, PROJECT.GROUP_NAME, a.WORKUNITID"
				+ " FROM WORKUNIT a,WORKUNIT b, PROJECT"
				+ " WHERE a.type(+)= 2 AND "
				+ " PROJECT.PROJECT_ID = a.TABLEID (+) "
				+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
				+ " AND (a.PARENTWORKUNITID=" + workUnitID + " OR b.PARENTWORKUNITID=" + workUnitID + ")"
				+ " AND (PROJECT.ACTUAL_FINISH_DATE IS NULL OR PROJECT.ACTUAL_FINISH_DATE > TO_DATE('" + beginMonth + "', 'MM/DD/YYYY')) "
				+ " AND PROJECT.START_DATE < TO_DATE('" + endMonth + "', 'MM/DD/YYYY') "
				+ " AND PROJECT.STATUS <> "+ProjectInfo.STATUS_CANCELLED+" AND PROJECT.STATUS <> "+ProjectInfo.STATUS_TENTATIVE+" "
				+ strWhere

				+ " UNION "

				+ " SELECT a.PARENTWORKUNITID p1,b.PARENTWORKUNITID p2, PROJECT.PROJECT_ID, PROJECT.CODE, PROJECT.GROUP_NAME, a.WORKUNITID"
				+ " FROM WORKUNIT a,WORKUNIT b, PROJECT"
				+ " WHERE a.type(+)= 2 AND "
				+ " PROJECT.PROJECT_ID = a.TABLEID (+) "
				+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
				+ " AND (a.PARENTWORKUNITID=" + workUnitID + " OR b.PARENTWORKUNITID=" + workUnitID + ")"
				+ " AND PROJECT.ACTUAL_FINISH_DATE >=TO_DATE('" + beginMonth + "', 'MM/DD/YYYY')"
				+ " AND PROJECT.ACTUAL_FINISH_DATE <=TO_DATE('" + endMonth + "', 'MM/DD/YYYY') "
				+ " AND PROJECT.STATUS <> "+ProjectInfo.STATUS_CANCELLED+" AND PROJECT.STATUS <> "+ProjectInfo.STATUS_TENTATIVE+" "
				+ strWhere;

			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);

			rs = stm.executeQuery();

			while (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				// projectInfo.project_id = rs.getLong(3);
				projectInfo.setProjectId(rs.getLong(3));
				// projectInfo.code = rs.getString(4);
				projectInfo.setProjectCode(rs.getString(4));
				projectInfo.setWorkUnitId(rs.getLong(6));

				vt.add(projectInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return vt;
		}
	}
	public static final Vector getClosedProjects(final long workUnitID, final java.sql.Date beginMonth, final java.sql.Date endMonth, final String strWhere) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		Vector vt = new Vector();
		try {
            sql = "SELECT a.PARENTWORKUNITID p1,b.PARENTWORKUNITID p2, PROJECT.PROJECT_ID PROJECT_ID, PROJECT.ACTUAL_FINISH_DATE ACTUAL_FINISH_DATE"
				+ " FROM WORKUNIT a,WORKUNIT b, PROJECT, PROJECT_PLAN"
				+ " WHERE a.type(+)= 2 AND "
				+ " PROJECT.PROJECT_ID = a.TABLEID (+) "
				+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
				+ " AND (a.PARENTWORKUNITID=? OR b.PARENTWORKUNITID=?)"
				+ " AND PROJECT_PLAN.PROJECT_ID (+)= PROJECT.PROJECT_ID"
				+ " AND PROJECT.ACTUAL_FINISH_DATE >=?"
				+ " AND PROJECT.ACTUAL_FINISH_DATE <=?"
				+ strWhere
				+ " ORDER BY PROJECT.CODE";
            conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, workUnitID);
			stm.setLong(2, workUnitID);
			stm.setDate(3, beginMonth);
			stm.setDate(4, endMonth);

			rs = stm.executeQuery();

			while (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.setProjectId(rs.getLong("PROJECT_ID"));
				projectInfo.setActualFinishDate(rs.getDate("ACTUAL_FINISH_DATE"));
				vt.add(projectInfo);
			}
            return vt;
		} catch (Exception e) {
			e.printStackTrace();
            return null;
		} finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
    /**
     * Get group type (Operation(1), support(0) group, OR others(-1)) 
     * @param wuInfo
     * @return
     */
    public static final int getGroupType(final WorkUnitInfo wuInfo) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        int n_Result = -1;
        
        try {
            // If this is a group then return group type (isoperationgroup), else return other (-1)
            if (wuInfo.type == WorkUnitInfo.TYPE_GROUP) {
                sql =   "SELECT isoperationgroup"
                        + " FROM WORKUNIT wu, Groups g"
                        + " WHERE wu.tableid=g.group_id AND wu.tableid=?";
                conn = ServerHelper.instance().getConnection();
                stm = conn.prepareStatement(sql);
                stm.setLong(1, wuInfo.tableID);
                rs = stm.executeQuery();
                if (rs.next()) {
                    n_Result = rs.getInt("isoperationgroup");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return n_Result;
        }
    }
    public static final void workUnitHome(final HttpServletRequest request, final HttpServletResponse response, final WorkUnitInfo wuInfo) {
		final HttpSession session = request.getSession();
		try {

            //HuyNH2 add for project archive remove session
            session.removeAttribute("archiveStatus");
            
			/**
			 * points
			 **/
			ReportMonth actualMonth = new ReportMonth();
			actualMonth.moveToPreviousMonth();

			int curYear ;
			int curMonth ;

			String curY = (String) request.getParameter("selectYear");
			if (curY != null){
				curYear = CommonTools.parseInt(curY);
				actualMonth.setYear(curYear);
			}
			else
				curYear = actualMonth.getYear();
				
			String curM = (String) request.getParameter("selectMonth");
			if (curM != null){
				curMonth = CommonTools.parseInt(curM);
				actualMonth.setMonth(curMonth);
			}
			else
				curMonth = actualMonth.getMonth();
				
			ReportMonth prevMonth = actualMonth.getPreviousMonth();
			int lastMonth =prevMonth.getMonth();
			int curYear2 = prevMonth.getYear();
			int numDev;
			int numProj;
			double dbEff;
			double dbCalendarEff;
			double dbBusyRate;
			int numGroup = 0, numGroupBA = 0;
            GroupPointInfo gpointInfo = null;
            GroupPointBAInfo gpointBAInfo = null;

			switch (wuInfo.type) {
				case WorkUnitInfo.TYPE_ADMIN :
					break;
				case WorkUnitInfo.TYPE_PROJECT :
					session.removeAttribute("allClosedTasks");
					session.removeAttribute("nextWeekTasks");
					long stageID = 0;
					long projectID = 0;
					projectID = wuInfo.tableID;
                    //Huynh2 add for project archive - only set here
                    boolean archiveStatus = com.fms1.tools.Db.checkProjecIsArchive(projectID);
                    if(archiveStatus){            
                        session.setAttribute("archiveStatus","4");
                    }
                    else{    
                    
                        session.setAttribute("archiveStatus","0");                
                    }
                    
					final Vector stageList = Schedule.getStageList(projectID);
					if ((stageList.size() == 0) || (stageList == null)) { // No stage is defined
						ProjectPointInfo ppointInfo = new ProjectPointInfo();
						ppointInfo.prjID = projectID;
						session.removeAttribute("metricInfoAtWelcomePage");
						session.setAttribute("projectPoint1", ppointInfo);
						session.setAttribute("numStage", "0");
						break;
					}
					else {
						StageInfo stageInfo = null;
						int i, j;
						j = 0;
						for (i = stageList.size() - 1; i >= 0; i--) {
							stageInfo = (StageInfo) stageList.elementAt(i);
							if ((stageInfo.aEndD != null)
								&& (stageInfo.actualBeginDate != null)
								&& (stageInfo.plannedEndDate != null)
								&& (stageInfo.plannedBeginDate != null)) {
								stageID = stageInfo.milestoneID;
								j = j + 1;
								if (j == 1) {
									ProjectPointInfo ppointInfo = Report.getProjectPoint(projectID, stageID);
									ppointInfo.stageName = stageInfo.stage;
									session.setAttribute("projectPoint1", ppointInfo);
									
									Vector cusMetric =  Metrics.getCusMetric(stageID);
									session.setAttribute("cusMetricStage1", cusMetric);
								}
								if (j == 2) {
									ProjectPointInfo ppointInfo = Report.getProjectPoint(projectID, stageID);
									ppointInfo.stageName = stageInfo.stage;
									session.setAttribute("projectPoint2", ppointInfo);
									
									Vector cusMetric =  Metrics.getCusMetric(stageID);
									session.setAttribute("cusMetricStage2", cusMetric);
									break;
								}
							}
						}
						if (j == 0) {
							ProjectPointInfo ppointInfo = new ProjectPointInfo();
							ppointInfo.prjID = projectID;
							session.setAttribute("projectPoint1", ppointInfo);
							session.setAttribute("numStage", "0");
						}
						session.setAttribute("numStage", String.valueOf(j));
					}
					// ProjectInfo projectInfo = new ProjectInfo(projectID);
					ProjectInfo projectInfo = Project.getProjectInfo(projectID);
					
					Date now = new Date();
					java.sql.Date sqlReportDate = new java.sql.Date(now.getTime()); // great than actual close date

					Vector requirementList = Requirement.getRequirementList(projectInfo);
					double completeness=Requirement.getActualRCRByDateFinal(projectInfo,requirementList,sqlReportDate);
                    //HuyNH2 add code for Project Archive
                    //if project archive - current date > project closed date
                    //actualEffort = acctualEffort
                    //if project archive actualEffort will be get from proejct_archive table. 
                    double actualEffort;             
                    //System.out.println("WorkUnit projectInfo.archive_status = "+projectInfo.archive_status);      
                    //System.out.println("WorkUnit projectInfo.project_id  = " + projectInfo.project_id); 
                    if(projectInfo.getArchiveStatus()==4){
                        //projec is archive 
                        actualEffort = Effort.getArchiveActualEffort(projectInfo.getProjectId());
                        //System.out.println("project are archived - WorkUnit, call this function - line code is 859, actualEffort = "+actualEffort);
                    }else{
                        actualEffort = Effort.getActualEffort(projectInfo.getProjectId(),null,sqlReportDate);
                        //System.out.println("project is not archived, call realtime - line code 862, WorkUnit Class, actualEffort = "+actualEffort);
                    }
                    //end Huynh2 add code                    
					ReportMetricInfo metricInfo1=new ReportMetricInfo();
					Vector metricList =new Vector();
					metricInfo1.name="ETC: Estimated effort to complete";
					metricInfo1.unit="person.day";
					metricInfo1.spent=actualEffort;
					if (completeness!=0){
						metricInfo1.remain=actualEffort*(100d-completeness)/completeness;
						metricInfo1.estimated= metricInfo1.spent+metricInfo1.remain;
					}

					metricInfo1.estimated = Double.isNaN(projectInfo.getPlanEffort())
                                            ? projectInfo.getBaseEffort()
                                            : projectInfo.getPlanEffort();
					
					metricList.add(metricInfo1);

					ReportMetricInfo metricInfo2 = new ReportMetricInfo();

					metricInfo2.name="ETC: Estimated date to complete";
					metricInfo2.unit="DD-MMM-YY";
					metricInfo2.estimated=projectInfo.getPlannedFinishDate().getTime();
					metricInfo2.spent=sqlReportDate.getTime();
					if (completeness>0){
						double spentRequirements =completeness;
						double remainRequirements =100d-spentRequirements;
						double elapsedDays =CommonTools.dateDiff(projectInfo.getStartDate(),sqlReportDate);
						double remainingDays=elapsedDays * remainRequirements / spentRequirements;
						if (projectInfo.getStatus()==ProjectInfo.STATUS_ONGOING || projectInfo.getStatus()==ProjectInfo.STATUS_TENTATIVE){
							metricInfo2.remain=sqlReportDate.getTime() + remainingDays * 24d * 3600d * 1000d;
						}
						else{
							if (projectInfo.getActualFinishDate() != null){
								metricInfo2.remain = projectInfo.getActualFinishDate().getTime();
							}
						}
					}
					metricList.add(metricInfo2);

					session.setAttribute("metricInfoAtWelcomePage", metricList);

					java.sql.Date nextSunday= new java.sql.Date(sqlReportDate.getTime() +6*24*3600*1000 -1);

					Vector allClosedTasks = Tasks.getClosedProjectTasks(projectID,projectInfo.getStartDate(),nextSunday);
					Vector nextWeekTasks = Tasks.getOpenProjectTasks(projectID,nextSunday);
					session.setAttribute("allClosedTasks", allClosedTasks);
					session.setAttribute("nextWeekTasks", nextWeekTasks);
					break;
				case WorkUnitInfo.TYPE_GROUP :
					final Vector vt = new Vector();
                    wuInfo.groupType = getGroupType(wuInfo);
                    
					session.setAttribute("lastMonth1", CommonTools.getMonth(curMonth) + "-" + String.valueOf(curYear));
                    session.setAttribute("lastMonth2", CommonTools.getMonth(lastMonth) + "-" + String.valueOf(curYear2));
					if (wuInfo.groupType == WorkUnitInfo.GROUP_OPERATION) {
                        gpointInfo = Report.getGroupPoint(curMonth, wuInfo.workUnitID, curYear);
                        if (gpointInfo != null) {
                            vt.addElement(gpointInfo);
                        }

                        gpointInfo = Report.getGroupPoint(lastMonth, wuInfo.workUnitID, curYear2);
                        if (gpointInfo != null) {
                            vt.addElement(gpointInfo);
                        }
                    }
                    else {
                        gpointBAInfo = Report.getGroupPointBA(curMonth, wuInfo.workUnitID, curYear);
                        if (gpointBAInfo != null) {
                            vt.addElement(gpointBAInfo);
                        }

                        gpointBAInfo = Report.getGroupPointBA(lastMonth, wuInfo.workUnitID, curYear2);
                        if (gpointBAInfo != null) {
                            vt.addElement(gpointBAInfo);
                        }
                    }
                    
					numDev = Report.getNumDeveloper(wuInfo.workUnitName);
					session.setAttribute("numDev", String.valueOf(numDev));

					numProj = WorkUnit.getRunningProjects(wuInfo.workUnitID, actualMonth.getFirstDayOfMonth(), actualMonth.getLastDayOfMonth()).length;
					session.setAttribute("numProj", String.valueOf(numProj));

					dbEff = Effort.getEffortByGroup(wuInfo.workUnitName, curMonth, curYear);
					dbCalendarEff = Effort.getCalendarEffort(wuInfo.workUnitName);
					dbBusyRate = (dbEff * 100) / (dbCalendarEff);
					
					if (dbCalendarEff < 0) dbBusyRate = 0;
					
					numGroup = getNumOperationGroup();
					numGroupBA = getNumSupportGroup();

					session.setAttribute("numBAGroup", String.valueOf(numGroupBA));
					session.setAttribute("numOperationGroup", String.valueOf(numGroup));
					session.setAttribute("busyRate", CommonTools.formatDouble(dbBusyRate));
					session.setAttribute("curMonth", String.valueOf(curMonth));
					session.setAttribute("curYear", String.valueOf(curYear));
					session.setAttribute("groupPoint", vt);
					session.setAttribute("workUnit", wuInfo);

					break;
				case WorkUnitInfo.TYPE_ORGANIZATION :
					final Vector vtOrg = new Vector();

					session.setAttribute("lastMonth1", CommonTools.getMonth(curMonth) + "-" + String.valueOf(curYear));
					gpointInfo = Report.getGroupPoint(curMonth, wuInfo.workUnitID, curYear);
					if (gpointInfo != null) {
						vtOrg.addElement(gpointInfo);
					}

					session.setAttribute("lastMonth2", CommonTools.getMonth(lastMonth) + "-" + String.valueOf(curYear2));
                    gpointInfo = Report.getGroupPoint(lastMonth, wuInfo.workUnitID, curYear2);
					if (gpointInfo != null) {
						vtOrg.addElement(gpointInfo);
					}

					numDev = UserHelper.getNumDeveloper();
					session.setAttribute("numDev", String.valueOf(numDev));

					numProj = WorkUnit.getRunningProjects( actualMonth.getFirstDayOfMonth(), actualMonth.getLastDayOfMonth()).length;
					session.setAttribute("numProj", String.valueOf(numProj));

					dbEff = Effort.getEffortByGroup(curMonth, curYear);
					dbCalendarEff = Effort.getCalendarEffort();
					dbBusyRate = (dbEff * 100) / (dbCalendarEff);

					numGroup = getNumOperationGroup();

					session.setAttribute("numOperationGroup", String.valueOf(numGroup));
					session.setAttribute("busyRate", CommonTools.formatDouble(dbBusyRate));
					session.setAttribute("curMonth", String.valueOf(curMonth));
					session.setAttribute("curYear", String.valueOf(curYear));
					session.setAttribute("groupPoint", vtOrg);
					session.setAttribute("workUnit", wuInfo);

					break;
			}
			/**
			 * End points
			 **/
			//if the user has selected a project, should we display menus ? synchronize with code in  WorkOrderCaller.doUpdatePerformanceList
			String disableMnu = "";
			if (wuInfo.type == WorkUnitInfo.TYPE_PROJECT && !Project.isWOConsistent(wuInfo.tableID) ){
				disableMnu = "?mnuDisable=1";
			}
			Fms1Servlet.callPage("welcomePage.jsp" + disableMnu, request, response);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static long [] getRunningProjects(final java.sql.Date startDate, final java.sql.Date endDate){
		return  getRunningProjects(0, startDate, endDate);
	}
	public static long [] getRunningProjects(long parentWU,final java.sql.Date startDate, final java.sql.Date endDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs=null;
		long [] result=null;
		try {
			boolean byGroup=(parentWU!=0);
			conn = ServerHelper.instance().getConnection();
			sql = "SELECT PROJECT.PROJECT_ID FROM PROJECT,WORKUNIT"
					+" WHERE PROJECT.START_DATE <= ? "
					+" AND (PROJECT.ACTUAL_FINISH_DATE IS NULL OR PROJECT.ACTUAL_FINISH_DATE >=?) "
					+" AND PROJECT.type<>'9' " //misc project
					+" AND PROJECT.STATUS<>"+ProjectInfo.STATUS_CANCELLED +" AND PROJECT.STATUS<>"+ProjectInfo.STATUS_TENTATIVE
					+" AND NOT( PROJECT.type='8' AND SUBSTR(Project.name,0,5)='Daily') "
					+" AND WORKUNIT.TABLEID= PROJECT.PROJECT_ID AND WORKUNIT.TYPE="+WorkUnitInfo.TYPE_PROJECT;
			if (byGroup)
				sql=sql+" AND WORKUNIT.PARENTWORKUNITID= ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setDate(1, endDate);
			prepStmt.setDate(2, startDate);
			if (byGroup)
				prepStmt.setLong(3, parentWU);
			rs = prepStmt.executeQuery();
			Vector temp=new Vector();
			while (rs.next())
				temp.add(new Long(rs.getLong("PROJECT_ID")));
			result=new long[temp.size()];
			for (int i=0;i<temp.size();i++)
				result[i]=((Long)temp.elementAt(i)).longValue();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return result;
		}
	}
}