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
 * Title:        ReportDefectTypeBO.java
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


public class ReportDefectByTestCaseIdBO {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    //private Connection con = null;

    private static Logger logger = Logger.getLogger(QueryBO.class.getName());

    public ReportDefectByTestCaseIdBO() {

    }

    public ReportWeeklyBean getDefectTestCaseIdBOReport(int nProjectID, String strFromDate, String strToDate, int nReport) throws SQLException, Exception {
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
            strFilter += " AND QA_ID > 9 AND QA_ID < 18 "; // only defect found by testing.
            
			strSQL.append("SELECT T0.TEST_CASE, Fatal, Serious, Medium, Cosmetic");
			strSQL.append(" FROM (SELECT (case when TEST_CASE is not null then TEST_CASE ELSE 'Other' end) AS TEST_CASE FROM Defect WHERE PROJECT_ID = " + nProjectID  +" "+  strFilter + " GROUP BY TEST_CASE)  T0, ");
			strSQL.append(" (SELECT (case when TEST_CASE is not null then TEST_CASE ELSE 'Other' end) AS TEST_CASE, Count(*) AS Fatal ");
			strSQL.append(" FROM Defect  WHERE DEFS_ID = 1 AND");
			strSQL.append(" Project_ID = " + nProjectID);
			strSQL.append(strFilter);
			strSQL.append(" GROUP BY TEST_CASE) T1,");
			strSQL.append(" (SELECT (case when TEST_CASE is not null then TEST_CASE ELSE 'Other' end) AS TEST_CASE, Count(*) AS Serious ");
			strSQL.append(" FROM Defect  WHERE DEFS_ID = 2 AND");
			strSQL.append(" Project_ID = " + nProjectID);
			strSQL.append(strFilter);
			strSQL.append(" GROUP BY TEST_CASE) T2,");
			strSQL.append(" (SELECT (case when TEST_CASE is not null then TEST_CASE ELSE 'Other' end) AS TEST_CASE, Count(*) AS Medium ");
			strSQL.append(" FROM Defect  WHERE DEFS_ID = 3 AND");
			strSQL.append(" Project_ID = " + nProjectID);
			strSQL.append(strFilter);
			strSQL.append(" GROUP BY TEST_CASE) T3,");
			strSQL.append(" (SELECT (case when TEST_CASE is not null then TEST_CASE ELSE 'Other' end) AS TEST_CASE, Count(*) AS Cosmetic ");
			strSQL.append(" FROM Defect  WHERE DEFS_ID = 4 AND");
			strSQL.append(" Project_ID = " + nProjectID);
			strSQL.append(strFilter);
			strSQL.append(" GROUP BY TEST_CASE) T4");
			strSQL.append(" WHERE ");
			strSQL.append(" T0.TEST_CASE = T1.TEST_CASE(+)");
			strSQL.append(" AND T0.TEST_CASE = T2.TEST_CASE(+)");
			strSQL.append(" AND T0.TEST_CASE = T3.TEST_CASE(+)");
			strSQL.append(" AND T0.TEST_CASE = T4.TEST_CASE(+)");
            
            //Order by Defect Type that appeared in screen
            strSQL.append(" ORDER BY UPPER(T0.TEST_CASE) ");
            if (DMS.DEBUG)
                logger.debug("strSQL = " + strSQL.toString());

            ds = conPool.getDS();
            con = ds.getConnection();

            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();

            StringVector svTemp = new StringVector(10);
            //Add First Row:
            svTemp.setCell(0, strFromDate);
            svTemp.setCell(1, strToDate);
            svTemp.setCell(2, Integer.toString(nReport));
            smResult.addRow(svTemp);

            while (rs.next()) {
            	if(rs.getString("test_case")==null){
					svTemp.setCell(3, "other");
            	}else{
					svTemp.setCell(3, String.valueOf(rs.getString("test_case")));	
            	}
                int nFatal = (rs.getString("Fatal") != null) ? rs.getInt("Fatal") : 0;
                svTemp.setCell(4, (nFatal == 0) ? "" : Integer.toString(nFatal));

                int nSerious = (rs.getString("Serious") != null) ? rs.getInt("Serious") : 0;
                svTemp.setCell(5, (nSerious == 0) ? "" : Integer.toString(nSerious));

                int nMedium = (rs.getString("Medium") != null) ? rs.getInt("Medium") : 0;
                svTemp.setCell(6, (nMedium == 0) ? "" : Integer.toString(nMedium));

                int nCosmetic = (rs.getString("Cosmetic") != null) ? rs.getInt("Cosmetic") : 0;
                svTemp.setCell(7, (nCosmetic == 0) ? "" : Integer.toString(nCosmetic));

                int nTotal = nFatal + nSerious + nMedium + nCosmetic;
                svTemp.setCell(8, (nTotal == 0) ? "" : Integer.toString(nTotal));

                int nWeighted = nFatal * 10 + nSerious * 5 + nMedium * 3 + nCosmetic * 1;
                svTemp.setCell(9, (nWeighted == 0) ? "" : Integer.toString(nWeighted));

                smResult.addRow(svTemp);
            	
            }

            rs.close();
            prepStmt.close();

            smResult = expandReport(smResult);
            if (smResult.getNumberOfRows() == 0) {
                smResult.addRow(svTemp);
            }
            beanReport.setWeeklyReportList(smResult);
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in ReportDefectTypeBO.getDefectTypeReport(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in ReportDefectTypeBO.getDefectTypeReport(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ReportDefectTypeBO.getDefectTypeReport(): ");
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
                vecTotal.setCell(i, "<B>" + (("0".equals(strTotal)) ? "" : strTotal) + "</B>");
            }
            smReport.addRow(vecTotal);
        }
        return smReport;
    }
}