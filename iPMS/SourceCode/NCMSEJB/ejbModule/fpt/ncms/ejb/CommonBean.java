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
 
 /*
 * @(#)CommonBean.java 12-Mar-03
 */


package fpt.ncms.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import fpt.ncms.framework.connection.WSConnectionPooling;
import fpt.ncms.framework.core.SessionInfoBaseBean;
import fpt.ncms.constant.NCMS;

/**
 * Bean implementation class for Enterprise Bean: Common
 */
public class CommonBean implements javax.ejb.SessionBean {
    private javax.ejb.SessionContext mySessionCtx;
    private WSConnectionPooling conPool = new WSConnectionPooling();
    private DataSource ds = null;
    private Connection con = null;
    /** Indicator for a mode the ejb will run on */
    private boolean debugMode = NCMS.DEBUG;

    /**
     * getSessionContext
     */
    public javax.ejb.SessionContext getSessionContext() {
        return mySessionCtx;
    }

    /**
     * setSessionContext
     */
    public void setSessionContext(javax.ejb.SessionContext ctx) {
        mySessionCtx = ctx;
    }

    /**
     * ejbActivate
     */
    public void ejbActivate() {
        try {
//            if (debugMode) {
//                System.out.println("ConstantEJB.makeConnection() called.");
//            }
            ds = conPool.getDS();
            con = ds.getConnection();
        }
        catch (Exception e) {
            throw new EJBException("ejbActivate Exception: " + e.getMessage());
        }
    }

    /**
     * ejbCreate
     */
    public void ejbCreate() throws javax.ejb.CreateException {
    }

    /**
     * ejbPassivate
     */
    public void ejbPassivate() {
    }

    /**
     * ejbRemove
     */
    public void ejbRemove() {
    }

    /**
     * validLoginRole
     * Check user role
     * @param   strRole - user role
     * @param   nLocation - login mode (user/admin)
     * @return  true if user has permission for this mode
     */
    private boolean validLoginRole(String strRole, int nLocation) {
        // error occured when retrieving data
        if (strRole == null) {
            return false;
        }
        if (strRole.length() < 10) {
            return false;
        }
        // External user - creator, Developer - assignee,
        // Project leader - reviewer
        if ("1".equals(strRole.substring(6, 7))
                || "1".equals(strRole.substring(0, 1))
                || "1".equals(strRole.substring(1, 2))) {
            //if ((nLocation == 0) || (nLocation == 1)) {
                return true;
            //}
        }
        // QA - admin
        if ("1".equals(strRole.substring(4, 5))) {
            return true;
        }
        return false;
    }

    /**
     * getUserInfo
     * Check user by account and password
     * @param   strAccount - account name
     * @param   strPassword - password
     * @param   nLocation - login mode (user/admin)
     * @return  information about user
     * @throws  SQLException
     */
    public SessionInfoBaseBean getUserInfo(String strAccount,
            String strPassword, int nLocation) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        SessionInfoBaseBean sessionInfoBaseBean = null;
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            String strSQL = "SELECT account,password,name,email,NP.role,group_name,"
                    + "status,NP.developer_id "
                    + "FROM Developer D, NCPermission NP "
                    + "WHERE UPPER(D.account)=? "
                    + "AND D.password=? "
                    + "AND D.status<>4 "
                    + "AND D.Developer_ID=NP.Developer_ID "
                    + "AND NP.Tool=?";
            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strAccount.toUpperCase());
            pStmt.setString(2, strPassword);
            pStmt.setInt(3, nLocation);
//            if (debugMode) {
//                System.out.println("getUserInfo: " + strSQL);
//            }
            rs = pStmt.executeQuery();
            if (rs.next()) {
                String strRole = rs.getString("role");
                if (validLoginRole(strRole, nLocation)) {
                    sessionInfoBaseBean = new SessionInfoBaseBean();
                    sessionInfoBaseBean.setLoginName(rs.getString("account"));
                    sessionInfoBaseBean.setLoginPassword(
                            rs.getString("password"));
                    sessionInfoBaseBean.setUserID(rs.getInt("developer_id"));
                    sessionInfoBaseBean.setFullname(rs.getString("name"));
                    sessionInfoBaseBean.setRole(rs.getString("role"));
                    sessionInfoBaseBean.setGroupName(
                            rs.getString("group_name"));
                    sessionInfoBaseBean.setStatus(
                            rs.getString("status") == null ?
                            "" : rs.getString("status"));
                    sessionInfoBaseBean.setEmail(
                            rs.getString("email"));
                }
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "CommonEJB.getUserInfo.");
            throw new SQLException("CommonEJB.getUserInfo: " + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "CommonEJB.getUserInfo: ");
        }
        return sessionInfoBaseBean;
    }

    /**
     * getUserInfo
     * Get user information by account
     * @param   strAccount - account name
     * @return  information about user
     * @throws  SQLException
     */
    public SessionInfoBaseBean getUserInfo(String strAccount) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        SessionInfoBaseBean sessionInfoBaseBean = null;
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            String strSQL = "SELECT DISTINCT account,password,name,email,role,group_name,"
                    + "status,developer_id "
                    + "FROM Developer "
                    + "WHERE UPPER(account)=? "
                    + "AND status<>4 ";
            pStmt = con.prepareStatement(strSQL);
            if (strAccount != null) {
                pStmt.setString(1, strAccount.toUpperCase());
            }
            else {
                pStmt.setString(1, null);
            }
            
//            if (debugMode) {
//                System.out.println("getUserInfo: " + strSQL);
//            }
            rs = pStmt.executeQuery();
            if (rs.next()) {
                sessionInfoBaseBean = new SessionInfoBaseBean();
                sessionInfoBaseBean.setLoginName(rs.getString("account"));
                sessionInfoBaseBean.setLoginPassword(
                        rs.getString("password"));
                sessionInfoBaseBean.setUserID(rs.getInt("developer_id"));
                sessionInfoBaseBean.setFullname(rs.getString("name"));
                sessionInfoBaseBean.setRole(rs.getString("role"));
                sessionInfoBaseBean.setGroupName(
                        rs.getString("group_name"));
                sessionInfoBaseBean.setStatus(
                        rs.getString("status") == null ?
                        "" : rs.getString("status"));
                sessionInfoBaseBean.setEmail(
                        rs.getString("email"));
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "CommonEJB.getUserInfo.");
            throw new SQLException("CommonEJB.getUserInfo: " + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "CommonEJB.getUserInfo: ");
        }
        return sessionInfoBaseBean;
    }

    /**
     * getUserList
     * Get list of users
     * @param   strRole - role of user to list
     * @return  list of users
     * @throws  SQLException
     */
    public ArrayList getUserList(String strRole, int nLocation) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        StringBuffer sbSQL = new StringBuffer();
        ArrayList strList = new ArrayList();
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            sbSQL.append("SELECT account,name FROM Developer D, NCPermission NP ");
            sbSQL.append("WHERE status<>4 ");
            sbSQL.append(" AND D.Developer_ID=NP.Developer_ID ");
            sbSQL.append(" AND NP.Tool=? ");
            
            if ("Creator".equalsIgnoreCase(strRole)) {
                // All users are creators
            }
            else if ("Assignee".equalsIgnoreCase(strRole)) {
                // All non-external users are assignee
                sbSQL.append("AND (SUBSTR(NP.role,7,1)<>'1') ");
            }
            else if ("Reviewer".equalsIgnoreCase(strRole)) {
                // All project leaders and QA are reviewers
                sbSQL.append("AND ((SUBSTR(NP.role,2,1)='1') ");
                sbSQL.append("OR (SUBSTR(NP.role,5,1)='1')) ");
            }
            else if ("PQA".equalsIgnoreCase(strRole)) {
                // Only QA can be counted as SEPG
                sbSQL.append("AND (SUBSTR(NP.role,5,1)='1') ");
            }
            sbSQL.append("ORDER BY account");
//            if (debugMode) {
//                System.out.println("CommonEJB.getUserList: " + sbSQL.toString());
//            }
            pStmt = con.prepareStatement(sbSQL.toString());
            pStmt.setInt(1, nLocation);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                strList.add(rs.getString(1));
                strList.add(rs.getString(2));
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "CommonEJB.getUserList.");
            throw new SQLException("CommonEJB.getUserList: " + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "CommonEJB.getUserList: ");
        }
        return strList;
    }

    /**
     * getGroupList
     * Get list of group names
     * @return  list of group names
     * @throws  SQLException
     */
    public ArrayList getGroupList() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList strList = new ArrayList();
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            /*
            String strSQL = "SELECT DISTINCT table1.* FROM (SELECT   dev.group_name, COUNT(project.project_id)"    
                            +" FROM project right join (SELECT DISTINCT GROUP_NAME  FROM DEVELOPER ORDER BY GROUP_NAME) Dev  on project.group_name= dev.group_name" 
                            +" GROUP BY dev.group_name UNION SELECT group_name,COUNT(project_id) FROM project) table1";
            */
			String strSQL = " SELECT DISTINCT table1.* FROM ( "
							+" SELECT   dev.group_name, COUNT(project.project_id) "    
										+" FROM project right join (SELECT DISTINCT GROUP_NAME  FROM DEVELOPER ORDER BY GROUP_NAME) Dev  on project.group_name= dev.group_name " 
										+" GROUP BY dev.group_name UNION SELECT group_name,COUNT(project_id) FROM project group by group_name "
							 +") table1  WHERE group_name is not null    ORDER BY table1.group_name ";                  
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSQL);
            while (rs.next()) {
                if (rs.getString(1) != null){
                    strList.add(rs.getString(1));
                    strList.add(rs.getString(2));
                }
                
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "CommonEJB.getGroupList.");

            throw new SQLException("CommonEJB.getGroupList: " + se.toString());
        }
        finally {
            conPool.releaseResource(con, stmt, rs, "CommonEJB.getGroupList: ");
        }
        return strList;
    }

    /**
     * getProjectList
     * Get list of project names
     * @param   strGroup - name of group listed projects related to
     * @return  list of project names
     * @throws  SQLException
     */
    public ArrayList getProjectList(String strGroup, int nType, int nLocation) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sbSQL = new StringBuffer();
        ArrayList strList = new ArrayList();
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            sbSQL.append("SELECT Code,UPPER(Code) AS OrderCode, Group_Name, STATUS ");
            sbSQL.append("FROM Project WHERE ");
            sbSQL.append(" NOT ( Project_ID IS NULL) ");
            if (nLocation == NCMS.USER_CALL) {
                sbSQL.append(" AND group_name IS NOT NULL "); // Status IN(0,3) AND 
            }
            
            if (!"".equals(strGroup) && (strGroup != null)) {
                sbSQL.append(" AND Group_name='").append(strGroup).append("' ");
            }
            if(NCMS.GROUP_VALUE == nType){
                sbSQL.append("ORDER BY Group_name,OrderCode");
            }
            else{
                sbSQL.append("ORDER BY OrderCode");
            }
//            if (debugMode) {
//                System.out.println("getProjectList.  SQL:" + sbSQL);
//            }
            
            stmt = con.createStatement();
            rs = stmt.executeQuery(sbSQL.toString());
            while (rs.next()) {
                if (rs.getString(1) != null)
                    strList.add(rs.getString(1));
                else 
                    strList.add("");
                if (rs.getString(3) != null)
                    strList.add(rs.getString(3));
                else 
                    strList.add("");
				strList.add(rs.getString(4));				
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "CommonEJB.getProjectList.");
            throw new SQLException("CommonEJB.getProjectList: "
                    + se.toString());
        }
        finally {
            conPool.releaseResource(con, stmt, rs, "CommonEJB.getProjectList: ");
        }
        return strList;
    }
	/**
	 * getProjectStatusList
	 * Get list of project status
	 * @return  list of project status
	 * @throws  SQLException
	 */
	public ArrayList getProjectStatusList() throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		String str = "";
		ArrayList strList = new ArrayList();
		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			str = "SELECT DISTINCT status FROM Project WHERE NOT ( Project_ID IS NULL) ORDER BY status ";
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(str.toString());
			while (rs.next()) {
					strList.add(rs.getString(1));
			}
			strList.add("-1");	//add "All Status" :NCMS.PROJECT_STATUS_ALL
		}
		catch (SQLException se) {
			System.out.println("SQLException occurs in "
					+ "CommonEJB.getProjectStatusList.");
			throw new SQLException("CommonEJB.getProjectStatusList: "
					+ se.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, "CommonEJB.getProjectStatusList: ");
		}
		return strList;
	}

    /**
     * getParameter
     * Get value of a parameter from Parameters table
     * @param   strParameter - parameter name
     * @return  parameter value
     * @throws  RemoteException
     * @throws  SQLException
     */
    public String getParameter(String strParameter) throws SQLException {
        String strValue = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            String strSQL = "SELECT value FROM Parameters WHERE parameter_id='" + strParameter + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSQL);
            if (rs.next()) {
                strValue = rs.getString("value");
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "CommonEJB.getParameter.");
            throw new SQLException("CommonEJB.getParameter: " + se.toString());
        }
        finally {
            conPool.releaseResource(con, stmt, rs, "CommonEJB.getParameter: ");
        }
        return strValue;
    }

    /**
     * getMailList
     * Get list of mail addresses of requested group
     * @param   nRequestToID - requested group
     * @return  list of project names
     * @throws  RemoteException
     * @throws  SQLException
     */
    public ArrayList getMailList(int nRequestToID) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        ArrayList listResult = new ArrayList();
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            String strSQL = "SELECT D.email FROM developer D, call_user_group  C" +
                            " WHERE C.requestto_id = ? AND" +
                            " D.developer_id = C.developer_id";
            pStmt = con.prepareStatement(strSQL);
            pStmt.setInt(1, nRequestToID);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                listResult.add(rs.getString("email"));
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "CommonEJB.getMailList.");
            throw new SQLException("CommonEJB.getMailList: " + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "CommonEJB.getMailList: ");
        }
        
        return listResult;
   }
}