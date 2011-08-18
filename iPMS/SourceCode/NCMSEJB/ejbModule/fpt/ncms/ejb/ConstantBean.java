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
 * @(#)ConstantBean.java 12-Mar-03
 */


package fpt.ncms.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import fpt.ncms.framework.connection.WSConnectionPooling;
import fpt.ncms.constant.NCMS;

/**
 * Bean implementation class for Enterprise Bean: Constant
 */
public class ConstantBean implements javax.ejb.SessionBean {
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
     * queryConstantByID
     * Retrieve constant information by ident.numbers
     * @param   strConstantID - list of constant ident.numbers
     * @return  list of constant information
     * @throws  SQLException
     */
    public ArrayList queryConstantByID(String strConstantID)
            throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList strList = new ArrayList();
        String strSQL = "SELECT ConstantID,Description,Type "
                + "FROM NCConstant WHERE ConstantID IN " + strConstantID
                + " ORDER BY Type,Description,ConstantID";
//        if (debugMode) {
//            System.out.println(strSQL);
//        }
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSQL);
            while (rs.next()) {
                strList.add(rs.getString("ConstantID"));
                strList.add(rs.getString("Description"));
                strList.add(rs.getString("Type"));
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "ConstantEJB.queryConstantByID.");
            throw new SQLException("ConstantEJB.queryConstantByID: "
                    + se.toString());
        }
        finally {
            conPool.releaseResource(con, stmt, rs,
                                    "ConstantEJB.queryConstantByID: ");
        }
        return strList;
    }

    /**
     * queryConstantByType
     * List constants by its type
     * @param   strConstantType - type of Constant
     * @return  list of constants of this type
     * @throws  SQLException
     */
    public ArrayList queryConstantByType(String strConstantType, int nUsage)
            throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        ArrayList strList = new ArrayList();
        String strSQL =
                "SELECT ConstantID,Description " +
                "FROM NCConstant " +
                " WHERE (Type=?) AND (Usage=?) " +
                "ORDER BY Description";
//        if (debugMode) {
//            System.out.println(strSQL);
//        }
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strConstantType);
            pStmt.setInt(2, nUsage);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                strList.add(rs.getString("ConstantID"));
                strList.add(rs.getString("Description"));
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "ConstantEJB.queryConstantByType.");
            throw new SQLException("ConstantEJB.queryConstantByType: "
                    + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs,
                                    "ConstantEJB.queryConstantByType: ");
        }
        return strList;
    }

    /**
     * queryConstant
     * List constants by certain condition
     * @param   strConstantType - sorting condition
     * @param   nFromRow - lower row number limit
     * @param   nToRow - upper row number limit
     * @return  list of constants
     * @throws  SQLException
     */
    public ArrayList queryConstant(String strConstantType, int nUsage,
            int nFromRow, int nToRow) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        StringBuffer sbSQL = new StringBuffer();
        ArrayList strList = new ArrayList();
        sbSQL.append("SELECT * FROM (SELECT ROWNUM R,N.* FROM (");
        sbSQL.append("SELECT * FROM NCConstant WHERE SystemType=0 ");
        if (!"All".equalsIgnoreCase(strConstantType)) {
            sbSQL.append("AND Type='" + strConstantType + "' ");
        }
        else {
            if (nUsage == 0) {
                sbSQL.append(" AND Type NOT IN(");
                sbSQL.append("'Priority', 'NCLevel', 'KPA', 'Process', 'NCType') ");
            }
            else {//if (nUsage == 1) {
                sbSQL.append(" AND Type NOT IN(");
                sbSQL.append("'ISOClause', 'KPA', 'DetectedBy', 'NCLevel', 'NCType') ");
            }
        }
        
        sbSQL.append("AND usage=? ");
        sbSQL.append("ORDER BY Type,Description,ConstantID)N WHERE ROWNUM<=?");
        sbSQL.append(") WHERE R>=?");
//        if (debugMode) {
//            System.out.println("queryConstant.SQL: " + sbSQL);
//        }
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            pStmt = con.prepareStatement(sbSQL.toString());
            pStmt.setInt(1, nUsage);
            pStmt.setInt(2, nToRow);
            pStmt.setInt(3, nFromRow);
            rs = pStmt.executeQuery();
            // NCMS
            if (nUsage == 0) {
                while (rs.next()) {
                    strList.add(rs.getString("ConstantID"));
                    strList.add(rs.getString("Description"));
                    strList.add(rs.getString("Type"));
                }
            }
            // Call
            else {//if (nUsage == 1) {
                while (rs.next()) {
                    strList.add(rs.getString("ConstantID"));
                    strList.add(rs.getString("Description"));
                    String strType = rs.getString("Type");
                    if ("TypeOfCause".equalsIgnoreCase(strType)) {
                        strList.add("RequestTo");
                    }
                    else if ("TypeOfAction".equalsIgnoreCase(strType)) {
                        strList.add("TypeOfSolution");
                    }
                    else if ("Repeat".equalsIgnoreCase(strType)) {
                        strList.add("Priority");
                    }
                    else {
                        strList.add(strType);
                    }
                }
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "ConstantEJB.queryConstant.");
            throw new SQLException("ConstantEJB.queryConstant: "
                    + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs,
                                    "ConstantEJB.queryConstant: ");
        }
        return strList;
    }

    /**
     * getNumByType
     * Get number of constant by its type
     * @param   strConstantType - sorting condition
     * @return  number of constants
     * @throws  SQLException
     */
    public int getNumByType(String strConstantType, int nUsage, boolean isShowAll) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        StringBuffer sbSQL = new StringBuffer();
        int retVal = 0;
        sbSQL.append("SELECT COUNT(ConstantID) FROM NCConstant ");
        sbSQL.append("WHERE SystemType=0 ");
        if (!"All".equalsIgnoreCase(strConstantType)) {
            sbSQL.append("AND Type='" + strConstantType + "' ");
        }
        
        if (nUsage == 0) {
            sbSQL.append(" AND Type NOT IN(");
            if (!isShowAll) {
                sbSQL.append("'Priority', 'NCLevel', 'KPA', 'Process', 'NCType') ");
            }
            else {
                sbSQL.append("'Priority') ");
            }
        }
        else {//if (nUsage == 1) {
            sbSQL.append(" AND Type NOT IN(");
            sbSQL.append("'ISOClause', 'KPA', 'DetectedBy', 'NCLevel', 'NCType') ");
        }
        sbSQL.append("AND usage=? ");
        
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            pStmt = con.prepareStatement(sbSQL.toString());
            pStmt.setInt(1, nUsage);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                retVal = rs.getInt(1);
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "ConstantEJB.getNumByType.");
            throw new SQLException("ConstantEJB.getNumByType: "
                    + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs,
                                    "ConstantEJB.getNumByType: ");
        }
        return retVal;
    }

    /**
     * queryConstantType
     * List all constant types
     * @return  list of constant types
     * @throws  SQL Exception
     */
    public ArrayList queryConstantType(int nUsage, boolean isShowAll,
                                       String strExcludeConstants)
                                       throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        ArrayList strList = new ArrayList();
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("SELECT Description FROM NCConstant ");
        sbSQL.append("WHERE SystemType=1 AND Type='System' ");
        // NCMS
        if (nUsage == 0) {
            sbSQL.append(" AND Description NOT IN(");
            if (!isShowAll) {
                sbSQL.append("'Priority', 'NCLevel', 'KPA', 'Process', 'NCType') ");
            }
            else {
                sbSQL.append("'Priority') ");
            }
            //sbSQL.append("'Priority', 'NCLevel', 'KPA', 'Process', 'NCType') ");
        }
        // Call
        else {//if (nUsage == 1) {
            sbSQL.append(" AND Description NOT IN(");
            sbSQL.append("'ISOClause', 'KPA', 'DetectedBy', 'NCLevel', 'NCType'");
            if ( (strExcludeConstants != null) && (!"".equals(strExcludeConstants)) ) {
                sbSQL.append(",").append(strExcludeConstants);
            }
            sbSQL.append(") ");
        }
        sbSQL.append(" ORDER BY Type,Description");
//        if (debugMode) {
//            System.out.println("queryConstantType.SQL: " + sbSQL);
//        }
        
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            pStmt = con.prepareStatement(sbSQL.toString());
            rs = pStmt.executeQuery();
            while (rs.next()) {
                strList.add(rs.getString("Description"));
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
            System.out.println("SQLException occurs in "
                    + "ConstantEJB.queryConstantType.");
            throw new SQLException("ConstantEJB.queryConstantType: "
                    + se.toString());
        }
        finally {
            conPool.releaseResource(con, pStmt, rs,
                                    "ConstantEJB.queryConstantType: ");
        }
        return strList;
    }

    /**
     * deleteConstant
     * Delete constant by ident.number
     * @param   strConstantID - constant ident.number
     * @return  -1: constant is linked to some NC or constant doesn't exist
     * @return  others: constant was deleted successfully
     * @throws  SQLException
     */
    public int deleteConstant(String strConstantID)
            throws SQLException {
        PreparedStatement pStmt = null;
        PreparedStatement pStmtCheckExist = null;
        PreparedStatement pStmtCheckLink = null;
        ResultSet rs = null;
        int retVal = 0;
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            // check for constant existence
            String strSQL = "SELECT Type FROM NCConstant "
                    + "WHERE ConstantID=?";
            pStmtCheckExist = con.prepareStatement(strSQL);
            pStmtCheckExist.setString(1, strConstantID);
            rs = pStmtCheckExist.executeQuery();
            if (rs.next()) {
                String strType = rs.getString(1);
                // check if constant is linked to some NC
                strSQL = "SELECT COUNT(NCID) FROM NC "
                        + "WHERE " + strType + "=? ";
                pStmtCheckLink = con.prepareStatement(strSQL);
                pStmtCheckLink.setString(1, strConstantID);
                rs = pStmtCheckLink.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        // Some NC is linked to this constant
                        retVal = -1;
                    }
                    else {
                        strSQL = "DELETE NCConstant WHERE ConstantID=?";
                        pStmt = con.prepareStatement(strSQL);
                        pStmt.setString(1, strConstantID);
                        retVal = pStmt.executeUpdate();
                    }
                }
            }
            else {
                // constant doesn't exist
                retVal = -1;
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "ConstantEJB.deleteConstant.");
            throw new SQLException("ConstantEJB.deleteConstant: "
                    + se.toString());
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (pStmt != null) {
                pStmt.close();
            }
            if (pStmtCheckLink != null) {
                pStmtCheckLink.close();
            }
            if (pStmtCheckExist != null) {
                pStmtCheckExist.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return retVal;
    }

    /**
     * insertConstant
     * Insert new constant to database
     * @param   strConstantType - type of inserted constant
     * @param   strDescription - value of inserted constant
     * @return  -2: parameters are wrong
     * @return  -1: constant already exists
     * @return  others: inserting was successful
     * @throws  SQLException
     */
    public int insertConstant(String strConstantType,
                              String strDescription,
                              int nUsage) throws SQLException {
        PreparedStatement pStmt = null;
        PreparedStatement pCheckStmt = null;
        ResultSet rs = null;
        int retVal = 0;
        // check if parameters are wrong
        if ("".equals(strConstantType) || (strConstantType == null)
                || "".equals(strDescription) || (strDescription == null)) {
            return -2;
        }
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            con.setAutoCommit(false);
            // check for constant existence
            String strSQL = "SELECT ConstantID FROM NCConstant "
                    + "WHERE Type=? AND Description=? AND usage=?";
            pCheckStmt = con.prepareStatement(strSQL);
            pCheckStmt.setString(1, strConstantType);
            pCheckStmt.setString(2, strDescription);
            pCheckStmt.setInt(3, nUsage);
            rs = pCheckStmt.executeQuery();
            if (rs.next()) {
                retVal = -1; // Constant already exists
            }
            else {
                strSQL = "INSERT INTO NCConstant " +
                         "(Type,Description,SystemType, usage) " +
                         "VALUES (?,?,0,?)";
                pStmt = con.prepareStatement(strSQL);
                pStmt.setString(1, strConstantType);
                pStmt.setString(2, strDescription);
                pStmt.setInt(3, nUsage);
                retVal = pStmt.executeUpdate();
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "ConstantEJB.insertConstant.");
            throw new SQLException("ConstantEJB.insertConstant: "
                    + se.toString());
        }
        finally {
            conPool.releaseResource(null, pCheckStmt, null,
                                    "ConstantEJB.insertConstant: ");
            conPool.releaseResourceAndCommit(con, pStmt, rs,
                                    "ConstantEJB.insertConstant: ");
        }
        return retVal;
    }

    /**
     * updateConstant
     * Update value of constant
     * @param   strDescription - value of updated constant
     * @param   strConstantID - ident.number of updated constant
     * @return  -2: parameters are wrong
     * @return  -1: constant of same type and same value already exists
     * @return  others: updating was successful
     * @throws  SQLException
     */
    public int updateConstant(String strDescription, String strConstantID,
                              int nUsage) throws SQLException {
        PreparedStatement pStmt = null;
        PreparedStatement pCheckStmt = null;
        ResultSet rs = null;
        int retVal = 0;
        // check if parameters are wrong
        if ("".equals(strDescription) || (strDescription == null)
                || "".equals(strConstantID) || (strConstantID == null)) {
            return -2;
        }
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            con.setAutoCommit(false);
            // check for duplicity
            String strSQL = "SELECT ConstantID FROM NCConstant "
                    + "WHERE Type IN (SELECT Type FROM NCConstant WHERE ConstantID="
                    + strConstantID + ") "
                    + "AND Description=? AND ConstantID<>? AND usage=?";
            pCheckStmt = con.prepareStatement(strSQL);
            pCheckStmt.setString(1, strDescription);
            pCheckStmt.setString(2, strConstantID);
            pCheckStmt.setInt(3, nUsage);
            rs = pCheckStmt.executeQuery();
            if (rs.next()) {
                // constant of same type and same value already exists
                retVal = -1;
            }
            else {
                strSQL = "UPDATE NCConstant SET "
                        + "Description=? "
                        + "WHERE ConstantID=?";
                pStmt = con.prepareStatement(strSQL);
                pStmt.setString(1, strDescription);
                pStmt.setString(2, strConstantID);
                retVal = pStmt.executeUpdate();
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "ConstantEJB.updateConstant.");
            throw new SQLException("ConstantEJB.updateConstant: "
                    + se.toString());
        }
        finally {
            conPool.releaseResource(null, pCheckStmt, null,
                                    "ConstantEJB.updateConstant: ");
            conPool.releaseResourceAndCommit(con, pStmt, rs,
                                    "ConstantEJB.updateConstant: ");
        }
        return retVal;
    }
}