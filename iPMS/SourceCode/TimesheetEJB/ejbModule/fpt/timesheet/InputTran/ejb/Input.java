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
// Input.java
//
// Created by  : ThanhSKID
// Created date  : July 27, 2001
//***************************************************************************

package fpt.timesheet.InputTran.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import javax.ejb.EJBObject;

public interface Input extends EJBObject {

    /**
     * Method GetFirstLastDate.
     * @param nDevID
     * @param nFlag
     * @return String
     * @throws RemoteException
     * @throws SQLException
     */
    public String GetFirstLastDate(int nDevID, int nFlag) throws RemoteException, SQLException;

    /**
     * Method IsRejectedWork.
     * @param nDevID
     * @return boolean
     * @throws RemoteException
     * @throws SQLException
     */
    public boolean IsRejectedWork(int nDevID) throws RemoteException, SQLException;

    /**
     * Method changePassword.
     * @param intID
     * @param strOldPassword
     * @param strNewPassword
     * @return int
     * @throws RemoteException
     * @throws SQLException
     */
    public int changePassword(int intID, String strOldPassword, String strNewPassword) throws RemoteException, SQLException;

    /**
     * Method addTimeSheetLine.
     * @param nDevID
     * @param sDate
     * @param nProject
     * @param nProcess
     * @param nTypeofWork
     * @param nWorkProduct
     * @param fDuration
     * @param sDescription
     * @throws RemoteException
     * @throws SQLException
     */
    public void addTimeSheetLine(int intDevID, int intProject, int intProcess, int intTypeofWork, int intWorkProduct, float fltDuration, String strDate, String strDescription) throws RemoteException, SQLException;
    
	/** Added by HaiMM - 08/Aug/08 *******************************************************************/
	
	public void addTimeSheetDummyLine(int intTimeSheetID, String status) throws RemoteException, SQLException;
	
	public void updateTimeSheetDummyLine(int intTimeSheetID) throws RemoteException, SQLException;
	
	public Collection getTimeSheetDummyList(int nDevID) throws RemoteException, SQLException;
	
	public void updateTimeSheetMigrateLine(int intTimeSheetID, String status) throws RemoteException, SQLException;
	
	public Collection getTimesheetMigrateList() throws RemoteException, SQLException;
	/*************************************** END *****************************************************/

    /**
     * Method updateTimeSheetLine.
     * @param nTimeSheetID
     * @param sDate
     * @param nProject
     * @param nProcess
     * @param nTypeofWork
     * @param nWorkProduct
     * @param fDuration
     * @param sDescription
     * @throws RemoteException
     * @throws SQLException
     */
    public void updateTimeSheetLine(int intTimeSheetID, int intProject, int intProcess, int intTypeofWork, int intWorkProduct, float fltDuration, String strDate, String strDescription) throws RemoteException, SQLException;

    /**
     * Method getRejectedTimesheets.
     * @param nDevID
     * @param nProjectID
     * @param strDateFrom
     * @param strDateTo
     * @return int
     * @throws RemoteException
     * @throws SQLException
     */
    public int getRejectedTimesheets(int nDevID, int nProjectID, String strDateFrom, String strDateTo) throws RemoteException, SQLException;

    /**
     * Method getTimeSheetList.
     * @param nDevID
     * @param nProjectID
     * @param nStatus
     * @param strDateFrom
     * @param strDateTo
     * @param nSort
     * @param nPage
     * @return Collection
     * @throws RemoteException
     * @throws SQLException
     */
    public Collection getTimeSheetList(int nDevID, int nProjectID, int nStatus, String strDateFrom, String strDateTo, int nSort, int nPage) throws RemoteException, SQLException;
    
	
    /**
     * Method getTotalPage.
     * @return int
     * @throws RemoteException
     */
    public int getTotalPage() throws RemoteException;

    /**
     * Method getTotalTimesheet.
     * @return int
     * @throws RemoteException
     */
    public int getTotalTimesheet() throws RemoteException;

    /**
     * Method getTimeSheetList.
     * @param sIDTimeSheetList
     * @return Collection
     * @throws RemoteException
     * @throws SQLException
     */
    public Collection getTimeSheetList(String sIDTimeSheetList) throws RemoteException, SQLException;

    /**
     * Method deleteTimeSheet.
     * @param strTimeSheetIDList
     * @throws RemoteException
     * @throws SQLException
     */
	public int getTimeSheetsById(java.lang.String strTimeSheetIDList)
			throws RemoteException, SQLException;
    public void deleteTimeSheet(String strTimeSheetIDList) throws RemoteException, SQLException;

	/**
	 * Method getLackedTimesheet.
	 * @param strGroup
	 * @param strFromDate
	 * @param strToDate
	 * @param strLogDateTime
	 * @return
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection getLackedTimesheet(String strGroup, 
										 String strFromDate, String strToDate) throws RemoteException, SQLException;
  
    /**
     * Method getPendingAccount.
     * @param lstProjects
     * @param strFromDate
     * @param strToDate
     * @return Collection
     * @throws RemoteException
     * @throws SQLException
     */
    public Collection getPendingAccount(Collection lstProjects, String strFromDate, String strToDate) throws RemoteException, SQLException;

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
     * @throws RemoteException
     * @throws SQLException
     */
    public Collection getSummaryEfforts(int nProjectID, String arrProjectIDs, int nStatus, String strFrom, String strTo, int nReportType, int type, String strDeveloperID) throws RemoteException, SQLException;

    /**
     * Method getProcessList.
     * @return ArrayList
     * @throws RemoteException
     * @throws SQLException
     */
    public ArrayList getProcessList() throws RemoteException, SQLException;

    /**
     * Method getWorkProductList.
     * @return ArrayList
     * @throws RemoteException
     * @throws SQLException
     */
    public ArrayList getWorkProductList() throws RemoteException, SQLException;

    /**
     * Method getMappingList.
     * @param strProcessID
     * @return ArrayList
     * @throws RemoteException
     * @throws SQLException
     */
    public ArrayList getMappingList(String strProcessID) throws RemoteException, SQLException;

    /**
     * Method checkValidateWorkProduct.
     * @param strProcessID
     * @param alCurrentWorkProductIDList
     * @return int
     * @throws RemoteException
     * @throws SQLException
     */
    public int checkValidateWorkProduct(String strProcessID, ArrayList alCurrentWorkProductIDList) throws RemoteException, SQLException;

    /**
     * Method deleteWorkProduct.
     * @param strProcessID
     * @param alCurrentWorkProductIDList
     * @throws RemoteException
     * @throws SQLException
     */
    public void deleteWorkProduct(String strProcessID, ArrayList alCurrentWorkProductIDList) throws RemoteException, SQLException;

    /**
     * Method addWorkProduct.
     * @param strProcessID
     * @param alCurrentWorkProductIDList
     * @throws RemoteException
     * @throws SQLException
     */
    public void addWorkProduct(String strProcessID, ArrayList alCurrentWorkProductIDList) throws RemoteException, SQLException;

    /**
     * Method getAllMapping.
     * @return ArrayList
     * @throws RemoteException
     * @throws SQLException
     */
    public ArrayList getAllMapping() throws RemoteException, SQLException;

    /**
     * Method checkUserAccount.
     * @param userAccount
     * @return ArrayList
     * @throws RemoteException
     * @throws SQLException
     * @author Tu Ngoc Trung
     */
    public ArrayList checkUserAccount(String userAccount) throws RemoteException, SQLException;
}