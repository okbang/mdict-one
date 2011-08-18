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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import fpt.dms.bean.ComboBoxExt;
import fpt.dms.constant.DMS;
import fpt.dms.framework.connection.WSConnectionPooling;
import fpt.dms.framework.util.StringUtil.StringMatrix;
import fpt.dms.framework.util.StringUtil.StringVector;

public class ComboProject extends ComboBoxExt {

    private WSConnectionPooling conPool = new WSConnectionPooling();
    private static Logger logger = Logger.getLogger(ComboProject.class.getName());
    private StringMatrix smStatus = new StringMatrix();
    private StringMatrix smListAllProjects = new StringMatrix();
    //private StringMatrix smProjectMembers = new StringMatrix();

    /**
     * @ComboProject.java
     * @Description  : Get Project List
     * @Author       : Pham Tuan Minh
     * @Create date  : November 18, 2002
     */
    public ComboProject() {
    }

    public ComboProject(String strAccount, String strRole) {
        smListAllProjects = setListAllProject(strAccount, strRole, DMS.LIST_ALL_PROJECT);
    }

    public StringMatrix getListAllProject() {
        return this.smListAllProjects;
    }

    private String genSQL(String strAccount, String strRole, String strProjectStatus) {

        StringBuffer strSQL = new StringBuffer();
        String strSelect;
        String strStatusCondition;
        String strOrder;

        if (DMS.LIST_ALL_PROJECT.equals(strProjectStatus)) {
            /* list all project */
            strSelect = "select p.status, p.project_id, p.code ";
            strStatusCondition = "";
            strOrder = " order by code asc";
        }
        else {
            if (strProjectStatus == null) {
                /* list status */
                strSelect = "select distinct p.status ";
                strStatusCondition = "";
                strOrder = " order by status";
            }
            else {
                /* list project by status */
                strSelect = "select p.status, p.project_id, p.code ";
                strStatusCondition = " and p.status = '" + strProjectStatus + "'";
                strOrder = " order by code asc";
            }
        }

        strSQL.append(strSelect);
        // Assigned to project
        strSQL.append(" from project p, defect_permission dp, developer d ");
        strSQL.append(" where dp.project_id=p.project_id ");
        strSQL.append(" and dp.developer_id=d.developer_id ");
        strSQL.append(" and dp.status=0 ");
        strSQL.append(" and d.status<>4 ");
		strSQL.append(" and p.archive_status <> 4 ");

        strSQL.append(strStatusCondition);
        strSQL.append(" and upper(d.account)='");
        strSQL.append(strAccount.toUpperCase()).append("'");

		strSQL.append(" union ");
		strSQL.append(strSelect);
		strSQL.append(" from project p, developer d ");
		strSQL.append(" where d.status<>4 ");
		strSQL.append(strStatusCondition);
		strSQL.append(" and p.archive_status <> 4 ");
		strSQL.append(" and upper(d.account)='");
		strSQL.append(strAccount.toUpperCase()).append("'");

		// Project leader
		strSQL.append(" AND( (upper(p.leader)=upper(d.account)) ");
		// Group leader
		strSQL.append(" OR (p.group_name=d.group_name and d.role='1110000000')");
		// QA
		strSQL.append(" OR (d.role LIKE '____1_____') ) ");
		/*
		// External at group level
		//strSQL.append(" OR (p.group_name=d.group_name and d.role='0000001100') )");

        // External at group level
        strSQL.append(" union ");
        strSQL.append(strSelect);
        strSQL.append(" from project p, developer d, assignment a ");
        strSQL.append(" where a.developer_id=d.developer_id ");
        strSQL.append(" and a.project_id=p.project_id ");
        strSQL.append(" and d.role='0000001100' ");
        strSQL.append(" and d.status<>4 ");
        strSQL.append(strStatusCondition);
        strSQL.append(" and upper(d.account)='");
        strSQL.append(strAccount.toUpperCase()).append("'");
        */

		// External at project level
		strSQL.append(" union ");
		strSQL.append(strSelect);
		strSQL.append(" from project p, developer d, assignment a ");
		strSQL.append(" where a.developer_id=d.developer_id ");
		strSQL.append(" and a.project_id=p.project_id ");
		strSQL.append(" and d.role='0000001000' ");
		strSQL.append(" and d.status<>4 ");
		strSQL.append(" and p.archive_status <> 4 ");
		strSQL.append(strStatusCondition);
		strSQL.append(" and upper(d.account)='");
		strSQL.append(strAccount.toUpperCase()).append("'");

        strSQL.append(strOrder);

		//logger.debug("@HanhTN -- ComboProject -- strSQL = " + strSQL.toString());

        if (DMS.DEBUG) {
            logger.debug("strSQL=" + strSQL);
        }
        return strSQL.toString();
    }

    public void setProjectList(String strAccount, String strRole, String strProjectStatus) {
        this.setListing(setListAllProject(strAccount, strRole, strProjectStatus));
    }

    private StringMatrix setListAllProject(String strAccount, String strRole, String strProjectStatus) {
        StringMatrix strList = new StringMatrix();
        StringVector strRow = new StringVector(3);

        DataSource ds = null;
		ResultSet rs = null;
		Connection con = null;
        PreparedStatement prepStmt = null;

        try {
            ds = conPool.getDS();
            con = ds.getConnection();

            prepStmt = con.prepareStatement(genSQL(strAccount, strRole, strProjectStatus));
            rs = prepStmt.executeQuery();

			//Map data from database
            while (rs.next()) {
			//Return Data:
                strRow.setCell(0, rs.getString("PROJECT_ID"));
                strRow.setCell(1, rs.getString("CODE"));
                strRow.setCell(2, rs.getString("STATUS"));
                strList.addRow(strRow);
            }
            rs.close();
            prepStmt.close();
			con.close();
        }
        catch (Exception ex) {
            logger.error("Exception occurs in ComboBox.ComboProject. " + ex.toString());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs, "ComboBox.ComboProject: ");
        }
        return strList;
    }

    public void setStatusList(String strAccount, String strRole) {
        StringMatrix strList = new StringMatrix();
        StringVector strRow = new StringVector(2);

        DataSource ds = null;
		ResultSet rs = null;
		Connection con = null;
        PreparedStatement prepStmt = null;

        try {
            ds = conPool.getDS();
            con = ds.getConnection();

            //get data
            prepStmt = con.prepareStatement(genSQL(strAccount, strRole, null));
            rs = prepStmt.executeQuery();
            String strStatus;

            //Map data from database
            while (rs.next()) {
                //Return Data:
                strStatus = rs.getString("STATUS");
                // Incorrect status, only occurrs when manual insert new project
                if (strStatus == null) {
                    continue;
                }
                
                strRow.setCell(0, strStatus);
                if (DMS.PROJECT_VALUE_STATUS_ALL.equals(strStatus)) {
                    strRow.setCell(1, DMS.PROJECT_STATUS_ALL);
                }
                else if (DMS.PROJECT_VALUE_STATUS_ONGOING.equals(strStatus)) {
                    strRow.setCell(1, DMS.PROJECT_STATUS_ONGOING);
                }
                else if (DMS.PROJECT_VALUE_STATUS_CLOSED.equals(strStatus)) {
                    strRow.setCell(1, DMS.PROJECT_STATUS_CLOSED);
                }
                else if (DMS.PROJECT_VALUE_STATUS_CANCELLED.equals(strStatus)) {
                    strRow.setCell(1, DMS.PROJECT_STATUS_CANCELLED);
                }
                else if (DMS.PROJECT_VALUE_STATUS_TENTATIVE.equals(strStatus)) {
                    strRow.setCell(1, DMS.PROJECT_STATUS_TENTATIVE);
                }
                strList.addRow(strRow);
            }
            rs.close();
            prepStmt.close();
			con.close();
        }
        catch (Exception ex) {
            logger.error("Exception occurs in ComboBox.ComboProject. " + ex.toString());
        }
        finally {
            conPool.releaseResource(con, prepStmt, rs, "ComboBox.ComboProject: ");
        }
        this.smStatus = strList;
    }

    public StringMatrix getStatusList() {
        return this.smStatus;
    }
}