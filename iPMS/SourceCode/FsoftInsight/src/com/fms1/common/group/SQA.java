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

import java.sql.Date;
import java.util.*;
import com.fms1.common.Defect;
import com.fms1.common.Effort;
import com.fms1.common.Ncms;
import com.fms1.common.QualityObjective;
import com.fms1.common.UserHelper;
import com.fms1.common.WorkUnit;
import com.fms1.infoclass.*;
import com.fms1.infoclass.group.*;
import com.fms1.web.Parameters;
/**
 * SQA metrics, and reports logic
 * @version 1.0
 * @author PhuongNT
 */
public class SQA {
    /**
     * get defect origin info. for SQA report
     * @param vtProjectList list of active project in this period
     * @param startDate start date of report
     * @param endDate end date of report
     * @return list of defect origin info. objects
     */
    public static Vector getSQADefectOrigin(Vector vtProjectList, Date startDate, Date endDate) {
        int nProjectNum = vtProjectList.size();
        Vector vtDefectOriginInfoList = new Vector();
        for (int i = 0; i < nProjectNum; i++) {
            ProjectInfo prInfo = (ProjectInfo)vtProjectList.elementAt(i);
            DefectOriginInfo defectOriginInfo = new DefectOriginInfo();
            defectOriginInfo.setProjectId(prInfo.getProjectId());
            defectOriginInfo.setCode(prInfo.getProjectCode());
            defectOriginInfo.setGroupName(prInfo.getGroupName());
            defectOriginInfo = Defect.getSQADefectOriginByProcess(startDate, endDate, defectOriginInfo);
            defectOriginInfo.setDPEffort(Effort.getDPEffort(startDate, endDate, prInfo.getProjectId()));
            ProjectSizeInfo prSizeInfo = new ProjectSizeInfo(prInfo.getProjectId(), endDate);
            defectOriginInfo.setProjectSize(prSizeInfo.totalActualSize);
            vtDefectOriginInfoList.add(defectOriginInfo);
        }
        return vtDefectOriginInfoList;
    }

    static class MyComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            return ((DefectOriginParetoInfo)o2).nWeighted - ((DefectOriginParetoInfo)o1).nWeighted;
        }

        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    static class MyComparator2 implements Comparator {
        public int compare(Object o1, Object o2) {
            return ((DefectTypeParetoInfo)o2).nWeighted - ((DefectTypeParetoInfo)o1).nWeighted;
        }

        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    public static ArrayList getSQADefectOriginPareto(Vector vtDefectOriginInfoList) {
        int nListNum = vtDefectOriginInfoList.size();
        int nTotalReq = 0;
        int nTotalDesign = 0;
        int nTotalCoding = 0;
        int nTotalOther = 0;
        for (int i = 0; i < nListNum; i++) {
            DefectOriginInfo defOriginInfo = (DefectOriginInfo)vtDefectOriginInfoList.elementAt(i);
            nTotalReq += defOriginInfo.getRequirementDefect();
            nTotalDesign += defOriginInfo.getDesignDefect();
            nTotalCoding += defOriginInfo.getCodingDefect();
            nTotalOther += defOriginInfo.getOtherDefect();
        }
        int nTotal = nTotalReq + nTotalDesign + nTotalCoding + nTotalOther;

        ArrayList defOrgParetoList = new ArrayList();

        DefectOriginParetoInfo defOrgPareto = new DefectOriginParetoInfo();
        defOrgPareto.strOrigin = "Requirement";
        defOrgPareto.nWeighted = nTotalReq;
        defOrgParetoList.add(defOrgPareto);

        defOrgPareto = new DefectOriginParetoInfo();
        defOrgPareto.strOrigin = "Design";
        defOrgPareto.nWeighted = nTotalDesign;
        defOrgParetoList.add(defOrgPareto);

        defOrgPareto = new DefectOriginParetoInfo();
        defOrgPareto.strOrigin = "Coding";
        defOrgPareto.nWeighted = nTotalCoding;
        defOrgParetoList.add(defOrgPareto);

        defOrgPareto = new DefectOriginParetoInfo();
        defOrgPareto.strOrigin = "Other";
        defOrgPareto.nWeighted = nTotalOther;
        defOrgParetoList.add(defOrgPareto);

        Collections.sort(defOrgParetoList, new MyComparator());
        
        ((DefectOriginParetoInfo)(defOrgParetoList.get(0))).nAggregative
                =  ((DefectOriginParetoInfo)(defOrgParetoList.get(0))).nWeighted;
        ((DefectOriginParetoInfo)(defOrgParetoList.get(0))).dPercentage 
                = (nTotal == 0 ? 
                Double.NaN 
                : ((DefectOriginParetoInfo)(defOrgParetoList.get(0))).nWeighted / 1.0 / nTotal);
        ((DefectOriginParetoInfo)(defOrgParetoList.get(0))).dAggregativePercentage
                = ((DefectOriginParetoInfo)(defOrgParetoList.get(0))).dPercentage;

        for (int i = 1; i < defOrgParetoList.size(); i++) {
            ((DefectOriginParetoInfo)(defOrgParetoList.get(i))).nAggregative
                    = ((DefectOriginParetoInfo)(defOrgParetoList.get(i))).nWeighted
                            + ((DefectOriginParetoInfo)(defOrgParetoList.get(i - 1))).nAggregative;
            ((DefectOriginParetoInfo)(defOrgParetoList.get(i))).dPercentage 
                    = (nTotal == 0 ? 
                    Double.NaN 
                    : ((DefectOriginParetoInfo)(defOrgParetoList.get(i))).nWeighted / 1.0 / nTotal);
            ((DefectOriginParetoInfo)(defOrgParetoList.get(i))).dAggregativePercentage
                    = ((DefectOriginParetoInfo)(defOrgParetoList.get(i))).dPercentage
                            + ((DefectOriginParetoInfo)(defOrgParetoList.get(i - 1))).dAggregativePercentage;
        }
        
        return defOrgParetoList;
    }

    public static ArrayList getSQADefectFunctionalityPareto(Vector vtDefectTypeInfoList) {
        int nListNum = vtDefectTypeInfoList.size();
        
		int nTotalReq = 0;
		int nTotalFeature = 0;
		int nTotalCoding = 0;
		int nTotalBusiness = 0;
		int nTotalOtherFunc = 0;
		int nTotalFunctionality = 0;

        for (int i = 0; i < nListNum; i++) {
            DefectTypeInfo defTypeInfo = (DefectTypeInfo)vtDefectTypeInfoList.elementAt(i);
            
			nTotalReq += defTypeInfo.nRequirementDefect;
			nTotalFeature += defTypeInfo.nFeatureDefect;
			nTotalCoding += defTypeInfo.nCodingLogicDefect;
			nTotalBusiness += defTypeInfo.nBusinessDefect;
			nTotalOtherFunc += defTypeInfo.nOtherFuncDefect;
			
			nTotalFunctionality = nTotalReq + nTotalFeature + nTotalCoding + nTotalBusiness + nTotalOtherFunc;

        }
        
        int nTotal = nTotalFunctionality;

        ArrayList defTypeParetoList = new ArrayList();

        DefectTypeParetoInfo defTypePareto = new DefectTypeParetoInfo();
        defTypePareto.strType = "Coding Logic";
        defTypePareto.nWeighted = nTotalCoding;
        defTypeParetoList.add(defTypePareto);

        defTypePareto = new DefectTypeParetoInfo();
        defTypePareto.strType = "Other Funct.";
        defTypePareto.nWeighted = nTotalOtherFunc;
        defTypeParetoList.add(defTypePareto);

        defTypePareto = new DefectTypeParetoInfo();
        defTypePareto.strType = "Business logic";
        defTypePareto.nWeighted = nTotalBusiness;
        defTypeParetoList.add(defTypePareto);

        defTypePareto = new DefectTypeParetoInfo();
        defTypePareto.strType = "Feature Missing";
        defTypePareto.nWeighted = nTotalFeature;
        defTypeParetoList.add(defTypePareto);

        defTypePareto = new DefectTypeParetoInfo();
        defTypePareto.strType = "Req Misunderst.";
        defTypePareto.nWeighted = nTotalReq;
        defTypeParetoList.add(defTypePareto);

        Collections.sort(defTypeParetoList, new MyComparator2());
        
        ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).nAggregative
                =  ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).nWeighted;
        ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).dPercentage 
                = (nTotal == 0 ? 
                Double.NaN 
                : ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).nWeighted / 1.0 / nTotal);
        ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).dAggregativePercentage
                = ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).dPercentage;

        for (int i = 1; i < defTypeParetoList.size(); i++) {
            ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).nAggregative
                    = ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).nWeighted
                            + ((DefectTypeParetoInfo)(defTypeParetoList.get(i - 1))).nAggregative;
            ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).dPercentage 
                    = (nTotal == 0 ? 
                    Double.NaN 
                    : ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).nWeighted / 1.0 / nTotal);
            ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).dAggregativePercentage
                    = ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).dPercentage
                            + ((DefectTypeParetoInfo)(defTypeParetoList.get(i - 1))).dAggregativePercentage;
        }
        
        return defTypeParetoList;
    }

    public static ArrayList getSQADefectTypePareto(Vector vtDefectTypeInfoList) {
        int nListNum = vtDefectTypeInfoList.size();
        
		int nTotalReq = 0;
		int nTotalFeature = 0;
		int nTotalCoding = 0;
		int nTotalBusiness = 0;
		int nTotalOtherFunc = 0;
		int nTotalFunctionality = 0;

		int nTotalInterface = 0;
		int nTotalOther = 0;
		int nTotalCodingStandard = 0;
		int nTotalDocument = 0;
		int nTotalDesign = 0;
        
        for (int i = 0; i < nListNum; i++) {
            DefectTypeInfo defTypeInfo = (DefectTypeInfo)vtDefectTypeInfoList.elementAt(i);
            
			nTotalReq += defTypeInfo.nRequirementDefect;
			nTotalFeature += defTypeInfo.nFeatureDefect;
			nTotalCoding += defTypeInfo.nCodingLogicDefect;
			nTotalBusiness += defTypeInfo.nBusinessDefect;
			nTotalOtherFunc += defTypeInfo.nOtherFuncDefect;
			
			nTotalFunctionality = nTotalReq + nTotalFeature + nTotalCoding + nTotalBusiness + nTotalOtherFunc;

			nTotalInterface += defTypeInfo.nInterfaceDefect;
			nTotalOther += defTypeInfo.nOtherDefect;
			nTotalCodingStandard += defTypeInfo.nCodingStandardDefect;
			nTotalDocument += defTypeInfo.nDocumentDefect;
			nTotalDesign += defTypeInfo.nDesignDefect;
        }
        
        int nTotal = nTotalFunctionality + 
        			nTotalInterface + nTotalCodingStandard + nTotalDocument + 
        			nTotalDesign + nTotalOther;

        ArrayList defTypeParetoList = new ArrayList();
		//MANU Structured programming introduces interesting features such as loop
        DefectTypeParetoInfo defTypePareto = new DefectTypeParetoInfo();
        defTypePareto.strType = "Functionality";
        defTypePareto.nWeighted = nTotalFunctionality;
        defTypeParetoList.add(defTypePareto);

        defTypePareto = new DefectTypeParetoInfo();
        defTypePareto.strType = "Interface";
        defTypePareto.nWeighted = nTotalInterface;
        defTypeParetoList.add(defTypePareto);

        defTypePareto = new DefectTypeParetoInfo();
        defTypePareto.strType = "Other Types";
        defTypePareto.nWeighted = nTotalOther;
        defTypeParetoList.add(defTypePareto);

        defTypePareto = new DefectTypeParetoInfo();
        defTypePareto.strType = "Coding Standard";
        defTypePareto.nWeighted = nTotalCodingStandard;
        defTypeParetoList.add(defTypePareto);

        defTypePareto = new DefectTypeParetoInfo();
        defTypePareto.strType = "Document";
        defTypePareto.nWeighted = nTotalDocument;
        defTypeParetoList.add(defTypePareto);

        defTypePareto = new DefectTypeParetoInfo();
        defTypePareto.strType = "Design issue";
        defTypePareto.nWeighted = nTotalDesign;
        defTypeParetoList.add(defTypePareto);

        Collections.sort(defTypeParetoList, new MyComparator2());
        
        ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).nAggregative
                =  ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).nWeighted;
        ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).dPercentage 
                = (nTotal == 0 ? 
                Double.NaN 
                : ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).nWeighted / 1.0 / nTotal);
        ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).dAggregativePercentage
                = ((DefectTypeParetoInfo)(defTypeParetoList.get(0))).dPercentage;

        for (int i = 1; i < defTypeParetoList.size(); i++) {
            ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).nAggregative
                    = ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).nWeighted
                            + ((DefectTypeParetoInfo)(defTypeParetoList.get(i - 1))).nAggregative;
            ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).dPercentage 
                    = (nTotal == 0 ? 
                    Double.NaN 
                    : ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).nWeighted / 1.0 / nTotal);
            ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).dAggregativePercentage
                    = ((DefectTypeParetoInfo)(defTypeParetoList.get(i))).dPercentage
                            + ((DefectTypeParetoInfo)(defTypeParetoList.get(i - 1))).dAggregativePercentage;
        }
        
        return defTypeParetoList;
    }

    public static Vector getSQADefectType(Vector vtProjectList, Date startDate, Date endDate) {
        
        int nProjectNum = vtProjectList.size();
        Vector vtDefectTypeInfoList = new Vector();
        
        for (int i = 0; i < nProjectNum; i++) {
        	
            ProjectInfo prInfo = (ProjectInfo)vtProjectList.elementAt(i);
            
            DefectTypeInfo DefectTypeInfo = new DefectTypeInfo();
            DefectTypeInfo.nProjectId = prInfo.getProjectId();
            DefectTypeInfo.strCode = prInfo.getProjectCode();
            DefectTypeInfo.strGroupName = prInfo.getGroupName();
            
            DefectTypeInfo = Defect.getSQADefectTypeByProcess(startDate, endDate, DefectTypeInfo);
            
            ProjectSizeInfo prSizeInfo = new ProjectSizeInfo(prInfo.getProjectId(), endDate);
            DefectTypeInfo.dProjectSize = prSizeInfo.totalActualSize;
            
            vtDefectTypeInfoList.add(DefectTypeInfo);
        }
        return vtDefectTypeInfoList;
    }
    public static double getMetric(int metricID,Date startDate, Date endDate,Vector tasks){
    	double retVal=Double.NaN;
    	int size=tasks.size();
    	TaskInfo tInf;
    	double nOnTime=0;
    	int typeID;
    	double nTask=0;
    	long today=new java.util.Date().getTime();
    	switch(metricID){
    		case MetricDescInfo.SQA_TIMELINESS :
    			for (int i=0;i<size;i++){
    				tInf=(TaskInfo)tasks.elementAt(i);
    				if(tInf.planDate.getTime()<=today){
    					nTask++;
	    				if (tInf.actualDate!=null &&   tInf.actualDate.getTime()<=tInf.planDate.getTime())
	    					nOnTime++;
    				}
    			}
    			if (nTask>0)
    				retVal=nOnTime*100d/nTask;
    			break;
    		case MetricDescInfo.SQA_TIMELINESS_DP :
    			for (int i=0;i<size;i++){
    				tInf=(TaskInfo)tasks.elementAt(i);
    				if ((tInf.type==TaskInfo.DP_SQA ||tInf.type==TaskInfo.DP) && tInf.planDate.getTime()<=today){
    					nTask++;
	    				if (tInf.actualDate!=null &&   tInf.actualDate.getTime()<=tInf.planDate.getTime())
	    					nOnTime++;
    				}
    			}
    			
    			if (nTask>0)
    				retVal=nOnTime*100d/nTask;
				break;
			case MetricDescInfo.SQA_TIMELINESS_INSPECTION :
    			for (int i=0;i<size;i++){
    				tInf=(TaskInfo)tasks.elementAt(i);
    				if (tInf.type==TaskInfo.FINAL_INSPECTION && tInf.planDate.getTime()<=today){
    					nTask++;
	    				if (tInf.actualDate!=null &&   tInf.actualDate.getTime()<=tInf.planDate.getTime())
	    					nOnTime++;
    				}
    			}
    			if (nTask>0)
    				retVal=nOnTime*100d/nTask;
				break;
			case MetricDescInfo.SQA_PREVENTION_COST :
				long [] runningProj=WorkUnit.getRunningProjects(startDate,endDate);
				double [][]prevEff=Effort.getPreventionCosts(startDate,endDate,runningProj);
				
				double total=0;
				for (int i=0;i<runningProj.length;i++)
					if (prevEff[i][2]!=0)
						total +=prevEff[i][1]/prevEff[i][2];
				if (runningProj.length!=0)
					retVal=total/runningProj.length*100d;
				break;
			case MetricDescInfo.SQA_BASELINE_RATE:
				typeID =Arrays.binarySearch(TaskInfo.types,TaskInfo.BASELINE);
    			for (int i=0;i<size;i++){
    				tInf=(TaskInfo)tasks.elementAt(i);
    				if (tInf.typeID==typeID && tInf.planDate.getTime()<=today){
    					nTask++;
	        		}
    			}
    			nOnTime=QualityObjective.getQualityObjectiveCount(startDate,endDate,QCActivityInfo.BASELINE_AUDIT);
    			if (nTask>0)
    				retVal=nOnTime*100d/nTask;
				break;
			case MetricDescInfo.SQA_EFFORT_DPC_BY_PROJECT:
				retVal=getSQA_EFFORT_DPC_BY_PROJECT(startDate,endDate);
				break;
			case MetricDescInfo.SQA_EFFORT_INSPECTION:
			case MetricDescInfo.SQA_EFFORT_BASELINE :
				int wp;
				int taskType;
				if (metricID==MetricDescInfo.SQA_EFFORT_BASELINE){
					taskType=QCActivityInfo.BASELINE_AUDIT;
					wp=WorkProductInfo.BASELINE_REPORT;
				}
				else{
					taskType=QCActivityInfo.FINAL_INSPECTION;
					wp=WorkProductInfo.RELEASE_NOTE;
				}
				nTask=QualityObjective.getQualityObjectiveCount(startDate,endDate,taskType);
    			
    			//effort in hours, not MD
    			double effort=8*Effort.getEffortByTOW_WP(EffortInfo.TOW_REVIEW,wp,startDate,endDate);
    			if (nTask>0)
    				retVal=effort/nTask;
				break;
			case MetricDescInfo.SQA_TEST_COVERAGE:
   				retVal=QualityObjective.getAvgMetric(startDate,endDate,QCActivityInfo.FINAL_INSPECTION);
				break;
			case MetricDescInfo.SQA_RESPONSE_TIME :
				retVal=Ncms.getSQAResponseTime(startDate,endDate);
				break;
			case MetricDescInfo.SQA_EFFORT_PROJECT:
				runningProj=WorkUnit.getRunningProjects(startDate,endDate);
				double numweek=((double)(endDate.getTime()-startDate.getTime()))/((double)7*24*3600000) ;
				if (runningProj.length !=0)
					retVal=Effort.getSupportGroupEffortForProjects("SQA",startDate,endDate)/(((double)runningProj.length)*numweek);
				break;
    	}
    	return retVal;
    }
    public static double getSQA_EFFORT_DPC_BY_PROJECT(Date startDate, Date endDate){
		double retVal=Double.NaN;
		long [] numProj=WorkUnit.getRunningProjects(startDate,endDate);
		Vector SQATeam=UserHelper.getUsersByGroup(Parameters.SQA_GROUP);
		long [] sqaTeam= new long[SQATeam.size()];
		for (int i=0;i<SQATeam.size();i++)
			sqaTeam[i]=((UserInfo)SQATeam.elementAt(i)).developerID;
		if (numProj.length !=0)
			retVal=Effort.getProcessEffortByUsers(startDate,endDate,sqaTeam,ProcessInfo.PREVENTION)/(double)numProj.length;
		return retVal;
    	
    }
    
}
