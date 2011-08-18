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
 
 // TrangTK
// 21/12/2001

package fpt.timesheet.ApproveTran.ejb.report;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJBObject;

public interface InquiryRemote extends EJBObject {

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
     * @throws RemoteException
     * @throws SQLException
     */
    public Collection ViewTimesheet(String strAccount, String strApprover, int nProjectID, String arrProjectID, int nStatus, String strDateFrom, String strDateTo, int nSort, String strGroup, int nCurrentPage, boolean bIsForExport) throws RemoteException, SQLException;

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
     * Method getSummaryReportData.
     * @param nProjectID
     * @param arrProjectIDs
     * @param strFromDate
     * @param strToDate
     * @param nStatus
     * @param type
     * @param strDeveloperID
     * @return Collection
     * @throws RemoteException
     * @throws SQLException
     */
    public Collection getSummaryReportData(int nProjectID, String arrProjectIDs, String strFromDate, String strToDate, int nStatus, int type, String strDeveloperID) throws RemoteException, SQLException;

    /**
     * Method getPendingReportData.
     * @param arrProjectIDs
     * @param strFromDate
     * @param strToDate
     * @param strGroup
     * @return Collection
     * @throws RemoteException
     * @throws SQLException
     */
    public Collection getPendingReportData(String arrProjectIDs, String strFromDate, String strToDate, String strGroup) throws RemoteException, SQLException;

	/**
	 * Method getUnapprovedPMEJB
	 * @param strGroup
	 * @param intProjectID
	 * @param arrProjectIDs
	 * @param strFromDate
	 * @param strToDate
	 * @return Collection
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection getUnreviewedPMEJB(String strGroup, 
										 int intProjectID, String arrProjectIDs,
										 String strFromDate, String strToDate, 
										 String strLogDateTime, int intProjectStatus) throws RemoteException, SQLException;
	
	
	/**
	 * Method getUnapprovedQAEJB
	 * @param strGroup
	 * @param intProjectID
	 * @param arrProjectIDs
	 * @param strFromDate
	 * @param strToDate
	 * @return Collection
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection getUnreviewedQAEJB(String strGroup, String strPQAName,
										 int intProjectID, String arrProjectIDs,
										 String strFromDate, String strToDate, 
										 String strLogDateTime, int intProjectStatus) throws RemoteException, SQLException;
	
	/**
	 * @param intProjectID
	 * @param arrProjectIDs
	 * @param strPQAName
	 * @return Collection
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection getPQAList(int intProjectID, String arrProjectIDs, String strPQAName) throws RemoteException, SQLException;	
    
    /**
     * Get project effort distribution by Process, Type of work, KPA, Work product 
     * @param nProjectID
     * @param arrProjectIDs
     * @param nStatus
     * @param strFrom
     * @param strTo
     * @param nReportType
     * @param type
     * @param strDeveloperID
     * @return List of ProjectPivotRow
     * @throws RemoteException
     * @throws SQLException
     */
    public ArrayList getProjectDistribution(int nProjectID, String arrProjectIDs, int nStatus, String strFrom, String strTo, int nReportType, int type, String strDeveloperID) throws RemoteException, SQLException;

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
     * @throws RemoteException
     * @throws SQLException
     */
    public ArrayList getProjectSummary(int nProjectID, String arrProjectIDs, int nStatus, String strFrom, String strTo, int nReportType, int type, String strDeveloperID) throws RemoteException, SQLException;
}
