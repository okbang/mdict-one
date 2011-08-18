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
 
 /**
 * Title:        QueryBO.java
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      FPT - FSOFT
 * Author:       Nguyen Thai Son
 * Create date:  October 24, 2002
 * Modified date:
 */

package fpt.dms.bo.DefectManagement;

//imported from standard JAVA

import fpt.dms.bean.DefectManagement.QueryAddBean;
import fpt.dms.bean.DefectManagement.QueryListingBean;
import fpt.dms.bean.UserInfoBean;
import fpt.dms.constant.DMS;
import fpt.dms.framework.connection.WSConnectionPooling;
import fpt.dms.framework.util.CommonUtil.CommonUtil;
import fpt.dms.framework.util.StringUtil.StringMatrix;
import fpt.dms.framework.util.StringUtil.StringVector;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class QueryBO {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    //private Connection con = null;

    private static Logger logger = Logger.getLogger(QueryBO.class.getName());

    //private static final String DATEFORMAT = "mm/dd/yy";

    public QueryBO() {
    }

    public StringMatrix getQueryList(int nProjectID, String strAccount){
        StringMatrix smResult = new StringMatrix();
        Connection con = null;
        DataSource ds = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        ResultSet rsDev = null;
        StringBuffer strSQL = new StringBuffer();

        try {
            ds = conPool.getDS();
            con = ds.getConnection();

            //Get developer ID
            /*
            String strDevID = "";
            String getDev = "SELECT developer_id FROM developer WHERE UPPER(account) = UPPER(?)";
            prepStmt = con.prepareStatement(getDev);
            prepStmt.setString(1, strAccount);
            rsDev = prepStmt.executeQuery();
            while (rsDev.next()) {
                strDevID = rsDev.getString(1);
            }
            rsDev.close();
            prepStmt.close();
            /**/

            /**/
            strSQL.append("SELECT distinct query_id, title, scope ");
            strSQL.append("FROM defect_query, developer ");
            //strSQL.append(" FROM defect_query");			
            //strSQL.append(" WHERE (developer_id = ? AND project_id =? ) OR ( project_id = ? AND scope = ?)");
            // huynh2 add
			strSQL.append(" where (defect_query.developer_id = developer.developer_id AND UPPER(developer.account) = UPPER(?) AND project_id=?) OR (( project_id = ? AND scope = ?))");
			//strSQL.append(" WHERE (developer_id = (SELECT developer_id FROM developer WHERE UPPER(account) = UPPER(?)) AND project_id =? ) OR ( project_id = ? AND scope = ?)");
            // end
			 /**/ 
			 
			//strSQL.append(" select  query_id, title, scope from (defect_query dq left outer join developer dv on dq.developer_id = dv.developer_id) where (defect_query.developer_id = ? AND UPPER(developer.account) = UPPER(?) AND  project_id =?) OR ( project_id = ? AND scope = ?)");
            prepStmt = con.prepareStatement(strSQL.toString());

            //prepStmt.setString(1, strDevID);
			prepStmt.setString(1, strAccount);
            prepStmt.setInt(2, nProjectID);
            prepStmt.setInt(3, nProjectID);
            prepStmt.setString(4, "0");
            rs = prepStmt.executeQuery();

            while (rs.next()) {
                StringVector tmpVector = new StringVector(3);
                tmpVector.setCell(0, rs.getString(1));
                tmpVector.setCell(1, CommonUtil.correctHTMLError(rs.getString(2)));
                tmpVector.setCell(2, rs.getString(3));
                smResult.addRow(tmpVector);
            }
            //logger.info("QueryBO.getQueryList(): smResult.getNumberOfRows() = " + smResult.getNumberOfRows());

            rs.close();
            prepStmt.close();

        }
        catch (SQLException ex) {
            logger.error("SQLException occurs in userviews.QueryBO.getQueryList(): " + ex.getMessage() + " strSQL = " + strSQL.toString());
        }
        catch (Exception e) {
            logger.error("Exception occurs in userviews.QueryBO.getQueryList(): " + e.getMessage());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs,
                                    "QueryBO.getQueryList(): ");
        }
        return smResult;
    }

    public void addQuery(QueryAddBean beanQuery, UserInfoBean beanUserInfo) throws SQLException, Exception {
        String strWhereSQL = createWhere(beanQuery);
        String strRole = beanUserInfo.getPosition();
        Connection con = null;
        String strScope = beanQuery.getScope();
        if ("0".equals(strScope)) {
            if (strRole.charAt(2) != '1') {
                strScope = "1";
            }
        }
        else if (strScope == null) {
            strScope = "1";
        }

        DataSource ds = null;
        PreparedStatement prepStmt = null;
        //PreparedStatement prepStmtDev = null;
        ResultSet rsDev = null;

        try {
            ds = conPool.getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);

            String dev_id = "";
            String getDev = "select developer_id from developer where UPPER(account) = UPPER( ? )";
            prepStmt = con.prepareStatement(getDev);
            prepStmt.setString(1, beanUserInfo.getAccount());
            rsDev = prepStmt.executeQuery();
            while (rsDev.next()) {
                dev_id = rsDev.getString(1);
            }
            rsDev.close();
            prepStmt.close();

            StringBuffer strInsertSQL = new StringBuffer();
            strInsertSQL.append("INSERT INTO defect_query(developer_id, project_id, title, scope, sql_text)");
            strInsertSQL.append(" VALUES(?, ?, ? , ? , ?)");

            if (DMS.DEBUG) {
                logger.debug("strInsertSQL = " + strInsertSQL.toString());
                logger.debug("dev_id = " + dev_id);
                logger.debug("project_id = " + beanUserInfo.getProjectID());
                logger.debug("title = " + beanQuery.getName());
                logger.debug("scope = " + strScope);
                logger.debug("sql_text = " + strWhereSQL);
            }

            prepStmt = con.prepareStatement(strInsertSQL.toString());
            prepStmt.setString(1, dev_id);
            prepStmt.setInt(2, beanUserInfo.getProjectID());
            prepStmt.setString(3, beanQuery.getName());
            prepStmt.setString(4, strScope);
            prepStmt.setString(5, strWhereSQL);
            prepStmt.executeUpdate();

            prepStmt.close();
            con.commit();
        }
        catch (SQLException ex) {
            con.rollback();
            logger.debug("SQLException occurs in userviews.QueryBO.addQuery(): " + ex.getMessage());
        }
        catch (Exception e) {
            con.rollback();
            logger.debug("Exception occurs in userviews.QueryBO.addQuery(): " + e.getMessage());
        }
        finally {
            conPool.releaseResourceAndCommit(con, prepStmt, rsDev,
                            "QueryBO.addQuery(): ");
        }
    }

    public void deleteQuery(QueryListingBean beanQuery, UserInfoBean beanUserInfo) throws SQLException, Exception {
        Connection con = null;        
        String[] arrQueryID = beanQuery.getSelectedQueries();
        String strDelete = "";
        for (int i = 0; i < arrQueryID.length; i++) {
            strDelete = strDelete + arrQueryID[i] + ",";
        }
        strDelete = strDelete.substring(0, strDelete.length() - 1);

        DataSource ds = null;
        PreparedStatement prepStmt = null;

        try {
            ds = conPool.getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);

            String deleteStatement = "";
            if (beanUserInfo.getPosition().charAt(2) == '1') {
                deleteStatement = "DELETE FROM defect_query WHERE query_id IN (" + strDelete + " )";
            }
            else {
                deleteStatement = "DELETE FROM defect_query WHERE query_id IN (" + strDelete + " ) AND NOT(scope='0')";
            }

            prepStmt = con.prepareStatement(deleteStatement);
            prepStmt.executeUpdate();
            prepStmt.close();
            con.commit();
        }
        catch (SQLException ex) {
            con.rollback();
            logger.error("SQLException occurs in userviews.QueryBO.deleteQuery(): " + ex.getMessage());
        }
        catch (Exception e) {
            con.rollback();
            logger.error("Exception occurs in userviews.QueryBO.deleteQuery(): " + e.getMessage());
        }
        finally {
            conPool.releaseResourceAndCommit(con, prepStmt, null,
                            "QueryBO.deleteQuery(): ");
        }
    }

    private String createWhere(QueryAddBean beanQuery) {
        int numExpress = beanQuery.getFieldName().length;
        int iCount = 0;
        for (int i = 0; i < numExpress; i++) {
            if ((!beanQuery.getFieldName()[i].equals("0")) && (!beanQuery.getFieldName()[i].equals(""))) {
                iCount++;
            }
        }
        numExpress = iCount;
        String sqlText = "";
        String[] expression = new String[10];

        boolean[] flagNot = new boolean[10];
        for (int i = 0; i < 10; i++) {
            flagNot[i] = false;
        }

        expression[0] = "";
        String[] notOper = beanQuery.getNotOpe();
        if (notOper != null) {
            for (int i = 0; i < notOper.length; i++) {
                flagNot[Integer.parseInt(notOper[i])] = true;
            }
        }

        for (int i = 0; i < numExpress; i++) {
            if ((beanQuery.getFieldName()[i].equals("defect.create_date")) ||
                    (beanQuery.getFieldName()[i].equals("defect.close_date")) ||
                    (beanQuery.getFieldName()[i].equals("defect.due_date")))
            {
                expression[i] = beanQuery.getFieldName()[i] + " " +
                        beanQuery.getCriteria()[i] + " " +
                        "to_date('" + beanQuery.getValue()[i] + "', '" + DMS.DATE_FORMAT + "')";
            }
            else {
                if (!beanQuery.getCriteria()[i].equals("LIKE")) {
                    expression[i] = "UPPER(" + beanQuery.getFieldName()[i] + ")"
                            + " " + beanQuery.getCriteria()[i] + " '"
                            + (CommonUtil.stringConvert(beanQuery.getValue()[i].toUpperCase())) + "'";
                }
                else {
                    expression[i] = beanQuery.getFieldName()[i] + " " + beanQuery.getCriteria()[i]
                            + " '%" + (CommonUtil.stringConvert(beanQuery.getValue()[i])) + "%'";
                }
                if (flagNot[i]) {
                    expression[i] = " NOT(" + expression[i] + ")";
                }
            }//end else

            expression[i] = " (" + expression[i] + ") ";
        }  //end for

        String[] group = new String[10];
        int nowLevel = 1;
        int numCount = 0;

        nowLevel = Integer.parseInt(beanQuery.getGroup()[0]);
//make group

        for (int i = 0; i < numExpress; i++) {
            group[i] = "";
        }
        for (int i = 0; i < numExpress; i++) {
            if (nowLevel != Integer.parseInt(beanQuery.getGroup()[i])) {
                nowLevel = Integer.parseInt(beanQuery.getGroup()[i]);
                numCount++;
            }
            group[numCount] = group[numCount] + expression[i] + beanQuery.getLogical()[i];
        }

        group[numCount] = group[numCount].trim();
        group[numCount] = group[numCount].substring(0, group[numCount].lastIndexOf(" "));

        //make statement
        String logic = "";
        for (int i = 0; i < numCount + 1; i++) {
            if (i == numCount) {
                sqlText = sqlText + "(" + group[i] + ")";
            }
            else {
                logic = group[i].substring(group[i].lastIndexOf(" "), group[i].length());
                group[i] = group[i].substring(0, group[i].lastIndexOf(" "));
                sqlText = sqlText + "(" + group[i] + ")" + logic;
            }
        }
        if (numCount == 0) {
            sqlText = group[0];
        }
        return sqlText;
    }
}
