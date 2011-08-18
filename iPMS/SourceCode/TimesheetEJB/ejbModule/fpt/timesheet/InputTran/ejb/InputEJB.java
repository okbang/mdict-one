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
 
 //***************************************************************************
// InputEJB.java
//
// Created by  : ThanhSKID
// Created date  : July 27, 2001
// Updated by  : Nguyen Thai Son (Nov 09, 2001)
// Updated by  : ThanhSKID (Dec 25, 2001)
//***************************************************************************
package fpt.timesheet.InputTran.ejb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import fpt.timesheet.ApproveTran.ejb.common.ProjectComboModel;
import fpt.timesheet.constant.Timesheet;
import fpt.timesheet.framework.connection.WSConnectionPooling;
import fpt.timesheet.framework.util.CommonUtil.CommonUtil;
import fpt.timesheet.framework.util.SqlUtil.SqlUtil;
import fpt.timesheet.framework.util.StringUtil.StringMatrix;
import fpt.timesheet.framework.util.StringUtil.StringVector;

public class InputEJB implements SessionBean {

	private static final Logger logger = Logger.getLogger(InputEJB.class);
	//*******************************************
	// Internal properties
	private Connection con;
	private SessionContext ctx;
	private WSConnectionPooling conPool = new WSConnectionPooling();
	private DataSource ds = null;
	//*******************************************
	private String strClassName = "InputEJB";

	private int nCurrentPage;
	private int nTotalPage = 0;
	private int nTotalTimesheet = 0;

	/**
	 * @see javax.ejb.SessionBean#setSessionContext(SessionContext)
	 */
	//*******************************************
	// Required methods
	public void setSessionContext(SessionContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * Method ejbCreate.
	 * @throws CreateException
	 */
	public void ejbCreate() throws CreateException {
		try {
			ds = conPool.getDS();
		}
		catch (Exception ex) {
			throw new CreateException(ex.getMessage());
		}
	}

	public void ejbRemove() {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	//*******************************************
	/**
	 * Method addTimeSheetLine
	 * @param intDevID
	 * @param intProject
	 * @param intProcess
	 * @param intTypeofWork
	 * @param intWorkProduct
	 * @param fltDuration
	 * @param strDate
	 * @param strDescription
	 * @throws SQLException
	 */
	public void addTimeSheetLine(int intDevID, int intProject, int intProcess, 
								 int intTypeofWork, int intWorkProduct, float fltDuration, 
								 String strDate, String strDescription) throws SQLException {
		String strSQL = null;		
		Connection con = null;
		PreparedStatement stm = null;
		
		// HaiMM add dummy_field for migration 08-Aug-2008 
		try {
			strSQL = " INSERT INTO TimeSheet(DEVELOPER_ID, " +
										   " PROJECT_ID, " +
										   " CREATE_DATE, " +
										   " OCCUR_DATE, " +
										   " DURATION, " +
										   " STATUS, " +
										   " DESCRIPTION, " +
										   " TOW_ID, " +
										   " WP_ID, " +
										   " PROCESS_ID, " +
										   " DUMMY_FIELD) " + // add here
					 " VALUES(" +
							" ?, ?, " +
							" TO_DATE(TO_CHAR(SYSDATE,'DD-MON-YYYY HH24:MI:SS'),'DD-MON-YYYY HH24:MI:SS'), " +
							" TO_DATE(?, 'mm/dd/yy'), ?, ?, ?, ?, ?, ?, ?" +  // HaiMM add here
							" ) ";

			//logger.debug("@HanhTN - EJB.InputEJB.addTimesheetLine: strSQL == " + strSQL.toString());

			if (ds == null) {
				ds = conPool.getDS(); 
			} 
			con = ds.getConnection();
			stm = con.prepareStatement(strSQL);

			stm.setInt(1, intDevID);
			stm.setInt(2, intProject);
			stm.setString(3, strDate);
			stm.setFloat(4, fltDuration);
			stm.setInt(5, 1); //strStatus
			stm.setString(6, strDescription);
			stm.setInt(7, intTypeofWork);
			stm.setInt(8, intWorkProduct);
			stm.setInt(9, intProcess);
			stm.setString(10, "1"); // HaiMM add here
			stm.executeQuery();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".addTimeSheetLine():" + ex.toString());
            logger.debug("InputEJB.addTimesheetLine: strSQL == " + strSQL.toString());
			throw ex;
		}
		catch (Exception ex) {
			logger.error("Exception occurs in InputEJB.addTimeSheetLine(). " + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stm, null, strClassName + ".addTimeSheetLine():");
		}
	}


	/***********************************************************************************
	 * Added by HaiMM - 08/Aug/08
	 * Method addTimeSheetDummyLine
	 */
	public void addTimeSheetDummyLine(int intDevID, String status) throws SQLException {
		String strSQL = null;		
		Connection con = null;
		PreparedStatement stm = null;
		
		try {
			strSQL = " INSERT INTO TIMESHEET_MIGRATE(TIMESHEET_ID, TYPE) " +
					 " VALUES(?, ?) ";

			if (ds == null) {
				ds = conPool.getDS(); 
			} 
			con = ds.getConnection();
			stm = con.prepareStatement(strSQL);

			stm.setInt(1, intDevID);
			if (status.equalsIgnoreCase("INSERT")) {
				stm.setString(2, "INSERT");	
			} else if (status.equalsIgnoreCase("UPDATE")) {
				stm.setString(2, "UPDATE");
			} else if (status.equalsIgnoreCase("DELETE")) {
				stm.setString(2, "DELETE");
			} else stm.setString(2, "DELETE1"); 
			
			stm.executeQuery();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".addTimeSheetDummyLine():" + ex.toString());
			logger.debug("InputEJB.addTimeSheetDummyLine: strSQL == " + strSQL.toString());
			throw ex;
		}
		catch (Exception ex) {
			logger.error("Exception occurs in InputEJB.addTimeSheetDummyLine(). " + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stm, null, strClassName + ".addTimeSheetDummyLine():");
		}
	}
	
	public void updateTimeSheetDummyLine(int intTimeSheetID) throws SQLException {
		String strSQL = null;
		Connection con = null;
		PreparedStatement stm = null;
		
		try {
			strSQL = " UPDATE TimeSheet SET DUMMY_FIELD = ? " +
					 " WHERE TIMESHEET_ID=? ";

			
			if (ds == null) {
				ds = conPool.getDS(); 
			} 
			con = ds.getConnection();
			stm = con.prepareStatement(strSQL);

			stm.setString(1, "");
			stm.setLong(2, intTimeSheetID);

			stm.executeQuery();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".updateTimeSheetDummyLine():" + ex.toString());
			logger.debug("InputEJB.updateTimeSheetDummyLine: strSQL == " + strSQL.toString());
			throw ex;
		}
		catch (Exception ex) {
			logger.error("Exception occurs in InputEJB.updateTimeSheetDummyLine(). " + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stm, null, strClassName + ".updateTimeSheetDummyLine():");
		}
	}
		
	public void updateTimeSheetMigrateLine(int intTimeSheetID, String status) throws SQLException {
		String strSQL = null;
		Connection con = null;
		PreparedStatement stm = null;
	
		try {
			strSQL = " UPDATE TIMESHEET_MIGRATE SET TYPE = ? " +
					 " WHERE TIMESHEET_ID=? ";

		
			if (ds == null) {
				ds = conPool.getDS(); 
			} 
			con = ds.getConnection();
			stm = con.prepareStatement(strSQL);

			stm.setString(1, status);
			stm.setLong(2, intTimeSheetID);

			stm.executeQuery();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".updateTimeSheetMigrateLine():" + ex.toString());
			logger.debug("InputEJB.updateTimeSheetMigrateLine: strSQL == " + strSQL.toString());
			throw ex;
		}
		catch (Exception ex) {
			logger.error("Exception occurs in InputEJB.updateTimeSheetMigrateLine(). " + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stm, null, strClassName + ".updateTimeSheetMigrateLine():");
		}
	}
		
	/************************************** END ***********************************************/
	
	/**
	 * Method updateTimeSheetLine
	 * @param intTimeSheetID
	 * @param intProject
	 * @param intProcess
	 * @param intTypeofWork
	 * @param intWorkProduct
	 * @param fltDuration
	 * @param strDate
	 * @param strDescription
	 * @throws SQLException
	 */
	public void updateTimeSheetLine(int intTimeSheetID, int intProject, int intProcess, 
									int intTypeofWork, int intWorkProduct, float fltDuration, 
									String strDate, String strDescription) throws SQLException {
		String strSQL = null;
		Connection con = null;
		PreparedStatement stm = null;
		// Modify by HaiMM for migration --> add batch
		try {
			strSQL = " UPDATE TimeSheet SET " +
											" PROJECT_ID = ?, " +
											" OCCUR_DATE = TO_DATE(?, 'mm/dd/yy'), " +
											" DURATION = ?, " +
											" STATUS = ?, " +
											" DESCRIPTION = ?, " +
											" TOW_ID = ?, " +
											" WP_ID = ?, " +
											" PROCESS_ID = ? " +
					 " WHERE TIMESHEET_ID=? AND STATUS IN (1,5) ";

			//logger.debug("@HanhTN - EJB.InputEJB.updateTimeSheetLine: strSQL == " + strSQL.toString());

			if (ds == null) {
				ds = conPool.getDS(); 
			} 
			con = ds.getConnection();
			stm = con.prepareStatement(strSQL);

			stm.setInt(1, intProject);
			stm.setString(2, strDate);
			stm.setFloat(3, fltDuration);
			stm.setInt(4, 1); //strStatus
			stm.setString(5, strDescription);
			stm.setInt(6, intTypeofWork);
			stm.setInt(7, intWorkProduct);
			stm.setInt(8, intProcess);
			stm.setLong(9, intTimeSheetID);

			stm.executeQuery();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".updateTimeSheetLine():" + ex.toString());
            logger.debug("InputEJB.updateTimeSheetLine: strSQL == " + strSQL.toString());
			throw ex;
		}
		catch (Exception ex) {
			logger.error("Exception occurs in InputEJB.updateTimeSheetLine(). " + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stm, null, strClassName + ".addTimeSheetLine():");
		}
	}

	
	public int getTimeSheetsById(String strTimeSheetID) throws SQLException {
		String strSQL = null;
		Statement stm = null;
		Connection con = null;
		ResultSet rs = null;
		int data = 0  ; 
		try {
			strSQL = "SELECT new_timesheet_id from TimeSheet WHERE TIMESHEET_ID = " + strTimeSheetID;
			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			stm = con.createStatement();
			rs = stm.executeQuery(strSQL);
			while (rs.next()){
				//data.addElement(rs.getInt("new_timesheet_id"));
				data = rs.getInt("new_timesheet_id") ; 
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getTimeSheetsById():" + ex.toString());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.error("Exception occurs in InputEJB.getTimeSheetsById(). " + ex.toString());
			ex.printStackTrace();
		}
		finally {
			conPool.releaseResource(con, stm, rs, strClassName + ".getTimeSheetsById():");
		}	
		return data;
	}
	/**
	 * Method deleteTimeSheet
	 * @param strTimeSheetIDList
	 * @throws SQLException
	 */
	public void deleteTimeSheet(String strTimeSheetIDList) throws SQLException {
		String strSQL = null;
		Statement stm = null;
		Connection con = null;

		try {
			strSQL = "DELETE FROM TimeSheet WHERE TIMESHEET_ID IN (" + strTimeSheetIDList + ")";

			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			stm = con.createStatement();

			stm.executeQuery(strSQL);
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".deleteTimeSheet():" + ex.toString());
			ex.printStackTrace();
			throw ex;
		}
		catch (Exception ex) {
			logger.error("Exception occurs in InputEJB.deleteTimeSheet(). " + ex.toString());
			ex.printStackTrace();
		}
		finally {
			conPool.releaseResource(con, stm, null, strClassName + ".getTimesheetExemptionEJB():");
		}
	}

	/**
	 * Method getTimeSheetList.
	 * @param sIDTimeSheetList
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getTimeSheetList(String sIDTimeSheetList) throws SQLException {
		String strSQL = "";
		List lstTimeSheet = new ArrayList();
		Statement st = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			st = con.createStatement();

			strSQL = " SELECT TimeSheet.*, Project.Code AS ProjectCode, Developer.Account AS UserName, " +
					 " Process.Name AS ProcessName, TypeOfWork.Name AS TypeOfWorkName, " +
					 " WorkProduct.Name AS WorkProductName, DeveloperTemp.ROLE AS LeaderRole " +
					 " FROM TimeSheet, Developer, Project, Process, TypeOfWork, WorkProduct, Developer DeveloperTemp " + 
					 " WHERE TimeSheet.DEVELOPER_ID = Developer.DEVELOPER_ID(+) " + 
					 " AND TimeSheet.APPROVED_BY_LEADER = DeveloperTemp.Account(+) " + 
					 " AND TimeSheet.PROJECT_ID = Project.PROJECT_ID(+) " + 
					 " AND TimeSheet.PROCESS_ID = Process.PROCESS_ID(+) " + 
					 " AND TimeSheet.TOW_ID = TypeOfWork.TOW_ID(+) " + 
					 " AND TimeSheet.WP_ID = WorkProduct.WP_ID(+) " + 
					 " AND TimeSheet.TIMESHEET_ID IN (" + sIDTimeSheetList + ")";
			rs = st.executeQuery(strSQL);

			//logger.debug("@HanhTN - EJB.InputEJB.getTimeSheetList(): strSQL 1 == " + strSQL);

			while (rs.next()) {
				TimeSheetInfo timeSheetInfo = new TimeSheetInfo();
				timeSheetInfo.setTimeSheetID(rs.getInt("TIMESHEET_ID"));
				timeSheetInfo.setDate(dateToString(rs.getDate("OCCUR_DATE"), "MM/dd/yy"));
				timeSheetInfo.setProject(rs.getInt("PROJECT_ID"));
				timeSheetInfo.setProjectName(rs.getString("ProjectCode"));
				timeSheetInfo.setProcess(rs.getInt("PROCESS_ID"));
				timeSheetInfo.setProcessName(rs.getString("ProcessName"));
				timeSheetInfo.setTypeofWork(rs.getInt("TOW_ID"));
				timeSheetInfo.setTypeofWorkName(rs.getString("TypeOfWorkName"));
				timeSheetInfo.setWorkProduct(rs.getInt("WP_ID"));
				timeSheetInfo.setWorkProductName((rs.getString("WorkProductName") == null) ? "" : rs.getString("WorkProductName"));
				timeSheetInfo.setDuration(rs.getFloat("Duration"));
				timeSheetInfo.setDescription(rs.getString("Description"));
				timeSheetInfo.setStatus(rs.getInt("Status"));

				switch (timeSheetInfo.getStatus()) {
					case 1:
						timeSheetInfo.setStatusName("unapproved");
						break;
					case 2:
						timeSheetInfo.setStatusName("PL");
						break;
					case 3:
						timeSheetInfo.setStatusName("MISC");
						break;
					case 4:
						timeSheetInfo.setStatusName("QA");
						break;
					case 5:
						timeSheetInfo.setStatusName("rejected");
						break;
					default :
						timeSheetInfo.setStatusName("unknown");
				}
				String sLeaderRole = rs.getString("LeaderRole");
				if (sLeaderRole != null && sLeaderRole.length() > 0 && sLeaderRole.substring(2, 3) == "1") {
					timeSheetInfo.setStatusName("GL");
				}
				lstTimeSheet.add(timeSheetInfo);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getTimeSheetList():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getTimeSheetList():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, st, rs, strClassName + ".getTimeSheetList():");
		}
		return lstTimeSheet;
	}

	/**
	 * HaiMM added for Migration - 08/Aug/2008
	 * Method getTimeSheetDummyList.
	 * @param nDevID
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getTimeSheetDummyList(int nDevID) throws SQLException {
		String strSQL = "";
		List lstTimeSheet = new ArrayList();
		Statement st = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			if (ds == null) ds = conPool.getDS();
			con = ds.getConnection();
			st = con.createStatement();

			strSQL = " SELECT TimeSheet_ID " +
					 " FROM TimeSheet " + 
					 " WHERE TimeSheet.DEVELOPER_ID = " + nDevID +
					 " AND TimeSheet.DUMMY_FIELD = 1" ; 
			rs = st.executeQuery(strSQL);

			while (rs.next()) {
				TimeSheetInfo timeSheetInfo = new TimeSheetInfo();
				timeSheetInfo.setTimeSheetID(rs.getInt("TIMESHEET_ID"));

				lstTimeSheet.add(timeSheetInfo);
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getTimeSheetDummyList():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getTimeSheetDummyList():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, st, rs, strClassName + ".getTimeSheetDummyList():");
		}
		return lstTimeSheet;
	}
	// Added by HaiMM
	public Collection getTimesheetMigrateList() throws SQLException {
			String strSQL = "";
			List lstTimeSheet = new ArrayList();
			Statement st = null;
			ResultSet rs = null;
			Connection con = null;

			try {
				if (ds == null) ds = conPool.getDS();
				con = ds.getConnection();
				st = con.createStatement();

				strSQL = " SELECT TimeSheet_ID FROM Timesheet_Migrate "; 
				rs = st.executeQuery(strSQL);

				while (rs.next()) {
					TimeSheetInfo timeSheetInfo = new TimeSheetInfo();
					timeSheetInfo.setTimeSheetID(rs.getInt("TIMESHEET_ID"));

					lstTimeSheet.add(timeSheetInfo);
				}
			}
			catch (SQLException ex) {
				logger.error(strClassName + ".getTimesheetMigrateList():" + ex.toString());
			}
			catch (Exception ex) {
				logger.error(strClassName + ".getTimesheetMigrateList():" + ex.toString());
			}
			finally {
				conPool.releaseResource(con, st, rs, strClassName + ".getTimesheetMigrateList():");
			}
			return lstTimeSheet;
		}

	/**
	 * Method getTimeSheetList.
	 * get a collection of timesheet for a user in a period of time,
	 * separated by pages
	 * @param nDevID
	 * @param nProjectID
	 * @param nStatus
	 * @param strDateFrom
	 * @param strDateTo
	 * @param nSort
	 * @param nPage
	 * @return Collection
	 * @throws SQLException
	 * @author Nguyen Thai Son
	 * @since Oct 15, 2002
	 */
	public Collection getTimeSheetList(int nDevID, int nProjectID, int nStatus, String strDateFrom, String strDateTo, int nSort, int nPage) throws SQLException {
		ArrayList lstResult = new ArrayList();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;

		//added by MinhPT 03/Oct/11
		//for use ROWNUM
		this.nCurrentPage = nPage;
		int nTotalPage = 0;
		int nTotalTimesheet = 0;

		try {
			//STEP1:prepare the filter
			StringBuffer strFilterSQL = new StringBuffer();
			strFilterSQL.append(" WHERE T.developer_id = " + nDevID);
			if (nProjectID != 0)
				strFilterSQL.append(" AND T.project_id = " + nProjectID);
			switch (nStatus) {
				case 0:
					break;
				case 1:
					strFilterSQL.append(" AND T.status = 1");
					break;
				case 2:
					strFilterSQL.append(" AND T.status IN (2, 3, 4)");
					break;
				case 3:
					strFilterSQL.append(" AND T.status = 5");
					break;
			}
			//Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
			strFilterSQL.append(SqlUtil.genDateConstraint("T.occur_date", strDateFrom, strDateTo));

			//STEP2: count the nTotalTimesheet
			StringBuffer strCountSQL = new StringBuffer();
			strCountSQL.append("SELECT COUNT(timesheet_id) as NUM FROM timesheet T");
			strCountSQL.append(strFilterSQL);

			//logger.debug("EJB.InputEJB.getTimeSheetList(): strCountSQL = " + strCountSQL.toString());

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.prepareStatement(strCountSQL.toString());
			rs = stmt.executeQuery();

			if (rs.next()) {
				nTotalTimesheet = (rs.getString("NUM") != null) ? rs.getInt("NUM") : 0;
			}
			rs.close();

			//logger.debug("EJB.InputEJB.getTimeSheetList(): nTotalTimesheet = " + nTotalTimesheet);

			nTotalPage = (nTotalTimesheet / Timesheet.MAX_RECORDS);
			if ((nTotalTimesheet % Timesheet.MAX_RECORDS) != 0)
				nTotalPage = nTotalPage + 1;

			//STEP3: for check nCurrentPage > (nTotalPage-1)
			//added by MinhPT 03Oct13
			if (this.nCurrentPage > (nTotalPage - 1))
				this.nCurrentPage = 0;
			int nStart = this.nCurrentPage * Timesheet.MAX_RECORDS;
			int nEnd = nStart + Timesheet.MAX_RECORDS;

			//STEP4:query the data
			StringBuffer strSQL = new StringBuffer();
			strSQL.append(" SELECT * FROM ( ");
			strSQL.append(" SELECT ROWNUM r, RN.* FROM ( ");

			strSQL.append(" SELECT T.*, P.code as ProjectCode, D.account as UserName, ");
			strSQL.append(" PS.name as ProcessName, TOW.name as TypeOfWorkName,");
			strSQL.append(" WP.name as WorkProductName, DTemp.role AS LeaderRole");
			strSQL.append(" FROM timesheet T, developer D, project P, process PS, typeofwork TOW, workproduct WP, developer DTemp");

			strSQL.append(strFilterSQL);
			strSQL.append(" AND T.developer_id = D.developer_id(+)");
			strSQL.append(" AND T.project_id = P.project_id(+)");
			strSQL.append(" AND T.process_id = PS.process_id(+)");
			strSQL.append(" AND T.tow_id = TOW.tow_id(+)");
			strSQL.append(" AND T.wp_id = WP.wp_id(+)");
			strSQL.append(" AND T.approved_by_leader = DTemp.account(+)");

			switch (nSort) {
				case 1: //Sort by Date
					strSQL.append(" ORDER BY T.occur_date, ProjectCode");
					break;
				case 2: //Sort by ProjectName
					strSQL.append(" ORDER BY ProjectCode, T.occur_date");
					break;
				case 3: //Sort by ProcessName
					strSQL.append(" ORDER BY ProcessName, T.occur_date");
					break;
				case 4: //Sort by TypeOfWork
					strSQL.append(" ORDER BY TypeOfWorkName, T.occur_date");
					break;
				case 5: //Sort by WorkProduct
					strSQL.append(" ORDER BY WorkProductName, T.occur_date");
					break;
				case 6: //Sort by Status
					strSQL.append(" ORDER BY T.status, T.occur_date");
					break;
			}
			//added by  MinhPT 03/Oct/11
			//for use ROWNUM
			strSQL.append(" ) RN )");
			strSQL.append(" WHERE r > ").append(nStart).append(" AND r <= ").append(nEnd);

			//logger.debug("HanhTN -- EJB.InputEJB.getTimeSheetList(): strSQL 2 = " + strSQL.toString());

			stmt = con.prepareStatement(strSQL.toString());
			rs = stmt.executeQuery();

			while (rs.next()) {
				TimeSheetInfo timeSheetInfo = new TimeSheetInfo();
				timeSheetInfo.setTimeSheetID(rs.getInt("TIMESHEET_ID"));
				timeSheetInfo.setDate(dateToString(rs.getDate("OCCUR_DATE"), "MM/dd/yy"));
				timeSheetInfo.setProject(rs.getInt("PROJECT_ID"));
				timeSheetInfo.setProjectName((rs.getString("ProjectCode") == null) ? "" : rs.getString("ProjectCode"));				
				timeSheetInfo.setProcess(rs.getInt("PROCESS_ID"));
				timeSheetInfo.setProcessName(rs.getString("ProcessName"));
				timeSheetInfo.setTypeofWork(rs.getInt("TOW_ID"));
				timeSheetInfo.setTypeofWorkName(rs.getString("TypeOfWorkName"));
				timeSheetInfo.setWorkProduct(rs.getInt("WP_ID"));
				timeSheetInfo.setWorkProductName((rs.getString("WorkProductName") == null) ? "" : rs.getString("WorkProductName"));
				timeSheetInfo.setDuration((rs.getString("Duration") == null) ? 0 : rs.getFloat("Duration"));
				timeSheetInfo.setDescription((rs.getString("Description") == null) ? "" : rs.getString("Description"));
				timeSheetInfo.setStatus((rs.getString("Status") == null) ? 0 : rs.getInt("Status"));

				switch (timeSheetInfo.getStatus()) {
					case 1:
						timeSheetInfo.setStatusName("unapproved");
						break;
					case 2:
						timeSheetInfo.setStatusName("PL");
						break;
					case 3:
						timeSheetInfo.setStatusName("MISC");
						break;
					case 4:
						timeSheetInfo.setStatusName("QA");
						break;
					case 5:
						timeSheetInfo.setStatusName("rejected");
						break;
					default :
						timeSheetInfo.setStatusName("unknown");
				}
				String sLeaderRole = rs.getString("LeaderRole");
				if (sLeaderRole != null && sLeaderRole.length() > 0 && sLeaderRole.substring(2, 3) == "1") {
					timeSheetInfo.setStatusName("GL");
				}
				timeSheetInfo.setComment((rs.getString("RCOMMENT") == null) ? "" : rs.getString("RCOMMENT"));
				lstResult.add(timeSheetInfo);
			}//end while

			setTotalPage(nTotalPage);
			setTotalTimesheet(nTotalTimesheet);
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getTimeSheetList():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getTimeSheetList():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getTimeSheetList():");
		}
		return lstResult;
	}

	/**
	 * Method getRejectedTimesheets.
	 * @param nDevID
	 * @param nProjectID
	 * @param strDateFrom
	 * @param strDateTo
	 * @return int
	 * @throws SQLException
	 */
	public int getRejectedTimesheets(int nDevID, int nProjectID, String strDateFrom, String strDateTo) throws SQLException {
		int nRejectedTimesheets = 0x00;
		String strSQL = "";
		PreparedStatement stm = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();

			strSQL = " SELECT count(*) AS RejectedTimesheets " + 
					 " FROM TimeSheet, Developer, Project, Process, TypeOfWork, WorkProduct, Developer DeveloperTemp " + 
					 " WHERE TimeSheet.DEVELOPER_ID = Developer.DEVELOPER_ID(+) " + 
					 " AND TimeSheet.APPROVED_BY_LEADER = DeveloperTemp.Account(+) " + 
					 " AND TimeSheet.PROJECT_ID = Project.PROJECT_ID(+) " +
					 " AND TimeSheet.PROCESS_ID = Process.PROCESS_ID(+) " + 
					 " AND TimeSheet.TOW_ID = TypeOfWork.TOW_ID(+) " +
					 " AND TimeSheet.WP_ID = WorkProduct.WP_ID(+) ";

			if (nDevID != 0) {
				strSQL += " AND TimeSheet.DEVELOPER_ID = " + nDevID;
			}
			if (nProjectID != 0)
				strSQL += " AND TimeSheet.PROJECT_ID = " + nProjectID;
				strSQL += " AND Timesheet.STATUS = 5"; // Rejected
				//Modified by Tu Ngoc Trung, 2003-11-24. Skip null values of date
				strSQL += SqlUtil.genDateConstraint("OCCUR_DATE", strDateFrom, strDateTo);

			stm = con.prepareStatement(strSQL.toString());
			rs = stm.executeQuery();

			//logger.debug("@HanhTN - InputEJB.getRejectedTimesheets: strSQL == " + strSQL) ;

			if (rs.next()) {
				nRejectedTimesheets = rs.getInt("RejectedTimesheets");
			}
			rs.close();
			stm.close();
			con.close();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getRejectedTimesheets():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getRejectedTimesheets():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stm, rs, strClassName + ".getRejectedTimesheets():");
			//conPool.releaseResource(con, stm, rs, strClassName + ".getRejectedTimesheets():");
		}
		return nRejectedTimesheets;
	}

	/**
	 * Method setTotalPage.
	 * @param nTotal
	 */
	private void setTotalPage(int nTotal) {
		this.nTotalPage = nTotal;
	}

	/**
	 * Method setTotalTimesheet.
	 * @param nTotal
	 */
	private void setTotalTimesheet(int nTotal) {
		this.nTotalTimesheet = nTotal;
	}
	/**
	 * Method getTotalPage.
	 * @return int
	 */
	public int getTotalPage() {
		return this.nTotalPage;
	}

	/**
	 * Method getTotalTimesheet.
	 * @return int
	 */
	public int getTotalTimesheet() {
		return this.nTotalTimesheet;
	}

	/**
	 * Method dateToString.
	 * Convert date to string in a certain format
	 * @param date
	 * @param formatter
	 * @return String
	 * @author Nguyen Thai Son
	 * @since Oct 15, 2002
	 */
	private String dateToString(java.sql.Date date, String formatter) {
		if (date == null) {
			return null;
		}
		else {
			SimpleDateFormat fm = new SimpleDateFormat(formatter);
			String dateString;
			dateString = fm.format(date);
			return dateString;
		}
	}

	/**
	 * Method executeUpdate.
	 * @param SQLCommand
	 * @throws SQLException
	 */
	protected void executeUpdate(String SQLCommand) throws SQLException {
		Statement stmt = null;

		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();

			stmt = con.createStatement();
			stmt.executeUpdate(SQLCommand);
			stmt.close();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".executeUpdate():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".executeUpdate():" + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, null, strClassName + ".executeUpdate():");
		} //finally
	}
    
	/**
	 * Method GetFirstLastDate.
	 * @param nDevID
	 * @param nFlag
	 * @return String
	 * @throws SQLException
	 * @author Nguyen Thai Son
	 * @since 8 Sep 2001
	 */
	public String GetFirstLastDate(int nDevID, int nFlag) throws SQLException {
		//Query rejected record(s)
		Statement stmt = null;
		ResultSet rs = null;

		String sSQL = "SELECT OCCUR_DATE FROM TimeSheet" + " WHERE Developer_ID = " + nDevID + " AND STATUS = 5";
		Collection lstDate = new ArrayList();

		try {
			//log("SQL query string:  " + sSQL);
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sSQL);
			while (rs.next()) {
				java.sql.Date occurDate = rs.getDate(1);
				lstDate.add(occurDate);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".GetFirstLastDate():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".GetFirstLastDate():" + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".GetFirstLastDate():");
		} //finally

		//Seek the first date
		java.sql.Date minDate = null;
		java.sql.Date maxDate = null;
		String strMinDate = null;
		String strMaxDate = null;

		try {
			Iterator itResult = null;
			if (lstDate != null)
				itResult = lstDate.iterator();
			if ((itResult != null) && (itResult.hasNext())) {
				minDate = (Date) itResult.next();
				maxDate = minDate;
			}
			if (itResult != null) {
				while (itResult.hasNext()) {
					java.sql.Date occurDate = (Date) itResult.next();
					switch (nFlag) {
						case 0: //find the first date
							{
								if (occurDate.getYear() < minDate.getYear())
									minDate = occurDate;
								else if (occurDate.getYear() == minDate.getYear()) {
									if (occurDate.getMonth() < minDate.getMonth())
										minDate = occurDate;
									else if (occurDate.getMonth() == minDate.getMonth()) {
										if (occurDate.getDate() < minDate.getDate())
											minDate = occurDate;
									}
								}
								break;
							} //end case
						default : //find the last date
							{
								if (occurDate.getYear() > maxDate.getYear())
									maxDate = occurDate;
								else if (occurDate.getYear() == maxDate.getYear()) {
									if (occurDate.getMonth() > maxDate.getMonth())
										maxDate = occurDate;
									else if (occurDate.getMonth() == maxDate.getMonth()) {
										if (occurDate.getDate() > maxDate.getDate())
											maxDate = occurDate;
									}
								}
							} //end default
					} //end switch
				} //end while
			} //end if
			strMinDate = dateToString(minDate, "MM/dd/yy");
			strMaxDate = dateToString(maxDate, "MM/dd/yy");
		}
		catch (Exception e) {
			logger.error("Exception occurs in GetFirstLastDate: " + e);
		}
		return (nFlag == 0) ? strMinDate : strMaxDate;
	}

	/**
	 * Method IsRejectedWork.
	 * @param nDevID
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean IsRejectedWork(int nDevID) throws SQLException {
		//Query rejected record(s)
		String sSQL = "SELECT COUNT(*) FROM TimeSheet WHERE Developer_ID = " + nDevID + " AND STATUS = 5";
		//count the rejected work(s)
		int nRejectedWorks = 0;
		Statement st = null;
		ResultSet rs = null;
		boolean bResult = false;
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sSQL);
			if (rs.next()) {
				nRejectedWorks = rs.getInt(1);
				//lstRejected.add(nRejectedWorks);
			}
			//logger.debug("Number of the rejected work(s) = " + nRejectedWorks);
			rs.close();
			st.close();

			if (nRejectedWorks > 0)
				bResult = true;
			else
				bResult = false;

		}
		catch (SQLException ex) {
			logger.error(strClassName + ".IsRejectedWork():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".IsRejectedWork():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, st, rs, strClassName + ".IsRejectedWork():");
		}//finally
		return bResult;
	}

	/**
	 * Method changePassword.
	 * @param intId
	 * @param strOldPassword
	 * @param strNewPassword
	 * @return int
	 * @throws SQLException
	 * @author TrangTK
	 */
	public int changePassword(int intId, String strOldPassword, String strNewPassword) throws SQLException {

		int n = 0;
		PreparedStatement prestmt = null;
		String strSql = "UPDATE Developer SET PASSWORD =?" + " WHERE DEVELOPER_ID =? AND PASSWORD =?";
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();

			prestmt = con.prepareStatement(strSql);
			prestmt.setString(1, strNewPassword);
			prestmt.setInt(2, intId);
			prestmt.setString(3, strOldPassword);
			n = prestmt.executeUpdate();
			prestmt.close();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".changePassword():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".changePassword():" + ex.getMessage());
		}
		finally {
			conPool.releaseResource(con, prestmt, null, strClassName + ".changePassword():");
		} //finally
		return n;
	}

	  /**
	   * Method getLackedTimesheet.
	   * get all timesheet(s) are lack by a range of date
	   * @param strGroup
	   * @param strFrom
	   * @param strTo
	   * @return Collection
	   * @throws SQLException
	   * @author Nguyen Thai Son
	   * @since Oct 14, 2002
	   */
	  public Collection getLackedTimesheet(String strGroup, String strFrom, String strTo) throws SQLException {
		  ArrayList arrReturn = new ArrayList();
		  StringMatrix smtLacked = new StringMatrix();
		  String[] arrWorkingDates = null;
		  try {
			  arrWorkingDates = DateUtils.getArrayOfWorkingDates(strFrom, strTo);
		  }
		  catch (Exception eMemory) {
			  return null;
		  }

		  int nNumberWorkingDates = arrWorkingDates.length;

		  Statement stmt = null;
		  ResultSet rs = null;

		  try {
			  //All developers who didnot report any timesheet.
			  StringBuffer strNoneSQL = new StringBuffer();
			  strNoneSQL.append("SELECT group_name, upper(account) as ORDER_ACCOUNT,");
			  strNoneSQL.append(" 'ZZZ' as ORDER_DATE, 'ZZZ' as OCCURDATE,");
			  strNoneSQL.append(" upper(group_name) as ORDER_GROUP");
			  strNoneSQL.append(" FROM developer");
			  strNoneSQL.append(" WHERE");
			  if (!strGroup.trim().toUpperCase().equals("ALL") && !strGroup.trim().toUpperCase().equals("0")) {
				  strNoneSQL.append(" group_name = '" + strGroup + "'");
			  }
			  else {
				  strNoneSQL.append(" 1 = 1");
			  }
			  strNoneSQL.append(" AND status IN (1, 2)"); //staff, or collaborator
			  strNoneSQL.append(" AND NOT EXISTS");
			  strNoneSQL.append(" (SELECT timesheet_id FROM timesheet T");
			  strNoneSQL.append(" WHERE T.developer_id = developer.developer_id");
			  //Modified by Tu Ngoc Trung, 2003-11-24. Skip null values of date
			  strNoneSQL.append(SqlUtil.genDateConstraint("T.occur_date", strFrom, strTo));
            
			  // Avoid Sunday from lack timsheet counting, TrungTN added TRIM function 19-Jun-06
			  strNoneSQL.append(" AND TRIM(TO_CHAR(T.occur_date, 'DAY')) != 'SUNDAY')");

			  //Developers who have at least 1 timesheet in the selected dates.
			  /////////////////////////////////////////////////////////
			  StringBuffer strSQL = new StringBuffer();
			  strSQL.append("SELECT DISTINCT D.group_name, upper(D.account) as ORDER_ACCOUNT,  ");
			  strSQL.append(" TO_CHAR(T.occur_date, 'yyyy/mm/dd') AS ORDER_DATE,");
			  strSQL.append(" TO_CHAR(T.occur_date, 'mm/dd/yy') AS OCCURDATE,");
			  strSQL.append(" upper(group_name) as ORDER_GROUP");
			  strSQL.append(" FROM timesheet T, developer D");
			  strSQL.append(" WHERE T.developer_id = D.developer_id");
			  strSQL.append(" AND D.status IN (1, 2)");   //staff, or collaborator
			  //Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
			  strSQL.append(SqlUtil.genDateConstraint("T.occur_date", strFrom, strTo));

			  // Avoid Sunday from lack timsheet counting, TrungTN added TRIM function 19-Jun-06
			  strSQL.append(" AND TRIM(TO_CHAR(T.occur_date, 'DAY')) != 'SUNDAY'");
			  if (!strGroup.trim().toUpperCase().equals("ALL") && !strGroup.trim().toUpperCase().equals("0"))
				  strSQL.append(" AND D.group_name = '" + strGroup + "'");
			  strSQL.append(" ORDER BY ORDER_GROUP, ORDER_ACCOUNT, ORDER_DATE");

			  StringBuffer strTotalSQL = new StringBuffer();
			  strTotalSQL.append(strNoneSQL + " UNION " + strSQL);

			  //logger.debug("EJB.InputEJB.getLackedTimesheet(): strSQL = " + strTotalSQL.toString());

			  ////////////////////////////////////////////////////Begin Thaison's adding (22 Feb 2002)
			  //STEP 3 - Add account, date of them in which they lacked timesheet.
			  ///////////////////////////////////////////////////////////
			  boolean bInsert;
			  String strCurrentDev, strNextDev;
			  String strCurrentDate;
			  int intIndex;
			  StringVector svtRow = null;
			  //int nPrevID = 0x00;

			  if (ds == null)
				  ds = conPool.getDS();
			  con = ds.getConnection();
			  stmt = con.createStatement();
			  rs = stmt.executeQuery(strTotalSQL.toString());

			  //don't changed here
			  if (rs.next()) {
				  while (!rs.isAfterLast()) {
					  String strOccurDate = rs.getString("OCCURDATE");

					  if (strOccurDate != null && !"ZZZ".equals(strOccurDate)) {
						  intIndex = 0;
						  bInsert = false;
						  String strGroupName = rs.getString("GROUP_NAME");
						  String strAccount = rs.getString("ORDER_ACCOUNT");

						  strNextDev = strCurrentDev = rs.getString("ORDER_ACCOUNT");
						  svtRow = new StringVector(nNumberWorkingDates + 0x02);
						  svtRow.setCell(0x00, strGroupName);
						  svtRow.setCell(0x01, strAccount);
						  strCurrentDate = arrWorkingDates[0];
						  while (!rs.isAfterLast() && (strCurrentDev.equals(strNextDev)) && (DateUtils.CompareDate(strCurrentDate, arrWorkingDates[nNumberWorkingDates - 1]) < 1)) //strCurrentDate <= strToDate
						  {
							  String strReport = rs.getString("OCCURDATE");
							  while ((DateUtils.CompareDate(strCurrentDate, strReport) == -1) && (intIndex < nNumberWorkingDates))
									  //strCurrentDate < strReport
							  {
								  //add the date which is lack of timesheet
								  svtRow.setCell(intIndex + 2, "X"); //2: ID and Account fields
								  intIndex++;
								  bInsert = true;
								  //go to next
								  if (intIndex < nNumberWorkingDates)
									  strCurrentDate = arrWorkingDates[intIndex];
							  }
							  intIndex++;
							  if (intIndex < nNumberWorkingDates)
								  strCurrentDate = arrWorkingDates[intIndex];
							  if (rs.next())
								  strNextDev = rs.getString("ORDER_ACCOUNT");
							  else
								  strNextDev = "";
							  if (!strCurrentDev.equals(strNextDev)) {
								  while (intIndex < nNumberWorkingDates) {
									  svtRow.setCell(intIndex + 2, "X"); //2: ID and Account fields
									  intIndex++;
									  bInsert = true;
								  }
							  }
						  }
						  if (bInsert) {
							  smtLacked.addRow(svtRow);
						  }
					  }//end if strOccurDate
					  else if ("ZZZ".equals(strOccurDate)) {
						  StringVector vecNone = new StringVector(nNumberWorkingDates + 0x02);
						  vecNone.setCell(0x00, rs.getString("group_name"));
						  vecNone.setCell(0x01, rs.getString("ORDER_ACCOUNT"));
						  for (int nWorkingDate = 0x00; nWorkingDate < nNumberWorkingDates; nWorkingDate++) {
							  vecNone.setCell(nWorkingDate + 0x02, "X");
						  }
						  smtLacked.addRow(vecNone);

						  rs.next();
					  }
				  } //end while
			  }
			  ////////////////////////////////////////////////////End Thaison's adding
			  rs.close();
			  stmt.close();

			  //STEP 5: Push titled row into the first line of table..
			  ////////////////////////////////////////////////////////////////
			  StringVector svtTitledRow = new StringVector(nNumberWorkingDates + 0x02);
			  svtTitledRow.setCell(0x00, "Group");
			  svtTitledRow.setCell(0x01, "Account");
			  for (int nWorkingDate = 0x00; nWorkingDate < nNumberWorkingDates; nWorkingDate++) {
				  svtTitledRow.setCell(nWorkingDate + 0x02, arrWorkingDates[nWorkingDate]);
			  }
			  if (smtLacked.getNumberOfRows() > 0)
				  smtLacked.insertRow(0x00, svtTitledRow);
			  else
				  smtLacked.addRow(svtTitledRow);
			  arrReturn.add(smtLacked);

		  }
		  catch (SQLException ex) {
			  logger.error(strClassName + ".getLackedTimesheet():" + ex.toString());
		  }
		  catch (Exception ex) {
			  logger.error(strClassName + ".getLackedTimesheet():" + ex.toString());
		  }
		  finally {
			  conPool.releaseResource(con, stmt, rs, strClassName + ".getLackedTimesheet():");
		  } //finally

		  return arrReturn;
	  }

	/**
	 * Method getPendingAccount.
	 * @param lstProjects
	 * @param strFromDate
	 * @param strToDate
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getPendingAccount(Collection lstProjects, String strFromDate, String strToDate) throws SQLException {
		ArrayList arrReturn = new ArrayList();
		if (lstProjects == null || lstProjects.size() <= 0)
			return arrReturn;
		int row = 0x00;
		StringMatrix smtPending = new StringMatrix(lstProjects.size(), 0x03);

		//Modified by Tu Ngoc Trung, 2003-11-24. Skip null values of date
		StringBuffer strRootSQL = new StringBuffer(); 
		strRootSQL.append("SELECT distinct DEVELOPER.ACCOUNT AS USERNAME");
		strRootSQL.append(" FROM TIMESHEET, DEVELOPER");
		strRootSQL.append(" WHERE TIMESHEET.DEVELOPER_ID = DEVELOPER.DEVELOPER_ID(+)");

		strRootSQL.append(SqlUtil.genDateConstraint("TIMESHEET.OCCUR_DATE", strFromDate, strToDate));
		//End Tu Ngoc Trung.

		strRootSQL.append(" AND TIMESHEET.STATUS NOT IN (2, 3, 4)");
		//Unapproved or rejected
		Iterator itrProjects = lstProjects.iterator();
		ProjectComboModel cboProject = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();

			while (itrProjects.hasNext()) {
				row++;
				// Go through each project
				cboProject = (ProjectComboModel) itrProjects.next();
				int nProjectID = Integer.parseInt(cboProject.getID());
				String strSQL = strRootSQL.toString();
				if (nProjectID != 0)
					strSQL += " AND TIMESHEET.PROJECT_ID = " + nProjectID;
				strSQL += " ORDER BY USERNAME";

				rs = stmt.executeQuery(strSQL);

				String strUserList = "";
				while (rs.next()) {
					strUserList += rs.getString("USERNAME") + ", ";
				}
				if (strUserList.length() > 0)
					strUserList = strUserList.substring(0x00, strUserList.length() - 0x02);
				smtPending.setCell(row, 0, nProjectID + "");
				smtPending.setCell(row, 1, cboProject.getCode());
				smtPending.setCell(row, 2, strUserList);

				rs.close();
			} //end while
			stmt.close();

			// Remove all empty row..
			row = 0x00;
			while (row < smtPending.getNumberOfRows()) {
				if (smtPending.getCell(row, 0x02).trim().length() <= 0x00)
					smtPending.removeRow(row);
				else
					row++;
			}
			// Sort by Project Code Colunm..
			smtPending.sortByColumn(0x01, false);

			// Insert titled row..
			StringVector svtTitledRow = new StringVector(0x03);
			svtTitledRow.setCell(0x00, "ID");
			svtTitledRow.setCell(0x01, "Project");
			svtTitledRow.setCell(0x02, "List of Pending");
			if (smtPending.getNumberOfRows() > 0)
				smtPending.insertRow(0x00, svtTitledRow);
			else
				smtPending.addRow(svtTitledRow);
			arrReturn.add(smtPending);

		} //end try
		catch (SQLException ex) {
			logger.error(strClassName + ".getPendingAccount():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getPendingAccount():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getPendingAccount():");
		}//finally
		return arrReturn;
	}

	/**
	 * Method getSummaryEfforts.
	 * @param nProjectID
	 * @param arrProjectIDs
	 * @param nStatus
	 * @param strFrom
	 * @param strTo
	 * @param nReportType
	 * @param type
	 * @param strDeveloperID
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getSummaryEfforts(int nProjectID, String arrProjectIDs, int nStatus, String strFrom, String strTo, int nReportType, int type, String strDeveloperID) throws SQLException {
		ArrayList arrReturn = new ArrayList();
		StringMatrix smtSummary = new StringMatrix();
		StringVector svtRow = null;
		int COLUMN_NUMBER = 0x05;
		// For TypeOfWork titled..
		String[] arrTypeOfWork = null;
		try {
			arrTypeOfWork = getTypeOfWork();
		}
		catch (Exception e) {
			logger.error("Error on EJB.getSummaryEfforts.getTypeOfWork: " + e);
			return arrReturn;
		}
		// Titled row..
		StringVector svtTitledRow = null;
		ArrayList arrWorking = null;
		try {
			switch (nReportType) {
				case 0x01:
					{
						arrWorking = (ArrayList) getProcess_TOW(nProjectID, arrProjectIDs, nStatus, strFrom, strTo, type, strDeveloperID);
						COLUMN_NUMBER = arrTypeOfWork.length + 0x01;
						svtTitledRow = new StringVector(COLUMN_NUMBER);
						svtTitledRow.setCell(0x00, "Process");
						for (int nCol = 0x01; nCol < COLUMN_NUMBER; nCol++)
							svtTitledRow.setCell(nCol, arrTypeOfWork[nCol - 0x01]);
					}
					break;
				case 0x02:
					{
						arrWorking = (ArrayList) getWorkProduct_TOW(nProjectID, arrProjectIDs, nStatus, strFrom, strTo, type, strDeveloperID);
						COLUMN_NUMBER = arrTypeOfWork.length + 0x01;
						svtTitledRow = new StringVector(COLUMN_NUMBER);
						svtTitledRow.setCell(0x00, "Product");
						for (int nCol = 0x01; nCol < COLUMN_NUMBER; nCol++)
							svtTitledRow.setCell(nCol, arrTypeOfWork[nCol - 0x01]);
					}
					break;
				case 0x03:
					{
						arrWorking = (ArrayList) getKPA_TOW(nProjectID, arrProjectIDs, nStatus, strFrom, strTo, type, strDeveloperID);
						COLUMN_NUMBER = arrTypeOfWork.length + 0x01;
						svtTitledRow = new StringVector(COLUMN_NUMBER);
						svtTitledRow.setCell(0x00, "KPA");
						for (int nCol = 0x01; nCol < COLUMN_NUMBER; nCol++)
							svtTitledRow.setCell(nCol, arrTypeOfWork[nCol - 0x01]);
					}
					break;
				case 0x04:
					{
						if ((arrWorking = (ArrayList) getAccountEffort(nProjectID, arrProjectIDs, strFrom, strTo, nStatus, type, strDeveloperID)) != null) {
							String[] arrWorkingDates = DateUtils.getArrayOfFullDates(strFrom, strTo);
							COLUMN_NUMBER = 0x02 + arrWorkingDates.length;
							svtTitledRow = new StringVector(COLUMN_NUMBER);
							svtTitledRow.setCell(0x00, "Account");
							svtTitledRow.setCell(0x01, "Total");
							for (int nCol = 0x02; nCol < COLUMN_NUMBER; nCol++) {
								svtTitledRow.setCell(nCol, arrWorkingDates[nCol - 0x02]);
							}
						}
					}
					break;
				default :
					arrWorking = null;
			}
		}
		catch (Exception e) {
			logger.error("Error on EJB.getSummaryEfforts.getTypeOfWork2: " + e.toString());
			return arrReturn;
		}
		// Add titled row..
		smtSummary.addRow(svtTitledRow);
		//logger.debug("* EJB.getSummaryEfforts TITLED ROW = " + svtTitledRow.toString());
		try {
			if (arrWorking != null) {
				Iterator itrWorking = arrWorking.iterator();
				while (itrWorking.hasNext()) {
					Object obj = itrWorking.next();
					switch (nReportType) {
						case 0x01:
							{
								ProcessRow row = (ProcessRow) obj;
								svtRow = new StringVector(COLUMN_NUMBER);
								svtRow.setCell(0x00, row.m_strProcessName);
								for (int nCol = 0x01; nCol < COLUMN_NUMBER; nCol++)
									svtRow.setCell(nCol, row.m_arrEffort[nCol - 0x01] + "");
								smtSummary.addRow(svtRow);
								// logger.debug("* EJB.getSummaryEfforts = " + svtRow.toString());
							}
							break;
						case 0x02:
							{
								WorkProductRow row = (WorkProductRow) obj;
								svtRow = new StringVector(COLUMN_NUMBER);
								svtRow.setCell(0x00, row.m_strWorkProductName);
								for (int nCol = 0x01; nCol < COLUMN_NUMBER; nCol++)
									svtRow.setCell(nCol, row.m_arrEffort[nCol - 0x01] + "");
								smtSummary.addRow(svtRow);
								//logger.debug("* EJB.getSummaryEfforts = " + svtRow.toString());
							}
							break;
						case 0x03:
							{
								KPARow row = (KPARow) obj;
								svtRow = new StringVector(COLUMN_NUMBER);
								svtRow.setCell(0x00, row.m_strKPAName);
								for (int nCol = 0x01; nCol < COLUMN_NUMBER; nCol++)
									svtRow.setCell(nCol, row.m_arrEffort[nCol - 0x01] + "");
								smtSummary.addRow(svtRow);
								// logger.debug("* EJB.getSummaryEfforts = " + svtRow.toString());
							}
							break;
						case 0x04:
							{
								AccountRow row = (AccountRow) obj;
								svtRow = new StringVector(COLUMN_NUMBER);
								svtRow.setCell(0x00, row._strAccount);
								svtRow.setCell(0x01, row._fTotalEffort + "");
								for (int nCol = 0x02; nCol < COLUMN_NUMBER; nCol++)
									svtRow.setCell(nCol, row._arrEffort[nCol - 0x02] + "");
								smtSummary.addRow(svtRow);
								// logger.debug("* EJB.getSummaryEfforts = " + svtRow.toString());
							}
							break;
					}
				}
			}
			arrReturn.add(smtSummary);
			return arrReturn;
		}
		catch (Exception e) {
			logger.error("Put data EJB.getSummaryEfforts.getTypeOfWork2: " + e.toString());
			return arrReturn;
		}
	}

	/**
	 * Method getTypeOfWork.
	 * @return String[]
	 * @throws SQLException
	 */
	private String[] getTypeOfWork() throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;
		Vector vecTOW = new Vector();

		String strSQL = "SELECT name FROM typeofwork ORDER BY name";
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSQL);
			while (rs.next()) {
				vecTOW.addElement(rs.getString("name"));
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getWorkType():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getWorkType():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getWorkType():");
		}//finally
		String[] arrTOWs = new String[vecTOW.size()];
		vecTOW.copyInto(arrTOWs);
		return arrTOWs;
	}

	/**
	 * Method getProcess_TOW.
	 * Fill effort values from Timesheet into the Process-TypeOfWork table.
	 * @param nProjectID
	 * @param arrProjectIDs
	 * @param nSelectedOption
	 * @param strFrom
	 * @param strTo
	 * @param type
	 * @param strDeveloperID
	 * @return Collection
	 * @throws SQLException
	 * @author Nguyen Thai Son
	 * @since 28 Dec 2001
	 */
	private Collection getProcess_TOW(int nProjectID, String arrProjectIDs, int nSelectedOption, String strFrom, String strTo, int type, String strDeveloperID) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		List lstListOfProcess = new ArrayList();
		///////////////////////////////////////////////////////////////
		//STEP1 - Get TOW_NAMEs
		String[] arrTOWs = null;
		int nNumOfTOWs = 0;
		arrTOWs = getTypeOfWork(); //TOW Names
		nNumOfTOWs = arrTOWs.length;
		///////////////////////////////////////////////////////////////
		//STEP 2 - Fill columns
		String[] strProject;
		String strProjectSQL;
		StringBuffer strSQL = new StringBuffer();

		try {
			boolean bInsert;
			String strCurrentProcess, strNextProcess;
			String strCurrentTOW;
			int intIndex;

			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();

			// Use Oracle HASH join hint on Project table
			strSQL.append(" SELECT /*+ USE_HASH(P)*/ PS.name as PROCESS, TW.name as TOW, SUM(T.duration) AS TOTAL ");
			strSQL.append(" FROM process PS, typeofwork TW, timesheet T, project P ");
			strSQL.append(" WHERE T.process_id = PS.process_id ");
			strSQL.append(" AND T.tow_id = TW.tow_id ");
			strSQL.append(" AND T.project_id = P.project_id ");
			//HanhTN add project archive - 21/03/2007
			strSQL.append(" AND P.archive_status != 4 ");
			//--------------------------
			if (nProjectID != 0) {
				strSQL.append(" AND P.project_id = " + nProjectID);
			}
			else if (arrProjectIDs != null && arrProjectIDs.length() > 0) {
				//strSQL.append(" AND P.project_id IN (" + arrProjectIDs + ")");
				//HanhTN fixbugs max 1000 records -- 28/04/2007
				strProject = CommonUtil.split(arrProjectIDs, ",");
				for (int i=0; i<strProject.length; i++) {
					strProjectSQL = "INSERT INTO project_temp (project_id) VALUES ("+ strProject[i] +")";
					stmt.addBatch(strProjectSQL);
				}
				stmt.executeBatch();
				strSQL.append(" AND P.project_id IN (select project_id from project_temp)");
			}
			if (type != 10) {
				strSQL.append(" AND P.type = " + type);
			}
			// Filter by DeveloperID
			if ((strDeveloperID != null) && (strDeveloperID.length() > 0)) {
				strSQL.append(" AND T.developer_ID=").append(strDeveloperID);
			}
			//Modified by Tu Ngoc Trung, 2003-11-24. Skip null values of date
			strSQL.append(SqlUtil.genDateConstraint("T.occur_date", strFrom, strTo));
			switch (nSelectedOption) {
				case 0:
					strSQL.append(" AND T.status = 1"); //Unapproved
					break;
				case 1:
					strSQL.append(" AND T.status IN (2, 3)");   //Approved By PL, GL,
					break;
				case 2:
					strSQL.append(" AND T.status = 4"); //Approved By QA
					break;
				case 3:
					strSQL.append(" AND T.status IN (1, 2, 3, 4)"); //Not Rejected
					break;
			}
			strSQL.append(" GROUP BY PS.name, TW.name");
			strSQL.append(" ORDER BY PROCESS, TOW");

			//logger.debug("InputEJB.getProcess_TOW(): strSQL = " + strSQL.toString());

			rs = stmt.executeQuery(strSQL.toString());
			if (rs.next()) {
				while (!rs.isAfterLast()) {
					intIndex = 0;
					bInsert = false;
					strNextProcess = strCurrentProcess = rs.getString("PROCESS");
					ProcessRow row = new ProcessRow();
					row.m_strProcessName = rs.getString("PROCESS");
					row.m_arrEffort = new float[nNumOfTOWs];
					strCurrentTOW = arrTOWs[0];
					int nStep = 1;
					while (!rs.isAfterLast() && (strCurrentProcess.equals(strNextProcess)) && (nStep <= nNumOfTOWs)) {
						String strTOW = rs.getString("TOW");
						while (!strCurrentTOW.equals(strTOW)) //strCurrentTOW < strTOW
						{
							row.m_arrEffort[intIndex] = 0;
							intIndex++;
							bInsert = true;
							if (intIndex < nNumOfTOWs) {
								nStep += 1;
								strCurrentTOW = arrTOWs[intIndex];
							}
						}
						row.m_arrEffort[intIndex] = rs.getFloat("TOTAL");
						intIndex++;
						bInsert = true;
						//go to next
						if (intIndex < nNumOfTOWs) {
							nStep += 1;
							strCurrentTOW = arrTOWs[intIndex];
						}
						if (rs.next())
							strNextProcess = rs.getString("PROCESS");
						else
							strNextProcess = "";
					} //end while
					if (bInsert) {
						//Thaison added here (20 Feb 2002)
						//Displayed as person-day, not person-hour
						for (int j = 0; j < row.m_arrEffort.length; j++)
							row.m_arrEffort[j] = row.m_arrEffort[j] / 8;
						//Add a row to the Process_TOW table.
						lstListOfProcess.add(row);
					}
				} //end while
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getProcess_TOW():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getProcess_TOW():" + ex.toString());
		}
		finally {
			conPool.releaseResourceAndCommit(con, stmt, rs, strClassName + ".getProcess_TOW():");
		}//finally
		return lstListOfProcess;
	}
	////////////////////////////////////////////////////////////////////
	/////////////////////WORK PRODUCT - TYPE OF WORK////////////////////////////
	/**
	 * Method getWorkProduct_TOW.
	 * Fill effort values from Timesheet into the WorkProduct-TypeOfWork table.
	 * @param nProjectID
	 * @param arrProjectIDs
	 * @param nSelectedOption
	 * @param strFrom
	 * @param strTo
	 * @param type
	 * @param strDeveloperID
	 * @return Collection
	 * @throws SQLException
	 * @author Nguyen Thai Son
	 * @since 28 Dec 2001
	 */
	private Collection getWorkProduct_TOW(int nProjectID, String arrProjectIDs, int nSelectedOption, String strFrom, String strTo, int type, String strDeveloperID) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		List lstListOfWPs = new ArrayList();
		///////////////////////////////////////////////////////////////
		//STEP1 - Get TOW_NAMEs
		String[] arrTOWs = null;
		int nNumOfTOWs = 0;
		arrTOWs = getTypeOfWork(); //TOW Names
		nNumOfTOWs = arrTOWs.length;
		///////////////////////////////////////////////////////////////
		//STEP 2 - Fill columns
		//Modified by Tu Ngoc Trung, 2003-11-25.
		String[] strProject;
		String strProjectSQL;
		StringBuffer strSQL = new StringBuffer();

		try {
			boolean bInsert;
			String strCurrentWP, strNextWP;
			String strCurrentTOW;
			int intIndex;

			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();

			// Use Oracle HASH join hint on Project table
			strSQL.append(" SELECT /*+ USE_HASH(project)*/ workproduct.name as WORKPRODUCT, typeofwork.name as TOW, SUM(timesheet.duration) AS TOTAL");
			strSQL.append(" FROM workproduct, typeofwork, timesheet, project");
			strSQL.append(" WHERE timesheet.wp_id = workproduct.wp_id");
			strSQL.append(" AND timesheet.tow_id = typeofwork.tow_id");
			strSQL.append(" AND timesheet.project_id=project.project_id");
			//HanhTN add project archive - 21/03/2007
			strSQL.append(" AND project.archive_status != 4 ");
			//---------------------------
			if (nProjectID != 0) {
				strSQL.append(" AND project.project_id = ");
				strSQL.append(nProjectID);
			}
			else if (arrProjectIDs != null && arrProjectIDs.length() > 0) {
				//strSQL.append(" AND project.project_id IN (");
				//strSQL.append(arrProjectIDs);
				//strSQL.append(")");
				//HanhTN fix maximum 1000 records -- 03/05/2007
				strProject = CommonUtil.split(arrProjectIDs, ",");
				for (int i=0; i<strProject.length; i++) {
					strProjectSQL = "INSERT INTO project_temp (project_id) VALUES ("+ strProject[i] +")";
					stmt.addBatch(strProjectSQL);
				}
				stmt.executeBatch();
				strSQL.append(" AND project.project_id IN (select project_id from project_temp)");
			}
			if (type != 10) {
				strSQL.append(" AND project.type=");
				strSQL.append(type);
			}
			// Filter by DeveloperID
			if ((strDeveloperID != null) && (strDeveloperID.length() > 0)) {
				strSQL.append(" AND TIMESHEET.developer_ID=").append(strDeveloperID);
			}
			//Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
			strSQL.append(SqlUtil.genDateConstraint("timesheet.occur_date", strFrom, strTo));
			switch (nSelectedOption) {
				case 0:
					strSQL.append(" AND timesheet.status = 1"); //Unapproved
					break;
				case 1:
					strSQL.append(" AND timesheet.status IN (2, 3)");    //Approved By PL, GL,
					break;
				case 2:
					strSQL.append(" AND timesheet.status = 4"); //Approved By QA
					break;
				case 3:
					strSQL.append(" AND timesheet.status IN (1, 2, 3, 4)"); //Not Rejected
					break;
			}
			strSQL.append(" GROUP BY workproduct.name, typeofwork.name");
			strSQL.append(" ORDER BY WORKPRODUCT, TOW");
			//End Tu Ngoc Trung
			//logger.debug("InputEJB.getWorkProduct_TOW(): strSQL = " + strSQL.toString());

			rs = stmt.executeQuery(strSQL.toString());
			if (rs.next()) {
				while (!rs.isAfterLast()) {
					intIndex = 0;
					bInsert = false;
					strNextWP = strCurrentWP = rs.getString("WORKPRODUCT");
					WorkProductRow row = new WorkProductRow();
					row.m_strWorkProductName = rs.getString("WORKPRODUCT");
					row.m_arrEffort = new float[nNumOfTOWs];
					strCurrentTOW = arrTOWs[0];
					int nStep = 1;
					while (!rs.isAfterLast() && (strCurrentWP.equals(strNextWP)) && (nStep <= nNumOfTOWs)) {
						String strTOW = rs.getString("TOW");
						while (!strCurrentTOW.equals(strTOW)) //strCurrentTOW < strTOW
						{
							row.m_arrEffort[intIndex] = 0;
							intIndex++;
							bInsert = true;
							if (intIndex < nNumOfTOWs) {
								nStep += 1;
								strCurrentTOW = arrTOWs[intIndex];
							}
						}
						row.m_arrEffort[intIndex] = rs.getFloat("TOTAL");
						intIndex++;
						bInsert = true;
						//go to next
						if (intIndex < nNumOfTOWs) {
							nStep += 1;
							strCurrentTOW = arrTOWs[intIndex];
						}
						if (rs.next())
							strNextWP = rs.getString("WORKPRODUCT");
						else
							strNextWP = "";
					} //end while
					if (bInsert) {
						//Thaison added here (20 Feb 2002)
						//Displayed as person-day, not person-hour
						for (int j = 0; j < row.m_arrEffort.length; j++)
							row.m_arrEffort[j] = row.m_arrEffort[j] / 8;
						lstListOfWPs.add(row);
					}
				} //end while
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getWorkProduct_TOW():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getWorkProduct_TOW():" + ex.toString());
		}
		finally {
			conPool.releaseResourceAndCommit(con, stmt, rs, strClassName + ".getWorkProduct_TOW():");
		}//finally
		return lstListOfWPs;
	}
	////////////////////////////////////////////////////////////////////
	/////////////////////KPA - TYPE OF WORK////////////////////////////
	/**
	 * Method getKPA_TOW.
	 * Fill effort values from Timesheet into the KPA-TypeOfWork table.
	 * @param nProjectID
	 * @param arrProjectIDs
	 * @param nSelectedOption
	 * @param strFrom
	 * @param strTo
	 * @param type
	 * @param strDeveloperID
	 * @return Collection
	 * @throws SQLException
	 * @author Nguyen Thai Son
	 * @since 28 Dec 2001
	 */
	private Collection getKPA_TOW(int nProjectID, String arrProjectIDs, int nSelectedOption, String strFrom, String strTo, int type, String strDeveloperID) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		List lstListOfKPAs = new ArrayList();
		///////////////////////////////////////////////////////////////
		//STEP1 - Get TOW_NAMEs
		String[] arrTOWs = null;
		int nNumOfTOWs = 0;
		arrTOWs = getTypeOfWork(); //TOW Names
		nNumOfTOWs = arrTOWs.length;
		///////////////////////////////////////////////////////////////
		//STEP 2 - Fill columns
		//Modified by Tu Ngoc Trung, 2003-11-25
		String[] strProject;
		String strProjectSQL;
		StringBuffer strSQL = new StringBuffer();

		try {
			boolean bInsert;
			String strCurrentKPA, strNextKPA;
			String strCurrentTOW;
			int intIndex;

			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();

			// Use Oracle HASH join hint on Project table
			strSQL.append(" SELECT /*+ USE_HASH(project)*/ kpa.name as KPA, typeofwork.name as TOW, SUM(timesheet.duration) AS TOTAL");
			strSQL.append(" FROM kpa, typeofwork, timesheet, project ");
			strSQL.append(" WHERE timesheet.kpa_id = kpa.kpa_id ");
			strSQL.append(" AND timesheet.tow_id = typeofwork.tow_id ");
			strSQL.append(" AND timesheet.project_id=project.project_id ");
			//HanhTN add project archive - 21/03/2007
			strSQL.append(" AND project.archive_status != 4 ");
			//---------------------------
			if (nProjectID != 0) {
				strSQL.append(" AND project.project_id = ").append(nProjectID);
			}
			else if (arrProjectIDs != null && arrProjectIDs.length() > 0) {
				//strSQL.append(" AND project.project_id IN (");
				//strSQL.append(arrProjectIDs);
				//strSQL.append(")");
				//HanhTN fix maximum 1000 records -- 03/05/2007
				strProject = CommonUtil.split(arrProjectIDs, ",");
				for (int i=0; i<strProject.length; i++) {
					strProjectSQL = "INSERT INTO project_temp (project_id) VALUES ("+ strProject[i] +")";
					stmt.addBatch(strProjectSQL);
				}
				stmt.executeBatch();
				strSQL.append(" AND project.project_id IN (select project_id from project_temp)");
			}

			// Filter by DeveloperID
			if ((strDeveloperID != null) && (strDeveloperID.length() > 0)) {
				strSQL.append(" AND TIMESHEET.developer_ID=").append(strDeveloperID);
			}
			//Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
			strSQL.append(SqlUtil.genDateConstraint("timesheet.occur_date", strFrom, strTo));
			if (type != 10) {
				strSQL.append(" AND project.type=").append(type);
			}
			switch (nSelectedOption) {
				case 0:
					strSQL.append(" AND timesheet.status = 1"); //Unapproved
					break;
				case 1:
					strSQL.append(" AND timesheet.status IN (2, 3)");    //Approved By PL, GL,
					break;
				case 2:
					strSQL.append(" AND timesheet.status = 4"); //Approved By QA
					break;
				case 3:
					strSQL.append(" AND timesheet.status IN (1, 2, 3, 4)"); //Not Rejected
					break;
			}
			strSQL.append(" GROUP BY kpa.name, typeofwork.name" + " ORDER BY KPA, TOW");
			//End Tu Ngoc Trung
			//logger.debug("InputEJB.getKPA_TOW(): strSQL = " + strSQL);

			rs = stmt.executeQuery(strSQL.toString());
			if (rs.next()) {
				while (!rs.isAfterLast()) {
					intIndex = 0;
					bInsert = false;
					strNextKPA = strCurrentKPA = rs.getString("KPA");
					KPARow row = new KPARow();
					row.m_strKPAName = rs.getString("KPA");
					row.m_arrEffort = new float[nNumOfTOWs];
					strCurrentTOW = arrTOWs[0];
					int nStep = 1;
					while (!rs.isAfterLast() //             while (rs.next()
							&& (strCurrentKPA.equals(strNextKPA)) && (nStep <= nNumOfTOWs)) {
						String strTOW = rs.getString("TOW");
						while (!strCurrentTOW.equals(strTOW)) //strCurrentTOW < strTOW
						{
							row.m_arrEffort[intIndex] = 0;
							intIndex++;
							bInsert = true;
							if (intIndex < nNumOfTOWs) {
								nStep += 1;
								strCurrentTOW = arrTOWs[intIndex];
							}
						}
						row.m_arrEffort[intIndex] = rs.getFloat("TOTAL");
						intIndex++;
						bInsert = true;
						//go to next
						if (intIndex < nNumOfTOWs) {
							nStep += 1;
							strCurrentTOW = arrTOWs[intIndex];
						}
						if (rs.next())
							strNextKPA = rs.getString("KPA");
						else
							strNextKPA = "";
					} //end while
					if (bInsert) {
						//Thaison added here (20 Feb 2002)
						//Displayed as person-day, not person-hour
						for (int j = 0; j < row.m_arrEffort.length; j++)
							row.m_arrEffort[j] = row.m_arrEffort[j] / 8;
						lstListOfKPAs.add(row);
					}
				} //end while
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getKPA_TOW():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getKPA_TOW():" + ex.toString());
		}
		finally {
			conPool.releaseResourceAndCommit(con, stmt, rs, strClassName + ".getKPA_TOW():");
		} //finally
		return lstListOfKPAs;
	}
	/**
	 * Method getAccountEffort.
	 * @param nProjectID
	 * @param arrProjectIDs
	 * @param strFrom
	 * @param strTo
	 * @param nSelectedOption
	 * @param type
	 * @param strDeveloperID
	 * @return Collection
	 * @throws SQLException
	 */
	///////////////////////////////////////////////////////////////////////
	private Collection getAccountEffort(int nProjectID, String arrProjectIDs, String strFrom, String strTo, int nSelectedOption, int type, String strDeveloperID) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		List lstListOfDevs = new ArrayList();
		//STEP 1 - Select developers who reported timesheet in the selected dates.
		/////////////////////////////////////////////////////////
		int intNumberOfDates;
		String[] arrDates = null;
		arrDates = DateUtils.getArrayOfFullDates(strFrom, strTo);
		intNumberOfDates = arrDates.length;
		//STEP 2 - Add account, effort total, dates.
		///////////////////////////////////////////////////////////
		//Modified by Tu Ngoc Trung, 2003-11-24
		String [] strProject;
		String strProjectSQL;
		StringBuffer strSQL = new StringBuffer();

		try {
			boolean bInsert;
			String strCurrentDev, strNextDev;
			String strCurrentDate;
			int intIndex;

			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();

			// Use Oracle HASH join hint on Project table
			strSQL.append("SELECT /*+ USE_HASH(project)*/ DEVELOPER.ACCOUNT, ");
			strSQL.append(" TO_CHAR(TIMESHEET.OCCUR_DATE, 'mm/dd/yy') AS OCCURDATE, SUM(TIMESHEET.DURATION) AS DURATION");
			strSQL.append(" FROM TIMESHEET, DEVELOPER,PROJECT");
			strSQL.append(" WHERE  DEVELOPER.DEVELOPER_ID = TIMESHEET.DEVELOPER_ID");
			//Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
			strSQL.append(SqlUtil.genDateConstraint("TIMESHEET.OCCUR_DATE", strFrom, strTo));
			//HanhTN add project archive - 21/03/2007
			strSQL.append(" AND project.archive_status != 4 ");
			//---------------------------
			strSQL.append(" AND Timesheet.project_id=project.project_id");
			if (nProjectID != 0) {
				strSQL.append(" AND project.project_id = ").append(nProjectID);
			}
			else if (arrProjectIDs != null && arrProjectIDs.length() > 0) {
				//strSQL.append(" AND project.project_id IN (").append(arrProjectIDs).append(")");
				//HanhTN fix maximum 1000 records -- 03/05/2007
				strProject = CommonUtil.split(arrProjectIDs, ",");
				for (int i=0; i<strProject.length; i++) {
					strProjectSQL = "INSERT INTO project_temp (project_id) VALUES ("+ strProject[i] +")";
					stmt.addBatch(strProjectSQL);
				}
				stmt.executeBatch();
				strSQL.append(" AND project.project_id IN (select project_id from project_temp)");
			}
			if (type != 10) {
				strSQL.append(" AND project.type=").append(type);
			}
			// Filter by DeveloperID
			if ((strDeveloperID != null) && (strDeveloperID.length() > 0)) {
				strSQL.append(" AND TIMESHEET.developer_ID=").append(strDeveloperID);
			}

			switch (nSelectedOption) {
				case 0:
					strSQL.append(" AND TIMESHEET.STATUS = 1"); //Unapproved
					break;
				case 1:
					strSQL.append(" AND TIMESHEET.STATUS IN (2, 3, 4)");
					//Approved By PL or MISC, or QA (because QA only approves when PL approved)
					break;
				case 2:
					strSQL.append(" AND TIMESHEET.STATUS = 4"); //Approved By QA
					break;
				case 3:
					strSQL.append(" AND TIMESHEET.STATUS IN (1, 2, 3, 4)"); //Not Rejected
					break;
			}
			//strSQL.append(" AND rownum < 500");
			strSQL.append(" GROUP BY DEVELOPER.ACCOUNT, TO_CHAR(TIMESHEET.OCCUR_DATE, 'mm/dd/yy'), Timesheet.Occur_Date");
			strSQL.append(" ORDER BY DEVELOPER.ACCOUNT, Timesheet.Occur_Date");
			//End Tu Ngoc Trung

			//logger.debug("In EJB getAccountEffort: strSQL ---->" + strSQL.toString());

			rs = stmt.executeQuery(strSQL.toString());

			if (rs.next()) {
				while (!rs.isAfterLast()) {
					intIndex = 0;
					bInsert = false;
					strNextDev = strCurrentDev = rs.getString("ACCOUNT");
					AccountRow row = new AccountRow();
					row._strAccount = rs.getString("ACCOUNT");
					row._arrEffort = new float[intNumberOfDates];
					strCurrentDate = arrDates[0];
					while (!rs.isAfterLast() && (strCurrentDev.equals(strNextDev)) && (DateUtils.CompareDate(strCurrentDate, arrDates[intNumberOfDates - 1]) < 1))
							//strCurrentDate <= strToDate
					{
						String strReport = rs.getString("OCCURDATE");
						while (DateUtils.CompareDate(strCurrentDate, strReport) == -1)
								//strCurrentDate < strReport
						{
							row._arrEffort[intIndex] = 0;
							intIndex++;
							bInsert = true;
							if (intIndex < intNumberOfDates)
								strCurrentDate = arrDates[intIndex];
						}
						row._arrEffort[intIndex] = rs.getFloat("DURATION");
						intIndex++;
						bInsert = true;
						//logger.debug(rs.getString("ACCOUNT")+rs.getString("OCCURDATE")+rs.getFloat("DURATION"));
						//go to next
						if (intIndex < intNumberOfDates)
							strCurrentDate = arrDates[intIndex];
						if (rs.next())
							strNextDev = rs.getString("ACCOUNT");
						else
							strNextDev = "";
					} //end while
					if (bInsert) {
						row._fTotalEffort = 0;
						for (int i = 0; i < row._arrEffort.length; i++)
							row._fTotalEffort += row._arrEffort[i];
						lstListOfDevs.add(row);
					}
				} //end while
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getAccountEffort():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getAccountEffort():" + ex.toString());
		}
		finally {
			conPool.releaseResourceAndCommit(con, stmt, rs, strClassName + ".getAccountEffort():");
		}//finally
		return lstListOfDevs;
	}
	//ThaiLH
	//Some methods for the mapping module
	/**
	 * Method getProcessList.
	 * @return ArrayList
	 * @throws SQLException
	 * @author ThaiLH
	 */
	public ArrayList getProcessList() throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList alProcessList = new ArrayList();

		String strSQL = "SELECT PROCESS_ID, NAME FROM PROCESS ORDER BY NAME";

		//logger.debug(strClassName +"getWorkProductList:" + strSQL);
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSQL);

			while (rs.next()) {
				alProcessList.add(rs.getString("PROCESS_ID") == null ? "" : rs.getString("PROCESS_ID"));
				alProcessList.add(rs.getString("NAME") == null ? "" : rs.getString("NAME"));
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getProcessList():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getProcessList():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getProcessList():");
		}//finally

		return alProcessList;
	}


	/**
	 * Method getWorkProductList.
	 * @return ArrayList
	 * @throws SQLException
	 */
	public ArrayList getWorkProductList() throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList alWorkProductList = new ArrayList();

		String strSQL = "SELECT WP_ID, NAME FROM WORKPRODUCT ORDER BY NAME";

		//logger.debug(strClassName +"getWorkProductList:" + strSQL);
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSQL);

			while (rs.next()) {
				alWorkProductList.add(rs.getString("WP_ID") == null ? "" : rs.getString("WP_ID"));
				alWorkProductList.add(rs.getString("NAME") == null ? "" : rs.getString("NAME"));
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getWorkProductList():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getWorkProductList():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getWorkProductList():");
		}//finally

		return alWorkProductList;
	}

	/**
	 * Method getMappingList.
	 * @param strProcessID
	 * @return ArrayList
	 * @throws SQLException
	 */
	public ArrayList getMappingList(String strProcessID) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList alWorkProductList = new ArrayList();

		String strSQL = "SELECT WORKPRODUCT.WP_ID as ID, WORKPRODUCT.NAME as NAME FROM WORKPRODUCT, PROCESS_WP_MAPPING ";
		strSQL += " WHERE PROCESS_WP_MAPPING.PROCESS_ID = '";
		strSQL += strProcessID.trim();
		strSQL += "' AND PROCESS_WP_MAPPING.WP_ID = WORKPRODUCT.WP_ID";
		strSQL += " AND PROCESS_WP_MAPPING.MAPPING_TYPE = 0 ";
		strSQL += " ORDER BY NAME";

		//logger.debug(strClassName + ".getMappingList():SQL:" + strSQL);
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSQL);

			while (rs.next()) {
				alWorkProductList.add(rs.getString("ID") == null ? "" : rs.getString("ID"));
				alWorkProductList.add(rs.getString("NAME") == null ? "" : rs.getString("NAME"));
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getMappingList():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getMappingList():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getMappingList():");
		}//finally

		return alWorkProductList;
	}

	/**
	 * Method checkValidateWorkProduct.
	 * @param strProcessID
	 * @param alCurrentWorkProductIDList
	 * @return int
	 * @throws SQLException
	 */
	public int checkValidateWorkProduct(String strProcessID, ArrayList alCurrentWorkProductIDList) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		int nResult = 0;
		String strCurrentWorkProductIDList = "";
		Iterator itCurrentWorkProductIDList = alCurrentWorkProductIDList.iterator();

		while (itCurrentWorkProductIDList.hasNext()) {
			if (!"".equals(strCurrentWorkProductIDList)) {
				strCurrentWorkProductIDList += ",";
			}
			strCurrentWorkProductIDList += itCurrentWorkProductIDList.next().toString().trim();
		}

		String strSQL = "SELECT COUNT(*) as TOTAL FROM PROCESS_WP_MAPPING ";
		strSQL += " WHERE PROCESS_ID = '";
		strSQL += strProcessID.trim();
		strSQL += "' AND WP_ID IN(";
		strSQL += strCurrentWorkProductIDList;
		strSQL += ") AND MAPPING_TYPE = 0";

		//logger.debug(strClassName + ".checkValidateWorkProduct():SQL:" + strSQL);
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSQL);
			int nCount = 0;
			while (rs.next()) {
				nCount += rs.getInt("TOTAL");
			}
			if (nCount > 0) {
				nResult = -1;
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".checkValidateWorkProduct():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".checkValidateWorkProduct():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".checkValidateWorkProduct():");
		}//finally
		return nResult;
	}

	/**
	 * Method deleteWorkProduct.
	 * @param strProcessID
	 * @param alCurrentWorkProductIDList
	 * @throws SQLException
	 */
	public void deleteWorkProduct(String strProcessID, ArrayList alCurrentWorkProductIDList) throws SQLException {

		String strCurrentWorkProductIDList = "";
		Iterator itCurrentWorkProductIDList = alCurrentWorkProductIDList.iterator();

		while (itCurrentWorkProductIDList.hasNext()) {
			if (!"".equals(strCurrentWorkProductIDList)) {
				strCurrentWorkProductIDList += ",";
			}
			strCurrentWorkProductIDList += itCurrentWorkProductIDList.next().toString().trim();
		}

		String strSQL = "DELETE FROM PROCESS_WP_MAPPING WHERE PROCESS_ID = '";
		strSQL += strProcessID.trim();
		strSQL += "' AND PROCESS_WP_MAPPING.MAPPING_TYPE = 0 ";

		if (!"".equals(strCurrentWorkProductIDList)) {
			strSQL += " AND WP_ID IN(";
			strSQL += strCurrentWorkProductIDList;
			strSQL += ")";
		}
		//logger.debug("deleteWorkProduct:" + strSQL);
		try {
			executeUpdate(strSQL);
		}
		catch (Exception ex) {
			logger.error("Exception occurs in InputEJB.deleteWorkProduct(). " + ex.toString());
		}
	}

	/**
	 * Method addWorkProduct.
	 * @param strProcessID
	 * @param alCurrentWorkProductIDList
	 * @throws SQLException
	 */
	public void addWorkProduct(String strProcessID, ArrayList alCurrentWorkProductIDList) throws SQLException {

		Iterator itCurrentWorkProductIDList = alCurrentWorkProductIDList.iterator();
		String strSQL = "";

		try {
			while (itCurrentWorkProductIDList.hasNext()) {
				strSQL = "INSERT INTO PROCESS_WP_MAPPING(PROCESS_ID, WP_ID, MAPPING_TYPE) VALUES(";
				strSQL += strProcessID.trim();
				strSQL += ", ";
				strSQL += itCurrentWorkProductIDList.next().toString().trim();
				strSQL += ", 0)";
				//logger.debug("addWorkProduct():" + strSQL);
				executeUpdate(strSQL);
			}
		}
		catch (Exception ex) {
			logger.error("Exception occurs in InputEJB.addWorkProduct(). " + ex.toString());
		}
	}

	/**
	 * Method getAllMapping.
	 * @return ArrayList
	 * @throws SQLException
	 */
	public ArrayList getAllMapping() throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList alWorkProductList = new ArrayList();

		String strSQL = "SELECT PROCESS_ID, WORKPRODUCT.WP_ID FROM PROCESS_WP_MAPPING, WORKPRODUCT WHERE MAPPING_TYPE = 0 " + "AND PROCESS_WP_MAPPING.WP_ID = WORKPRODUCT.WP_ID ORDER BY PROCESS_ID, WORKPRODUCT.NAME";

		//logger.debug(strClassName + ".getAllMapping():SQL:" + strSQL);
		try {
			if (ds == null)
				ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSQL);

			while (rs.next()) {
				alWorkProductList.add(rs.getString("PROCESS_ID") == null ? "" : rs.getString("PROCESS_ID"));
				alWorkProductList.add(rs.getString("WP_ID") == null ? "" : rs.getString("WP_ID"));
			}
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getAllMapping():" + ex.toString());
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getAllMapping():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getAllMapping():");
		}//finally
		return alWorkProductList;
	}
	//EndOfThaiLH

	/**
	 * Method checkUserAccount.
	 * Check User Account
	 * @return ArrayList: User information if existed
	 * @author Tu Ngoc Trung
	 * @since 2003-11-15
	 */
	public ArrayList checkUserAccount(String strUserAccount) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList alsUser = new ArrayList();
		//Return immediate if incorrect UserAccount was pushed in
		if ((strUserAccount == null) || (strUserAccount.length() <= 0)) {
			return alsUser;
		}
		strUserAccount.toUpperCase();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("SELECT * FROM developer WHERE Upper(account)='").append(strUserAccount).append("'");

		//logger.debug(strClassName + ".checkUserAccount():SQL:" + strSQL);
		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSQL.toString());
			if (rs.next()) {
				alsUser.add(rs.getString("developer_id"));
				alsUser.add(rs.getString("ACCOUNT"));
				alsUser.add(rs.getString("ROLE"));
				alsUser.add(rs.getString("STATUS"));
				alsUser.add(rs.getString("GROUP_NAME"));
			}
		}
		catch (SQLException ex) {
			//ex.printStackTrace();
		}
		catch (Exception ex) {
			//ex.printStackTrace();
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getUserAccountList(): ");
		}//finally

		return alsUser;
	}
	/*End Tu Ngoc Trung*/
}