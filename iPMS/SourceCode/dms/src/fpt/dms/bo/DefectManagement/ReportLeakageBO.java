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
 
 
package fpt.dms.bo.DefectManagement;

/**
 * Title:        ReportLeakageBO.java
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      FPT - FSOFT
 * Author:       Nguyen Thai Son
 * Create date:  October 28, 2002
 * Modified date:
 */

//imported from standard JAVA

import fpt.dms.bean.DefectManagement.ReportWeeklyBean;
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


public class ReportLeakageBO {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    //private Connection con = null;

    private static Logger logger = Logger.getLogger(QueryBO.class.getName());

    public ReportLeakageBO() {

    }

    public ReportWeeklyBean getLeakageReport(int nProjectID, String strFromDate, String strToDate, int nReport) throws SQLException, Exception {
        ReportWeeklyBean beanReport = new ReportWeeklyBean();
        StringMatrix smResult = new StringMatrix();
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            String strFilter = "", strForFixed = "";
            String strFromCreateDate = "", strFromCloseDate = "", strToCreateDate = "", strToCloseDate = "";
            //String strExclude = " AND ds_id < 5 "; // exclude defect with status Accepted or Cancelled
            String strExclude = " AND ds_id <> 6 "; // exclude defect with status Cancelled

            if (!"".equals(strFromDate)) {
                strFilter += " AND create_date >= TO_DATE('" + strFromDate + "', '" + DMS.DATE_FORMAT + "')";
                strForFixed += " AND close_date >= TO_DATE('" + strFromDate + "', '" + DMS.DATE_FORMAT + "')";
                strFromCreateDate += " create_date >= TO_DATE('" + strFromDate + "', '" + DMS.DATE_FORMAT + "')";
                strFromCloseDate += " close_date >= TO_DATE('" + strFromDate + "', '" + DMS.DATE_FORMAT + "')";
            }
            if (!"".equals(strToDate)) {
                strFilter += " AND create_date <= TO_DATE('" + strToDate + "', '" + DMS.DATE_FORMAT + "')";
                strForFixed += " AND close_date <= TO_DATE('" + strToDate + "', '" + DMS.DATE_FORMAT + "')";
                strToCreateDate += " create_date <= TO_DATE('" + strToDate + "', '" + DMS.DATE_FORMAT + "')";
                strToCloseDate += " close_date <= TO_DATE('" + strToDate + "', '" + DMS.DATE_FORMAT + "')";
            }

            /**************************************************************************************/
            //Select WORK PRODUCT
            StringBuffer strSQLForWP = new StringBuffer();
            strSQLForWP.append(" SELECT WPName, ModuleName, ");
            strSQLForWP.append(" Fatal_Total, Serious_Total, Medium_Total, Cosmetic_Total,");
            strSQLForWP.append(" Fatal_Fixed, Serious_Fixed, Medium_Fixed, Cosmetic_Fixed");
            strSQLForWP.append(" FROM ");

            //WORKPRODUCT NAME
            strSQLForWP.append(" (SELECT Defect.wp_id, WP.Name as WPName, 'ZZZ' as ModuleName");
            strSQLForWP.append(" FROM Defect, WorkProduct WP");
            strSQLForWP.append(" WHERE project_id = " + nProjectID + " AND Defect.wp_id = WP.wp_id ");
            strSQLForWP.append(" AND Defect.module_id = 0");

            if (!strFromDate.equals("") && !strToDate.equals("")) {
                strSQLForWP.append(" AND ((" + strFromCreateDate + " AND " + strToCreateDate + ")");
                strSQLForWP.append("     OR (" + strFromCloseDate + " AND " + strToCloseDate + "))");
            }
            else if (!strFromDate.equals("")) {
                strSQLForWP.append(" AND ((" + strFromCreateDate + ")");
                strSQLForWP.append("     OR (" + strFromCloseDate + "))");
            }
            else if (!strToDate.equals("")) {
                strSQLForWP.append(" AND ((" + strToCreateDate + ")");
                strSQLForWP.append("     OR (" + strToCloseDate + "))");
            }
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY Defect.wp_id, WP.Name)  T0, ");

            //FATAL TOTAL
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Fatal_Total ");
            strSQLForWP.append(" FROM Defect  WHERE defs_id = 1 AND");
            strSQLForWP.append(" project_id = " + nProjectID + " AND qa_id IN (13, 22, 15) ");
            strSQLForWP.append(" AND Defect.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T1_Total,");

            //SERIOUS TOTAL
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Serious_Total ");
            strSQLForWP.append(" FROM Defect  WHERE defs_id = 2 AND");
            strSQLForWP.append(" project_id = " + nProjectID + " AND qa_id IN (13, 22, 15) ");
            strSQLForWP.append(" AND Defect.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T2_Total,");

            //MEDIUM TOTAL
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Medium_Total");
            strSQLForWP.append(" FROM Defect  WHERE defs_id = 3 AND");
            strSQLForWP.append(" project_id = " + nProjectID + " AND qa_id IN (13, 22, 15) ");
            strSQLForWP.append(" AND Defect.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T3_Total,");

            //COSMETIC TOTAL
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Cosmetic_Total");
            strSQLForWP.append(" FROM Defect  WHERE defs_id = 4 AND");
            strSQLForWP.append(" project_id = " + nProjectID + " AND qa_id IN (13, 22, 15) ");
            strSQLForWP.append(" AND Defect.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T4_Total,");

            //FATAL FIXED
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Fatal_Fixed ");
            strSQLForWP.append(" FROM Defect  WHERE defs_id = 1 AND");
            strSQLForWP.append(" project_id = " + nProjectID + " AND qa_id IN (13,15,22) AND ds_id IN (4, 5)");
            strSQLForWP.append(" AND Defect.module_id = 0");
            strSQLForWP.append(strForFixed);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T1_Fixed,");

            //SERIOUS FIXED
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Serious_Fixed ");
            strSQLForWP.append(" FROM Defect  WHERE defs_id = 2 AND");
            strSQLForWP.append(" project_id = " + nProjectID + " AND qa_id IN (13,15,22) AND ds_id IN (4, 5)");
            strSQLForWP.append(" AND Defect.module_id = 0");
            strSQLForWP.append(strForFixed);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T2_Fixed,");

            //MEDIUM FIXED
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Medium_Fixed ");
            strSQLForWP.append(" FROM Defect  WHERE defs_id = 3 AND");
            strSQLForWP.append(" project_id = " + nProjectID + " AND qa_id IN (13,15,22) AND ds_id IN (4, 5)");
            strSQLForWP.append(" AND Defect.module_id = 0");
            strSQLForWP.append(strForFixed);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T3_Fixed,");

            //COSMETIC FIXED
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Cosmetic_Fixed");
            strSQLForWP.append(" FROM Defect  WHERE defs_id = 4 AND");
            strSQLForWP.append(" project_id = " + nProjectID + " AND qa_id IN (13,15,22) AND ds_id IN (4, 5)");
            strSQLForWP.append(" AND Defect.module_id = 0");
            strSQLForWP.append(strForFixed);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T4_Fixed");

            //JOIN TOGETHER
            strSQLForWP.append(" WHERE ");
            strSQLForWP.append(" T0.wp_id = T1_Total.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T2_Total.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T3_Total.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T4_Total.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T1_Fixed.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T2_Fixed.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T3_Fixed.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T4_Fixed.wp_id(+)");

            /**************************************************************************************/
            //Select MODULE
            StringBuffer strSQLForModule = new StringBuffer();
            strSQLForModule.append(" SELECT WPName, ModuleName, ");
            strSQLForModule.append(" Fatal_Total, Serious_Total, Medium_Total, Cosmetic_Total,");
            strSQLForModule.append(" Fatal_Fixed, Serious_Fixed, Medium_Fixed, Cosmetic_Fixed");
            strSQLForModule.append(" FROM ");

            //MODULE NAME
            strSQLForModule.append(" (SELECT Defect.module_id, WP.name as WPName, M.Name as ModuleName");
            strSQLForModule.append(" FROM Defect, Module M, WorkProduct WP");
            strSQLForModule.append(" WHERE Defect.project_id = " + nProjectID);
            strSQLForModule.append(" AND Defect.project_id = M.project_id");
            strSQLForModule.append(" AND Defect.wp_id = WP.wp_id");
            strSQLForModule.append(" AND Defect.module_id = M.module_id ");
            strSQLForModule.append(" AND Defect.module_id > 0");

            if (!strFromDate.equals("") && !strToDate.equals("")) {
                strSQLForModule.append(" AND ((" + strFromCreateDate + " AND " + strToCreateDate + ")");
                strSQLForModule.append("     OR (" + strFromCloseDate + " AND " + strToCloseDate + "))");
            }
            else if (!strFromDate.equals("")) {
                strSQLForModule.append(" AND ((" + strFromCreateDate + ")");
                strSQLForModule.append("     OR (" + strFromCloseDate + "))");
            }
            else if (!strToDate.equals("")) {
                strSQLForModule.append(" AND ((" + strToCreateDate + ")");
                strSQLForModule.append("     OR (" + strToCloseDate + "))");
            }

            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY WP.name, Defect.module_id, M.Name)  T0, ");

            //FATAL TOTAL
            strSQLForModule.append(" (SELECT module_id, Count(module_id) AS Fatal_Total ");
            strSQLForModule.append(" FROM Defect  WHERE defs_id = 1 AND");
            strSQLForModule.append(" project_id = " + nProjectID + " AND qa_id IN (13, 22, 15) ");
            strSQLForModule.append(" AND Defect.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id) T1_Total,");

            //SERIOUS TOTAL
            strSQLForModule.append(" (SELECT module_id, Count(module_id) AS Serious_Total ");
            strSQLForModule.append(" FROM Defect  WHERE defs_id = 2 AND");
            strSQLForModule.append(" project_id = " + nProjectID + " AND qa_id IN (13, 22, 15) ");
            strSQLForModule.append(" AND Defect.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id) T2_Total,");

            //MEDIUM TOTAL
            strSQLForModule.append(" (SELECT module_id, Count(module_id) AS Medium_Total");
            strSQLForModule.append(" FROM Defect  WHERE defs_id = 3 AND");
            strSQLForModule.append(" project_id = " + nProjectID + " AND qa_id IN (13, 22, 15) ");
            strSQLForModule.append(" AND Defect.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id) T3_Total,");

            //COSMETIC TOTAL
            strSQLForModule.append(" (SELECT module_id, Count(module_id) AS Cosmetic_Total");
            strSQLForModule.append(" FROM Defect  WHERE defs_id = 4 AND");
            strSQLForModule.append(" project_id = " + nProjectID + " AND qa_id IN (13, 22, 15) ");
            strSQLForModule.append(" AND Defect.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id) T4_Total,");

            //FATAL FIXED
            strSQLForModule.append(" (SELECT module_id, Count(module_id) AS Fatal_Fixed ");
            strSQLForModule.append(" FROM Defect  WHERE defs_id = 1 AND");
            strSQLForModule.append(" project_id = " + nProjectID + " AND qa_id IN (13,15,22) AND ds_id IN (4, 5)");
            strSQLForModule.append(" AND Defect.module_id > 0");
            strSQLForModule.append(strForFixed);
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id) T1_Fixed,");

            //SERIOUS FIXED
            strSQLForModule.append(" (SELECT module_id, Count(module_id) AS Serious_Fixed ");
            strSQLForModule.append(" FROM Defect  WHERE defs_id = 2 AND");
            strSQLForModule.append(" project_id = " + nProjectID + " AND qa_id IN (13,15,22) AND ds_id IN (4, 5)");
            strSQLForModule.append(" AND Defect.module_id > 0");
            strSQLForModule.append(strForFixed);
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id) T2_Fixed,");

            //MEDIUM FIXED
            strSQLForModule.append(" (SELECT module_id, Count(module_id) AS Medium_Fixed ");
            strSQLForModule.append(" FROM Defect  WHERE defs_id = 3 AND");
            strSQLForModule.append(" project_id = " + nProjectID + " AND qa_id IN (13,15,22) AND ds_id IN (4, 5)");
            strSQLForModule.append(" AND Defect.module_id > 0");
            strSQLForModule.append(strForFixed);
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id) T3_Fixed,");

            //COSMETIC FIXED
            strSQLForModule.append(" (SELECT module_id, Count(module_id) AS Cosmetic_Fixed");
            strSQLForModule.append(" FROM Defect  WHERE defs_id = 4 AND");
            strSQLForModule.append(" project_id = " + nProjectID + " AND qa_id IN (13,15,22) AND ds_id IN (4, 5)");
            strSQLForModule.append(" AND Defect.module_id > 0");
            strSQLForModule.append(strForFixed);
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id) T4_Fixed");

            //JOIN TOGETHER
            strSQLForModule.append(" WHERE ");
            strSQLForModule.append(" T0.module_id = T1_Total.module_id(+)");
            strSQLForModule.append(" AND T0.module_id = T2_Total.module_id(+)");
            strSQLForModule.append(" AND T0.module_id = T3_Total.module_id(+)");
            strSQLForModule.append(" AND T0.module_id = T4_Total.module_id(+)");
            strSQLForModule.append(" AND T0.module_id = T1_Fixed.module_id(+)");
            strSQLForModule.append(" AND T0.module_id = T2_Fixed.module_id(+)");
            strSQLForModule.append(" AND T0.module_id = T3_Fixed.module_id(+)");
            strSQLForModule.append(" AND T0.module_id = T4_Fixed.module_id(+)");

            /*********************************************************************************/
            strSQL.append(strSQLForWP + " UNION " + strSQLForModule + " ORDER BY WPName, ModuleName");
            if (DMS.DEBUG)
                logger.info("LeakageSQL = " + strSQL.toString());

            ds = conPool.getDS();
            con = ds.getConnection();

            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();

            StringVector svTemp = new StringVector(15);
            svTemp.setCell(0, strFromDate);
            svTemp.setCell(1, strToDate);
            svTemp.setCell(2, Integer.toString(nReport));
            smResult.addRow(svTemp);

            while (rs.next()) {
                svTemp.setCell(3, String.valueOf(rs.getString("WPName")));
                svTemp.setCell(4, ("ZZZ".equals(rs.getString("ModuleName"))) ? "" : rs.getString("ModuleName"));

                int nFatal = (rs.getString("Fatal_Total") != null) ? rs.getInt("Fatal_Total") : 0;
                svTemp.setCell(5, (nFatal == 0) ? "" : Integer.toString(nFatal));

                int nSerious = (rs.getString("Serious_Total") != null) ? rs.getInt("Serious_Total") : 0;
                svTemp.setCell(6, (nSerious == 0) ? "" : Integer.toString(nSerious));

                int nMedium = (rs.getString("Medium_Total") != null) ? rs.getInt("Medium_Total") : 0;
                svTemp.setCell(7, (nMedium == 0) ? "" : Integer.toString(nMedium));

                int nCosmetic = (rs.getString("Cosmetic_Total") != null) ? rs.getInt("Cosmetic_Total") : 0;
                svTemp.setCell(8, (nCosmetic == 0) ? "" : Integer.toString(nCosmetic));

                int nWeighted = nFatal * 10 + nSerious * 5 + nMedium * 3 + nCosmetic;
                svTemp.setCell(13, (nWeighted == 0) ? "" : Integer.toString(nWeighted));

                nFatal = (rs.getString("Fatal_Fixed") != null) ? rs.getInt("Fatal_Fixed") : 0;
                svTemp.setCell(9, (nFatal == 0) ? "" : Integer.toString(nFatal));

                nSerious = (rs.getString("Serious_Fixed") != null) ? rs.getInt("Serious_Fixed") : 0;
                svTemp.setCell(10, (nSerious == 0) ? "" : Integer.toString(nSerious));

                nMedium = (rs.getString("Medium_Fixed") != null) ? rs.getInt("Medium_Fixed") : 0;
                svTemp.setCell(11, (nMedium == 0) ? "" : Integer.toString(nMedium));

                nCosmetic = (rs.getString("Cosmetic_Fixed") != null) ? rs.getInt("Cosmetic_Fixed") : 0;
                svTemp.setCell(12, (nCosmetic == 0) ? "" : Integer.toString(nCosmetic));

                nWeighted = nFatal * 10 + nSerious * 5 + nMedium * 3 + nCosmetic;
                svTemp.setCell(14, (nWeighted == 0) ? "" : Integer.toString(nWeighted));

                smResult.addRow(svTemp);
            }

            rs.close();
            prepStmt.close();

            smResult = expandReport(smResult);
            if (smResult.getNumberOfRows() == 0) smResult.addRow(svTemp);

            beanReport.setWeeklyReportList(smResult);
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in ReportLeakageBO.getLeakageReport(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in ReportLeakageBO.getLeakageReport(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ReportLeakageBO.getLeakageReport(): ");
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////

        return beanReport;
    }

    public StringMatrix expandReport(StringMatrix smReport) throws SQLException, Exception {
        if (smReport.getNumberOfRows() > 0) {
            final int SHIFT = 5;    //fromdate, todate, reportType, column title
            int nSize = smReport.getNumberOfCols() - SHIFT;
            int[] arrTotal = new int[nSize];
            for (int i = 0; i < nSize; i++) {
                arrTotal[i] = 0;
            }

            for (int c = 0; c < nSize; c++) {
                for (int r = 0; r < smReport.getNumberOfRows(); r++) {
                    int nItem = ("".equals(smReport.getCell(r, c + SHIFT))) ? 0 : Integer.parseInt(smReport.getCell(r, c + SHIFT));
                    arrTotal[c] += nItem;
                }
            }

            StringVector vecTotal = new StringVector(smReport.getNumberOfCols());
            vecTotal.setCell(0, smReport.getCell(0, 0));
            vecTotal.setCell(1, smReport.getCell(0, 1));
            vecTotal.setCell(2, smReport.getCell(0, 2));
            vecTotal.setCell(3, "<B>All work products</B>");
            vecTotal.setCell(4, "<B>All modules</B>");
            for (int i = SHIFT; i < smReport.getNumberOfCols(); i++) {
                String strTotal = Integer.toString(arrTotal[i - SHIFT]);
                vecTotal.setCell(i, "<B>" + (("0".equals(strTotal)) ? "" : strTotal) + "</B>");
            }
            smReport.addRow(vecTotal);
        }
        return smReport;
    }
}