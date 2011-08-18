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
 
 package fpt.dms.bo.login;

/**
 * Title:        Project
 * Description:
 * Copyright:    Copyright (C) 2001 Cogita FPTSoft
 * Company:      FPT Corporation
 * @author
 * @version 1.0.0
 */

import fpt.dms.framework.connection.WSConnectionPooling;
import fpt.dms.framework.util.DateUtil.DateUtil;
import fpt.dms.framework.util.StringUtil.StringMatrix;
import fpt.dms.framework.util.StringUtil.StringVector;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class VerifyLogin {
    private static Logger logger = Logger.getLogger(VerifyLogin.class.getName());

    private WSConnectionPooling conPool = new WSConnectionPooling();

    public VerifyLogin() {
    }
   /**
    * @VerifyLogin.java
    * @Description    : getUserInfo
 	* @Modified by    : Pham Tuan Minh
    * @Modified date  : December 5, 2002
    */

    public StringMatrix getUserInfo(String strAccount, String strPassword, int nProjectID) {
        StringMatrix strList = new StringMatrix();
        StringVector strRow = new StringVector(9);
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        try {
            ds = conPool.getDS();
            con = ds.getConnection();

            //get data
            StringBuffer strSQL = new StringBuffer();
            if (nProjectID == 0) //QA
            {
				strSQL.append(" SELECT d.name, d.group_name, d.account, r.name as role, r.name as position, p.code, p.project_id, d.developer_id ");
				strSQL.append(" FROM assignment a, project p, developer d, responsibility r ");
				strSQL.append(" WHERE a.project_id = p.project_id ");
				strSQL.append(" AND a.developer_id = d.developer_id ");
				strSQL.append(" AND a.response = r.responsibility_id ");
				strSQL.append(" AND d.account = ?");
				strSQL.append(" AND d.password = ?");
				strSQL.append(" AND d.status <> 4 ");				
            }
            else //Project Member
            {
                /**/
                //select project leader from project.
                strSQL.append(" SELECT D.developer_id, D.name, D.group_name, D.account, D.role, '1110000000' AS position, P.code, P.project_id ");
                strSQL.append(" FROM developer D, project P ");
                // group leader
                strSQL.append(" WHERE (P.Group_Name = D.Group_Name AND D.Role='1110000000') ");
                strSQL.append(" AND D.account = ?");
                strSQL.append(" AND D.password = ?");
                strSQL.append(" AND P.project_id = ?");
                strSQL.append(" AND D.status <> 4 ");

                /**/
				strSQL.append(" UNION ");
				strSQL.append(" SELECT D.developer_id, D.name, D.group_name, D.account, D.role, DP.position, P.code, p.project_id ");
				strSQL.append(" FROM defect_permission DP, developer D, project P ");
				strSQL.append(" WHERE DP.developer_id = D.developer_id ");
				strSQL.append(" AND DP.project_id = P.project_id ");
				strSQL.append(" AND D.account = ?");
				strSQL.append(" AND D.password = ?");
				strSQL.append(" AND DP.project_id = ?");
				strSQL.append(" AND DP.status = 0 ");
				strSQL.append(" AND D.status <> 4 ");

				//quality assurance
                strSQL.append(" UNION ");
                strSQL.append(" SELECT D.developer_id, D.name, D.group_name, D.account, D.role, '1100000000' AS position, P.code, P.project_id");
                strSQL.append(" FROM developer D, project P");
                strSQL.append(" WHERE D.role LIKE '____1_____' ");
                strSQL.append(" AND D.account = ?");
                strSQL.append(" AND D.password = ?");
                strSQL.append(" AND P.project_id = ?");
                strSQL.append(" AND D.status <> 4");

				//External user of project level
				strSQL.append(" UNION ");
				strSQL.append(" SELECT D.developer_id, D.name, D.group_name, D.account, D.role, '0000001000' AS position, P.code, P.project_id");
				strSQL.append(" FROM Developer D, Project P, Assignment A");
				strSQL.append(" WHERE D.role = '0000001000' ");
				strSQL.append(" AND A.developer_id = D.developer_id");
				strSQL.append(" AND A.project_id = P.project_id");
				strSQL.append(" AND D.account = ?");
				strSQL.append(" AND D.password = ?");
				strSQL.append(" AND P.project_id = ?");
				strSQL.append(" AND D.status <> 4");
            }
			//logger.debug("@HanhTN -- VerifyLogin -- getUserInfo = " + strSQL.toString());

            if (fpt.dms.constant.DMS.DEBUG) {
                logger.debug("SQL in getUserInfo = " + strSQL.toString());
            }
            prepStmt = con.prepareStatement(strSQL.toString());
            if (nProjectID == 0) {//QA
                prepStmt.setString(1, strAccount.toUpperCase());
                prepStmt.setString(2, strPassword);
            }
            else {
                prepStmt.setString(1, strAccount.toUpperCase());
                prepStmt.setString(2, strPassword);
                prepStmt.setInt(3, nProjectID);
                prepStmt.setString(4, strAccount.toUpperCase());
                prepStmt.setString(5, strPassword);
                prepStmt.setInt(6, nProjectID);
                prepStmt.setString(7, strAccount.toUpperCase());
                prepStmt.setString(8, strPassword);
                prepStmt.setInt(9, nProjectID);
                prepStmt.setString(10, strAccount.toUpperCase());
                prepStmt.setString(11, strPassword);
                prepStmt.setInt(12, nProjectID);
            }
            rs = prepStmt.executeQuery();

            //Map data from database
            while (rs.next()) {
                //Return Data:
                strRow.setCell(0, rs.getString("NAME"));
                strRow.setCell(1, rs.getString("GROUP_NAME"));
                strRow.setCell(2, rs.getString("ACCOUNT"));
                strRow.setCell(3, rs.getString("ROLE"));
                strRow.setCell(4, DateUtil.getCurrentDate());
                strRow.setCell(5, rs.getString("POSITION"));
                strRow.setCell(6, rs.getString("CODE"));
                strRow.setCell(7, rs.getString("PROJECT_ID"));
                strRow.setCell(8, rs.getString("developer_id"));
            }
            if (strRow.getCell(0).length() > 0) {
                strList.addRow(strRow);
            }
        }
        catch (Exception e) {
            logger.error("Exception occurs in userviews.VerifyLogin(). " +
                    e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                    "VerifyLogin.getUserInfo(): ");
        }
        return strList;
    }
}