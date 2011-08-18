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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import com.fms1.infoclass.*;
import com.fms1.tools.CommonTools;
import com.fms1.tools.Db;
import com.fms1.web.ServerHelper;
/**
 * Logic of Schedule menu item
 *
 */
public class Schedule {
	/**
	 * returns vector of StageInfo objects sorted by acending planned finish date
	 */
	public static final Vector getStageList(long prjID) {
		ProjectInfo projectInfo = Project.getProjectInfo(prjID);
		return getStageList(projectInfo);
	}
	public static final StageInfo getStageByID(long milestoneID) {
		Connection conn = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		String sql = null;
		StageInfo stageInfo = new StageInfo();
		try {
			conn = ServerHelper.instance().getConnection();
			//get the planned end date of the stage
			sql =
				"SELECT milestone.milestone_id milestone_id, project_id, name, complete, base_start_date,"
					+ " plan_start_date, actual_start_date, base_finish_date,"
					+ " plan_finish_date, actual_finish_date, base_effort, plan_effort,"
					+ " actual_effort, milestone.description description, milestone.milestone milestone, NVL ( PLAN_FINISH_DATE, BASE_FINISH_DATE) PLANNED_FINISH_DATE, "
					+ " (select count(*) from iteration where iteration.milestone_id =milestone.milestone_id ) NITERATIONS,COMMENTS, STANDARDSTAGE "
					+ " FROM milestone "
					+ " WHERE milestone.MILESTONE_ID = ?";
			preStm = conn.prepareStatement(sql);
			preStm.setLong(1, milestoneID);
			rs = preStm.executeQuery();
			if (rs.next()) {
				stageInfo.milestoneID = rs.getLong("milestone_id");
				stageInfo.prjID = rs.getLong("project_id");
				stageInfo.stage = rs.getString("name");
				stageInfo.bEndD = rs.getDate("base_finish_date");
				stageInfo.pEndD = rs.getDate("plan_finish_date");
				stageInfo.aEndD = rs.getDate("actual_finish_date");
				System.out.println("actual end date"+stageInfo.aEndD);
				stageInfo.description = rs.getString("description");
				stageInfo.milestone = rs.getString("milestone");
				stageInfo.plannedEndDate = rs.getDate("PLANNED_FINISH_DATE");
				stageInfo.iterationCnt = rs.getInt("NITERATIONS");
				stageInfo.comments = Db.getClob(rs, "COMMENTS");
				stageInfo.StandardStage = rs.getInt("STANDARDSTAGE");
				rs.close();
				preStm.close();

                if ((stageInfo.stage.equalsIgnoreCase("Termination")) ||
                    (stageInfo.StandardStage == StageInfo.STAGE_TERMINATION))
                {
					// This is for actual end date of termination stage if user forgets to enter it
					sql = "SELECT ACTUAL_FINISH_DATE, START_DATE FROM PROJECT WHERE PROJECT_ID=" + stageInfo.prjID;
					preStm = conn.prepareStatement(sql);
					rs = preStm.executeQuery(sql);
					Date actualEndDate = null;
					if (rs.next()) {
						actualEndDate = rs.getDate("ACTUAL_FINISH_DATE");
						if (actualEndDate != null) {
							stageInfo.aEndD = actualEndDate;
						}
					}
					rs.close();
					preStm.close();
				}
				//get planned begin date of stage (=plan finish date of previous stage +1)
				sql =
					"SELECT (ACTUAL_FINISH_DATE+1) ACTUAL_END_DATE, NVL ( PLAN_FINISH_DATE, BASE_FINISH_DATE)+1 PLANNED_FINISH_DATE"
						+ " FROM MILESTONE "
						+ " WHERE PROJECT_ID = ?"
						+ " AND (NVL ( PLAN_FINISH_DATE, BASE_FINISH_DATE)+1) <= ?"
						+ " ORDER BY PLANNED_FINISH_DATE DESC";
				preStm = conn.prepareStatement(sql);
				preStm.setLong(1, stageInfo.prjID);
				preStm.setDate(2, stageInfo.plannedEndDate);
				rs = preStm.executeQuery();
				if (rs.next()) {
					stageInfo.plannedBeginDate = rs.getDate("PLANNED_FINISH_DATE");
					stageInfo.actualBeginDate = rs.getDate("ACTUAL_END_DATE");
				}
				else { //no previous stage, use the project planned start date instead
					ProjectInfo objProject = Project.getProjectInfo(stageInfo.prjID);
					if (objProject.getPlanStartDate() != null) {
						stageInfo.plannedBeginDate = objProject.getPlanStartDate();
						stageInfo.actualBeginDate =
							(objProject.getStartDate() != null) ? objProject.getStartDate() : stageInfo.plannedBeginDate;
					}
					else {
						System.err.println("StageInfo.getStageByID : Project planned start date not defined");
					}
				}
				double lgPlannedDuration = CommonTools.dateDiff(stageInfo.plannedBeginDate, stageInfo.plannedEndDate);
				stageInfo.pDuration = CommonTools.decForm.format(lgPlannedDuration);
				if ((stageInfo.aEndD != null) && (stageInfo.actualBeginDate != null)) {
                    double lgActualDuration = CommonTools.dateDiff(stageInfo.actualBeginDate, stageInfo.aEndD);
					stageInfo.aDuration = CommonTools.decForm.format(lgActualDuration);
					if (lgPlannedDuration != 0)
						stageInfo.duDeviation =
							CommonTools.decForm.format(
								(lgActualDuration - lgPlannedDuration) * 100.0 / lgPlannedDuration);
				}
				if (stageInfo.description == null)
					stageInfo.description = "N/A";
				if (stageInfo.stage == null)
					stageInfo.stage = "N/A";
				if (stageInfo.milestone == null)
					stageInfo.milestone = "N/A";
				if ((stageInfo.aEndD != null)) {
					if (stageInfo.pEndD != null)
						stageInfo.isOntime = (stageInfo.aEndD.compareTo(stageInfo.pEndD) <= 0) ? "Yes" : "No";
					else if (stageInfo.bEndD != null)
						stageInfo.isOntime = (stageInfo.aEndD.compareTo(stageInfo.bEndD) <= 0) ? "Yes" : "No";
				}
				if (stageInfo.aEndD != null
					&& stageInfo.plannedEndDate != null
					&& stageInfo.pDuration != null
					&& lgPlannedDuration != 0) {
					stageInfo.deviation =
						CommonTools.decForm.format(
							(stageInfo.aEndD.getTime() - stageInfo.plannedEndDate.getTime())
								* 100.0
								/ ((24 * 3600 * 1000) * lgPlannedDuration));
				}
			}
			else {
				System.err.println("StageInfo.getStageByID : StageID" + milestoneID + " not found");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, rs);
			return stageInfo;
		}
	}
	/**
	 * All stages of a project, much faster than calling one by one
	 * return Vector of StageInfo
	 */
	public static final Vector getStageList(ProjectInfo projectInfo) {
		Connection conn = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		String sql = null;
		Vector result = new Vector();
		try {
			conn = ServerHelper.instance().getConnection();
			//get the planned end date of the stage
			sql =
				"SELECT milestone.*, NVL ( PLAN_FINISH_DATE, BASE_FINISH_DATE) PLANNED_FINISH_DATE,DEVELOPER.ACCOUNT, "
					+ " (select count(*) from iteration where iteration.milestone_id =milestone.milestone_id ) NITERATIONS "
					+ " FROM milestone,DEVELOPER "
					+ " WHERE milestone.project_id = ?"
					+ " AND DEVELOPER.DEVELOPER_ID(+)=milestone.CONDUCTOR"
					+ " ORDER BY PLANNED_FINISH_DATE ASC";
			preStm = conn.prepareStatement(sql);
            //System.out.println("sql = "+ sql +"  ===project_id = "+projectInfo.project_id);
			preStm.setLong(1, projectInfo.getProjectId());
			rs = preStm.executeQuery();
			StageInfo stageInfo=null;
            StageInfo prevStageInfo;
			while (rs.next()) {
				stageInfo = new StageInfo();
				stageInfo.milestoneID = rs.getLong("milestone_id");
				stageInfo.prjID = rs.getLong("project_id");
				stageInfo.stage = rs.getString("name");
				stageInfo.bEndD = rs.getDate("base_finish_date");
				stageInfo.pEndD = rs.getDate("plan_finish_date");
				stageInfo.aEndD = rs.getDate("actual_finish_date");
				//System.out.println("actual finish date"+stageInfo.aEndD);
				stageInfo.description = rs.getString("description");
				stageInfo.milestone = rs.getString("milestone");
				stageInfo.plannedEndDate = rs.getDate("PLANNED_FINISH_DATE");
				stageInfo.iterationCnt = rs.getInt("NITERATIONS");
				stageInfo.comments = Db.getClob(rs, "COMMENTS");
				stageInfo.StandardStage = rs.getInt("STANDARDSTAGE");
				stageInfo.estimatedEffort = Db.getDouble(rs, "BASE_EFFORT");
				stageInfo.reEstimatedEffort = Db.getDouble(rs, "PLAN_EFFORT");
				stageInfo.QGateConductor=rs.getLong("CONDUCTOR");
				stageInfo.QGateConductorName=rs.getString("ACCOUNT");
				result.add(stageInfo);
			}
            if (stageInfo!=null)
                //stageInfo.aEndD = projectInfo.getActualFinishDate();
			ServerHelper.closeConnection(conn, preStm, rs);
            //Unknow purpose of this function. Rem 11-Mar-2007
            //Huynh2 Rem this function.
            /**/
			for (int i = 0; i < result.size(); i++) {
				stageInfo = (StageInfo) result.elementAt(i);
				//initiation stage starts with the beginning of project
				if (i == 0) {
					stageInfo.plannedBeginDate = projectInfo.getPlanStartDate();
					stageInfo.actualBeginDate =
						(projectInfo.getStartDate() != null) ? projectInfo.getStartDate() : stageInfo.plannedBeginDate;
				}
				else {
					//get planned begin date of stage (=plan finish date of previous stage +1)
					prevStageInfo = (StageInfo) result.elementAt(i - 1);
					if (prevStageInfo.plannedEndDate != null)
						stageInfo.plannedBeginDate =
							new java.sql.Date(prevStageInfo.plannedEndDate.getTime() + (long) (24 * 3600 * 1000));
					if (prevStageInfo.aEndD != null)
						stageInfo.actualBeginDate =
							new java.sql.Date(prevStageInfo.aEndD.getTime() + (long) (24 * 3600 * 1000));
				}
				double lgPlannedDuration = CommonTools.dateDiff(stageInfo.plannedBeginDate, stageInfo.plannedEndDate);
				stageInfo.pDuration = CommonTools.decForm.format(lgPlannedDuration);
				if ((stageInfo.aEndD != null) && (stageInfo.actualBeginDate != null)) {
                    double lgActualDuration = CommonTools.dateDiff(stageInfo.actualBeginDate, stageInfo.aEndD);
					stageInfo.aDuration = CommonTools.decForm.format(lgActualDuration);
					if (lgPlannedDuration != 0)
						stageInfo.duDeviation =
							CommonTools.decForm.format(
								(lgActualDuration - lgPlannedDuration) * 100.0 / lgPlannedDuration);
				}
				if (stageInfo.description == null)
					stageInfo.description = "N/A";
				if (stageInfo.stage == null)
					stageInfo.stage = "N/A";
				if (stageInfo.milestone == null)
					stageInfo.milestone = "N/A";
				if ((stageInfo.aEndD != null)) {
					if (stageInfo.pEndD != null)
						stageInfo.isOntime = (stageInfo.aEndD.compareTo(stageInfo.pEndD) <= 0) ? "Yes" : "No";
					else if (stageInfo.bEndD != null)
						stageInfo.isOntime = (stageInfo.aEndD.compareTo(stageInfo.bEndD) <= 0) ? "Yes" : "No";
				}
				if (stageInfo.aEndD != null
					&& stageInfo.plannedEndDate != null
					&& stageInfo.pDuration != null
					&& lgPlannedDuration != 0) {
					stageInfo.deviation =
						CommonTools.decForm.format(
							(stageInfo.aEndD.getTime() - stageInfo.plannedEndDate.getTime())
								* 100d
								/ ((24 * 3600 * 1000) * lgPlannedDuration));
				}
			}
            /**/
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, preStm, rs);
			return result;
		}
	}
	
	/**
	 * result[0]= num stages closed
	 * result[1]=num stages
	 * 
	 */
	public static final int[] getStageCount(long prjID, java.sql.Date sRptDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		String sql = null;
		int[] result = new int[2];
		result[0] = 0;
		result[1] = 0;
		try {
			sql =
				"SELECT "
					+ "COUNT(MILESTONE_ID) N_MSTN "
					+ "FROM "
					+ "MILESTONE "
					+ "WHERE "
					+ "ACTUAL_FINISH_DATE <= ? AND "
					+ "PROJECT_ID = ?";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setDate(1, sRptDate);
			prepStmt.setLong(2, prjID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				result[0] = rs.getInt("N_MSTN");
			}
			rs.close();
			prepStmt.close();
			sql = "SELECT COUNT(MILESTONE_ID) N_MSTN FROM MILESTONE WHERE PROJECT_ID = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				result[1] = rs.getInt("N_MSTN");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return result;
		}
	}
	/**
	 *@return sheduleHeaderInfo
	 *@param projectID
	 */
	public static final ScheduleHeaderInfo getSchHeader(final long prjID) {
		ScheduleHeaderInfo info = new ScheduleHeaderInfo();
		try {
			ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			info.pStartD = projectInfo.getPlanStartDate();
			info.aStartD = projectInfo.getStartDate();
			info.pEndD = projectInfo.getPlannedFinishDate();
			info.aEndD = projectInfo.getActualFinishDate();
			info.remainD = (projectInfo.getActualFinishDate()==null)?(double) CommonTools.dateDiff(new java.util.Date(), info.pEndD) + 1:(double) CommonTools.dateDiff(projectInfo.getActualFinishDate(), info.pEndD) ;
			double timeliness = getTimeliness(prjID);
			info.timeliness = (Double.isNaN(timeliness)) ? -1 : (float) timeliness;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return info;
		}
	}
	public static final double getTimeliness(final long prjID) {
		return getTimeliness(prjID, null);
	}
	public static final double getTimeliness(final long prjID, java.sql.Date date) {
		double returnVal = Double.NaN;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			double total = 0;
			double ontime = 0;
			Date aDate = null;
			Date pDate = null;
			boolean dateDefined = (date != null && date.getTime() != 0);
				sql =
					" SELECT ACTUAL_RELEASE_DATE, NVL(REPLANNED_RELEASE_DATE,PLANNED_RELEASE_DATE) PDATE"
						+ " FROM MODULE "
						+ " WHERE PROJECT_ID = ? "
						+ " AND IS_DELIVERABLE = 1 "
						+ " AND STATUS != 4 " ;//not cancelled
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			java.util.Date today = new java.util.Date();
			while (rs.next()) {
				aDate = rs.getDate("ACTUAL_RELEASE_DATE");
				pDate = rs.getDate("PDATE");
				if (!dateDefined){
					if (aDate != null) {
						total++;
						if ((pDate != null) && (aDate.compareTo(pDate) <= 0))
							ontime++;
					}
					else if ((pDate != null) && (pDate.compareTo(today) <= 0)) {
						total++;
					}
				}
				else {
				
					if (aDate != null && aDate.compareTo(date) <= 0) {
						total++;
						if ((pDate != null) && (aDate.compareTo(pDate) <= 0))
							ontime++;
					}
					else if ((pDate != null) && (pDate.compareTo(date) <= 0)) {
						total++;
					}
				
				}
			}
			if (total != 0)
				returnVal = (double) ontime * 100d / (double) total;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return returnVal;
		}
	}

    public static final int [] getStdStageCount(Vector stageList) {
    	int [] stdStageCount=new int[StageInfo.stageList.length];
    	StageInfo stageInf=null;
    	//calc number of instances of std stage
    	for (int i=0;i<stageList.size();i++){
    		stageInf=(StageInfo)stageList.elementAt(i);
    		if (stageInf.StandardStage>0)
    			stdStageCount[stageInf.StandardStage-1]++;
    	}
    	return stdStageCount;
    }
    /**
     * Must update the final stage of milestone before closing this project because:
     * FsoftInsight don't allow update the final stage
     * @param projectInfo
     * @return true if update successfully, false if update failt
     * @throws SQLException
     */
    public static boolean doUpdateMilestoneBeforeCloseProject(ProjectInfo projectInfo) throws SQLException{
		Connection conn = null;
		PreparedStatement preStm = null;
		ResultSet rs = null;
		String sql = null;
		long lMilestoneID = 0;
    	try{
    		conn = ServerHelper.instance().getConnection();
    		conn.setAutoCommit(false);
    		sql = "SELECT MILESTONE_ID, NVL(BASE_FINISH_DATE, PLAN_FINISH_DATE) PLANNED_FINISH_DATE" +
    			  " FROM MILESTONE" +
    			  " WHERE PROJECT_ID = ? AND ACTUAL_FINISH_DATE IS NULL" +
    			  " ORDER BY PLANNED_FINISH_DATE ASC";
    		preStm = conn.prepareStatement(sql);
    		preStm.setLong(1, projectInfo.getProjectId());
    		rs = preStm.executeQuery();

			if (rs == null){
				return true;
			}
			while (rs.next()){
				lMilestoneID = rs.getLong("MILESTONE_ID");
			}
			if (lMilestoneID != 0){
				sql = "UPDATE MILESTONE SET ACTUAL_FINISH_DATE = ? , COMPLETE = 1" +
					  " WHERE MILESTONE_ID = ? AND PROJECT_ID = ?";
				preStm = conn.prepareStatement(sql);
				preStm.setDate(1, projectInfo.getActualFinishDate());
				preStm.setLong(2, lMilestoneID);
				preStm.setLong(3, projectInfo.getProjectId());
				preStm.executeUpdate();
			}

			conn.commit();
    		conn.setAutoCommit(true);
    		return true;
    	}
    	catch (SQLException ex){
    		ex.printStackTrace();
    		conn.rollback();
    		return false;
    	}
    	finally{
    		ServerHelper.closeConnection(conn, preStm, rs);
    	}
    }
}
