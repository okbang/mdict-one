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
 * Title:        DefectPlanBO.java
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      FPT - FSOFT
 * Author:       Nguyen Thai Son
 * Create date:  November 01, 2002
 * Modified date:
 */

import fpt.dms.constant.DMS;
import fpt.dms.framework.connection.WSConnectionPooling;
import fpt.dms.framework.util.StringUtil.StringMatrix;
import fpt.dms.framework.util.StringUtil.StringVector;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DefectPlanBO {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    //private Connection con = null;

    private static Logger logger = Logger.getLogger(DefectPlanBO.class.getName());
    private int NUMBER_OF_QC = 7;

    public DefectPlanBO() {
    }

    public StringMatrix getPlannedDefectList(int nProjectID) throws SQLException, Exception {
        StringMatrix smResult = new StringMatrix();
        Connection con = null;
        DataSource ds = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer strSQL = new StringBuffer();
        try {
            //	The below SQL statement (named strSQLforModule) is to query all work products and modules INCLUDED.
            StringBuffer strSQLforModule = new StringBuffer();

            //Headers of planned defect table
            strSQLforModule.append("SELECT T0.dp_id, WPName, ModuleName,");
            strSQLforModule.append(" WPPlannedDefect, WPReplannedDefect,");
            strSQLforModule.append(" PlannedDefect, ReplannedDefect,");
            strSQLforModule.append(" DocumentReview, PrototypeReview, CodeReview, ");
            strSQLforModule.append(" UnitTest, IntegrationTest, SystemTest, AcceptanceTest, Others");
            strSQLforModule.append(" FROM");

            //Work product and Module
            strSQLforModule.append(" (SELECT DP.dp_id, WP.name as WPName, M.name as ModuleName");
            strSQLforModule.append(" FROM defect_plan DP, workproduct WP, wp_size WPS, module M");
            strSQLforModule.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforModule.append(" AND DP.project_id = WPS.project_id");		//Thaison - November 25, 2002
            strSQLforModule.append(" AND DP.wp_id = WP.wp_id");
            strSQLforModule.append(" AND WP.wp_id = WPS.wp_id");
            strSQLforModule.append(" AND DP.module_id = M.module_id");
            strSQLforModule.append(" GROUP BY DP.dp_id, WP.name, M.name)  T0,");

            //Planned defect and Re-planned defect of work product
            strSQLforModule.append(" (SELECT DP.dp_id, WPS.planned_defect as WPPlannedDefect, WPS.replanned_defect as WPReplannedDefect");
            strSQLforModule.append(" FROM defect_plan DP, wp_size WPS");
            strSQLforModule.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforModule.append(" AND DP.project_id = WPS.project_id");
            strSQLforModule.append(" AND DP.wp_id = WPS.wp_id");
            strSQLforModule.append(" GROUP BY DP.dp_id, WPS.planned_defect, WPS.replanned_defect)  T_WPPlanned,");

            //Planned defect and Re-planned defect of module
            strSQLforModule.append(" (SELECT DP.dp_id, M.planned_defect as PlannedDefect, M.replanned_defect as ReplannedDefect");
            strSQLforModule.append(" FROM defect_plan DP, module M");
            strSQLforModule.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforModule.append(" AND DP.module_id = M.module_id");
            strSQLforModule.append(" GROUP BY DP.dp_id, M.planned_defect, M.replanned_defect)  T_Planned,");

            //Document review
            strSQLforModule.append(" (SELECT DP.dp_id, DP.value AS DocumentReview");
            strSQLforModule.append(" FROM defect_plan DP");
            strSQLforModule.append(" WHERE project_id = " + nProjectID);
            strSQLforModule.append(" AND DP.qa_id = 20");
            strSQLforModule.append(" GROUP BY DP.dp_id, DP.value) T_DocumentR,");

            //Prototype review
            strSQLforModule.append(" (SELECT DP.dp_id, DP.value AS PrototypeReview");
            strSQLforModule.append(" FROM defect_plan DP");
            strSQLforModule.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforModule.append(" AND DP.qa_id = 23");
            strSQLforModule.append(" GROUP BY DP.dp_id, DP.value) T_PrototypeR,");

            //Code review
            strSQLforModule.append(" (SELECT DP.dp_id, DP.value AS CodeReview");
            strSQLforModule.append(" FROM defect_plan DP");
            strSQLforModule.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforModule.append(" AND DP.qa_id = 21");
            strSQLforModule.append(" GROUP BY DP.dp_id, DP.value) T_CodeR,");

            //Unit test
            strSQLforModule.append(" (SELECT DP.dp_id, DP.value AS UnitTest");
            strSQLforModule.append(" FROM defect_plan DP");
            strSQLforModule.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforModule.append(" AND DP.qa_id = 10");
            strSQLforModule.append(" GROUP BY DP.dp_id, DP.value) T_UnitT,");

            //Integration test
            strSQLforModule.append(" (SELECT DP.dp_id, DP.value AS IntegrationTest");
            strSQLforModule.append(" FROM defect_plan DP");
            strSQLforModule.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforModule.append(" AND DP.qa_id = 11");
            strSQLforModule.append(" GROUP BY DP.dp_id, DP.value) T_IntegrationT,");

            //System test
            strSQLforModule.append(" (SELECT DP.dp_id, DP.value AS SystemTest");
            strSQLforModule.append(" FROM defect_plan DP");
            strSQLforModule.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforModule.append(" AND DP.qa_id = 12");
            strSQLforModule.append(" GROUP BY DP.dp_id, DP.value) T_SystemT,");

            //Acceptance test
            strSQLforModule.append(" (SELECT DP.dp_id, DP.value AS AcceptanceTest");
            strSQLforModule.append(" FROM defect_plan DP");
            strSQLforModule.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforModule.append(" AND DP.qa_id = 13");
            strSQLforModule.append(" GROUP BY DP.dp_id, DP.value) T_AcceptanceT, ");

            //Others
            strSQLforModule.append(" (SELECT DP.dp_id, DP.value AS Others");
            strSQLforModule.append(" FROM defect_plan DP");
            strSQLforModule.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforModule.append(" AND DP.qa_id NOT IN (10, 11, 12, 13, 20, 21, 23)");
            strSQLforModule.append(" GROUP BY DP.dp_id, DP.value) T_Others");

            //Join all together
            strSQLforModule.append(" WHERE T0.dp_id = T_Planned.dp_id(+)");
            strSQLforModule.append(" AND T0.dp_id = T_WPPlanned.dp_id(+)");
            strSQLforModule.append(" AND T0.dp_id = T_DocumentR.dp_id(+)");
            strSQLforModule.append(" AND T0.dp_id = T_PrototypeR.dp_id(+)");
            strSQLforModule.append(" AND T0.dp_id = T_CodeR.dp_id(+)");
            strSQLforModule.append(" AND T0.dp_id = T_UnitT.dp_id(+)");
            strSQLforModule.append(" AND T0.dp_id = T_IntegrationT.dp_id(+)");
            strSQLforModule.append(" AND T0.dp_id = T_SystemT.dp_id(+)");
            strSQLforModule.append(" AND T0.dp_id = T_AcceptanceT.dp_id(+)");
            strSQLforModule.append(" AND T0.dp_id = T_Others.dp_id(+)");
//			strSQLforModule.append(" ORDER BY WPName, ModuleName");

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //	The below SQL statement (named strSQLforWP) is to query all work products without any module.
            StringBuffer strSQLforWP = new StringBuffer();

            //Headers of planned defect table
            strSQLforWP.append("SELECT T0.dp_id, WPName, ModuleName,");
            strSQLforWP.append(" WPPlannedDefect, WPReplannedDefect,");
            strSQLforWP.append(" PlannedDefect, ReplannedDefect,");
            strSQLforWP.append(" DocumentReview, PrototypeReview, CodeReview, ");
            strSQLforWP.append(" UnitTest, IntegrationTest, SystemTest, AcceptanceTest, Others");
            strSQLforWP.append(" FROM");

            //Work product and EMPTY module (set as ZZZ to order at the bottom of table)
            strSQLforWP.append(" (SELECT DP.dp_id, WP.name as WPName, 'ZZZ' as ModuleName");
            strSQLforWP.append(" FROM defect_plan DP, workproduct WP");
            strSQLforWP.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforWP.append(" AND DP.wp_id = WP.wp_id");
            strSQLforWP.append(" AND DP.module_id = 0");
            strSQLforWP.append(" GROUP BY DP.dp_id, WP.name)  T0,");

            //Planned defect and Re-planned defect of work product
            strSQLforWP.append(" (SELECT DP.dp_id, WPS.planned_defect as WPPlannedDefect, WPS.replanned_defect as WPReplannedDefect");
            strSQLforWP.append(" FROM defect_plan DP, wp_size WPS");
            strSQLforWP.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforWP.append(" AND DP.project_id = WPS.project_id");
            strSQLforWP.append(" AND DP.wp_id = WPS.wp_id");
            strSQLforWP.append(" GROUP BY DP.dp_id, WPS.planned_defect, WPS.replanned_defect)  T_WPPlanned,");

            //Planned defect and Re-planned defect of module
            strSQLforWP.append(" (SELECT DP.dp_id, 0 as PlannedDefect, 0 as ReplannedDefect");
            strSQLforWP.append(" FROM defect_plan DP, workproduct WP");
            strSQLforWP.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforWP.append(" AND DP.module_id = 0");
            strSQLforWP.append(" AND DP.wp_id = WP.wp_id");
            strSQLforWP.append(" GROUP BY DP.dp_id)  T_Planned,");

            //Document review
            strSQLforWP.append(" (SELECT DP.dp_id, DP.value AS DocumentReview");
            strSQLforWP.append(" FROM defect_plan DP");
            strSQLforWP.append(" WHERE project_id = " + nProjectID);
            strSQLforWP.append(" AND DP.qa_id = 20");
            strSQLforWP.append(" AND DP.module_id = 0");
            strSQLforWP.append(" GROUP BY DP.dp_id, DP.value) T_DocumentR,");

            //Prototype review
            strSQLforWP.append(" (SELECT DP.dp_id, DP.value AS PrototypeReview");
            strSQLforWP.append(" FROM defect_plan DP");
            strSQLforWP.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforWP.append(" AND DP.qa_id = 23");
            strSQLforWP.append(" AND DP.module_id = 0");
            strSQLforWP.append(" GROUP BY DP.dp_id, DP.value) T_PrototypeR,");

            //Code review
            strSQLforWP.append(" (SELECT DP.dp_id, DP.value AS CodeReview");
            strSQLforWP.append(" FROM defect_plan DP");
            strSQLforWP.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforWP.append(" AND DP.qa_id = 21");
            strSQLforWP.append(" AND DP.module_id = 0");
            strSQLforWP.append(" GROUP BY DP.dp_id, DP.value) T_CodeR,");

            //Unit test
            strSQLforWP.append(" (SELECT DP.dp_id, DP.value AS UnitTest");
            strSQLforWP.append(" FROM defect_plan DP");
            strSQLforWP.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforWP.append(" AND DP.qa_id = 10");
            strSQLforWP.append(" AND DP.module_id = 0");
            strSQLforWP.append(" GROUP BY DP.dp_id, DP.value) T_UnitT,");

            //Integration test
            strSQLforWP.append(" (SELECT DP.dp_id, DP.value AS IntegrationTest");
            strSQLforWP.append(" FROM defect_plan DP");
            strSQLforWP.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforWP.append(" AND DP.qa_id = 11");
            strSQLforWP.append(" AND DP.module_id = 0");
            strSQLforWP.append(" GROUP BY DP.dp_id, DP.value) T_IntegrationT,");

            //System test
            strSQLforWP.append(" (SELECT DP.dp_id, DP.value AS SystemTest");
            strSQLforWP.append(" FROM defect_plan DP");
            strSQLforWP.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforWP.append(" AND DP.qa_id = 12");
            strSQLforWP.append(" AND DP.module_id = 0");
            strSQLforWP.append(" GROUP BY DP.dp_id, DP.value) T_SystemT,");

            //Acceptance test
            strSQLforWP.append(" (SELECT DP.dp_id, DP.value AS AcceptanceTest");
            strSQLforWP.append(" FROM defect_plan DP");
            strSQLforWP.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforWP.append(" AND DP.qa_id = 13");
            strSQLforWP.append(" AND DP.module_id = 0");
            strSQLforWP.append(" GROUP BY DP.dp_id, DP.value) T_AcceptanceT, ");

            //Others
            strSQLforWP.append(" (SELECT DP.dp_id, DP.value AS Others");
            strSQLforWP.append(" FROM defect_plan DP");
            strSQLforWP.append(" WHERE DP.project_id = " + nProjectID);
            strSQLforWP.append(" AND DP.qa_id NOT IN (10, 11, 12, 13, 20, 21, 23)");
            strSQLforWP.append(" AND DP.module_id = 0");
            strSQLforWP.append(" GROUP BY DP.dp_id, DP.value) T_Others");

            //Join all together
            strSQLforWP.append(" WHERE T0.dp_id = T_Planned.dp_id(+)");
            strSQLforWP.append(" AND T0.dp_id = T_WPPlanned.dp_id(+)");
            strSQLforWP.append(" AND T0.dp_id = T_DocumentR.dp_id(+)");
            strSQLforWP.append(" AND T0.dp_id = T_PrototypeR.dp_id(+)");
            strSQLforWP.append(" AND T0.dp_id = T_CodeR.dp_id(+)");
            strSQLforWP.append(" AND T0.dp_id = T_UnitT.dp_id(+)");
            strSQLforWP.append(" AND T0.dp_id = T_IntegrationT.dp_id(+)");
            strSQLforWP.append(" AND T0.dp_id = T_SystemT.dp_id(+)");
            strSQLforWP.append(" AND T0.dp_id = T_AcceptanceT.dp_id(+)");
            strSQLforWP.append(" AND T0.dp_id = T_Others.dp_id(+)");

            strSQL.append(strSQLforModule + " UNION " + strSQLforWP);
            strSQL.append(" ORDER BY WPName, ModuleName");
            if (DMS.DEBUG)
                logger.debug("strSQL = " + strSQL.toString());

            StringVector vecPL = new StringVector(16);

            ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSQL.toString());

            String strPrevWP = "";
            String strPrevModule = "";
            String strDefectPlanIDs = "";
            int nRow = 0;

            while (rs.next()) {
                String strID = rs.getString("dp_id");

                String strWP = (rs.getString("WPName") != null) ? rs.getString("WPName") : "";
                String strModule = (rs.getString("ModuleName") != null) ? rs.getString("ModuleName") : "";
                if ("ZZZ".equals(strModule)) {
                    strModule = "";
                }

                String strPlannedDefect = (rs.getString("PlannedDefect") == null || "0".equals(rs.getString("PlannedDefect"))) ? "" : rs.getString("PlannedDefect");
                String strReplannedDefect = (rs.getString("ReplannedDefect") == null || "0".equals(rs.getString("ReplannedDefect"))) ? "" : rs.getString("ReplannedDefect");

                String strDR = (rs.getString("DocumentReview") == null || "0".equals(rs.getString("DocumentReview"))) ? "" : rs.getString("DocumentReview");
                String strPR = (rs.getString("PrototypeReview") == null || "0".equals(rs.getString("PrototypeReview"))) ? "" : rs.getString("PrototypeReview");
                String strCR = (rs.getString("CodeReview") == null || "0".equals(rs.getString("CodeReview"))) ? "" : rs.getString("CodeReview");

                String strUT = (rs.getString("UnitTest") == null || "0".equals(rs.getString("UnitTest"))) ? "" : rs.getString("UnitTest");
                String strIT = (rs.getString("IntegrationTest") == null || "0".equals(rs.getString("IntegrationTest"))) ? "" : rs.getString("IntegrationTest");
                String strST = (rs.getString("SystemTest") == null || "0".equals(rs.getString("SystemTest"))) ? "" : rs.getString("SystemTest");
                String strAT = (rs.getString("AcceptanceTest") == null || "0".equals(rs.getString("AcceptanceTest"))) ? "" : rs.getString("AcceptanceTest");
                String strOthers = (rs.getString("Others") == null || "0".equals(rs.getString("Others"))) ? "" : rs.getString("Others");

                String strWPPlannedDefect = (rs.getString("WPPlannedDefect") == null || "0".equals(rs.getString("WPPlannedDefect"))) ? "" : rs.getString("WPPlannedDefect");
                String strWPReplannedDefect = (rs.getString("WPReplannedDefect") == null || "0".equals(rs.getString("WPReplannedDefect"))) ? "" : rs.getString("WPReplannedDefect");

                //float fActualTotal = 0;
                int nActualTotal = 0;

                if (strWP.equals(strPrevWP) && strModule.equals(strPrevModule)) {
                    strDefectPlanIDs += ", " + strID;
                    smResult.setCell(nRow - 1, 0, strDefectPlanIDs);

                    if (!"".equals(strDR)) {
                        smResult.setCell(nRow - 1, 5, strDR);
                    }
                    if (!"".equals(strPR)) {
                        smResult.setCell(nRow - 1, 6, strPR);
                    }
                    if (!"".equals(strCR)) {
                        smResult.setCell(nRow - 1, 7, strCR);
                    }
                    if (!"".equals(strUT)) {
                        smResult.setCell(nRow - 1, 8, strUT);
                    }
                    if (!"".equals(strIT)) {
                        smResult.setCell(nRow - 1, 9, strIT);
                    }
                    if (!"".equals(strST)) {
                        smResult.setCell(nRow - 1, 10, strST);
                    }
                    if (!"".equals(strAT)) {
                        smResult.setCell(nRow - 1, 11, strAT);
                    }
                    if (!"".equals(strOthers)) {
                        smResult.setCell(nRow - 1, 12, strOthers);
                    }

                    //Total
                    for (int i = 5; i <= 12; i++) {
                        //float fTemp = ("".equals(smResult.getCell(nRow-1, i))) ? 0 : Float.parseFloat(smResult.getCell(nRow-1, i));
                        //fActualTotal += fTemp;
                        int nTemp = ("".equals(smResult.getCell(nRow - 1, i))) ? 0 : Integer.parseInt(smResult.getCell(nRow - 1, i));
                        nActualTotal += nTemp;
                    }
                    //smResult.setCell(nRow-1, 13, Float.toString(fActualTotal));
                    smResult.setCell(nRow - 1, 13, Integer.toString(nActualTotal));
                }
                else {
                    vecPL.setCell(0, strID);		//project_plan ID
                    vecPL.setCell(1, strWP);		//work product name
                    vecPL.setCell(2, strModule);		//module name

                    vecPL.setCell(3, strPlannedDefect);		//planned defect
                    vecPL.setCell(4, strReplannedDefect);		//replanned defect

                    vecPL.setCell(5, strDR);		//document review
                    vecPL.setCell(6, strPR);		//prototype review
                    vecPL.setCell(7, strCR);		//code review

                    vecPL.setCell(8, strUT);		//unit test
                    vecPL.setCell(9, strIT);		//integration test
                    vecPL.setCell(10, strST);		//system test
                    vecPL.setCell(11, strAT);		//acceptance test
                    vecPL.setCell(12, strOthers);		//others

                    for (int i = 5; i <= 12; i++) {
                        //float fTemp = ("".equals(vecPL.getCell(i))) ? 0 : Float.parseFloat(vecPL.getCell(i));
                        //fActualTotal += fTemp;
                        int nTemp = ("".equals(vecPL.getCell(i))) ? 0 : Integer.parseInt(vecPL.getCell(i));
                        nActualTotal += nTemp;
                    }
                    //DecimalFormat dfTotal = new DecimalFormat("0.##");
                    //vecPL.setCell(13, dfTotal.format(fActualTotal));		//Total
                    vecPL.setCell(13, Integer.toString(nActualTotal));		//Total
                    vecPL.setCell(14, strWPPlannedDefect);
                    vecPL.setCell(15, strWPReplannedDefect);

                    smResult.addRow(vecPL);
                    strDefectPlanIDs = strID;
                    nRow++;
                }//end else

                strPrevWP = strWP;
                strPrevModule = strModule;
            }//end while

            if (DMS.DEBUG)
                logger.debug("smResult.getNumberOfRows() = " + smResult.getNumberOfRows());

            rs.close();
            stmt.close();

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //1- Add a total line of modules for each work product at the end of that workproduct
            //2- Add a TOTAL line at the end of table
            if (smResult.getNumberOfRows() > 0) {
                final int SHIFT = 3;
                int nSize = smResult.getNumberOfCols() - SHIFT;		//from PlannedDefect to the end.
                //float[] arrTotal = new float[nSize];
                int[] arrTotal = new int[nSize];
                for (int i = 0; i < nSize; i++) arrTotal[i] = 0;

                for (int c = 0; c < nSize; c++) {
                    for (int r = 0; r < smResult.getNumberOfRows(); r++) {
                        //Total of all work products
                        //float fItem = ("".equals(smResult.getCell(r, c+SHIFT))) ? 0 : Float.parseFloat(smResult.getCell(r, c+SHIFT));
                        int nItem = ("".equals(smResult.getCell(r, c + SHIFT))) ? 0 : Integer.parseInt(smResult.getCell(r, c + SHIFT));
                        arrTotal[c] += nItem;
                    }
                }

                //STEP 4 - Insert the TOTAL line at the bottom of table
                StringVector vecTotal = new StringVector(smResult.getNumberOfCols());
                vecTotal.setCell(0, "0");
                vecTotal.setCell(1, "<B>All work products</B>");
                vecTotal.setCell(2, "<B>All modules</B>");
                for (int i = SHIFT; i < smResult.getNumberOfCols(); i++) {
                    //DecimalFormat dfTotal = new DecimalFormat("0.##");
                    //String strTotal = dfTotal.format(arrTotal[i - SHIFT]);
                    String strTotal = Integer.toString(arrTotal[i - SHIFT]);

                    vecTotal.setCell(i, "<B>" + (("0".equals(strTotal)) ? "" : strTotal) + "</B>");
                }
                smResult.addRow(vecTotal);
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in DefectPlanBO.getPlannedDefectList(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in DefectPlanBO.getPlannedDefectList(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, stmt, rs,
                            "DefectPlanBO.getPlannedDefectList(): ");
        }

        return smResult;
    }

    public String addPlannedDefect(int nProjectID, StringVector svPlannedDefect) throws SQLException, Exception {
        String strResult = "";
        Connection con = null;
        DataSource ds = null;
        Statement stmt = null;
        //Statement stmtCheck = null;
        //Statement stmtDelete = null;
        ResultSet rs = null;
        ResultSet rsCheck = null;

        try {
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //STEP 1: Get data from client
            int nWorkProductID = Integer.parseInt(svPlannedDefect.getCell(2));
            int nModuleID = Integer.parseInt(svPlannedDefect.getCell(3));

            int[] arrQAID = new int[NUMBER_OF_QC];
            String[] arrQAValue = new String[NUMBER_OF_QC];

            //Document review
            arrQAID[0] = Integer.parseInt(svPlannedDefect.getCell(4));
            arrQAValue[0] = svPlannedDefect.getCell(5);

            //Prototype review
            arrQAID[1] = Integer.parseInt(svPlannedDefect.getCell(6));
            arrQAValue[1] = svPlannedDefect.getCell(7);

            //Code review
            arrQAID[2] = Integer.parseInt(svPlannedDefect.getCell(8));
            arrQAValue[2] = svPlannedDefect.getCell(9);

            //Unit test
            arrQAID[3] = Integer.parseInt(svPlannedDefect.getCell(10));
            arrQAValue[3] = svPlannedDefect.getCell(11);

            //Integration test
            arrQAID[4] = Integer.parseInt(svPlannedDefect.getCell(12));
            arrQAValue[4] = svPlannedDefect.getCell(13);

            //System test
            arrQAID[5] = Integer.parseInt(svPlannedDefect.getCell(14));
            arrQAValue[5] = svPlannedDefect.getCell(15);

            //Acceptance test
            arrQAID[6] = Integer.parseInt(svPlannedDefect.getCell(16));
            arrQAValue[6] = svPlannedDefect.getCell(17);

            //Others
            //String strOtherIDs = svPlannedDefect.getCell(18);
            String strOtherValue = svPlannedDefect.getCell(19);

            //Planned and replanned defect of module
            int nPlannedDefect = Integer.parseInt(svPlannedDefect.getCell(20));
            int nReplannedDefect = Integer.parseInt(svPlannedDefect.getCell(21));

            //Planned and replanned defect of work product
            int nWPPlannedDefect = Integer.parseInt(svPlannedDefect.getCell(22));
            int nWPReplannedDefect = Integer.parseInt(svPlannedDefect.getCell(23));
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            ds = conPool.getDS();
            con = ds.getConnection();
            //STEP 2 - Check constraint between planned defect value and total of qc_activity values. They must be equal
            StringBuffer strCheckSQL = new StringBuffer();
            strCheckSQL.append("SELECT module_id, SUM(value) as TotalByModule FROM defect_plan");
            strCheckSQL.append(" WHERE project_id = " + nProjectID);
            strCheckSQL.append(" AND wp_id = " + nWorkProductID);
            strCheckSQL.append(" GROUP BY module_id");
//			strCheckSQL.append(" AND module_id = " + nModuleID);

            stmt = con.createStatement();
            rsCheck = stmt.executeQuery(strCheckSQL.toString());
            int nTotalByModule = 0;
            int nTotalByWP = 0;
            while (rsCheck.next()) {
                String strTotal = (rs.getString("TotalByModule") != null) ? rs.getString("TotalByModule") : "";
                int nTotal = (!"".equals(strTotal)) ? Integer.parseInt(strTotal) : 0;

                String strModuleID = rs.getString("module_id");
                if (Integer.toString(nModuleID).equals(strModuleID)) {
                    nTotalByModule = nTotal;
                }
                nTotalByWP += nTotal;
            }
            rsCheck.close();
            stmt.close();

            //Checking
            nTotalByModule += ("".equals(strOtherValue) ? 0 : Integer.parseInt(strOtherValue));
            nTotalByWP += ("".equals(strOtherValue) ? 0 : Integer.parseInt(strOtherValue));

            for (int i = 0; i < NUMBER_OF_QC; i++) {
                nTotalByModule += ("".equals(arrQAValue[i]) ? 0 : Integer.parseInt(arrQAValue[i]));
                nTotalByWP += ("".equals(arrQAValue[i]) ? 0 : Integer.parseInt(arrQAValue[i]));
            }

            if (nWPPlannedDefect <= 0 && nWPReplannedDefect <= 0) {
                strResult = DMS.MSG_NO_PLANNED_DEFECT_FOR_WP;
                return strResult;
            }
            else if (nPlannedDefect <= 0 && nModuleID > 0) {
                strResult = DMS.MSG_NO_PLANNED_DEFECT_FOR_MODULE;
                return strResult;
            }
            else if ((nTotalByModule > nPlannedDefect && nTotalByModule > nReplannedDefect && nModuleID > 0) &&
                    (nTotalByModule <= nWPPlannedDefect || nTotalByModule <= nWPReplannedDefect)) {
                strResult = DMS.MSG_INVALID_PLANNED_DEFECT_BY_MODULE;
                return strResult;
            }
            else if ((nTotalByModule > nWPPlannedDefect && nTotalByModule > nWPReplannedDefect) ||
                    (nTotalByWP >= nWPPlannedDefect)) {
                strResult = DMS.MSG_INVALID_PLANNED_DEFECT_BY_WP;
                return strResult;
            }
            else if (nTotalByModule < nPlannedDefect && nTotalByWP < nWPPlannedDefect) {
                //Update "Others" QC_Activity
                int nOthers = Integer.parseInt(strOtherValue);
                nOthers += nPlannedDefect - nTotalByModule;
                strOtherValue = Integer.toString(nOthers);
                strResult = DMS.MSG_OTHER_QC_ACTIVITY_IS_UPDATED;
            }
            else if (nTotalByWP < nWPPlannedDefect && nModuleID == 0) {
                //Not update "Others" QC_Activity
                strResult = "";
            }
            else {
                if (DMS.DEBUG) {
                    logger.debug("Total by selected module = " + nTotalByModule);
                    logger.debug("Total by selected work product = " + nTotalByWP);
                    logger.debug("Planned/re-planned by module = " + nPlannedDefect + "/" + nReplannedDefect);
                    logger.debug("Planned/re-planned by work product = " + nWPPlannedDefect + "/" + nWPReplannedDefect);
                }

                strResult = DMS.MSG_UNSPECIFIED_ERROR;
                return strResult;
            }

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //STEP 3: Delete old DP_IDs which will be overwrited by new defect plan
            //Because a squad of project_id, wp_id, module_id, qa_id contributes a constraint.
            StringBuffer strDeleteSQL = new StringBuffer();
            strDeleteSQL.append("DELETE from defect_plan");
            strDeleteSQL.append(" WHERE dp_id IN (");
            strDeleteSQL.append(" SELECT dp_id from defect_plan");
            strDeleteSQL.append(" WHERE project_id = " + nProjectID);
            strDeleteSQL.append(" AND wp_id = " + nWorkProductID);
            strDeleteSQL.append(" AND module_id = " + nModuleID);
            String strQA_IDs = "";
            for (int i = 0; i < NUMBER_OF_QC - 1; i++) {
                if (!"0".equals(arrQAValue[i])) {//set 0 as default in WebTransfer
                    strQA_IDs += arrQAID[i] + ",";
                }
            }

            con.setAutoCommit(false);
            stmt = con.createStatement();

            if (!"".equals(strQA_IDs)) {
                strQA_IDs = strQA_IDs.substring(0, strQA_IDs.length() - 1);	//truncate the last comma.
                strDeleteSQL.append(" AND qa_id IN (" + strQA_IDs + "))");
                //logger.error("DefectPlanBO.addPlannedDefect(): SQL in step 2 = " + strDeleteSQL.toString());
                stmt.executeUpdate(strDeleteSQL.toString());
            }
            stmt.close();
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //STEP 4:	Generate INSERT SQL statement
            //For "review" and "test" QC_Activities
            StringBuffer strSQL = new StringBuffer();
            stmt = con.createStatement();
            for (int i = 0; i < NUMBER_OF_QC; i++) {
                if (!"0".equals(arrQAValue[i])) {
                    strSQL = new StringBuffer();
                    strSQL.append("INSERT INTO defect_plan(project_id, wp_id, module_id, qa_id, value)");
                    strSQL.append(" VALUES (" + nProjectID + ", " + nWorkProductID + ", " + nModuleID + ", " + arrQAID[i] + ", " + arrQAValue[i] + ")");

                    //logger.debug("DefectPlanBO.addPlannedDefect(): strSQL[" + i + "] = " + strSQL.toString());
                    stmt.addBatch(strSQL.toString());
                }
            }

            //For other QC_Activities
            if (!"0".equals(strOtherValue.trim())) {
                strSQL = new StringBuffer();
                strSQL.append("INSERT INTO defect_plan(project_id, wp_id, module_id, qa_id, value)");
                strSQL.append(" VALUES (" + nProjectID + ", " + nWorkProductID + ", " + nModuleID + ", " + "0" + ", " + strOtherValue + ")");

                //logger.info("DefectPlanBO.addPlannedDefect(): strSQL (Others) = " + strSQL.toString());
                stmt.addBatch(strSQL.toString());
            }

            stmt.executeBatch();
            stmt.close();
            con.commit();
        }
        catch (NumberFormatException ex) {
            con.rollback();
            logger.debug("NumberFormatException occurs in DefectPlanBO.addPlannedDefect(): " + ex.getMessage());
        }
        catch (SQLException ex) {
            con.rollback();
            logger.error("SQLException occurs in DefectPlanBO.addPlannedDefect(): " + ex.getMessage());
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Exception occurs in DefectPlanBO.addPlannedDefect(): " + e.getMessage());
        }
        finally {
            conPool.releaseResourceAndCommit(con, stmt, rs,
                            "DefectPlanBO.addPlannedDefect(): ");
        }

        return strResult;
    }

    public int deletePlannedDefect(String strSelectedIDs) throws SQLException, Exception {
        int nResult = -1;
        Connection con = null;
        DataSource ds = null;
        Statement stmt = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            ds = conPool.getDS();
            con = ds.getConnection();

            con.setAutoCommit(false);
            stmt = con.createStatement();

            if ((strSelectedIDs != null) && !"".equals(strSelectedIDs)) {
                strSQL.append("DELETE FROM defect_plan");
                strSQL.append(" WHERE dp_id IN (" + strSelectedIDs + ")");
                stmt.executeUpdate(strSQL.toString());
                stmt.close();
                con.commit();
            }
        }// end try
        catch (SQLException ex) {
            con.rollback();
            logger.error("SQLException occurs in userviews.DefectPlanBO.deletePlannedDefect(): " + ex.getMessage());
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Exception occurs in userviews.DefectPlanBO.deletePlannedDefect(): " + e.getMessage());
        }
        finally {
            conPool.releaseResourceAndCommit(con, stmt, null,
                            "DefectPlanBO.deletePlannedDefect(): ");
        }

        return nResult;
    }
}