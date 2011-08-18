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
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import com.fms1.web.*;
import com.fms1.tools.*;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.*;

/**
 * @author thimb
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/**
 * Defect related logic and interface with DMS data.
 * 
 */

public final class Defect {
	
	public static final int NUMBER_OF_ROW_ADDABLE = 10;
	//MISC Constants
	static public final boolean NO_LEAKAGE = false;
	static public final boolean WITH_LEAKAGE = true;
    final static String EARLY_START_DATE = "01-JAN-2000";
    final static String LATEST_END_DATE = "01-JAN-3000";    // Does this program still running until 3000? 
	public static double getProjectWeightedLeakage(final long projectID, final DefectInfo info) {
		return getProjectWeightedLeakage(projectID, null, info);
	}
    //HuyNH2 modify this function for project archive
	public static double getProjectWeightedLeakage(final long projectID, Date endDate, DefectInfo info) {			
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int totalWeightedLeakage = 0;
		double leakage = Double.NaN;
		String dateConstraint;
        String defectTable = "DEFECT";
//        if(Db.checkProjecIsArchive(projectID)){
//            defectTable = "DEFECT_ARCHIVE";
//        }
		try {
			if (endDate == null) {
                dateConstraint = "";
			}
			else {
				java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
				dateConstraint = " AND A.CREATE_DATE <='" + dateFormat.format(endDate) + "'";
			}
			conn = ServerHelper.instance().getConnection();
				sql =
					" SELECT SUM(B.WEIGHT) TOTAL_WEIGHTED_LEAKAGE "
						+ " FROM " + defectTable +  " A, DEFECT_SEVERITY B "
						+ " WHERE A.DEFS_ID = B.DEFS_ID "
						+ " AND A.PROJECT_ID = ? "
						+ " AND A.QA_ID <>30 " // CR - Quality_Gate_Inspection
						+ " AND A.QA_ID <>40 " // CR - BaseLine_Audit
						+ " AND A.QA_ID <>41 " // CR - Other_audit
						+ " AND A.DS_ID <>6" //not cancelled
	                    + " AND (A.QA_ID = 13 OR A.QA_ID = 15 OR A.QA_ID=22)" + dateConstraint;
			stm = conn.prepareStatement(sql);
			stm.setLong(1, projectID);
			rs = stm.executeQuery();
			if (rs.next()) {
                totalWeightedLeakage = rs.getInt("TOTAL_WEIGHTED_LEAKAGE");
			}
			//close the connection before creating a new one 
			ServerHelper.closeConnection(conn, stm, rs);
			ProjectSizeInfo sizeInfo = new ProjectSizeInfo(projectID);
			if (sizeInfo.totalActualSize != 0)
				leakage = totalWeightedLeakage / sizeInfo.totalCreatedSize;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
            if (info != null) {
                info.totalWeightedLeakage = totalWeightedLeakage;
                info.leakage = leakage;
            }
            return leakage;
		}
	}
    
    /**
     * Returns number of defects and weigthed defect of project until endDate
     * @param projectID
     * @param endDate
     * @return
     */
    public static DefectInfo getTotalDefects(
            long projectId, java.sql.Date endDate) {
        return getTotalDefects(projectId, null, endDate, true);
    }
    /**
     * Returns number of defects and weigthed defects
     * @param projectID
     * @param beginDate
     * @param endDate
     * @param isAccumulate Calculate from begining until endDate or
     *  from beginDate to endDate
     * @return
     */
	public static DefectInfo getTotalDefects(long projectId,
            java.sql.Date beginDate, java.sql.Date endDate,
            boolean isAccumulate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
        DefectInfo result = new DefectInfo();
		String dateFilter = "";
        String projectFilter = "";
		try {
            if (projectId > 0) {
                projectFilter = " AND DF.PROJECT_ID=?";
            }
            if (beginDate != null && !isAccumulate) {
                dateFilter += " AND DF.CREATE_DATE >=?";
            }
			if (endDate != null) {
				dateFilter += " AND DF.CREATE_DATE <=?";
			}
			conn = ServerHelper.instance().getConnection();
			sql = " SELECT COUNT(DF.DEFECT_ID) NUMBERS_OF_DEFECTS,"
                + " SUM(DS.WEIGHT) TOTAL_WEIGHTED_DEFECT "
				+ " FROM DEFECT DF, DEFECT_SEVERITY DS "
				+ " WHERE DF.DEFS_ID = DS.DEFS_ID "
				+ " AND DF.QA_ID <>30 " // CR - Quality_Gate_Inspection
				+ " AND DF.QA_ID <>40 " // CR - BaseLine_Audit
				+ " AND DF.QA_ID <>41 " // CR - Other_Audit
				+ " AND DF.DS_ID <>6" //not cancelled
				+ projectFilter
                + dateFilter;

			prepStmt = conn.prepareStatement(sql);
            int i = 1;
            if (projectId > 0) {
                prepStmt.setLong(i++, projectId);
            }
            if (beginDate != null && !isAccumulate) {
                prepStmt.setDate(i++, beginDate);
            }
            if (endDate != null) {
                prepStmt.setDate(i++, endDate);
            }
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				result.totalDefect = rs.getInt("NUMBERS_OF_DEFECTS");
				result.totalWeightedDefect = rs.getInt("TOTAL_WEIGHTED_DEFECT");
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
     * Returns number of defects and weigthed defects
     * @param wu
     * @param beginDate
     * @param endDate
     * @param isAccumulate Calculate from begining until endDate or
     *  from beginDate to endDate
     * @return
     */
    public static DefectInfo getTotalDefects(WorkUnitInfo wu,
            java.sql.Date beginDate, java.sql.Date endDate,
            boolean isAccumulate) {

        // Get for a project
        if (wu.type == WorkUnitInfo.TYPE_PROJECT) {
            return getTotalDefects(wu.tableID, beginDate, endDate, isAccumulate);
        }
        // Get for all projects of organization (means leaves project blank (0))
        else if (wu.type == WorkUnitInfo.TYPE_ORGANIZATION) {
            return getTotalDefects(0, beginDate, endDate, isAccumulate);
        }
        
        Connection conn = null;
        PreparedStatement prepStmt = null;
        String sql = null;
        ResultSet rs = null;
        DefectInfo result = new DefectInfo();
        String dateFilter = "";
        String projectFilter = "";
        try {
            if (beginDate != null && !isAccumulate) {
                dateFilter = " AND DF.CREATE_DATE >=?";
            }
            if (endDate != null) {
                dateFilter = " AND DF.CREATE_DATE <=?";
            }
            // Because above top condition is already calculated for project and
            // for Organization, the following filter is for Group case.
            // The condition should always TRUE but we still need to check it
            if (wu.type == WorkUnitInfo.TYPE_GROUP) {
                projectFilter = " AND P.GROUP_NAME=?";
            }
            
            conn = ServerHelper.instance().getConnection();
            sql = " SELECT COUNT(DF.DEFECT_ID) NUMBERS_OF_DEFECTS,"
                + " SUM(DS.WEIGHT) TOTAL_WEIGHTED_DEFECT "
                + " FROM DEFECT DF, DEFECT_SEVERITY DS, PROJECT P "
                + " WHERE DF.DEFS_ID = DS.DEFS_ID "
                + " AND DF.DS_ID <>6" //not cancelled
                + " AND P.PROJECT_ID = DF.PROJECT_ID"
                + dateFilter
                + projectFilter;

            prepStmt = conn.prepareStatement(sql);
            int i = 1;
            if (beginDate != null && !isAccumulate) {
                prepStmt.setDate(i++, beginDate);
            }
            if (endDate != null) {
                prepStmt.setDate(i++, endDate);
            }
            if (wu.type == WorkUnitInfo.TYPE_GROUP) {
                prepStmt.setString(i++, wu.workUnitName);
            }
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                result.totalDefect = rs.getInt("NUMBERS_OF_DEFECTS");
                result.totalWeightedDefect = rs.getInt("TOTAL_WEIGHTED_DEFECT");
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
    
    
	public static double getWeightedDefectByActivityType(final long projectID, final String ActivityType, boolean withLeakage) {
		return getWeightedDefectByActivityType(projectID, ActivityType, new Date(0), withLeakage);
	}
    
    //HuyNH2 modify this function for project archive.
	public static double getWeightedDefectByActivityType(
		final long projectID,
		final String ActivityType,
		Date endDate,
		boolean withLeakage) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		double totalWeightedDFR = 0;
        String defectTable = "DEFECT";
//        if(Db.checkProjecIsArchive(projectID)){
//            defectTable = "DEFECT_ARCHIVE";
//        }        
		try {
			String dateConstraint = null;
			if ((endDate == null) || (endDate.getTime() == 0))
				dateConstraint = "";
			else {
				java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
				dateConstraint = "AND CREATE_DATE <= '" + dateFormat.format(endDate) + "' ";
			}
			String leakageConstraint;
			if (withLeakage) {
                leakageConstraint = "";
			}
			else {
                leakageConstraint =
                    " AND QC_ACTIVITY.QA_ID = DF.QA_ID"
                        + " AND NOT(QC_ACTIVITY.QA_ID = 13 OR QC_ACTIVITY.QA_ID = 15 OR QC_ACTIVITY.QA_ID=22)";
			}
			sql =
				"SELECT "
					+ "SUM(DS.WEIGHT) WGH_DEF "
					+ "FROM "
                    + defectTable 
					+ " DF, ACTIVITY_TYPE AC, DEFECT_SEVERITY DS "
					+ ((withLeakage) ? "" : ",QC_ACTIVITY ")
					+ "WHERE "
					+ "DF.DEFS_ID = DS.DEFS_ID AND "
					+ "DF.QA_ID <>30 AND " // CR - Quality_Gate_Inspection
					+ "DF.QA_ID <>40 AND " // CR - BaseLine_Audit
					+ "DF.QA_ID <>41 AND " // CR - Other_Audit
					+ "DF.DS_ID <>6 AND " //not cancelled
                    + "DF.AT_ID = AC.AT_ID AND " + "UPPER(AC.NAME) = ? AND "
                    + "DF.PROJECT_ID = ? " + dateConstraint + leakageConstraint;
            conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(2, projectID);
			prepStmt.setString(1, ActivityType.toUpperCase());
			rs = prepStmt.executeQuery();
			if (rs.next())
				totalWeightedDFR = rs.getDouble("WGH_DEF");
			return totalWeightedDFR;
		}
		catch (Exception e) {
			e.printStackTrace();
			return totalWeightedDFR;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
    
    //HuyNH2 modify this function for proejct archive.
	public static final DefectInfo getDefect(final long projectID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		final DefectInfo info = new DefectInfo();
		double personDayReview = -1;
		double personDayTest = -1;
        String defectTable = "DEFECT";
//        if(Db.checkProjecIsArchive(projectID)){
//            defectTable = "DEFECT_ARCHIVE";
//        }        
		try {
			conn = ServerHelper.instance().getConnection();
			//Pending defects
				sql =
					"SELECT "
						+ "COUNT(DF.DEFECT_ID) N_DF, SUM(DS.WEIGHT) SUM_W "
						+ "FROM "
                        + defectTable
						+ " DF, DEFECT_SEVERITY DS "
						+ "WHERE "
						+ "DF.DEFS_ID = DS.DEFS_ID AND "
						+ "DF.DS_ID <>6 AND " //not cancelled
						+ "DF.QA_ID <>30 AND " // CR - Quality_Gate_Inspection
						+ "DF.QA_ID <>40 AND " // CR - BaseLine_Audit
						+ "DF.QA_ID <>41 AND " // CR - Other_Audit
                    	+ "DS_ID IN(1,2,3) AND " + "PROJECT_ID = ? ";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				info.pendingDefect = rs.getInt("N_DF");
				info.pendingWeightedDefect = rs.getInt("SUM_W");
			}
			rs.close();
			prepStmt.close();
            DefectInfo totalDefects = getTotalDefects(projectID, null); // 2. Modified by HaiMM for CR
			info.totalDefect = totalDefects.totalDefect;
			info.totalWeightedDefect = totalDefects.totalWeightedDefect;
			getProjectWeightedLeakage(projectID, info); // 3. Modified by HaiMM for CR
			// We must exclude effort for non-engineering activities like PM, QC, ...
			personDayReview = Effort.getEffortByTOW(projectID, EffortInfo.TOW_ENGINEERING_REVIEW);
			personDayTest = Effort.getActualEffortProcessByDate(projectID, ProcessInfo.TEST, null, null);
			//4. Modified by HaiMM for CR
			info.reviewEffect = getWeightedDefectByActivityType(projectID, "Review", WITH_LEAKAGE) / personDayReview;
			info.testEffect = getWeightedDefectByActivityType(projectID, "Test", NO_LEAKAGE) / personDayTest;
			info.reviewEfficiency = getWeightedDefectByActivityType(projectID, "Review", NO_LEAKAGE) / (double) info.totalWeightedDefect;
			info.testEfficiency = getWeightedDefectByActivityType(projectID, "Test", NO_LEAKAGE) / (double) info.totalWeightedDefect;
			// End of 4
			
			info.defectRemovalEfficiency = info.reviewEfficiency + info.testEfficiency;
			double[] planedDefect = getPlannedDefects(projectID);
			info.estimatedDefect = planedDefect[0];
			info.reestimatedDefect = planedDefect[1];
			ProjectInfo projectInfo = Project.getProjectInfo(projectID);
			NormInfo defectRateInfo = Norms.getNormDetails(MetricDescInfo.DEFECT_RATE, projectInfo);
			if (defectRateInfo != null) {
				info.normDefectRate = defectRateInfo.average;
				info.defectRate = defectRateInfo.actualValue;
			}
			NormInfo leakageInfo = Norms.getNormDetails(MetricDescInfo.LEAKAGE, projectInfo);
			if (leakageInfo != null)
				info.targetLeakage = leakageInfo.plannedValue;
			final ProjectSizeInfo sizeInfo = new ProjectSizeInfo(projectID);
			if (sizeInfo.totalActualSize > 0) {
				info.possibleLeakage =
					sizeInfo.totalActualSize * info.normDefectRate - info.totalWeightedDefect + info.pendingWeightedDefect;
			}
			else {
				info.possibleLeakage = sizeInfo.totalPlannedSize - info.totalWeightedDefect + info.pendingWeightedDefect;
			}
			if (info.possibleLeakage < 0)
				info.possibleLeakage = 0;
			info.removedDefect = sizeInfo.totalPlannedSize * (info.normDefectRate - info.targetLeakage);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
		return info;
	}
    //HuyNH2 modify this function for project archive.
	public static final Vector getWeeklyDefect(final long projectID, final java.util.Date reportDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
        final Vector weeklyDefectList = new Vector();
        final DefectWeeklyInfo openingDefectWeeklyInfo = new DefectWeeklyInfo();
        final DefectWeeklyInfo totalDefectWeeklyInfo = new DefectWeeklyInfo();
        final DefectWeeklyInfo weightedOpeningDefectWeeklyInfo = new DefectWeeklyInfo();
        final DefectWeeklyInfo weightedTotalDefectWeeklyInfo = new DefectWeeklyInfo();
		final java.sql.Date dateReportDate = new java.sql.Date(reportDate.getTime());
        String defectTable = "DEFECT";
//        if(Db.checkProjecIsArchive(projectID)) {
//            defectTable = "DEFECT_ARCHIVE";
//        }
		try {
			conn = ServerHelper.instance().getConnection();
            conn.setAutoCommit(false);
			//open and weighted open defects
			sql =
				"SELECT "
					+ "COUNT(DF.DEFECT_ID) N_DF, SUM(DS.WEIGHT) SUM_W, DF.DEFS_ID "
					+ "FROM "
                    + defectTable
					+ " DF, DEFECT_SEVERITY DS "
					+ "WHERE "
					+ "DF.DEFS_ID = DS.DEFS_ID AND "
					+ "DS_ID BETWEEN 1 AND 3 AND "
					+ "DF.QA_ID <>30 AND "  // CR - Quality_Gate_Inspection
					+ "DF.QA_ID <>40 AND " // CR - BaseLine_Audit
					+ "DF.QA_ID <>41 AND " // CR - Other_Audit
					+ "PROJECT_ID = ? AND CREATE_DATE <= ? "
					+ "GROUP BY DF.DEFS_ID ";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectID);
			prepStmt.setDate(2, dateReportDate);
			int pending = 0;
			int total = 0;
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				do {
                    int intDefectSeverityId = rs.getInt("DEFS_ID");
                    int intOpening = rs.getInt("N_DF");
                    int intWeightedOpening = rs.getInt("SUM_W");
                    switch (intDefectSeverityId) {
                        case 1:
                            openingDefectWeeklyInfo.setFatal(intOpening);
                            weightedOpeningDefectWeeklyInfo.setFatal(intWeightedOpening);
                            break;
                        case 2:
                            openingDefectWeeklyInfo.setSerious(intOpening);
                            weightedOpeningDefectWeeklyInfo.setSerious(intWeightedOpening);
                            break;
                        case 3:
                            openingDefectWeeklyInfo.setMedium(intOpening);
                            weightedOpeningDefectWeeklyInfo.setMedium(intWeightedOpening);
                            break;
                        case 4:
                            openingDefectWeeklyInfo.setCosmetic(intOpening);
                            weightedOpeningDefectWeeklyInfo.setCosmetic(intWeightedOpening);
                            break;
                    }
                    pending += intOpening;
                    total += intWeightedOpening;
				} while (rs.next());
                openingDefectWeeklyInfo.setTotal(pending);
                weightedOpeningDefectWeeklyInfo.setTotal(total);
                rs.close();
			} else {
                //total and total weighted defects
                sql =
                    "SELECT "
                        + "COUNT(DF.DEFECT_ID) N_DF, SUM(DS.WEIGHT) SUM_W, DF.DEFS_ID "
                        + "FROM "
                        + defectTable
                        + " DF, DEFECT_SEVERITY DS "
                        + "WHERE "
                        + "DF.DEFS_ID = DS.DEFS_ID AND "
                        + "DF.DS_ID <> 6 AND " //not cancelled
						+ "DF.QA_ID <>30 AND " // CR - Quality_Gate_Inspection
						+ "DF.QA_ID <>40 AND " // CR - BaseLine_Audit
						+ "DF.QA_ID <>41 AND " // CR - Other_Audit
                        + "PROJECT_ID = ? AND CREATE_DATE <= ? " + "GROUP BY DF.DEFS_ID";
			}
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectID);
			prepStmt.setDate(2, dateReportDate);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				pending = 0;
				total = 0;
				do {
                    int intDefectSeverityId = rs.getInt("DEFS_ID");
                    int intTotal = rs.getInt("N_DF");
                    int intWeightedTotal = rs.getInt("SUM_W");
                    switch (intDefectSeverityId){
                        case 1: 
                            totalDefectWeeklyInfo.setFatal(intTotal);
                            weightedTotalDefectWeeklyInfo.setFatal(intWeightedTotal);
                            break;
                        case 2:
                            totalDefectWeeklyInfo.setSerious(intTotal);
                            weightedTotalDefectWeeklyInfo.setSerious(intWeightedTotal);
                            break;
                        case 3:
                            totalDefectWeeklyInfo.setMedium(intTotal);
                            weightedTotalDefectWeeklyInfo.setMedium(intWeightedTotal);
                            break;
                        case 4:
                            totalDefectWeeklyInfo.setCosmetic(intTotal);
                            weightedTotalDefectWeeklyInfo.setCosmetic(intWeightedTotal);
                            break;
                    }
                    pending += intTotal;
                    total += intWeightedTotal;
				} while (rs.next());
                totalDefectWeeklyInfo.setTotal(pending);
                weightedTotalDefectWeeklyInfo.setTotal(total);
				rs.close();                
			}
            conn.commit();
            conn.setAutoCommit(true);
            weeklyDefectList.add(openingDefectWeeklyInfo);
            weeklyDefectList.add(totalDefectWeeklyInfo);
            weeklyDefectList.add(weightedOpeningDefectWeeklyInfo);
            weeklyDefectList.add(weightedTotalDefectWeeklyInfo);
            return weeklyDefectList;
		} catch (Exception e) {
			e.printStackTrace();
            return null;
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	//HuyNH2 modify this function for project archive
    public static final Vector getWeightedDefect(final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		Vector vt = null;
		ResultSet rs = null;
        String defectTable = "DEFECT";
//        if(Db.checkProjecIsArchive(prjID)){
//            defectTable = "DEFECT_ARCHIVE";
//        }        
		try {
			vt = new Vector();
			conn = ServerHelper.instance().getConnection();
			//Requirement review
			final WeightedDefectInfo info1 = new WeightedDefectInfo();
			info1.detection = "Requirement review";
			sql =
				"SELECT SUM(DS.WEIGHT) WEIGHTED_DEFECT, D.PROCESS_ID"
					+ " FROM "+ defectTable + " D,DEFECT_SEVERITY DS"
					+ " WHERE PROJECT_ID= ?"
					+ " AND DS.DEFS_ID = D.DEFS_ID"
					+ " AND D.DS_ID <> 6"  //not cancelled
					+ " AND D.QA_ID <>30"  // CR - Quality_Gate_Inspection
					+ " AND D.QA_ID <>40" // CR - BaseLine_Audit
					+ " AND D.QA_ID <>41" // CR - Other_Audit
					+ " AND D.AT_ID = 2 "   //Type of activity = Review
                    + " AND qa_id <> " + QCActivityInfo.CODE_REVIEW // Exclude code review defects 
					+ " AND D.WP_ID IN(" + WorkProductInfo.SRS
                    + "," + WorkProductInfo.URD
                    + "," + WorkProductInfo.REQUIREMENT_PROTOTYPE
                    + ") GROUP BY D.PROCESS_ID";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			int i = 0;
			int defect = -1;
			while (rs.next()) {
				i = rs.getInt("PROCESS_ID");
				defect = rs.getInt("WEIGHTED_DEFECT");
				switch (i) {
					case ProcessInfo.REQUIREMENT :
						info1.injRequirement = defect;
						break;
					case ProcessInfo.DESIGN :
						info1.injDesign = defect;
						break;
					case ProcessInfo.CODING :
						info1.injCoding = defect;
						break;
					default :
						info1.injOther += defect;
				}
			}
			rs.close();
			stm.close();
			info1.weightedDefect = info1.injCoding + info1.injOther + info1.injDesign + info1.injRequirement;
			vt.add(info1);
			// Design review
			final WeightedDefectInfo info2 = new WeightedDefectInfo();
			info2.detection = "Design review";
            sql =
                "SELECT SUM(DS.WEIGHT) WEIGHTED_DEFECT, D.PROCESS_ID"
                + "  FROM " + defectTable + " D,DEFECT_SEVERITY DS"
                + " WHERE PROJECT_ID=?"
                + " AND DS.DEFS_ID=D.DEFS_ID "
                + " AND D.DS_ID <>6" //not cancelled
				+ " AND D.QA_ID <>30"  // CR - Quality_Gate_Inspection
				+ " AND D.QA_ID <>40" // CR - BaseLine_Audit
				+ " AND D.QA_ID <>41" // CR - Other_Audit
                + " AND qa_id <> " + QCActivityInfo.CODE_REVIEW // Exclude code review defects
                + " AND ((D.QA_ID=" + QCActivityInfo.PROTOTYPE_REVIEW
                + "       AND D.WP_ID=" + WorkProductInfo.DESIGN_PROTOTYPE
                + "     ) OR (D.AT_ID=2 AND WP_ID IN(" + WorkProductInfo.ARCHITECTURAL_DESIGN
                + "," + WorkProductInfo.DETAILED_DESIGN
                + ")))"
                + " GROUP BY D.PROCESS_ID";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);
			rs = stm.executeQuery();
			while (rs.next()) {
				i = rs.getInt("PROCESS_ID");
				defect = rs.getInt("WEIGHTED_DEFECT");
				switch (i) {
					case ProcessInfo.REQUIREMENT:
						info2.injRequirement = defect;
						break;
					case ProcessInfo.DESIGN :
						info2.injDesign = defect;
						break;
					case ProcessInfo.CODING :
						info2.injCoding = defect;
						break;
					default :
						info2.injOther += defect;
				}
			}
			rs.close();
			stm.close();
			info2.weightedDefect = info2.injCoding + info2.injOther + info2.injDesign + info2.injRequirement;
			vt.add(info2);
			//Code review 
			//Unit test 
			//Integration test 
			//System test 
			//Acceptance test 
			final WeightedDefectInfo info3 = new WeightedDefectInfo();
			final WeightedDefectInfo info4 = new WeightedDefectInfo();
			final WeightedDefectInfo info5 = new WeightedDefectInfo();
			final WeightedDefectInfo info6 = new WeightedDefectInfo();
			final WeightedDefectInfo info7 = new WeightedDefectInfo();
			WeightedDefectInfo infotemp = null;
			int qaID = 0;
				sql =
					"SELECT SUM(DS.WEIGHT) WEIGHTED_DEFECT, D.PROCESS_ID"
						+ " FROM " + defectTable + "  D,DEFECT_SEVERITY DS"
						+ " WHERE PROJECT_ID=?"
						+ " AND DS.DEFS_ID=D.DEFS_ID "
						+ " AND D.DS_ID <>6" //not cancelled
						+" AND D.QA_ID=?"
						 + " GROUP BY D.PROCESS_ID";
			stm = conn.prepareStatement(sql);
			for (int j = 0; j < 5; j++) {
				switch (j) {
					case 0 :
						infotemp = info3;
						infotemp.detection = "Code review";
						qaID = QCActivityInfo.CODE_REVIEW;
						break;
					case 1 :
						infotemp = info4;
						infotemp.detection = "Unit test";
						qaID = QCActivityInfo.UNIT_TEST;
						break;
					case 2 :
						infotemp = info5;
						infotemp.detection = "Integration test";
						qaID = QCActivityInfo.INTEGRATION_TEST;
						break;
					case 3 :
						infotemp = info6;
						infotemp.detection = "System test";
						qaID = QCActivityInfo.SYSTEM_TEST;
						break;
					case 4 :
						infotemp = info7;
						infotemp.detection = "Acceptance test";
						qaID = QCActivityInfo.ACCEPTANCE_TEST;
						break;
				}
				stm.setLong(1, prjID);
				stm.setInt(2, qaID);
				rs = stm.executeQuery();
				while (rs.next()) {
					i = rs.getInt("PROCESS_ID");
					defect = rs.getInt("WEIGHTED_DEFECT");
					switch (i) {
						case ProcessInfo.REQUIREMENT :
							infotemp.injRequirement = defect;
							break;
						case ProcessInfo.DESIGN :
							infotemp.injDesign = defect;
							break;
						case ProcessInfo.CODING :
							infotemp.injCoding = defect;
							break;
						default :
							infotemp.injOther += defect;
					}
				}
				rs.close();
				infotemp.weightedDefect = infotemp.injCoding + infotemp.injOther + infotemp.injDesign + infotemp.injRequirement;
				vt.add(infotemp);
			}
			stm.close();
			//total
			final WeightedDefectInfo infoTotal = new WeightedDefectInfo();
			infoTotal.detection = "Total";
				sql =
					"SELECT SUM(DS.WEIGHT) WEIGHTED_DEFECT, D.PROCESS_ID"
						+ " FROM " + defectTable +  " D,DEFECT_SEVERITY DS"
						+ " WHERE PROJECT_ID="
						+ prjID
						+ " AND D.DS_ID <>6" //not cancelled
						+ " AND D.QA_ID <>30" // CR - Quality_Gate_Inspection
						+ " AND D.QA_ID <>40" // CR - BaseLine_Audit
						+ " AND D.QA_ID <>41" // CR - Other_Audit
						+ " AND DS.DEFS_ID=D.DEFS_ID " 
						+ " GROUP BY D.PROCESS_ID";
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			while (rs.next()) {
				i = rs.getInt("PROCESS_ID");
				defect = rs.getInt("WEIGHTED_DEFECT");
				switch (i) {
					case ProcessInfo.REQUIREMENT :
						infoTotal.injRequirement = defect;
						break;
					case ProcessInfo.DESIGN:
						infoTotal.injDesign = defect;
						break;
					case ProcessInfo.CODING :
						infoTotal.injCoding = defect;
						break;
					default :
						infoTotal.injOther += defect;
				}
			}
			infoTotal.weightedDefect = infoTotal.injCoding + infoTotal.injOther + infoTotal.injDesign + infoTotal.injRequirement;
			// Other acitivity
            final WeightedDefectInfo info8 = new WeightedDefectInfo();
			info8.detection = "Other";
			info8.injCoding =
				infoTotal.injCoding
					- info1.injCoding
					- info2.injCoding
					- info3.injCoding
					- info4.injCoding
					- info5.injCoding
					- info6.injCoding
					- info7.injCoding;
			info8.injDesign =
				infoTotal.injDesign
					- info1.injDesign
					- info2.injDesign
					- info3.injDesign
					- info4.injDesign
					- info5.injDesign
					- info6.injDesign
					- info7.injDesign;
			info8.injRequirement =
				infoTotal.injRequirement
					- info1.injRequirement
					- info2.injRequirement
					- info3.injRequirement
					- info4.injRequirement
					- info5.injRequirement
					- info6.injRequirement
					- info7.injRequirement;
			info8.injOther =
				infoTotal.injOther
					- info1.injOther
					- info2.injOther
					- info3.injOther
					- info4.injOther
					- info5.injOther
					- info6.injOther
					- info7.injOther;
			info8.weightedDefect =
				infoTotal.weightedDefect
					- info1.weightedDefect
					- info2.weightedDefect
					- info3.weightedDefect
					- info4.weightedDefect
					- info5.weightedDefect
					- info6.weightedDefect
					- info7.weightedDefect;
			vt.add(info8);
			vt.add(infoTotal);
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
	 * get defect distribution by severity
	 * @param: project ID
	 * @return: Vector
	 */
    //HuyNH2 modify this function for projec archive
	public static final Vector getSeverityDefect(final long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		Vector vt = null;
		ResultSet rs = null;
        String defectTable = "DEFECT";
//        if(Db.checkProjecIsArchive(prjID)){
//            defectTable = "DEFECT_ARCHIVE";
//        }        
		try {
			vt = new Vector();
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			final SeverityDefectInfo info1 = new SeverityDefectInfo("Fatal");
			final SeverityDefectInfo info2 = new SeverityDefectInfo("Serious");
			final SeverityDefectInfo info3 = new SeverityDefectInfo("Medium");
			final SeverityDefectInfo info4 = new SeverityDefectInfo("Cosmetic");
			final SeverityDefectInfo totalInfo = new SeverityDefectInfo("Total");
			int dsID = 0;
			int num = 0;
			int atID = 0;
				sql =
					"SELECT COUNT(d.DEFECT_ID) NUM,DS.DEFS_ID,D.AT_ID"
						+ " FROM " + defectTable + " D,  DEFECT_SEVERITY DS"
						+ " WHERE PROJECT_ID ="
						+ prjID
						+ " AND D.DS_ID <>6" //not cancelled
						+ " AND D.QA_ID <>30" // CR - Quality_Gate_Inspection
						+ " AND D.QA_ID <>40" // CR - BaseLine_Audit
						+ " AND D.QA_ID <>41" // CR - Other_Audit			
						+" AND DS.DEFS_ID=D.DEFS_ID" + " GROUP BY DS.DEFS_ID,D.AT_ID";
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				num = rs.getInt("NUM");
				dsID = rs.getInt("DEFS_ID");
				atID = rs.getInt("AT_ID");
				switch (dsID) {
					case 1 :
						if (atID == 2) {
							info1.nDreview += num;
						}
						else if (atID == 1) {
							info1.nDtest += num;
						}
						else
							info1.nDothers += num;
						break;
					case 2 :
						if (atID == 2) {
							info2.nDreview += num;
						}
						else if (atID == 1) {
							info2.nDtest += num;
						}
						else
							info2.nDothers += num;
						break;
					case 3 :
						if (atID == 2) {
							info3.nDreview += num;
						}
						else if (atID == 1) {
							info3.nDtest += num;
						}
						else
							info3.nDothers += num;
						break;
					case 4 :
						if (atID == 2) {
							info4.nDreview += num;
						}
						else if (atID == 1) {
							info4.nDtest += num;
						}
						else
							info4.nDothers += num;
						break;
					default :
						;
				}
			}
			totalInfo.nDreview = info1.nDreview + info2.nDreview + info3.nDreview + info4.nDreview;
			totalInfo.nDtest = info1.nDtest + info2.nDtest + info3.nDtest + info4.nDtest;
			totalInfo.nDothers = info1.nDothers + info2.nDothers + info3.nDothers + info4.nDothers;
			vt.add(info1);
			vt.add(info2);
			vt.add(info3);
			vt.add(info4);
			vt.add(totalInfo);
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
	 * get Defect distribution by type
	 * @param: project ID
	 * @return: Vector containing defect distribution by type
	 */
	public static final TypeDefectInfo[] getTypeDefect(final long prjID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		Vector vt = null;		 
		ResultSet rs = null;
		TypeDefectInfo[] typeDefectArray = null;
        String defectTable = "DEFECT";
//        if(Db.checkProjecIsArchive(prjID)){
//            defectTable = "DEFECT_ARCHIVE";
//        }		
		try {
			vt = new Vector();
			sql =
				"SELECT COUNT(*) NUM,-1 DT_ID,-1 DEFS_ID,'-1' DTNAME"
				+" FROM DEFECT_TYPE UNION"
					+ " SELECT COUNT(d.DEFECT_ID) NUM,DT.DT_ID,DS.DEFS_ID,DT.NAME DTNAME"
					+ " FROM " + defectTable + " D,  DEFECT_SEVERITY DS, DEFECT_TYPE DT"
					+ " WHERE PROJECT_ID(+) =?"
					+ " AND D.DT_ID(+)=DT.DT_ID"
					+ " AND D.DS_ID (+) <>6" //not cancelled
					+ " AND D.QA_ID <>30"  // CR - Quality_Gate_Inspection
					+ "	AND D.QA_ID <>40" // CR - BaseLine_Audit
					+ "	AND D.QA_ID <>41" // CR - Other_Audit
					+ " AND DS.DEFS_ID(+)=D.DEFS_ID" + " GROUP BY DT.DT_ID,DT.NAME, DS.DEFS_ID" + " ORDER BY DT_ID";
					
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prjID);		
			rs = stm.executeQuery();
			
			int N = 0;		//Number of DefectTypes.
			int num = 0;	//Number of each DefectTypes.
			int dtID = 0;	//stores DefectType.
			int dsID = 0;	//stores Defect Summary.
			int count = 0;
			int indexID = 0;	//determine index of diID in dtIDArray.					
			int[] dtIDArray = null;	//stores all defectTypes
			boolean check = true;
			
			rs.next();

			N = rs.getInt("NUM") + 1;
			
			typeDefectArray = new TypeDefectInfo[N];
			dtIDArray = new int[N];
			
			for (int i = 0; i < typeDefectArray.length; i++)
				typeDefectArray[i] = new TypeDefectInfo();
			
			while (rs.next()) {				
				num   = rs.getInt("NUM");							
				dsID  = rs.getInt("DEFS_ID");
				dtID  = rs.getInt("DT_ID") - 1;	
				
				//determine index of dtID in DefectType list describled by dtIDArray;
				//if defectType is has not existed in dtIDArray then it will create new index
				check = true;
				
				for(int i=0; i < count; i++)
					if(dtIDArray[i] == dtID){
						 indexID = i;
						 check = false;
						 break;
					}
					
				if(check) {
					indexID = count;
					dtIDArray[count] = dtID;
					count++;
				}
				
				if (typeDefectArray[indexID].type == null)
					typeDefectArray[indexID].type = rs.getString("DTNAME");
				
				//determine kinds of Defect then store its number of defects 
				// and calculate its weights
				switch (dsID) {
					case 1:
						typeDefectArray[indexID].fatal += num;
						typeDefectArray[indexID].weighted += num * 10;
						break;
						
					case 2:
						typeDefectArray[indexID].serious += num;
						typeDefectArray[indexID].weighted += num * 5;
						break;
						
					case 3:
						typeDefectArray[indexID].medium += num;
						typeDefectArray[indexID].weighted += num * 3;
						break;
						
					case 4:
						typeDefectArray[indexID].cosmetic += num;
						typeDefectArray[indexID].weighted += num;			
						break;
						
					default:
						;
				}
			}
			
			int lastRow = typeDefectArray.length - 1;		
			
			for (int i = 0; i < lastRow; i++) {
				typeDefectArray[lastRow].type = "Total";
				typeDefectArray[lastRow].fatal += typeDefectArray[i].fatal;
				typeDefectArray[lastRow].medium += typeDefectArray[i].medium;
				typeDefectArray[lastRow].serious += typeDefectArray[i].serious;
				typeDefectArray[lastRow].cosmetic += typeDefectArray[i].cosmetic;
				typeDefectArray[lastRow].weighted += typeDefectArray[i].weighted;
				vt.add(typeDefectArray[i]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return typeDefectArray;
			
		}
	}
	/**
	 * get defect for 4 categories requirement,design,coding and others
	 */
	public static DefectByProcessInfo[] getWeigthedDefectByOrigin(final long prjID) {
		DefectByProcessInfo reqProcess = new DefectByProcessInfo("Requirement");
		DefectByProcessInfo desProcess = new DefectByProcessInfo("Design");
		DefectByProcessInfo codProcess = new DefectByProcessInfo("Coding");
		DefectByProcessInfo othProcess = new DefectByProcessInfo("Other");
		DefectByProcessInfo tempProcess = null;
		DefectByProcessInfo[] returnValue = new DefectByProcessInfo[4];
		try {
			//GET PLANNED DATA
			UpdateDefectPlanInfo planInfo = getDetailPlannedDefects(prjID);
			reqProcess.planReview = planInfo.requirementPlanReview;
			reqProcess.rePlanReview = planInfo.requirementRePlanReview;
			reqProcess.planTest = planInfo.requirementPlanTest;
			reqProcess.rePlanTest = planInfo.requirementRePlanTest;
			
			reqProcess.noteReview = planInfo.requirementNoteReview;
			reqProcess.noteTest = planInfo.requirementNoteTest;
			
			
			desProcess.planReview = planInfo.designPlanReview;
			desProcess.rePlanReview = planInfo.designRePlanReview;
			desProcess.planTest = planInfo.designPlanTest;
			desProcess.rePlanTest = planInfo.designRePlanTest;
			codProcess.planReview = planInfo.codingPlanReview;
			codProcess.rePlanReview = planInfo.codingRePlanReview;
			codProcess.planTest = planInfo.codingPlanTest;
			codProcess.rePlanTest = planInfo.codingRePlanTest;
			othProcess.planReview = planInfo.otherPlanReview;
			othProcess.rePlanReview = planInfo.otherRePlanReview;
			othProcess.planTest = planInfo.otherPlanTest;
			othProcess.rePlanTest = planInfo.otherRePlanTest;
			
			/* Add by HaiMM - Start
			reqProcess.noteReview = planInfo.requirementNoteReview;
			reqProcess.noteTest = planInfo.requirementNoteTest;
			
			desProcess.noteReview = planInfo.designNoteReview;
			desProcess.noteTest = planInfo.designNoteTest;
			
			codProcess.noteReview = planInfo.codingNoteReview;
			codProcess.noteTest = planInfo.codingNoteTest;
			// Add by HaiMM - End */
						
			// GET NORMS
			//get lifecycle
			ProjectInfo prjInfo = Project.getProjectInfo(prjID);
			//get project size
			ProjectSizeInfo sizeInfo = new ProjectSizeInfo(prjID);
			NormInfo reviewEfficiency = Norms.getNormDetails(MetricDescInfo.REVIEW_EFFICIENCY, prjInfo);
			NormInfo testEfficiency = Norms.getNormDetails(MetricDescInfo.TEST_EFFICIENCY, prjInfo);
			NormInfo defectRate = Norms.getNormDetails(MetricDescInfo.DEFECT_RATE, prjInfo);
			java.sql.Date date = (prjInfo.getStartDate() == null) ? new java.sql.Date(new java.util.Date().getTime()) : prjInfo.getStartDate();
			final Vector defectPrcList = Norms.getDefectDstrByPrc(Parameters.FSOFT_WU, prjInfo.getLifecycleId(), date);
			double reqNorm = Double.NaN;
			double desNorm = Double.NaN;
			double codNorm = Double.NaN;
			double othNorm = Double.NaN;
			for (int i = 0; i < defectPrcList.size(); i++) {
				final NormRefInfo info = (NormRefInfo) defectPrcList.elementAt(i);
				if (info.lifecycleID == prjInfo.getLifecycleId()) {
					switch (info.prcID) {
						case MetricDescInfo.DEFECTS_FROM_REQUIREMENTS :
							reqNorm = info.value;
							break;
						case MetricDescInfo.DEFECTS_FROM_DESIGN :
							desNorm = info.value;
							break;
						case MetricDescInfo.DEFECTS_FROM_CODING :
							codNorm = info.value;
							break;
						case MetricDescInfo.DEFECTS_FROM_OTHER :
							othNorm = info.value;
							break;
					}
				}
			}
			double reviewFactor = sizeInfo.totalPlannedSize * reviewEfficiency.average * defectRate.average / 10000d;
			double testFactor = sizeInfo.totalPlannedSize * testEfficiency.average * defectRate.average / 10000d;
			reqProcess.normReview = reqNorm * reviewFactor;
			reqProcess.normTest = reqNorm * testFactor;
			desProcess.normReview = desNorm * reviewFactor;
			desProcess.normTest = desNorm * testFactor;
			othProcess.normReview = othNorm * reviewFactor;
			othProcess.normTest = othNorm * testFactor;
			codProcess.normReview = codNorm * reviewFactor;
			codProcess.normTest = codNorm * testFactor;
			reqProcess.actualReview = getWeigthedDefectByOriginAndActivity(prjID, "Requirement", "Review"); // 7. Modified by HaiMM
			reqProcess.actualTest = getWeigthedDefectByOriginAndActivity(prjID, "Requirement", "Test");
			desProcess.actualReview = getWeigthedDefectByOriginAndActivity(prjID, "Design", "Review");
			desProcess.actualTest = getWeigthedDefectByOriginAndActivity(prjID, "Design", "Test");
			codProcess.actualReview = getWeigthedDefectByOriginAndActivity(prjID, "Coding", "Review");
			codProcess.actualTest = getWeigthedDefectByOriginAndActivity(prjID, "Coding", "Test");
			othProcess.actualReview =
				getWeightedDefectByActivityType(prjID, "Review", Defect.NO_LEAKAGE)
					- reqProcess.actualReview
					- desProcess.actualReview
					- codProcess.actualReview;
			othProcess.actualTest =
				getWeightedDefectByActivityType(prjID, "Test", Defect.NO_LEAKAGE)
					- reqProcess.actualTest
					- desProcess.actualTest
					- codProcess.actualTest;
			for (int i = 0; i < 4; i++) {
				switch (i) {
					case 0 :
						tempProcess = reqProcess;
						break;
					case 1 :
						tempProcess = desProcess;
						break;
					case 2 :
						tempProcess = codProcess;
						break;
					case 3 :
						tempProcess = othProcess;
						break;
				}
				tempProcess.deviationReview =
					CommonTools.metricDeviation(tempProcess.planReview, tempProcess.rePlanReview, tempProcess.actualReview);
				tempProcess.deviationTest =
					CommonTools.metricDeviation(tempProcess.planTest, tempProcess.rePlanTest, tempProcess.actualTest);
				returnValue[i] = tempProcess;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	// Add by HaiMM
	public static DefectByProcessInfo[] getEstimateDefect(final long prjID) {
		DefectByProcessInfo reqReview = new DefectByProcessInfo("Requirement review");
		DefectByProcessInfo desReview = new DefectByProcessInfo("Design review");
		DefectByProcessInfo codReview = new DefectByProcessInfo("Coding review");

		DefectByProcessInfo unitTest = new DefectByProcessInfo("Unit testing");
		DefectByProcessInfo itergrationTest = new DefectByProcessInfo("Integration testing & System test");
		DefectByProcessInfo acceptanceTest = new DefectByProcessInfo("Acceptance testing");


		DefectByProcessInfo tempProcess = null;
		DefectByProcessInfo[] returnValue = new DefectByProcessInfo[6];
		try {
			UpdateDefectPlanInfo planInfo = getDetailPlannedDefects(prjID);
		
			reqReview.planReview = planInfo.requirementPlanReview;
			reqReview.noteReview = planInfo.requirementNoteReview;
			desReview.planReview = planInfo.designPlanReview;
			desReview.noteReview = planInfo.designNoteReview;
			codReview.planReview = planInfo.codingPlanReview;
			codReview.noteReview = planInfo.codingNoteReview;

			/* Mapping with the current Database
			 	unitTest = requirementTest;
				itergrationTest = designTest;
				acceptanceTest = codingTest
			 */  
			unitTest.planTest = planInfo.requirementPlanTest;
			unitTest.noteTest = planInfo.requirementNoteTest;

			itergrationTest.planTest = planInfo.designPlanTest;
			itergrationTest.noteTest = planInfo.designNoteTest;

			acceptanceTest.planTest = planInfo.codingPlanTest;
			acceptanceTest.noteTest = planInfo.codingNoteTest;
		
					
			for (int i = 0; i < 6; i++) {
				switch (i) {
					case 0 :
						tempProcess = reqReview;
						break;
					case 1 :
						tempProcess = desReview;
						break;
					case 2 :
						tempProcess = codReview;
						break;
					case 3 :
						tempProcess = unitTest;
						break;
					case 4 :
						tempProcess = itergrationTest;
						break;
					case 5 :
						tempProcess = acceptanceTest;
						break;
				}
				returnValue[i] = tempProcess;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	// Add by HaiMM
	public static final Vector getEstDefect(final long prjID) {
		 Connection conn = null;
		 Statement stm = null;
		 String sql = null;
		 Vector vt = null;
		 ResultSet rs = null;
		 try {
			 conn = ServerHelper.instance().getConnection();
			 stm = conn.createStatement();
			 sql = "SELECT * FROM EST_DEFECT WHERE PROJECT_ID = " + prjID; 
				  
			 rs = stm.executeQuery(sql);             
			 if (rs != null) {                   
				 vt = new Vector();
				 while (rs.next()) {                     
					 EstDefectInfo info = new EstDefectInfo();   
					 info.estDefectID = rs.getLong("EST_DEFECT_ID");                          
					 info.reviewTest = rs.getString("REVIEW_TEST");
					 info.target = Db.getDouble(rs,"TARGET");
					 info.basicEst = rs.getString("BASIC_EST");
					 
					 vt.add(info);
				 }
			 }
		 }
		 catch (SQLException e) {
			 e.printStackTrace();
		 }
		 finally {
			 ServerHelper.closeConnection(conn, stm, rs);
			 return vt;
		 }
	 }
	
	/**
	 * returns array of 2 double, the 1st is Weighted Planned defects,
	 * the second is Weighted Re-Planned defects
	 */
	public static double[] getPlannedDefects(final long prjID) {
		return getPlannedDefectsByActivity(prjID, null);
	}
	/**
	 * returns array of 2 double, the 1st is Weighted Planned defects,
	 * the second is Weighted Re-Planned defects
	 */
	public static double[] getPlannedDefectsByActivity(final long prjID, String processName) {
		double[] result = new double[2];
		result[0] = 0;
		result[1] = 0;
		double[] tempArray = new double[16];
		UpdateDefectPlanInfo defectInfo = getDetailPlannedDefects(prjID);
		tempArray[0] = defectInfo.requirementPlanReview;
		tempArray[1] = defectInfo.requirementRePlanReview;
		tempArray[2] = defectInfo.requirementPlanTest;
		tempArray[3] = defectInfo.requirementRePlanTest;
		tempArray[4] = defectInfo.designPlanReview;
		tempArray[5] = defectInfo.designRePlanReview;
		tempArray[6] = defectInfo.designPlanTest;
		tempArray[7] = defectInfo.designRePlanTest;
		tempArray[8] = defectInfo.codingPlanReview;
		tempArray[9] = defectInfo.codingRePlanReview;
		tempArray[10] = defectInfo.codingPlanTest;
		tempArray[11] = defectInfo.codingRePlanTest;
		tempArray[12] = defectInfo.otherPlanReview;
		tempArray[13] = defectInfo.otherRePlanReview;
		tempArray[14] = defectInfo.otherPlanTest;
		tempArray[15] = defectInfo.otherRePlanTest;
		boolean onePlanned = false;
		boolean oneRePlanned = false;
		int i = 0;
		boolean caseOption = false;
		if (processName != null)
			if (processName.toLowerCase().equals("review")) {
				i = 0;
				caseOption = true;
			}
			else if (processName.toLowerCase().equals("test")) {
				i = 2;
				caseOption = true;
			}
		if (caseOption) { //activity is defined
			for (; i < tempArray.length; i += 4) {
				if (!Double.isNaN(tempArray[i])) { //planned
					result[0] += tempArray[i];
					onePlanned = true;
					if (Double.isNaN(tempArray[i + 1])) {
						result[1] += tempArray[i]; //re-planned is null, use planned instead
					}
					else {
						result[1] += tempArray[i + 1];
						oneRePlanned = true;
					}
				}
			}
		}
		else { //activity is not defined return sum of all planned and replanned
			for (; i < tempArray.length; i += 2) {
				//planned
				if (!Double.isNaN(tempArray[i])) {
					result[0] += tempArray[i];
					onePlanned = true;
					if (Double.isNaN(tempArray[i + 1])) {
						result[1] += tempArray[i]; //re-planned is null, use planned instead
					}
					else {
						result[1] += tempArray[i + 1];
						oneRePlanned = true;
					}
				}
			}
		}
		//if ALL replanned are null then replan is null
		if (!oneRePlanned)
			result[1] = Double.NaN;
		if (!onePlanned)
			result[0] = Double.NaN;
		return result;
	}
	public static UpdateDefectPlanInfo getDetailPlannedDefects(final long prjID) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		UpdateDefectPlanInfo defectInfo = new UpdateDefectPlanInfo();
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"SELECT REQPLANREVIEWDEFECT, REQREPLANREVIEWDEFECT, DESPLANREVIEWDEFECT,"
					+ " DESREPLANREVIEWDEFECT, CODPLANREVIEWDEFECT,CODREPLANREVIEWDEFECT,"
					+ " OTHPLANREVIEWDEFECT,OTHREPLANREVIEWDEFECT, REQPLANTESTDEFECT,"
					+ " REQREPLANTESTDEFECT,DESPLANTESTDEFECT, DESREPLANTESTDEFECT,"
					+ " CODPLANTESTDEFECT,CODREPLANTESTDEFECT, OTHPLANTESTDEFECT, OTHREPLANTESTDEFECT,"
					+ " REQNOTEREVIEWDEFECT, DESNOTEREVIEWDEFECT, CODNOTEREVIEWDEFECT,"
					+ " REQNOTETESTDEFECT, DESNOTETESTDEFECT, CODNOTETESTDEFECT"
					+ " FROM PROJECT_PLAN WHERE PROJECT_ID=?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, prjID);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				defectInfo.requirementPlanReview = Db.getDouble(rs, "REQPLANREVIEWDEFECT");
				defectInfo.requirementRePlanReview = Db.getDouble(rs, "REQREPLANREVIEWDEFECT");
				defectInfo.requirementPlanTest = Db.getDouble(rs, "REQPLANTESTDEFECT");
				defectInfo.requirementRePlanTest = Db.getDouble(rs, "REQREPLANTESTDEFECT");
				defectInfo.designPlanReview = Db.getDouble(rs, "DESPLANREVIEWDEFECT");
				defectInfo.designRePlanReview = Db.getDouble(rs, "DESREPLANREVIEWDEFECT");
				defectInfo.designPlanTest = Db.getDouble(rs, "DESPLANTESTDEFECT");
				defectInfo.designRePlanTest = Db.getDouble(rs, "DESREPLANTESTDEFECT");
				defectInfo.codingPlanReview = Db.getDouble(rs, "CODPLANREVIEWDEFECT");
				defectInfo.codingRePlanReview = Db.getDouble(rs, "CODREPLANREVIEWDEFECT");
				defectInfo.codingPlanTest = Db.getDouble(rs, "CODPLANTESTDEFECT");
				defectInfo.codingRePlanTest = Db.getDouble(rs, "CODREPLANTESTDEFECT");
				defectInfo.otherPlanReview = Db.getDouble(rs, "OTHPLANREVIEWDEFECT");
				defectInfo.otherRePlanReview = Db.getDouble(rs, "OTHREPLANREVIEWDEFECT");
				defectInfo.otherPlanTest = Db.getDouble(rs, "OTHPLANTESTDEFECT");
				defectInfo.otherRePlanTest = Db.getDouble(rs, "OTHREPLANTESTDEFECT");
				// add by HaiMM - Start
				defectInfo.requirementNoteReview = rs.getString("REQNOTEREVIEWDEFECT");
				defectInfo.designNoteReview = rs.getString("DESNOTEREVIEWDEFECT");
				defectInfo.codingNoteReview = rs.getString("CODNOTEREVIEWDEFECT");
				defectInfo.requirementNoteTest = rs.getString("REQNOTETESTDEFECT");
				defectInfo.designNoteTest = rs.getString("DESNOTETESTDEFECT");
				defectInfo.codingNoteTest = rs.getString("CODNOTETESTDEFECT");
				// add by HaiMM - End
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return defectInfo;
		}
	}
	/**
	* The origin is based on Workproduct/process mapping (table workproduct)
	* As it says but be carefull, LEAKAGE DEFECTS ARE EXCLUDED
	**/
    //HuyNH2 add code for project archive.
	private static double getWeigthedDefectByOriginAndActivity(final long prjID, String origin, String activity) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		double result = Double.NaN;
        String defectTable = "DEFECT";
//        if(Db.checkProjecIsArchive(prjID)){
//            defectTable = "DEFECT_ARCHIVE";
//        }        
		try {
			sql =
					" SELECT SUM(DEFECT_SEVERITY.WEIGHT) WEIGHTED_DEFECT"
            			+ " FROM "+ defectTable +" , DEFECT_SEVERITY,PROCESS,ACTIVITY_TYPE, QC_ACTIVITY,WORKPRODUCT"
            			+ " WHERE "+ defectTable +".DEFS_ID = DEFECT_SEVERITY.DEFS_ID "
            			+ " AND "+ defectTable +".WP_ID=WORKPRODUCT.WP_ID"
            			+ " AND WORKPRODUCT.PROCESS=PROCESS.PROCESS_ID"
            			+ " AND UPPER(PROCESS.NAME)=?"
            			+ "	AND "+ defectTable +".PROJECT_ID = ?"
            			+ " AND "+ defectTable +".DS_ID <>6" //not cancelled
						+ " AND "+ defectTable +".QA_ID <>30" //Quality_Gate_Inspection
						+ " AND "+ defectTable +".QA_ID <>40" //BaseLine_Audit
						+ " AND "+ defectTable +".QA_ID <>41" //Other_Audit
                        + " AND "+ defectTable +".AT_ID=ACTIVITY_TYPE.AT_ID"
                		+ " AND UPPER(ACTIVITY_TYPE.NAME)=?"
                		+ " AND QC_ACTIVITY.QA_ID = "+ defectTable +".QA_ID"
                		+ " AND NOT(QC_ACTIVITY.QA_ID = 13 OR QC_ACTIVITY.QA_ID = 15 OR QC_ACTIVITY.QA_ID=22)";

            conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1, origin.trim().toUpperCase());
			stm.setLong(2, prjID);
			stm.setString(3, activity.trim().toUpperCase());
			rs = stm.executeQuery();
			if (rs.next()) {
				result = rs.getLong("WEIGHTED_DEFECT");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return result;
		}
	}
	
	/**
	 * first row include names, second row the values
	 * 
	 */
	public static PCBEffortDistribInfo[] getGroupWeigthedDefectsByOrigin(final long[] prjIDs) {
		PCBEffortDistribInfo[] result = new PCBEffortDistribInfo[4];
		try {
			double[] values = new double[result.length];
			double total = 0;
			for (int i = 0; i < result.length; i++) {
				result[i] = new PCBEffortDistribInfo();
			}
			result[0].name = "Requirement";
			result[1].name = "Design";
			result[2].name = "Coding";
			result[3].name = "Other";
			result[0].id = MetricDescInfo.DEFECTS_FROM_REQUIREMENTS;
			result[1].id = MetricDescInfo.DEFECTS_FROM_DESIGN;
			result[2].id = MetricDescInfo.DEFECTS_FROM_CODING;
			result[3].id = MetricDescInfo.DEFECTS_FROM_OTHER;
			for (int i = 0; i < result.length; i++) {
				values[i] = getGroupWeigthedDefectByOrigin(prjIDs, result[i].name);
				if (!Double.isNaN(values[i]))
					total += values[i];
			}
			if (total != 0)
				for (int i = 0; i < result.length; i++) {
					result[i].value = values[i] * 100d / total;
				}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return result;
		}
	}
	/**
	 * leakage included
	 * 
	 */
    //HuyNH2 add code for project archive
	public static double getGroupWeigthedDefectByOrigin(final long[] prjIDs, String origin) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		double result = Double.NaN;
		String idConstraint = null;
		try {
			if (prjIDs.length == 0)
				return 0d;
			idConstraint = ConvertString.arrayToString(prjIDs, ",");
				sql =
					" SELECT SUM(DEFECT_SEVERITY.WEIGHT) WEIGHTED_DEFECT"
						+ " FROM DEFECT , DEFECT_SEVERITY,PROCESS,WORKPRODUCT"
						+ " WHERE DEFECT.DEFS_ID = DEFECT_SEVERITY.DEFS_ID "
						+ " AND DEFECT.WP_ID=WORKPRODUCT.WP_ID"
						+ " AND DEFECT.DS_ID <>6" //not cancelled
	+" AND WORKPRODUCT.PROCESS=PROCESS.PROCESS_ID" + " AND UPPER(PROCESS.NAME)=?" + "	AND DEFECT.PROJECT_ID IN(" + idConstraint + ")";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1, origin.trim().toUpperCase());
			//stm.setString(2, idConstraint);
			rs = stm.executeQuery();
			if (rs.next()) {
				result = rs.getLong("WEIGHTED_DEFECT");
			}
        //HuyNH2 add code for project archive
//            sql =
//                " SELECT SUM(DEFECT_SEVERITY.WEIGHT) WEIGHTED_DEFECT"
//                    + " FROM DEFECT_ARCHIVE , DEFECT_SEVERITY,PROCESS,WORKPRODUCT"
//                    + " WHERE DEFECT_ARCHIVE.DEFS_ID = DEFECT_SEVERITY.DEFS_ID "
//                    + " AND DEFECT_ARCHIVE.WP_ID=WORKPRODUCT.WP_ID"
//                    + " AND DEFECT_ARCHIVE.DS_ID <>6" //not cancelled
//    +" AND WORKPRODUCT.PROCESS=PROCESS.PROCESS_ID" + " AND UPPER(PROCESS.NAME)=?" + "   AND DEFECT_ARCHIVE.PROJECT_ID IN(" + idConstraint + ")";
//            stm = conn.prepareStatement(sql);
//            stm.setString(1, origin.trim().toUpperCase());
//            rs = stm.executeQuery();
//            if (rs.next()) {
//                result += rs.getLong("WEIGHTED_DEFECT");
//            }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return result;
		}
	}
	public static boolean updateDefectPlan(final long prjID, UpdateDefectPlanInfo defectInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql =
				"UPDATE PROJECT_PLAN SET"
					+ " REQPLANREVIEWDEFECT=?,"
					+ " REQREPLANREVIEWDEFECT=?,"
					+ " REQPLANTESTDEFECT=?,"
					+ " REQREPLANTESTDEFECT=?,"
					+ " DESPLANREVIEWDEFECT=?,"
					+ " DESREPLANREVIEWDEFECT=?,"
					+ " DESPLANTESTDEFECT=?,"
					+ " DESREPLANTESTDEFECT=?,"
					+ " CODPLANREVIEWDEFECT=?,"
					+ " CODREPLANREVIEWDEFECT=?,"
					+ " CODPLANTESTDEFECT=?,"
					+ " CODREPLANTESTDEFECT=?,"
					+ " OTHPLANREVIEWDEFECT=?,"
					+ " OTHREPLANREVIEWDEFECT=?,"
					+ " OTHPLANTESTDEFECT=?,"
					+ " OTHREPLANTESTDEFECT =?,"
					// Modify by HaiMM: Add Note - Start
					+ " REQNOTEREVIEWDEFECT =?,"			
					+ " DESNOTEREVIEWDEFECT =?,"
					+ " CODNOTEREVIEWDEFECT =?,"
					+ " REQNOTETESTDEFECT =?,"
					+ " DESNOTETESTDEFECT =?,"
					+ " CODNOTETESTDEFECT =?"
					// Modify by HaiMM: Add Note - End
					
					+ " WHERE PROJECT_ID=?";
			stm = conn.prepareStatement(sql);
			Db.setDouble(stm, 1, defectInfo.requirementPlanReview);
			Db.setDouble(stm, 2, defectInfo.requirementRePlanReview);
			Db.setDouble(stm, 3, defectInfo.requirementPlanTest);
			Db.setDouble(stm, 4, defectInfo.requirementRePlanTest);
			Db.setDouble(stm, 5, defectInfo.designPlanReview);
			Db.setDouble(stm, 6, defectInfo.designRePlanReview);
			Db.setDouble(stm, 7, defectInfo.designPlanTest);
			Db.setDouble(stm, 8, defectInfo.designRePlanTest);
			Db.setDouble(stm, 9, defectInfo.codingPlanReview);
			Db.setDouble(stm, 10, defectInfo.codingRePlanReview);
			Db.setDouble(stm, 11, defectInfo.codingPlanTest);
			Db.setDouble(stm, 12, defectInfo.codingRePlanTest);
			Db.setDouble(stm, 13, defectInfo.otherPlanReview);
			Db.setDouble(stm, 14, defectInfo.otherRePlanReview);
			Db.setDouble(stm, 15, defectInfo.otherPlanTest);
			Db.setDouble(stm, 16, defectInfo.otherRePlanTest);
			// Modify by HaiMM: Add Note - Start
			stm.setString(17, defectInfo.requirementNoteReview);
			stm.setString(18, defectInfo.designNoteReview);
			stm.setString(19, defectInfo.codingNoteReview);
			stm.setString(20, defectInfo.requirementNoteTest);
			stm.setString(21, defectInfo.designNoteTest);
			stm.setString(22, defectInfo.codingNoteTest);
			// Modify by HaiMM: Add Note - End
			
			stm.setLong(23, prjID);
			return (stm.executeUpdate() == 1);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	
	public static boolean addEstDef(long prjId, EstDefectInfo inf) {
		Connection conn = null;
		String sql = null;
		PreparedStatement stmt = null;
		try {
			sql =
				"INSERT INTO est_defect (est_defect_id, project_id, review_test, target, basic_est) "
					+ " VALUES (NVL((SELECT MAX(est_defect_id)+1 FROM est_defect),1),?,?,?,?)";
			conn = ServerHelper.instance().getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, prjId);
			stmt.setString(2, inf.reviewTest);
			Db.setDouble(stmt, 3, inf.target);
			stmt.setString(4, inf.basicEst);
			return (stmt.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, null);
		}
	}
	
	public static final boolean doDeleteEstDef(final long estDefId) {
			return Db.delete(estDefId, "est_defect_id", "est_defect");
	}
	
	public static final boolean doDeleteReviewDefectProduct(final String idList) {
			return Db.updateBatch(idList,"MODULE_ID", "MODULE", "IS_DEFECT_REVIEW","0");
	}
	
	public static final boolean doDeleteTestDefectProduct(final String idList) {
			return Db.updateBatch(idList,"MODULE_ID", "MODULE", "IS_DEFECT_TEST","0");
	}
	
	public static void updateEstDef(final long prjID, EstDefectInfo inf){
		Connection conn = null;
		PreparedStatement stm = null;
		String sql=null;		
		long QA_ID;
		long QA_Type;		
		try {			
			DefectRateInfo defectRateInfo;
			conn = ServerHelper.instance().getConnection();			
			
			sql="UPDATE est_defect SET review_test=?, target=?, basic_est=? " +
				" WHERE PROJECT_ID = " + prjID + " AND " +
				" est_defect_id = " + inf.estDefectID ;
							
			stm=conn.prepareStatement(sql);
			stm.setString(1,inf.reviewTest);		
			Db.setDouble(stm,2,inf.target);
			stm.setString(3,inf.basicEst);			
			
			stm.executeUpdate();
			stm.close();																				   				
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, null, null);
		}
	}
	
	
	public static Vector getDefectProductPlan(long nProjectID){
	 Connection conn=null;	 
	 PreparedStatement stmt=null;
	 PreparedStatement stmt1=null;
	 PreparedStatement stmt2=null;
	 ResultSet rs=null;
	 ResultSet rsModule=null;	 
	 ResultSet rsWD=null;
	 String sql=null;
	 String sqlDefect=null;
	 String strQA=null;	 
	 double planValue=Double.NaN;
	 double total_P;
	 double total_R;
	 double total_A;
	 double total_D;	 	 
	 double temp_PR;
	 Vector result = new Vector();
     String defectTable = "DEFECT";
//     if(Db.checkProjecIsArchive(nProjectID)){
//         defectTable = "DEFECT_ARCHIVE";
//     }    	 		 
	 try{	 
		sql="SELECT DISTINCT DEFECT_PRODUCT_PLAN.MODULE_ID MODID," +
			" WORKPRODUCT.WP_ID WPID,WORKPRODUCT.NAME WPNAME," +
			" MODULE.NAME MODNAME, MODULE.ACTUAL_RELEASE_DATE RELEASEDATE" +
			" FROM DEFECT_PRODUCT_PLAN,WORKPRODUCT,MODULE" +
			" WHERE WORKPRODUCT.WP_ID=DEFECT_PRODUCT_PLAN.WP_ID" +
			" AND MODULE.MODULE_ID=DEFECT_PRODUCT_PLAN.MODULE_ID" +
			" AND DEFECT_PRODUCT_PLAN.PROJECT_ID=?" +
			" ORDER BY MODULE.ACTUAL_RELEASE_DATE";		 						
					
		conn = ServerHelper.instance().getConnection();
		stmt = conn.prepareStatement(sql);
		stmt.setLong(1, nProjectID);
		rsModule = stmt.executeQuery();		
		DefectProductPlanInfo defectPlanWPInfo;		
		while (rsModule.next()){
			defectPlanWPInfo=new DefectProductPlanInfo();					
			defectPlanWPInfo.wpID = rsModule.getLong("WPID");
			defectPlanWPInfo.moduleID = rsModule.getLong("MODID");					
			defectPlanWPInfo.wpName = rsModule.getString("WPNAME").trim();
			defectPlanWPInfo.moduleName = rsModule.getString("MODNAME").trim();						
		
			if (rsModule.getString("RELEASEDATE")==null || rsModule.getString("RELEASEDATE")==""){			
				defectPlanWPInfo.released=false;
			}else{
				defectPlanWPInfo.released=true;
			}	
			sql="SELECT DEFECT_PRODUCT_PLAN.MODULE_ID MODID,DEFECT_PRODUCT_PLAN.RATE_PLAN_VALUE RATEPLVALUE,DEFECT_PRODUCT_PLAN.PLAN_VALUE PLVALUE,DEFECT_PRODUCT_PLAN.REPLAN_VALUE REPLVALUE," +
				" DEFECT_PRODUCT_PLAN.QA_ID QAID,DEFECT_PRODUCT_PLAN.QA_TYPE QATYPE" +
				" FROM DEFECT_PRODUCT_PLAN" + 
				" WHERE DEFECT_PRODUCT_PLAN.PROJECT_ID=?" +
				" AND DEFECT_PRODUCT_PLAN.WP_ID=?" +
				" AND DEFECT_PRODUCT_PLAN.MODULE_ID=?" ;
				      	    
			stmt1 = conn.prepareStatement(sql);
			stmt1.setLong(1, nProjectID);
			stmt1.setLong(2,(long) defectPlanWPInfo.wpID);
			stmt1.setLong(3,(long) defectPlanWPInfo.moduleID);
			rs = stmt1.executeQuery();
			
			total_P=Double.NaN;
			total_R=Double.NaN;
			total_A=Double.NaN;
			total_D=Double.NaN;
			temp_PR=Double.NaN;						
			while (rs.next()){				
				sqlDefect="SELECT SUM(DEFECT_SEVERITY.WEIGHT) AS ACTUAL" +
					" FROM "+ defectTable +" INNER JOIN DEFECT_SEVERITY ON  "+ defectTable +".DEFS_ID=DEFECT_SEVERITY.DEFS_ID" +
					" WHERE PROJECT_ID="+(long)nProjectID+" AND WP_ID="+(long)defectPlanWPInfo.wpID+" AND MODULE_ID="+(long)defectPlanWPInfo.moduleID+" " +
					" AND DF.QA_ID <>30" +  // CR - Quality_Gate_Inspection
					" AND DF.QA_ID <>40" + // CR - BaseLine_Audit
					" AND DF.QA_ID <>41" + // CR - Other_Audit
					" AND DS_ID NOT IN (6) ";
													
				if ((int) rs.getDouble("QATYPE")==1){//Requirement review					
					defectPlanWPInfo.requirementReview[0]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"RATEPLVALUE"),true));							
					defectPlanWPInfo.requirementReview[1]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"PLVALUE"),true));
					defectPlanWPInfo.requirementReview[2]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"REPLVALUE"),true));
					
					sqlDefect+="AND ((QA_ID=20 AND PROCESS_ID=2) OR (QA_ID=23 AND WP_ID=21))";
					stmt2 = conn.prepareStatement(sqlDefect);
					rsWD=stmt2.executeQuery();				
					if (rsWD.next())																		
					defectPlanWPInfo.requirementReview[3]=rsWD.getInt("ACTUAL");
					
					planValue=Double.isNaN(defectPlanWPInfo.requirementReview[2])?defectPlanWPInfo.requirementReview[1]:defectPlanWPInfo.requirementReview[2];
					if (!Double.isNaN(planValue) && !Double.isNaN(defectPlanWPInfo.requirementReview[3]))					
						if (CommonTools.parseDouble(CommonTools.formatNumber(planValue,true))>0) 
						defectPlanWPInfo.requirementReview[4]=((defectPlanWPInfo.requirementReview[3]-planValue)/planValue)*100;													
						
					if (!Double.isNaN(defectPlanWPInfo.requirementReview[1]))
						total_P=Double.isNaN(total_P)?defectPlanWPInfo.requirementReview[1]:total_P+defectPlanWPInfo.requirementReview[1];
					if (!Double.isNaN(defectPlanWPInfo.requirementReview[2]))	
						total_R=Double.isNaN(total_R)?defectPlanWPInfo.requirementReview[2]:total_R+defectPlanWPInfo.requirementReview[2];
					if (!Double.isNaN(defectPlanWPInfo.requirementReview[3]))
						total_A=Double.isNaN(total_A)?defectPlanWPInfo.requirementReview[3]:total_A+defectPlanWPInfo.requirementReview[3];		
													
					stmt2.close();
					rsWD.close();							
				}else if ((int) rs.getDouble("QATYPE")==2){//Design review													
					defectPlanWPInfo.designReview[0]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"RATEPLVALUE"),true));
					defectPlanWPInfo.designReview[1]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"PLVALUE"),true));
					defectPlanWPInfo.designReview[2]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"REPLVALUE"),true));
										
					sqlDefect+="AND ((QA_ID=20 AND PROCESS_ID=3) OR (QA_ID=23 AND WP_ID=42))";
					stmt2 = conn.prepareStatement(sqlDefect);
					rsWD=stmt2.executeQuery();
					if (rsWD.next())																						
					defectPlanWPInfo.designReview[3]=rsWD.getInt("ACTUAL");
					
					planValue=Double.isNaN(defectPlanWPInfo.designReview[2])?defectPlanWPInfo.designReview[1]:defectPlanWPInfo.designReview[2];
					if (!Double.isNaN(planValue) && !Double.isNaN(defectPlanWPInfo.designReview[3]))					
						if (CommonTools.parseDouble(CommonTools.formatNumber(planValue,true))!=0)						
							defectPlanWPInfo.designReview[4]=((defectPlanWPInfo.designReview[3]-planValue)/planValue)*100;												
					
					if (!Double.isNaN(defectPlanWPInfo.designReview[1]))					
						total_P=Double.isNaN(total_P)?defectPlanWPInfo.designReview[1]:total_P+defectPlanWPInfo.designReview[1];
					if (!Double.isNaN(defectPlanWPInfo.designReview[2]))
						total_R=Double.isNaN(total_R)?defectPlanWPInfo.designReview[2]:total_R+defectPlanWPInfo.designReview[2];
					if (!Double.isNaN(defectPlanWPInfo.designReview[3]))
						total_A=Double.isNaN(total_A)?defectPlanWPInfo.designReview[3]:total_A+defectPlanWPInfo.designReview[3];
					stmt2.close();
					rsWD.close();
				}else if ((int) rs.getDouble("QATYPE")==3){//Others			 
					defectPlanWPInfo.others[0]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"RATEPLVALUE"),true));
					defectPlanWPInfo.others[1]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"PLVALUE"),true));
					defectPlanWPInfo.others[2]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"REPLVALUE"),true));			
					
					sqlDefect="SELECT SUM(DEFECT_SEVERITY.WEIGHT) AS ACTUAL" +
						" FROM " +defectTable+ " INNER JOIN DEFECT_SEVERITY ON  "+ defectTable +".DEFS_ID=DEFECT_SEVERITY.DEFS_ID" +
						" WHERE PROJECT_ID="+(long)nProjectID+" AND WP_ID="+(long)defectPlanWPInfo.wpID+" AND MODULE_ID="+(long)defectPlanWPInfo.moduleID +
						" AND DS_ID NOT IN (6)" +
						" AND NOT((QA_ID=20 AND PROCESS_ID=2) OR (QA_ID=23 AND WP_ID=21))" +
						" AND NOT((QA_ID=20 AND PROCESS_ID=3) OR (QA_ID=23 AND WP_ID=42))" +
						" AND NOT (QA_ID=14)" +
						" AND NOT (QA_ID=16)" +
						" AND NOT (QA_ID=17)" +
						" AND QA_ID<>"+QCActivityInfo.CODE_REVIEW +
						" AND QA_ID<>"+QCActivityInfo.UNIT_TEST +
						" AND QA_ID<>"+QCActivityInfo.INTEGRATION_TEST +
						" AND QA_ID<>"+QCActivityInfo.SYSTEM_TEST + 
						" AND QA_ID<>"+QCActivityInfo.OTHER_REVIEW +
						" AND QA_ID <>30" +  // CR - Quality_Gate_Inspection
						" AND QA_ID <>40" + // CR - BaseLine_Audit
						" AND QA_ID <>41"; // CR - Other_Audit
 
												   
					stmt2 = conn.prepareStatement(sqlDefect);
					rsWD=stmt2.executeQuery();
					if (rsWD.next())																						
					defectPlanWPInfo.others[3]=rsWD.getInt("ACTUAL");
					
					planValue=Double.isNaN(defectPlanWPInfo.others[2])?defectPlanWPInfo.others[1]:defectPlanWPInfo.others[2];
					if (!Double.isNaN(planValue) && !Double.isNaN(defectPlanWPInfo.others[3]))					
						if (CommonTools.parseDouble(CommonTools.formatNumber(planValue,true))>0) 
						defectPlanWPInfo.others[4]=((defectPlanWPInfo.others[3]-planValue)/planValue)*100;							
					
					if (!Double.isNaN(defectPlanWPInfo.others[1]))
						total_P=Double.isNaN(total_P)?defectPlanWPInfo.others[1]:total_P+defectPlanWPInfo.others[1];
					if (!Double.isNaN(defectPlanWPInfo.others[2]))
						total_R=Double.isNaN(total_R)?defectPlanWPInfo.others[2]:total_R+defectPlanWPInfo.others[2];
					if (!Double.isNaN(defectPlanWPInfo.others[3]))
						total_A=Double.isNaN(total_A)?defectPlanWPInfo.others[3]:total_A+defectPlanWPInfo.others[3];	
					stmt2.close();
					rsWD.close();
				}else if ((int) rs.getDouble("QATYPE")==4){//Other Test				 
					defectPlanWPInfo.otherTest[0]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"RATEPLVALUE"),true));
					defectPlanWPInfo.otherTest[1]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"PLVALUE"),true));
					defectPlanWPInfo.otherTest[2]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"REPLVALUE"),true));
					
					sqlDefect="SELECT SUM(DEFECT_SEVERITY.WEIGHT) AS ACTUAL" +
						" FROM " +defectTable+ " INNER JOIN DEFECT_SEVERITY ON  "+ defectTable +".DEFS_ID=DEFECT_SEVERITY.DEFS_ID" +
						" WHERE PROJECT_ID="+(long)nProjectID+" AND WP_ID="+(long)defectPlanWPInfo.wpID+" AND MODULE_ID="+(long)defectPlanWPInfo.moduleID +
						" AND DS_ID NOT IN (6)" +
						" AND ((QA_ID=14) OR (QA_ID=16 ) OR (QA_ID=17 ))";
					stmt2 = conn.prepareStatement(sqlDefect);
					rsWD=stmt2.executeQuery();
					if (rsWD.next())																						
					defectPlanWPInfo.otherTest[3]=rsWD.getInt("ACTUAL");
					
					planValue=Double.isNaN(defectPlanWPInfo.otherTest[2])?defectPlanWPInfo.otherTest[1]:defectPlanWPInfo.otherTest[2];
					if (!Double.isNaN(planValue) && !Double.isNaN(defectPlanWPInfo.otherTest[3]))					
						if (CommonTools.parseDouble(CommonTools.formatNumber(planValue,true))>0) 
						defectPlanWPInfo.otherTest[4]=((defectPlanWPInfo.otherTest[3]-planValue)/planValue)*100;							
					
					if (!Double.isNaN(defectPlanWPInfo.otherTest[1]))
						total_P=Double.isNaN(total_P)?defectPlanWPInfo.otherTest[1]:total_P+defectPlanWPInfo.otherTest[1];
					if (!Double.isNaN(defectPlanWPInfo.otherTest[2]))
						total_R=Double.isNaN(total_R)?defectPlanWPInfo.otherTest[2]:total_R+defectPlanWPInfo.otherTest[2];
					if (!Double.isNaN(defectPlanWPInfo.otherTest[3]))
						total_A=Double.isNaN(total_A)?defectPlanWPInfo.otherTest[3]:total_A+defectPlanWPInfo.otherTest[3];	
					stmt2.close();
					rsWD.close();
													

				}else{					
					switch ((int) rs.getDouble("QAID")){
						case QCActivityInfo.CODE_REVIEW: //Code review
								defectPlanWPInfo.codeReview[0]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"RATEPLVALUE"),true));
								defectPlanWPInfo.codeReview[1]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"PLVALUE"),true));
								defectPlanWPInfo.codeReview[2]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"REPLVALUE"),true));
							
							sqlDefect+="AND QA_ID="+QCActivityInfo.CODE_REVIEW;												
							stmt2 = conn.prepareStatement(sqlDefect);
							rsWD=stmt2.executeQuery();
							if (rsWD.next())																						
							defectPlanWPInfo.codeReview[3]=rsWD.getInt("ACTUAL");

							planValue=Double.isNaN(defectPlanWPInfo.codeReview[2])?defectPlanWPInfo.codeReview[1]:defectPlanWPInfo.codeReview[2];
							if (!Double.isNaN(planValue) && !Double.isNaN(defectPlanWPInfo.codeReview[3]))					
								if (CommonTools.parseDouble(CommonTools.formatNumber(planValue,true))>0) 
								defectPlanWPInfo.codeReview[4]=((defectPlanWPInfo.codeReview[3]-planValue)/planValue)*100;					
						
							if (!Double.isNaN(defectPlanWPInfo.codeReview[1]))
								total_P=Double.isNaN(total_P)?defectPlanWPInfo.codeReview[1]:total_P+defectPlanWPInfo.codeReview[1];
							if (!Double.isNaN(defectPlanWPInfo.codeReview[2]))
								total_R=Double.isNaN(total_R)?defectPlanWPInfo.codeReview[2]:total_R+defectPlanWPInfo.codeReview[2];
							if (!Double.isNaN(defectPlanWPInfo.codeReview[3]))
								total_A=Double.isNaN(total_A)?defectPlanWPInfo.codeReview[3]:total_A+defectPlanWPInfo.codeReview[3];	
							stmt2.close();
							rsWD.close();													    
							break;
						case QCActivityInfo.UNIT_TEST: //Unit test
							defectPlanWPInfo.unitTest[0]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"RATEPLVALUE"),true));
							defectPlanWPInfo.unitTest[1]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"PLVALUE"),true));
							defectPlanWPInfo.unitTest[2]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"REPLVALUE"),true));							
							
							sqlDefect+="AND QA_ID="+QCActivityInfo.UNIT_TEST;										
							stmt2 = conn.prepareStatement(sqlDefect);
							rsWD=stmt2.executeQuery();																						
							if (rsWD.next())
							defectPlanWPInfo.unitTest[3]=rsWD.getInt("ACTUAL");
							
							planValue=Double.isNaN(defectPlanWPInfo.unitTest[2])?defectPlanWPInfo.unitTest[1]:defectPlanWPInfo.unitTest[2];
							if (!Double.isNaN(planValue) && !Double.isNaN(defectPlanWPInfo.unitTest[3]))					
								if (CommonTools.parseDouble(CommonTools.formatNumber(planValue,true))>0) 
									defectPlanWPInfo.unitTest[4]=((defectPlanWPInfo.unitTest[3]-planValue)/planValue)*100;				
							
							if (!Double.isNaN(defectPlanWPInfo.unitTest[1]))
								total_P=Double.isNaN(total_P)?defectPlanWPInfo.unitTest[1]:total_P+defectPlanWPInfo.unitTest[1];
							if (!Double.isNaN(defectPlanWPInfo.unitTest[2]))
								total_R=Double.isNaN(total_R)?defectPlanWPInfo.unitTest[2]:total_R+defectPlanWPInfo.unitTest[2];
							if (!Double.isNaN(defectPlanWPInfo.unitTest[3]))
								total_A=Double.isNaN(total_A)?defectPlanWPInfo.unitTest[3]:total_A+defectPlanWPInfo.unitTest[3];	
							stmt2.close();
							rsWD.close();							
							break;
						case QCActivityInfo.INTEGRATION_TEST: //Integration test
							defectPlanWPInfo.integrationTest[0]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"RATEPLVALUE"),true));
							defectPlanWPInfo.integrationTest[1]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"PLVALUE"),true));
							defectPlanWPInfo.integrationTest[2]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"REPLVALUE"),true));							
							
							sqlDefect+="AND QA_ID="+QCActivityInfo.INTEGRATION_TEST;
							stmt2 = conn.prepareStatement(sqlDefect);
							rsWD=stmt2.executeQuery();																						
							if (rsWD.next())
							defectPlanWPInfo.integrationTest[3]=rsWD.getInt("ACTUAL");
							
							planValue=Double.isNaN(defectPlanWPInfo.integrationTest[2])?defectPlanWPInfo.integrationTest[1]:defectPlanWPInfo.integrationTest[2];
							if (!Double.isNaN(planValue) && !Double.isNaN(defectPlanWPInfo.integrationTest[3]))					
								if (CommonTools.parseDouble(CommonTools.formatNumber(planValue,true))>0) 
									defectPlanWPInfo.integrationTest[4]=((defectPlanWPInfo.integrationTest[3]-planValue)/planValue)*100;													
							
							if (!Double.isNaN(defectPlanWPInfo.integrationTest[1]))
								total_P=Double.isNaN(total_P)?defectPlanWPInfo.integrationTest[1]:total_P+defectPlanWPInfo.integrationTest[1];
							if (!Double.isNaN(defectPlanWPInfo.integrationTest[2]))	
								total_R=Double.isNaN(total_R)?defectPlanWPInfo.integrationTest[2]:total_R+defectPlanWPInfo.integrationTest[2];
							if (!Double.isNaN(defectPlanWPInfo.integrationTest[3]))	
								total_A=Double.isNaN(total_A)?defectPlanWPInfo.integrationTest[3]:total_A+defectPlanWPInfo.integrationTest[3];	
							stmt2.close();
							rsWD.close();							
							break;
						case QCActivityInfo.SYSTEM_TEST: //System test
							defectPlanWPInfo.systemTest[0]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"RATEPLVALUE"),true));
							defectPlanWPInfo.systemTest[1]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"PLVALUE"),true));
							defectPlanWPInfo.systemTest[2]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"REPLVALUE"),true));							
							
							sqlDefect+="AND QA_ID="+QCActivityInfo.SYSTEM_TEST;							
							stmt2 = conn.prepareStatement(sqlDefect);
							rsWD=stmt2.executeQuery();																						
							if (rsWD.next())
							defectPlanWPInfo.systemTest[3]=rsWD.getInt("ACTUAL");
							
							planValue=Double.isNaN(defectPlanWPInfo.systemTest[2])?defectPlanWPInfo.systemTest[1]:defectPlanWPInfo.systemTest[2];
							if (!Double.isNaN(planValue) && !Double.isNaN(defectPlanWPInfo.systemTest[3]))					
								if (CommonTools.parseDouble(CommonTools.formatNumber(planValue,true))>0) 
									defectPlanWPInfo.systemTest[4]=((defectPlanWPInfo.systemTest[3]-planValue)/planValue)*100;				
							
							if (!Double.isNaN(defectPlanWPInfo.systemTest[1]))								
								total_P=Double.isNaN(total_P)?defectPlanWPInfo.systemTest[1]:total_P+defectPlanWPInfo.systemTest[1];
							if (!Double.isNaN(defectPlanWPInfo.systemTest[2]))
								total_R=Double.isNaN(total_R)?defectPlanWPInfo.systemTest[2]:total_R+defectPlanWPInfo.systemTest[2];
							if (!Double.isNaN(defectPlanWPInfo.systemTest[3]))
								total_A=Double.isNaN(total_A)?defectPlanWPInfo.systemTest[3]:total_A+defectPlanWPInfo.systemTest[3];
							stmt2.close();
							rsWD.close();														
							break;
						case QCActivityInfo.OTHER_REVIEW:
							defectPlanWPInfo.otherReview[0]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"RATEPLVALUE"),true));
							defectPlanWPInfo.otherReview[1]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"PLVALUE"),true));
							defectPlanWPInfo.otherReview[2]=CommonTools.parseDouble(CommonTools.formatNumber(Db.getDouble(rs,"REPLVALUE"),true));
														
							sqlDefect+="AND QA_ID="+QCActivityInfo.OTHER_REVIEW;							
							stmt2 = conn.prepareStatement(sqlDefect);
							rsWD=stmt2.executeQuery();								
							if (rsWD.next())
								defectPlanWPInfo.otherReview[3]=rsWD.getInt("ACTUAL");
							if (!Double.isNaN(planValue) && !Double.isNaN(defectPlanWPInfo.otherReview[3]))					
							if (CommonTools.parseDouble(CommonTools.formatNumber(planValue,true))>0) 
								defectPlanWPInfo.otherReview[4]=((defectPlanWPInfo.otherReview[3]-planValue)/planValue)*100;				
							
							if (!Double.isNaN(defectPlanWPInfo.otherReview[1]))								
								total_P=Double.isNaN(total_P)?defectPlanWPInfo.otherReview[1]:total_P+defectPlanWPInfo.otherReview[1];
							if (!Double.isNaN(defectPlanWPInfo.otherReview[2]))
								total_R=Double.isNaN(total_R)?defectPlanWPInfo.otherReview[2]:total_R+defectPlanWPInfo.otherReview[2];
							if (!Double.isNaN(defectPlanWPInfo.otherReview[3]))
								total_A=Double.isNaN(total_A)?defectPlanWPInfo.otherReview[3]:total_A+defectPlanWPInfo.otherReview[3];
							stmt2.close();
							rsWD.close();
							break;
					}	
				}								
			}
			temp_PR=Double.isNaN(total_R)?total_P:total_R;
			if (CommonTools.parseDouble(CommonTools.formatNumber(temp_PR,true))>0)
				total_D=((total_A-temp_PR)/temp_PR)*100;
			
			defectPlanWPInfo.total[1]=total_P;
			defectPlanWPInfo.total[2]=total_R;
			defectPlanWPInfo.total[3]=total_A;
			defectPlanWPInfo.total[4]=total_D;
										
			result.add(defectPlanWPInfo);
		}				
	}catch (Exception e) {
			e.printStackTrace();
	}finally {
			ServerHelper.closeConnection(conn, stmt, rsModule);
			ServerHelper.closeConnection(conn, stmt1, rs);
	}
		return result;
    }
		
	public static Vector getDefectProductReplan(long ProjectID,Vector defectInfoVector){
		Connection conn=null;	 
		PreparedStatement stmt=null;
		Vector result = new Vector();
		ResultSet rs=null;
		String sql=null;
		double estimatedSize=Double.NaN;			
		double plannedWD=Double.NaN;
		double totalDeviationWD=Double.NaN;			
		double AverageWD=Double.NaN;	
		double tempTotal=Double.NaN;		
		try{
			DefectProductReplanInfo defectReplanInfo;					
			DefectProductPlanInfo defectInfo;
			conn = ServerHelper.instance().getConnection();
			//Caculate Average of all WD of product.							
			int countReleased=0;							
			for (int i=0;i<defectInfoVector.size();i++){				
				defectInfo=(DefectProductPlanInfo)defectInfoVector.elementAt(i);				
				if (defectInfo.released){									
					plannedWD=defectInfo.total[1];
					if (!Double.isNaN(defectInfo.total[2]))
						plannedWD=defectInfo.total[2];
						
					if (!Double.isNaN(defectInfo.total[4])){					
						totalDeviationWD=Double.isNaN(totalDeviationWD)?defectInfo.total[4]:totalDeviationWD + defectInfo.total[4];
					countReleased++;
				}						
			}
			}
			
			if (countReleased>0)
				AverageWD=totalDeviationWD/countReleased;							
									
			for (int i=0;i<defectInfoVector.size();i++){
				defectInfo=(DefectProductPlanInfo)defectInfoVector.elementAt(i);								
				defectReplanInfo=new DefectProductReplanInfo();															
				
				defectReplanInfo.wpID=defectInfo.wpID;
				defectReplanInfo.moduleID=defectInfo.moduleID;
				defectReplanInfo.wpName=defectInfo.wpName;
				defectReplanInfo.moduleName=defectInfo.moduleName;
				defectReplanInfo.released=defectInfo.released;
							
				//-----------------Actual-Forecast by QC activity---------------------------------------								
				//(Re-) Estimated size of product
				Vector modules=WorkProduct.getModuleSize(ProjectID, defectReplanInfo.moduleID);
				WPSizeInfo info  = (WPSizeInfo)modules.elementAt(0);
				estimatedSize=Double.isNaN(info.reestimatedSizeConv)?info.estimatedSizeConv:info.reestimatedSizeConv;				
				estimatedSize=CommonTools.parseDouble(CommonTools.formatNumber(estimatedSize,true));
				
				
				//Norm defect rate by product				
				NormInfo normDefectRate=getNormByProductType(ProjectID,defectReplanInfo.wpID,true);
				//Norm Norm Removal Efficiency by product				
				NormInfo normRemovalEfficiency=getNormByProductType(ProjectID,defectReplanInfo.wpID,false);				
							
				//Actual
				if (defectReplanInfo.released){
					defectReplanInfo.requirementReview[0]=defectInfo.requirementReview[3];  
					defectReplanInfo.designReview[0]=defectInfo.designReview[3];
					defectReplanInfo.codeReview[0]=defectInfo.codeReview[3];
					defectReplanInfo.unitTest[0]=defectInfo.unitTest[3];
					defectReplanInfo.integrationTest[0]=defectInfo.integrationTest[3];
					defectReplanInfo.systemTest[0]=defectInfo.systemTest[3];
					defectReplanInfo.others[0]=defectInfo.others[3];			
					// Add Other Review and Other Test
					defectReplanInfo.otherReview[0] = defectInfo.otherReview[3];
					defectReplanInfo.otherTest[0] = defectInfo.otherTest[3];
					// The End
				}else{//Forecast
					defectReplanInfo.requirementReview[0]=(estimatedSize*normDefectRate.average*(1+(AverageWD/100))-(defectInfo.requirementReview[1]*(1-normRemovalEfficiency.average/100)))*defectInfo.requirementReview[0]/100;										  
					defectReplanInfo.designReview[0]=(estimatedSize*normDefectRate.average*(1+(AverageWD/100))-(defectInfo.designReview[1]*(1-normRemovalEfficiency.average/100)))*defectInfo.designReview[0]/100;					 
					defectReplanInfo.codeReview[0]=(estimatedSize*normDefectRate.average*(1+(AverageWD/100))-(defectInfo.codeReview[1]*(1-normRemovalEfficiency.average/100)))*defectInfo.codeReview[0]/100;					
					defectReplanInfo.unitTest[0]=(estimatedSize*normDefectRate.average*(1+(AverageWD/100))-(defectInfo.unitTest[1]*(1-normRemovalEfficiency.average/100)))*defectInfo.unitTest[0]/100;					
					defectReplanInfo.integrationTest[0]=(estimatedSize*normDefectRate.average*(1+(AverageWD/100))-(defectInfo.integrationTest[1]*(1-normRemovalEfficiency.average/100)))*defectInfo.integrationTest[0]/100;					
					defectReplanInfo.systemTest[0]=(estimatedSize*normDefectRate.average*(1+(AverageWD/100))-(defectInfo.systemTest[1]*(1-normRemovalEfficiency.average/100)))*defectInfo.systemTest[0]/100;				
					defectReplanInfo.others[0]=(estimatedSize*normDefectRate.average*(1+(AverageWD/100))-(defectInfo.others[1]*(1-normRemovalEfficiency.average/100)))*defectInfo.others[0]/100;
					// Add Other Review and Other Test
					defectReplanInfo.otherReview[0] = (estimatedSize*normDefectRate.average*(1+(AverageWD/100))-(defectInfo.otherReview[1]*(1-normRemovalEfficiency.average/100)))*defectInfo.otherReview[0]/100;
					defectReplanInfo.otherTest[0] = (estimatedSize*normDefectRate.average*(1+(AverageWD/100))-(defectInfo.otherTest[1]*(1-normRemovalEfficiency.average/100)))*defectInfo.otherTest[0]/100;
					// The End
				}
				
				//CommonTools.parseDouble(CommonTools.formatNumber(
				defectReplanInfo.requirementReview[0]=CommonTools.parseDouble(CommonTools.formatNumber(defectReplanInfo.requirementReview[0],true));
				defectReplanInfo.designReview[0]=CommonTools.parseDouble(CommonTools.formatNumber(defectReplanInfo.designReview[0],true));
				defectReplanInfo.codeReview[0]=CommonTools.parseDouble(CommonTools.formatNumber(defectReplanInfo.codeReview[0],true));
				defectReplanInfo.unitTest[0]=CommonTools.parseDouble(CommonTools.formatNumber(defectReplanInfo.unitTest[0],true));
				defectReplanInfo.integrationTest[0]=CommonTools.parseDouble(CommonTools.formatNumber(defectReplanInfo.integrationTest[0],true));
				defectReplanInfo.systemTest[0]=CommonTools.parseDouble(CommonTools.formatNumber(defectReplanInfo.systemTest[0],true));
				defectReplanInfo.others[0]=CommonTools.parseDouble(CommonTools.formatNumber(defectReplanInfo.others[0],true));
				
				tempTotal=defectReplanInfo.requirementReview[0];
				if (!Double.isNaN(defectReplanInfo.designReview[0]))
					tempTotal=Double.isNaN(tempTotal)?defectReplanInfo.designReview[0]:tempTotal+defectReplanInfo.designReview[0];
				if (!Double.isNaN(defectReplanInfo.codeReview[0]))
					tempTotal=Double.isNaN(tempTotal)?defectReplanInfo.codeReview[0]:tempTotal+defectReplanInfo.codeReview[0];
				if (!Double.isNaN(defectReplanInfo.unitTest[0]))	
					tempTotal=Double.isNaN(tempTotal)?defectReplanInfo.unitTest[0]:tempTotal+defectReplanInfo.unitTest[0];
				if (!Double.isNaN(defectReplanInfo.integrationTest[0]))					
					tempTotal=Double.isNaN(tempTotal)?defectReplanInfo.integrationTest[0]:tempTotal+defectReplanInfo.integrationTest[0];
				if (!Double.isNaN(defectReplanInfo.systemTest[0]))	
					tempTotal=Double.isNaN(tempTotal)?defectReplanInfo.systemTest[0]:tempTotal+defectReplanInfo.systemTest[0];
				if (!Double.isNaN(defectReplanInfo.others[0]))	
					tempTotal=Double.isNaN(tempTotal)?defectReplanInfo.others[0]:tempTotal+defectReplanInfo.others[0];
				defectReplanInfo.total[0]=tempTotal;						
								
				//-----------------Replan by QC activity------------------------------------------------
			    defectReplanInfo.requirementReview[1]=defectInfo.requirementReview[2];  
			    defectReplanInfo.designReview[1]=defectInfo.designReview[2];
			    defectReplanInfo.codeReview[1]=defectInfo.codeReview[2];
			    defectReplanInfo.unitTest[1]=defectInfo.unitTest[2];
			    defectReplanInfo.integrationTest[1]=defectInfo.integrationTest[2];
			    defectReplanInfo.systemTest[1]=defectInfo.systemTest[2];
			    defectReplanInfo.others[1]=defectInfo.others[2];
			    defectReplanInfo.total[1]=defectInfo.total[2];
				
				result.add(defectReplanInfo);												
			}									
			
		}catch (Exception e) {
				e.printStackTrace();
		}finally {			
			ServerHelper.closeConnection(conn, null, null);
		}
		return result;		
	}
		
	public static Vector getDefectRateReplan(long ProjectID,Vector defectInfoVector){
		Connection conn=null;	 
		PreparedStatement stmt=null;
		Vector result = new Vector();
		ResultSet rs=null;
		String sql=null;
		double re_PlanSize=Double.NaN;								
		try{
			DefectRateInfo defectRateInfo;					
			DefectProductPlanInfo defectInfo;
			NormInfo normdefectRate;
			conn = ServerHelper.instance().getConnection();
			//Caculate Average of all WD of product.							
			int countReleased=0;							
			for (int i=0;i<defectInfoVector.size();i++){				
				defectInfo=(DefectProductPlanInfo)defectInfoVector.elementAt(i);
				defectRateInfo=new DefectRateInfo();
				
				Vector modules=WorkProduct.getModuleSize(ProjectID, defectInfo.moduleID);
				WPSizeInfo info  = (WPSizeInfo)modules.elementAt(0);
				
				defectRateInfo.wpID=defectInfo.wpID;
				defectRateInfo.moduleID=defectInfo.moduleID;
				defectRateInfo.wpName=defectInfo.wpName;
				defectRateInfo.moduleName=defectInfo.moduleName;
				defectRateInfo.released=defectInfo.released;		
								
				//Get Norm
				normdefectRate=getNormByProductType(ProjectID,defectRateInfo.wpID,true);
				defectRateInfo.norm=normdefectRate.average;					
							
				//Get replan, plan and note defect rate from module.
				sql="SELECT REPLAN_DEFECT_RATE, NOTE_DEFECT_RATE " +
					"FROM MODULE WHERE PROJECT_ID=? AND WP_ID=? AND MODULE_ID=?";
					
				stmt = conn.prepareStatement(sql);
				stmt.setLong(1, ProjectID);
				stmt.setLong(2, defectRateInfo.wpID);
				stmt.setLong(3, defectRateInfo.moduleID);
				rs=stmt.executeQuery();

				if (rs.next()){											
					defectRateInfo.replan=Db.getDouble(rs, "REPLAN_DEFECT_RATE");						
					defectRateInfo.note=rs.getString("NOTE_DEFECT_RATE");
					re_PlanSize=info.reestimatedSizeConv;					
					if (Double.isNaN(re_PlanSize)){
						re_PlanSize=info.estimatedSizeConv;	
					}
					
					
					re_PlanSize=CommonTools.parseDouble(CommonTools.formatNumber(re_PlanSize,true));
					if (!Double.isNaN(re_PlanSize) && re_PlanSize>0) 
						defectRateInfo.plan=defectInfo.total[1]/re_PlanSize;																											
				}									
				
				if (defectRateInfo.released){
					if (CommonTools.parseDouble(CommonTools.formatNumber(info.actualSizeConv,true))>0)
						defectRateInfo.actual=defectInfo.total[3]/info.actualSizeConv;										
				}else{
					defectRateInfo.actual=Double.NaN;
				}
								
				result.add(defectRateInfo);					
				ServerHelper.closeConnection(null, stmt, rs);										
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {			
			ServerHelper.closeConnection(conn, null, null);
		}
		
		return result;
	}		
		
	public static Vector getproductDefectDevTracking(long ProjectID,Vector defectProductPlanVector){
		Connection conn=null;	 
		PreparedStatement stmt=null;
		Vector result = new Vector();
		ResultSet rs=null;
		String sql=null;								
		double totalWDfound=Double.NaN;		 	
		try{
			DefectProductPlanInfo defectInfo;
			DefectDevTrackingProductInfo productDefDevTrackingInfo;
			conn = ServerHelper.instance().getConnection();
			for(int i=0;i<defectProductPlanVector.size();i++){		
				defectInfo=(DefectProductPlanInfo)defectProductPlanVector.elementAt(i);						
				productDefDevTrackingInfo=new DefectDevTrackingProductInfo();
			
				productDefDevTrackingInfo.moduleID=defectInfo.moduleID;
				productDefDevTrackingInfo.moduleName=defectInfo.moduleName;
				productDefDevTrackingInfo.released=defectInfo.released;					
				
								
				//Caculate Product Defect Found Deviation Value
					totalWDfound=getTotalWDByProduct(ProjectID,defectInfo.moduleID,true); // 6. Modify by HaiMM				
					productDefDevTrackingInfo.productDefectFoundDeviation=CommonTools.metricDeviation(defectInfo.total[1],defectInfo.total[2],totalWDfound);															
								
			  	//Caculate Product Defect Removed Deviation Value					
					productDefDevTrackingInfo.productDefectRemovedDeviation=getDefectRemovedDeviation(ProjectID,defectInfo.wpID,defectInfo.moduleID,defectInfo.total[1],defectInfo.total[2]);					
				
				//get reasions/actions				 
				sql="SELECT action, reasion FROM module " +
					"WHERE project_id=? AND module_id=?";
					
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, ProjectID);
				stmt.setLong(2,	defectInfo.moduleID);
			rs=stmt.executeQuery();
								
				if (rs.next()){
					productDefDevTrackingInfo.actions=rs.getString("action");
					productDefDevTrackingInfo.reasions=rs.getString("reasion");
				}
				ServerHelper.closeConnection(null, stmt, rs);
							
				result.add(productDefDevTrackingInfo);
			}		
		}catch (Exception e) {
					e.printStackTrace();
		}finally {			
			ServerHelper.closeConnection(conn, null, null);
		}
				
		return result;
	}
	
	public static boolean updateProductDefectDevTracking(long ProjectID,Vector defectDevTrackingVector,String strReasion[],String strAction[]){		
		Connection conn=null;	
		int n=0;		 	
		try{
			conn = ServerHelper.instance().getConnection();
			DefectDevTrackingProductInfo productTrackingInfo;			
			for (int i=0;i<defectDevTrackingVector.size();i++){
				productTrackingInfo=(DefectDevTrackingProductInfo)defectDevTrackingVector.elementAt(i);
				//if (!productTrackingInfo.released){
					if (!updateReasion_ActionInfo(ProjectID,productTrackingInfo,strReasion[n],strAction[n],conn))						
						return false;			
				
					n++;
				//}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;								
		}finally {			
			ServerHelper.closeConnection(conn, null, null);
		}

		return true;	
	}
	
	public static boolean updateReasion_ActionInfo(long ProjectID,DefectDevTrackingProductInfo defectTrackingInfo,String strReasion,String strAction,final Connection conn){		 
		PreparedStatement stmt=null;		
		String sql=null;								 	
		try{			
			sql="UPDATE MODULE SET REASION=?, ACTION=? " +
				"WHERE PROJECT_ID="+ProjectID+" AND MODULE_ID="+defectTrackingInfo.moduleID;		
			
				stmt = conn.prepareStatement(sql);
			stmt.setString(1, strReasion.trim());
			stmt.setString(2, strAction.trim());			
			if (stmt.executeUpdate() != 1)
				return false;	

		}catch (Exception e) {
				e.printStackTrace();
				return false;
		}finally {			
			ServerHelper.closeConnection(null, stmt, null);
				}									
			
		return true;			
			}
	
	//HuyNH2 modify this function
    public static double getTotalWDByProduct(long projectID,long moduleID,boolean include_leakage){		
		Connection conn=null;	 
		PreparedStatement stmt=null;	
		ResultSet rs=null;	
		String sql=null;	
		double returnValue=Double.NaN;
        String defectTable = "DEFECT";
//        if(Db.checkProjecIsArchive(projectID)){
//            defectTable = "DEFECT_ARCHIVE";
//        }        
		try{
			conn = ServerHelper.instance().getConnection();
			sql =" SELECT SUM(B.WEIGHT) TOTAL_WEIGHTED_DEFECT "
					+ " FROM "+ defectTable +" A, DEFECT_SEVERITY B "
					+ " WHERE A.DEFS_ID = B.DEFS_ID "
					+ " AND A.PROJECT_ID = ? AND A.MODULE_ID= ? "
					+ " AND DF.QA_ID <>30 " // CR - Quality_Gate_Inspection
					+ " AND DF.QA_ID <>40 " // CR - BaseLine_Audit
					+ " AND DF.QA_ID <>41 " // CR - Other_Audit
					+ " AND A.DS_ID <>6 "; //not cancelled
					
			if (!include_leakage) //exclude leakage
				sql+=" AND A.QA_ID <> 13 AND A.QA_ID <> 15 AND A.QA_ID<>22";
						
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, projectID);
			stmt.setLong(2, moduleID);
			rs=stmt.executeQuery();	

			if (rs.next())
				returnValue=Db.getDouble(rs,"TOTAL_WEIGHTED_DEFECT");
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {			
			ServerHelper.closeConnection(conn, stmt, rs);
		}
	
		return returnValue;
	}		
				
	public static double getDefectRemovedDeviation(long projectID,long wpID,long moduleID,double plannedWD,double replannedWD){
		double returnValue=Double.NaN;
		double re_plannedWD=Double.NaN;		
		double actualRemovedDefect=Double.NaN;
		double tobeRemovedDefect=Double.NaN;
		double committedLeakage=Double.NaN; 
		double normDefectremovalefficiency;
		try{
			NormInfo normInfo;			
			//Caculate Actual Removed Defect
			actualRemovedDefect=getTotalWDByProduct(projectID,moduleID,false);
			//Get norm.Defect removed efficiency
			normInfo=getNormByProductType(projectID,wpID,false);
			normDefectremovalefficiency=normInfo.average;				
			committedLeakage=plannedWD*(1 - normDefectremovalefficiency);
			//Caculate To be Removed Defect
			re_plannedWD=Double.isNaN(replannedWD)? plannedWD:replannedWD;			
			tobeRemovedDefect=re_plannedWD-committedLeakage;
						
			if (!Double.isNaN(tobeRemovedDefect) && tobeRemovedDefect!=0)
				returnValue=(actualRemovedDefect-tobeRemovedDefect)*100d/tobeRemovedDefect;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnValue;	
	}
	
					
	public static Vector getDefectsByProcess(long nProjectID) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql;
		Vector result = new Vector();
		boolean atLeastOneModule = false;
		boolean atLeastOneWP = false;
		try {
			//needed for defect planning by WP
			Vector wpList = WorkProduct.getWPAndSizeList(nProjectID);
			final Vector moduleList = WorkProduct.getModuleList(nProjectID);
				sql =
					"SELECT  VAL,QC,WPNAME,WPID,MODNAME,MODID"
						+ " FROM ("
						+ " SELECT SUM(VALUE) VAL, QC_ACTIVITY.QA_ID QC,WORKPRODUCT.NAME WPNAME,"
						+ " WORKPRODUCT.WP_ID WPID,MODULE.NAME MODNAME, MODULE.MODULE_ID MODID,QC_ACTIVITY.NAME QCNAME,1 "
						+ " FROM DEFECT_PLAN, QC_ACTIVITY,WORKPRODUCT,MODULE"
						+ " WHERE QC_ACTIVITY.QA_ID=DEFECT_PLAN.QA_ID"
						+ " AND WORKPRODUCT.WP_ID=NVL(DEFECT_PLAN.WP_ID, (SELECT MODULE.WP_ID FROM MODULE WHERE MODULE.MODULE_ID=DEFECT_PLAN.MODULE_ID))"
						+ " AND MODULE.MODULE_ID(+)=DEFECT_PLAN.MODULE_ID"
						+ " AND DEFECT_PLAN.PROJECT_ID=?"
						+ " GROUP BY WORKPRODUCT.NAME,WORKPRODUCT.WP_ID,MODULE.NAME,MODULE.MODULE_ID ,QC_ACTIVITY.NAME,QC_ACTIVITY.QA_ID"
						+ " UNION" //Work products not in defect plan (no planning by QC activity)
		+" SELECT 0 VAL, null QC,WORKPRODUCT.NAME WPNAME,"
			+ " WORKPRODUCT.WP_ID WPID,NULL MODNAME, NULL MODID, null QCNAME,2 "
			+ " FROM WP_SIZE,WORKPRODUCT"
			+ " WHERE WP_SIZE.PLANNED_DEFECT IS NOT NULL"
			+ " AND WP_SIZE.WP_ID = WORKPRODUCT.WP_ID"
			+ " AND WP_SIZE.PROJECT_ID=?"
			+ " UNION" //Modules not in defect plan (no planning by QC activity)
	+" SELECT 0 VAL, null QC,WORKPRODUCT.NAME WPNAME,"
		+ " WORKPRODUCT.WP_ID WPID,MODULE.NAME MODNAME, MODULE.MODULE_ID MODID, null QCNAME,3 "
		+ " FROM MODULE,WORKPRODUCT"
		+ " WHERE MODULE.PLANNED_DEFECT IS NOT NULL"
		+ " AND WORKPRODUCT.WP_ID=MODULE.WP_ID"
		+ " AND MODULE.PROJECT_ID = ?"
		+ " )"
		+ " ORDER BY WPID, NVL(MODNAME,' '),QCNAME";
			// 1 if WP_ID is null, then it is module, we get the mapping from module table
			//2 we use NVL in order to display first the WP without module
			conn = ServerHelper.instance().getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, nProjectID);
			stmt.setLong(2, nProjectID);
			stmt.setLong(3, nProjectID);
			rs = stmt.executeQuery();
			double prevWP = -1;
			double prevModule = -1;
			double currWP;
			double currModule;
			double qcID;
			double value;
			DefectPlanInfo totalInfoWP = new DefectPlanInfo();
			//total for work products
			DefectPlanInfo totalInfoModules = new DefectPlanInfo();
			//total for Modules
			DefectPlanInfo totalInfo;
			DefectPlanInfo prevDefectInfo = null;
			while (rs.next()) {
				currWP = Db.getDouble(rs, "WPID");
				currModule = Db.getDouble(rs, "MODID");
				//can be null
				if (Double.isNaN(currModule)) {
					//it's a work product
					totalInfo = totalInfoWP;
					atLeastOneWP = true;
				}
				else { //it's a module
					totalInfo = totalInfoModules;
					atLeastOneModule = true;
				}
				qcID = Db.getDouble(rs, "QC");
				value = Db.getDouble(rs, "VAL");
				DefectPlanInfo defectInfo;
				if (((currWP == prevWP) || (Double.isNaN(currWP) && Double.isNaN(prevWP)))
					&& ((currModule == prevModule) || (Double.isNaN(currModule) && Double.isNaN(prevModule)))) { //!! NaN != NaN
					//same module we merge
					defectInfo = prevDefectInfo;
				}
				else {
					defectInfo = new DefectPlanInfo();
					defectInfo.wpName = rs.getString("WPNAME").trim();
					defectInfo.moduleName = rs.getString("MODNAME").trim();
					if (Double.isNaN(currModule)) //it's a work product
						//get planned defects for Work Product
						for (int i = 0; i < wpList.size(); i++) {
							WorkProductInfo wpInfo = (WorkProductInfo) wpList.elementAt(i);
							if (wpInfo.workProductID == currWP) {
								defectInfo.plannedWDefect = wpInfo.plannedDefects;
								defectInfo.rePlannedWDefect = wpInfo.rePlannedDefects;
								break;
							}
						}
					else { //it's a module
						//get planned defects for modules
						for (int i = 0; i < moduleList.size(); i++) {
							WPSizeInfo moduleInfo = (WPSizeInfo) moduleList.elementAt(i);
							if (moduleInfo.moduleID == currModule) {
								defectInfo.plannedWDefect = moduleInfo.plannedDefects;
								defectInfo.rePlannedWDefect = moduleInfo.rePlannedDefects;
								break;
							}
						}
					}
					if (!Double.isNaN(defectInfo.plannedWDefect)) {
						if (Double.isNaN(totalInfo.plannedWDefect))
							totalInfo.plannedWDefect = 0;
						totalInfo.plannedWDefect += defectInfo.plannedWDefect;
					}
					if (!Double.isNaN(defectInfo.rePlannedWDefect)) {
						if (Double.isNaN(totalInfo.rePlannedWDefect))
							totalInfo.rePlannedWDefect = 0;
						totalInfo.rePlannedWDefect += defectInfo.rePlannedWDefect;
					}
					result.add(defectInfo);
				}
				if (!Double.isNaN(qcID))
					switch ((int) qcID) {
						case 20 :
							defectInfo.documentReview = value;
							if (Double.isNaN(totalInfo.documentReview))
								totalInfo.documentReview = 0;
							totalInfo.documentReview += value;
							break;
						case 23 :
							defectInfo.prototypeReview = value;
							if (Double.isNaN(totalInfo.prototypeReview))
								totalInfo.prototypeReview = 0;
							totalInfo.prototypeReview += value;
							break;
						case 21 :
							defectInfo.codeReview = value;
							if (Double.isNaN(totalInfo.codeReview))
								totalInfo.codeReview = 0;
							totalInfo.codeReview += value;
							break;
						case 10 :
							defectInfo.unitTest = value;
							if (Double.isNaN(totalInfo.unitTest))
								totalInfo.unitTest = 0;
							totalInfo.unitTest += value;
							break;
						case 11 :
							defectInfo.integrationTest = value;
							if (Double.isNaN(totalInfo.integrationTest))
								totalInfo.integrationTest = 0;
							totalInfo.integrationTest += value;
							break;
						case 12 :
							defectInfo.systemTest = value;
							if (Double.isNaN(totalInfo.systemTest))
								totalInfo.systemTest = 0;
							totalInfo.systemTest += value;
							break;
						case 13 :
							defectInfo.acceptanceTest = value;
							if (Double.isNaN(totalInfo.acceptanceTest))
								totalInfo.acceptanceTest = 0;
							totalInfo.acceptanceTest += value;
							break;
						default :
							if (Double.isNaN(defectInfo.others))
								defectInfo.others = 0;
							defectInfo.others += value;
							break;
					}
				defectInfo.total += value;
				totalInfo.total += value;
				prevDefectInfo = defectInfo;
				defectInfo.wpID = currWP;
				defectInfo.moduleID = currModule;
				prevWP = currWP;
				prevModule = currModule;
			} //Add a TOTAL line at the end of table
			if (result.size() > 1) {
				if (atLeastOneWP) {
					totalInfoWP.wpName = "All work products";
					totalInfoWP.moduleName = "";
					result.add(totalInfoWP);
				}
				if (atLeastOneModule) {
					totalInfoModules.wpName = "";
					totalInfoModules.moduleName = "All products";
					result.add(totalInfoModules);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stmt, rs);
		}
		return result;
	}
		
	/**
	 * Update Defect product planning
	 *  
	 **/	
	public static boolean updateDefectProductPlan(final long prjID, DefectPlanInfo defectInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		PreparedStatement stmInsert = null;
		try {
			if ((defectInfo.moduleID == 0) || Double.isNaN(defectInfo.moduleID)) {
				defectInfo.moduleID = Double.NaN; //if NaN
				updateWPDefectPlan(prjID, defectInfo);
			}
			else {
				defectInfo.wpID = Double.NaN;
				//the mapping between WP and module is done in module table
				updateModuleDefectPlan(prjID, defectInfo);
			} //mapping with QC_ACTIVITY.QA_ID column
			double[][] valArray = new double[8][2];
			valArray[0][0] = defectInfo.acceptanceTest;
			valArray[1][0] = defectInfo.codeReview;
			valArray[2][0] = defectInfo.documentReview;
			valArray[3][0] = defectInfo.integrationTest;
			valArray[4][0] = defectInfo.others;
			valArray[5][0] = defectInfo.prototypeReview;
			valArray[6][0] = defectInfo.systemTest;
			valArray[7][0] = defectInfo.unitTest;
			valArray[0][1] = 13;
			valArray[1][1] = 21;
			valArray[2][1] = 20;
			valArray[3][1] = 11;
			valArray[4][1] = 26;
			valArray[5][1] = 23;
			valArray[6][1] = 12;
			valArray[7][1] = 10;
			conn = ServerHelper.instance().getConnection();
			stm =
				conn.prepareStatement(
					"UPDATE DEFECT_PLAN SET"
						+ " VALUE=?"
						+ " WHERE PROJECT_ID=?"
						+ " AND WP_ID "
						+ ((Double.isNaN(defectInfo.wpID)) ? " IS NULL" : "=" + (long) defectInfo.wpID)
						+ " AND QA_ID=?"
						+ " AND MODULE_ID "
						+ ((Double.isNaN(defectInfo.moduleID)) ? " IS NULL" : "=" + (long) defectInfo.moduleID));
			//trigger for table ID insertion already exists	 		
			stmInsert = conn.prepareStatement("INSERT INTO DEFECT_PLAN (PROJECT_ID, WP_ID, MODULE_ID, QA_ID, VALUE) VALUES(?,?,?,?,?)");
			for (int i = 0; i < valArray.length; i++) {
				if (!Double.isNaN(valArray[i][0])) { //try to update first
					Db.setDouble(stm, 1, valArray[i][0]);
					stm.setLong(2, prjID);
					stm.setDouble(3, valArray[i][1]);
					if (stm.executeUpdate() < 1) { //update fail, then insert
						stmInsert.setLong(1, prjID);
						Db.setDouble(stmInsert, 2, defectInfo.wpID);
						Db.setDouble(stmInsert, 3, defectInfo.moduleID);
						stmInsert.setDouble(4, valArray[i][1]);
						Db.setDouble(stmInsert, 5, valArray[i][0]);
						if (stmInsert.executeUpdate() != 1)
							return false;
					}
				}
			}
			stmInsert.close();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}	
	/**
	 * Update Defect Product planning
	 *  
	 **/

	public static boolean updateDefectProductPlan_New(final long prjID, DefectProductPlanInfo defectInfo) {
		Connection conn = null;
		PreparedStatement stmUpdate = null;
		PreparedStatement stmInsert = null;		
		long QA_ID;
		long QA_Type;		
		double planedSize=0;
		try {			
			Vector modules=WorkProduct.getModuleSize(prjID,(long) defectInfo.moduleID);
			WPSizeInfo info  = (WPSizeInfo)modules.elementAt(0);			
			planedSize=Double.isNaN(info.reestimatedSizeConv)?info.estimatedSizeConv:info.reestimatedSizeConv;
			NormInfo normDefectRate =getNormByProductType(prjID,defectInfo.wpID,true);		
			NormInfo normDefectRE =getNormByProductType(prjID,defectInfo.wpID,false);		
			//normDefectRate.										
			conn = ServerHelper.instance().getConnection();									
			
			//Calculte requirementReview plan value			
			defectInfo.requirementReview[1]=planedSize*normDefectRate.average*(normDefectRE.average/100)*(defectInfo.requirementReview[0]/100);				
			QA_ID=0;
			QA_Type=1;
			if (!updatePlanValue(prjID,defectInfo,QA_ID,QA_Type))
				return false;
				
			//Calculte designReview plan value								
			defectInfo.designReview[1]=planedSize*normDefectRate.average*(normDefectRE.average/100)*(defectInfo.designReview[0]/100);			
			QA_ID=0;
			QA_Type=2;
			if (!updatePlanValue(prjID,defectInfo,QA_ID,QA_Type))
				return false;								
		
			//Calculte others plan value			
			defectInfo.others[1]=planedSize*normDefectRate.average*(normDefectRE.average/100)*(defectInfo.others[0]/100);
			QA_ID=0;
			QA_Type=3;
			if (!updatePlanValue(prjID,defectInfo,QA_ID,QA_Type))			
				return false;		
			//Calculate Other Test
			QA_ID = 0;
			QA_Type = 4;
			defectInfo.otherTest[1] = planedSize*normDefectRate.average*(normDefectRE.average/100)*(defectInfo.otherTest[0]/100);
			if (!updatePlanValue(prjID,defectInfo,QA_ID,QA_Type))			
				return false;	
			//Calculate Other Review
			QA_ID = QCActivityInfo.OTHER_REVIEW;
			QA_Type = 0;
			defectInfo.otherReview[1] = planedSize*normDefectRate.average*(normDefectRE.average/100)*(defectInfo.otherReview[0]/100);		
			if (!updatePlanValue(prjID,defectInfo,QA_ID,QA_Type))			
				return false;	
			//Calculte codeReview plan value						
			defectInfo.codeReview[1]=planedSize*normDefectRate.average*(normDefectRE.average/100)*(defectInfo.codeReview[0]/100);
			QA_ID=QCActivityInfo.CODE_REVIEW;
			QA_Type=0;
			if (!updatePlanValue(prjID,defectInfo,QA_ID,QA_Type))
				return false;												
						
			//Calculte unitTest plan value
			defectInfo.unitTest[1]=planedSize*normDefectRate.average*(normDefectRE.average/100)*(defectInfo.unitTest[0]/100);
			QA_ID=QCActivityInfo.UNIT_TEST;
			QA_Type=0;
			if (!updatePlanValue(prjID,defectInfo,QA_ID,QA_Type))
				return false;								
					
			//Calculte integrationTest plan value
			defectInfo.integrationTest[1]=planedSize*normDefectRate.average*(normDefectRE.average/100)*(defectInfo.integrationTest[0]/100);
			QA_ID=QCActivityInfo.INTEGRATION_TEST;
			QA_Type=0;
			if (!updatePlanValue(prjID,defectInfo,QA_ID,QA_Type))
				return false;								
					
			//Calculte systemTest plan value				
			defectInfo.systemTest[1]=planedSize*normDefectRate.average*(normDefectRE.average/100)*(defectInfo.systemTest[0]/100);								
			QA_ID=QCActivityInfo.SYSTEM_TEST;
			QA_Type=0;
			if (!updatePlanValue(prjID,defectInfo,QA_ID,QA_Type))
				return false;				
			
			return true;	
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, null, null);
		}
	}
		
	public static boolean updatePlanValue(final long prjID,DefectProductPlanInfo defectInfo,long QA_ID,long QA_Type){
		Connection conn = null;
		PreparedStatement stmUpdate = null;
		PreparedStatement stmInsert = null;	
		String sql=null;
		double ratePlanValue=Double.NaN;
		double planValue=Double.NaN;
		try {
			if (QA_ID==0){			
				switch ((int) QA_Type){
					case 1 :
						ratePlanValue=defectInfo.requirementReview[0];
						planValue=defectInfo.requirementReview[1];
						break;
					case 2 :
						ratePlanValue=defectInfo.designReview[0];
						planValue=defectInfo.designReview[1];
						break;
					case 3 :
						ratePlanValue=defectInfo.others[0];
						planValue=defectInfo.others[1];
						break;
					case 4:
						ratePlanValue=defectInfo.otherTest[0];
						planValue=defectInfo.otherTest[1];						
						break;
				}							
			}else{
				switch ((int) QA_ID){
					case QCActivityInfo.CODE_REVIEW:
						ratePlanValue=defectInfo.codeReview[0];
						planValue=defectInfo.codeReview[1];
					break;
					case QCActivityInfo.UNIT_TEST:
						ratePlanValue=defectInfo.unitTest[0];
						planValue=defectInfo.unitTest[1];
					break;
					case QCActivityInfo.INTEGRATION_TEST:
						ratePlanValue=defectInfo.integrationTest[0];
						planValue=defectInfo.integrationTest[1];
					break;		
					case QCActivityInfo.SYSTEM_TEST:
						ratePlanValue=defectInfo.systemTest[0];
						planValue=defectInfo.systemTest[1];
					break;
					case QCActivityInfo.OTHER_REVIEW:
						ratePlanValue=defectInfo.otherReview[0];
						planValue=defectInfo.otherReview[1];
					break;
			}
			}
						
			conn = ServerHelper.instance().getConnection();
			sql="UPDATE DEFECT_PRODUCT_PLAN SET RATE_PLAN_VALUE=?, PLAN_VALUE=?" +
					" WHERE PROJECT_ID=?"
					+" AND WP_ID=?"
					+" AND MODULE_ID=?"
					+" AND QA_ID=?"
					+" AND QA_TYPE=?";
			
			stmUpdate = conn.prepareStatement(sql);
			Db.setDouble(stmUpdate,1,ratePlanValue);
			Db.setDouble(stmUpdate,2,planValue);
			stmUpdate.setLong(3, prjID);
			stmUpdate.setLong(4, defectInfo.wpID);
			stmUpdate.setLong(5, defectInfo.moduleID);
			stmUpdate.setLong(6, QA_ID);
			stmUpdate.setLong(7, QA_Type);
			
			if (stmUpdate.executeUpdate() < 1) { //update fail, then insert				
				sql = "INSERT INTO DEFECT_PRODUCT_PLAN (PROJECT_ID, WP_ID, MODULE_ID, QA_ID, QA_TYPE, RATE_PLAN_VALUE, PLAN_VALUE) VALUES (?,?,?,?,?,?,?)";			
				stmInsert = conn.prepareStatement(sql);		
					stmInsert.setLong(1, prjID);
					stmInsert.setLong(2, defectInfo.wpID);
					stmInsert.setLong(3,defectInfo.moduleID);
				stmInsert.setLong(4,QA_ID); //QA_ID=0: Others
				stmInsert.setLong(5,QA_Type); //QA_TYPE=3: Others
				Db.setDouble(stmInsert,6,ratePlanValue);
				Db.setDouble(stmInsert,7,planValue);									
				if (stmInsert.executeUpdate()!=1)
					return false;
					stmInsert.close();	
				}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stmUpdate, null);
		}
	}

	public static boolean updateDefectProductReplan(final long prjID, Vector defectReplanVector) {			
		Connection conn = null;
		long QA_ID;
		long QA_Type;			
		try {			
			DefectProductReplanInfo defectInfo;	
			conn = ServerHelper.instance().getConnection();	
			for(int i=0;i<defectReplanVector.size();i++){
				defectInfo=(DefectProductReplanInfo)defectReplanVector.elementAt(i);
				//if (!defectInfo.released){
				    //Requirement review				     
				    QA_ID=0;
				    QA_Type=1;
				    if (!updateReplanValue(prjID,defectInfo,QA_ID,QA_Type,conn))
						return false;
				    //Design review
				    QA_ID=0;
				    QA_Type=2;
				    if (!updateReplanValue(prjID,defectInfo,QA_ID,QA_Type,conn))
				    	return false;
					//Others
					QA_ID=0;
					QA_Type=3;
					if (!updateReplanValue(prjID,defectInfo,QA_ID,QA_Type,conn))
						return false;
					// Other Test
					QA_ID=0;
					QA_Type=4;
					if (!updateReplanValue(prjID,defectInfo,QA_ID,QA_Type,conn))
						return false;
					// Other Review 
					QA_ID = QCActivityInfo.OTHER_REVIEW ;
					QA_Type = 0;
					if(!updateReplanValue(prjID,defectInfo,QA_ID,QA_Type,conn))
						return false;
					//Code Review	
					QA_ID=QCActivityInfo.CODE_REVIEW;
					QA_Type=0;
					if (!updateReplanValue(prjID,defectInfo,QA_ID,QA_Type,conn))
						return false;
				   //Unit Test	
				   QA_ID=QCActivityInfo.UNIT_TEST;
				   QA_Type=0;
				   if (!updateReplanValue(prjID,defectInfo,QA_ID,QA_Type,conn))
					   return false;
					//Inregration Test	
					QA_ID=QCActivityInfo.INTEGRATION_TEST;
					QA_Type=0;
					if (!updateReplanValue(prjID,defectInfo,QA_ID,QA_Type,conn))
						return false;
					//System Test	
					QA_ID=QCActivityInfo.SYSTEM_TEST;
					QA_Type=0;
					if (!updateReplanValue(prjID,defectInfo,QA_ID,QA_Type,conn))
						return false;											   				
			    //}
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {			
			ServerHelper.closeConnection(conn, null, null);
		}
	}

	public static boolean updateReplanValue(final long prjID,DefectProductReplanInfo defectInfo,long QA_ID,long QA_Type,Connection conn){		
		PreparedStatement stm = null;
		double rePlanValue=Double.NaN;
		String sql=null;
		try {
			if (QA_ID==0){			
				switch ((int) QA_Type){
					case 1 :
						rePlanValue=defectInfo.requirementReview[1];						
						break;
					case 2 :
						rePlanValue=defectInfo.designReview[1];
						break;
					case 3 :
						rePlanValue=defectInfo.others[1];
						break;
					case 4:
						rePlanValue = defectInfo.otherTest[1];
						break;
				}
			}else{
				switch ((int) QA_ID){
					case QCActivityInfo.CODE_REVIEW:
						rePlanValue=defectInfo.codeReview[1];
						break;
					case QCActivityInfo.UNIT_TEST:
						rePlanValue=defectInfo.unitTest[1];
						break;
					case QCActivityInfo.INTEGRATION_TEST:
						rePlanValue=defectInfo.integrationTest[1];
						break;
					case QCActivityInfo.SYSTEM_TEST:
						rePlanValue=defectInfo.systemTest[1];
						break;	
					case QCActivityInfo.OTHER_REVIEW:
						rePlanValue = defectInfo.otherReview[1];
						break;
			}
			}		
					 
			sql="UPDATE DEFECT_PRODUCT_PLAN SET REPLAN_VALUE=?" +
					" WHERE PROJECT_ID="+prjID +" AND " +
					" WP_ID="+defectInfo.wpID +" AND " +
					" MODULE_ID="+defectInfo.moduleID +" AND" +
				" QA_ID="+QA_ID+" AND QA_TYPE="+QA_Type;
									
			stm=conn.prepareStatement(sql);
			Db.setDouble(stm,1,rePlanValue);			
			if (stm.executeUpdate()!=1)
				return false;
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(null, stm, null);
		}	
	}
	
	public static boolean updateDefectRateReplan(final long prjID, Vector defectRateReplanVector){
		Connection conn = null;
		PreparedStatement stm = null;
		String sql=null;		
		long QA_ID;
		long QA_Type;		
		try {			
			DefectRateInfo defectRateInfo;
			conn = ServerHelper.instance().getConnection();			
			for(int i=0;i<defectRateReplanVector.size();i++){
				defectRateInfo=(DefectRateInfo)defectRateReplanVector.elementAt(i);
				//if (!defectRateInfo.released){
					sql="UPDATE MODULE SET REPLAN_DEFECT_RATE=?, NOTE_DEFECT_RATE=? " +
					" WHERE PROJECT_ID="+prjID +" AND " +
						" WP_ID="+defectRateInfo.wpID +" AND " +
						" MODULE_ID="+defectRateInfo.moduleID;
										
					stm=conn.prepareStatement(sql);
					Db.setDouble(stm,1,defectRateInfo.replan);
					stm.setString(2,defectRateInfo.note);			
					if (stm.executeUpdate() != 1)
						return false;
						
					stm.close();																				   				
				//}
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, null, null);
		}
	}
	
	/**
	 * Update detail Defect planning
	 *  
	 **/
	
	public static boolean updateDefectPlanDetail(final long prjID, DefectPlanInfo defectInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		PreparedStatement stmInsert = null;
		try {
			if ((defectInfo.moduleID == 0) || Double.isNaN(defectInfo.moduleID)) {
				defectInfo.moduleID = Double.NaN; //if NaN
				updateWPDefectPlan(prjID, defectInfo);
			}
			else {
				defectInfo.wpID = Double.NaN;
				//the mapping between WP and module is done in module table
				updateModuleDefectPlan(prjID, defectInfo);
			} //mapping with QC_ACTIVITY.QA_ID column
			double[][] valArray = new double[8][2];
			valArray[0][0] = defectInfo.acceptanceTest;
			valArray[1][0] = defectInfo.codeReview;
			valArray[2][0] = defectInfo.documentReview;
			valArray[3][0] = defectInfo.integrationTest;
			valArray[4][0] = defectInfo.others;
			valArray[5][0] = defectInfo.prototypeReview;
			valArray[6][0] = defectInfo.systemTest;
			valArray[7][0] = defectInfo.unitTest;
			valArray[0][1] = 13;
			valArray[1][1] = 21;
			valArray[2][1] = 20;
			valArray[3][1] = 11;
			valArray[4][1] = 26;
			valArray[5][1] = 23;
			valArray[6][1] = 12;
			valArray[7][1] = 10;
			conn = ServerHelper.instance().getConnection();
			stm =
				conn.prepareStatement(
					"UPDATE DEFECT_PLAN SET"
						+ " VALUE=?"
						+ " WHERE PROJECT_ID=?"
						+ " AND WP_ID "
						+ ((Double.isNaN(defectInfo.wpID)) ? " IS NULL" : "=" + (long) defectInfo.wpID)
						+ " AND QA_ID=?"
						+ " AND MODULE_ID "
						+ ((Double.isNaN(defectInfo.moduleID)) ? " IS NULL" : "=" + (long) defectInfo.moduleID));
			//trigger for table ID insertion already exists	 		
			stmInsert = conn.prepareStatement("INSERT INTO DEFECT_PLAN (PROJECT_ID, WP_ID, MODULE_ID, QA_ID, VALUE) VALUES(?,?,?,?,?)");
			for (int i = 0; i < valArray.length; i++) {
				if (!Double.isNaN(valArray[i][0])) { //try to update first
					Db.setDouble(stm, 1, valArray[i][0]);
					stm.setLong(2, prjID);
					stm.setDouble(3, valArray[i][1]);
					if (stm.executeUpdate() < 1) { //update fail, then insert
						stmInsert.setLong(1, prjID);
						Db.setDouble(stmInsert, 2, defectInfo.wpID);
						Db.setDouble(stmInsert, 3, defectInfo.moduleID);
						stmInsert.setDouble(4, valArray[i][1]);
						Db.setDouble(stmInsert, 5, valArray[i][0]);
						if (stmInsert.executeUpdate() != 1)
							return false;
					}
				}
			}
			stmInsert.close();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}

	/**
	* Update Work product Defect planning
	*  
	**/
	public static boolean updateWPDefectPlan(final long prjID, DefectPlanInfo defectInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			//try to update first
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE WP_SIZE SET" + " PLANNED_DEFECT = ?," + " REPLANNED_DEFECT =?" + " WHERE PROJECT_ID=?" + " AND WP_ID=?";
			stm = conn.prepareStatement(sql);
			Db.setDouble(stm, 1, defectInfo.plannedWDefect);
			Db.setDouble(stm, 2, defectInfo.rePlannedWDefect);
			stm.setLong(3, prjID);
			stm.setLong(4, (long) defectInfo.wpID);
			if (stm.executeUpdate() < 1) { //update fail, then insert
				stm.close();
				sql = "INSERT INTO WP_SIZE (PLANNED_DEFECT, REPLANNED_DEFECT, PROJECT_ID,WP_ID)" + " VALUES (?,?,?,?)";
				stm = conn.prepareStatement(sql);
				stm.setDouble(1, defectInfo.plannedWDefect);
				Db.setDouble(stm, 2, defectInfo.rePlannedWDefect);
				stm.setLong(3, prjID);
				stm.setLong(4, (long) defectInfo.wpID);
				if (stm.executeUpdate() != 1)
					return false;
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	
	
	/**
	* Update Module Defect planning
	*  
	**/
	
	public static boolean updateModuleDefectPlan(final long prjID, DefectPlanInfo defectInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE MODULE SET PLANNED_DEFECT = ?, REPLANNED_DEFECT =? WHERE MODULE_ID=?";
			stm = conn.prepareStatement(sql);
			Db.setDouble(stm, 1, defectInfo.plannedWDefect);
			Db.setDouble(stm, 2, defectInfo.rePlannedWDefect);
			stm.setLong(3, (long) defectInfo.moduleID);
			return (stm.executeUpdate() > 0);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	
	
	public static boolean deleteDetailDefectPlan(long projectID, DefectPlanInfo defectInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			defectInfo.plannedWDefect = Double.NaN;
			defectInfo.rePlannedWDefect = Double.NaN;
			if (Double.isNaN(defectInfo.moduleID)) //it's a workproduct
				updateWPDefectPlan(projectID, defectInfo);
			else //it's a module
				updateModuleDefectPlan(projectID, defectInfo);
			conn = ServerHelper.instance().getConnection();
			sql =
				"DELETE FROM DEFECT_PLAN WHERE PROJECT_ID=?"
					+ " AND WP_ID "
					+ ((!Double.isNaN(defectInfo.moduleID)) ? " IS NULL" : "=" + (long) defectInfo.wpID)
					+ " AND MODULE_ID "
					+ ((Double.isNaN(defectInfo.moduleID)) ? " IS NULL" : "=" + (long) defectInfo.moduleID);
			stm = conn.prepareStatement(sql);
			stm.setLong(1, projectID);
			stm.executeUpdate();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	
	public static boolean deleteDefectProductPlan(long projectID, DefectProductPlanInfo defectInfo) {
			Connection conn = null;
			PreparedStatement stm = null;
			String sql = null;
			try {				
				conn = ServerHelper.instance().getConnection();
				sql =
					"DELETE FROM DEFECT_PRODUCT_PLAN " +
					"WHERE PROJECT_ID=? AND WP_ID=? AND MODULE_ID=?";						
				
				stm = conn.prepareStatement(sql);				
				stm.setLong(1, projectID);
				stm.setLong(2, defectInfo.wpID);
				stm.setLong(3, defectInfo.moduleID);				
				stm.executeUpdate();
				return true;
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			finally {
				ServerHelper.closeConnection(conn, stm, null);
			}
		}
	
	public static final Vector getDPTask(final long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		Vector vt = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT *  FROM DPTASK DP WHERE PROJECTID = " + prjID + " ORDER BY DP.DPTASK";
			
			rs = stm.executeQuery(sql);
			if (rs != null) {
				vt = new Vector();
				while (rs.next()) {
					final DPTaskInfo info = new DPTaskInfo();
					info.dptaskID = rs.getLong("DPTASKID");
					info.prjID = rs.getLong("PROJECTID");
					info.item = rs.getString("DPTASK");
					info.unit = rs.getString("DPUNIT");
					info.planValue = rs.getDouble("DPPLAN");
					// Add by HaiMM
					info.usl = rs.getDouble("USL");
					info.lsl = rs.getDouble("LSL");
                    info.actualValue = Db.getDouble(rs, "DPACTUAL");
                    if (info.planValue != 0) {
                        info.deviationValue = 100*((info.actualValue - info.planValue)/info.planValue);
                    }
                    info.dpCause = (rs.getString("DP_CAUSE") == null ? "" : rs.getString("DP_CAUSE")) ;   
					vt.add(info);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
			return vt;
		}
	}
	// Added by anhtv08
	public static final Vector getDPTask(final long prjID, long mileStoneID) {
			Connection conn = null;
			Statement stm = null;
			String sql = null;
			Vector vt = null;
			ResultSet rs = null;
			try {
				conn = ServerHelper.instance().getConnection();
					  sql = "SELECT a.dptaskid, a.projectid, a.dptask, a.dpunit, a.dpplan,"+
		       		   " a.dp_cause, a.usl, a.lsl, b.actual,b.milestone_id"+
					   " FROM dptask a, dp_milestone b "+
					   " where a.dptaskid = b.dptaskid and b.milestone_id="+mileStoneID+
					  " order by a.dptaskid";
				stm= conn.createStatement();
				stm.executeQuery(sql);
				rs = stm.executeQuery(sql);
				if (rs != null) {
					vt = new Vector();
					while (rs.next()) {
						final DPTaskInfo info = new DPTaskInfo();
						info.dptaskID = rs.getLong("DPTASKID");
						info.prjID = rs.getLong("PROJECTID");
						info.item = rs.getString("DPTASK");
						info.unit = rs.getString("DPUNIT");
						info.planValue = rs.getDouble("DPPLAN");
						// Add by HaiMM
						info.usl = rs.getDouble("USL");
						info.lsl = rs.getDouble("LSL");
						// anhtv08-start
						
						info.actualValue= Db.getDouble(rs,"Actual");
						info.milestoneID=rs.getDouble("MILESTONE_ID");
						// end
						
						if (info.planValue != 0) {
							info.deviationValue = 100*((info.actualValue - info.planValue)/info.planValue);
						}
						info.dpCause = (rs.getString("DP_CAUSE") == null ? "" : rs.getString("DP_CAUSE")) ;   
						vt.add(info);
					}
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn, stm, null);
				return vt;
			}
		}

				
	public static boolean addDPTask(DPTaskInfo dptaskInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "INSERT INTO DPTASK VALUES ((SELECT NVL(MAX(DPTASKID)+1,1) FROM DPTASK), ?, ?, ?, ?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, dptaskInfo.prjID);
			stm.setString(2, dptaskInfo.item);
			stm.setString(3, dptaskInfo.unit);
			Db.setDouble(stm, 4, dptaskInfo.planValue);
			Db.setDouble(stm, 5, dptaskInfo.actualValue);
			stm.setString(6, dptaskInfo.dpCause);
			Db.setDouble(stm, 7, dptaskInfo.usl);
			Db.setDouble(stm, 8, dptaskInfo.lsl);
			//Added anhtv08-start
			// Insert number of (DPTask*stagelist) into DP_milestone..
			 
			 if(stm.executeUpdate()>0)
			 {
			 	stm.close();
			 	//Vector stageList= Schedule.getStageList(dptaskInfo.prjID);
				Vector milestoneList = Project.getMilestoneListByProject(dptaskInfo.prjID);
			 	for(int i=0;i<milestoneList.size();i++)
			 	{
//					swl= "INSERT INTO CUS_POINT (CUS_POINT_ID, MILESTONE_ID, CODE, POINT) "
//												+ " VALUES (NVL((SELECT MAX(CUS_POINT_ID)+1 FROM CUS_POINT),1), ?, (SELECT MAX(CODE) FROM CUS_METRICS), null)";
					MilestoneInfo milestoneInfo = new MilestoneInfo();
					milestoneInfo = (MilestoneInfo)milestoneList.elementAt(i);
					
			 		sql=" INSERT INTO DP_MILESTONE (DP_MILESTONE_ID, MILESTONE_ID, DPTASKID, ACTUAL)"+
			 			" VALUES (NVL((SELECT MAX(DP_MILESTONE_ID)+1 FROM DP_MILESTONE),1),?, (SELECT MAX(DPTASKID) FROM DPTASK), null)";
					stm= conn.prepareStatement(sql);
					Db.setDouble(stm,1,milestoneInfo.getMilestoneId());
					stm.executeUpdate();
					stm.close();			 				
			 	}
			 	return true;
			 }
			else return false;
			/*if (stm.executeUpdate() != 1)
				return false;
			return true; */
		
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
		
	public static boolean updateDPTask(DPTaskInfo dptaskInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE DPTASK SET DPTASK = ?, DPUNIT = ?, DPPLAN = ?, DPACTUAL = ?, DP_CAUSE = ?, USL = ?, LSL = ? WHERE DPTASKID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, dptaskInfo.item);
			stm.setString(2, dptaskInfo.unit);
			Db.setDouble(stm, 3, dptaskInfo.planValue);
			Db.setDouble(stm, 4, dptaskInfo.actualValue);
            stm.setString(5, dptaskInfo.dpCause);
			stm.setDouble(6, dptaskInfo.usl);
			stm.setDouble(7, dptaskInfo.lsl);
			stm.setLong(8, dptaskInfo.dptaskID);
			if (stm.executeUpdate() != 1)
				return false;
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}

	public static boolean deleteDPTask(long dpTaskId) {			
		if(	Db.delete(dpTaskId, "DPTASKID", "DP_MILESTONE")&&Db.delete(dpTaskId, "DPTASKID", "DPTASK"))
		{
			return true;
		}
		else return false;
	}

	public static boolean updateADPTask(DPTaskInfo dptaskInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "UPDATE DPTASK SET DPACTUAL = ?, DP_CAUSE = ? WHERE DPTASKID = ?";
			stm = conn.prepareStatement(sql);
			Db.setDouble(stm, 1, dptaskInfo.actualValue);
            stm.setString(2, dptaskInfo.dpCause);
			stm.setLong(3, dptaskInfo.dptaskID);
			if (stm.executeUpdate() != 1)
				return false;
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	private static String genSQLDefectOrigin(int nSeverity, String strProcessCondition, String strSumName) {
		StringBuffer sbTemp = new StringBuffer();
		sbTemp.append("(SELECT COUNT(defect_id) AS ").append(strSumName);
		sbTemp.append(" FROM defect");
		sbTemp.append(" WHERE project_id=? AND ds_id NOT IN (6)");
		sbTemp.append(" AND defs_id=").append(nSeverity);
		sbTemp.append(" AND process_id").append(strProcessCondition);
		sbTemp.append(" AND create_date BETWEEN ? AND ?) t_").append(strSumName);
		return sbTemp.toString();
	}
    //HuyNH2 add function for project archive.
//    private static String genSQLDefectArchiveOrigin(int nSeverity, String strProcessCondition, String strSumName) {
//        StringBuffer sbTemp = new StringBuffer();
//        sbTemp.append("(SELECT COUNT(defect_id) AS ").append(strSumName);
//        sbTemp.append(" FROM DEFECT_ARCHIVE");
//        sbTemp.append(" WHERE project_id=? AND ds_id NOT IN (6)");
//        sbTemp.append(" AND defs_id=").append(nSeverity);
//        sbTemp.append(" AND process_id").append(strProcessCondition);
//        sbTemp.append(" AND create_date BETWEEN ? AND ?) t_").append(strSumName);
//        return sbTemp.toString();
//    }

	/**
	 * Phuongnt
	 * get weighted defect distributed by process for SQA report
	 * @param startDate start date of report
	 * @param endDate end date of report
	 * @param defectOriginInfo defect origin info.object
	 * @return defect origin info.object
	 */
	public static DefectOriginInfo getSQADefectOriginByProcess(Date startDate, Date endDate, DefectOriginInfo defectOriginInfo) {
		Connection con = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			// for defect
            String strSQL =
				"SELECT fatal_req*10+serious_req*5+medium_req*3+cosmetic_req AS req_defect,"
					+ "fatal_design*10+serious_design*5+medium_design*3+cosmetic_design AS design_defect,"
					+ "fatal_coding*10+serious_coding*5+medium_coding*3+cosmetic_coding AS coding_defect,"
					+ "fatal_other*10+serious_other*5+medium_other*3+cosmetic_other AS other_defect"
					+ " FROM"
					+ genSQLDefectOrigin(1, "=2", "fatal_req")
					+ ","
					+ genSQLDefectOrigin(2, "=2", "serious_req")
					+ ","
					+ genSQLDefectOrigin(3, "=2", "medium_req")
					+ ","
					+ genSQLDefectOrigin(4, "=2", "cosmetic_req")
					+ ","
					+ genSQLDefectOrigin(1, "=3", "fatal_design")
					+ ","
					+ genSQLDefectOrigin(2, "=3", "serious_design")
					+ ","
					+ genSQLDefectOrigin(3, "=3", "medium_design")
					+ ","
					+ genSQLDefectOrigin(4, "=3", "cosmetic_design")
					+ ","
					+ genSQLDefectOrigin(1, "=4", "fatal_coding")
					+ ","
					+ genSQLDefectOrigin(2, "=4", "serious_coding")
					+ ","
					+ genSQLDefectOrigin(3, "=4", "medium_coding")
					+ ","
					+ genSQLDefectOrigin(4, "=4", "cosmetic_coding")
					+ ","
					+ genSQLDefectOrigin(1, " NOT IN (2,3,4)", "fatal_other")
					+ ","
					+ genSQLDefectOrigin(2, " NOT IN (2,3,4)", "serious_other")
					+ ","
					+ genSQLDefectOrigin(3, " NOT IN (2,3,4)", "medium_other")
					+ ","
					+ genSQLDefectOrigin(4, " NOT IN (2,3,4)", "cosmetic_other");
			con = ServerHelper.instance().getConnection();
			prepStmt = con.prepareStatement(strSQL);
			for (int i = 0; i < 16; i++) {
				prepStmt.setLong(3 * i + 1, defectOriginInfo.getProjectId());
				prepStmt.setDate(3 * i + 2, startDate);
				prepStmt.setDate(3 * i + 3, endDate);
			}
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				defectOriginInfo.setRequirementDefect(rs.getInt("req_defect"));
				defectOriginInfo.setDesignDefect(rs.getInt("design_defect"));
				defectOriginInfo.setCodingDefect(rs.getInt("coding_defect"));
				defectOriginInfo.setOtherDefect(rs.getInt("other_defect"));
			}
            
//            rs.close();
//            prepStmt.close();
            // for archive table
            // HuyNH2 add code for project archive
//            strSQL =
//                "SELECT fatal_req*10+serious_req*5+medium_req*3+cosmetic_req AS req_defect,"
//                    + "fatal_design*10+serious_design*5+medium_design*3+cosmetic_design AS design_defect,"
//                    + "fatal_coding*10+serious_coding*5+medium_coding*3+cosmetic_coding AS coding_defect,"
//                    + "fatal_other*10+serious_other*5+medium_other*3+cosmetic_other AS other_defect"
//                    + " FROM"
//                    + genSQLDefectArchiveOrigin(1, "=2", "fatal_req")
//                    + ","
//                    + genSQLDefectArchiveOrigin(2, "=2", "serious_req")
//                    + ","
//                    + genSQLDefectArchiveOrigin(3, "=2", "medium_req")
//                    + ","
//                    + genSQLDefectArchiveOrigin(4, "=2", "cosmetic_req")
//                    + ","
//                    + genSQLDefectArchiveOrigin(1, "=3", "fatal_design")
//                    + ","
//                    + genSQLDefectArchiveOrigin(2, "=3", "serious_design")
//                    + ","
//                    + genSQLDefectArchiveOrigin(3, "=3", "medium_design")
//                    + ","
//                    + genSQLDefectArchiveOrigin(4, "=3", "cosmetic_design")
//                    + ","
//                    + genSQLDefectArchiveOrigin(1, "=4", "fatal_coding")
//                    + ","
//                    + genSQLDefectArchiveOrigin(2, "=4", "serious_coding")
//                    + ","
//                    + genSQLDefectArchiveOrigin(3, "=4", "medium_coding")
//                    + ","
//                    + genSQLDefectArchiveOrigin(4, "=4", "cosmetic_coding")
//                    + ","
//                    + genSQLDefectArchiveOrigin(1, " NOT IN (2,3,4)", "fatal_other")
//                    + ","
//                    + genSQLDefectArchiveOrigin(2, " NOT IN (2,3,4)", "serious_other")
//                    + ","
//                    + genSQLDefectArchiveOrigin(3, " NOT IN (2,3,4)", "medium_other")
//                    + ","
//                    + genSQLDefectArchiveOrigin(4, " NOT IN (2,3,4)", "cosmetic_other");
//            prepStmt = con.prepareStatement(strSQL);
//            for (int i = 0; i < 16; i++) {
//                prepStmt.setLong(3 * i + 1, defectOriginInfo.getProjectId());
//                prepStmt.setDate(3 * i + 2, startDate);
//                prepStmt.setDate(3 * i + 3, endDate);
//            }
//            rs = prepStmt.executeQuery();
//            if (rs.next()) {
//                defectOriginInfo.setRequirementDefect(rs.getInt("req_defect")+defectOriginInfo.getRequirementDefect());
//                defectOriginInfo.setDesignDefect(rs.getInt("design_defect")+defectOriginInfo.getDesignDefect());
//                defectOriginInfo.setCodingDefect(rs.getInt("coding_defect")+defectOriginInfo.getCodingDefect());
//                defectOriginInfo.setOtherDefect(rs.getInt("other_defect")+defectOriginInfo.getOtherDefect());
//            }
            
		}
		catch (Exception e){
		}
		finally {
			ServerHelper.closeConnection(con, prepStmt, rs);
			return defectOriginInfo;
		}
	}
	private static String genSQLDefectType(int nSeverity, String strDefectType, String strSumName) {
		StringBuffer sbTemp = new StringBuffer();
		sbTemp.append("(SELECT COUNT(defect_id) AS ").append(strSumName);
		sbTemp.append(" FROM defect");
		sbTemp.append(" WHERE project_id=? AND ds_id NOT IN (6)");
		sbTemp.append(" AND defs_id=").append(nSeverity);
		sbTemp.append(" AND dt_id").append(strDefectType);
		sbTemp.append(" AND create_date BETWEEN ? AND ?) t_").append(strSumName);
		return sbTemp.toString();
	}
    //HuyNH2 add function for project archive
//    private static String genSQLDefectArchiveType(int nSeverity, String strDefectType, String strSumName) {
//        StringBuffer sbTemp = new StringBuffer();
//        sbTemp.append("(SELECT COUNT(defect_id) AS ").append(strSumName);
//        sbTemp.append(" FROM DEFECT_ARCHIVE");
//        sbTemp.append(" WHERE project_id=? AND ds_id NOT IN (6)");
//        sbTemp.append(" AND defs_id=").append(nSeverity);
//        sbTemp.append(" AND dt_id").append(strDefectType);
//        sbTemp.append(" AND create_date BETWEEN ? AND ?) t_").append(strSumName);
//        return sbTemp.toString();
//    }
    
	/**
	 * Linhdd
	 * @param startDate
	 * @param endDate
	 * @param defectTypeInfo
	 * @return
	 */
	public static DefectTypeInfo getSQADefectTypeByProcess(Date startDate, Date endDate, DefectTypeInfo defectTypeInfo) {
		Connection con = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		try {
			String strSQL =
				"SELECT "
					+ "fatal_req*10+serious_req*5+medium_req*3+cosmetic_req AS req_defect,"
					+ "fatal_feature*10+serious_feature*5+medium_feature*3+cosmetic_feature AS feature_defect,"
					+ "fatal_coding*10+serious_coding*5+medium_coding*3+cosmetic_coding AS coding_defect,"
					+ "fatal_business*10+serious_business*5+medium_business*3+cosmetic_business AS business_defect,"
					+ "fatal_funcOther*10+serious_funcOther*5+medium_funcOther*3+cosmetic_funcOther AS funcOther_defect,"
					+ "fatal_interface*10+serious_interface*5+medium_interface*3+cosmetic_interface AS interface_defect,"
					+ "fatal_codeStandard*10+serious_codeStandard*5+medium_codeStandard*3+cosmetic_codeStandard AS codeStandard_defect,"
					+ "fatal_document*10+serious_document*5+medium_document*3+cosmetic_document AS document_defect,"
					+ "fatal_design*10+serious_design*5+medium_design*3+cosmetic_design AS design_defect,"
					+ "fatal_other*10+serious_other*5+medium_other*3+cosmetic_other AS other_defect"
					+ " FROM"				// 12: 011-Req misunderstanding
	+genSQLDefectType(1, "=12", "fatal_req")
		+ ","
		+ genSQLDefectType(2, "=12", "serious_req")
		+ ","
		+ genSQLDefectType(3, "=12", "medium_req")
		+ ","
		+ genSQLDefectType(4, "=12", "cosmetic_req")
		+ ","				// 13: 012-Feature missing
	+genSQLDefectType(1, "=13", "fatal_feature")
		+ ","
		+ genSQLDefectType(2, "=13", "serious_feature")
		+ ","
		+ genSQLDefectType(3, "=13", "medium_feature")
		+ ","
		+ genSQLDefectType(4, "=13", "cosmetic_feature")
		+ ","				// 14: 013-Coding logic
	+genSQLDefectType(1, "=14", "fatal_coding")
		+ ","
		+ genSQLDefectType(2, "=14", "serious_coding")
		+ ","
		+ genSQLDefectType(3, "=14", "medium_coding")
		+ ","
		+ genSQLDefectType(4, "=14", "cosmetic_coding")
		+ ","				// 15: 014-Business logic
	+genSQLDefectType(1, "=15", "fatal_business")
		+ ","
		+ genSQLDefectType(2, "=15", "serious_business")
		+ ","
		+ genSQLDefectType(3, "=15", "medium_business")
		+ ","
		+ genSQLDefectType(4, "=15", "cosmetic_business")
		+ ","				// 1: 01-Functionality (Other)
	+genSQLDefectType(1, "=1", "fatal_funcOther")
		+ ","
		+ genSQLDefectType(2, "=1", "serious_funcOther")
		+ ","
		+ genSQLDefectType(3, "=1", "medium_funcOther")
		+ ","
		+ genSQLDefectType(4, "=1", "cosmetic_funcOther")
		+ ","				// 2: 02-User Interface
	+genSQLDefectType(1, "=2", "fatal_interface")
		+ ","
		+ genSQLDefectType(2, "=2", "serious_interface")
		+ ","
		+ genSQLDefectType(3, "=2", "medium_interface")
		+ ","
		+ genSQLDefectType(4, "=2", "cosmetic_interface")
		+ ","				// 5: 05-Coding standard
	+genSQLDefectType(1, "=5", "fatal_codeStandard")
		+ ","
		+ genSQLDefectType(2, "=5", "serious_codeStandard")
		+ ","
		+ genSQLDefectType(3, "=5", "medium_codeStandard")
		+ ","
		+ genSQLDefectType(4, "=5", "cosmetic_codeStandard")
		+ ","				// 6: 06-Document
	+genSQLDefectType(1, "=6", "fatal_document")
		+ ","
		+ genSQLDefectType(2, "=6", "serious_document")
		+ ","
		+ genSQLDefectType(3, "=6", "medium_document")
		+ ","
		+ genSQLDefectType(4, "=6", "cosmetic_document")
		+ ","				// 4: 04-Design issue
	+genSQLDefectType(1, "=4", "fatal_design")
		+ ","
		+ genSQLDefectType(2, "=4", "serious_design")
		+ ","
		+ genSQLDefectType(3, "=4", "medium_design")
		+ ","
		+ genSQLDefectType(4, "=4", "cosmetic_design")
		+ ","				// xxx: xxx-Other Funct: 03-Performance, 07-Data & Database integrity, 
		// 08-Security & Access Control, 09-Portability, 10-Other, 11-Tools
	+genSQLDefectType(1, " IN (3,7,8,9,10,11)", "fatal_other")
		+ ","
		+ genSQLDefectType(2, " IN (3,7,8,9,10,11)", "serious_other")
		+ ","
		+ genSQLDefectType(3, " IN (3,7,8,9,10,11)", "medium_other")
		+ ","
		+ genSQLDefectType(4, " IN (3,7,8,9,10,11)", "cosmetic_other");
			con = ServerHelper.instance().getConnection();
			prepStmt = con.prepareStatement(strSQL);
			for (int i = 0; i < 40; i++) {
				prepStmt.setLong(3 * i + 1, defectTypeInfo.nProjectId);
				prepStmt.setDate(3 * i + 2, startDate);
				prepStmt.setDate(3 * i + 3, endDate);
			}
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				defectTypeInfo.nRequirementDefect = rs.getInt("req_defect");
				defectTypeInfo.nFeatureDefect = rs.getInt("feature_defect");
				defectTypeInfo.nCodingLogicDefect = rs.getInt("coding_defect");
				defectTypeInfo.nBusinessDefect = rs.getInt("business_defect");
				defectTypeInfo.nOtherFuncDefect = rs.getInt("funcOther_defect");
				defectTypeInfo.nInterfaceDefect = rs.getInt("interface_defect");
				defectTypeInfo.nCodingStandardDefect = rs.getInt("codeStandard_defect");
				defectTypeInfo.nDocumentDefect = rs.getInt("document_defect");
				defectTypeInfo.nDesignDefect = rs.getInt("design_defect");
				defectTypeInfo.nOtherDefect = rs.getInt("other_defect");
			}
//            rs.close();
//            prepStmt.close();
            
            //HuyNH2 add code for project archive.
//            strSQL =
//                "SELECT "
//                    + "fatal_req*10+serious_req*5+medium_req*3+cosmetic_req AS req_defect,"
//                    + "fatal_feature*10+serious_feature*5+medium_feature*3+cosmetic_feature AS feature_defect,"
//                    + "fatal_coding*10+serious_coding*5+medium_coding*3+cosmetic_coding AS coding_defect,"
//                    + "fatal_business*10+serious_business*5+medium_business*3+cosmetic_business AS business_defect,"
//                    + "fatal_funcOther*10+serious_funcOther*5+medium_funcOther*3+cosmetic_funcOther AS funcOther_defect,"
//                    + "fatal_interface*10+serious_interface*5+medium_interface*3+cosmetic_interface AS interface_defect,"
//                    + "fatal_codeStandard*10+serious_codeStandard*5+medium_codeStandard*3+cosmetic_codeStandard AS codeStandard_defect,"
//                    + "fatal_document*10+serious_document*5+medium_document*3+cosmetic_document AS document_defect,"
//                    + "fatal_design*10+serious_design*5+medium_design*3+cosmetic_design AS design_defect,"
//                    + "fatal_other*10+serious_other*5+medium_other*3+cosmetic_other AS other_defect"
//                    + " FROM"               // 12: 011-Req misunderstanding
//    +genSQLDefectArchiveType(1, "=12", "fatal_req")
//        + ","
//        + genSQLDefectArchiveType(2, "=12", "serious_req")
//        + ","
//        + genSQLDefectArchiveType(3, "=12", "medium_req")
//        + ","
//        + genSQLDefectArchiveType(4, "=12", "cosmetic_req")
//        + ","               // 13: 012-Feature missing
//    +genSQLDefectArchiveType(1, "=13", "fatal_feature")
//        + ","
//        + genSQLDefectArchiveType(2, "=13", "serious_feature")
//        + ","
//        + genSQLDefectArchiveType(3, "=13", "medium_feature")
//        + ","
//        + genSQLDefectArchiveType(4, "=13", "cosmetic_feature")
//        + ","               // 14: 013-Coding logic
//    +genSQLDefectArchiveType(1, "=14", "fatal_coding")
//        + ","
//        + genSQLDefectArchiveType(2, "=14", "serious_coding")
//        + ","
//        + genSQLDefectArchiveType(3, "=14", "medium_coding")
//        + ","
//        + genSQLDefectArchiveType(4, "=14", "cosmetic_coding")
//        + ","               // 15: 014-Business logic
//    +genSQLDefectArchiveType(1, "=15", "fatal_business")
//        + ","
//        + genSQLDefectArchiveType(2, "=15", "serious_business")
//        + ","
//        + genSQLDefectArchiveType(3, "=15", "medium_business")
//        + ","
//        + genSQLDefectArchiveType(4, "=15", "cosmetic_business")
//        + ","               // 1: 01-Functionality (Other)
//    +genSQLDefectArchiveType(1, "=1", "fatal_funcOther")
//        + ","
//        + genSQLDefectArchiveType(2, "=1", "serious_funcOther")
//        + ","
//        + genSQLDefectArchiveType(3, "=1", "medium_funcOther")
//        + ","
//        + genSQLDefectArchiveType(4, "=1", "cosmetic_funcOther")
//        + ","               // 2: 02-User Interface
//    +genSQLDefectArchiveType(1, "=2", "fatal_interface")
//        + ","
//        + genSQLDefectArchiveType(2, "=2", "serious_interface")
//        + ","
//        + genSQLDefectArchiveType(3, "=2", "medium_interface")
//        + ","
//        + genSQLDefectArchiveType(4, "=2", "cosmetic_interface")
//        + ","               // 5: 05-Coding standard
//    +genSQLDefectArchiveType(1, "=5", "fatal_codeStandard")
//        + ","
//        + genSQLDefectArchiveType(2, "=5", "serious_codeStandard")
//        + ","
//        + genSQLDefectArchiveType(3, "=5", "medium_codeStandard")
//        + ","
//        + genSQLDefectArchiveType(4, "=5", "cosmetic_codeStandard")
//        + ","               // 6: 06-Document
//    +genSQLDefectArchiveType(1, "=6", "fatal_document")
//        + ","
//        + genSQLDefectArchiveType(2, "=6", "serious_document")
//        + ","
//        + genSQLDefectArchiveType(3, "=6", "medium_document")
//        + ","
//        + genSQLDefectArchiveType(4, "=6", "cosmetic_document")
//        + ","               // 4: 04-Design issue
//    +genSQLDefectArchiveType(1, "=4", "fatal_design")
//        + ","
//        + genSQLDefectArchiveType(2, "=4", "serious_design")
//        + ","
//        + genSQLDefectArchiveType(3, "=4", "medium_design")
//        + ","
//        + genSQLDefectArchiveType(4, "=4", "cosmetic_design")
//        + ","               // xxx: xxx-Other Funct: 03-Performance, 07-Data & Database integrity, 
//        // 08-Security & Access Control, 09-Portability, 10-Other, 11-Tools
//    +genSQLDefectArchiveType(1, " IN (3,7,8,9,10,11)", "fatal_other")
//        + ","
//        + genSQLDefectArchiveType(2, " IN (3,7,8,9,10,11)", "serious_other")
//        + ","
//        + genSQLDefectArchiveType(3, " IN (3,7,8,9,10,11)", "medium_other")
//        + ","
//        + genSQLDefectArchiveType(4, " IN (3,7,8,9,10,11)", "cosmetic_other");
//            prepStmt = con.prepareStatement(strSQL);
//            for (int i = 0; i < 40; i++) {
//                prepStmt.setLong(3 * i + 1, defectTypeInfo.nProjectId);
//                prepStmt.setDate(3 * i + 2, startDate);
//                prepStmt.setDate(3 * i + 3, endDate);
//            }
//            rs = prepStmt.executeQuery();
//            if (rs.next()) {
//                defectTypeInfo.nRequirementDefect += rs.getInt("req_defect");
//                defectTypeInfo.nFeatureDefect += rs.getInt("feature_defect");
//                defectTypeInfo.nCodingLogicDefect += rs.getInt("coding_defect");
//                defectTypeInfo.nBusinessDefect += rs.getInt("business_defect");
//                defectTypeInfo.nOtherFuncDefect += rs.getInt("funcOther_defect");
//                defectTypeInfo.nInterfaceDefect += rs.getInt("interface_defect");
//                defectTypeInfo.nCodingStandardDefect += rs.getInt("codeStandard_defect");
//                defectTypeInfo.nDocumentDefect += rs.getInt("document_defect");
//                defectTypeInfo.nDesignDefect += rs.getInt("design_defect");
//                defectTypeInfo.nOtherDefect += rs.getInt("other_defect");
//            }            
		}
		catch (Exception e) {}
		finally {
			ServerHelper.closeConnection(con, prepStmt, rs);
			return defectTypeInfo;
		}
	}
	/**
	 * Another great algorythm copyright MANU
	 * returns array of int [a][b]
	 * a-0 created bugs
	 * a-1 closed bugs
	 * b- elapsed day from the start date
	 */
    //HuyNH2 modify this function for project archive.
	public static int[][] getProgress(long projectID, Date startDate, Date endDate) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int dayElapse = (int) CommonTools.dateDiff(startDate, endDate) + 1;
		int[][] arrTotal = new int[2][dayElapse];
        String defectTable = "DEFECT";
//        if(Db.checkProjecIsArchive(projectID)){
//            defectTable = "DEFECT_ARCHIVE";
//        }        
		try {
			//doesn't work using the setDate
			String bugJDBC = CommonTools.dateFormat(startDate);
			sql =
				"SELECT TRUNC(CREATE_DATE - TO_DATE('"
					+ bugJDBC
					+ "')) AA, COUNT(*)"
					+ " FROM " + defectTable
					+ " WHERE PROJECT_ID=?"
					+ " AND DS_ID <> 6"
					+ " AND CREATE_DATE >=? AND CREATE_DATE <=?"
					+ " GROUP BY TRUNC(CREATE_DATE - TO_DATE('"
					+ bugJDBC
					+ "'))"
					+ " ORDER BY AA ";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, projectID);
			stm.setDate(2, startDate);
			stm.setDate(3, endDate);
			rs = stm.executeQuery();
			while (rs.next()) {
				arrTotal[0][rs.getInt(1)] = rs.getInt(2);
			}
			rs.close();
			stm.close();
			sql =
				"SELECT TRUNC(CLOSE_DATE- TO_DATE('"
					+ bugJDBC
					+ "')) AA,COUNT(*)"
					+ " FROM " + defectTable
					+ " WHERE PROJECT_ID=?"
					+ " AND DS_ID <> 6"
					+ " AND CLOSE_DATE >=? AND CLOSE_DATE <=?"
					+ " GROUP BY TRUNC(CLOSE_DATE- TO_DATE('"
					+ bugJDBC
					+ "'))"
					+ " ORDER BY AA";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, projectID);
			stm.setDate(2, startDate);
			stm.setDate(3, endDate);
			rs = stm.executeQuery();
			while (rs.next()) {
				arrTotal[1][rs.getInt(1)] = rs.getInt(2);
			}
			//cumulated values
			for (int i = 1; i < dayElapse; i++) {
				arrTotal[0][i] += arrTotal[0][i - 1];
				arrTotal[1][i] += arrTotal[1][i - 1];
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return arrTotal;
		}
	}
	public static final Vector getDPLog(Date startDate, Date endDate) {
		return getDPLog(-1,startDate,endDate);
	}
	public static final Vector getDPLog(final long workUnitID) {
		return getDPLog(workUnitID,null,null);
	}
	public static final Vector getDPLog(long workUnitID,Date startDate, Date endDate) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		Vector vt = new Vector();
		ResultSet rs = null;
		String constraint="";
		if (workUnitID >0) 
			constraint =" AND DPL.WORKUNITID = ? ";
		if (startDate!=null)
			constraint =constraint+" AND TARGETDATE >= ? ";
		if (endDate!=null)
			constraint =constraint+" AND TARGETDATE <= ? ";
		try {
			if (workUnitID != Parameters.SQA_WU) 
				sql =
					"SELECT DPL.*,ACCOUNT, WORKUNITNAME, PROJECT.CODE,PROJECT.PROJECT_ID, PROJECT.GROUP_NAME,PARENTWORKUNITID"
					+" FROM DPLOG DPL,DEVELOPER,WORKUNIT, PROJECT"
					+" WHERE PROJECT.PROJECT_ID = WORKUNIT.TABLEID"
					+constraint
					+" AND WORKUNIT.TYPE="+WorkUnitInfo.TYPE_PROJECT
					+" AND DPL.DEVELOPER_ID = DEVELOPER.DEVELOPER_ID"
					+" AND WORKUNIT.WORKUNITID = DPL.WORKUNITID ORDER BY DPL.CREATEDATE";
			else
				sql =
					"SELECT DPL.*,ACCOUNT, WORKUNITNAME,PARENTWORKUNITID"
					+" FROM DPLOG DPL,DEVELOPER,WORKUNIT"
					+" WHERE DPL.DEVELOPER_ID = DEVELOPER.DEVELOPER_ID"
					+constraint
					+" AND WORKUNIT.WORKUNITID = DPL.WORKUNITID ORDER BY DPL.CREATEDATE";
			
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			int i=1;
			if (workUnitID >0) 
				stm.setLong(i++, workUnitID);
			if (startDate!=null)
				stm.setDate(i++, startDate);
			if (endDate!=null)
				stm.setDate(i++, endDate);
			rs = stm.executeQuery();
			while (rs.next()) {
				final DPLogInfo info = new DPLogInfo();
				info.dplogID = rs.getLong("DPACTIONID");
				info.workunitID = rs.getLong("WORKUNITID");
				info.parentWorkunitID= rs.getLong("PARENTWORKUNITID");
				info.dpaction = rs.getString("DPACTION");
				info.dpcode = rs.getString("DPCODE");
				info.processID = rs.getInt("PROCESSID");
				info.commonDefCode = rs.getString("COMMDEFCODE");
				info.createDate = rs.getDate("CREATEDATE");
				info.targetDate = rs.getDate("TARGETDATE");
				info.closedDate = rs.getDate("CLOSEDDATE");
				info.dpStatus = rs.getInt("DPSTATUS");
				info.dpBenefit = rs.getString("DPBENEFIT");
				info.dpNote = rs.getString("DPNOTE");
				info.devID = rs.getLong("DEVELOPER_ID");
				info.devAccount = rs.getString("ACCOUNT");
				info.wuName = rs.getString("WORKUNITNAME");
				
				
				if (workUnitID != Parameters.SQA_WU) 
				{
					info.projectCode = rs.getString("CODE");
					info.groupName = rs.getString("GROUP_NAME");
					info.projectID=rs.getLong("PROJECT_ID");
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
	public static final Vector getDPLogDatabase(final long workUnitID, final java.sql.Date beginMonth, final java.sql.Date endMonth, final String strWhere) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		Vector vt = null;
		ResultSet rs = null;
		try {
			sql =
				"SELECT c.*, PROJECT.CODE, PROJECT.GROUP_NAME"
					+ " FROM WORKUNIT a,WORKUNIT b, PROJECT, DPLOG c"
					+ " WHERE a.type(+) = 2 AND"
					+ " PROJECT.PROJECT_ID = a.TABLEID (+)"
					+ " AND a.PARENTWORKUNITID = b.WORKUNITID (+)"
					+ " AND (a.PARENTWORKUNITID =? OR b.PARENTWORKUNITID=?)"
					+ " AND a.WORKUNITID = c.WORKUNITID"
					+ " AND c.TARGETDATE >=?"
					+ " AND c.TARGETDATE <=?"
					+ strWhere
					+ " ORDER BY PROJECT.GROUP_NAME, PROJECT.CODE";
					
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, workUnitID);
			stm.setLong(2, workUnitID);
			stm.setDate(3, beginMonth);
			stm.setDate(4, endMonth);
			rs = stm.executeQuery();
			vt = new Vector();
			
			if (rs != null) {
				while (rs.next()) {
					final DPLogInfo info = new DPLogInfo();
					info.dplogID = rs.getLong("DPACTIONID");
					info.workunitID = rs.getLong("WORKUNITID");
					info.projectCode = rs.getString("CODE");
					info.groupName = rs.getString("GROUP_NAME");
					info.dpaction = rs.getString("DPACTION");
					info.dpcode = rs.getString("DPCODE");
					info.processID = rs.getInt("PROCESSID");
					info.commonDefCode = rs.getString("COMMDEFCODE");
					info.createDate = rs.getDate("CREATEDATE");
					info.targetDate = rs.getDate("TARGETDATE");
					info.closedDate = rs.getDate("CLOSEDDATE");
					info.dpStatus = rs.getInt("DPSTATUS");
					info.dpBenefit = rs.getString("DPBENEFIT");
					info.dpNote = rs.getString("DPNOTE");
					info.devID = rs.getLong("DEVELOPER_ID");
					info.linkTo = "Fms1Servlet?reqType=" + Constants.DEFECT_LOG;
					vt.add(info);
				}
			}

			if ((workUnitID == Parameters.FSOFT_WU) || (workUnitID == Parameters.SQA_WU)) {
				sql = "SELECT * FROM DPLOG c WHERE WORKUNITID = ?"
					+ " AND c.TARGETDATE >=? AND c.TARGETDATE <=?"
					+ strWhere;
				
				stm = conn.prepareStatement(sql);
				stm.setLong(1, Parameters.SQA_WU);
				stm.setDate(2, beginMonth);
				stm.setDate(3, endMonth);
				rs = stm.executeQuery();
				
				if (rs != null) {
					while (rs.next()) {
						final DPLogInfo info = new DPLogInfo();
						info.dplogID = rs.getLong("DPACTIONID");
						info.workunitID = rs.getLong("WORKUNITID");
						info.projectCode = "";
						info.groupName = Parameters.SQA_ROLE;
						info.dpaction = rs.getString("DPACTION");
						info.dpcode = rs.getString("DPCODE");
						info.processID = rs.getInt("PROCESSID");
						info.commonDefCode = rs.getString("COMMDEFCODE");
						info.createDate = rs.getDate("CREATEDATE");
						info.targetDate = rs.getDate("TARGETDATE");
						info.closedDate = rs.getDate("CLOSEDDATE");
						info.dpStatus = rs.getInt("DPSTATUS");
						info.dpBenefit = rs.getString("DPBENEFIT");
						info.dpNote = rs.getString("DPNOTE");
						info.devID = rs.getLong("DEVELOPER_ID");
						vt.add(info);
					}
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return vt;
		}
	}
	public static boolean addDPLog(DPLogInfo info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql = "INSERT INTO DPLOG VALUES ((SELECT NVL(MAX(DPACTIONID)+1,1) FROM DPLOG), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, info.workunitID);
			stm.setString(2, info.dpaction);
			stm.setString(3, info.dpcode);
			stm.setInt(4, info.processID);
			stm.setString(5, info.commonDefCode);
			stm.setDate(6, info.createDate);
			stm.setDate(7, info.targetDate);
			stm.setDate(8, info.closedDate);
			stm.setInt(9, info.dpStatus);
			stm.setString(10, info.dpBenefit);
			stm.setString(11, info.dpNote);
			stm.setLong(12, info.devID);
			if (stm.executeUpdate() < 1){
				conn.rollback();
				return false;
			}
			
			sql = "UPDATE DPLOG SET DPCODE = ? || DPACTIONID WHERE WORKUNITID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, info.dpcode);
			stm.setLong(2, info.workunitID);
			if (stm.executeUpdate() < 1){
				conn.rollback();
				return false;
			}
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static void deleteDPLog(final long dplogid, final String dpcode) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql = "DELETE FROM DPLOG WHERE DPACTIONID=?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, dplogid);
			stm.executeUpdate();
			stm.close();

			sql = "UPDATE COMMDEF SET DPCODE = 'N/A' WHERE DPCODE=?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, dpcode);
			stm.executeUpdate();
			stm.close();

			conn.commit();
			conn.setAutoCommit(true);
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static boolean updateDPLog(DPLogInfo info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql =
				"UPDATE DPLOG SET DPACTION = ?, DPCODE = ?, PROCESSID = ?, COMMDEFCODE = ?, CREATEDATE = ?, "
					+ "TARGETDATE = ?, CLOSEDDATE = ?, DPSTATUS = ?,  DPBENEFIT = ?, DPNOTE = ?, DEVELOPER_ID = ? "
					+ "WHERE DPACTIONID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, info.dpaction);
			stm.setString(2, info.dpcode);
			stm.setInt(3, info.processID);
			stm.setString(4, info.commonDefCode);
			stm.setDate(5, info.createDate);
			stm.setDate(6, info.targetDate);
			stm.setDate(7, info.closedDate);
			stm.setInt(8, info.dpStatus);
			stm.setString(9, info.dpBenefit);
			stm.setString(10, info.dpNote);
			stm.setLong(11, info.devID);
			stm.setLong(12, info.dplogID);
			if (stm.executeUpdate() < 1){
				conn.rollback();
				return false;
			}
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final Vector getCommDefDatabase(final long workUnitID) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		Vector vt = null;
		ResultSet rs = null;
		try {
			sql =
				"SELECT c.*, PROJECT.CODE, PROJECT.GROUP_NAME"
					+ " FROM WORKUNIT a,WORKUNIT b, PROJECT, COMMDEF c"
					+ " WHERE a.type(+) = 2 AND"
					+ " PROJECT.PROJECT_ID = a.TABLEID (+)"
					+ " AND a.PARENTWORKUNITID = b.WORKUNITID (+)"
					+ " AND (a.PARENTWORKUNITID =? OR b.PARENTWORKUNITID=?)"
					+ " AND PROJECT.PROJECT_ID = c.PROJECTID";
			conn = ServerHelper.instance().getConnection();
			stm = conn.prepareStatement(sql);
			stm.setLong(1, workUnitID);
			stm.setLong(2, workUnitID);
			rs = stm.executeQuery();
			if (rs != null) {
				vt = new Vector();
				while (rs.next()) {
					final CommDefInfo info = new CommDefInfo();
					info.commdefID = rs.getLong("COMMDEFID");
					info.prjID = rs.getLong("PROJECTID");
					info.projectCode = rs.getString("CODE");
					info.groupName = rs.getString("GROUP_NAME");
					info.commdef = rs.getString("DEFECTDES");
					info.commonDefCode = rs.getString("COMMDEFCODE");
					info.dpcode = rs.getString("DPCODE");
					info.defecttype = rs.getInt("DEFTYPE");
					info.rootcause = rs.getString("ROOTCAUSE");
					info.causecate = rs.getInt("CAUSECATE");
					vt.add(info);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return vt;
		}
	}
	public static final Vector getCommDef(final long prjID) {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		Vector vt = null;
		ResultSet rs = null;
		try {
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM COMMDEF WHERE PROJECTID = " + prjID + " ORDER BY COMMDEFID";
			rs = stm.executeQuery(sql);
			if (rs != null) {
				vt = new Vector();
				while (rs.next()) {
					final CommDefInfo info = new CommDefInfo();
					info.commdefID = rs.getLong("COMMDEFID");
					info.prjID = rs.getLong("PROJECTID");
					info.commdef = rs.getString("DEFECTDES");
					info.commonDefCode = rs.getString("COMMDEFCODE");
					info.dpcode = rs.getString("DPCODE");
					info.defecttype = rs.getInt("DEFTYPE");
					info.rootcause = rs.getString("ROOTCAUSE");
					info.causecate = rs.getInt("CAUSECATE");
					vt.add(info);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, rs);
			return vt;
		}
	}
	public static boolean addCommDef(CommDefInfo info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
            long commDefId = ServerHelper.getNextSeq("COMMDEF_SEQ");
			sql = "INSERT INTO COMMDEF VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(sql);
            stm.setLong(1, commDefId);
			stm.setLong(2, info.prjID);
			stm.setString(3, info.commdef);
			stm.setString(4, info.commonDefCode + Long.toString(commDefId));
			stm.setString(5, info.dpcode);
			stm.setInt(6, info.defecttype);
			stm.setString(7, info.rootcause);
			stm.setInt(8, info.causecate);
			if (stm.executeUpdate() != 1){
				conn.rollback();
				return false;
			}
			return true;
		}
        catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static void deleteCommDef(final long commdefid, final String commdefcode) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			sql = "DELETE FROM COMMDEF WHERE COMMDEFID = ?";
			stm = conn.prepareStatement(sql);
			stm.setLong(1, commdefid);
			stm.executeUpdate();
			stm.close();

			sql = "UPDATE DPLOG SET COMMDEFCODE = 'N/A' WHERE COMMDEFCODE = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, commdefcode);
			stm.executeUpdate();
			stm.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static boolean updateCommDef(CommDefInfo info) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		try {
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			sql = "UPDATE COMMDEF SET DEFECTDES = ?, DPCODE = ?, DEFTYPE = ?, " + "ROOTCAUSE = ?, CAUSECATE = ? " + "WHERE COMMDEFID = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, info.commdef);
			stm.setString(2, info.dpcode);
			stm.setInt(3, info.defecttype);
			stm.setString(4, info.rootcause);
			stm.setInt(5, info.causecate);
			stm.setLong(6, info.commdefID);
			if (stm.executeUpdate() < 1){
				conn.rollback();
				return false;
			}
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, stm, null);
		}
	}
	public static final Vector getDefectType() {
		Connection conn = null;
		Statement stm = null;
		String sql = null;
		Vector vt = null;
		ResultSet rs = null;
		try {
			vt = new Vector();
			conn = ServerHelper.instance().getConnection();
			stm = conn.createStatement();
			sql = "SELECT * FROM DEFECT_TYPE ORDER BY DT_ID";
			rs = stm.executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					final DTInfo info = new DTInfo();
					info.defect_type_id = rs.getInt("DT_ID");
					info.name = rs.getString("NAME");
					vt.add(info);
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
    //Get norm by product type (Work product)
    //isDefectRate=true: Norm Defect Rate 
    //isDefectRate=false: Norm Removal Efficiency
    //Return NormInfo
    public static final NormInfo getNormByProductType(long projID,long wpID,boolean isDefectRate){      
        int tempMetric;                         
        switch((int)wpID){              
            case WorkProductInfo.URD:               
                tempMetric=isDefectRate?MetricDescInfo.DR_URD:MetricDescInfo.DRE_URD;       
                break;
            case WorkProductInfo.SRS:               
                tempMetric=isDefectRate?MetricDescInfo.DR_SRS:MetricDescInfo.DRE_SRS;               
                break;
            case WorkProductInfo.REQUIREMENT_PROTOTYPE:             
                tempMetric=isDefectRate?MetricDescInfo.DR_RPROTOTYPE:MetricDescInfo.DRE_RPROTOTYPE;
                break;
            case WorkProductInfo.ARCHITECTURAL_DESIGN:              
                tempMetric=isDefectRate?MetricDescInfo.DR_ADD:MetricDescInfo.DRE_ADD;
                break;
            case WorkProductInfo.DETAILED_DESIGN:               
                tempMetric=isDefectRate?MetricDescInfo.DR_DDD:MetricDescInfo.DRE_DDD;       
                break;
            case WorkProductInfo.DESIGN_PROTOTYPE:          
                tempMetric=isDefectRate?MetricDescInfo.DR_DPROTOTYPE:MetricDescInfo.DRE_DPROTOTYPE;
                break;      
            case WorkProductInfo.SOFTWARE_PACKAGE:              
                tempMetric=isDefectRate?MetricDescInfo.DR_SOFTWARE_PACKAGE:MetricDescInfo.DRE_SOFTWARE_PACKAGE;     
                break;
            case WorkProductInfo.TEST_CASE_DATA:            
                tempMetric=isDefectRate?MetricDescInfo.DR_TC_TDATA:MetricDescInfo.DRE_TC_TDATA;     
                break;      
            case WorkProductInfo.USER_MANUAL:           
                tempMetric=isDefectRate?MetricDescInfo.DR_USER_MANUAL:MetricDescInfo.DRE_USER_MANUAL;       
                break;
            default:                
                tempMetric=isDefectRate?MetricDescInfo.DR_OTHERS:MetricDescInfo.DRE_OTHERS; //Others                
        }               
        
		ProjectInfo info2 = Project.getProjectInfo(projID);
        return Norms.getNorm(info2.getParent(), info2.getGrandParent(), Metrics.getMetricID(tempMetric),info2.getLifecycleId(), info2.getStartDate());
    }   

    public static final void getPlannedQcStage(DreByStageInfo info, long projectId) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        DreByStageInfo.Row row = info.new Row();
        try {
            // Clear check exist flags (all milestones was not found by default)
            for (int i = 0; i < info.stages; i++) {
                info.milestoneExisted[i] = false;
            }
            sql = "SELECT QCID,MILESTONEID,PLAN,REPLAN, REPLAN_DEFECT_RATE" +
                " FROM PLANS_QC_STAGE P, MILESTONE M" +
                " WHERE P.MILESTONEID=M.MILESTONE_ID" +
                " AND M.PROJECT_ID=?" +
                " ORDER BY QCID,M.ACTUAL_FINISH_DATE";
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setLong(1, projectId);
            rs = stm.executeQuery();
            int nCurrentQc = 0;
            long nMilestone;
            int nLastQc = -1;
            info.planRecords = 0;
            while (rs.next()) {
                nCurrentQc = rs.getInt("QCID");
                if (nLastQc != nCurrentQc) {
                    row = (DreByStageInfo.Row) info.rows.get(nCurrentQc - 1);
                    nLastQc = nCurrentQc;
                }
                nMilestone = rs.getLong("MILESTONEID");
                for (int i = 0; i < info.stages; i++) {
                    // Find the correct stage to set plan/replan values
                    if (info.milestone_Id[i] == nMilestone) {
                        row.plans[i] = Db.getDouble(rs, "PLAN");
                        row.replans[i] = Db.getDouble(rs, "REPLAN");
                        row.replan_defect_rate[i] = Db.getDouble(rs, "REPLAN_DEFECT_RATE");
                        info.planRecords++;     // Increase records count
                        info.milestoneExisted[i] = true;
                        break;
                    }
                }
            }
            // Check consistency between project milestones and PLANS_QC_STAGE milestones
            info.isConsistent = true;
            for (int i = 0; i < info.stages; i++) {
                if (! info.milestoneExisted[i]) {
                    info.isConsistent = false;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
        }
    }
    public static final String updatePlannedQcStage(DreByStageInfo info, HttpServletRequest request) {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        ResultSet rs = null;
        String strMsg = "Defect plan updated successfully";
        long milestone_Id;
        String plan;
        try {
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            /* Please be careful:
             * - If project has been inserted new Milestone(s) after it has been
             * planned/replanned Defect Removal Efficiency then program
             * should correct this problem by UPDATE current values and INSERT
             * other plans for added milestones that submitted from client
             * Solution 1: warning and force user to update those values in
             * plan DRE screen before allow user to update replan values.
             * - If project has been removed some Milestone(s), db will remove
             * planned/replanned values automatically by db foreign k constraint
             * but plans total values will be incorrected (<> 100%) because plan
             * for the removed milestone has been lost => should warning user about this issue
             * */
            for (int iStage = 0; iStage < info.stages; iStage++) {
                milestone_Id = info.milestone_Id[iStage];
                // If milestone existed => UPDATE, else => INSERT
                if (info.milestoneExisted[iStage]) {
                    for (int iQc = 0; iQc < info.rows.size(); iQc++) {
                        plan = (request.getParameter("plan." + iQc + "." + milestone_Id) != null) ?
                                (request.getParameter("plan." + iQc + "." + milestone_Id)) : "";
                        sql = "UPDATE PLANS_QC_STAGE SET" + " plan='" + plan + 
                            "' WHERE qcid=" + (iQc + 1) +
                            " AND milestoneid=" + milestone_Id;
                        stm.addBatch(sql);
                    }
                }
                else {
                    for (int iQc = 0; iQc < info.rows.size(); iQc++) {
                        plan = (request.getParameter("plan." + iQc + "." + milestone_Id) != null) ?
                                (request.getParameter("plan." + iQc + "." + milestone_Id)) : "";
                        sql = "INSERT INTO PLANS_QC_STAGE VALUES(" + (iQc + 1) + "," +
                            milestone_Id + ",'" + plan + "',NULL,NULL)";
                        stm.addBatch(sql);
                    }
                }
            }
            stm.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            strMsg = "Error occured during update";
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return strMsg;
        }
    }
    /**
     * Update old style planned defects (for projects that have been not applied PPM)
     * @param info
     * @param projectID
     */
    public static final void updateForProjectPlan(DreByStageInfo info, long projectID) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            double plan_RReview = Math.round(CommonTools.arrayRoundSum(((DreByStageInfo.Row) info.rows.get(0)).plan_wd));
            double plan_DReview = Math.round(CommonTools.arrayRoundSum(((DreByStageInfo.Row) info.rows.get(1)).plan_wd));
            double plan_CReview = Math.round(CommonTools.arrayRoundSum(((DreByStageInfo.Row) info.rows.get(2)).plan_wd));
            double plan_UTest = Math.round(CommonTools.arrayRoundSum(((DreByStageInfo.Row) info.rows.get(3)).plan_wd));
            double plan_ITest = Math.round(CommonTools.arrayRoundSum(((DreByStageInfo.Row) info.rows.get(4)).plan_wd));
            double plan_STest = Math.round(CommonTools.arrayRoundSum(((DreByStageInfo.Row) info.rows.get(5)).plan_wd));
            double plan_OReview = Math.round(CommonTools.arrayRoundSum(((DreByStageInfo.Row) info.rows.get(6)).plan_wd));
            double plan_OTest = Math.round(CommonTools.arrayRoundSum(((DreByStageInfo.Row) info.rows.get(7)).plan_wd));
            //double plan_Other = Math.round(CommonTools.arrayRoundSum(((DreByStageInfo.Row) info.rows.get(8)).plan_wd));
            sql = "UPDATE PROJECT_PLAN SET" +
                " REQPLANREVIEWDEFECT=?,DESPLANREVIEWDEFECT=?,CODPLANREVIEWDEFECT=?," +  
                " CODPLANTESTDEFECT=?,DESPLANTESTDEFECT=?,REQPLANTESTDEFECT=?," +  
                " OTHPLANREVIEWDEFECT=?," +  
                " othplantestdefect=?" +  
                " WHERE PROJECT_ID=" + projectID;
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setDouble(1, plan_RReview);
            stm.setDouble(2, plan_DReview);
            stm.setDouble(3, plan_CReview);
            stm.setDouble(4, plan_UTest);
            stm.setDouble(5, plan_ITest);
            stm.setDouble(6, plan_STest);
            stm.setDouble(7, plan_OReview);
            stm.setDouble(8, plan_OTest);
            stm.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            try {
				conn.rollback();
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
        }
    }
    /**
     * Update old style re-planned defects (for projects that have been not applied PPM)
     * @param info
     * @param projectID
     */
    public static final void updateForProjectReplan(DreByStageInfo info, long projectID) {
        Connection conn = null;
        PreparedStatement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            double plan_RReview = CommonTools.arraySum(((DreByStageInfo.Row) info.rows.get(0)).replans);
            double plan_DReview = CommonTools.arraySum(((DreByStageInfo.Row) info.rows.get(1)).replans);
            double plan_CReview = CommonTools.arraySum(((DreByStageInfo.Row) info.rows.get(2)).replans);
            double plan_UTest = CommonTools.arraySum(((DreByStageInfo.Row) info.rows.get(3)).replans);
            double plan_ITest = CommonTools.arraySum(((DreByStageInfo.Row) info.rows.get(4)).replans);
            double plan_STest = CommonTools.arraySum(((DreByStageInfo.Row) info.rows.get(5)).replans);
            double plan_OReview = CommonTools.arraySum(((DreByStageInfo.Row) info.rows.get(6)).replans);
            double plan_OTest = CommonTools.arraySum(((DreByStageInfo.Row) info.rows.get(7)).replans);
            //double plan_Other = CommonTools.arraySum(((DreByStageInfo.Row) info.rows.get(8)).replans);
            sql = "UPDATE PROJECT_PLAN SET" +
                " REQREPLANREVIEWDEFECT=?,DESREPLANREVIEWDEFECT=?,CODREPLANREVIEWDEFECT=?," +  
                " CODREPLANTESTDEFECT=?,DESREPLANTESTDEFECT=?,REQREPLANTESTDEFECT=?," +  
                " OTHREPLANREVIEWDEFECT=?," +  
                " othreplantestdefect=?" +  
                " WHERE PROJECT_ID=" + projectID;
            conn = ServerHelper.instance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setDouble(1, plan_RReview);
            stm.setDouble(2, plan_DReview);
            stm.setDouble(3, plan_CReview);
            stm.setDouble(4, plan_UTest);
            stm.setDouble(5, plan_ITest);
            stm.setDouble(6, plan_STest);
            stm.setDouble(7, plan_OReview);
            stm.setDouble(8, plan_OTest);
            stm.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
				conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
        }
    }
    /**
     * Update re-plan Defect Removal Efficiency
     * @param info
     * @param request
     * @return
     */
    public static final String updateDreReplan(DreByStageInfo info, HttpServletRequest request) {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        ResultSet rs = null;
        String strMsg = "Defect replan updated successfully";
        long milestone_Id;
        String replan;
        try {
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            for (int iStage = info.runningStage - 1; iStage < info.stages; iStage++) {
                milestone_Id = info.milestone_Id[iStage];
                for (int iQc = 0; iQc < info.rows.size(); iQc++) {
                    replan = (request.getParameter("replan." + iQc + "." + milestone_Id) != null) ?
                            (request.getParameter("replan." + iQc + "." + milestone_Id)) : "";
                    sql = "UPDATE PLANS_QC_STAGE SET" + " replan='" + replan + 
                        "' WHERE qcid=" + (iQc + 1) +
                        " AND milestoneid=" + milestone_Id;
                    stm.addBatch(sql);
                }
            }
            stm.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            strMsg = "Error occured during update";
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return strMsg;
        }
    }
    public static void calcDreByStage(long projectID, DreByStageInfo result,
            Vector stageVt, int[] stdStageCount, Vector normsQc, Vector normsQcStage) {
        result.stageCoeff = new int [stageVt.size()];
        result.standardStageMap = new int [stageVt.size()];
        result.milestone_Id = new long [stageVt.size()];
        result.runningStage = 1;    // Default running stage is the first
        for (int i = 0; i < stageVt.size(); i++) {
            StageInfo stInfo = (StageInfo) stageVt.get(i);
            result.standardStageMap[i] = stInfo.StandardStage;
            result.milestone_Id[i] = stInfo.milestoneID;
            result.stageNames.add(stInfo.stage);
            if (stInfo.StandardStage > 0) {
                result.stageCoeff[i] = stdStageCount[stInfo.StandardStage - 1];
            }
            else {
                result.stageCoeff[i] = 1;
            }
            // Please note: latest milestone's actual end date is project actual end date
            if (stInfo.aEndD != null) {
                result.runningStage = i + 2;
            }
        }
        //result.normQC = new double [normsQc.size()];
        for (int i = 0; i < normsQc.size(); i++) {
            result.normQC[i] = ((NormInfo) normsQc.get(i)).average;
        }
        if (normsQcStage.size() > 0) {
            // Calculate Norms of project by QC by stage
            for (int iQc = 0; iQc < DreByStageInfo.QC_ACTIVITIES; iQc++ ) {
                DreByStageInfo.Row row = (DreByStageInfo.Row) result.rows.get(iQc);
                for (int iSt = 0; iSt < stageVt.size(); iSt++) {
                    if (result.standardStageMap[iSt] > 0) {
                        // Norms value is distributed by standard stages
                        // For real project stages, should use standard stages mapping 
                        NormInfo info = (NormInfo) normsQcStage.get(
                                ((iQc * DreByStageInfo.STANDARD_STAGES) + result.standardStageMap[iSt] - 1));
                        row.norms[iSt] = info.average;
                    }
                    if (iSt < result.runningStage) {
                        row.actuals[iSt] = 0;
                    }
                }
            }
        }
        // Get planned defects, then calculate the total line
        Defect.getPlannedQcStage(result, projectID);
        result.calulateSumRow();
    }
    public static final Vector getDefectByQcStage(Vector stageVt, int runningStage) {
        Vector ret = new Vector();
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            sql = "SELECT milestone_no" +
                ",SUM(CASE WHEN ((qc_id=20 AND process_id=2) OR (qc_id=23 AND wp_id=21)) THEN weight ELSE NULL END) as RReview" +
                ",SUM(CASE WHEN ((qc_id=20 AND process_id=3) OR (qc_id=23 AND wp_id=42)) THEN weight ELSE NULL END) as DReview" +
                ",SUM(CASE WHEN (qc_id=21) THEN weight ELSE NULL END) as CReview" +
                ",SUM(CASE WHEN (qc_id=10) THEN weight ELSE NULL END) as UTest" +
                ",SUM(CASE WHEN (qc_id=11) THEN weight ELSE NULL END) as ITest" +
                ",SUM(CASE WHEN (qc_id=12) THEN weight ELSE NULL END) as STest" +
                ",SUM(CASE WHEN (qc_id=24) THEN weight ELSE NULL END) as OReview" +
                ",SUM(CASE WHEN (qc_id=16) THEN weight ELSE NULL END) as OTest" +
                ",SUM(CASE WHEN (qc_id NOT IN(21,10,11,12,24,16)" +
                        " AND NOT((qc_id=20 AND process_id=2) OR (qc_id=23 AND wp_id=21))" +
                        " AND NOT((qc_id=20 AND process_id=3) OR (qc_id=23 AND wp_id=42))" +
                        ") THEN weight ELSE NULL END) as Other"+
                " FROM (" + buildDefectByStageSql(stageVt, runningStage) + ")" +
                " WHERE ds_id<>6" +
                " GROUP BY milestone_no";
            
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                final int[] arrSum = new int[10];
                arrSum[0] = rs.getInt("milestone_no");
                arrSum[1] = rs.getInt("RReview");
                arrSum[2] = rs.getInt("DReview");
                arrSum[3] = rs.getInt("CReview");
                arrSum[4] = rs.getInt("UTest");
                arrSum[5] = rs.getInt("ITest");
                arrSum[6] = rs.getInt("STest");
                arrSum[7] = rs.getInt("OReview");
                arrSum[8] = rs.getInt("OTest");
                arrSum[9] = rs.getInt("Other");
                ret.add(arrSum); 
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return ret;
        }
    }
    public static final String buildDefectByStageSql(Vector stageVt, int runningStage) {
        String strRet = "";
        String strStartDate = null;
        String strEndDate = null;
        final String strDateFormat = "dd-MMM-yyyy";
        final String strSqlDateFormat = "dd-Mon-yyyy";
        final java.text.SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        
        for (int i = 1; ((i <= runningStage) && (i <= stageVt.size())); i++) {
            StageInfo info = (StageInfo) stageVt.get(i - 1);
            strEndDate = LATEST_END_DATE;
            if (info.aEndD != null) {
                strEndDate = dateFormat.format(info.aEndD);
            }
            else if (info.pEndD != null) {
                strEndDate = dateFormat.format(info.pEndD);
            }
            else if (info.bEndD != null) {
                strEndDate = dateFormat.format(info.bEndD);
            }
            if (i == 1) {   // First stage;
                strStartDate = EARLY_START_DATE;
            }
            else if (i == stageVt.size()) { // Last stage;
                strEndDate = LATEST_END_DATE;
            }
            strRet += getStageSql(i, info.prjID, strStartDate, strEndDate, strSqlDateFormat);

            if (info.aEndD != null) {
                strStartDate = dateFormat.format(info.aEndD);  // Begin date for next stage
            }
            else {
                break;  // Exit loop when go to the first stage that not has close date
            }
        }
        return strRet;
    }
    
    //HuyNH2 modify this function for project archive
    public static final String getStageSql(int milestone_no, long projectID,
            String strStartDate, String strEndDate, String strDateFormat) {
        String strRet = "";
        String defectTable = "DEFECT";
//        if(Db.checkProjecIsArchive(projectID)){
//            defectTable = "DEFECT_ARCHIVE";
//        }        
        if (milestone_no > 1) {   // UNION with all stages
            strRet = " UNION ALL ";
        }
        strRet += "SELECT qa_id as qc_id,process_id,wp_id,weight," +
                    milestone_no + " as milestone_no,ds_id" +
            " FROM " + defectTable + " d,defect_severity ds" +
            " WHERE d.defs_id=ds.defs_id" +
            " AND project_id=" + projectID +
            " AND create_date > TO_DATE('" + strStartDate + "','" + strDateFormat + "')" +
            " AND create_date <= TO_DATE('" + strEndDate + "','" + strDateFormat + "')";
        return strRet;
    }
    public static final Vector getDefectByStageStatus(Vector stageVt, int runningStage) {
        Vector ret = new Vector();
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        ResultSet rs = null;
        try {
            sql = "SELECT milestone_no" +
                ",SUM(CASE WHEN ds_id=1 THEN weight ELSE 0 END) as ds1" +
                ",SUM(CASE WHEN ds_id=2 THEN weight ELSE 0 END) as ds2" +
                ",SUM(CASE WHEN ds_id=3 THEN weight ELSE 0 END) as ds3" +
                ",SUM(CASE WHEN ds_id=4 THEN weight ELSE 0 END) as ds4" +
                ",SUM(CASE WHEN ds_id=5 THEN weight ELSE 0 END) as ds5" +
                ",SUM(CASE WHEN ds_id=6 THEN weight ELSE 0 END) as ds6" +
                ",SUM(weight) as total" +
                " FROM (" + buildDefectByStageSql(stageVt, runningStage) + ")" +
                " GROUP BY milestone_no";

            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                final int[] arrSum = new int[8];
                arrSum[0] = rs.getInt("milestone_no");
                arrSum[1] = rs.getInt("ds1");
                arrSum[2] = rs.getInt("ds2");
                arrSum[3] = rs.getInt("ds3");
                arrSum[4] = rs.getInt("ds4");
                arrSum[5] = rs.getInt("ds5");
                arrSum[6] = rs.getInt("ds6");
                arrSum[7] = rs.getInt("total");
                ret.add(arrSum); 
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return ret;
        }
    }
    public static void calcDREPlanReplan(long projectID, ProjectSizeInfo projectInfo,
            DefectInfo defectInfo, DreByStageInfo dreByStage, Vector stageVt) {
        final Vector moduleList = WorkProduct.getModuleListSize(projectID);
        Vector vtActualWD = new Vector();
        if (stageVt.size() > 0) {
            vtActualWD = Defect.getDefectByQcStage(stageVt, dreByStage.runningStage);
        }
        // Get actual Weighted Defect
        for (int i = 0; i < vtActualWD.size(); i++) {
            int[] arrSum = (int[]) vtActualWD.get(i);
            int milestone_no = arrSum[0];
            for (int iQc = 0; iQc < DreByStageInfo.QC_ACTIVITIES; iQc++) {
                DreByStageInfo.Row row = (DreByStageInfo.Row) dreByStage.rows.get(iQc);
                row.actuals[milestone_no - 1] = arrSum[iQc + 1];
            }
        }
        // Get plan/replan defects
        Defect.getPlannedQcStage(dreByStage, projectID);
        
        // double estimatedUpTo = 0;
        // Calculate project (re-)estimate size, (re-)estimate size up to stage
        for (int i = 0; i < stageVt.size(); i++) {
            StageInfo stageInfo = (StageInfo)stageVt.elementAt(i);
            final WPSizeInfo info = WorkProduct.getWPSizeSumByStage(projectID, stageInfo, moduleList);
            double estimated = Double.NaN;
            double estimatedUpTo = 0;
            if (! Double.isNaN(info.reestimatedSize)) {
                estimated = info.reestimatedSize;
            }
            else if (! Double.isNaN(info.estimatedSize)) {
                estimated = info.estimatedSize;
            }
            if (! Double.isNaN(estimated)) {
                estimatedUpTo += estimated;
            }
            dreByStage.estimatedSize[i] = estimated;
            dreByStage.estimatedSizeUpTo[i] = estimatedUpTo;
            dreByStage.actualSizeUcp[i] = info.actualSize;
            dreByStage.actualSizeUcpUpTo[i] = CommonTools.arraySum(dreByStage.actualSizeUcp, 0, i);
        }
        double projectSize = projectInfo.totalEstimatedSize;
        if ((! Double.isNaN(projectInfo.totalReestimatedSize)) &&
                (projectInfo.totalReestimatedSize > 0)) {
            projectSize = projectInfo.totalReestimatedSize;
        }
        dreByStage.projectEstimatedSize = projectSize;
        
        dreByStage.calculatePlanWD(projectSize * defectInfo.normDefectRate,
                                    projectSize * defectInfo.targetLeakage);
        dreByStage.calculateDeviation();
        dreByStage.calulateSumRow();    // Calculate sum row without forecast
        //dreByStage.calculateReestimatedDefect(defectInfo.normDefectRate);
        dreByStage.calculateReestimatedDefect(defectInfo, projectInfo);
        dreByStage.calculateForecast(projectSize * defectInfo.targetLeakage);
        dreByStage.calulateSumRow();    // Calculate sum row include forecast
    }
	
    public static DreByStageInfo calcDREDefectRate(long projectID, ProjectSizeInfo projectInfo,
            DefectInfo defectInfo, DreByStageInfo dreByStage, Vector stageVt) {
        Defect.calcDREPlanReplan(projectID, projectInfo, defectInfo, dreByStage, stageVt);
        DreByStageInfo dreDefectRate = new DreByStageInfo(stageVt.size());
        dreDefectRate.runningStage = dreByStage.runningStage;
        dreDefectRate.estimatedSize = dreByStage.estimatedSize;
        
        // Use first row as model for Defect Rate table
        DreByStageInfo.Row rowPlan = (DreByStageInfo.Row) dreByStage.rows.get(0);
        DreByStageInfo.Row row = (DreByStageInfo.Row) dreDefectRate.rows.get(0);
        // Calculate Plan Defect Rate
        for (int i = 0; i < row.plans.length; i++) {
            row.replan_defect_rate[i] = rowPlan.replan_defect_rate[i];
            dreDefectRate.sumRow.plan_wd[i] = dreByStage.sumRow.plan_wd[i];
            if (dreDefectRate.estimatedSize[i] >= 0.01) {   // Avoid very large values of plan
                row.plans[i] = dreDefectRate.sumRow.plan_wd[i] / dreDefectRate.estimatedSize[i];
            }
            else {
                row.plans[i] = Double.NaN;
            }
            if (dreByStage.actualSizeUcp[i] >= 0.01) {
                row.actuals[i] = dreByStage.sumRow.actuals[i] / dreByStage.actualSizeUcp[i];
            }
            else {
                row.actuals[i] = Double.NaN;
        }
        }
        for (int i = 0; i < row.deviations.length; i++) {
            row.deviations[i] = CommonTools.metricDeviation(
                    row.plans[i], row.replan_defect_rate[i], row.actuals[i]);
        }
        
        return dreDefectRate;
    }
    public static final String updateDreDefectRate(DreByStageInfo info, HttpServletRequest request) {
        Connection conn = null;
        Statement stm = null;
        String sql = null;
        ResultSet rs = null;
        String strMsg = "Defect rate updated successfully";
        long milestone_Id;
        String replan;
        try {
            conn = ServerHelper.instance().getConnection();
            stm = conn.createStatement();
            for (int iStage = info.runningStage - 1; iStage < info.stages; iStage++) {
                milestone_Id = info.milestone_Id[iStage];
                // Defect Rate replan use QCID=1 (first line of dreByStage)
                replan = (request.getParameter("replan_dr." + milestone_Id) != null) ?
                        (request.getParameter("replan_dr." + milestone_Id)) : "";
                sql = "UPDATE PLANS_QC_STAGE SET replan_defect_rate='" + replan + 
                    "' WHERE qcid=1 AND milestoneid=" + milestone_Id;
                stm.addBatch(sql);
            }
            stm.executeBatch();
        }
        catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            strMsg = "Error occured during update";
        }
        finally {
            ServerHelper.closeConnection(conn, stm, rs);
            return strMsg;
        }
    }
    public static DreByStageInfo calcDRELeakage(long projectID, ProjectSizeInfo projectInfo,
            DefectInfo defectInfo, DreByStageInfo dreByStage, Vector stageVt) {
        Defect.calcDREPlanReplan(projectID, projectInfo, defectInfo, dreByStage, stageVt);
        DreByStageInfo dreDefectRate = new DreByStageInfo(stageVt.size());
        dreDefectRate.runningStage = dreByStage.runningStage;
        Vector vtWd = new Vector();
        if (stageVt.size() > 0) {
            vtWd = Defect.getDefectByStageStatus(stageVt, dreByStage.runningStage);
        }
        // Use first row as model for Leakage data
        DreByStageInfo.Row row = (DreByStageInfo.Row) dreDefectRate.rows.get(0);
        // Calculate Plan possible leakage
        double reestimatedDf = 0;
        if (dreByStage.runningStage <= dreByStage.stages) {
            reestimatedDf = dreByStage.reestimatedDefect[dreByStage.runningStage - 1];
        }
        else if (dreByStage.runningStage >= 2) {
            reestimatedDf = dreByStage.reestimatedDefect[dreByStage.runningStage - 2];
        }
        for (int i = 0; i < dreByStage.runningStage - 1; i++) {
            double actual = CommonTools.arraySum(dreByStage.sumRow.actuals, 0, i);
            row.plans[i] = reestimatedDf - actual; 
        }
        // All actuals
        double actual = CommonTools.arraySum(dreByStage.sumRow.actuals, 0, dreByStage.runningStage - 2);
        for (int i = dreByStage.runningStage - 1; i < row.actuals.length; i++) {
            double planwd = CommonTools.arraySum(dreByStage.sumRow.plan_wd, dreByStage.runningStage - 1, i);
            double replanwd = CommonTools.arraySum(dreByStage.sumRow.replans, dreByStage.runningStage - 1, i);
            double plan = replanwd;
            if (Double.isNaN(replanwd) || (replanwd == 0)) {
                plan = planwd;
            }
            row.plans[i] = dreByStage.reestimatedDefect[dreByStage.runningStage - 1] - actual - plan;
        }
        
        double[] arrSumWd = new double[stageVt.size()];
        for (int i = 0; i < arrSumWd.length; i++) {
            arrSumWd[i] = 0;
        }
        double total, open, cancel;
        for (int i = 0; i < vtWd.size(); i++) {
            int[] arrWd = (int[]) vtWd.get(i);
            total = arrWd[7];
            open = arrWd[1];
            cancel = arrWd[6];
            int milestone_no = arrWd[0];
            arrSumWd[milestone_no - 1] = (total - cancel - open);
        }
      
        // double estimatedSize = CommonTools.arraySum(dreByStage.estimatedSizeUpTo);
        for (int i = 0; i < stageVt.size(); i++) {
        // if (dreByStage.estimatedSizeUpTo[i - 1] != 0) {
        //     row.actuals[i] = Math.round(
        //         dreByStage.actualSizeUcpUpTo[i - 1] / dreByStage.estimatedSizeUpTo[i - 1] * 
        //             dreByStage.reestimatedDefect[i - 1] - arrSumWd[i]);
            if (i == 0) {
                row.actuals[i] = Double.NaN;
            }
            else {
                row.actuals[i] = Math.round(
                    dreByStage.actualSizeUcpUpTo[i - 1] / dreByStage.projectEstimatedSize * 
                    dreByStage.reestimatedDefect[i - 1] - arrSumWd[i]);
            }
        }
        
        for (int i = 0; i < row.deviations.length; i++) {
            row.deviations[i] = CommonTools.metricDeviation(
                    row.plans[i], row.plans[i], row.actuals[i]);
        }
        
        return dreDefectRate;
    }
    public static DreByStageInfo calcDREDefectChart(long projectID, ProjectSizeInfo projectInfo,
            DefectInfo defectInfo, DreByStageInfo dreByStage, Vector stageVt) {
        DreByStageInfo dreLeakage = Defect.calcDRELeakage(projectID,
                projectInfo, defectInfo, dreByStage, stageVt);
        for (int i = 0; i < dreByStage.stages; i++) {
            if (dreByStage.sumRow.plan_wd[i] != 0) {
                dreLeakage.sumRow.deviations[i] =
                    (dreByStage.sumRow.actuals[i] - dreByStage.sumRow.plan_wd[i]) /
                    dreByStage.sumRow.plan_wd[i];
            }
        }
        return dreLeakage;
    }
	public static long[] getTotalDefectsByQCActivity(final long projectID, final byte bQCActivityId, java.util.Date endDate) {
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		long[] result = new long[2];
		String dateConstraint;
		try {
			if ((endDate == null) || (endDate.getTime() == 0))
				dateConstraint = "";
			else {
				java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
				dateConstraint = " AND A.CREATE_DATE <='" + dateFormat.format(endDate) + "'";
			}
			conn = ServerHelper.instance().getConnection();
				sql =
					" SELECT COUNT(A.DEFECT_ID) N_DEF, SUM(B.WEIGHT) TOTAL_WEIGHTED_DEFECT "
						+ " FROM DEFECT A, DEFECT_SEVERITY B, qc_activity C"
						+ " WHERE A.DEFS_ID = B.DEFS_ID "
						+ " AND A.QA_ID = C.QA_ID"
						+ " AND A.PROJECT_ID = ? "
						+ " AND A.QA_ID = ?"
						+ " AND A.DS_ID <>6" //not cancelled
						+ dateConstraint;
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, projectID);
			prepStmt.setByte(2, bQCActivityId);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				result[0] = rs.getLong("N_DEF"); //number of defects
				result[1] = rs.getLong("TOTAL_WEIGHTED_DEFECT");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return result;
		}
	}
	
	public static final int doAddReviewProduct(final WPSizeInfo mInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;		

		try {
			conn = ServerHelper.instance().getConnection();
	
			sql =  " UPDATE MODULE"
				 + " SET IS_DEFECT_REVIEW = 1, "
				 + " NEW_PLAN_SIZE_REV = ? "
				 + " WHERE MODULE_ID = ?";
			
				stm = conn.prepareStatement(sql);
				if ( mInfo.newPlanSizeReview == -1) stm.setNull(1, Types.LONGVARCHAR);
				else stm.setDouble(1, mInfo.newPlanSizeReview);				
				stm.setLong(2, mInfo.moduleID);
				stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final int doAddTestProduct(final WPSizeInfo mInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;		

		try {
			conn = ServerHelper.instance().getConnection();
	
			sql =  " UPDATE MODULE"
				 + " SET IS_DEFECT_TEST = 1, "
				 + " NEW_PLAN_SIZE_TEST = ? "
				 + " WHERE MODULE_ID = ?";
			
				stm = conn.prepareStatement(sql);				
				if ( mInfo.newPlanSizeTest == -1) stm.setNull(1, Types.LONGVARCHAR);
				else stm.setDouble(1, mInfo.newPlanSizeTest);				
				stm.setLong(2, mInfo.moduleID);
				stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final int doUpdateReviewProduct(final WPSizeInfo mInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;		

		try {
			conn = ServerHelper.instance().getConnection();

			sql =  " UPDATE MODULE"
				 + " SET NEW_PLAN_SIZE_REV = ? "				 
				 + " WHERE MODULE_ID = ?";
			
					stm = conn.prepareStatement(sql);				
					if ( mInfo.newPlanSizeReview == -1) stm.setNull(1, Types.LONGVARCHAR);
					else stm.setDouble(1, mInfo.newPlanSizeReview);
					stm.setLong(2, mInfo.moduleID);
					stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	
	public static final int doUpdateTestProduct(final WPSizeInfo mInfo) {
		Connection conn = null;
		PreparedStatement stm = null;
		String sql = null;
		ResultSet rs = null;
		int iRet = 0;		

		try {
			conn = ServerHelper.instance().getConnection();

			sql =  " UPDATE MODULE"
				 + " SET NEW_PLAN_SIZE_TEST = ? "				 
				 + " WHERE MODULE_ID = ?";
			
					stm = conn.prepareStatement(sql);				
					if ( mInfo.newPlanSizeTest == -1) stm.setNull(1, Types.LONGVARCHAR);
					else stm.setDouble(1, mInfo.newPlanSizeTest);
					stm.setLong(2, mInfo.moduleID);
					stm.executeUpdate();
		
		} catch (Exception e) {
			iRet = 1;
			e.printStackTrace();
		} finally {			
			ServerHelper.closeConnection(conn, stm,null);
		}
 
		return iRet;
	}
	public static void updateDefectActualValue(Vector defectList)
	{
		Connection conn=null;
		PreparedStatement stm=null;
		String sql= null;
		ResultSet rs= null;
		try
		{
				conn= ServerHelper.instance().getConnection();
				for(int i=0;i<defectList.size();i++)
				{
					DPTaskInfo dPTaskInfo= (DPTaskInfo)defectList.elementAt(i);
					sql= "update DP_milestone set actual=? where dptaskid=? and milestone_id = ?";
					stm= conn.prepareStatement(sql);
					stm.setDouble(1,dPTaskInfo.actualValue);
					stm.setDouble(2,dPTaskInfo.dptaskID);
					stm.setDouble(3,dPTaskInfo.milestoneID);
					stm.executeUpdate();
					stm.close();
				}
			}catch(Exception ex)
			{
		
			}
			finally
			{
				ServerHelper.closeConnection(conn,stm,rs);
			}
		}
	
}