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
 * Title:        ReportGeneralBO.java
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


public class ReportGeneralBO {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    //private Connection con = null;
    private static Logger logger = Logger.getLogger(QueryBO.class.getName());

    public ReportGeneralBO() {
    }

    public ReportWeeklyBean getGeneralReport(int nProjectID, String strFromDate, String strToDate, int nReport)
            throws SQLException, Exception {
        ReportWeeklyBean beanReport = new ReportWeeklyBean();
        Connection con = null;
        StringMatrix smResult = new StringMatrix();
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();
        try {
            ds = conPool.getDS();
            con = ds.getConnection();

            strSQL.append("SELECT D.defs_id, D.ds_id, COUNT(D.defect_id) AS Total");
            strSQL.append(" FROM defect D");
            strSQL.append(" WHERE D.project_id = " + nProjectID);
            if (!strFromDate.equals("")) {
                strSQL.append(" AND D.create_date >= TO_DATE('" + strFromDate + "', '" + DMS.DATE_FORMAT + "')");
            }
            if (!strToDate.equals("")) {
                strSQL.append(" AND D.create_date <= TO_DATE('" + strToDate + "', '" + DMS.DATE_FORMAT + "')");
            }
            strSQL.append(" GROUP BY D.defs_id, D.ds_id");
            strSQL.append(" ORDER BY D.defs_id, D.ds_id");
            if (DMS.DEBUG)
                logger.debug("strSQL = " + strSQL.toString());

            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();

            StringVector svFatal = new StringVector(11);
            StringVector svSerious = new StringVector(11);
            StringVector svMedium = new StringVector(11);
            StringVector svCosmetic = new StringVector(11);

            svFatal.setCell(0, strFromDate);
            svFatal.setCell(1, strToDate);
            svFatal.setCell(2, Integer.toString(nReport));
            svFatal.setCell(3, "Fatal");

            svSerious.setCell(0, strFromDate);
            svSerious.setCell(1, strToDate);
            svSerious.setCell(2, Integer.toString(nReport));
            svSerious.setCell(3, "Serious");

            svMedium.setCell(0, strFromDate);
            svMedium.setCell(1, strToDate);
            svMedium.setCell(2, Integer.toString(nReport));
            svMedium.setCell(3, "Medium");

            svCosmetic.setCell(0, strFromDate);
            svCosmetic.setCell(1, strToDate);
            svCosmetic.setCell(2, Integer.toString(nReport));
            svCosmetic.setCell(3, "Cosmetic");

            int nFError = 0, nFAssigned = 0, nFPending = 0, nFTested = 0, nFAccepted = 0, nFCancelled = 0, nF = 0;
            int nSError = 0, nSAssigned = 0, nSPending = 0, nSTested = 0, nSAccepted = 0, nSCancelled = 0, nS = 0;
            int nMError = 0, nMAssigned = 0, nMPending = 0, nMTested = 0, nMAccepted = 0, nMCancelled = 0, nM = 0;
            int nCError = 0, nCAssigned = 0, nCPending = 0, nCTested = 0, nCAccepted = 0, nCCancelled = 0, nC = 0;

            while (rs.next()) {
                String strStatusID = (rs.getString("ds_id") == null) ? "0" : rs.getString("ds_id");
                String strSeverityID = (rs.getString("defs_id") == null) ? "0" : rs.getString("defs_id");
                int nStatusID = Integer.parseInt(strStatusID);

                if ("1".equals(strSeverityID))  //FATAL
                {
                    switch (nStatusID) {
                        case 1:
                            nFError += rs.getInt("Total");
                            svFatal.setCell(4, Integer.toString(nFError));
                            break;
                        case 2:
                            nFAssigned += rs.getInt("Total");
                            svFatal.setCell(5, Integer.toString(nFAssigned));
                            break;
                        case 3:
                            nFPending += rs.getInt("Total");
                            svFatal.setCell(6, Integer.toString(nFPending));
                            break;
                        case 4:
                            nFTested += rs.getInt("Total");
                            svFatal.setCell(7, Integer.toString(nFTested));
                            break;
                        case 5:
                            nFAccepted += rs.getInt("Total");
                            svFatal.setCell(8, Integer.toString(nFAccepted));
                            break;
                        case 6:
                            nFCancelled += rs.getInt("Total");
                            svFatal.setCell(9, Integer.toString(nFCancelled));
                            break;
                    }//end switch

                    nF = nFError + nFAssigned + nFPending + nFTested + nFAccepted + nFCancelled;
                    svFatal.setCell(10, Integer.toString(nF));
                }//end if

                else if ("2".equals(strSeverityID)) //SERIOUS
                {
                    switch (nStatusID) {
                        case 1:
                            nSError += rs.getInt("Total");
                            svSerious.setCell(4, Integer.toString(nSError));
                            break;
                        case 2:
                            nSAssigned += rs.getInt("Total");
                            svSerious.setCell(5, Integer.toString(nSAssigned));
                            break;
                        case 3:
                            nSPending += rs.getInt("Total");
                            svSerious.setCell(6, Integer.toString(nSPending));
                            break;
                        case 4:
                            nSTested += rs.getInt("Total");
                            svSerious.setCell(7, Integer.toString(nSTested));
                            break;
                        case 5:
                            nSAccepted += rs.getInt("Total");
                            svSerious.setCell(8, Integer.toString(nSAccepted));
                            break;
                        case 6:
                            nSCancelled += rs.getInt("Total");
                            svSerious.setCell(9, Integer.toString(nSCancelled));
                            break;
                    }//end switch

                    nS = nSError + nSAssigned + nSPending + nSTested + nSAccepted + nSCancelled;
                    svSerious.setCell(10, Integer.toString(nS));
                }//end if

                else if ("3".equals(strSeverityID)) //MEDIUM
                {
                    switch (nStatusID) {
                        case 1:
                            nMError += rs.getInt("Total");
                            svMedium.setCell(4, Integer.toString(nMError));
                            break;
                        case 2:
                            nMAssigned += rs.getInt("Total");
                            svMedium.setCell(5, Integer.toString(nMAssigned));
                            break;
                        case 3:
                            nMPending += rs.getInt("Total");
                            svMedium.setCell(6, Integer.toString(nMPending));
                            break;
                        case 4:
                            nMTested += rs.getInt("Total");
                            svMedium.setCell(7, Integer.toString(nMTested));
                            break;
                        case 5:
                            nMAccepted += rs.getInt("Total");
                            svMedium.setCell(8, Integer.toString(nMAccepted));
                            break;
                        case 6:
                            nMCancelled += rs.getInt("Total");
                            svMedium.setCell(9, Integer.toString(nMCancelled));
                            break;
                    }//end switch

                    nM = nMError + nMAssigned + nMPending + nMTested + nMAccepted + nMCancelled;
                    svMedium.setCell(10, Integer.toString(nM));
                }//end if

                else if ("4".equals(strSeverityID)) //COSMETIC
                {
                    switch (nStatusID) {
                        case 1:
                            nCError += rs.getInt("Total");
                            svCosmetic.setCell(4, Integer.toString(nCError));
                            break;
                        case 2:
                            nCAssigned += rs.getInt("Total");
                            svCosmetic.setCell(5, Integer.toString(nCAssigned));
                            break;
                        case 3:
                            nCPending += rs.getInt("Total");
                            svCosmetic.setCell(6, Integer.toString(nCPending));
                            break;
                        case 4:
                            nCTested += rs.getInt("Total");
                            svCosmetic.setCell(7, Integer.toString(nCTested));
                            break;
                        case 5:
                            nCAccepted += rs.getInt("Total");
                            svCosmetic.setCell(8, Integer.toString(nCAccepted));
                            break;
                        case 6:
                            nCCancelled += rs.getInt("Total");
                            svCosmetic.setCell(9, Integer.toString(nCCancelled));
                            break;
                    }//end switch

                    nC = nCError + nCAssigned + nCPending + nCTested + nCAccepted + nCCancelled;
                    svCosmetic.setCell(10, Integer.toString(nC));
                }//end if
            }//end while

            smResult.addRow(svFatal);
            smResult.addRow(svSerious);
            smResult.addRow(svMedium);
            smResult.addRow(svCosmetic);

            rs.close();
            prepStmt.close();

            smResult = expandReport(smResult);
            beanReport.setWeeklyReportList(smResult);
        }// end try
        catch (SQLException ex) {
            logger.error("SQLException occurs in ReportGeneralBO.getGeneralReport(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in ReportGeneralBO.getGeneralReport(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ReportGeneralBO.getGeneralReport(): ");
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

            int[] arrWeighted = new int[nSize];
            int nF, nS, nM, nC;
            for (int i = 0; i < nSize; i++) {
                nF = ("".equals(smReport.getCell(0, i + SHIFT))) ? 0 : Integer.parseInt(smReport.getCell(0, i + SHIFT));
                nS = ("".equals(smReport.getCell(1, i + SHIFT))) ? 0 : Integer.parseInt(smReport.getCell(1, i + SHIFT));
                nM = ("".equals(smReport.getCell(2, i + SHIFT))) ? 0 : Integer.parseInt(smReport.getCell(2, i + SHIFT));
                nC = ("".equals(smReport.getCell(3, i + SHIFT))) ? 0 : Integer.parseInt(smReport.getCell(3, i + SHIFT));
                arrWeighted[i] = nF * 10 + nS * 5 + nM * 3 + nC * 1;
            }

            StringVector vecTotal = new StringVector(smReport.getNumberOfCols());
            vecTotal.setCell(0, smReport.getCell(0, 0));
            vecTotal.setCell(1, smReport.getCell(0, 1));
            vecTotal.setCell(2, smReport.getCell(0, 2));
            vecTotal.setCell(3, "<B>Total</B>");
            for (int i = SHIFT; i < smReport.getNumberOfCols(); i++) {
                String strTotal = Integer.toString(arrTotal[i - SHIFT]);
                vecTotal.setCell(i, "<B>" + (("0".equals(strTotal)) ? "" : strTotal) + "</B>");
            }
            smReport.addRow(vecTotal);
            vecTotal.setCell(0, smReport.getCell(0, 0));
            vecTotal.setCell(1, smReport.getCell(0, 1));
            vecTotal.setCell(2, smReport.getCell(0, 2));
            vecTotal.setCell(3, "<B>Total Wdef</B>");
            for (int i = SHIFT; i < smReport.getNumberOfCols(); i++) {
                String strWeighted = Integer.toString(arrWeighted[i - SHIFT]);
                vecTotal.setCell(i, "<B>" + (("0".equals(strWeighted)) ? "" : strWeighted) + "</B>");
            }
            smReport.addRow(vecTotal);
        }
        return smReport;
    }
}