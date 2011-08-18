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
 * @(#)NCBean.java 13-Mar-03
 */


package fpt.ncms.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import fpt.ncms.framework.connection.WSConnectionPooling;
import fpt.ncms.framework.util.SqlUtil;
import fpt.ncms.model.NCModel;
import fpt.ncms.constant.NCMS;

/**
 * Bean implementation class for Enterprise Bean: NC
 */
public class NCBean implements javax.ejb.SessionBean {
    private javax.ejb.SessionContext mySessionCtx;
    private WSConnectionPooling conPool = new WSConnectionPooling();
    private DataSource ds = null;
    private Connection con = null;
    /** Indicator for a mode the ejb will run on */
    private boolean debugMode = NCMS.DEBUG;

    /**
     * getSessionContext
     */
    public javax.ejb.SessionContext getSessionContext() {
        return mySessionCtx;
    }

    /**
     * setSessionContext
     */
    public void setSessionContext(javax.ejb.SessionContext ctx) {
        mySessionCtx = ctx;
    }

    /**
     * ejbActivate
     */
    public void ejbActivate() {
        try {
//            if (debugMode) {
//                System.out.println("ConstantEJB.makeConnection() called.");
//            }
            ds = conPool.getDS();
            con = ds.getConnection();
        }
        catch (Exception e) {
            throw new EJBException("ejbActivate Exception: " + e.getMessage());
        }
    }

    /**
     * ejbCreate
     */
    public void ejbCreate() throws javax.ejb.CreateException {
    }

    /**
     * ejbPassivate
     */
    public void ejbPassivate() {
    }

    /**
     * ejbRemove
     */
    public void ejbRemove() {
    }

    /**
     * queryByID
     * Retrieve one NC by ID
     * @param   strNCID - NC ident.number
     * @return  NCModel information
     * @throws  SQLException
     */
    //public ArrayList queryByID(String strNCID) throws SQLException {
    public NCModel queryByID(String strNCID, int nLocation) throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        SimpleDateFormat sDate = new SimpleDateFormat(NCMS.DATE_FORMAT);
        //ArrayList strList = new ArrayList();
        NCModel modelNC = null;

        String strSQL =
            "SELECT " +
            "ncid, nclevel, projectid, nctype, detectedby, code, description," +
            "creator, creationdate, status, typeofcause, cause, process," +
            "impact, typeofaction, cpaction, assignee, deadline, repeat,effectofchange, note," +
            "closuredate, reviewer, kpa, isoclause, groupname, reviewdate," +
            // Pick up time values in hour
            "TO_CHAR(creationdate, 'HH24:MI') createTime," +
            "TO_CHAR(deadline, 'HH24:MI') deadlineTime," +
            "TO_CHAR(closuredate, 'HH24:MI') closureTime," +
            "TO_CHAR(reviewdate, 'HH24:MI') reviewTime" +
            " FROM " + SqlUtil.getTableName(nLocation) + " WHERE NCID=?";
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strNCID);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                modelNC = new NCModel();
                modelNC.setNCID(rs.getInt("NCID"));
                modelNC.setNCLevel(rs.getInt("NCLevel"));
                modelNC.setProjectID(rs.getString("ProjectID"));
                modelNC.setNCType(rs.getInt("NCType"));
                modelNC.setDetectedBy(rs.getInt("DetectedBy"));
                modelNC.setCode(rs.getString("Code"));
                modelNC.setDescription(rs.getString("Description"));
                modelNC.setCreator(rs.getString("Creator"));
                modelNC.setCreateDate(rs.getDate("CreationDate"));
                modelNC.setStatus(rs.getInt("Status"));
                modelNC.setTypeOfCause(rs.getInt("TypeOfCause"));
                modelNC.setCause(rs.getString("Cause"));
                modelNC.setProcess(rs.getInt("Process"));
                modelNC.setImpact(rs.getString("Impact"));
                modelNC.setTypeOfAction(rs.getInt("TypeOfAction"));
                modelNC.setCPAction(rs.getString("CPAction"));
                modelNC.setAssignee(rs.getString("Assignee"));
                modelNC.setDeadLine(rs.getDate("DeadLine"));
                
                modelNC.setRepeat(rs.getInt("Repeat"));
                if (rs.wasNull()) {
                    modelNC.setRepeat(-1);  //NULL for repeat is -1
                }
                
				modelNC.setEffectOfChange(rs.getString("EffectOfChange"));
                modelNC.setNote(rs.getString("Note"));
                modelNC.setClosureDate(rs.getDate("ClosureDate"));
                modelNC.setReviewer(rs.getString("Reviewer"));
                modelNC.setKPA(rs.getInt("KPA"));
                modelNC.setISOClause(rs.getInt("ISOClause"));
                modelNC.setGroupName(rs.getString("GroupName"));
                modelNC.setReviewDate(rs.getDate("ReviewDate"));
                //modelNC.setPriority(rs.getInt("Priority"));

                modelNC.setCreateTime(rs.getString("createTime"));
                modelNC.setDeadLineTime(rs.getString("deadlineTime"));
                modelNC.setClosureTime(rs.getString("closureTime"));
                modelNC.setReviewTime(rs.getString("reviewTime"));
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in NCEJB.queryByID.");
            throw new SQLException("NCEJB.queryByID: " + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "NCEJB.queryByID: ");
        }
        return modelNC;
    }

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
     * @param   nUsage - what's mode: NCMS, CallLog
     * @return  list of NC
     * @throws  SQLException
     */
    public ArrayList queryByCriteria(String strUser, String strRole,
            String strFields, String strCondition,
            String strOrderBy, int nDirection,
            int nFromRow, int nToRow,
            String strFromDate, String strToDate,
            int nUsage) throws SQLException {
        StringBuffer sbSQL = new StringBuffer();
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        //SimpleDateFormat sDate = new SimpleDateFormat(NCMS.DATE_FORMAT);
        ArrayList strList = new ArrayList();
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            StringTokenizer sk = new StringTokenizer(strFields, ",");
            // field NC.status is mandatory for every NC list
            StringBuffer sbSelect = new StringBuffer();
            StringBuffer sbFrom = new StringBuffer();
            StringBuffer sbWhere = new StringBuffer();
            StringBuffer sbOrderBy = new StringBuffer();
            sbSelect.append("SELECT C.description status ");
            sbFrom.append(" FROM ").append(SqlUtil.getTableName(nUsage))
                  .append(" NC, NCConstant C ");
            sbWhere.append(" WHERE NC.status = C.ConstantID ");
            // From date, To date
            sbWhere.append(
                    SqlUtil.genDateConstraint("NC.CreationDate",
                                              strFromDate, strToDate));
            // Usage field
            //sbWhere.append(" AND C.usage=").append(nUsage);
            // NCMS
            /*if (nUsage == 0) {
                sbWhere.append(" AND NC.NCType IN (21,22,23)");
            }
            /* Others: CallLog
            else {
                sbWhere.append(" AND NC.NCType=24");
            }*/

            if ("Creator".equalsIgnoreCase(strRole)) {
                // creator can see only NC created by him
                sbWhere.append(" AND NC.creator = '");
                sbWhere.append(strUser);
                sbWhere.append("'");
            }
            if ("Assignee".equalsIgnoreCase(strRole)) {
                // assignee can see only NC created by him or assigned to him
                sbWhere.append(" AND (NC.creator = '");
                sbWhere.append(strUser);
                sbWhere.append("' OR NC.assignee = '");
                sbWhere.append(strUser);
                sbWhere.append("')");
            }
            // reviewers and QA can see all NC
            int i = 1;
            int numField = sk.countTokens();
            ArrayList aFieldName = new ArrayList(numField);
            while (sk.hasMoreTokens()) {
                String sField = sk.nextToken();
                aFieldName.add(sField);
                if ("Status".equalsIgnoreCase(sField)) {
                    // do nothing, this field is already added to SQL query
                }
                else if ("NCID".equalsIgnoreCase(sField)
                        || "ProjectID".equalsIgnoreCase(sField)
                        || "Code".equalsIgnoreCase(sField)
                        || "Description".equalsIgnoreCase(sField)
                        || "Creator".equalsIgnoreCase(sField)
                        || "Cause".equalsIgnoreCase(sField)
                        || "Impact".equalsIgnoreCase(sField)
                        || "CPAction".equalsIgnoreCase(sField)
                        || "Assignee".equalsIgnoreCase(sField)
						|| "EffectOfChange".equalsIgnoreCase(sField)
                        || "Note".equalsIgnoreCase(sField)
                        || "Reviewer".equalsIgnoreCase(sField)
                        || "GroupName".equalsIgnoreCase(sField)) {
                    sbSelect.append(", NC.");
                    sbSelect.append(sField);
                    sbSelect.append(" ");
                    sbSelect.append(sField);
                }
                else if ("CreationDate".equalsIgnoreCase(sField)
                        || "Deadline".equalsIgnoreCase(sField)
                        || "ClosureDate".equalsIgnoreCase(sField)
                        || "ReviewDate".equalsIgnoreCase(sField)) {
                    sbSelect.append(", TO_CHAR(NC.");
                    sbSelect.append(sField);
                    sbSelect.append(",'");
                    if (nUsage == 0) {
                        sbSelect.append(NCMS.SQL_DATE_FORMAT);
                    }
                    else {//if (nUsage == 1) {
                        sbSelect.append(NCMS.SQL_DATE_FORMAT_EXT);
                    }
                    sbSelect.append("') ");
                    sbSelect.append(sField);
                }
                else if ("NCLevel".equalsIgnoreCase(sField)
                        || "NCType".equalsIgnoreCase(sField)
                        || "DetectedBy".equalsIgnoreCase(sField)
                        || "TypeOfCause".equalsIgnoreCase(sField)
                        || "Process".equalsIgnoreCase(sField)
                        || "TypeOfAction".equalsIgnoreCase(sField)
                        || "KPA".equalsIgnoreCase(sField)
                        || "ISOClause".equalsIgnoreCase(sField)) {
                    sbSelect.append(", C");
                    sbSelect.append(i);
                    sbSelect.append(".description ");
                    sbSelect.append(sField);
                    sbFrom.append(", NCConstant C");
                    sbFrom.append(i);
                    sbWhere.append(" AND C");
                    sbWhere.append(i);
                    sbWhere.append(".ConstantId(+)=NC.");
                    sbWhere.append(sField);
                    // Usage field
                    sbWhere.append(" AND C");
                    sbWhere.append(i);
                    sbWhere.append(".Usage(+)=");
                    sbWhere.append(nUsage);
                    i++;
                }
                else if ("Repeat".equalsIgnoreCase(sField)) {
                    if (nUsage == NCMS.USER_NCMS) {
                        sbSelect.append(", NC.");
                        sbSelect.append(sField);
                        sbSelect.append(" ");
                        sbSelect.append(sField);
                    }
                    else {
                        sbSelect.append(", C");
                        sbSelect.append(i);
                        sbSelect.append(".description ");
                        sbSelect.append(sField);
                        sbFrom.append(", NCConstant C");
                        sbFrom.append(i);
                        sbWhere.append(" AND C");
                        sbWhere.append(i);
                        sbWhere.append(".ConstantId(+)=NC.");
                        sbWhere.append(sField);
                        // Usage field
                        sbWhere.append(" AND C");
                        sbWhere.append(i);
                        sbWhere.append(".Usage(+)=");
                        sbWhere.append(nUsage);
                        i++;
                    }
                }
            }

            if ((strCondition != null) && (strCondition.length() != 0)) {
                sbWhere.append(strCondition);
            }

            if ((strOrderBy != null) && (strOrderBy.length() > 0)) {
                // Sort by date field
                if ("CreationDate".equalsIgnoreCase(strOrderBy)
                        || "DeadLine".equalsIgnoreCase(strOrderBy)
                        || "ClosureDate".equalsIgnoreCase(strOrderBy)) {
                    sbOrderBy.append("NC.").append(strOrderBy);
                }
                // Other fields
                else {
                    sbOrderBy.append(strOrderBy);
                }
                // Using direction property
                if (nDirection > 0) {
                    sbOrderBy.append(" ASC");
                }
                else {
                    sbOrderBy.append(" DESC");
                }
                
                /* to avoid wrong record fetching, add NCID as
                   second order condition */
                sbOrderBy.append(",NCID");
            }
            else {
                // this case should never occur
                sbOrderBy.append("NCID");
            }

            sbWhere.append(" ORDER BY ");
            sbWhere.append(sbOrderBy.toString());
            // Important note: this query is valid only for Oracle 8i and later
            sbSQL.append("SELECT * FROM (SELECT ROWNUM R, N.* FROM (");
            sbSQL.append(sbSelect.toString());
            sbSQL.append(sbFrom.toString());
            sbSQL.append(sbWhere.toString());
            sbSQL.append(")N )WHERE R<=? AND R>=?");
//            if (debugMode) {
//                System.out.println(sbSQL.toString());
//            }
            pStmt = con.prepareStatement(sbSQL.toString());
            pStmt.setInt(1, nToRow);
            pStmt.setInt(2, nFromRow);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                // Note: the field 'Status' must be always on the first place
                for (i = 0; i < numField; i++) {
                    strList.add(rs.getString(i + 2) == null ? ""
                            : rs.getString(i + 2));
//                    if ("CreationDate".equalsIgnoreCase(
//                            aFieldName.get(i).toString())
//                            || "DeadLine".equalsIgnoreCase(
//                                    aFieldName.get(i).toString())
//                            || "ClosureDate".equalsIgnoreCase(
//                                    aFieldName.get(i).toString())) {
//                        strList.add(rs.getDate(i + 2) == null ? ""
//                                : sDate.format(rs.getDate(i + 2)));
//                    }
//                    else {
//                        strList.add(rs.getString(i + 2) == null ? ""
//                                : rs.getString(i + 2));
//                    }
                }
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "NCEJB.queryByCriteria.");
            throw new SQLException("NCEJB.queryByCriteria: " + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "NCEJB.queryByCriteria: ");
        }
        return strList;
    }

    /**
     * getNumByCriteria
     * Get number of NC by criteria
     * @param   strUser - user who's querying
     * @param   strRole - role of this user
     * @param   strCondition - condition for query
     * @return  number of NC
     * @throws  SQLException
     */
    public int getNumByCriteria(String strUser, String strRole,
            String strCondition,
            int nLocation,
            String strFromDate, String strToDate) throws SQLException {
        StringBuffer sbSQL = new StringBuffer();
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        int retVal = 0;
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            sbSQL.append("SELECT COUNT(NCID) FROM ")
                 .append(SqlUtil.getTableName(nLocation)).append(" NC ");
            StringBuffer sbCondition = new StringBuffer();
            sbCondition.append(strCondition);
            sbCondition.append("");
            if ("Creator".equalsIgnoreCase(strRole)) {
                // creator can see only NC created by him
                sbCondition.append(" AND NC.creator = '");
                sbCondition.append(strUser);
                sbCondition.append("'");
            }
            if ("Assignee".equalsIgnoreCase(strRole)) {
                // assignee can see only NC created by him or assigned to him
                sbCondition.append(" AND (NC.creator = '");
                sbCondition.append(strUser);
                sbCondition.append("' OR NC.assignee = '");
                sbCondition.append(strUser);
                sbCondition.append("')");
            }
            // reviewers and QA can see all NC
            sbSQL.append("WHERE 1=1 ");
            
            // NCMS
            /*if (nLocation == 0) {
                sbSQL.append(" AND NC.NCType IN (21,22,23)");
            }
            /* Others: CallLog
            else {
                sbSQL.append(" AND NC.NCType=24");
            }*/
            // From date, To date
            sbSQL.append(
                    SqlUtil.genDateConstraint("NC.CreationDate",
                                              strFromDate, strToDate));
            if (sbCondition.length() > 0) {
                sbSQL.append(sbCondition.toString());
            }
            System.out.println(sbSQL.toString());
            pStmt = con.prepareStatement(sbSQL.toString());
            rs = pStmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getInt(1);
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "NCEJB.queryNumByCriteria.");
            throw new SQLException("NCEJB.queryNumByCriteria: "
                    + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "NCEJB.queryNumByCriteria: ");
        }
        return retVal;
    }

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
    public int addNC(String strNCLevel, String strProjectID, String strNCType,
            String strDetectedBy, String strCode, String strDescription,
            String strCreator, String strCreationDate, String strStatus,
            String strTypeOfCause, String strCause, String strProcess,
            String strImpact, String strTypeOfAction, String strCPAction,
            String strAssignee, String strDeadLine, String strRepeat,
            String strNote, String strClosureDate, String strReviewer,
            String strKPA, String strISOClause, String strGroupName)
            throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        PreparedStatement pStmtCheck = null;
        ResultSet rs = null;
        int retVal = -1;
        String strSQL;
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            // Check if code already exists
            strSQL = "SELECT NCID FROM NC WHERE UPPER(Code)=?";
            pStmtCheck = con.prepareStatement(strSQL);
            pStmtCheck.setString(1, strCode.toUpperCase());
            rs = pStmtCheck.executeQuery();
            if (!rs.next()) {
                strSQL = "INSERT INTO NC (NCLevel,ProjectID,NCType,DetectedBy,"
                        + "Code,Description,Creator,CreationDate,Status,TypeOfCause,"
                        + "Cause,Process,Impact,TypeOfAction,CPAction,Repeat,Note,KPA,"
                        + "ISOClause,GroupName,Assignee,DeadLine,ClosureDate,"
                        + "Reviewer) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pStmt = con.prepareStatement(strSQL);
                int i = 1;
                pStmt.setString(i++, strNCLevel);
                pStmt.setString(i++, strProjectID);
                pStmt.setString(i++, strNCType);
                pStmt.setString(i++, strDetectedBy);
                pStmt.setString(i++, strCode);
                pStmt.setString(i++, strDescription);
                pStmt.setString(i++, strCreator);
                pStmt.setString(i++, strCreationDate);
                pStmt.setString(i++, strStatus);
                pStmt.setString(i++, strTypeOfCause);
                pStmt.setString(i++, strCause);
                pStmt.setString(i++, strProcess);
                pStmt.setString(i++, strImpact);
                pStmt.setString(i++, strTypeOfAction);
                pStmt.setString(i++, strCPAction);
                pStmt.setString(i++, strRepeat);
                pStmt.setString(i++, strNote);
                pStmt.setString(i++, strKPA);
                pStmt.setString(i++, strISOClause);
                pStmt.setString(i++, strGroupName);
                pStmt.setString(i++, strAssignee);
                pStmt.setString(i++, strDeadLine);
                pStmt.setString(i++, strClosureDate);
                pStmt.setString(i, strReviewer);
                retVal = pStmt.executeUpdate();
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in NCEJB.addNC.");
            throw new SQLException("NCEJB.addNC: " + se.toString());
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (pStmtCheck != null) {
                pStmtCheck.close();
            }
            if (pStmt != null) {
                pStmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return retVal;
    }
     */
    /**
     * Method addNC.
     * Add a new NC.
     * @param modelNC A NC record
     * @return int
     * @throws SQLException
     */
    public int addNC(NCModel modelNC, int nLocation) throws SQLException, Exception {
        Connection con = null;
        PreparedStatement pStmt = null;
        PreparedStatement pStmtCheck = null;
        ResultSet rs = null;
        int retVal = -1;
        String strSQL;
        boolean bCodeExisted = false;
        
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            con.setAutoCommit(false);
            
            if (modelNC.getCode() != null) {
                // Check if code already exists
                strSQL = "SELECT NCID FROM " + SqlUtil.getTableName(nLocation) +
                         " WHERE UPPER(Code)=?";
                pStmtCheck = con.prepareStatement(strSQL);
                pStmtCheck.setString(1, modelNC.getCode().toUpperCase());
                rs = pStmtCheck.executeQuery();
                if (rs.next()) {
                    bCodeExisted = true;
                }
            }
            
            if (!bCodeExisted) {
                String strGroupName;
                // CallLog system => not included group name
                if (modelNC.getGroupName() == null) {
                    // General project => general group
                    if ("General".equals(modelNC.getProjectID())) {
                        strGroupName = "'General'";
                    }
                    // Retrieve group name from project table
                    else {
                        strGroupName =
                                "(SELECT group_name FROM project WHERE code='" +
                                modelNC.getProjectID() + "')";
                    }
                }
                // NCMS
                else {
                    strGroupName = "'" + modelNC.getGroupName() + "'";
                }
                
                strSQL =
                    "INSERT INTO " + SqlUtil.getTableName(nLocation) +
                    " (CreationDate,DeadLine,ClosureDate,ReviewDate," +
                    "NCLevel,ProjectID,NCType,DetectedBy," +
                    "Code,Description,Creator,Status,TypeOfCause," +
                    "Cause,Process,Impact,TypeOfAction,CPAction,Repeat,Note,KPA," +
                    "ISOClause,GroupName,Assignee,Reviewer) " +
                    "VALUES (" +
                    SqlUtil.genSQL_To_Date(modelNC.getCreateDate()) + "," +
                    SqlUtil.genSQL_To_Date(modelNC.getDeadLine()) + "," +
                    SqlUtil.genSQL_To_Date(modelNC.getClosureDate()) + "," +
                    SqlUtil.genSQL_To_Date(modelNC.getReviewDate()) + "," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    strGroupName +
                    ",?,?)";
                pStmt = con.prepareStatement(strSQL);
                
                int i = 1;
                pStmt.setString(i++, (modelNC.getNCLevel() > 0) ?
                                     Integer.toString(modelNC.getNCLevel()) : null);
                pStmt.setString(i++, modelNC.getProjectID());
                pStmt.setString(i++, (modelNC.getNCType() > 0) ?
                                     Integer.toString(modelNC.getNCType()) : null);
                pStmt.setString(i++, (modelNC.getDetectedBy() > 0) ?
                                     Integer.toString(modelNC.getDetectedBy()) : null);
                pStmt.setString(i++, modelNC.getCode());
                pStmt.setString(i++, modelNC.getDescription());
                pStmt.setString(i++, modelNC.getCreator());
                // Status=5: Opened
                pStmt.setString(i++, (modelNC.getStatus() > 0) ?
                                     Integer.toString(modelNC.getStatus()) : "5");
                pStmt.setString(i++, (modelNC.getTypeOfCause() > 0) ?
                                     Integer.toString(modelNC.getTypeOfCause()) : null);
                pStmt.setString(i++, modelNC.getCause());
                pStmt.setString(i++, (modelNC.getProcess() > 0) ?
                                     Integer.toString(modelNC.getProcess()) : null);
                pStmt.setString(i++, modelNC.getImpact());
                pStmt.setString(i++, (modelNC.getTypeOfAction() > 0) ?
                                     Integer.toString(modelNC.getTypeOfAction()) : null);
                pStmt.setString(i++, modelNC.getCPAction());
                //pStmt.setString(i++, (modelNC.getRepeat() > 0) ?
                //                     Integer.toString(modelNC.getRepeat()) : null);
                
                // -1: null, 0: No, 1: Yes
                pStmt.setString(i++, (modelNC.getRepeat() >= 0) ?
                                     Integer.toString(modelNC.getRepeat()) : null);				
				//pStmt.setString(i++, modelNC.getEffectOfChange());
                pStmt.setString(i++, modelNC.getNote());
                pStmt.setString(i++, (modelNC.getKPA() > 0) ?
                                     Integer.toString(modelNC.getKPA()) : null);
                pStmt.setString(i++, (modelNC.getISOClause() > 0) ?
                                     Integer.toString(modelNC.getISOClause()) : null);
                //pStmt.setString(i++, modelNC.getGroupName());
                pStmt.setString(i++, modelNC.getAssignee());
                pStmt.setString(i++, modelNC.getReviewer());
                //pStmt.setString(i++, (modelNC.getPriority() > 0) ?
                //                     Integer.toString(modelNC.getPriority()) : null);
                retVal = pStmt.executeUpdate();
            }
        }
        catch (SQLException se) {
            con.rollback();
            System.out.println("SQLException occurs in NCEJB.addNC.");
            throw new SQLException("NCEJB.addNC: " + se.toString());
        }
        catch (Exception e) {
            con.rollback();
            System.out.println("Exception occurs in NCEJB.addNC.");
            throw new Exception("NCEJB.addNC: " + e.toString());
        }
        finally {
            conPool.releaseResource(null, pStmtCheck, null, "NCEJB.addNC: ");
            conPool.releaseResourceAndCommit(con, pStmt, rs, "NCEJB.addNC: ");
        }
        return retVal;
    }

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
    public int updateNC(String strUserID, String strNCID, String strNCLevel,
            String strProjectID, String strNCType, String strDetectedBy,
            String strCode, String strDescription, String strStatus,
            String strTypeOfCause, String strCause, String strProcess,
            String strImpact, String strTypeOfAction, String strCPAction,
            String strAssignee, String strDeadLine, String strRepeat,
            String strNote, String strClosureDate, String strReviewer,
            String strKPA, String strISOClause, String strGroupName)
            throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        PreparedStatement pStmtLog = null;
        int retVal = 0;

        String strSQLLog = "INSERT INTO NCLog(logdate,userid,ncid,nclevel,projectid,"
                + "nctype,detectedby,code,description,creator,creationdate,"
                + "status,typeofcause,cause,process,impact,typeofaction,"
                + "cpaction,assignee,deadline,repeat,note,closuredate,kpa,"
                + "isoclause,groupname,reviewer) "
                + "SELECT SYSDATE,?,"
                + "ncid,nclevel,projectid,nctype,detectedby,code,description,"
                + "creator,creationdate,status,typeofcause,cause,process,"
                + "impact,typeofaction,cpaction,assignee,deadline,repeat,"
                + "note,closuredate,kpa,isoclause,groupname,reviewer "
                + "FROM NC "
                + "WHERE ncid=?";

        String strSQL = "UPDATE NC SET NCLevel=?,ProjectID=?,NCType=?,DetectedBy=?,"
                + "Code=?,Description=?,Status=?,TypeOfCause=?,Cause=?,"
                + "Process=?,Impact=?,TypeOfAction=?,CPAction=?,Repeat=?,"
                + "Note=?,KPA=?,ISOClause=?,GroupName=?,Assignee=?,"
                + "DeadLine=?,ClosureDate=?,Reviewer=? "
                + "WHERE NCID=?";
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            pStmtLog = con.prepareStatement(strSQLLog);
            pStmtLog.setString(1, strUserID);
            pStmtLog.setString(2, strNCID);
            pStmtLog.executeUpdate();

            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strNCLevel);
            pStmt.setString(2, strProjectID);
            pStmt.setString(3, strNCType);
            pStmt.setString(4, strDetectedBy);
            pStmt.setString(5, strCode);
            pStmt.setString(6, strDescription);
            pStmt.setString(7, strStatus);
            pStmt.setString(8, strTypeOfCause);
            pStmt.setString(9, strCause);
            pStmt.setString(10, strProcess);
            pStmt.setString(11, strImpact);
            pStmt.setString(12, strTypeOfAction);
            pStmt.setString(13, strCPAction);
            pStmt.setString(14, strRepeat);
            pStmt.setString(15, strNote);
            pStmt.setString(16, strKPA);
            pStmt.setString(17, strISOClause);
            pStmt.setString(18, strGroupName);
            pStmt.setString(19, strAssignee);
            pStmt.setString(20, strDeadLine);
            pStmt.setString(21, strClosureDate);
            pStmt.setString(22, strReviewer);
            pStmt.setString(23, strNCID);
            retVal = pStmt.executeUpdate();
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in NCEJB.updateNC.");
            se.printStackTrace();
            throw new SQLException("NCEJB.updateNC: " + se.toString());
        }
        finally {
            if (pStmtLog != null) {
                pStmtLog.close();
            }
            if (pStmt != null) {
                pStmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return retVal;
    }
     */
    /**
     * Method updateNC.
     * @param modelNC A NC record
     * @param strUser User that updated this NC
     * @return int
     * @throws SQLException
     */
    public int updateNC(NCModel modelNC, String strUser, int nLocation) throws SQLException, Exception {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        int retVal = -1;
        boolean bCodeExisted = false;
        String strSQL;
        
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            con.setAutoCommit(false);
            
            if (modelNC.getCode() != null) {
                // Check other NC already have the Code wants to update
                strSQL = "SELECT NCID FROM " + SqlUtil.getTableName(nLocation) +
                         " WHERE UPPER(Code) = ? AND NCID <> ?";
                pStmt = con.prepareStatement(strSQL);
                pStmt.setString(1, modelNC.getCode().toUpperCase());
                pStmt.setInt(2, modelNC.getNCID());
                rs = pStmt.executeQuery();
                if (rs.next()) {
                    bCodeExisted = true;
                }
                pStmt.close();
            }
            
            if (!bCodeExisted) {
                String strGroupName;
                // CallLog system => not included group name
                if (modelNC.getGroupName() == null) {
                    // General project => general group
                    if ("General".equals(modelNC.getProjectID())) {
                        strGroupName = "'General'";
                    }
                    // Retrieve group name from project table
                    else {
                        strGroupName =
                                "(SELECT group_name FROM project WHERE code='" +
                                modelNC.getProjectID() + "')";
                    }
                }
                // NCMS
                else {
                    strGroupName = "'" + modelNC.getGroupName() + "'";
                }

                strSQL =
                    "UPDATE " + SqlUtil.getTableName(nLocation) + " SET " +
                    "DeadLine=" + SqlUtil.genSQL_To_Date(modelNC.getDeadLine()) + "," +
                    "ClosureDate=" + SqlUtil.genSQL_To_Date(modelNC.getClosureDate()) + "," +
                    "ReviewDate=" + SqlUtil.genSQL_To_Date(modelNC.getReviewDate()) + "," +
                    "NCLevel=?,ProjectID=?,NCType=?,DetectedBy=?," +
                    "Code=?,Description=?,Status=?,TypeOfCause=?,Cause=?," +
                    "Process=?,Impact=?,TypeOfAction=?,CPAction=?,Repeat=?," +
                    "EffectOfChange=?,Note=?,KPA=?,ISOClause=?," +
                    "groupname=" + strGroupName +
                    ",Assignee=?,Reviewer=?," +
                    "Updated_By='" + strUser + "'" +
                    " WHERE NCID=?";
                pStmt = con.prepareStatement(strSQL);
                int i = 1;
                pStmt.setString(i++, (modelNC.getNCLevel() > 0) ?
                                     Integer.toString(modelNC.getNCLevel()) : null);
                pStmt.setString(i++, modelNC.getProjectID());
                pStmt.setString(i++, (modelNC.getNCType() > 0) ?
                                     Integer.toString(modelNC.getNCType()) : null);
                pStmt.setString(i++, (modelNC.getDetectedBy() > 0) ?
                                     Integer.toString(modelNC.getDetectedBy()) : null);
                pStmt.setString(i++, modelNC.getCode());
                pStmt.setString(i++, modelNC.getDescription());
                pStmt.setString(i++, (modelNC.getStatus() > 0) ?
                                     Integer.toString(modelNC.getStatus()) : null);
                pStmt.setString(i++, (modelNC.getTypeOfCause() > 0) ?
                                     Integer.toString(modelNC.getTypeOfCause()) : null);
                pStmt.setString(i++, modelNC.getCause());
                pStmt.setString(i++, (modelNC.getProcess() > 0) ?
                                     Integer.toString(modelNC.getProcess()) : null);
                pStmt.setString(i++, modelNC.getImpact());
                pStmt.setString(i++, (modelNC.getTypeOfAction() > 0) ?
                                     Integer.toString(modelNC.getTypeOfAction()) : null);
                pStmt.setString(i++, modelNC.getCPAction());
                //pStmt.setString(i++, (modelNC.getRepeat() > 0) ?
                //                     Integer.toString(modelNC.getRepeat()) : null);
                
                // -1: null, 0: No, 1: Yes
                pStmt.setString(i++, (modelNC.getRepeat() >= 0) ?
                                     Integer.toString(modelNC.getRepeat()) : null);				
				pStmt.setString(i++, modelNC.getEffectOfChange());
                pStmt.setString(i++, modelNC.getNote());
                pStmt.setString(i++, (modelNC.getKPA() > 0) ?
                                     Integer.toString(modelNC.getKPA()) : null);
                pStmt.setString(i++, (modelNC.getISOClause() > 0) ?
                                     Integer.toString(modelNC.getISOClause()) : null);
                //pStmt.setString(i++, modelNC.getGroupName());
                pStmt.setString(i++, modelNC.getAssignee());
                pStmt.setString(i++, modelNC.getReviewer());
                
                //pStmt.setString(i++, (modelNC.getPriority() > 0) ?
                //                     Integer.toString(modelNC.getPriority()) : null);
                pStmt.setInt(i++, modelNC.getNCID());
                retVal = pStmt.executeUpdate();
            }
        }
        catch (SQLException se) {
            con.rollback();
            System.out.println("SQLException occurs in NCEJB.updateNC.");
            throw new SQLException("NCEJB.updateNC: " + se.toString());
        }
        catch (Exception e) {
            con.rollback();
            System.out.println("Exception occurs in NCEJB.updateNC.");
            throw new Exception("NCEJB.updateNC: " + e.toString());
        }
        finally {
            conPool.releaseResourceAndCommit(con, pStmt, rs, "NCEJB.updateNC: ");
        }
        return retVal;
    }

    /**
     * Method deleteNC.
     * @param modelNC A NC record
     * @return int
     * @throws SQLException
     */
    public int deleteNC(NCModel modelNC, int nLocation) throws SQLException, Exception {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        int retVal = -1;
        String strSQL;
        
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            con.setAutoCommit(false);
            strSQL = "DELETE FROM " + SqlUtil.getTableName(nLocation) +
                     " WHERE NCID=?";
            pStmt = con.prepareStatement(strSQL);
            pStmt.setInt(1,modelNC.getNCID());
            retVal = pStmt.executeUpdate();
        }
        catch (SQLException se) {
            con.rollback();
            System.out.println("SQLException occurs in NCEJB.deleteNC.");
            throw new SQLException("NCEJB.deleteNC: " + se.toString());
        }
        catch (Exception e) {
            con.rollback();
            System.out.println("Exception occurs in NCEJB.deleteNC.");
            throw new Exception("NCEJB.deleteNC: " + e.toString());
        }
        finally {
            conPool.releaseResourceAndCommit(con, pStmt, rs, "NCEJB.deleteNC: ");
        }
        return retVal;
    }

    /**
     * queryForReport
     * NC overall report
     * @param   strFromDate - date to query report from
     * @param   strToDate - date to query report to
     * @param   strGroupBy - grouping condition (ProjectID/GroupName)
     * @return  report
     * @throws  SQLException
     */
    public ArrayList queryForReport(String strFromDate, String strToDate,
            						String strGroupBy, int nUsage, 
            						String strNCTypes, int nTypeOfCause)
            throws SQLException {
        StringBuffer sbSQL = new StringBuffer();
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        ArrayList strList = new ArrayList();
        DecimalFormat decFmt = new DecimalFormat("0.00");
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            
            StringBuffer sbNCType = new StringBuffer();
            // NCMS system
            if (nUsage == 0) {
                if (strNCTypes.length() > 0) {
                    sbNCType.append(" AND NC.NCType IN (")
                            .append(strNCTypes).append(")");
                }
                /*else {
                    sbNCType.append(" AND NC.NCType IN (21,22,23)");
                }*/
            }
            else if (nTypeOfCause > 0) {
                sbNCType.append(" AND NC.TypeOfCause = ").append(nTypeOfCause);
            }
            // Exclude Cancelled NCs from report
            StringBuffer sbNonCancelled = new StringBuffer();
            sbNonCancelled.append(" AND nc.status<>").append(NCMS.NC_STATUS_CANCELLED);
            
            StringBuffer sbFromDate = new StringBuffer();
            StringBuffer sbToDate = new StringBuffer();
            // 16-Oct-2004: To date is mean end of that date so last time of to date is 23:59:59
            if ((strToDate != null) && (strToDate.length() != 0)) {
                sbToDate.append("TO_DATE('");
                sbToDate.append(strToDate);
                sbToDate.append(" 23:59:59','DD-MON-YY HH24:MI:SS')");
                //sbToDate.append("','").append(NCMS.SQL_DATE_FORMAT).append("')");
            }
            else {
                sbToDate.append("SYSDATE");
            }
            if ((strFromDate != null) && (strFromDate.length() != 0)) {
                sbFromDate.append("CreationDate>=TO_DATE('");
                sbFromDate.append(strFromDate);
                sbFromDate.append("','").append(NCMS.SQL_DATE_FORMAT).append("')");
            }
            else {
                sbFromDate.append("CreationDate<=");
                sbFromDate.append(sbToDate.toString());
            }
            sbSQL.append("SELECT A.*,B.*,C.*,D.*,E.*,F.*,G.*,H.* FROM ");
            sbSQL.append("(SELECT COUNT(*) TotalNC,");
            sbSQL.append(strGroupBy);
            sbSQL.append(" AName FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND CreationDate<=");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType.toString());

            sbSQL.append(" GROUP BY ");
            sbSQL.append(strGroupBy);
            sbSQL.append(")A,(SELECT COUNT(*) TotalNCClosed,");
            sbSQL.append(strGroupBy);
            sbSQL.append(" BName FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND ClosureDate<=");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Status=");
            sbSQL.append(NCMS.NC_STATUS_CLOSED);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType.toString());

            sbSQL.append(" GROUP BY ");
            sbSQL.append(strGroupBy);
            sbSQL.append(")B,(SELECT COUNT(*) TotalNCRepeated,");
            sbSQL.append(strGroupBy);
            sbSQL.append(" CName FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND CreationDate<=");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Repeat=");
            sbSQL.append(NCMS.NC_REPEAT);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType.toString());

            sbSQL.append(" GROUP BY ");
            sbSQL.append(strGroupBy);
            sbSQL.append(")C,(SELECT COUNT(*) TotalNCIntime,");
            sbSQL.append(strGroupBy);
            sbSQL.append(" DName FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND Deadline>=ClosureDate AND ClosureDate<=");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Status=");
            sbSQL.append(NCMS.NC_STATUS_CLOSED);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType.toString());

            sbSQL.append(" GROUP BY ");
            sbSQL.append(strGroupBy);
            sbSQL.append(")D,(SELECT COUNT(*) TotalNCDelayed,");
            sbSQL.append(strGroupBy);
            sbSQL.append(" EName FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
//			HanhTN added for fixing date     
				sbSQL.append(sbFromDate.toString());
				sbSQL.append(" AND CreationDate<=");
				sbSQL.append(sbToDate.toString());
            
            //sbSQL.append(sbFromDate.toString());            
            //HanhTN comment --> it canculates wrong Delaying's value
            //sbSQL.append(" AND Deadline < ");
            //sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND ((ClosureDate > ");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(") OR (ClosureDate IS NULL))");
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType);

            sbSQL.append(" GROUP BY ");
            sbSQL.append(strGroupBy);
            sbSQL.append(")E,(SELECT AVG(ClosureDate-CreationDate) AvrFixed,");
            sbSQL.append(strGroupBy);
            sbSQL.append(" FName FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND ClosureDate <= ");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Status=");
            sbSQL.append(NCMS.NC_STATUS_CLOSED);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType.toString());

            sbSQL.append(" GROUP BY ");
            sbSQL.append(strGroupBy);
            sbSQL.append(")F,(SELECT COUNT(*) TotalNCOverTime,");
            sbSQL.append(strGroupBy);
            sbSQL.append(" HName FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND ClosureDate<=");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Deadline IS NOT NULL AND ClosureDate>DeadLine");
            sbSQL.append(" AND Status=");
            sbSQL.append(NCMS.NC_STATUS_CLOSED);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType.toString());

            sbSQL.append(" GROUP BY ");
            sbSQL.append(strGroupBy);
            sbSQL.append(")H,(SELECT AVG(NVL(ReviewDate,CreationDate)-CreationDate) AvrResponse,");
            sbSQL.append(strGroupBy);
            sbSQL.append(" GName FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND ClosureDate <= ");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Status<>");
            sbSQL.append(NCMS.NC_STATUS_OPENED);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType.toString());

            sbSQL.append(" GROUP BY ");
            sbSQL.append(strGroupBy);
            sbSQL.append(")G WHERE B.BName(+)=A.AName AND C.CName(+)=");
            sbSQL.append("A.AName AND D.DName(+)=A.AName AND E.EName(+)=");
            sbSQL.append("A.AName AND F.FName(+)=A.AName AND G.GName(+)=");
            sbSQL.append("A.AName AND H.HName(+)=A.AName");
            
//            if (debugMode) {
//                System.out.println("queryForReport: "+sbSQL.toString());
//            }
            pStmt = con.prepareStatement(sbSQL.toString());
            rs = pStmt.executeQuery();
            while (rs.next()) {
                strList.add(rs.getString("AName") == null ? ""
                        : rs.getString("AName"));
                strList.add(rs.getString("TotalNC") == null ? ""
                        : rs.getString("TotalNC"));
                strList.add(rs.getString("TotalNCInTime") == null ? ""
                        : rs.getString("TotalNCInTime"));
                strList.add(rs.getString("TotalNCDelayed") == null ? ""
                        : rs.getString("TotalNCDelayed"));
                strList.add(rs.getString("TotalNCRepeated") == null ? ""
                        : rs.getString("TotalNCRepeated"));
                strList.add(rs.getString("TotalNCClosed") == null ? ""
                        : rs.getString("TotalNCClosed"));
                strList.add(rs.getString("TotalNCOverTime") == null ? ""
                        : rs.getString("TotalNCOverTime"));
                if (rs.getString("AvrFixed") == null) {
                    strList.add("");
                }
                else {
                    double fAvrFixed = rs.getDouble("AvrFixed");
                    if (nUsage == 0) {
                        strList.add(Long.toString(Math.round(fAvrFixed)));
                    }
                    else {
                        strList.add(decFmt.format(fAvrFixed * 24));
                    }
                }
                if (rs.getString("AvrResponse") == null) {
                    strList.add("");
                }
                else {
                    double fAvrResponse = rs.getDouble("AvrResponse");
                    if (nUsage == 0) {
                        strList.add(Long.toString(Math.round(fAvrResponse)));
                    }
                    else {
                        strList.add(decFmt.format(fAvrResponse * 24));
                    }
                }
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in NCEJB.queryForReport.");
            throw new SQLException("NCEJB.queryForReport: " + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "NCEJB.queryForReport: ");
        }
        return strList;
    }


    public ArrayList queryForReportAll(
                String strFromDate,
                String strToDate,
                int nUsage, String strNCTypes, int nTypeOfCause)
                throws SQLException {
        StringBuffer sbSQL = new StringBuffer();
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        ArrayList strList = new ArrayList();
        DecimalFormat decFmt = new DecimalFormat("0.00");
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            StringBuffer sbNCType = new StringBuffer();
            // NCMS system
            if (nUsage == 0) {
                if (strNCTypes.length() > 0) {
                    sbNCType.append(" AND NC.NCType IN (")
                            .append(strNCTypes).append(")");
                }
                /*else {
                    sbNCType.append(" AND NC.NCType IN (21,22,23)");
                }*/
            }
            // CallLog system
            else if (nTypeOfCause > 0) {
                sbNCType.append(" AND NC.TypeOfCause = ").append(nTypeOfCause);
            }
            // Exclude Cancelled NCs from report
            StringBuffer sbNonCancelled = new StringBuffer();
            sbNonCancelled.append(" AND nc.status<>").append(NCMS.NC_STATUS_CANCELLED);

            StringBuffer sbFromDate = new StringBuffer();
            StringBuffer sbToDate = new StringBuffer();
            if ((strToDate != null) && (strToDate.length() != 0)) {
                sbToDate.append("TO_DATE('");
                sbToDate.append(strToDate);
                sbToDate.append("','").append(NCMS.SQL_DATE_FORMAT).append("')");
            }
            else {
                sbToDate.append("SYSDATE");
            }
            if ((strFromDate != null) && (strFromDate.length() != 0)) {
                sbFromDate.append("CreationDate>=TO_DATE('");
                sbFromDate.append(strFromDate);
                sbFromDate.append("','").append(NCMS.SQL_DATE_FORMAT).append("')");
            }
            else {
                sbFromDate.append("CreationDate<=");
                sbFromDate.append(sbToDate.toString());
            }
            sbSQL.append("SELECT A.*,B.*,C.*,D.*,E.*,F.*,G.*,H.* FROM ");
            sbSQL.append("(SELECT COUNT(*) TotalNC");

            sbSQL.append(" FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND CreationDate<=");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType);

            sbSQL.append(")A,(SELECT COUNT(*) TotalNCClosed");
            sbSQL.append(" FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND ClosureDate<=");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Status=");
            sbSQL.append(NCMS.NC_STATUS_CLOSED);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType);

            sbSQL.append(")B,(SELECT COUNT(*) TotalNCRepeated");
            sbSQL.append(" FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND CreationDate<=");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Repeat=");
            sbSQL.append(NCMS.NC_REPEAT);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType);

            sbSQL.append(")C,(SELECT COUNT(*) TotalNCIntime");
            sbSQL.append(" FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND Deadline>=ClosureDate AND ClosureDate<=");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Status=");
            sbSQL.append(NCMS.NC_STATUS_CLOSED);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType);

            sbSQL.append(")D,(SELECT COUNT(*) TotalNCDelayed");
            sbSQL.append(" FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            //HanhTN added for fixing date     
				sbSQL.append(sbFromDate.toString());
				sbSQL.append(" AND CreationDate<=");
				sbSQL.append(sbToDate.toString());
            //sbSQL.append(sbFromDate.toString());
            //HanhTN comment
            //sbSQL.append(" AND Deadline < ");
            //sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND ((ClosureDate > ");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(") OR (ClosureDate IS NULL))");
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType);

            sbSQL.append(")E,(SELECT AVG(ClosureDate-CreationDate) AvrFixed");
            sbSQL.append(" FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND ClosureDate <= ");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Status=");
            sbSQL.append(NCMS.NC_STATUS_CLOSED);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType);

            sbSQL.append(")F,(SELECT AVG(NVL(ReviewDate, CreationDate)-CreationDate) AvrResponse");
            sbSQL.append(" FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND ClosureDate <= ");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Status<>");
            sbSQL.append(NCMS.NC_STATUS_OPENED);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType);

            sbSQL.append(")G,(SELECT COUNT(*) TotalNCOverTime");
            sbSQL.append(" FROM ").append(SqlUtil.getTableName(nUsage))
                 .append(" NC WHERE ");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND ClosureDate<=");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(" AND Deadline IS NOT NULL AND ClosureDate>DeadLine");
            sbSQL.append(" AND Status=");
            sbSQL.append(NCMS.NC_STATUS_CLOSED);
            sbSQL.append(sbNonCancelled);
            sbSQL.append(sbNCType);
            sbSQL.append(")H");

//            if (debugMode) {
//                System.out.println("queryForReportAll: "+sbSQL.toString());
//            }
            pStmt = con.prepareStatement(sbSQL.toString());
            rs = pStmt.executeQuery();
            while (rs.next()) {
                strList.add(rs.getString("TotalNC") == null ? ""
                        : rs.getString("TotalNC"));
                strList.add(rs.getString("TotalNCInTime") == null ? ""
                        : rs.getString("TotalNCInTime"));
                strList.add(rs.getString("TotalNCDelayed") == null ? ""
                        : rs.getString("TotalNCDelayed"));
                strList.add(rs.getString("TotalNCRepeated") == null ? ""
                        : rs.getString("TotalNCRepeated"));
                strList.add(rs.getString("TotalNCClosed") == null ? ""
                        : rs.getString("TotalNCClosed"));
                strList.add(rs.getString("TotalNCOverTime") == null ? ""
                        : rs.getString("TotalNCOverTime"));
                if (rs.getString("AvrFixed") == null) {
                    strList.add("");
                }
                else {
                    double fAvrFixed = rs.getDouble("AvrFixed");
                    if (nUsage == 0) {
                        strList.add(Long.toString(Math.round(fAvrFixed)));
                    }
                    else {
                        strList.add(decFmt.format(fAvrFixed * 24));
                    }
                }
                if (rs.getString("AvrResponse") == null) {
                    strList.add("");
                }
                else {
                    double fAvrResponse = rs.getDouble("AvrResponse");
                    if (nUsage == 0) {
                        strList.add(Long.toString(Math.round(fAvrResponse)));
                    }
                    else {
                        strList.add(decFmt.format(fAvrResponse * 24));
                    }
                }
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in NCEJB.queryForReportAll.");
            throw new SQLException("NCEJB.queryForReportAll: " + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "NCEJB.queryForReportAll: ");
        }
        return strList;
    }

    /*
     * queryForPivotReport
     * NC pivot report
     * @param   strFromDate - date to query report from
     * @param   strToDate - date to query report to
     * @param   strGroupBy - grouping condition (ProjectID/GroupName)
     * @param   strType - report type
     * @param   nUsage - what's mode: NCMS, CallLog
     * @return  pivot report
     * @throws  SQLException
    public ArrayList queryForPivotReport(String strFromDate, String strToDate,
            String strGroupBy, String strType, int nUsage) throws SQLException {
        StringBuffer sbSQL = new StringBuffer();
        StringBuffer sbNA = new StringBuffer();
        StringBuffer sbSQLfunction;
        Connection con = null;
        PreparedStatement pStmt = null;
        PreparedStatement pStmtType = null;
        PreparedStatement pStmtFunction = null;
        ResultSet rs = null;
        ArrayList strList = new ArrayList();
        ArrayList arrColumn = new ArrayList();
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            
            StringBuffer sbNCType = new StringBuffer();
            // NCMS system
            if (nUsage == 0) {
                sbNCType.append(" AND NC.NCType IN (21,22,23)");
            }
            // CallLog system
            else if (nUsage == 1) {
                sbNCType.append(" AND NC.NCType = 24");
            }
            
            StringBuffer sbToDate = new StringBuffer();
            if ((strToDate != null) && (strToDate.length() != 0)) {
                sbToDate.append("TO_DATE('");
                sbToDate.append(strToDate);
                sbToDate.append("','").append(NCMS.SQL_DATE_FORMAT).append("')");
            }
            else {
                sbToDate.append("SYSDATE");
            }
            StringBuffer sbFromDate = new StringBuffer();
            if ((strFromDate != null) && (strFromDate.length() != 0)) {
                sbFromDate.append("CreationDate>=TO_DATE('");
                sbFromDate.append(strFromDate);
                sbFromDate.append("','").append(NCMS.SQL_DATE_FORMAT).append("')");
            }
            StringBuffer sbConstant = new StringBuffer();
            sbConstant.append("SELECT ConstantID,Description FROM NCConstant");
            sbConstant.append(" WHERE Type=?");
            sbConstant.append(" AND Usage=?");
            pStmtType = con.prepareStatement(sbConstant.toString());
            pStmtType.setString(1, strType);
            pStmtType.setInt(2, nUsage);
            rs = pStmtType.executeQuery();
            // pivot report must contain all states of queried NC constant type
            sbSQL.append("SELECT COUNT(*) TotalNC,NC.");
            sbSQL.append(strGroupBy);
            sbSQL.append(" Code,");
            sbNA.append("(COUNT(*)");
            while (rs.next()) {
                String strConstantID = rs.getString("ConstantID");
                strConstantID = strConstantID == null ? "0"
                        : strConstantID.trim();
                arrColumn.add("F" + strConstantID);
                arrColumn.add(rs.getString("Description"));
                sbSQLfunction = new StringBuffer();
                sbSQLfunction.append("CREATE OR REPLACE FUNCTION f_");
                sbSQLfunction.append(strType);
                sbSQLfunction.append("(intValue IN NUMBER, strID IN VARCHAR)");
                sbSQLfunction.append("\nRETURN NUMBER\nIS\nnCount NUMBER;");
                sbSQLfunction.append("\nBEGIN\nSELECT COUNT(NC.NCID) INTO ");
                sbSQLfunction.append("nCount FROM NC WHERE ");
                sbSQLfunction.append(strType);
                sbSQLfunction.append("=intValue");
                sbSQLfunction.append(sbFromDate.toString());
                sbSQLfunction.append(" AND NC.CreationDate<=");
                sbSQLfunction.append(sbToDate.toString());
                sbSQLfunction.append(sbNCType);
                
                sbSQLfunction.append(" GROUP BY NC.");
                sbSQLfunction.append(strGroupBy);
                sbSQLfunction.append(" HAVING NC.");
                sbSQLfunction.append(strGroupBy);
                sbSQLfunction.append("=strID;\nRETURN nCount;\nEND;");
                pStmtFunction = con.prepareStatement(sbSQLfunction.toString());
                pStmtFunction.execute();
                pStmtFunction.close();
                sbSQL.append("NVL(f_");
                sbSQL.append(strType);
                sbSQL.append("(");
                sbSQL.append(strConstantID);
                sbSQL.append(",NC.");
                sbSQL.append(strGroupBy);
                sbSQL.append("),0)F");
                sbSQL.append(strConstantID);
                sbSQL.append(",");
                sbNA.append("-NVL(f_");
                sbNA.append(strType);
                sbNA.append("(");
                sbNA.append(strConstantID);
                sbNA.append(",NC.");
                sbNA.append(strGroupBy);
                sbNA.append("),0)");
            }
            rs.close();
            pStmtType.close();
            sbSQL.append(sbNA.toString());
            sbSQL.append(") AS NA FROM NC WHERE 1=1");
            sbSQL.append(sbFromDate.toString());
            sbSQL.append(" AND NC.CreationDate<=");
            sbSQL.append(sbToDate.toString());
            sbSQL.append(sbNCType);
            
            sbSQL.append(" GROUP BY NC.");
            sbSQL.append(strGroupBy);
            if (debugMode) {
                System.out.println("queryForPivotReport:" +
                                   sbSQL.toString());
            }
            pStmt = con.prepareStatement(sbSQL.toString());
            rs = pStmt.executeQuery();
            // Add column name to result
            strList.add("Name");
            strList.add("Total");
            for (int i = 0; i < arrColumn.size() / 2; i++) {
                strList.add(arrColumn.get(i * 2 + 1).toString());
            }
            strList.add("The rest");
            // Result
            while (rs.next()) {
                strList.add(rs.getString("Code"));
                strList.add(rs.getString("TotalNC"));
                for (int i = 0; i < arrColumn.size() / 2; i++) {
                    strList.add(rs.getString(arrColumn.get(i * 2).toString()));
                }
                strList.add(rs.getString("NA"));
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "NCEJB.queryForPivotReport.");
            throw new SQLException("NCEJB.queryForPivotReport: "
                    + se.toString());
        }
        finally {
            conPool.releaseResource(null, pStmtFunction, null, "NCEJB.queryForPivotReport: ");
            conPool.releaseResource(null, pStmtType, null, "NCEJB.queryForPivotReport: ");
            conPool.releaseResource(con, pStmt, rs, "NCEJB.queryForPivotReport: ");
            /*if (rs != null) {
                rs.close();
            }
            if (pStmtFunction != null) {
                pStmtFunction.close();
            }
            if (pStmtType != null) {
                pStmtType.close();
            }
            if (pStmt != null) {
                pStmt.close();
            }
            if (con != null) {
                con.close();
            }*/
/*        }
        return strList;
    }
*/
    /**
     * Get the detail report of Project/Group for a specific Constant type
     * NC pivot report
     * @param   strFromDate - date to query report from
     * @param   strToDate - date to query report to
     * @param   strGroupBy - grouping condition (ProjectID/GroupName)
     * @param   strType - report type
     * @param   nUsage - what's mode: NCMS, CallLog
     * @return  pivot report
     * @throws  SQLException
     */
    public ArrayList queryForPivotReport(String strFromDate, String strToDate,
            String strGroupBy, String strType, int nUsage, String strNCTypes,
            int nTypeOfCause)
            throws SQLException {
        StringBuffer sbSQL = new StringBuffer();
        StringBuffer sbSQLPivot = new StringBuffer();
        StringBuffer sbSQLSelect = new StringBuffer();
        Connection con = null;
        PreparedStatement pStmt = null;
        PreparedStatement pStmtConstant = null;
        PreparedStatement pStmtCount = null;
        ResultSet rs = null;
        ResultSet rsConstant = null;
        ResultSet rsCount = null;
        ArrayList arrConstant = new ArrayList();
        ArrayList arrList = new ArrayList();
        ArrayList arrData = new ArrayList();
        int arrNum[] = new int[2];  // Number of Rows, Columns
        arrList.add(arrNum);
        /*
         * The structure of return list (arrList) as follow:
         *      - int[2] : number of rows (currently not used), number of columns
         *      - ArrayList: list of constants (columns)
         *      - ArrayList: query result represent in string data type*/
        
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            
            String strNCTypeCondition = "";
            // NCMS system
            if (nUsage == 0) {
                if (strNCTypes.length() > 0) {
                    strNCTypeCondition +=
                            " AND NC.NCType IN (" + strNCTypes + ")";
                }
                /*else {
                    strNCTypeCondition = " AND NC.NCType IN (21,22,23)";
                }*/
            }
            // CallLog system
            else if (nTypeOfCause > 0) {
                strNCTypeCondition = " AND NC.TypeOfCause = " + nTypeOfCause;
            }
            // Exclude Cancelled NCs from report
            StringBuffer sbNonCancelled = new StringBuffer();
            sbNonCancelled.append(" AND nc.status<>").append(NCMS.NC_STATUS_CANCELLED);
            
            String strDateConstraint = SqlUtil.genDateConstraint(
                            "CreationDate", strFromDate, strToDate);
            StringBuffer sbConstantSQL = new StringBuffer();
            StringBuffer sbCountSQL = new StringBuffer();
            StringBuffer sbConstantSet = new StringBuffer();
            
            // Count number of constants
            sbCountSQL.append("SELECT COUNT(*) FROM NCConstant");
            sbCountSQL.append(" WHERE Type=? AND Usage=?");
            pStmtCount = con.prepareStatement(sbCountSQL.toString());
            pStmtCount.setString(1, strType);
            pStmtCount.setInt(2, nUsage);
            rsCount = pStmtCount.executeQuery();
            if (rsCount.next()) {
                // Number of columns =
                //      number of constants +
                //      groupby field + total field + other field
                arrNum[1] = rsCount.getInt(1) + 3;
            }
            // Get constant names
            sbConstantSQL.append("SELECT ConstantID,Description FROM NCConstant");
            sbConstantSQL.append(" WHERE Type=? AND Usage=? ORDER BY Description");
            pStmtConstant = con.prepareStatement(sbConstantSQL.toString());
            pStmtConstant.setString(1, strType);
            pStmtConstant.setInt(2, nUsage);
            rsConstant = pStmtConstant.executeQuery();
            // pivot report must contain all states of queried NC constant type
            sbSQLSelect.append("SELECT COUNT(*) AS TotalNC,");
            sbSQLSelect.append(strGroupBy).append(" AS Code");
            
            arrConstant.add("Request from");
            arrConstant.add("Total");
            // Loop of constants title
            while (rsConstant.next()) {
                String strConstantID = rsConstant.getString("ConstantID");
                if (strConstantID != null) {
                    sbConstantSet.append(strConstantID).append(",");
                    sbSQLPivot.append(", COUNT(CASE WHEN ").append(strType)
                         .append("='").append(strConstantID).append("'")
                         .append(" THEN 1 ELSE NULL END)")
                         .append(" AS const_").append(strConstantID);
                    
                    // Add column names
                    String strDesc = rsConstant.getString("Description");
                    arrConstant.add(strDesc);
                }
            }
            if (sbConstantSet.length() > 0) {
                sbConstantSet.deleteCharAt(sbConstantSet.length() - 1);
            }
            arrConstant.add("The rest");
            arrList.add(arrConstant);
            
            // Constant type is NULL
            sbSQLPivot.append(", COUNT(CASE WHEN ").append(strType)
                      .append(" NOT IN (").append(sbConstantSet)
                      .append(") THEN 1 ELSE NULL END)")
                      .append(" AS Other");
            sbSQLPivot.append(" FROM ").append(SqlUtil.getTableName(nUsage))
                      .append(" NC WHERE 1=1 ");
            sbSQLPivot.append(sbNonCancelled)
                      .append(strNCTypeCondition)
                      .append(strDateConstraint);
            sbSQL.append(sbSQLSelect).append(sbSQLPivot)
                        .append(" GROUP BY ").append(strGroupBy);
            
//            if (debugMode) {
//                System.out.println("queryForPivotReport:" + sbSQL);
//            }
            pStmt = con.prepareStatement(sbSQL.toString());
            rs = pStmt.executeQuery();
            int nColumn;    // Column index
            while (rs.next()) {
                String strCode = rs.getString("Code");
                String strTotal = rs.getString("TotalNC");
                arrData.add(strCode);
                arrData.add(strTotal);
                nColumn = 3;    // after Name and Total
                for (int i = 0; i < arrConstant.size() - 2; i++) {
                    arrData.add(rs.getString(nColumn++));
                }
                // Increase number of rows
                arrNum[0]++;
            }
            rs.close();
            pStmt.close();
            
            // Get summary of each column
            sbSQLSelect.setLength(0);
            sbSQLSelect.append("SELECT COUNT(*) AS TotalNC,");
            sbSQLSelect.append(" 'All' AS Code");
            sbSQL.setLength(0);
            sbSQL.append(sbSQLSelect).append(sbSQLPivot);
            
            pStmt = con.prepareStatement(sbSQL.toString());
            rs = pStmt.executeQuery();
            // result set always 1 by default
            while (rs.next()) {
                String strCode = rs.getString("Code");
                String strTotal = rs.getString("TotalNC");
                arrData.add(strCode);
                arrData.add(strTotal);
                nColumn = 3;    // after Name and Total
                for (int i = 0; i < arrConstant.size() - 2; i++) {
                    arrData.add(rs.getString(nColumn++));
                }
                // Increase number of rows
                arrNum[0]++;
            }
            arrList.add(arrData);
            
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "NCEJB.queryForPivotReport.");
            throw new SQLException("NCEJB.queryForPivotReport: "
                    + se.toString());
        }
        finally {
            conPool.releaseResource(null, pStmtConstant, rsConstant, "NCEJB.queryForPivotReport: ");
            conPool.releaseResource(null, pStmtCount, rsCount, "NCEJB.queryForPivotReport: ");
            conPool.releaseResource(con, pStmt, rs, "NCEJB.queryForPivotReport: ");
        }
        return arrList;
        //return new ArrayList();
    }

    /**
     * Get history of NC.
     * @param strID
     * @return String
     * @throws SQLException
     * @throws Exception
     */
    public String getNCHistory(String strID, int nLocation) throws SQLException{
        String strHistory = "";
        DataSource ds = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            ds = conPool.getDS();
            con = ds.getConnection();
            String strSQL =
                    "SELECT * FROM " + SqlUtil.getHistoryTableName(nLocation) +
                    " WHERE NCID=?";
//            if (debugMode) {
//                System.out.println("getNCHistory: " + strSQL);
//            }
            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strID);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                strHistory = rs.getString("History");
            }

        }
        catch (SQLException e) {
            System.out.println("SQLException occurs in "
                    + "NCEJB.getNCHistory.");
            throw new SQLException("NCEJB.getNCHistory: "
                    + e.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs, "NCEJB.getNCHistory(): ");
        } //end finally

        return strHistory;
    }

}