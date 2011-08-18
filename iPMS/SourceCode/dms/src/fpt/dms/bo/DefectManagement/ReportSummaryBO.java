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
 * Title:        ReportSummaryBO.java
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
import java.text.DecimalFormat;


public class ReportSummaryBO {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    //private Connection con = null;

    private static Logger logger = Logger.getLogger(QueryBO.class.getName());

    private String strFilter = "";
    private String strForFixed = "";
    private String strFromCreateDate = "";
    private String strToCreateDate = "";
    private String strFromCloseDate = "";
    private String strToCloseDate = "";

    public ReportSummaryBO() {

    }

    public ReportWeeklyBean getSummaryReport(int nProjectID, String strFromDate, String strToDate, int nReport, int nSubReport) throws SQLException, Exception {
        ReportWeeklyBean beanReport = new ReportWeeklyBean();
        StringMatrix smResult = new StringMatrix();
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            setDateConstraint(strFromDate, strToDate);

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Below SQL command (named strSQLforModule) is to query all work products which INCLUDED modules

            StringBuffer strSQLforModule = new StringBuffer();
            //Headers of planned defect table
            strSQLforModule.append("SELECT WPName, ModuleName, PlannedDefect, ReplannedDefect,");
            strSQLforModule.append(" ActualDefect_Fatal, ActualDefect_Serious, ActualDefect_Medium, ActualDefect_Cosmetic");
            strSQLforModule.append(" FROM");

            //Work product and Module
            strSQLforModule.append(" (SELECT WP.name as WPName, M.Name as ModuleName, D.Module_ID");
            strSQLforModule.append(" FROM Defect D, Module M, WorkProduct WP");
            strSQLforModule.append(" WHERE D.project_id = " + nProjectID);
            strSQLforModule.append(" AND D.wp_id = WP.wp_id");
            strSQLforModule.append(" AND D.Module_ID = M.Module_ID");
            strSQLforModule.append(" AND D.Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY D.Module_ID, M.Name, WP.name)  T0,");

            //Planned defect and Re-planned defect
            strSQLforModule.append(" (SELECT M.module_id, M.planned_defect as PlannedDefect, M.replanned_defect as ReplannedDefect");
            strSQLforModule.append(" FROM module M, workproduct WP");
            strSQLforModule.append(" WHERE M.project_id = " + nProjectID);
            strSQLforModule.append(" AND M.wp_id = WP.wp_id");
            strSQLforModule.append(" GROUP BY M.module_id, M.Name, WP.name, M.planned_defect, M.replanned_defect)  T_Planned,");

            //Fatal actual defect
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS ActualDefect_Fatal");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND Defect.defs_id = 1");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_ActualF,");

            //Serious actual defect
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS ActualDefect_Serious");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND Defect.defs_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_ActualS,");

            //Medium actual defect
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS ActualDefect_Medium");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND Defect.defs_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_ActualM,");

            //Cosmetic actual defect
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS ActualDefect_Cosmetic");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND Defect.defs_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_ActualC");

            //Join all together
            strSQLforModule.append(" WHERE T0.Module_ID = T_Planned.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_ActualF.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_ActualS.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_ActualM.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_ActualC.Module_ID(+)");

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Below SQL command (named strSQLforWP) is to query all work products WITHOUT any module

            StringBuffer strSQLforWP = new StringBuffer();
            //Headers of planned defect table
            strSQLforWP.append("SELECT WPName, ModuleName, PlannedDefect, ReplannedDefect,");
            strSQLforWP.append(" ActualDefect_Fatal, ActualDefect_Serious, ActualDefect_Medium, ActualDefect_Cosmetic");
            strSQLforWP.append(" FROM");

            //Work product and EMPTY Module
            strSQLforWP.append(" (SELECT WP.wp_id, WP.name as WPName, 'ZZZ' as ModuleName");    //set as ZZZ to order at the end of resultset
            strSQLforWP.append(" FROM Defect D, WorkProduct WP");
            strSQLforWP.append(" WHERE D.project_id = " + nProjectID);
            strSQLforWP.append(" AND D.wp_id = WP.wp_id");
            strSQLforWP.append(" AND D.Module_ID = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY WP.wp_id, WP.name)  T0,");

            //Planned defect and Re-planned defect
            strSQLforWP.append(" (SELECT WP.wp_id, WPS.planned_defect as PlannedDefect, WPS.replanned_defect as ReplannedDefect");
            strSQLforWP.append(" FROM workproduct WP, wp_size WPS");
            strSQLforWP.append(" WHERE WPS.project_id = " + nProjectID);
            strSQLforWP.append(" AND WPS.wp_id = WP.wp_id");
            strSQLforWP.append(" GROUP BY WP.wp_id, WPS.planned_defect, WPS.replanned_defect)  T_Planned,");

            //Fatal actual defect
            strSQLforWP.append(" (SELECT D.wp_id, Count(D.defect_id) AS ActualDefect_Fatal");
            strSQLforWP.append(" FROM Defect D");
            strSQLforWP.append(" WHERE D.project_id = " + nProjectID);
            strSQLforWP.append(" AND D.defs_id = 1");
            strSQLforWP.append(" AND D.module_ID = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY D.wp_id) T_ActualF,");

            //Serious actual defect
            strSQLforWP.append(" (SELECT D.wp_id, Count(D.defect_id) AS ActualDefect_Serious");
            strSQLforWP.append(" FROM Defect D");
            strSQLforWP.append(" WHERE D.Project_ID = " + nProjectID);
            strSQLforWP.append(" AND D.defs_id = 2");
            strSQLforWP.append(" AND D.module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY D.wp_id) T_ActualS,");

            //Medium actual defect
            strSQLforWP.append(" (SELECT D.wp_id, Count(D.defect_id) AS ActualDefect_Medium");
            strSQLforWP.append(" FROM Defect D");
            strSQLforWP.append(" WHERE D.project_id = " + nProjectID);
            strSQLforWP.append(" AND D.defs_id = 3");
            strSQLforWP.append(" AND D.module_ID = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY D.wp_id) T_ActualM,");

            //Cosmetic actual defect
            strSQLforWP.append(" (SELECT D.wp_id, Count(D.defect_id) AS ActualDefect_Cosmetic");
            strSQLforWP.append(" FROM Defect D");
            strSQLforWP.append(" WHERE D.project_id = " + nProjectID);
            strSQLforWP.append(" AND D.defs_id = 4");
            strSQLforWP.append(" AND D.module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY D.wp_id) T_ActualC");

            //Join all together
            strSQLforWP.append(" WHERE T0.wp_id = T_Planned.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_ActualF.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_ActualS.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_ActualM.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_ActualC.wp_id(+)");

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            strSQL.append(strSQLforModule + " UNION " + strSQLforWP);
            strSQL.append(" ORDER BY WPName, ModuleName");
            if (DMS.DEBUG)
                logger.debug("Summary SQL = " + strSQL.toString());
            StringVector vecItem = new StringVector(9);

            //Add First Row:
            vecItem.setCell(0, strFromDate);
            vecItem.setCell(1, strToDate);
            vecItem.setCell(2, Integer.toString(nReport));
            beanReport.setSubReportType(nSubReport);

            ds = conPool.getDS();
            con = ds.getConnection();

            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                vecItem.setCell(3, (rs.getString("WPName") == null) ? "" : rs.getString("WPName"));

                String strModule = (rs.getString("ModuleName") == null) ? "" : rs.getString("ModuleName");
                if ("ZZZ".equals(strModule)) strModule = "";
                vecItem.setCell(4, strModule);

                String strPlannedDefect = rs.getString("PlannedDefect");
                vecItem.setCell(5, (strPlannedDefect == null) ? "" : strPlannedDefect);
                String strReplannedDefect = rs.getString("ReplannedDefect");
                vecItem.setCell(6, (strReplannedDefect == null) ? "" : strReplannedDefect);

                int nAF = (rs.getString("ActualDefect_Fatal") == null) ? 0 : rs.getInt("ActualDefect_Fatal");
                int nAS = (rs.getString("ActualDefect_Serious") == null) ? 0 : rs.getInt("ActualDefect_Serious");
                int nAM = (rs.getString("ActualDefect_Medium") == null) ? 0 : rs.getInt("ActualDefect_Medium");
                int nAC = (rs.getString("ActualDefect_Cosmetic") == null) ? 0 : rs.getInt("ActualDefect_Cosmetic");
                int nWeightedActual = nAF * 10 + nAS * 5 + nAM * 3 + nAC * 1;

                String strActualDefect = (nWeightedActual == 0) ? "" : Integer.toString(nWeightedActual);
                vecItem.setCell(7, strActualDefect);

                float fPlanned = (strPlannedDefect == null || "".equals(strPlannedDefect)) ? 0 : Float.parseFloat(strPlannedDefect);
                float fReplanned = (strReplannedDefect == null || "".equals(strReplannedDefect)) ? fPlanned : Float.parseFloat(strReplannedDefect);
                float fActual = (strActualDefect == null || "".equals(strActualDefect)) ? 0 : Float.parseFloat(strActualDefect);
                float fDeviation = fActual - fReplanned;

                DecimalFormat dfDev = new DecimalFormat("0.##");
                vecItem.setCell(8, (fDeviation == 0) ? ((fPlanned == 0) ? "" : "0") : dfDev.format(fDeviation));

                smResult.addRow(vecItem);
            }//end while

            if (smResult.getNumberOfRows() == 0) smResult.addRow(vecItem);

            rs.close();
            prepStmt.close();


            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Add a TOTAL line at the end of table
            if (smResult.getNumberOfRows() > 0) {
                final int SHIFT = 5;
                int nSize = smResult.getNumberOfCols() - SHIFT;     //from PlannedDefect to the end.
                float[] arrTotal = new float[nSize];
                for (int i = 0; i < nSize; i++) arrTotal[i] = 0;

                for (int c = 0; c < nSize; c++) {
                    for (int r = 0; r < smResult.getNumberOfRows(); r++) {
                        float fItem = ("".equals(smResult.getCell(r, c + SHIFT))) ? 0 : Float.parseFloat(smResult.getCell(r, c + SHIFT));
                        arrTotal[c] += fItem;
                    }
                }

                StringVector vecTotal = new StringVector(smResult.getNumberOfCols());
                vecTotal.setCell(0, strFromDate);
                vecTotal.setCell(1, strToDate);
                vecTotal.setCell(2, Integer.toString(nReport));
                vecTotal.setCell(3, "<b>All work products</b>");
                vecTotal.setCell(4, "<b>All modules</b>");
                for (int i = SHIFT; i < smResult.getNumberOfCols(); i++) {
                    DecimalFormat dfTotal = new DecimalFormat("0.##");
                    String strTotal = dfTotal.format(arrTotal[i - SHIFT]);
                    vecTotal.setCell(i, "<b>" + (("0".equals(strTotal)) ? "" : strTotal) + "</b>");
                }
                smResult.addRow(vecTotal);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            beanReport.setWeeklyReportList(smResult);

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////Thaison - Oct 01, 2002
            StringMatrix mtxSubReport = new StringMatrix();
            switch (nSubReport) {
                case 1:
                    mtxSubReport = getSubReportByQC(con, nProjectID, strFromDate, strToDate);
                    break;
                case 2:
                    mtxSubReport = getSubReportByProcess(con, nProjectID, strFromDate, strToDate);
                    break;
            }

            beanReport.setSubSummaryReport(mtxSubReport);

        }// end try
        catch (SQLException ex) {
            logger.error("SQLException occurs in ReportSummaryBO.getSummaryReport(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in ReportSummaryBO.getSummaryReport(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ReportSummaryBO.getSummaryReport(): ");
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////

        return beanReport;
    }

    /**
     * method: getSubReportByProcess()
     * author: Nguyen Thai Son
     * date: Oct 01, 2002
     * description: get a sub-summary report by process
     * */
    private StringMatrix getSubReportByQC(Connection con, int nProjectID, String strFromDate, String strToDate) {
        StringMatrix mtxResult = new StringMatrix();
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        try {
            setDateConstraint(strFromDate, strToDate);

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Below SQL command (named strSQLforModule) is to query all work products INCLUDE modules

            StringBuffer strSQLforModule = new StringBuffer();

            //Headers of planned defect table
            strSQLforModule.append("SELECT WPName, ModuleName,");
            strSQLforModule.append(" DocumentReviewF, DocumentReviewS, DocumentReviewM, DocumentReviewC,");
            strSQLforModule.append(" PrototypeReviewF, PrototypeReviewS, PrototypeReviewM, PrototypeReviewC,");
            strSQLforModule.append(" CodeReviewF, CodeReviewS, CodeReviewM, CodeReviewC,");
            strSQLforModule.append(" UnitTestF, UnitTestS, UnitTestM, UnitTestC,");
            strSQLforModule.append(" IntegrationTestF, IntegrationTestS, IntegrationTestM, IntegrationTestC,");
            strSQLforModule.append(" SystemTestF, SystemTestS, SystemTestM, SystemTestC,");
            strSQLforModule.append(" AcceptanceTestF, AcceptanceTestS, AcceptanceTestM, AcceptanceTestC,");
            strSQLforModule.append(" OthersF, OthersS, OthersM, OthersC");
            strSQLforModule.append(" FROM");

            //Work product and Module
            strSQLforModule.append(" (SELECT WP.name as WPName, M.Name as ModuleName, D.Module_ID");
            strSQLforModule.append(" FROM Defect D, Module M, WorkProduct WP");
            strSQLforModule.append(" WHERE D.project_id = " + nProjectID);
            strSQLforModule.append(" AND D.wp_id = WP.wp_id");
//          strSQLforModule.append(" AND WP.wp_id = M.wp_id");
            strSQLforModule.append(" AND D.Module_ID = M.Module_ID");
            strSQLforModule.append(" AND D.Module_ID > 0");
            strSQLforModule.append(strFilter);

            strSQLforModule.append(" GROUP BY D.Module_ID, M.Name, WP.name)  T0,");

            //DOCUMENT REVIEW
            //Fatal
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS DocumentReviewF");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 20");
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_DocumentRF,");

            //Serious
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS DocumentReviewS");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 20");
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_DocumentRS,");

            //Medium
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS DocumentReviewM");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 20");
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_DocumentRM,");

            //Cosmetic
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS DocumentReviewC");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 20");
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_DocumentRC,");

            //PROTOTYPE REVIEW
            //Fatal
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS PrototypeReviewF");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 23");
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_PrototypeRF,");

            //Serious
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS PrototypeReviewS");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 23");
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_PrototypeRS,");

            //Medium
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS PrototypeReviewM");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 23");
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_PrototypeRM,");

            //Cosmetic
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS PrototypeReviewC");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 23");
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_PrototypeRC,");

            //CODE REVIEW
            //Fatal
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS CodeReviewF");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 21");
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_CodeRF,");

            //Serious
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS CodeReviewS");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 21");
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_CodeRS,");

            //Medium
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS CodeReviewM");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 21");
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_CodeRM,");

            //Cosmetic
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS CodeReviewC");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 21");
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_CodeRC,");

            //UNIT TEST
            //Fatal
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS UnitTestF");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 10");
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_UnitTF,");

            //Serious
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS UnitTestS");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 10");
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_UnitTS,");

            //Medium
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS UnitTestM");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 10");
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_UnitTM,");

            //Cosmetic
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS UnitTestC");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 10");
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_UnitTC,");

            //INTEGRATION TEST
            //Fatal
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS IntegrationTestF");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 11");
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_IntegrationTF,");

            //Serious
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS IntegrationTestS");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 11");
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_IntegrationTS,");

            //Medium
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS IntegrationTestM");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 11");
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_IntegrationTM,");

            //Cosmetic
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS IntegrationTestC");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 11");
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_IntegrationTC,");

            //SYSTEM TEST
            //Fatal
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS SystemTestF");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 12");
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_SystemTF,");

            //Serious
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS SystemTestS");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 12");
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_SystemTS,");

            //Medium
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS SystemTestM");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 12");
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_SystemTM,");

            //Cosmetic
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS SystemTestC");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 12");
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_SystemTC,");

            //ACCEPTANCE TEST
            //Fatal
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS AcceptanceTestF");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 13");
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_AcceptanceTF,");

            //Serious
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS AcceptanceTestS");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 13");
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_AcceptanceTS,");

            //Medium
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS AcceptanceTestM");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 13");
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_AcceptanceTM,");

            //Cosmetic
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS AcceptanceTestC");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID = 13");
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_AcceptanceTC,");

            //OTHERS
            //Fatal
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS OthersF");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID NOT IN (10, 11, 12, 13, 20, 21, 23)");
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_OthersF,");

            //Serious
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS OthersS");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID NOT IN (10, 11, 12, 13, 20, 21, 23)");
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_OthersS,");

            //Medium
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS OthersM");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID NOT IN (10, 11, 12, 13, 20, 21, 23)");
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_OthersM,");

            //Cosmetic
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS OthersC");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND QA_ID NOT IN (10, 11, 12, 13, 20, 21, 23)");
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T_OthersC");

            //Join all together
            strSQLforModule.append(" WHERE T0.Module_ID = T_DocumentRF.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_DocumentRS.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_DocumentRM.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_DocumentRC.Module_ID(+)");

            //prototype review
            strSQLforModule.append(" AND T0.Module_ID = T_PrototypeRF.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_PrototypeRS.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_PrototypeRM.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_PrototypeRC.Module_ID(+)");

            //code review
            strSQLforModule.append(" AND T0.Module_ID = T_CodeRF.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_CodeRS.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_CodeRM.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_CodeRC.Module_ID(+)");

            //unit test
            strSQLforModule.append(" AND T0.Module_ID = T_UnitTF.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_UnitTS.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_UnitTM.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_UnitTC.Module_ID(+)");

            //integration test
            strSQLforModule.append(" AND T0.Module_ID = T_IntegrationTF.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_IntegrationTS.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_IntegrationTM.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_IntegrationTC.Module_ID(+)");

            //system test
            strSQLforModule.append(" AND T0.Module_ID = T_SystemTF.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_SystemTS.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_SystemTM.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_SystemTC.Module_ID(+)");

            //acceptance test
            strSQLforModule.append(" AND T0.Module_ID = T_AcceptanceTF.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_AcceptanceTS.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_AcceptanceTM.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_AcceptanceTC.Module_ID(+)");

            //others
            strSQLforModule.append(" AND T0.Module_ID = T_OthersF.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_OthersS.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_OthersM.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T_OthersC.Module_ID(+)");

//          strSQLforModule.append(" ORDER BY T0.WPName, T0.ModuleName");
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Below SQL command (named strSQLforWP) is to query all work products INCLUDE modules

            StringBuffer strSQLforWP = new StringBuffer();

            //Headers of planned defect table
            strSQLforWP.append("SELECT WPName, ModuleName,");
            strSQLforWP.append(" DocumentReviewF, DocumentReviewS, DocumentReviewM, DocumentReviewC,");
            strSQLforWP.append(" PrototypeReviewF, PrototypeReviewS, PrototypeReviewM, PrototypeReviewC,");
            strSQLforWP.append(" CodeReviewF, CodeReviewS, CodeReviewM, CodeReviewC,");
            strSQLforWP.append(" UnitTestF, UnitTestS, UnitTestM, UnitTestC,");
            strSQLforWP.append(" IntegrationTestF, IntegrationTestS, IntegrationTestM, IntegrationTestC,");
            strSQLforWP.append(" SystemTestF, SystemTestS, SystemTestM, SystemTestC,");
            strSQLforWP.append(" AcceptanceTestF, AcceptanceTestS, AcceptanceTestM, AcceptanceTestC,");
            strSQLforWP.append(" OthersF, OthersS, OthersM, OthersC");
            strSQLforWP.append(" FROM");

            //Work product and EMPTY Module
            strSQLforWP.append(" (SELECT D.wp_id, WP.name as WPName, 'ZZZ' as ModuleName");     //set as ZZZ to order at the bottom of result set
            strSQLforWP.append(" FROM Defect D, WorkProduct WP");
            strSQLforWP.append(" WHERE D.project_id = " + nProjectID);
            strSQLforWP.append(" AND D.wp_id = WP.wp_id");
            strSQLforWP.append(" AND D.Module_ID = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY D.wp_id, WP.name)  T0,");

            //DOCUMENT REVIEW
            //Fatal
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS DocumentReviewF");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 20");
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_DocumentRF,");

            //Serious
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS DocumentReviewS");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 20");
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_DocumentRS,");

            //Medium
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS DocumentReviewM");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 20");
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_DocumentRM,");

            //Cosmetic
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS DocumentReviewC");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 20");
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_DocumentRC,");

            //PROTOTYPE REVIEW
            //Fatal
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS PrototypeReviewF");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 23");
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_PrototypeRF,");

            //Serious
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS PrototypeReviewS");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 23");
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_PrototypeRS,");

            //Medium
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS PrototypeReviewM");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 23");
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_PrototypeRM,");

            //Cosmetic
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS PrototypeReviewC");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 23");
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_PrototypeRC,");

            //CODE REVIEW
            //Fatal
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS CodeReviewF");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 21");
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_CodeRF,");

            //Serious
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS CodeReviewS");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 21");
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_CodeRS,");

            //Medium
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS CodeReviewM");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 21");
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_CodeRM,");

            //Cosmetic
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS CodeReviewC");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 21");
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_CodeRC,");

            //UNIT TEST
            //Fatal
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS UnitTestF");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 10");
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_UnitTF,");

            //Serious
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS UnitTestS");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 10");
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_UnitTS,");

            //Medium
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS UnitTestM");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 10");
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_UnitTM,");

            //Cosmetic
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS UnitTestC");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 10");
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_UnitTC,");

            //INTEGRATION TEST
            //Fatal
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS IntegrationTestF");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 11");
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_IntegrationTF,");

            //Serious
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS IntegrationTestS");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 11");
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_IntegrationTS,");

            //Medium
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS IntegrationTestM");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 11");
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_IntegrationTM,");

            //Cosmetic
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS IntegrationTestC");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 11");
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_IntegrationTC,");

            //SYSTEM TEST
            //Fatal
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS SystemTestF");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 12");
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_SystemTF,");

            //Serious
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS SystemTestS");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 12");
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_SystemTS,");

            //Medium
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS SystemTestM");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 12");
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_SystemTM,");

            //Cosmetic
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS SystemTestC");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 12");
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_SystemTC,");

            //ACCEPTANCE TEST
            //Fatal
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS AcceptanceTestF");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 13");
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_AcceptanceTF,");

            //Serious
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS AcceptanceTestS");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 13");
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_AcceptanceTS,");

            //Medium
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS AcceptanceTestM");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 13");
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_AcceptanceTM,");

            //Cosmetic
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS AcceptanceTestC");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID = 13");
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_AcceptanceTC,");

            //OTHERS
            //Fatal
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS OthersF");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID NOT IN (10, 11, 12, 13, 20, 21, 23)");
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_OthersF,");

            //Serious
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS OthersS");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID NOT IN (10, 11, 12, 13, 20, 21, 23)");
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_OthersS,");

            //Medium
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS OthersM");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID NOT IN (10, 11, 12, 13, 20, 21, 23)");
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_OthersM,");

            //Cosmetic
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS OthersC");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND QA_ID NOT IN (10, 11, 12, 13, 20, 21, 23)");
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T_OthersC");

            //Join all together
            strSQLforWP.append(" WHERE T0.wp_id = T_DocumentRF.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_DocumentRS.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_DocumentRM.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_DocumentRC.wp_id(+)");

            //prototype review
            strSQLforWP.append(" AND T0.wp_id = T_PrototypeRF.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_PrototypeRS.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_PrototypeRM.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_PrototypeRC.wp_id(+)");

            //code review
            strSQLforWP.append(" AND T0.wp_id = T_CodeRF.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_CodeRS.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_CodeRM.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_CodeRC.wp_id(+)");

            //unit test
            strSQLforWP.append(" AND T0.wp_id = T_UnitTF.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_UnitTS.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_UnitTM.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_UnitTC.wp_id(+)");

            //integration test
            strSQLforWP.append(" AND T0.wp_id = T_IntegrationTF.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_IntegrationTS.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_IntegrationTM.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_IntegrationTC.wp_id(+)");

            //system test
            strSQLforWP.append(" AND T0.wp_id = T_SystemTF.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_SystemTS.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_SystemTM.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_SystemTC.wp_id(+)");

            //acceptance test
            strSQLforWP.append(" AND T0.wp_id = T_AcceptanceTF.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_AcceptanceTS.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_AcceptanceTM.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_AcceptanceTC.wp_id(+)");

            //others
            strSQLforWP.append(" AND T0.wp_id = T_OthersF.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_OthersS.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_OthersM.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T_OthersC.wp_id(+)");

//          strSQLforWP.append(" ORDER BY T0.WPName, T0.ModuleName");
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            StringBuffer strSQL = new StringBuffer();

            strSQL.append(strSQLforModule + " UNION " + strSQLforWP);
            strSQL.append(" ORDER BY WPName, ModuleName");
            if (DMS.DEBUG)
                logger.info("getSubReportByQC(): strSQL = " + strSQL.toString());
            StringVector vecItem = new StringVector(11);
            vecItem.setCell(0, "1");        //1: QC Activity

            DataSource ds = null;
            if (con.isClosed()) {
                ds = conPool.getDS();
                con = ds.getConnection();
            }

            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                vecItem.setCell(1, (rs.getString("WPName") == null) ? "" : rs.getString("WPName"));
                String strModule = (rs.getString("ModuleName") == null) ? "" : rs.getString("ModuleName");
                if ("ZZZ".equals(strModule)) strModule = "";
                vecItem.setCell(2, strModule);

                //Document Review
                int nDRF = (rs.getString("DocumentReviewF") == null) ? 0 : rs.getInt("DocumentReviewF");
                int nDRS = (rs.getString("DocumentReviewS") == null) ? 0 : rs.getInt("DocumentReviewS");
                int nDRM = (rs.getString("DocumentReviewM") == null) ? 0 : rs.getInt("DocumentReviewM");
                int nDRC = (rs.getString("DocumentReviewC") == null) ? 0 : rs.getInt("DocumentReviewC");
                int nWeightedDR = nDRF * 10 + nDRS * 5 + nDRM * 3 + nDRC * 1;
                String strDR = (nWeightedDR == 0) ? "" : Integer.toString(nWeightedDR);
                vecItem.setCell(3, strDR);

                //Prototype Review
                int nPRF = (rs.getString("PrototypeReviewF") == null) ? 0 : rs.getInt("PrototypeReviewF");
                int nPRS = (rs.getString("PrototypeReviewS") == null) ? 0 : rs.getInt("PrototypeReviewS");
                int nPRM = (rs.getString("PrototypeReviewM") == null) ? 0 : rs.getInt("PrototypeReviewM");
                int nPRC = (rs.getString("PrototypeReviewC") == null) ? 0 : rs.getInt("PrototypeReviewC");
                int nWeightedPR = nPRF * 10 + nPRS * 5 + nPRM * 3 + nPRC * 1;
                String strPR = (nWeightedPR == 0) ? "" : Integer.toString(nWeightedPR);
                vecItem.setCell(4, strPR);

                //Code Review
                int nCRF = (rs.getString("CodeReviewF") == null) ? 0 : rs.getInt("CodeReviewF");
                int nCRS = (rs.getString("CodeReviewS") == null) ? 0 : rs.getInt("CodeReviewS");
                int nCRM = (rs.getString("CodeReviewM") == null) ? 0 : rs.getInt("CodeReviewM");
                int nCRC = (rs.getString("CodeReviewC") == null) ? 0 : rs.getInt("CodeReviewC");
                int nWeightedCR = nCRF * 10 + nCRS * 5 + nCRM * 3 + nCRC * 1;
                String strCR = (nWeightedCR == 0) ? "" : Integer.toString(nWeightedCR);
                vecItem.setCell(5, strCR);

                //Unit Test
                int nUTF = (rs.getString("UnitTestF") == null) ? 0 : rs.getInt("UnitTestF");
                int nUTS = (rs.getString("UnitTestS") == null) ? 0 : rs.getInt("UnitTestS");
                int nUTM = (rs.getString("UnitTestM") == null) ? 0 : rs.getInt("UnitTestM");
                int nUTC = (rs.getString("UnitTestC") == null) ? 0 : rs.getInt("UnitTestC");
                int nWeightedUT = nUTF * 10 + nUTS * 5 + nUTM * 3 + nUTC * 1;
                String strUT = (nWeightedUT == 0) ? "" : Integer.toString(nWeightedUT);
                vecItem.setCell(6, strUT);

                //Integration Test
                int nITF = (rs.getString("IntegrationTestF") == null) ? 0 : rs.getInt("IntegrationTestF");
                int nITS = (rs.getString("IntegrationTestS") == null) ? 0 : rs.getInt("IntegrationTestS");
                int nITM = (rs.getString("IntegrationTestM") == null) ? 0 : rs.getInt("IntegrationTestM");
                int nITC = (rs.getString("IntegrationTestC") == null) ? 0 : rs.getInt("IntegrationTestC");
                int nWeightedIT = nITF * 10 + nITS * 5 + nITM * 3 + nITC * 1;
                String strIT = (nWeightedIT == 0) ? "" : Integer.toString(nWeightedIT);
                vecItem.setCell(7, strIT);

                //System Test
                int nSTF = (rs.getString("SystemTestF") == null) ? 0 : rs.getInt("SystemTestF");
                int nSTS = (rs.getString("SystemTestS") == null) ? 0 : rs.getInt("SystemTestS");
                int nSTM = (rs.getString("SystemTestM") == null) ? 0 : rs.getInt("SystemTestM");
                int nSTC = (rs.getString("SystemTestC") == null) ? 0 : rs.getInt("SystemTestC");
                int nWeightedST = nSTF * 10 + nSTS * 5 + nSTM * 3 + nSTC * 1;
                String strST = (nWeightedST == 0) ? "" : Integer.toString(nWeightedST);
                vecItem.setCell(8, strST);

                //Acceptance Test
                int nATF = (rs.getString("AcceptanceTestF") == null) ? 0 : rs.getInt("AcceptanceTestF");
                int nATS = (rs.getString("AcceptanceTestS") == null) ? 0 : rs.getInt("AcceptanceTestS");
                int nATM = (rs.getString("AcceptanceTestM") == null) ? 0 : rs.getInt("AcceptanceTestM");
                int nATC = (rs.getString("AcceptanceTestC") == null) ? 0 : rs.getInt("AcceptanceTestC");
                int nWeightedAT = nATF * 10 + nATS * 5 + nATM * 3 + nATC * 1;
                String strAT = (nWeightedAT == 0) ? "" : Integer.toString(nWeightedAT);
                vecItem.setCell(9, strAT);

                //Acceptance Test
                int nOthersF = (rs.getString("OthersF") == null) ? 0 : rs.getInt("OthersF");
                int nOthersS = (rs.getString("OthersS") == null) ? 0 : rs.getInt("OthersS");
                int nOthersM = (rs.getString("OthersM") == null) ? 0 : rs.getInt("OthersM");
                int nOthersC = (rs.getString("OthersC") == null) ? 0 : rs.getInt("OthersC");
                int nWeightedOthers = nOthersF * 10 + nOthersS * 5 + nOthersM * 3 + nOthersC * 1;
                String strOthers = (nWeightedOthers == 0) ? "" : Integer.toString(nWeightedOthers);
                vecItem.setCell(10, strOthers);

                mtxResult.addRow(vecItem);
            }//end while

            rs.close();
            prepStmt.close();

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (mtxResult.getNumberOfRows() > 0) {
                //Add a TOTAL line at the end of table
                final int SHIFT = 3;
                int nSize = mtxResult.getNumberOfCols() - SHIFT;        //from DocumentReview to the end.
                float[] arrTotal = new float[nSize];
                for (int i = 0; i < nSize; i++) {
                    arrTotal[i] = 0;
                }

                for (int c = 0; c < nSize; c++) {
                    for (int r = 0; r < mtxResult.getNumberOfRows(); r++) {
                        float fItem = ("".equals(mtxResult.getCell(r, c + SHIFT))) ? 0 : Float.parseFloat(mtxResult.getCell(r, c + SHIFT));
                        arrTotal[c] += fItem;
                    }
                }

                StringVector vecTotal = new StringVector(mtxResult.getNumberOfCols());
                vecTotal.setCell(0, "1");
                vecTotal.setCell(1, "<B>All work products</B>");
                vecTotal.setCell(2, "<B>All modules</B>");
                for (int i = SHIFT; i < mtxResult.getNumberOfCols(); i++) {
                    DecimalFormat dfTotal = new DecimalFormat("0.##");
                    String strTotal = dfTotal.format(arrTotal[i - SHIFT]);
                    vecTotal.setCell(i, "<B>" + (("0".equals(strTotal)) ? "" : strTotal) + "</B>");
                }
                mtxResult.addRow(vecTotal);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in ReportSummaryBO.getSubReportByQC(): " + ex.getMessage());
        }
        catch (Exception e) {
            logger.error("Exception occurs in ReportSummaryBO.getSubReportByQC(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ReportSummaryBO.getSubReportByQC(): ");
        }

        return mtxResult;
    }


    private StringMatrix getSubReportByProcess(Connection con, int nProjectID, String strFromDate, String strToDate) {
        StringMatrix mtxResult = new StringMatrix();

        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            setDateConstraint(strFromDate, strToDate);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Below SQL command (named strSQLforModuleforModule) is to query all work products INCLUDE modules
            StringBuffer strSQLforModule = new StringBuffer();

            //Headers of planned defect table
            strSQLforModule.append("SELECT WPName, ModuleName,");
            strSQLforModule.append(" Fatal_Requirement, Serious_Requirement, Medium_Requirement, Cosmetic_Requirement,");
            strSQLforModule.append(" Fatal_Design, Serious_Design, Medium_Design, Cosmetic_Design,");
            strSQLforModule.append(" Fatal_Coding, Serious_Coding, Medium_Coding, Cosmetic_Coding,");
            strSQLforModule.append(" Fatal_Other, Serious_Other, Medium_Other, Cosmetic_Other");
            strSQLforModule.append(" FROM");

            //Work product and Module
            strSQLforModule.append(" (SELECT WP.name as WPName, M.Name as ModuleName, D.Module_ID");
            strSQLforModule.append(" FROM Defect D, Module M, WorkProduct WP");
            strSQLforModule.append(" WHERE D.project_id = " + nProjectID);
            strSQLforModule.append(" AND D.wp_id = WP.wp_id");
//          strSQLforModule.append(" AND WP.wp_id = M.wp_id");
            strSQLforModule.append(" AND D.Module_ID = M.Module_ID");
            strSQLforModule.append(" AND D.Module_ID > 0");

            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY D.Module_ID, M.Name, WP.name)  T0,");

            //Fatal requirement
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Fatal_Requirement");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND process_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T1_Requirement,");

            //Serious requirement
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Serious_Requirement");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND process_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T2_Requirement,");

            //Medium requirement
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Medium_Requirement");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND process_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T3_Requirement,");

            //Cosmetic requirement
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Cosmetic_Requirement");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND process_id = 2");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T4_Requirement,");

            //Fatal design
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Fatal_Design");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND process_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T1_Design,");

            //Serious design
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Serious_Design");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND process_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T2_Design,");

            //Medium design
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Medium_Design");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND process_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T3_Design,");

            //Cosmetic design
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Cosmetic_Design");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND process_id = 3");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T4_Design,");


            //Fatal coding
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Fatal_Coding");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND process_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T1_Coding,");

            //Serious coding
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Serious_Coding");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND process_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T2_Coding,");

            //Medium coding
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Medium_Coding");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND process_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T3_Coding,");

            //Cosmetic coding
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Cosmetic_Coding");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND process_id = 4");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T4_Coding,");


            //Fatal other
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Fatal_Other");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 1");
            strSQLforModule.append(" AND process_id NOT IN (2, 3, 4)");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T1_Other,");

            //Serious other
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Serious_Other");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 2");
            strSQLforModule.append(" AND process_id NOT IN (2, 3, 4)");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T2_Other,");

            //Medium other
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Medium_Other");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 3");
            strSQLforModule.append(" AND process_id NOT IN (2, 3, 4)");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T3_Other,");

            //Cosmetic other
            strSQLforModule.append(" (SELECT Module_ID, Count(Module_ID) AS Cosmetic_Other");
            strSQLforModule.append(" FROM Defect");
            strSQLforModule.append(" WHERE Project_ID = " + nProjectID);
            strSQLforModule.append(" AND defs_id = 4");
            strSQLforModule.append(" AND process_id NOT IN (2, 3, 4)");
            strSQLforModule.append(" AND Module_ID > 0");
            strSQLforModule.append(strFilter);
            strSQLforModule.append(" GROUP BY Module_ID) T4_Other");

            //Join all together
            strSQLforModule.append(" WHERE T0.Module_ID = T1_Requirement.Module_ID(+) ");
            strSQLforModule.append(" AND T0.Module_ID = T2_Requirement.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T3_Requirement.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T4_Requirement.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T1_Design.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T2_Design.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T3_Design.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T4_Design.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T1_Coding.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T2_Coding.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T3_Coding.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T4_Coding.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T1_Other.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T2_Other.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T3_Other.Module_ID(+)");
            strSQLforModule.append(" AND T0.Module_ID = T4_Other.Module_ID(+)");
            //strSQLforModule.append(" ORDER BY WPName, ModuleName");

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Below SQL command (named strSQLforWP) is to query all work products WITHOUT any module
            StringBuffer strSQLforWP = new StringBuffer();

            //Headers of planned defect table
            strSQLforWP.append("SELECT WPName, ModuleName,");
            strSQLforWP.append(" Fatal_Requirement, Serious_Requirement, Medium_Requirement, Cosmetic_Requirement,");
            strSQLforWP.append(" Fatal_Design, Serious_Design, Medium_Design, Cosmetic_Design,");
            strSQLforWP.append(" Fatal_Coding, Serious_Coding, Medium_Coding, Cosmetic_Coding,");
            strSQLforWP.append(" Fatal_Other, Serious_Other, Medium_Other, Cosmetic_Other");
            strSQLforWP.append(" FROM");

            //Work product and Module
            strSQLforWP.append(" (SELECT D.wp_id, WP.name as WPName, 'ZZZ' as ModuleName");
            strSQLforWP.append(" FROM Defect D, WorkProduct WP");
            strSQLforWP.append(" WHERE D.project_id = " + nProjectID);
            strSQLforWP.append(" AND D.wp_id = WP.wp_id");
            strSQLforWP.append(" AND D.Module_ID = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY D.wp_id, WP.name)  T0,");

            //Fatal requirement
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Fatal_Requirement");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND process_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T1_Requirement,");

            //Serious requirement
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Serious_Requirement");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND process_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T2_Requirement,");

            //Medium requirement
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Medium_Requirement");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND process_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T3_Requirement,");

            //Cosmetic requirement
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Cosmetic_Requirement");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND process_id = 2");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T4_Requirement,");

            //Fatal design
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Fatal_Design");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND process_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T1_Design,");

            //Serious design
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Serious_Design");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND process_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T2_Design,");

            //Medium design
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Medium_Design");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND process_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T3_Design,");

            //Cosmetic design
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Cosmetic_Design");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND process_id = 3");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T4_Design,");


            //Fatal coding
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Fatal_Coding");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND process_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T1_Coding,");

            //Serious coding
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Serious_Coding");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND process_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T2_Coding,");

            //Medium coding
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Medium_Coding");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND process_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T3_Coding,");

            //Cosmetic coding
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Cosmetic_Coding");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND process_id = 4");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T4_Coding,");


            //Fatal other
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Fatal_Other");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 1");
            strSQLforWP.append(" AND process_id NOT IN (2, 3, 4)");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T1_Other,");

            //Serious other
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Serious_Other");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 2");
            strSQLforWP.append(" AND process_id NOT IN (2, 3, 4)");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T2_Other,");

            //Medium other
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Medium_Other");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 3");
            strSQLforWP.append(" AND process_id NOT IN (2, 3, 4)");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T3_Other,");

            //Cosmetic other
            strSQLforWP.append(" (SELECT wp_id, Count(defect_id) AS Cosmetic_Other");
            strSQLforWP.append(" FROM Defect");
            strSQLforWP.append(" WHERE Project_ID = " + nProjectID);
            strSQLforWP.append(" AND defs_id = 4");
            strSQLforWP.append(" AND process_id NOT IN (2, 3, 4)");
            strSQLforWP.append(" AND module_id = 0");
            strSQLforWP.append(strFilter);
            strSQLforWP.append(" GROUP BY wp_id) T4_Other");

            //Join all together
            strSQLforWP.append(" WHERE T0.wp_id = T1_Requirement.wp_id(+) ");
            strSQLforWP.append(" AND T0.wp_id = T2_Requirement.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T3_Requirement.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T4_Requirement.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T1_Design.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T2_Design.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T3_Design.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T4_Design.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T1_Coding.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T2_Coding.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T3_Coding.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T4_Coding.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T1_Other.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T2_Other.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T3_Other.wp_id(+)");
            strSQLforWP.append(" AND T0.wp_id = T4_Other.wp_id(+)");
            //strSQLforWP.append(" ORDER BY WPName, ModuleName");

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            StringBuffer strSQL = new StringBuffer();
            strSQL.append(strSQLforModule + " UNION " + strSQLforWP);
            strSQL.append(" ORDER BY WPName, ModuleName");
            if (DMS.DEBUG)
                logger.info("modelhandlers.SearchWeeklyReportHandler.getSubReportByProcess(): strSQL = " + strSQL.toString());
            StringVector vecItem = new StringVector(23);

            vecItem.setCell(0, "2");        //2: Process injected

            DataSource ds = null;
            if (con.isClosed()) {
                ds = conPool.getDS();
                con = ds.getConnection();
            }
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                vecItem.setCell(1, (rs.getString("WPName") == null) ? "" : rs.getString("WPName"));

                String strModule = (rs.getString("ModuleName") == null) ? "" : rs.getString("ModuleName");
                if ("ZZZ".equals(strModule)) {
                    strModule = "";
                }
                vecItem.setCell(2, strModule);

                //Requirement
                int nFR = rs.getString("Fatal_Requirement") == null ? 0 : rs.getInt("Fatal_Requirement");
                vecItem.setCell(3, (nFR == 0) ? "" : Integer.toString(nFR));
                int nSR = rs.getString("Serious_Requirement") == null ? 0 : rs.getInt("Serious_Requirement");
                vecItem.setCell(4, (nSR == 0) ? "" : Integer.toString(nSR));
                int nMR = rs.getString("Medium_Requirement") == null ? 0 : rs.getInt("Medium_Requirement");
                vecItem.setCell(5, (nMR == 0) ? "" : Integer.toString(nMR));
                int nCR = rs.getString("Cosmetic_Requirement") == null ? 0 : rs.getInt("Cosmetic_Requirement");
                vecItem.setCell(6, (nCR == 0) ? "" : Integer.toString(nCR));
                int nWR = nFR * 10 + nSR * 5 + nMR * 3 + nCR * 1;
                vecItem.setCell(7, (nWR == 0) ? "" : Integer.toString(nWR));

                //Design
                int nFD = rs.getString("Fatal_Design") == null ? 0 : rs.getInt("Fatal_Design");
                vecItem.setCell(8, (nFD == 0) ? "" : Integer.toString(nFD));
                int nSD = rs.getString("Serious_Design") == null ? 0 : rs.getInt("Serious_Design");
                vecItem.setCell(9, (nSD == 0) ? "" : Integer.toString(nSD));
                int nMD = rs.getString("Medium_Design") == null ? 0 : rs.getInt("Medium_Design");
                vecItem.setCell(10, (nMD == 0) ? "" : Integer.toString(nMD));
                int nCD = rs.getString("Cosmetic_Design") == null ? 0 : rs.getInt("Cosmetic_Design");
                vecItem.setCell(11, (nCD == 0) ? "" : Integer.toString(nCD));
                int nWD = nFD * 10 + nSD * 5 + nMD * 3 + nCD * 1;
                vecItem.setCell(12, (nWD == 0) ? "" : Integer.toString(nWD));

                //Coding
                int nFC = rs.getString("Fatal_Coding") == null ? 0 : rs.getInt("Fatal_Coding");
                vecItem.setCell(13, (nFC == 0) ? "" : Integer.toString(nFC));
                int nSC = rs.getString("Serious_Coding") == null ? 0 : rs.getInt("Serious_Coding");
                vecItem.setCell(14, (nSC == 0) ? "" : Integer.toString(nSC));
                int nMC = rs.getString("Medium_Coding") == null ? 0 : rs.getInt("Medium_Coding");
                vecItem.setCell(15, (nMC == 0) ? "" : Integer.toString(nMC));
                int nCC = rs.getString("Cosmetic_Coding") == null ? 0 : rs.getInt("Cosmetic_Coding");
                vecItem.setCell(16, (nCC == 0) ? "" : Integer.toString(nCC));
                int nWC = nFC * 10 + nSC * 5 + nMC * 3 + nCC * 1;
                vecItem.setCell(17, (nWC == 0) ? "" : Integer.toString(nWC));

                //Other
                int nFO = rs.getString("Fatal_Other") == null ? 0 : rs.getInt("Fatal_Other");
                vecItem.setCell(18, (nFO == 0) ? "" : Integer.toString(nFO));
                int nSO = rs.getString("Serious_Other") == null ? 0 : rs.getInt("Serious_Other");
                vecItem.setCell(19, (nSO == 0) ? "" : Integer.toString(nSO));
                int nMO = rs.getString("Medium_Other") == null ? 0 : rs.getInt("Medium_Other");
                vecItem.setCell(20, (nMO == 0) ? "" : Integer.toString(nMO));
                int nCO = rs.getString("Cosmetic_Other") == null ? 0 : rs.getInt("Cosmetic_Other");
                vecItem.setCell(21, (nCO == 0) ? "" : Integer.toString(nCO));
                int nWO = nFO * 10 + nSO * 5 + nMO * 3 + nCO * 1;
                vecItem.setCell(22, (nWO == 0) ? "" : Integer.toString(nWO));

                mtxResult.addRow(vecItem);
            }//end while
            if (DMS.DEBUG)
                logger.info("modelhandlers.SearchWeeklyReportHandler.getSubReportByProcess(): mtxResult.getNumberOfRows() = " + mtxResult.getNumberOfRows());

            rs.close();
            prepStmt.close();

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (mtxResult.getNumberOfRows() > 0) {
                //Add a TOTAL line at the end of table
                final int SHIFT = 3;
                int nSize = mtxResult.getNumberOfCols() - SHIFT;        //from PlannedDefect to the end.
                float[] arrTotal = new float[nSize];
                for (int i = 0; i < nSize; i++) {
                    arrTotal[i] = 0;
                }

                for (int c = 0; c < nSize; c++) {
                    for (int r = 0; r < mtxResult.getNumberOfRows(); r++) {
                        float fItem = ("".equals(mtxResult.getCell(r, c + SHIFT))) ? 0 : Float.parseFloat(mtxResult.getCell(r, c + SHIFT));
                        arrTotal[c] += fItem;
                    }
                }

                StringVector vecTotal = new StringVector(mtxResult.getNumberOfCols());
                vecTotal.setCell(0, "2");
                vecTotal.setCell(1, "<B>All work products</B>");
                vecTotal.setCell(2, "<B>All modules</B>");
                for (int i = SHIFT; i < mtxResult.getNumberOfCols(); i++) {
                    DecimalFormat dfTotal = new DecimalFormat("0.##");
                    String strTotal = dfTotal.format(arrTotal[i - SHIFT]);
                    vecTotal.setCell(i, "<B>" + (("0".equals(strTotal)) ? "" : strTotal) + "</B>");
                }
                mtxResult.addRow(vecTotal);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in ReportSummaryBO.getSubReportByProcess(): " + ex.getMessage());
        }
        catch (Exception e) {
            logger.error("Exception occurs in ReportSummaryBO.getSubReportByProcess(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ReportSummaryBO.getSubReportByProcess(): ");
        }

        return mtxResult;
    }


    private void setDateConstraint(String strFromDate, String strToDate) {
        strFilter = "";
        strForFixed = "";
        strFromCreateDate = "";
        strToCreateDate = "";
        strFromCloseDate = "";
        strToCloseDate = "";

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
        //strFilter += " AND ds_id < 5 "; // exclude defect with status Accepted or Cancelled
        strFilter += " AND ds_id <> 6 "; // exclude defect with status Accepted or Cancelled
    }
}