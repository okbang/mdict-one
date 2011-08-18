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
 
 /*
 * @(#)ApproveRemote.java 25-Apr-03
 */


package fpt.timesheet.ApproveTran.ejb.approve;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.EJBObject;

/**
 * Interface ApproveRemote
 * Description: Remote interface for ApproveEJB.
 * @version 1.0 25-Apr-03
 * @author
 */
public interface ApproveRemote extends EJBObject {

    /**
     * getTimesheetList
     * get list of timesheet
     * @param strListingName - working mode
     * @param nDevId - developer's ident.number
     * @param sProjectIdList - list of project ident.number
     * @param nStatus - status of timesheet record
     * @param strFromDate - date to query from
     * @param strToDate - date to query to
     * @param nSortBy - sorting condition
     * @param nCurrentPage - number of current timesheet page
     * @return list of timesheet
     * @throws SQLException
     */
    public ArrayList getTimesheetList(String strListingName, int nDevId, String sProjectIdList, int nStatus, String strFromDate, String strToDate, String strAccount, int nSortBy, int nCurrentPage) throws RemoteException, SQLException;

    /**
     * getTimesheetList
     * get list of timesheet based on list of timesheet indet.number
     * @param strListingName - working mode
     * @param strIdList - list of timesheet ident.number
     * @param nSortBy - sorting condition
     * @return list of timesheet
     * @throws SQLException
     */
    public ArrayList getTimesheetList(String strListingName, String strIdList, int nSortBy) throws RemoteException, SQLException;

    /**
     * changeStatus
     * Change status of timesheet records by leader or QA
     * @param strListingName - working mode
     * @param nStatus - timesheet status
     * @param arrIdList - list of timesheet ident.number
     * @param arrComment - list of added comment
     * @param strApprover - approver's name
     * @throws SQLException
     */
    public void changeStatus(String strListingName, int nStatus, String[] arrIdList, String[] arrComment, String strApprover) throws RemoteException, SQLException;

    /**
     * changeStatus
     * Change status of timesheet records by leader or QA
     * @param strListingName - working mode
     * @param nStatus - timesheet status
     * @param arrIdList - list of timesheet ident.number
     * @param arrComment - list of added comment
     * @param arrKPA - list of selected KPA_ID
     * @param strApprover - approver's name
     * @throws SQLException
     */
    public void changeStatus(String strListingName, int nStatus, String[] arrIdList, String[] arrComment, String[] arrKPA, String strApprover) throws RemoteException, SQLException;

    /**
     * LDcorrect
     * Update list of timesheet by leader
     * @param strApprover - name of approver
     * @param sId - list of timesheet ident.number
     * @param sProcess - list of process
     * @param sType - list of work type
     * @param sProduct - list of work product
     * @param sDuration - list of duration
     * @param sDescription - list of description
     * @param sDate - list of timesheet date
     * @throws SQLException
     */
    public void LDcorrect(String strApprover, String[] sId, String[] sProcess, String[] sType, String[] sProduct, String[] sDuration, String[] sDescription, String[] sDate) throws RemoteException, SQLException;


    /**
     * QAcorrect
     * Update list of timesheet by QA
     * @param strApprover - name of approver
     * @param nStatus - new status of timesheet
     * @param sId - list of timesheet ident.number
     * @param sType - list of work type
     * @param sProcess - list of process
     * @param sProduct - list of work product
     * @param sKpa - list of KPA
     * @param sDate - list of timesheet date
     * @throws SQLException
     */
    public void QAcorrect(String strApprover, int nStatus, String[] sId, String[] sType, String[] sProcess, String[] sProduct, String[] sKpa, String[] sDate) throws RemoteException, SQLException;

    /**
     * getTotalPage
     * Getter method for nTotalPage
     * @return nTotalPage
     */
    public int getTotalPage() throws RemoteException;

    /**
     * getTotalTimesheet
     * Getter method for nTotalTimesheet
     * @return nTotalTimesheet+
     */
    public int getTotalTimesheet() throws RemoteException;
}
