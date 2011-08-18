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
 
 
package fpt.dms.bo.combobox;

/**
 * Title:        Project
 * Description:
 * Copyright:    Copyright (C) 2001 Cogita FPTSoft
 * Company:      FPT Corporation
 * @author
 * @version 1.0.0
 */

import fpt.dms.bean.ComboBoxExt;
import fpt.dms.framework.connection.WSConnectionPooling;
import fpt.dms.framework.util.CommonUtil.CommonUtil;
import fpt.dms.framework.util.StringUtil.StringMatrix;
import fpt.dms.framework.util.StringUtil.StringVector;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ComboModuleCode extends ComboBoxExt {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    private Connection con = null;
    private static Logger logger = Logger.getLogger(ComboModuleCode.class.getName());

    /**
     * Modified by: Tu Ngoc Trung
     * Date: 2003-10-21
     * Purpose: Add selections for [ALL] and [NULL] options
     * @param nProjectID: ProjectID
     * @param nType: Type of this Combo Box. i.e.Nomarl combo or combo with [ALL], [NULL] options
     *          0: List all options without [ALL] and [NULL] options
     *          1: List all options plus empty option
     *          2: List all options plus [ALL] option
     *          3: List all options plus [ALL] option and [NULL] option
     */
    public ComboModuleCode(int nProjectID, int nType) {
        StringMatrix strList = new StringMatrix();
        StringVector strRow = new StringVector(5);

        /**
         * Modified by: Tu Ngoc Trung
         * Date: 2003-10-21
         * Purpose: Add selections for [ALL] and [NULL] options*/
        //Create empty option
        if (this.COMBO_EMPTY == nType) {
            strRow.setCell(0, this.STR_NONE_VALUE);
            strRow.setCell(1, "");
            strRow.setCell(2, "0");
            strRow.setCell(3, "0");
            strList.addRow(strRow);
        }
        
        //Create [ALL] option
        if (nType >= this.COMBO_ALL) {
            strRow.setCell(0, this.STR_ALL_VALUE);
            strRow.setCell(1, this.STR_ALL_STRING);
            strRow.setCell(2, "0");
            strRow.setCell(3, "0");
            strList.addRow(strRow);
        }
        
        //Create [NULL] option
        if (this.COMBO_ALL_NONE == nType) {
            strRow.setCell(0, this.STR_NONE_VALUE);
            strRow.setCell(1, this.STR_NONE_STRING);
            strRow.setCell(2, "0");
            strRow.setCell(3, "0");
            strList.addRow(strRow);
        }
        /**End*/

        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        try {
            ds = conPool.getDS();
            this.con = ds.getConnection();

            //get data
            StringBuffer strSQL = new StringBuffer();
            strSQL.append(
                    "SELECT module_id, name, planned_defect, replanned_defect, wp_id FROM MODULE");
            strSQL.append(" WHERE project_id = " + nProjectID);
            strSQL.append(" ORDER BY name ASC");
            prepStmt = this.con.prepareStatement(strSQL.toString());
            rs = prepStmt.executeQuery();

            //Map data from database
            while (rs.next()) {
                //Return Data:
                strRow.setCell(0, rs.getString("module_id"));
                strRow.setCell(
                        1,
                        CommonUtil.correctHTMLError(rs.getString("name")));
                String strPlannedDefect = rs.getString("planned_defect");
                String strReplannedDefect = rs.getString("replanned_defect");
                strRow.setCell(
                        2,
                        (strPlannedDefect != null && !"".equals(strPlannedDefect))
                        ? strPlannedDefect
                        : "0");
                strRow.setCell(
                        3,
                        (strReplannedDefect != null
                        && !"".equals(strReplannedDefect))
                        ? strReplannedDefect
                        : "0");
                strRow.setCell(4,rs.getString("wp_id"));
                strList.addRow(strRow);
            }

            rs.close();
            prepStmt.close();
        }
        catch (Exception ex) {
            logger.error(
                    "Exception occurs in ComboBox.ComboModuleCode."
                    + ex.toString());
            logger.error(ex.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ComboBox.ComboModuleCode: ");
        }
        this.setListing(strList);
    }
}