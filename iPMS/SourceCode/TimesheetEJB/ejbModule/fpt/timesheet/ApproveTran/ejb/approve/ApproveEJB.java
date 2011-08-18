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
 
 // TrangTK
// Dec 21, 2001
//********************************************

package fpt.timesheet.ApproveTran.ejb.approve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import fpt.timesheet.constant.Timesheet;
import fpt.timesheet.framework.connection.WSConnectionPooling;
import fpt.timesheet.framework.util.CommonUtil.CommonUtil;
import fpt.timesheet.framework.util.SqlUtil.SqlUtil;

public class ApproveEJB implements SessionBean {
    private static Logger logger = Logger.getLogger(ApproveEJB.class);
	private String strClassName = "ApproveEJB";
	
    final private int TIMESHEET_GENERAL = 1;
    final private int TIMESHEET_UPDATE_FORM = 2;
    final private int LD_REJECT_STATUS = 5;

    final private int QA_APPROVE_STATUS = 4;
    final private int QA_REJECT_STATUS = 5;
    final private boolean DEBUG_MODE = true;

    private Connection con;
    private SessionContext context;
    private WSConnectionPooling conPool = new WSConnectionPooling();
    private DataSource ds = null;

    private int nTotalPage = 0;
    private int nTotalTimesheet = 0;
    private int nCurrentPage;
    private int nStatus;
    
	private String strDataConstraintSQL = "";
	private String sProjectIdList;
    private String strFromDate = null;
    private String strToDate = null;

    /**
     * Method ejbCreate.
     * @throws CreateException
     */
    public void ejbCreate() throws CreateException {
        try {
            ds = conPool.getDS();
        }
        catch (Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }

    // Release resource
    public void ejbRemove() {
    }

    public void ejbLoad() {
    }

    // EJB active
    public void ejbActivate() {
    }

    // EJB Passivate
    public void ejbPassivate() {
    }

    /**
     * @see javax.ejb.SessionBean#setSessionContext(SessionContext)
     */
    // Init Session Context
    public void setSessionContext(SessionContext context) {
        this.context = context;

    }

    /**
     * get Timseheet List
     * TrangTK
     * Method getTimesheetList.
     * @param strListingName
     * @param nDevId
     * @param sProjectIdList
     * @param nStatus
     * @param strFromDate
     * @param strToDate
     * @param nSortby
     * @param nCurrentPage
     * @return ArrayList
     * @throws SQLException
     */
    public ArrayList getTimesheetList(String strListingName, int nDevId, String sProjectIdList, int nStatus, String strFromDate, String strToDate, String strAccount, int nSortby, int nCurrentPage) throws SQLException {

        ArrayList arrDataList = new ArrayList();

        this.nCurrentPage = nCurrentPage;
        this.nStatus = nStatus;
        this.sProjectIdList = sProjectIdList;
        this.strFromDate = strFromDate;
        this.strToDate = strToDate;
        try {
            String strIdList = selectTimesheetIDList(strListingName, nDevId, sProjectIdList, nStatus, strFromDate, strToDate, strAccount, nCurrentPage);

            if (strIdList.trim().length() > 0) {
                if (strListingName.equalsIgnoreCase("QAListing")) {
                    arrDataList = selectQAObjectList(strIdList, nSortby, this.TIMESHEET_GENERAL);
                }
                else {
                    arrDataList = selectLDObjectList(strIdList, nSortby, this.TIMESHEET_GENERAL);
                }
            }
        }
        catch (Exception ex) {
            logger.error("getTimesheetList() - error: " + ex.toString());
        }
        return arrDataList;
    }

    /**
     * Method selectTimesheetIDList.
     * @param strListingName
     * @param nDevId
     * @param sProjectIdList
     * @param nStatus
     * @param strFromDate
     * @param strToDate
     * @param nCurrentPage
     * @return String
     * @throws SQLException
     */
    private String selectTimesheetIDList(String strListingName, int nDevId, String sProjectIdList, int nStatus, String strFromDate, String strToDate, String strAccount, int nCurrentPage) throws SQLException {

        String strSql;
        StringBuffer strCountSQL = new StringBuffer();

        if (strListingName.equalsIgnoreCase("QAListing")) {
            strSql = genQASqlCommandGetIdList(sProjectIdList, nStatus, strFromDate, strToDate, strAccount);
            //Count all timesheet with data constraint (see method genQASqlCommandGetIdList())
            strCountSQL.append("SELECT COUNT(timesheet_id) as TOTAL FROM timesheet");
            strCountSQL.append(" WHERE project_id IN (" + sProjectIdList + ")");
            if (nStatus > 0)
                strCountSQL.append(" AND status = " + nStatus);
            else
                strCountSQL.append(" AND status IN (2, 4)");
            //Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
            strCountSQL.append(SqlUtil.genDateConstraint("occur_date", strFromDate, strToDate));
            // Filter by account
            if ((strAccount != null) && (! "".equals(strAccount))) {
                strCountSQL.append(" AND developer_id=");
                strCountSQL.append("(SELECT developer_id FROM developer WHERE account='")
                            .append(CommonUtil.stringConvert(strAccount))
                            .append("')");
            }
        }
        else {
            strSql = genLDSqlCommandGetIdList(strListingName, nDevId, sProjectIdList, nStatus, strFromDate, strToDate, strAccount);
            strCountSQL.append("SELECT COUNT(timesheet_id) as TOTAL FROM timesheet");
            strCountSQL.append(getPLDataConstraint());
        }
        //logger.debug("ApproveEJB.selectTimesheetIDList(): strCountSQL = " + strCountSQL.toString());

        // IN VALID VALUE
        if (strSql.length() < 1)
            return "";
        Statement stmt = null;
        ResultSet rs = null;
        int nTotalPage = 0;
        int nTotalTimesheet = 0;

        try {
            if (ds == null)
                ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            rs = stmt.executeQuery(strCountSQL.toString());
            if (rs.next()) {
                nTotalTimesheet = (rs.getString("TOTAL") != null) ? rs.getInt("TOTAL") : 0;
            }
            rs.close();

            nTotalPage = (nTotalTimesheet / Timesheet.MAX_RECORDS);
            if ((nTotalTimesheet % Timesheet.MAX_RECORDS) != 0)
                nTotalPage = nTotalPage + 1;
            //for check nCurrentPage > (nTotalPage-1)
            if (this.nCurrentPage > (nTotalPage - 1))
                this.nCurrentPage = 0;

            setTotalPage(nTotalPage);
            setTotalTimesheet(nTotalTimesheet);
        }
        catch (SQLException ex) {
            logger.error(strClassName + ".selectTimesheetIDList():" + ex.toString());
            throw ex;
        }
        catch (Exception ex) {
            logger.error(strClassName + ".selectTimesheetIDList():" + ex.toString());
        }
        finally {
            conPool.releaseResource(con, stmt, rs, strClassName + ".selectTimesheIDList():");
        } //finally
        return strSql;
    }

    /**
     * Method getTimesheetList.
     * @param strListingName
     * @param strIdList
     * @param nSortby
     * @return ArrayList
     * @throws SQLException
     */
    public ArrayList getTimesheetList(String strListingName, String strIdList, int nSortby) throws SQLException {

        ArrayList arrDataList = new ArrayList();
        try {
            if (strListingName.equalsIgnoreCase("QAListing")) {
                arrDataList = selectQAObjectList(strIdList, nSortby, this.TIMESHEET_UPDATE_FORM);
            }
            else {
                arrDataList = selectLDObjectList(strIdList, nSortby, this.TIMESHEET_UPDATE_FORM);
            }
        }
        catch (Exception ex) {
            logger.error("getTimesheetList() - error: " + ex.toString());
        }
        return arrDataList;
    }

    /**
     * Method changeStatus.
     * @param strListingName
     * @param nStatus
     * @param arrIdList
     * @param arrComment
     * @param strApprover
     * @throws SQLException
     */
    public void changeStatus(String strListingName, int nStatus, String[] arrIdList, String[] arrComment, String strApprover) throws SQLException {
        try {
            if (strListingName.equalsIgnoreCase("QAListing")) {
                qaChangeStatus(nStatus, arrIdList, arrComment, strApprover);
            }
            else {
                leaderChangeStatus(nStatus, arrIdList, arrComment, strApprover);
            }
        }
        catch (Exception ex) {
            logger.error("getTimesheetList() - error: " + ex.toString());
        }
    }

    /**
     * Method changeStatus. Approve by QA, set KPA by get from Process-KPA mapping automatically
     * @param strListingName
     * @param nStatus
     * @param arrIdList
     * @param arrComment
     * @param arrKPA
     * @param strApprover
     * @throws SQLException
     */
    public void changeStatus(String strListingName, int nStatus, String[] arrIdList, String[] arrComment, String[] arrKPA, String strApprover) throws SQLException {
        try {
            if (strListingName.equalsIgnoreCase("QAListing")) {
                qaChangeStatus(nStatus, arrIdList, arrComment, arrKPA, strApprover);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method genLDSqlCommandGetObject.
     * @param strIdList
     * @param nSortby
     * @return String
     */
    private static String genLDSqlCommandGetObject(String strIdList, int nSortby) {

        String strSql = "";
        strIdList = strIdList.trim();

        if (strIdList.length() > 0) {

            strSql = "SELECT Ts.Timesheet_Id,Ts.Description," + " to_char(Ts.OCCUR_DATE,'mm/dd/yy') as tsDate," + " Ts.Duration,Ts.Process_ID," + " Ts.TOW_ID,Ts.WP_ID," + " Ts.APPROVED_BY_LEADER,Ts.STATUS," + " Project.Code AS ProjectCode," + " Developer.Account AS Account," + " Ts.rcomment";

            switch (nSortby) {
                /* 26-Aug-2004: changes sorting methods
                 * Sort by Account first then by Timesheet Date for easier view */
                case 1:
                    // Project Code
                    strSql += " FROM TimeSheet Ts,Project,Developer" +
                                " WHERE Ts.Project_ID = Project.Project_ID" + " AND Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY Project.Code,Developer.Account,ts.occur_date";
                    break;

                case 2:
                    //strOrder += "Account";
                    strSql += " FROM TimeSheet Ts,Project, Developer" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY Developer.Account, ts.occur_date";
                    break;

                case 3:
                    //strOrder += "Occur_Date";
                    strSql += " FROM TimeSheet Ts,Project, Developer" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY ts.Occur_Date,Project.Code,Developer.Account";
                    break;

                case 4:
                    //strOrder += "ProcessName";
                    strSql += " FROM TimeSheet Ts,Project, Developer,Process" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.Process_ID = Process.Process_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY Process.Name,Project.Code,Developer.Account, ts.occur_date";

                    break;
                case 5:
                    //strOrder += "TypeName";
                    strSql += " FROM TimeSheet Ts,Project, Developer,TypeofWork" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" + " AND Ts.TOW_ID = TypeofWork.TOW_ID" +
                                " ORDER BY TypeofWork.Name,Project.Code,Developer.Account, ts.occur_date";
                    break;

                case 6:
                    //strOrder += "ProductName";
                    strSql += " FROM TimeSheet Ts,Project,Developer,WorkProduct" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.WP_ID = WorkProduct.WP_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY WorkProduct.Name,Project.Code,Developer.Account, ts.occur_date";
                    break;
            }
        }
        //logger.debug(strSql);
        return strSql;
    }

    /**
     * Method genLDSqlCommandGetIdList.
     * @param strListingName
     * @param nDevId
     * @param sProjectIdList
     * @param nStatus
     * @param strFromDate
     * @param strToDate
     * @return String
     */
    private String genLDSqlCommandGetIdList(String strListingName, int nDevId, String sProjectIdList, int nStatus, String strFromDate, String strToDate, String strAccount) {

        String strSql;
        String strExtra = "";
        String strFilter = "";

        strSql = "SELECT timesheet_id FROM Timesheet";

        StringBuffer strConstraint = new StringBuffer();
        StringBuffer strAccFilter = new StringBuffer();
        strConstraint.append(" WHERE project_id IN (" + sProjectIdList + ")");
        // Filter by selected account
        if ((strAccount != null) && (! "".equals(strAccount))) {
            strAccFilter.append(" AND developer_id=");
            strAccFilter.append("(SELECT developer_id FROM developer WHERE account='")
                        .append(CommonUtil.stringConvert(strAccount))
                        .append("')");
            strConstraint.append(strAccFilter);
        }
        //logger.debug("ApproveEJB.genLDSqlCommandGetIdList:" + strListingName);

        if (nStatus > 0)
            strExtra += " AND status = " + nStatus + "";
        else
            strExtra += " AND status IN (1, 2, 5)";

        //Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
        strExtra += SqlUtil.genDateConstraint("occur_date", strFromDate, strToDate);

        strConstraint.append(strExtra);

        if (!strFilter.equals("")) {
            if (sProjectIdList.lastIndexOf(',') != -1)
                strFilter += strExtra;
            else
                strFilter = strExtra;
        }
        strConstraint.append(strFilter);
        setPLDataConstraint(strConstraint.toString());

        strSql += strConstraint.toString();

        return strSql;
    }

    /**
     * Method getPLDataConstraint.
     * @return String
     */
    private String getPLDataConstraint() {
        return strDataConstraintSQL;
    }

    /**
     * Method setPLDataConstraint.
     * @param data
     */
    private void setPLDataConstraint(String data) {
        if (data != null)
            strDataConstraintSQL = data;
    }

    /**
     * Method selectLDObjectList.
     * @param strIdList
     * @param nSortby
     * @param nType
     * @return ArrayList
     * @throws SQLException
     */
    private ArrayList selectLDObjectList(String strIdList, int nSortby, int nType) throws SQLException {

        ArrayList arrList = new ArrayList();
        StringBuffer strBufferSQL = new StringBuffer();
        String strSQL;

        strSQL = genLDSqlCommandGetObject(strIdList, nSortby);

        if (nType == this.TIMESHEET_GENERAL) {
            int nStart = this.nCurrentPage * Timesheet.MAX_RECORDS;
            int nEnd = nStart + Timesheet.MAX_RECORDS;
            strBufferSQL.append(" SELECT * FROM ( ");
            strBufferSQL.append(" SELECT ROWNUM r, RN.* FROM ( ");
            strBufferSQL.append(strSQL);
            strBufferSQL.append(" ) RN )");
            strBufferSQL.append(" WHERE r > ").append(nStart).append(" AND r <= ").append(nEnd);
            strSQL = strBufferSQL.toString();
        }
        //logger.debug("ApprovalEJB.selectLDObjectList: strSql = " + strSQL);

        Statement stmt = null;
        ResultSet rs = null;

        try {
            if (ds == null)
                ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSQL);

            while (rs.next()) {

                ApproveModel atmData = new ApproveModel();

                atmData.setId(rs.getInt("Timesheet_Id"));
                atmData.setProject(rs.getString("ProjectCode"));
                atmData.setAccount(rs.getString("Account"));

                atmData.setDate(rs.getString("tsDate"));
                atmData.setDescription(rs.getString("Description"));
                atmData.setDuration(rs.getFloat("Duration"));

                if (rs.getString("Process_ID") != "null")
                    atmData.setProcess(rs.getInt("Process_ID"));

                if (rs.getString("TOW_ID") != "null")
                    atmData.setType(rs.getInt("TOW_ID"));

                if (rs.getString("WP_ID") != "null")
                    atmData.setProduct(rs.getInt("WP_ID"));

                atmData.setLeader(rs.getString("APPROVED_BY_LEADER"));
                atmData.setStatus(rs.getInt("STATUS"));
                atmData.setComment(rs.getString("rcomment"));
                arrList.add(atmData);
            }

            rs.close();
            stmt.close();

        }
        catch (SQLException ex) {
            logger.error(strClassName + ".selectLDObject():" + ex.toString());
            throw ex;
        }
        catch (Exception ex) {
            logger.error(strClassName + ".selectLDObject():" + ex.toString());
        }
        finally {
            conPool.releaseResource(con, stmt, rs, strClassName + ".selectLDObject():");
        } //finally

        return arrList;
    }

    /**
     * Method genQASqlCommandGetIdList.
     * @param sProjectIdList
     * @param nStatus
     * @param strFromDate
     * @param strToDate
     * @param strAccount
     * @return String
     */
    private static String genQASqlCommandGetIdList(String sProjectIdList, int nStatus, String strFromDate, String strToDate, String strAccount) {
        StringBuffer strSQL = new StringBuffer();
        strSQL.append("SELECT timesheet_id FROM timesheet");
        strSQL.append(" WHERE project_id IN (" + sProjectIdList + ")");
        if (nStatus > 0)
            strSQL.append(" AND status = " + nStatus);
        else
            strSQL.append(" AND status IN (2, 4)");

        //Modified by Tu Ngoc Trung, 2003-11-25. Skip null values of date
        strSQL.append(SqlUtil.genDateConstraint("occur_date", strFromDate, strToDate));
        //      strSQL.append(" AND rownum <= " + this.MAX_ROW_NUM);
        if ((strAccount != null) && (! "".equals(strAccount))) {
            strSQL.append(" AND developer_id=");
            strSQL.append("(SELECT developer_id FROM developer WHERE account='")
                    .append(CommonUtil.stringConvert(strAccount))
                    .append("')");
        }
        return strSQL.toString();
    }

    /**
     * Method genQASqlCommandGetObject.
     * @param strIdList
     * @param nSortby
     * @return String
     */
    private static String genQASqlCommandGetObject(String strIdList, int nSortby) {

        String strSql;
        strIdList = strIdList.trim();

        strSql = "SELECT Ts.Timesheet_Id,Ts.Description," + " to_char(Ts.OCCUR_DATE,'mm/dd/yy') as tsDate," + " Ts.Duration,Ts.Process_ID, Ts.TOW_ID," + " Ts.WP_ID,Ts.KPA_ID," + " Ts.APPROVED_BY_SEPG,Ts.STATUS," + " Project.Code AS ProjectCode," + " Developer.Account AS Account," + " Ts.rcomment";

        if (strIdList.length() > 0) {
            switch (nSortby) {
                /* 26-Aug-2004: changes sorting methods
                 * Sort by Account first then by Timesheet Date for easier view */
                case 1:
                    // Project Code
                    strSql += " FROM TimeSheet Ts,Project,Developer" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY Project.Code,Developer.Account, ts.occur_date";
                    break;

                case 2:
                    //strOrder += "Account";
                    strSql += " FROM TimeSheet Ts,Project, Developer" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY Account,ts.occur_date";
                    break;
                case 3:
                    //strOrder += "Occur_Date";
                    strSql += " FROM TimeSheet Ts,Project, Developer" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY Occur_Date,Project.Code,Developer.Account";
                    break;
                case 4:
                    //strOrder += "ProcessName";
                    strSql += " FROM TimeSheet Ts,Project, Developer,Process" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.Process_ID = Process.Process_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY Process.Name,Project.Code,Developer.Account, ts.occur_date";
                    break;
                case 5:
                    //strOrder += "TypeName";
                    strSql += " FROM TimeSheet Ts,Project, Developer,TypeofWork" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.TOW_ID = TypeofWork.TOW_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY TypeofWork.Name,Project.Code,Developer.Account, ts.occur_date";
                    break;
                case 6:
                    //strOrder += "ProductName";
                    strSql += " FROM TimeSheet Ts,Project,Developer,WorkProduct" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.WP_ID = WorkProduct.WP_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY WorkProduct.Name,Project.Code,Developer.Account, ts.occur_date";
                    break;

                case 7:
                    //strOrder += "Kpa Name";
                    strSql += " FROM TimeSheet Ts,Project,Developer,KPA" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.KPA_ID = KPA.KPA_ID(+)" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY KPA.Name,Project.Code,Developer.Account, ts.occur_date";
                    break;

                case 8:
                    //strOrder +"Group";
                    strSql += " FROM TimeSheet Ts,Project,Developer" +
                                " WHERE Ts.TimeSheet_ID IN (" + strIdList + ")" + " AND Ts.Project_ID = Project.Project_ID" + " AND Ts.Developer_ID = Developer.Developer_ID" +
                                " ORDER BY Project.Group_Name,Project.Code,Developer.Account, ts.occur_date";
                    break;
            }
        }

        //logger.debug(strSql);
        return strSql;
    }

    /**
     * Method selectQAObjectList.
     * @param strIdList
     * @param nSortby
     * @param nType
     * @return ArrayList
     * @throws SQLException
     */
    private ArrayList selectQAObjectList(String strIdList, int nSortby, int nType) throws SQLException {

        Statement stmt = null;
        ResultSet rs = null;

        ArrayList arrList = new ArrayList();
        String strSQL;
        StringBuffer strBufferSQL = new StringBuffer();

        strSQL = genQASqlCommandGetObject(strIdList, nSortby);

        if (nType == TIMESHEET_GENERAL) {
            int nStart = this.nCurrentPage * Timesheet.MAX_RECORDS;
            int nEnd = nStart + Timesheet.MAX_RECORDS;
            strBufferSQL.append(" SELECT * FROM ( ");
            strBufferSQL.append(" SELECT ROWNUM r, RN.* FROM ( ");
            strBufferSQL.append(strSQL);
            strBufferSQL.append(" ) RN )");
            strBufferSQL.append(" WHERE r > ").append(nStart).append(" AND r <= ").append(nEnd);
            strSQL = strBufferSQL.toString();
        }
        //logger.debug("ApprovalEJB.selectQAObjectList: strSQL = " + strSQL);

        try {
            if (ds == null)
                ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(strSQL);

            while (rs.next()) {

                ApproveModel atmData = new ApproveModel();

                atmData.setId(rs.getInt("Timesheet_Id"));
                atmData.setProject(rs.getString("ProjectCode"));
                atmData.setAccount(rs.getString("Account"));

                atmData.setDate(rs.getString("tsDate"));
                atmData.setDescription(rs.getString("Description"));
                atmData.setDuration(rs.getFloat("Duration"));

                if (rs.getString("Process_ID") != "null")
                    atmData.setProcess(rs.getInt("Process_ID"));

                if (rs.getString("TOW_ID") != "null")
                    atmData.setType(rs.getInt("TOW_ID"));

                if (rs.getString("WP_ID") != "null")
                    atmData.setProduct(rs.getInt("WP_ID"));

                if (rs.getString("KPA_ID") != null)
                    atmData.setKpa(rs.getInt("KPA_ID"));

                atmData.setQA(rs.getString("APPROVED_BY_SEPG"));
                atmData.setStatus(rs.getInt("STATUS"));
                atmData.setComment(rs.getString("rcomment"));
                arrList.add(atmData);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException ex) {
            logger.error(strClassName + ".selectQAObjectList():" + ex.toString());
            throw ex;
        }
        catch (Exception ex) {
            logger.error(strClassName + ".selectQAObjectList():" + ex.toString());
        }
        finally {
            conPool.releaseResource(con, stmt, rs, strClassName + ".selectQAObjectList():");
        } //finally

        return arrList;
    }

    //////////////////////////////////////
    /**
     * Method leaderChangeStatus.
     * @param nStatus
     * @param arrIdList
     * @param arrComment
     * @param strApprover
     * @throws SQLException
     */
    public void leaderChangeStatus(int nStatus, String[] arrIdList, String[] arrComment, String strApprover) throws SQLException {

		Statement stmt = null;
	    try {
		   String strTimesheetIds = "";
		   strTimesheetIds = CommonUtil.arrayToString(arrIdList, ",");
		   //" WHERE TIMESHEET_ID IN(" + strTimesheetId +")";
		   if (nStatus == LD_REJECT_STATUS) {
		       strApprover = "";
		   }
		   if (ds == null) {
			   ds = conPool.getDS();
		   }
		   con = ds.getConnection();
		   con.setAutoCommit(false);
		   stmt = con.createStatement();

           for (int i = 0; i < arrIdList.length; i++) {
                String strSQL1 = "UPDATE TIMESHEET SET " + 
									" STATUS = " + nStatus + "," + 
									" RCOMMENT= '" + CommonUtil.stringConvert(arrComment[i]) + "'," + 
									" APPROVED_BY_LEADER ='" + strApprover + "'" +
								" WHERE TIMESHEET_ID IN(" + arrIdList[i] +")";
				stmt.addBatch(strSQL1);
           }

		   String strSQL2 = "UPDATE TIMESHEET SET " +
								 " PLAPPROVEDTIME = TO_DATE(TO_CHAR(SYSDATE,'DD-MON-YYYY HH24:MI:SS'),'DD-MON-YYYY HH24:MI:SS')" +
							 " WHERE TIMESHEET_ID IN(" + strTimesheetIds +")";
		   stmt.addBatch(strSQL2);

		   stmt.executeBatch();
		   con.commit();
        }
        catch (SQLException ex) {
            logger.error(strClassName + ".leaderChangeStatus():" + ex.toString());
        }
        catch (Exception ex) {
            logger.error(strClassName + ".leaderChangeStatus():" + ex.toString());
        }
    }

    /**
     * Method qaChangeStatus.
     * @param nStatus
     * @param arrIdList
     * @param arrComment
     * @param strApprover
     * @throws SQLException
     */
    public void qaChangeStatus(int nStatus, String[] arrIdList, String[] arrComment, String strApprover) throws SQLException {
		Statement stmt = null;
	    try {
			String strTimesheetIds = "";
			strTimesheetIds = CommonUtil.arrayToString(arrIdList, ",");
			
		    if (nStatus == QA_REJECT_STATUS) {
			   strApprover = "";
		    }
		    if (ds == null) {
			   ds = conPool.getDS();
		    }
		    con = ds.getConnection();
		    con.setAutoCommit(false);
		    stmt = con.createStatement();        
        
            for (int i = 0; i < arrIdList.length; i++) {
                String strSQL1 = "UPDATE TIMESHEET SET " +
									" STATUS = " + nStatus + "," +
									" RCOMMENT= '" + CommonUtil.stringConvert(arrComment[i]) + "'," +
									" APPROVED_BY_SEPG ='" + strApprover + "'" +
								" WHERE TIMESHEET_ID IN(" + arrIdList[i] +")";

				stmt.addBatch(strSQL1);
            }

			String strSQL2 = "UPDATE TIMESHEET SET " +
								" QAAPPROVEDTIME = TO_DATE(TO_CHAR(SYSDATE,'DD-MON-YYYY HH24:MI:SS'),'DD-MON-YYYY HH24:MI:SS')" +
							" WHERE TIMESHEET_ID IN(" + strTimesheetIds +")";
			stmt.addBatch(strSQL2);

			stmt.executeBatch();
			con.commit();
        }
        catch (SQLException ex) {
            logger.error(strClassName + ".qaChangeStatus():" + ex.toString());
        }
        catch (Exception ex) {
            logger.error(strClassName + ".qaChangeStatus():" + ex.toString());
        }
    }

    /**
     * Method qaChangeStatus. Approve by QA, set KPA by get from Process-KPA mapping automatically
     * @param nStatus
     * @param arrIdList
     * @param arrComment
     * @param strApprover
     * @throws SQLException
     */
    private void qaChangeStatus(int nStatus, String[] arrIdList, String[] arrComment, String[] arrKPA, String strApprover) throws SQLException {
		Statement stmt = null;
		try {
			String strTimesheetIds = "";
		    strTimesheetIds = CommonUtil.arrayToString(arrIdList, ",");

			if (nStatus == QA_REJECT_STATUS) {
			   strApprover = "";
			}
			if (ds == null) {
			   ds = conPool.getDS();
			}
			con = ds.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();

            for (int i = 0; i < arrIdList.length; i++) {
                String strSQL1 = "UPDATE TIMESHEET SET" +
                    				" STATUS=" + nStatus + "," +
                    				" RCOMMENT='" + CommonUtil.stringConvert(arrComment[i]) + "'," +
				                    " KPA_ID=" + arrKPA[i] + "," +
				                    " APPROVED_BY_SEPG ='" + strApprover + "'" +
								" WHERE TIMESHEET_ID IN(" + arrIdList[i] +")";
				stmt.addBatch(strSQL1);
			}

			String strSQL2 = "UPDATE TIMESHEET SET " +
								" QAAPPROVEDTIME = TO_DATE(TO_CHAR(SYSDATE,'DD-MON-YYYY HH24:MI:SS'),'DD-MON-YYYY HH24:MI:SS')" +
							" WHERE TIMESHEET_ID IN(" + strTimesheetIds +")";
			stmt.addBatch(strSQL2);

            stmt.executeBatch();
            con.commit();
        }
        catch (SQLException ex) {
            logger.error(strClassName + ".qaChangeStatus():" + ex.toString());
        }
        catch (Exception ex) {
            logger.error(strClassName + ".qaChangeStatus():" + ex.toString());
        }
        finally {
            conPool.releaseResourceAndCommit(con, stmt, null, strClassName + ".qaChangeStatus():");
        }
    }

    /////////////////////////////////////////////////////////
    // Update information
    // AND
    //  + Approve
    //  + Update
    /////////////////////////////////////////////////////////

    /**
     * Method LDcorrect.
     * @param strApprover
     * @param sId
     * @param sProcess
     * @param sType
     * @param sProduct
     * @param sDuration
     * @param sDescription
     * @param sDate
     * @throws SQLException
     */
	  public void LDcorrect(String strApprover, String[] arrIdList, String[] sProcess, String[] sType, String[] sProduct, String[] sDuration, String[] sDescription, String[] sDate) throws SQLException {

		  String strSQL1;
		  String strSQL2;
		  int nStatus = 2; //mean Approve
		  int max = arrIdList.length;
		  PreparedStatement prestmt = null;
		  Statement stmt = null;

		  try {
			  String strTimesheetIds = "";
			  strTimesheetIds = CommonUtil.arrayToString(arrIdList, ",");

			  if (ds == null)
				  ds = conPool.getDS();
			  con = ds.getConnection();
			  con.setAutoCommit(false);
			  stmt = con.createStatement();

			  strSQL1 = "UPDATE TIMESHEET SET tow_id =?" + ",duration =?" + ",description =?" + ",status =?" + ",approved_by_leader =?" + ",process_id =?" + ",wp_id =?" + ",occur_date = TO_DATE(?,'mm/dd/yy') " + " WHERE timesheet_id =?";
			  //logger.debug("EJB.ApproveEJB.LDcorrect(): @@@@@@ strSQL = " + strSQL1.toString());

			  prestmt = con.prepareStatement(strSQL1);
			  for (int i = 0; i < max; i++) {
				  prestmt.setInt(1, Integer.parseInt(sType[i]));
				  prestmt.setFloat(2, Float.parseFloat(sDuration[i]));
				  prestmt.setString(3, sDescription[i]);
				  prestmt.setInt(4, nStatus);
				  prestmt.setString(5, strApprover);
				  prestmt.setInt(6, Integer.parseInt(sProcess[i]));
				  prestmt.setInt(7, Integer.parseInt(sProduct[i]));
				  prestmt.setString(8, sDate[i]);
				  prestmt.setInt(9, Integer.parseInt(arrIdList[i]));
				  prestmt.executeUpdate();
				  //prestmt.addBatch();
			  }
			  strSQL2 = "UPDATE TIMESHEET SET " +
						  " PLAPPROVEDTIME = TO_DATE(TO_CHAR(SYSDATE,'DD-MON-YYYY HH24:MI:SS'),'DD-MON-YYYY HH24:MI:SS')" +
					    " WHERE TIMESHEET_ID IN(" + strTimesheetIds +")";
			  stmt.executeUpdate(strSQL2);
			  con.commit();
		  }
		  catch (SQLException ex) {
			  con.rollback();
			  logger.error(strClassName + ".LDcorrect():" + ex.toString());
		  }
		  catch (Exception ex) {
			  con.rollback();
			  logger.error(strClassName + ".LDcorrect():" + ex.toString());
		  }
		  finally {
			  conPool.releaseResourceAndCommit(con, prestmt, null, strClassName + ".LDcorrect():");
		  } //finally
	  }

    /**
     * Method QAcorrect.
     * @param strApprover
     * @param nStatus
     * @param sId
     * @param sType
     * @param sProcess
     * @param sProduct
     * @param sKpa
     * @param sDate
     * @throws SQLException
     */
    public void QAcorrect(String strApprover, int nStatus, String[] arrIdList, String[] sType, String[] sProcess, String[] sProduct, String[] sKpa, String[] sDate) throws SQLException {
        int i = 0;
        int max = arrIdList.length;
        String strSQL1;
		String strSQL2;
        Statement stmt = null;
        try {
			String strTimesheetIds = "";
			strTimesheetIds = CommonUtil.arrayToString(arrIdList, ",");

            if (ds == null)
                ds = conPool.getDS();
            con = ds.getConnection();
            con.setAutoCommit(false);
            stmt = con.createStatement();

            // Correct only
            if (nStatus != QA_APPROVE_STATUS) {
                for (i = 0; i < max; i++) {
                    strSQL1 = " UPDATE TimeSheet SET" + 
							 	 " PROCESS_ID =" + sProcess[i] + 
								 ",TOW_ID =" + sType[i] + 
								 ",WP_ID =" + sProduct[i] + 
								 ",KPA_ID = " + sKpa[i] + 
								 ",occur_date = TO_DATE('" + sDate[i] + "','mm/dd/yy') " + 
							" WHERE TIMESHEET_ID IN(" + arrIdList[i] +")";
                    stmt.addBatch(strSQL1);
                }
            }
            //Correct and Approve
            else {
                for (i = 0; i < max; i++) {
					strSQL1 = " UPDATE TimeSheet SET" + 
									" PROCESS_ID =" + sProcess[i] + 
									" ,TOW_ID =" + sType[i] + 
									" ,WP_ID =" + sProduct[i] + 
									" ,KPA_ID =" + sKpa[i] + 
									" ,STATUS =" + nStatus + 
									" ,APPROVED_BY_SEPG ='" + strApprover + "'" + 
									",occur_date = TO_DATE('" + sDate[i] + "','mm/dd/yy') " + 
							  " WHERE TIMESHEET_ID IN(" + arrIdList[i] +")";
                    stmt.addBatch(strSQL1);
                }
            }
			strSQL2 = " UPDATE TIMESHEET SET " +
							" QAAPPROVEDTIME = TO_DATE(TO_CHAR(SYSDATE,'DD-MON-YYYY HH24:MI:SS'),'DD-MON-YYYY HH24:MI:SS')" +
					  " WHERE TIMESHEET_ID IN(" + strTimesheetIds +")";	
			stmt.addBatch(strSQL2);
			
            stmt.executeBatch();
            stmt.close();
            con.commit();
        }
        catch (SQLException ex) {
            con.rollback();
            logger.error(strClassName + ".QAcorrect():" + ex.toString());
        }
        catch (Exception ex) {
            con.rollback();
            logger.error(strClassName + ".QAcorrect():" + ex.toString());
        }
        finally {
            conPool.releaseResourceAndCommit(con, stmt, null, strClassName + ".QAcorrect():");
        } //finally

    }

    /**
     * Method executeQuery.
     * @param strSql
     * @throws SQLException
     */
    private void executeQuery(String strSql) throws SQLException {
        Statement stmt = null;
        try {
            if (ds == null)
                ds = conPool.getDS();
            con = ds.getConnection();
            stmt = con.createStatement();
            stmt.executeQuery(strSql);
            stmt.close();
        }
        catch (SQLException ex) {
            logger.error(strClassName + ".executeQuery():" + ex.toString());
            throw ex;
        }
        catch (Exception ex) {
            logger.error(strClassName + ".executeQuery():" + ex.toString());
        }
        finally {
            conPool.releaseResource(con, stmt, null, strClassName + ".executeQuery():");
        } //finally
    }

    //Thaison - Oct 15, 2002
    /**
     * Method setTotalPage.
     * @param nTotal
     */
    private void setTotalPage(int nTotal) {
        this.nTotalPage = nTotal;
    }

    /**
     * Method setTotalTimesheet.
     * @param nTotal
     */
    private void setTotalTimesheet(int nTotal) {
        this.nTotalTimesheet = nTotal;
    }

    /**
     * Method getTotalPage.
     * @return int
     */
    public int getTotalPage() {
        return this.nTotalPage;
    }

    /**
     * Method getTotalTimesheet.
     * @return int
     */
    public int getTotalTimesheet() {
        return this.nTotalTimesheet;
    }
}