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
 
 package com.fms1.common.group;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Vector;
import java.sql.Date;

import jxl.CellView;
import jxl.NumberCell;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import javax.servlet.http.HttpServletRequest;

import com.fms1.tools.CommonTools;
import com.fms1.web.Constants;
import com.fms1.web.Fms1Servlet;
import com.fms1.web.ServerHelper;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.CalendarEffort;
import com.fms1.infoclass.group.HumanResourceInfo;


/**
 * 
 * @author Nguyen Van Hieu- Hieunv1
 * @version 1.0 - 20/Oct/2007
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class HumanResource {
	
	static WritableFont writeFont = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.NO_BOLD);
	static WritableCellFormat cellFormat = new WritableCellFormat(writeFont);
	static jxl.write.NumberFormat fmNumber = new jxl.write.NumberFormat("0.0#"); 
	static WritableCellFormat numberCell = new WritableCellFormat(fmNumber);

	/**
	 * get list of calendar effort of one project or all projects for group.
	 * @param group
	 * @param projectId
	 * @param fromDate
	 * @param toDate
	 * @param table: either ASSIGNMENT or ASSIGNMEN_PLAN
	 * @return
	 */
	public static Vector getCalendarEffortList(HumanResourceInfo resourceInfo){
		Vector result = new Vector();
		Connection conn = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		String sql = null;
		String condition = "";
		String conditionDate = "";
		String conditionUser = "";
		double totalCalendarEffort = 0.0;
		try{
			Date cfToDate = null;
			Date cfFromDate = null;
			condition += (resourceInfo.getProjectId() > 0) ? " AND a.project_id = ?" : "";
			if (resourceInfo.getStatus() != -1){
				condition += " AND P.STATUS = ?";
			}
			if (resourceInfo.getProjectType() != -1){
				condition += " AND P.TYPE = ?";
			}
			if (resourceInfo.getFromDate() != null) {
				cfFromDate = CommonTools.parseSQLDate(resourceInfo.getFromDate());
				conditionDate += " AND NVL (p.actual_finish_date, "
										   + "NVL (p.plan_finish_date, p.base_finish_date)) >= ?";
			}
			if (resourceInfo.getToDate() != null){
				cfToDate = CommonTools.parseSQLDate(resourceInfo.getToDate());
				conditionDate += " AND NVL (p.START_DATE, p.PLAN_START_DATE) <= ?";
			}
			if (resourceInfo.getFromDate() != null){
				conditionUser += " AND A.END_DATE >= ?";
			}
			if (resourceInfo.getToDate() != null){
				conditionUser += " AND A.BEGIN_DATE <= ?";
			}

			sql = "SELECT   p.PROJECT_ID, p.CODE, a.BEGIN_DATE, a.END_DATE, a.USAGE"
					+ " FROM PROJECT p, ASSIGNMENT a"
					+ " WHERE a.PROJECT_ID = p.PROJECT_ID"
					+ " 	AND p.GROUP_NAME =?"
					+ condition
					+ conditionDate
					+ conditionUser
					+ "		AND a.RESPONSE NOT IN (5, 7, 8) "
					+ " ORDER BY p.CODE";
			conn = ServerHelper.instance().getConnection();
			preStm = conn.prepareStatement(sql);

			int index = 0;
			preStm.setString(++index, resourceInfo.getUserGroup());
			if (resourceInfo.getProjectId() > 0 ){
				preStm.setLong(++index, resourceInfo.getProjectId());
			}
			if (resourceInfo.getStatus() != -1){
				preStm.setInt(++index, resourceInfo.getStatus());
			}
			if (resourceInfo.getProjectType() != -1){
				preStm.setInt(++index, resourceInfo.getProjectType());
			}
			if (resourceInfo.getFromDate() != null){
				preStm.setDate(++index, CommonTools.parseSQLDate(resourceInfo.getFromDate()));
			}
			if (resourceInfo.getToDate() != null){
				preStm.setDate(++index, CommonTools.parseSQLDate(resourceInfo.getToDate()));
			}
			if (resourceInfo.getFromDate() != null){
				preStm.setDate(++index, CommonTools.parseSQLDate(resourceInfo.getFromDate()));
			}
			if (resourceInfo.getToDate() != null){
				preStm.setDate(++index, CommonTools.parseSQLDate(resourceInfo.getToDate()));
			}
			rs = preStm.executeQuery();
			long prevousProject = 0;
			double dCalendarEffort = 0.0;
			CalendarEffort calendarEffort = new CalendarEffort();

			while (rs.next()){
				Date begin_date = rs.getDate("BEGIN_DATE");
				Date end_date = rs.getDate("END_DATE");
				if (cfFromDate != null && begin_date.getTime() < cfFromDate.getTime()){
					begin_date = cfFromDate;
				}
				if (cfToDate != null && end_date.getTime() > cfToDate.getTime()){
					end_date = cfToDate;
				}
				double workingDays = CommonTools.getWorkingDays(begin_date, end_date);
				if (prevousProject != rs.getLong("PROJECT_ID")){
					if (prevousProject != 0){
						calendarEffort.setActualCalendarEffort(dCalendarEffort);
						calendarEffort.setPlannedCalendarEffort(dCalendarEffort);
						result.add(calendarEffort);
						totalCalendarEffort = CommonTools.addDouble(totalCalendarEffort, dCalendarEffort);
						dCalendarEffort = 0.0;
					}
					prevousProject = rs.getLong("PROJECT_ID");
					calendarEffort =  new CalendarEffort();
					calendarEffort.setProjectId(prevousProject);
					calendarEffort.setProjectCode(rs.getString("CODE"));
					//calculate calendar effort
					dCalendarEffort += workingDays * rs.getDouble("USAGE") / 100.0;
				}
				else {
					dCalendarEffort += workingDays * rs.getDouble("USAGE") / 100.0;
				}
			}
			if (prevousProject != 0) {
				calendarEffort.setActualCalendarEffort(dCalendarEffort);
				calendarEffort.setPlannedCalendarEffort(dCalendarEffort);
				result.add(calendarEffort);
				totalCalendarEffort = CommonTools.addDouble(totalCalendarEffort, dCalendarEffort);
			}
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally{
			ServerHelper.closeConnection(conn, preStm, rs);
			result.add(new Double(totalCalendarEffort));
			return result;
		}
	}
	/**
	 * get all information of Human resource from ResultSet 
	 * @param rs
	 * @param resourceBean
	 * @return
	 */
	public static HumanResourceInfo getResourceInfo(ResultSet rs, HumanResourceInfo resourceBean){
		HumanResourceInfo humanInfo = new HumanResourceInfo();
		try {
			humanInfo.setProjectGroup(rs.getString("PROJECT_GROUP"));
			humanInfo.setUserGroup(rs.getString("USER_GROUP"));
			humanInfo.setProjectStatusName(rs.getInt("PROJECT_STATUS"));
			humanInfo.setProjectCode(rs.getString("PROJECT_CODE"));
			humanInfo.setUserAccount(rs.getString("ACCOUNT"));
			humanInfo.setWorkingTime(rs.getDouble("USAGE"));

			Date begin_date = rs.getDate("BEGIN_DATE");
			Date end_date = rs.getDate("END_DATE");
			Date fromDate = null;
			Date toDate = null;
			if (resourceBean.getFromDate() != null){
				fromDate = CommonTools.parseSQLDate(resourceBean.getFromDate());
			}
			if (resourceBean.getFromDate() != null){
				toDate = CommonTools.parseSQLDate(resourceBean.getToDate());
			}
			if (fromDate != null && begin_date.getTime() < fromDate.getTime()){
				begin_date = fromDate;
			}
			if (toDate != null && end_date.getTime() > toDate.getTime()){
				end_date = toDate;
			}

			humanInfo.setBeginAssingment(rs.getDate("BEGIN_DATE"));
			humanInfo.setEndAssingment(rs.getDate("END_DATE"));
			double workingDays = CommonTools.getWorkingDays(begin_date,	end_date);
			double calendarEffort = workingDays * humanInfo.getWorkingTime() / 100.0;
			humanInfo.setUserCalendarEffort(calendarEffort);
			humanInfo.setUserRole(rs.getInt("RESPONSE"));
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally {
			return humanInfo;
		}
	}
	/**
	 * Get Resource Allocation List
	 * @param resourceBean
	 * @return
	 */
	public static Vector getResourceAllocationList(HumanResourceInfo resourceBean){
		Vector result = new Vector();
		Connection conn = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		String sql = null;
		double totalUserEffort = 0.0;
		try {
			sql = doGenerateSQLResource(resourceBean);
			conn = ServerHelper.instance().getConnection();
			preStm = conn.prepareStatement(sql);
			doSetIndexSQL(preStm, resourceBean);
			rs = preStm.executeQuery();
			while (rs.next()){
				HumanResourceInfo humanInfo = getResourceInfo(rs, resourceBean);
				totalUserEffort = CommonTools.addDouble(totalUserEffort, humanInfo.getUserCalendarEffort());
				result.add(humanInfo);
			}
		}
		catch (SQLException ex){
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, rs);
			//The last element of Result is total User Effort.
			result.add(new Double(totalUserEffort));
			return result;
		}
	}
	/**
	 * Export Resource Allocation List
	 * @param resourceBean
	 * @param servlet
	 * @return true if export Successfully, false if export have error
	 */
	public static boolean doExportResourceAllocationList(HumanResourceInfo resourceBean, Fms1Servlet servlet){
		boolean result = true;
		try{
			String strExcelFile = CommonTools.getRealPath(servlet) + CommonTools.doGenerateFile("HumanResourceFI_", ".xls");
			resourceBean.setFileName(strExcelFile);
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			WritableWorkbook workbook = Workbook.createWorkbook(new File(strExcelFile), ws);
			WritableSheet sheetResource = workbook.createSheet("ResourceAllocation", 0);
			doWriteFileTile(sheetResource);
			result = doWriteResourceSheet(resourceBean, sheetResource);
			workbook.write();
			workbook.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		}
		finally {
			return result;
		}
	}
	/**
	 * get all information from Database then write to Sheet of Excel
	 * @param resourceBean
	 * @param resourceSheet
	 * @return
	 */
	public static boolean doWriteResourceSheet(HumanResourceInfo resourceBean, WritableSheet resourceSheet){
		Connection conn = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		String sql = null;
		boolean result = true;
		try {
			sql = doGenerateSQLResource(resourceBean);
			conn = ServerHelper.instance().getConnection();
			preStm = conn.prepareStatement(sql);
			doSetIndexSQL(preStm, resourceBean);
			rs = preStm.executeQuery();
			int count = 1;
			double totalUserEffort = 0.0;
			while (rs.next()) {
				HumanResourceInfo resourceData = getResourceInfo(rs, resourceBean);
				doWriteResourceData(resourceData, resourceSheet, count);
				totalUserEffort = CommonTools.addDouble(totalUserEffort, resourceData.getUserCalendarEffort());
				count++;
			}
			if (count == 1){
				result = false;
			}
			else {
				doWriteTotalEffortExport(totalUserEffort, resourceSheet, count);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, rs);
			return result;
		}
	}
	/**
	 * to add final row into the sheet to view total
	 * @param totalCalendarEffort
	 * @param resourceSheet
	 * @param count
	 */
	public static void doWriteTotalEffortExport(final double totalCalendarEffort, WritableSheet resourceSheet, int count){
		try{
			Label lblProjectGroup		= new Label(0,  count, "", cellFormat);
			Label lblUserGroup 			= new Label(1, count, "", cellFormat);
			Label lblProject_Code 		= new Label(2, count, "", cellFormat);
			Label lblProject_status 	= new Label(3, count, "", cellFormat);
			Label lblAccount 			= new Label(4, count, "", cellFormat);
			Label lblWorkingTime 		= new Label(5, count, "", cellFormat);
			Label lblBeginAssignment 	= new Label(6, count, "", cellFormat);
			Label lblEndAssignment 		= new Label(7, count, "", cellFormat);
			Label lblRole 				= new Label(8, count, "", cellFormat);
			Number lblCalendarEffort 	= new Number(9, count, totalCalendarEffort, numberCell);
			
			resourceSheet.addCell(lblUserGroup);
			resourceSheet.addCell(lblProject_Code);
			resourceSheet.addCell(lblProject_status);
			resourceSheet.addCell(lblAccount);
			resourceSheet.addCell(lblWorkingTime);
			resourceSheet.addCell(lblBeginAssignment);
			resourceSheet.addCell(lblEndAssignment);
			resourceSheet.addCell(lblRole);
			resourceSheet.addCell(lblCalendarEffort);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * Write data to Excel File
	 * @param resourceData
	 * @param resourceSheet
	 * @param count
	 */
	public static void doWriteResourceData(HumanResourceInfo resourceData, WritableSheet resourceSheet, int count){
		try {
			Label lblProjectGroup		= new Label(0,  count, resourceData.getProjectGroup(), cellFormat);
			Label lblUserGroup 			= new Label(1, count, resourceData.getUserGroup(), cellFormat);
			Label lblProject_Code 		= new Label(2, count, resourceData.getProjectCode(), cellFormat);
			Label lblProject_status 	= new Label(3, count, resourceData.getProjectStatusName(), cellFormat);
			Label lblAccount 			= new Label(4, count, resourceData.getUserAccount(), cellFormat);
			Number lblWorkingTime 		= new Number(5, count, resourceData.getWorkingTime(), numberCell);
			Label lblBeginAssignment 	= new Label(6, count, CommonTools.dateFormat(resourceData.getBeginAssignment()), cellFormat);
			Label lblEndAssignment 		= new Label(7, count, CommonTools.dateFormat(resourceData.getEndAssignment()), cellFormat);
			Label lblRole 				= new Label(8, count, resourceData.getUserRole(), cellFormat);
			Number lblCalendarEffort 	= new Number(9, count, resourceData.getUserCalendarEffort(), numberCell);
			
			resourceSheet.addCell(lblProjectGroup);
			resourceSheet.addCell(lblUserGroup);
			resourceSheet.addCell(lblProject_Code);
			resourceSheet.addCell(lblProject_status);
			resourceSheet.addCell(lblAccount);
			resourceSheet.addCell(lblWorkingTime);
			resourceSheet.addCell(lblBeginAssignment);
			resourceSheet.addCell(lblEndAssignment);
			resourceSheet.addCell(lblRole);
			resourceSheet.addCell(lblCalendarEffort);
			
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * Create Title of Excel file
	 * @param resourceSheet
	 */
	public static void doWriteFileTile(WritableSheet resourceSheet){
		try {
			WritableFont writeFontTitle = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLUE);
			WritableCellFormat cellFormatTitle = new WritableCellFormat(writeFontTitle);
			cellFormatTitle.setAlignment(Alignment.CENTRE);
			cellFormatTitle.setWrap(true);
			CellView cvShort = new CellView();
			CellView cvMedium = new CellView();
			CellView cvLong	= new CellView();
			cvShort.setSize(10 * 256);
			cvMedium.setSize(18 * 256);
			cvLong.setSize(19 * 256);
			//Set Column for Resource Sheet
			cvShort.setFormat(cellFormatTitle);
			resourceSheet.setColumnView(0, cvMedium);
			resourceSheet.setColumnView(1, cvMedium);
			resourceSheet.setColumnView(2, cvMedium);
			resourceSheet.setColumnView(3, cvMedium);
			resourceSheet.setColumnView(4, cvMedium);
			resourceSheet.setColumnView(5, cvMedium);
			resourceSheet.setColumnView(6, cvLong);
			resourceSheet.setColumnView(7, cvMedium);
			resourceSheet.setColumnView(8, cvMedium);
			resourceSheet.setColumnView(9, cvMedium);
			
			//Label Title
			Label lblTitle_ProjectGroup		= new Label(0,  0, "PROJECT_GROUP", cellFormatTitle);
			Label lblTitle_UserGroup		= new Label(1,  0, "USER_GROUP", cellFormatTitle);
			Label lblTitle_ProjectCode		= new Label(2,  0, "PROJECT_CODE", cellFormatTitle);
			Label lblTitle_ProjectStatus	= new Label(3,  0, "PROJECT_STATUS", cellFormatTitle);
			Label lblTitle_Account			= new Label(4,  0, "ACCOUNT", cellFormatTitle);
			Label lblTitle_WorkingTime		= new Label(5,  0, "WORKING_TIME", cellFormatTitle);
			Label lblTitle_BeginAssignment	= new Label(6,  0, "BEGIN_DATE", cellFormatTitle);
			Label lblTitle_EndAssignment	= new Label(7,  0, "END_DATE", cellFormatTitle);
			Label lblTitle_Role				= new Label(8,  0, "ROLE", cellFormatTitle);
			Label lblTitle_CalendarEffort	= new Label(9,  0, "CALENDAR_EFFORT", cellFormatTitle);
			
			resourceSheet.addCell(lblTitle_ProjectGroup);
			resourceSheet.addCell(lblTitle_UserGroup);
			resourceSheet.addCell(lblTitle_ProjectCode);
			resourceSheet.addCell(lblTitle_ProjectStatus);
			resourceSheet.addCell(lblTitle_Account);
			resourceSheet.addCell(lblTitle_WorkingTime);
			resourceSheet.addCell(lblTitle_BeginAssignment);
			resourceSheet.addCell(lblTitle_EndAssignment);
			resourceSheet.addCell(lblTitle_Role);
			resourceSheet.addCell(lblTitle_CalendarEffort);
		}
		catch (WriteException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Generate SQL for get All Resource List or Export Resource Allocation
	 * @param resourceInfo
	 * @return
	 */
	public static String doGenerateSQLResource(HumanResourceInfo resourceInfo){
		String sql = null;
		String conditionDate = "";
		String conditionStatus = "";
		String conditionGroup = "";
		String conditionProject = "";
		String conditionClosedCancelled = "";
		String conditionOngoingTentative = "";
		String conditionUserTime = "";
		try{
			if (resourceInfo.getFromDate() != null){
				conditionOngoingTentative += " AND NVL (p.actual_finish_date, "
										   + "NVL (p.plan_finish_date, p.base_finish_date)) >= ?";

				conditionClosedCancelled += " AND NVL (p.actual_finish_date, "
												+ " NVL (p.plan_finish_date, p.base_finish_date)) >= ?";
			}
			if (resourceInfo.getToDate() != null){
				conditionOngoingTentative += " AND NVL (p.START_DATE, p.PLAN_START_DATE) <= ?"; 

				conditionClosedCancelled += " AND NVL (p.actual_finish_date, "
												+ " NVL (p.plan_finish_date, p.base_finish_date)) <=?";
			}
			
			if (resourceInfo.getFromDate() != null){
				conditionUserTime += " AND A.END_DATE >= ?";
			}
			if (resourceInfo.getToDate() != null){
				conditionUserTime += " AND A.BEGIN_DATE <= ?";
			}
			switch (resourceInfo.getStatus()){
				case -1:
					if (!"".equals(conditionOngoingTentative)){
						conditionDate += " AND (((P.STATUS = 0 OR P.STATUS = 3) " + conditionOngoingTentative + ")";
					}
					if (!"".equals(conditionClosedCancelled)){
						conditionDate += " OR ((P.STATUS = 1 OR P.STATUS = 2) " + conditionClosedCancelled + "))";
					}
					break;
				case ProjectInfo.STATUS_TENTATIVE:
					conditionDate = conditionOngoingTentative;
					break;
				case ProjectInfo.STATUS_ONGOING:
					conditionDate = conditionOngoingTentative;
					break;
				case ProjectInfo.STATUS_CLOSED:
					conditionDate = conditionClosedCancelled;
					break;
				case ProjectInfo.STATUS_CANCELLED:
					conditionDate = conditionClosedCancelled;
					break;
			}
			if (resourceInfo.getStatus() != -1){ //if all status then ignore
				conditionStatus += " AND P.STATUS = ?";
			}
			if (resourceInfo.getUserBy() == Constants.HR_USER_BY_GROUP){
				conditionGroup += " AND D.GROUP_NAME = ?";
			}
			else if (resourceInfo.getUserBy() == Constants.HR_USER_BY_PROJECT) {
				conditionGroup += " AND P.GROUP_NAME = ?";
			}
			else if (resourceInfo.getUserBy() == Constants.HR_USER_BY_ALL){
				conditionGroup += " AND( P.GROUP_NAME = ? ";
				conditionGroup += " OR D.GROUP_NAME = ? )";
			}
			if (resourceInfo.getProjectId() != 0){
				conditionProject += " AND P.PROJECT_ID = ?";
			}
			sql = " SELECT p.GROUP_NAME AS PROJECT_GROUP, d.GROUP_NAME AS USER_GROUP, "
						+ " p.STATUS AS PROJECT_STATUS, P.CODE PROJECT_CODE, D.ACCOUNT, A.USAGE, " 
						+ " A.BEGIN_DATE, A.END_DATE, A.RESPONSE"
				+ " FROM PROJECT P, DEVELOPER D, ASSIGNMENT A"
				+ " WHERE P.PROJECT_ID = A.PROJECT_ID"
						+ "	AND A.DEVELOPER_ID = D.DEVELOPER_ID"
						+ conditionGroup
						+ conditionProject
						+ conditionStatus
						+ conditionDate
						+ conditionUserTime
				+ " ORDER BY p.group_name, PROJECT_CODE, ACCOUNT";
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			return sql;
		}
	}
	/**
	 * set Index of PreparedStatement
	 * @param preStm
	 * @param resourceBean
	 */
	public static void doSetIndexSQL(PreparedStatement preStm, HumanResourceInfo resourceBean){
		try {
			int index = 0;
//			Date projectFromDate = CommonTools.parseSQLDate(resourceBean.getProjectFromDate());
//			Date projectToDate = CommonTools.parseSQLDate(resourceBean.getProjectToDate());
			Date resourceFromDate = CommonTools.parseSQLDate(resourceBean.getFromDate());
			Date resourceToDate = CommonTools.parseSQLDate(resourceBean.getToDate());
			if (resourceBean.getUserBy() == Constants.HR_USER_BY_GROUP){
				preStm.setString(++index, resourceBean.getUserGroup());
			}
			else if (resourceBean.getUserBy() == Constants.HR_USER_BY_PROJECT) {
				preStm.setString(++index, resourceBean.getProjectGroup());
			}
			else if (resourceBean.getUserBy() == Constants.HR_USER_BY_ALL){
				preStm.setString(++index, resourceBean.getProjectGroup());
				preStm.setString(++index, resourceBean.getUserGroup());
			}
			if (resourceBean.getProjectId() != 0){
				preStm.setLong(++index, resourceBean.getProjectId());
			}
			if (resourceBean.getStatus() != -1){
				preStm.setInt(++index, resourceBean.getStatus());
			}
			if (resourceBean.getToDate() != null && resourceBean.getFromDate() != null) {
				if (resourceBean.getStatus() == -1) {
					preStm.setDate(++index, resourceFromDate);
					preStm.setDate(++index, resourceToDate);
				}
				preStm.setDate(++index, resourceFromDate);
				preStm.setDate(++index, resourceToDate);
			}
			else if (resourceBean.getFromDate() != null){
				if (resourceBean.getStatus() == -1) {
					preStm.setDate(++index, resourceFromDate);
				}
				preStm.setDate(++index, resourceFromDate);
			}
			else if (resourceBean.getToDate() != null){
				if (resourceBean.getStatus() == -1) {
					preStm.setDate(++index, resourceToDate);
				}
				preStm.setDate(++index, resourceToDate);
			}
			if (resourceBean.getFromDate() != null){
				preStm.setDate(++index, resourceFromDate);
			}
			if (resourceBean.getToDate() != null){
				preStm.setDate(++index, resourceToDate);
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}