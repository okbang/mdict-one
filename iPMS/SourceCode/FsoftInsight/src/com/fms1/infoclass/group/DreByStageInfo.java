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
 * Created on Aug 17, 2005
 *
 */
package com.fms1.infoclass.group;

import java.util.Vector;

import com.fms1.infoclass.DefectInfo;
import com.fms1.infoclass.ProjectSizeInfo;
import com.fms1.tools.CommonTools;

/**
 * @author trungtn
 * Store Defect Removal Efficiency by stage of project
 */
public class DreByStageInfo implements java.io.Serializable {   // Make it Serializable when store in a session
    public final static int QC_ACTIVITIES = 9;      // RReview,DReview,CReview,...
    public final static int STANDARD_STAGES = 6;    // Standard stages numbers
    public int stages;          // Numbers of stages 
    public int runningStage;    // Current running stage 
    public Vector stageNames = new Vector();    // Project stage names (milestones)
    
    public Vector rows = new Vector();
    public Row sumRow = new Row();      // Total row
    // Each row will contain value for each QC Activity, 0=RReview,1=DReview,...
    public class Row {
        public double[] norms;
        public double[] plans;
        public double[] plan_wd;    // Convert to Weighted Defect
        public double[] replans;    // Weighted Defect
        public double[] actuals;
        public double[] forecasts;
        public double[] replan_defect_rate; // Replan defect rate, only use with QCID = 1
        public double[] deviations;
    }
    public int[] stageCoeff;        // Coefficient of stages, based on standard stage count, (actual norm[i] = norm[i] / stageCoeff[i])
    public int[] standardStageMap;  // Standard stage mapping
    public long[] milestone_Id;     // Milestone_ID of stages
    public boolean[] milestoneExisted;  // Detect inconsistency between PROJECT milestones and milestones in PLANS_QC_STAGE table 
    public boolean isConsistent = true;
    public double[] normQC = new double[QC_ACTIVITIES]; // DRE norms for QC activities
    public int planRecords = 0;     // Numbers of records in PLAN_QC_STAGE table
    public double[] estimatedSize;      // (Re-)Estimated size of project for stage
    public double[] estimatedSizeUpTo;  // (Re-)Estimated size of project up to stage
    public double projectEstimatedSize = Double.NaN;    // Estimated size of project
    public double[] reestimatedDefect;  // (Re-)Estimated defect (WD) of stage
    public double[] actualDefectDeviation;
    public double[] avgDefectDeviation;
    public double[] replanDefectRate;
    public double[] actualSizeUcp;      // Total size UCP of stage (actuals)
    public double[] actualSizeUcpUpTo;  // Total size UCP up to this stage (actuals)
    //public double[] totalWDPlanned;   // Total WD planned in stage (sumRow.plan_wd)
    
    public DreByStageInfo(int n_Stages) {
        Row row;
        stages = n_Stages;
        for (int i = 0; i < QC_ACTIVITIES; i++) {
            row = new Row();
            row.norms = new double[n_Stages];
            row.plans = new double[n_Stages];
            row.plan_wd = new double[n_Stages];
            row.replans = new double[n_Stages];
            row.actuals = new double[n_Stages];
            row.forecasts = new double[n_Stages];
            row.replan_defect_rate = new double[n_Stages];
            row.deviations = new double[n_Stages];
            for (int j = 0; j < n_Stages; j++) {
                row.norms[j] = Double.NaN;
                row.plans[j] = Double.NaN;
                row.plan_wd[j] = Double.NaN;
                row.replans[j] = Double.NaN;
                row.actuals[j] = Double.NaN;
                row.forecasts[j] = Double.NaN;
                row.replan_defect_rate[j] = Double.NaN;
                row.deviations[j] = Double.NaN;
            }
            rows.add(row);
        }
        sumRow.norms = new double[n_Stages];
        sumRow.plans = new double[n_Stages];
        sumRow.plan_wd = new double[n_Stages];
        sumRow.replans = new double[n_Stages];
        sumRow.actuals = new double[n_Stages];
        sumRow.forecasts = new double[n_Stages];
        sumRow.deviations = new double[n_Stages];
        milestoneExisted = new boolean[n_Stages];
        estimatedSize = new double[n_Stages];
        estimatedSizeUpTo = new double[n_Stages];
        reestimatedDefect = new double[n_Stages];
        actualDefectDeviation = new double[n_Stages];
        avgDefectDeviation = new double[n_Stages];
        replanDefectRate = new double[n_Stages];
        actualSizeUcp = new double[n_Stages];
        actualSizeUcpUpTo = new double[n_Stages];
        for (int i = 0; i < n_Stages; i++) {
            sumRow.norms[i] = Double.NaN;
            sumRow.plans[i] = Double.NaN;
            sumRow.plan_wd[i] = Double.NaN;
            sumRow.replans[i] = Double.NaN;
            sumRow.actuals[i] = Double.NaN;
            sumRow.forecasts[i] = Double.NaN;
            sumRow.deviations[i] = Double.NaN;
            
            milestoneExisted[i] = false;
            estimatedSize[i] = Double.NaN;
            estimatedSizeUpTo[i] = Double.NaN;
            reestimatedDefect[i] = Double.NaN;
            actualDefectDeviation[i] = Double.NaN;
            avgDefectDeviation[i] = Double.NaN;
            replanDefectRate[i] = Double.NaN;
            actualSizeUcp[i] = Double.NaN;
            actualSizeUcpUpTo[i] = Double.NaN;
        }
        for (int i = 0; i < QC_ACTIVITIES; i++) {
            normQC[i] = Double.NaN;
        }
    }
    public void calculatePlanWD(double estimatedDefect, double committedLeakage) {
        Row row;
        double estimated = estimatedDefect - committedLeakage;
        if (! Double.isNaN(estimated)) {
            for (int j = 0; j < QC_ACTIVITIES; j++) {
                row = (Row) rows.get(j);
                if (! Double.isNaN(normQC[j])) {
                    for (int i = 0; i < stages; i++) {
                        if (! Double.isNaN(row.plans[i])) {
//                            row.plan_wd[i] = Math.round(
//                                estimated * row.plans[i] * normQC[j] / 10000.0);
                            row.plan_wd[i] = estimated * row.plans[i] * normQC[j] / 10000.0;
                            sumRow.plan_wd[i] += row.plan_wd[i];
                        }
                    }
                }
            }
        }
    }
    public void calculateDeviation() {
        Row row;
        for (int j = 0; j < QC_ACTIVITIES; j++) {
            row = (Row) rows.get(j);
            for (int i = 0; i < stages; i++) {
                row.deviations[i] = CommonTools.metricDeviation(row.plan_wd[i],
                        row.replans[i], row.actuals[i]);
            }
        }
    }
    public void calculateReestimatedDefect(DefectInfo defectInfo, ProjectSizeInfo projectInfo) {
        int runSt;
        double comittedDefectRate = defectInfo.normDefectRate;
        projectEstimatedSize = projectInfo.totalReestimatedSize;
        if (Double.isNaN(projectEstimatedSize) || (projectEstimatedSize == 0)) {
            projectEstimatedSize = projectInfo.totalEstimatedSize;
        }
        // Loop from first stage to current running stage
        // (simulate running stage from 1 to current to calculate data)
        for (runSt = 1; ((runSt <= runningStage) && (runSt <= sumRow.replans.length)); runSt++) {
            if (runSt == 1) {
                replanDefectRate[0] = comittedDefectRate;
//                reestimatedDefect[0] = Math.round(estimatedSize * comittedDefectRate);
                reestimatedDefect[0] = projectEstimatedSize * comittedDefectRate;
            }
            else if ((runSt == 2) && (stages >= 2)) {
                actualDefectDeviation[0] =
                    (sumRow.actuals[0] - sumRow.plan_wd[0]) / sumRow.plan_wd[0];
                replanDefectRate[1] = comittedDefectRate;
                //reestimatedDefect[1] = Math.round(estimatedSizeUpTo[1] * replanDefectRate[1]);
//                reestimatedDefect[1] = Math.round(estimatedSize * replanDefectRate[1]);
                reestimatedDefect[1] = projectEstimatedSize * replanDefectRate[1];
            }
            else if (runSt == 3) {
                // actualDefDeviation[0] = 0
                for (int i = 0; ((i < 2) && (i < actualDefectDeviation.length)); i++) {
                    actualDefectDeviation[i] =
                        (sumRow.actuals[i] - sumRow.plan_wd[i]) / sumRow.plan_wd[i];
                }
            }
//            else if (runSt == 4) {
//                actualDefectDeviation[runSt - 1] =
//                    (sumRow.actuals[runSt - 1] - sumRow.plan_wd[runSt - 1]) /
//                    sumRow.plan_wd[runSt - 1];
//            }
            else {
                if ((! Double.isNaN(sumRow.replans[runSt - 2])) &&
                        (sumRow.replans[runSt - 2] != 0)) {
                    actualDefectDeviation[runSt - 1] =
                        (sumRow.actuals[runSt - 2] -
                        sumRow.replans[runSt - 2]) /
                        sumRow.replans[runSt - 2];
                }
                else if ((! Double.isNaN(sumRow.plan_wd[runSt - 2])) &&
                        (sumRow.plan_wd[runSt - 2] != 0)) {
                    actualDefectDeviation[runSt - 1] =
                        (sumRow.actuals[runSt - 2] -
                        sumRow.plan_wd[runSt - 2]) /
                        sumRow.plan_wd[runSt - 2];
                }
            }
            if ((runSt > 1) && (runSt <= avgDefectDeviation.length)) {
                avgDefectDeviation[runSt - 1] =
                    (CommonTools.arraySum(actualDefectDeviation, 0, runSt - 2) / (runSt - 1));
            }
            
            if (runSt > 2) {
                replanDefectRate[runSt - 1] = replanDefectRate[runSt - 2] *
                        (1 + avgDefectDeviation[runSt - 1]);
                reestimatedDefect[runSt - 1] =  projectEstimatedSize * replanDefectRate[runSt - 1];
            }
            for (int i = runSt; i < reestimatedDefect.length; i++) {
                // The first defect rate is committed defect rate
                replanDefectRate[i] = replanDefectRate[runSt - 1];
                reestimatedDefect[i] = reestimatedDefect[runSt - 1];
            }
        }
    }
    public void calculateForecast(double committedLeakage) {
        Row row;
        double reestimatedDf = 0;
        if (runningStage <= stages) {
            reestimatedDf = reestimatedDefect[runningStage - 1];
        }
        else if (runningStage >= 2) {
            reestimatedDf = reestimatedDefect[runningStage - 2];
        }
        for (int j = 0; j < QC_ACTIVITIES; j++) {
            if (! Double.isNaN(normQC[j])) {
                row = (Row) rows.get(j);
                for (int i = 1; i < stages; i++) {  // Forecast from second stage
                    row.forecasts[i] = Math.round(
                            (reestimatedDf - committedLeakage) *
                            row.plans[i] * normQC[j] / 10000.0);
                }
            }
        }
    }
    // Calculate total row, fomula=Plan by QC * value (norm OR plan)
    public void calulateSumRow() {
        Row row;
        for (int i = 0; i < stages; i++) {
            sumRow.norms[i] = Double.NaN;
            sumRow.plans[i] = Double.NaN;
            sumRow.plan_wd[i] = Double.NaN;
            sumRow.replans[i] = Double.NaN;
            sumRow.actuals[i] = Double.NaN;
            sumRow.forecasts[i] = Double.NaN;
            sumRow.deviations[i] = Double.NaN;
        }
        for (int j = 0; j < QC_ACTIVITIES; j++) {
            row = (Row) rows.get(j);
            if (! Double.isNaN(normQC[j])) {
                for (int i = 0; i < stages; i++) {
                    if (! Double.isNaN(row.norms[i])) {
                        sumRow.norms[i] = CommonTools.nanToZero(sumRow.norms[i]);
                        sumRow.norms[i] += (row.norms[i] * normQC[j]) / 100;
                    }
                    if (! Double.isNaN(row.plans[i])) {
                        sumRow.plans[i] = CommonTools.nanToZero(sumRow.plans[i]);
                        sumRow.plans[i] += (row.plans[i] * normQC[j]) / 100; 
                    }
                }
            }
            for (int i = 0; i < stages; i++) {
                if (! Double.isNaN(row.plan_wd[i])) {
                    sumRow.plan_wd[i] = CommonTools.nanToZero(sumRow.plan_wd[i]);
                    sumRow.plan_wd[i] += Math.round(row.plan_wd[i]); 
                }
                if (! Double.isNaN(row.replans[i])) {
                    sumRow.replans[i] = CommonTools.nanToZero(sumRow.replans[i]);
                    sumRow.replans[i] += row.replans[i]; 
                }
                if (! Double.isNaN(row.actuals[i])) {
                    sumRow.actuals[i] = CommonTools.nanToZero(sumRow.actuals[i]);
                    sumRow.actuals[i] += row.actuals[i]; 
                }
                if (! Double.isNaN(row.forecasts[i])) {
                    sumRow.forecasts[i] = CommonTools.nanToZero(sumRow.forecasts[i]);
                    sumRow.forecasts[i] += row.forecasts[i]; 
                }
            }
        }
        for (int i = 0; i < stages; i++) {
            sumRow.deviations[i] = CommonTools.metricDeviation(
                    sumRow.plan_wd[i], sumRow.replans[i], sumRow.actuals[i]);
        }
    }
}
