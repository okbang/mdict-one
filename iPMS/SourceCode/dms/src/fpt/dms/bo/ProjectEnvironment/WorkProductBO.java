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
 * Title:        WorkProductBO.java
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      FPT - FSOFT
 * Author:       Nguyen Thai Son
 * Create date:  October 30, 2002
 * Modified date:
 */

//imported from standard JAVA

import fpt.dms.bean.ProjectEnvironment.WorkProductListBean;
import fpt.dms.constant.DMS;
import fpt.dms.framework.connection.WSConnectionPooling;
import fpt.dms.framework.util.StringUtil.StringMatrix;
import fpt.dms.framework.util.StringUtil.StringVector;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;


public class WorkProductBO {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    //private Connection con = null;

    private static Logger logger = Logger.getLogger(WorkProductBO.class.getName());

    public WorkProductBO() {
    }

    public WorkProductListBean getWorkProductList(int nProjectID) throws SQLException, Exception {
        WorkProductListBean beanWorkProductList = new WorkProductListBean();
        StringMatrix smResult = new StringMatrix();
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            strSQL.append("SELECT WP_Size.WP_ID, WP_Size.WP_Size, WP_Size.Unit_ID, ");
            strSQL.append(" WorkProduct.Name AS WPName, AP_SizeUnit.Name As UnitName,");
            strSQL.append(" WP_Size.planned_defect AS PlannedDefect, WP_Size.replanned_defect AS ReplannedDefect");
            strSQL.append(" FROM WP_Size, WorkProduct, AP_SizeUnit");
            strSQL.append(" WHERE WP_Size.WP_ID = WorkProduct.WP_ID(+)");
            strSQL.append(" AND WP_Size.Unit_ID = AP_SizeUnit.Unit_ID(+)");
            strSQL.append(" AND WP_Size.Project_ID IN (" + nProjectID + ")");
            strSQL.append(" ORDER BY WorkProduct.Name");
            if (DMS.DEBUG)
                logger.info("getWorkProductList(): strSQL = " + strSQL.toString());

            StringVector svItem = new StringVector(6);

            ds = conPool.getDS();
            con = ds.getConnection();
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();

            while (rs.next()) {
                svItem.setCell(0, rs.getString("WP_ID"));
                svItem.setCell(1, rs.getString("WPName"));
                svItem.setCell(2, rs.getString("WP_Size"));
                svItem.setCell(3, rs.getString("UnitName"));
                svItem.setCell(4, (rs.getString("PlannedDefect") == null || rs.getInt("PlannedDefect") == 0) ? "" : rs.getString("PlannedDefect"));
                svItem.setCell(5, (rs.getString("ReplannedDefect") == null || rs.getInt("ReplannedDefect") == 0) ? "" : rs.getString("ReplannedDefect"));

                smResult.addRow(svItem);
            }
            if (DMS.DEBUG)
                logger.info("Work product size = " + smResult.getNumberOfRows());

            rs.close();
            prepStmt.close();

            beanWorkProductList.setWorkProductSizeList(smResult);
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in userviews.WorkProductBO.getWorkProductList(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in userviews.WorkProductBO.getWorkProductList(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "WorkProductBO.getWorkProductList(): ");
        }

        return beanWorkProductList;
    }

    public int addWorkProduct(int nProjectID, String strWPID, String strSize, String strUnit,
            String strPlannedDefect, String strReplannedDefect) throws SQLException, Exception {
        int nResult = -1;
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        PreparedStatement prepStmtCheck = null;
        ResultSet rsCheck = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            String strSQLCheck = "Select * from WP_Size Where Project_ID = " + nProjectID + " AND WP_ID = " + strWPID + "";

            ds = conPool.getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);

            prepStmtCheck = con.prepareStatement(strSQLCheck);
            rsCheck = prepStmtCheck.executeQuery();
            if (rsCheck.next()) {
                nResult = 1;
            }
            else {
                if ((strPlannedDefect != null && !"".equals(strPlannedDefect)) &&
                        (strReplannedDefect != null && !"".equals(strReplannedDefect))) {
                    strSQL.append("INSERT INTO WP_Size(Project_ID, WP_ID, WP_Size, Unit_ID, planned_defect, replanned_defect)");
                    strSQL.append(" VALUES(" + nProjectID + ", " + strWPID + ", " + strSize + ", " + strUnit + ", " + strPlannedDefect + ", " + strReplannedDefect + ")");
                }
                else if (strPlannedDefect != null && !"".equals(strPlannedDefect)) {
                    strSQL.append("INSERT INTO WP_Size(Project_ID, WP_ID, WP_Size, Unit_ID, planned_defect)");
                    strSQL.append(" VALUES(" + nProjectID + ", " + strWPID + ", " + strSize + ", " + strUnit + ", " + strPlannedDefect + ")");
                }
                else if (strReplannedDefect != null && !"".equals(strReplannedDefect)) {
                    strSQL.append("INSERT INTO WP_Size(Project_ID, WP_ID, WP_Size, Unit_ID, replanned_defect)");
                    strSQL.append(" VALUES(" + nProjectID + ", " + strWPID + ", " + strSize + ", " + strUnit + ", " + strReplannedDefect + ")");
                }
                else {
                    strSQL.append("INSERT INTO WP_Size(Project_ID, WP_ID, WP_Size, Unit_ID)");
                    strSQL.append(" VALUES(" + nProjectID + ", " + strWPID + ", " + strSize + ", " + strUnit + ")");
                }
                if (DMS.DEBUG)
                    logger.info("strSQL = " + strSQL.toString());
                prepStmt = con.prepareStatement(strSQL.toString());
                prepStmt.executeUpdate();
            }//end else

            rsCheck.close();
            prepStmtCheck.close();
            con.commit();
        }
        catch (SQLException ex) {
            con.rollback();
            logger.error("SQLException occurs in WorkProductBO.addWorkProduct(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Exception occurs in WorkProductBO.addWorkProduct(): " + e.getMessage());
        }
        finally {
            //Release prepare statement only
            conPool.releaseResource(null, prepStmt, null,
                            "WorkProductBO.addWorkProduct(): ");
            //Release connection, prepare statement, result set
            conPool.releaseResourceAndCommit(con, prepStmtCheck, rsCheck,
                            "WorkProductBO.addWorkProduct(): ");
        }

        return nResult;
    }

    public String deleteWorkProduct(int nProjectID, String[] arrWPID) throws SQLException, Exception {
        String strResult = "";
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        PreparedStatement prepStmtCheck = null;
        ResultSet rsCheck = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
//STEP 1 - Checking if wp_id existed in DEFECT and in MODULE or not
            StringBuffer strCheckSQL = new StringBuffer();
            strCheckSQL.append("SELECT distinct wp_id");
            strCheckSQL.append(" FROM defect");
            strCheckSQL.append(" WHERE project_id = " + nProjectID);

//Get WP IDs in module list
            strCheckSQL.append(" UNION ");
            strCheckSQL.append(" SELECT distinct wp_id");
            strCheckSQL.append(" FROM module");
            strCheckSQL.append(" WHERE project_id = " + nProjectID);
            if (DMS.DEBUG)
                logger.debug("Check SQL = " + strCheckSQL.toString());

            Vector vecExistedID = new Vector();

            ds = conPool.getDS();
            con = ds.getConnection();
            prepStmtCheck = con.prepareStatement(strCheckSQL.toString());
            rsCheck = prepStmtCheck.executeQuery();
            while (rsCheck.next()) {
                vecExistedID.add(rsCheck.getString("wp_id"));
            }
            rsCheck.close();
            prepStmtCheck.close();

            String[] arrExistedID = new String[vecExistedID.size()];
            vecExistedID.copyInto(arrExistedID);

            Vector vecNotExistedID = new Vector();
            Vector vecConflictID = new Vector();

            for (int j = 0; j < arrWPID.length; j++) {
                boolean bOK = false;
                for (int i = 0; i < arrExistedID.length; i++) {
                    if (arrWPID[j].equals(arrExistedID[i])) {
                        vecConflictID.add(arrWPID[j]);
                        bOK = true;
                        break;
                    }
                }
                if (!bOK) {
                    vecNotExistedID.add(arrWPID[j]);
                }
            }

            String strNotExistedID = "", strConflictID = "";
            for (int i = 0; i < vecNotExistedID.size(); i++) {
                strNotExistedID += vecNotExistedID.get(i) + ",";
            }
            if (strNotExistedID.length() > 0) {
                strNotExistedID = strNotExistedID.substring(0, strNotExistedID.length() - 1);
            }

//To re-direct message to client
            for (int i = 0; i < vecConflictID.size(); i++) {
                strConflictID += "#" + vecConflictID.get(i) + ",";
            }
            if (strConflictID.length() > 0) {
                strConflictID = strConflictID.substring(0, strConflictID.length() - 1);
                strResult = "ID " + strConflictID + " already existed in defect/module.";
            }
///////////////////////////////////////////////////////////////////////////////////////////////////////////

            if (strNotExistedID.length() > 0) {
                strSQL.append("DELETE FROM WP_Size");
                strSQL.append(" WHERE WP_ID IN (" + strNotExistedID + ")");
                strSQL.append(" AND Project_ID = " + nProjectID);

                con.setAutoCommit(false);
                prepStmt = con.prepareStatement(strSQL.toString());
                prepStmt.executeQuery();

                prepStmt.close();
                con.commit();
            }
        }
        catch (SQLException ex) {
            con.rollback();
            logger.error("SQLException occurs in WorkProductBO.deleteWorkProduct(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Exception occurs in WorkProductBO.deleteWorkProduct(): " + e.getMessage());
        }
        finally {
            //Release prepare statement only
            conPool.releaseResource(null, prepStmt, null,
                            "WorkProductBO.deleteWorkProduct(): ");
            //Release connection, prepare statement, result set
            conPool.releaseResourceAndCommit(con, prepStmtCheck, rsCheck,
                            "WorkProductBO.deleteWorkProduct(): ");
        }
        return strResult;
    }

    public String updateWorkProduct(int nProjectID, StringMatrix smWP) throws SQLException, Exception {
        String strResult = "";
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        StringBuffer strSQL = new StringBuffer();
        try {
            String strID = smWP.getCell(0, 0);
            String strSize = smWP.getCell(0, 2);
            String strUnit = smWP.getCell(0, 3);
            String strPlannedDefect = smWP.getCell(0, 4);
            String strReplannedDefect = smWP.getCell(0, 5);

            strResult = validatePlannedDefectByQC(nProjectID, strID, strPlannedDefect, strReplannedDefect);
            if (!"".equals(strResult)) {
                return strResult;
            }

            strSQL.append("UPDATE WP_Size");
            strSQL.append(" SET WP_Size = " + strSize);
            strSQL.append(", Unit_ID = " + strUnit);
            if (strPlannedDefect != null && !"".equals(strPlannedDefect)) {
                strSQL.append(", planned_defect = " + strPlannedDefect);
            }
            if (strReplannedDefect != null && !"".equals(strReplannedDefect)) {
                strSQL.append(", replanned_defect = " + strReplannedDefect);
            }
            else {
                strSQL.append(", replanned_defect = 0");
            }
            strSQL.append(" WHERE WP_ID IN (" + strID + ") AND Project_ID IN (" + nProjectID + ")");
            if (DMS.DEBUG)
                logger.debug("strSQL = " + strSQL.toString());

            ds = conPool.getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);
            prepStmt = con.prepareStatement(strSQL.toString());
            prepStmt.executeQuery();
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in WorkProductBO.updateWorkProduct(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in WorkProductBO.updateWorkProduct(): " + e.getMessage());
        }
        finally {
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "WorkProductBO.updateWorkProduct(): ");
        }

        return strResult;
    }

    private String validatePlannedDefectByQC(int nProjectID, String strWPID,
            String strWPPlannedDefect, String strWPReplannedDefect) throws Exception {
        String strResult = "";
        DataSource ds = null;
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            strSQL.append("SELECT SUM(value) as TotalByWP FROM defect_plan");
            strSQL.append(" WHERE project_id = " + nProjectID);
            strSQL.append(" AND wp_id = " + strWPID);

            int nTotalByWP = 0;

            ds = conPool.getDS();
            con = ds.getConnection();
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                String strTotal = (rs.getString("TotalByWP") != null) ? rs.getString("TotalByWP") : "";
                nTotalByWP = (!"".equals(strTotal)) ? Integer.parseInt(strTotal) : 0;
            }

            int nTotalPlannedDefect = (!"".equals(strWPPlannedDefect)) ? Integer.parseInt(strWPPlannedDefect) : 0;
            //int nTotalReplannedDefect = (!"".equals(strWPReplannedDefect)) ? Integer.parseInt(strWPReplannedDefect) : 0;

            if (nTotalByWP > 0 && nTotalPlannedDefect != nTotalByWP) {
                //Invalid planned defect of module
                strResult = DMS.MSG_INVALID_WP_PLANNED;
                strResult += " It should be equal to " + nTotalByWP + " (the total by QC activities)";
            }
        }
        catch (NumberFormatException ex) {
            logger.debug("NumberFormatException occurs in WorkProductBO.validatePlannedDefectByQC(): " + ex.getMessage());
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in WorkProductBO.validatePlannedDefectByQC(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in WorkProductBO.validatePlannedDefectByQC(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "WorkProductBO.validatePlannedDefectByQC(): ");
        }

        return strResult;
    }
}