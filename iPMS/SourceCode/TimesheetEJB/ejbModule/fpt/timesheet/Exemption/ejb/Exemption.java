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

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import fpt.timesheet.vo.*;

/**
 * Remote interface for Enterprise Bean: ExemptionEJB
 */
public interface Exemption extends javax.ejb.EJBObject {

	/**
	 * Method getCurrentPage
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public int getCurrentPage() throws RemoteException;

	/**
	 * Method getTotalPage
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public int getTotalPage() throws RemoteException;

	/**
	 * Method getTotalExemption
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public int getTotalExemption() throws RemoteException;

	/**
	 * Method hasPermanentExemption
	 * @param intExemptionId
	 * @param intDevId
	 * @param intType
	 * @param strSearchFromDate
	 * @param strSearchToDate
	 * @return Collection
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection hasExistedExemption(int intExemptionId, int intDevId, int intType, 
										  String strSearchFromDate, String strSearchToDate) throws RemoteException, SQLException;

	/**
	 * Method hasExistedAccount
	 * @param strAccount
	 * @param strName
	 * @return Collection
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection hasExistedAccount(String strAccount, String strName) throws RemoteException, SQLException;

	/**
	 * Method getDeveloperList
	 * @return Collection
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection getDeveloperListEJB() throws RemoteException, SQLException;

	/**
	 * Method getExemptionListEJB
	 * @param strGroupName
	 * @param strAccount
	 * @param strName
	 * @param intType
	 * @param strSearchFromDate
	 * @param strSearchToDate
	 * @param intCurPage
	 * @return Collection
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection getExemptionListEJB(String strGroupName, String strAccount, String strName, int intType, 
										  String strSearchFromDate, String strSearchToDate, int intCurPage) throws RemoteException, SQLException;
	/**
	 * Method getExemptionByIdEJB
	 * @param intExemptionId
	 * @return ExemptionBean
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public ExemptionBean getExemptionByIdEJB(int intExemptionId) throws RemoteException, SQLException;
	// Added by HaiMM
	public ExemptionBean getDummyExemptionByIdEJB(int intExemptionId) throws RemoteException, SQLException;
	
	public Collection getDummyMigrationByIdEJB() throws RemoteException, SQLException;
	
	public void addDummyExemptionEJB(int intDevId, String status) throws RemoteException, SQLException;
	
	public void updateDummyExemptionEJB(int intDevId) throws RemoteException, SQLException;
	
	public void updateDummyMigrationEJB(int intExemptionId, String status) throws RemoteException, SQLException;
	
	/**
	 * Method addExemptionEJB
	 * @param intDevId
	 * @param intType
	 * @param intWeekDay
	 * @param strFromDate
	 * @param strToDate
	 * @param strReason
	 * @param strNote
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public void addExemptionEJB(int intDevId, int intType, String strWeekDay, 
								String strFromDate, String strToDate, 
								String strReason, String strNote) throws RemoteException, SQLException;


	/**
	 * Method updateExemptionEJB
	 * @param intExemptionId
	 * @param intDevId
	 * @param intType
	 * @param intWeekDay
	 * @param strFromDate
	 * @param strToDate
	 * @param strReason
	 * @param strNote
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public void updateExemptionEJB(int intExemptionId, int intType, String strWeekDay, 
								   String strFromDate, String strToDate, String strReason, String strNote) throws RemoteException, SQLException;

	/**
	 * Method deleteExemptionEJB
	 * @param intExemptionId
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public void deleteExemptionEJB(String strExemptionId) throws RemoteException, SQLException;
	public int getExemptionNewId(String strExemptionId) throws RemoteException, SQLException;

	/**
	 * Method getTimesheetExemptionEJB
	 * @param strGroup
	 * @param strSearchFromDate
	 * @param strSearchToDate
	 * @return Collection
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Vector getTimesheetExemptionEJB(String strGroup, String strSearchFromDate, String strSearchToDate) throws RemoteException, SQLException;
	
	
	/**
	 * Method getTrackingReportEJB
	 * @param strGroup
	 * @param strFromDate
	 * @param strToDate
	 * @param strLogDateTime
	 * @return Collection
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection getTrackingReportEJB(TrackingByProjectForm formRequest) throws RemoteException, SQLException;

	/**
	 * Method getArrAssignment
	 * @param strGroup
	 * @param intProjectID
	 * @param arrProjectIDs
	 * @param intProjectStatus
	 * @param strFromDate
	 * @param strToDate
	 * @return arrList
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection getArrAssignment(String strGroup, int intProjectID, 
									   String arrProjectIDs, int intProjectStatus, 
									   String strFromDate, String strToDate) throws RemoteException, SQLException;

	/**
	 * Method getVctAssignment
	 * @return vctList
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Vector getVctAssignment() throws RemoteException, SQLException;

}
