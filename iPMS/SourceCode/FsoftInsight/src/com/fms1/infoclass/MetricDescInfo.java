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
 
 package com.fms1.infoclass;
import java.util.Hashtable;
import java.util.Vector;
import com.fms1.common.Metrics;
import com.fms1.tools.CommonTools;
/**
 * @author manu
 * info from metric desc table
 */
public class MetricDescInfo {
	//Metric constants
	public static final int ACCEPTANCE_RATE = 1;
	public static final int REQUIREMENT_COMPLETENESS = 2;
	public static final int REQUIREMENT_STABILITY = 3;
	public static final int TIMELINESS = 4;
	public static final int RESPONSE_TIME = 5;
	public static final int DURATION_ACHIEVEMENT = 6;
	public static final int PROJECT_SCHEDULE_DEVIATION = 7;
	public static final int STAGE_SCHEDULE_DEVIATION = 8;
	public static final int DELIVERY_SCHEDULE_DEVIATION = 9;
    public static final int EFFORT_EFFICIENCY = 254;
	public static final int EFFORT_EFFECTIVENESS = 11;
	public static final int PROJECT_MANAGEMENT = 13;
	public static final int QUALITY_COST = 14;
	public static final int CORRECTION_COST = 15;
	public static final int LEAKAGE = 36;
	public static final int DEFECT_RATE = 35;
	public static final int DEFECT_RATE_LOC = 255;
	public static final int DEFECT_RATE_UNIT_TEST_LOC = 256;
	public static final int DEFECT_RATE_INTEGRATION_TEST_LOC = 257;
	public static final int DEFECT_RATE_SYSTEM_TEST_LOC = 258;
	public static final int REVIEW_EFFICIENCY = 38;
	public static final int TEST_EFFICIENCY = 39;
	public static final int DEFECT_REMOVAL_EFFICIENCY = 37;
	public static final int QUALITY_ACHIEVEMENT = 40;
	public static final int CUSTOMER_SATISFACTION = 41;
	public static final int PRODUCTIVITY = 47;
	public static final int PRODUCTIVITY_LOC = 259;
	public static final int PRODUCTIVITY_ACHIEVEMENT = 50;
	public static final int PRODUCTIVITY_ACHIEVEMENT_LOC = 260;
	public static final int TEST_CASE_DENSITY_LOC = 261;
	public static final int UNIT_TEST_CASE_DENSITY_LOC = 262;
	public static final int INTEGRATION_TEST_CASE_DENSITY_LOC = 263;
	public static final int SYSTEM_TEST_CASE_DENSITY_LOC = 264;
	public static final int REVIEW_EFFECTIVENESS = 48;
	public static final int TEST_EFFECTIVENESS = 49;
	public static final int SIZE_ACHIEVEMENT = 52;
	public static final int SIZE_DEVIATION = 53;
	public static final int PROCESS_COMPLIANCE = 65;
	public static final int CUSTOMER_COMPLAINTS = 42;
	public static final int OVERDUE_NCsOBs = 420;
	public static final int CP_TIME = 66;
	public static final int EFFORT_DEVIATION = 12;
	public static final int IN_PROGRESS_PROJECTS = 67;
	public static final int PRODUCTION_SIZE = 51;
	public static final int NONCONFORMING_PRODUCT_RATE = 68;
	public static final int DELIVERY_LIST = 69;
	//Process effort distribution
	public static final int REQUIREMENT_EFFORT = 17;
	public static final int DESIGN_EFFORT = 18;
	public static final int CODING_EFFORT = 19;
	public static final int DEPLOYMENT_EFFORT = 20;
	public static final int CUSTOMER_SUPPORT_EFFORT = 21;
	public static final int TEST_EFFORT = 22;
	public static final int CM_EFFORT = 23;
	public static final int PROJECT_PLANNING_EFFORT = 25;
	public static final int PROJECT_MONITORING_EFFORT = 26;
	public static final int QUALITY_CONTROL_EFFORT = 24;
	public static final int TRAINING_EFFORT = 27;
	public static final int OTHER_EFFORT = 28;
	//Stage effort distribution DEPRECATED
	public static final int INITIATION_EFFORT = 29;
	public static final int DEFINITION_EFFORT = 30;
	public static final int SOLUTION_EFFORT = 31;
	public static final int CONSTRUCTION_EFFORT = 32;
	public static final int TRANSITION_EFFORT = 33;
	public static final int TERMINATION_EFFORT = 34;
	//defect dist by origin
	public static final int DEFECTS_FROM_REQUIREMENTS = 43;
	public static final int DEFECTS_FROM_DESIGN = 44;
	public static final int DEFECTS_FROM_CODING = 45;
	public static final int DEFECTS_FROM_OTHER = 46;
	//STAFF
	public static final int TOTAL_STAFF = 57;
	public static final int TOTAL_DEVELOPERS = 58;
	public static final int BUSY_RATE = 60;
	public static final int BILLABLE_RATE = 75;
	public static final int TOTAL_TEMPRORARY_STAFF = 59;
	//FINANCE
	public static final int REVENUE = 76;
	public static final int COST_OF_GOOD_SOLD = 77;
	public static final int OPERATION_EXPENSE = 78;
	//INFRASTRUCTURE
	public static final int INTERNET_BANDWIDTH = 64;
	//??
	public static final int SPENT_EFFORT = 10;
	public static final int TRANSLATION_COST = 16;
	//SQA
	public static final int SQA_TIMELINESS = 79;
	public static final int SQA_TIMELINESS_INSPECTION = 80;
	public static final int SQA_TIMELINESS_DP = 81;
	public static final int SQA_PREVENTION_COST = 87;
	public static final int SQA_BASELINE_RATE = 82;
	public static final int SQA_EFFORT_DPC_BY_PROJECT = 83;
	public static final int SQA_EFFORT_BASELINE = 84;
	public static final int SQA_EFFORT_INSPECTION = 85;
	public static final int SQA_TEST_COVERAGE = 86;
	public static final int SQA_RESPONSE_TIME = 88;
	public static final int SQA_EFFORT_PROJECT = 89;
	//PROCESS
	public static final int REPEATED_NCS = 90;
	public static final int NCS_IN_TIME = 91;
	public static final int PQA_RESPONSE_TIME = 92;
	public static final int PQA_SATISFACTION = 93;
	public static final int OVERDUE_TARGETS = 94;
	public static final int PQA_TIMELINESS = 96;
	public static final int FEASIBLE_DECISIONS = 97;
	public static final int SATISFIED_INDICATORS = 95;
	public static final int PQA_FIX_TIME = 98;
    // DRE - Defect Removal Efficiency
    public static final String METRIC_DRE_QC = "5.9.";
    public static final String METRIC_DRE = "5.10.";
    public static final String METRIC_DRE_RR = "5.10.1.";
    public static final String METRIC_DRE_DR = "5.10.2.";
    public static final String METRIC_DRE_CR = "5.10.3.";
    public static final String METRIC_DRE_UT = "5.10.4.";
    public static final String METRIC_DRE_IT = "5.10.5.";
    public static final String METRIC_DRE_ST = "5.10.6.";
    public static final String METRIC_DRE_OT = "5.10.7.";
    // Norms of DRE by QC Activity
    public static final int DRE_QC_RREVIEW = 171;
    public static final int DRE_QC_DREVIEW = 172;
    public static final int DRE_QC_CREVIEW = 173;
    public static final int DRE_QC_UTEST = 174;
    public static final int DRE_QC_ITEST = 175;
    public static final int DRE_QC_STEST = 176;
    public static final int DRE_QC_OREVIEW = 240;
	public static final int DRE_QC_OTEST = 241;
	public static final int DRE_QC_OTHER = 177;
    //FROM 99 to 164: RCR metrics
	//Metric group constants for software, the value is the mgroup_id from table metric group
	public static final int GR_SOFTWARE = 1;
	public static final int GR_REQUIREMENT = 2;
	public static final int GR_SCHEDULE = 3;
	public static final int GR_EFFORT = 4;
	public static final int GR_PRODUCT_QUALITY = 5;
	public static final int GR_PRODUCTIVITY = 6;
	public static final int GR_PRODUCT_SIZE = 7;
	public static final int GR_SQA = 20;
	public static final int GR_DP = 21;
	public static final int GR_PROCESS = 10;
	//Defect rate by work product	
	public static final int DR_URD=220;
	public static final int DR_SRS=221;
	public static final int DR_RPROTOTYPE=222;
	public static final int DR_ADD=223;
	public static final int DR_DDD=224;
	public static final int DR_DPROTOTYPE=225;
	public static final int DR_SOFTWARE_PACKAGE=226;
	public static final int DR_TC_TDATA=227;
	public static final int DR_USER_MANUAL=228;
	public static final int DR_OTHERS=229; 
	//Defect removal efficiency by work product
	public static final int DRE_URD=230;
	public static final int DRE_SRS=231;
	public static final int DRE_RPROTOTYPE=232;
	public static final int DRE_ADD=233;
	public static final int DRE_DDD=234;
	public static final int DRE_DPROTOTYPE=235;
	public static final int DRE_SOFTWARE_PACKAGE=236;
	public static final int DRE_TC_TDATA=237;
	public static final int DRE_USER_MANUAL=238;
	public static final int DRE_OTHERS=239;
	//artificial groups not in DB the ID must be > 1000 to differentiate from stdrt groups
	public static final int GR_SET_SQA_HOME_PAGE = 1001;
	public static final int[] SET_SQA_HOME_PAGE =
		{
			SQA_TIMELINESS,
			SQA_TIMELINESS_INSPECTION,
			SQA_TIMELINESS_DP,
			SQA_PREVENTION_COST,
			SQA_BASELINE_RATE,
			SQA_EFFORT_DPC_BY_PROJECT,
			SQA_EFFORT_BASELINE,
			SQA_EFFORT_INSPECTION,
			SQA_TEST_COVERAGE,
			SQA_RESPONSE_TIME,
			SQA_EFFORT_PROJECT };
	public static final int[] trackedEffortProcessId =
		{
			REQUIREMENT_EFFORT,
			DESIGN_EFFORT,
			CODING_EFFORT,
			DEPLOYMENT_EFFORT,
			CUSTOMER_SUPPORT_EFFORT,
			TEST_EFFORT,
			CM_EFFORT,
			PROJECT_PLANNING_EFFORT,
			PROJECT_MONITORING_EFFORT,
			QUALITY_CONTROL_EFFORT,
			TRAINING_EFFORT,
			OTHER_EFFORT };
	public static int getRCRMetricConstant(int processid, int standardStageID) {
		int i = 0;
		try {
			
			for (; i < ProcessInfo.trackedProcessIdForRCR.length; i++) {
				if (ProcessInfo.trackedProcessIdForRCR[i] == processid)
					break;
			}
		}
		catch (Exception e) {
			System.err.println("index:"+i);
			e.printStackTrace();
		}
        return 98 + i * StageInfo.stageList.length
		+standardStageID;
	}
    /**
     * [0]-ProcessID
     * [1]-stageid
     */
    public static int[] getRCRMetricProcStage(int metricID) {
        int [] retval={-1,-1}; 
        try {
        
        retval[1]=(int)(metricID-98)%StageInfo.stageList.length;
        retval[0]=(int)(metricID-98) /StageInfo.stageList.length;
        if (retval[1]==0){
            retval[1]=StageInfo.stageList.length;
            retval[0]--;
        }
        retval[0]=ProcessInfo.trackedProcessIdForRCR[retval[0]];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }
    public static boolean isRCRMetric(int metricID) {
        return (metricID>98 && metricID<= 98+ ProcessInfo.trackedProcessIdForRCR.length * StageInfo.stageList.length);
    }
	/* must map with the arrays in requirementInfo 
	*-1 stands for ACCEPTANCE EFFORT which doesnt exist because the completion rate is always 100% for accepted requirements
	*/
    public static int getCompletionMetricByProcess(int processId){ 
        int arrayId=CommonTools.arrayScan(ProcessInfo.RCRProcesses,processId);
        if (arrayId<0)
            return -1;
        else
            return completionMetrics[arrayId];
    }
	public static final int[] completionMetrics =
		{ REQUIREMENT_EFFORT, DESIGN_EFFORT, CODING_EFFORT, TEST_EFFORT, DEPLOYMENT_EFFORT, CUSTOMER_SUPPORT_EFFORT };
	public int metricConstant;
	public String metricID;
	public String metricName = null;
	public String unit = null;
	public int displayIndex;
	public int hotPriority;
	public int colorType;
	public long mgroupID;
	public String definition;
	public static Vector metricDescList = Metrics.getAllMetricDesc(); //this data is very static, and can be cached
	// Metric hash table, stored by key = metric constant
    public static Hashtable metricHashTbByMetricConstant =
            Metrics.constructMetricHashTb(metricDescList);
    
/* Hieunv1 rem this function because it affect in Performance of Tools.
 * 11/Dec/2007 
 	public final static String getMetricID(int metricConstant) {
		int i = 0;
		try {
			Vector metrics = metricDescList;
			MetricDescInfo metricDesc;
			for (i = 0; i < metrics.size(); i++) {
				metricDesc = (MetricDescInfo) metrics.elementAt(i);
				if (metricDesc.metricConstant == metricConstant)
					return metricDesc.metricID;
			}
			return null;
		}
		catch (Exception e) {
			return null;
		}
	}
*/
    
    /**
     * Get metric information by metric constant using constructed hash table of
     * metrics (metricHashTbByMetricConstant)
     * @param metricConstant
     * @return
     */
    public static final MetricDescInfo getMetricDesc(int metricConstant) {
        MetricDescInfo metricInfo = (MetricDescInfo)
            metricHashTbByMetricConstant.get(new Integer(metricConstant));
        return metricInfo;
    }

    /**
     * Get metric ID by metric constant using constructed hash table of metrics
     * (metricHashTbByMetricConstant)
     * @param metricConstant
     * @return
     */
    public static String getMetricID(int metricConstant) {
        String metricId = "";
        MetricDescInfo metricInfo = (MetricDescInfo)
                MetricDescInfo.getMetricDesc(metricConstant);
        if (metricInfo != null) {
            metricId = metricInfo.metricID;
        }
        return metricId;
    }

	public final static int getMetricType(String metricID) {
		int i = 0;
		try {
			Vector metrics = metricDescList;
			MetricDescInfo metricDesc;
			for (i = 0; i < metrics.size(); i++) {
				metricDesc = (MetricDescInfo) metrics.elementAt(i);
				if (metricDesc.metricID.trim().equals(metricID))
					return metricDesc.metricConstant;
			}
			System.err.println("getMetricType metricID '" + metricID + "' not found at index " + i);
			return -1;
		}
		catch (Exception e) {
			System.err.println("getMetricType metricID '" + metricID + "' not found at index " + i);
			return -1;
		}
	}
    
    /**
     * Determine the specified metric constant is Process Norm (displayed in Process Norm page)
     * @param metricID
     * @return
     */
    public final static boolean isProcessNorm(int metricID) {
        boolean ret = false;
        // SELECT following value from Metric_des table, ID in following IDs
        if (((metricID >=65) && (metricID <= 68)) ||
            ((metricID >=90) && (metricID <= 98)))
        {
            ret = true;
        }
        return ret;
    }
	/**
	 * returns null if the mgroupid is a natural group
	 * 
	 */
	public static int[] getArtificialGroup(int mgroupID) {
		if (mgroupID == GR_SET_SQA_HOME_PAGE)
			return SET_SQA_HOME_PAGE;
		return null;
	}
	public static final double[][] upperBoundaries = { { ACCEPTANCE_RATE, 100d }, {
			REQUIREMENT_COMPLETENESS, 100d }, {
			REQUIREMENT_STABILITY, 100d }, {
			TIMELINESS, 100d }, {
			REVIEW_EFFICIENCY, 100d }, {
			TEST_EFFICIENCY, 100d }, {
			DEFECT_REMOVAL_EFFICIENCY, 100d }, {
			CUSTOMER_SATISFACTION, 100d }, {
			SQA_TIMELINESS, 100d }, {
			SQA_TIMELINESS_INSPECTION, 100d }, {
			SQA_TIMELINESS_DP, 100d }, {
			SQA_TEST_COVERAGE, 100d }, {
			NCS_IN_TIME, 100d }, {
			PQA_SATISFACTION, 100d }, {
			OVERDUE_TARGETS, 100d }, {
			PQA_TIMELINESS, 100d }, {
			FEASIBLE_DECISIONS, 100d }, {
			SATISFIED_INDICATORS, 100d }
	};
	public static final double[][] lowerBoundaries = { { ACCEPTANCE_RATE, 0d }, {
			REQUIREMENT_COMPLETENESS, 0d }, {
			REQUIREMENT_STABILITY, 0d }, {
			TIMELINESS, 0d }, {
			RESPONSE_TIME, 0d }, {
			EFFORT_EFFECTIVENESS, 0d }, {
			PROJECT_MANAGEMENT, 0d }, {
			QUALITY_COST, 0d }, {
			CORRECTION_COST, 0d }, {
			LEAKAGE, 0d }, {
			DEFECT_RATE, 0d }, {
			REVIEW_EFFICIENCY, 0d }, {
			TEST_EFFICIENCY, 0d }, {
			DEFECT_REMOVAL_EFFICIENCY, 0d }, {
			QUALITY_ACHIEVEMENT, 0d }, {
			CUSTOMER_SATISFACTION, 0d }, {
			PRODUCTIVITY, 0d }, {
			PRODUCTIVITY_ACHIEVEMENT, 0d }, {
			REVIEW_EFFECTIVENESS, 0d }, {
			TEST_EFFECTIVENESS, 0d }, {
			SIZE_ACHIEVEMENT, 0d }, {
			PROCESS_COMPLIANCE, 0d }, {
			CUSTOMER_COMPLAINTS, 0d }, {
			CP_TIME, 0d }, {
			PRODUCTION_SIZE, 0d }, {
			NONCONFORMING_PRODUCT_RATE, 0d }, {
			SQA_TIMELINESS, 0d }, {
			SQA_TIMELINESS_INSPECTION, 0d }, {
			SQA_TIMELINESS_DP, 0d }, {
			SQA_PREVENTION_COST, 0d }, {
			SQA_BASELINE_RATE, 0d }, {
			SQA_EFFORT_DPC_BY_PROJECT, 0d }, {
			SQA_EFFORT_BASELINE, 0d }, {
			SQA_EFFORT_INSPECTION, 0d }, {
			SQA_TEST_COVERAGE, 0d }, {
			SQA_RESPONSE_TIME, 0d }, {
			SQA_EFFORT_PROJECT, 0d }, {
			REPEATED_NCS, 0d }, {
			NCS_IN_TIME, 0d }, {
			PQA_RESPONSE_TIME, 0d }, {
			PQA_SATISFACTION, 0d }, {
			OVERDUE_TARGETS, 0d }, {
			PQA_TIMELINESS, 0d }, {
			FEASIBLE_DECISIONS, 0d }, {
			SATISFIED_INDICATORS, 0d }, {
			PQA_FIX_TIME, 0d }
	};
	/**
	 * false =min boundary
	 * true = maxbound
	 */
	public static double getBoundary(int metricConst, boolean isMinBound) {
		double[][] bounds = isMinBound ? lowerBoundaries : upperBoundaries;
		for (int i = 0; i < bounds.length; i++)
			if (metricConst == bounds[i][0]) {
				return bounds[i][1];
			}
		return Double.NaN;
	}
}
