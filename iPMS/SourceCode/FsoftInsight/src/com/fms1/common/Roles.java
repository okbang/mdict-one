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
import com.fms1.infoclass.RolesInfo;
import com.fms1.infoclass.WorkUnitInfo;
import com.fms1.tools.*;
import com.fms1.web.*;
import com.fms1.infoclass.UserInfo;
/**
 * User rights management
 *
 */
public final class Roles {
	// rename this function from addRightOfUserByWU to addRightOfUserByWorkUnit
	public static Vector getChildrenRights(final RolesInfo parentInfo) {
		Vector childrenRights = new Vector();
		Vector childrenWU = WorkUnit.getChildrenWU(parentInfo.workUnitID);
		for (int i = 0; i < childrenWU.size(); i++) {
			RolesInfo tempInfo = (RolesInfo) parentInfo.clone();
			WorkUnitInfo tempWU = (WorkUnitInfo) childrenWU.elementAt(i);
			tempInfo.workUnitID = tempWU.workUnitID;
			tempInfo.workunitName = tempWU.workUnitName;
			tempInfo.workunitType = tempWU.type;
			tempInfo.tableID=tempWU.tableID;
			childrenRights.add(tempInfo);
		}
		return childrenRights;
	}
	public static boolean addRightOfUserByWorkUnit(final RolesInfo newInfo) throws SQLException {
		Connection conn = null;
		PreparedStatement preStm = null;
		String sql = null;
		ResultSet rs=null;
		try {
			conn = ServerHelper.instance().getConnection();
			// final ConvertString cs = new ConvertString();
			sql = 
				"SELECT * FROM RIGHTGROUPOFUSERBYWORKUNIT WHERE DEVELOPERID=? AND WORKUNITID=?";
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, newInfo.developerID);
			preStm.setLong(2, newInfo.workUnitID);
			rs = preStm.executeQuery();
			if ((rs != null) && (rs.next())) {
				return false;
			}

			sql =
				"INSERT INTO RIGHTGROUPOFUSERBYWORKUNIT (DEVELOPERID, WORKUNITID, RIGHTGROUPID) VALUES (?, ?, ?)";
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, newInfo.developerID);
			preStm.setLong(2, newInfo.workUnitID);
			// preStm.setString(3, cs.toSql(newInfo.rightGroupID.trim(), cs.adText));
            preStm.setString(3, ConvertString.toSql(newInfo.rightGroupID.trim(), ConvertString.adText));
			if (preStm.executeUpdate() == 0){
				return false;
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, null);
		}
	}
	public static boolean deleteAllRightOfUser(final long developerID) {
        return Db.delete(developerID,"DEVELOPERID","RIGHTGROUPOFUSERBYWORKUNIT");
	}
	public static Vector getRightOfUser(final long developerID) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT A.RIGHTGROUPID, A.WORKUNITID, B.TYPE, B.WORKUNITNAME,B.TABLEID"
					+ " FROM RIGHTGROUPOFUSERBYWORKUNIT A, WORKUNIT B WHERE DEVELOPERID = ?"
				+ " AND B.WORKUNITID = A.WORKUNITID ORDER BY B.WORKUNITNAME";

			stm = conn.prepareStatement(sql);
			stm.setLong(1,developerID);
			rs = stm.executeQuery();

			while (rs.next()) {
				RolesInfo info = new RolesInfo();
				info.developerID = developerID;
				info.workUnitID = rs.getLong("WORKUNITID");
				info.rightGroupID = rs.getString("RIGHTGROUPID");
				info.workunitName = rs.getString("WORKUNITNAME");
				info.workunitType = rs.getInt("TYPE");
				info.tableID = rs.getLong("TABLEID");
				resultVector.addElement(info);
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
	public static boolean isAdminOfFsoft(final long developerID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT A.RIGHTGROUPID, A.WORKUNITID, B.TYPE, B.WORKUNITNAME,B.TABLEID"
					+ " FROM RIGHTGROUPOFUSERBYWORKUNIT A, WORKUNIT B WHERE DEVELOPERID = ?"
					+ " AND A.RIGHTGROUPID = 'admin' AND A.WORKUNITID = 132 "
					+ " AND B.WORKUNITID = A.WORKUNITID ORDER BY B.WORKUNITNAME";

			stm = conn.prepareStatement(sql);
			stm.setLong(1,developerID);
			rs = stm.executeQuery();

			if (rs.next()) {
				return true;
			}
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
		}
	}

	public static void setUserRoles(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final HttpSession session = request.getSession();
			//set global parameters (should only be executed one time during app statup, but couln't find this place)
			// Get userID from login form
			UserInfo userLoginInfo = (UserInfo)session.getAttribute("UserLoginInfo");
			final long userID = userLoginInfo.developerID;
			int i;

			Vector [] roles=getUserRoles(userID);
			Vector projectRoles = roles[0];
			Vector groupRoles = roles[1];
			Vector orgRoles = roles[2];
			Vector roleVector = roles[3];
			if ((roleVector == null) || (roleVector.size() == 0)) {
				Fms1Servlet.callPage(
					"error.jsp?error=You don't have right on any work unit, please contact your administrator",request,response);
				return;
			}


			//default home:
			String pageName="";
			int priLevel = 0;
			if (orgRoles.size() > 0){
				priLevel = Constants.RIGHT_ORGANIZATION;
				pageName="organizationHome.jsp";
			}
			else if (groupRoles.size() > 0){
				priLevel = Constants.RIGHT_GROUP;
				pageName="groupHome.jsp";
			}
			else if (projectRoles.size() > 0){
				priLevel = Constants.RIGHT_PROJECT;
				pageName="projectHome.jsp";				
				String userNameLogged = userLoginInfo.account;
				Vector exPjListID = Project.getExternalProjectsIDList(userNameLogged);
				if (exPjListID == null) exPjListID = new Vector();
				Vector vProject = null;
				
				if (exPjListID.size() > 0)
					 vProject = Project.getProjectsByWUsAndEx(projectRoles, exPjListID);
				else
				 	vProject = Project.getProjectsByWUs(projectRoles);
				 	
				session.setAttribute("ProjectFilterInfo", vProject);
                session.setAttribute("RankList", Project.getRankList());
			}
			else{
				for (i = 0; i < roleVector.size(); i++) {
					RolesInfo tempRole = (RolesInfo) roleVector.elementAt(i);
					if (tempRole.workunitType==3) {
						priLevel = Constants.RIGHT_ADMIN; //admin
						pageName="welcomePage.jsp";
						break;
					}
				}
			}
			session.setAttribute("defaultHome", String.valueOf(priLevel));
			// Charging all type of workunit
			session.setAttribute("orgList", orgRoles);
			session.setAttribute("groupList", groupRoles);
			session.setAttribute("projectList", projectRoles);
			Fms1Servlet.callPage("mainFrame.jsp?main="+pageName,request,response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * returns array of 3 vectors
	 * [2]=Org rights
	 * [1]=Group rights
	 * [0]=Projects rights
	 * [3]=Role Vector
	 */
	public static Vector[] getUserRoles(long userID) {
		Vector[] result=new Vector[4];
		try {
			int i, j, k;

			final Vector roleVector = Roles.getRightOfUser(userID);
			Vector projectRoles = new Vector();
			Vector groupRoles = new Vector();
			Vector orgRoles = new Vector();
			result[0]=projectRoles;
			result[1]=groupRoles;
			result[2]=orgRoles;
			result[3]=roleVector;

			if ((roleVector == null) || (roleVector.size() == 0)) {
				return result;
			}
			//dispach
			for (i = 0; i < roleVector.size(); i++) {
				RolesInfo tempRole = (RolesInfo) roleVector.elementAt(i);
				switch (tempRole.workunitType) {
					case 0 : //org
						orgRoles.add(tempRole);
						break;
					case 1 : //group
						groupRoles.add(tempRole);
						break;
					case 2 : //project
						projectRoles.add(tempRole);
						break;
				}
			}
			/**
			* process inheritance among WU
			* project override group override org
			* process orgranization inheritance
			*/
			boolean isAlready = false;
			for (i = 0; i < orgRoles.size(); i++) {
				RolesInfo orgInfo = (RolesInfo) orgRoles.elementAt(i);
				Vector childrenWU = Roles.getChildrenRights(orgInfo);
				for (j = 0; j < childrenWU.size(); j++) {
					RolesInfo childrenWUinfo = (RolesInfo) childrenWU.elementAt(j);
					isAlready = false;
					for (k = 0; k < groupRoles.size(); k++) {
						RolesInfo groupInfo = (RolesInfo) groupRoles.elementAt(k);
						if (groupInfo.workUnitID == childrenWUinfo.workUnitID) {
							isAlready = true;
							break;
						}
					}
					if (!isAlready)
						groupRoles.add(childrenWUinfo);
				}
			}
			/*process group inheritance*/
			for (i = 0; i < groupRoles.size(); i++) {
				RolesInfo groupInfo = (RolesInfo) groupRoles.elementAt(i);
				Vector childrenWU = Roles.getChildrenRights(groupInfo);
				for (j = 0; j < childrenWU.size(); j++) {
					RolesInfo childrenWUinfo = (RolesInfo) childrenWU.elementAt(j);
					isAlready = false;
					for (k = 0; k < projectRoles.size(); k++) {
						RolesInfo projectInfo = (RolesInfo) projectRoles.elementAt(k);
						if (projectInfo.workUnitID == childrenWUinfo.workUnitID) {
							isAlready = true;
							break;
						}
					}
					if (!isAlready)
						projectRoles.add(childrenWUinfo);
				}
			}
            /**/
			//projectRoles.
            // HuyNH2 modify 
			for (i = 0; i < projectRoles.size()-1; i++) {
				for (j = i+1; j < projectRoles.size(); j++) {
					RolesInfo r1 = (RolesInfo) projectRoles.elementAt(i);
					RolesInfo r2 = (RolesInfo) projectRoles.elementAt(j);
					RolesInfo r3;
					if (r1.workunitName.compareTo(r2.workunitName)  <= 0) {
						r3 = (RolesInfo) projectRoles.elementAt(i);
						projectRoles.setElementAt(r2,i);
						projectRoles.setElementAt(r3,j);
					}
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			return result;
		}
	}

}
