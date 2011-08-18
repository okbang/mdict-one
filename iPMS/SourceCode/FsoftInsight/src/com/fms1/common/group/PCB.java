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
 
 package com.fms1.common.group;
import java.sql.*;
import java.util.Vector;
import com.fms1.tools.*;
import com.fms1.common.*;
import com.fms1.infoclass.group.*;
import com.fms1.infoclass.*;
import com.fms1.web.*;
/**
 * Business logic of PCB report
 * @author manu
 * Created on Jul 17, 2003
 */
public class PCB {
	public final static int REPORT_DEVELOPMENT = ProjectInfo.LIFECYCLE_DEVELOPMENT;
	public final static int REPORT_MAINTENANCE = ProjectInfo.LIFECYCLE_MAINTENANCE;
	public final static int REPORT_OTHER = ProjectInfo.LIFECYCLE_OTHER;
	public final static int REPORT_ALL = 6;
	public final static int REPORT_CUSTOM = 7;
	public static final Vector getPCBs(long workUnitID) {
		PreparedStatement prepStmt = null;
		String sql = null;
		Connection conn = null;
		ResultSet rs = null;
		Vector result = new Vector();
		try {
			sql =
				" SELECT PCB_ID, WORKUNIT, FROMDATE, TODATE, REPORT_DATE, PCB.DEVELOPER_ID,"
					+ " REPORTNAME, REPORTTYPE, METHODOLOGY, GENERALCOMM,"
					+ " LASTPROBLEMREVIEW, LASTSUGGREVIEW, PROBLEMS, SUGGESTIONS,PERIOD,YEAR,DEVELOPER.NAME DEVNAME"
					+ " FROM PCB,DEVELOPER "
					+ " WHERE WORKUNIT=? AND PCB.DEVELOPER_ID=DEVELOPER.DEVELOPER_ID ORDER BY TODATE DESC,REPORTTYPE,PERIOD";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, workUnitID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				PCBGalInfo pcbInfo = new PCBGalInfo();
				pcbInfo.pcbID = rs.getLong("PCB_ID");
				pcbInfo.workUnitID = workUnitID;
				pcbInfo.startDate = rs.getDate("FROMDATE");
				pcbInfo.endDate = rs.getDate("TODATE");
				pcbInfo.reportDate = rs.getDate("REPORT_DATE");
				pcbInfo.authorID = rs.getLong("DEVELOPER_ID");
				pcbInfo.author = rs.getString("DEVNAME");
				pcbInfo.methodology = Db.getClob(rs, "METHODOLOGY");
				pcbInfo.galComment = Db.getClob(rs, "GENERALCOMM");
				pcbInfo.lastProblemsReview = Db.getClob(rs, "LASTPROBLEMREVIEW");
				pcbInfo.lastSuggestionsReview = Db.getClob(rs, "LASTSUGGREVIEW");
				pcbInfo.problems = Db.getClob(rs, "PROBLEMS");
				pcbInfo.suggestions = Db.getClob(rs, "SUGGESTIONS");
				pcbInfo.reportName = rs.getString("REPORTNAME");
				pcbInfo.setReportType(rs.getInt("REPORTTYPE"));
				pcbInfo.period = rs.getString("PERIOD");
				pcbInfo.year = rs.getInt("YEAR");
				//report type = lifecycle if <3
				pcbInfo.lifecycleID=(pcbInfo.reportType>2)?ProjectInfo.LIFECYCLE_DEVELOPMENT:pcbInfo.reportType;
				result.add(pcbInfo);
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
	public static final long createPCB(PCBGalInfo pcbInfo) {
		PreparedStatement prepStmt = null;
		String sql = null;
		Connection conn = null;
		ResultSet rs = null;
		long pcbID = 0;
		try {
			sql = " SELECT NVL((SELECT MAX(PCB_ID)+1 FROM PCB),1) FROM DUAL";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			rs.next();
			pcbID = rs.getLong(1);
			rs.close();
			prepStmt.close();
			sql =
				"INSERT INTO PCB"
					+ " ( WORKUNIT, FROMDATE, TODATE, REPORT_DATE, DEVELOPER_ID,"
					+ " REPORTNAME, REPORTTYPE,PCB_ID,PERIOD,YEAR)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, pcbInfo.workUnitID);
			prepStmt.setDate(2, pcbInfo.startDate);
			prepStmt.setDate(3, pcbInfo.endDate);
			prepStmt.setDate(4, pcbInfo.reportDate);
			prepStmt.setLong(5, pcbInfo.authorID);
			prepStmt.setString(6, pcbInfo.reportName);
			prepStmt.setInt(7, pcbInfo.reportType);
			prepStmt.setLong(8, pcbID);
			prepStmt.setString(9, pcbInfo.period);
			prepStmt.setInt(10, pcbInfo.year);
			prepStmt.executeUpdate();
			//save metric selection
			prepStmt.close();
			sql = "INSERT INTO PCBMGROUP (PCB_ID, MGROUP_ID) VALUES (?,?)";
			prepStmt = conn.prepareStatement(sql);
			for (int i = 0; i < pcbInfo.metricIDs.length; i++) {
				prepStmt.setLong(1, pcbID);
				prepStmt.setLong(2, pcbInfo.metricIDs[i]);
				prepStmt.executeUpdate();
			}
			if (pcbInfo.reportType == PCB.REPORT_CUSTOM) {
				//save project list
				prepStmt.close();
				sql = "INSERT INTO PCBPROJECT (PCB_ID, PROJECT_ID) VALUES (?,?)";
				prepStmt = conn.prepareStatement(sql);
				for (int i = 0; i < pcbInfo.projectIDs.length; i++) {
					prepStmt.setLong(1, pcbID);
					prepStmt.setLong(2, pcbInfo.projectIDs[i]);
					prepStmt.executeUpdate();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return pcbID;
		}
	}
	public static final void updatePCB(PCBGalInfo pcbInfo) {
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		String sql = null;
		Connection conn = null;
		try {
			sql =
				"UPDATE PCB SET"
					+ " METHODOLOGY =?,"
					+ " GENERALCOMM=?,"
					+ " LASTPROBLEMREVIEW=?,"
			 		+ " LASTSUGGREVIEW=?,"
					+ " PROBLEMS=?,"
					+ " SUGGESTIONS=?"
					+ " WHERE PCB_ID=?";
			conn = Db.getOracleConn();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setClob(1, Db.createOracleCLOB(conn,pcbInfo.methodology));
			prepStmt.setClob(2, Db.createOracleCLOB(conn,pcbInfo.galComment));
			prepStmt.setClob(3, Db.createOracleCLOB(conn,pcbInfo.lastProblemsReview));
			prepStmt.setClob(4, Db.createOracleCLOB(conn,pcbInfo.lastSuggestionsReview));
			prepStmt.setClob(5, Db.createOracleCLOB(conn,pcbInfo.problems));
			prepStmt.setClob(6, Db.createOracleCLOB(conn,pcbInfo.suggestions));
			prepStmt.setLong(7, pcbInfo.pcbID);
			prepStmt.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	public static final Vector getPCBMetricList(PCBGalInfo pcbInfo) {
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		Connection conn = null;
		Vector metricList = new Vector();
		try {
			//metric analysis
			sql = "SELECT METRIC_ID,ANALYSIS, SUGGESTION FROM PCBMETRIC  WHERE PCB_ID=? ORDER BY METRIC_ID";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, pcbInfo.pcbID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				PCBMetricInfo metricInfo = new PCBMetricInfo();
				metricInfo.metricConstant=rs.getInt("METRIC_ID");
				metricInfo.metricID = Metrics.getMetricID(metricInfo.metricConstant);
				metricInfo.analysis = rs.getString("ANALYSIS");
				metricInfo.suggestion = rs.getString("SUGGESTION");
				metricList.add(metricInfo);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return metricList;
		}
	}
	public static final int[] loadPCBMetricGroups(long pcbID) {
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		Connection conn = null;
		int arraySize = 0;
		int[] metricIDs = null;
		;
		try {
			//metric analysis
			sql =
				"SELECT COUNT(*) a,1 FROM PCBMGROUP WHERE PCB_ID=? UNION SELECT MGROUP_ID,2 FROM PCBMGROUP WHERE PCB_ID=?  ORDER BY 2";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, pcbID);
			prepStmt.setLong(2, pcbID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				//trick to get n rows in order to dim the array
				arraySize = rs.getInt(1);
				if (arraySize == 0)
					return null;
				metricIDs = new int[arraySize];
			}
			else
				return null;
			int arrIndex = 0;
			while (rs.next()) {
				metricIDs[arrIndex++] = rs.getInt(1);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return metricIDs;
		}
	}
	//Metric Analysis by project, updates the pcbMetrics object
	public static final void loadProjectComments(PCBGalInfo pcbInfo, Vector pcbMetrics) {
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		Connection conn = null;
		String[] anas;
		int[] metricIDs;
		long[] projectIDs;
		int arraySize = 0;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT ' ' ANALYSIS,null METRIC_ID, COUNT(*) PROJECT_ID,1 FROM PCBMETRICPROJECT WHERE PCB_ID=?"
					+ " UNION  SELECT ANALYSIS,METRIC_ID,PROJECT_ID,2 FROM PCBMETRICPROJECT WHERE PCB_ID=? ORDER BY 4";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, pcbInfo.pcbID);
			prepStmt.setLong(2, pcbInfo.pcbID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				//a small trick to get n rows in order to use arrays instead of vectors
				arraySize = rs.getInt("PROJECT_ID");
				if (arraySize == 0)
					return;
				anas = new String[arraySize];
				metricIDs = new int[arraySize];
				projectIDs = new long[arraySize];
			}
			else
				return;
			int arrIndex = 0;
			while (rs.next()) {
				anas[arrIndex] = rs.getString("ANALYSIS");
				metricIDs[arrIndex] = rs.getInt("METRIC_ID");
				projectIDs[arrIndex] = rs.getLong("PROJECT_ID");
				arrIndex++;
			}
			//now update the pcbMetrics with the comments
			for (int i = 0; i < pcbMetrics.size(); i++) {
				PCBMetricInfo metricInfo = (PCBMetricInfo) pcbMetrics.elementAt(i);
				for (int k = 0; k < pcbInfo.projectIDs.length; k++) {
					for (int j = 0; j < arraySize; j++) {
						if ((projectIDs[j] == pcbInfo.projectIDs[k]) && (metricInfo.metricConstant==metricIDs[j])) {
							metricInfo.projectComments[k] = anas[j];
							break;
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	public static final Vector getCustomProjectIDs(long pcbID) {
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		Connection conn = null;
		Vector result = new Vector();
		try {
			sql = "SELECT PROJECT_ID FROM PCBPROJECT WHERE PCB_ID=?";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, pcbID);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				Long myLong = new Long(rs.getLong("PROJECT_ID"));
				result.add(myLong);
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
	public static final void deletePCB(long pcbID) {
		PreparedStatement prepStmt = null;
		String sql = null;
		Connection conn = null;
		try {
			sql = "DELETE FROM PCB WHERE PCB_ID=?";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, pcbID);
			prepStmt.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, null);
		}
	}
	public static final void updatePCBMetrics(PCBGalInfo pcbInfo, Vector metricList) {
		PreparedStatement prepStmtUpd = null, prepStmtIns = null;
		String sqlUpd = null;
		String sqlIns = null;
		Connection conn = null;
		try {
			//metric analysis
			sqlUpd = "UPDATE PCBMETRIC SET ANALYSIS =?, SUGGESTION=? WHERE PCB_ID=? AND METRIC_ID=?";
			sqlIns = "INSERT INTO PCBMETRIC (PCB_ID,METRIC_ID,ANALYSIS,SUGGESTION) VALUES (?,?,?,?)";
			conn = ServerHelper.instance().getConnection();
			prepStmtUpd = conn.prepareStatement(sqlUpd);
			prepStmtIns = conn.prepareStatement(sqlIns);
			for (int i = 0; i < metricList.size(); i++) {
				PCBMetricInfo metricInfo = (PCBMetricInfo) metricList.elementAt(i);
				if (!metricInfo.isMetricGroup) {
					//first try to update
					prepStmtUpd.setString(1, metricInfo.analysis);
					prepStmtUpd.setString(2, metricInfo.suggestion);
					prepStmtUpd.setLong(3, pcbInfo.pcbID);
					prepStmtUpd.setInt(4, metricInfo.metricConstant);
					if (prepStmtUpd.executeUpdate() == 0) {
						//update failed, do insert
						prepStmtIns.setLong(1, pcbInfo.pcbID);
						prepStmtIns.setInt(2, metricInfo.metricConstant);
						prepStmtIns.setString(3, metricInfo.analysis);
						prepStmtIns.setString(4, metricInfo.suggestion);
						prepStmtIns.executeUpdate();
					}
				}
			}
			prepStmtUpd.close();
			prepStmtIns.close();
			//Metric Analysis by project
			sqlUpd = "UPDATE PCBMETRICPROJECT SET ANALYSIS =? WHERE PCB_ID=? AND METRIC_ID=? AND PROJECT_ID=?";
			sqlIns = "INSERT INTO PCBMETRICPROJECT (PCB_ID,METRIC_ID,PROJECT_ID,ANALYSIS) VALUES (?,?,?,?)";
			prepStmtUpd = conn.prepareStatement(sqlUpd);
			prepStmtIns = conn.prepareStatement(sqlIns);
			for (int i = 0; i < metricList.size(); i++) {
				PCBMetricInfo metricInfo = (PCBMetricInfo) metricList.elementAt(i);
				if (!metricInfo.isMetricGroup)
					for (int j = 0; j < metricInfo.projectComments.length; j++) {
						//first try to update
						prepStmtUpd.setString(1, metricInfo.projectComments[j]);
						prepStmtUpd.setLong(2, pcbInfo.pcbID);
						prepStmtUpd.setInt(3, metricInfo.metricConstant);
						prepStmtUpd.setLong(4, pcbInfo.projectIDs[j]);
						if (prepStmtUpd.executeUpdate() == 0 && (metricInfo.projectComments[j] != null)) {
							//update failed, do insert
							prepStmtIns.setLong(1, pcbInfo.pcbID);
							prepStmtIns.setInt(2, metricInfo.metricConstant);
							prepStmtIns.setLong(3, pcbInfo.projectIDs[j]);
							prepStmtIns.setString(4, metricInfo.projectComments[j]);
							prepStmtIns.executeUpdate();
						}
					}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmtUpd, null);
			ServerHelper.closeConnection(null, prepStmtIns, null);
		}
	}
	/**
     * 
	 * @param period
	 * @param year
	 * @return
	 */
	public static final java.sql.Date[] getDatesFromPrevPeriod(String period, int year) {
		try {
			period = period.toUpperCase();
			if (period.equals("S1")) {
				period = "S2";
				year--;
			}
			else if (period.equals("S2"))
				period = "S1";
			else if (period.equals("Q1")) {
				period = "Q4";
				year--;
			}
			else if (period.equals("Q2"))
				period = "Q1";
			else if (period.equals("Q3"))
				period = "Q2";
			else if (period.equals("Q4"))
				period = "Q3";
			return getDatesFromPeriod(period, year);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
 /**
 * returns an array of 2 dates :start and end date of a period
 * period can be s1, s2 or q1 to q4
 */
	public static final String formatPrevPeriod(String period, int year) {
		try {
			period = period.toUpperCase();
			if (period.equals("S1")) {
				period = "S2";
				year--;
			}
			else if (period.equals("S2"))
				period = "S1";
			else if (period.equals("Q1")) {
				period = "Q4";
				year--;
			}
			else if (period.equals("Q2"))
				period = "Q1";
			else if (period.equals("Q3"))
				period = "Q2";
			else if (period.equals("Q4"))
				period = "Q3";
			return period + "-" + year;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * returns an array of 2 dates :start and end date of a period
	 * period can be s1, s2 or q1 to q4
	 */
	public static final java.sql.Date[] getDatesFromPeriod(String period, int year) {
		Date[] dateArray = new Date[2];
		try {
			period = period.toUpperCase();
			String startDate = null;
			String endDate = null;
			if (period.equals("S1")) {
				startDate = "01-jan-";
				endDate = "30-jun-";
			}
			else if (period.equals("S2")) {
				startDate = "01-jul-";
				endDate = "31-dec-";
			}
			else if (period.equals("Q1")) {
				startDate = "01-jan-";
				endDate = "31-mar-";
			}
			else if (period.equals("Q2")) {
				startDate = "01-apr-";
				endDate = "30-jun-";
			}
			else if (period.equals("Q3")) {
				startDate = "01-jul-";
				endDate = "30-sep-";
			}
			else if (period.equals("Q4")) {
				startDate = "01-oct-";
				endDate = "31-dec-";
			}
			dateArray[0] = CommonTools.parseSQLDate(startDate + year);
			dateArray[1] = CommonTools.parseSQLDate(endDate + year);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return dateArray;
		}
	}
	/**
	 * Returns vector of PCBMetricInfo
	 * @param array of metric contants referring to metric groups,
	 *  end date of previous period,
	 *  end date of actual period
	 */
    public static final Vector getPCBMetricGroupsRefreshCache(long workunitID,int lifecycleID,int[] metrics, Vector projectList, Date endDate) {
            Vector  pcbMetricInfos =getPCBMetricGroups(workunitID, lifecycleID, metrics,  projectList, endDate,false);
            PCBMetricInfo pcbMetricInfo;
            for (int i=0;i<pcbMetricInfos.size();i++){
                pcbMetricInfo = ( PCBMetricInfo)pcbMetricInfos.elementAt(i);
                DBCache.setPCB(workunitID, lifecycleID,endDate,pcbMetricInfo.metricConstant,0,pcbMetricInfo.actualAvg);
                if (pcbMetricInfo.projectValues!=null){
                    for (int j=0;j<projectList.size();j++){
                        DBCache.setPCB(workunitID, lifecycleID,endDate,pcbMetricInfo.metricConstant,((ProjectInfo)projectList.elementAt(j)).getProjectId(),pcbMetricInfo.projectValues[j]);
                    }
                }
            }
            return pcbMetricInfos;
            
    }
	public static final Vector getPCBMetricGroups(long workunitID,int lifecycleID,int[] metrics, Vector projectList, Date endDate) {
        return getPCBMetricGroups(workunitID, lifecycleID, metrics,  projectList, endDate,false);
    }
    public static final Vector getPCBMetricGroups(long workunitID,int lifecycleID,int[] metrics, Vector projectList, Date endDate, boolean useCache) {
		//Vector metricInfoList = new Vector(); //metric backup for detail PCB
		Vector result = new Vector();
		try {
			long[] projectIDs = new long[projectList.size()];
			ProjectInfo projectInfo;
			for (int k = 0; k < projectList.size(); k++) {
				projectInfo=(ProjectInfo)projectList.elementAt(k);
				projectIDs[k] = projectInfo.getProjectId();
			}
			Vector effDist = null;
            double [][]effDistStage=null;
			PCBEffortDistribInfo [] defectDist=null;
			NormPlanInfo normPlans=Norms.getNormPlan(workunitID,lifecycleID,endDate);
			boolean isSpecialDist = false;
            Vector cache=null;
            
            //TODO: Following loop cause program connect to database many time, program may crash here, should update to avoid this issue.
			// Function to loop:
            // Effort.getPCBProcessEffort, Effort.getAverageEffortByProcessAndStage and
            // Effort.getPreventionCosts , (these functions call to
            // Effort.getActualEffortProcessByDate, (this function SELECT in database))
            for (int i = 0; i < metrics.length; i++) {
				
				//get children metrics for each metric group
				PCBMetricInfo pcbGroupInfo = new PCBMetricInfo();
				pcbGroupInfo.isMetricGroup = true;
				pcbGroupInfo.name = Metrics.getMetricGroup(metrics[i]).groupName;
				pcbGroupInfo.metricID = Integer.toString(metrics[i]);
				Vector childMetrics = Metrics.getChildrenMetrics(metrics[i]);
                
				if (childMetrics.size() > 0)
					result.add(pcbGroupInfo);
				for (int j = 0; j < childMetrics.size(); j++) {
					MetricDescInfo metricDescInfo = (MetricDescInfo) childMetrics.elementAt(j);
                    if (useCache)
                        cache=DBCache.getPCB(workunitID,lifecycleID,endDate,metricDescInfo.metricConstant);
					PCBMetricInfo pcbMetricInfo = new PCBMetricInfo();
					pcbMetricInfo.name = metricDescInfo.metricName;
					pcbMetricInfo.unit = metricDescInfo.unit;
					pcbMetricInfo.metricID = metricDescInfo.metricID;
					pcbMetricInfo.definition = metricDescInfo.definition;
					pcbMetricInfo.metricConstant = metricDescInfo.metricConstant;
					pcbMetricInfo.projectValues = new double[projectList.size()];
					pcbMetricInfo.projectComments = new String[projectList.size()];
					//FOLOWING IS THE LIST OF METRIC WITH A SPECIAL DISPLAY TYPE (NOT BY PROJECT)
					//effort distribution BY PROCESS
					isSpecialDist = false;
					for (int l = 0; l < MetricDescInfo.trackedEffortProcessId.length; l++) {
						if (pcbMetricInfo.metricConstant == MetricDescInfo.trackedEffortProcessId[l]) {
							isSpecialDist = true;
                            pcbMetricInfo.name = ProcessInfo.getProcessName(ProcessInfo.trackedProcessId[l]);
                            if (!useCache) {
                                if (effDist == null)
                                    effDist = Effort.getPCBProcessEffort(projectIDs, endDate);  // call to Effort.getActualEffortProcessByDate
                                for (int m = 0; m < effDist.size(); m++) {
                                    PCBEffortDistribInfo infoTemp = (PCBEffortDistribInfo)effDist.elementAt(m);
                                    if (infoTemp.id == ProcessInfo.trackedProcessId[l]) {
                                        pcbMetricInfo.actualAvg = infoTemp.value;
                                        break;
                                    }
                                }
                            }
                            else {
                                DBCacheInfo cacheInf = DBCacheInfo.get(cache, 0);
                                if (cacheInf != null)
                                    pcbMetricInfo.actualAvg = cacheInf.value;
                            }
                            break;
                        }
                    }
					//effort distribution BY STAGE & process
					if(!isSpecialDist && MetricDescInfo.isRCRMetric(pcbMetricInfo.metricConstant)){
                        isSpecialDist=true;
                        if (!useCache) {
                            if (effDistStage == null)
                                effDistStage=Effort.getAverageEffortByProcessAndStage(projectList); // call to Effort.getActualEffortProcessByDate
                            int []procStage=MetricDescInfo.getRCRMetricProcStage(pcbMetricInfo.metricConstant);
                            int z=CommonTools.arrayScan(ProcessInfo.trackedProcessId,procStage[0]);
                            if (z>=0)
                                pcbMetricInfo.actualAvg = effDistStage[z][procStage[1]-1];
                        }
                        else {
                            DBCacheInfo cacheInf = DBCacheInfo.get(cache, 0);
                            if (cacheInf != null)
                                pcbMetricInfo.actualAvg = cacheInf.value;
                        }
                          
					}
					//defect distribution by origin
                    if (!isSpecialDist)
                        if (pcbMetricInfo.metricConstant == MetricDescInfo.DEFECTS_FROM_REQUIREMENTS
                            || pcbMetricInfo.metricConstant == MetricDescInfo.DEFECTS_FROM_DESIGN
                            || pcbMetricInfo.metricConstant == MetricDescInfo.DEFECTS_FROM_CODING
                            || pcbMetricInfo.metricConstant == MetricDescInfo.DEFECTS_FROM_OTHER) {
                            isSpecialDist = true;
                            if (defectDist == null)
                                defectDist = Defect.getGroupWeigthedDefectsByOrigin(projectIDs);
                            for (int m = 0; m < defectDist.length; m++) {
                                if (defectDist[m].id == pcbMetricInfo.metricConstant) {
                                    pcbMetricInfo.actualAvg = defectDist[m].value;
                                    //use name of origin instead of name of name of metric
                                    pcbMetricInfo.name = defectDist[m].name;
                                    break;
                                }
                            }
                        }
					//distribution by month
					if(!isSpecialDist){
						if (pcbMetricInfo.metricConstant == MetricDescInfo.SQA_TIMELINESS_DP
						||pcbMetricInfo.metricConstant == MetricDescInfo.SQA_EFFORT_DPC_BY_PROJECT){
							isSpecialDist=true;
							//one iteration /month
							ReportMonth rm= new ReportMonth(endDate);
							pcbMetricInfo.timeValues=new double[6];
							pcbMetricInfo.timeLabels=new String[6];
							rm.moveToPreviousMonth(5);//go to the first month of the semester
							Vector dpLog=null;
							if(pcbMetricInfo.metricConstant == MetricDescInfo.SQA_TIMELINESS_DP){
								//project DP
								dpLog=Defect.getDPLog(rm.getFirstDayOfMonth(),endDate);
								//group DP
								dpLog.addAll(Defect.getDPLog(Parameters.SQA_WU,rm.getFirstDayOfMonth(),endDate));
							}
							DPLogInfo dpInf;
							int total;
							int count;
							for (int k=0;k<6;k++){
								total=0;
								count=0;
								pcbMetricInfo.timeLabels[k]=rm.getMonthLabel();
								if (pcbMetricInfo.metricConstant == MetricDescInfo.SQA_TIMELINESS_DP){
									for (int m=0;m<dpLog.size();m++){
										dpInf=(DPLogInfo)dpLog.elementAt(m);
										if(dpInf.targetDate.compareTo(rm.getFirstDayOfMonth())>=0 
										&& dpInf.targetDate.compareTo(rm.getLastDayOfMonth())<=0
										&& dpInf.targetDate.compareTo(new java.util.Date())<=0){
											total++;
											if (dpInf.closedDate!=null && dpInf.targetDate.compareTo(dpInf.closedDate)>=0 )
												count++;
										}
									}
									if (total!=0)
										pcbMetricInfo.timeValues[k]=(double)count*100d/(double)total;
									else
										pcbMetricInfo.timeValues[k]=Double.NaN;
								}
								else{
									pcbMetricInfo.timeValues[k]=SQA.getSQA_EFFORT_DPC_BY_PROJECT(rm.getFirstDayOfMonth(),rm.getLastDayOfMonth());
								}
								rm.moveToNextMonth();
							}
							stats(pcbMetricInfo,pcbMetricInfo.timeValues);
						}
						
					}
					
					//other metrics, distribution by project
                    if (!isSpecialDist) {
                        //fast procedure one query for all projects
                        if (pcbMetricInfo.metricConstant == MetricDescInfo.SQA_PREVENTION_COST) {
                            long[] projects = new long[projectList.size()];
                            for (int k = 0; k < projects.length; k++)
                                projects[k] = ((ProjectInfo)projectList.elementAt(k)).getProjectId();
                            double[][] prevEff = Effort.getPreventionCosts(null, endDate, projects);    // call to Effort.getActualEffortProcessByDate
                            for (int k = 0; k < projects.length; k++)
                                if (prevEff[k][2] != 0)
                                    pcbMetricInfo.projectValues[k] = prevEff[k][1] * 100d / prevEff[k][2];
                        }
                        else// slow procedure, project by project to be updated
                            for (int k = 0; k < projectList.size(); k++) {
                                projectInfo = (ProjectInfo)projectList.elementAt(k);
                                // NormInfo nrmInfo = Norms.getNormDetails(metricDescInfo.metricConstant, projectInfo, endDate);
                                if (useCache){
                                    DBCacheInfo cacheInf = DBCacheInfo.get(cache, projectInfo.getProjectId());
                                    if (cacheInf!=null)
                                        pcbMetricInfo.projectValues[k]=cacheInf.value;
                                }
                                else 
                                    pcbMetricInfo.projectValues[k] = Norms.computeMetric(projectInfo,metricDescInfo.metricConstant, endDate);
                            }
                            
                        stats(pcbMetricInfo, pcbMetricInfo.projectValues);
                    }
					NormPlanInfo.Row theNorm;
					for(int k=0;k<normPlans.rows.size();k++){
						theNorm=(NormPlanInfo.Row)normPlans.rows.elementAt(k);
						if (theNorm.metricID==pcbMetricInfo.metricConstant){
							pcbMetricInfo.normLCL = theNorm.LCL;
							pcbMetricInfo.normUCL = theNorm.UCL;
							pcbMetricInfo.normValue = theNorm.norm;
							break;
						}
					
					}
					pcbMetricInfo.deviation =
						(pcbMetricInfo.actualAvg - pcbMetricInfo.normValue) * 100d / pcbMetricInfo.normValue;
					result.add(pcbMetricInfo);
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return result;
		}
	}
	/*public static final Vector getPCBGalInfo(){
	}*/
	/**
	 * Calculate LCL and UCL based on RAU's formula
	 * parameters are average value and standart deviation of metrics 
	 * returns array, index 0 is LCL, index 1 is UCL,2 is K, 3 is sigma
	 */
	public static final double[] calcLCL_UCL_K_SIGMA(double average, double stDev) {
		double K = Double.NaN;
		double LCL = Double.NaN;
		double UCL = Double.NaN;
		double sigma = Double.NaN;
		double[] result = { Double.NaN, Double.NaN, Double.NaN, Double.NaN };
		try {
			K = stDev / average;
			if (K < 0.15)
				sigma = 3;
			else if (K < 0.25)
				sigma = 2;
			else if (K < 0.50)
				sigma = 1;
			else if (K >= 0.50)
				sigma = 0.5;
			UCL = average + sigma * stDev;
			LCL = average - sigma * stDev;
			result[0] = LCL;
			result[1] = UCL;
			result[2] = K;
			result[3] = sigma;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return result;
		}
	}
	/**
	 * get average and standart deviation 
	 * based on the formula SUM((value-average)^2)
	 * returns array of double, index0=avg;index 2=stdev
	 * @param vector of 
	 */
	public static final double[] calcAVG_STDEV(double[] valueList) {
		double metricSum = 0;
		double[] result = { Double.NaN, Double.NaN };
		double avg = Double.NaN;
		double stDev = Double.NaN;
		double valuesFound = 0;
		double val1 = 0;
		try {
			for (int i = 0; i < valueList.length; i++) {
				if (!Double.isNaN(valueList[i])) {
					metricSum += valueList[i];
					val1 += valueList[i] * valueList[i];
					valuesFound++;
				}
			}
			if (valuesFound > 0) {
				avg = metricSum / valuesFound;
				if (valuesFound > 1)
					stDev = Math.sqrt((valuesFound * val1 - metricSum * metricSum) / (valuesFound * (valuesFound - 1)));
			}
			result[0] = avg;
			result[1] = stDev;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return result;
		}
	}
	private static void stats (PCBMetricInfo pcbMetricInfo, double [] values){
		double[] avg_stdev = calcAVG_STDEV(values);
		pcbMetricInfo.actualAvg = avg_stdev[0];
		pcbMetricInfo.stDev = avg_stdev[1];
		double[] lcl_ucl_k_sigma = calcLCL_UCL_K_SIGMA(pcbMetricInfo.actualAvg, avg_stdev[1]);
		pcbMetricInfo.LCL = lcl_ucl_k_sigma[0];
		pcbMetricInfo.UCL = lcl_ucl_k_sigma[1];
		pcbMetricInfo.K = lcl_ucl_k_sigma[2];
		pcbMetricInfo.sigma = lcl_ucl_k_sigma[3];
		metricBoundaries(pcbMetricInfo);
	}
	public static final void metricBoundaries(PCBMetricInfo inf) {
		double maxBound=MetricDescInfo.getBoundary(inf.metricConstant,false);
		if (!Double.isNaN(maxBound) && inf.UCL>maxBound)
			inf.UCL=maxBound;

		double minBound=MetricDescInfo.getBoundary(inf.metricConstant,true);
		if (!Double.isNaN(minBound) && inf.LCL<minBound)
			inf.LCL=minBound;
	}
}
