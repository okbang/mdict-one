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

import com.fms1.web.*;
import com.fms1.tools.*;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.*;

/**
 * General management of norms
 *
 * @author manu
 * @date Mar 13, 2003
 */

final public class Norms {
	private static final int[] testCaseProduct = {
		WorkProductInfo.SYSTEM_TEST_CASE,
		WorkProductInfo.UNIT_TEST_CASE,
		WorkProductInfo.INTEGRATION_TEST_CASE
	};
    private static final int[] summaryMetrics =
        {
            MetricDescInfo.ACCEPTANCE_RATE,
            MetricDescInfo.REQUIREMENT_COMPLETENESS,
            MetricDescInfo.REQUIREMENT_STABILITY,
            MetricDescInfo.TIMELINESS,
            MetricDescInfo.RESPONSE_TIME,
            MetricDescInfo.DURATION_ACHIEVEMENT,
            MetricDescInfo.PROJECT_SCHEDULE_DEVIATION,
            MetricDescInfo.STAGE_SCHEDULE_DEVIATION,
            MetricDescInfo.DELIVERY_SCHEDULE_DEVIATION,
            MetricDescInfo.EFFORT_EFFICIENCY,
            MetricDescInfo.EFFORT_EFFECTIVENESS,
            MetricDescInfo.PROJECT_MANAGEMENT,
            MetricDescInfo.EFFORT_DEVIATION,
            MetricDescInfo.CORRECTION_COST,
            MetricDescInfo.LEAKAGE,
            MetricDescInfo.DEFECT_RATE,
            MetricDescInfo.DEFECT_RATE_LOC,
            MetricDescInfo.DEFECT_RATE_UNIT_TEST_LOC,
            MetricDescInfo.DEFECT_RATE_INTEGRATION_TEST_LOC,
            MetricDescInfo.DEFECT_RATE_SYSTEM_TEST_LOC,
            MetricDescInfo.REVIEW_EFFICIENCY,
            MetricDescInfo.TEST_EFFICIENCY,
            MetricDescInfo.DEFECT_REMOVAL_EFFICIENCY,
            MetricDescInfo.QUALITY_ACHIEVEMENT,
            MetricDescInfo.CUSTOMER_SATISFACTION,
            MetricDescInfo.PRODUCTIVITY,
            MetricDescInfo.PRODUCTIVITY_LOC,
            MetricDescInfo.PRODUCTIVITY_ACHIEVEMENT_LOC,
            MetricDescInfo.TEST_CASE_DENSITY_LOC,
            MetricDescInfo.UNIT_TEST_CASE_DENSITY_LOC,
            MetricDescInfo.INTEGRATION_TEST_CASE_DENSITY_LOC,
            MetricDescInfo.SYSTEM_TEST_CASE_DENSITY_LOC,
            MetricDescInfo.PRODUCTIVITY_ACHIEVEMENT,
            MetricDescInfo.REVIEW_EFFECTIVENESS,
            MetricDescInfo.TEST_EFFECTIVENESS,
            MetricDescInfo.SIZE_ACHIEVEMENT,
            MetricDescInfo.SIZE_DEVIATION,
            MetricDescInfo.PROCESS_COMPLIANCE,
            MetricDescInfo.CP_TIME,
            MetricDescInfo.QUALITY_COST,
            MetricDescInfo.TRANSLATION_COST };
	private static String summaryMetricsStr; 
    
	public final static NormInfo getNormByProject(
		final long projectid,
		final int metricConstant,
		final java.util.Date date) {
		final NormInfo normInfo = new NormInfo();
		try {
			final ProjectInfo projectInfo = Project.getProjectInfo(projectid);
			java.sql.Date sqlDate = null;
			if (date != null)
				sqlDate = new java.sql.Date(date.getTime());
			else
				sqlDate = new java.sql.Date(0);
			return getNormDetails(metricConstant, projectInfo, sqlDate);
		}
		catch (Exception e) {
			e.printStackTrace();
			return normInfo;
		}
	}
	public final static NormInfo getNorm(final long projectid, final int metricConstant) {
		return getNormByProject(projectid, metricConstant, new java.util.Date(0));
	}
	/**
	 * get all info about Metric excepted actual value
	 */
	final static NormInfo getNormGeneralInfo( final int metricConstant,
		final ProjectInfo projectInfo, final java.sql.Date date) {
        long groupsID = WorkUnit.getGroupsIDbyWorkUnitID(projectInfo.getWorkUnitId());
        final NormInfo normInfo = new NormInfo();        
        try {
            MetricDescInfo metricDescInfo = Metrics.getMetricDesc(metricConstant);
            normInfo.normID = metricDescInfo.metricID;
            normInfo.metricID = metricConstant;
            normInfo.normName = metricDescInfo.metricName;
            normInfo.normUnit = metricDescInfo.unit;
            normInfo.colorType = metricDescInfo.colorType;
            MetricInfo inf = Metrics.getMetric(projectInfo.getProjectId(), metricConstant);
            normInfo.cause =inf.causal;
            normInfo.note=inf.note;
            normInfo.plannedValue=inf.plannedValue;
            
            // Add by HaiMM - Start
			normInfo.usl = inf.usl;
			normInfo.lsl = inf.lsl;
			// Add by HaiMM - End
			
            // Norms
            normInfo.dateDefined = ((date != null) && (date.getTime() != 0)); // with date specified
			NormInfo temp = new NormInfo();
            if(metricConstant != MetricDescInfo.CUSTOMER_SATISFACTION){
            	temp = getNorm(-1, Parameters.FSOFT_WU, normInfo.normID, projectInfo.getLifecycleId(), projectInfo.getStartDate());
            }else{
				temp = getCSSNorm(MetricDescInfo.CUSTOMER_SATISFACTION, projectInfo.getLifecycleId(), groupsID , projectInfo.getStartDate());
            }
            normInfo.average = temp.average;
            normInfo.ucl = temp.ucl;
            normInfo.lcl = temp.lcl;
            normInfo.date = date;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return normInfo;            
        }
	}
	final static NormInfo getNormDetails(final int metricConstant, final ProjectInfo projectInfo) {
		
		return getNormDetails(metricConstant, projectInfo, new java.sql.Date(0));
	}
	public final static NormInfo getNormDetails(
		final int metricConstant,
		final ProjectInfo projectInfo,
		final java.sql.Date date) {
		NormInfo normInfo = getNormGeneralInfo(metricConstant, projectInfo, date);
		//the general info gathering is successfull, more specific info is folowing
		return computeMetric(projectInfo, normInfo);
	}
    public static double computeMetric(ProjectInfo projectInfo,int metricConstant, java.sql.Date date) {
        final NormInfo normInfo = new NormInfo();
        try {
            MetricDescInfo metricDescInfo = Metrics.getMetricDesc(metricConstant);
            normInfo.normID = metricDescInfo.metricID;
            normInfo.metricID = metricConstant;
            normInfo.normName = metricDescInfo.metricName;
            normInfo.normUnit = metricDescInfo.unit;
            normInfo.colorType = metricDescInfo.colorType;
            MetricInfo inf = Metrics.getMetric(projectInfo.getProjectId(), metricConstant);
            normInfo.cause = inf.causal;
            normInfo.note = inf.note;
            normInfo.plannedValue = inf.plannedValue;
            // Norms
            normInfo.dateDefined = ((date != null) && (date.getTime() != 0)); // with date specified
            normInfo.date = date;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return computeMetric(projectInfo, normInfo).actualValue;            
        }
    }
	public static NormInfo computeMetric(ProjectInfo projectInfo, NormInfo normInfo) {
		ResultSet rs = null;
		PreparedStatement prepStmt = null;
		String sql = null;
		String strDate = null;
		Connection conn = null;
		try {
			if (normInfo.dateDefined)
				strDate = "'" + CommonTools.dateFormat(normInfo.date) + "'";

			switch (normInfo.metricID) {
				case MetricDescInfo.ACCEPTANCE_RATE :
					String dateConstaint = (normInfo.dateDefined) ? " AND ACTUAL_RELEASE_DATE <= ?" : "";
					sql =
						" SELECT A,B "
							+ " FROM (SELECT COUNT(*) A FROM MODULE WHERE PROJECT_ID = ? AND IS_DELIVERABLE = 1 and STATUS  = 2 AND ACTUAL_RELEASE_DATE IS NOT NULL "
							+ dateConstaint
							+ ") "
							+ " , (SELECT COUNT(*) B FROM MODULE WHERE PROJECT_ID = ? AND IS_DELIVERABLE = 1 and STATUS != 4 AND ACTUAL_RELEASE_DATE IS NOT NULL "
							+ dateConstaint
							+ ")";
					conn = ServerHelper.instance().getConnection();
					prepStmt = conn.prepareStatement(sql);
					prepStmt.setLong(1, projectInfo.getProjectId());
					if (normInfo.dateDefined) {
						prepStmt.setDate(2, normInfo.date);
						prepStmt.setLong(3, projectInfo.getProjectId());
						prepStmt.setDate(4, normInfo.date);
					}
					else {
						prepStmt.setLong(2, projectInfo.getProjectId());
					}
					rs = prepStmt.executeQuery();
					if (rs.next()) {
						if (rs.getDouble("B") != 0)
							normInfo.actualValue = rs.getDouble("A") * 100.0 / rs.getDouble("B");
					}
					normInfo.plannedValue = normInfo.average;
					break;
                case MetricDescInfo.REQUIREMENT_COMPLETENESS :
                    normInfo.actualValue =
                        Requirement.getActualRCRByDateHistory(
                            projectInfo,
                            Requirement.getRequirementList(
                                projectInfo,
                                normInfo.date),
                            normInfo.date);
                    break;
				case MetricDescInfo.REQUIREMENT_STABILITY :
					RequirementInfo reqHdrInfo2 = null;
					if (normInfo.dateDefined) {
						reqHdrInfo2 =
							 Requirement.getRequirementInfo(
								Requirement.getRequirementList(projectInfo),
								normInfo.date);
					}
					else {
						reqHdrInfo2 =
							Requirement.getRequirementInfo(
								Requirement.getRequirementList(projectInfo));
					}
					if (reqHdrInfo2 != null) {
						normInfo.actualValue = reqHdrInfo2.sumSizeStability;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.TIMELINESS :
					if (normInfo.dateDefined)
						normInfo.actualValue = Schedule.getTimeliness(projectInfo.getProjectId(), normInfo.date);
					else
						normInfo.actualValue = Schedule.getTimeliness(projectInfo.getProjectId());
					break;
				case MetricDescInfo.RESPONSE_TIME :
					sql =
						"SELECT (AVG( RESPONSE_DATE - RECEIVED_DATE ))*24"
							+ " FROM  REQUIREMENTS "
							+ " WHERE PROJECT_ID = ?"
							+ (normInfo.dateDefined ? (" AND RECEIVED_DATE <=" + strDate) : "");
					conn = ServerHelper.instance().getConnection();
					prepStmt = conn.prepareStatement(sql);
					prepStmt.setLong(1, projectInfo.getProjectId());
					rs = prepStmt.executeQuery();
					if (rs.next()) {
						normInfo.actualValue = rs.getDouble(1);
					}
					break;
				case MetricDescInfo.DURATION_ACHIEVEMENT :
				case MetricDescInfo.PROJECT_SCHEDULE_DEVIATION :
                    double actualDuration = Double.NaN;
                    double plannedDuration = Double.NaN;
                    java.util.Date plannedFinishDate;
                    java.util.Date actualFinishDate;
                    java.util.Date today = new java.util.Date();
                    
                    plannedFinishDate = (projectInfo.getPlanFinishDate() != null)
                                        ? projectInfo.getPlanFinishDate()
                                        : projectInfo.getBaseFinishDate();
                    
                    actualFinishDate = (projectInfo.getActualFinishDate() != null) 
                                       ? projectInfo.getActualFinishDate() 
                                       : today;
                    
                    if ((projectInfo.getPlanStartDate() != null) && (plannedFinishDate != null)) {
                        plannedDuration =
                            CommonTools.dateDiff(projectInfo.getPlanStartDate(), plannedFinishDate);
                    }
                    if ((projectInfo.getStartDate() != null) && (actualFinishDate != null)) {
                        actualDuration =
                            CommonTools.dateDiff(projectInfo.getStartDate(), actualFinishDate);
                    }
                    if ((plannedDuration != 0) && (!Double.isNaN(actualDuration))) {
                        if (normInfo.metricID == MetricDescInfo.DURATION_ACHIEVEMENT)
                            normInfo.actualValue = 100d + ((actualDuration - plannedDuration) * 100d / plannedDuration);
                        else //"Project Schedule Deviation"
                            normInfo.actualValue = ((actualDuration - plannedDuration) * 100d / plannedDuration);
                    }
                    
                    /*
                    sql =
						" SELECT (NVL(PLAN_FINISH_DATE,BASE_FINISH_DATE) - PLAN_START_DATE) TARGET_DURATION, "
							+ " (NVL(ACTUAL_FINISH_DATE , SYSDATE) - START_DATE) ACTUAL_DURATION  "
							+ " FROM PROJECT WHERE PROJECT_ID = ?";
					conn = ServerHelper.instance().getConnection();
					prepStmt = conn.prepareStatement(sql);
					prepStmt.setLong(1, projectInfo.getProjectId());
					rs = prepStmt.executeQuery();
					if (rs.next()) {
						final double targetDuration = rs.getDouble("TARGET_DURATION");
						final double actualDuration = rs.getDouble("ACTUAL_DURATION");
						if (targetDuration != 0) {
							if (normInfo.metricID == MetricDescInfo.DURATION_ACHIEVEMENT)
								normInfo.actualValue = 100d + ((actualDuration - targetDuration) * 100d / targetDuration);
							else //"Project Schedule Deviation"
								normInfo.actualValue = ((actualDuration - targetDuration) * 100d / targetDuration);
						}
					}
                    */
                    
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.STAGE_SCHEDULE_DEVIATION :
					StageInfo stageInfo;
					final Vector stageList = Schedule.getStageList(projectInfo.getProjectId());
					double sumDeviation = 0;
					int count = 0;
					for (int i = 0; i < stageList.size(); i++) {
						stageInfo = (StageInfo) stageList.elementAt(i);
						if (ConvertString.isNumber(stageInfo.deviation)) {
							sumDeviation += Double.parseDouble(stageInfo.deviation);
							count++;
						}
					}
					if (count != 0)
						normInfo.actualValue = sumDeviation / count;
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.DELIVERY_SCHEDULE_DEVIATION :
					final Vector test_review_Vt;
					if (normInfo.dateDefined) {
						test_review_Vt = WorkProduct.getModuleListSchedule(projectInfo.getProjectId(), WorkProduct.ORDER_BY_NAME, normInfo.date);
					}
					else {
						test_review_Vt = WorkProduct.getModuleListSchedule(projectInfo.getProjectId(), WorkProduct.ORDER_BY_PRELEASE);
					}
					float sum = 0;
					int count2 = 0;
					for (int i = 0; i < test_review_Vt.size(); i++) {
						final ModuleInfo info = (ModuleInfo) test_review_Vt.get(i);
						if (!Double.isNaN(info.deviation) && info.isDel) {
							sum += (float)info.deviation;
							count2++;
						}
					}
					if (count2 != 0) {
						normInfo.actualValue = sum / count2;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.EFFORT_EFFICIENCY :
                    // Get actual calendar effort by date defined (usually used
                    // by Product Index pages) or by default date (determined
                    // automatically by Assignments.getActualCalendarEffort())
                    /*
                    double calEffort = Assignments.getActualCalendarEffort(
                            projectInfo,
                            normInfo.dateDefined ? normInfo.date : null);
                    if (calEffort > 0) {
                        // Get billable effort up to normInfo.date or today
                        double ratio = CommonTools.getMilestoneRatio(
                            projectInfo.getLatestStartDate(),
                            projectInfo.getLatestFinishDate(),
                            (normInfo.dateDefined) ?
                                normInfo.date : new java.util.Date());
                        normInfo.actualValue =
                            projectInfo.getLatestBillableEffort() *
                            ratio * 100.0 / calEffort;
                    }*/
                    
                    // Changed in 28-Feb-07:
                    // Effort efficiency for on-going project=Last committed
                    //   billable(Actual if any)/Last committed Calendar
                    // Effort efficiency for closed project=
                    //   Actual billable/Actual Calendar
                    normInfo.actualValue = Effort.getEffortEfficiency(projectInfo);
                    if (projectInfo.getPlannedCalendarEffort() > 0) {
                        normInfo.plannedValue =
                            projectInfo.getPlannedBillableEffort() * 100.0 /
                                projectInfo.getPlannedCalendarEffort();
                    }

                    break;
                case MetricDescInfo.EFFORT_EFFECTIVENESS :
				case MetricDescInfo.EFFORT_DEVIATION :
					EffortInfo effortInfo = Effort.getEffortInfo(projectInfo);
					if (!Double.isNaN(effortInfo.effortDeviation))
						if (normInfo.metricID == MetricDescInfo.EFFORT_EFFECTIVENESS)
							normInfo.actualValue = 100 + effortInfo.effortDeviation;
						else //"Effort deviation"
							normInfo.actualValue = effortInfo.effortDeviation;
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.PROJECT_MANAGEMENT :
				case MetricDescInfo.QUALITY_COST :
					effortInfo = Effort.getEffortInfo(projectInfo, normInfo.date);
					//Planned values :
					double mngEffort = Double.NaN;
					double qltEffort = Double.NaN;
					sql = " SELECT MANAGEMENT_EFFORT, QUALITY_EFFORT FROM PROJECT_PLAN WHERE PROJECT_ID = ?";
					conn = ServerHelper.instance().getConnection();
					prepStmt = conn.prepareStatement(sql);
					prepStmt.setLong(1, projectInfo.getProjectId());
					rs = prepStmt.executeQuery();
					if (rs.next()) {
						mngEffort = rs.getDouble("MANAGEMENT_EFFORT");
						qltEffort = rs.getDouble("QUALITY_EFFORT");
					}
					
					if (normInfo.metricID == MetricDescInfo.PROJECT_MANAGEMENT) {
						normInfo.plannedValue = mngEffort;
						normInfo.actualValue = effortInfo.perManagementEffort;
					}
					else{
						normInfo.plannedValue = qltEffort;
						normInfo.actualValue = effortInfo.perQualityEffort;
					}
					break;
				case MetricDescInfo.CORRECTION_COST :
					final EffortInfo effortInfo2 = Effort.getEffortInfo(projectInfo, normInfo.date);
					normInfo.actualValue = effortInfo2.perCorrectionEffort;
					break;
				case MetricDescInfo.TRANSLATION_COST:
					effortInfo = Effort.getEffortInfo(projectInfo, normInfo.date);
					normInfo.actualValue = effortInfo.perTranslationEffort;
					break;
				case MetricDescInfo.LEAKAGE :
					normInfo.actualValue = Defect.getProjectWeightedLeakage(projectInfo.getProjectId(), normInfo.date, null);
					break;
				case MetricDescInfo.DEFECT_RATE :
					final ProjectSizeInfo sizeInfo; 
					double totalWeightedDefect = 0;
					boolean isSoftPack = false;
					ModuleInfo moduleInfo;
					Vector modules = WorkProduct.getModuleListSchedule( projectInfo.getProjectId(), WorkProduct.ORDER_BY_NAME);
					if (normInfo.dateDefined) {
						//change request from RAU: defect rate is calc with planned size if no soft package released && act size <1 UCP
						for (int i = 0;i < modules.size();i++){
							moduleInfo = (ModuleInfo)modules.elementAt(i);
							if (moduleInfo.wpName != null && "Software package".equals(moduleInfo.wpName.trim()) && moduleInfo.actualReleaseDate != null && moduleInfo.actualReleaseDate.compareTo(normInfo.date)<=0){
								isSoftPack = true;
								break;
							}
						}
						totalWeightedDefect = Defect.getTotalDefects(projectInfo.getProjectId(), normInfo.date).totalWeightedDefect;
						sizeInfo= new ProjectSizeInfo(projectInfo.getProjectId(),normInfo.date);
					}
					else {
						for (int i=0;i<modules.size();i++){
							moduleInfo = (ModuleInfo)modules.elementAt(i);
							if (moduleInfo.wpName !=null && moduleInfo.wpName.trim().equals("Software package") && moduleInfo.actualReleaseDate!=null){
								isSoftPack=true;
								break;
							}
						}
						totalWeightedDefect = Defect.getTotalDefects(projectInfo.getProjectId(), null).totalWeightedDefect;
						sizeInfo= new ProjectSizeInfo(projectInfo.getProjectId());
					}
                    // change request from RAU: defect rate is calc with planned size if no soft package released && act size <1 UCP
					if (isSoftPack || sizeInfo.totalActualSize > 1 ) {
                        normInfo.actualValue = totalWeightedDefect / sizeInfo.totalCreatedSize;
                        //normInfo.actualValue = totalWeightedDefect / sizeInfo.totalActualSize;
					}
					else if ( sizeInfo.totalPlannedSize !=0){
						normInfo.actualValue = totalWeightedDefect / sizeInfo.totalPlannedSize;
					}
					normInfo.plannedValue = normInfo.average;
					break;
                
                // Should calculate totalProductivityLoc & totalQualityLoc
                // before calculate LOC metrics
				case MetricDescInfo.DEFECT_RATE_LOC:
					DefectInfo totalDefects = Defect.getTotalDefects(projectInfo.getProjectId(), null);
					// Get total LOC for Quality of this project
					if (projectInfo.getTotalQualityLoc() != 0){
						normInfo.actualValue = totalDefects.totalWeightedDefect / projectInfo.getTotalQualityLoc()*1000;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.DEFECT_RATE_UNIT_TEST_LOC:
					long[] totalUnitTestDefects = Defect.getTotalDefectsByQCActivity(projectInfo.getProjectId(), (byte)10, null);
					if (projectInfo.getTotalQualityLoc() != 0){
						normInfo.actualValue = totalUnitTestDefects[1] / projectInfo.getTotalQualityLoc()*1000;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.DEFECT_RATE_INTEGRATION_TEST_LOC:
					long[] totalIntegrationTestDefect = Defect.getTotalDefectsByQCActivity(projectInfo.getProjectId(), (byte)11, null);
					if (projectInfo.getTotalQualityLoc() != 0){
						normInfo.actualValue = totalIntegrationTestDefect[1] / projectInfo.getTotalQualityLoc()*1000;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.DEFECT_RATE_SYSTEM_TEST_LOC:
					long[] totalSystemTestDefect = Defect.getTotalDefectsByQCActivity(projectInfo.getProjectId(), (byte)12, null);
					if (projectInfo.getTotalQualityLoc() != 0){
						normInfo.actualValue = totalSystemTestDefect[1] / projectInfo.getTotalQualityLoc()*1000;
					}
					normInfo.plannedValue = normInfo.average;
					break;				
				case MetricDescInfo.REVIEW_EFFICIENCY :
				case MetricDescInfo.TEST_EFFICIENCY :
				case MetricDescInfo.DEFECT_REMOVAL_EFFICIENCY :
					final long ttlWeightedDefect = Defect.getTotalDefects(projectInfo.getProjectId(), null).totalWeightedDefect;
					if (normInfo.metricID == MetricDescInfo.REVIEW_EFFICIENCY)
						normInfo.actualValue =
							(double) Defect.getWeightedDefectByActivityType(
								projectInfo.getProjectId(),
								"Review",
								Defect.NO_LEAKAGE)
								* 100
								/ (double) ttlWeightedDefect;
					else if (normInfo.metricID == MetricDescInfo.TEST_EFFICIENCY)
						normInfo.actualValue =
							(double) Defect.getWeightedDefectByActivityType(
								projectInfo.getProjectId(),
								"Test",
								Defect.NO_LEAKAGE)
								* 100
								/ (double) ttlWeightedDefect;
					else //Defect removal efficiency
						normInfo.actualValue =
							(double) (Defect
								.getWeightedDefectByActivityType(projectInfo.getProjectId(), "Test", Defect.NO_LEAKAGE)
								+ (double) Defect.getWeightedDefectByActivityType(
									projectInfo.getProjectId(),
									"Review",
									Defect.NO_LEAKAGE))
								* 100
								/ (double) ttlWeightedDefect;
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.QUALITY_ACHIEVEMENT :
					MetricInfo metricInf = Metrics.getMetric(projectInfo.getProjectId(), MetricDescInfo.LEAKAGE);
					//if (normInfo.plannedValue != 0)
					if (metricInf.plannedValue > 0 )
						normInfo.actualValue = Defect.getProjectWeightedLeakage(projectInfo.getProjectId(), null) * 100d / metricInf.plannedValue;
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.CUSTOMER_SATISFACTION :
					metricInf = Metrics.getMetric(projectInfo.getProjectId(), normInfo.metricID);
					normInfo.actualValue =metricInf.actualValue;
					normInfo.plannedValue =metricInf.plannedValue;
					break;
				case MetricDescInfo.TEST_CASE_DENSITY_LOC:
					if (projectInfo.getTotalProductivityLoc() != 0){
						normInfo.actualValue = WorkProduct.doSumNumberOfTestCaseProduct(projectInfo.getProjectId(), testCaseProduct)/projectInfo.getTotalProductivityLoc()*1000;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.UNIT_TEST_CASE_DENSITY_LOC:
					final int[] unitTestCase = {WorkProductInfo.UNIT_TEST_CASE};
					if (projectInfo.getTotalProductivityLoc() != 0){
						normInfo.actualValue = WorkProduct.doSumNumberOfTestCaseProduct(projectInfo.getProjectId(), unitTestCase)/projectInfo.getTotalProductivityLoc()*1000;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.INTEGRATION_TEST_CASE_DENSITY_LOC:
					final int[] integrationTestCase = {WorkProductInfo.INTEGRATION_TEST_CASE};
					if (projectInfo.getTotalProductivityLoc() != 0){
						normInfo.actualValue = WorkProduct.doSumNumberOfTestCaseProduct(projectInfo.getProjectId(), integrationTestCase)/projectInfo.getTotalProductivityLoc()*1000;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.SYSTEM_TEST_CASE_DENSITY_LOC:
					final int[] systemTestCase = {WorkProductInfo.SYSTEM_TEST_CASE};
					if (projectInfo.getTotalProductivityLoc() != 0){
						normInfo.actualValue = WorkProduct.doSumNumberOfTestCaseProduct(projectInfo.getProjectId(), systemTestCase)/projectInfo.getTotalProductivityLoc()*1000;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.PRODUCTIVITY :
				case MetricDescInfo.PRODUCTIVITY_ACHIEVEMENT :
					final ProjectSizeInfo sizeInfor = new ProjectSizeInfo(projectInfo.getProjectId());
				    double plannedValue=Double.NaN;
					double replannedValue=Double.NaN;
					double reestimatedSize;
					double reestimatedEffort;
					if (projectInfo.getBaseEffort() != 0) {
						plannedValue = sizeInfor.totalEstimatedSize / (projectInfo.getBaseEffort());
						}
				    if ((!Double.isNaN(sizeInfor.totalReestimatedSize))||(projectInfo.getPlanEffort()!=0)){
                    	reestimatedSize = (Double.isNaN(sizeInfor.totalReestimatedSize) ? sizeInfor.totalEstimatedSize:sizeInfor.totalReestimatedSize);
						reestimatedEffort = (projectInfo.getPlanEffort()==0 ? projectInfo.getBaseEffort():projectInfo.getPlanEffort());
						replannedValue = (double)(reestimatedSize/reestimatedEffort);
					}
					normInfo.plannedValue = (Double.isNaN(replannedValue)?plannedValue:replannedValue);
					double actualEffort = Effort.getActualEffort(projectInfo.getProjectId());
					if (actualEffort != 0) {
						normInfo.actualValue = sizeInfor.totalCreatedSize / actualEffort;
						//normInfo.actualValue = sizeInfor.totalActualSize / actualEffort;
					}
					if (normInfo.metricID == MetricDescInfo.PRODUCTIVITY_ACHIEVEMENT) {
						if (normInfo.plannedValue != 0) {
							normInfo.actualValue = normInfo.actualValue * 100 / normInfo.plannedValue;
							normInfo.plannedValue = normInfo.average;
						}
					}
					break;
                case MetricDescInfo.PRODUCTIVITY_LOC:
                    double projectActualEffrot = Effort.getActualEffort(projectInfo.getProjectId());
                    if (projectActualEffrot > 0){
                        normInfo.actualValue = projectInfo.getTotalProductivityLoc()/projectActualEffrot;
                    }
                    normInfo.plannedValue = normInfo.average;
                    break;
				case MetricDescInfo.PRODUCTIVITY_ACHIEVEMENT_LOC:
					ProductLocInfo productLocPlan = WorkProduct.getTotalLocProductivityQuality(projectInfo.getProjectId(), "PRODUCT_LOC_PLAN");
					ProductLocInfo productLocActual = WorkProduct.getTotalLocProductivityQuality(projectInfo.getProjectId(), "PRODUCT_LOC_ACTUAL");
					if (productLocPlan.getLocProductivity() != 0){
						normInfo.actualValue = productLocActual.getLocProductivity()/productLocPlan.getLocProductivity()*100;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.REVIEW_EFFECTIVENESS :
					// We must exclude effort for non-engineering activities like PM, QC, ...
					final double revEffort =
						Effort.getEffortByTOW(
							projectInfo.getProjectId(),
							EffortInfo.TOW_ENGINEERING_REVIEW,
							(normInfo.dateDefined) ? normInfo.date : null);
					if (revEffort != 0)
						normInfo.actualValue =
							Defect.getWeightedDefectByActivityType(
								projectInfo.getProjectId(),
								"Review",
								normInfo.date,								
								Defect.NO_LEAKAGE)
								/ revEffort;
					double[] plannedReview = Defect.getPlannedDefectsByActivity(projectInfo.getProjectId(), "Review");
						double plannedDefectForReview =
						(Double.isNaN(plannedReview[1])) ? plannedReview[0] : plannedReview[1];
					sql = "SELECT SUM(NVL(RE_PLAN_EFFORT,PLAN_EFFORT))  FROM REVIEW_EFFORT WHERE PROJECT_ID = ? ";
					double plannedReview_Effort = 0;
					conn = ServerHelper.instance().getConnection();
					prepStmt = conn.prepareStatement(sql);
					prepStmt.setLong(1, projectInfo.getProjectId());
					rs = prepStmt.executeQuery();
					if (rs.next()) {
						//final double value1 = Db.getDouble(rs, "PLAN_EFFORT");
						//final double value2 = Db.getDouble(rs, "RE_PLAN_EFFORT");
						//plannedReview_Effort += (Double.isNaN(value2) ? (Double.isNaN(value1) ? 0 : value1) : value2);
						plannedReview_Effort =rs.getDouble(1);
					}
					rs.close();
					if (plannedReview_Effort != 0) {
						normInfo.plannedValue = plannedDefectForReview / plannedReview_Effort;
					}
					break;
				case MetricDescInfo.TEST_EFFECTIVENESS :						
				
					final double testEffort =
						Effort.getActualEffortByTOW(
							projectInfo.getProjectId(),
							TypeOfWork.TEST,
							null,
							(normInfo.dateDefined) ? normInfo.date : null);
					if (testEffort != 0) {
						if (normInfo.dateDefined)
							normInfo.actualValue =
								Defect.getWeightedDefectByActivityType(
									projectInfo.getProjectId(),
									"Test",
									normInfo.date,
									Defect.NO_LEAKAGE)
									/ testEffort;
						else
							normInfo.actualValue =
								Defect.getWeightedDefectByActivityType(
									projectInfo.getProjectId(),
									"Test",
									Defect.NO_LEAKAGE)
									/ testEffort;
					}
                    double[] plannedTest = Defect.getPlannedDefectsByActivity(projectInfo.getProjectId(), "Test");
					double plannedDefectForTest = (Double.isNaN(plannedTest[1])) ? plannedTest[0] : plannedTest[1];
					double plannedTest_Effort = 0;
					if ((projectInfo.getStartDate().after(Parameters.dayOfValidation)) && 
				   		(projectInfo.getApplyPPM() == 1))
				   	{//Apply for new projects													
						sql="SELECT  sum(a.plan_effort) as PLANED_EFFORT, b.actual_finish_date " +
							"FROM plans_process_stage a inner join milestone b on a.milestoneid=b.milestone_id " +
							"WHERE b.project_id=? and a.processid=? " +
							"GROUP BY b.milestone_id,b.actual_finish_date " +
							"ORDER BY b.actual_finish_date DESC";
						
						conn = ServerHelper.instance().getConnection();	
						prepStmt = conn.prepareStatement(sql);
						prepStmt.setLong(1, projectInfo.getProjectId());
						prepStmt.setInt(2, ProcessInfo.TEST);
						rs = prepStmt.executeQuery();
						if (rs.next()) 
							plannedTest_Effort =Db.getDouble(rs, "PLANED_EFFORT");	
				   	}else{
						sql ="SELECT PLAN_EFFORT, RE_PLAN_EFFORT FROM PROCESS_EFFORT WHERE PROJECT_ID = ? AND PROCESS_ID = ?";			
						conn = ServerHelper.instance().getConnection();
						prepStmt = conn.prepareStatement(sql);
						prepStmt.setLong(1, projectInfo.getProjectId());
						prepStmt.setInt(2, ProcessInfo.TEST);
						rs = prepStmt.executeQuery();
						if (rs.next()) {
							final double value1 = Db.getDouble(rs, "PLAN_EFFORT");
							final double value2 = Db.getDouble(rs, "RE_PLAN_EFFORT");
							plannedTest_Effort = (value2 >= 0) ? value2 : value1;					 
						}
					}
					
					if (plannedTest_Effort > 0) {
						normInfo.plannedValue = plannedDefectForTest / plannedTest_Effort;
					}
					break;
				case MetricDescInfo.SIZE_ACHIEVEMENT :
					final ProjectSizeInfo infoSize = new ProjectSizeInfo(projectInfo.getProjectId());
					if (infoSize.totalPlannedSize != 0) {
						normInfo.actualValue = infoSize.totalActualSize * 100 / infoSize.totalPlannedSize;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.SIZE_DEVIATION :
					final ProjectSizeInfo inforSize = new ProjectSizeInfo(projectInfo.getProjectId());
					if (inforSize.totalPlannedSize != 0) {
						normInfo.actualValue =
							(inforSize.totalActualSize - inforSize.totalPlannedSize)
								* 100d
								/ inforSize.totalPlannedSize;
					}
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.PROCESS_COMPLIANCE :
					normInfo.actualValue =
						Ncms.getNCCount(projectInfo.getProjectId(), (normInfo.dateDefined) ? normInfo.date : null);
					break;
				case MetricDescInfo.CUSTOMER_COMPLAINTS :
					normInfo.actualValue =
						Ncms.getCustoComplaintsCount(
							projectInfo.getProjectId(),
							(normInfo.dateDefined) ? normInfo.date : null);
					normInfo.plannedValue = normInfo.average;
					break;
				case MetricDescInfo.OVERDUE_NCsOBs :
					normInfo.actualValue =
						Ncms.getOverdueNCsObsCount(projectInfo.getProjectId(),(normInfo.dateDefined) ? normInfo.date : null);
					normInfo.plannedValue = normInfo.average;
					break;	
				case MetricDescInfo.CP_TIME :
					normInfo.actualValue = Ncms.getCPTime(projectInfo.getProjectId());
					normInfo.plannedValue = normInfo.average;
					break;
			}
			// Deviation from Norm
			if (normInfo.average != 0)
				normInfo.normDeviation = (normInfo.actualValue - normInfo.average) * 100 / normInfo.average;
			// Deviation from Plan
			if (normInfo.plannedValue != 0)
				normInfo.planDeviation = (normInfo.actualValue - normInfo.plannedValue) * 100 / normInfo.plannedValue;
			normInfo.isOk = true;
			return normInfo;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("SQL :" + sql);
			return normInfo;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	public final static Vector getNormList(final long projectID) {
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		String sql = null;
		final Vector normVector = new Vector();
		Connection conn = null;
		try {
			final ProjectInfo projectInfo = Project.getProjectInfo(projectID);
            ProductLocInfo productLoc = WorkProduct.getTotalLocProductivityQuality(projectInfo.getProjectId(),"PRODUCT_LOC_ACTUAL");
            projectInfo.setTotalProductivityLoc(productLoc.getLocProductivity());
            projectInfo.setTotalQualityLoc(productLoc.getLocQuality());
            
			//get the constraint string with metric ids
			if (summaryMetricsStr == null) {
				summaryMetricsStr = ConvertString.arrayToString(summaryMetrics,",");
			}
			//causal analysis
			sql =
				"SELECT MD.ID,MD.METRIC_NAME , MD.METRIC_ID,MD.UNIT,MD.COLOR_TYPE,M.PLANNED_VALUE,M.CAUSAL,M.NOTE, M.USL, M.LSL" // Modify by HaiMM - add USL, LSL
					+ " FROM METRIC_DES MD, METRICS M"
					+ " WHERE M.METRIC_INDEX(+)= MD.ID"
					+ " AND MD.ID IN ("
					+ summaryMetricsStr
					+ ")"
					+ " AND M.PROJECT_CODE (+) = ?"
					+ " ORDER BY MD.METRIC_ID ASC, M.CODE DESC";
			//attention should only take latests metrics
			/*****/
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setString(1, projectInfo.getProjectCode());
			rs = prepStmt.executeQuery();
			String previousMetricID = null;
			while (rs.next()) {
				NormInfo normInfo = new NormInfo();
				normInfo.normID = rs.getString("METRIC_ID").trim();
				//redundant rows
				if (normInfo.normID.equals(previousMetricID))
					continue;
				normInfo.metricID = rs.getInt("ID");
				normInfo.normName = rs.getString("METRIC_NAME");
				normInfo.normUnit = rs.getString("UNIT");
				normInfo.colorType = rs.getInt("COLOR_TYPE");
				normInfo.cause = rs.getString("CAUSAL");
				if (normInfo.cause == null)
					normInfo.cause = "";
				normInfo.note = rs.getString("NOTE");
				if (normInfo.note == null)
					normInfo.note = "";
					
				normInfo.plannedValue = Db.getDouble(rs, "PLANNED_VALUE");
				
				// Add by HaiMM - Start
				normInfo.usl = Db.getDouble(rs, "USL");
				normInfo.lsl = Db.getDouble(rs, "LSL");
				// Add by HaiMM - End
				
				normVector.add(normInfo);
				previousMetricID = normInfo.normID;
			}
            // Close connection because computeMetric() connect to DB also
			ServerHelper.closeConnection(conn, prepStmt, rs);

			Vector norms = getNormList(projectInfo,summaryMetrics);
			NormInfo normInfo, normInfo2;
			for (int i = 0; i < normVector.size(); i++) {
				normInfo = (NormInfo) normVector.elementAt(i);
				if(normInfo.metricID != MetricDescInfo.CUSTOMER_SATISFACTION){
					normInfo2 = NormInfo.getNormByMetricID(normInfo.metricID,norms);
				}else{
					normInfo2 = Norms.getCSSNorm(MetricDescInfo.CUSTOMER_SATISFACTION, projectInfo.getLifecycleId(), WorkUnit.getGroupsIDbyWorkUnitID(projectInfo.getWorkUnitId()),projectInfo.getStartDate());
				}
				if (normInfo2 != null) {
					normInfo.average = normInfo2.average;
					normInfo.lcl = normInfo2.lcl;
					normInfo.ucl = normInfo2.ucl;
				}
				computeMetric(projectInfo, normInfo);
			}
			return normVector;
		}
		catch (Exception e) {
			e.printStackTrace();
			return normVector;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	public static final NormPlanInfo getNormPlan(long workUnitID, int lifecycleID, java.sql.Date endDate) {
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		Connection conn = null;
		NormPlanInfo normPlanInfo = new NormPlanInfo();
		try {
			normPlanInfo.workUnitID = workUnitID;
			normPlanInfo.lifecycleID = lifecycleID;
			normPlanInfo.endDate = endDate;
			//metric analysis
			sql =
				"SELECT NORMPLAN.NORMPLAN_ID, NORMPLAN.END_DATE, NORMPLAN.LASTUPDATE,VALUE, UCL, METRIC_ID, LCL "
					+ " FROM NORMPLAN, NORMS"
					+ " WHERE WORKUNIT =? AND LIFECYCLE_ID = ? AND END_DATE=?"
					+ " AND NORMS.NORMPLAN_ID=NORMPLAN.NORMPLAN_ID";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, workUnitID);
			prepStmt.setInt(2, lifecycleID);
			prepStmt.setDate(3, endDate);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				if (normPlanInfo.normPlanID == 0)
					normPlanInfo.normPlanID = rs.getLong("NORMPLAN_ID");
				NormPlanInfo.Row row = normPlanInfo.new Row();
				row.LCL = Db.getDouble(rs, "LCL");
				row.UCL = Db.getDouble(rs, "UCL");
				row.norm = Db.getDouble(rs, "VALUE");
				row.metricID= rs.getInt("METRIC_ID");
				row.strMetricID = Metrics.getMetricID(row.metricID);
				normPlanInfo.rows.add(row);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return normPlanInfo;
		}
	}
	/**
	 * Parameters :parent and grand parent workunit
	 * Only returns LCL, UCL,norm value and date of the planning 
	 * 
	 * */
	public static final NormInfo getNorm(long parentWU, long grandParentWU, String metricID, int lifecycleID, java.sql.Date date) {
		return getNorm(parentWU, grandParentWU, MetricDescInfo.getMetricType(metricID.trim()), lifecycleID, date);
	}
	public static final NormInfo getCSSNorm(
			int metricConstant,
			int lifecycleID,
			long groupID,
			java.sql.Date date) {
			PreparedStatement prepStmt = null;
			String sql = null;
			ResultSet rs = null;
			Connection conn = null;
			NormInfo normInfo = new NormInfo();
			int lifecycleID_new = MetricDescInfo.isProcessNorm(metricConstant) ? 0 : lifecycleID;
			try {
				if ((date == null) || (date.getTime() == 0))
					date = new java.sql.Date(new java.util.Date().getTime());
				
				sql =
					"SELECT AVERRATE, UCL, LCL,END_DATE "
						+ " FROM NORMPLAN, CSS_NORM"
						+ " WHERE GROUP_ID=? AND NORMPLAN.LIFECYCLE_ID = ? "
						+ " AND END_DATE>=?"
						+ " AND CSS_NORM.NORMPLAN_ID = NORMPLAN.NORMPLAN_ID"
						+ " ORDER BY END_DATE ASC";	
				conn = ServerHelper.instance().getConnection();
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setLong(1, groupID);
				prepStmt.setInt(2, lifecycleID_new);
				prepStmt.setDate(3, date);
				rs = prepStmt.executeQuery();
				if (rs.next()) {
					normInfo.lcl = Db.getDouble(rs, "LCL");
					normInfo.ucl = Db.getDouble(rs, "UCL");
					normInfo.average = Db.getDouble(rs, "AVERRATE");
					normInfo.date = rs.getDate("END_DATE");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				ServerHelper.closeConnection(conn, prepStmt, rs);
				return normInfo;
			}
		}
	public static final NormInfo getNorm(
		long parentWU,
		long grandParentWU,
		int metricConstant,
		int lifecycleID,
		java.sql.Date date) {
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		Connection conn = null;
		NormInfo normInfo = new NormInfo();
            
        // For Process Norm metrics example Process Compliance metric,
        // it has value for Development life cycle only => get this metric norm of
        // DEVELOPMENT life cycle (=0) any way
        int lifecycleID_new = MetricDescInfo.isProcessNorm(metricConstant) ? 0 : lifecycleID;
		try {
			if ((date == null) || (date.getTime() == 0))
				date = new java.sql.Date(new java.util.Date().getTime()); // with date specified
				
			sql =
				"SELECT VALUE, UCL, LCL,END_DATE "
					+ " FROM NORMPLAN, NORMS"
					+ " WHERE WORKUNIT in (?,?) AND METRIC_ID=? AND LIFECYCLE_ID = ? "
					+ " AND END_DATE>=?"
					+ " AND NORMS.NORMPLAN_ID=NORMPLAN.NORMPLAN_ID"
					+ " ORDER BY END_DATE ASC";	
			
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setLong(1, parentWU);
			prepStmt.setLong(2, grandParentWU);
			prepStmt.setInt(3, metricConstant);
            prepStmt.setInt(4, lifecycleID_new);
            prepStmt.setDate(5, date);
			rs = prepStmt.executeQuery();
			if (rs.next()) {
				normInfo.lcl = Db.getDouble(rs, "LCL");
				normInfo.ucl = Db.getDouble(rs, "UCL");
				normInfo.average = Db.getDouble(rs, "VALUE");
				normInfo.date = rs.getDate("END_DATE");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return normInfo;
		}
	}
	public static final boolean updateCSSNorm(Vector vectorCSSNorm, long normPlanID) {
		PreparedStatement prepStmt = null;
		PreparedStatement prepStmt1 = null;
		PreparedStatement prepStmt2 = null;
		String sql = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			java.sql.Date now = new java.sql.Date(new java.util.Date().getTime());
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);

			if(normPlanID < 1){
				sql = "SELECT MAX(NORMPLAN_ID) as NORMPLANID FROM NORMPLAN ";
				prepStmt1 = conn.prepareStatement(sql);
				rs = prepStmt1.executeQuery();
				if(rs.next()){
					normPlanID = rs.getLong("NORMPLANID");
				}
				prepStmt1.close();
			} 
			
			sql = "UPDATE css_norm SET AVERRATE=?, UCL=?, LCL=? WHERE GROUP_ID =? AND NORMPLAN_ID=?";
			prepStmt = conn.prepareStatement(sql);
			sql =	"INSERT INTO css_norm (AVERRATE, UCL, LCL, GROUP_ID,NORMPLAN_ID )"
						+ " VALUES(?,?,?,?,?)";
			prepStmt2 = conn.prepareStatement(sql);
			CSSNormInfo cssNormInfo = new CSSNormInfo();
			for(int i=0;i<vectorCSSNorm.size();i++){
				cssNormInfo = (CSSNormInfo)vectorCSSNorm.elementAt(i);
				if(cssNormInfo.normPlanID > 0){
					Db.setDouble(prepStmt,1,cssNormInfo.average);
					Db.setDouble(prepStmt,2,cssNormInfo.ucl);
					Db.setDouble(prepStmt,3,cssNormInfo.lcl);
					prepStmt.setLong(4,cssNormInfo.groupID);
					prepStmt.setLong(5,cssNormInfo.normPlanID);
					prepStmt.executeUpdate();
				}else{
					Db.setDouble(prepStmt2,1,cssNormInfo.average);
					Db.setDouble(prepStmt2,2,cssNormInfo.ucl);
					Db.setDouble(prepStmt2,3,cssNormInfo.lcl);
					prepStmt2.setLong(4,cssNormInfo.groupID);
					prepStmt2.setLong(5,normPlanID);
					prepStmt2.executeUpdate();
				}
			}
			prepStmt2.close();
			conn.commit();
			conn.setAutoCommit(true);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return true;
		}
	}
	
	
	public static final boolean updateNormPlan(NormPlanInfo normPlanInfo) {
		PreparedStatement prepStmt = null;
		PreparedStatement prepStmt2 = null;
		String sql = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			java.sql.Date now = new java.sql.Date(new java.util.Date().getTime());
			NormPlanInfo.Row row;
			conn = ServerHelper.instance().getConnection();
			conn.setAutoCommit(false);
			if (normPlanInfo.normPlanID == 0) {
				sql =
					"INSERT INTO NORMPLAN (NORMPLAN_ID, END_DATE, LASTUPDATE, LIFECYCLE_ID, WORKUNIT)"
						+ " VALUES((SELECT NVL(MAX(NORMPLAN_ID)+1,1) FROM NORMPLAN ),?,?,?,?)";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setDate(1, normPlanInfo.endDate);
				prepStmt.setDate(2, now);
				prepStmt.setInt(3, normPlanInfo.lifecycleID);
				prepStmt.setLong(4, normPlanInfo.workUnitID);
				if (prepStmt.executeUpdate() > 0) {
					prepStmt.close();
					sql =
						"INSERT INTO NORMS (NORMPLAN_ID,VALUE, UCL, LCL, METRIC_ID )"
							+ " VALUES((SELECT MAX(NORMPLAN_ID) FROM NORMPLAN ),?,?,?,?)";
					prepStmt = conn.prepareStatement(sql);
					for (int i = 0; i < normPlanInfo.rows.size(); i++) {
						row = (NormPlanInfo.Row) normPlanInfo.rows.elementAt(i);
						if (!row.isMetricGroup) {
							Db.setDouble(prepStmt, 1, row.norm);
							Db.setDouble(prepStmt, 2, row.UCL);
							Db.setDouble(prepStmt, 3, row.LCL);
							prepStmt.setInt(4, row.metricID);
							prepStmt.executeUpdate();
						}
					}
				}
			}
			else {
				sql = "UPDATE NORMPLAN SET LASTUPDATE =? WHERE NORMPLAN_ID=? ";
				prepStmt = conn.prepareStatement(sql);
				prepStmt.setDate(1, now);
				prepStmt.setLong(2, normPlanInfo.normPlanID);
				prepStmt.executeUpdate();
				prepStmt.close();
				sql = "UPDATE NORMS SET VALUE=?, UCL=?, LCL=? WHERE METRIC_ID =? AND NORMPLAN_ID=?";
				prepStmt = conn.prepareStatement(sql);
				sql =	"INSERT INTO NORMS (VALUE, UCL, LCL, METRIC_ID,NORMPLAN_ID )"
							+ " VALUES(?,?,?,?,?)";
				prepStmt2 = conn.prepareStatement(sql);
				for (int i = 0; i < normPlanInfo.rows.size(); i++) {
					row = (NormPlanInfo.Row) normPlanInfo.rows.elementAt(i);
					if (!row.isMetricGroup) {
						Db.setDouble(prepStmt, 1, row.norm);
						Db.setDouble(prepStmt, 2, row.UCL);
						Db.setDouble(prepStmt, 3, row.LCL);
						prepStmt.setInt(4, row.metricID);
						prepStmt.setLong(5, normPlanInfo.normPlanID);
						if (prepStmt.executeUpdate()<1){
							//if the plan already exists but there are new metrics
							Db.setDouble(prepStmt2, 1, row.norm);
							Db.setDouble(prepStmt2, 2, row.UCL);
							Db.setDouble(prepStmt2, 3, row.LCL);
							prepStmt2.setInt(4, row.metricID);
							prepStmt2.setLong(5, normPlanInfo.normPlanID);
							prepStmt2.executeUpdate();
						}
					}
				}
				prepStmt2.close();
			}
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Save Norms SQL=" + sql);
			return false;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
		finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
		}
	}
	/** get list of effort distribution by stage
	 * @return Vector of NormRefInfo
	 */
	public static final Vector getEffortDstrByStg(ProjectInfo projectInfo) {
		final Vector list = new Vector();
		try {
				Vector normsProcessStage=Norms.getNormList(projectInfo,RequirementInfo.getRCRMetricConstants());
				Vector normsEffortProcess=Norms.getNormList(projectInfo,MetricDescInfo.trackedEffortProcessId);
			    NormRefInfo nRefInfo;
				double sumProSt=0;
				NormInfo nrmEffPro;
				NormInfo nrmProSt;
				int metricID;
				for (int i = 0; i < StageInfo.stageList.length; i++) {
					nRefInfo = new NormRefInfo();
					nRefInfo.prcID = i + 1;
					nRefInfo.lifecycleID = projectInfo.getLifecycleId();
					nRefInfo.stage = StageInfo.stageNames[i];
					nRefInfo.lifecycle = projectInfo.getLifecycle();
					sumProSt=0;
					for (int k=0;k<normsEffortProcess.size();k++){
						nrmEffPro=(NormInfo)normsEffortProcess.elementAt(k);
						metricID=MetricDescInfo.getRCRMetricConstant(ProcessInfo.getMetricMapping(nrmEffPro.metricID),StageInfo.stageList[i]);
						nrmProSt=NormInfo.getNormByMetricID(metricID,normsProcessStage);
						if (nrmProSt!=null && !Double.isNaN(nrmProSt.average))
							sumProSt+=nrmProSt.average*nrmEffPro.average;
					}
					nRefInfo.value = sumProSt/100d;
			
					list.addElement(nRefInfo);
				}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return list;
		}
	}
	public static final Vector getEffortDstrByStg(long wu, int lifecycle, java.sql.Date date) {
		final Vector list = new Vector();
		try {
			NormInfo nrmInf;
			for (int i = 0; i < StageInfo.effortDistMetrics.length; i++) {
				nrmInf =
					Norms.getNorm(-1, wu, Metrics.getMetricID(StageInfo.effortDistMetrics[i]), lifecycle, date);
				final NormRefInfo info = new NormRefInfo();
				info.prcID = i + 1;
				info.lifecycleID = lifecycle;
				info.value = nrmInf.average;
				info.stage = StageInfo.stageNames[i];
				info.lifecycle = ProjectInfo.parseLifecycle(lifecycle);
				list.addElement(info);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return list;
		}
	}
	/** get list of weighted defect distribution by process
	 * @return Vector of NormRefInfo
	 */
	public static final Vector getDefectDstrByPrc(long wu, int lifecycle, java.sql.Date date) {
		final Vector list = new Vector();
		try {
			int[] metrics =
				{
					MetricDescInfo.DEFECTS_FROM_REQUIREMENTS,
					MetricDescInfo.DEFECTS_FROM_DESIGN,
					MetricDescInfo.DEFECTS_FROM_CODING,
					MetricDescInfo.DEFECTS_FROM_OTHER };
			String[] strMetrics = { "Requirements", "Design", "Coding", "Other" };
			NormInfo nrmInf;
			for (int i = 0; i < metrics.length; i++) {
				nrmInf = Norms.getNorm(-1, wu, Metrics.getMetricID(metrics[i]), lifecycle, date);
				final NormRefInfo info = new NormRefInfo();
				info.prcID = metrics[i];
				info.lifecycleID = lifecycle;
				info.value = nrmInf.average;
				info.prcName = strMetrics[i];
				info.lifecycle = ProjectInfo.parseLifecycle(lifecycle);
				list.addElement(info);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return list;
		}
	}
	/** get list of effort distribution by process
	 * @return Vector of NormRefInfo
	 */
	public static final Vector getEffortDstrByProc(long wu, int lifecycle, java.sql.Date date) {
		final Vector list = new Vector();
		Vector processes = ProcessInfo.projectProcessList;
		//set headers
		ProcessInfo procInfo;
		NormInfo nrmInf;
		NormRefInfo info;
        
		for (int i = 0; i < processes.size(); i++) {
			procInfo = (ProcessInfo) processes.elementAt(i);
			nrmInf = Norms.getNorm(-1, wu, Metrics.getMetricID(procInfo.metricConstant), lifecycle, date);
			info = new NormRefInfo();
			info.prcID = procInfo.processId;
			info.lifecycleID = lifecycle;
			info.value = nrmInf.average;
			info.prcName =procInfo.name;
			info.lifecycle = ProjectInfo.parseLifecycle(lifecycle);
			list.addElement(info);
		}
		return list;
    }
    /**
     * @return vector of NormInfo
     * The order of returned values must be kept
     */
    public static final Vector getEffortDstrByProcRCR(ProjectInfo pinf) {
        final Vector list = new Vector();
        NormInfo nrmInf;
        double sum = 0;
        Vector normList = Norms.getNormList(pinf,MetricDescInfo.completionMetrics);
        if (normList!=null && normList.size()>0)
        for (int i = 0; i < MetricDescInfo.completionMetrics.length; i++) {
            nrmInf=NormInfo.getNormByMetricID(MetricDescInfo.completionMetrics[i],normList);
            if (nrmInf != null){
                sum += nrmInf.average;
            }
            list.addElement(nrmInf);
        }
        if (sum != 0)
            for (int k = 0; k < list.size(); k++) {
                nrmInf = (NormInfo) list.elementAt(k);
                if (nrmInf != null) {
                    nrmInf.average *= 100d / sum;
                }
            }
        return list;
    }
    public final static Vector getNormList(ProjectInfo projectInfo,int[] metricConstants) {
    	PreparedStatement prepStmt = null;
    	ResultSet rs = null;
    	String sql = null;
    	final Vector normVector = new Vector();
    	Connection conn = null;
    	try {
    		//get the constraint string with metric ids
    		String constraint = ConvertString.arrayToString(metricConstants,",");
    		conn = ServerHelper.instance().getConnection();
    		sql =
    			"SELECT NORMPLAN.NORMPLAN_ID,METRIC_ID,VALUE, UCL, LCL "
    				+ " FROM NORMPLAN, NORMS"
    				+ " WHERE WORKUNIT in (?,?)"
    				+ " AND METRIC_ID  IN ("
    				+ constraint
    				+ ") AND LIFECYCLE_ID = ? "
    				+ " AND END_DATE>=?"
    				+ " AND NORMS.NORMPLAN_ID=NORMPLAN.NORMPLAN_ID"
    				+ " ORDER BY END_DATE ASC";
    		prepStmt = conn.prepareStatement(sql);
    		prepStmt.setLong(1, projectInfo.getParent());
    		prepStmt.setLong(2, projectInfo.getGrandParent());
    		prepStmt.setInt(3, projectInfo.getLifecycleId());
    		//use the start date of project or the date of the day
    		prepStmt.setDate(4,(projectInfo.getStartDate() == null)? new java.sql.Date(new java.util.Date().getTime())
    				: projectInfo.getStartDate());
    		rs = prepStmt.executeQuery();
    		int previousMetricID2=0 ;
    		int currentMetricID =0;
    		long planid = 0;
    		long planidBK = 0;
    		NormInfo normInfo;
    		while (rs.next()) {
    			//we only need the latest norms returned by the query
    			planid = rs.getLong("NORMPLAN_ID");
    			if (planidBK == 0)
    				planidBK = planid;
    				//but not the ones after the project's semestser
    			if (planid != planidBK)
    				break;
    			currentMetricID = rs.getInt("METRIC_ID");
    			if (currentMetricID==previousMetricID2)
    				continue;
    			normInfo = new NormInfo();
    			normInfo.metricID=currentMetricID;
    			normInfo.average = Db.getDouble(rs, "VALUE");
    			normInfo.lcl = Db.getDouble(rs, "LCL");
    			normInfo.ucl = Db.getDouble(rs, "UCL");
    			normVector.add(normInfo);
    			previousMetricID2 = currentMetricID;
    		}
    		return normVector;
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return normVector;
    	}
    	finally {
    		ServerHelper.closeConnection(conn, prepStmt, rs);
    	}
    }
    public static Vector getQcNorms(long projectID) {
		ProjectInfo projectInfo = Project.getProjectInfo(projectID);
        Vector normsQc=getDREByQcMetrics(projectInfo);
        return normsQc;
    }
    public static Vector getQcStageNorms(long projectID) {
		ProjectInfo projectInfo = Project.getProjectInfo(projectID);
        Vector normsQcStage=getDREByQcStageMetrics(projectInfo);
        return normsQcStage;
    }
    public static final Vector getDREByQcMetrics(ProjectInfo projectInfo) {
        Vector result = new Vector();
        result = Norms.getNormFamily(projectInfo, MetricDescInfo.METRIC_DRE_QC);
        return result;
    }
    public static final Vector getDREByQcStageMetrics(ProjectInfo projectInfo) {
        Vector result = new Vector();
        result = Norms.getNormFamily(projectInfo, MetricDescInfo.METRIC_DRE);
        return result;
    }
    // strFamily: use format x.x.x. OR x.x. (refer MetricDescInfo)
    public final static Vector getNormFamily(ProjectInfo projectInfo,String strFamily) {
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        String sql = null;
        final Vector normVector = new Vector();
        Connection conn = null;
        try {
            conn = ServerHelper.instance().getConnection();
            // Order by Norm plan end date, then (importance) by Metric_Des.Metric_ID in metric tree
            sql =
                "SELECT NP.NORMPLAN_ID,N.METRIC_ID,VALUE, UCL, LCL "
                    + " FROM NORMPLAN NP, NORMS N, METRIC_DES MD"
                    + " WHERE NP.WORKUNIT in (?,?)"
                    + " AND MD.METRIC_ID  LIKE '" + strFamily + "%'"
                    + " AND NP.LIFECYCLE_ID = ? "
                    + " AND NP.END_DATE>=?"
                    + " AND N.NORMPLAN_ID=NP.NORMPLAN_ID"
                    + " AND N.METRIC_ID=MD.ID"
                    + " ORDER BY NP.END_DATE ASC,MD.METRIC_ID ASC";
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setLong(1, projectInfo.getParent());
            prepStmt.setLong(2, projectInfo.getGrandParent());
            prepStmt.setInt(3, projectInfo.getLifecycleId());
            //use the start date of project or the date of the day
            prepStmt.setDate(4,(projectInfo.getStartDate() == null)? new java.sql.Date(new java.util.Date().getTime())
                    : projectInfo.getStartDate());
            rs = prepStmt.executeQuery();
            
            boolean isGetFirstPlan = false;
            long planId;
            long oldPlanId = Long.MIN_VALUE;
            NormInfo normInfo;
            while (rs.next()) {
                //we only need the latest norms returned by the query
                planId = rs.getLong("NORMPLAN_ID");
                if (planId != oldPlanId) {
                    if (isGetFirstPlan) {
                        break;
                    }
                    else {
                        oldPlanId = planId;
                        isGetFirstPlan = true;
                    }
                }
                normInfo = new NormInfo();
                normInfo.metricID = rs.getInt("METRIC_ID");
                normInfo.average = Db.getDouble(rs, "VALUE");
                normInfo.lcl = Db.getDouble(rs, "LCL");
                normInfo.ucl = Db.getDouble(rs, "UCL");
                normVector.add(normInfo);
            }
            return normVector;
        }
        catch (Exception e) {
            e.printStackTrace();
            return normVector;
        }
        finally {
            ServerHelper.closeConnection(conn, prepStmt, rs);
        }
    }
	public static final Vector getNormOfAllOperationGroup(
		int lifecycleID,
		java.sql.Date dates) {
		PreparedStatement prepStmt = null;
		String sql = null;
		ResultSet rs = null;
		Connection conn = null;
		Vector vectorGroup = new Vector();
		Vector vectorCSSNorm = new Vector();
		try {
			sql =
				"SELECT GROUP_ID, groupname FROM groups WHERE isoperationgroup = 1  order by groupname";
			conn = ServerHelper.instance().getConnection();
			prepStmt = conn.prepareStatement(sql);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				CSSNormInfo cssNormInfo = new CSSNormInfo();
				cssNormInfo.groupID = rs.getLong("GROUP_ID");
				cssNormInfo.groupName = rs.getString("groupname");
				vectorGroup.add(cssNormInfo);
			}
			for (int i = 0; i < vectorGroup.size(); i++) {
				CSSNormInfo cssNormInfo =
					(CSSNormInfo) vectorGroup.elementAt(i);
						sql =
					       " SELECT	c.lcl, c.averrate, c.ucl, c.NORMPLAN_ID, n.lifecycle_id, n.end_date "
						 + "   FROM	GROUPS g, css_norm c, normplan n "
						 + "  WHERE	g.isoperationgroup = 1 "
						 + "    AND	g.GROUP_ID = c.GROUP_ID "
						 + "    AND c.normplan_id = n.normplan_id "
						 + "    AND n.lifecycle_id = ? "
						 + "    AND g.GROUP_ID = ? "
						 + "    AND n.WORKUNIT = 132 "
						 + "    AND n.end_date = ? "
						 + "ORDER BY g.groupname ";

				prepStmt = conn.prepareStatement(sql);
				prepStmt.setInt(1, lifecycleID);
				prepStmt.setLong(2, cssNormInfo.groupID);
				prepStmt.setDate(3, dates);
				rs = prepStmt.executeQuery();
				if (rs.next()) {
					cssNormInfo.lcl = rs.getDouble("lcl");
					cssNormInfo.average = rs.getDouble("averrate");
					cssNormInfo.ucl = rs.getDouble("ucl");
					cssNormInfo.normPlanID = rs.getLong("NORMPLAN_ID");
					cssNormInfo.lifecycle_id = rs.getInt("lifecycle_id");
					cssNormInfo.endDate = rs.getDate("end_date");
				}
				vectorCSSNorm.add(cssNormInfo);
				prepStmt.close();
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ServerHelper.closeConnection(conn, prepStmt, rs);
			return vectorCSSNorm;
		}
	}
}