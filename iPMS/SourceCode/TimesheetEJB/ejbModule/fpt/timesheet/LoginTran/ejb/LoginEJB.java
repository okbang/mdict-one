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
 * Timesheet 2 Application - Follow Framework
 * Ejb Layer
 * LoginTranAction
 * Author : ThanhPT
 * Update : NhanDQ, @ Oct 09,2002
 * History: TrangTK, Jan 04,2002
 * 			ThanhPT created
 */
package fpt.timesheet.LoginTran.ejb;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import fpt.timesheet.constant.Timesheet;
import fpt.timesheet.framework.connection.WSConnectionPooling;
import fpt.timesheet.framework.core.SessionInfoBaseBean;

public class LoginEJB implements SessionBean {

	private static final Logger logger = Logger.getLogger(LoginEJB.class);
	//*******************************************
	// Internal properties
	private Connection con;
	private SessionContext context;
	private WSConnectionPooling conPool = new WSConnectionPooling();
	private DataSource ds = null;
	private String strClassName = "LoginEJB";
	//*******************************************

    /**
     * Method ejbCreate.
     * @throws CreateException
     */
    public void ejbCreate() throws CreateException {
        ds = conPool.getDS();
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {

    }

    public void ejbPassivate() {
    }

    /**
     * @see javax.ejb.SessionBean#setSessionContext(SessionContext)
     */
    public void setSessionContext(SessionContext context) {
        this.context = context;
    }

	/**
	 * Method getExternalUserInfo
	 * @param strUserName
	 * @param strPassword
	 * @param nLocation
	 * @return sessionInfoBaseBean
	 * @throws SQLException
	 */
	public Collection getExternalUserInfo(String strUserName, String strPassword) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		SessionInfoBaseBean sessionInfoBaseBean = null;
		ArrayList listExternalUser = new ArrayList();

		String strAccount = strUserName.toUpperCase();
		StringBuffer strSQL = new StringBuffer();

		strSQL.append(" SELECT d.developer_id, d.group_name, d.NAME, d.ACCOUNT, d.PASSWORD, d.ROLE AS ROLE, d.status , d.quit_date ");
		strSQL.append(" FROM developer d ");
		strSQL.append(" WHERE (d.ROLE = '0000001000' OR d.ROLE = '0000001100')");
		strSQL.append(" AND UPPER(d.account) = '" + strAccount + "'");
		strSQL.append(" AND d.password = '" + strPassword + "'");

		//logger.debug(strClassName + ".getExternalUser(): strSQL = " + strSQL.toString());
		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();

			if (rs.next()) {
				String strRole = rs.getString("ROLE").trim();
				sessionInfoBaseBean = new SessionInfoBaseBean();
				sessionInfoBaseBean.setUserID(rs.getInt("DEVELOPER_ID"));
				sessionInfoBaseBean.setGroupName(rs.getString("GROUP_NAME"));
				sessionInfoBaseBean.setFullname(rs.getString("NAME"));
				sessionInfoBaseBean.setLoginName(rs.getString("ACCOUNT"));
				sessionInfoBaseBean.setLoginPassword(rs.getString("PASSWORD"));
				sessionInfoBaseBean.setRole(strRole);
				sessionInfoBaseBean.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
				if (!sessionInfoBaseBean.getStatus().equalsIgnoreCase("4"))
					listExternalUser.add(sessionInfoBaseBean);
				else {
					String temp = rs.getString("QUIT_DATE");
					if (!temp.trim().equalsIgnoreCase("")) {
						Date quitDate = rs.getDate("QUIT_DATE");
						if (quitDate.after(new Date(System.currentTimeMillis()))) {
							listExternalUser.add(sessionInfoBaseBean);
						}
					}
				}
				
			}
		}
		catch (SQLException ex) {
			System.out.println(strClassName + ".getExternalUser():" + "SQLException occurs in LoginEJB.getUserInfo(). " + ex.toString());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			System.out.println(strClassName + ".getExternalUser():" + "Exception occurs in getExternalUser :" + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getExternalUser():");
            return listExternalUser;
		}
	}

	/**
	 * Method getUserInfo
	 * @param strUserName
	 * @param strPassword
	 * @param nLocation
	 * @return sessionInfoBaseBean
	 * @throws SQLException
	 */
	public Collection getUserInfo(String strUserName, String strPassword) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		SessionInfoBaseBean sessionInfoBaseBean = null;
		ArrayList listUserInfo = new ArrayList();

		String strAccount = strUserName.toUpperCase();
		StringBuffer strSQL = new StringBuffer();

		strSQL.append(" SELECT DISTINCT d.developer_id, d.group_name, d.NAME, d.ACCOUNT, d.PASSWORD, d.quit_date, ");
		strSQL.append(" (CASE ");
		strSQL.append(" WHEN a.response = " + Timesheet.RESPONSIBILITY_PTL);
		strSQL.append("   OR a.response = " + Timesheet.RESPONSIBILITY_PM);
		strSQL.append("   THEN '1100000000' ");
		strSQL.append(" WHEN a.response = " + Timesheet.RESPONSIBILITY_SQA);
		strSQL.append("   OR a.response = " + Timesheet.RESPONSIBILITY_PQA);
		strSQL.append("   THEN '1000100000' ");
		strSQL.append(" WHEN a.response = " + Timesheet.RESPONSIBILITY_EXTERNAL);
		strSQL.append("   THEN '0000001000' ");
		strSQL.append(" ELSE '1000000000' ");
		strSQL.append(" END) AS ROLE, d.status ");
		strSQL.append(" FROM assignment a, developer d ");
		strSQL.append(" WHERE a.developer_id = d.developer_id ");
		strSQL.append(" AND UPPER(d.account) = '" + strAccount + "'");
		strSQL.append(" AND d.password = '" + strPassword + "'");

		strSQL.append(" UNION ");
		strSQL.append(" SELECT DISTINCT d.developer_id, d.group_name, d.NAME, d.ACCOUNT, d.PASSWORD,d.quit_date, d.ROLE AS ROLE, d.status ");
		strSQL.append(" FROM developer d ");
		strSQL.append(" WHERE UPPER(d.account) = '" + strAccount + "'");
		strSQL.append(" AND d.password = '" + strPassword + "'");

		strSQL.append(" ORDER BY ROLE ASC ");

		//logger.debug(strClassName + ".getUserInfo(): strSQL = " + strSQL.toString());
		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();
			
			String strRole = "";
			int intCountPM = 0;
			int intCountQA = 0;
			
			while (rs.next()) {
				String strRoleTmp = rs.getString("ROLE").trim();
				if (strRoleTmp.equals("1100000000")) { //PM, PTL
					intCountPM += 1;
				}
				if (strRoleTmp.equals("1000100000")) { //PQA, SQA
					intCountQA += 1;
				}
				if (intCountPM >= 1 && intCountQA >= 1 ){
					strRole = "1100100000"; // convert to QA-PM
				}
				else {
					strRole = strRoleTmp;
				}
				sessionInfoBaseBean = new SessionInfoBaseBean();
				sessionInfoBaseBean.setUserID(rs.getInt("DEVELOPER_ID"));
				sessionInfoBaseBean.setGroupName(rs.getString("GROUP_NAME"));
				sessionInfoBaseBean.setFullname(rs.getString("NAME"));
				sessionInfoBaseBean.setLoginName(rs.getString("ACCOUNT"));
				sessionInfoBaseBean.setLoginPassword(rs.getString("PASSWORD"));
				sessionInfoBaseBean.setRole(strRole);
				sessionInfoBaseBean.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
				if (!sessionInfoBaseBean.getStatus().equalsIgnoreCase("4"))
					listUserInfo.add(sessionInfoBaseBean);
				else {
					String temp = rs.getString("QUIT_DATE");
					if (!temp.trim().equalsIgnoreCase("")) {
						Date quitDate = rs.getDate("QUIT_DATE");
						if (quitDate.after(new Date(System.currentTimeMillis()))) {
							listUserInfo.add(sessionInfoBaseBean);
						}
					}
				}
				logger.debug(strClassName + "strRole()= " + sessionInfoBaseBean.getRole());
			}

//			if (rs.next()) {
//				String strRole = rs.getString("ROLE").trim();
//				sessionInfoBaseBean = new SessionInfoBaseBean();
//				sessionInfoBaseBean.setUserID(rs.getInt("DEVELOPER_ID"));
//				sessionInfoBaseBean.setGroupName(rs.getString("GROUP_NAME"));
//				sessionInfoBaseBean.setFullname(rs.getString("NAME"));
//				sessionInfoBaseBean.setLoginName(rs.getString("ACCOUNT"));
//				sessionInfoBaseBean.setLoginPassword(rs.getString("PASSWORD"));
//				sessionInfoBaseBean.setRole(strRole);
//				sessionInfoBaseBean.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
//				listUserInfo.add(sessionInfoBaseBean);
//				logger.debug(strClassName + "strRole()= " + rs.getString("ROLE").trim());
//			}
		}
		
		catch (SQLException ex) {
			System.out.println(strClassName + ".getUserInfor():" + "SQLException occurs in LoginEJB.getUserInfo(). " + ex.toString());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			System.out.println(strClassName + ".getUserInfor():" + "Exception occurs in getUserInfo :" + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getUserInfo():");
            return listUserInfo;
		}
	}

    /**
     * Method getCommunicatorUserInfo
     * @param strUserName
     * @param strPassword
     * @param nLocation
     * @return sessionInfoBaseBean
     * @throws SQLException
     */
    public Collection getCommunicatorUserInfo(String strUserName, String strPassword) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        SessionInfoBaseBean sessionInfoBaseBean = null;
        ArrayList listCommunicatorUser = new ArrayList();

        String strAccount = strUserName.toUpperCase();
        StringBuffer strSQL = new StringBuffer();

        strSQL.append(" SELECT d.developer_id, d.group_name, d.NAME, d.ACCOUNT, d.PASSWORD, d.ROLE AS ROLE, d.status , d.quit_date ");
        strSQL.append(" FROM developer d ");
        strSQL.append(" WHERE (d.ROLE = '0000000010')");
        strSQL.append(" AND UPPER(d.account) = '" + strAccount + "'");
        strSQL.append(" AND d.password = '" + strPassword + "'");

        //logger.debug(strClassName + ".getCommunicatorUser(): strSQL = " + strSQL.toString());
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            stmt = con.prepareStatement(strSQL.toString());
            rs = stmt.executeQuery();

            if (rs.next()) {
                String strRole = rs.getString("ROLE").trim();
                sessionInfoBaseBean = new SessionInfoBaseBean();
                sessionInfoBaseBean.setUserID(rs.getInt("DEVELOPER_ID"));
                sessionInfoBaseBean.setGroupName(rs.getString("GROUP_NAME"));
                sessionInfoBaseBean.setFullname(rs.getString("NAME"));
                sessionInfoBaseBean.setLoginName(rs.getString("ACCOUNT"));
                sessionInfoBaseBean.setLoginPassword(rs.getString("PASSWORD"));
                sessionInfoBaseBean.setRole(strRole);
                sessionInfoBaseBean.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
				if (!sessionInfoBaseBean.getStatus().equalsIgnoreCase("4"))
					listCommunicatorUser.add(sessionInfoBaseBean);
				else {
					String temp = rs.getString("QUIT_DATE");
					if (!temp.trim().equalsIgnoreCase("")) {
						Date quitDate = rs.getDate("QUIT_DATE");
						if (quitDate.after(new Date(System.currentTimeMillis()))) {
							listCommunicatorUser.add(sessionInfoBaseBean);
						}
					}
				}
               
            }
        }
        catch (SQLException ex) {
            System.out.println(strClassName + ".getCommunicatorUser():" + "SQLException occurs in LoginEJB.getCommunicatorUserInfo(). " + ex.toString());
            ex.printStackTrace();
        }
        catch (Exception ex) {
            System.out.println(strClassName + ".getCommunicatorUser():" + "Exception occurs in getCommunicatorUser :" + ex.getMessage());
        }
        finally {
            conPool.releaseResource(con, stmt, rs, strClassName + ".getCommunicatorUser():");
            return listCommunicatorUser;
        }
    }
}