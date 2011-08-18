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


public class ComboActivityType extends ComboBoxExt {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    private Connection con = null;
    private static Logger logger = Logger.getLogger(ComboActivityType.class.getName());

    /**
     * Modified by: Tu Ngoc Trung
     * Date: 2003-10-21
     * Purpose: Add selections for [ALL] and [NULL] options
     * @param nType: Type of this Combo Box. i.e.Normal combo or combo with [ALL], [NULL] options
     *          0: List all options without [ALL] and [NULL] options
     *          1: List all options plus empty option
     *          2: List all options plus [ALL] option
     *          3: List all options plus [ALL] option and [NULL] option
     */
    public ComboActivityType(int nType) {

        StringMatrix strList = new StringMatrix();
        StringVector strRow = new StringVector(2);
        
        /**
         * Modified by: Tu Ngoc Trung
         * Date: 2003-10-21
         * Purpose: Add selections for [ALL] and [NULL] options*/
        //Create an empty option, this combo box has not used currently
//        if (this.COMBO_EMPTY == nType) {
//            strRow.setCell(0, this.STR_NONE_VALUE);
//            strRow.setCell(1, "");
//            strList.addRow(strRow);
//        }
        
        //Create [ALL] option
        if (nType >= this.COMBO_ALL) {
            strRow.setCell(0, this.STR_ALL_VALUE);
            strRow.setCell(1, this.STR_ALL_STRING);
            strList.addRow(strRow);
        }
        
        //Create [NULL] option, this combo box has not used currently
//        if (this.COMBO_ALL_NONE == nType) {
//            strRow.setCell(0, this.STR_NONE_VALUE);
//            strRow.setCell(1, this.STR_NONE_STRING);
//            strList.addRow(strRow);
//        }
        /**End*/

        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        try {
            ds = conPool.getDS();
            this.con = ds.getConnection();

            //get data
            String selectStatement =
                    "SELECT * FROM Activity_Type ORDER BY NAME ASC";
            prepStmt = this.con.prepareStatement(selectStatement);
            rs = prepStmt.executeQuery();
            //Map data from database
            while (rs.next()) {
                //Return Data:
                strRow.setCell(0, rs.getString("AT_ID"));
                strRow.setCell(
                        1,
                        CommonUtil.correctHTMLError(rs.getString("Name")));
                strList.addRow(strRow);
            }
            rs.close();
            prepStmt.close();
        }
        catch (Exception ex) {
            logger.error(
                    "Exception occurs in ComboBox.ComboActivityType."
                    + ex.toString());
            logger.error(ex.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                            "ComboBox.ComboActivityType: ");
        }
        this.setListing(strList);
    }
}