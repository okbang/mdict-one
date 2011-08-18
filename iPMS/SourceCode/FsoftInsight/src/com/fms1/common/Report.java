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
import java.util.Date;
import com.fms1.infoclass.*;
import com.fms1.tools.*;
import com.fms1.web.*;
/**
 * Weekly, milestone and Post-mortem reports logic.
 * TODO:Group points should be moved somewhere else
 *
 */
public class Report {
	public final static int WEEKLY_REPORT=1;
	public final static int MILESTONE_REPORT=2;
/**
 * this function collects the necessary info for 
 * the function getReportMetrics
 * 
 */
	public static final WeeklyReportInfo getReportCommonInfo(final ProjectInfo projectInfo , final java.sql.Date reportDate) {
        WeeklyReportInfo info=new WeeklyReportInfo();
        info.reportDate=reportDate;
        info.projectInfo = projectInfo;
        info.performanceMetrics = Project.getPerformanceMetrics(projectInfo.getProjectId(), reportDate);
        //Effort
        info.nrmEffortUsage = Norms.getNormGeneralInfo(MetricDescInfo.EFFORT_EFFECTIVENESS, info.projectInfo, reportDate);
        info.effortInfo = Effort.getEffortInfo(projectInfo, reportDate);
        //issues
        int numIssues[] = Issues.getIssueCount(projectInfo.getProjectId(), reportDate);
        info.issue = numIssues[0];
        info.issueClosed = numIssues[1];
        info.issueList = Issues.getIssuesByDate(projectInfo.getProjectId(), reportDate);
        //risks
        int[] numRisk = Risk.getRiskCount(projectInfo.getProjectId(), reportDate);
        info.risk = numRisk[0];
        info.riskOcurred = numRisk[1];
        info.riskList = Risk.getOccuredRisks(projectInfo.getProjectId(), reportDate);
        //stage
        int[] stageCount = Schedule.getStageCount(projectInfo.getProjectId(), reportDate);
        info.numStage = stageCount[0];
        info.numStageAll = stageCount[1];
        //NCMS
        info.nrmCustoComplaints = Norms.getNormDetails( MetricDescInfo.CUSTOMER_COMPLAINTS,info.projectInfo,reportDate );
        info.nrmProcessCompliance = Norms.getNormDetails(MetricDescInfo.PROCESS_COMPLIANCE, info.projectInfo, reportDate);
        //defects
        double[] planReviewDefect = Defect.getPlannedDefectsByActivity(projectInfo.getProjectId(), "review");
        info.defectPlanReview = (Double.isNaN(planReviewDefect[1])) ? planReviewDefect[0] : planReviewDefect[1];
        double[] planTestDefect = Defect.getPlannedDefectsByActivity(projectInfo.getProjectId(), "test");
        info.defectPlanTest = (Double.isNaN(planTestDefect[1])) ? planTestDefect[0] : planTestDefect[1];
        double[] planTotalDefect = Defect.getPlannedDefects(projectInfo.getProjectId());
        info.defectAll = (Double.isNaN(planTotalDefect[1])) ? planTotalDefect[0] : planTotalDefect[1];
        info.weightedDefectReview =
        	Defect.getWeightedDefectByActivityType(projectInfo.getProjectId(), "Review", reportDate, Defect.NO_LEAKAGE);
        info.weightedDefectTest = Defect.getWeightedDefectByActivityType(projectInfo.getProjectId(), "Test", reportDate, Defect.NO_LEAKAGE);
        info.defectSoFar = (int) Defect.getTotalDefects(projectInfo.getProjectId(), reportDate).totalWeightedDefect;        
        info.defectWeeklyInfo = Defect.getWeeklyDefect(projectInfo.getProjectId(), reportDate);
        //requirements
        info.requirementList = Requirement.getRequirementList(projectInfo,reportDate);
        info.nrmRequirements = Norms.getNormGeneralInfo(MetricDescInfo.REQUIREMENT_STABILITY, info.projectInfo, reportDate);
        info.requirementInfo = Requirement.getRequirementInfo(info.requirementList  , reportDate) ;
        //size
        info.projectSizeInfo = new ProjectSizeInfo(projectInfo.getProjectId(),reportDate);
        info.nrmSizeAchievement = Norms.getNormGeneralInfo(MetricDescInfo.SIZE_ACHIEVEMENT, info.projectInfo, reportDate);
        //ReviewEffort
        info.reviewEffectiveness  = Norms.getNormDetails(MetricDescInfo.REVIEW_EFFECTIVENESS, info.projectInfo, reportDate);
        //TestEffort
        info.testEffectiveness  = Norms.getNormDetails(MetricDescInfo.TEST_EFFECTIVENESS, info.projectInfo, reportDate);
        
        return info;
	}

	/**
	 * get the comments for reports (only weekly is implemented, milestone is in milestone table, not comments for PM)
	 * 
	 */
	public static final String getReportComments(final long prjID, int reportType, java.sql.Date reportDate) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		String comments = null;
		try {
			String strReportType=null;
			switch(reportType){
				case WEEKLY_REPORT:
					strReportType="WR";
					break;
				default:
					return null;
			
			}
			sql = "SELECT COMMENTS FROM REPORTCOMMENTS WHERE PROJECT_ID=? AND REPORT_TYPE=? AND REPORT_DATE=?";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			stm.setString(2, strReportType);
			stm.setDate(3, reportDate);
			rs = stm.executeQuery();
			if (rs.next())
				comments = Db.getClob(rs, "COMMENTS");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return comments;
		}
	}
	/**
	 * get the comments for reports (only weekly is implemented, milestone is in milestone table, not comments for PM)
	 * 
	 */
	public static final ProjectPointInfo getProjectPoint(final long prjID, final long milestoneID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		ProjectPointInfo info=new ProjectPointInfo();
		try {
			sql = "SELECT PROJECTSIZE, EFFEFECTIVENESS, CUSTOMERSATISFACTION, ACCEPTANCEOFDELIVERABLES, REQUIREMENTCOMPLETENESS, "
				+ "TIMELINESS, LEAKAGE, CORRECTIONCOST, PROCESSCOMPLIANCE, WORKORDER, ACCEPTANCENOTE, PROJECTPLAN, PROJECTREPORTS, "
				+ "PRESTIGE, PROJECTPOINT, PROJECTEVAL, CUSPOINT, EFFORTEFFICIENCY, OverdueNCsObs FROM MILESTONE WHERE PROJECT_ID=? AND MILESTONE_ID = ?";
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			stm.setLong(2, milestoneID);
			rs = stm.executeQuery();
			
			if (rs.next()) {
				info.ProjectSize  = Db.getDouble(rs, "PROJECTSIZE");
				info.EffortEff  = Db.getDouble(rs, "EFFEFECTIVENESS");
				info.CusSatisfaction = Db.getDouble(rs, "CUSTOMERSATISFACTION");
				info.AcceptanceOfDeliverable = Db.getDouble(rs, "ACCEPTANCEOFDELIVERABLES");
				info.ReqCompleteness = Db.getDouble(rs, "REQUIREMENTCOMPLETENESS");
				info.Timeliness = Db.getDouble(rs, "TIMELINESS");
				info.Leakage = Db.getDouble(rs, "LEAKAGE");
				info.CorrectionCost = Db.getDouble(rs, "CORRECTIONCOST");
				info.ProjectNC = Db.getDouble(rs, "PROCESSCOMPLIANCE");
				info.WOPoint = Db.getDouble(rs, "WORKORDER");
				info.ANPoint = Db.getDouble(rs, "ACCEPTANCENOTE");
				info.PPPoint = Db.getDouble(rs, "PROJECTPLAN");
				info.PRPoint = Db.getDouble(rs, "PROJECTREPORTS");
				info.PrestigePoint = Db.getDouble(rs, "PRESTIGE");
				info.ProjectPoint = Db.getDouble(rs, "PROJECTPOINT");
				info.ProjectEval = rs.getString("PROJECTEVAL");
				if (info.ProjectEval == null)
					info.ProjectEval = "N/A";
				info.CusPoint = Db.getDouble(rs, "CUSPOINT");
				info.EffortEfficiency = Db.getDouble(rs, "EFFORTEFFICIENCY");
				info.OverdueNCsObsPoint = Db.getDouble(rs, "OverdueNCsObs");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return info;
		}
	}
	public static final GroupPointInfo getGroupPoint(final int month, final long wuID, final int curYear) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		GroupPointInfo info=new GroupPointInfo();
		try {
			
			sql = "SELECT PROJECT, PROPOSAL, "
				+ "REVENUE, NETINCOME, RECEIVABLE, LANGUAGE, EXPERIENCE, TURNOVER, TECHNOLOGY, RANK, "
				+ "(PROJECT+PROPOSAL+REVENUE+NETINCOME+RECEIVABLE+LANGUAGE+EXPERIENCE+TURNOVER+TECHNOLOGY) GROUP_TOTAL "
				+ "FROM ORGPOINT WHERE WORKUNIT_ID = " + wuID
				+ " AND YEAR = " + curYear
				+ " AND MONTH = " + month;
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			
			info.workUnitId = wuID;
			info.year = curYear;
			info.month = month;
			
			if (rs.next()) {
				info.Project = Db.getDouble(rs, "PROJECT");
				info.Proposal = Db.getDouble(rs, "PROPOSAL");
				info.Revenue  = Db.getDouble(rs, "REVENUE");
				info.NetIncome  = Db.getDouble(rs, "NETINCOME");
				info.Receivable  = Db.getDouble(rs, "RECEIVABLE");
				info.Language  = Db.getDouble(rs, "LANGUAGE");
				info.Experience = Db.getDouble(rs, "EXPERIENCE");
				info.Turnover = Db.getDouble(rs, "TURNOVER");
				info.Technology  = Db.getDouble(rs, "TECHNOLOGY");
				info.GroupRanking = Db.getDouble(rs, "RANK");
				info.Total = Db.getDouble(rs, "GROUP_TOTAL");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return info;
		}
	}

	//HanhTN added
    public static final GroupPointBAInfo getGroupPointBA(final int curMonth, final long wuID, final int curYear, java.sql.Date startDate, java.sql.Date endDate, String name) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        GroupPointBAInfo info=new GroupPointBAInfo();

        try {
           
			sql = "UPDATE ORGPOINTBA SET "
									+ " RESPONSETIME = " + 0 
									+ " WHERE WORKUNIT_ID = " + wuID
									+ " AND YEAR = " + curYear
									+ " AND MONTH = " + curMonth;
            
			
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);           
            stm.executeUpdate();
            
            int count = stm.executeUpdate();
                        
            if (count == 0) {
                sql = "INSERT INTO ORGPOINTBA (WORKUNIT_ID, YEAR, MONTH, TIMELINESS, RESPONSETIME,"
                    + "BUDGETPERFORMANCE, BUSYRATE, CUSTOMERSATISFACTION, VALUEACHIEVEMENT, LANGUAGEINDEX,"
                    + "TECHNOLOGYINDEX, RANK) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

                stm = conn.prepareStatement(sql);

                stm.setLong(1, wuID);
                stm.setInt(2, curYear);
                stm.setInt(3, curMonth);
                stm.setDouble(4, info.timeLiness);
                stm.setDouble(5, 0);
                stm.setDouble(6, info.budgetPerformance);
                stm.setDouble(7, info.busyRate);
                stm.setDouble(8, info.customerSatisfaction);
                stm.setDouble(9, info.valueAchievement);
                stm.setDouble(10, info.languageIndex);
                stm.setDouble(11, info.technologyIndex);
                stm.setDouble(12, info.GroupRanking);

                count = stm.executeUpdate();
            }
            stm.close();

            sql = "SELECT TIMELINESS, RESPONSETIME, BUDGETPERFORMANCE, "
                + "BUSYRATE, CUSTOMERSATISFACTION, VALUEACHIEVEMENT, "
                + "LANGUAGEINDEX, TECHNOLOGYINDEX, RANK, "
                + "(TIMELINESS+RESPONSETIME) SUM_FIRST, "
                + "(BUDGETPERFORMANCE+BUSYRATE+CUSTOMERSATISFACTION+VALUEACHIEVEMENT) SUM_SECOND, "
                + "(LANGUAGEINDEX+TECHNOLOGYINDEX) SUM_THIRD, "
                + "(TIMELINESS+RESPONSETIME+BUDGETPERFORMANCE+BUSYRATE+CUSTOMERSATISFACTION+VALUEACHIEVEMENT+LANGUAGEINDEX+TECHNOLOGYINDEX) GROUP_TOTAL "
                + "FROM ORGPOINTBA WHERE WORKUNIT_ID = " + wuID
                + " AND YEAR = " + curYear
                + " AND MONTH = " + curMonth;
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();

            while ( rs != null && rs.next()) {
                info.workUnitId = wuID;
                info.year = curYear;
                info.month = curMonth;
                info.timeLiness = Db.getDouble(rs, "TIMELINESS");
                info.responseTime = Db.getDouble(rs,"RESPONSETIME");
                info.budgetPerformance  = Db.getDouble(rs, "BUDGETPERFORMANCE");
                info.busyRate  = Db.getDouble(rs, "BUSYRATE");
                info.customerSatisfaction  = Db.getDouble(rs, "CUSTOMERSATISFACTION");
                info.valueAchievement  = Db.getDouble(rs, "VALUEACHIEVEMENT");
                info.languageIndex = Db.getDouble(rs, "LANGUAGEINDEX");
                info.technologyIndex = Db.getDouble(rs, "TECHNOLOGYINDEX");
                info.sumFirst = Db.getDouble(rs, "SUM_FIRST");
                info.sumSecond = Db.getDouble(rs, "SUM_SECOND");
                info.sumThird = Db.getDouble(rs, "SUM_THIRD");
                info.Total = Db.getDouble(rs, "GROUP_TOTAL");
                info.GroupRanking = Db.getDouble(rs, "RANK");
            }
        }

        catch (Exception e) {
            e.printStackTrace();
            System.err.println("sql stmt = " + sql);
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return info;
        }
    }   
    public static final GroupPointBAInfo getGroupPointBA(final int curMonth, final long wuID, final int curYear) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        GroupPointBAInfo info=new GroupPointBAInfo();
        try {
            conn = ServerHelper.instance().getConnection();
            sql = "SELECT TIMELINESS, RESPONSETIME, BUDGETPERFORMANCE, "
                + "BUSYRATE, CUSTOMERSATISFACTION, VALUEACHIEVEMENT, "
                + "LANGUAGEINDEX, TECHNOLOGYINDEX, RANK, "
                + "(TIMELINESS+RESPONSETIME) SUM_FIRST, "
                + "(BUDGETPERFORMANCE+BUSYRATE+CUSTOMERSATISFACTION+VALUEACHIEVEMENT) SUM_SECOND, "
                + "(LANGUAGEINDEX+TECHNOLOGYINDEX) SUM_THIRD, "
                + "(TIMELINESS+RESPONSETIME+BUDGETPERFORMANCE+BUSYRATE+CUSTOMERSATISFACTION+VALUEACHIEVEMENT+LANGUAGEINDEX+TECHNOLOGYINDEX) GROUP_TOTAL "
                + "FROM ORGPOINTBA WHERE WORKUNIT_ID = " + wuID
                + " AND YEAR = " + curYear
                + " AND MONTH = " + curMonth;
            stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();

            while ( rs != null && rs.next()) {
                info.workUnitId = wuID;
                info.year = curYear;
                info.month = curMonth;
                info.timeLiness = Db.getDouble(rs, "TIMELINESS");
                info.responseTime = Db.getDouble(rs,"RESPONSETIME");
                info.budgetPerformance  = Db.getDouble(rs, "BUDGETPERFORMANCE");
                info.busyRate  = Db.getDouble(rs, "BUSYRATE");
                info.customerSatisfaction  = Db.getDouble(rs, "CUSTOMERSATISFACTION");
                info.valueAchievement  = Db.getDouble(rs, "VALUEACHIEVEMENT");
                info.languageIndex = Db.getDouble(rs, "LANGUAGEINDEX");
                info.technologyIndex = Db.getDouble(rs, "TECHNOLOGYINDEX");
                info.sumFirst = Db.getDouble(rs, "SUM_FIRST");
                info.sumSecond = Db.getDouble(rs, "SUM_SECOND");
                info.sumThird = Db.getDouble(rs, "SUM_THIRD");
                info.Total = Db.getDouble(rs, "GROUP_TOTAL");
                info.GroupRanking = Db.getDouble(rs, "RANK");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("sql stmt = " + sql);
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return info;
        }
    }   
    
	public static final double getFsoftPropjectPoint(final int month, final int curYear) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		double fsoftProject = 0;
		try {
			
			sql = "SELECT SUM(PROJECT) S1 FROM ORGPOINT, WORKUNIT, GROUPS WHERE " // If we have 2 organization shared one DB, it will be hell;
				+ "ORGPOINT.WORKUNIT_ID = WORKUNIT.WORKUNITID AND GROUPS.GROUP_ID = WORKUNIT.TABLEID "
				+ "AND GROUPS.ISOPERATIONGROUP = 1 AND "
				+ "YEAR = " + curYear
				+ " AND MONTH = " + month
				+ " GROUP BY YEAR, MONTH";
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			
			if (rs.next()) {
				fsoftProject = Db.getDouble(rs, "S1");
			}

			int numGroup = WorkUnit.getNumOperationGroup();

			if (numGroup > 0) {
				fsoftProject = (fsoftProject/numGroup);
				
				/*
				if (fsoftProject > 10) {
					if (fsoftProject > 40) {
						fsoftProject = 80;
					} else {
						fsoftProject = fsoftProject * 2;
					}
				} else {
					if (fsoftProject >= 0) {
						fsoftProject = fsoftProject;
					} else {
						fsoftProject = -30;
					}
				}
				*/
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return fsoftProject;
		}
	}
	public static final int getNumDeveloper(String wuName) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int numDev = 0;
		try {
			
			//sql = "SELECT COUNT(*) FROM DEVELOPER WHERE UPPER(GROUP_NAME) = '" + wuName.toUpperCase() + "'"
            // Avoid to use function in column (ie. Where Upper(...)=... )
            sql = "SELECT COUNT(*) FROM DEVELOPER WHERE GROUP_NAME = '" + wuName + "'"
				+ " AND (STATUS = 1 OR STATUS = 2)";
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			
			if (rs.next()) {
				numDev = rs.getInt(1);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return numDev;
		}
	}
	/*MANU Can't understand this code
	 * public static final int getNumRunningProject(final long workUnitID, final String beginMonth, final String endMonth) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int numProj = 0;
		try {
			
		sql =
			"SELECT a.PARENTWORKUNITID p1,b.PARENTWORKUNITID p2,PROJECT.PROJECT_ID, PROJECT.CODE"
				+ " FROM WORKUNIT a,WORKUNIT b, PROJECT"
				+ " WHERE a.type(+)= 2 AND "
				+ " PROJECT.PROJECT_ID = a.TABLEID (+) "
				+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
				+ " AND (a.PARENTWORKUNITID=" + workUnitID + " OR b.PARENTWORKUNITID=" + workUnitID + ")"
				+ " AND (PROJECT.ACTUAL_FINISH_DATE IS NULL OR PROJECT.ACTUAL_FINISH_DATE > TO_DATE('" + beginMonth + "', 'MM/DD/YYYY')) "
				+ " AND PROJECT.START_DATE < TO_DATE('" + endMonth + "', 'MM/DD/YYYY') "
				+ " UNION "
				+ " SELECT a.PARENTWORKUNITID p1,b.PARENTWORKUNITID p2, PROJECT.PROJECT_ID, PROJECT.CODE"
				+ " FROM WORKUNIT a,WORKUNIT b, PROJECT"
				+ " WHERE a.type(+)= 2 AND "
				+ " PROJECT.PROJECT_ID = a.TABLEID (+) "
				+ " AND a.PARENTWORKUNITID=b.WORKUNITID (+)"
				+ " AND (a.PARENTWORKUNITID="+workUnitID+" OR b.PARENTWORKUNITID="+workUnitID+")"
				+ " AND PROJECT.ACTUAL_FINISH_DATE >=TO_DATE('" + beginMonth + "', 'MM/DD/YYYY')"
				+ " AND PROJECT.ACTUAL_FINISH_DATE <=TO_DATE('" + endMonth + "', 'MM/DD/YYYY') ";
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);

			rs = stm.executeQuery();
			
			while (rs.next()) {
				numProj = numProj + 1;
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return numProj;
		}
	}*/
	public static final int groupRanking(final String wuName, final int month, final int year) {
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String sql = null;
		
		int groupRank = 0;
		String groupName;
		
		try {
			
			sql = "SELECT WORKUNITNAME, SUM(PROJECT+PROPOSAL+REVENUE+NETINCOME+RECEIVABLE+LANGUAGE+EXPERIENCE+TURNOVER+TECHNOLOGY) GROUP_TOTAL"
				+ " FROM ORGPOINT, WORKUNIT, GROUPS WHERE" // If we have 2 organization shared one DB, it will be hell;
				+ " YEAR = " + year
				+ " AND MONTH = " + month
				+ " AND GROUPS.ISOPERATIONGROUP = 1" //We count only operational groups
				+ " AND ORGPOINT.WORKUNIT_ID = WORKUNIT.WORKUNITID"
				+ " AND GROUPS.GROUP_ID = WORKUNIT.TABLEID"
				+ " GROUP BY YEAR, MONTH, WORKUNITNAME"
				+ " ORDER BY GROUP_TOTAL DESC";
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			
			while (rs.next()) {
				groupName = rs.getString(1);
				
				groupRank = groupRank + 1;
				
				if (groupName.trim().equalsIgnoreCase(wuName.trim())) {
					break;
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return groupRank;
		}
	}
	public static final void groupRankingAll(final int month, final int year) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		
		int groupRank = 0;
		String groupName;
		
		try {
			sql = "SELECT WORKUNITNAME, WORKUNITID FROM WORKUNIT, GROUPS WHERE TYPE = 1 AND PARENTWORKUNITID = "+Parameters.FSOFT_WU
				+ " AND GROUPS.ISOPERATIONGROUP = 1 "
				+ " AND GROUPS.GROUP_ID = WORKUNIT.TABLEID";
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			
			while (rs.next()) {
				groupName = rs.getString(1);
				groupRank = groupRanking(groupName, month, year);
				
				GroupPointInfo info=new GroupPointInfo();
				
				info.workUnitId = rs.getLong(2);
				info.year = year;
				info.month = month;
				info.GroupRanking  = groupRank;
				
				setGroupRank(info);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	
	//	HanhTN added
	public static final int groupRankingBA(final String wuName, final int month, final int year) {
		  Connection conn = null;
		  PreparedStatement stm = null;
		  ResultSet rs = null;
		  String sql = null;

		  int groupRank = 0;
		  String groupName;

		  try {
			  sql = "SELECT WORKUNITNAME, SUM(TIMELINESS+RESPONSETIME+BUDGETPERFORMANCE+BUSYRATE+CUSTOMERSATISFACTION+VALUEACHIEVEMENT+LANGUAGEINDEX+TECHNOLOGYINDEX) GROUP_TOTAL"
				  + " FROM ORGPOINTBA, WORKUNIT, GROUPS WHERE" // If we have 2 organization shared one DB, it will be hell;
				  + " YEAR = " + year
				  + " AND MONTH = " + month
				  + " AND GROUPS.ISOPERATIONGROUP IS NULL" //We count not in operational groups
				  + " AND ORGPOINTBA.WORKUNIT_ID = WORKUNIT.WORKUNITID"
				  + " AND GROUPS.GROUP_ID = WORKUNIT.TABLEID"
				  + " GROUP BY YEAR, MONTH, WORKUNITNAME"
				  + " ORDER BY GROUP_TOTAL DESC";

			  conn = ServerHelper.instance().getConnection();
			  stm = conn.prepareStatement(sql);
			  rs = stm.executeQuery();

			  while (rs.next()) {
				  groupName = rs.getString(1);
				  groupRank = groupRank + 1;

				  if (groupName.trim().equalsIgnoreCase(wuName.trim())) {
					  break;
				  }
			  }
		  }
		  catch (Exception e) {
			  e.printStackTrace();
		  }
		  finally {
			  ServerHelper.closeConnection(conn, stm, rs);
			  return groupRank;
		  }
	}

	//HanhTN added
	public static final void groupRankingAllBA(final int month, final int year) {
		  Connection conn = null;
		  PreparedStatement stm = null;
		  String sql = null;
		  ResultSet rs = null;

		  int groupRank = 0;
		  String groupName;

		  try {
			  sql = "SELECT WORKUNITNAME, WORKUNITID FROM WORKUNIT, GROUPS WHERE TYPE = 1 AND PARENTWORKUNITID = "+Parameters.FSOFT_WU
				  + " AND GROUPS.ISOPERATIONGROUP IS NULL "
				  + " AND GROUPS.GROUP_ID = WORKUNIT.TABLEID";

			  conn = ServerHelper.instance().getConnection();
			  stm = conn.prepareStatement(sql);
			  rs = stm.executeQuery();
			  GroupPointBAInfo info;
			  while (rs.next()) {
				  groupName = rs.getString(1);
				  groupRank = groupRankingBA(groupName, month, year);				  
				  
				  info=new GroupPointBAInfo();				  
				  info.workUnitId = rs.getLong(2);
				  info.year = year;
				  info.month = month;
				  info.GroupRanking  = groupRank;

				  setGroupRankBA(info);
			  }
		  }
		  catch (Exception e) {
			  e.printStackTrace();
		  }
		  finally {
			  ServerHelper.closeConnection(conn, stm, rs);
		  }
	}	
	
	public static final double sumGroupPoint(final long workUnitID,final int curMonth, final int curYear) {		
		double sumProjectPoint=0;
		
		try {
			
			String beginMonth, beginMonth1;
			// beginMonth = CommonTools.getMonth(curMonth) + "/" + "01/" + String.valueOf(curYear);
			beginMonth = "Jan/01/" + String.valueOf(curYear);
			// beginMonth1 = "01-" + CommonTools.getMonth(curMonth) + "-" + String.valueOf(curYear).trim().substring(2,4);
			beginMonth1 = "01-Jan-"+ String.valueOf(curYear).trim().substring(2,4);
			
			String endMonth, endMonth1;
			endMonth = CommonTools.getMonth(curMonth) + "/" + CommonTools.getNoDay(curMonth, curYear) + "/" + String.valueOf(curYear);
			endMonth1 = CommonTools.getNoDay(curMonth, curYear) + "-" + CommonTools.getMonth(curMonth) + "-" + String.valueOf(curYear).trim().substring(2,4);
			
			Date startDate = CommonTools.parseDate(beginMonth1);
			Date endDate = CommonTools.parseDate(endMonth1);
			
			Vector vtProject = new Vector();
			String strWhere = "";
			vtProject = WorkUnit.getProjects(workUnitID, beginMonth, endMonth, strWhere);
			
			ProjectInfo projectInfo = null;
			ProjectPointInfo ppointInfo = null;
			
			if (vtProject != null)
			{
				int projectCount= vtProject.size();
				for (int j = 0; j < projectCount; j ++)
				{
					projectInfo = (ProjectInfo)vtProject.elementAt(j);
					long projectID = projectInfo.getProjectId();
					
					Vector stageList = Schedule.getStageList(projectID);
					
					if (
						(stageList.size() == 0) ||
						(stageList == null)
						)
						{
							sumProjectPoint = sumProjectPoint + 12; // 12 is default for just starting project
					}
					else
					{
						StageInfo stageInfo = null;
						int k;
						boolean getStageCompleted = false;
						boolean getStageOfMonth = false;
						
						for (k = stageList.size() - 1; k >= 0; k--) {
							stageInfo = (StageInfo) stageList.elementAt(k);

							if ((stageInfo.aEndD != null)
								&& (stageInfo.actualBeginDate != null)
								&& (stageInfo.plannedEndDate != null)
								&& (stageInfo.plannedBeginDate != null)) {

								getStageCompleted = true;
								
								if (
									(stageInfo.aEndD.after(startDate)) &&
									(stageInfo.aEndD.before(endDate))
									)
									{
										long stageID = 0;
										stageID = stageInfo.milestoneID;
		
										ppointInfo = Report.getProjectPoint(projectID,stageID);
										if (Double.isNaN(ppointInfo.ProjectPoint))
											ppointInfo.ProjectPoint = 12;
										sumProjectPoint = sumProjectPoint + ppointInfo.ProjectPoint;
										
										getStageOfMonth = true;
										break;
									}
							}
						}
						
						if (!getStageCompleted)
						{
							sumProjectPoint = sumProjectPoint + 12;
						}
						else if (!getStageOfMonth)
						{
							for (k = stageList.size() - 1; k >= 0; k--) {
								stageInfo = (StageInfo) stageList.elementAt(k);
	
								if ((stageInfo.aEndD != null)
									&& (stageInfo.actualBeginDate != null)
									&& (stageInfo.plannedEndDate != null)
									&& (stageInfo.plannedBeginDate != null))
								{
	
									long stageID = 0;
									stageID = stageInfo.milestoneID;
	
									ppointInfo = Report.getProjectPoint(projectID,stageID);
									
									if (Double.isNaN(ppointInfo.ProjectPoint))
										ppointInfo.ProjectPoint = 12;
										
									sumProjectPoint = sumProjectPoint + ppointInfo.ProjectPoint;
									
									break;
								}
							}
						}
					}
				}
				
				if (projectCount > 0) {
					sumProjectPoint = (sumProjectPoint/projectCount);
					
					if (sumProjectPoint > 10) {
						if (sumProjectPoint > 40) {
							sumProjectPoint = 80;
						} 
						else {
							sumProjectPoint = sumProjectPoint * 2;
						}
					} 
					else {
						if (sumProjectPoint < 0) {
							sumProjectPoint = -30;
						} 

					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return sumProjectPoint;
		}
	}
	public static final void setGroupPoint(final GroupPointInfo info1) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			sql = "UPDATE ORGPOINT SET "
				+ "PROJECT = ?,"
				+ "PROPOSAL = ?,"
				+ "REVENUE = ?,"
				+ "NETINCOME = ?,"
				+ "RECEIVABLE = ?,"
				+ "LANGUAGE = ?,"
				+ "EXPERIENCE = ?,"
				+ "TURNOVER = ?,"
				+ "TECHNOLOGY = ?,"
				+ "RANK = ? "
				+ "WHERE WORKUNIT_ID = " + info1.workUnitId
				+ " AND YEAR = " + info1.year
				+ " AND MONTH = " + info1.month;
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setDouble(1, info1.Project);
			stm.setDouble(2, info1.Proposal);
			stm.setDouble(3, info1.Revenue);
			stm.setDouble(4, info1.NetIncome);
			stm.setDouble(5, info1.Receivable);
			stm.setDouble(6, info1.Language);
			stm.setDouble(7, info1.Experience);
			stm.setDouble(8, info1.Turnover);
			stm.setDouble(9, info1.Technology);
			stm.setDouble(10, info1.GroupRanking);
			
			int count = stm.executeUpdate();
			
			if (count ==0) {
				sql = "INSERT INTO ORGPOINT (WORKUNIT_ID, YEAR, MONTH, PROJECT, PROPOSAL,"
					+ "REVENUE,NETINCOME,RECEIVABLE,LANGUAGE,EXPERIENCE,"
					+ "TURNOVER,TECHNOLOGY,RANK) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

				stm = conn.prepareStatement(sql);
				
				stm.setLong(1, info1.workUnitId);
				stm.setInt(2, info1.year);
				stm.setInt(3, info1.month);
				stm.setDouble(4, info1.Project);
				stm.setDouble(5, info1.Proposal);
				stm.setDouble(6, info1.Revenue);
				stm.setDouble(7, info1.NetIncome);
				stm.setDouble(8, info1.Receivable);
				stm.setDouble(9, info1.Language);
				stm.setDouble(10, info1.Experience);
				stm.setDouble(11, info1.Turnover);
				stm.setDouble(12, info1.Technology);
				stm.setDouble(13, info1.GroupRanking);
				
				count = stm.executeUpdate();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	
	//	HanhTN added
	public static final void setGroupPointBA(final GroupPointBAInfo info1) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			sql = "UPDATE ORGPOINTBA SET "
				  + "TIMELINESS = ?,"
	//				  + "RESPONSETIME = ?,"
				  + "BUDGETPERFORMANCE = ?,"
				  + "BUSYRATE = ?,"
				  + "CUSTOMERSATISFACTION = ?,"
				  + "VALUEACHIEVEMENT = ?,"
				  + "LANGUAGEINDEX = ?,"
				  + "TECHNOLOGYINDEX = ?,"
				  + "RANK = ? "
				  + "WHERE WORKUNIT_ID = " + info1.workUnitId
				  + " AND YEAR = " + info1.year
				  + " AND MONTH = " + info1.month;
	
			  conn = ServerHelper.instance().getConnection();
			  stm = conn.prepareStatement(sql);
	
			  stm.setDouble(1, info1.timeLiness);
	//			  stm.setDouble(2, info1.responseTime);
			  stm.setDouble(2, info1.budgetPerformance);
			  stm.setDouble(3, info1.busyRate);
			  stm.setDouble(4, info1.customerSatisfaction);
			  stm.setDouble(5, info1.valueAchievement);
			  stm.setDouble(6, info1.languageIndex);
			  stm.setDouble(7, info1.technologyIndex);
			  stm.setDouble(8, info1.GroupRanking);
	
			  int count = stm.executeUpdate();
	
			  if (count ==0) {
				  sql = "INSERT INTO ORGPOINTBA (WORKUNIT_ID, YEAR, MONTH, TIMELINESS, RESPONSETIME,"
					  + "BUDGETPERFORMANCE, BUSYRATE, CUSTOMERSATISFACTION, VALUEACHIEVEMENT, LANGUAGEINDEX,"
					  + "TECHNOLOGYINDEX, RANK) "
					  + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	
				  stm = conn.prepareStatement(sql);
	
				  stm.setLong(1, info1.workUnitId);
				  stm.setInt(2, info1.year);
				  stm.setInt(3, info1.month);
				  stm.setDouble(4, info1.timeLiness);
				  stm.setDouble(5, info1.responseTime);
				  stm.setDouble(6, info1.budgetPerformance);
				  stm.setDouble(7, info1.busyRate);
				  stm.setDouble(8, info1.customerSatisfaction);
				  stm.setDouble(9, info1.valueAchievement);
				  stm.setDouble(10, info1.languageIndex);
				  stm.setDouble(11, info1.technologyIndex);
				  stm.setDouble(12, info1.GroupRanking);
	
				  count = stm.executeUpdate();
			  }
		}
		catch (Exception e) {
			  e.printStackTrace();
		}
		finally {
			  ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	
	public static final void setGroupRank(final GroupPointInfo info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			sql = "UPDATE ORGPOINT SET "
				+ "RANK = ? "
				+ "WHERE WORKUNIT_ID = " + info.workUnitId
				+ " AND YEAR = " + info.year
				+ " AND MONTH = " + info.month;
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setDouble(1, info.GroupRanking);
			stm.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	
	//HanhTN added
	public static final void setGroupRankBA(final GroupPointBAInfo info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			sql = "UPDATE ORGPOINTBA SET "
				+ "RANK = ? "
				+ "WHERE WORKUNIT_ID = " + info.workUnitId
				+ " AND YEAR = " + info.year
				+ " AND MONTH = " + info.month;

			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);

			stm.setDouble(1, info.GroupRanking);
			stm.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	
	public static final Vector getProjectPoint(final Vector vtProject, final String beginMonth, final String endMonth) {
		
		Vector vtProjectPoint = new Vector();
		Date startDate = CommonTools.parseDate(beginMonth);
		Date endDate = CommonTools.parseDate(endMonth);

		try {
			
			if (vtProject != null)
			{
				ProjectPointInfo ppointInfo = null;
				for (int j = 0; j < vtProject.size(); j ++)
				{
					ProjectInfo projectInfo = (ProjectInfo)vtProject.elementAt(j);
					long projectID = projectInfo.getProjectId();
					
					Vector stageList = Schedule.getStageList(projectID);
					
					if (
						(stageList.size() == 0) ||
						(stageList == null)
						)
					{
						
						ppointInfo = new ProjectPointInfo();
						ppointInfo.prjID = projectID;
						
						vtProjectPoint.add(ppointInfo);
					}
					else
					{
						StageInfo stageInfo = null;
						int k;
						boolean getStageCompleted = false;
						boolean getStageOfMonth = false;
						
						for (k = stageList.size() - 1; k >= 0; k--) {
							stageInfo = (StageInfo) stageList.elementAt(k);

							if ((stageInfo.aEndD != null)
								&& (stageInfo.actualBeginDate != null)
								&& (stageInfo.plannedEndDate != null)
								&& (stageInfo.plannedBeginDate != null)) {

								getStageCompleted = true;
								
								if (
									(stageInfo.aEndD.after(startDate)) &&
									(stageInfo.aEndD.before(endDate))
									)
									{
										long stageID = 0;
										stageID = stageInfo.milestoneID;
		
										ppointInfo = Report.getProjectPoint(projectID,stageID);
										ppointInfo.stageName = stageInfo.stage;
										
										vtProjectPoint.add(ppointInfo);
										getStageOfMonth = true;
										break;
									}
							}
						}
						
						if (!getStageCompleted)
						{
							ppointInfo = new ProjectPointInfo();
							ppointInfo.prjID = projectID;
							
							vtProjectPoint.add(ppointInfo);
						}
						else if (!getStageOfMonth)
						{
							for (k = stageList.size() - 1; k >= 0; k--) {
								stageInfo = (StageInfo) stageList.elementAt(k);
	
								if ((stageInfo.aEndD != null)
									&& (stageInfo.actualBeginDate != null)
									&& (stageInfo.plannedEndDate != null)
									&& (stageInfo.plannedBeginDate != null))
								{
	
									long stageID = 0;
									stageID = stageInfo.milestoneID;
	
									ppointInfo = Report.getProjectPoint(projectID,stageID);
									ppointInfo.stageName = stageInfo.stage;
									
									vtProjectPoint.add(ppointInfo);
									break;
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return vtProjectPoint;
		}
	}
	/**
	 * update the comments for Weekly reports
	 * 
	 */
	public static void setReportComments(
		final long prjID,
		String reportType,
		java.sql.Date reportDate,
		String comments) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			//Check if already exists
			sql = "SELECT COUNT(*) COL1 FROM REPORTCOMMENTS WHERE PROJECT_ID=? AND REPORT_TYPE=? AND REPORT_DATE=?";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			stm.setString(2, reportType);
			stm.setDate(3, reportDate);
			rs = stm.executeQuery();
			rs.next();
			int result = rs.getInt("COL1");
			rs.close();
			stm.close();
			//if doesn't exist, then insert
			if (result == 0) {
				sql =
					"INSERT INTO REPORTCOMMENTS (REPORTCOMMENTS_ID, PROJECT_ID, REPORT_TYPE,REPORT_DATE,COMMENTS)"
						+ " VALUES(NVL((SELECT MAX(REPORTCOMMENTS_ID)+1 FROM REPORTCOMMENTS),1),?,?,?,?)";
				stm = conn.prepareStatement(sql);
				stm.setLong(1, prjID);
				stm.setString(2, reportType);
				stm.setDate(3, reportDate);
				stm.setString(4, comments);
			}
			else {
				//if exists then update
				sql =
					"UPDATE REPORTCOMMENTS SET"
						+ " COMMENTS=?"
						+ " WHERE PROJECT_ID =? AND"
						+ " REPORT_TYPE=? AND"
						+ " REPORT_DATE=?";
				conn = ServerHelper.instance().getConnection();
				stm = conn.prepareStatement(sql);
				stm.setString(1, comments);
				stm.setLong(2, prjID);
				stm.setString(3, reportType);
				stm.setDate(4, reportDate);
			}
			stm.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}

    /*
     * 16-Jan-08: Switched into new CLOB function to avoid CLOB update issue.
	 * update the comments for Milestone reports
	public static void setMilestoneReportComments( long milestoneID, String comments) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			sql = "update MILESTONE set COMMENTS=? WHERE MILESTONE_ID =? ";
			conn=Db.getOracleConn();
			stm = conn.prepareStatement(sql);
			stm.setLong(2, milestoneID);
			Db.setCLOB(stm,1, comments);
			stm.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
     */
    
     /**
      * Update milestone comment
      * @param milestoneID
      * @param comments
      */
    public static void setMilestoneReportComments(
        long milestoneID, String comments)
    {
        Db.writeClob("MILESTONE", "COMMENTS", "MILESTONE_ID",
                Long.toString(milestoneID), comments);
    }

	public static void setProjectPoint( long milestoneID, final ProjectPointInfo info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		try {
			sql = "UPDATE MILESTONE SET "
				+ "PROJECTSIZE = ?,"
				+ "EFFEFECTIVENESS = ?,"
				+ "CUSTOMERSATISFACTION = ?,"
				+ "ACCEPTANCEOFDELIVERABLES = ?,"
				+ "REQUIREMENTCOMPLETENESS = ?,"
				+ "TIMELINESS = ?,"
				+ "LEAKAGE = ?,"
				+ "CORRECTIONCOST = ?,"
				+ "PROCESSCOMPLIANCE = ?,"
				+ "WORKORDER = ?,"
				+ "ACCEPTANCENOTE = ?,"
				+ "PROJECTPLAN = ?,"
				+ "PROJECTREPORTS = ?,"
				+ "CUSPOINT = ?,"
				+ "PRESTIGE = ?,"
				+ "PROJECTPOINT = ?,"
				+ "PROJECTEVAL = ?, "
				+ "EFFORTEFFICIENCY = ?, "
				+ "OVERDUENCSOBS = ? "
				+ "WHERE MILESTONE_ID = " + milestoneID;
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setDouble(1, info.ProjectSize);
			stm.setDouble(2, info.EffortEff);
			stm.setDouble(3, info.CusSatisfaction);
			stm.setDouble(4, info.AcceptanceOfDeliverable);
			stm.setDouble(5, info.ReqCompleteness);
			stm.setDouble(6, info.Timeliness);
			stm.setDouble(7, info.Leakage);
			stm.setDouble(8, info.CorrectionCost);
			stm.setDouble(9, info.ProjectNC);
			stm.setDouble(10, info.WOPoint);
			stm.setDouble(11, info.ANPoint);
			stm.setDouble(12, info.PPPoint);
			stm.setDouble(13, info.PRPoint);
			stm.setDouble(14, info.CusPoint);
			stm.setDouble(15, info.PrestigePoint);
			stm.setDouble(16, info.ProjectPoint);
			stm.setString(17, info.ProjectEval);
			stm.setDouble(18, info.EffortEfficiency);
			stm.setDouble(19, info.OverdueNCsObsPoint);
			stm.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
		}
	}
	/**
	 * get Post-mortem reviewers and approvers
	 * @param: project_ID;
	 * @return pmReportHeaderInfo
	 */
	public static final PmReportHeaderInfo getPmHeader(final long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		ResultSet rs = null;
		PmReportHeaderInfo info = null;
		try {
			info = new PmReportHeaderInfo();
			info.prjID = prjID;
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM POST_MORTEM_REPORT WHERE PROJECT_ID = " + prjID;
			rs = stm.executeQuery(sql);
			if (rs.next()) {
				info.reviewers = rs.getString("REVIEWERS");
				info.approvers = rs.getString("APPROVERS");
			}
			else {
				rs.close();
				sql = "INSERT INTO POST_MORTEM_REPORT(PROJECT_ID) VALUES(" + prjID + ")";
				rs=stm.executeQuery(sql);
				if (rs.next()) {
					info.approvers = "";
					info.reviewers = "";
				}
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
	 * get Post-mortem reviewers and approvers
	 * @param: project_ID;
	 * @return pmReportHeaderInfo
	 */
	public static final boolean updatePmHeader(final PmReportHeaderInfo info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		boolean bl = true;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE POST_MORTEM_REPORT SET REVIEWERS = ?, APPROVERS = ? WHERE PROJECT_ID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, info.reviewers);
			stm.setString(2, info.approvers);
			stm.setLong(3, info.prjID);
			if (stm.executeUpdate() == 0) {
				bl = false;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn,stm,null);
			return bl;
		}
	}
	/**
	 * create the list of metrics for the weekly and milestone report
	 */
	public static final Vector getReportMetrics(WeeklyReportInfo reportInfo, int reportType) {
		Vector metricList =new Vector();
		MetricDescInfo mdesc;
		//different reports, different metrics
		for (int i=0;i<21;i++){
			ReportMetricInfo metricInfo=new ReportMetricInfo();
			switch(i){
				case 0:
					metricInfo.name="Requirements";
					metricInfo.unit="Arbitrary unit";
					metricInfo.estimated=reportInfo.requirementInfo.sumSizeCommitted;
					metricInfo.spent=reportInfo.requirementInfo.sumSizeDeployedOrAccepted;
					metricInfo.remain=metricInfo.estimated-metricInfo.spent;
					break;
				case 1:
					mdesc=Metrics.getMetricDesc(MetricDescInfo.REQUIREMENT_STABILITY);
					metricInfo.name=mdesc.metricName;
					metricInfo.unit=mdesc.unit;
					metricInfo.estimated=reportInfo.nrmRequirements.average;
					metricInfo.spent=reportInfo.requirementInfo.sumSizeStability;
					metricInfo.remain=metricInfo.estimated-metricInfo.spent;
					metricInfo.color =Color.getColor(reportInfo.requirementInfo.sumSizeStability,reportInfo.nrmRequirements.lcl,reportInfo.nrmRequirements.ucl,reportInfo.nrmRequirements.colorType);
					break;
				case 2:
					metricInfo.name = "Project duration";
					metricInfo.unit = "elapsed days";
					metricInfo.estimated = CommonTools.dateDiff(reportInfo.projectInfo.getPlanStartDate(), reportInfo.projectInfo.getPlannedFinishDate());
					metricInfo.spent = CommonTools.dateDiff(reportInfo.projectInfo.getStartDate(),reportInfo.reportDate);
					metricInfo.remain = metricInfo.estimated-metricInfo.spent;
					metricInfo.color = (metricInfo.spent>metricInfo.estimated)?Color.BADMETRIC:Color.NOCOLOR;
					break;	
				case 3:
                    // Not count for weekly report
					if (reportType == Report.WEEKLY_REPORT) {
                        continue;
					}
					metricInfo.name = "ETC: Estimated date to complete";
					metricInfo.unit = "DD-MMM-YY";
					//date is stored in milliseconds
					metricInfo.estimated = reportInfo.projectInfo.getPlannedFinishDate().getTime();
					metricInfo.spent = reportInfo.reportDate.getTime();
					if (reportInfo.requirementInfo.sumSizeCompletion>0){
						double spentRequirements = reportInfo.requirementInfo.sumSizeCompletion;
						double remainRequirements = 100d-spentRequirements;
						double elapsedDays = CommonTools.dateDiff(reportInfo.projectInfo.getStartDate(),reportInfo.reportDate);
						double remainingDays = elapsedDays * remainRequirements / spentRequirements;
						metricInfo.remain = reportInfo.reportDate.getTime() + remainingDays * 24d * 3600000d;
						metricInfo.color = (metricInfo.remain>(metricInfo.estimated+24d * 3600d * 1000d))?Color.BADMETRIC:Color.GOODMETRIC;
					}
					break;
				case 4:
					metricInfo.name = "Effort status";
					metricInfo.unit = "%";
					metricInfo.estimated = 100;
					if ((reportInfo.effortInfo.budgetedEffort !=0)&&(reportInfo.requirementInfo.sumSizeCompletion!=0)){
						metricInfo.spent = Effort.calcEffortStatus(reportInfo.effortInfo.budgetedEffort,reportInfo.effortInfo.actualEffort,reportInfo.requirementInfo.sumSizeCompletion);
						metricInfo.remain = metricInfo.estimated-metricInfo.spent;
						metricInfo.color = Color.getColor(metricInfo.spent,80d,100d,Color.HIGHBAD);
					}
					break;
				case 5:
                    // Not count for weekly report
                    if (reportType == Report.WEEKLY_REPORT) {
                        continue;
                    }
					metricInfo.name = "ETC: Estimated effort to complete";
					metricInfo.unit = "person.day";
					metricInfo.spent = reportInfo.effortInfo.actualEffort;
					if (reportInfo.requirementInfo.sumSizeCompletion!=0){
						metricInfo.remain = reportInfo.effortInfo.actualEffort*(100d-reportInfo.requirementInfo.sumSizeCompletion)/reportInfo.requirementInfo.sumSizeCompletion;
						metricInfo.estimated = metricInfo.spent+metricInfo.remain;
						//same color as effort status
						metricInfo.color = Color.getColor((metricInfo.estimated*100d/reportInfo.effortInfo.budgetedEffort),reportInfo.nrmEffortUsage.lcl,reportInfo.nrmEffortUsage.ucl,reportInfo.nrmEffortUsage.colorType);
					}
					break;
                // Inserted Calendar effort, 01-Feb-2007, TrungTN
                case 6:
                    metricInfo.name = "Calendar effort";
                    metricInfo.unit = "person.day";
                    // Calendar Effort index is 9 (refer Project.getPerformanceMetrics() function)
                    MetricInfo calEffMetric =
                        (MetricInfo)reportInfo.performanceMetrics.elementAt(9);
                    if (!Double.isNaN(calEffMetric.rePlannedValue) &&
                        (calEffMetric.rePlannedValue != 0))
                    {
                        metricInfo.estimated = calEffMetric.rePlannedValue;
                    }
                    else {
                        metricInfo.estimated = calEffMetric.plannedValue;
                    }
                    metricInfo.spent = calEffMetric.actualValue;
                    metricInfo.remain = metricInfo.estimated-metricInfo.spent;
                    break;
                // Inserted Effort efficiency, 28-Feb-2007, TrungTN
                case 7:
                    metricInfo.name = "Effort efficiency";
                    metricInfo.unit = "%";
                    metricInfo.estimated =
                        reportInfo.projectInfo.getPlannedBillableEffort() * 100.0 /
                        reportInfo.projectInfo.getPlannedCalendarEffort();
                    metricInfo.spent = Effort.getEffortEfficiency(
                        reportInfo.projectInfo);
                    metricInfo.remain = metricInfo.estimated-metricInfo.spent;
                    break;
                case 8:
					metricInfo.name = "Effort usage";
					metricInfo.unit = "person.day";
					metricInfo.estimated = reportInfo.effortInfo.budgetedEffort;
					metricInfo.spent = reportInfo.effortInfo.actualEffort;
					metricInfo.remain = metricInfo.estimated-metricInfo.spent;
					break;
				case 9:
					metricInfo.name = "In which: Development";
					metricInfo.unit = "person.day";
					MetricInfo devEffort = (MetricInfo)reportInfo.performanceMetrics.elementAt(5);
					metricInfo.estimated = devEffort.plannedValue *reportInfo.effortInfo.budgetedEffort/100d;
					metricInfo.spent = reportInfo.effortInfo.developementEffort;
					metricInfo.remain = metricInfo.estimated-metricInfo.spent;
					break;
				case 10:
					metricInfo.name = "&nbsp;&nbsp;Management";
					metricInfo.unit = "person.day";
					MetricInfo manEffort = (MetricInfo)reportInfo.performanceMetrics.elementAt(6);
					metricInfo.estimated = manEffort.plannedValue *reportInfo.effortInfo.budgetedEffort/100d;
					metricInfo.spent = reportInfo.effortInfo.managementEffort;
					metricInfo.remain = metricInfo.estimated-metricInfo.spent;
					break;
				case 11:
					metricInfo.name = "&nbsp;&nbsp;Quality";
					metricInfo.unit = "person.day";
					MetricInfo quaEffort = (MetricInfo)reportInfo.performanceMetrics.elementAt(7);
					metricInfo.estimated = quaEffort.plannedValue *reportInfo.effortInfo.budgetedEffort/100d;
					metricInfo.spent = reportInfo.effortInfo.qualityEffort;
					metricInfo.remain = metricInfo.estimated-metricInfo.spent;
					break;
				case 12:
					metricInfo.name = "Defects found by review (pre-release)";
					metricInfo.unit = "weighted defects";
					metricInfo.estimated = reportInfo.defectPlanReview;
					metricInfo.spent = reportInfo.weightedDefectReview;
					metricInfo.remain = metricInfo.estimated-metricInfo.spent;
					break;
				case 13:
					metricInfo.name = "Defects found by test (pre-release)";
					metricInfo.unit = "weighted defects";
					metricInfo.estimated = reportInfo.defectPlanTest;
					metricInfo.spent = reportInfo.weightedDefectTest;
					metricInfo.remain = metricInfo.estimated-metricInfo.spent;
					break;
				case 14:
					metricInfo.name = "Defect status";
					metricInfo.unit = "%";
					metricInfo.estimated = 100;
					if (reportInfo.defectSoFar !=0)
						metricInfo.spent = reportInfo.defectAll / reportInfo.defectSoFar * 100d;
					metricInfo.remain = metricInfo.estimated-metricInfo.spent;
					metricInfo.color = Color.getColor(metricInfo.spent,80d,100d,Color.HIGHBAD);
					break;
				case 15:
					mdesc = Metrics.getMetricDesc(MetricDescInfo.TEST_EFFECTIVENESS);
					metricInfo.name = mdesc.metricName;
					metricInfo.unit = mdesc.unit;
					metricInfo.estimated = reportInfo.testEffectiveness.plannedValue;
					metricInfo.spent = reportInfo.testEffectiveness.actualValue;
					metricInfo.remain = reportInfo.testEffectiveness.plannedValue-reportInfo.testEffectiveness.actualValue;
					metricInfo.color = Color.getColor(metricInfo.spent,reportInfo.testEffectiveness.lcl,reportInfo.testEffectiveness.ucl,reportInfo.testEffectiveness.colorType);
					break;
				case 16:
					mdesc = Metrics.getMetricDesc(MetricDescInfo.REVIEW_EFFECTIVENESS);
					metricInfo.name = mdesc.metricName;
					metricInfo.unit = mdesc.unit;
					metricInfo.estimated = reportInfo.reviewEffectiveness.plannedValue;
					metricInfo.spent = reportInfo.reviewEffectiveness.actualValue;
					metricInfo.remain = reportInfo.reviewEffectiveness.plannedValue-reportInfo.reviewEffectiveness.actualValue;
					metricInfo.color = Color.getColor(metricInfo.spent,reportInfo.reviewEffectiveness.lcl,reportInfo.reviewEffectiveness.ucl,reportInfo.reviewEffectiveness.colorType);
					break;
				case 17:
					metricInfo.name = "Total product size";
					metricInfo.unit = "UCP";
					metricInfo.estimated = reportInfo.projectSizeInfo.totalPlannedSize;
					metricInfo.spent = reportInfo.projectSizeInfo.totalActualSize;
					metricInfo.remain = metricInfo.estimated-metricInfo.spent;
					metricInfo.color = Color.getColor((metricInfo.spent*100d/metricInfo.estimated),reportInfo.nrmSizeAchievement.lcl,reportInfo.nrmSizeAchievement.ucl,reportInfo.nrmSizeAchievement.colorType);
					break;
                // Inserted Total product (LOC) size, 19-Sep-2007, TrungTN
                case 18:
                    // Not count for weekly report
                    if (reportType == Report.WEEKLY_REPORT) {
                        continue;
                    }
                    ProductLocInfo planLoc =
                        WorkProduct.getTotalLocProductivityQuality(
                            reportInfo.projectInfo.getProjectId(),
                            "PRODUCT_LOC_PLAN");
                    ProductLocInfo stageLoc =
                        WorkProduct.getTotalLocProductivityQuality(
                            reportInfo.projectInfo.getProjectId(),
                            null,   // Get from earliest date
                            reportInfo.reportDate,  // Until milestone end date
                            Project.ALLPROJECTS,
                            true,   // Get accumulate value
                            "PRODUCT_LOC_ACTUAL");
                    metricInfo.name=""; // Total product LOC size
                    metricInfo.unit="LOC";
                    metricInfo.estimated = planLoc.getLocProductivity();
                    metricInfo.spent = stageLoc.getLocProductivity();
                    metricInfo.remain = metricInfo.estimated-metricInfo.spent;
                    metricInfo.color = Color.getColor(
                        (metricInfo.spent * 100d / metricInfo.estimated),
                        reportInfo.nrmSizeAchievement.lcl,
                        reportInfo.nrmSizeAchievement.ucl,
                        reportInfo.nrmSizeAchievement.colorType);
                    break;
				case 19:
					metricInfo.name="Total issues";
					metricInfo.unit="issues";
					metricInfo.estimated=reportInfo.issue;
					metricInfo.spent=reportInfo.issueClosed;
					metricInfo.remain=metricInfo.estimated-metricInfo.spent;
					break;
				case 20:
					metricInfo.name="Total risk";
					metricInfo.unit="risks";
					metricInfo.estimated=reportInfo.risk;
					metricInfo.spent=reportInfo.riskOcurred;
					metricInfo.remain=metricInfo.estimated-metricInfo.spent;
					break;	
				//TODO Not displayed in Metrics list of Weekly, Milestone report => remove ?
                case 21:
					metricInfo.name="Customer complaints";
					metricInfo.unit="complaints";
					metricInfo.estimated=reportInfo.nrmCustoComplaints.plannedValue;
					metricInfo.spent=reportInfo.nrmCustoComplaints.actualValue;
					metricInfo.remain=metricInfo.estimated-metricInfo.spent;
					break;
			}
			metricList.add(metricInfo);
		}
		return metricList;
	
	}

	public static final Vector getProjectHis(final long prjID) {
		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		
		Vector vtNullHis = new Vector();
		Vector vtSortedHis = new Vector();
		
		try {
			
			conn = ServerHelper.instance().getConnection();
			
			sql = "SELECT * FROM PROJECT_HIS WHERE PROJECT_ID = " + prjID;
			stm = conn.prepareStatement(sql);
			
			rs = stm.executeQuery();

			while (rs.next()) {
				ProjectHisInfo prjHisInfo = new ProjectHisInfo();
				
				prjHisInfo.projectHisId = rs.getLong("PROJECT_HIS_ID");
				prjHisInfo.projectId = prjID;
				prjHisInfo.eventDate = rs.getDate ("EVENT_DATE");
				prjHisInfo.events = rs.getString("EVENT");
				prjHisInfo.comments = rs.getString("COMMENTS");
				prjHisInfo.link = ProjectHisInfo.HISLINK + "&prjHisId=" + prjHisInfo.projectHisId;
				
				if (prjHisInfo.eventDate == null)
					vtNullHis.add(prjHisInfo);
				else
					vtSortedHis.add(prjHisInfo);
			}

			Vector stageList = Schedule.getStageList(prjID);
			StageInfo stageInfo = null;
	        for(int i = 0; i < stageList.size(); i++)
    	    {
				stageInfo = (StageInfo) stageList.elementAt(i);
				
				ProjectHisInfo prjHisInfo = new ProjectHisInfo();
				
				prjHisInfo.projectHisId = -1; // -1: Stage
				prjHisInfo.projectId = prjID;
				prjHisInfo.eventDate = stageInfo.aEndD;
				prjHisInfo.events = "Stage: " + stageInfo.stage + " finished";
				prjHisInfo.comments = stageInfo.description;
				prjHisInfo.link = ProjectHisInfo.MILESTONELINK;
				
				if (prjHisInfo.eventDate == null)
					vtNullHis.add(prjHisInfo);
				else
					vtSortedHis.add(prjHisInfo);
    	    }
			
			Vector iterationList = Project.getPLIterationList(prjID);
			IterationInfo iterationInfo = null;
			for(int i = 0; i < iterationList.size(); i++)
			{
			 	iterationInfo = (IterationInfo) iterationList.elementAt(i);
			 	
				ProjectHisInfo prjHisInfo = new ProjectHisInfo();
				
				prjHisInfo.projectHisId = -2; // -2: Iteration
				prjHisInfo.projectId = prjID;
				prjHisInfo.eventDate = iterationInfo.actualEndDate;
				prjHisInfo.events = "Iteration" + iterationInfo.iteration + ": "+ iterationInfo.milestone + " finished";
				prjHisInfo.comments = iterationInfo.description;
				prjHisInfo.link = ProjectHisInfo.MILESTONELINK;
				
				if (prjHisInfo.eventDate == null)
					vtNullHis.add(prjHisInfo);
				else
					vtSortedHis.add(prjHisInfo);
			}

			Vector deliverableList = WorkProduct.getDeliverableList(prjID);
			ModuleInfo moduleInfo = null;
			for(int i = 0; i < deliverableList.size(); i++)
			{
			 	moduleInfo = (ModuleInfo) deliverableList.elementAt(i);
			 	
				ProjectHisInfo prjHisInfo = new ProjectHisInfo();
				
				prjHisInfo.projectHisId = -3; // -3: Delivery
				prjHisInfo.projectId = prjID;
				prjHisInfo.eventDate = moduleInfo.actualReleaseDate;
				prjHisInfo.events = "Deliver: " + moduleInfo.name + " to customer";
				prjHisInfo.comments = (moduleInfo.note == null) ? "": moduleInfo.note;
				prjHisInfo.link = ProjectHisInfo.DELIVERYLINK;
				
				if (prjHisInfo.eventDate == null)
					vtNullHis.add(prjHisInfo);
				else
					vtSortedHis.add(prjHisInfo);
			}
			
			// ProjectInfo projectInfo = new ProjectInfo(prjID);
			ProjectInfo projectInfo = Project.getProjectInfo(prjID);
			
			ProjectHisInfo prjHisInfo1 = new ProjectHisInfo();
			
			prjHisInfo1.projectHisId = -4; // -4: start date
			prjHisInfo1.projectId = prjID;
			prjHisInfo1.eventDate = projectInfo.getStartDate();
			prjHisInfo1.events = "Project starts";
			prjHisInfo1.comments = "";
			prjHisInfo1.link = ProjectHisInfo.NOLINK;
			
			if (prjHisInfo1.eventDate == null)
				vtNullHis.add(prjHisInfo1);
			else
				vtSortedHis.add(prjHisInfo1);
			
			ProjectHisInfo prjHisInfo2 = new ProjectHisInfo();
			
			prjHisInfo2.projectHisId = -5; // -5: end date
			prjHisInfo2.projectId = prjID;
			prjHisInfo2.eventDate = projectInfo.getActualFinishDate();
			prjHisInfo2.events = "Project ends";
			prjHisInfo2.comments = "";
			prjHisInfo2.link = ProjectHisInfo.NOLINK;
			
			if (prjHisInfo2.eventDate == null)
				vtNullHis.add(prjHisInfo2);
			else
				vtSortedHis.add(prjHisInfo2);
			
			sortHis(vtSortedHis);
			
			for (int i=0;i<vtNullHis.size();i++)
			{
				ProjectHisInfo prjHisInfo = (ProjectHisInfo)vtNullHis.elementAt(i);
				vtSortedHis.add(prjHisInfo);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return vtSortedHis;
		}
	}
	public static final ProjectHisInfo getProjectHis2(final long prjHisId) {
		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		
		ProjectHisInfo prjHisInfo = new ProjectHisInfo();
		try {
			
			conn = ServerHelper.instance().getConnection();
			
			sql = "SELECT * FROM PROJECT_HIS WHERE PROJECT_HIS_ID = " + prjHisId;
			stm = conn.prepareStatement(sql);
			
			rs = stm.executeQuery();

			if (rs.next()) {
				
				prjHisInfo.projectHisId = prjHisId;
				prjHisInfo.projectId = rs.getLong("PROJECT_ID");
				prjHisInfo.eventDate = rs.getDate ("EVENT_DATE");
				prjHisInfo.events = rs.getString("EVENT");
				prjHisInfo.comments = rs.getString("COMMENTS");
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return prjHisInfo;
		}
	}
	public static final void deleteProjectHis(final long prjHisId) {
		Db.delete(prjHisId,"PROJECT_HIS_ID","PROJECT_HIS");
	}
	public static final void updateProjectHis(ProjectHisInfo info) {
		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		
		try {
			sql = "UPDATE PROJECT_HIS SET "
				+ "EVENT_DATE = ?,"
				+ "EVENT = ?,"
				+ "COMMENTS = ?"
				+ "WHERE PROJECT_HIS_ID = ?";
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setDate(1, info.eventDate);
			stm.setString(2, info.events);
			stm.setString(3, info.comments);
			stm.setLong(4, info.projectHisId);
			
			stm.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final void addProjectHis(ProjectHisInfo info) {
		
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		
		try {
			sql = "INSERT INTO PROJECT_HIS VALUES (NVL((SELECT MAX(PROJECT_HIS_ID)+1 FROM PROJECT_HIS),1)"
				+ ",?,?,?,?)";
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setLong(1, info.projectId);
			stm.setDate(2, info.eventDate);
			stm.setString(3, info.events);
			stm.setString(4, info.comments);
			
			stm.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public final static void sortHis(Vector sortMe) {
		// sort by date
		boolean isFound=true;
		
		int size = sortMe.size();
		ProjectHisInfo ProjectHis,ProjectHisPrev,ProjectHisTmp;
		
		long prevDate,date;
		
		while(isFound){
			prevDate=0;
			date=0;
			isFound=false;
			for (int i=1;i<size;i++){
				ProjectHisPrev=(ProjectHisInfo)sortMe.elementAt(i-1);
				ProjectHis=(ProjectHisInfo)sortMe.elementAt(i);
				prevDate=ProjectHisPrev.eventDate.getTime();
				date=ProjectHis.eventDate.getTime();
				
				if (date<prevDate)
				{
					ProjectHisTmp=ProjectHis;
					sortMe.setElementAt(ProjectHisPrev,i);
					sortMe.setElementAt(ProjectHisTmp,i-1);
					isFound=true;
				}
			}
		}
	}
}