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
 * Description  :  Timesheet <p>
 * Author       :  TrangTK
 * Created date :  12/21/2001
 */
package fpt.timesheet.ApproveTran.ejb.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import fpt.timesheet.constant.Timesheet;
import fpt.timesheet.framework.connection.WSConnectionPooling;
import fpt.timesheet.framework.util.CommonUtil.CommonUtil;
import fpt.timesheet.framework.util.CommonUtil.ConstantRow;
import fpt.timesheet.framework.util.CommonUtil.ProjectPivotRow;
import fpt.timesheet.framework.util.CommonUtil.ProjectSummaryRow;
import fpt.timesheet.framework.util.SqlUtil.SqlUtil;

public class InquiryEJB implements SessionBean {

	private static final Logger logger = Logger.getLogger(InquiryEJB.class);
	//*******************************************
	private int nCurrentPage;
	private Connection con;
	private SessionContext context;
	private WSConnectionPooling conPool = new WSConnectionPooling();
	private DataSource ds = null;
	//*******************************************
	private static final String strClassName = InquiryEJB.class.getName();

	private int nTotalPage = 0;
	private int nTotalTimesheet = 0;

	/**
	 * Method ejbCreate.
	 * @throws CreateException
	 */
	public void ejbCreate() throws CreateException {
		try {
			ds = conPool.getDS();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CreateException("Create SessionBean failed");
		}
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
	 * Method ViewTimesheet.
	 * @param strAccount
	 * @param strApprover
	 * @param nProjectID
	 * @param arrProjectID
	 * @param nStatus
	 * @param strDateFrom
	 * @param strDateTo
	 * @param nSort
	 * @param strGroup
	 * @param nCurrentPage
	 * @param bIsForExport
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection ViewTimesheet(String strAccount, String strApprover, int nProjectID, String arrProjectID, int nStatus, String strDateFrom, String strDateTo, int nSort, String strGroup, int nCurrentPage, boolean bIsForExport) throws SQLException {

		ResultSet rs = null;
		Statement stmt = null;
		ArrayList arrList = new ArrayList();

		//added by MinhPT 03/Oct/11
		//for use ROWNUM
		int nTotalPage = 0;
		int nTotalTimesheet = 0;
		this.nCurrentPage = nCurrentPage;

		String[] strProject;
		String strProjectSQL;
		StringBuffer strSQL = new StringBuffer();
		StringBuffer strFilterSQL = new StringBuffer();
		StringBuffer strCountSQL = new StringBuffer();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		try {
			//long lngStartTime = System.currentTimeMillis() * 60;
			// take start time before making the request
			Date startTime = new java.util.Date();
			//convert start time to milliseconds
			long startTimeMS = startTime.getTime();
			// format start time to display timestamp
			String strStartTime = formatter.format(startTime);
			logger.debug("@HanhTN - InquiryEJB.ViewTimesheet - Start Time ==" + strStartTime + " -- Start Time Milisecond == "+ startTimeMS);
			
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();
			stmt.setFetchSize(300);

			//modified by MinhPT 03/Oct/11
			//STEP 1: prepare the filter
			//==================================
			strFilterSQL.append(" WHERE 1 = 1");
			if (!"".equals(strAccount)) {
				strFilterSQL.append(" AND UPPER(D.account) LIKE '")
							.append(CommonUtil.stringConvert(strAccount))
							.append("'");
			}
			if (nStatus != 0) {
				strFilterSQL.append(" AND T.status = " + nStatus);
			}
			//Modified by Tu Ngoc Trung, 2003-11-26, display Approved by QA also
			if (!"".equals(strApprover)) {
				strFilterSQL.append(" AND (UPPER(T.approved_by_leader) LIKE '")
							.append(CommonUtil.stringConvert(strApprover))
							.append("'");
				strFilterSQL.append(" OR UPPER(T.approved_by_sepg) LIKE '")
							.append(CommonUtil.stringConvert(strApprover))
							.append("')");
			}
			if (nProjectID != 0) {
				strFilterSQL.append(" AND T.project_id = " + nProjectID);
			}
			else {
				//strFilterSQL.append(" AND T.project_id IN (" + arrProjectID + ")");
				//HanhTN fixbugs max 1000 records -- 28/04/2007
				strProject = CommonUtil.split(arrProjectID, ",");
				for (int i=0; i<strProject.length; i++) {
					strProjectSQL = "INSERT INTO project_temp (project_id) VALUES ("+ strProject[i] +")";
					stmt.addBatch(strProjectSQL);
				}
				stmt.executeBatch();
				strFilterSQL.append(" AND T.project_id IN (select project_id from project_temp)");
			}
			if ((strGroup != null) && (!strGroup.equals("All"))) {
				strFilterSQL.append(" AND D.group_name = '" + strGroup + "'");
			}
			//Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
			strFilterSQL.append(SqlUtil.genDateConstraint("T.occur_date", strDateFrom, strDateTo));

			//STEP 2: count the timesheet_id
			//==================================
			strCountSQL.append("SELECT COUNT(timesheet_id) as NUM FROM timesheet T, developer D");
			strCountSQL.append(strFilterSQL);
			strCountSQL.append(" AND T.developer_id = D.developer_id(+)");
			//logger.debug("EJB.InquiryEJB.ViewTimesheet(): strCountSQL = " + strCountSQL.toString());

			rs = stmt.executeQuery(strCountSQL.toString());
			if (rs.next()) {
				nTotalTimesheet = (rs.getString("NUM") != null) ? rs.getInt("NUM") : 0;
			}
			rs.close();
			//logger.debug("EJB.InquiryEJB.ViewTimesheet(): nTotalTimesheet = " + nTotalTimesheet);

			nTotalPage = (nTotalTimesheet / Timesheet.MAX_RECORDS);
			if ((nTotalTimesheet % Timesheet.MAX_RECORDS) != 0) {
				nTotalPage = nTotalPage + 1;
			}
			if (this.nCurrentPage > (nTotalPage - 1)) {
				this.nCurrentPage = 0;
			}

			//STEP 3: query the data
			//==================================
			int nStart = this.nCurrentPage * Timesheet.MAX_RECORDS;
			int nEnd = nStart + Timesheet.MAX_RECORDS;
			strAccount = strAccount.toUpperCase();
			strApprover = strApprover.toUpperCase();

			strSQL.append(" SELECT * FROM ( ");
			strSQL.append(" SELECT ROWNUM r, RN.* FROM ( ");

			strSQL.append(" SELECT ");
			strSQL.append(" t.TimeSheet_ID, t.Occur_Date, t.Description, t.Duration, t.Approved_By_Leader, ");
			strSQL.append(" t.Process_ID, t.TOW_ID, t.WP_ID, t.KPA_ID, ");
			strSQL.append(" P.code as ProjectCode, P.type as ProjectType, P.status as ProjectStatus, D.account as UserName,");			
			strSQL.append(" PS.name as ProcessName, TOW.name as TypeOfWorkName, WP.name as WorkProductName,");
			strSQL.append(" KPA.name as KPAName, P.group_name as GroupName");
			strSQL.append(", TO_CHAR(T.Create_Date,'")
				  .append((bIsForExport) ? "dd-MON-yyyy HH24:MI" : "MM/dd/yy HH24:MI")
				  .append("') AS CreateTimestamp");
			strSQL.append(", TO_CHAR(T.plapprovedtime,'dd-MON-yyyy HH24:MI') AS plapprovedtime");
			strSQL.append(", TO_CHAR(T.qaapprovedtime,'dd-MON-yyyy HH24:MI') AS qaapprovedtime");
			strSQL.append(" FROM timesheet T, developer D, project P, process PS, typeofwork TOW, workproduct WP, KPA");
			strSQL.append(strFilterSQL);

			strSQL.append(" AND T.developer_id = D.developer_id(+)");
			strSQL.append(" AND T.project_id = P.project_id(+)");
			strSQL.append(" AND T.process_id = PS.process_id(+)");
			strSQL.append(" AND T.tow_id = TOW.tow_id(+)");
			strSQL.append(" AND T.wp_id = WP.wp_id(+)");
			strSQL.append(" AND T.kpa_id = KPA.kpa_id(+)");

			switch (nSort) {
				case 1: //Sort by Project
					strSQL.append(" ORDER BY ProjectCode, T.occur_date");
					break;
				case 2: //Sort by UserName
					strSQL.append(" ORDER BY UserName, T.occur_date");
					break;
				case 3: //Sort by ReportDate
					strSQL.append(" ORDER BY T.occur_date, ProjectCode");
					break;
				case 4: //Sort by ProcessName
					strSQL.append(" ORDER BY ProcessName, T.occur_date");
					break;
				case 5: //Sort by TypeOfWork
					strSQL.append(" ORDER BY TypeOfWorkName, T.occur_date");
					break;
				case 6: //Sort by WorkProductName
					strSQL.append(" ORDER BY WorkProductName, T.occur_date");
					break;
				case 7: //Sort by KPA
					strSQL.append(" ORDER BY KPAName, T.occur_date");
					break;
				case 8: //Sort by GroupName
					strSQL.append(" ORDER BY GroupName, UserName, T.occur_date");
					break;
			}
			strSQL.append(" ) RN )");

			//Modified by Trungtn 2003-11-19, for export inquiry report
			if (!bIsForExport) {
				strSQL.append(" WHERE r > ").append(nStart).append(" AND r <= ").append(nEnd);
			}

			//logger.debug("@HanhTN -- EJB.InquiryEJB.ViewTimesheet(): strSQL = " + strSQL.toString());

			rs = stmt.executeQuery(strSQL.toString());
			while (rs.next()) {
				InquiryRow tsData = new InquiryRow();

				tsData.Timesheet_ID = rs.getString("TimeSheet_ID");
				tsData.ProjectCode = rs.getString("ProjectCode");
				tsData.ProjectType = rs.getString("ProjectType");
				tsData.ProjectStatus = rs.getString("ProjectStatus");
				tsData.UserName = rs.getString("UserName");
				//Date format for export is "dd-MMM-yyyy", default is "MM/dd/yy"
				tsData.Create_Date = rs.getString("CreateTimestamp");
				tsData.Occur_Date = dateToString(rs.getDate("Occur_Date"), (bIsForExport) ? "dd-MMM-yyyy" : "MM/dd/yy");
				tsData.Description = rs.getString("Description");
				tsData.Duration = rs.getString("Duration");
				tsData.ProcessName = rs.getString("ProcessName");
				tsData.TypeOfWorkName = rs.getString("TypeOfWorkName");
				tsData.WorkProductName = rs.getString("WorkProductName");
				tsData.KPAName = rs.getString("KPAName");
				tsData.Approved_By_Leader = rs.getString("Approved_By_Leader");
				tsData.Process_ID = (rs.getString("Process_ID") == null) ? "0" : rs.getString("Process_ID");
				tsData.TOW_ID = (rs.getString("TOW_ID") == null) ? "0" : rs.getString("TOW_ID");
				tsData.WP_ID = (rs.getString("WP_ID") == null) ? "0" : rs.getString("WP_ID");
				tsData.KPA_ID = (rs.getString("KPA_ID") == null) ? "0" : rs.getString("KPA_ID");
				tsData.GroupName = rs.getString("GroupName");
				tsData.PLapprovedtime = rs.getString("plapprovedtime");
				tsData.QAapprovedtime = rs.getString("qaapprovedtime");
				arrList.add(tsData);
			}
			setTotalPage(nTotalPage);
			setTotalTimesheet(nTotalTimesheet);

			//long lngEndTime = System.currentTimeMillis() * 1000 * 60;
			//logger.debug("@HanhTN -- Log End Time ==" + lngEndTime);

			// take end time immediately
//			Date endTime = new java.util.Date();
			//convert endTime to milliseconds
//			long endTimeMS = endTime.getTime();
//			long runTime = endTimeMS - startTimeMS;
//			//format endTime for timestamp
//			String strEndTime = formatter.format(endTime);

//			logger.debug("@HanhTN - InquiryEJB.ViewTimesheet -  End Time ==" + strEndTime + " -- Run Time = "+runTime);
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".ViewTimesheet():" + ex.toString());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.error(strClassName + ".ViewTimesheet():" + ex.toString());
		}
		finally {
			conPool.releaseResourceAndCommit(con, stmt, rs, strClassName + ".ViewTimesheet(): ");
		}
		return arrList;
	}

	/**
	 * Method dateToString.
	 * Convert date to string in a certain format
	 * @param date
	 * @param formatter
	 * @return String
	 */
	private static String dateToString(java.sql.Date date, String formatter) {
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
	 * Method getSummaryReportData.
	 * @param nProjectID
	 * @param arrProjectIDs
	 * @param strDateFrom
	 * @param strDateTo
	 * @param nStatus
	 * @param type
	 * @param strDeveloperID
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getSummaryReportData(int nProjectID, String arrProjectIDs, String strDateFrom, String strDateTo, int nStatus, int type, String strDeveloperID) throws SQLException {

		ArrayList dataList = new ArrayList();
		ResultSet rs = null;
		Statement stmt = null;
		Connection con = null;

		String[] strProject;
		String strProjectSQL = "";
		String strAccountFilter = "";
		String strProjectFilter = "";

		StringBuffer strTotalSQL = new StringBuffer();
		StringBuffer strManagementSQL = new StringBuffer();
		StringBuffer strQualitySQL = new StringBuffer();
		StringBuffer strTranslationSQL = new StringBuffer();
		StringBuffer strCorrectionSQL = new StringBuffer();
		StringBuffer strSelectSQL = new StringBuffer();
		StringBuffer strSumSQL = new StringBuffer();

		try {
			//Debug running time
			long sysTime = System.currentTimeMillis();
			//logger.debug("sysTime=" + sysTime);

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();

			String strFilter = "";
			switch (nStatus) {
				case 0:
					strFilter = " AND T.status = 1"; //Unapproved
					break;
				case 1:
					strFilter = " AND T.status IN (2, 3)";  //Approved By PL or GL
					break;
				case 2:
					strFilter = " AND T.status = 4"; //Approved By QA
					break;
				case 3:
					strFilter = " AND T.status IN (1, 2, 3, 4)"; //Not Rejected
					break;
			}
			if (type != 10) {
				strFilter += " AND P.type = " + type;
			}
			// Filter by DeveloperID
			if ((strDeveloperID != null) && (strDeveloperID.length() > 0)) {
				strAccountFilter = " AND Developer_ID=" + strDeveloperID;
			}
			//Filter by ProjectID
			if (nProjectID != 0) {
				strProjectFilter = " AND P.project_id = " + nProjectID;
			}
			else if ((arrProjectIDs != null) && (arrProjectIDs.length() > 0)) {
				//strProjectFilter = " AND P.project_id IN (" + arrProjectIDs + ")";
				//HanhTN fixbugs max 1000 records -- 28/04/2007
				stmt.addBatch("DELETE project_temp");
				strProject = CommonUtil.split(arrProjectIDs, ",");

				for (int i=0; i<strProject.length; i++) {
					strProjectSQL = "INSERT INTO project_temp (project_id) VALUES ("+ strProject[i] +")";
					stmt.addBatch(strProjectSQL);
				}
				stmt.executeBatch();
				strProjectFilter = " AND P.project_id IN (select project_id from project_temp)";
			}
			// Insert into timesheet temporary table for used with SELECT SUM statements
			StringBuffer strInsertTSTemp = new StringBuffer();
			strInsertTSTemp.append("INSERT INTO timesheet_temp (DURATION , tow_id , process_id)")
					.append(" (SELECT /*+ USE_HASH(P)*/ DURATION , tow_id , process_id")
					.append(" FROM timesheet T, project P")
					.append(" WHERE T.project_id = P.project_id")
					.append(strProjectFilter).append(strAccountFilter).append(strFilter)
					.append(")");
			//logger.debug("@HanhTN: strInsertTSTemp=" + strInsertTSTemp);
			stmt.executeUpdate(strInsertTSTemp.toString());

			// 28-Apr-2007: remove WITH TS_Temp statement so re-write folowing lines
			strTotalSQL.append("(SELECT SUM(duration) / 8 AS SUM_TOTAL")
						.append(" FROM timesheet_temp T")
						.append(" WHERE 1=1");

			strManagementSQL.append("(SELECT SUM(duration) / 8 AS SUM_MANAGEMENT")
						.append(" FROM timesheet_temp T")
						.append(" WHERE 1=1").append(SqlUtil.genManagement("T.tow_id", "T.process_id"));

			strQualitySQL.append("(SELECT SUM(duration) / 8 AS SUM_QUALITY")
						.append(" FROM timesheet_temp T")
						.append(" WHERE 1=1").append(SqlUtil.genQuality("T.tow_id", "T.process_id"));

			strTranslationSQL.append("(SELECT SUM(duration) / 8 AS SUM_TRANSLATION")
						.append(" FROM timesheet_temp T")
						.append(" WHERE 1=1").append(SqlUtil.genTranslation("T.tow_id"));

			strCorrectionSQL.append("(SELECT SUM(duration) / 8 AS SUM_CORRECTION")
						.append(" FROM timesheet_temp T")
						.append(" WHERE 1=1").append(SqlUtil.genCorrection("T.tow_id", "T.process_id"));

			// 28-Apr-2007: remove WITH TS_Temp statement so re-write folowing lines
			strSumSQL.append(" SELECT * FROM ");
			strSumSQL.append(strTotalSQL).append("),");
			strSumSQL.append(strManagementSQL).append("),");
			strSumSQL.append(strQualitySQL).append("),");
			strSumSQL.append(strTranslationSQL).append("),");
			strSumSQL.append(strCorrectionSQL).append(")");

			//logger.debug("strSumSQL =" + strSumSQL);            
			float fTotalDuration = 0;
			float fCorrectionDuration = 0;
			float fManagementDuration = 0;
			float fQualityDuration = 0;
			float fDevelopmentDuration = 0;
			float fTranslationDuration = 0;
			final float RATE = 100;

			rs = stmt.executeQuery(strSumSQL.toString());
			if (rs.next()) {
				fTotalDuration = rs.getFloat("SUM_TOTAL");
				fCorrectionDuration = rs.getFloat("SUM_CORRECTION");
				fManagementDuration = rs.getFloat("SUM_MANAGEMENT");
				fQualityDuration = rs.getFloat("SUM_QUALITY");
				fTranslationDuration = rs.getFloat("SUM_TRANSLATION");
			}
			//round
			fTotalDuration = Math.round(fTotalDuration * RATE) / RATE;
			fCorrectionDuration = Math.round(fCorrectionDuration * RATE) / RATE;
			fManagementDuration = Math.round(fManagementDuration * RATE) / RATE;
			fQualityDuration = Math.round(fQualityDuration * RATE) / RATE;
			fTranslationDuration = Math.round(fTranslationDuration * RATE) / RATE;
			fDevelopmentDuration = fTotalDuration - fManagementDuration - fQualityDuration;     // - fTranslationDuration;
			fDevelopmentDuration = Math.round(fDevelopmentDuration * RATE) / RATE;

			//Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
			String strPeriodFilter = SqlUtil.genDateConstraint("OCCUR_DATE", strDateFrom, strDateTo);

			//TrungTN add timesheet_temp 28/04/2007
			//Insert temporary table for SELECT SUM statements below
			stmt.executeUpdate("DELETE timesheet_temp");
			strInsertTSTemp.setLength(0);
			strInsertTSTemp.append("INSERT INTO timesheet_temp (DURATION , tow_id , process_id)")
					.append(" (SELECT /*+ USE_HASH(P)*/ DURATION , tow_id , process_id")
					.append(" FROM timesheet T, project P")
					.append(" WHERE T.project_id = P.project_id")
					.append(strProjectFilter).append(strAccountFilter).append(strFilter)
					.append(strPeriodFilter)
					.append(")");
			//logger.debug("@HanhTN: strInsertTSTemp(perriod)=" + strInsertTSTemp);
			stmt.executeUpdate(strInsertTSTemp.toString());

			strSumSQL.setLength(0);
			strSumSQL.append(" SELECT * FROM ");
			strSumSQL.append(strTotalSQL).append("),");
			strSumSQL.append(strManagementSQL).append("),");
			strSumSQL.append(strQualitySQL).append("),");
			strSumSQL.append(strTranslationSQL).append("),");
			strSumSQL.append(strCorrectionSQL).append(")");

			//logger.debug("strSumSQL =" + strSumSQL);
			float fTotalDuration2 = 0;
			float fCorrectionDuration2 = 0;
			float fManagementDuration2 = 0;
			float fQualityDuration2 = 0;
			float fDevelopmentDuration2 = 0;
			float fTranslationDuration2 = 0;

			rs.close();
			rs = stmt.executeQuery(strSumSQL.toString());
			if (rs.next()) {
				fTotalDuration2 = rs.getFloat("SUM_TOTAL");
				fCorrectionDuration2 = rs.getFloat("SUM_CORRECTION");
				fManagementDuration2 = rs.getFloat("SUM_MANAGEMENT");
				fQualityDuration2 = rs.getFloat("SUM_QUALITY");
				fTranslationDuration2 = rs.getFloat("SUM_TRANSLATION");
			}
			//round
			fTotalDuration2 = Math.round(fTotalDuration2 * RATE) / RATE;
			fCorrectionDuration2 = Math.round(fCorrectionDuration2 * RATE) / RATE;
			fManagementDuration2 = Math.round(fManagementDuration2 * RATE) / RATE;
			fQualityDuration2 = Math.round(fQualityDuration2 * RATE) / RATE;
			fTranslationDuration2 = Math.round(fTranslationDuration2 * RATE) / RATE;
			fDevelopmentDuration2 = fTotalDuration2 - fManagementDuration2 - fQualityDuration2;     // - fTranslationDuration2;
			fDevelopmentDuration2 = Math.round(fDevelopmentDuration2 * RATE) / RATE;

			rs.close();
			stmt.close();

			SummaryModel smData1 = new SummaryModel();
			SummaryModel smData2 = new SummaryModel();
			SummaryModel smData3 = new SummaryModel();
			SummaryModel smData4 = new SummaryModel();
			SummaryModel smData5 = new SummaryModel();
			SummaryModel smData6 = new SummaryModel();

			smData1.setAll("Correction", fCorrectionDuration2 + "", fCorrectionDuration + "");
			dataList.add(smData1);

			smData6.setAll("Translation", fTranslationDuration2 + "", fTranslationDuration + "");
			dataList.add(smData6);

			smData2.setAll("Total", fTotalDuration2 + "", fTotalDuration + "");
			dataList.add(smData2);

			smData3.setAll("Management", fManagementDuration2 + "", fManagementDuration + "");
			dataList.add(smData3);

			smData4.setAll("Quality", fQualityDuration2 + "", fQualityDuration + "");
			dataList.add(smData4);

			smData5.setAll("Development", fDevelopmentDuration2 + "", fDevelopmentDuration + "");
			dataList.add(smData5);

			// Debug running time
			//logger.debug("sysTime=" + System.currentTimeMillis() +
			//	";total running time(ms):" + (System.currentTimeMillis() - sysTime) + 
			//	";total running time(seconds):" + (System.currentTimeMillis() - sysTime)/1000);
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getSummaryReportData():" + ex.toString());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			//ex.printStackTrace();
			logger.error(strClassName + ".getSummaryReportData():" + ex.toString());
		}
		finally {
			conPool.releaseResourceAndCommit(con, stmt, rs, strClassName + ".getSummaryReportData(): ");
		}
		return dataList;
	}

	/**
	 * Method getProjectList.
	 * @param arrProjectIDs
	 * @return String[]
	 * @throws SQLException
	 */
	private String[] getProjectList(String arrProjectIDs) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;

		String[] pList = null;
		String [] strProject;
		String strProjectSQL;
		Vector vtList = new Vector();

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();

			String strSQL = "SELECT Code FROM PROJECT";

			if (arrProjectIDs != null && arrProjectIDs.length() > 0) {
				//strSQL += " WHERE project_id IN (" + arrProjectIDs + ")";
				//HanhTN fix maximum 1000 records -- 03/05/2007
				strProject = CommonUtil.split(arrProjectIDs, ",");
				for (int i=0; i<strProject.length; i++) {
					strProjectSQL = "INSERT INTO project_temp (project_id) VALUES ("+ strProject[i] +")";
					stmt.addBatch(strProjectSQL);
				}
				stmt.executeBatch();
				strSQL += " WHERE project_id IN (select project_id from project_temp)";
				strSQL += " ORDER BY Code";
			}
			//logger.debug("InquiryEJB.getProjectList(): strSQL = " + strSQL);

			rs = stmt.executeQuery(strSQL);

			while (rs.next()) {
				vtList.addElement(rs.getString("Code"));
			}
			pList = new String[vtList.size()];
			vtList.copyInto(pList);
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getProjectList():" + ex.toString());
			//ex.printStackTrace();
			throw ex;
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getProjectList():" + ex.toString());
		}
		finally {
			conPool.releaseResourceAndCommit(con, stmt, rs, strClassName + ".getProjectList(): ");
		} //finally
		return pList;
	}

	/**
	 * Method getPendingReportData.
	 * @param arrProjectIDs
	 * @param strFromDate
	 * @param strToDate
	 * @param strGroup
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getPendingReportData(String arrProjectIDs,
										   String strFromDate,
										   String strToDate,
										   String strGroup) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;

		String strSql;
		String strRootSql;
		ArrayList dataList = new ArrayList();
		String[] lstProject = this.getProjectList(arrProjectIDs);

		int max = lstProject.length;
		if (max < 1)
			return dataList;

		ReportWeeklyModel wmHeaderData = new ReportWeeklyModel("Project", "Account List", "Leader");
		dataList.add(wmHeaderData);

		strRootSql = " SELECT distinct DEVELOPER.account as Value,Project.LEADER as Leader " +
					 " FROM TIMESHEET Ts, DEVELOPER, PROJECT" +
					 " WHERE Ts.DEVELOPER_ID = DEVELOPER.DEVELOPER_ID" +
					 " AND Ts.PROJECT_ID = PROJECT.project_id" +
					 " AND Ts.STATUS = 1";

		//Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
		strRootSql += (SqlUtil.genDateConstraint("Ts.OCCUR_DATE", strFromDate, strToDate));

		try {
			String strKey;
			String strValues;
			String strLeader = "";

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.createStatement();

			for (int i = 0; i < max; i++) {
				strKey = lstProject[i].toString();
				strSql = strRootSql + " AND PROJECT.code ='" + strKey + "'";
				if (!strGroup.equals("All"))
					strSql += " AND DEVELOPER.group_name ='" + strGroup + "'";
				strSql += " ORDER BY DEVELOPER.account";

				rs = stmt.executeQuery(strSql);
				strValues = "";

				while (rs.next()) {
					strValues += rs.getString("Value") + ", ";
					strLeader = rs.getString("Leader");
				}
				rs.close();

				if (strValues.length() > 2) {
					strValues = strValues.substring(0, strValues.length() - 2);

					ReportWeeklyModel wmData = new ReportWeeklyModel(strKey, strValues, strLeader);
					dataList.add(wmData);
				}
			}
			stmt.close();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getPendingReportData():" + ex.toString());
			//ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getPendingReportData():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getPendingReportData(): ");
		} //finally
		return dataList;
	}

	/**
	 * Method getUnapprovedPMEJB
	 * @param strGroup
	 * @param intProjectID
	 * @param arrProjectIDs
	 * @param strFromDate
	 * @param strToDate
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getUnreviewedPMEJB(String strGroup, 
										 int intProjectID, String arrProjectIDs,
										 String strFromDate, String strToDate, 
										 String strLogDateTime, int intProjectStatus) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;

		String strSQL;
		String strRootSQL;
		ArrayList dataList = new ArrayList();
		String[] lstProject = this.getProjectList(arrProjectIDs);

		int max = lstProject.length;
		if (max < 1) return dataList;

		strRootSQL =
			"SELECT DISTINCT p.project_id as ProjectId, p.code, p.leader as Leader, d.name as Value, " +
			" (CASE WHEN t.status IN(2,4,5)" +
			" AND NVL(t.plapprovedtime, to_date('31-Dec-9999','dd-mon-yyyy')) <= to_date('" + strLogDateTime + "', 'mm/dd/yy hh24:mi:ss')" +
			" THEN 1 ELSE 0 END) AS PL_Approve, SUM (t.duration) / 8 AS Effort" +
			" FROM timesheet t, developer d, project p" +
			" WHERE t.developer_id = d.developer_id AND t.project_id = p.project_id " +
			" AND t.create_date <= to_date('" + strLogDateTime + "', 'mm/dd/yy hh24:mi:ss')";
			//" AND NVL(t.plapprovedtime, to_date('31-Dec-9999','dd-mon-yyyy')) <= to_date('" + strLogDateTime + "', 'mm/dd/yy hh24:mi:ss')";

		//Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
		strRootSQL += (SqlUtil.genDateConstraint("t.OCCUR_DATE", strFromDate, strToDate));

		try {
			String strKey;
			String strValues;
			String strLeader;
			int intProjectId;

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.createStatement();

			for (int i = 0; i < max; i++) {
				strKey = lstProject[i].toString();
				strSQL = strRootSQL + " AND p.code ='" + strKey + "'";

				if (!strGroup.equals("All")) {
					strSQL += " AND p.group_name ='" + strGroup + "'";
				}
				if (intProjectStatus != -1) {
					strSQL += " AND p.status ='" + intProjectStatus + "'";
				}
				strSQL += " GROUP BY p.code, p.leader, d.name, p.project_id," +
					" (CASE WHEN t.status IN (2,4,5)" +
					" AND NVL(t.plapprovedtime, to_date('31-Dec-9999','dd-mon-yyyy')) <= to_date('" + strLogDateTime + "', 'mm/dd/yy hh24:mi:ss')" +
					" THEN 1 ELSE 0 END)";

				//logger.debug("EJB.InquiryEJB.getUnapprovedPMEJB -- getUnapprovedPMEJB = " + strSQL);

				rs = stmt.executeQuery(strSQL);

				strValues = "";
				strLeader = "";
				intProjectId = 0;

				double dblTotalEffort = 0.0;
				double dblUnapprovedEffort = 0.0;
				double dblRatioEffort = 0.0;

				while (rs.next()) {
					strLeader = rs.getString("Leader");
					intProjectId = rs.getInt("ProjectId");
					if (rs.getInt("PL_Approve") == 0) {	// 0 - Unapproved, 1 - Approved
						strValues += rs.getString("Value") + ", ";
						dblUnapprovedEffort += rs.getDouble("Effort");
					}
					dblTotalEffort += rs.getDouble("Effort");
					dblRatioEffort = (dblUnapprovedEffort/dblTotalEffort)*100.0;
				}
				rs.close();

				if (strValues.length() > 2 && dblUnapprovedEffort > 0) {
					strValues = strValues.substring(0, strValues.length() - 2);

					ReportWeeklyModel beanReport = new ReportWeeklyModel();
					beanReport.setProjectId(intProjectId);
					beanReport.setKey(strKey);
					beanReport.setLeader(strLeader);
					beanReport.setValue(strValues);
					beanReport.setUnapprovedEffort(dblUnapprovedEffort);
					beanReport.setTotalEffort(dblTotalEffort);
					beanReport.setRatioEffort(dblRatioEffort);
					dataList.add(beanReport);
				}
			}
			stmt.close();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getUnapprovedPMEJB():" + ex.toString());
			//ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getUnapprovedPMEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getUnapprovedPMEJB(): ");
		} //finally

		return dataList;
	}

	/**
	 * Method getUnapprovedQAEJB
	 * @param strGroup
	 * @param intProjectID
	 * @param arrProjectIDs
	 * @param strFromDate
	 * @param strToDate
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getUnreviewedQAEJB(String strGroup, String strPQAName,
										 int intProjectID, String arrProjectIDs,
										 String strFromDate, String strToDate,
										 String strLogDateTime, int intProjectStatus) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;

		String strSQL;
		String strRootSQL;
		ArrayList dataList = new ArrayList();
		String[] lstProject = this.getProjectList(arrProjectIDs);

		int max = lstProject.length;
		if (max < 1)
			return dataList;

		strRootSQL = " SELECT DISTINCT p.project_id as ProjectId, p.code, " +
							" p.leader as Leader, d.name as Value, " +
							" (CASE WHEN t.status IN (4, 5) " +
							" AND NVL(t.plapprovedtime, to_date('31-Dec-9999','dd-mon-yyyy')) <= to_date('" + strLogDateTime + "', 'mm/dd/yy hh24:mi:ss')" +
							"THEN 1 ELSE 0 END) AS PQA_Approve, " +
							" SUM (t.duration) / 8 AS Effort " +
					 " FROM timesheet t, developer d, project p " +
					 " WHERE t.developer_id = d.developer_id AND t.project_id = p.project_id " +
					 " AND t.create_date <= to_date('" + strLogDateTime + "', 'mm/dd/yy hh24:mi:ss')";

		//Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
		strRootSQL += (SqlUtil.genDateConstraint("t.OCCUR_DATE", strFromDate, strToDate));

		try {
			String strKey;
			String strValues;

			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.createStatement();

			for (int i = 0; i < max; i++) {
				strKey = lstProject[i].toString();
				strSQL = strRootSQL + " AND p.code ='" + strKey + "'";

				if (!strGroup.equals("All")) {
					strSQL += " AND p.group_name ='" + strGroup + "'";
				}
				if (intProjectStatus != -1) {
					strSQL += " AND p.status ='" + intProjectStatus + "'";
				}

				strSQL += " GROUP BY p.code, p.leader, d.name, p.project_id," +
					" (CASE WHEN t.status IN (4,5) " +
					" AND NVL(t.plapprovedtime, to_date('31-Dec-9999','dd-mon-yyyy')) <= to_date('" + strLogDateTime + "', 'mm/dd/yy hh24:mi:ss')" +
					"THEN 1 ELSE 0 END)";

				//logger.debug("EJB.InquiryEJB.getUnapprovedQAEJB -- getUnapprovedQAEJB = " + strSQL);

				rs = stmt.executeQuery(strSQL);

				strValues = "";
				String strLeader = "";
				int intProjectIds = 0;

				double dblTotalEffort = 0.0;
				double dblUnapprovedEffort = 0.0;
				double dblRatioEffort = 0.0;

				while (rs.next()) {
					strLeader = rs.getString("Leader");
					intProjectIds = rs.getInt("ProjectId");
					if (rs.getInt("PQA_Approve") == 0) {	// 0 - Unapproved, 1 - Approved
						strValues += rs.getString("Value") + ", ";
						dblUnapprovedEffort += rs.getDouble("Effort");
					}
					dblTotalEffort += rs.getDouble("Effort");
					dblRatioEffort = (dblUnapprovedEffort/dblTotalEffort)*100.0;
				}
				rs.close();

				if (strValues.length() > 2 && dblUnapprovedEffort > 0) {
					strValues = strValues.substring(0, strValues.length() - 2);

					ReportWeeklyModel beanReport = new ReportWeeklyModel();
					beanReport.setProjectId(intProjectIds);
					beanReport.setKey(strKey);
					beanReport.setLeader(strLeader);
					beanReport.setValue(strValues);
					beanReport.setUnapprovedEffort(dblUnapprovedEffort);
					beanReport.setTotalEffort(dblTotalEffort);
					beanReport.setRatioEffort(dblRatioEffort);

					dataList.add(beanReport);
				}
			}
			stmt.close();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getUnapprovedQAEJB():" + ex.toString());
			//ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getUnapprovedQAEJB():" + ex.toString());
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getUnapprovedQAEJB(): ");
		} //finally

		return dataList;
	}

	/**
	 * Method getPQAList
	 * @param intProjectID
	 * @param arrProjectIDs
	 * @param strPQAName
	 * @return Collection
	 * @throws SQLException
	 */
	public Collection getPQAList(int intProjectID, String arrProjectIDs, String strPQAName) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;

		String strSQL;
		String strRootSQL;
		ArrayList dataList = new ArrayList();
		String[] lstProject = this.getProjectList(arrProjectIDs);
		int max = lstProject.length;
		if (max < 1)
			return dataList;

		strRootSQL = " SELECT DISTINCT p.project_id AS projectid, p.code AS projectcode, " +
					 " d.account AS pqaaccount, d.name AS pqaname " +							
					 " FROM developer d, assignment a, project p " +
					 " WHERE d.developer_id = a.developer_id AND p.project_id = a.project_id " +
					 " AND a.response = " + Timesheet.PQA_ROLE + " AND d.status != " + Timesheet.USER_QUIT;
		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.createStatement();

			for (int i = 0; i < max; i++) {
				String strProjectCode = lstProject[i].toString();
				strSQL = strRootSQL + " AND p.code ='" + strProjectCode + "'";

				if (strPQAName.equals("0") || strPQAName.equals("All")) {
					strSQL += " AND 1 = 1 ";
				}
				else {
					strSQL += " AND d.account ='" + strPQAName + "'";
				}
				strSQL += " GROUP BY p.project_id, p.code, d.account, d.name ";

				//logger.debug("EJB.InquiryEJB.getPQAList -- getPQAList = " + strSQL);

				rs = stmt.executeQuery(strSQL);

				String strName = "";
				String strAccount = "";
				int intProjectId = 0;

				boolean isExisted = false;
				if (rs.next()) {
					strName = rs.getString("pqaname");
					strAccount = rs.getString("pqaaccount");
					intProjectId = rs.getInt("projectid");
					isExisted = true;
				}
				while (rs.next()) {
					strName += ", " + rs.getString("pqaname");
					strAccount += ", " + rs.getString("pqaaccount");
					//intProjectId = rs.getInt("projectid");
				}
				rs.close();

				if (isExisted) {
					ReportWeeklyModel beanReport = new ReportWeeklyModel();
					beanReport.setProjectId(intProjectId);
					beanReport.setProjectCode(strProjectCode);
					beanReport.setPQAAccount(strAccount);
					beanReport.setPQAName(strName);
					dataList.add(beanReport);
				}
			}
			stmt.close();
		}
		catch (SQLException ex) {
			logger.error(strClassName + ".getPQAList():" + ex.toString());
			ex.printStackTrace();
		}
		catch (Exception ex) {
			logger.error(strClassName + ".getPQAList():" + ex.toString());
			ex.printStackTrace();
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getPQAList(): ");
		}
		return dataList;
	}	

	/**
	 * Get project effort distribution by Process, Type of work, KPA or Work product 
	 * @param nProjectID
	 * @param arrProjectIDs
	 * @param nStatus
	 * @param strFrom
	 * @param strTo
	 * @param nReportType
	 * @param type
	 * @param strDeveloperID
	 * @return ArrayList, the structure is two list:
	 * - ArrayList: list of ConstantRow
	 * - ArrayList: list of ProjectPivotRow (report rows)
	 * @throws SQLException
	 */
	public ArrayList getProjectDistribution(int nProjectID,
											 String arrProjectIDs,
											 int nStatus,
											 String strFrom,
											 String strTo,
											 int nReportType,
											 int type, 
											 String strDeveloperID) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		String [] strProject;
		String strProjectSQL;

		ArrayList listReport = new ArrayList(); // Contain listRow and listConst
		ArrayList listRow = new ArrayList();    // Report rows
		ArrayList listConst = getConstantList(nReportType); // List of constants
		listReport.add(listConst);

		String strPivotColumn = getColumnByReportType(nReportType);
		StringBuffer sbSQL = new StringBuffer();
		StringBuffer sbConstList = new StringBuffer();

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();

			sbSQL.append("SELECT SUM(CASE WHEN ")
				 .append(strPivotColumn)
				 .append(" IS NULL THEN NULL ELSE duration END) AS total,p.code as pcode ");

			// Build distribution report
			Iterator itr = listConst.iterator();
			while (itr.hasNext()) {
				ConstantRow row = (ConstantRow) itr.next();
				sbConstList.append(row.getID()).append(",");
				sbSQL.append(",SUM(CASE WHEN ")
					 .append(strPivotColumn)
					 .append("=").append(row.getID())
					 .append(" THEN duration ELSE NULL END) AS c_").append(row.getID());
			}
			if (sbConstList.length() > 0) {
				sbConstList.deleteCharAt(sbConstList.length() - 1);
			}
			sbSQL.append(",SUM(CASE WHEN ")
				 .append(strPivotColumn)
				 .append(" NOT IN(").append(sbConstList)
				 .append(") THEN duration ELSE NULL END) AS others");
			// End: Build distribution report

			sbSQL.append(" FROM timesheet T, project P");
			sbSQL.append(" WHERE T.project_id=P.project_id");
            
			// Append conditions by client parameters
			if (nProjectID != 0) {
				sbSQL.append(" AND P.project_id = " + nProjectID);
			}
			else if (arrProjectIDs != null && arrProjectIDs.length() > 0) {
				//sbSQL.append(" AND P.project_id IN (" + arrProjectIDs + ")");
				//HanhTN fix maximum 1000 records -- 03/05/2007
				strProject = CommonUtil.split(arrProjectIDs, ",");
				for (int i=0; i<strProject.length; i++) {
					strProjectSQL = "INSERT INTO project_temp (project_id) VALUES ("+ strProject[i] +")";
					stmt.addBatch(strProjectSQL);
				}
				stmt.executeBatch();
				sbSQL.append(" AND P.project_id IN (select project_id from project_temp)");
			}
			if (type != 10) {
				sbSQL.append(" AND P.type = " + type);
			}
			// Filter by DeveloperID
			if ((strDeveloperID != null) && (strDeveloperID.length() > 0)) {
				sbSQL.append(" AND T.developer_ID=").append(strDeveloperID);
			}
			sbSQL.append(SqlUtil.genDateConstraint("T.occur_date", strFrom, strTo));
			switch (nStatus) {
				case 0:
					sbSQL.append(" AND T.status = 1"); //Unapproved
					break;
				case 1:
					sbSQL.append(" AND T.status IN (2, 3)");   //Approved By PL, GL,
					break;
				case 2:
					sbSQL.append(" AND T.status = 4"); //Approved By QA
					break;
				case 3:
					sbSQL.append(" AND T.status IN (1, 2, 3, 4)"); //Not Rejected
					break;
			}
			// End: Append conditions by client parameters
			sbSQL.append(" GROUP BY p.code");

			//logger.debug("@HanhTN -- InquiryEJB.getProjectDistribution -- sbSQL=" + sbSQL);

			int nCol;
			rs = stmt.executeQuery(sbSQL.toString());
			while (rs.next()) {
				// Avoid projects without efforts
				if (rs.getFloat("total") == 0.0) {
					continue;
				}
				ProjectPivotRow row = new ProjectPivotRow();
				row.setProjectCode(rs.getString("pcode"));
				// pd(Person Day) ~ ph(Person Hour) / 8
				row.setTotal(rs.getFloat("total") / 8);
				row.setOthers(rs.getFloat("others"));
				float[] arrEffort = new float[listConst.size()];    // Efforts by constant fields
				row.setArrEffort(arrEffort);
				for (nCol = 0; nCol < listConst.size(); nCol++) {
					// pd(Person Day) ~ ph(Person Hour) / 8
					arrEffort[nCol] = rs.getFloat(nCol + 3) / 8;    // Efforts is begining from column 3 (after TOTAL and PCODE)
				}
				listRow.add(row);
			}
			listReport.add(listRow);
		}
		catch (SQLException e) {
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conPool.releaseResourceAndCommit(con, stmt, rs, strClassName + ".getProjectDistribution(): ");
		}
		return listReport; 
	}

	/**
	 * Get column name for distribution report by report type
	 * @param nReportType
	 * @return
	 */
	private static String getColumnByReportType(int nReportType) {
		switch (nReportType) {
			case (Timesheet.REPORTTYPE_PROJECT_PROCESS):
				return "process_id";
			case (Timesheet.REPORTTYPE_PROJECT_PRODUCT):
				return "wp_id";
			case (Timesheet.REPORTTYPE_PROJECT_KPA):
				return "kpa_id";
			case (Timesheet.REPORTTYPE_PROJECT_TOW):
				return "tow_id";
			default:
				return "";
		}
	}

	/**
	 * Get constant list by report type
	 * @param nReportType
	 * @return
	 * @throws SQLException
	 */
	private ArrayList getConstantList(int nReportType) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList listConst = null;
		String strSQL = "SELECT ";
		switch (nReportType) {
			case (Timesheet.REPORTTYPE_PROJECT_PROCESS):
				strSQL += "process_id, name FROM process";
				break;
			case (Timesheet.REPORTTYPE_PROJECT_PRODUCT):
				strSQL += "wp_id, name FROM workproduct";
				break;
			case (Timesheet.REPORTTYPE_PROJECT_KPA):
				strSQL += "kpa_id, name FROM kpa";
				break;
			case (Timesheet.REPORTTYPE_PROJECT_TOW):
				strSQL += "tow_id, name FROM typeofwork";
				break;
		}
        
		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSQL);
			listConst = new ArrayList();
			while (rs.next()) {
				ConstantRow row = new ConstantRow();
				row.setID(rs.getInt(1));
				row.setTitle(rs.getString(2));
				listConst.add(row);
			}
		}
		catch (SQLException e) {
		}
		finally {
			conPool.releaseResource(con, stmt, rs, strClassName + ".getConstantList(): ");
		}
		return listConst;
	}

	/**
	 * Get summary effort of projects
	 * @param nProjectID
	 * @param arrProjectIDs
	 * @param nStatus
	 * @param strFrom
	 * @param strTo
	 * @param nReportType
	 * @param type
	 * @param strDeveloperID
	 * @return List of ProjectSummaryRow
	 * @throws SQLException
	 */
	public ArrayList getProjectSummary(int nProjectID, String arrProjectIDs,
									   int nStatus, String strFrom, String strTo,
									   int nReportType, int type,
									   String strDeveloperID) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		String [] strProject;
		String strProjectSQL;
		ArrayList listSummary = new ArrayList();

		try {
			if (ds == null) {
				ds = conPool.getDS();
			}
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();

			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT SUM(duration) AS total,p.code as pcode");
			sbSql.append(",SUM(CASE WHEN 1=1")
				 .append(SqlUtil.genManagement("T.tow_id","T.process_id"))
				 .append(" THEN duration ELSE NULL END) AS sum_management");

			sbSql.append(",SUM(CASE WHEN 1=1")
				 .append(SqlUtil.genQuality("T.tow_id","T.process_id"))
				 .append(" THEN duration ELSE NULL END) AS sum_quality");

			sbSql.append(",SUM(CASE WHEN 1=1")
				 .append(SqlUtil.genCorrection("T.tow_id","T.process_id"))
				 .append(" THEN duration ELSE NULL END) AS sum_correction");

			sbSql.append(",SUM(CASE WHEN 1=1")
				 .append(SqlUtil.genTranslation("T.tow_id"))
				 .append(" THEN duration ELSE NULL END) AS sum_translation");

			sbSql.append(" FROM timesheet T, project P");
			sbSql.append(" WHERE T.project_id=P.project_id");
        
			String strFilter = "";
			switch (nStatus) {
				case 0:
					strFilter = " AND T.status=1"; //Unapproved
					break;
				case 1:
					strFilter = " AND T.status IN (2, 3)";  //Approved By PL or GL
					break;
				case 2:
					strFilter = " AND T.status=4"; //Approved By QA
					break;
				case 3:
					strFilter = " AND T.status IN (1, 2, 3, 4)"; //Not Rejected
					break;
			}
			if (type != Timesheet.PROJECTTYPE_ALL) {
				strFilter += " AND P.type=" + type;
			}

			// Filter by DeveloperID
			String strAccountFilter = "";
			if ((strDeveloperID != null) && (strDeveloperID.length() > 0)) {
				strAccountFilter = " AND Developer_ID=" + strDeveloperID;
			}
			//Filter by ProjectID
			String strProjectFilter = "";
			if (nProjectID != 0) {
				strProjectFilter = " AND P.project_id=" + nProjectID;
			}
			else if ((arrProjectIDs != null) && (arrProjectIDs.length() > 0)) {
				//strProjectFilter = " AND P.project_id IN (" + arrProjectIDs + ")";
				//HanhTN fix maximum 1000 records -- 03/05/2007
				strProject = CommonUtil.split(arrProjectIDs, ",");
				for (int i=0; i<strProject.length; i++) {
					strProjectSQL = "INSERT INTO project_temp (project_id) VALUES ("+ strProject[i] +")";
					stmt.addBatch(strProjectSQL);
				}
				stmt.executeBatch();
				strProjectFilter = " AND P.project_id IN (select project_id from project_temp)";
			}
			// Gen date constrain statements
			String strPeriodFilter = SqlUtil.genDateConstraint("OCCUR_DATE", strFrom, strTo);

			sbSql.append(strFilter)
				 .append(strAccountFilter)
				 .append(strProjectFilter)
				 .append(strPeriodFilter);
			sbSql.append("GROUP BY p.code");

			//logger.debug("InquiryEJB.getProjectSummary -- sbSql = " + sbSql);

			rs = stmt.executeQuery(sbSql.toString());
			while (rs.next()) {
				ProjectSummaryRow row = new ProjectSummaryRow();
				row.setProjectCode(rs.getString("pcode"));
				// pd(person day) ~ ph(person hour) / 8
				row.setTotal(rs.getFloat("total") / 8);
				row.setManagement(rs.getFloat("sum_management") / 8);
				row.setQuality(rs.getFloat("sum_quality") / 8);
				// Total = Development + Management + Quality
				row.setDevelopment(row.getTotal() - row.getManagement() - row.getQuality());

				row.setCorrection(rs.getFloat("sum_correction") / 8);
				row.setTranslation(rs.getFloat("sum_translation") / 8);

				//row.roundNumbers(); // Round and format all fields
				listSummary.add(row);
			}
		}
		catch (SQLException e) {
		}
		finally {
			conPool.releaseResourceAndCommit(con, stmt, rs, strClassName + ".getProjectSummary(): ");
		}
		return listSummary;
	}
	//Thaison - Oct 15, 2002
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
}