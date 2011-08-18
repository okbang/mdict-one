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
 
 package fpt.dms.bo.DefectManagement;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

//import com.jspsmart.upload.SmartUpload;

import fpt.dms.bean.ComboBoxExt;
import fpt.dms.bean.UserInfoBean;
import fpt.dms.bean.DefectManagement.DefectAddBean;
import fpt.dms.bean.DefectManagement.DefectAttachBean;
import fpt.dms.bean.DefectManagement.DefectBatchUpdateBean;
import fpt.dms.bean.DefectManagement.DefectListingBean;
import fpt.dms.bean.DefectManagement.DefectUpdateBean;
import fpt.dms.constant.DMS;
import fpt.dms.framework.connection.WSConnectionPooling;
import fpt.dms.framework.util.CommonUtil.CommonUtil;
import fpt.dms.framework.util.DateUtil.DateUtil;
import fpt.dms.framework.util.StringUtil.StringMatrix;
import fpt.dms.framework.util.StringUtil.StringVector;


public class DefectBO {

	private WSConnectionPooling conPool = new WSConnectionPooling();

	private static Logger logger = Logger.getLogger(QueryBO.class.getName());

	public DefectBO() {
	}

	/**
	 * Method getDefectList.
	 * @param beanDefectList
	 * @param beanUserInfo
	 * @param isForExport
	 * @return DefectListingBean
	 * @throws SQLException
	 * @throws Exception
	 */
	public DefectListingBean getDefectList(DefectListingBean beanDefectList,
										   UserInfoBean beanUserInfo,
										   boolean isForExport) throws SQLException, Exception {

		StringMatrix smResult = new StringMatrix();
		DataSource ds = null;
		ResultSet rs = null;
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();

		//STEP 1 - Get data from client
		//Data for sort
		String strOrder = beanDefectList.getSortBy();
		String strDirection = "";
		//data for filter
		String strTypeOfView = beanUserInfo.getTypeOfView();
		String DefectOwner = beanDefectList.getDefectOwner();
		String AssignTo = beanDefectList.getAssignTo();
		String CreatedBy = beanDefectList.getCreatedBy();
		String Status = beanDefectList.getStatus();
		String WorkProduct = beanDefectList.getWorkProduct();
		String ModuleCode = beanDefectList.getModuleCode();
		String Severity = beanDefectList.getSeverity();
		String Priority = beanDefectList.getPriority();
		String StageDefected = beanDefectList.getStageDefected();
		String QCActivity = beanDefectList.getQCActivity();
		String StageInjected = beanDefectList.getStageInjected();
		String DefectOrigin = beanDefectList.getDefectOrigin();
		String Type = beanDefectList.getType();
		String FromDate = beanDefectList.getFromDate();
		String ToDate = beanDefectList.getToDate();
		String Ref = beanDefectList.getRef();
		String strFixedFrom = beanDefectList.getFixedFrom();
		String strFixedTo = beanDefectList.getFixedTo();
		String strDefectID = beanDefectList.getDefectID();
		String strTitle = beanDefectList.getTitle();
		String strTestCase = beanDefectList.getTestCase();

		int nProjectID = beanUserInfo.getProjectID();
		int nTotalpage = 0;
		int nNumDefect = 0;
		int nNumpage = Integer.parseInt(beanDefectList.getNumpage());
		int nItem = 20;     //Number of Defects per display screen
		///////////////////////////////////////////////////////////////////////////////////////////////////
        
		if (beanDefectList.getDirection() > 0) {
			strDirection = " ASC";
		}
		else {
			strDirection = " DESC";
		}

		//STEP 2 - Build SQL statements
		strSQL.append(" SELECT defect_id as ID, title as TITLE, defect_severity.name as DEFS, ");
		strSQL.append(" defect_priority.name as PRIORITY, defect_status.name as STATUS, assigned_to as ASSIGNEDTO, Defect_Owner as DEFECTOWNER, ");

		/**
		 * Modified by: Tu Ngoc Trung
		 * Date: 2003-11-10
		 * Purpose: Correct date in export file, format is 'DD-MON-YYYY' */
		if (isForExport) {
			strSQL.append(" TO_CHAR(due_date,'DD-MON-YYYY') as DUEDATE, created_by as CREATEDBY, ");
			strSQL.append(" TO_CHAR(fixed_date, 'DD-MON-YYYY') as FIXEDDATE, TO_CHAR(create_date, 'DD-MON-YYYY') as CREATEDATE, ");
		}
		else {
			strSQL.append(" TO_CHAR(due_date,'mm/dd/yy') as DUEDATE, created_by as CREATEDBY, ");
			strSQL.append(" TO_CHAR(fixed_date, 'mm/dd/yy') as FIXEDDATE, TO_CHAR(create_date, 'mm/dd/yy') as CREATEDATE, ");
		}

		strSQL.append(" description as DESCRIPTION ");
		/** Added by PhuongNT, Friday, July 25, 2003 */
		if (isForExport) {
			strSQL.append(", project_origin as PROJECT_ORIGIN, reference, qc_activity.name as QC_ACTIVITY");
			strSQL.append(", process.name as DEFECT_ORIGIN, activity_type.name as TYPE_OF_ACTIVITY");
			strSQL.append(", stage_inj.name as STAGE_INJECTED, stage_det.name as STAGE_DETECTED");
			strSQL.append(", workproduct.name as WORK_PRODUCT, module.name as MODULE_NAME");
			strSQL.append(", defect_type.name as DEF_TYPE, solution as CORRECTIVE_ACTION");
			strSQL.append(", TO_CHAR(close_date, 'DD-MON-YYYY') as CLOSEDATE, cause_analysis AS CAUSE_ANALYSIS ");
		}
		/** End */
		strSQL.append(", test_case as TEST_CASE ");

		String strWhereSQL = "";
		//HanhTN add beanUserInfo for checking External User
		strWhereSQL = createDataConstraint(beanUserInfo, strTypeOfView, nProjectID, isForExport);        

		strSQL.append(strWhereSQL);

		StringBuffer strCountSQL = new StringBuffer();
		strCountSQL.append(" SELECT count(defect_id) as num ");
		strCountSQL.append(strWhereSQL);
		String strFilter = "";
        
		/**
		 * Modified by: Tu Ngoc Trung
		 * Date: 2003-10-21
		 * Purpose: Check selected option is [ALL], [NULL] or other options*/
		if (DefectOwner != null) {
					//USER_ID is [NULL]
			if (ComboBoxExt.STR_NONE_STRING.equals(DefectOwner)) {
				strFilter += " AND defect.Defect_Owner IS NULL ";
			}
					//Any USER_ID
			else if ( !(ComboBoxExt.STR_ALL_STRING.equals(DefectOwner) || 
                         "".equals(DefectOwner))) {
			    strFilter += " AND defect.Defect_Owner = '" + DefectOwner + "'";
			}
		}
		
		if (AssignTo != null) {
			     //USER_ID is [NULL]
			if (ComboBoxExt.STR_NONE_STRING.equals(AssignTo)) {
				strFilter += " AND defect.assigned_to IS NULL ";
			}
			//Any USER_ID
			else if ( !(ComboBoxExt.STR_ALL_STRING.equals(AssignTo) ||
						 "".equals(AssignTo))) {
				strFilter += " AND defect.assigned_to = '" + AssignTo + "'";
			}
		}        
		if (CreatedBy != null) {
			//USER_ID is [NULL]
			if (ComboBoxExt.STR_NONE_STRING.equals(CreatedBy)) {
				strFilter += " AND defect.created_by IS NULL ";
			}
			//Any USER_ID
			else if (! (ComboBoxExt.STR_ALL_STRING.equals(CreatedBy) ||
						 "".equals(CreatedBy)) ) {
				strFilter += " AND defect.created_by = '" + CreatedBy + "'";
			}
		}
        
		if (ComboBoxExt.STR_NONE_VALUE.equals(ModuleCode)) {
			//defect.module_id = 0 means that NULL value
			strFilter += " AND (defect.module_id IS NULL OR defect.module_id = 0) ";
		}
		else if (! (ComboBoxExt.STR_ALL_VALUE.equals(ModuleCode) ||
					 "".equals(CreatedBy) )) {
			strFilter += " AND defect.module_id = " + ModuleCode;
		}
        
		if (ComboBoxExt.STR_NONE_VALUE.equals(StageDefected)) {
			//defect.sd_id = 0 means that NULL value
			strFilter += " AND (defect.sd_id IS NULL OR defect.sd_id = 0) ";
		}
		else if (! (ComboBoxExt.STR_ALL_VALUE.equals(StageDefected) ||
					 "".equals(CreatedBy) )) {
			strFilter += " AND defect.sd_id = " + StageDefected;
		}
        
		if (ComboBoxExt.STR_NONE_VALUE.equals(StageInjected)) {
			//defect.si_id = 0 means that NULL value
			strFilter += " AND (defect.si_id IS NULL OR defect.si_id = 0) ";
		}
		else if (! (ComboBoxExt.STR_ALL_VALUE.equals(StageInjected) ||
					 "".equals(CreatedBy) )) {
			strFilter += " AND defect.si_id = " + StageInjected;
		}
        
		if (ComboBoxExt.STR_NONE_VALUE.equals(Ref)) {
			strFilter += " AND defect.reference IS NULL ";
		}
		else if (! (ComboBoxExt.STR_ALL_VALUE.equals(Ref) ||
					 "".equals(Ref) )) {
			strFilter += " AND defect.reference = '" + Ref + "'";
		}

		if (ComboBoxExt.STR_NONE_VALUE.equals(Priority)) {
			//defect.dp_id = 0 means that NULL value
			strFilter += " AND (defect.dp_id IS NULL OR defect.dp_id = 0) ";
		}
		else if (! (ComboBoxExt.STR_ALL_VALUE.equals(Priority) ||
					 "".equals(Priority) )) {
			strFilter += " AND defect.dp_id = " + Priority;
		}

		if (! ComboBoxExt.STR_ALL_VALUE.equals(Status)) {
			strFilter += " AND defect.ds_id = " + Status;
		}
		if (! ComboBoxExt.STR_ALL_VALUE.equals(WorkProduct)) {
			strFilter += " AND defect.wp_id = " + WorkProduct;
		}
		if (! ComboBoxExt.STR_ALL_VALUE.equals(Severity)) {
			strFilter += " AND defect.defs_id = " + Severity;
		}
		if (! ComboBoxExt.STR_ALL_VALUE.equals(QCActivity)) {
			strFilter += " AND defect.qa_id = " + QCActivity;
		}
		if (! ComboBoxExt.STR_ALL_VALUE.equals(DefectOrigin)) {
			strFilter += " AND defect.process_id = " + DefectOrigin;
		}
		if (! ComboBoxExt.STR_ALL_VALUE.equals(Type)) {
			strFilter += " AND defect.dt_id = " + Type;
		}
        
		/**End*/
        
		if (!"".equals(FromDate)) {
			strFilter += " AND defect.create_date >= TO_DATE('"
					+ FromDate
					+ "', 'MM/DD/YY')";
		}
		if (!"".equals(ToDate)) {
			strFilter += " AND defect.create_date <= TO_DATE('"
					+ ToDate
					+ "', 'MM/DD/YY')";
		}
        
		if (!"".equals(strFixedFrom)) {
			strFilter += " AND defect.fixed_date >= TO_DATE('"
					+ strFixedFrom
					+ "', 'MM/DD/YY')";
		}
		if (!"".equals(strFixedTo)) {
			strFilter += " AND defect.fixed_date <= TO_DATE('"
					+ strFixedTo
					+ "', 'MM/DD/YY')";
		}
		if (!"".equals(strDefectID)) {
			strFilter += " AND defect.defect_id = " + strDefectID;
		}
		// Search by title => full text search
		if (!"".equals(strTitle)) {
			if(strTitle.indexOf("%")>=0){            
				strFilter += " AND lower(defect.title) = '" + CommonUtil.stringConvert(strTitle.toLowerCase()) + "'";
			}
			else{
				strFilter += " AND lower(defect.title) LIKE '%" + CommonUtil.stringConvert(strTitle.toLowerCase()) + "%'";
			}
		}	
		// Search by title => full text search
		if (!"".equals(strTestCase)) {
			if(strTestCase.indexOf("%")>=0){                           
				strFilter += " AND lower(defect.test_case) = '" + CommonUtil.stringConvert(strTestCase.toLowerCase()) + "'";
			}
			else{
				strFilter += " AND lower(defect.test_case) LIKE '%" + CommonUtil.stringConvert(strTestCase.toLowerCase()) + "%'";
			}
		}
	
		if (strOrder.equals("DefectID")) {
			strFilter += " ORDER BY defect.defect_id" + strDirection;
		}
		else if (strOrder.equals("Title")) {
			strFilter += " ORDER BY defect.title" + strDirection;
		}
		else if (strOrder.equals("TestCase")) {
			strFilter += " ORDER BY defect.test_case" + strDirection;
		}        
		else if ("DueDate".equals(strOrder)) {
			strFilter += " ORDER BY defect.due_date" + strDirection;
		}
		else if (strOrder.equals("Severity")) {
			strFilter += " ORDER BY defect_severity.name" + strDirection;
		}
		else if (strOrder.equals("Priority")) {
			strFilter += " ORDER BY defect_priority.name" + strDirection;
		}
		else if (strOrder.equals("Status")) {
			strFilter += " ORDER BY defect_status.name " + strDirection;
		}
		else if (strOrder.equals("AssignTo")) {
			strFilter += " ORDER BY defect.assigned_to " + strDirection;
		}
		else if ("CreatedBy".equals(strOrder)) {
			strFilter += " ORDER BY defect.created_by" + strDirection;
		}
		else if ("FixedDate".equals(strOrder)) {
			strFilter += " ORDER BY defect.fixed_date" + strDirection;
		}
		else {
			strFilter += " ORDER BY defect.defect_id" + strDirection;
		}
		strSQL.append(strFilter);
		strCountSQL.append(strFilter);

		//logger.debug("@HanhTN -- DefectBO -- strSQL = " + strSQL.toString());

		if (DMS.DEBUG) {
			logger.debug("strCountSQL = " + strCountSQL.toString());
			logger.debug("strSQL = " + strSQL.toString());
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////

		//STEP 3 - Execute SQL statements
		//get data from database here
		Connection con = null;
		try {
			/**
			 * Modified by: Tu Ngoc Trung
			 * Date: 2003-10-31
			 * Description: Number of records can get directly by JDBC 2.0,
			 *              also jump directly to any position in scrollable cursor.
			 */
			/**
			ds = conPool.getDS();
			con = ds.getConnection();
			//get number of record
			prepStmtCount = con.prepareStatement(strCountSQL.toString());
			rsCount = prepStmtCount.executeQuery();
			while (rsCount.next()) {
				nNumDefect = rsCount.getInt("num");
			}
            
			rsCount.close();
			prepStmtCount.close();

			nTotalpage = (nNumDefect / nItem);
			if ((nNumDefect % nItem) != 0) {
				nTotalpage = nTotalpage + 1;
			}
			int intStart = nNumpage * nItem;
			int intEnd = nItem;
			prepStmt = con.prepareStatement(strSQL.toString());
			rs = prepStmt.executeQuery();

			int countRow = 0;
			// for (int i = 0; i < intStart; i++) {
				rs.next();
			}
			*/
			ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL.toString(),
											ResultSet.TYPE_SCROLL_SENSITIVE,
											ResultSet.CONCUR_READ_ONLY);
            // Set other fetch size rather than default value 10 => scroll faster
            // because data is cached but should use this value carefully or you should clear about it 
            prepStmt.setFetchSize(100);
            
            rs = prepStmt.executeQuery();
			rs.last();                  // Go to last record
			nNumDefect = rs.getRow();   // =>Number of rows returned
			nTotalpage = ((nNumDefect + nItem - 1) / nItem);
			// If selected page is larger than total pages => reset page number
			if (nNumpage + 1 > nTotalpage) {
				nNumpage = 0;
				beanDefectList.setNumpage(Integer.toString(nNumpage));
			}
			int intStart = nNumpage * nItem;
			int intEnd = nItem;
            
			rs.first();
			rs.relative(intStart - 1);  //Begin fetch position of records
			/**End*/
            
			StringVector tmpVector;
			if (isForExport) {
				tmpVector = new StringVector(26);
			}
			else {
				tmpVector = new StringVector(13);
			}
//Removed this solution            
			// Plain format or fine format of export file
			//boolean bPlainMode = ("on".equals(beanDefectList.getExportMode()));
			int countRow = 0;
			while (rs.next()) {
				tmpVector.setCell(0, rs.getString("ID"));
				tmpVector.setCell(
						1,
				CommonUtil.correctHTMLError(rs.getString("TITLE")));
				tmpVector.setCell(2, rs.getString("DEFS"));
				tmpVector.setCell(3, rs.getString("PRIORITY"));
				tmpVector.setCell(4, rs.getString("STATUS"));
				tmpVector.setCell(5, rs.getString("ASSIGNEDTO"));
				tmpVector.setCell(6, rs.getString("DUEDATE"));
				tmpVector.setCell(7, rs.getString("CREATEDBY")); //Created by
				tmpVector.setCell(8, rs.getString("FIXEDDATE")); //Fixed date
				tmpVector.setCell(9, rs.getString("CREATEDATE"));
				tmpVector.setCell(
						10, CommonUtil.correctHTMLError(rs.getString("DESCRIPTION")));

				/** Added by PhuongNT, Friday, July 25, 2003 */
				if (isForExport) {
					tmpVector.setCell(11, CommonUtil.correctHTMLError(rs.getString("PROJECT_ORIGIN")));
					tmpVector.setCell(12, CommonUtil.correctHTMLError(rs.getString("reference")));
					tmpVector.setCell(13, rs.getString("QC_ACTIVITY"));
					tmpVector.setCell(14, rs.getString("DEFECT_ORIGIN"));
					tmpVector.setCell(15, rs.getString("TYPE_OF_ACTIVITY"));
					tmpVector.setCell(16, rs.getString("STAGE_INJECTED"));
					tmpVector.setCell(17, rs.getString("STAGE_DETECTED"));
					tmpVector.setCell(18, rs.getString("WORK_PRODUCT"));
					tmpVector.setCell(19, CommonUtil.correctHTMLError(rs.getString("MODULE_NAME")));
					tmpVector.setCell(20, rs.getString("DEF_TYPE"));
					tmpVector.setCell(21, CommonUtil.correctHTMLError(rs.getString("CORRECTIVE_ACTION")));
					tmpVector.setCell(22, rs.getString("CLOSEDATE"));
					tmpVector.setCell(23, CommonUtil.correctHTMLError(rs.getString("CAUSE_ANALYSIS")));
					tmpVector.setCell(24, CommonUtil.correctHTMLError(rs.getString("TEST_CASE")));
					tmpVector.setCell(25, CommonUtil.correctHTMLError(rs.getString("DEFECTOWNER")));
					//new solution
					tmpVector.setCell(10, CommonUtil.insertHtmlTag(tmpVector.getCell(10), "\n", "", "<br style='mso-data-placement:same-cell;'/>"));
					tmpVector.setCell(21, CommonUtil.insertHtmlTag(tmpVector.getCell(21), "\n", "", "<br style='mso-data-placement:same-cell;'/>"));		
					tmpVector.setCell(23, CommonUtil.insertHtmlTag(tmpVector.getCell(23), "\n", "", "<br style='mso-data-placement:same-cell;'/>"));

//Removed this solution				
// Correct display of return characters when export, 29-July-2004
//					// If not export as plain format then set format for easier view
//					if (! bPlainMode) {
//						// Description
//						//tmpVector.setCell(10, CommonUtil.correctErrorChar(tmpVector.getCell(10), '\n', "<BR>"));
//						// Corrective action
//						//tmpVector.setCell(21, CommonUtil.correctErrorChar(tmpVector.getCell(21), '\n', "<BR>"));
//						tmpVector.setCell(10, CommonUtil.insertHtmlTag(tmpVector.getCell(10), "\r\n", "<DIV>", "</DIV>"));
//						tmpVector.setCell(21, CommonUtil.insertHtmlTag(tmpVector.getCell(21), "\r\n", "<DIV>", "</DIV>"));				
//					}

				}else{
					tmpVector.setCell(11, CommonUtil.correctHTMLError(rs.getString("TEST_CASE")));			
					tmpVector.setCell(12, CommonUtil.correctHTMLError(rs.getString("DEFECTOWNER")));
				}
				/** End */

				smResult.addRow(tmpVector);
				countRow++;
				if ((countRow > intEnd - 1) && (!beanDefectList.IsExportAll())) {
					break;
				}
			}
			if (DMS.DEBUG) {
				logger.debug(
						"smResult.getNumberOfRows() = "
						+ smResult.getNumberOfRows());
			}

			beanDefectList.setDefectList(smResult);
			beanDefectList.setTotalpage(String.valueOf(nTotalpage));
			beanDefectList.setTotalRecord(String.valueOf(nNumDefect));
		}
		catch (SQLException ex) {
			logger.error(
					"SQLException occurs in DefectBO.getDefectList(): "
					+ ex.getMessage()
					+ " strSQL = "
					+ strSQL.toString());
		}
		catch (Exception e) {
			logger.error(
					"Exception occurs in DefectBO.getDefectList(): "
					+ e.getMessage());
		}
		finally {
			conPool.releaseResource(con, prepStmt, rs,
							"DefectBO.getDefectList(): ");
		}
		////////////////////////////////////////////////////////////////////////////////////////////////

		return beanDefectList;
	}

	/**
	 * Method getDefectInfo.
	 * @param beanDefect
	 * @param beanUserInfo
	 * @return DefectUpdateBean
	 * @throws SQLException
	 * @throws Exception
	 */
	public DefectUpdateBean getDefectInfo(
			DefectUpdateBean beanDefect,
			UserInfoBean beanUserInfo)
			throws SQLException, Exception {
		DataSource ds = null;
		Connection con = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		StringBuffer strSQL = new StringBuffer();

		SimpleDateFormat dateFormat = new SimpleDateFormat(DMS.DATE_FORMAT);
		strSQL.append("SELECT * FROM defect WHERE defect_id = " + beanDefect.getDefectID());
		strSQL.append(" AND project_id=" + beanDefect.getProjectId());
        
		if (DMS.DEBUG) {
			logger.debug("DefectBO.getDefectInfo(). strSQL=" + strSQL);
		}
		try {
			ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL.toString());
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				beanDefect.setDefectID(rs.getString("defect_id"));
				beanDefect.setCreateUser(rs.getString("Created_By"));
				beanDefect.setCreateDate(
						dateFormat.format(rs.getDate("Create_date")));
				beanDefect.setTitle(
						CommonUtil.correctHTMLError(rs.getString("Title")));

				beanDefect.setAssignTo(
						CommonUtil.correctHTMLError(rs.getString("Assigned_To")));
				beanDefect.setTestCase(
						CommonUtil.correctHTMLError(rs.getString("Test_Case")));
				beanDefect.setStatus(rs.getInt("DS_ID"));
				beanDefect.setWorkProduct(rs.getInt("WP_ID"));
				beanDefect.setSeverity(rs.getInt("DEFS_ID"));
				beanDefect.setModuleCode(rs.getInt("Module_ID"));
				beanDefect.setTypeofActivity(rs.getInt("AT_ID"));
				beanDefect.setPriority(rs.getInt("DP_ID"));
				beanDefect.setType(rs.getInt("DT_ID"));
				beanDefect.setQCActivity(rs.getInt("QA_ID"));
				beanDefect.setDefectOrigin(rs.getInt("Process_ID"));
				beanDefect.setStageDetected(rs.getInt("SD_ID"));
				beanDefect.setStageInjected(rs.getInt("SI_ID"));
				beanDefect.setDefectOwner(CommonUtil.correctHTMLError(rs.getString("Defect_Owner")));
				if (rs.getString("Due_Date") != null
						&& rs.getString("Due_Date").length() > 0) {
					beanDefect.setDueDate(
							dateFormat.format(rs.getDate("Due_Date")));
				}
				else {
					beanDefect.setDueDate("");
				}
				if (rs.getString("Close_Date") != null
						&& rs.getString("Close_Date").length() > 0) {
					beanDefect.setCloseDate(
							dateFormat.format(rs.getDate("Close_Date")));
				}
				else {
					beanDefect.setCloseDate("");
				}
				beanDefect.setDescription(
						CommonUtil.correctHTMLError(rs.getString("Description")));
				beanDefect.setCorrectiveAction(
						CommonUtil.correctHTMLError(rs.getString("Solution")));
				beanDefect.setCauseAnalysis(
						CommonUtil.correctHTMLError(rs.getString("CAUSE_ANALYSIS")));
				if (rs.getString("Image") != null) {
					beanDefect.setStrImage(rs.getString("Image"));
				}
				else {
					beanDefect.setStrImage("");
				}

				if (rs.getString("Project_Origin") != null) {
					beanDefect.setStrProject_Origin(
							CommonUtil.correctHTMLError(
									rs.getString("Project_Origin")));
				}
				else {
					beanDefect.setStrProject_Origin("");
				}
				if (rs.getString("Reference") != null) {
					beanDefect.setReference(
							CommonUtil.correctHTMLError(rs.getString("Reference")));
				}
				else {
					beanDefect.setReference("");
				}

				//Thaison - Sep 27, 2002
				if (rs.getString("fixed_date") != null
						&& rs.getString("fixed_date").length() > 0) {
					beanDefect.setFixedDate(
							dateFormat.format(rs.getDate("fixed_date")));
				}
				else {
					beanDefect.setFixedDate("");
				}
			}
			else {
				beanDefect.setDefectID("0");
				//beanUserInfo.set
			}
			rs.close();
			prepStmt.close();
//			  prepStmt = con.prepareStatement("SELECT content FROM defect_attachment WHERE defect_id=?");
//			  prepStmt.setString(1, beanDefect.getDefectID());
//			  rs = prepStmt.executeQuery();
//			  int i = 0;
//			  while (rs.next()) {
//				  if (i == 2) {
//					  logger.debug("attach content:" + rs.getString(1));
//				  }
//				  i++;
//			  }
//			  rs.close();
//			  prepStmt.close();
		}
		catch (SQLException ex) {
			logger.error(
					"SQLException occurs in DefectBO.getDefectInfo(): "
					+ ex.getMessage()
					+ " strSQL = "
					+ strSQL.toString());
		}
		catch (Exception e) {
			logger.error(
					"Exception occurs in DefectBO.getDefectInfo(): "
					+ e.getMessage());
		}
		finally {
			conPool.releaseResource(con, prepStmt, rs,
							"DefectBO.getDefectInfo(): ");
		}

		return beanDefect;
	}

	/**
	 * Method deleteDefect.
	 * @param beanDefectList
	 * @throws SQLException
	 * @throws Exception
	 */
	public void deleteDefect(DefectListingBean beanDefectList)
			throws SQLException, Exception {
		DataSource ds = null;
		PreparedStatement prepStmt = null;
		Connection con = null;
		StringBuffer strSQL = new StringBuffer();
		try {
			String[] arrDefectID = beanDefectList.getArrDeleteDefect();
			String strDelete = "";
			for (int i = 0; i < arrDefectID.length; i++) {
				strDelete = strDelete + arrDefectID[i] + ",";
			}
			strDelete = strDelete.substring(0, strDelete.length() - 1);
			strSQL.append(
					"DELETE FROM defect WHERE defect_id IN (" + strDelete + " )");

			ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);

			prepStmt = con.prepareStatement(strSQL.toString());
			prepStmt.executeUpdate();
			prepStmt.close();
			con.commit();
		}
		catch (SQLException ex) {
			con.rollback();
			logger.error(
					"SQLException occurs in DefectBO.deleteDefect(): "
					+ ex.getMessage()
					+ " strSQL = "
					+ strSQL.toString());
		}
		catch (Exception e) {
			con.rollback();
			logger.error(
					"Exception occurs in DefectBO.deleteDefect(): "
					+ e.getMessage());
		}
		finally {
			conPool.releaseResourceAndCommit(con, prepStmt, null,
							"DefectBO.deleteDefect(): ");
		}

		return;
	}

	/**
	 * Method deleteDefect.
	 * @param nDefectID DefectID
	 * @return int Number of deleted defects (1-successfull)
	 * @throws SQLException
	 * @throws Exception
	 */
	public int deleteDefect(int nDefectID) throws SQLException, Exception {
		DataSource ds = null;
		Connection con = null;
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		int nUpdates = 0;
		try {
			strSQL.append("DELETE FROM defect WHERE defect_id = " + nDefectID);
			ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);

			prepStmt = con.prepareStatement(strSQL.toString());
			nUpdates = prepStmt.executeUpdate();
		}
		catch (SQLException ex) {
			con.rollback();
			logger.error(
					"SQLException occurs in DefectBO.deleteDefect(): "
					+ ex.getMessage()
					+ ", strSQL = "
					+ strSQL.toString());
		}
		catch (Exception e) {
			con.rollback();
			e.printStackTrace();
		}
		finally {
			conPool.releaseResourceAndCommit(con, prepStmt, null,
							"DefectBO.deleteDefect(): ");
		}

		return nUpdates;
	}

	/**
	 * Method getBatchUpdateDefect.
	 * @param strIDs
	 * @return StringMatrix
	 * @throws SQLException
	 * @throws Exception
	 */
	public StringMatrix getBatchUpdateDefect(String strIDs)
			throws SQLException, Exception {
		StringMatrix smResult = new StringMatrix();
		Connection con = null;
		DataSource ds = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		StringBuffer strSQL = new StringBuffer();
		//modified by MinhPT 
		//08-Dec-03
		//for get title of the defect
		strSQL.append(" SELECT Defect_ID, DS_ID, DEFS_ID, DP_ID, ASSIGNED_TO, Due_Date, Title, Fixed_Date, Solution, DEFECT_OWNER ");
		strSQL.append(" FROM Defect");
		strSQL.append(" WHERE Defect_ID IN (" + strIDs + ") ORDER BY Defect_ID DESC ");

		SimpleDateFormat dateFormat = new SimpleDateFormat(DMS.DATE_FORMAT);

		try {
			ds = conPool.getDS();
			con = ds.getConnection();
			prepStmt = con.prepareStatement(strSQL.toString());
			rs = prepStmt.executeQuery();

			while (rs.next()) {
				StringVector rowVector = new StringVector(10);
				rowVector.setCell(0, rs.getString("Defect_ID"));
				rowVector.setCell(1, rs.getString("DS_ID"));
				rowVector.setCell(2, rs.getString("DEFS_ID"));
				rowVector.setCell(3, rs.getString("DP_ID"));
				rowVector.setCell(4, CommonUtil.correctHTMLError(rs.getString("ASSIGNED_TO")));
				if (rs.getString("Due_Date") != null && rs.getString("Due_Date").length() > 0) {
					rowVector.setCell(5, dateFormat.format(rs.getDate("Due_Date")));
				}
				else {
					rowVector.setCell(5, "");
				}
				rowVector.setCell(6, CommonUtil.correctHTMLError(rs.getString("title")));
				if (rs.getString("Fixed_Date") != null && rs.getString("Fixed_Date").length() > 0) {
					rowVector.setCell(7, dateFormat.format(rs.getDate("Fixed_Date")));
				}
				else {
					rowVector.setCell(7, "");
				}
				rowVector.setCell(8, rs.getString("Solution"));
				rowVector.setCell(9, CommonUtil.correctHTMLError(rs.getString("DEFECT_OWNER")));
				smResult.addRow(rowVector);
			} //end while
			rs.close();
			prepStmt.close();
		} // end try
		catch (SQLException ex) {
			logger.error(
					"SQLException occurs in DefectBO.getBatchUpdateDefect(): "
					+ ex.getMessage()
					+ " strSQL = "
					+ strSQL.toString());
		}
		catch (Exception e) {
			logger.error(
					"Exception occurs in DefectBO.getBatchUpdateDefect(): "
					+ e.getMessage());
		}
		finally {
			conPool.releaseResource(con, prepStmt, rs,
							"DefectBO.getBatchUpdateDefect(): ");
		}

		return smResult;
	}

	/**
	 * Method addDefect.
	 * @param beanDefect
	 * @param beanUserInfo
	 * @throws SQLException
	 * @throws Exception
	 */
	public void addDefect(DefectAddBean beanDefect, UserInfoBean beanUserInfo)
			throws SQLException, Exception {
		DataSource ds = null;
		Connection con = null;
		PreparedStatement prepStmt = null;
		/*PreparedStatement prepStmtID = null;
		ResultSet rsID = null;
		StringBuffer strSQL = new StringBuffer();*/

		// Get defect_id for new defect entry, by get next Value from Sequence..
		//strSQL.append("SELECT Defect_Seq.NextVal AS Defect_ID FROM Dual");

		try {
			long nDefectId = conPool.getNextSeq("Defect_seq");
			beanDefect.setDefectID(Long.toString(nDefectId));
			ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);

			/*prepStmtID = con.prepareStatement(strSQL.toString());
			rsID = prepStmtID.executeQuery();
			rsID.next();

			rsID.close();
			prepStmtID.close();*/

			StringBuffer strFieldNames = new StringBuffer();
			strFieldNames.append(
					"defect_id,project_id, created_by, create_date, title, assigned_to, ds_id, wp_id, defs_id,");
			strFieldNames.append(
					" module_id, at_id, dp_id, dt_id, qa_id, process_id, sd_id, si_id, due_date, description,");
			strFieldNames.append(
					" solution, cause_analysis, updated_by, image, reference, project_origin,test_case,DEFECT_OWNER");

			StringBuffer strFieldValues = new StringBuffer();
			strFieldValues.append(nDefectId).append(",");
			strFieldValues.append(beanUserInfo.getProjectID() + ", ");
			strFieldValues.append(
					"'"
					+ CommonUtil.stringConvert(beanDefect.getCreateUser())
					+ "', ");
			strFieldValues.append(
					"TO_DATE('"
					+ beanDefect.getCreateDate()
					+ "', '"
					+ DMS.DATE_FORMAT
					+ "'), ");
			strFieldValues.append(
					"'" + CommonUtil.stringConvert(beanDefect.getTitle()) + "', ");
			strFieldValues.append(
					"'"
					+ CommonUtil.stringConvert(beanDefect.getAssignTo())
					+ "', ");
			strFieldValues.append(beanDefect.getStatus() + ", ");
			strFieldValues.append(beanDefect.getWorkProduct() + ", ");
			strFieldValues.append(beanDefect.getSeverity() + ", ");
			strFieldValues.append(beanDefect.getModuleCode() + ", ");
			strFieldValues.append(beanDefect.getTypeofActivity() + ", ");
			strFieldValues.append(beanDefect.getPriority() + ", ");
			strFieldValues.append(beanDefect.getType() + ", ");
			strFieldValues.append(beanDefect.getQCActivity() + ", ");
			strFieldValues.append(beanDefect.getDefectOrigin() + ", ");
			strFieldValues.append(beanDefect.getStageDetected() + ", ");
			strFieldValues.append(beanDefect.getStageInjected() + ", ");
			strFieldValues.append(
					"TO_DATE('"
					+ beanDefect.getDueDate()
					+ "', '"
					+ DMS.DATE_FORMAT
					+ "'), ");
			strFieldValues.append(
					"'"
					+ CommonUtil.stringConvert(beanDefect.getDescription())
					+ "', ");
			strFieldValues.append(
					"'"
					+ CommonUtil.stringConvert(beanDefect.getCorrectiveAction())
					+ "', ");
			strFieldValues.append(
					"'"
					+ CommonUtil.stringConvert(beanDefect.getCauseAnalysis())
					+ "', ");

			strFieldValues.append("'', ");
			strFieldValues.append(
					"'"
					+ CommonUtil.stringConvert(beanDefect.getStrImage())
					+ "', ");
			strFieldValues.append(
					"'"
					+ CommonUtil.stringConvert(beanDefect.getReference())
					+ "', ");
			strFieldValues.append(
					"'"
					+ CommonUtil.stringConvert(beanDefect.getStrProject_Origin())
					+ "',");
			strFieldValues.append(
					"'"
					+ CommonUtil.stringConvert(beanDefect.getTestCase())
					+ "',");
			strFieldValues.append(
					"'"
					+ CommonUtil.stringConvert(beanDefect.getDefectOwner())
					+ "'");

			// do insert into DataBase..
			String strInsertSQL =
					"INSERT INTO Defect ("
					+ strFieldNames.toString()
					+ ") VALUES ("
					+ strFieldValues.toString()
					+ ")";
			if (DMS.DEBUG) {
				logger.info("DefectBO(). strInsertSQL : " + strInsertSQL);
			}
			prepStmt = con.prepareStatement(strInsertSQL);
			prepStmt.executeQuery();
			con.commit();
			prepStmt.close();
		}
		catch (SQLException ex) {
			con.rollback();
			logger.error(
					"SQLException occurs in DefectBO.addDefect(): "
					+ ex.getMessage());
		}
		catch (Exception e) {
			con.rollback();
			logger.error(
					"Exception occurs in DefectBO.addDefect(): " + e.getMessage());
		}
		finally {
			conPool.releaseResourceAndCommit(con, prepStmt, null,
							"DefectBO.addDefect(): ");
		}
	}

	/**
	 * Method updateDefect.
	 * @param beanDefect
	 * @param beanUserInfo
	 * @throws SQLException
	 * @throws Exception
	 */
	public void updateDefect(DefectUpdateBean beanDefect,
							 UserInfoBean beanUserInfo)
			throws SQLException, Exception {
		DataSource ds = null;
		Connection con = null;
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();

		try {
			String updateImg = "";
			updateImg = ",Image='" + beanDefect.getStrImage() + "'";

			strSQL.append("UPDATE defect SET");
			strSQL.append(" project_id = " + beanUserInfo.getProjectID());
			strSQL.append(
					", created_by = '"
					+ CommonUtil.stringConvert(beanDefect.getCreateUser())
					+ "'");
			strSQL.append(
					", create_date = TO_DATE('"
					+ beanDefect.getCreateDate()
					+ "', '"
					+ DMS.DATE_FORMAT
					+ "')");
			strSQL.append(
					", title = '"
					+ CommonUtil.stringConvert(beanDefect.getTitle())
					+ "'");	
			strSQL.append(
					", assigned_to = '"
					+ CommonUtil.stringConvert(beanDefect.getAssignTo())
					+ "'");
			strSQL.append(
					", test_case = '"
					+ CommonUtil.stringConvert(beanDefect.getTestCase())
					+ "'");

			strSQL.append(", ds_id = " + beanDefect.getStatus());
			strSQL.append(", wp_id = " + beanDefect.getWorkProduct());
			strSQL.append(", defs_id = " + beanDefect.getSeverity());
			strSQL.append(", module_id = " + beanDefect.getModuleCode());
			strSQL.append(", at_id = " + beanDefect.getTypeofActivity());
			strSQL.append(", dp_id = " + beanDefect.getPriority());
			strSQL.append(", dt_id = " + beanDefect.getType());
			strSQL.append(", qa_id = " + beanDefect.getQCActivity());
			strSQL.append(", process_id = " + beanDefect.getDefectOrigin());
			strSQL.append(", sd_id = " + beanDefect.getStageDetected());
			strSQL.append(", si_id = " + beanDefect.getStageInjected());
			strSQL.append(
					", due_date = TO_DATE('"
					+ beanDefect.getDueDate()
					+ "', '"
					+ DMS.DATE_FORMAT
					+ "')");
			if (!"".equals(beanDefect.getFixedDate().trim()))
				strSQL.append(
						", fixed_date = TO_DATE('"
						+ beanDefect.getFixedDate()
						+ "', '"
						+ DMS.DATE_FORMAT
						+ "')");
			//Thaison - Sep 27, 2002
			else
				strSQL.append(", fixed_date = '' ");
			strSQL.append(
					", description = '"
					+ CommonUtil.stringConvert(beanDefect.getDescription())
					+ "'");
			strSQL.append(
					", solution = '"
					+ CommonUtil.stringConvert(beanDefect.getCorrectiveAction())
					+ "'");
			strSQL.append(
					", CAUSE_ANALYSIS = '"
					+ CommonUtil.stringConvert(beanDefect.getCauseAnalysis())
					+ "'");

			strSQL.append(
					", updated_by = '"
					+ beanUserInfo.getAccount()
					+ "'"
					+ updateImg);
			strSQL.append(
					", reference = '"
					+ CommonUtil.stringConvert(beanDefect.getReference())
					+ "'");
			strSQL.append(
					", project_origin = '"
					+ CommonUtil.stringConvert(beanDefect.getStrProject_Origin())
					+ "'");
			if (beanDefect.getStatus() == 4) //TESTED Defect
				strSQL.append(
						", close_date = TO_DATE('"
						+ DateUtil.getCurrentDate(0)
						+ "', '"
						+ DMS.DATE_FORMAT
						+ "')");
			strSQL.append(
					 ", DEFECT_OWNER = '"
					 + CommonUtil.stringConvert(beanDefect.getDefectOwner())
					 +	"'");
						 			
			strSQL.append(" WHERE defect_id = " + beanDefect.getDefectID());

			// do update into DataBase..
			if (DMS.DEBUG) {
				logger.debug("strSQL = " + strSQL.toString());
			}
			ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);

			prepStmt = con.prepareStatement(strSQL.toString());
			prepStmt.executeUpdate();
			con.commit();
			prepStmt.close();
		}
		catch (SQLException ex) {
			con.rollback();
			logger.error(
					"SQLException occurs in DefectBO.updateDefect(): "
					+ ex.getMessage()
					+ " strSQL = "
					+ strSQL.toString());
		}
		catch (Exception e) {
			con.rollback();
			logger.error(
					"Exception occurs in DefectBO.updateDefect(): "
					+ e.getMessage());
		}
		finally {
			conPool.releaseResourceAndCommit(con, prepStmt, null,
							"DefectBO.updateDefect(): ");
		}
		return;
	}

	/**
	 * Method batchUpdateDefect.
	 * @param beanDefectBatch
	 * @param beanUserInfo
	 * @throws SQLException
	 * @throws Exception
	 */
	public void batchUpdateDefect(DefectBatchUpdateBean beanDefectBatch, UserInfoBean beanUserInfo)
			throws SQLException, Exception {
		DataSource ds = null;
		Statement stmt = null;
		Connection con = null;
        
		try {
			StringMatrix smxDefectBatchUpdate = beanDefectBatch.getBatchUpdateList();
            ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();

			for (int i = 0; i < smxDefectBatchUpdate.getNumberOfRows(); i++) {
				StringBuffer strSQL = new StringBuffer();
				strSQL.append("UPDATE Defect SET");
				strSQL.append(" DEFS_ID = " + smxDefectBatchUpdate.getCell(i, 1));
				strSQL.append(", DP_ID = " + smxDefectBatchUpdate.getCell(i, 2));
				strSQL.append(
						", ASSIGNED_TO = '"
						+ smxDefectBatchUpdate.getCell(i, 3)
						+ "'");
                strSQL.append(
                             ", DS_ID = '"
                             + smxDefectBatchUpdate.getCell(i, 7)
                             + "'");          
				strSQL.append(
						", Due_Date = TO_DATE('"
						+ smxDefectBatchUpdate.getCell(i, 4)// old 5
						+ "','"
						+ DMS.DATE_FORMAT
						+ "')");
                      
			/*			
				if (smxDefectBatchUpdate.getCell(i, 1).equals("4")) {
				//Closed Defect
					strSQL.append(
							", Close_date=TO_DATE('"
							+ DateUtil.getCurrentDate(0)
							+ "', '"
							+ DMS.DATE_FORMAT
							+ "')");
				}
				
				if (smxDefectBatchUpdate.getCell(i, 1).equals("3")) {
				//Pending Defect
					strSQL.append(
							", fixed_date=TO_DATE('"
							+ DateUtil.getCurrentDate(0)
							+ "', '"
							+ DMS.DATE_FORMAT
							+ "')");
				}
            */
                strSQL.append(
                            ", DEFECT_OWNER = '" + smxDefectBatchUpdate.getCell(i,6)
                            + "'");   

				strSQL.append(", updated_by = '").append(beanUserInfo.getAccount()).append("'");
				strSQL.append(" WHERE Defect_ID=" + smxDefectBatchUpdate.getCell(i, 0));
				if (DMS.DEBUG) {
					logger.debug("strSQL[" + i + "] = " + strSQL.toString());
				}
				stmt.addBatch(strSQL.toString());
			}

			stmt.executeBatch();
			stmt.close();
			con.commit();
		}
		catch (SQLException ex) {
			con.rollback();
			logger.error(
					"SQLException occurs in DefectBO.batchUpdateDefect(): "
					+ ex.getMessage());
		}
		catch (Exception e) {
			con.rollback();
			logger.error(
					"Exception occurs in DefectBO.batchUpdateDefect(): "
					+ e.getMessage());
		}
		finally {
			conPool.releaseResourceAndCommit(con, stmt, null, "DefectBO.batchUpdateDefect(): ");
		} //end finally
		return;
	}

	/**
	 * Method getDefectHistory.
	 * @param nDefectID
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */
	public String getDefectHistory(int nDefectID)
			throws SQLException, Exception {
		String strHistory = "";
		Connection con = null;
		DataSource ds = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			ds = conPool.getDS();
			con = ds.getConnection();
			stmt = con.createStatement();

			String strSQL =
					"SELECT * FROM Defect_history WHERE Defect_ID = " + nDefectID;
			rs = stmt.executeQuery(strSQL);
			if (rs.next()) {
				strHistory = rs.getString("History");
				strHistory =
						CommonUtil.correctErrorChar(strHistory, '\r', "<br>");
				strHistory = strHistory.replace('\n', ' ');
			}

			rs.close();
			stmt.close();
		}
		catch (SQLException ex) {
			logger.error(
					"SQLException occurs in DefectBO.getDefectHistory(): "
					+ ex.getMessage());
		}
		catch (Exception e) {
			logger.error(
					"Exception occurs in DefectBO.getDefectHistory(): "
					+ e.getMessage());
		}
		finally {
			conPool.releaseResource(con, stmt, rs,
							"DefectBO.getDefectHistory(): ");
		} //end finally

		return strHistory;
	}

	/**
	 * Method createDataConstraint.
	 * @param strQueryID
	 * @param nProjectID
	 * @param isForExport
	 * @return String
	 */
	private String createDataConstraint(UserInfoBean beanUserInfo, String strQueryID, int nProjectID, boolean isForExport) {
		StringBuffer strResult = new StringBuffer();
		Connection con = null;
		DataSource ds = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		StringBuffer strSQL = new StringBuffer();

		try {
			strResult.append(" FROM defect, defect_severity, defect_priority, defect_status ");

			/** Added by PhuongNT, Friday, July 25, 2003 */
			if (isForExport) {
				strResult.append(", qc_activity, process, activity_type, stage stage_inj, stage stage_det");
				strResult.append(", workproduct, module, defect_type");
			}
			/* End */

			strResult.append(" WHERE defect.defs_id = defect_severity.defs_id(+)");
			strResult.append(" AND defect.dp_id = defect_priority.dp_id(+)");
			strResult.append(" AND defect.ds_id = defect_status.ds_id(+)");
			strResult.append(" AND defect.project_id = " + nProjectID);

			//HanhTN added
			//if External User
			String strPosition = beanUserInfo.getPosition();
			if (strPosition.substring(6, 7).equals("1")) {
				strResult.append(" AND upper(defect.created_by) = '" + beanUserInfo.getAccount().toUpperCase() + "'");
			}

			/** Added by PhuongNT, Friday, July 25, 2003 */
			if (isForExport) {
				strResult.append(" AND defect.qa_id = qc_activity.qa_id(+)");
				strResult.append(" AND defect.process_id = process.process_id(+)");
				strResult.append(" AND defect.at_id = activity_type.at_id(+)");
				strResult.append(" AND defect.si_id = stage_inj.stage_id(+)");
				strResult.append(" AND defect.sd_id = stage_det.stage_id(+)");
				strResult.append(" AND defect.wp_id = workproduct.wp_id(+)");
				strResult.append(" AND defect.module_id = module.module_id(+)");
				strResult.append(" AND defect.dt_id = defect_type.dt_id(+)");
			}
			/* End */

			//List defects defined by user query
			if (DMS.VIEW_ALL_DEFECTS.equals(strQueryID)
					|| DMS.VIEW_ALL_OPEN_DEFECTS.equals(strQueryID)
					|| DMS.VIEW_ALL_LEAKAGE_DEFECTS.equals(strQueryID)) {

				if (DMS.DEBUG) {
					logger.debug(
							"createDataConstraint: strQueryID = " + strQueryID);
				}

				if (DMS.VIEW_ALL_OPEN_DEFECTS.equals(strQueryID))
				//All opening defects
				{
					strResult.append(
							" AND "
							+ "defect.ds_id <> 4 AND defect.ds_id <> 5 AND defect.ds_id <> 6 ");
				}
				else if (
						DMS.VIEW_ALL_LEAKAGE_DEFECTS.equals(
								strQueryID)) //All leakage defects
				{
					strResult.append(" AND defect.qa_id IN (13, 22, 15) ");
				}
			}
			else //List all defects
			{
				if (DMS.DEBUG) {
					logger.debug(
							"createDataConstraint: strQueryID = " + strQueryID);
				}

				String strWhereSQL = "";
				strSQL.append(
						"SELECT sql_text FROM defect_query WHERE query_id = "
						+ strQueryID);

				ds = conPool.getDS();
				con = ds.getConnection();
				prepStmt = con.prepareStatement(strSQL.toString());
				rs = prepStmt.executeQuery();
				if (rs.next()) {
					strWhereSQL = rs.getString("sql_text");
				}

				rs.close();
				prepStmt.close();

				if (!"".equals(strWhereSQL))
					strResult.append(" AND (" + strWhereSQL + ")");
			} //end else
		}
		catch (SQLException ex) {
			logger.error(
					"SQLException occurs in DefectBO.createDataConstraint(): "
					+ ex.getMessage()
					+ " strSQL = "
					+ strSQL.toString());
		}
		catch (Exception e) {
			logger.error(
					"Exception occurs in DefectBO.createDataConstraint(): "
					+ e.getMessage());
		}
		finally {
			conPool.releaseResource(con, prepStmt, rs,
							"DefectBO.createDataConstraint(): ");
		}
		////////////////////////////////////////////////////////////////////////////////////////////////
		if (DMS.DEBUG) {
			logger.debug("strResult = " + strResult.toString());
		}
		return strResult.toString();
	}

	/**
	 * Method moveDefect.
	 * @param beanDefectList
	 * @return int
	 * @throws SQLException
	 * @throws Exception
	 */
	public int moveDefect(DefectListingBean beanDefectList)
			throws SQLException, Exception {
		int nResult = -1;
		Connection con = null;
		DataSource ds = null;
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();

		try {
			String[] arrDefectID = beanDefectList.getArrDeleteDefect();
			String strDelete = "";
			for (int i = 0; i < arrDefectID.length; i++) {
				strDelete += arrDefectID[i] + ",";
			}
			strDelete = strDelete.substring(0, strDelete.length() - 1);

			ds = conPool.getDS();
			con = ds.getConnection();

			strSQL.append(
					"UPDATE Defect SET project_id = "
					+ beanDefectList.getNewProjectID());
			strSQL.append(" WHERE defect_id IN (" + strDelete + ")");

			if (DMS.DEBUG) {
				logger.debug("strSQL = " + strSQL.toString());
			}
			con.setAutoCommit(false);
			prepStmt = con.prepareStatement(strSQL.toString());
			prepStmt.executeUpdate();
			prepStmt.close();
			con.commit();
		}
		catch (SQLException ex) {
			con.rollback();
			logger.error(
					"SQLException occurs in DefectBO.moveDefect(): "
					+ ex.getMessage()
					+ " strSQL = "
					+ strSQL.toString());
		}
		catch (Exception e) {
			con.rollback();
			logger.error(
					"Exception occurs in DefectBO.moveDefect(): " + e.getMessage());
		}
		finally {
			conPool.releaseResourceAndCommit(con, prepStmt, null,
							"DefectBO.createDataConstraint(): ");
		}
		////////////////////////////////////////////////////////////////////////////////////////////////

		return nResult;
	}

	/** Added by PhuongNT, Friday, July 18, 2003 */

	/**
	 * getImportedDefectList
	 * get list of defects from Excel file
	 * @param strExcelFile - MS Excel file name
	 * @return list of imported defects
	 */
    
    
	/** Modify by HuyNH2, Aug 28, 2006 */
	/*
	 * get "Cause Analysis" field
    
	private StringMatrix getImportedDefectList(String strExcelFile)
			throws Exception {            	
		StringMatrix smDefList = new StringMatrix();    
		try {
			logger.debug("trungtn.getImportedDefectList");
			File fExcelFile = new File(strExcelFile);
			Workbook wb = Workbook.getWorkbook(fExcelFile);
			Sheet sh = wb.getSheet(DMS.IMPORT_SHEET);
			for (int i = 0; i < DMS.MAX_ROW_IMPORT; i++) {
				NumberCell c = (NumberCell)sh.getCell(i, 0);
				if (c.getValue() > 0) {
					StringVector svRow = new StringVector(18);
					for (int j = 1; j < 20; j++) {                    	
						String strTmp = "";
						if (sh.getCell(i, j).getType() == CellType.STRING_FORMULA ||
							sh.getCell(i, j).getType() == CellType.NUMBER_FORMULA)
						{
							strTmp = ((LabelCell)sh.getCell(i, j)).getString();
						}
						svRow.setCell(j - 1, strTmp);
					}
					smDefList.addRow(svRow);
				}
			}
			wb.close();
			fExcelFile.delete();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return smDefList;
	}
	 */

	/**
	 * correctModuleName
	 * check existence of module name and change it by module id.
	 * @param smList - list of imported defects
	 * @param nProjectID - ident.number of project
	 * @return corrected list
	 */
	private StringMatrix correctModuleName(StringMatrix smList, int nProjectID)
			throws Exception {
		DataSource ds = null;
		Connection con = null;        
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		StringBuffer strSQL = new StringBuffer();
		try {
			ds = conPool.getDS();
			con = ds.getConnection();

			strSQL.append("SELECT name,module_id FROM module WHERE project_id=").append(nProjectID);
			prepStmt = con.prepareStatement(strSQL.toString());
			rs = prepStmt.executeQuery();
			ArrayList alModule = new ArrayList();
			while (rs.next()) {
				alModule.add(rs.getString("name").toUpperCase());
				alModule.add(rs.getString("module_id"));
			}
			for (int i = 0; i < smList.getNumberOfRows(); i++) {
				int nPosition = -1;
				if (smList.getCell(i, 13).length() > 0) {
					nPosition = alModule.indexOf(smList.getCell(i, 13).toUpperCase());
				}
				String strModuleID = "0";
				if (nPosition >= 0) {
					strModuleID = alModule.get(nPosition + 1).toString();
				}
				smList.setCell(i, 13, strModuleID);
			}

		}
		catch (SQLException se) {
			se.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conPool.releaseResource(con, prepStmt, rs,
							"DefectBO.createDataConstraint(): ");
			return smList;
		}
	}

	/**
	 * correctAssignee
	 * correct field assigned_to
	 * @param smList - list of imported defects
	 * @param nProjectID - ident.number of project
	 * @return corrected list
	 */
	private StringMatrix correctAssignee(StringMatrix smList, int nProjectID)
			throws Exception {
		DataSource ds = null;
		Connection con = null;        
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		StringBuffer strSQL = new StringBuffer();
		try {
			ds = conPool.getDS();
			con = ds.getConnection();

			strSQL.append("SELECT d.account FROM developer d,defect_permission p ");
			strSQL.append("WHERE d.developer_id=p.developer_id ");
			strSQL.append("AND p.project_id=");
			strSQL.append(nProjectID);
			prepStmt = con.prepareStatement(strSQL.toString());
			rs = prepStmt.executeQuery();
			ArrayList alAssignee = new ArrayList();
			while (rs.next()) {
				alAssignee.add(rs.getString(1).toUpperCase());
			}
			for (int i = 0; i < smList.getNumberOfRows(); i++) {
				String strAssignee = smList.getCell(i, 15);
				if (strAssignee.length() > 0) {
					int nPosition = alAssignee.indexOf(strAssignee.toUpperCase());
					if (nPosition >= 0) {
						strAssignee = strAssignee.toUpperCase();
						smList.setCell(i, 15, strAssignee);
					}
					else {
						smList.setCell(i, 15, "");
					}
				}
				
				strAssignee = smList.getCell(i,16);
				if (strAssignee.length() > 0) {
					int nPosition = alAssignee.indexOf(strAssignee.toUpperCase());
					if (nPosition >= 0) {
						strAssignee = strAssignee.toUpperCase();
						smList.setCell(i, 16, strAssignee);
                        smList.setCell(i, DMS.MAX_COL_IMPORT + 2,"2-Assigned");
                        smList.setCell(i, DMS.MAX_COL_IMPORT + 3,"2");
					}
					else {
						smList.setCell(i, 16, "");
					}
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conPool.releaseResource(con, prepStmt, rs,
							"DefectBO.createDataConstraint(): ");
			return smList;
		}
	}

	/*
	 * importFromExcel
	 * import list of defect in Excel file
	 * @param strUserID - importer's account
	 * @param strProjectID - ident.number of project
	 * @param strExcelFile - MS Excel file name
	public void importFromExcel(String strUserID,
								String strProjectID,
								String strExcelFile)
			throws SQLException, Exception {
		DataSource ds = null;
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		try {
			StringMatrix smDefList = getImportedDefectList(strExcelFile);
			if (DMS.DEBUG) {
				logger.info("Number of imported defects: " + smDefList.getNumberOfRows());
			}
			int nProjectID = Integer.parseInt(strProjectID);
			smDefList = correctModuleName(smDefList, nProjectID);
			smDefList = correctAssignee(smDefList, nProjectID);

			ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);
			strSQL.append("INSERT INTO defect(project_id,created_by,create_date,title,assigned_to,");
			strSQL.append("ds_id,wp_id,defs_id,module_id,at_id,dp_id,dt_id,qa_id,process_id,sd_id,");
			strSQL.append("si_id,due_date,description,solution,updated_by,image,reference,project_origin,defect_id,cause_analysis)");
			strSQL.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			prepStmt = con.prepareStatement(strSQL.toString());
			for (int i = 0; i < smDefList.getNumberOfRows(); i++) {
				prepStmt.setInt(1, nProjectID);
				prepStmt.setString(2, strUserID);
				prepStmt.setString(3, smDefList.getCell(i, 0));
				prepStmt.setString(4, smDefList.getCell(i, 1));
				prepStmt.setString(5, smDefList.getCell(i, 15));
				prepStmt.setString(6, "1"); // defect status is "1-Error"
				prepStmt.setString(7, smDefList.getCell(i, 11));
				prepStmt.setString(8, smDefList.getCell(i, 12));
				prepStmt.setString(9, smDefList.getCell(i, 13));
				prepStmt.setString(10, smDefList.getCell(i, 7));
				prepStmt.setString(11, smDefList.getCell(i, 10));
				prepStmt.setString(12, smDefList.getCell(i, 14));
				prepStmt.setString(13, smDefList.getCell(i, 5));
				prepStmt.setString(14, smDefList.getCell(i, 6));
				prepStmt.setString(15, smDefList.getCell(i, 9));
				prepStmt.setString(16, smDefList.getCell(i, 8));
				prepStmt.setString(17, smDefList.getCell(i, 16));
				prepStmt.setString(18, smDefList.getCell(i, 3));
				prepStmt.setString(19, smDefList.getCell(i, 17));
				prepStmt.setString(20, "");
				prepStmt.setString(21, "");
				prepStmt.setString(22, smDefList.getCell(i, 4));
				prepStmt.setString(23, smDefList.getCell(i, 2));
				prepStmt.setLong(24, conPool.getNextSeq("Defect_seq"));
				prepStmt.addBatch();
			}
			prepStmt.executeBatch();
			con.commit();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			 conPool.releaseResourceAndCommit(con, prepStmt, null,
							"DefectBO.importFromExcel(): ");
		}
	}
	 */
	/** End of added */
    
	/**
	 * upload
	 * Upload file, import defect or upload attachment
	 * @author  Tu Ngoc Trung
	 * @since December 28, 2004
	 * @param request javax.servlet.HttpServletRequest: the request object.
	 * @param response javax.servlet.HttpServletResponse: the response object.
	 * @param servlet javax.servlet.HttpServlet: the servlet object that has called.
	 * @return int Detect posted page 0: Import page, 1: Attachment, -1: Others
	 * @throws Exception
	*/
	public int upload(HttpServletRequest request,
					 HttpServletResponse response,
					 HttpServlet servlet) throws Exception, Throwable {
		int nResult = -1;
		if (!ServletFileUpload.isMultipartContent(request)) return -1;

		ArrayList files = new ArrayList();
		HashMap fields = new HashMap();
	
		//SmartUpload myUpload = null;
		//com.jspsmart.upload.Request jspRequest = null;
		try {
			//String strActionDetail = request.getParameter("hidActionDetail");
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
//			Set max attachment size (for all files) and max attachment file size for each file
			List items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
			
				if (item.isFormField()) {
					fields.put(item.getFieldName(), item.getString());
				} else {
					files.add(item);
				}
			}
		
			String strActionDetail = fields.get("hidActionDetail") == null ? "" : fields.get("hidActionDetail").toString();
			/*
			if (DMS.DEFECT_IMPORT.equals(strActionDetail)){
				//myUpload.setTotalMaxFileSize(DMS.MAX_IMPORT_SIZE);
			 	//myUpload.setMaxFileSize(DMS.MAX_IMPORT_SIZE);
				upload.setFileSizeMax(DMS.MAX_IMPORT_SIZE);
				upload.setSizeMax(DMS.MAX_IMPORT_SIZE);
			}
			else {
				//myUpload.setTotalMaxFileSize(DMS.MAX_ATTACH_SIZE);
				//myUpload.setMaxFileSize(DMS.MAX_ATTACH_FILE_SIZE);
				upload.setFileSizeMax(DMS.MAX_ATTACH_FILE_SIZE);
				upload.setSizeMax(DMS.MAX_ATTACH_SIZE);
			}
			
//			List items = upload.parseRequest(request);
//			Iterator iter = items.iterator();
//			while (iter.hasNext()) {
//				FileItem item = (FileItem) iter.next();
//		
//				if (item.isFormField()) {
//					fields.put(item.getFieldName(), item.getString());
//				} else {
//					files.add(item);
//				}
//			}
			// Check post form data length first
			/*myUpload = new SmartUpload();
			myUpload.initialize(servlet.getServletConfig(), request, response);
			myUpload.upload();
			jspRequest = myUpload.getRequest();
			String strActionDetail = jspRequest.getParameter("hidActionDetail");*/
			
			if (DMS.DEFECT_IMPORT_SUBMIT.equals(strActionDetail)){
				HttpSession session = request.getSession();
				//String strProjectID = jspRequest.getParameter("cboProjectList");
				//String strCreatedBy = jspRequest.getParameter("cboCreatedBy");
				String strProjectID = fields.get("cboProjectList") == null ? "" : fields.get("cboProjectList").toString();// request.getParameter("cboProjectList");
				String strCreatedBy = fields.get("cboCreatedBy") == null ? "" : fields.get("cboCreatedBy").toString();// request.getParameter("cboCreatedBy");
				StringMatrix smImport = (StringMatrix)session.getAttribute("ImportDefectList");
				//count number of defect was imported successfully!
				int numberDefectImport = 0;
				if (smImport != null ){
					if (smImport.getNumberOfRows() > 0){
						numberDefectImport = importDefectList(strProjectID, strCreatedBy, smImport);
					}
					if (numberDefectImport < smImport.getNumberOfRows()){
						int numberDefectError = smImport.getNumberOfRows() - numberDefectImport;
						request.setAttribute("uploadMessage", "<FONT color=red class=\"label1\">Successed: "+ String.valueOf(numberDefectImport)+ " defects<br> Errors&nbsp&nbsp&nbsp&nbsp&nbsp: "+ String.valueOf(numberDefectError) + " defects!</FONT>");
					}
					else {
						session.removeAttribute("ImportDefectList");
						request.setAttribute("uploadMessage", "<FONT color=blue class=\"label1\">Import successfully with "+ String.valueOf(numberDefectImport) + " defects! </FONT>");
					}
				}
				nResult = 3;
				return nResult;
			}
			
			if (DMS.DEFECT_IMPORT.equals(strActionDetail)){
				if  (request.getContentLength() > DMS.MAX_IMPORT_SIZE){
					throw new Exception("Import file size exceeded");
				}
			}
			
			else if (request.getContentLength() > DMS.MAX_ATTACH_SIZE) {
				throw new Exception("Attach size exceeded");
			}
			//HttpSession session = request.getSession();
			
			if (DMS.DEBUG) {
				logger.debug("DefectBO.upload(): strActionDetail=" + strActionDetail);
			}
			
			if (DMS.DEFECT_IMPORT.equals(strActionDetail)) {
				//importDefect(myUpload, request, servlet);
				importDefect(files, fields, request, servlet);
				nResult = 0;
			}
			// Attach file from add new
			else if (DMS.DEFECT_ATTACH_NEW.equals(strActionDetail)) {
				UploadBO boAttach = new UploadBO();
				//boAttach.attachUpdate(myUpload, request, servlet);
				boAttach.attachUpdate(files, request, servlet);
//				  DefectAttachBean beanDefectAttach =
//						  boAttach.attachUpdate(myUpload, request, servlet);
//				  session.setAttribute("beanDefectAttach", beanDefectAttach);
				nResult = 1;
			}
			// Attach file from update
			else if (DMS.DEFECT_ATTACH_UPDATE.equals(strActionDetail)) {
				UploadBO boAttach = new UploadBO();
				//boAttach.attachUpdate(myUpload, request, servlet);
				boAttach.attachUpdate(files, request, servlet);
//				  DefectAttachBean beanDefectAttach =
//						  boAttach.attachUpdate(myUpload, request, servlet);
//				  session.setAttribute("beanDefectAttach", beanDefectAttach);
				nResult = 2;
			}
		}
		catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			nResult = redirectUploadFailed(request, e.getMessage());
		}
		finally{
			return nResult;
		}
	}
	/**
	 * Redirect to appropriate page when upload files failed
	 * @param request
	 * @return
	 */
	private int redirectUploadFailed(HttpServletRequest request, String strMsg) {
		int nResult = -1;
		HttpSession session = request.getSession();
		DefectAttachBean beanDefectAttach =
				(DefectAttachBean) session.getAttribute("beanDefectAttach");
		session.setAttribute(DMS.MSG_ATTACH_MESSAGE, DMS.MSG_ATTACH_FAILED +
							":" + strMsg);
		if (beanDefectAttach != null) {
			if (beanDefectAttach.getAttachMode() == DefectAttachBean.MODE_ADD_NEW) {
				nResult = 1;
			}
			else if (beanDefectAttach.getAttachMode() == DefectAttachBean.MODE_UPDATE) {
				nResult = 2;
			}
			else {
				nResult = 0;
			}
		}
		return nResult;
	}
    
	/**
	 * importDefect
	 * Import Defect by user
	 * @author  Tu Ngoc Trung
	 * @since December 11, 2003
	 * @param request javax.servlet.HttpServletRequest: the request object.
	 * @param response javax.servlet.HttpServletResponse: the response object.
	 * @param servlet javax.servlet.HttpServlet: the servlet object that has called.
	 * @throws Exception
	 * @throws Throwable
	 */
	private void importDefect(/*SmartUpload myUpload,*/
							List files, Map fields,
							HttpServletRequest request,
							HttpServlet servlet) throws Exception, Throwable {
		HttpSession session = request.getSession();
		UserInfoBean beanUserInfo =
			(UserInfoBean) session.getAttribute("beanUserInfo");
		DefectListingBean beanDefectList = new DefectListingBean();
		//Set initialize DefectListingBean
		request.setAttribute("beanDefectList", beanDefectList);

		request.removeAttribute("uploadMessage");
		try {
			//com.jspsmart.upload.Request jspRequest = myUpload.getRequest();
			//String strProjectID = jspRequest.getParameter("cboProjectList");
			//String strCreatedBy = jspRequest.getParameter("cboCreatedBy");
			String strProjectID = fields.get("cboProjectList") == null ? "" : fields.get("cboProjectList").toString();
			String strCreatedBy = fields.get("cboCreatedBy") == null ? "":fields.get("cboCreatedBy").toString();
			// Import by self
			strCreatedBy = beanUserInfo.getAccount() == null ? "" : beanUserInfo.getAccount();
//			if (beanUserInfo.isDeveloper() && !beanUserInfo.isTester() && !beanUserInfo.isProjectLeader()) {
//				strCreatedBy = beanUserInfo.getAccount();
//			}
//			else if (beanUserInfo.isExternalUser()) {
//				strCreatedBy = ""+beanUserInfo.getAccount();
//			}
			strCreatedBy = strCreatedBy.toUpperCase();
			session.setAttribute("diCreatedBy", strCreatedBy);

			//Clock milli seconds
			String strMillis = String.valueOf(System.currentTimeMillis());
			String strExt = ".xls";
			String strFile_name = strMillis + strExt;

			String strRealPath = servlet.getServletContext().getRealPath(DMS.DIRECTORY_UPLOAD);
			String strExcelFile = strRealPath + File.separator + strFile_name;

			//com.jspsmart.upload.File myFile = myUpload.getFiles().getFile(0);
			FileItem myFile = files.size() > 0 ? (FileItem)files.get(0) : null;
			
			if (myFile != null) {
				if (DMS.DEBUG) {
					logger.debug("strExcelFile=" + strExcelFile);
				}
				java.io.File mFile = new java.io.File(strRealPath);
				if (!mFile.exists()) {
					mFile.mkdir();
				}
				//myFile.saveAs(strExcelFile, SmartUpload.SAVE_PHYSICAL);
				myFile.write(new File(strExcelFile));
				StringMatrix smImport = getUploadedDefect(strExcelFile, request);
				int nImported = nImported = smImport.getNumberOfRows();

				if (smImport != null && nImported > 0){
					session.setAttribute("ImportDefectList", smImport);
				}
				showUploadResult(request, strProjectID, strCreatedBy, smImport, nImported);
			}
		}
		catch (Exception e) {
			request.setAttribute("uploadMessage", "<FONT color=red class=\"label1\">Cannot import defect list</FONT>");
			e.printStackTrace();
		}
	}
	private boolean doSubmitInportDefect(String strProjectID,
					 String strCreatedBy,
					 StringMatrix smImported)
			throws NumberFormatException, SQLException, Exception, Throwable{
		boolean result = false;
		try{
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally{
			return result;
		}
	}
	/**
	 * getUploadedTimesheet
	 * Get list of Timesheet from Excel file
	 * @param strExcelFile - MS Excel file name
	 * @return StringMatrix list of imported Timesheet
	 * @throws Exception
	 * @throws Throwable
	 */
	private StringMatrix getUploadedDefect(String strExcelFile, HttpServletRequest request)
			throws Exception, Throwable {

		StringMatrix smTSList = new StringMatrix();
		java.io.File fExcelFile = null;

		try {
			fExcelFile = new java.io.File(strExcelFile);
			Workbook wb = Workbook.getWorkbook(fExcelFile);
			//Get import sheet from excel file. You must have sheet name in your file.
			Sheet sh = wb.getSheet(DMS.IMPORT_SHEET);
			Sheet shDefect = wb.getSheet(DMS.INPUT_SHEET);
			if ((sh != null) && (shDefect != null)) {
				//Import data plus additional informations
				StringVector svRow = new StringVector(DMS.MAX_COL_IMPORT + 4);
				for (int i = 0; i < DMS.MAX_ROW_IMPORT; i++) {
					NumberCell c;
					try{
						Cell temp = sh.getCell(0, i);
						if (temp != null && !"".equalsIgnoreCase(temp.getContents())){
							c = (NumberCell) temp; 
						}
						else{
							break;
						}
					}
					catch (Exception ex){
						ex.printStackTrace();
						String strTmp = "N/A";
						for (int j = 1; j < DMS.MAX_COL_IMPORT ; j++){
							svRow.setCell(j - 1, strTmp);
						}
						svRow.setCell(DMS.MAX_COL_IMPORT, strTmp); //severity
						svRow.setCell(DMS.MAX_COL_IMPORT + 1, strTmp);  //Priority
						svRow.setCell(DMS.MAX_COL_IMPORT + 2, "1-Error");   //Default status
						svRow.setCell(DMS.MAX_COL_IMPORT + 3, "1");
						smTSList.addRow(svRow);
						continue;
					}
					if (c.getValue() > 0) {
						for (int j = 1; j < DMS.MAX_COL_IMPORT ; j++) {
							String strTmp = "";
							Cell cell = sh.getCell(j, i);
							if (CellType.STRING_FORMULA.equals(cell.getType())) {
								strTmp = cell.getContents();
							}
							else if (CellType.NUMBER_FORMULA.equals(cell.getType())) {
								if (!Double.isNaN(
									((NumberCell) cell).getValue()))
								{
									strTmp = cell.getContents();
								}
							}
							svRow.setCell(j - 1, strTmp);
						}
						svRow.setCell(DMS.MAX_COL_IMPORT, shDefect.getCell(13, DMS.INPUT_START_ROW + i).getContents()); //severity
						svRow.setCell(DMS.MAX_COL_IMPORT + 1, shDefect.getCell(11, DMS.INPUT_START_ROW + i).getContents());  //Priority
						svRow.setCell(DMS.MAX_COL_IMPORT + 2, "1-Error");   //Default status
                        svRow.setCell(DMS.MAX_COL_IMPORT + 3, "1");
						smTSList.addRow(svRow);
					}
				}
			}
			else {
				request.setAttribute("uploadMessage",
									 "<FONT color=red class=\"label1\">You must use defect template of Fsoft</FONT>");
			}
			wb.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (fExcelFile != null) {
				fExcelFile.delete();
			}
			return smTSList;
		}
	}

	/**
	 * Method importDefectList.
	 * Import Timesheet from Imported List
	 * @param strProjectID
	 * @param strCreatedBy
	 * @param smImported Imported List
	 * @return int Number of Defects imported
	 * @throws NumberFormatException
	 * @throws SQLException
	 * @throws Exception
	 * @throws Throwable
	 */
	private int importDefectList(String strProjectID,
								 String strCreatedBy,
								 StringMatrix smImported)
			throws NumberFormatException, SQLException, Exception, Throwable {
		int nImportCount = 0;
		DataSource ds = null;
		Connection con = null;
		PreparedStatement prepStmt = null;
		StringBuffer strSQL = new StringBuffer();
		try {
			int nProjectID = Integer.parseInt(strProjectID);
			smImported = correctModuleName(smImported, nProjectID);
			smImported = correctAssignee(smImported, nProjectID);
			ds = conPool.getDS();
			con = ds.getConnection();
			con.setAutoCommit(false);
			strSQL.append("INSERT INTO defect(project_id,created_by,create_date,title,ASSIGNED_TO,");
			strSQL.append("ds_id,wp_id,defs_id,module_id,at_id,dp_id,dt_id,qa_id,process_id,sd_id,");
			strSQL.append("si_id,due_date,description,solution,updated_by,image,reference,project_origin,defect_id,CAUSE_ANALYSIS, DEFECT_OWNER)");
			strSQL.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			prepStmt = con.prepareStatement(strSQL.toString());
            
			String strCreatedDate, strTitle, strProjectOrigin, strDescription,
				   strReference, strQCActivity, strDefectOrigin,
				   strTypeOfActivity, strStageInjected, strStageDetected,
				   strPriority, strWorkProduct, strSeverity, strModule,
				   strType, strAssignTo, strDueDate, strCorrectiveAction,strCauseAnalysis,strDefectOwner,strDefectStatus;
            
			for (int i = 0; i < smImported.getNumberOfRows(); i++) {
				try {
					strCreatedDate = smImported.getCell(i, 0);
					if (strCreatedDate.length() <= 0 ){
						continue;
					}
					strTitle = smImported.getCell(i, 1).trim();
					if ("N/A".equals(strTitle)){
						continue; 
					}
					if (strTitle != null && strTitle.length() > 150){
						continue;
					}
					strProjectOrigin = smImported.getCell(i, 2).trim();
					strDescription = smImported.getCell(i, 3).trim();
					if (strDescription != null && strDescription.length() > 1200){
						continue;
					}
					strReference = smImported.getCell(i, 4).trim();
					strQCActivity = smImported.getCell(i, 5).trim();
					strDefectOrigin = smImported.getCell(i, 6).trim();
					strTypeOfActivity = smImported.getCell(i, 7).trim();
					strStageInjected = smImported.getCell(i, 8).trim();
					strStageDetected = smImported.getCell(i, 9).trim();
					strPriority = smImported.getCell(i, 10).trim();
					strWorkProduct = smImported.getCell(i, 11).trim();
					strSeverity = smImported.getCell(i, 12).trim();
					strModule = smImported.getCell(i, 13).trim();
					strType = smImported.getCell(i, 14).trim(); // increment in here
					strDefectOwner = smImported.getCell(i, 15).trim();
					strAssignTo = smImported.getCell(i, 16).trim();
					strDueDate = smImported.getCell(i, 17).trim();
					strCorrectiveAction = smImported.getCell(i, 18).trim();
					strCauseAnalysis = smImported.getCell(i, 19).trim();
					strDefectStatus = smImported.getCell(i, DMS.MAX_COL_IMPORT + 3);
					// Try to parse numbers
					Integer.parseInt(strQCActivity);
					Integer.parseInt(strDefectOrigin);
					Integer.parseInt(strTypeOfActivity);
					Integer.parseInt(strWorkProduct);
					Integer.parseInt(strSeverity);
					Integer.parseInt(strType);
					if (strStageInjected.length() > 0) {
						Integer.parseInt(strStageInjected);
					}
					if (strStageDetected.length() > 0) {
						Integer.parseInt(strStageDetected);
					}
					if (strPriority.length() > 0) {
						Integer.parseInt(strPriority);
					}
                
					prepStmt.setInt(1, nProjectID);
					prepStmt.setString(2, strCreatedBy);
					prepStmt.setString(3, strCreatedDate);
					prepStmt.setString(4, strTitle);
					prepStmt.setString(5, strAssignTo);
					prepStmt.setString(6, strDefectStatus); // defect status is "1-Error"
					prepStmt.setString(7, strWorkProduct);
					prepStmt.setString(8, strSeverity);
					prepStmt.setString(9, strModule);
					prepStmt.setString(10, strTypeOfActivity);
					prepStmt.setString(11, strPriority);
					prepStmt.setString(12, strType);
					prepStmt.setString(13, strQCActivity);
					prepStmt.setString(14, strDefectOrigin);
					prepStmt.setString(15, strStageDetected);
					prepStmt.setString(16, strStageInjected);
					prepStmt.setString(17, strDueDate);
					prepStmt.setString(18, strDescription);
					prepStmt.setString(19, strCorrectiveAction);
					prepStmt.setString(20, "");
					prepStmt.setString(21, "");
					prepStmt.setString(22, strReference);
					prepStmt.setString(23, strProjectOrigin);
					prepStmt.setLong(24, conPool.getNextSeq("Defect_seq"));
					prepStmt.setString(25, strCauseAnalysis);
					prepStmt.setString(26, strDefectOwner);
					prepStmt.executeUpdate();
					nImportCount++;
		   		}
				catch (NumberFormatException e) {   //Test number failed
					e.printStackTrace();
					continue;
				}
				catch (SQLException e) {
					e.printStackTrace();
					continue;
				}
		   		catch (Exception ex){
		   			ex.printStackTrace();
		   			continue;
			   	}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conPool.releaseResourceAndCommit(con, prepStmt, null,
							"DefectBO.importDefectList(): ");
			
			return nImportCount;
		}
	}

	/**
	 * showUploadResult
	 * Show imported timesheet
	 * @author Tu Ngoc Trung
	 * @since November 18, 2003
	 * @param request javax.servlet.HttpServletRequest: the request object.
	 * @param strCreatedBy String:   User info bean for import.
	 * @param smImport StringMatrix:   Imported timesheet.
	 * @param nImported: Number of imported timesheet.
	 * @throws Exception
	 * @throws Throwable
	 */
	private void showUploadResult(HttpServletRequest request,
								  String strProjectID,
								  String strCreatedBy,
								  StringMatrix smImport,
								  int nImported) throws Exception, Throwable {
		DefectListingBean beanDefectList = new DefectListingBean();
		
		if ((nImported > 0)) {
			StringMatrix smResult = new StringMatrix();
	
    		for (int iImport = 0; iImport < smImport.getNumberOfRows(); iImport++) {
				StringVector vecRecord = new StringVector(10);
				String strTitle = smImport.getCell(iImport, 1);
				String strAssignTo = smImport.getCell(iImport, 16);//
				String strDueDate = smImport.getCell(iImport, 17);
				String strDefectOwner = smImport.getCell(iImport,15);
				vecRecord.setCell(0, Integer.toString(iImport + 1));  //ID
				vecRecord.setCell(1, strTitle);             //Title
                vecRecord.setCell(2, smImport.getCell(iImport, DMS.MAX_COL_IMPORT));
                vecRecord.setCell(3, smImport.getCell(iImport, DMS.MAX_COL_IMPORT + 1));
				vecRecord.setCell(4, smImport.getCell(iImport,
													  DMS.MAX_COL_IMPORT +2));    //Status
				vecRecord.setCell(5, strAssignTo);      //Assign To
				vecRecord.setCell(6, strDueDate);       //Due Date
				vecRecord.setCell(7, strCreatedBy);     //Created By
				vecRecord.setCell(8, "");           //Fixed Date
				vecRecord.setCell(9, strDefectOwner);//Defect Owner
				smResult.addRow(vecRecord);
			}
			beanDefectList.setDefectList(smResult);
			beanDefectList.setTotalpage("1");
			beanDefectList.setTotalRecord(Integer.toString(nImported));
			beanDefectList.setNumpage("0");
		}
		else {
			request.setAttribute("uploadMessage",
								 "<FONT color=red class=\"label1\">There are not defects</FONT>");
		}
		request.setAttribute("beanDefectList", beanDefectList);
	}
}