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
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Vector;

import com.fms1.infoclass.CustomerInfo;
import com.fms1.infoclass.UserInfo;
import com.fms1.tools.*;
import com.fms1.web.*;
/**
 * User profile logic.
 * Rename to Users
 */
public final class UserHelper {
	public static UserInfo getUser(final long developerID) {
		return getUser("SELECT * FROM DEVELOPER WHERE DEVELOPER_ID = " + developerID);
	}
	public static UserInfo getUserInfo(final String account, final String password){
		return getUser("SELECT * FROM DEVELOPER WHERE ACCOUNT ='" + account.trim().toUpperCase() + "'" + " AND PASSWORD = '" + password.trim() + "'");
	}
	public static UserInfo getUserNoCaseSensitive(final String account) {
		try {
			UserInfo temp =  getUser("SELECT * FROM DEVELOPER WHERE ACCOUNT ='" + account.trim().toUpperCase() + "'");
			if (temp!=null){
				if (temp.status.equalsIgnoreCase("4")) {
					Date dateQuit = temp.userQuitDate ; 
					if (dateQuit==null) return null;
					else {
						if (!dateQuit.after(new Date(System.currentTimeMillis()))) {
							return null;
						}
						else return temp ; 
					}
				}
				else return temp;
			}
			return temp ; 
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static UserInfo getUserByName(final String strName){
		return getUser("SELECT * FROM DEVELOPER WHERE UPPER(NAME) ='" + strName.trim().toUpperCase() + "'" + " AND STATUS != 4");
	}
	/**
	 * User want to check which input user has been signed. 
	 * @param iType: applies to PP  or WO signatures
	 * @param lProjectID: 
	 * @param strAccountName: Name or Account of User which is signed.
	 * @param strType: type of searching by Name or by Account
	 * @return
	 */
	public static UserInfo doCheckUserAssigned(int iType, long lProjectID, String strAccountName, String strType){
		if (strAccountName != null && !"".equals(strAccountName)){
			String sql = "";
			String strCondition = "";
			String strCondition2 = "";
			if ("Account".equals(strType)){
				strCondition = CommonTools.doCreateSQLCondition("ACCOUNT", strAccountName, ConvertString.adCheck);
			}
			else{
				strCondition = CommonTools.doCreateSQLCondition("UPPER(NAME)", strAccountName, ConvertString.adCheck); 
			}
			strCondition2 += CommonTools.doCreateSQLCondition("TYPE", new Integer(iType));
			strCondition2 += CommonTools.doCreateSQLCondition("PROJECT_ID", new Long(lProjectID));
			sql =
				" SELECT * FROM DEVELOPER "
					+ " WHERE DEVELOPER_ID NOT IN "
					+ "   (SELECT DEVELOPER_ID FROM APPROVAL "
					+ "  	WHERE 1 = 1 "
					+ strCondition2
					+" ) "
					+ strCondition
					+ " AND STATUS != 4"
					+ " ORDER BY ACCOUNT";
			return getUser(sql);
		}
		else {
			return null;
		}
	}
	private static UserInfo getUser(String sql) {
		UserInfo result = null;//must return null if not found
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			if (rs.next()){
				result = new UserInfo();
				result.developerID =rs.getLong("DEVELOPER_ID");
				result.account = rs.getString("ACCOUNT");
				result.Password = rs.getString("PASSWORD");
                result.userEmail = rs.getString("EMAIL");
                result.userStartDate = rs.getDate("BEGIN_DATE");
                result.userQuitDate = rs.getDate("QUIT_DATE");
                result.Name = rs.getString("NAME");
				result.maxLevWU = 0; //todo
				result.designation = rs.getString("DESIGNATION");
				result.role = rs.getString("ROLE");
				result.status = rs.getString("STATUS");
				result.group = rs.getString("GROUP_NAME");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return result;
		}
	}

	static boolean updateUser(final UserInfo newUser) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
        try {
			conn = ServerHelper.instance().getConnection();
            sql =  "UPDATE DEVELOPER SET "
                  + "EMAIL = ?, BEGIN_DATE = ?, QUIT_DATE = ?, NAME = ?, DESIGNATION  = ?, ROLE= ?, STATUS = ?, GROUP_NAME = ? "
                  + "WHERE DEVELOPER_ID = ?";
        	stm = conn.prepareStatement(sql);
            stm.setString(1, newUser.userEmail.trim());
            stm.setDate(2,(java.sql.Date) newUser.userStartDate);             
            stm.setDate(3, (java.sql.Date) newUser.userQuitDate);
			stm.setString(4, newUser.Name.trim());
			stm.setString(5, newUser.designation.trim());
			stm.setString(6, newUser.role.trim());
			stm.setString(7, newUser.status.trim());
			stm.setString(8, newUser.group.trim());
			stm.setLong(9, newUser.developerID);
			if (stm.executeUpdate() == 0)
				return false;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return true;
		}
	}
	
	static long addUser(final UserInfo newUser) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = "";
		long newID = 0;
		try {
			conn = ServerHelper.instance().getConnection();
            sql = "SELECT developer_id FROM DEVELOPER WHERE ACCOUNT ='" + newUser.account.toUpperCase()  + "'";
			stm = conn.prepareStatement(sql);
			ResultSet rs = stm.executeQuery(sql);
			if (rs.next()) {
				return 0;
			}
			rs.close();
			stm.close();
			sql =
				"INSERT INTO DEVELOPER ( DEVELOPER_ID,ACCOUNT, PASSWORD, EMAIL, BEGIN_DATE, QUIT_DATE,  NAME, DESIGNATION, ROLE, STATUS,GROUP_NAME)"
					+ " VALUES (0,?,?,?,?,?,?,?,?,?,?)";
			stm = conn.prepareStatement(sql);
			stm.setString(1, newUser.account);
            stm.setString(2, newUser.Password);
            stm.setString(3, newUser.userEmail.trim());
            stm.setDate(4,(java.sql.Date) newUser.userStartDate);             
            stm.setDate(5, (java.sql.Date) newUser.userQuitDate);
			stm.setString(6, newUser.Name.trim());
			stm.setString(7, newUser.designation.trim());
			stm.setString(8, newUser.role.trim());
			stm.setString(9, newUser.status.trim());
			stm.setString(10, newUser.group);
			if (stm.executeUpdate() == 0) {
				return 0;
			} else {
				sql = "SELECT DEVELOPER_ID FROM DEVELOPER where ACCOUNT ='" + newUser.account + "'";
				stm = conn.prepareStatement(sql);
				rs = stm.executeQuery(sql);
				if (rs.next()) {
					newID =  rs.getLong("DEVELOPER_ID");
				}
				else {
					return 0;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return newID;
		}
	}
	static boolean deleteUser(final long developerID) {
        return Db.delete(developerID,"DEVELOPER_ID","DEVELOPER");
	}
	/**
	 * @return Vector of UserInfo
	 */
	public static Vector getUsersByGroup(String groupName) {
		return getUsers("GROUP", groupName);
	}
	/**
	 * @return Vector of UserInfo
	 */
	public static Vector getAllUsers() {
		return getUsers(null, null);
	}
	/**
	 * We can get all users who has Account(Name) may be belong to a group
	 * We also can check user who has Account(Name) and may be belong to a group
	 * @param strGroup: GroupName: if need check user belong to one group
	 * @param strSearchRule: Search by Name or by Account
	 * @param strSearchName: Name of User or Account of User
	 * @param typeQuery: search(using LIKE), check (Using =)
	 * @return
	 */
	public static Vector getUserToFillter(String strGroup, String strSearchRule, String strSearchName, int typeQuery){
		Vector vtResult = new Vector();
		Connection conn = null;
		Statement stm = null;
		String sql = "";
		ResultSet rs = null;
		try{
			final ConvertString cs = new ConvertString();
			String strCondition = "";
			if (strSearchName == null || "".equals(strSearchName)){
				strCondition = "";
			}
			else if ("Name".equals(strSearchRule)){
				strCondition  += CommonTools.doCreateSQLCondition("UPPER(NAME)", strSearchName, typeQuery);
			}
			else if ("Account".equals(strSearchRule)){
				strCondition += CommonTools.doCreateSQLCondition("ACCOUNT", strSearchName, typeQuery);
			}
			if (strGroup != null && !"".equals(strGroup)){
				strCondition += CommonTools.doCreateSQLCondition("UPPER(GROUP_NAME)", strGroup, ConvertString.adCheck);
			}
			sql = "SELECT  DEVELOPER_ID, ACCOUNT, NAME, GROUP_NAME "
				+ " FROM DEVELOPER "
				+ " WHERE 1 = 1 "
				+ strCondition 
				+ " AND STATUS != 4"
				+ " ORDER BY ACCOUNT, GROUP_NAME";
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()){
				UserInfo userInfo = new UserInfo();
				userInfo.developerID = rs.getLong("DEVELOPER_ID");
				userInfo.account = rs.getString("ACCOUNT");
				userInfo.Name = rs.getString("NAME");
				userInfo.group = rs.getString("GROUP_NAME");
				vtResult.add(userInfo);
			}
			return vtResult;
		}
		catch (SQLException ex){
			ex.printStackTrace();
			return vtResult;
		}
		finally{
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	
	public static Vector getCustomerToFillter(String strSearchRule, String strSearchName, int typeQuery){
		Vector vtResult = new Vector();
		Connection conn = null;
		Statement stm = null;
		String sql = "";
		ResultSet rs = null;
		try{
			final ConvertString cs = new ConvertString();
			String strCondition = "";
			if (strSearchName == null || "".equals(strSearchName)){
				strCondition = "";
			}
			else if ("StandardName".equals(strSearchRule)){
				strCondition  += CommonTools.doCreateSQLCondition("UPPER(CUS_NAME)", strSearchName, typeQuery);
			}
			else if ("FullName".equals(strSearchRule)){
				strCondition += CommonTools.doCreateSQLCondition("UPPER(CUS_DESCRIPTION)", strSearchName, typeQuery);
			}
			sql = "SELECT  CUS_NAME, CUS_DESCRIPTION, CUS_NOTE, OG "
				+ " FROM CUSTOMER "
				+ " WHERE 1 = 1 "
				+ strCondition 
				+ " ORDER BY CUS_NAME, CUS_DESCRIPTION";
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()){
				CustomerInfo customerInfo = new CustomerInfo();
				customerInfo.standardName = rs.getString("CUS_NAME");
				customerInfo.fullName = rs.getString("CUS_DESCRIPTION");
				customerInfo.note = rs.getString("CUS_NOTE");
				customerInfo.ofOGs = rs.getString("OG");
				vtResult.add(customerInfo);
			}
			return vtResult;
		}
		catch (SQLException ex){
			ex.printStackTrace();
			return vtResult;
		}
		finally{
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
		
	/**
	 * @return Vector of UserInfo
	 */
	public static Vector getUsers(String searchRule, String searchName) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			
			final ConvertString cs = new ConvertString();
			String condition = "";
			if (searchName == null||"".equals(searchName))
				condition = "";
			else if ("NAME".equals(searchRule)){
				condition += CommonTools.doCreateSQLCondition("UPPER(NAME)", searchName, ConvertString.adSearch_);
			}
			else if ("ID".equals(searchRule)){
				condition += CommonTools.doCreateSQLCondition("ACCOUNT", searchName, ConvertString.adSearch_);
			}
			else if ("GROUP".equals(searchRule)){
				condition += CommonTools.doCreateSQLCondition("GROUP_NAME", searchName, ConvertString.adCheck);
			}
			sql = "SELECT DEVELOPER_ID, ACCOUNT, PASSWORD, NAME, DESIGNATION, ROLE, STATUS, GROUP_NAME"
				+ " FROM DEVELOPER "
				+ " WHERE 1 = 1 "
				+ condition
				+ " ORDER BY ACCOUNT";
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				final UserInfo result = new UserInfo();
				result.developerID = rs.getLong("DEVELOPER_ID");
				result.account = rs.getString("ACCOUNT");
				result.Password = rs.getString("PASSWORD");
				result.Name = rs.getString("NAME");
				result.maxLevWU = 0; //todo
				result.designation = rs.getString("DESIGNATION");
				result.role = rs.getString("ROLE");
				result.status = rs.getString("STATUS");
				result.group = rs.getString("GROUP_NAME");
				resultVector.addElement(result);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
	public static final int getNumDeveloper() {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int numDev = 0;
		try {
			
			sql = "SELECT COUNT(*) FROM DEVELOPER WHERE (STATUS = 1 OR STATUS = 2)";
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			
			if (rs.next()) {
				numDev = rs.getInt(1);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return numDev;
		}
	}
	public static boolean ChangePassword(final String newpass, final long developerID) {
			Connection conn = null;
			PreparedStatement stm = null;
			String sql = null;
			try {
				conn = ServerHelper.instance().getConnection();
				sql ="UPDATE DEVELOPER SET PASSWORD = ? WHERE DEVELOPER_ID = ?";
				stm = conn.prepareStatement(sql);			
				stm.setString(1,newpass);
				stm.setLong(2,developerID);
			
				if (stm.executeUpdate() == 0)
					return false;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn, stm, null);
				return true;
			}
		}
	/**
	 * check user login: PL, PM, QA
	 * @param strRole
	 * @return
	 */
	public static int getUserLoginRole(String strRole){
		int iRole = 0;
		try{
			String role = strRole;
			if (role.trim().charAt(1) == '1'){   //Project Leader
				iRole = 1;
			}
			if (role.trim().charAt(4) == '1'){   //PQA
				iRole = 2;
			}
			int i = 0;
			for (i = 0; i < role.length(); i++){
				if (role.charAt(i) == '1')
					break;
			}
			if (i == 10){
				iRole = 3;
			}
			if (role.trim().charAt(6) == '1'){   //external user of project level
				iRole = 5;
			}
			if (role.trim().charAt(7) == '1'){   //external user of group level
				iRole = 4;
			}
			return iRole;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return iRole;
		}
	}
	/**
	 * get information about group, project (workunit) which users were assigned into.
	 * @param searchRule
	 * @param searchName
	 * @return
	 */
	public static Vector getUsersProfile(String searchRule, String searchName) {
		final Vector resultVector = new Vector();
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			
			String condition = "";
			if (searchName == null || "".equals(searchName))
				condition = "";
			else if ("NAME".equals(searchRule)){
				condition += CommonTools.doCreateSQLCondition("UPPER(NAME)", searchName, ConvertString.adSearch_);
			}
			else if ("ID".equals(searchRule)){
				condition += CommonTools.doCreateSQLCondition("ACCOUNT", searchName, ConvertString.adSearch_);
			}

			sql = " SELECT   d.DEVELOPER_ID, d.ACCOUNT, d.PASSWORD, d.NAME,"
						+ " d.DESIGNATION, D.ROLE, d.STATUS, d.GROUP_NAME, b.WORKUNITNAME " 
				 + " FROM DEVELOPER d, RIGHTGROUPOFUSERBYWORKUNIT a, WORKUNIT b "
				 + " WHERE a.DEVELOPERID (+)= d.DEVELOPER_ID "
				 + " AND b.WORKUNITID(+) = a.WORKUNITID "
				 + condition
				 + " ORDER BY ACCOUNT";
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			long developerId = 0;
			String workUnitName = "";
			UserInfo userInfo = new UserInfo();
			while (rs.next()) {
				workUnitName = "";
				if (rs.getString("WORKUNITNAME") != null){
					workUnitName = rs.getString("WORKUNITNAME");
				}
				if (developerId != rs.getLong("DEVELOPER_ID")){
					if (developerId != 0){
						if (userInfo.wuNote != null && !"".equals(userInfo.wuNote)){
							userInfo.wuNote = userInfo.wuNote.substring(0, userInfo.wuNote.length() - 2);
						}
						resultVector.addElement(userInfo);
					}

					developerId = rs.getLong("DEVELOPER_ID");
					userInfo = new UserInfo();
					userInfo.developerID = developerId;
					userInfo.account = rs.getString("ACCOUNT");
					userInfo.Password = rs.getString("PASSWORD");
					userInfo.Name = rs.getString("NAME");
					userInfo.maxLevWU = 0; //todo
					userInfo.designation = rs.getString("DESIGNATION");
					userInfo.role = rs.getString("ROLE");
					userInfo.status = rs.getString("STATUS");
					userInfo.group = rs.getString("GROUP_NAME");
					userInfo.wuNote += workUnitName + ", ";
				}
				else {
					userInfo.wuNote += workUnitName + ", ";
				}
			}
			if (developerId != 0){
				userInfo.wuNote = userInfo.wuNote.substring(0, userInfo.wuNote.length() - 2);
				resultVector.addElement(userInfo);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return resultVector;
		}
	}
	
	public static void EncodeAllUserPassword(){
		String originalPass = "";
		String encyptedPass = "";
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = "";
		ResultSet rs = null;
		String[][] arrPass = new String[2500][3];
		int i=0;
		try{
			sql = "SELECT PASSWORD,ACCOUNT FROM DEVELOPER where STATUS !=4";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery(sql);
			while (rs.next()){
				originalPass = rs.getString("PASSWORD");
				encyptedPass = SHA1.hex_sha1(originalPass);
				arrPass[i][0]=originalPass;
				arrPass[i][1]=encyptedPass;
				arrPass[i][2]=rs.getString("ACCOUNT");
				i++;
			}
			ServerHelper.closeConnection(conn, stm, rs);
			
			for(int j=0;j<i;j++){
				conn = ServerHelper.instance().getConnection();
				sql = "Update developer d set d.PASSWORD = ? where d.ACCOUNT = ? and d.PASSWORD = ?";
				stm = conn.prepareStatement(sql);
				stm.setString(1, arrPass[j][1]);
				stm.setString(2, arrPass[j][2]);
				stm.setString(3, arrPass[j][0]);
				stm.executeUpdate();
				ServerHelper.closeConnection(conn, stm, rs);
			}
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		finally{
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
}