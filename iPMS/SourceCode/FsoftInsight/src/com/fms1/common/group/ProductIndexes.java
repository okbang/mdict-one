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
 
 /*
 * @(#) ProductIndexes.java 6-Oct-03
 */
package com.fms1.common.group;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.*;
import com.fms1.common.*;
import com.fms1.tools.*;

/**
 * Calculate Product indexes for work unit
 * @author Phuong
 */
public class ProductIndexes {
	//To be replaced by color type
    // affected to IndexDrillInfo.getAchievementValue() & getImprovementValue()
	public final static boolean HIGH_GOOD = true;
	public final static boolean HIGH_BAD = false;
	
	/**
	 * Returns planned value for metric from FSOFT plan
	 */
	public static double getPlannedValueFromPlan(long nWorkUnitID, ReportMonth rm, 
			int metricConstant) {
		return Plans.getPlannedVal(nWorkUnitID, metricConstant, rm.getYear(), rm.getMonth());
	}

	/**
	 * Returns planned value for metric from FSOFT norm
	 */
	public static double getPlannedValueFromNorm(WorkUnitInfo workUnitInfo, ReportMonth rm, 
			int metricConstant) {

		double dRetVal = Double.NaN;
		long nParentID, nGrandParentID;
		//if the value does not exist in  the project plans, then we get it from the norms
		if (workUnitInfo.type == WorkUnitInfo.TYPE_PROJECT) {
			MetricInfo minfo=Metrics.getMetric(workUnitInfo.tableID,metricConstant);
			dRetVal = minfo.plannedValue;
		}
		if (!Double.isNaN(dRetVal)) {
			return dRetVal;
		}
		else {
			int lifecycleID;
			if (workUnitInfo.type == WorkUnitInfo.TYPE_PROJECT) {
				ProjectInfo projectInfo = Project.getProjectInfo(workUnitInfo.tableID);
				nParentID = projectInfo.getParent();
				nGrandParentID = projectInfo.getGrandParent();
				lifecycleID = projectInfo.getLifecycleId();
			}
			else {
				nParentID = workUnitInfo.workUnitID;
				nGrandParentID = workUnitInfo.parentWorkUnitID;
				lifecycleID = ProjectInfo.LIFECYCLE_DEVELOPMENT;
			}
			NormInfo nrmInfo = Norms.getNorm(nParentID, nGrandParentID, metricConstant, lifecycleID,
                    getReportDayOfMonth(rm));
			dRetVal = nrmInfo.average;
		}
		return dRetVal;
	}
	
	/**
	 * Returns planned production size for set of projects
	 */
	public static double getPlannedProductionSize(Vector vtInProgressProjects) {
		double dRetVal = 0;
		int nNumInProgressProjects = vtInProgressProjects.size();
		for (int i = 0; i < nNumInProgressProjects; i++) {
			ProjectSizeInfo prSizeInf = new ProjectSizeInfo(
					((ProjectInfo)vtInProgressProjects.elementAt(i)).getProjectId());
			double dTmp = (prSizeInf != null 
					? (Double.isNaN(prSizeInf.totalEstimatedSize) ? 0 : prSizeInf.totalEstimatedSize) 
					: 0);
			dRetVal += dTmp;
		}
		return dRetVal;			
	}

	/**
	 * Return picked day for monthly report in format d-M-YYYY
	 */
	private static java.sql.Date getReportDayOfMonth(ReportMonth rm) {
		StringBuffer sbTemp = new StringBuffer();
		Calendar today = Calendar.getInstance();
		if ((today.get(Calendar.MONTH) + 1 == rm.getMonth()) && (today.get(Calendar.YEAR) == rm.getYear())) {
			sbTemp.append(today.get(Calendar.DAY_OF_MONTH)).append("-").append(rm.getMonth());
			sbTemp.append("-").append(rm.getYear());
			SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
			java.util.Date date = new java.util.Date();
			try {
				date = sdf.parse(sbTemp.toString());
			}
			catch (ParseException pe) {
			}	
			return new java.sql.Date(date.getTime());
		}
		else {
			return rm.getLastDayOfMonth();
		}
	}

	/**
	 * Return list of inprogress projects belong to current work unit or return
     * a list of one project if entry workunit is a project
	 */
	public static Vector getInProgressProjects(long nWorkUnitID, ReportMonth rm) {
        java.sql.Date reportDate = getReportDayOfMonth(rm);
        java.sql.Date firstDayOfMonth = rm.getFirstDayOfMonth();
		WorkUnitInfo wuTemp = WorkUnit.getWorkUnitInfo(nWorkUnitID);
		if (wuTemp.type == WorkUnitInfo.TYPE_PROJECT) {
			ProjectInfo prInf = Project.getProjectInfo(wuTemp.tableID);
			Vector vtProjectInfoList = new Vector();
            // If project is running through the report month
			if (prInf.getStartDate().compareTo(reportDate) <= 0 
					&& (prInf.getActualFinishDate() == null 
						|| prInf.getActualFinishDate().compareTo(
                            firstDayOfMonth) >= 0)) {
				vtProjectInfoList.add(prInf);
			}
			return vtProjectInfoList;
		}
		else {
			return Project.getChildProjectsByWU(nWorkUnitID, firstDayOfMonth, 
                    reportDate,Project.INPROGRESS_PROJECTS);
		}
	}

	/**
	 * Returns number of inprogress projects belong to current work unit
	 */
	public static int getActualNumOfInProgressProjects(long nWorkUnitID, ReportMonth rm) {
		return getInProgressProjects(nWorkUnitID, rm).size();
	}

	/**
	 * Returns actual production size
	 */
	public static double getActualProductionSize(Vector vtInProgressProjects, ReportMonth rm) {
		double dRetVal = 0;
		
		int nNumInProgressProjects = vtInProgressProjects.size();
		for (int i = 0; i < nNumInProgressProjects; i++) {
			ProjectSizeInfo prSizeInf = new ProjectSizeInfo(
					((ProjectInfo)vtInProgressProjects.elementAt(i)).getProjectId(), 
					getReportDayOfMonth(rm));
			double dTmp = (prSizeInf != null 
					? (Double.isNaN(prSizeInf.totalActualSize) ? 0 : prSizeInf.totalActualSize) 
					: 0);
			dRetVal += dTmp;
		}
		return dRetVal;
	}
	
	/**
	 * Returns actual metric value
	 */
	public static double getActualMetricValue(Vector vtInProgressProjects,
            ReportMonth rm, int nMetricID) {
		double dRetVal = 0;
		int nNumInProgressProjects = vtInProgressProjects.size();
		int nProjects = nNumInProgressProjects;
		java.sql.Date date = getReportDayOfMonth(rm);
        ProductLocInfo productLoc;
		for (int i = 0; i < nNumInProgressProjects; i++) {
			ProjectInfo projectInfo =
                (ProjectInfo)vtInProgressProjects.elementAt(i);
            // Because for LOC indexes projectInfo should has totalProductivityLoc
            // & totalQualityLoc so should calculate them before calculate the metric
            if ((nMetricID == MetricDescInfo.DEFECT_RATE_LOC ||
                nMetricID == MetricDescInfo.PRODUCTIVITY_LOC) &&
                projectInfo.getTotalProductivityLoc() == 0 &&
                projectInfo.getTotalQualityLoc() == 0)
            {
                productLoc = WorkProduct.getTotalLocProductivityQuality(
                        projectInfo.getProjectId(), "PRODUCT_LOC_ACTUAL");
                projectInfo.setTotalProductivityLoc(
                    productLoc.getLocProductivity());
                projectInfo.setTotalQualityLoc(productLoc.getLocQuality());
            }
			NormInfo normInfo = new NormInfo();
			normInfo.metricID = nMetricID;
			normInfo.date = date;
			normInfo.dateDefined = true;
			normInfo.normID = Metrics.getMetricID(nMetricID);
			Norms.computeMetric(projectInfo, normInfo);
			if (!Double.isNaN(normInfo.actualValue)) {
				dRetVal += normInfo.actualValue;
			}
			else {
				nProjects--;
			}
		}
		return (nProjects == 0 ? Double.NaN : dRetVal / nProjects);
	}
	
	/**
	 * Returns nonconforming product rate
	 */
	public static double getActualNonconformingProductRate(Vector vtInProgressProjects, 
			ReportMonth rm) {
		int nNonconformingProjects = 0;
		int nNumInProgressProjects = vtInProgressProjects.size();
		java.sql.Date date = getReportDayOfMonth(rm);
		for (int i = 0; i < nNumInProgressProjects; i++) {
			ProjectInfo projectInfo = (ProjectInfo)vtInProgressProjects.elementAt(i);
			boolean isNonconforming = false;
			NormInfo normInfo = new NormInfo();
			normInfo.date = date;
			normInfo.dateDefined = true;
			normInfo = Norms.getNormDetails(MetricDescInfo.TIMELINESS, projectInfo, date);
			int color = Color.getColor(normInfo.actualValue, normInfo.lcl, normInfo.ucl, 
					normInfo.colorType);
			if (color == Color.BADMETRIC) {
				isNonconforming = true;
			}
			else {	
				/* Timeliness is good, check requirement completeness */
				normInfo = Norms.getNormDetails(MetricDescInfo.REQUIREMENT_COMPLETENESS, 
						projectInfo, date);
				color = Color.getColor(normInfo.actualValue, normInfo.lcl, normInfo.ucl, 
						normInfo.colorType);
				if (color == Color.BADMETRIC) {
					isNonconforming = true;
				}
				else {
					/* Requirement completeness is good, check leakage */
					normInfo = Norms.getNormDetails(MetricDescInfo.LEAKAGE, projectInfo, date);
					color = Color.getColor(normInfo.actualValue, normInfo.lcl, normInfo.ucl, 
							normInfo.colorType);
					if (color == Color.BADMETRIC) {
						isNonconforming = true;
					}
					else {
						/* Leakage is good, check acceptance rate */
						normInfo = Norms.getNormDetails(MetricDescInfo.ACCEPTANCE_RATE, projectInfo, 
								date);
						color = Color.getColor(normInfo.actualValue, normInfo.lcl, normInfo.ucl, 
								normInfo.colorType);
						if (color == Color.BADMETRIC) {
							isNonconforming = true;
						}
					}
				}
			}
			if (isNonconforming) {
				nNonconformingProjects++;
			}
		}
		return (nNumInProgressProjects == 0 ? Double.NaN 
				: (double)nNonconformingProjects / nNumInProgressProjects * 100);
	}
	
    /*
     * Get producttivity LOC value of organization, group or project
     * @param wu
     * @param reportMonth
     * @param tableName
     * @return
    private static ProductLocInfo getLocProductivityQuality(
        WorkUnitInfo wu, ReportMonth reportMonth, String tableName)
    {
        // Get data until end date so the beginDate is not specified
        java.sql.Date beginDate = reportMonth.getFirstDayOfMonth();
        java.sql.Date endDate = getReportDayOfMonth(reportMonth);
        ProductLocInfo locSum =
            WorkProduct.getTotalLocProductivityQuality(
                    wu, beginDate, endDate,
                    Project.INPROGRESS_PROJECTS, true, tableName);
        return locSum;
    }
     */
    /*
     * Get product index for Productivity LOC
     * @param wu
     * @param reportMonth
     * @param locProQua
     * @return
    public static ProductIndexInfo getProductivityLocIndexInfo(
        WorkUnitInfo wu, ReportMonth reportMonth)
    {
        ReportMonth lastMonth = new ReportMonth(reportMonth);
        lastMonth.moveToPreviousMonth();
        ReportMonth nextMonth = new ReportMonth(reportMonth);
        nextMonth.moveToNextMonth();
        ReportMonth next2Month = new ReportMonth(nextMonth);
        next2Month.moveToNextMonth();
        double effortLast =
            Effort.getActualEffortPeriod(wu, lastMonth.getFirstDayOfMonth(),
                    getReportDayOfMonth(lastMonth), true);
        double effortReportMonth = 
            Effort.getActualEffortPeriod(wu, reportMonth.getFirstDayOfMonth(),
                    getReportDayOfMonth(reportMonth), true);
        
//        double effortNextMonth =
//            Effort.getActualEffortPeriod(wu, nextMonth.getFirstDayOfMonth(),
//                    getReportDayOfMonth(nextMonth), true);
//        double effortNext2Month =
//            Effort.getActualEffortPeriod(wu, next2Month.getFirstDayOfMonth(),
//                    getReportDayOfMonth(next2Month), true);
        if (effortLast == 0) {
            effortLast = Double.NaN;
        }
        if (effortReportMonth == 0) {
            effortReportMonth = Double.NaN;
        }
//        if (effortNextMonth == 0) {
//            effortNextMonth = Double.NaN;
//        }
//        if (effortNext2Month == 0) {
//            effortNext2Month = Double.NaN;
//        }
        ProductLocInfo locPQLastActual =
            getLocProductivityQuality(wu, lastMonth, "PRODUCT_LOC_ACTUAL");
        ProductLocInfo locPQReportMonthActual =
            getLocProductivityQuality(wu, reportMonth, "PRODUCT_LOC_ACTUAL");
//        ProductLocInfo locPQReportMonthPlan =
//            getLocProductivityQuality(wu, reportMonth, "PRODUCT_LOC_PLAN");
//        ProductLocInfo locPQNextMonthPlan =
//            getLocProductivityQuality(wu, nextMonth, "PRODUCT_LOC_PLAN");
//        ProductLocInfo locPQNext2MonthPlan =
//            getLocProductivityQuality(wu, next2Month, "PRODUCT_LOC_PLAN");
        
        MetricDescInfo metricDescInfo = Metrics.getMetricDesc(
                MetricDescInfo.PRODUCTIVITY_LOC);
        ProductIndexInfo indexInfo = new ProductIndexInfo(
            metricDescInfo.metricConstant,
            metricDescInfo.metricID,
            metricDescInfo.metricName,
            metricDescInfo.unit,
            locPQLastActual.getLocProductivity() / effortLast,
            locPQReportMonthActual.getLocProductivity() / effortReportMonth,
            getPlannedValueFromNorm(wu, reportMonth, MetricDescInfo.PRODUCTIVITY_LOC),
            getPlannedValueFromNorm(wu, nextMonth, MetricDescInfo.PRODUCTIVITY_LOC),
            getPlannedValueFromNorm(wu, next2Month, MetricDescInfo.PRODUCTIVITY_LOC),
            metricDescInfo.colorType);
        return indexInfo;
    }
     */
    /*
     * Get product index for Defect Rate LOC
     * @param wu
     * @param reportMonth
     * @param locProQua
     * @return
    public static ProductIndexInfo getDefectRateLocIndexInfo(
        WorkUnitInfo wu, ReportMonth reportMonth)
    {
        ReportMonth lastMonth = new ReportMonth(reportMonth);
        lastMonth.moveToPreviousMonth();
        ReportMonth nextMonth = new ReportMonth(reportMonth);
        nextMonth.moveToNextMonth();
        ReportMonth next2Month = new ReportMonth(nextMonth);
        next2Month.moveToNextMonth();
        DefectInfo defectLast =
            Defect.getTotalDefects(wu, lastMonth.getFirstDayOfMonth(),
                getReportDayOfMonth(lastMonth), true);
        DefectInfo defectReportMonth =
            Defect.getTotalDefects(wu, reportMonth.getFirstDayOfMonth(),
                getReportDayOfMonth(reportMonth), true);
//        DefectInfo defectNextMonth =
//            Defect.getTotalDefects(wu, nextMonth.getFirstDayOfMonth(),
//                getReportDayOfMonth(nextMonth), true);
//        DefectInfo defectNext2Month =
//            Defect.getTotalDefects(wu, next2Month.getFirstDayOfMonth(),
//                getReportDayOfMonth(next2Month), true);

        ProductLocInfo locPQLastActual =
            getLocProductivityQuality(wu, lastMonth, "PRODUCT_LOC_ACTUAL");
        ProductLocInfo locPQReportMonthActual =
            getLocProductivityQuality(wu, reportMonth, "PRODUCT_LOC_ACTUAL");
//        ProductLocInfo locPQReportMonthPlan =
//            getLocProductivityQuality(wu, reportMonth, "PRODUCT_LOC_PLAN");
//        ProductLocInfo locPQNextMonthPlan =
//            getLocProductivityQuality(wu, nextMonth, "PRODUCT_LOC_PLAN");
//        ProductLocInfo locPQNext2MonthPlan =
//            getLocProductivityQuality(wu, next2Month, "PRODUCT_LOC_PLAN");
        
        MetricDescInfo metricDescInfo = Metrics.getMetricDesc(
                MetricDescInfo.DEFECT_RATE_LOC);
        ProductIndexInfo indexInfo = new ProductIndexInfo(
            metricDescInfo.metricConstant,
            metricDescInfo.metricID,
            metricDescInfo.metricName,
            metricDescInfo.unit,
            locPQLastActual.getLocQuality() == 0 ? Double.NaN :
                defectLast.totalWeightedDefect /
                    locPQLastActual.getLocQuality() * 1000,
            locPQReportMonthActual.getLocQuality() == 0 ? Double.NaN :
                defectReportMonth.totalWeightedDefect /
                    locPQReportMonthActual.getLocQuality() * 1000,
            getPlannedValueFromNorm(wu, reportMonth, MetricDescInfo.DEFECT_RATE_LOC),
            getPlannedValueFromNorm(wu, nextMonth, MetricDescInfo.DEFECT_RATE_LOC),
            getPlannedValueFromNorm(wu, next2Month, MetricDescInfo.DEFECT_RATE_LOC),
//            locPQReportMonthPlan.getLocQuality() == 0 ? Double.NaN :
//                defectReportMonth.totalWeightedDefect /
//                    locPQReportMonthPlan.getLocQuality() * 1000,
//            locPQNextMonthPlan.getLocQuality() == 0 ? Double.NaN :
//                defectNextMonth.totalWeightedDefect /
//                    locPQNextMonthPlan.getLocQuality() * 1000,
//            locPQNext2MonthPlan.getLocQuality() == 0 ? Double.NaN :
//                defectNext2Month.totalWeightedDefect /
//                    locPQNext2MonthPlan.getLocQuality() * 1000,
            metricDescInfo.colorType);
        return indexInfo;
    }
     */
	/**
	 * Returns product indexes for work unit
	 */
	public static ArrayList getProductIndexes(long nWorkUnitID, ReportMonth actualMonth) {
        WorkUnitInfo wu = WorkUnit.getWorkUnitInfo(nWorkUnitID);
        ArrayList alProductIndexes = new ArrayList();
		ReportMonth lastMonth = new ReportMonth(actualMonth);
		lastMonth.moveToPreviousMonth();
		ReportMonth nextMonth = new ReportMonth(actualMonth);
		nextMonth.moveToNextMonth();
		ReportMonth next2Month = new ReportMonth(nextMonth);
		next2Month.moveToNextMonth();

		MetricDescInfo metricDescInfo;
		//String strMetricID;

		Vector vtInProgressProjects = getInProgressProjects(nWorkUnitID, actualMonth);
		Vector vtLastInProgressProjects = getInProgressProjects(nWorkUnitID, lastMonth);

		/* In Progress Projects */
		metricDescInfo = Metrics.getMetricDesc(MetricDescInfo.IN_PROGRESS_PROJECTS);
		ProductIndexInfo prodIndInf = new ProductIndexInfo(
                metricDescInfo.metricConstant,
				metricDescInfo.metricID, 
				metricDescInfo.metricName, 
				metricDescInfo.unit,
				getActualNumOfInProgressProjects(nWorkUnitID, lastMonth),
				getActualNumOfInProgressProjects(nWorkUnitID, actualMonth),
				getPlannedValueFromPlan(nWorkUnitID, actualMonth, metricDescInfo.metricConstant),
				getPlannedValueFromPlan(nWorkUnitID, nextMonth, metricDescInfo.metricConstant),
				getPlannedValueFromPlan(nWorkUnitID, next2Month, metricDescInfo.metricConstant),
				metricDescInfo.colorType);
		alProductIndexes.add(prodIndInf);
		
		/* Production Size */
		metricDescInfo = Metrics.getMetricDesc(MetricDescInfo.PRODUCTION_SIZE);
		prodIndInf = new ProductIndexInfo(
                metricDescInfo.metricConstant,
                metricDescInfo.metricID,
				metricDescInfo.metricName,
				metricDescInfo.unit,
				getActualProductionSize(vtLastInProgressProjects, lastMonth),
				getActualProductionSize(vtInProgressProjects, actualMonth),
				getPlannedProductionSize(vtInProgressProjects),
				getPlannedProductionSize(getInProgressProjects(nWorkUnitID, nextMonth)),
				getPlannedProductionSize(getInProgressProjects(nWorkUnitID, next2Month)),
				metricDescInfo.colorType);
		alProductIndexes.add(prodIndInf);
				
		MetricDescInfo [] mDescs= new MetricDescInfo[11];
        // Inserted LOC Productivity from 05-Sep-2007
        mDescs[0]=Metrics.getMetricDesc(MetricDescInfo.PRODUCTIVITY_LOC);
        mDescs[1]=Metrics.getMetricDesc(MetricDescInfo.REQUIREMENT_STABILITY);
		mDescs[2]=Metrics.getMetricDesc(MetricDescInfo.ACCEPTANCE_RATE);
		mDescs[3]=Metrics.getMetricDesc(MetricDescInfo.REQUIREMENT_COMPLETENESS);
		mDescs[4]=Metrics.getMetricDesc(MetricDescInfo.TIMELINESS);
		mDescs[5]=Metrics.getMetricDesc(MetricDescInfo.DELIVERY_SCHEDULE_DEVIATION);
		mDescs[6]=Metrics.getMetricDesc(MetricDescInfo.DEFECT_RATE);
		mDescs[7]=Metrics.getMetricDesc(MetricDescInfo.LEAKAGE);
        // Inserted LOC Defect rate from 05-Sep-2007
        mDescs[8]=Metrics.getMetricDesc(MetricDescInfo.DEFECT_RATE_LOC);
        mDescs[9]=Metrics.getMetricDesc(MetricDescInfo.CUSTOMER_SATISFACTION);
        // Inserted Effort Efficiency from 07-Feb-2007
        mDescs[10]=Metrics.getMetricDesc(MetricDescInfo.EFFORT_EFFICIENCY);
		// Note: the following metric's actual values are calculated by average of all projects
        //TODO vtLastInProgressProjects & vtInProgressProjects are different.
        // They make the report incorrectly in Summary at Organization level
        // for some metrics when compared with detail index of those metrics
    	for (int i=0;i<mDescs.length;i++){
    		prodIndInf = new ProductIndexInfo(		
                mDescs[i].metricConstant,
                mDescs[i].metricID,
    			mDescs[i].metricName,
    			mDescs[i].unit,
    			getActualMetricValue(vtLastInProgressProjects, lastMonth, mDescs[i].metricConstant),
    			getActualMetricValue(vtInProgressProjects, actualMonth, mDescs[i].metricConstant),
    			getPlannedValueFromNorm(wu, actualMonth, mDescs[i].metricConstant),
    			getPlannedValueFromNorm(wu, nextMonth, mDescs[i].metricConstant),
    			getPlannedValueFromNorm(wu, next2Month, mDescs[i].metricConstant),
    			mDescs[i].colorType);
		    alProductIndexes.add(prodIndInf);
	    }
		
		/* Nonconforming Product Rate */
		metricDescInfo = Metrics.getMetricDesc(MetricDescInfo.NONCONFORMING_PRODUCT_RATE);
		prodIndInf = new ProductIndexInfo(
                metricDescInfo.metricConstant,
                metricDescInfo.metricID,
				metricDescInfo.metricName,
				metricDescInfo.unit,
				getActualNonconformingProductRate(vtLastInProgressProjects, lastMonth),
				getActualNonconformingProductRate(vtInProgressProjects, actualMonth),
				getPlannedValueFromPlan(nWorkUnitID, actualMonth, metricDescInfo.metricConstant),
				getPlannedValueFromPlan(nWorkUnitID, nextMonth, metricDescInfo.metricConstant),
				getPlannedValueFromPlan(nWorkUnitID, next2Month, metricDescInfo.metricConstant),
				metricDescInfo.colorType);
		alProductIndexes.add(prodIndInf);

        // Productivity LOC (displayed at 3rd position (index=2))
//        ProductIndexInfo pdLocIndex = getProductivityLocIndexInfo(wu, actualMonth);
//        alProductIndexes.add(2, pdLocIndex);
        // Defect Rate LOC (displayed at 11st position (index=10))
//        ProductIndexInfo drLocIndex = getDefectRateLocIndexInfo(wu, actualMonth);
//        alProductIndexes.add(10, drLocIndex);

		return alProductIndexes;		
	}
	
	/**
	 * Returns SCI for set of product indexes
	 */
	public static double getSCI(ArrayList alProductIndexes) {
		/* Updated from 07-Feb-2007 for more clear an easier to update
        
        double dTmp = ((ProductIndexInfo)alProductIndexes.get(0)).getActualValue();
		double dInProgressProjects = Double.isNaN(dTmp) ? 0 : dTmp * 10;

		dTmp = ((ProductIndexInfo)alProductIndexes.get(1)).getActualValue();
		double dProductionSize = Double.isNaN(dTmp) ? 0 : dTmp / 100;

		dTmp = ((ProductIndexInfo)alProductIndexes.get(2)).getActualValue();
		double dRequirementStability = Double.isNaN(dTmp) ? 0 : dTmp / 100 * dProductionSize;

		dTmp = ((ProductIndexInfo)alProductIndexes.get(3)).getActualValue();
		double dAcceptanceRate = Double.isNaN(dTmp) ? 0 : - (1 - dTmp / 100)  * dProductionSize;

		dTmp = ((ProductIndexInfo)alProductIndexes.get(4)).getActualValue();
		double dRequirementCompleteness = Double.isNaN(dTmp) ? 0 : - (1 - dTmp / 100) * dProductionSize;

		dTmp = ((ProductIndexInfo)alProductIndexes.get(5)).getActualValue();
		double dTimeliness = Double.isNaN(dTmp) ? 0 : - (1 - dTmp / 100) * dProductionSize;

		dTmp = ((ProductIndexInfo)alProductIndexes.get(6)).getActualValue();
		double dDeliveryScheduleDeviation = Double.isNaN(dTmp) ? 0 : - dTmp / 100 * dProductionSize;

		dTmp = ((ProductIndexInfo)alProductIndexes.get(8)).getActualValue();
		double dLeakage = Double.isNaN(dTmp) ? 0 : - dTmp / 100 * dProductionSize;

		dTmp = ((ProductIndexInfo)alProductIndexes.get(9)).getActualValue();
		double dCustomerSatisfaction = Double.isNaN(dTmp) ? 0 : - (1 - dTmp / 100) * dProductionSize;
        */
        double dTmp;
        double dInProgressProjects = 0, dProductionSize = 0,
            dRequirementStability = 0, dAcceptanceRate = 0,
            dRequirementCompleteness = 0, dTimeliness = 0,
            dDeliveryScheduleDeviation = 0, dLeakage = 0,
            dCustomerSatisfaction = 0, dEffortEfficiency = 0,
            dProductivityLoc = 0, dDefectRateLoc = 0;
		for (int i = 0; i < alProductIndexes.size(); i++) {
            ProductIndexInfo pi = (ProductIndexInfo) alProductIndexes.get(i);
            switch (pi.getMetricConstant()) {
                case MetricDescInfo.IN_PROGRESS_PROJECTS:
                    dTmp = pi.getActualValue();
                    dInProgressProjects = Double.isNaN(dTmp) ? 0 : dTmp * 10;
                    break;
                case MetricDescInfo.PRODUCTION_SIZE:
                    dTmp = pi.getActualValue();
                    dProductionSize = Double.isNaN(dTmp) ? 0 : dTmp / 100;
                    break;
                case MetricDescInfo.REQUIREMENT_STABILITY:
                    dTmp = pi.getActualValue();
                    dRequirementStability =
                        Double.isNaN(dTmp) ? 0 : dTmp / 100 * dProductionSize;
                    break;
                case MetricDescInfo.ACCEPTANCE_RATE:
                    dTmp = pi.getActualValue();
                    dAcceptanceRate = Double.isNaN(dTmp) ?
                            0 : - (1 - dTmp / 100)  * dProductionSize;
                    break;
                case MetricDescInfo.REQUIREMENT_COMPLETENESS:
                    dTmp = pi.getActualValue();
                    dRequirementCompleteness = Double.isNaN(dTmp) ?
                            0 : - (1 - dTmp / 100) * dProductionSize;
                    break;
                case MetricDescInfo.TIMELINESS:
                    dTmp = pi.getActualValue();
                    dTimeliness = Double.isNaN(dTmp) ?
                            0 : - (1 - dTmp / 100) * dProductionSize;
                    break;
                case MetricDescInfo.DELIVERY_SCHEDULE_DEVIATION:
                    dTmp = pi.getActualValue();
                    dDeliveryScheduleDeviation = Double.isNaN(dTmp) ?
                            0 : - dTmp / 100 * dProductionSize;
                    break;
                case MetricDescInfo.CUSTOMER_SATISFACTION:
                    dTmp = pi.getActualValue();
                    dCustomerSatisfaction = Double.isNaN(dTmp) ?
                            0 : - (1 - dTmp / 100) * dProductionSize;
                    break;
                case MetricDescInfo.EFFORT_EFFICIENCY:
                    //(Should insert later after defined)
                    //dTmp = pi.getActualValue();
                    //dEffortEfficiency = 
                    break;
                case MetricDescInfo.PRODUCTIVITY_LOC:
                    //(Should insert later after defined)
                    //dTmp = pi.getActualValue();
                    //dProductivityLoc = 
                    break;
                case MetricDescInfo.DEFECT_RATE_LOC:
                    //(Should insert later after defined)
                    //dTmp = pi.getActualValue();
                    //dDefectRateLoc = 
                    break;
            }
		}
		return (dInProgressProjects + dProductionSize + dRequirementStability
                + dAcceptanceRate + dRequirementCompleteness + dTimeliness
                + dDeliveryScheduleDeviation + dLeakage + dCustomerSatisfaction
                + dEffortEfficiency + dProductivityLoc + dDefectRateLoc);
	}	

	/**
	 * Returns index drill info record for set of projects
	 */
	private static IndexDrillInfo getIndexDrillInfo(
            WorkUnitInfo wui, ReportMonth actualMonth, int nMetricID,
            Vector vtInProgressProjects, Vector vtLastInProgressProjects) {
		ReportMonth lastMonth = new ReportMonth(actualMonth);
		lastMonth.moveToPreviousMonth();
		IndexDrillInfo indDrillInf = new IndexDrillInfo();
		indDrillInf.setWorkUnitID(wui.workUnitID);
		indDrillInf.setWorkUnitName(wui.workUnitName);
		switch (nMetricID) {
			case MetricDescInfo.IN_PROGRESS_PROJECTS :
				indDrillInf.setPlannedValue(getPlannedValueFromPlan(wui.workUnitID, actualMonth, nMetricID));
				indDrillInf.setActualValue(getActualNumOfInProgressProjects(wui.workUnitID, actualMonth));
				indDrillInf.setLastValue(getActualNumOfInProgressProjects(wui.workUnitID, lastMonth));
				indDrillInf.setMetricType(HIGH_GOOD);
				break;
			case MetricDescInfo.PRODUCTION_SIZE :
				indDrillInf.setPlannedValue(getPlannedProductionSize(vtInProgressProjects));
				indDrillInf.setActualValue(getActualProductionSize(vtInProgressProjects, actualMonth));
				indDrillInf.setLastValue(getActualProductionSize(vtLastInProgressProjects, lastMonth));
				indDrillInf.setMetricType(HIGH_GOOD);
				break;
            case MetricDescInfo.PRODUCTIVITY_LOC :
                // Productivity LOC index
//                ProductIndexInfo productivityLocIndex =
//                        getProductivityLocIndexInfo(wui, actualMonth);
//                indDrillInf.setPlannedValue(productivityLocIndex.getActualPlannedValue());
//                indDrillInf.setActualValue(productivityLocIndex.getActualValue());
//                indDrillInf.setLastValue(productivityLocIndex.getLastValue());
//                indDrillInf.setMetricType(HIGH_GOOD);
//                break;
			case MetricDescInfo.REQUIREMENT_STABILITY :
			case MetricDescInfo.ACCEPTANCE_RATE :
			case MetricDescInfo.REQUIREMENT_COMPLETENESS :
			case MetricDescInfo.TIMELINESS :
				indDrillInf.setPlannedValue(getPlannedValueFromNorm(wui, actualMonth, nMetricID));
				indDrillInf.setActualValue(getActualMetricValue(vtInProgressProjects, actualMonth, nMetricID));
				indDrillInf.setLastValue(getActualMetricValue(vtLastInProgressProjects, lastMonth, nMetricID));
				indDrillInf.setMetricType(HIGH_GOOD);
				break;
			case MetricDescInfo.DELIVERY_SCHEDULE_DEVIATION :
			case MetricDescInfo.DEFECT_RATE :
			case MetricDescInfo.LEAKAGE :
            case MetricDescInfo.DEFECT_RATE_LOC :
				indDrillInf.setPlannedValue(getPlannedValueFromNorm(wui, actualMonth, nMetricID));
				indDrillInf.setActualValue(getActualMetricValue(vtInProgressProjects, actualMonth, nMetricID));
				indDrillInf.setLastValue(getActualMetricValue(vtLastInProgressProjects, lastMonth, nMetricID));
				indDrillInf.setMetricType(HIGH_BAD);
				break;
//            case MetricDescInfo.DEFECT_RATE_LOC :
//                // Defect Rate LOC index
//                ProductIndexInfo defectRateLocIndex =
//                        getDefectRateLocIndexInfo(wui, actualMonth);
//                indDrillInf.setPlannedValue(defectRateLocIndex.getActualPlannedValue());
//                indDrillInf.setActualValue(defectRateLocIndex.getActualValue());
//                indDrillInf.setLastValue(defectRateLocIndex.getLastValue());
//                indDrillInf.setMetricType(HIGH_BAD);
//                break;
			case MetricDescInfo.CUSTOMER_SATISFACTION :
            case MetricDescInfo.EFFORT_EFFICIENCY :
				indDrillInf.setPlannedValue(getPlannedValueFromNorm(wui, actualMonth, nMetricID));
				indDrillInf.setActualValue(getActualMetricValue(vtInProgressProjects, actualMonth, nMetricID));
				indDrillInf.setLastValue(getActualMetricValue(vtLastInProgressProjects, lastMonth, nMetricID));
				indDrillInf.setMetricType(HIGH_GOOD);
				break;
			case MetricDescInfo.NONCONFORMING_PRODUCT_RATE :
				indDrillInf.setPlannedValue(getPlannedValueFromPlan(wui.workUnitID, actualMonth, nMetricID));
				indDrillInf.setActualValue(getActualNonconformingProductRate(vtInProgressProjects, actualMonth));
				indDrillInf.setLastValue(getActualNonconformingProductRate(vtLastInProgressProjects, lastMonth));
				indDrillInf.setMetricType(HIGH_BAD);
				break;
		}
		return indDrillInf;
	}
    
    /**
     * Get summary information (of all Workunits (Projects)) by average of
     * metrics of all workunits OR TOTAL value of all workunits if it is
     * PRODUCTION_SIZE or IN_PROGRESS_PROJECTS
     * @param wuiRoot
     * @param vtIndexDrill
     * @return Summary Index infomation
     */
    public static IndexDrillInfo getSummaryIndexDrill(
        WorkUnitInfo wuiRoot, int nMetricID, Vector vtIndexDrill) {
        
        IndexDrillInfo sumInfo = new IndexDrillInfo();
        if (vtIndexDrill.size() > 0) {
            int nPlans = 0, nActuals = 0, nLasts = 0;
            double totalPlan = 0.0, totalActual = 0.0, totalLast = 0.0;
            IndexDrillInfo idi = new IndexDrillInfo();
            // Get summary of all numbers exclude Double.NaN
            for (int i = 0; i < vtIndexDrill.size(); i++) {
                idi = (IndexDrillInfo) vtIndexDrill.get(i);
                if (!Double.isNaN(idi.getPlannedValue())) {
                    totalPlan += idi.getPlannedValue();
                    nPlans++;
                }
                if (!Double.isNaN(idi.getActualValue())) {
                    totalActual += idi.getActualValue();
                    nActuals++;
                }
                if (!Double.isNaN(idi.getLastValue())) {
                    totalLast += idi.getLastValue();
                    nLasts++;
                }
            }
            double avg = 0;
            if (nPlans > 0) {
                avg = totalPlan / nPlans;
                if ((nMetricID == MetricDescInfo.PRODUCTION_SIZE) ||
                    (nMetricID == MetricDescInfo.IN_PROGRESS_PROJECTS)) {
                    sumInfo.setPlannedValue(totalPlan);
                }
                else {
                    sumInfo.setPlannedValue(avg);
                }
            }
            if (nActuals > 0) {
                avg = totalActual / nActuals;
                if ((nMetricID == MetricDescInfo.PRODUCTION_SIZE) ||
                    (nMetricID == MetricDescInfo.IN_PROGRESS_PROJECTS)) {
                    sumInfo.setActualValue(totalActual);
                }
                else {
                    sumInfo.setActualValue(avg);
                }
            }
            if (nLasts > 0) {
                avg = totalLast / nLasts;
                if ((nMetricID == MetricDescInfo.PRODUCTION_SIZE) ||
                    (nMetricID == MetricDescInfo.IN_PROGRESS_PROJECTS)) {
                    sumInfo.setLastValue(totalLast);
                }
                else {
                    sumInfo.setLastValue(avg);
                }
            }
            sumInfo.setWorkUnitID(wuiRoot.workUnitID);
            sumInfo.setWorkUnitName(wuiRoot.workUnitName);
            // Get other information by last element because this common
            // information is the same with every elements
            sumInfo.setMetricType(idi.getMetricType());
        }
        return sumInfo;
    }

	/**
	 * Returns index drill for work unit 
	 * @param nWorkUnitID ident.number of work unit
	 * @param actualMonth report month
	 * @param nMetricID ident. of metric
	 * @return ArrayList
	 */
	public static Vector getIndexDrill(long nWorkUnitID, ReportMonth actualMonth, int nMetricID) {
		Vector alIndexDrill = new Vector();
		ReportMonth lastMonth = new ReportMonth(actualMonth);
		lastMonth.moveToPreviousMonth();
		
//		Vector vtRootInProgressProjects = new Vector();
//		Vector vtRootLastInProgressProjects = new Vector();
		Vector vtInProgressProjects = new Vector();
		Vector vtLastInProgressProjects = new Vector();

		Vector vtDirectChild, vtInprogress, vtLastInprogress;
		WorkUnitInfo wuiRoot = WorkUnit.getWorkUnitInfo(nWorkUnitID);

		// NOTE: date dependency only for group root, not for organization root 
		// (group doesn't have create_date or finish_date) like projects
		if (wuiRoot.type == WorkUnitInfo.TYPE_GROUP) {
			// be aware: list of children projects must contain all in-progress during 2 months
//			vtDirectChild = WorkUnit.getChildrenProjectUnit(nWorkUnitID, 
//					lastMonth.getFirstDayOfMonth(),
//                    getReportDayOfMonth(actualMonth));
            
            // Updated 02-Mar-2007: separate into actual and last, not merge by two months
            vtInprogress = WorkUnit.getChildrenProjectUnit(nWorkUnitID, 
                    actualMonth.getFirstDayOfMonth(),
                    getReportDayOfMonth(actualMonth));
            vtLastInprogress = WorkUnit.getChildrenProjectUnit(nWorkUnitID, 
                    lastMonth.getFirstDayOfMonth(),
                    getReportDayOfMonth(lastMonth));
            // Create HashMap (hash table) to store unique workunits
            java.util.HashMap uniqueWU = new java.util.HashMap();
            for (int i = 0; i < vtInprogress.size(); i++) {
                WorkUnitInfo wui = (WorkUnitInfo) vtInprogress.get(i);
                uniqueWU.put(new Long(wui.workUnitID), wui);
            }
            for (int i = 0; i < vtLastInprogress.size(); i++) {
                WorkUnitInfo wui = (WorkUnitInfo) vtLastInprogress.get(i);
                uniqueWU.put(new Long(wui.workUnitID), wui);
            }
            Iterator itr = uniqueWU.values().iterator();
            vtDirectChild = new Vector();
            while (itr.hasNext()) {
                WorkUnitInfo wui = (WorkUnitInfo) itr.next();
                vtDirectChild.add(wui);
            }
		}
		else {
			vtDirectChild = WorkUnit.getChildrenWU(nWorkUnitID);
		}

		for (int i = 0; i < vtDirectChild.size(); i++) {
			WorkUnitInfo wuiTemp = (WorkUnitInfo)vtDirectChild.elementAt(i);
			vtInProgressProjects = getInProgressProjects(wuiTemp.workUnitID,
                    actualMonth);
			vtLastInProgressProjects = getInProgressProjects(wuiTemp.workUnitID,
                    lastMonth);

//			vtRootInProgressProjects.addAll(vtInProgressProjects);
//			vtRootLastInProgressProjects.addAll(vtLastInProgressProjects);
			
			IndexDrillInfo indDrillInf = getIndexDrillInfo(wuiTemp, actualMonth,
                    nMetricID, vtInProgressProjects, vtLastInProgressProjects);
			
			alIndexDrill.add(indDrillInf);
		}
        //Removed this expesive function call because above loop already count for all projects
//		IndexDrillInfo indDrillInf = getIndexDrillInfo(wuiRoot, actualMonth, nMetricID, 
//					vtRootInProgressProjects, vtRootLastInProgressProjects);
        
        IndexDrillInfo indDrillInf = new IndexDrillInfo();
        // Calculate summary for Productivity LOC and Defect rate LOC
//        if (nMetricID == MetricDescInfo.PRODUCTIVITY_LOC ||
//            nMetricID == MetricDescInfo.DEFECT_RATE_LOC)
//        {
//            indDrillInf = getIndexDrillInfo(wuiRoot, actualMonth, nMetricID, 
//                    vtInProgressProjects, vtLastInProgressProjects);
//        }
        // Get summary for other metrics (calculate by Average OR Summary of
        // actual, plan values)
//        else {
              indDrillInf = getSummaryIndexDrill(wuiRoot, nMetricID, alIndexDrill);
//        }
        alIndexDrill.add(indDrillInf);

		return alIndexDrill;
	}
}