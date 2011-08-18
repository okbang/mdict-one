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
 
package fpt.timesheet.Exemption.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import fpt.timesheet.ApproveTran.ejb.common.CommonListBean;
import fpt.timesheet.InputTran.ejb.DateUtils;
import fpt.timesheet.InputTran.ejb.AssignmentInfo;
import fpt.timesheet.constant.Timesheet;
import fpt.timesheet.framework.connection.WSConnectionPooling;
import fpt.timesheet.framework.util.CommonUtil.CommonUtil;
import fpt.timesheet.framework.util.SqlUtil.SqlUtil;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.framework.util.StringUtil.StringVector;
import fpt.timesheet.vo.*;

/**
 * Bean implementation class for Enterprise Bean: ExemptionEJB
 */
public class ExemptionEJB implements javax.ejb.SessionBean {

	private static final Logger logger = Logger.getLogger(ExemptionEJB.class);
	//*******************************************
	// Internal properties
	//private SessionContext ctx;
	private javax.ejb.SessionContext mySessionCtx;
	private WSConnectionPooling conPool = new WSConnectionPooling();
	private DataSource ds = null;
	private String strClassName = "ExemptionEJB";
	//*******************************************
	//Paging
	private int intCurrentPage    = 0;
	private int intTotalPage 	  = 0;
	private int intTotalExemption = 0;

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
	 * ejbCreate
	 */
	public void ejbCreate() throws CreateException {
		try {
			ds = conPool.getDS();
		}
		catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}
	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
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
	//*******************************************

	/**
	 * Method getCurrentPage.
	 * @return intCurrentPage
	 */
	public int getCurrentPage() {
		return intCurrentPage;
	}

	/**
	 * Method setCurrentPage.
	 * @param intCurrentPage
	 */
	public void setCurrentPage(int intCurrentPage) {
		this.intCurrentPage = intCurrentPage;
	}

	/**
	 * Method getTotalPage.
	 * @return intTotalPage
	 */
	public int getTotalPage() {
		return intTotalPage;
	}

	/**
	 * Method setTotalPage.
	 * @param intTotalPage
	 */
	public void setTotalPage(int intTotalPage) {
		this.intTotalPage = intTotalPage;
	}

	/**
	 * Method getTotalExemption.
	 * @return intTotalExemption
	 */
	public int getTotalExemption() {
		return intTotalExemption;
	}

	/**
	 * Method setTotalExemption
	 * @param intTotalExemption
	 */
	public void setTotalExemption(int intTotalExemption) {
		this.intTotalExemption = intTotalExemption;
	}

	/**
	 * Method getDeveloperList
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getDeveloperListEJB() throws SQLException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;

		ArrayList arList = new ArrayList();
		CommonListBean beanCommonList;

		try {
			String strSQL = "SELECT developer_id, account, name, group_name FROM developer where status IN (1,2) ORDER by account, name";

			//logger.debug(strClassName + ".getDeveloperList(): strSQL = " + strSQL.toString());

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL);
			rs = stmt.executeQuery();

			while (rs.next()) {
				beanCommonList = new CommonListBean();
				beanCommonList.setDevId(rs.getString("developer_id"));
				beanCommonList.setAccount(rs.getString("account"));
				beanCommonList.setDevName(rs.getString("name"));
				beanCommonList.setGroupName(rs.getString("group_name"));
				arList.add(beanCommonList);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getDeveloperListEJB():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getDeveloperListEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getDeveloperListEJB():");
		}
		return arList;
	}

	/**
	 * Method hasExistedExemption
	 * @param intDevId
	 * @param intType
	 * @param strSearchFromDate
	 * @param strSearchToDate
	 * @return listExistedExemption
	 * @throws SQLException
	 */
	public Collection hasExistedExemption(int intExemptionId, int intDevId, int intType,
										  String strSearchFromDate, String strSearchToDate) throws SQLException {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;

		ExemptionBean beanExemption;
		ArrayList listExistedExemption = new ArrayList();
		StringBuffer strSQL = new StringBuffer();

		try {
			strSQL.append(" SELECT t.developer_id, t.type, ");
			strSQL.append(" TO_CHAR (t.from_date, 'mm/dd/yy') AS from_date, ");
			strSQL.append(" TO_CHAR (t.to_date, 'mm/dd/yy') AS to_date ");
			strSQL.append(" FROM timesheet_exemption t ");
			strSQL.append(" WHERE ");
			if (intExemptionId != 0) {
				strSQL.append(" t.exemption_id != " + intExemptionId);
				strSQL.append(" AND ");
			}
			strSQL.append(" t.developer_id = " + intDevId);
			strSQL.append(" AND t.type = 1 ");

			if (intType != 1) {
				strSQL.append(" UNION ");
				strSQL.append(" SELECT t.developer_id, t.type, ");
				strSQL.append(" TO_CHAR (t.from_date, 'mm/dd/yy') AS from_date, ");
				strSQL.append(" TO_CHAR (t.to_date, 'mm/dd/yy') AS to_date ");
				strSQL.append(" FROM timesheet_exemption t ");
				strSQL.append(" WHERE ");
				if (intExemptionId != 0) {
					strSQL.append(" t.exemption_id != " + intExemptionId);
					strSQL.append(" AND ");
				}
				strSQL.append(" t.developer_id = " + intDevId);
				strSQL.append(" AND t.type IN(2, 3) ");
				if (strSearchToDate != null && strSearchToDate.trim().length() > 0) {
					strSQL.append(" AND t.from_date <= TO_DATE('").append(strSearchToDate);
					strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
					strSQL.append("')");
				}
				if (strSearchFromDate != null && strSearchFromDate.trim().length() > 0) {
					strSQL.append(" AND t.to_date >= TO_DATE('").append(strSearchFromDate);
					strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
					strSQL.append("')");
				}
			}
			//logger.debug(strClassName + ".hasExistedExemption(): strSQL = " + strSQL.toString());

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();

			while (rs.next()) {
				beanExemption = new ExemptionBean();
				beanExemption.setDeveloperId(rs.getInt("developer_id"));
				beanExemption.setType(rs.getInt("type"));
				beanExemption.setFromDate(rs.getString("from_date"));
				beanExemption.setToDate(rs.getString("to_date"));
				listExistedExemption.add(beanExemption);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".hasExistedExemption():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".hasExistedExemption():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".hasExistedExemption():");
		}
		return listExistedExemption;
	}

	/**
	 * Method hasExistedAccount
	 * @param strAccount
	 * @param strName
	 * @return listExistedAccount
	 * @throws SQLException
	 */
	public Collection hasExistedAccount(String strAccount, String strName) throws SQLException {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;

		ExemptionBean beanExemption;
		ArrayList listExistedAccount = new ArrayList();
		StringBuffer strSQL = new StringBuffer();

		try {
			strSQL.append(" SELECT d.developer_id, d.account, d.name ");
			strSQL.append(" FROM developer d ");
			strSQL.append(" WHERE 1=1 ");
			if (! "".equals(strAccount) && ! "".equals(strName) ) {
				strSQL.append(" AND ( d.account = '" + strAccount.toUpperCase() +"'");
				strSQL.append(" OR UPPER(d.name) LIKE '%" + strName.toUpperCase() +"%')");
			}
			else if (! "".equals(strAccount) && "".equals(strName) ) {
				strSQL.append(" AND d.account = '" + strAccount.toUpperCase() +"'");
			}
			else if ("".equals(strAccount) && ! "".equals(strName) ) {
				strSQL.append(" AND UPPER(d.name) LIKE '%" + strName.toUpperCase() +"%'");
			}
			//logger.debug(strClassName + ".hasExistedAccount(): strSQL = " + strSQL.toString());

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();

			while (rs.next()) {
				beanExemption = new ExemptionBean();
				beanExemption.setDeveloperId(rs.getInt("developer_id"));
				beanExemption.setAccount(rs.getString("account"));
				beanExemption.setFullName(rs.getString("name"));
				listExistedAccount.add(beanExemption);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".hasExistedAccount():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".hasExistedAccount():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".hasExistedAccount():");
		}
		return listExistedAccount;
	}

	/**
	 * Method getExemptionListEJB
	 * @param strGroupName
	 * @param strAccount
	 * @param strName
	 * @param intType
	 * @param strSearchFromDate
	 * @param strSearchToDate
	 * @param intCurrPage
	 * @return
	 * @throws SQLException
	 */
	public Collection getExemptionListEJB(String strGroupName, String strAccount, String strName, int intType,
										  String strSearchFromDate, String strSearchToDate, int intCurrPage) throws SQLException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;

		ExemptionBean beanExemption;
		ArrayList listExemption = new ArrayList();

		StringBuffer strSQL = new StringBuffer();
		StringBuffer strFilterSQL = new StringBuffer();
		StringBuffer strCountSQL = new StringBuffer();

		try {
			//@STEP 1 ===== Add Filter
			strFilterSQL.append(" WHERE t.developer_id = d.developer_id ");
			if (! "All".equals(strGroupName)) {
				strFilterSQL.append(" AND d.group_name = '" + strGroupName +"'");
			}
			if (intType != 0) {
				strFilterSQL.append(" AND t.type = " + intType);
			}
			if (! "".equals(strAccount)) {
				strFilterSQL.append(" AND d.account = '" + strAccount.toUpperCase() +"'");
			}
			if (! "".equals(strName)) {
				strFilterSQL.append(" AND UPPER(d.name) LIKE '%" + strName.toUpperCase() +"%'");
			}
			if (strSearchToDate != null && strSearchToDate.trim().length() > 0) {
				strFilterSQL.append(" AND ( ");
				strFilterSQL.append(" t.from_date <= TO_DATE('").append(strSearchToDate);
				strFilterSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
				strFilterSQL.append("')");
				strFilterSQL.append(" OR t.from_date IS NULL ) ");
			}
			if (strSearchFromDate != null && strSearchFromDate.trim().length() > 0) {
				strFilterSQL.append(" AND ( ");
				strFilterSQL.append(" t.to_date >= TO_DATE('").append(strSearchFromDate);
				strFilterSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
				strFilterSQL.append("')");
				strFilterSQL.append(" OR t.to_date IS NULL ) ");
			}

			//@STEP 2 ===== Count Total of Exemption
			strCountSQL.append("SELECT COUNT(exemption_id) as NUM");
			strCountSQL.append(" FROM timesheet_exemption t, developer d ");
			strCountSQL.append(strFilterSQL);

			//logger.debug(strClassName + ".getExemptionListEJB(): strCountSQL = " + strCountSQL.toString());

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strCountSQL.toString());
			rs = stmt.executeQuery();

			if (rs.next()) {
				intTotalExemption = (rs.getString("NUM") != null) ? rs.getInt("NUM") : 0;
			}
			rs.close();

			//@STEP 3 ===== Make SQL
			intCurrentPage = intCurrPage;
			intTotalPage = (intTotalExemption / Timesheet.MAX_RECORDS);

			if ((intTotalExemption % Timesheet.MAX_RECORDS) != 0) {
				intTotalPage = intTotalPage + 1;
			}
			if (intCurrentPage > (intTotalPage - 1)) {
				intCurrentPage = 0;
			}
			int intStart = intCurrentPage * Timesheet.MAX_RECORDS;
			int intEnd = intStart + Timesheet.MAX_RECORDS;

			strSQL.append(" SELECT * ");
			strSQL.append(" FROM (SELECT ROWNUM r, rn.* ");
			strSQL.append(" FROM ( ");
			strSQL.append(" SELECT d.group_name, d.account, d.name, ");
			strSQL.append(" t.exemption_id, t.developer_id, t.type, t.weekday, ");
			strSQL.append(" TO_CHAR(t.from_date,'mm/dd/yy') as from_date, TO_CHAR(t.to_date,'mm/dd/yy') as to_date, t.reason, t.note ");
			strSQL.append(" FROM timesheet_exemption t, developer d ");
			strSQL.append(strFilterSQL);
			strSQL.append(" ORDER BY d.group_name, d.account, d.name ");
			strSQL.append(" ) rn) ");
			strSQL.append(" WHERE r > ").append(intStart).append(" AND r <= ").append(intEnd);

			//logger.debug(strClassName + ".getExemptionListEJB(): strSQL = " + strSQL.toString());

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();

			while (rs.next()) {
				beanExemption = new ExemptionBean();
				beanExemption.setGroup(rs.getString("group_name"));
				beanExemption.setAccount(rs.getString("account"));
				beanExemption.setFullName(rs.getString("name"));
				beanExemption.setExemptionId(rs.getInt("exemption_id"));
				beanExemption.setDeveloperId(rs.getInt("developer_id"));
				beanExemption.setType(rs.getInt("type"));
				beanExemption.setWeekDay(rs.getString("weekday"));
				beanExemption.setFromDate(rs.getString("from_date"));
				beanExemption.setToDate(rs.getString("to_date"));
				beanExemption.setReason(rs.getString("reason"));
				beanExemption.setNote(rs.getString("note"));
				listExemption.add(beanExemption);
			}
			setTotalPage(intTotalPage);
			setTotalExemption(intTotalExemption);
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getExemptionListEJB():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getExemptionListEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getExemptionListEJB():");
		}
		return listExemption;
	}

	/**
	 * Method getExemptionByIdEJB
	 * Get an exemption case by exemptionId field from database
	 * @param intExemptionId
	 * @return ExemptionBean
	 * @throws SQLException
	 */
	public ExemptionBean getExemptionByIdEJB(int intExemptionId) throws SQLException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ExemptionBean beanExemption = new ExemptionBean();

		try	{
			StringBuffer strSQL = new StringBuffer();
			strSQL.append(" SELECT d.group_name, d.account, d.name, ");
			strSQL.append(" t.exemption_id, t.developer_id, t.type, t.weekday, ");
			strSQL.append(" TO_CHAR(t.from_date,'mm/dd/yy') as from_date, TO_CHAR(t.to_date,'mm/dd/yy') as to_date, t.reason, t.note ");
			strSQL.append(" FROM timesheet_exemption t, developer d ");
			strSQL.append(" WHERE t.developer_id = d.developer_id AND t.exemption_id = "+intExemptionId);

			//logger.debug(strClassName + ".getExemptionEJB(): strSQL = " + strSQL.toString());

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();

			if (rs.next()) {
				beanExemption.setExemptionId(intExemptionId);
				beanExemption.setGroup((rs.getString("group_name") == null) ? "" : rs.getString("group_name"));
				beanExemption.setAccount((rs.getString("account") == null) ? "" : rs.getString("account"));
				beanExemption.setFullName((rs.getString("name") == null) ? "" : rs.getString("name"));
				beanExemption.setDeveloperId(rs.getInt("developer_id"));
				beanExemption.setType(rs.getInt("type"));
				beanExemption.setWeekDay(rs.getString("weekday"));
				beanExemption.setFromDate((rs.getString("from_date") == null) ? "" : rs.getString("from_date"));
				beanExemption.setToDate((rs.getString("to_date") == null) ? "" : rs.getString("to_date"));
				beanExemption.setReason((rs.getString("reason") == null) ? "" : rs.getString("reason"));
				beanExemption.setNote((rs.getString("note") == null) ? "" : rs.getString("note"));
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getExemptionByIdEJB():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getExemptionByIdEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getExemptionByIdEJB():");
		}
		return beanExemption;
	}

	// Added by HaiMM =============================================================
	public ExemptionBean getDummyExemptionByIdEJB(int intDevId) throws SQLException {

			ResultSet rs = null;
			Connection con = null;
			PreparedStatement stmt = null;
			ExemptionBean beanExemption = new ExemptionBean();

			try	{
				StringBuffer strSQL = new StringBuffer();
				strSQL.append(" SELECT t.exemption_id ");
				strSQL.append(" FROM timesheet_exemption t ");
				strSQL.append(" WHERE t.DUMMY_FIELD = 1 AND t.developer_id = " + intDevId);

				if (ds == null) {
					ds = conPool.getDS();
				}
				con = ds.getConnection();
				stmt = con.prepareStatement(strSQL.toString());
				rs = stmt.executeQuery();

				if (rs.next()) {
					beanExemption.setExemptionId(rs.getInt("exemption_id"));
				}
			}
			catch (SQLException ex) {
				logger.error(strClassName + ".getDummyExemptionByIdEJB():" + ex.toString());
			}
			catch (Exception ex) {
				logger.error(strClassName + ".getDummyExemptionByIdEJB():" + ex.toString());
			}
			finally {
				conPool.releaseResource(con, stmt, rs, strClassName + ".getDummyExemptionByIdEJB():");
			}
			return beanExemption;
		}
		
	public void addDummyExemptionEJB(int intExemptionId, String status) throws SQLException {

		String strSQL = "";
		Connection con = null;
		PreparedStatement preStmt = null;
		// Modify by HaiMM
		try	{
			strSQL = "INSERT INTO EXEMPTION_MIGRATE(EXEMPTION_ID, type)" +
					 "VALUES(?, ?)";

			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			preStmt = con.prepareStatement(strSQL);

			preStmt.setInt(1, intExemptionId);
			preStmt.setString(2, status);

			preStmt.executeQuery();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".addDummyExemptionEJB():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".addDummyExemptionEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, preStmt, null, strClassName + ".addDummyExemptionEJB():");
		}
	}
	
	public void updateDummyExemptionEJB(int intExemptionId) throws SQLException {

			String strSQL = "";
			Connection con = null;
			PreparedStatement preStmt = null;

			try	{
				strSQL = " UPDATE timesheet_exemption SET DUMMY_FIELD = ? " +
						 " WHERE exemption_id =? ";

				
				if (ds == null) ds = conPool.getDS();
				con = ds.getConnection();
				preStmt = con.prepareStatement(strSQL);

				preStmt.setString(1, "");
				preStmt.setInt(2, intExemptionId);
				
				preStmt.executeQuery();
			}
			catch (SQLException ex) {
				logger.error(strClassName + ".updateDummyExemptionEJB():" + ex.toString());
			}
			catch (Exception ex) {
				logger.error(strClassName + ".updateDummyExemptionEJB():" + ex.toString());
			}
			finally {
				conPool.releaseResource(con, preStmt, null, strClassName + ".updateDummyExemptionEJB():");
			}
		}
	public Collection getDummyMigrationByIdEJB() throws SQLException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;
		List lstExemptionTimeSheet = new ArrayList();

		try	{
			StringBuffer strSQL = new StringBuffer();
			strSQL.append(" SELECT t.exemption_id ");
			strSQL.append(" FROM EXEMPTION_MIGRATE t ");

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				ExemptionBean beanExemption = new ExemptionBean();
				beanExemption.setExemptionId(rs.getInt("exemption_id"));

				lstExemptionTimeSheet.add(beanExemption);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getDummyExemptionByIdEJB():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getDummyExemptionByIdEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getDummyExemptionByIdEJB():");
		}
		return lstExemptionTimeSheet;
	}
	
	public void updateDummyMigrationEJB(int intExemptionId, String status) throws SQLException {

		String strSQL = "";
		Connection con = null;
		PreparedStatement preStmt = null;

		try	{
			strSQL = " UPDATE EXEMPTION_MIGRATE SET TYPE = ? " +
					 " WHERE exemption_id =? ";

		
			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			preStmt = con.prepareStatement(strSQL);

			preStmt.setString(1, status);
			preStmt.setInt(2, intExemptionId);
		
			preStmt.executeQuery();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".updateDummyExemptionEJB():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".updateDummyExemptionEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, preStmt, null, strClassName + ".updateDummyExemptionEJB():");
		}
	}
	// End ====================================================

	/**
	 * Method addExemptionEJB
	 * Add an exemption case into database
	 * @param intDevId
	 * @param intType
	 * @param intWeekDay
	 * @param strFromDate
	 * @param strToDate
	 * @param strReason
	 * @param strNote
	 * @throws SQLException
	 */
	public void addExemptionEJB(int intDevId, int intType, String strWeekDay,
								String strFromDate, String strToDate,
								String strReason, String strNote) throws SQLException {

		String strSQL = "";
		Connection con = null;
		PreparedStatement preStmt = null;
		// Modify by HaiMM
		try	{
			strSQL = "INSERT INTO timesheet_exemption(developer_id, type, weekday, from_date, to_date, reason, note, DUMMY_FIELD)" +
					 "VALUES(?, ?, ?, TO_DATE(?, 'mm/dd/yy'), TO_DATE(?, 'mm/dd/yy'), ?, ?, ?)";

			//logger.debug(strClassName + ".addExemptionEJB(): strSQL = " + strSQL.toString());

			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			preStmt = con.prepareStatement(strSQL);

			preStmt.setInt(1, intDevId);
			preStmt.setInt(2, intType);
			preStmt.setString(3, strWeekDay);
			preStmt.setString(4, strFromDate);
			preStmt.setString(5, strToDate);
			preStmt.setString(6, strReason);
			preStmt.setString(7, strNote);
			preStmt.setString(8, "1"); // HaiMM added here

			preStmt.executeQuery();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".addExemptionEJB():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".addExemptionEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, preStmt, null, strClassName + ".addExemptionEJB():");
		}
	}

	/**
	 * Method updateExemptionEJB
	 * Update an exemption case to database
	 * @param intExemptionId
	 * @param intDevId
	 * @param intType
	 * @param intWeekDay
	 * @param strFromDate
	 * @param strToDate
	 * @param strReason
	 * @param strNote
	 * @throws SQLException
	 */
	public void updateExemptionEJB(int intExemptionId, int intType, String strWeekDay,
								   String strFromDate, String strToDate, String strReason, String strNote) throws SQLException {

		String strSQL = "";
		Connection con = null;
		PreparedStatement preStmt = null;

		try	{
			strSQL = " UPDATE timesheet_exemption SET " +
					 " type = ? " +
					 " ,weekday = ? " +
					 ", from_date = TO_DATE(?, 'mm/dd/yy') " +
					 " ,to_date = TO_DATE(?, 'mm/dd/yy') " +
					 " ,reason = ? " +
					 " ,note = ? " +
					 " WHERE exemption_id =? ";

			//logger.debug(strClassName + ".updateExemptionEJB(): strSQL = " + strSQL.toString());

			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			preStmt = con.prepareStatement(strSQL);

			preStmt.setInt(1, intType);
			preStmt.setString(2, strWeekDay);
			preStmt.setString(3, strFromDate);
			preStmt.setString(4, strToDate);
			preStmt.setString(5, strReason);
			preStmt.setString(6, strNote);
			preStmt.setInt(7, intExemptionId);

			preStmt.executeQuery();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".updateExemptionEJB():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".updateExemptionEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, preStmt, null, strClassName + ".updateExemptionEJB():");
		}
	}

	/**
	 * Method deleteExemptionEJB
	 * Delete an exemption case from database
	 * @param intExemptionId
	 * @throws SQLException
	 */
	
	public void deleteExemptionEJB(String strExemptionId) throws SQLException {
		Integer iExemptionId;
		int intExemptionId = 0;

		String strSQL = "";
		Connection con = null;
		PreparedStatement prepStmt = null;

		try	{
			strSQL = "DELETE FROM timesheet_exemption WHERE exemption_id=?";

			//logger.debug(strClassName + ".deleteExemptionEJB(): strSQL = " + strSQL.toString());

			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL);

			iExemptionId = new Integer(strExemptionId);
			intExemptionId = iExemptionId.intValue();
			prepStmt.setInt(1, intExemptionId);

			prepStmt.executeUpdate();
		}
		catch (SQLException ex)	{
			logger.error(strClassName + ".deleteExemptionEJB():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".deleteExemptionEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, prepStmt, null, strClassName + ".deleteExemptionEJB():");
		}
	}
	
	public int getExemptionNewId(String strExemptionId)  throws SQLException {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;
		int id = 0 ; 
		try {
					String strSQL = "SELECT new_exemption_id FROM timesheet_exemption where exemption_id =  "+strExemptionId;

					if (ds == null) {
						ds = conPool.getDS();
					}
					con = ds.getConnection();
					stmt = con.prepareStatement(strSQL);
					rs = stmt.executeQuery();

					while (rs.next()) {
							id = rs.getInt("new_exemption_id");
					}
		}
		catch (SQLException ex) {
					logger.error(strClassName + ".getExemptionNewId():" + ex.toString());
				}
				catch (Exception ex) {
					logger.error(strClassName + ".getExemptionNewId():" + ex.toString());
				}
				finally {
					conPool.releaseResource(con, stmt, rs, strClassName + ".getExemptionNewId():");
				}
				return id;
	}
	/*
	 * Method getUserLogTS
	 * Get all users logged TS
	 * @param strGroup
	 * @param strSearchFromDate
	 * @param strSearchToDate
	 * @return Collection
	 * @throws SQLException
	 */
	public Vector getUserLogTS(String strGroup, String strLogDate, String strSearchFromDate, String strSearchToDate) throws SQLException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;
		StringBuffer strSQL = new StringBuffer();

		strSQL.append(" SELECT T.Developer_id, TO_CHAR (T.occur_date, 'mm/dd/yy') AS occur_date ");
		strSQL.append(" FROM timesheet T, Developer D ");
		strSQL.append(" WHERE T.developer_id = D.developer_id ");
		strSQL.append(" AND T.create_date <= to_date('");
		  strSQL.append(strLogDate);
		  strSQL.append("' , 'MM/DD/YY HH24:MI:SS')");
		strSQL.append(SqlUtil.genDateConstraint("T.occur_date",
				  strSearchFromDate, strSearchToDate));

		if (strGroup != null &&
			!"All".equals(strGroup) &&
			!"0".equals(strGroup)) {
		   strSQL.append(" AND D.group_name = '" + strGroup + "'");
		}
		else {
		   strSQL.append(" AND 1 = 1");
		}
		strSQL.append(" AND D.status IN (1, 2) ");
		
		if (strSearchToDate != null && strSearchToDate.trim().length() > 0) {
			strSQL.append(" AND D.begin_date <= TO_DATE('").append(strSearchToDate);
			strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
			strSQL.append("')");
		}
		strSQL.append(" AND TRIM(TO_CHAR(T.occur_date, 'DAY')) != 'SUNDAY'");

		logger.debug(strClassName + ".getUserLogTS(): strSQL = " + strSQL.toString());

		Vector vecUserLogTS = new Vector();
		//Hashtable hashExemption = new Hashtable();
		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();

			while (rs.next()) {
				ExemptionBean beanExemption = new ExemptionBean();

				beanExemption.setDeveloperId(rs.getInt("developer_id"));
				beanExemption.setOccurdate(rs.getString("occur_date"));

				putExemptionHashTable(beanExemption, vecUserLogTS);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getUserLogTS():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getUserLogTS():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getUserLogTS():");
		}
		return vecUserLogTS;
	}
	
	/**
	 * Method getTimesheetExemption
	 * Get information of all timesheet exemption cases
	 * @param strGroup
	 * @param strSearchFromDate
	 * @param strSearchToDate
	 * @return Collection
	 * @throws SQLException
	 */
	public Vector getTimesheetExemptionEJB(String strGroup, String strSearchFromDate, String strSearchToDate) throws SQLException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;
		StringBuffer strSQL = new StringBuffer();

		//GET PERMANENT TIMESHEET EXEMPTION INFO
		strSQL.append(" SELECT ");
		strSQL.append(" t.developer_id, d.group_name AS group_name, d.ACCOUNT AS ACCOUNT, ");
		strSQL.append(" d.NAME AS full_name, t.TYPE, t.weekday AS weekday, ");
		strSQL.append(" TO_CHAR (t.from_date, 'mm/dd/yy') AS from_date, ");
		strSQL.append(" TO_CHAR (t.to_date, 'mm/dd/yy') AS to_date ");
		strSQL.append(" FROM timesheet_exemption t, developer d ");
		strSQL.append(" WHERE t.developer_id = d.developer_id ");
		strSQL.append(" AND t.TYPE = 1 ");
		if (! "All".equals(strGroup)) {
			strSQL.append(" AND d.group_name = '" + strGroup +"'");
		}

		//GET TIMESHEET EXEMPTION INFO
		strSQL.append(" UNION ");
		strSQL.append(" SELECT "); 
		strSQL.append(" t.developer_id, d.group_name AS group_name, d.ACCOUNT AS ACCOUNT, ");
		strSQL.append(" d.NAME AS full_name, t.TYPE, t.weekday AS weekday, ");
		strSQL.append(" TO_CHAR (t.from_date, 'mm/dd/yy') AS from_date, ");
		strSQL.append(" TO_CHAR (t.to_date, 'mm/dd/yy') AS to_date ");
		strSQL.append(" FROM timesheet_exemption t, developer d ");
		strSQL.append(" WHERE t.developer_id = d.developer_id ");

		if (! "All".equals(strGroup)) {
			strSQL.append(" AND d.group_name = '" + strGroup +"'");
		}
		if (strSearchToDate != null && strSearchToDate.trim().length() > 0) {
			strSQL.append(" AND t.from_date <= TO_DATE('").append(strSearchToDate);
			strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
			strSQL.append("')");
		}
		if (strSearchFromDate != null && strSearchFromDate.trim().length() > 0) {
			strSQL.append(" AND t.to_date >= TO_DATE('").append(strSearchFromDate);
			strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
			strSQL.append("')");
		}
		//GET OFF, TRAINING INFO FROM OTHER ASSIGNMENTS
		strSQL.append(" UNION ");
		strSQL.append(" SELECT ");
		strSQL.append(" o.developer_id, d.group_name AS group_name, d.ACCOUNT AS ACCOUNT, ");
		strSQL.append(" d.name as full_name, o.type, null AS weekday, ");
		strSQL.append(" TO_CHAR (o.from_date, 'mm/dd/yy') AS from_date, ");
		strSQL.append(" TO_CHAR (o.end_date, 'mm/dd/yy') AS to_date ");
		strSQL.append(" FROM other_assignment o, developer d ");
		strSQL.append(" WHERE o.developer_id = d.developer_id ");

		if (! "All".equals(strGroup)) {
			strSQL.append(" AND d.group_name = '" + strGroup +"'");
		}
		if (strSearchToDate != null && strSearchToDate.trim().length() > 0) {
			strSQL.append(" AND o.from_date <= TO_DATE('").append(strSearchToDate);
			strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
			strSQL.append("')");
		}
		if (strSearchFromDate != null && strSearchFromDate.trim().length() > 0) {
			strSQL.append(" AND o.end_date >= TO_DATE('").append(strSearchFromDate);
			strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
			strSQL.append("')");
		}
		strSQL.append(" AND o.type IN (4, 5)");
		strSQL.append(" ORDER BY group_name, ACCOUNT, full_name ");

		//logger.debug(strClassName + ".getTimesheetExemptionEJB(): strSQL = " + strSQL.toString());

		Vector vecTimesheetExemption = new Vector();
		//Hashtable hashExemption = new Hashtable();
		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();

//			int count = 0;
			while (rs.next()) {
				ExemptionBean beanExemption = new ExemptionBean();
				beanExemption.setDeveloperId(rs.getInt("developer_id"));
				beanExemption.setGroup(rs.getString("group_name"));
				beanExemption.setAccount(rs.getString("account"));
				beanExemption.setFullName(rs.getString("full_name"));
				beanExemption.setType(rs.getInt("type"));
				beanExemption.setWeekDay(rs.getString("weekday"));
				beanExemption.setFromDate(rs.getString("from_date"));
				beanExemption.setToDate(rs.getString("to_date"));

				putExemptionHashTable(beanExemption, vecTimesheetExemption);
				
				//int intDevId = beanExemption.getDeveloperId();
				//hashExemption.put(new Integer(intDevId), oaInfo);
//				count ++;
			}
			//logger.debug(strClassName + ".getTimesheetExemptionEJB(): count=" + count);
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getTimesheetExemptionEJB():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getTimesheetExemptionEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getTimesheetExemptionEJB():");
		}
		return vecTimesheetExemption;
	}

	/**
	 * Put exemption information into a list of hash table. Because an user may
	 * has more than one exemption information so we should create a list of
	 * hash table in order to store all exemption information
	 * @param oaInfo
	 * @param vecExemption
	 */
	private void putExemptionHashTable(ExemptionBean beanExemption, Vector vecTimesheetExemption) {
		boolean isPut = false;
		Hashtable hashExemption = null;
		Integer integerDevId = new Integer(beanExemption.getDeveloperId());
		for (int i=0; i<vecTimesheetExemption.size(); i++) {
			hashExemption = (Hashtable) vecTimesheetExemption.get(i);
			if (hashExemption.get(integerDevId) == null) {
				hashExemption.put(integerDevId, beanExemption);
				isPut = true;   // Check this developer is put into an hashtable
				break;
			}
		}
		// Not put user into any hash table => create new hash table and put
		// the user info into hash table using user id as key
		if (!isPut) {
			Hashtable hashExemptionNew = new Hashtable();
			hashExemptionNew.put(integerDevId, beanExemption);
			vecTimesheetExemption.add(hashExemptionNew);
		}
	}
	

	/**
	 * Method isUserLogTS
	 * @param intDevId
	 * @param strCurrentDate
	 * @param vecUserLogTS
	 */
	private boolean isUserLogTS(int intDevId, String strCurrentDate, Vector vecUserLogTS) {

		boolean bResult = false;
		Integer integerDevId = new Integer(intDevId);
		ExemptionBean beanExemption = new ExemptionBean();

		try {
			for (int i=0; i<vecUserLogTS.size(); i++) {
				Hashtable hashExemption = (Hashtable) vecUserLogTS.get(i);
				if (hashExemption.get(integerDevId) != null) {
					beanExemption = (ExemptionBean) hashExemption.get(integerDevId);
					String strOccurDate = beanExemption.getOccurdate();
					if (DateUtils.CompareDate(strOccurDate,strCurrentDate) == 0) {
						bResult = true;
						break;
					}
				}
			}
		}
		catch(Exception ex) {
			logger.error(strClassName + ".isUserLogTS():" + ex.toString());
		}
		return bResult;
	}


	/**
	 * Method isUserBeginDate
	 * @param intDevId
	 * @param strCurrentDate
	 * @param vecUserBetween
	 */
	private boolean isUserBeginDate(int intDevId, String strCurrentDate, Vector vecUserBetween) {

		boolean bResult = false;
		Integer integerDevId = new Integer(intDevId);
		ExemptionBean beanExemption = new ExemptionBean();

		try {
//			logger.debug("vecUserBetween.size()=" + vecUserBetween.size());			 
			for (int i=0; i<vecUserBetween.size(); i++) {
				Hashtable hashExemption = (Hashtable) vecUserBetween.get(i);
				if (hashExemption.get(integerDevId) != null) {
					beanExemption = (ExemptionBean) hashExemption.get(integerDevId);
					String strBeginDate = beanExemption.getBegindate();
					if (DateUtils.CompareDate(strBeginDate,strCurrentDate) > 0) {
						bResult = true;
						break;
					}
				}
			}
		}
		catch(Exception ex) {
			logger.error(strClassName + ".isUserBeginDate():" + ex.toString());
		}
		return bResult;
	}


	/**
	 * Method isTimesheetFree
	 * @param intDevId
	 * @param strCurrentDate
	 * @param oaLeft
	 * @param oaRight
	 */
	private boolean isTimesheetExemption(int intDevId, String strCurrentDate, Vector vecExemption) {

		boolean bResult = false;
		Integer integerDevId = new Integer(intDevId);
		ExemptionBean beanExemption = new ExemptionBean();

		try {
			Calendar calData = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");

			int intWeekDay = 0;
			for (int i=0; i<vecExemption.size(); i++) {
				Hashtable hashExemption = (Hashtable) vecExemption.get(i);

				if (hashExemption.get(integerDevId) != null) {
					beanExemption = (ExemptionBean) hashExemption.get(integerDevId);

					String strExempFromDate = beanExemption.getFromDate();
					String strExempToDate   = beanExemption.getToDate();
					int intType = beanExemption.getType();
					String strWeekDay = beanExemption.getWeekDay();

					Date dtCurrentDate = formatter.parse(strCurrentDate);
					calData.setTime(dtCurrentDate);
					int intWeekDayCalendar = calData.get(Calendar.DAY_OF_WEEK);

					//Permanent, Off, Training
					if (intType == 2 || intType == 4 || intType == 5) {
						if (DateUtils.CompareDate(strCurrentDate, strExempFromDate) >= 0 &&
							DateUtils.CompareDate(strCurrentDate, strExempToDate)   <= 0) {
							bResult = true;
							break;
						}
					}
					//Weekly
					else if (intType == 3) {
						if (DateUtils.CompareDate(strCurrentDate, strExempFromDate) >= 0 &&
							DateUtils.CompareDate(strCurrentDate, strExempToDate)   <= 0)
						{
							for (int j = 0; j < strWeekDay.length(); j++) {
								if (strWeekDay.charAt(j) == '1') {
									intWeekDay = 1;
								}
								else if (strWeekDay.charAt(j) == '2') {
									intWeekDay = 2;
								}
								else if (strWeekDay.charAt(j) == '3') {
									intWeekDay = 3;
								}
								else if (strWeekDay.charAt(j) == '4') {
									intWeekDay = 4;
								}
								else if (strWeekDay.charAt(j) == '5') {
									intWeekDay = 5;
								}
								else if (strWeekDay.charAt(j) == '6') {
									intWeekDay = 6;
								}
								else if (strWeekDay.charAt(j) == '7') {
									intWeekDay = 7;
								}
								if (intWeekDay == intWeekDayCalendar) {
									bResult = true;
									break;  //for (int j = 0; j < strWeekDay.length(); j++)
								}
							}
						}
						if (bResult) {  // Found exemption by weekly
							break;  //for (int i=0; i<vecExemption.size(); i++)
						}
					} //end if Weekly
				} //end if
			} //end for
		} //end try
		catch(ParseException ex) {
			logger.error(strClassName + ".isTimesheetExemption():" + ex.toString());
		}
		return bResult;
	}

	/**
	 * Check the user is not needed to record timesheet (permanent exemption)
	 * @param developerId
	 * @param strFromDate
	 * @param strToDate
	 * @param vecExemption
	 * @return
	 */
	private boolean isPermanentExemption(int developerId,
		String strFromDate, String strToDate, Vector vecExemption)
	{
		boolean bResult = false;
		ExemptionBean beanExemption;
		Integer integerDevId = new Integer(developerId);
		for (int i=0; i<vecExemption.size(); i++) {
			Hashtable hashExemption = (Hashtable) vecExemption.get(i);
			// Found an exemption record
			if (hashExemption.get(integerDevId) != null) {
				beanExemption = (ExemptionBean) hashExemption.get(integerDevId);
				String strExempFromDate = beanExemption.getFromDate();
				String strExempToDate   = beanExemption.getToDate();
				int intType = beanExemption.getType();
				// Permanent exemption
				if (intType == 1) {
					bResult = true;
					break;
				}
				// Temporary, Off, Training that contains the whole searching period
				else if (intType == 2 || intType == 4 || intType == 5) {
					if ((strExempFromDate != null) && (strExempToDate != null) &&
						(strFromDate != null) && (strToDate != null) &&
						(DateUtils.CompareDate(strFromDate, strExempFromDate) >= 0) &&
						(DateUtils.CompareDate(strToDate, strExempToDate)   <= 0))
					{
						bResult = true;
						break;
					}
				}
			} //end if
		} //end for

		return bResult;
	}

	/**
	 * Uncheck users have begin_date between from_date and to_date
	 * @param smtLacked
	 * @param vecUserBetween
	 * @param arrWorkingDates
	 */
	private void uncheckUserBeginDateBetween(StringMatrix smtLacked,
		Vector vecUserBetween, String[] arrWorkingDates)
	{
		if (smtLacked != null) {
			for (int row = 0; row < smtLacked.getNumberOfRows(); row++) {
				int intDevId = Integer.parseInt(smtLacked.getCell(row, 3));
				for (int nWorkingDate = 0;
					nWorkingDate < arrWorkingDates.length; nWorkingDate++)
				{
					if ("X".equals(smtLacked.getCell(row, nWorkingDate + 4))) {
						if (isUserBeginDate(intDevId,
							arrWorkingDates[nWorkingDate], vecUserBetween))
						{
							smtLacked.setCell(row, nWorkingDate + 4, "");
						}
					}
				}
			}
		}
	}


	/**
	 * Uncheck exemption days of users following by exemption information
	 * @param smtLacked
	 * @param vecExemption
	 * @param arrWorkingDates
	 */
	private void uncheckExemptionDays(StringMatrix smtLacked,
		Vector vecExemption, String[] arrWorkingDates)
	{
		if (smtLacked != null) {
			// Check exemption for working days
			for (int row = 0; row < smtLacked.getNumberOfRows(); row++) {
				int intDevId = Integer.parseInt(smtLacked.getCell(row, 3));
//				  logger.debug("XXXXXXXXXXXXX: ExemptionEJB.java - uncheckExemptionDays(): inDevId = " + intDevId );
				for (int nWorkingDate = 0;
					nWorkingDate < arrWorkingDates.length; nWorkingDate++)
				{
					// Un-check lacked days only
					if ("X".equals(smtLacked.getCell(row, nWorkingDate + 4))) {
						// Uncheck exemption days of users
						if (isTimesheetExemption(intDevId,
							arrWorkingDates[nWorkingDate], vecExemption))
						{
							// The first four columns are user information,
							// the rest are tracking records represented by X
							smtLacked.setCell(row, nWorkingDate + 4, "");
						}
					}
				}
			}
		}
	}


	//HaiMM
	/**
	 * Uncheck users logged TS
	 * @param smtLacked
	 * @param vecUserLogTS
	 * @param arrWorkingDates
	 */
	private void uncheckUserLogTS(StringMatrix smtLacked,
		Vector vecUserLogTS, String[] arrWorkingDates)
	{
		if (smtLacked != null) {
			for (int row = 0; row < smtLacked.getNumberOfRows(); row++) {
				int intDevId = Integer.parseInt(smtLacked.getCell(row, 3));
				for (int nWorkingDate = 0;
					nWorkingDate < arrWorkingDates.length; nWorkingDate++)
				{
					if ("X".equals(smtLacked.getCell(row, nWorkingDate + 4))) {
						if (isUserLogTS(intDevId,
							arrWorkingDates[nWorkingDate], vecUserLogTS))
						{
							smtLacked.setCell(row, nWorkingDate + 4, "");
						}
					}
				}
			}
		}
	}

	/**
	 * Remove users that are permanently exempted from timesheet recording
	 * @param smtLacked
	 * @param vecExemption
	 * @param arrWorkingDates
	 */
	private void removePermanentExemption(StringMatrix smtLacked,
		Vector vecExemption, String[] arrWorkingDates)
	{
		// Remove users that are permanently exempted
		for (int row = smtLacked.getNumberOfRows() - 1; row >= 0; row--) {
			int intDevId = Integer.parseInt(smtLacked.getCell(row, 3));
			if (isPermanentExemption(intDevId, arrWorkingDates[0],
				arrWorkingDates[arrWorkingDates.length-1], vecExemption))
			{
				smtLacked.removeRow(row);
			}
		}
	}

	/**
	 * Remove users that not lacked TS after apply all exemption rules
	 * @param smtLacked
	 */
	private void removeNotLackedTS(StringMatrix smtLacked) {
		if (smtLacked != null) {
			int count;
			for (int row = smtLacked.getNumberOfRows() - 1; row >= 0; row--) {
				count = 0;
				for (int col=4; col < smtLacked.getNumberOfCols(); col++){
					if (smtLacked.getCell(row, col).equals("X")) {
						count++;
					}
				}
				if (count == 0) {
					smtLacked.removeRow(row);
				}
			}
		}
	}

	/*
	 * Generate project filter condition of select timesheet records based on
	 * project id and project status
	 * @param sbSql SQL string buffer
	 * @param formRequest input parameters include project id and project status
	 * @param projectTbAlias Project table alias in the query
	private void appendProjectCondition(StringBuffer sbSql,
		TrackingByProjectForm formRequest, String tableAlias)
	{
		if (formRequest.getProjectId() > 0) {
			sbSql.append(" AND " + tableAlias + ".project_id=");
			sbSql.append(formRequest.getProjectId());
		}
		if (formRequest.getProjectStatus() >= 0) {
			sbSql.append(" AND " + tableAlias + ".status=");
			sbSql.append(formRequest.getProjectStatus());
		}
	}
	 */
	
	/**
	 * Method getUserBeginDateBetween
	 * Get information of user have begin_date beween from_date and to_date
	 * @param strGroup
	 * @param strSearchFromDate
	 * @param strSearchToDate
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector getUserBeginDateBetween(String strGroup, String strSearchFromDate, String strSearchToDate) throws SQLException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;
		StringBuffer strSQL = new StringBuffer();

		strSQL.append(" SELECT D.developer_id, ");
		strSQL.append(" TO_CHAR (D.begin_date, 'mm/dd/yy') AS begin_date");
		strSQL.append(" FROM developer d ");
		strSQL.append(" WHERE ");

		if (strGroup != null &&
			!"All".equals(strGroup) &&
			!"0".equals(strGroup)) {
		   strSQL.append(" D.group_name = '" + strGroup + "'");
		}
		else {
		   strSQL.append(" 1 = 1");
		}
		strSQL.append(" AND D.status IN (1, 2) ");
		
		if (strSearchToDate != null && strSearchToDate.trim().length() > 0) {
			strSQL.append(" AND D.begin_date <= TO_DATE('").append(strSearchToDate);
			strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
			strSQL.append("')");
		}

		if (strSearchFromDate != null && strSearchFromDate.trim().length() > 0) {
			strSQL.append(" AND D.begin_date >= TO_DATE('").append(strSearchFromDate);
			strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
			strSQL.append("')");
		}

//		logger.debug(strClassName + ".getUserBeginDateBetween(): strSQL = " + strSQL.toString());
		
		Vector vecUserBeginDate = new Vector();

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();

			while (rs.next()) {
				ExemptionBean beanExemption = new ExemptionBean();
				
				beanExemption.setDeveloperId(rs.getInt("developer_id"));
				beanExemption.setBegindate(rs.getString("begin_date"));

				putExemptionHashTable(beanExemption, vecUserBeginDate);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getUserBeginDateBetween():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getUserBeginDateBetween():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getUserBeginDateBetween():");
		}
		return vecUserBeginDate;
	}

	/**
	 * Exclude users that began to work later than the checking period
	 * @param sbSql SQL string buffer
	 * @param formRequest input parameters include To Date parameter
	 * @param developerTbAlias Developer table alias in the query
	 */
	private void appendUserBeginDateCondition(StringBuffer sbSql,
		TrackingByProjectForm formRequest, String tableAlias)
	{
		if (formRequest.getToDate().trim().length() > 0) {
			sbSql.append(" AND " + tableAlias + ".begin_date <= TO_DATE('");
			sbSql.append(formRequest.getToDate());
			sbSql.append("','").append(Timesheet.SQL_DATE_FORMAT);
			sbSql.append("')");
		}
	}

	/*
	 * Get users that began to work between searching period, this method will
	 * be used to avoid tracking days that users have not began to work
	 * @param strFromDate
	 * @param strToDate
	 * @return
	private Hashtable getUsersBeganBetween(String strFromDate, String strToDate) {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;
		StringBuffer strSQL = new StringBuffer();
		Hashtable userHashTb = new Hashtable();

		return userHashTb;
	}
	 */

	/**
	 * Method getTrackingReportEJB
	 * @param strGroup
	 * @param strFromDate
	 * @param strToDate
	 * @param strLogDateTime
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getTrackingReportEJB(TrackingByProjectForm formRequest)
		throws SQLException
	{
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;

		ArrayList arrReturn = new ArrayList();
		StringMatrix smtLacked = new StringMatrix();
		String[] arrWorkingDates = null;

		try {
			arrWorkingDates = DateUtils.getArrayOfWorkingDates(
				formRequest.getFromDate(), formRequest.getToDate());
		}
		catch (Exception eMemory) {
			return null;
		}
		int nNumberWorkingDates = arrWorkingDates.length;

		try {
			StringBuffer strNoneSQL = new StringBuffer();
			strNoneSQL.append("SELECT D.developer_id, D.group_name, D.name as full_name, ");
			strNoneSQL.append(" UPPER(D.account) as ORDER_ACCOUNT, ");
			strNoneSQL.append(" UPPER(D.group_name) as ORDER_GROUP ");
			strNoneSQL.append(" FROM developer D");
			strNoneSQL.append(" WHERE ");
			if (formRequest.getGroup() != null &&
				!"ALL".equals(formRequest.getGroup().trim().toUpperCase()) &&
				!"0".equals(formRequest.getGroup().trim().toUpperCase())) {
			   strNoneSQL.append(" D.group_name = '" + formRequest.getGroup() + "'");
			}
			else {
			   strNoneSQL.append(" 1 = 1");
			}
			strNoneSQL.append(" AND D.status IN (1, 2) "); //staff, or collaborator

			// Exclude users that began to work later than the checking period
			appendUserBeginDateCondition(strNoneSQL, formRequest, "D");
			strNoneSQL.append(" ORDER BY ORDER_GROUP, ORDER_ACCOUNT");

			logger.debug(strClassName + ".getTrackingReportEJB(): strSQL	 = " + strNoneSQL.toString());

			int intIndex;
			boolean bInsert;
			String strCurrentDev;
			String strNextDev;
			String strCurrentDate;
			String strOccurDate;

			StringVector svtRow = null;

			Vector vecUserLogTS = getUserLogTS(
					formRequest.getGroup(), formRequest.getLogDateTime(), formRequest.getFromDate(),
					formRequest.getToDate());

			Vector vecExemption = getTimesheetExemptionEJB(
					formRequest.getGroup(), formRequest.getFromDate(),
					formRequest.getToDate());
                    
			Vector vecUserBetween = getUserBeginDateBetween(
					formRequest.getGroup(), formRequest.getFromDate(),
					formRequest.getToDate());
                  
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.createStatement();
			stmt.setFetchSize(100);
			
			rs = stmt.executeQuery(strNoneSQL.toString());
		
			if (rs.next()) {
			
				while (!rs.isAfterLast()) {
						intIndex = 0;
						bInsert = false;

						String strGroupName = rs.getString("GROUP_NAME");
						String strFullName = rs.getString("FULL_NAME");
						String strAccount = rs.getString("ORDER_ACCOUNT");
						String strDeveloperID = rs.getString("DEVELOPER_ID");

						strNextDev = strCurrentDev = rs.getString("ORDER_ACCOUNT");
						//Data
						svtRow = new StringVector(nNumberWorkingDates + 0x04);
						svtRow.setCell(0x00, strGroupName);
						svtRow.setCell(0x01, strAccount);
						svtRow.setCell(0x02, strFullName);
						svtRow.setCell(0x03, strDeveloperID);

						strCurrentDate = arrWorkingDates[0];

						while (!rs.isAfterLast() && (strCurrentDev.equals(strNextDev)))
						{
							while ((intIndex < nNumberWorkingDates))
							{
									svtRow.setCell(intIndex + 4, "X"); //2: ID and Account fields
									bInsert = true;
									intIndex++;
								if (intIndex < nNumberWorkingDates) {
								   strCurrentDate = arrWorkingDates[intIndex];
								}
							}
							
							intIndex++;

							if (intIndex < nNumberWorkingDates) {
								strCurrentDate = arrWorkingDates[intIndex];
							}
							if (rs.next()) {
								strNextDev = rs.getString("ORDER_ACCOUNT");
							}
							else {
								strNextDev = "";
							}
							if (!strCurrentDev.equals(strNextDev)) {
								while (intIndex < nNumberWorkingDates) {
									//if (! isTimesheetExemption(intDevId, arrWorkingDates[intIndex], vecExemption)) {
										svtRow.setCell(intIndex + 4, "X");
										bInsert = true;
										intIndex++;
									//}
								}
							}
						}//end while
						if (bInsert) {
							smtLacked.addRow(svtRow);
						}
				} //end while
			}
			rs.close(); 
			stmt.close();

			// Uncheck users that logged TS
			uncheckUserLogTS(smtLacked, vecUserLogTS, arrWorkingDates);

			// Remove users that permanently exempted
			removePermanentExemption(smtLacked, vecExemption, arrWorkingDates);

			// Un-check exemption days from tracking records
			uncheckExemptionDays(smtLacked, vecExemption, arrWorkingDates);
            
			//Un-check users have begin_date between from_date and to_date
			uncheckUserBeginDateBetween(smtLacked, vecUserBetween, arrWorkingDates);
			
			// Remove not lacked TS users
			removeNotLackedTS(smtLacked);

			//Title --> [Group name] [Account] [Full name] [Total] 08/12/06 08/14/06 08/15/06 08/16/06 08/17/06 08/18/06
			StringVector svtTitledRow = new StringVector(nNumberWorkingDates + 0x04);
			svtTitledRow.setCell(0x00, "Group name");
			svtTitledRow.setCell(0x01, "Account");
			svtTitledRow.setCell(0x02, "Full name");
			svtTitledRow.setCell(0x03, "Total number of TS lacking days");

			for (int nWorkingDate = 0x00; nWorkingDate < nNumberWorkingDates; nWorkingDate++) {
				svtTitledRow.setCell(nWorkingDate + 0x04, arrWorkingDates[nWorkingDate]);
			}
			
			if (smtLacked.getNumberOfRows() > 0) {
				smtLacked.insertRow(0x00, svtTitledRow);
			}
			else {
				smtLacked.addRow(svtTitledRow);
			}
			arrReturn.add(smtLacked);
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getTrackingReportEJB():" + ex.toString());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getTrackingReportEJB():" + ex.toString());
			ex.printStackTrace();
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getTrackingReportEJB():");
		} //finally
		return arrReturn;
	}

	/**
	 * Method getArrAssignment
	 * @param strGroup
	 * @param intProjectID
	 * @param arrProjectIDs
	 * @param intProjectStatus
	 * @param strFromDate
	 * @param strToDate
	 * @return arrList
	 * @throws SQLException
	 */
	public Collection getArrAssignment(String strGroup, int intProjectID,
									   String arrProjectIDs, int intProjectStatus,
									   String strFromDate, String strToDate) throws SQLException {
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;

		String[] strProject;
		String strProjectSQL;
		StringBuffer strSQL = new StringBuffer();
		ArrayList arrList = new ArrayList();

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();

			strSQL.append(" SELECT DISTINCT a.project_id AS PROJECT_ID, a.developer_id AS DEV_ID, d.name AS DEV_NAME, d.account AS ACCOUNT, p.code AS PROJECT_CODE")
				  .append(" FROM assignment a, developer d, project p ")
				  .append(" WHERE a.project_id = p.project_id AND a.developer_id = d.developer_id ")
				  .append(" AND p.group_name = '" + strGroup + "'");

			if (intProjectID != 0) {
				strSQL.append(" AND p.project_id = " + intProjectID);
			}
			else {
				//strSQL.append(" AND p.project_id IN (" + arrProjectIDs + ")");
				//HanhTN fix maximum 1000 records -- 03/05/2007
				strProject = CommonUtil.split(arrProjectIDs, ",");
				for (int i=0; i<strProject.length; i++) {
					strProjectSQL = "INSERT INTO project_temp (project_id) VALUES ("+ strProject[i] +")";
					stmt.addBatch(strProjectSQL);
				}
				stmt.executeBatch();
				strSQL.append(" AND p.project_id IN (select project_id from project_temp)");
			}

			if (intProjectStatus == -1) {
				strSQL.append(" AND ( (p.status IN (0, 3)) OR ( (p.status = 1) AND ( ");
				if (strFromDate.trim().length() > 0) {
					strSQL.append(" p.actual_finish_date >= TO_DATE('").append(strFromDate);
					strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
					strSQL.append("')");
				}
				if (strToDate.trim().length() > 0) {
					strSQL.append(" AND p.actual_finish_date <= TO_DATE('").append(strToDate);
					strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
					strSQL.append("')");
				}
				strSQL.append(" ) OR ( ");
				if (strFromDate.trim().length() > 0) {
					strSQL.append(" p.actual_finish_date >= TO_DATE('").append(strFromDate);
					strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
					strSQL.append("')");
				}
				if (strToDate.trim().length() > 0) {
					strSQL.append(" AND p.actual_finish_date <= TO_DATE('").append(strToDate);
					strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
					strSQL.append("')");
				}
				strSQL.append(" ) ) ) ");
			}
			else if (intProjectStatus == 1) {
				strSQL.append(" AND ( (p.status = 1) AND ( ");
				if (strFromDate.trim().length() > 0) {
					strSQL.append(" p.actual_finish_date >= TO_DATE('").append(strFromDate);
					strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
					strSQL.append("')");
				}
				if (strToDate.trim().length() > 0) {
					strSQL.append(" AND p.actual_finish_date <= TO_DATE('").append(strToDate);
					strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
					strSQL.append("')");
				}
				strSQL.append(" ) OR ( ");
				if (strFromDate.trim().length() > 0) {
					strSQL.append(" p.actual_finish_date >= TO_DATE('").append(strFromDate);
					strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
					strSQL.append("')");
				}
				if (strToDate.trim().length() > 0) {
					strSQL.append(" AND p.actual_finish_date <= TO_DATE('").append(strToDate);
					strSQL.append("','").append(Timesheet.SQL_DATE_FORMAT);
					strSQL.append("')");
				}
				strSQL.append(" ) ) ");
			}
			else {
				strSQL.append(" AND p.status = "+intProjectStatus);
			}
			//logger.debug(strClassName + ".getAssignment(): strSQL = " + strSQL.toString());

			rs = stmt.executeQuery(strSQL.toString());
			while (rs.next()) {
				AssignmentInfo lackInfo = new AssignmentInfo();
				lackInfo.setProjectID(rs.getInt("PROJECT_ID"));
				lackInfo.setDeveloperID(rs.getInt("DEV_ID"));
				lackInfo.setDeveloperName(rs.getString("DEV_NAME"));
				lackInfo.setAccount(rs.getString("ACCOUNT"));
				lackInfo.setProjectCode(rs.getString("PROJECT_CODE"));
				arrList.add(lackInfo);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getArrAssignment():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getArrAssignment():" + ex.toString());
		}
		finally {
			conPool.releaseResourceAndCommit(con, stmt, rs, strClassName + ".getArrAssignment():");
		}
		return arrList;
	}

	/**
	 * Method getVctAssignment
	 * @return vctList
	 * @throws SQLException
	 */
	public Vector getVctAssignment() throws SQLException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement stmt = null;
		String strSQL = "";

		Vector vctList = new Vector();
		strSQL = "SELECT a.project_id, a.developer_id, d.name, d.account, p.code " +
				 "FROM assignment a, developer d, project p " +
				 "WHERE a.project_id = p.project_id and a.developer_id = d.developer_id ";

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strSQL);
			rs = stmt.executeQuery();

			String project_id = Integer.toString(rs.getInt("project_id"));
			String developer_id = Integer.toString(rs.getInt("developer_id"));
			String full_name = rs.getString("name");
			String account = rs.getString("account");
			String project_code = rs.getString("code");

			while (rs.next()) {
				vctList.add(project_id == null ? "" : project_id);
				vctList.add(developer_id == null ? "" : developer_id);
				vctList.add(full_name == null ? "" : full_name);
				vctList.add(account == null ? "" : account);
				vctList.add(project_code == null ? "" : project_code);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getVctAssignment():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getVctAssignment():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getVctAssignment():");
		}
		return vctList;
	}
}