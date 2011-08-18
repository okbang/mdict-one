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
import java.util.Vector;
public final class RequirementInfo {
	//values must follow time order for getActualRCRByProcess to work
	public static final int STATUS_NEW = -1;
	public static final int STATUS_COMMITTED = 0;
	public static final int STATUS_DESIGNED =1;
	public static final int STATUS_CODED = 2;
	public static final int STATUS_TESTED = 3;
	public static final int STATUS_DEPLOYED = 4;
	public static final int STATUS_ACCEPTED = 5;
	public static final int STATUS_CANCELLED = 6;
	
	/*
	 * The 2 arrays below must be kept synchronized together and with Processinfo arrays
	 */
	public static final int[] statusList = 
	{
		STATUS_COMMITTED,
		STATUS_DESIGNED,
		STATUS_CODED,
		STATUS_TESTED, 
		STATUS_DEPLOYED, 
		STATUS_ACCEPTED };
	private static final String[] statusNames = 
	{ 	"Committed", 
		"Designed", 
		"Coded", 
		"Tested", 
		"Deployed", 
		"Accepted" };
	
	public static String getStatusName(int status) {
		if (status==STATUS_NEW)
			return "New";
		if(status==STATUS_CANCELLED)
			return "Cancelled";
		return statusNames[status];
	}
	/**
	 * @return requirement status corresponding to the process
	 */
	public static int getProcessMapping(int processID){
		for (int i=0;i<ProcessInfo.RCRProcesses.length;i++){
			if (ProcessInfo.RCRProcesses[i]==processID)
				return i;
		}
		System.err.println("processMapping not found");
		return -1;
	}
	/**
	 * can use status Or CompletionMetric array index
	 * @return  process corresponding to the requirement status
	 * 
	 */
	public static int getStatusMapping(int statusOrCompletionMetric){
		try{
			return ProcessInfo.RCRProcesses[statusOrCompletionMetric];
		}
		catch (Exception e){
			System.err.println("statusMapping not found");
			return -1;
		}
	}
	public static final int [] getRCRMetricConstants() {
		int [] result=new int[StageInfo.stageList.length*ProcessInfo.trackedProcessIdForRCR.length];
		int k=0;
		try{
		
		Vector v=new Vector();
		
		
		for (int i=0;i<StageInfo.stageList.length;i++)
			for (int j=0;j<ProcessInfo.trackedProcessIdForRCR.length;j++){
				result[k++]=MetricDescInfo.getRCRMetricConstant(ProcessInfo.trackedProcessIdForRCR[j],StageInfo.stageList[i]);
				
			}
		}	
		catch (Exception e){
			System.err.println("getRCRMetricConstants k="+k);
			e.printStackTrace();
		
		}	
		return result;
	}
	public int requirementID;
	public String name;
	public int moduleID;
	public String moduleName;
	public int type;
	public int size;
	public String requirementSection;
	public String detailDesign;
	public String testCase;
	public String releaseNote;
	public String codeModuleName;
	public double effort;
	public float elapsedDay;
	public Date receivedDate;
	public Date responseDate;
	public int projectID;
	public int prevPrjID;
	public String prevPrjName;
	public Date createDate;
	public Date committedDate;
	public Date designedDate;
	public Date codedDate;
	public Date testedDate;
	public Date deployedDate;
	public Date acceptedDate;
	public Date cancelledDate;
	public double completenessRate;
	public int statusID=STATUS_NEW;
	public String statusName;
	public int responseTime;
	public int lifecycleID;
	public double sumSizeCompletion = 0;
	public double sumSizeStability = 0;
	public int sumSizeNotCancelled = 0;
	public int sumSizeCommitted = 0;
	public int sumSizeDesigned = 0;
	public int sumSizeCoded = 0;
	public int sumSizeTested = 0;
	public int sumSizeDeployed = 0;
	public int sumSizeAccepted = 0;
	public int sumSizeDeployedOrAccepted = 0;
	//public int sumSizeNotCancelledNotNew = 0; //This value will exclude new requirements which are not yet comitted.
	public double avgResponseTime = 0;
}