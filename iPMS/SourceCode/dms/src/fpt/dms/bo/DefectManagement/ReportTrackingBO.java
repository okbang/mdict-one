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
 * Title:        ReportTrackingBO.java
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


public class ReportTrackingBO {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    //private Connection con = null;

    private static Logger logger = Logger.getLogger(QueryBO.class.getName());

    public ReportTrackingBO() {

    }

    public ReportWeeklyBean getTrackingReport(int nProjectID, String strFromDate, String strToDate, int nReport) throws SQLException, Exception {
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

            //        SELECT BY WORK PRODUCT
            StringBuffer strSQLForWP = new StringBuffer();
            strSQLForWP.append(" SELECT WPName, ModuleName, ");
            strSQLForWP.append(" Fatal_Review, Serious_Review, Medium_Review, Cosmetic_Review,");
            strSQLForWP.append(" Fatal_Testing, Serious_Testing, Medium_Testing, Cosmetic_Testing,");
            strSQLForWP.append(" Fatal_Fixed, Serious_Fixed, Medium_Fixed, Cosmetic_Fixed,");
            strSQLForWP.append(" Fatal_Other, Serious_Other, Medium_Other, Cosmetic_Other");
            strSQLForWP.append(" FROM ");

            //WORK PRODUCT NAME
            strSQLForWP.append(" (SELECT D.wp_id, WP.Name as WPName, 'ZZZ' as ModuleName"); //set as ZZZ to order at the bottom of resultset
            strSQLForWP.append(" FROM defect D, WorkProduct WP");
            strSQLForWP.append(" WHERE project_id = " + nProjectID + " AND D.wp_id = WP.wp_id ");
            strSQLForWP.append(" AND D.module_id = 0");

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
            strSQLForWP.append(" GROUP BY D.wp_id, WP.Name)  T0, ");

            //FATAL REVIEW
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Fatal_Review ");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 1");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id <= 29 AND qa_id >=20 AND qa_id <>22 ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T1_Review,");

            //SERIOUS REVIEW
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Serious_Review ");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 2");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id <= 29 AND qa_id >=20 AND qa_id <>22 ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T2_Review,");

            //MEDIUM REVIEW
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Medium_Review");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 3");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id <= 29 AND qa_id >=20 AND qa_id <>22 ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T3_Review,");

            //COSMETIC REVIEW
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Cosmetic_Review");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 4");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id <= 29 AND qa_id >=20 AND qa_id <>22 ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T4_Review,");

            //FATAL TESTING
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Fatal_Testing ");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 1");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id <= 19 AND qa_id >=10 AND qa_id NOT IN (13,15) ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T1_Testing,");

            //SERIOUS TESTING
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Serious_Testing ");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 2");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id <= 19 AND qa_id >=10 AND qa_id NOT IN (13,15) ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T2_Testing,");

            //MEDIUM TESTING
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Medium_Testing ");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 3");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id <= 19 AND qa_id >=10 AND qa_id NOT IN (13,15) ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T3_Testing,");

            //COSMETIC TESTING
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Cosmetic_Testing ");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 4");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id <= 19 AND qa_id >=10 AND qa_id NOT IN (13,15) ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T4_Testing, ");

            //FATAL FIXED
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Fatal_Fixed ");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 1");
            strSQLForWP.append(" AND project_id = " + nProjectID + "  AND qa_id NOT IN (13,15,22)");
            strSQLForWP.append(" AND DS_ID IN (4, 5) ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strForFixed);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T1_Fixed,");

            //SERIOUS FIXED
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Serious_Fixed ");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 2");
            strSQLForWP.append(" AND project_id = " + nProjectID + "  AND qa_id NOT IN (13,15,22)");
            strSQLForWP.append(" AND DS_ID IN (4, 5) ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strForFixed);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T2_Fixed,");

            //MEDIUM FIXED
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Medium_Fixed ");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 3");
            strSQLForWP.append(" AND project_id = " + nProjectID + "  AND qa_id NOT IN (13,15,22)");
            strSQLForWP.append(" AND DS_ID IN (4, 5) ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strForFixed);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T3_Fixed,");

            //COSMETIC FIXED
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Cosmetic_Fixed");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 4");
            strSQLForWP.append(" AND project_id = " + nProjectID + "  AND qa_id NOT IN (13,15,22)");
            strSQLForWP.append(" AND DS_ID IN (4, 5) ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strForFixed);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T4_Fixed,");

            //FATAL OTHER
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Fatal_Other ");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 1");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id >=30 ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T1_Other,");

            //SERIOUS OTHER
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Serious_Other");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 2");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id >=30 ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T2_Other,");

            //MEDIUM OTHER
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Medium_Other");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 3");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id >=30 ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T3_Other,");

            //COSMETIC OTHER
            strSQLForWP.append(" (SELECT wp_id, Count(wp_id) AS Cosmetic_Other");
            strSQLForWP.append(" FROM defect D");
            strSQLForWP.append(" WHERE defs_id = 4");
            strSQLForWP.append(" AND project_id = " + nProjectID + " AND qa_id >=30 ");
            strSQLForWP.append(" AND D.module_id = 0");
            strSQLForWP.append(strFilter);
            strSQLForWP.append(strExclude);
            strSQLForWP.append(" GROUP BY wp_id) T4_Other");

            strSQLForWP.append(" WHERE ");

            //JOIN TABLES TOGETHER
            strSQLForWP.append(" T0.wp_id = T1_Review.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T2_Review.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T3_Review.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T4_Review.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T1_Testing.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T2_Testing.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T3_Testing.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T4_Testing.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T1_Fixed.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T2_Fixed.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T3_Fixed.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T4_Fixed.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T1_Other.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T2_Other.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T3_Other.wp_id(+)");
            strSQLForWP.append(" AND T0.wp_id = T4_Other.wp_id(+)");

            //SELECT BY MODULE

            StringBuffer strSQLForModule = new StringBuffer();
            strSQLForModule.append(" SELECT WPName, ModuleName,");
            strSQLForModule.append(" Fatal_Review, Serious_Review, Medium_Review, Cosmetic_Review,");
            strSQLForModule.append(" Fatal_Testing, Serious_Testing, Medium_Testing, Cosmetic_Testing,");
            strSQLForModule.append(" Fatal_Fixed, Serious_Fixed, Medium_Fixed, Cosmetic_Fixed,");
            strSQLForModule.append(" Fatal_Other, Serious_Other, Medium_Other, Cosmetic_Other");
            strSQLForModule.append(" FROM ");

            //Module Name
            strSQLForModule.append(" (SELECT D.module_id, M.name as ModuleName, WP.name as WPName, WP.wp_id");
            strSQLForModule.append(" FROM defect D, module M, WorkProduct WP");
            strSQLForModule.append(" WHERE D.project_id = " + nProjectID);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id");
            strSQLForModule.append(" AND D.module_id = M.module_id ");
            //      strSQLForModule.append(" AND WP.wp_id = M.wp_id");  //Thaison - Oct 23, 2002
            strSQLForModule.append(" AND D.module_id > 0");

            if (!strFromDate.equals("") && !strToDate.equals("")) {
                strSQLForModule.append(" AND ((" + strFromCreateDate + " AND " + strToCreateDate + ")");
                strSQLForModule.append(" OR (" + strFromCloseDate + " AND " + strToCloseDate + "))");
            }
            else if (!strFromDate.equals("")) {
                strSQLForModule.append(" AND ((" + strFromCreateDate + ")");
                strSQLForModule.append(" OR (" + strFromCloseDate + "))");
            }
            else if (!strToDate.equals("")) {
                strSQLForModule.append(" AND ((" + strToCreateDate + ")");
                strSQLForModule.append(" OR (" + strToCloseDate + "))");
            }

            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY WP.name, D.module_id, M.name, WP.wp_id)  T0, ");

            //Fatal Review
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Fatal_Review ");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 1 AND");
            strSQLForModule.append(" D.project_id = " + nProjectID + " AND QA_ID <= 29 AND QA_ID >=20 AND QA_ID <>22 ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T1_Review,");

            //Serious Review
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Serious_Review ");
            strSQLForModule.append(" FROM defect D, workproduct WP");
            strSQLForModule.append(" WHERE DEFS_ID = 2 AND");
            strSQLForModule.append(" D.project_id = " + nProjectID + " AND QA_ID <= 29 AND QA_ID >=20 AND QA_ID <>22 ");
            strSQLForModule.append(" AND module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T2_Review,");

            //Medium Review
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Medium_Review");
            strSQLForModule.append(" FROM defect D, workproduct WP");
            strSQLForModule.append(" WHERE DEFS_ID = 3 AND");
            strSQLForModule.append(" project_id = " + nProjectID + " AND QA_ID <= 29 AND QA_ID >=20 AND QA_ID <>22 ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T3_Review,");

            //Cosmetic Review
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Cosmetic_Review");
            strSQLForModule.append(" FROM defect D, workproduct WP");
            strSQLForModule.append(" WHERE DEFS_ID = 4 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + " AND QA_ID <= 29 AND QA_ID >=20 AND QA_ID <>22 ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T4_Review,");

            //Fatal Testing
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Fatal_Testing ");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 1 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + " AND QA_ID <= 19 AND QA_ID >=10 AND QA_ID NOT IN (13,15) ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T1_Testing,");

            //Serious Testing
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Serious_Testing ");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 2 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + " AND QA_ID <= 19 AND QA_ID >=10 AND QA_ID NOT IN (13,15) ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T2_Testing,");

            //Medium Testing
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Medium_Testing ");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 3 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + " AND QA_ID <= 19 AND QA_ID >=10 AND QA_ID NOT IN (13,15) ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T3_Testing,");

            //Cosmetic Testing
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Cosmetic_Testing ");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 4 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + " AND QA_ID <= 19 AND QA_ID >=10 AND QA_ID NOT IN (13,15) ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T4_Testing, ");

            //Fatal Fixed
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Fatal_Fixed ");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 1 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + "  AND QA_ID NOT IN (13,15,22)");
            strSQLForModule.append(" AND DS_ID IN (4, 5) ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strForFixed);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T1_Fixed,");

            //Serious Fixed
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Serious_Fixed ");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 2 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + "  AND QA_ID NOT IN (13,15,22)");
            strSQLForModule.append(" AND DS_ID IN (4, 5) ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strForFixed);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T2_Fixed,");

            //Medium Fixed
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Medium_Fixed ");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 3 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + "  AND QA_ID NOT IN (13,15,22)");
            strSQLForModule.append(" AND DS_ID IN (4, 5) ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strForFixed);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T3_Fixed,");

            //Cosmetic Fixed
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Cosmetic_Fixed");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 4 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + "  AND QA_ID NOT IN (13,15,22)");
            strSQLForModule.append(" AND DS_ID IN (4, 5) ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strForFixed);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T4_Fixed,");

            //Fatal Other
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Fatal_Other ");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 1 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + " AND QA_ID >=30 ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T1_Other,");

            //Serious Other
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Serious_Other");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 2 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + " AND QA_ID >=30 ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T2_Other,");

            //Medium Other
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Medium_Other");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 3 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + " AND QA_ID >=30 ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T3_Other,");

            //Cosmetic Other
            strSQLForModule.append(" (SELECT module_id, WP.wp_id, COUNT(module_id) AS Cosmetic_Other");
            strSQLForModule.append(" FROM defect D, workproduct WP ");
            strSQLForModule.append(" WHERE DEFS_ID = 4 AND");
            strSQLForModule.append(" Project_ID = " + nProjectID + " AND QA_ID >=30 ");
            strSQLForModule.append(" AND D.module_id > 0");
            strSQLForModule.append(strFilter);
            strSQLForModule.append(" AND D.wp_id = WP.wp_id(+)");
            strSQLForModule.append(strExclude);
            strSQLForModule.append(" GROUP BY module_id, WP.wp_id) T4_Other");

            //Join tables together
            strSQLForModule.append(" WHERE ");
            strSQLForModule.append(" T0.module_id = T1_Review.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T1_Review.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T2_Review.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T2_Review.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T3_Review.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T3_Review.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T4_Review.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T4_Review.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T1_Testing.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T1_Testing.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T2_Testing.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T2_Testing.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T3_Testing.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T3_Testing.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T4_Testing.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T4_Testing.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T1_Fixed.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T1_Fixed.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T2_Fixed.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T2_Fixed.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T3_Fixed.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T3_Fixed.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T4_Fixed.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T4_Fixed.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T1_Other.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T1_Other.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T2_Other.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T2_Other.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T3_Other.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T3_Other.wp_id(+)");

            strSQLForModule.append(" AND T0.module_id = T4_Other.module_id(+)");
            strSQLForModule.append(" AND T0.wp_id = T4_Other.wp_id(+)");
            /**************************************************************/

            strSQL.append(strSQLForWP + " UNION " + strSQLForModule);
            strSQL.append(" ORDER BY WPName, ModuleName");
            if (DMS.DEBUG)
                logger.debug("Tracking SQL = " + strSQL.toString());

            StringVector svTemp = new StringVector(25);

            //Add First Row:
            svTemp.setCell(0, strFromDate);
            svTemp.setCell(1, strToDate);
            svTemp.setCell(2, Integer.toString(nReport));
            smResult.addRow(svTemp);

            ds = conPool.getDS();
            con = ds.getConnection();
            prepStmt = con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();

            int nIndex = 0;
            String strPrevItem = "";
            boolean bInserted = false;

            while (rs.next()) {
                String strItem = rs.getString("WPName");

                if (strPrevItem.equals(strItem) && !bInserted) {
                    //Insert a subtotal row with empty counted-defect values
                    //(Sub-total is total of all modules for each work product)
                    StringVector vecSubTotal = new StringVector(25);
                    vecSubTotal.setCell(0, strFromDate);
                    vecSubTotal.setCell(1, strToDate);
                    vecSubTotal.setCell(2, Integer.toString(nReport));
                    vecSubTotal.setCell(3, strItem);        //WorkProduct

                    vecSubTotal.setCell(4, "0");
                    vecSubTotal.setCell(5, "0");
                    vecSubTotal.setCell(6, "0");
                    vecSubTotal.setCell(7, "0");
                    vecSubTotal.setCell(8, "0");

                    vecSubTotal.setCell(9, "0");
                    vecSubTotal.setCell(10, "0");
                    vecSubTotal.setCell(11, "0");
                    vecSubTotal.setCell(12, "0");
                    vecSubTotal.setCell(13, "0");

                    vecSubTotal.setCell(14, "0");
                    vecSubTotal.setCell(15, "0");
                    vecSubTotal.setCell(16, "0");
                    vecSubTotal.setCell(17, "0");
                    vecSubTotal.setCell(18, "0");

                    vecSubTotal.setCell(19, "0");
                    vecSubTotal.setCell(20, "0");
                    vecSubTotal.setCell(21, "0");
                    vecSubTotal.setCell(22, "0");
                    vecSubTotal.setCell(23, "0");

                    vecSubTotal.setCell(24, DMS.ALL_MODULES);       //for all module

                    smResult.insertRow(nIndex, vecSubTotal);
                    nIndex++;

                    bInserted = true;
                }
                else if (!strPrevItem.equals(strItem)) {
                    bInserted = false;
                }

                strPrevItem = strItem;

                //WorkProduct Name
                svTemp.setCell(3, strItem);

                //Module Name
                String strModule = ("ZZZ".equals(rs.getString("ModuleName"))) ? "" : rs.getString("ModuleName");
                svTemp.setCell(24, strModule);

                //REVIEW
                int nFatal = (rs.getString("Fatal_Review") != null) ? rs.getInt("Fatal_Review") : 0;
                svTemp.setCell(4, (nFatal == 0) ? "0" : Integer.toString(nFatal));

                int nSerious = (rs.getString("Serious_Review") != null) ? rs.getInt("Serious_Review") : 0;
                svTemp.setCell(5, (nSerious == 0) ? "0" : Integer.toString(nSerious));

                int nMedium = (rs.getString("Medium_Review") != null) ? rs.getInt("Medium_Review") : 0;
                svTemp.setCell(6, (nMedium == 0) ? "0" : Integer.toString(nMedium));

                int nCosmetic = (rs.getString("Cosmetic_Review") != null) ? rs.getInt("Cosmetic_Review") : 0;
                svTemp.setCell(7, (nCosmetic == 0) ? "0" : Integer.toString(nCosmetic));

                int nWeighted = nFatal * 10 + nSerious * 5 + nMedium * 3 + nCosmetic * 1;
                svTemp.setCell(8, (nWeighted == 0) ? "0" : Integer.toString(nWeighted));

                //TESTING
                nFatal = (rs.getString("Fatal_Testing") != null) ? rs.getInt("Fatal_Testing") : 0;
                svTemp.setCell(9, (nFatal == 0) ? "0" : Integer.toString(nFatal));

                nSerious = (rs.getString("Serious_Testing") != null) ? rs.getInt("Serious_Testing") : 0;
                svTemp.setCell(10, (nSerious == 0) ? "0" : Integer.toString(nSerious));

                nMedium = (rs.getString("Medium_Testing") != null) ? rs.getInt("Medium_Testing") : 0;
                svTemp.setCell(11, (nMedium == 0) ? "0" : Integer.toString(nMedium));

                nCosmetic = (rs.getString("Cosmetic_Testing") != null) ? rs.getInt("Cosmetic_Testing") : 0;
                svTemp.setCell(12, (nCosmetic == 0) ? "0" : Integer.toString(nCosmetic));

                nWeighted = nFatal * 10 + nSerious * 5 + nMedium * 3 + nCosmetic * 1;
                svTemp.setCell(13, (nWeighted == 0) ? "0" : Integer.toString(nWeighted));

                //OTHER
                nFatal = (rs.getString("Fatal_Other") != null) ? rs.getInt("Fatal_Other") : 0;
                svTemp.setCell(14, (nFatal == 0) ? "0" : Integer.toString(nFatal));

                nSerious = (rs.getString("Serious_Other") != null) ? rs.getInt("Serious_Other") : 0;
                svTemp.setCell(15, (nSerious == 0) ? "0" : Integer.toString(nSerious));

                nMedium = (rs.getString("Medium_Other") != null) ? rs.getInt("Medium_Other") : 0;
                svTemp.setCell(16, (nMedium == 0) ? "0" : Integer.toString(nMedium));

                nCosmetic = (rs.getString("Cosmetic_Other") != null) ? rs.getInt("Cosmetic_Other") : 0;
                svTemp.setCell(17, (nCosmetic == 0) ? "0" : Integer.toString(nCosmetic));

                nWeighted = nFatal * 10 + nSerious * 5 + nMedium * 3 + nCosmetic * 1;
                svTemp.setCell(18, (nWeighted == 0) ? "0" : Integer.toString(nWeighted));

                //FIXED
                nFatal = (rs.getString("Fatal_Fixed") != null) ? rs.getInt("Fatal_Fixed") : 0;
                svTemp.setCell(19, (nFatal == 0) ? "0" : Integer.toString(nFatal));

                nSerious = (rs.getString("Serious_Fixed") != null) ? rs.getInt("Serious_Fixed") : 0;
                svTemp.setCell(20, (nSerious == 0) ? "0" : Integer.toString(nSerious));

                nMedium = (rs.getString("Medium_Fixed") != null) ? rs.getInt("Medium_Fixed") : 0;
                svTemp.setCell(21, (nMedium == 0) ? "0" : Integer.toString(nMedium));

                nCosmetic = (rs.getString("Cosmetic_Fixed") != null) ? rs.getInt("Cosmetic_Fixed") : 0;
                svTemp.setCell(22, (nCosmetic == 0) ? "0" : Integer.toString(nCosmetic));

                nWeighted = nFatal * 10 + nSerious * 5 + nMedium * 3 + nCosmetic * 1;
                svTemp.setCell(23, (nWeighted == 0) ? "0" : Integer.toString(nWeighted));

                smResult.addRow(svTemp);
                nIndex++;
            }

            rs.close();
            prepStmt.close();

            smResult = expandTrackingReport(smResult);
            beanReport.setWeeklyReportList(smResult);
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in ReportTrackingBO.getTrackingReport(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in ReportTrackingBO.getTrackingReport(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ReportTrackingBO.getTrackingReport(): ");
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////

        return beanReport;
    }

    public StringMatrix expandTrackingReport(StringMatrix smReport) throws SQLException, Exception {
        //Sub-total of all modules for each work product
        int nRow = 0;
        while (nRow < smReport.getNumberOfRows()) {
            String strWP = smReport.getCell(nRow, 3); //work product
            String strModule = smReport.getCell(nRow, 24); //module

            if (DMS.ALL_MODULES.equals(strModule)) {
                int nUpdatedRow = nRow; //keep this row to update later

                int[] arrSubTotal = new int[smReport.getNumberOfCols()];
                //init with zeros
                for (int nCol = 0; nCol < smReport.getNumberOfCols(); nCol++) {
                    arrSubTotal[nCol] = 0;
                }

                String strPrevWP = strWP;
                while (strPrevWP.equals(strWP)) {
                    //Review
                    arrSubTotal[4] += Integer.parseInt(smReport.getCell(nRow, 4));
                    arrSubTotal[5] += Integer.parseInt(smReport.getCell(nRow, 5));
                    arrSubTotal[6] += Integer.parseInt(smReport.getCell(nRow, 6));
                    arrSubTotal[7] += Integer.parseInt(smReport.getCell(nRow, 7));
                    arrSubTotal[8] += Integer.parseInt(smReport.getCell(nRow, 8));

                    //Testing
                    arrSubTotal[9] += Integer.parseInt(smReport.getCell(nRow, 9));
                    arrSubTotal[10] += Integer.parseInt(smReport.getCell(nRow, 10));
                    arrSubTotal[11] += Integer.parseInt(smReport.getCell(nRow, 11));
                    arrSubTotal[12] += Integer.parseInt(smReport.getCell(nRow, 12));
                    arrSubTotal[13] += Integer.parseInt(smReport.getCell(nRow, 13));

                    //Other
                    arrSubTotal[14] += Integer.parseInt(smReport.getCell(nRow, 14));
                    arrSubTotal[15] += Integer.parseInt(smReport.getCell(nRow, 15));
                    arrSubTotal[16] += Integer.parseInt(smReport.getCell(nRow, 16));
                    arrSubTotal[17] += Integer.parseInt(smReport.getCell(nRow, 17));
                    arrSubTotal[18] += Integer.parseInt(smReport.getCell(nRow, 18));

                    //Fixed
                    arrSubTotal[19] += Integer.parseInt(smReport.getCell(nRow, 19));
                    arrSubTotal[20] += Integer.parseInt(smReport.getCell(nRow, 20));
                    arrSubTotal[21] += Integer.parseInt(smReport.getCell(nRow, 21));
                    arrSubTotal[22] += Integer.parseInt(smReport.getCell(nRow, 22));
                    arrSubTotal[23] += Integer.parseInt(smReport.getCell(nRow, 23));

                    nRow++;
                    strWP = smReport.getCell(nRow, 3); //work product
                } //end while

                //Update the sub-total row.
                for (int nCol = 4; nCol < smReport.getNumberOfCols() - 1; nCol++) {
                    smReport.setCell(nUpdatedRow, nCol, (arrSubTotal[nCol] == 0) ? "" : ("<b>" + Integer.toString(arrSubTotal[nCol]) + "</b>"));
                }
            } //end if
            else
                nRow++;
        }
        /////////////////////////////////////////////////////////////// Thaison - Sep 23, 2002
        //Sum columns
        int[] arrTotal = new int[smReport.getNumberOfCols()];
        for (int i = 0; i < smReport.getNumberOfCols(); i++) {
            arrTotal[i] = 0;
        }
        for (int i = 0; i < smReport.getNumberOfRows(); i++) {
            if (DMS.ALL_MODULES.equals(smReport.getCell(i, 24))) {
                smReport.setCell(i, 24, "<B>" + "All modules" + "</B>");
                continue;
            }
            for (int j = 4; j < smReport.getNumberOfCols() - 1; j++) {
                if (!smReport.getCell(i, j).equals("")) {
                    arrTotal[j - 1] += Integer.parseInt(smReport.getCell(i, j));
                }
                //Thaison - Sep 23, 2002
                if ("0".equals(smReport.getCell(i, j)))
                    smReport.setCell(i, j, "");
            }
        }

        //TOTAL line at the end of table.
        StringVector vtTemp = new StringVector(smReport.getNumberOfCols());
        vtTemp.setCell(3, "<B>All work products</B>");
        for (int i = 4; i < smReport.getNumberOfCols(); i++) {
            if (arrTotal[i - 1] != 0)
                vtTemp.setCell(i, "<B>" + String.valueOf(arrTotal[i - 1]) + "</B>");
            else
                vtTemp.setCell(i, "");
        }
        vtTemp.setCell(24, "<B>All</B>");

        smReport.addRow(vtTemp);

        return smReport;
    }
}