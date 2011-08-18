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
 * Title:        ModuleBO.java
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      FPT - FSOFT
 * Author:       Nguyen Thai Son
 * Create date:  October 30, 2002
 * Modified date:
 */

import fpt.dms.bean.ProjectEnvironment.ModuleListBean;
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
import java.util.Vector;


public class ModuleBO {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    private DataSource ds = null;
    //private Connection con = null;

    private static Logger logger = Logger.getLogger(ModuleBO.class.getName());

    public ModuleBO() {
    }

    public ModuleListBean getModuleList(int nProjectID) throws SQLException, Exception {
        ModuleListBean beanModuleList = new ModuleListBean();
        StringMatrix smResult = new StringMatrix();
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            strSQL.append("SELECT M.module_id as ID, M.name as MODULENAME, WP.wp_id as WPID, WP.name as WPNAME, planned_defect, replanned_defect");
            strSQL.append(" FROM Module M, WorkProduct WP");
            strSQL.append(" WHERE M.project_id = " + nProjectID);
            strSQL.append(" AND M.wp_id = WP.wp_id");
            strSQL.append(" ORDER BY MODULENAME, WPNAME");
            if (DMS.DEBUG)
                logger.info("getModuleList(): strSQL = " + strSQL.toString());

            StringVector svItem = new StringVector(6);

            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();

            while (rs.next()) {
                svItem.setCell(0, rs.getString("ID"));
                svItem.setCell(1, CommonUtil.correctHTMLError(rs.getString("MODULENAME")));
                svItem.setCell(2, (rs.getString("WPNAME") == null) ? "" : rs.getString("WPNAME"));
                svItem.setCell(3, (rs.getString("planned_defect") == null || rs.getInt("planned_defect") == 0) ? "" : rs.getString("planned_defect"));
                svItem.setCell(4, (rs.getString("replanned_defect") == null || rs.getInt("replanned_defect") == 0) ? "" : rs.getString("replanned_defect"));
                svItem.setCell(5, rs.getString("WPID"));

                smResult.addRow(svItem);
            }
            if (DMS.DEBUG)
                logger.info("Work product size = " + smResult.getNumberOfRows());

            rs.close();
            prepStmt.close();

            beanModuleList.setSetupModuleList(smResult);
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in ModuleBO.getModuleList(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in ModuleBO.getModuleList(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ModuleBO.getModuleList(): ");
        }

        return beanModuleList;
    }

    public String addModule(int nProjectID, StringVector vecModule) throws SQLException, Exception {
        String strResult = "";
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            //STEP 1 - Get values from client
            //String strID = vecModule.getCell(0);
            String strName = CommonUtil.stringConvert(vecModule.getCell(1));
            String strWPID = vecModule.getCell(2);
            String strPlannedDefect = vecModule.getCell(3);
            String strReplannedDefect = vecModule.getCell(4);

            //Planned and replanned defect of work product
            int nWPPlannedDefect = Integer.parseInt(vecModule.getCell(5));
            int nWPReplannedDefect = Integer.parseInt(vecModule.getCell(6));
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            //STEP 2 - Check if insert or not
            strResult = validateModuleName(nProjectID, strName, strWPID);
            if (!"".equals(strResult)) {
                return strResult;
            }

            strResult = validatePlannedDefect(nProjectID, strWPID, strPlannedDefect, strReplannedDefect, nWPPlannedDefect, nWPReplannedDefect);
            if (!"".equals(strResult)) {
                return strResult;
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //STEP 3 - Insert new module
            
            // 2004-Sep-30: set default values for
            //      planned_size=0,
            //      planned_size_unit_id=3,
            //      planned_size_type=1
            if ((strPlannedDefect != null && !"".equals(strPlannedDefect)) &&
                    (strReplannedDefect != null && !"".equals(strReplannedDefect))) {
                strSQL.append("INSERT INTO Module(Name, Project_ID, wp_id, planned_defect, replanned_defect,planned_size,planned_size_unit_id,planned_size_type)");
                strSQL.append(" VALUES('" + strName + "', " + nProjectID + ", " + strWPID + ", " + strPlannedDefect + ", " + strReplannedDefect + ",0,3,1)");
            }
            else if (strPlannedDefect != null && !"".equals(strPlannedDefect)) {
                strSQL.append("INSERT INTO Module(Name, Project_ID, wp_id, planned_defect,planned_size,planned_size_unit_id,planned_size_type)");
                strSQL.append(" VALUES('" + strName + "', " + nProjectID + ", " + strWPID + ", " + strPlannedDefect + ",0,3,1)");
            }
            else if (strReplannedDefect != null && !"".equals(strReplannedDefect)) {
                strSQL.append("INSERT INTO Module(Name, Project_ID, wp_id, replanned_defect,planned_size,planned_size_unit_id,planned_size_type)");
                strSQL.append(" VALUES('" + strName + "', " + nProjectID + ", " + strWPID + ", " + strReplannedDefect + ",0,3,1)");
            }
            else {
                strSQL.append("INSERT INTO Module(Name, Project_ID, wp_id,planned_size,planned_size_unit_id,planned_size_type)");
                strSQL.append(" VALUES('" + strName + "', " + nProjectID + ", " + strWPID + ",0,3,1)");
            }
            if (DMS.DEBUG)
                logger.info("strSQL = " + strSQL.toString());

            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            con.setAutoCommit(false);

            prepStmt = con.prepareStatement(strSQL.toString());
            prepStmt.executeUpdate();

            prepStmt.close();
            con.commit();
        }
        catch (NumberFormatException ex) {
            logger.error("NumberFormatException occurs in ModuleBO.addModule(): " + ex.getMessage());
        }
        catch (SQLException ex) {
            con.rollback();
            logger.error("SQLException occurs in ModuleBO.addModule(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Exception occurs in ModuleBO.addModule(): " + e.getMessage());
        }
        finally {
            conPool.releaseResourceAndCommit(con, prepStmt, rs,
                            "ModuleBO.addModule(): ");
        }

        return strResult;
    }

    public String deleteModule(int nProjectID, String[] arrModuleID) throws SQLException, Exception {
        String strResult = "";
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rsCheck = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
//STEP 1 - Checking if module_id existed in DEFECT or not
            StringBuffer strCheckSQL = new StringBuffer();
            strCheckSQL.append("SELECT distinct module_id");
            strCheckSQL.append(" FROM defect");
            strCheckSQL.append(" WHERE project_id = " + nProjectID);

            Vector vecExistedModuleID = new Vector();

            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            con.setAutoCommit(false);
            prepStmt = con.prepareStatement(strCheckSQL.toString());
            rsCheck = prepStmt.executeQuery();
            while (rsCheck.next()) {
                vecExistedModuleID.add(rsCheck.getString("module_id"));
            }
            rsCheck.close();
            prepStmt.close();
            String[] arrExistedModuleID = new String[vecExistedModuleID.size()];
            vecExistedModuleID.copyInto(arrExistedModuleID);

            Vector vecNotExistedModuleID = new Vector();
            Vector vecConflictModuleID = new Vector();

            for (int j = 0; j < arrModuleID.length; j++) {
                boolean bOK = false;
                for (int i = 0; i < arrExistedModuleID.length; i++) {
                    if (arrModuleID[j].equals(arrExistedModuleID[i])) {
                        vecConflictModuleID.add(arrModuleID[j]);
                        bOK = true;
                        break;
                    }
                }
                if (!bOK) {
                    vecNotExistedModuleID.add(arrModuleID[j]);
                }
            }

            String strNotExistedID = "", strConflictID = "";
            for (int i = 0; i < vecNotExistedModuleID.size(); i++) {
                strNotExistedID += vecNotExistedModuleID.get(i) + ",";
            }
            if (strNotExistedID.length() > 0) {
                strNotExistedID = strNotExistedID.substring(0, strNotExistedID.length() - 1);
            }

//To re-direct message to client
            for (int i = 0; i < vecConflictModuleID.size(); i++) {
                strConflictID += "#" + vecConflictModuleID.get(i) + ",";
            }
            if (strConflictID.length() > 0) {
                strConflictID = strConflictID.substring(0, strConflictID.length() - 1);
                strResult = "ID " + strConflictID + " already existed in defect.";
            }
///////////////////////////////////////////////////////////////////////////////////////////////////////////

            if (strNotExistedID.length() > 0) {
                String selectStatement = "DELETE FROM Module WHERE Module_ID IN (" + strNotExistedID + ") AND Project_ID IN (" + nProjectID + ")";
                if (DMS.DEBUG)
                    logger.info("Delete SQL:" + selectStatement);
                prepStmt = con.prepareStatement(selectStatement);
                prepStmt.executeQuery();
                prepStmt.close();
                con.commit();
            }
        }
        catch (SQLException ex) {
            con.rollback();
            logger.error("SQLException occurs in ModuleBO.deleteModule(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Exception occurs in ModuleBO.deleteModule(): " + e.getMessage());
        }
        finally {
            conPool.releaseResourceAndCommit(con, prepStmt, rsCheck,
                            "ModuleBO.deleteModule(): ");
        }
        return strResult;
    }

    public String updateModule(int nProjectID, StringMatrix smModule) throws SQLException, Exception {
        String strResult = "";
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();
        try {
            String strID = smModule.getCell(0, 0);
            String strName = CommonUtil.stringConvert(smModule.getCell(0, 1));
            String strWPID = smModule.getCell(0, 2);
            String strPlannedDefect = smModule.getCell(0, 3);
            String strReplannedDefect = smModule.getCell(0, 4);
//Planned and replanned defect of work product
            int nWPPlannedDefect = Integer.parseInt(smModule.getCell(0, 5));
            int nWPReplannedDefect = Integer.parseInt(smModule.getCell(0, 6));

            ////////////////////////////////////////////////////////////////////////////////////////////////////////
            //STEP 2 - Check constraint between planned defect value and total of qc_activity values.
            strResult = validatePlannedDefect(nProjectID, strWPID, strPlannedDefect, strReplannedDefect, nWPPlannedDefect, nWPReplannedDefect);
            if (!"".equals(strResult)) {
                return strResult;
            }

            strResult = validatePlannedDefectByQC(nProjectID, strWPID, strID, strPlannedDefect, strReplannedDefect);
            if (!"".equals(strResult)) {
                return strResult;
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////
            //STEP 3 - Update database
            strSQL.append("UPDATE Module SET Name = '" + strName + "'");
            strSQL.append(", wp_id = " + strWPID);
            if (strPlannedDefect != null && !"".equals(strPlannedDefect)) {
                strSQL.append(", planned_defect = " + strPlannedDefect);
            }
            if (strReplannedDefect != null && !"".equals(strReplannedDefect)) {
                strSQL.append(", replanned_defect = " + strReplannedDefect);
            }
            else {
                strSQL.append(", replanned_defect = 0");
            }
            strSQL.append(" WHERE Module_ID IN (" + strID + ")");
            strSQL.append(" AND Project_ID IN (" + nProjectID + ")");
            if (DMS.DEBUG)
                logger.debug("strSQL = " + strSQL.toString());

            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            con.setAutoCommit(false);

            prepStmt = con.prepareStatement(strSQL.toString());
            prepStmt.executeQuery();
            prepStmt.close();
            con.commit();
        }
        catch (NumberFormatException ex) {
            logger.debug("NumberFormatException occurs in ModuleBO.updateModule(): " + ex.getMessage());
        }
        catch (SQLException ex) {
            con.rollback();
            logger.error("SQLException occurs in ModuleBO.updateModule(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Exception occurs in ModuleBO.updateModule(): " + e.getMessage());
        }
        finally {
            conPool.releaseResourceAndCommit(con, prepStmt, rs,
                            "ModuleBO.updateModule(): ");
        }

        return strResult;
    }

    private String validateModuleName(int nProjectID, String strName, String strWPID) {
        String strResult = "";
        Connection con = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String strSQL = "SELECT module_id FROM module"
                + " WHERE project_id=" + nProjectID
                + " AND wp_id=" + strWPID
                + " AND name='" + strName + "'";
        try {
            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            preStmt = con.prepareStatement(strSQL);
            rs = preStmt.executeQuery();
            if (rs.next()) {
                strResult = "Module name already exists.";
            }
        }
        catch (Exception e) {
        }
        finally {
            conPool.releaseResource(con, preStmt, rs,
                            "ModuleBO.validateModuleName(): ");
            return strResult;
        }
    }

    private String validatePlannedDefect(int nProjectID, String strWPID,
            String strModulePlannedDefect, String strModuleReplannedDefect,
            int nWPPlannedDefect, int nWPReplannedDefect) throws Exception {
        String strResult = "";

        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            strSQL.append("SELECT SUM(planned_defect) as PlannedDefect, SUM(replanned_defect) as ReplannedDefect");
            strSQL.append(" FROM module");
            strSQL.append(" WHERE project_id = " + nProjectID);
            strSQL.append(" AND wp_id = " + strWPID);

            int nTotalPlannedDefect = 0;
            int nTotalReplannedDefect = 0;

            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                String strTotalPlanned = (rs.getString("PlannedDefect") != null) ? rs.getString("PlannedDefect") : "";
                nTotalPlannedDefect = (!"".equals(strTotalPlanned)) ? Integer.parseInt(strTotalPlanned) : 0;

                String strTotalReplanned = (rs.getString("ReplannedDefect") != null) ? rs.getString("ReplannedDefect") : "";
                nTotalReplannedDefect = (!"".equals(strTotalReplanned)) ? Integer.parseInt(strTotalReplanned) : 0;
            }

            nTotalPlannedDefect += (!"".equals(strModulePlannedDefect)) ? Integer.parseInt(strModulePlannedDefect) : 0;
            nTotalReplannedDefect += (!"".equals(strModuleReplannedDefect)) ? Integer.parseInt(strModuleReplannedDefect) : 0;

            if (nTotalPlannedDefect > nWPPlannedDefect) {
                //Invalid planned defect of new module
                strResult = DMS.MSG_INVALID_MODULE_PLANNED;
                strResult += " It should be lower than " + nWPPlannedDefect + " (the total in work product)";
            }
            else if (nTotalReplannedDefect > nWPReplannedDefect && nWPReplannedDefect > 0) {
                //Invalid re-planned defect of new module
                strResult = DMS.MSG_INVALID_MODULE_REPLANNED;
                strResult += " It should be lower than " + nWPReplannedDefect + " (the total in work product)";
            }
        }
        catch (NumberFormatException ex) {
            logger.error("NumberFormatException occurs in ModuleBO.validatePlannedDefect(): " + ex.getMessage());
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in ModuleBO.validatePlannedDefect(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in ModuleBO.validatePlannedDefect(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ModuleBO.validatePlannedDefect(): ");
        }

        return strResult;
    }

    private String validatePlannedDefectByQC(int nProjectID, String strWPID, String strModuleID,
            String strModulePlannedDefect, String strModuleReplannedDefect) throws Exception {
        String strResult = "";

        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            strSQL.append("SELECT SUM(value) as TotalByModule FROM defect_plan");
            strSQL.append(" WHERE project_id = " + nProjectID);
            strSQL.append(" AND wp_id = " + strWPID);
            strSQL.append(" AND module_id = " + strModuleID);

            int nTotalByModule = 0;

            if (ds == null) {
                ds = conPool.getDS();
            }
            con = ds.getConnection();
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                String strTotal = (rs.getString("TotalByModule") != null) ? rs.getString("TotalByModule") : "";
                nTotalByModule = (!"".equals(strTotal)) ? Integer.parseInt(strTotal) : 0;
            }

            int nTotalPlannedDefect = (!"".equals(strModulePlannedDefect)) ? Integer.parseInt(strModulePlannedDefect) : 0;
            //int nTotalReplannedDefect = (!"".equals(strModuleReplannedDefect)) ? Integer.parseInt(strModuleReplannedDefect) : 0;

            if (nTotalByModule > 0 && nTotalPlannedDefect != nTotalByModule) {
                //Invalid planned defect of module
                strResult = DMS.MSG_INVALID_MODULE_PLANNED;
                strResult += " It should be equal to " + nTotalByModule + " (the total by QC activities)";
            }
        }
        catch (NumberFormatException ex) {
            logger.debug("NumberFormatException occurs in ModuleBO.validatePlannedDefectByQC(): " + ex.getMessage());
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in ModuleBO.validatePlannedDefectByQC(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in ModuleBO.validatePlannedDefectByQC(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ModuleBO.validatePlannedDefectByQC(): ");
        }

        return strResult;
    }
}