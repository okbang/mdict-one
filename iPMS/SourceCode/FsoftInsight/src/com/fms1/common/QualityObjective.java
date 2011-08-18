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
import java.util.Vector;
import com.fms1.infoclass.*;
import com.fms1.tools.CommonTools;
import com.fms1.tools.ConvertString;
import com.fms1.tools.Db;
import com.fms1.web.*;
/**
 * Other quality activities logic
 *
 */
public final class QualityObjective {

	/********************************OtherActivities*******************************************/
	/**
	 * @return List of OtherActInfo
	 */
	
	public static final int NUMBER_OF_ROW_ADDABLE = 10;
	public static final Vector getOtherActivityList(final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs=null;
		Vector vt = new Vector();
		Vector stages = Schedule.getStageList(prjID);
		StageInfo stinf;
		OtherActInfo oaInfo;
		for (int i = 0; i < stages.size(); i++) {
			stinf = (StageInfo)stages.elementAt(i);
			oaInfo = new OtherActInfo();
			oaInfo.otherActID = stinf.milestoneID;
			oaInfo.prjID = prjID;
			oaInfo.pStartD =stinf.plannedBeginDate;
			oaInfo.pEndD = stinf.plannedEndDate;
			oaInfo.aEndD = stinf.aEndD;
			oaInfo.conductor = stinf.QGateConductor;
			oaInfo.conductorName = stinf.QGateConductorName;
			oaInfo.note = stinf.milestone;
			oaInfo.activity = "Quality Gate: "+stinf.stage;
			oaInfo.qcActivity = QCActivityInfo.QUALITY_GATE_INSPECTION;
			oaInfo.standartStage = stinf.StandardStage;
			oaInfo.status = (int)((stinf.aEndD == null) ? TaskInfo.STATUS_PENDING : TaskInfo.STATUS_PASS);
			vt.add(oaInfo);
		}
		try {
			sql =
				"SELECT OTHER_ACTIVITY.*,DEVELOPER.ACCOUNT FROM OTHER_ACTIVITY,DEVELOPER WHERE OTHER_ACTIVITY.PROJECT_PLAN_ID= ? AND DEVELOPER.DEVELOPER_ID=CONDUCTOR ORDER BY PLANNED_END_DATE";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1,prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				oaInfo = new OtherActInfo();
				oaInfo.otherActID = rs.getLong("OTHER_ACTIVITY_ID");
				oaInfo.prjID = rs.getLong("PROJECT_PLAN_ID");
				oaInfo.pStartD = rs.getDate("PLANNED_START_DATE");
				oaInfo.pEndD = rs.getDate("PLANNED_END_DATE");
				oaInfo.aEndD = rs.getDate("ACTUAL_END_DATE");
				oaInfo.conductor = rs.getLong("CONDUCTOR");
				oaInfo.conductorName=rs.getString("ACCOUNT");
				oaInfo.note = rs.getString("NOTE");
				oaInfo.activity = rs.getString("ACTIVITY");
				oaInfo.qcActivity=rs.getInt("TYPE");
				oaInfo.metric=Db.getDouble(rs,"METRIC");
				oaInfo.status=rs.getInt("STATUS");
				// Modified by HaiMM
				oaInfo.risk_id = rs.getLong("risk_id");
				oaInfo.risk_Miti_Contin_ID = rs.getInt("risk_miti_contin_id");
				oaInfo.risk_type = rs.getInt("risk_type");
				
				vt.add(oaInfo);
			}
			CommonTools.sortVector(vt,"pEndD");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
			return vt;
		}
	}
	/**
	 * @return List of OtherActInfo
	 */
	public static final Vector getOtherActivityList(Date startDate,Date endDate,int [] types) {				
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		Vector vt = new Vector();
		OtherActInfo oaInfo;
		String fromConstraint = (startDate == null) ? "" : " AND NVL(milestone.PLAN_FINISH_DATE, milestone.BASE_FINISH_DATE) >= ? ";		
		try {
			conn = ServerHelper.instance().getConnection();
			for (int i = 0; i < types.length; i++) {
                if (types[i] == QCActivityInfo.QUALITY_GATE_INSPECTION) {
                    sql =
                        "SELECT milestone_id, milestone.project_id, milestone.name,milestone.standardstage,"
                            + " milestone.actual_finish_date,PROJECT.actual_finish_date PENDDATE, "
                            + " milestone.description, milestone, NVL ( milestone.PLAN_FINISH_DATE, milestone.BASE_FINISH_DATE) PLANNED_FINISH_DATE, "
                            + " milestone.COMMENTS,PROJECT.code,milestone.CONDUCTOR,DEVELOPER.ACCOUNT "
                            + " FROM milestone,PROJECT,DEVELOPER "
                            + " WHERE milestone.project_id= PROJECT.project_id "
                            + " AND DEVELOPER.DEVELOPER_ID (+)=MILESTONE.CONDUCTOR"
                            + " AND PROJECT.STATUS <>"
                            + ProjectInfo.STATUS_CANCELLED
                            + " AND NVL(milestone.PLAN_FINISH_DATE, milestone.BASE_FINISH_DATE) <= ? "
                            + fromConstraint
                            + " ORDER BY PLANNED_FINISH_DATE ASC";
                    stm = conn.prepareStatement(sql);           
                    stm.setDate(1, endDate);
                    if (startDate != null) {
                        stm.setDate(2, startDate);
                    }
                    rs = stm.executeQuery();
                    while (rs.next()) {
                        oaInfo = new OtherActInfo();
                        oaInfo.otherActID = rs.getLong("milestone_id");
                        oaInfo.prjID = rs.getLong("project_id");
                        oaInfo.pEndD = rs.getDate("PLANNED_FINISH_DATE");
                        //if termination use the actual end date of project instead
                        oaInfo.aEndD = (rs.getInt("STANDARDSTAGE") == StageInfo.STAGE_TERMINATION) ? rs.getDate("PENDDATE") : rs.getDate("actual_finish_date");
                        oaInfo.conductor = rs.getLong("CONDUCTOR");
                        oaInfo.conductorName = rs.getString("ACCOUNT");
                        oaInfo.note = rs.getString("milestone");
                        oaInfo.activity = "Quality Gate : " + rs.getString("name");
                        oaInfo.qcActivity = QCActivityInfo.QUALITY_GATE_INSPECTION;
                        oaInfo.status = (int) ((oaInfo.aEndD == null) ? TaskInfo.STATUS_PENDING : TaskInfo.STATUS_PASS);
                        vt.add(oaInfo);
                    }
                    rs.close();
                    stm.close();
                    break;
                }
			}
            
			fromConstraint = (startDate == null) ? "" : " AND OTHER_ACTIVITY.PLANNED_END_DATE >=? ";
			
			sql = 
                "SELECT OTHER_ACTIVITY.*, DEVELOPER.ACCOUNT FROM OTHER_ACTIVITY, PROJECT, DEVELOPER"
        			+ " WHERE OTHER_ACTIVITY.TYPE in(" + ConvertString.arrayToString(types,",")
        			+ " ) AND OTHER_ACTIVITY.PROJECT_PLAN_ID= PROJECT.project_id"
        			+ " AND PROJECT.STATUS <> " + ProjectInfo.STATUS_CANCELLED
        			+ " AND OTHER_ACTIVITY.PLANNED_END_DATE <= ? " + fromConstraint
        			+ " AND OTHER_ACTIVITY.STATUS <> " + (int)TaskInfo.STATUS_CANCELLED
        			+ " AND DEVELOPER.DEVELOPER_ID = OTHER_ACTIVITY.CONDUCTOR"
        			+ " ORDER BY OTHER_ACTIVITY.PLANNED_END_DATE";
			stm = conn.prepareStatement(sql);
			stm.setDate(1, endDate);
			if (startDate != null) {
                stm.setDate(2, startDate);
            }
			rs = stm.executeQuery();
			while (rs.next()) {
				oaInfo = new OtherActInfo();
				oaInfo.otherActID = rs.getLong("OTHER_ACTIVITY_ID");
				oaInfo.prjID = rs.getLong("PROJECT_PLAN_ID");
				oaInfo.pStartD = rs.getDate("PLANNED_START_DATE");
				oaInfo.pEndD = rs.getDate("PLANNED_END_DATE");
				oaInfo.aEndD = rs.getDate("ACTUAL_END_DATE");
				oaInfo.conductor = rs.getLong("CONDUCTOR");
				oaInfo.conductorName = rs.getString("ACCOUNT");
				oaInfo.note = rs.getString("NOTE");
				oaInfo.activity = rs.getString("ACTIVITY");
				oaInfo.qcActivity = rs.getInt("TYPE");
				oaInfo.metric = Db.getDouble(rs, "METRIC");
				oaInfo.status = rs.getInt("STATUS");
				vt.add(oaInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
			return vt;
		}
	}
    /**
	 * @return true if add successfully
	 * @param  OtherActivity infomation
	 *
	 */
	public static final boolean addOtherActivity(final OtherActInfo oaInfo) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String insertStatement = null;
		boolean bl = true;
		try {
			conn = ServerHelper.instance().getConnection();
			insertStatement =
				" INSERT INTO OTHER_ACTIVITY ("
					+ " OTHER_ACTIVITY_ID,"
					+ " PROJECT_PLAN_ID,"
					+ " ACTIVITY,"
					+ " PLANNED_START_DATE,"
					+ " PLANNED_END_DATE,"
					+ " ACTUAL_END_DATE,"
					+ " CONDUCTOR,"
					+ " TYPE,"
					+ " NOTE,"
					+ " STATUS,"
					+ " METRIC,"
					+ " RISK_ID,"
					+ " risk_miti_contin_id,"
					+ " risk_type)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			prepStmt = conn.prepareStatement(insertStatement);
            final long id = ServerHelper.getNextSeq("OTHER_ACTIVITY_SEQ");
			prepStmt.setLong(1, id);
			prepStmt.setLong(2, oaInfo.prjID);
			prepStmt.setString(3, oaInfo.activity);
			prepStmt.setDate(4, oaInfo.pStartD);
			prepStmt.setDate(5, oaInfo.pEndD);
			prepStmt.setDate(6, oaInfo.aEndD);
			prepStmt.setLong(7, oaInfo.conductor);
			prepStmt.setInt(8, oaInfo.qcActivity);
			prepStmt.setString(9, oaInfo.note);
			prepStmt.setInt(10, oaInfo.status);
			Db.setDouble(prepStmt,11,oaInfo.metric);
			prepStmt.setLong(12, oaInfo.risk_id);
			prepStmt.setLong(13, oaInfo.risk_Miti_Contin_ID);
			prepStmt.setInt(14, oaInfo.risk_type);
			if (prepStmt.executeUpdate() == 0) {
				bl = false;
			}
			prepStmt.close();
            final long idE = ServerHelper.getNextSeq("QUALITY_ACTIVITY_EFFORT_SEQ");
			insertStatement =
				" INSERT INTO QUALITY_ACTIVITY_EFFORT ("
					+ " QUALITY_ACTIVITY_EFFORT_ID,"
					+ " PROJECT_ID,"
					+ " ACTIVITY_ID,"
					+ " TYPE)"
					+ " VALUES (?,?,?,?)";
			prepStmt = conn.prepareStatement(insertStatement);
			prepStmt.setLong(1, idE);
			prepStmt.setLong(2, oaInfo.prjID);
			prepStmt.setLong(3, id);
			prepStmt.setInt(4, 1);
			if (prepStmt.executeUpdate() == 0) {
				bl = false;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,prepStmt,null);	
			return bl;
		}
	}
	
	public static final boolean updateToRisk(final OtherActInfo oaInfo, final String account, final int status) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String updateStatement = null;
		try {
			conn = ServerHelper.instance().getConnection();
			// Modified by HaiMM -- Start
			if (oaInfo.risk_type == 1) {
				updateStatement = " UPDATE RISK_MITIGATION"
										+ " SET PLAN_END_DATE = ?, ACTUAL_END_DATE = ?,"
										+ " ACTION_STATUS = ?, DEVELOPER_ACC = ?"
										+ " WHERE RISK_MITIGATION_ID = ?";
				prepStmt = conn.prepareStatement(updateStatement);
				prepStmt.setDate(1, oaInfo.pEndD);
				prepStmt.setDate(2, oaInfo.aEndD);
				prepStmt.setInt(3, status);
				prepStmt.setString(4, account);
				prepStmt.setLong(5, oaInfo.risk_Miti_Contin_ID);

			} else if(oaInfo.risk_type == 2) {
				updateStatement = " UPDATE RISK_CONTIGENCY"
										+ " SET PLAN_END_DATE = ?, ACTUAL_END_DATE = ?,"
										+ " ACTION_STATUS = ?, DEVELOPER_ACC = ?"
										+ " WHERE RISK_CONTIGENCY_ID = ?";
				prepStmt = conn.prepareStatement(updateStatement);
				prepStmt.setDate(1, oaInfo.pEndD);
				prepStmt.setDate(2, oaInfo.aEndD);
				prepStmt.setInt(3, status);
				prepStmt.setString(4, account);
				prepStmt.setLong(5, oaInfo.risk_Miti_Contin_ID);
				
			}
			return (prepStmt.executeUpdate() > 0);
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
	 * @return true if update successfully
	 * @param  OtherActivity infomation
	 *
	 */
	public static final boolean uppdateOtherActivity(final OtherActInfo oaInfo) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String updateStatement = null;
		try {
			conn = ServerHelper.instance().getConnection();
			if (oaInfo.qcActivity == QCActivityInfo.QUALITY_GATE_INSPECTION) {
				updateStatement =
					" UPDATE MILESTONE"
						+ " SET ACTUAL_FINISH_DATE = ?,"
						+ " CONDUCTOR = ?"
						+ " WHERE MILESTONE_ID = ?";
				prepStmt = conn.prepareStatement(updateStatement);
				prepStmt.setDate(1, oaInfo.aEndD);
				prepStmt.setLong(2, oaInfo.conductor);
				prepStmt.setLong(3, oaInfo.otherActID);
			}
			else {
				updateStatement =
					" UPDATE OTHER_ACTIVITY "
						+ " SET ACTIVITY = ?,"
						+ " PLANNED_START_DATE = ?,"
						+ " PLANNED_END_DATE = ?,"
						+ " ACTUAL_END_DATE = ?,"
						+ " CONDUCTOR = ?,"
						+ " TYPE = ?,"
						+ " NOTE = ?, "
						+ " METRIC = ?, "
						+ " STATUS = ? "
						+ " WHERE OTHER_ACTIVITY_ID = ?";
				prepStmt = conn.prepareStatement(updateStatement);
				prepStmt.setString(1, oaInfo.activity);
				prepStmt.setDate(2, oaInfo.pStartD);
				prepStmt.setDate(3, oaInfo.pEndD);
				prepStmt.setDate(4, oaInfo.aEndD);
				prepStmt.setLong(5, oaInfo.conductor);
				prepStmt.setInt(6, oaInfo.qcActivity);
				prepStmt.setString(7, oaInfo.note);
				Db.setDouble(prepStmt,8, oaInfo.metric);
				prepStmt.setInt(9, oaInfo.status);
				prepStmt.setLong(10, oaInfo.otherActID);
			}
			return (prepStmt.executeUpdate() > 0);
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
	 * @return true if delete successfully
	 * @param  OtherActivity_ID
	 *
	 */
	public static final boolean deleteOtherActivity(final long oaID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "DELETE OTHER_ACTIVITY WHERE OTHER_ACTIVITY_ID=" + oaID;
			stm.executeQuery(sql);
			sql = "DELETE QUALITY_ACTIVITY_EFFORT WHERE ACTIVITY_ID=" + oaID + " AND TYPE = 1";
			stm.executeUpdate(sql);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("sql stmt = " + sql);
		}
		finally {
			ServerHelper.closeConnection(conn,stm,null);	
		}
		return false;
	}
	/**
	 * Get one QualityObjective for a project
	 * @Param: Project_ID
	 *
	 */
	public static final String getQualityObjective(final long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs=null;
		String retVal=null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT QUALITY_OBJECTIVE_STRATEGY FROM PROJECT_PLAN WHERE PROJECT_ID=" + prjID;
			rs = stm.executeQuery(sql);
			if (rs.next())
				retVal=Db.getClob(rs,"QUALITY_OBJECTIVE_STRATEGY");
			return (retVal== null)?"":retVal;
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
		}
	}
	
    /* 16-Jan-08: replaced by writeClob() function because Db.setCLOB matched
     *  issues when working with LOB data
     * 
	 * Update QualityObjective for a project plan
	 * @Param: qualityObjective,Project_ID
	 *
	public static final boolean updateQualityObjective(final long prjID, final String qlt) {
		Connection conn = null;
		PreparedStatement prepStm = null;
		String sql = null;
		try {
			conn = Db.getOracleConn();
			sql =
				"UPDATE PROJECT_PLAN SET QUALITY_OBJECTIVE_STRATEGY= ?"
					+ " WHERE PROJECT_ID= ?";
			prepStm=conn.prepareStatement(sql);
			Db.setCLOB(prepStm,1,qlt);
			prepStm.setLong(2,prjID);
			return (prepStm.executeUpdate()>0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn,prepStm,null);
		}
	}
     */

    /**
     * 16-Jan-08: replaced old function by this new function to avoid LOB issues
     * Update Quality objective of project
     * @param prjID
     * @param qlt
     * @return
     */
    public static final boolean updateQualityObjective(
        final long prjID, final String qlt)
    {
        return Db.writeClob("PROJECT_PLAN", "QUALITY_OBJECTIVE_STRATEGY",
            "PROJECT_ID", Long.toString(prjID), qlt);
    }
    
	public static int getQualityObjectiveCount(Date fromDate,Date toDate,int type) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs=null;
		int retVal=0;
		try {
			conn = ServerHelper.instance().getConnection();
			
			sql = "SELECT count(*) FROM OTHER_ACTIVITY,PROJECT"
				+ " WHERE OTHER_ACTIVITY.STATUS <>"+TaskInfo.STATUS_CANCELLED
				+ " AND OTHER_ACTIVITY.TYPE=? AND OTHER_ACTIVITY.ACTUAL_END_DATE <= ? AND OTHER_ACTIVITY.ACTUAL_END_DATE >= ?"
				+ " AND PROJECT.PROJECT_id=OTHER_ACTIVITY.PROJECT_PLAN_ID AND PROJECT.STATUS<>"
				+ ProjectInfo.STATUS_CANCELLED;
			stm = conn.prepareStatement(sql);
			stm.setInt(1, type);
			stm.setDate(2, toDate);
			stm.setDate(3, fromDate);
			rs = stm.executeQuery();
			if (rs.next())
				retVal = rs.getInt(1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
			return retVal;
		}
	}
	public static double getAvgMetric(Date fromDate,Date toDate,int type) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		double retVal = Double.NaN;
		try {
			sql = 
                "SELECT AVG(METRIC) FROM OTHER_ACTIVITY WHERE STATUS <> "
                    + TaskInfo.STATUS_CANCELLED
                    + " AND TYPE = ? AND ACTUAL_END_DATE <= ? AND ACTUAL_END_DATE >= ?";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setInt(1, type);
			stm.setDate(2, toDate);
			stm.setDate(3, fromDate);
			rs = stm.executeQuery();
			if (rs.next()) {
                retVal=Db.getDouble(rs,1);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,rs);
			return retVal;
		}
	}
	
	public static final Vector getReviewStrategyList(final long prjID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {			
			conn = ServerHelper.instance().getConnection();			
			//get the list of strategy of meeting quality objectives
			
			sql = "SELECT  A.REV_ID," 
					   	+ "A.REV_ITEM,"
					   	+ "A.REV_TYPE,"
					   	+ "A.REV_REVIEWER,"
						+ "A.REV_WHEN"
				 +" FROM REVIEW_STRATEGY A" 
				 +" WHERE A.REV_PRJ_ID = ?"
				 +" ORDER BY A.REV_ID DESC";
				
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			ReviewStrategyInfo info ;
			while (rs.next()) {
				info = new ReviewStrategyInfo();
				info.revID=rs.getLong("REV_ID");
				info.revItem = rs.getString("REV_ITEM");
				info.revType = rs.getString("REV_TYPE");
				info.revReviewer = rs.getString("REV_REVIEWER");
				info.revWhen = rs.getString("REV_WHEN");
				list.addElement(info);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return list;
		}
	}
	
	public static final int doAddPLReviewStrategy(final ReviewStrategyInfo revInfo, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;
		int iCount = 0;
	
		try {
			conn = ServerHelper.instance().getConnection();
		
			sql =  " INSERT INTO REVIEW_STRATEGY("
					+ "REV_ID, "
					+ "REV_PRJ_ID,"
					+ "REV_ITEM, "				    
					+ "REV_TYPE, "
					+ "REV_REVIEWER, "
					+ "REV_WHEN)"
				 + " VALUES(NVL((SELECT MAX(REV_ID)+1 FROM REVIEW_STRATEGY),1),"+ prjID+",?,?,?,?)";
			stm = conn.prepareStatement(sql);
			stm.setString(1, revInfo.revItem);
			stm.setString(2, revInfo.revType);
			stm.setString(3, revInfo.revReviewer);
			stm.setString(4, revInfo.revWhen);
			stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final int doUpdatePLReviewStrategy(Vector vReviewStratList, final long prjID) {		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;		
		ReviewStrategyInfo revInfo = null;
		int nRev = vReviewStratList.size();
		int iRet = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < nRev; i++) {
				revInfo = (ReviewStrategyInfo) vReviewStratList.elementAt(i);
							
				sql =  " UPDATE REVIEW_STRATEGY R "
						+ "SET R.REV_PRJ_ID = ?"
						+ "  , R.REV_ITEM = ?"
						+ "  , R.REV_TYPE = ?"
						+ "  , R.REV_REVIEWER = ?"
						+ "  , R.REV_WHEN = ?"
					 + " WHERE R.REV_ID = ?";
				stm = conn.prepareStatement(sql);
				stm.setLong(1, prjID);
				stm.setString(2, revInfo.revItem);				
				stm.setString(3, revInfo.revType);	
				stm.setString(4, revInfo.revReviewer);
				stm.setString(5, revInfo.revWhen);			
				stm.setLong(6, revInfo.revID);
				
				stm.executeUpdate();
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			try {
				conn.rollback();
				conn.setAutoCommit(true);
			} catch (SQLException ex) {			
				ex.printStackTrace();
			}
			finally {
				e.printStackTrace();
				iRet = 1;
			}
		} finally {
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final boolean doDeletePLReviewStrategy(final long revID) {
			return Db.delete(revID, "REV_ID", "REVIEW_STRATEGY");
	}
	
	
	public static final Vector getStrategyOfMeetingList(final long prjID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		final Vector list = new Vector();
		ResultSet rs = null;
		try {			
			conn = ServerHelper.instance().getConnection();			
			//get the list of strategy of meeting quality objectives
			
			sql = "SELECT A.QO_STRAT_ID," 
					   + "A.QO_STRAT_DESC,"
					   + "A.QO_STRAT_EXPECTED_BENEFITS"
				 +" FROM QO_STRATEGY_FOR_MEETING A" 
				 +" WHERE A.QO_STRAT_PRJ_ID = ?"
				 +" ORDER BY A.QO_STRAT_ID DESC";
				
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			StrategyOfMeetingInfo info ;
			while (rs.next()) {
				info = new StrategyOfMeetingInfo();
				info.stratID=rs.getLong("QO_STRAT_ID");
				info.stratDesc = rs.getString("QO_STRAT_DESC");
				info.stratExBene = rs.getString("QO_STRAT_EXPECTED_BENEFITS");				
				list.addElement(info);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return list;
		}
	}
	
	public static final int doAddPLStrategyForMeeting(final StrategyOfMeetingInfo stratInfo, final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;
		int iCount = 0;
	
		try {
			conn = ServerHelper.instance().getConnection();
		
			sql =  " INSERT INTO QO_STRATEGY_FOR_MEETING("
					+ "QO_STRAT_ID, "
					+ "QO_STRAT_PRJ_ID,"
					+ "QO_STRAT_DESC, "				    
					+ "QO_STRAT_EXPECTED_BENEFITS)"
				 + " VALUES(NVL((SELECT MAX(QO_STRAT_ID)+1 FROM QO_STRATEGY_FOR_MEETING),1),"+ prjID+",?,?)";
			stm = conn.prepareStatement(sql);				
			stm.setString(1, stratInfo.stratDesc);				
			stm.setString(2, stratInfo.stratExBene);
			stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final int doUpdatePLStrategyForMeeting(Vector vStratList, final long prjID) {		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;		
		StrategyOfMeetingInfo stratInfo = null;
		int nStrat = vStratList.size();
		int iRet = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			for (int i = 0; i < nStrat; i++) {
				stratInfo = (StrategyOfMeetingInfo) vStratList.elementAt(i);
							
				sql =  " UPDATE QO_STRATEGY_FOR_MEETING P "
						+ "SET P.QO_STRAT_DESC = ?"
						+ "  , P.QO_STRAT_EXPECTED_BENEFITS = ?"
					 + " WHERE P.QO_STRAT_ID = ?";
				stm = conn.prepareStatement(sql);				
				stm.setString(1, stratInfo.stratDesc);				
				stm.setString(2, stratInfo.stratExBene);				
				stm.setLong(3, stratInfo.stratID);
				
				stm.executeUpdate();
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			try {
				conn.rollback();
				conn.setAutoCommit(true);
			} catch (SQLException ex) {			
				ex.printStackTrace();
			}
			finally {
				e.printStackTrace();
				iRet = 1;
			}
		} finally {
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final boolean doDeletePLStrategyForMeeting(final long stratID) {
			return Db.delete(stratID, "QO_STRAT_ID", "QO_STRATEGY_FOR_MEETING");
	}
}