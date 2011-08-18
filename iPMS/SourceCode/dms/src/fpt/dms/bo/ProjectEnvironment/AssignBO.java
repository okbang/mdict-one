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
 
 
package fpt.dms.bo.ProjectEnvironment;

/**
 * Title:        AssignBO.java
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      FPT - FSOFT
 * Author:       Nguyen Thai Son
 * Create date:  October 29, 2002
 * Modified date:
 */

//imported from standard JAVA

import fpt.dms.bean.ProjectEnvironment.AssignListBean;
import fpt.dms.constant.DMS;
import fpt.dms.framework.connection.WSConnectionPooling;
import fpt.dms.framework.util.CommonUtil.CommonUtil;
import fpt.dms.framework.util.StringUtil.StringMatrix;
import fpt.dms.framework.util.StringUtil.StringVector;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AssignBO {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    //private Connection con = null;

    private static Logger logger = Logger.getLogger(AssignBO.class.getName());

    public AssignBO() {
    }

    public AssignListBean getAssignList(int nProjectID) throws SQLException, Exception {
        AssignListBean beanAssignList = new AssignListBean();
        StringMatrix smResult = new StringMatrix();
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            strSQL.append("SELECT DP.Developer_ID, DP.Position, DP.Status, D.Name");
            strSQL.append(" FROM Defect_Permission DP, Developer D");
            strSQL.append(" WHERE DP.Developer_ID = D.Developer_ID(+)");
            strSQL.append(" AND DP.Project_ID = " + nProjectID);
            strSQL.append(" ORDER BY D.Name");
            if (DMS.DEBUG)
                logger.debug("getAssignList(): strSQL = " + strSQL.toString());

            ds = conPool.getDS();
            con = ds.getConnection();
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();

            StringVector svAssign = new StringVector(4);
            String strPosition = "";
            String strTemp = "";
            while (rs.next()) {
                strTemp = "";
                svAssign.setCell(0, rs.getString("Developer_ID"));
                svAssign.setCell(1, CommonUtil.correctHTMLError(rs.getString("Name")));
                strPosition = rs.getString("Position");

                if (!"".equals(strPosition)) {
                    if (strPosition.substring(0, 1).equals("1")) {
                        strTemp = "Developer";
                    }
                    if (strPosition.substring(1, 2).equals("1")) {
                        strTemp = "Tester/SQA";
                    }
                    if (strPosition.substring(2, 3).equals("1")) {
                        strTemp = "Project Leader";
                    }
                }
                svAssign.setCell(2, strTemp);
                svAssign.setCell(3, ("0".equals(rs.getString("Status"))) ? DMS.ACTIVE : DMS.INACTIVE);

                smResult.addRow(svAssign);
            }

            rs.close();
            prepStmt.close();

            beanAssignList.setAssignUserList(smResult);
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in userviews.AssignBO.getAssignList(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in userviews.AssignBO.getAssignList(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "AssignBO.getAssignList(): ");
        }

        return beanAssignList;
    }

    public int addAssignedUser(int nProjectID, int nDevID, String strPosition, String strStatus) throws SQLException, Exception {
        int nResult = -1;
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            String strTemp = "";
            if (strPosition.equals("012")) {
                strTemp = "1110000000";
            }
            else if (strPosition.equals("01")) {
                strTemp = "1100000000";
            }
            else if (strPosition.equals("02")) {
                strTemp = "0010000000";
            }
            else if (strPosition.equals("12")) {
                strTemp = "0110000000";
            }
            else if (strPosition.equals("0")) {
                strTemp = "1000000000";
            }
            else if (strPosition.equals("1")) {
                strTemp = "1100000000";
            }
            else if (strPosition.equals("2")) {
                strTemp = "1110000000";
            }

            strSQL.append("SELECT * FROM Defect_Permission");
            strSQL.append(" WHERE Project_ID = " + nProjectID);
            strSQL.append(" AND Developer_ID = " + nDevID);

            ds = conPool.getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                nResult = 1;
            }
            else {
                strSQL = new StringBuffer();
                strSQL.append("INSERT INTO Defect_Permission(Developer_ID, Position, Status, Project_ID)");
                strSQL.append(" VALUES(" + nDevID + ", '" + strTemp + "', '" + strStatus + "', " + nProjectID + ")");
                rs.close();
                prepStmt.close();
                prepStmt = con.prepareStatement(strSQL.toString());
                prepStmt.executeQuery();
            }

            con.commit();
        }
        catch (SQLException ex) {
            con.rollback();
            logger.error("SQLException occurs in AssignBO.addAssignedUser(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Exception occurs in AssignBO.addAssignedUser(): " + e.getMessage());
        }
        finally {
            conPool.releaseResourceAndCommit(con, prepStmt, rs,
                            "AssignBO.addAssignedUser(): ");
        }

        return nResult;
    }

    public int deleteAssignedUser(int nProjectID, String[] arrUserID) throws SQLException, Exception {
        int nResult = -1;
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            int nCount = arrUserID.length;
            String strIDList = "";
            for (int i = 0; i < nCount; i++) {
                strIDList += arrUserID[i] + ",";
            }
            if (strIDList.length() > 0) {
                strIDList = strIDList.substring(0, strIDList.length() - 1);
            }

            if (strIDList.length() > 0) {
                strSQL.append("DELETE FROM Defect_Permission");
                strSQL.append(" WHERE Developer_ID IN (" + strIDList + ")");
                strSQL.append(" AND Project_ID IN (" + nProjectID + ")");

                ds = conPool.getDS();
                con = ds.getConnection();
                con.setAutoCommit(false);
                prepStmt = con.prepareStatement(strSQL.toString());
                prepStmt.executeQuery();

                prepStmt.close();
                con.commit();
            }
        }
        catch (SQLException ex) {
            con.rollback();
            logger.error("SQLException occurs in AssignBO.deleteAssignedUser(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Exception occurs in AssignBO.deleteAssignedUser(): " + e.getMessage());
        }
        finally {
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "AssignBO.deleteAssignedUser(): ");
        }
        return nResult;
    }

    public int updateAssignedUser(int nProjectID, int nDevID, String strPosition, String strStatus) throws SQLException, Exception {
        int nResult = -1;
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        StringBuffer strSQL = new StringBuffer();
        try {
            String strTemp = "";
            if (strPosition.equals("012")) {
                strTemp = "1110000000";
            }
            else if (strPosition.equals("01")) {
                strTemp = "1100000000";
            }
            else if (strPosition.equals("02")) {
                strTemp = "0010000000";
            }
            else if (strPosition.equals("12")) {
                strTemp = "0110000000";
            }
            else if (strPosition.equals("0")) {
                strTemp = "1000000000";
            }
            else if (strPosition.equals("1")) {
                strTemp = "1100000000";
            }
            else if (strPosition.equals("2")) {
                strTemp = "1110000000";
            }

            strSQL.append("UPDATE Defect_Permission");
            strSQL.append(" SET Position = '" + strTemp + "', Status = '" + strStatus + "'");
            strSQL.append(" WHERE Developer_ID IN (" + nDevID + ")");
            strSQL.append(" AND Project_ID IN (" + nProjectID + ")");

            ds = conPool.getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);
            prepStmt = con.prepareStatement(strSQL.toString());
            prepStmt.executeQuery();
            prepStmt.close();
            con.commit();
        }
        catch (SQLException ex) {
            con.rollback();
            logger.error("SQLException occurs in AssignBO.updateAssignedUser(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Exception occurs in AssignBO.updateAssignedUser(): " + e.getMessage());
        }
        finally {
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "AssignBO.updateAssignedUser(): ");
        }

        return nResult;
    }
}