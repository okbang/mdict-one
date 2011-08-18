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
 
 package fpt.timesheet.InputTran.ejb;

import java.util.Vector;

/**
 * Local interface for Enterprise Bean: InputEJB
 */
public interface InputEJBLocal extends javax.ejb.EJBLocalObject {
	/**
	 * GetFirstLastDate
	 */
	public java.lang.String GetFirstLastDate(int nDevID, int nFlag)
		throws java.sql.SQLException;
	/**
	 * IsRejectedWork
	 */
	public boolean IsRejectedWork(int nDevID) throws java.sql.SQLException;
	/**
	 * changePassword
	 */
	public int changePassword(
		int intID,
		java.lang.String strOldPassword,
		java.lang.String strNewPassword)
		throws java.sql.SQLException;
	/**
	 * addTimeSheetLine
	 */
	public void addTimeSheetLine(
		int intDevID,
		int intProject,
		int intProcess,
		int intTypeofWork,
		int intWorkProduct,
		float fltDuration,
		java.lang.String strDate,
		java.lang.String strDescription)
		throws java.sql.SQLException;
	
	// Added by HaiMM - 08/Aug/08	
	public void addTimeSheetDummyLine(int intTimeSheetID, String status)
		throws java.sql.SQLException;
	
	public void updateTimeSheetDummyLine(int intTimeSheetID)
		throws java.sql.SQLException;
	
	public void updateTimeSheetMigrateLine(int intTimeSheetID, String status) throws java.sql.SQLException;
	
	public java.util.Collection getTimesheetMigrateList()
		throws java.sql.SQLException;	
	
	// End
	/**
	 * updateTimeSheetLine
	 */
	public void updateTimeSheetLine(
		int intTimeSheetID,
		int intProject,
		int intProcess,
		int intTypeofWork,
		int intWorkProduct,
		float fltDuration,
		java.lang.String strDate,
		java.lang.String strDescription)
		throws java.sql.SQLException;
	/**
	 * getRejectedTimesheets
	 */
	public int getRejectedTimesheets(
		int nDevID,
		int nProjectID,
		java.lang.String strDateFrom,
		java.lang.String strDateTo)
		throws java.sql.SQLException;
	/**
	 * getTimeSheetList
	 */
	public java.util.Collection getTimeSheetList(
		int nDevID,
		int nProjectID,
		int nStatus,
		java.lang.String strDateFrom,
		java.lang.String strDateTo,
		int nSort,
		int nPage)
		throws java.sql.SQLException;
		
	/**
	 * Added by HaiMM
	 * getTimeSheetDummyList
	 */
	public java.util.Collection getTimeSheetDummyList(int nDevID)
		throws java.sql.SQLException;
	/**
	 * getTotalPage
	 */
	public int getTotalPage();
	/**
	 * getTotalTimesheet
	 */
	public int getTotalTimesheet();
	/**
	 * getTimeSheetList
	 */
	public java.util.Collection getTimeSheetList(
		java.lang.String sIDTimeSheetList)
		throws java.sql.SQLException;
		
	/**
	 * deleteTimeSheet
	 */
	public void deleteTimeSheet(java.lang.String strTimeSheetIDList)
		throws java.sql.SQLException;
	/**
		 * getTimeSheet
		 */
	public int getTimeSheetsById(java.lang.String strTimeSheetIDList)
		throws java.sql.SQLException;
	/**
	 * getLackedTimesheet
	 */
	public java.util.Collection getLackedTimesheet(
		java.lang.String strGroup,
		java.lang.String strFromDate,
		java.lang.String strToDate)
		throws java.sql.SQLException;
	/**
	 * getPendingAccount
	 */
	public java.util.Collection getPendingAccount(
		java.util.Collection lstProjects,
		java.lang.String strFromDate,
		java.lang.String strToDate)
		throws java.sql.SQLException;
	/**
	 * getSummaryEfforts
	 */
	public java.util.Collection getSummaryEfforts(
		int nProjectID,
		java.lang.String arrProjectIDs,
		int nStatus,
		java.lang.String strFrom,
		java.lang.String strTo,
		int nReportType,
		int type,
		java.lang.String strDeveloperID)
		throws java.sql.SQLException;
	/**
	 * getProcessList
	 */
	public java.util.ArrayList getProcessList() throws java.sql.SQLException;
	/**
	 * getWorkProductList
	 */
	public java.util.ArrayList getWorkProductList()
		throws java.sql.SQLException;
	/**
	 * getMappingList
	 */
	public java.util.ArrayList getMappingList(java.lang.String strProcessID)
		throws java.sql.SQLException;
	/**
	 * checkValidateWorkProduct
	 */
	public int checkValidateWorkProduct(
		java.lang.String strProcessID,
		java.util.ArrayList alCurrentWorkProductIDList)
		throws java.sql.SQLException;
	/**
	 * deleteWorkProduct
	 */
	public void deleteWorkProduct(
		java.lang.String strProcessID,
		java.util.ArrayList alCurrentWorkProductIDList)
		throws java.sql.SQLException;
	/**
	 * addWorkProduct
	 */
	public void addWorkProduct(
		java.lang.String strProcessID,
		java.util.ArrayList alCurrentWorkProductIDList)
		throws java.sql.SQLException;
	/**
	 * getAllMapping
	 */
	public java.util.ArrayList getAllMapping() throws java.sql.SQLException;
	/**
	 * checkUserAccount
	 */
	public java.util.ArrayList checkUserAccount(java.lang.String userAccount)
		throws java.sql.SQLException;
}
