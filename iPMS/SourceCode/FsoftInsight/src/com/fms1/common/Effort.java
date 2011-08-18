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
 
 package com.fms1.common;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.sql.Date;
import java.util.Vector;
import com.fms1.infoclass.*;
import com.fms1.tools.*;
import com.fms1.web.*;
import com.fms1.infoclass.group.*;
/**
 * Effort logic and interface with Timesheet data.
* @author: Hoang Thi Nga
*/
public final class Effort {
    public static double getEffortByTOW(final long projectID, int tow) {
        return getEffortByTOW(projectID, tow, new Date(0));
    }
    public static double getEffortByTOW(final long projectID, int tow, Date endDate) {
        return getEffortByTOW(projectID, tow, null, endDate, 0);
    }
    public static double getEffortByTOW_WP(int tow, int workProd, Date startDate, Date endDate) {
        return getEffortByTOW(0, tow, startDate, endDate, workProd);
    }
    //HUYNH2 modify function for projec archive
    public static double getEffortByTOW(final long projectID, int tow, Date startDate, Date endDate, int workProd) {
        Connection conn = null;
        PreparedStatement prepStmt = null;
        String sql = null;
        double revEffort = 0;
        ResultSet rs = null;
        try {
            String endDateCons = ((endDate == null) || (endDate.getTime() == 0)) ? "" : " AND OCCUR_DATE <=? ";
            String startDateCons = ((startDate == null) || (startDate.getTime() == 0)) ? "" : " AND OCCUR_DATE >=? ";
            String projCons = projectID > 0 ? " AND PROJECT_ID = ? " : "";
            String timsheetTable = "TIMESHEET";
            if(Db.checkProjecIsArchive(projectID)){                
                timsheetTable = "TIMESHEET_ARCHIVE";                 
            }
            String wpCons = workProd > 0 ? " AND WP_ID=? " : "";
            String misc = "";
            if (tow == EffortInfo.TOW_ENGINEERING_REVIEW) {
                misc = " AND " + timsheetTable + ".PROCESS_ID IN (" + ConvertString.arrayToString(ProcessInfo.engineeringProcesses, ",") + ") ";
                tow = EffortInfo.TOW_REVIEW;
            }
            sql =
                "SELECT SUM(DURATION)/(8) ACTUAL_REV_EFFORT "
                    + " FROM " + timsheetTable + " "
                    + " WHERE STATUS =4"
                    + " AND " + timsheetTable + ".TOW_ID =? "
                    + projCons
                    + endDateCons
                    + startDateCons
                    + wpCons
                    + misc;
            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setInt(1, tow);
            int i = 2;
            if (!"".equals(projCons))
                prepStmt.setLong(i++, projectID);
            if (!"".equals(endDateCons))
                prepStmt.setDate(i++, new java.sql.Date(endDate.getTime()));
            if (!"".equals(startDateCons))
                prepStmt.setDate(i++, new java.sql.Date(startDate.getTime()));
            if (!"".equals(wpCons))
                prepStmt.setInt(i++, workProd);
            rs = prepStmt.executeQuery();
            if (rs.next())
                revEffort = rs.getDouble("ACTUAL_REV_EFFORT");
            return revEffort;
        }
        catch (Exception e) {
            System.err.println("Effort.getEffortByTOW Error: " + sql);
            e.printStackTrace();
            return revEffort;
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
        }
    }
    /**
     * Get project effort information up to current system date
     * @param pinf
     * @return
     */
    public static final EffortInfo getEffortInfo(ProjectInfo projectInfo) {
        return getEffortInfo(projectInfo, null);
    }
    
    //Huynh2   add function for project archive
    // 1 - ACTUAL_MAN_EFFORT
    // 2 - ACTUAL_DEV_EFFORT
    // 3 - CORRECTION_COST
    // 4 - TRANS_COST 
    public static final boolean cacheActualEffortByProcess(long prjID, int process){
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        String columnName = "";
        boolean returnValue = false;
        double actualManEffort = 0;
        switch (process) {
            case    1:       
                sql =
                    "SELECT SUM(DURATION)/8 ACTUAL_MAN_EFFORT "
                        + " FROM TIMESHEET "
                        + " WHERE PROJECT_ID = ?"
                        + " AND STATUS =4"
                        + " AND PROCESS_ID IN (1,9,11,15,16,19,20, 21,22, 23,24,25, 27)" ;//12 process + 27 = Technology management
                columnName = "ACTUAL_MAN_EFFORT" ;
                break;       
            case    2:
                sql =
                    "SELECT SUM(DURATION)/8 ACTUAL_DEV_EFFORT "
                        + " FROM TIMESHEET "
                        + " WHERE PROJECT_ID = ?"
                        + " AND STATUS =4"
                        + " AND PROCESS_ID IN (2, 3, 4, 5, 6, 8, 26)"
                        + " AND TOW_ID IN (1, 2,6)";
                columnName = "ACTUAL_DEV_EFFORT" ;
                break;       
            case    3:
                sql =
                    "SELECT SUM(DURATION)/8 CORRECTION_COST "
                        + " FROM TIMESHEET "
                        + " WHERE PROJECT_ID = ?"
                        + " AND STATUS=4 "
                        + " AND (( PROCESS_ID IN (2, 3, 4, 5, 6, 7, 8,26)"
                        + " AND TOW_ID = 5) "
                        + " OR ( PROCESS_ID = 14))";
                columnName = "CORRECTION_COST" ;
                break;       
            case    4:                  
                sql =
                    "SELECT SUM(DURATION)/8 TRANS_COST "
                        + " FROM TIMESHEET "
                        + " WHERE PROJECT_ID = ?"
                        + " AND STATUS=4"
                        + " AND TOW_ID = 6";
                columnName = "TRANS_COST" ;
                break;       
                    
        }
        try{
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1,prjID);
            rs = stm.executeQuery();
            while(rs.next()){
                actualManEffort = rs.getDouble(columnName);
            }
            rs.close();
            if(Db.checkProjectHaveArchive(prjID))
                sql = "UPDATE project_archive SET "+ columnName + " = ? WHERE project_id = ?";
            else
                sql = "INSERT INTO project_archive("+ columnName + ",project_id) VALUES(?,?)";            
            stm = conn.prepareStatement(sql);
            stm.setDouble(1,actualManEffort);
            stm.setLong(2,prjID);                
            if(stm.executeUpdate()>0){
                returnValue = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Effort class, sql stmt = " + sql);
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return returnValue;
        }        
    }
    
    /**
     * Get project effort information up to specified date
     * @param pinf
     * @param date
     * @return
     */
    public static final EffortInfo getEffortInfo(ProjectInfo projectInfo, Date date) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        EffortInfo info = new EffortInfo();
        ResultSet rs = null;
        String dateConstraint = null;
        String timesheetTable = "";
        long time = 0;
        long actual_finish_date = 0;
        if(date != null)
           time = date.getTime();
        if(projectInfo.getActualFinishDate() != null)
            actual_finish_date = projectInfo.getActualFinishDate().getTime();
        boolean check = false;
        if(time == 0)
            check = true;
        if(time >= actual_finish_date)
            check = true;
        check =  (projectInfo.getArchiveStatus() == 4) && check;
        
        
        // check project is archive and move direction.
        if(projectInfo.getArchiveStatus() == 4){
            timesheetTable = "TIMESHEET_ARCHIVE";            
        }else{
            timesheetTable = "TIMESHEET";
        }
        
          
        try {
            boolean hasDate = (date != null);
            String strDate = hasDate ? CommonTools.dateFormat(date) : null;
            dateConstraint = hasDate ? " AND OCCUR_DATE <= ?" : "";
            //budgeted effort and actual
            info.plannedEffort = projectInfo.getBaseEffort();
            info.rePlannedEffort = projectInfo.getPlanEffort();
            info.budgetedEffort = projectInfo.getPlannedEffort();
            info.actualEffort = Effort.getActualEffort(projectInfo.getProjectId(), null, date); //(Date)();
            info.calendarEffort = Assignments.getActualCalendarEffort(projectInfo, date);
            /*
            if (info.calendarEffort > 0) {
                // Get billable effort up to date
                double ratio = CommonTools.getMilestoneRatio(
                        pinf.getLatestStartDate(),
                        pinf.getLatestFinishDate(),
                        (date != null) ? date : new java.util.Date());
                info.effortEfficiency = pinf.getLatestBillableEffort() *
                    ratio * 100.0 / info.calendarEffort;
            }*/
            // Changed in 28-Feb-07:
            // Effort efficiency for on-going project=Last committed
            //   billable(Actual if any)/Last committed Calendar
            // Effort efficiency for closed project=
            //   Actual billable/Actual Calendar
            info.effortEfficiency = getEffortEfficiency(projectInfo);
            // Effort effectiveness
            if (info.budgetedEffort != 0) {
                info.effortUseage = (info.actualEffort * 100d / info.budgetedEffort);
                // effort deviation
                info.effortDeviation = info.effortUseage - 100d;
            }
            else if (info.actualEffort == 0) {
                info.effortUseage = 0;
                info.effortDeviation = 0;
            }
            else {
                info.effortUseage = Double.NaN;
                info.effortDeviation = Double.NaN;
            }
            if (info.actualEffort != 0) {
                conn = ServerHelper.instance().getConnection();
                // developement effort, quality effort, management effort
                // Huynh2 modify this function for project archive
                if(check){//archive
                    sql = "SELECT ACTUAL_MAN_EFFORT FROM project_archive WHERE project_id = ?";
                }else{                
                    sql =
                        "SELECT SUM(DURATION)/8 ACTUAL_MAN_EFFORT "
                            + " FROM " + timesheetTable + " "
                            + " WHERE PROJECT_ID = ?"
                            + " AND STATUS =4"
                            + " AND PROCESS_ID IN (1,9,11,15,16,19,20, 21,22, 23,24,25, 27)" //12 process + 27 = Technology management
                            + dateConstraint;
                }                        
                stm = conn.prepareStatement(sql);
                stm.setLong(1, projectInfo.getProjectId());
                if (hasDate&&!check) {
                    stm.setString(2, strDate);
                }
                rs = stm.executeQuery();
                if (rs.next()) {
                    info.managementEffort = rs.getDouble("ACTUAL_MAN_EFFORT");
                    info.perManagementEffort  = info.managementEffort * 100d / info.actualEffort;
                }                
                rs.close();
                if(check){//archive
                    sql = "SELECT ACTUAL_DEV_EFFORT FROM project_archive WHERE project_id = ?";
                }
                else{                
                    sql =
                        "SELECT SUM(DURATION)/8 ACTUAL_DEV_EFFORT "
                            + " FROM " + timesheetTable + " "
                            + " WHERE PROJECT_ID = ?"
                            + " AND STATUS =4"
                            + " AND PROCESS_ID IN (2, 3, 4, 5, 6, 8, 26)"
                            + " AND TOW_ID IN (1, 2,6)"
                            + dateConstraint;
                }
                stm = conn.prepareStatement(sql);
                stm.setLong(1, projectInfo.getProjectId());
                if (hasDate&&!check) {
                    stm.setString(2, strDate);
                }                            
                rs = stm.executeQuery();
                if (rs.next()) {
                    info.developementEffort = rs.getDouble("ACTUAL_DEV_EFFORT");
                    info.perDevelopementEffort = info.developementEffort * 100d / info.actualEffort;
                }
                rs.close();
                //correction effort
                if(check){//archive
                    sql = "SELECT CORRECTION_COST FROM project_archive WHERE project_id = ?";
                }
                else{                
                    sql =
                        "SELECT SUM(DURATION)/8 CORRECTION_COST "
                            + " FROM  " + timesheetTable + " "
                            + " WHERE PROJECT_ID = ?"
                            + " AND STATUS=4 "
                            + " AND (( PROCESS_ID IN (2, 3, 4, 5, 6, 7, 8,26)"
                            + " AND TOW_ID = 5) "
                            + " OR ( PROCESS_ID = 14))"
                            + dateConstraint;
                }
                stm = conn.prepareStatement(sql);
                stm.setLong(1, projectInfo.getProjectId());
                if (hasDate&&!check) {
                    stm.setString(2, strDate);
                }
                rs = stm.executeQuery();
                if (rs.next()) {
                    info.correctionEffort = rs.getDouble("CORRECTION_COST");
                    info.perCorrectionEffort = info.correctionEffort * 100d / info.actualEffort;
                }
                rs.close();
                info.qualityEffort = (info.actualEffort - info.developementEffort - info.managementEffort);
                info.perQualityEffort = info.qualityEffort * 100d / info.actualEffort;
                //translation effort
                if(check){//archive
                    sql = "SELECT TRANS_COST FROM project_archive WHERE project_id = ?";
                }
                else{                
                    sql =
                        "SELECT SUM(DURATION)/8 TRANS_COST "
                            + " FROM  " + timesheetTable + " "
                            + " WHERE PROJECT_ID = ?"
                            + " AND STATUS=4"
                            + " AND TOW_ID = 6"
                            + dateConstraint;
                }
                stm = conn.prepareStatement(sql);
                stm.setLong(1, projectInfo.getProjectId());
                if (hasDate&&!check) {
                    stm.setString(2, strDate);
                }
                rs = stm.executeQuery();
                if (rs.next()) {
                    info.translationEffort = rs.getDouble("TRANS_COST");
                    info.perTranslationEffort = info.translationEffort * 100d / info.actualEffort;
                }
                rs.close();
            }
            else {
                info.correctionEffort = Double.NaN;
                info.managementEffort = Double.NaN;
                info.developementEffort = Double.NaN;
                info.qualityEffort = Double.NaN;
                info.translationEffort = Double.NaN;
            }
            return info;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("sql stmt = " + sql);
            return info;
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
        }
    }
    /**
     * Get project effort efficiency metric
     * @param pinf
     * @return
     */
    public static double getEffortEfficiency(ProjectInfo projectInfo) {
        // Changed in 28-Feb-07:
        // Effort efficiency for on-going project=Last committed
        //   billable(Actual if any)/Last committed Calendar
        // Effort efficiency for closed project=
        //   Actual billable/Actual Calendar
        double effortEfficiency = Double.NaN;
        // 03-Mar-07: temporary disable status closed condition
        if (/*(pinf.status == ProjectInfo.STATUS_CLOSED) &&*/
            (projectInfo.getActualFinishDate() != null))
        {
            double calEffort = Assignments.getActualCalendarEffort(
			projectInfo, null);
            if (calEffort > 0) {
                effortEfficiency =
				projectInfo.getLatestBillableEffort() * 100.0 /
                    calEffort;
            }
        }
        else {  // On-going projects => get by planned calendar
            if (projectInfo.getPlannedCalendarEffort() > 0) {
                effortEfficiency =
				projectInfo.getLatestBillableEffort() * 100.0 /
				projectInfo.getPlannedCalendarEffort();
            }
        }
        return effortEfficiency;
    }
    /**
     * Get calendar effort of project
     * @param pinf
     * @return
     */
    public static double getCalendarEffort(ProjectInfo projectInfo) {
        double calEffort = Double.NaN;
        // 03-Mar-07: temporary disable status closed condition
        if (/*(pinf.status == ProjectInfo.STATUS_CLOSED) &&*/
            (projectInfo.getActualFinishDate() != null))
        {
            calEffort = Assignments.getActualCalendarEffort(
			projectInfo, null);
        }
        else {  // On-going projects => get by planned calendar
            calEffort = projectInfo.getPlannedCalendarEffort();
        }
        return calEffort;
    }
    /**
     * Get calendar effort of project by ProjectId
     * @param projectId
     * @return
     */
    public static double getCalendarEffort(long projectId) {
		ProjectInfo projectInfo = Project.getProjectInfo(projectId);
        double calEffort = Double.NaN;
        // 03-Mar-07: temporary disable status closed condition
        if (/*(pinf.status == ProjectInfo.STATUS_CLOSED) &&*/
            (projectInfo.getActualFinishDate() != null))
        {
            calEffort = Assignments.getActualCalendarEffort(
			projectInfo, null);
        }
        else {  // On-going projects => get by planned calendar
            calEffort = projectInfo.getPlannedCalendarEffort();
        }
        return calEffort;
    }
    /**
	 * @param prjIDs
	 * @param startD
	 * @param endD
	 * @return
	 */
    // Huynh2 modify for project archive 
    private static double getGroupActualEffort(final long[] prjIDs, final java.sql.Date startD, final java.sql.Date endD) {
        String insertStatement = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        double rslt = 0;
        ResultSet rs = null;
        try {
            if (prjIDs.length == 0)
                return rslt;
            String projectConstraint = ConvertString.arrayToString(prjIDs, ", ");
            String dateCondition = null;
            boolean dateNull = false;
            /*((startD == null) ||*/
            if (endD == null) {
                dateCondition = "";
                dateNull = true;
            }
            else
                dateCondition = " AND OCCUR_DATE <= ?" + ((startD != null) ? " AND OCCUR_DATE >= ?" : "");
            insertStatement =
                "SELECT SUM(DURATION)/8 ACTUAL_EFFORT FROM TIMESHEET "
                    + " WHERE PROJECT_ID IN("
                    + projectConstraint
                    + ") AND STATUS=4 "
                    + dateCondition;
            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(insertStatement);
            if (!dateNull) {
                prepStmt.setDate(1, endD);
                if (startD != null)
                    prepStmt.setDate(2, startD);
            }
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                rslt = rs.getDouble("ACTUAL_EFFORT");
            }            
            rs.close();
            prepStmt.close();
            
            //HuyNH2 add some line code for project archive
            insertStatement =
                "SELECT SUM(DURATION)/8 ACTUAL_EFFORT FROM TIMESHEET_ARCHIVE "
                    + " WHERE PROJECT_ID IN("
                    + projectConstraint
                    + ") AND STATUS=4 "
                    + dateCondition;
            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(insertStatement);
            if (!dateNull) {
                prepStmt.setDate(1, endD);
                if (startD != null)
                    prepStmt.setDate(2, startD);
            }
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                rslt += rs.getDouble("ACTUAL_EFFORT");
            }
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("SQL stm = " + insertStatement);
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
            return rslt;
        }
    }
    /**
     * Get actual effort of all projects
     * @param startDate
     * @param endDate
     * @return
     */
    public static double getActualEffort(java.sql.Date startDate, java.sql.Date endDate) {
        return getActualEffort(0, startDate, endDate);
    }
    /**
     * Get actual effort of project
     * @param projectID
     * @return
     */
    public static double getActualEffort(final long projectID) {
        return getActualEffort(projectID, null, null);
    }
    /**
     * Get actual effort of a project or all projects if project_id=0
     * @param prjID
     * @param startD
     * @param endD
     * @return
     */
    public static double getActualEffort(long prjID, Date startD, Date endD) {
        String sql = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        double rslt = -1;
        ResultSet rs = null;
        //HuyNH2 add some code line for project archive. 
        String timesheetTable = "TIMESHEET";        
        try {
            String dateCondition = "";
            if (startD != null)
                dateCondition = " OCCUR_DATE >= ?  AND ";
            if (endD != null)
                dateCondition = dateCondition + " OCCUR_DATE <= ?  AND ";
            String projCondition = (prjID == 0) ? "" : " PROJECT_ID = ? AND ";
            conn = ServerHelper.instance().getConnection();
            sql =
                "SELECT SUM(DURATION)/(8) ACTUAL_EFFORT "
                + " FROM  " + timesheetTable
                + " WHERE " + projCondition + dateCondition + "STATUS=4 ";
            prepStmt = conn.prepareStatement(sql);
            int i = 1;
            if (!"".equals(projCondition))
                prepStmt.setLong(i++, prjID);
            if (startD != null)
                prepStmt.setDate(i++, startD);
            if (endD != null)
                prepStmt.setDate(i++, endD);
            rs = prepStmt.executeQuery();
            if (rs.next())
                rslt = rs.getDouble("ACTUAL_EFFORT");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Effort.getActualEffort SQL = " + sql);                            
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
            return rslt;
        }
    }
    
    /**
     * Get actual effort of projects belong to a work unit within a selected period
     * @param wu
     * @param startD
     * @param endD
     * @param isAccumulate Determine the result is accumulate of effort
     * @return
     */
    public static double getActualEffortPeriod(
        WorkUnitInfo wu, Date startDate, Date endDate, boolean isAccumulate)
    {
        // If get effort of a project
        if (wu.type == WorkUnitInfo.TYPE_PROJECT && wu.tableID > 0) {
            return getActualEffortPeriod(
                wu.tableID, startDate, endDate, isAccumulate);
        }
        
        double effort = Double.NaN;
        String sql = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            String timesheetFilter = "";
            if (startDate != null && !isAccumulate) {
                timesheetFilter += " AND T.OCCUR_DATE >= ?";
            }
            if (endDate != null) {
                timesheetFilter += " AND T.OCCUR_DATE <= ?";
            }

            // Filter by project dates
            String projectFilter =
                    " AND P.STATUS <> " + ProjectInfo.STATUS_CANCELLED
                    + " AND P.STATUS <> " + ProjectInfo.STATUS_TENTATIVE;
            if (startDate != null) {
                projectFilter += " AND P.START_DATE <=?";
            }
            if (endDate != null) {
                projectFilter += " AND (P.ACTUAL_FINISH_DATE IS NULL"
                                + " OR P.ACTUAL_FINISH_DATE >=?)";
            }
            // Filter by group if calculate for group
            // (for Organization: select all projects so it is not necessary
            //  to set filter by organization)
            if (wu.type == WorkUnitInfo.TYPE_GROUP) {
                projectFilter += " AND P.GROUP_NAME=?";
            }
            
            // T.status=4 means select effort that approved by QA
            sql =
                "SELECT /*Use_Hash(p)*/ SUM(DURATION)/(8) ACTUAL_EFFORT"
                + " FROM TIMESHEET T, project P"
                + " WHERE P.project_id=T.project_id AND T.STATUS=4 "
                + timesheetFilter + projectFilter;

System.out.println("Effort.getActualEffortPeriod():sql=" + sql +
            ";startDate=" + startDate + ";endDate=" + endDate +
            ";isAccumulate=" + isAccumulate + ";workUnitID=" + wu.workUnitID +
            ";workUnitName=" + wu.workUnitName);

            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sql);
            int i = 1;
            // Timesheet occur_date filter
            if (startDate != null && !isAccumulate) {
                prepStmt.setDate(i++, startDate);
            }
            if (endDate != null) {
                prepStmt.setDate(i++, endDate);
            }
            //Project dates filter
            if (startDate != null) {
                prepStmt.setDate(i++, startDate);
            }
            if (endDate != null) {
                prepStmt.setDate(i++, endDate);
            }
            // Filter by group if calculate for group
            if (wu.type == WorkUnitInfo.TYPE_GROUP) {
                prepStmt.setString(i++, wu.workUnitName);
            }
            
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                effort = rs.getDouble("ACTUAL_EFFORT");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Effort.getActualEffort SQL = " + sql);                            
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
            return effort;
        }
    }

    /**
     * Get actual effort of project within a selected period. If passed
     * projectId=0 => select effort of all projects (that means for organization)
     * @param projectId
     * @param startDate
     * @param endDate
     * @param isAccumulate
     * @return
     */
    public static double getActualEffortPeriod(
        long projectId, Date startDate, Date endDate, boolean isAccumulate)
    {
        double effort = Double.NaN;
        String sql = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            String projCondition = (projectId > 0) ? " AND P.project_id=?" : "";
            // Filter by timesheet date
            String dateCondition = "";
            if (startDate != null && !isAccumulate) {
                dateCondition +=  " AND T.OCCUR_DATE >=?";
            }
            if (endDate != null) {
                dateCondition +=  " AND T.OCCUR_DATE <=?";
            }
            // Filter by project date (passed projectId=0 in => For organization)
            if (projectId <= 0) {
                dateCondition +=
                    (startDate != null) ?
                    " AND NVL(P.ACTUAL_FINISH_DATE, NVL(P.PLAN_FINISH_DATE," +
                                " P.BASE_FINISH_DATE)) >=?" : "";
                dateCondition +=
                    (endDate != null) ? " AND P.START_DATE <=?" : "";
            }
            // T.status=4 means select effort that approved by QA
            sql =
                "SELECT /*Use_Hash(p)*/ SUM(DURATION)/(8) ACTUAL_EFFORT"
                + " FROM TIMESHEET T, project P"
                + " WHERE P.project_id=T.project_id AND T.STATUS=4 "
                + projCondition + dateCondition;

System.out.println("Effort.getActualEffortPeriod():sql=" + sql +
            ";startDate=" + startDate + ";endDate=" + endDate +
            ";projectId=" + projectId + ";isAccumulate=" + isAccumulate);

            conn = ServerHelper.instance().getConnection();
            prepStmt = conn.prepareStatement(sql);
            int i = 1;
            if (projectId > 0) {
                prepStmt.setLong(i++, projectId);
            }
            if (startDate != null && !isAccumulate) {
                prepStmt.setDate(i++, startDate);
            }
            if (endDate != null) {
                prepStmt.setDate(i++, endDate);
            }
            if (projectId <= 0) {
                if (startDate != null) {
                    prepStmt.setDate(i++, startDate);
                }
                if (endDate != null) {
                    prepStmt.setDate(i++, endDate);
                }
            }
            
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                effort = rs.getDouble("ACTUAL_EFFORT");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Effort.getActualEffort SQL = " + sql);                            
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
            return effort;
        }
    }

    //Huynh2 add code for project archive
    public static double getArchiveActualEffort(long prjID) {
            String insertStatement = "";
            Connection conn = null;
            PreparedStatement prepStmt = null;
            double rslt = -1;
            ResultSet rs = null;
            try {
                String dateCondition = "";
                conn = ServerHelper.instance().getConnection();
                insertStatement =
                    "SELECT actual_effort  FROM project_archive WHERE project_id = ?";
                prepStmt = conn.prepareStatement(insertStatement);
                prepStmt.setLong(1,prjID);
                rs = prepStmt.executeQuery();
                if (rs.next())
                    rslt = rs.getDouble("ACTUAL_EFFORT");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                ServerHelper.closeConnection(conn, prepStmt, rs);
                return rslt;
            }
    }
    //cache actual effort
    /**
     * @author:Nguyen Huu Huy - account: HuyNH2
     * @return boolean 
     * @param prjID
     */    
    public static boolean cacheActualEffort(long prjID){
        boolean returnValue = true;
        double actualEffort = 0;
        boolean isUpdate = Db.checkProjectHaveArchive(prjID);
        int numUpdateItem = 0;
        Connection conn = null;
        PreparedStatement prepStm = null;
        String sql = null;                    
        actualEffort = getActualEffort(prjID);
        // cache data
        // check for insert or update;
        if(isUpdate){
            sql = "UPDATE project_archive SET actual_effort = ? WHERE project_id = ?";
        }
        else{            
            sql = "INSERT INTO project_archive(actual_effort,project_id) VALUES (?, ?)";
        }
        try{
            conn = ServerHelper.instance().getConnection();
            prepStm = conn.prepareStatement(sql);
            prepStm.setDouble(1,actualEffort);
            prepStm.setDouble(2,prjID);
            numUpdateItem = prepStm.executeUpdate();            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            ServerHelper.closeConnection(conn, prepStm, null);
            if(numUpdateItem > 0){
                return true;
            }
            else{
                return false;
            }
        }
    }
    //end 
    
    /**
     * @author:Hoang Thi Nga
     * @return Vector containing weekly effort info
     * @param prjID
     */
    public static final Vector getWeeklyEffortList(final long prjID) {
        Connection conn = null;
        PreparedStatement prepStm = null;
        String sql = null;
        Vector vt = new Vector();
        ResultSet rs = null;
        java.sql.Date endD = null;
        java.sql.Date beginD = null;
        final java.util.Date today = new java.util.Date();
        java.sql.Date nextD = new java.sql.Date(0);
        double aEffort = -1;
        try {
            ProjectDateInfo pinfo = Project.getProjectDate(prjID);
            endD = (pinfo.actualEndDate == null) ? new java.sql.Date(today.getTime()) : pinfo.actualEndDate;
            beginD = pinfo.actualStartDate;
            Vector mondayList = CommonTools.getMondayList(beginD, endD);
            if (mondayList.size() == 0)
                mondayList.add(beginD);
            else if (((java.util.Date)mondayList.elementAt(0)).after((java.util.Date)beginD)) //add the start date of project to the list
                mondayList.add(0, (java.util.Date)beginD);
            int nmonday = mondayList.size();
            for (int i = 0; i < nmonday; i++) {
                final WeeklyEffortInfo info = new WeeklyEffortInfo();
                info.prjID = prjID;
                info.date = new java.sql.Date(((java.util.Date)mondayList.elementAt(i)).getTime());
                if (i == nmonday - 1)
                    nextD = endD;
                else
                    nextD.setTime(((java.util.Date)mondayList.elementAt(i + 1)).getTime() - 1000);
                aEffort = getActualEffort(prjID, info.date, nextD);
                if (aEffort != -1) {
                    info.actualE = aEffort;
                }
                vt.add(info);
            }
            conn = ServerHelper.instance().getConnection();
            sql = "SELECT * FROM WEEKLY_EFFORT WHERE PROJECT_ID = ? AND START_DATE <= ? AND START_DATE >= ? ORDER BY START_DATE ASC";
            prepStm = conn.prepareStatement(sql);
            prepStm.setLong(1, prjID);
            prepStm.setDate(2, endD);
            prepStm.setDate(3, beginD);
            rs = prepStm.executeQuery();
            //check which weeks have already been planned
            while (rs.next()) {
                boolean found = false;
                Date monday = rs.getDate("START_DATE");
                WeeklyEffortInfo weeklyEffortInfo = null;
                for (int i = 0; i < vt.size(); i++) {
                    weeklyEffortInfo = (WeeklyEffortInfo)vt.elementAt(i);
                    if (weeklyEffortInfo.date.equals(monday)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    weeklyEffortInfo.weeklyE_ID = rs.getLong("WEEKLY_EFFORT_ID");
                    weeklyEffortInfo.estimatedE = rs.getFloat("PLANNED_EFFORT");
                }
                else {
                    System.err.println("Effort.getWeeklyEffortList error : Bad monday in Database");
                    continue;
                }
                //Caculate actual effort of Weekly Effort from Timesheet
                //Caculate deviation
                if (!((Double.isNaN(weeklyEffortInfo.actualE))
                    || (Double.isNaN(weeklyEffortInfo.estimatedE))
                    || (weeklyEffortInfo.estimatedE == 0))) {
                    weeklyEffortInfo.deviation =
                        (weeklyEffortInfo.actualE - weeklyEffortInfo.estimatedE) * 100.0 / weeklyEffortInfo.estimatedE;
                }
            }
            return vt;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("sql stmt = " + sql);
            return vt;
        }
        finally {
            ServerHelper.closeConnection(conn, prepStm, rs);
        }
    }
    /**
     * @author Hoang Thi Nga
     * @project: Fms-1
     * @param:WeeklyEffortInfo
     * @return boolean
     */
    public static final boolean updateWeeklyEffort(final WeeklyEffortInfo info) {
        String updateStatement = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        try {
            conn = ServerHelper.instance().getConnection();
            if (info.weeklyE_ID != -1) {
                updateStatement = " UPDATE WEEKLY_EFFORT  SET PLANNED_EFFORT = ?" + " WHERE WEEKLY_EFFORT_ID = ?";
                prepStmt = conn.prepareStatement(updateStatement);
                prepStmt.setDouble(1, info.estimatedE);
                prepStmt.setLong(2, info.weeklyE_ID);
                final int rowCount = prepStmt.executeUpdate();
                if (rowCount == 0) {
                    System.err.println("weeky effort update failed");
                    return false;
                }
                else
                    return true;
            }
            else { //add new
                updateStatement =
                    " INSERT INTO WEEKLY_EFFORT (WEEKLY_EFFORT_ID,PROJECT_ID,PLANNED_EFFORT,START_DATE)"
                        + " VALUES( NVL((SELECT MAX(WEEKLY_EFFORT_ID)+1 FROM WEEKLY_EFFORT),1),?,?,?)";
                prepStmt = conn.prepareStatement(updateStatement);
                prepStmt.setLong(1, info.prjID);
                prepStmt.setDouble(2, info.estimatedE);
                prepStmt.setDate(3, info.date);
                final int rowCount = prepStmt.executeUpdate();
                if (rowCount == 0) {
                    System.err.println("weeky effort insert failed");
                    return false;
                }
                else
                    return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
        }
    }
    /**
     * @return Vector containing stage effort info
     * @param prjID
     * in case the login changes,
     *  please also update the function getEffortDistByStage below this one
     */
    public static final StageEffortInfo getStageEffort(ProjectInfo projectInfo, Vector processEffortByStages, long stageid) {
        ProcessEffortByStageInfo processEffortByStage;
        Vector vt = new Vector();
        for (int i = 0; i < processEffortByStages.size(); i++) {
            processEffortByStage = (ProcessEffortByStageInfo)processEffortByStages.elementAt(i);
            if (processEffortByStage.stageid == stageid) {
                vt.add(processEffortByStage);
                return (StageEffortInfo)getStageEffortList(projectInfo, vt).elementAt(0);
            }
        }
        return null;
    }
    /**
     * Just sums the values in the vector of ProcessEffortByStageInfo
     * @param vector ProcessEffortByStageInfo
     * @return vector of StageEffortInfo
     */
    public static final Vector getStageEffortList(ProjectInfo projectInfo, Vector processEffortByStages) {
        Vector stageEffortList = new Vector();
        ProcessEffortByStageInfo processEffortByStage;
        StageEffortInfo info;

		double totalPlannedEffort = 0;
		double averageAdjustment = 0;

		double totalNormEffort = 0;
		double averageAdjustment2 = 0;

        for (int i = 0; i < processEffortByStages.size(); i++) {
            processEffortByStage = (ProcessEffortByStageInfo)processEffortByStages.elementAt(i);
            info = new StageEffortInfo();
            info.prjID = projectInfo.getProjectId();
            info.estimated = CommonTools.arraySum(processEffortByStage.planned);
            info.reEstimated = CommonTools.arraySum(processEffortByStage.rePlannedLatest);
            
            info.stage = processEffortByStage.stage_name;
            info.norm = CommonTools.arraySum(processEffortByStage.norm);
            info.actual = CommonTools.arraySum(processEffortByStage.actual);
            info.deviation = CommonTools.metricDeviation(info.estimated, info.reEstimated, info.actual);
            stageEffortList.add(info);
            
			totalPlannedEffort = totalPlannedEffort + info.estimated;
			totalNormEffort = totalNormEffort + info.norm;
        }

		if (stageEffortList.size() > 0)
		{
			averageAdjustment = (projectInfo.getBaseEffort() - totalPlannedEffort) / stageEffortList.size();
			averageAdjustment2 = (projectInfo.getBaseEffort() - totalNormEffort) / stageEffortList.size();
		}

		for (int i=0; i < stageEffortList.size(); i++) {
			info=(StageEffortInfo)stageEffortList.get(i);
			//info.estimated = info.estimated + averageAdjustment;			
			//info.deviation = CommonTools.metricDeviation(info.estimated, info.reEstimated, info.actual);

			info.norm = info.norm + averageAdjustment2;
		}

        return stageEffortList;
    }
    /**
     * gets effort distribution by stage for several projects
     * see above for a similar function
     */
    public static final PCBEffortDistribInfo[] getEffortDistByStage(Vector projectInfoList) {
        PCBEffortDistribInfo[] result = new PCBEffortDistribInfo[StageInfo.stageList.length];
        double values[] = new double[StageInfo.stageList.length];
        java.util.Arrays.fill(values, 0);
        double tempVal, totalEffort = 0;
        for (int i = 0; i < projectInfoList.size(); i++) {
            ProjectInfo projectInfo = (ProjectInfo)projectInfoList.elementAt(i);
            Vector stageList = Schedule.getStageList(projectInfo);
            for (int j = 0; j < stageList.size(); j++) {
                StageInfo stageInfo = (StageInfo)stageList.elementAt(j);
                if ((stageInfo.actualBeginDate != null) && (stageInfo.aEndD != null)) {
                    if (stageInfo.StandardStage > 0) {
                        tempVal = getActualEffort(projectInfo.getProjectId(), stageInfo.actualBeginDate, stageInfo.aEndD);
                        if (tempVal > 0) {
                            totalEffort += tempVal;
                            values[stageInfo.StandardStage - 1] += tempVal;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < result.length; i++) {
            result[i] = new PCBEffortDistribInfo();
            result[i].id = i + 1;
            result[i].name = StageInfo.getStageName(i + 1);
            result[i].value = (totalEffort == 0) ? Double.NaN : values[i] * 100 / totalEffort;
        }
        return result;
    }

   //include norm, plan_effort and re_plan_effort
    /**
     * 
     * @return vector of ProcessInfo
     */
    public static final Vector getProcessEffortList(ProjectInfo projectInfo, Vector processEffortByStages) {
        return getProcessEffortList(projectInfo, processEffortByStages, -1, true);
    }
    /**
     * 
     * @return vector of ProcessInfo
     */
    public static final Vector getProcessEffortList(ProjectInfo projectInfo, Vector processEffortByStages, long stageID, boolean cumulated) {
        Vector vt = new Vector();
        ProcessEffortInfo info = null;		
        double [][] actEffort = getActualEffortProcess(projectInfo.getProjectId(), true);
        ProcessEffortByStageInfo processEffortByStage;
		Vector processEffortByFirstStages=new Vector();	
		ProcessEffortByStageInfo processEffortByFirstStage;						
        //is current stage already replanned
        boolean hasBeenReplanned=true;
		boolean existReplanedValue=false;		
        int currentStage=0;
        
        for (int j = 0; j < processEffortByStages.size(); j++) {            
            processEffortByStage = (ProcessEffortByStageInfo)processEffortByStages.elementAt(j);						
            currentStage=j;				
            if (processEffortByStage.isOpen){
                hasBeenReplanned=checkPlanEffortProcess(processEffortByStage.stageid);                                        					
                break;
            }       
        }
        					
        //Get first milestone - BinhNT
		processEffortByFirstStages = (Vector) getProcessEffortByStage(projectInfo,Schedule.getStageList(projectInfo.getProjectId()),0);   
        
		double totalPlannedEffort = 0;
		double averageAdjustment = 0;

		double totalNormEffort = 0;
		double averageAdjustment2 = 0;

        for (int i = 0; i < ProcessInfo.trackedProcessId.length; i++) {        	
            info = new ProcessEffortInfo();			
            int x = CommonTools.arrayScan(actEffort[0], ProcessInfo.trackedProcessId[i]);
            if (x>=0)
            	info.actual =actEffort[1][x];
            info.processID = ProcessInfo.trackedProcessId[i];
            info.process = ProcessInfo.getProcessName(info.processID);          

			//Plan - Update by BinhNT (Get Plan at first milestone) 						
			for (int j = 0; j < processEffortByFirstStages.size(); j++) {
				processEffortByFirstStage = (ProcessEffortByStageInfo)processEffortByFirstStages.elementAt(j);
				if (!cumulated && stageID > 0 && processEffortByFirstStage.stageid != stageID)
				continue;
									
				if (!Double.isNaN(processEffortByFirstStage.planned[i])) {
			  	if (Double.isNaN(info.estimated))
					info.estimated = 0;                                    
				  	info.estimated += processEffortByFirstStage.planned[i];			  	
				}		
			}
			
			          
			for (int j = 0; j < processEffortByStages.size(); j++) {							                
                processEffortByStage = (ProcessEffortByStageInfo)processEffortByStages.elementAt(j);                				
			              
                if (!cumulated && stageID > 0 && processEffortByStage.stageid != stageID)
                    continue;
			
			/*Comment by BinhNT	
				if (!Double.isNaN(processEffortByStage.planned[i])) {
					if (Double.isNaN(info.estimated))
						info.estimated = 0;                                    
						info.estimated += processEffortByStage.planned[i];			  	
					}	
             */   				
                //Check replan value in case there is no any replan action.    
				if (j>0 && !existReplanedValue)
					if (!Double.isNaN(processEffortByStage.rePlanned[i]))
						existReplanedValue=true;
					
				//Replan				             
                if (currentStage>1 || (currentStage==1 &&  hasBeenReplanned)){                	
                	               	
                    if (Double.isNaN(info.reEstimated))
                        info.reEstimated = 0;

                    if(processEffortByStage.isOpen){
						info.reEstimated += processEffortByStage.rePlannedLatest[i];
                    }
                    else{
                        //if no replan, we take the prev replan
                        if (!hasBeenReplanned && j==currentStage-1){
							info.reEstimated += processEffortByStage.rePlannedLatest[i];
                        }else
							info.reEstimated += processEffortByStage.actual[i];
					}
                }else{
					info.reEstimated=Double.NaN;
                }

                if (!Double.isNaN(processEffortByStage.norm[i])) {
                    if (Double.isNaN(info.norm))
                        info.norm = 0;
                    info.norm += processEffortByStage.norm[i];
                }
                if (stageID > 0 && processEffortByStage.stageid == stageID)
                    break;
            }

           	// info.deviation = CommonTools.metricDeviation(info.estimated, info.reEstimated, info.actual);
            vt.add(info);

			totalPlannedEffort = totalPlannedEffort + info.estimated;
			totalNormEffort = totalNormEffort + info.norm;
        }

		if (ProcessInfo.trackedProcessId.length > 0){
			averageAdjustment = (projectInfo.getBaseEffort() - totalPlannedEffort) / ProcessInfo.trackedProcessId.length;
			averageAdjustment2 = (projectInfo.getBaseEffort() - totalNormEffort) / ProcessInfo.trackedProcessId.length;
		}

		for (int i=0; i < ProcessInfo.trackedProcessId.length; i++) {
			info=(ProcessEffortInfo)vt.get(i);
			if (!existReplanedValue)
				info.reEstimated =Double.NaN;			

			info.estimated = CommonTools.parseDouble(CommonTools.formatNumber(info.estimated + averageAdjustment,true));
			info.deviation = CommonTools.metricDeviation(info.estimated, info.reEstimated, info.actual);
			info.norm = info.norm + averageAdjustment2;
		}
        return vt;
    }
    /**
     * 
     * @return vector of ProcessEffortInfo
     */
    public static final Vector getPlanProcessEffortAndRCR(ProjectInfo projectInfo) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        Vector vt = new Vector();
        ResultSet rs = null;
        ProcessEffortInfo info = null;
        try {
            Date date = projectInfo.getStartDate() == null ? projectInfo.getPlanStartDate() : projectInfo.getStartDate();
            Vector norms = Norms.getEffortDstrByProc(Parameters.FSOFT_WU, projectInfo.getLifecycleId(), date);
            conn = ServerHelper.instance().getConnection();
            sql = "SELECT PE.* FROM PROCESS_EFFORT PE WHERE PE.PROJECT_ID = ?";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, projectInfo.getProjectId());
            rs = stm.executeQuery();
            while (rs.next()) {
                info = new ProcessEffortInfo();
                info.prjID = projectInfo.getProjectId();
                info.processID = rs.getInt("PROCESS_ID");
                info.proEffID = rs.getLong("PROCESS_EFFORT_ID");
                
                 info.estimated = Db.getDouble(rs, "PLAN_EFFORT");
//              these values should come from PLANS_PROCESS_STAGE 
                /*info.reEstimated = Db.getDouble(rs, "RE_PLAN_EFFORT");*/
                info.estimatedRCR = Db.getDouble(rs, "PLAN_RCR");
                //Norm
                for (int i = 0; i < norms.size(); i++) {
                    NormRefInfo nref = (NormRefInfo)norms.elementAt(i);
                    if (nref.prcID == info.processID) {
                        info.process = nref.prcName;
                        info.normPercent = nref.value;
                        info.norm = projectInfo.getPlannedEffort() * nref.value / 100d;
                        break;
                    }
                }
                vt.add(info);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return vt;
        }
    }
    /**
     * returns vector of PCBEffortDistribInfo
     */
    public static final Vector getPCBProcessEffort(final long[] prjIDs, java.sql.Date endDate) {
        Vector distrib = new Vector();
        try {
            Vector processes = ProcessInfo.projectProcessList;
            PCBEffortDistribInfo effInf;
            ProcessInfo info;
            double[][] effortSpent = getActualEffortProcessByDate(prjIDs, endDate);
            for (int i = 0; i < processes.size(); i++) {
                info = (ProcessInfo)processes.elementAt(i);
                effInf = new PCBEffortDistribInfo();
                effInf.id = info.processId;
                effInf.name = info.name;
                for (int j = 0; j < effortSpent[0].length; j++) {
                    if (effInf.id == effortSpent[0][j]) {
                        effInf.value = effortSpent[1][j];
                        break;
                    }
                }
                distrib.add(effInf);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return distrib;
        }
    }
    /**
    * in % or absolute total effort
    * returns an array [2][n process]
    * [0][x]contain process IDs
    * [1][x]contain values
    */
    public static final double[][] getActualEffortProcess(long  prjID, boolean absolute) {
           return getActualEffortProcessByDate(new long[] { prjID }, null, null, absolute);
    }
    /**
    * in % or absolute total effort
    * returns an array [2][n process]
    * [0][x]contain process IDs
    * [1][x]contain values
    */
    public static final double[][] getActualEffortProcessByDate(final long[] prjIDs, Date endDate) {
        return getActualEffortProcessByDate(prjIDs, null, endDate, false);
    }
    /**
    * in % or absolute total effort
    * returns an array [2][n process]
    * [0][x]contain process IDs
    * [1][x]contain values
    */
    public static final double[][] getActualEffortProcessByDate(long prjID, Date startDate, Date endDate, boolean absolute) {
        return getActualEffortProcessByDate(new long[] { prjID }, startDate, endDate, absolute);
    }
    /**
    * in % or absolute total effort
    * returns an array [2][n process]
    * [0][x]contain process IDs
    * [1][x]contain values
    */
    public static final double[][] getActualEffortProcessByDate(final long[] prjIDs, Date startDate, Date endDate, boolean absolute) {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        ResultSet rs = null;
		Vector processes = ProcessInfo.projectProcessList;
		ProcessInfo info;
		double[][] retval = new double[2][processes.size()];
		try {
            String strByDate = "";
            String projectConstraint = ConvertString.arrayToString(prjIDs, ", ");
            if (endDate != null)
                strByDate = " AND OCCUR_DATE <= '" + CommonTools.dateFormat(endDate) + "' ";
            if (startDate != null)
                strByDate += " AND OCCUR_DATE >= '" + CommonTools.dateFormat(startDate) + "' ";
            Arrays.fill(retval[0], Double.NaN);
            Arrays.fill(retval[1], 0);
            for (int i = 0; i < processes.size(); i++) {
                info = (ProcessInfo)processes.elementAt(i);
                retval[0][i] = info.processId;
            }
            double effortSpent = getGroupActualEffort(prjIDs, startDate, endDate);
            if (effortSpent == 0 || Double.isNaN(effortSpent))
                return retval;
            //project management exists in timesheet but not project monitoring & planning, that's why the process must be split
            sql =
                "SELECT  procID, SUM(DURATION)/(8) ACTUAL_EFFORT FROM ("
                    + "select DECODE (PROCESS_ID,"
                    + ProcessInfo.PROJECT_MANAGEMENT
                    + ",DECODE(KPA_ID,2,"
                    + ProcessInfo.PROJECT_PLANNING
                    + ","
                    + ProcessInfo.PROJECT_MONITORING
                    + "),PROCESS_ID) PROCID,DURATION"
                    + " FROM TIMESHEET WHERE PROJECT_ID IN("
                    + projectConstraint
                    + ") AND STATUS=4"
                    + strByDate
                    + " AND PROCESS_ID in( "
                    + ConvertString.arrayToString(retval[0], ", ")
                    + ","
                    + ProcessInfo.PROJECT_MANAGEMENT
                    + "))"
                    + " group by procID";

            int procid;
            double totalEff = 0;
            int i = 0;

            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);
//TODO seen java crashing in this loop, have no clue why
//LINH: The author of this function return to the callers double[][] which is put in the stack java, if
//the double[][] is big and callers is within a loop by too many rounds then it will cause stack overflow

//TRUNGTN: this loop is only connect and SELECT from database, not crash program if it is called one time but
// it is called from loop of function PCB.getPCBMetricGroups, the loop calls are cause of program crash
            while (rs.next()) {

                procid = rs.getInt("procID");

                i = CommonTools.arrayScan(retval[0], procid);
                
                double dbActualEffort;
				dbActualEffort = rs.getDouble("ACTUAL_EFFORT");

                if (i >= 0) {
                    retval[1][i] = dbActualEffort;
                    
                    if (!absolute)
						retval[1][i] = retval[1][i] * 100d / effortSpent;

                    totalEff += retval[1][i];
                }
            }
            i = CommonTools.arrayScan(retval[0], ProcessInfo.OTHER);
            if (i >= 0) {
                retval[1][i] += ((absolute) ? effortSpent : 100d) - totalEff;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("SQL= " + sql);
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return retval;
        }
    }
    /*
     * Make sure to also update getGroupEffortProcessByDate
     *
     */
    public static final float getActualEffortProcessByDate(final long prjID, final long processID, Date startDate, Date endDate) {
        double[][] result = getActualEffortProcessByDate(prjID, startDate, endDate, true);
        for (int i = 0; i < result[0].length; i++) {
            if (result[0][i] == processID) {
                return Double.isNaN(result[1][i]) ? 0 : (float)result[1][i];
            }
        }
        return 0;
    }

	public static final float getActualEffortByTOW(final long prjID, final long towID, Date startDate, Date endDate) {
			Connection conn = null;
			Statement stm = null;
			String sql = null;
			ResultSet rs = null;
			String strByDate = "";
			if (startDate != null)
				strByDate = " AND OCCUR_DATE >= '" + CommonTools.dateFormat(startDate) + "' " ;
			if (endDate != null) 
				strByDate+= " AND OCCUR_DATE <= '"+ CommonTools.dateFormat(endDate)+ "' ";
			
			String timesheetTable = "";
			ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			if(projectInfo.getArchiveStatus() == 4){
				timesheetTable = "TIMESHEET_ARCHIVE";            
			}else{
				timesheetTable = "TIMESHEET";
			}                    
			try {
				conn = ServerHelper.instance().getConnection();
				stm = conn.createStatement();
				sql =
						"SELECT SUM(DURATION)/(8) ACTUAL_EFFORT FROM " + timesheetTable + " "
							+ " WHERE PROJECT_ID = "
							+ prjID
							+ " AND TOW_ID = "
							+ towID
							+ " AND STATUS = 4 "
							+ strByDate ;
					rs = stm.executeQuery(sql);
					return (rs.next()) ? rs.getFloat("ACTUAL_EFFORT") : 0;
			}catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			finally {
				ServerHelper.closeConnection(conn, stm, rs);
			}
		} 
/*		
	public static final float getActualEffortProcessByDateForNorms(final long prjID, final long towID, Date startDate, Date endDate) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		String strByDate = "";
		if (startDate != null)
			strByDate = " AND OCCUR_DATE >= '" + CommonTools.dateFormat(startDate) + "' " ;
		if (endDate != null) 
			strByDate+= " AND OCCUR_DATE <= '"+ CommonTools.dateFormat(endDate)+ "' ";
        //Huynh2 add for projectd archive    
        String timesheetTable = "";
		ProjectInfo projectInfo = Project.getProjectInfo(prjID);
        if(projectInfo.getArchiveStatus() == 4){
            timesheetTable = "TIMESHEET_ARCHIVE";            
        }else{
            timesheetTable = "TIMESHEET";
        }                    
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			if ((towID == ProcessInfo.PROJECT_PLANNING)
				|| (towID == ProcessInfo.PROJECT_MONITORING)) //Monitoring project and planing project
				{
				sql =
					"SELECT  SUM(DURATION)/(8) ACTUAL_EFFORT FROM " + timesheetTable + " "
						+ " WHERE PROJECT_ID = "
						+ prjID
						+ " AND STATUS=4"
						+ strByDate 
						+ " AND PROCESS_ID = "
						+ ProcessInfo.PROJECT_MANAGEMENT
						+ " AND KPA_ID "
						+ ((towID == ProcessInfo.PROJECT_PLANNING) ? "=2" : "!=2");
				rs = stm.executeQuery(sql);
				return (rs.next()) ? rs.getFloat("ACTUAL_EFFORT") : 0;
			}
			else if (towID != ProcessInfo.OTHER) {
				sql =
					"SELECT SUM(DURATION)/(8) ACTUAL_EFFORT FROM " + timesheetTable + " "
						+ " WHERE PROJECT_ID = "
						+ prjID
						+ " AND TOW_ID = "
						+ towID
						+ " AND STATUS=4 "
						+ strByDate ;
				rs = stm.executeQuery(sql);
				return (rs.next()) ? rs.getFloat("ACTUAL_EFFORT") : 0;
			}
			else //Calculate for other effort
				{
				sql =
					"SELECT SUM(DURATION)/8 ACTUAL_EFFORT FROM " + timesheetTable + " "
						+ " WHERE PROJECT_ID = "
						+ prjID
						+ " AND (PROCESS_ID NOT IN ("
						+ ConvertString.arrayToString(ProcessInfo.trackedProcessId, ",")
						+ ","
						+ ProcessInfo.PROJECT_MANAGEMENT
						+ ") OR PROCESS_ID = "
						+ ProcessInfo.OTHER
						+ ") AND STATUS=4 "
						+ strByDate ;
				rs = stm.executeQuery(sql);
				return (rs.next()) ? rs.getFloat("ACTUAL_EFFORT") : 0;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
*/

    public static final float getActualEffortTypeOfWorkByDateForNorms(final long prjID, final long typeOfWork, Date startDate, Date endDate) {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        ResultSet rs = null;
        String strByDate = "";
        if (startDate != null)
            strByDate = " AND OCCUR_DATE >= '" + CommonTools.dateFormat(startDate) + "' " ;
        if (endDate != null) 
            strByDate+= " AND OCCUR_DATE <= '"+ CommonTools.dateFormat(endDate)+ "' ";
        //Huynh2 add for projectd archive    
        String timesheetTable = "";
        ProjectInfo projectInfo = Project.getProjectInfo(prjID);
        if(projectInfo.getArchiveStatus() == 4){
            timesheetTable = "TIMESHEET_ARCHIVE";            
        }else{
            timesheetTable = "TIMESHEET";
        }                    
        try {
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            sql =
                "SELECT SUM(DURATION)/(8) ACTUAL_EFFORT FROM " + timesheetTable + " "
                    + " WHERE PROJECT_ID = "
                    + prjID
                    + " AND PROCESS_ID = "
                    + typeOfWork
                    + " AND STATUS=4 "
                    + strByDate ;
            rs = stm.executeQuery(sql);
            return (rs.next()) ? rs.getFloat("ACTUAL_EFFORT") : 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
        }
    }

    /**
     * 
     * Input Vector of RCRByProcessInfo
     */
    public static final void updateProcessRCRs(Vector rCRByProcessInfos) {
        Connection conn = null;
        PreparedStatement prepStmt = null;
        PreparedStatement prepStmtIns = null;
        try {
            conn = ServerHelper.instance().getConnection();
            String updateStatement = " UPDATE PROCESS_EFFORT SET PLAN_RCR = ? WHERE PROJECT_ID = ? AND PROCESS_ID=?";
            String insStatement = " insert into PROCESS_EFFORT ( process_effort_id,PLAN_RCR, PROJECT_ID, PROCESS_ID)"
            +" values (NVL(?,1),?,?,?)";
            prepStmt = conn.prepareStatement(updateStatement);
            prepStmtIns = conn.prepareStatement(insStatement);
            RCRByProcessInfo rCRByProcessInfo;
            for (int i = 0; i < rCRByProcessInfos.size(); i++) {
                rCRByProcessInfo = (RCRByProcessInfo)rCRByProcessInfos.elementAt(i);
                Db.setDouble(prepStmt, 1, rCRByProcessInfo.plan);
                prepStmt.setLong(2, rCRByProcessInfo.projectID);
                prepStmt.setInt(3, rCRByProcessInfo.processID);
                if (prepStmt.executeUpdate()<1){
                    prepStmtIns.setLong(1,ServerHelper.getNextSeq("PROCESS_EFFORT_SEQ"));                    
                    Db.setDouble(prepStmtIns, 2, rCRByProcessInfo.plan);
                    prepStmtIns.setLong(3, rCRByProcessInfo.projectID);
                    prepStmtIns.setInt(4, rCRByProcessInfo.processID);
                    prepStmtIns.executeUpdate();
                }
            }
            ServerHelper.closeConnection(null, prepStmtIns, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
        }
    }
    /**
     * @author:Hoang Thi Nga (R.I.P.)
     * @return Vector containing ReviewEffortInfo
     * @param prjID
     */
    public static final Vector getReviewEffortList(final long prjID) {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        Vector vt = null;
        ResultSet rs = null;
        try {
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            // Get Norm of review Effectiveness
            NormInfo reviewEffNorm = Norms.getNorm(prjID, MetricDescInfo.REVIEW_EFFECTIVENESS);
            double revEffectiveness = reviewEffNorm.average;
            double revEfficiency = Norms.getNorm(prjID, MetricDescInfo.REVIEW_EFFICIENCY).average;
            //get planned values
            vt = new Vector();
            sql =
                "SELECT R.*,M.WP_ID, M.NAME,M.PLANNED_DEFECT, M.REPLANNED_DEFECT, M.MODULE_ID MODID "
                    + " FROM REVIEW_EFFORT R, MODULE M "
                    + " WHERE R.MODULE_ID (+) = M.MODULE_ID"
                    + " AND M.PROJECT_ID = "
                    + prjID
                    + " AND M.PLANNED_REVIEW_DATE IS NOT NULL"
                    + " ORDER BY M.PLANNED_REVIEW_DATE";
            rs = stm.executeQuery(sql);
            double pDefect = Double.NaN;
            double repDefect = Double.NaN;
            while (rs.next()) {
                final ReviewEffortInfo info = new ReviewEffortInfo();
                info.prjID = prjID;
                info.reviewE_ID = rs.getLong("REVIEW_EFFORT_ID");
                info.estimated = Db.getDouble(rs, "PLAN_EFFORT");
                info.reEstimated = Db.getDouble(rs, "RE_PLAN_EFFORT");
                info.moduleName = rs.getString("NAME");
                info.moduleID = rs.getLong("MODID");
                //TODO : Should get actual effort from Timesheet when timesheet supports module/product
                info.actual = Db.getDouble(rs, "ACTUAL_EFFORT");
                //calculate deviation
                info.deviation = CommonTools.metricDeviation(info.estimated, info.reEstimated, info.actual);
                //get Norm
                pDefect = Db.getDouble(rs, "PLANNED_DEFECT");
                repDefect = Db.getDouble(rs, "REPLANNED_DEFECT");
                if (revEffectiveness > 0) {
                    if (repDefect >= 0)
                        info.norm = (repDefect * revEfficiency) / (100 * revEffectiveness);
                    else if (pDefect >= 0)
                        info.norm = (pDefect * revEfficiency) / (100 * revEffectiveness);
                }
                vt.add(info);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return vt;
        }
    }
    /**
     * @author Hoang Thi Nga(R.I.P.)
     * @project: Fms-1
     * @param:ProcessEffortInfo
     * @return boolean
     */
    public static final boolean updateReviewEffort(final ReviewEffortInfo info) {
        String updateStatement = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        boolean bl = false;
        try {
            conn = ServerHelper.instance().getConnection();
            if (info.reviewE_ID != 0) { //update
                updateStatement =
                    " UPDATE REVIEW_EFFORT "
                        + " SET PLAN_EFFORT = ?,"
                        + " RE_PLAN_EFFORT = ?,"
                        + " ACTUAL_EFFORT = ?"
                        + " WHERE REVIEW_EFFORT_ID = ?";
                prepStmt = conn.prepareStatement(updateStatement);
                Db.setDouble(prepStmt, 1, info.estimated);
                Db.setDouble(prepStmt, 2, info.reEstimated);
                Db.setDouble(prepStmt, 3, info.actual);
                prepStmt.setLong(4, info.reviewE_ID);
                final int rowCount = prepStmt.executeUpdate();
                if (rowCount == 0)
                    System.err.println("process effort update failed");
                else
                    bl = true;
            }
            else { //add
                updateStatement =
                    " INSERT INTO REVIEW_EFFORT (REVIEW_EFFORT_ID,PLAN_EFFORT,RE_PLAN_EFFORT,ACTUAL_EFFORT,MODULE_ID,PROJECT_ID)"
                        + " VALUES ((SELECT NVL(MAX(REVIEW_EFFORT_ID)+1,1) FROM REVIEW_EFFORT ),?,?,?,?,?) ";
                prepStmt = conn.prepareStatement(updateStatement);
                Db.setDouble(prepStmt, 1, info.estimated);
                Db.setDouble(prepStmt, 2, info.reEstimated);
                Db.setDouble(prepStmt, 3, info.actual);
                prepStmt.setLong(4, info.moduleID);
                prepStmt.setLong(5, info.prjID);
                final int rowCount = prepStmt.executeUpdate();
                if (rowCount == 0)
                    System.err.println("process effort insert failed");
                else
                    bl = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("sql stmt = " + updateStatement);
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
            return bl;
        }
    }
    /******************************************Quality Activity Effort**********************************/
    public static final void addQltActEffort(final QltActivityEffortInfo info) {
        String insertStatement = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        try {
            conn = ServerHelper.instance().getConnection();
            insertStatement =
                " INSERT INTO QUALITY_ACTIVITY_EFFORT ("
                    + " QUALITY_ACTIVITY_EFFORT_ID,"
                    + " PROJECT_ID,"
                    + " ACTIVITY_ID,"
                    + " TYPE)"
                    + " VALUES (NVL((SELECT MAX(QUALITY_ACTIVITY_EFFORT_ID)+1 FROM QUALITY_ACTIVITY_EFFORT),1),?,?,?)";
            prepStmt = conn.prepareStatement(insertStatement);
            prepStmt.setLong(1, info.prjID);
            prepStmt.setLong(2, info.activityID);
            prepStmt.setInt(3, info.type);
            prepStmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL stm = " + insertStatement);
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
        }
    }
    ////////////////////
    private static Vector addAllQltActEffort(final long prjID) {
        Vector vt = new Vector();
        //Process control
        final QltActivityEffortInfo info = new QltActivityEffortInfo();
        info.prjID = prjID;
        info.type = QltActivityEffortInfo.TYPE_PROCESS_CONTROL;
        addQltActEffort(info);
        //Process Audit
        info.type = QltActivityEffortInfo.TYPE_PROCESS_AUDIT;
        addQltActEffort(info);
        // stage
        info.type = QltActivityEffortInfo.TYPE_MILESTONE;
        final Vector stageList = Schedule.getStageList(prjID);
        for (int i = 0; i < stageList.size(); i++) {
            final StageInfo stageTemp = (StageInfo)stageList.elementAt(i);
            info.activityID = stageTemp.milestoneID;
            addQltActEffort(info);
        }
        return vt;
    }
    /**
     * @author:Hoang Thi Nga
     * @return Vector of QltActivityEffortInfo
     */
    public static final Vector getQltActivityEffortList(ProjectInfo projectInfo) {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        Vector vt = new Vector();
        ResultSet rs = null;
        final long project_id = projectInfo.getProjectId();
        try {
            QltActivityEffortInfo info;
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            sql =
                "SELECT QE.*,OA.ACTIVITY "
                    + " FROM QUALITY_ACTIVITY_EFFORT QE, OTHER_ACTIVITY OA "
                    + " WHERE QE.ACTIVITY_ID = OA.OTHER_ACTIVITY_ID"
                    + " AND QE.TYPE = 1"
                    + " AND QE.PROJECT_ID = "
                    + project_id;
            rs = stm.executeQuery(sql);
            int count = -1;
            float effort = -1;
            while (rs.next()) {
                info = new QltActivityEffortInfo();
                info.prjID = rs.getLong("PROJECT_ID");
                info.qltAct_ID = rs.getLong("QUALITY_ACTIVITY_EFFORT_ID");
                info.estimated = rs.getFloat("PLAN_EFFORT");
                info.reEstimated = rs.getFloat("RE_PLAN_EFFORT");
                info.actual = rs.getFloat("ACTUAL_EFFORT");
                info.activity = rs.getString("ACTIVITY");
                info.activityID = rs.getInt("ACTIVITY_ID");
                if (info.activity.length() > 50) {
                    info.activity = info.activity.substring(0, 50);
                }
                //get deviation
                effort = (info.reEstimated == -1) ? info.estimated : info.reEstimated;
                if ((effort > 0) && (info.actual != -1))
                    info.deviation = CommonTools.formatDouble((info.actual - effort) * 100f / effort);
                //get Norm
                info.norm = "N/A";
                info.type = QltActivityEffortInfo.TYPE_OTHER_QUALITY;
                vt.add(info);
                count++;
            }
            rs.close();
            String NormQActivityEffort;
            NormQActivityEffort = Parameters.NormQActivityEffort;
            sql =
                "SELECT QE.QUALITY_ACTIVITY_EFFORT_ID,QE.PLAN_EFFORT,QE.RE_PLAN_EFFORT,QE.ACTUAL_EFFORT,M.NAME,M.MILESTONE_ID"
                    + " FROM QUALITY_ACTIVITY_EFFORT QE,MILESTONE M "
                    + " WHERE QE.ACTIVITY_ID (+)= M.MILESTONE_ID"
                    + " AND M.PROJECT_ID = "
                    + project_id
                    + " AND QE.TYPE (+)= "
                    + QltActivityEffortInfo.TYPE_MILESTONE
                    + " AND QE.PROJECT_ID (+)= "
                    + project_id
                    + " ORDER BY NVL(M.PLAN_FINISH_DATE, BASE_FINISH_DATE)";
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                info = new QltActivityEffortInfo();
                info.prjID = project_id;
                info.qltAct_ID = rs.getLong("QUALITY_ACTIVITY_EFFORT_ID");
                info.estimated = rs.getFloat(2);
                info.reEstimated = rs.getFloat(3);
                info.actual = rs.getFloat(4);
                info.activity = rs.getString(5) + " gate inspection";
                info.activityID = rs.getLong(6);
                //get deviation
                effort = (info.reEstimated == -1) ? info.estimated : info.reEstimated;
                if ((effort > 0) && (info.actual != -1))
                    info.deviation = CommonTools.formatDouble((info.actual - effort) * 100f / effort);
                //get Norm
                info.norm = NormQActivityEffort;
                info.type = QltActivityEffortInfo.TYPE_MILESTONE;
                vt.add(info);
                count++;
            }
            rs.close();
            String NormProcessControlEffort;
            NormProcessControlEffort = Parameters.NormProcessControlEffort;
            //process control
            sql =
                "SELECT * FROM QUALITY_ACTIVITY_EFFORT WHERE TYPE = "
                    + QltActivityEffortInfo.TYPE_PROCESS_CONTROL
                    + " AND PROJECT_ID = "
                    + project_id;
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                info = new QltActivityEffortInfo();
                info.prjID = rs.getLong("PROJECT_ID");
                info.qltAct_ID = rs.getLong("QUALITY_ACTIVITY_EFFORT_ID");
                info.estimated = rs.getFloat("PLAN_EFFORT");
                info.reEstimated = rs.getFloat("RE_PLAN_EFFORT");
                info.actual = rs.getFloat("ACTUAL_EFFORT");
                info.activity = "Process control";
                //get deviation
                effort = (info.reEstimated == -1) ? info.estimated : info.reEstimated;
                if ((effort > 0) && (info.actual != -1))
                    info.deviation = CommonTools.formatDouble((info.actual - effort) * 100f / effort);
                //get Norm
                int weekCount = 0;
                Date endD = projectInfo.getActualFinishDate() == null ? projectInfo.getPlannedFinishDate() : projectInfo.getActualFinishDate();
                if (projectInfo.getStartDate() != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(projectInfo.getStartDate());
                    cal.add(Calendar.DAY_OF_YEAR, 7);
                    while (cal.getTime().compareTo(endD) <= 0) {
                        weekCount++;
                        cal.add(Calendar.DAY_OF_YEAR, 7);
                    }
                }
                if (weekCount != 0) {
                    info.norm = CommonTools.formatDouble(CommonTools.parseDouble(NormProcessControlEffort) / weekCount);
                }
                count++;
                info.type = QltActivityEffortInfo.TYPE_PROCESS_CONTROL;
                vt.add(info);
            }
            rs.close();
            String NormProcessAuditEffort;
            NormProcessAuditEffort = Parameters.NormProcessAuditEffort;
            //process audit
            sql =
                "SELECT * FROM QUALITY_ACTIVITY_EFFORT WHERE TYPE = "
                    + QltActivityEffortInfo.TYPE_PROCESS_AUDIT
                    + " AND PROJECT_ID = "
                    + project_id;
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                info = new QltActivityEffortInfo();
                info.prjID = rs.getLong("PROJECT_ID");
                info.qltAct_ID = rs.getLong("QUALITY_ACTIVITY_EFFORT_ID");
                info.estimated = rs.getFloat("PLAN_EFFORT");
                info.reEstimated = rs.getFloat("RE_PLAN_EFFORT");
                info.activity = "Process audit";
                //get actual effort from timesheet
                info.actual = getActualEffortProcessByDate(project_id, ProcessInfo.INTERNAL_AUDIT, null, null);
                //get deviation
                effort = (info.reEstimated == -1) ? info.estimated : info.reEstimated;
                if ((effort > 0) && (info.actual != -1))
                    info.deviation = CommonTools.formatDouble((info.actual - effort) * 100f / effort);
                //get Norm
                info.norm = NormProcessAuditEffort;
                info.type = QltActivityEffortInfo.TYPE_PROCESS_AUDIT;
                vt.add(info);
                count++;
            }
            if (count == -1) {
                vt = addAllQltActEffort(project_id);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("sql stmt = " + sql);
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return vt;
        }
    }
    /**
     * @author Hoang Thi Nga(R.I.P.)
     * @project: Fms-1
     * @param:QltActivityEffortInfo
     * @return boolean
     */
    public static final boolean updateQltActivityEffort(final QltActivityEffortInfo info) {
        String updateStatement = "";
        Connection conn = null;
        PreparedStatement prepStmt = null;
        boolean bl = true;
        try {
            conn = ServerHelper.instance().getConnection();
            if (info.qltAct_ID <= 0) { //especially used for new milestone gate inspection
                updateStatement =
                    " INSERT INTO QUALITY_ACTIVITY_EFFORT "
                        + " VALUES(  ?, ?,?,?,(select max(QUALITY_ACTIVITY_EFFORT_ID)+1 from QUALITY_ACTIVITY_EFFORT),?,?)";
                prepStmt = conn.prepareStatement(updateStatement);
                prepStmt.setLong(1, info.prjID);
                prepStmt.setFloat(2, info.estimated);
                prepStmt.setFloat(3, info.reEstimated);
                prepStmt.setFloat(4, info.actual);
                prepStmt.setLong(5, info.activityID);
                prepStmt.setInt(6, info.type);
            }
            else {
                updateStatement =
                    " UPDATE QUALITY_ACTIVITY_EFFORT "
                        + " SET PLAN_EFFORT = ?,"
                        + " RE_PLAN_EFFORT = ?,"
                        + " ACTUAL_EFFORT = ?"
                        + " WHERE QUALITY_ACTIVITY_EFFORT_ID = ?";
                prepStmt = conn.prepareStatement(updateStatement);
                prepStmt.setFloat(1, info.estimated);
                prepStmt.setFloat(2, info.reEstimated);
                prepStmt.setFloat(3, info.actual);
                prepStmt.setLong(4, info.qltAct_ID);
            }
            final int rowCount = prepStmt.executeUpdate();
            if (rowCount == 0) {
                bl = false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("sql stmt = " + updateStatement);
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, null);
            return bl;
        }
    }
    /**
     * Effort distribution by type
     */
    
    // Huynh2 modify for project archive
    public static final Vector getEffortDistributionByType(final long prjID) {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        Vector vt = null;
        ResultSet rs = null;
        try {
            final DecimalFormat decFm = new DecimalFormat("###.##");
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            float total = 0;
            float effortPD = 0;
            vt = new Vector();
            if(!Db.checkProjecIsArchive(prjID)){
                sql =
                    "SELECT  TYPEOFWORK.NAME,SUM(DURATION)/(8) ACTUAL_EFFORT  "
                        + "  FROM TIMESHEET,TYPEOFWORK "
                        + " WHERE PROJECT_ID = "
                        + prjID
                        + " AND STATUS=4 "
                        + " AND TIMESHEET.TOW_ID=TYPEOFWORK.TOW_ID GROUP BY TYPEOFWORK.TOW_ID,TYPEOFWORK.NAME";
            } 
            else {
                sql =
                    "SELECT  TYPEOFWORK.NAME, ACTUAL_EFFORT  "
                        + "  FROM TYPEOFWORK,Archive_Actual_Effort_By_Type arch "
                        + " WHERE PROJECT_ID = "
                        + prjID                    
                        + " AND arch.typeofwork_id=TYPEOFWORK.TOW_ID ";
            }        
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                final EffortDistributionByTypeInfo info = new EffortDistributionByTypeInfo();
                info.type = rs.getString("NAME");
                effortPD = rs.getFloat("ACTUAL_EFFORT");
                info.effortPD = decFm.format(effortPD);
                total += effortPD;
                vt.add(info);
            }
            final EffortDistributionByTypeInfo info = new EffortDistributionByTypeInfo();
            info.type = "Total";
            if (total != -1) {
                info.effortPD = decFm.format(total);
            }
            vt.add(info);
            
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Effort class, line 1620 sql = "+ sql);
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return vt;
        }
    }

    public static final boolean cacheEffortType(final long prjID) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        Vector vt = null;        
        int numRecord = 0;
        try {
            conn = ServerHelper.instance().getConnection();
            //delete
            sql = "DELETE Archive_Actual_Effort_By_Type where project_id = ?";
            stm = conn.prepareStatement(sql);
            stm.setLong(1,prjID);
            stm.execute() ;
            stm.close();           
            sql = 
                 "INSERT INTO Archive_Actual_Effort_By_Type "
                +"SELECT   ? project_id, typeofwork.tow_id,   SUM (DURATION) "
                +                                        " / (8) actual_effort "
                +   "FROM timesheet, typeofwork "
                +   "WHERE project_id = ? "
                +   "AND status = 4 "
                +   "AND timesheet.tow_id = typeofwork.tow_id "
                +"GROUP BY typeofwork.tow_id, typeofwork.NAME ";
            stm = conn.prepareStatement(sql);
            stm.setLong(1,prjID);
            stm.setLong(2,prjID);                                 
            numRecord = stm.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Effort class, cacheEffort_type function  line 1651 sql = "+ sql);
        }
        finally {
            ServerHelper.closeConnection(conn, stm, null);
            if(numRecord > 0){
                return true;
            }else{
                return false;
            }            
        }
    }
    
    // remove Effort_type from acutal_effort_by_type_archive
    public static final boolean removeEffortType(final long prjID) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        Vector vt = null;        
        int numRecord = 0;
        try {
            conn = ServerHelper.instance().getConnection();            
            sql = "DELETE FROM Archive_Actual_Effort_By_Type WHERE project_id = ?";
            stm = conn.prepareStatement(sql);
            stm.setLong(1,prjID);
            numRecord = stm.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, null);
            if(numRecord > 0){
                return true;
            }else{
                return false;
            }
        }
    }

    
    //end 
    public static final double getCalendarEffort(String wuName) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        double calEff1 = 0;
        double calEff2 = 0;
        try {
            //sql = "SELECT COUNT(*) " + "FROM DEVELOPER WHERE UPPER(GROUP_NAME) = '" + wuName.toUpperCase() + "'";
            // Avoid to use function in column (ie. Where Upper(...)=... )
            sql = "SELECT COUNT(*) " + "FROM DEVELOPER WHERE GROUP_NAME = '" + wuName + "'";
            sql = sql + " AND STATUS = 1";
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                calEff1 = rs.getDouble(1);
                calEff1 = calEff1 - 1; //Do not count GLs, they never check in timesheet
            }
            rs.close();
            stm.close();
            //sql = "SELECT COUNT(*)/2 " + "FROM DEVELOPER WHERE UPPER(GROUP_NAME) = '" + wuName.toUpperCase() + "'";
            // Avoid to use function in column (ie. Where Upper(...)=... )
            sql = "SELECT COUNT(*)/2 " + "FROM DEVELOPER WHERE GROUP_NAME = '" + wuName + "'";
            sql = sql + " AND STATUS = 2";
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                calEff2 = rs.getDouble(1);
            }
            rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return (calEff1 + calEff2);
        }
    }
    public static final double getCalendarEffort() {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        double calEff1 = 0;
        double calEff2 = 0;
        try {
            sql = "SELECT COUNT(*) FROM DEVELOPER WHERE STATUS = 1";
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                calEff1 = rs.getDouble(1);
                calEff1 = calEff1 - 10;
                //Do not count GLs, boss, they never check in timesheet, it is told by Ana, suze ?
            }
            rs.close();
            stm.close();
            sql = "SELECT COUNT(*)/2 FROM DEVELOPER WHERE STATUS = 2";
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                calEff2 = rs.getDouble(1);
            }
            rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return (calEff1 + calEff2);
        }
    }
    
    // HuyNH2 modify function for project archive
    // calculate from timesheet and timesheet_archive.
    public static final double getEffortByGroup(String wuName, final int curMonth, final int curYear) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = null;
        double timesheetEff = 0;
        try {
            ReportMonth actualMonth = new ReportMonth();
            int year = actualMonth.getYear();
            int month = actualMonth.getMonth();
            java.util.Date now = new java.util.Date();
            String currentDate;
            currentDate = CommonTools.dateFormat(now);
            int nNumWorkingDay;
            String beginMonth;
            String endMonth;
            if ((year == curYear) && (month == curMonth)) { // Get busy rate of the current Month
                int noDayToNow = Integer.parseInt(currentDate.substring(0, 2));
                nNumWorkingDay = CommonTools.getNoWorkingDay(noDayToNow, curMonth, curYear);
            }
            else {
                nNumWorkingDay = 22; //default number of working days per month
            }
            beginMonth = CommonTools.getMonth(curMonth) + "/" + "01" + "/" + String.valueOf(curYear);
            endMonth = CommonTools.getMonth(curMonth) + "/" + CommonTools.getNoDay(curMonth, curYear) + "/" + String.valueOf(curYear);
            // calculate from timesheet
            sql =
                "SELECT (SUM(DURATION)/(8*"
                    + nNumWorkingDay
                    + "))  "
                    + "FROM TIMESHEET, DEVELOPER WHERE DEVELOPER.GROUP_NAME = '"
                    + wuName.toUpperCase()
                    + "'"
                    + " AND (DEVELOPER.STATUS = 1 OR DEVELOPER.STATUS = 2)"
                    + " AND (TIMESHEET.STATUS = 2 OR TIMESHEET.STATUS = 4)"
                    + " AND (TIMESHEET.OCCUR_DATE >= TO_DATE('"
                    + beginMonth
                    + "','MM/DD/YYYY'))"
                    + " AND (TIMESHEET.OCCUR_DATE <= TO_DATE('"
                    + endMonth
                    + "','MM/DD/YYYY'))"
                    + " AND TIMESHEET.DEVELOPER_ID = DEVELOPER.DEVELOPER_ID";
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                timesheetEff = rs.getInt(1);
            }
            // calculate from timesheet_archive
            sql =
                "SELECT (SUM(DURATION)/(8*"
                    + nNumWorkingDay
                    + "))  "
                    + "FROM TIMESHEET_ARCHIVE, DEVELOPER WHERE DEVELOPER.GROUP_NAME = '"
                    + wuName.toUpperCase()
                    + "'"
                    + " AND (DEVELOPER.STATUS = 1 OR DEVELOPER.STATUS = 2)"
                    + " AND (TIMESHEET_ARCHIVE.STATUS = 2 OR TIMESHEET_ARCHIVE.STATUS = 4)"
                    + " AND (TIMESHEET_ARCHIVE.OCCUR_DATE >= TO_DATE('"
                    + beginMonth
                    + "','MM/DD/YYYY'))"
                    + " AND (TIMESHEET_ARCHIVE.OCCUR_DATE <= TO_DATE('"
                    + endMonth
                    + "','MM/DD/YYYY'))"
                    + " AND TIMESHEET_ARCHIVE.DEVELOPER_ID = DEVELOPER.DEVELOPER_ID";
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                timesheetEff += rs.getInt(1);
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return timesheetEff;
        }
    }
    public static final double getEffortByGroup(final int curMonth, final int curYear) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = null;
        double timesheetEff = 0;
        try {
            ReportMonth actualMonth = new ReportMonth();
            int year = actualMonth.getYear();
            int month = actualMonth.getMonth();
            java.util.Date now = new java.util.Date();
            String currentDate;
            currentDate = CommonTools.dateFormat(now);
            int nNumWorkingDay;
            String beginMonth;
            String endMonth;
            if ((year == curYear) && (month == curMonth)) // Get busy rate of the current Month
                {
                int noDayToNow = Integer.parseInt(currentDate.substring(0, 2));
                nNumWorkingDay = CommonTools.getNoWorkingDay(noDayToNow, curMonth, curYear);
            }
            else {
                nNumWorkingDay = 22; //default number of working days per month
            }
            beginMonth = CommonTools.getMonth(curMonth) + "/" + "01" + "/" + String.valueOf(curYear);
            endMonth = CommonTools.getMonth(curMonth) + "/" + CommonTools.getNoDay(curMonth, curYear) + "/" + String.valueOf(curYear);
            sql =
                "SELECT (SUM(DURATION)/(8*"
                    + nNumWorkingDay
                    + ")) TIMESHEET_EFFORT "
                    + "FROM TIMESHEET, DEVELOPER WHERE "
                    + " (DEVELOPER.STATUS = 1 OR DEVELOPER.STATUS = 2)"
                    + " AND (TIMESHEET.STATUS = 2 OR TIMESHEET.STATUS = 4)"
                    + " AND (TIMESHEET.OCCUR_DATE >= TO_DATE('"
                    + beginMonth
                    + "','MM/DD/YYYY'))"
                    + " AND (TIMESHEET.OCCUR_DATE <= TO_DATE('"
                    + endMonth
                    + "','MM/DD/YYYY'))"
                    + " AND TIMESHEET.DEVELOPER_ID = DEVELOPER.DEVELOPER_ID";
                    
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                timesheetEff = rs.getInt(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Effort.java, line code: 1870 sql = " + sql);
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return timesheetEff;
        }
    }
    
    // HuyNH2 modify function for project archive 
    /**
     * get effort spent for DP activities
     * @param startDate start date of report
     * @param endDate end date of report
     * @param nProjectID ident.number of project
     * @return effort in person day
     */    
    public static double getDPEffort(java.sql.Date startDate, java.sql.Date endDate, long nProjectID) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        double dRetVal = 0;
        String timesheetTable = "timesheet";
        if(Db.checkProjecIsArchive(nProjectID)){
            timesheetTable = "timesheet_archive";
        }
        String projConstraint = (nProjectID > 0) ? " AND project_id=?" : "";
        String dateContraint = (startDate == null || endDate == null) ? "" : " AND occur_date BETWEEN ? AND ?";
        try {
                String strSQL =
                    "SELECT SUM(duration)/8 FROM " + timesheetTable + " WHERE status=4 AND kpa_id=16 "
            // count only records approved by QA with KPA=DP
    +projConstraint + dateContraint;
            con = ServerHelper.instance().getConnection();
            prepStmt = con.prepareStatement(strSQL);
            int i = 1;
            if (nProjectID > 0)
                prepStmt.setLong(i++, nProjectID);
            if (dateContraint.length() > 0) {
                prepStmt.setDate(i++, startDate);
                prepStmt.setDate(i++, endDate);
            }
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                dRetVal = rs.getDouble(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(con, prepStmt, rs);
            return dRetVal;
        }
    }
    //HuyNH2 modify this function for project archive
    public static double getProcessEffortByUsers(java.sql.Date startDate, java.sql.Date endDate, long[] users, int processID) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        double dRetVal = 0;
        String dateContraint = (startDate == null || endDate == null) ? "" : " AND occur_date BETWEEN ? AND ?";
        try {
            String strSQL =
                "SELECT SUM(duration) FROM timesheet"
                    + " WHERE status=4 AND PROCESS_ID="
                    + processID
                    + " AND DEVELOPER_ID IN ("
                    + ConvertString.arrayToString(users, ",")
                    + ")"
                    + dateContraint;
            con = ServerHelper.instance().getConnection();
            prepStmt = con.prepareStatement(strSQL);
            int i = 1;
            /*if (nProjectID>0)
            	prepStmt.setLong(i++, nProjectID);*/
            if (dateContraint.length() > 0) {
                prepStmt.setDate(i++, startDate);
                prepStmt.setDate(i++, endDate);
            }
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                dRetVal = rs.getDouble(1);
            }
            rs.close();
            prepStmt.close();
            //HuyNH2 add code here
            strSQL =
                "SELECT SUM(duration) FROM TIMESHEET_ARCHIVE"
                    + " WHERE status=4 AND PROCESS_ID="
                    + processID
                    + " AND DEVELOPER_ID IN ("
                    + ConvertString.arrayToString(users, ",")
                    + ")"
                    + dateContraint;
            prepStmt = con.prepareStatement(strSQL);
            i = 1;
            /*if (nProjectID>0)
                prepStmt.setLong(i++, nProjectID);*/
            if (dateContraint.length() > 0) {
                prepStmt.setDate(i++, startDate);
                prepStmt.setDate(i++, endDate);
            }
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                dRetVal += rs.getDouble(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(con, prepStmt, rs);
            return dRetVal;
        }
    }
    /**
     * Based on pple belonging to groups, not on WU
     *
     */
    //Huynh2 modify this function for project archive.
    public static double getSupportGroupEffortForProjects(String groupName, java.sql.Date startDate, java.sql.Date endDate) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        double dRetVal = 0;
        try {
            String strSQL =
                "SELECT SUM(DURATION) "
                    + "FROM TIMESHEET,PROJECT "
                    + "WHERE TIMESHEET.status=4 "
                    + "AND occur_date BETWEEN ? AND ? "
                    + "AND TIMESHEET.developer_id IN( SELECT DEVELOPER_ID FROM DEVELOPER WHERE DEVELOPER.group_name=?) "
                    + "AND Project.project_ID=TIMESHEET.project_ID "
                    + "AND Project.type<>'9' "
                    + "AND NOT( Project.type='8' AND SUBSTR(Project.name,0,5)='Daily') ";
            con = ServerHelper.instance().getConnection();
            prepStmt = con.prepareStatement(strSQL);
            prepStmt.setDate(1, startDate);
            prepStmt.setDate(2, endDate);
            prepStmt.setString(3, groupName);
            rs = prepStmt.executeQuery();
            if (rs.next())
                dRetVal = rs.getDouble(1);
            rs.close();
            prepStmt.close();
                                
            //HuyNH2 add code for project archive.    
            strSQL =
                "SELECT SUM(DURATION) "
                    + "FROM TIMESHEET_ARCHIVE,PROJECT "
                    + "WHERE TIMESHEET_ARCHIVE.status=4 "
                    + "AND occur_date BETWEEN ? AND ? "
                    + "AND TIMESHEET_ARCHIVE.developer_id IN( SELECT DEVELOPER_ID FROM DEVELOPER WHERE DEVELOPER.group_name=?) "
                    + "AND Project.project_ID=TIMESHEET_ARCHIVE.project_ID "
                    + "AND Project.type<>'9' "
                    + "AND NOT( Project.type='8' AND SUBSTR(Project.name,0,5)='Daily') ";
            prepStmt = con.prepareStatement(strSQL);
            prepStmt.setDate(1, startDate);
            prepStmt.setDate(2, endDate);
            prepStmt.setString(3, groupName);
            rs = prepStmt.executeQuery();
            if (rs.next())
                dRetVal = rs.getDouble(1);


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(con, prepStmt, rs);
            return dRetVal;
        }
    }
    /**
     * returns array of 3 vals
     * 0: project ID
     * 1: prevention effort
     * 2: total effort
     */
    public static double[][] getPreventionCosts(java.sql.Date startDate, java.sql.Date endDate, long[] projects) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        double dRetVal[][] = null;
        String strSQL = null;
        try {
            String sdcond = (startDate == null) ? "" : " AND occur_date >= ? ";
            double projectID;
            dRetVal = new double[projects.length][3];
            for (int i = 0; i < projects.length; i++)
                dRetVal[i][0] = projects[i];
            strSQL =
                "SELECT PROJECT_ID, SUM(DURPREVENT) SUMDURPREVENT, SUM(DURTOTAL) SUMDURTOTAL FROM ("
                    + " SELECT PROJECT_ID,SUM(DURATION) DURPREVENT,0 DURTOTAL"
                    + " FROM TIMESHEET"
                    + " WHERE PROCESS_ID IN("
                    + ConvertString.arrayToString(ProcessInfo.preventionProcesses, ",")
                    + " ) AND PROJECT_ID in("
                    + ConvertString.arrayToString(projects, ",")
                    + ")"
                    + sdcond
                    + " AND occur_date <= ?"
                    + " AND STATUS =4 GROUP BY PROJECT_ID"
                    + " UNION "
                    + " SELECT PROJECT_ID,0 DURPREVENT,SUM(DURATION) DURTOTAL "
                    + " FROM TIMESHEET"
                    + " WHERE PROJECT_ID in("
                    + ConvertString.arrayToString(projects, ",")
                    + ")"
                    + sdcond
                    + " AND occur_date <= ?"
                    + " AND STATUS =4 GROUP BY PROJECT_ID"
                    + ") GROUP BY PROJECT_ID";
            con = ServerHelper.instance().getConnection();
            prepStmt = con.prepareStatement(strSQL);
            if (startDate == null) {
                prepStmt.setDate(1, endDate);
                prepStmt.setDate(2, endDate);
            }
            else {
                prepStmt.setDate(1, startDate);
                prepStmt.setDate(2, endDate);
                prepStmt.setDate(3, startDate);
                prepStmt.setDate(4, endDate);
            }
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                projectID = rs.getDouble("PROJECT_ID");
                for (int i = 0; i < projects.length; i++)
                    if (dRetVal[i][0] == projectID) {
                        dRetVal[i][1] = rs.getDouble("SUMDURPREVENT");
                        dRetVal[i][2] = rs.getDouble("SUMDURTOTAL");
                    }
            }
            rs.close();
            prepStmt.close();
            //HuyNH2 add code for projeca             
            strSQL =
                "SELECT PROJECT_ID, SUM(DURPREVENT) SUMDURPREVENT, SUM(DURTOTAL) SUMDURTOTAL FROM ("
                    + " SELECT PROJECT_ID,SUM(DURATION) DURPREVENT,0 DURTOTAL"
                    + " FROM TIMESHEET_ARCHIVE"
                    + " WHERE PROCESS_ID IN("
                    + ConvertString.arrayToString(ProcessInfo.preventionProcesses, ",")
                    + " ) AND PROJECT_ID in("
                    + ConvertString.arrayToString(projects, ",")
                    + ")"
                    + sdcond
                    + " AND occur_date <= ?"
                    + " AND STATUS =4 GROUP BY PROJECT_ID"
                    + " UNION "
                    + " SELECT PROJECT_ID,0 DURPREVENT,SUM(DURATION) DURTOTAL "
                    + " FROM TIMESHEET_ARCHIVE"
                    + " WHERE PROJECT_ID in("
                    + ConvertString.arrayToString(projects, ",")
                    + ")"
                    + sdcond
                    + " AND occur_date <= ?"
                    + " AND STATUS =4 GROUP BY PROJECT_ID"
                    + ") GROUP BY PROJECT_ID";
            prepStmt = con.prepareStatement(strSQL);
            if (startDate == null) {
                prepStmt.setDate(1, endDate);
                prepStmt.setDate(2, endDate);
            }
            else {
                prepStmt.setDate(1, startDate);
                prepStmt.setDate(2, endDate);
                prepStmt.setDate(3, startDate);
                prepStmt.setDate(4, endDate);
            }
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                projectID = rs.getDouble("PROJECT_ID");
                for (int i = 0; i < projects.length; i++)
                    if (dRetVal[i][0] == projectID) {
                        dRetVal[i][1] += rs.getDouble("SUMDURPREVENT");
                        dRetVal[i][2] += rs.getDouble("SUMDURTOTAL");
                    }
            }
            //End add code
            
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Effort.getPreventionCosts() sql=" + strSQL);
        }
        finally {
            ServerHelper.closeConnection(con, prepStmt, rs);
            return dRetVal;
        }
    }
    public static double calcEffortStatus(double planEffort, double actualEffort, double reqCompleteness) {
        return actualEffort / planEffort / reqCompleteness * 10000d;
    }
    /**
     * return vector of ProcessEffortByStageInfo
     */
    public static Vector getProcessEffortByStage(ProjectInfo projectInfo, Vector stageList) {
        return getProcessEffortByStage(projectInfo, stageList,-1);
    }
    public static Vector getProcessEffortByStage(ProjectInfo projectInfo, Vector stageList,int curStage) {
        //DB access
        StageInfo firstStage = (StageInfo)stageList.elementAt(0);
        Vector plan = Requirement.getDBPlanRCR(projectInfo.getProjectId());
        Vector planRCRProcess = Effort.getPlanProcessEffortAndRCR(projectInfo);
        Vector normEffortDistribs = Norms.getEffortDstrByProc(Parameters.FSOFT_WU, projectInfo.getLifecycleId(), projectInfo.getStartDate());
        Vector norms = Norms.getNormList(projectInfo, RequirementInfo.getRCRMetricConstants());
        Vector reqs = Requirement.getRequirementList(projectInfo);
        Vector normRCR=null;
        //Processing
        StageInfo stageInfo;
        StageInfo stageInfoTemp;
        StageInfo stageInfCurrent = null;
        StageInfo stageInfPrev = null;
        ProcessEffortByStageInfo info = null;
        ProcessEffortInfo processEffortInfo = null;
        Vector vt = new Vector();
        int metricid;
        int[] stdStageCount = Schedule.getStdStageCount(stageList);
        NormInfo normInfo;
        PlanRCRInfo planInf;
        PlanRCRInfo planInfPrev;
        int currentStageID = 0;
        //get ratio RCR proces/total processes
        NormRefInfo nrInf;
        double ratioRCRProc = 0;
        		
        for (int j = 0; j < ProcessInfo.RCRProcesses.length; j++) {
            nrInf = NormRefInfo.get(ProcessInfo.RCRProcesses[j], normEffortDistribs);

			if(nrInf!=null)
			{
				ratioRCRProc += nrInf.value;
			}

            if (nrInf!=null)
            	ratioRCRProc += nrInf.value;

        }
        ratioRCRProc /= 100d;
        //get current stage
        for (int i = 0; i < stageList.size(); i++) {
            stageInfCurrent = (StageInfo)stageList.elementAt(i);
            if (i > 0)
                stageInfPrev = (StageInfo)stageList.elementAt(i - 1);
            currentStageID = i;			
			
            if (stageInfCurrent.aEndD == null) {
                break;
            }
        }
        if (curStage>=0){
            stageInfCurrent = (StageInfo)stageList.elementAt(curStage);
            if (curStage > 0)
                stageInfPrev = (StageInfo)stageList.elementAt(curStage - 1);
            currentStageID=curStage;
        }
        double[][] actualStageEffort = null;
        double[][] EERCR = new double[stageList.size()][ProcessInfo.trackedProcessId.length];
        for (int i = 0; i < stageList.size(); i++)
            Arrays.fill(EERCR[i], Double.NaN);
        double[] avgEERCR = null;
		double[] avgEERCRDeviation = null;
        boolean isEngineeringProcess = false;
		boolean hasBeenReplan=false;
        				
		hasBeenReplan=checkPlanEffortProcess(stageInfCurrent.milestoneID);

        for (int i = 0; i < stageList.size(); i++) {
            stageInfo = (StageInfo)stageList.get(i);
            if (stageInfo.aEndD != null)
                actualStageEffort = getActualEffortProcessByDate(projectInfo.getProjectId(), stageInfo.actualBeginDate, stageInfo.aEndD, true);
            info = new ProcessEffortByStageInfo();
            info.stage_name = stageInfo.stage;
            info.stageid = stageInfo.milestoneID;
            info.isOpen = ((curStage>=0 && i>=curStage)|| stageInfo.aEndD == null);
            for (int j = 0; j < ProcessInfo.trackedProcessId.length; j++) {
                isEngineeringProcess = CommonTools.arrayScan(ProcessInfo.RCRProcesses, ProcessInfo.trackedProcessId[j]) >= 0;
                //NORM
                normInfo=null;
                nrInf=null;
                if (stageInfo.StandardStage >=1){
                    metricid = MetricDescInfo.getRCRMetricConstant(ProcessInfo.trackedProcessId[j], stageInfo.StandardStage);
                    normInfo = NormInfo.getNormByMetricID(metricid, norms);
                    nrInf = NormRefInfo.get(ProcessInfo.trackedProcessId[j], normEffortDistribs);
                    
                    if (normInfo != null)
						/*if ((currentStageID > 0) && (curStage != 0))
	                        info.norm[j] =
	                            nrInf.value * normInfo.average * pinf.planned_effort / 10000d / (double)stdStageCount[stageInfo.StandardStage - 1];
	                    else
	                    */
							info.norm[j] =
								nrInf.value * normInfo.average * projectInfo.getBaseEffort() / 10000d / (double)stdStageCount[stageInfo.StandardStage - 1];
                }
                if (!info.isOpen) {
                    //  ACTUAL EFFORT  
                    int x = CommonTools.arrayScan(actualStageEffort[0], ProcessInfo.trackedProcessId[j]);
                    if (x >= 0)
                        info.actual[j] = actualStageEffort[1][x];                        

                    // ACTUAL RCR
                    if (isEngineeringProcess) {
                        info.actualRCR[j] =
                            Requirement.getActualRCRByProcess(
                                reqs,
                                ProcessInfo.trackedProcessId[j],
                                stageInfo.actualBeginDate,
                                stageInfo.aEndD);
                        if (info.actualRCR[j] != 0)
                        {
							EERCR[i][j] = info.actual[j] / info.actualRCR[j];
                        }
                    }
                }else{
                	//Added by BinhNT
					info.actual[j]=Double.NaN;
                }
                //PLAN
				planInf = PlanRCRInfo.getPlan(plan, ProcessInfo.trackedProcessId[j], stageInfo.milestoneID, firstStage.milestoneID);
                if (isEngineeringProcess) {
                    if (planInf != null){
                        for (int k = 0; k < planRCRProcess.size(); k++) {
                            processEffortInfo = (ProcessEffortInfo)planRCRProcess.elementAt(k);
                            if (processEffortInfo.processID == ProcessInfo.trackedProcessId[j]) {
								//BinhNT comment
								//if ((currentStageID > 0) && (curStage != 0))
	                            //    info.planned[j] =
	                            //        planInf.plannedValue * processEffortInfo.estimatedRCR * pinf.base_effort / 10000d * ratioRCRProc;
	                            //else
									info.planned[j] = planInf.plannedEffort;
                                break;
                            }
                        }
                        //if no plan RCR use norm instead
                        if (Double.isNaN(info.planned[j])){
							if ((currentStageID > 0) && (curStage != 0))
							{
								if (normRCR==null)
									normRCR= Norms.getEffortDstrByProcRCR(projectInfo);
	                                
								NormInfo ninf=NormInfo.getNormByMetricID(MetricDescInfo.getCompletionMetricByProcess(ProcessInfo.trackedProcessId[j]),normRCR);
								if (ninf != null) {
                                    info.planned[j] = planInf.plannedValue * ninf.average* projectInfo.getBaseEffort() / 10000d * ratioRCRProc;
								}
							}
							else
								info.planned[j] = planInf.plannedEffort;
                        }
                    }
                }
                else if (normInfo != null && stageInfo.StandardStage>=1) {
					//BinhNT comment
					//if ((currentStageID > 0) && (curStage != 0))
	                //    info.planned[j] =
	                //        nrInf.value * normInfo.average * pinf.base_effort / 10000d / (double)stdStageCount[stageInfo.StandardStage - 1];
	                //else
						if (planInf != null)
							info.planned[j] = planInf.plannedEffort;
                }
                
                //REPLAN                               
				if ((currentStageID > 0) && (curStage != 0))
				{
					planInf =PlanRCRInfo.getPlan(plan,ProcessInfo.trackedProcessId[j],stageInfo.milestoneID,
					(i >= currentStageID) ? stageInfCurrent.milestoneID : stageInfo.milestoneID);
							//(i >= currentStageID) ? stageInfCurrent.milestoneID : stageInfo.milestoneID	
																
					if (planInf != null && !Double.isNaN(planInf.plannedEffort)){
						//Binhnt														
						info.rePlanned[j] = planInf.plannedEffort;												
						info.rePlannedLatest[j] = info.rePlanned[j];	
																															
																																			
						//In case Second stage
						if (i==0){							
							if (!hasBeenReplan && currentStageID==1) //Check has been Replanned
								info.rePlanned[j] = Double.NaN;							
							else
								info.rePlanned[j] = info.actual[j];
								
							info.rePlannedLatest[j] = info.rePlanned[j];																														
						}					
						
					}
					else{						
						for (int k = currentStageID-1; k >=1; k--) {
							stageInfoTemp=(StageInfo)stageList.elementAt(k);
							planInf =
								   PlanRCRInfo.getPlan(
									   plan,
									   ProcessInfo.trackedProcessId[j],
									   stageInfo.milestoneID,
										stageInfoTemp.milestoneID);
										
							if (planInf != null && !Double.isNaN(planInf.plannedEffort)){								
								info.rePlannedLatest[j] = planInf.plannedEffort;						
								break;
							}
						}						
					}
				}
				else
				{
					
				}								
				
						
//				-- PPM Effort 2.0
//				-- RCR and EE Deviations
				if (!info.isOpen) {
					if (planInf != null && !Double.isNaN(planInf.plannedEffort)){
						info.effortDeviation[j] = (info.actual[j] - info.rePlannedLatest[j])/info.rePlannedLatest[j];
					}						
					else
						info.effortDeviation[j] = 0;
					
					if (planInf != null && !Double.isNaN(planInf.plannedValue))
						info.rcrDeviation[j] = (info.actualRCR[j] - planInf.plannedValue)/planInf.plannedValue;
					else
						info.rcrDeviation[j] = 0;
				}
            }
            vt.add(info);
        }
        //2nd loop to forecast
        avgEERCR = getAvgEERCR(EERCR, currentStageID);
		avgEERCRDeviation = getAvgEERCRDeviation(vt);
		
        //no forecast for 1st stage
        if (stageInfPrev != null)
            for (int i = currentStageID; i < vt.size(); i++) {
                info = (ProcessEffortByStageInfo)vt.elementAt(i);
                if (info.isOpen) {
                    for (int j = 0; j < ProcessInfo.trackedProcessId.length; j++) {
                        isEngineeringProcess = CommonTools.arrayScan(ProcessInfo.RCRProcesses, ProcessInfo.trackedProcessId[j]) >= 0;
                        if (isEngineeringProcess) {
/*                        	
                            planInf =
                                PlanRCRInfo.getPlan(
                                    plan,
                                    ProcessInfo.trackedProcessId[j],
                                    info.stageid,
                                    stageInfCurrent.milestoneID);
                            if (planInf != null && !Double.isNaN(avgEERCR[j])){
                                System.out.println("case 1:EERCR"+avgEERCR[j]+"  plan "+planInf.plannedValue);
                                info.forecast[j] = avgEERCR[j] * planInf.plannedValue;
                            }
                            else {
                                planInfPrev =
                                    PlanRCRInfo.getPlan(
                                        plan,
                                        ProcessInfo.trackedProcessId[j],
                                        info.stageid,
                                        stageInfPrev.milestoneID);
                                if (planInfPrev != null) {
                                  if (planInf != null && planInfPrev.plannedValue != 0)
                                        info.forecast[j] = planInf.plannedValue / planInfPrev.plannedValue * planInfPrev.plannedEffort;
                                    else
                                        info.forecast[j] = planInfPrev.plannedEffort;
                                }
                            }
*/
// -- PPM Effort 2.0
							planInfPrev =
								PlanRCRInfo.getPlan(
									plan,
									ProcessInfo.trackedProcessId[j],
									info.stageid,
									stageInfPrev.milestoneID);
							if (planInfPrev != null) {
								if (
									(planInfPrev.plannedValue == 0) ||
									(Double.isNaN(planInfPrev.plannedValue))
									)
									info.forecast[j] = info.planned[j];
								else
								{
									planInf =
										PlanRCRInfo.getPlan(
											plan,
											ProcessInfo.trackedProcessId[j],
											info.stageid,
											stageInfCurrent.milestoneID);
									if (planInf != null && !Double.isNaN(avgEERCRDeviation[j]))
										info.forecast[j] = (1 - avgEERCRDeviation[j])*info.planned[j] + avgEERCRDeviation[j]*info.planned[j]/planInfPrev.plannedValue*planInf.plannedValue;
									else
										info.forecast[j] = info.planned[j];
								}
							}
							else
								info.forecast[j] = info.planned[j];
														                            
                            if (!Double.isNaN(info.forecast[j]))
                                info.totalForecast += info.forecast[j];
                            if (currentStageID == 1) {
                                //second stage: the previous is plan instead of replan
                                if (!Double.isNaN(info.planned[j]))
                                    info.totalPlannedPrev += info.planned[j];
                            }
                            else {
                                planInfPrev =
                                    PlanRCRInfo.getPlan(
                                        plan,
                                        ProcessInfo.trackedProcessId[j],
                                        info.stageid,
                                        stageInfPrev.milestoneID);
                                if (planInfPrev != null && !Double.isNaN(planInfPrev.plannedEffort))
                                    info.totalPlannedPrev += planInfPrev.plannedEffort;
                            }
                        }
                    }
                    //second pass for support processes
                    if (info.totalPlannedPrev != 0)
                        for (int j = 0; j < ProcessInfo.trackedProcessId.length; j++) {
                            isEngineeringProcess = CommonTools.arrayScan(ProcessInfo.RCRProcesses, ProcessInfo.trackedProcessId[j]) >= 0;
                            if (!isEngineeringProcess) {
                                if (currentStageID == 1) {
                                    //second stage: the previous is plan instead of replan
                                    info.forecast[j] = info.planned[j] / info.totalPlannedPrev * info.totalForecast;
                                }
                                else {
                                    planInfPrev =
                                        PlanRCRInfo.getPlan(
                                            plan,
                                            ProcessInfo.trackedProcessId[j],
                                            info.stageid,
                                            stageInfPrev.milestoneID);
                                    if (planInfPrev != null)
                                        info.forecast[j] = planInfPrev.plannedEffort / info.totalPlannedPrev * info.totalForecast;
                                }
                            }
                        }
                }
            }
        return vt;
    }
    /**
     * 
     * @param vector of ProjectInfo
     * @return new double[ProcessInfo.trackedProcessId.length][StageInfo.stageList.length];
     */
    public static double[][] getAverageEffortByProcessAndStage(Vector projectList){
        double[][] actualAllStageEffort = new double[ProcessInfo.trackedProcessId.length][StageInfo.stageList.length];
        double[][] percentStageEffort = new double[ProcessInfo.trackedProcessId.length][StageInfo.stageList.length];
        double[][] actualStageEffort =null;
        for (int i = 0; i < percentStageEffort.length; i++)
            Arrays.fill(percentStageEffort[i], 0);
       // CommonTools.arrayScan(actualStageEffort[0], ProcessInfo.trackedProcessId[j]);
        ProjectInfo projectInfo = null;
        Vector stageList;
        StageInfo stageInfo;
        boolean oneIsMapped=false;
        int [] nproject=new int[ProcessInfo.trackedProcessId.length];
        Arrays.fill(nproject, 0);
        double totalEffort;
        int otherID=CommonTools.arrayScan(ProcessInfo.trackedProcessId,ProcessInfo.OTHER);
        for (int k = 0; k < projectList.size(); k++) {
			projectInfo = (ProjectInfo)projectList.elementAt(k);
            oneIsMapped=false;
            stageList=Schedule.getStageList(projectInfo);
            for (int i = 0; i < actualAllStageEffort.length; i++)
                Arrays.fill(actualAllStageEffort[i], 0);
                //get the absolute effort
            for (int i = 0; i < stageList.size(); i++) {
                stageInfo = (StageInfo)stageList.get(i);
                if (stageInfo.aEndD != null && stageInfo.StandardStage>0 ){
                    
                    actualStageEffort = getActualEffortProcessByDate(projectInfo.getProjectId(), stageInfo.actualBeginDate, stageInfo.aEndD, true);

                    for (int m = 0; m < actualStageEffort[0].length; m++){
                        int x=CommonTools.arrayScan(ProcessInfo.trackedProcessId, (int)actualStageEffort[0][m]);
                        if(!Double.isNaN(actualStageEffort[1][m]) && actualStageEffort[1][m]!=0){
                            oneIsMapped=true;
                            if (x>=0)
                                actualAllStageEffort[x][stageInfo.StandardStage-1]+=actualStageEffort[1][m];
                            else
                                actualAllStageEffort[otherID][stageInfo.StandardStage-1]+=actualStageEffort[1][m];
                        }

                    }
                        
                }
            }
            if (oneIsMapped){
                //calc the % and add it to the sum
                for (int m=0;m<actualAllStageEffort.length;m++){
                    totalEffort=CommonTools.arraySum(actualAllStageEffort[m]);
                    if (totalEffort>0){
                        nproject[m]++;//if total =0 cant get the distrib > remove from average
                        for (int n=0;n<actualAllStageEffort[m].length;n++){
                            percentStageEffort[m][n]+=actualAllStageEffort[m][n]*100d/totalEffort;
                        }

                    }
                    
                    
                }
            }
                
        }
        for (int m=0;m<percentStageEffort.length;m++){
            if (nproject[m]!=0)
                for (int n=0;n<percentStageEffort[m].length;n++){
                        percentStageEffort[m][n]/=nproject[m];
                }
            
        }
        return percentStageEffort;
    }
    private static double[] getAvgEERCR(double[][] ratios, int currentStageID) {
        double[] avgs = new double[ratios[0].length];
        double rowTotal;
        double count;
        for (int j = 0; j < avgs.length; j++) {
            rowTotal = 0;
            count = 0;
            for (int i = 0; i < currentStageID; i++) {
                if (!Double.isNaN(ratios[i][j])) {
                    rowTotal += ratios[i][j];
                    count++;
                }
            }
            if (count != 0)
                avgs[j] = rowTotal / count;
        }
        return avgs;
    }
    private static double[] getAvgEERCRDeviation(Vector vtProcessEffortByStageInfo)
    {
		int numTrackedProcess = ProcessInfo.trackedProcessId.length;
		double[] avgs = new double[numTrackedProcess];
		
    	ProcessEffortByStageInfo info;
    	
		for (int l = 0; l < numTrackedProcess; l++) {
			double rowDeviation = 0;
			double rowTotal = 0;
			double count = 0;
			
	    	for (int k = 0; k < vtProcessEffortByStageInfo.size(); k ++)
	    	{
				info = (ProcessEffortByStageInfo)vtProcessEffortByStageInfo.elementAt(k);
				if (!info.isOpen)
				{
					rowDeviation = info.effortDeviation[k]/info.rcrDeviation[k];
					rowTotal += rowDeviation;
					count++;
				}
			}
			if (count != 0)
				avgs[l] = rowTotal / count;
    	}
		return avgs;
    }
    public static final void updatePlanEffortStageProcess(Vector planRCRInfos) {
        Requirement.updateStageProcess( planRCRInfos,1);
    }
    public static final boolean checkPlanEffortProcess(long milestoneID){
        return Requirement.checkPlanProcessStage(milestoneID,true);
    }
	public static final Vector getProcessEffortList(ProjectInfo projectInfo, Date startDate, Date endDate) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		Vector vt = new Vector();
		ResultSet rs = null;
		ProcessEffortInfo info = null;
		try {
			java.sql.Date date =
				(projectInfo.getStartDate() == null)
					? new java.sql.Date(new java.util.Date().getTime())
					: projectInfo.getStartDate();
			Vector norms = Norms.getEffortDstrByProc(Parameters.FSOFT_WU, projectInfo.getLifecycleId(),date);
			double budgetEffort = getBudgetEffort(projectInfo.getProjectId())[2];

			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT PE.*"
					+ " FROM PROCESS_EFFORT PE"
					+ " WHERE PE.PROJECT_ID = ?";
			stm=conn.prepareStatement(sql);
			stm.setLong(1, projectInfo.getProjectId());
			rs = stm.executeQuery();
			//Norm
			
			while (rs.next()) {
				info = new ProcessEffortInfo();
				info.prjID = projectInfo.getProjectId();
				info.processID = rs.getInt("PROCESS_ID");
				info.proEffID = rs.getLong("PROCESS_EFFORT_ID");
				info.estimated = Db.getDouble(rs, "PLAN_EFFORT");
				info.reEstimated = Db.getDouble(rs, "RE_PLAN_EFFORT");
				//9 is the id for project management process, project planning and monitoring are sub entities of it
				for (int i = 0; i < norms.size(); i++){
					NormRefInfo nref = (NormRefInfo) norms.elementAt(i);
					if (nref.prcID == info.processID){
						info.process = nref.prcName;
						info.norm = budgetEffort* nref.value/ 100d;
						break;
					}
				}
				vt.add(info);
			}
			if (vt.size() == 0 && (addAllProcessEffort(projectInfo.getProjectId()).size()>0)){
				return getProcessEffortList(projectInfo, startDate, endDate);
			}
			else{
				for (int i=0;i<vt.size();i++){
					info =(ProcessEffortInfo)vt.elementAt(i);
					info.actual = getActualEffortProcessByDate_Old(projectInfo, info.processID, startDate, endDate);
					info.deviation = CommonTools.metricDeviation(info.estimated, info.reEstimated, info.actual);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return vt;
		}

	}
	
	// Add by HaiMM
	public static final Vector getEstEffortList(ProjectInfo projectInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		Vector vt = new Vector();
		ResultSet rs = null;
		EstimateEffortInfo info = null;
		try {

			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT EST.*"
					+ " FROM EST_EFFORT EST, MILESTONE MI"
					+ " WHERE EST.MILESTONE_ID = MI.MILESTONE_ID" 
					+ "	AND EST.PROJECT_ID = ?";
			stm=conn.prepareStatement(sql);
			stm.setLong(1, projectInfo.getProjectId());
			rs = stm.executeQuery();
			while (rs.next()) {
				info = new EstimateEffortInfo();
				info.prjID = projectInfo.getProjectId();
				info.estEffID = rs.getLong("EST_EFFORT_ID");
				info.milestoneID = rs.getLong("MILESTONE_ID");

				info.requirement = Db.getDouble(rs, "REQ");
				info.design = Db.getDouble(rs, "DES");
				info.coding = Db.getDouble(rs, "COD");
				info.unitTest = Db.getDouble(rs, "U_TEST");
				info.test = Db.getDouble(rs, "TEST");
				info.deployment = Db.getDouble(rs, "DEPLOY");
				info.acceptanceTest = Db.getDouble(rs, "A_TEST");
				info.projPlanning = Db.getDouble(rs, "PROJ_PLAN");
				info.projMonitoring = Db.getDouble(rs, "PROJ_MO");
				info.qualityAssurance = Db.getDouble(rs, "QA");
				info.training = Db.getDouble(rs, "TRAINING");
				info.others = Db.getDouble(rs, "OTHERS");
				
				vt.add(info);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return vt;
		}

	}
	
	public static Vector getProcessEffortByStageList(final ProjectInfo projectInfo, Vector stageList) {
		StageInfo stageInfo;
		ProcessEffortByStageInfo_Old info = null;
		Vector vt = new Vector();
		try {
			float actualEffort;
			//countNAStage use to determind number of stages which has not known actual end date
			// if find the fist stage does not know actual end date
			int countNAStage = 0;
			for (int i = 0; i < stageList.size(); i++) {
				stageInfo = (StageInfo) stageList.get(i);
				if (i == 0){
					stageInfo.actualBeginDate = CommonTools.getDateForPreviousYear(stageInfo.actualBeginDate);
				}
				if (i == stageList.size() -1){
					stageInfo.aEndD = CommonTools.getDateForNextYear(new Date(System.currentTimeMillis()));
				}
				if (stageInfo.actualBeginDate == null)
					stageInfo.actualBeginDate = stageInfo.plannedBeginDate;
				if (stageInfo.aEndD == null)
					stageInfo.aEndD = stageInfo.pEndD;
				info = new ProcessEffortByStageInfo_Old();
				info.stage_name = stageInfo.stage;
				for (int j = 0; j < ProcessInfo.trackedProcessId.length; j++) {
					actualEffort = Float.NaN;
					if (stageInfo.aEndD == null){
						stageInfo.aEndD = new Date(System.currentTimeMillis());
						countNAStage ++;
					}
					if (countNAStage <= 1){
						actualEffort = getActualEffortProcessByDate_Old(
								projectInfo,
								ProcessInfo.trackedProcessId[j],
								stageInfo.actualBeginDate,
								stageInfo.aEndD);
					}
					switch (ProcessInfo.trackedProcessId[j]) {
						case ProcessInfo.PROJECT_PLANNING :
							info.projectPlaning = actualEffort;
							break;
						case ProcessInfo.PROJECT_MONITORING :
							info.projectMonitoring = actualEffort;
							break;
						case ProcessInfo.REQUIREMENT :
							info.requirement = actualEffort;
							break;
						case ProcessInfo.DESIGN :
							info.design = actualEffort;
							break;
						case ProcessInfo.CODING :
							info.coding = actualEffort;
							break;
						case ProcessInfo.DEPLOYMENT :
							info.deployment = actualEffort;
							break;
						case ProcessInfo.CUSTOMER_SUPPORT :
							info.customerSupport = actualEffort;
							break;
						case ProcessInfo.TEST :
							info.test = actualEffort;
							break;
						case ProcessInfo.CM :
							info.configManagement = actualEffort;
							break;
						case ProcessInfo.QUALITY_CONTROL :
							info.qualityControl = actualEffort;
							break;
						case ProcessInfo.TRAINING :
							info.training = actualEffort;
							break;
						case ProcessInfo.OTHER :
							info.other = actualEffort;
							break;
					}
				}
				vt.add(info);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return vt;
	}
	public static final double [] getBudgetEffort(final long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		double [] result={Double.NaN,Double.NaN,Double.NaN};
		try {
			sql = "SELECT BASE_EFFORT, PLAN_EFFORT FROM PROJECT WHERE PROJECT_ID = " + prjID;
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			rs.next();
			result[0]= Db.getDouble(rs,"BASE_EFFORT");
			result[1]= Db.getDouble(rs,"PLAN_EFFORT");
			result[2]=(Double.isNaN(result[1]))?result[0]:result[1];
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return result;
		}
	}
	private static Vector addAllProcessEffort(final long prjID) {
		Vector vt = null;
		try {
			vt = new Vector();
			for (int i = 0; i < ProcessInfo.trackedProcessId.length; i++) {
				final ProcessEffortInfo info = new ProcessEffortInfo();
				info.prjID = prjID;
				info.processID = ProcessInfo.trackedProcessId[i];
				vt.add(info);
				if (!addProcessEffort(info)) return vt;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return vt;
		}
	}
	private static boolean addProcessEffort(final ProcessEffortInfo info) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
        String insertStatement = "";
		boolean rslt = true;
		try {
			conn = ServerHelper.instance().getConnection();
			insertStatement =
				" INSERT INTO PROCESS_EFFORT ("
					+ " PROCESS_EFFORT_ID,"
					+ " PROJECT_ID,"
					+ " PROCESS_ID )"
					+ " VALUES (?,?,?)";
			prepStmt = conn.prepareStatement(insertStatement);
			final long id = ServerHelper.getNextSeq("PROCESS_EFFORT_SEQ");
			prepStmt.setLong(1, id);
			prepStmt.setLong(2, info.prjID);
			prepStmt.setLong(3, info.processID);
			final int rowCount = prepStmt.executeUpdate();
			if (rowCount == 0) {
				rslt = false;
			}
			return rslt;
		}
		catch (Exception e) {
			e.printStackTrace();
            System.err.println("insertStatement = " + insertStatement);
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public static final boolean updateProcessEffort(final ProcessEffortInfo info) {
		String updateStatement = "";
		Connection conn = null;
		PreparedStatement prepStmt = null;
		boolean bl = true;
		try {
			conn = ServerHelper.instance().getConnection();
			updateStatement =
				" UPDATE PROCESS_EFFORT "
					+ " SET PLAN_EFFORT = ?,"
					+ " RE_PLAN_EFFORT = ?"
					+ " WHERE PROCESS_EFFORT_ID = ?";
			prepStmt = conn.prepareStatement(updateStatement);
			Db.setDouble(prepStmt, 1, info.estimated);
			Db.setDouble(prepStmt, 2, info.reEstimated);
			prepStmt.setLong(3, info.proEffID);
			final int rowCount = prepStmt.executeUpdate();
			if (rowCount == 0) {
				bl = false;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("sql stmt = " + updateStatement);
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
			return bl;
		}
	}
	/**
	 * Get all Effort of each stage of project.
	 * @param projectInfo
	 * @return vector of effortStage.
	 */
	public static final Vector getEffortListByStage(final ProjectInfo projectInfo, Vector stageList) {
		Vector stageEffortList = new Vector();
		EffortHeaderInfo effortHeader = getEffortHeader(projectInfo);
		// Vector projectInfoList = Schedule.getStageList(projectInfo);
		double numI = 0, numD = 0, numS = 0, numC = 0, numT = 0, numTe = 0;
		StageInfo stageInfo;
		int countNAStage = 0;
		for (int i = 0; i < stageList.size(); i++) {
			stageInfo = (StageInfo) stageList.elementAt(i);
			if (i == 0){
				stageInfo.actualBeginDate = CommonTools.getDateForPreviousYear(stageInfo.actualBeginDate);
			}
			if (i == stageList.size() -1){
				stageInfo.aEndD = CommonTools.getDateForNextYear(stageInfo.aEndD);
			}
			final StageEffortInfo info = new StageEffortInfo();
			info.prjID = projectInfo.getProjectId();
			info.estimated = stageInfo.estimatedEffort;
			info.reEstimated = stageInfo.reEstimatedEffort;
			info.stage = stageInfo.stage;
			info.milestoneID = stageInfo.milestoneID;
			info.standardStage = stageInfo.StandardStage;
			if (stageInfo.milestoneID != 0) {
				if (stageInfo.actualBeginDate != null) {
					if (stageInfo.aEndD == null){ //if stage started but not closed then we calculate effort up to today
						countNAStage ++;
						stageInfo.aEndD = new java.sql.Date(System.currentTimeMillis());
					}
						//first stage includes all previous effort
					if (countNAStage <= 1){
						info.actual = getActualEffort(projectInfo.getProjectId(), (i==0)?null:stageInfo.actualBeginDate, stageInfo.aEndD);
					}
				}
			}
			info.deviation = CommonTools.metricDeviation(info.estimated, info.reEstimated, info.actual);
			switch (info.standardStage) {
				case StageInfo.STAGE_INITIATION :
					numI++;
					break;
				case StageInfo.STAGE_DEFINITION :
					numD++;
					break;
				case StageInfo.STAGE_SOLUTION :
					numS++;
					break;
				case StageInfo.STAGE_CONSTRUCTION :
					numC++;
					break;
				case StageInfo.STAGE_TRANSITION :
					numT++;
					break;
				case StageInfo.STAGE_TERMINATION :
					numTe++;
					break;
			}
			stageEffortList.add(info);
		}
		double budgetedEffort = Double.parseDouble(effortHeader.budgetedEffort) / 100d;
		java.sql.Date date =
			(projectInfo.getStartDate() == null)
				? new java.sql.Date(new java.util.Date().getTime())
				: projectInfo.getStartDate();
		
		//Norms.getEffortDstrByStg(prInfo);
		
		boolean notStandard;
		for (int i = 0; i < stageEffortList.size(); i++) {
			StageEffortInfo info = (StageEffortInfo) stageEffortList.elementAt(i);
			notStandard = false;
			switch (info.standardStage) {
				case StageInfo.STAGE_INITIATION :
					info.norm = budgetedEffort / numI;
					break;
				case StageInfo.STAGE_DEFINITION :
					info.norm = budgetedEffort / numD;
					break;
				case StageInfo.STAGE_SOLUTION :
					info.norm = budgetedEffort / numS;
					break;
				case StageInfo.STAGE_CONSTRUCTION :
					info.norm = budgetedEffort / numC;
					break;
				case StageInfo.STAGE_TRANSITION :
					info.norm = budgetedEffort / numT;
					break;
				case StageInfo.STAGE_TERMINATION :
					info.norm = budgetedEffort / numTe;
					break;
				default :
					notStandard = true;
					break;
			}
			if (!notStandard) {
				final Vector dstList = Norms.getEffortDstrByStg(Parameters.FSOFT_WU, projectInfo.getLifecycleId(), date);
				for (int j = 0; j < dstList.size(); j++) {
					NormRefInfo nref = (NormRefInfo) dstList.elementAt(j);
					if (nref.prcID == info.standardStage) {
						info.norm *= nref.value;
						break;
					}
				}
			}
		}
		return stageEffortList;
	}
	public static final EffortHeaderInfo getEffortHeader(final ProjectInfo projectInfo) {
		EffortHeaderInfo info = new EffortHeaderInfo();
		EffortInfo effortInfo = getEffortInfo(projectInfo);
		//info.projectCode = effortInfo.projectCode;
		info.budgetedEffort = CommonTools.formatDouble(effortInfo.budgetedEffort);
		info.actualEffort = CommonTools.formatDouble(effortInfo.actualEffort);
		info.effortUseage = CommonTools.formatDouble(effortInfo.effortUseage);
		info.effortDeviation = CommonTools.formatDouble(effortInfo.effortDeviation);
		info.developementEffort = CommonTools.formatDouble(effortInfo.developementEffort);
		info.managementEffort = CommonTools.formatDouble(effortInfo.managementEffort);
		info.qualityEffort = CommonTools.formatDouble(effortInfo.qualityEffort);
		info.correctionEffort = CommonTools.formatDouble(effortInfo.correctionEffort);
		info.perDevelopementEffort = CommonTools.formatDouble(effortInfo.perDevelopementEffort);
		info.perManagementEffort = CommonTools.formatDouble(effortInfo.perManagementEffort);
		info.perQualityEffort = CommonTools.formatDouble(effortInfo.perQualityEffort);
		info.perCorrectionEffort = CommonTools.formatDouble(effortInfo.perCorrectionEffort);
		return info;
	}
    //Huynh2 modify function for project archive.
    //if proejct
	public static final EffortInfo getEffortInfo(final long prjID) {
		return getEffortInfo(prjID, new Date(0));
	}
	public static final EffortInfo getEffortInfo(final long prjID, java.util.Date date) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		EffortInfo info = new EffortInfo();
		ResultSet rs = null;
		String dateConstraint = null;
        long time = 0;
        long actual_finish_date = 0;
		ProjectInfo projectInfo = Project.getProjectInfo(prjID);
        if(date != null)
           time = date.getTime();
        if(projectInfo.getActualFinishDate() != null)
            actual_finish_date = projectInfo.getActualFinishDate().getTime();
        boolean check = false;
        if(time == 0)
            check = true;
        if(time >= actual_finish_date)
            check = true;        
        String timesheetTable = "";
        if(projectInfo.getArchiveStatus() == 4){
            timesheetTable = "TIMESHEET_ARCHIVE";            
        }else{
            timesheetTable = "TIMESHEET";
        }                    
		try {
			dateConstraint = ((date != null) && (date.getTime() != 0))?" AND OCCUR_DATE <= '" + CommonTools.dateFormat(date) + "'":"";

			//budgeted effort and actual
			double[] effort=getBudgetEffort(prjID);
			info.plannedEffort=effort[0];
			info.rePlannedEffort=effort[1];
			info.budgetedEffort =effort[2];
            //Huynh2 modify function for project archive
            if(projectInfo.getArchiveStatus() == 4 && check){//archive
                sql = "SELECT ACTUAL_EFFORT FROM project_archive WHERE project_id = "+ prjID;
            }else{
    			sql =
    				"SELECT SUM(DURATION)/(8) ACTUAL_EFFORT "
    					+ " FROM " + timesheetTable + " "
    					+ " WHERE PROJECT_ID = "
    					+ prjID
    					+ " AND STATUS =4"
    					+ dateConstraint;
            }
            System.out.print("Effort.class getEffortInfo function sql = "+ sql );
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			//approved by QA
			rs = stm.executeQuery(sql);
			if (rs.next()) {
				info.actualEffort = rs.getDouble("ACTUAL_EFFORT");
			}
			rs.close();
			// Effort effectiveness
			if (info.budgetedEffort != 0) {
				info.effortUseage = (info.actualEffort * 100d / info.budgetedEffort);
				// effort deviation
				info.effortDeviation = info.effortUseage - 100d;
			}
			else if (info.actualEffort == 0) {
				info.effortUseage = 0;
				info.effortDeviation = 0;
			}
			else {
				info.effortUseage = Double.NaN;
				info.effortDeviation = Double.NaN;
			}
			if (info.actualEffort != 0) {
				// developement effort, quality effort, management effort
                if(projectInfo.getArchiveStatus() == 4 && check){//archive
                    sql = "SELECT ACTUAL_MAN_EFFORT FROM project_archive WHERE project_id = "+ prjID;
                }else{                
					sql =
						"SELECT SUM(DURATION)/8 ACTUAL_MAN_EFFORT "
							+ " FROM " + timesheetTable + " "
							+ " WHERE PROJECT_ID = "
							+ prjID
							+ " AND STATUS =4"
							+ " AND PROCESS_ID IN (1,9,11,15,16,19,20, 21,22, 23,24,25, 27)" //12 process + 27 = Technology management
							+ dateConstraint;
                }
				rs = stm.executeQuery(sql);
				if (rs.next()) {
					info.managementEffort = rs.getDouble("ACTUAL_MAN_EFFORT");
					info.perManagementEffort = info.managementEffort * 100d / info.actualEffort;
				}
				rs.close();
                if(projectInfo.getArchiveStatus() == 4 && check){//archive
                    sql = "SELECT ACTUAL_DEV_EFFORT FROM project_archive WHERE project_id = " + prjID;
                }else{
    				sql =
    					"SELECT SUM(DURATION)/8 ACTUAL_DEV_EFFORT "
    						+ " FROM " + timesheetTable + " "
    						+ " WHERE PROJECT_ID = "
    						+ prjID
    						+ " AND STATUS =4"
    						+ " AND PROCESS_ID IN (2, 3, 4, 5, 6, 8, 26)"
    						+ " AND TOW_ID IN (1, 2,6)"
    						+ dateConstraint;    				
                }    
                rs = stm.executeQuery(sql);
				if (rs.next()) {
					info.developementEffort = rs.getDouble("ACTUAL_DEV_EFFORT");
					info.perDevelopementEffort = info.developementEffort * 100d / info.actualEffort;
				}
				rs.close();
				//correction effort
                if(projectInfo.getArchiveStatus() == 4 && check){//archive
                    sql = "SELECT CORRECTION_COST FROM project_archive WHERE project_id = "+ prjID;
                }else{
    				sql =
    					"SELECT SUM(DURATION)/8 CORRECTION_COST "
    						+ " FROM " + timesheetTable + " "
    						+ " WHERE PROJECT_ID = "
    						+ prjID
    						+ " AND STATUS=4 "
    						+ " AND (( PROCESS_ID IN (2, 3, 4, 5, 6, 7, 8,26)"
    						+ " AND TOW_ID = 5) "
    						+ " OR ( PROCESS_ID = 14))"
    						+ dateConstraint;
                }
				rs = stm.executeQuery(sql);
				if (rs.next()) {
					info.correctionEffort = rs.getDouble("CORRECTION_COST");
					info.perCorrectionEffort = info.correctionEffort * 100d / info.actualEffort;
				}
				rs.close();
				info.qualityEffort = (info.actualEffort - info.developementEffort - info.managementEffort);
				info.perQualityEffort = info.qualityEffort * 100d / info.actualEffort;
				//translation effort
                if(projectInfo.getArchiveStatus() == 4 && check){//archive
                    sql = "SELECT TRANS_COST FROM project_archive WHERE project_id = "+ prjID;
                }else{
    				sql =
    					"SELECT SUM(DURATION)/8 TRANS_COST "
    						+ " FROM " + timesheetTable + " "
    						+ " WHERE PROJECT_ID = "
    						+ prjID
    						+ " AND STATUS=4"
    						+ " AND TOW_ID = 6"
    						+ dateConstraint;
                }            
				rs = stm.executeQuery(sql);
				if (rs.next()) {
					info.translationEffort= rs.getDouble("TRANS_COST");
					info.perTranslationEffort= info.translationEffort * 100d / info.actualEffort;
				}
				rs.close();
			}
			else {
				info.correctionEffort = Double.NaN;
				info.managementEffort = Double.NaN;
				info.developementEffort = Double.NaN;
				info.qualityEffort = Double.NaN;
				info.translationEffort = Double.NaN;
			}
			return info;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("sql stmt = " + sql);
			return info;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	public static final boolean updateStageEffort(final StageEffortInfo info) {
		String updateStatement = "";
		Connection conn = null;
		PreparedStatement prepStmt = null;
		boolean bl = true;
		try {
			conn = ServerHelper.instance().getConnection();
			updateStatement = " UPDATE MILESTONE SET BASE_EFFORT = ?, PLAN_EFFORT = ? WHERE MILESTONE_ID = ?";
			prepStmt = conn.prepareStatement(updateStatement);
			Db.setDouble(prepStmt, 1, info.estimated);
			Db.setDouble(prepStmt, 2, info.reEstimated);
			prepStmt.setLong(3, info.milestoneID);
			final int rowCount = prepStmt.executeUpdate();
			bl = (rowCount != 0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
			return bl;
		}
	}
	public static final float getActualEffortProcessByDate_Old(
		final ProjectInfo projectInfo,
		final long processID,
	/*final String processName,*/ //Only be used when processName="Other"
	java.util.Date startDate, java.util.Date endDate) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;        
		final boolean byDate = (endDate != null);
		String strByDate = null;
        String timesheetTable = "";
		//ProjectInfo projectInfo = Project.getProjectInfo(prjID);
        if(projectInfo.getArchiveStatus()==4){
            timesheetTable = "TIMESHEET_ARCHIVE";
        }else{
            timesheetTable = "TIMESHEET";
        }
		if (byDate)
			strByDate =
				((startDate != null) ? " AND OCCUR_DATE >= '" + CommonTools.dateFormat(startDate) + "' " : "")
					+ " AND OCCUR_DATE <= '"
					+ CommonTools.dateFormat(endDate)
					+ "' ";
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			if ((processID == ProcessInfo.PROJECT_PLANNING)
				|| (processID == ProcessInfo.PROJECT_MONITORING)) //Monitoring project and planing project
				{
				sql =
					"SELECT  SUM(DURATION)/(8) ACTUAL_EFFORT FROM  " + timesheetTable + " "
						+ " WHERE PROJECT_ID = "
						+ projectInfo.getProjectId()
						+ " AND STATUS=4"
						+ ((byDate) ? strByDate : "")
						+ " AND PROCESS_ID = "
						+ ProcessInfo.PROJECT_MANAGEMENT
						+ " AND KPA_ID "
						+ ((processID == ProcessInfo.PROJECT_PLANNING) ? "=2" : "!=2");
				rs = stm.executeQuery(sql);
				return (rs.next()) ? rs.getFloat("ACTUAL_EFFORT") : 0;
			}
			else if (processID != ProcessInfo.OTHER) {
				sql =
					"SELECT SUM(DURATION)/(8) ACTUAL_EFFORT FROM " + timesheetTable + " "
						+ " WHERE PROJECT_ID = "
						+ projectInfo.getProjectId()
						+ " AND PROCESS_ID = "
						+ processID
						+ " AND STATUS=4 "
						+ ((byDate) ? strByDate : "");
				rs = stm.executeQuery(sql);
				return (rs.next()) ? rs.getFloat("ACTUAL_EFFORT") : 0;
			}
			else //Calculate for other effort
				{
				sql =
					"SELECT SUM(DURATION)/8 ACTUAL_EFFORT FROM " + timesheetTable + " "
						+ " WHERE PROJECT_ID = "
						+ projectInfo.getProjectId()
						+ " AND (PROCESS_ID NOT IN ("
						+ ConvertString.arrayToString(ProcessInfo.trackedProcessId, ",")
						+ ","
						+ ProcessInfo.PROJECT_MANAGEMENT
						+ ") OR PROCESS_ID = "
						+ ProcessInfo.OTHER
						+ ") AND STATUS=4 "
						+ ((byDate) ? strByDate : "");
				rs = stm.executeQuery(sql);
				return (rs.next()) ? rs.getFloat("ACTUAL_EFFORT") : 0;
			}
		}
		catch (SQLException ex){
			ex.printStackTrace();
			return 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	
    // Get development effort, management effort, quality effort from Project Plan
    public static EffortTypeInfo getEffortType(final long projectId) {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = null;
        EffortTypeInfo effortTypeInfo = new EffortTypeInfo();
        try {
            conn = ServerHelper.instance().getConnection();
            sql = "SELECT  MANAGEMENT_EFFORT, QUALITY_EFFORT FROM PROJECT_PLAN WHERE PROJECT_ID = ?";
            stm = conn.prepareStatement(sql);
            stm.setLong(1, projectId);
            rs = stm.executeQuery();
            if (rs.next()) {
                effortTypeInfo.setQualityEffort(rs.getFloat("QUALITY_EFFORT"));
                effortTypeInfo.setManagementEffort( rs.getFloat("MANAGEMENT_EFFORT"));
                effortTypeInfo.setDevelopmentEffort(100 - (effortTypeInfo.getManagementEffort() + effortTypeInfo.getQualityEffort()));
            }
            rs.close();
            stm.close();
            return effortTypeInfo;
        }
        catch (Exception e) {
            e.printStackTrace();
            return effortTypeInfo;
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
        }
    }
}