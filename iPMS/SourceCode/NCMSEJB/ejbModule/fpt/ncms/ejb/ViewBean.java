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
 * @(#)ViewBean.java 03-Apr-03
 */


package fpt.ncms.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import fpt.ncms.constant.NCMS;
import fpt.ncms.framework.connection.WSConnectionPooling;

/**
 * Bean implementation class for Enterprise Bean: View
 */
public class ViewBean implements javax.ejb.SessionBean {
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
     * queryViewByID
     * Retrieve view information by its ident.number
     * @param   strViewID - view ident.number
     * @return  view information
     * @throws  SQLException
     */
    public ArrayList queryViewByID(String strViewID)
            throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        ArrayList strList = new ArrayList();
        String strSQL = "SELECT Title,Fields,OrderBy "
                + "FROM NCView WHERE ViewID=?";
//        if (debugMode) {
//            System.out.println(strSQL);
//        }
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strViewID);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                strList.add(rs.getString("Title"));
                strList.add(rs.getString("Fields"));
                strList.add(rs.getString("OrderBy"));
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "ViewEJB.queryViewByID.");
            throw new SQLException("ViewEJB.queryViewByID: " + se.toString());
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (pStmt != null) {
                pStmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return strList;
    }

    /**
     * queryViewByAccount
     * Retrieve view information by user's account
     * @param   strAccount - user's account
     * @return  view information
     * @throws  SQLException
     */
    public ArrayList queryViewByAccount(String strAccount)
            throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        ArrayList strList = new ArrayList();
        String strSQL = "SELECT Title,Fields,OrderBy,ViewID "
                + "FROM NCView WHERE Account=? "
                + "ORDER BY Title";
//        if (debugMode) {
//            System.out.println(strSQL);
//        }
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strAccount);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                strList.add(rs.getString("ViewID"));
                strList.add(rs.getString("Title"));
                strList.add(rs.getString("Fields"));
                strList.add(rs.getString("OrderBy"));
            }
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in "
                    + "ViewEJB.queryViewByAccount.");
            throw new SQLException("ViewEJB.queryViewByAccount: "
                    + se.toString());
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (pStmt != null) {
                pStmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return strList;
    }

    /**
     * insertView
     * Add new view to database
     * @param   strAccount - user's account
     * @param   strTitle - view title
     * @param   strFields - field names
     * @param   strOrderBy - sorting condition
     * @return  -1: view with such title already exists
     * @return  other: inserting was successful
     * @throws  SQLException
     */
    public int insertView(String strAccount, String strTitle,
            String strFields, String strOrderBy) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        int retVal = 0;
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            // Note: this method doesn't check for user's existence
            String strSQL = "SELECT ViewID FROM NCView WHERE Title=? AND Account=?";
            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strTitle);
            pStmt.setString(2, strAccount);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                // View with such title already exists
                return -1;
            }
            strSQL = "INSERT INTO NCView(Account,Title,Fields,OrderBy) "
                    + "VALUES(?,?,?,?)";
            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strAccount);
            pStmt.setString(2, strTitle);
            pStmt.setString(3, strFields);
            pStmt.setString(4, strOrderBy);
            retVal = pStmt.executeUpdate();
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in ViewEJB.insertView.");
            throw new SQLException("ViewEJB.insertView: " + se.toString());
        }
        finally {
            if (rs != null) {
                rs.close();
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

    /**
     * deleteView
     * Remove a view from database
     * @param   strViewID - view ident.number
     * @return  number of removed records (1)
     * @throws  SQLException
     */
    public int deleteView(String strViewID)
            throws SQLException {
        PreparedStatement pStmt = null;
        int retVal = 0;
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            // Note: this method doesn't check for view existence
            String strSQL = "DELETE NCView WHERE ViewID=?";
            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strViewID);
            retVal = pStmt.executeUpdate();
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in ViewEJB.deleteView.");
            throw new SQLException("ViewEJB.deleteView: " + se.toString());
        }
        finally {
            if (pStmt != null) {
                pStmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return retVal;
    }

    /**
     * updateView
     * Update view
     * @param   strViewID - view ident.number
     * @param   strTitle - view title
     * @param   strFields - field names
     * @param   strOrderBy - sorting condition
     * @param   strAccount - user's account
     * @return  -1: view with such title already exists
     * @return  other: updating was successful
     * @throws  SQLException
     */
    public int updateView(String strViewID, String strTitle, String strFields,
            String strOrderBy, String strAccount) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        int retVal = 0;
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            // Note: this method doesn't check for view existence
            String strSQL = "SELECT ViewID FROM NCView "
                    + "WHERE Account=? AND Title=? AND ViewID<>?";
            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strAccount);
            pStmt.setString(2, strTitle);
            pStmt.setString(3, strViewID);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                // View with such title already exists
                return -1;
            }
            strSQL = "UPDATE NCView SET Title=?,Fields=?,OrderBy=? "
                    + "WHERE ViewID=?";
            pStmt = con.prepareStatement(strSQL);
            pStmt.setString(1, strTitle);
            pStmt.setString(2, strFields);
            pStmt.setString(3, strOrderBy);
            pStmt.setString(4, strViewID);
            retVal = pStmt.executeUpdate();
        }
        catch (SQLException se) {
            System.out.println("SQLException occurs in ViewEJB.updateView.");
            throw new SQLException("ViewEJB.updateView: " + se.toString());
        }
        finally {
            if (rs != null) {
                rs.close();
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
}