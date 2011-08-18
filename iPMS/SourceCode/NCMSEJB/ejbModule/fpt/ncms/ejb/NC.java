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
 * @(#)NC.java 13-Mar-03
 */


package fpt.ncms.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.EJBObject;

import fpt.ncms.model.NCModel;


/**
 * Remote interface for Enterprise Bean: NC
 */
public interface NC extends javax.ejb.EJBObject {

    /**
     * queryByID
     * Retrieve one NC by ID
     * @param   strNCID - NC ident.number
     * @return  NC information
     * @throws  SQLException
     * @throws  RemoteException
     */
    //public ArrayList queryByID(String strNCID)
    //        throws RemoteException, SQLException;
    public NCModel queryByID(String strNCID, int nLocation)
            throws RemoteException, SQLException;

    /**
     * queryByCriteria
     * List NC by criteria
     * @param   strUser - user who's querying
     * @param   strRole - role of this user
     * @param   strFields - array of field names, separated by comma
     * @param   strCondition - condition for query
     * @param   strOrderBy - sorting condition
     * @param   nFromRow - lower row number limit
     * @param   nToRow - upper row number limit
     * @return  list of NC
     * @throws  SQLException
     * @throws  RemoteException
     */
    public ArrayList queryByCriteria(String strUser, String strRole,
            String strFields, String strCondition,
            String strOrderBy, int nDirection,
            int nFromRow, int nToRow,
            String strFromDate, String strToDate,
            int nUsage) throws RemoteException, SQLException;

    /**
     * getNumByCriteria
     * Get number of NC by criteria
     * @param   strUser - user who's querying
     * @param   strRole - role of this user
     * @param   strCondition - condition for query
     * @return  number of NC
     * @throws  SQLException
     * @throws  RemoteException
     */
    public int getNumByCriteria(String strUser, String strRole,
            String strCondition,
            int nLocation,
            String strFromDate, String strToDate)
            throws RemoteException, SQLException;

    /*
     * addNC
     * Add new NC.
     * @param   strNCLevel - level of NC
     * @param   strProjectID - project that NC is related of
     * @param   strNCType - type of NC
     * @param   strDetectedBy - indicator for NC detection
     * @param   strCode - NC code
     * @param   strDescription - description of NC
     * @param   strCreator - person who raised NC
     * @param   strCreationDate - date when NC has been raised
     * @param   strStatus - status of NC
     * @param   strTypeOfCause - source of NC
     * @param   strCause - NC cause
     * @param   strProcess - process that NC occurs during
     * @param   strImpact - impact of NC
     * @param   strTypeOfAction - type of action to be done
     * @param   strCPAction - CP action to be done
     * @param   strAssignee - person who has been assigned to NC
     * @param   strDeadLine - deadline to deal with NC
     * @param   strRepeat - indicator for NC repeatability
     * @param   strNote - note related to NC
     * @param   strClosureDate - date when NC has been closed
     * @param   strReviewer - person who analyzes NC and assigns NC
     * @param   strKPA - KPA NC refers to
     * @param   strISOClause - ISO Clause NC refers to
     * @param   strGroupName - group that NC is related of
     * @return  number of inserted NC (1)
     * @throws  SQLException
     * @throws  RemoteException
    public int addNC(String strNCLevel, String strProjectID, String strNCType,
            String strDetectedBy, String strCode, String strDescription,
            String strCreator, String strCreationDate, String strStatus,
            String strTypeOfCause, String strCause, String strProcess,
            String strImpact, String strTypeOfAction, String strCPAction,
            String strAssignee, String strDeadLine, String strRepeat,
            String strNote, String strClosureDate, String strReviewer,
            String strKPA, String strISOClause, String strGroupName)
            throws RemoteException, SQLException;
     */
    /**
     * addNC
     * Add a new NC.
     * @param   modelNC NC record
     * @return  number of inserted NC
     * @throws  SQLException
     * @throws  RemoteException
     */
    public int addNC(NCModel modelNC, int nLocation) throws RemoteException,
                                             SQLException,
                                             Exception;
    
    /**
     * updateNC
     * Update a NC.
     * @param   modelNC NC record
     * @return  number of inserted NC
     * @throws  SQLException
     * @throws  RemoteException
     */
    public int updateNC(NCModel modelNC, String strUser, int nLocation)
                throws RemoteException, SQLException, Exception;

    /*
     * updateNC and backup current NC to log
     * Update NC
     * @param   strUserID - ident.number of user who's updating this NC
     * @param   strNCID - NC ident.number
     * @param   strNCLevel - level of NC
     * @param   strProjectID - project that NC is related of
     * @param   strNCType - type of NC
     * @param   strDetectedBy - indicator for NC detection
     * @param   strCode - NC code
     * @param   strDescription - description of NC
     * @param   strStatus - status of NC
     * @param   strTypeOfCause - source of NC
     * @param   strCause - NC cause
     * @param   strProcess - process that NC occurs during
     * @param   strImpact - impact of NC
     * @param   strTypeOfAction - type of action to be done
     * @param   strCPAction - CP action to be done
     * @param   strAssignee - person who has been assigned to NC
     * @param   strDeadLine - deadline to deal with NC
     * @param   strRepeat - indicator for NC repeatability
     * @param   strNote - note related to NC
     * @param   strClosureDate - date when NC has been closed
     * @param   strReviewer - person who analyzes NC and assigns NC
     * @param   strKPA - KPA NC refers to
     * @param   strISOClause - ISO Clause NC refers to
     * @param   strGroupName - group that NC is related of
     * @return  number of updated record
     * @throws  SQLException
     * @throws  RemoteException
    public int updateNC(String strUserID, String strNCID, String strNCLevel,
            String strProjectID, String strNCType, String strDetectedBy,
            String strCode, String strDescription, String strStatus,
            String strTypeOfCause, String strCause, String strProcess,
            String strImpact, String strTypeOfAction, String strCPAction,
            String strAssignee, String strDeadLine, String strRepeat,
            String strNote, String strClosureDate, String strReviewer,
            String strKPA, String strISOClause, String strGroupName)
            throws RemoteException, SQLException;
     */

    /**
     * deleteNC
     * delete a NC.
     * @param   modelNC NC record
     * @return  number of inserted NC
     * @throws  SQLException
     * @throws  RemoteException
     */
    public int deleteNC(NCModel modelNC, int nLocation) throws RemoteException,
                                                SQLException,
                                                Exception;


    /**
     * queryForReport
     * NC overall report
     * @param   strFromDate - date to query report from
     * @param   strToDate - date to query report to
     * @param   strGroupBy - grouping condition (ProjectID/GroupName)
     * @return  report
     * @throws  SQLException
     * @throws  RemoteException
     */
    public ArrayList queryForReport(String strFromDate, String strToDate,
            String strGroupBy, int nUsage, String strNCTypes, int nTypeOfCause)
            throws RemoteException, SQLException;

    /**
     * queryForReport
     * NC overall report
     * @param   strFromDate - date to query report from
     * @param   strToDate - date to query report to
     * @param   strNCType - NC type for reporting
     * @return  report
     * @throws  SQLException
     * @throws  RemoteException
     */
    public ArrayList queryForReportAll(
            String strFromDate, String strToDate, int nUsage, String strNCTypes,
            int nTypeOfCause)
            throws RemoteException, SQLException;


    /**
     * queryForPivotReport
     * NC pivot report
     * @param   strFromDate - date to query report from
     * @param   strToDate - date to query report to
     * @param   strGroupBy - grouping condition (ProjectID/GroupName)
     * @param   strType - report type
     * @return  pivot report
     * @throws  SQLException
     * @throws  RemoteException
     */
    public ArrayList queryForPivotReport(String strFromDate, String strToDate,
            String strGroupBy, String strType, int nUsage, String strNCTypes,
            int nTypeOfCause)
            throws RemoteException, SQLException;

    public String getNCHistory(String strID, int nLocation)
                throws RemoteException, SQLException;
}