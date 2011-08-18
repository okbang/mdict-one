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
import java.sql.Date;
public final class StageInfo {
	public static final int STAGE_INITIATION = 1;
	public static final int STAGE_DEFINITION = 2;
	public static final int STAGE_SOLUTION = 3;
	public static final int STAGE_CONSTRUCTION = 4;
	public static final int STAGE_TRANSITION = 5;
	public static final int STAGE_TERMINATION = 6;
//	must keep the mapping between the 2arrays below and with processInfo & with requirementInfo arrays
	public static final String[] stageNames =
		{ "Initiation", "Definition", "Solution", "Construction", "Transition", "Termination" };
	public static final int[] effortDistMetrics =
		{
			MetricDescInfo.INITIATION_EFFORT,
			MetricDescInfo.DEFINITION_EFFORT,
			MetricDescInfo.SOLUTION_EFFORT,
			MetricDescInfo.CONSTRUCTION_EFFORT,
			MetricDescInfo.TRANSITION_EFFORT,
			MetricDescInfo.TERMINATION_EFFORT };
	
	public static final int[] stageList =
		{ STAGE_INITIATION, STAGE_DEFINITION, STAGE_SOLUTION, STAGE_CONSTRUCTION, STAGE_TRANSITION, STAGE_TERMINATION };
	public static String getStageName(int constant) {
		return stageNames[constant - 1];
	}
	public long milestoneID = 0;
	public long prjID = 0;
	public String stage = "";
	public Date bEndD = null; //base end date = first planning
	public Date pEndD = null; //plan end date= 2nd planning
	public Date aEndD = null; //actual end date
	public Date plannedEndDate = null; // =2nd planning if 2nd planning is defined otherwise =first planning 
	public Date plannedBeginDate = null;
	public Date actualBeginDate = null;
	public int numIternation = 0;
	public String description = "";
	public String milestone = "";
	public String isOntime = "N/A";
	public String deviation = "N/A";
	public int iterationCnt = 0;
	public String pDuration = "N/A";
	public String aDuration = "N/A";
	public String duDeviation = "N/A";
	public String comments = null;
	public int StandardStage = 0;
	public double estimatedEffort = Double.NaN;
	public double reEstimatedEffort = Double.NaN;
    //use stageEffortInfo
	public long QGateConductor;
	public String QGateConductorName;
}