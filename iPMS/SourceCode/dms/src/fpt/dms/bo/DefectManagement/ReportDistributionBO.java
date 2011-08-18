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
 * Title:        ReportDistributionBO.java
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


public class ReportDistributionBO {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    //private Connection con = null;

    private static Logger logger = Logger.getLogger(QueryBO.class.getName());

    public ReportDistributionBO() {

    }

    public ReportWeeklyBean getDistributionReport(int nProjectID, String strFromDate, String strToDate, int nReport) throws SQLException, Exception {
        ReportWeeklyBean beanReport = new ReportWeeklyBean();
        StringMatrix smResult = new StringMatrix();
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            String strFilter = "";
            if (!"".equals(strFromDate)) {
                strFilter += " AND Create_DATE >= TO_DATE('" + strFromDate + "', '" + DMS.DATE_FORMAT + "')";
            }
            if (!"".equals(strToDate)) {
                strFilter += " AND Create_DATE <= TO_DATE('" + strToDate + "', '" + DMS.DATE_FORMAT + "')";
            }
            //strFilter += " AND ds_id < 5 "; // exclude defect with status Accepted or Cancelled
            strFilter += " AND ds_id <> 6 "; // exclude defect with status Cancelled

            strSQL.append("SELECT T0.Name, ");
            strSQL.append(" Fatal_Requirement, Serious_Requirement, Medium_Requirement, Cosmetic_Requirement,");
            strSQL.append(" Fatal_Design, Serious_Design, Medium_Design, Cosmetic_Design,");
            strSQL.append(" Fatal_Coding, Serious_Coding, Medium_Coding, Cosmetic_Coding,");
            strSQL.append(" Fatal_Other, Serious_Other, Medium_Other, Cosmetic_Other");
            strSQL.append(" FROM ");
            strSQL.append(" (SELECT Defect.QA_ID,Name ");
            strSQL.append(" FROM Defect,QC_Activity");
            strSQL.append(" WHERE PROJECT_ID = " + nProjectID + " AND Defect.QA_ID = QC_Activity.QA_ID ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY Defect.QA_ID,Name)  T0, ");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Fatal_Requirement ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 1 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 2 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T1_Requirement,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Serious_Requirement ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 2 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 2 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T2_Requirement,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Medium_Requirement ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 3 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 2 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T3_Requirement,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Cosmetic_Requirement ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 4 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 2 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T4_Requirement,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Fatal_Design ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 1 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 3 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T1_Design,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Serious_Design ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 2 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 3 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T2_Design,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Medium_Design ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 3 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 3 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T3_Design,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Cosmetic_Design ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 4 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 3 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T4_Design,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Fatal_Coding ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 1 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 4 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T1_Coding,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Serious_Coding ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 2 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 4 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T2_Coding,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Medium_Coding ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 3 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 4 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T3_Coding,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Cosmetic_Coding ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 4 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID = 4 ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T4_Coding,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Fatal_Other ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 1 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID NOT IN (2,3,4) ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T1_Other,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Serious_Other ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 2 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID NOT IN (2,3,4) ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T2_Other,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Medium_Other ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 3 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID NOT IN (2,3,4) ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T3_Other,");
            strSQL.append(" (SELECT QA_ID, Count(QA_ID) AS Cosmetic_Other ");
            strSQL.append(" FROM Defect  WHERE DEFS_ID = 4 AND");
            strSQL.append(" Project_ID = " + nProjectID + " AND PROCESS_ID NOT IN (2,3,4) ");
            strSQL.append(strFilter);
            strSQL.append(" GROUP BY QA_ID) T4_Other");
            strSQL.append(" WHERE ");
            strSQL.append(" T0.QA_ID = T1_Requirement.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T2_Requirement.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T3_Requirement.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T4_Requirement.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T1_Design.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T2_Design.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T3_Design.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T4_Design.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T1_Coding.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T2_Coding.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T3_Coding.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T4_Coding.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T1_Other.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T2_Other.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T3_Other.QA_ID(+)");
            strSQL.append(" AND T0.QA_ID = T4_Other.QA_ID(+)");
            strSQL.append(" ORDER BY Name");

            if (DMS.DEBUG)
                logger.debug("Defect Distribution SQL = " + strSQL.toString());

            ds = conPool.getDS();
            con = ds.getConnection();

            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();

            StringVector smTemp = new StringVector(24);
            smTemp.setCell(0, strFromDate);
            smTemp.setCell(1, strToDate);
            smTemp.setCell(2, Integer.toString(nReport));
            smResult.addRow(smTemp);
            while (rs.next()) {
                smTemp.setCell(3, String.valueOf(rs.getString("Name")));

                //REQUIREMENT
                int nFatal = (rs.getString("Fatal_Requirement") != null) ? rs.getInt("Fatal_Requirement") : 0;
                smTemp.setCell(4, (nFatal == 0) ? "" : Integer.toString(nFatal));

                int nSerious = (rs.getString("Serious_Requirement") != null) ? rs.getInt("Serious_Requirement") : 0;
                smTemp.setCell(5, (nSerious == 0) ? "" : Integer.toString(nSerious));

                int nMedium = (rs.getString("Medium_Requirement") != null) ? rs.getInt("Medium_Requirement") : 0;
                smTemp.setCell(6, (nMedium == 0) ? "" : Integer.toString(nMedium));

                int nCosmetic = (rs.getString("Cosmetic_Requirement") != null) ? rs.getInt("Cosmetic_Requirement") : 0;
                smTemp.setCell(7, (nCosmetic == 0) ? "" : Integer.toString(nCosmetic));

                int nWeighted = nFatal * 10 + nSerious * 5 + nMedium * 3 + nCosmetic * 1;
                smTemp.setCell(8, (nWeighted == 0) ? "" : Integer.toString(nWeighted));

                //DESIGN
                nFatal = (rs.getString("Fatal_Design") != null) ? rs.getInt("Fatal_Design") : 0;
                smTemp.setCell(9, (nFatal == 0) ? "" : Integer.toString(nFatal));

                nSerious = (rs.getString("Serious_Design") != null) ? rs.getInt("Serious_Design") : 0;
                smTemp.setCell(10, (nSerious == 0) ? "" : Integer.toString(nSerious));

                nMedium = (rs.getString("Medium_Design") != null) ? rs.getInt("Medium_Design") : 0;
                smTemp.setCell(11, (nMedium == 0) ? "" : Integer.toString(nMedium));

                nCosmetic = (rs.getString("Cosmetic_Design") != null) ? rs.getInt("Cosmetic_Design") : 0;
                smTemp.setCell(12, (nCosmetic == 0) ? "" : Integer.toString(nCosmetic));

                nWeighted = nFatal * 10 + nSerious * 5 + nMedium * 3 + nCosmetic * 1;
                smTemp.setCell(13, (nWeighted == 0) ? "" : Integer.toString(nWeighted));

                //CODING
                nFatal = (rs.getString("Fatal_Coding") != null) ? rs.getInt("Fatal_Coding") : 0;
                smTemp.setCell(14, (nFatal == 0) ? "" : Integer.toString(nFatal));

                nSerious = (rs.getString("Serious_Coding") != null) ? rs.getInt("Serious_Coding") : 0;
                smTemp.setCell(15, (nSerious == 0) ? "" : Integer.toString(nSerious));

                nMedium = (rs.getString("Medium_Coding") != null) ? rs.getInt("Medium_Coding") : 0;
                smTemp.setCell(16, (nMedium == 0) ? "" : Integer.toString(nMedium));

                nCosmetic = (rs.getString("Cosmetic_Coding") != null) ? rs.getInt("Cosmetic_Coding") : 0;
                smTemp.setCell(17, (nCosmetic == 0) ? "" : Integer.toString(nCosmetic));

                nWeighted = nFatal * 10 + nSerious * 5 + nMedium * 3 + nCosmetic * 1;
                smTemp.setCell(18, (nWeighted == 0) ? "" : Integer.toString(nWeighted));

                //OTHER
                nFatal = (rs.getString("Fatal_Other") != null) ? rs.getInt("Fatal_Other") : 0;
                smTemp.setCell(19, (nFatal == 0) ? "" : Integer.toString(nFatal));

                nSerious = (rs.getString("Serious_Other") != null) ? rs.getInt("Serious_Other") : 0;
                smTemp.setCell(20, (nSerious == 0) ? "" : Integer.toString(nSerious));

                nMedium = (rs.getString("Medium_Other") != null) ? rs.getInt("Medium_Other") : 0;
                smTemp.setCell(21, (nMedium == 0) ? "" : Integer.toString(nMedium));

                nCosmetic = (rs.getString("Cosmetic_Other") != null) ? rs.getInt("Cosmetic_Other") : 0;
                smTemp.setCell(22, (nCosmetic == 0) ? "" : Integer.toString(nCosmetic));

                nWeighted = nFatal * 10 + nSerious * 5 + nMedium * 3 + nCosmetic * 1;
                smTemp.setCell(23, (nWeighted == 0) ? "" : Integer.toString(nWeighted));

                smResult.addRow(smTemp);
            }

            rs.close();
            prepStmt.close();

            smResult = expandReport(smResult);
            if (smResult.getNumberOfRows() == 0) {
                for (int i = 3; i < 24; i++) {
                    smTemp.setCell(i, "");
                }
                smResult.addRow(smTemp);
            }

            beanReport.setWeeklyReportList(smResult);
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in ReportDistributionBO.getDistributionReport(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in ReportDistributionBO.getDistributionReport(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ReportDistributionBO.getDistributionReport(): ");
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////

        return beanReport;
    }

    public StringMatrix expandReport(StringMatrix smReport) throws SQLException, Exception {
        if (smReport.getNumberOfRows() > 0) {
            final int SHIFT = 4;    //fromdate, todate, reportType, column title
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
            vecTotal.setCell(3, "<B>Total</B>");
            for (int i = SHIFT; i < smReport.getNumberOfCols(); i++) {
                String strTotal = Integer.toString(arrTotal[i - SHIFT]);
                vecTotal.setCell(i, ("0".equals(strTotal)) ? "" : strTotal);
            }
            smReport.addRow(vecTotal);
        }
        return smReport;
    }
}